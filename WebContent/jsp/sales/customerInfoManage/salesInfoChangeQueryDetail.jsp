<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.TtDealerActualSalesAuditPO"%>
<%@ page import="com.infodms.dms.po.TtCustomerEditLogPO"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%
	String contextPath = request.getContextPath();
    List linkManList = (List)request.getAttribute("linkManList");  		//其他联系人
	String fleet_name = (String)request.getAttribute("fleetName");		//集团客户名称
	String contract_no = (String)request.getAttribute("contractNo");	//集团客户合同名称
	if(null == fleet_name || "".equals(fleet_name)){
		fleet_name = " ";
		contract_no = " "; 
	}
	Map<String,Object> vehicleInfo = (Map<String,Object>)request.getAttribute("vehicleInfo");
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("lists");
	String vehicleId = vehicleInfo.get("VEHICLE_ID").toString();
	String isOldCtm = "";
	String orderId = "";
	if(null != vehicleInfo && vehicleInfo.size()>0){
		isOldCtm = vehicleInfo.get("IS_OLD_CTM")+"";//保存后：新/老客户
		orderId = vehicleInfo.get("ORDER_ID")+"";   //实销ID
	}
	
	TtDealerActualSalesAuditPO salesInfo = (TtDealerActualSalesAuditPO)request.getAttribute("salesAuditInfo");//车辆实销信息
	int isFleet = 0;
	int IsOldCtm = 0;
	int IsFleet = 0;
	Long ctmId_Old = new Long(0);
	if(null != salesInfo && !"".equals(salesInfo)){
		isFleet = salesInfo.getIsFleet();	 //是否是集团客户
		ctmId_Old = salesInfo.getCtmId();	 //客户ID:如果客户id不为0，说明已做了“保存”，页面点击“保存按钮”，后台进行修改操作，否则新增
		IsOldCtm = salesInfo.getIsOldCtm();  //用户保存时的新/老客户
	}
	TtCustomerEditLogPO customerInfo = (TtCustomerEditLogPO)request.getAttribute("cusInfo");//客户信息
	Long province__ = new Long(0);			  //省
	Long city__ = new Long(0);	  			  //市
	Long town__ = new Long(0);	  			  //县
	int ctmType = 0;			  			  //客户类型
	int s_ctmType = Constant.CUSTOMER_TYPE_02;//客户类型：公司客户
	int yes = Constant.IF_TYPE_YES;			  //"是"
	String oldflag = Constant.IS_OLD_CTM_02+"";
	if(null != customerInfo && !"".equals(customerInfo)){
		province__ = customerInfo.getProvince();
		city__ = customerInfo.getCity();
		town__ = customerInfo.getTown();
		ctmType = customerInfo.getCtmType();
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
		var ctmType = '<%=ctmType%>';
		var s_ctmType = '<%=s_ctmType%>';
		if(ctmType == s_ctmType){
			document.getElementById("company_table").style.display = "table-row";
			document.getElementById("customerInfoId").style.display = "none";
		}
		var IsOldCtm = '<%=IsOldCtm%>';
		if(IsOldCtm == '<%=oldflag%>'){
			document.getElementById("sel_cus_type_old").checked=true;
			showCustSel(IsOldCtm);
		}
		
		var isFleet = '<%=isFleet%>';//是否是集团客户
		var yes = '<%=yes%>';
		if(isFleet == yes){
			document.getElementById("is_fleet_show").style.display = "table-row";
		}
		changeMortgageType('${salesAuditInfo.payment}');
	}
//选择购置方式之后执行的代码
	function changeMortgageType(value){
		//var mg_type = 10361003;//按揭
		//一次性付款
		if(value =="10361002"){
			document.getElementById("MORTGAGE_TYPE").style.display = "none";
			document.getElementById("Loans1").style.display = "none";
			document.getElementById("Loans2").style.display = "none";
			document.getElementById("Loans3").style.display = "none";
			document.getElementById("changeVicle").style.display="none";
			//按揭		
		}else if(value=="10361003"){
			document.getElementById("MORTGAGE_TYPE").style.display = "table-row";
			document.getElementById("Loans1").style.display = "table-row";
			document.getElementById("Loans2").style.display = "table-row";
			document.getElementById("Loans3").style.display = "table-row";
			document.getElementById("changeVicle").style.display="none";
		//置换	
		}else if(value=="10361004"){
			document.getElementById("MORTGAGE_TYPE").style.display = "none";
			document.getElementById("Loans1").style.display = "none";
			document.getElementById("Loans2").style.display = "none";
			document.getElementById("Loans3").style.display = "none";
			document.getElementById("changeVicle").style.display="table-row";
			//置换转按揭
		}else{
			document.getElementById("MORTGAGE_TYPE").style.display = "table-row";
			document.getElementById("Loans1").style.display = "table-row";
			document.getElementById("Loans2").style.display = "table-row";
			document.getElementById("Loans3").style.display = "table-row";
			document.getElementById("changeVicle").style.display="table-row";
		}
	}	
	//已进行保存，查询保存后的其他联系人信息
	function queryOtherLinkMan(value){
		var sum = document.getElementById("iframe");
		var ctmId_Old = document.getElementById("ctmId_Old").value;//保存后的客户id
		var oldCustomerId = document.getElementById("oldCustomerId").value;//用户选择的老客户id
		var sel_cus_type = document.getElementsByName("sel_cus_type");
		var isOldCtm = '<%=isOldCtm%>';
		var firstCheck = 0;
		for(var i=0; i < sel_cus_type.length; i++){
			if(sel_cus_type[i].checked){
				firstCheck = sel_cus_type[i].value;
			}
		}
		var id = 0;
		if(0 != ctmId_Old && "" != ctmId_Old){
			id = ctmId_Old;
		}else{
			id = oldCustomerId;
		}
		if(isOldCtm != firstCheck){
			id = 0;
		}
		if(value){
			id = value;
		}
		sum.src = "<%=contextPath%>/sales/customerInfoManage/SalesReport/queryOtherLinkMan.do?ctmId_Old="+id;
	}
	function changeCustomerType(value){
		var ctm_type = <%=Constant.CUSTOMER_TYPE_01%>;//个人客户
		if(value == ctm_type){
			document.getElementById("company_table").style.display = "none";
		}else{
			document.getElementById("company_table").style.display = "table-row";
		}
	}	
	function is_fleet_Sel(){
		document.getElementById("is_fleet_show").style.display = "table-row";
		document.getElementById("company_table").style.display = "none";
		document.getElementById("ctm_table_id").style.display = "none";
		//document.getElementById("ctm_table_id_2").style.display = "none";
		document.getElementById("iframediv").style.display = "none";
		document.getElementById("select_ctm_type_id").disabled =true ;
		document.getElementById("select_ctm_type_id_1").disabled =true ;
	}
	//得到iframe信息，验证其他联系人填写是否正确
	function check_Link(){
		
		var check_Link_flag = true;
		var ifr_window=window.frames["iframe"];
		var linkMan_names = ifr_window.getlinkMan_names();
		var linkMan_main_phones = ifr_window.getlinkMan_main_phones();
		var linkMan_other_phones = ifr_window.getlinkMan_other_phones();
		var linkMan_contract_reasons = ifr_window.getlinkMan_contract_reasons();
		var tab = document.getElementById("submitTable");
		if(linkMan_names){
			if(linkMan_names.length>0){
				for(var i=0; i<linkMan_names.length;i++){
					if(linkMan_names[i].value.replace(/(^\s*)|(\s*$)/g, "")!="" && linkMan_main_phones[i].value.replace(/(^\s*)|(\s*$)/g, "")==""){
						MyAlert("请填写主要联系电话!");
						check_Link_flag = false;
						return check_Link_flag;
					}
					if(linkMan_names[i].value.replace(/(^\s*)|(\s*$)/g, "")=="" && linkMan_main_phones[i].value.replace(/(^\s*)|(\s*$)/g, "")!=""){
						MyAlert("请填写其他联系人姓名!");
						check_Link_flag = false;
						return check_Link_flag;
					}
					if(linkMan_main_phones[i].value){
						var str = /^[0-9]*$/;
						if(!str.test(linkMan_main_phones[i].value.replace(/(^\s*)|(\s*$)/g, ""))){
							MyAlert("其他联系人信息:\"主要联系电话\"请输入数字!");
							check_Link_flag = false;
							return check_Link_flag;
						}
					}
					if(linkMan_other_phones[i].value){
						var str = /^[0-9]*$/;
						if(!str.test(linkMan_other_phones[i].value.replace(/(^\s*)|(\s*$)/g, ""))){
							MyAlert("其他联系人信息:\"其他联系电话\"请输入数字!");
							check_Link_flag = false;
							return check_Link_flag;
						}
					}
				
				var rowObj = tab.insertRow(tab.rows.length);
				var cell1 = rowObj.insertCell(0);
				var cell2 = rowObj.insertCell(1);
				var cell3 = rowObj.insertCell(2);
				var cell4 = rowObj.insertCell(3);
				cell1.innerHTML = '<td class="center"><div class="center"><input type="hidden" name="linkMan_name" value="'+linkMan_names[i].value+'" /></div></td>';
				cell2.innerHTML = '<td class="center"><div class="center"><input type="hidden" name="linkMan_main_phone" value="'+linkMan_main_phones[i].value+'" /></div></td>';
				cell3.innerHTML = '<td class="center"><div class="center"><input type="hidden" name="linkMan_other_phone" value="'+linkMan_other_phones[i].value+'" /></div></td>';
				cell4.innerHTML = '<td class="center"><div class="center"><input type="hidden" name="linkMan_contract_reason" value="'+linkMan_contract_reasons[i].value+'" /></div></td>';
				}
			}
		}
		return check_Link_flag;
	}

	//判断是否是集团客户
	function check_fleet(){
		var check_fleet_flag = true;
		//判断是否是集团客户
		var is_fleet = document.getElementsByName("is_fleet");
		var fleetValur = 0;
		var yes = '<%=yes%>';
		if(is_fleet){
			for(var i=0; i< is_fleet.length; i++){
				if(is_fleet[i].checked){
					fleetValur = is_fleet[i].value;
				}
			}
		}
		//如果不是集团客户
		if(fleetValur != yes){
			var sel_cus_type = document.getElementsByName("sel_cus_type");
			if(sel_cus_type){
				var oldCustomerId = document.getElementById("oldCustomerId").value;
				var oldflag = '<%=oldflag %>';
				var selvalue = 0;
				for(var i=0;i<sel_cus_type.length;i++){
					if(sel_cus_type[i].checked){
						selvalue = sel_cus_type[i].value;
					}
				}
				if(selvalue == oldflag){
					if(!oldCustomerId){
						MyAlert("请选择老客户");
						check_fleet_flag = false;
						return check_fleet_flag;
					}
				}
			}
		}else{
			var fleet_id = document.getElementById("fleet_id").value;
			var fleet_contract_no_id = document.getElementById("fleet_contract_no_id").value;
			if(fleet_id == 0){
				MyAlert("请选择集团客户");
				check_fleet_flag = false;
				return check_fleet_flag;
			}
			if(fleet_contract_no_id == 0){
				MyAlert("请选择集团客户合同");
				check_fleet_flag = false;
				return check_fleet_flag;
			}
		}
		return check_fleet_flag;
	}
	function saveReport(){
		if(submitForm('fm')){
			if(check_fleet()){		//如果填写其他联系人信息，验证填写是否正确
				if(check_Link()){	   //判断是否是集团客户
					MyConfirm("是否保存?",saveReportAction);
				}
			}
		}
	}
	function saveReportAction(){
		var fsm = document.getElementById("fm");
		fsm.action= "<%=request.getContextPath()%>/sales/customerInfoManage/SalesReport/saveReportInfo.do";
		fsm.submit();
	}
	
	function doReport(){
		if(submitForm('fm')){
			if(check_fleet()){		//如果填写其他联系人信息，验证填写是否正确
				if(check_Link()){	//判断是否是集团客户
					MyConfirm("是否上报?",doReportAction);
				}
			}
		}
	}
	function doReportAction(){
		saveReportAction();
		var vehicleId = '<%=vehicleId%>';
		//return;
		var fsm = document.getElementById("fm");
		fsm.action= "<%=request.getContextPath()%>/sales/customerInfoManage/SalesReport/doReportAction.do?vehicleId="+vehicleId;
		fsm.submit();
	}
	//客户列表
	function showCustomerList(){
		OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/toQueryCtmList.do',800,600);
	}
	//联系人列表
	function showLinkManList(){
		OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/toQueryLinkManList.do',800,600);
	}
	//显示集团客户信息
	function showFleetInfo(fleet_id,fleet_name){
		document.getElementById("fleet_name").value = fleet_name;
		document.getElementById("fleet_id").value = fleet_id;
		document.getElementById("fleet_contract_no").value ="";
		document.getElementById("fleet_contract_no_id").value = "";
	}
	//显示集团客户合同信息
	function showFleetContractInfo(contract_id,contract_no){
		document.getElementById("fleet_contract_no").value = contract_no;
		document.getElementById("fleet_contract_no_id").value = contract_id;
	}

	//显示客户信息
	function showCustomerInfo(ctmId_Old){
		var url = "<%=request.getContextPath()%>/sales/customerInfoManage/SalesReport/getOldCustomerInfo.json";
		makeCall(url,doShowCustomerInfo,{ctmId_Old:ctmId_Old}); 
	}
	function doShowCustomerInfo(json){
		document.getElementById("ctm_name").value=objToStr(json.oldCustomerInfo.ctmName);
		document.getElementById("company_phone").value=objToStr(json.oldCustomerInfo.companyPhone);
		document.getElementById("company_name").value=objToStr(json.oldCustomerInfo.companyName);
		document.getElementById("ctm_form").value=objToStr(json.oldCustomerInfo.ctmForm);
		document.getElementById("customer_type").value=objToStr(json.oldCustomerInfo.ctmType);
		//如果客户类型为10831001，公司客户信息table隐藏
		if(document.getElementById("customer_type").value+"" != '<%=s_ctmType%>'+""){
			document.getElementById("company_table").style.display = "none";
		}
		document.getElementById("job").value=objToStr(json.oldCustomerInfo.job);
		//document.getElementById("ctmId_Old").value=objToStr(json.oldCustomerInfo.ctmId);
		document.getElementById("sex").value=objToStr(json.oldCustomerInfo.sex);
		document.getElementById("is_married").value=objToStr(json.oldCustomerInfo.isMarried);
		document.getElementById("profession").value=objToStr(json.oldCustomerInfo.profession);
		document.getElementById("email").value=objToStr(json.oldCustomerInfo.email);
		document.getElementById("kind").value=objToStr(json.oldCustomerInfo.kind);
		document.getElementById("birthday").value=objToStr(json.oldCustomerInfo.birthday.substring(0,10));
		document.getElementById("company_s_name").value=objToStr(json.oldCustomerInfo.companySName);
		document.getElementById("card_type").value=objToStr(json.oldCustomerInfo.cardType);
		document.getElementById("other_phone").value=objToStr(json.oldCustomerInfo.otherPhone);
		document.getElementById("address").value=objToStr(json.oldCustomerInfo.address);
		var town_=json.oldCustomerInfo.town;
		document.getElementById("main_phone").value=objToStr(json.oldCustomerInfo.mainPhone);
		document.getElementById("vehicle_num").value=objToStr(json.oldCustomerInfo.vehicleNum);
		document.getElementById("industry").value=objToStr(json.oldCustomerInfo.industry);
		document.getElementById("income").value=objToStr(json.oldCustomerInfo.income);
		var province_=json.oldCustomerInfo.province;
		document.getElementById("post_code").value=objToStr(json.oldCustomerInfo.postCode);
		document.getElementById("card_num").value=objToStr(json.oldCustomerInfo.cardNum);
		document.getElementById("level_id").value=objToStr(json.oldCustomerInfo.levelId);
		document.getElementById("education").value=objToStr(json.oldCustomerInfo.education);
		document.getElementById("oldCustomerId").value=objToStr(json.oldCustomerInfo.ctmId);
		var city_=json.oldCustomerInfo.city;

		var s_ctmType = '<%=s_ctmType%>';
		if(json.oldCustomerInfo.ctmType ==s_ctmType){
			document.getElementById("company_table").style.display = "table";
		}
		genLocSel('txt1','txt2','txt3',province_,city_,town_); // 加载省份城市和县

		queryOtherLinkMan(json.oldCustomerInfo.ctmId);
	}
	function showCustSel(status){
		if(status.value == "<%=oldflag%>" || status == "<%=oldflag%>"){
			document.getElementById("showCustomerListID").disabled =false;
			document.getElementById("select_ctm_type_id").disabled =true ;
			document.getElementById("select_ctm_type_id_1").disabled =true ;
			document.getElementById("company_table").disabled =true;
			//document.getElementById("ctm_table_id_2").disabled =true;
			document.getElementById("ctm_name").disabled =true;
			document.getElementById("tcmtd").disabled =true;
			document.getElementById("sex").disabled =true;
			document.getElementById("sextd").disabled =true;
		}else{
			document.getElementById("showCustomerListID").disabled =true;
			document.getElementById("select_ctm_type_id").disabled =false;
			document.getElementById("select_ctm_type_id_1").disabled =false;
			document.getElementById("company_table").disabled =false;
			//document.getElementById("ctm_table_id_2").disabled =false;
			document.getElementById("ctm_name").disabled =false;
			document.getElementById("tcmtd").disabled =false;
			document.getElementById("sex").disabled =false;
			document.getElementById("sextd").disabled =false;
		}
		queryOtherLinkMan("");
	}
	function objToStr(itemValue){
		itemValue = itemValue==null?"":itemValue;
		itemValue = itemValue=='null'?"":itemValue;
		return itemValue;
	}
</script>

<title>实销信息更改查询</title>
</head>
<body onunload='javascript:destoryPrototype();'>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 &gt; 客户信息管理 &gt; 实销信息更改查询</div>
<form id="fm" name="fm" method="post"><input type="hidden" name="curPage" id="curPage" value="1" /> <input type="hidden" id="dlrId" name="dlrId" value="" />
<table class="table_query table_list" class="center">
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />修改说明</th>
	</tr>
	<tr>
		<td width="15%" class=right>修改说明：</td>
		<td class=left>${cusInfo.editRemark }</td>
	</tr>
</table>
<table class="table_query table_list" class="center">
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />审批结果</th>
	</tr>
	<tr>
		<td width="15%" class=right>审批结果：</td>
		<td width="85%" class="left"><script>document.write(getItemValue(${salesAuditInfo.status}));</script>		</td>
	</tr>
	<tr>
		<td width="15%" class=right>审批意见：</td>
		<td width="85%" class="left">${salesAuditInfo.checkRamark }</td>
	</tr>
</table>
<table class="table_query table_list" class=center>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />车辆资料</th>
	</tr>
	<tr>
		<td width="15%" class=right>VIN：</td>
		<td class=left>${vehicleInfo.VIN }</td>
		<td class=right>发动机号：</td>
		<td class=left>${vehicleInfo.ENGINE_NO }</td>
	</tr>
	<tr>
		<td class=right>车系：</td>
		<td class=left>${vehicleInfo.SERIES_NAME }</td>
		<td class=right>车型：</td>
		<td class=left>${vehicleInfo.MODEL_NAME }</td>
	</tr>
	<tr>
		<td class=right>物料代码：</td>
		<td class=left>${vehicleInfo.MATERIAL_CODE }</td>
		<td class=right>物料名称：</td>
		<td class=left>${vehicleInfo.MATERIAL_NAME }</td>
	</tr>
	<tr>
		<td class=right>颜色：</td>
		<td class=left>${vehicleInfo.COLOR }</td>
		<td class=right>&nbsp;</td>
		<td class=left>&nbsp;</td>
	</tr>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />销售信息</th>
	</tr>
	<tr>
		<td class="right">车牌号：</td>
		<td width="35%" class="left">${salesAuditInfo.vehicleNo}
		</td>
		<td width="15%" class="right">合同编号：</td>
		<td width="35%" class="left">${salesAuditInfo.contractNo}</td>
	</tr>
	<tr>
		<td class="right">开票日期：</td>
		<td class="left">
		<%
			if(null != salesInfo.getInvoiceDate() && !"".equals(salesInfo.getInvoiceDate())){
				out.print(CommonUtils.printDate(salesInfo.getInvoiceDate()));
			}	
		%>	 
		</td>
		<td class="right">发票编号：</td>
		<td class="left">${salesAuditInfo.invoiceNo }</td>
	</tr>
	<tr>
		<td class="right">保险公司：</td>
		<td class="left">${salesAuditInfo.insuranceCompany }</td>
		<td class="right">保险日期：</td>
		<td class="left">
		<%
			if(null != salesInfo.getInvoiceDate() && !"".equals(salesInfo.getInvoiceDate())){
				out.print(CommonUtils.printDate(salesInfo.getInsuranceDate()));
			}	
		%>	 	
		</td>
	</tr>
	<tr>
		<td class="right">车辆交付日期：</td>
		<td class="left">
		<%
			if(null != salesInfo.getInvoiceDate() && !"".equals(salesInfo.getInvoiceDate())){
				out.print(CommonUtils.printDate(salesInfo.getConsignationDate()));
			}	
		%>		
		</td>
		<td class="right">交付时公里数：</td>
		<td class="left">${salesAuditInfo.miles }</td>
	</tr>
	<tr>
		<td class="right">付款方式：</td>
		<td class="left" >
		<script>document.write(getItemValue(${salesAuditInfo.payment}));</script>	
		</td>
		<td class="right">价格：</td>
		<td class="left">
			${salesAuditInfo.price }
		</td>
	</tr>
	<tr id="MORTGAGE_TYPE" >
		<td class="right">按揭类型：</td>
		<td class="left">
		<script>document.write(getItemValue(${salesAuditInfo.mortgageType}));</script>
		</td>
		<td class="right">首付比例：</td>
		<td class="left">${salesAuditInfo.shoufuRatio}%</td>
		
	</tr>
	<tr id="Loans1" >
			<td class="right">贷款方式：</td>
			<td class="left">
			<script>document.write(getItemValue(${salesAuditInfo.loansType}));</script>
			</td>
			<td class="right">按揭银行：</td>
			<td class="left">
			<script>document.write(getItemValue(${salesAuditInfo.bank}));</script>
			</td>
	</tr>
	<tr id="Loans2" >
			<td class="right">贷款金额：</td>
			<td class="left"><label>${salesAuditInfo.money}</label></td>
			<td class="right">贷款年限：</td>
			<td class="left">${salesAuditInfo.loansYear}</td>
	</tr>
	<tr id="Loans3" >
			<td class="right">利率：</td>
			<td class="left"><label>${salesAuditInfo.lv}%</label></td>
			<td class="right"></td>
			<td class="left"></td>
	</tr>
	<tr id="changeVicle" >
		<td class="right">本品牌置换：</td>
		<td class="left">
			<script>document.write(getItemValue(${salesAuditInfo.thischange}));</script>
		</td>
		<td class="right">其他品牌置换：</td>
		<td class="left">${salesAuditInfo.loanschange}</td>
	</tr>
	<tr>
		<td class="right">备注：</td>
		<td colspan="2" class="left">${salesAuditInfo.memo }</td>
		<td class="left">&nbsp;</td>
	</tr>
</table>
<table class="table_query table_list" class="center" id="customer_type_table">
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />客户类型</th>
	</tr>
	<tr>
		<td width="15%" class=right id="select_ctm_type_id">客户类型：</td>
		<td width="35%" class="left" id="select_ctm_type_id_1">
		<script>document.write(getItemValue(${cusInfo.ctmType}));</script>			
		</td>
		<td width="15%" class="right"></td>
		<td width="35%" class="left">
			<!-- 是否集团客户：<script>document.write(getItemValue(${salesAuditInfo.isFleet}));</script>	 -->	
		</td>
	</tr>
	<tbody id="is_fleet_show" style="display: none;">
		<tr>
			<td class="right">集团客户名称：</td>
			<td class="left">
			<c:if test="${cName!=null}">
			${cName}
			</c:if>
			
				<%
					if(null != fleet_name && !"".equals(fleet_name)){
						out.println(fleet_name);
					}
				%> 
			</td>
			<td class="right">集团客户合同：</td>
			<td class="left">
			<c:if test="${cName!=null}">
			${cName}
			</c:if>
				<%
					if(null != contract_no && !"".equals(contract_no)){
						out.println(contract_no);
					}
				%>
			</td>
		</tr>
	</tbody>
</table>
<table class="table_query table_list" class="center" id="company_table" style="display: none;">
	<tr class="tabletitle">
		<th colspan="4" class="right"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />公司客户信息</th>
	</tr>

	<tr>
		<td width="15%" class="right">公司名称：</td>
		<td width="35%" class="left">${cusInfo.companyName }</td>
		<td width="15%" class="right">公司简称：</td>
		<td width="35%" class="left">${cusInfo.companySName }</td>
	</tr>
	<tr>
		<td class="right">公司电话：</td>
		<td class="left">${cusInfo.companyPhone }</td>
		<td class="right">公司规模 ：</td>
		<td class="left">
		<script>document.write(getItemValue(${cusInfo.levelId}));</script>				
		</td>
	</tr>
	<tr>
		<td class="right">公司性质：</td>
		<td class="left">
		<script>document.write(getItemValue(${cusInfo.kind}));</script>		
		</td>
		<td class="right">目前车辆数：</td>
		<td class="left">${cusInfo.vehicleNum }</td>
	</tr>
		<tr>
		<td class="right">了解途径：</td>
		<td class="left">
		<script>document.write(getItemValue(${cusInfo.ctmForm}));</script>		
		</td>
	</tr>
	<tr>
		<td class="right">所在地：</td>
		<td colspan="3" class="left">
		省份：<script type='text/javascript'>
     			writeRegionName(${cusInfo.province });
 			</script>
	&nbsp;&nbsp;&nbsp;
		地级市：<script type='text/javascript'>
     			writeRegionName(${cusInfo.city });
 			</script>&nbsp;&nbsp;&nbsp;
		区、县 :<script type='text/javascript'>
     			writeRegionName(${cusInfo.town });
 			</script>&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
</table>
<div id="customerInfoId">
<table class="table_query table_list" class="center" id="ctm_table_id">
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />个人客户信息</th>
	</tr>
	<tr>
		<td width="15%" class="right" id="tcmtd">客户姓名：</td>
		<td width="35%" class="left">
		${cusInfo.ctmName }
		</td>
		<td width="15%" class="right" id="sextd">性别：</td>
		<td class="left">
		<script>document.write(getItemValue(${cusInfo.sex}));</script>			
		</td>
	</tr>
<!-- </table>
<table class="table_query table_list" class="center" id="ctm_table_id_2"> -->
	<tr>
		<td width="15%" class="right">证件类别：</td>
		<td width="35%" class="left">
		<script>document.write(getItemValue(${cusInfo.cardType}));</script>				
		</td>
		<td width="15%" class="right">证件号码：</td>
		<td width="35%" class="left">${cusInfo.cardNum }</td>
	</tr>
	<tr>
		<td class="right">主要联系电话：</td>
		<td class="left">${cusInfo.mainPhone }</td>
		<td class="right">其他联系电话：</td>
		<td class="left">${cusInfo.otherPhone }</td>
	</tr>
	<tr>
		<td class="right">电子邮件：</td>
		<td class="left">${cusInfo.email }</td>
		<td class="right">邮编：</td>
		<td class="left">${cusInfo.postCode }</td>
	</tr>
	<tr>
		<td class="right">出生年月：</td>
		<td class="left">
		<%if(null != customerInfo){
			if(null != customerInfo.getBirthday() && !"".equals(customerInfo.getBirthday())){
				out.print(CommonUtils.printDate(customerInfo.getBirthday()));
			}	
		}
		%>					
		</td>
		<td class="right">客户来源：</td>
		<td class="left">
		<script>document.write(getItemValue(${cusInfo.ctmForm}));</script>				
		</td>
	</tr>
	<tr>
		<td class="right">家庭月收入：</td>
		<td class="left">
		<script>document.write(getItemValue(${cusInfo.income}));</script>			
		</td>
		<td class="right">教育程度：</td>
		<td class="left">
		<script>document.write(getItemValue(${cusInfo.education}));</script>				
		</td>
	</tr>
	<tr>
		<td class="right">所在行业：</td>
		<td class="left">
		<script>document.write(getItemValue(${cusInfo.industry}));</script>			
		</td>
		<td class="right">婚姻状况：</td>
		<td class="left">
		<script>document.write(getItemValue(${cusInfo.isMarried}));</script>	
		</td>
	</tr>
	<tr>
		<td class="right">职业：</td>
		<td class="left">
		<script>document.write(getItemValue(${cusInfo.profession}));</script>		
		</td>
		<td class="right">职务：</td>
		<td class="left">${cusInfo.job }</td>
	</tr>
	<tr>
		<td class="right">购买用途：</td>
		<td class="left">
		<script>document.write(getItemValue(${salesAuditInfo.salesAddress}));</script>		
		</td>
		<td class="right">购买原因：</td>
		<td class="left"><script>document.write(getItemValue(${salesAuditInfo.salesReson}));</script></td>
	</tr>

	<tr>
		<td class="right">所在地：</td>
		<td colspan="3" class="left">
		省份：<script type='text/javascript'>
     			writeRegionName(${cusInfo.province });
 			</script>
	&nbsp;&nbsp;&nbsp;
		地级市：<script type='text/javascript'>
     			writeRegionName(${cusInfo.city });
 			</script>&nbsp;&nbsp;&nbsp;
		区、县 :<script type='text/javascript'>
     			writeRegionName(${cusInfo.town });
 			</script>&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
	
	<tr>
		<td class="right">详细地址：</td>
		<td colspan="3" class="left">${cusInfo.address }</td>
	</tr>
</table>
</div>
<!-- 附件 开始  -->
       
<table class="table_query table_list" border="0" id="file" style="display: none;">
			<input type="hidden" name="fjids"/>
		<tr>
			<td width="100%" colspan="2">
				<jsp:include page="${contextPath}/uploadDiv.jsp" />
			</td>
		</tr>
		<%for(int i=0;i<attachLs.size();i++) { %>
    			<script type="text/javascript">
    			addUploadRowByDbView('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
    			</script>
    	<%} %>
    
</table>
	
 <!-- 附件 结束 -->
<%
	if(null != linkManList && linkManList.size()>0){
%>
<table class="table_query table_list">
    <tr class="left">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />其他联系人信息</th>
	</tr>
    <tr >
      <th width="15%">姓名</th>
      <th width="20%">主要联系电话</th>
      <th width="20%">其他联系电话</th>
      <th width="45%">联系目的</th>
    </tr>
   <c:forEach items="${linkManList}" var="linkManList">
  		<tr class = "table_list_row1">
       		<td >${linkManList.NAME }</td>
       		<td >${linkManList.MAIN_PHONE }</td>
       		<td >${linkManList.OTHER_PHONE }</td>
       		<td >${linkManList.CONTRACT_REASON }</td>
     	</tr>
  	</c:forEach>
 </table>

<%
	}
%>
<table class="table_query" id="submitTable">
	<tr>
		<td class="center">
			<input type="button" value="返 回"  class="u-button u-reset"  onclick="history.back();" /> 
		</td>
	</tr>
</table>
</form>
</div>
</body>
</html>
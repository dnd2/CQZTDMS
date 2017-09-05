<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.TtDealerActualSalesPO"%>
<%@ page import="com.infodms.dms.po.TtCustomerPO"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	Map vehicleInfo = (Map)request.getAttribute("vehicleInfo");
	TtDealerActualSalesPO salesInfo = (TtDealerActualSalesPO)request.getAttribute("salesInfo");	//车辆实销信
	int isFleet = salesInfo.getIsFleet();	 													//是否是集团客户
	TtCustomerPO customerInfo = (TtCustomerPO)request.getAttribute("customerInfo");				//客户信息
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("lists");
	Long CtmId = new Long(0);
	if(null != customerInfo){
		CtmId = customerInfo.getCtmId();
	}
	String vehicleId = vehicleInfo.get("VEHICLE_ID").toString();
	int s_ctmType = Constant.CUSTOMER_TYPE_02;									//客户类型：公司客户
	int yes = Constant.IF_TYPE_YES;			  									//"是"
	//集团客户相关信息
	String fleetId = (String)request.getAttribute("fleetId");					//集团客户id
	String fleetContractId = (String)request.getAttribute("fleetContractId");	//集团客户合同id
	String fleetName = (String)request.getAttribute("fleetName");				//集团客户名称
	String contractNo = (String)request.getAttribute("contractNo");				//集团客户合同号 
	
	
	Long province__ = new Long(0);			  //省
	Long city__ = new Long(0);	  			  //市
	Long town__ = new Long(0);	  			  //县
	int ctmType = 0;			  			  //客户类型
	if(null != customerInfo && !"".equals(customerInfo)){
		province__ = customerInfo.getProvince();
		city__ = customerInfo.getCity();
		town__ = customerInfo.getTown();
		ctmType = customerInfo.getCtmType();
	}
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		genLocSel('txt1','txt2','txt3','<%=province__%>','<%=city__%>','<%=town__%>'); // 加载省份城市和县
		genLocSel('txt4','txt5','txt6','<%=province__%>','<%=city__%>','<%=town__%>'); // 加载省份城市和县
		//queryOtherLinkMan();
		var ctmType = '<%=ctmType%>';
		var s_ctmType = '<%=s_ctmType%>';
		if(ctmType == s_ctmType){
			document.getElementById("company_table").style.display = "table";
			document.getElementById("customerInfoId").style.display = "none";
			document.getElementById("file").style.display = "none";
		}
	}
	//查询集团客户
	function toFleetList(){
		OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/toQueryFleetList.do',800,600);
	}
	//查询集团客户合同
	function toFleetContractList(){
		var fleet_id = document.getElementById("fleet_id").value;
		if(fleet_id == 0 || ""==fleet_id){
			MyAlert("请选择集团客户!");
		}else{
			OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/toQueryFleetContractList.do',800,600);
		}
	}
	
	function changeCustomerType(value){
		var ctm_type = <%=Constant.CUSTOMER_TYPE_01%>;//个人客户
		if(value == ctm_type){
			document.getElementById("company_table").style.display = "none";
			document.getElementById("customerInfoId").style.display = "inline";
		}else{
			document.getElementById("company_table").style.display = "table";
			document.getElementById("customerInfoId").style.display = "none";
			
		}
	}	
	//是否是集团客户
	function is_fleet_Sel(value){
		var yes = '<%=yes%>';
		var s_ctmType = '<%=s_ctmType%>';
		var customer_type = document.getElementsByName("customer_type");
		if(yes == value){
			document.getElementById("is_fleet_yes").checked=true;
			document.getElementById("is_fleet_show").style.display = "inline";
			/*var tab1 = document.getElementById("tab1");
			var inputs = tab1.getElementsByTagName("input");
			for(i=0;i <inputs.length;i++){ 
				if(inputs[i].type=="text"){
					inputs[i].readOnly=true;
				}
			}*/
			//document.getElementById("aaa").style.display = "none";
			//document.getElementById("customerInfoId").style.display = "none";
		}else{
			document.getElementById("is_fleet_no").checked=true;
			document.getElementById("is_fleet_show").style.display = "none";
			//document.getElementById("aaa").style.display = "inline";
			//document.getElementById("customerInfoId").style.display = "inline";
			/*var tab1 = document.getElementById("tab1");
			var inputs = tab1.getElementsByTagName("input");
			for(i=0;i <inputs.length;i++){ 
				if(inputs[i].type=="text"){
					inputs[i].readOnly=false;
				}
			}*/
		}
	}
	
	//得到iframe信息，验证其他联系人填写是否正确
	function check_Link(){
		var check_Link_flag = true;
		var linkMan_names = document.getElementsByName("linkMan_name");
		var linkMan_main_phones = document.getElementsByName("linkMan_main_phone");
		var linkMan_other_phones = document.getElementsByName("linkMan_other_phone");
		var linkMan_contract_reasons = document.getElementsByName("linkMan_contract_reason");
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
				
				}
			}
		}
		return check_Link_flag;
	}

	function doReport(){
		if(document.getElementById("changeVicle").style.display=='table-row'){
			var thischange=document.getElementById("thischange").value;
			if(thischange=="" ||thischange==null){
				MyAlert("请选择本品牌置换！");
				return;
				}
		}
		/*var tab =document.getElementById("fileUploadTab");
		var rows = tab.rows.length;
		if(rows!=3){
			 MyAlert("必须上传2个附件,上传附件1为零售发票、2为PDI点检表!");
			 return;
			 }
		var uploadFileName = document.getElementById("uploadFileName").value;//上传附件的名称
		if(uploadFileName==null||uploadFileName=="")
		{
			MyAlert("必须上传2个附件,上传附件1为零售发票、2为PDI点检表!");
			return;
		}*/
		if(document.getElementById("MORTGAGE_TYPE").style.display=='table-row'){
			
		//	var First_Price = document.getElementById("FirstPrice").value;
			var mortgageType = document.getElementById("mortgageType").value;

		//	var LoansType = document.getElementById("LoansType").value;

			var LoansYear = document.getElementById("LoansYear").value;
			
			var bank= document.getElementById("bank").value;
			//if(LoansType=="" ||LoansType==null){
			//	MyAlert("请选择贷款方式！");
			//	return;
			//	}
			if(bank=="" ||bank==null){
				MyAlert("请选择按揭银行！");
				return;
				}
			if(mortgageType=="" ||mortgageType==null){
				MyAlert("请选择按揭类型！");
				return;
				}
	              
	              	//1、判断不为空
		if(LoansYear==""){     
			MyAlert("贷款年限不能为空！");
			return;
	                //2、判断输入的是不是数字
		}else {
	                         if(/^(\+|-)?\d+($|\.\d+$)/.test(LoansYear)) {
	                                    //进入这里，表明是一个数字
	                    //转换为数字格式
	                    var value=parseInt(LoansYear);
	                    if(value>=1&&value<=100){
	                                           //表明输入的数字是0-100之间的
	                       
	                                   }else{
	                                           //表明输入的数字不在0-100之间
	                                       
	                       				MyAlert("贷款年限必须为0-100的数字！");
	                       				return;
	                                   }      
	                         }  else{
	                                   //表明输入的不是数字
	                             
	             				MyAlert("贷款年限必须为0-100数字！");
	             				return;
	                        } 
		                        }     

			}
		
		if(submitForm('fm')){
			var edit_remark = document.getElementById("edit_remark").value.replace(/(^\s*)|(\s*$)/g, "");
			if(!edit_remark){
				MyAlert("请填写修改原因!");
				return;
			}
			var invoice_date = document.getElementById("invoice_date").value;//用户选择的开票时间
			if(compareNowAndDate(invoice_date,"yyyy-MM-dd")){
				MyAlert("开票时间不能大于当前时间!");
				return;
			}
			
			if(check_Link()){
				var isFleetHidden = document.getElementById("isFleetHidden").value;
				MyConfirm("是否提交?",doReportAction,[isFleetHidden]);
			}	
			
		}
	}
	
	function doReportAction(isFleet){
		var fsm = document.getElementById("fm");
		fsm.action= "<%=request.getContextPath()%>/sales/customerInfoManage/SalesInfoChange/salesInfoChangeAction.do?isFleet="+isFleet;
		// MyAlert($('fm').action);
		fsm.submit();
	}
	//客户列表
	function showCustomerList(){
		OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/toQueryCtmList.do',800,600);
	}
	//显示集团客户信息
	function showFleetInfo(fleet_id,fleet_name,type){
		document.getElementById("fleet_name").value = fleet_name;
		document.getElementById("fleet_id").value = fleet_id;
		document.getElementById("type").value = type;
		document.getElementById("fleet_contract_no").value ="";
		document.getElementById("fleet_contract_no_id").value = "";
		var contractSpan = document.getElementById("contractSpan");
		if(type == 'FLEET'){
			contractSpan.innerHTML = "<select name='fleetContract' class='short_sel' onchange='getContractNo();'></select>";
			getFleetContractList();//获得合同列表
		}
		else{
			contractSpan.innerHTML = fleet_name;
		}
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
	
	function queryOtherLinkMan1(){
		var CtmId = '<%=CtmId%>';
		var sum = document.getElementById("iframe");
		sum.src = "<%=contextPath%>/sales/customerInfoManage/SalesReport/queryOtherLinkMan.do?oldCustomerId="+CtmId;
	}
	function goback(){
		history.back();
		//window.location.href = "<%=request.getContextPath()%>/sales/customerInfoManage/SalesReport/salesReportInit.do";
	}
	
	function objToStr(itemValue){
		itemValue = itemValue==null?"":itemValue;
		itemValue = itemValue=='null'?"":itemValue;
		return itemValue;
	}
	function addTblRow() {
		var tbl = document.getElementById('file');
		var rowObj = tbl.insertRow(tbl.rows.length);
		rowObj.className  = "table_list_row2";
		var cell1 = rowObj.insertCell(0);
		var cell2 = rowObj.insertCell(1);
		var cell3 = rowObj.insertCell(2);
		var cell4 = rowObj.insertCell(3);
		var cell5 = rowObj.insertCell(4);
		cell1.innerHTML = '<TD class="center"><div class="center"><input type="text" maxlength="20" name="linkMan_name" style="width: 80%" /><font color="#FF0000">*</font></div></TD>';
		cell2.innerHTML = '<TD class="center"><div class="center"><input type="text" maxlength="15" name="linkMan_main_phone" style="width: 70%" /><font color="#FF0000">*</font></div></TD>';
		cell3.innerHTML = '<TD class="center"><div class="center"><input type="text" maxlength="15" name="linkMan_other_phone" style="width: 80%" /></div></TD>';
		cell4.innerHTML = '<TD class="center"><div class="center"><input type="text" maxlength="200" name="linkMan_contract_reason" style="width: 80%" /></div></TD>';
		cell5.innerHTML = '<TD class="center"><div class="center"><input class="normal_btn" type="button" value="删除" width="80%" onclick="deleteTblRow(this);" /></div></TD></TR>';
	}
	function deleteTblRow(obj) {
		var idx = obj.parentElement.parentElement.parentElement.rowIndex;
		var tbl = document.getElementById('file');
		tbl.deleteRow(idx);		
	}
	function deleteOtherLink(lm_id,obj){
		MyConfirm("是否删除?",deleteOtherLinkAction,[lm_id,obj]);
	}

	var obj_=0;
	function deleteOtherLinkAction(lm_id,obj){
		makeNomalFormCall('<%=contextPath%>/sales/customerInfoManage/SalesReport/delOtherLinkMan.json?lm_id='+lm_id,showResult1,'fm');
		//var idx = obj.parentElement.parentElement.parentElement.rowIndex;
		//var tbl = document.getElementById('file');
		//tbl.deleteRow(idx);
		obj_ = obj;
	}
	
	function del(obj){
		var idx = obj.parentElement.parentElement.parentElement.rowIndex;
		var tbl = document.getElementById('file');
		tbl.deleteRow(idx);
	}
	function showResult1(json){
		if(json.returnValue == 1){
			del(obj_);
			return true;
		}else{
			MyAlert("删除失败，请联系管理员!");
			return false;
		}
	}
	function getFleetContractList(){
		var fleet_id = document.getElementById("fleet_id").value;
		var url = "<%=request.getContextPath()%>/sales/customerInfoManage/SalesReport/getContractList.json";
		makeCall(url,showFleetContractList,{fleet_id:fleet_id}); 
	}
	
	function showFleetContractList(json){
		var obj = document.getElementById("fleetContract");
		obj.options.length = 0;
		for(var i=0;i<json.contractList.length;i++){
			obj.options[i]=new Option(json.contractList[i].CONTRACT_NO, json.contractList[i].CONTRACT_ID + "|" + json.contractList[i].CONTRACT_NO);
		}
		getContractNo();
	}
	function getContractNo(){
		var contract = document.getElementById("fleetContract").value;
		if(contract != ""){
			var contractId = contract.split("|")[0];
			var contractNo = contract.split("|")[1];
			document.getElementById("fleet_contract_no_id").value = contractId;
			document.getElementById("fleet_contract_no").value = contractNo;
		}
	}

	//function changeMortgageType(value){
		
		//var mg_type = <%=Constant.PAYMENT_03%>;//按揭
		//if(value == mg_type){
		//	document.getElementById("MORTGAGE_TYPE").style.display = "inline";
		//	document.getElementById("Loans").style.display = "inline";
		
		//	}else{
		//	document.getElementById("MORTGAGE_TYPE").style.display = "none";
			//document.getElementById("Loans").style.display = "none";
		//	document.getElementById("mortgageType").value="";
		//	document.getElementById("FirstPrice").value="";
		//	document.getElementById("LoansType").value="";
		//	document.getElementById("LoansYear").value="";
		//}
	//}	
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
			//按揭相关
			document.getElementById("mortgageType").value="";
			document.getElementById("FirstPrice").value="";
			document.getElementById("LoansType").value="";
			document.getElementById("bank").value="";
			document.getElementById("money").value="";
			document.getElementById("LoansYear").value="";
			document.getElementById("lv").value="";
			//置换相关
			document.getElementById("thischange").value="";
			document.getElementById("loanschange").value="";
			//按揭		
		}else if(value=="10361003"){
			document.getElementById("MORTGAGE_TYPE").style.display = "table-row";
			document.getElementById("Loans1").style.display = "table-row";
			document.getElementById("Loans2").style.display ="table-row";
			document.getElementById("Loans3").style.display = "table-row";
			document.getElementById("changeVicle").style.display="none";
			//置换相关
			document.getElementById("thischange").value="";
			document.getElementById("loanschange").value="";
		//置换	
		}else if(value=="10361004"){
			document.getElementById("MORTGAGE_TYPE").style.display = "none";
			document.getElementById("Loans1").style.display = "none";
			document.getElementById("Loans2").style.display = "none";
			document.getElementById("Loans3").style.display = "none";
			document.getElementById("changeVicle").style.display="table-row";
			//按揭相关
			document.getElementById("mortgageType").value="";
			document.getElementById("FirstPrice").value="";
			document.getElementById("LoansType").value="";
			document.getElementById("bank").value="";
			document.getElementById("money").value="";
			document.getElementById("LoansYear").value="";
			document.getElementById("lv").value="";
			//置换转按揭
		}else{
			document.getElementById("MORTGAGE_TYPE").style.display = "table-row";
			document.getElementById("Loans1").style.display = "table-row";
			document.getElementById("Loans2").style.display = "table-row";
			document.getElementById("Loans3").style.display = "table-row";
			document.getElementById("changeVicle").style.display="table-row";
		}
	}
	//传入时间和格式：比较传入的时间与当前时间 当前时间小于传入时间 返回true 反之 false
	function compareNowAndDate(sel_date,fmt){
		var flag = "";
		var  date=new Date();
		if(fmt){
			var date_format = date.Format(fmt);
		}else{
			var date_format = date.Format("yyyy-MM-dd");
		}
		if(date_format < sel_date){
			flag = true;
		}else{
			flag = false;
		}
		return flag;
	}
</script>
<title> 实销信息更改申请</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="doInit();sx_addRegionInit();sx_addRegionInit2();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 &gt; 客户信息管理 &gt; 实销信息更改申请</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="knowaddress" id="knowaddress" value="<%=Constant.KNOW_ADDRESS%>"></input>
<input type="hidden" name="pName" id="pName" value="${pName}"></input>
<input type="hidden" name="curPage" id="curPage" value="1" /> 
<input type="hidden" id="dlrId" name="dlrId" value="" />
 <input type="hidden" id="provinceId" name="provinceId" value="<%=province__%>"/>
<input type="hidden" id="cityId" name="cityId" value="<%=city__%>"/>
<input type="hidden" id="townId" name="townId" value="<%=town__%>"/>
<input type="hidden" id="c_provinceId" name="c_provinceId" value="<%=province__%>"/>
<input type="hidden" id="c_cityId" name="c_cityId" value="<%=city__%>"/>
<input type="hidden" id="c_townId" name="c_townId" value="<%=town__%>"/>
<input type="hidden" id="isFleetHidden" value="${salesInfo.isFleet}"/>
<table class="table_query table_list">
    <tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />修改说明</th>
	</tr>
  </table>
  <table class="table_query table_list" width="90%" class="center" id="person2">
    <tr>
      <td width="15%" class="right">修改原因：<br /></td>
      <td width="85%" class="left">
	      <span class="datadetail">
	        	<textarea id="edit_remark" name="edit_remark" cols="40" rows="3" datatype="0,is_textarea,300" ></textarea>
	      </span>
      </td>
    </tr>
  </table>

<table class="table_query table_list" class=center id="tab1">
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
	<tr>
		<td colspan="4">
	<table class="table_query table_list" class=center id="aaa">
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />销售信息</th>
	</tr>
	<tr>
		<td class="right">车牌号：</td>
		<td width="35%" class="left"><input id="vehicle_no"
			name="vehicle_no" type="text" class="middle_txt"
			value="${salesInfo.vehicleNo }" datatype="1,is_carno,20"
			maxlength="20" /></td>
		<td width="15%" class="right">合同编号：</td>
		<td width="35%" class="left"><input id="contract_no"
			name="contract_no" type="text" class="middle_txt"
			value="${salesInfo.contractNo }" datatype="1,is_textarea,25"
			maxlength="25" /></td>
	</tr>
	<tr>
		<td class="right">开票日期：</td>
		<td class="left"><input name="invoice_date" id="invoice_date"  type="text" class="short_txt" style="width: 80px;"
			value="<%=salesInfo==null?"":(salesInfo.getInvoiceDate()==null?"":CommonUtils.printDate(salesInfo.getInvoiceDate()))%>"
			datatype="0,is_date,10"  readonly="readonly" />
		</td>
		<td class="right">发票编号：</td>
		<td class="left"><input id="invoice_no" name="invoice_no"
			type="text" class="middle_txt" value="${salesInfo.invoiceNo }"
			datatype="0,is_textarea,25" maxlength="25" />
		</td>
	</tr>
	<tr>
		<td class="right">保险公司：</td>
		<td class="left"><input id="insurance_company"
			name="insurance_company" type="text" class="middle_txt"
			value="${salesInfo.insuranceCompany }" datatype="1,is_textarea,50"
			maxlength="50" /></td>
		<td class="right">保险日期：</td>
		<td class="left"><input name="insurance_date"  id="t2" type="text" class="short_txt"
			value="<%=salesInfo==null?"":(salesInfo.getInsuranceDate()==null?"":CommonUtils.printDate(salesInfo.getInsuranceDate())) %>"
				onFocus="WdatePicker({el:$dp.$('t2')})"  style="cursor: pointer;width: 80px;"/>		
		</td>
	</tr>
	<!-- <tr>
		<td class="right">车辆交付日期：</td>
		<td class="left"><input name="consignation_date" id="t3"
			type="text" class="short_txt"
			value="<%=salesInfo==null?"":(salesInfo.getConsignationDate()==null?"":CommonUtils.printDate(salesInfo.getConsignationDate())) %>"
			datatype="0,is_date,10" hasbtn="true"
			callFunction="showcalendar(event, 't3', false);" /></td>
		<td class="right">交付时公里数：</td>
		<td class="left"><input id="miles" name="miles" type="text"
			class="middle_txt" value="${salesInfo.miles }"
			datatype="1,is_double,6" maxlength="6" /></td>
	</tr> -->
	<tr>
		<td class="right">车辆性质：</td>
		<td class="left"><label> 
			<script type="text/javascript">
					genSelBoxExp("carCharactor",<%=Constant.SERVICEACTIVITY_CHARACTOR%>,'${salesInfo.carCharactor}',false,"",'',"false",'');
			</script><font color="red">*</font></label>
		</td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td class="right">购置方式：</td>
		<td class="left"><label> <script type="text/javascript">
					genSelBoxExp("payment",<%=Constant.PAYMENT%>,"<%=salesInfo==null?"":(salesInfo.getPayment()==null?"":salesInfo.getPayment())%>",false,"","onchange='changeMortgageType(this.value)'","false",'');
				</script> </label></td>
		<td class="right">价格：</td>
		<td class="left"><input id="price" name="price" type="text"
			class="middle_txt" value="${salesInfo.price }"
			datatype="0,isMoney,10" maxlength="10" /></td>
	</tr>
	<% if(salesInfo.getPayment().toString().equals(Constant.PAYMENT_03.toString())||salesInfo.getPayment().toString().equals(Constant.PAYMENT_05.toString())){%>
	<tr id="MORTGAGE_TYPE"  style="display: table-row;">
	<%}else {%>
		<tr id="MORTGAGE_TYPE"  style="display: none;">
	<% }%>
		<td class="right">按揭类型：</td>
		<td class="left"><label> 
			<script type="text/javascript">
					genSelBoxExp("mortgageType",<%=Constant.MORTGAGE_TYPE%>,'<%=salesInfo.getMortgageType() %>',true,"",'',"false",'');
			</script><font color="red">*</font></label>
		</td>
<!--		<td class="right">首付比例：</td>-->
<!--		<td class="left"><input id="FirstPrice" name="FirstPrice" type="text" class="middle_txt" value="<%=salesInfo.getShoufuRatio() %>"  />%<font color="red">*</font></td>-->
		
	</tr>
	<% if(salesInfo.getPayment().toString().equals(Constant.PAYMENT_03.toString())||salesInfo.getPayment().toString().equals(Constant.PAYMENT_05.toString())){%>
	<tr id="Loans1"  style="display: table-row;">
	<%}else {%>
		<tr id="Loans1"  style="display: none;">
	<% }%>

	
<!--		<td class="right">贷款方式：</td>-->
<!--		<td class="left"><label> -->
<!--			<script type="text/javascript">-->
<!--					genSelBoxExp("LoansType",<%=Constant.Loans%>,'<%=salesInfo.getLoansType() %>',true,"",'',"false",'');-->
<!--			</script><font color="red">*</font></label>-->
<!--		</td>-->
		<td class="right">贷款年限(期)：</td>
		<td class="left">
<!--	 <input id="LoansYear1" name="LoansYear1" type="text" class="middle_txt" value="<%=salesInfo.getLoansYear() %>"  /> -->
		<select id="LoansYear" name="LoansYear" class="u-select""> 
		<option <%=salesInfo.getLoansYear().toString().equals("12") ? "selected" : ""%> value="12" title="12">12</option>
		<option <%=salesInfo.getLoansYear().toString().equals("18") ? "selected" : ""%> value="18" title="18">18</option>
		<option <%=salesInfo.getLoansYear().toString().equals("24") ? "selected" : ""%> value="24" title="24">24</option>
		<option <%=salesInfo.getLoansYear().toString().equals("36") ? "selected" : ""%> value="36" title="36">36</option>
		<option <%=salesInfo.getLoansYear().toString().equals("48") ? "selected" : ""%> value="48" title="48">48</option>
		<option <%=salesInfo.getLoansYear().toString().equals("60") ? "selected" : ""%> value="60" title="60">60</option>
		<option <%=salesInfo.getLoansYear().toString().equals("-1") ? "selected" : ""%> value="-1" title="-1">其他</option>
		
		</select>
		<font color="red">*</font></td>
		
	</tr>
	<% if(salesInfo.getPayment().toString().equals(Constant.PAYMENT_03.toString())||salesInfo.getPayment().toString().equals(Constant.PAYMENT_05.toString())){%>
	<tr id="Loans2"  style="display: table-row;">
	<%}else {%>
		<tr id="Loans2"  style="display: none;">
	<% }%>
		<td class="right">按揭银行：</td>
		<td class="left"><label> 
			<script type="text/javascript">
					genSelBoxExp("bank",9997,'<%=salesInfo.getBank() %>',true,"",'',"false",'');
			</script><font color="red">*</font></label>
		</td>
		<td class="right">贷款金额：</td>
		<td class="left"><input id="money" name="money" type="text" class="middle_txt" value="<%=salesInfo.getMoney() %>"  /><font color="red">*</font></td>
		
	</tr>
	<% if(salesInfo.getPayment().toString().equals(Constant.PAYMENT_03.toString())||salesInfo.getPayment().toString().equals(Constant.PAYMENT_05.toString())){%>
	<tr id="Loans3"  style="display: table-row;">
	<%}else {%>
		<tr id="Loans3"  style="display: none;">
	<% }%>
		<td class="right">利率：</td>
		<td class="left"><label> 
			<input id="lv" name="lv" type="text" class="middle_txt" value="<%=salesInfo.getLv() %>"  /><font color="red">*</font>
		</td>
		<td class="right"></td>
		<td class="left"></td>
		
	</tr>
		
	<% if(salesInfo.getPayment().toString().equals(Constant.PAYMENT_04.toString())||salesInfo.getPayment().toString().equals(Constant.PAYMENT_05.toString())){%>
	<tr id="changeVicle"  style="display: table-row;">
	<%}else {%>
		<tr id="changeVicle"  style="display: none;">
	<% }%>
		<td class="right">本品牌置换：</td>
		<td class="left"><script type="text/javascript">
								genSelBoxExp("thischange",1993,'<%=salesInfo.getThischange() %>',true,"",'',"false",'');
					</script><font color="red">*	</font></td>
		<td class="right">其他品牌置换：</td>
		<td class="left"><input id="loanschange" name="loanschange" type="text" class="middle_txt" value="${salesInfo.loanschange}"><font color="red">*</font></td>
	</tr>
	<tr>
		<td class="right">备注：</td>
		<td colspan="2" class="left"><input type="text" id="memo" class="middle_txt" style="width:300px" 
			name="memo" value="${salesInfo.memo }" datatype="1,is_textarea,60" maxlength="60" size="50" />
		</td>
		<td class="left">&nbsp;</td>
	</tr>
	</table>
	</td>
	</tr>
</table>
<table class="table_query table_list" class="center" id="customer_type_table">
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />客户类型</th>
	</tr>
	<tr>
		<td class=right  id="select_ctm_type_id" width="15%">选择客户类型：</td>
		<td class="left"  id="select_ctm_type_id_1" width="85%">
			<script type="text/javascript">
				genSelBox("customer_type",<%=Constant.CUSTOMER_TYPE%>,"<%=customerInfo.getCtmType()%>",false,"","onchange='changeCustomerType(this.value)'");
			</script><font color="red">*</font>
		</td>
		<td class="right" style="display: none;">是否集团客户：</td>
		<td class="left" style="display: none;">
			<c:if test="${fleetName==null}">
				<input type="radio" id="is_fleet_yes" name="is_fleet" onclick="is_fleet_Sel(<%=Constant.IF_TYPE_YES %>);" value="<%=Constant.IF_TYPE_YES %>" />是 
				<input type="radio" id="is_fleet_no" name="is_fleet" onclick="is_fleet_Sel(<%=Constant.IF_TYPE_NO %>);" value="<%=Constant.IF_TYPE_NO %>" checked="checked" />否
			</c:if >
			<c:if test="${fleetName!=null}">
				<input type="radio" id="is_fleet_yes" name="is_fleet" onclick="is_fleet_Sel(<%=Constant.IF_TYPE_YES %>);" value="<%=Constant.IF_TYPE_YES %>" checked="checked"/>是 
				<input type="radio" id="is_fleet_no" name="is_fleet" onclick="is_fleet_Sel(<%=Constant.IF_TYPE_NO %>);" value="<%=Constant.IF_TYPE_NO %>"  />否
			</c:if >
		</td>
	</tr>
<!--	<c:if test="${pName!=null}">-->
<!--	<tr>-->
<!--			<td class="right">集团客户名称：</td>-->
<!--			<td class="left"><label>${pName}</label></td>-->
<!--			<td class="right" style="display:none;">集团客户合同：</td>-->
<!--			<td class="left" style="display:none;"><label>${pName}</label>-->
<!--			</td>-->
<!--			<td class="right" style="display:none;"></td>-->
<!--			<td class="left" style="display:none;">-->
<!--			</td>-->
<!--		</tr>-->
<!--	</c:if>-->
<c:if test="${fleetName==null}">
		<tr id="is_fleet_show" style="display:none;">
			<td class="right">集团客户代码：</td>
			<td class="left">
				<input id="fleet_name" name="fleet_name" type="text" value="" class="middle_txt" datatype="0,is_textarea,30" size="30" readonly="readonly" /> 
				<input id="fleet_id" name="fleet_id" value="${fleetId }" type="hidden" class="middle_txt" /> 
				<input id="type" name="type" value="" type="hidden"/> 
				<input type="button" value="..." class="mini_btn" onclick="toFleetList();" /></td>
		</tr>
	</c:if>
	<c:if test="${fleetName!=null}">
<!--	<tbody id="is_fleet_show" ">-->
		<tr id="is_fleet_show">
			<td class="right">集团客户代码：</td>
			<td class="left">
				<input id="fleet_name" name="fleet_name" type="text" value="${fleetCode}" class="middle_txt" datatype="0,is_textarea,30" size="30" readonly="readonly"  /> 
				<input id="fleet_id" name="fleet_id" value="${fleetId }" type="hidden" class="middle_txt" /> 
				<input id="type" name="type" value="" type="hidden"/> 
				<input type="button" value="..." class="mini_btn" onclick="toFleetList();" /></td>
<!--			<td class="right">集团客户合同：</td>-->
<!--			<td class="left">-->
				<!--<input id="fnoid" name="fleet_contract_no" type="text" class="middle_txt" value="" datatype="0,is_textarea,20" size="20" readonly="readonly" /> 
				 --><!-- 大客户合同id -->
				<!--<input id="fleet_contract_no_id" value="${salesInfo.contractId }" name="fleet_contract_no_id" type="hidden" />
				<input type="button" value="..." class="mini_btn" onclick="toFleetContractList();" />
				 --><!-- 大客户审核状态 --> 
<!--				<input id="fleet_check_status" name="fleet_check_status" type="hidden" />-->
<!--				<input id="fleet_contract_no_id" value="${salesInfo.contractId}" name="fleet_contract_no_id" type="hidden" />-->
<!--				<input id="fleet_contract_no" name="fleet_contract_no" type="hidden" />-->
<!--				<span id="contractSpan"></span>-->
<!--			</td>-->
		</tr>
<!--	</tbody>-->
	</c:if>
</table>
<table class="table_query table_list center" class="center" id="company_table" style="display: none;">
	<tr class="tabletitle">
		<th colspan="4" class="right"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />公司客户信息</th>
	</tr>
	<tr>
		<td width="15%" class="right">公司名称：</td>
		<td width="35%" class="left"><input type="text" class="middle_txt" id="company_name" name="company_name" value="${customerInfo.companyName }" datatype="0,is_textarea,60" maxlength="60" /></td>
		<td width="15%" class="right">公司简称：</td>
		<td width="35%" class="left"><input id="company_s_name"
			name="company_s_name" value="${customerInfo.companySName }"
			type="text" class="middle_txt" datatype="1,is_textarea,60"
			maxlength="60" /></td>
	</tr>
	<tr>
		<td class="right">公司电话：</td>
		<td class="left"><input id="company_phone" name="company_phone" value="${customerInfo.companyPhone }" type="text" class="middle_txt" size="20"
			datatype="0,is_digit,11" maxlength="11" /></td>
		<td class="right">公司规模 ：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("level_id",<%=Constant.COMPANY_SCOPE%>,"<%=customerInfo==null?"":(customerInfo.getLevelId()==null?"":customerInfo.getLevelId())%>",false,"",'',"false",'');
				</script></td>
	</tr>
	<tr>
		<td class="right">公司性质：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("kind",<%=Constant.COMPANY_KIND%>,"<%=customerInfo==null?"":(customerInfo.getKind()==null?"":customerInfo.getKind())%>",false,"",'',"false",'');
			</script></td>
		<td class="right">目前车辆数：</td>
		<td class="left"><input id="vehicle_num" name="vehicle_num"
			value="${customerInfo.vehicleNum }" type="text" class="middle_txt"
			size="20" datatype="1,is_digit,6" maxlength="6" /></td>
	</tr>
	<tr>
	<td class="right">联系人（人名）：</td>
		<td class="left">
		<input id="company_man" name="company_man"  type="text" class="middle_txt" size="20"
			 maxlength="12"   datatype="0,is_textarea,30" value="${customerInfo.ctmName}" />
		</td>
			<td class="right">联系电话：</td>
		<td class="left"><input id="company_tel" name="company_tel"  type="text" class="middle_txt" size="20"
			 maxlength="12"  datatype="0,is_textarea,30" value="${customerInfo.mainPhone}"/></td>
	</tr>
	<tr>
		<td class="right">客户来源：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("myctm_form",<%=Constant.CUSTOMER_FROM%>,"<%=customerInfo==null?"":(customerInfo.getCtmForm()==null?"":customerInfo.getCtmForm())%>",false,"",'',"false",'<%=Constant.CUSTOMER_FROM_01%>,<%=Constant.CUSTOMER_FROM_03%>,<%=Constant.CUSTOMER_FROM_04%>,<%=Constant.CUSTOMER_FROM_12%>');
			</script></td>
			<td class="right">组织代码：</td>
		<td class="left"><input id="company_code" name="company_code"  type="text" class="middle_txt" size="20"
			 maxlength="18"   datatype="0,is_textarea,30"  value="${customerInfo.companyCode}"/></td>
	</tr>
	<tr>
	<td class="right">购买用途：</td>
		<td class="left"><script>genSelBoxExp("salesAddress",<%=Constant.SALES_ADDRESS%>,"<%=salesInfo==null?"":(salesInfo.getSalesAddress()==null?"":salesInfo.getSalesAddress())%>",false,"",'',"false",'');</script><font color="red">*</font></td>		
	</tr>
	<tr>
		<td class="right">所在地：</td>
		<td colspan="3" class="left">
		省份： <select id="txt1" name="province" onchange="_regionCity(this,'txt2')" class="u-select"   style="width: 80px;"></select>
		地级市： <select id="txt2" name="city" onchange="_regionCity(this,'txt3')" class="u-select"   style="width: 80px;">
		</select> 区、县 <select id="txt3" name="district" class="u-select"   style="width: 80px;"></select></td>
	</tr>
</table>
<div id="customerInfoId">
<table class="table_query table_list" class="center" id="ctm_table_id">
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />个人客户信息</th>
	</tr>
	<tr>
		<td width="15%" class="right"  id="tcmtd">客户姓名：</td>
		<td width="35%" class="left"><input type="text" name="ctmName" value="${customerInfo.ctmName }"  class="middle_txt"/></td>
		<td width="15%" class="right" id="sextd">性别：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("sex",<%=Constant.GENDER_TYPE%>,"<%=customerInfo==null?"":(customerInfo.getSex()==null?"":customerInfo.getSex())%>",false,"",'',"false",'');
			</script></td>
	</tr>
<!-- </table>
<table class="table_query table_list" class="center" id="ctm_table_id_2"> -->
	<tr>
		<td width="15%" class="right">证件类别：</td>
		<td width="35%" class="left"><script type="text/javascript">
					genSelBoxExp("card_type",<%=Constant.CARD_TYPE%>,"<%=customerInfo==null?"":(customerInfo.getCardType()==null?"":customerInfo.getCardType())%>",false,"",'',"false",'');
			</script></td>
		<td width="15%" class="right">证件号码：</td>
		<td width="35%" class="left"><input id="card_num" name="card_num" value="${customerInfo.cardNum }" type="text"
			class="middle_txt" size="20" datatype="0,is_digit_letter,30" maxlength="30" />
		</td>
	</tr>
	<tr>
		<td class="right">主要联系电话：</td>
		<td class="left"><input id="main_phone" name="main_phone"
			value="${customerInfo.mainPhone }" type="text" class="middle_txt"
			size="20" datatype="0,is_digit,15" maxlength="15" /></td>
		<td class="right">其他联系电话：</td>
		<td class="left"><input id="other_phone"
			value="${customerInfo.otherPhone }" name="other_phone" type="text"
			class="middle_txt" size="20" datatype="1,is_digit,15" maxlength="15" /></td>
	</tr>
	<tr>
		<td class="right">电子邮件：</td>
		<td class="left"><input id="email" name="email"
			value="${customerInfo.email }" type="text" class="middle_txt"
			size="20" datatype="1,is_email,70" maxlength="70" /></td>
		<td class="right">邮编：</td>
		<td class="left"><input id="post_code" name="post_code"
			value="${customerInfo.postCode }" type="text" class="middle_txt"
			size="20" datatype="1,is_digit,10" maxlength="10" /></td>
	</tr>
	<tr>
		<td class="right">出生年月：</td>
		<td class="left"><input name="birthday" id="birthday"
			value="<%=customerInfo==null?"":(customerInfo.getBirthday()==null?"":CommonUtils.printDate(customerInfo.getBirthday())) %>"
			type="text" class="short_txt" 
			 onFocus="WdatePicker({el:$dp.$('birthday')})"  style="cursor: pointer;width: 80px;"/>	
		</td>
		<td class="right">了解途径：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("ctm_form",<%=Constant.CUSTOMER_FROM%>,"<%=customerInfo==null?"":(customerInfo.getCtmForm()==null?"":customerInfo.getCtmForm())%>",false,"",'',"false",'<%=Constant.CUSTOMER_FROM_01%>,<%=Constant.CUSTOMER_FROM_03%>,<%=Constant.CUSTOMER_FROM_04%>,<%=Constant.CUSTOMER_FROM_12%>');
			</script></td>
	</tr>
	<tr>
		<td class="right">家庭月收入：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("income",<%=Constant.EARNING_MONTH%>,"<%=customerInfo==null?"":(customerInfo.getIncome()==null?"":customerInfo.getIncome())%>",false,"",'',"false",'');
			</script></td>
		<td class="right">教育程度：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("education",<%=Constant.EDUCATION_TYPE%>,"<%=customerInfo==null?"":(customerInfo.getEducation()==null?"":customerInfo.getEducation())%>",false,"",'',"false",'');
			</script></td>
	</tr>
	<tr>
		<td class="right">所在行业：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("industry",<%=Constant.TRADE_TYPE%>,"<%=customerInfo==null?"":(customerInfo.getIndustry()==null?"":customerInfo.getIndustry())%>",false,"",'',"false",'');
			</script></td>
		<td class="right">婚姻状况：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("is_married",<%=Constant.MARRIAGE_TYPE%>,"<%=customerInfo==null?"":(customerInfo.getIsMarried()==null?"":customerInfo.getIsMarried())%>",false,"",'',"false",'');
			</script></td>
	</tr>
	<tr>
		<td class="right">职业：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("profession",<%=Constant.PROFESSION_TYPE%>,"<%=customerInfo==null?"":(customerInfo.getProfession()==null?"":customerInfo.getProfession())%>",false,"",'',"false",'');
			</script></td>
		<td class="right">职务：</td>
		<td class="left"><input id="job" name="job"
			value="${customerInfo.job }" type="text" class="middle_txt" size="20"
			datatype="1,is_textarea,50" maxlength="50" />
		</td>
	</tr>
	<tr>
	<td class="right">购买用途：</td>
		<td class="left"><script>genSelBoxExp("salesAddress1",<%=Constant.SALES_ADDRESS%>,"<%=salesInfo==null?"":(salesInfo.getSalesAddress()==null?"":salesInfo.getSalesAddress())%>",false,"",'',"false",'');</script></td>		
		<td class="right">购买原因：</td>
		<td class="left"><script>genSelBoxExp("salesreson",<%=Constant.SALES_RESON%>,"<%=salesInfo==null?"":(salesInfo.getSalesReson()==null?"":salesInfo.getSalesReson())%>",false,"",'',"false",'');</script></td>
	</tr>
	<tr>
</tr>
	<tr>
		<td class="right">所在地：</td>
		<td colspan="3" class="left">
		省份： <select  id="txt4" name="province1" onchange="_regionCity(this,'txt5')" class="u-select"   style="width: 80px;"></select>
		地级市： <select id="txt5" name="city1" onchange="_regionCity(this,'txt6')" class="u-select"   style="width: 80px;"></select> 
		区、县 <select  id="txt6" name="district1" class="u-select"   style="width: 80px;"></select></td>
	</tr>
	<tr>
		<td class="right">详细地址：</td>
		<td colspan="3" class="left"><input id="address" name="address"	class="middle_txt" style="width:450px" type="text" size="80" value="${customerInfo.address }"
			datatype="0,is_textarea,200" maxlength="200" />
	</td>
	</tr>
</table>
</div>
<!--<div id="iframediv">
<iframe src="" frameborder="0" marginheight="0" marginwidth="0" frameborder="0" scrolling="auto" id="iframe" name="iframe" width="100%"></iframe>
</div>
-->

 <!-- 添加附件 开始  -->
        <table id="add_file" style="display:none" width="100%"  id="file">
				<input type="hidden" id="fjids" name="fjids"/>
	    		<tr>
	        		<th>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息(添加零售发票、PDI点检表,支持格式包括：pdf,图片或是压缩文件等)
						<font color="red">
							<span id="span1"></span>
						</font>
			     		<input type="button" class="normal_btn"  onclick="showUploadReport('<%=contextPath%>')" value ='添加附件'/><font color="red">*</font>
					</th>
				</tr>
				<tr>
    				<td width="100%" colspan="2">
				   <jsp:include page="${contextPath}/uploadDiv.jsp" />
			       </td>
  				</tr>
  					<%for(int i=0;i<attachLs.size();i++) { %>
    				<script type="text/javascript">
    				addUploadRowByDb('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
    				</script>
    				<%} %>
			</table> 
			
	
  		<!-- 添加附件 结束 -->
<!-- 
<table class="table_list" id="file">
    <tr class= "tabletitle">
      <td class = "left" colspan="3"><img class="nav" src="<%=contextPath%>/img/nav.gif" />其他联系人信息 <img src="<%=contextPath%>/img/add.png" alt="新增" width="16" height="16" class="absmiddle"  onclick="addTblRow();"/></td>
    </tr>
    <tr>
      <th width="13%" nowrap="nowrap" >姓名</th>
      <th width="14%" nowrap="nowrap" >主要联系电话</th>
      <th width="16%" nowrap="nowrap" >其他联系电话</th>
      <th width="45%" nowrap="nowrap" >联系目的</th>
      <th width="12%" nowrap="nowrap" >操作</th>
    </tr>
     <c:forEach items="${linkManList}" var="linkManList" varStatus="vstatus">
		<tr id="linkmanList${vstatus.index }" class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
			<td><div class="center">${linkManList.NAME}</div></td>
			<td><div class="center">${linkManList.MAIN_PHONE}</div></td>
			<td><div class="center">${linkManList.OTHER_PHONE}</div></td>
			<td><div class="center">${linkManList.CONTRACT_REASON}</div></td>
			<td><div class="center"><input class="normal_btn" type="button" value="删除" width="80%" onclick="deleteOtherLink(${linkManList.LM_ID },this)" /></div></td>
		</tr>
	</c:forEach>
</table> -->
<table class="table_query" id="submitTable">
	<tr>
		<td class="center">
			<input type="button" value="上 报"  class="u-button u-submit" onclick="doReport();" /> 
			<input type="button" value="返 回"  class="u-button u-reset"  onclick="goback();" /> 
			<!--1.上报车辆id  --> 
			<input type="hidden" id="vehicle_id" name="vehicle_id" value="<%=vehicleInfo.get("VEHICLE_ID") %>" />
			<!--2.实销id  --> 
			<input type="hidden" id="orderId" name="orderId" value="${salesInfo.orderId }" />
			<!--3.客户id  --> 
			<input type="hidden" id="ctmId" name="ctmId" value="${customerInfo.ctmId }" />
			<!--4.实销时间  --> 
			<input type="hidden" id="salesDate" name="salesDate" value="<%=CommonUtils.printDate(salesInfo.getSalesDate()) %>" />
		</td>
	</tr>
</table>
</form>
</div>

<script type="text/javascript">
	
</script>
</body>
</html>
<%@page import="java.text.SimpleDateFormat"%>
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
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
	String contextPath = request.getContextPath();
	Map<String,Object> vehicleInfo = (Map<String,Object>)request.getAttribute("vehicleInfo");
	String vehicleId = vehicleInfo.get("VEHICLE_ID").toString();
	String oldflag = Constant.IS_OLD_CTM_02+"";
	int s_ctmType = Constant.CUSTOMER_TYPE_02;//客户类型：公司客户
	int yes = Constant.IF_TYPE_YES;			  //"是"
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		is_fleet_Sel(<%=Constant.IF_TYPE_NO %>) ;
		changeMortgageType(<%=Constant.PAYMENT_02 %>);
		//sx_addRegionInit();
		//sx_addRegionInit2();
		genLocSel('txt1','txt2','txt3','','',''); // 加载省份城市和县
		genLocSel('txt4','txt5','txt6','${tpo.ownerProvince}','${tpo.ownerCity}','${tpo.ownerArea}'); // 加载省份城市和县
		//setJson("1");
	}
	//查询大客户
	function toFleetList(){
		OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/toQueryFleetList.do',800,600);
	}
	//查询大客户合同
	function toFleetContractList(){
		var fleet_id = document.getElementById("fleet_id").value;
		if(fleet_id == 0 || ""==fleet_id){
			MyAlert("请选择大客户!");
		}else{
			OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/toQueryFleetContractList.do',800,600);
		}
	}
	
	function changeCustomerType(value){
		var ctm_type = <%=Constant.CUSTOMER_TYPE_01%>;//个人客户
		if(value == ctm_type){
			document.getElementById("company_table").style.display = "none";
			document.getElementById("customerInfoId").style.display = "inline";
			document.getElementById("file").style.display = "inline";
		}else{
			document.getElementById("company_table").style.display = "table";
			document.getElementById("customerInfoId").style.display = "none";
			document.getElementById("file").style.display = "inline";
		}
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
			//按揭相关
			document.getElementById("mortgageType").value="";
			//document.getElementById("FirstPrice").value="";
			//document.getElementById("LoansType").value="";
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
			document.getElementById("Loans2").style.display = "table-row";
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
			//document.getElementById("FirstPrice").value="";
			//document.getElementById("LoansType").value="";
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

	//是否是大客户
	function is_fleet_Sel(value){
		var yes = '<%=yes%>';
		var s_ctmType = '<%=s_ctmType%>';
		var customer_type = document.getElementsByName("customer_type");
		if(yes == value){
			document.getElementById("is_fleet_yes").checked=true;
			document.getElementById("is_fleet_show").style.display = "inline";
			//document.getElementById("company_table").style.display = "none";
			//document.getElementById("ctm_table_id").style.display = "none";
			//document.getElementById("ctm_table_id_2").style.display = "none";
			//document.getElementById("select_ctm_type_id").disabled =true ;
			//document.getElementById("select_ctm_type_id_1").disabled =true ;
			//document.getElementById("sel_cus_type_new").disabled =true ;
			//document.getElementById("sel_cus_type_old").disabled =true ;

			//document.getElementById("file").style.display = "none";
		}else{
			document.getElementById("is_fleet_no").checked=true;
			document.getElementById("is_fleet_show").style.display = "none";
			//document.getElementById("ctm_table_id").style.display = "inline";
			//document.getElementById("ctm_table_id_2").style.display = "inline";
			//document.getElementById("select_ctm_type_id").disabled =false ;
			//document.getElementById("select_ctm_type_id_1").disabled =false ;
			//if(s_ctmType == customer_type[0].value){
			//	document.getElementById("company_table").style.display = "inline";
			//}else{
			//	document.getElementById("company_table").style.display = "none";
			//}
			//document.getElementById("sel_cus_type_new").disabled =false ;
			//document.getElementById("sel_cus_type_old").disabled =false ;
			//document.getElementById("file").style.display = "inline";
		}
	}
	

	//上报
	function doReport(){		
		////////验证车牌号是否为空   开始///////
		var vehicle_no_fir = document.getElementById("vehicle_no_fir").value;
		var vehicle_no = document.getElementById("vehicle_no").value;
		if(vehicle_no_fir != null && vehicle_no_fir != ''){
			 if(vehicle_no == null || vehicle_no == ''){
				 MyAlert("输入的车牌号不合法!");
				return;		 	
			 }
		}
		 if(vehicle_no != null && vehicle_no != ''){
				if(vehicle_no_fir == null || vehicle_no_fir == ''){
					MyAlert("请选择车牌号首字!");
					return;
				}
		 }
		
		////////验证车牌号是否为空   结束///////
		
		///////验证发票    开始////////
		var invoice_no = document.getElementById("invoice_no").value;
		if(invoice_no == null || invoice_no == ''){
			MyAlert("请输入发票编号!");
			return;
		}
		///////验证发票    结束////////
		
		///////验证价格    开始////////
		var price = document.getElementById("price").value;
		if(price == null || price == ''){
			MyAlert("请输入价格!");
			return;
		}
		if(isNaN(price)){
			MyAlert("请输入正确价格模式!");
			return;
		}
		///////验证价格    结束////////
		
		///////验证销售顾问    开始////////
		var sales_man = document.getElementById("sales_man").value;
		if(sales_man == null || sales_man == ''){
			MyAlert("请输入销售顾问!");
			return;
		}
		///////验证销售顾问    结束////////
		
		///////验证客户姓名   开始////////
		var ctm_name = document.getElementById("ctm_name").value;
		if(ctm_name == null || ctm_name == ''){
			MyAlert("请输入客户姓名!");
			return;
		}
		///////验证客户姓名    结束////////
		
		
		var card_num = document.getElementById("card_num").value;
		if(card_num == null || card_num == ''){
			MyAlert("请输入证件号码!");
			return;
		}
		if(card_num != null || card_num != ''){
			var card_type=document.getElementById("card_type").value
			if(card_type==10931001){
				var identityNumber = document.getElementById("card_num").value;
				var sexOnPage = document.getElementById('sex').value;
				if(null==identityNumber||null==sexOnPage||undefined==identityNumber||undefined==sexOnPage||''==identityNumber||''==sexOnPage){
					//return;
				}
				identityNumber = identityNumber.toUpperCase();  
			    //身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X。   
			    if(!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(identityNumber))) { 
			    	MyAlert('输入的身份证号长度不对，或者号码不符合规定！15位号码应全为数字，18位号码末位可以为数字或X。'); 
			        return; 
			    };
			    var len = identityNumber.length;
			    var year;
			    var month;
			    var day;
			    var sex;
			    if(len == 15) {
			    	year = identityNumber.substr(6,2);
			    	year += '19';
			        month = identityNumber.substr(8,2);     
			        day = identityNumber.substr(10,2); 
			        sex = identityNumber.substr(14);
			    } else if(len == 18) {
			    	year = identityNumber.substr(6,4);     
			        month = identityNumber.substr(10,2);     
			        day = identityNumber.substr(12,2); 
			        sex = identityNumber.substr(16,1);
			    }
			   	 //验证月份
				   if(month.substr(0,1)!=0){
				    	if(parseInt(month)>12){
				    		MyAlert("输入的身份证号月份不匹配!");
				    		return;
				    	}
				 	}
				 	if(day.substr(0,1)!=0){
				 		if(parseInt(day)>31){
				    		MyAlert("输入的身份证号天数不匹配!");
				    		return;
				    	}
				 	}
			    if(sex % 2 == 0) {
			    	if(sexOnPage == "10031001") {
			    		MyAlert("输入的身份证号的性别和用户选择的性别不匹配!");
			    		return;
			    	} 
			    } else {
			    	if(sexOnPage == "10031002") {
			    		MyAlert("输入的身份证号的性别和用户选择的性别不匹配!");
			    		return;
			    	}
			    }
			}
		}

		////******手机号码验证    开始******////////
		var handphone = document.getElementById("main_phone").value;
		var phone = document.getElementById("other_phone").value;
		var company=document.getElementById("company_table");
		if((handphone == null || handphone == '') && (phone == null || phone == '')&&(company.style.display=='none')){
			MyAlert("手机或固定电话必须填写一个");
			return;
		}else if(handphone != null && handphone != ''){
			 if(handphone.length != 11){
				 MyAlert("输入的手机号必须为11位!");
				return;		 	
			 }
		}
		////******手机号码验证    结束******////////


		
		var birthday = document.getElementById("birthday").value;
		if(birthday == null || birthday == ''){
			MyAlert("请选择出生日期!");
			return;
		}
		
		if(submitForm('fm')){
			MyConfirm("是否提交?",doReportAction);
		}
	}
	
	function doReportAction(){
		
		//document.getElementById("re_Id").disabled = true;
		//return ;
		makeNomalFormCall('<%=contextPath%>/sales/customerInfoManage/SalesReport/doReportAction.json',showReportRes,'fm');
	}
	function showReportRes(json){
		if(json.returnValue == '2'){
			MyAlert("此车辆已进行过实效上报，无法重复操作!");
		}else if(json.returnValue == '5'){
			MyAlert("开票日期不能超过当前日期!");
			document.getElementById("re_Id").disabled=false;
		}else{
			MyAlert("上报成功！！！");
			document.getElementById("re_Id").disabled=false;
			//doreportInit();
			//history.back();
			// location.href='<%=request.getContextPath()%>/crm/delivery/DelvManage/doInit.do';
			location.href='<%=request.getContextPath()%>/sales/customerInfoManage/SalesReport/salesReportInit.do';//跳转到 实效上报查询
		}
	}
	function doreportInit(){
		var fsm = document.getElementById("fm");
		fsm.action= "<%=request.getContextPath()%>/sales/customerInfoManage/SalesReport/salesReportInit.do";
		fsm.submit();
	}
	function Init(){
		var fsm = document.getElementById("fm");
		fsm.action= "<%=request.getContextPath()%>/sales/customerInfoManage/SalesReport/salesReportInit.do";
		fsm.submit();
	}
	//客户列表
	function showCustomerList(){
		OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/toQueryCtmList.do',800,600);
	}
	//显示大客户信息
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
		$('#fleet_id').click();
	}

	//显示大客户合同信息
	function showFleetContractInfo(contract_id,contract_no){
		document.getElementById("fleet_contract_no").value = contract_no;
		document.getElementById("fleet_contract_no_id").value = contract_id;
	}
	
	function goback(){
		window.location.href = "<%=request.getContextPath()%>/sales/customerInfoManage/SalesReport/salesReportInit.do";
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
	
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
    function clrActivityTxt(){
    	document.getElementById('activity').value = "";
    	document.getElementById('activity_id').value = "";
    }
    //验证大客户车系
  function checkSeriesGroup(obj){
  		if($(obj).value!=null&&""!=$(obj).value){
  			var group_id=document.getElementById("groupId").value;
  			var fleet_id=$(obj).value;
  			var url = "<%=request.getContextPath()%>/sales/customerInfoManage/SalesReport/checkSeriesGroup.json";
  			makeCall(url,getCheckResult,{fleetId:fleet_id,groupId:group_id}); 
  		}
    }
    //验证大客户车系的回调函数
    function getCheckResult(json){
    	if(json.count==0){
    		MyAlert("该大客户没有该车系需求！！！");
    		document.getElementById("fleet_id").value='';
    		document.getElementById("fleet_name").value='';
    	}
    }
    
	function muchInputChang(id){
		var y=document.getElementById(id).value;
		//c=y.toLowerCase();
		//document.getElementById(id).value=y.replace(/[^\d.]/g,''); 
		onlyNumber(document.getElementById(id));
	
	}
	
	function onlyNumber(obj){
		//得到第一个字符是否为负号
		var t = obj.value.charAt(0); 
		//先把非数字的都替换掉，除了数字和. 
		obj.value = obj.value.replace(/[^\d\.]/g,''); 
		//必须保证第一个为数字而不是. 
		obj.value = obj.value.replace(/^\./g,''); 
		//保证只有出现一个.而没有多个. 
		obj.value = obj.value.replace(/\.{2,}/g,'.'); 
		//保证.只出现一次，而不能出现两次以上 
		obj.value = obj.value.replace('.','$#$').replace(/\./g,'').replace('$#$','.');
		var strs= new Array(); //定义一数组 
	    strs= obj.value.split(".");	

		if(strs.length>1){
			if(strs[1].length>2){
				var last = strs[1].substring(0,2) ;
				obj.value =strs[0]+"."+last;
			}
		}
		
		//如果第一位是负号，则允许添加
		if(t == '-'){
			obj.value = '-'+obj.value;
		}
				
	}
//添加手机和固定电话号码验证；
function isPhoneNumber(){
	var handphone = document.getElementById("main_phone").value;
	var phone = document.getElementById("other_phone").value;
	var company=document.getElementById("company_table");
	if((handphone == null || handphone == '') && (phone == null || phone == '')&&(company.style.display=='none')){
		MyAlert("手机或固定电话必须填写一个");
		return false;
	}else if(handphone != null && handphone != ''){
		 if(handphone.length != 11){
			MyAlert("输入的手机号必须为11位!");
			return false;		 	
		 }
	}
	return true;
}
function _isVehicleNo(){
	var vehicle_no_fir = document.getElementById("vehicle_no_fir").value;
	var vehicle_no = document.getElementById("vehicle_no").value;
    if(vehicle_no_fir != null && vehicle_no_fir != ''){
		 if(vehicle_no == null || vehicle_no == ''){
			MyAlert("输入的车牌号不合法!");
			return false;		 	
		 }
	}else if(vehicle_no_fir == null || vehicle_no_fir == ''){
		if(vehicle_no != null && vehicle_no != ''){
			MyAlert("请选择车牌号首字!");
			return false;			
		}
	} 
	return true;
}

//实销上报车辆附件图片上传 modify by qpdong 2016-03-28
function showUploadReport(path){
	OpenHtmlWindow(path+'/commonUploadReport.jsp',800,450);
}
</script>

<title>实销信息上报</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="doInit();sx_addRegionInit();sx_addRegionInit2();">
<div class="wbox"  id="wbox" >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 &gt; 客户信息管理 &gt; 实销信息上报</div>
<form id="fm" name="fm" method="post"><input type="hidden" name="curPage" id="curPage" value="1" /> 
<input type="hidden" name="knowaddress" id="knowaddress" value="<%=Constant.KNOW_ADDRESS%>"></input>
<input type="hidden" name="qkId" id="qkId" value="${tpc.customerId}"/>
<input type="hidden" name="qkOrderId" id="qkOrderId" value="${tpo.orderId}"/>
<input type="hidden" name="qkOrderDetailId" id="qkOrderDetailId" value="${tpod.orderDetailId}"/>
<input type="hidden" name="delvDetailId" id="delvDetailId" value="${delvDetailId}"/>
 <input type="hidden" id="provinceId" name="provinceId" value=""/>
<input type="hidden" id="cityId" name="cityId" value=""/>
<input type="hidden" id="townId" name="townId" value=""/>
<input type="hidden" id="c_provinceId" name="c_provinceId" value="${tpo.ownerProvince}"/>
<input type="hidden" id="c_cityId" name="c_cityId" value="${tpo.ownerCity}"/>
<input type="hidden" id="c_townId" name="c_townId" value="${tpo.ownerArea}"/>
<input type="hidden" id="dlrId" name="dlrId" value="" />
<table class="table_query table_list"  class="center">
	<tr class="tabletitle">
		<th colspan="4">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				车辆资料<br/>
		</th>
	</tr>
	<tr>
		<td width="15%" class=right>VIN：</td>
		<td class=left>${vehicleInfo.VIN }<input type="hidden" id="vin" name="vin" value="${vehicleInfo.VIN }" /></td>
		<td class=right>发动机号：</td>
		<td class=left>${vehicleInfo.ENGINE_NO }</td> 
	</tr>
	<tr>
		<td class=right>车系：</td>
		<td class=left>${vehicleInfo.SERIES_NAME }</td>
		<td class=right>车型：<input type="hidden" id="modelCode" name="modelCode" value="${vehicleInfo.MODEL_CODE }" /></td>
		<td class=left>${vehicleInfo.MODEL_NAME }<input type="hidden" id="model_name" name="model_name" value="${vehicleInfo.MODEL_NAME }" /></td>
	</tr>
	<tr>
		<td class=right>物料代码：</td>
		<td class=left>${vehicleInfo.MATERIAL_CODE }<input type="hidden" value="${vehicleInfo.GROUP_ID} " id="groupId"/></td>
		<td class=right>物料名称：</td>
		<td class=left>${vehicleInfo.MATERIAL_NAME }</td>
	</tr>
	<tr>
		<td class=right>颜色：</td>
		<td class=left>${vehicleInfo.COLOR }<input type="hidden" value="${vehicleInfo.COLOR}" name="color"  id="color"/></td>
		<td class=right>&nbsp;</td>
		<td class=left>&nbsp;</td>
	</tr>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />销售信息</th>
	</tr>
	<tr>
		<td class="right">车牌号：</td>
		<td width="35%" class="left">
		    <script type="text/javascript">
              genSelBoxExp("vehicle_no_fir",'<%=Constant.LICENSE_PLATE_FIRST%>',"",true,"","","","");
            </script>
			<input id="vehicle_no" name="vehicle_no" type="text" class="short_txt" value=""  maxlength="6" /><!-- datatype="1,isVehicleNo,6" -->
		</td>
		<td width="15%" class="right">合同编号：</td>
		<td width="35%" class="left">
			<input id="contract_no" name="contract_no" type="text" class="middle_txt" value="" datatype="1,is_textarea,25" maxlength="25" />
		</td>
	</tr>
	<tr>
		<td class="right">开票日期：</td>
		<td class="left">
			<input name="invoice_date" id="invoice_date" type="text" class="short_txt" readonly value="${billDate}" 
				onFocus="WdatePicker({el:$dp.$('invoice_date')})"  style="cursor: pointer;width: 80px;"/>
<!--			callFunction="showcalendar(event, 'invoice_date', false);"-->
			 </td>
		<td class="right">发票编号：</td>
		<td class="left">
			<input id="invoice_no" name="invoice_no" type="text" class="middle_txt" value="NO" datatype="0,isInvoiceNo,8" maxlength="8" />
<%--			<font color="red">(必须NO开头,然后输入6或</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">7位数字，例如：NO123456)</font>--%>
		</td>
	</tr>
	<tr>
		<td class="right">保险公司：</td>
		<td class="left"><input id="insurance_company" name="insurance_company" type="text" class="middle_txt" value=""
			datatype="1,is_textarea,50" maxlength="50" /></td>
		<td class="right">保险日期：</td>
		<td class="left"><input name="insurance_date" id="insurance_date" type="text" class="short_txt" value="" 
			onFocus="WdatePicker({el:$dp.$('insurance_date')})"  style="cursor: pointer;width: 80px;"/>
		</td>
	</tr>
	<!-- <tr>
		<td class="right">车辆交付日期：</td>
		<td class="left"><input name="consignation_date" id="consignation_date" type="text" class="short_txt" value="" datatype="0,is_date,10" hasbtn="true"
			callFunction="showcalendar(event, 'consignation_date', false);" /></td>
<%--		<td class="right">交付时公里数：</td>--%>
<%--		<td class="left"><input id="miles" name="miles" type="text" class="middle_txt" value="" datatype="1,is_double,6" maxlength="6" /></td>--%>
	</tr> -->
	<tr>
		<td class="right">车辆性质：</td>
		<td class="left"><label> 
			<script type="text/javascript">
					genSelBoxExp("carCharactor",<%=Constant.SERVICEACTIVITY_CHARACTOR%>,'',false,"",'',"false",'');
			</script><font color="red">*</font></label>
		</td>
		<td class="right">价格：</td>
		<td class="left"><input id="price" name="price" type="text" class="middle_txt" value="" datatype="0,is_money_z,10" maxlength="10" onkeyup="muchInputChang(this.id)"/><font color="red">*</font></td>
	</tr>
	<tr>
	<td class="right">购置方式：</td>
		<td class="left">
		<select id="payment" name="payment"  class="u-select" onchange="changeMortgageType(this.value)"> 
			<option selected="selected" value="10361002" title="一次性付款">一次性付款</option>
			<option value="10361003" title="按揭">按揭</option><option value="10361004" title="二手车置换">二手车置换</option>
			<option value="10361005" title="二手车置换转按揭">二手车置换转按揭</option>
		</select><font color="red">*</font>
	</td>
		<td class="right">
		</td>
		<td class="left">
		</td>
	</tr>
	<tr id="MORTGAGE_TYPE" style="display: none;">
	
		<td class="right">车贷类型：</td>
		<td class="left"><label> 
			<script type="text/javascript">
					genSelBoxExp("mortgageType",1389,'',true,"",'',"false",'');
			</script><font color="red">*</font></label>
		</td>
		<td class="right">按揭银行：</td>
			<td class="left"><script type="text/javascript">
								genSelBoxExp("bank",9997,'',true,"",'',"false",'');
			</script><font color="red">*</font></td>
<!--		<td class="right">首付比例：</td>-->
<!--		<td class="left"><input id="FirstPrice" name="FirstPrice" type="text" class="middle_txt" value=""/>%<font color="red">*</font></td>-->
		
	</tr>
	<tr id="Loans1" style="display: none;">
<!--			<td class="right">贷款方式：</td>-->
<!--			<td class="left"><label> -->
<!--					<script type="text/javascript">-->
<!--								genSelBoxExp("LoansType",1390,'',true,"short_sel",'',"false",'');-->
<!--					</script><font color="red">*</font></label>-->
<!--			</td>-->
<!--			<td class="right">按揭银行：</td>-->
<!--			<td class="left"><script type="text/javascript">-->
<!--								genSelBoxExp("bank",9997,'',true,"short_sel",'',"false",'');-->
<!--			</script><font color="red">*</font></td>-->
	</tr>
	<tr id="Loans2" style="display: none;">
			<td class="right">贷款金额：</td>
			<td class="left"><input id="money" name="money" type="text" class="middle_txt" value=""><font color="red">*</font></td>
			<td class="right">贷款年限(期)：</td>
<!--        <td class="left"><input id="LoansYear" name="LoansYear" type="text" class="middle_txt" value=""><font color="red">*</font></td>  -->		
<td class="left">
<select id="LoansYear" name="LoansYear" class="u-select""> 
		<option selected="selected" value="12" title="">12</option>
		<option value="18" title="">18</option>
		<option value="24" title="24">24</option>
		<option value="36" title="36">36</option>
		<option value="48" title="48">48</option>
		<option value="60" title="60">60</option>
		<option value="-1" title="-1">其他</option>
</select>
<font color="red">*</font></td>	
	</tr>
	<tr id="Loans3" style="display: none;">
			<td class="right">利率：</td>
			<td class="left"><input id="lv" name="lv" type="text" class="middle_txt" value=""><font color="red">*</font></td>
			<td class="right"></td>
			<td class="left"></td>
	</tr>
	<tr id="changeVicle" style="display:none;" >
		<td class="right">本品牌置换：</td>
		<td class="left"><script type="text/javascript">
								genSelBoxExp("thischange",1993,'',true,"",'',"false",'');
					</script><font color="red">*</font></td>
		<td class="right">其他品牌置换：</td>
		<td class="left"><input id="loanschange" name="loanschange" type="text" class="middle_txt" value=""><font color="red">*</font></td>
	</tr>
	<tr>
		<td class="right">销售顾问：</td>
		<td class="left">
			<input id="sales_man" name="sales_man" type="text" value="${tpc.adviser}" class="middle_txt" datatype="1,is_null,100" size="30" /><font color="red">*</font>
			<input id="sales_man_id" name="sales_man_id" value="${tpc.adviser}" type="hidden" class="middle_txt" /> 
			<input id="type" name="type" value="" type="hidden"/> 
			<!-- <input type="button" value="..." class="mini_btn" onclick="toSalesManList();" />
			<input type="button" value="清空" class="normal_btn" onclick="clrTxt('sales_man');" />-->
		</td>
		<td class="right"><!-- 市场活动： --></td>
		<td class="left">
			<!-- <input id="activity" name="activity" type="text" value="" class="middle_txt"  size="30"  /> 
			<input id="activity_id" name="activity_id" value="" type="hidden" class="middle_txt" /> 
			<input id="type" name="type" value="" type="hidden"/> 
			<input type="button" value="..." class="mini_btn" onclick="toActivitiesList();" />
			<input type="button" value="清空" class="normal_btn" onclick="clrActivityTxt();" /> -->
		</td>
	</tr>
	<tr>
		<td class="right">备注：</td>
		<td colspan="2" class="left"><input type="text" class="middle_txt" style="width:300px" id="memo" name="memo" value="" datatype="1,is_textarea,60" maxlength="60" size="50" /></td>
		<td class="left">&nbsp;</td>
	</tr>
	

</table>
<table class="table_query table_list" class="center" id="customer_type_table">
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />客户类型</th>
	</tr>
	<tr >
		<td width="15%" class=right id="select_ctm_type_id">选择客户类型：</td>
		<td width="85%" class="left" id="select_ctm_type_id_1">
			<script type="text/javascript">
				genSelBox("customer_type",<%=Constant.CUSTOMER_TYPE%>,"",false,"","onchange='changeCustomerType(this.value)'");
			</script><font color="red">*</font>
		</td>
		<td class="right" style="display: none;">是否集团客户：</td>
		<td class="left" style="display: none;">
			<input type="radio" id="is_fleet_yes" name="is_fleet" onclick="is_fleet_Sel(<%=Constant.IF_TYPE_YES %>);" value="<%=Constant.IF_TYPE_YES %>"/>是 
			<input type="radio" id="is_fleet_no" name="is_fleet" onclick="is_fleet_Sel(<%=Constant.IF_TYPE_NO %>);" value="<%=Constant.IF_TYPE_NO %>" checked="checked"/>否
		</td>
	</tr>
	<tbody id="is_fleet_show" style="display: none;">
		<tr>
			<td class="right">大客户代码：</td>
			<td class="left">
				<input id="fleet_name" name="fleet_name" type="text" value="" class="middle_txt" datatype="0,is_textarea,30" size="30" readonly="readonly" /> 
				<input id="fleet_id" name="fleet_id" value=""  type="hidden" class="middle_txt" onclick="checkSeriesGroup(this);"   /> 
				<input id="type" name="type" value="" type="hidden"/> 
				<input type="button" value="..." class="mini_btn" onclick="toFleetList();" />   <input type="button" value="清空" class="normal_btn" onclick="clrTxt('fleet_name');" /></td>
			<td class="right" style="display:none;">集团客户合同：</td>
			<td class="left">
				<!--<input id="fnoid" name="fleet_contract_no" type="text" class="middle_txt" value="" datatype="0,is_textarea,20" size="20" readonly="readonly" /> 
				 --><!-- 大客户合同id -->
				<!--<input id="fleet_contract_no_id" value="${salesInfo.contractId }" name="fleet_contract_no_id" type="hidden" />
				<input type="button" value="..." class="mini_btn" onclick="toFleetContractList();" />
				 --><!-- 大客户审核状态 --> 
				<input id="fleet_check_status" name="fleet_check_status" type="hidden" />
				<input id="fleet_contract_no_id" value="${salesInfo.contractId}" name="fleet_contract_no_id" type="hidden" />
				<input id="fleet_contract_no" name="fleet_contract_no" type="hidden" />
				<span id="contractSpan" style="display: none;"></span>
			</td>
		</tr>
	</tbody>
</table>
<table  class="center table_query table_list"  id="company_table" style="display: none;">
	<tr class="tabletitle">
		<th colspan="4" class="right"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />公司客户信息</th>
	</tr>

	<tr>
		<td width="15%" class="right">公司名称：</td>
		<td width="35%" class="left"><input id="company_name" name="company_name" value="" type="text" class="middle_txt"
			datatype="0,is_textarea,60" maxlength="60" /></td>
		<td width="15%" class="right">公司简称：</td>
		<td width="35%" class="left"><input id="company_s_name" name="company_s_name" value="" type="text" class="middle_txt"
			datatype="1,is_textarea,60" maxlength="60" /></td>
	</tr>
	<tr>
		<td class="right">公司电话：</td>
		<td class="left"><input id="company_phone" name="company_phone" value="" type="text" class="middle_txt" size="20"
			datatype="0,is_digit,12" maxlength="12" /></td>
		<td class="right">公司规模 ：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("level_id",<%=Constant.COMPANY_SCOPE%>,"",false,"",'',"false",'');
				</script></td>
	</tr>
	<tr>
		<td class="right">公司性质：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("kind",<%=Constant.COMPANY_KIND%>,"",false,"",'',"false",'');
			</script></td>
		<td class="right">目前车辆数：</td>
		<td class="left"><input id="vehicle_num" name="vehicle_num" value="" type="text" class="middle_txt" size="20" datatype="1,is_digit,6"
			maxlength="6" /></td>
	</tr>
	<tr>
	<td class="right">客户来源：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("myctm_form",<%=Constant.CUSTOMER_FROM%>,"",false,"",'',"false",'');
			</script></td>
			<td class="right">组织代码：</td>
		<td class="left"><input id="company_code" name="company_code" value="" type="text" class="middle_txt" size="20"
			 maxlength="18"   datatype="0,is_textarea,30"/></td>
	</tr>
	<tr>
	<td class="right">联系人（人名）：</td>
		<td class="left">
		<input id="company_man" name="company_man" value="" type="text" class="middle_txt" size="20"
			 maxlength="12"   datatype="0,is_textarea,30"/>
		</td>
			<td class="right">联系电话：</td>
		<td class="left"><input id="company_tel" name="company_tel" value="" type="text" class="middle_txt" size="20"
			 maxlength="12"  datatype="0,is_textarea,30" /></td>
	</tr>
	<tr>
	<td class="right">购买用途：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("salesaddress",<%=Constant.SALES_ADDRESS%>,"80051001",false,"",'',"false",'');
			</script><font color="red">*</font></td>
	</tr>
	<tr>
		<td class="right">所在地：</td>
		<td colspan="3" class="left">
			省份： <select id="txt1" name="province" onchange="_regionCity(this,'txt2')" class="u-select"   style="width: 100px;"></select> 
			地级市： <select  id="txt2" name="city" onchange="_regionCity(this,'txt3')" class="u-select"   style="width: 100px;"></select> 
			区、县 <select  id="txt3" name="district" class="u-select"   style="width: 100px;"></select><font color="red"></font>
		</td>
	</tr>
	<tr>
	<td class="right">详细地址：</td>
		<td colspan="3" class="left">
			<input id="companyAddr" name="companyAddr" type="text"  class="middle_txt" style="width:400px" size="80" value="" datatype="0,is_textarea,200" maxlength="200" />
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
			<input id="ctm_name" name="ctm_name" value="${tpo.ownerName}" type="text"  size="10" datatype="1,is_null,100" maxlength="30" class="middle_txt"/><font color="red">*</font> 
			<input id="ctmID" name="ctmID" value="${tpo.customerId}" type="hidden"  size="10" datatype="0,is_textarea,30" maxlength="30" /> 
			<!-- 
			<input type="radio" id="sel_cus_type_new" name="sel_cus_type" value="<%=Constant.IS_OLD_CTM_01 %>" checked="checked" onclick="showCustSel(this.value)" />新客户
			<input type="radio" id="sel_cus_type_old" name="sel_cus_type" value="<%=Constant.IS_OLD_CTM_02 %>" onclick="showCustSel(this.value)" />老客户 
			<input type="button" id="showCustomerListID" value="..." class="mini_btn" onclick="showCustomerList();" disabled="disabled" /></td> -->
		<td width="15%" class="right" id="sextd">性别：</td>
		<td class="left">
			<script type="text/javascript">
					genSelBoxExp("sex",<%=Constant.GENDER_TYPE%>,"",false,"",'',"false",'');
			</script>
		</td>
	</tr>
<!-- </table>
<table class="table_query table_list" class="center" id="ctm_table_id_2"> -->
	<tr>
		<td width="15%" class="right">证件类别：</td>
		<td width="35%" class="left"><script type="text/javascript">
					genSelBoxExp("card_type",<%=Constant.CARD_TYPE%>,"${tpo.ownerPaperType}",false,"",'',"false",'');
			</script></td>
		<td width="15%" class="right">证件号码：</td>
		<td width="35%" class="left"><input id="card_num" name="card_num" value="${tpo.ownerPaperNo}" type="text" class="middle_txt" size="20"
			datatype="0,is_digit_letter,30" maxlength="30" /><font color="red">*</font></td>
	</tr>
	<tr>
		<td class="right">手机：</td>
		<td class="left"><input id="main_phone" name="main_phone" value="${tpo.ownerPhone}" type="text" class="middle_txt" size="20" datatype="1,is_digit,11"
			maxlength="11" /><font color="red">*</font></td>
		<td class="right">固定电话：</td>
		<td class="left"><input id="other_phone" value="" name="other_phone" type="text" class="middle_txt" size="20" datatype="1,is_null,100"
			maxlength="15" /></td>
	</tr>
	<tr>
		<td class="right">电子邮件：</td>
		<td class="left"><input id="email" name="email" value="" type="text" class="middle_txt" size="20" datatype="1,is_email,70" maxlength="70" /></td>
		<td class="right">邮编：</td>
		<td class="left"><input id="post_code" name="post_code" value="" type="text" class="middle_txt" size="20" datatype="1,is_digit,10"
			maxlength="10" /></td>
	</tr>
	<tr>
		<td class="right">出生年月：</td>
		<td class="left"><input name="birthday" id="birthday" value="" type="text" class="short_txt" datatype="1,is_null,100" 
			onFocus="WdatePicker({el:$dp.$('birthday')})"  style="cursor: pointer;width: 80px;"/><font color="red">*</font>
		</td>
		<td class="right">了解途径：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("ctm_form",<%=Constant.CUSTOMER_FROM%>,"",false,"",'',"false",'<%=Constant.CUSTOMER_FROM_01%>,<%=Constant.CUSTOMER_FROM_03%>,<%=Constant.CUSTOMER_FROM_12%>');
			</script><font color="red">*</font></td>
	</tr>
	<tr>
		<td class="right">家庭月收入：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("income",<%=Constant.EARNING_MONTH%>,"",false,"",'',"false",'');
			</script><font color="red">*</font></td>
		<td class="right">教育程度：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("education",<%=Constant.EDUCATION_TYPE%>,"",false,"",'',"false",'');
			</script><font color="red">*</font></td>
	</tr>
	<tr>
		<td class="right">所在行业：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("industry",<%=Constant.TRADE_TYPE%>,"",false,"",'',"false",'');
			</script><font color="red">*</font></td>
		<td class="right">婚姻状况：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("is_married",<%=Constant.MARRIAGE_TYPE%>,"",false,"",'',"false",'');
			</script><font color="red">*</font></td>
	</tr>
	<tr>
		<td class="right">职业：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("profession",<%=Constant.PROFESSION_TYPE%>,"",false,"",'',"false",'');
			</script><font color="red">*</font></td>
		<td class="right">职务：</td>
		<td class="left"><input id="job" name="job" value="" type="text" class="middle_txt" size="20" datatype="1,is_textarea,50" maxlength="50" /></td>
	</tr>
	<tr>
		<td class="right">购买用途：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("salesaddress1",<%=Constant.SALES_ADDRESS%>,"80051001",false,"",'',"false",'');
			</script><font color="red">*</font></td>
		<td class="right">购买原因：</td>
		<td class="left"><script type="text/javascript">
					genSelBoxExp("salesreson",<%=Constant.SALES_RESON%>,"",false,"",'',"false",'');
			</script><font color="red">*</font></td>
	</tr>
	<tr>
		<td class="right">所在地：</td>
		<td colspan="3" class="left">
			省份： <select  id="txt4"  name="province1"  onchange="_regionCity(this,'txt5')"  class="u-select"   style="width: 100px;"></select> 
			地级市： <select id="txt5"  name="city1"  onchange="_regionCity(this,'txt6')"  class="u-select"   style="width: 100px;"></select> 
			区、县 <select  id="txt6"  name="district1" class="u-select"  style="width: 100px;"></select><font color="red"></font>
		</td>
	</tr>
	<tr>
		<td class="right">详细地址：</td>
		<td colspan="3" class="left">
			<input id="address" name="address" type="text" class="middle_txt" style="width:450px" size="80" value="${tpo.ownerAddress}" datatype="0,is_textarea,200" maxlength="200" />
		</td>
	</tr>
</table>
</div>

 <!-- 添加附件 开始  -->
        <!-- <table id="add_file" style="display:block" width="100%" class="table_info" border="0" id="file">
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
    				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  					<%if(fileList!=null){for(int i=0;i<fileList.size();i++) { %>
	 					 <script type="text/javascript">
	 	 					 addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>');
	 	 				</script>
					<%}}%>
			</table>  -->
	
  		<!-- 添加附件 结束 -->
<!-- <table class="table_query table_list" id="file">
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
</table> -->
<table class="table_query table_list" id="submitTable">
	<tr >
		<td class="center">
			<input type="button" id="re_Id" value="上 报" class="u-button u-submit" onclick="doReport();" /> 
			<input type="button" value="返 回"  class="u-button u-reset"  onclick="history.back();" /> 
			<!--1.上报车辆id  --> 
			<input type="hidden" id="vehicle_id" name="vehicle_id" value="<%=vehicleInfo.get("VEHICLE_ID") %>" />
			<!--2.用户选择的老客户id  --> 
			<input type="hidden" id="oldCustomerId" name="oldCustomerId" value="" /> 
			<!--3.默认值为0，如果从接口得到的JSON值不为空，则值为1  --> 
			<input type="hidden" id="isJson" name="isJson" value="0" /> 
		</td>
	</tr>
</table>
</form>
</div>
</body>
</html>
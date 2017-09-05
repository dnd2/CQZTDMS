	<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>咨询查询</title>
<script type="text/javascript">
function doInit(){
		checkSet1();
   		//选择大区
   		$("hasCarCustomer").onclick = function() {
   			checkSet1();
   		} 
   		
   		//选择服务站
   		$("noCarCustomer").onclick = function() {
   			checkSet2();
   			
   		} 
	}
</script>
</head>
<body onload="doInit();">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系 &gt; 投诉咨询管理 &gt;投诉咨询分派</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<input type="hidden" id="cpId" name="cpId" value="${cpid}">
		<tr>
	      <td align="left" colspan="4">
	      <label><input type="radio" id="hasCarCustomer" name="hasCarCustomer"  value="0" checked="checked"/></label>
					&nbsp;大&nbsp;&nbsp;&nbsp;区：
					<select id="orgId" name="orgId">
		        		<option value="">-- 请选择 --</option>
		        		<c:forEach items="${orglist }" var="list">
		        			<option value="${list.ORG_ID }">${list.ORG_NAME }</option>
		        		</c:forEach>
		        	</select>
				</td>
			</tr>
			<tr id="complaintTrThree">
				<td colspan="4">
				<label><input type="radio" id="noCarCustomer" name="hasCarCustomer" value="1" /></label>
					&nbsp;服务商：			
					<select id="vcPro" name="vcPro" class="short_sel" onchange="changeMyCityEvent(this.value,'',false)">
						<option value=''>-请选择-</option>
						<c:forEach var="os" items="${tmOrgSmallList}">
							<option value="${os.orgId}" title="${os.orgName}">${os.orgName}</option>
						</c:forEach>
					</select>
					<select id="myCity" name="myCity" class="short_sel" onchange="changeVcProEvent(this.value,'',false)">
						<option value=''>-请选择-</option>
					</select>
					<select id="vehicleCompany" name="vehicleCompany">
						<option value=''>-请选择-</option>
					</select>
				</td>
	    </tr>
			<tr>
				<td colspan="8" align="center">
					<input name="button"  id="setQ" type="button" class="normal_btn" onclick="setComplaintPerson();"  value="确定分派" />
					&nbsp;
					<input class="normal_btn" type="reset" value="重置" />
					&nbsp;
					<input class="normal_btn" type="button" onclick="_hide();" value="关 闭" /></td>
        		</td>
			</tr>
		</table>
	
	</form>
<script type="text/javascript">

	function changeCityEvent(value,defaultValue,isdisabled){
		if(''!=value){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeRegion.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeCityBack,'fm','');
		}else{
			resetSelectOption('citysel',null,null,null,null,null);
		}
	}
	
	//城市级联回调方法：
	function changeCityBack(json) {
		resetSelectOption('citysel',json.regionList,'REGION_NAME','REGION_CODE',json.defaultValue,json.isdisabled);
	}
	
	function changeModelEvent(value,defaultValue,isdisabled){
		if(''!=value){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeVhclMaterialGroup.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeVhclMaterialGroupBack,'fm','');
		}else{
			resetSelectOption('model',null,null,null,null,null);
		}
	}
	
	//车型级联回调方法：
	function changeVhclMaterialGroupBack(json) {
		//json.defaultValue 修改了默认值,json.isdisabled 修改了置灰
		resetSelectOption('model',json.vhclList,'GROUPNAME','GROUPID',json.defaultValue,json.isdisabled);
	}
	
	function changeOrgSmallOrgEvent(value,defaultValue,isdisabled){
		if(''!=value){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/cascadeOrgSmallOrg.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeOrgSmallOrgEventBack,'fm','');
		}else{
			resetSelectOption('vcPro',null,null,null,null,null);
			resetSelectOption('vehicleCompany',null,null,null,null,null);
		}
	}
	//大区级联小区回调方法：
	function changeOrgSmallOrgEventBack(json) {
		resetSelectOption('vcPro',json.orgProList,'ORG_NAME','ORG_ID',json.defaultValue,json.isdisabled);
		resetSelectOption('vehicleCompany',null,null,null,null,null);
	}
	
	// 艾春 9.13 修改城市回调方法
	function changeMyCityEvent(value,defaultValue,isdisabled){
		if(''!=value){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeCity.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeMyCityBack,'fm','');
			resetSelectOption('vehicleCompany',null,null,null,null,null);
		}else{
			resetSelectOption('myCity',null,null,null,null,null);
			resetSelectOption('vehicleCompany',null,null,null,null,null);
		}
	}
	//经销商回调方法：
	function changeMyCityBack(json) {
		resetSelectOption('myCity',json.myCityList,'CITY_NAME','CITY_ID',json.defaultValue,json.isdisabled);
	}
	
	// 艾春 9.13 修改经销商回调方法
	function changeVcProEvent(value,defaultValue,isdisabled){
		if(''!=value){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeOrgDealer1.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeVcProBack,'fm','');
		}else{
			resetSelectOption('vehicleCompany',null,null,null,null,null);
		}
	}
	//经销商回调方法：
	function changeVcProBack(json) {
		resetSelectOption('vehicleCompany',json.dealerList,'DEALER_NAME','DEALER_ID',json.defaultValue,json.isdisabled);
	}
	
	function changeBizTypeEvent(value,defaultValue,isdisabled){
		makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeBizContent.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeBizTypeBack,'fm','');
		//隐藏或显示
		//投诉显示
		if(value == <%=Constant.VOUCHER_TYPE_01%>){
			isDisplay(true);
		//咨询隐藏
		}else {
			isDisplay(false);
		}
	}
	//内容类型回调方法：
	function changeBizTypeBack(json) {
		resetSelectOptionConsult('contType',json.bclist,'CODEDESCVIEW','CODEID',json.defaultValue,json.isdisabled);
		//删除已处理
		for (var i=0;i<json.processList.length;i++){
			if(json.processList[i].CODE_ID ==<%=Constant.CONSULT_PROCESS_FINISH%>){
				json.processList.splice(i,1);
			}
		}
	}
	//重置下拉框数据 特殊内容类型处理
	function resetSelectOptionConsult(id,maps,dataName,dataValue,dataId,isdisabled){
		clearSelectNode(id);
		addSelectNodeConsult(id,maps,dataName,dataValue,dataId,isdisabled);
	}
	
	
	//重置下拉框数据
	function resetSelectOption(id,maps,dataName,dataValue,dataId,isdisabled){
		clearSelectNode(id);
		addSelectNode(id,maps,dataName,dataValue,dataId,isdisabled);
	}
	
	//动态删除下拉框节点
	function clearSelectNode(id) {			
		document.getElementById(id).options.length=0; 			
	}
	//动态添加下拉框节点
	function addSelectNode(id,maps,dataName,dataValue,dataId,isdisabled){
		document.getElementById(id).options.add(new Option('-请选择-',''));
		if(maps != null){
			for(var i = 0; i<maps.length;i++){
				if((maps[i])['' +dataValue+''] == dataId){
					document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,true));
				}
				else{
					document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,false));
				}
			}
		}
		
		if(isdisabled == 'true' || isdisabled == true){
			document.getElementById(id).disabled = "disabled";
		}else{
			document.getElementById(id).disabled = "";
		}
		
	}
	
	//特殊内容类型处理
	function addSelectNodeConsult(id,maps,dataName,dataValue,dataId,isdisabled){
		document.getElementById(id).options.add(new Option('-请选择-',''));
		if(maps != null){
			for(var i = 0; i<maps.length;i++){
				if((maps[i])['' +dataValue+''] == dataId){
					var option = new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,true);
					if((maps[i])['MAXLEV'] != (maps[i])['LEV']){
						option.disabled = true;
					}
					document.getElementById(id).options.add(option);
				}
				else{
					var option = new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,false);
					if((maps[i])['MAXLEV'] != (maps[i])['LEV']){
						option.disabled = true;
					}
					document.getElementById(id).options.add(option);
				}
			}
		}
		
		if(isdisabled == 'true' || isdisabled == true){
			document.getElementById(id).disabled = "disabled";
		}else{
			document.getElementById(id).disabled = "";
		}
		
	}
	
	
	function checkSet1(){
		$("vcPro").value = "";
		$("vcPro").disabled = true;
		$("myCity").value = "";
		$("myCity").disabled = true;
		$("vehicleCompany").value = "";
		$("vehicleCompany").disabled = true;
		$("orgId").removeAttribute("disabled");
	}
	function checkSet2(){
		$("vcPro").removeAttribute("disabled");
		$("myCity").removeAttribute("disabled");
		$("vehicleCompany").removeAttribute("disabled");
		$("orgId").value = "";
		$("orgId").disabled = true;
	}
	
	function setComplaintPerson(){
    	if(confirm("确定分派吗？")){
    		if(check()){
    		var cpId=	$("cpId").value;
    		var orgId=	$("orgId").value;
    		var vehic=  $("vehicleCompany").value;
			makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptKh/complaintKHSet.json?cpId='+cpId+'&orgId='+orgId+'&vehicleCompany='+vehic,setBack,'fm','');
				}
    	}
	}
	function setBack(json){
		if(json.success != null && json.success=='true'){
			MyAlert("分派成功!");
			_hide();
		}else{
			MyAlert("分派失败,请联系管理员!");
		}
	}
	
	function check(){ 
		var msg ="";
		var hl="";
		var obj = document.getElementsByName("hasCarCustomer");
		 for(var i=0; i<obj.length; i ++){
		        if(obj[i].checked){
		            hl=obj[i].value;
		        }
		    }
		if(""==document.getElementById('orgId').value&&"0"==hl){
			msg+="分派大区不能为空!</br>"
		}
		if(""==document.getElementById('vehicleCompany').value&&"1"==hl){
			msg+="分派服务站不能为空!</br>"
		}
		
		if(msg!=""){
			MyAlert(msg);
			return false;
		}else{
			return true;
		}
	}
	
	

</script>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>咨询处理</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 投诉咨询管理 &gt;咨询处理</div>
	<form method="post" name = "fm" id="fm">
		<table width="100%" class="tab_edit">
			<th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />投诉单信息</th>
			<tr>
				<td width="20%" align="right" nowrap="true">客户名称：</td>
				<td width="30%" align="left" nowrap="true">
					<input id="ctmname" name="ctmname" type="text" value="${complaintAcceptMap.CTMNAME}">					
					<font color="red">*</font>
				</td>
				<td width="20%" align="right" nowrap="true">联系电话：</td>
				<td width="30%" align="left">
					<input id="phone" name="phone" type="text" value="${complaintAcceptMap.PHONE}" maxlength="40">
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">VIN：</td>
				<td align="left" nowrap="true">
					${complaintAcceptMap.VIN}
				</td>
				<td align="right" nowrap="true">行驶里程：</td>
				<td align="left">
					${complaintAcceptMap.MILEAGE}	
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">里程范围：</td>
				<td align="left" nowrap="true">
					${complaintAcceptMap.MILEAGERANGE}
				</td>
				<td align="right" nowrap="true">车辆用途：</td>
				<td align="left">
					${complaintAcceptMap.VINUSER}
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">省份：</td>
				<td align="left" nowrap="true">
					${complaintAcceptMap.PRO}
				</td>
				<td align="right" nowrap="true">城市：</td>
				<td align="left">
					${complaintAcceptMap.CITY}
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">车种：</td>
				<td align="left" nowrap="true">
					${complaintAcceptMap.SGROUP}
				</td>
				<td align="right" nowrap="true">购车日期：</td>
				<td align="left">
					${complaintAcceptMap.BUYDATE}
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">生产日期：</td>
				<td align="left" nowrap="true">
					${complaintAcceptMap.PRDATE}
				</td>
				<td align="right" nowrap="true">车型：</td>
				<td align="left">
					${complaintAcceptMap.MGROUP}
				</td>
			</tr>
			
			<tr>
              <td align="right" nowrap="true">处理结果记录：</td>
              <td valign="top" colspan="3">
              	<table width="100%" class="tab_edit">
              		<tr>
	              		<th align="center" width="20%">处理时间</th>
	              		<th align="center" width="50%">处理内容</th>
	              		<th align="center" width="10%">反馈人</th>
	              		<th align="center" width="20%">当前处理人</th>
              		</tr>
              		<c:forEach items="${dealRecordList}" var="dealR">
              			<tr>
              				<td align="center">${dealR.CDDATE}</td>
              				<td align="center">${dealR.CDCONT}</td>
              				<td align="center">${dealR.USERNAME}</td>
              				<td align="center">${dealR.NEXTNAME}</td>
              			</tr>
              		</c:forEach>
              	</table>
              </td>
            </tr>
			<tr>
				<td align="right" nowrap="true">业务类型：</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
						document.write(getItemValue(${complaintAcceptMap.BIZTYPE}));
	       			</script>		
				</td>
				<td align="right" nowrap="true">内容类型：</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
						document.write(getItemValue(${complaintAcceptMap.BIZCONT}));
	       			</script>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">处理方式：</td>
				<td align="left" nowrap="true" colspan="3">
					<script type="text/javascript">
						document.write(getItemValue(${complaintAcceptMap.DEALMODE}));
	       			</script>
				</td>
			</tr>
			
			<tr id="rcontTr">
				<td align="right" nowrap="true">回复内容：</td>
				<td align="left" nowrap="true" colspan="3">
				<textarea id="rcont" name="rcont" rows="5" style="width: 95%">${complaintAcceptMap.RCONT}</textarea>
				<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">抱怨咨询内容：</td>
				<td align="left" nowrap="true" colspan="3">
					<textarea id="complaintContent" name="complaintContent" rows="5" style="width: 95%">${complaintAcceptMap.CPCONT}</textarea>
					<font color="red">*</font>
					<input type="hidden" id="cpid" name="cpid" value="${complaintAcceptMap.CPID}">
				</td>
			</tr>		
			
			<tr>
				<td colspan="8" align="center">
        			<c:choose>
						<c:when test="${openPage==1}">
							<input class="normal_btn" type="button" value="保存" name="saveButton" id="saveButton" onclick="save()" />
							&nbsp;
							<input name="button" type="button" class="long_btn" onclick="window.close();" value="关闭" /> 
						</c:when>
						<c:otherwise>
							<input class="normal_btn" type="button" value="保存" name="saveButton" id="saveButton" onclick="save()" />
							&nbsp;
							<input name="addBtn" type="button" class="normal_btn"  value="返回" onclick="history.back();" />
						</c:otherwise>
					</c:choose>
        		</td>
			</tr>
		</table>
		
	</form>
	<script type="text/javascript">
		function save(){
			//验证
			if(check()){
				document.getElementById("saveButton").disabled = true;
				makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/ConsultSearch/consultSearchEditSubmit.json',saveBack,'fm','');
			}
		}
		
		function saveBack(json){
			if(json.success != null && json.success=='true'){
				MyAlertForFun("保存成功!",sendPage);
			}else{
				MyAlert("保存失败,请联系管理员!");
			}
			document.getElementById("saveButton").disabled = false;
		}
		
		function check(){ 
			var msg ="";			
			if(""==document.getElementById('ctmname').value){
				msg+="客户名称不能为空!</br>"
			}
			if(""==document.getElementById('phone').value){
				msg+="联系电话不能为空!</br>"
			}
			if(""==document.getElementById('complaintContent').value){
				msg+="抱怨咨询内容不能为空!</br>"
			}else if(!WidthCheck(document.getElementById('complaintContent').value,1000)){
				msg+="抱怨咨询内容太长!</br>"
			}
			if(""==document.getElementById('rcont').value){
				msg+="回复内容不能为空!</br>"
			}
			if(msg!=""){
				MyAlert(msg);
				return false;
			}else{
				return true;
			}
		}
		
		//   判断长度是否合格 
	// 
	// 引数 s   传入的字符串 
	//   n   限制的长度n以下 
	function WidthCheck(s, n){   
		var w = 0;   
		for (var i=0; i<s.length; i++) {   
		   var c = s.charCodeAt(i);   
		   //单字节加1   
		   if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) {   
		    w++;   
		   }   
		   else {   
		    w+=2;   
		   }   
		}   
		if (w > n) {   
		   return false;   
		}   
		return true;   
	}
		
		//页面跳转：
		function sendPage(){
			opener.location="javascript:refreshData()";
			window.close();
			//window.location.href = "<%=contextPath%>/customerRelationships/complaintConsult/ConsultSearch/consultSearchInit.do";
		}
		
		function changeBizTypeEvent(value,defaultValue,isdisabled){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeBizContent.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeBizTypeBack,'fm','');
			//隐藏或显示
			//投诉显示
			if(value == <%=Constant.TYPE_COMPLAIN%>){
				isDisplay(true);
			//咨询隐藏
			}else {
				isDisplay(false);
			}
			document.getElementById('rcontTr').style.display = 'none';
			document.getElementById('dealuserTr').style.display = 'none';
		}
		//内容类型回调方法：
		function changeBizTypeBack(json) {
			resetSelectOption('contType',json.bclist,'CODEDESCVIEW','CODEID',json.defaultValue,json.isdisabled);
			//删除已处理
			for (var i=0;i<json.processList.length;i++){
				if(json.processList[i].CODE_ID ==<%=Constant.CONSULT_PROCESS_SPOT%>){
					json.processList.splice(i,1);
				}
			}
			resetSelectOption('dealModel',json.processList,'CODE_DESC','CODE_ID',json.defaultValue,json.isdisabled);
		}
		
		function changeVcProEvent(value,defaultValue,isdisabled){
			if(''!=value){
				makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeOrgDealer.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeVcProBack,'fm','');
			}else{
				resetSelectOption('vehicleCompany',null,null,null,null,null);
			}
		}
		//经销商回调方法：
		function changeVcProBack(json) {
			resetSelectOption('vehicleCompany',json.dealerList,'DEALER_NAME','DEALER_ID',json.defaultValue,json.isdisabled);
		}
		
				
		function changeDealuserEvent(value,defaultValue,isdisabled){
			if(''!=value){
				makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeOrg.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeDealuserEventBack,'fm','');
			}else{
				checkedSelect('dealuser','',false);
			}
		}
		
		function changeDealuserEventBack(json){
			resetSelectOption('dealuser',json.orgUserList,'NAME','USER_ID',json.defaultValue,json.isdisabled);
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
			for(var i = 0; i<maps.length;i++){
				if((maps[i])['' +dataValue+''] == dataId){
					document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,true));
				}
				else{
					document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,false));
				}
			}
			if(isdisabled == 'true' || isdisabled == true){
				document.getElementById(id).disabled = "disabled";
			}else{
				document.getElementById(id).disabled = "";
			}
			
		}
		function changeDealModelEvent(value){
			if(value == <%=Constant.COMPLAINT_PROCESS_TURN%> ){
				document.getElementById('rcontTr').style.display = 'none';
				document.getElementById('dealuserTr').style.display = '';
			}else if(value == <%=Constant.CONSULT_PROCESS_FINISH%> || value == <%=Constant.CONSULT_PROCESS_WAIT%> ){
				document.getElementById('rcontTr').style.display = '';
				document.getElementById('dealuserTr').style.display = 'none';
			}else{
				document.getElementById('rcontTr').style.display = 'none';
				document.getElementById('dealuserTr').style.display = 'none';
			}
		}
		
		//是否显示  isDisplay->true 显示 false 隐藏
		function isDisplay(isDisplay){
			if(isDisplay){
				document.getElementById('complaintTrOne').style.display = '';
				document.getElementById('complaintTrTwo').style.display = '';
			}else{
				document.getElementById('complaintTrOne').style.display = 'none';
				document.getElementById('complaintTrTwo').style.display = 'none';
			}
		}
	</script>
</body>
</html>
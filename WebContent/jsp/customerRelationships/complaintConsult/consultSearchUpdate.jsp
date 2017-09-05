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
					${complaintAcceptMap.CTMNAME}
				</td>
				<td width="20%" align="right" nowrap="true">联系电话：</td>
				<td width="30%" align="left">
					${complaintAcceptMap.PHONE}
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
					<select id="bizType" name="bizType" class="short_sel"  onchange="changeBizTypeEvent(this.value,'',false)">
						<option value=''>-请选择-</option>
						<c:forEach var="bt" items="${bizTypeList}">
							<c:choose>
								<c:when test="${complaintAcceptMap.BIZTYPE == bt.id}">
									<option value="${bt.id}" title="${bt.name}" selected="selected">${bt.name}</option>
								</c:when>
								<c:otherwise>
									<option value="${bt.id}" title="${bt.name}">${bt.name}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<font color="red">*</font>
				</td>
				<td align="right" nowrap="true">内容类型：</td>
				<td align="left" nowrap="true">
					<select id="contType" name="contType" class="short_sel">
						<option value=''>-请选择-</option>
						<c:forEach var="bc" items="${bclist}">
							<c:choose>
								<c:when test="${complaintAcceptMap.BIZCONT == bc.CODE_ID}">
									<option value="${bc.CODE_ID}" title="${bc.CODE_DESC}" selected="selected">${bc.CODE_DESC}</option>
								</c:when>
								<c:otherwise>
									<option value="${bc.CODE_ID}" title="${bc.CODE_DESC}">${bc.CODE_DESC}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<font color="red">*</font>
				</td>
			</tr>
			
			<tr id="complaintTrOne" style="display: none">
				<td align="right" nowrap="true">抱怨级别：</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
						genSelBoxExp("cplevel",<%=Constant.COMPLAINT_LEVEL%>,"",true,"short_sel","","false",'');
					</script>
					<font color="red">*</font>
				</td>
				<td align="right" nowrap="true">规定处理期限：</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
						genSelBoxExp("cplimit",<%=Constant.RULE_LIMIT%>,"",true,"short_sel","","false",'');
					</script>
					<font color="red">*</font>
				</td>
			</tr>
			<tr id="complaintTrTwo" style="display: none">
				<td align="right" nowrap="true">抱怨对象：</td>
				<td align="left" nowrap="true" colspan="3">
					<select id="cpObject" name="cpObject" class="short_sel">
						<option value=''>-请选择-</option>
						<c:forEach var="cpObj" items="${cpObjectList}">
							<option value="${cpObj.ORG_ID}" title="${cpObj.ORG_NAME}">${cpObj.ORG_NAME}</option>
						</c:forEach>
					</select>
					经销商
					<select id="vcPro" name="vcPro" class="short_sel" onchange="changeVcProEvent(this.value,'',false)">
						<option value=''>-请选择-</option>
						<c:forEach var="os" items="${tmOrgSmallList}">
							<option value="${os.orgId}" title="${os.orgName}">${os.orgName}</option>
						</c:forEach>
					</select>
					<select id="vehicleCompany" name="vehicleCompany" class="short_sel">
						<option value=''>-请选择-</option>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">抱怨咨询内容：</td>
				<td align="left" nowrap="true" colspan="3">
					<textarea id="complaintContent" name="complaintContent" rows="5" style="width: 95%">${complaintAcceptMap.CPCONT}</textarea>
					<font color="red">*</font>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">处理方式：</td>
				<td align="left" nowrap="true" colspan="3">
					<select id="dealModel" name="dealModel" class="short_sel"  onchange="changeDealModelEvent(this.value)">
						<option value=''>-请选择-</option>
						<c:forEach var="dm" items="${dealModelList}">
							<c:choose>
								<c:when test="${complaintAcceptMap.DEALMODE == dm.CODE_ID}">
									<option value="${dm.CODE_ID}" title="${dm.CODE_DESC}" selected="selected">${dm.CODE_DESC}</option>
								</c:when>
								<c:otherwise>
									<option value="${dm.CODE_ID}" title="${dm.CODE_DESC}">${dm.CODE_DESC}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<font color="red">*</font>
				</td>
			</tr>
			
			<tr id="rcontTr">
				<td align="right" nowrap="true">回复内容：</td>
				<td align="left" nowrap="true" colspan="3">
					<textarea id="rcont" name="rcont" rows="5" style="width: 95%">${complaintAcceptMap.RCONT}</textarea>
					<font color="red">*</font>
				</td>
			</tr>
			
			<tr style="display: none" id="dealuserTr">
				<td align="right" nowrap="true">处理人：</td>
				<td align="left">
					<select id="cpObject" name="cpObject" class="short_sel" onchange="changeDealuserEvent(this.value,'',false)">
						<option value=''>-请选择-</option>
						<c:forEach var="tmOrg" items="${tmOrgList}">
							<option value="${tmOrg.orgId}" title="${tmOrg.orgName}">${tmOrg.orgName}</option>
						</c:forEach>
					</select>
					<select id="dealuser" name="dealuser" class="short_sel">
						<option value=''>-请选择-</option>
					</select>
					<font color="red">*</font>
					<input type="hidden" value="${complaintAcceptMap.CPID}" id="cpid" name="cpid">
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
				makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/ConsultSearch/consultSearchUpdateSubmit.json',saveBack,'fm','');
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
			if(""==document.getElementById('complaintContent').value){
				msg+="抱怨咨询内容不能为空!</br>"
			}else if(!WidthCheck(document.getElementById('complaintContent').value,1000)){
				msg+="抱怨咨询内容太长!</br>"
			}
			if(""==document.getElementById('bizType').value){
				msg+="业务类型不能为空!</br>"
			}
			
			if(""==document.getElementById('contType').value){
				msg+="内容类型不能为空!</br>"
			}
			if(document.getElementById('complaintTrOne').style.display != "none"){
				if(""==document.getElementById('cplevel').value){
				msg+="抱怨级别不能为空!</br>"
				}
				if(""==document.getElementById('cplimit').value){
					msg+="规定处理期限不能为空!</br>"
				}
				if(""!=document.getElementById('cpObject').value && ""!=document.getElementById('vehicleCompany').value){
					msg+="抱怨对象只能选其一!</br>"
				}
				if(""==document.getElementById('cpObject').value && ""==document.getElementById('vehicleCompany').value){
					msg+="抱怨对象必选其一!</br>"
				}
			}
			
			if(""==document.getElementById('dealModel').value){
				msg+="处理方式不能为空!</br>"
			}
			
			if(""==document.getElementById('dealuser').value && document.getElementById('dealuserTr').style.display != "none" ){
				msg+="处理人不能为空!</br>"
			}
			
			if(""==document.getElementById('rcont').value && document.getElementById('rcontTr').style.display != "none" ){
				msg+="回复内容不能为空!</br>"
			}else if(!WidthCheck(document.getElementById('rcont').value,1000) && document.getElementById('rcontTr').style.display != "none"){
				msg+="回复内容太长!</br>"
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
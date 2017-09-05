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
<title>坐席修改</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	
	function isAdminEvent(){
		checkedSelect('isSeats',<%=Constant.IF_TYPE_NO%>,'disabled');
		document.getElementById('isSeatsH').value = '<%=Constant.IF_TYPE_NO%>';
		checkedSelect('seatTeam','','disabled');
		checkedSelect('level','','disabled');
	}
	
	function checkedSelect(id,dataId,isdisabled){
		var obj = document.getElementById(id);
		for(var i=0;i<obj.options.length;i++){
			if(obj.options[i].value == dataId) obj.options[i].selected = 'selected';
		}
		obj.disabled = isdisabled;
	}
</script>
</head>
<body>
	<c:choose>
		<c:when test="${empty ttCrmSeatsPO  }">
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 基础设定 &gt;坐席新增</div>
		</c:when>
		<c:otherwise>
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 基础设定 &gt;坐席修改</div>
		</c:otherwise>
	</c:choose>
	
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />坐席信息</th>
			
			<tr>
				<td align="right" nowrap="true">用户名：</td>
				<td align="left" nowrap="true">
					<c:out value="${ttCrmSeatsPO.seName}"/>
					<input type="hidden" id="seName" name="seName" value="${ttCrmSeatsPO.seName}"/>
					<input type="hidden" id="seUserId" name="seUserId" value="${ttCrmSeatsPO.seUserId}"/>
				</td>
				<td class="table_query_2Col_label_6Letter">帐号：</td>
				<td align="left">
					<c:out value="${ttCrmSeatsPO.seAccount}"/>
					<input type="hidden" id="seAccount" name="seAccount" value="${ttCrmSeatsPO.seAccount}"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">是否坐席：</td>
				<td align="left" nowrap="true">
					<select id="isSeats" name="isSeats" class="short_sel" >
						<c:forEach var="isSt" items="${isSeatsList}">
							<c:choose>
								<c:when test="${isSt.CODE_ID == ttCrmSeatsPO.seIsSeats}">
									<option value="${isSt.CODE_ID}" title="${isSt.CODE_DESC}" selected="selected">${isSt.CODE_DESC}</option> 
								</c:when>
								<c:otherwise>
									<option value="${isSt.CODE_ID}" title="${isSt.CODE_DESC}">${isSt.CODE_DESC}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<input type="hidden" id="isSeatsH" name="isSeatsH" >
				</td>
				<td class="table_query_2Col_label_6Letter">工号：</td>
				<td align="left">
					<input type="hidden" id="seSeatsNo" name="seSeatsNo" value="${ttCrmSeatsPO.seSeatsNo}"/>${ttCrmSeatsPO.seSeatsNo}
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">分机号：</td>
				<td align="left" nowrap="true">
					<select id="seExt" name="seExt" class="short_sel" >
						<option value="" >-请选择-</option>
						<c:forEach var="ext" items="${seatsExtList}">
							<c:choose>
								<c:when test="${ext.CODE_ID == ttCrmSeatsPO.seExt}">
									<option value="${ext.CODE_ID}" title="${ext.CODE_DESC}" selected="selected">${ext.CODE_DESC}</option> 
								</c:when>
								<c:otherwise>
									<option value="${ext.CODE_ID}" title="${ext.CODE_DESC}">${ext.CODE_DESC}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
				<td class="table_query_2Col_label_6Letter" style="display: none;">IP地址：</td>
				<td align="left" style="display: none;" >
					<input type="text" id="seIp" name="seIp" value="${ttCrmSeatsPO.seIp}"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">坐席组：</td>
				<td align="left" nowrap="true">
					<select id="seatTeam" name="seatTeam" class="short_sel" >
						<option value="" >-请选择-</option>
						<c:forEach var="ttCrmSeatsTeamPO" items="${seatTeamList}">
							<c:choose>
								<c:when test="${ttCrmSeatsPO.stId == ttCrmSeatsTeamPO.stId}">
									<option value="${ttCrmSeatsTeamPO.stId}" title="${ttCrmSeatsTeamPO.stName}" selected="selected">${ttCrmSeatsTeamPO.stName}</option> 
								</c:when>
								<c:otherwise>
									<option value="${ttCrmSeatsTeamPO.stId}" title="${ttCrmSeatsTeamPO.stName}">${ttCrmSeatsTeamPO.stName}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
				<td class="table_query_2Col_label_6Letter">坐席级别：</td>
				<td align="left">
				<select id="level" name="level" class="short_sel" >
						<option value="" >-请选择-</option>
						<c:forEach var="seatLevel" items="${seatLevelList}">
							<c:choose>
								<c:when test="${ttCrmSeatsPO.seLevel == seatLevel.CODE_ID}">
									<option value="${seatLevel.CODE_ID}" title="${seatLevel.CODE_DESC}" selected="selected">${seatLevel.CODE_DESC}</option> 
								</c:when>
								<c:otherwise>
									<option value="${seatLevel.CODE_ID}" title="${seatLevel.CODE_DESC}">${seatLevel.CODE_DESC}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
				</select>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">是否管理员：</td>
				<td align="left" nowrap="true">
					<select id="isAdmin" name="isAdmin" class="short_sel" onchange="isAdminChangeEvent(this.value)">
						<c:forEach var="isA" items="${isAdminList}">
							<c:choose>
								<c:when test="${isA.CODE_ID == ttCrmSeatsPO.seIsManamger}">
									<option value="${isA.CODE_ID}" title="${isA.CODE_DESC}" selected="selected">${isA.CODE_DESC}</option> 
									<c:if test="${isA.CODE_ID == 95221001}">
										<script type="text/javascript">
											isAdminEvent();
										</script>
									</c:if>
								</c:when>
								<c:otherwise>
									<option value="${isA.CODE_ID}" title="${isA.CODE_DESC}">${isA.CODE_DESC}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="保存" name="recommit" id="queryBtn" onclick="checkForm();" />
					&nbsp;
          			<input name="addBtn" type="button" class="normal_btn"  value="返回" onclick="history.back();" />
        		</td>
			</tr>
		</table>
		
	</form>
	<script type="text/javascript">
		function checkForm(){
			if(check()){
				MyConfirm("是否确认修改？",editsubmit,"");
			}
		}

		function editsubmit(){
			makeNomalFormCall('<%=contextPath%>/customerRelationships/baseSetting/seatsSet/updateSeatsSetSubmit.json?',editBack,'fm','');
		}
		
		function check(){ 
			var msg ="";
			
			if(msg!=""){
				MyAlert(msg);
				return false;
			}else{
				return true;
			}
		}	
		//回调方法：
		function editBack(json) {
			if(json.success != null && json.success=='true'){
				document.getElementById("queryBtn").disabled = true;
				MyAlertForFun("修改成功",sendPage);
			}else{
				MyAlert("修改失败！请联系管理员");
				document.getElementById("queryBtn").disabled = false;
			}
		}
		
		function isAdminChangeEvent(value){
			//当选择是管理员时
			if(value == <%=Constant.se_is_manamger_1%>){
				isAdminEvent();
			//当选择不是管理员时
			}else if(value == <%=Constant.se_is_manamger_2%>){
				notIsAdminEvent();
			}
		}
		
		function isAdminEvent(){
			checkedSelect('isSeats',<%=Constant.IF_TYPE_NO%>,'disabled');
			document.getElementById('isSeatsH').value = '<%=Constant.IF_TYPE_NO%>';
			checkedSelect('seatTeam','','disabled');
			checkedSelect('level','','disabled');
		}
		
		function notIsAdminEvent(){
			show();
		}
		
		function show(){
			document.getElementById('isSeats').disabled = "";
			document.getElementById('seatTeam').disabled = "";
			document.getElementById('level').disabled = "";
			document.getElementById('isSeatsH').value = '';
		}
		
		function checkedSelect(id,dataId,isdisabled){
			var obj = document.getElementById(id);
			for(var i=0;i<obj.options.length;i++){
				if(obj.options[i].value == dataId) obj.options[i].selected = 'selected';
			}
			obj.disabled = isdisabled;
		}
		
		//页面跳转：
		function sendPage(){
			window.location.href = "<%=contextPath%>/customerRelationships/baseSetting/seatsSet/seatsSetInit.do";
		}

	</script>
</body>
</html>
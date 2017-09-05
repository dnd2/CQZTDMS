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
<title>班次类型设置新增</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
	<c:choose>
		<c:when test="${empty ttCrmWorktimePO  }">
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 基础设定 &gt;班次类型新增</div>
		</c:when>
		<c:otherwise>
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 基础设定 &gt;班次类型修改</div>
		</c:otherwise>
	</c:choose>
	
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />班次类型信息</th>
			
			<tr>
				<td align="right" nowrap="true">班次类型：</td>
				<td align="left" nowrap="true">
					<select id="wtType" name="wtType" class="short_sel" >
						<c:forEach var="st" items="${sts}">
							<c:choose>
								<c:when test="${st.CODE_ID == ttCrmWorktimePO.wtType}">
									<option value="${st.CODE_ID}" title="${st.CODE_DESC}" selected="selected">${st.CODE_DESC}</option> 
								</c:when>
								<c:otherwise>
									<option value="${st.CODE_ID}" title="${st.CODE_DESC}">${st.CODE_DESC}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>          
				</td>
				<td align="right" nowrap="true"></td>
				<td align="left" nowrap="true">
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter">上班时钟：</td>
				<td align="left">
					<select id="workAmStartHourTime" name="workAmStartHourTime" class="short_sel" >
						<option value=''>-请选择-</option>
						<c:forEach var="hourstr" items="${hourStr}">
							<c:choose>
								<c:when test="${ttCrmWorktimePO.wtStaOnMinute == hourstr}">
									<option value="${hourstr}" title="${hourstr}" selected="selected">${hourstr}</option> 
								</c:when>
								<c:otherwise>
									<option value="${hourstr}" title="${hourstr}">${hourstr}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<font color="red">*</font>
				</td>
				<td class="table_query_2Col_label_6Letter">上班分钟：</td>
				<td align="left">
					<select id="workAmStartMinTime" name="workAmStartMinTime" class="short_sel"">
						<option value=''>-请选择-</option>
						<c:forEach var="minstr" items="${minStr}">
							<c:choose>
								<c:when test="${ttCrmWorktimePO.wtStaOffMinute2 == minstr || (ttCrmWorktimePO.wtStaOffMinute2 == 0 && minstr == '00')}">
									<option value="${minstr}" title="${minstr}" selected="selected">${minstr}</option>
								</c:when>
								<c:otherwise>
									<option value="${minstr}" title="${minstr}">${minstr}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter">下班时钟：</td>
				<td align="left">
					<select id="workPmEndHourTime" name="workPmEndHourTime" class="short_sel" >
						<option value=''>-请选择-</option>
						<c:forEach var="hourstr" items="${hourStr}">
							<c:choose>
								<c:when test="${ttCrmWorktimePO.wtEndOnMinute == hourstr}">
									<option value="${hourstr}" title="${hourstr}" selected="selected">${hourstr}</option>
								</c:when>
								<c:otherwise>
									<option value="${hourstr}" title="${hourstr}">${hourstr}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<font color="red">*</font>
				</td>
				<td class="table_query_2Col_label_6Letter">下班分钟：</td>
				<td align="left">
					<select id="workPmEndMinTime" name="workPmEndMinTime" class="short_sel"">
						<option value=''>-请选择-</option>
						<c:forEach var="minstr" items="${minStr}">
							<c:choose>
								<c:when test="${ttCrmWorktimePO.wtEndOffMinute == minstr || (ttCrmWorktimePO.wtEndOffMinute == 0 && minstr == '00')}">
									<option value="${minstr}" title="${minstr}" selected="selected">${minstr}</option>
								</c:when>
								<c:otherwise>
									<option value="${minstr}" title="${minstr}">${minstr}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<font color="red">*</font>
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
			if(document.getElementById('workAmStartHourTime').value==''){
				MyAlert('上班时钟不能为空!');
			}else if(document.getElementById('workAmStartMinTime').value==''){
				MyAlert('上班分钟不能为空!');
			}else if(document.getElementById('workPmEndHourTime').value==''){
				MyAlert('下班时钟不能为空!');
			}else if(document.getElementById('workPmEndMinTime').value==''){
				MyAlert('下班分钟不能为空!');
			}else{
				if(${empty ttCrmWorktimePO}){
					addsubmit();
				}else{
					MyConfirm("是否确认修改？",editsubmit,"");
				}
				
			}
		}
		function addsubmit(){
			makeNomalFormCall('<%=contextPath%>/customerRelationships/baseSetting/workTimeSet/addWorkTimeSetSubmit.json',addBack,'fm','');
		}
		function editsubmit(){
			makeNomalFormCall('<%=contextPath%>/customerRelationships/baseSetting/workTimeSet/addWorkTimeSetSubmit.json?id=${ttCrmWorktimePO.wtId}',editBack,'fm','');
		}
		//回调方法：
		function addBack(json) {
			if(json.success != null && json.success=='true'){
				document.getElementById("queryBtn").disabled = true;
				MyAlertForFun("新增成功",sendPage);
			}else{
				MyAlert("新增失败！请联系管理员");
				document.getElementById("queryBtn").disabled = false;
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
		
		//页面跳转：
		function sendPage(){
			window.location.href = "<%=contextPath%>/customerRelationships/baseSetting/workTimeSet/workTimeSetInit.do";
		}

	</script>
</body>
</html>
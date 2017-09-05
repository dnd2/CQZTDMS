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
<title>坐席组新增修改</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
	<c:choose>
		<c:when test="${empty ttCrmSeatsTeamPO  }">
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 基础设定 &gt;坐席组新增</div>
		</c:when>
		<c:otherwise>
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 基础设定 &gt;坐席组修改</div>
		</c:otherwise>
	</c:choose>
	
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />坐席组信息</th>
			<tr>
				<td align="right" nowrap="true">组编号：</td>
				<td align="left" nowrap="true">
					<c:choose>
						<c:when test="${empty ttCrmSeatsTeamPO  }">
							<input type="text" id="seatsTeamCode" name="seatsTeamCode" value="${ttCrmSeatsTeamPO.stCode}" datatype="0,isDigit,25" maxlength="25"/>
						</c:when>
						<c:otherwise>
							${ttCrmSeatsTeamPO.stCode}<input type="hidden" id="seatsTeamCode" name="seatsTeamCode" value="${ttCrmSeatsTeamPO.stCode}" datatype="0,isDigit,25" maxlength="25"/>
						</c:otherwise>
					</c:choose>
					          
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">组名称：</td>
				<td align="left" nowrap="true">
					<input type="text" id="seatsTeamName" name="seatsTeamName" value="${ttCrmSeatsTeamPO.stName}" datatype="0,is_name,16" maxlength="16"/>          
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter">备注：</td>
				<td align="left">
					<textarea id="remark" name="remark" style="width: 95%" rows="4"  datatype="1,is_textarea,66">${ttCrmSeatsTeamPO.stMemo}</textarea>
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
			if(${empty ttCrmSeatsTeamPO}){
				addsubmit();
			}else{
				MyConfirm("是否确认修改？",editsubmit,"");
			}		
		}
		function addsubmit(){
			makeNomalFormCall('<%=contextPath%>/customerRelationships/baseSetting/seatsTeamSet/addOrUpdateSeatsTeamSetSubmit.json',addBack,'fm','');
		}
		function editsubmit(){
			makeNomalFormCall('<%=contextPath%>/customerRelationships/baseSetting/seatsTeamSet/addOrUpdateSeatsTeamSetSubmit.json?id=${ttCrmSeatsTeamPO.stId}',editBack,'fm','');
		}
		//回调方法：
		function addBack(json) {
		  if(json.bug == "false")
		  {
			  	MyAlert("作息编号不能重复");
			    document.getElementById("queryBtn").disabled = false;
		  }else if(json.success != null && json.success=='true'){
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
			window.location.href = "<%=contextPath%>/customerRelationships/baseSetting/seatsTeamSet/seatsTeamSetInit.do";
		}

	</script>
</body>
</html>
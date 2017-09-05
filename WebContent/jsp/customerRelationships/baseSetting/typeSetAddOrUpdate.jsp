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
<title>类型新增修改</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
	<c:choose>
		<c:when test="${empty tcCodePO  }">
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 基础设定 &gt;类型新增</div>
		</c:when>
		<c:otherwise>
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 基础设定 &gt;类型修改</div>
		</c:otherwise>
	</c:choose>
	
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />类型信息</th>
			
			<tr>
				<td align="right" nowrap="true">类型：</td>
				<td align="left" nowrap="true">
				<c:choose>
					<c:when test="${empty tcCodePO  }">
						<select id="type" name="type" class="short_sel">
							<option value=''>-请选择-</option>
							<c:forEach var="type" items="${typeList}">
								<c:choose>
									<c:when test="${type.CODEID == tcCodePO.type}">
										<option value="${type.CODEID},${type.CODEDESC}" title="${type.CODEDESC}" selected="selected">${type.CODEDESCVIEW}</option> 
									</c:when>
									<c:otherwise>
										<option value="${type.CODEID},${type.CODEDESC}" title="${type.CODEDESC}">${type.CODEDESCVIEW}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>    
					</c:when>
					<c:otherwise>
						<input type="hidden" id="type" name="type" value="${tcCodePO.type},${tcCodePO.typeName}"/>
						<select class="short_sel" disabled="disabled">
							<option value=''>-请选择-</option>
							<c:forEach var="type" items="${typeList}">
								<c:choose>
									<c:when test="${type.CODEID == tcCodePO.type}">
										<option value="${type.CODEID},${type.CODEDESC}" title="${type.CODEDESC}" selected="selected">${type.CODEDESCVIEW}</option> 
									</c:when>
									<c:otherwise>
										<option value="${type.CODEID},${type.CODEDESC}" title="${type.CODEDESC}">${type.CODEDESCVIEW}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>   
					</c:otherwise>
				</c:choose>
				<font color="red">*</font>      
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter">类型名称：</td>
				<td align="left">
					<input id="codeDesc" name="codeDesc" value="${tcCodePO.codeDesc}" datatype="0,is_null,32" maxlength="32"/>
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
			if(document.getElementById("type").value =="") {
				MyAlert("类型不能为空!");
				return;
			}
			if(${empty tcCodePO}){
				addsubmit();
			}else{
				MyConfirm("是否确认修改？",editsubmit,"");
			}		
		}
		function addsubmit(){
			makeNomalFormCall('<%=contextPath%>/customerRelationships/baseSetting/typeSet/addOrUpdateTypeSetSubmit.json',addBack,'fm','');
		}
		function editsubmit(){
			var codeDesc = document.getElementById("codeDesc").value;
			var codeDescOwn = '${tcCodePO.codeDesc}';
			if( codeDescOwn == codeDesc){
				MyAlert("修改成功!");
				sendPage();
			}else
			makeNomalFormCall('<%=contextPath%>/customerRelationships/baseSetting/typeSet/addOrUpdateTypeSetSubmit.json?id=${tcCodePO.codeId}',editBack,'fm','');
		}
		//回调方法：
		function addBack(json) {
			if(json.success != null && json.success=='true'){
				document.getElementById("queryBtn").disabled = true;
				MyAlertForFun("新增成功",sendPage);
			}else if(json.repeat != null && json.repeat=='true'){
				MyAlert("数据重复,请重新输入！");
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
			}else if(json.repeat != null && json.repeat=='true'){
				MyAlert("数据重复,请重新输入！");
			}else{
				MyAlert("修改失败！请联系管理员");
				document.getElementById("queryBtn").disabled = false;
			}
		}
		
		//页面跳转：
		function sendPage(){
			window.location.href = "<%=contextPath%>/customerRelationships/baseSetting/typeSet/typeSetInit.do";
		}

	</script>
</body>
</html>
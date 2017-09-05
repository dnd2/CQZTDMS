<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.TmOrgRegionRelationPO" %>
<%@ page import="java.util.List" %>
<%
	String contextPath = request.getContextPath();

	
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>部门人员维护修改</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}

</script>
</head>
<body>

	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理 &gt; 基础设定 &gt; 部门人员维护修改</div>
	
	<form method="post" action="<%=request.getContextPath()%>/customerRelationships/baseSetting/OrgCustomUserRelation/orgCustomUserRelationUpdate.do" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="5"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />部门人员维护修改</th>
			<tr>
				<td align="right" nowrap="true">部门代码：</td>
				<td align="left" nworap="true">
					<input type="text" id=orgCode name="orgCode" value="${orgCode }" />
				</td>
				<td align="right" nowrap="true">部门名称：</td>
				<td align="left" nworap="true">
					<input type="text" id=orgName name="orgName" value="${orgName }" />
				</td>
				<td align="center" nworap="true">
					<input class="normal_btn" type="button" value="查询" name="queryBtn" id="queryBtn" onclick="queryCustomer();" />
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">姓名：</td>
				<td align="left" nowrap="true" colspan="4">
   					${user.NAME}
   					<input type="hidden" id="userid" name="userid" value="${userid}">
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter">部门：</td>
				<td align="left" colspan="4">
					<table width="100%">
						<tr>
							<th></th>
							<th>部门名称</th>
							<th>部门代码</th>
							<th>上级部门</th>
						</tr>
						<c:forEach var="oc" items="${allList}">
							<tr>
								<td>
									<c:choose>
										<c:when test="${oc.isCheck == true}">
											<input type="radio" name='orgids' value='${oc.ORG_ID}' checked="checked"/>
										</c:when>
										<c:otherwise>
											<input type='radio' name='orgids' value='${oc.ORG_ID}'/>
										</c:otherwise>
									</c:choose>
								</td>
								<td>${oc.ORG_NAME}</td>
								<td>${oc.ORG_CODE}</td>
								<td>${oc.PARENT_ORG_NAME}</td>
							</tr>
						</c:forEach>
					</table>
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
		var pIds = new Array(); 
		function addOrgIds(){
			var cnt = 0;
			var chk=document.getElementsByName("orgids");
			var l = chk.length;
			pIds.splice(0,pIds.length);
			for(var i=0;i<l;i++){
				if(chk[i].checked){
			       cnt++;
			       pIds.push(chk[i].value);
				}
			 }
		}
		
		function checkForm(){
			MyConfirm("是否确认修改？",editsubmit,"");	
		}

		function editsubmit(){
			addOrgIds()
			makeNomalFormCall('<%=contextPath%>/customerRelationships/baseSetting/OrgCustomUserRelation/orgCustomUserRelationUpdateSubmit.json?orgids='+pIds,editBack,'fm','');
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
			history.back();
		}

		function queryCustomer(){
			$("fm").submit();
		}
		
		function goBack(){
			window.location.href = "<%=contextPath%>/customerRelationships/baseSetting/OrgCustomUserRelation/orgCustomUserRelationInit.do";
		}
	</script>
</body>
</html>
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
<title>延期授权人员维护修改</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}

</script>
</head>
<body>

	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理 &gt; 基础设定 &gt; 延期授权人员维护修改</div>
	
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />延期授权人员维护修改</th>
			
			<tr>
				<td align="right" nowrap="true">姓名：</td>
				<td align="left" nowrap="true">
   					${user.NAME}
   					<input type="hidden" id="userid" name="userid" value="${userid}">
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter">延期授权：</td>
				<td align="left">
					<table width="100%">
						<tr>
							<th></th>
							<th>部门名称</th>
							<th>延期授权</th>
							<th>部门代码</th>
							<th>上级部门</th>
						</tr>
						<c:forEach var="oc" items="${allList}">
							<tr>
								<td>
									<c:choose>
										<c:when test="${oc.isCheck == true}">
											<script type="text/javascript">
												oldOrgs = ${oc.ORG_ID}
											</script>
											<input type="radio" name='orgids' value='${oc.ORG_ID}' checked="checked" onclick="clearAllCheckBox(${oc.ORG_ID})" readonly="readonly" disabled="disabled"/>
										</c:when>
										<c:otherwise>
											<input type='radio' name='orgids' value='${oc.ORG_ID}' onclick="clearAllCheckBox(${oc.ORG_ID})" readonly="readonly" disabled="disabled"/>
										</c:otherwise>
									</c:choose>
								</td>
								<td>${oc.ORG_NAME}</td>
								<c:choose>
									<c:when test="${oc.isCheck == true}">
											<td>
												<c:choose>
													<c:when test="${oc.isLevelOne == true}">
														<input type="checkbox" name='levels' value='${oc.ORG_ID}-<%=Constant.Level_Manager_01 %>' checked="checked"/>室主任审核
													</c:when>
													<c:otherwise>
														<input type='checkbox' name='levels' value='${oc.ORG_ID}-<%=Constant.Level_Manager_01 %>'/>室主任审核
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${oc.isLevelTwo == true}">
														<input type="checkbox" name='levels' value='${oc.ORG_ID}-<%=Constant.Level_Manager_02 %>' checked="checked"/>处长审核
													</c:when>
													<c:otherwise>
														<input type='checkbox' name='levels' value='${oc.ORG_ID}-<%=Constant.Level_Manager_02 %>'/>处长审核
													</c:otherwise>
												</c:choose>
												<c:if test="${oc.ORG_ID == 2010010100070674}">
													<c:choose>
														<c:when test="${oc.isLevelThree == true}">
															<input type="checkbox" name='levels' value='${oc.ORG_ID}-<%=Constant.Level_Manager_03 %>' checked="checked"/>公司副总审核
														</c:when>
														<c:otherwise>
															<input type='checkbox' name='levels' value='${oc.ORG_ID}-<%=Constant.Level_Manager_03 %>'/>公司副总审核
														</c:otherwise>
													</c:choose>
												</c:if>
											</td>
									</c:when>
									<c:otherwise>
										<td>
												<c:choose>
													<c:when test="${oc.isLevelOne == true}">
														<input type="checkbox" name='levels' value='${oc.ORG_ID}-<%=Constant.Level_Manager_01 %>' checked="checked" disabled="disabled"/>室主任审核
													</c:when>
													<c:otherwise>
														<input type='checkbox' name='levels' value='${oc.ORG_ID}-<%=Constant.Level_Manager_01 %>' disabled="disabled"/>室主任审核
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${oc.isLevelTwo == true}">
														<input type="checkbox" name='levels' value='${oc.ORG_ID}-<%=Constant.Level_Manager_02 %>' checked="checked" disabled="disabled"/>处长审核
													</c:when>
													<c:otherwise>
														<input type='checkbox' name='levels' value='${oc.ORG_ID}-<%=Constant.Level_Manager_02 %>' disabled="disabled"/>处长审核
													</c:otherwise>
												</c:choose>
												<c:if test="${oc.ORG_ID == 2010010100070674}">
													<c:choose>
														<c:when test="${oc.isLevelThree == true}">
															<input type="checkbox" name='levels' value='${oc.ORG_ID}-<%=Constant.Level_Manager_03 %>' checked="checked" disabled="disabled"/>公司副总审核
														</c:when>
														<c:otherwise>
															<input type='checkbox' name='levels' value='${oc.ORG_ID}-<%=Constant.Level_Manager_03 %>' disabled="disabled"/>公司副总审核
														</c:otherwise>
													</c:choose>
												</c:if>
											</td>
									</c:otherwise>
								</c:choose>
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
		var oldOrgs;
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
		
		var orgLevels = new Array(); 
		function addLevelIds(){
			var cnt = 0;
			var chk=document.getElementsByName("levels");
			var l = chk.length;
			orgLevels.splice(0,orgLevels.length);
			for(var i=0;i<l;i++){
				if(chk[i].checked){
			       cnt++;
			       orgLevels.push(chk[i].value);
				}
			 }
		}
		
		function checkForm(){
			MyConfirm("是否确认修改？",editsubmit,"");	
		}

		function editsubmit(){
			addOrgIds();
			addLevelIds();
			makeNomalFormCall('<%=contextPath%>/customerRelationships/baseSetting/OrgCustomUserLevelRelation/orgCustomUserLevelRelationUpdateSubmit.json?orgids='+pIds+'&orglevels='+orgLevels,editBack,'fm','');
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
			window.location.href = "<%=contextPath%>/customerRelationships/baseSetting/OrgCustomUserLevelRelation/orgCustomUserLevelRelationInit.do";
		}
		
		function clearAllCheckBox(orgid){
			
			if(oldOrgs != orgid){
				var chk=document.getElementsByName("levels");
				var l = chk.length;
				for(var i=0;i<l;i++){
					if(chk[i].checked){
				      chk[i].checked = false;
					}
				}
				oldOrgs = orgid;
			}
		}

	</script>
</body>
</html>
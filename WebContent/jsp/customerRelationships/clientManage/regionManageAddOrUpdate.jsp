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
<title>大区新增修改</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}

</script>
</head>
<body>
	<c:choose>
		<c:when test="${isUpdate != true }">
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 客户关系 &gt;大区管理新增</div>
		</c:when>
		<c:otherwise>
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 客户关系 &gt;大区管理修改</div>
		</c:otherwise>
	</c:choose>
	
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />大区管理信息</th>
			
			<tr>
				<td align="right" nowrap="true">大区名称：</td>
				<td align="left" nowrap="true">
				<c:choose>
					<c:when test="${isUpdate != true  }">
						<select id="orgId" name="orgId" class="short_sel">
							<option value=''>-请选择-</option>
							<c:forEach var="org" items="${orgList}">
								<c:choose>
									<c:when test="${org.orgId == orgId}">
										<option value="${org.orgId}" title="${org.orgName}" selected="selected">${org.orgName}</option> 
									</c:when>
									<c:otherwise>
										<option value="${org.orgId}" title="${org.orgName}">${org.orgName}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>    
					</c:when>
					<c:otherwise>
						<input type="hidden" id="orgId" name="orgId" value="${orgId}"/>
						<select class="short_sel" disabled="disabled">
							<option value=''>-请选择-</option>
							<c:forEach var="org" items="${orgList}">
								<c:choose>
									<c:when test="${org.orgId == orgId}">
										<option value="${org.orgId}" title="${org.orgName}" selected="selected">${org.orgName}</option> 
									</c:when>
									<c:otherwise>
										<option value="${org.orgId}" title="${org.orgName}">${org.orgName}</option>
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
				<td class="table_query_2Col_label_6Letter">所包含省份：</td>
				<td align="left">
				<c:choose>
					<c:when test="${isUpdate != true }">
						<c:forEach var="province" items="${provinceList}">
							<input type='checkbox' name='provinceIds' value='${province.REGION_ID}' />${province.REGION_NAME}
						</c:forEach>
					</c:when>
					<c:otherwise>
						<c:forEach var="province" items="${provinceList}">
								<c:choose>
									<c:when test="${province.isCheck == true}">
										<input type='checkbox' name='provinceIds' value='${province.REGION_ID}' checked="checked"/>${province.REGION_NAME}
									</c:when>
									<c:otherwise>
										<input type='checkbox' name='provinceIds' value='${province.REGION_ID}' />${province.REGION_NAME}
									</c:otherwise>
								</c:choose>
						</c:forEach>
					</c:otherwise>
				</c:choose>
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
		function addProvinceIds(){
			var cnt = 0;
			var chk=document.getElementsByName("provinceIds");
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
			if(${isUpdate != true }){
				addsubmit();
			}else{
				MyConfirm("是否确认修改？",editsubmit,"");
			}		
		}
		function addsubmit(){
			if(document.getElementById("orgId").value == ""){
				MyAlert("请选择大区名称!");
				return;
			}else{
				addProvinceIds();
				makeNomalFormCall('<%=contextPath%>/customerRelationships/clientManage/RegionManage/addOrUpdateRegionManageSubmit.json?regionIds='+pIds,addBack,'fm','');
			}
		}
		function editsubmit(){
			addProvinceIds()
			makeNomalFormCall('<%=contextPath%>/customerRelationships/clientManage/RegionManage/addOrUpdateRegionManageSubmit.json?regionIds='+pIds,editBack,'fm','');
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
			window.location.href = "<%=contextPath%>/customerRelationships/clientManage/RegionManage/regionManageInit.do";
		}

	</script>
</body>
</html>
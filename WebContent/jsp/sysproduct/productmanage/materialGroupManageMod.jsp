<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/jstl/cout" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<title>物料组维护</title>
	<script type="text/javascript">
		function doInit(){

		}
		
		function confirmAdd(){
			if(submitForm('fm')){
				MyConfirm("是否确认保存?",addSave);
			}
		}
		
		function addSave(){
			document.getElementById("id1").disabled = true;
		    document.getElementById("id2").disabled = true;
			makeNomalFormCall('<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/materialGroupManageMod.json',showResult,'fm');
		}
		
		function showResult(json){
			if(json.returnValue == '1'){
				var groupCodeArg = $('groupCodeArg').value;
				var groupNameArg = encodeURI(encodeURI($('groupNameArg').value));
				var statusArg = $('statusArg').value;
				var parentGroupCodeArg = $('parentGroupCodeArg').value;
				var curPage = $('curPage').value;
				var forcast_flag = $('forcast_flag').value;
				window.location.href='<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/materialGroupManageQueryPre.do?groupCodeArg='+groupCodeArg+'&groupNameArg='+groupNameArg+'&statusArg='+statusArg+'&parentGroupCodeArg='+parentGroupCodeArg+'&curPage='+curPage+'&forcast_flag='+forcast_flag;
			
				//window.location.href = '<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/materialGroupManageQueryPre.do';
			}else{
				document.getElementById('id1').disabled = '';
			    document.getElementById('id2').disabled = '';
				MyAlert("修改失败！请联系系统管理员！");
			}
		}
		
		function clrTxt(txtId){ 
	    	document.getElementById(txtId).value = "";
	    	getModelGroup('clear');
	    }

		function goBackToSearchByArg(){
				var groupCodeArg = $('groupCodeArg').value;
				var groupNameArg = encodeURI(encodeURI($('groupNameArg').value));
				var statusArg = $('statusArg').value;
				var parentGroupCodeArg = $('parentGroupCodeArg').value;
				var curPage = $('curPage').value;
				var forcast_flag = $('forcast_flag').value;
				//MyAlert(forcast_flag);
				window.location.href='<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/materialGroupManageQueryPre.do?groupCodeArg='+groupCodeArg+'&groupNameArg='+groupNameArg+'&statusArg='+statusArg+'&parentGroupCodeArg='+parentGroupCodeArg+'&curPage='+curPage+'&forcast_flag='+forcast_flag;
		}
	</script>
</head>
<body onload="showOrHiddenColorTable();">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 物料组维护
		</div>
		<form method="POST" name="fm" id="fm">
			<input type=hidden name='groupCodeArg' id='groupCodeArg' value='${groupCode }' /> 
			<input type=hidden name='groupNameArg' id='groupNameArg' value='${groupName }' />
			<input type=hidden name='statusArg' id='statusArg' value='${status }' />
			<input type=hidden name='parentGroupCodeArg' id='parentGroupCodeArg' value='${parentGroupCode }' /> 
			<input type=hidden name='curPage' id='curPage' value='${page }' /> 
			<input type=hidden id="groupLevel" value="${po.groupLevel}"> 
			<input type=hidden name='forcast_flag' id='forcast_flag' value='${forcast_flag }' />
			<div class="form-panel">
				<h2>物料组维护</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">物料组代码：</td>
							<td>
								<input name="groupCode" datatype="0,is_null,50" id="groupCode" type="text" class="middle_txt" value="${po.groupCode}" />
							</td>
							<td class="right">物料组名称：</td>
							<td>
								<input name="groupName" datatype="0,is_null,300" id="groupCode" type="text" class="middle_txt" value="${po.groupName}" />
							</td>
						</tr>
						<tr>
							<td class="right">上级物料组：</td>
							<td>
								<input type="text" class="middle_txt" name="parentGroupCode" id="parentGroupCode" readonly="readonly" value="${parent.groupCode}" onpropertychange="getModelGroup(this.value);" onclick="showMaterialGroup('parentGroupCode','','false','','true')"/> 
								<input class="normal_btn" type="button" value="清空" onclick="clrTxt('parentGroupCode');" />
							</td>
							<td class="right">物料状态：</td>
							<td>
								<script type="text/javascript">
										genSelBoxExp("status",<%=Constant.STATUS%>,"${po.status}",false,"","","false",'');
								</script>
							</td>
						</tr>
						<tr>
							<td class="right">是否在产：</td>
							<td colspan="3">
								<c:if test="${po.forcastFlag==1}">
									<script type="text/javascript">
										genSelBoxExp("ifType",<%=Constant.IF_TYPE%>,"<%=Constant.IF_TYPE_YES%>",false,"","","false",'');
									</script>
								</c:if> 
								<c:if test="${po.forcastFlag==0}">
									<script type="text/javascript">
										genSelBoxExp("ifType",<%=Constant.IF_TYPE%>,"<%=Constant.IF_TYPE_NO%>",false,"","","false",'');
									</script>
								</c:if>
							</td>
						</tr>
						<!-- <tr id="trObj" style="display:${po.groupLevel == 3 ? '': 'none'}">
							<td class="right">售后车型组：</td>
							<td>
								<select class="u-select" name="modelGroup1" id="modelGroup1">
								<c:forEach items="${groups1}" var="po">
									<option value="${po.WRGROUP_ID}">${po.WRGROUP_NAME}</option>
								</c:forEach>
								</select>&nbsp;<font style="color: red">*</font>
							</td>
							<td class="right">销售车型组：</td>
							<td>
								<select class="u-select" name="modelGroup2" id="modelGroup2">
									<option value="">－请选择－</option>
									<c:forEach items="${groups2}" var="po">
										<option value="${po.WRGROUP_ID}">${po.WRGROUP_NAME}</option>
									</c:forEach>
								</select>&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="4">
								<table id="colorTable" class="table_list" width="10%" align="center">
									<thead>
										<tr>
											<th colSpan=6 align=left>
												<img src="<%=request.getContextPath()%>/img/subNav.gif"> <strong>颜色信息</strong>
												<input class="cssbutton u-button" onclick="addColor();" value="新增" type="button" name="add222" />
											</th>
										</tr>
									</thead>
									<tbody  id="tbody1">
										<tr class="table_list_th">
											<th nowrap="nowrap">颜色编码</th>
											<th nowrap="nowrap">颜色名称</th>
											<th nowrap="nowrap">操作</th>
										</tr>
										<c:forEach items="${colorList}" var="list">
											<tr class="table_list_row2">
												<td>
													<input type="text" name="colorCode" value='${list.COLOR_CODE}'>&nbsp;<font style="color: red">*</font>
												</td>
												<td>
													<input type="text" name="colorName" value='${list.COLOR_NAME}'>&nbsp;<font style="color: red">*</font>
												</td>
												<td>
													<a href="javascript:void(0)" onclick="deleteColorRow(this);">删除</a>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</td>
						</tr> -->
						<tr>
							<td colspan="4" class="center">
								<input type="hidden" name="groupId" value="${po.groupId}"> 
								<input id="id1" name="button2" type="button" class="u-button u-query" onclick="confirmAdd();" value="保存" />
								<input id="id2" name="button" type="button" class="u-button u-cancel" onclick="goBackToSearchByArg();" value="返回" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			
		</form>
	</div>
</body>
</html>

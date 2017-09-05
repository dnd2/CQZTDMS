<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<title>物料组维护</title>
	<script type="text/javascript">
		function goBackToSearchByArg(){
			var groupCodeArg = $('groupCodeArg').value;
			var groupNameArg = encodeURI(encodeURI($('groupNameArg').value));
			var statusArg = $('statusArg').value;
			var parentGroupCodeArg = $('parentGroupCodeArg').value;
			var curPage = $('curPage').value;
			var forcast_flag = $('forcast_flag').value;
			window.location.href='<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/materialGroupManageQueryPre.do?groupCodeArg='+groupCodeArg+'&groupNameArg='+groupNameArg+'&statusArg='+statusArg+'&parentGroupCodeArg='+parentGroupCodeArg+'&curPage='+curPage+'&forcast_flag='+forcast_flag;
		}
		function confirmAdd(){
			if(submitForm('fm')){
				MyConfirm("是否确认保存?",addSave);
			}
		}
		
		function addSave(){
			document.getElementById("id1").disabled = true;
		    document.getElementById("id2").disabled = true;
			makeNomalFormCall('<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/materialGroupManageAdd.json',showResult,'fm');
		}
		
		function showResult(json){
			if(json.returnValue == '1'){
				var groupCodeArg = $('groupCodeArg').value;
				var groupNameArg = encodeURI(encodeURI($('groupNameArg').value));
				var statusArg = $('statusArg').value;
				var parentGroupCodeArg = $('parentGroupCodeArg').value;
				var forcast_flag = $('forcast_flag').value;
				var curPage = $('curPage').value;
				window.location.href='<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/materialGroupManageQueryPre.do?groupCodeArg='+groupCodeArg+'&groupNameArg='+groupNameArg+'&statusArg='+statusArg+'&parentGroupCodeArg='+parentGroupCodeArg+'&curPage='+curPage+'&forcast_flag='+forcast_flag;
			}else{
				document.getElementById("id1").disabled = '';
			    document.getElementById("id2").disabled = '';
				MyAlert("新增失败！请联系系统管理员！");
			}
		}
		
		function clrTxt(txtId){
	    	document.getElementById(txtId).value = "";
	    	getModelGroup('clear');
	    }
	    
	    function getModelGroup(arg){	
	    	if (event.propertyName == "value"){
	    		var url = "<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/getModelGroup.json";
				makeCall(url,showModelGroup,{groupCode:arg}); 
	    	}	
		}
		
		function showModelGroup(json){
			var trObj = document.getElementById("trObj");
			if(json.returnValue == '1'){
				trObj.style.display = "inline";
			}
			else{
				trObj.style.display = "none";
			}
		}
		
	</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 物料组维护</div>
		<form method="POST" name="fm" id="fm">
			<input type=hidden name='groupCodeArg' id='groupCodeArg' value='${groupCode }'/>
			<input type=hidden name='groupNameArg' id='groupNameArg' value='${groupName }'/>
			<input type=hidden name='statusArg' id='statusArg' value='${status }'/>
			<input type=hidden name='parentGroupCodeArg' id='parentGroupCodeArg' value='${parentGroupCode }'/>
			<input type=hidden name='forcast_flag' id='forcast_flag' value='${forcast_flag }'/>
			<input type=hidden name='curPage' id='curPage' value='${page }'/>
			<input type=hidden id="groupLevel" value="${po.groupLevel}">
			<div class="form-panel">
				<h2>物料组维护</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">物料组代码：</td>
							<td>
								<input name="groupCode" datatype="0,is_null,30" id="groupCode" type="text" class="middle_txt" />
							</td>
							<td class="right">物料组名称：</td>
							<td>
								<input name="groupName" datatype="0,is_null,100" id="groupCode" type="text" class="middle_txt" />
							</td>
						</tr>
						<tr>
							<td class="right">上级物料组：</td>
							<td>
								<input type="text" class="middle_txt" name="parentGroupCode" id="parentGroupCode" readonly="readonly" onclick="showMaterialGroup('parentGroupCode','','false','','true')" />
								<input class="u-button" type="button" value="清空" onclick="clrTxt('parentGroupCode');"/>
							</td>
							<td class="right">物料状态：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("status",<%=Constant.STATUS%>,"",false,"","","false",'');
								</script>
							</td>
						</tr>
						<tr>
							<td class="right">是否在产：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("ifType",<%=Constant.IF_TYPE%>,"",false,"","","false",'');
								</script>
							</td>
						</tr>
						<!-- <tr id="trObj" style="display:none1">
							<td class="right">售后车型组：</td>
							<td>
								<select name="modelGroup1" id="modelGroup1">
									<c:forEach items="${groups1}" var="po">
										<option value="${po.WRGROUP_ID}">${po.WRGROUP_NAME}</option>
									</c:forEach>
								</select><font color="red">*</font>
							</td>
							<td class="right">销售车型组：</td>
							<td>
								<select name="modelGroup2">
									<option value="">－请选择－</option>
									<c:forEach items="${groups2}" var="po">
										<option value="${po.WRGROUP_ID}">${po.WRGROUP_NAME}</option>
									</c:forEach>
								</select>
							</td>
						</tr> -->
						<!-- <tr>
							<td colspan="4">
								<table id="colorTable" class="table_list" style="margin-top: 3px;display:none" width="10%" align="center" >
									<thead>
										<tr>
											<th colspan="3">
												<img  src="<%=request.getContextPath()%>/img/subNav.gif"> 
												<strong>颜色信息</strong> 
												<input class="cssbutton" onclick="addColor();" value="新增" type="button" name="add222" /> 
											</th>
										</tr>
										<tr class="table_list_th">
											<th nowrap="nowrap">颜色编码</th>
											<th nowrap="nowrap">颜色名称</th>
											<th nowrap="nowrap">操作</th>
										</tr>
									</thead>
									<tbody id="tbody1"></tbody> 
								</table>
							</td>
						</tr> -->
						<tr>
							<td colspan="4" class="center">
								<input id="id1" name="button2" type="button" class="u-button" onclick="confirmAdd();" value="保存" /> 
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

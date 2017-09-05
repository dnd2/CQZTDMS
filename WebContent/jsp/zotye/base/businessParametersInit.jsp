<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>参数维护</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
<!--
function addSubmit() {
	var selectParametarType = document.getElementById("selectParametarType"); 
	var hiddenTypeIsAdd = document.getElementById("hiddenTypeIsAdd"); 
	
	if(!selectParametarType.value) {
		MyAlert("请选择参数类型！") ;
		return false ;
	}
	
	if(hiddenTypeIsAdd.value == <%=Constant.IF_TYPE_YES %>) {
		
	} else {
		MyAlert("该参数类型下不允许新增参数！") ;
		return false ;
	}
	
	var url = "<%=contextPath%>/zotye/base/BusinessParametersAction/businessParametersAddInit.do";
	fm.action = url ;
	fm.submit() ;
}

function updateIt(value) {
	var url = "<%=contextPath%>/zotye/base/BusinessParametersAction/firstMaintenaceUpdateInit.do?id=" + value;
	fm.action = url ;
	fm.submit() ;
}

function chgSystemModule() {
	var url = "<%=contextPath%>/zotye/base/BusinessParametersAction/systemBusinessQuery.json";
	makeNomalFormCall(url, getSystemBusiness, "fm") ;
}

function getSystemBusiness(json) {
	var list = json.list ;
	var selectSystemModule = document.getElementById("selectSystemModule"); 
	var selectSystemBusiness = document.getElementById("selectSystemBusiness"); 
	var selectParametarType = document.getElementById("selectParametarType"); 
	selectSystemBusiness.innerHTML = "";
	selectSystemBusiness.options.add(new Option("-请选择-", ""));
	selectParametarType.innerHTML = "";
	selectParametarType.options.add(new Option("-请选择-", ""));
	if(selectSystemModule.value != ""){
		if(list) {
			for(var i=0; i< list.length; i++) {
				selectSystemBusiness.options.add(new Option(list[i].NAME, list[i].CODE));
			}
		}
	}
}

function chgSystemBusiness() {
	var url = "<%=contextPath%>/zotye/base/BusinessParametersAction/businessParametarsTypeQuery.json";
	makeNomalFormCall(url, getBusinessParametars, "fm") ;
}

function getBusinessParametars(json) {
	var list = json.list ;
	var selectSystemBusiness = document.getElementById("selectSystemBusiness"); 
	var selectParametarType = document.getElementById("selectParametarType"); 
	var hiddenTypeIsAdds = document.getElementById("hiddenTypeIsAdds"); 
	var typeAdd = "" ;
	
	hiddenTypeIsAdds.value = "" ;
	selectParametarType.innerHTML = "";
	selectParametarType.options.add(new Option("-请选择-", ""));
	if(selectSystemBusiness.value != ""){
		if(list) {
			for(var i=0; i< list.length; i++) {
				selectParametarType.options.add(new Option(list[i].NAME, list[i].CODE));
				
				if(typeAdd == "") {
					typeAdd = list[i].IS_PARA_ADD ;
				} else {
					typeAdd += "," + list[i].IS_PARA_ADD ;
				}
			}
			
			hiddenTypeIsAdds.value = typeAdd ;
		}
	}
}

function chgBusinessParametars() {
	var selectParametarType = document.getElementById("selectParametarType"); 
	var hiddenTypeIsAdds = document.getElementById("hiddenTypeIsAdds"); 
	var hiddenTypeIsAdd = document.getElementById("hiddenTypeIsAdd"); 
	
	var typeAdd = hiddenTypeIsAdds.value.split(",") ;
	document.getElementById("hiddenParametarType").value = selectParametarType.options[selectParametarType.selectedIndex].text ;
	
	if(selectParametarType.selectedIndex == 0) {
		hiddenTypeIsAdd.value = "" ;
	} else {
		hiddenTypeIsAdd.value = typeAdd[selectParametarType.selectedIndex - 1] ;
	}
}
//-->
</script>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
<div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	<table class="table_query" >
		<tr>
			<td class="right">系统模块：</td>
		    <td>
		    	<select class="u-select" name="selectSystemModule" id="selectSystemModule" onchange="chgSystemModule() ;">
					<option value="">-请选择-</option>
					<c:forEach items="${list }" var="moduleMap">
						<option value="${moduleMap.CODE }">${moduleMap.NAME }</option>
					</c:forEach>
				</select>
		    </td>
		    <td class="right">系统业务：</td>
		    <td>
		    	<select class="u-select" name="selectSystemBusiness" id="selectSystemBusiness" onchange="chgSystemBusiness() ;">
					<option value="">-请选择-</option>
				</select>
		    </td>
		    <td class="right">参数类型：</td>
		    <td>
		    	<select class="u-select" name="selectParametarType" id="selectParametarType" onchange="chgBusinessParametars() ;">
					<option value="">-请选择-</option>
				</select>
				<input type="hidden" id="hiddenParametarType" name="hiddenParametarType" value="" />
				<input type="hidden" id="hiddenTypeIsAdds" name="hiddenTypeIsAdds" value="" />
				<input type="hidden" id="hiddenTypeIsAdd" name="hiddenTypeIsAdd" value="" />
		    </td>
		</tr>
		<tr>
			<td class="center" colspan="6">
				<input name="qryBtn" id="queryBtn" type="button" class="u-button" onClick="__extQuery__(1);" value="查询">&nbsp;
				<input name="addBtn" id="addBtn" type="button" class="u-button" onClick="addSubmit() ;" value="新增">
			</td>
		</tr>
	</table>
	</div>
	</div>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
<!--
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/zotye/base/BusinessParametersAction/businessParametersQuery.json";
	var title = null;
	var columns = [
				{header: "操作",sortable: false, dataIndex: 'ID', align:'center',renderer:myLink},
				{header: "系统模块",dataIndex: 'MODULE_NAME',align:'center'},
				{header: "系统业务",dataIndex: 'BUSINESS_NAME',align:'center'},
				{header: "参数类型",dataIndex: 'TYPE_NAME',align:'center'},
				{header: "参数编码",dataIndex: 'CODE',align:'center'},
				{header: "参数名称",dataIndex: 'NAME',align:'center'},
				{header: "参数值",dataIndex: 'VALUE',align:'center'},
				{header: "参数状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
				{header: "备注",dataIndex: 'REMARK',align:'center'}
		      ];
	
	function myLink(value,meta,record){
		var typeIsModify = record.data.IS_PARA_MODIFY ;
		if(typeIsModify == "<%=Constant.IF_TYPE_YES %>")
			return String.format("<a href='#' onclick='updateIt(" + value + ")'>[修改]</a>");
		else 
			return "" ;
	}
//-->
</script>
</div>
<!--页面列表 end -->
</body>
</html>
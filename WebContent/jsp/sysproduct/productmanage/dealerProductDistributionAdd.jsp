<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%String contextPath = request.getContextPath();%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
 <!--
 var aGroupDtlId = new Array() ;
 var aGroupDtlName = new Array() ;
 var aGroupDtlCode = new Array() ;
 
 function doInit() {
	 changeType() ;
 }
 
 function startIt() {
	 var sTheId = "${theId}" ;
	 var sTheName = "${theName}" ;
	 var sTheCode = "${theCode}" ;

	 if(sTheId) {
		 aGroupDtlId = sTheId.split(",") ;
		 aGroupDtlName = sTheName.split(",") ;
		 aGroupDtlCode = sTheCode.split(",") ;
	 }
 }
 
 function chkChg(obj) {
	 var iLen = aGroupDtlId.length ;
	 
	 var aDtl = obj.value.split(",") ;
	 var sTheId = aDtl[0];
	 var sTheName = aDtl[1];
	 var sTheCode = aDtl[2];
	 
	 if(obj.checked) {
		 aGroupDtlId.push(sTheId) ;
		 aGroupDtlName.push(sTheName) ;
		 aGroupDtlCode.push(sTheCode) ;
	 } else {
		 for(var i=0; i<iLen; i++) {
			 if(aGroupDtlId[i] == sTheId) {
				 aGroupDtlId.splice(i, 1) ;
				 aGroupDtlName.splice(i, 1) ;
				 aGroupDtlCode.splice(i, 1) ;
			 }
		 }
	 }
 }
 
 function clrTxt(valueId) {
	 document.getElementById(valueId).value = "" ;
 }
 
 function clrArray() {
	 aGroupDtlId = new Array() ;
	 aGroupDtlName = new Array() ;
	 aGroupDtlCode = new Array() ;
 }
 
 function addSub() {
	 if(submitForm('fm')) {
		 var iLen = aGroupDtlId.length ;
		 if(iLen == 0) {
			 MyAlert("你还没有选择需要分组的数据！") ;
			 return false ;
		 }
		 MyConfirm("确认提交？", addSure) ;
	 }
 }
 
 function addSure() {
	 document.getElementById("dtlIds").value = aGroupDtlId.join() ;
	 document.getElementById("dtlNames").value = aGroupDtlName.join() ;
	 document.getElementById("dtlCodes").value = aGroupDtlCode.join() ;

	 var url = "<%=contextPath%>/groups/DivideGroupsAction/packageUpdate.json" ;
	 makeFormCall(url, returnResult, "fm") ;
 }
 
 function returnResult(json) {
	 var sFlag = json.flag ;
	 
	 if(sFlag == "success") {
		 MyAlert("操作成功！") ;
		 history.back() ;
	 } else {
		 MyAlert("操作失败！") ;
	 }
 }
 //-->
 </script>
<title>经销商产品套餐分配维护</title>
</head>
<body> 
<div>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  系统管理 > 产品维护 > 经销商产品套餐分配 > 维护</div>
		<form id="fm" name="fm" method="post">
			<table class="table_query">
				<tr>
					<td align="right">套餐代码：</td>
					<td>${map.PACKAGE_CODE }<input type="hidden" name="regionCode" id="regionCode" value="${map.REGION_CODE }" /></td>
					<td align="right">套餐名称：</td>
					<td>${map.PACKAGE_NAME }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="OpenHtmlWindow('<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/dealerProductPackagedetail.do?ID=${ID }',900,500)">[查看明细]</a></td>
				</tr>
				<tr>
					<td align="right"><label for="groupsStatus">省份：</label></td>
					<td>${map.REGION_NAME }</td>
					<td align="right">状态：</td>
       				<c:if test="${map.STATUS==10011001 }">
       					<td>有效</td>
       				</c:if>
       				<c:if test="${map.STATUS==10011002 }">
       					<td>无效</td>
       				</c:if>
				</tr>
			</table>
			<div>
				<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;分组选择</div>
				<table class="table_query">
				<tr>
					<td align="right"><label for="queryName">名称：</label></td>
					<td>
						<input type="text" class="middle_txt" name="queryName" id="queryName" value="" />&nbsp;
						<input type="button" class="normal_btn" name="queryBtn" id="queryBtn" value="查 询" onclick="theQuery() ;" />
					</td>
				</tr>
				</table>
				<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
				<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			</div>
			<br />
			<table class="table_query" id="dtlTab">
				<tr>
					<td colspan="5" align="center">
						<input type="hidden" name="productId" id="productId" value="${ID }" />
						<input type="hidden" name="dtlIds" id="dtlIds" />
						<input type="hidden" name="dtlNames" id="dtlNames" />
						<input type="hidden" name="dtlCodes" id="dtlCodes" />
						<input type="button" class="normal_btn" name="subIt" id="subIt" value="提 交" onclick="addSub() ;" />&nbsp;
						<input type="button" class="normal_btn" name="retIt" id="retIt" value="返 回" onclick="javascript:history.back() ;"/>
					</td>
				</tr>
			</table>
		</form>
</div>
<script type="text/javascript">
	var myPage;
	var url = null ;
	var title = null;
	var columns = null ;
	
	function changeType() {
		startIt() ;
		theQuery() ;
 	}
	
	function theQuery() {
		url = "<%=contextPath%>/groups/DivideGroupsAction/groupsDtlUpdateQuery111.json" ;
		columns = [
					{id:'check',header: "选择", width:'6%',sortable: false,dataIndex: 'COMPANY_ID',renderer:myCheckBox},
					{header: "名称", dataIndex: 'COMPANY_NAME', align:'center'},
					{header: "编码", dataIndex: 'COMPANY_CODE', align:'center'}
				   ];
			__extQuery__(1) ; 
	}
	
	function myCheckBox(value,metaDate,record){
		var theName = record.data.COMPANY_NAME ;
		var theCode = record.data.COMPANY_CODE ;
		
		var theValue = value + "," + theName + "," + theCode ;
		
		var iLen = aGroupDtlId.length ;
		
		for(var i=0; i<iLen; i++) {
			if(aGroupDtlId[i] == value)
				return String.format("<input type=\"checkbox\" name='theIds' value='" + theValue + "' onclick='chkChg(this);' checked='checked'/>");
		}
		
		return String.format("<input type=\"checkbox\" name='theIds' value='" + theValue + "' onclick='chkChg(this);' />");
	}
</script>    
</body>
</html>
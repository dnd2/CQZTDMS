<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<!--去缓存start  -->
<META HTTP-EQUIV="pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"> 
<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
<!--去缓存end  -->
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
	 
	 aGroupDtlId = sTheId.split(",") ;
	 aGroupDtlName = sTheName.split(",") ;
	 aGroupDtlCode = sTheCode.split(",") ;
 }
 
 function chkChg(obj) {
	 var iLen = aGroupDtlId.length ;
	 
	 var aDtl = obj.value.split(",") ;
	 var sTheId = aDtl[0] ;
	 var sTheName = aDtl[1] ;
	 var sTheCode = aDtl[2] ;
	 
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
		 var oTypeBox = document.getElementById("groupsType") ;
		 
		 if(!oTypeBox.value) {
			 MyAlert("请选择组类型！") ;
			 
			 return false ;
		 }
		 
		 var oStatusBox = document.getElementById("groupsStatus") ;
		 
		 if(!oStatusBox.value) {
			 MyAlert("请选择组状态！") ;
			 
			 return false ;
		 }
		 
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
	 
	 var url = "<%=contextPath%>/groups/DivideGroupsAction/groupsUpdate.json" ;
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
<title>修改分组</title>
</head>
<body> 
<div>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 分组管理 &gt; 修改分组</div>
		<form id="fm" name="fm" method="post">
			<table class="table_query">
				<tr>
					<td align="right"><label for="groupsName">组名称：</label></td>
					<td><input type="text" class="middle_txt" name="groupsName" id="groupsName" value="${headMap.GROUP_NAME }" datatype="0,is_null,25" maxlength="25" /></td>
					<td align="right"><label for="groupsType">组类型：</label></td>
					<td>
						<script type="text/javascript">
							genSelBoxExp("groupsType",<%=Constant.DIVIDE_GROUP_TYPE%>,"${headMap.GROUP_TYPE}",true,"short_sel","onchange=\"clrArray() ;changeType() ;\"","false",'') ;
						</script>
					</td>
					<td></td>
				</tr>
				<tr>
					<td align="right"><label for="groupsStatus">组状态：</label></td>
					<td>
						<script type="text/javascript">
							genSelBoxExp("groupsStatus",<%=Constant.STATUS%>,"${headMap.GROUP_STATUS}",true,"short_sel","","false",'') ;
						</script>
					</td>
					<td align="right"></td>
					<td></td>
					<td></td>
				</tr>
			</table>
			<div id="groupsDtl" style="display:none">
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
						<input type="hidden" name="headId" id="headId" value="${headId }" />
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
		var oDiv = document.getElementById("groupsDtl") ;
		/* 按经销商公司分组 */
		if(parseInt(document.getElementById("groupsType").value) == <%=Constant.DIVIDE_GROUP_TYPE_DLR_COMPANY%>) {
			oDiv.style.display = "inline" ;
			
			url = "<%=contextPath%>/groups/DivideGroupsAction/groupsDtlUpdateQuery.json" ;
			columns = [
						{id:'check',header: "选择", width:'6%',sortable: false,dataIndex: 'THE_ID',renderer:myCheckBox}, //<input type='checkbox' name='checkAll' onclick='selectAll(this,\"theIds\");' />
						{header: "名称", dataIndex: 'THE_NAME', align:'center'},
						{header: "编码", dataIndex: 'THE_CODE', align:'center'}
				      ];
			__extQuery__(1) ; 
		}
		/* END */
		else {
			oDiv.style.display = "none" ;
		}
	}
	
	function myCheckBox(value,metaDate,record){
		var theName = record.data.THE_NAME ;
		var theCode = record.data.THE_CODE ;
		
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
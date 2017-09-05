<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.math.BigDecimal"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@ page import=" com.infodms.dms.util.CommonUtils"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>售后经销商地址导入确认</title>
</head>
<body onunload='javascript:destoryPrototype()'>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置 >系统管理>经销商管理>售后经销商维护>地址导入确认</div>	 
<form  name="fm" id="fm">
<table class="table_query" id="subtab">
  <tr class="csstr" align="center"> 
		<th>
			<div align="left">
				<input class="cssbutton" type="button" id="savebtn" name='saveResButton' onclick='isSave();' value='保存' />
				<input class="cssbutton" type='button' name='saveResButton' onclick='history.back();' value='返回' />
			</div>
		</th>	
  	</tr>
</table>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<script type="text/javascript">
var myPage;
//查询路径           
var url = "<%=contextPath%>/sysmng/dealer/ShDealerImport/shImportAddressOperateQuery.json";
var title = null;
var columns = [
			{header: "序号",align:'center',renderer:getIndex},
			{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
			{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
			{header: "联系人",dataIndex: 'LINKMAN',align:'center'},
// 			{header: "性别",dataIndex: 'GENDER',align:'center'},
			{header: "联系人电话",dataIndex: 'TEL',align:'center'},
			{header: "联系人手机",dataIndex: 'MOBILE_PHONE',align:'center'},
			{header: "详细地址",dataIndex: 'ADDR',align:'center'},
			{header: "地址状态",dataIndex: 'STATE',align:'center'},
			{header: "地址类型",dataIndex: 'ADDRESS_TYPE',align:'center'}
	      ];
//初始化    
function doInit(){
	__extQuery__(1);
}
function isSave(){
    if(submitForm('fm')){
	    MyConfirm("是否确认保存信息?",importSave);
    }
 }
function importSave() {
	if(submitForm('fm')){
			fm.action = "<%=contextPath %>/sysmng/dealer/ShDealerImport/importExcelForAddress.do";
			fm.submit();
		}
}
</script>
</body>
</html>

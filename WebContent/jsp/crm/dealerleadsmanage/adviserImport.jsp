<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%>
<head>
<%
String contentPath=request.getContextPath();
%>
<jsp:include page="${contextPath}/common/sales_header.jsp" />
<title>客流导入 </title>
</head>
<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contentPath %>/img/nav.gif" />&nbsp;当前位置>潜客管理>经销商线索管理>顾问导入
	</div>
<form name="fm" id="fm"  method="post"  enctype="multipart/form-data">
<table class="table_query">
	<tr> 
		<td class="table_query_label" colspan="2">
			1、点“<font color="#FF0000">浏览</font>”按钮，找到您所要导入的客流文件</td>
    </tr>
	<tr>
	    <td class="table_query_input" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp; 
	      <input type="file" name="uploadFile" datatype="0,is_null,2000" id="upfile" value="" />
	    </td>
    </tr>
	<tr> 
		<td class="table_query_label" width="30%">2、选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成粘贴。</td>
	    <td class="table_query_label" align="left" width="56%">
	       <input type="button" id="upbtn" class="cssbutton"  name="vdcConfirm" value="确定" onclick="upload()" />
	       <input name="tempImportCus" type="button" class="normal_btn" onclick="window.location.href='<%=contentPath%>/crm/dealerleadsmanage/DlrLeadsManage/downloadCusTemple.do'" value="模板下载" />
	    </td>
	</tr>
</table>
</form>
</div>
<script type="text/javascript">
	function upload(){
		document.getElementById("upbtn").disabled=true;
		fm.action ="<%=contentPath%>/crm/dealerleadsmanage/DlrLeadsManage/adviserCusImportFlow.do";
    	fm.submit();
	}
</script>
</body>
</html>

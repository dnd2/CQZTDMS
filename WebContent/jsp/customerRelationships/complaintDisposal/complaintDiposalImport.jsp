<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
String contentPath=request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<link href="<%=contentPath %>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contentPath %>/style/calendar.css" type="text/css" rel="stylesheet" />
<title>投诉信息导入</title>

<script language="JavaScript">
	
</script>
</head>

<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contentPath %>/img/nav.gif" />&nbsp;当前位置>客户关系管理>客户投诉管理>客户投诉导入(总部)

	</div>
<form name="fm" method="post"  enctype="multipart/form-data">
  
<table class="table_query">
	<tr> 
		<td class="table_query_label" colspan="2">
			点“<font color="#FF0000">浏览</font>”按钮，找到您所要导入的的投诉信息文件</td>
    </tr>
	<tr>
	    <td class="table_query_input" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp; 
	      <input type="file" name="uploadFile" style="width: 250px"  datatype="0,is_null,2000" id="uploadFile" value="" />
	    </td>
    </tr>
	<tr> 
		<td class="table_query_label" width="30%">选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成导入。</td>
	    <td class="table_query_label" align="left" width="56%">
	       <input type="button" class="cssbutton"  name="queryBtn" value="确定" onclick="upload()" />
	       <input type="button" class="long_btn"  name="downloadBtn" value="导入模板下载" onclick="downloadFile()" />
	    </td>
	    <td><input type="hidden" name="singleSheet" value="1"/></td>
	</tr>
</table>
</form>
</div>
<script type="text/javascript">

	function upload(){
		var fn = document.getElementById("uploadFile").value;
		if (!fn) {
			MyAlert("请选择要导入的文件");
			return;
		}
		if(!submitForm('fm')){
			return false;
		}
		fm.action = "<%=contentPath %>/customerRelationships/export/ComplaintExpImp/complaintImp.do";
	    fm.submit();
	}
	function downLoadTemp(){
		location.href="<%=contentPath %>/sales/planmanage/YearTarget/YearTargetImport/tmp.do";
	}
	
	// 导入模板下载
	function downloadFile(){
	    fm.action = "<%=contentPath %>/customerRelationships/export/ComplaintExpImp/complaintImpDown.do";
	    fm.submit();
	}
</script>
</body>
</html>

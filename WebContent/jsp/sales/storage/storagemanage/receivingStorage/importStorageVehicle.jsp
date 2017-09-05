<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>三方车辆入库管理</title>
</head>
 
<body>
	<div class="wbox">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>仓库管理>三方车辆入库管理</div>
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<div class="form-panel">
<h2>三方车辆入库管理</h2>
<div class="form-body">	  
<table class="table_query">
	<tr> 
		<td>
			1、点“<font color="#FF0000">浏览</font>”按钮，找到您所要导入的车辆信息文件：&nbsp;&nbsp;&nbsp;</td>
    </tr>
	<tr>
	    <td>&nbsp;&nbsp;&nbsp;&nbsp; 
	      <input type="file" name="uploadFile" datatype="0,is_null,2000" id="upfile" value="" />
	     	
	    </td>
    </tr>
   <!-- 
   <tr>
	    <td>
	     	2、入库类型：<font color="#FF0000">[借车、大修、其他、在途换车]</font><br />
	     	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;出库类型：<font color="#FF0000">[还车、质损、其他、在途换车]</font>
	    </td>
    </tr>
    --> 
	<tr> 
		<td width="400px">2、选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成粘贴。</td>
	       	    <td class="table_query_label" align="left" width="56%">
	       <input type="button" id="upbtn" class="normal_btn"  name="vdcConfirm" value="确定" onclick="upload()" />
	       <input type="button" class="normal_btn"  name="downloadBtn" value="导入模板下载" onclick="downloadFile()" />
		</td>
	</tr>
</table>
</div>
</div>
</form>
</div>
<script type="text/javascript"> 
	
	function upload(){
		/*
		
		var lockError=document.getElementById("lockError").value;
		if(lockError!=null&&lockError!=''){
			document.getElementById("upbtn").disabled=true;//日志表被锁定时不允许导入
			MyAlert(lockError);
			return false;
		}
		
		*/
		var importFileName = document.getElementById("upfile").value;
 
		var index = importFileName.lastIndexOf(".");
		var suffix = importFileName.substr(index+1,importFileName.length).toLowerCase();
		if(suffix != "xls"){
		MyAlert("Excel格式不正确只能是xls后缀结束");
			return false;
		}
		document.getElementById("upbtn").disabled=true;
		document.getElementById('fm').action = "<%=request.getContextPath()%>/sales/storage/storagemanage/RecievingStorageImport/storageExcelOperate.do";
    	document.getElementById("fm").submit();
	}
	
	function downloadFile(){
		
	    fm.action ="<%=request.getContextPath()%>/sales/storage/storagemanage/RecievingStorageImport/downloadTemplateStorage.do";
	    fm.submit();
	}
	
	
</script>
</body>
</html>


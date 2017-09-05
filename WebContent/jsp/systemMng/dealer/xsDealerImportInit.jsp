<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<%
String contentPath=request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>销售经销商信息导入</title>
</head>

<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contentPath %>/img/nav.gif" />&nbsp;当前位置 >系统管理>经销商管理>销售经销商维护>导入
	</div>
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
  
<table class="table_query">
	<tr>
	    <td class="table_query_input" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp; 
	    	备注：<font color="#FF0000">请按模板填写经销商导入信息</font> 
	    </td>
    </tr>
	<tr> 
	       <td class="table_query_label" align="left" width="56%">
	       &nbsp;&nbsp;&nbsp;&nbsp; <input type="file" name="uploadFile" datatype="0,is_null,2000" id="upfile" value="" />
	       <input type="button" id="upbtn" class="cssbutton"  name="vdcConfirm" value="确定" onclick="upload()" />
	       <input type="button" class="cssbutton"  name="goBackBtn" value="返回" onclick="goBack()" />
	       <input type="button" class="long_btn"  name="downloadBtn" value="导入模板下载" onclick="downloadFile()" />
	    </td>
	</tr>
</table>
</form>
</div>
<script type="text/javascript">
	function upload(){
		if(!submitForm('fm')){
			return false;
		}
		var importFileName = $("upfile").value;
		var index = importFileName.lastIndexOf(".");
		var suffix = importFileName.substr(index+1,importFileName.length).toLowerCase();
		if(suffix != "xls"){
		MyAlert("Excel格式不正确只能是xls后缀结束");
			return false;
		}
		document.getElementById("upbtn").disabled=true;
		fm.action = "<%=contentPath %>/sysmng/dealer/XsDealerImport/xsImportOperate.do";
		fm.submit();
	}
	
	function downloadFile(){
	    fm.action = "<%=contentPath %>/sysmng/dealer/XsDealerImport/downloadTempleXs.do";
	    fm.submit();
	}
	function goBack(){
		fm.action = "<%=contentPath %>/sysmng/dealer/DealerInfo/queryDealerInfoInit.do";
	    fm.submit();
	}
</script>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<head>
<jsp:include page="${path}/common/jsp_head_new.jsp" />
</head>
<body>
<form id="fm" method="post" enctype="multipart/form-data">
<table class="table_add">
	<tr>
		<td class="table_add_2Col_label_4Letter" nowrap="nowrap">批量导入：</td>
		<td colspan="3" nowrap="nowrap" class="table_add_2Col_input">
			<input type="file" name="uploadFile" style="width: 250px"  datatype="0,is_null,2000" id="importFile" value="" />
		</td>
		<font color="red"> 支持word和txt文件 </font>
	</tr>
	<tr class="table_query_last">
		<td nowrap="nowrap" colspan="6">
		<input class="normal_btn" type="button" id="addBtn" value="确 定" onclick="upload()" />
		<input class="normal_btn" type="reset" id="clearBtn"  value="清 空"/>
		</td>
	</tr>
</table>
</form>
<script type="text/javascript">
	function upload(){
		//disableBtn($("addBtn"));
		//makeNomalFormCall('<%=path%>/common/FileUploadAction/fileUpload.json',showResult,'fm');
		if(!submitForm('fm')){
			return false;
		}
		$('fm').action = "<%=path %>/common/FileUploadAction/fileUpload.do";
    	$("fm").submit();
	}
	function showResult(json){
		if(json.ACTION_RESULT == '1'){
			window.location.href = '<%=request.getContextPath()%>/fileUpload/fileUploadFrame.jsp';
		}
	}
</script>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String id = request.getParameter("id");
%>
<html>
<head>
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<title>Insert title here</title>
</head>
<body>
<form id="fm" method="post" enctype="multipart/form-data">
<table width='100%'>
	<tr>
		<td  nowrap="nowrap"><a href="" target="_blank"></a>
			<iframe id="upIframe" name="upIframe" src="<%=path%>/commonUploadIF.jsp" width='100%' height=80  scrolling="no" style="overflow-x:hidden;overflow-y:visible;" frameborder="0"></iframe>
		</td>
	</tr>
</table>
<table class="table_list" style="border-bottom:1px solid #DAE0EE" id="uptab" >
	<tr>
		<th>附件名称</th>
		<th>
			操作
		</th>
	</tr>
</table>
<table class="table_query" >	
	<tr>
		<td nowrap="nowrap" class="center" >
		<input class="normal_btn" type="button" id="addBtn" value="确认" onclick='setBackValue()'  />	
		<input class="normal_btn" type="reset" id="clearBtn" onclick='cancel()'  value="取消"/>
		</td>
	</tr>
</table>
</form>
<script type="text/javascript">
	function upload(){
		if(!submitForm('fm')){
			return false;
		}
		fm.action = "<%=path %>/common/FileUploadAction/fileUpload.do";
    	fm.submit();
	}
function delUploadFile(obj){
  		var idx = obj.parentElement.parentElement.rowIndex;
		var tbl = document.getElementById('uptab');
		tbl.deleteRow(idx);
}

function setBackValue(){
   var crow;
   var id = "<%=id%>";
    var rows = uptab.rows;
    if(rows.length==1){
        MyAlert("您还没有上传附件");
    	return;
    }
    
  try{
	if (id == "null" || id == "" || id == "undefined") {
   	 for(var i=1;i<rows.length;i++){
    	 crow=rows[i];
         if (__parent()) {
        	 __parent().addUploadRow(crow);
        	 } else {
        		 __parent().addUploadRow(crow);
        	 }
    }
	} else {
		for(var i=1;i<rows.length;i++){
	    	 crow=rows[i];
	         if (__parent()) {
	        	 __parent().addUploadRowWithId(crow,id);
	        	 } else {
	        		 __parent().addUploadRowWithId(crow,id);
	        	 }
	    }
	}
    }catch(E){MyAlert(E.message);}
    _hide();
}

function cancel(){
	_hide();
}
</script>

</body>
</html>
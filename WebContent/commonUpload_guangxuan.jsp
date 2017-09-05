<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String id = request.getParameter("id");
	String index = request.getParameter("index");
	String index2 = request.getParameter("index2");
	String index3 = request.getParameter("index3");
	String sdetial_id = request.getParameter("sdetial_id");
	String type = request.getParameter("type");
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
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
			<iframe id="upIframe" name="upIframe" src="" width='100%' height=80  scrolling="no" style="overflow-x:hidden;overflow-y:visible;" frameborder="0"></iframe>
		</td>
	</tr>
</table>
<table class="table_info" style="border-bottom:1px solid #DAE0EE" id="uptab" >
	<tr>
		<th>附件名称</th>
		<th>
			操作
		</th>
	</tr>
</table>
<table class="table_add" >	
	<tr class="table_query_last">
		<td nowrap="nowrap" align="center" >
		<input class="normal_btn" type="button" id="addBtn" value="确认" onclick='setBackValue()'  />
		<input class="normal_btn" type="reset" id="clearBtn" onclick='cancel()'  value="取消"/>
		</td>
	</tr>
</table>
</form>
<script type="text/javascript">
function doInit(){
	var index = <%=index%>;
	document.getElementById("upIframe").src ="<%=path%>/commonUploadIF.jsp?index="+index;
}

	function upload(){
		if(!submitForm('fm')){
			return false;
		}
		$('fm').action = "<%=path %>/common/FileUploadAction/fileUpload.do";
    	$("fm").submit();
	}
function delUploadFile(obj){
  		var idx = obj.parentElement.parentElement.rowIndex;
		var tbl = document.getElementById('uptab');
		tbl.deleteRow(idx);
}

function setBackValue(){
   var crow;
   var id = "<%=id%>";
   var index = "<%=index%>";
   var index2 = "<%=index2%>";
   var index3 = "<%=index3%>";
   var sdetial_id = "<%=sdetial_id%>";
   var type = "<%=type%>";
    var rows = uptab.rows;
    if(rows.length==1){
        MyAlert("您还没有上传附件");
    	return;
    }
  try{
	if (id == "null" || id == "" || id == "undefined") {
   	 for(var i=1;i<rows.length;i++){
    	 crow=rows[i];
         if (parent.$('inIframe')) {
	        	 if(index==1){
	        		 parentContainer.addUploadRow1(crow,index2,index3,sdetial_id,type);
	        	 }else if(index==2){
	        		 parentContainer.addUploadRow2(crow,index2,index3,sdetial_id,type);
	        	 }else if(index==3){
	        		 parentContainer.addUploadRow3(crow,index2,index3,sdetial_id,type);
	        	 }else if(index==4){
	        		 parentContainer.addUploadRow4(crow,index2,index3,sdetial_id,type);
	        	 }else if(index==5){
	        		 parentContainer.addUploadRow5(crow,index2,index3,sdetial_id,type);
	        	 }else if(index==6){
	        		 parentContainer.addUploadRow6(crow,index2,index3,sdetial_id,type);
	        	 }else if(index==7){
	        		 parentContainer.addUploadRow7(crow,index2,index3,sdetial_id,type);
	        	 }else if(index==8){
	        		 parentContainer.addUploadRow8(crow,index2,index3,sdetial_id,type);
	        	 }
        	 } else {
        		 if(index==1){
        			 parent.addUploadRow1(crow,index2,index3,sdetial_id,type);
	        	 }else if(index==2){
	        		 parent.addUploadRow2(crow,index2,index3,sdetial_id,type);
	        	 }else if(index==3){
	        		 parent.addUploadRow3(crow,index2,index3,sdetial_id,type);
	        	 }else if(index==4){
	        		 parent.addUploadRow4(crow,index2,index3,sdetial_id,type);
	        	 }else if(index==5){
	        		 parent.addUploadRow5(crow,index2,index3,sdetial_id,type);
	        	 }else if(index==6){
	        		 parent.addUploadRow6(crow,index2,index3,sdetial_id,type);
	        	 }else if(index==7){
	        		 parent.addUploadRow7(crow,index2,index3,sdetial_id,type);
	        	 }else if(index==8){
	        		 parent.addUploadRow8(crow,index2,index3,sdetial_id,type);
	        	 }
        	 }
    }
	} else {
		for(var i=1;i<rows.length;i++){
	    	 crow=rows[i];
	         if (parent.$('inIframe')) {
	        	 parentContainer.addUploadRowWithId(crow,id);
	        	 } else {
	        	 	parent.addUploadRowWithId(crow,id);
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
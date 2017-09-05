<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<html>
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<title>附件上传</title>
	<script type="text/javascript">
		function upload(){
			if(!submitForm('fm')){
				return false;
			}
			document.getElementById('fm').action = "<%=contextPath %>/common/FileUploadAction/filesUpload.do";
			document.getElementById("fm").submit();
		}
		function delUploadFile(obj){
		  		var idx = obj.parentElement.parentElement.rowIndex;
				var tbl = document.getElementById('uptab');
				tbl.deleteRow(idx);
		}
		
		function setBackValue(){
		   var crow;
		    var rows = document.getElementById('uptab').rows;
		    if(rows.length==0){
		        MyAlert("您还没有上传附件");
		    	return;
		    }
		  
		  try{
		    for(var i=0;i<rows.length;i++){
		    	 crow=rows[i];
		         parent.addUploadRow(crow);
		    }
		    parent.closeUploadWin();
		   }catch(e){MyAlert(e.message);}
		}
		
		function cancel(){
			_hide();
		}
		var loadingIdx, uploadIdex=0;
		function showLoadImg(){
			loadingIdx = layer.load(1, {shade: [0.1,'#000']});
		}
		function hideLoadImg(){
			if(loadingIdx) {
				layer.close(loadingIdx);
			}
		}
		function getUploadFileIndex() {
			uploadIdex++;
			if(uploadIdex) return uploadIdex;
		}
	</script>
</head>
<body>
	<div class="wbox">
		<form id="fm" method="post">
			<table class="table_query">
				<tr>
					<td>
						<iframe id="upIframe" name="upIframe" src="<%=contextPath%>/dialog/upload/uploadCommonIF.jsp" width='100%' height=80  scrolling="no" style="overflow-x:hidden;overflow-y:visible;" frameborder="0"></iframe>
					</td>
				</tr>
			</table>
			<div id="myGrid">
				<table class="table_list" id="uptab" ></table>
			</div>
			<br/><br/>
			<center>
				<input class="u-button u-cancel" type="button" id="addBtn" value="确认" onclick='setBackValue()'  />	
			</center>
		</form>
	</div>
</body>
</html>
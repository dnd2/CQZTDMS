<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();


	String fileId = (String)request.getAttribute("fileId");
	String fileUrl = (String)request.getAttribute("fileUrl");
	String fileName = (String)request.getAttribute("fileName");
    String errMsg = (String)request.getAttribute("errMsg");
    
    Object obj = request.getAttribute("fjid");
    String fjid = "";
    if(obj != null)
    	fjid = obj.toString();
%>
<html>
<head>
	<title>附件上传</title>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<script type="text/javascript">
	    function setValue(){
	    	var fileName = '<%=fileName%>';
	    	var fileId   = '<%=fileId%>';
	    	var fileUrl = '<%=fileUrl %>';
	    	var fjid = "<%=fjid%>";
	    	var tab = "";
	    	if(fileName!=null && fileName !='' && fileName!='null'){
	    	   tab = parent.document.getElementById('uptab');
	    	    var row = tab.insertRow();
	    	    row.className = "table_list_row1";
	    	    var td = document.createElement("td");
	    	    td.style.width = "30px";
	    	    td.innerHTML = parent.getUploadFileIndex();
	    	    row.appendChild(td);
	    	    td = document.createElement("td");
	    	    td.style.width = "150px";
	    	    td.style.textAlign = "left";
	    	    td.innerHTML = "<a target='_blank' href='"+fileUrl+"' title='"+fileName+"'>"+fileName+"</a>" +
	    	    	"<input type='hidden' value='"+fileId+"' name='uploadFileId'/>" +
	    	    	"<input type='hidden' value='"+fileName+"' name='uploadFileName'/>" + 
	    	    	"<input type='hidden' name='fjid' value='"+fjid+"'/>";
	    	    	 row.appendChild(td);
	    	    td = document.createElement("td");
	    	    td.style.width = "60px";
	    	    td.innerHTML = 	"<a href='#' onclick='delUploadFile(this)'>[删除]</a>";
	    	    row.appendChild(td);
	    	} 
	    }
	
		function upload(){
			if(document.getElementById("uploadFile").value != "") {
				parent.showLoadImg();
				fm.action = "<%=contextPath %>/common/FileUploadAction/filesUpload.do";
		    	fm.submit();
			}
			else
			{
				parent.MyAlert('请选择上传文件!');				
			}
		}
		
		function setTmpFileName(fileName) {
			$("#tmpFileName").val(fileName);
		}
		
		$(function(){
			try{
				parent.hideLoadImg();
				setValue();
			}catch(e){alert(e);}
		});
	</script>
</head>
<body>
	<form id="fm" method="post" enctype="multipart/form-data">
		<table class="table_query" width='100%'>
			<tr>
				<td class="right">文件上传：</td>
				<td>
					<input type="file" id="uploadFile" name="uploadFile" style="display:none;"  id="uploadFile" onchange="setTmpFileName(this.value)"/>
					<input type="text" class="middle_txt" id="tmpFileName" readonly="readonly"/>
					<input class="u-button u-submit" type="button" id="addBtn" value="浏  览" onclick="$('#uploadFile').click()" />
					<input class="u-button u-submit" type="button" id="addBtn" value="开始上传" onclick="upload()" />
					<font color='red'><% if(errMsg!=null ){ %> <%=errMsg %><% } %> 
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
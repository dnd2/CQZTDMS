<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();


	String fileId = (String)request.getAttribute("fileId");
	String fileUrl = (String)request.getAttribute("fileUrl");
	String fileName = (String)request.getAttribute("fileName");
    String errMsg = (String)request.getAttribute("errMsg");
    
    //added by andy.ten@tom.com
    Object obj = request.getAttribute("fjid");
    String fjid = "";
    if(obj != null)
    	fjid = obj.toString();
%>
<html>
<head>
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<title>Insert title here</title>
</head>
<body onload="setValue()">
<form id="fm" method="post" enctype="multipart/form-data">
<table class="table_add" width='100%'  >
	<tr>
		<td align='right'  width=20% nowrap="nowrap">文件上传：</td>
		<td   nowrap="nowrap" class="table_add_2Col_input" align='left' >
			<input type="file" name="uploadFile" style="width: 250px"  datatype="0,is_null,2000" id="importFile" value="" />
		</td>
	</tr>
	<tr>
	   <td></td>
	   <td><font color='red'  ><% if(errMsg!=null ){ %> <%=errMsg %><% } %> </font></td>
	</tr>
	<tr>
		<td nowrap="nowrap"  align='right' colspan=2 >
		<input class="normal_btn" type="button" id="addBtn" value="上传" onclick="upload()" />
		<input class="normal_btn" type="reset" id="clearBtn"  value="清 空"/>
		</td>
	</tr>
</table>
</form>
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
    	    row.insertCell();
    	    row.insertCell();
    	    row.className='table_list_row1';
    	    row.cells(0).innerHTML="<a  target='_blank' href='"+fileUrl+"'>"+fileName+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId'/>"+"<input type='hidden' value='"+fileName+"' name='uploadFileName'/>" + "<input type='hidden' name='fjid' value='"+fjid+"'/>";
    	    row.cells(1).innerHTML="<input type=button onclick='delUploadFile(this)' class='normal_btn' value='删 除' />";
    	}
    }

	function upload(){
		$("addBtn").disabled = true;
		if(!submitForm('fm'))
		{
			$("addBtn").disabled = false;
			return false;
		}
		$('fm').action = "<%=path %>/common/dealerFileUploadAction/fileUpload.do";
    	$("fm").submit();
	}
	
</script>

</body>
</html>
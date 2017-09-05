<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class='table_info' id='fileUploadTab'>
	<tr>
		<th>附件名称</th>
	</tr>
</table>

<script type="text/javascript">
 function delUploadFile(obj){
  		var idx = obj.parentElement.parentElement.rowIndex;
		var tbl = document.getElementById('fileUploadTab');
		tbl.deleteRow(idx);
}

function addUploadRowByDb(filename,fileId,fileUrl,fjid){
 var tab =document.getElementById("fileUploadTab");
 var row =  tab.insertRow();
  row.className='table_list_row1';
  row.insertCell();
  row.insertCell();
  //modified by andy.ten@tom.com
  if(fjid){
  	row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='fjid' id='fjid'/>";
  }else{
    row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fileId+"' name='fjid' id='fjid'/>";
  }
}
function showUploadRowByDb(filename,fileId,fileUrl,fjid){
  var tab =document.getElementById("fileUploadTab");
  var row =  tab.insertRow();
  row.className='table_list_row1';
  row.insertCell();
  if(fjid)
  	row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='uploadFjid' />";
  else
  	row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> ";
}

function addUploadRow(obj)
{
 var tab =document.getElementById("fileUploadTab");
 var row =  tab.insertRow();
  row.className='table_list_row1';
  row.insertCell();
  row.insertCell();
  //added by andy.ten@tom.com
  row.setAttribute("FJID" ,obj.cells[0].childNodes[3].value);
  row.cells(0).innerHTML = obj.cells[0].innerHTML;
  //end
}

</script>

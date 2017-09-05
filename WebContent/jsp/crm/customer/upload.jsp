<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class='table_info' id='fileUploadTab' style="display:none;">
	<tr>
		<th>附件名称</th>
		<th>操作</th>
	</tr>
</table>
<div id="imgDiv" style="display:none;"></div>
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
  if(fjid){
  	row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='fjid' id='fjid'/>";
  }else{
    row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fileId+"' name='fjid' id='fjid'/>";
  }
  row.cells(1).innerHTML = "<input type=button onclick='delUploadFile(this)' class='normal_btn' value='删 除' />";    
}

function addUploadRowByDbOnlyView(filename,fileId,fileUrl,fjid){
 var tab =document.getElementById("fileUploadTab");
 var row =  tab.insertRow();
  row.className='table_list_row1';
  row.insertCell();
  row.insertCell();
  if(fjid){
  	row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='fjid' id='fjid'/>";
  }else{
    row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fileId+"' name='fjid' id='fjid'/>";
  }
  row.cells(1).innerHTML = "<input type='hidden' onclick='delUploadFile(this)' class='normal_btn' value='删 除' />";    
}
function addUploadRowByDbView(filename,fileId,fileUrl,fjid){ //YH 2011.5.30 修改首页新闻附件不可用问题
	 var tab =document.getElementById("fileUploadTab");
	 var row =  tab.insertRow();
	  row.className='table_list_row1';
	  row.insertCell();
	  row.insertCell();	  
	  if(fjid){
	  	row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='fjid' id='fjid'/>";
	  }else{
	    row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fileId+"' name='fjid' id='fjid'/>";
	  }
	  row.cells(1).innerHTML = "<input type=button onclick='delUploadFile(this)' disabled='disabled'  class='normal_btn' value='删 除' />";    
}
function showUploadRowByDb(filename,fileId,fileUrl,fjid){
  var tab =document.getElementById("fileUploadTab");
  var row =  tab.insertRow();
  row.className='table_list_row1';
  row.insertCell();
  row.insertCell();
  if(fjid)
  	row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='uploadFjid' />";
  else
  	row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> ";
  row.cells(1).innerHTML = "<input type=button onclick='delUploadFile(this)' class='normal_btn' value='删 除' disabled=\"disabled\"/>";    
}

function addUploadRowByDL(filename,fileId,fileUrl,fjid){
	 var tab =document.getElementById("fileUploadTab");
	 var row =  tab.insertRow();
	  row.className='table_list_row1';
	  row.insertCell();
	  row.insertCell();
	  //modified by andy.ten@tom.com
	  if(fjid){
	  	row.cells(0).innerHTML = filename+"<input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='fjid' id='fjid'/>";
	  }else{
	    row.cells(0).innerHTML = filename+"<input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fileId+"' name='fjid' id='fjid'/>";
	  }
	  row.cells(1).innerHTML = "<a  target='_blank' href='"+fileUrl+"'>[下载]</a>";    
	}
//customer上传图片的方法
function addUploadRow(obj)
{
 $("imgDiv").innerHTML=obj.cells[0].innerHTML;
 document.getElementById("imgPath").src=$("imgDiv").children[0].href;
 document.getElementById("img_url").value=$("imgDiv").children[0].href;
}

function showUploadRowByDb_01(filename,fileId,fileUrl,fjid){
  var tab =document.getElementById("fileUploadTab_01");
  var row =  tab.insertRow();
  row.className='table_list_row1';
  row.insertCell();
  row.insertCell();
  if(fjid)
  	row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='uploadFjid' />";
  else
  	row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> ";
}

</script>

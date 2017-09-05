<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class='table_info' id='fileUploadTab'>
	<tr>
		<th>附件名称</th>
		<th>新件代码</th>
		<th>操作</th>
		<td><input type="hidden" id="beforeValue"/></td>
	</tr>
</table>

<script type="text/javascript">
//只需要显示的后缀名
var showImageSuffix = ".jpg,.JPG,.jpeg,.JPEG,.gif,.GIF,.bmp,.BMP,.png,.PNG";
var fileSuffix = "";
 function delUploadFile(obj){
  		var idx = obj.parentElement.parentElement.rowIndex;
		var tbl = document.getElementById('fileUploadTab');
		tbl.deleteRow(idx);
}
function delUploadFileExt(obj,fjid){
	var idx = obj.parentElement.parentElement.rowIndex;
	var tbl = document.getElementById('fileUploadTab');
	tbl.deleteRow(idx);
	var vs = document.getElementsByName("fjid");
	/*var fjs = "";
	for(var i=0;i<vs.length;i++){
		fjs = fjs +vs[i].value+",";
	}
	document.getElementById("fjids").value=fjs;
	*/
	document.getElementById("delAttachs").value =  fjid+","+document.getElementById("delAttachs").value;
	
}

function alterPartCode(obj) {
	var sss = document.getElementById('fileUploadTab');
	if (sss.rows.length > 1) {
		document.getElementById('beforeValue').value=obj;
	}
}

function alterPartCodeLast(obj) {
	var sss = document.getElementById('fileUploadTab');
	if (sss.rows.length > 1) {
		for (var i=1;i<sss.rows.length;i++) {
			
			if (sss.rows[i].childNodes[1].innerHTML != "" && sss.rows[i].childNodes[1].innerHTML == document.getElementById('beforeValue').value) {
				sss.rows[i].childNodes[1].innerHTML = obj;
			}
		}
	}
	document.getElementById('beforeValue').value = "";
}

function delAttach(obj) {
	var sss = document.getElementById('fileUploadTab');
	if (sss.rows.length > 1) {
		for (var i=1;i<sss.rows.length;i++) {
			if (sss.rows[i].childNodes[1].innerHTML != "" && sss.rows[i].childNodes[1].innerHTML == obj) {
				sss.deleteRow(i);
			}
		}
	}
}

function addUploadRowByDbExt(filename,fileId,fileUrl,fjid){
	 var tab =document.getElementById("fileUploadTab");
	 var row =  tab.insertRow();
	  row.className='table_list_row1';
	  row.insertCell();
	  row.insertCell();
	  if(fileUrl){
		  var strImg = fileUrl.split(".");
			fileSuffix = "."+strImg[strImg.length-1];
	  }
	  if(fjid){
		if(showImageSuffix.indexOf(fileSuffix)>=0){
			row.cells(0).innerHTML = "<a class='showimg' target='_blank' href='"+fileUrl+"'/>"+filename+"<span><img src='"+fileUrl+"' /></span></a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='fjid' id='fjid'/>";
	    }else{
	    	row.cells(0).innerHTML = "<a target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='fjid' id='fjid'/>";
	    }
	  	
	  }else{
	    if(showImageSuffix.indexOf(fileSuffix)>=0){
	    	row.cells(0).innerHTML = "<a class='showimg' target='_blank' href='"+fileUrl+"'/>"+filename+"<span><img src='"+fileUrl+"' /></span></a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fileId+"' name='fjid' id='fjid'/>";
	    }else{
	    	row.cells(0).innerHTML = "<a target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fileId+"' name='fjid' id='fjid'/>";
	    }
	    
	  }
	  row.cells(1).innerHTML = "<input type=button onclick='delUploadFileExt(this,"+fileId+")' class='normal_btn' value='删 除' />";    
	}

function addUploadRowByDb(filename,fileId,fileUrl,fjid){
 var tab =document.getElementById("fileUploadTab");
 var row =  tab.insertRow();
  row.className='table_list_row1';
  row.insertCell();
  row.insertCell();
  if(fileUrl){
	  var strImg = fileUrl.split(".");
	  fileSuffix = "."+strImg[strImg.length-1];
  }
  if(fjid){
	  if(showImageSuffix.indexOf(fileSuffix)>=0){
		  row.cells(0).innerHTML = "<a class='showimg' target='_blank' href='"+fileUrl+"'/>"+filename+"<span><img src='"+fileUrl+"' /></span></a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='fjid' id='fjid'/>";
	    }else{
	    	row.cells(0).innerHTML = "<a target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='fjid' id='fjid'/>";
	    }
  	
  }else{
	  if(showImageSuffix.indexOf(fileSuffix)>=0){
		  row.cells(0).innerHTML = "<a class='showimg' target='_blank' href='"+fileUrl+"'/>"+filename+"<span><img src='"+fileUrl+"' /></span></a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fileId+"' name='fjid' id='fjid'/>";
	    }else{
	    	row.cells(0).innerHTML = "<a target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fileId+"' name='fjid' id='fjid'/>";
	    }
    
  }
  row.cells(1).innerHTML = "<input type=button onclick='delUploadFileExt(this)' class='normal_btn' value='删 除' />";    
}

function addUploadRowByDbView(filename,fileId,fileUrl,fjid,pjid,url){ //YH 2011.5.30 修改首页新闻附件不可用问题
	//lijj 2014.03.31 增加隐藏附件链接，提供附件下载功能 (url)
	 var tab =document.getElementById("fileUploadTab");
	 var row =  tab.insertRow();
	 var commonUrl = "<%=request.getContextPath()%>/util/FileDownLoad/fileDownloadQuery.do?fjid=";
	 url=(url==""||url==null||commonUrl.toLowerCase()==url.toLowerCase())?commonUrl:url;
	  row.className='table_list_row1';
	  row.insertCell();
	  row.insertCell();	 
	  row.insertCell();
	  if(fileUrl){
		  var strImg = fileUrl.split(".");
		  fileSuffix = "."+strImg[strImg.length-1];
	  }
	  if(fjid){
		  //row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='fjid' id='fjid'/>";
		  if(showImageSuffix.indexOf(fileSuffix)>=0){
			  row.cells(0).innerHTML = "<a class='showimg' target='_blank' href='"+url+fileId+"'/>"+filename+"<span><img src='"+fileUrl+"' /></span></a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='fjid' id='fjid'/>";
		    }else{
		    	row.cells(0).innerHTML = "<a target='_blank' href='"+url+fileId+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='fjid' id='fjid'/>";
		    }
		  
		  row.cells(1).innerHTML = pjid;
	  }else{
		//row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fileId+"' name='fjid' id='fjid'/>";
		  if(showImageSuffix.indexOf(fileSuffix)>=0){
			  row.cells(0).innerHTML = "<a class='showimg' target='_blank' href='"+url+fileId+"'/>"+filename+"<span><img src='"+fileUrl+"' /></span></a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fileId+"' name='fjid' id='fjid'/>";
		    }else{
		    	row.cells(0).innerHTML = "<a target='_blank' href='"+url+fileId+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fileId+"' name='fjid' id='fjid'/>";
		    }
		
	    row.cells(1).innerHTML = pjid;
	  }
	  row.cells(2).innerHTML = "<input type=button onclick='delUploadFile(this)' disabled='disabled'  class='normal_btn' value='删 除' />";    
}
function addUploadRowByDbShow(filename,fileId,fileUrl,fjid,pjid,url){ //YH 2011.5.30 修改首页新闻附件不可用问题
	 var tab =document.getElementById("fileUploadTab");
	 var row =  tab.insertRow();
	 var commonUrl = "<%=request.getContextPath()%>/util/FileDownLoad/fileDownloadQuery.do?fjid=";
	 url=(url==""||url==null||commonUrl.toLowerCase()==url.toLowerCase())?commonUrl:url;
	  row.className='table_list_row1';
	  row.insertCell();
	  row.insertCell();	 
	  row.insertCell();
	  if(fileUrl){
		  var strImg = fileUrl.split(".");
		  fileSuffix = "."+strImg[strImg.length-1];
	  }
	  if(fjid){
		  //row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='fjid' id='fjid'/>";
		  if(showImageSuffix.indexOf(fileSuffix)>=0){
			  row.cells(0).innerHTML = "<a class='showimg' target='_blank' href='"+url+fileId+"'/>"+filename+"<span><img src='"+fileUrl+"' /></span></a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='fjid' id='fjid'/>";
		    }else{
		    	row.cells(0).innerHTML = "<a target='_blank' href='"+url+fileId+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='fjid' id='fjid'/>";
		    }
		  
		  row.cells(1).innerHTML = pjid;
	  }else{
		//row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fileId+"' name='fjid' id='fjid'/>";
		  if(showImageSuffix.indexOf(fileSuffix)>=0){
			  row.cells(0).innerHTML = "<a class='showimg' target='_blank' href='"+url+fileId+"'/>"+filename+"<span><img src='"+fileUrl+"' /></span></a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fileId+"' name='fjid' id='fjid'/>";
		    }else{
		    	row.cells(0).innerHTML = "<a target='_blank' href='"+url+fileId+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' id='uploadFileId'/> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fileId+"' name='fjid' id='fjid'/>";
		    }
		
	    row.cells(1).innerHTML = pjid;
	  }
	  row.cells(2).innerHTML = "<input type=button onclick='delUploadFile(this)'   class='normal_btn' value='删 除' />";    
}
function showUploadRowByDb(filename,fileId,fileUrl,fjid,url){
  var tab =document.getElementById("fileUploadTab");
  var row =  tab.insertRow();
  var commonUrl = "<%=request.getContextPath()%>/util/FileDownLoad/fileDownloadQuery.do?fjid=";
  url=(url==""||url==null||commonUrl.toLowerCase()==url.toLowerCase())?commonUrl:url;
  row.className='table_list_row1';
  row.insertCell();
  row.insertCell();
  if(fileUrl){
	  var strImg = fileUrl.split(".");
	  fileSuffix = "."+strImg[strImg.length-1];
  }
  if(fjid){
	  if(showImageSuffix.indexOf(fileSuffix)>=0){
		  row.cells(0).innerHTML = "<a class='showimg' target='_blank' href='"+url+fjid+"'/>"+filename+"<span><img src='"+fileUrl+"' /></span></a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='uploadFjid' />";
	    }else{
	    	row.cells(0).innerHTML = "<a target='_blank' href='"+url+fjid+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='uploadFjid' />";
	    }
  	
  }else{
	  if(showImageSuffix.indexOf(fileSuffix)>=0){
		  row.cells(0).innerHTML = "<a class='showimg' target='_blank' href='"+url+fileId+"'/>"+filename+"<span><img src='"+fileUrl+"' /></span></a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> ";
	    }else{
	    	row.cells(0).innerHTML = "<a target='_blank' href='"+url+fileId+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> ";
	    }
  	
  }
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
	  row.cells(1).innerHTML = "<a class='showimg' target='_blank' href='"+fileUrl+"'>[下载]</a>";    
	}
function addUploadRowWithId(obj,id) {
	var tab =document.getElementById("fileUploadTab");
	 var row =  tab.insertRow();
	  row.className='table_list_row1';
	  row.insertCell();
	  row.insertCell();
	  row.insertCell();
	  //added by andy.ten@tom.com
	  row.setAttribute("FJID" ,obj.cells[0].childNodes[3].value);
	  row.cells(0).innerHTML = obj.cells[0].innerHTML;
	  row.cells(1).innerHTML = id;
	  //end
	  row.cells(2).innerHTML = "<input type=button onclick='delUploadFile(this)' class='normal_btn' value='删 除' />";    
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
  row.cells(1).innerHTML = "<input type=button onclick='delUploadFile(this)' class='normal_btn' value='删 除' />";    
}

function showUploadRowByDb_01(filename,fileId,fileUrl,fjid){
  var tab =document.getElementById("fileUploadTab_01");
  var row =  tab.insertRow();
  row.className='table_list_row1';
  row.insertCell();
  row.insertCell();
  if(fileUrl){
	  var strImg = fileUrl.split(".");
	  fileSuffix = "."+strImg[strImg.length-1];
  }
  if(fjid){
	  if(showImageSuffix.indexOf(fileSuffix)>=0){
		  row.cells(0).innerHTML = "<a class='showimg' target='_blank' href='"+fileUrl+"'/>"+filename+"<span><img src='"+fileUrl+"' /></span></a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='uploadFjid' />";
	    }else{
	    	row.cells(0).innerHTML = "<a target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> "+"<input type='hidden' value='"+fjid+"' name='uploadFjid' />";
	    }
  	
  }else{
	  if(showImageSuffix.indexOf(fileSuffix)>=0){
		  row.cells(0).innerHTML = "<a class='showimg' target='_blank' href='"+fileUrl+"'/>"+filename+"<span><img src='"+fileUrl+"' /></span></a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> ";
	    }else{
	    	row.cells(0).innerHTML = "<a target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> ";
	    }
  	
  }
}

</script>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>


<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发票补录 </title>

</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理&gt;结算管理&gt;结算发票补录&gt;发票补录</div>
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<div class="form-panel">
	<h2>发票补录</h2>
	<div class="form-body">  
	<table class="table_query">
	 <tr class="csstr">
	  </tr> 
	  	<tr>
		    <td class="table_query_label" colspan="2">发票号：<input type="text" name="invoice_no" id="invoice_no" datatype="0,is_null,30" class="middle_txt" /></td>
	    </tr>
		<tr> 
			<td class="table_query_label" colspan="2">
				1、点“<font color="#FF0000">浏览</font>”按钮，找到您所要导入发票补录图片附件：&nbsp;&nbsp;&nbsp;</td>
	    </tr>
		<tr>
		    <td class="table_query_input" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp; 
		      <input type="file" name="uploadFile" datatype="0,is_null,2000" id="upfile" value="" multiple="multiple" accept="image/jpeg,image/jpg,image/png,image/svg,image/bmp"/>
		      
		    </td>
	    </tr>
		<tr> 
			<td class="table_query_label" width="30%">2、选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成粘贴。</td>
		    <td class="table_query_label" align="left" width="56%">
		       <input type="hidden" name="applyId" id="applyId" value="${applyId}"/>
		       <input type="button" id="upbtn" class="normal_btn"  name="vdcConfirm" value="确定" onclick="upload()"/>
		    </td>
		</tr>
	</table>
</div>
</div>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
function upload(){
	//if(submitForm('fm')){
		var invoiceNo=document.getElementById("invoice_no").value;
		if(invoiceNo==''){
			MyAlert("请填写发票号"); 
			return false; 
		}
		var file =document.getElementById("upfile").files;
		if(file.length==0) 
		{ 
			MyAlert("请选择图片文件"); 
			return false; 
		} 
		var flag=true;
		for(var i=0;i<file.length;i++){
			var importFileName = file[i].name;
			var index = importFileName.lastIndexOf(".");
			var suffix = importFileName.substr(index+1,importFileName.length).toLowerCase();
			//MyAlert(importFileName+","+index+","+suffix);
			if(suffix != "jpeg"&&suffix != "jpg"&&suffix != "png"&&suffix != "svg"&&suffix != "bmp"){
				MyAlert("图片格式不正确只能是jpeg，jpg，png，svg，bmp后缀结束");
				flag=false;
				break;
			}
		}
		if(flag){
			document.getElementById("upbtn").disabled=true;
			document.getElementById('fm').action = "<%=request.getContextPath()%>/sales/storage/balancemanage/SalesBalanceSuppply/addBalSupply.do";
			document.getElementById("fm").submit();
		}
	//}
}

</script>
</body>
</html>

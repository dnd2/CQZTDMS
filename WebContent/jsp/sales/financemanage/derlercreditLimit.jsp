<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%>
<head>
<%
String contentPath=request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>返利任务导入 </title>
<script language="JavaScript">
	function doInit(){
		createMonthOptions();
	}
	//创建月份OPTION
	function createMonthOptions(){
		var curyear=${curYear};
		var year=document.getElementById("year").value;
		var month=${curMonth};
		var obj=document.getElementById("month");
		clrOptions(obj);
		if(year!=curyear){
			month=1;
		}
		for(var i=month;i<13;i++){
			var opt=document.createElement("option");	
			opt.value=i;
			opt.appendChild(document.createTextNode(i));
			obj.appendChild(opt);	
		}
	}
	//清空月份OPTION
	function clrOptions(obj){
		obj.options.length=0;
	}
</script>

<script type="text/javascript">

	function upload(){
		/* if(!submitForm('#fm')){
			return false;
		} */
		var upfile = document.getElementById("upfile").value;
		if(upfile==null||upfile==''){
			MyAlert("未选取文件");
			return;
		}
		document.getElementById("upbtn").disabled=true;
		var fm = document.getElementById("fm");
		fm.action = "<%=contentPath %>/sales/financemanage/DerlerCreditLimit/ExcelOperate.do";
    	fm.submit();					
	}
	
	function downloadFile(){
		var fm = document.getElementById("fm");
	    fm.action= "<%=contentPath %>/sales/financemanage/DerlerCreditLimit/downloadTemple.do";
	    fm.submit();
	}
	
</script>

</head>

<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contentPath %>/img/nav.gif" />&nbsp;当前位置>财务管理>返利管理>返利任务导入
	</div>
<form name="fm" id="fm"  method="post"  enctype="multipart/form-data">
  <div class="form-panel">
			<h2>返利任务导入</h2>
			<div class="form-body">	
<table class="table_query">
 
	<tr> 
		<td class="table_query_label" colspan="2">
			1、点“<font color="#FF0000">浏览</font>”按钮，找到您所要导入的的返利目标文件,请确定文件的格式为“<strong>经销商代码—经销商简称—返利金额—备注</strong>”：&nbsp;&nbsp;&nbsp;</td>
    </tr>
	<tr>
	    <td class="table_query_input" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp; 
	      <input type="file" name="uploadFile" datatype="0,is_null,2000" id="upfile" value="" />
	    </td>
    </tr>
	<tr> 
		<td class="table_query_label" width="30%">2、选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成粘贴。</td>
	    <td class="table_query_label" align="left" width="56%">
	       <input type="button" id="upbtn" class="u-button u-query"  name="vdcConfirm" value="确定" onclick="upload()" />
	       <input type="button" class="u-button u-submit"  name="downloadBtn" value="导入模板下载" onclick="downloadFile()" />
	    </td>
	</tr>
</table>
</div>
</div>
</form>
</div>

</body>
</html>

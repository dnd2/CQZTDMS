<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
String contentPath=request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<link href="<%=contentPath %>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contentPath %>/style/calendar.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=contentPath %>/js/jslib/prototype.js"></script>
<script type="text/javascript" src="<%=contentPath %>/js/jslib/calendar.js"></script>
<title>工作日历导入</title>
<script language="JavaScript" src="<%=contentPath %>/js/ut.js"></script>

<script language="JavaScript">
<!--
/*function finish(obj){
		location='targetOrder_importdetail_01.htm';
		if(document.FRM.Type.value==1){
			location='targetOrder_importdetail.htm';
		}
		if(document.FRM.Type.value==0){
			location='targetOrder_importdetail_01.htm';
		}*/
	}
//-->
</script>
</head>

<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contentPath %>/img/nav.gif" />&nbsp;当前位置>工作日历导入
	</div>
<form name="fm" method="post">
  
<table class="table_query">
  <tr>
    <td class="table_query_label" colspan="2">1、请选择要导入目标的年份：
      <select name='year'>
        <%
        	Integer year=(Integer)request.getAttribute("year");
        	int intYear=year.intValue();
        %>
        	<option value="<%=intYear %>"><%=intYear %></option>
        	<option value="<%=intYear+1 %>"><%=intYear+1 %></option>
      </select>
    </td>
  </tr>
	<tr> 
		<td class="table_query_label" colspan="2">
			3、点“<font color="#FF0000">浏览</font>”按钮，找到您所要导入的的工作日历文件,请确定文件的格式为“<strong>年度—月份—周度—日期</strong>”：&nbsp;&nbsp;&nbsp;</td>
    </tr>
	<tr>
	    <td class="table_query_input" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp; 
	      <input type="file" name="uploadFile" id="uploadFile" style="width: 250px"  datatype="0,is_null,2000" id=""uploadFile"" value="" />
	    </td>
    </tr>
	<tr> 
		<td class="table_query_label" width="30%">4、选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成粘贴。</td>
	    <td class="table_query_label" align="left" width="56%">
	       <input type="button" class="cssbutton"  name="vdcConfirm" id="vdcConfirm" value="确定" onclick="upload()" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	       <input type="button" class="cssbutton"  name="execConfirm" id="execConfirm" value="生成" onclick="execConfirmIt() ;" />
	    </td>
	</tr>
</table>
</form>
</div>
<script type="text/javascript">

	function upload(){
		if(!submitForm('fm')){
			return false;
		}
		$('fm').action = "<%=contentPath %>/sysmng/paraConfig/DateSetImport/dateSetExcelOperate.do";
    	$("fm").submit();
	}
	
	function execConfirmIt() {
		//$('fm').action = "<%=contentPath%>/sysmng/paraConfig/DateOfJob/exeDateOfJob.do";
    	//$("fm").submit();
    	document.getElementById("execConfirm").disabled = true ;
		var url = "<%=contentPath%>/sysmng/paraConfig/DateOfJob/exeDateOfJob.json";
		makeFormCall(url,showItResult,"fm") ;
	}
	
	function showItResult(json) {
		document.getElementById("execConfirm").disabled = false ;
		if(json.resultStr == "0") {
			MyAlert("生成失败:已存在需要生成的数据!") ;
		} else if(json.resultStr == "1") {
			MyAlert("生成成功!") ;
		}
	}
</script>
</body>
</html>

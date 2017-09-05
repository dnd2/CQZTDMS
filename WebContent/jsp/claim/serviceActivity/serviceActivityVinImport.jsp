<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动-服务活动设定-VIN清单</title>
<%
	String contextPath = request.getContextPath();
%>
<script language="JavaScript">
	//功能：导入文件功能;
	//描述：导入指定的VIN;
    function importFileUpload(){
    	disableBtn($("commitBtn"));//点击按钮后，按钮变成灰色不可用;
    	var fileName = document.FRM.importFile.value;
		if(fileName==""){
			MyAlert("您没有选择要上传的文件！");
			return false;
		}
		if(fileName.indexOf(".xls")<0&&fileName.indexOf(".csv")<0){
			MyAlert("请选择xls格式或csv格式文件！");
			return false;
		}
		var activityId=document.getElementById("activityId").value;
		var flag=document.getElementById("flag").value;
		FRM.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityVinImport/serviceActivityVinImportOption.do?activityId="+activityId+"&flag="+flag;
		FRM.submit();
	}
</script>
</head>

<body>
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动管理
	</div>
 <form enctype="multipart/form-data" method="post"  name="FRM" target="_self" id="FRM">
	 <input type="hidden" name="activityId" id="activityId" value="<%=request.getAttribute("activityId") %>"/>
	 <input type="hidden" name="flag" id="flag" value="<%=request.getAttribute("flag") %>"/>
<table class="table_query">
	<tr>
	  	<td>
 			1、点“<font color="#FF0000">浏览</font>”按钮，找到您所要上传VIN清单文件。
  	   </td>   
		<td>
			  <input type="file" name="importFile" class="InputButton"  size="30">
		</td>
    </tr>
    <tr> 
		<td>2、选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成粘贴。</td>
	    <td>
	    	<input type="button" value="确定" name="button1"  class="normal_btn" onclick="importFileUpload();" id="commitBtn">
	    	<input type="button" value="关闭" name="button2" class="normal_btn" onclick="_hide();">
	    </td>
    </tr>
</table>
</form>
</body>
</html>

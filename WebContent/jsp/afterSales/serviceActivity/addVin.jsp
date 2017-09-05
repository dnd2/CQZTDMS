<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@page import="com.infodms.dms.common.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/jsp_head_new.jsp"%> 
<script type="text/javascript" >
 	function uploadExcel(){
 		var fm = document.getElementById("fileForm");
 		document.getElementById("backBtn1").disabled=true;
 		fm.action = "<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/uploadExcel.do";
 		fm.submit();
 	}
 	
 	function backInit(){
 		var id = $("#id").val();
 		window.location.href = "<%=contextPath%>/jsp/afterSales/serviceActivity/checkVin.jsp?activityId="+id+"&status=1";
 	}
 	
 	//下载模板
 	function downloadExcel(){
 		window.location.href =  "<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/downloadExcel.json";
 	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>vin导入</title>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： VIN导入</div>
<form method="post" name="fileForm" id="fileForm" enctype="multipart/form-data">
<input type="hidden" id="id" name="id" value="${id }"/>
<table  class="table_query">
	<tr>
		<td class="left">1、点击<input class="normal_btn" type="button" name="button"  id="backBtn" value="导入模板下载" onclick="downloadExcel();" style="width: 80px;"/>下载模板。</td>
	</tr>
	<tr>
		<td class="left">2、点击“浏览”按钮，找到您所要导入的计划文件:</td>
	</tr>
	<tr>
		<td class="left">
			3、<input id="uploadFile" name="uploadFile" type="file"/>
		</td>
	</tr>
	<tr>
		<td colspan="6" style="text-align: center">
			<input class="u-button u-submit" type="button" name="queryBtn"  id="backBtn1" value="保存" onclick="uploadExcel();"/>
			<input class="u-button u-cancel" type="button" name="button"  id="backBtn" value="返回" onclick="backInit();"/>
		</td>
	</tr>
</table>
</form>
</div>
	<c:if test="${msg == 0 }">  
		<script type="text/javascript">
			MyAlert("上传文件应是.xls文件！");
		</script>
	</c:if>
	<c:if test="${msg == 1 }">  
		<script type="text/javascript">
		MyAlert("上传文件为空！");
		</script>
	</c:if>
	<c:if test="${msg == 2 }">  
		<script type="text/javascript">
		var message = '${message}';
		MyAlert("第"+message+"行vin不能为空！");
		</script>
	</c:if>	
	<c:if test="${msg == 3 }">  
		<script type="text/javascript">
		var message = '${message}';
		var vin = '${vin}';
		MyAlert("导入数据重复！第"+message+"行,vin:"+vin+"。");
		</script>
	</c:if>
	<c:if test="${msg == 4 }">
		<script type="text/javascript">
			MyAlert("导入成功！");
			backInit();
		</script>
	</c:if>	
	<c:if test="${msg == 5 }">  
		<script type="text/javascript">
		MyAlert("请选择导入文件！");
		</script>
	</c:if>		
	<c:if test="${msg == 6 }">  
		<script type="text/javascript">
			var message = '${message}';
			MyAlert("第"+message+"行vin不存在！");
		</script>
	</c:if>
</body>
</html>
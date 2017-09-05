<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<title>车辆PIN码导入</title>

</head>

<body >
<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">当前位置&gt;售后服务管理&gt;车辆信息管理&gt;车辆PIN码导入</div>
 <form name="fm" method="post"  enctype="multipart/form-data">
<table class="table_query">  
  <tr>
    <td class="table_query_label" colspan="2">1、点击
          <input type="button" class="long_btn"  name="downloadBtn" value="导入模板下载" onclick="downloadFile()"/>
          下载模板。</td>
  </tr>
	<tr> 
		<td class="table_query_label" colspan="2">
			2、点“<font color="#FF0000">浏览</font>”按钮，找到您所要导入的车辆PIN码文件:</td>
    </tr>
	<tr>
	    <td class="table_query_input" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp; 
	      <input type="file" name="uploadFile" id="uploadFile" style="width: 250px"  datatype="0,is_null,2000" value="" />	    </td>
    </tr>
	<tr> 
		<td class="table_query_label" width="65%">3、选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成导入，导入格式:<font color="#FF0000">VIN--PIN(VIN必须在系统中存在)</font>。</td>
	</tr>
	<tr>
	    <td class="table_query_label" align="center" colspan="6">
	       <input type="button" class="cssbutton"  name="queryBtn" value="确 定" onclick="confirmUpload()" />
	    </td>
	</tr>
</table>
</form>
<script type="text/javascript">

	//上传检查和确认信息
	function confirmUpload()
	{
		if(fileVilidate()){
			MyConfirm("确定上传选中的文件?",upload,[]);
		}
	}
	
	function upload(){
		btnDisable();
		fm.action = "<%=contextPath%>/repairOrder/VehicleQueryPin/exceloadVehicleQueryPinByDlr.do";
		fm.submit();
	}

	function fileVilidate(){
		var importFileName = $("uploadFile").value;
		if(importFileName==""){
		    MyAlert("请选择上传文件");
			return false;
		}
		var index = importFileName.lastIndexOf(".");
		var suffix = importFileName.substr(index+1,importFileName.length).toLowerCase();
		if(suffix != "xls"){
		MyAlert("Excel格式不正确只能是xls后缀结束");
			return false;
		}
		return true;
	}
	
	//下载模板
	function downloadFile(){
		fm.action = '<%=contextPath%>/repairOrder/VehicleQueryPin/downloadVehicleQueryPin.do';
	    fm.submit();
	}

	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	}
</script>
</body>
</html>

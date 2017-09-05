<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	Long userId = (Long)request.getAttribute("logonUser");
%>

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/globalVariable.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/style/table.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/LigerUI/Source/lib/jquery/jquery-1.5.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/ajaxfileupload.js"></script>
<title>导入新闻</title>
<script type="text/javascript">
	var buttons = ["importFile", "importAttachment", "importRelations"];
	$(document).ready(function(){
	});
	// 上传新闻文件
	function uploadFile(){
		disableButton();
    	var fileNode = document.getElementById("inputFile");
		if(!validate(fileNode)){
			useableButton();
			return;
		}
    	FRM.action = globalContextPath + "/claim/basicData/HomePageNews/importNews.do";
		FRM.submit();
	}
	
	// 校验上传文件
	function validate(fileNode,notNull){
    	var fileName = fileNode.value;
		if(fileName==""){
			MyAlert("您没有选择要上传的文件！");
			return false;
		}
		if(notNull){
			return true;
		}
		if(fileName.indexOf(".xls")<0&&fileName.indexOf(".xlsx")<0){
			MyAlert("请选择xls格式或xlsx格式文件！");
			return false;
		}
		return true;
	}
    // 禁用按钮
	function disableBtn(obj){
		obj.disabled = true;
		obj.style.border = '1px solid #999';
		obj.style.background = '#EEE';
		obj.style.color = '#999';
	}
    // 启用按钮
	function useableBtn(obj){
		obj.disabled = false;
		obj.style.border = '1px solid #5E7692';
		obj.style.background = '#EEF0FC';
		obj.style.color = '#1E3988';
	}

    // 导出模版文件
    function exportModel(exportButton){
    	exportButton.parentNode.removeChild(exportButton);
    	//disableBtn(exportButton);
    	FRM.action = globalContextPath + "/claim/basicData/HomePageNews/exportNewsModel.do";
		FRM.submit();
    }
    // 导出关联文件
    function exportRelationsModel(exportButton){
    	exportButton.parentNode.removeChild(exportButton);
    	//disableBtn(exportButton);
    	FRM.action = globalContextPath + "/claim/basicData/HomePageNews/exportNewsRelations.do";
		FRM.submit();
    }

    // 禁用按钮
    function disableButton(){
    	for(var i=0;i<buttons.length;i++){
    		var ibtn = document.getElementById(buttons[i]);
        	disableBtn(ibtn);
    	}
    }

    // 启用按钮
    function useableButton(){
    	for(var i=0;i<buttons.length;i++){
    		var ibtn = document.getElementById(buttons[i]);
    		useableBtn(ibtn);
    	}
    }
    
    // 上传附件
    function uploadAttachment(){
		disableButton();
    	var fileNode = document.getElementById("inputFile");
		if(!validate(fileNode,true)){
			useableButton();
			return;
		}
    	FRM.action = globalContextPath + "/claim/basicData/HomePageNews/importNewsAttachment.do";
		FRM.submit();
    }

	// 上传关联文件
	function uploadRelations(){
		disableButton();
    	var fileNode = document.getElementById("inputFile");
		if(!validate(fileNode)){
			useableButton();
			return;
		}
    	FRM.action = globalContextPath + "/claim/basicData/HomePageNews/importRelations.do";
		FRM.submit();
	}
	
	function turnBack(){
    	window.location.href = globalContextPath + "/claim/basicData/HomePageNews/mainNews.do";
	}
	
</script>
</head>
<body>
	<form enctype="multipart/form-data" method="post"  name="FRM" target="_self" id="FRM">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;个人信息管理&gt;首页新闻导入
		</div>
		<div>
			<input size="80" type="file" id="inputFile" name="inputFile" />
		</div>
		<br />
		<div align="center">
			<input class="normal_btn" type="button" name="ok" id="importFile" value="导入新闻" onclick="uploadFile();" />
			&nbsp;&nbsp;<input class="normal_btn" type="button" name="importAttachment" id="importAttachment" value="导入附件" onclick="uploadAttachment();" />
			&nbsp;&nbsp;<input class="normal_btn" type="button" name="importRelations" id="importRelations" value="导入关联" onclick="uploadRelations();" />
			&nbsp;&nbsp;<input class="normal_btn" name="back" type="button" onclick="turnBack();" value="返回" />
			&nbsp;&nbsp;<input style="width: 80px;" class="normal_btn" type="button" name="exportModelFile" id="exportModelFile" value="导出新闻模版" onclick="exportModel(this);" />
			&nbsp;&nbsp;<input style="width: 80px;" class="normal_btn" type="button" name="exportRelationsModelFile" id="exportRelationsModelFile" value="导出关系模版" onclick="exportRelationsModel(this);" />
		</div>
		<div>
			<font color="green" size="2">
			导入顺序:新闻-->附件-->关系!<br/>
			</font>
			<font color="red">
				${hint}
			</font>
		</div>
	</form>
</body>
</html>
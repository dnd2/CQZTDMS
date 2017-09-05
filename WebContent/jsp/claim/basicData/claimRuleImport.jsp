<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>三包规则维护-配件清单</title>
<%
	String contextPath = request.getContextPath();
%>
<script language="JavaScript">
	//功能：导入文件功能;
	//描述：导入指定的配件;
    function importFileUpload(){
    	document.getElementById("commitBtn").disabeld = true ;
    	var fileName = document.FRM.importFile.value;
		if(fileName==""){
			MyAlert("您没有选择要上传的文件！");
			document.getElementById("commitBtn").disabeld = false ;
			return false;
		}
		if(fileName.indexOf(".xls")<0&&fileName.indexOf(".csv")<0){
			document.getElementById("commitBtn").disabeld = false ;
			MyAlert("请选择xls格式或csv格式文件！");
			return false;
		}
		var ruleId=document.getElementById("ruleId").value;
		FRM.action = "<%=contextPath%>/claim/basicData/ClaimRule/claimRulePartsImportOption.do?ruleId="+ruleId;
		FRM.submit();
	}
</script>
</head>

<body>
<div class="wbox">
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;三包规则维护
	</div>
 <form enctype="multipart/form-data" method="post"  name="FRM" target="_self" id="FRM">
 <div class="form-panel">
		<h2>数据导入</h2>
			<div class="form-body">
 <input type="hidden" id="ruleId" name="ruleId" value="<%=request.getAttribute("ruleId") %>"/>
	<table class="table_query">
	<tr>
	  	<td>
 			1、点“<font color="#FF0000">浏览</font>”按钮，找到您所要上传配件清单文件。
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
	</div>
	</div>
 </form>
 </div>
</body>
</html>
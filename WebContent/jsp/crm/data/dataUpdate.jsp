<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>经销商用户组修改</title>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/style/system.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/style/diaCnt.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/mootools.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/mtcommon.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/web.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/menu.js"></script>
<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dict.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/dialog_new.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/DestoryPrototype.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/prototype.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/InfoAjax.js"></script>
<link rel="StyleSheet"type="text/css" href="<%=contextPath %>/style/dtree.css"/>
<script type="text/javascript" src="<%=contextPath %>/js/jslib/dtree.js"></script>
</head>
<body   onload="showList();">
<div class="wbox">
<div class="navigation" align="left"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 字典管理 &gt; 字典维护</div>
<form id="fm" name="fm" method="post">
<input type="hidden" id="userId" name="userId" value="${userId}"/>
<input type="hidden" id="sessionId" name="sessionId" value="${sessionId}"/>
<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
</form>
</div>
<div style="margin:2px; " align="left" id="div">
	<script type="text/javascript">
	String.prototype.trim = function(){  
		return this.replace(/(^\s*)|(\s*$)/g, "");  
		}  
	function showList(){
			var ids="${idlist}";
			var idArray=ids.substring(1,ids.length-1).split(",");
			var types="${typelist}";
			var typeArray=types.substring(1,types.length-1).split(",");
			var descs="${desclist}";
			var descArray=descs.substring(1,descs.length-1).split(",");
		//var childs="${childlist}";
		//	var childArray=childs.substring(1,childs.length-1).split(",");
			var str="";
			for(var k=0;k<idArray.length;k++){
				if(idArray[k].trim().length==4){
					str+="<a href='#'>"+descArray[k]+"</a><br/>";
				}else if(idArray[k].trim().length==8){
					str+="&nbsp;&nbsp;<a href='#'>"+descArray[k]+"</a><br/>";
				}else{
					str+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#'>"+descArray[k]+"</a><br/>";
				}
			}
			MyAlert(str);
			document.getElementById("div").innerHTML=str;
		}		
	</script>
	</div>
</body>
</html>

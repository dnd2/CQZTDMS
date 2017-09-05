<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<%@taglib uri="/jstl/x" prefix="x" %>

<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="mobile-web-app-capable" content="yes">
<meta name="format-detection"  content="telephone=no">
<meta http-equiv="x-rim-auto-match" content="none">
<meta http-equiv="pragma" content="no-cache">  
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate">  
<meta http-equiv="expires" content="0"> 
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />

<link href="<%=request.getContextPath()%>/jmstyle/css/font.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/jmstyle/css/calendar.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/jmstyle/css/content.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/jmstyle/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<script type="text/javascript">
	var globalContextPath = "<%=request.getContextPath()%>";
	var g_webAppName = "<%=request.getContextPath()%>";
</script>

<!-- jquery-1.12.4  -->
<script type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/jquery-1.12.4.js"></script>
<!-- 数据字典 -->
<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
<!-- 地级市数据字典表 -->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/regionData.js"></script>
<!-- 数据字典处理工具类 -->
<script type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/InfoDict.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/framecommon/HashMap.js"></script>
<!-- 公用弹出层工具类 -->
<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/js/framecommon/DialogManager.js"></script> --%>
<!-- 公用弹数据校验工具类 -->
<script type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/default.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/validate.js"></script>
<!-- AJAX后台数据访问类 -->
<script type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/InfoAjax.js"></script>
<!-- 公用弹出层工具类 -->
<script type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/InfoDialog.js"></script>
<!-- 附件上传公共类 -->
<script type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/InfoUpload.js"></script>
<!-- table 分页类  -->
<script type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/InfoGrid.js"></script>
<!-- 公用工具类  -->
<script type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/InfoCommon.js"></script>
<!-- 日期时间控件 -->
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/datepicker/WdatePicker.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dmssales.common.Constant" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="<%=request.getContextPath() %>/js/temp/webfxlayout.js"></script>
<link id="luna-tab-style-sheet" type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/js/temp/tab.css" disabled="disabled" />
<link id="webfx-tab-style-sheet" type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/js/temp/tab.webfx.css" disabled="disabled" />
<link id="winclassic-tab-style-sheet" type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/js/temp/tab.winclassic.css"  disabled="disabled" />

<style type="text/css">

.dynamic-tab-pane-control .tab-page {
	height:		200px;
}

.dynamic-tab-pane-control .tab-page .dynamic-tab-pane-control .tab-page {
	height:		100px;
}

html, body {
	background:	ThreeDFace;
}

form {
	margin:		0;
	padding:	0;
}

/* over ride styles from webfxlayout */

body {
	margin:		10px;
	width:		auto;
	height:		auto;
}

.dynamic-tab-pane-control h2 {
	text-align:	center;
	width:		auto;
}

.dynamic-tab-pane-control h2 a {
	display:	inline;
	width:		auto;
}

.dynamic-tab-pane-control a:hover {
	background: transparent;
}



</style>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/temp/tabpane.js"></script>
</head>
<body>
<script type="text/javascript">

function setLinkSrc( sStyle ) {
	document.getElementById( "luna-tab-style-sheet" ).disabled = sStyle != "luna";
	document.getElementById( "webfx-tab-style-sheet" ).disabled = sStyle != "webfx"
	document.getElementById( "winclassic-tab-style-sheet" ).disabled = sStyle != "winclassic"
	document.documentElement.style.background = 
	document.body.style.background = sStyle == "webfx" ? "white" : "ThreeDFace";	
}

setLinkSrc( "webfx" );
tp1 = new WebFXTabPane( document.getElementById( "tabPane1" ) );
tp2 = new WebFXTabPane( document.getElementById( "tabPane2" ) );
setupAllTabs();
</script>

<form action="void();">
<fieldset style="display:none;">
	<legend>Select Style Sheet</legend>
<input id="luna-radio" type="radio" name="tab-css-radio" 
	value="css/luna/tab.css" onclick="setLinkSrc('luna')"/>
<label for="luna-radio">luna/tab.css</label>
<br />
<input id="webfx-radio" type="radio" name="tab-css-radio" value="css/tab.webfx.css"
	 onclick="setLinkSrc('webfx')" checked="checked"/>
<label for="webfx-radio">tab.webfx.css</label>
<br />
<input id="classic-radio" type="radio" name="tab-css-radio"
	value="css/tab.winclassic.css" onclick="setLinkSrc('winclassic')"/>
<label for="classic-radio">tab.winclassic.css</label>
</fieldset>
</form>

</body>
</html>

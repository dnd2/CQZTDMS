<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<meta name="GENERATOR" content="Microsoft FrontPage 4.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<title><%=request.getParameter("root") %></title>
</head>

   <BODY>

<div sytle="width:0px; height:0px;">
<applet code="com.jatools.viewer.ZViewer.class" archive= "viewer.jar" width="0px" height="0px" align="baseline">
    <param name="root" value="<%=request.getParameter("root") %>">
    <param name="files" value="<%=request.getParameter("files")%>">
	<param name="call_cache" value="<%=request.getParameter("call_cache")%>">
	<param name="frame_width" value="500">
	<param name="frame_height" value="600">
</applet>
</div>

  </BODY>
   </HTML>

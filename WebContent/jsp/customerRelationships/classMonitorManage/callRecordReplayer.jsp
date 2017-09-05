<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>通话记录查询</title>

</head>
<body>
<table>
<tr>
<td>
<div id="replayer" >
		<object id="omplayer" width="460" height="50"
       classid="CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6"
       codebase="http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#Version=5,1,52,701"
       border="0"
       standby="Loading Microsoft Windows Media Player components..."
       type="application/x-oleobject" name="MediaPlayer">
		<param name="URL" value="${url}"/>
		<param name="rate" value="1"/>
		<param name="balance" value="0"/>
		<param name="currentPosition" value="0"/>
		<param name="defaultFrame" value=""/>
		<param name="playCount" value="1"/>
		<param name="autoStart" value="-1"/>
		<param name="currentMarker" value="0"/>
		<param name="invokeURLs" value="-1"/>
		<param name="baseURL" value=""/>
		<param name="volume" value="80"/>
		<param name="mute" value="0"/>
		<param name="uiMode" value="full"/>
		<param name="stretchToFit" value="-1"/>
		<param name="windowlessVideo" value="0"/>
		<param name="enabled" value="-1"/>
		<param name="enableContextMenu" value="0"/>
		<param name="fullScreen" value="0"/>
		<param name="SAMIStyle" value=""/>
		<param name="SAMILang" value=""/>
		<param name="SAMIFilename" value=""/>
		<param name="captioningID" value=""/>
		<param name="enableErrorDialogs" value="0"/>
		<param name="_cx" value="7223"/>
		<param name="_cy" value="1693"/>       
      </object>
	</div>
</td>
</tr>
</table>

	
<CENTER>
<input name="addBtn" type="button" class="normal_btn"  value="返回" onclick="history.back();" />
<a style="cursor: pointer;text-decoration: none;size: 20px;" href="${url}"   >下载</a>
</CENTER>

</body>
</html>
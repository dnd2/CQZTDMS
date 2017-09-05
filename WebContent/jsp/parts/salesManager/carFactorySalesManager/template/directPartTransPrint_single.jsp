<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-barcode-2.0.2.min.js"></script>
<html>

<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8">
<style media=print>   
.Noprint{display:none;}

.p_next {page-break-after: always;}  
</style>
<style>
<!--table
	{mso-displayed-decimal-separator:"\.";
	mso-displayed-thousand-separator:"\,";}
@page
	{margin:1.0in .75in 1.0in .75in;
	mso-header-margin:.5in;
	mso-footer-margin:.5in;}
.style0
	{mso-number-format:General;
	text-align:general;
	vertical-align:bottom;
	white-space:nowrap;
	mso-rotate:0;
	mso-background-source:auto;
	mso-pattern:auto;
	color:black;
	font-size:12.0pt;
	font-weight:400;
	font-style:normal;
	text-decoration:none;
	font-family:ËÎÌå, sans-serif;
	mso-font-charset:134;
	border:none;
	mso-protection:locked visible;
	mso-style-name:ÆÕÍ¨;
	mso-style-id:0;}
td
	{mso-style-parent:style0;
	padding-top:1px;
	padding-right:1px;
	padding-left:1px;
	mso-ignore:padding;
	color:black;
	font-size:12.0pt;
	font-weight:400;
	font-style:normal;
	text-decoration:none;
	font-family:ËÎÌå, sans-serif;
	mso-font-charset:134;
	mso-number-format:General;
	text-align:general;
	vertical-align:bottom;
	border:none;
	mso-background-source:auto;
	mso-pattern:auto;
	mso-protection:locked visible;
	white-space:nowrap;
	border: 1px solid black;
	mso-rotate:0;}
.xl65
	{mso-style-parent:style0;
	text-align:center;
	vertical-align:middle;}
.xl66
	{mso-style-parent:style0;
	text-align:left;
	vertical-align:middle;}
ruby
	{ruby-align:left;}
rt
	{color:windowtext;
	font-size:9.0pt;
	font-weight:400;
	font-style:normal;
	text-decoration:none;
	font-family:ËÎÌå, monospace;
	mso-font-charset:134;
	mso-char-type:none;
	display:none;}
-->
</style>
</head>
<script language="javascript">
jQuery.noConflict();
	function doInit(){
		
	}
	function getBarCode1(pkgNo){
		jQuery('#pkgNo_'+pkgNo).barcode(pkgNo,'code11',{barWidth:2, barHeight:40});
	}
	
</script>
<body link=blue vlink=purple class=xl65 onload="doInit()">

<div>
<table border=0 cellpadding=0 cellspacing=0 style='width:450pt; height: 80mm;'>
  <tr height="20mm">
    <td colspan="2">
    <%-- <%=request.getParameter("pkgNo")%> --%>
    <div id="pkgNo_${param.pkgNo}"></div>
	  <script type="text/javascript">
	  getBarCode1('${param.pkgNo}');
	  </script>
    </td>
  </tr>
  <tr height="16mm">
    <td width="30%" style="font-size: 25px; font-weight: bold; line-height: 16mm;"><%=request.getParameter("dealerCode")%>&nbsp;</td>
    <td style="font-size: 25px; font-weight: bold; line-height: 16mm;"><%=request.getParameter("pkgNo")%></td>
  </tr>
  <tr height="20mm">
    <td colspan="2" style="font-size: 30px; font-weight: bold; line-height: 20mm;"><%=request.getParameter("dealerName")%></td>
  </tr>
  <tr height="12mm">
    <td style="font-size: 20px; font-weight: bold; line-height: 12mm;"><%=request.getParameter("recName")%>&nbsp;</td>
    <td rowspan="2" style="font-size: 20px; font-weight: bold; line-height: 24mm;"><%=request.getParameter("recAddress")%>&nbsp;</td>
  </tr>
  <tr height="12mm">
    <td style="font-size: 20px; font-weight: bold; line-height: 12mm;"><%=request.getParameter("telPhone")%>&nbsp;</td>
  </tr>
</table>

<table width=100% height=76pt >
	<tr>
		<td  style="border: none;">
			&nbsp;
		</td>
	</tr>
</table>
</div>

</html>

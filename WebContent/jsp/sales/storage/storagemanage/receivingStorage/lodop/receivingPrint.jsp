<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrGatherBalancePO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.po.TmDealerPO"%><html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>入库打印</title>
<script language="javascript" src="LodopFuncs.js"></script> 
<object  id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>  
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed> 
</object> 
</head>
<body onload="printInit();">
<center>
<div id="divObj">
	<table border="0" width="100%">
	 <tr>
	<td align="center"><strong><font size="6">{valueMap.AREA_NAME }-${valueMap.ROAD_NAME }-${valueMap.SIT_NAME } * ${valueMap.PER_CODE }</font></strong></td>
	</tr>
	<tr>
	<td align="center"><strong><font size="6">${valueMap.VIN }</font></strong><input type="hidden" id="vin" value="${valueMap.VIN }"/></td>
	</tr>
	</table>
</div>
</center>
<!--@autor:ranj  cqranpok@163.com  2013-08-18-->
<script language="javascript" type="text/javascript">  
    var LODOP; //声明为全局变量  
	function printInit() {	
		LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));  //初始化LODOP
		try
		{
			sitCode();//打印库位码
			vinPrint();//打印车架号（VIN）
		}catch(e){}
		window.opener = null;
		window.close();
	}
	//车架号打印（打印一维码）
	function vinPrint() {
		var vin=document.getElementById("vin").value;//获取VIN
		LODOP.PRINT_INIT("");
		LODOP.SET_PRINTER_INDEX(-1);//指定打印机
		LODOP.PRINT_INITA(0,0,300,150,"");
		LODOP.SET_PRINT_PAGESIZE(1,0,0,"A4");//指定方向和纸张
		LODOP.SET_PRINT_COPIES(2);//指定份数
		LODOP.ADD_PRINT_BARCODE(28,34,400,60,"128A",vin);//一维码类型128A
		LODOP.SET_PRINT_STYLEA(0,"FontSize",15);//设置样式
		LODOP.PRINT();	
	}
	//库位码打印（库位码+接车员+vin）
	function sitCode(){
		//var strBodyStyle="<style>table{ border: 0 solid #000000;border-collapse:collapse}</style>";
		var strFormHtml="<body><center>"+document.getElementById("divObj").innerHTML+"</center></body>";
		LODOP.PRINT_INIT("");
		LODOP.SET_PRINTER_INDEX(-1);//指定打印机
		LODOP.SET_PRINT_PAGESIZE(1,0,0,"A4");//指定方向和纸张
		LODOP.SET_PRINT_COPIES(2);//指定份数  
		LODOP.ADD_PRINT_HTM(5,5,"100%","100%",strFormHtml);
		LODOP.PRINT();	
	}
</script> 
</body>
</html>
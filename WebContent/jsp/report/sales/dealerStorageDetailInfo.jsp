<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<%
	String contextPath = request.getContextPath(); 
	
	

 %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>经销商库存明细报表</title>



<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/style/calendar.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/style/page-info.css" rel="stylesheet" type="text/css" />
<style type="text/css"> 
body,table, td, a { 
font:9pt; 
} 

/*固定行头样式*/
.scrollRowThead 
{
     position: relative; 
     left: expression(this.parentElement.parentElement.parentElement.scrollLeft);
     z-index:0;
}

/*固定表头样式*/
.scrollColThead {
     top: expression(this.parentElement.parentElement.parentElement.scrollTop);
     z-index:2;
}

/*行列交叉的地方*/
.scrollCR {
     z-index:3;
} 
 
/*div外框*/
.scrollDiv {
height:480px;
clear: both; 
border: 1px solid #EEEEEE;
OVERFLOW: scroll;
width: 100%; 
}

/*行头居中*/
.scrollColThead td,.scrollColThead th
{
     text-align: center ;
}

/*行头列头背景*/
.scrollRowThead,.scrollColThead td,.scrollColThead th
{
background-color:EEEEEE;
}

/*表格的线*/
.scrolltable
{
border-bottom:1px solid #CCCCCC; 
border-right:1px solid #CCCCCC; 
}

/*单元格的线等*/
.scrolltable td,.scrollTable th
{
     border-left: 1px solid #CCCCCC; 
     border-top: 1px solid #CCCCCC; 
     padding: 5px; 
     white-space: nowrap;
     text-align: center;
}
.juzuo td
{
line-height: 1px solid #CCCCCC;
height: 1px solid #CCCCCC;
}
</style> 
</head>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>经销商库存明细查询&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">
	<tr class="scrollColThead">
		<th colspan="12" align="center">昌河汽车产品库龄统计表</th>
	</tr>
	<tr class="scrollColThead">
		<th>大区</th>
		<th>车型</th>
		<th>配置</th>
		<th>省份	</th>
   <!-- <th>发票代码</th> -->
   <!-- <th>站点代码</th> -->  
		<th>分销商</th>
		<th>车系</th>
		<th>底盘号</th>
		<th>生产日期</th>
		<th>一级商家	</th>
   <!-- <th>开票日期</th> -->
   		<th>库存类别</th>
   		<th>库存时间</th>
   		<th>类别</th>
	<!--<th>分类</th>  -->
	</tr>
	<c:forEach items="${result }" var="po">
		<tr>
			<Td>${po.ORG_NAME }</Td>
			<Td>${po.MODEL_NAME }</Td>
			<Td>${po.PACKAGE_NAME }</Td>
			<Td>${po.REGION_NAME }</Td>
    		<!-- <td></td>  -->
    		<!-- <td></td>  -->
			<Td>${po.DEALER_NAME }</Td>
			<Td>${po.SERIES_NAME }</Td>
			<Td>${po.VIN }</Td>
			<Td>${po.PRODUCT_DATE }</Td>
			<Td>${po.P_DEALER_NAME }</Td>
			<!-- <TD></TD>  -->
			<Td>${po.CODE_DESC }</Td>
			<Td>${po.STORAGE_AGE }</Td>
			<Td>${po.VEHICLE_KIND }</Td>
			<!-- <Td></td> -->
		</tr>
	</c:forEach>
</table>
</div>
<br>
<table border="0" align="center" class="table_list">
	<tr><td>
		<input name="button2" type=button class="cssbutton" onClick="window.close();" value="关闭">
	</td></tr>
</table>
</body>
</html>

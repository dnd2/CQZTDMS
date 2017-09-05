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
<title>库龄报表</title>



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
	<strong>库龄报表查询&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">
	<tr class="scrollColThead">
		<th colspan="12" align="center">昌河汽车产品库龄统计表</th>
	</tr>
	<tr class="scrollColThead">
		<th rowspan="2" >类别</th>
		<th rowspan="2">车种</th>
		<th colspan="4">经销商库存</th>
		<th colspan="4">企业库存</th>
		<th rowspan="2">库存总计</th>
	</tr>
	<tr class="scrollColThead">
		<th>3个月以内库存</th>
		<th>3-6个月库存</th>
		<th>6个月以上库存</th>
		<th>合计</th>
		<th>3个月以内库存</th>
		<th>3-6个月库存</th>
		<th>6个月以上库存</th>
		<th>合计</th>	
	</tr>
	<c:forEach items="${result }" var="po">
		<c:if test="${po.YIELDLY != null && po.GROUP_NAME != null  }">
			<tr>
				<td>${po.YIELDLY }</td>
				<td>${po.GROUP_NAME }</td>
				<td>
				<c:if test="${po.STORAGE_1==0}">
					${po.STORAGE_1 }
				</c:if>
				<c:if test="${po.STORAGE_1!=0}">
					<a href="#"  onclick="showMsg('${po.YIELDLY }','${po.GROUP_ID }','JXSKC',1);" title="查看明细">${po.STORAGE_1 }</a>
				</c:if>
				</td>
				<td>
				<c:if test="${po.STORAGE_2==0}">
					${po.STORAGE_2 }
				</c:if>
				<c:if test="${po.STORAGE_2!=0}">
					<a href="#"  onclick="showMsg('${po.YIELDLY }','${po.GROUP_ID }','JXSKC',2);" title="查看明细">${po.STORAGE_2 }</a>
				</c:if>
				</td>
				<td>
				<c:if test="${po.STORAGE_3==0}">
					${po.STORAGE_3 }
				</c:if>
				<c:if test="${po.STORAGE_3!=0}">
					<a href="#"  onclick="showMsg('${po.YIELDLY }','${po.GROUP_ID }','JXSKC',3);" title="查看明细">${po.STORAGE_3 }</a>
				</c:if>
				</td>
				<td>${po.TOTAL_STORAGE }</td>
				<td>
				<c:if test="${po.OEM_STORAGE_1==0}">
					${po.OEM_STORAGE_1 }
				</c:if>
				<c:if test="${po.OEM_STORAGE_1!=0}">
					<a href="#"  onclick="showMsg('${po.YIELDLY }','${po.GROUP_ID }','QYKC',1);" title="查看明细">${po.OEM_STORAGE_1 }</a>
				</c:if>
				</td>
				<td>
				<c:if test="${po.OEM_STORAGE_2==0}">
					${po.OEM_STORAGE_2 }
				</c:if>
				<c:if test="${po.OEM_STORAGE_2!=0}">
					<a href="#"  onclick="showMsg('${po.YIELDLY }','${po.GROUP_ID }','QYKC',2);" title="查看明细">${po.OEM_STORAGE_2 }</a>
				</c:if>
				</td>
				<td>
				<c:if test="${po.OEM_STORAGE_3==0}">
					${po.OEM_STORAGE_3 }
				</c:if>
				<c:if test="${po.OEM_STORAGE_3!=0}">
					<a href="#"  onclick="showMsg('${po.YIELDLY }','${po.GROUP_ID }','QYKC',3);" title="查看明细">${po.OEM_STORAGE_3 }</a>
				</c:if>
				</td>
				<td>${po.TOTAL_OEM_STORAGE }</td>
				<td>${po.TOTAL }</td>
		     </tr>
		</c:if>
		<c:if test="${po.YIELDLY != null && po.GROUP_NAME == null  }">
			<tr>
				<td>${po.YIELDLY }合计</td>
				<td>&nbsp;</td>
				<td>${po.STORAGE_1 }</td>
				<td>${po.STORAGE_2 }</td>
				<td>${po.STORAGE_3 }</td>
				<td>${po.TOTAL_STORAGE }</td>
				<td>${po.OEM_STORAGE_1 }</td>
				<td>${po.OEM_STORAGE_2 }</td>
				<td>${po.OEM_STORAGE_3 }</td>
				<td>${po.TOTAL_OEM_STORAGE }</td>
				<td>${po.TOTAL }</td>
		     </tr>
		</c:if>
		<c:if test="${po.YIELDLY == null  && po.GROUP_NAME == null }">
			<tr>
				<td>合&nbsp;计</td>
				<td>&nbsp;</td>
				<td>${po.STORAGE_1 }</td>
				<td>${po.STORAGE_2 }</td>
				<td>${po.STORAGE_3 }</td>
				<td>${po.TOTAL_STORAGE }</td>
				<td>${po.OEM_STORAGE_1 }</td>
				<td>${po.OEM_STORAGE_2 }</td>
				<td>${po.OEM_STORAGE_3 }</td>
				<td>${po.TOTAL_OEM_STORAGE }</td>
				<td>${po.TOTAL }</td>
			</tr>
		</c:if>
	</c:forEach>
	</table>
</div>
<br>
<table border="0" align="center" class="table_list">
	<tr><td>
		<input name="button2" type=button class="cssbutton" onClick="window.close();" value="关闭">
		<input type="hidden" name="reAreaId" id="reAreaId" value="${paramMap.areaId }">
		<input type="hidden" name="reDealerId" id="reDealerId" value="${paramMap.dealerId }">
		<input type="hidden" name="reHaveCon" id="reHaveCon"  value="${paramMap.haveCon }">
	</td></tr>
</table>
</body>
<script type="text/javascript">
//查看明细
//type:类别[无用];serName:车系;kcType:库存类型;daylen:库龄
function showMsg(type,serName,kcType,daylen){
	window.open("<%=request.getContextPath()%>/jsp/report/sales/storageAgeInfoMsg.jsp?type=-1&serName="+serName+"&kcType="+kcType+"&daylen="+daylen);
}

</script>
</html>

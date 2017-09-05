<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<% 
	String contextPath = request.getContextPath(); 
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集客量查询</title>
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
     left: expression(this.parentElement.parentElement.parentElement.parentElement.scrollLeft);
     z-index:0;
}

/*固定表头样式*/
.scrollColThead {
     position: relative; 
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
}
</style> 
</head>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>集客量查询&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">日期:</font>${startDate}&nbsp;至&nbsp;${endDate}</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">
	<tr class="scrollColThead">
		<th class="scrollRowThead scrollCR" nowrap>时间</th>
		<th class="scrollRowThead" nowrap>日期</th>
		<c:forEach items="${realList}" var="realList">
			<th>${realList.day }</th>
		</c:forEach>
		<th >&nbsp;</th> 
	</tr>
	<tr class="scrollColThead">
		<th class="scrollRowThead scrollCR">&nbsp;</th> 
		<th class="scrollRowThead" nowrap>星期</th>
		<c:forEach items="${realList}" var="realList">
			<th>${realList.week }</th>
		</c:forEach>
		<th nowrap>合计</th>
	</tr>
	<tr>
		<th colspan="2" class="scrollRowThead" nowrap>来电/店客户数</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.totalCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.totalCountT }</td>
	</tr>
	<tr>
		<th colspan="2" class="scrollRowThead" nowrap>留有信息客户数</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.saveInfoCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.saveInfoCountT }</td>
	</tr>
	<tr>
		<th colspan="2" class="scrollRowThead" nowrap>成交数</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.bargainCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.bargainCountT }</td>
	</tr>
	<tr>
		<th colspan="2" class="scrollRowThead" nowrap>首洽成交数</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.bargainSQCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.bargainSQCountT }</td>
	</tr>
	<tr>
		<th colspan="2" class="scrollRowThead" nowrap>跟踪成交数</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.bargainGZCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.bargainGZCountT }</td>
	</tr>
	<tr>
		<th colspan="2" class="scrollRowThead" nowrap>试驾成交数</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.bargainSJCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.bargainSJCountT }</td>
	</tr>
	<tr>
		<th rowspan="10" class="scrollRowThead" nowrap>信息渠道</th>
		<th class="scrollRowThead" nowrap>朋友推荐</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.infoDitPYTJCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.infoDitPYTJCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>展销会</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.infoDitZXHCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.infoDitZXHCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>店招吸引</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.infoDitDZXYCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.infoDitDZXYCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>报纸</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.infoDitBZCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.infoDitBZCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>电视</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.infoDitDSCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.infoDitDSCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>短信</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.infoDitDXCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.infoDitDXCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>广播</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.infoDitGBCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.infoDitGBCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>网络</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.infoDitWLCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.infoDitWLCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>户外广告</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.infoDitHWGGCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.infoDitHWGGCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>杂志</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.infoDitZZCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.infoDitZZCountT }</td>
	</tr>
	<tr>
		<th rowspan="10" class="scrollRowThead" nowrap>购买侧重点</th>
		<th class="scrollRowThead" nowrap>外观</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.buyPoiWGCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.buyPoiWGCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>动力性能</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.buyPoiDLXNCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.buyPoiDLXNCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>使用成本</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.buyPoiSYCBCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.buyPoiSYCBCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>售后方便</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.buyPoiSHFBCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.buyPoiSHFBCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>舒适性</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.buyPoiSSXCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.buyPoiSSXCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>安全性</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.buyPoiAQXCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.buyPoiAQXCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>操控性</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.buyPoiCKXCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.buyPoiCKXCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>价格</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.buyPoiJGCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.buyPoiJGCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>结实耐用</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.buyPoiJSNYCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.buyPoiJSNYCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>空间</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.buyPoiKJCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.buyPoiKJCountT }</td>
	</tr>
	<tr>
		<th rowspan="7" class="scrollRowThead" nowrap>放弃购买原因</th>
		<th class="scrollRowThead" nowrap>车的外观</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.desertReCDWGCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.desertReCDWGCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>油耗</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.desertReHYCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.desertReHYCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>产品质量</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.desertReCPZLCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.desertReCPZLCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>价格</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.desertReJGCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.desertReJGCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>空间</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.desertRePPCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.desertRePPCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>售后服务政策</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.desertReSHZCCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.desertReSHZCCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>考虑其他品牌</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.desertReQTPPCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.desertReQTPPCountT }</td>
	</tr>
	<tr>
		<th rowspan="3" class="scrollRowThead" nowrap>客户性质</th>
		<th class="scrollRowThead" nowrap>新购</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.cusNatXGCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.cusNatXGCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>换购</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.cusNatHGCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.cusNatHGCountT }</td>
	</tr>
	<tr>
		<th class="scrollRowThead" nowrap>增购</th>
		<c:forEach items="${realList}" var="realList">
			<td>${realList.cusNatZGCount }&nbsp;</td>
		</c:forEach>
		<td>${totalMap.cusNatZGCountT }</td>
	</tr>
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

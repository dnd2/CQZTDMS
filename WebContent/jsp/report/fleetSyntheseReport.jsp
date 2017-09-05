<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="/jstl/fmt" prefix="fmt"%>
<head>
<% 
	String contextPath = request.getContextPath(); 
	int tba = 0;
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团客户综合查询表</title>
<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/style/calendar.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/style/page-info.css" rel="stylesheet" type="text/css" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script>
	var row = 2;
	
	//浮动显示DIV
	var divElem;
	var infoContent;
	
	//显示提示信息
	var offsetX = 2; 
	var offsetY = 16; 
	var x = 0; 
	var y = 0; 
	var show = false;
	var tipStyle;
	
	function getTipInfo(obj) {
		var value = obj.innerHTML.replace(/[\r\n]/g,"");
		value = value.replace("&nbsp;","");
		value = value.trim();
		if(value == "") {
			return;
		} 
		divElem = document.getElementById("tipInfo");
		infoContent = document.getElementById('infoContent');
		tableElem = document.getElementById('dataTable');
		scrollElem = document.getElementById('scrollDiv');
		divElem.style.top = obj.parentNode.offsetTop + obj.clientHeight  - scrollElem.scrollTop + 25;
		divElem.style.left =  obj.parentNode.offsetLeft + obj.clientWidth - scrollElem.scrollLeft + -5;
		divElem.style.display = 'block';
		infoContent.innerHTML = value;
	}
	
	function removeTipInfo() {
		 if(divElem != undefined) {
			 infoContent.innerHTML = "";
			divElem.style.display = 'none';
		}
	}
</script>
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
     text-align: center;
}

.tipGrid {
	width: 60px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}
/* .tipInfo {width:180px; height:50px; position:absolute;margin:0px;padding:0px;display:block;z-index:10;background-color: #EEE;text-align: justify;} */


.tipInfo {
	position:absolute;
	font-size: 9pt;
	display:block;
	height:100px;
	width:200px;
	background-color:transparent;
	*border:1px solid #666;
}

/* s{
	position:absolute;
	top:20px;
	*top:22px;
	left:-20px;
	display:block;
	height:0;
	width:0;
	font-size: 0; 
	line-height: 0;
	border-color:transparent transparent #666 transparent;
	border-style:dashed solid dashed dashed;
	border-width:10px;
}
i{position:absolute;
	top:9px;
	*top:9px;
	left:10px;
	display:block;
	height:0;
	width:0;
	font-size: 0;
	line-height: 0;
	border-color:transparent transparent #fff transparent;
	border-style:dashed solid dashed dashed;
	border-width:10px;
} */

.infoContent{
	border:1px solid #666;
	-moz-border-radius:5px;
	-webkit-border-radius:5px;
	border-radius : 10px;
	position:absolute;
	background-color:#fff;
	width:100%;
	height:100%;
	padding:5px;
	*top:-2px;
	*border-top:1px solid #666;
	*border-top:1px solid #666;
	*border-left:none;
	*border-right:none;
	*height:102px;
	 box-shadow: 3px 3px 4px #999;
	-moz-box-shadow: 3px 3px 4px #999;
	-webkit-box-shadow: 3px 3px 4px #999;
	/* For IE 5.5 - 7 */
	filter: progid:DXImageTransform.Microsoft.Shadow(Strength=4, Direction=135, Color='#999999'); 
	/* For IE 8 */
	-ms-filter: "progid:DXImageTransform.Microsoft.Shadow(Strength=4, Direction=135, Color='#999999')"; 
}

</style> 
</head>
<body>

<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>集团客户综合查询&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
</div>
<div id="scrollDiv" class="scrollDiv" > 
<table id="dataTable" border="0" cellpadding="3" cellspacing="0" width="100%" class="scrollTable">

	<tr class="scrollColThead">
		<th colspan="28" align="center">集团客户综合查询表</th>
	</tr>
	<%-- <tr class="scrollColThead">
		<td colspan="7" align="left"><div align="left">起止时间：${startTime}--${endTime}</div></td>
	</tr> --%>
	<tr class="scrollColThead">
		<th class="scrollRowThead scrollCR" nowrap>分销中心</th>
		<th class="scrollRowThead scrollCR" nowrap>省份</th>
		<th class="scrollRowThead scrollCR" nowrap>提报日期</th>
		<th class="scrollRowThead scrollCR" nowrap>提报单位</th>
		<th class="scrollRowThead scrollCR" nowrap>客户名称</th>
		<th class="scrollRowThead scrollCR" nowrap>客户类型</th>
		<!-- <th class="scrollRowThead scrollCR" nowrap>主营业务</th> -->
		<th class="scrollRowThead scrollCR" nowrap>邮编</th>
		<!-- <th class="scrollRowThead scrollCR" nowrap>区域</th> -->
		<th class="scrollRowThead scrollCR" nowrap>详细地址</th>
		<th class="scrollRowThead scrollCR" nowrap>主联系人</th>
		<th class="scrollRowThead scrollCR" nowrap>职务</th>
		<th class="scrollRowThead scrollCR" nowrap>电话</th>
		<!-- <th class="scrollRowThead scrollCR" nowrap>车系</th> -->
		<th class="scrollRowThead scrollCR" nowrap>报备数量</th>
		<!-- <th class="scrollRowThead scrollCR" nowrap>备注</th> -->
		<!-- <th class="scrollRowThead scrollCR" nowrap>确认状态</th> -->
		<!-- <th class="scrollRowThead scrollCR" nowrap>确认说明</th> -->
		<th class="scrollRowThead scrollCR" nowrap>确认人</th>
		<th class="scrollRowThead scrollCR" nowrap>确认时间</th>
		<th class="scrollRowThead scrollCR" nowrap>跟进时间</th>
		<th class="scrollRowThead scrollCR" nowrap>跟进内容</th>
		<th class="scrollRowThead scrollCR" nowrap>签约日期</th>
		<th class="scrollRowThead scrollCR" nowrap>有效期起</th>
		<th class="scrollRowThead scrollCR" nowrap>有效期止</th>
		<!-- <th class="scrollRowThead scrollCR" nowrap>签约车系</th> -->
		<th class="scrollRowThead scrollCR" nowrap>合同数量</th>
		<th class="scrollRowThead scrollCR" nowrap>实销数量</th>
	</tr>
	
	<c:forEach items="${list_report}" var="item">
		<tr>
			<td nowrap="nowrap" align="center">
				<c:if test="${item.ROOT_ORG_NAME != null}">
					${item.ROOT_ORG_NAME}
				</c:if>
				<c:if test="${item.ROOT_ORG_NAME == null}">
					&nbsp;
				</c:if>
			</td>
			<td nowrap="nowrap" align="center">
				<c:if test="${item.PROVINCE != null}">
					${item.PROVINCE}
				</c:if>
				<c:if test="${item.PROVINCE == null}">
					&nbsp;
				</c:if>
			</td>
			<td nowrap="nowrap" align="center">
				<c:if test="${item.SUBMIT_DATE != null}">
					<fmt:formatDate value="${item.SUBMIT_DATE}" pattern="yyyy-MM-dd" />
				</c:if>
				<c:if test="${item.SUBMIT_DATE == null}">
					&nbsp;
				</c:if>
			</td>
			<td nowrap="nowrap" align="center">
				<div class="tipGrid" onmousemove="getTipInfo(this);" onmouseout="removeTipInfo()"> 
					<c:if test="${item.DEALER_NAME != null}">
						${item.DEALER_NAME}
					</c:if>
					<c:if test="${item.DEALER_NAME == null}">
						&nbsp;
					</c:if>
				</div>
			</td>
			<td nowrap="nowrap" align="center">
				<div class="tipGrid" onmousemove="getTipInfo(this);" onmouseout="removeTipInfo()">
					<c:if test="${item.FLEET_NAME != null}">
						${item.FLEET_NAME}
					</c:if>
					<c:if test="${item.FLEET_NAME == null}">
						&nbsp;
					</c:if>
				</div>
			</td>
			<td nowrap="nowrap" align="center">
				<c:if test="${item.FLEET_TYPE != null}">
					${item.FLEET_TYPE}
					<!-- <script>
						var value = getItemValue(${item.FLEET_TYPE});
						if("" == value) {
							value = '&nbsp;';
						}
						document.getElementById('dataTable').rows[row++].cells[5].innerHTML = value;
					</script> -->
				</c:if>
				<c:if test="${item.FLEET_TYPE == null}">&nbsp;</c:if>
			</td>
			<%-- <td nowrap="nowrap" align="center">
				<c:if test="${item.MAIN_BUSINESS != null}">
					<script>
						var value = getItemValue(${item.MAIN_BUSINESS});
						if("" == value) {
							value = '&nbsp';
						}
						document.getElementById('dataTable').rows[row].cells[6].innerHTML = value;
					</script>
				</c:if>
				<c:if test="${item.MAIN_BUSINESS == null}">&nbsp;</c:if>
			</td> --%>
			<td nowrap="nowrap" align="center">
				<c:if test="${item.ZIP_CODE != null}">${item.ZIP_CODE}</c:if>
				<c:if test="${item.ZIP_CODE == null}">&nbsp;</c:if>
			</td>
			<%-- <td nowrap="nowrap">
				<c:if test="${item.REGION_NAME != null}">${item.REGION_NAME}</c:if>
				<c:if test="${item.REGION_NAME == null}">&nbsp;</c:if>
			</td> --%>
			<td nowrap="nowrap" align="center">
				<div class="tipGrid" onmousemove="getTipInfo(this);" onmouseout="removeTipInfo()">
					<c:if test="${item.ADDRESS != null}">${item.ADDRESS}</c:if>
					<c:if test="${item.ADDRESS == null}">&nbsp;</c:if>
				</div>
			</td>
			<td nowrap="nowrap" align="center">
				<c:if test="${item.MAIN_LINKMAN != null}">${item.MAIN_LINKMAN}</c:if>
				<c:if test="${item.MAIN_LINKMAN == null}">&nbsp;</c:if>
			</td>
			<td nowrap="nowrap" align="center">
				<c:if test="${item.MAIN_JOB != null}">${item.MAIN_JOB}</c:if>
				<c:if test="${item.MAIN_JOB == null}">&nbsp;</c:if>
			</td>
			<td nowrap="nowrap" align="center">
				<c:if test="${item.MAIN_PHONE != null}">${item.MAIN_PHONE}</c:if>
				<c:if test="${item.MAIN_PHONE == null}">&nbsp;</c:if>
			</td>
			<%-- <td nowrap="nowrap" align="center">
				<c:if test="${item.SERIES_ID == -1}">
					全系
				</c:if>
				<c:if test="${item.CARS != null}">
					${item.CARS}
				</c:if>
				<c:if test="${item.CARS == null}">&nbsp;</c:if>
			</td> --%>
			<td nowrap="nowrap" align="center">
				<c:if test="${item.SERIES_COUNT != null}">${item.SERIES_COUNT}</c:if>
				<c:if test="${item.SERIES_COUNT == null}">&nbsp;</c:if>
			</td>
			<%-- <td nowrap="nowrap" align="center">
				<div class="tipGrid" onmousemove="getTipInfo(this);" onmouseout="removeTipInfo()">
					<c:if test="${item.FOLLOW_REMARK != null}">${item.FOLLOW_REMARK}</c:if>
					<c:if test="${item.FOLLOW_REMARK == null}">&nbsp;</c:if>
				</div>
			</td> --%>
		<%-- <td nowrap="nowrap" align="center">
				<c:if test="${item.STATUS != null}">
					<script>
						var value = getItemValue(${item.STATUS});
						if("" == value) {
							value = '&nbsp';
						} 
						document.getElementById('dataTable').rows[row++].cells[16].innerHTML = value;
					</script>
				</c:if>
				<c:if test="${item.STATUS == null}">&nbsp;</c:if>
			</td> --%>
			<%-- <td nowrap="nowrap" align="center">
				<div class="tipGrid" onmousemove="getTipInfo(this);" onmouseout="removeTipInfo()">
					<c:if test="${item.REQ_REMARK != null}">${item.REQ_REMARK}</c:if>
					<c:if test="${item.REQ_REMARK == null}">&nbsp;</c:if>
				</div>
			</td> --%>
			<td nowrap="nowrap" align="center">
				<c:if test="${item.NAME != null}">${item.NAME}</c:if>
				<c:if test="${item.NAME == null}">&nbsp;</c:if>
			</td>
			<td nowrap="nowrap" align="center">
				<c:if test="${item.AUDIT_DATE != null}">
					<fmt:formatDate value="${item.AUDIT_DATE}" pattern="yyyy-MM-dd" />
				</c:if>
				<c:if test="${item.AUDIT_DATE == null}">&nbsp;</c:if>
			</td>
			<td nowrap="nowrap" align="center">
				<c:if test="${item.FOLLOW_DATE != null}">
					<fmt:formatDate value="${item.FOLLOW_DATE}" pattern="yyyy-MM-dd" />
				</c:if>
				<c:if test="${item.FOLLOW_DATE == null}">&nbsp;</c:if>
			</td>
			<td nowrap="nowrap" align="center">
				<div class="tipGrid" onmousemove="getTipInfo(this);" onmouseout="removeTipInfo()">
					<c:if test="${item.FOLLOW_REMARK != null}">${item.FOLLOW_REMARK}</c:if>
					<c:if test="${item.FOLLOW_REMARK == null}">&nbsp;</c:if>
				</div>
			</td>
			<td nowrap="nowrap" align="center">
				<c:if test="${item.CHECK_DATE != null}">
					<fmt:formatDate value="${item.CHECK_DATE}" pattern="yyyy-MM-dd" />
				</c:if>
				<c:if test="${item.CHECK_DATE == null}">&nbsp;</c:if>
			</td>
			<td nowrap="nowrap" align="center">
				<c:if test="${item.START_DATE != null}">
					<fmt:formatDate value="${item.START_DATE}" pattern="yyyy-MM-dd" />
				</c:if>
				<c:if test="${item.START_DATE == null}">&nbsp;</c:if>
			</td>
			<td nowrap="nowrap" align="center">
				<c:if test="${item.END_DATE != null}">
					<fmt:formatDate value="${item.END_DATE}" pattern="yyyy-MM-dd" />
				</c:if>
				<c:if test="${item.END_DATE == null}">&nbsp;</c:if>
			</td>
			<%-- <td nowrap="nowrap" align="center">
				<c:if test="${item.GROUP_NAME != null}">
					${item.GROUP_NAME}
				</c:if>
				<c:if test="${item.GROUP_NAME == null}">&nbsp;</c:if>
			</td> --%>
			<td nowrap="nowrap" align="center">
				<c:if test="${item.INTENT_COUNT != null}">${item.INTENT_COUNT}</c:if>
				<c:if test="${item.INTENT_COUNT == null}">&nbsp;</c:if>
			</td>
			<td nowrap="nowrap" align="center">
				<c:if test="${item.ACTCOUNT != null}">${item.ACTCOUNT}</c:if>
				<c:if test="${item.ACTCOUNT == null}">&nbsp;</c:if>
			</td>
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
<div id="tipInfo" class="tipInfo" style="display: none;">
	<div id="infoContent" class="infoContent"></div>
</div>
</body>
</html>

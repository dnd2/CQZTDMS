<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<% String contextPath = request.getContextPath(); %>
<head>
	<title>打印拣配单</title>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<style media=print>
.Noprint {
	display: none;
}

.p_next {
	page-break-after: always;
}
</style>
<style type="text/css">
.tableHead{
	border-collapse: collapse;
	border-spacing:0px;
}
.tableHead td{
	height: 10mm;
	line-height: 10mm;
         /*border: 1px dashed black;*/
}
.tableBody{
	border-collapse: collapse;
	border-spacing:0px;
}
.tableBody td{
	white-space:nowrap;
	word-break:nowrap;
	height: 8mm; 
	line-height: 8mm;
         /*border: 1px dashed black;*/
}
#mian_div {
	width: 241mm;
	height: 280mm;
	margin: 0 auto;
	padding: 10mm 15mm 10mm 15mm;
	border: 0px solid blue;
}
#cen_mid_div {
	width: 100%;
	height: 100%;
	border: 0px solid red;
}

#checkMsgDiv0 {
	display: none;
}

#top_cen_mid_div {
	width: 100%;
	height: 54px;
	margin: 0px;
	text-align: center;
    position:relative
}
#top_cen_mid_div span {
    position:absolute;
    bottom:0px;
    padding:0px;
    margin:0px;
    text-align: center;
}
#but_cen_mid_div {
	width: 100%;
	height: 270mm;
	margin: 0px;
	text-align: center;
	line-height: 10mm;
}
.my_css {
    overflow:hidden;
    word-break:keep-all;
    white-space:nowrap;
    text-overflow:ellipsis;
    width:800px;
    line-height: 20px;
    font-size:14px;
}

table {
    border-collapse: collapse;
    border-spacing: 0;
    border-left: 1px dashed #888;
    border-top: 1px dashed #888;
    /*background: #efefef;*/
}

th, td {
    border-right: 1px dashed #888;
    border-bottom: 1px dashed #888;
    /*padding: 5px 15px;*/
}

th {
    font-weight: bold;
    /*background: #ccc;*/
}

.tableBottom td {
    border-right: 0px;
    border-bottom: 0px;
}
body, td {font-size: 16px}
</style>
<script language="javascript">
var idx = 0;
function getIndex() {
    idx += 1
    document.write(idx);
}
function printOrder() {
	document.all.WebBrowser.ExecWB(6,1);//打印
}
function printpreview() {
	document.all.WebBrowser.ExecWB(7,1);// 打印页面预览
}
</script>
</head>
	<body style="margin: 0px;padding: 0px;text-align: center;font-size:16px;color:#000">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<OBJECT id=WebBrowser classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 style="display: none"> </OBJECT>
			<input name="pickOrderId" id="pickOrderId" value="${dataMap.pickOrderId}" type="hidden" />
			<div id="fot_div">
				<TABLE border=0 cellpadding=0 cellspacing=0 class="Noprint page-print-buttons"
					align="center" width=100%>
					<tr style="border: 0px;">
						<td style="border: 0px;">
							<input type=button id="printBtn" class="txtToolBarButton"
								value="打印">
							<input type=button id="printBtn2" class="txtToolBarButton"
								value="打印预览">
						</td>
					</tr>
				</TABLE>
			</div>
			<c:forEach items="${dataMap.detailList}" var="data" varStatus="countter">
            <c:choose>
            <c:when test="${!(countter.last==true)}">
                <div id="mian_div" name="thisblock" class="p_next">
            </c:when>
            <c:otherwise>
                <div id="mian_div" name="thisblock">
            </c:otherwise>
            </c:choose>
					<div id="cen_mid_div">
						<div id="top_cen_mid_div">
							<img src="<%=request.getContextPath()%>/img/bq_log1.gif" style="float: left;height: 55px;width: 250px;margin-bottom:5px" />
                            <span style="font-size:30px;margin-left:-120px;font-weight:bold">拣配单</span>
						</div>
						<div id="but_cen_mid_div">
							<table class="tableHead" width="100%">
								<tr>
									<td colspan="6" class="center" style="font-size: 18px;">拣配单抬头信息</td>
								</tr>
								<tr style="height: 10mm; line-height: 10mm;">
									<td>拣货单号</td>
									<td  class="center" style="font-size: 30px;">&nbsp;${dataMap.pickOrderId}</td>
                                    <td>订单号</td>
                                    <td  class="center">&nbsp;${dataMap.orderCode}</td>
									<td>订单类型</td>
									<td>&nbsp;${dataMap.orderType}</td>
								</tr>
								<tr>
									<td>经销商代码</td> 
									<td>&nbsp;${dataMap.dealerCode}</td> 
									<td>经销商名称</td> 
									<td colspan="3" style="text-align:center;">&nbsp;${dataMap.dealerName}</td> 
									</tr>
									<tr><td>发货工厂</td> 
									<td style="text-align:center;">&nbsp;${dataMap.companyName}</td> 
									<td>收货地址</td> 
									<td colspan="3" style="width:380px; line-height:20px; text-overflow:ellipsis; white-space:nowrap; overflow:hidden;">&nbsp;${dataMap.addr}</td> 
								</tr>
								<tr>
									<td>拣货日期</td> 
									<td>&nbsp;${dataMap.date}</td> 
									<td>制表日期</td> 
									<td>&nbsp;${dataMap.createDate}</td> 
									<td>制表人</td> 
									<td>&nbsp;${dataMap.userName}</td> 
								</tr>
                                <tr>
                                    <td>备注</td>
                                    <td colspan="5" align="left" >
                                        <div class="my_css">&nbsp;${dataMap.remark}</div>
                                    </td>
                                </tr>
							</table>
							<table class="tableBody" width="100%">
								<tr>
									<td colspan="8" align="center" style="font-size: 18px; border-top: 0px;">
										配件拣配单细节信息
									</td>
								</tr>
								<tr>
									<td align="center" style="width: 35px;">序号</td>
									<td align="center" style="width: 100px;"><span>批次</span></td>
									<td align="center" style="width: 100px;"><span>货位</span></td>
									<td align="center" style="">配件编码</td>
									<td align="center" style="">配件名称</td>
									<td align="center" style="width: 50px;">单位</td>
<!-- 									<td align="center" style="width: 65px;">当前库存</td> -->
									<td align="center" style="width: 65px;">订货数量</td>
									<td align="center" style="width: 65px;">拣配数量</td>
<!-- 									<td align="center" style="width: 65px;">备注</td> -->
								</tr>
								<c:forEach items="${data}" var="data">
									<tr>
										<td>
											<script language="javascript">getIndex();</script>
										</td>
										<td>${data.BATCH_NO}</td>
										<td>${data.LOC}</td>
										<td align="left">${data.PART_OLDCODE}</td>
										<td align="left">${data.PART_CNAME}</td>
										<td>${data.UNIT}</td>
<!-- 										<td>${data.NORMAL_QTY_NOW} --%></td> -->
										<td>${data.SALES_QTY}</td>
										<td>${data.BOOKED_QTY}</td>
<!-- 										<td align="left">${data.REMARK}</td> -->
									</tr>
								</c:forEach>
								<c:if test="${countter.last}">
								<%
					                Object dtlListAct = request.getAttribute("dtlListAct");
					                int count = 0;
					                if (null != dtlListAct) {
					                    count = Integer.parseInt(dtlListAct.toString());
					                }
					                int fyNum = 20;
					                int cyclNum = fyNum - count;
					                for (int i = 0; i < cyclNum; i++) {
					            %>
									<tr>
										<td>&nbsp;</td> 
										<td>&nbsp;</td> 
										<td>&nbsp;</td> 
										<td>&nbsp;</td> 
										<td>&nbsp;</td> 
										<td>&nbsp;</td> 
										<td>&nbsp;</td> 
										<td>&nbsp;</td>
									</tr>
									<% } %>
								</c:if>
							</table>
                            <table class="tableBottom" width="100%" style="border-top: 0px;border-left: 0px">
                                <tr style="height: 10mm; line-height: 10mm;  text-align: right;">
                                    <td align="right" colspan="9">捡货员：...................</td>
                                </tr>
                                <tr style="height: 10mm; line-height: 10mm;  text-align: right;">
                                    <td align="right" colspan="9">核对员：...................</td>
                                </tr>
                            </table>
						</div>
					</div>
				</div>
			</c:forEach>
		</form>
	</body>
</html>
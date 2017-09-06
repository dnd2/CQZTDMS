<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
   String contextPath = request.getContextPath();
%>
<head>
<title>打印发运单</title>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
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
	height: 8mm; 
	line-height: 8mm;
			/*border: 1px solid black;*/
}
.tableBody{
 	border-collapse: collapse;
 	border-spacing:0px;
}
.tableBody td{
	white-space:nowrap;
	word-break:nowrap;
	height: 7mm; 
	line-height: 7mm;
			/*border: 1px solid black;*/
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
	margin: 0px;
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
body, td{font-size: 16px;color:#000}
</style>
</head>
	<script language="javascript">
    //获取选择框的值
    var idx = 0;
    function getIndex() {
        idx += 1
        document.write(idx);
    }
    function printOrder() {
    	MyConfirm("确定打印吗?",confirmResult);
    }
    function confirmResult(){
    	document.all.WebBrowser.ExecWB(6,1);//打印
	}
</script>
	<body style="margin: 0px;padding: 0px;text-align: center;">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input name="pickOrderId" id="pickOrderId" value="${dataMap.pickOrderId}" type="hidden" />
			<div id="fot_div">
				<TABLE border=0 cellpadding=0 cellspacing=0 class="Noprint page-print-buttons center" width=100%>
					<tr style="border: 0px;">
						<td style="border: 0px;">
							<input type=button id="printBtn" class="txtToolBarButton" value="打印">
							<input type=button id="printBtn2" class="txtToolBarButton" value="预览">
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
                        <span style="font-size:30px;margin-left:-120px;font-weight:bold">配件发运单</span>
                    </div>
						<div id="but_cen_mid_div">
							<table class="tableHead" width="100%" style="text-align: left">
								<tr>
									<td colspan="6" align="center" style="font-size: 18px;">经销商商信息</td>
								</tr>
								<tr>
									<td align="left">发运单号</td>
									<td style="font-size: 30px; ">&nbsp;${dataMap.transCode}</td>
									<td >订单单号</td>
									<td >    &nbsp;${dataMap.orderCode}</td>
									<td >运单类型</td>
									<td >&nbsp;${dataMap.transType}</td>
								</tr>
								<tr>
									<td >经销商代码</td>
									<td >&nbsp;${dataMap.dealerCode}</td>
									<td >经销商名称</td>
									<td  colspan="3">&nbsp;${dataMap.dealerName}</td>
								</tr>	
								<tr>
									<td >填表日期</td>
									<td >&nbsp;${dataMap.createDate}</td>
									<td >发运日期</td>
									<td >&nbsp;${dataMap.driveDate}</td>
									<td >预计到货时间</td>
									<td >&nbsp;${dataMap.recvDate}</td>
								</tr>
								<tr>
									<td >收货地址</td>
									<td colspan="3" style="font-weight: normal;">&nbsp;${dataMap.addr}</td>
									<td >联系电话</td>
									<td >&nbsp;${dataMap.phone}</td>
								</tr>
								<tr>
									<td >发运地址</td>
									<td colspan="3" >&nbsp;${dataMap.companyName}</td>
									<td >联系人</td>
									<td >&nbsp;${dataMap.linkName}</td>
								</tr>
							</table>
							<table class="tableBody" width="100%">
								<tr>
									<td colspan="10" class="center" style="font-size: 18px; border-top: 0px;">运单基本信息</td>
								</tr>
								<tr style="text-align: center;">
									<td style="width: 10mm;" rowspan="2">序号</td>
									<td rowspan="2">箱号</td>
									<td colspan="3">规格（单位：cm）</td>
									<td style="width: 20mm;" rowspan="2">体积<br/>（m3）</td>
									<td style="width: 20mm;" rowspan="2">重量（KG）</td>
									<td style="width: 20mm;" rowspan="2">折合重量 （KG）</td>
									<td style="width: 20mm;" rowspan="2">计费重量 （KG）</td>
									<td style="width: 25mm;" rowspan="2">备注</td>
								</tr>
								<tr>
									<td>长</td>
									<td>宽</td>
									<td>高</td>
								</tr>
								<c:forEach items="${data}" var="data">
									<tr style="text-align: center;">
										<td><script language="javascript"> getIndex();</script></td>
										<td>&nbsp;${data.PKG_NO}</td>
										<td>&nbsp;${data.LENGTH}</td>
										<td>&nbsp;${data.WIDTH}</td>
										<td>&nbsp;${data.HEIGHT}</td>
										<td>&nbsp;${data.VOLUME}</td>
										<td>&nbsp;${data.WEIGHT}</td>
										<td>&nbsp;${data.EQ_WEIGHT}</td>
										<td>&nbsp;${data.CH_WEIGHT}</td>
										<td>&nbsp;${data.REMARK}</td>
									</tr>
								</c:forEach>
								<c:if test="${countter.last}">
									<%
							            Object dtlListAct = request.getAttribute("dtlListAct");
							            int count = 0;
							            if (null != dtlListAct) {
							                count = Integer.parseInt(dtlListAct.toString());
							            }
							            int fyNum = 15; //
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
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									<% } %>
								</c:if>
								<tr style="height: 12mm; line-height: 12mm;font-weight: bold;">
									<td colspan="2">第${countter.count}页/共${listAcount}页</td>
									<td>合计</td>
									<td colspan="2">${dataMap.boxNum}箱</td>
									<td>${dataMap.volume}m³</td>
									<td>${dataMap.weight}KG</td>
									<td>${dataMap.eqWeight}KG</td>
									<td>${dataMap.chWeight}KG</td>
									<td>&nbsp;</td>
								</tr>
							</table>
							<table class="tableBody" width="100%" height="30mm">
								<tr
									style="height: 5mm; font-weight: normal; text-align: center;">
									<td style="border-top: 0px;" colspan="8">
										《物流公司收货确认栏》
									</td>
								</tr>
								<tr style="height: 10mm; text-align: left;">
									<td colspan="4" width="60%">&nbsp;&nbsp;包装是否全部完好：（是）（否）</td>
									<td colspan="4">&nbsp;&nbsp;包装破损的箱号：</td>
								</tr>
								<tr style="height: 15mm; text-align: center;">
									<td width="5%">物流公司签字</td>
									<td width="30%" colspan="2">&nbsp;</td>
									<td width="8%">仓库方签字</td>
									<td width="30%" colspan="2">&nbsp;</td>
<!-- 									<td width="10%">北汽银翔整车物流签字</td> -->
<!-- 									<td width="20%">&nbsp;</td> -->
									<td width="10%">签字日期：</td>
									<td width="13%">&nbsp;</td>
								</tr>
							</table>
							<table class="tableBody" width="100%" height="30mm">
								<tr style="height: 5mm; font-weight: normal; text-align: center;">
									<td style="border-top: 0px;" colspan="6">
										《经销店收货确认栏》
									</td>
								</tr>
								<tr style="height: 10mm; text-align: left;">
									<td colspan="4" width="60%">
										&nbsp;&nbsp;包装是否全部完好：（是）（否）
									</td>
									<td colspan="2">
										&nbsp;&nbsp;包装破损的箱号：
									</td>
								</tr>
								<tr style="height: 15mm; text-align: center;">
									<td width="10%">经销店签字</td>
									<td width="13%">&nbsp;</td>
									<td width="10%">物流公司签字</td>
									<td width="10%">&nbsp;</td>
									<td width="30%" style="text-align: left;">&nbsp;签字日期： 年 月 日</td>
									<td width="13%">&nbsp;</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</c:forEach>
		</form>
	</body>
</html>

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>拣货单批量打印</title>
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
    .tableHead {
        border-collapse: collapse;
        border-spacing: 0px;
    }
    .tableHead td {
        height: 10mm;
        line-height: 10mm;
            /*border: 1px solid black;*/
    }
    .tableBody {
        border-collapse: collapse;
        border-spacing: 0px;
    }
    .tableBody td {
        white-space: nowrap;
        height: 8mm;
            /*line-height: 8mm;*/
            /*border: 1px solid black;*/
    }
    #mian_div {
        width: 241mm;
        height: 280mm;
        margin: 0 auto;
        padding: 10mm 15mm 10mm 15mm;
            /*border: 0px solid black;*/
   }
   #cen_mid_div {
       width: 100%;
       height: 100%;
       border: 0px solid red;
   }
   #top_cen_mid_div {
       width: 100%;
       height: 54px;
       margin: 0px;
       text-align: center;
       position: relative
   }
   #top_cen_mid_div span {
       position: absolute;
       bottom: 0px;
       padding: 0px;
       margin: 0px;
       text-align: center;
   }
  .my_css {
      overflow: hidden;
      word-break: keep-all;
      white-space: nowrap;
      text-overflow: ellipsis;
      width: 400px;
      line-height: 20px;
      font-size: 14px;
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
</style>
<script language="javascript">
var printStyle = "width:241mm;height:280mm;";
var printStyle4A4 = "width:210mm;height:297mm;";
var addrStyle = "text-align: left;width: 400px;line-height: 20px;text-overflow: ellipsis;overflow: hidden;";
var addrStyle4A4 = "text-align: left;width: 300px;line-height: 15px;text-overflow: ellipsis;overflow: hidden;";
var phoStyle = "text-align: left;width: 150px;line-height: 15px;text-overflow: ellipsis;overflow: hidden;";
var phoStyle4A4 = "text-align: left;width: 100px;line-height: 15px;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;";

function changeStyle(obj, style) {
    var mian_div = document.getElementsByName(obj);
    for (var i = 0; i < mian_div.length; i++) {
        mian_div[i].style.cssText = style;
    }
}
function changeStyleBatch() {
    changeStyle("mian_div", printStyle);
    changeStyle("addr", addrStyle);
    changeStyle("tel", phoStyle);
}
function changeStyleA4Batch() {
    changeStyle("mian_div", printStyle4A4);
    changeStyle("addr", addrStyle4A4);
    changeStyle("tel", phoStyle4A4);
}

function changeToA4() {
    changeStyle("mian_div", printStyle4A4);
    changeStyle("addr", addrStyle4A4);
    changeStyle("tel", phoStyle4A4);
    document.all.WebBrowser.ExecWB(6, 1);//打印
}
function printpreview() {
    changeStyle("mian_div", printStyle);
    changeStyle("addr", addrStyle);
    changeStyle("tel", phoStyle);
    document.all.WebBrowser.ExecWB(7, 1);// 打印页面预览
}
function printOrder() {
    changeStyle("mian_div", printStyle);
    changeStyle("addr", addrStyle);
    changeStyle("tel", phoStyle);
    document.all.WebBrowser.ExecWB(6, 1);//打印
}
function printpreviewToA4() {
    changeStyle("mian_div", printStyle4A4);
    changeStyle("addr", addrStyle4A4);
    changeStyle("tel", phoStyle4A4);
    document.all.WebBrowser.ExecWB(7, 1);// 打印页面预览
}
</script>
</head>
<body style="text-align:center;margin:0px;padding:0px;">
<TABLE border=0 cellpadding=0 cellspacing=0 class="Noprint center page-print-buttons" width=100%>
    <tr style="border: 0px;">
        <td style="border: 0px;">
            <input type=button class="txtToolBarButton" value="打印" data-before="changeStyleBatch">
            <input type=button class="txtToolBarButton" value="A4打印" data-before="changeStyleA4Batch">
            <input type=button class="txtToolBarButton" value="预览" data-before="changeStyleBatch">
            <input type=button class="txtToolBarButton" value="A4预览" data-before="changeStyleA4Batch">
        </td>
    </tr>
</TABLE>
<c:forEach items="${batchDatas}" var="tableData" varStatus="countAll">
<c:forEach items="${tableData.detailList}" var="data" varStatus="count">
<c:choose>
<c:when test="${!(countAll.last==true && count.last==true)}">
<div id="mian_div" name="mian_div" class="p_next">
    </c:when>
    <c:otherwise>
    <div id="mian_div" name="mian_div">
        </c:otherwise>
        </c:choose>
        <div id="cen_mid_div" name="cen_mid_div">
            <div id="top_cen_mid_div">
                <img src="<%=request.getContextPath()%>/img/bq_log1.gif" style="float: left;height: 55px;width: 250px"/>
                <span style="font-size:40px;margin-left:-120px;font-weight:bold">拣配单</span>
            </div>
            <table class="tableHead" width="100%">
                <tr>
                    <td colspan="6" class="center" style="font-size: 18px;">拣配单抬头信息</td>
                </tr>
                <tr style="height: 10mm; line-height: 10mm;">
					<td>拣货单号</td>
					<td  class="center" style="font-size: 30px;">&nbsp;${tableData.pickOrderId}</td>
                    <td>订单号</td>
                    <td  class="center">&nbsp;${tableData.orderCodes}</td>
					<td>订单类型</td>
					<td>&nbsp;${tableData.orderType}</td>
				</tr>
                <tr>
                    <td>经销商代码</td>
                    <td class="center">&nbsp;${tableData.dealerCode}</td>
                    <td>经销商名称</td>
                    <td colspan="3" style="text-align:center;">&nbsp;${tableData.dealerName}</td>
                </tr>
                <tr>
                    <td>发货工厂</td>
                    <td style="text-align:center;">&nbsp;${tableData.companyName}</td>
                    <td>收货地址</td>
                    <td colspan="3">
                        <div id="addr" name="addr"
                             style="text-align: left;width: 400px;line-height: 20px;text-overflow: ellipsis;overflow: hidden;">
                            &nbsp;${tableData.addr}
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>拣货日期</td>
                    <td>&nbsp;${tableData.date}</td>
                    <td>制表日期</td>
                    <td>&nbsp;${tableData.createDate}</td>
                    <td>制表人</td>
                    <td>&nbsp;${tableData.creater}</td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td colspan="5" align="left">
                        <div class="my_css">&nbsp;${tableData.remark}</div>
                    </td>
                </tr>
            </table>
            <table class="tableBody" width="100%" style="border-top: 0px;">
                <tr>
                    <td colspan="8" class="center" style="font-size: 18px;border-top: 0px;">配件拣配单细节信息</td>
                </tr>
                <tr>
                    <td class="center" style="width: 10mm;">序号</td>
					<td class="center" style="width: 100px;"><span>批次</span></td>
					<td class="center" style="width: 100px;"><span>货位</span></td>
					<td class="center" style="">配件编码</td>
					<td class="center" style="">配件名称</td>
					<td class="center" style="width: 50px;">单位</td>
<!-- 				<td class="center" style="width: 65px;">当前库存</td> -->
					<td class="center" style="width: 65px;">订货数量</td>
					<td class="center" style="width: 65px;">拣配数量</td>
<!-- 				<td class="center" style="width: 65px;">备注</td> -->
                </tr>
                <c:forEach items="${data}" var="dataDetail">
                    <tr>
                        <td>${dataDetail.indexNO}</td>
                        <td>${dataDetail.BATCH_NO}</td>
						<td>${dataDetail.LOC}</td>
						<td align="left">${dataDetail.PART_OLDCODE}</td>
						<td align="left">${dataDetail.PART_CNAME}</td>
						<td>${dataDetail.UNIT}</td>
<%--                         <td>${dataDetail.NORMAL_QTY_NOW}</td> --%>
                        <td>${dataDetail.SALES_QTY}</td>
						<td>${dataDetail.BOOKED_QTY}</td>
<%--                         <td style="text-align: left;font-size: 10px">&nbsp;${dataDetail.REMARK}</td> --%>
                    </tr>
                </c:forEach>
                <c:if test="${count.last}">
                    <c:forEach items="${tableData.dtlListAct}" var="data2">
                        <tr>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
<!--                             <td>&nbsp;</td> -->
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
<!--                             <td>&nbsp;</td> -->
                        </tr>
                    </c:forEach>
                </c:if>
            </table>
            <table class="tableBottom" width="100%" style="border-top: 0px;border-left: 0px">
                <tr style="height: 10mm; line-height: 10mm;  text-align: right;">
                    <td align="right" colspan="9">捡货员：___________________</td>
                </tr>
                <tr style="height: 10mm; line-height: 10mm;  text-align: right;">
                    <td align="right" colspan="9">核对员：___________________</td>
                </tr>
            </table>
        </div>
        第 ${count.count} 页/总 ${tableData.pageNOs} 页
    </div>
    </c:forEach>
    </c:forEach>
</body>
</html>
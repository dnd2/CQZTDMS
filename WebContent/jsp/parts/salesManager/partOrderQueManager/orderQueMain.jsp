<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<%
   String contextPath = request.getContextPath();
%>
<title>配件订单查询</title>
<style type="text/css">
   .mystyle {
       background-color: #F3F4F8;
       border: none;
       width: 65px;
       color: red;
   }
</style>
<script type="text/javascript">
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/partOrderQueManager/orderQueAction/orderQueSearch.json";
    var title = null;
    var columns = [
        {header: "序号", dataIndex: 'ORDER_ID', renderer: getIndex, align: 'center'},
//                    {id:'action',header: "操作",sortable: false,dataIndex: 'ORDER_ID',renderer:myLink ,align:'center'},
        {header: "订单类型", dataIndex: 'CODE_DESC'},
        {header: "订单次数", dataIndex: 'ORDER_NUM', sortable: true, align: 'center'},
        {header: "订单号", dataIndex: 'ORDER_CODE', sortable: true, align: 'center', renderer: viewOrder},
        {header: "订单金额", dataIndex: 'ORDER_AMOUNT', style: 'text-align: right;'},
        {header: "BO单号", dataIndex: 'BO_CODE', sortable: true, align: 'center', renderer: viewBoOrder},
        {header: "流水号", dataIndex: 'SO_CODE', align: 'center', renderer: viewSoOrder},
        {header: "销售单状态", dataIndex: 'SO_STATE', sortable: true},
        {header: "订货单位编码", dataIndex: 'DEALER_CODE', style: 'text-align: center;'},
        {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align: left;'},
        {header: "订货人", dataIndex: 'NAME'},
        {header: "提交日期", dataIndex: 'SUBMIT_DATE'},
        {header: "流水金额", dataIndex: 'AMOUNT', style: 'text-align: right;'},
        {header: "初审时间", dataIndex: 'CREATE_DATE', align: 'center'},
        {header: "审核金额", dataIndex: 'SH_AMOUNT', style: 'text-align: right;'},
        {header: "财务审核日期", dataIndex: 'FCAUDIT_DATE'},
        {header: "拣货单", dataIndex: 'PICK_ORDER_ID', renderer: viewPikOrder},
//    {header: "拣货日期", dataIndex: 'PICK_ORDER_PRINT_DATE'},
//    {header: "开始装箱日期", dataIndex: 'PKG_BEIGIN'},
        {header: "装箱完成日期", dataIndex: 'PKG_OVER_DATE'},
//    {header: "总箱数", dataIndex: 'ALL_NUM', align: 'center'},
        {header: "装箱明细", dataIndex: 'PICK_ORDER_ID', renderer: viewPkg},
//    {header: "发运单号", dataIndex: 'LOGISTICS_NO', style: 'text-align: center;', renderer: viewTrans},
//    {header: "发运件数", dataIndex: 'PKG_NUM', align: 'center'},
//    {header: "发运方式", dataIndex: 'TRANS_TYPE', style: 'text-align: center;'},
//    {header: "发运日期", dataIndex: 'TRANS_DATE', align: 'center'},
        {header: "发运金额", dataIndex: 'TRANS_AMOUNT', style: 'text-align: right;', renderer: viewTrans}
//    {header: "现场BO", dataIndex: 'XCBO_CODE', align: 'center', renderer: viewBoOrder2},
//    {header: "验收日期", dataIndex: 'IN_STOCK_DATE', align: 'center'},
//    {header: "验收人", dataIndex: 'NAME', align: 'center'},
//    {header: "是否已开票", dataIndex: 'IS_BILL', align: 'center'},
//    {header: "开票日期", dataIndex: 'BILL_DATE', align: 'center'},
//    {header: "验收异常数量", dataIndex: 'IF_HS', align: 'center', renderer: viewOrderEX},
//    {header: "接收单位", dataIndex: 'RCV_ORG', style: 'text-align: left;'}
    ];

    //设置超链接
    function myLink(value, meta, record) {
        var orderId = record.data.ORDER_ID;
        var str = "<a href=\"#\" onclick='viewDetail(\"" + orderId + "\")'>[查看]</a>";
        return String.format(str);
    }
    //查看
    function viewDetail(parms) {
        document.getElementById("orderId").value = parms;
        btnDisable();
        document.fm.action = "<%=contextPath%>/parts/salesManager/partOrderQueManager/orderQueAction/viewDeatilInit.do";
        document.fm.target = "_self";
        document.fm.submit();
    }

    function viewSoOrder(value, meta, record) {
        var soId = record.data.SO_ID;
        var soCode = record.data.SO_CODE;
        if (soCode != null) {
            return String.format("<a href=\"#\" title='查看销售明细' onclick='viewSoDtl(\"" + soId + "\",\"" + soCode + "\")' >" + soCode + "</a>");
        } else {
            return String.format("");
        }
    }
    function viewBoOrder(value, meta, record) {
        var boId = record.data.BO_ID;
        var orderId = record.data.ORDER_ID;
        var boCode = record.data.BO_CODE == null ? "" : record.data.BO_CODE;
        return String.format("<a href=\"#\" title='查看订单产生的BO明细' onclick='viewBoDtl(\"" + boId + "\",\"" + orderId + "\")' >" + boCode + "</a>");

    }
    function viewBoOrder2(value, meta, record) {
        var boId = record.data.XCBO_ID;
        var orderId = record.data.ORDER_ID;
        var boCode = record.data.XCBO_CODE == null ? "" : record.data.XCBO_CODE;
        return String.format("<a href=\"#\" title='查看订单产生的BO明细' onclick='viewBoDtl(\"" + boId + "\",\"" + orderId + "\")' >" + boCode + "</a>");

    }
    function viewSoDtl(soId, soCode) {
        var buttonFalg = "disabled";
        OpenHtmlWindow('<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/detailOrder.do?soId=' + soId + '&soCode=' + soCode + '&buttonFalg=' + buttonFalg, 900, 500);
    }
    function viewBoDtl(value, orderId) {
        var buttonFalg = "disabled";
        OpenHtmlWindow('<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/queryPartBoDetailInit.do?boId=' + value + '&orderId=' + orderId + '&flag=0&buttonFalg=' + buttonFalg, 900, 500);
    }

    function viewPikOrder(value, meta, record) {
    	if(value!=""){
        	return String.format("<a href=\"#\" onclick='viewPikDtl(\"" + value + "\")'>" + '查看' + "</a>");
    	}else{
    		return "";
    	}
    }
    function viewPikDtl(value) {
        OpenHtmlWindow("<%=request.getContextPath()%>/parts/salesManager/carFactorySalesManager/PartPkg/partPikViewDtlInit.do?pickOrderID=" + value, 600, 500);
    }
    function viewPkg(value, meta, record) {
    	if(value!=""){
    		return String.format("<a href=\"#\" onclick='viewPkgDtl(\"" + value + "\")'>" + '查看' + "</a>");
    	}else{
    		return "";
    	}
    }
    function viewPkgDtl(value) {
        OpenHtmlWindow("<%=request.getContextPath()%>/parts/salesManager/carFactorySalesManager/PartPkg/partPkgDtlInit.do?pickOrderID=" + value, 600, 500);
    }
    function viewTrans(value, meta, record) {
        var pickOrderId = record.data.PICK_ORDER_ID
        return String.format("<a href=\"#\" onclick='viewTransDtl(\"" + pickOrderId + "\")'>" + value + "</a>");
//    	return String.format("<a href=\"#\" onclick='viewTransDtl(\"" + value + "\")'>" + value + "</a>");
    }
    function viewTransDtl(value) {
        OpenHtmlWindow("<%=request.getContextPath()%>/parts/salesManager/carFactorySalesManager/PartPkg/partTransViewDtlInit.do?pickOrderId=" + value, 1000, 500);
    }
    //下载
    function exportPartStockExcel() {
        fm.action = "<%=contextPath%>/parts/salesManager/partOrderQueManager/orderQueAction/exportOrderExcel.do";
        fm.submit();
    }

    function viewOrder(value, meta, record) {
        var ORDER_ID = record.data.ORDER_ID;
        var ORDER_CODE = record.data.ORDER_CODE;
        if (ORDER_CODE != null) {
            return String.format("<a href=\"#\" title='查看订单号' onclick='viewOrderDtl(\"" + ORDER_ID + "\",\"" + ORDER_CODE + "\")' >" + ORDER_CODE + "</a>");
        } else {
            return String.format("");
        }
    }

    function viewOrderDtl(ORDER_ID, ORDER_CODE) {
        var buttonFalg = "disabled";//用于判断跳转页面是返回还是关闭
        OpenHtmlWindow('<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/detailDlrOrder.do?orderId=' + ORDER_ID + '&orderCode=' + ORDER_CODE + '&buttonFalg=' + buttonFalg, 900, 500);
    }

    function viewOrderEX(value, meta, record) {
        var IN_ID = record.data.IN_ID;
        if (value != '0') {
            return String.format("<a href=\"#\" title='查看异常验收明细' onclick='viewOrderEXDtl(\"" + IN_ID + "\")' >" + value + "</a>");
        } else {
            return String.format("");
        }
    }
    function viewOrderEXDtl(IN_ID) {
        OpenHtmlWindow('<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/detailDlrOrderEX.do?IN_ID=' + IN_ID, 900, 500);
    }
    function clrTxt(txtId) {
        document.getElementById(txtId).value = "";
    }
    function txtClr(txtId) {
        document.getElementById(txtId).value = "";
    }
    
    //批量导入
    function showUpload() {
        if ($("#uploadTable")[0].style.display == "none") {
            $("#uploadTable")[0].style.display = "block";
        } else {
            $("#uploadTable")[0].style.display = "none";
        }
    }

    //下载模板
    function exportExcelTemplate() {
        fm.action = "<%=contextPath%>/parts/salesManager/partOrderQueManager/orderQueAction/exportExcelTemplate.do";
        fm.submit();
    }

    //导入xls
    function uploadEx() {
        var fileValue;
        fileValue = document.getElementById("uploadFile").value;
        if (fileValue == "") {
            MyAlert("请选择文件!");
            return;
        }
        var fi = fileValue.substring(fileValue.length - 3, fileValue.length);
        if (fi != 'xls') {
            MyAlert('导入文件格式不对,请导入xls文件格式');
            return false;
        }
        MyConfirm("确定导入?",confirmResult);
    }
    function confirmResult(){
        fm.action = "<%=contextPath%>/parts/salesManager/partOrderQueManager/orderQueAction/uploadPartLogExcel.do";
        fm.submit();
    }
    
    function doQuery() {
        //__extQuery__(1);
        makeNomalFormCall(url, initResult, 'fm');
    }
    function initResult(json) {
        /* if (json != null) {
        	document.getElementById("orderQty").value = (json.ORDERQTY == null ? 0 : json.ORDERQTY);//订货总数量
            document.getElementById("orderAmount").value = (json.ORDERAMOUNT == null ? 0 : json.ORDERAMOUNT);//订货总金额
            document.getElementById("outQTY").value = (json.OUTQTY == null ? 0 : json.OUTQTY);//总出总数量
            document.getElementById("outAmount").value = (json.OUTAMOUNT == null ? 0 : json.OUTAMOUNT);//总发总金额
            document.getElementById("orderCnt").value = (json.ORDERCNT == null ? 0 : json.ORDERCNT);//订单总数量
            document.getElementById("outCnt").value = (json.OUTCNT == null ? 0 : json.OUTCNT);//发运单总数量
        }else{
			document.getElementById("orderQty").value    = 0;
            document.getElementById("orderAmount").value = 0;
            document.getElementById("outQTY").value      = 0;
            document.getElementById("outAmount").value   = 0;
            document.getElementById("orderCnt").value    = 0;
            document.getElementById("outCnt").value      = 0;
        } */
        __extQuery__(1);
    }

    
    $(document).ready(function(){    	
    	doQuery();
    });

</script>    
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
    <input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
    <input type="hidden" name="companyName" id="companyName" value="${companyName }"/>
    <input type="hidden" name="userRole" id="userRole" value="${userRole }"/>
    <input type="hidden" name="orderId" id="orderId" value=""/>
    <div class="wbox">
        <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>
            &nbsp;当前位置： 配件采购管理 &gt; 配件订单查询
        </div>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table class="table_query">
	            <tr>
	                <td class="right">订单号：</td>
	                <td>
	                    <input class="middle_txt" type="text" name="orderCode" id="orderCode"/>
	                </td>
	                <td class="right">提交日期：</td>
	                <td>
	                    <input id="cmmSDate" class="short_txt"  style="width: 80px;"
	                           name="cmmSDate" datatype="1,is_date,10" maxlength="10" value="${old}"
	                           group="cmmSDate,cmmEDate"/>
	                    <input class="time_ico" value=" "type="button"/>至
	                    <input id="cmmEDate" class="short_txt" name="cmmEDate" datatype="1,is_date,10"  style="width: 80px;"
	                           value="${now}" maxlength="10" group="cmmSDate,cmmEDate"/>
	                    <input class="time_ico" value=" " type="button"/>
	                </td>
	                <td class="right">订单类型：</td>
	                <td>
	                    <select name="orderType" id="orderType"  class="u-select">
	                        <option selected value=''>-请选择-</option>
	                        <c:forEach items="${orderType}" var="orderType">
	                            <option value="${orderType.key}">${orderType.value}</option>
	                        </c:forEach>
	                    </select>
	                </td>
	            </tr>
	            <tr>
	                <td class="right">发运单号：</td>
	                <td>
	                    <input class="middle_txt" type="text" name="transCode" id="transCode"/>
	                </td>
	                <td class="right">发运日期：</td>
	                <td>
	                    <input id="transSDate" class="short_txt" style="width: 80px;"
	                           name="transSDate" datatype="1,is_date,10" maxlength="10" value="" group="transSDate,transEDate"/>
	                    <input class="time_ico"
	                           value=" "
	                           type="button"/>至
	                    <input id="transEDate" class="short_txt" style="width: 80px;" name="transEDate"
	                           datatype="1,is_date,10" value="" maxlength="10" group="transSDate,transEDate"/>
	                    <input class="time_ico" value=" " type="button"/>
	                </td>
	                <td class="right">销售单状态：</td>
	                <td>
	                    <select name="soSate" id="soSate"  class="u-select">
	                        <option selected value=''>-请选择-</option>
	                        <c:forEach items="${stateMap}" var="stateMap">
	                            <option value="${stateMap.key}">${stateMap.value}</option>
	                        </c:forEach>
	                    </select>
	                </td>
	            </tr>
	            <tr>
	                <td class="right">配件编码：</td>
	                <td>
	                    <input class="middle_txt" type="text" name="partOldCode" id="partOldCode"/>
	                </td>
	                <td class="right">配件名称：</td>
	                <td>
	                    <input class="middle_txt" type="text" name="partName" id="partName"/>
	                </td>
	                <td class="right">排序方式：</td>
	                <td>
	                    <select id="order_By" name="order_By"  class="u-select">
	                        <option value="">-请选择-</option>
	                        <option value="1">订单号</option>
	                        <option value="2">提报日期</option>
	                        <option value="3">服务商编码</option>
	                        <option value="4">订单类型</option>
	                        <option value="5">销售单生成时间</option>
	                    </select>
	                </td>
	            </tr>
	            <tr>
	                <c:choose>
	                    <c:when test="${'3' eq userRole}">
	                        &nbsp;
	                    </c:when>
	                    <c:otherwise>
	<!--                         <td class="right">选择大区：</td> -->
	<!--                         <td> -->
	<!--                             <input type="text" id="orgCode" name="orgCode" value="" class="middle_txt"/> -->
	<!--                             <input name="obtn" id="obtn" class="mini_btn" type="button" value="&hellip;" -->
	<%--                                    onclick="showOrg('orgCode','' ,'true','${orgId}');"/> --%>
	<!--                             <input class="mini_btn" type="button" value="清空" onclick="clrTxt('orgCode');"/> -->
	<!--                         </td> -->
	<!--                         <td class="right">选择订货单位：</td> -->
	<!--                         <td align="left"> -->
	<!--                             <input type="text" class="middle_txt" name="dealerName" size="15" value="" id="dealerName"/> -->
	<!--                             <input type="hidden" class="middle_txt" name="dealerCode" size="15" value="" -->
	<!--                                    id="dealerCode"/> -->
	<!--                             <input name="button2" type="button" class="mini_btn" -->
	<%--                                    onclick="showOrgDealer('dealerCode','','true','','','','<%=Constant.DEALER_TYPE_DWR%>','dealerName');" --%>
	<!--                                    value="..."/> -->
	<!--                             <input type="button" class="mini_btn" onclick="txtClr('dealerCode');txtClr('dealerName');" -->
	<!--                                    value="清 空" id="clrBtn"/> -->
	<!--                         </td> -->
	                        <td class="right">订货单位：</td>
	                        <td>
	                            <input class="middle_txt" type="text" name="dealerName2" id="dealerName2"/>
	                        </td>
	<!--                         <td class="right"> -->
	<%--                             <c:choose> --%>
	<%--                                 <c:when test="${'2' eq userRole}"> --%>
	<!--                                    	 是否销售单位： -->
	<%--                                 </c:when> --%>
	<%--                                 <c:otherwise> --%>
	<!--                                     &nbsp; -->
	<%--                                 </c:otherwise> --%>
	<%--                             </c:choose> --%>
	<!--                         </td> -->
	<!--                         <td> -->
	<%--                             <c:choose> --%>
	<%--                                 <c:when test="${'2' eq userRole}"> --%>
	<!--                                     <script type="text/javascript"> -->
	<%--                                         genSelBoxExp("isSaler", <%=Constant.IF_TYPE%>, "", true, "short_sel", "onchange='__extQuery__(1)'", "false", ""); --%>
	<!--                                     </script> -->
	<%--                                 </c:when> --%>
	<%--                                 <c:otherwise> --%>
	<!--                                     &nbsp; -->
	<%--                                 </c:otherwise> --%>
	<%--                             </c:choose> --%>
	<!--                         </td> -->
	                    </c:otherwise>
	                </c:choose>
	                 <td class="right">流水号：</td>
	                <td>
	                    <input class="middle_txt" type="text" name="soCode" id="soCode"/>
	                </td>
	            	<td class="right"></td>
	                <td></td>
	            </tr>
	<!--             <tr> -->
	<%--                <td class="right">订货单位编码：</td>
	<%--                 <td> --%>
	<%--                     <input class="middle_txt" type="text" name="dealerCode2" id="dealerCode2"/> --%>
	<%--                 </td> --%>
	<!--                 <td class="right">流水号：</td> -->
	<!--                 <td> -->
	<!--                     <input class="middle_txt" type="text" name="soCode" id="soCode"/> -->
	<!--                 </td> -->
	<%--                <td class="right">BO单号：</td>
	<%--                 <td> --%>
	<%--                     <input class="middle_txt" type="text" name="boCode" id="boCode"/> --%>
	<%--                 </td>  --%>
	<!--             	<td class="right"></td> -->
	<!--                 <td></td> -->
	<!--             	<td class="right"></td> -->
	<!--                 <td></td> -->
	<!--             </tr> -->
	            <tr>
	                <td class="center" colspan="6">
	                    <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn"
	                           onclick="doQuery();"/>
	                    <input class="normal_btn" type="button" value="导 出"  name="export" id="export"
	                    		onclick="exportPartStockExcel()"/>
	                </td>
	            </tr>
	            <!-- <tr>
	                <td align="center" colspan="6">
				                    订单总数量:<input type="text" id="orderCnt" name="orderCnt" value="0" class="mystyle" readonly/>&nbsp;
				                    订货总数量:<input type="text" id="orderQty" name="orderQty" value="0" class="mystyle" readonly/>&nbsp;
				                    订货总金额:<input type="text" id="orderAmount" name="orderAmount" value="0" class="mystyle" readonly/>&nbsp;
				                    发运单数量:<input type="text" id="outCnt" name="outCnt" value="0" class="mystyle" readonly/>&nbsp;
				                    发出总数量:<input type="text" id="outQTY" name="outQTY" value="0" class="mystyle" readonly/>&nbsp;
				                    发出总金额:<input type="text" id="outAmount" name="outAmount" value="0" class="mystyle" readonly/>&nbsp;
	                </td>
	            </tr> -->
	        </table>
	    	</div>    
	    </div>    
        <table id="uploadTable" style="display: none">
            <tr>
                <td>
                	<font color="red">
                    	<input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate()"/>
                    	文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;
                    </font>
                    <input type="file" name="uploadFile" style="width: 250px" id="uploadFile" value=""/>
                    &nbsp;
                    <input type="button" id="upbtn" class="normal_btn" value="确定" onclick="uploadEx('1')"/></td>
            </tr>
        </table>
    </div>
    <!-- 查询条件 end -->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>进销存报表(供应中心)</title>
    <script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
            __extQuery__(1);
        }

    </script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;供应中心报表&gt;进销存报表(供应中心)
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />查询条件</th>
		<tr>
	           <td width="10%" align="right">出库日期：</td>
			   <td width="25%"><input id="checkSDate" class="short_txt"
				name="checkSDate" datatype="1,is_date,10" maxlength="10"
				group="checkSDate,checkEDate" value="${old}"/> <input class="time_ico"
				onclick="showcalendar(event, 'checkSDate', false);" value=" "
				type="button" />至 <input id="checkEDate"
				class="short_txt" name="checkEDate" datatype="1,is_date,10"
				maxlength="10" group="checkSDate,checkEDate" value="${now}"/> <input
				class="time_ico" onclick="showcalendar(event, 'checkEDate', false);"
				value=" " type="button" />
			  </td>
			  
			  <c:if test="${flag eq 1 }">
			        <td width="10%"   align="right">供应中心代码：</td>
                    <td width="20%">
                    <input class="middle_txt" type="text" readonly="readonly" id="dealerCode" name="dealerCode" value="${venderCode }"/>
                    </td>
			  </c:if>
			  <c:if test="${flag eq 0 }">
                    <td width="10%"   align="right">供应中心代码：</td>
                    <td width="20%">
                    <input class="middle_txt" type="text" id="dealerCode" name="dealerCode" value="${venderCode }"/>
                 </td>
              </c:if>
	           
	           <c:if test="${flag eq 1 }">
                    <td width="10%"   align="right">供应中心名称：</td>
                    <td width="20%">
                    <input class="middle_txt" type="text" readonly="readonly" id="dealerName" name="dealerName" value="${venderName }"/>
                    <input type="hidden" id="dealerId" name="dealerId" value="${venderId }"/>
                    </td>
                 </c:if>
                 <c:if test="${flag eq 0 }">
                    <td width="10%"   align="right">供应中心名称：</td>
                    <td width="20%">
                    <input class="middle_txt" type="text"  id="dealerName" name="dealerName"/>
                    <input class="mark_btn" type="button" value="&hellip;"
                           onclick="showGyzx('dealerId','dealerName','dealerCode');"/>
                    <input class="mini_btn" onclick="clearGyzxInput();" value="清除" type="button" name="clrBtn"/>
                    <input type="hidden" id="dealerId" name="dealerId" value=""/>
                 </td>
                 </c:if>
	   </tr>
       <tr>
            <td width="10%" align="right">配件编码：</td>
            <td width="25%"><input name="partOldcode" type="text" class="long_txt" id="partOldcode"/></td>
            <td width="10%" align="right">配件名称：</td>
            <td width="20%"><input name="partCname" type="text" class="middle_txt" id="partCname"/></td>
            <td width="10%" align="right">配件件号：</td>
            <td width="25%"><input name="partCode" type="text" class="long_txt" id="partCode"/></td>
            
        </tr>
        
	  <tr>
	   <td   colspan="6" align="center">
	   		<input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1);"/>
            <input class="normal_btn" type="button" value="导出" onclick="expMothStkAmtGyzxDlrExcel();"/>
       </td>
      </tr>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript">
var myPage;
var url = "<%=contextPath%>/report/partReport/partSalesReport/mothOutStockAmtDlrAction/mothOutStockAmtGyzxSearch.json";

var title = null;

var columns = [
    {header: "序号", align:'center',renderer:getIndex},
    {header: "供应中心代码", dataIndex: 'DEALER_CODE', style: 'text-align:left'},
    {header: "供应中心名称", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "期初数量", dataIndex: 'QC_STOCK'},
    {header: "入库数量", dataIndex: 'IN_QTY'},
    {header: "入库金额", dataIndex: 'IN_AMOUNT', style: 'text-align:right'},
    {header: "出库数量", dataIndex: 'OUT_QTY'},
    {header: "出库金额", dataIndex: 'SALE_AMOUNT', style: 'text-align:right'},
    {header: "账面库存", dataIndex: 'ITEM_QTY'}
];

//导出
function expMothStkAmtGyzxDlrExcel() {
    fm.action = "<%=contextPath%>/report/partReport/partSalesReport/mothOutStockAmtDlrAction/expMothOutStockAmtGyzxExcel.do";
    fm.target = "_self";
    fm.submit();
}

function clearGyzxInput() {
    document.getElementById("dealerId").value = '';
    document.getElementById("dealerCode").value = '';
    document.getElementById("dealerName").value = '';
}

function showGyzx(venderId,venderName,venderCode) {
    if (!venderName) {
    	venderName = null;
    }
    if (!venderId) {
    	venderId = null;
    }
    OpenHtmlWindow("<%=contextPath%>/jsp/parts/salesManager/carFactorySalesManager/gyzxSelect.jsp?venderName=" + venderName + "&venderId=" + venderId+"&venderCode="+venderCode, 730, 390);
}

</script>
</div>
</body>
</html>
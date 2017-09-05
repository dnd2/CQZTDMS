<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<title>配件销售单明细查看</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script type="text/javascript">
    //获取选择框的值
    function getCode(value) {
        var str = getItemValue(value);
        document.write(str);
    }
    //获取序号
    function getIdx(id) {
        document.write(document.getElementById(id).rows.length - 1);
    }
    //返回
    function goBack() {
    	_hide();
    	__parent().doQuery();
<%--         window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/partSoManageInit.do?flag=true'; --%>
    }

    function goClose() {
        _hide();
    }

    //明细下载
    function exportDetl() {
        document.fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/exportOrderExcel.do";
        document.fm.target = "_self";
        document.fm.submit();
    }
</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" name="soId" id="soId" value="${soId }"/>
<input type="hidden" name="soCode" id="soCode" value="${soCode}"/>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif"/>&nbsp;当前位置:
    配件管理 > 配件销售管理 &gt;配件销售单 >配件销售单查看
</div>
		<div class="form-panel">
            <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>销售单信息</h2>
            <div class="form-body">
		<table class="table_query"  cellSpacing=1 cellPadding=1 width="100%">
		    <td width="10%" class="right">销售单号:</td>
		    <td width="20%">${mainMap.SO_CODE}</td>
		    <td width="10%" class="right">销售日期:</td>
		    <td width="20%">${mainMap.CREATE_DATE_FM}</td>
		    <td width="10%" class="right">销售制单人:</td>
		    <td width="20%">${mainMap.CREATE_BY_NAME}</td>
		    </tr>
		    <tr>
		        <td width="10%" class="right">订单号:</td>
		        <td width="20%">${mainMap.ORDER_CODE}</td>
		        <td width="10%" class="right">订货单位:</td>
		        <td>${mainMap.DEALER_NAME}</td>
		        <td width="10%" class="right">订货制单人:</td>
		        <td width="20%">${mainMap.BUYER_NAME}</td>
		    </tr>
		    <tr>
		        <%-- <tr>
		           <td width="10%" class="right">销售单位:</td>
		           <td width="20%">${mainMap.SELLER_NAME}</td>
		           <td width="10%" class="right">订货单位:</td>
		           <td >${mainMap.DEALER_NAME}</td>
		           <td width="10%" class="right">订货制单人:</td>
		           <td width="20%">${mainMap.BUYER_NAME}</td>
		         </tr>--%>
		        <%-- <tr>
		           <td width="10%" class="right">出库仓库:</td>
		               <td width="20%">
		                   ${mainMap.WH_NAME}
		               </td>
		           <td width="10%" class="right">接收单位:</td>
		           <td width="20%">${mainMap.CONSIGNEES}</td>
		            <td width="10%" class="right">&nbsp;</td>
		           <td width="20%">&nbsp;</td>
		         </tr>--%>
		    <tr>
		        <td width="10%" class="right">接收地址:</td>
		        <td colspan="3">${mainMap.ADDR}</td>
		    </tr>
		    <tr>
		        <%--  <td width="10%" class="right">接收地址:</td>
		          <td width="20%">${mainMap.ADDR}</td>--%>
		        <td width="10%" class="right"> 接收人:</td>
		        <td width="20%">${mainMap.RECEIVER}</td>
		        <td width="10%" class="right"><span> 接收人电话:</span></td>
		        <td width="20%">${mainMap.TEL}</td>
		    </tr>
		    <%--<tr>
		      <td width="10%" class="right">邮政编码:</td>
		      <td width="20%">${mainMap.POST_CODE}</td>
		      <td width="10%" class="right">到站名称:</td>
		      <td width="20%">${mainMap.STATION}</td>
		      <td width="10%" class="right">发运方式:</td>
		      <td width="20%">
		      	${mainMap.TRANS_TYPE}
		      </td>
		    </tr>--%>
		    <tr>
		        <td width="10%" class="right">付款方式:</td>
		        <td width="20%">
		            <script type="text/javascript">
		                getCode(${mainMap.PAY_TYPE});
		            </script>
		        </td>
		        <td width="10%" class="right">订单类型:</td>
		        <td width="20%">
		            <script type="text/javascript">
		                getCode(${mainMap.ORDER_TYPE});
		            </script>
		        </td>
		        <td width="10%" class="right">运费支付方式:</td>
		        <td width="20%">
		            <script type="text/javascript">
		                getCode(${mainMap.TRANSPAY_TYPE});
		            </script>
		        </td>
		    </tr>
		    <tr>
		        <td width="10%" class="right">总金额:</td>
		        <td width="20%">${mainMap.YX_AMOUNT}</td>
		       <%-- <td width="10%" class="right">配件金额:</td>
		        <td width="20%">
		            <script type="text/javascript">document.write((${mainMap.AMOUNT}-${mainMap.FREIGHT}).toFixed(2))</script>
		        </td>--%>
		        <td width="10%" class="right">运费金额:</td>
		        <td width="20%">${mainMap.FREIGHT}</td>
		    </tr>
		    <tr>
		        <td width="10%" class="right">备注:</td>
		        <td colspan="5">${mainMap.REMARK2}</td>
		    </tr>
		</table>
		</div>
	</div>
<table id="file" class="table_list" >
   <caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息</caption>
    <tr>
        <th>序号</th>
        <th>配件编码</th>
        <th>配件名称</th>
        <!-- 
        <th>件号</th>
         -->
        <th>单位</th>
        <th>最小包装量</th>
        <th>采购数量</th>
        <th>销售数量</th>
        <th>出库数量</th>
        <th style="color: red">现场BO数量</th>
        <th>销售单价</th>
        <th>销售金额</th>
        <%--  <th>出库数量</th>
          <th>出库金额</th>--%>
        <!-- <th>批次号</th>
        <th>货位</th>
        <th>销售占用数量</th> -->
        <th>备注</th>
    </tr>
    <c:forEach items="${detailList}" var="data">
        <tr class="table_list_row1">
            <td align="center">
                <script type="text/javascript">getIdx("file");</script>
            </td>
            <td style="text-align: left"><c:out value="${data.PART_OLDCODE}"/></td>
            <td style="text-align: left"><c:out value="${data.PART_CNAME}"/></td>
            <!-- 
            <td align="left"><c:out value="${data.PART_CODE}"/></td>
             -->
            <td>&nbsp;<c:out value="${data.UNIT}"/></td>
            <td>&nbsp;<c:out value="${data.MIN_PACKAGE}"/></td>
            <td>&nbsp;<c:out value="${data.BUY_QTY}"/></td>
            <td>&nbsp;<c:out value="${data.SALES_QTY}"/></td>
            <td>&nbsp;<c:out value="${data.YX_QTY}"/></td>
            <td style="color: red">&nbsp;<c:out value="${data.BO_QTY}"/></td>
            <td style="text-align: right">&nbsp;<c:out value="${data.CONVERS_PRICE}"/></td>
            <td style="text-align: right">&nbsp;<c:out value="${data.YX_AMOUNT}"/></td>
                <%-- <td>&nbsp;<c:out value="${data.OUTSTOCK_QTY}" /></td>
                 <td>&nbsp;<c:out value="${data.SALE_AMOUNT}" /></td>--%>
            <%-- <td>&nbsp;<c:out value="${data.BATCH_NO}"/></td>
            <td>&nbsp;<c:out value="${data.LOC_CODE}"/></td>
            <td>&nbsp;<c:out value="${data.BOOKED_QTY}"/></td> --%>
            <td>&nbsp;<c:out value="${data.REMARK}"/></td>
        </tr>
    </c:forEach>
</table>
<%-- <table id="file1" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr>
        <th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>赠品信息</th>
    </tr>
    <tr bgcolor="#FFFFCC">
        <td align="center" width="3%">序号</td>
        <td align="center" width="10%">配件编码</td>
        <td align="center" width="13%">配件名称</td>
        <!-- 
        <td align="center" width="14%">配件件号</td>
         -->
        <td align="center" width="8%">赠送数量</td>
    </tr>
    <c:forEach items="${giftList}" var="data">
        <tr class="table_list_row1">
            <td align="center">
                <script type="text/javascript">getIdx("file1");</script>
            </td>
            <td align="center"><c:out value="${data.PART_OLDCODE}"/></td>
            <td align="center"><c:out value="${data.PART_CNAME}"/></td>
            <!-- 
            <td align="center"><c:out value="${data.PART_CODE}"/></td>
             -->
            <td align="center"><c:out value="${data.GIFT_QTY}"/></td>
        </tr>
    </c:forEach>
</table> --%>

<table id="file2" class="table_list"  >
<caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>单据操作信息</caption>
    <tr>
        <th align="center" width="3%">序号</th>
        <th align="center" width="10%">操作人</th>
        <th align="center" width="13%">操作时间</th>
        <th align="center" width="14%">环节</th>
        <th align="center" width="8%">状态</th>
    </tr>
    <c:forEach items="${historyList}" var="data">
        <tr class="table_list_row1">
            <td align="center">
                <script type="text/javascript">getIdx("file2");</script>
            </td>
            <td align="center"><c:out value="${data.OPT_NAME}"/></td>
            <td align="center"><c:out value="${data.OPT_DATE}"/></td>
            <td align="center"><c:out value="${data.WHAT}"/></td>
            <td><script type="text/javascript">getCode(${data.STATUS});</script></td>
        </tr>
    </c:forEach>
</table>
<table border="0" class="table_query">
    <tr>
        <td class="center">
            <c:choose>
                <c:when test="${'disabled' eq buttonFalg}">
                    <input class="normal_btn" type="button" value="明细下载" onclick="exportDetl()"/>
                    <input class="u-button" type="button" value="关 闭" onclick="goClose()"/>
                </c:when>
                <c:otherwise>
                    <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>
</div>
</form>
</body>
</html>


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
    <title>配件订货明细及交货率统计(供应中心)</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 报表管理 &gt;配件报表&gt;供应中心报表 &gt;
        配件订货明细及交货率统计(供应中心)
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <table class="table_query">
            <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>

                <td width="10%"   align="right">服务商编码：</td>
                <td width="20%"><input class="middle_txt" type="text" name="DEALER_CODE" id="DEALER_CODE"/></td>
                <td width="10%"   align="right">服务商：</td>
                <td width="20%">
                    <input class="middle_txt" type="text" id="childorgName" name="childorgName"/>
                    <input class="mark_btn" type="button" value="&hellip;"
                           onclick="showChildOrgByGyzx('childorgId','childorgName');"/>
                    <input class="mini_btn" onclick="clearInput();" value="清除" type="button" name="clrBtn"/>
                    <input id="childorgId" name="childorgId" type="hidden" value=""/>
                </td>
                 <td width="10%"   align="right">订货日期：</td>
                <td width="20%">
                    <input name="startDate" id="t1" value="${old }" type="text" class="short_txt" datatype="1,is_date,10"
                           group="t1,t2">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't1', false);"/>
                    至
                    <input name="endDate" id="t2" value="${now }" type="text" class="short_txt" datatype="1,is_date,10"
                           group="t1,t2">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't2', false);"/>
                </td>
            </tr>
            <tr>
	           <td width="10%"   align="right">订货单号：</td>
                <td width="20%"><input class="middle_txt" type="text" name="ORDER_CODE" id="ORDER_CODE"/></td>
	            <td width="10%"   align="right">销售单号：</td>
                <td width="20%"><input class="middle_txt" type="text" name="SO_CODE" id="SO_CODE"/></td>
                <td width="10%" align="right">配件类型：</td>
                <td width="20%">
                    <script type="text/javascript">
                    genSelBoxExp("PART_TYPE", <%=Constant.PART_BASE_PART_TYPES %>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
            </tr>
             <tr>
                <td width="10%" align="right">省份：</td>
                <td width="20%"><input class="middle_txt" type="text" name="REGION_NAME" id="REGION_NAME"/></td>
                <td width="10%" align="right">配件编码：</td>
                <td width="20%"><input class="middle_txt" type="text" name="PARTOLD_CODE" id="PARTOLD_CODE"/></td>
               <td width="10%" align="right">配件件号：</td>
               <td width="20%"><input class="middle_txt" type="text" name="PART_CODE" id="PART_CODE"/></td>
            </tr>
             <tr>
                 <td width="10%" align="right">配件名称：</td>
                 <td width="20%"><input class="middle_txt" type="text" name="PART_CNAME" id="PART_CNAME"/></td>
                 <c:if test="${flag eq 1 }">
                    <td width="10%"   align="right">供应中心：</td>
                    <td width="20%">
                    <input class="middle_txt" type="text" readonly="readonly" id="venderName" name="venderName" value="${venderName }"/>
                    <input type="hidden" id="venderId" name="venderId" value="${venderId }"/>
                    <input type="hidden" id="venderCode" name="venderCode" value="${venderCode }"/>
                    </td>
                 </c:if>
                 <c:if test="${flag eq 0 }">
                    <td width="10%"   align="right">供应中心：</td>
                    <td width="20%">
                    <input class="middle_txt" type="text" readonly="readonly" id="venderName" name="venderName"/>
                    <input class="mark_btn" type="button" value="&hellip;"
                           onclick="showGyzx('venderId','venderName','venderCode');"/>
                    <input class="mini_btn" onclick="clearGyzxInput();" value="清除" type="button" name="clrBtn"/>
                    <input type="hidden" id="venderId" name="venderId" value=""/>
                    <input type="hidden" id="venderCode" name="venderCode" value=""/>
                 </td>
                 </c:if>
            </tr>
            
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查询"
                           onclick="__extQuery__(1);"/>
                    <input class="normal_btn" type="button" value="导出" onclick="expPartOrderDtlExcel();"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        autoAlertException();//输出错误信息
        var myPage;
        var url = "<%=contextPath%>/report/partReport/partSalesReport/PartOrderDetailRep/queryPartOrderDtl4Gyzx.json";

        var title = null;

        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left'},
            {header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
            {header: "配件件号", dataIndex: 'PART_CODE',  style: 'text-align:left'},
            {header: "配件类型", dataIndex: 'PART_TYPE', align:'center',renderer: getItemValue},
            {header: "单位", dataIndex: 'UNIT', align:'center'},
            {header: "当前可用库存", dataIndex: 'STOCK', align:'center'},
            {header: "订货数量", dataIndex: 'BUY_QTY', align: 'center'},
            {header: "订货单价", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
            {header: "订货金额", dataIndex: 'BUY_AMOUNT', style: 'text-align:right'},
            {header: "已交货数量", dataIndex: 'SALES_QTY', align: 'center'},
            {header: "BO数量", dataIndex: 'BO_QTY', align: 'center'},
            {header: "BO项数", dataIndex: 'BOXS', align: 'center'},
            {header: "BO金额", dataIndex: 'BO_AMOUNT', style: 'text-align:right'},
            {header: "BO满足数量", dataIndex: 'TOSAL_QTY', align: 'center'},
            {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
            {header: "服务商编码", dataIndex: 'DEALER_CODE',  style: 'text-align:left'},
            {header: "服务商名称", dataIndex: 'DEALER_NAME',  style: 'text-align:left'},
            {header: "订货日期", dataIndex: 'SUBMIT_DATE', align:'center'},
            {header: "供应中心编码", dataIndex: 'SELLER_CODE', align:'center'},
            {header: "供应中心名称", dataIndex: 'SELLER_NAME', align:'center'},
            {header: "省份", dataIndex: 'REGION_NAME',  style: 'text-align:left'},
            {header: "销售单号", dataIndex: 'SO_CODE', align:'center'}
        ];

        function showGyzx(venderId,venderName,venderCode) {
            if (!venderName) {
            	venderName = null;
            }
            if (!venderId) {
            	venderId = null;
            }
            OpenHtmlWindow("<%=contextPath%>/jsp/parts/salesManager/carFactorySalesManager/gyzxSelect.jsp?venderName=" + venderName + "&venderId=" + venderId+"&venderCode="+venderCode, 730, 390);
        }

        function showChildOrgByGyzx(childorgId,childorgName) {
        	var venderId = $("venderId").value;
            if (!childorgName) {
                childorgName = null;
            }
            if (!childorgId) {
                childorgId = null;
            }
           
            OpenHtmlWindow("<%=contextPath%>/jsp/parts/salesManager/carFactorySalesManager/dealerSelectByGyzx.jsp?childorgName=" + childorgName + "&childorgId=" + childorgId+"&venderId="+venderId, 730, 390);
        }
        
        function clearGyzxInput() {
            document.getElementById("venderId").value = '';
            document.getElementById("venderName").value = '';
            document.getElementById("venderCode").value = '';
        }

        function clearInput() {
            document.getElementById("childorgId").value = '';
            document.getElementById("childorgName").value = '';
        }
        
        //导出
        function expPartOrderDtlExcel() {
            fm.action = "<%=contextPath%>/report/partReport/partSalesReport/PartOrderDetailRep/expPartOrderDtl4GyzxExcel.do";
            fm.target = "_self";
            fm.submit();
        }
        
    </script>
</div>
</body>
</html>
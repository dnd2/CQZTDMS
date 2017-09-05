<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <%
        String contextPath = request.getContextPath();
    %>
    <title>现场BO单审核</title>

</head>
<body onload="__extQuery__(1);">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="wbox">
        <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>
            &nbsp;当前位置：  配件管理 &gt; 配件销售管理 &gt; 现场BO审核
            <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
            <input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
            <input type="hidden" name="companyName" id="companyName" value="${companyName }"/>
            <input type="hidden" name="boId" id="boId" value=""/>
            <input type="hidden" name="boLineIds" id="boLineIds" value=""/>
        </div>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table class="table_query">
	            <tr>
	                <td width="10%" class="right">拣货单：</td>
	                <td width="20%">
	                    <input class="middle_txt" type="text" name="pickOrderId" id="pickOrderId"/>
	                </td>
	                <td width="10%" class="right">流水号：</td>
	                <td width="20%">
	                    <input class="middle_txt" type="text" name="soCode" id="soCode"/>
	                </td>
	                <td width="10%" class="right">订单号：</td>
	                <td width="20%">
	                    <input class="middle_txt" type="text" name="orderCode" id="orderCode"/>
	                </td>
	
	                <%-- <td width="10%" class="right">订货单位：</td>
	                 <td width="20%">
	                     <input class="middle_txt" type="text" name="dealerName" id="dealerName"/>
	                 </td>--%>
	            </tr>
	            <tr>
	                <td class="center" colspan="6">
	                    <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn"
	                           onclick="__extQuery__(1)"/>
	                </td>
	            </tr>
	        </table>
    	</div>
    	</div>
    </div>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
<script type="text/javascript">
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/BOManager/locBOHndAction/locBOHandleQuery.json";
    var title = null;

    var columns = [
        {header: "序号", dataIndex: '', renderer: getIndex, align: 'center'},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'SO_ID', renderer: myLink, align: 'center'},
        {header: "流水号", dataIndex: 'SO_CODE', align: 'center'},
        {header: "订单号", dataIndex: 'ORDER_CODE', style: 'text-align: left;'},
        {header: "订单单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
        {header: "订货单位", dataIndex: 'DEALER_NAME', style: "text-align:left"},
        {header: "审核日期", dataIndex: 'CREATE_DATE', style: 'text-align: left;'},
        {header: "拣货单", dataIndex: 'PICK_ORDER_ID', style: 'text-align: left;'},
        {header: "完成装箱日期", dataIndex: 'PKG_OVER_DATE', align: 'center'}
    ];

    function myLink(value, meta, record) {/* FLAG等于1是整单bo,2是部分bo */
        return String.format("<a href=\"#\" onclick='viewCountPg(\"" + record.data.FLAG + "\",\"" + record.data.SO_CODE + "\",\"" + record.data.PICK_ORDER_ID + "\")'>[审核]</a>");
    }

    //弹出审核页面
    function viewCountPg(flag, soCode, pickOrderId) {
        OpenHtmlWindow('<%=contextPath%>/parts/salesManager/BOManager/locBOHndAction/showBoDetlinit.do?pickOrderId=' + pickOrderId + '&soCode=' + soCode + '&flag=' + flag, 950, 500);
    }

    //有效
    function checkAction(value,flag) {
    	MyConfirm("确定强制关闭?",confirmResult,[value,flag]);
    }
    
    function confirmResult(pickOrderId,flag){
    	var checkUrl = '<%=contextPath%>/parts/salesManager/BOManager/locBOHndAction/checkBoDtl.json?pickOrderId=' + pickOrderId + '&curPage=' + myPage.page + '&flag=' + flag;
        makeNomalFormCall(checkUrl, getResult, 'fm');
    }

    function getResult(jsonObj) {
        if (jsonObj != null) {
            var success = jsonObj.success;
            MyAlert(success);
            __extQuery__(jsonObj.curPage);
        }
    }
</script>
</body>
</html>
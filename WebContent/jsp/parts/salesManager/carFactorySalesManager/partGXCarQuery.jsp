<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<head>
    <title>广宣品随车确认</title>
</head>
<script language="javascript">
	jQuery.noConflict();
	autoAlertException();
	loadcalendar();
	//初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxPlan/queryPartGxZCar.json";
    var title = null;
    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'PLAN_ID', align: 'center', renderer: getActionLink},
        {header: "计划单号", dataIndex: 'PLAN_CODE', align: 'center'},
        {header: "订货单位", dataIndex: 'DEALER_NAME', align: 'center'},
        {header: "出库类型", dataIndex: 'OUT_TYPE', align: 'center', renderer: getItemValue},
        {header: "出库仓库", dataIndex: 'WH_NAME', align: 'center'},
        {header: "随车包装号", dataIndex: 'PKG_NO1', align: 'center'},
        {header: "未随车包装号", dataIndex: 'PKG_NO2', align: 'center'}
    ];
    function getActionLink(value, meta, record) {
    	var link_1="<a href=\"#\" onclick='confirmPlan(\"" + value + "\")'>[确认]</a>";
    	return link_1;
    }
    
    function confirmPlan(planId){
    	if(confirm("确定这样操作?")){
    		var rollbackUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxPlan/confirmPlan.json?planId="+planId;
    	    sendAjax(rollbackUrl, ajaxResult, 'fm');
    	}
    }
   
   function ajaxResult(jsonObj){
		if (jsonObj != null) {
	        var success = jsonObj.success;
	        var error = jsonObj.error;
	        var exceptions = jsonObj.Exception;
	        if (success) {
	            MyAlert(success);
	       	} else if (error) {
	       		MyAlert(error);
	        } else if (exceptions) {
	            MyAlert(exceptions.message);
	        }
	    }
		__extQuery__(1);
	}
	jQuery(function(){
		__extQuery__(1);
		jQuery(document).on('click','#queryBtn',function(){
			__extQuery__(1);
		})
	})
  
	function exportGxCarExcel() {
    fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxPlan/exportGxCarExcel.do";
    fm.target = "_self";
    fm.submit();
}
	
</script>
</head>
<body  >
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
	<div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理 &gt; 配件销售管理  &gt; 广宣品随车确认</div>
		<table border="0" class="table_query">
            <th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td width="24%" align="right">计划单号：</td>
                <td >
                <input type="text" id="planCode" name="planCode" class="middle_txt">
                </td>
                <td align="right">订货单位：</td>
                <td ><input type="text" id="dealerName" name="dealerName" class="middle_txt">
                </td>
            </tr>
            <tr>
                <td width="24%" align="right">出库仓库：</td>
                <td >
                    <select name="whId" id="whId" class="short_sel">
                        <option selected value=''>-请选择-</option>
                        <c:forEach items="${wareHouseList}" var="wareHouse">
                            <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
                        </c:forEach>
                    </select>
                </td>
                
                <td align="right">出库类型：</td>
                <td >
                    <script type="text/javascript">
                        genSelBox("OUT_TYPE", <%=Constant.PART_GX_ORDER_OUT_TYPE%>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
                
                
            </tr>
            
            <tr>
                <td colspan="4" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"/>
                    &nbsp;
                    <input name="expButton" id="expButton" class="normal_btn" type="button" value="导出"
                       onclick="exportGxCarExcel();"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>

    </div>
</html>
</form>
</body>
</html>
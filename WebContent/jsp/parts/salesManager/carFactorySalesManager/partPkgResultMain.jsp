<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
	<title>装箱单管理</title>
	<style>.table_query .short_txt{margin: 0}</style>
</head>
<script language="javascript">
	//初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/query.json";
    var title = null;
    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'PICK_ORDER_ID', align: 'center', renderer: getActionLink},
        {header: "拣货单号", dataIndex: 'PICK_ORDER_ID', align: 'center'},
        {header: "订货单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
        {header: "订货单位", dataIndex: 'DEALER_NAME',style:'text-align:left'},
        {header: "合并人", dataIndex: 'CREATE_BY_NAME', align: 'center'},
        {header: "装箱时间", dataIndex: 'CREATE_DATE', align: 'center'}
    ];
    function getActionLink(value, meta, record) {
        var flag = record.data.FLAG;
    	var link_1="<a href=\"#\" onclick='rollbackOrder(\"" + value + "\")'>[取消装箱]</a>";
    	var link_2="<a href=\"#\" onclick='modifyOrder(\"" + value + "\")'>[修改装箱]</a>";
    	//var link_3="<a href=\"#\" onclick='modifyDlt(\"" + value + "\")'>[修改明细]</a>";
        if(flag == '0'){
            return link_2+link_1;
        }else
            return link_2;

    }
    function rollbackOrder(id){
    	MyConfirm("确定取消装箱?",confirmResult,[id]);
    }
    
    function confirmResult(id){
    	var rollbackUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/rollbackOrder.json?pickOrderId="+id;
	    makeNomalFormCall(rollbackUrl, ajaxResult, 'fm');
    }

    function modifyOrder(id){
    	window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/modifyOrderPage.do?pickOrderId="+id;
    }
    function modifyDlt(id){
    	window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/modifyDtlPage.do?pickOrderId="+id;
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
	$(document).ready(function(){
		__extQuery__(1);
		$(document).on('click','#queryBtn',function(){
			__extQuery__(1);
		})
	});
</script>
</head>
<body  >
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input name="IsPkg" id="IsPkg" value="<%=Constant.PART_BASE_FLAG_YES%>" type="hidden" />
	<div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理 > 配件销售管理 > 装箱结果修改</div>
		<div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
			<table border="0" class="table_query">
	            <tr>
	                <td class="right" width="11%">订货单位编码：</td>
	                <td width="24%"><input type="text" id="dealerCode" name="dealerCode" class="middle_txt">
	                <td class="right" width="11%">订货单位：</td>
	                <td width="24%"><input type="text" id="dealerName" name="dealerName" class="middle_txt">
	                </td>
	                <td class="right" width="11%">拣货单号：</td>
	                <td width="24%"><input class="middle_txt" type="text" id="pickOrderId" name="pickOrderId"/></td>
	            </tr>
	            <tr>
	                <td class="right">装箱日期：</td>
	                <td width="25%">
	                    <input name="CstartDate" type="text" class="short_txt" id="CstartDate" value="${old}"
	                           style="width:80px"/>
	                    <input name="button2" value=" " type="button" class="time_ico" />
	                    	至
	                    <input name="CendDate" type="text" class="short_txt" id="CendDate" value="${now}"
	                           style="width:80px"/>
	                    <input name="button2" value=" " type="button" class="time_ico" />
	                           
	                           
	                           </td>
	                <td class="right"</td>
	                <td width="24%">
	                    
	                </td>
	                <td class="right"></td>
	                <td width="24%">
	                </td>
	            </tr>
	            <tr>
	                <td colspan="6" class="center">
	                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"/>
	                </td>
	            </tr>
	        </table>
	        </div>
	    </div>    
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</html>
</form>
</body>
</html>
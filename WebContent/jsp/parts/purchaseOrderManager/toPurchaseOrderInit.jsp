<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="/jstl/cout" %>
<%@page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title></title>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
    <div class="navigation">
    	<img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：配件管理&gt; 采购计划管理&gt; 采购订单查询
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
        <table class="table_query">
            <tr>
                <td class="right">采购单号：</td>
                <td >
                	<input class="middle_txt" type="text" id="ORDER_CODE"  name="ORDER_CODE" value=""/>
                </td>
                <td class="right">计划单号：</td>
                <td >
                    <input class="middle_txt" type="text" id="PLAN_CODE" name="PLAN_CODE" value=""/>
                </td>
                
                <td class="right">订单日期：</td>
                <td >
                   <input name="sCreateDate" readonly="readonly" id="sCreateDate" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="sCreateDate,eCreateDate" style="width:80px;">
                   <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" onclick="showcalendar(event, 'sCreateDate', false);"/>
				   <label>至</label>
                   <input name="eCreateDate" readonly="readonly" id="eCreateDate" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="sCreateDate,eCreateDate" style="width:80px;">
                   <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" onclick="showcalendar(event, 'eCreateDate', false);"/>
                </td>
            </tr>
            <tr>
                <td class="right">供应商编码：</td>
                <td >
                	<input class="middle_txt" type="text" id="VENDER_CODE"  name="VENDER_CODE" value=""/>
                </td>
                <td class="right">供应商名称：</td>
                <td >
                    <input class="middle_txt" type="text" id="VENDER_NAME" name="VENDER_NAME" value=""/>
                </td>
                 <td class="right">采购员：</td> 
                 <td > 
                 	<select id="purUserId" name="purUserId" class="u-select"> 
                 		<option value="">-请选择-</option> 
                 		<c:forEach items="${purUserList }" var="list" varStatus="v"> 
                 			<option <c:if test="${list.USER_ID == nowUserId }">selected="selected"</c:if> value="${list.USER_ID }">${list.NAME }</option> --%>
                 		</c:forEach> 
                 	</select> 
                 </td> 
            </tr>
            
            <tr>
                <td class="center" colspan="6">
                	<input type="button" class="u-button" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1);" value="查询"/>&nbsp;
                	<input type="button" class="u-button" onclick="exportPurOrderHz();" value="导出"/>&nbsp;
                	<input type="reset" class="u-button" onclick="btnEable();" value="重置"/>&nbsp;
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        var myPage;
        var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseArrConfir/getPurcharOrderHz.json';
        var title = null;
        var columns = [
				{header: "序号", renderer: getIndex},
				{header: "操作",dataIndex:'ORDER_CODE',renderer: myLink, style:"text-align: center"},
				{header: "采购单号",dataIndex:'ORDER_CODE', style:"text-align: center"},
				{header: "计划单号",dataIndex:'PLAN_CODE', style:"text-align: center"},
 				{header: "供应商编码",dataIndex:'VENDER_CODE', style:"text-align: center"},
				{header: "供应商名称",dataIndex:'VENDER_NAME', style:"text-align: center"},
 				{header: "采购项数",dataIndex:'PART_COUNT', style:"text-align: center"},
				{header: "采购数量",dataIndex:'CHECK_QTY_SUM', style:"text-align:center"},
				{header: "订单日期",dataIndex:'CREATE_DATE', style:"text-align: center"}
            ];
        
        //操作链接生成
        function myLink(value,meta,record){
        	var str = '<a href="javascript:void(0)" onclick="toPurcharOrderMx(\''+value+'\')">[查看]</a>&nbsp;';
        	 	//str+= '<a href="<%=contextPath%>/parts/purchaseOrderManager/PurchaseArrConfir/toPurcharOrderAsnPrint.do?orderCode='+value+'" target="_bank">[打印ASN单]</a>&nbsp;';
        	return str;
        }
        //查看
        function toPurcharOrderMx(value){
        	var urlkey = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseArrConfir/toPurcharOrderMx.do?orderCode="+value;
        	OpenHtmlWindow(urlkey,950,500);
        }
        
        //导出
        function exportPurOrderHz(){
        	MyConfirm('确认导出？',function(){
        		btnDisable();
        		fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseArrConfir/exportPurOrderHz.do";
     			fm.submit();
        	});
        }
    </script>
</div>
</body>
</html>
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
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>查询计划明细</title>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
    <div class="navigation">
    	<img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：总部采购管理&gt; 采购计划管理 &gt; 月度计划&gt; 查看
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
    	<input type="hidden" id="planId" name="planId" value="${planId}">
    	<input type="hidden" id="planTypes" name="planTypes" value="${planTypes}">
    	<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
        <table class="table_query" >
            
            <tr>
            	<td class="right">配件编码：</td>
                <td >
                	<input type="text" class="middle_txt" id="PART_OLDCODE" name="PART_OLDCODE" value="">
                </td>
            	<td class="right">配件名称：</td>
                <td >
                	<input type="text" class="middle_txt" id="PART_CNAME" name="PART_CNAME" value="">
                </td>
                <td class="right">配件件号：</td>
                <td >
                	<input type="text" class="middle_txt" id="PART_CODE" name="PART_CODE" value="">
                </td>
            </tr>
            <tr>
            	<td class="right">供应商编码：</td>
                <td >
                    <input class="middle_txt" type="text" id="vender_code" name="vender_code" value=""/>
                </td>
                <td class="right">供应商名称：</td>
                <td >
                    <input class="middle_txt" type="text" id="vender_name" name="vender_name" value=""/>
                </td>
                <td class="right">配件备件类型：</td>
                <td >
                    <script type="text/javascript"> genSelBoxExp("produce_state",<%=Constant.PART_PRODUCE_STATE%>,"","true","","","false",
							'');
					</script>
                </td>
            </tr>
            
            <tr>
                <td class="center" colspan="6">
                	<input type="button" class="u-button u-query" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1);" value="查询"/>&nbsp;
                	<input type="button" class="normal_btn" onclick="exportPurOrderHz();" value="导出"/>&nbsp;
                	<input type="button" class="normal_btn" onclick="goback();" value="返回"/>&nbsp;
                	<input type="reset" class="u-button u-cancel" onclick="btnEable();" value="重置"/>&nbsp;
                </td>
            </tr>
        </table>
        </div>
        </div>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
    	//btnEable();btnDisable();//全局
      // autoAlertException();//输出错误信息
        var myPage;
        var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseArrConfir/getMonthlyPlanHzs.json';
        var title = null;
        var columns = [
				{header: "序号", renderer: getIndex},
				{header: "计划单号",dataIndex:'PLAN_NO', style:"text-align: center"},
				{header: "计划数量",dataIndex:'PLAN_NUM', style:"text-align: center"},
				{header: "备件编码",dataIndex:'PART_OLDCODE', style:"text-align: center"},
				{header: "备件件号",dataIndex:'PART_CODE', style:"text-align: center"},
				{header: "备件名称",dataIndex:'PART_CNAME', style:"text-align:center"},
				{header: "备件类型",dataIndex:'PRODUCE_STATE', style:"text-align: center",renderer:getItemValue},
 				{header: "供应商编码",dataIndex:'VENDER_CODE', style:"text-align: center"},
 				{header: "供应商名称",dataIndex:'VENDER_NAME', style:"text-align: center"},
				{header: "计划日期",dataIndex:'MONTH_DATE', style:"text-align: center"},
 				{header: "状态",dataIndex:'STATUS', style:"text-align: center"}
            ];
        
        function goback(){
        	if('${planTypes}'=='<%=Constant.PART_PURCHASE_PLAN_TYPE_01%>'){
        		location = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseArrConfir/toMonthlyPlanInit.do';
        	}else{
        		location = '<%=contextPath%>/parts/planManager/PartPlanManager/toSupplementPlanInit.do';
        	}
        }
      //导出
        function exportPurOrderHz(){
        		btnDisable();
        		fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseArrConfir/exMonthlyPlanHz.do";
     			fm.submit();
        }
    </script>
</div>
</body>
</html>
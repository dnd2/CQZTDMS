<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
    <title>查询计划明细</title>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
    <div class="navigation">
    	<img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理&gt; 采购计划管理 &gt; 计划编制&gt; 查看
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
    	<input type="hidden" id="planId" name="planId" value="${planId}">
    	<input type="hidden" id="planTypes" name="planTypes" value="${planTypes}">
    	 <div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
        <table class="table_query">
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
                <td class="center" colspan="6">
                	<input type="button"  class="u-button" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1);" value="查询"/>&nbsp;
                	 <input type="button" class="normal_btn" onclick="javascript:history.go(-1);" value="返回"/>&nbsp;
                	 
                </td>
            </tr>
        </table>
        </div>
        </div>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        var myPage;
        var url = "<%=contextPath%>/parts/planManager/PartPlanManager/getRollingPlanInfoById.json";
        var title = null;
        var columns = [
				{header: "序号", renderer: getIndex},
				{header: "计划单号",dataIndex:'PLAN_NO', style:"text-align: center"},
				{header: "配件编码",dataIndex:'PART_OLDCODE', style:"text-align: center"},
				{header: "配件名称",dataIndex:'PART_CNAME', style:"text-align: center"},
				{header: "配件件号",dataIndex:'PART_CODE', style:"text-align: center"},
				{header: "计划数量",dataIndex:'PLAN_NUM', style:"text-align: center"},
				//{header: "备件类型",dataIndex:'PART_TYPE', style:"text-align: center",renderer:getItemValue},
				{header: "自制/配套",dataIndex:'PRODUCE_STATE', style:"text-align: center",renderer:getItemValue},
				{header: "采购方式",dataIndex:'PRODUCE_FAC', style:"text-align: center",renderer:getItemValue},
				{header: "上级单位",dataIndex:'SUPERIOR_PURCHASING', style:"text-align: center",renderer:getItemValue},
				{header: "计划年月",dataIndex:'MONTH_DATE', style:"text-align: center"},
				//{header: "转单周期",dataIndex:'ORDER_PERIOD', style:"text-align: center",renderer:getItemValue},
				{header: "供应商",dataIndex:'VENDER_NAME', style:"text-align: center"},
				{header: "收货库房",dataIndex:'WH_NAME', style:"text-align: center"},
				{header: "备注",dataIndex:'PLAN_REMARK', style:"text-align: center"}
            ];
        
        function goback(){
        	if('${planTypes}'=='<%=Constant.PART_PURCHASE_PLAN_TYPE_01%>'){
        		location = '<%=contextPath%>/parts/planManager/PartPlanManager/toRollingPlanInit.do';
        	}else{
        		location = '<%=contextPath%>/parts/planManager/PartPlanManager/toSupplementPlanInit.do';
        	}
        }
    </script>
</div>
</body>
</html>
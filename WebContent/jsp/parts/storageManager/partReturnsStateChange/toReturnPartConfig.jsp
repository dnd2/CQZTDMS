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
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>退换货解封审核配置</title>
    <script language="JavaScript">
    	jQuery.noConflict();//初始化jquery
        //loadcalendar();//初始化方法
    </script>
</head>
<body onload="selectQuery()">
<div class="wbox">
    <div class="navigation">
    	<img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置： 备件仓储管理&gt; 备件退换货状态变更&gt; 退换货解封审核配置
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <table class="table_query" bordercolor="#DAE0EE">
            <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td width="10%" align="right">配送中心编码：</td>
                <td width="20%">
                	<input class="middle_txt" type="text" id="dealerCode"  name="dealerCode" value=""/>
                </td>
                
                <td width="10%" align="right">配送中心名称：</td>
                <td width="20%">
                	<input class="middle_txt" type="text" id="dealerName"  name="dealerName" value=""/>
                </td>
                
            </tr>
            
            <tr>
                <td align="center" colspan="6">
                	<input type="button" class="normal_btn" name="BtnQuery" id="queryBtn" onclick="selectQuery()" value="查询"/>&nbsp;&nbsp;
                </td>
            </tr>
        </table>
	 
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        autoAlertException();//输出错误信息
        var myPage;
        var url = '<%=contextPath%>/parts/storageManager/partReturns/ReturnPartStateChange/getReturnPartConfig.json';
        var title = null;
        var columns = [
				{header: "序号", renderer: getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'DEALER_ID',renderer:myLink , style: 'text-align:center'},
				{header: "配送中心编码",dataIndex:'DEALER_CODE', style:"text-align: left"},
				{header: "配送中心名称",dataIndex:'DEALER_NAME', style:"text-align: left"},
				{header: "额度",dataIndex:'AMOUNT', style:"text-align: left"},
				{header: "备注",dataIndex:'REMARK', style:"text-align: left"},
				{header: "修改日期",dataIndex:'UPDATE_DATE', style:"text-align: left"},
				{header: "修改人",dataIndex:'UPDATE_BY_CN', style:"text-align: left"},
				{header: "状态",dataIndex:'STATE', style:"text-align: left",renderer:getItemValue}
            ];
        
        //操作链接生成
        function myLink(value,meta,record){
        	return '<a href="javascript:void(0)" onclick="toReturnPartcfgUpdate(\''+value+'\')">[修改]</a>';
        }
        
        //btnEnable();btnDisable();//全局
        
        //报表特殊时间控制查询
        function selectQuery(){
       		__extQuery__(1);
        }
        //跳转修改页面
        function toReturnPartcfgUpdate(dealerId){
        	var urlkey = '<%=contextPath%>/parts/storageManager/partReturns/ReturnPartStateChange/toReturnPartcfgUpdate.do?dealerId='+dealerId ;
			OpenHtmlWindow(urlkey,850,500);
        }
    </script>
</div>
</body>
</html>
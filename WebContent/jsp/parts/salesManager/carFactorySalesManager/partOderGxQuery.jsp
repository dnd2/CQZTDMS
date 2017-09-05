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
    <meta http-equiv="keywords" content="广宣品订单发运查询">
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>广宣品订单发运查询</title>
</head>
<body >
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置: 配件管理 > 配件销售管理 > 广宣品订单发运查询
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <table class="table_query">
            <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>
            	<td width="10%" align="right">提交日期：</td>
                <td width="22%" align="left"><input class="time_txt" id="SCREATE_DATE" name="SCREATE_DATE"
                                                    datatype="1,is_date,10" maxlength="10" value="${start}" style="width:65px"
                                                    group="SCREATE_DATE,ECREATE_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'SCREATE_DATE', false);" type="button"/>
                    至
                    <input class="time_txt" id="ECREATE_DATE" name="ECREATE_DATE" datatype="1,is_date,10" value="${end}" style="width:65px"
                           maxlength="10" group="SCREATE_DATE,ECREATE_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'ECREATE_DATE', false);" type="button"/>
                </td>
                
                 <td width="10%" align="right" id="deId">订货单位代码：</td>
				<td align="left" nowrap="true" id="deId1">
					<input class="middle_txt" id="dealerCode" value="${orgCodes}" name="dealerCode" type="text" />
					<input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true,'','10771001','');" value="..." />
					<input name="clrBtn" type="button" class="normal_btn" onclick="clrTxt('dealerCode');" value="清除" />
					<input type="hidden" name="DEALER_IDS" id="DEALER_IDS" value="" />
				</td>
				
                 <td width="10%" align="right">订货单位：</td>
                <td width="20%">
                    <input class="middle_txt" type="text" id="dealerName" name="dealerName"/>
                </td>
               
            </tr>
            <tr>
                <td width="10%" align="right">配件编码：</td>
                <td width="20%">
                    <input class="middle_txt" type="text" id="partOldCode" name="partOldCode"/>
                </td>
                <td width="10%" align="right">配件名称：</td>
                <td width="20%">
                    <input class="middle_txt" type="text" id="partCname" name="partCnaem"/>
                </td>
                
                
			</tr>
            
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" />
                    <input name="BtnQuery" id="exportBtn" class="normal_btn" type="button" value="导 出" />
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        jQuery.noConflict();
        var myPage;
	    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOrderGxQuery/query.json";
	    var title = null;
		var columns = [
		            {header: "序号", align:'center',renderer:getIndex},
                    {header: "订货单位代码", dataIndex:"DEALER_CODE", align:'center'},
		            {header: "订货单位", dataIndex:"DEALER_NAME", align:'center'},
		            {header: "配件编码", dataIndex:"PART_OLDCODE", align:'center'},
                    {header: "配件名称", dataIndex:"PART_CNAME", align:'center'},
                    {header: "订购数量", dataIndex:"BUY_QTY", align:'center'},
                    {header: "订购已发运数量", dataIndex:"TRANS_QTY", align:'center'},
                    {header: "赠送数量", dataIndex:"REPORT_QTY", align:'center'}                    
			      ];
	    jQuery(function(){
	    	loadcalendar();
        	autoAlertException();
        	jQuery(document).on('click','#queryBtn',function(){
	    		__extQuery__(1);
	    	})
	    	jQuery(document).on('click','#exportBtn',function(){
	    		fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOrderGxQuery/exportExcel.do";
	    		fm.submit();
	    	})
	    	
	    	__extQuery__(1);
	    })
	    
	    function clrTxt(value) {
			document.getElementById(value).value = "";
		}

    </script>
</div>
</body>
</html>
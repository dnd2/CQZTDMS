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
    <style>.table_query .middle_txt{width: 79px;margin-right:2px}</style>
    <script type="text/javascript">
    	jQuery.noConflict();

        var myPage;
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/report/PartTransRep/query.json";
        var title = null;
								
        var columns = [
 			{header: "序号", align: 'center', renderer: getIndex},
        	{header: "发出时间", dataIndex: 'CREATE_DATE', align: 'center'},
            {header: "订单提交日期", dataIndex: 'SUBMIT_DATE', align: 'center'},
            {header: "财务审核日期", dataIndex: 'FCAUDIT_DATE', align: 'center'},
            {header: "库房代码", dataIndex: 'WH_CODE', align: 'center'},
            {header: "库房名称", dataIndex: 'WH_NAME', align: 'center'},
            {header: "服务站名称", dataIndex: 'DEALER_NAME', align: 'center'},
            {header: "金额", dataIndex: 'AMOUNT', align: 'center',style:'text-align:right'},
            {header: "销售单号", dataIndex: 'SO_CODE', align: 'center'},
            {header: "出库清单号码", dataIndex: 'OUT_CODE', align: 'center'},
            {header: "拣货单", dataIndex: 'PICK_ORDER_ID', align: 'center'},
//             {header: "件数", dataIndex: 'PKGNO', align: 'center'},
//             {header: "总重量", dataIndex: 'WEIGHT', align: 'center'},
            {header: "发运方式名称", dataIndex: 'FIX_NAME', align: 'center'},
           	{header: "备注", dataIndex: 'REMARK2', align: 'center'}
      ];
        function myLink(value, meta, record) {
        	 return "";
        }
        $(document).ready(function(){
        	__extQuery__(1);
        });
    </script>
</head>
<body onload="__extQuery__(1);" enctype="multipart/form-data">
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置： 配件管理 > 配件销售管理 > 销售明细报表</div>
    <form name="fm" id="fm" method="post">
        <div class="form-panel">
            <h2><img src="/CQZTDMS/jmstyle/img/search-ico.png" class="panel-icon panel-query-title">查询条件</h2>
            <div class="form-body">
                <table border="0" class="table_query">
                    <tr>
                        <td class="right" width="10%" align="right">发出时间：</td>
                        <td width="30%"><input name="transStartDate" type="text" class="short_time_txt middle_txt" id="transStartDate" value="${old}"/>
                            <input name="button2" type="button" class="time_ico" value=" "/>
                            至
                            <input name="transEndDate" type="text" class="short_time_txt middle_txt" id="transEndDate" value="${now}"/>
                            <input name="button2" type="button" class="time_ico" value=" "/></td>
                        <td class="right" width="12%" align="right">订单提交时间：</td>
                        <td width="30%"><input name="subStartDate" type="text" class="short_time_txt middle_txt" id="subStartDate" value=""/>
                            <input name="button2" type="button" class="time_ico" value=" "/>
                            至
                            <input name="subEndDate" type="text" class="short_time_txt middle_txt" id="subEndDate" value=""/>
                            <input name="button2" type="button" class="time_ico" value=" "/></td>
                    </tr>
                    <tr>
                        <td class="right" width="10%" align="right">财务审核时间：</td>
                        <td width="30%"><input name="finStartDate" type="text" class="short_time_txt middle_txt" id="finStartDate" value=""/>
                            <input name="button2" type="button" class="time_ico" value=" "/>
                            至
                            <input name="finEndDate" type="text" class="short_time_txt middle_txt" id="finEndDate" value=""/>
                            <input name="button2" type="button" class="time_ico" value=" "/></td>
                        <td class="right" width="10%" align="right">库房：</td>
                        <td width="20%">
                            <select name="whId" id="whId" class="short_sel u-select">
                                <option selected value=''>-请选择-</option>
                                <c:forEach items="${wareHouseList}" var="wareHouse">
                                    <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td width="10%" align="right"></td>
                        <td width="20%"></td>
                        <td width="10%" align="right"></td>
                        <td width="20%"></td>
                    </tr>
                    <tr>
                        <td colspan="6" align="center" class="center">
                            <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="MyAlert();"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>
</div>    
</body>
</html>

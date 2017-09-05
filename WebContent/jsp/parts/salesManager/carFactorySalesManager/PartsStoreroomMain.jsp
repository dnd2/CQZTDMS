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
    <meta http-equiv="keywords" content="配件库存明细">
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>配件库存明细</title>
</head>
<body >
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;本部仓储报表&gt;
       配件库存明细
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <table class="table_query">
            <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>
               
                <td width="10%" align="right">需要查询的物料编码：</td>
                <td width="20%" align="left"><input class="middle_txt" type="text" id="partOldcode" name="partOldcode"/>
                
                  <td width="10%" align="right">查询时间：</td>
                <td width="22%" align="left"><input class="time_txt" id="SCREATE_DATE" name="SCREATE_DATE"
                                                    datatype="1,is_date,10" maxlength="10" value="${start}" style="width:65px"
                                                    group="SCREATE_DATE,ECREATE_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'SCREATE_DATE', false);" type="button"/>
                    至
                    <input class="time_txt" id="ECREATE_DATE" name="ECREATE_DATE" datatype="1,is_date,10" value="${end}" style="width:65px"
                           maxlength="10" group="SCREATE_DATE,ECREATE_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'ECREATE_DATE', false);" type="button"/>
                </td>
                
                <!--<td width="10%" align="right">单据类型：</td>
                <td width="20%">
                    <select id="WH_ID" name="WH_ID" class="short_sel">
                        <c:forEach items="${wareHouses}" var="wareHouse">
                            <option value="${wareHouse.whId }">${wareHouse.whName }</option>
                        </c:forEach>
                    </select>
                </td>-->
                
                
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
	    var url = "<%=contextPath%>/report/partReport/partBuySaleStoreReport/PartsStoreroomAction/query.json";
	    var title = null;
		var columns = [
		            {header: "序号", align:'center',renderer:getIndex},
		            {header: "物料编号", dataIndex:"PART_ID", align:'center'},
                    {header: "物料编码", dataIndex:"PART_OLDCODE", align:'center'},
                    {header: "物料名称", dataIndex:"PART_NAME",align:'center'},
		            {header: "单号", dataIndex:"CODE", align:'center'},
		            {header: "单据类型", dataIndex:"TO_TYPE", align:'center'},
		            {header: "业务员", dataIndex:"NAME", align:'center'},
		            {header: "日期", dataIndex:"CREATE_DATE", align:'center'},
                    {header: "货位", dataIndex:"LOC_NAME", align:'center'},
                    {header: "入库数量", dataIndex:"RU_PART_NUM", align:'center'},
                    {header: "出库数量", dataIndex:"CHU_PART_NUM", align:'center'}
                    //{header: "库存数量", dataIndex:"STOCK_QTY", align:'center'},
                    //{header: "库存金额", dataIndex:"STOCK_AMOUNT", align:'center'}
			      ];
	    jQuery(function(){
	    	loadcalendar();
        	autoAlertException();
        	jQuery(document).on('click','#queryBtn',function(){
	    		__extQuery__(1);
	    	})
	    	jQuery(document).on('click','#exportBtn',function(){
	    		fm.action = "<%=contextPath%>/report/partReport/partBuySaleStoreReport/PartsStoreroomAction/exportExcel.do";
	    		fm.submit();
	    	})
	    	__extQuery__(1);
	    })

    </script>
</div>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <script type="text/javascript">
    	jQuery.noConflict();
        var myPage;
        var url = "<%=contextPath%>/report/partReport/partSalesReport/PartOrderDetailRep/query.json";
        var title = null;
																
        var columns = [
 			{header: "序号", align:"center", renderer: getIndex},
           /* {header: "是否生成销售单", dataIndex: "SO_FLAG", align: 'center'},*/
            {header: "配件编码", dataIndex: "PART_OLDCODE", style:'text-align:left;'},
            {header: "配件名称", dataIndex: "PART_CNAME", style:'text-align:left;'},
            {header: "配件件号", dataIndex: "PART_CODE", style:'text-align:left;'},
            {header: "配件类型", dataIndex: "PART_TYPE", align: 'center',renderer:getItemValue},
            {header: "单位", dataIndex: "UNIT", align: 'center'},
            {header: "当前可用库存", dataIndex: "STOCK", align:"center"},
            {header: "订货单价", dataIndex: "BUY_PRICE", align: 'center'},
            {header: "订货数量", dataIndex: "BUY_QTY", align: 'center'},
            {header: "订货金额", dataIndex: "BUY_AMOUNT", align: 'center'},
            {header: "已交货数量", dataIndex: "SALES_QTY", align: 'center'},
            {header: "BO数量", dataIndex: "BO_QTY", align: 'center'},
            {header: "BO金额", dataIndex: "BO_AMOUNT", align: 'center'},
            {header: "BO项数", dataIndex: "BOXS", align: 'center'},
            {header: "BO满足数量", dataIndex: "TOSAL_QTY", align: 'center'},
            {header: "订货时间", dataIndex: "SUBMIT_DATE", align: 'center'},
            {header: "默认供货商", dataIndex: "VENDER_NAME", style:'text-align:left;'},
            {header: "服务商代码", dataIndex: "DEALER_CODE", style:'text-align:left;'},
            {header: "服务商名称", dataIndex: "DEALER_NAME", style:'text-align:left;'},
            {header: "订服务商区代码", dataIndex: "PROVINCE_ID", align: 'center'},
            {header: "订货服务商名称", dataIndex: "REGION_NAME", style:'text-align:left;'},
            {header: "销售单号", dataIndex: "SO_CODE", style:'text-align:left;'}
           /* {header: "销售总项数", dataIndex: "SALE_TERM_QTY", align: 'center'},
            {header: "BO总项数", dataIndex: "BO_TOTAL_TERM_QTY", align: 'center'},
            {header: "BO率", dataIndex: "BO_PERCENT", align: 'center'}*/
      ];
        //ready事件
        jQuery(function(){
        	jQuery("#queryBtn").click(function(){
        		__extQuery__(1);
        	});
        	jQuery("#exportBtn").click(function(){
        		exportExcel();
        	});
        	loadcalendar();
        	autoAlertException();
        	__extQuery__(1);
        })
       function query(){
    	   __extQuery__(1);
       }
       function exportExcel(){
    	   fm.action = "<%=contextPath%>/report/partReport/partSalesReport/PartOrderDetailRep/exportExcel.do";
           fm.submit();
       }
       
     
    </script>
</head>
<body enctype="multipart/form-data">
<form name="fm" id="fm" method="post">
    <div id="div1" class="wbox">
        <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置: 报表管理&gt;配件报表&gt;本部销售报表&gt;配件订货明细及交货率统计</div>
        <table border="0" class="table_query">
            <th colspan="6" width="100%"><img class="nav" src="<%=contextPath %>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td width="10%"   align="right">订货日期：</td>
                <td width="22%">
                    <input name="orderStartDate" id="orderStartDate" value="${old}" type="text" class="short_txt" datatype="1,is_date,10" group="orderStartDate,orderEndDate">
	           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 'orderStartDate', false);"/>
	           		&nbsp;至&nbsp;
	           		<input name="orderEndDate" id="orderEndDate" value="${now}" type="text" class="short_txt" datatype="1,is_date,10" group="orderStartDate,orderEndDate">
	           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 'orderEndDate', false);"/>
                </td>
                <td width="10%"   align="right">服务商编码：</td>
                <td width="20%">
					<input type="text" class="middle_txt" id="orgCode" name="orgCode" />
				</td>
                <td width="10%"   align="right">订货单位名称：</td>
                <td width="20%">
					<input type="text" class="middle_txt" id="orgName" name="orgName" />
				</td>
            </tr>
            <tr>
                <td width="10%"   align="right">配件编码：</td>
                <td width="20%">
					<input type="text" class="long_txt" id="partOldCode" name="partOldCode" />
				</td>
				<td width="10%"   align="right">配件名称：</td>
                <td width="20%">
					<input type="text" class="middle_txt" id="partCname" name="partCname" />
				</td>
				<td width="10%"   align="right">配件件号：</td>
                <td width="20%">
					<input type="text" class="middle_txt" id="partCode" name="partCode" />
				</td>
            </tr>
            <tr>
                <td width="10%"   align="right">配件类型：</td>
                <td width="20%">
					<script type="text/javascript">
                    genSelBoxExp("partType", <%=Constant.PART_BASE_PART_TYPES%>, "", true, "long_sel", "", "false", '');
                </script>
				</td>
				<td width="10%"   align="right">销售单号：</td>
                <td width="20%">
					<input type="text" class="middle_txt" id="soCode" name="soCode" />
				</td>
				<td width="10%"   align="right">是否生成销售单：</td>
                <td width="20%">
					<select id="soFlag" name="soFlag" class="short_sel">
						<option value="" >--请选择--</option>
						<option value="1" >是</option>
						<option value="0" >否</option>
					</select>
				</td>
            </tr>
            <tr>
                <td width="10%"   align="right">订单单位省份：</td>
                <td width="20%">
					<input type="text" class="long_txt" id="province" name="province" />
				</td>
				<td width="10%"   align="right"></td>
                <td width="20%">
					
				</td>
				<td width="10%"   align="right"></td>
                <td width="20%">
					
				</td>
            </tr>
            
            <tr>
                <td colspan="6" align="center">
            	    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button"  value="查 询" />&nbsp;
                	<input name="BtnQuery" id="exportBtn" class="normal_btn" type="button"  value="导 出" />
                </td>
            </tr>
        </table>
    </div>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
    String error = request.getParameter("error");
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>预计到货查询</title>
    <script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
            var dt = new Date();
            var dStr = dt.format("yyyy-MM-dd");

            $("#foreCastBeginTime")[0].value=dStr;
            $("#foreCastEndTime")[0].value=dStr;
            __extQuery__(1);
        }

        Date.prototype.format = function(format)
        {
         var o = {
         "M+" : this.getMonth()+1, //month
         "d+" : this.getDate(),    //day
         "h+" : this.getHours(),   //hour
         "m+" : this.getMinutes(), //minute
         "s+" : this.getSeconds(), //second
         "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
         "S" : this.getMilliseconds() //millisecond
         }
         if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
         (this.getFullYear()+"").substr(4 - RegExp.$1.length));
         for(var k in o)if(new RegExp("("+ k +")").test(format))
         format = format.replace(RegExp.$1,
         RegExp.$1.length==1 ? o[k] :
         ("00"+ o[k]).substr((""+ o[k]).length));
         return format;
        }
    </script>
</head>
<body onunload='javascript:destoryPrototype()'>
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：
    当前位置：配件管理&gt; 采购订单管理&gt; 预计到货查询
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage"/>
    <input type="hidden" name="flag" id="flag"/>
    <input type="hidden" name="partId" id="partId"/>
    <input type="hidden" name="chkId" id="chkId" value="${chkId}"/>
    <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	    <table class="table_query">
	        <tr>
	            <td width="10%" class="right">配件编码：</td>
	            <td width="20%"><input name="PART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE"/></td>
	            <td width="10%" class="right">配件名称：</td>
	            <td width="20%"><input name="PART_CNAME" type="text" class="middle_txt" id="PART_CNAME"/></td>
	            <td width="10%" class="right">件号：</td>
	            <td width="20%"><input name="PART_CODE" type="text" class="middle_txt" id="PART_CODE"/></td>
	            </tr>
	        <tr>
	            <td width="10%" class="right">预计到货日期：</td>
	            <td width="30%">
	                <input name="foreCastBeginTime" id="foreCastBeginTime" value="" type="text" class="short_txt" datatype="1,is_date,10"
	                       group="foreCastBeginTime,foreCastEndTime" style="width:80px;"/>
	                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
	                       onclick="showcalendar(event, 'foreCastBeginTime', false);"/>
	                &nbsp;至&nbsp;
	                <input name="foreCastEndTime" id="foreCastEndTime" value="" type="text" class="short_txt" datatype="1,is_date,10"
	                       group="foreCastBeginTime,foreCastEndTime" style="width:80px;"/>
	                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
	                       onclick="showcalendar(event, 'foreCastEndTime', false);"/>
	            </td>
	        </tr>
	      <!--   <tr>
	        	 <td width="10%" class="right">计划单号：</td>
	             <td width="20%">
	                <input class="middle_txt" type="text" name="PLAN_CODE" id="PLAN_CODE"/>
	             </td>
	             <td width="10%" class="right">计划类型：</td>
	             <td width="20%">
	                <script type="text/javascript">
	                    genSelBoxExp("PLAN_TYPE", <%=Constant.PART_PURCHASE_PLAN_TYPE%>, "", true, "short_sel", "", "false", '');
	                </script>
	            </td>
	        </tr> -->
	        <tr>
	            <td class="center" colspan="6"><input name="BtnQuery" id="queryBtn" type="button" class="normal_btn"
	                                                  onclick="__extQuery__(1)" value="查 询"/>
	                &nbsp;<input name="button" type="button" class="normal_btn" onclick="exportForeCastExcel();"
	                             value="导出"/>
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
var title = null;
var url = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/queryForeCastPartInfo.json";
var columns = [
    {header: "序号", align:'center',renderer:getIndex},
    //{header: "计划单号", dataIndex: 'PLAN_CODE', align: 'center'},
    //{header: "计划类型", dataIndex: 'PLAN_TYPE', align: 'center', renderer: getItemValue},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "计划数量", dataIndex: 'PLAN_QTY', style: 'text-align:left'},
    {header: "最小包装量", dataIndex: 'BUY_MIN_PKG', align: 'center'},
    {header: "预计到货日期", dataIndex: 'FORECAST_DATE', align: 'center', renderer: formatDate}
];

//导出
function exportForeCastExcel() {
    fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/exportForeCastExcel.do";
    fm.target = "_self";
    fm.submit();
}

//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}
</script>
</div>
</body>
</html>
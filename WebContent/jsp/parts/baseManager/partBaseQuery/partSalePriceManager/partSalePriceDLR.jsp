<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();

%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=7">
<title>配件销售价格查询</title>
<script type="text/javascript">

$(function(){
	__extQuery__(1);
});

var myPage;

var url = "<%=contextPath%>/parts/baseManager/partBaseQuery/partSaleManager/PartSalePriceActon/queryPartSalePrice.json";

var title = null;

 var columns = [
     {header: "序号", align: 'center', renderer: getIndex},
     {header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:center'},
     {header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:center'},
//   {header: "件号", dataIndex: 'PART_CODE',  style: 'text-align:center'},
     {header: "适用车型", dataIndex: 'MODEL_NAME',  style: 'text-align:center'},
//     {header: "结算基地", dataIndex: 'PART_IS_CHANGHE', style: "text-align: center", renderer: getItemValue},
     {header: "${map.prciceName1}(含税)", dataIndex: 'SALE_PRICE1',  style: 'text-align:center'},
     {header: "${map.prciceName2}(含税)", dataIndex: 'SALE_PRICE2', style: 'text-align:center'},
     {header: "${map.prciceName3}(含税)", dataIndex: 'SALE_PRICE3', style: 'text-align:center'},
     {header: "${map.prciceName4}(含税)", dataIndex: 'SALE_PRICE4', style: 'text-align:center'},
//      {header: "${map.prciceName5}(含税)", dataIndex: 'SALE_PRICE5', style: 'text-align:center'}
//      {header: "${map.prciceName6}(含税)", dataIndex: 'SALE_PRICE6', style: 'text-align:center'}
//      {header: "修改日期", dataIndex: 'UPDATE_DATE', align:'center'},
//      {header: "修改人", dataIndex: 'NAME', align:'center'}
 ];


//导出销售价格信息
function loadPartPriceExcel() {
    fm.action = "<%=contextPath%>/parts/baseManager/partBaseQuery/partSaleManager/PartSalePriceActon/exportPartPriceExcelDLR.do";
	fm.submit();
}


</script>
    
</head>
<body >
<div class="wbox">
<form name="fm" id="fm" method="post">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：基础信息管理 &gt; 配件基础信息查询 &gt; 销售价格查询</div>
    <input type="hidden" name="orgType" id="orgType" value="DLR"/>
     <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
        <div class="form-body">
		    <table class="table_query">
		        <tr>
		            <td class="right">配件编码：</td>
		            <td><input class="middle_txt" type="text" name="PART_OLDCODE"/></td>
		            <td class="right">配件名称：</td>
		            <td><input class="middle_txt" type="text" name="PART_CNAME"/></td>
		           <!--  <td class="right">件号：</td>
		            <td><input class="middle_txt" type="text" name="PART_CODE"/></td> -->
		        </tr>
		        <tr>
		            <td class="center" colspan="6">
		                <input class="u-button u-query" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1);"/>
						<input class="u-button u-cancel" type="reset" value="重 置">
		               <input class="normal_btn" type="button" value="导 出" onclick="loadPartPriceExcel();"/>
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

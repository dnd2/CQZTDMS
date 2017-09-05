<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	
%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<title>配件价格查询</title>
<script type="text/javascript" >
	$(function(){
		__extQuery__(1);
	});
	
	var myPage;

	var url = "<%=contextPath%>/parts/baseManager/partBaseQuery/partSaleManager/PartSalePriceActon/queryPartSalePrice.json";
				
	var title = null;

	var columns = [
				{header: "序号",  style: 'text-align: center',renderer:getIndex},
				{header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align: center'},
				{header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align: center'},
                {header: "件号", dataIndex: 'PART_CODE',  style: 'text-align: center'},
              /*  {header: "结算基地", dataIndex: 'PART_IS_CHANGHE', style: "text-align: center", renderer: getItemValue},*/
                {header: '${map.prciceName1}(元)', dataIndex: 'SALE_PRICE1',  style: 'text-align: center'},
				{header: '${map.prciceName2}(元)', dataIndex: 'SALE_PRICE2',  style: 'text-align: center'},
				{header: '${map.prciceName3}(元)', dataIndex: 'SALE_PRICE3',  style: 'text-align: center'},
				{header: '${map.prciceName4}(元)', dataIndex: 'SALE_PRICE4',  style: 'text-align: center'},
				// {header: '${map.prciceName5}(元)', dataIndex: 'SALE_PRICE5',  style: 'text-align: center'},
// 				{header: '${map.prciceName6}(元)', dataIndex: 'SALE_PRICE6',  style: 'text-align: center'}
				//{header: '${map.prciceName7}(元)', dataIndex: 'SALE_PRICE7',  style: 'text-align: center'},
				<%--{header: '${map.prciceName5}(元)', dataIndex: 'SALE_PRICE5',  style: 'text-align: center'},--%>
				<%--{header: '${map.prciceName6}(元)', dataIndex: 'SALE_PRICE6',  style: 'text-align: center'},--%>
				<%--{header: '${map.prciceName7}(元)', dataIndex: 'SALE_PRICE7',  style: 'text-align: center'},--%>
				<%--{header: '${map.prciceName8}(元)', dataIndex: 'SALE_PRICE8',  style: 'text-align: center'},--%>
				<%--{header: '${map.prciceName9}(元)', dataIndex: 'SALE_PRICE9',  style: 'text-align: center'},--%>
				<%--{header: '${map.prciceName10}(元)', dataIndex: 'SALE_PRICE10',  style: 'text-align: center'},--%>
				<%--{header: '${map.prciceName11}(元)', dataIndex: 'SALE_PRICE11',  style: 'text-align: center'},--%>
				<%--{header: '${map.prciceName12}(元)', dataIndex: 'SALE_PRICE12',  style: 'text-align: center'},--%>
				<%--{header: '${map.prciceName13}(元)', dataIndex: 'SALE_PRICE13',  style: 'text-align: center'},--%>
				<%--{header: '${map.prciceName14}(元)', dataIndex: 'SALE_PRICE14',  style: 'text-align: center'},--%>
				<%--{header: '${map.prciceName15}(元)', dataIndex: 'SALE_PRICE15',  style: 'text-align: center'}--%>
			/*	{header: "修改日期", dataIndex: 'UPDATE_DATE',  style: 'text-align: center'},
				{header: "修改人", dataIndex: 'NAME',  style: 'text-align: center'}*/
	//			{id:'action',header: "操作",sortable: false,dataIndex: 'PRICE_ID',renderer:myLink , style: 'text-align: center'}
		      ];
  
	  
	//导出销售价格信息
	function loadPartPriceExcel(){
		fm.action = "<%=contextPath%>/parts/baseManager/partBaseQuery/partSaleManager/PartSalePriceActon/exportPartPriceExcelOEM.do";
		fm.submit();
	}
	
	function myLink(value,meta,record)
	{
  		return String.format("<a href=\"#\" onclick='viewPriceList(\""+value+"\")'>[查看价格列表]</a>");
	}
	
	//查看价格列表
	function viewPriceList(){
		fm.action='<%=contextPath%>/parts/baseManager/partBaseQuery/partSaleManager/PartSalePriceActon/viewPriceListInit.do';
		fm.submit();
	}
	
</script>
</head>
<body>
<div class="wbox">
	<form name="fm" id="fm" method="post" style="width:98%">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息查询 &gt; 配件价格查询 </div>
		<input type="hidden" name="orgType" id="orgType" value="OEM"/>
		<div class="form-panel">
			<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
		    <div class="form-body">
				<table class="table_query">
					<th colspan="6"></th>
					<tr>
						<td class="right">配件编码：</td>
						<td><input class="middle_txt" type="text"  name="PART_OLDCODE"  /></td>
						<td class="right" >配件名称：</td>
						<td><input class="middle_txt" type="text"  name="PART_CNAME" /></td>
						<td class="right">件号：</td>
						<td><input class="middle_txt" type="text" name="PART_CODE"  /></td>
					</tr>
					<tr>
						<td class="center" colspan="6" >
							<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1);"/>
							<input class="u-button" type="reset" value="重 置">
							<input class="u-button" type="button" value="导 出" onclick="loadPartPriceExcel();"/>
						</td>    
					</tr>
				</table>
			</div>
		</div>
		
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

	</form>
</div>	
</body>
</html>

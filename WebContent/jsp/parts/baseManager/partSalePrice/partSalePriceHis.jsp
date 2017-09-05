<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>配件销售价格维护</title>
</head>

<body onload="__extQuery__(1);enableAllStartBtn();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" name="priceId" id="priceId" value="${priceId }"/>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件销售价格维护 &gt; 销售价格变更详情 </div>
	</div>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<table width="100%">
		<tr>
			<td align="center"><input class="normal_btn" align="center"
				type="button" name="queryBtn" id="queryBtn" value="关闭"
				onclick="_hide()" /></td>
		</tr>
	</table>
</form>
<script type="text/javascript" >
	var myPage;
	var query = "json";
	
	var url = "<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePrice/queryModifyHis.json?query="+query;
				
	var title = null;

	var columns = [
	            {header: "序号",width:'5%',dataIndex: 'PRICE_ID', renderer:getIndex},
				{header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align: center'},
				{header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:center'},
				{header: "件号", dataIndex: 'PART_CODE',  style: 'text-align:center'},
				{header: "原调拨价有效开始日期 ~ 调拨价有效开始日期", dataIndex: 'PRICE_VALID_START_DATE',  style: 'text-align: center', renderer: formatDateStr},
				{header: "原调拨价有效结束日期 ~ 调拨价有效结束日期", dataIndex: 'PRICE_VALID_END_DATE',  style: 'text-align:center', renderer: formatDateStr},
				{header: "原促销价有效开始日期 ~ 促销价有效开始日期", dataIndex: 'SALE_PRICE_START_DATE',  style: 'text-align:center', renderer: formatDateStr},
				{header: "原促销价有效结束日期 ~ 促销价有效结束日期", dataIndex: 'SALE_PRICE_END_DATE',  style: 'text-align:center', renderer: formatDateStr},
				{header: '原${map.prciceName1}(元)', dataIndex: 'OLD_SALE_PRICE1',  style: 'text-align: center'},
				{header: "${map.prciceName1}(元)", dataIndex: 'SALE_PRICE1',  style: 'text-align:center'},
				{header: '原${map.prciceName2}(元)', dataIndex: 'OLD_SALE_PRICE2',  style: 'text-align: center'},
				{header: '${map.prciceName2}(元)', dataIndex: 'SALE_PRICE2',  style: 'text-align: center'},
				{header: '原${map.prciceName3}(元)', dataIndex: 'OLD_SALE_PRICE3',  style: 'text-align: center'},
				{header: '${map.prciceName3}(元)', dataIndex: 'SALE_PRICE3',  style: 'text-align: center'},
				{header: '原${map.prciceName4}(元)', dataIndex: 'OLD_SALE_PRICE4',  style: 'text-align: center'},
				{header: '${map.prciceName4}(元)', dataIndex: 'SALE_PRICE4',  style: 'text-align: center'},
				/*{header: "原计划价", dataIndex: 'OLD_SALE_PRICE3',  style: 'text-align:center'},
				{header: "计划价", dataIndex: 'SALE_PRICE3',  style: 'text-align:center'},
		 		{header: "原海运价", dataIndex: 'OLD_SALE_PRICE4',  style: 'text-align:center'},
				{header: "海运价", dataIndex: 'SALE_PRICE4',  style: 'text-align:center'},
				{header: "原空运价", dataIndex: 'OLD_SALE_PRICE5',  style: 'text-align:center'},
				{header: "空运价", dataIndex: 'SALE_PRICE5',  style: 'text-align:center'},
				{header: "原价格6", dataIndex: 'OLD_SALE_PRICE6',  style: 'text-align:center'},
				{header: "价格6", dataIndex: 'SALE_PRICE6',  style: 'text-align:center'},
				{header: "原价格7", dataIndex: 'OLD_SALE_PRICE7',  style: 'text-align:center'},
				{header: "价格7" ,dataIndex: 'SALE_PRICE7',  style: 'text-align:center'},
				{header: "原价格8", dataIndex: 'OLD_SALE_PRICE8',  style: 'text-align:center'},
				{header: "价格8", dataIndex: 'SALE_PRICE8',  style: 'text-align:center'},
		 		{header: "原价格9", dataIndex: 'OLD_SALE_PRICE9',  style: 'text-align:center'},
				{header: "价格9", dataIndex: 'SALE_PRICE9',  style: 'text-align:center'},
				{header: "原价格10", dataIndex: 'OLD_SALE_PRICE10',  style: 'text-align:center'}, 
				{header: "价格10", dataIndex: 'SALE_PRICE10',  style: 'text-align:center'},
				{header: "原价格11", dataIndex: 'OLD_SALE_PRICE11',  style: 'text-align:center'},
				{header: "价格11", dataIndex: 'SALE_PRICE11',  style: 'text-align:center'},
				{header: "原价格12", dataIndex: 'OLD_SALE_PRICE12',  style: 'text-align:center'},
				{header: "价格12", dataIndex: 'SALE_PRICE12',  style: 'text-align:center'},
				{header: "原价格13", dataIndex: 'OLD_SALE_PRICE13',  style: 'text-align:center'},
				{header: "价格13", dataIndex: 'SALE_PRICE13',  style: 'text-align:center'},
		 		{header: "原领用价", dataIndex: 'OLD_SALE_PRICE14',  style: 'text-align:center'},
				{header: "领用价", dataIndex: 'SALE_PRICE14',  style: 'text-align:center'},
				{header: "原价格15", dataIndex: 'OLD_SALE_PRICE15',  style: 'text-align:center'}, 
				{header: "价格15", dataIndex: 'SALE_PRICE15',  style: 'text-align:center'},*/
				{header: "修改人", dataIndex: 'NAME',  style: 'text-align:center'},
				{header: "修改日期", dataIndex: 'CREATE_DATE',  style: 'text-align:center'}
		      ];

	function formatDateStr(value, meta, record) {
		var html = "";
		if(this.dataIndex == "PRICE_VALID_START_DATE"){
			//	调拨价有效开始日期
			var oldDate = record.data.OLD_PRICE_VALID_START_DATE;
			var newDate = record.data.PRICE_VALID_START_DATE;
			if(!oldDate || oldDate == null){
				oldDate = '';
			}
			if (!newDate || newDate == null){
				newDate = '';
			}
			
			html =  oldDate + " ~ " + newDate;
		}if(this.dataIndex == "PRICE_VALID_END_DATE"){
			//	调拨价有效结束日期
			var oldDate = record.data.OLD_PRICE_VALID_END_DATE;
			var newDate = record.data.PRICE_VALID_END_DATE;
			if(!oldDate || oldDate == null){
				oldDate = '';
			}
			if (!newDate || newDate == null){
				newDate = '';
			}
			html =  oldDate + " ~ " + newDate;
			
		}if(this.dataIndex == "SALE_PRICE_START_DATE"){
			//	促销价有效开始日期
			var oldDate = record.data.OLD_SALE_PRICE_START_DATE;
			var newDate = record.data.SALE_PRICE_START_DATE;
			if(!oldDate || oldDate == null){
				oldDate = '';
			}
			if (!newDate || newDate == null){
				newDate = '';
			}
			html =  oldDate + " ~ " + newDate;
			
		}if(this.dataIndex == "SALE_PRICE_END_DATE"){
			//	促销价有效结束日期
			var oldDate = record.data.OLD_SALE_PRICE_END_DATE;
			var newDate = record.data.SALE_PRICE_END_DATE;
			if(!oldDate || oldDate == null){
				oldDate = '';
			}
			if (!newDate || newDate == null){
				newDate = '';
			}
			html =  oldDate + " ~ " + newDate;
		}
		return html;
	}

</script>
</body>
</html>

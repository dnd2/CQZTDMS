<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配额常规订单转化率查询</title>
<script type="text/javascript">
	function doInit(){
		showDate();
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单查询 > 配额常规订单转化率查询</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="95%">
  	<TR>
      <TD align=right nowrap width="40%">选择周度：</TD>
      <td  nowrap>
      	<select id="startYear"  name="orderYear" class="min_sel" style="width:55px" onchange="showDate();">
       		<c:forEach items="${years}" var="po">
       			<c:choose>
					<c:when test="${po == curYear}">
						<option value="${po}" selected="selected">${po}</option>
					</c:when>
					<c:otherwise>
						<option value="${po}">${po}</option>
					</c:otherwise>
				</c:choose> 
			</c:forEach>
         </select>
      	 年
      	 <select  id="startWeek" name="orderWeek" class="min_sel" style="width:50px" onchange="showDate();">
       		<c:forEach items="${weeks}" var="po">
				<c:choose>
					<c:when test="${po == curWeek}">
						<option value="${po}" selected="selected">${po}</option>
					</c:when>
					<c:otherwise>
						<option value="${po}">${po}</option>
					</c:otherwise>
				</c:choose> 
			</c:forEach>
	  	 </select>  
	  	 周
	  	 &nbsp;至&nbsp;
	  	 <select id="endYear"  name="endYear" class="min_sel" style="width:55px" onchange="showDate();">
       		<c:forEach items="${years}" var="po">
       			<c:choose>
					<c:when test="${po == curYear}">
						<option value="${po}" selected="selected">${po}</option>
					</c:when>
					<c:otherwise>
						<option value="${po}">${po}</option>
					</c:otherwise>
				</c:choose> 
			</c:forEach>
         </select>
      	 年
      	 <select  id="endWeek" name="endWeek" class="min_sel" style="width:50px" onchange="showDate();">
       		<c:forEach items="${weeks}" var="po">
				<c:choose>
					<c:when test="${po == curWeek}">
						<option value="${po}" selected="selected">${po}</option>
					</c:when>
					<c:otherwise>
						<option value="${po}">${po}</option>
					</c:otherwise>
				</c:choose> 
			</c:forEach>
	  	 </select>  
	  	 周
	  	 <span id="dateInfo" class="innerHTMLStrong"></span>
	  	 <span id="endDateInfo" class="innerHTMLStrong"></span>
	  </td>
    </TR>
    <tr>
	  <td align="right" class="table_list_th">选择业务范围：</td>
	  <td class="table_list_th">
		  <select name="areaId" class="short_sel">
      	  	<option value="">-请选择-</option>
			<c:forEach items="${areaList}" var="po">
				<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
			</c:forEach>
	      </select>
	  </td>
	  <td align="left" nowrap="nowrap">&nbsp;</td>
	</tr>
	<tr>
      <td align="right" nowrap >选择物料组：</td>
      <td align="left" nowrap >
      	<input type="text" class="middle_txt" name="groupCode" size="15"  value="" id="groupCode" />
      	<input type="hidden" name="groupName" size="20" id="groupName" value="" />
		<input name="button1" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','groupName','true','4');" value="..." />
		<input class="cssbutton" type="button" value="清空" onclick="clrTxt('groupCode');"/>
      </td>
      <td>&nbsp; </td> 
    </tr> 
	<TR>
	  <td align=right nowrap>&nbsp;</td>
      <td width="17%" align="left" nowrap>
      	  <input name="button2" type=button class="cssbutton" onClick="totalQuery();" value="汇总查询">
          <input name="button2" type=button class="cssbutton" onClick="detailQuery();" value="明细查询">
          <input name="button2" type=button class="cssbutton" onClick="totalDownload();"value="汇总下载">
          <input name="button2" type=button class="cssbutton" onClick="detailDownload();"value="明细下载">
      </td>
	  <TD align=left nowrap>&nbsp;</TD>
	</TR>
  </table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">
	var myPage;
	//查询路径
	var url;
				
	var title = null;

	var columns;		       
	
	function totalQuery(){
		url = "<%=request.getContextPath()%>/sales/ordermanage/orderquery/DealerQuotaTransOrderRateQuery/dealerQuotaTransOrderRateTotalQuery.json";
		columns = [
				{header: "周次", dataIndex: 'QUOTA_DATE', align:'center'},
				{header: "车系名称", dataIndex: 'SERIES_NAME', align:'center'},
				{header: "车型名称", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "物料组代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "配置名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "配额数量", dataIndex: 'QUOTA_AMT', align:'center'},
				{header: "订单最小提报量", dataIndex: 'MIN_AMOUNT', align:'center'},
				{header: "提报数量", dataIndex: 'YTB', align:'center'},
				{header: "审核数量", dataIndex: 'CHECK_AMOUNT', align:'center'},
				{header: "发运申请数量", dataIndex: 'REQ_AMOUNT', align:'center'},
				{header: "保留资源数量", dataIndex: 'RESERVE_AMOUNT', align:'center'},
				{header: "开票数量", dataIndex: 'DELIVERY_AMOUNT', align:'center'},
				{header: "差异数量", dataIndex: 'CY', align:'center'},
				{header: "提报率", dataIndex: 'WCL', align:'center'},
				{header: "执行率", dataIndex: 'ZXL', align:'center'},
				{header: "常规订单占比", dataIndex: 'ZBL', align:'center'}
		      ];
		__extQuery__(1);
	}
	
	function detailQuery(){
		url = "<%=request.getContextPath()%>/sales/ordermanage/orderquery/DealerQuotaTransOrderRateQuery/dealerQuotaTransOrderRateDetailQuery.json";
		columns = [
				{header: "周次", dataIndex: 'QUOTA_DATE', align:'center'},
				{header: "大区名称", dataIndex: 'ORG_NAME', align:'center'},
				{header: "省份",dataIndex: 'PROVINCE_ID',align:'center', renderer:getRegionName},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "配额数量", dataIndex: 'QUOTA_AMT', align:'center'},
				{header: "订单最小提报量", dataIndex: 'MIN_AMOUNT', align:'center'},
				{header: "提报数量", dataIndex: 'YTB', align:'center'},
				{header: "审核数量", dataIndex: 'CHECK_AMOUNT', align:'center'},
				{header: "发运申请数量", dataIndex: 'REQ_AMOUNT', align:'center'},
				{header: "保留资源数量", dataIndex: 'RESERVE_AMOUNT', align:'center'},
				{header: "开票数量", dataIndex: 'DELIVERY_AMOUNT', align:'center'},
				{header: "差异数量", dataIndex: 'CY', align:'center'},
				{header: "提报率", dataIndex: 'WCL', align:'center'},
				{header: "执行率", dataIndex: 'ZXL', align:'center'},
				{header: "常规订单占比", dataIndex: 'ZBL', align:'center'}
		      ];
		__extQuery__(1);
	}
	
	function myLink(value,meta,record){
		return String.format("<a href='#'>"+value+"</a>");
	}
	
	function totalDownload(){
		$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/orderquery/DealerQuotaTransOrderRateQuery/dealerQuotaTransOrderRateTotalExport.do";
		$('fm').submit();
	}
	
	function detailDownload(){
		$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/orderquery/DealerQuotaTransOrderRateQuery/dealerQuotaTransOrderRateDetailExport.do";
		$('fm').submit();
	}

	function showDate(){
		var startYear = document.getElementById("startYear").value;
		var startWeek = document.getElementById("startWeek").value;

		makeNomalFormCall('<%=request.getContextPath()%>/sales/showDate/ShowDateInfo/showDate.json?startYear='+startYear+'&startWeek='+startWeek,showDateInfo,'fm');
	}
	function showDateInfo(json){
		if(json.returnValue == '1'){
			document.getElementById("dateInfo").innerHTML = json.showDate;
		}
	}
	
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
</script>
</body>
</html>

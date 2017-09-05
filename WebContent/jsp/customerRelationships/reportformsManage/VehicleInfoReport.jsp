<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>全国各经销商商品车统计</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();
   		dtlQuery();
	}
</script>
</head>
<body >
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 报表管理 &gt;全国各经销商商品车统计</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />全国各经销商商品车统计</th>
			
			<tr>		<td class="tblopt" nowrap="nowrap">
			<div align="right">选择经销商：</div>
		</td>
			<td>
				<input type="hidden" class="middle_txt" style="width:100px" name="dealerCode" readonly="readonly" size="2" value="" id="dealerCode"/>
				<input type="text" class="middle_txt" style="width:100px" name="dealerName" readonly="readonly" size="20" value="" id="dealerName"/>
				<input name="dealerBtn" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','dealerIds','true','','','','<%=Constant.DEALER_TYPE_DVS %>','dealerName');" value="..." />
				<input type="hidden" name="dealerIds" id="dealerIds" value="" />
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');clrTxt('dealerIds');clrTxt('dealerName');"/>
			</td>
				<td align="right" nowrap="true">日期：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="dateStart" name="dateStart" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" name="button" value=" " onclick="showcalendar(event, 'dateStart', false);" type="button"/>
                	    至
                    <input class="short_txt" id="dateEnd" name="dateEnd" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" name="button" value=" " onclick="showcalendar(event, 'dateEnd', false);" type="button"/>
				</td>
				<td align="left">
					<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="dtlQuery();" />
					<input id="downExcel" name="downExcel" type="button" value="导出Excel" class="normal_btn" onclick="downExcelQuery();" />
        		</td>
			</tr>

		</table>
		
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	</form>
<script type="text/javascript">
	function downExcelQuery(){
		fm.action = '<%=contextPath%>/customerRelationships/reportformsManage/IncomingCallDetailReportforms/vehicleInfoReportExcel.do';
		fm.submit();
	}
	var myPage;
	var title = null;
	var url;
	var columns;
	function dtlQuery() {
		var dateStart = document.getElementById("dateStart").value;
		var dateEnd = document.getElementById("dateEnd").value;
		url = "<%=contextPath%>/customerRelationships/reportformsManage/IncomingCallDetailReportforms/VehicleInfoReportQuery.json";
		if(dateEnd=="" && dateStart==""){
			 columns = [
						{header: "大区", dataIndex: 'ROOT_ORG_NAME', style: 'text-align:left'},
						{header: "经销商代码", dataIndex: 'DEALER_CODE', style: 'text-align:left'},
						{header: "省份", dataIndex: 'ORG_NAME', style: 'text-align:left'},
						{header: "地级市",dataIndex: 'REGION_NAME',style: 'text-align:left'},
						{header: "经销商全称", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
						{header: "经销商简称", dataIndex: 'DEALER_SHORTNAME', style: 'text-align:left'},
						{header: "累计发车(辆)", dataIndex: 'LEIJIFACHE', style: 'text-align:right'},
						{header: "累计在途(辆)", dataIndex: 'LEIJIZAITU', style: 'text-align:right'},
						{header: "累计库存(辆)", dataIndex: 'LEIJIKUCUN', style: 'text-align:right'},
						{header: "累计终端(辆)", dataIndex: 'LEIJIZHONGDUAN', style: 'text-align:right'},
						{header: "当日终端", dataIndex: 'DANGTIANSHIXIAO', style: 'text-align:right'},
						{header: "最近六天内已发车(辆)", dataIndex: 'SEND_NUM', style: 'text-align:right'},
						{header: "经销商订单(辆)", dataIndex: 'CHK_NUM', style: 'text-align:right'}
// 						{header: "账上累计资金总额(元)", dataIndex: 'AMOUNT', style: 'text-align:right',renderer:formatCurrency},
// 						{header: "账上当前余额资金总额(元)", dataIndex: 'YUE_AMOUNT', style: 'text-align:right',renderer:formatCurrency}
				      ];
		}else{
			 columns = [
						{header: "大区", dataIndex: 'ROOT_ORG_NAME', style: 'text-align:left'},
						{header: "经销商代码", dataIndex: 'DEALER_CODE', style: 'text-align:left'},
						{header: "省份", dataIndex: 'ORG_NAME', style: 'text-align:left'},
						{header: "地级市",dataIndex: 'REGION_NAME',style: 'text-align:left'},
						{header: "经销商全称", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
						{header: "经销商简称", dataIndex: 'DEALER_SHORTNAME', style: 'text-align:left'},
						{header: "累计发车(辆)", dataIndex: 'LEIJIFACHE', style: 'text-align:right'},
						{header: "累计在途(辆)", dataIndex: 'LEIJIZAITU', style: 'text-align:right'},
						{header: "累计库存(辆)", dataIndex: 'LEIJIKUCUN', style: 'text-align:right'},
						{header: "累计终端(辆)", dataIndex: 'LEIJIZHONGDUAN', style: 'text-align:right'},
						{header: "当日终端", dataIndex: 'DANGTIANSHIXIAO', style: 'text-align:right'},
						{header: dateStart+"至"+dateEnd+"已发车(辆)", dataIndex: 'SEND_NUM', style: 'text-align:right'},
						{header: "经销商订单(辆)", dataIndex: 'CHK_NUM', style: 'text-align:right'}
// 						{header: "账上累计资金总额(元)", dataIndex: 'AMOUNT', style: 'text-align:right',renderer:formatCurrency},
// 						{header: "账上当前余额资金总额(元)", dataIndex: 'YUE_AMOUNT', style: 'text-align:right',renderer:formatCurrency}
				      ];
		}
	
	__extQuery__(1);
	}
	function clrTxt(value){
		document.getElementById(value).value = "";
	}
</script>
</body>
</html>
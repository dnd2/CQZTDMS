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
<title>配件样本申报结算情况表</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	function AlertExcelInfo(){
		
		var info = '<%=request.getAttribute("DataLonger")%>';
		if(info !=null && info!="" && info != "null"){
			MyAlert(info);
		}
	}
</script>
</head>
<body onload="AlertExcelInfo();">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表 &gt;配件样本申报结算情况表</div>
	<form method="post" name = "fm" id="fm">
		<input type="hidden" id="importantLevelsH" name="importantLevelsH">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />配件样本申报结算情况表</th>
			
			<tr>
				<td align="right" nowrap="true">服务站代码：</td>
				<td align="left" nowrap="true">
					<input type="text" id="dealerCode" name="dealerCode">
				</td>
				<td align="right" nowrap="true">服务站名称：</td>
				<td align="left" nowrap="true">
					<input type="text" id="dealerName" name="dealerName">
				</td>			
			</tr>

			<tr>
				<td align="right" nowrap="true">配件代码：</td>
				<td align="left" nowrap="true">
					<input type="text" id="partCode" name="partCode">
				</td>
				<td align="right" nowrap="true">配件名称：</td>
				<td align="left" nowrap="true">
					<input type="text" id="partName" name="partName">
				</td>
			</tr>

			<tr>
				<td align="right" nowrap="true">结算基地：</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
						genSelBoxExp("yieldly",<%=Constant.PART_IS_CHANGHE%>,"",true,"short_sel","","false",'');
					</script>
				</td>
				<td align="right" nowrap="true">结算上报日期：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="dateStart" name="dateStart" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateStart', false);" type="button"/>
                	    至
                    <input class="short_txt" id="dateEnd" name="dateEnd" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateEnd', false);" type="button"/>
				</td>
			</tr>
			
			<tr>
				<td colspan="4" align="center">
					<input align="right" class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="search();" />
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
		fm.action = '<%=contextPath%>/report/service/PartReportForms/partReportFormsExcel.do';
		fm.submit();
	}
	
	function search(){
		
		__extQuery__(1);
	}
	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/report/service/PartReportForms/queryPartReportForms.json";
				
	var title = null;
	var columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "服务商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "服务商名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_NAME',align:'center'},
				{header: "换上件数量",dataIndex: 'QUANTITY',align:'center'}				
		      ];

</script>
</body>
</html>
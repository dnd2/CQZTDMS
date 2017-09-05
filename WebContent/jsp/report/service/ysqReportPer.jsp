<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="java.util.*" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>预授权申请明细报表</title>
<%
	String contextPath = request.getContextPath();
List<Map<String, Object>> list = (List<Map<String, Object>>)request.getAttribute("list");
System.out.println(list.size()+"-----------");
%>
</head>
<body>
<div id="loader" style='position: absolute; z-index: 200; background: #FFCC00; padding: 1px; top: 4px; display: none; display: none;'></div>
<div class="navigation">
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;预授权申请明细报表</div>
<form method="post" name="fm" id="fm">
<table class="table_query">
	<tr>
		<td width="15%" align="right">VIN：</td>
		<td width="30%" align="left">
			<input type="text" name="vin" class="middle_txt" id="vin" value="${vin }"/>
		</td>
		<td width="15%" align="right">车型：</td>
		<td width="30%" align="left">
			<input type="text" name="model_name" class="middle_txt" value="${ model_name}" id="model_name"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="right">服务站简称：</td>
		<td width="30%" align="left">
			<input type="text" name="dealer_name" class="middle_txt" value="${dealer_name }" id="dealer_name"/>
		</td>
		<td width="15%" align="right">小区：</td>
		<td width="30%" align="left">
			<select class="short_sel" id="small_org" name="small_org" value="${small_org }">
				<option value="">--请选择--</option>
				<%for(int i=0;i<list.size();i++){%>
					<option value="<%=list.get(i).get("ORG_ID") %>"><%=list.get(i).get("ORG_NAME") %></option>
				<%} %>
			</select>
		</td>
	</tr>
	<tr>
		<td width="15%" align="right">主因件名称：</td>
		<td width="30%" align="left">
			<input type="text" name="part_name" value="${part_name }" class="middle_txt" id="part_name"/>
		</td>
		<td width="15%" align="right">发动机：</td>
		<td width="30%" align="left">
			<input type="text" name="engine_no" value="${engine_no }" class="middle_txt" id="engine_no"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="right">预授权申请时间：</td>
		<td align="left" nowrap="true">
			<input name="bDate" type="text" class="short_time_txt" id="bDate" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bDate', false);" />  	
             &nbsp;至&nbsp; <input name="eDate" type="text" class="short_time_txt" id="eDate" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'eDate', false);" /> 
		</td>	
		<td align="center" colspan="2">
			<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询" onclick="queryPer();" />&nbsp;
		    <input class="normal_btn" type="button" id="queryBtn2" name="button1" value="下载" onclick="exportExcel();" />
		</td>
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> <jsp:include
	page="${contextPath}/queryPage/pageDiv.html" /></form>
<script type="text/javascript">
	var myPage;

	var url = "<%=contextPath%>/report/service/ClaimReport/ysqReportDetailQuery.json?type=0";
				
	var title = null;

	var columns = [
				{header: "序号", renderer:getIndex, align:'center'},
				{header: "工单号", dataIndex: 'RO_NO', align:'center'},
				{header: "预授权单号", dataIndex: 'FO_NO', align:'center'},
				{header: "地区", dataIndex: 'ORG_NAME', align:'center'},
				{header: "服务站简称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "用户姓名", dataIndex: 'CTM_NAME', align:'center'},
				{header: "联系电话", dataIndex: 'MAIN_PHONE', align:'center'},
				{header: "车型", dataIndex: 'MODE_CODE', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
				{header: "生产日期", dataIndex: 'PRODUCT_TIME', align:'center'},
				{header: "购买日期", dataIndex: 'PURCHASED_TIME', align:'center'},
				{header: "单据里程数", dataIndex: 'IN_MILEAGE', align:'center'},
				{header: "故障名称", dataIndex: 'MAL_NAME', align:'center'},
				{header: "处理方法", dataIndex: 'REPAIR_METHOD', align:'center'},
				{header: "主因件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "上报日期", dataIndex: 'APPLY_TIME', align:'center'},
				{header: "技术员", dataIndex: 'DQ_NAME', align:'center'},
				{header: "技术员审核状态", dataIndex: 'DQ_RESULT', align:'center'},
				{header: "技术员审核时间", dataIndex: 'DQ_TIME', align:'center'},
				{header: "主任", dataIndex: 'ZR_NAME', align:'center'},
				{header: "主任审核状态", dataIndex: 'ZR_RESULT', align:'center'},
				{header: "处长", dataIndex: 'CZ_NAME', align:'center'},
				{header: "处长审核状态", dataIndex: 'CZ_RESULT', align:'center'},
				{header: "副总", dataIndex: 'FZ_NAME', align:'center'},
				{header: "副总审核状态", dataIndex: 'FZ_RESULT', align:'center'},
				{header: "批准日期", dataIndex: 'AUDIT_TIME', align:'center'},
				{header: "工时费", dataIndex: 'LABOUR_AMOUNT', align:'center'},
				{header: "材料费", dataIndex: 'REPAIR_PART_AMOUNT', align:'center'},
				{header: "费用合计", dataIndex: 'ALL_MOUNT', align:'center'}
		      ];
		            
	function exportExcel(){
	if($('vin').value==""&&$('model_name').value==""&&$('dealer_name').value==""&&$('small_org').value==""
	&&$('part_name').value==""&&$('engine_no').value==""&&$('bDate').value==""&&$('eDate').value==""){
		if(confirm("你未选择任何条件,可能会导致数据量过大而失败.继续下载?")){
		exportExcelDo();}
	}else{
	exportExcelDo();
	}
	}
	function exportExcelDo(){
	$('queryBtn2').disabled=true;
		fm.action = "<%=contextPath%>/report/service/ClaimReport/ysqReportDetailQuery.do";
		fm.submit();
}
	loadcalendar();
	 function   showMonthFirstDay()     
  {     
	  var   Nowdate=new   Date();     
	  var   MonthFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth(),1);     
	  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
  }     
  function   showMonthLastDay()     
  {     
	  var   Nowdate=new   Date();     
	  var   MonthNextFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
	  var   MonthLastDay=new   Date(MonthNextFirstDay-86400000);     
	  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
  }     
 // $('CREATE_DATE_STR').value=showMonthFirstDay();
  $('bDate').value=showMonthFirstDay();
  $('eDate').value=showMonthLastDay();
  function queryPer(){
	var star = $('bDate').value;
	var end = $('eDate').value;
	  if(star==""||end ==""){
	  	MyAlert("查询时间必须选择");
	 	 return false;
	  }else if(star>end){
	  	MyAlert("开始时间不能大于结束时间");
	  	return false;
	  }else {
	   var s1 = star.replace(/-/g, "/");
		var s2 = end.replace(/-/g, "/");
		var d1 = new Date(s1);
		var d2 = new Date(s2);
		var time= d2.getTime() - d1.getTime();
		var days = parseInt(time / (1000 * 60 * 60 * 24));
		if(days>=93){
		MyAlert("时间跨度不能超过3个月");
	  		return false;
		}
		$('queryBtn2').disabled=false;
	 	 __extQuery__(1);
	  }
	}
</script>
</body>
</html>
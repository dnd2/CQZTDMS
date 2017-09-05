<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>打印收票报表</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />当前位置：售后服务管理&gt;索赔结算管理&gt;打印收票报表
</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="nowrap">汇总单号：</td>
		<td>
			<input type="text" name="report_code" id="report_code" class="middle_txt"/>
		</td>
		<td align="right" nowrap="nowrap">财务人员：</td>
		<td>
			<input type="text" name="financeName" id="financeName" class="middle_txt"/>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">经销商代码：</td>
		<td>
			<input type="text" name="dealerCode" id="dealerCode" class="middle_txt"/>
		</td>
		<td align="right" nowrap="nowrap">经销商名称：</td>
		<td>
			<input type="text" name="dealerName" id="dealerName" class="middle_txt"/>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">收票日期：</td>
		<td align="left" nowrap="nowrap">
			<input type="text" name="bDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="eDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
		</td>
		<td align="right" nowrap="nowrap">签收人：</td>
		<td>
			<input type="text" name="name" id="person" class="middle_txt"/>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">挂账日期：</td>
		<td align="left" nowrap="nowrap">
			<input type="text" name="gzDate" id="t3" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4"  hasbtn="true" callFunction="showcalendar(event, 't3', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="gzDate1" id="t4" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4" hasbtn="true" callFunction="showcalendar(event, 't4', false);"/>
		</td>
		<td align="right" nowrap="nowrap"></td>
		<td></td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
			<input type="button" class="normal_btn" value="打印" onclick="goPrint(1)"/>
			<input type="button" class="normal_btn" value="导出" onclick="printExport()"/>
			<input type="button" class="long_btn" value="按收票时间打印" onclick="goPrint(2)"/>
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
		var myPage;
		var url = '<%=contextPath%>/claim/authorization/BalanceMain/printPorterQuery.json';
		var title = null;
		
		var columns = [
					{header: "序号",align:'center',renderer:getIndex},
					{header: "汇总单号",dataIndex: 'REPORT_CODE',align:'center'},
					{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
					//{header: "维修开始时间",dataIndex: 'START_DATE',align:'center'},	
					//{header: "维修结束时间",dataIndex: 'END_DATE',align:'center'},			
					{header: "省份",dataIndex: 'REGION_NAME',align:'center'},
					{header:'发票号码',dataIndex:'INVOICE_CODE',align:'center'},
					{header: "发票金额",dataIndex: 'AMOUNT',align:'center'}, 
					{header: "税率",dataIndex: 'TAX_RATE',align:'center'},
					{header:'收票时间',dataIndex:'AUTH_TIME',align:'center'},
					{header:'挂账时间',dataIndex:'GZTIME',align:'center'},
					//{header:'财务人员',dataIndex:'AUTH_PERSON_NAME',align:'center'},
					{header:'签收人',dataIndex:'A_NAME',align:'center'},
					{header:'签名栏',dataIndex:'',align:'center'}
			      ];
	function goPrint(value){
		if(submitForm('fm')==false)return;;
		var b = $('t1').value ;
		var e = $('t2').value ;
		var name = $('person').value ;
		//var url = '<%=contextPath%>/claim/authorization/BalanceMain/printPorter.do?bDate='+b+'&eDate='+e+'&name='+name+'&flag='+value ;
		fm.target="_blank";
		fm.action = '<%=contextPath%>/claim/authorization/BalanceMain/printPorter.do?&flag='+value ;
		fm.submit();
	}

	

	function printExport(){
		fm.action = '<%=contextPath%>/claim/authorization/BalanceMain/exportExcel.do' ;
		fm.submit();

	}
	function   showMonthFirstDay()     
	{     
		  var   Nowdate=new   Date();     
		  var   MonthFirstDay=new   Date(Nowdate.getYear(),0);     
		  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
	}     
	function   showMonthLastDay()     
	{     
		  var   Nowdate=new   Date();     
		  var   MonthNextFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
		  var   MonthLastDay=new   Date(MonthNextFirstDay-86400000);     
		  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
	}
	$('t1').value=showMonthFirstDay();
	$('t2').value=showMonthLastDay();
</script>
</body>
</html>
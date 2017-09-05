<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%String contextPath = request.getContextPath();%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>结算室收票状态明细</title>
	<script type="text/javascript">
	    function doInit(){
		   loadcalendar();
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />当前位置：售后服务管理&gt;索赔结算管理&gt;结算室收票状态明细
</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="nowrap">汇总单号：</td>
		<td>
			<input type="text" name="report_code" id="report_code" class="middle_txt"/>
		</td>
		<td align="right" nowrap="nowrap">收票状态：</td>
		<td>
			<script type="text/javascript">
				genSelBoxExp("status",<%=Constant.TAXABLE_SERVICE_SUM%>,"",true,"short_sel","","true",'<%=Constant.TAXABLE_SERVICE_SUM_FANCE%>,<%=Constant.TAXABLE_SERVICE_SUM_FANCE3%>,<%=Constant.TAXABLE_SERVICE_SUM_FANCE4%>');
		    </script>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">收票日期：</td>
		<td align="left" nowrap="nowrap">
			<input type="text" name="bDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="eDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
		</td>
		<td align="right" nowrap="nowrap">汇总开票制单日期：</td>
		<td align="left" nowrap="nowrap">
			<input type="text" name="gzDate" id="t3" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4"  hasbtn="true" callFunction="showcalendar(event, 't3', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="gzDate1" id="t4" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4" hasbtn="true" callFunction="showcalendar(event, 't4', false);"/>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">经销商代码：</td>
		<td>
			<input type="text" name="dealerCode" id="dealerCode" class="middle_txt"/>
		</td>
		<td align="right" nowrap="nowrap">生产基地：</td>
		<td>
			<script type="text/javascript">
				genSelBoxExp("yielyld",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"<%=Constant.SERVICEACTIVITY_CAR_YIELDLY_01%>",false,"short_sel","","true",'');
		    </script>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">收票人：</td>
		<td>
			<input type="text" name="sp" id="sp" class="middle_txt"/>
		</td>
		<td align="right" nowrap="nowrap"></td>
		<td></td>
	</tr>
	<!-- 
	<tr>
		<td align="right" nowrap="nowrap">经销商代码：</td>
		<td align="left" nowrap="nowrap">
			<input class="long_txt" id="dealerCode"  name="dealerCode" type="text"/>
            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>
		</td>
	</tr>
	 -->
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
			<input type="button" class="normal_btn" value="导出" onclick="printExport()"/>
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
		var myPage;
		var url = '<%=contextPath%>/claim/authorization/BalanceMain/queryBalanceDetailQuery.json';
		var title = null;
		
		var columns = [
					{header: "序号",align:'center',renderer:getIndex},
					{header: "分销中心",dataIndex: 'ROOT_ORG_NAME',align:'center'},
					{header: "省份",dataIndex: 'REGION_NAME',align:'center'},
					{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "开票单位名称",dataIndex: 'NAME',align:'center'},
					{header: "汇总单号",dataIndex: 'REPORT_CODE',align:'center'},
					{header: "生产基地",dataIndex: 'YIELDLY',align:'center'},
					{header: "金额",dataIndex: 'AUTH_AMOUNT',align:'center'},
					{header: "收票状态",dataIndex: 'AUTH_STATUS',align:'center'},
					{header:'收票时间',dataIndex:'AUTH_TIME',align:'center'},
					{header:'收票人',dataIndex:'AUTH_PERSON_NAME',align:'center'}
			      ];
	function printExport(){
		fm.action = '<%=contextPath%>/claim/authorization/BalanceMain/exportExcel1.do' ;
		fm.submit();

	}
	function showMonthFirstDay(){     
		  var Nowdate=new   Date();     
		  var MonthFirstDay=new Date(Nowdate.getYear(),0);     
		  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
	}     
	function showMonthLastDay(){     
		  var Nowdate=new   Date();     
		  var MonthNextFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
		  var MonthLastDay=new   Date(MonthNextFirstDay-86400000);     
		  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
	}
	//$('t1').value=showMonthFirstDay();
	//$('t2').value=showMonthLastDay();
	$('t3').value=showMonthFirstDay();
	$('t4').value=showMonthLastDay();
</script>
</body>
</html>
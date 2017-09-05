<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>财务挂账报表</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onload="loadcalendar();">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;轿车售后索赔报表&gt;财务挂账报表</div>
  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">
		<tr>
			<td align="right" width="10%">经销商代码：</td>
			<td width="20%"><input type="text" class="middle_txt" name="dealerCode"/></td>
			<td align="right" width="10%">经销商名称：</td>
			<td width="20%"><input type="text" class="middle_txt" name="dealerName"/></td>
		</tr>
		<tr>
			<td align="right">生产基地：</td>
			<td>
				<script type="text/javascript">
					genSelBoxExp("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",false,"short_sel",'',"false",'');
				</script>
			</td>
			<td align="right">年份：</td>
			<td><input type="text" datatype="0,is_digit,4" class="short_txt" id="year" name="year"/></td>
		</tr>
       <tr>
         <td align="center" colspan="4">
         	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="__extQuery__(1)"/>&nbsp;
         	<input type="button" id="queryBtn" value="下载" class="normal_btn" onclick="goDownload()"/>
         </td>
         </tr>
       </table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

</form>
<script type="text/javascript" >
	var date = new Date();
	var year = date.getYear();
	$('year').value = year ;
	
	var myPage;
	var url = "<%=contextPath%>/report/jcafterservicereport/FinancialCreditReport/mainQuery.json";
				
	var title = null;

	var columns = [
				{header: "序号",renderer:getIndex, align:'center'},
				{header: '经销商代码',dataIndex:'DEALER_CODE',align:'center'},
				{header: '经销商名称',dataIndex:'DEALER_NAME',align:'center'},
				{header: '一月结算金额',dataIndex:'NOTE_AMOUNT_A',align:'center',renderer:fmtMoney},
				{header: '一月挂账时间',dataIndex:'GUAZHANG_DATE_A',align:'center'},
				{header: '二月结算金额',dataIndex:'NOTE_AMOUNT_B',align:'center',renderer:fmtMoney},
				{header: '二月挂账时间',dataIndex:'GUAZHANG_DATE_B',align:'center'},
				{header: '三月结算金额',dataIndex:'NOTE_AMOUNT_C',align:'center',renderer:fmtMoney},
				{header: '三月挂账时间',dataIndex:'GUAZHANG_DATE_C',align:'center'},
				{header: '四月结算金额',dataIndex:'NOTE_AMOUNT_D',align:'center',renderer:fmtMoney},
				{header: '四月挂账时间',dataIndex:'GUAZHANG_DATE_D',align:'center'},
				{header: '五月结算金额',dataIndex:'NOTE_AMOUNT_E',align:'center',renderer:fmtMoney},
				{header: '五月挂账时间',dataIndex:'GUAZHANG_DATE_E',align:'center'},
				{header: '六月结算金额',dataIndex:'NOTE_AMOUNT_F',align:'center',renderer:fmtMoney},
				{header: '六月挂账时间',dataIndex:'GUAZHANG_DATE_F',align:'center'},
				{header: '七月结算金额',dataIndex:'NOTE_AMOUNT_G',align:'center',renderer:fmtMoney},
				{header: '七月挂账时间',dataIndex:'GUAZHANG_DATE_G',align:'center'},
				{header: '八月结算金额',dataIndex:'NOTE_AMOUNT_H',align:'center',renderer:fmtMoney},
				{header: '八月挂账时间',dataIndex:'GUAZHANG_DATE_H',align:'center'},
				{header: '九月结算金额',dataIndex:'NOTE_AMOUNT_I',align:'center',renderer:fmtMoney},
				{header: '九月挂账时间',dataIndex:'GUAZHANG_DATE_I',align:'center'},
				{header: '十月结算金额',dataIndex:'NOTE_AMOUNT_J',align:'center',renderer:fmtMoney},
				{header: '十月挂账时间',dataIndex:'GUAZHANG_DATE_J',align:'center'},
				{header: '十一月结算金额',dataIndex:'NOTE_AMOUNT_K',align:'center',renderer:fmtMoney},
				{header: '十一月挂账时间',dataIndex:'GUAZHANG_DATE_K',align:'center'},
				{header: '十二月结算金额',dataIndex:'NOTE_AMOUNT_L',align:'center',renderer:fmtMoney},
				{header: '十二月挂账时间',dataIndex:'GUAZHANG_DATE_L',align:'center'}
		      ];
	function fmtMoney(val,meta,rec){
		if(val=='-1')return '+';
		else return val ;
	}
	function fmtDate(val,meta,rec){
		if(val=='1111-11-11 00:00:00' || val=='')return '--';
		else return val.substring(0,10);
	}
	function goDownload(){
		fm.action = '<%=contextPath%>/report/jcafterservicereport/FinancialCreditReport/download.do' ;
		fm.submit();
	}
</script>
</body>
</html>
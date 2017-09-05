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
	<title>结算单复核申请管理</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;结算单复核申请管理
</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="nowrap">签收日期：</td>
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
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
			<input type="button" class="normal_btn" value="打印" onclick="goPrint()"/>
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
		var myPage;
		var url = '<%=contextPath%>/claim/authorization/BalanceMain/claimPrintQuery.json';
		var title = null;
		
		var columns = [
					{header: "序号",align:'center',renderer:getIndex},
					{header: "维修站代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "维修站名称",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "结算单号",dataIndex: 'TOTAL_NO',align:'center'},		
					{header: "维修时间起",dataIndex: 'B_DATE',align:'center'},
					{header:'维修时间止',dataIndex:'E_DATE',align:'center'},
					{header: "索赔单数量",dataIndex: 'CLAIM_COUNT',align:'center'}, 
					{header: "签收日期",dataIndex: 'SIGN_DATE',align:'center'},
					{header:'省份',dataIndex:'REGION_NAME',align:'center'},
					{header:'收单人',dataIndex:'NAME',align:'center'}
			      ];
	function goPrint(){
		if(submitForm('fm')==false)return;;
		var b = $('t1').value ;
		var e = $('t2').value ;
		var name = $('person').value ;
		//var url = '<%=contextPath%>/claim/authorization/BalanceMain/goPrint.do?bDate='+b+'&eDate='+e+'&name='+name ;
		//window.open(url);
		fm.target="_blank";
		fm.action = '<%=contextPath%>/claim/authorization/BalanceMain/goPrint.do' ;
		fm.submit();
	}
</script>
</body>
</html>
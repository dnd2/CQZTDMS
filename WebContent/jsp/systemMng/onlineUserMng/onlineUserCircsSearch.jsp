<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>在线用户情况查询</title>
<style>
.img {
	border: none
}
</style>
</head>

<body onunload="javascript:destoryPrototype()" onload="init();__extQuery__(1);">
	<div class="wbox">
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
		系统管理 &gt; 在线用户查询 &gt; 在线用户情况查询</div>
		<form id="fm" name="fm">
			<table class="table_query" border="0">
				<tr>
					<input type="hidden" name="userOnlineId" id="userOnlineId" value="" />
					<td class="table_query_2Col_label_4Letter"  nowrap="nowrap">年份：</td>
					<td nowrap="nowrap" >
						<select name="select_year" onchange="change(this, document.fm.select_month, document.fm.select_day)">
							<option value=""><script type="text/javascript">setAllSelect();</script></option>
						</select>
					</td>
					<td class="table_query_2Col_label_4Letter" nowrap="nowrap">月份：</td>
					<td nowrap="nowrap">
						<select name="select_month" onchange="change1(document.fm.select_year, this,document.fm.select_day)">
							<option value=""><script type="text/javascript">setAllSelect();</script></option>
						</select>
					</td>
					<td class="table_query_2Col_label_4Letter" nowrap="nowrap">日期：</td>
					<td>
						<select name="select_day" id="select_day">
							<option value=""><script type="text/javascript">setAllSelect();</script></option>
						</select>
					</td>
					<td class="table_query_2Col_label_4Letter" nowrap="nowrap" >
						<span class="table_query_last">
			  				<input class="normal_btn" type="button" value="查 询" onclick="query();" id="queryBtn"/>
						</span>
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		</form>
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</div>
	<script type="text/javascript" >

		var url = "<%=contextPath%>/sysmng/onlineusermng/OnlineUserCircs/queryOnlineUserCircs.json";

		//设置表格标题
		var title= null;

		var columns = [
						{header: "周度", width:'31%', dataIndex: 'week',orderCol:"week"},
						{header: "总在线人数", width:'23%', dataIndex: 'sumCount'},
						{header: "JMC在线人数", width:'23%', dataIndex: 'sgmCount'},
						{header: "经销商在线人数", width:'23%', dataIndex: 'dlrCount'}
					  ];

		var _swap = columns;
		
		var columnsmonth = [
						{header: "日期", width:'31%', dataIndex: 'year',orderCol:"year"},
						{header: "总在线人数", width:'23%', dataIndex: 'sumCount'},
						{header: "JMC在线人数", width:'23%', dataIndex: 'sgmCount'},
						{header: "经销商在线人数", width:'23%', dataIndex: 'dlrCount'}
					  ];
		var columnsday = [
							{header: "使用时间", width:'31%', dataIndex: 'year',orderCol:"year"},
							{header: "总在线人数", width:'23%', dataIndex: 'sumCount'},
							{header: "JMC在线人数", width:'23%', dataIndex: 'sgmCount'},
							{header: "经销商在线人数", width:'23%', dataIndex: 'dlrCount'}
						  ];
				      
		function init(){
			initDate(document.fm.select_year,document.fm.select_month,document.fm.select_day);
		}
		function query(){
			if($("select_month").value != "" && $("select_year").value == ""){
				MyAlert("月份不为空时，年份不能为空！");	
				return false;
			}
			if($("select_year").value != "" && $("select_month").value != "" && $("select_day").value == ""){
				columns = columnsmonth;
			}else if($("select_year").value != "" && $("select_month").value != "" && $("select_day").value != ""){
				columns = columnsday;
			}else{
				columns = _swap;
			}
			__extQuery__(1);
		}
	</script>
</body>
</html>

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
<title>及时关闭率统计</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 报告管理 &gt;及时关闭率统计</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />及时关闭率统计</th>
			
			<tr>
				<td align="right" nowrap="true">受理人：</td>
				<td align="left" nowrap="true">
					<input type="text" id="dealName" name="dealName"/>
				</td>
				<td align="right" nowrap="true">日期：</td>
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

				<td align="right" nowrap="true">报表类型：</td>
				<td align="left" nowrap="true">
					<select id="reportType" name="reportType" class="short_sel">
						<c:forEach var="rep" items="${reportTypeList}">
							<option value="${rep.value}" title="${rep.key}">${rep.key}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="6" align="center">
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
		fm.action = '<%=contextPath%>/customerRelationships/reportformsManage/ComplaintClosedReportforms/complaintClosedReportformsExcel.do';
		fm.submit();
	}
	function check(){
		var msg ="";
		if(""==document.getElementById('dateStart').value){
			msg+="开始日期不能为空!</br>"
		}
		if(""==document.getElementById('dateEnd').value){
			msg+="结束日期不能为空!</br>"
		}
		if(msg!=""){
			MyAlert(msg);
			return false;
		}else{
			return true;
		}
	}
	
	function search(){
		if(check()){
			setColumns();
			__extQuery__(1);
		}
	}
	
	function setColumns(){
		var reportValue = document.getElementById('reportType').value;
		if(reportValue == '1'){
			columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "来源部门",dataIndex: 'TYPENAME',align:'center'},
				{header: "需考核抱怨总数",dataIndex: 'TOTAL',align:'center'},
				{header: "实际受理抱怨总数",dataIndex: 'ACCC',align:'center'},
				{header: "及时关闭抱怨数量", dataIndex: 'TIMELYCLOSEC', align:'center'},
				{header: "已关闭抱怨数量",dataIndex: 'CLOSEDC',align:'center'},
				{header: "延期处理抱怨数量",dataIndex: 'DELAYC',align:'center'},
				{header: "关闭率", dataIndex: 'CLOSER', align:'center'},
				{header: "及时关闭率", dataIndex: 'TIMELYCLOSER', align:'center'},
				{header: "一次性及时关闭率", dataIndex: 'ONCETIMELYCLOSEC', align:'center'}
		      ];
		}else if(reportValue == '2'){
			columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "大区",dataIndex: 'TYPENAME',align:'center'},
				{header: "需考核抱怨总数",dataIndex: 'TOTAL',align:'center'},
				{header: "实际受理抱怨总数",dataIndex: 'ACCC',align:'center'},
				{header: "及时关闭抱怨数量", dataIndex: 'TIMELYCLOSEC', align:'center'},
				{header: "已关闭抱怨数量",dataIndex: 'CLOSEDC',align:'center'},
				{header: "延期处理抱怨数量",dataIndex: 'DELAYC',align:'center'},
				{header: "关闭率", dataIndex: 'CLOSER', align:'center'},
				{header: "及时关闭率", dataIndex: 'TIMELYCLOSER', align:'center'},
				{header: "一次性及时关闭率", dataIndex: 'ONCETIMELYCLOSEC', align:'center'}
		      ];
		}else if(reportValue == '3'){
			columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "省份",dataIndex: 'TYPENAME',align:'center'},
				{header: "需考核抱怨总数",dataIndex: 'TOTAL',align:'center'},
				{header: "实际受理抱怨总数",dataIndex: 'ACCC',align:'center'},
				{header: "及时关闭抱怨数量", dataIndex: 'TIMELYCLOSEC', align:'center'},
				{header: "已关闭抱怨数量",dataIndex: 'CLOSEDC',align:'center'},
				{header: "延期处理抱怨数量",dataIndex: 'DELAYC',align:'center'},
				{header: "关闭率", dataIndex: 'CLOSER', align:'center'},
				{header: "及时关闭率", dataIndex: 'TIMELYCLOSER', align:'center'},
				{header: "一次性及时关闭率", dataIndex: 'ONCETIMELYCLOSEC', align:'center'}
		      ];
		}
		
	}
	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/reportformsManage/ComplaintClosedReportforms/queryComplaintClosedReportforms.json";
				
	var title = null;
	var columns = null;

		      

</script>
</body>
</html>
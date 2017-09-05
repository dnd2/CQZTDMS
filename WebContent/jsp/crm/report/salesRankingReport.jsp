<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    int yes = Constant.IF_TYPE_YES;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
</script>

<title>销售排名报表</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);loadcalendar();">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>报表>销售排名报表查询
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="1" /> <input
				type="hidden" id="dlrId" name="dlrId" value="" />

			<table class="table_query" width="95%" align="center">
				<c:if test="${returnValue==2}">
					<tr>
						<td colspan="6"></td>
					</tr>
				</c:if>
				<tr>
					<td align="right" width="18%">经销商：</td>
					<td>
						<input name="dealerCode" type="text" id="dealerCode" class="middle_txt" value="" size="20" readonly="readonly" />
	                  	
	                  	<c:if test="${dutyType==10431001}">
	                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer('dealerCode','','true', '${orgId}')" value="..." />
	                 	</c:if>
	                  	<c:if test="${dutyType==10431002}">
	                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer3('dealerCode','','true', '${orgId}')" value="..." />
	                  	</c:if>
	                 	<c:if test="${dutyType==10431003}">
	                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer3('dealerCode','','true', '${orgId}')" value="..." />
	                  	</c:if>
	                 	<c:if test="${dutyType==10431004}">
	                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer6('dealerCode','','true', '${orgId}')" value="..." />
	                  	</c:if>                    
	                    <input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
					</td>
					<td width="12%" align="right">报表时间从：</td>
					<td width="35%">
						<div align="left">
							<input name="startDate" id="startDate" value="" type="text"
								class="short_txt" datatype="1,is_date,10"
								group="startDate,endDate" hasbtn="true"
								callFunction="showcalendar(event, 'startDate', false);" />
							&nbsp;&nbsp;&nbsp;到 <input name="endDate" id="endDate" value=""
								type="text" class="short_txt" datatype="1,is_date,10"
								group="startDate,endDate" hasbtn="true"
								callFunction="showcalendar(event, 'endDate', false);" />
						</div></td>
				</tr>
				<tr>
					<td colspan="6" align="center"><input name="queryBtn"
						type="button" class="normal_btn" onclick="__extQuery__(1);"
						value="查询" />
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>

	</div>
	<script type="text/javascript"> 
	
	var myPage;
	var url = "<%=contextPath%>/crm/report/Report/salesRankingReportQuery.json";
	var title = null;
	var columns = [
			    {header: "排名", dataIndex: 'RANKING', align:'center'},
				{header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
				{header: "小区", dataIndex: 'PQ_ORG_NAME', align:'center'},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "数量", dataIndex: 'CUT', align:'center'},
				{header: "操作",sortable: false,dataIndex: 'DEALER_ID',renderer:myLink}
		      ];
	function myLink(value,meta,record){
		return String.format("<a href=\"#\" id=\"DEALER_ID\" name=\"DEALER_ID\" onclick='checkDetailUrl(\""+record.data.DEALER_ID+"\")'>[详情]</a>");
	}   
	function checkDetailUrl(dealerId){
		var startDate = document.getElementById("startDate").value;
		var endDate = document.getElementById("endDate").value;
		OpenHtmlWindow("<%=contextPath%>/crm/report/Report/salesRankingDetail.do?dealerId="+dealerId+"&startDate="+startDate+"&endDate="+endDate,600,400);
	}
	</script>
</body>
</html>
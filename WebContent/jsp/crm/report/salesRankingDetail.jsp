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

<title>销售排名详细</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);loadcalendar();">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>报表>销售排名详细
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="1" /> <input
				type="hidden" id="dlrId" name="dlrId" value="" />
			<input type="hidden" name="dealerId" id="dealerId" value="${dealerId }" />
			<input type="hidden" name="startDate2" id="startDate2" value="${startDate }" />
			<input type="hidden" name="endDate2" id="endDate2" value="${endDate }" />

			<table class="table_query" width="95%" align="center">
				<c:if test="${returnValue==2}">
					<tr>
						<td colspan="6"></td>
					</tr>
				</c:if>
				<tr>
					<td align="right">报表时间从：</td>
					<td>
						<div align="left">
							<input name="startDate" id="startDate" value="${startDate }" type="text"
								class="short_txt" datatype="1,is_date,10"
								group="startDate,endDate" hasbtn="true"
								callFunction="showcalendar(event, 'startDate', false);" />
							&nbsp;&nbsp;&nbsp;到 <input name="endDate" id="endDate" value="${endDate }"
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
	var url = "<%=contextPath%>/crm/report/Report/salesRankingDetailQuery.json";
	var title = null;
	var columns = [
			    {header: "排名", dataIndex: 'RANKING', align:'center'},
				{header: "顾问", dataIndex: 'ADVISER', align:'center'},
				{header: "数量", dataIndex: 'CUT', align:'center'}
		      ];
	</script>
</body>
</html>
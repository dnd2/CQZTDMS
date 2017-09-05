<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>巡航服务线路申报</title>
<% String contextPath = request.getContextPath(); %>
<%@taglib uri="/jstl/cout" prefix="c" %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 信息反馈管理&gt;信息反馈提报 &gt;巡航服务线路申报</div>
<form id="fm" name="fm">
<input type="hidden" name="flag" value="${flag}"/>
<table class="table_query">
	<tr>
		<td width="10%" align="right">航线代码：</td>
		<td width="20%">
			<input type="text" class="middle_txt" name="cr_no"/>
		</td>
		<td width="10%" align="right">目的地：</td>
		<td width="20%">
			<input type="text" class="middle_txt" name="whither"/>
		</td>
	</tr>
	<tr>
		<td width="10%" align="right">状态：</td>
		<td>
			<c:if test="${flag==1}">
				<script type="text/javascript">
					genSelBoxExp("status",<%=Constant.CRUISE_STATUS%>,"<%=Constant.CRUISE_STATUS_02%>",true,"short_sel","","true",'');
				</script>
			</c:if>
			<c:if test="${flag!=1}">
				<script type="text/javascript">
				 	genSelBoxExp("status",<%=Constant.CRUISE_STATUS%>,"<%=Constant.CRUISE_STATUS_04%>",true,"short_sel","","true",'');
				</script>
			</c:if>
		</td>
		<td colspan="2" align="center">
			<input class="normal_btn" type="button" value="查询" name="qryButton" onClick="__extQuery__(1);">
   		</td>  
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript">
	var url = "<%=contextPath%>/feedbackmng/apply/CruiseAction/queryCruise4Audit1.json";		
	var title = null;
	var columns = [
                  {header:'序号',align:'center'},
                  {header:'航线代码',dataIndex:'CR_NO',align:'center'},
                  {header:'目的地',dataIndex:'CR_WHITHER',align:'center'},
                  {header:'航行天数',dataIndex:'CR_DAY',align:'center'},
                  {header:'航行里程',dataIndex:'CR_MILEAGE',align:'center'},
                  {header:'每月定点日期',dataIndex:'FIX_POINT_DATE',align:'center'},
                  {header:'状态',dataIndex:'STATUS',align:'center',renderer:getItemValue},
                  {header:'操作',align:'center',renderer:myHandler}
                  ];
	function myHandler(value,meta,rec){
		var id = rec.data.ID ;
		var status = rec.data.STATUS;
		if(status=='<%=Constant.CRUISE_STATUS_02%>' && '${flag}' == 1)
			return '<a href="<%=contextPath%>/feedbackmng/apply/CruiseAction/showCruise4Audit.do?type=1&id='+id+'">审核</a>' ;
		else if(status=='<%=Constant.CRUISE_STATUS_04%>' && '${flag}' != 1)
			return '<a href="<%=contextPath%>/feedbackmng/apply/CruiseAction/showCruise4Audit.do?type=3&id='+id+'">审核</a>' ;
		else return '<a href="<%=contextPath%>/feedbackmng/apply/CruiseAction/showCruise4Audit.do?type=2&id='+id+'">查看</a>' ;
	}
</script>
</BODY>
</html>
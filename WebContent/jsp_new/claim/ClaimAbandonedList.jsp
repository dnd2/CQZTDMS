<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>保养索赔单管理</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/ClaimAction/ClaimAbandonedQuery.json";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "索赔单号", width:'15%', dataIndex: 'CLAIM_NO'},
				{header: "索赔类型", width:'7%', dataIndex: 'CLAIM_TYPE',renderer:getItemValue},
				{header: "结算基地", width:'7%', dataIndex: 'BALANCE_YIELDLY',renderer:getItemValue},
				{header: "修改次数", width:'15%', dataIndex: 'SUBMIT_TIMES'},
				{header: "VIN", width:'15%', dataIndex: 'VIN'},
				{header: "建单时间", width:'15%', dataIndex: 'CREATE_DATE'},
				{header: "申请状态", width:'15%', dataIndex: 'STATUS',renderer:getItemValue}
	      ];
	     
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔单管理&gt;索赔单废弃查询
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">索赔单号：</td>
		<td width="15%"><input name="claim_no" type="text" id="claim_no"  maxlength="30" class="middle_txt"/></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">开始时间：</td>
      	<td width="15%"><input name="beginTime" type="text" id="beginTime" readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">结束时间：</td>
      	<td width="15%"><input name="endTime" type="text" id="endTime"  readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">索赔单类型：</td>
		<td width="15%" nowrap="true">
		    <script type="text/javascript">
		       genSelBoxExp("status",<%=Constant.CLAIM_APPLY_ORD_TYPE%>,"",true,"short_sel","","false",'10661002,10661003,10661004,10661005,10661008,10661009,10661011,10661012');
		    </script>
		</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">VIN：</td>
      	<td width="15%" nowrap="true">
      	      <input name="vin" type="text" id="vin" maxlength="100" class="middle_txt"/>
      	</td>
      	<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
		</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
		</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
      	<td width="15%" nowrap="true">
      	</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    	</td>
    </tr>
</table>
<!-- 查询条件 end -->

<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>
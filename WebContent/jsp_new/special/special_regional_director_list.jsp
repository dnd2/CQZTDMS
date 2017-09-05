<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>特殊费用</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>


<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/SpecialAction/specialRegionalDirectorList.json?query=true";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'SPE_ID',renderer:myLink,align:'center'},
				{header: "申请单号", dataIndex: 'APPLY_NO', align:'center'},
				{header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
				{header: "经销商简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "费用类型", dataIndex: 'SPECIAL_TYPE', align:'center',renderer:getType},
				{header: "申请金额", dataIndex: 'APPLY_MONEY', align:'center'},
				{header: "上报时间", dataIndex: 'REPORT_DATE', align:'center'},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue}
	      ];
	function getType(value,meta,record){
		var type="";
		if(1==value){
			type+="善意索赔";
		}
		if(0==value){
			type+="退换车";
		}
		return String.format(type);
	}
	function myLink(value,meta,record){
		var status=record.data.STATUS;
		var special_type=record.data.SPECIAL_TYPE;
		var url="";
		if(20331005==status){
			var urlUpdate='<%=contextPath%>/SpecialAction/serviceManagerSpeInit.do?id='+value+'&special_type='+special_type+'&url=specialRegionalDirectorList';
			url+="<a href='"+urlUpdate+"');'>[审核]</a>";
		}
		var urlView='<%=contextPath%>/SpecialAction/viewSpe.do?id='+value+'&special_type='+special_type;
		url+="<a href='"+urlView+"');'>[明细]</a>";
		return String.format(url);
	}
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;特殊费用管理&gt;特殊费用服务站查询
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">服务站简称：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="dealer_shortname"  name="dealer_shortname" maxlength="30" type="text"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">服务站代码：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="dealer_code"  name="dealer_code" maxlength="30" type="text"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">服务站联系人</td>
		<td width="15%" nowrap="true">
			<input class="middle_txt" id="dealer_contact_person"  name="dealer_contact_person" maxlength="30" type="text"/>
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
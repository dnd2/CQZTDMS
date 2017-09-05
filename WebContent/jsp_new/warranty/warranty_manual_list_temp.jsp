<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>质保手册</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
	

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/WarrantyManualAction/warrantyManualListTemp.json?query=true";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'},
				{header: "报告编号", dataIndex: 'REPORT_NO', align:'center'},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "用户姓名", dataIndex: 'CREATE_NAME', align:'center'},
				{header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
				{header: "申请状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "申请时间", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "是否发运", dataIndex: 'IS_SEND', align:'center',renderer:myLink1},
				{header: "发运单号", dataIndex: 'SEND_NO', align:'center'},
				{header: "审核人", dataIndex: 'SEND_NAME', align:'center'},
				{header: "审核时间", dataIndex: 'AUDIT_DATE', align:'center'}
	      ];
	function myLink1(value,meta,record){
		var is_send=record.data.IS_SEND;
		var url="";
		if(0==is_send){
			url+="否";
		}else{
			url+="是";
		}
		return String.format(url);
	}
	function myLink(value,meta,record){
		var status=record.data.STATUS;
		var url="";
		if(95451002==status){
			var urlUpdate='<%=contextPath%>/WarrantyManualAction/auditWarrantyManualInit.do?id='+value;
			url+="<a href='"+urlUpdate+"');'>[审核]</a>";
		}
		var urlView='<%=contextPath%>/WarrantyManualAction/auditViewWarrantyManual.do?id='+value;
		url+="<a href='"+urlView+"');'>[明细]</a>";
		return String.format(url);
	}
	function expotData(){
		   fm.action="<%=contextPath%>/WarrantyManualAction/expotWarrantyData.do";
	       fm.submit();
	}
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;车辆信息管理&gt;质保手册车厂查询
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
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">服务站电话：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="dealer_contact_phone"  name="dealer_contact_phone" maxlength="30" type="text"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">审核状态：</td>
      	<td width="15%" nowrap="true">
      		<script type="text/javascript">
         	genSelBoxExp("status",<%=Constant.BARCODE_APPLY_STATUS%>,"",true,"short_sel","","false",'95451001,95451003');
         	 </script>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">审核人:</td>
		<td width="15%" nowrap="true">
			<input class="middle_txt" id="audit_name"  name="audit_name" maxlength="30" type="text"/>
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">审核时间：</td>
      	<td width="15%" nowrap="true">
      		<input name="audit_date_begin" type="text" id="audit_date_begin" readonly="readonly" onfocus="calendar();" class="short_txt"/>至
      		<input name="audit_date_end" type="text" id="audit_date_end" readonly="readonly" onfocus="calendar();" class="short_txt"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">申请时间：</td>
      	<td width="15%" nowrap="true">
      		<input name="report_date_begin" type="text" id="report_date_begin" readonly="readonly" onfocus="calendar();" class="short_txt"/>至
      		<input name="report_date_end" type="text" id="report_date_end" readonly="readonly" onfocus="calendar();" class="short_txt"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">是否发送:</td>
		<td width="15%" nowrap="true">
			<select name="is_send" class="short_sel">
				<option value="">--请选择--</option>
				<option value="1">是</option>
				<option value="0">否</option>
			</select>
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">大区：</td>
      	<td width="15%" nowrap="true">
      		<select name="root_org_name" class="short_sel">
				<option value="">-请选择-</option>
				<c:forEach var="org" items="${org }">
					<option value="${org.ORG_NAME }">${org.ORG_NAME }</option>
				</c:forEach>
			</select>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">报告编号：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="report_no"  name="report_no" maxlength="30" type="text"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="btnQuery" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="btnQuery"  value="导出"  class="normal_btn" onClick="expotData();" >
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
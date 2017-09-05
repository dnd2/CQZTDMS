<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>工单</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/MainTainAction/emergencyMainTainList.json?query=true";
			
var title = null;

var columns = [
			{header: "序号",sortable: false,align:'center',renderer:getIndex},
			{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:mySelect,align:'center'},
			{header: "申请部门", dataIndex: 'APPLY_DEPT', align:'center'},
			{header: "借件人", dataIndex: 'BORROW_PERSON', align:'center'},
			{header: "借件部门", dataIndex: 'BORROW_DEPT', align:'center'},
			{header: "收件人", dataIndex: 'CONSIGNEE_PERSON', align:'center'}
	      ];
		function mySelect(value,metaDate,record){
			var PRODUCT_ADDR = record.data.PRODUCT_ADDR;
			var APPLY_DEPT= record.data.APPLY_DEPT;
		//	var BORROW_PERSON= record.data.BORROW_PERSON;
			var BORROW_NO= record.data.BORROW_NO;
			var BORROW_DEPT= record.data.BORROW_DEPT;
			var CONSIGNEE_PERSON= record.data.CONSIGNEE_PERSON;
			var CONSIGNEE_PHONE= record.data.CONSIGNEE_PHONE;
			var CONSIGNEE_ADDR= record.data.CONSIGNEE_ADDR;
			var CONSIGNEE_EMAIL= record.data.CONSIGNEE_EMAIL;
			var BORROW_PHONE= record.data.BORROW_PHONE;
			var BORROW_REASON= record.data.BORROW_REASON;
			return String.format("<input type='radio' name='rd' onclick='check(\""+PRODUCT_ADDR+"\",\""+APPLY_DEPT+"\",\""+BORROW_NO+"\",\""+BORROW_DEPT+"\",\""+CONSIGNEE_PERSON+"\",\""+CONSIGNEE_PHONE+"\",\""+CONSIGNEE_ADDR+"\",\""+CONSIGNEE_EMAIL+"\",\""+BORROW_PHONE+"\",\""+BORROW_REASON+"\")' />");
		}
		//null返回“”
		function getNull(data) {
			if (data==null) {
				return '';
			}else {
				return data;
			}
		}
		function check(PRODUCT_ADDR,APPLY_DEPT,BORROW_NO,BORROW_DEPT,CONSIGNEE_PERSON,CONSIGNEE_PHONE,CONSIGNEE_ADDR,CONSIGNEE_EMAIL,BORROW_PHONE,BORROW_REASON){
			parentContainer.setMainTain(PRODUCT_ADDR,APPLY_DEPT,BORROW_NO,BORROW_DEPT,CONSIGNEE_PERSON,CONSIGNEE_PHONE,CONSIGNEE_ADDR,CONSIGNEE_EMAIL,BORROW_PHONE,BORROW_REASON);
			_hide();
		} 
</script>
<!--页面列表 end -->
</head>
<body onload="__extQuery__(1);">
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;紧急调件人员维护查询页面
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">借件人：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="borrow_person"  name="borrow_person" maxlength="30" type="text"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">收件人：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="consignee_person"  name="consignee_person" maxlength="30" type="text"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">申请部门:</td>
		<td width="15%" nowrap="true">
			<input class="middle_txt" id="apply_dept"  name="apply_dept" maxlength="30" type="text"/>
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;
    		<input type="button" id="back" onClick="_hide();" class="normal_btn"  style="width=8%" value="关闭"/>
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
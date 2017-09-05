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
var url = "<%=contextPath%>/MainTainAction/emergencyMainTain.json?query=true";
			
var title = null;

var columns = [
			{header: "序号",sortable: false,align:'center',renderer:getIndex},
			{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'},
			{header: "申请部门", dataIndex: 'APPLY_DEPT', align:'center'},
			{header: "借件人", dataIndex: 'BORROW_PERSON', align:'center'},
			{header: "借件部门", dataIndex: 'BORROW_DEPT', align:'center'},
			{header: "收件人", dataIndex: 'CONSIGNEE_PERSON', align:'center'}
	      ];
		 function myLink(value,meta,record){
			var url="";
	   		url+="<a href='#' onclick='del(\""+value+"\");'>[删除]</a>";
	    	url+="<a href='#' onclick='view(\""+value+"\");'>[明细]</a>";
	    	return String.format(url);
		 }
		 
		 function view(id){
			 OpenHtmlWindow('<%=contextPath%>/MainTainAction/viewEmergency.do?id='+id,850,450);
		 }
		 
		function del(id){
		    	var urlDel='<%=contextPath%>/CommonAction/del.json?tableName=tt_as_emergency_MainTain&idName=id&id='+id;
		    	sendAjax(urlDel,function(json){
		    		if(json.succ=="1"){
		    			MyAlert("提示：删除成功！");
		    			__extQuery__(1);
		    		}else{
		    			MyAlert("提示：删除失败！");
		    		}
		    	},'fm');
		 }
		function add(){
		   OpenHtmlWindow('<%=contextPath%>/MainTainAction/addEmergency.do',850,450);
		}
	    function extQuery(){
	    	__extQuery__(1);
	    }
</script>
<!--页面列表 end -->
</head>
<body >
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
    		<input type="button"  name="bntAdd"  id="bntAdd"  value="添加" onclick="add();" class="normal_btn" />
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
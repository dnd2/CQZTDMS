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
var url = "<%=contextPath%>/MainTainAction/mailListTemp.json?query=true";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'},
				{header: "用户名称", dataIndex: 'USER_NAME', align:'center'},
				{header: "用户电话", dataIndex: 'USER_PHONE', align:'center'},
				{header: "大区", dataIndex: 'AREA', align:'center'},
				{header: "岗位名称", dataIndex: 'POSITION_NAME', align:'center'},
				{header: "岗位职责", dataIndex: 'POSITION_DUTY', align:'center'}
	      ];
function myLink(value,meta,record){
	var url="<a href='#' onclick='del(\""+value+"\");'>[删除]</a>";
	return String.format(url);
}
function del(id){
	var urlDel='<%=contextPath%>/CommonAction/del.json?tableName=tt_AS_mail_List&idName=id&id='+id;
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
	var url='<%=contextPath%>/MainTainAction/addMailList.do';
	window.location.href=url;
}
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;售后管理人员通讯
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">用户名称：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="user_name"  name="user_name" maxlength="30" type="text"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">用户电话：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="user_phone"  name="user_phone" maxlength="30" type="text"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button"  name="bntAdd"  id="bntAdd"  value="新增" onclick="add();" class="normal_btn" />
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
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
var url = "<%=contextPath%>/MainTainAction/LabRalation.json";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
				{header: "工时代码", dataIndex: 'LAB_CODE', align:'center'},
				{header: "工时名称", dataIndex: 'LAB_NAME', align:'center'},
				{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'}
	      ];
	      
	      
	    /* function operaStatus(value,metaDate,record){
	    	if("0"==value){
	    		return String.format("<a herf='#' onclick='statusChange(\""+value+"\",\""+record.data.ID+"\")'>[有效]</a>");
	    	}else{
	    		return String.format("<a herf='#' onclick='statusChange(\""+value+"\",\""+record.data.ID+"\")'>[无效]</a>");
	    	}
	    } */
	    function myLink(value,meta,record){
	    	var url="<a href='#' onclick='statusDel(\""+value+"\");'>[删除]</a>";
	    	return String.format(url);
	    }
	    function statusDel(id){
	    	var urlDel='<%=contextPath%>/MainTainAction/statusDel.json?id='+id;
	    	sendAjax(urlDel,function(json){
	    		if(json.succ=="1"){
	    			MyAlert("提示：删除成功！");
	    			__extQuery__(1);
	    		}else{
	    			MyAlert("提示：删除失败！");
	    		}
	    	},'fm');
	    }
		function goToRelationJsp(){
			var part_code=$("part_code").value;
			var part_id=$("part_id").value;
			OpenHtmlWindow('<%=contextPath%>/MainTainAction/goToRelationJsp.do?part_id='+part_id+'&part_code='+part_code,800,500);
		}
		function setLabourCode(){
			__extQuery__(1);
		}
		function backList(){
			window.location.href='<%=contextPath%>/MainTainAction/labPart.do';
		}
</script>
<!--页面列表 end -->
</head>
<body onload="__extQuery__(1);">
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;配件工时关系查询
</div>
<form name="fm" id="fm">
<input type="hidden" id="part_id" name="part_id" value="${part_id }"/>
<input type="hidden" id="part_code" name="part_code" value="${part_code }"/>
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">工时代码：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="lab_code"  name="lab_code" maxlength="30" type="text"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">工时名称：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="lab_name"  name="lab_name" maxlength="30" type="text"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button"  name="bntAdd"  id="bntAdd"  value="新增" onclick="goToRelationJsp();" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;
    		<input type="button" id="back" onClick="backList();" class="normal_btn"  style="width=8%" value="返回"/>
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
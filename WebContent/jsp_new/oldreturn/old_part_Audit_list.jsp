<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title></title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/OldReturnAction/oldPartApplyList.json?query=true";
			
var title = null;

var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'},
  				{header: "服务站代码", dataIndex: 'DEALER_CODE', align:'center',renderer:formatDate3},
  				{header: "服务站简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "清单号",dataIndex: 'RETURN_NO',align:'center'},
  				{header: "开始时间",dataIndex: 'START_DATE',align:'center',renderer:formatDate1},
  				{header: "结束时间", dataIndex: 'END_DATE', align:'center',renderer:formatDate2},
  				{header: "申请时间", dataIndex: 'APPLY_DATE', align:'center',renderer:formatDate3},
  				{header: "审核时间", dataIndex: 'AUDIT_DATE', align:'center',renderer:formatDate3},
  				{header: "审核人", dataIndex: 'AUDIT_MAN1', align:'center'},
  				{header: "状态", dataIndex: 'CODE_DESC', align:'center'}
	      ];
	    function myLink(value,meta,record){
	    	var status=record.data.STATUS;
	    	var RETURN_NO =record.data.RETURN_NO; 
	    	var url="";
	    	if("93451001"==status ){
	        	url+="<a href='#' onclick='del(\""+value+"\");'>[删除]</a>";
	        	var urlUpdate="<%=contextPath%>/OldReturnAction/oldPartApplyInit.do?id="+value+"&type=update";
	        	url+="<a href='"+urlUpdate+"' >[修改]</a>";
	        	var urlReport="<%=contextPath%>/OldReturnAction/oldParReport.do?id="+value;
	        	url+="<a href='#' onclick='Report(\""+value+","+RETURN_NO+"\");'>[上报]</a>";
	    	}else if("93451002"==status){
	        	url+="<a href='#' onclick='Revoke(\""+value+"\");'>[撤销]</a>";
	    	}
	    	
	    	var urlView="<%=contextPath%>/OldReturnAction/oldPartApplyInitAutid.do?id="+value+"&type=apply";
	    	url+="<a href='"+urlView+"'>[明细]</a>";
	        return String.format(url);
	    }
	  //格式化时间为YYYY-MM-DD
		function formatDate1(value,meta,record) {
			if (value==""||value==null) {
				return "";
			}else {
				return value.substr(0,7)+"-26";
			}
		}
		 //格式化时间为YYYY-MM-DD
		function formatDate2(value,meta,record) {
			if (value==""||value==null) {
				return "";
			}else {
				return value.substr(0,7)+"-25";
			}
		}
		function formatDate3(value,meta,record) {
			if (value==""||value==null) {
				return "";
			}else {
				return value.substr(0,19);
			}
		}
	    function add(){
	    	window.location.href='<%=contextPath%>/OldReturnAction/oldPartApplyAdd.do';
	    }
	    function del(id){
	    	var urlDel='<%=contextPath%>/CommonAction/del1.json?tableName=tt_AS_old_return_apply&idName=id&id='+id;
	    	sendAjax(urlDel,function(json){
	    		if(json.succ=="1"){
	    			MyAlert("提示：删除成功！");
	    			__extQuery__(1);
	    		}else{
	    			MyAlert("提示：删除失败！");
	    		}
	    	},'fm');
	    }
	    //撤销操作
	  function  Revoke(value){
	      var urlRevoke="<%=contextPath%>/OldReturnAction/oldPartApplyInit.json?id="+value+"&type=Revoke";
	        	sendAjax(urlRevoke,function(json){
	    		if(json.succ=="1"){
	    			MyAlert("提示：操作成功！");
	    			__extQuery__(1);
	    		}else{
	    			MyAlert("提示：操作失败！");
	    			__extQuery__(1);
	    		}
	    	},'fm');
	  }
	  //上报操作
	     function Report(value,RETURN_NO){
	        var urlReport="<%=contextPath%>/OldReturnAction/oldParReport.json?id="+value+"&RETURN_NO="+RETURN_NO;
	        sendAjax(urlReport,function(json){
	    		if(json.succ=="1"){
	    			MyAlert("提示：操作成功！");
	    			__extQuery__(1);
	    		}else{
	    			MyAlert("提示：操作失败，该数据已上报！");
	    			__extQuery__(1);
	    		}
	    	},'fm');
	    }
</script>
<!--页面列表 end -->
</head>
<body onload="loadcalendar();">
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;旧件回运延期查询</div>
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">申请时间起：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="start_date"  name="start_date" maxlength="30" type="text"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">申请时间止：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="end_date"  name="end_date" maxlength="30" type="text"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">申请人：</td>
		<td width="15%" nowrap="true">
		    <input class="middle_txt" id="user_name"  name="user_name" maxlength="30" type="text"/>
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">状态：</td>
      	<td width="15%" nowrap="true">
          <select id="status" name="status">
      	      <option value="">请选择</option>
      	      <option value="已保存">已保存</option>
      	      <option value="已上报">已上报</option>
      	      <option value="审核通过">审核通过</option>
      	      <option value="审核拒绝">审核拒绝</option>
      	  </select>       
    	</td>
         <td align="right" nowrap="nowrap">回运清单号：</td>
           <td>
             <input id="RETURN_NO" name="RETURN_NO" value="" type="text" maxlength="20" class="middle_txt" />
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
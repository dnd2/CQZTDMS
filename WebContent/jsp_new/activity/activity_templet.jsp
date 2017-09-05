<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>?</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="${contextPath}/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

	var myPage;
	
	var url = "<%=contextPath%>/ActivityAction/activityTemplet.json?query=true";//url查询
	var title = null;//头标
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "模板编号", dataIndex: 'TEMPLET_NO', align:'center'},
		{header: "模板名称", dataIndex: 'TEMPLET_NAME', align:'center'},
		{header: "主题编号", dataIndex: 'SUBJECT_NO', align:'center'},
		{header: "主题名称", dataIndex: 'SUBJECT_NAME', align:'center'},
		{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
		{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'}
	];
	function myLink(value,meta,record){
	    var status=record.data.STATUS;
	  	var url="";
	    if(status==18041001){//未发布
	    	var urlUpdate="<%=contextPath%>/ActivityAction/updateTempletInit.do?id="+value;
	    	url+="<a href='"+urlUpdate+"'>[修改]</a>";
	    	url+="<a href='#' onclick='del(this,\""+ value +"\")';'>[作废]</a>";
	    	url+="<a href='#' onclick='pulish(this,\""+ value +"\")';'>[下发]</a>";
	    }
	    if(status==18041002){//发布
	    	//url+="<a href='#' onclick='del(this,\""+ value +"\")';'>[作废]</a>";
	    }
    	var urlView="<%=contextPath%>/ActivityAction/templetView.do?id="+value;
	    url+="<a href='"+urlView+"'>[明细]</a>";
    	
        return String.format(url);
      }
      var id_del="";
      var vin_del="";
      function pulish(obj,id){
    	  id_del=id;
    	  MyConfirm("是否确认下发？",pulishsubmit,"");
      }
      function del(obj,id){
    	  id_del=id;
    	  MyConfirm("是否确认作废？",delsubmit,"");
      }
      function pulishsubmit(){
    	  sendAjax('<%=contextPath%>/ActivityAction/templetPublish.json?id='+id_del,backpulish,'fm');
      }
      function backpulish(json){
    	  if(json.succ=="1"){
    		  MyAlert("提示：发布成功！");
    		  __extQuery__(1);
    	  }else{
    		  MyAlert("提示："+json.msg);
    	  }
      }
      function delsubmit(){
    	  sendAjax('<%=contextPath%>/ActivityAction/deleteTemplet.json?id='+id_del,backDel,'fm');
      }
      function backDel(json){
    	  if(json.succ=="1"){
    		  MyAlert("提示：作废成功！");
    		  __extQuery__(1);
    	  }else{
    		  MyAlert("提示："+json.msg);
    	  }
      }
	function add(){
  	  window.location.href='<%=contextPath%>/ActivityAction/templetAdd.do';
    }
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动模板管理
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">模板编号：</td>
      	<td width="15%" nowrap="true">   
      		<input type="text"  name="templet_no" id="templet_no" maxlength="30" class="middle_txt"/> 	
         </td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">模板名称：</td>
      	<td width="15%" nowrap="true">
	      	<input type="text"  name="templet_name" id="templet_name" maxlength="30" class="middle_txt"/>
	   	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">模板状态：</td>
		<td width="15%" nowrap="true">
			<script type="text/javascript">
         		genSelBoxExp("status",<%=Constant.OLD_PART_BORROW%>,"",true,"short_sel","","false",'');
         	 </script>
			</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    	    <input type="button"  name="bntAdd"  id="bntAdd"  value="新增" onclick="add();" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;&nbsp;
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
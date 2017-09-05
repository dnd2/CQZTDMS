<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>索赔旧件管理</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<script type="text/javascript">
	function newAdd(){
		OpenHtmlWindow('<%=contextPath%>/jsp/claim/oldPart/newAddInit.jsp',400,150);
	}
	function chooseType(chooseType){
		$('chooseType').value=chooseType;
		fm.action="<%=contextPath%>/claim/oldPart/EmergencyDevice/newAdd.do";
	    fm.submit();
	}
	function mainTain(){
		window.location.href='<%=contextPath%>/MainTainAction/emergencyMainTain.do';
	}
</script>
</head>
<body onload="__extQuery__(1);">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;紧急调件清单
</div>
<form name="fm" id="fm">
<input id="chooseType" value="" name="chooseType" type="hidden" />
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true"></td>
		<td width="15%">
		</td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">下发时间始：</td>
      	<td width="15%"><input name="beginTime" type="text" id="beginTime" readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">下发时间止：</td>
      	<td width="15%"><input name="endTime" type="text" id="endTime"  readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
		<td width="10%" class="table_query_2Col_label_6Letter" nowrap="true"></td>
		<td width="15%">
		</td>
	</tr>
	<tr>
				<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true"></td>
		<td width="15%">
		</td>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">借件部门：</td>
		<td width="15%">
			<input name="borrowDept" type="text" id="borrowDept" class="middle_txt" maxlength="30" value="" />
		</td>
		<td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">借件人：</td>
		<td width="15%">
			<input name="borrowPerson" type="text" id="borrowPerson" class="middle_txt" maxlength="30" value="" />
		</td>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true"></td>
		<td width="15%">
		</td>
	</tr>
	<tr>
				<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true"></td>
		<td width="15%">
		</td>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">经销商代码：</td>
		<td width="15%"  nowrap="true">
				<input class="middle_txt" id="dealer_code"  name="dealerCode" type="text" onclick="showOrgDealer('dealer_code','dealer_id','true','','false','','10771002');" readonly="readonly"/>
				<input type="hidden" name="dealerId" id="dealer_id"/>
 				<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
		</td>
		</td>
		<td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">收件人：</td>
		<td width="15%">
			<input name="consigneePerson" type="text" id="consigneePerson" class="middle_txt" maxlength="30" value="" />
		</td>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true"></td>
		<td width="15%">
		</td>
	</tr>
	<tr>
				<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true"></td>
		<td width="15%">
		</td>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">状态：</td>
		<td width="15%"  nowrap="true">
  			<select class="short_sel" name="status">
  				<option value="">--请选择--</option>
				<option value="18041001">已保存</option>
				<option value="18041002">已下发</option>
				<option value="18041003">无效</option>
			</select>
		</td>
		</td>
		<td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">借出人：</td>
		<td width="15%">
			<input name="borrow_man" type="text" id="borrow_man" class="middle_txt" maxlength="30" value="" />
		</td>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true"></td>
		<td width="15%">
		</td>
	</tr>
	<tr>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true"></td>
		<td width="15%">
		</td>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">是否创建：</td>
		<td width="15%"  nowrap="true">
			<select class="short_sel" name="is_return">
				<option value="">--请选择--</option>
				<option value="0">未创建</option>
				<option value="1">已创建</option>
			</select>
		</td>
		</td>
		<td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">索赔单:</td>
		<td width="15%">
			<input name="claim_no" type="text" id="claim_no" class="middle_txt" maxlength="30" value="" />
		</td>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true"></td>
		<td width="15%">
		</td>
	</tr>
    <tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="btnQuery"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="btnQuery"  value="新增"  class="normal_btn" onClick="newAdd();" >
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="btnQuery"  value="维护人员"  class="normal_btn" onClick="mainTain();" >
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
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/claim/oldPart/EmergencyDevice/listShowDataView.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'},
		{header: "申请部门", dataIndex: 'APPLY_DEPT', align:'center'},
		{header: "借件人", dataIndex: 'BORROW_PERSON', align:'center'},
		{header: "借件部门", dataIndex: 'BORROW_DEPT', align:'center'},
		{header: "收件人", dataIndex: 'CONSIGNEE_PERSON', align:'center'},
		{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
		{header: "服务站简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
		{header: "状态", dataIndex: 'STATUS', align:'center',renderer:link},
		{header: "服务站是否创建", dataIndex: 'IS_RETURN', align:'center',renderer:link_1},
		{header: "创建时间", dataIndex: 'CREATE_DATE', align:'center'},
		{header: "下发时间", dataIndex: 'NEXT_TIME', align:'center',renderer:show}
	];
	function show(value,meta,record){
		if(value==""||value==null){
			return String.format('--');
		}else{
			return String.format(value);
		}
	}
	function link_1(value,meta,record){
		if(value=="0"){
			return String.format("未创建");
		}
		if(value=="1"){
			return String.format("已创建");
		}
	}
	function link(value,meta,record){
		if(value=="18041001"){
			return String.format("已保存");
		}
		if(value=="18041002"){
			return String.format("已下发");
		}
		if(value=="18041003"){
			return String.format("失效");
		}
	}
	//超链接设置
	   function myLink(value,meta,record){
		var status=record.data.STATUS;
		var is_return=record.data.IS_RETURN;
		 if(status==18041001){ 
			return String.format("<a href='#' onClick='forward1("+value+",3)'>[明细]</a><a href='#' onClick='forward("+value+",2)'>[删除]</a><a href='#' onClick='forward1("+value+",1)'>[修改]</a>");
		 }
		 if(status==18041002){
			 var url="";
			 if("0"==is_return){
				 url+="<a href='#' onClick='forward("+value+",2)'>[撤销删除]</a>"
			 }
			 url+="<a href='#' onClick='forward1("+value+",3)'>[明细]</a>";
			return String.format(url);
		} 
		 if(status==18041003){ 
			return String.format("<a href='#' onClick='forward1("+value+",3)'>[明细]</a>");
		 }
			
	}
	function forward1(value,type){
		var url="<%=contextPath%>/claim/oldPart/EmergencyDevice/forward.do?id="+value+"&type="+type;
		fm.action=url;
		fm.method="post";
	    fm.submit(); 
	}
	function forward(value,type){
		MyConfirm("是否确认删除？",delSure,[value,type]);
	}
	function delSure(value,type){
		var url="<%=contextPath%>/claim/oldPart/EmergencyDevice/forward.json?id="+value+"&type="+type;
		sendAjax(url,delSureBack,"fm");
	}
	function delSureBack(json){
		if(json.succ=="1"){
			MyAlert("提示：删除成功！");
			__extQuery__(1);
		}else{
			MyAlert("提示：删除失败！");
		}
	}
	function wrapOut(){
		$("dealer_id").value="";
		$("dealer_code").value="";
	}
</script>
<!--页面列表 end -->
</html>
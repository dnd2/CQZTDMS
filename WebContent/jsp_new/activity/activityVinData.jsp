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
	
	var url = "<%=contextPath%>/ActivityAction/ActivityVinData.json?query=true&activity_id="+${activity_id};//url查询
	var title = null;//头标
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\"  onclick='selectAll(this,\"check\")' />全选", align:'center',sortable:false, dataIndex:'ACTIVITY_ID',width:'2%',renderer:checkBoxShow},
		{header: "VIN", dataIndex: 'VIN', align:'center'},
		{header: "关联商家代码", dataIndex: 'DEALER_CODE', align:'center'},
		{header: "关联商家简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
		{header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
		{header: "是否销售", dataIndex: 'SALE_STATUS', align:'center',renderer:getItemValue},
		{header: "是否维修", dataIndex: 'REPAIR_STATUS', align:'center',renderer:getItemValue},
		{header: "实际维修商家代码", dataIndex: 'DEALER_CODE_REAL', align:'center'},
		{header: "实际维修商家简称", dataIndex: 'DEALER_SHORTNAME_REAL', align:'center'}
	];
	function checkBoxShow(value,meta,record){
		if(record.data.REPAIR_STATUS=="10481002"){
			return String.format("<input type='checkbox' id='check' name='check' value='" + record.data.ACTIVITY_ID +","+record.data.VIN+"' />");
		}else{
			return String.format("");
		}
	}
	function wrapOut(){
		$('dealer_id').value='';
		$('dealer_code').value='';
	}
	function wrapOut1(){
		$('dealer_id_real').value='';
		$('dealer_code_real').value='';
	}
	function bntDelAll(){
		var ids=document.getElementsByName("check");
		var idss="";
		for(var i=0;i<ids.length;i++){
			if(ids[i].checked){
				idss+=ids[i].value+";";
			}
		}
		if(idss==""){
			MyAlert("提示：请先选择一条数据！");
			return;
		}
		sendAjax('<%=contextPath%>/ActivityAction/bntDelAll.json?ids='+idss,back,'fm');

	}
	function back(json){
		if(json.succ=="1"){
			MyAlert("提示：当前页数据已经删除成功！");
			__extQuery__(1);
		}else{
			MyAlert("提示：当前页数据删除失败！");
		}
	}
	function expotData(){
		   fm.action="<%=contextPath%>/ActivityAction/expotActivityVinData.do";
	       fm.submit();
	}
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动VIN管理
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<input type="hidden" name="activity_id" value="${activity_id}"/>
	<tr>
		<td width="12.5%"></td>
		<td width="10%"  nowrap="true">关联商家名称：</td>
      	<td width="15%" nowrap="true">
      			<input class="middle_txt" id="dealer_code"  name="dealerCode" type="text" onclick="showOrgDealer('dealer_code','dealer_id','true','','false','','10771002');" readonly="readonly"/>
				<input type="hidden" name="dealerId" id="dealer_id"/>
 				<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">大区选择：</td>
      	<td width="15%" nowrap="true">
      	   <select id="root_org_id" name="root_org_id" class="short_sel">
							<option value="">--请选择--</option>
							<c:forEach var="org" items="${orglist}">
								<option value="${org.ORG_ID}" title="${org.ORG_NAME}">${org.ORG_NAME}</option>
							</c:forEach>
			</select>
		</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">是否维修：</td>
		<td width="15%" nowrap="true">
				<script type="text/javascript">
  					genSelBoxExp("repair_status",<%=Constant.SERVICEACTIVITY_REPAIR_STATUS%>,"",true,"short_sel","","false",'');
  				</script>
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td width="10%"  nowrap="true">实际维修商家简称：</td>
      	<td width="15%" nowrap="true">
      			<input class="middle_txt" id="dealer_code_real"  name="dealerCodeReal" type="text" onclick="showOrgDealer('dealer_code_real','dealer_id_real','true','','false','','10771002');" readonly="readonly"/>
				<input type="hidden" name="dealerIdReal" id="dealer_id_real"/>
 				<input type="button" value="清除" class="normal_btn" onclick="wrapOut1();"/>
      	</td>
        <td width="10%" nowrap="true" class="table_query_2Col_label_6Letter">VIN:</td>
      	<td width="15%" nowrap="true">
      		<input type="text" class="middle_txt" name="vin" id="vin" maxlength="30"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="btnQuery" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button"  name="bntDel" id="bntDel" onclick="bntDelAll();" value="删除" class="normal_btn" />
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
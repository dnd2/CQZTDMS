<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/jstl/cout"%>
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>退换货解封审核</title>
<script type="text/javascript">
$(function(){
	selectQuery();
});
    var myPage;
    var url = '<%=contextPath%>/parts/storageManager/partReturns/ReturnPartStateChange/getReturnPartJFAppInfos.json';
    var title = null;
    var columns = [
    		//renderer:getItemValue
			{header: "序号", renderer: getIndex},
			{id:'action',header: "操作",sortable: false,dataIndex: 'UNLOC_ID',renderer:myLink , style: 'text-align: center'},
			{header: "解封单号",dataIndex:'UNLOC_CODE', style:"text-align: center"},
			{header: "申请单位编码",dataIndex:'DEALER_CODE', style:"text-align: center"},
			{header: "申请单位名称",dataIndex:'DEALER_NAME', style:"text-align: center"},
			{header: "金额",dataIndex:'AMOUNT', style:"text-align: center"},
			{header: "状态",dataIndex:'STATE', style:"text-align: left",renderer:getItemValue},
			{header: "提交人",dataIndex:'SUBMIT_BY_CN', style:"text-align: center"},
			{header: "提交日期",dataIndex:'SUBMIT_DATE', style:"text-align: center"},
			{header: "创建日期",dataIndex:'CREATE_DATE', style:"text-align: center"},
			{header: "创建人",dataIndex:'CREATE_BY_CN', style:"text-align: center"},
			{header: "备注",dataIndex:'REMARK', style:"text-align: center"}
        ];

//操作链接生成
function myLink(value,meta,record){
	var state = record.data.STATE;
	var str = '<a href="javascript:void(0)" onclick="toReturnPartSelect(\''+value+'\', \''+state+'\')">[查看]</a>&nbsp;';
	if(state=='<%=Constant.RC_JF_STATE_02%>'){
		str+= '<a href="javascript:void(0)" onclick="toReturnPartAppAudit(\''+value+'\', \''+state+'\')">[审核]</a>&nbsp;';
	}
	return str;
}

//btnEnable();btnDisable();//全局

//报表特殊时间控制查询
function selectQuery(){
	__extQuery__(1);
}

//查看
function toReturnPartSelect(value, state){
	urlkey = '<%=contextPath%>/parts/storageManager/partReturns/ReturnPartStateChange/toReturnPartSelect.do?value='+value+'&state='+state;
	OpenHtmlWindow(urlkey,900,500);
}
//审核
function toReturnPartAppAudit(value, state){
	location = '<%=contextPath%>/parts/storageManager/partReturns/ReturnPartStateChange/toReturnPartAppAudit.do?value='+value+'&state='+state;
}
</script>
</head>
<body onload="">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件仓储管理&gt; 配件退换货状态变更&gt; 退换货解封审核
		</div>
		<form method="post" name="fm" id="fm" enctype="multipart/form-data">
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">解封单号：</td>
							<td>
								<input class="middle_txt" type="text" id="UNLOC_CODE" name="UNLOC_CODE" value="" />
							</td>
							<td class="right">创建时间：</td>
							<td>
								<input name="startDate" readonly="readonly" id="startDate" value="${oldDate}" type="text" class="short_txt" datatype="1,is_date,10" group="startDate,endDate" style="width:80px;"/>
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" onclick="showcalendar(event, 'startDate', false);" />
								<label>至</label>
								<input name="endDate" readonly="readonly" id="endDate" value="${newDate}" type="text" class="short_txt" datatype="1,is_date,10" group="startDate,endDate" style="width:80px;"/>
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" onclick="showcalendar(event, 'endDate', false);" />
							</td>
							<td class="right" style="display: none;">状态：</td>
							<td style="display: none;">
								<input type="hidden" name="STATE" value="<%=Constant.RC_JF_STATE_02%>">
							</td>
						</tr>

						<tr>
							<td class="center" colspan="6">
								<input type="button" class="u-button" name="BtnQuery" id="queryBtn" onclick="selectQuery()" value="查询" />
								&nbsp;&nbsp;
							</td>
						</tr>
					</table>
				</div>
			</div>

			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>

	</div>
</body>
</html>
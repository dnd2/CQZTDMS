<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>售后经销商查询</title>
<script type="text/javascript">
var dealerLevel=<%=Constant.DEALER_LEVEL_01%>;
function doInit()
{
	loadcalendar();
	__extQuery__(1);
}   
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;服务站备案信息更改审核</div>
<form method="post" id="fm" name="fm">
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
<input id="user_id" name="user_id" type="hidden" value="${user_id}"/>
<input type="hidden" name="curPage" id="curPage" value="${curPage}" />
<input id="DEALER_TYPE" name="DEALER_TYPE" value="<%=Constant.DEALER_TYPE_DVS %>" type="hidden"/>
<table class="table_query" border="0">
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商代码：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_CODE" maxlength="30" datatype="1,is_noquotation,30" id="DEALER_CODE" type="text" class="middle_txt" /></td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商简称：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_NAME" maxlength="30" datatype="1,is_noquotation,75" id="DEALER_NAME" type="text" class="middle_txt" /></td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">修改类型：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
		<select id="itemName" name="itemName">
		<option value="">-请选择-</option>
		<option value="服务经理">服务经理</option>
		<option value="备件接收人">备件接收人</option>
		<option value="备件接收地址">备件接收地址</option>
		<option value="24小时热线">24小时热线</option>
		<option value="索赔主管">索赔主管</option>
		<option value="技术主管">技术主管</option>
		<option value="备件主管">备件主管</option>
		<option value="服务主管">服务主管</option>
		</select>
		</td>
		<td align="right" nowrap="true">提报时间：</td>
		<td align="left" nowrap="true"><input class="short_txt" id="dealStart" name="dealStart" datatype="1,is_date,10" maxlength="10" group="dealStart,dealEnd" /> <input
			class="time_ico" value=" " onclick="showcalendar(event, 'dealStart', false);" type="button" /> 至 <input class="short_txt" id="dealEnd" name="dealEnd" datatype="1,is_date,10"
			maxlength="10" group="dealStart,dealEnd" /> <input class="time_ico" value=" " onclick="showcalendar(event, 'dealEnd', false);" type="button" /></td>
	</tr>
	<tr class="csstr" align="center">
		<td align="right">大区：</td>
		<td align="left">
			<select id="__large_org" name="__large_org" class="short_sel" onchange="changeOrg(this.value)">
				<option value="">--请选择--</option>
				<c:forEach var="org" items="${orglist}">
					<option value="${org.ORG_ID}" title="${org.ORG_NAME}">${org.ORG_NAME}</option>
				</c:forEach>
			</select>
		</td>
	<td align="right">省份：</td> 
  	<td align="left" colspan="3">
		<select id="__province_org" name="__province_org"><option value="">==请选择==</option></select>
	</td> 
 	</tr> 
	<tr align="center">
		<td colspan="4" class="table_query_4Col_input" style="text-align: center">
			<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
			<input name="queryBtn" type="button" class="normal_btn" onclick="batchAccept()" value="通过" id="queryBtn" /> &nbsp;
			<input name="queryBtn" type="button" class="normal_btn" onclick="batchRebut()" value="驳回" id="queryBtn" /> &nbsp;
		</td>
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
</form>
<script>
var myPage;
var url = "<%=contextPath%>/sysmng/dealer/ChangeServiceDealerInfo/queryAuditServiceDealerInfo2.json";
var title= null;
var columns = [
{header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"codeIds\")'/>", width:'8%',sortable: false,dataIndex: 'ID',renderer:myCheckBox},
				{header: "经销商代码",width:'10%',   dataIndex: 'DEALER_CODE'},
				{header: "经销商名称",width:'10%', dataIndex: 'DEALER_NAME'},
				{header: "状态", width:'10%', dataIndex: 'AUDIT_STATUS',renderer:getItemValue},
				{header: "备注", dataIndex: 'REMARK',renderer:getText},
				{header: "修改项目", width:'10%', dataIndex: 'ITEM_NAME'},
				{header: "原姓名", width:'10%', dataIndex: 'OLD_NAME'},
				{header: "新姓名", width:'20%', dataIndex: 'NAME'},
				{header: "原手机", width:'20%', dataIndex: 'OLD_MOBIL'},
				{header: "新手机", width:'20%', dataIndex: 'MOBIL'},
				{header: "原座机",width:'10%',   dataIndex: 'OLD_PHONE'},
				{header: "新座机", width:'10%', dataIndex: 'PHONE'},
				{header: "原邮箱", width:'10%', dataIndex: 'OLD_EMAIL'},
				{header: "新邮箱", width:'10%', dataIndex: 'EMAIL'},
				{header: "原传真", width:'10%', dataIndex: 'OLD_FAX'},
				{header: "新传真", width:'10%', dataIndex: 'FAX'},
				{header: "原接收地址", width:'10%', dataIndex: 'OLD_ADDRESS'},
				{header: "新接收地址", width:'10%', dataIndex: 'ADDRESS'},
				{header: "原发票地址", width:'10%', dataIndex: 'OLD_FAPIAO_ADDRESS'},
				{header: "新发票地址", width:'10%', dataIndex: 'FAPIAO_ADDRESS'},
				{header: "服务站变更提交时间", width:'10%', dataIndex: 'CREATE_DATE'},
				{header: "审核人", width:'10%', dataIndex: 'AUDIT_BY_DEALER_NAME'},
				{header: "审核时间", width:'10%', dataIndex: 'AUDIT_DATE_DEALER'}
			  ];
function myCheckBox(value,metaDate,record){
	return String.format("<input type='checkbox' name='codeIds' value='" + value +"' />");
}
function getText(value,meta,record){
	var ID = record.data.ID;
		return String.format("<input name='remark_"+ID+"'  id='remark_"+ID+"' type='text' value='"+value+"'  class='long_txt'>");
}
function batchAccept(){		
	if (confirm("确认要批量通过?")){
		var delIds = new Array(); 
		var cnt = 0;
		var chk=document.getElementsByName("codeIds");
		var l = chk.length;
		for(var i=0;i<l;i++){
			if(chk[i].checked){
		       cnt++;
		       delIds.push(chk[i].value);
			}
		 }
		if(cnt==0){
		    MyAlert("请勾选要通过的数据！");
		    return ;
		}
		var url = '<%=contextPath%>/sysmng/dealer/ChangeServiceDealerInfo/batchAccept2.do?ID='+delIds;
	    fm.action = url;
	    fm.submit();
	}
}
function batchRebut(){		
	if (confirm("确认要驳回?")){
		var delIds = new Array(); 
		var cnt = 0;
		var chk=document.getElementsByName("codeIds");
		var l = chk.length;
		for(var i=0;i<l;i++){
			if(chk[i].checked){
		       cnt++;
		       delIds.push(chk[i].value);
			}
		 }
		if(cnt==0){
		    MyAlert("请勾选要驳回的数据！");
		    return ;
		}
		var url = '<%=contextPath%>/sysmng/dealer/ChangeServiceDealerInfo/batchRebut2.do?ID='+delIds;
	    fm.action = url;
	    fm.submit();
	}
}
function updateInfo() {
	var url = "<%=contextPath%>/sysmng/dealer/ChangeServiceDealerInfo/updateServiceDealerInfoInit.do";
	fm.action = url;
 	fm.submit();
}
function formatDate(value,meta,record){
	if(value!=null && value!=""){
		return value.substring(0,7).replace("-","");
	}
	else{
		return "";
	}
}
function clrTxt(txtId){
   	document.getElementById(txtId).value = "";
}
function exportFunc() {
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfoQuery/dealerInfoDownloadNew.do?DEALER_TYPE=<%=Constant.DEALER_TYPE_DVS%>" ;
	document.fm.action = url ;
	document.fm.submit() ;
}
</script>
</body>
</html>

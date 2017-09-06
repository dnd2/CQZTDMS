<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infoservice.mvc.context.RequestWrapper"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ taglib uri="/jstl/fmt" prefix="fmt"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件打款审核</title>

<script type="text/javascript" >
$(function(){
	__extQuery__(1);
});
var myPage;
var calculateConfig = {subTotalColumns:" |DEALER_NAME"};
//查询路径           
var url = "<%=request.getContextPath()%>/parts/financeManager/dealerAccImpRecordManager/PartDaKuanAudit/xianKuanDakuanAuditQuery.json";
var title = null;
var columns = [
    {header : "序号", sortable : false, dataIndex : 'PARENTORG_ID',  renderer : getIndex}, 
    //{id:'action', dataIndex : 'STATUS',header : '<input type="checkbox" name="checkAll" onclick="selectAll(this,\'checkids\')"/>', renderer : mySelect}, 
    {header : "操作", sortable : false, dataIndex : 'STATUS', align : 'center', renderer : myLink}, 
    {header : "流水号",dataIndex : 'PZ_NO',align : 'center'}, 
	{header : "汇款用途",dataIndex : 'FIN_TYPE',align : 'center',renderer: getItemValue}, 
	{header : "经销商/经销商",dataIndex : 'DEALER_NAME',align : 'center', style: 'text-align: left'},
	{header : "打款金额",dataIndex : 'AMOUNT',style : 'text-align:right',renderer: formatCurrency}, 
	{header : "打款时间",dataIndex : 'DK_DATE'},
	{header : "状态",dataIndex : 'STATUS',align : 'center', renderer:getStatus}, 
	{header : "备注",dataIndex : 'REMARK',align : 'center', style: 'text-align: left'}
];

function formatCurrency(num,metadata,record) {
	num = num.toString().replace(/\$|\,/g,'');
	if(isNaN(num))
		num = "0";
	sign = (num == (num = Math.abs(num)));
	num = Math.floor(num*100+0.50000000001);
	cents = num%100;
	num = Math.floor(num/100).toString();
	if(cents<10)
		cents = "0" + cents;
	for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
		num = num.substring(0,num.length-(4*i+3))+','+
	num.substring(num.length-(4*i+3));
	return (((sign)?'':'-') + '' + num + '.' + cents);
}

function myLink(value, meta, record) {
var t = document.getElementById("parentOrgId").value;
	if(value == "<%=Constant.TRANSFER_STATUS_02%>") {
		if(t==<%=Constant.OEM_ACTIVITIES%>&&record.data.PARENTORG_ID!="<%=Constant.OEM_ACTIVITIES%>"){
			return "";
		}else{
		var link = "<a  href=\"#\" id='passAuditBtn' onclick=\"passAudit('"+ record.data.DETAIL_ID +"','"+ record.data.FIN_TYPE +"','"+ record.data.DEALER_ID +"','"+ record.data.AMOUNT +"');\">[审核通过]</a>";
			link += "&nbsp;&nbsp;<a href=\"#\" id='unpassAuditBtn' onclick=\"rejectAudit('"+ record.data.DETAIL_ID +"','"+ record.data.FIN_TYPE +"','"+ record.data.DEALER_ID +"','"+ record.data.AMOUNT +"');\">[审核驳回]</a>";
		return String.format(link);
		}
	} else {
		return "";
	}
}

function passAudit(detail_id, fin_type, dealer_id, amount) {
	audit(detail_id, fin_type, dealer_id, amount, 1);
}

function rejectAudit(detail_id, fin_type, dealer_id, amount) {
	audit(detail_id, fin_type, dealer_id, amount, 2);
}

function audit(detail_id, fin_type, dealer_id, amount, type) {
	var msg = "";
	if(type == 1) {
		msg = "审核通过?";
	} else {
		msg = "审核驳回?";
	}
	var auditRemark = document.getElementById('audit_remark').value;
	MyConfirm(msg,toSubmit,[detail_id, fin_type, dealer_id, amount, type, auditRemark]);
}

function toSubmit(detail_id, fin_type, dealer_id, amount, type, auditRemark){
	document.getElementById('passAuditBtn').style.display='none';
	document.getElementById('unpassAuditBtn').style.display='none';
	makeCall("<%=request.getContextPath()%>/parts/financeManager/dealerAccImpRecordManager/PartDaKuanAudit/zhengcheDakuanAuditProcess.json",showFunc,{detail_id:detail_id, fin_type:fin_type, dealer_id:dealer_id, amount:amount, type:type, auditRemark: auditRemark});
}

function showFunc(json){
	
	 var success = json.success;
           var error = json.error;
           var exceptions = json.Exception;
	if(json.errcode == 0) {
		MyAlert("操作成功！");
		//MyAlertForFun("操作成功！",reload);
	}else if (error) {
              MyAlert(error);
          } else if (exceptions) {
              MyAlert(exceptions.message);
          }
	reload();
}

function reload() {
	__extQuery__(1);
}

window.document.onkeydown = function (){
	if(event.keyCode==13){
		__extQuery__(1,valflagTemp);
	};
}

function getStatus(value) {
	if(value == "<%=Constant.TRANSFER_STATUS_02%>") {
		return "待审核";
	} else {
		return "审核通过";
	}
}

//设置金钱格式
function myformat(value, metaDate, record) {
	return String.format(amountFormat(value));
}
function clrTxt(id) {
	document.getElementById(id).value = '';
}
</script>
</head>
<body>
	<div id="div1" class="wbox">
		<div class=navigation>
			<img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;当前位置： 配件财务管理 &gt; 配件打款登记审核
		</div>
		<form method="post" name="fm" id="fm" enctype="multipart/form-data">
			<!-- 查询条件 begin -->
			<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId}" />

			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">经销商：</td>
							<td>
								<input type="hidden" class="middle_txt" name="dealerCode" readonly="readonly" value="" id="dealerCode" />
								<input type="text" class="middle_txt" name="dealerName" readonly="readonly" size="20" value="" id="dealerName" />
								<input id="dealerBtn" name="dealerBtn" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','dealerIds','true','','','','','dealerName');" value="..." />
								<input type="hidden" name="dealerIds" id="dealerIds" value="" />
								<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');clrTxt('dealerIds');clrTxt('dealerName');" />
							</td>
							<td class="right">汇款用途：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("acount_kind", <%=Constant.PART_ACCOUNT_PURPOSE_TYPE%>, "<%=Constant.PART_ACCOUNT_PURPOSE_TYPE_01%>", true, "u-select", "", "false", '');
			                    </script>
							</td>
						</tr>
						<tr>
							<td class="right">流水号：</td>
							<td>
								<input type="text" id="pz_no" class="middle_txt" name="pz_no" />
							</td>
							<td class="right">审核状态：</td>
							<td>
								<select name="status" id="status" class="u-select">
									<option selected="selected" value="<%=Constant.TRANSFER_STATUS_02%>">待审核</option>
									<option value="<%=Constant.TRANSFER_STATUS_03%>">审核通过</option>
								</select>
							</td>
						</tr>
						<tr>
							<td colspan="6" class="center">
								<input class="u-button" value=查询 type=button id=queryBtn onclick="__extQuery__(1);" />
								&nbsp;&nbsp;
								<input class="u-button" value=重置 type="reset" />
							</td>
						</tr>
						<tr>
							<td class="right"><font style="color: #3a459a; font-weight: bolder; font-size: 13px;">审批意见：</font></td>
							<td colspan="5">
								<input type="text" class="middle_txt" name="audit_remark" id="audit_remark" style="width: 400px;" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<!-- 查询条件 end -->
			<!-- 分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
</body>
</html>
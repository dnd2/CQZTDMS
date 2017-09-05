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
<title>退换货解封单审核</title>
<script type="text/javascript">
//返回
function goback(){
	location = '<%=contextPath%>/parts/storageManager/partReturns/ReturnPartStateChange/toReturnPartAudit.do';
}
//驳回
function dontAgreeWithUnloc(){
	var checkRemark = $('#checkRemark').val();
	if(checkRemark==''){
		MyAlert('请输入审核意见！');
		$('#checkRemark').focus();
		return;
	}
	MyConfirm('确认驳回该申请？',function(){
		btnDisable();
		var urlkey = '<%=contextPath%>/parts/storageManager/partReturns/ReturnPartStateChange/dontAgreeWithUnloc.json';
		makeNomalFormCall(urlkey, getResult, 'fm');
    });
	
}
//通过
function agreeWithUnloc(){
	MyConfirm('确认通过该申请？', function(){
		btnDisable();
		var urlkey = '<%=contextPath%>/parts/storageManager/partReturns/ReturnPartStateChange/agreeWithUnloc.json';
		makeNomalFormCall(urlkey, getResult, 'fm');
	});
}

//操作结果
function getResult(json){
	var success = json.success;
	var error = json.error;
	var ex = json.Exception;
	if(success!=null && success!='' && success!='null' && success!='undefined'){
		MyAlert(success, goback);
	}else if(error!=null && error!='' && error!='null' && error!='undefined'){
		MyAlert(error);
	}else if(ex!=null && ex!='' && ex!='null' && ex!='undefined'){
		MyAlert(json.Exception.message);
	}else{
		MyAlert("操作失败，请联系管理员！");
	}
	btnEnable();
}
</script>

</head>
<body>
<style type="text/css">
.table_list_row0 td {
	background-color: #FFFFCC;
	border: 1px solid #DAE0EE;
	white-space: nowrap;
}
</style>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件仓储管理&gt; 配件退换货状态变更&gt; 退换货解封单审核 &gt; 审核
		</div>
		<form method="post" name="fm" id="fm" enctype="multipart/form-data">
			<input type="hidden" name="unlocId" value="${mains.UNLOC_ID }">
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 主要数据
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">制单人：</td>
							<td>${mains.CREATER_NAME}</td>
							<td class="right">制单时间：</td>
							<td>${mains.CREATE_DATE}</td>
						</tr>
						<tr>
							<td class="right">申请单位：</td>
							<td>${mains.DEALER_NAME }</td>

							<td class="right">申请人：</td>
							<td>${mains.SUBMITER_NAME}</td>
						</tr>
						<tr>
							<td class="right">解封原因：</td>
							<td colspan="3">${mains.REMARK}</td>
						</tr>
						<tr>
							<td class="right">审核意见：</td>
							<td colspan="3">
								<textarea class="form-control" style="width: 80%;" id="checkRemark" name="checkRemark" rows="3"></textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<table id="savefile" class="table_list" style="border-bottom: 1px;">
				<tr>
					<th colspan="15" align="left">
						<img src="<%=contextPath%>/img/nav.gif" />&nbsp;配件解封明细
					</th>
				</tr>
				<tr class="table_list_row0">
					<td>序号</td>
					<td>退换单号</td>
					<td>销售单号</td>
					<td>入库单号</td>
					<td>配件编码</td>
					<td>配件名称</td>
					<td>配件件号</td>
					<td>单位</td>
					<td>申请数量</td>
				</tr>

				<c:forEach items="${mapList}" var="list" varStatus="v">
					<c:if test="${(v.count%2)==0}">
						<tr class="table_list_row2">
					</c:if>
					<c:if test="${(v.count%2)!=0}">
						<tr class="table_list_row1">
					</c:if>
					<td>${v.count}</td>
					<td>${list.RETURN_CODE}</td>
					<td>${list.SO_CODE}</td>
					<td>${list.IN_CODE}</td>
					<td>${list.PART_OLDCODE}</td>
					<td>${list.PART_CNAME}</td>
					<td>${list.PART_CODE}</td>
					<td>${list.UNIT}</td>
					<td>${list.APPLY_QTY}</td>
					</tr>
				</c:forEach>

			</table>
			<table class="table_query">
				<tr>
					<td class="center">
						<input type="button" class="u-button" onclick="agreeWithUnloc()" value="通过" />
						&nbsp;&nbsp;
						<input type="button" class="u-button" onclick="dontAgreeWithUnloc()" value="驳回" />
						&nbsp;&nbsp;
						<input type="button" class="u-button" onclick="goback()" value="返回" />
						&nbsp;&nbsp;
					</td>
				</tr>
			</table>

		</form>

	</div>
</body>
</html>
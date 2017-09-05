<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>库存盘点调整处理</title>
<script type="text/javascript" >
$(function(){
	__extQuery__(1);
});
var myPage;

var url = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockInvReqSolveAction/partStockDetailSearch.json";
			
var title = null;

var columns = [
			{header: "序号", dataIndex: 'DTL_ID', renderer:getIndex,align:'center'},
			{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center'},
			{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'},
			{header: "件号", dataIndex: 'PART_CODE', align:'center'},
			{header: "单位", dataIndex: 'UNIT', align:'center'},
			{header: "批次号", dataIndex: 'BATCH_NO', align:'center'},
			{header: "货位", dataIndex: 'LOC_NAME', align:'center'},
			{header: "账面库存", dataIndex: 'ITEM_QTY', align:'center'},
			{header: "盘点库存", dataIndex: 'CHECK_QTY', align:'center'},
			{header: "盈亏库存", dataIndex: 'DIFF_QTY', align:'center'},
			{header: "盘点结果", dataIndex: 'CHECK_RESULT', align:'center',renderer:getItemValue},
			//{header: "处理状态", dataIndex: 'IS_OVER_TEXT', align:'center'},
			{header: "备注", dataIndex: 'REMARK', align:'center'},
			{id:'action',header: "操作",sortable: false,dataIndex: 'DTL_ID',renderer:myLink ,align:'center'}
	      ];

//设置超链接
function myLink(value,meta,record)
{
	var str = "";
	if(record.data.IS_OVER == '1'){
		str = "已处理";
	}else{
		var detailId = record.data.DTL_ID;
		var checkResult = record.data.CHECK_RESULT;
		var inveProfile = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02 %>;
		var inveLosses = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03 %>;
		
		var resolveType = document.getElementById("resolveType").value;

		if("处理" != resolveType){
			str = "<input type='button' class='u-button' onclick='confirmCommitSingle(\""+detailId+"\")' value='封 存'/>";
		}else if( inveProfile == checkResult){
			str = "<input type='button' class='u-button' onclick='confirmCommitSingle(\""+detailId+"\")' value='入 库'/>";
		}else if(inveLosses == checkResult){
			str = "<input type='button' class='u-button' onclick='confirmCommitSingle(\""+detailId+"\")' value='出 库'/>";
		}
	}
	
	return String.format(str);
}

function goBack(){
	btnDisable();
	fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockInvReqSolveAction/stockInvReqSolveInit.do";
	fm.submit();
	//window.history.back(-1);
}

function confirmCommitAll()
{
	var handleType = "all";
	var parms = "";
	MyConfirm("确定处理所有结果?",commitOrder,[[parms,handleType]]);
}

//处理单个
function confirmCommitSingle(parms)
{
	var handleType = "single";
	MyConfirm("确定处理该结果?",commitOrder,[[parms,handleType]]);
}

function commitOrder(paramArr)
{
	var parms = paramArr[0];
	var handleType = paramArr[1];
	btnDisable();
	var url = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockInvReqSolveAction/commitHandleResult.json?handleType=" + handleType + "&detailId=" + parms;	
	makeNomalFormCall(url,getResult,'fm');
}
function getResult(json){
	btnEnable();
	if(null != json){
        if (json.errorExist != null && json.errorExist.length > 0) {
        	MyAlert(json.errorExist);
        } else if (json.success != null) {
	        if("single" == json.success){
	        	MyAlert("操作成功!");
	        	__extQuery__(json.curPage);
	        }else if("all" == json.success){
	        	MyAlert("操作成功!", function(){
		        	window.location = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockInvReqSolveAction/stockInvReqSolveInit.do";
	        	});
	        }
        } else {
            MyAlert("操作失败，请联系管理员!");
        }
	}
}

</script>

</head>
<style type="text/css">
#myTable {
	margin-top: 0px !important;
}

#_page {
	margin-top: 0px !important;
}
</style>
<body>
	<div class="wbox">
		<form method="post" name="fm" id="fm">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置： 配件仓库管理&gt;库存盘点管理&gt;库存盘点调整处理&gt;处理
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
				<input type="hidden" value="${map.RESULT_ID}" name="resultId" id="resultId" />
				<input type="hidden" value="${map.HANDLE_TYPE}" name="resolveType" id="resolveType" />
				<input type="hidden" value="${map.RESULT_CODE}" name="resultCode" id="resultCode" />
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 基本信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">盘点单号：</td>
							<td>${map.CHANGE_CODE}</td>
							<td class="right">盘点仓库：</td>
							<td>${map.WH_CNAME}</td>
							<td class="right"></td>
							<td></td>
						</tr>
						<tr>
							<td class="right">申请单号：</td>
							<td>
								${map.RESULT_CODE}
								<input type="hidden" value="${map.RESULT_ID}" name="resultId" id="resultId" />
							</td>
							<td class="right">导入人：</td>
							<td>${map.IMP_NAME}</td>
							<td class="right">导入日期：</td>
							<td>${map.CREATE_DATE}</td>
						</tr>
						<tr>
							<td class="right">单据状态：</td>
							<td>${map.STATE}</td>
							<td class="right">提交人：</td>
							<td>${map.COMM_NAME}</td>
							<td class="right">提交日期：</td>
							<td>${map.COMMIT_DATE}</td>
						</tr>
						<tr>
							<td class="right">盘点类型：</td>
							<td>${map.CHECK_TYPE}</td>
							<td class="right">审核人：</td>
							<td>${map.CHE_NAME}</td>
							<td class="right">审核日期：</td>
							<td>${map.CHECK_DATE}</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button u-submit" type="button" value="全部${map.HANDLE_TYPE}" onclick="confirmCommitAll()" />
								<input class="u-button u-cancel" type="button" value="返 回" onclick="goBack()" />
							</td>
						</tr>
					</table>
				</div>
			</div>

			<div class="form-panel" style="margin-top: 2px; margin-bottom: 0px;">
				<h2 style="border-bottom: 0px;">
					<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 盘点结果信息
				</h2>
			</div>
			<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
		</form>
	</div>
</body>
</html>
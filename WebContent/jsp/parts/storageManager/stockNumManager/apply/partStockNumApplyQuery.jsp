<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%>
<% String contextPath = request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>库存调整申请</title>
<script language="javascript" type="text/javascript">
$(function(){
	__extQuery__(1);
});

var myPage;
var url = "<%=contextPath%>/parts/storageManager/stockNumManager/StockNumApplyAction/queryStockNumApply.json";
var title = null;
var columns = [
			{header: "序号", dataIndex: 'ABJUSTMENT_ID', renderer:getIndex,align:'center'},
			{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'},
			{header: "调整单号", dataIndex: 'ABJUSTMENT_CODE', align:'center'},
			{header: "调整类型", dataIndex: 'ABJUSTMENT_TYPE', align:'center',renderer:getItemValue},
			{header: "调整仓库", dataIndex: 'WH_CNAME', align:'center'},
			{header: "调整状态", dataIndex: 'STATE', align:'center',renderer:getItemValue},
			{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
			{header: "创建日期", dataIndex: 'CREATE_DATE', align:'center'},
	      ];

//设置超链接
function myLink(value,meta,record)
{
	var id = record.data.ABJUSTMENT_ID;
	var applyState = record.data.APPLY_STATE;
	var status = record.data.STATUS;
	var html = "<a href=\"#\" onclick='viewDetail(\""+id+"\")'>[查看]</a>&nbsp;";
	if(status == <%=Constant.STATUS_ENABLE%>){
		if(applyState == <%=Constant.PART_ABJUSTMENT_APPLY_01%>){
			html += "<a href=\"#\" onclick='modifyDetail(\""+id+"\")'>[修改]</a>&nbsp;";
			html += "<a href=\"#\" onclick='submitApply(\""+id+"\")'>[提交申请]</a>&nbsp;";
			html += "<a href=\"#\" onclick='invalidApply(\""+id+"\",\""+record.data.ABJUSTMENT_TYPE+"\")'>[失效]</a>&nbsp;";
		}
	}
	return String.format(html);
}

//新增
function addNew(){
	document.fm.action = "<%=contextPath%>/parts/storageManager/stockNumManager/StockNumApplyAction/toAddStockNumApply.do";
	document.fm.target = "_self";
	document.fm.submit();
}

//查看
function viewDetail(parms){
	document.fm.action="<%=contextPath%>/parts/storageManager/stockNumManager/StockNumApplyAction/toAbjustmentDetail.do?abjustmentId=" + parms +"&actionType=view";
	document.fm.target="_self";
	document.fm.submit();
}

//修改
function modifyDetail(parms){
	document.fm.action="<%=contextPath%>/parts/storageManager/stockNumManager/StockNumApplyAction/toAbjustmentDetail.do?abjustmentId=" + parms +"&actionType=edit";
	document.fm.target="_self";
	document.fm.submit();
}

// 提交申请
function submitApply(parms){
    MyConfirm("确定要提交申请?", submitApply2, [parms]);
}

function submitApply2(parms){
	btnDisable();
	var url = "<%=contextPath%>/parts/storageManager/stockNumManager/StockNumApplyAction/submitApply.json?abjustmentId="+parms+"&curPage="+myPage.page;
	makeNomalFormCall(url, submitApplyResult, 'fm');
}

function submitApplyResult(json) {
	btnEnable();
    if (json != null) {
    	if(json.returnCode == 1){
	        MyAlert(json.msg);
    	}else{
	        MyAlert(json.msg);
    	}
        __extQuery__(json.curPage);
    }
}

// 失效
function invalidApply(parms, type){
    MyConfirm("确定要将申请置为无效?", invalidApply2, [parms, type]);
}
function invalidApply2(parms, type){
	var url = "<%=contextPath%>/parts/storageManager/stockNumManager/StockNumApplyAction/invalidApply.json?abjustmentId="+parms+"&type="+type+"&curPage="+myPage.page;
	makeNomalFormCall(url, invalidResult, 'fm');
}
function invalidResult(json) {
    if (json != null) {
    	if(json.returnCode == 1){
	        MyAlert(json.msg);
    	}else{
	        MyAlert(json.msg);
    	}
        __extQuery__(json.curPage);
    }
}

//下载
function exportPartStockExcel(){
	document.fm.action="<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/exportPartStockStatusExcel.do";
	document.fm.target="_self";
	document.fm.submit();
}
</script>
</head>
<body>
<div class="wbox">
	<form method="post" name="fm" id="fm">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置：配件仓库管理 &gt;库存调整管理&gt;库存调整申请
			</div>
			<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">申请单号：</td>
							<td>
								<input class="middle_txt" type="text" name="abjustmentCode" id="abjustmentCode" />
							</td>
							<td class="right">创建日期：</td>
							<td>
								<input id="checkSDate" class="short_txt" style="width: 80px;" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
								至 
								<input id="checkEDate" class="short_txt" style="width: 80px;" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
							</td>
							<td class="right">是否有效：</td>
							<td>
								<script type="text/javascript">
									genSelBox("status",<%=Constant.STATUS%>,<%=Constant.STATUS_ENABLE%>,true,"u-select","","false","");
								</script>
							</td>
						</tr>
						<tr>
							<td class="right">调整仓库：</td>
							<td>
								<select name="whId" id="whId" class="u-select">
									<option value="">-请选择-</option>
									<c:if test="${WHList!=null}">
										<c:forEach items="${WHList}" var="list">
											<option value="${list.WH_ID }">${list.WH_CNAME }</option>
										</c:forEach>
									</c:if>
								</select>
							</td>
							<td class="right">调整类型：</td>
							<td>
								<script type="text/javascript">
									genSelBox("abjustmentType",<%=Constant.PART_ABJUSTMENT_TYPE%>,"",true,"u-select","","false","");
								</script>
					  		</td>
							<td class="right">调整状态：</td>
							<td>
								<script type="text/javascript">
									genSelBox("state",<%=Constant.PART_ABJUSTMENT_STATE%>,<%=Constant.PART_ABJUSTMENT_STATE_01%>,true,"","","false","");
							  	</script>
					  		</td>
						</tr>
						<tr>
							<td colspan="6" class="center">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" /> 
								<input class="u-button" type="reset" value="重 置" /> 
								<input class="u-button" type="button" value="新 增" onclick="addNew()" /> 
								<!-- <input class="u-button" type="button" value="导 出" onclick="exportPartStockExcel()" /> -->
							</td>
						</tr>
					</table>
				</div>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</div>
	</form>
</div>
</body>
</html>
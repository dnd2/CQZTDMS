<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件采购退货申请查询</title><script type="text/javascript">
$(function(){
	__extQuery__(1);
});
var myPage;

var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/queryPartOemReturnApplyInfo.json";

var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'RETURN_ID', renderer: myLink, align: 'center'},
    {header: "退货单号", dataIndex: 'RETURN_CODE', align: 'center'},
    {header: "退货类型", dataIndex: 'RETURN_TO', align: 'center'},
    {header: "退回供应商", dataIndex: 'RETURN_TO', align: 'center'},
    {header: "制单人", dataIndex: 'CREATE_NAME', align: 'center'},
    {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
    {header: "退货原因", dataIndex: 'REMARK', style: 'text-align: center;'},
    {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue},
//     {header: "提交日期", dataIndex: 'APPLY_DATE', align: 'center', renderer: formatDate}
];


//设置超链接
function myLink(value, meta, record) {
    var state = record.data.STATE;
    var rType = record.data.RETURN_TYPE;
    var html = "<a href=\"#\" onclick='view(\"" + value + "\"," + state + ")'>[查看]</a>";
    if(state == <%=Constant.PART_OEM_RETURN_STATUS_00%>){
		html += "<a href=\"#\" onclick='edit(\"" + value + "\"," + rType + ")'>[修改]</a>";
		html += "<a href=\"#\" onclick='submit(\"" + value + "\"," + rType + ")'>[提交]</a>";
    }
    html += "<a href=\"#\" onclick='PrintView(\"" + value + "\"," + rType + ")'>[打印]</a>";
    return String.format(html);
}

//查看申请详细页面
function view(value, state) {
    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/queryOemReturnApplyDetailInit.do?returnId=' + value + '&state=' + state + '&pageFlag=1';
}

// 修改
function edit(value, state) {
    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/modApplyInit.do?returnId=' + value + '&state=' + state + '&pageFlag=1';
}

// 提交
function submit(value, state) {
	MyConfirm("确定提交?", function(){
        var url = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/sumitReturnOrder.json?returnId=' + value + '&curPage=' + myPage.page;
        makeNomalFormCall(url, getResult, 'fm');
	});
}

//作废
function delReturn(value, rType) {
	MyConfirm("确定作废?", function(){
        btnDisable();
        var url = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/delReturnOrder.json?returnId=' + value + '&curPage=' + myPage.page + '&rType=' + rType;
        sendAjax(url, getResult, 'fm');
	});
}

function getResult(jsonObj) {
	btnEnable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            __extQuery__(jsonObj.curPage);
        } else if (exceptions) {
        	MyAlert(exceptions.message);
        } else if (error) {
        	MyAlert(error);
        }
    }
}

//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}

// 跳转新增退货单
function addInit() {
    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/generateApplyInit.do';
}


//打印 显示
function PrintView(value,type){
    window.open('<%=contextPath%>/parts/storageManager/partReturnManager/PartReturnQuery/partProcurementReturnPrint.do?value=' + value +'&type=' + type);
}

</script>

</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件仓库管理 >配件退货管理&gt;采购退货申请
		</div>
		<form name="fm" id="fm" method="post">
			<input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
			<input id="VENDER_NAME" name="VENDER_NAME" type="hidden" value="">
			<input id="STATE" name="STATE" type="hidden" value="<%=Constant.PART_OEM_RETURN_STATUS_00%>">
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
						<tr>
							<td class="right">退货单号：</td>
							<td>
								<input class="middle_txt" type="text" name="RETURN_CODE" id="RETURN_CODE" />
							</td>
							<td class="right">制单日期：</td>
							<td>
								<input name="startDate" id="t1" value="" type="text" style="width: 80px;" class="short_txt" datatype="1,is_date,10" group="t1,t2">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
								&nbsp;至&nbsp;
								<input name="endDate" id="t2" value="" type="text" style="width: 80px;" class="short_txt" datatype="1,is_date,10" group="t1,t2">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"  />
							</td>
							<td class="right">制单人：</td>
							<td colspan="3">
								<input class="middle_txt" type="text" name="CREATE_NAME" id="CREATE_NAME" />
							</td>
						</tr>
						<tr>
							<td colspan="6" class="center">
								<input name="BtnQuery" id="queryBtn" class="u-button" type="button" value="查 询" onclick="__extQuery__(1);" />
								<%-- <input name="button" type="button" class="long_btn" value="有单退货" onclick="addInit();"/>
                     &nbsp;--%>
								<input name="button" type="button" class="u-button" onclick="addInit();" value="新 增" />
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

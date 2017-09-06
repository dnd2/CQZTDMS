<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件销售退货回运</title>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});

var myPage;
var title = null;
var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnChkManager/queryPartDlrReturnApplyDetail.json";

var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', align: 'center'},
    {header: "配件名称", dataIndex: 'PART_CNAME', align: 'center'},
//     {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
//    {header: "采购数量", dataIndex: 'BUY_QTY', align: 'center'},
    {header: "申请退货数量", dataIndex: 'APPLY_QTY', align: 'center'},
    {header: "审核数量", dataIndex: 'CHECK_THREE_QTY', align: 'center'},
    {header: "回运数量", dataIndex: 'CHECK_THREE_QTY', align: 'center'},
    {header: "备注", dataIndex: 'REMARK', align: 'center'}
];
//回运
function agreeApply() {
    var wl = $("#wl").val();
    var wlNo = $("#wlNo").val();
    var wlDate = $("#wlDate").val();
    if (wl == '') {
        MyAlert("承运物流必填!")
        return;
    }
    if (wlNo == '') {
        MyAlert("物流单号必填!")
        return;
    }
    if (wlDate == '') {
        MyAlert("回运日期必填!")
        return;
    }
    
    MyConfirm("确定回运?", function(){
        btnDisable();
        var url = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnChkManager/PartDlrReturnBack.json';
        sendAjax(url, getResult, 'fm');
    });
}

function getResult(jsonObj) {
	btnEnable();
    if (jsonObj) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success, goback);
        }
        if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}
//返回查询页面
function goback() {
    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnChkManager/queryPartReturnBackInit.do';
}
</script>

</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件仓库管理 >配件退货管理&gt;销售退货回运&gt;回运信息填写
		</div>
		<form name="fm" id="fm" method="post">
			<input type="hidden" name="returnId" value="${po['RETURN_ID']}" />
			<input type="hidden" name="soCode" value="${po['SO_CODE']}" />
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 回运信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">退货单号：</td>
							<td>${po["RETURN_CODE"]}</td>
							<td class="right">申请单位：</td>
							<td>${po["DEALER_NAME"]}</td>
						</tr>

						<tr>
							<td class="right">承运物流：</td>
							<td>
								<input type="text" id="wl" name="wl" value="" maxlength="20" class="middle_txt" />
								<font style="color: red">*</font>
							</td>
							<td class="right">物流单号：</td>
							<td>
								<input type="text" id="wlNo" name="wlNo" value="" maxlength="20" class="middle_txt" />
								<font style="color: red">*</font>
							</td>
						</tr>
						<tr>
							<td class="right">回运日期：</td>
							<td>
								<input name="wlDate" id="wlDate" value="${old}" type="text" class="middle_txt" datatype="1,is_date,10" group="t1,t2" readonly>
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"  />
								<font style="color: red">*</font>
							</td>
						</tr>
						<tr>
							<td class="right">备注：</td>
							<td colspan="3">
								<textarea name="wlRemark" id="wlRemark" rows="4" class="form-control" style="width: 80%;"></textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>

			<div class="form-panel" style="margin-top: 2px; margin-bottom: 0px;">
				<h2 style="border-bottom: 0px;">
					<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 配件信息
				</h2>
			</div>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<table border="0" class="table_query">
				<tr>
					<td class="center">
						<input name="agreeBtn" id="agreeBtn" class="u-button" type="button" value="回 运" onclick="agreeApply();" />
						&nbsp;
						<input class="u-button" type="button" value="返 回" onclick="javascript:goback();" />
						&nbsp;
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>

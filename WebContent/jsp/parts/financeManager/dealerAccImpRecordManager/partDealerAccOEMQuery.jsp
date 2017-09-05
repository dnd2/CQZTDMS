<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>配件打款等级</title>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});
var myPage;
var url = "<%=contextPath%>/parts/financeManager/dealerAccImpRecordManager/partDealerAccImpRecAction/partDealerAccQueryQuery.json";
var title = null;

var columns = [
    {header: "序号", dataIndex: 'HISTRORY_ID', renderer: getIndex},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'HISTRORY_ID', renderer: myLink, align: 'center'},
    {header: "服务商编码", dataIndex: 'CHILDORG_CODE'},
    {header: "服务商名称", dataIndex: 'CHILDORG_NAME', style: 'text-align:left' },
    {header: "首批日期", dataIndex: 'DK_DATE'},
    {header: "首批金额", dataIndex: 'AMOUNT', style: 'text-align:right' },
    {header: "备注", dataIndex: 'REMARK', style: 'text-align:left' },
    {header: "登记人", dataIndex: 'NAME'}
];
function myLink(value, meta, record) {
    var status = record.data.STATUS;
    if (status == "-1") {//未提及或已驳回
    	var a1 = String.format("<a href='#' onclick='toEdit(" + value + ")'>[修改]</a>");
//            var a2 = String.format("<a href='#' onclick='toSubmit(" + value + ")'>[提交]</a>");
        var a3 = String.format("<a href='#' onclick='toDelete(" + value + ")'>[作废]</a>");
        return a1 + a3;
    } else {
        return "已提交";
    }
}
//编辑
var flag = $("flag").value;
function toEdit(value) {
    window.location.href = "<%=request.getContextPath()%>/parts/financeManager/dealerAccImpRecordManager/partDealerAccImpRecAction/partDealerAccQueryEdit.do?hid=" + value + "&flag=" + flag;
}

//提交
function toSubmit(value) {
    MyConfirm("提交后将不能修改，确认提交?", toSubmit1, [value]);

}
function toSubmit1(value) {
    makeCall("<%=request.getContextPath()%>/parts/financeManager/dealerAccImpRecordManager/partDealerAccImpRecAction/partDealerAccQueryMng.json", showFunc, {hid: value, opType: 'SUB'});
}
//删除
function toDelete(value) {
    MyConfirm("作废后将不再显示，确认作废?", toDelete1, [value]);
}
function toDelete1(value) {
    makeCall("<%=request.getContextPath()%>/parts/financeManager/dealerAccImpRecordManager/partDealerAccImpRecAction/partDealerAccQueryMng.json", showFunc, {hid: value, opType: 'DEL'});
}
//新增
function Add() {
    window.location.href = "<%=request.getContextPath()%>/parts/financeManager/dealerAccImpRecordManager/partDealerAccImpRecAction/partDealerAccQueryAdd.do?flag=" + flag;
}
function showFunc(json) {
    MyAlert(json.ACTION_RESULT);
    window.location.reload();
}
</script>

</head>
<body>
	<div class="wbox">
		<form method="post" name="fm" id="fm">
			<input type="hidden" id="flag" name="flag" value="1" />

			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理 &gt; 配件财务管理 &gt; 配件首批款登记
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">日期：</td>
							<td>
								<input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
								&nbsp;至&nbsp;
								<input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
							</td>
							<td class="right">备注：</td>
							<td>
								<input class="middle_txt" type="text" name="remark" id="remark" />
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
								<input class="normal_btn" type="button" value="新 增" name="BtnAdd" id="BtnAdd" onclick="Add();" />
							</td>
						</tr>
					</table>
				</div>
			</div>

			<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
		</form>
</body>
</div>
</html>
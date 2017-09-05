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
<title>销售退货查询</title><script type="text/javascript">
$(function(){
	query(1);
});
var myPage;
var title = null;
var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartReturnQuery/queryPartDlrReturnInfo.json";
var columns = [];
function query(value) {
	if(!value){
		value = "1";
	}
	if (value == "1") {//发运跟踪
	    columns = [
	        {header: "序号", align: 'center', renderer: getIndex},
	        {
	            id: 'action',
	            header: "操作",
	            sortable: false,
	            dataIndex: 'RETURN_ID',
	            renderer: myLink,
	            align: 'center'
	        },
	        {header: "退货单号", dataIndex: 'RETURN_CODE', align: 'center'},
	        {header: "退货单位", dataIndex: 'DEALER_NAME', style: 'text-align: left;'},
	        {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
	        {header: "退货原因", dataIndex: 'REMARK', style: 'text-align: left;'},
	        {header: "提交人", dataIndex: 'T6NAME', align: 'center'},
	        {header: "提交日期", dataIndex: 'APPLY_DATE', align: 'center', renderer: formatDate},
	        {header: "审核人", dataIndex: 'T3NAME', align: 'center'},
	        {header: "审核日期", dataIndex: 'VERIFY_DATE', align: 'center', renderer: formatDate},
	        {header: "回运人", dataIndex: 'T4NAME', align: 'center'},
	        {header: "回运日期", dataIndex: 'WL_DATE', align: 'center', renderer: formatDate},
	        {header: "验收人", dataIndex: 'T5NAME', align: 'center'},
	        {header: "验收日期", dataIndex: 'IN_DATE', align: 'center', renderer: formatDate},
	        {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
	    ];
	
	} else if (value == "2") {//明细
	    columns = [
	        {header: "序号", align: 'center', renderer: getIndex},
	        {header: "退货单号", dataIndex: 'RETURN_CODE', align: 'center'},
	        {header: "退货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
	        {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
	        {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
	        // {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
	        {header: "单位", dataIndex: 'UNIT', align: 'center'},
	        {header: "出库库房", dataIndex: 'STOCK_OUT_NAME', align: 'center'},
	        {header: "入库库房", dataIndex: 'STOCK_IN_NAME', align: 'center'},
	        {header: "申请数量", dataIndex: 'APPLY_QTY', align: 'center'},
	        {header: "审核数量", dataIndex: 'CHECK_QTY', align: 'center'},
	        {header: "回运数量", dataIndex: 'BACK_QTY', align: 'center'},
	        {header: "验收数量", dataIndex: 'RETURN_QTY', align: 'center'},
	        {header: "退货价", dataIndex: 'BUY_PRICE', align: 'center'},
	        {header: "退货金额", dataIndex: 'BUY_AMOUNT', align: 'center'},
	        {header: "验收日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
	        {header: "状态", dataIndex: 'STATE2', align: 'center', renderer: getItemValue}
	    ];
	} else {
	    columns = [
	        {header: "序号", align: 'center', renderer: getIndex},
	        {header: "退货单号", dataIndex: 'RETURN_CODE', align: 'center'},
	        {header: "退货单位编码", dataIndex: 'DEALER_CODE', style: 'text-align:left'},
	        {header: "退货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
	        {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
	        {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
	        {header: "单位", dataIndex: 'UNIT', align: 'center'},
	        {header: "验收数量", dataIndex: 'RETURN_QTY', align: 'center'},
	        {header: "验收日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
	        {header: "验收人", dataIndex: 'NAME', align: 'center'}
	    ];
	}
	__extQuery__(1);
}
//查看申请详细页面
function view(value, state) {
    OpenHtmlWindow('<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/queryReturnApplyDetailInit.do?returnId=' + value + '&state=' + state + "&flag=1", 800, 400);
}

//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}
//表格导出
function exportPartReturnExcel() {
    fm.action = "<%=contextPath%>/parts/storageManager/partReturnManager/PartReturnQuery/exportPartReturnExcel.do";
    fm.target = "_self";
    fm.submit();
}
//设置超链接
function myLink(value, meta, record) {
    var state = record.data.STATE;
    if (state == <%=Constant.PART_DLR_RETURN_STATUS_06%>) {
        return String.format("<a href=\"#\" onclick='view(\"" + value + "\"," + state + ")'>[查看]</a>"
                + "<a href=\"#\" onclick='PrintView(\"" + value + "\"," + state + ")'>[打印]</a>");
    } else {
        return String.format("<a href=\"#\" onclick='view(\"" + value + "\"," + state + ")'>[查看]</a>");
    }
}
function showPart(obj) {
// 	$('#fm')[0].reset();
    if (obj.value == '1') {
        document.getElementById("part").style.display = "none";
        document.getElementById("state").style.display = "";
        document.getElementById("state1").style.display = "";
        document.getElementById("order").style.display = "";
        document.getElementById("date").style.display = "";
    } else if (obj.value == "2") {
        document.getElementById("part").style.display = "";
        document.getElementById("state").style.display = "none";
        document.getElementById("state1").style.display = "none";
        document.getElementById("order").style.display = "";
        document.getElementById("date").style.display = "none";
    } else {
        document.getElementById("part").style.display = "";
        document.getElementById("order").style.display = "";
        document.getElementById("state").style.display = "none";
        document.getElementById("state1").style.display = "none";
        document.getElementById("date").style.display = "none";
    }
    query(obj.value);
}

function PrintView(value, state) {
    window.open('<%=contextPath%>/parts/storageManager/partReturnManager/PartReturnQuery/sellReturnPrint.do?value=' + value + '&state=' + state);
}
</script>

</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件仓库管理 >配件退货管理&gt;销售退货查询
		</div>
		<form name="fm" id="fm" method="post">
			<input type="hidden" name="flag" value="1" />
			<input type="hidden" name="chkLevel" value="all" />
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
						<tr id="part" style="display: none">
							<td class="right">配件编码：</td>
							<td>
								<input class="middle_txt" type="text" name="PART_OLDCODE" id="PART_OLDCODE" />
							</td>
							<td class="right">配件名称：</td>
							<td>
								<input class="middle_txt" type="text" name="PART_CNAME" id="PART_CNAME" />
							</td>
						</tr>
						<tr id="order">
							<td class="right">退货单号：</td>
							<td>
								<input class="middle_txt" type="text" name="RETURN_CODE" id="RETURN_CODE" />
							</td>
							<td class="right">退货单位：</td>
							<td>
								<input class="middle_txt" type="text" name="DEALER_NAME" id="DEALER_NAME" />
							</td>
							<td class="right" id="state1">状态：</td>
							<td>
								<select name="state" id="state" class="u-select">
									<option value="">--请选择--</option>
									<option value="92361002">已提交</option>
									<option value="92361004">已审核</option>
									<option value="92361005">已回运</option>
									<option value="92361006">已入库</option>
								</select>
							</td>
						</tr>
						<tr id="date">
							<td class="right">提交日期：</td>
							<td>
								<input name="startDate_t" type="text" style="width: 80px;" class="middle_txt" id="startDate_t" value="" style="width: 65px" />
								<input name="button2" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'startDate_t', false);" />
								至
								<input name="endDate_t" type="text" style="width: 80px;" class="middle_txt" id="endDate_t" value="" style="width: 65px" />
								<input name="button2" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'endDate_t', false);" />
							</td>
							</td>
							<td class="right">审核日期：</td>
							<td>
								<input name="startDate_s" type="text" style="width: 80px;" class="middle_txt" id="startDate_s" value="" style="width: 65px" />
								<input name="button2" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'startDate_s', false);" />
								至
								<input name="endDate_s" type="text" style="width: 80px;" class="middle_txt" id="endDate_s" value="" style="width: 65px" />
								<input name="button2" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'endDate_s', false);" />
							</td>
							</td>
							<td class="right">回运日期：</td>
							<td>
								<input name="startDate_h" type="text" style="width: 80px;" class="middle_txt" id="startDate_h" value="" style="width: 65px" />
								<input name="button2" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'startDate_h', false);" />
								至
								<input name="endDate_h" type="text" style="width: 80px;" class="middle_txt" id="endDate_h" value="" style="width: 65px" />
								<input name="button2" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'endDate_h', false);" />
							</td>
							</td>
						</tr>
						<tr>
							<td class="right">验收日期：</td>
							<td>
								<input name="startDate_y" type="text" style="width: 80px;" class="middle_txt" id="startDate_y" value="" style="width: 65px" />
								<input name="button2" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'startDate_y', false);" />
								至
								<input name="endDate_y" type="text" style="width: 80px;" class="middle_txt" id="endDate_y" value="" style="width: 65px" />
								<input name="button2" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'endDate_y', false);" />
							</td>
							</td>
						</tr>
						<tr>
							<td colspan="6" class="center">
								<input type="radio" name="RADIO_SELECT" value="1" checked onclick="showPart(this);" />
								退货单&nbsp;
								<input type="radio" name="RADIO_SELECT" value="2" onclick="showPart(this);" />
								退货明细&nbsp;
								<input type="radio" name="RADIO_SELECT" value="3" onclick="showPart(this);" />
								验收明细&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="6" class="center">
								<input name="BtnQuery" id="queryBtn" class="u-button" type="button" value="查 询" onclick="__extQuery__(1);" />
								<input name="expButton" id="expButton" class="u-button" type="button" value="导出" onclick="exportPartReturnExcel();" />
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

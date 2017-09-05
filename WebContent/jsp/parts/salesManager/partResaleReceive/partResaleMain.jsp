<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>配件零售单</title>
<style>.table_query input.short_txt:first-child{margin-left:0}</style>
<script language="javascript" type="text/javascript">
$(function(){
    __extQuery__(1);
});

function genSelBoxExpStr(id, type, selectedKey, setAll, _class_, _script_, nullFlag, expStr) {
    var str = "";
    var arr;
    if (expStr.indexOf(",") > 0)
        arr = expStr.split(",");
    else {
        expStr = expStr + ",";
        arr = expStr.split(",");
    }
    str += "<select id='" + id + "' name='" + id + "' class='" + _class_ + "' " + _script_;
    if (nullFlag && nullFlag == "true") {
        str += " datatype='0,0,0' ";
    }
    // end
    str += " onChange=doCusChange(this.value);> ";
    if (setAll) {
        str += genDefaultOpt();
    }
    var bzType = <%=Constant.PART_RESALE_RECEIVE_ORDER_TYPE%>;
	var oemId = <%=Constant.OEM_ACTIVITIES%>;
	var orgId = document.getElementById("parentOrgId").value;
	var bzType1 = <%=Constant.PART_RESALE_RECEIVE_ORDER_TYPE_01%>;
	var bzType2 = <%=Constant.PART_RESALE_RECEIVE_ORDER_TYPE_02%>;
	var bzType3 = <%=Constant.PART_RESALE_RECEIVE_ORDER_TYPE_03%>;
	var bzType6 = <%=Constant.PART_RESALE_RECEIVE_ORDER_TYPE_06%>;
    var arrType = [bzType1.toString(), bzType2.toString(), bzType3.toString(), bzType6.toString()];

    for (var i = 0; i < codeData.length; i++) {
        var flag = true;
        for (var j = 0; j < arr.length; j++) {
            if (codeData[i].codeId == arr[j]) {
                flag = false;
            }
        }

        if (type == bzType.toString()) {
            if (orgId == oemId.toString()) {
                if (codeData[i].type == type && flag) {
                    str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
                }
            }
            else {
                if (codeData[i].type == type && flag && arrType.indexOf(codeData[i].codeId.toString()) > -1) {
                    str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
                }
            }

        }
        else {
            if (codeData[i].type == type && flag) {
                str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
            }
        }
    }
    str += "</select>";
    document.write(str);
}


var myPage;

var url = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/partResRecSearch.json";

var title = null;

var columns = [
    {header: "序号", dataIndex: 'RETAIL_ID', renderer: getIndex, align: 'center'},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'ID', renderer: myLink, align: 'center'},
    {header: "零售单号", dataIndex: 'RETAIL_CODE', align: 'center'},
    {header: "类型", dataIndex: 'CHG_TYPE', align: 'center', renderer: getItemValue},
    {header: "制单单位", dataIndex: 'SORG_CNAME', align: 'center'},
    {header: "仓库", dataIndex: 'WH_CNAME', align: 'center'},
    {header: "制单人", dataIndex: 'NAME', align: 'center'},
    {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center'},
    {header: "制单金额(元)", dataIndex: 'AMOUNTS', style: 'text-align: right;'},
    {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
];

//设置超链接
function myLink(value, meta, record) {
    var changeId = record.data.RETAIL_ID;
    var state = record.data.STATE;
    var str = "";
    var saveState = <%=Constant.PART_RESALE_RECEIVE_ORDER_TYPE_01 %>;
    var cmmtState = <%=Constant.PART_RESALE_RECEIVE_ORDER_TYPE_02 %>;
    var rejState = <%=Constant.PART_RESALE_RECEIVE_ORDER_TYPE_05 %>;
    if (saveState == state || rejState == state) {
        str = "<a href=\"#\" onclick='viewDetail(\"" + changeId + "\")'>[查看]</a>&nbsp;<a href=\"#\" onclick='moifyPg(\"" + changeId + "\")'>[修改]</a>&nbsp;<a href=\"#\" onclick='ConformCommit(\"" + changeId + "\")'>[提交]</a>&nbsp;<a href=\"#\" onclick='openPtPage(\"" + changeId + "\")'>[打印]</a>&nbsp;<a href=\"#\" onclick='ConformDisable(\"" + changeId + "\")'>[作废]</a>"
    }
    else if (cmmtState == state) {
        str = "<a href=\"#\" onclick='viewDetail(\"" + changeId + "\")'>[查看]</a>&nbsp;<a href=\"#\" onclick='openPtPage(\"" + changeId + "\")'>[打印]</a>";
    }
    else {
        str = "<a href=\"#\" onclick='viewDetail(\"" + changeId + "\")'>[查看]</a>&nbsp;<a href=\"#\" onclick='openPtPage(\"" + changeId + "\")'>[打印]</a>";
    }
    return String.format(str);
}

function ConformCommit(retailId) {
    MyConfirm("确定提交订单?", commitOrder, [retailId]);
}

function ConformDisable(retailId) {
    MyConfirm("确定作废订单?", commitDisable, [retailId]);
}

function commitOrder(retailId) {
    btnDisable();
    var url = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/commitOrderInfos.json?retailId=" + retailId + "&curPage=" + myPage.page;
    sendAjax(url, getResult, 'fm');
}

function commitDisable(retailId) {
    btnDisable();
    var url = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/disableOrderInfos.json?retailId=" + retailId + "&curPage=" + myPage.page;
    sendAjax(url, getResult, 'fm');
}

function getResult(json) {
    btnEnable();
    if (null != json) {
        if (json.errorExist != null && json.errorExist.length > 0) {
            MyAlert(json.errorExist);
            __extQuery__(json.curPage);
        } else if (json.success != null && json.success == "true") {
            MyAlert("订单操作成功!", function(){
	            __extQuery__(json.curPage);
            });
        } else {
            MyAlert("订单操作失败，请联系管理员!");
        }
    }
}

//查看
function viewDetail(parms) {
    var optionType = "view";
    var actionURL = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/viewOrderDeatilInint.do";
    var orderType = document.getElementById("orderType").value;
    OpenHtmlWindow(actionURL+"?changeId=" + parms + "&optionType=" + optionType + "&actionURL=" + actionURL + "&orderType=" + orderType, 800, 500);
}

//修改
function moifyPg(parms) {
    btnDisable();
    var optionType = "modify";
    var actionURL = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/partResInit.do";
    document.getElementById("actionURL").value = actionURL;
    document.fm.action = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/viewOrderDeatilInint.do?changeId=" + parms + "&optionType=" + optionType;
    document.fm.target = "_self";
    document.fm.submit();
}

//打印页面
function openPtPage(parms) {
    var optionType = "print";
    var pageType = "res";
    var orderType = document.getElementById("orderType").value;
    var parentOrgId = document.getElementById("parentOrgId").value;
    window.open("<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/viewOrderDeatilInint.do?changeId=" + parms + "&optionType=" + optionType + "&pageType=" + pageType + "&orderType=" + orderType + "&parentOrgId=" + parentOrgId, "", "toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500");
    //document.fm.action="<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/viewOrderDeatilInint.do?changeId=" + parms +"&optionType=" + optionType + "&pageType=" + pageType;
    //document.fm.target="_blank";
    //document.fm.submit();
}

//新增领用
function addRec() {
    var parentOrgId = document.getElementById("parentOrgId").value;
    var companyName = document.getElementById("companyName").value;
    var addType = "rec";
    var actionURL = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/partOrderAddInit.do?addType=" + addType;
    document.getElementById("actionURL").value = actionURL;
    btnDisable();
    document.fm.action = actionURL;
    document.fm.target = "_self";
    document.fm.submit();
}

//新增零售
function addRes() {
    var parentOrgId = document.getElementById("parentOrgId").value;
    var companyName = document.getElementById("companyName").value;

    var addType = "res";
    var actionURL = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/partOrderAddInit.do?addType=" + addType;
    document.getElementById("actionURL").value = actionURL;
    btnDisable();
    document.fm.action = actionURL;
    document.fm.target = "_self";
    document.fm.submit();
}

//下载
function exportPartStockExcel() {
    document.fm.action = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/exportSaleOrdersExcel.do";
    document.fm.target = "_self";
    document.fm.submit();
}

</script>
</head>
<body>
	<div class="wbox">
		<form method="post" name="fm" id="fm">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置： 配件管理 &gt; 零售领用管理 &gt; 配件零售单
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
				<input type="hidden" name="companyName" id="companyName" value="${companyName }" />
				<input type="hidden" name="actionURL" id="actionURL" value="" />
				<input type="hidden" name="orderType" id="orderType" value="<%=Constant.PART_SALE_STOCK_REMOVAL_TYPE_01%>" />
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">零售单号：</td>
							<td>
								<input class="middle_txt" type="text" name="changeCode" id="changeCode" />
							</td>
							<td class="right">状态：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("orderState", <%=Constant.PART_RESALE_RECEIVE_ORDER_TYPE%>, <%=Constant.PART_RESALE_RECEIVE_ORDER_TYPE_01%>, true, "", "onchange='__extQuery__(1)'", "false", "");
								</script>
							</td>
						</tr>
						<tr>
							<td class="right">制单日期：</td>
							<td>
								<input id="checkSDate" style="width: 80px;" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" value=" " type="button" />
								至
								<input id="checkEDate" style="width: 80px;" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" value=" " type="button" />
							</td>
							<td class="right">仓库：</td>
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
						</tr>
						<tr>
							<td class="center" colspan="4">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
								<input class="u-button" type="reset" value="重 置" />
								<input class="u-button" type="button" value="新增零售单" onclick="addRes()" />
								<input class="u-button" type="button" value="导 出" onclick="exportPartStockExcel()" />
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
	</div>
	<script type="text/javascript">
		
	</script>
</body>
</html>
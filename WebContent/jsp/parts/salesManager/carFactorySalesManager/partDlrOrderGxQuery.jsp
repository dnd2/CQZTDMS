<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>广宣订单审核</title>
<script language="javascript">

//初始化查询TABLE
var myPage;

var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/partDlrOrderGxQuery.json";

var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'ORDER_ID', renderer: myLink, align: 'center'},
    {header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'center', renderer: getItemValue},
    {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
    {header: "订货单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
    {header: "订货单位名称", dataIndex: 'DEALER_NAME', style:'text-align:left;'},
    {header: "备注", dataIndex: 'REMARK', style:'text-align:left;'},
    {header: "订货人", dataIndex: 'BUYER_NAME', style:'text-align:left;'},
    // {header: "订货日期", dataIndex: 'CREATE_DATE', align: 'center'},
    /*{header: "销售单位", dataIndex: 'SELLER_NAME', align:'center'},*/
    {header: "订货总金额(元)", dataIndex: 'ORDER_AMOUNT',  style: 'text-align:right'},
    {header: "提交时间", dataIndex: 'SUBMIT_DATE', align: 'center'},
    {header: "订单状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
    /*{header: "驳回原因", dataIndex: 'REBUT_REASON', align: 'center'}*/
    /* {header: "已预审", dataIndex: 'IS_AUTCHK', align: 'center', renderer: getItemValue}*/

];
function myLink(value, meta, record) {
    if ((record.data.STATE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02%> || record.data.STATE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06%>) && record.data.OEM_FLAG!="<%=Constant.IF_TYPE_YES%>") {
        return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[查看]</a>" /*+ "<a href=\"#\" onclick='modify(\"" + value + "\")'>[修改]</a>"*/ + "<a href=\"#\" onclick='rebutConfirm(\"" + value + "\")'>[驳回]</a>" + "<a href=\"#\" onclick='checkOrder(\"" + value + "\",\"" + record.data.ORDER_TYPE + "\")'>[审核]</a>"/*+"<a href=\"#\" onclick='confirmClose(\"" + value  + "\")'>[强制关闭]</a>"*/);
    }
    if ((record.data.STATE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02%> || record.data.STATE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06%>) && record.data.OEM_FLAG=="<%=Constant.IF_TYPE_YES%>") {
        return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[查看]</a>" /*+ "<a href=\"#\" onclick='modify(\"" + value + "\")'>[修改]</a>"*/ + "<a href=\"#\" onclick='confirmCancelOrder(\"" + value + "\")'>[作废]</a>" + "<a href=\"#\" onclick='checkOrder(\"" + value + "\",\"" + record.data.ORDER_TYPE + "\")'>[审核]</a>"/*+"<a href=\"#\" onclick='confirmClose(\"" + value  + "\")'>[强制关闭]</a>"*/);
    }
    return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[查看]</a>");
}
//查看
function detailOrder(value, code) {
    var buttonFalg="disabled";
    OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/detailDlrOrder.do?orderId=" + value + "&&orderCode=" + code+"&buttonFalg="+buttonFalg,900,400);
}
function confirmClose(id){
	if(confirm("确定关闭?")){
		closeOrder(id)
	}
}
function closeOrder(id){
	var closeUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/closeOrderAction.json?orderId=" + id;
    sendAjax(closeUrl, closeResult, 'fm');
}
function closeResult(jsonObj){
	if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            __extQuery__(1);
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}
//审核
function checkOrder(value, type) {
    disableAllClEl();
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/checkDlrGxOrder.do?orderId=" + value + "&planFlag=" + $('planFlag').value;
}
//导出
function exportEx() {
    fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/exportPartGxExcel.do";
    fm.submit();
}
function rebutReason(id){
	OpenHtmlWindow(g_webAppName + '/jsp/parts/salesManager/carFactorySalesManager/rebutGxOrderReason.jsp?id=' + id , 300, 200);
}
//查询
function doQuery() {
    var msg = "";
    //校验时间范围
    if (document.getElementById("CstartDate").value != "") {
        if (document.getElementById("CendDate").value == "") {
            msg += "请填写制单结束日期!</br>"
        }
    }
    if (document.getElementById("CendDate").value != "") {
        if (document.getElementById("CstartDate").value == "") {
            msg += "请填写制单开始日期!</br>"
        }
    }
    if (document.getElementById("SstartDate").value != "") {
        if (document.getElementById("SendDate").value == "") {
            msg += "请填写提交结束日期!</br>"
        }
    }
    if (document.getElementById("SendDate").value != "") {
        if (document.getElementById("SstartDate").value == "") {
            msg += "请填写提交开始日期!</br>"
        }
    }
    if (msg != "") {
        //弹出提示
        MyAlert(msg);
        return;
    }
    //执行查询
    __extQuery__(1);
}
function disableAllA() {
    var inputArr = document.getElementsByTagName("a");
    for (var i = 0; i < inputArr.length; i++) {
        inputArr[i].disabled = true;
    }
}

function enableAllA() {

    var inputArr = document.getElementsByTagName("a");
    for (var i = 0; i < inputArr.length; i++) {
        inputArr[i].disabled = false;
    }
}
function disableAllBtn() {
    var inputArr = document.getElementsByTagName("input");
    for (var i = 0; i < inputArr.length; i++) {
        if (inputArr[i].type == "button") {
            inputArr[i].disabled = true;
        }
    }
}
function enableAllBtn() {
    var inputArr = document.getElementsByTagName("input");
    for (var i = 0; i < inputArr.length; i++) {
        if (inputArr[i].type == "button") {
            inputArr[i].disabled = false;
        }
    }
}
function disableAllClEl() {
    disableAllA();
    disableAllBtn();
}
function enableAllClEl() {
    enableAllBtn();
    enableAllA();
}
function genMySelBoxExp(id, type, selectedKey, setAll, _class_, _script_, nullFlag, expStr) {
    var str = "";
    var arr;
    if (expStr.indexOf(",") > 0)
        arr = expStr.split(",");
    else {
        expStr = expStr + ",";
        arr = expStr.split(",");
    }
    str += "<select id='" + id + "' name='" + id + "' class='" + _class_ + "' " + _script_;
    // modified by lishuai@infoservice.com.cn 2010-05-18 解决select下拉框中增加属性datatype判断bug begin
    if (nullFlag && nullFlag == "true") {
        str += " datatype='0,0,0' ";
    }
    // end
    str += " onChange=doCusChange(this.value);> ";
    if (setAll) {
        str += genDefaultOpt();
    }
    for (var i = 0; i < codeData.length; i++) {
        var flag = true;
        for (var j = 0; j < arr.length; j++) {
            if (codeData[i].codeId == arr[j]) {
                flag = false;
            }
        }
        if (${planFlag=="noPlan"}) {
            if (codeData[i].codeId ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_06%>) {
                continue;
            }
        }
        if (codeData[i].type == type && flag) {
            str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
        }
    }
    str += "</select>";
    document.write(str);
}
function doInit() {
    loadcalendar();
    //initCondition();
    __extQuery__(1);
    enableAllClEl();
}
function rebutConfirm(id) {
	if(confirm("确定驳回?")){
		rebutReason(id);
	}
}
function rebut(id,reason) {
    var rebutUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/rebut.json?orderId=" + id+"&reason="+reason;
    sendAjax(rebutUrl, rebutResult, 'fm');
}
function rebutResult(jsonObj) {

    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            __extQuery__(1);
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}
function modify(id) {
    disableAllClEl();
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/modifyDlrOrder.do?orderId=" + id;
}
//新增
function addOrder() {
        disableAllClEl();
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/addMarketOrder.do?";

    }
autoAlertException();
function initCondition(){
		if($('condition_orderCode')){
			$('ORDER_CODE').value=$('condition_orderCode').value;
		}
		if($('condition_dealerName')){
			$('DEALER_NAME').value=$('condition_dealerName').value;
		}
		if($('condition_sellerName')){
			//$('SELLER_NAME').value=$('condition_sellerName').value;
		}
		if($('condition_state')){
			$('state').value=$('condition_state').value==""?<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02%>:$('condition_state').value;
		}
		if($('condition_orderType')){
			$('ORDER_TYPE').value=$('condition_orderType').value;
		}
		if($('condition_salerId')){
			$('salerId').value=$('condition_salerId').value==""?${curUserId}:$('condition_salerId').value;
		}
		if($('condition_CstartDate')){
			//$('CstartDate').value=$('condition_CstartDate').value;
		}
		if($('condition_CendDate')){
			//$('CendDate').value=$('condition_CendDate').value;
		}
		if($('condition_SstartDate')){
			//$('SstartDate').value=$('condition_SstartDate').value;
		}
		if($('condition_SendDate')){
			//$('SendDate').value=$('condition_SendDate').value;
		}
}
function callBack(json){
    var ps;
    //设置对应数据
    if(Object.keys(json).length>0){
        keys = Object.keys(json);
        for(var i=0;i<keys.length;i++){
            if(keys[i] =="ps"){
                ps = json[keys[i]];
                break;
            }
        }
        //	ps = json[Object.keys(json)[0]];
    }

    //生成数据集
    if(ps.records != null){
        $("_page").hide();
        $('myGrid').show();
        new createGrid(title,columns, $("myGrid"),ps).load();
        //分页
        myPage = new showPages("myPage",ps,url);
        myPage.printHtml();
        hiddenDocObject(2);
    }else{
        $("_page").show();
        $("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据</div>";
        $("myPage").innerHTML = "";
        removeGird('myGrid');
        $('myGrid').hide();
        hiddenDocObject(1);
    }
    if(null != json){
        $("sumAmount").value= json.accountSum;
        $("xs").value= json.xs;
    }
}
//作废
function confirmCancelOrder(value) {
    MyConfirm("确定作废订单?", cancelOrder, [value])
}
function cancelOrder(value) {
    disableAllClEl();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/cancelOrder.json?orderId=" + value+"&flag=1";
    sendAjax(url, getResult, 'fm');
}
function getResult(jsonObj) {
    enableAllClEl();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            __extQuery__(1);
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}
</script>
</head>
<body onload="doInit();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input id="planFlag" name="planFlag" value="${planFlag}" type="hidden"/>
    <input id="condition_orderCode" name="condition_orderCode" value="${condition.orderCode}" type="hidden"/>
    <input id="condition_dealerName" name="condition_dealerName" value="${condition.dealerName}" type="hidden"/>
   	<input id="condition_sellerName" name="condition_sellerName" value="${condition.sellerName}" type="hidden"/>
   	<input id="condition_state" name="condition_state" value="${condition.state}" type="hidden"/>
   	<input id="condition_orderType" name="condition_orderType" value="${condition.orderType}" type="hidden"/>
   	<input id="condition_salerId" name="condition_salerId" value="${condition.salerId}" type="hidden"/>
   	<input id="condition_CstartDate" name="condition_CstartDate" value="${condition.CstartDate}" type="hidden"/>
   	<input id="condition_CendDate" name="condition_CendDate" value="${condition.CendDate}" type="hidden"/>
   	<input id="condition_SstartDate" name="condition_SstartDate" value="${condition.SstartDate}" type="hidden"/>
   	<input id="condition_SendDate" name="condition_SendDate" value="${condition.SendDate}" type="hidden"/>
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置： 配件管理 &gt; 配件销售管理  &gt; 广宣订单审核 </div>
        <table border="0" class="table_query">
            <th colspan="6" width="100%"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>查询条件</th>
            <tr >
                <td   align="right">订单号：</td>
                <td width="24%"><input class="middle_txt" type="text" id="ORDER_CODE" name="ORDER_CODE"/></td>
                <td   align="right">订货单位编码：</td>
                <td width="24%"><input class="middle_txt" type="text" id="DEALER_CODE" name="DEALER_CODE"/></td>
                <td   align="right">订货单位：</td>
                <td width="24%"><input class="middle_txt" type="text" id="DEALER_NAME" name="DEALER_NAME"/></td>
              <%--  <td   align="right">销售单位：</td>
                <td width="24%"><input class="middle_txt" type="text" id="SELLER_NAME" name="SELLER_NAME"/></td>--%>
            </tr>
            <tr>
                <td   align="right">订单状态：</td>
                <td width="24%">
                    <script type="text/javascript">
                        genSelBox("state", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE%>, "<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02%>", true, "short_sel", "", "false", '');
                    </script>
                </td>
                

                <td align="right">销售人员：</td>
		        <td>
			       	<select  name="salerId" id = "salerId" class="short_sel" >
			       		<option  value="">--请选择--</option>
                        <c:if test="${salerFlag}" >
					   	<c:forEach items="${salerList}" var="saler">
						  <c:choose> 
							<c:when test="${curUserId eq saler.USER_ID}">
							  <option selected="selected" value="${saler.USER_ID}">${saler.NAME}</option>
							</c:when>
							<c:otherwise>
							  <option  value="${saler.USER_ID}">${saler.NAME}</option>
							</c:otherwise>
						  </c:choose>
						</c:forEach>
                        </c:if>
			      	</select>
		        </td>

               <td   align="right">制单日期：</td>
                <td width="24%">
                <input name="CstartDate" type="text" class="short_time_txt" id="CstartDate" value="${old}"
                       style="width:65px"/>
                <input name="button2" value=" " type="button" class="time_ico"
                       onclick="showcalendar(event, 'CstartDate', false);"/>
                至
                <input name="CendDate" type="text" class="short_time_txt" id="CendDate" value="${now}"
                       style="width:65px"/>
                <input name="button2" value=" " type="button" class="time_ico"
                       onclick="showcalendar(event, 'CendDate', false);"/>
                       </td>
            </tr>
            <tr>
                <td   align="right">提交时间：</td>
                <td width="24%"><input name="SstartDate" type="text" class="short_time_txt" id="SstartDate"
                                       style="width:65px" value=""/>
                    <input name="button2" value=" " type="button" class="time_ico"
                           onclick="showcalendar(event, 'SstartDate', false);"/>
                    至
                    <input name="SendDate" type="text" class="short_time_txt" id="SendDate" style="width:65px"
                           value=""/>
                    <input name="button2" value=" " type="button" class="time_ico"
                           onclick="showcalendar(event, 'SendDate', false);"/></td>
            </tr>
            <tr>
                <td colspan="7" align="center"><input name="BtnQuery" id="queryBtn" class="normal_btn" type="button"
                                                      value="查 询" onclick="doQuery();"/>
                                                      &nbsp;<!--
                    &nbsp;
                    <input class="normal_btn" type="button" value="新 增" onclick="addOrder()"/>
                    &nbsp;
                    --><input class="normal_btn" type="button" value="导 出" onclick="exportEx()"/>
                    &nbsp; &nbsp; &nbsp; &nbsp;
                </td>

            </tr>
        </table>
        <div id="layer">
            <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
            <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
        </div>
    </div>
</form>
</body>
</html>
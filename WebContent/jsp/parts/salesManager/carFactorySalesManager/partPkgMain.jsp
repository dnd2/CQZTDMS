<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<head>
    <title>拣货单管理</title>
</head>
<script language="javascript">

//初始化查询TABLE
var myPage;
var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/partPkgQuery.json";
var title = null;
var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {header: "<input name='cbAll' id='cbAll' onclick='ckAll(this)' type='checkbox' />", dataIndex: 'SO_ID', align: 'center', renderer: checkLink},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'SO_ID', renderer: myLink, align: 'center'},
//     {header: "整车计划", dataIndex: 'VSO_FLAG', align: 'center',renderer:showZCPlan},
    {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
    {header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'center', renderer: getItemValue},
    {header: "销售单号", dataIndex: 'SO_CODE', align: 'center', renderer: getSoCode},
//    {header: "拣货单号", dataIndex: 'PICK_ORDER_ID', align: 'center'},
    {header: "订货单位编码", dataIndex: 'DEALER_CODE', style: 'text-align:left'},
    {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
    {header: "备注", dataIndex: 'REMARK2', style: 'text-align:left'},
    /* {header: "发运方式", dataIndex: 'TRANS_TYPE_NAME', align: 'center'},*/
    /*{header: "销售单位", dataIndex: 'SELLER_NAME',  style: 'text-align:left'},*/
    {header: "销售金额", dataIndex: 'CONVERSEAMOUNT', align: 'center', style: 'text-align:right'},
    {header: "制单人", dataIndex: 'CREATE_BY_NAME', align: 'center'},
    {header: "销售日期", dataIndex: 'CREATE_DATE', align: 'center'},
    {header: "财务审核日期", dataIndex: 'FCAUDIT_DATE', align: 'center'},
    {header: "出库仓库", dataIndex: 'WH_NAME', align: 'center'}/* ,
    {header: "状态", dataIndex: 'STATE', align: 'center'} */
    //{header: "装箱号", dataIndex: 'pkgedNo', align:'center'},
    /*{header: "状态", dataIndex: 'STATE', align: 'center', renderer: getMyItemValue}*/

];
function myLink(value, meta, record) {
    var view = "<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.SO_CODE + "\")'>[查看]</a>";
    var reject = "<a href=\"#\" onclick='rejectOrder(\"" + value + "\",\"" + record.data.SO_CODE + "\")'>[驳回到销售]</a>";
    /* if (record.data.PICK_ORDER_ID == null) {
        return String.format(view + reject);
    } else {
        return String.format(view);
    } */
    return String.format(view);

}
function checkLink(value, meta, record) {
    if (record.data.PICK_ORDER_ID == null && record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_05%>) {
        return String.format("<input id='cb' name='cb' type='checkbox' onclick='ck(this)' value='" + value + "' />");
    }
    if (record.data.SELLER_ID !=<%=Constant.OEM_ACTIVITIES%> && record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_02%>) {    //非主机厂(供应中心）提交状态
        return String.format("<input id='cb' name='cb' type='checkbox' onclick='ck(this)' value='" + value + "' />");
    }
    return String.format('<img src="<%=contextPath%>/img/close.gif" />');
}
function getMyItemValue(value, meta, record) {
    var str = getItemValue(value);
    if (record.data.PICK_ORDER_ID == null && record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_05%>) {
        return str;
    }
    if (record.data.SELLER_ID !=<%=Constant.OEM_ACTIVITIES%> && record.data.STATE ==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_02%>) {    //不是整车场  就是已经提交的
        return str;
    }
    if (record.data.PICK_ORDER_ID != "") {
        return str + "(已合并)";
    }
    return str + "(未合并)";
}
function getSoCode(value, meta, record) {
    var soId = record.data.SO_ID;
    return String.format("<input type='hidden' name='soCode_" + soId + "' value='" + value + "'/>" + value);
}
function ckAll(obj) {
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].disabled) {
            continue;
        }
        cb[i].checked = obj.checked;
    }
}
function ck(obj) {
    var cb = document.getElementsByName("cb");
    var flag = true;
    for (var i = 0; i < cb.length; i++) {
        if (!cb[i].checked) {
            flag = false;
        }
    }
    $('#cbAll')[0].checked = flag;
}
//查看
function detailOrder(value, code) {
    //disableAllClEl();
    //window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/detailOrder.do?soId=" + value;
    var buttonFalg = "disabled";
    OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/detailOrder.do?soId=" + value + "&&soCode=" + code + "&buttonFalg=" + buttonFalg, 800, 400);
}

//装箱
function pkgPart(soId) {
    disableAllClEl();
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/pkgOrder.do?soId=" + soId;
}
//使所有同类型的标签DISABLED掉
function eleControl(flag) {
    var inputTags = document.getElementsByTagName('input');
    var selTags = document.getElementsByTagName('select');
    var aTags = document.getElementsByTagName('a');
    for (var i = 0; i < inputTags.length; i++) {
        inputTags[i].disabled = flag;
    }
    for (var i = 0; i < selTags.length; i++) {
        selTags[i].disabled = flag;
    }
    for (var i = 0; i < aTags.length; i++) {
        aTags[i].disabled = flag;
    }
}

function getCbArr() {
    var cbArr = [];
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            cbArr.push(cb[i].value);
        }
    }
    return cbArr;
}
function doQuery() {
    var msg = "";
    if ("" != document.getElementById("SendDate").value) {
        if ("" == document.getElementById("SstartDate").value) {
            msg += "请选择销售开始时间!</br>";
        }
    }
    if ("" != document.getElementById("SstartDate").value) {
        if ("" == document.getElementById("SendDate").value) {
            msg += "请选择销售结束时间!</br>";
        }
    }
    if (msg != "") {
        MyAlert(msg);
        return;
    }
    __extQuery__(1);
}


function exportExcel() {
    fm.action = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/exportExcel.do';
    fm.submit();
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
    disabledAllCb();
}
function enableAllClEl() {
    enableAllBtn();
    enableAllA();
    enabledAllCb();
}
function disabledAllCb() {
    var inputArr = document.getElementsByTagName("input");
    for (var i = 0; i < inputArr.length; i++) {
        if (inputArr[i].type == "checkbox") {
            inputArr[i].disabled = true;
        }
    }
}
function enabledAllCb() {
    var inputArr = document.getElementsByTagName("input");
    for (var i = 0; i < inputArr.length; i++) {
        if (inputArr[i].type == "checkbox") {
            inputArr[i].disabled = false;
        }
    }
}
function unitPickOrderConfirm(value) {
    var flag = false;
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            flag = true;
        }
    }
    if (!flag) {
        MyAlert("请选择一条记录!");
        return;
    }
    if (value == '1') {
        MyConfirm("确认合并?", unitPickOrder, [value]);
    } else {
        MyConfirm("确认生成?", unitPickOrder, [value]);
    }

}
function unitPickOrder(value) {
    btnDisable();
    var ar = getCbArr();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/unitPickOrder.json?cbAr=" + ar + "&unitType=" + value;
    makeNomalFormCall(url, getResult, 'fm');
}

function getResult(jsonObj) {
    btnEnable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            __extQuery__(1);
            MyAlert(success);
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
        	MyAlert(exceptions.message);
        }
    }
    printprintPrintInfo();
}
function getDate() {
    var dateS = "";
    var dateE = "";
    var myDate = new Date();
    var year = myDate.getFullYear();   //获取完整的年份(4位,1970-????)
    var moth = myDate.getMonth();      //获取当前月份(0-11,0代表1月)
    if (moth < 10) {
        if (0 < moth) {
            moth = "0" + moth;
        }else {
            year = myDate.getFullYear() - 1;
            moth = moth + 12;
            if (moth < 10) {
                moth = "0" + moth;
            }
        }
    }
    var day = myDate.getDate();       //获取当前日(1-31)
    if (day < 10) {
        day = "0" + day;
    }
    dateS = year + "-" + moth + "-" + day;
    moth = myDate.getMonth() + 1;	//获取当前月份(0-11,0代表1月)
    if (moth < 10) {
        moth = "0" + moth;
    }
    dateE = myDate.getFullYear() + "-" + moth + "-" + day;

    //document.getElementById('SstartDate').value = dateS;
    //document.getElementById('SendDate').value = dateE;
}


// function printPickOrder(id) {
<%--     OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/opPrintHtml.do?pickOrderId=" + id, 1100, 400); --%>
// }

function printPickOrder() {
	window.open("<%=request.getContextPath()%>/parts/salesManager/carFactorySalesManager/PartPickOrder/PartPickOrderInit.do?flag=pickPrint");
}

function rejectOrder(id, code) {
	MyConfirm("确定驳回?",confirmResult,[id]);
}

function confirmResult(id){
	var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPickOrder/rejectOrder.json?SO_ID=" + id;
   makeNomalFormCall(url, getResult1, 'fm');
}
function getResult1(jsonObj) {
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
function showZCPlan(value, meta, record) {
    var view = "<a href=\"#\" onclick='showDetail(\"" + record.data.DEALER_ID + "\")'>[有]</a>";
    if (record.data.VSO_FLAG == "有") {
        return String.format(view);
    } else {
        return String.format("无");
    }
}
function showDetail(dealerId) {
    OpenHtmlWindow('<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/partQueryZCPlanInit.do?dealerId=' + dealerId, 950, 500);
}
$(document).ready(function(){
	getDate(); 
	enableAllClEl();
	__extQuery__(1);

});
</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理 &gt; 配件销售管理 &gt;拣货单管理
        </div>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table border="0" class="table_query">
	            <tr>
	                <td class="right"><span class="right">订单号</span>：</td>
	                <td width="24%"><input type="text" id="orderCode" name="orderCode" class="middle_txt">
	                <td class="right"><span class="right">订货单位编码</span>：</td>
	                <td width="24%"><input type="text" id="dealerCode" name="dealerCode" class="middle_txt">
	                <td class="right"><span class="right">订货单位</span>：</td>
	                <td width="24%"><input type="text" id="dealerName" name="dealerName" class="middle_txt">
	                    <%--<td class="right"><span class="right">发运方式</span>：</td>
	                    <td width="24%">
	                        <select name="TRANS_TYPE" id="TRANS_TYPE"  class="u-select">
	                            <option value="">-请选择-</option>
	                            <c:if test="${transList!=null}">
	                                <c:forEach items="${transList}" var="list">
	                                    <option value="${list.fixValue }">${list.fixName }</option>
	                                </c:forEach>
	                            </c:if>
	                        </select>
	                    </td>--%>
	
	            </tr>
	            <tr>
	                <td class="right"><span class="right">销售单号：</span></td>
	                <td width="24%"><input class="middle_txt" type="text" id="soCode" name="soCode"/></td>
	                <td class="right">销售日期：</td>
	                <td width="26%"><input name="SstartDate" type="text" class="short_txt" id="SstartDate"
	                                       value="${old}" style="width: 80px"/>
	                    <input name="button" value=" " type="button" class="time_ico"
	                           onclick="showcalendar(event, 'SstartDate', false);"/>
	                    至
	                    <input name="SendDate" type="text" class="short_txt" id="SendDate" value="${now}"
	                           style="width: 80px"/>
	                    <input name="button" value=" " type="button" class="time_ico"
	                           onclick="showcalendar(event, 'SendDate', false);"/></td>
	                <td class="right">出库仓库:</td>
	                <td width="20%">
	                    <select name="whId" id="whId"  class="u-select">
	                        <option selected value=''>-请选择-</option>
	                        <c:forEach items="${wareHouseList}" var="wareHouse">
	                            <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
	                        </c:forEach>
	                    </select>
	                </td>
	            </tr>
	            <tr>
	                <td class="right"><span>订单类型：</span></td>
	                <td>
	                    <script type="text/javascript">
	                        genSelBox("orderType", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>, "", true, "", "", "false", '');
	                    </script>
	                </td>
	                <td class="right">是否拣货：</td>
	                <td>
	                    <script type="text/javascript">
	                        genSelBox("IF_PICK", <%=Constant.IF_TYPE%>, "<%=Constant.IF_TYPE_NO%>", true, "", "", "false", '');
	                     </script> 
	                </td>
	                <td></td>
	                <td></td>
	<!--                 <td class="right">是否有整车发运单：</td> -->
	<!--                 <td> -->
	<!--                     <script type="text/javascript"> -->
	<%--                         genSelBox("IS_HAVA", <%=Constant.IF_TYPE%>, "", true, "", "", "false", ''); --%>
	<!--                     </script> -->
	<!--                 </td> -->
	                <%-- <td class="right">状态：</td>
	                 <td width="24%">
	                     <select name="state" id="state"  class="u-select" onchange="__extQuery__(1)">
	                         <option selected value=''>-请选择-</option>
	                         <c:forEach items="${stateMap}" var="stateMap">
	                             <option value="${stateMap.key}">${stateMap.value}</option>
	                         </c:forEach>
	                     </select>
	                 </td>--%>
	            </tr>
	            <%--  <tr>
	                  <td class="right"><span>是否进出口公司：</span></td>
	                  <td>
	                      <script type="text/javascript">
	                          genSelBox("IS_SPS", <%=Constant.PART_BASE_FLAG%>, "<%=Constant.PART_BASE_FLAG_NO%>", true, "", "", "false", '');
	                      </script>
	                  </td>
	              </tr>--%>
	            <tr>
	                <td colspan="6" class="center">
	                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
	                           onclick="doQuery();"/>
	                    <%-- &nbsp;<input class="normal_btn" style="width: 100px" type="button" value="合并拣货单" name="unitPick"
	                                  id=unitPick" onclick="unitPickOrderConfirm(1);"/>--%>
	                    &nbsp;<input class="normal_btn" style="width: 100px" type="button" value="生成拣货单" name="unitPick"
	                                 id=unitPick"  onclick="unitPickOrderConfirm(2);"/>
	                    &nbsp;<input class="normal_btn"  style="width: 100px"  type="button" value="打印拣货单" name="printPrintInfo"
	                                 id="printPrintInfo" onclick="printPickOrder();"/>
	                    <%-- &nbsp;<input class="normal_btn" type="button" value="不并拣货单" onclick="pickOrder();"/>--%>
	                </td>
	            </tr>
	        </table>
			</div>
        </div>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</form>
</body>
</html>
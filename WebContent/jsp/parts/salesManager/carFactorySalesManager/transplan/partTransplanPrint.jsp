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
    <title>配件发运计划打印</title>
    <style>.table_query .short_txt{margin: 0}</style>
</head>
<script language="javascript">
    var myObjArr = [];
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTransPlan/partOutstockQuery.json";
    var title = null;
    var columns = null;
    
	function query(type){
		document.getElementById("queryType").value = type;
		if(type=="normal"){
			columns =[
		        {header: "序号", align: 'center', renderer: getIndex},
		        {id: 'action', header: "操作", sortable: false, dataIndex: 'PICK_ORDER_ID', align: 'center', renderer: myLink},
		        {header: "拣货单号", dataIndex: 'PICK_ORDER_ID', align: 'center'},
		        {header: "订货单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
		        {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
		        {header: "备注", dataIndex: 'REMARK2', style: 'text-align:left'},
//		        {header: "发运方式", dataIndex: 'TRANS_TYPE_NAME', align: 'center'},
		        {header: "总金额", dataIndex: 'TOTALMONEY', align: 'center', style: 'text-align:right'},
		        {header: "装箱完成日期", dataIndex: 'PKG_OVER_DATE', align: 'center'},
//		        {header: "合并人", dataIndex: 'CREATEBYNAME', align: 'center'},
		        {header: "仓库", dataIndex: 'WH_NAME', align: 'center'},
		        {header: "VIN", dataIndex: 'VIN', align: 'center'}
//		        {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}

		    ];
		}else if(type=="cancel"){
			columns =[
		        {header: "序号", align: 'center', renderer: getIndex},
                {header: "<input name='cbAll' id='cbAll' onclick='ckAll(this)' type='checkbox' />", align: 'center', dataIndex: 'TRPLAN_ID', renderer: checkLink},
		        {id: 'action', header: "操作", sortable: false, dataIndex: 'PICK_ORDER_ID', align: 'center', renderer: myCancel},
		        {header: "发运计划号", dataIndex: 'TRPLAN_CODE', align: 'center'},
		        {header: "拣货单号", dataIndex: 'PICK_ORDERIDS', align: 'center'},
		        {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
		        {header: "订货单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
		        {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
//		        {header: "销售单位", dataIndex: 'SELLER_NAME', align: 'center'},
		        {header: "箱号", dataIndex: 'PKG_NO', align: 'center'},
		        {header: "发运方式", dataIndex: 'TRANS_TYPE', align: 'center'},
		        {header: "承运物流", dataIndex: 'TRANSPORT_ORG', align: 'center'},
		        {header: "发运日期", dataIndex: 'CREATE_DATE', align: 'center'},
		        {header: "打印日期", dataIndex: 'PRINT_DATE', align: 'center'},
		        {header: "备注", dataIndex: 'REMARK', align: 'center'},
		        {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
		    ];
		}
		__extQuery__(1);
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
    function checkLink(value, meta, record) {
        var id = value + ":" + record.data.PICK_ORDER_ID;
        return String.format("<input  name='cb' type='checkbox'  value='" + id + "' />");
    }
    //add zhumingwei 2013-09-17
    function myLink(value, meta, record) {
             var text = "<a href=\"#\" onclick='selectPkgNo(\"" + value + "\")'>[生成发运计划]</a>";
            return String.format(text);

    }
    function myCancel(value, meta, record) {
        var pkgNos = record.data.PKG_NO;
        var pickOrderId = record.data.PICK_ORDER_ID;
        var trplanId =     record.data.TRPLAN_ID;
//        var text = "<a href='#' onclick='cancelOrder(\""+trplanId+"\",\""+pickOrderId+"\",\""+pkgNos+"\")'>[取消]</a>";
        var print= "<a href='#' onclick='printOrder(\""+trplanId+"\",\""+pickOrderId+"\",\""+pkgNos+"\")'>[打印发运单]</a>";
       	return String.format(print);
    }
    function printOrder(trplanId,pickOrderId,pkgNos){
// 		var url3 = g_webAppName+"/parts/salesManager/carFactorySalesManager/PartPickOrder/printTransPlan.do?pickOrderId=" + pickOrderId+"&trplanId="+trplanId
// 		window.open(url3, '', 'left=0,top=0,width=' + screen.availWidth + '- 10,height=' + screen.availHeight + '-10,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
      window.open(g_webAppName+"/parts/salesManager/carFactorySalesManager/PartPickOrder/printTransPlan.do?pickOrderId=" + pickOrderId+"&trplanId="+trplanId);
		__extQuery__(1);
    }
    function cancelOrder(trplanId,pickOrderId,pkgNos){
        MyConfirm("确定取消",cancel,[trplanId,pickOrderId,pkgNos]);
    }

    function cancel(value){
    	var trplanId=value[0];
    	var pickOrderId=value[1];
    	var pkgNos=value[2];
		var url2 = g_webAppName+"/parts/salesManager/carFactorySalesManager/PartTransPlan/cancelTransPlan.json";
		var params = "trplanId="+trplanId+"&pickOrderId="+pickOrderId+"&pkgNos="+pkgNos;
		makeCall(url2,getResult,params);
    }
    function getResult(json){
		if(json.successMsg){
			MyAlert(json.successMsg);
			query("cancel");
		}else if(json.errorMsg){
			MyAlert(json.errorMsg);
		}		
    }    
    //生成对象
    function createObj(loc, soId) {
        for (var i = 0; i < loc.length; i++) {
            var obj = new Object();
            obj.soId = soId;
            obj.loc_id = loc[i].LOC_ID;
            obj.loc_name = loc[i].LOC_NAME;
            myObjArr.push(obj);
        }
    }
    //查看
    function detailOrder(value) {
        disableAllClEl();
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/detailOrder.do?pickOrderId=" + value;
    }
    //打印链接
    function printPkgOrder(id) {
        OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/opPrintHtml.do?pickOrderId=" + id, 1100, 400);
    }
    //发运计划
    function selectPkgNo(pickOrderId) {
    	OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTransPlan/pkgNoSelect.do?pickOrderId="+pickOrderId,800,450);
    }
    //出库
    function pkgPart(pickOrderId,pkgNos) {
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTransPlan/partOutstock.do?pickOrderId=" + pickOrderId+"&pkgNos="+pkgNos;
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

    function doQuery() {
        var msg = "";
        if ("" != document.getElementById("SendDate").value) {
            if ("" == document.getElementById("SstartDate").value) {
                msg += "请选择合并开始时间!</br>";
            }
        }
        if ("" != document.getElementById("SstartDate").value) {
            if ("" == document.getElementById("SendDate").value) {
                msg += "请选择合并结束时间!</br>";
            }
        }
        if (msg != "") {
            MyAlert(msg);
            return;
        }
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/amount.json";
        makeNomalFormCall(url, getFreightResult, 'fm');
        __extQuery__(1);
    }

    function getFreightResult(json){
    	var DCK_AMOUNT = json.DCK_AMOUNT==null?0:json.DCK_AMOUNT;
    	var YCK_AMOUNT = json.YCK_AMOUNT==null?0:json.YCK_AMOUNT;
    	$("#in_amount")[0].value=DCK_AMOUNT;
    	$("#out_amount")[0].value=YCK_AMOUNT;
    }

    function exportExcel() {
        fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/exportExcel.do";
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
    }
    function enableAllClEl() {
        enableAllBtn();
        enableAllA();
    }
    function detailOrder1(value, code) {
        var SstartDate = $('#SstartDate')[0].value;
        var SendDate = $('#SendDate')[0].value;
        OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/viewOutRepo.do",800,400);
    }
    //批量打印
    function batchPrint(){
    	var rsFlag = false;
    	var mt = document.getElementById("myTable");
    	if(mt == null){
    		MyAlert("请选择需要批量打印发运单！");
    		return;
    	}	
        for (var i = 1; i < mt.rows.length; i++) {
            var flag = mt.rows[i].cells[1].firstChild.checked;
        	if(flag){
        		rsFlag = true; 
            }
        }
        if(rsFlag){
			var url2 = g_webAppName+"/parts/salesManager/carFactorySalesManager/PartPickOrder/batchPrintTransPlan.do";
			document.fm.action = url2;
            document.fm.target="_blank";
			document.fm.submit();
        }else{
			MyAlert("请选择需要批量打印发运单！");
        }
        __extQuery__(1);
    }
    function closeWindow(){
        window.returnValue = "refresh";
        window.close();
    }
    $(document).ready(function(){
    	query('cancel');
    	enableAllClEl();
    });
</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
	<input type="hidden" name="queryType" id="queryType" value="cancel"/>
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理 &gt; 配件销售管理 &gt;配件发运计划&gt;打印</div>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table border="0" class="table_query">
	            <tr>
	                <td class="right">发运单号：</td>
	                <td width="24%"><input  type="text" id="TransCode" name="TransCode" class="middle_txt">
	                <td class="right">拣货单号：</td>
	                <td width="24%"><input class="middle_txt" type="text" id="pickOrderId" name="pickOrderId"/></td>
	               <%-- <td class="right">订货单位编码：</td>
	                <td width="24%"><input  type="text" id="dealerCode" name="dealerCode" class="middle_txt">
	                </td>--%>
	                <td class="right">订货单位：</td>
	                <td width="24%"><input  type="text" id="dealerName" name="dealerName" class="middle_txt">
	                </td>
	            </tr>
	            <tr>
	                <td class="right">发运日期：</td>
	                <td width="24%"><input name="SstartDate" type="text" class="short_txt" id="SstartDate" value="${old}" style="width:80px;"/>
	                    <input name="button" value=" " type="button" class="time_ico" />
	                    	至
	                    <input name="SendDate" type="text" class="short_txt" id="SendDate" value="${now}" style="width:80px;"/>
	                    <input name="button" value=" " type="button" class="time_ico" /></td>
	               <%-- <td class="right"><span class="right">发运方式</span>：</td>
	                <td width="24%">
	                    <select name="TRANS_TYPE" id="TRANS_TYPE" class="u-select">
	                        <option value="">-请选择-</option>
	                        <c:if test="${transList!=null}">
	                            <c:forEach items="${transList}" var="list">
	                                <option value="${list.fixValue }">${list.fixName }</option>
	                            </c:forEach>
	                        </c:if>
	                    </select>
	                </td>--%>
	                <%--<td class="right">VIN：</td>
	                <td width="24%"><input class="middle_txt" type="text" id="VIN" name="VIN"/></td>--%>
	                <td class="right">是否已打印：</td>
	                <td>
	                    <script type="text/javascript">
	                        genSelBox("IF_PRINT", <%=Constant.IF_TYPE%>, "<%=Constant.IF_TYPE_NO%>", true, "", "", "false", '');
	                    </script>
	                </td>
	                <td class="right">出库仓库：</td>
	                <td width="24%">
	                    <select name="whId" id="whId" class="u-select">
	                        <option selected value=''>-请选择-</option>
	                        <c:forEach items="${wareHouseList}" var="wareHouse">
	                            <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
	                        </c:forEach>
	                    </select>
	                </td>
	            </tr>
	            <tr>
	                <td colspan="6" class="center">
	                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="query('cancel');"/>
	                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="批量打印"  disabled="disabled"
	                           onclick="batchPrint();"/>
	                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="关 闭" onclick="closeWindow();"/>
	                </td>
	            </tr>
	        </table>
        	</div>
        </div>
    </div>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</html>
</form>
</body>
</html>
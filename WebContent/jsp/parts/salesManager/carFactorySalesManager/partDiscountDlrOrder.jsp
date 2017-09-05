<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>配件促销订单提报</title>
</head>
<script language="javascript">
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/queryPartDlrOrder.json?ORDER_TYPE="+<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08%>+"&discount=discount";
    var title = null;
    var columns = [
        //{header: "<input name='cbAll' id='cbAll' onclick='ckAll(this)' type='checkbox' />",  align:'center',dataIndex: 'ORDER_ID',renderer:checkLink},
        {header: "序号", align: 'center', renderer: getIndex},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'ORDER_ID', renderer: myLink, align: 'center'},
        {header: "订单号", dataIndex: 'ORDER_CODE', align: 'left'},
        {header: "销售单位", dataIndex: 'SELLER_NAME', align: 'left'},
        {header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'left', renderer: getItemValue},
        {header: "总金额(元)", dataIndex: 'ORDER_AMOUNT',  style: 'text-align:right'},
        {header: "订货单位编码", dataIndex: 'DEALER_CODE', align: 'left'},
        {header: "订货单位", dataIndex: 'DEALER_NAME', align: 'left'},
        {header: "订货人", dataIndex: 'BUYER_NAME', align: 'left'},
      	{header: "制单日期", dataIndex: 'CREATE_DATE', align: 'left'},
        {header: "提交日期", dataIndex: 'SUBMIT_DATE', align: 'left'},
        {header: "驳回原因", dataIndex: 'REBUT_REASON', align: 'left'},
        {header: "订单状态", dataIndex: 'STATE', align: 'left', renderer: getItemValue}
    ];

    function myLink(value, meta, record) {
        //只有已保存状态的才有[查看][修改][作废][提交]
        if (record.data.STATE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01%>||
        		record.data.STATE==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05%>) {
            if (record.data.ORDER_TYPE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_05%>) {
                return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[查看]</a>" + "<a href=\"#\" onclick='modifyOrder(\"" + value + "\")'>[修改]</a>" + "<a href=\"#\" onclick='confirmSubmitOrd(" + '"' + value + '"' + "," + record.data.SELLER_ID + "," + '"' + record.data.DEALER_ID + '"' + "," + '"' + record.data.ORDER_AMOUNT + '"' + "," + '"' + record.data.ORDER_CODE + '"'  + "," + '"' + record.data.FREIGHT + '"' +  ")'>[提交]</a>");
            }
            return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[查看]</a>" + "<a href=\"#\" onclick='modifyOrder(\"" + value + "\")'>[修改]</a>" + "<a href=\"#\" onclick='confirmCancelOrder(\"" + value + "\")'>[作废]</a>" + "<a href=\"#\" onclick='confirmSubmitOrd(" + '"' + value + '"' + "," + '"' + record.data.SELLER_ID + '"' + "," + '"' + record.data.DEALER_ID + '"' + "," + '"' + record.data.ORDER_AMOUNT + '"' + "," + '"' + record.data.ORDER_CODE + '"'  + "," + '"' + record.data.FREIGHT + '"' +  ")'>[提交]</a>");
        }
        return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[查看]</a>");
    }
	function checkLink(value, meta, record){
		if (record.data.STATE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01%>) {
            return String.format("<input id='cb' name='cb' type='checkbox' onclick='ck(this)' value='"+value+"' />");
        }
       return String.format('<img src="<%=contextPath%>/img/close.gif" />');
	}
    function ck(obj){
		var cb = document.getElementsByName("cb");
		var flag = true;
	    for (var i = 0; i < cb.length; i++) {
	        if(!cb[i].checked) {
	            flag= false;
	        }
	    }
	   $('#cbAll')[0].checked = flag;
	}
    function ckAll(obj){
	var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].disabled) {
            continue;
        }
        cb[i].checked = obj.checked;
    }
}
    //查看
    function detailOrder(value, code) {
        disableAllClEl();
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/detailDlrOrder.do?orderId=" + value + "&&orderCode=" + code;
    }
    //修改
    function modifyOrder(value) {
        disableAllClEl();
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/modifyDlrOrder.do?modifyFlag=discount&&orderId=" + value;
    }
    //作废
    function confirmCancelOrder(value) {
        MyConfirm("确定作废订单?", cancelOrder, [value]);
    }
    function cancelOrder(value) {
        disableAllClEl();
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/cancelOrder.json?orderId=" + value;
        makeNomalFormCall(url, getResult, 'fm');
    }

  //提交
    function confirmSubmitOrd(orderId, parentId, dealerId, amount, orderCode,freight) {
    	var msg = "";
    	if(parseFloat(freight)>0){
    		msg = "<font color='red' >订单总金额未达到免除运费金额，运费金额："+freight+"</font></br>";
    	}
    	msg +="确定提报订单?";
        MyConfirm(msg, submitOrd, [orderId, parentId, dealerId, amount, orderCode])
    }
    function submitOrd(orderId, parentId, dealerId, amount, orderCode) {
        disableAllClEl();
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/submitOrder.json?orderId=" + orderId + "&parentId=" + parentId
                + "&dealerId=" + dealerId + "&amount=" + amount + "&orderCode=" + orderCode;
        makeNomalFormCall(url, getResult, 'fm');
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
            }/*  else if (exceptions) {
                MyAlert(exceptions.message);
            } */
        }
    }
    //新增
    function addOrder() {
        disableAllClEl();
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/addDiscountOrder.do?";

    }
    //查询
    function doQuery() {
        var msg = "";
        //校验时间范围
        if (document.getElementById("SCREATE_DATE").value != "") {
            if (document.getElementById("ECREATE_DATE").value == "") {
                msg += "请填写制单结束日期!</br>";
            }
        }
        if (document.getElementById("ECREATE_DATE").value != "") {
            if (document.getElementById("SCREATE_DATE").value == "") {
                msg += "请填写制单开始日期!</br>";
            }
        }
        if (document.getElementById("SSUBMIT_DATE").value != "") {
            if (document.getElementById("ESUBMIT_DATE").value == "") {
                msg += "请填写提交结束日期!</br>";
            }
        }
        if (document.getElementById("ESUBMIT_DATE").value != "") {
            if (document.getElementById("SSUBMIT_DATE").value == "") {
                msg += "请填写提交开始日期!</br>";
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
    function exportEx() {
    	 var msg = "";
        //校验时间范围
        if (document.getElementById("SCREATE_DATE").value != "") {
            if (document.getElementById("ECREATE_DATE").value == "") {
                msg += "请填写制单结束日期!</br>";
            }
        }
        if (document.getElementById("ECREATE_DATE").value != "") {
            if (document.getElementById("SCREATE_DATE").value == "") {
                msg += "请填写制单开始日期!</br>";
            }
        }
        if (document.getElementById("SSUBMIT_DATE").value != "") {
            if (document.getElementById("ESUBMIT_DATE").value == "") {
                msg += "请填写提交结束日期!</br>";
            }
        }
        if (document.getElementById("ESUBMIT_DATE").value != "") {
            if (document.getElementById("SSUBMIT_DATE").value == "") {
                msg += "请填写提交开始日期!</br>";
            }
        }
        if (msg != "") {
            //弹出提示
            MyAlert(msg);
            return;
        }
        fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/exportPartPlanExcel.do";
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
	function repConfirm(){
		var cb = document.getElementsByName("cb");
		var flag = true;
		for (var i = 0; i < cb.length; i++) {
	        if(cb[i].checked) {
	            flag= false;
	        }
	    }
	    if(flag){
	    	MyAlert("请选择一条记录!");
	    	return;
	    }
	    MyConfirm("确认提交 ?",rep,[]);
	}
	function rep(){
		var ar = getCbArr();
		var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/batchRep.json?ar="+ar;
		disableAllClEl();
		makeNomalFormCall(url, getRepResult, 'fm');
	}
function getRepResult(jsonObj){
		enableAllClEl();
		if(jsonObj!=null){
		    var success = jsonObj.success;
		    var error = jsonObj.error;
		    var exceptions = jsonObj.Exception;
		    if(success){
		    	MyAlert(success);
		    	__extQuery__(1);
		    }else if(error){
		    	MyAlert(error);
		    }/* else if(exceptions){
		    	MyAlert(exceptions.message);
		    } */
		}
	}
	
	function getCbArr(){
	var cbArr = [] ;
	var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
          	cbArr.push(cb[i].value);
        }
    }
    return cbArr;
}
	function doInit(){
		//默认状态
		$('#state')[0].value=<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01%>;
		__extQuery__(1);
	}
</script>
<body onload="doInit();enableAllClEl()">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input id="ORDER_TYPE" name="ORDER_TYPE" type="hidden" value="<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08%>" />
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 配件管理 &gt;配件采购管理&gt;配件促销订单提报</div>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table border="0" class="table_query">
	            <tr>
	                <td width="10%" class="right">订单号：</td>
	                <td width="20%" class="left"><input class="middle_txt" type="text" id="ORDER_CODE" name="ORDER_CODE"/>
	                </td>
	                <td width="10%" class="right">订货单位：</td>
	                <td width="20%" class="left"><input class="middle_txt" type="text" id="DEALER_NAME" name="DEALER_NAME"/>
	                </td>
	                <td width="10%" class="right">销售单位：</td>
	                <td width="15%" class="left"><input class="middle_txt" type="text" id="SELLER_NAME" name="SELLER_NAME"/>
	                </td>
	                </tr>
	            <tr>
	                <td width="10%" class="right">订单状态：</td>
	                <td width="20%" class="left">
	                    <select  name="state" id = "state"  class="u-select">
			                <option selected value=''>-请选择-</option>
			                <c:forEach items="${stateMap}" var="stateMap" >
			                    <option value="${stateMap.key}">${stateMap.value}</option>
			                </c:forEach>
	            		</select>
	                </td>
	                <td width="10%" class="right">制单日期：</td>
	                <td width="20%" class="left"><input class="short_txt" id="SCREATE_DATE" name="SCREATE_DATE" value="${old}" style="width:80px"
	                                                    datatype="1,is_date,10" maxlength="10"
	                                                    group="SCREATE_DATE,ECREATE_DATE"/>
	                    <input class="time_ico" value=" " onclick="showcalendar(event, 'SCREATE_DATE', false);" type="button"/>
	                   	 至
	                    <input class="short_txt" id="ECREATE_DATE" name="ECREATE_DATE" datatype="1,is_date,10" value="${now}" style="width:80px"
	                           maxlength="10" group="SCREATE_DATE,ECREATE_DATE"/>
	                    <input class="time_ico" value=" " onclick="showcalendar(event, 'ECREATE_DATE', false);" type="button"/>
	                </td>
	                
	                <td width="10%" class="right">提交日期：</td>
	                <td width="20%" class="left"><input class="short_txt" id="SSUBMIT_DATE" name="SSUBMIT_DATE" style="width:80px"
	                                                    datatype="1,is_date,10" maxlength="10"
	                                                    group="SSUBMIT_DATE,ESUBMIT_DATE"/>
	                    <input class="time_ico" value=" " onclick="showcalendar(event, 'SSUBMIT_DATE', false);" type="button"/>
	                    至
	                    <input class="short_txt" id="ESUBMIT_DATE" name="ESUBMIT_DATE" datatype="1,is_date,10" style="width:80px"
	                           maxlength="10" group="SSUBMIT_DATE,ESUBMIT_DATE"/>
	                    <input class="time_ico" value=" " onclick="showcalendar(event, 'ESUBMIT_DATE', false);" type="button"/></td>
	            </tr>
	            
	            <tr>
	                <td colspan="6" class="center">
	                	<input name="BtnQuery" id="queryBtn" class="normal_btn" type="button"
	                                                      value="查 询" onclick="doQuery();"/>
	                    <input class="normal_btn" type="button" value="新 增" onclick="addOrder()"/>
	                    
	                    <input class="normal_btn" type="button" value="导 出" onclick="exportEx()"/></td>
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

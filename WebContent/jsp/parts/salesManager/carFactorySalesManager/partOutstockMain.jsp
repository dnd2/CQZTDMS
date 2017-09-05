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
    <title>配件出库单</title>
</head>
<script language="javascript">
    var myObjArr = [];
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/partOutstockQuery.json";
    var title = null;
    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
/*         {header: "<input type='checkbox' id='checkAll' name='checkAll' onclick='selectAll(this,\"ids\")' />", sortable: false, dataIndex: 'TRPLAN_ID', renderer: myCheckBox}, 批量出库才会使用复选框*/
        {id: 'action', header: "操作", sortable: false, dataIndex: 'TRPLAN_ID', align: 'center', renderer: myLink},
        {header: "发运单号", dataIndex: 'TRPLAN_CODE', align: 'center'},
        {header: "拣货单号", dataIndex: 'PICKORDERIDS', align: 'center'},
        {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
        {header: "订货单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
        {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
        {header: "箱号", dataIndex: 'PKG_NO', style: 'text-align:left'},
//         {header: "发运方式", dataIndex: 'TRANS_TYPE', align: 'center'},
//         {header: "承运物流", dataIndex: 'TRANSPORT_ORG', align: 'center'}
        {header: "发运方式", dataIndex: 'TRANS_TYPE_CN', align: 'center'},
        {header: "承运物流", dataIndex: 'TRANSPORT_ORG_CN', align: 'center'}
    ];

    
  //全选checkbox
    function myCheckBox(value, metaDate, record) {
	  var pickOrderId = record.data.PICKORDERIDS;
        return String.format("<input type='checkbox' name='ids' value='" + value + "' onclick='chkPart()'/>"
        +"<input type='hidden' name='pickOrderId_"+value+"' value='" + pickOrderId + "'/>");

    }
    
    function chkPart() {
        var cks = document.getElementsByName('ids');
        var flag = true;
        for (var i = 0; i < cks.length; i++) {
            var obj = cks[i];
            if (!obj.checked) {
                flag = false;
            }
        }
        document.getElementById("checkAll").checked = flag;
    }
    
    //add zhumingwei 2013-09-17
    function myLink(value, meta, record) {
       <%-- if (record.data.STATE ==<%=Constant.CAR_FACTORY_PKG_STATE_02%>) {
            return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\")'>[查看]</a>" + "<a href=\"#\" onclick='selectPkgNo(\"" + value + "\")'>[出库]</a>");
        }
         else {
        	return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\")'>[查看]</a>");
        }--%>
        return String.format("<a href=\"#\" onclick='pkgPart2(\"" + value + "\",\"" + record.data.PICKORDERIDS + "\")'>[出库]</a>");
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
        alert(id);
        OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/opPrintHtml.do?pickOrderId=" + id, 1100, 400);
    }
    //出库
    function selectPkgNo(pickOrderId) {
    	OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/pkgNoSelect.do?pickOrderId="+pickOrderId,730,390);
    }
    //出库
    function pkgPart(pickOrderId,pkgNos) {
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/partOutstock.do?pickOrderId=" + pickOrderId+"&pkgNos="+pkgNos;
    }
    //按发运单出库
    function pkgPart2(trplanId,pickOrderId) {
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/partOutstock.do?trplanId=" + trplanId+"&pickOrderId="+pickOrderId;
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
        var SstartDate = $('SstartDate').value;
        var SendDate = $('SendDate').value;
        OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/viewOutRepo.do",800,400);
    }
    
    function batchOutStock() {
        var chk = document.getElementsByName("ids");
        var l = chk.length;
        var cnt = 0;
        for (var i = 0; i < l; i++) {
            if (chk[i].checked) {
                cnt++;
            }
        }
        if (cnt == 0) {
            MyAlert("请选择要出库的发运单！");
            return;
        }

        MyConfirm("确认出库？", outStockOrder);
    }

    //批量出库
    function outStockOrder() {
        btnDisable();
        var url = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/createPartOutstockOrder.json';
        makeNomalFormCall(url, handleControl, 'fm');
    }
    
    function handleControl(jsonObj) {
        btnEnable();
        if (jsonObj != null) {
            var success = jsonObj.success;
            var error = jsonObj.error;
            var exceptions = jsonObj.Exception;
            var boId = jsonObj.boId;
            if (error) {
                alert(error);
                __extQuery__(1);
            } else if (success) {
            	alert(success);
                if(boId){
                    OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/showPartLocBoInit.do?boId="+boId,730,390);
                }
                __extQuery__(1);
            } else {
                MyAlert(exceptions.message);
            }
        }
    }
    
    function queryOutStockMain(){
    	__extQuery__(1);
    }
    
    $(document).ready(function(){
    	__extQuery__(1);
		enableAllClEl();
    });
</script>
</head>
<body onload=" ">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理 &gt; 配件销售管理 &gt;配件出库单</div>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table border="0" class="table_query">
	            <tr>
	                <td align="right">发运单号：</td>
	                <td width="24%"><input class="middle_txt" type="text" id="trplanCode" name="trplanCode"/></td>
	                <%--<td align="right">订货单位编码：</td>
	                <td width="24%"><input  type="text" id="dealerCode" name="dealerCode" class="middle_txt">--%>
	                <td align="right">拣货单号：</td>
	                <td width="24%"><input class="middle_txt" type="text" id="pickOrderId" name="pickOrderId"/></td>
	                </td>
	                <td align="right">订货单位：</td>
	                <td width="24%"><input  type="text" id="dealerName" name="dealerName" class="middle_txt">
	                </td>
	            </tr>
	            <%--<tr>
	                <td align="right">合并日期：</td>
	                <td width="24%"><input name="SstartDate" type="text" class="short_time_txt" id="SstartDate" value="${old}"/>
	                    <input name="button" value=" " type="button" class="time_ico"
	                           onclick="showcalendar(event, 'SstartDate', false);"/>
	                    至
	                    <input name="SendDate" type="text" class="short_time_txt" id="SendDate" value="${now}"/>
	                    <input name="button" value=" " type="button" class="time_ico"
	                           onclick="showcalendar(event, 'SendDate', false);"/></td>
	
	                <td align="right"><span align="right">发运方式</span>：</td>
	                <td width="24%">
	                    <select name="TRANS_TYPE" id="TRANS_TYPE"  class="u-select">
	                        <option value="">-请选择-</option>
	                        <c:if test="${transList!=null}">
	                            <c:forEach items="${transList}" var="list">
	                                <option value="${list.fixValue }">${list.fixName }</option>
	                            </c:forEach>
	                        </c:if>
	                    </select>
	                </td>
	                <td align="right">出库仓库：</td>
	                <td width="24%">
	                    <select name="whId" id="whId"  class="u-select">
	                        <option selected value=''>-请选择-</option>
	                        <c:forEach items="${wareHouseList}" var="wareHouse">
	                            <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
	                        </c:forEach>
	                    </select>
	                </td>
	            </tr>--%>
	            <!-- //add zhumingwei 2013-09-17 -->
	            <%--<tr>
	                <td align="right">是否已出库：</td>
	                <td width="24%">
	                    <script type="text/javascript">
	                        genSelBoxExp("pkgState", <%=Constant.IF_TYPE%>, "<%=Constant.IF_TYPE_NO%>", false, "short_sel", "", "false", '');
	                    </script>
	                </td>
	              &lt;%&ndash;  <td width="10%" align="right">已出库金额：</td>
	                <td width="20%">
	                    <input id="out_amount" class="middle_txt" type="text" style="background-color: #99D775;" readonly="readonly"/>
	                </td>
	                <td width="10%" align="right">待出库金额：</td>
	                <td width="20%">
	                    <input id="in_amount" class="middle_txt" type="text" style="background-color: #99D775;" readonly="readonly"/>
	                </td>&ndash;%&gt;
	            </tr>--%>
	            <tr>
	                <td colspan="6" class="center">
	                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1);"/>
	                    <%--&nbsp;
	                    <input type="button" class="normal_btn" value="出库快报" onclick="detailOrder1();"/>--%>
	                    <!-- &nbsp;
	                    <input type="button" class="normal_btn" value="批量出库" onclick="batchOutStock();"/> -->
	                </td>
	            </tr>
	            <!-- //add zhumingwei 2013-09-17 -->
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
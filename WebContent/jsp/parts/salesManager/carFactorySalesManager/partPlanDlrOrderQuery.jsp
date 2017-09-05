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
    <title>demo</title>
    <script language="javascript">

        //初始化查询TABLE
        var myPage;

        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/partDlrOrderQuery.json?ORDER_TYPE="+<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03%>;

        var title = null;

        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
            {header: "订货单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
            {header: "订货单位", dataIndex: 'DEALER_NAME', align: 'center'},
            {header: "订货人", dataIndex: 'BUYER_NAME', align: 'center'},
            {header: "订货日期", dataIndex: 'CREATE_DATE', align: 'center'},
            /*{header: "销售单位", dataIndex: 'SELLER_NAME', align:'center'},*/
            {header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'center', renderer: getItemValue},
            {header: "订货总金额(元)", dataIndex: 'ORDER_AMOUNT', align: 'center'},
            {header: "提交时间", dataIndex: 'SUBMIT_DATE', align: 'center'},
            {header: "订单状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue},
            {header: "已预审", dataIndex: 'IS_AUTCHK', align: 'center', renderer: getItemValue},
            {id: 'action', header: "操作", sortable: false, dataIndex: 'ORDER_ID', renderer: myLink, align: 'center'}
        ];
        function myLink(value, meta, record) {
            if (record.data.STATE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02%> || record.data.STATE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06%>) {
                return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[查看]</a>" + "<a href=\"#\" onclick='checkOrder(\"" + value + "\")'>[审核]</a>");
            }
            return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[查看]</a>");
        }
        //查看
        function detailOrder(value, code) {
            disableAllClEl();
            window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/detailDlrOrder.do?orderId=" + value + "&&orderCode=" + code;
        }
        //审核
        function checkOrder(value) {
            disableAllClEl();
            window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/checkPlanDlrOrder.do?orderId=" + value+"&planFlag="+$('planFlag').value;
        }
        //导出
        function exportEx() {
            fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/exportPartPlanExcel.do";
            fm.submit();
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
	if(expStr.indexOf(",")>0)
		arr = expStr.split(",");
	else {
		expStr = expStr+",";
		arr = expStr.split(",");
	}
	str += "<select id='" + id + "' name='" + id +"' class='"+ _class_ +"' " + _script_ ;
	// modified by lishuai@infoservice.com.cn 2010-05-18 解决select下拉框中增加属性datatype判断bug begin
	if(nullFlag && nullFlag == "true"){
		str += " datatype='0,0,0' ";
	}
	// end
	str += " onChange=doCusChange(this.value);> ";
	if(setAll){
		str += genDefaultOpt();
	}
	for(var i=0;i<codeData.length;i++){
		var flag = true;
		for(var j=0;j<arr.length;j++){
			if(codeData[i].codeId == arr[j]){
				flag = false;
			}
		}
		if(${planFlag=="noPlan"}){  
	        if (codeData[i].codeId ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03%>) {
	            continue;
	        }
        }
		if(codeData[i].type == type && flag){
			str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '"+codeData[i].codeDesc+"' >" + codeData[i].codeDesc + "</option>";
		}
	}
	str += "</select>";	
	document.write(str);
}
function doInit(){
	if(${planFlag=="plan"}){
		$('planType').style.display="none";
		$('planTypeSel').style.display="none";
	}
	loadcalendar();
	__extQuery__(1);
	enableAllClEl();
}
</script>
</head>
<body onload="doInit()">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input id="planFlag" name="planFlag" value="${planFlag}" type="hidden" />
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置： 配件管理 > 配件销售管理 >
            配件精品订单审核
        </div>
        <table border="0" class="table_query">
            <th colspan="7" width="100%"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>查询条件</th>
            <tr colspan="7" >
            <td  style="color:#252525;width:50px;text-align:right;">&nbsp;</td>
                <td class="table_query_2Col_label_5Letter">订单号：</td>
                <td width="24%"><input class="middle_txt" type="text" id="ORDER_CODE" name="ORDER_CODE"/></td>
                <td class="table_query_2Col_label_5Letter" id="planType">订单类型：</td>
                <td width="24%" id="planTypeSel">
                    <script type="text/javascript" >
                        genMySelBoxExp("ORDER_TYPE", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
                <td class="table_query_2Col_label_5Letter">销售单位：</td>
                <td width="24%"><input class="middle_txt" type="text" id="SELLER_NAME" name="SELLER_NAME"/></td>
            </tr>
            <tr>
            <td  style="color:#252525;width:50px;text-align:right;">&nbsp;</td>
                <td class="table_query_2Col_label_5Letter">制单日期：</td>
                <td width="24%"><input name="CstartDate" type="text" class="short_time_txt" id="CstartDate" value=""/>
                    <input name="button2" value=" " type="button" class="time_ico"
                           onclick="showcalendar(event, 'CstartDate', false);"/>
                    至
                    <input name="CendDate" type="text" class="short_time_txt" id="CendDate" value=""/>
                    <input name="button2" value=" " type="button" class="time_ico"
                           onclick="showcalendar(event, 'CendDate', false);"/></td>
                <td class="table_query_2Col_label_5Letter">提交时间：</td>
                <td width="24%"><input name="SstartDate" type="text" class="short_time_txt" id="SstartDate" value=""/>
                    <input name="button2" value=" " type="button" class="time_ico"
                           onclick="showcalendar(event, 'SstartDate', false);"/>
                    至
                    <input name="SendDate" type="text" class="short_time_txt" id="SendDate" value=""/>
                    <input name="button2" value=" " type="button" class="time_ico"
                           onclick="showcalendar(event, 'SendDate', false);"/></td>
                <td class="table_query_2Col_label_5Letter">订货单位：</td>
                <td width="24%"><input class="middle_txt" type="text" id="DEALER_NAME" name="DEALER_NAME"/></td>
            </tr>
            <tr>
            <td  style="color:#252525;width:50px;text-align:right;">&nbsp;</td>
                <td class="table_query_2Col_label_5Letter">已预审：</td>
                <td width="24%">
                    <script type="text/javascript">
                        genSelBoxExp("autoPreCheck", <%=Constant.PART_BASE_FLAG%>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
                <td class="table_query_2Col_label_5Letter">订单状态：</td>
                <td width="24%">
                    <script type="text/javascript">
                        genSelBoxExp("state", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE%>, "<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02%>", true, "short_sel", "", "false", '');
                    </script>
                </td>
            </tr>
            <tr>
                <td colspan="7" align="center"><input name="BtnQuery" id="queryBtn" class="normal_btn" type="button"
                                                      value="查 询" onclick="doQuery();"/>
                    &nbsp;
                    <input class="normal_btn" type="button" value="导 出" onclick="exportEx()"/></td>
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
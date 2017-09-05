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
    <title>拣货单打印</title>
</head>
<script language="javascript">
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPickOrder/partPickOrderQuery.json";
    var title = null;
    var columns = [
        {header: "<input name='cbAll' id='cbAll' onclick='ckAll(this)' type='checkbox' />", align: 'center', dataIndex: 'PICK_ORDER_ID', renderer: checkLink},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'PICK_ORDER_ID', align: 'center', renderer: getActionLink},
        {header: "拣货单号", dataIndex: 'PICK_ORDER_ID', align: 'center'},
        {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
        {header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'center',renderer:getItemValue},
        {header: "订货单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
        {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
        /*{header: "发运方式", dataIndex: 'TRANS_TYPE_NAME', align: 'center'},*/
       /* {header: "拣货单打印", dataIndex: 'PICK_ORDER_PRINT_NUM', align: 'center', renderer: getPrint},
        {header: "装箱单打印", dataIndex: 'PKG_PRINT_NUM', align: 'center', renderer: getPrint2},*/
        {header: "备注", dataIndex: 'REMARK', style: 'text-align:left'},
        {header: "总金额", dataIndex: 'TOTALMONEY', align: 'center', style: 'text-align:right'},
        {header: "拣货单生成日期", dataIndex: 'PICK_ORDER_CREATE_DATE', align: 'center'},
        {header: "拣货单打印日期", dataIndex: 'PICK_ORDER_PRINT_DATE', align: 'center'},
        {header: "拣货单打印次数", dataIndex: 'PICK_ORDER_PRINT_NUM', align: 'center'},
        {header: "合并人", dataIndex: 'CREATEBYNAME', align: 'center'},
        {header: "仓库", dataIndex: 'WH_NAME', style: 'text-align:left'}
        /*{header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}*/
        /*{id: 'backupAction', header: "补打操作", sortable: false, dataIndex: 'PICK_ORDER_ID', align: 'center', renderer: getBackupActionLink}*/
    ];
    function getActionLink(value, meta, record) {
        var flag = record.data.FLAG;
        //return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\")'>[查看]</a>" + "<a href=\"#\" onclick='printPickOrder(\"" + value + "\",\"" + record.data.PICK_ORDER_PRINT_NUM + "\")'>[打印拣配单]</a>");
       	var text = "<a href=\"#\" onclick='printPickOrder(\"" + value + "\",\"" + record.data.PICK_ORDER_PRINT_NUM + "\")'>[打印拣配单]</a>";
       	/* text = text + "<a href=\"#\" onclick='exportExcel(\"" + value + "\")'>[导出明细]</a>"; */
       	var cancelUrl="";
       	/* cancelUrl = "<a href=\"#\" onclick='cancelPick(\"" + value + "\")'>[取消拣货单]</a>"; */
        if(flag == '1'){
            return String.format(text);
        }
            return String.format(text+cancelUrl);

    }
    function exportExcel(pickOrderId){
        var url_t = g_webAppName+"/parts/salesManager/carFactorySalesManager/PartPickOrder/exportExcel.do?pickOrderId="+pickOrderId;
		document.fm.action=url_t;
		document.fm.target="_self";
		document.fm.submit();
	 }
    //补打操作
    /*function getBackupActionLink(value, meta, record) {
        var pkgDtlPrint = "<a href=\"#\" onclick='pkgDtlPrint(\"" + value + "\",\"" + record.data.PKG_PRINT_NUM + "\")'>[补打装箱单]</a>";
        return String.format("<a href=\"#\" onclick='printPickOrder(\"" + value + "\",\"" + record.data.PICK_ORDER_PRINT_NUM + "\")'>[补打捡配单]</a>" + pkgDtlPrint);

    }*/
    
    function detailOrder(id) {
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPickOrder/partPickOrderDetail.do?pickOrderId=" + id;
    }

    //装箱
    function pkgPart(pickOrderId) {
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/pkgOrder.do?pickOrderId=" + pickOrderId;
    }
    //运单
    function printTransOrder(pickOrderId) {
    	window.open("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPickOrder/printTransOrder.do?pickOrderId=" + pickOrderId, '', 'left=0,top=0,width=' + screen.availWidth + '- 10,height=' + screen.availHeight + '-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
    }
    //拣货单
    function printPickOrder(id, count) {
        var w = screen.availWidth - 10;
        var h = screen.availHeight - 50;
        var sFeatures = "top=0,left=0,width="+w+"px,height="+h+"px,location=yes,menubar=no,resizable=yes,scrollbars=yes,status=no,toolbar=no";
        var url_m = g_webAppName+"/parts/salesManager/carFactorySalesManager/PartPkg/opPrintHtml.do?pickOrderId="+id;
        /* if (count > 1) {
        	MyConfirm("您已经打印过" + count + "次拣货单?是否继续打印?",confirmResult,[id]);
            __extQuery__(1);
            return;
        } */
        window.open(url_m,'',sFeatures);
        __extQuery__(1);
    }
    function confirmResult(id){
    	 var w = screen.availWidth - 10;
         var h = screen.availHeight - 50;
         var sFeatures = "top=0,left=0,width="+w+"px,height="+h+"px,location=yes,menubar=no,resizable=yes,scrollbars=yes,status=no,toolbar=no";
         var url_m = g_webAppName+"/parts/salesManager/carFactorySalesManager/PartPkg/opPrintHtml.do?pickOrderId="+id;
         window.open(url_m,'', sFeatures);
    }
    function getPrint(num) {
        if (num > 0) {
            return "是";
        }
        return "否";
    }
    function getPrint2(num) {
        if (num > 0) {
            return "是";
        }
        return "否";
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
          return String.format("<input  name='cb' type='checkbox' value='" + value + "' />");
    }
    function batchPrint(){
    	var rsFlag = false;
    	var mt = document.getElementById("myTable");
    	if(mt == null){
    		MyAlert("请选择需要批量打印拣货单！");
    		return;
    	}	
        for (var i = 1; i < mt.rows.length; i++) {
            var flag = mt.rows[i].cells[0].firstChild.checked;
        	if(flag){
        		rsFlag = true; 
            }
        }
        if(rsFlag){
			var url2 = g_webAppName+"/parts/salesManager/carFactorySalesManager/PartPkg/batchPrintPickOrder.do";
			document.fm.action = url2;
			document.fm.target = "_blank";
			document.fm.submit();
        }else{
			MyAlert("请选择需要批量打印拣货单！");
        }
    }
    function cancelPick(id){
    	MyConfirm("确定取消拣货单?",confirmResult,[id]);
    }
    function confirmResult(id){
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/canclePickOrder.json?pickOrderId=" + id;
        makeNomalFormCall(url, getResult, 'fm');
    }
    function getResult(jsonObj) {
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
    function closeWindow(){
        window.returnValue = "refresh";
        window.close();
    }
    
    $(document).ready(function(){
    	__extQuery__(1);
    });
</script>
</head>
<body onafterprint="MyAlert('complete')" >
<form name="fm" id="fm" method="post">
	<div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理 &gt; 配件销售管理 &gt;拣货单管理&gt;拣货单打印</div>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table border="0" class="table_query">
	            <tr>
	                <td class="right">订单号：</td>
	                <td width="24%">
	                	<input class="middle_txt" type="text" id="orderCode" name="orderCode"/>
	                </td>
	                <td class="right">订货单位编码：</td>
	                <td width="24%">
	                	<input type="text" id="dealerCode" name="dealerCode" class="middle_txt"/>
	                </td>
	                <td class="right">订货单位：</td>
	                <td width="24%">
	                	<input type="text" id="dealerName" name="dealerName" class="middle_txt"/>
	                </td>
	            </tr>
	            <tr>
	                <td class="right">拣货单号：</td>
	                <td width="24%"><input class="middle_txt" type="text" id="pickOrderId" name="pickOrderId"/></td>
	                <td class="right">拣货单打印：</td>
	                <td width="24%">
	                    <script type="text/javascript">
	                        genSelBox("printFlag", <%=Constant.PART_BASE_FLAG%>, "<%=Constant.PART_BASE_FLAG_NO%>", true, "", "", "false", '');
	                    </script>
	                </td>
	                <td class="right">出库仓库：</td>
	                <td width="24%">
	                    <select name="whId" id="whId"  class="u-select">
	                        <option selected value=''>-请选择-</option>
	                        <c:forEach items="${wareHouseList}" var="wareHouse">
	                            <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
	                        </c:forEach>
	                    </select>
	                </td>
	            </tr>
	            <tr>
	                <td class="right">拣货单生成日期：</td>
	                <td width="24%"><input name="SstartDate" type="text" class="short_txt" style="width: 80px;"  id="SstartDate" value="${old}"/>
	                    <input name="button" type="button" class="time_ico" />
	                   	 至
	                    <input name="SendDate" id="SendDate" type="text" class="short_txt" style="width: 80px;"  id="SendDate" value="${now}"/>
	                    <input name="button"  type="button" class="time_ico" /></td>
	               <%-- <td class="right">装箱单打印：</td>
	                <td width="24%">
	                    <script type="text/javascript">
	                        genSelBox("TransFlag", <%=Constant.PART_BASE_FLAG%>, "", true, "", "", "false", '');
	                    </script>
	                </td>
	                <td class="right">是否装箱：</td>
	                <td width="24%">
	                    <script type="text/javascript">
	                        genSelBox("IsPkg", <%=Constant.PART_BASE_FLAG%>, <%=Constant.PART_BASE_FLAG_NO%>, true, "", "", "false", '');
	                    </script>
	                </td>--%>
	           <%-- </tr>
	            <tr>--%>
	                <td class="right">拣货单打印日期：</td>
	                <td width="24%"><input name="SstartDate1" type="text" class="short_txt" style="width: 80px;" id="SstartDate1"/>
	                    <input name="button" value=" " type="button" class="time_ico" />
	                    	至
	                    <input name="SendDate1" id="SendDate1" type="text" class="short_txt" style="width: 80px;"  id="SendDate1" />
	                    <input name="button" value=" " type="button" class="time_ico" />
	                </td>
	                <td class="right"><span class="right">订单类型</span>：</td>
	                <td width="24%">
	                    <script type="text/javascript">
	                        genSelBoxExp("ORDER_TYPE", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>, "", true, "", "", "false", '');
	                    </script>
	                </td>
	            </tr>
	            <tr>
	                <td colspan="6" class="center">
	                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
	                           onclick="doQuery();"/>
	                    &nbsp; <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="批量打印" onclick="batchPrint();"/>
	                    &nbsp; <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="关 闭"
	                                  onclick="closeWindow();"/>
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
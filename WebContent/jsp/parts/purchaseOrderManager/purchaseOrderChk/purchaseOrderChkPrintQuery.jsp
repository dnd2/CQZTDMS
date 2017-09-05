<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
    String error = request.getParameter("error");
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>验收单打印</title>
    <script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }

    </script>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：
    当前位置：配件管理&gt; 采购计划管理&gt; 验收单打印
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage"/>
    <input type="hidden" name="partId" id="partId"/>
    <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />查询条件</th>
	    <tr >
	       <td width="10%" align="right">验收单号：</td>
	       <td width="20%" ><input class="middle_txt" type="text" name="CHECK_CODE" id="CHECK_CODE"/></td>
	       <td width="10%"   align="right">计划员：</td>
            <td width="20%">
                <%--  <input class="phone_txt" type="text" id="PLANER_NAME" name="PLANER_NAME" /></td>--%>
                <select id="PLANER_ID" name="PLANER_ID" class="short_sel">
                    <option value="">-请选择-</option>
                    <c:forEach items="${planerList}" var="planerList">
                        <option value="${planerList.USER_ID }">${planerList.USER_NAME }</option>
                    </c:forEach>
                </select>
            </td>
	       <td width="10%" align="right">供应商：</td>
	       <td width="30%">
           <div align="left">
           		<input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME" />
			    <input class="mark_btn" type="button" value="&hellip;" onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
			    <INPUT class=normal_btn onclick="clearInput();" value=清除 type=button name=clrBtn>
			    <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
            </div>
           </td>
      </tr>
	    <tr>

	       <td width="10%" align="right">验收日期：</td>
	       <td width="20%">
	            <input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't1', false);"/>
           		&nbsp;至&nbsp;
           		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't2', false);"/>
	       </td>
            <td width="10%" align="right">库管员：</td>
            <td width="20%">
               <select id="WHMAN_ID" name="WHMAN_ID" class="short_sel">
                <option value="">-请选择-</option>
                <c:forEach items="${whmans}" var="whmans">
                    <option value="${whmans.WHMAN_ID }" ${whmans.WHMAN_ID eq userId?"selected":""}>${whmans.WHMAN_NAME }</option>
                </c:forEach>
            </select>
            </td>
	       <td width="10%" align="right">打印日期：</td>
	       <td width="20%">
           <div align="left">
           		<input name="pBeginTime" id="t3" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't3', false);"/>
           		&nbsp;至&nbsp;
           		<input name="pEndTime" id="t4" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't4', false);"/>
            </div>
           </td>
      </tr>
       <tr>
            <td width="10%" align="right">配件编码：</td>
            <td width="20%"><input name="PART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE"/></td>
            <td width="10%" align="right">配件名称：</td>
            <td width="20%"><input name="PART_CNAME" type="text" class="middle_txt" id="PART_CNAME"/></td>
            <td width="10%" align="right">配件件号：</td>
            <td width="20%"><input name="PART_CODE" type="text" class="middle_txt" id="PART_CODE"/></td>
        </tr>
        
        <!-- zhumingwei 2013-09-11 添加是否打印查询条件 begin -->
        <tr >
            <td align="right">是否打印：</td>
            <td>
            	<select id="is_print" name="is_print" class="short_sel">
                	<option value="">-请选择-</option>
                    <option value="0">未打印</option>
                    <option value="1">已打印</option>
            </select>
            </td>
        </tr>
        <!-- zhumingwei 2013-09-11 添加是否打印查询条件 end -->
        
	  <tr>
	   <td   colspan="6" align="center">
	   		<input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1);"/>
            <input name="BtnPrint" id="BtnPrint" class="normal_btn" type="button" value="打 印" onclick="printBatch();"/>
       </td>
      </tr>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript">
autoAlertException();//输出错误信息
var myPage;
var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryOrderChkInfo1.json";
<%-- var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryOrderChkAllInfo.json"; --%>

var title = null;

/* var columns = [
    {header: "序号", align:'center',renderer:getIndex},
    {header: "<input type='checkbox' name='checkAll' id='checkAll' onclick='selectAll(this,\"ids\")' />", sortable: false, dataIndex: 'CHK_ID', renderer: myCheckBox},
    {header: "验收单号", dataIndex: 'CHK_CODE', style: 'text-align:left', renderer:linkCode},
    {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
    {header: "制造商名称", dataIndex: 'MAKER_NAME', style: 'text-align:left'},
    {header: "验货日期", dataIndex: 'CREATE_DATE', align: 'center', renderer:formatDate},
    {header: "计划员", dataIndex: 'PLANER', style: 'text-align:left'},
    {header: "库管员", dataIndex: 'WHMAN', style: 'text-align:left'},
    {header: "打印日期", dataIndex: 'PRINT_DATE', align: 'center'},
    {header: "验货总数量", dataIndex: 'GENERATE_QTY', align: 'center'},
    {header: "打印次数", dataIndex: 'PRINT_TIMES', align: 'center'}/*,
    {id: 'action', header: "操作", sortable: false, dataIndex: 'CHK_ID,', renderer: myLink, align: 'center'}
]; */

var columns = [
    {header: "序号", align:'center',renderer:getIndex},
    {header: "<input type='checkbox' name='checkAll' id='checkAll' onclick='selectAll(this,\"ids\")' />", sortable: false, dataIndex: 'PO_ID', renderer: myCheckBox},
    {header: "验收单号", dataIndex: 'CHK_CODE', style: 'text-align:left', renderer:linkCode},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "验货总数量", dataIndex: 'GENERATE_QTY', align: 'center'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
    {header: "制造商名称", dataIndex: 'MAKER_NAME', style: 'text-align:left'},
    {header: "验货日期", dataIndex: 'CREATE_DATE', align: 'center', renderer:formatDate},
    {header: "计划员", dataIndex: 'PLANER', style: 'text-align:left'},
    {header: "库管员", dataIndex: 'WHMAN', style: 'text-align:left'},
    {header: "打印日期", dataIndex: 'PRINT_DATE', align: 'center'},
    {header: "打印次数", dataIndex: 'PRINT_TIMES', align: 'center'}
];

var len = columns.length;
//设置超链接  begin      

function myLink(value, meta, record) {
    return String.format("<a href=\"#\" onclick='printChkOrder(\"" + record.data.CHK_ID + "\")'>[打印]</a>");
}

function linkCode(value, meta, record) {
    return String.format("<a href=\"#\" onclick='viewChkOrder(\"" + record.data.CHK_ID + "\")'>"+value+"</a>");
}

function viewChkOrder(chkId){
	window.location.href='<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryChkOrderDetailInit.do?chkId='+chkId;
}

//全选checkbox
function myCheckBox(value, meta, record) {
    return String.format("<input type='checkbox' name='ids' value='" + value + "' onclick='chkPart()'/>");
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
//打印
function printChkOrder(value)
{
	OpenHtmlWindow("<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/opPrintHtml.do?chkId="+value,1100,400);
}

//批量打印验收单
/*function printBatch(){
    var ids = '';
    var chk = document.getElementsByName("ids");
    var n=0;
    for(var i = 0 ; i < chk.length ; i++){
        if(chk[i].checked){
           ids += chk[i].value+',';
           n++;
        }
    }
    if(n==0){
        MyAlert("请选择要打印的订货单!");
        return;
    }
    window.open("<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/opPrintListHtml.do?chkIds="+ids,"","toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500");
    //OpenHtmlWindow("<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/opPrintListHtml.do?chkIds="+ids,1100,400);
}*/

//批量打印验收明细
function printBatch(){
    var ids = '';
    var chk = document.getElementsByName("ids");
    var n=0;
    for(var i = 0 ; i < chk.length ; i++){
        if(chk[i].checked){
           ids += chk[i].value+',';
           n++;
        }
    }
    if(n==0){
        MyAlert("请选择要打印的配件!");
        return;
    }
    window.open("<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/opPrintChkHtml.do?poIds="+ids,"","toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500");
}

//格式化日期
function formatDate(value,meta,record){
	var output = value.substr(0,10);
	return output;
}

function clearInput() {
	//清空选定供应商
	document.getElementById("VENDER_ID").value = '';
	document.getElementById("VENDER_NAME").value = '';
}
</script>
</div>
</body>
</html>
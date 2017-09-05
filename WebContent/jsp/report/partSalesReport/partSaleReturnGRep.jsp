<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>进销存报表(供应中心)</title>
    <script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
           // __extQuery__(1);
        }

    </script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;供应中心报表&gt;销售退货报表(供应中心)
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />查询条件</th>
		<tr>
	           <td width="10%" align="right">退货日期：</td>
			   <td width="25%"><input id="checkSDate" class="short_txt"
				name="checkSDate" datatype="1,is_date,10" maxlength="10"
				group="checkSDate,checkEDate" value="${old}"/> <input class="time_ico"
				onclick="showcalendar(event, 'checkSDate', false);" value=" "
				type="button" />至 <input id="checkEDate"
				class="short_txt" name="checkEDate" datatype="1,is_date,10"
				maxlength="10" group="checkSDate,checkEDate" value="${now}"/> <input
				class="time_ico" onclick="showcalendar(event, 'checkEDate', false);"
				value=" " type="button" />
			  </td>
			  <td width="10%" align="right">服务商代码：</td>
              <td width="20%"><input name="dlrNCode" type="text" class="middle_txt" id="dlrNCode"/></td>
              <td width="10%" align="right">服务商名称：</td>
              <td width="20%"><input name="dlrNName" type="text" class="middle_txt" id="dlrNName"/></td>
	   </tr>
       <tr>
            <td width="10%" align="right">配件编码：</td>
            <td width="25%"><input name="partOldcode" type="text" class="long_txt" id="partOldcode"/></td>
            <td width="10%" align="right">配件名称：</td>
            <td width="20%"><input name="partCname" type="text" class="middle_txt" id="partCname"/></td>
            <td width="10%" align="right">配件件号：</td>
            <td width="20%"><input name="partCode" type="text" class="middle_txt" id="partCode"/></td>
        </tr>
        <tr>
        	<td width="10%" align="right">退货单号：</td>
            <td width="25%"><input name="returnCode" type="text" class="long_txt" id="returnCode"/></td>
        	<c:if test="${flag eq 1 }">
			        <td width="10%"   align="right">供应中心代码：</td>
                    <td width="20%">
                    <input class="middle_txt" type="text" readonly="readonly" style="background-color: #E5E3E3;" id="dealerCode" name="dealerCode" value="${venderCode }"/>
                    </td>
		  	</c:if>
		  	<c:if test="${flag eq 0 }">
                <td width="10%"   align="right">供应中心代码：</td>
                <td width="20%">
                <input class="middle_txt" type="text" id="dealerCode" name="dealerCode" value="${venderCode }"/>
                </td>
            </c:if>
          	<c:if test="${flag eq 1 }">
                <td width="10%"   align="right">供应中心名称：</td>
                <td width="20%">
                <input class="middle_txt" type="text" readonly="readonly" style="background-color: #E5E3E3;" id="dealerName" name="dealerName" value="${venderName }"/>
                <input type="hidden" id="dealerId" name="dealerId" value="${venderId }"/>
                </td>
            </c:if>
            <c:if test="${flag eq 0 }">
                <td width="10%"   align="right">供应中心名称：</td>
                <td width="20%">
                <input class="middle_txt" type="text"  id="dealerName" name="dealerName"/>
                <!-- 
                <input class="mark_btn" type="button" value="&hellip;"
                       onclick="showGyzx('dealerId','dealerName','dealerCode');"/>
                <input class="short_btn" onclick="clearGyzxInput();" value="清除" type="button" name="clrBtn"/> -->
                <input type="hidden" id="dealerId" name="dealerId" value=""/>
             	</td>
            </c:if>
        </tr>
        
	  <tr>
	   <td   colspan="6" align="center">
	   		<input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1);"/>
            <input class="normal_btn" type="button" value="导出" onclick="expMothStkAmtGyzxDlrExcel();"/>
       </td>
      </tr>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript">
var myPage;
var baseUrl = "<%=contextPath%>/report/partReport/partSalesReport/PartSaleReturnGReport/";
var url = baseUrl+"query.json";

var title = null;
var columns = [
    		{header: "序号", align:"center", renderer: getIndex},
        	{header: "退货单号", dataIndex: "RETURN_CODE", align:"center"},
        	{header: "服务商代码", dataIndex: "DEALER_CODE", align:"center"},
        	{header: "服务商名称", dataIndex: "DEALER_NAME", style: 'text-align:left'},
        	{header: "供应中心代码", dataIndex: "SELLER_CODE", align:"center"},
        	{header: "供应中心名称", dataIndex: "SELLER_NAME", style: 'text-align:left'},
        	{header: "配件编码", dataIndex: "PART_OLDCODE", style: 'text-align:left'},
        	{header: "配件名称", dataIndex: "PART_CNAME", style: 'text-align:left'},
        	{header: "配件件号", dataIndex: "PART_CODE", style: 'text-align:left'},
        	{header: "单位", dataIndex: "UNIT", align:"center"},
        	{header: "配件类型", dataIndex: "PART_TYPE", align:"center"},
        	{header: "退货数量", dataIndex: "RETURN_QTY", align:"center"},
        	{header: "单价", dataIndex: "BUY_PRICE", style: 'text-align:right'},
        	{header: "退货金额", dataIndex: "BUY_AMOUNT", style: 'text-align:right'},
        	{header: "原因", dataIndex: "REMARK", style: 'text-align:left'},
        	{header: "退货日期", dataIndex: "CREATE_DATE", align:"center"}
];

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
		var tbl = $("myTable");
		var rowObj = tbl.insertRow(tbl.rows.length);
		rowObj.className = "table_list_row1";
		var cell1 = rowObj.insertCell(0);
		    var cell2 = rowObj.insertCell(1);
		    var cell3 = rowObj.insertCell(2);
		    var cell4 = rowObj.insertCell(3);
		    var cell5 = rowObj.insertCell(4);
		    var cell6 = rowObj.insertCell(5);
		    var cell7 = rowObj.insertCell(6);
		    var cell8 = rowObj.insertCell(7);
		    var cell9 = rowObj.insertCell(8);
		    var cell10 = rowObj.insertCell(9);
		    var cell11 = rowObj.insertCell(10);
		    var cell12 = rowObj.insertCell(11);
		    var cell13 = rowObj.insertCell(12);
		    var cell14 = rowObj.insertCell(13);
		    var cell15 = rowObj.insertCell(14);
		    var cell16 = rowObj.insertCell(15);
		    cell1.innerHTML = '<tr><td></td>';
		    cell2.innerHTML = '<td><strong>合计：</strong></td>';
		    cell3.innerHTML = '<td></td>';
		    cell4.innerHTML = '<td></td>';
		    cell5.innerHTML = '<td></td>';
		    cell6.innerHTML = '<td></td>';
		    cell7.innerHTML = '<td></td>';
		    cell8.innerHTML = '<td></td>';
		    cell9.innerHTML = '<td></td>';
		    cell10.innerHTML = '<td></td>';
		    cell11.innerHTML = '<td></td>';
		    cell12.innerHTML = '<td></td>';
		    cell13.innerHTML = '<td></td>';
		    cell14.innerHTML = '<td><strong>'+json.allSalesAmount+'</strong></td>';
		    cell15.innerHTML = '<td></td>';
		    cell16.innerHTML = '<td></td>';
	}else{
		$("_page").show();
		$("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据!</div>";
		$("myPage").innerHTML = "";
		removeGird('myGrid');
		$('myGrid').hide();
		hiddenDocObject(1);
	}
}

//导出
function expMothStkAmtGyzxDlrExcel() {
    fm.action = baseUrl+"export.do";
    fm.target = "_self";
    fm.submit();
}

function clearGyzxInput() {
    document.getElementById("dealerId").value = '';
    document.getElementById("dealerCode").value = '';
    document.getElementById("dealerName").value = '';
}

function showGyzx(venderId,venderName,venderCode) {
    if (!venderName) {
    	venderName = null;
    }
    if (!venderId) {
    	venderId = null;
    }
    OpenHtmlWindow("<%=contextPath%>/jsp/parts/salesManager/carFactorySalesManager/gyzxSelect.jsp?venderName=" + venderName + "&venderId=" + venderId+"&venderCode="+venderCode, 730, 390);
}

</script>
</div>
</body>
</html>

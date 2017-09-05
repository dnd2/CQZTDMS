<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%String contextPath = request.getContextPath();%>
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>月度目标查询(本部)</title>
</head>
<body onload="dtlQuery();">
<form name="fm" method="post" enctype="multipart/form-data" id="fm">
    <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif">当前位置&gt;配件管理&gt;服务商月度目标&gt;月度目标完成(本部)</div>
    <table class="table_query">
        <input type="hidden" id="queryType" name="queryType" value="0"/>
        <th colspan="10"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
        <tr>
        	<td width="10%" align="right">选择服务商：</td>
            <td width="25%">
            	<input type="text" class="middle_txt" name="dealerCode" id="dealerCode" size="17" value=""/>
                <input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode', '', 'true', '', 'false', '', '','');" value="..."/>
                <input type="button" class="short_btn" onclick="clrTxt('dealerCode');" value="清 空" id="clrBtn"/>
            </td>
            <td width="8%" align="right">选择年：</td>
            <td width="5%"><select id="year" onchange="getPlanVer();" name="year">
                <%
                    String year = (String) request.getAttribute("curYear");
                    if (null == year || "".equals(year)) {
                        year = "0";
                    }
                    int y = Integer.parseInt(year);
                %>
                <option value="<%=y-1 %>"><%=y - 1 %>
                </option>
                <option value="<%=y %>" selected="selected"><%=y %>
                </option>
                <option value="<%=y+1 %>"><%=y + 1 %>
                </option>
                <option value="<%=y+2 %>"><%=y + 2 %>
                </option>
            </select>
            </td>
            <td width="8%" align="right">选择月：</td>
            <td width="15%">
                    <select name="month" id="month" onchange="getPlanVer()">
                  <%--  <option value=''>全年</option>--%>
                    <option value='1' <c:if test="${curMonth==1}">selected="selected"</c:if>>01</option>
                    <option value='2' <c:if test="${curMonth==2}">selected="selected"</c:if>>02</option>
                    <option value='3' <c:if test="${curMonth==3}">selected="selected"</c:if>>03</option>
                    <option value='4' <c:if test="${curMonth==4}">selected="selected"</c:if>>04</option>
                    <option value='5' <c:if test="${curMonth==5}">selected="selected"</c:if>>05</option>
                    <option value='6' <c:if test="${curMonth==6}">selected="selected"</c:if>>06</option>
                    <option value='7' <c:if test="${curMonth==7}">selected="selected"</c:if>>07</option>
                    <option value='8' <c:if test="${curMonth==8}">selected="selected"</c:if>>08</option>
                    <option value='9' <c:if test="${curMonth==9}">selected="selected"</c:if>>09</option>
                    <option value='10' <c:if test="${curMonth==10}">selected="selected"</c:if>>10</option>
                    <option value='11' <c:if test="${curMonth==11}">selected="selected"</c:if>>11</option>
                    <option value='12' <c:if test="${curMonth==12}">selected="selected"</c:if>>12</option>
                </select>
                   到
                 <select name="month2" id="month2" onchange="getPlanVer()">
                  <%--  <option value=''>全年</option>--%>
                    <option value='1' <c:if test="${curMonth==1}">selected="selected"</c:if>>01</option>
                    <option value='2' <c:if test="${curMonth==2}">selected="selected"</c:if>>02</option>
                    <option value='3' <c:if test="${curMonth==3}">selected="selected"</c:if>>03</option>
                    <option value='4' <c:if test="${curMonth==4}">selected="selected"</c:if>>04</option>
                    <option value='5' <c:if test="${curMonth==5}">selected="selected"</c:if>>05</option>
                    <option value='6' <c:if test="${curMonth==6}">selected="selected"</c:if>>06</option>
                    <option value='7' <c:if test="${curMonth==7}">selected="selected"</c:if>>07</option>
                    <option value='8' <c:if test="${curMonth==8}">selected="selected"</c:if>>08</option>
                    <option value='9' <c:if test="${curMonth==9}">selected="selected"</c:if>>09</option>
                    <option value='10' <c:if test="${curMonth==10}">selected="selected"</c:if>>10</option>
                    <option value='11' <c:if test="${curMonth==11}">selected="selected"</c:if>>11</option>
                    <option value='12' <c:if test="${curMonth==12}">selected="selected"</c:if>>12</option>
                </select>
            </td>
            <td width="8%" align="right">内部单位：</td>
	        <td width="5%">
	            <select id="is_type" name="is_type">
                    <option value="8" selected="selected">否</option>
                    <option value="9">是</option>
                </select>
            </td>
            <td width="8%" align="right">类型：</td>
	        <td width="10%">
	            <select id="type" name="type">
                    <option value="0">-请选择-</option>
                    <option value="3">销售员</option>
                    <option value="4">省份</option>
                    <option value="5">大区</option>
                </select>
            </td>
        </tr>
       
        <tr align="center">
            <td colSpan="10">
                <input class="cssbutton" onclick="collQuery();" value="汇总查询" type="button" name="queryBtn1"/>
                <input class="normal_btn" onclick="dtlQuery();" value=查询 type="button" name="queryBtn2"/>
                <input class="normal_btn" value="明细下载" type="button" name="queryBtn2" onclick="downloadFile();"/>
                <input class="normal_btn" value="汇总下载" type="button" name="queryBtn2" onclick="downloadFile1();"/>
            </td>
        </tr>
        <tr>
            <td colSpan="8" align="center">
                <font color="red">购买金额以出库为准，金额单位：(万元/不含税);购买金额中已扣除当月发生的退货金额!</font>
            </td>
        </tr>
    </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    <div style="width: 100%; height: 30px; float: left; " id="countDiv">
     <div style="width: 20%; height: 30px; float: left; background-color: #DAE0EE;">
     </div>
     <div style="width: 10%; height: 30px; float: left; background-color: #DAE0EE;">
     </div>
     <div style="width: 35%; height: 30px; line-height: 30px; float: left; text-align: right; background-color: #DAE0EE;">
       <font style="font-weight: bolder;">总&nbsp;&nbsp;计：&nbsp;&nbsp;&nbsp;&nbsp;</font>
     </div>
     <div style="width: 15%; height: 30px; line-height: 30px; float: left; text-align: center; background-color: #DAE0EE;">
       <font id="taskAmount"></font>
     </div>
     <div style="width: 10%; height: 30px; line-height: 30px; float: left; text-align: center; background-color: #DAE0EE;">
       <font id="saleAmount"></font>
     </div>
     <div style="width: 10%; height: 30px; line-height: 30px; float: left; text-align: center; background-color: #DAE0EE;">
       <font id="saleRate"></font>
     </div>
    </div>
</form>

<script type="text/javascript">
    var myPage;
    //查询路径
    var url;
    var title = null;
    var columns;

    function dtlQuery() {
        var queryType = document.getElementById("queryType").value;
        var year = document.getElementById("year").value;
    	var month = document.getElementById("month").value;
    	var month2 = document.getElementById("month2").value;
        var countDivObj = document.getElementById("countDiv");
        var type = document.getElementById("type").value;
        url = "<%=contextPath%>/parts/monthPlan/MonthPlanManager/planQueryBb.json";
        if (queryType == '0' || month == '') {
        	if( "" == year || "" == month || "" == month2)
        	{
            	MyAlert("请选择查询的年月!");
            	return;
        	}
        	else if(parseInt(month2) < parseInt(month))
        	{
        		MyAlert("截止月应大于起始月!");
        		return;
        	}
        	countDivObj.style.cssText = "display: block;";
        	if(type == '0'){
	            columns = [
					{header: "序号", align: 'center', renderer: getIndex,style: 'width: 10%;'},
	                {header: "年月", dataIndex: 'TASK_MONTH', align: 'center', style: 'width: 20%;'},
	                {header: "服务商代码", dataIndex: 'DEALER_CODE', align: 'center', style: 'width: 10%;'},
	                {header: "服务商名称", dataIndex: 'DEALER_NAME', align: 'center', style: 'text-align:left'},
	                {header: "任务金额", dataIndex: 'AMOUNT', align: 'center', style: 'width: 15%;'},
	                {header: "购买金额", dataIndex: 'SELLMONEY', align: 'center', style: 'width: 15%;'},
	                {header: "完成率", dataIndex: 'RATIO', align: 'center', style: 'width: 10%;'}
	            ];
	            normalAmountQuery();
        	}

        	if(type == '3'){
        		columns = [
					{header: "序号", align: 'center', renderer: getIndex,style: 'width: 10%;'},
  	                {header: "年月", dataIndex: 'TASK_MONTH', align: 'center', style: 'width: 20%;'},
  	                {header: "销售员", dataIndex: 'NAME', align: 'center', style: 'width: 10%;'},
  	                {header: "任务金额", dataIndex: 'AMOUNT', align: 'center', style: 'width: 15%;'},
  	                {header: "购买金额", dataIndex: 'SELLMONEY', align: 'center', style: 'width: 15%;'},
  	                {header: "完成率", dataIndex: 'RATIO', align: 'center', style: 'width: 10%;'}
  	            ];
  	            normalAmountQuery();
        	}
        	if(type == '4'){
        		columns = [
					{header: "序号", align: 'center', renderer: getIndex,style: 'width: 10%;'},
  	                {header: "年月", dataIndex: 'TASK_MONTH', align: 'center', style: 'width: 20%;'},
  	                {header: "省份", dataIndex: 'REGION_NAME', align: 'center', style: 'width: 10%;'},
  	                {header: "任务金额", dataIndex: 'AMOUNT', align: 'center', style: 'width: 15%;'},
  	                {header: "购买金额", dataIndex: 'SELLMONEY', align: 'center', style: 'width: 15%;'},
  	                {header: "完成率", dataIndex: 'RATIO', align: 'center', style: 'width: 10%;'}
  	            ];
  	            normalAmountQuery();
        	}
        	if(type == '5'){
        		columns = [
					{header: "序号", align: 'center', renderer: getIndex,style: 'width: 10%;'},
  	                {header: "年月", dataIndex: 'TASK_MONTH', align: 'center', style: 'width: 20%;'},
  	              	{header: "大区", dataIndex: 'ROOT_ORG_NAME', align: 'center', style: 'width: 10%;'},
  	                //{header: "省份", dataIndex: 'REGION_NAME', align: 'center', style: 'width: 10%;'},
  	                {header: "任务金额", dataIndex: 'AMOUNT', align: 'center', style: 'width: 15%;'},
  	                {header: "购买金额", dataIndex: 'SELLMONEY', align: 'center', style: 'width: 15%;'},
  	                {header: "完成率", dataIndex: 'RATIO', align: 'center', style: 'width: 10%;'}
  	            ];
  	            normalAmountQuery();
        	}
        }

        __extQuery__(1);
    }

    function collQuery() {
    	url = "<%=contextPath%>/parts/monthPlan/MonthPlanManager/planCountQueryBb.json";
    	var year = document.getElementById("year").value;
    	var month = document.getElementById("month").value;
    	var month2 = document.getElementById("month2").value;
    	var countDivObj = document.getElementById("countDiv");

    	var type = document.getElementById("type").value;
    	
    	if( "" == year || "" == month || "" == month2)
    	{
        	MyAlert("请选择查询的年月!");
        	return;
    	}
    	else if(parseInt(month2) < parseInt(month))
    	{
    		MyAlert("截止月应大于起始月!");
    		return;
    	}
    	else
    	{
    		countDivObj.style.cssText = "display: block;";
    		if(type == '0'){
	    		columns = [
						   {header: "序号", align: 'center', renderer: getIndex},
	                       {header: "起止年月", dataIndex: 'TASK_MONTH', align: 'center', style: 'width: 20%;'},
	                       {header: "服务商代码", dataIndex: 'DEALER_CODE', align: 'center', style: 'width: 10%;'},
	                       {header: "服务商名称", dataIndex: 'DEALER_NAME', align: 'center', style: 'text-align:left'},
	                       {header: "任务金额", dataIndex: 'AMOUNT', align: 'center', style: 'width: 15%;'},
	                       {header: "购买金额", dataIndex: 'SELLMONEY', align: 'center', style: 'width: 15%;'},
	                       {header: "完成率", dataIndex: 'RATIO', align: 'center', style: 'width: 10%;'}
	                   ];
	    		countAmountQuery();
	        	__extQuery__(1);
    		}
    		if(type == '3'){
    			columns = [
							{header: "序号", align: 'center', renderer: getIndex,style: 'width: 10%;'},
	                       {header: "起止年月", dataIndex: 'TASK_MONTH', align: 'center', style: 'width: 20%;'},
	                       {header: "销售员", dataIndex: 'NAME', align: 'center', style: 'width: 10%;'},
	                       {header: "任务金额", dataIndex: 'AMOUNT', align: 'center', style: 'width: 15%;'},
	                       {header: "购买金额", dataIndex: 'SELLMONEY', align: 'center', style: 'width: 15%;'},
	                       {header: "完成率", dataIndex: 'RATIO', align: 'center', style: 'width: 10%;'}
	                   ];
	    		countAmountQuery();
	        	__extQuery__(1);
    		}
    		if(type == '4'){
    			columns = [
							{header: "序号", align: 'center', renderer: getIndex,style: 'width: 10%;'},
	                       {header: "起止年月", dataIndex: 'TASK_MONTH', align: 'center', style: 'width: 20%;'},
	                       {header: "省份", dataIndex: 'REGION_NAME', align: 'center', style: 'width: 10%;'},
	                       {header: "任务金额", dataIndex: 'AMOUNT', align: 'center', style: 'width: 15%;'},
	                       {header: "购买金额", dataIndex: 'SELLMONEY', align: 'center', style: 'width: 15%;'},
	                       {header: "完成率", dataIndex: 'RATIO', align: 'center', style: 'width: 10%;'}
	                   ];
	    		countAmountQuery();
	        	__extQuery__(1);
    		}
    		if(type == '5'){
    			columns = [
							{header: "序号", align: 'center', renderer: getIndex,style: 'width: 10%;'},
	                       {header: "起止年月", dataIndex: 'TASK_MONTH', align: 'center', style: 'width: 20%;'},
	                       {header: "大区", dataIndex: 'ROOT_ORG_NAME', align: 'center', style: 'width: 10%;'},
	                       //{header: "省份", dataIndex: 'REGION_NAME', align: 'center', style: 'width: 10%;'},
	                       {header: "任务金额", dataIndex: 'AMOUNT', align: 'center', style: 'width: 15%;'},
	                       {header: "购买金额", dataIndex: 'SELLMONEY', align: 'center', style: 'width: 15%;'},
	                       {header: "完成率", dataIndex: 'RATIO', align: 'center', style: 'width: 10%;'}
	                   ];
	    		countAmountQuery();
	        	__extQuery__(1);
    		}
    	}
    }

    function normalAmountQuery()
    {
    	var url = "<%=contextPath%>/parts/monthPlan/MonthPlanManager/normalCountQueryBb.json";	
		sendAjax(url,countResult,'fm');
		
    }

    function countAmountQuery()
    {
    	var url = "<%=contextPath%>/parts/monthPlan/MonthPlanManager/countAmountQueryBb.json";	
		sendAjax(url,countResult,'fm');
    }

    function countResult(json){
		if(null != json){
			document.getElementById("taskAmount").innerText = addKannma(parseFloat(json.amountTotal).toFixed(2));
			document.getElementById("saleAmount").innerText = addKannma(parseFloat(json.saleTotal).toFixed(2));
			document.getElementById("saleRate").innerText = parseFloat(json.rate).toFixed(2) + "%";
		}
	}

  //千分格式
	function addKannma(number) {  
	    var num = number + "";  
	    num = num.replace(new RegExp(",","g"),"");   
	    // 正负号处理   
	    var symble = "";   
	    if(/^([-+]).*$/.test(num)) {   
	        symble = num.replace(/^([-+]).*$/,"$1");   
	        num = num.replace(/^([-+])(.*)$/,"$2");   
	    }   
	  
	    if(/^[0-9]+(\.[0-9]+)?$/.test(num)) {   
	        var num = num.replace(new RegExp("^[0]+","g"),"");   
	        if(/^\./.test(num)) {   
	        num = "0" + num;   
	        }   
	  
	        var decimal = num.replace(/^[0-9]+(\.[0-9]+)?$/,"$1");   
	        var integer= num.replace(/^([0-9]+)(\.[0-9]+)?$/,"$1");   
	  
	        var re=/(\d+)(\d{3})/;  
	  
	        while(re.test(integer)){   
	            integer = integer.replace(re,"$1,$2");  
	        }   
	        return symble + integer + decimal;   
	  
	    } else {   
	        return number;   
	    }   
	}
    
    //清空
    function clrTxt(txtId) {
        document.getElementById(txtId).value = "";
    }
    //是否是去查询版本
    function getPlanVer() {
        var cbox = document.getElementById("cbox");
        var month = document.getElementById("month").value;
        if (month != '') {
            if (cbox.checked) {
                var url = '<%=contextPath%>/parts/monthPlan/MonthPlanManager/getPlanVer.json';
                makeNomalFormCall(url, handleCel, 'fm');
            }
        }
    }
    function handleCel(json) {
        if (json != null) {
            var obj = document.getElementById("plan_ver");
            clrOptions(obj);
            if (json.maxVer != 0) {
                for (var i = 1; i <= json.maxVer; i++) {
                    var opt = document.createElement("option");
                    opt.value = i;
                    opt.appendChild(document.createTextNode(i));
                    obj.appendChild(opt);
                }
            }
        }
    }
    //清空版本OPTION
    function clrOptions(obj) {
        obj.options.length = 0;
    }

    function downloadFile() {
        fm.action = '<%=contextPath%>/parts/monthPlan/MonthPlanManager/downloadDataBb.do';
        fm.submit();
    }
    function downloadFile1() {
        fm.action = '<%=contextPath%>/parts/monthPlan/MonthPlanManager/downloadDataBb1.do';
        fm.submit();
    }
</script>
</body>
</html>

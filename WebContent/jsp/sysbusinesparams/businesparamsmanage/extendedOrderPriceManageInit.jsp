<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ taglib prefix="c" uri="/jstl/cout" %>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>超期常规订单资源单价维护</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  系统管理 &gt; 系统业务参数维护 &gt; 超期常规订单资源单价维护</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt"><div align="right">维护年份：</div></td>
				<td width="20%" >
      				<select id="year" name="year" onchange="cascadeMonth()">
      					<option value="" selected="selected">--请选择--</option>
      					<c:forEach items="${years}"  var="year">
	    					<option value="${year.SET_YEAR}">${year.SET_YEAR}</option>
      					</c:forEach>
      				</select>
    			</td>
    			<td width="10%" class="tblopt"><div align="right">维护月份：</div></td>
				<td width="20%" >
      				<select id="month" name="month">
      					<option id="monthDefault" value="" selected="selected">--请选择--</option>
      				</select>
    			</td>
				<td class="table_query_3Col_input" >
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> 
					<input type="button" class="normal_btn" onclick="addPrice();" value="新 增" id="queryBtn" /> 
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
<script type="text/javascript" >

	var myPage;
	
	var url = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/ExtendedOrderPriceMaintenance/extendedOrderPriceList.json";
	
	var title = null;

	var columns = [
				{header: "年份", dataIndex: 'PAYMENT_YEAR', align:'center'},
				{header: "月份", dataIndex: 'PAYMENT_MONTH', align:'center'},
				{header: "价格", dataIndex: 'PAYMENT_PRICE', align:'center'},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'PRICE_ID',renderer:myLink}
		      ];
		      
	function myLink(priceId){
        return String.format("<a href=\"<%=contextPath%>/sysbusinesparams/businesparamsmanage/ExtendedOrderPriceMaintenance/extendedOrderPriceInfo.do?priceId="
                + priceId + "\">[修改]</a>" <%-- 
                + "<a href=\"<%=contextPath%>/sysbusinesparams/businesparamsmanage/ExtendedOrderPriceMaintenance/delExtendedOrderPriceInfo.json?priceId="
                + priceId + "\">[删除]</a>" --%>);
    }
	
	function delPrice() {
		
	}

    function addPrice(){
    	sendAjax("<%=contextPath%>/sysbusinesparams/businesparamsmanage/ExtendedOrderPriceMaintenance/addExtendedOrderPriceInit.json", addInit, "fm");
    }
    
    function addInit(json) {
    	if(json.msg) {
    		MyAlert(json.msg);
    	} else {
	    	fm.action = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/ExtendedOrderPriceMaintenance/addExtendedOrderPriceInit.do";
			fm.submit();
    	}
    }
    
    function cascadeMonth() {
    	var year = document.getElementById('year').value;
    	if(year) {
    		sendAjax("<%=contextPath%>/sysbusinesparams/businesparamsmanage/ExtendedOrderPriceMaintenance/getCascadeMonth.json", fillMonth, "fm");
    	} 
    }
    
    function fillMonth(json) {
    	var month = $('month');
    	var months = json['months'];
    	while (month.firstChild) {
	    	month.removeChild(month.firstChild);
    	}
    	month.insert("<option id='monthDefault' value='' selected='selected'>--请选择--</option>")
    	for(var i = 0; i < months.length; i++) {
	    	/* var optionElement = document.createElement("OPTION");
	    	optionElement.value = months[i].SET_MONTH;
	    	optionElement.innerText = months[i].SET_MONTH;
	    	month.appendChild(optionElement); */
	    	month.insert("<option value=" + months[i].SET_MONTH + ">" + months[i].SET_MONTH + "</option>");
    	}
    	month.options[0].selected = 'selected';
    }
	    
 </script>    
</body>
</html>
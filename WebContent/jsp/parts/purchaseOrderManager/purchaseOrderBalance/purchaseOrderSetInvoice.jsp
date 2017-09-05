<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
	String contextPath = request.getContextPath();
	String balCode=request.getParameter("balCode");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<title>服务活动选择</title>
	<script type="text/javascript">

//设置发票号
function setInvoice() {
   var invoNo = document.getElementById("INVO_NO").value;
   var inAmountNotax = document.getElementById("IN_AMOUNT_NOTAX").value;
    if ((invoNo.trim()) == "") {
        MyAlert("请输入发票号!");
        return;
    }
    var reg2 = /(^[1-9]{1,7}?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-4]\.[0-9]([0-9])?$)/;  
	if(inAmountNotax.trim()=='' || inAmountNotax.trim()==null){
		MyAlert("请输入发票无税金额!");
		return;
	}else if(!reg2.test(inAmountNotax.trim())){
		MyAlert("请输入正确的发票无税金额!,小数点后面只能有两位！");
		document.getElementById("IN_AMOUNT_NOTAX").value="";
		return;
	}
    MyConfirm("确认设置？", setBalanceInvoNo);
}

//设置发票号
function setBalanceInvoNo() {
    var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/setBalanceOrderInvoNo.json';
    sendAjax(url, getResult, 'fm');
}

function getResult(jsonObj) {
    if (jsonObj != null) {
        var success = jsonObj.success;
        if (success=="1") {
        	alert("设置成功！");
            __parent().getInvoNo(jsonObj.balanceCode); 
            _hide();
        } else {
        	MyAlert("设置失败！请联系管理员！");
        }
    }
}
</script>
</head>
<body >
<form name="fm" id="fm" method="post">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp; 当前位置： 发票号设置</div>
  	<table class="table_query" id="uploadTable">
  		<input class="middle_txt" type="hidden" name="BAL_CODE" id="BAL_CODE" value="<%=balCode%>"/>
        <tr>
            <td class="right">发票号:</td>
            <td colspan="3">
				<textarea name="INVO_NO" id="INVO_NO" style="width: 90%" rows="2"></textarea>
			</td>
			<td class="right">发票无税金额:</td>
            <td >
				<input class="middle_txt" type="text" name="IN_AMOUNT_NOTAX" id="IN_AMOUNT_NOTAX"/>
			</td>
        </tr>
        <tr>
        	 <td class="center" colspan="6">
        	 <input type="button" class="normal_btn" onclick="setInvoice();" value="设置"/>
        	 &nbsp;
        	 <input type="button" class="normal_btn" onclick="_hide();" value="关闭"/>
        	 </td>
        </tr>
    </table>
	</form>
	</div>
</body>
</html>
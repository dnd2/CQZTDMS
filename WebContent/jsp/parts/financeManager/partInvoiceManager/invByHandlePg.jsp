<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>手工开票</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" >

$(function(){
	initData();
});

function initData()
{
 	var invAmount = document.getElementById("invAmount").value.replace(new RegExp(",","g"),"");
 	var tax = document.getElementById("tax").value;
 	var taxAmountObj = document.getElementById("taxAmount");
 	var invAmountNoTaxObj = document.getElementById("invAmountNoTax");

 	taxAmountObj.value = addKannma((parseFloat(tax) * parseFloat(invAmount)).toFixed(2));
 	invAmountNoTaxObj.value = addKannma((parseFloat(invAmount) -(parseFloat(tax) * parseFloat(invAmount)).toFixed(2)).toFixed(2));
}

function dataTypeCheck(obj)
{
	var value = obj.value;
    if (isNaN(value) || "" == value) {
        MyAlert("请输入数字!");
        obj.value = 0.17;
        return;
    }
    if(0 >= value)
    {
    	MyAlert("税率必须大于 0 !");
        obj.value = 0.17;
        return;
    }
    if(1 < value)
    {
    	MyAlert("税率不能大于 1 !");
        obj.value = 0.17;
        return;
    }
    obj.value = parseFloat(value).toFixed(2);
    var invAmount = document.getElementById("invAmount").value.replace(new RegExp(",","g"),"");
 	var tax =  parseFloat(value).toFixed(2);
 	var taxAmountObj = document.getElementById("taxAmount");
 	var invAmountNoTaxObj = document.getElementById("invAmountNoTax");

 	taxAmountObj.value = addKannma((parseFloat(tax) * parseFloat(invAmount)).toFixed(2));
 	invAmountNoTaxObj.value = addKannma((parseFloat(invAmount) -(parseFloat(tax) * parseFloat(invAmount)).toFixed(2)).toFixed(2));
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

function saveInvConfirm()
{
 	var invCode = document.getElementById("invCode").value;
 	var invNumber = document.getElementById("invNumber").value;
 	var invPerson = document.getElementById("invPerson").value;

 	if(null == invCode || "" == invCode)
 	{
 	 	MyAlert("发票代码不能为空!");
 	 	return false;
 	}
 	if(null == invNumber || "" == invNumber)
 	{
 	 	MyAlert("发票号码不能为空!");
 	 	return false;
 	}
 	if(null == invPerson || "" == invPerson)
 	{
 	 	MyAlert("开票人不能为空!");
 	 	return false;
 	}
 	
 	MyConfirm("确认保存开票信息?", saveInvByHandle);
 	
}

function saveInvByHandle()
{
	btnDisable();
	var url = "<%=contextPath%>/parts/financeManager/partInvoiceManager/partInvoiceAction/saveInvByHandle.json";	
	sendAjax(url,showResult,'fm');
}

function showResult(json) {
	btnEnable();
	if (json.errorExist != null && json.errorExist.length > 0) {
   		MyAlert(json.errorExist);
	} else if (json.success != null && json.success == "true") {
   		MyAlert("开票成功!");
		_hide();        
	} else {
		MyAlert("开票失败，请联系管理员!");
	}
}
	
//关闭
function returnBefore()
{
	parentContainer.__extQuery__(1);	
}

</script>

</head>
<body onbeforeunload="returnBefore();">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件财务管理 &gt; 财务金税发票 &gt; 手工开票
		</div>
		<form name='fm' id='fm' method="post">

			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/img/subNav.gif" /> 销售单信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">销售单号：</td>
							<td>
								<input class="middle_txt" id="saleCode" name="saleCode" type="text" value="${soCode }" readonly="readonly" />
								<input class="middle_txt" id="oldBillNo" name="oldBillNo" type="hidden" value="${billNo }" />
								<input class="middle_txt" id="outIds" name="outIds" type="hidden" value="${outId }" />
							</td>
							<td class="right">订货单位：</td>
							<td>
								<input class="middle_txt" id="dealerName" name="dealerName" type="text" value="${dealerName }" readonly="readonly" />
							</td>

							<td class="right">开票总金额(含税)：</td>
							<td width="20%">
								<input class="middle_txt" id="invAmount" name="invAmount" type="text" value="${invAmout }" readonly="readonly" />
							</td>
						</tr>
						<tr>
							<td class="right">税率：</td>
							<td>
								<input class="middle_txt" id="tax" name="tax" type="text" value="0.17" onchange="dataTypeCheck(this)" />
								<font color="red">*</font>
							</td>
							<td class="right">总税额：</td>
							<td>
								<input class="middle_txt" id="taxAmount" name="taxAmount" type="text" value="" readonly="readonly" />
								<font color="red">*</font>
							</td>

							<td class="right">开票总金额(无税)：</td>
							<td>
								<input class="middle_txt" id="invAmountNoTax" name="invAmountNoTax" type="text" value="" readonly="readonly" />
								<font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td class="right">发票代码：</td>
							<td>
								<input class="middle_txt" id="invCode" name="invCode" type="text" value="${invCode }" />
								<font color="red">*</font>
							</td>
							<td class="right">发票号码：</td>
							<td>
								<input class="middle_txt" id="invNumber" name="invNumber" type="text" value="${invNum }" />
								<font color="red">*</font>
							</td>

							<td class="right">开票人：</td>
							<td>
								<input class="middle_txt" id="invPerson" name="invPerson" type="text" value="${billBy }" />
								<font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td colspan="6" class="center">
								<input class="u-button" type="button" value="保 存" name="BtnQuery" id="saveBtn" onclick="saveInvConfirm()" />
								<input class="u-button" type="button" name="button1" value="关 闭" onclick="_hide();" />
							</td>
						</tr>
					</table>
					<br />
		</form>
</body>
</html>
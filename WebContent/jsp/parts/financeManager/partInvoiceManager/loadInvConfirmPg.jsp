<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.Map"%>
<%@ page import="java.util.List"%>

<%
	String contextPath = request.getContextPath();
	
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<title>财务开票导入信息</title>

</head>
<body onload="autoAlertException();">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />
		  &nbsp;当前位置： 配件财务管理 &gt; 财务金税发票 &gt; 财务开票导入信息确认
</div>
<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;未开票销售单</div>
 <form name="fm" method="post"  enctype="multipart/form-data" id="fm">
 <input type="hidden" name="unInvListSize" id="unInvListSize" value="${unInvListSize }"/>
 <input type="hidden" name="invTax" id="invTax" value="${tax }"/>
<table class="table_list" style="border-bottom: 1px;">
  <tr class="table_list_row1" style="background-color:#DAE0EE; ">
    <td width="8%" align="center">序号</td>
  	<td width="23%" align="center">销售单号</td>
  	<td width="23%" align="center">订货单位</td>
  	<td width="15%" align="center">发票号码</td>
  	<td width="10%" align="center">开票金额(元)</td>
  	<td width="15%" align="center">开票人</td>
  </tr>
  <c:if test="${unInvlist !=null}">
      <c:forEach items="${unInvlist}" var="list" varStatus="_sequenceNum">
          <c:if test="${((_sequenceNum.index+1) mod 2) != 0}">
              <tr class="table_list_row1">
          </c:if>
          <c:if test="${((_sequenceNum.index+1) mod 2) == 0}">
              <tr class="table_list_row2">
          </c:if>
          <td align="center" nowrap>${_sequenceNum.index+1}
          </td>
          <td align="center">
              <input name="soCode_${_sequenceNum.index+1}" id="soCode_${_sequenceNum.index+1}" value="${list.soCode}"
                     type="hidden"/>${list.soCode}
          </td>
          <td align="center" nowrap>
          	  <input name="dealerId_${_sequenceNum.index+1}" id="dealerId_${_sequenceNum.index+1}" value="${list.dealerId}"
                     type="hidden"/>
              <input name="dealerCode_${_sequenceNum.index+1}" id="dealerCode_${_sequenceNum.index+1}" value="${list.dealerCode}"
                     type="hidden"/>
              <input name="dealerName_${_sequenceNum.index+1}" id="dealerName_${_sequenceNum.index+1}" value="${list.dealerName}"
                     type="hidden"/>${list.dealerName}
          </td>
          <td align="center" nowrap>
              <input name="billNo_${_sequenceNum.index+1}" id="billNo_${_sequenceNum.index+1}" value="${list.billNo}"
                     type="hidden"/>${list.billNo}
          </td>
          <td align="center" nowrap>
              <input name="amount_${_sequenceNum.index+1}" id="amount_${_sequenceNum.index+1}" value="${list.amount}"
                     type="hidden"/>${list.amount}
          </td>
          <td align="center" nowrap>
              <input name="noTaxAmount_${_sequenceNum.index+1}" id="noTaxAmount_${_sequenceNum.index+1}" value="${list.noTaxAmount}"
                     type="hidden"/>
              <input name="taxAmount_${_sequenceNum.index+1}" id="taxAmount_${_sequenceNum.index+1}" value="${list.taxAmount}"
                     type="hidden"/>
              <input name="billDate_${_sequenceNum.index+1}" id="billDate_${_sequenceNum.index+1}" value="${list.billDate}"
                     type="hidden"/>
              <input name="billBy_${_sequenceNum.index+1}" id="billBy_${_sequenceNum.index+1}" value="${list.billBy}"
                     type="hidden"/>${list.billBy}
          </td>
          </tr>
      </c:forEach>
  </c:if>
</table>
<br/>
<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;<span style='color: red;'>已开票销售单</span></div>
<table class="table_list" style="border-bottom: 1px;">
  <tr class="table_list_row1" style="background-color:#DAE0EE; ">
    <td width="8%" align="center">序号</td>
  	<td width="23%" align="center">销售单号</td>
  	<td width="23%" align="center">订货单位</td>
  	<td width="15%" align="center">发票号码</td>
  	<td width="10%" align="center">开票金额(元)</td>
  	<td width="15%" align="center">开票人</td>
  </tr>
 <c:if test="${invedlist !=null}">
      <c:forEach items="${invedlist}" var="list" varStatus="_sequenceNum">
          <c:if test="${((_sequenceNum.index+1) mod 2) != 0}">
              <tr class="table_list_row1">
          </c:if>
          <c:if test="${((_sequenceNum.index+1) mod 2) == 0}">
              <tr class="table_list_row2">
          </c:if>
          <td align="center" nowrap>${_sequenceNum.index+1}
          </td>
          <td align="center">
              <input name="soCodeInv_${_sequenceNum.index+1}" id="soCodeInv_${_sequenceNum.index+1}" value="${list.soCode}"
                     type="hidden"/>${list.soCode}
          </td>
          <td align="center" nowrap>
              <input name="dealerIdInv_${_sequenceNum.index+1}" id="dealerIdInv_${_sequenceNum.index+1}" value="${list.dealerId}"
                     type="hidden"/>${list.dealerName}
          </td>
          <td align="center" nowrap>
              <input name="billNoInv_${_sequenceNum.index+1}" id="billNoInv_${_sequenceNum.index+1}" value="${list.billNo}"
                     type="hidden"/>${list.billNo}
          </td>
          <td align="center" nowrap>
              <input name="amountInv_${_sequenceNum.index+1}" id="amountInv_${_sequenceNum.index+1}" value="${list.amount}"
                     type="hidden"/>${list.amount}
          </td>
          <td align="center" nowrap>
              <input name="billByInv_${_sequenceNum.index+1}" id="billByInv_${_sequenceNum.index+1}" value="${list.billBy}"
                     type="hidden"/>${list.billBy}
          </td>
          </tr>
      </c:forEach>
  </c:if>
</table>
<table width="95%"  align="center" class="table_query">
  <tr class=csstr>
    <td align="center"><input class="normal_btn" type="button" value="确 定" name="button1"  onClick="add();">
        <input class="normal_btn" type="button" value="返 回" name="button1"  onClick="goBack()">
    </td>
  </tr>
</table>
</form>
<script type="text/javascript">

 //服务商资金确认导入：
	function add(parms) {
		if(confirm("确认提交开票信息?")){
			btnDisable();
		    var url = '<%=contextPath%>/parts/financeManager/partInvoiceManager/partInvoiceAction/commitInvoices.json';
		  	makeFormCall(url,showResult,'fm');
		    }
	}

   function showResult(json) {
	   btnEnable();
	   if(null != json)
	   {
		   if (json.errorExist != null && json.errorExist.length > 0) {
			   MyAlert(json.errorExist);
		   } 
	       else if (json.success != null && json.success == "true") {
	       	   MyAlert("开票信息提交成功!");
	       	   goBack();
	       } else {
	           MyAlert("开票信息提交失败，请联系管理员!");
	       }
	   }
       
   }

   function goBack(){
		btnDisable();
		fm.action = "<%=contextPath%>/parts/financeManager/partInvoiceManager/partInvoiceAction/partInvoiceQueryInit.do";
		fm.submit();
	}

 	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	}
</script>
</body>
</html>

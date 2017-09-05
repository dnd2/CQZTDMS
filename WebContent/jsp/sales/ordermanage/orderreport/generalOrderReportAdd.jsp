<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.List"%>
<% 
List materialList = (List)request.getAttribute("materialList");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>常规订单提报</title>
<script type="text/javascript">
	function doInit(){
		checkPrice();
		totalPrice();
	}
	
	function checkPrice(){
		var size = <%=materialList.size()%>;
   		for(var i=1; i<=size; i++){
   			var price = document.getElementById("price"+i).value;
   			var priceTdObj = document.getElementById("priceTd"+i);
   			var amountObj = document.getElementById("amount"+i);
   			if(parseFloat(price, 10) >= <%=Constant.MATERIAL_PRICE_MAX%>){
   				//priceTdObj.innerHTML = "尚未维护<input type='hidden' name='price"+i+"' value='"+price+"'>";
   				//amountObj.disabled = "disabled";
   			}
   		}
	}
	
	function totalPrice(){
		var size = <%=materialList.size()%>;
		var amountTotal = 0;
		var hejiTotal = 0;
   		for(var i=1; i<=size; i++){
   			var amountObj = document.getElementById("amount"+i);
   			var priceObj = document.getElementById("price"+i);
   			var hejiObj = document.getElementById("heji"+i);
   			amountTotal += parseInt(amountObj.value, 10);
   			var hejiValue =  parseFloat(priceObj.value, 10) * parseInt(amountObj.value, 10); 
   			hejiObj.innerHTML = (hejiValue == 0 ? "0" : amountFormat(hejiValue));
   			hejiTotal += parseFloat(priceObj.value, 10) * parseInt(amountObj.value, 10);
   		}
   		var amountTotalObj = document.getElementById("amountTotal");
   		var hejiTotalObj = document.getElementById("hejiTotal");
   		amountTotalObj.innerHTML = amountTotal;
   		document.getElementById('applyAmount').value = amountTotal ;
   		hejiTotalObj.innerHTML = (hejiTotal == 0 ? "0" : amountFormat(hejiTotal));
   		document.getElementById("total").value = hejiTotal;
	}	

	function confirmAdd(){
		if(submitForm('fm')){
			var wtb = document.getElementById("amountTotal").innerHTML;
			var ytb = ${detail.YTB};
			var quota = ${detail.QUOTA_AMT};
			/*if(wtb == 0){
				MyAlert("提报数量不能为0！");
				return false;
			}
			if(parseInt(wtb, 10)+parseInt(ytb, 10)>parseInt(quota, 10)){
				MyAlert("各个配置的“本次提报数量”不能小于对应配置的“最小提报量”！");
				return false;
			}*/
			MyConfirm("是否确认新增?",orderAdd);
		}
	}
	
	function orderAdd(){
		disableBtn($("baocun"));
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/GeneralOrderReport/generalOrderReoprtAdd.json',showResult,'fm');
	}
	
	function showResult(json){
		if(json.returnValue == '1'){
			if(json.sysServer == "jc") {
				window.location.href = '<%=request.getContextPath()%>/sales/ordermanage/orderreport/GeneralOrderReport/generalOrderReoprtQueryPre.do?COMMAND=1';
			} else if(json.sysServer == "cvs") {
				window.location.href = '<%=request.getContextPath()%>/sales/ordermanage/orderreport/GeneralOrderReport/generalOrderReoprtQueryPreCVS.do?COMMAND=1';
			}
			
		} else if (json.returnValue == '2'){
			parent.MyAlert("新增失败：申请数量已超出最大配额！");
			if(json.sysServer == "jc") {
				window.location.href = '<%=request.getContextPath()%>/sales/ordermanage/orderreport/GeneralOrderReport/generalOrderReoprtQueryPre.do?COMMAND=1';
			} else if(json.sysServer == "cvs") {
				window.location.href = '<%=request.getContextPath()%>/sales/ordermanage/orderreport/GeneralOrderReport/generalOrderReoprtQueryPreCVS.do?COMMAND=1';
			}
		} else{
			MyAlert("新增失败！请联系系统管理员！");
			useableBtn($("baocun"));
		}
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单提报 > 常规订单提报</div>
<form method="POST" name="fm" id="fm">
  <table class="table_list" style="border-bottom:1px solid #DAE0EE" >
  <tr align="center" class="tabletitle">
    <th>周度</th>
    <th>车系代码</th>
    <th>车系名称</th>
    <th>配置代码</th>
    <th>配置名称</th>
    <th>配额数量</th>
    <th><p>最小提报数量</p></th>
    <th align="center">已提报数量</th>
    <th align="center">本次提报数量</th>
  </tr>
  <tr class="table_list_row1">
    <td align="center">${detail.QUOTA_DATE}</td>
    <td align="center" >${detail.SERIES_CODE}</td>
    <td align="center" >${detail.SERIES_NAME}</td>
    <td align="center">${detail.GROUP_CODE}</td>
    <td align="center">${detail.GROUP_NAME}</td>
    <td align="center" >${detail.QUOTA_AMT}</td>
    <td align="center" >${detail.MIN_AMOUNT}</td>
    <td align="center">${detail.YTB}</td>
    <td align="center">${detail.WTB}</td>
  </tr>
</table>
<TABLE class=table_list style="border-bottom:1px solid #DAE0EE" >  
    <TR class=cssTable style="cursor:hand">
      <th nowrap="nowrap">车系</th>
      <th nowrap="nowrap">物料编号</th>
      <th nowrap="nowrap">物料名称</th>
      <th nowrap="nowrap">颜色</th>
      <th nowrap="nowrap">数量</th>
      <th nowrap="nowrap" style="display:none">单价</th>
      <th nowrap="nowrap" style="display:none">合计</th>
    </tr>
    <%int index = 0;%>
    <c:forEach items="${materialList}" var="po">
    	<%index++;%>
     <tr class="table_list_row1">
      <td align="center" nowrap="nowrap">${detail.SERIES_NAME}</td>
      <td align="center" nowrap="nowrap">${po.MATERIAL_CODE}<input type="hidden" name="material<%=index%>" value="${po.MATERIAL_ID}"></td>
      <td align="center" nowrap="nowrap"><p>${po.MATERIAL_NAME}</p></td>
      <td align="center" nowrap="nowrap">${po.COLOR_NAME}</td>
      <td align="center" nowrap="nowrap"><input id="amount<%=index%>" name="amount<%=index%>" type="text"  class='SearchInput' value="${po.ORDER_AMOUNT}" datatype="0,is_digit,6" size="2" maxlength="6" onchange="totalPrice();"></td>
      <td align="center" nowrap="nowrap" id="priceTd<%=index%>" style="display:none">
      	<script type="text/javascript">document.write(amountFormat(${po.PRICE}));</script>
      	<input type="hidden" name="price<%=index%>" value="${po.PRICE}"> 
      	<input type="hidden" name="priceListId<%=index%>" value="${po.PRICELISTID}"> 
      </td>
      <td align="center" nowrap="nowrap" id="heji<%=index%>" style="display:none"></td>
     </tr>
    </c:forEach>
    <tr class="table_list_row1">
      <td nowrap="nowrap"  >&nbsp;</td>
      <td nowrap="nowrap" >&nbsp;</td>
      <td align="right" nowrap="nowrap"  >&nbsp;</td>
      <td align="center" nowrap="nowrap" ><strong>合计： </strong></td>
      <td align="center" nowrap="nowrap" id="amountTotal"></td>
      <td align="center" nowrap="nowrap" style="display:none">&nbsp;</td>
      <td align="center" nowrap="nowrap" id="hejiTotal" style="display:none"></td>
    </tr>
  </table>	
  <TABLE class=table_query>
    <TR class=cssTable style="cursor:hand">
      <td width="100%" align=left>
      	<input type="hidden" name="year" value="${detail.QUOTA_YEAR}">
      	<input type="hidden" name="week" value="${detail.QUOTA_WEEK}">
      	<input type="hidden" name="areaId" value="${detail.AREA_ID}">
      	<input type="hidden" name="dealerId" value="${detail.DEALER_ID}">
      	<input type="hidden" name="index" value="<%=index%>">
      	<input type="hidden" name="total" value="0">
      	<input type="hidden" id="applyAmount" name="applyAmount" value="0" />
      	<input type="hidden" id="groupId" name="groupId" value="${groupId }" />
      	<input class="cssbutton" name="baocun" type="button"  value="保存" onClick="confirmAdd();">
        <input class="cssbutton" name="shangbao2" type="button" value="返回" onclick="history.back();" />
      </td>
    </tr>
 </table>
</form>
</body>
</html>

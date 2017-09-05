<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.bean.TtAsRepairOrderExtBean"%>
<%@page import="com.infodms.dms.po.TtAsRoAddItemPO"%>
<%@page import="com.infodms.dms.po.TtAsRoRepairPartBean"%> 
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.text.DecimalFormat" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%String contextPath = request.getContextPath();%>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<%@taglib uri="/jstl/change" prefix="change" %>
<script language="javascript">    
function printsetup(){    
	// 打印页面设置    
	wb.execwb(8,1);    
} 

function printpreview(){    
	// 打印页面预览      
	wb.execwb(7,1);    
}      
function printit(){    
	if (confirm('确定打印吗？')){
	wb.execwb(6,6)    
	}    
}    

function nowprint() {   
    window.print();   
}   
function window.onbeforeprint() {   
    eval(visble_property_printview + " = \"" + visble_property_false + "\"");   
}   
function window.onafterprint() {   
    eval(visble_property_printview + " = \"" + visble_property_true + "\"");   
}   

function sxsw(){
	WebBrowser.ExecWB(7,1); 
	window.opener=null; 
	window.close();
}
$(function(){
	var amountAll=0;
	var part_cost_amount=$("input[name='part_cost_amount']");
	var labour_amount=$("input[name='labour_amount']");
	var acc_amount=$("input[name='acc_amount']");
	var discount_amount=$("#discount_amount").text();
	var out_amount=$("#out_amount").text();
	if(""!=out_amount){
		amountAll+=parseFloat(out_amount);
	}
	if(labour_amount.length>0 ){
		$(labour_amount).each(function(){
			amountAll+=parseFloat($(this).val());
		});
	}
	if(part_cost_amount.length>0 ){
		$(part_cost_amount).each(function(){
			amountAll+=parseFloat($(this).val());
		});
	}
	if(acc_amount.length>0 ){
		$(acc_amount).each(function(){
			amountAll+=parseFloat($(this).val());
		});
	}
	
	$("#amountAll").text(amountAll.toFixed(2));
	
	var discount= parseFloat(discount_amount);
	var real_amount=amountAll-discount;
	$("#real_amount").text(real_amount.toFixed(2));
})
</script>

<style media=print>
    /* 应用这个样式的在打印时隐藏 */
    .Noprint {
     display: none;
    }
   
    /* 应用这个样式的，从那个标签结束开始另算一页，之后在遇到再起一页，以此类推 */
    .PageNext {
     page-break-after: always;
    }
   </style>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	</head>
<body onload="init(); ffTableId(); tiaoma();" >
  <object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
<div id="topContainer" style="" >
<br/>
<center>
  <strong><font size="5px">北汽幻速${ro.DEALER_SHORTNAME }服务站维修结算工单</font></strong>
</center>
<br/>
<form method="post" name="fm" id="fm">
<center>
<table class="tab_print" width="750px">
	 <tr >
	 <td width="10%"nowrap="true" style="font-weight: normal;" style="text-align: left;border: none;">
	 		&nbsp;&nbsp;工单号:${ro.RO_NO }
       </td>
      <td width="9%"nowrap="true" style="border: none;">
      
       </td>
      <td width="8%"nowrap="true"style="border: none;">
       </td>
       <td width="25%"nowrap="true" style="border: none;">
       
       </td>
      <td width="8%"nowrap="true" style="border: none;">
       </td>
       <td width="25%"nowrap="true">
       </td>
    </tr>
    <tr >
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">开单日期
       </td>
      <td width="25%"nowrap="true">
       	<fmt:formatDate value="${ro.CREATE_DATE }" pattern='yyyy-MM-dd hh:mm:ss'/>
       </td>
       <td width="8%"nowrap="true"style="font-weight: bold; font-size:12px;">服务商电话
       </td>
       <td width="25%"nowrap="true">
        ${ro.PHONE }
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">服务顾问
       </td>
      <td width="25%"nowrap="true">
     	 ${ro.SERVICE_ADVISOR }
       </td>
    </tr>
	 <tr >
       <td width="8%"nowrap="true"style="font-weight: bold; font-size:12px;">结算日期
       </td>
       <td width="25%"nowrap="true">
       	<fmt:formatDate value="${ro.FOR_BALANCE_TIME }" pattern='yyyy-MM-dd hh:mm:ss'/>
       </td>
      <td width="8%"nowrap="true"style="font-weight: bold; font-size:12px;">服务商地址
       </td>
       <td width="25%"nowrap="true" colspan="3">
       		${ro.ADDRESS }
       </td>
	 <tr >
      <td colspan="6" style="text-align: center; font-size: 18px; font-weight: bold;">基本信息
       </td>
    </tr>
	 <tr >
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">车牌
       </td>
      <td width="25%"nowrap="true">
      	${ro.LICENSE }
       </td>
      <td width="8%"nowrap="true"style="font-weight: bold; font-size:12px;">送修人姓名
       </td>
       <td width="25%"nowrap="true">
       ${ro.DELIVERER }
       </td>
      <td width="8%"nowrap="true"style="font-weight: bold; font-size:12px;">车型
       </td>
       <td width="25%"nowrap="true">
       ${ro.MODEL }
       </td>
    </tr>
	 <tr >
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">行驶里程
       </td>
      <td width="25%"nowrap="true">
     	 ${ro.IN_MILEAGE }km
       </td>
      <td width="8%"nowrap="true"style="font-weight: bold; font-size:12px;">送修人电话
       </td>
       <td width="25%"nowrap="true">
       ${ro.DELIVERER_PHONE }
       </td>
     <td width="8%"nowrap="true"style="font-weight: bold; font-size:12px;">VIN号
       </td>
       <td width="25%"nowrap="true">
      	 ${ro.VIN }
       </td>
       </td>
    </tr>
    
     <tr style="height: 45px;">
      <td colspan="1"  nowrap="true" style="font-weight: bold; font-size:12px;">报修项目
      	
       </td>
       <td colspan="5" nowrap="true"  >
       ${ro.REMARKS }
       </td>
    </tr>

</table>
<table class="tab_print" width="750px">
	<tr >
	 <td width="10%"nowrap="true" style="border: none;">
       </td>
      <td width="10%"nowrap="true" style="border: none;">
      
       </td>
      <td width="10%"nowrap="true"style="border: none;">
       </td>
       <td width="20%"nowrap="true" style="border: none;">
       
       </td>
      <td width="50%"nowrap="true" style="border: none;">
       </td>
    </tr>
    <tr >
      <td colspan="6" style="text-align: center; font-size: 18px; font-weight: bold;">维修项目明细
       </td>
    </tr>
    <tr >
	 <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
		 序号
       </td>
      <td width="15%"nowrap="true" style="font-weight: bold; font-size:12px;">
     	 作业代码
       </td>
       <td width="45%"nowrap="true" style="font-weight: bold; font-size:12px;">
      	 作业名称
       </td>
      <td width="20%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	 维修种类
       </td>
        <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	 维修费
       </td>
    </tr>
    <c:forEach var="l" items="${labours}" varStatus="st">
	    <tr >
		 <td width="10%"nowrap="true">
		 	${st.index+1 }
	       </td>
	       <td width="15%"nowrap="true" style="font-weight: bold; font-size:12px;">
	     	${l.LABOUR_CODE}
	       </td>
	       <td width="45%"nowrap="true" style="font-weight: bold; font-size:12px;">
	      	${l.LABOUR_NAME}
	       </td>
	      <td width="20%"nowrap="true"style="font-weight: bold; font-size:12px;">
	     	<change:tcCode value="${l.PAY_TYPE}" showType="0"></change:tcCode>
	       </td>
	        <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
	        <c:if test="${l.PAY_TYPE==11801002}">
	        	0
	        </c:if>
	        <c:if test="${l.PAY_TYPE!=11801002}">
	        	${l.LABOUR_AMOUNT}<input name="labour_amount" type="hidden" value="${l.LABOUR_AMOUNT}"/>
	        </c:if>
	       </td>
	    </tr>
    </c:forEach>
</table>
<c:if test="${acc!=null}">
<table class="tab_print" width="750px">
	<tr >
	 <td width="10%"nowrap="true" style="border: none;">
       </td>
      <td width="20%"nowrap="true" style="border: none;">
      
       </td>
      <td width="20%"nowrap="true"style="border: none;">
       </td>
       <td width="20%"nowrap="true" style="border: none;">
       
       </td>
      <td width="20%"nowrap="true" style="border: none;">
       </td>
    </tr>
    <tr >
      <td colspan="6" style="text-align: center; font-size: 18px; font-weight: bold;">辅料
       </td>
    </tr>
    <tr >
	 <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
		 序号
       </td>
      <td width="10%"nowrap="true" style="font-weight: bold; font-size:12px;">
     	 辅料代码
       </td>
       <td  width="10%" nowrap="true" style="font-weight: bold; font-size:12px;">
      	 辅料名称
       </td>
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	 金额
       </td>
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	辅料关联件 
       </td>
    </tr>
    <c:forEach var="a" items="${acc}" varStatus="st">
	    <tr >
		 <td width="10%"nowrap="true">
		 	${st.index+1 }
	       </td>
	       
	      <td width="10%"nowrap="true" >
			${a.WORKHOUR_CODE }
	       </td>
	       <td width="10%" nowrap="true" >
	       	${a.WORKHOUR_NAME }
	       </td>
	      <td  width="10%"nowrap="true">
	      	<c:if test="${a.PAY_TYPE==11801002}">
	      		0
	        </c:if>
	        <c:if test="${a.PAY_TYPE!=11801002}">
		     	${a.PRICE}<input name="acc_amount" type="hidden" value="${a.PRICE}"/>
	        </c:if>
	       </td>
	       <td width="10%" nowrap="true"> 
	      	${a.MAIN_PART_CODE }
       </td> 
	    </tr>
    </c:forEach>
</table>
</c:if>
<table class="tab_print" width="750px">
	<tr >
	 <td width="10%"nowrap="true" style="border: none;">
       </td>
      <td width="10%"nowrap="true" style="border: none;">
      
       </td>
      <td width="10%"nowrap="true"style="border: none;">
       </td>
       <td width="20%"nowrap="true" style="border: none;">
       
       </td>
      <td width="20%"nowrap="true" style="border: none;">
       </td>
       <td width="10%"nowrap="true"style="border: none;">
       </td>
       <td width="10%"nowrap="true"style="border: none;">
       </td>
    </tr>
    <tr >
      <td colspan="6" style="text-align: center; font-size: 18px; font-weight: bold;">维修零部件明细
       </td>
    </tr>
    <tr >
	 <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
		 序号
       </td>
       <td width="20%"nowrap="true"style="font-weight: bold; font-size:12px;" >
      	 配件代码
       </td>
      <td width="20%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	 配件名称
       </td>
       <td width="10%"nowrap="true" style="font-weight: bold; font-size:12px;" >
     	数量
       </td>
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
      	维修种类
       </td>
       <td width="10%"nowrap="true" style="font-weight: bold; font-size:12px;">
     	 金额(元)
       </td>
    </tr>
    <c:forEach var="p" items="${parts}" varStatus="sta">
    <tr >
	 <td width="10%"nowrap="true">
	 	${sta.index+1 }
       </td>
     <td width="20%"nowrap="true"style="font-weight: bold; font-size:12px;" >
      	${p.PART_NO}
       </td>
      <td width="20%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	${p.PART_NAME}
       </td>
       <td width="10%"nowrap="true" style="font-weight: bold; font-size:12px;" >
     	${p.PART_QUANTITY}
       </td>
      <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	 <change:tcCode value="${p.PAY_TYPE}" showType="0"></change:tcCode>
       </td>
       <td width="10%"nowrap="true" style="font-weight: bold; font-size:12px;">
       <c:if test="${p.PAY_TYPE==11801002}">
	        0
        </c:if>
        <c:if test="${p.PAY_TYPE!=11801002}">
	     	${p.PART_COST_AMOUNT}<input name="part_cost_amount" type="hidden" value="${p.PART_COST_AMOUNT}"/>
        </c:if>
       </td>
    </tr>
     </c:forEach>
</table>
<table class="tab_print" width="750px">
	<tr >
       <td width="20%"nowrap="true" style="border: none;">
       </td>
       <td width="20%"nowrap="true"style="border: none;">
       </td>
       <td width="20%"nowrap="true"style="border: none;">
       </td>
       <td width="20%"nowrap="true"style="border: none;">
       </td>
       <td width="20%"nowrap="true" style="border: none;">
       </td>
       <td width="20%"nowrap="true"style="border: none;">
       </td>
       <td width="20%"nowrap="true"style="border: none;">
       </td>
       <td width="20%"nowrap="true"style="border: none;">
       </td>
       <td width="20%"nowrap="true"style="border: none;">
       </td>
    </tr>
     <tr >
      <td colspan="6" style="text-align: center; font-size: 18px; font-weight: bold;">外出维修
       </td>
    </tr>
	<tr >
	 <td width="25%" colspan="2"nowrap="true" style="font-weight: bold; font-size:12px;">
		 外出地点
       </td>
       <td width="25%"colspan="2" nowrap="true"style="font-weight: bold; font-size:12px;">
     	 外出里程（KM往返）
       </td>
       <td width="25%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	 付费方式
       </td>
       <td width="25%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	 外出费用(元)
       </td>
    </tr>
    <tr >
	 <td width="25%" colspan="2"nowrap="true" style="font-weight: bold; font-size:12px;">
		 ${outData.ADDRESS }
       </td>
       <td width="25%"colspan="2" nowrap="true"style="font-weight: bold; font-size:12px;">
       	${outData.MILEAGE }
       </td>
       <td width="25%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	${outData.PAY_TYPE }
       </td>
       <td width="25%"nowrap="true"style="font-weight: bold; font-size:12px;" id="out_amount">
        <c:if test="${outData.PAY_TYPE=='自费' }">
	       ${outData.OUT_AMOUNT }
        </c:if>
        <c:if test="${outData.PAY_TYPE=='索赔'}">
        	0
        </c:if>
       </td>
    </tr>
    </table>
    <table class="tab_print" width="750px">
    <tr>
     <td width="10%" nowrap="true" style="font-weight: bold; font-size:12px;">
		费用合计(元):
       </td>
       <td width="10%" nowrap="true"style="font-weight: bold; font-size:12px;" id="amountAll">
       		0
       </td>
       <td width="10%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	折扣(元):
       </td>
       <td width="15%"nowrap="true"style="font-weight: bold; font-size:12px;" id="discount_amount">
       	<c:if test="${ro.DISCOUNT_AMOUNT==null}">
       		0
       	</c:if>
       	<c:if test="${ro.DISCOUNT_AMOUNT!=null}">
       		${ro.DISCOUNT_AMOUNT}
       	</c:if>
       </td>
       <td width="15%"nowrap="true"style="font-weight: bold; font-size:12px;">
     	实收(元)：
       </td>
        <td width="15%"nowrap="true"style="font-weight: bold; font-size:12px;" id="real_amount">
     	0
       </td>
</tr>
<tr >
      <td nowrap="true" style="font-weight: bold;height:40px; font-size:12px;">
      	服务顾问签字：
       </td>
       <td nowrap="true" colspan="2">
       </td>
       <td nowrap="true"style="font-weight: bold;height:40px; font-size:12px;">
       	用户签字：
       </td>
       <td nowrap="true" colspan="2">
       </td>
    </tr>
</table>
<table class="tab_print" width="750px">
	
</table>
<table class="tab_print" width="750px">
	<tr >
	 <td width="5%"nowrap="true" style="border: none;">
       </td>
      <td width="5%"nowrap="true" style="border: none;">
      	年
       </td>
       <td width="2%"nowrap="true"style="border: none;">
       </td>
      <td width="5%"nowrap="true" style="border: none;">
      	月
       </td>
       <td width="2%"nowrap="true"style="border: none;">
       </td>
      <td width="5%"nowrap="true" style="border: none;">
      	日
       </td>
       
       <td width="52%"nowrap="true"style="border: none;">
       </td>
       <td width="5%"nowrap="true"style="border: none;">
       </td>
      <td width="5%"nowrap="true"style="border: none;" >
      	年
       </td>
       <td width="2%"nowrap="true"style="border: none;">
       </td>
      <td width="5%"nowrap="true"style="border: none;" >
      	月
       </td>
       <td width="2%"nowrap="true"style="border: none;">
       </td>
      <td width="5%"nowrap="true" style="border: none;">
      	日
       </td>
    </tr>
</table>
  <br>
    <p class="Noprint">
		<input type=button name= button_print class="normal_btn" value="打印" onclick ="printit();">
		<input type=button name= button_print class="long_btn" value="打印页面设置" onclick ="printsetup();">
		<input type=button name= button_print class="normal_btn" value="打印预览" onclick ="printpreview();">
    </p>
</center>
</form>
<script language="javascript">    
  
	function printsetup()
	{       
		wb.execwb(8,1);    // 打印页面设置 
	}    
	function printpreview()
	{    
		wb.execwb(7,1);   // 打印页面预览       
	}      
	function printit()    
	{    
		if(confirm('确定打印吗？'))
		{    
			wb.execwb(6,6)    
		}    
	} 
	  $$('.partCode').each(function(e){
		    var partCode = e.value ;
		    var firstPart = e.next().value;
		    var labourAmount = e.next(1).value
		    var labourHour = e.next(2).value
			if(partCode!=firstPart){
				if($('codeId').value==80081001){
					e.up().next(10).innerText = 0;
					e.up().next(9).innerText = 0;
				}else{
					e.up().next(9).innerText = 0;
					e.up().next(8).innerText = 0;
				}
				
			}
			else{
				if($('codeId').value==80081001){
					e.up().next(10).innerText = labourAmount;
					e.up().next(9).innerText = labourHour;
				}else{
					e.up().next(9).innerText = labourAmount;
					e.up().next(8).innerText = labourHour;
				}
			}

		  });
</script> 
</div>
<script type="text/javascript">
	$('topContainer').style.height=document.viewport.getHeight();
</script>
</body>

</html>


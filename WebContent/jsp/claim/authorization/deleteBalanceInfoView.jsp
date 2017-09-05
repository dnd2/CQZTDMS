<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib prefix="fmt" uri="/jstl/fmt" %>
<%@taglib uri="/jstl/change" prefix="change" %>


<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>开票管理</title>
	</head>
<<style>
</style>	
<body >
<center>
<form id="fm" name="fm">
<input id="yieldly" type="hidden" name="yieldly"  value="${mapCLAIM.YIELDLY}" />
<input id="dealerId" type="hidden" name="dealerId"  value="${dealer_id}" />
<input id="startDate" type="hidden" name="startDate"  value="${startDate}" />
<input id="endDate" type="hidden" name="endDate"  value="${endDate}" />
								<div style="font-size: 20px;">服务站结算费用汇总单</div>
			<div>
				<div>
				<table width="900px" class="tab_edit" id="tab1" >
  			<tr height="40px;">
  				<td colspan="4">服务站号: ${mapdel.DEALER_CODE}</td>
  				<td colspan="3">联系电话:${mapdel.PHONE}</td>
  				<td colspan="3" >结算单号：${mapCLAIM.REMARK}<input id="balanecNo" type="hidden" name="BALANCE_ODER"  value="${mapCLAIM.REMARK}" />  </td>
  			</tr>
  			<tr>
  				<td colspan="7" style="border-right: 0px;" align="left">重庆北汽幻速汽车销售有限公司 ：    </td>
  				<td colspan="3" style="border-left: 0px;" align="right">一式三联，两联随发票寄出</td>
  			</tr>
  			<tr>
<%--   				<td align="center"><a style="color:red" href="#" style="text-decoration: none;cursor: pointer;" onclick="AppprintAll('${mapCLAIM.REMARK}')" >[一键打印]</a></td> --%>
  				<td colspan="10" align="left" >&nbsp;&nbsp;&nbsp;&nbsp;我站的保养、索赔单据等，经贵公司审核，结算情况如下：</td>
  			</tr>
  			<tr>
  				<td>项目名称</td>
  				<td colspan="2">保养台次</td>
  				<td colspan="2">PDI台次</td>
  				<td colspan="2">一般索赔台次</td>
  				<td colspan="1" >活动台次</td>
  				<td colspan="2">合计台次</td>
  			</tr>
  			<tr>
  				<td>台次（台）</td>
  				<td colspan="2"><a href="#" style="text-decoration: none;cursor: pointer;" onclick="Appprint(10661002,'${mapApp.CLAIM_TYPE_02}','${mapCLAIM.REMARK}');" >${mapApp.CLAIM_TYPE_02}</a><span style="color: red;font-size: 10px;">(打印)</span> </td>
  				<td colspan="2"><a href="#" style="text-decoration: none;cursor: pointer;" onclick="Appprint(10661011,'${mapApp.CLAIM_TYPE_11}','${mapCLAIM.REMARK}');" >${mapApp.CLAIM_TYPE_11}</a><span style="color: red;font-size: 10px;">(打印)</span></td>
  				<td colspan="2"><a href="#" style="text-decoration: none;cursor: pointer;" onclick="Appprint(10661001,'${mapApp.CLAIM_TYPE_01+mapApp.SPECIL_TYPE_01}','${mapCLAIM.REMARK}');" >${mapApp.CLAIM_TYPE_01+InfoSecondTime+mapApp.SPECIL_TYPE_01}</a><span style="color: red;font-size: 10px;">(打印)</span></td>
  				<td colspan="1"> <a href="#" style="text-decoration: none;cursor: pointer;" onclick="Appprint(10661006,'${mapApp.CLAIM_TYPE_06}','${mapCLAIM.REMARK}');" >${mapApp.CLAIM_TYPE_06}</a><span style="color: red;font-size: 10px;">(打印)</span></td>
  				<td colspan="2">${mapApp.COUNTSUM+InfoSecondTime}</td>
  			</tr>
  			<tr>
  				<td >项目名称</td>
  				<td  colspan="2">保养费</td>
  				<td  colspan="2">PDI费</td>
  				<td  colspan="2">一般索赔费</td>
  				<td colspan="1" >服务活动费</td>
  				<td >旧件运费</td>
  				<td >正负激励费用</td>
<!--   				<td>合计费用</td> -->
  			</tr>
  			<tr>
  				<td>金额（元）</td>
  				<td colspan="2">${mapApp.CLAIM_TYPE_AMOUNT_02}</td>
  				<td colspan="2">${mapApp.CLAIM_TYPE_AMOUNT_11}</td>
  				<td colspan="2"><fmt:formatNumber value="${mapApp.CLAIM_TYPE_AMOUNT_01-mapCLAIM.APPEND_AMOUNT+InfoMoney.secondTimeMoney+mapCLAIM.OTHERACCOUNT}" pattern="0.00" ></fmt:formatNumber></td>
  				<td colspan="1">${mapApp.CLAIM_TYPE_AMOUNT_06}</td>
  				<td>${mapCLAIM.RETURN_AMOUNT}</td>
  				<td>${mapCLAIM.PLUS_MINUS_SUM }</td>
  			</tr>
  			
  			
  			
  			<tr>
  			   <td colspan="2">材料</td>
  				<td colspan="2"><fmt:formatNumber value="${InfoMoney.partAndAccMoney }" pattern="0.00" ></fmt:formatNumber></td>
  				<td>维修工时费</td>
  				<td><fmt:formatNumber value="${InfoMoney.ohersMoney }" pattern="0.00" ></fmt:formatNumber></td>
  				<td colspan="2">保养</td>
  				<td colspan="2"><fmt:formatNumber value="${InfoMoney.pdiAndKeepFitMoney }" pattern="0.00" ></fmt:formatNumber></td>
  				
  				
  			</tr>
  			
  			<tr>
  				<td align="center" colspan="10" id="fee_sum">
  					费用合计：${mapCLAIM.AMOUNT_SUM}
  				</td>
  			</tr>


  			<tr>
  				<td colspan="10" align="left">发票开票情况如下：</td>
  			</tr>
  			
  			<tr>
  				<td>开票日期</td>
  				<td>${date}</td>
  				<td colspan="2"></td>
  				<td colspan="2" ></td>
  				<td colspan="2" >
  				<c:if test="${STATUS > 0 }">
  				${invoiceName }
  				</c:if>
  				<input type="hidden" name="invoice_name1" value="" /> 
  				
  				<c:if test="${STATUS == 0 && !empty dealerId}">
	  				<c:if test="${!empty dealerId }">
		  				<select name="invoice_name" id="invoice_name">
		  				<option value="0">-请选择-</option>
		  				<c:forEach items="${voices}" var="voice">
		  					<c:if test="${invoiceValue==voice.id}">
		  						<option selected="selected" value="${voice.id}_${voice.taxRate}">${voice.invoiceName}</option>
		  					</c:if>
			  				<c:if test="${invoiceValue!=voice.id}">
		  						<option  value="${voice.id}_${voice.taxRate}">${voice.invoiceName}</option>
		  					</c:if>
		<%--   					<option value="${voice.id}_${voice.taxRate}">${voice.invoiceName}</option> --%>
		  				</c:forEach>
		  				</select>
	  				</c:if>
	  				
	  				<c:if test="${empty dealerId }">
	  				${invoiceName }
	  				</c:if>
  				
  				</c:if>
  				</td>
  				<td colspan="2" id="ticket_fee" >开票金额:${sum } </td>
  				<input type="hidden" name="CLAIM_AMOUNT_SUM" id="claim_amount"/>
  			</tr>
  			
  			
  			
  			<tr>
  				<td>
  				<c:if test="${STATUS == 0 }">
  				<c:if test="${!empty dealerId }">
  				<a href="#"><button onclick="addLine();">新增</button></a>
  				</c:if>
  				</c:if>
  				</td>
  				<td>序号</td>
  				<td colspan="2">发票批号</td>
  				<td colspan="2">发票号码</td>
  				<td>金额</td>
  				<td>税额</td>
  				<td>合计</td>
  				<td>备注</td>
  			</tr>
  			<c:if test="${STATUS == 0 }">
  			<c:if test="${empty dealerId }">
  			<c:forEach items="${list}" var="po" varStatus="status">
  				<tr>
  				<td></td>
  				<td>${status.index+1}</td>
  				<td style="border-right: 0px;">${po.labourReceipt }</td>
  				<td style="border-left: 0px;"></td>
  				<td style="border-right: 0px;">${po.partReceipt }</td>
  				<td style="border-left: 0px;"> </td>
  				<td >${po.amountOfMoney }</td>
  				<td>${po.taxRateMoney }</td>
  				<td>${po.amountSum }</td>
  				<td>${po.remark }</td>
  			</tr>
  			
  			
  			</c:forEach>
  			
  			</c:if>
  			<c:if test="${!empty dealerId }">
  			<c:forEach items="${list}" var="po" varStatus="status">
  				<tr>
  				<td><a href="#" onclick="remove(${po.id},this);">[删除]</a></td>
  				<td>${status.index+1}</td>
  				<td ><input style="width: 130px;" type="text" name= "LABOUR_RECEIPT" value="${po.labourReceipt }" id="LABOUR_RECEIPT${status.index+1}" datatype="0,is_null,10" /> </td>
  				<td style="bo"></td>
  				<td ><input style="width: 130px;" type="text" name= "PART_RECEIPT" value="${po.partReceipt }" id="PART_RECEIPT${status.index+1}" datatype="0,is_null,8" /></td>
  				<td></td>
  				<td ><input style="width: 70px;"   name= "AMOUNT_OF_MONEY" value="${po.amountOfMoney }" id="AMOUNT_OF_MONEY${status.index+1}" onChange="checkNum(this);getSum1(${status.index+1});" /></td>
  				<td><input style="width: 70px;"   name= "TAX_RATE_MONEY" value="${po.taxRateMoney }" id="TAX_RATE_MONEY${status.index+1}"  onChange="checkNum(this);getSum2(${status.index+1});"/></td>
  				<td><input style="width: 70px;" name= "AMOUNT_SUM" value="${po.amountSum }"  onClick="countAll(${status.index+1});" id="sum${status.index+1}"/></td>
  				<td><input style="width: 70px;" name= "REMARK" value="${po.remark }" onClick="countAll(${status.index+1});" id="sum${status.index+1}" /></td>
  				<input type="hidden" name="ID"  value="${po.id}"
  			</tr>
  			</c:forEach>
  			</c:if>
  			
  			</c:if>
  			
  			
  			<c:if test="${STATUS > 0 }">
  			
  			<c:forEach items="${list}" var="po" varStatus="status">
  				<tr>
  				<td></td>
  				<td>${status.index+1}</td>
  				<td style="border-right: 0px;">${po.labourReceipt }</td>
  				<td style="border-left: 0px;"></td>
  				<td style="border-right: 0px;">${po.partReceipt }</td>
  				<td style="border-left: 0px;"> </td>
  				<td >${po.amountOfMoney }</td>
  				<td>${po.taxRateMoney }</td>
  				<td>${po.amountSum }</td>
  				<td>${po.remark }</td>
  			</tr>
  			
  			
  			</c:forEach>
  			</c:if>
  			
  			
  			<tr>
	  			<td align="left" colspan="6">价税合计：</td>
	  			<td id="amountOfMoneySum" name="amountOfMoneySum">${ amountOfMoneySum}</td>
	  			<td id="taxRateMoneySum" name="taxRateMoneySum">${taxRateMoneySum }</td>
	  			<td id="amountSumSum" name="amountSumSum">${amountSumSum }</td>
	  			<td></td>
	  			<td></td>
  			</tr>
  			
  			
  			
  			
  			
  			<tr>
  				<td colspan="2">服务商索赔员：</td>
  				<td></td>
  				<td colspan="3">服务商财务:</td>
  				<td></td>
  				<td colspan="2">服务经理：</td>
  				<td></td>
  			</tr>
  			
  		
  			
  			
  			
  			<tr  style="height: 86px;">
  				<td align="left" valign="bottom" style="border: 0px;">备注：</td>
  				<td colspan="6" align="right" valign="top" style="border: 0px;"></td>
  				<td colspan="3" align="center" valign="top" style="border: 0px;">
  				</br>
  				</br>
  				</br>
  				单位名称：${mapdel.DEALER_NAME}</br>
  				</br>
  				(服务站盖发票专用章)
  				<div style="height: 60px;"></div>
  				&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp; 月&nbsp;&nbsp;&nbsp;&nbsp; 日
  				</td>
  			</tr>
  			<tr>
  				<td colspan="2">北汽幻速签字确认：</td>
  				<td colspan="5">&nbsp;</td>
  				<td colspan="3">&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日</td>
  			</tr>
  			<tr>
  				<td>购</td>
  				<td colspan="2">纳税人识别号</td>
  				<td colspan="7">500117083059795</td>
  			</tr>
  			<tr>
  				<td>货</td>
  				<td colspan="2">地 址 电 话</td>
  				<td colspan="7">重庆市合川区土场镇三口村 023-42661188</td>
  			</tr>
  			<tr>
  				<td>单</td>
  				<td colspan="2">开   户   行</td>
  				<td colspan="7">中信银行重庆九龙坡支行</td>
  			</tr>
  			<tr>
  				<td>位</td>
  				<td colspan="2">账     号</td>
  				<td colspan="7">7422410182600052664</td>
  			</tr>
  			<tr>
  				<td colspan="10"></td>
  			</tr>
  			<tr>
  				<td>收</td>
  				<td colspan="2">单 位 名 称</td>
  				<td colspan="7">重庆北汽幻速汽车销售有限公司</td>
  			</tr>
  			<tr>
  				<td>件</td>
  				<td colspan="2">收件人姓名<input type="hidden" id="STATUS" name="STATUS" />  </td>
  				<td colspan="7">重庆北汽幻速汽车销售有限公司索赔管理部</td>
  			</tr>
  			<tr>
  				<td>单</td>
  				<td colspan="2">地址、电话</td>
  				<td colspan="7">重庆市北碚区土场镇三口村北汽银翔（研发中心二楼） 023-42668160</td>
  			</tr>
  			<tr>
  				<td>位</td>
  				<td colspan="2">邮 政 编 码</td>
  				<td colspan="7">401520</td>
  			</tr>
  		</table>
			</div>
			</center>
			<table class="table_edit">
			<tr>
				<td colspan="6" align="center">
				<div id="buttontype">
				<c:if test="${STATUS == 0 && !empty dealerId }">
				    <input type="button"  class="normal_btn" name="backBtn" onclick="commit(0)" value="保存"/>
				    <input type="button" class="normal_btn" name="backBtn" onclick="commit(1)" value="上报"/>
				 </c:if>   
					<input type="button" class="normal_btn" name="backBtn" onclick="javascript:deleteBalanceConfirm()" value="撤销结算"/> 
					<input type="button" class="normal_btn" name="backBtn" onclick="javascript:history.go(-1)" value="返回"/>
				</div>
				</td>
			</tr>
		</table>
			
</form>
</center>
<script type="text/javascript">


var size="${size}";
var tdLength=1;
var rowNum=12+size*1;
if(size>0){
	tdLength=size*1+1;
}
var val;
function addLine(){
 	var ta=document.getElementById("tab1");
	var insertRow=ta.insertRow(rowNum);
	insertRow.insertCell(0);
	insertRow.insertCell(1);
	insertRow.insertCell(2);
	insertRow.insertCell(3);
	insertRow.insertCell(4);
	insertRow.insertCell(5);
	insertRow.insertCell(6);
	insertRow.insertCell(7);
	insertRow.insertCell(8);
	insertRow.insertCell(9);
	insertRow.cells[2].setAttribute("colspan",2);
	insertRow.cells[0].innerHTML="<td><input type='hidden' name='ID' value=''></td>";
	insertRow.cells[1].innerHTML=tdLength;
	insertRow.cells[2].innerHTML='<td colspan="2"><input style="width: 130px;" type="text" name= "LABOUR_RECEIPT" value="" id="LABOUR_RECEIPT'+tdLength+'" datatype="0,is_null,10" /> </td>';
	insertRow.cells[4].innerHTML='<td colspan="2"><input  type="text" name= "PART_RECEIPT" value="" id="PART_RECEIPT'+tdLength+'" datatype="0,is_null,8" /></td>';
	insertRow.cells[6].innerHTML='<td colspan="1"><input style="width: 60px;"  type="text" name= "AMOUNT_OF_MONEY" value="" id="AMOUNT_OF_MONEY'+tdLength+'" onChange="checkNum(this);" /></td>';
	insertRow.cells[7].innerHTML='<td><input style="width: 60px;" type="text" name= "TAX_RATE_MONEY" value="" id="TAX_RATE_MONEY'+tdLength+'"  onChange="checkNum(this);" /></td>';
	insertRow.cells[8].innerHTML='<td><input style="width: 60px;" readonly="readonly" type="text" name= "AMOUNT_SUM" value="" onClick="countAll('+tdLength+');" id="sum'+tdLength+'"  /></td>';
	insertRow.cells[9].innerHTML='<td><input style="width: 60px;" type="text" name= "REMARK" value="" id="remark"  /></td>';
	rowNum++;
	tdLength++;
}	
	function countAll(tdLength){
		var AMOUNT_OF_MONEY=document.getElementById("AMOUNT_OF_MONEY"+tdLength).value  ;
		var TAX_RATE_MONEY=document.getElementById("TAX_RATE_MONEY"+tdLength).value;
		var sumMoney= AMOUNT_OF_MONEY*1+TAX_RATE_MONEY*1 ;
		document.getElementById("sum"+tdLength).value=toDecimal2(sumMoney);
		
	}

	 function toDecimal2(x) {  
         var f = parseFloat(x);  
         if (isNaN(f)) {  
             return false;  
         }  
         var f = Math.round(x*100)/100;  
         var s = f.toString();  
         var rs = s.indexOf('.');  
         if (rs < 0) {  
             rs = s.length;  
             s += '.';  
         }  
         while (s.length <= rs + 2) {  
             s += '0';  
         }  
         return s;  
     }  
	
	
	
	function checkNum(num){
		var reg =/^\d+(\.\d+)?$/;
	//	var reg = /^\d+$/;
	    if(!num.value.match(reg)){
			MyAlert("请输入数字");
			num.value="";
	    }
	}
   
   
   function commit(val1)
   {
	   var valueSum=0;
	   if(document.getElementById("invoice_name").value==0){
		   MyAlert("请先选择税率类型");
		   return false;
	   }
	   for(var i=1;i<tdLength;i++){
		   var money_sum=document.getElementById("sum"+i).value;
		   MyAlert(money_sum);
		   valueSum=valueSum+money_sum*1;
	   }
	   var ticketFee= document.getElementById("ticket_fee").innerHTML.replace("开票金额:","");
	   if(valueSum!=ticketFee){
		   MyAlert("合计金额必须与开票金额一致");
		   return false;
	   }else{
		    var fm = document.getElementById("fm");
		    if(!submitForm('fm')) {//表单校验
				return false;
		    }
			val=val1;
	        document.getElementById('STATUS').value = val;
	        var balanecNo=$('balanecNo').value;
			fm.action = '<%=request.getContextPath()%>/claim/authorization/BalanceMain/commit_PAYMENT.do';
			fm.submit();
	   }
// 		val=val1;
   	//	var balanecNo=$('balanecNo').value;
<%-- 	//	sendAjax('<%=contextPath%>/claim/autorization/BalanceMain/checkMoneyByBalanceNo.json?balanecNo='+balanecNo,backCheck,'fm'); --%>
   };
   
   
   
   function backCheck(json){
		if(json.succ=="-1"){
			MyAlert("提示：改结算单的索赔单有人改动了数据，请联系管理员查看纰漏！");
		}else{
			var fm = document.getElementById("fm");
			   if(!submitForm('fm')) {//表单校验
					return false;
			    }
	            document.getElementById('STATUS').value = val;
				fm.action = '<%=request.getContextPath()%>/claim/authorization/BalanceMain/commit_PAYMENT.do';
				fm.submit();
		};
	}
  function Appprint(val,count,BALANCE_ODER)
  {
     if(count != 0)
     {
        window.open('<%=contextPath%>/claim/application/DealerNewKp/Appprint.do?val='+val+'&BALANCE_ODER='+BALANCE_ODER,"开票通知单索赔数据打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
     }
  }
  
  function AppprintAll(BALANCE_ODER) {
	   window.open('<%=contextPath%>/claim/application/DealerNewKp/AppprintAll.do?BALANCE_ODER='+BALANCE_ODER,"开票通知单索赔数据打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
  };
  window.onload=function (){
	   document.getElementById("invoice_name").onchange=function(){
		   var invoice_value=this.value;
		   var fee=document.getElementById("fee_sum").innerHTML.replace("费用合计：","");
		   var realFee=toDecimal2(fee*invoice_value*0.01);
		   document.getElementById("ticket_fee").innerHTML="开票金额:"+realFee;
		   document.getElementById("claim_amount").value=realFee;
	   };
  };
   //打印二次入库
   function AppprintSencondByNo(count,BALANCE_ODER){
	   if(count!=0){
	        window.open('<%=contextPath%>/claim/application/DealerNewKp/appprintSencondByNo.do?BALANCE_ODER='+BALANCE_ODER,"开票通知单索赔数据打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	   }
   }
   function deleteBalanceConfirm(){
	   MyConfirm("是否确认撤销结算？",deleteBalance);
   }
   

   function deleteBalance()
   {
	   		var balanecNo=$('balanecNo').value;
			sendAjax('<%=contextPath%>/claim/authorization/BalanceMain/deleteFinancialInfoView.json?remark='+balanecNo,deleteBalanceBack,'fm');
   }
   
   function deleteBalanceBack(json){
		if(json.succ=="1"){
			MyAlert("撤销结算成功！");
			window.history.go(-1);
		}else{
			MyAlert("提示："+json.msg);
		}
	}
</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@page import="java.util.*"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件退赔单</title>
<% 
   String contextPath = request.getContextPath();
List<Map<String,Object>> list  = (List)request.getAttribute("listBean");
%>
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;索赔旧件退赔单信息</div>
<c:choose>
	<c:when test="${!empty listBean}">
 <form method="post" name ="fm" id="fm">
 <input type="hidden" name="outNo" id="outNo" value="<%=list.get(0).get("OUT_NO") %>"/>
 <input type="hidden" name="SUPPLY_CODE" id="SUPPLY_CODE" value="<%=list.get(0).get("SUPPLY_CODE") %>"/>
 <input type="hidden" name="SUPPLY_NAME" id="SUPPLY_NAME" value="<%=list.get(0).get("SUPPLY_NAME") %>"/>
  <input type="hidden" name="cType" id="cType" value="${cType }"/>
  <table class="table_list" width="100%">
    <tr >
      <td align="center" class="tdp"colspan="14" style="font-size: 25px"><strong>索赔件退赔单 </strong></td>
     </tr>
     <tr >
       <td align="right"class="tdp" colspan="12"><strong>退赔单号：<%=list.get(0).get("RANGE_NO") %> </strong></td>
     </tr>
     <tr >
       <td align="center" class="tdp" colspan="2">供应商代码</td>
       <td align="center" class="tdp" colspan="3"><%=list.get(0).get("SUPPLY_CODE") %></td>
       <td align="center" class="tdp" colspan="2">供应商名称</td>
       <td align="center" class="tdp" colspan="5"><%=list.get(0).get("SUPPLY_NAME") %></td>
     </tr>
        <tr >
            <td align="center" class="tdp">序号</td>
            <td align="center" class="tdp">零部件代码</td>
            <td align="center" class="tdp">零部件名称</td>
            <td align="center" class="tdp">单位</td>
            <td align="center" class="tdp">数量</td>
            <td align="center" class="tdp">工时金额</td>
            <td align="center" class="tdp">关联损失</td>
            <td align="center" class="tdp">外出费用</td>
            <td align="center" class="tdp">材料合计</td>
            <td align="center" class="tdp">小计</td>
            <td align="center" class="tdp">打印数量</td>
            <td align="center" class="tdp">备注</td>
       </tr>
       <c:set var="pageSize"  value="10000" />
     <c:forEach var="dList" items="${listBean}" varStatus="status">
	<tr style='${(status.count%pageSize==0) ? "page-break-after:always;":""}'  align="center">
	  	<td class="tdp" align="center" >${status.index+1}</td> 
	    <td class="tdp" align="center"  >${dList.PART_CODE} 
	    <input id="OUT_PART_CODE${status.index}" name="OUT_PART_CODE" value="${dList.PART_CODE}" type="hidden"/>
	     <input id="id${status.index}" name="id" value="${dList.ID}" type="hidden"/>
	     <input id="PART_AMOUNTS${status.index}"  name="PART_AMOUNTS"  value="${dList.PART_AMOUNT}" type="hidden"/>
	     <input id="OUT_TYPE${status.index}"  name="OUT_TYPE"  value="${dList.OUT_TYPE}" type="hidden"/>
	    </td>
	     <td class="tdp" align="center"  >${dList.PART_NAME} 
	     <input id="OUT_PART_NAME${status.index}" name="OUT_PART_NAME" value="${dList.PART_NAME}" type="hidden"/> 
	     </td>
	     
	  	<td class="tdp" align="center"  >  ${dList.PART_UNIT} 
	  	 <input id="UNIT${status.index}" name="UNIT" value="${dList.PART_UNIT}" type="hidden"/>  </td>
	  	 
	    <td class="tdp" align="center" >${dList.PART_QUANTITY}
	    	 <input id="NUM${status.index}" name="NUM" value="${dList.PART_QUANTITY}" type="hidden"/>  </td>
	    	 
	    <td class="tdp" align="center">
	    <input type="text" value="${dList.LABOUR_AMOUNT}" readOnly id="LABOUR_AMOUNT_CHG${status.index}" name="LABOUR_AMOUNT_CHG" />
	    <input id="labourCode${status.index}" name="labourCode" type="hidden" /> 
	     <input id="LABOUR_AMOUNT${status.index}" name="LABOUR_AMOUNT" value="${dList.LABOUR_AMOUNT}" type="hidden"/> </td> 
	     
	    <td class="tdp" align="center" >${dList.RELATED_LOSSES} 
	     <input id="RELATED_LOSSES${status.index}" name="RELATED_LOSSES" value="${dList.RELATED_LOSSES}" type="hidden"/> </td>
	     
	     <td class="tdp" align="center" >${dList.OUT_AMOUNT}
	      <input id="OUT_AMOUNT${status.index}" name="OUT_AMOUNT" value="${dList.OUT_AMOUNT}" type="hidden"/> </td>
	      
	      
	      <c:if test="${dList.OUT_TYPE=='0'&&cType==1}">
	    <td class="tdp" align="center"  >  <input id="PART_AMOUNT${status.index}" class="short_txt" name="PART_AMOUNT" readonly="readonly" value="${dList.PRINT_PART}" type="text"/></td>
	    <td class="tdp"  align="center"  ><input id="SMALL_AMOUNT${status.index}" name="SMALL_AMOUNT" class="short_txt"value="${dList.SMALL_AMOUNT}" readonly="readonly" type="text"/> </td>
	   <c:if test="${dList.PART_QUANTITY ==dList.PRINT_NUM}">
	    <td class="tdp" align="center" > <input id="printNum${status.index}" readOnly name="printNum" class="short_txt" onblur="checkNum(this.value,${status.index })" value="${dList.PART_QUANTITY}"  type="text"/>
	    	<input type="button" value='修改打印数量' id="modifyNum${status.index}" name="modifyNum" onclick='openLabour(${status.index},${dList.PRINT_NUM},"${dList.PART_CODE}");' />
	    <span style="color: red">*</span></td> 
	   </c:if>
	    <c:if test="${dList.PART_QUANTITY != dList.PRINT_NUM}">
	    <td class="tdp" align="center" > <input id="printNum${status.index}" readOnly name="printNum" class="short_txt" onblur="checkNum(this.value,${status.index })" value="${dList.PRINT_NUM}"  type="text"/>
	    	<input type="button" value='修改打印数量' id="modifyNum${status.index}" name="modifyNum" onclick='openLabour(${status.index},${dList.PRINT_NUM},"${dList.PART_CODE}");' />
	    <span style="color: red">*</span></td> 
	   </c:if>
	    <td align="center" class="tdp" >
	    	<input id="remark${status.index}" class="short_txt" name="remark"  value="${dList.REMARK}" type="text"/>
	    </td>
	   </c:if>
	   
	   
	    <c:if test="${dList.OUT_TYPE =='2'||dList.OUT_TYPE =='1' || cType==0||cType==2}">
	    <td class="tdp" align="center"  > <input id="PART_AMOUNT${status.index}" class="short_txt" name="PART_AMOUNT"  readonly="readonly" value="${dList.PRINT_PART}" type="text"/></td>
	    <td class="tdp"  align="center" ><input id="SMALL_AMOUNT${status.index}" name="SMALL_AMOUNT" class="short_txt"value="${dList.SMALL_AMOUNT}" readonly="readonly" type="text"/> </td>
	   <c:if test="${dList.PART_QUANTITY ==dList.PRINT_NUM}">
	    <td class="tdp" align="center" > <input id="printNum${status.index}" name="printNum" class="short_txt" readonly="readonly" value="${dList.PART_QUANTITY}"  type="text"/></td> 
	   </c:if>
	    <c:if test="${dList.PART_QUANTITY != dList.PRINT_NUM}">
	    <td class="tdp" align="center" > <input id="printNum${status.index}" name="printNum" class="short_txt" readonly="readonly" value="${dList.PRINT_NUM}"  type="text"/></td> 
	   </c:if>
	   <td align="center" class="tdp" >
	    	<input id="remark${status.index}" class="short_txt" name="remark" readonly="readonly" value="${dList.REMARK}" type="text"/>
	    </td>
	   </c:if>
	    
	 </tr>
	  <c:set var="outType"  value="${dList.OUT_TYPE}" />
	  
	  
	  
	    <c:set var="num1"  value="${dList.PART_QUANTITY+num1}" />
        <c:set var="labour"  value="${dList.LABOUR_AMOUNT+labour}" />
        <c:set var="related"  value="${dList.RELATED_LOSSES+related}" />
        <c:set var="out"  value="${dList.OUT_AMOUNT+out}" />
         <c:set var="part"  value="${dList.PART_AMOUNT+part}" />
        <c:set var="small"  value="${dList.SMALL_AMOUNT+small}" />
        <c:set var="print"  value="${dList.PRINT_NUM+print}" />
  </c:forEach>
   <tr >
       		<td align="center" class="tdp" colspan="4">合计</td>
            <td align="center" id="num1" class="tdp">${num1 }</td>
            <td align="center" id="labour" name="labour"  class="tdp">${labour }</td>
            <td align="center" id="related" class="tdp">${related }</td>
            <td align="center" id="out" class="tdp">${out }</td>
            <td align="center" id="part" class="tdp">${part }</td>
            <td align="center" id="small" class="tdp">${small }</td>
            <td align="center" id="print" class="tdp">${print }</td>
            <td align="center" class="tdp" colspan="1"></td>
     </tr>
     </table>
     <table class="table_list">
      <tr > 
       <td height="12" align="center">
         <c:if test="${outType==0&&cType==1}">
        <input type="button" id="save_btn" onclick="save();" class="normal_btn" style="width=8%" value="保存"/>
       </c:if>
        <c:if test="${outType==1&&cType==2}">
        <input type="button" id="save_btn" onclick="save();" class="normal_btn" style="width=8%" value="确认"/>
       </c:if>
          <input type="button"  onclick="history.back();" class="normal_btn" style="width=8%" value="返回"/>
       </td>
      </tr>
    </table>  
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
</c:when>
<c:otherwise>
	<div align="center">
		<font color="red" size="10">没有退配单信息</font>
		<br />
    	<input type="button"  onclick="history.back();" class="normal_btn" style="width=8%" value="返回"/>
	</div>
</c:otherwise>
</c:choose>
<br />
<script type="text/javascript">
function setMainTime(idex,newPrintNum,addMoney,labourCode){
	var oldLabourAmount = document.getElementById('LABOUR_AMOUNT'+idex).value;
	document.getElementById('printNum'+idex).value = newPrintNum;
	document.getElementById('LABOUR_AMOUNT_CHG'+idex).value = parseFloat(oldLabourAmount)+parseFloat(addMoney);
	document.getElementById('labourCode'+idex).value = labourCode;
	var sum = 0;
	//document.getElementById('labourCode'+idex).value = labourCode;
	var value =document.getElementById('labourCode'+idex).value;
	
	var LABOUR_AMOUNT_CHG=document.getElementsByName("LABOUR_AMOUNT_CHG");
	for (var i = 0; i < LABOUR_AMOUNT_CHG.length; i++) {
		sum+=parseFloat(LABOUR_AMOUNT_CHG[i].value);
	}
	document.getElementById("labour").innerHTML=sum;
 	
	var printSum=0;
	var printNum=document.getElementsByName("printNum");
	for (var i = 0; i < printNum.length; i++) {
		printSum+=parseFloat(printNum[i].value);
	}
	document.getElementById("print").innerHTML=printSum;
	
	
	//MyAlert(sum);
//	sum += sumparseFloat(value);
	checkNum(newPrintNum,idex,addMoney);
}

function checkNum(num,id){
	var reg = /^\d+$/;
	if (num!=null&&num!=""){
		if(!reg.test(num)){
			MyAlert("打印数量请输入正整数!");
			$('printNum'+id).value=$('NUM'+id).value;
			$('PART_AMOUNT'+id).value= $('PART_AMOUNTS'+id).value;
			sumPrice($('PART_AMOUNTS'+id).value,id);
			return false;
		}
		if(parseInt(num)<parseInt($('NUM'+id).value)){
			$('printNum'+id).value=$('NUM'+id).value;
			MyAlert("打印数量不能小于出库数量!");
			$('PART_AMOUNT'+id).value= $('PART_AMOUNTS'+id).value;
			sumPrice($('PART_AMOUNTS'+id).value,id);
			return false;
			}
		var partPrice = $('PART_AMOUNTS'+id).value;
		var newPrice=0;
		if(parseInt($('NUM'+id).value)==0){
			newPrice=0;
			}else{
				newPrice = (parseInt(num)/parseInt($('NUM'+id).value));
				}
		newPrice = parseFloat(newPrice)*parseFloat(partPrice);
		$('PART_AMOUNT'+id).value= parseFloat(newPrice).toFixed(2);
		sumPrice(parseFloat(newPrice).toFixed(2),id);
	}else{
		MyAlert("打印数量不能为空!");
		return false;
	}
	
}
function openLabour(idex,print_num,partCode){
	OpenHtmlWindow(g_webAppName + '/claim/oldPart/ClaimOldPartOutStorageManager/selectMainTimeForward.do?idex='+idex+"&printNum="+print_num+"&partCode="+partCode,1000,500);
}
function sumPrice(price,id){
	var reg =/^(?:0|[1-9]\d*)+(?:\.\d{0,2})?$/;
	if(!reg.test(price)){//判断输入的索赔价是否是数字且最多2位小数
		MyAlert("请输入正确的金额格式!");
		$('PART_AMOUNT'+id).value="";
	}
	smmAll();
}

function smmAll(){
//通过循环得到总计
	//取得材料费合计
	var partAmount = document.getElementsByName("PART_AMOUNT");
	var smallAmount = document.getElementsByName("SMALL_AMOUNT");
	
	var labour = document.getElementsByName("LABOUR_AMOUNT_CHG");
	var related = document.getElementsByName("RELATED_LOSSES");
	var out = document.getElementsByName("OUT_AMOUNT");
	var pa=0;
	var sa=0;
	for(var i=0;i<partAmount.length;i++){//材料费
	var sum=0.0;
		if(partAmount[i].value!=null&&partAmount[i].value!=""){
		pa += parseFloat(partAmount[i].value);
			sum = parseFloat(labour[i].value)+parseFloat(related[i].value)+parseFloat(out[i].value)+parseFloat(partAmount[i].value);
			$('SMALL_AMOUNT'+i).value=parseFloat(sum).toFixed(2);
		}else{
			pa +=0.0;
			$('SMALL_AMOUNT'+i).value='';
		}
		
	}
	if(null!=document.getElementById("part") && "undefined"!= typeof (document.getElementById("part")))
		document.getElementById("part").innerHTML= ''+parseFloat(pa).toFixed(2)+'';
	
	for(var i=0;i<smallAmount.length;i++){//税额
		if(smallAmount[i].value!=null&&smallAmount[i].value!=""){
		sa =parseFloat(sa)+parseFloat(smallAmount[i].value); 
		}else{
		sa =parseFloat(sa)+0.0;
		}
	}
	if(null!=document.getElementById("small") && "undefined"!= typeof (document.getElementById("small")))
		document.getElementById("small").innerHTML=''+parseFloat(sa).toFixed(2)+'';
}

function save(){
	var partAmount = document.getElementsByName("PART_AMOUNT");
	for(var i=0;i<partAmount.length;i++){//材料费
		if(partAmount[i].value==null || partAmount[i].value==""){
			MyAlert("请输入材料费!");
			return ;
		}
	}
	var printNum = document.getElementsByName("printNum");
	for(var i=0;i<printNum.length;i++){//材料费
		if(printNum[i].value==null || printNum[i].value==""){
			MyAlert("请输入打印数量!");
			return ;
		}
	}
	var cType=$('cType').value;
	if(cType==2){
		MyConfirm("是否确认？",outOfStore,[]);
		}else{
			MyConfirm("是否保存？",outOfStore,[]);
			}
	
}
function outOfStore(){
 var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/saveRenge.json?type=1";
 	 makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
}
 function afterCall(json){	
   	var retCode=json.updateResult;
      if(retCode=="updateSuccess"){
    	    MyAlert("操作成功!");
    	    if(json.cType==2){
    	    	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/rangeFinancialPer.do";
    	    }else{
    	    	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryListPage.do";
    	    }
    	    
			fm.submit();
      }else{
    	    MyAlert("操作失败!");
     }
  }
 
</script>
</body>
</html>

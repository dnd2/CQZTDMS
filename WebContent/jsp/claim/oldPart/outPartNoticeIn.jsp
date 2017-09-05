<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@page import="com.infodms.dms.common.Constant" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件审批入库</title>
<% 
   String contextPath = request.getContextPath();
%>
</head>
<body onload="init();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;通知单生成</div>
 <form method="post" name ="fm" id="fm">
  <input type="hidden" name="yieldly" id="yieldly" value="${yieldly }"/>
    <input type="hidden" name="code" id="code" value="${code }"/>
    <input type="hidden" name="outNo" id="outNo" value="${outNo }"/>
    <input type="hidden" name="listSize" id="listSize" value="${size}"/>
  <table class="table_edit">
  <c:if test="${yieldly == 95411001}">
    <tr bgcolor="F3F4F8">
      <td align="right">通知单标题：</td>
       <td>江西昌河汽车质量保证索赔通知单(昌河)
        <input type="hidden" name="noticeTittle" id="noticeTittle" value="江西昌河汽车质量保证索赔通知单(昌河)"/>
        </td>
       <td align="right">通知单编号：</td>
       <td>
          ${baseBean.noticeNo}
        <input type="hidden" name="noticeNo" id="noticeNo" value=" ${baseBean.noticeNo}"/>
       </td>
     </tr>
     <tr></tr>
     <tr bgcolor="F3F4F8">
       <td align="right">被索赔单位：</td>
       <td>
 			${baseBean.noticeCompany }
 			 <input type="hidden" name="noticeTo" id="noticeTo" value="${baseBean.noticeCompany }"/>
		</td>
       <td align="right">联系方式：</td>
       <td>
       ${baseBean.noticeCompanyByTel }
       <input type="hidden" name="noticeTel" id="noticeTel" value="${baseBean.noticeCompanyByTel }"/>
       </td>
     </tr>
        <tr bgcolor="F3F4F8">
       <td align="right">索赔单位：</td>
       <td colspan="3">
 			江西昌河汽车有限责任公司
 			  <input type="hidden" name="noticeBy" id="noticeBy" value="江西昌河汽车有限责任公司"/>
		</td>
     </tr>
     
      <tr bgcolor="F3F4F8">
       <td align="right">开户行：</td>
       <td>
 			江西省景德镇市中国工商银行昌河支行
 			 <input type="hidden" name="noticeBank" id="noticeBank" value="江西省景德镇市中国工商银行昌河支行"/>
		</td>
       <td align="right">银行账号：</td>
       <td>
      1503214109022102903
       <input type="hidden" name="noticeAcount" id="noticeAcount" value="1503214109022102903"/>
       </td>
     </tr>
     </c:if>
      <c:if test="${yieldly == 95411002}">
    <tr bgcolor="F3F4F8">
      <td align="right">通知单标题：</td>
       <td>江西昌河汽车质量保证索赔通知单(东安)
        <input type="hidden" name="noticeTittle" id="noticeTittle" value="江西昌河汽车质量保证索赔通知单(东安)"/>
        </td>
       <td align="right">通知单编号：</td>
       <td>
          ${baseBean.noticeNo}
        <input type="hidden" name="noticeNo" id="noticeNo" value=" ${baseBean.noticeNo}"/>
       </td>
     </tr>
     <tr></tr>
     <tr bgcolor="F3F4F8">
       <td align="right">被索赔单位：</td>
       <td>
 			${baseBean.noticeCompany }
 			 <input type="hidden" name="noticeTo" id="noticeTo" value="${baseBean.noticeCompany }"/>
		</td>
       <td align="right">联系方式：</td>
       <td>
      ${baseBean.noticeCompanyByTel }
       <input type="hidden" name="noticeTel" id="noticeTel" value="${baseBean.noticeCompanyByTel }"/>
       </td>
     </tr>
        <tr bgcolor="F3F4F8">
       <td align="right">索赔单位：</td>
       <td>
 			江西昌河汽车有限责任公司(东安),待确定具体名称
 			  <input type="hidden" name="noticeBy" id="noticeBy" value="江西昌河汽车有限责任公司,待确定具体名称"/>
		</td>
       <td align="right">&nbsp;</td>
       <td>
      &nbsp;
       </td>
     </tr>
     
      <tr bgcolor="F3F4F8">
       <td align="right">开户行：</td>
       <td>
 			江西省景德镇市中国工商银行昌河支行(东安),待确定具体名称
 			 <input type="hidden" name="noticeBank" id="noticeBank" value="江西省景德镇市中国工商银行昌河支行,待确定具体名称"/>
		</td>
       <td align="right">银行账号：</td>
       <td>
      1503214109022102903,待确定具体名称
       <input type="hidden" name="noticeAcount" id="noticeAcount" value="1503214109022102903,待确定具体名称"/>
       </td>
     </tr>
     </c:if>
  </table>
  <table width="100%" class="table_list">
        <tr class="table_list_th">
            <th>序号</th>
            <th>配件名称</th>
            <th>剩余数量</th>
            <th>开票数量</th>
            <th>型号</th>
            <th>索赔单价</th>
            <th>工时</th>
            <th>材料费</th>
            <th>工时费</th>
            <th>其他</th>
            <th>小计</th>
            <th>税额</th>
            <th>合计</th>
       </tr>
       <c:if test="${hasDetail==0}">
       <c:set var="pageSize"  value="10000" />
     <c:forEach var="dList" items="${listBean}" varStatus="status">
	<tr style='${(status.count%pageSize==0) ? "page-break-after:always;":""}'  align="center">
	  	<td class="tdp" align="center" >${status.index+1}</td> 
	    <td class="tdp" align="center"  >${dList.partName}
	    <input type="hidden" name="partName" id="partName${status.index }" value="${dList.partName}" />
	    <input type="hidden" name="partCode" id="partCode${status.index }" value="${dList.partCode}" />
	    <input type="hidden" name="labourtemp1" id="labourtemp1${status.index }" value="${dList.labourHours}"/>
	     <input type="hidden" name="labourtemp2" id="labourtemp2${status.index }" value="${dList.labourAmount}"/>
	      <input type="hidden" name="outPartType" id="outPartType${status.index }" value="${dList.outPartType}"/>
	    </td>
	     <td class="tdp" align="center"  >${dList.outAmount}
	     <input type="hidden" name="allNum" id="allNum${status.index }" value="${dList.outAmount}" />
	    </td>
	    <td class="tdp" align="center"  >
	<!--      <input type="text" name="outAmount" class="short_txt" id="outAmount${status.index }" onblur="sumPrice2(this.value,${status.index})" value="${dList.outAmount}" /><span style="color:red">*</span>--> 
	  <input type="hidden" name="outAmount" class="short_txt" id="outAmount${status.index }" onblur="sumPrice2(this.value,${status.index})" value="${dList.outAmount}" />
	  ${dList.outAmount}
	    </td>
	    <td class="tdp" align="center" >${dList.modelName}
	    <input type="hidden" name="modelName" id="modelName${status.index }" value="${dList.modelName}" />
	    </td>
	    <td class="tdp" align="center">
	     <input type="text" name="claimPrice" id="claimPrice${status.index}" maxlength="6" onblur="sumPrice(this.value,${status.index })"  class="short_txt" value="${dList.claimPrice}"/><span style="color:red">*</span>
	    </td> 
	    <td class="tdp" align="center" id="labour1${status.index }">${dList.labourHours}
	    <input type="hidden" name="labourHours" id="labourHours${status.index }" value="${dList.labourHours}"/>
	    </td>
	     <td class="tdp" align="center" id="partPrice${status.index }"></td>
	    <td class="tdp" align="center" id="labour2${status.index }" >${dList.labourAmount}
	     <input type="hidden" name="labourPrice" id="labourPrice${status.index }" value="${dList.labourAmount}"/>
	    </td>
	    <td class="tdp" align="center" id="otherPrice${status.index }" ></td>
	    <td class="tdp" align="center" id="smallTotal${status.index }"></td> 
	    <td class="tdp" align="center" id="taxPrice${status.index }"></td>
	    <td class="tdp" align="center" id="total${status.index }"></td>
	 </tr>
  </c:forEach>
    <tr align="center">
	  	<td class="tdp" align="center" height="25px" colspan="8">&nbsp;</td> 
	    <td class="tdp" align="center" height="25px" colspan="2">总计：</td>
	    <td class="tdp" align="center"  height="25px" id="smallTotala" ></td>
	    <td class="tdp" align="center" height="25px" id = "taxPricea"></td>
	    <td class="tdp" align="center" height="25px" id = "totalPricea" ></td> 
	 </tr>
	 </c:if>
	  <c:if test="${hasDetail==1}">
       <c:set var="pageSize"  value="10000" />
     <c:forEach var="dList" items="${listBean}" varStatus="status">
	<tr style='${(status.count%pageSize==0) ? "page-break-after:always;":""}'  align="center">
	  	<td class="tdp" align="center" >${status.index+1}</td> 
	    <td class="tdp" align="center"  >${dList.partName}
	    </td>
	    <td class="tdp" align="center"  >${dList.outNum}
	    </td>
	    <td class="tdp" align="center" >${dList.modelName}
	    </td>
	    <td class="tdp" align="center">
	     <input type="text" name="claimPrice" id="claimPrice${status.index}" disabled="disabled" onblur="sumPrice(this.value,${status.index })"  class="short_txt" value="${dList.claimPrice }"/><span style="color:red">*</span>
	    </td> 
	    <td class="tdp" align="center" >${dList.claimLabour}
	    </td>
	     <td class="tdp" align="center" id="partPrice${status.index }">${dList.partPrice }</td>
	    <td class="tdp" align="center"  >${dList.labourPrice}
	    </td>
	    <td class="tdp" align="center" id="otherPrice${status.index }" >${dList.otherPrice }</td>
	    <td class="tdp" align="center" id="smallTotal${status.index }">${dList.smallTotal }</td> 
	    <td class="tdp" align="center" id="taxPrice${status.index }">${dList.taxTotal }</td>
	    <td class="tdp" align="center" id="total${status.index }">${dList.total }</td>
	 </tr>
  </c:forEach>
    <tr align="center">
	  	<td class="tdp" align="center" height="25px" colspan="7">&nbsp;</td> 
	    <td class="tdp" align="center" height="25px" colspan="2">总计：</td>
	    <td class="tdp" align="center"  height="25px" id="smallTotala" >${baseBean.smallTotal }</td>
	    <td class="tdp" align="center" height="25px" id = "taxPricea">${baseBean.taxTotal }</td>
	    <td class="tdp" align="center" height="25px" id = "totalPricea" >${baseBean.total}</td> 
	 </tr>
	 </c:if>
     </table>
     <table class="table_list">
      <tr > 
       <td height="12" align="center">
       <c:if test="${hasDetail==1}">
       <input type="button" id="save_btn" disabled="disabled" onclick="save();" class="normal_btn" style="width=8%" value="保存"/>
       </c:if>
        <c:if test="${hasDetail==0}">
       <input type="button" id="save_btn" onclick="save();" class="normal_btn" style="width=8%" value="保存"/>
       </c:if>
          <input type="button"  onclick="history.back();" class="normal_btn" style="width=8%" value="返回"/>
      	 <input type="hidden" name="totalPrice3" id="totalPrice3" value=""/>
       </td>
      </tr>
    </table>  
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<br />
<script type="text/javascript">
function sumPrice2(num,id){
	var allNum = $('allNum'+id).value;
	var price = $('claimPrice'+id).value;
	var reg =/^\d+$/;
	if(!reg.test(num)){
	MyAlert("数量请输入大于等于0整数!");
	$('outAmount'+id).value="";
	document.getElementById("partPrice"+id).innerHTML='';
	document.getElementById("otherPrice"+id).innerHTML='';
	document.getElementById("smallTotal"+id).innerHTML='';
	document.getElementById("taxPrice"+id).innerHTML='';
	document.getElementById("total"+id).innerHTML='';
	smmAll();
	return false;
	}
	if(parseInt(allNum)<parseInt(num)){
		MyAlert("开票数量不能大于剩余数量!");
		$('outAmount'+id).value="";
	document.getElementById("partPrice"+id).innerHTML='';
	document.getElementById("otherPrice"+id).innerHTML='';
	document.getElementById("smallTotal"+id).innerHTML='';
	document.getElementById("taxPrice"+id).innerHTML='';
	document.getElementById("total"+id).innerHTML='';
	smmAll();
		return false;
	}
	var temp = parseInt(num)/parseInt(allNum);
	//得到预加载出的工时以及工时费
	var labourHours = document.getElementById("labourtemp1"+id).value;//工时费
	var labourPrice = document.getElementById("labourtemp2"+id).value;//工时费
	//重新定位的工时以及工时费
	labourHours = (parseFloat(temp)*parseFloat(labourHours)).toFixed(2);
	labourPrice = (parseFloat(temp)*parseFloat(labourPrice)).toFixed(2);
	//根据输入的开票数量,重新定位刷新总工时以及工时费
	document.getElementById("labour1"+id).innerHTML=''+labourHours+' <input type="hidden" name="labourHours" id="labourHours'+id+'" value="'+labourHours+'"/>';
	document.getElementById("labour2"+id).innerHTML=''+labourPrice+' <input type="hidden" name="labourPrice" id="labourPrice'+id+'" value="'+labourPrice+'"/>';
	
	if(price==""||price==null){
	
	}else{
			sumPrice(price,id);
		}
	}
function sumPrice(price,id){
	var reg =/^\d+(?:\.\d{0,2})?$/;
	if(!reg.test(price)){//判断输入的索赔价是否是数字且最多2位小数
	MyAlert("请输入最多2位小数的数字!");
	document.getElementById("claimPrice"+id).value="";
	document.getElementById("partPrice"+id).innerHTML='';
	document.getElementById("otherPrice"+id).innerHTML='';
	document.getElementById("smallTotal"+id).innerHTML='';
	document.getElementById("taxPrice"+id).innerHTML='';
	document.getElementById("total"+id).innerHTML='';
	smmAll();
	return false;
	}
	//开始统计所在行费用
	var num = document.getElementById("outAmount"+id).value
	if(num!=""&&num!=null){
	var labourPrice = document.getElementById("labourPrice"+id).value
	var partPrice = parseFloat(price)*parseFloat(num);//出库数量*索赔价格
	var otherPrice = parseFloat((parseFloat(partPrice)+parseFloat(labourPrice))*0.1).toFixed(2);//材料费+工时费 的10%
	var smallTotal = parseFloat((parseFloat(partPrice)+parseFloat(labourPrice))*1.1).toFixed(2);//材料费+工时费+其他费用
	var taxPrice =  parseFloat(parseFloat(smallTotal)*0.17).toFixed(2);// 税额 = （材料费+工时费+其他费用）*0.17
	var total =  	parseFloat(parseFloat(smallTotal)*1.17).toFixed(2);//合计= 小计+税额
	document.getElementById("partPrice"+id).innerHTML=''+parseFloat(partPrice).toFixed(2)+' <input type="hidden" name="partPrice" id="partPrice'+id+'" value="'+partPrice+'"/>';
	document.getElementById("otherPrice"+id).innerHTML=''+parseFloat(otherPrice).toFixed(2)+' <input type="hidden" name="otherPrice" id="otherPrice'+id+'" value="'+otherPrice+'"/>';
	document.getElementById("smallTotal"+id).innerHTML=''+parseFloat(smallTotal).toFixed(2)+' <input type="hidden" name="smallTotal" id="smallTotal'+id+'" value="'+smallTotal+'"/>';
	document.getElementById("taxPrice"+id).innerHTML=''+parseFloat(taxPrice).toFixed(2)+' <input type="hidden" name="taxPrice" id="taxPrice'+id+'" value="'+taxPrice+'"/>';
	document.getElementById("total"+id).innerHTML=''+parseFloat(total).toFixed(2)+' <input type="hidden" name="total" id="total'+id+'" value="'+total+'"/>';
	smmAll();
	}
}
function smmAll(){
//通过循环得到总计
	var small = document.getElementsByName("smallTotal");
	var tax = document.getElementsByName("taxPrice");
	var totall = document.getElementsByName("total");	
	var sm=0;
	var ta=0;
	var to=0;
	for(var i=0;i<small.length;i++){//小计
		if(small[i].value!=null&&small[i].value!=""){
		sm = parseFloat(sm)+parseFloat(small[i].value);
		}
	}
	document.getElementById("smallTotala").innerHTML= ''+parseFloat(sm).toFixed(2)+' <input type="hidden" name="smallTotal2a" id="smallTotal2a" value="'+parseFloat(sm).toFixed(2)+'"/>';
	for(var i=0;i<tax.length;i++){//税额
		if(tax[i].value!=null&&tax[i].value!=""){
		ta =parseFloat(ta)+parseFloat(tax[i].value); 
		}
	}
	document.getElementById("taxPricea").innerHTML=''+parseFloat(ta).toFixed(2)+' <input type="hidden" name="taxPrice2a" id="taxPrice2a" value="'+parseFloat(ta).toFixed(2)+'"/>';
	for(var i=0;i<totall.length;i++){//税额
		if(totall[i].value!=null&&totall[i].value!=""){
		to =parseFloat(to)+parseFloat(totall[i].value); 
		}
	}
	document.getElementById("totalPricea").innerHTML=''+parseFloat(to).toFixed(2)+' <input type="hidden" name="totalPrice2a" id="totalPrice2a" value="'+parseFloat(to).toFixed(2)+'"/>';
	document.getElementById("totalPrice3").value=parseFloat(to).toFixed(2);
}
function save(){
	var claim = document.getElementsByName("claimPrice");
	var num = document.getElementsByName("outAmount");
	var totalPrice3 = document.getElementById('totalPrice3').value;
	var labour = document.getElementsByName("labourHours");
	var flag = true;
	for(var j=0;j<labour.length;j++){
		if(labour[j].value==""|| labour[j].value==0){
			MyAlert("开票基本工时不能为0!");
			return;
		}
	}
	for(var i=0;i<num.length;i++){
		if(num[i].value==""||num[i].value==null){
			MyAlert("请输入出库数量,此次不开票则输入0!");
			return;
		}
		if(num[i].value>0){
			flag = false;
		}
	}
	if(flag){
		MyAlert("出库数量不能全为0");
		return false;
	}
	for(var i=0;i<claim.length;i++){
		if(claim[i].value==""||claim[i].value==null){
			MyAlert("请输入索赔价格,不开票请输入0!");
			return;
		}
	}
	if(parseFloat(totalPrice3)==0){
		MyAlert("开票总金额必须大于0");
		return false;
	}
	MyConfirm("请再次确认出库数量以及索赔价,是否保存？",outOfStore,[]);
}
function outOfStore(){
$('save_btn').disabled=true;
 var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/saveOutNotice.json";
 	 makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
}
 function afterCall(json){	
   	var retCode=json.updateResult;
      if(retCode=="updateSuccess"){
    	    MyAlert("通知单生成成功!");
    	  //   if(json.yieldly==<%=Constant.PART_IS_CHANGHE_01%>){
    	  //  fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryOut.do";
    	 //   }else{
    	 //   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryOut2.do";
    	//    }
		//	fm.submit();
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outPartNoticeInfo.do?out_no="+json.outNo+"&code="+json.code;
       	fm.method="post";
       	fm.submit();
      }else if(retCode=="noValue"){
    	    MyAlert("通知单生成失败!");
     }
  }
  function init(){
  var size = $('listSize').value;
  for(var i=0;i<parseInt(size);i++){
  	sumPrice($('claimPrice'+i).value,i);
  }
}
</script>
</body>
</html>

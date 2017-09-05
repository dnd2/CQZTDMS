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
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;特殊单通知单生成</div>
 <form method="post" name ="fm" id="fm">
 <input type="hidden" name="id" id="id" value="${bean.id }" />
  <table class="table_edit">
    <tr bgcolor="F3F4F8">
      <td align="right">通知单标题：</td>
       <td>江西昌河汽车质量保证索赔通知单(特殊单)
        <input type="hidden" name="noticeTittle" id="noticeTittle" value="江西昌河汽车质量保证索赔通知单(特殊单)"/>
        </td>
       <td colspan="3">
       </td>
     </tr>
     <tr></tr>
     <tr bgcolor="F3F4F8">
       <td align="right">被索赔单位：</td>
       <td>
 			${bean.supplyName }
 			 <input type="hidden" name="noticeTo" id="noticeTo" value="${bean.supplyName }"/>
 			  <input type="hidden" name="code" id="code" value="${bean.supplyCode }"/>
		</td>
       <td align="right">联系方式：</td>
       <td>
       ${bean.tel }
       <input type="hidden" name="noticeTel" id="noticeTel" value="${bean.tel }"/>
       </td>
     </tr>
        <tr bgcolor="F3F4F8">
       <td align="right">索赔单位：</td>
       <td>
 			江西昌河汽车有限责任公司
 			  <input type="hidden" name="noticeBy" id="noticeBy" value="江西昌河汽车有限责任公司"/>
		</td>
       <td align="right">&nbsp;</td>
       <td>
      &nbsp;
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
  </table>
  <table width="100%" class="table_list">
        <tr class="table_list_th">
            <th>配件名称</th>
            <th>出库数量</th>
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
      
	<tr align="center">
	    <td class="tdp" align="center"  >${bean.partName}
	     <input type="hidden" name="partName" id="partName0" value="${bean.partName}" />
	      <input type="hidden" name="partCode" id="partName0" value="${bean.partCode}" />
	    </td>
	    <td class="tdp" align="center"  >1
	    <input type="hidden" name="outAmount" class="short_txt" id="outAmount0"  value="1" />
	    </td>
	    <td class="tdp" align="center" >
 		 <input type="text" name="modelName" id="modelName0"class="short_txt" value="${bean.modelCode}" /><span style="color:red">*</span>
	    </td>
	    <td class="tdp" align="center">
	     <input type="text" name="claimPrice" id="claimPrice0"  maxlength="6" onblur="sumPrice(this.value,0)"  class="short_txt" value="${bean.claimPrice }"/><span style="color:red">*</span>
	    </td> 
	    <td class="tdp" align="center" >0
	    <input type="hidden" name="labourHours" id="labourHours0" value="0"/>
	    </td>
	     <td class="tdp" align="center" id="partPrice0"></td>
	    <td class="tdp" align="center"  >0
	     <input type="hidden" name="labourPrice" id="labourPrice0" value="0"/>
	    </td>
	    <td class="tdp" align="center" id="otherPrice0" ></td>
	    <td class="tdp" align="center" id="smallTotal0"></td> 
	    <td class="tdp" align="center" id="taxPrice0"></td>
	    <td class="tdp" align="center" id="total0"></td>
	 </tr>
 
    <tr align="center">
	  	<td class="tdp" align="center" height="25px" colspan="6">&nbsp;</td> 
	    <td class="tdp" align="center" height="25px" colspan="2">总计：</td>
	    <td class="tdp" align="center"  height="25px" id="smallTotal2a" ></td>
	    <td class="tdp" align="center" height="25px" id = "taxPrice2a"></td>
	    <td class="tdp" align="center" height="25px" id = "totalPrice2a" ></td> 
	 </tr>
	
     </table>
     <table class="table_list">
      <tr > 
       <td height="12" align="center">
     
       <input type="button" id="save_btn" onclick="save();" class="normal_btn" style="width=8%" value="保存"/>

          <input type="button"  onclick="history.back();" class="normal_btn" style="width=8%" value="返回"/>
      	 <input type="hidden" name="totalPrice3" id="totalPrice3" value=""/>
       </td>
      </tr>
    </table>  
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<br />
<script type="text/javascript">
function init(){
sumPrice($('claimPrice0').value,0);
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
	var labourPrice = document.getElementById("labourPrice"+id).value
	var partPrice = parseFloat(price)*parseFloat(num);//出库数量*索赔价格
	var otherPrice = parseFloat((parseFloat(partPrice)+parseFloat(labourPrice))*0.1).toFixed(2);//材料费+工时费 的10%
	var smallTotal = parseFloat((parseFloat(partPrice)+parseFloat(labourPrice))*1.1).toFixed(2);//材料费+工时费+其他费用
	var taxPrice =  parseFloat(parseFloat(smallTotal)*0.17).toFixed(2);// 税额 = （材料费+工时费+其他费用）*0.17
	var total =  	parseFloat(parseFloat(smallTotal)*1.17).toFixed(2);//合计= 小计+税额
	document.getElementById("partPrice"+id).innerHTML=''+partPrice+' <input type="hidden" name="partPrice" id="partPrice'+id+'" value="'+partPrice+'"/>';
	document.getElementById("otherPrice"+id).innerHTML=''+otherPrice+' <input type="hidden" name="otherPrice" id="otherPrice'+id+'" value="'+otherPrice+'"/>';
	document.getElementById("smallTotal"+id).innerHTML=''+smallTotal+' <input type="hidden" name="smallTotal" id="smallTotal'+id+'" value="'+smallTotal+'"/>';
	document.getElementById("taxPrice"+id).innerHTML=''+taxPrice+' <input type="hidden" name="taxPrice" id="taxPrice'+id+'" value="'+taxPrice+'"/>';
	document.getElementById("total"+id).innerHTML=''+total+' <input type="hidden" name="total" id="total'+id+'" value="'+total+'"/>';
	smmAll();
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
	document.getElementById("smallTotal2a").innerHTML= ''+parseFloat(sm).toFixed(2)+' <input type="hidden" name="smallTotal2a" id="smallTotal2a" value="'+parseFloat(sm).toFixed(2)+'"/>';
	for(var i=0;i<tax.length;i++){//税额
		if(tax[i].value!=null&&tax[i].value!=""){
		ta =parseFloat(ta)+parseFloat(tax[i].value); 
		}
	}
	document.getElementById("taxPrice2a").innerHTML=''+parseFloat(ta).toFixed(2)+' <input type="hidden" name="taxPrice2a" id="taxPrice2a" value="'+parseFloat(ta).toFixed(2)+'"/>';
	for(var i=0;i<totall.length;i++){//税额
		if(totall[i].value!=null&&totall[i].value!=""){
		to =parseFloat(to)+parseFloat(totall[i].value); 
		}
	}
	document.getElementById("totalPrice2a").innerHTML=''+parseFloat(to).toFixed(2)+' <input type="hidden" name="totalPrice2a" id="totalPrice2a" value="'+parseFloat(to).toFixed(2)+'"/>';
	document.getElementById("totalPrice3").value=parseFloat(to).toFixed(2);
}
function save(){
	var claim = document.getElementsByName("claimPrice");
	var totalPrice3 = document.getElementById('totalPrice3').value;
	
	for(var i=0;i<claim.length;i++){
		if(claim[i].value==""||claim[i].value==null){
			MyAlert("请输入索赔价格!");
			return;
		}
	}
	if($('modelName0').value==""||$('modelName0').value==null){
		MyAlert("请输入车型!");
		return false;
	}
	if(parseFloat(totalPrice3)==0){
		MyAlert("开票总金额必须大于0");
		return false;
	}
	MyConfirm("请再次确认车型以及索赔价,是否保存？",outOfStore,[]);
}
function outOfStore(){
 var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/saveOutNotice.json?type=1";
 	 makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
}
 function afterCall(json){	
   	var retCode=json.updateResult;
      if(retCode=="updateSuccess"){
    	    MyAlert("通知单生成成功!");
    	    fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/mainPartClaim.do";
			fm.submit();
      }else if(retCode=="noValue"){
    	    MyAlert("通知单生成失败!");
     }
  }
</script>
</body>
</html>

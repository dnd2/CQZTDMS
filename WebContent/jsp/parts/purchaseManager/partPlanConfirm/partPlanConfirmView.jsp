<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib prefix="fmt" uri="/jstl/fmt" %>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看</title>
<link href="<%=contextPath%>/style/content.css" type="text/css" rel="stylesheet"/>
<link href="<%=contextPath%>/style/calendar.css" type="text/css" rel="stylesheet" />
<style type="text/css">
.table_list_row0 td {
	background-color:#FFFFCC;
	border: 1px solid #DAE0EE;
	white-space:    nowrap;
}
</style>
<LINK href="<%=contextPath%>/style/page-info.css" type="text/css"/>

<SCRIPT type=text/javascript>

function selBox(id,type,selectedKey,setAll,_class_,_script_,nullFlag,expStr){
	var str = "";
	var arr;
	if(expStr.indexOf(",")>0)
		arr = expStr.split(",");
	else {
		expStr = expStr+",";
		arr = expStr.split(",");
	}
	str += "<select disabled id='" + id + "' name='" + id +"' class='"+ _class_ +"' " + _script_ ;
	if(nullFlag && nullFlag == "true"){
		str += " datatype='0,0,0' ";
	}
	str += " onChange=doCusChange(this.value);> ";
	if(setAll){
		str += genDefaultOpt();
	}
	for(var i=0;i<codeData.length;i++){
		var flag = true;
		for(var j=0;j<arr.length;j++){
			if(codeData[i].codeId == arr[j]){
				flag = false;
			}
		}
		if(codeData[i].type == type && flag){
			str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '"+codeData[i].codeDesc+"' >" + codeData[i].codeDesc + "</option>";
		}
	}
	str += "</select>";

	document.write(str);
}
function getIdx(){
	document.write(document.getElementById("file").rows.length-2);
}
function goBack(){
	  document.fm.action = "<%=contextPath%>/parts/purchaseManager/partPlanConfirm/PartPlanConfirm/partPlanConfirmInit.do";
      document.fm.submit();
	}

function btnActions(value){
	if(value=="1"){
		var cbs = document.getElementsByName("cb");
		var flag = false;
        var venderArr = new Array();
		for(var i=0;i<cbs.length;i++){
			if(cbs[i].checked){
				var buyQty1= $("buyQty1_"+cbs[i].value).value;
				var buyQty2= $("buyQty2_"+cbs[i].value).value;
				var buyQty3= $("buyQty3_"+cbs[i].value).value;
				var origin1 = $("origin1_"+cbs[i].value).value;
				var origin2 = $("origin2_"+cbs[i].value).value;
				var origin3 = $("origin3_"+cbs[i].value).value;
				if(origin1==""&&origin2==""&&origin3==""){
					MyAlert("请选择第"+(i+1)+"行的供应商!");
					return ;
				}
				if(buyQty1==""&&origin1!=""){
					MyAlert("请填写第"+(i+1)+"行的采购量!");
					return ;
				}
				if(buyQty2==""&&origin2!=""){
					MyAlert("请填写第"+(i+1)+"行的采购量!");
					return ;
				}
				if(buyQty3==""&&origin3!=""){
					MyAlert("请填写第"+(i+1)+"行的采购量!");
					return ;
				}
                
                if(origin1!=""){
                    venderArr.push(origin1);
                }
                if(origin2!=""){
                    venderArr.push(origin2);
                }
                if(origin3!=""){
                    venderArr.push(origin3);
                }
				flag = true;
			}

		}
		
		if(!flag){
			MyAlert("请选中一条记录!");
			return;
		}
        if (venderArr.length > 0&&value=="1") {
            for (var i = 0; i < venderArr.length - 1; i++) {
                if (venderArr[i] != venderArr[i + 1] && venderArr[i] != '' && venderArr[i + 1] != '') {
                    MyAlert("请为每个配件选择相同的供应商!");
                    return;
                }
            }
            MyConfirm("确定保存?",passTheConfirm,[]);
        }
        
	}
	
	if(value=="3"){
		var cbs = document.getElementsByName("cb");
	    var flag = false;
	    var venderArr = new Array();
		for(var i=0;i<cbs.length;i++){
			if(cbs[i].checked){
				var deVenderId= $("DEVENDERID_"+cbs[i].value).value;
	            if(deVenderId!=""){
	                venderArr.push(deVenderId);
	            }
				flag = true;
			}
	
		}
		if(!flag){
			MyAlert("请选中一条记录!");
			return;
		}
	    
		var deVenderArr = distinctArray(venderArr);
    	var deVenderStr = deVenderArr.join(",");
		$("deVenderStr").value = deVenderStr;
		MyConfirm("确定按默认保存?",passTheConfirmDefault,[]);
	}
	if(value=="2"){
		MyConfirm("确定驳回?",rebutTheConfirm,[]);
	}
}



function rebutTheConfirm(){
	    disableAllStartBtn();
		fm.action = "<%=contextPath%>/parts/purchaseManager/partPlanConfirm/PartPlanConfirm/rebutTheConfirm.do";
		fm.submit();
}

function checkValue(obj){
	if(""==obj.value){
		return;
	}
	if(isNaN(obj.value)){
		MyAlert("请填写数字!");
		obj.value="";
		return;
	}
	var re = /^[1-9]+[0-9]*]*$/;
		if(!re.test(obj.value)){
			MyAlert("请输入正整数!");
			obj.value = "";
			return;
	}
	var tbl = document.getElementById("file");
	var idx = obj.parentElement.parentElement.rowIndex;
	if(parseFloat(obj.value)>parseFloat(tbl.rows[idx].cells[9].innerText)){
		MyAlert("采购量不能大于计划量!");
		obj.value="";
		return;
	}
}

function checkAll(obj){
	var cb = document.getElementsByName("cb");
	for(var i=0;i<cb.length;i++){
		cb[i].checked = obj.checked;
	}
	if(obj.checked){
		obj.value="1";
	}
    getTotal();
}

function cbc(obj){
	var cb = document.getElementsByName("cb");
	var flag= false;
	for(var i=0;i<cb.length;i++){
		if(!cb[i].checked){
			flag = true;
		}
	}
	if(flag){
		document.getElementById("cbAll").checked=false;
		document.getElementById("cbAll").value="0";
	}else{
		document.getElementById("cbAll").checked=true;
			document.getElementById("cbAll").value="1";
	}
    getTotal();
}

function getTotal(){
		var qtyCount=parseFloat(0);
		var amountCount=parseFloat(0);
		var tbl = document.getElementById("file");
		for(var i = 2;i<tbl.rows.length;i++){
          if(tbl.rows[i].cells[0].childNodes[1].checked){
        	 var id=tbl.rows[i].cells[0].childNodes[1].value;
        	 var rowTotalQty=0;
        	 if($('buyQty1_'+id).value&&$('origin1_'+id).value){
        		 rowTotalQty = $('buyQty1_'+id).value;
        	 }
        	 if($('buyQty2_'+id).value&&$('origin2_'+id).value){
        		 rowTotalQty = $('buyQty2_'+id).value;
        	 }
        	 if($('buyQty3_'+id).value&&$('origin3_'+id).value){
        		 rowTotalQty = $('buyQty3_'+id).value;
        	 }
        	 //rowTotalQty =  parseFloat($('buyQty1_'+id).value) +  parseFloat($('buyQty2_'+id).value) +  parseFloat($('buyQty3_'+id).value);
             var rowTotalMoney = (parseFloat(rowTotalQty)*parseFloat(tbl.rows[i].cells[15].innerText)).toFixed(2);
        	 qtyCount =(parseFloat(qtyCount)+parseFloat(rowTotalQty)).toFixed(2);
        	 amountCount = (parseFloat(amountCount)+parseFloat(rowTotalMoney)).toFixed(2);
          }
		}
		document.getElementById("SUM_QTY").value=qtyCount;
		document.getElementById("AMOUNT").value=amountCount;

}

function passTheConfirm(){
		//通过DISABLED属性 控制提交数据
		var cbs = document.getElementsByName("cb");
		for(var i=0;i<cbs.length;i++){
			if(!cbs[i].checked){
				document.getElementById("buyQty1_"+cbs[i].value).disabled=true;
				document.getElementById("buyQty2_"+cbs[i].value).disabled=true;
				document.getElementById("buyQty3_"+cbs[i].value).disabled=true;
			}
		}
		if(!submitForm('fm')){
		return;
	}
	disableAllStartBtn();
	var url = "<%=contextPath%>/parts/purchaseManager/partPlanConfirm/PartPlanConfirm/passTheConfirm.json";
	sendAjax(url,getResult,'fm');
}

function passTheConfirmDefault(){
		if(!submitForm('fm')){
		return;
	}
	disableAllStartBtn();
	var url = "<%=contextPath%>/parts/purchaseManager/partPlanConfirm/PartPlanConfirm/passTheConfirmDefault.json";
	sendAjax(url,getResult,'fm');
}

function getResult(jsonObj){
	enableAllStartBtn();
	if(jsonObj!=null){
	    var success = jsonObj.success;
	    var error = jsonObj.error;
	    var exceptions = jsonObj.Exception;
	    if(success){
           	MyAlert(success);
           	var msg = jsonObj.msg;
           	if(msg=="finish"){
           		window.location.href = '<%=contextPath%>/parts/purchaseManager/partPlanConfirm/PartPlanConfirm/partPlanConfirmInit.do';
           	}else{
           		window.location.href = "<%=contextPath%>/parts/purchaseManager/partPlanConfirm/PartPlanConfirm/partPlanView.do?planId=" + $('planId').value;
           	}
	       // window.location.href = '<%=contextPath%>/parts/purchaseManager/partPlanConfirm/PartPlanConfirm/partPlanConfirmInit.do';
	    }else if(error){
	    	MyAlert(error);
	    }else if(exceptions){
	    	MyAlert(exceptions.message);
			window.location.href="<%=contextPath%>/parts/purchaseManager/partPlanConfirm/PartPlanConfirm/partPlanView.do?planId="+${mainMap.PLAN_ID};
		}
	}
}

function disableAllStartBtn(){
		var inputArr = document.getElementsByTagName("input");
		for(var i=0;i<inputArr.length;i++){
			if(inputArr[i].type=="button"){
				inputArr[i].disabled=true;
			}
		}
	}
function enableAllStartBtn(){
	var inputArr = document.getElementsByTagName("input");
	for(var i=0;i<inputArr.length;i++){
		if(inputArr[i].type=="button"){
			inputArr[i].disabled=false;
		}
	}
}
function getCount(obj,price,confirmQty,totalQty,partId){
	var tbl = document.getElementById("file");
	if(isNaN(obj.value)){
		MyAlert("请输入数字!");
		obj.value = "";
		return;
	}
	var re = /^[1-9]+[0-9]*]*$/;
	if(!re.test(obj.value)){
		MyAlert("请输入正整数!");
		obj.value = "";
		return;
	}

	var idx = obj.parentElement.parentElement.rowIndex;
	var money = (parseFloat(price)*parseFloat(obj.value)).toFixed(2);
	tbl.rows[idx].cells[16].innerHTML = '<td ><input type="hidden" id="AMOUNT_'+partId+'" name="AMOUNT_'+partId+'"  value="'+money+'" />'+money+'</td>';
	
	var rowTotalQty =  parseFloat(tbl.rows[idx].cells[9].firstChild.value) +  parseFloat(tbl.rows[idx].cells[11].firstChild.value) +  parseFloat(tbl.rows[idx].cells[13].firstChild.value);
	if(parseFloat(rowTotalQty)+parseFloat(confirmQty)>parseFloat(totalQty)){
			MyAlert("确认通过的数量不得大于计划总量!");
			obj.value="";
			return;
	}
	getTotal();
}

function validateSel(obj,partId,num,index){
	if(obj.value&&index==1){
		$("origin2_"+partId).value = "";
		$("origin3_"+partId).value = "";
		$("buyQty1_"+partId).value = num;
		$("buyQty2_"+partId).value = "0";
		$("buyQty3_"+partId).value = "0";
	}
	if(obj.value&&index==2){
		$("origin1_"+partId).value = "";
		$("origin3_"+partId).value = "";
		$("buyQty2_"+partId).value = num;
		$("buyQty1_"+partId).value = "0";
		$("buyQty3_"+partId).value = "0";
	}
	if(obj.value&&index==3){
		$("origin1_"+partId).value = "";
		$("origin2_"+partId).value = "";
		$("buyQty3_"+partId).value = num;
		$("buyQty1_"+partId).value = "0";
		$("buyQty2_"+partId).value = "0";
	}
	getTotal();
	/* if(obj.value==""&&obj2.value==""){
		return;
	}
	if(obj.value == obj2.value){
		if(code=='1'){
			MyAlert("同一行数据供应商1不能和供应商2相同!");
		}
		if(code=='2'){
			MyAlert("同一行数据供应商2不能和供应商1相同!");
		}
		obj.value="";
		return;
	} */
}

function queryPlanDtl(){
	disableAllStartBtn();
	fm.action = "<%=contextPath%>/parts/purchaseManager/partPlanConfirm/PartPlanConfirm/partPlanView.do";
    fm.submit();
}

function clearInput() {//清空选定供应商
	var venderId = document.getElementById("VENDER_ID").value;
	if(venderId!=null&&venderId!=""){
		document.getElementById("VENDER_ID").value = '';
	    document.getElementById("VENDER_NAME").value = '';
	    changeDiv();
	}
}

//获取序号
function getIdx(){
	document.write(document.getElementById("file").rows.length-2);
}

function distinctArray(arr){
	var obj={},temp=[];
	for(var i=0;i<arr.length;i++){
	if(!obj[typeof (arr[i])+arr[i]]){
	temp.push(arr[i]);
	obj[typeof (arr[i])+arr[i]] =true;
	   }
	}
    return temp;
}
</script>
</HEAD>
<BODY onload="enableAllStartBtn();getTotal();">
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>配件管理>采购计划管理>计划查询>计划确认明细</div>
  <table class="table_query" bordercolor="#DAE0EE">
  <input type="hidden" name="planId" id="planId" value="${mainMap.PLAN_ID}">
  <input type="hidden" name="deVenderStr" id="deVenderStr" value="">

    <tr>
      <th width="100%" align="left" colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 计划单信息</th>
    </tr>
    <tr>
      <td bgcolor="#F3F4F8"   align="right">计划单号:</td>
      <td bgcolor="#FFFFFF" align="left" width="24%">&nbsp;<c:out value="${mainMap.PLAN_CODE}" /></td>
      <td   align="right" bgcolor="#F3F4F8">计划员:</td>
      <td align="left" bgcolor="#FFFFFF" width="24%">&nbsp;<c:out value="${mainMap.NAME}" /><br />
      <input type="hidden" name="planerId" value="${mainMap.PLANER_ID}"/>
      <input type="hidden" name="planerName" value="${mainMap.NAME}"/>
      </td>
      <td   align="right" bgcolor="#F3F4F8">制单日期:</td>
      <td align="left" bgcolor="#FFFFFF" width="24%">&nbsp;<c:out value="${mainMap.CREATE_DATE}" /></td>
    </tr>
    <tr>
      <td   align="right" bgcolor="#F3F4F8">计划年月:</td>
      <td align="left" bgcolor="#FFFFFF">&nbsp;<c:out value="${mainMap.YEAR_MONTH}" /></td>
      <td   align="right" bgcolor="#F3F4F8">计划类型:</td>
      <td align="left" bgcolor="#FFFFFF">&nbsp;
      	<script type="text/javascript">
       			selBox("PLAN_TYPE",<%=Constant.PART_PURCHASE_PLAN_TYPE%>,${mainMap.PLAN_TYPE},true,"short_sel","","false",'');
		</script>
      </td>
      <td   align="right" bgcolor="#F3F4F8">库房:</td>
      <td align="left" bgcolor="#FFFFFF" width="21%">&nbsp;<c:out value="${mainMap.WH_NAME}" /><br /></td>
    </tr>
    <tr>
      <td bgcolor="#F3F4F8"   align="right">总数量:</td>
      <td bgcolor="#FFFFFF" align="left">&nbsp;<input readonly class="phone_txt" type="text" style="border:0px;background-color:#F3F4F8;" value="${mainMap.SUM_QTY}"  name="SUM_QTY" id="SUM_QTY" /></td>
      <td bgcolor="#F3F4F8"   align="right">总金额:</td>
      <td bgcolor="#FFFFFF" align="left">&nbsp;<input readonly class="phone_txt" type="text" style="border:0px;background-color:#F3F4F8;" value="${mainMap.CONVERSEAMOUNT}"  name="AMOUNT" id="AMOUNT" /></td>
      <td bgcolor="#F3F4F8"   align="right"></td>
      <td bgcolor="#FFFFFF" align="left"></td>
    </tr>
      <tr>
          <td align="right" bgcolor="#F3F4F8">备注:</td>
          <td colspan="3">${mainMap.REMARK}</td>
      </tr>
    <tr>
      <td align="right" bgcolor="#F3F4F8">订单备注:</td>
      <td colspan="3"><textarea name="REMARK" id="REMARK" style="width:90%" rows="2"></textarea>
      </td>
    </tr>

  </table>
  </div>
  <FIELDSET>
    <LEGEND
            style="MozUserSelect: none; KhtmlUserSelect: none"
            unselectable="on">
        <th colspan="6">
            <img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>
            配件查询
        </th>
    </LEGEND>
    <div style="heigeht: 5px" id="partDiv">
        <table class="table_query" width=100% border="0" align="center"
               cellpadding="1" cellspacing="1">
            <tr>

                <td   align="right" width="10%">
                    配件编码：
                </td>
                <td width="20%">
                    <input class="middle_txt" id="PART_OLDCODE"
                           datatype="1,is_noquotation,30" name="PART_OLDCODE"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" value="${partOldCode }"/>
                </td>
                <td   align="right" width="10%">
                    配件名称：
                </td>
                <td width="20%">
                    <input class="middle_txt" id="PART_CNAME"
                           datatype="1,is_noquotation,30" name="PART_CNAME"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" value="${partCname }"/>
                </td>
                <td   align="right" width="10%">
                    默认供应商：
                </td>
                <td width="30%">
                    <input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME" value="${venderName }"/>
                    <input class="mark_btn" type="button" value="&hellip;"
                           onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
                           <INPUT class=normal_btn onclick="clearInput();" value=清除 type=button name=clrBtn>
                    <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="${venderId }">
                </td>
            </tr>
            <tr>
                <td align="center" colspan="6">
                    <input class="normal_btn" type="button" name="BtnQuery"
                           id="queryBtn" value="查 询" onclick="queryPlanDtl();"/>
                </td>
            </tr>
        </table>
    </div>
</FIELDSET>
  <table id="file" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr>
      <th colspan="24" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />配件信息</th>
    </tr>
    <tr class="table_list_row0">
      <td>&nbsp;<input id="cbAll" name="cbAll" onclick="checkAll(this)" value="0" type="checkbox" checked/></td>
      <td>序号</td>
      <td>配件编码</td>
      <td>配件名称</td>
      <td>件号</td>
      <td>单位</td>
      <td>计划量</td>
      <td>已确认计划量</td>
      <td>默认供应商</td>
      <td>数量</td>
      <td>供应商1</td>
      <td>数量</td>
      <td>供应商2</td>
      <td>数量</td>
      <td>最小包装量</td>
      <td>计划价</td>
      <td>金额</td>
      <td>可用库存量</td>
      <td>BO量</td>
      <td>月均销量</td>
      <td>在途量</td>
      <td>安全库存量</td>
      <td>预计到货日期</td>
      <td>备注</td>
     <c:forEach items="${detailList}" var="data" >
     	<tr class="table_list_row1">
     		  <td>&nbsp;
     		  <input id="cb" name="cb" value="${data.PART_ID}" onclick="cbc(this);" type="checkbox" checked="checked" />
     		  <input id="VER_${data.PART_ID}" name="VER_${data.PART_ID}"  value="${data.VERSION}" type="hidden" />
     		  <input name="VER_${data.PART_ID}_${data.defaultVenderId}" value="${data.VERSION}" type="hidden" />
     		  <input name="PLINE_ID_${data.PART_ID}_${data.defaultVenderId}"  value="${data.PLINE_ID}" type="hidden" />
     		  <input id="DEVENDERID_${data.PART_ID}" name="DEVENDERID_${data.PART_ID}"  value="${data.defaultVenderId}" type="hidden" />
     		  </td>
     		  <td align="left">&nbsp;
   			  	<script type="text/javascript">
       				getIdx();
				</script>
                  &nbsp;
   			  </td>
		      <td align="left">
		      <c:out value="${data.PART_OLDCODE}" />
		      <input type="hidden" name="partOldCode_${data.PART_ID}_${data.defaultVenderId}" value="${data.PART_OLDCODE}"/>
		      </td>
		      <td align="left"><c:out value="${data.PART_CNAME}" />
		      <input type="hidden" name="partCname_${data.PART_ID}_${data.defaultVenderId}" value="${data.PART_CNAME}"/></td>
              <td align="left">
              <c:out value="${data.PART_CODE}" />
              <input type="hidden" name="partCode_${data.PART_ID}_${data.defaultVenderId}" value="${data.PART_CODE}"/></td>
              <input type="hidden" name="partType_${data.PART_ID}_${data.defaultVenderId}" value="${data.PART_TYPE}"/></td>
              <input type="hidden" name="lineNo_${data.PART_ID}_${data.defaultVenderId}" value="${data.LINE_NO}"/></td>
              </td>
		      <td>
		      <c:out value="${data.UNIT}" />
		      <input type="hidden" name="unit_${data.PART_ID}_${data.defaultVenderId}" value="${data.UNIT}"/>
		      </td>
		      <td>
		      <c:out value="${data.CHECK_NUM}" />
		      <input type="hidden" name="planQty_${data.PART_ID}_${data.defaultVenderId}" value="${data.PLAN_QTY}"/>
		      </td>
		      <td><c:out value="${data.confirmPlanQty}" /></td>
		      <td>
				<select  name="origin1_${data.PART_ID}" id = "origin1_${data.PART_ID}" onchange="validateSel(this,${data.PART_ID},${data.CHECK_NUM-data.confirmPlanQty},1)" style="width: 250px">
                    <option  value=''>-请选择-</option>
		      		<c:forEach items="${data.defaultList}" var="vender" >
		      			<option  value="${vender.VENDER_ID}" selected>${vender.VENDER_NAME}</option>
				   	</c:forEach>
      			</select>
      			
		      </td>
		      <td>
		      <input id="buyQty1_${data.PART_ID}" name="buyQty1_${data.PART_ID}" onchange="getCount(this,${data.PLAN_PRICE},${data.confirmPlanQty},${data.PLAN_QTY},${data.PART_ID})" onkeyup="getCount(this,${data.PLAN_PRICE},${data.confirmPlanQty},${data.PLAN_QTY},${data.PART_ID})" style="width:35px;background-color:#FF9" value="${data.CHECK_NUM-data.confirmPlanQty}" type="text" />
		      <input type="hidden" name="buyQty1_${data.PART_ID}_${data.defaultVenderId}" value="${data.CHECK_NUM-data.confirmPlanQty}"/>
		      </td>
		      <td>
				<select  name="origin2_${data.PART_ID}" id = "origin2_${data.PART_ID}" onchange="validateSel(this,${data.PART_ID},${data.CHECK_NUM-data.confirmPlanQty},2)" class="short_sel">
                    <option selected value=''>-请选择-</option>
		      		<c:forEach items="${data.venderList}" var="vender" >
		      			<option  value="${vender.VENDER_ID}">${vender.VENDER_NAME}</option>
				   	</c:forEach>
      			</select>
		      </td>
		      <td>
		      <input id="buyQty2_${data.PART_ID}" name="buyQty2_${data.PART_ID}"  onchange="getCount(this,${data.PLAN_PRICE},${data.confirmPlanQty},${data.PLAN_QTY},${data.PART_ID})" onkeyup="getCount(this,${data.PLAN_PRICE},${data.confirmPlanQty},${data.PLAN_QTY},${data.PART_ID})"  style="width:35px;background-color:#FF9" value="0" type="text" />
		      </td>
		      <td>
				<select  name="origin3_${data.PART_ID}" id = "origin3_${data.PART_ID}" onchange="validateSel(this,${data.PART_ID},${data.CHECK_NUM-data.confirmPlanQty},3)" class="short_sel">
                    <option selected value=''>-请选择-</option>
		      		<c:forEach items="${data.venderList}" var="vender" >
		      			<option  value="${vender.VENDER_ID}">${vender.VENDER_NAME}</option>
				   	</c:forEach>
      			</select>
		      </td>
		      <td>
		      <input id="buyQty3_${data.PART_ID}" name="buyQty3_${data.PART_ID}"  onchange="getCount(this,${data.PLAN_PRICE},${data.confirmPlanQty},${data.PLAN_QTY},${data.PART_ID})" onkeyup="getCount(this,${data.PLAN_PRICE},${data.confirmPlanQty},${data.PLAN_QTY},${data.PART_ID})"  style="width:35px;background-color:#FF9" value="0" type="text" />
		      </td>
		      <td><c:out value="${data.MIN_PACKAGE}" /></td>
		      <td>
		      <c:out value="${data.PLAN_PRICE}" />
		      </td>
		      <td>
		      <input id="AMOUNT_${data.PART_ID}" name="AMOUNT_${data.PART_ID}"  value="${data.buyAmount }" type="hidden" />${data.buyAmount }
		      <input type="hidden" name="AMOUNT_${data.PART_ID}_${data.defaultVenderId}" value="${data.buyAmount}"/>
		      </td>
		      <td><c:out value="${data.STOCK_QTY}" /></td>
		      <td><c:out value="${data.BO_QTY}" /></td>
		      <td><c:out value="${data.AVG_QTY}" /></td>
		      <td><c:out value="${data.ZT_NUM}" /></td>
		      <td><c:out value="${data.SFATE_STOCK}" /></td>
		      <td>
		      <fmt:formatDate value="${data.FORECAST_DATE}" pattern="yyyy-MM-dd"/> 
		      <input type="hidden" name="FORECAST_DATE_${data.PART_ID}_${data.defaultVenderId}" value="${data.FORECAST_DATE}"/>
		      </td>
		      <td>
		      <c:out value="${data.REMARK}" />
		      <input type="hidden" name="REMARK_${data.PART_ID}_${data.defaultVenderId}" value="${data.REMARK}"/>
		      </td>

   		 </tr>
	</c:forEach>
     </table>
  <table width="100%" align="center">
    <tr>
      <td height="2"></td>
    </tr>
    <tr>
        <td align="center">
        <input class="normal_btn" type="button" value="确认" name="button1" onclick="btnActions(1);" />
        &nbsp;
        <input class="long_btn" type="button" value="确认(按默认)" name="button3" onclick="btnActions(3);" />
        &nbsp;
        <input class="normal_btn" type="button" value="返回" name="button2" onclick="goBack();" />
        </td>
    </tr>
    <tr>
      <td height="1"></td>
    </tr>
  </table>
</form>
</BODY>
</html>

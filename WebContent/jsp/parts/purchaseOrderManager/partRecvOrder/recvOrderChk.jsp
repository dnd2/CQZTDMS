<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>领件单验收</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
	<div class="navigation"> <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
	 配件管理&gt;采购计划管理&gt;配件领件订单&gt;生成验收指令
	</div>
<form name="fm" id="fm" method="post" >
<input type="hidden" name="orderId" value="${map.ORDER_ID}"/>
	<table class="table_query">
    <tr>
      <td width="10%" align="right">领件单号:</td>
      <td width="20%" align="left" width="24%">
        &nbsp;<c:out value="${map.ORDER_CODE}" />
        <input type="hidden" name="orderCode" value="${map.ORDER_CODE}"/>
      </td>
      <td width="10%" align="right" >领件人:</td>
      <td align="left"  width="20%">
        &nbsp;<c:out value="${map.BUYER}" />
        <input type="hidden" name="BUYER_ID" value="${map.BUYER_ID}"/>
        <input type="hidden" name="BUYER" value="${map.BUYER}"/>
      </td>
      <td width="10%" align="right" >制单日期:</td>
      <td align="left" width="20%" >
        &nbsp;<c:out value="${map.CREATE_DATE}" />
      </td>
    </tr>
    <tr>
      <td  align="right">总数量:</td>
      <td  align="left">
        &nbsp;${map.SUM_QTY}
      </td>
      <td align="right" >计划类型:</td>
      <td align="left" >
        &nbsp;${map.PLAN_TYPE}
		<input type="hidden" name="PLAN_TYPE1" value="<%=Constant.PART_PURCHASE_PLAN_TYPE_03%>"/>
      </td>
      <td align="right" >库房:</td>
      <td align="left"  width="21%">&nbsp;<c:out value="${map.WH_NAME}" />
      <input type="hidden" name="WH_ID" value="${map.WH_ID}"/>
      <input type="hidden" name="WH_NAME" value="${map.WH_NAME}"/>
      </td>
    </tr>
    <tr>
      <td  align="right">总金额:</td>
      <td  align="left">
        &nbsp;${map.AMOUNT}
      </td>
      <td align="right">库管员:</td>
      <td align="left">
    	<select id="WHMAN_ID" name="WHMAN_ID" class="short_sel">
            <option value="">-请选择-</option>
            <c:forEach items="${whmans}" var="whman">
                <option value="${whman.WHMAN_ID }">${whman.WHMAN_NAME }</option>
            </c:forEach>
        </select>
        <font color="red">*</font>
      </td>
      <td align="right" >备注:</td>
      <td align="left">&nbsp;<c:out value="${map.REMARK}" /></td>
    </tr>
    <tr>
      
    </tr>
</table>
<table class="table_query">
<th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />配件信息</th>
    <tr>
                <td align="right" width="10%">
                    配件编码：
                </td>
                <td align="left" width="20%">
                    <input class="middle_txt" id="partOldcode"
                           datatype="1,is_noquotation,30" name="partOldcode"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td align="right" width="10%">
                    配件名称：
                </td>
                <td align="left" width="20%">
                    <input class="middle_txt" id="partCname"
                           datatype="1,is_noquotation,30" name="partCname"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td align="right" width="10%">
                    件号：
                </td>
                <td width="20%" align="left">
                    <input class="middle_txt" id="partCode"
                           datatype="1,is_noquotation,30" name="partCode"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
            </tr>
    		<tr>
				<td align="center" colspan="6">
					<input class="normal_btn" type="button" name="BtnQuery"
						id="queryBtn" value="查 询" onclick="__extQuery__(1)" />
				</td>
			</tr>
 </table>
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<table border="0" class="table_query">
  <tr align="center">
  <td>
  <input name="agreeBtn" id="agreeBtn"  class="normal_btn" type="button" value="确 定" onclick="agreeApply();"/>
    &nbsp;
  <input class="normal_btn" type="button" value="返 回" onclick="javascript:goback();"/>&nbsp;</td>
  </tr>
  </table>
</form>
<script type="text/javascript" >
var myPage;

var url = "<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/queryPurchaseOrderDetail.json";
			
var title = null;

var columns = [
				{header: "序号", align:'center',renderer:getIndex},
				{header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'POLINE_ID', align:'center',width: '33px' ,renderer:seled},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
				{header: "配件名称", dataIndex: 'PART_CNAME',style: 'text-align:left'},
                {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left',renderer:insertCodeInput},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "供应商名称", dataIndex: 'VENDER_NAME', align:'center',renderer:insertVenderInput},
				//{header: "制造商名称", dataIndex: 'MAKER_NAME', align:'center',renderer:insertMakerInput},
				{header: "制造商名称", dataIndex: 'MAKER_NAME', align:'center', renderer: makerSelect},
				{header: "计划数量", dataIndex: 'PLAN_QTY', align:'center',renderer:insertPlanQtyInput},
				{header: "领件数量", dataIndex: 'BUY_QTY', align:'center',renderer:insertBuyQtyInput},
				{header: "金额", dataIndex: 'BUY_AMOUNT', align:'center'},
				{header: "生成数量", dataIndex: 'GE_QTY', align:'center',renderer:insertGeQtyInput},
				{header: "已生成数量", dataIndex: 'CHECK_QTY', align:'center',renderer:insertChkQtyInput},
				{header: "备注", dataIndex: 'REMARK', align:'center',renderer:insertRemarkInput}
		      ];


function seled(value,meta,record) 
{
	 return "<input type='checkbox' value='"+value+"' name='ck' id='ck' onclick='chkPart()'/>";
}

function chkPart(){
	var cks = document.getElementsByName('ck');
	var flag = true;
	for(var i =0 ;i<cks.length;i++){
		var obj  = cks[i];
		if(!obj.checked){
			flag = false;
		}
	}
	document.getElementById("ckbAll").checked = flag;
}

var poIdArr = new Array();
//插入制造商下拉选择框
function makerSelect(value, metaDate, record) {
 
  var makerOutput;
  var poId = record.data.POLINE_ID;
  //var curVenderId = record.data.VENDER_ID;
  var curPartId = record.data.PART_ID;
  var curMakerId = record.data.MAKER_ID;
  //var url1 = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryMakerInfo.json?curVenderId=" + curVenderId + "&poId=" + poId;
  var url1 = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryMakerInfo.json?curPartId=" + curPartId + "&poId=" + poId;
  if (curMakerId) {//如果当前制造商id存在
      makerOutput = "<select class='short_sel' id='MAKER_ID" + poId + "' name='MAKER_ID" + poId + "' onmouseover='insertMaker(" + poId + "," + curPartId + "," + curMakerId + ")'>"
              + "<option value='" + curMakerId + "'>" + value + "</option>";
      +"</select>";
  } else {
      makerOutput = "<select class='short_sel' id='MAKER_ID" + poId + "' name='MAKER_ID" + poId + "'>"
              + "<option value=''>--请选择--</option>"
              + "</select>";
      sendAjax(url1, getMaker, 'fm');
  }

  return makerOutput;
}

function insertMaker(poId, curPartId, curMakerId) {
  if (poIdArr.length > 0) {
      for (var i = 0; i < poIdArr.length; i++) {
          if (poIdArr[i] == poId) {//如果数组里面已经包含了当前领件订单ID
              return;
          }
      }
  }
  var url1 = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryMakerInfo.json?curPartId=" + curPartId + "&poId=" + poId + "&curMakerId=" + curMakerId;
  sendAjax(url1, getMaker1, 'fm');
}

function getMaker(jsonObj) {
  var poId = jsonObj.poId;
  var arry = jsonObj.makers;
  if (arry.length > 0) {
      var obj = document.getElementById('MAKER_ID' + poId);//根据id查找对象
      for (var i = 0; i < arry.length; i++) {
          obj.options.add(new Option(arry[i].MAKER_NAME, arry[i].MAKER_ID.toString())); //兼容IE与firefox
      }
  }
}

function getMaker1(jsonObj) {
  var poId = jsonObj.poId;
  poIdArr.push(poId);
  var arry = jsonObj.makers;
  if (arry.length > 0) {
      var obj = document.getElementById('MAKER_ID' + poId);//根据id查找对象
      obj.options.length = 0;
      for (var i = 0; i < arry.length; i++) {
          obj.options.add(new Option(arry[i].MAKER_NAME, arry[i].MAKER_ID.toString())); //兼容IE与firefox
          if (jsonObj.curMakerId == arry[i].MAKER_ID) {
              obj.selectedIndex = i;
          }
      }
  }
}

//注释部分是通过供应商来获取制造商
/*function insertMaker(poId, curVenderId, curMakerId) {
  if (poIdArr.length > 0) {
      for (var i = 0; i < poIdArr.length; i++) {
          if (poIdArr[i] == poId) {//如果数组里面已经包含了当前领件订单ID
              return;
          }
      }
  }
  var url1 = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/queryMakerInfo.json?curVenderId=" + curVenderId + "&poId=" + poId + "&curMakerId=" + curMakerId;
  sendAjax(url1, getMaker1, 'fm');
}
function getMaker(jsonObj) {
  var poId = jsonObj.poId;
  var arry = jsonObj.makers;
  if (arry.length > 0) {
      var obj = document.getElementById('MAKER_ID' + poId);//根据id查找对象
      for (var i = 0; i < arry.length; i++) {
          obj.options.add(new Option(arry[i].MAKER_NAME, arry[i].MAKER_ID.toString())); //兼容IE与firefox
      }
  }
}

function getMaker1(jsonObj) {
  var poId = jsonObj.poId;
  poIdArr.push(poId);
  var arry = jsonObj.makers;
  if (arry.length > 0) {
      var obj = document.getElementById('MAKER_ID' + poId);//根据id查找对象
      obj.options.length = 0;
      for (var i = 0; i < arry.length; i++) {
          obj.options.add(new Option(arry[i].MAKER_NAME, arry[i].MAKER_ID.toString())); //兼容IE与firefox
          if (jsonObj.curMakerId == arry[i].MAKER_ID) {
              obj.selectedIndex = i;
          }
      }
  }
}*/

function selAll(obj){
	var cks = document.getElementsByName('ck');
	for(var i =0 ;i<cks.length;i++){
		if(obj.checked){
			cks[i].checked = true;
		}else{
			cks[i].checked = false;
		}
	}
}

function insertCodeInput(value,meta,record){
	var pid = record.data.POLINE_ID;
	var partId = record.data.PART_ID;
	var partOldCOde = record.data.PART_OLDCODE;
	var partCname = record.data.PART_CNAME;
	var unit = record.data.UNIT;
	var buyPrice = record.data.BUY_PRICE;
	var output;
	output = '<input type="hidden"  id="PART_CODE'+pid+'" name="PART_CODE'+pid+'" value="'+value+'"/>'+value
	+'<input type="hidden"  id="PART_ID'+pid+'" name="PART_ID'+pid+'" value="'+partId+'"/>'
	+'<input type="hidden"  id="PART_OLDCODE'+pid+'" name="PART_OLDCODE'+pid+'" value="'+partOldCOde+'"/>'
	+'<input type="hidden"  id="PART_CNAME'+pid+'" name="PART_CNAME'+pid+'" value="'+partCname+'"/>'
	+'<input type="hidden"  id="UNIT'+pid+'" name="UNIT'+pid+'" value="'+unit+'"/>'
	+'<input type="hidden"  id="BUY_PRICE'+pid+'" name="BUY_PRICE'+pid+'" value="'+buyPrice+'"/>';
	return output;
}

function insertVenderInput(value,meta,record){
	var pid = record.data.POLINE_ID;
	var venderId = record.data.VENDER_ID;
	var output;
	output = '<input type="hidden"  id="VENDER_NAME'+pid+'" name="VENDER_NAME'+pid+'" value="'+value+'"/>'+value
	+'<input type="hidden"  id="VENDER_ID'+pid+'" name="VENDER_ID'+pid+'" value="'+venderId+'"/>';
	return output;
}

function insertMakerInput(value,meta,record){
	var pid = record.data.POLINE_ID;
	var makerId = record.data.MAKER_ID;
	var partType = record.data.PART_TYPE;
	var output;
	output = '<input type="hidden"  id="MAKER_NAME'+pid+'" name="MAKER_NAME'+pid+'" value="'+value+'"/>'+value
	+'<input type="hidden"  id="MAKER_ID'+pid+'" name="MAKER_ID'+pid+'" value="'+makerId+'"/>'
	+'<input type="hidden"  id="PART_TYPE'+pid+'" name="PART_TYPE'+pid+'" value="'+partType+'"/>';
	return output;
}

function insertPlanQtyInput(value,meta,record){
	var pid = record.data.POLINE_ID;
	var output;
	output = '<input type="hidden"  id="PLAN_QTY'+pid+'" name="PLAN_QTY'+pid+'" value="'+value+'"/>'+value;
	return output;
}

function insertBuyQtyInput(value,meta,record){
	var pid = record.data.POLINE_ID;
	var output;
	output = '<input type="hidden"  id="BUY_QTY'+pid+'" name="BUY_QTY'+pid+'" value="'+value+'"/>'+value;
	return output;
}

function insertGeQtyInput(value,meta,record){
	var pid = record.data.POLINE_ID;
	var output;
	output = '<input type="text" class="short_txt"  id="GE_QTY'+pid+'" name="GE_QTY'+pid+'" value="'+value+'"/>';
	return output;
}

function insertChkQtyInput(value,meta,record){
	var pid = record.data.POLINE_ID;
	var output;
	output = '<input type="hidden"  id="CHECK_QTY'+pid+'" name="CHECK_QTY'+pid+'" value="'+value+'"/>'+value;
    return output;
}

function insertRemarkInput(value,meta,record){
	var pid = record.data.POLINE_ID;
    var output = '<input type="text" class="middle_txt" id="REMARK'+pid+'" name="REMARK'+pid+'" value="'+value+'"/>\n';
    return output;
}

//确定
function agreeApply(){
	var whManId = document.getElementById("WHMAN_ID").value;
	if ("" == whManId)
	{
		MyAlert("库管员不能为空!");
		return false;
	}
	 var pIds = document.getElementsByName("ck");
     var l = pIds.length;
     var cnt = 0;
     var makerArr = new Array();
     var venderArr = new Array();
    for (var i = 0; i < l; i++) {
        if (pIds[i].checked) {
            cnt++;
            var pattern1 = /^[1-9][0-9]*$/;
        	var geQty = document.getElementById("GE_QTY"+pIds[i].value).value;//生成数量
        	var checkQty = document.getElementById("CHECK_QTY"+pIds[i].value).value;
        	var buyQty = document.getElementById("BUY_QTY"+pIds[i].value).value;
        	var makerId = document.getElementById("MAKER_ID"+pIds[i].value).value;
        	var venderId = document.getElementById("VENDER_ID"+pIds[i].value).value;
        	if(!makerId){
            	MyAlert("请为第"+(i+1)+"行配件设置其供应商对应的制造商!");
            	return;
        	}
    	    if (!pattern1.exec(geQty)) {
    	        MyAlert("第"+(i+1)+"行，生成数量不能为空且只能输入非零的正整数!");
    	        return;
    	   }
    	   if(parseInt(geQty)>parseInt(buyQty)){
    		   MyAlert("第"+(i+1)+"行，生成数量不能大于领件数量!");
    		   return;
    	   }
    	   if(parseInt(geQty)>(parseInt(buyQty)-parseInt(checkQty))){
    		   MyAlert("第"+(i+1)+"行，生成数量不能大于领件数量与已生成数量之差!");
    		   return;
    	   }
    	 //  makerArr.push(makerId);
    	 //  venderArr.push(venderId);
        }
    }
    if (cnt == 0) {
        MyAlert("请选择配件信息！");
        return;
    }

    if(venderArr.length>1){
        for(var i=0;i<venderArr.length-1;i++){
            if(venderArr[i]!=venderArr[i+1]){
                MyAlert("所选配件的供应商必须一致,请重新选择!");
                return;
            }
        }
    }
    if(makerArr.length>1){
        for(var i=0;i<makerArr.length-1;i++){
            if(makerArr[i]!=makerArr[i+1]){
                MyAlert("所选配件的制造商必须一致,请重新选择!");
                return;
            }
        }
    }
	if(confirm("确定生成验收单?")){
        btnDisable();
		var url = '<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/generateRecvOrderChk.json?vflag=1';
		sendAjax(url,getResult,'fm');
	}   
}

function getResult(jsonObj) {
	  btnEable();
	  if(jsonObj!=null){
	     var success = jsonObj.success;
	     var error = jsonObj.error;
	     var flag = jsonObj.flag;
	     var exceptions = jsonObj.Exception;
	     if(success){
	    	 MyAlert(success);
	    	 if(flag==1){
	    		 window.location.href = '<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/partRecInit.do';
	    	 }else{
	    		 __extQuery__(1);
	    	 }
		 }else if(error){
	    	 MyAlert(error);
		 }else if(exceptions){
	    	 MyAlert(exceptions.message);
		}
			
	  }
 }

//返回查询页面
function goback(){
		window.location.href = '<%=contextPath%>/parts/purchaseOrderManager/partRecvOrderAction/partRecInit.do';
}
</script>
</div>
</body>
</html>

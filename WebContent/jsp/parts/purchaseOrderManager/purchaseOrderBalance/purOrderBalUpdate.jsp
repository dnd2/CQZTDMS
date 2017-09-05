<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    int balanceType = (Integer)request.getAttribute("balanceType");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>采购单结算修改</title>
 <script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }

        function getCheck(inId){
        	var tbl = document.getElementById('file');
        	var str =  '<input  type="checkbox" value="' + inId + '" id="cell_' + (tbl.rows.length - 2) + '" name="cb" checked="true"/>';
        	document.write(str);
        }
    </script>
</head>
<body onunload='javascript:destoryPrototype()' onload="autoAlertException();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" name="invoNo" value="${invoNo}"/>
<div class="wbox">
	<div class="navigation"> <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
	 配件管理&gt;采购计划管理&gt;采购订单结算&gt;修改
	</div>
<div>
	<table class="table_query">
	<tr>
      <th colspan="6"><img src="<%=contextPath%>/img/nav.gif" />结算单信息</th>
    </tr>
    <tr>
      <td bgcolor="#F3F4F8"   align="right">结算单号:</td>
      <td bgcolor="#FFFFFF" align="left" width="24%">&nbsp;${balanceCode}
      <input type="hidden" name="balanceCode" value="${balanceCode}"/>
      </td>
      <td   align="right" bgcolor="#F3F4F8">结算人员:</td>
      <td align="left" bgcolor="#FFFFFF" width="24%">&nbsp;${balancer}
      </td>
      <td   align="right" bgcolor="#F3F4F8">结算日期:</td>
      <td align="left" bgcolor="#FFFFFF" width="24%">&nbsp;${balanceDate}
      <input type="hidden" name="createDate" value="${createDate}"/>
      </td>
    </tr>
    <tr>
      <td bgcolor="#F3F4F8"   align="right">结算类型:</td>
      <td bgcolor="#FFFFFF" align="left" width="24%">
                <script type="text/javascript">
                    genSelBoxExp("PART_BALANCE_TYPE", <%=Constant.PART_BALANCE_TYPE %>, <%=balanceType%>, true, "short_sel", "", "false", "");
                </script>
                <font color="red">*</font>
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
           待结算明细
            <input type="button" class="normal_btn" name="addPartViv"
                   id="addPartViv" value="增加" onclick="addPartDiv()"/>
        </th>
    </LEGEND>
    <div style="display: none; heigeht: 5px" id="partDiv">
        <table class="table_query" width=100% border="0" align="center"
               cellpadding="1" cellspacing="1">
           <tr>

                <td   align="right" width="10%">
                    配件编码：
                </td>
                <td width="20%">
                    <input class="middle_txt" id="PART_OLDCODE"
                           datatype="1,is_noquotation,30" name="PART_OLDCODE"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td   align="right" width="10%">
                    配件名称：
                </td>
                <td width="22%">
                    <input class="long_txt" id="PART_CNAME"
                           datatype="1,is_noquotation,30" name="PART_CNAME"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td width="10%"   align="right">
                    件号：
                </td>
                <td width="22%">
                    <input class="long_txt" id="PART_CODE"
                           datatype="1,is_noquotation,30" name="PART_CODE"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
            </tr>
           <tr>

                <td   align="right" width="10%">
                    计划员：
                </td>
                <td width="20%">
                     <select id="PLANER_ID" name="PLANER_ID" class="short_sel">
                    <option value="">-请选择-</option>
                    <c:forEach items="${planerList}" var="planerList">
                      <c:choose>
						<c:when test="${curUserId eq planerList.USER_ID}">
						  <option selected="selected" value="${planerList.USER_ID }" >${planerList.USER_NAME }</option>
						</c:when>
						<c:otherwise>
						  <option value="${planerList.USER_ID }" >${planerList.USER_NAME }</option>
						</c:otherwise>
					  </c:choose>
                    </c:forEach>
                </select>
                </td>
                <td width="10%"   align="right">
                    验收日期：
                </td>
                <td width="22%">
                    <input name="beginTime" id="beginTime" value="" type="text" class="short_txt" datatype="1,is_date,10"
                       group="beginTime,endTime">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                       onclick="showcalendar(event, 'beginTime', false);"/>
                至
                <input name="endTime" id="endTime" value="" type="text" class="short_txt" datatype="1,is_date,10"
                       group="beginTime,endTime">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                       onclick="showcalendar(event, 'endTime', false);"/>
                </td>
                <td   align="right" width="10%">
                    供应商：
                </td>
                <td width="22%">
                    <input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME"/>
                <input class="mark_btn" type="button" value="&hellip;"
                       onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
                <INPUT class="short_btn" onclick="clearInput();" value=清除 type=button name=clrBtn>
                <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
                </td>
            </tr>
            <tr>
             <td width="10%"   align="right">
                    来源：
                </td>
                <td width="20%">
                    <script type="text/javascript">
                    genSelBoxExp("ORIGIN_TYPE", <%=Constant.ORDER_ORIGIN_TYPE %>, "", true, "short_sel", "", "false", "");
                    </script>
                </td>
                <td width="10%"   align="right">
                    入库时间：
                </td>
                <td width="22%">
                    <input name="inSDate" id="inSDate" value="" type="text" class="short_txt" datatype="1,is_date,10"
                       group="inSDate,inEDate">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                       onclick="showcalendar(event, 'inSDate', false);"/>
                至
                <input name="inEDate" id="inEDate" value="" type="text" class="short_txt" datatype="1,is_date,10"
                       group="inSDate,inEDate">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                       onclick="showcalendar(event, 'inEDate', false);"/>
                </td>
                 <td   align="right" width="10%">
                   制造商名称：
                </td>
                <td width="20%">
                    <input class="middle_txt" type="text" readonly="readonly" id="MAKER_NAME" name="MAKER_NAME"/>
                    <input class="mark_btn" type="button" value="&hellip;"
                           onclick="showPartMaker1('MAKER_NAME','MAKER_ID','false')"/>
                           <INPUT class=short_btn onclick="clearMInput();" value=清除 type=button name=clrBtn>
                    <input id="MAKER_ID" name="MAKER_ID" type="hidden" value="">
                </td>
            </tr>
            <tr>
				<td   align="right" width="10%">
                   验收单号：
                </td>
                <td width="20%">
                    <input class="middle_txt" id="CHK_CODE"
                           datatype="1,is_noquotation,30" name="CHK_CODE"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td   align="right" width="10%">
                    入库单号：
                </td>
                <td width="20%">
                    <input class="long_txt" id="inCodeS" name="inCodeS" type="text"/>
                </td>
                <td   align="right" width="10%">
                     订单单号：
                </td>
                <td width="22%">
                    <input class="long_txt" id="orderCodeS" name="orderCodeS" type="text"/>
                </td>
            </tr>
            
            <tr>
				<td   align="right" width="15%">
                   制造商与供应商是否相同：
                </td>
                <td width="20%">
                    <select id="isSeam" name="isSeam" class="short_sel">
                    <option value="">-请选择-</option>
                    <option value="1">是</option>
                    <option value="0">否</option>
                </select>
                </td>
            </tr>
            
            <tr>
                <td align="center" colspan="6">
                    <input class="normal_btn" type="button" name="BtnQuery"
                           id="queryBtn" value="查 询" onclick="__extQuery__(1)"/>
                    <input class="normal_btn" type="button" name="BtnQuery"
                           id="queryBtn2" value="添加" onclick="addCells()"/>
                           <font color="red">(注:请选择供应商相同的明细添加)</font>
                </td>
            </tr>
            
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</FIELDSET>
<table id="file" class="table_list" style="border-bottom: 1px;">
    <tr>
        <th colspan="18" align="left">
            <img src="<%=contextPath%>/img/nav.gif"/>详细信息
    </tr>
     <tr  bgcolor="#FFFFCC">
        <td>
            <input type="checkbox" onclick="selAll2(this)" checked/>
        </td>
        <td>
            序号
        </td>
        <td>
            验收单号
        </td>
        <td>
            配件编码
        </td>
        <td>
            配件名称
        </td>
        <td>
            件号
        </td>
        <td>
            单位
        </td>
        <td>
            供应商名称
        </td>
        <td>
            制造商名称
        </td>

         <td>
            结算数量
        </td>
        <td>
            入库数量
        </td>
        <td>
            退货数量
        </td>
        <td>
            已结算数量
        </td>
        <td>
            备注
        </td>
         <td>
             入库单号
         </td>
         <td>
             订单单号
         </td>
        <td>
            入库时间
        </td>
        <td>
            验收日期
        </td>
        <td>
            操作
        </td>
    </tr>
    
     <c:forEach items="${detailList}" var="data" varStatus="_sequenceNum" step="1">
            <c:if test="${((_sequenceNum.index+1) mod 2) != 0}">
							<tr class="table_list_row1">
			</c:if>
			<c:if test="${((_sequenceNum.index+1) mod 2) == 0}">
			<tr class="table_list_row2">
			</c:if>
   			  <td align="center" nowrap>
   			  <script type="text/javascript">
   			  	getCheck(${data.IN_ID});
   			  </script>
   			  </td>
   			   <td align="center">
   			   <span id="orderLine_SEQ" >${_sequenceNum.index+1}</span>
   			  </td>
		      <td style="text-align: left"><input   name="CHK_CODE${data.IN_ID}" id="CHK_CODE${data.IN_ID}" value="${data.CHECK_CODE}" type="hidden" />${data.CHECK_CODE}</td>
			  <td style="text-align: left"><input   name="PART_OLDCODE${data.IN_ID}" id="PART_OLDCODE${data.IN_ID}" value="${data.PART_OLDCODE}" type="hidden" />${data.PART_OLDCODE}</td>
			  <td style="text-align: left"><input   name="PART_CNAME${data.IN_ID}" id="PART_CNAME${data.IN_ID}" value="${data.PART_CNAME}" type="hidden" />${data.PART_CNAME}</td>
			  <td style="text-align: left"><input   name="PART_CODE${data.IN_ID}" id="PART_CODE${data.IN_ID}" value="${data.PART_CODE}" type="hidden" />${data.PART_CODE}</td>
			  <td align="center" nowrap><input   name="UNIT${data.IN_ID}" id="UNIT${data.IN_ID}" value="${data.UNIT}" type="hidden" />${data.UNIT}</td>
			  <td style="text-align: left"><input   name="VENDER_NAME1" id="VENDER_NAME1${data.IN_ID}" value="${data.VENDER_NAME}" type="hidden" />${data.VENDER_NAME}</td>
			  <td style="text-align: left"><input   name="MAKER_NAME1" id="MAKER_NAME1${data.IN_ID}" value="${data.MAKER_NAME}" type="hidden" />${data.MAKER_NAME}</td>

			  <td align="center" nowrap><input   name="BAL_QTY1${data.IN_ID}" id="BAL_QTY1${data.IN_ID}" value="${data.BAL_QTY}" type="text" class="short_txt" /></td>
			  <td align="center" nowrap><input   name="IN_QTY${data.IN_ID}" id="IN_QTY${data.IN_ID}" value="${data.IN_QTY}" type="hidden" />${data.IN_QTY}</td>
			  <td align="center" nowrap><input   name="RE_QTY${data.IN_ID}" id="RE_QTY${data.IN_ID}" value="${data.RETURN_QTY}" type="hidden" />${data.RETURN_QTY}</td>
			  <td align="center" nowrap><input   name="BALED_QTY${data.IN_ID}" id="BALED_QTY${data.IN_ID}" value="${data.BALED_QTY}" type="hidden" />${data.BALED_QTY}</td>
			  <td style="text-align: left"><input class="short_txt"  name="REMARK${data.IN_ID}" id="REMARK${data.IN_ID}" value="${data.REMARK}" type="text"/></td>
              <td style="text-align: left"><input   name="IN_CODE${data.IN_ID}" id="IN_CODE${data.IN_ID}" value="${data.IN_CODE}" type="hidden" />${data.IN_CODE}</td>
              <td style="text-align: left"><input   name="ORDER_CODE${data.IN_ID}" id="ORDER_CODE${data.IN_ID}" value="${data.IN_CODE}" type="hidden" />${data.ORDER_CODE}</td>
			  <td align="center" nowrap><input   name="IN_DATE${data.IN_ID}" id="IN_DATE${data.IN_ID}" value="${data.IN_DATE}" type="hidden" />${data.IN_DATE}</td>
			  <td align="center" nowrap><input   name="CHECK_DATE${data.IN_ID}" id="CHECK_DATE${data.IN_ID}" value="${data.CHECK_DATE}" type="hidden" />${data.CHECK_DATE}</td>
			  <td><input  type="button" class="short_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" /></td>
		   </tr>
	</c:forEach>
	
</table>
<table border="0" class="table_query">
  <tr align="center">
  <td>
  <input name="agreeBtn" id="agreeBtn"  class="normal_btn" type="button" value="保 存" onclick="agreeApply();"/>
    &nbsp;
  <input class="normal_btn" type="button" value="返 回" onclick="javascript:goback();"/>&nbsp;</td>
  </tr>
  </table>
  </div>
</form>
<script type="text/javascript" ><!--
var myPage;

var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/queryPurOrderInInfo.json";

var title = null;

var columns = [
				{header: "序号", align:'center',renderer:getIndex},
				{header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'IN_ID', align:'center',width: '33px' ,renderer:seled},
				{header: "验收单号", dataIndex: 'CHECK_CODE', style: 'text-align:left'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
				{header: "配件名称", dataIndex: 'PART_CNAME',style: 'text-align:left'},
               {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
				{header: "制造商名称", dataIndex: 'MAKER_NAME', style: 'text-align:left'},
				{header: "结算数量", dataIndex: 'SPARE_QTY', align:'center',renderer:insertBalQtyInput},
				{header: "入库数量", dataIndex: 'IN_QTY', align:'center',renderer:insertInQtyInput},
				{header: "退货数量", dataIndex: 'RETURN_QTY', align:'center',renderer:insertReQtyInput},
				{header: "已结算数量", dataIndex: 'BAL_QTY', align:'center',renderer:insertBaledQtyInput},
				{header: "备注", dataIndex: 'REMARK', align:'center',renderer:insertRemarkInput},
                {header: "入库单号", dataIndex: 'IN_CODE', style: 'text-align:left'},
                {header: "订单单号", dataIndex: 'ORDER_CODE', style: 'text-align:left'},
				{header: "入库时间", dataIndex: 'IN_DATE', align:'center'},
				{header: "验收日期", dataIndex: 'CREATE_DATE', align:'center'}
		      ];


function seled(value,meta,record)
{
	 return "<input type='checkbox' value='"+value+"' name='ck' id='ck"+value+"' onclick='chkPart()'/>";
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

function insertBalQtyInput(value,meta,record){
	var inId = record.data.IN_ID;
	var inQty = record.data.IN_QTY;
	var reQty = record.data.RETURN_QTY;
	var baledQty = record.data.BAL_QTY;
	var output;
	output = '<input type="text" class="short_txt" onchange="check(this,'+inId+','+inQty+','+reQty+','+baledQty+');"  id="BAL_QTY'+inId+'" name="BAL_QTY'+inId+'" value="'+value+'" onkeydown="toNext('+inId+')"/>';
	return output;
}

function insertBaledQtyInput(value,meta,record){
	var inId = record.data.IN_ID;
	var output;
	output = '<input type="hidden"  id="BALED_QTY1'+inId+'" name="BALED_QTY1'+inId+'" value="'+value+'"/>'+value;
   return output;
}

function toNext(inId){
	var ck = document.getElementsByName("ck");
	var obj = $("BAL_QTY"+inId);
	var idx = obj.parentElement.parentElement.rowIndex-1;
	if(event.keyCode==40){
		var balQty = obj.value;
		var pattern1 = /^[1-9][0-9]*$/;
       if (!pattern1.exec(balQty)) {
       	obj.value = obj.value.replace(/\D/g, '');
       }
       $("ck"+inId).checked=true;
       chkPart();
		 if(ck[idx+1]&&$("BAL_QTY"+(ck[idx+1].value))){
			 var val = $("BAL_QTY"+(ck[idx+1].value)).value;
			 $("BAL_QTY"+(ck[idx+1].value)).focus();
			 $("BAL_QTY"+(ck[idx+1].value)).value="";
			 $("BAL_QTY"+(ck[idx+1].value)).value=val;
		 }
	}
	if(event.keyCode==38){
		 if(ck[idx-1]&&$("BAL_QTY"+(ck[idx-1].value))){
			 var val = $("BAL_QTY"+(ck[idx-1].value)).value;
			 $("BAL_QTY"+(ck[idx-1].value)).focus();
			 $("BAL_QTY"+(ck[idx-1].value)).value="";
			 $("BAL_QTY"+(ck[idx-1].value)).value=val;
		 }
	}
}

function check(value,inId,inQty,reQty,baledQty) {
	var pattern1 = /^[1-9][0-9]*$/;
   if (!pattern1.exec($(value).value)) {
       //MyAlert("请录入正整数且必须大于0！");
       $(value).value = $(value).value.replace(/\D/g, '');
       $(value).focus();
   }
   if (isNumber($(value).value)) {
       if ($(value).value == 0) {
           MyAlert("结算数量是正整数且必须大于0！");
           $(value).value = "";
           $(value).focus();
           return;
       }

   }
   var balQty = $(value).value;
   if(parseInt(balQty)>(parseInt(inQty)-parseInt(reQty)-parseInt(baledQty))){
       MyAlert("结算数量不能大于入库数量与退货数量、已结算之差!");
       $(value).value = "";
       $(value).focus();
       return;
   }
   $("ck"+inId).checked=true;
   chkPart();
}

function insertInQtyInput(value,meta,record){
	var inId = record.data.IN_ID;
	var output;
	output = '<input type="hidden"  id="IN_QTY1'+inId+'" name="IN_QTY1'+inId+'" value="'+value+'"/>'+value;
   return output;
}

function insertReQtyInput(value,meta,record){
	var inId = record.data.IN_ID;
	var output;
	output = '<input type="hidden"  id="RE_QTY1'+inId+'" name="RE_QTY1'+inId+'" value="'+value+'"/>'+value;
   return output;
}

function insertRemarkInput(value,meta,record){
	var pid = record.data.POLINE_ID;
   var output = '<input type="text" class="middle_txt" id="REMARK1'+pid+'" name="REMARK1'+pid+'" value="'+value+'"/>\n';
   return output;
}

//结算
function agreeApply(){
	var part_balance_type = $("PART_BALANCE_TYPE").value;
    if(!part_balance_type){
    	MyAlert("请选择结算类型!");
    	return;
    }
	 var inIds = document.getElementsByName("cb");
    var l = inIds.length;
    var cnt = 0;
   for (var i = 0; i < l; i++) {
       if (inIds[i].checked) {
           cnt++;
           var pattern1 = /^[1-9][0-9]*$/;
       	var balQty = document.getElementById("BAL_QTY1"+inIds[i].value).value;//结算数量
       	var inQty = document.getElementById("IN_QTY"+inIds[i].value).value;//入库数量
       	var reQty = document.getElementById("RE_QTY"+inIds[i].value).value;//退货数量
       	var baledQty = document.getElementById("BALED_QTY"+inIds[i].value).value;//已结算数量
       	
   	    if (!pattern1.exec(balQty)) {
   	        MyAlert("第"+(i+1)+"行，结算数量不能为空且只能输入非零的正整数!");
   	        return;
   	    }
   	   
   	   if(parseInt(balQty)>(parseInt(inQty)-parseInt(reQty)-parseInt(baledQty))){
   		   MyAlert("第"+(i+1)+"行，结算数量不能大于入库数量与退货数量、已结算数量之差!");
   		   return;
   	    }
       }
   }
   if (cnt == 0) {
       MyAlert("请选择详细信息！");
       return;
   }

	if(confirm("确定保存修改?")){
       btnDisable();
       var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/updateBalanceOrder.json';
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
    	   window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purchaseOrderBalanceQueryInit.do";
		 }else if(error){
	    	 MyAlert(error);
		 }else if(exceptions){
	    	 MyAlert(exceptions.message);
		}

	  }
 }

function addPartDiv() {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    var part_balance_type = $("PART_BALANCE_TYPE").value;
    if(!part_balance_type){
    	MyAlert("请选择结算类型!");
    	return;
    }
    if (partDiv.style.display == "block") {
        addPartViv.value = "增加";
        partDiv.style.display = "none";
    } else {
        addPartViv.value = "收起";
        partDiv.style.display = "block";
        __extQuery__(1);
    }
}

function addCells() {

    var ck = document.getElementsByName('ck');
    var mt = document.getElementById("myTable");
    var cn = 0;
    for (var i = 1; i < mt.rows.length; i++) {
        if (mt.rows[i].cells[1].childNodes[0].checked) {
        	cn++;
        	var pQty = mt.rows[i].cells[9].childNodes[0].value;
        	var pattern1 = /^[1-9][0-9]*$/;
            if (!pattern1.exec(pQty)) {
                MyAlert("第" + i + "行配件：" + mt.rows[i].cells[5].innerText + " 结算数量必须是大于0的整数!</r>");
                break;
            }
            var inId = mt.rows[i].cells[1].childNodes[0].value;  //ID
            if (validateCell(inId)) {
            	var venderName = mt.rows[i].cells[7].innerText;  //供应商名称
            	if(validateVenderName(venderName)){
            		 var chkCode = mt.rows[i].cells[2].innerText;  //验收单号
                     var partOldcode = mt.rows[i].cells[3].innerText;  //配件编码
                     var partCname = mt.rows[i].cells[4].innerText;  //配件名称
                     var partCode = mt.rows[i].cells[5].innerText;  //件号
                     var unit = mt.rows[i].cells[6].innerText;  //单位
                     var makerName = mt.rows[i].cells[8].innerText;  //制造商名称
                     var balQty = mt.rows[i].cells[9].childNodes[0].value;  //结算数量
                     var inQty = mt.rows[i].cells[10].innerText;  //入库数量
                     var reQty = mt.rows[i].cells[11].innerText;  //退货数量
                     var baledQty = mt.rows[i].cells[12].innerText;  //已结算数量
                     var remark = mt.rows[i].cells[13].childNodes[0].value;  //备注
                    var inCode = mt.rows[i].cells[14].innerText;  //入库单号
                    var orderCode = mt.rows[i].cells[15].innerText;  //订单单号
                     var inDate = mt.rows[i].cells[16].innerText;  //入库时间
                     var checkDate = mt.rows[i].cells[17].innerText;  //验收日期

                     if(parseInt(balQty)>(parseInt(inQty)-parseInt(reQty)-parseInt(baledQty))){
                     	 MyAlert("第" + i + "行配件：" + mt.rows[i].cells[5].innerText + "结算数量不能大于入库数量与退货数量、已结算数量之差!</br>");
                     	 mt.rows[i].cells[1].firstChild.checked = false;
                         return;
                     }
                     
                     addCell(inId,chkCode,partOldcode,partCname,partCode,unit,venderName,makerName,inCode,orderCode,balQty,inQty,reQty,baledQty,remark,inDate,checkDate);
            	}else{
            		MyAlert("请添加同一个供应商下的配件!");
                    mt.rows[i].cells[1].firstChild.checked=false;
                    break;
            	}
               
            } else {
                MyAlert("第" + i + "行配件：" + mt.rows[i].cells[5].innerText + " 已存在!</br>");
                mt.rows[i].cells[1].firstChild.checked = false;
                break;
            }
        }
    }
    if(cn==0){
    	MyAlert("请选择要添加的配件!");
    }
}

function validateCell(value) {
    var flag = true;
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
    	var val = cb[i].value;
        if (value == val) {
            flag = false;
            break;
        }
    }
    return flag;
}

function validateVenderName(venderName)
{
	var venderNames = document.getElementsByName("VENDER_NAME1");
    if (venderNames && venderNames.length > 0) {
        for (var i = 0; i < venderNames.length; i++) {
            if (venderName != venderNames[i].value) {
                return false;
            }
        }
        return true;
    }
    return true;
}

function addCell(inId,chkCode,partOldcode,partCname,partCode,unit,venderName,makerName,inCode,orderCode,balQty,inQty,reQty,baledQty,remark,inDate,checkDate) {
    var tbl = document.getElementById('file');
    var rowObj = tbl.insertRow(tbl.rows.length);
    if (tbl.rows.length % 2 == 0) {
        rowObj.className = "table_list_row2";
    } else {
        rowObj.className = "table_list_row1";
    }
    var cell1 = rowObj.insertCell(0);
    var cell2 = rowObj.insertCell(1);
    var cell3 = rowObj.insertCell(2);
    var cell4 = rowObj.insertCell(3);
    var cell5 = rowObj.insertCell(4);
    var cell6 = rowObj.insertCell(5);
    var cell7 = rowObj.insertCell(6);
    var cell8 = rowObj.insertCell(7);
    var cell9 = rowObj.insertCell(8);
    var cell10 = rowObj.insertCell(9);
    var cell11 = rowObj.insertCell(10);
    var cell12 = rowObj.insertCell(11);
    var cell13 = rowObj.insertCell(12);
    var cell14 = rowObj.insertCell(13);
    var cell15 = rowObj.insertCell(14);
    var cell16 = rowObj.insertCell(15);
    var cell17 = rowObj.insertCell(16);
    var cell18 = rowObj.insertCell(17);
    var cell19 = rowObj.insertCell(18);
    
    cell1.innerHTML = '<tr><td align="center" nowrap><input  type="checkbox" value="' + inId + '" id="cell_' + (tbl.rows.length - 2) + '" name="cb" checked="true"/></td>';
    cell2.innerHTML = '<td align="center" nowrap><span id="orderLine_SEQ" >' + (tbl.rows.length - 2) + '</span><input id="idx_' + inId + '" name="idx_' + inId + '" value="' + (tbl.rows.length - 2) + '" type="hidden" ></td>';
    cell3.innerHTML = '<td style="text-align: left"><input   name="CHK_CODE' + inId + '" id="CHK_CODE' + inId + '" value="' + chkCode + '" type="hidden" />' + chkCode + '</td>';
    cell4.innerHTML = '<td style="text-align: left"><input   name="PART_OLDCODE' + inId + '" id="PART_OLDCODE' + inId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    cell5.innerHTML = '<td style="text-align: left"><input   name="PART_CNAME' + inId + '" id="PART_CNAME' + inId + '" value="' + partCname + '" type="hidden" />' + partCname + '</td>';
    cell6.innerHTML = '<td style="text-align: left"><input   name="PART_CODE' + inId + '" id="PART_CODE' + inId + '" value="' + partCode + '" type="hidden" />' + partCode + '</td>';
    cell7.innerHTML = '<td align="center" nowrap><input   name="UNIT' + inId + '" id="UNIT' + inId + '" value="' + unit + '" type="hidden" />' + unit + '</td>';
    cell8.innerHTML = '<td align="center" nowrap><input   name="VENDER_NAME1" id="VENDER_NAME1' + inId + '" value="' + venderName + '" type="hidden" />' + venderName + '</td>';
    cell9.innerHTML = '<td align="center" nowrap><input   name="MAKER_NAME1" id="MAKER_NAME1' + inId + '" value="' + makerName + '" type="hidden" />' + makerName + '</td>';
    cell10.innerHTML = '<td align="center" nowrap><input   name="BAL_QTY1' + inId + '" id="BAL_QTY1' + inId + '" value="' + balQty + '" type="text" class="short_txt" /></td>';
    cell11.innerHTML = '<td align="center" nowrap><input   name="IN_QTY' + inId + '" id="IN_QTY' + inId + '" value="' + inQty + '" type="hidden" />' + inQty + '</td>';
    cell12.innerHTML = '<td align="center" nowrap><input   name="RE_QTY' + inId + '" id="RE_QTY' + inId + '" value="' + reQty + '" type="hidden" />' + reQty + '</td>';
    cell13.innerHTML = '<td align="center" nowrap><input   name="BALED_QTY' + inId + '" id="BALED_QTY' + inId + '" value="' + baledQty + '" type="hidden" />' + baledQty + '</td>';
    cell14.innerHTML = '<td align="center" nowrap><input class="short_txt"  name="REMARK' + inId + '" id="REMARK' + inId + '" value="' + remark + '" type="text"/></td>';
    cell15.innerHTML = '<td style="text-align: left"><input   name="IN_CODE' + inId + '" id="IN_CODE' + inId + '" value="' + inCode + '" type="hidden" />' + inCode + '</td>';
    cell16.innerHTML = '<td style="text-align: left"><input   name="ORDER_CODE' + inId + '" id="ORDER_CODE' + inId + '" value="' + orderCode + '" type="hidden" />' + orderCode + '</td>';
    cell17.innerHTML = '<td align="center" nowrap><input   name="IN_DATE' + inId + '" id="IN_DATE' + inId + '" value="' + inDate + '" type="hidden" />' + inDate + '</td>';
    cell18.innerHTML = '<td align="center" nowrap><input   name="CKECK_DATE' + inId + '" id="CKECK_DATE' + inId + '" value="' + checkDate + '" type="hidden" />' + checkDate + '</td>';
    cell19.innerHTML = '<td><input  type="button" class="short_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" /></td></TR>';

}

Array.prototype.indexOf = function(val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) return i;
    }
    return -1;
}

function deleteTblRow(obj) {
    var idx = obj.parentElement.parentElement.rowIndex;
    var tbl = document.getElementById('file');
    tbl.deleteRow(idx);
    refreshMtTable('orderLine', 'SEQ');//刷新行号
}

function delteTab(tab){
    var tb = document.getElementById(tab);
    var rowNum=tb.rows.length;
    for (i=2;i<rowNum;i++)
    {
        tb.deleteRow(i);
        rowNum=rowNum-1;
        i=i-1;
    }
}
//刷新行号
function refreshMtTable(mtId, strType) {
    if (strType == "SEQ") {
        var oSeq = eval("document.all." + mtId + "_SEQ");
        if (oSeq != null && oSeq.length != null) {
            for (var i = 0; i < oSeq.length; i++) {
                oSeq[i].innerText = (i + 1);
            }
        }
    }
}

function selAll2(obj) {
    var cb = document.getElementsByName('cb');
    for (var i = 0; i < cb.length; i++) {
        if (obj.checked) {
            cb[i].checked = true;
        } else {
            cb[i].checked = false;
        }
    }
}

function showUpload() {
    var uploadDiv = $("uploadDiv");
    if (uploadDiv.style.display == "block") {
        uploadDiv.style.display = "none";
    } else {
        uploadDiv.style.display = "block";
    }
}

function exportExcelTemplate() {
    fm.action = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/exportExcelTemplate.do";
    fm.submit();
}

function uploadExcel(){
	fm.action = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/uploadPartPlanExcel.do?"
	fm.submit();
}

//清空选定供应商
function clearInput() {
    document.getElementById("VENDER_ID").value = '';
    document.getElementById("VENDER_NAME").value = '';
}

function clearMInput() {//清空选定制造商
	var makerId = document.getElementById("MAKER_ID").value;
	if(makerId!=null&&makerId!=""){
		 document.getElementById("MAKER_ID").value = '';
	     document.getElementById("MAKER_NAME").value = '';
	}
}

function showPartMaker1(inputCode ,inputId ,isMulti ){
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	OpenHtmlWindow("<%=contextPath%>/jsp/parts/purchaseOrderManager/purchaseOrderBalance/makerSelect.jsp?INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,390);
}

//返回查询页面
function goback(){
	window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purchaseOrderBalanceQueryInit.do";
}
--></script>
</div>
</body>
</html>

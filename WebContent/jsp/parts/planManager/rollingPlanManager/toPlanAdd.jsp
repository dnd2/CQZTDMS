<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
    request.setAttribute("wareHouseList", request.getAttribute("wareHouseList"));
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<TITLE>计划新增</TITLE>
<style>table.form-table{background-color: transparent}input.mini_btn{min-width:50px}</style>
<script language="JavaScript">
        //初始化方法
        $(document).ready(function(){
            refeshQtyAndAmount();
        	});

        Date.prototype.format = function(format)
        {
         var o = {
         "M+" : this.getMonth()+1, //month
         "d+" : this.getDate(),    //day
         "h+" : this.getHours(),   //hour
         "m+" : this.getMinutes(), //minute
         "s+" : this.getSeconds(), //second
         "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
         "S" : this.getMilliseconds() //millisecond
         }
         if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
         (this.getFullYear()+"").substr(4 - RegExp.$1.length));
         for(var k in o)if(new RegExp("("+ k +")").test(format))
         format = format.replace(RegExp.$1,
         RegExp.$1.length==1 ? o[k] :
         ("00"+ o[k]).substr((""+ o[k]).length));
         return format;
        }
       
    </script>
<SCRIPT type=text/javascript>

var myPage;

var url = "<%=contextPath%>/parts/planManager/PartPlanManager/showPartBase.json";

var title = null;

var columns = [
    {header: "序号", style: 'text-align:left', renderer: getIndex, width: '7%'},
    {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', style: 'text-align:left', renderer: seled},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "配件属性", dataIndex: 'PRODUCE_STATE', style: 'text-align:left',renderer: getItemValue},
    {header: "单位", dataIndex: 'UNIT', style: 'text-align:center'},
    {header: "供应商", dataIndex: 'VENDER_NAME', style: 'text-align:left', renderer: inPutVender},
    {header: "库存数量", dataIndex: 'QTY', style: 'text-align:center'},
    {header: "最小包装量", dataIndex: 'MIN_PACKAGE', style: 'text-align:center'},
    {header: "计划数量", dataIndex: 'PLAN_QTY', style: 'text-align:right', renderer: inPutPlanQty},
    {header: "预计到货日期", dataIndex: 'DELIVER_PERIOD', style: 'text-align:center'}
];

function seled(value, meta, record) {
	var planQty = record.data.PLAN_QTY;
	if(planQty>0){
		return "<input type='checkbox' value='" + value + "' name='ck' id='ck"+value+"' onclick='refeshQtyAndAmount();' checked/>";
	}else{
		return "<input type='checkbox' value='" + value + "' name='ck' id='ck"+value+"' onclick='refeshQtyAndAmount();'/>";
	}
}

function inPutVender(value, meta, record) {
	var partId = record.data.PART_ID;
	var venderId = record.data.VENDER_ID;
	var partType = record.data.PART_TYPE;
    return "<input type='hidden' name='venderInput' id='vender"+partId+"' value='" + venderId + "'/>"+value
    +"<input type='hidden' name='typeInput' id='partType"+partId+"' value='" + partType + "'/>";

}

function inPutPlanQty(value, meta, record) {
	var num = record.data.R_NUM;
	var partId = record.data.PART_ID;
    return "<input type='input' name='planQtyInput' id='PQty"+num+"' value='" + value + "' onchange='check(this,"+partId+");' maxlength='8' onkeydown='toNext("+num+","+partId+")'/>";

}

function toNext(num,partId1){
	if(event.keyCode==40){
		var flag = true;
		var planQty = $("#PQty"+(num))[0].value;
		var pattern1 = /^[1-9][0-9]*$/;
        if (!pattern1.exec(planQty)) {
        	flag=false;
        }
		 if(planQty>0){
			 var mt = document.getElementById("myTable");
			 for (var i = 1; i < mt.rows.length; i++) {
				 var partId = mt.rows[i].cells[1].childNodes[0].value;  //ID
				 if (validateCell(partId)&&partId==partId1&&flag) {
					 var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
		                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
		                var partCode = mt.rows[i].cells[4].innerText;  //件号
		                var partType = $("#partType"+partId)[0].value;//配件属性
		                var partTypeName = mt.rows[i].cells[5].innerText;  //配件属性名称
		                var unit = mt.rows[i].cells[6].innerText;  //单位
		                var venderId = $("#vender"+partId)[0].value;  //供应商id
		                var venderName = mt.rows[i].cells[7].innerText;  //供应商名称
		                var whQty = mt.rows[i].cells[8].innerText;  //库存数量
		                var minPackage = mt.rows[i].cells[9].innerText;  //最小包装量
		                //var salePrice3 = mt.rows[i].cells[9].innerText;  //计划价
		                var planQty = mt.rows[i].cells[10].childNodes[0].value;  //计划数量
		                //var buyPrice = mt.rows[i].cells[11].innerText;  //采购价格
		                //var itemQty = mt.rows[i].cells[12].innerText;  //可用库存量		            
		                var preArriveDate = mt.rows[i].cells[11].innerText;  //到货日期
		                addCell(partId,partCode, partOldcode, partCname,partType,partTypeName, unit,venderId,venderName, minPackage, planQty, preArriveDate);
		                refeshQtyAndAmount();
		                break;
		            }
			 }
			 
		 }
		 if($("#PQty"+(num+1))[0]){
			 var val = $("#PQty"+(num+1))[0].value;
			 $("#PQty"+(num+1)).focus();
			 $("#PQty"+(num+1))[0].value="";
			 $("#PQty"+(num+1))[0].value=val;
		 }
	}
	if(event.keyCode==38){
		 if($("#PQty"+(num-1))[0]){
			 var val = $("#PQty"+(num-1))[0].value;
			 $("#PQty"+(num-1)).focus();
			 $("#PQty"+(num-1))[0].value="";
			 $("#PQty"+(num-1))[0].value=val;
		 }
	}
}

function toNext1(partId){
	 var cb = document.getElementsByName("cb");
	 var obj = $("#plan_qty_"+partId)[0];
	 var idx = obj.parentElement.parentElement.rowIndex-1;
	 if(event.keyCode==40){
		 var flag = true;
		 if(obj.value==0){//如果输入0,就删除这行
		    	deleteTblRow(obj);
		    	flag = false;
		 }
		 if(flag&&cb[idx]&&$("plan_qty_"+(cb[idx].value))){
			 var val = $("#plan_qty_"+(cb[idx].value))[0].value;
			 $("#plan_qty_"+(cb[idx].value)).focus();
			 $("#plan_qty_"+(cb[idx].value))[0].value="";
			 $("#plan_qty_"+(cb[idx].value))[0].value=val;
		 }
		 var cb1 = document.getElementsByName("cb");
		 if(!flag&&cb1[idx-1]&&$("plan_qty_"+(cb1[idx-1].value))){
			 var val = $("plan_qty_"+(cb1[idx-1].value)).value;
			 setTimeout("$('plan_qty_'+"+(cb1[idx-1].value)+").focus();$('plan_qty_'+"+(cb1[idx-1].value)+").value='';$('plan_qty_'+"+(cb1[idx-1].value)+").value="+val+";",50);
			 //$("plan_qty_"+(cb1[idx-1].value)).value="";
			 //$("plan_qty_"+(cb1[idx-1].value)).value=val;
		 }
	}
	if(event.keyCode==38){
		 if(cb[idx-2]&&$("plan_qty_"+(cb[idx-2].value))){
			 var val = $("#plan_qty_"+(cb[idx-2].value))[0].value;
			 $("#plan_qty_"+(cb[idx-2].value)).focus();
			 $("#plan_qty_"+(cb[idx-2].value))[0].value="";
			 $("#plan_qty_"+(cb[idx-2].value))[0].value=val;
		 }
	}	 
}

function check(value,partId) {
	var pattern1 = /^[1-9][0-9]*$/;
    if (!pattern1.exec($(value).value)) {
        //MyAlert("请录入正整数且必须大于0！");
        $(value).value = $(value).value.replace(/\D/g, '');
        $(value).focus();
    }
    if (isNumber($(value).value)) {
        if ($(value).value == 0) {
            MyAlert("数量是正整数且必须大于0！");
            $(value).value = "";
            $(value).focus();
            return;
        }

    }
    $("ck"+partId).checked=true;
}

function selAll(obj) {
    var cks = document.getElementsByName('ck');
    for (var i = 0; i < cks.length; i++) {
        if (obj.checked) {
            cks[i].checked = true;
        } else {
            cks[i].checked = false;
        }
        refeshQtyAndAmount();
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
        refeshQtyAndAmount();
    }
}
function getYearSelect(id, name, scope, value) {
    var date = new Date();
    var year = date.getFullYear();    //获取完整的年份
    var str = "";
    str += "<select  id='" + id + "' name='" + name + "'  style='width:60px;'>";
    str += "<option selected value=''>-请选择-</option>";
    for (var i = (year - scope); i <= (year + scope); i++) {
        if (value == "") {
            if (i == year) {
                str += "<option  selected value =" + i + ">" + i + "</option >";
            } else {
                str += "<option   value =" + i + ">" + i + "</option >";
            }
        } else {
            str += "<option  " + (i == value ? "selected" : "") + "value =" + i + ">" + i + "</option >";
        }
    }
    str += "</select> 年";
    document.write(str);
}
function getMonThSelect(id, name, value) {
    var date = new Date();
    var month = date.getMonth() + 2;
    var str = "";
    str += "<select  id='" + id + "' name='" + name + "'  style='width:60px;' class='u-select'>";
    str += "<option selected value=''>-请选择-</option>";
    for (var i = 1; i <= 12; i++) {
        if (value == "") {
            if (i == month) {
                str += "<option selected value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
            } else {
                str += "<option  value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
            }
        } else {
            str += "<option " + (i == value ? "selected" : "") + "value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
        }
    }
    str += "</select> 月";
    document.write(str);
}

function addCells() {

    var ck = document.getElementsByName('ck');
    var mt = document.getElementById("myTable");
    var cn = 0;
    for (var i = 1; i < mt.rows.length; i++) {
        if (mt.rows[i].cells[1].childNodes[0].checked) {
        	cn++;
        	var pQty = mt.rows[i].cells[10].childNodes[0].value;
        	var pattern1 = /^[1-9][0-9]*$/;
            if (!pattern1.exec(pQty)) {
                MyAlert("第" + i + "行配件：" + mt.rows[i].cells[2].innerText + " 计划数量必须是大于0的整数!</r>");
                break;
            }
            var partId = mt.rows[i].cells[1].childNodes[0].value;  //ID
            if (validateCell(partId)) {
            	var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
                var partCode = mt.rows[i].cells[4].innerText;  //件号
                var partType = $("#partType"+partId)[0].value;//配件属性
                var partTypeName = mt.rows[i].cells[5].innerText;  //配件属性名称
                var unit = mt.rows[i].cells[6].innerText;  //单位
                var venderId = $("#vender"+partId)[0].value;  //供应商id
                var venderName = mt.rows[i].cells[7].innerText;  //供应商名称
                var whQty = mt.rows[i].cells[8].innerText;  //库存数量
                var minPackage = mt.rows[i].cells[9].innerText;  //最小包装量
                //var salePrice3 = mt.rows[i].cells[9].innerText;  //计划价
                var planQty = mt.rows[i].cells[10].childNodes[0].value;  //计划数量
                //var buyPrice = mt.rows[i].cells[11].innerText;  //采购价格
                //var itemQty = mt.rows[i].cells[12].innerText;  //可用库存量		            
                var preArriveDate = mt.rows[i].cells[11].innerText;  //到货日期
                addCell(partId, partCode, partOldcode, partCname,partType,partTypeName, unit,venderId,venderName, minPackage, planQty, preArriveDate);
                refeshQtyAndAmount();
            } else {
                MyAlert("第" + i + "行配件：" + mt.rows[i].cells[2].innerText + " 已存在!</br>");
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
function addCell(partId, partCode, partOldcode, partCname,partType,partTypeName, unit,venderId,venderName,
		minPackage, planQty, preArriveDate) {
    var tbl = document.getElementById('file');
    var rowObj = tbl.insertRow(tbl.rows.length);
    if (tbl.rows.length % 2 == 0) {
        rowObj.className = "table_list_row2";
    } else {
        rowObj.className = "table_list_row1";
    }
   // var amount = (parseFloat(salePrice3) * parseFloat(planQty)).toFixed(2)
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
    cell1.innerHTML = '<tr><td class="center" nowrap><input  type="checkbox" value="' + partId + '" id="cell_' + (tbl.rows.length - 2) + '" name="cb" checked="true" onclick="refeshQtyAndAmount();" /></td>';
    cell2.innerHTML = '<td class="center" nowrap><span id="orderLine_SEQ" >' + (tbl.rows.length - 2) + '</span><input id="idx_' + partId + '" name="idx_' + partId + '" value="' + (tbl.rows.length - 2) + '" type="hidden" ></td>';
    cell3.innerHTML = '<td style="text-align: left" ><input   name="partOldcode_' + partId + '" id="partOldcode_' + partId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    cell4.innerHTML = '<td style="text-align: left"><input   name="partCname_' + partId + '" id="partCname_' + partId + '" value="' + partCname + '" type="hidden" />' + partCname + '</td>';
    cell5.innerHTML = '<td style="text-align: left"><input   name="partCode_' + partId + '" id="partCode_' + partId + '" value="' + partCode + '" type="hidden" />' + partCode + '</td>';
    cell6.innerHTML = '<td style="text-align: left"><input   name="partType_' + partId + '" id="partType_' + partId + '" value="' + partType + '" type="hidden" />' + partTypeName + '</td>';
    cell7.innerHTML = '<td class="center" nowrap><input   name="unit_' + partId + '" id="unit_' + partId + '" value="' + unit + '" type="hidden" />' + unit + '</td>';
    cell8.innerHTML = '<td class="center" nowrap><input   name="venderId_' + partId + '" id="venderId_' + partId + '" value="' + venderId + '" type="hidden" />' + venderName + '</td>';
    cell9.innerHTML = '<td class="center" nowrap><input   name="minPackage_' + partId + '" id="minPackage_' + partId + '" value="' + minPackage + '" type="hidden" />' + minPackage + '</td>';
    cell10.innerHTML = '<td class="center" nowrap><input  class="middle_txt"  name="plan_qty_' + partId + '" id="plan_qty_' + partId + '" maxlength="8" value="' + planQty + '" type="text" onkeydown="toNext1('+partId+')"/></td>';
    cell11.innerHTML = '<td class="center" nowrap><input   name="preArriveDate_' + partId + '" id="preArriveDate_' + partId + '" value="' + preArriveDate + '" type="hidden" />' + preArriveDate + '</td>';
    cell12.innerHTML = '<td class="center" nowrap><input class="middle_txt" name="remark_' + partId + '" id="remark_' + partId + '" type="text"/></td>';
    cell13.innerHTML = '<td><input  type="button" class="u-button"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" /></td></TR>';

}

function countMoney(loc, obj, price, partId) {
    if ("" == obj.value) {
        return;
    }
    var tbl = document.getElementById('file');
    var value = obj.value;
    if (isNaN(value)) {
        MyAlert("请输入数字!");
        obj.value = "";
        return;
    }
    var re = /^[1-9]+[0-9]*]*$/;
    if (value!=0&&!re.test(value)) {
        MyAlert("请输入正整数!");
        obj.value = "";
        return;
    }

    var money = (parseFloat(price) * parseFloat(value)).toFixed(2);
    tbl.rows[loc + 1].cells[11].innerHTML = '<td class="center" nowrap>' + '<input   name="planAmount_' + partId + '" id="planAmount_' + partId + '" value="' + money + '" type="hidden" />' + money + '</td>';
    refeshQtyAndAmount();
}
function deleteTblRow(obj) {
    var idx = obj.parentElement.parentElement.rowIndex;
    var tbl = document.getElementById('file');
    tbl.deleteRow(idx);
    refreshMtTable('orderLine', 'SEQ');//刷新行号
    refeshQtyAndAmount();//刷新总金额和数量
}
//计算金额和数量
function refeshQtyAndAmount() {
    var qtyCount = parseFloat(0);
    var amountCount = parseFloat(0);
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            if ("" != cb[i].value && null != document.getElementById("plan_qty_" + cb[i].value) && "" != document.getElementById("plan_qty_" + cb[i].value).value) {
                qtyCount = ( parseFloat(qtyCount) + parseFloat(document.getElementById("plan_qty_" + cb[i].value).value));
            }
        }
    }
    document.getElementById("SUM_QTY").value = qtyCount;
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
function addPartDiv() {
    if ($("#PLAN_TYPE")[0].value == "") {
        MyAlert("请选择计划类型!");
        return;
    }
   /*  if ($("#wh_id")[0].value == "") {
        MyAlert("请选择仓库!");
        return;
    } */
    /* if ($("t3").value == "") {
        MyAlert("请选择计划日期!");
        return;
    } */
   /*  if ($("#t2")[0].value == "") {
        MyAlert("请选择预计到货日期!");
        return;
    } */
    /* if (checkSys_Sel_Date($("t3").value, "yyyy-MM-dd")) {
        MyAlert("计划日期不能小于制单日期!");
        return;
    } */
   /*  if($("#PLAN_CYCLE")[0].value==0||$("#PLAN_CYCLE")[0].value==""){
    	MyAlert("请输入合法的订货周期!");
    	return;
    }
    if($("#COME_CYCLE")[0].value==0||$("#COME_CYCLE")[0].value==""){
    	MyAlert("请输入合法的到货周期!");
    	return;
    }
    if (checkSys_Sel_Date($("#t2")[0].value, "yyyy-MM-dd")) {
        MyAlert("预计到货日期不能小于制单日期!");
        return;
    } */

   /*  if(checkSys_Sel_Date1($("t3").value,$("t2").value)){
    	MyAlert("计划日期不能大于预计到货日期!");
        return;
    } */
    /*var planCycle = $("PLAN_CYCLE").value;
    if(!planCycle){
        MyAlert("订货提前期不能为空!");
        $("PLAN_CYCLE").value = "";
        $("PLAN_CYCLE").focus();
        return;
    }else{
    	var pattern1 = /^[1-9][0-9]*$/;
        if (!pattern1.exec(planCycle)) {
            MyAlert("订货提前期只能输入正整数!");
            $("PLAN_CYCLE").value = "";
            $("PLAN_CYCLE").focus();
            return;
    }
    }*/
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    if (partDiv.style.display == "block") {
        addPartViv.value = "增加";
        partDiv.style.display = "none";
    } else {
        addPartViv.value = "收起";
        partDiv.style.display = "block";
        __extQuery__(1);
    }
}

function checkSys_Sel_Date(sel_date,fmt){
	var flag = "";
	var sys_date = new Date();//document.getElementById("sys_date__").value;
	var yyyy = sys_date.getFullYear();
	var MM = sys_date.getMonth();
	var dd = sys_date.getDate();
	var hh = sys_date.getHours();
	var mm = sys_date.getMinutes();
	var ss = sys_date.getSeconds();
	var  date=new Date(yyyy,MM-1,dd,hh,mm,ss);
	if(fmt){
		var date_format = date.Format(fmt);
	}else{
		var date_format = date.Format("yyyy-MM-dd");
	}
	if(date_format <= sel_date){
		flag = false;
	}else{
		flag = true;
	}
	return flag;
}

function checkSys_Sel_Date1(date1,date2){
	var flag = "";
	if(date1 <= date2){
		flag = false;
	}else{
		flag = true;
	}
	return flag;
}

function savePlan(t) {
    disableAllStartBtn();
    var url = "<%=contextPath%>/parts/planManager/PartPlanManager/savePlan.json?t="+t;
    makeNomalFormCall(url, getResult, 'fm');
}

function getResult(jsonObj) {
    enableAllStartBtn();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            window.location.href = '<%=contextPath%>/parts/planManager/PartPlanManager/toRollingPlanInitZ.do';
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}

function exportExcelTemplate() {
    fm.action = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/exportExcelTemplate.do";
    fm.submit();
}
function uploadEx() {
    var url1 = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/uploadPartPlanExcel.json";
    disableAllStartBtn();
    makeNomalFormCall(url1, getUploadResult, 'fm');
}

function getUploadResult(partArr,error) {
    if (partArr != null) {
        var data = partArr;
        var msg = "";
        if (error != "") {
            msg += '<font color="RED">' + error + '</font>';
        }
        for (var i = 0; i < data.length; i++) {
            var tbl = document.getElementById('file');
            var flag = true;
            for (var j = 0; j < tbl.rows.length; j++) {
                if (data[i].PART_ID == tbl.rows[j].cells[0].firstChild.value) {
                    msg += "第" + (j - 1) + "行配件：" + data[i].PART_CNAME + " 已存在!</br>";
                    flag = false;
                    break;
                }

            }
            if (flag) {
                addCell(data[i].PART_ID,
                        data[i].PART_CODE,
                        data[i].PART_OLDCODE,
                        data[i].PART_CNAME,
                        data[i].UNIT == null ? "" : data[i].UNIT,
                        data[i].MIN_PACKAGE == null ? "" : data[i].MIN_PACKAGE,
                        data[i].SALE_PRICE3 == null ? "" : data[i].SALE_PRICE3,
                        data[i].BUY_PRICE == null ? "" : data[i].BUY_PRICE,
                        data[i].planQty == null ? "" : data[i].planQty,
                        data[i].ITEM_QTY == null ? "" : data[i].ITEM_QTY,
                        data[i].BO_QTY == null ? "" : data[i].BO_QTY,
                        data[i].AVG_QTY == null ? "" : data[i].AVG_QTY,
                        data[i].ORDER_QTY == null ? "" : data[i].ORDER_QTY,
                        data[i].SAFETY_STOCK == null ? "" : data[i].SAFETY_STOCK,
                        data[i].PREARRIVEDATE == null ? "" : data[i].PREARRIVEDATE);
                if (data[i] && data[i].planQty != "") {
                    document.getElementById('plan_qty_' + data[i].PART_ID).value = data[i].planQty;
                }
                document.getElementById('remark_' + data[i].PART_ID).value = data[i].REMARK;
                countMoney(document.getElementById('plan_qty_' + data[i].PART_ID).parentNode.parentNode.rowIndex - 1, document.getElementById('plan_qty_' + data[i].PART_ID), data[i].SALE_PRICE3 == null ? "" : data[i].SALE_PRICE3, data[i].PART_ID);
            }
        }
        if ("" != msg) {
            MyAlert(msg);
        }
    }
}
function saveAndCommit() {
    document.getElementById("planState").value =<%=Constant.PART_PURCHASE_PLAN_CHECK_STATUS_02%>;
    fm.action = "<%=contextPath%>/parts/planManager/PartPlanManager/savePlan.do";
    disableAllStartBtn();
    fm.submit();
}
function confirmSubmit(value) {
    var cb = document.getElementsByName('cb');
    var msg = "";
    var msg1 = "";
    if (document.getElementById("PLAN_TYPE").value == "") {
        msg += "请填写计划类型!</br>";
    }
  /*   if ($("#wh_id")[0].value == "") {
        MyAlert("请选择仓库!");
        return;
    } */
    /* if ($("t3").value == "") {
        MyAlert("请选择计划日期!");
        return;
    } */
  /*   if ($("#t2")[0].value == "") {
        MyAlert("请选择预计到货日期!");
        return;
    }
    if (checkSys_Sel_Date($("#t2")[0].value, "yyyy-MM-dd")) {
        MyAlert("预计到货日期不能小于制单日期!");
        return;
    } */
   
    //提交时,将其属性设置成DISABLED,从而达到过滤选择的目的
    var flag = false;
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            cb[i].disabled = false;
            //需要校验计划量是否填写
            if (document.getElementById("plan_qty_" + cb[i].value).value == "") {
                msg += "请填写第" + document.getElementById("idx_" + cb[i].value).value + "行的计划量!</br>";
                flag = true;
            } else {
                var planQty = document.getElementById("plan_qty_" + cb[i].value).value;
            	var pattern1 = /^[1-9][0-9]*$/;
                if (!pattern1.exec(planQty)) {
                	msg += "第" + document.getElementById("idx_" + cb[i].value).value + "行的计划量只能为正整数!</br>";
                	flag = true;
                }
                var mod = document.getElementById("plan_qty_" + cb[i].value).value % document.getElementById("minPackage_" + cb[i].value).value;
                if (mod != 0) {
                	msg1 += "第" + document.getElementById("idx_" + cb[i].value).value + "行的计划数量不是最小包装量的整数倍,确定这样操作?";
                }
            }
        } else {
            cb[i].disabled = true;
        }
    }
    if (flag) {
        for (var i = 0; i < cb.length; i++) {
            cb[i].disabled = false;
        }
    }
    var cb = document.getElementsByName("cb");
    if (cb.length <= 0) {
        msg += "请添加计划明细!</br>";
    }
    var flag = false;
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            flag = true;
            break;
        }
    }
    if (!flag) {
        msg += "请选择计划明细!</br>";
    }
    if (msg != "") {
        MyAlert(msg);
        for (var i = 0; i < cb.length; i++) {
            cb[i].disabled = false;
        }
        return;
    }
    
    if(msg1 != ""){
    	MyAlert(msg1);
    }
    if (value == 1) {
        document.getElementById("planState").value =<%=Constant.PART_PURCHASE_PLAN_CHECK_STATUS_01%>;
        refeshQtyAndAmount();
        MyConfirm("确定保存计划?", savePlan, [1]);
    } else {
        refeshQtyAndAmount();
        MyConfirm("确定提交并保存计划?", savePlan, [2]);
    }
    for (var i = 0; i < cb.length; i++) {
        cb[i].disabled = false;
    }
}
function goBack() {
    fm.action = "<%=contextPath%>/parts/planManager/PartPlanManager/toRollingPlanInitZ.do";
    fm.submit();
}
function validateNum(obj) {
    if (isNaN(obj.value)) {
        MyAlert("请输入数字!");
        obj.value = "";
        return;
    }
}
function showUpload() {
    if ($("#PLAN_TYPE")[0].value == "") {
        MyAlert("请选择计划类型!");
        return;
    }
    if ($("#wh_id")[0].value == "") {
        MyAlert("请选择仓库!");
        return;
    }
    var uploadDiv = $("#uploadDiv")[0];
    if (uploadDiv.style.display == "block") {
        uploadDiv.style.display = "none";
    } else {
        uploadDiv.style.display = "block";
    }
}
function doCusChange(value) {
    var addPartViv = document.getElementById("addPartViv");
    var partDiv = document.getElementById("partDiv");
    if ('' == value) {
        addPartViv.value = "增加";
        partDiv.style.display = "none";
    }
    var tbl = document.getElementById('file');
    var len = tbl.rows.length;
    if (len > 2) {
        //改变仓库之后就要删除明细,重新选择
        for (var i = tbl.rows.length - 1; i >= 2; i--) {
            tbl.deleteRow(i);
        }
    }
    $("#SUM_QTY")[0].value = "";
}


function disableAllStartBtn() {
    var inputArr = document.getElementsByTagName("input");
    for (var i = 0; i < inputArr.length; i++) {
        if (inputArr[i].type == "button") {
            inputArr[i].disabled = true;
        }
    }
}
function enableAllStartBtn() {
    var inputArr = document.getElementsByTagName("input");
    for (var i = 0; i < inputArr.length; i++) {
        if (inputArr[i].type == "button") {
            inputArr[i].disabled = false;
        }
    }
}

function clearInput() {//清空选定供应商
	var venderId = document.getElementById("VENDER_ID").value;
	if(venderId!=null&&venderId!=""){
		document.getElementById("VENDER_ID").value = '';
	    document.getElementById("VENDER_NAME").value = '';
	}
}


var curVenderId;
var curMakerId;
var curT3;
var curT2;

var curPlanCycle;
function changeDiv3(){
	var planCycle = $("#PLAN_CYCLE")[0].value;
	var partDiv = document.getElementById("partDiv");
	var addPartViv = document.getElementById("addPartViv");
	var tbl = document.getElementById('file');
	var len = tbl.rows.length;
	if (planCycle != curPlanCycle) {
        if (len > 2) {
            for (var i = tbl.rows.length - 1; i >= 2; i--) {
                tbl.deleteRow(i);
            }
        }
        addPartViv.value = "增加";
        partDiv.style.display = "none";
        $("#SUM_QTY")[0].value = "";
    }
	
	if(!planCycle){
        MyAlert("订货周期不能为空!");
        $("#PLAN_CYCLE")[0].value = "";
        $("#PLAN_CYCLE")[0].focus();
        return;
    }else{
    	/*var pattern1 = /^[1-9][0-9]*$/;
        if (!pattern1.exec(planCycle)) {
        	$("PLAN_CYCLE").value = planCycle.replace(/\D/g, '');
        }*/
        if(isNaN(planCycle)){
        	MyAlert("订货周期不合法!");
        	$("#PLAN_CYCLE")[0].value = "";
            $("#PLAN_CYCLE").focus();
        	return;
        }
    }
	curPlanCycle = planCycle;
	
	var num = new Number(planCycle);
    var planCycle1 = num.toFixed(2);
    $("#PLAN_CYCLE")[0].value = planCycle1;
}


var curComeCycle;
function changeDiv4(){
	var comeCycle = $("COME_CYCLE").value;
	var partDiv = document.getElementById("partDiv");
	var addPartViv = document.getElementById("addPartViv");
	var tbl = document.getElementById('file');
	var len = tbl.rows.length;
	if (comeCycle != curComeCycle) {
        if (len > 2) {
            for (var i = tbl.rows.length - 1; i >= 2; i--) {
                tbl.deleteRow(i);
            }
        }
        addPartViv.value = "增加";
        partDiv.style.display = "none";
        $("#SUM_QTY")[0].value = "";
    }
	
	if(!comeCycle){
        MyAlert("到货周期不能为空!");
        $("#COME_CYCLE")[0].value = "";
        $("#COME_CYCLE")[0].focus();
        return;
    }else{
        if(isNaN(comeCycle)){
        	MyAlert("到货周期不合法!");
        	$("#COME_CYCLE")[0].value = "";
            $("#COME_CYCLE").focus();
        	return;
        }
    }
	curComeCycle = comeCycle;
	
	var num = new Number(comeCycle);
    var comeCycle1 = num.toFixed(2);
    $("#COME_CYCLE")[0].value = comeCycle1;
}

function validateQty(obj){
	var pattern1 = /^[1-9][0-9]*$/;
    if (obj.value!=0&&!pattern1.exec(obj.value)) {
    	MyAlert("计划数量输入不合法!");
    	obj.value = "";
    	return;
    }
}
/*function changeDiv() {
    var venderId = $("VENDER_ID").value;
    var makerId = $("MAKER_ID").value;
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    var tbl = document.getElementById('file');
    var len = tbl.rows.length;

    if (venderId != curVenderId) {
        if (len > 2) {
            //改变供应商之后就要删除明细,重新选择
            for (var i = tbl.rows.length - 1; i >= 2; i--) {
                tbl.deleteRow(i);
            }
        }
        curVenderId = venderId;
        $("SUM_QTY").value = "";
        $("AMOUNT").value = "";
        __extQuery__(1);
    }
    if (makerId != curMakerId) {
        if (len > 2) {
            //改变制造商之后就要删除明细,重新选择
            for (var i = tbl.rows.length - 1; i >= 2; i--) {
                tbl.deleteRow(i);
            }
        }
        curMakerId = makerId;
        $("SUM_QTY").value = "";
        $("AMOUNT").value = "";
        __extQuery__(1);
    }
}*/

function uploadExcel(){
	fm.action = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/uploadPartPlanExcel.do?"
	fm.submit();
}

/**
 * 选择供应商
 * inputId   : 回填页供应商code域id
 * inputName ：回填页供应商id域id
 * isMulti   : true值多选，否则单选
 */
function showPartVender(inputCode ,inputId ,isMulti ){
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	OpenHtmlWindow("<%=contextPath%>/dialog/venderSelectSingle.jsp?INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,450);
}

function isCloseDealerTreeDiv() {}
</script>
</HEAD>

<BODY onload="enableAllStartBtn();"> <!-- onunload='javascript:destoryPrototype()' -->
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置配件管理&gt; 采购计划管理 &gt; 周度需求计划编制&gt;新增</div>
        <form name="fm" id="fm" method="post" enctype="multipart/form-data">
            <input type="hidden" name="planState" id="planState"/>
            <input type="hidden" name="planType" id="planType" value="<%=Constant.PART_PURCHASE_PLAN_TYPE_04%>">
            <input type="hidden" name="NEGATIVE_FILT" id="NEGATIVE_FILT" value="1">

            <div class="form-panel">
                <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/> 计划单信息</h2>
                <div class="form-body">
                    <table class="table_query">
                        <tr>
                            <td class="right">计划单号:</td>
                            <td width="10%">${planCode}
                            <input type="hidden" name="planCode" value="${planCode}"/>
                            </td>
                            <td class="right">计划员:</td>
                            <td  >${name}
                            <input type="hidden" name="planName" value="${name}"/>
                            </td>
                            <td class="right">制单日期:</td>
                            <td  width="21%">${now}
                            <input type="hidden" name="createDate" id="createDate" value="${now}"/>
                            <input type="hidden" name="PLAN_TYPE" id="PLAN_TYPE" value="<%=Constant.PART_PURCHASE_PLAN_TYPE_04%>"/>
                            </td>
                        </tr>
                    <%--  <tr>
                            <td class="right">计划日期:</td>
                            <td  >
                            <!--   <script type="text/javascript">
                                    getYearSelect("YEAR", "YEAR", 1, '');
                                </script>
                                <script type="text/javascript">
                                    getMonThSelect("MONTH", "MONTH", '');
                                </script> -->
                                <input name="beginTime2" id="t3" value="" type="text" readonly="readonly" class="middle_txt" style="width: 80px;" >
                                <input name="button" value=" " type="button" class="time_ico" title="点击选择时间" onclick="showcalendar(event, 't3', false);" value="" />
                                <font color="#FF000">*</font>
                                </td> 
                            <td class="right">库房:</td>
                            <td  >
                                <select name="wh_id" id="wh_id" class="u-select" onchange="doCusChange(this.value)">
                                    <option selected value=''>-请选择-</option>
                                    <c:forEach items="${wareHouseList}" var="wareHouse">
                                        <option value="${wareHouse.WH_ID}" selected="${wareHouse.WH_ID==wh_id?'selected':''}">${wareHouse.WH_NAME}</option>
                                    </c:forEach>
                                </select> 
                                <font color="#FF000">*</font>
                            </td>
                            <td class="right">预计到货日期:</td>
                            <td  >
                                <input name="beginTime2" id="t2" type="text" readonly="readonly" class="middle_txt"  value="${beginTime2}" >
                                <input name="button" value=" " type="button" class="time_ico" title="点击选择时间" onclick="showcalendar(event, 't2', false);changeDiv1();"/>
                                <font color="#FF000">*</font>
                            </td>
                            <td class="right">订货周期(天):</td>
                            <td  >
                            <input  class="middle_txt" type="text" name="PLAN_CYCLE" id="PLAN_CYCLE" value="${planCycle eq null?30:planCycle }" onchange="changeDiv3();"/>
                            <font color="#FF000">*</font>
                            <!--<input  class="short_txt" type="text" name="PLAN_CYCLE" id="PLAN_CYCLE" value="" readonly="readonly" style="border:0px;background-color:#F3F4F8;"/>-->
                            </td>
                        </tr> --%>
                        <tr>
                        <td class="right">总数量:</td>
                            <td><input readonly class="phone_txt" type="text" style="border:0px;background-color:#f2f2f2;" name="SUM_QTY" id="SUM_QTY"/>
                            </td>
                        <!--   <td class="right">总金额:</td>
                            <td  ><input class="phone_txt" type="text"
                                                                style="border:0px;background-color:#f2f2f2;"
                                                                readonly name="AMOUNT" id="AMOUNT"/></td> -->
                        <%--  <td class="right">到货周期(天):</td>
                            <td  >
                            <input  class="middle_txt" type="text" name="COME_CYCLE" id="COME_CYCLE" value="${comeCycle eq null?30:comeCycle }" onchange="changeDiv4();"/>
                            <font color="#FF000">*</font> --%>
                            </td>
                        </tr>
                        <tr>
                            <td class="right">备注:</td>
                            <td colspan="5">
                                <textarea class="form-control remark align" id="remark" name="remark">${remark }</textarea></td>
                        </tr>
                    </table>
                </div>
            </div>
            <FIELDSET class="form-fieldset sel-buttons">
                <LEGEND style="MozUserSelect: none; KhtmlUserSelect: none"
                        unselectable="on">
                    <th colspan="6">
                        <img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/> 配件查询
                        <input type="button" class="normal_btn" name="addPartViv"
                            id="addPartViv" value="增加" onclick="addPartDiv()"/>
                    </th>
                </LEGEND>
                <div style="display: none;" id="partDiv" class="form-panel">
                    <table class="table_query" width=100% border="0" class="center"
                        cellpadding="1" cellspacing="1">
                        <tr>
                            <td width="10%" class="right">配件编码：</td>
                            <td width="29%">
                                <input class="middle_txt" id="PART_OLDCODE"
                                    datatype="1,is_noquotation,30" name="PART_OLDCODE"
                                    onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                            </td>
                            <td class="right">配件名称：</td>
                            <td width="17%">
                                <input class="middle_txt" id="PART_CNAME"
                                    datatype="1,is_noquotation,30" name="PART_CNAME"
                                    onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                            </td>
                            <td class="right"> 件号：</td>
                            <td >
                                <input class="middle_txt" id="PART_CODE"
                                    datatype="1,is_noquotation,30" name="PART_CODE"
                                    onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                            </td>
                        </tr>
                        <tr>
                            <td   class="right">供应商：</td>
                            <td >
                                <input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME"/>
                                <input class="mark_btn" type="button" value="&hellip;"
                                    onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
                                    <INPUT class="u-button mini_btn" onclick="clearInput();" value=清除 type=button name=clrBtn>
                                <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
                            </td>
                           <!--  <td   class="right">
                                制造商：
                            </td>
                            <td >
                                <input class="middle_txt" type="text" readonly="readonly" id="MAKER_NAME" name="MAKER_NAME"/>
                                <input class="mark_btn" type="button" value="&hellip;"
                                    onclick="showPartMaker('MAKER_NAME','MAKER_ID','false')"/>
                                    <INPUT class=mini_btn onclick="clearMInput();" value=清除 type=button name=clrBtn>
                                <input id="MAKER_ID" name="MAKER_ID" type="hidden" value="">
                            </td>
                            <td class="right">
                            计划数量:    
                            </td>
                            <td width="10%">
                            <select id="SELECT_OP" name="SELECT_OP">
                            <option value="">--请选择--</option>
                            <option value="1">大于</option>
                            <option value="2">等于</option>
                            <option value="3">小于</option>
                            <option value="4">小于等于</option>
                            <option value="5">大于等于</option>
                            <option value="6">不等于</option>
                            </select>
                            <input  class="short_txt" type="text" name="PLANQTY" id="PLANQTY" maxlength="8" value="" onchange="validateQty(this);"/>
                            </td>
            -->            </tr>
                        <tr>
                            <td class="center" colspan="6">
                                <input class="u-button" type="button" name="BtnQuery"
                                    id="queryBtn" value="查 询" onclick="__extQuery__(1)"/>
                                <input class="normal_btn" type="button" name="BtnQuery"
                                    id="queryBtn2" value="添加" onclick="addCells()"/>
                            </td>
                        </tr>
                    </table>
                    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
                    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
                </div>
            </FIELDSET>
            <table id="file" class="table_list" style="border-bottom: 1px;">
                <caption><img class="panel-icon" src="<%=contextPath%>/img/nav.gif"/>详细计划</caption>
                <tr class="table_list_row0">
                    <th><input type="checkbox" onclick="selAll2(this)"/></th>
                    <th>序号</th>
                    <th>配件编码</th>
                    <th>配件名称</th>
                    <th>件号</th>
                    <th>配件属性</th>
                    <th>单位</th>
                    <th>供应商</th>
                    <th>最小包装量</th>
                    <th>计划量</th>
                    <th>预计到货日期</th>
                    <th>备注</th>
                    <th>操作</th>
                </tr>
                <c:if test="${list !=null}">
                <c:forEach items="${list}" var="list" varStatus="_sequenceNum" step="1">
                    <c:if test="${((_sequenceNum.index+1) mod 2) != 0}">
                    <tr class="table_list_row1">
                    </c:if>
                    <c:if test="${((_sequenceNum.index+1) mod 2) == 0}">
                    <tr class="table_list_row2">
                    </c:if>
                    <td class="center" nowrap>
                        <input  type="checkbox" value="${list.PART_ID}" id="cell_${_sequenceNum.index+1}" name="cb" checked="checked" onclick="refeshQtyAndAmount();"/>
                    </td>
                    <td class="center" nowrap>
                    <span id="orderLine_SEQ" >${_sequenceNum.index+1}</span>
                        <input id="idx_${list.PART_ID}" name="idx_${list.PART_ID}" value="${_sequenceNume.index+1}" type="hidden" >
                    </td>
                    <td >
                        <input   name="partOldcode_${list.PART_ID}" id="partOldcode_${list.PART_ID}" value="${list.PART_OLDCODE}" type="hidden" />${list.PART_OLDCODE}
                    </td>
                    <td  nowrap>
                        <input   name="partCname_${list.PART_ID}" id="partCname_${list.PART_ID}" value="${list.PART_CNAME}" type="hidden" class="cname_${list.partId}"/>${list.PART_CNAME}
                    </td>
                    <td  nowrap>
                        <input   name="partCode_${list.PART_ID}" id="partCode_${list.PART_ID}" value="${list.PART_CODE}" type="hidden" class="cname_${list.partId}"/>${list.PART_CODE}
                    </td>
                    <td  nowrap>
                        <input   name="partType_${list.PART_ID}" id="partType_${list.PART_ID}" value="${list.PART_TYPE}" type="hidden" />${list.PARTTYPENAME}
                    </td>
                    <td class="center" nowrap>
                        <input   name="unit_${list.PART_ID}" id="unit_${list.PART_ID}" value="${list.UNIT}" type="hidden" />${list.UNIT}
                    </td>
                    <td class="center" nowrap>
                        <input   name="venderId_${list.PART_ID}" id="venderId_${list.PART_ID}" value="${list.VENDER_ID}" type="hidden" />${list.VENDER_NAME}
                    </td>
                    <td class="center" nowrap>
                        <input   name="minPackage_${list.PART_ID}" id="minPackage_${list.PART_ID}" value="${list.MIN_PACKAGE}" type="hidden" />${list.MIN_PACKAGE}
                    </td>
                    <td class="center" nowrap>
                        <input   name="salePrice3_${list.PART_ID}" id="salePrice3_${list.PART_ID}" value="${list.SALE_PRICE3}" type="hidden" />${list.SALE_PRICE3}
                    </td>
                    <td class="center" nowrap>
                        <input onchange="countMoney(this.parentNode.parentNode.rowIndex-1,this,${list.SALE_PRICE3},${list.PART_ID})" maxlength="8" name="plan_qty_${list.PART_ID}" id="plan_qty_${list.PART_ID}" value="${list.QTY}" type="text" onkeydown="toNext1(${list.PART_ID})"/>
                    </td>
                    <td class="center" nowrap>
                        <input name="planAmount_${list.PART_ID}" id="planAmount_${list.PART_ID}" value="${list.AMOUNT}" type="hidden" />${list.AMOUNT}
                    </td>
                    
                    <td class="center" nowrap>
                        <input name="safetyStock_${list.PART_ID}" id="safetyStock_${list.PART_ID}" value="${list.SAFETY_STOCK}" type="hidden" />${list.SAFETY_STOCK}
                    </td>
                    <td class="center" nowrap>
                        <input name="preArriveDate_${list.PART_ID}" id="preArriveDate_${list.PART_ID}" value="${list.PREARRIVEDATE}" type="hidden" />${list.PREARRIVEDATE}
                    </td>
                    <td class="center" nowrap>
                        <input class="middle_txt" name="remark_${list.PART_ID}" id="remark_${list.PART_ID}" value="${list.REMARK}" type="text" />
                    </td>
                    <td>
                        <input  type="button" class="u-button"  name="queryBtn4" value="删除" onclick="deleteTblRow(this);" />
                    </td>
                    </tr>
                </c:forEach>
                </c:if>
            </table>
            <table width="100%" class="center">
                <tr>
                    <td height="2"></td>
                </tr>
                <tr>
                    <td style="text-align: center">
                    <!--  <input class="u-button" type="button" value="上传文件" name="button1"
                            onclick="showUpload();"> &nbsp; -->
                        <input class="u-button" type="button" value="保存" name="button1"
                            onclick="confirmSubmit(1);">
                        &nbsp;
                        <input class="u-button" type="button" value="提交" name="button1"
                            onclick="confirmSubmit(2)">
                        &nbsp;
                        <input class="u-button" type="button" value="返回" name="button1"
                            onclick="goBack();">
                    </td>
                </tr>
            </table>
            <!-- <iframe frameborder="0" name="myIframe" id="myIframe" src="<%=request.getContextPath() %>/jsp/parts/purchaseManager/purchasePlanSetting/uploadFile.jsp" height="100%" width="100%" scrolling="auto" align="middle">

            </iframe> -->
            <div style="display:none; heigeht: 5px" id="uploadDiv">

                <tr>
                    <td><font color="red">
                        <input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate()"/>
                        文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
                        <input type="file" name="uploadFile1" id="uploadFile1" style="width: 250px" datatype="0,is_null,2000"
                            value=""/>
                        &nbsp;
                        <input type="button" id="upbtn" class="normal_btn" value="确定" onclick="uploadExcel()"/></td>
                </tr>
            </div>
        </form>
    </div>    
    <script>
        $(function() {
            var grid  = $('#myGrid'),
                panel = $('.form-panel');
            $(window).resize(function() {
                grid.width(panel.width() - 26);
            });
        });
    </script>    
</BODY>
</html>

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
<script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
            var valT = ${beginTime2};
            if(valT==1){
            	var dt = new Date(); 
                //dt.setDate(1);  
                //dt.setMonth(dt.getMonth()+2);
                /*var cdt = new Date(dt.getTime()+1000*60*60*24*30);
                var dateStr = cdt.format("yyyy-MM-dd");
                $("t3").value = dateStr;*/

                /*var cdt = new Date(dt.getTime()+1000*60*60*24*60);
                var yearStr = cdt.getFullYear();
                var monStr = cdt.getMonth();
                var dayStr = cdt.getDate();
                var cdt1 = new Date(yearStr,monStr,dayStr);*/

                //var dt1 = new Date();
                //dt1.setDate(1);  
                //dt1.setMonth(dt.getMonth()+2);
                var cdt1 = new Date(dt.getTime()+1000*60*60*24*30);
                var dateStr3 = cdt1.format("yyyy-MM-dd");
                $("t2").value = dateStr3;
                
                /*var dt1 = new Date();  
                var cdt2 = new Date(dt1.getTime()+1000*60*60*24*60);
                var yearStr2 = cdt2.getFullYear();
                var monStr2 = cdt2.getMonth();
                var dayStr2 = cdt2.getDate();
                var cdt3 = new Date(yearStr2,monStr2,dayStr2);
                var dateStr3 = cdt3.format("yyyy-MM-dd");
                $("t2").value = dateStr3;*/
                //MyAlert(cdt.getFullYear()+"年"+(Number(cdt.getMonth())+1)+"月月末日期:"+cdt.getDate()+"日");
            }
            
            refeshQtyAndAmount();
        }

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

        /* function initDate(){
        	document.getElementById('t3').attachEvent('onpropertychange',function(o){
            	if(o.propertyName=='value'){//如果是value属性才执行
            		var t3 = $("t3").value;
            		var partDiv = document.getElementById("partDiv");
            	    var addPartViv = document.getElementById("addPartViv");
            	    var tbl = document.getElementById('file');
            	    var len = tbl.rows.length;
            	    if (t3 != curT3) {
            	        if (len > 2) {
            	            //改变计划日期之后就要删除明细,重新选择
            	            for (var i = tbl.rows.length - 1; i >= 2; i--) {
            	                tbl.deleteRow(i);
            	            }
            	        }
            	        addPartViv.value = "增加";
        	            partDiv.style.display = "none";
            	        
            	        $("SUM_QTY").value = "";
            	        $("AMOUNT").value = "";

            	        if (checkSys_Sel_Date($("t3").value, "yyyy-MM-dd")) {
            	            MyAlert("计划日期不能小于制单日期!");
            	            return;
            	        }else{
            	        	//重新计算计划提前期
            			    var t3Arr = t3.split("-");
            			    var dt = new Date(t3Arr[0], t3Arr[1]-1, t3Arr[2]);
            			    
            			    var createDate = $("createDate").value;
            			    var cArr = createDate.split("-");
            			    var cDt = new Date(cArr[0], cArr[1]-1, cArr[2]);

            			    var cha = (dt.getTime()-cDt.getTime())/(24 * 60 * 60 * 1000);
            			    var reCha = 1+cha/30;
            			    var num = new Number(reCha);
            			    var planCycle = num.toFixed(1);
            			    $("PLAN_CYCLE").value = planCycle;
            			    curT3 = t3;
            	        }
            	    }
            	}
            }); 
        } */
       
    </script>
<SCRIPT type=text/javascript><!--

var myPage;

var url = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/showPartBase.json";

var title = null;

var columns = [
    {header: "序号", style: 'text-align:left', renderer: getIndex, width: '7%'},
    {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', style: 'text-align:left', renderer: seled},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "配件属性", dataIndex: 'PART_TYPE', style: 'text-align:left',renderer: getItemValue},
    {header: "单位", dataIndex: 'UNIT', style: 'text-align:center'},
    {header: "供应商", dataIndex: 'VENDER_NAME', style: 'text-align:left', renderer: inPutVender},
    {header: "最小包装量", dataIndex: 'MIN_PACKAGE', style: 'text-align:center'},
    {header: "计划价", dataIndex: 'SALE_PRICE3', style: 'text-align:right'},
    {header: "计划数量", dataIndex: 'PLAN_QTY', style: 'text-align:right', renderer: inPutPlanQty},
    {header: "采购价格", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
    {header: "可用库存", dataIndex: 'ITEM_QTY', style: 'text-align:right'},
    {header: "BO量", dataIndex: 'BO_QTY', style: 'text-align:right'},
    {header: "月均销量", dataIndex: 'AVG_QTY', style: 'text-align:right'},
    {header: "年平均销量", dataIndex: 'YEAR_QTY', style: 'text-align:right'},
    {header: "半年平均销量", dataIndex: 'HFYEAR_QTY', style: 'text-align:right'},
    {header: "季平均销量", dataIndex: 'QUARTER_QTY', style: 'text-align:right'},
    {header: "在途量", dataIndex: 'ORDER_QTY', style: 'text-align:right'},
    {header: "安全库存", dataIndex: 'SAFETY_STOCK', style: 'text-align:right'},
    {header: "预计到货日期", dataIndex: 'PREARRIVEDATE', style: 'text-align:center'}
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
    return "<input type='input' name='planQtyInput' id='PQty"+num+"' value='" + value + "' onchange='check(this,"+partId+");' onkeydown='toNext("+num+","+partId+")'/>";

}

function toNext(num,partId1){
	if(event.keyCode==40){
		var flag = true;
		var planQty = $("PQty"+(num)).value;
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
		                var partType = $("partType"+partId).value;//配件属性
		                var partTypeName = mt.rows[i].cells[5].innerText;  //配件属性名称
		                var unit = mt.rows[i].cells[6].innerText;  //单位
		                var venderId = $("vender"+partId).value;  //供应商id
		                var venderName = mt.rows[i].cells[7].innerText;  //供应商名称
		                var minPackage = mt.rows[i].cells[8].innerText;  //最小包装量
		                var salePrice3 = mt.rows[i].cells[9].innerText;  //计划价
		                var planQty = mt.rows[i].cells[10].childNodes[0].value;  //计划数量
		                var buyPrice = mt.rows[i].cells[11].innerText;  //采购价格
		                var itemQty = mt.rows[i].cells[12].innerText;  //可用库存量
		                var boQty = mt.rows[i].cells[13].innerText;  // BO量
		                var avgQty = mt.rows[i].cells[14].innerText;  //月均销量
		                var yearQty = mt.rows[i].cells[15].innerText;  //年平均销量
		                var hfyearQty = mt.rows[i].cells[16].innerText;  //半年平均销量
		                var quarterQty = mt.rows[i].cells[17].innerText;  //季平均销量
		                var orderQty = mt.rows[i].cells[18].innerText;  //在途量
		                var safetyStock = mt.rows[i].cells[19].innerText;  //安全库存量
		                var preArriveDate = mt.rows[i].cells[20].innerText;  //到货日期
		                addCell(partId, partCode, partOldcode, partCname,partType,partTypeName, unit,venderId,venderName, minPackage, salePrice3, planQty, buyPrice, itemQty, boQty, avgQty,yearQty,hfyearQty,quarterQty, orderQty, safetyStock, preArriveDate);
		                refeshQtyAndAmount();
		                break;
		            }
			 }
			 
		 }
		 if($("PQty"+(num+1))){
			 var val = $("PQty"+(num+1)).value;
			 $("PQty"+(num+1)).focus();
			 $("PQty"+(num+1)).value="";
			 $("PQty"+(num+1)).value=val;
		 }
	}
	if(event.keyCode==38){
		 if($("PQty"+(num-1))){
			 var val = $("PQty"+(num-1)).value;
			 $("PQty"+(num-1)).focus();
			 $("PQty"+(num-1)).value="";
			 $("PQty"+(num-1)).value=val;
		 }
	}
}

function toNext1(partId){
	 var cb = document.getElementsByName("cb");
	 var obj = $("plan_qty_"+partId);
	 var idx = obj.parentElement.parentElement.rowIndex-1;
	 if(event.keyCode==40){
		 var flag = true;
		 if(obj.value==0){//如果输入0,就删除这行
		    	deleteTblRow(obj);
		    	flag = false;
		 }
		 if(flag&&cb[idx]&&$("plan_qty_"+(cb[idx].value))){
			 var val = $("plan_qty_"+(cb[idx].value)).value;
			 $("plan_qty_"+(cb[idx].value)).focus();
			 $("plan_qty_"+(cb[idx].value)).value="";
			 $("plan_qty_"+(cb[idx].value)).value=val;
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
			 var val = $("plan_qty_"+(cb[idx-2].value)).value;
			 $("plan_qty_"+(cb[idx-2].value)).focus();
			 $("plan_qty_"+(cb[idx-2].value)).value="";
			 $("plan_qty_"+(cb[idx-2].value)).value=val;
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
    str += "<select  id='" + id + "' name='" + name + "'  style='width:60px;'>";
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
                var partType = $("partType"+partId).value;//配件属性
                var partTypeName = mt.rows[i].cells[5].innerText;  //配件属性名称
                var unit = mt.rows[i].cells[6].innerText;  //单位
                var venderId = $("vender"+partId).value;  //供应商id
                var venderName = mt.rows[i].cells[7].innerText;  //供应商名称
                var minPackage = mt.rows[i].cells[8].innerText;  //最小包装量
                var salePrice3 = mt.rows[i].cells[9].innerText;  //计划价
                var planQty = mt.rows[i].cells[10].childNodes[0].value;  //计划数量
                var buyPrice = mt.rows[i].cells[11].innerText;  //采购价格
                var itemQty = mt.rows[i].cells[12].innerText;  //可用库存量
                var boQty = mt.rows[i].cells[13].innerText;  // BO量
                var avgQty = mt.rows[i].cells[14].innerText;  //月均销量
                var yearQty = mt.rows[i].cells[15].innerText;  //年平均销量
                var hfyearQty = mt.rows[i].cells[16].innerText;  //半年平均销量
                var quarterQty = mt.rows[i].cells[17].innerText;  //季平均销量
                var orderQty = mt.rows[i].cells[18].innerText;  //在途量
                var safetyStock = mt.rows[i].cells[19].innerText;  //安全库存量
                var preArriveDate = mt.rows[i].cells[20].innerText;  //到货日期
                addCell(partId, partCode, partOldcode, partCname,partType,partTypeName, unit,venderId,venderName, minPackage, salePrice3, planQty, buyPrice, itemQty, boQty, avgQty,yearQty,hfyearQty,quarterQty, orderQty, safetyStock, preArriveDate);
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
		minPackage, salePrice3, planQty, buyPrice, itemQty, boQty, avgQty,yearQty,hfyearQty,quarterQty, orderQty, safetyStock, preArriveDate) {
    var tbl = document.getElementById('file');
    var rowObj = tbl.insertRow(tbl.rows.length);
    if (tbl.rows.length % 2 == 0) {
        rowObj.className = "table_list_row2";
    } else {
        rowObj.className = "table_list_row1";
    }
    var amount = (parseFloat(salePrice3) * parseFloat(planQty)).toFixed(2)
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
    var cell20 = rowObj.insertCell(19);
    var cell21 = rowObj.insertCell(20);
    var cell22 = rowObj.insertCell(21);
    var cell23 = rowObj.insertCell(22);
    cell1.innerHTML = '<tr><td align="center" nowrap><input  type="checkbox" value="' + partId + '" id="cell_' + (tbl.rows.length - 2) + '" name="cb" checked="true" onclick="refeshQtyAndAmount();" /></td>';
    cell2.innerHTML = '<td align="center" nowrap><span id="orderLine_SEQ" >' + (tbl.rows.length - 2) + '</span><input id="idx_' + partId + '" name="idx_' + partId + '" value="' + (tbl.rows.length - 2) + '" type="hidden" ></td>';
    cell3.innerHTML = '<td style="text-align: left" ><input   name="partOldcode_' + partId + '" id="partOldcode_' + partId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    cell4.innerHTML = '<td style="text-align: left"><input   name="partCname_' + partId + '" id="partCname_' + partId + '" value="' + partCname + '" type="hidden" />' + partCname + '</td>';
    cell5.innerHTML = '<td style="text-align: left"><input   name="partCode_' + partId + '" id="partCode_' + partId + '" value="' + partCode + '" type="hidden" />' + partCode + '</td>';
    cell6.innerHTML = '<td style="text-align: left"><input   name="partType_' + partId + '" id="partType_' + partId + '" value="' + partType + '" type="hidden" />' + partTypeName + '</td>';
    cell7.innerHTML = '<td align="center" nowrap><input   name="unit_' + partId + '" id="unit_' + partId + '" value="' + unit + '" type="hidden" />' + unit + '</td>';
    cell8.innerHTML = '<td align="center" nowrap><input   name="venderId_' + partId + '" id="venderId_' + partId + '" value="' + venderId + '" type="hidden" />' + venderName + '</td>';
    cell9.innerHTML = '<td align="center" nowrap><input   name="minPackage_' + partId + '" id="minPackage_' + partId + '" value="' + minPackage + '" type="hidden" />' + minPackage + '</td>';
    cell10.innerHTML = '<td align="center" nowrap><input   name="salePrice3_' + partId + '" id="salePrice3_' + partId + '" value="' + salePrice3 + '" type="hidden" />' + salePrice3 + '</td>';
    cell11.innerHTML = '<td align="center" nowrap><input onchange="countMoney(this.parentNode.parentNode.rowIndex-1,this,' + salePrice3 + ',' + partId + ')"  name="plan_qty_' + partId + '" id="plan_qty_' + partId + '" value="' + planQty + '" type="text" onkeydown="toNext1('+partId+')"/></td>';
    cell12.innerHTML = '<td align="center" nowrap><input  name="planAmount_' + partId + '" id="planAmount_' + partId + '" value="' + amount + '" type="hidden" />' + amount + '</td>';
    cell13.innerHTML = '<td align="center" nowrap><input   name="itemQty_' + partId + '" id="itemQty_' + partId + '" value="' + itemQty + '" type="hidden" />' + itemQty + '</td>';
    cell14.innerHTML = '<td align="center" nowrap><input   name="boQty_' + partId + '" id="boQty_' + partId + '" value="' + boQty + '" type="hidden" />' + boQty + '</td>';
    cell15.innerHTML = '<td align="center" nowrap><input   name="avgQty_' + partId + '" id="avgQty_' + partId + '" value="' + avgQty + '" type="hidden" />' + avgQty + '</td>';
    cell16.innerHTML = '<td align="center" nowrap><input   name="yearQty_' + partId + '" id="yearQty_' + partId + '" value="' + yearQty + '" type="hidden" />' + yearQty + '</td>';
    cell17.innerHTML = '<td align="center" nowrap><input   name="hfyearQty_' + partId + '" id="hfyearQty_' + partId + '" value="' + hfyearQty + '" type="hidden" />' + hfyearQty + '</td>';
    cell18.innerHTML = '<td align="center" nowrap><input   name="quarterQty_' + partId + '" id="quarterQty_' + partId + '" value="' + quarterQty + '" type="hidden" />' + quarterQty + '</td>';
    cell19.innerHTML = '<td align="center" nowrap><input   name="orderQty_' + partId + '" id="orderQty_' + partId + '" value="' + orderQty + '" type="hidden" />' + orderQty + '</td>';
    cell20.innerHTML = '<td align="center" nowrap><input   name="safetyStock_' + partId + '" id="safetyStock_' + partId + '" value="' + safetyStock + '" type="hidden" />' + safetyStock + '</td>';
    cell21.innerHTML = '<td align="center" nowrap><input   name="preArriveDate_' + partId + '" id="preArriveDate_' + partId + '" value="' + preArriveDate + '" type="hidden" />' + preArriveDate + '</td>';
    cell22.innerHTML = '<td align="center" nowrap><input class="short_txt" name="remark_' + partId + '" id="remark_' + partId + '" type="text"/></td>';
    cell23.innerHTML = '<td><input  type="button" class="short_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" /></td></TR>';

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
    tbl.rows[loc + 1].cells[11].innerHTML = '<td align="center" nowrap>' + '<input   name="planAmount_' + partId + '" id="planAmount_' + partId + '" value="' + money + '" type="hidden" />' + money + '</td>';
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
            if ("" != cb[i].value && null != document.getElementById("planAmount_" + cb[i].value) && "" != document.getElementById("planAmount_" + cb[i].value).value) {
                amountCount = (parseFloat(amountCount) + parseFloat(document.getElementById("planAmount_" + cb[i].value).value)).toFixed(2);
                ;
            }
        }
    }
    document.getElementById("SUM_QTY").value = qtyCount;
    document.getElementById("AMOUNT").value = amountCount;
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
    if ($("PLAN_TYPE").value == "") {
        MyAlert("请选择计划类型!");
        return;
    }
    if ($("wh_id").value == "") {
        MyAlert("请选择仓库!");
        return;
    }
    /* if ($("t3").value == "") {
        MyAlert("请选择计划日期!");
        return;
    } */
    if ($("t2").value == "") {
        MyAlert("请选择预计到货日期!");
        return;
    }
    /* if (checkSys_Sel_Date($("t3").value, "yyyy-MM-dd")) {
        MyAlert("计划日期不能小于制单日期!");
        return;
    } */
    if($("PLAN_CYCLE").value==0||$("PLAN_CYCLE").value==""){
    	MyAlert("请输入合法的订货周期!");
    	return;
    }
    if($("COME_CYCLE").value==0||$("COME_CYCLE").value==""){
    	MyAlert("请输入合法的到货周期!");
    	return;
    }
    if (checkSys_Sel_Date($("t2").value, "yyyy-MM-dd")) {
        MyAlert("预计到货日期不能小于制单日期!");
        return;
    }

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
	var sys_date = document.getElementById("sys_date__").value;
	var date_array = sys_date.split(","); 
	var yyyy = date_array[0];
	var MM = date_array[1];
	var dd = date_array[2];
	var hh = date_array[3];
	var mm = date_array[4];
	var ss = date_array[5];
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

function savePlan() {
    disableAllStartBtn();
    var url = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/savePlan.json";
    sendAjax(url, getResult, 'fm');
}

function getResult(jsonObj) {
    enableAllStartBtn();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            window.location.href = '<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/purchasePlanSettingInit.do';
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}
//修改提示框超出6行会超出框的BUG
function MyAlert(info) {
    var owner = getTopWinRef();
    try {
        _dialogInit();
        owner.getElementById('dialog_content_div').innerHTML = '\
		    <div style="font-size:12px;">\
		     <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
		      <b>信息</b>\
		     </div>\
		     <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
		     <div style="padding:2px;text-align:center;background:#D0BFA1;">\
		      <input id="dialog_alert_button" type="button" value="确定" style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
		     </div>\
		    </div>';
        owner.getElementById('dialog_alert_info').innerHTML = info;
        owner.getElementById('dialog_alert_button').onclick = _hide;
        var height = 200;

        if (info.split('</br>').length >= 6) {
            height = height + (info.split('</br>').length - 6) * 27;
        }
        _setSize(300, height);

        _show();
    } catch (e) {
        MyAlert('MyAlert : ' + e.name + '=' + e.message);
    } finally {
        owner = null;
    }
}
function exportExcelTemplate() {
    fm.action = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/exportExcelTemplate.do";
    fm.submit();
}
function uploadEx() {
    var url1 = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/uploadPartPlanExcel.json";
    disableAllStartBtn();
    sendAjax(url1, getUploadResult, 'fm');
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
    fm.action = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/savePlan.do";
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

    if ($("wh_id").value == "") {
        MyAlert("请选择仓库!");
        return;
    }
    /* if ($("t3").value == "") {
        MyAlert("请选择计划日期!");
        return;
    } */
    if ($("t2").value == "") {
        MyAlert("请选择预计到货日期!");
        return;
    }
    if (checkSys_Sel_Date($("t2").value, "yyyy-MM-dd")) {
        MyAlert("预计到货日期不能小于制单日期!");
        return;
    }
   
    /* if (checkSys_Sel_Date($("t2").value, "yyyy-MM-dd")) {
        MyAlert("预计到货日期不能小于当前日期!");
        return;
    }
    if(checkSys_Sel_Date1($("t3").value,$("t2").value)){
    	MyAlert("计划日期不能大于预计到货日期!");
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
            if (document.getElementById("salePrice3_" + cb[i].value).value == "0") {
                msg += "第" + document.getElementById("idx_" + cb[i].value).value + "行的计划价不能为零!</br>";
                flag = true;
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
        MyConfirm("确定保存计划?", savePlan, []);
    } else {
        refeshQtyAndAmount();
        MyConfirm("确定提交并保存计划?", saveAndCommit, []);
    }
    for (var i = 0; i < cb.length; i++) {
        cb[i].disabled = false;
    }
}
function goBack() {
    fm.action = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/purchasePlanSettingInit.do";
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
    if ($("PLAN_TYPE").value == "") {
        MyAlert("请选择计划类型!");
        return;
    }
    if ($("wh_id").value == "") {
        MyAlert("请选择仓库!");
        return;
    }
    var uploadDiv = $("uploadDiv");
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
    $("SUM_QTY").value = "";
    $("AMOUNT").value = "";
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

function clearMInput() {//清空选定制造商
	var makerId = document.getElementById("MAKER_ID").value;
	if(makerId!=null&&makerId!=""){
		 document.getElementById("MAKER_ID").value = '';
	     document.getElementById("MAKER_NAME").value = '';
	}
}

var curVenderId;
var curMakerId;
var curT3;
var curT2;

function changeDiv1(){
	$("t2").attachEvent('onpropertychange',function(o){
		if(o.propertyName=='value'){//如果是value属性才执行
			var t2 = $("t2").value;
			var partDiv = document.getElementById("partDiv");
		    var addPartViv = document.getElementById("addPartViv");
		    var tbl = document.getElementById('file');
		    var len = tbl.rows.length;
		    if (t2 != curT2) {
		        if (len > 2) {
		            //改变计划日期之后就要删除明细,重新选择
		            for (var i = tbl.rows.length - 1; i >= 2; i--) {
		                tbl.deleteRow(i);
		            }
		        }
		        addPartViv.value = "增加";
		        partDiv.style.display = "none";
		        $("SUM_QTY").value = "";
		        $("AMOUNT").value = "";
		    }
		    //重新计算订货周期
		    var t3Arr = t2.split("-");
		    var dt = new Date(t3Arr[0], t3Arr[1]-1, t3Arr[2]);
		    
		    var createDate = $("createDate").value;
		    var cArr = createDate.split("-");
		    var cDt = new Date(cArr[0], cArr[1]-1, cArr[2]);

		    var cha = (dt.getTime()-cDt.getTime())/(24 * 60 * 60 * 1000);
		    var num = new Number(cha);
		    var planCycle = num.toFixed(2);
		    $("PLAN_CYCLE").value = planCycle;
		    curT2 = t2;
		}
	});
}

var curPlanCycle;
function changeDiv3(){
	var planCycle = $("PLAN_CYCLE").value;
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
        $("SUM_QTY").value = "";
        $("AMOUNT").value = "";
    }
	
	if(!planCycle){
        MyAlert("订货周期不能为空!");
        $("PLAN_CYCLE").value = "";
        $("PLAN_CYCLE").focus();
        return;
    }else{
    	/*var pattern1 = /^[1-9][0-9]*$/;
        if (!pattern1.exec(planCycle)) {
        	$("PLAN_CYCLE").value = planCycle.replace(/\D/g, '');
        }*/
        if(isNaN(planCycle)){
        	MyAlert("订货周期不合法!");
        	$("PLAN_CYCLE").value = "";
            $("PLAN_CYCLE").focus();
        	return;
        }
    }
	curPlanCycle = planCycle;
	
	var num = new Number(planCycle);
    var planCycle1 = num.toFixed(2);
    $("PLAN_CYCLE").value = planCycle1;
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
        $("SUM_QTY").value = "";
        $("AMOUNT").value = "";
    }
	
	if(!comeCycle){
        MyAlert("到货周期不能为空!");
        $("COME_CYCLE").value = "";
        $("COME_CYCLE").focus();
        return;
    }else{
        if(isNaN(comeCycle)){
        	MyAlert("到货周期不合法!");
        	$("COME_CYCLE").value = "";
            $("COME_CYCLE").focus();
        	return;
        }
    }
	curComeCycle = comeCycle;
	
	var num = new Number(comeCycle);
    var comeCycle1 = num.toFixed(2);
    $("COME_CYCLE").value = comeCycle1;
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
--></script>
</HEAD>

<BODY onunload='javascript:destoryPrototype()' onload="autoAlertException();enableAllStartBtn();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" name="planState" id="planState"/>
<input type="hidden" name="planType" id="planType" value="<%=Constant.PART_PURCHASE_PLAN_TYPE_01%>">

<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置>配件管理>采购计划管理>计划维护>计划新增</div>
<div>
    <table class="table_query">

        <tr>
            <th colspan="6" width="100%"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 计划单信息</th>
        </tr>
        <tr>
            <td width="10%"   align="right">计划单号:</td>
            <td width="20%" align="left">${planCode}
            <input type="hidden" name="planCode" value="${planCode}"/>
            </td>
            <td width="10%"   align="right">计划员:</td>
            <td align="left" width="20%">${name}
            <input type="hidden" name="planName" value="${name}"/>
            </td>
            <td width="10%"   align="right">制单日期:</td>
            <td align="left" width="21%">${now}
            <input type="hidden" name="createDate" id="createDate" value="${now}"/>
            </td>
        </tr>
        <tr>
            <!-- <td width="10%"   align="right">计划日期:</td>
            <td width="20%" align="left">
                <!-- <script type="text/javascript">
                    getYearSelect("YEAR", "YEAR", 1, '');
                </script>
                <script type="text/javascript">
                    getMonThSelect("MONTH", "MONTH", '');
                </script>
                <input name="beginTime2" id="t3" value="" type="text" readonly="readonly" class="short_txt" >
                <input name="button" value=" " type="button" class="time_ico" title="点击选择时间" onclick="showcalendar(event, 't3', false);" value="" />
                <font color="#FF000">*</font>
                </td> -->
            <td width="10%"   align="right">计划类型:</td>
            <td width="20%" align="left">
                <script type="text/javascript">
                    genSelBoxExp("PLAN_TYPE", <%=Constant.PART_PURCHASE_PLAN_TYPE%>, "<%=Constant.PART_PURCHASE_PLAN_TYPE_01%>", false, "short_sel", "disabled=disabled", "true", '');
                </script>
                </td>
            <td width="10%"   align="right">库房:</td>
            <td width="20%" align="left">
                <select name="wh_id" id="wh_id" class="short_sel" onchange="doCusChange(this.value)">
                    <option selected value=''>-请选择-</option>
                    <c:forEach items="${wareHouseList}" var="wareHouse">
                        <option value="${wareHouse.WH_ID}" selected="${wareHouse.WH_ID==wh_id?'selected':''}">${wareHouse.WH_NAME}</option>
                    </c:forEach>
                </select> 
                <font color="#FF000">*</font>
            </td>
            <td width="10%"   align="right">预计到货日期:</td>
            <td width="20%" align="left">
                <input name="beginTime2" id="t2" type="text" readonly="readonly" class="short_txt"  value="${beginTime2}" >
                <input name="button" value=" " type="button" class="time_ico" title="点击选择时间" onclick="showcalendar(event, 't2', false);changeDiv1();"/>
                <font color="#FF000">*</font>
            </td>
        </tr>
        <tr>
            <td width="10%"  align="right">过滤负数:</td>
            <td width="20%" align="left">
                <input  type="checkbox" name="NEGATIVE_FILT" id="NEGATIVE_FILT" value="1" checked="checked" onclick="__extQuery__(1);" />
                <font color="#FF000">*</font>
            <td width="10%"   align="right">订货周期(天):</td>
            <td width="20%" align="left">
            <input  class="short_txt" type="text" name="PLAN_CYCLE" id="PLAN_CYCLE" value="${planCycle eq null?30:planCycle }" onchange="changeDiv3();"/>
            <font color="#FF000">*</font>
            <!--<input  class="short_txt" type="text" name="PLAN_CYCLE" id="PLAN_CYCLE" value="" readonly="readonly" style="border:0px;background-color:#F3F4F8;"/>-->
            </td>
            <td width="10%"   align="right">到货周期(天):</td>
            <td width="20%" align="left">
            <input  class="short_txt" type="text" name="COME_CYCLE" id="COME_CYCLE" value="${comeCycle eq null?30:comeCycle }" onchange="changeDiv4();"/>
            <font color="#FF000">*</font>
            </td>
           
        </tr>
        <tr>
        <td width="10%"   align="right">总数量:</td>
            <td width="20%" align="left"><input readonly class="phone_txt" type="text"
                                                style="border:0px;background-color:#F3F4F8;" name="SUM_QTY"
                                                id="SUM_QTY"/>
            </td>
            <td width="10%"   align="right">总金额:</td>
            <td width="20%" align="left"><input class="phone_txt" type="text"
                                                style="border:0px;background-color:#F3F4F8;"
                                                readonly name="AMOUNT" id="AMOUNT"/></td>
        </tr>
        <tr>
            <td   align="right">备注:</td>
            <td align="left" colspan="5">
                <textarea style="width:95%" id="remark" name="remark">${remark }</textarea></td>
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
                <td width="20%">
                    <input class="middle_txt" id="PART_CNAME"
                           datatype="1,is_noquotation,30" name="PART_CNAME"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td width="10%"   align="right">
                    件号：
                </td>
                <td width="22%">
                    <input class="middle_txt" id="PART_CODE"
                           datatype="1,is_noquotation,30" name="PART_CODE"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
            </tr>
            <tr>
                <td   align="right" width="10%">
                    供应商：
                </td>
                <td width="20%">
                    <input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME"/>
                    <input class="mark_btn" type="button" value="&hellip;"
                           onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
                           <INPUT class=mini_btn onclick="clearInput();" value=清除 type=button name=clrBtn>
                    <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
                </td>
                <td   align="right" width="10%">
                    制造商：
                </td>
                <td width="22%">
                    <input class="middle_txt" type="text" readonly="readonly" id="MAKER_NAME" name="MAKER_NAME"/>
                    <input class="mark_btn" type="button" value="&hellip;"
                           onclick="showPartMaker('MAKER_NAME','MAKER_ID','false')"/>
                           <INPUT class=mini_btn onclick="clearMInput();" value=清除 type=button name=clrBtn>
                    <input id="MAKER_ID" name="MAKER_ID" type="hidden" value="">
                </td>
                <td width="10%"  align="right">
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
                <input  class="short_txt" type="text" name="PLANQTY" id="PLANQTY" value="" onchange="validateQty(this);"/>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="6">
                    <input class="normal_btn" type="button" name="BtnQuery"
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
    <tr>
        <th colspan="23" align="left">
            <img src="<%=contextPath%>/img/nav.gif"/>详细计划
    </tr>
    <tr class="table_list_row0">
        <td>
            <input type="checkbox" onclick="selAll2(this)"/>
        </td>
        <td>
            序号
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
            配件属性
        </td>
        <td>
            单位
        </td>
        <td>
            供应商
        </td>
        <td>
            最小包装量
        </td>
        <td>
            计划价
        </td>
        <td>
            计划量
        </td>
        <td>
            计划金额
        </td>
        <td>
            可用库存量
        </td>
        <td>
            BO量
        </td>
        <td>
            月均销量
        </td>
        <td>
            年平均销量
        </td>
        <td>
            半年平均销量
        </td>
        <td>
            季平均销量
        </td>
        <td>
            在途量
        </td>
        <td>
            安全库存量
        </td>
        <td>
            预计到货日期
        </td>
        <td>
            备注
        </td>
        <td>
            操作
        </td>
    </tr>
    <c:if test="${list !=null}">
	  <c:forEach items="${list}" var="list" varStatus="_sequenceNum" step="1">
	    <c:if test="${((_sequenceNum.index+1) mod 2) != 0}">
		<tr class="table_list_row1">
		</c:if>
		<c:if test="${((_sequenceNum.index+1) mod 2) == 0}">
		<tr class="table_list_row2">
		</c:if>
		  <td align="center" nowrap>
		    <input  type="checkbox" value="${list.PART_ID}" id="cell_${_sequenceNum.index+1}" name="cb" checked="checked" onclick="refeshQtyAndAmount();"/>
		  </td>
		  <td align="center" nowrap>
		  <span id="orderLine_SEQ" >${_sequenceNum.index+1}</span>
		    <input id="idx_${list.PART_ID}" name="idx_${list.PART_ID}" value="${_sequenceNume.index+1}" type="hidden" >
		  </td>
		  <td align="left">
		    <input   name="partOldcode_${list.PART_ID}" id="partOldcode_${list.PART_ID}" value="${list.PART_OLDCODE}" type="hidden" />${list.PART_OLDCODE}
		  </td>
		  <td align="left" nowrap>
		    <input   name="partCname_${list.PART_ID}" id="partCname_${list.PART_ID}" value="${list.PART_CNAME}" type="hidden" class="cname_${list.partId}"/>${list.PART_CNAME}
		  </td>
		  <td align="left" nowrap>
		    <input   name="partCode_${list.PART_ID}" id="partCode_${list.PART_ID}" value="${list.PART_CODE}" type="hidden" class="cname_${list.partId}"/>${list.PART_CODE}
		  </td>
		  <td align="left" nowrap>
		    <input   name="partType_${list.PART_ID}" id="partType_${list.PART_ID}" value="${list.PART_TYPE}" type="hidden" />${list.PARTTYPENAME}
		  </td>
		  <td align="center" nowrap>
		    <input   name="unit_${list.PART_ID}" id="unit_${list.PART_ID}" value="${list.UNIT}" type="hidden" />${list.UNIT}
		  </td>
		  <td align="center" nowrap>
		    <input   name="venderId_${list.PART_ID}" id="venderId_${list.PART_ID}" value="${list.VENDER_ID}" type="hidden" />${list.VENDER_NAME}
		  </td>
		  <td align="center" nowrap>
		    <input   name="minPackage_${list.PART_ID}" id="minPackage_${list.PART_ID}" value="${list.MIN_PACKAGE}" type="hidden" />${list.MIN_PACKAGE}
		  </td>
		  <td align="center" nowrap>
		    <input   name="salePrice3_${list.PART_ID}" id="salePrice3_${list.PART_ID}" value="${list.SALE_PRICE3}" type="hidden" />${list.SALE_PRICE3}
		  </td>
		  <td align="center" nowrap>
		    <input onchange="countMoney(this.parentNode.parentNode.rowIndex-1,this,${list.SALE_PRICE3},${list.PART_ID})"  name="plan_qty_${list.PART_ID}" id="plan_qty_${list.PART_ID}" value="${list.QTY}" type="text" onkeydown="toNext1(${list.PART_ID})"/>
		  </td>
		  <td align="center" nowrap>
		    <input name="planAmount_${list.PART_ID}" id="planAmount_${list.PART_ID}" value="${list.AMOUNT}" type="hidden" />${list.AMOUNT}
		  </td>
		  <td align="center" nowrap>
		    <input name="itemQty_${list.PART_ID}" id="itemQty_${list.PART_ID}" value="${list.ITEM_QTY}" type="hidden" />${list.ITEM_QTY}
		  </td>
		  <td align="center" nowrap>
		    <input name="boQty_${list.PART_ID}" id="boQty_${list.PART_ID}" value="${list.BO_QTY}" type="hidden" />${list.BO_QTY}
		  </td>
		  <td align="center" nowrap>
		    <input name="avgQty_${list.PART_ID}" id="avgQty_${list.PART_ID}" value="${list.AVG_QTY}" type="hidden" />${list.AVG_QTY}
		  </td>
		  <td align="center" nowrap>
		    <input name="yearQty_${list.PART_ID}" id="yearQty_${list.PART_ID}" value="${list.YEAR_QTY}" type="hidden" />${list.YEAR_QTY}
		  </td>
		  <td align="center" nowrap>
		    <input name="hfyearQty_${list.PART_ID}" id="hfyearQty_${list.PART_ID}" value="${list.HFYEAR_QTY}" type="hidden" />${list.HFYEAR_QTY}
		  </td>
		  <td align="center" nowrap>
		    <input name="quarterQty_${list.PART_ID}" id="quarterQty_${list.PART_ID}" value="${list.QUARTER_QTY}" type="hidden" />${list.QUARTER_QTY}
		  </td>
		  <td align="center" nowrap>
		    <input name="orderQty_${list.PART_ID}" id="orderQty_${list.PART_ID}" value="${list.ORDER_QTY}" type="hidden" />${list.ORDER_QTY}
		  </td>
		  <td align="center" nowrap>
		    <input name="safetyStock_${list.PART_ID}" id="safetyStock_${list.PART_ID}" value="${list.SAFETY_STOCK}" type="hidden" />${list.SAFETY_STOCK}
		  </td>
		  <td align="center" nowrap>
		    <input name="preArriveDate_${list.PART_ID}" id="preArriveDate_${list.PART_ID}" value="${list.PREARRIVEDATE}" type="hidden" />${list.PREARRIVEDATE}
		  </td>
		  <td align="center" nowrap>
		    <input class="short_txt" name="remark_${list.PART_ID}" id="remark_${list.PART_ID}" value="${list.REMARK}" type="text" />
		  </td>
		  <td>
		    <input  type="button" class="short_btn"  name="queryBtn4" value="删除" onclick="deleteTblRow(this);" />
		  </td>
		</tr>
	  </c:forEach>
	</c:if>
</table>
<table width="100%" align="center">
    <tr>
        <td height="2"></td>
    </tr>
    <tr>
        <td align="center">
            <input class="cssbutton" type="button" value="上传文件" name="button1"
                   onclick="showUpload();"> &nbsp;
            <input class="cssbutton" type="button" value="保存" name="button1"
                   onclick="confirmSubmit(1);">
            &nbsp;
            <input class="cssbutton" type="button" value="提交" name="button1"
                   onclick="confirmSubmit(2)">
            &nbsp;
            <input class="cssbutton" type="button" value="返回" name="button1"
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
</div>
</form>
</BODY>
</html>

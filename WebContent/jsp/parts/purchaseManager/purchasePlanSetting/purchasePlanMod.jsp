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
	Map mainMap = (Map)request.getAttribute("mainMap");
	List detailList = (List)request.getAttribute("detailList");
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
.table_list {
	width:100%;
	border-collapse:collapse;
	text-align:center;
	margin-top:2px;
}
input.short_btn {
	width:35px;
	height:16px;
	color:#1E3988;
}
</style>
<LINK href="<%=contextPath%>/style/page-info.css" type="text/css"/>
<SCRIPT type=text/javascript src="<%=contextPath%>/js/jslib/calendar.js"></SCRIPT>
<SCRIPT type=text/javascript src="<%=contextPath%>/js/jslib/prototype.js"></SCRIPT>
<SCRIPT type=text/javascript src="<%=contextPath%>/js/framecommon/default.js"></SCRIPT>
<SCRIPT type=text/javascript src="<%=contextPath%>/js/framecommon/DialogManager.js"></SCRIPT>
<SCRIPT type=text/javascript charset=UTF-8 src="<%=contextPath%>/js/validate/validate.js"></SCRIPT>
<SCRIPT type=text/javascript>
 var myPage;

 var url = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/showPartBase.json";

 var title = null;

 var columns = [
				{header: "序号", align:'center', renderer:getIndex,width:'7%'},
				{header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', align:'center',width: '33px' ,renderer:seled},
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

function getYearSelect(id,name,scope,value){
		value = value.split("-")[0];

		var date = new Date();
		var year = date.getFullYear();    //获取完整的年份
		var str = "";
		str += "<select  id='" +id+ "' name='" + name +"'  style='width:73px;'>" ;
		str += "<option  value=''>-请选择-</option>";
		for(var i=(year-scope);i<=(year+scope);i++){

			str += "<option  "+(i == value ? "selected" : "")+ " value ="+i+">"+i+"</option >";
		}
		str += "</select> 年";

		document.write(str);
}
function getMonThSelect(id,name,value){
		value = value.split("-")[1];
		var str = "";
		str += "<select  id='" +id+ "' name='" + name +"'  style='width:73px;'>" ;
		str += "<option selected value=''>-请选择-</option>";
		for(var i=1;i<=12;i++){
			str += "<option "+(i == value ? "selected" : "")+" value ="+(i < 10 ? "0"+i : i)+">"+(i < 10 ? "0"+i : i)+"</option >";
		}
		str += "</select> 月";
		document.write(str);
}

 	function addPartDiv(){
		var partDiv = document.getElementById("partDiv");
		var addPartViv = document.getElementById("addPartViv");
		if(partDiv.style.display=="block" ){
			addPartViv.value="增加";
			partDiv.style.display = "none";
		}else{
			addPartViv.value="收起";
			partDiv.style.display="block" ;
			__extQuery__(1);
		}
	}
 function deleteTblRow(obj) {
     var idx = obj.parentElement.parentElement.rowIndex;
     var tbl = document.getElementById('file');
     tbl.deleteRow(idx);
     refreshMtTable('orderLine','SEQ');//刷新行号
     refeshQtyAndAmount();//刷新总金额和数量
 }
 //计算金额和数量
 function refeshQtyAndAmount(){
     var qtyCount=parseFloat(0);
     var amountCount=parseFloat(0);
     var cb = document.getElementsByName("cb");
     for(var i=0 ;i<cb.length;i++){
         if(cb[i].checked){
             if(""!=cb[i].value&&null!=document.getElementById("plan_qty_"+cb[i].value)&&""!=document.getElementById("plan_qty_"+cb[i].value).value){
                 qtyCount =( parseFloat(qtyCount)+parseFloat(document.getElementById("plan_qty_"+cb[i].value).value));
             }
             if(""!=cb[i].value&&null!=document.getElementById("planAmount_"+cb[i].value)&&""!=document.getElementById("planAmount_"+cb[i].value).value){
                 amountCount =(parseFloat(amountCount)+ parseFloat(document.getElementById("planAmount_"+cb[i].value).value)).toFixed(2);;
             }}
     }
     document.getElementById("SUM_QTY").value=qtyCount;
     document.getElementById("AMOUNT").value=amountCount;
 }
 //刷新行号
 function refreshMtTable(mtId, strType){
     if(strType == "SEQ"){
         var oSeq = eval("document.all." + mtId + "_SEQ");
         if(oSeq != null && oSeq.length != null){
             for(var i=0; i<oSeq.length; i++){
                 oSeq[i].innerText = (i + 1);
             }
         }
     }
 }
	function selAll(obj){
		var cks = document.getElementsByName('ck');
		for(var i =0 ;i<cks.length;i++){
			if(obj.checked){
				cks[i].checked = true;
			}else{
				cks[i].checked = false;
			}
            refeshQtyAndAmount();
		}
}
function selAll2(obj){
		var cb = document.getElementsByName('cb');
		for(var i =0 ;i<cb.length;i++){
			if(obj.checked){
				cb[i].checked = true;
			}else{
				cb[i].checked = false;
			}
            refeshQtyAndAmount();
		}
}
 function countMoney(loc, obj, price, partId) {
     //MyAlert(loc+":"+obj+":"+price+":"+partId);
     if ("" == obj.value) {
         return;
     }
     var tbl = document.getElementById('file');
     var value = obj.value;
     if (isNaN(value)) {
         MyAlert("请输入数字!");
         obj.value =  obj.value.replace(/\D/g,'');
         return;
     }
     var re = /^[1-9]+[0-9]*]*$/;
     if (value!=0&&!re.test(value)) {
         MyAlert("请输入正整数!");
         obj.value = "";
         return;
     }

     var money = (parseFloat(price) * parseFloat(value)).toFixed(2);
     tbl.rows[loc + 1].cells[9].innerHTML = '<td align="center" nowrap>' + '<input   name="planAmount_' + partId + '" id="planAmount_' + partId + '" value="' + money + '" type="hidden" />' + money + '</td>';
     refeshQtyAndAmount();
 }
	function getIdx(){
		document.write(document.getElementById("file").rows.length-2);
	}
	function doActions(value){
			var cb = document.getElementsByName('cb');
			var msg = "";
			if(document.getElementById("PLAN_TYPE").value==""){
				msg += "请填写计划类型!</br>";
			}
			if(document.getElementById("YEAR").value==""){
				msg += "请填写计划年!</br>";
			}
			if(document.getElementById("MONTH").value==""){
				msg += "请填写计划月!</br>";
			}
           if(document.getElementById("wh_id").value==""){
                msg += "请填写库房!</br>";
           }
			//提交时,将其属性设置成DISABLED,从而达到过滤选择的目的
			var flag = false;
			for(var i =0 ;i<cb.length;i++){
				//需要校验计划量是否填写
				if(document.getElementById("plan_qty_"+cb[i].value).value==""){
					msg += "请填写第"+(i+1)+"行的计划量!</br>";
				}else{
                    var  mod =  document.getElementById("plan_qty_"+cb[i].value).value%document.getElementById("minPackage_"+cb[i].value).value;
                    if(mod!=0){
                      msg += "第"+(i+1)+"行的计划数量必须为最小包装量的整数倍!</br>";
                    }
                }
                /* if(document.getElementById("salePrice3_"+cb[i].value).value=="0"){
                    msg += "第"+document.getElementById("idx_"+cb[i].value).value+"行的计划价不能为零!</br>";

                            } */
			}
			if(msg!=""){
				MyAlert(msg);
				return;
			}
			var confirmMsg = "";
			if(value==1){
				document.getElementById("planState").value=<%=Constant.PART_PURCHASE_PLAN_CHECK_STATUS_01%>;
				confirmMsg = "确定要保存修改?";
			}
			if(value==2){
				document.getElementById("planState").value=<%=Constant.PART_PURCHASE_PLAN_CHECK_STATUS_02%>;
				confirmMsg = "确定要提交并保存修改?";
			}
			MyConfirm(confirmMsg,saveData,[]);
	}

	function saveData(){

		fm.action = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/modifyPlan.do";
		fm.submit();
	}
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
	// modified by lishuai@infoservice.com.cn 2010-05-18 解决select下拉框中增加属性datatype判断bug begin
	if(nullFlag && nullFlag == "true"){
		str += " datatype='0,0,0' ";
	}
	// end
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
	
function getCheck(partId){
	var tbl = document.getElementById('file');
	var str =  '<input checked checked type="checkbox" value="'+partId+'" id="cell_'+(tbl.rows.length-2)+'" name="cb" onclick="refeshQtyAndAmount();"/>'
	document.write(str);
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

function goBack(){
    if(${mainMap.PLAN_TYPE} == <%=Constant.PART_PURCHASE_PLAN_TYPE_01.toString()%>){
        fm.action = '<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/purchasePlanSettingInit.do';
    }else{
        fm.action = '<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/purchasePlanSettingInit2.do';
    }
    fm.submit();
	}
 function confirmSubmit(value) {
     var cb = document.getElementsByName('cb');
     var msg = "";
     var msg1 = "";
     if (document.getElementById("PLAN_TYPE").value == "") {
         msg += "请填写计划类型!</br>";
     }

     //提交时,将其属性设置成DISABLED,从而达到过滤选择的目的
     var flag = false;
     for (var i = 0; i < cb.length; i++) {
         if (cb[i].checked) {
             cb[i].disabled = false;
             //需要校验计划量是否填写

             if (document.getElementById("plan_qty_" + cb[i].value).value == "") {
                 msg += "请填写第" + (i+1) + "行的计划量!</br>";
                 flag = true;
             } else {
            	 var planQty = document.getElementById("plan_qty_" + cb[i].value).value;
             	 var pattern1 = /^[1-9][0-9]*$/;
                 if (!pattern1.exec(planQty)) {
                 	msg += "第" + (i+1) + "行的计划量只能为正整数!</br>";
                 	flag = true;
                 }
                 
                 var mod = document.getElementById("plan_qty_" + cb[i].value).value % document.getElementById("minPackage_" + cb[i].value).value;
                 if (mod != 0) {
                	 msg1 += "第" + (i+1) + "行的计划数量不是最小包装量的整数倍,确定这样操作?";
                 }
             }
             if (document.getElementById("salePrice3_" + cb[i].value).value == "0") {
                 msg += "第" + (i+1) + "行的计划价不能为零!</br>";
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
 function savePlan() {
	 btnDisable();
     var url = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/modifyPlan.json";
     sendAjax(url, getResult, 'fm');
 }

 function getResult(jsonObj) {
	 btnEable();
     if (jsonObj != null) {
         var success = jsonObj.success;
         var error = jsonObj.error;
         var exceptions = jsonObj.Exception;
         if (success) {
             MyAlert(success);
             if(jsonObj.planType == <%=Constant.PART_PURCHASE_PLAN_TYPE_01.toString()%>){
                window.location.href = '<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/purchasePlanSettingInit.do';
             }else{
                 window.location.href = '<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/purchasePlanSettingInit2.do';
             }
         } else if (error) {
             MyAlert(error);
         } else if (exceptions) {
             MyAlert(exceptions.message);
         }
     }
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
 function saveAndCommit() {
     document.getElementById("planState").value =<%=Constant.PART_PURCHASE_PLAN_CHECK_STATUS_02%>;
     fm.action = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/modifyPlan.do";
     disableAllStartBtn();
     fm.submit();
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
	function validateQty(obj){
		var pattern1 = /^[1-9][0-9]*$/;
	    if (obj.value!=0&&!pattern1.exec(obj.value)) {
	    	MyAlert("计划数量输入不合法!");
	    	obj.value = "";
	    	return;
	    }
	}
</script>
</HEAD>
<BODY>
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">

<div class="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>配件管理>采购计划管理>计划查询>计划查询修改</div>
  <input type="hidden" value="${mainMap.PLAN_ID}" name="planId" id="planId" />
  <input type="hidden" name="planState" id="planState" />
    <input type="hidden" name="planType" id="planType" value="${mainMap.PLAN_TYPE}">
  <table class="table_query" bordercolor="#DAE0EE">
    <tr>
      <th width="100%" align="left" colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 计划单信息</th>
    </tr>
    <tr>
      <td bgcolor="#F3F4F8"   align="right" width="13%">计划单号:</td>
      <td width="20%" align="left">
      <c:out value="${mainMap.PLAN_CODE}" /></td>
      <td   align="right" bgcolor="#F3F4F8" width="13%">计划员:</td>
      <td align="left" bgcolor="#FFFFFF" width="20%"><c:out value="${mainMap.NAME}" /><br /></td>
      <td   align="right" bgcolor="#F3F4F8" width="13%">制单日期:</td>
      <td align="left" bgcolor="#FFFFFF" width="21%"><c:out value="${mainMap.CREATE_DATE}" /></td>
    </tr>
    <tr>
      <!-- <td width="10%"   align="right">计划日期:</td>
      <td width="20%" align="left">
       <script type="text/javascript">
        	 getYearSelect("YEAR","YEAR",1,'<c:out value="${mainMap.YEAR_MONTH}" />');
        </script>
        <script type="text/javascript">
        	 getMonThSelect("MONTH","MONTH",'<c:out value="${mainMap.YEAR_MONTH}" />');
        </script>
        ${mainMap.YEAR_MONTH}
        <input type="hidden" name="beginTime2" value="${mainMap.YEAR_MONTH }"/>
      </td> -->
      <td   align="right" bgcolor="#F3F4F8">计划类型:</td>
      <td align="left" bgcolor="#FFFFFF">
          <script type="text/javascript">
              selBox("PLAN_TYPE",<%=Constant.PART_PURCHASE_PLAN_TYPE%>,${mainMap.PLAN_TYPE},true,"short_sel","","false",'');
          </script>
      </td>
      <td   align="right" bgcolor="#F3F4F8" width="13%">库房:</td>
      <td align="left" bgcolor="#FFFFFF" width="21%">
     	<!-- <select  name="wh_id" id = "wh_id" class="short_sel">
      		<option value=''>-请选择-</option>
      		<c:forEach items="${wareHouseList}" var="wareHouse" >
      			 <option <c:if test="${wareHouse.WH_ID==mainMap.WH_ID}">selected</c:if> value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
      		</c:forEach>
      	</select> -->
      	<input type="hidden" name="wh_id" value="${mainMap.WH_ID}"/>
      	${mainMap.WH_NAME}
      </td>
      <td width="10%"   align="right">预计到货日期:</td>
            <td width="20%" align="left">
            ${mainMap.FORECAST_DATE }
            <input type="hidden" name="beginTime2" value="${mainMap.FORECAST_DATE }"/>
            </td>
    </tr>
    <tr>
            <td width="10%"   align="right">订货周期(天):</td>
            <td width="20%" align="left">
            ${mainMap.PLAN_CYCLE }
            <input type="hidden" name="PLAN_CYCLE" value="${mainMap.PLAN_CYCLE }"/>
            </td>
            <td width="10%"   align="right">到货周期(天):</td>
            <td width="20%" align="left">
            ${mainMap.COME_CYCLE }
            <input type="hidden" name="COME_CYCLE" value="${mainMap.COME_CYCLE }"/>
            <font color="#FF000">*</font>
            </td>
        </tr>
        <tr>
        <td bgcolor="#F3F4F8"   align="right">总数量:</td>
      <td bgcolor="#FFFFFF" align="left"><input class="phone_txt" type="text" style="border:0px;background-color:#F3F4F8;" readonly  name="SUM_QTY" id="SUM_QTY" value="${mainMap.SUM_QTY}" /></td>
      <td bgcolor="#F3F4F8"   align="right">总金额:</td>
      <td bgcolor="#FFFFFF" align="left"><input class="phone_txt" type="text" style="border:0px;background-color:#F3F4F8;" readonly name="AMOUNT" id="AMOUNT" value="${mainMap.AMOUNT}" /></td>
        </tr>
    <tr>
      <td   align="right" bgcolor="#F3F4F8">备注:</td>
      <td align="left" bgcolor="#FFFFFF" colspan="5">&nbsp;
      <textarea style="width:95%" id="remark" name="remark" >${mainMap.REMARK}</textarea></td>
    </tr>
  </table>
  <FIELDSET >
					<LEGEND
						style="MozUserSelect: none; KhtmlUserSelect: none"
						unselectable="on">
					<th colspan="6">
						<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
						配件查询
						<input type="button" class="normal_btn" name="addPartViv"
							id="addPartViv" value="增加" onclick="addPartDiv()" />
					</th>
					</LEGEND>
					<div style="display: none; heigeht: 5px" id="partDiv">
						<table class="table_query" width=100% border="0" align="center"
							cellpadding="1" cellspacing="1">
							<tr>

								<td   align="right" width="13%">
									配件编码：
								</td>
								<td align="left" width="20%">
									&nbsp;
									<input class="middle_txt" id="PART_OLDCODE"
										datatype="1,is_noquotation,30" name="PART_OLDCODE"
										onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" />
								</td>
								<td   align="right" width="13%">
									配件名称：
								</td>
								<td align="left" width="21%">
									&nbsp;
									<input class="middle_txt" id="PART_CNAME"
										datatype="1,is_noquotation,30" name="PART_CNAME"
										onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" />
								</td>
                                <td   align="right" width="13%">
                                    件号：
                                </td>
                                <td width="20%" align="left">
                                    &nbsp;
                                    <input class="middle_txt" id="PART_CODE"
                                           datatype="1,is_noquotation,30" name="PART_CODE"
                                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" />
                                </td>
							</tr>
							<tr>

                <td   align="right" width="10%">
                    供应商：
                </td>
                <td width="30%">
                    <input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME"/>
                    <input class="mark_btn" type="button" value="&hellip;"
                           onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
                           <INPUT class=normal_btn onclick="clearInput();" value=清除 type=button name=clrBtn>
                    <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
                </td>
                <td   align="right" width="10%">
                    制造商：
                </td>
                <td width="30%">
                    <input class="middle_txt" type="text" readonly="readonly" id="MAKER_NAME" name="MAKER_NAME"/>
                    <input class="mark_btn" type="button" value="&hellip;"
                           onclick="showPartMaker('MAKER_NAME','MAKER_ID','false')"/>
                           <INPUT class=normal_btn onclick="clearMInput();" value=清除 type=button name=clrBtn>
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
										id="queryBtn" value="查 询" onclick="__extQuery__(1)" />
									<input class="normal_btn" type="button" name="BtnQuery"
										id="queryBtn2" value="添加" onclick="addCells()" />
								</td>
							</tr>
						</table>
							<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
							<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
					</div>
					</FIELDSET>
  <table id="file" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr>
      <th width="100%"  colspan="23" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />配件信息</th>
    </tr>
    <tr class="table_list_row0">
      <td ><input type="checkbox" onclick="selAll2(this)" /></td>
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
            单位
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
            配件属性
        </td>
        <td>
            供应商
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
     <c:forEach items="${detailList}" var="data" >
     	<tr class="table_list_row1">
   			  <td align="center">
   			  	<input id="plineId_${data.PART_ID}" name="plineId_${data.PART_ID}"  value="${data.PLINE_ID}"  type="hidden"  />
   			  <script type="text/javascript">
   			  	getCheck(${data.PART_ID});
   			  </script>
   			  </td>
   			   <td align="center">
   			   <span id="orderLine_SEQ" ><c:out  value="${data.LINE_NO}" /></span>
   			  </td>
		      <td align="left"><c:out value="${data.PART_OLDCODE}" /><input   name="partOldcode_${data.PART_ID}" id="partOldcode_${data.PART_ID}" value="${data.PART_OLDCODE}" type="hidden" /></td>
		      <td align="left"><div class="subStr"><c:out value="${data.PART_CNAME}" /></div> <input   name="partCname_${data.PART_ID}" id="partCname_${data.PART_ID}" value="${data.PART_CNAME}" type="hidden" /></td>
              <td align="left"><div class="subStr"><c:out value="${data.PART_CODE}" /></div><input   name="partCode_${data.PART_ID}" id="partCode_${data.PART_ID}" value="<c:out value="${data.PART_CODE}" />" type="hidden" /></td>
		      <td><c:out value="${data.UNIT}" /><input   name="unit_${data.PART_ID}" id="unit_${data.PART_ID}" value="${data.UNIT}" type="hidden" /></td>
		      <td><c:out value="${data.MIN_PACKAGE}" /><input   name="minPackage_${data.PART_ID}" id="minPackage_${data.PART_ID}" value="${data.MIN_PACKAGE}" type="hidden" /></td>
		      <td><c:out value="${data.PLAN_PRICE}" /><input   name="salePrice3_${data.PART_ID}" id="salePrice3_${data.PART_ID}" value="${data.PLAN_PRICE}" type="hidden" /></td>
		      <td><input onkeyup="countMoney(this.parentNode.parentNode.rowIndex-1,this,${data.PLAN_PRICE},${data.PART_ID})" name="plan_qty_${data.PART_ID}" id="plan_qty_${data.PART_ID}" type="text" value="${data.PLAN_QTY}" onkeydown="toNext1(${data.PART_ID})"/></td>
		      <td><c:out value="${data.PLAN_AMOUNT}" /><input   name="planAmount_${data.PART_ID}" id="planAmount_${data.PART_ID}" value="${data.PLAN_AMOUNT}" type="hidden" /></td>
		      <td><c:out value="${data.STOCK_QTY}" /><input   name="itemQty_${data.PART_ID}" id="itemQty_${data.PART_ID}" value="${data.STOCK_QTY}" type="hidden" /></td>
		      <td><c:out value="${data.BO_QTY}" /><input   name="boQty_${data.PART_ID}" id="boQty_${data.PART_ID}" value="${data.BO_QTY}" type="hidden" /></td>
		      <td><c:out value="${data.AVG_QTY}" /><input   name="avgQty_${data.PART_ID}" id="avgQty_${data.PART_ID}" value="${data.AVG_QTY}" type="hidden" /></td>
		      <td><c:out value="${data.YEAR_QTY}" /><input   name="yearQty_${data.PART_ID}" id="yearQty_${data.PART_ID}" value="${data.YEAR_QTY}" type="hidden" /></td>
		      <td><c:out value="${data.HFYEAR_QTY}" /><input   name="hfyearQty_${data.PART_ID}" id="hfyearQty_${data.PART_ID}" value="${data.HFYEAR_QTY}" type="hidden" /></td>
		      <td><c:out value="${data.QUARTER_QTY}" /><input   name="quarterQty_${data.PART_ID}" id="quarterQty_${data.PART_ID}" value="${data.QUARTER_QTY}" type="hidden" /></td>
		      <td><c:out value="${data.ZT_NUM}" /><input   name="orderQty_${data.PART_ID}" id="orderQty_${data.PART_ID}" value="${data.ZT_NUM}" type="hidden" /></td>
		      <td><c:out value="${data.SFATE_STOCK}" /><input   name="safetyStock_${data.PART_ID}" id="safetyStock_${data.PART_ID}" value="${data.SFATE_STOCK}" type="hidden" /></td>
              <td><c:out value="${data.PARTTYPENAME}" /><input   name="partType_${data.PART_ID}" id="partType_${data.PART_ID}" value="${data.PART_TYPE}" type="hidden" /></td>
              <td align="left"><c:out value="${data.VENDER_NAME}" /><input   name="venderId_${data.PART_ID}" id="venderId_${data.PART_ID}" value="${data.VENDER_ID}" type="hidden" /></td>
              <td><fmt:formatDate value="${data.FORECAST_DATE}" pattern="yyyy-MM-dd"/><input   name="preArriveDate_${data.PART_ID}" id="preArriveDate_${data.PART_ID}" value="${data.FORECAST_DATE}" type="hidden" /></td>
		      <td><input class="short_txt" name="remark_${data.PART_ID}" id="remark_${data.PART_ID}" value="${data.REMARK}" type="text" /></td>
		      <td><input  type="button" class="short_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" /></td></TR>
		   </tr>
	</c:forEach>

     </table>
    <table width="100%" align="center">
        <tr>
            <td height="2"></td>
        </tr>
        <tr>
            <td align="center">
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
        <tr>
            <td height="1"></td>
        </tr>
        <tr>
            <td valign="top">
                <br>
            </td>
        </tr>
    </table>


</div>
</form>
</BODY>
</html>

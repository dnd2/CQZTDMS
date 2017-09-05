<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配件调拨单新增</title>
<SCRIPT type=text/javascript>
jQuery.noConflict();
 var myPage;
 var url = "<%=contextPath%>/parts/partAllotManager/PartTransferManager/queryPartInfoByWhId.json";
				
 var title = null;

 var columns = [
				{header: "序号", align:'center', renderer:getIndex,width:'7%'},
				{header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', align:'center',width: '33px' ,renderer:seled},
				{header: "件号", dataIndex: 'PART_CODE', align:'center',renderer:insertHidden},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'},
				{header: "最小包装量", dataIndex: 'MIN_PACK1', align:'center'},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "可调拨数量", dataIndex: 'NORMAL_QTY', align:'center'},
				{header: "调拨单价", dataIndex: 'SALE_PRICE1', align:'center',renderer:formatPrice}
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
function insertHidden(value,meta,record){
	var partId = record.data.PART_ID;
	var output = "<input type='hidden' value='"+record.data.IS_DIRECT+"' name='IS_DIRECT'"+partId+" id='IS_DIRECT'"+partId+" />"
 	 +"<input type='hidden' value='"+record.data.IS_PLAN+"' name='IS_PLAN"+partId+"' id='IS_PLAN"+partId+"' />"
 	 +"<input type='hidden' value='"+record.data.IS_LACK+"' name='IS_LACK"+partId+"' id='IS_LACK"+partId+"' />"
 	 +"<input type='hidden' value='"+record.data.IS_REPLACED+"' name='IS_REPLACED"+partId+"' id='IS_REPLACED"+partId+"' />"+value;
	 return output;
}

function formatPrice(value,meta,record){
	return formatNum(parseFloat(value.toString()).toFixed(2));
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
function selAll2(obj){
		var cb = document.getElementsByName('cb');
		for(var i =0 ;i<cb.length;i++){
			if(obj.checked){
				cb[i].checked = true;
			}else{
				cb[i].checked = false;
			}
		}
		countAll();
}
function addCells(){
		var ck = document.getElementsByName('ck');
		var mt = document.getElementById("myTable");
		$("ckAll").checked = true;
		var cn=0;
		for(var i = 1 ;i<mt.rows.length; i ++){
			var partId = mt.rows[i].cells[1].firstChild.value;  //ID
			if(mt.rows[i].cells[1].firstChild.checked){
				cn++;
				if(validateCell(partId)){
					var partCode = mt.rows[i].cells[2].innerText;  //件号
					var partOldcode = mt.rows[i].cells[3].innerText;  //配件编码
					var partCname = mt.rows[i].cells[4].innerText;  //配件名称
					var miniPack = mt.rows[i].cells[5].innerText;  //最小包装量
					var unit = mt.rows[i].cells[6].innerText;  //单位
					var normalQty = mt.rows[i].cells[7].innerText;  //可调拨数量
					var allotPrice = mt.rows[i].cells[8].innerText;  //调拨单价
					addCell(partId, partCode, partOldcode, partCname, miniPack, unit,normalQty,allotPrice);
				}else{
					MyAlert("第"+i+"行的配件："+mt.rows[i].cells[4].innerText+" 已存在于调拨明细中!");
					break;
				}
			}
	}
		if(cn==0){
			MyAlert("请选择要添加的配件信息!");
		}
}
function validateCell(spartId){
	var partIds = document.getElementsByName("cb");
	if(partIds&&partIds.length>0){
		for(var i=0;i<partIds.length;i++){
			if(spartId==partIds[i].value){
				return false;
			}
		}
		return true;
	}
	return true;
}

function addCell(partId, partCode, partOldcode, partCname, miniPack, unit,normalQty,allotPrice){
		var tbl = document.getElementById('file');
		var rowObj = tbl.insertRow(tbl.rows.length);
		if(tbl.rows.length%2 == 0) {
			rowObj.className  = "table_list_row2";
		}else{
			rowObj.className  = "table_list_row1";
		}
		var cell1 = rowObj.insertCell(0);
		var cell2 = rowObj.insertCell(1);
		var cell3 = rowObj.insertCell(2);
		var cell4 = rowObj.insertCell(3);
		var cell5 = rowObj.insertCell(4);
		var cell6 = rowObj.insertCell(5);
		var cell7 = rowObj.insertCell(6);
		var cell8  = rowObj.insertCell(7);
		var cell9  = rowObj.insertCell(8);
		var cell10 = rowObj.insertCell(9);
		var cell11 = rowObj.insertCell(10);
		var cell12 = rowObj.insertCell(11);
		var cell13 = rowObj.insertCell(12);
		
		cell1.innerHTML = '<tr><td align="center" nowrap><input  type="checkbox" value="'+partId+'" id="cell_'+(tbl.rows.length-2)+'" name="cb" checked="true" onclick="countAll()"/></td>';                                                            
		cell2.innerHTML = '<td align="center" nowrap><input id="idx_'+partId+'" name="idx_'+partId+'" value="'+(tbl.rows.length-2)+'" type="hidden" ></td>'+(tbl.rows.length-2);                                                                                                                            
		cell3.innerHTML = '<td align="center" nowrap><input   name="partCode_'+partId+'" id="partCode_'+partId+'" value="'+partCode+'" type="hidden"/>'+partCode+'</td>';                                                 
		cell4.innerHTML = '<td align="center"><input   name="partOldcode_'+partId+'" id="partOldcode_'+partId+'" value="'+partOldcode+'" type="hidden" />'+partOldcode+'</td>';                                               
		cell5.innerHTML = '<td align="center" nowrap><input   name="partCname_'+partId+'" id="partCname_'+partId+'" value="'+partCname+'" type="hidden" class="cname_'+partId+'"/>'+partCname+'</td>';
		cell6.innerHTML = '<td align="center" nowrap><input   name="miniPack_'+partId+'" id="miniPack_'+partId+'" value="'+miniPack+'" type="hidden" />'+miniPack+'</td>';                                              
		cell7.innerHTML = '<td align="center" nowrap><input   name="unit_'+partId+'" id="unit_'+partId+'" value="'+unit+'" type="hidden" />'+unit+'</td>';
		cell8.innerHTML = '<td align="center" nowrap><input   name="normalQty_'+partId+'" id="normalQty_'+partId+'" value="'+normalQty+'" type="hidden" />'+normalQty+'</td>';
		cell9.innerHTML = '<td align="center" nowrap><input   name="allotQty_'+partId+'" id="allotQty_'+partId+'" value="" type="text" class="short_txt" onblur="checkAllotQty(this);" style="background-color:#FF9"/></td>';
		cell10.innerHTML = '<td align="center" nowrap><input   name="allotPrice_'+partId+'" id="allotPrice_'+partId+'" value="'+allotPrice+'" type="text" class="phone_txt" onblur="checkPrice(this)" maxlength="18" style="background-color:#FF9"/></td>';                                                            
		cell11.innerHTML = '<td align="center" nowrap><input   name="orderAmount_'+partId+'" id="orderAmount_'+partId+'" value="0.00" readonly  style="border:0;background:transparent;" type="text" class="phone_txt"/></td>';                                                       
		cell12.innerHTML = '<td align="center" nowrap><input class="long_txt" name="remark_'+partId+'" id="remark_'+partId+'" type="text"/></td>';                                                            
		cell13.innerHTML = '<td><input  type="button" class="cssbutton"  name="queryBtn4" value="删除" onclick="deleteTblRow('+(tbl.rows.length-1)+');" /></td></TR>';                                                               

}

	function checkAllotQty(obj){
		var curPartId = obj.name.substr(obj.name.indexOf("_")+1);//当前配件id
		var allotQty = obj.value;
		var pattern1 = /^[1-9][0-9]*$/; 
	    if (!pattern1.exec(allotQty)) {
	        MyAlert("调拨数量只能输入非零的正整数!");
	        document.getElementById("orderAmount_"+curPartId).value="0.00";
	        obj.value = "";
	        countAll();
	        return;
	   }
	   var normalQty = document.getElementById("normalQty_"+curPartId).value;
	   if(parseInt(allotQty)>parseInt(normalQty)){
		   MyAlert("调拨数量不能大于可调拨数量!");
		   document.getElementById("orderAmount_"+curPartId).value="0.00";
	       obj.value = "";
	       countAll();
		   return;
	   }
	   var fAllotPrice = document.getElementById("allotPrice_"+curPartId).value;
	   if(!fAllotPrice){
		   fAllotPrice=0;
	   }
	   var allotPrice = unFormatNum(fAllotPrice);
	   var allotAmount  = formatNum((parseFloat(allotPrice)*parseFloat(allotQty)).toFixed(2));
	   document.getElementById("orderAmount_"+curPartId).value = allotAmount;
	   countAll();
	}


	function formatNum(str) {
	    var len = str.length;
	    var step = 3;
	    var splitor = ",";
	    var decPart = ".";
	    if (str.indexOf(".") > -1) {
	        var strArr = str.split(".");
	        str = strArr[0];
	        decPart += strArr[1];
	    }
	    if (len > step) {
	        var l1 = len % step, l2 = parseInt(len / step), arr = [], first = str.substr(0, l1);
	        if (first != '') {
	            arr.push(first);
	        }

	        for (var i = 0; i < l2; i++) {
	            arr.push(str.substr(l1 + i * step, step));
	        }

	        str = arr.join(splitor);
	        str = str.substr(0, str.length - 1);
	    }

	       if (decPart != ".") {
	        str += decPart;
	        }

	    return str;
	}

	function unFormatNum(str) {
	    str = str + "";
	    if (str.indexOf(",") > -1) {
	        str = str.replace(/\,/g, "");
	    }
	    return str;
	}

    //计算总金额
	function countAll() {
	    var flag = true;
	    var amountCount = parseFloat(0);
	    var cb = document.getElementsByName("cb");
	    for (var i = 0; i < cb.length; i++) {
	        if ("" != cb[i].value && null != document.getElementById("orderAmount_" + cb[i].value) && "" != document.getElementById("orderAmount_" + cb[i].value).value) {
	            //只有选中的才参与计算
	            if (cb[i].checked) {
	                amountCount = (parseFloat(unFormatNum(amountCount)) + parseFloat(unFormatNum(document.getElementById("orderAmount_" + cb[i].value).value))).toFixed(2);
	            }
	        }
	        if (!cb[i].checked) {
	            flag = false;
	        }
	    }
	    document.getElementById("ckAll").checked = flag;
	    if(amountCount==0){
	    	document.getElementById("ORDER_AMOUNT").value = "0.00";
	    }else{
	    	document.getElementById("ORDER_AMOUNT").value = formatNum(amountCount);
		}
	}
	
	function deleteTblRow(rowNum) {
		var tbl = document.getElementById('file');
		tbl.deleteRow(rowNum);
		for (var i=rowNum;i<tbl.rows.length;i++)
	       {
	         tbl.rows[i].cells[1].innerText=i-1;
	         tbl.rows[i].cells[12].innerHTML="<input type=\"button\" class=\"cssbutton\"  name=\"deleteBtn\" value=\"删除\" onclick='deleteTblRow("+i+")'/></td></tr>";
	         if(i%2==0){
			    	tbl.rows[i].className   = "table_list_row1";
				  }else{
					  tbl.rows[i].className  = "table_list_row2";
				  }
	      }
	}
	
	function queryPartInfo(){
		var partDiv = document.getElementById("partDiv");
		var addPartViv = document.getElementById("addPartViv");
		if($("WH_ID").value==""){
			addPartViv.value="增加";
			partDiv.style.display = "none";
			return;
		}
		partDiv.style.display="";
		addPartViv.value = "收起";
		__extQuery__(1);
	}

	function addPartDiv(){
	    var partDiv = document.getElementById("partDiv");
		var addPartViv = document.getElementById("addPartViv");
        if($("WH_ID").value==""){
            MyAlert("请选择仓库!");
            return;
        }
		if(partDiv.style.display=="block" ){
			addPartViv.value="增加";
			partDiv.style.display = "none";
		}else{
			addPartViv.value="收起";
			partDiv.style.display="block" ;
		}
        __extQuery__(1);
	}
	
	//上传
	function uploadExcel(){
		if(document.getElementById("WH_ID").value==""){
			MyAlert("请先选择仓库!");
			return;
		}
		 var fileValue = document.getElementById("uploadFile").value;
         if (fileValue == "") {
             MyAlert("请选择文件!");
             return;
         }
         var fi = fileValue
                 .substring(fileValue.length - 3, fileValue.length);
         if (fi != 'xls') {
             MyAlert('导入文件格式不对,请导入xls文件格式');
             return false;
         }
        var obj = document.getElementById("WH_ID");
		var whname = document.getElementById("WH_NAME");
		var txt = obj.options[obj.selectedIndex].text;
		whname.value = txt;
		fm.action = "<%=contextPath%>/parts/partAllotManager/PartTransferManager/uploadPartTransferExcel.do";
		fm.submit();
	}

    function checkForm(){
    	var whId = document.getElementById("WH_ID").value;
		var rcvOrg = document.getElementById("RCV_ORG").value;
		var addr = document.getElementById("ADDR").value;
		var receiver = document.getElementById("RECEIVER").value;
		var tel = document.getElementById("TEL").value;
		var postCode = document.getElementById("POST_CODE").value;
		var station = document.getElementById("STATION").value;
		if(rcvOrg==null||rcvOrg==""){
			MyAlert("请选择接收单位!");
			$("RCV_ORG").value="";
			$("RCV_ORG").focus();
			return false;
		}
		if(addr==null||addr==""){
			MyAlert("请选择接收地址!");
			$("ADDR").value="";
			$("ADDR").focus();
			return false;
		}
		if(receiver==null||receiver==""){
			MyAlert("接收人不能为空!");
			$("RECEIVER").value="";
			$("RECEIVER").focus();
			return false;
		}
		if(tel==null||tel==""){
			MyAlert("接收人电话不能为空!");
			$("TEL").value="";
			$("TEL").focus();
			return false;
		}else{
			var pattern =/((^[0-9]{3,4}\-[0-9]{7,8})(-(\d{3,}))?$)|(^[0-9]{7,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^1[0-9]{10}$)/;
			if(!pattern.exec(tel)){
				MyAlert("请输入正确的接收电话!");
				$("TEL").value="";
				$("TEL").focus();
				return false;
			}
		}
		if(postCode==null||postCode==""){
			MyAlert("邮政编码不能为空!");
			$("POST_CODE").value="";
			$("POST_CODE").focus();
			return false;
		}else{
			var pattern =/^[0-9]{6}$/;
			if(!pattern.exec(postCode)){
				MyAlert("请输入正确的邮政编码!");
				$("POST_CODE").value="";
				$("POST_CODE").focus();
				return false;
			}
		}
		if(station==null||station==""){
			MyAlert("到站名称不能为空!");
			$("STATION").value="";
			$("STATION").focus();
			return false;
		}
		if(whId==""){
			MyAlert("请选择仓库！");
			return false;
		}
		var cb = document.getElementsByName("cb");
		var l = cb.length;
		var cnt = 0;
		//var amount = 0;
		var ary = new Array();
		for(var i=0;i<l;i++)
		{        
			if(cb[i].checked)
			{            
				cnt++;
				var partId = cb[i].value;
				ary.push(partId);
				var allotQty = document.getElementById("allotQty_"+partId).value;
				var allotPrice = document.getElementById("allotPrice_"+partId).value;
				if(allotQty==null||allotQty==""){
					MyAlert("第"+(i+1)+"行的调拨数量不能为空!");
					return false;
				}
				var pattern1 = /^[1-9][0-9]*$/; 
			    if (!pattern1.exec(allotQty)) {
			        MyAlert("第"+(i+1)+"行的调拨数量只能输入非零的正整数!");
			        return false;
			   }
			   var normalQty = document.getElementById("normalQty_"+partId).value;
			   if(parseInt(allotQty)>parseInt(normalQty)){
				   MyAlert("第"+(i+1)+"行的调拨数量不能大于可调拨数量!");
				   return false;
			   }

			   if(allotPrice==null||allotPrice==""){
				   MyAlert("第"+(i+1)+"行的调拨单价不能为空!");
				   return false;
			   }

			   //amount+=allotQty*allotPrice;
			}
		}
	      if(cnt==0)
	      {
	    	  MyAlert("请选择调拨明细！");
	          return false;
	      }
	      var s = ary.join(",")+",";
	      var pflag = false;
	      var nclass="";
	      var sid="";
	      for(var i=0;i<ary.length;i++){
	    	  jQuery(".cname_"+ary[i]).parent("td").css({background:""});
	      }
	      for(var i=0;i<ary.length;i++){
	    	  if(s.replace(ary[i]+",","").indexOf(ary[i]+",")>-1) {
	    		  pflag = true;
		    	  sid = "partCname_"+ary[i];
		    	  nclass = "cname_"+ary[i];
		    	  var partCname = document.getElementById(sid).value;
	    		  MyAlert("调拨明细中配件名称为：" +partCname+" 的配件重复!" );
	    		  break;
	    	  }
	      }
	      if(pflag){
	    	  jQuery("."+nclass).parent("td").css({background:"red"});
	    	  return false;
	      }
	      //document.getElementById("ORDER_AMOUNT").value=amount;
	      return true;
    }
	
	function saveTransfer(){
		if(checkForm()){
			var obj = document.getElementById("WH_ID");
			var whname = document.getElementById("WH_NAME");
			var txt = obj.options[obj.selectedIndex].text;
			whname.value = txt;
			if(confirm("确定要保存?")){
				btnDisable();
				var cb = document.getElementsByName("cb");
				var l = cb.length;
				for(var i=0;i<l;i++)
				{        
					if(cb[i].checked)
					{  
						var partId = cb[i].value;
						var orderPrice = document.getElementById("orderAmount_"+partId).value;
						var oPrice = unFormatNum(orderPrice);
						document.getElementById("orderAmount_"+partId).value = oPrice;
				    }
				}
				var orderAmount = document.getElementById("ORDER_AMOUNT").value;
				var oAmount = unFormatNum(orderAmount);
				document.getElementById("ORDER_AMOUNT").value = oAmount;
				var url = "<%=contextPath%>/parts/partAllotManager/PartTransferManager/savePartTransfer.json?flag=0";	
				sendAjax(url,getResult,'fm');
			}
		}
	}

    function submitTransfer(){
    	if(checkForm()){
    		var obj = document.getElementById("WH_ID");
			var whname = document.getElementById("WH_NAME");
			var txt = obj.options[obj.selectedIndex].text;
			whname.value = txt;
			if(confirm("确定要提报?")){
				btnDisable();
				var url = "<%=contextPath%>/parts/partAllotManager/PartTransferManager/savePartTransfer.json?flag=1";	
				sendAjax(url,getResult,'fm');
			}
		}
    }
    
	function getResult(jsonObj){
		btnEable();
		if(jsonObj!=null){
		    var success = jsonObj.success;
		    var curQty = jsonObj.curQty;
		    var partId = jsonObj.partId;
		    var error = jsonObj.error;
		    var exceptions = jsonObj.Exception;
		    if(success){
		    	MyAlert(success);
		    	window.location.href = "<%=contextPath%>/parts/partAllotManager/PartTransferManager/queryPartTransferInit.do";
		    }else if(error){
		    	document.getElementById("normalQty_"+partId).value=curQty;
		    	MyAlert(error);
		    }else if(exceptions){
		    	MyAlert(exceptions.message);
			}
		}
	}
	//修改提示框超出6行会超出框的BUG
	function MyAlert(info){
		 var owner = getTopWinRef();
		 try{
		  _dialogInit();  
		  owner.getElementById('dialog_content_div').innerHTML='\
		    <div style="font-size:12px;">\
		     <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
		      <b>信息</b>\
		     </div>\
		     <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
		     <div style="padding:2px;text-align:center;background:#D0BFA1;">\
		      <input id="dialog_alert_button" type="button" value="确定" style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
		     </div>\
		    </div>';
		  owner.getElementById('dialog_alert_info').innerHTML=info;
		  owner.getElementById('dialog_alert_button').onclick=_hide;
		  var height=200 ;
		  
		  if(info.split('</br>').length>=6){
		 	height = height + (info.split('</br>').length-6)*27;
		  }
		   _setSize(300,height);
		    
		  _show();
		 }catch(e){
		  MyAlert('MyAlert : '+ e.name+'='+e.message);
		 }finally{
		  owner=null;
		 }
	}

	//下载上传模板
	function expPartTransferTmp(){
		fm.action = "<%=contextPath%>/parts/partAllotManager/PartTransferManager/expPartTransferTmp.do";
        fm.submit();
	}

	function goBack(){
		window.location.href = "<%=contextPath%>/parts/partAllotManager/PartTransferManager/queryPartTransferInit.do";
	}
	
	function validateNum(obj){
		if(isNaN(obj.value)){
			MyAlert("请输入数字!");
			obj.value = "";
			return;
		}
	}
	
	//type:接收单位(2)地址(3)
	function getSelSale(inputId,inputCode,inputName,inputLinkMan,inputTel,inputPostCode,inputStation,dealerId,type){
		    var rcvOrgId = $("RCV_ORGID").value;
		    if(type=='3'){//如果选择接收地址，就要先判断是否已经选择了接收单位
			    if(rcvOrgId==null||rcvOrgId==""){
				    MyAlert("请先选择接收单位!");
				    return;
			    }
		    }
		    if(!inputId){ inputId = null;}
			if(!inputCode){ inputCode = null;}
			if(!inputName){ inputName = null;}
			if(!inputLinkMan){ inputLinkMan = null;}
			if(!inputTel){ inputTel = null;}
			if(!inputPostCode){ inputPostCode = null;}
			if(!inputStation){ inputStation = null;}
			if(!dealerId){ dealerId = null;}
			if(!type){ type = null;}
			if(!rcvOrgId){ rcvOrgId = null;}
			OpenHtmlWindow(g_webAppName+'/jsp/parts/partAllotManager/partTransferManager/selSales.jsp?dealerId='+dealerId+'&type='+type+'&inputName='+inputName+'&inputId='+inputId+'&inputCode='+inputCode+'&inputLinkMan='+inputLinkMan+'&inputTel='+inputTel+'&inputPostCode='+inputPostCode+'&inputStation='+inputStation+'&rcvOrgId='+rcvOrgId,730,390);
     }

	 function showUpload(){
	        if($("uploadTable").style.display == "none") {
	            $("uploadTable").style.display = "block";
	        }else {
	            $("uploadTable").style.display = "none";
	        }
     }

	 function checkPrice(obj){
		 var curPartId = obj.name.substr(obj.name.indexOf("_")+1);//当前配件id
         if(obj.value==""){
             MyAlert("调拨单价不能为空!");
             document.getElementById("orderAmount_"+curPartId).value="0.00";
  	         countAll();
             return;
         }
     	var patrn = /^((0\.[0-9]*[1-9][0-9]*)|([1-9][0-9]*\.[0-9]+)|([1-9][0-9]*))$/;
         if (!patrn.exec(obj.value)){
             MyAlert("调拨单价无效,请重新输入!");
             obj.value="";
             document.getElementById("orderAmount_"+curPartId).value="0.00";
  	        countAll();
             return;
         }else{
             if(obj.value.indexOf(".") >= 0){
             	var patrn = /^[0-9]{0,10}.[0-9]{0,7}$/;
             	if(!patrn.exec(obj.value)){
             		MyAlert("调拨单价整数部分不能超过10位,且保留精度最大为7位!");
             		obj.value="";
             		document.getElementById("orderAmount_"+curPartId).value="0.00";
         	        countAll();
                     return;
                 }
             }else{
             	var patrn = /^[0-9]{0,10}$/;
             	if(!patrn.exec(obj.value)){
             		MyAlert("调拨单价整数部分不能超过10位!");
             		obj.value="";
             		document.getElementById("orderAmount_"+curPartId).value="0.00";
         	        countAll();
                    return;
                 }
             }
         }
         
 		var allotQty = document.getElementById("allotQty_"+curPartId).value;
 		var pattern1 = /^[1-9][0-9]*$/; 
 	    if (!pattern1.exec(allotQty)) {
 	        document.getElementById("orderAmount_"+curPartId).value="0.00";
 	        countAll();
 	        return;
 	   }
 	   var fAllotPrice = obj.value;
 	   if(!fAllotPrice){
 		   fAllotPrice=0;
 	   }
 	   var allotPrice = unFormatNum(fAllotPrice);
 	   var allotAmount  = formatNum((parseFloat(allotPrice)*parseFloat(allotQty)).toFixed(2));
 	   document.getElementById("orderAmount_"+curPartId).value = allotAmount;
 	   countAll();
     }

	 
</script>
</head>

<body onload="loadcalendar();">
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理&gt;配件调拨管理&gt;配件调拨单&gt;新增</div>
  <table class="table_query">
    <tr>
    <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />调拨信息</th>
  </tr>
  <tr>
      <td width="109"   align="right" >调拨单号：</td>
      <td width="378" >
        ${orderCode }
        <input type="hidden" name="orderCode" id="orderCode" value="${orderCode }"/>
      </td>
      <td width="109"   align="right" >调出单位：</td>
      <td width="260" >
        ${sellerName }
        <input type="hidden" name="sellerCode" id="sellerCode" value="${sellerCode }"/>    
        <input type="hidden" name="sellerId" id="sellerId" value="${sellerId }"/>    
        <input type="hidden" name="sellerName" id="sellerName" value="${sellerName }"/>    
      </td>
      <td width="98"   align="right" >制单人：</td>
      <td width="307" >
      ${createName }
      </td>
    </tr>
    <tr>
      <td   align="right">制单日期：</td>
      <td>
      ${now }
      <input type="hidden" name="now" id="now" value="${now }"/>   
      </td>
      <td   align="right"  >接收单位：</td>
      <td >
      	<input name="RCV_ORG" class="SearchInput" id="RCV_ORG" type="text" size="20" readonly="true" />
	    <input name="RCV_CODE" class="SearchInput" id="RCV_CODE" type="hidden" size="20" readonly="true" />
	    <input name="RCV_ORGID" class="SearchInput" id="RCV_ORGID" type="hidden" size="20" readonly="true" />
  		<input name='dlbtn2' id='dlbtn2' class='mini_btn'  type='button' value='...'  onclick="getSelSale('RCV_ORGID','RCV_CODE','RCV_ORG','','','','',${sellerId },'2');"/>
        <font color="RED">*</font>
       </td>
       <td   align="right" >接收地址：</td>
      <td >
      	<input name="ADDR" class="SearchInput" id="ADDR" type="text" size="20" readonly="readonly" value="${addr }"/>
	    <input name="ADDR_ID" id="ADDR_ID" type="hidden" value="${addrId }"/>
	    <input name='dlbtn2' id='dlbtn2' class='mini_btn'  type='button' value='...'  onclick="getSelSale('ADDR_ID','','ADDR','RECEIVER','TEL','POST_CODE','STATION',${sellerId },'3');"/>
        <font color="RED">*</font>
        </td>
    </tr>
    <tr>
    <td   align="right"  >接收人：</td>
      <td >
      <input id="RECEIVER" name="RECEIVER"  type="text" class="normal_txt" value="${receiver }"/><font color="RED">*</font>
      </td>
        <td   align="right">接收人电话：</td>
        <td>
         <input id="TEL" name="TEL" type="text" class="normal_txt" value="${tel }"/><font color="RED">*</font>
       </td>
       <td   align="right" >邮政编码：</td>
       <td >
       <input id="POST_CODE" name="POST_CODE" type="text" class="normal_txt" value="${postCode }" maxlength="6"/><font color="RED">*</font>
       </td>
    </tr>
    <tr>
    <td   align="right"  >发运方式：</td>
      <td >
      <c:if test="${uFlag eq 1}">
       <select id="TRANS_TYPE" name="TRANS_TYPE">
      <c:forEach items="${transList}" var="trans">
         <option value="${trans.fixValue }" ${transType==trans.fixValue?"selected":""}>${trans.fixName}</option>
      </c:forEach>
      </select>
      </c:if>
      <c:if test="${uFlag ne 1}">
      <select id="TRANS_TYPE" name="TRANS_TYPE">
      <c:forEach items="${transList}" var="trans">
         <option value="${trans.fixValue }">${trans.fixName}</option>
      </c:forEach>
      </select>
      </c:if>
	  <font color="RED">*</font>
	 </td>
	 <td width="98"   align="right" >付款方式：</td>
      <td width="307" >
      <c:if test="${uFlag eq 1}">
      <script type="text/javascript">
			genSelBoxExp("PAY_TYPE",<%=Constant.CAR_FACTORY_SALES_PAY_TYPE%>,${payType},false,"short_sel","","false",'');
	  </script> 
      </c:if>
      <c:if test="${uFlag ne 1}">
      <script type="text/javascript">
			genSelBoxExp("PAY_TYPE",<%=Constant.CAR_FACTORY_SALES_PAY_TYPE%>,"",false,"short_sel","","false",'');
	  </script> 
      </c:if>
      <font color="RED">*</font>
      </td>
      <td   align="right">总金额：</td>
      <td>
      <input readonly class="phone_txt" type="text"  style="border:0;background:transparent;" name="ORDER_AMOUNT" id="ORDER_AMOUNT" value="0.00"/>元
      </td>
    </tr>
    <tr>
    <td   align="right">仓库：</td>
     <td >
     <c:if test="${uFlag eq 1}">
     <select id="WH_ID" name="WH_ID">
      <option value="${whId }">${whName }</option>
     </select>
      <input type="hidden" id="WH_NAME" name="WH_NAME"/>
     </c:if>
     <c:if test="${uFlag ne 1}">
     <select id="WH_ID" name="WH_ID" onchange="queryPartInfo();">
     <%-- <option value="">-请选择-</option>--%>
      <c:forEach items="${wareHouses}" var="wareHouse">
           <option value="${wareHouse.whId }">${wareHouse.whName }</option>
      </c:forEach>
      </select>
      <input type="hidden" id="WH_NAME" name="WH_NAME"/>
     </c:if>
	  <font color="RED">*</font>
      </td>
      <td   align="right" >到站名称：</td>
       <td >
       <input id="STATION" name="STATION" type="text" class="normal_txt" value="${station }"/><font color="RED">*</font>
       </td>
    </tr>
    <tr>
      <td   align="right" >备注：</td>
      <td colspan="6"><textarea name="remark1" cols="80" rows="4">${remark1 }</textarea></td>
    </tr>
</table>
<FIELDSET >
					<LEGEND 
						style="MozUserSelect: none; KhtmlUserSelect: none"
						unselectable="on">
					<th colspan="6" style="background-color:#DAE0EE;font-weight:normal;color:#416C9B;padding:2px;line-height:1.5em;border: 1px solid #E7E7E7;">
						<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
						<font color="blue">配件信息</font>
						<input type="button" class="normal_btn" name="addPartViv"
							id="addPartViv" value="增加" onclick="addPartDiv()" />
					</th>
					</LEGEND>
					<div style="display: none;heigeht: 5px" id="partDiv">
						<table class="table_query" width=100% border="0" align="center"
							cellpadding="1" cellspacing="1">
							<tr>
								<td   align="right" width="13%">
									件号：
								</td>
								<td width="20%" align="left">
									&nbsp;
									<input class="middle_txt" id="PART_CODE"
										datatype="1,is_noquotation,30" name="PART_CODE"
										onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" />
								</td>
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
							</tr>
							<tr>
								<td align="center" colspan="6">
									<input class="normal_btn" type="button" name="BtnQuery"
										id="queryBtn" value="查 询" onclick="__extQuery__(1)" />
									<input class="normal_btn" type="button" name="BtnQuery"
										id="queryBtn" value="添加" onclick="addCells()" />
								</td>
							</tr>
						</table>
							<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
							<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
					</div>
					</FIELDSET>
					<table id="file" class="table_list" style="border-bottom: 1px;">
						<tr>
							<th colspan="18" align="left">
								<img src="<%=contextPath%>/img/nav.gif" />调拨明细
						</tr>
						<tr class="table_list_row0">
							<td>
								<input type="checkbox" onclick="selAll2(this)"  id="ckAll"/>
							</td>
							<td>
								序号
							</td>
							<td>
								件号
							</td>
							<td>
								配件编码
							</td>
							<td>
								配件名称
							</td>
							<td>
								最小包装量 
							</td>
							<td>
								单位
							</td>
							<td>
								可调拨数量
							</td>
							<td>
								调拨数量<font color="red">*</font>
							</td>
							<td>
								调拨单价(元)
							</td>
							<td>
								调拨金额(元)
							</td>
							<td>
								备注
							</td>
							<td>
								操作
							</td>
						</tr>
						<c:if test="${list !=null}">
						<SCRIPT type=text/javascript>
						var partDiv1 = document.getElementById("partDiv");
						partDiv1.style.display="";
						__extQuery__(1);
						</SCRIPT>
						  <c:forEach items="${list}" var="list" varStatus="_sequenceNum" step="1">
						    <c:if test="${((_sequenceNum.index+1) mod 2) != 0}">
							<tr class="table_list_row1">
							</c:if>
							<c:if test="${((_sequenceNum.index+1) mod 2) == 0}">
							<tr class="table_list_row2">
							</c:if>
							  <td align="center" nowrap>
							    <input  type="checkbox" value="${list.partId}" id="cell_${_sequenceNum.index+1}" name="cb" checked="checked" onclick="countAll()"/>
							  </td>
							  <td align="center" nowrap>${_sequenceNum.index+1}
							    <input id="idx_${list.partId}" name="idx_${list.partId}" value="${_sequenceNume.index+1}" type="hidden" >
							  </td>
							  <td align="center" nowrap>
							    <input   name="partCode_${list.partId}" id="partCode_${list.partId}" value="${list.partCode}" type="hidden" />${list.partCode}
							    <input   name="IS_DIRECT${list.partId}" id="IS_DIRECT${list.partId}" value="${list.isDirect}" type="hidden" />
							    <input   name="IS_PLAN${list.partId}" id="IS_PLAN${list.partId}" value="${list.isPlan}" type="hidden" />
							    <input   name="IS_LACK${list.partId}" id="IS_LACK${list.partId}" value="${list.isLack}" type="hidden" />
							    <input   name="IS_REPLACED${list.partId}" id="IS_REPLACED${list.partId}" value="${list.isReplaced}" type="hidden" />
							  </td>
							  <td align="center">
							    <input   name="partOldcode_${list.partId}" id="partOldcode_${list.partId}" value="${list.partOldcode}" type="hidden" />${list.partOldcode}
							  </td>
							  <td align="center" nowrap>
							    <input   name="partCname_${list.partId}" id="partCname_${list.partId}" value="${list.partCname}" type="hidden" class="cname_${list.partId}"/>${list.partCname}
							  </td>
							  <td align="center" nowrap>
							    <input   name="miniPack_${list.partId}" id="miniPack_${list.partId}" value="${list.miniPack1}" type="hidden" />${list.miniPack1}
							  </td>
							  <td align="center" nowrap>
							    <input   name="unit_${list.partId}" id="unit_${list.partId}" value="${list.unit}" type="hidden" />${list.unit}
							  </td>
							  <td align="center" nowrap>
							    <input class="middle_txt" name="normalQty_${list.partId}" id="normalQty_${list.partId}" value="${list.normalQty}" type="hidden" />${list.normalQty}
							  </td>
							  <td align="center" nowrap>
							    <input class="short_txt"  name="allotQty_${list.partId}" id="allotQty_${list.partId}" value="${list.allotQty}" type="text" onblur="checkAllotQty(this);"/>
							  </td>
							  <td align="center" nowrap>
							    <input type="text" class="phone_txt" maxlength="18" style="background-color:#FF9"  name="allotPrice_${list.partId}" id="allotPrice_${list.partId}" value="${list.salePrice1}"  onblur="checkPrice(this)"/>
							  </td>
							  <td>
							    <input class="phone_txt" name="orderAmount_${list.partId}" id="orderAmount_${list.partId}" value="${list.orderAmount}" readonly  style="border:0;background:transparent;" type="text" />
							  </td>
							  <td align="center" nowrap>
							    <input class="long_txt" name="remark_${list.partId}" id="remark_${list.partId}" value="" type="text" />
							  </td>
							  <td>
							    <input  type="button" class="cssbutton"  name="queryBtn4" value="删除" onclick="deleteTblRow(${_sequenceNum.index+2});" />
							  </td>
							</tr>
						  </c:forEach>
						</c:if>
					</table>
  
<table border="0" class="table_query">
  <tr align="center">
  <td><input class="normal_btn" type="button" value="保存" name="button1"	id="saveButton" onclick="saveTransfer();">
  &nbsp;
  <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
  &nbsp;
  <input class="normal_btn" type="button" id="submitButton" value="提 报" onclick="submitTransfer();"/>
  &nbsp;
  <input name="BtnUpload" id="BtnUpload" class="normal_btn" type="button" value="批量导入" onclick="showUpload();"/>
  </td>
  </tr>
  </table>
  <table class="table_edit" id = "uploadTable" style="display: none">
            <tr>
                <td><font color="red">
                    <input type="button" class="normal_btn" value="模版下载" onclick="expPartTransferTmp()"/>
                    文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
                    <input type="file" name="uploadFile" style="width: 250px" id="uploadFile" value=""/>
                    &nbsp;
                    <input type="button" id="upbtn" class="normal_btn" value="确定" onclick="uploadExcel()"/></td>
            </tr>
        </table>
  
</div>
</form>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	request.setAttribute("wareHouseList", request.getAttribute("wareHouseList"));
%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<TITLE>现场BO单处理新增</TITLE>
<style type="text/css">
.table_list_row0 td {
	background-color:#FFFFCC;
	border: 1px solid #DAE0EE;
	white-space:    nowrap;
}
</style>

<SCRIPT type=text/javascript>
jQuery.noConflict();
 var myPage;

 var url = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockLocalBOAction/showPartStockBase.json";
				
 var title = null;

 var columns = [
				{header: "序号", align:'center', renderer:getIndex,width:'7%'},
				{header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', align:'center',width: '33px' ,renderer:seled},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center',style:'text-align: left;'},
				{header: "配件名称", dataIndex: 'PART_CNAME', align:'center',style:'text-align: left;'},
				{header: "件号", dataIndex: 'PART_CODE', align:'center',style:'text-align: left;'},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "货位", dataIndex: 'LOC_NAME', align:'center',style:'text-align: left;'},
				{header: "可用库存", dataIndex: 'NORMAL_QTY', align:'center'},
				{header: "占用库存", dataIndex: 'BOOKED_QTY', align:'center'},
				{header: "正常封存", dataIndex: 'ZCFC_QTY', align:'center'},
				{header: "盘亏封存", dataIndex: 'PKFC_QTY'},
				{header: "账面库存", dataIndex: 'ITEM_QTY', align:'center'}
			  ];
function seled(value,meta,record) 
 {
 	 return "<input type='checkbox' value='"+value+"' name='ck' id='ck' />";
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
}
function addCells(){
		
		var ck = document.getElementsByName('ck');
		var mt = document.getElementById("myTable");
		var cn=0;
		for(var i = 1 ;i<mt.rows.length ; i ++){
			var partId = mt.rows[i].cells[1].firstChild.value;  //ID
			if(mt.rows[i].cells[1].firstChild.checked){
				cn++;
				if(validateCell(partId)){
					var partCode = mt.rows[i].cells[4].innerText;  //件号
					var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
					var partCname = mt.rows[i].cells[3].innerText;  //配件名称
					var itemQty = mt.rows[i].cells[10].innerText;  //库存
					var normalQty = mt.rows[i].cells[7].innerText;  //当前可用库存
					var bookedQty = mt.rows[i].cells[8].innerText;  //已占用
					var fcQty = mt.rows[i].cells[9].innerText;  //已封存
					var unit = mt.rows[i].cells[5].innerText;  //单位
					addCell(partId, partCode, partOldcode, partCname, itemQty, normalQty, bookedQty, fcQty, unit);
				}else{
					MyAlert("第"+i+"行配件："+mt.rows[i].cells[4].innerText+" 已存在!</br>");
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

function genSelBoxExpStr1(id,type,selectedKey,setAll,_class_,_script_,nullFlag,expStr){
	var str = "";
	var arr;
	if(expStr.indexOf(",")>0)
		arr = expStr.split(",");
	else {
		expStr = expStr+",";
		arr = expStr.split(",");
	}
	str += "<select id='" + id + "' name='" + id +"' class='"+ _class_ +"' " + _script_ ;
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

function genSelBoxExpStr(id,type,selectedKey,setAll,_class_,_script_,nullFlag,expStr){
	var str = "";
	var arr;
	if(expStr.indexOf(",")>0)
		arr = expStr.split(",");
	else {
		expStr = expStr+",";
		arr = expStr.split(",");
	}
	str += "<select id='" + id + "' name='" + id +"' class='"+ _class_ +"' " + _script_ ;
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
	return str;
}

function addCell(partId,partCode, partOldcode, partCname,itemQty,normalQty,bookedQty,fcQty,unit){
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

		var stockValue = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_07%>;
		var stockName = "现场BO";
//		var bzType = genSelBoxExpStr("bzType_"+ partId,<%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE%>,"",true,"short_sel","","false","").toString();
		//var cgType = genSelBoxExpStr("cgType_"+ partId,<%=Constant.PART_STOCK_STATUS_CHANGE_TYPE%>,<%=Constant.PART_STOCK_STATUS_CHANGE_TYPE_01%>,true,"short_sel","","false","").toString();
		var cgType = "<input type='hidden' name='cgType_"+ partId +"' id='cgType_"+ partId +"' value='"+ <%=Constant.PART_STOCK_STATUS_CHANGE_TYPE_01%> +"' />封存";
		var remark = document.getElementById("remark").value;
		
		cell1.innerHTML = '<tr><td align="center" nowrap><input type="checkbox" value="'+partId+'" id="cell_'+(tbl.rows.length-2)+'" name="cb" checked="true" /></td>';                                                            
		cell2.innerHTML = '<td align="center" nowrap>'+(tbl.rows.length-2)+'<input id="idx_'+partId+'" name="idx_'+partId+'" value="'+(tbl.rows.length-2)+'" type="hidden" ></td>';                                                                                                                            
		cell5.innerHTML = '<td align="center" nowrap><input name="partCode_'+partId+'" id="partCode_'+partId+'" value="'+partCode+'" type="hidden" />'+partCode+'</td>';                                                 
		cell3.innerHTML = '<td align="center" nowrap><input name="partOldcode_'+partId+'" id="partOldcode_'+partId+'" value="'+partOldcode+'" type="hidden" />'+partOldcode+'</td>';                                               
		cell4.innerHTML = '<td align="center" nowrap><input name="partCname_'+partId+'" id="partCname_'+partId+'" value="'+partCname+'" type="hidden" class="cname_'+partId+'"/>'+partCname+'</td>';
		cell6.innerHTML = '<td align="center" nowrap><input name="unit_'+partId+'" id="unit_'+partId+'" value="'+unit+'" type="hidden" />'+unit+'</td>';
		cell7.innerHTML = '<td align="center" nowrap><input name="normalQty_'+partId+'" id="normalQty_'+partId+'" value="'+normalQty+'" type="hidden" />'+normalQty+'</td>';                                              
		cell8.innerHTML = '<td align="center" nowrap><input name="fcQty_'+partId+'" id="fcQty_'+partId+'" value="'+fcQty+'" type="hidden" />'+fcQty+'</td>';
		cell9.innerHTML = '<td align="center" nowrap><input name="bzType_'+partId+'" id="bzType_'+partId+'" value="'+stockValue+'" type="hidden" />'+ stockName +'</td>';                                                            
		cell10.innerHTML = '<td align="center" nowrap>'+ cgType +'</td>';                                                            
		cell11.innerHTML = '<td align="center" nowrap><input class="short_txt" onchange="dataTypeCheck('+(tbl.rows.length-2)+','+partId+',this)" name="returnQty_'+partId+'" id="returnQty_'+partId+'" type="text"/></td>';
		cell12.innerHTML = '<td align="center" nowrap><input class="short_txt" name="remark_'+partId+'" id="remark_'+partId+'" type="text" value="'+remark+'"/></td>';                                                            
		cell13.innerHTML = '<td><input type="button" class="short_btn"  name="deleteBtn" value="删 除" onclick="deleteTblRow('+(tbl.rows.length-1)+');" /></td></TR>';                                                               

}
	function dataTypeCheck(loc,partId,obj){
		if(""==obj.value){
			return;
		}
		var tbl = document.getElementById('file');
		var value = obj.value;
		if(isNaN(value)){
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
		var normalQty = parseInt(document.getElementById("normalQty_"+partId).value);
		var fcQty = parseInt(document.getElementById("fcQty_"+partId).value);
		var cgType = document.getElementById("cgType_"+partId).value;
		var cgType1 = <%=Constant.PART_STOCK_STATUS_CHANGE_TYPE_01%>;
		var cgType2 = <%=Constant.PART_STOCK_STATUS_CHANGE_TYPE_02%>;

		if(cgType1 == cgType && normalQty < parseInt(obj.value))
		{
			MyAlert("封存数量不能大于可用库存数!");
			obj.value = "";
			return;
		}
		else if(cgType2 == cgType && fcQty < parseInt(obj.value))
		{
			MyAlert("解封数量不能大于正常封存数!");
			obj.value = "";
			return;
		}
	} 

	function deleteTblRow(rowNum) {
		var tbl = document.getElementById('file');
		tbl.deleteRow(rowNum);
		var count = tbl.rows.length;
		for (var i=rowNum;i<=count;i++)
	       {
	         tbl.rows[i].cells[1].innerText=i-1;
	         tbl.rows[i].cells[12].innerHTML="<td><input type=\"button\" class=\"short_btn\"  name=\"deleteBtn\" value=\"删 除\" onclick='deleteTblRow("+i+")'/></td></tr>";
	         if(i%2==0){
		    	tbl.rows[i].className   = "table_list_row1";
			  }else{
				  tbl.rows[i].className  = "table_list_row2";
			  }
	      }
	}
	
	function addPartDiv(){
		var partDiv = document.getElementById("partDiv");
		var addPartViv = document.getElementById("addPartViv");
		var whValue = document.getElementById("whId").value;
		if("增 加" == addPartViv.value)
		{
			if("" == whValue)
			{
				MyAlert("请先选择仓库!");
				return false;
			}
		}
		
		if(partDiv.style.display=="block" ){
			addPartViv.value="增 加";
			partDiv.style.display = "none";
		}else{
			addPartViv.value="收 起";
			partDiv.style.display="block" ;
			__extQuery__(1);
		}
	}

	//仓库变化
	function WHChanged(){
		var partDiv = document.getElementById("partDiv");
		var addPartViv = document.getElementById("addPartViv");
		var whValue = document.getElementById("whId").value;
		
		if("" == whValue && "收 起" == addPartViv.value)
		{
			addPartViv.value="增 加";
			partDiv.style.display = "none";
		}
		if("" != whValue && "收 起" == addPartViv.value)
		{
			__extQuery__(1);
		}
	}

	//上传检查和确认信息
	function confirmUpload()
	{
		if(fileVilidate()){
			MyConfirm("确定上传选中的文件?",uploadExcel,[]);
    	}
	}
	
	//上传
	function uploadExcel(){
		btnDisable();
		fm.action = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockLocalBOAction/partStockLocalBOUpload.do";
		fm.submit();
	}

	function fileVilidate(){
		var msg = "";
		if(document.getElementById("whId").value==""){
			msg += "请先选择仓库!</br>";
		}
		if(msg != "")
		{
			MyAlert(msg);
			return false;
		}
		var importFileName = $("uploadFile").value;
		if(importFileName==""){
		    MyAlert("请选择上传文件!");
			return false;
		}
		var index = importFileName.lastIndexOf(".");
		var suffix = importFileName.substr(index+1,importFileName.length).toLowerCase();
		if(suffix != "xls" && suffix != "xlsx"){
		MyAlert("请选择Excel格式文件!");
			return false;
		}
		return true;
	}
	
	function savePlan(){
		btnDisable();
		var url = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockLocalBOAction/saveStockInfos.json";	
		sendAjax(url,getResult,'fm');
	}

	function getResult(json){
		btnEnable();
		if(null != json){
	        if (json.errorExist != null && json.errorExist.length > 0) {
	        	 MyAlert(json.errorExist);
	        } else if (json.success != null && json.success == "true") {
	        	btnDisable();
	        	MyAlert("保存成功!");
	            window.location.href = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockLocalBOAction/partStockLocalBOInit.do";
	        } else {
	            MyAlert("保存失败，请联系管理员!");
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
	function exportExcelTemplate(){
		fm.action = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockLocalBOAction/exportExcelTemplate.do";
		fm.submit();
	}

	//保存
	function confirmSubmit(){
		var cb = document.getElementsByName("cb");
		var msg = "";
		if(document.getElementById("whId").value==""){
			msg += "请先选择仓库!</br>";
		}

		var maxLineNum = <%=Constant.PART_STOCK_STATUS_CHANGE_MAX_LINENUM%>;
		if(maxLineNum < cb.length)
		{
			msg += "添加的数据不能超过 " + maxLineNum +" 行!</br>";
		}

		var ary = new Array();
		var l = cb.length;
		for(var i=0;i<l;i++)
		{        
			if(cb[i].checked)
			{            
				var partId = cb[i].value;
				ary.push(partId);
			}
		}
		
		//提交时,将其属性设置成DISABLED,从而达到过滤选择的目的
		var flag = false;
		for(var i =0 ;i<cb.length;i++){
			if(cb[i].checked){
				cb[i].disabled = false;
				//需要校验业务类型是否选择
				if(document.getElementById("bzType_"+cb[i].value).value==""){
					msg += "请选择第"+document.getElementById("idx_"+cb[i].value).value+"行的业务类型!</br>";
					flag = true;
				}
				//需要校验调整类型是否选择
				if(document.getElementById("cgType_"+cb[i].value).value==""){
					msg += "请选择第"+document.getElementById("idx_"+cb[i].value).value+"行的调整类型!</br>";
					flag = true;
				}
				//需要校验调整数量是否为空
				if(document.getElementById("returnQty_"+cb[i].value).value==""){
					
					msg += "请填写第"+document.getElementById("idx_"+cb[i].value).value+"行的调整数量!</br>";
					flag = true;
				} else {
					var returnQty = parseInt(document.getElementById("returnQty_"+cb[i].value).value);
					var normalQty = parseInt(document.getElementById("normalQty_"+cb[i].value).value);
					var fcQty = parseInt(document.getElementById("fcQty_"+cb[i].value).value);
					var cgType = document.getElementById("cgType_"+cb[i].value).value;
					var cgType1 = <%=Constant.PART_STOCK_STATUS_CHANGE_TYPE_01%>;
					var cgType2 = <%=Constant.PART_STOCK_STATUS_CHANGE_TYPE_02%>;

					if(cgType1 == cgType && normalQty < returnQty)
					{
						msg += "第"+document.getElementById("idx_"+cb[i].value).value+"行的封存数量不能大于可用库存数!</br>";
						flag = true;
					}
					else if(cgType2 == cgType && fcQty < returnQty)
					{
						msg += "第"+document.getElementById("idx_"+cb[i].value).value+"行的解封数量不能大于正常封存数!</br>";
						flag = true;
					}
				}
			}else{
				cb[i].disabled = true;
			}
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
	    		MyAlert("配件：" +partCname+" 被重复上传!" );
	    		break;
	    	}
	    }
	    if(pflag){
	    	jQuery("."+nclass).parent("td").css({background:"red"});
	    	return false;
	    }
	    
		if(flag){
			for(var i =0 ;i<cb.length;i++){
				cb[i].disabled = false;
			}
		}
		var cb = document.getElementsByName("cb");
		if(cb.length<=0){
			msg +="请添加配件库存信息!</br>";
		}
		var flag = false;
		for(var i=0;i<cb.length;i++){
			if(cb[i].checked){
				flag = true;
				break;
			}
		}
		if(!flag){
			msg +="请选择配件库存信息!</br>";
		}
		if(msg!=""){
			MyAlert(msg);
			for(var i =0 ;i<cb.length;i++){
				cb[i].disabled = false;
			}
			return;
		}
		MyConfirm("确定保存新增信息?",savePlan,[]);
		for(var i =0 ;i<cb.length;i++){
			cb[i].disabled = false;
		}
	}


	function goBack(){
		btnDisable();
		fm.action = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockLocalBOAction/partStockLocalBOInit.do";
		fm.submit();
	}
	function validateNum(obj){
		if(isNaN(obj.value)){
			MyAlert("请输入数字!");
			obj.value = "";
			return;
		}
	}
	function showUpload(){
		var uploadDiv = document.getElementById("uploadDiv");
		if(uploadDiv.style.display=="block" ){
			uploadDiv.style.display = "none";
		}else {
		uploadDiv.style.display = "block";
		}
	}

	function setRemark()
	{
		var cb = document.getElementsByName("cb");
		var remark = document.getElementById("remark").value;
		for(var i = 0; i < cb.length; i++){
			document.getElementById("remark_"+cb[i].value).value = remark;
		}
	}
	
	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	}
</script>
</head>

<body>
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<input type="hidden" name="planState" id="planState" />
<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
<input type="hidden" name="chgorgCname" id=chgorgCname value="${companyName }"/>
<input type="hidden" name="actionURL" id="actionURL" value="${actionURL }"/>
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件仓库管理&gt;配件状态变更&gt;现场BO单处理&gt;新增</div>
  <div >
  <table class="table_query"  bordercolor="#DAE0EE">
  
  	<tr>
      <th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 信息</th>
    </tr>
    <tr>
    
      <td width="20%" align="right">变更单号：</td>
      <td width="30%" align="left" >${changeCode}
        <input type="hidden" name="changeCode" id="changeCode" value="${changeCode}" />
      </td>
      <td width="20%" align="right">制单单位：</td>
      <td width="30%" align="left" >${companyName }
      </td>
    </tr>
    <tr>
      <td align="right">制单人：</td>
      <td align="left">${marker}</td>
      <td align="right">仓库：</td>
      <td align="left" >
         <select  name="whId" id = "whId" style="width:150px;" onchange="WHChanged()">
  			<c:if test="${WHList!=null}">
				<c:forEach items="${WHList}" var="list">
				  <c:choose> 
					<c:when test="${selectedWhId eq list.WH_ID}">
					  <option selected="selected" value="${list.WH_ID }">${list.WH_CNAME }</option>
					</c:when>
					<c:otherwise>
					  <option value="${list.WH_ID }">${list.WH_CNAME }</option>
					</c:otherwise>
				  </c:choose>
				</c:forEach>
			</c:if>
      	</select> <font color="#FF000">*请先选择仓库</font>
	  </td>
    </tr>
    <tr>
      <td align="right">备注：</td>
      <td  align="left" colspan="3">
        <textarea style="width:80%" id="remark" name="remark" onchange="setRemark()"></textarea></td>
    </tr>
    </table>
   

				<FIELDSET >
					<LEGEND 
						style="MozUserSelect: none; KhtmlUserSelect: none"
						unselectable="on">
					<th colspan="6">
						<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
						配件库存查询
						<input type="button" class="normal_btn" name="addPartViv"
							id="addPartViv" value="增 加" onclick="addPartDiv()" />
					</th>
					</LEGEND>
					<div style="display: none; heigeht: 5px" id="partDiv">
						<table class="table_query" width=100% border="0" align="center"
							cellpadding="1" cellspacing="1">
							<tr>
								<td align="right" width="10%">
									配件编码：
								</td>
								<td align="left" width="20%">
									<input class="middle_txt" id="partOldcode"
										datatype="1,is_noquotation,30" name="partOldcode"
										onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" />
								</td>
								<td align="right" width="10%">
									配件名称：
								</td>
								<td align="left" width="20%">
									<input class="middle_txt" id="partCname"
										datatype="1,is_noquotation,30" name="partCname"
										onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" />
								</td>
								<td align="right" width="10%">
									件号：
								</td>
								<td width="20%" align="left">
									<input class="middle_txt" id="partCode"
										datatype="1,is_noquotation,30" name="partCode"
										onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" />
								</td>
							</tr>
							<tr>
								<td align="center" colspan="6">
									<input class="normal_btn" type="button" name="BtnQuery"
										id="queryBtn" value="查 询" onclick="__extQuery__(1)" />
									<input class="normal_btn" type="button" name="BtnQuery"
										id="queryBtn" value="添 加" onclick="addCells()" />
								</td>
							</tr>
						</table>
							<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
							<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
					</div>
					</FIELDSET>
					<table id="file" class="table_list" style="border-bottom: 1px;">
						<tr>
							<th colspan="13" align="left">
								<img src="<%=contextPath%>/img/nav.gif" />库存变更详细
						</tr>
						<tr class="table_list_row0">
							<td>
								<input type="checkbox" onclick="selAll2(this)"  />
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
								单位
							</td>
							<td>
								可用库存
							</td>
							<td>
								正常封存
							</td>
							<td>
								业务类型
							</td>
							<td>
								调整类型<font color="red">*</font>
							</td>
							<td>
								调整数量 <font color="red">*</font>
							</td>
							<td>
								备注
							</td>
							<td>
								操作
							</td>
						</tr>
						<c:if test="${list !=null}">
						  <c:forEach items="${list}" var="list" varStatus="_sequenceNum">
							<c:if test="${((_sequenceNum.index+1) mod 2) != 0}">
							<tr class="table_list_row1">
							</c:if>
							<c:if test="${((_sequenceNum.index+1) mod 2) == 0}">
							<tr class="table_list_row2">
							</c:if>
							  <td align="center" nowrap>
							    <input  type="checkbox" value="${list.partId}" id="cell_${_sequenceNum.index+1}" name="cb" checked="checked" />
							  </td>
							  <td align="center" nowrap>${_sequenceNum.index+1}
							    <input id="idx_${list.partId}" name="idx_${list.partId}" value="${_sequenceNum.index+1}" type="hidden" >
							  </td>
							  <td align="center">
							    <input   name="partOldcode_${list.partId}" id="partOldcode_${list.partId}" value="${list.partOldcode}" type="hidden" />${list.partOldcode}
							  </td>
							  <td align="center" nowrap>
							    <input   name="partCname_${list.partId}" id="partCname_${list.partId}" value="${list.partCname}" type="hidden" class="cname_${list.partId}"/>${list.partCname}
							  </td>
							  <td align="center" nowrap>
							    <input   name="partCode_${list.partId}" id="partCode_${list.partId}" value="${list.partCode}" type="hidden" />${list.partCode}
							  </td>
							  <td align="center" nowrap>
							    <input   name="unit_${list.partId}" id="unit_${list.partId}" value="${list.unit}" type="hidden" />${list.unit}
							  </td>
							  <td align="center" nowrap>
							    <input   name="normalQty_${list.partId}" id="normalQty_${list.partId}" value="${list.normalQty}" type="hidden" />${list.normalQty}
							  </td>
							  <td align="center" nowrap>
							    <input   name="fcQty_${list.partId}" id="fcQty_${list.partId}" value="${list.fcQty}" type="hidden" />${list.fcQty}
							  </td>
							  <td align="center" nowrap>
							    <input   name="bzType_${list.partId}" id="bzType_${list.partId}" value="<%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_07%>" type="hidden" />现场BO
							  </td>
							  <td align="center" nowrap>
							    <input type="hidden" name="cgType_${list.partId}" id="cgType_${list.partId}" value="<%=Constant.PART_STOCK_STATUS_CHANGE_TYPE_01%>" />封存
							  </td>
							  <td align="center" nowrap>
							    <input class="short_txt" name="returnQty_${list.partId}" id="returnQty_${list.partId}" value="${list.returnQty}" type="hidden" />${list.returnQty}
							  </td>
							  <td align="center" nowrap>
							    <input class="short_txt" name="remark_${list.partId}" id="remark_${list.partId}" value="${list.remark}" type="hidden" />${list.remark}
							  </td>
							  <td>
							    <input  type="button" class="short_btn"  name="deleteBtn" value="删 除" onclick="deleteTblRow('${_sequenceNum.index+2}');" />
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
									onclick="showUpload();">
								<input class="cssbutton" type="button" value="保 存" id="saveButton" name="button1"
									onclick="confirmSubmit();">
								<input class="cssbutton" type="button" value="返 回" name="button1"
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
					<div style="display:none ; heigeht: 5px" id="uploadDiv">
					 <table>
	  <tr>
	    <td><font color="red">
	      <input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate()"/>
        文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
        	<input type="file" name="uploadFile" id="uploadFile" style="width: 250px"  datatype="0,is_null,2000" value="" />
        	&nbsp;
        <input type="button" id="upbtn" class="normal_btn" value="确 定" onclick="confirmUpload()"/></td>
    </tr>
  </table>
  </div>
</div>
</form>
</BODY>
</html>

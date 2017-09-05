<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
			request.setAttribute("wareHouseList", request.getAttribute("wareHouseList"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>库存调整新增申请</title>
<script type=text/javascript>
var myPage;
var url = "<%=contextPath%>/parts/storageManager/stockNumManager/StockNumApplyAction/queryPartStock.json";
var title = null;
var columns = [
			{header: "序号", align:'center', renderer:getIndex,width:'7%'},
			{header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', align:'center',width: '33px' ,renderer: seled},
			{header: "件号", dataIndex: 'PART_CODE', align:'center'},
			{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center'},
			{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'},
			{header: "单位", dataIndex: 'UNIT', align:'center'},
			{header: "货位", dataIndex: 'LOC_NAME', align:'center', renderer: returnLocCode},
			{header: "批次号", dataIndex: 'BATCH_NO', align:'center'},
			{header: "库存数量", dataIndex: 'ITEM_QTY', align:'center'},
			{header: "可用库存", dataIndex: 'NORMAL_QTY', align:'center'}
		  ];
		  
var tidx = 0;
var tidxn = "";
function seled(value,meta,record){
	tidx++;
	tidxn = record.data.PART_ID + '_RNUM'+tidx;
    return "<input type='checkbox' value='" + tidxn + "' name='ck' id='ck_" + tidxn + "' />";
}

function returnLocCode(value, meta, record) {
    var inputVal = record.data.PART_ID + "," + record.data.LOC_ID + "," + record.data.LOC_CODE + "," + record.data.LOC_NAME;
    inputVal += "," + record.data.BATCH_NO;
    var html = "<input type='hidden' value='" + inputVal + "' name='loc_" + tidxn + "' id='loc_" + tidxn + "'/>" + value;
    html += '<input type="hidden" value="'+record.data.STOCK_ID+'" name="stockId_'+tidxn+'"/>';
    html += '<input type="hidden" value="'+record.data.BOOK_ID+'" name="bookId_'+tidxn+'"/>';
    return html;
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
	if(document.getElementById("abjustmentType").value == ""){
		MyAlert('请先选择调整类型！</br>');
		return false;
	}
	var ck = document.getElementsByName('ck');
	var mt = document.getElementById("myTable");
	var abjustmentType = document.getElementById('abjustmentType');
	var cn=0;
	for(var i = 1 ;i<mt.rows.length; i ++){
		if(mt.rows[i].cells[1].firstChild.checked){
			cn++;
	        var partId = mt.rows[i].cells[1].firstChild.value;  //ID
	        var loc = document.getElementById("loc_" + partId).value;  //货位信息
            if (validateCell(partId, loc)) {
				var itemQty = mt.rows[i].cells[8].innerText;  // 库存数量
				var normalQty = mt.rows[i].cells[9].innerText;  // 可用数量
				if(abjustmentType == <%=Constant.PART_ABJUSTMENT_TYPE_02%> && parseInt(normalQty) <= 0){
					MyAlert("第"+i+"行配件【"+mt.rows[i].cells[2].innerText+"】：</br>可用库存数量小于等于0时不能减少库存!</br>");
					break;
				}
				var partCode = mt.rows[i].cells[2].innerText;  //件号
				var partOldcode = mt.rows[i].cells[3].innerText;  //配件编码
				var partCname = mt.rows[i].cells[4].innerText;  //配件名称
				var unit = mt.rows[i].cells[5].innerText;  //单位
                var batchCode = mt.rows[i].cells[7].innerText;  // 批次号
				
				addCell(partId, partCode, partOldcode, partCname, normalQty, itemQty, unit, loc, batchCode);
			}else{
				MyAlert("第"+i+"行配件【"+mt.rows[i].cells[2].innerText+"】已存在!</br>");
				break;
			}
		}
       }
	if(cn==0){
		MyAlert("请选择要添加的配件信息!");
	}
}

// 验证是否已选择配件
function validateCell(spartId, loc) {
    var partIds = document.getElementsByName("cb");
    if (partIds && partIds.length > 0) {
        for (var i = 0; i < partIds.length; i++) {
        	var loc2 = document.getElementById("locInfo_"+partIds[i].value).value;
            if (loc == loc2) {
                return false;
            }
        }
        return true;
    }
    return true;
}

// 添加配件行
function addCell(partId,partCode, partOldcode, partCname, normalQty, itemQty,unit, loc, batchCode){
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
		
		var locArr = loc.split(",");
		var locName = locArr[3];
		
		cell1.innerHTML = '<tr><td align="center" nowrap><input  type="checkbox" value="'+partId+'" id="cell_'+(tbl.rows.length-1)+'" name="cb" checked="true" /></td>';
		cell2.innerHTML = '<td align="center" nowrap><input id="idx_'+partId+'" name="idx_'+partId+'" value="'+(tbl.rows.length-1)+'" type="hidden" ></td>'+(tbl.rows.length-1);                                                                                                                            
		cell5.innerHTML = '<td align="center" nowrap><input name="partCode_'+partId+'" id="partCode_'+partId+'" value="'+partCode+'" type="hidden"/>'+partCode+'</td>';                                                 
		cell3.innerHTML = '<td align="center" nowrap><input name="partOldcode_'+partId+'" id="partOldcode_'+partId+'" value="'+partOldcode+'" type="hidden" />'+partOldcode+'</td>';                                               
		cell4.innerHTML = '<td align="center" nowrap><input name="partCname_'+partId+'" id="partCname_'+partId+'" value="'+partCname+'" type="hidden" class="cname_'+partId+'"/>'+partCname+'</td>';
		cell6.innerHTML = '<td align="center" nowrap><input name="unit_'+partId+'" id="unit_'+partId+'" value="'+unit+'" type="hidden" />'+unit+'</td>';
		var locHtml = '<td align="center" nowrap>';
// 		locHtml += '<input type="hidden" id="locId_'+partId+'" name="locId_'+partId+'" value="'+locId+'"/>';
// 		locHtml += '<input type="hidden" id="locCode_'+partId+'" name="locCode_'+partId+'" value="'+locCode+'"/>';
		locHtml += '<input type="hidden" id="locInfo_'+partId+'" name="locInfo_'+partId+'" value="'+loc+'"/>'+locName;
		locHtml += '</td>';
		cell7.innerHTML = locHtml;
		cell8.innerHTML = '<td align="center" nowrap><input name="batchCode_'+partId+'" id="batchCode_'+partId+'" value="'+batchCode+'" type="hidden" />'+batchCode+'</td>';
		cell9.innerHTML = '<td align="center" nowrap><input name="itemQty_'+partId+'" id="itemQty_'+partId+'" value="'+itemQty+'" type="hidden" />'+itemQty+'</td>';
		cell10.innerHTML = '<td align="center" nowrap><input name="normalQty_'+partId+'" id="normalQty_'+partId+'" value="'+normalQty+'" type="hidden" />'+normalQty+'</td>';
		cell11.innerHTML = '<td align="center" nowrap><input class="short_txt" name="abjustmentNum_'+partId+'" id="abjustmentNum_'+partId+'" type="text" onchange="validAbjNum('+normalQty+',this)" maxlength="10"/></td>';
		cell12.innerHTML = '<td align="center" nowrap><input class="middle_txt" name="remark_'+partId+'" id="remark_'+partId+'" type="text"/></td>';
		cell13.innerHTML = '<td><input type="button" class="u-button"  name="deleteBtn" value="删除" onclick="deleteTblRow('+(tbl.rows.length-1)+');" /></td></TR>';                                                               
}

//移除非数字字符并验证库存数量大于调整数量
function validAbjNum(maxNum, obj){
	var replaceVal = obj.value.replace(/\D/g,'');
	if(replaceVal == ''){
		replaceVal = 0;
	}
	var abjustmentType = document.getElementById("abjustmentType").value;
	if(abjustmentType == <%=Constant.PART_ABJUSTMENT_TYPE_02%> && maxNum < parseInt(replaceVal)){
		MyAlert('调整数量必须小于等于库存数量！');
		replaceVal = 0;
	}
	obj.value = replaceVal;
}

//移除非数字字符
function removeNotNum(obj){
	var str = obj.value.replace(/\D/g,'');
	obj.value = str;
}

// 删除行
function deleteTblRow(rowNum) {
	var tbl = document.getElementById('file');
	tbl.deleteRow(rowNum);
	var count = tbl.rows.length;
	for (var i=rowNum;i<count;i++)
       {
         tbl.rows[i].cells[1].innerText=i;
         tbl.rows[i].cells[12].innerHTML="<td><input type=\"button\" class=\"u-button\"  name=\"deleteBtn\" value=\"删除\" onclick='deleteTblRow("+i+")'/></td></tr>";
         if(i%2==0){
	    	tbl.rows[i].className   = "table_list_row1";
		  }else{
			  tbl.rows[i].className  = "table_list_row2";
		  }
      }
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
		if(document.getElementById("abjustmentType").value == ""){
			MyAlert('请先选择调整类型！</br>');
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

// 删除表格下所有记录
function deleteAllTblRow(obj){
    var tbl = document.getElementById('file');
    var len = tbl.rows.length;
    if (len > 1) {
        //改变仓库之后就要删除移库明细,重新选择
        for (var i = tbl.rows.length - 1; i >= 1; i--) {
            tbl.deleteRow(i);
        }
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

//保存
function confirmSubmit(){
	var abjustmentType = document.getElementById("abjustmentType").value;
	var msg = "";

	if(abjustmentType == ""){
		MyAlert('请先选择调整类型！</br>');
		return;
	}

	if(document.getElementById("whId").value==""){
		MyAlert('请先选择仓库！</br>');
		return;
	}
	var whidObj = document.getElementById("whId");
	var index = whidObj.selectedIndex; // 选中索引
	var text = whidObj.options[index].text; // 选中文本
	document.getElementById("whName").value = text;
	
	var maxLineNum = <%=Constant.PART_STOCK_STATUS_CHANGE_MAX_LINENUM%>;
	var cb = document.getElementsByName("cb");
	if(cb.length<=0){
		MyAlter('请添加配件库存信息！</br>');
		return;
	}
	if(maxLineNum < cb.length)
	{
		MyAlert("添加的数据不能超过 " + maxLineNum +" 行!</br>");
		return;
	}
	var ary = new Array();
	var l = cb.length;
	var flag = false;
	for(var i=0;i<l;i++)
	{        
		if(cb[i].checked)
		{
			var partId = cb[i].value;
			var itemQty = document.getElementById('itemQty_'+partId).value;
			var normalQty = document.getElementById('normalQty_'+partId).value;
			if(normalQty <= 0 && abjustmentType == <%=Constant.PART_ABJUSTMENT_TYPE_02%>){
				MyAlert('第'+(i+1)+'行可用库存数量小于1，不能够减少库存！');
				return;
			}
			var abjustmentNum = document.getElementById('abjustmentNum_'+partId).value;
			if(abjustmentNum == ''){
				MyAlert('第'+(i+1)+'行调整数量不能够小于1！');
				return;
			}
			if(normalQty < abjustmentNum && abjustmentType == <%=Constant.PART_ABJUSTMENT_TYPE_02%>){
				MyAlert('减少库存时，第'+(i+1)+'行调整数量不能够大于可用库存数量！');
				return;
			}
			ary.push(partId);	
			flag = true;
		}
	}
	
	if(!flag){
		MyAlert("请选择配件库存调整信息!</br>");
		return;
	}

	var s = ary.join(",")+",";
    var pflag = false;
    var nclass="";
    var sid="";
    for(var i=0;i<ary.length;i++){
    	$(".cname_"+ary[i]).parent("td").css({background:""});
    }
    for(var i=0;i<ary.length;i++){
    	if(s.replace(ary[i]+",","").indexOf(ary[i]+",")>-1) {
    		pflag = true;
	    	sid = "partCname_"+ary[i];
	    	nclass = "cname_"+ary[i];
	    	var partCname = document.getElementById(sid).value;
	    	MyAlert("配件：" +partCname+" 被重复上传!" );
    		return;
    	}
    }
    if(pflag){
    	$("."+nclass).parent("td").css({background:"red"});
    	return false;
    }
	
	if(msg!=""){
		MyAlert(msg);
		for(var i =0 ;i<cb.length;i++){
			cb[i].disabled = false;
		}
		return;
	}
	MyConfirm("确定保存修改信息?",savePlan,[]);
	for(var i =0 ;i<cb.length;i++){
		cb[i].disabled = false;
	}
}

function savePlan(){
	btnDisable();
	var url = "<%=contextPath%>/parts/storageManager/stockNumManager/StockNumApplyAction/modStockNumApply.json";	
	sendAjax(url,getResult,'fm');
}

function getResult(json){
	btnEnable();
	if(null != json){
		if(json.returnCode == 1){
        	MyAlert("保存成功!", function(){
	        	window.location.href = "<%=contextPath%>/parts/storageManager/stockNumManager/StockNumApplyAction/queryStockNumApplyInit.do";
        	});
		}else{
            MyAlert("保存失败，请联系管理员!");
		}
	}
}

// 返回
function goBack(){
	btnDisable();
	fm.action = "<%=contextPath%>/parts/storageManager/stockNumManager/StockNumApplyAction/queryStockNumApplyInit.do";
	fm.submit();
}
</script>
</head>
<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input type="hidden" id="abjustmentId" name="abjustmentId" value="${mainMap.ABJUSTMENT_ID }">
			<input type="hidden" id="abjustmentType" name="abjustmentType" value="${mainMap.ABJUSTMENT_TYPE}">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件仓库管理&gt;库存调整&gt;库存调整申请&gt;新增
			</div>
			<div class="form-panel">
				<table class="table_list">
					<tr>
						<th colspan="12" align="left">
							<img src="<%=contextPath%>/img/nav.gif" />库存调整信息
						</th>
					</tr>
				</table>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right" width="20%">调整编码：</td>
							<td width="30%">${mainMap.ABJUSTMENT_CODE}
								<input type="hidden" name="abjustmentCode" id="abjustmentCode" value="${mainMap.ABJUSTMENT_CODE}" />
							</td>
							<td class="right" width="20%">调整类型：</td>
							<td width="30%">
								<input type="hidden" name="abjustmentType" value="${mainMap.ABJUSTMENT_TYPE}">
								${mainMap.ABJUSTMENT_TYPE_DESC}
							</td>
						</tr>
						<tr>
							<td class="right">调整仓库：</td>
							<td colspan="3">
								<input type="hidden" id="whName" name="whName">
								<select class="u-select" name="whId" id="whId" style="width: 150px;" onchange="WHChanged()">
									<c:if test="${not empty WHList}">
										<c:forEach items="${WHList}" var="list">
											<option <c:if test="${mainMap.WH_ID eq list.WH_ID}">selected</c:if> value="${list.WH_ID }">${list.WH_CNAME }</option>
										</c:forEach>
									</c:if>
								</select> <font color="#FF000">*</font>
							</td>
						</tr>
						<tr>
							<td class="right">备注：</td>
							<td align="left" colspan="3">
								<textarea class="form-control" style="width: 80%" id="remark" name="remark">${mainMap.REMARK }</textarea>
							</td>
						</tr>
					</table>
				</div>
				<table id="file" class="table_list">
					<caption>
						<img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif" />配件信息
					</caption>
					<tr>
						<th>
							<input type="checkbox" onclick="selAll2(this)" />
						</th>
						<th>序号</th>
						<th>配件编码</th>
						<th>配件名称</th>
						<th>件号</th>
						<th>单位</th>
						<th>货位</th>
						<th>批次号</th>
						<th>账面库存</th>
						<th>可用数量</th>
						<th>调整数量</th>
						<th>备注</th>
						<th>操作</th>
						<c:forEach items="${dtlList}" var="list" varStatus="_seq" step="1">
							<tr class="table_list_row1">
								<td align="center" nowrap>
									<input type="checkbox" value="${list.PART_ID}_RNUM_${_seq.index}" id="cell_${_seq.index+1}" name="cb" checked="checked" />
									<input type="hidden" value="${list.STOCK_ID}" name="stockId_${list.PART_ID}_RNUM_${_seq.index}" />
									<input type="hidden" value="${list.BOOK_ID}" name="bookId_${list.PART_ID}_RNUM_${_seq.index}" />
								</td>
								<td align="center" nowrap>${_seq.index+1}
									<input id="idx_${list.PART_ID}_RNUM_${_seq.index}" name="idx_${list.PART_ID}_RNUM_${_seq.index}" value="${_sequenceNume.index+1}" type="hidden">
								</td>
								<td align="center" nowrap>
									<input name="partOldcode_${list.PART_ID}_RNUM_${_seq.index}" id="partOldcode_${list.PART_ID}_RNUM_${_seq.index}" value="${list.PART_OLDCODE}" type="hidden" />${list.PART_OLDCODE}
								</td>
								<td align="center" nowrap>
									<input name="partCname_${list.PART_ID}_RNUM_${_seq.index}" id="partCname_${list.PART_ID}_RNUM_${_seq.index}" value="${list.PART_CNAME}" type="hidden" />${list.PART_CNAME}
								</td>
								<td align="center" nowrap>
									<input name="partCode_${list.PART_ID}_RNUM_${_seq.index}" id="partCode_${list.PART_ID}_RNUM_${_seq.index}" value="${list.PART_CODE}" type="hidden" />${list.PART_CODE}
								</td>
								<td align="center" nowrap>
									<input name="unit_${list.PART_ID}_RNUM_${_seq.index}" id="unit_${list.PART_ID}_RNUM_${_seq.index}" value="${list.UNIT}" type="hidden" />${list.UNIT}
								</td>
								<td align="center" nowrap>
									<input name="locInfo_${list.PART_ID}_RNUM_${_seq.index}" id="locInfo_${list.PART_ID}_RNUM_${_seq.index}"
										value="${list.PART_ID},${list.LOC_ID},${list.LOC_CODE},${list.LOC_NAME},${list.BATCH_CODE}" type="hidden" />
										${list.LOC_NAME}
								</td>
								<td align="center" nowrap>
									<input name="batchCode_${list.PART_ID}_RNUM_${_seq.index}" id="batchCode_${list.PART_ID}_RNUM_${_seq.index}" value="${list.BATCH_CODE}" type="hidden" />${list.BATCH_CODE}
								</td>
								<td align="center" nowrap>
									<input name="itemQty_${list.PART_ID}_RNUM_${_seq.index}" id="itemQty_${list.PART_ID}_RNUM_${_seq.index}" value="${list.ITEM_QTY}" type="hidden" />${list.ITEM_QTY}
								</td>
								<td align="center" nowrap>
									<input name="normalQty_${list.PART_ID}_RNUM_${_seq.index}" id="normalQty_${list.PART_ID}_RNUM_${_seq.index}" value="${list.ITEM_QTY}" type="hidden" />${list.NORMAL_QTY}
								</td>
								<td align="center" nowrap>
									<input class="short_txt" name="abjustmentNum_${list.PART_ID}_RNUM_${_seq.index}" id="abjustmentNum_${list.PART_ID}_RNUM_${_seq.index}" value="${list.ABJUSTMENT_NUM}" type="text" />
								</td>
								<td align="center" nowrap>
									<input class="middle_txt" name="remark_${list.PART_ID}_RNUM_${_seq.index}" id="remark_${list.PART_ID}_RNUM_${_seq.index}" value="${list.REMARK}" type="text" />
								</td>
								<td>
									<input type="button" class="u-button" name="deleteBtn" value="删除" onclick="deleteTblRow('${_seq.index+1}');" />
								</td>
							</tr>
						</c:forEach>
				</table>
				<table width="100%">
					<tr>
						<td height="2"></td>
					</tr>
					<tr>
						<td align="center">
							<input class="u-button" type="button" value="保 存" id="saveButton" name="button1" onclick="confirmSubmit();">
							<input class="u-button" type="button" value="返 回" name="button1" onclick="goBack();">
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
				<div id="main_body">
					<FIELDSET>
						<LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
							<th colspan="6">
								<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> 配件库存查询
								<input type="button" class="normal_btn" name="addPartViv" id="addPartViv" value="增 加" onclick="addPartDiv()" />
							</th>
						</LEGEND>
						<div style="display: none; heigeht: 5px" id="partDiv">
							<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
								<tr>
									<td class="right">配件编码：</td>
									<td>
										<input class="middle_txt" id="partOldcode" datatype="1,is_noquotation,30" name="partOldcode" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" />
									</td>
									<td class="right">配件名称：</td>
									<td>
										<input class="middle_txt" id="partCname" datatype="1,is_noquotation,30" name="partCname" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" />
									</td>
									<td class="right">库存数量范围：</td>
									<td>
										<input type="text" class="short_txt" id="itemQtyRangeStart" name="itemQtyRangeStart" maxlength="" onkeyup="removeNotNum(this)" />
										&nbsp;-&nbsp;
										<input type="text" class="short_txt" id="itemQtyRangeEnd" name="itemQtyRangeEnd" maxlength="" onkeyup="removeNotNum(this)" />
									</td>
								</tr>
								<tr>
									<td class="center" colspan="6">
										<input class="u-button" type="button" name="BtnQuery" id="queryBtn" value="查 询" onclick="__extQuery__(1)" />
										<input class="u-button" type="button" name="BtnQuery" id="queryBtn" value="添 加" onclick="addCells()" />
									</td>
								</tr>
							</table>
							<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
							<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
						</div>
					</FIELDSET>
				</div>
				<div style="display: none; heigeht: 5px" id="uploadDiv">
					<table>
						<tr>
							<td>
								<font color="red"> <input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate()" /> 文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;
								</font>
								<input type="file" name="uploadFile" id="uploadFile" style="width: 250px" datatype="0,is_null,2000" value="" />
								&nbsp;
								<input type="button" id="upbtn" class="normal_btn" value="确 定" onclick="confirmUpload()" />
							</td>
						</tr>
					</table>
				</div>
			</div>
		</form>
		</dody>
	</div>
</html>

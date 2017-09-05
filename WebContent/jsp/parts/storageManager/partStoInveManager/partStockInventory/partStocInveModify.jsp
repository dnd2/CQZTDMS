<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
			request.setAttribute("wareHouseList", request.getAttribute("wareHouseList"));
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<TITLE>库存盘点</TITLE>
<script type=text/javascript>
$(function(){
	showOrCloseMainBody();
});
function showOrCloseMainBody() {
	var main_body = document.getElementById("main_body");
	var typeAll = <%=Constant.PART_STOCK_INVE_TYPE_01%>;
	var oriType = '${map.CHECK_TYPE}';
	if (parseInt(typeAll) == parseInt(oriType)) {
		main_body.style.display = "none";
	}
}

 var myPage;

 var url = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/showPartStockBase.json";
				
 var title = null;

 var columns = [
				{header: "序号", align:'center', renderer:getIndex,width:'7%'},
				{header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', align:'center',width: '33px' ,renderer:seled},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center',style:'text-align: center;'},
				{header: "配件名称", dataIndex: 'PART_CNAME', align:'center',style:'text-align: center;'},
				{header: "件号", dataIndex: 'PART_CODE', align:'center',style:'text-align: center;'},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "批次号", dataIndex: 'BATCH_NO', align:'center'},
				{header: "货位", dataIndex: 'LOC_NAME', align:'center',style:'text-align: center;', renderer: returnLocCode},
				{header: "可用库存", dataIndex: 'NORMAL_QTY', align:'center'},
				{header: "占用库存", dataIndex: 'BOOKED_QTY', align:'center'},
				{header: "正常封存", dataIndex: 'ZCFC_QTY', align:'center'},
				{header: "盘亏封存", dataIndex: 'PKFC_QTY'},
				{header: "账面库存", dataIndex: 'ITEM_QTY', align:'center'}
				
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
    html += '<input type="hidden" value="'+record.data.STOCK_VENDER_ID+'" name="venderId_'+tidxn+'"/>';
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
	var ck = document.getElementsByName('ck');
	var mt = document.getElementById("myTable");
	var cn=0;
	for(var i = 1 ;i<mt.rows.length; i ++){
		var partId = mt.rows[i].cells[1].firstChild.value;  //ID
        var loc = document.getElementById("loc_" + partId).value;  //货位信息
		if(mt.rows[i].cells[1].firstChild.checked){
			cn++;
            if (validateCell(partId, loc)) {
				var partCode = mt.rows[i].cells[4].innerText;  //件号
				var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
				var partCname = mt.rows[i].cells[3].innerText;  //配件名称
				var unit = mt.rows[i].cells[5].innerText;  //单位
				var batchCode = mt.rows[i].cells[6].innerText;  //批次号
				var itemQty = mt.rows[i].cells[12].innerText;  //当前账面库存

			    var locArr = loc.split(",");
				var locId = locArr[1]; //货位id
				var locCode = locArr[2]; //货位编码
				var locName = locArr[3]; //货位名称
			    var batchNo = locArr[4]; // 批次号
// 				var normalQty = mt.rows[i].cells[6].innerText;  //当前可用库存
// 				var bookedQty = mt.rows[i].cells[7].innerText;  //已占用
// 				var fcQty = mt.rows[i].cells[8].innerText;  //已封存
// 				var unit = mt.rows[i].cells[9].innerText;  //单位
// 				addCell(partId, partCode, partOldcode, partCname, itemQty, normalQty, bookedQty, fcQty, unit);
				
				addCell(partId, partCode, partOldcode, partCname, itemQty, unit, locId, locCode, locName, batchCode);
			}else{
				MyAlert("第"+i+"行"+locName+"货位下配件："+mt.rows[i].cells[2].innerText+" 已存在!</br>");
				break;
			}
		}
       }

	if(cn==0){
		MyAlert("请选择要添加的配件信息!");
	}
}

function validateCell(spartId, loc) {
    var partIds = document.getElementsByName("cb");
    if (partIds && partIds.length > 0) {
        for (var i = 0; i < partIds.length; i++) {
        	var loc2 = document.getElementById("locName_"+partIds[i].value).value;
            if (loc == loc2) {
                return false;
            }
        }
        return true;
    }
    return true;
}

function addCell(partId,partCode, partOldcode, partCname,itemQty,unit, locId, locCode, locName, batchCode){
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
	
	cell1.innerHTML = '<tr><td align="center" nowrap><input  type="checkbox" value="'+partId+'" id="cell_'+(tbl.rows.length-1)+'" name="cb" checked="true" /></td>';                                                            
	cell2.innerHTML = '<td align="center" nowrap><input id="idx_'+partId+'" name="idx_'+partId+'" value="'+(tbl.rows.length-1)+'" type="hidden" ></td>'+(tbl.rows.length-1);                                                                                                                            
	cell3.innerHTML = '<td align="center" nowrap><input name="partOldcode_'+partId+'" id="partOldcode_'+partId+'" value="'+partOldcode+'" type="hidden" />'+partOldcode+'</td>';                                               
	cell4.innerHTML = '<td align="center" nowrap><input name="partCname_'+partId+'" id="partCname_'+partId+'" value="'+partCname+'" type="hidden" class="cname_'+partId+'"/>'+partCname+'</td>';
//		cell5.innerHTML = '<td align="center" nowrap><input name="partCode_'+partId+'" id="partCode_'+partId+'" value="'+partCode+'" type="hidden"/>'+partCode+'</td>';                                                 
	cell5.innerHTML = '<td align="center" nowrap><input name="unit_'+partId+'" id="unit_'+partId+'" value="'+unit+'" type="hidden" />'+unit+'</td>';
	var batchCodeHtml = '<td align="center" nowrap>'+batchCode;
	batchCodeHtml += '<input type="hidden" id="batchCode_'+partId+'" name="batchCode_'+partId+'" value="'+batchCode+'"/>';
	batchCodeHtml += '<input name="partCode_'+partId+'" id="partCode_'+partId+'" value="'+partCode+'" type="hidden"/>';
	batchCodeHtml += '</td>';
	cell6.innerHTML = batchCodeHtml;
	var locHtml = '<td align="center" nowrap>';
	locHtml += '<input type="hidden" id="locId_'+partId+'" name="locId_'+partId+'" value="'+locId+'"/>';
	locHtml += '<input type="hidden" id="locCode_'+partId+'" name="locCode_'+partId+'" value="'+locCode+'"/>';
	locHtml += '<input type="hidden" id="locName_'+partId+'" name="locName_'+partId+'" value="'+locName+'"/>'+locName;
	locHtml += '</td>';
	cell7.innerHTML = locHtml;
	cell8.innerHTML = '<td align="center" nowrap><input name="itemQty_'+partId+'" id="itemQty_'+partId+'" value="'+itemQty+'" type="hidden" />'+itemQty+'</td>';
	cell9.innerHTML = '<td align="center" nowrap><input class="middle_txt" name="remark_'+partId+'" id="remark_'+partId+'" type="text"/></td>';                                                            
	cell10.innerHTML = '<td><input type="button" class="u-button" name="deleteBtn" value="删除" onclick="deleteTblRow('+(tbl.rows.length-1)+');" /></td></tr>';                                                               

}

function deleteTblRow(rowNum) {
	var tbl = document.getElementById('file');
	tbl.deleteRow(rowNum);
	var count = tbl.rows.length;
	for (var i=rowNum;i<=count;i++)
       {
         tbl.rows[i].cells[1].innerText=i;
         tbl.rows[i].cells[9].innerHTML='<td><input type="button" class="u-button"  name="deleteBtn" value="删除" onclick="deleteTblRow('+i+');" /></td></tr>';
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
	
	var tbl = document.getElementById('file');
	for(var i = 2; i <= tbl.rows.length; i++){
		tbl.deleteRow(i);
	}
}

//上传
function uploadExcel(){
	var msg = "";
	if(document.getElementById("whId").value==""){
		msg += "请先选择仓库!</br>";
	}
	if(msg != "")
	{
		MyAlert(msg);
		return false;
	}
	fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/stockInventoryUpload.do";
	fm.submit();
}


function savePlan(){
	btnDisable();
	var url = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/saveModifyStockInfos.json";	
	sendAjax(url,getResult,'fm');
}

function getResult(json){
	btnEnable();
	if(null != json){
        if (json.errorExist != null && json.errorExist.length > 0) {
        	 MyAlert(json.errorExist);
        } else if (json.success != null && json.success == "true") {
        	btnDisable();
        	MyAlert("保存成功!", function(){
	            window.location.href = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/stockInventoryInit.do";
        	});
        } else {
            MyAlert("保存失败，请联系管理员!");
        }
	}
}


//下载上传模板
function exportExcelTemplate(){
	fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/exportExcelTemplate.do";
	fm.submit();
}

//保存
function confirmSubmit(){
	var typeAll = <%=Constant.PART_STOCK_INVE_TYPE_01%>;
	var cb = document.getElementsByName("cb");
	var invType = document.getElementById("inveType").value;
	var msg = "";

	if(invType==""){
		msg += "请先选择盘点类型!</br>";
	}
	
	if(document.getElementById("whId").value==""){
		msg += "请先选择仓库!</br>";
	}

	var maxLineNum = <%=Constant.PART_STOCK_STATUS_CHANGE_MAX_LINENUM%>;
	if(maxLineNum < cb.length)
	{
		msg += "添加的数据不能超过 " + maxLineNum +" 行!</br>";
	}
	
	//提交时,将其属性设置成DISABLED,从而达到过滤选择的目的
	var flag = false;
	if(typeAll == invType)
	{
		flag = true;
	}
	else
	{
		for(var i =0 ;i<cb.length;i++){
			if(cb[i].checked){
				cb[i].disabled = false;
				flag = true;
			}else{
				cb[i].disabled = true;
			}
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
	MyConfirm("确定保存修改信息?",savePlan,[]);
	for(var i =0 ;i<cb.length;i++){
		cb[i].disabled = false;
	}
}

function goBack(){
	btnDisable();
	//fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/stockInventoryInit.do";
	//fm.submit();
	window.history.back(-1);
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

function doCusChange(typeValue){
	var typeAll = <%=Constant.PART_STOCK_INVE_TYPE_01%>;
	var main_body = document.getElementById("main_body");
	if(typeAll == typeValue)
	{
		main_body.style.display = "none";
	}
	else
	{
		main_body.style.display = "block";
	}
}

</script>
</head>
<body>
	<form name="fm" id="fm" method="post" enctype="multipart/form-data">
		<input type="hidden" name="planState" id="planState" />
		<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
		<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
		<input type="hidden" name="chgorgCname" id=chgorgCname value="${companyName }" />
		<div class="wbox">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件仓库管理&gt;库存盘点管理&gt;库存盘点&gt;修改
			</div>
			<div>
				<div class="form-panel">
					<h2>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 信息
					</h2>
					<div class="form-body">
						<table class="table_query">
							<tr>

								<td class="right">盘点单号：</td>
								<td>${map.CHANGE_CODE}
									<input type="hidden" name="changeCode" id="changeCode" value="${map.CHANGE_CODE}" />
									<input type="hidden" name="changeId" id="changeId" value="${map.CHANGE_ID}" />
								</td>
								<td class="right">盘点类型：</td>
								<td>
									<script type="text/javascript">
										genSelBoxExp("inveType", <%=Constant.PART_STOCK_INVE_TYPE%>, '${map.CHECK_TYPE}', true, "", "", "false", "");
									</script>
									<font color="#FF000">*</font>
								</td>
							</tr>
							<tr>
								<td class="right">盘点人：</td>
								<td align="left">${marker}</td>
								<td class="right">盘点仓库：</td>
								<td align="left">
									<c:if test="${WHList!=null}">
										<c:forEach items="${WHList}" var="list">
											<c:if test="${selectedWhId eq list.WH_ID}">
												${list.WH_CNAME }
												<input type="hidden" name="whId" id="whId" value="${selectedWhId}" />
											</c:if>
										</c:forEach>
									</c:if>
								</td>
							</tr>
							<tr>
								<td class="right">备注：</td>
								<td colspan="3">
									<textarea class="form-control" style="width: 80%" id="remark" name="remark">${map.REMARK }</textarea>
								</td>
							</tr>
						</table>
					</div>
				</div>
			<div id="main_body">
				<table id="file" class="table_list" style="border-bottom: 1px;">
					<caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>盘点配件信息</caption>
					<tr>
						<th>
							<input type="checkbox" onclick="selAll2(this)" />
						</th>
						<th>序号</th>
						<th>配件编码</th>
						<th>配件名称</th>
<!-- 							<th>件号</th> -->
						<th>单位</th>
						<th>批次号</th>
						<th>货位</th>
						<th>账面库存</th>
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
							<td align="center" nowrap>
								<input type="checkbox" value="${list.PART_ID},${list.LOC_ID}" id="cell_${_sequenceNum.index}" name="cb" checked="checked" />
							</td>
							<td align="center" nowrap>${_sequenceNum.index+1}
								<input id="idx_${list.PART_ID},${list.LOC_ID}" name="idx_${list.PART_ID},${list.LOC_ID}" value="${_sequenceNume.index}" type="hidden">
							</td>
							<td align="center" nowrap>
								<input name="partCode_${list.PART_ID},${list.LOC_ID}" id="partCode_${list.PART_ID},${list.LOC_ID}" value="${list.PART_CODE}" type="hidden" />
								<input name="partOldcode_${list.PART_ID},${list.LOC_ID}" id="partOldcode_${list.PART_ID},${list.LOC_ID}" value="${list.PART_OLDCODE}" type="hidden" />${list.PART_OLDCODE}
							</td>
							<td align="center" nowrap>
								<input name="partCname_${list.PART_ID},${list.LOC_ID}" id="partCname_${list.PART_ID},${list.LOC_ID}" value="${list.PART_CNAME}" type="hidden" />${list.PART_CNAME}
							</td>
							<td align="center" nowrap>
								<input name="unit_${list.PART_ID},${list.LOC_ID}" id="unit_${list.PART_ID},${list.LOC_ID}" value="${list.UNIT}" type="hidden" />${list.UNIT}
							</td>
							<td align="center" nowrap>
								<input name="venderId_${list.PART_ID},${list.LOC_ID}" id="venderId_${list.PART_ID},${list.LOC_ID}" value="${list.VENDER_ID}" type="hidden" />
								<input name="batchCode_${list.PART_ID},${list.LOC_ID}" id="batchCode_${list.PART_ID},${list.LOC_ID}" value="${list.BATCH_CODE}" type="hidden" />
								${list.BATCH_CODE}
							</td>
							<td align="center" nowrap>
								<input name="locId_${list.PART_ID},${list.LOC_ID}" id="locId_${list.PART_ID},${list.LOC_ID}" value="${list.LOC_ID}" type="hidden" />
								<input name="locCode_${list.PART_ID},${list.LOC_ID}" id="locCode_${list.PART_ID},${list.LOC_ID}" value="${list.LOC_CODE}" type="hidden" />
								<input name="locName_${list.PART_ID},${list.LOC_ID}" id="locName_${list.PART_ID},${list.LOC_ID}" value="${list.LOC_NAME}" type="hidden" />${list.LOC_NAME}
							</td>
							<td align="center" nowrap>
								<input name="itemQty_${list.PART_ID},${list.LOC_ID}" id="itemQty_${list.PART_ID},${list.LOC_ID}" value="${list.ITEM_QTY}" type="hidden" />${list.ITEM_QTY}
							</td>
							<td align="center" nowrap>
								<input class="middle_txt" name="remark_${list.PART_ID},${list.LOC_ID}" id="remark_${list.PART_ID},${list.LOC_ID}" value="${list.REMARK}" type="text" />
							</td>
							<td>
								<input type="button" class="u-button" name="deleteBtn" value="删除" onclick="deleteTblRow('${_sequenceNum.index+1}');" />
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
					<FIELDSET>
						<LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
							<th colspan="6">
								<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> 配件库存查询
								<input type="button" class="normal_btn" name="addPartViv" id="addPartViv" value="增 加" onclick="addPartDiv()" />
							</th>
						</LEGEND>
						<div style="display: none; heigeht: 5px" id="partDiv">
							<table class="table_query">
								<tr>
									<td class="right">配件编码：</td>
									<td>
										<input class="middle_txt" id="partOldcode" datatype="1,is_noquotation,30" name="partOldcode" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" />
									</td>
									<td class="right">配件名称：</td>
									<td>
										<input class="middle_txt" id="partCname" datatype="1,is_noquotation,30" name="partCname" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" />
									</td>
									<td class="right">件号：</td>
									<td>
										<input class="middle_txt" id="partCode" datatype="1,is_noquotation,30" name="partCode" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text" />
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

			</div>
		</div>
	</form>
</body>
</html>

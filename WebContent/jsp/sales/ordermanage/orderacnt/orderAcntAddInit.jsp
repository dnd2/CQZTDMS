<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单审核账号新增</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;销售订单管理 &gt; 订单审核账号管理&gt;订单审核账号新增</div>
<form method="post" name="fm" id="fm">
	<input type="hidden" id="largeId" name="largeId" />
	<input type="hidden" id="largeIds" name="largeIds" />
	<table class="table_query" style="border: 0">
	<tr>
		<td><div align="right">选择账号:</div></td>
		<td align="left" width="30%">
		<input type="text" class="middle_txt" id="orderAcnt" readonly="readonly"  name="orderAcnt" onclick="showOrderAcnt();">
		<input type="hidden" id="orderAcntId" name="orderAcntId">
		<!-- <input name="button3" type="button" class="normal_btn" onclick="showOrderAcnt();" value="..." /> -->
		<input class="u-button" type="button" value="清空" onclick="clrTxt('orderAcnt','orderUserName');"/>
		</td>
		<td ><div align="right">用户姓名:</div></td>
		<td align="left"><input type="text" class="middle_txt" id="orderUserName" readonly="readonly" name="orderUserName" value=""></td>
	</tr>
	</table>
	<table  class="table_list">
	<tr>
		<th width="10%"><div align="left"><input name="button3" type="button" class="normal_btn" onclick="showOrderLarge();" value="选择大区" /></div></th>
		<th width="20%" >大区代码</th>
		<th width="30%">大区名称</th>
		<th width="10%">操作</th>
	</tr>
	 <tbody id="tbody1"></tbody>
	 <tr class="cssTable">
	</tr>
	</table>
	<table class="table_query">
	  <TR class=cssTable >
	  <td align="center">
		<input class="normal_btn" id="id1save" name="baocun" type="button" value="保存" onClick="confirmAdd();">
	    <input class="normal_btn" id="shangbao2" name="shangbao2" type="button" value="返回" onclick="history.back();" />
	    </td>
	    </TR>
	  </table>
</form>

<script type="text/javascript">
function clrTxt(value,name){
	document.getElementById(value).value="";
	document.getElementById(name).value="";
}
function showOrderAcnt(){
	//var dealerId = document.getElementById("dealerId").value;
	OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderacnt/OrderAcntQuery/showOrderAcnt.do',700,500);
}
function showOrderLarge(){
	var ids = "";
	var myForm = document.getElementById("fm");
	for (var i=0; i<myForm.length; i++){  
		var obj = myForm.elements[i];
		if(obj.id.length>=5 && obj.id.substring(0,5)=="orgId"){
			ids += obj.value + ",";
		}   
	} 
	ids = (ids == "" ? ids : ids.substring(0,ids.length-1));
	//var largeIds=document.getElementById("largeIds").value;
	//MyAlert("largeIds:"+largeIds);
	OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderacnt/OrderAcntQuery/showOrderLarge.do?largeIdss='+ids,700,500);
}

//新增产品
function addLarge(){	
	var largeId=document.getElementById("largeId").value;
	var acntId=document.getElementById("orderAcnt").value;
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderacnt/OrderAcntQuery/orderLargeAddMap.json";
	makeCall(url,addRow,{largeId:largeId,acntId:acntId}); 
}

function addRow(json){	
			var timeValue = json.info.ORG_ID;
			var newRow = document.getElementById("tbody1").insertRow();
			newRow.className  = "table_list_row2";
			var newCell = newRow.insertCell(0);
			newCell.align = "center";
			newCell.innerHTML = "";
			newCell = newRow.insertCell(1);
			newCell.align = "center";
			newCell.innerHTML = json.info.ORG_CODE;
			newCell = newRow.insertCell(2);
			newCell.align = "center";
			newCell.innerHTML = json.info.ORG_NAME;
			newCell = newRow.insertCell(3);
			newCell.align = "center";
			newCell.innerHTML = "<a href='#' onclick='delLarge();'>[删除]</a><input type='hidden' id='orgId"+timeValue+"' name='orgId"+timeValue+"' value='"+json.info.ORG_ID+"'>";
}
function delLarge(){	
  	document.getElementById("tbody1").deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex - 1);  
  	_setSelDisabled_("tbody1", 0) ;
}

function confirmAdd(){
	var actn=document.getElementById("orderAcnt").value;
	var largeIds=document.getElementById("largeIds").value;
	if(actn==null){
		MyAlert("请选择账号！");
		return;
	}
	if(largeIds==null){
		MyAlert("请选择区域！");
		return;
	}
	MyConfirm("是否确认保存？",orderAdd);
}

function orderAdd(){
	makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderacnt/OrderAcntQuery/orderAcntLargeAdd.json', showResult ,'fm');
}

function showResult(json){
	if(json.returnValue == '1'){
		window.location.href = '<%=request.getContextPath()%>/sales/ordermanage/orderacnt/OrderAcntQuery/OrderAcntQueryInit.do';
	}else{
		MyAlert("保存失败！");
	}
}
</script>
</body>
</html>
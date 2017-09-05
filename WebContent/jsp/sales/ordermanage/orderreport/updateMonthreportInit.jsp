<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/productCombofunc.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>月度常规订单新增</title>
<script type="text/javascript">
function doInit(){
	sumNO();
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单提报 >月度常规订单新增</div>
<form method="POST" name="fm" id="fm">
<table class="table_query" align=center width="95%">

	<tr>
		<td align="right" nowrap>提报月度：</td>
		<td width="43%" class="table_query_2Col_input" nowrap>
			${year }年${month }月
			<input type="hidden" id="orderYear" name="orderYear" value="${year }" />
			<input type="hidden" id="orderMonth" name="orderMonth" value="${month }" />
		</td>
		<td align="right" nowrap></td>
		<td align="left" nowrap></td>
		<td align="right" nowrap>业务范围：</td>
		<td class="table_query_2Col_input" nowrap>
			${areaName }<input type="hidden" id="areaId" name="areaId" value="${areaId }" />
		</td>
 <td id="_productControl_">
      	<script type="text/javascript">
      		if("${order_id}") {
      			productStart("<%=request.getContextPath()%>", '${productId}', true, true) ;
      		} else {
      			productStart("<%=request.getContextPath()%>", '', false, true) ;
      		}
      	</script>
      </td>
	</tr>
</table>

<table class="table_list" id="file">
	<tr class="table_list_th">
		<th>车系</th>
		<th>物料编号</th>
		<th>物料名称</th>
		<th>颜色</th>
		<th>数量</th>
		<th>操作</th>
	</tr>
	<c:if test="${dList!=null}">
		<c:forEach items="${dList}" var="list">
			<tr align="center" class="table_list_row2">
				<td>${list.SERIES_NAME}<input type="hidden" name="material_id" value="${list.MATERIAL_ID }" /></td>
				<td>${list.MATERIAL_CODE}</td>
				<td>${list.MATERIAL_NAME}</td>
				<td>
					${list.COLOR_NAME}
					<input type="hidden" id="detail_id${list.DETAIL_ID}" name="detail_id" value="${list.DETAIL_ID}" />
				</td>
				<td><input type="text" id="${list.MATERIAL_ID }" name="order_amount" class="SearchInput" datatype="0,is_digit,6"  size="2" maxlength="6" value="${list.ORDER_AMOUNT}" onchange="sumNO(this.value,${list.MATERIAL_ID });" /></td>
				<td><a href="#" onclick="doDelete(${list.DETAIL_ID},this);">[删除]</a></td>
			</tr>
		</c:forEach>
	</c:if>
	<tr align="center" class="table_list_row1">
		<td></td>
		<td></td>
		<td></td>
		<td align="center">
			总计：
		</td>
		<td><span id="allDetailNO"></span></td>
		<td></td>
	</tr>
</table>
<table class="table_query">
	<tr align="left">
		<td align="left">
			<input id="queryBtn" name="button2" type=button class="cssbutton" onClick="toAdd();" value="新增产品"> 
		</td> 
	</tr>
</table>
<br>
<table class="table_list">
	<c:if test="${null!=checkList}">
		<tr>
			<th align="left" colspan="7"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />审核信息：</th>
		</tr>
		<tr  class="table_list_row2" >
			<td width="12%" align="center" valign="top" nowrap="nowrap">
		      	审核类型
		    </td>
		    <td width="12%" align="center" valign="top" nowrap="nowrap">
		      	审核状态
		    </td>
		    <td width="12%" align="center" valign="top" nowrap="nowrap">
		      	审核时间
		    </td>
		    <td width="64%" align="center" valign="top" nowrap="nowrap">
		      	审核描述
		    </td>
		</tr>
		<c:forEach items="${checkList}" var="list">
			<tr class="table_list_row1" >
		      <td>
		      	<script>document.write(getItemValue(${list.CHECK_TYPE }));</script>
		      </td>
		       <td>
		      	<script>document.write(getItemValue(${list.CHECK_STATUS }));</script>
		      </td>
		      <td>${list.CHECK_DATE }</td>
		      <td>${list.CHECK_DESC}</td>
		    </tr>
		</c:forEach>
	</c:if>
	
	

    <tr class="cssTable" >
      <td  align="left" valign="top" nowrap="nowrap" colspan="7">
      	<input class='normal_btn' name="baocun2" type="button"  value="保存" onclick="saveOrSubmit('1');" />
        <input class='normal_btn' name="baocun" type="button"  value="提报" onclick="saveOrSubmit('2');" />
		<input name="shangbao" type="button" class="normal_btn" value="返回" onclick="history.back();" /></td>
      <td>
        <!-- 用户选择要删除的detailId -->
      	<input type="hidden" id="detailIds_delete" name="detailIds_delete" />
      	<!-- 订单id -->
      	<input type="hidden" id="orderId" name="orderId" value="${order_id }" />
      	<!-- 订单号 -->
      	<input type="hidden" id="order_no" name="order_no" value="${order_no }" />
      	<!-- 订单总数 -->
      	<input type="hidden" id="order_amonut" name="order_amonut" value="" />
      	<!--经销商id -->
      	<input type="hidden" id="dealerId" name="dealerId" value="${dealerId }" />
      </td>
    </tr>
</table>

</form>
<script type="text/javascript">
	function toAdd(){
		if(!_getTip_()) {
			return false ;
		}
		var areaId = document.getElementById("areaId").value;
		var ids = "";
		var ids_ = document.getElementsByName("material_id");
		for(var i=0;i<ids_.length;i++){
			ids += ids_[i].value + ",";
		}
		ids = (ids == "" ? ids : ids.substring(0,ids.length-1));
		
		var productId = "" ;
		
		if(document.getElementById("_productId_")) {
			productId = document.getElementById("_productId_").value ;
		}
		
		showMaterialByAreaId_Mini('','','false',areaId,ids,productId);
	}

	var detail_ids_delete = "";
	function doDelete(detail_id,obj){
		detail_ids_delete = detail_ids_delete + detail_id +","
		var idx = obj.parentElement.parentElement.rowIndex;
		var tbl = document.getElementById('file');
		tbl.deleteRow(idx);
		sumNO();
		_setSelDisabled_("file", 2) ;
	}
	function contentTrim(str){
		if(str){
			str = str.replace(/(^\s*)|(\s*$)/g, "");
			return str;
		}else{
			return "";
		} 
	}

	function saveOrSubmit(value){
		if(!_getTip_()) {
			return false ;
		}
		var numbers = document.getElementsByName("order_amount");
		for(var i=0;i<numbers.length;i++){
			if(!contentTrim(numbers[i].value)){
				MyAlert("数量不能为空，请重新输入!");
				return;
			}
		}
		var rowNum = document.getElementById("file").rows.length;
		if(rowNum+""== "2"){
			MyAlert("请选择产品!");
			return;
		}
		var allNO = document.getElementById("allDetailNO").innerHTML;
		if(!allNO || (allNO+"" == "0")){
			MyAlert("请输入数量信息!");
			return;
		}
		if(value+"" == "1"){
			MyConfirm("是否确认修改?",saveAction,[value]);
		}
		if(value+"" == "2"){
			document.getElementById("order_amonut").value = document.getElementById("allDetailNO").innerHTML;
			MyConfirm("是否确认提交?",saveAction,[value]);
		}
		
	}
	function saveAction(value){
		disableBtn($("baocun2"));
		disableBtn($("baocun"));
		document.getElementById("detailIds_delete").value = detail_ids_delete;
		$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/orderreport/MonthOrderReport/reportChangeSaveOrSubmit.do?operateType="+value;
		$('fm').submit();
	}

	function showMaterialInfo(material_id,materialName,materialCode){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/MonthOrderReport/showMaterialInfo.json?material_id='+material_id,addTblRow,'fm');
	}
	function addTblRow(json) {
		var group_name = json.materialInfo.GROUP_NAME;
		var material_code = json.materialInfo.MATERIAL_CODE;
		var material_name = json.materialInfo.MATERIAL_NAME;
		var color_name = json.materialInfo.COLOR_NAME;
 		var material_id = json.materialInfo.MATERIAL_ID;
		   
		var tbl = document.getElementById('file');
		var rowObj = tbl.insertRow(tbl.rows.length-1);
		rowObj.className  = "table_list_row2";
		var cell1 = rowObj.insertCell(0);
		var cell2 = rowObj.insertCell(1);
		var cell3 = rowObj.insertCell(2);
		var cell4 = rowObj.insertCell(3);
		var cell5 = rowObj.insertCell(4);
		var cell6 = rowObj.insertCell(5);
		cell1.innerHTML = "<TD align='center'><div align='center'>"+group_name+"</div><input type='hidden' name='material_id' value="+material_id+" /></TD>";
		cell2.innerHTML = "<TD align='center'><div align='center'>"+material_code+"</div></TD>";
		cell3.innerHTML = "<TD align='center'><div align='center'>"+material_name+"</div></TD>";
		cell4.innerHTML = "<TD align='center'><div align='center'>"+color_name+"<input type='hidden' name='detail_id' value=''></div></TD></TR>";
		cell5.innerHTML = "<TD align='center'><div align='center'><input type='text' id="+material_id+" name='order_amount' class='SearchInput' datatype='1,is_digit,6'  size='2' maxlength='6'  onchange='sumNO(this.value,"+material_id+");'/><font color='red'>*</font></div></TD></TR>";
		cell6.innerHTML = "<TD align='center'><div align='center'><a href='#' onclick='deleteTblRow(this);' >[删除]</a></div></TD></TR>";
		_setSelDisabled_("file", 2) ;
	}
	function deleteTblRow(obj) {
		var idx = obj.parentElement.parentElement.parentElement.rowIndex;
		var tbl = document.getElementById('file');
		tbl.deleteRow(idx);		
		sumNO();
	}
	function sumNO(value,material_id){
		if(value && material_id){
			var reg = /^\+?[1-9][0-9]*$/;
			if(!reg.test(value)){
				MyAlert("只能输入非0正整数!");
				document.getElementById(material_id).value = 1;
			}
		}
		
		
		var allDetailNOs = 0;
		var allDetailNO = document.getElementsByName("order_amount");
		for(var i=0;i<allDetailNO.length;i++){
			allDetailNOs = Number(allDetailNOs)+Number(allDetailNO[i].value);
		}
		document.getElementById("allDetailNO").innerHTML = allDetailNOs;
	}

</script>
</body>
</html>

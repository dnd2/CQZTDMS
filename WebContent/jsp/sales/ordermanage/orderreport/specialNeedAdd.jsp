<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/productCombofunc.js"></script>
<title>订单提报</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订做车需求提报 > 订做车需求新增</div>
<form method="POST" name="fm" id="fm">
	<table class="table_query" align="center">
		<tr class= "tabletitle">
			<td align = "right" width="10%">业务范围：</td>
			<td align = "left" >
				<select name="areaId" class="short_sel" onchange="getDealerAreaId(this.options[this.options.selectedIndex].value);">
					<c:forEach items="${areaList}" var="po">
						<option value="${po.AREA_ID}|${po.DEALER_ID}">${po.AREA_NAME}</option>
					</c:forEach>
				</select>
				<input type="hidden" name="area" id="area"/>
				<input type="hidden" name="dealerId" id="dealerId"/>
				<input type="hidden" name="lever" id="lever"/>
			</td>
			<td id="_productControl_">
      	<script type="text/javascript">
      		// productStart("<%=request.getContextPath()%>", '', false, true) ;
      	</script>
      </td>
		</tr>
	</table>
	<table class=table_list style="border-bottom:1px solid #DAE0EE" >  
		<tr class=cssTable >
			<th nowrap="nowrap">车系</th>
			<th nowrap="nowrap">车型编号</th>
			<th nowrap="nowrap">配置编号</th>
			<th nowrap="nowrap">配置名称</th>
			<th nowrap="nowrap">需求数量</th>
			<th nowrap="nowrap">操作</th>
		</tr>
		<tbody id="tbody1"></tbody>
	</table>	
	<table class="table_query">
		<tr class="cssTable" >
			<td width="100%" align="left">
				<input type="text" name="materialCode" size="15" id="materialCode" style="display:none"/>
				<input class="cssbutton" name="add22" type="button" onclick="materialShow();" value ='新增配置' />
				&nbsp;				
			</td>
		</tr>
	</table>
	<table class="table_info" border="0" id="file">
	    <tr>
	        <th>附件列表：<input type="hidden" id="fjids" name="fjids"/>
				<span>
					<input type="button" class="cssbutton"  onclick="showUpload('<%=request.getContextPath()%>')" value ='添加附件'/>
				</span>
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
	</table>
	<p>&nbsp;</p>
	<table class=table_query>
		<tr>
			<th colspan="2" align="left"  nowrap="nowrap"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 改装需求说明
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
			<th align="right"   nowrap="nowrap">&nbsp;</th>
		</tr>
		<tr class="cssTable">
	      <td align="right" nowrap="nowrap">集团客户：</td>
	      <td align="left" nowrap="nowrap" colspan="3">
		      <input id="fleetName" name="fleetName" type="text" class="long_txt" readonly="readonly" />
			  <input id="fleetId" name="fleetId" type="hidden"/>				
			  <input class="mini_btn" type="button" value="..." onclick="showFleet();"/>
			  <input class="cssbutton" type="button" value="清除" onclick="toClear();"/>
		  </td>
	    </tr>
		<tr class=cssTable>
			<td width="7%" align="right">改装说明：</td>
			<td width="50%" colspan="3" align="left"  nowrap>
				<textarea name="remark" id="remark" rows="4" cols="50"></textarea>
				<font color="red">*</font>
			</td>
		</tr>
		<tr class=cssTable >
			<td>&nbsp;</td>
			<td colspan="3" align="left">
				<input type="hidden" name="materialIds" id="materialIds"/>
				<input type="hidden" name="amounts" id="amounts"/>
				<input type="button" name="button1" class="cssbutton" onclick="confirmAdd(1)" value="保存" id="queryBtn1" />
				<input type="button" name="button1" class="cssbutton" onclick="confirmAdd(2)" value="提报" id="queryBtn3" /> 
				<input type="button" name="button2" class="cssbutton" onclick="javascript:history.go(-1)" value="返回" id="queryBtn2" /> 
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	//页面初始化
	function doInit(){
		getDealerAreaId(document.getElementById("areaId").value);
	}
	
	//设置业务范围灰显
	function disableArea(){
		var rowsnum = document.getElementById("tbody1").rows.length;
		if(rowsnum != 0){
			document.getElementById('areaId').disabled = "disabled";
		}
		else{
			document.getElementById('areaId').disabled = "";
		}
	}
	//设置业务范围ID,经销商ID
	function getDealerAreaId(arg){
		var areaObj = document.getElementById("areaId");
		var areaId = areaObj.value.split("|")[0];
		var dealerId = areaObj.value.split("|")[1];
		document.getElementById("area").value = areaId;
		document.getElementById("dealerId").value = dealerId;
		// var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedReport/viewDelaerLever.json";
		// makeCall(url,viewDealerLever,{dealerId:dealerId}); 
	}	

	//查询经销商等级
	
	function viewDealerLever(json){
		$('lever').value=json.boo;
		if(json.boo){
			$('queryBtn3').disabled=true;
		}

	}
	//新增配置链接
	function addMaterial(){		
		var materialCode = document.getElementById("materialCode").value;
		/* var materialIds = document.getElementsByName('materialId');
		var addedMaterialId = "";
		for(var i=0; i<materialIds.length; i++) {
			if(i != materialIds.length-1) {
				addedMaterialId += materialIds[i].value + ",";
			} else {
				addedMaterialId += materialIds[i].value;
			}
		} */
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedReport/addMaterial.json";
		makeCall(url,addRow,{materialCode:materialCode/* , addedMaterialId:addedMaterialId */}); 
	}
	//删除配置链接
	function delMaterial(){	
	  	document.getElementById("tbody1").deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex - 1);  
	  	disableArea();
	  	_setSelDisabled_("tbody1", 0) ;
	}
	//新增配置列表
	function addRow(json){
		for(var i=0; i<json.info.length; i++) {
			var timeValue = new Date().getTime();
			//判断配置是否重复
			var isDuplicate = false;
			var materialIds = document.getElementsByName('materialId');
			for(var k=0; k<materialIds.length; k++) {
				if(materialIds[k].value == json.info[i].GROUP_ID) {
					isDuplicate = true;
				}
			}
			if(isDuplicate) {
				MyAlert(json.info[i].GROUP_NAME+"配置已存在!");
			} else {
				var newRow = document.getElementById("tbody1").insertRow(i);
				newRow.className  = "table_list_row2";
				var newCell = newRow.insertCell(0);
				newCell.align = "center";
				newCell.innerHTML = json.info[i].SERIES_CODE;
				newCell = newRow.insertCell(1);
				newCell.align = "center";
				newCell.innerHTML = json.info[i].MODEL_CODE;
				newCell = newRow.insertCell(2);
				newCell.align = "center";
				newCell.innerHTML = json.info[i].GROUP_CODE;
				newCell = newRow.insertCell(3);
				newCell.align = "center";
				newCell.innerHTML = json.info[i].GROUP_NAME;
				newCell = newRow.insertCell(4);
				newCell.align = "center";
				newCell.innerHTML = "<input id='amount"+timeValue+"' name='amount' type='text' datatype='1,is_digit,6' class='SearchInput' value='0' size='2' maxlength='6'/>";
				newCell = newRow.insertCell(5);
				newCell.align = "center";
				newCell.innerHTML = "<a href='#' onclick='delMaterial();'>[删除]</a><input type='hidden' id='materialId"+timeValue+"' name='materialId' value='"+json.info[i].GROUP_ID+"'>";
			}
		}
		disableArea();
		_setSelDisabled_("tbody1", 0) ;
	}
	//物料弹出选择
	function materialShow(){
		/* if(!_getTip_()) {
			return false ;
		}
		 var ids = "";
		var myForm = document.getElementById("fm");
		for (var i=0; i<myForm.length; i++){  
			var obj = myForm.elements[i];
			if(obj.id.length>=10 && obj.id.substring(0,10)=="materialId"){
				if(ids&&ids.length>0)
					ids = ids +","+ obj.value;
				else
					ids += obj.value;
			}   
		} 	
		var areaObj = document.getElementById("area");
		var areaId = areaObj.value;	
		
		var productId = "" ;
		
		if(document.getElementById("_productId_")) {
			productId = document.getElementById("_productId_").value ;
		}  */
		var areaObj = document.getElementById("area");
		var areaId = areaObj.value;	
		showMaterialGroupByConf('materialCode','','true', '4' , areaId);
		<%-- showMaterialByAreaIdAndOrderType('materialCode','','false',areaId,ids.substring(0,ids.length-1), '<%=Constant.ORDER_TYPE_03%>', productId); --%>
	}
	//保存校验
	function confirmAdd(val){
		if(!_getTip_()) {
			return false ;
		}
		var amounts = '';
		var materialIds ='';
		var remark = document.getElementById("remark").value;
		var amount = document.getElementsByName("amount");
		var materialId = document.getElementsByName("materialId");
		var rowsnum = document.getElementById("tbody1").rows.length;
		if(rowsnum == 0){
			MyAlert("请至少添加一项产品！");
			return false;
		}
		for(var i=0 ;i< materialId.length; i++){
			if(!amount[i].value){
				MyAlert("请填写需求数量！");
				return false;
			}
			if(amount[i].value<=0){
				MyAlert("需求数量不小于零，请重新填写！");
				return false;
			}
			amounts = amount[i].value + ',' + amounts;
			materialIds = materialId[i].value + ',' + materialIds;
		}
		document.getElementById("amounts").value=amounts;
		document.getElementById("materialIds").value=materialIds;
		if(!remark){
			MyAlert("请填写改装说明！");
			return false;
		}
		if(val == 1)
		{
			MyConfirm("确认保存？",toAdd, [val]);
		}
		else
		{
			MyConfirm("确认提报？",toAdd, [val]);
		}
		
	}
	//保存提交
	function toAdd(val){
		//新增功能: 要求新增,修改时的提报和保存时均需要使得操作按钮变为不可用  2011-12-30 HXY
		document.getElementById('queryBtn1').disabled = true;
		document.getElementById('queryBtn3').disabled = true;
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedReport/specialNeedAdd.json?subFlag='+val,showResult,'fm');
	}
	//返回
	function toBack(){
		$('fm').action='<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedReport/specialNeedReportInit.do?&flag=1';
		$('fm').submit();
	}
	//回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("保存成功！");
			$('fm').action='<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedReport/specialNeedReportInit.do?&flag=1';
			$('fm').submit();
		}else if(json.returnValue == '2'){
			window.parent.MyAlert("提报成功！");
			$('fm').action='<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedReport/specialNeedReportInit.do?&flag=1';
			$('fm').submit();
		}else{
			MyAlert("新增失败！请联系系统管理员！");
		}
	}
	//清除按钮
	function toClear(){
		document.getElementById("fleetName").value = "";
		document.getElementById("fleetId").value = "";
	}
	
	//大用户弹出
	function showFleet(){
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedConfirm/queryFleetInit.do',700,500);
	}
</script>
</body>
</html>

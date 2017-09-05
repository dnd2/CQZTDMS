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
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订做车需求提报 > 订做车需求修改</div>
<form method="POST" name="fm" id="fm">
	<table class="table_query" align="center">
		<tr class= "tabletitle">
			<td align = "right" >业务范围：</td>
			<td align = "left" >
				<select name="areaId" class="short_sel" onchange="getDealerAreaId(this.options[this.options.selectedIndex].value);">
					<c:forEach items="${areaList}" var="po">
						<option <c:if test="${po.AREA_ID==areaId}">selected</c:if> value="${po.AREA_ID}|${po.DEALER_ID}">${po.AREA_NAME}</option>
					</c:forEach>
				</select>
				<input type="hidden" name="area" id="area"/>
				<input type="hidden" name="dealerId" id="dealerId"/>
				<input type="hidden" name="reqId" id="reqId" value="${reqId}"/>
				<input type="hidden" name="ver" id="ver" value="${ver}"/>
				<input type="hidden" id="reqStatus" name="reqStatus" value="${reqStatus}" />
			</td>
			<td id="_productControl_">
      	<script type="text/javascript">
      		// productStart("<%=request.getContextPath()%>", '${productId}', true, true) ;
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
		<tbody id="tbody1">
			<%int index = 0;%>
	    	<c:forEach items="${list}" var="po">
	    		<%index++;%>
	    		<tr class="table_list_row2">
			      <td align="center">${po.SERIES_NAME}</td>
			      <td align="center">${po.MODEL_CODE}</td>
			      <td align="center">${po.GROUP_CODE}</td>
			      <td align="center">${po.GROUP_NAME}</td>
			      <td align="center"><input id='amount<%=index%>' name='amount' type='text' datatype='1,is_digit,6' class='SearchInput' value='${po.AMOUNT}' size='2' maxlength='6'></td>
			      <td align="center"><a href='#' onclick='delMaterial();'>[删除]</a><input type='hidden' id='materialId<%=index%>' name='materialId' value='${po.GROUP_ID}'></td>
			    </tr>
	    	</c:forEach>
		</tbody>
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
	<table id="attachTab" class="table_info">
		<c:if test="${attachList!=null}">
	  		<c:forEach items="${attachList}" var="attls">
			    <tr class="table_list_row1" id="${attls.FJID}">
			    <td><a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a></td>
			    <td><input type=button onclick="delAttach('${attls.FJID}')" class="normal_btn" value="删 除"/></td>
			    </tr>
			</c:forEach>
		</c:if>
	</table>
	<br>
	<table class="table_list" style="border-bottom:1px solid #DAE0EE" >
		<tr class="table_list_row1">
			<th  align="center" nowrap="nowrap" >日期</th>
			<th align="center" nowrap="nowrap"  >单位</th>
			<th align="center" nowrap="nowrap"  >操作人</th>
			<th align="center" nowrap="nowrap"  >审核结果</th>
			<th align="center" nowrap="nowrap"  >审核描述</th>
		</tr>
		<c:forEach items="${checkList}" var="po">
			<tr class="table_list_row1">
				<td align="center" nowrap="nowrap" class="table_list_row1" >${po.CHECK_DATE}</td>
				<td align="center" nowrap="nowrap" class="table_list_row1"  >${po.ORG_NAME}</td>
				<td align="center" nowrap="nowrap" class="table_list_row1"  >${po.NAME}</td>
				<td align="center" nowrap="nowrap" class="table_list_row1"  >${po.CHECK_STATUS}</td>
				<td align="center" nowrap="nowrap"  >${po.CHECK_DESC}</td>
			</tr>
		</c:forEach>
	</table>
	<br>
	<table class=table_query>
		<tr>
			<th colspan="2" align="left"  nowrap="nowrap"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 改装需求说明
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
			<th align="right"   nowrap="nowrap">&nbsp;</th>
		</tr>
		<tr class="cssTable">
	      <td align="right" nowrap="nowrap">集团客户：</td>
	      <td align="left" nowrap="nowrap" colspan="3">
		      <input id="fleetName" name="fleetName" class="long_txt" type="text" value="${fleetName}" readonly="readonly" />
			  <input id="fleetId" name="fleetId" type="hidden" value="${fleetId}"/>				
			  <input class="mini_btn" type="button" value="..." onclick="showFleet();"/>
			  <input class="cssbutton" type="button" value="清除" onclick="toClear();"/>
		  </td>
	    </tr>
		<tr class=cssTable>
			<td width="7%" align="right">改装说明：</td>
			<td width="50%" colspan="3" align="left"  nowrap><textarea name="remark" id="remark" rows="4" cols="50"><c:out value="${remark}"/></textarea><font color="red">*</font></td>
		</tr>
		<tr class=cssTable >
			<td>&nbsp;</td>
			<td colspan="3" align="left">
				<input type="hidden" name="materialIds" id="materialIds"/>
				<input type="hidden" name="amounts" id="amounts"/>
				<input type="button" name="button1" class="cssbutton" onclick="confirmAdd();" value="保存" id="queryBtn1" /> 
				<input type="button" name="button2" class="cssbutton" onclick="toBack();" value="返回" id="queryBtn2" /> 
				<!-- 要删除的附件id -->
				<input type="hidden" id="delFileIds" name="delFileIds" value="" />
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	//页面初始化
	function doInit(){
		getDealerAreaId(document.getElementById("areaId").value);
		disableArea();
	}
	function delAttach(value){
		var delFileIds = document.getElementById("delFileIds").value;
		document.getElementById("delFileIds").value = delFileIds+","+value;
		document.getElementById(value).style.display="none";
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
	}	
	//新增产品链接
	function addMaterial(){		
		var materialCode = document.getElementById("materialCode").value;
		var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedReport/addMaterial.json";
		makeCall(url,addRow,{materialCode:materialCode}); 
	}
	//删除产品链接
	function delMaterial(){	
	  	document.getElementById("tbody1").deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex - 1);  
	  	disableArea();
	  	_setSelDisabled_("tbody1", 0) ;
	}
	//新增产品列表
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
				var newRow = document.getElementById("tbody1").insertRow();
				newRow.className = "table_list_row2";
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
		}
		 */
		 var areaObj = document.getElementById("area");
		 var areaId = areaObj.value;	
		 showMaterialGroupByConf('materialCode','','true', '4' , areaId);
		/* showMaterialByAreaId('materialCode','','false',areaId,ids.substring(0,ids.length-1),productId); */
	}
	//保存校验
	function confirmAdd(){
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
		MyConfirm("确认保存？",toAdd);
	}
	//保存提交
	function toAdd(){
		//新增功能: 要求新增,修改时的提报和保存时均需要使得操作按钮变为不可用  2011-12-30 HXY
		document.getElementById('queryBtn1').disabled = true;
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedReport/specialNeedModify.json',showResult,'fm');
	}
	//返回
	function toBack(){
		$('fm').action='<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedReport/specialNeedReportInit.do';
		$('fm').submit();
	}
	//
	function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("保存成功！");
			$('fm').action='<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedReport/specialNeedReportInit.do';
			$('fm').submit();
		}else if(json.returnValue == '2'){
			window.parent.MyAlert("该数据已被修改，保存失败！");
			$('fm').action='<%=request.getContextPath()%>/sales/ordermanage/orderreport/SpecialNeedReport/specialNeedReportInit.do';
			$('fm').submit();
		}else{
			MyAlert("保存失败！请联系系统管理员！");
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

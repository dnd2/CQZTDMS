<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
Map<Object,List<Map<String, Object>>> mapc =(Map<Object,List<Map<String, Object>>>)request.getAttribute("boByVeMap");
List<Map<String, Object>>  list =(List<Map<String, Object>>)request.getAttribute("list");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 配车调整 </title>
</head>
<body>
<form name="fm" method="post" id="fm">
<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;" id="subtab1">
<tr class="table_list_row2">
	<td noWrap align=right>组板号:</td>
	<td  align=center width="15%">${sendBoardMap.BO_NO }</td>
	<td noWrap align=right>承运车牌号:</td>
	<td align=center width="15%">${sendBoardMap.CAR_NO }</td>
	<td noWrap align=right>装车道次:</td>
	<td align=center width="15%">${sendBoardMap.LOADS }</td>
	<td noWrap align=right>领票车队:</td>
	<td align=center width="15%">${sendBoardMap.CAR_TEAM }</td>
</tr>
</table>
<div style="
	overflow-x:scroll;
	overflow-y:scroll;
	border:solid 1px #C2C2C2;
	scrollbar-3dlight-color:#595959;
	scrollbar-arrow-color:#CCCCCC;
	scrollbar-base-color:#CFCFCF;
	scrollbar-darkshadow-color:#FFFFFF;
	scrollbar-face-color:#F3F4F8;
	scrollbar-highlight-color:#FFFFFF;
	scrollbar-shadow-color:#595959;" id=detailDiv>
<table class=table_list width="100%">
<tbody>
<tr class=cssTable>
<th noWrap align=middle><font size="4">操作</font></th>
<th noWrap align=middle><font size="4"><b>车系</b></font></th>
<th noWrap align=middle><font size="4"><b>发运申请号</b></font></th>
<!-- <th noWrap align=middle><font size="4">是否中转</font></th> -->
<th align=middle><font size="4"><b>车系</b></font></th>
<th align=middle><font size="4"><b>车型</b></font></th>
<th align=middle><font size="4"><b>配置</b></font></th>
<th noWrap align=middle><font size="4"><b>颜色</b></font></th>
<th noWrap align=middle><font size="4"><b>物料代码</b></font></th>
<th noWrap align=middle><font size="4"><b>本次组板数量</b></font></th>
<th noWrap align=middle><font size="4"><b>配车数量</b></font></th>

</tr>
<%
if(list!=null)	{
	for(int bo=0;bo<list.size();bo++){
		Map<String, Object> boMap=(Map<String, Object>)list.get(bo);
		
%>
<tr class=table_list_row2>
<%
if(Integer.parseInt(boMap.get("THIS_BOARD_NUM").toString())>0){
%>
	<td><img id="img_<%=boMap.get("BO_DE_ID") %>" onclick="showPart('<%=boMap.get("BO_DE_ID") %>',this)" alt="配车列表" src="<%=contextPath %>/img/nolines_plus.gif" /></td>	
	
<% 
}else{
%>
	<td><img  alt="配车列表" src="<%=contextPath %>/img/nolines_minus.gif" /></td>	
<%
}
%>
<td><font size="3"><%=boMap.get("SERIES_NAME") %></font></td><!-- 车系名称 -->
<%
if(boMap.get("IS_RETAIL")!=null && boMap.get("IS_RETAIL").toString().equals(Constant.IS_DEL_01)){
%>
<td><font size="3"><a href="javascript:void(0);" style="color: red;" onclick="getOrderInfoString(<%=boMap.get("ORDER_ID") %>)"><%=boMap.get("ORDER_NO") %>(零售)</a></font></td><!-- 批售单号 -->
<%
	}else{
%>
<td><font size="3"><a href="javascript:void(0);" onclick="getOrderInfoString(<%=boMap.get("ORDER_ID") %>)"><%=boMap.get("ORDER_NO") %></a></font></td><!-- 批售单号 -->
<%} %>
<%-- <%
if(boMap.get("ORDER_TYPE")!=null && boMap.get("ORDER_TYPE").toString().equals(Constant.ORDER_TYPE_04.toString())){
%>
<td><font size="3">是</font></td><!-- 是否中转-->
<%}else{ %>
<td><font size="3">否</font></td><!-- 是否中转-->
<%} %> --%>
<td><font size="3"><%=boMap.get("SERIES_NAME") %></font></td>
<td><font size="3"><%=boMap.get("MODEL_NAME") %></font></td>
<td><font size="3"><%=boMap.get("PACKAGE_NAME") %></font></td>
<td><font size="3"><%=boMap.get("COLOR_NAME") %></font></td><!-- 颜色 -->
<td><font size="3"><%=boMap.get("MATERIAL_CODE") %></font></td><!-- 物料CODE-->
<td><font size="3"  id='thisNum_<%=boMap.get("BO_DE_ID") %>'><%=boMap.get("THIS_BOARD_NUM") %></font></td><!-- 当前组板号组板数  -->
<td><font size="3"  id='aNum_<%=boMap.get("BO_DE_ID") %>'><%=boMap.get("ALLOCA_NUM") %></font></td>
<input type='hidden'  name='HIDDEN_THIS_BOARD_NUM' value="<%=boMap.get("THIS_BOARD_NUM") %>" />
<input type='hidden' id="HIDDEN_<%=boMap.get("BO_DE_ID") %>"  name='HIDDEN_ALLOCA_NUM' value="<%=boMap.get("ALLOCA_NUM") %>" /><!-- 当前组板号配车数  -->
</tr>
<tr class=table_list_row1 style="display: none" id='<%=boMap.get("BO_DE_ID") %>'>
<td></td><td colspan="11">
<table  class=table_listPC width="100%">
<%
	if(!mapc.isEmpty()){
		List<Map<String, Object>> orgList=(List<Map<String, Object>>)mapc.get("ORG_"+boMap.get("BO_DE_ID"));
		List<Map<String, Object>> vehicleList=(List<Map<String, Object>>)mapc.get(boMap.get("BO_DE_ID"));
		if(orgList!=null){
			for(int ve=0;ve<orgList.size();ve++){
				Map<String,Object> orgMap=(Map<String,Object>)orgList.get(ve);
				%>
				<tr  class=table_list_row6>
				<td class="right">资源预留：</td><td align="left"><%=orgMap.get("AMOUNT") %></td>
				<td class="right">已配资源：</td><td align="left" id="aNum~<%=orgMap.get("ORG_ID") %>_<%=boMap.get("BO_DE_ID") %>"><%=orgMap.get("ALLOCA_NUM") %></td>
				<td class="right">最大可配资源：</td><td align="left" id="maxNum~<%=orgMap.get("ORG_ID") %>_<%=boMap.get("BO_DE_ID") %>"><%=orgMap.get("CAN_COUNT") %></td>
				<td align="left">
				</td>
				</tr>
				<tr>
				<td colspan="9">
					<table class="table_list" id="pcTable~<%=orgMap.get("ORG_ID") %>_<%=boMap.get("BO_DE_ID") %>">
						<tr>
						<th></th>
						<th>序号</th>
<!--						<th>流水号</th>-->
						<th>VIN</th>
						<th>发动机号</th>
						<th>入库日期</th>
						<th>库区</th>
						<th>库道</th>
						<th>库位</th>
						<th>生命周期</th>
						</tr>
		<%		
		if(vehicleList!=null){
			for(int ve1=0;ve1<vehicleList.size();ve1++){
				Map<String,Object> vehicleMap=(Map<String,Object>)vehicleList.get(ve1);
				%>
				<input type='hidden' name='OLD_DETAIL_IDS' value='<%=vehicleMap.get("DETAIL_ID") %>'/><!-- 配车星系信息表 -->
				<input type='hidden' name='VEHICLE_IDS' value='<%=vehicleMap.get("VEHICLE_ID") %>'/><!-- 旧的车辆信息IDs -->
				<input type='hidden' name='BO_DE_IDS' value='<%=boMap.get("BO_DE_ID") %>'/><!-- 组板明细IDs -->	
				<input type="hidden" name="RE_DEALER_IDS" value="<%=boMap.get("RE_DEALER_ID") %>"/><!-- 收货方ID-->
				<input type="hidden" name="ORDER_TYPES" value="<%=boMap.get("ORDER_TYPE") %>"/><!-- 订单类型-->
				<tr>
					<td><input type='checkbox'   id='groupIds' name='groupIds_<%=boMap.get("BO_DE_ID") %>' onclick='selectOnly(this,<%=vehicleMap.get("DETAIL_ID") %>)'/>
					</td><!-- 车辆ID -->
					<td><%=ve1+1 %></td><!-- 序号 -->
<!--					<td><%=vehicleMap.get("SD_NUMBER")==null?"":vehicleMap.get("SD_NUMBER") %></td> 流水号 -->
					<td><%=vehicleMap.get("VIN") %></td><!-- VIN -->
					<td><%=vehicleMap.get("ENGINE_NO") %></td><!-- 发动机号 -->
					<td><%=vehicleMap.get("ORG_STORAGE_DATE") %></td><!-- 入库日期 -->
					<td><%=vehicleMap.get("AREA_NAME") %></td><!-- 库区 -->
					<td><%=vehicleMap.get("ROAD_NAME") %></td><!-- 库道 -->
					<td><%=vehicleMap.get("SIT_NAME") %></td><!-- 库位 -->
					<td><script type="text/javascript">
								document.write(getItemValue(<%=vehicleMap.get("LIFE_CYCLE") %>));
							</script>&nbsp;</td><!-- 生命周期 -->
				</tr>
				<tr class=table_list_row1 style="display: none" id="edit_<%=vehicleMap.get("DETAIL_ID") %>">
				  <td>&nbsp;</td>
				  <td>&nbsp;</td>
				<td colspan="9"><table class=table_list width="100%" >
				<tr class=cssTable>
				<th width="23%" align=middle noWrap>新VIN</th> 
<!--				<th width="23%" align=middle noWrap>流水号</th> -->
				<th width="23%" align=middle noWrap>发动机号</th>
				<th width="17%" align=middle>入库日期</th>
				<th width="15%" align=middle noWrap>库区</th>
				<th width="13%" align=middle noWrap>库道</th>
				<th width="9%" align=middle noWrap>库位</th>
				</tr>
				<tr bgcolor="#CCFF99" class=table_list_row1>
				  <td align="left">
				  	<input type="hidden"  name="NEW_VEHICLE_IDS" class="middle_txt" size="15" value=""  id="NEW_VEHICLE_IDS_<%=vehicleMap.get("DETAIL_ID") %>"/>
					<input type="text" maxlength="20"  readonly="readonly"  name="vin" class="middle_txt" size="15" value="<%=vehicleMap.get("VIN") %>" id="vin_<%=vehicleMap.get("DETAIL_ID") %>"/>
					<input name="button2" type="button" class="mini_btn" onclick="chooseVehicleFlex(<%=boMap.get("MATERIAL_ID") %>,<%=boMap.get("BO_DE_ID") %>,<%=vehicleMap.get("DETAIL_ID") %>,<%=vehicleMap.get("VEHICLE_ID") %>,<%=boMap.get("YELID") %>,'<%=vehicleMap.get("SPECIAL_ORDER_NO") %>');" value="..." />
				  </td>
<!--				<td id="td6_<%=vehicleMap.get("DETAIL_ID") %>"><%=vehicleMap.get("SD_NUMBER")==null?"":vehicleMap.get("SD_NUMBER") %></td>-->
				<td id="td1_<%=vehicleMap.get("DETAIL_ID") %>"><%=vehicleMap.get("ENGINE_NO") %></td>
				<td id="td2_<%=vehicleMap.get("DETAIL_ID") %>"><%=vehicleMap.get("ORG_STORAGE_DATE") %></td>
				<td id="td3_<%=vehicleMap.get("DETAIL_ID") %>"><%=vehicleMap.get("AREA_NAME") %></td>
				<td id="td4_<%=vehicleMap.get("DETAIL_ID") %>"><%=vehicleMap.get("ROAD_NAME") %></td> 
				<td id="td5_<%=vehicleMap.get("DETAIL_ID") %>"><%=vehicleMap.get("SIT_NAME") %></td>
				</tr></table></td>
				</tr>
					<%
						}
					}	
					%>
					</table>
					</td>
				</tr>
				<%
			}
		}	
	}
%>
</table>
</td>
</tr>  
<%
  }
}
%>
<tr class=table_list_row1>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td></td>
<td><h2><font size="3"  >合计：</font></h2></td>
<td><h2><font size="3"  id='SHOW_THIS_BOARD_NUM'></font></h2></td>
<td><h2><font size="3" id="SHOW_ALLOCA_NUM"></font ></h2></td>
</tr></tbody></table>
</div>
<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;"  id="subtab1">
	<tr class="table_list_row2">
		<td align="center" colspan="8">
			<input type="button" name="button1" class="normal_btn" id="saveButton" onclick="save();" value="换车完成" /> 
			<input type="button" name="button1" class="normal_btn" id="goBack" onclick="back();" value="返回" /> 
		</td>
	</tr>
</table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var changeType="only";//判断能否多选（单选）
	var delID;//配车明细信息ID
	function doInit(){
		var arrayObj2 = new Array(); 
		var thisBoardSum=0;//当前组板总数
		arrayObj2=document.getElementsByName("HIDDEN_THIS_BOARD_NUM");//当前组板总数
		for(var i=0;i<arrayObj2.length;i++){
			if(arrayObj2[i].value!=""){
				thisBoardSum+=arrayObj2[i].value*1;
			}
		}
		document.getElementById("SHOW_THIS_BOARD_NUM").innerHTML=thisBoardSum;
		var arrayObj3 = new Array(); 
		 var thisAllocaNum=0;//当前配车总数
		arrayObj3=document.getElementsByName("HIDDEN_ALLOCA_NUM");//当前配车数组
		for(var i=0;i<arrayObj3.length;i++){
			if(arrayObj3[i].value!=""){
				thisAllocaNum+=arrayObj3[i].value*1;
			}
		}
		document.getElementById("SHOW_ALLOCA_NUM").innerHTML=thisAllocaNum;
	}
	function changeRowVehicle(arrayAllObj,boDeId,delId){
		var vehicle_id_="NEW_VEHICLE_IDS_"+delId;
		var vehicle_id_delId=document.getElementById(vehicle_id_);
		var vin_="vin_"+delId;
		var vin_delId=document.getElementById(vin_);
		var td1_="td1_"+delId;//发动机号
		var td1_obj=document.getElementById(td1_);
		var td2_="td2_"+delId;//入库日期
		var td2_obj=document.getElementById(td2_);
		var td3_="td3_"+delId;//库区
		var td3_obj=document.getElementById(td3_);
		var td4_="td4_"+delId;//库位
		var td4_obj=document.getElementById(td4_);
		var td5_="td5_"+delId;//库道
		var td5_obj=document.getElementById(td5_);	
		//var td6_="td6_"+delId;//流水号
		//var td6_obj=document.getElementById(td6_);	
		if(arrayAllObj!=null && arrayAllObj.length>0){	
				for(var i=0;i<arrayAllObj.length;i++){
					var arr=arrayAllObj[i];
						vehicle_id_delId.value=arr[0];// 	车辆ID
						vin_delId.value=arr[1];// 	vin
						td1_obj.innerHTML=arr[2];// 发动机号
						td2_obj.innerHTML=arr[3];//<!-- 入库日期 -->
						td3_obj.innerHTML=arr[4];//<!-- 库区 -->
						td4_obj.innerHTML=arr[5];//<!-- 库道-->
						td5_obj.innerHTML=arr[6];//<!-- 库位 -->td6_obj.innerHTML=arr[7]==null?"":arr[7];//<!-- 流水号-->
					}
			}
	}
	function showPart(va,tva){
		var trId=document.getElementById(va);
		if(trId.style.display==""){
			tva.src="<%=contextPath %>/img/nolines_plus.gif";//隐藏变加号图标
			trId.style.display="none";	
		}else{//显示时候 添加动态表
			//改变图标 Start
			tva.src="<%=contextPath %>/img/nolines_minus.gif";//显示变减号图标
			trId.style.display="";
		}
	}
	//选择一行的CHECKBOX
	function selectOnly(va,bo) { 
		if(va.checked==true){
			document.getElementById("edit_"+bo).style.display="";
		}else{
			document.getElementById("edit_"+bo).style.display="none";
		}
	}
	//配车完成
	function save(){
		MyConfirm("确认换车?",saveAlloca);	
	}
	function saveAlloca(){
		disabledButton(["saveButton","goBack"],true);
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/AllocaAdjust/allocaAdjustMain.json",saveAllocaBack,'fm','queryBtn'); 
	}
	function saveAllocaBack(json)
	{
		if(json.returnValue == 1)
		{
			if(json.infoMsg!=""){
				parent.MyAlert("操作成功！以下车辆已占用："+json.infoMsg);
			}else{
				parent.MyAlert("操作成功！");
			}
			fm.action = "<%=contextPath%>/sales/storage/sendmanage/AllocaAdjust/allocaAdjustInit.do";
			fm.submit();
		}
		else if(json.returnValue == 2)
		{
			parent.MyAlert("保存成功，无更换任何车辆！");
			fm.action = "<%=contextPath%>/sales/storage/sendmanage/AllocaAdjust/allocaAdjustInit.do";
			fm.submit();
		}
		else
		{
			disabledButton(["saveButton","goBack"],false);
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	//选择车辆JS
	function chooseVehicle(materialId,boDeId,delId){
		delID=delId;//设置配车信息ID
		 window.open('<%=request.getContextPath()%>/sales/storage/sendmanage/AllocaManage/picRecInit.do?MATERIAL_ID='+materialId+'&boDeId='+boDeId) ;
	}
	//选择车辆flex
	function chooseVehicleFlex(materialId,boDeId,delId,vehicleId,yelId,specialOrderNo){
		delID=delId;//设置配车信息ID
		var strFeatures = "left=0,screenX=0,top=0,screenY=0";  
	      if (window.screen){
	          //获取屏幕的分辨率
	           var maxh = screen.availHeight-30;
	           var maxw = screen.availWidth-10;
	           strFeatures += ",height="+maxh;
	           strFeatures += "innerHeight"+maxh;
	           strFeatures += ",width="+maxw;
	           strFeatures += "innerwidth"+maxw;
	      }else{
	          strFeatures +=",resizable";    
	      }
		  var specialOrderNumber = "'" + specialOrderNo + "'";
		  
	      getCheckVehicle();
		 window.open('<%=request.getContextPath()%>/sales/storage/storagemanage/VehiclePicture/allocaAdjPictureFlex.do?MATERIAL_ID='+materialId+'&boDeId='+boDeId+'&yelId='+yelId+'&specialOrderNo='+specialOrderNumber,'图形配车',strFeatures) ;
	}
	var checkVehicleObj;//选中的车
	//获取当前明细选中的配车
	function getCheckVehicle(){
		checkVehicleObj=new Array();
		var veIds=document.getElementsByName("NEW_VEHICLE_IDS");
		for(var i=0;i<veIds.length;i++){
			checkVehicleObj.push(veIds[i].value);//不需要判断那个明细，所有车辆都要拿过去
		}
	}
	function back(){
		fm.action = "<%=contextPath%>/sales/storage/sendmanage/AllocaAdjust/allocaAdjustInit.do";
		fm.submit();
	}
	function getOrderInfoString(value) {
		var url = '<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionOrderQuery/showOrderReport.do?orderId=' + value;
		OpenHtmlWindow(url,1000,450);
	}
</script>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
Map<Object,List<Map<String, Object>>> mapc =(Map<Object,List<Map<String, Object>>>)request.getAttribute("boByVeMap");

List<Map<String, Object>>  list =(List<Map<String, Object>> )request.getAttribute("list");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 配车 </title>
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
<th noWrap align=middle><font size="4">车系</font></th>
<th noWrap align=middle><font size="4">发运申请号</font></th>
<th noWrap align=middle><font size="4">是否中转</font></th>
<th noWrap align=middle><font size="4">车系</font></th>
<th noWrap align=middle><font size="4">车型</font></th>
<th noWrap align=middle><font size="4">配置</font></th>
<th noWrap align=middle><font size="4">颜色</font></th>
<th noWrap align=middle><font size="4">物料代码</font></th>
<th noWrap align=middle><font size="4">本次组板数量</font></th>
<th noWrap align=middle><font size="4">配车数量</font></th>

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
<%
if(boMap.get("ORDER_TYPE")!=null && boMap.get("ORDER_TYPE").toString().equals(Constant.ORDER_TYPE_04.toString())){
%>
<td><font size="3">是</font></td><!-- 是否中转-->
<%}else{ %>
<td><font size="3">否</font></td><!-- 是否中转-->
<%} %>
<td><font size="3"><%=boMap.get("SERIES_NAME") %></font></td><!-- 车系-->
<td><font size="3"><%=boMap.get("MODEL_NAME") %></font></td><!-- 车型-->
<td><font size="3"><%=boMap.get("PACKAGE_NAME") %></font></td><!-- 配置-->
<td><font size="3"><%=boMap.get("COLOR_NAME") %></font></td><!-- 颜色 -->
<td><font size="3"><%=boMap.get("MATERIAL_CODE") %></font></td><!-- 物料代码 -->
<td><font size="3" id='thisNum_<%=boMap.get("BO_DE_ID") %>'><%=boMap.get("THIS_BOARD_NUM") %></font></td><!-- 当前组板号组板数  -->
<td><font size="3" id='aNum_<%=boMap.get("BO_DE_ID") %>'><%=boMap.get("ALLOCA_NUM") %></font>
<input type="hidden" name="boId"  value="<%=boMap.get("BO_ID") %>" /> 
<input type='hidden'  name='HIDDEN_THIS_BOARD_NUM' value="<%=boMap.get("THIS_BOARD_NUM") %>" />
<input type="hidden" name="BO_DE_ID" value="<%=boMap.get("BO_DE_ID") %>"/><!-- 明细ID -->
<input type="hidden" name="ORDER_TYPE" value="<%=boMap.get("ORDER_TYPE") %>"/><!-- 订单类型-->
<input type="hidden" name="RE_DEALER_ID" value="<%=boMap.get("REC_DEALER_ID") %>"/><!-- 收货方ID-->
<input type='hidden' id="HIDDEN_<%=boMap.get("BO_DE_ID") %>"  name='HIDDEN_ALLOCA_NUM' value="<%=boMap.get("ALLOCA_NUM") %>" /><!-- 当前组板号配车数  -->
</td>

</tr>
<tr class=table_list_row1 style="display: none" id='<%=boMap.get("BO_DE_ID") %>'>
<td></td><td colspan="11">
<table  class=table_listPC width="100%" >			
<%
	if(!mapc.isEmpty()){
		List<Map<String, Object>> orgList=(List<Map<String, Object>>)mapc.get("ORG_"+boMap.get("BO_DE_ID"));
		List<Map<String, Object>> vehicleList=(List<Map<String, Object>>)mapc.get(boMap.get("BO_DE_ID"));
		if(orgList!=null){
			for(int ve=0;ve<orgList.size();ve++){
				Map<String,Object> orgMap=(Map<String,Object>)orgList.get(ve);
				%>
				<tr>
				<td colspan="9">
					<table class="table_list" id="pcTable~<%=orgMap.get("ORG_ID") %>_<%=boMap.get("BO_DE_ID") %>">
						<tr  class=table_list_row6>
						<td>资源预留：</td><td><%=orgMap.get("AMOUNT") %></td>
						<td>已配资源：</td><td id="aNum~<%=orgMap.get("ORG_ID") %>_<%=boMap.get("BO_DE_ID") %>"><%=orgMap.get("ALLOCA_NUM") %></td>
						<td>最大可配资源：</td><td id="maxNum~<%=orgMap.get("ORG_ID") %>_<%=boMap.get("BO_DE_ID") %>"><%=orgMap.get("CAN_COUNT") %></td>
						<td colspan="5">
						<input class="normal_btn"  onclick="chooseVehicle(<%=boMap.get("MATERIAL_ID") %>,<%=boMap.get("BO_DE_ID") %>,<%=orgMap.get("ORG_ID") %>,<%=boMap.get("YELID") %>,<%=orgMap.get("CREATE_ID") %>);" value="选择车辆" type="button" name="appl2" />
						<input class='normal_btn' onclick='del(<%=boMap.get("BO_DE_ID") %>,<%=orgMap.get("ORG_ID") %>);' value='删除' type='button' name='appl2' />
						</td>
						</tr>
						<tr>
						<th><input type='checkbox' onclick="selectAll(<%=boMap.get("BO_DE_ID") %>,<%=orgMap.get("ORG_ID") %>);"/></th>
						<th>序号</th>
						<th>VIN</th>
						<th>发动机号</th>
						<th>入库日期</th>
						<th>库区</th>
						<th>库道</th>
						<th>库位</th>
						<th>操作</th>
						</tr>
						<%
							if(vehicleList!=null){
								for(int ve1=0;ve1<vehicleList.size();ve1++){
									Map<String,Object> vehicleMap=(Map<String,Object>)vehicleList.get(ve1);
									%>
									<tr>
										<td><input type='checkbox' id='groupIds' name="groupIds~<%=orgMap.get("ORG_ID") %>_<%=boMap.get("BO_DE_ID") %>"/>
											<input type='hidden'  name='VEHICLE_IDS' value='<%=vehicleMap.get("VEHICLE_ID") %>'/>
											<input type='hidden'  name='VEHICLE_IDS_<%=boMap.get("BO_DE_ID") %>' value='<%=vehicleMap.get("VEHICLE_ID") %>'/>
											<input type='hidden' name='BO_DE_IDS' value='<%=boMap.get("BO_DE_ID") %>'/>
											<input type='hidden' name='NEW_DETAIL_ID' value='<%=vehicleMap.get("DETAIL_ID") %>'/>
										</td><!-- 车辆ID -->
										<td><%=ve1+1 %></td><!-- 序号 -->
										<td><%=vehicleMap.get("VIN") %></td><!-- VIN -->
										<td><%=vehicleMap.get("ENGINE_NO") %></td><!-- 发动机号 -->
										<td><%=vehicleMap.get("ORG_STORAGE_DATE") %></td><!-- 入库日期 -->
										<td><%=vehicleMap.get("AREA_NAME") %></td><!-- 库区 -->
										<td><%=vehicleMap.get("ROAD_NAME") %></td><!-- 库道 -->
										<td><%=vehicleMap.get("SIT_NAME") %></td><!-- 库位 -->
										<td><a href='javascript:void(0);' onclick='delOnly(this)'>[删除]</a></td><!-- 车辆ID -->
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
<td></td>
<td><h2><font size="3"  >合计：</font></h2></td>
<td><h2><font size="3"  id='SHOW_THIS_BOARD_NUM'></font></h2></td>
<td><h2><font size="3" id="SHOW_ALLOCA_NUM"></font ></h2></td>
</tr></tbody></table>
</div>
<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;" id="subtab1">
	<tr class="table_list_row2">
		<td align="center" colspan="8"> 
			<input type="button" name="button1" class="normal_btn" id="saveButton" onclick="save();" value="配车完成" /> 
			<input type="button" name="button1" class="normal_btn" id="goBack" onclick="back();" value="返回" /> 
		</td>
	</tr>
</table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var changeType="more";//判断能否多选（单选）
	function doInit(){
		var arrayObj2 = new Array(); 
		var thisBoardSum=0;//当前组板总数
		arrayObj2=document.getElementsByName("HIDDEN_THIS_BOARD_NUM");//当前组板数组
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
	function selectAll(va,orgId) { 
		var checkboxs=document.getElementsByName("groupIds~"+orgId+"_"+va); 
		for (var i=0;i<checkboxs.length;i++) {  
			var e=checkboxs[i];  
			e.checked=!e.checked; 
		}
	}
	//表头批量删除处理方法
	function del(va,orgId){
		var checkNum=0;
		var arrayObj = new Array(); 
		arrayObj=document.getElementsByName("groupIds~"+orgId+"_"+va); //所有checkbox
		for(var i=arrayObj.length-1;i>=0;i--){
			if(arrayObj[i].checked){
				var rowIndex =arrayObj[i].parentElement.parentElement.rowIndex;
				arrayObj[i].parentElement.parentElement.parentElement.deleteRow(rowIndex); 
				checkNum++;
			}else{

			}
		}
		if(checkNum==0){
			MyAlert("请选中需要删除的配车信息！");
		}
		showAlloca(va,-checkNum);//-checkNum代表数量减去checkNum
		showAllocaOrg(va,-checkNum,orgId);//删除已配资源数量
		showAllocaMax(va,checkNum,orgId);//最大可配资源数
	}
	//删除当前行处理方法
	function delOnly(obj){
		var tbId=obj.parentElement.parentElement.parentElement.parentElement.id;//获取当前table的值
		var rowIndex =obj.parentElement.parentElement.rowIndex;
		obj.parentElement.parentElement.parentElement.deleteRow(rowIndex); 
		var lastVa=tbId.substring(tbId.indexOf("_")+1,tbId.length);//截取组板明细ID
		var lastOrg=tbId.substring(tbId.indexOf("~")+1,tbId.indexOf("_"));//省份ID
		showAlloca(lastVa,-1);//-1代表数量减去1
		showAllocaOrg(lastVa,-1,lastOrg);
		showAllocaMax(lastVa,1,lastOrg);
		
		
	} 
	//动态添加一行记录（车辆信息）传入当前的组板 ID
	function addRowVehicle(arrayAllObj,boDeId,orgId){
			var trId=document.getElementById(boDeId);
			var img_=document.getElementById("img_"+boDeId);
			if(trId.style.display=="none"){
				img_.src="<%=contextPath %>/img/nolines_minus.gif";//隐藏变加号图标
				trId.style.display="";	
			}
			var tabParam="pcTable~"+orgId+"_"+boDeId;
			var tableID = document.getElementById(tabParam); 
			if(arrayAllObj!=null && arrayAllObj.length>0){	
			for(var i=0;i<arrayAllObj.length;i++){
				var arr=arrayAllObj[i];
				var newRow=tableID.insertRow(tableID.rows.length);//插入新的一行  
					 var str="<input type='checkbox' id='groupIds' name='groupIds~"+orgId+"_"+boDeId+"'/>";
					 	str+="<input type='hidden' name='VEHICLE_IDS' value='"+arr[0]+"'/>";
					 	 str+="<input type='hidden' name='VEHICLE_IDS_"+boDeId+"' value='"+arr[0]+"'/>";
					 	 str+="<input type='hidden' name='BO_DE_IDS' value='"+boDeId+"'/>";
					 	 str+="<input type='hidden' name='NEW_DETAIL_ID' value='newVechile'/>"
					 var cel0=newRow.insertCell(0);
					     cel0.innerHTML=str;//多选框
					 var cel1=newRow.insertCell(1);//序号
					     cel1.innerHTML=tableID.rows.length-2;
					 //var cel9=newRow.insertCell(2);//流水号
					    // cel9.innerHTML=arr[7]==null?"":arr[7];
					 var cel2=newRow.insertCell(2);//<!-- VIN -->
					     cel2.innerHTML=arr[1];
					 var cel3=newRow.insertCell(3);//<!-- 发动机号 -->
					     cel3.innerHTML=arr[2];
					 var cel4=newRow.insertCell(4);//<!-- 入库日期 -->
					     cel4.innerHTML=arr[3];
					 var cel5=newRow.insertCell(5);//<!-- 库区 -->
						 cel5.innerHTML=arr[4];
					 var cel6=newRow.insertCell(6);//<!-- 库道 -->
						 cel6.innerHTML=arr[5];
					 var cel7=newRow.insertCell(7);//<!-- 库位 -->
						 cel7.innerHTML=arr[6];
					 var cel8=newRow.insertCell(8);//<!-- 车辆ID -->
						 cel8.innerHTML="<a href='javascript:void(0);' onclick='delOnly(this)'>[删除]</a>";	 
						 showAlloca(boDeId,1);
						 showAllocaOrg(boDeId,1,orgId);
						 showAllocaMax(boDeId,-1,orgId);
				}
			}
			 
	}
	//显示配车数量(组板明细ID，添加数量)
	function showAlloca(boDeId,num){
		//页面显示
		var aNum_= "aNum_"+boDeId;
		var alloca_num=document.getElementById(aNum_); 
		var alloca_numBefore=parseInt(alloca_num.innerHTML);
		alloca_num.innerHTML=alloca_numBefore+num;
		//后台调用
		var hidden_= "HIDDEN_"+boDeId;
		var hidden_alloca_num=document.getElementById(hidden_); 
		hidden_alloca_num.value=alloca_numBefore+num;
		
		var arr=new Array();
		var allocaAll=0;
		arr=document.getElementsByName("HIDDEN_ALLOCA_NUM");//配车数组
		for(var i=0;i<arr.length;i++){
			if(arr[i].value!=""){
				allocaAll+=arr[i].value*1;
			}
		}
		document.getElementById("SHOW_ALLOCA_NUM").innerHTML=allocaAll;
		
	}
	//显示已配资源的数量（组板明细ID，添加数量，省份ID)
	function showAllocaOrg(boDeId,num,orgId){
		//页面显示
		var aNum_org= "aNum~"+orgId+"_"+boDeId;
		var alloca_num_org=document.getElementById(aNum_org); 
		var alloca_numBefore_org=parseInt(alloca_num_org.innerHTML);
		alloca_num_org.innerHTML=alloca_numBefore_org+num;
		////后台调用
		//var hidden_= "HIDDEN_"+boDeId;
		//var hidden_alloca_num=document.getElementById(hidden_); 
		//hidden_alloca_num.value=alloca_numBefore+num;
		
	}
	//显示最大可配资源的数量（组板明细ID，添加数量，省份ID)
	function showAllocaMax(boDeId,num,orgId){
		//页面显示
		var aNum_max= "maxNum~"+orgId+"_"+boDeId;
		var alloca_num_max=document.getElementById(aNum_max); 
		var alloca_numBefore_max=parseInt(alloca_num_max.innerHTML);
		alloca_num_max.innerHTML=alloca_numBefore_max+num;
		////后台调用
		//var hidden_= "HIDDEN_"+boDeId;
		//var hidden_alloca_num=document.getElementById(hidden_); 
		//hidden_alloca_num.value=alloca_numBefore+num;
		
	}
	//配车完成
	function save(){
		var thisNum=document.getElementById("SHOW_THIS_BOARD_NUM").innerHTML * 1;
		var allocaNum=document.getElementById("SHOW_ALLOCA_NUM").innerHTML *1;
		if(allocaNum>thisNum){
			MyAlert("操作有误！当前配车数量不能大于本次组板数量！");
			return;
		}
		MyConfirm("确认配车！",saveAlloca);	
	}
	function saveAlloca(){
		disabledButton(["saveButton","goBack"],true);
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/AllocaManage/allocaManageMain.json",saveAllocaBack,'fm','queryBtn'); 
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
			
			fm.action = "<%=contextPath%>/sales/storage/sendmanage/AllocaManage/allocaManageInit.do";
			fm.submit();
		}
		else if(json.returnValue == 2)
		{
			parent.MyAlert("操作失败，无数据需添加！");
			fm.action = "<%=contextPath%>/sales/storage/sendmanage/AllocaManage/allocaManageInit.do";
			fm.submit();
		}
		else
		{
			disabledButton(["saveButton","goBack"],false);
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	//选择车辆
	//function chooseVehicle(materialId,boDeId){
		//OpenHtmlWindow('<%=contextPath%>/sales/storage/sendmanage/AllocaManage/picRecInit.do?MATERIAL_ID='+materialId+'&boDeId='+boDeId,1000,500);
		// window.open('<%=request.getContextPath()%>/sales/storage/sendmanage/AllocaManage/picRecInit.do?MATERIAL_ID='+materialId+'&boDeId='+boDeId) ;
	//}
	//选择车辆
	function chooseVehicle(materialId,boDeId,orgId,yelId,specialOrderNo){//createId生产计划明细ID
		 getCheckVehicle(boDeId);//打开之前保存已选
		 getMaxNum(boDeId,orgId);//最大资源数
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
		 window.open('<%=request.getContextPath()%>/sales/storage/storagemanage/VehiclePicture/allocaPictureFlex.do?MATERIAL_ID='+materialId+'&boDeId='+boDeId+'&orgId='+orgId+'&yelId='+yelId+'&specialOrderNo='+specialOrderNo,'图形配车',strFeatures) ;
	}
	var thisAllocaNum=0;//当前物料下已配车数量
	var maxNum=0;//最大可用资源数
	var checkVehicleObj;//选中的车
	//获取当前明细选中的配车
	function getCheckVehicle(bodeId){
		thisAllocaNum=0;
		checkVehicleObj=new Array();
		var bos=document.getElementsByName("BO_DE_IDS");
		var veIds=document.getElementsByName("VEHICLE_IDS");
		for(var i=0;i<bos.length;i++){
			if(bos[i].value==bodeId){
				thisAllocaNum++;
			}
			checkVehicleObj.push(veIds[i].value);//不需要判断那个明细，所有车辆都要拿过去
		}
	}
	//获取最大资源数
	function getMaxNum(bodeId,orgId){
		maxNum=0;
		var max=document.getElementById("maxNum~"+orgId+"_"+bodeId);
		maxNum=parseInt(max.innerHTML);
	}
	function back(){
		fm.action = "<%=contextPath%>/sales/storage/sendmanage/AllocaManage/allocaManageInit.do";
		fm.submit();
	}
	function getOrderInfoString(value) {
		var url = '<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionOrderQuery/showOrderReport.do?orderId=' + value;
		OpenHtmlWindow(url,1000,450);
	}
</script>
</body>
</html>

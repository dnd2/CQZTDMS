<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrQualityDamagePO" %>
<%@ page import="com.infodms.dms.bean.AclUserBean"%>
<%@ page import="com.infoservice.mvc.context.ActionContext"%>
<%@taglib uri="/jstl/change" prefix="change" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%
	String contextPath = request.getContextPath();
	int scan = (Integer)request.getAttribute("isScan");
	ActionContext act = ActionContext.getContext();
    AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER); 
    List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>索赔申请创建</title>
		<script language="JavaScript">
	var cloMainTime=1; //关闭主工时选择页面
	var cloTime=1; //关闭附属工时选择页面
	var cloMainPart=1; //关闭主要配件选择页面
	var cloPart=1; //关闭附加配件选择页面
	var myobj;
	var modelId='';
	var purchasedDate = '';
	var random=0;
	var hasFore=false;  //已经授权标识
	var actIsClaim='1'; //不是索赔
	var count=0;//点击次数
	//定义维修项代码和名称全局变量
	var itemcodes = new Array();
	var itemnames = new Array();
	function setCodesAndNames(){
		//刷新全局数组
		itemcodes = new Array();
		itemnames = new Array();
		
		//赋值全局数据
		var WR_LABOURCODEs = document.getElementsByName("WR_LABOURCODE");
		var WR_LABOURNAMEs = document.getElementsByName("WR_LABOURNAME");
		for(var i=0; i<WR_LABOURCODEs.length; i++){
			itemcodes[i] = WR_LABOURCODEs[i].value;
			itemnames[i] = WR_LABOURNAMEs[i].value;
		}
		
		//刷新维修配件上的作业代码值
		var Labour0s = document.getElementsByName("Labour0");
		if(Labour0s.length > 0){
			//清空所有值
			var name = '';
			for(var j=0; j<Labour0s.length; j++){
				if(j==0)
				{
					name = Labour0s[j].options.value;
				}
				Labour0s[j].options.length = 0;
			}
			
			for(var l =0; l < itemcodes.length ;l++)
			{
				if(name == itemcodes[l]){
				var type = itemcodes[0];
				var type1 = itemnames[0];
				itemcodes[0] = name;
				itemcodes[l] = type
				itemnames[0] = itemnames[l];
				itemnames[l] = type1;
				}
			}
			//重新赋值
			for(var k=0; k<Labour0s.length; k++){
				for(var a=0; a<itemcodes.length; a++){
					var varItem = new Option(itemnames[a],itemcodes[a]);
					Labour0s[k].options.add(varItem);
				}
			}
		}
	}
	var partcodes = new Array();
	var partnames = new Array();
	
	//循环配件列表生成主因件选择框
		
	function setPartCodes(){
		//刷新全局数组
		partcodes = new Array();
		partnames = new Array();
		var temp=0;
		//赋值全局数据
		var partTable = document.getElementById('partTable');
			var len  = partTable.rows.length;
			for(var a =0;a<len;a++){
				var malValue = partTable.childNodes[a].childNodes[9].childNodes[0].value;
				if(malValue==<%=Constant.RESPONS_NATURE_STATUS_01%>){//如果是主因件则加入集合
					partcodes[temp]=partTable.childNodes[a].childNodes[1].childNodes[0].value;
					partnames[temp]=partTable.childNodes[a].childNodes[2].childNodes[0].childNodes[0].value;
					temp++;
				}
			}
		//刷新维修配件上的作业代码值
		var mainPartCode = document.getElementsByName("mainPartCode");
		var outMainPart = document.getElementsByName("OUT_MAIN_PART");
		var accessoriesOutMainPart = document.getElementsByName("accessoriesOutMainPart");
		if(accessoriesOutMainPart.length>0){
		//清空辅料的值
			var fname ='';
			for(var m=0; m<accessoriesOutMainPart.length; m++){
				if(m==0)
				{
					name = accessoriesOutMainPart[m].options.value;
				}
				accessoriesOutMainPart[m].options.length = 0;
			}
			for( var n=0;n<accessoriesOutMainPart.length;n++){
				var tempV=0;
				var varItem2 = new Option("---请选择---","-1");
				if(tempV==0){
					accessoriesOutMainPart[n].options.add(varItem2);
					tempV++;
					}
				for(var a=0; a<partcodes.length; a++){
					var varItem = new Option(partnames[a],partcodes[a]);
					accessoriesOutMainPart[n].options.add(varItem);
				}
				}
			}
		if(mainPartCode.length > 0){
			//清空所有值
			var name = '';
			for(var j=0; j<mainPartCode.length; j++){
				if(j==0)
				{
					name = mainPartCode[j].options.value;
				}
				mainPartCode[j].options.length = 0;
			}
			//重新赋值
			outMainPart[0].options.length = 0;
			for(var k=0; k<mainPartCode.length; k++){
				var malValue = partTable.childNodes[k].childNodes[9].childNodes[0].value;
				var z = 0;
				for(var a=0; a<partcodes.length; a++){
					
					if(malValue==<%=Constant.RESPONS_NATURE_STATUS_02%>){//如果是次因件，则让其选择对应的主因件
					 var  varItem = new Option(partnames[a],partcodes[a]);
					 	mainPartCode[k].options.add(varItem);
					} else{
						var varItem2 = new Option("---请选择---","-1");
						if( z== 0  )
						{
							mainPartCode[k].options.add(varItem2);
							z++;
						}
						 
					}
				}
			}
			var varItem3 = new Option("---请选择---","-1");
			outMainPart[0].options.add(varItem3);
			for(var sa=0;sa<partcodes.length;sa++){
				varItem3 = new Option(partnames[sa],partcodes[sa]);
				outMainPart[0].options.add(varItem3);
			}
		}
	}
	//循环配件列表生成主因件选择框
		
	function setPartCodesCompensation(){
		//刷新全局数组
		partcodes = new Array();
		partnames = new Array();
		var temp=0;
		//赋值全局数据
		var partTable = document.getElementById('partTable');
			var len  = partTable.rows.length;
			for(var a =0;a<len;a++){
				var malValue = partTable.childNodes[a].childNodes[9].childNodes[0].value;
				if(malValue==<%=Constant.RESPONS_NATURE_STATUS_01%>){//如果是主因件则加入集合
					partcodes[temp]=partTable.childNodes[a].childNodes[1].childNodes[0].value;
					partnames[temp]=partTable.childNodes[a].childNodes[2].childNodes[0].childNodes[0].value;
					temp++;
				}
			}
		//刷新维修配件上的作业代码值
		var mainPartCode = document.getElementsByName("mainPartCode");
		var outMainPart = document.getElementsByName("OUT_MAIN_PART");
		var part_code_temp = document.getElementsByName("part_code_temp");
		if(part_code_temp.length>0){
		//清空辅料的值
			var fname ='';
			for(var m=0; m<part_code_temp.length; m++){
				if(m==0)
				{
					name = part_code_temp[m].options.value;
				}
				part_code_temp[m].options.length = 0;
			}
			for( var n=0;n<part_code_temp.length;n++){
				var tempV=0;
				var varItem2 = new Option("---请选择---","-1");
				if(tempV==0){
					part_code_temp[n].options.add(varItem2);
					tempV++;
					}
				for(var a=0; a<partcodes.length; a++){
					var varItem = new Option(partnames[a],partcodes[a]);
					part_code_temp[n].options.add(varItem);
				}
				}
			}
		if(mainPartCode.length > 0){
			//清空所有值
			var name = '';
			for(var j=0; j<mainPartCode.length; j++){
				if(j==0)
				{
					name = mainPartCode[j].options.value;
				}
				mainPartCode[j].options.length = 0;
			}
			//重新赋值
			outMainPart[0].options.length = 0;
			for(var k=0; k<mainPartCode.length; k++){
				var malValue = partTable.childNodes[k].childNodes[9].childNodes[0].value;
				var z = 0;
				for(var a=0; a<partcodes.length; a++){
					
					if(malValue==<%=Constant.RESPONS_NATURE_STATUS_02%>){//如果是次因件，则让其选择对应的主因件
					 var  varItem = new Option(partnames[a],partcodes[a]);
					 	mainPartCode[k].options.add(varItem);
					} else{
						var varItem2 = new Option("---请选择---","-1");
						if( z== 0  )
						{
							mainPartCode[k].options.add(varItem2);
							z++;
						}
						 
					}
				}
			}
			var varItem3 = new Option("---请选择---","-1");
			outMainPart[0].options.add(varItem3);
			for(var sa=0;sa<partcodes.length;sa++){
				varItem3 = new Option(partnames[sa],partcodes[sa]);
				outMainPart[0].options.add(varItem3);
			}
		}
	}
			
	function btnEnabled(){
		$('save_but').disabled=false;
		}
	//产生收费方式下拉框
	function genSelBoxExpPay(id,type,selectedKey,setAll,_class_,_script_,nullFlag,expStr){
		var str = "";
		var arr;
		if(expStr.indexOf(",")>0)
			arr = expStr.split(",");
		else {
			expStr = expStr+",";
			arr = expStr.split(",");
		}
		str += "<select style='width:60px;' id='" + id + "' name='" + id +"' class='"+ _class_ +"' " + _script_ ;
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
	function genSelBoxExpPay10(id,type,selectedKey,setAll,_class_,_script_,nullFlag,expStr){
		var str = "";
		var arr;
		if(expStr.indexOf(",")>0)
			arr = expStr.split(",");
		else {
			expStr = expStr+",";
			arr = expStr.split(",");
		}
		str += "<select style='width:69px;'id='" + id + "' name='" + id +"' class='"+ _class_ +"' " + _script_ ;
		if(nullFlag && nullFlag == "true"){
			str += " datatype='0,0,0' ";
		}
		// end
		str += " onChange=setPartCodes(this.value);> ";
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
	
	
	//null返回“”
	function getNull(data) {
		if (data==null) {
			return '';
		}else {
			return data;
		}
	}
	//返回
	function goBack(){
		window.location.href = "<%=request.getContextPath()%>/repairOrder/RoMaintainMain/roForward.do";
	}
	//判断免费保养是否需要授权
			function verFree1() {
				$('save_but').disabled=true;
			  	var purchasedDate = document.getElementById("GUARANTEE_DATE").value;
				var vin = document.getElementById("VIN").value;
				var inMileage = document.getElementById("IN_MILEAGE").value;
				if (vin==null||vin==''||vin=='null') {
					MyAlert("车辆VIN不能为空！");
					$('save_but').disabled=false;
				}else if (inMileage==null||inMileage==''||inMileage=='null') {
					MyAlert("进厂里程数不能为空！");
					$('save_but').disabled=false;
				}else{
				var url = '<%=contextPath%>/repairOrder/RoMaintainMain/getFree.json';
			   sendAjax(url,verFreeBack1,'fm');
				}
			}
			function verFreeBack1(json) {
			    if(json.is_domestic == "该车已脱保")
			    {
			       MyAlert(json.is_domestic);
			       return false;
			    }
			    if(json.bug == "做了售前索赔的不能做PDI工单")
			    {
			         MyAlert(json.bug);
			          return false;
			    }
			    
			    if(json.bug == "售前只能由做过PDI的服务站才能做")
			    {
			         MyAlert(json.bug);
			          return false;
			    }
			    
			     if(json.bug == "该件更换状态下材料费为零，请联系总部确认")
			    {
			         MyAlert(json.bug);
			          return false;
			    }
			
			
			
				var last = json.approve;
				var mastMileage = json.mileage_vin.MILEAGE;
				$('mastMileage').value=mastMileage;
				if(Number($('IN_MILEAGE').value)<Number(mastMileage)){
					MyAlert("进厂行驶里程不能小于系统行驶里程");
					$('save_but').disabled=false;
					return false;
				}

				if(json.flag=='false'){
					MyAlert("此车不在这个用户范围内!");
					$('save_but').disabled=false;
					return false;
				}
				document.getElementById("freeTimes").value=json.needTime;
				var boo = true;
       var type =$('REPAIR_TYPE').value;//获取维修类型
		if(type==<%=Constant.REPAIR_TYPE_09%>||type==<%=Constant.REPAIR_TYPE_07%>||type==<%=Constant.REPAIR_TYPE_01%>||type==<%=Constant.REPAIR_TYPE_03%>||type==<%=Constant.REPAIR_TYPE_06%>){
		    var addTable = document.getElementById('itemTable');
		    var addPartTavle= document.getElementById('partTable');
			var itemRows = addTable.rows;
			var partRows = addPartTavle.rows;
			if(itemRows.length<1){
				MyAlert("一般维修,售前维修,特殊服务,配件索赔以及急件工单必须选择一个作业代码和一个维修配件!");
				$('save_but').disabled=false;
				return false;
			}
			if(partRows.length<1){
				MyAlert("一般维修,售前维修,特殊服务,配件索赔以及急件工单必须选择一个作业代码和一个维修配件!");
				$('save_but').disabled=false;
				return false;
			}
		}
		if(type==<%=Constant.REPAIR_TYPE_02%>){
		 	var otherTable= document.getElementById('otherTable');
		 	var otherRows = otherTable.rows;
		 	if(otherRows.length<1){
			MyAlert("外出维修,必须选择其他项目,添加外出项!");
				$('save_but').disabled=false;
				return false;
			}
		}
		if(Number($('IN_MILEAGE').value)<Number($('mastMileage').value)){
			MyAlert("进厂行驶里程不能小于系统行驶里程");
			boo= false;
		}
		if(boo){
			if($('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_05 %>){
					if($('ACTIVITYCOMBO').value==''){
						MyAlert("请选择活动名称");
						$('save_but').disabled=false;
						boo = false;
					}
			}
		}
		<%-- //厂家活动对车的VIN判断一台车只能做一次活动 2014-6-24 zyw
		var ro_type = $('REPAIR_TYPE').value ;
		if('<%=Constant.REPAIR_TYPE_05%>'==ro_type){
			makeNomalFormCall('<%=request.getContextPath()%>/repairOrder/RoMaintainMain/checkActivetyOne.json',checkActivetyOne,'fm');
			var bool=document.getElementById("activityBoolean").value;
			if(bool=="false"){
				boo=false;
				$('save_but').disabled=false;
			}
		} --%>
		if(boo){
			boo = false;
			btnEnabled();
			if(submitForm('fm')){
				boo = true;
			}
		}

		
		if(type!=<%=Constant.REPAIR_TYPE_04%>){
				//判断 工时为自费,配件为索赔
	 	makeNomalFormCall('<%=request.getContextPath()%>/repairOrder/RoMaintainMain/checkLabour.json',checks,'fm');
	 	}
	 	//if((type!=<%=Constant.REPAIR_TYPE_04%>)&&(type!=<%=Constant.REPAIR_TYPE_05%>)){
	 	if((type!=<%=Constant.REPAIR_TYPE_04%>)){
	 	//判断 是否为一车一天
	 	makeNomalFormCall('<%=request.getContextPath()%>/repairOrder/RoMaintainMain/checkOneDay.json',checkVinDay,'fm');
	 	}
		if(boo){
				MyConfirm("是否添加？",confirmAdd0,[]);
			}
		}
function checks(json){
		//MyAlert(json.flag);
		if(json.flag=='true'){
			MyAlert("工时为自费时,其下配件不能进行索赔!");
		$('save_but').disabled=false;			
			boo = false;
			return false;
		}else  {
		boo = true;
		}
		}
	function confirmAdd() {
			//===============================
			var supplier_codes = document.getElementsByName('supplier_code');
			var apply_pricess = document.getElementsByName('apply_price');
			var part_code_temp = document.getElementsByName('part_code_temp');
			
			var sum_amount=0;
			if($('REPAIR_TYPE').value=='11441009'){
				var item_amount = document.getElementsByName('ITEM_AMOUNT'); 
				
				var START_DATE =$('START_DATE').value;
				var se_hh      =$('se_hh').value;
				var se_mm      =$('se_mm').value;
				var OUT_PERSON =$('OUT_PERSON').value;
				var OUT_LICENSENO= $('OUT_LICENSENO').value;
				var END_DATE= $('END_DATE').value;
				var mm1= $('mm1').value;
				var hh1= $('hh1').value;
				var OUT_SITE =$('OUT_SITE').value;
				var OUT_MILEAGE =$('OUT_MILEAGE').value;
				var OUT_MAIN_PART= $('OUT_MAIN_PART').value;
				
				
				
				
				fore:for(var i=0; i<item_amount.length;i++){
					var val=parseInt(sum_amount+item_amount[i].value);
					outer:if(val>0){
						if(START_DATE==""||se_hh==""||se_mm==""||OUT_PERSON==""||OUT_LICENSENO==""||OUT_LICENSENO==""||END_DATE==""||mm1==""||hh1==""||OUT_SITE==""||OUT_MILEAGE==""||OUT_MAIN_PART=="-1"){
							MyAlert("提示：外出维修不能有空项");
							return false;
						}
					};
				};
			}
			
			
			if(supplier_codes.length>0){
				for(var l=0;l<supplier_codes.length;l++){
					if(supplier_codes[l].value==""||supplier_codes[l].value==null){
							MyAlert("提示：新增了补偿选项,请选择供应商!");
							return;
					}
				}
			}
			if(apply_pricess.length>0){
				for(var q=0;q<apply_pricess.length;q++){
					if(apply_pricess[q].value==""||apply_pricess[q].value==null){
							MyAlert("提示：新增了补偿选项,请填写申请金额!");
							return;
					}
				}
			}
			if(part_code_temp.length!=0){
				for(var k=0;k<part_code_temp.length;k++){
					if(part_code_temp[k].value==null||part_code_temp[k].value=='-1'||part_code_temp[k].value==""){
						MyAlert('提示：请选择补偿费关联主因件!');
						$('save_but').disabled=false;	
						return false;   
					}
				}
			}
			//========================
			var workHours = document.getElementsByName('workHourCodeMap');
			var accPart = document.getElementsByName('accessoriesOutMainPart');
			for(var i=0;i<workHours.length;i++){
				if(workHours[i].value==""||workHours[i].value==null){
						MyAlert("新增了辅料选项,请选择相应辅料!");
						return;
					}
				}
			if(accPart.length!=0){
				for(var j=0;j<accPart.length;j++){
					if(accPart[j].value==null||accPart[j].value=='-1'||accPart[j].value==""){
						MyAlert('请选择辅料关联主因件!');
						$('save_but').disabled=false;	
						return false;   
					}
					}
			}
	//if($('REPAIR_TYPE').value!=<%=Constant.REPAIR_TYPE_02%> &&<%=scan%>==0&&$('VIN_INTYPE').value==<%=Constant.IF_TYPE_YES%>){
	//	MyAlert("你不能手输VIN,VIN录入方式请选择'否'");
	//	return false;
	//}
	//if($('QUELITY_GRATE').value==""||$('QUELITY_GRATE').value==null){
	//		MyAlert('请选择质量等级!');
	//			$('save_but').disabled=false;	
	//			return false;
	//	}
		//zjimingwei 2011-03-09
		if($('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_02%>){
			if($('START_DATE').value==''){
				MyAlert('外出维修开始时间不能为空');
				$('save_but').disabled=false;	
				return false;
			}
			if($('hh').value==''){
				MyAlert('外出维修开始时间时不能为空');
				$('save_but').disabled=false;	
				return false;
			}
			if($('mm').value==''){
				MyAlert('外出维修开始时间分不能为空');
				$('save_but').disabled=false;	
				return false;
			}
			if($('END_DATE').value==''){
				MyAlert('外出维修结束时间不能为空');
				$('save_but').disabled=false;	
				return false;
			}
			if($('hh1').value==''){
				MyAlert('外出维修结束时间时不能为空');
				$('save_but').disabled=false;	
				return false;
			}
			if($('mm1').value==''){
				MyAlert('外出维修结束时间分不能为空');
				$('save_but').disabled=false;	
				return false;
			}
			if($('OUT_SITE').value==''){
				MyAlert('外出目的地不能为空');
				$('save_but').disabled=false;	
				return false;   
			}
			if($('OUT_PERSON').value==''){
				MyAlert('外出人不能为空');
				$('save_but').disabled=false;	
				return false;   
			}
			if($('OUT_MAIN_PART').value==''||$('OUT_MAIN_PART').value==null||$('OUT_MAIN_PART').value=='-1'){
				MyAlert('请选择外出费用关联主因件!');
				$('save_but').disabled=false;	
				return false;   
			}
		}
		var addTable = document.getElementById('itemTable');
			//MyAlert(addTable);
		var len  = addTable.rows.length;
			
		var partTable = document.getElementById('partTable');
		var partlen  = partTable.rows.length;
			
			
			for(var a =0;a<len;a++){
			var tempTime=0;
			var laHasPart=false;
				var malValue = addTable.childNodes[a].childNodes[6].childNodes[0].value;
				if(malValue==null || malValue ==""){
				 MyAlert("请选择故障代码!");
				 $('save_but').disabled=false;	
				 return false;
				}
				for(var k =0;k<partlen;k++){
				var lCode=addTable.childNodes[a].childNodes[0].childNodes[1].value;//主工时
				var pCode = partTable.childNodes[k].childNodes[7].childNodes[0].value;//配件对应工时
				var resb = partTable.childNodes[k].childNodes[9].childNodes[0].value;
					if(lCode==pCode ){
					laHasPart=true;
						if(resb==<%=Constant.RESPONS_NATURE_STATUS_01%>){
						tempTime ++;
						}
					}
				}
				if(tempTime>1){
					MyAlert("一个工时最多只能含有一个主因件!");
					return;
				}
				if(!laHasPart){
					MyAlert("一个工时至少对应一个配件!");
					return;
				}
			}
			
			
			//判断次因件是否都选择了对应的主因件
			
			var hasMainCode=false;
			for(var a =0;a<partlen;a++){
				var mainCode = partTable.childNodes[a].childNodes[8].childNodes[0].value;
				var malValue=partTable.childNodes[a].childNodes[9].childNodes[0].value;
				var troubleDesc = partTable.childNodes[a].childNodes[12].childNodes[0].value;//故障描述
				var reason = partTable.childNodes[a].childNodes[13].childNodes[0].value;//故障原因
				var method = partTable.childNodes[a].childNodes[14].childNodes[0].value;//处理措施
				if(malValue==<%=Constant.RESPONS_NATURE_STATUS_02%>&&(mainCode==""||mainCode==null)){//如果是主因件则加入集合
					MyAlert("次因件必须选择一个对应的主因件!");
					return false;
				}
				if(malValue==<%=Constant.RESPONS_NATURE_STATUS_01%>){
					hasMainCode = true;
				}
				if(malValue==<%=Constant.RESPONS_NATURE_STATUS_01%>){
					if(troubleDesc==""||troubleDesc==null){
						MyAlert("主因件必须填写故障描述!");
						return false;
					}
					if(troubleDesc!=""){
						troubleDesc=troubleDesc.replace(/(^\s*)|(\s*$)/g, "");
						if(""==troubleDesc){
							MyAlert("提示：主因件必须填写故障描述!不能填写空格");
							return false;
						}
					}
					if(reason==""||reason==null){
						MyAlert("主因件必须填写故障原因!");
						return false;
					}
					if(reason!=""){
						reason=reason.replace(/(^\s*)|(\s*$)/g, "");
						if(""==reason){
							MyAlert("提示：主因件必须填写故障原因!不能填写空格");
							return false;
						}
					}
					if(method==""||method==null){
						MyAlert("主因件必须填写维修措施!");
						return false;
					}
					if(method!=""){
						method=method.replace(/(^\s*)|(\s*$)/g, "");
						if(""==method){
							MyAlert("提示：主因件必须填写维修措施!不能填写空格");
							return false;
						}
					}
				}
			}
			if(len > 0 || partlen > 0){
				if(!hasMainCode){
					MyAlert("每次维修必须选择一个主因件!");
					return false;
				}
			}
			
		
		//zjimingwei 2011-03-09
		if(submitForm(fm)==false)return;
		 if($('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_08%>){
			 var auditCons=document.getElementsByName('ITEM_REMARK');
				for(var i=0;i<auditCons.length;i++){
					if(auditCons[i].value==""){
						MyAlert("提示：索赔类型为PDI的必须填写备注！  ");
						return;
					}
				}
		 }
		//如果车辆有购车日期，刚不能做售前维修
		var ro_type = $('REPAIR_TYPE').value ;
		var saleInfo = document.getElementById("saleInfo").value;
		var pdiInfo = document.getElementById("pdiInfo").value;
		var is_gua = document.getElementsByName("IS_GUA");
		if('<%=Constant.REPAIR_TYPE_08%>' == ro_type){
		if(pdiInfo=="noTime"){
				MyAlert('未查到该车的入库信息,不能做PDI!');
				$('save_but').disabled=false;	
				return ;   
		 }else if(pdiInfo=="outTime"){
				MyAlert('该车已经超过时间限制,不能做PDI!');
				$('save_but').disabled=false;	
				return ;   
		 }
		}
		for(var i=0;i<is_gua.length;i++){
			if(is_gua[i].value=="0"&& '<%=Constant.REPAIR_TYPE_06%>' != ro_type&& '<%=Constant.REPAIR_TYPE_05%>' != ro_type&& '<%=Constant.REPAIR_TYPE_03%>' != ro_type){
					MyAlert("含有超三包配件,请开特殊服务单!");
					return false;
			}else if(is_gua[i].value =="1"&& '<%=Constant.REPAIR_TYPE_06%>' == ro_type){
				MyAlert("含有未超三包配件,不能开特殊服务单!");
					return false;
			}
		}
		if('<%=Constant.REPAIR_TYPE_03%>' != ro_type&&'<%=Constant.REPAIR_TYPE_05%>'!= ro_type&&'<%=Constant.REPAIR_TYPE_08%>'!= ro_type){
		if(saleInfo=="unSale"){
				MyAlert('售前车辆只能做售前维修或者相关活动!');
				$('save_but').disabled=false;	
				return ;
		}
		}
		
		var inmail = document.getElementById("IN_MILEAGE").value;
		var timeout = document.getElementById("timeOut").value ;
		var wrMail = document.getElementById("Wr_Melieage").value;
		if('<%=Constant.REPAIR_TYPE_04%>' == ro_type){
		var freeTimes = document.getElementById("freeTimes").value;
		if((parseInt(inmail)>parseInt(wrMail)||timeout=="outTime")&& freeTimes==1){
			document.getElementById("hasWr").value="0";
			MyAlert("该车已经超过整车三包!此次保养不能享受三包服务.");
		}
		}
		verFree1();
		
	}

function checkVinDay(json){
		if(json.flag=='true'){
			MyAlert("同一车一天只能开一次索赔或者预授权工单!");
		$('save_but').disabled=false;			
			boo = false;
			return false;
		}else if(json.haspart=='true'){
			MyAlert("含有没有库存的配件,不能开工单!");
		$('save_but').disabled=false;			
			boo = false;
			return false;
		}else {
			boo = true;
		}
}
	
	function confirmAdd0() {
		$('freeTimes').disabled=false;
		$('save_but').disabled=true;
		//设置判定需要的数据
		var arr = document.getElementsByName('PART_CODE');
		var PAY_TYPE_PART = document.getElementsByName('PAY_TYPE_PART');
		var WR_LABOURCODE= document.getElementsByName('WR_LABOURCODE');
		var PAY_TYPE_ITEM= document.getElementsByName('PAY_TYPE_ITEM');
		var str = ''; 
		for(var i=0;i<arr.length;i++)
			str = str+arr[i].value+"," ;
		var codes = str.substr(0,str.length-1);
		str = '';
		for(var i=0;i<PAY_TYPE_PART.length;i++)
			str = str+PAY_TYPE_PART[i].value+"," ;
		var codes_type = str.substr(0,str.length-1);
		var strcode = '';
		for(var i=0;i<WR_LABOURCODE.length;i++)
			strcode = strcode+WR_LABOURCODE[i].value+"," ;	
		var labcodes = strcode.substr(0,strcode.length-1);
		strcode = '';
		for(var i=0;i<PAY_TYPE_ITEM.length;i++)
			strcode = strcode+PAY_TYPE_ITEM[i].value+"," ;	
		var labcodes_type = strcode.substr(0,strcode.length-1);
		
		$("codes").value = codes;
		$("codes_type").value = codes_type;
		$("labcodes").value = labcodes;
		$("labcodes_type").value = labcodes_type;
		var vin =$('VIN').value;
		var ro_type = $('REPAIR_TYPE').value ;
		makeFormCall('<%=request.getContextPath()%>/OrderAction/checkActivityByVin.json?vin='+vin+'&ro_type='+ro_type,backCheckActivityByVin,'fm');
	}
	function backCheckActivityByVin(json){
		var res=json.map.res;
		var checkParts=json.map.checkParts;
		if(""==checkParts){
			makeFormCall('<%=request.getContextPath()%>/repairOrder/RoMaintainMain/roInsert.json?uuFlag=2',saveBut,'fm');
		}else{
			MyAlert(res+checkParts);
			$('save_but').disabled=false;
		}
	}
	function  saveBut(json){
		if(json.success=="init"){
		if(json.baoyang!=""){
			MyAlert("保存成功,"+json.baoyang);
		}else{
			MyAlert("保存成功!");
		}
	updateFsFile(json);
	
	if(json.accredit==1 && json.isClaimFore==0){
		fm.action="<%=request.getContextPath()%>/repairOrder/RoMaintainMain/roModifyForward1.do?ID="+json.ID;
		fm.submit();
	}else {
		fm.action="<%=request.getContextPath()%>/repairOrder/RoMaintainMain/roForward.do";
		fm.submit();
	}
			
		} else  if(json.success=="outin"){
			MyAlert(json.outNotice);
			$('save_but').disabled=false;
			return;	
		}else{
		 MyAlert("保存失败,请联系系统管理员!");
		 $('save_but').disabled=false;
		 //goBack();
		}
	}
	
	function updateFsFile(json) {
		var sss = document.getElementById('fileUploadTab');
		var id = json.ID;
		for (var i=1;i<sss.rows.length;i++) {
			var fjid = sss.rows[i].FJID;
			var pjid = sss.rows[i].childNodes[1].innerHTML;
			
			makeFormCall('<%=request.getContextPath()%>/repairOrder/RoMaintainMain/updateFileUploadLast.json?ID='+id+'&fjid='+fjid+'&pjid='+pjid,updateFileUp,'fm');
		}
	}
	
	
	function updateFileUp(json) {
		if(json.result==0){
			MyAlert("更新附件失败!");
		}
	}
	//验证发动机号
	function engineNo(){
		var vin = $('VIN').value;
		var engineNo = $('ENGINE_NO').value;
		MyAlert(vin);	MyAlert(engineNo);
		var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/viewEngineNo.json';
    	makeCall(url,verEngineNo,{VIN:vin,ENGINE_NO:engineNo});
	}
	function verEngineNo(json){
		var is_a = json.is_a;
		if(is_a!=1){
			MyAlert("发动机号录入错误，请重新输入！");
		}
		}
	//校验工单和行号在索赔单表中是否有重复
	function verDupRo() {
		var roNo = document.getElementById("RO_NO").value;
		var lineNo = document.getElementById("LINE_NO").value;
		var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/verifyDupRo.json';
    	makeCall(url,verDupRoBack,{roNo:roNo,lineNo:lineNo});
	}
	//回调函数
	function verDupRoBack(json) {
    	var last=json.dup;
    	if (last){
    		MyAlert("该工单号和行号已经在索赔申请单中存在！");
    	}else {
    		MyConfirm("是否添加？",confirmAdd0,[]);
    	}
    }
    //取得配件是否在三包期内
	function getGuaFlag() {
		var partCode = myobj.cells.item(1).childNodes[0].value;
		var vin = document.getElementById("VIN").value;
		var inMileage = document.getElementById("IN_MILEAGE").value;
		var purchasedDate = document.getElementById("GUARANTEE_DATE").value;
		var repairType = document.getElementById("REPAIR_TYPE").value;
		
		if (repairType==<%=Constant.REPAIR_TYPE_09%>){
			var url = '<%=contextPath%>/repairOrder/RoMaintainMain/getGuaFlag1.json';
			makeCall(url,getGuaFlagBack,{vin:vin,inMileage:inMileage,partCode:partCode,purchasedDate:purchasedDate,repairType:repairType,zfRoNo:zfRoNo1});
		}else{
			var url = '<%=contextPath%>/repairOrder/RoMaintainMain/getGuaFlag.json';
    		makeCall(url,getGuaFlagBack,{vin:vin,inMileage:inMileage,partCode:partCode,purchasedDate:purchasedDate,repairType:repairType});
		}
	}
	//回调函数
	function getGuaFlagBack(json) {
    	var last=json.flag;
    	if (last){
    		myobj.cells.item(0).childNodes[0].checked=true;
    		myobj.cells.item(0).childNodes[0].value="1";
    		assignSelectObj(myobj.cells.item(6).childNodes[0],'<%=Constant.PAY_TYPE_02%>');
    	}else if(json.noNo !="NoGame"){
    	if($('REPAIR_TYPE').value != '<%=Constant.REPAIR_TYPE_06%>'){
    		MyAlert(json.notice+" 请开特殊服务单!");
    		delPartItem(myobj,'part');
    	}else{
    		myobj.cells.item(0).childNodes[0].checked=false;
    		myobj.cells.item(0).childNodes[0].value="0";
    		}
    	}else{
    		MyAlert(json.notice);
    		delPartItem(myobj,'part');
    	}
    }
    
	function doInit()
	{
	   	loadcalendar();
	}
	//计算费用
	function countQuantity(obj) {
	var total =0;
		myobj = obj.parentNode.parentNode
		var price = myobj.cells.item(4).childNodes[0].value;
		var quantity = obj.value;
		var amount =(price*quantity);
		var down  = document.getElementById("CL_DOWN").value;
		var reg = /^\d+$/;
		if (quantity!=null&&quantity!=""){
			if(!reg.test(quantity)){
				MyAlert("配件数量请输入正整数!");
				$('save_but').disabled=false;
				return false;
			}else{
			$('save_but').disabled=false;
			if(down !=null && down !=""){
			total = amount*down/100;
			}else {
			total = amount;
			}
			total=total.toFixed(2); 
			myobj.cells.item(5).innerHTML = '<td><input type="text" class="little_txt" value="'+total+'" name="AMOUNT" id="AMOUNT"  size="10" maxlength="9" readonly /></td>';
			document.getElementById("ALL_PART_AMOUNT").innerText =sumArr(document.getElementsByName("AMOUNT")) ;
			sumAll();
			}
		}else{
			MyAlert("请输入配件使用数量!");
			myobj.cells.item(5).childNodes[0].value=0;
			$('save_but').disabled=false;
		}
		
		
	}
	//计算费用(工时)
	function countQuantityLabour(obj) {
		myobj = obj.parentNode.parentNode
		var parameterValue = myobj.cells.item(3).childNodes[0].value;
		var labourQuotiety = obj.value;
		if (labourQuotiety!=null&&labourQuotiety!=""){
			myobj.cells.item(4).innerHTML = '<td><input type="text" name="LABOUR_AMOUNT" class="little_txt" readonly  value="'+accMul(labourQuotiety,parameterValue)+'" size="8" maxlength="9" ="true" readonly/></td>';
			document.getElementById("BASE_LABOUR").innerText = sumArr(document.getElementsByName("LABOUR_HOURS"));
			document.getElementById("BASE_LABOUR_AMOUNT").innerText = sumArr(document.getElementsByName("LABOUR_AMOUNT"));
			document.getElementById("ALL_LABOUR_AMOUNT").innerText = sumArr(document.getElementsByName("LABOUR_AMOUNT"));
			sumAll();
		}else {
			//MyAlert("请输入数量！");
		}
	}
	//计算工时，配件和附加的费用
	function countFee() {
		document.getElementById("ADD_LABOUR").innerText = sumArr(document.getElementsByName("LABOUR_HOURS0"));
		document.getElementById("ADD_LABOUR_AMOUNT").innerText = sumArr(document.getElementsByName("LABOUR_AMOUNT0"));
		document.getElementById("ALL_LABOUR_AMOUNT").innerText = accAdd(sumArr(document.getElementsByName("LABOUR_AMOUNT")),sumArr(document.getElementsByName("LABOUR_AMOUNT0")));
		document.getElementById("ALL_PART_AMOUNT").innerText = sumArr(document.getElementsByName("AMOUNT"));
		document.getElementById("OTHER_AMOUNT").innerText = sumArr(document.getElementsByName("ITEM_AMOUNT"));
		sumAll(); 
	}
	//获取VIN的方法
	function showVIN(){
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectWorkHoursCodeForward.do',800,500);
	}
	//获取子页面传过来的数据
	function setVIN(VIN,LICENSE_NO,MODEL_NAME,SERIES_NAME,REARAXLE_NO,GEARBOX_NO,ENGINE_NO,COLOR,PRODUCT_DATE,PURCHASED_DATE,CUSTOMER_NAME,CERT_NO,MOBILE,ADDRESS_DESC,HISTORY_MILE,MODEL_ID,BRAND_NAME,PURCHASED_DATE,TRANSFER_NO,BRAND_CODE,SERIES_CODE,MODEL_CODE,YIELDLY){
		document.getElementById("VIN").value = VIN;
		document.getElementById("LICENSE_NO").value = LICENSE_NO;
		document.getElementById("BRAND_NAME").innerHTML = BRAND_NAME;
		document.getElementById("SERIES_NAME").innerHTML = SERIES_NAME;
		document.getElementById("MODEL_NAME").innerHTML = MODEL_NAME;
		document.getElementById("ENGINE_NOS").innerHTML = ENGINE_NO;
		
		document.getElementById("BRAND_NAME0").value = BRAND_NAME;
		document.getElementById("SERIES_NAME0").value = SERIES_NAME;
		document.getElementById("MODEL_NAME0").value = MODEL_NAME;
		document.getElementById("BRAND_CODE0").value = BRAND_CODE;
		document.getElementById("SERIES_CODE0").value = SERIES_CODE;
		document.getElementById("MODEL_CODE0").value = MODEL_CODE;
		document.getElementById("ENGINE_NO0").value = ENGINE_NO;
		document.getElementById("GUARANTEE_DATE").value=formatDate(PURCHASED_DATE);
		
		modelId=MODEL_ID;
		purchasedDate=PURCHASED_DATE;
		freeOnchangeText(modelId);
	}
	//服务活动车型联动
	function changeActivity(modelId) {
		var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getActivityStrById.json';
		makeCall(url,changeActivityBack,{modelId:modelId});
	}
	function changeActivityBack(json) {
		var last = json.activity;
		var str = '<select  id="ACTIVITYCOMBO" name="ACTIVITYCOMBO" onchange="setCheckbox(this);">';
		str += last;
		str += '</select>';
		document.getElementById("activityTableId0").innerHTML=str;
	}
	//校验工单和行号在索赔单表中是否有重复
	function oneVIN() {
		var vin = document.getElementById("VIN").value;
		var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getDetailByVin.json?VIN='+vin;
		var pattern=/^([A-Z]|[0-9]){17,17}$/;
		if(pattern.exec(vin) && "" != vin && "undefined" !=typeof vin && "undefined" != vin) {
			if (vin!=null&&vin!='') {
			clearAllNode(document.getElementById('itemTable'));//当更换了车辆VIN后，将工时，配件,其他项目以及活动编号清空。
			clearAllNode(document.getElementById('partTable'));
			if($('REPAIR_TYPE').value!=<%=Constant.REPAIR_TYPE_02%>&&$('REPAIR_TYPE').value!=<%=Constant.REPAIR_TYPE_08%>&&$('REPAIR_TYPE').value!=<%=Constant.REPAIR_TYPE_09%>){
			clearAllNode(document.getElementById('otherTable'));
			}
			document.getElementById('ACTIVITYCOMBO').selectedIndex=0;
	    		makeCall(url,oneVINBack,{vinParent:vin});
	    	}
		}else {
			$("LICENSE_NO").value = '' ;
			$("BRAND_NAME").innerHTML = '' ;
			$("SERIES_NAME").innerHTML = '' ;
			$("MODEL_NAME").innerHTML = '' ;
			$("packageName").innerHTML = '' ;
			$("ENGINE_NO").value = '';
			$("ENGINE_NOS").innerHTML='';
			$("BRAND_NAME0").value = '' ;
			$("SERIES_NAME0").value = '' ;
			$("car_color").innerHTML='';
			$("car_use_desc").innerHTML='';
			$("car_use_type").value = '' ;
			$("MODEL_NAME0").value = '' ;
			$("BRAND_CODE0").value = '' ;
			$("SERIES_CODE0").value = '';
			$("MODEL_CODE0").value = '' ;
			$("ENGINE_NO0").value = '' ;
			$("GUARANTEE_DATE").value='';
			$("GUARANTEE_DATE_ID").innerHTML='';
			$("PRODUCT_DATE_ID").innerHTML='';
			$("CTM_NAME").innerHTML = '';
    		$("CTM_NAME_1").value ='' ;
			$("MAIN_PHONE").innerHTML = '' ;
			$("GUARANTEE_CODE").innerHTML ='';
			$("YIELDLY").innerHTML = '' ;
			$("timeOut").value = '';
			$("Wr_Melieage").value = '';
			$('DELIVERER').value = '' ;
			$('DELIVERER_PHONE').value = '' ;
			$("DELIVERER_ADRESS").innerHTML= '' ;
			$("DELIVERER_ADRESSS").value = '' ;
			assignSelect("YIELDLY",'');
			modelId='';
			purchasedDate='';
			freeOnchangeText('');
			//维修历史，授权历史，保养历史三按钮 控制
			$('btn1').disabled = 'disabled' ;
			$('btn2').disabled = 'disabled' ;
			$('btn3').disabled = 'disabled' ;
			MyAlert("输入的不是有效VIN格式！");
		}
	}
	//回调函数
	function oneVINBack(json) {
		if (json.ps.records==null){
		MyAlert("未找到该车辆信息!");
			$("VIN").value="";
			$("saleInfo").value="";
			$("pdiInfo").value="";
			$("VIN").focus();
			$("LICENSE_NO").value = '' ;
			$("BRAND_NAME").innerHTML = '' ;
			$("SERIES_NAME").innerHTML = '' ;
			$("MODEL_NAME").innerHTML = '' ;
			$("packageName").innerHTML = '' ;
			$("ENGINE_NO").value = '';
			$("ENGINE_NOS").innerHTML='';
			$('car_use_desc').innerHTML='';
			$('car_use_type').value='';
			$("BRAND_NAME0").value = '' ;
			$("SERIES_NAME0").value = '' ;
			$("car_color").value = '' ;
			$("MODEL_NAME0").value = '' ;
			$("BRAND_CODE0").value = '' ;
			$("SERIES_CODE0").value = '';
			$("MODEL_CODE0").value = '' ;
			$("ENGINE_NO0").value = '' ;
			$("GUARANTEE_DATE").value='';
			$("GUARANTEE_DATE_ID").innerHTML='';
			$("PRODUCT_DATE_ID").innerHTML='';
			$("CTM_NAME").innerHTML = '';
    		$("CTM_NAME_1").value ='' ;
			$("MAIN_PHONE").innerHTML = '' ;
			$("GUARANTEE_CODE").innerHTML ='';
			$("YIELDLY").innerHTML = '' ;
			$("timeOut").value = '';
			$("Wr_Melieage").value = '';
			$('DELIVERER').value = '' ;
			$('DELIVERER_PHONE').value = '' ;
			$("DELIVERER_ADRESS").innerHTML = '' ;
			$("DELIVERER_ADRESSS").value = '' ;
			modelId='';
			purchasedDate='';
			freeOnchangeText('');
			$('btn1').disabled = 'disabled' ;
			$('btn2').disabled = 'disabled' ;
			$('btn3').disabled = 'disabled' ;
		}else {
    	var last=json.ps.records;
    	var size=last.length;
    	var record;
    	if (size>0) {
    		//维修历史，授权历史，保养历史三按钮 控制
			$('btn1').disabled = false ;
			$('btn2').disabled = false ;
			$('btn3').disabled = false ;
			$('three_package_set_btn').disabled = false ;
    		record = last[0];
    		$("VIN").value =getNull(record.vin) ;
    		$("saleInfo").value=json.saleInfo;
    		$("pdiInfo").value=json.pdiInfo;
    		$("timeOut").value = json.timeOut;
			$("Wr_Melieage").value = json.Wr_Melieage;
			$("LICENSE_NO").value = getNull(record.licenseNo) ;
			$("BRAND_NAME").innerHTML = getNull(record.brandName) ;
			$("SERIES_NAME").innerHTML = getNull(record.seriesName) ;
			$("MODEL_NAME").innerHTML = getNull(record.modelCode) ;
			$("packageName").innerHTML = getNull(record.packageName) ;
			$("ENGINE_NO").value = getNull(record.engineNo) ;
			$("ENGINE_NOS").innerHTML=getNull(record.engineNo);
			$("LAST_MILEAGE").innerHTML = getNull(record.mileage)+"(公里)" ;
			$("GUARANTEE_CODE").innerHTML = getNull(record.ruleCode) ;
			$("BRAND_NAME0").value = getNull(record.brandName) ;
			$("SERIES_NAME0").value = getNull(record.seriesName) ;
			$("MODEL_NAME0").value = getNull(record.modelName) ;
			$("BRAND_CODE0").value = getNull(record.brandCode) ;
			$("SERIES_CODE0").value = getNull(record.seriesCode) ;
			$("MODEL_CODE0").value = getNull(record.modelCode) ;
			$("ENGINE_NO0").value = getNull(record.engineNo) ;
			$("GUARANTEE_DATE").value=formatDate(getNull(record.purchasedDate) );
			$("GUARANTEE_DATE_ID").innerHTML=formatDate(getNull(record.purchasedDate) );
			$("PRODUCT_DATE_ID").innerHTML=formatDate(getNull(record.productDate) );
			$("YIELDLY").innerHTML = getNull(record.yieldlyName) ;
			$("car_color").innerHTML = getNull(record.color) ;
			
			$('car_use_desc').innerHTML=getNull(record.carUseDesc);
			$('car_use_type').value=getNull(record.carUseType);
			
			$("CTM_NAME").innerHTML = getNull(record.ctmName) ;
    		$("CTM_NAME_1").value = getNull(record.ctmName) ;
			$("MAIN_PHONE").innerHTML = getNull(record.mainPhone) ;
			$('DELIVERER').value = getNull(record.ctmName) ;
			$('DELIVERER_PHONE').value = getNull(record.mainPhone) ;
			$("DELIVERER_ADRESS").innerHTML = getNull(record.address) ;
			modelId=record.packageId;
			purchasedDate=record.purchasedDate;
			freeOnchangeText(record.modelId);
        }

		//zhumingwei 2012-12-25 判断这个vin嘛在不在这个用户的业务范围里面
		
		$('save_but').disabled=false;
    }
    }
	//时间格式化
	Date.prototype.format = function(format) {   
    	var o = {   
			     "M+" : this.getMonth()+1, //month   
			     "d+" : this.getDate(),    //day   
			     "h+" : this.getHours(),   //hour   
			     "m+" : this.getMinutes(), //minute   
			     "s+" : this.getSeconds(), //second   
			     "q+" : Math.floor((this.getMonth()+3)/3), //quarter   
			     "S" : this.getMilliseconds() //millisecond   
   				}   
	   if(/(y+)/.test(format)) format=format.replace(RegExp.$1,   
	     (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
	   for(var k in o)if(new RegExp("("+ k +")").test(format))   
	     format = format.replace(RegExp.$1,   
	       RegExp.$1.length==1 ? o[k] :    
	         ("00"+ o[k]).substr((""+ o[k]).length));   
	   return format;   
	}  
	//格式化时间为YYYY-MM-DD
	function formatDate(value) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
	
	//下拉框修改时赋值
		function assignSelect(name,value) {
		var sel = document.getElementById(name);
		var option = sel.options;
		var optionLength = option.length;
		for (var i = 0;i<optionLength;i++) {
		  	if (value ==option[i].value) {
		  		sel.selectedIndex = i;
		  		break;
		  	}
		}
		}
		//下拉框修改时赋值
		function assignSelectObj(objec,value) {
		var sel = objec;
		var option = sel.options;
		var optionLength = option.length;
		for (var i = 0;i<optionLength;i++) {
		  	if (value ==option[i].value) {
		  		sel.selectedIndex = i;
		  		break;
		  	}
		}
		}
	//添加事件
	function AttachEvent(target, eventName, handler, argsObject)
	{
    var eventHandler = handler;
    if(argsObject)
    {
        eventHander = function(e)
        {
            handler.call(argsObject, e);
        }
    }
    if(window.attachEvent)//IE
        target.attachEvent("on" + eventName, eventHander );
    else//FF
        target.addEventListener(eventName, eventHander, false);
	}
	//乘法运算
	function accMul(arg1,arg2) { 
	   var m=0,s1=arg1.toString(),s2=arg2.toString(); 
	　　try{m+=s1.split(".")[1].length}catch(e){} 
	　　try{m+=s2.split(".")[1].length}catch(e){} 
	　　return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m) 
　　} 
	//加法运算
	function accAdd(arg1,arg2){ 
	   
	　　var r1,r2,m; 
	　　try{r1=arg1.toString().split(".")[1].length}catch( e){r1=0} 
	　　try{r2=arg2.toString().split(".")[1].length}catch( e){r2=0} 
	　　m=Math.pow(10,Math.max(r1,r2)) 
	　　return (arg1*m+arg2*m)/m;
　　} 

	//免费保养联动
	function freeOnchange(modelId) {
		var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeFree.json';
    	makeCall(url,changeFree,{MODEL_ID:modelId,PURCHASED_DATE:purchasedDate});
	}
	//
	function changeFree(json) {
    	var last=json.changeFree;
     	last = "<select id='FREE_M_AMOUNT' name='FREE_M_AMOUNT' onchange='setFee(this)'>"+last+"</select>";
     	document.getElementById("feeTableId").innerHTML=last;
     	var obj0 = document.getElementById("FREE_M_AMOUNT");
    	if(obj0){
   		AttachEvent(obj0,'onchange',setFee,obj0);//
   		}
    }
    //免费保养联动
	function freeOnchangeText(modelId) {
		var vinF = $('VIN').value;
		var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeFreeText.json';
    	makeCall(url,changeFreeText,{MODEL_ID:modelId,PURCHASED_DATE:purchasedDate,VIN:vinF});
	}
	//免费保养联动回调函数
	function changeFreeText(json) {
    	var last=json.changeFree;
    	if (last!=''&&last!=null) {
    		var arr1 = last.split("*");
    		arr2 = arr1[0].split(','); //免费保养费用
    		arr3 = arr1[1].split(','); //免费保养次数
    	}
    	if(obj0){
   		AttachEvent(obj0,'onchange',setFee,obj0);//
   		}
    }
	
	//品牌联动车系
	function brandOnchange(object) {
		var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getSeries.json';
    	makeCall(url,changeTroubleCode,{BRAND_CODE:itemId});
	}
	//车系联动车型
	function seriesOnchange(object) {
		var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getModel.json';
    	makeCall(url,changeTroubleCode,{SERIES_CODE:itemId});
	}
	//累加数组和
    function sumArr(arr) {
    	var sum=0;
    	var tmp = 0;
    	for (var i=0;i<arr.length;i++) {
    		tmp = arr[i].value==""?0:arr[i].value;
    		sum = accAdd(sum,parseFloat(tmp));
    		}
		return sum;	    	
    }
    //累加数组和
    function sumArr1(arr) {
    	var sum=0;
    	var tmp = 0;
    	for (var i=0;i<arr.length;i++) {
    		tmp = arr[i]==""?0:arr[i];
    		sum = accAdd(sum,parseFloat(tmp));
    	}
		return sum;	    	
    }
    //累加其他项目费用和
    function sumItem() {
    	document.getElementById("OTHER_AMOUNT").innerText = sumArr(document.getElementsByName("ITEM_AMOUNT"));
    	sumAll();
    }
    
    //计算申请金额，总金额
    function sumAll() {
    	var baseLabour = document.getElementById('BASE_LABOUR').innerText; //基本工时
    	var baseLabourAmount = document.getElementById('BASE_LABOUR_AMOUNT').innerText; //基本工时金额
    	var addLabour = document.getElementById('ADD_LABOUR').innerText; //附加工时
    	var addLabourAmount = document.getElementById('ADD_LABOUR_AMOUNT').innerText; //附加工时金额
    	var allPartAmount = document.getElementById('ALL_PART_AMOUNT').innerText;//配件金额
    	var allLabourAmount = document.getElementById('ALL_LABOUR_AMOUNT').innerText;//工时金额
    	var otherAmount = document.getElementById('OTHER_AMOUNT').innerText;//其他费用金额
    	var gameAmount = document.getElementById('GAME_AMOUNT').innerText;//免费保养金额
    	var activityAmount = document.getElementById('ACTIVITY_AMOUNT').innerText;//服务活动金额
    	var applyAmount = document.getElementById('APPLY_AMOUNT').innerText;//申请金额
    	var tax = document.getElementById('TAX').innerText;//税额
    	var arr=new Array();
		
		arr.push(allPartAmount);
		arr.push(allLabourAmount);
		arr.push(otherAmount);
		arr.push(gameAmount);
		arr.push(activityAmount);
		
    	document.getElementById('APPLY_AMOUNT').innerText = sumArr1(arr);
    	applyAmount = document.getElementById('APPLY_AMOUNT').innerText;
    	
    	arr.push(tax);
    	document.getElementById('ALL_AMOUNT').innerText = sumArr1(arr);
    	
    	
    }
    //清空费用
    function zeroAllFee() {
    	document.getElementById('BASE_LABOUR').innerText='0.00'; //基本工时
    	document.getElementById('BASE_LABOUR_AMOUNT').innerText='0.00'; //基本工时金额
    	document.getElementById('ADD_LABOUR').innerText='0.00'; //附加工时
    	document.getElementById('ADD_LABOUR_AMOUNT').innerText='0.00'; //附加工时金额
    	document.getElementById('ALL_PART_AMOUNT').innerText='0.00';//配件金额
    	document.getElementById('ALL_LABOUR_AMOUNT').innerText='0.00';//工时金额
    	document.getElementById('OTHER_AMOUNT').innerText='0.00';//其他费用金额
    	document.getElementById('GAME_AMOUNT').innerText='0.00';//免费保养金额
    	document.getElementById('ACTIVITY_AMOUNT').innerText='0.00';//服务活动金额
    	document.getElementById('APPLY_AMOUNT').innerText='0.00';//申请金额
    	document.getElementById('TAX').innerText='0.00';//税额
    	document.getElementById('ALL_AMOUNT').innerText ='0.00'; //总费用
    }
 
	//工时选择
	function setMainTime(id,labourCode,wrgroupName,cnDes,labourQuotiety,parameterValue,fore,isSpec,level) {
		var table = myobj.parentNode;
		var length= table.childNodes.length;
		var flag=0;
		var total=0;
		//判断是否添加了重复的主工时
		for (var i = 0;i<length;i++) {
			if (table.childNodes[i].childNodes[7].childNodes.length==3) {
				if(labourCode==table.childNodes[i].childNodes[0].childNodes[1].value){
					MyAlert("该主工时已经存在，不可添加！");
					cloMainTime=0;
					flag=1;
					break;
				}
			}
		}
		if (flag==0){
			cloMainTime=1;
			if(isSpec==0){
			var down  = document.getElementById("GS_DOWN").value;
			if(down !=null && down !=""){
			total = accMul(labourQuotiety,parameterValue)*down/100;
			}else{
			total = accMul(labourQuotiety,parameterValue);
			}
			total=total.toFixed(2); 
			//chooseItem(labourCode);
			myobj.cells.item(0).innerHTML='<td><input name="MAIN_ITEM"  type="hidden" value="on"/><input type="text" class="phone_txt"   name="WR_LABOURCODE" readonly value="'+labourCode+'" size="10"/><span style="color:red">*</span><a href="#"  onclick="selectMainTime(this);">选择</a></td>'
			myobj.cells.item(1).innerHTML='<td><span class="tbwhite"><input type="text" class="long_txt" name="WR_LABOURNAME"  value="'+cnDes+'" size="10" readonly/><input type="hidden" class="long_txt" name="FORE"  value="'+fore+'" size="10" readonly/></span></td>';
			myobj.cells.item(2).innerHTML='<td><input type="text" name="LABOUR_HOURS" class="little_txt"   value="'+labourQuotiety+'" size="8" maxlength="11" id="PRICE" readonly/></td>';
			myobj.cells.item(3).innerHTML='<td><input type="text" name="LABOUR_PRICE" class="little_txt"   value="'+parameterValue+'" size="8" maxlength="11" id="y20000001" readonly/></td>';
			myobj.cells.item(4).innerHTML='<td><input type="text" name="LABOUR_AMOUNT" class="little_txt" readonly  value="'+total+'" size="8" maxlength="9" readonly="true"/></td>';
			myobj.cells.item(7).innerHTML =  '<td><input type="hidden"  class="little_txt"  value="'+level+'"  name="LEVELS"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this);"/></td>';
			
			}else {
			var down  = document.getElementById("GS_DOWN").value;
			if(down !=null && down !=""){
			total = accMul(labourQuotiety,parameterValue)*down/100;
			}else {
			total=accMul(labourQuotiety,parameterValue);
			}
			total=total.toFixed(2); 
				random++;
				//chooseItem(labourCode);
				myobj.cells.item(0).innerHTML='<td><input name="MAIN_ITEM"  type="hidden" value="on"/><input type="text" class="phone_txt"   name="WR_LABOURCODE" readonly value="'+labourCode+'" size="10"/><span style="color:red">*</span><a href="#"  onclick="selectMainTime(this);">选择</a></td>'
				myobj.cells.item(1).innerHTML='<td><input type="text" class="long_txt"  name="WR_LABOURNAME" datatype="0,is_digit_letter_cn,100" value="'+cnDes+'" size="10" /><input type="hidden" class="long_txt"  name="FORE" datatype="0,is_digit_letter_cn,100" value="'+fore+'" size="10" /></td>';
				setMustStyle([myobj.cells.item(1).childNodes[0]]);
				myobj.cells.item(2).innerHTML='<td><input type="text"  name="LABOUR_HOURS" class="little_txt" datatype="0,is_double,6" decimal="2"  value="'+labourQuotiety+'" size="8" maxlength="11" id="PRICE'+random+'" onblur="countQuantityLabour(this);" /></td>';
				setMustStyle([myobj.cells.item(2).childNodes[0]]);
				myobj.cells.item(3).innerHTML='<td><input type="text" name="LABOUR_PRICE" class="little_txt"   value="'+parameterValue+'" size="8" maxlength="11" id="y20000001" readonly/></td>';
				myobj.cells.item(4).innerHTML='<td><input type="text" name="LABOUR_AMOUNT" class="little_txt" readonly  value="'+total+'" size="8" maxlength="9" ="true" readonly/></td>';
				myobj.cells.item(7).innerHTML =  '<td><input type="hidden"  class="little_txt"  value="'+level+'"  name="LEVELS"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this,\'item\');"/></td>';
			}
			document.getElementById("BASE_LABOUR").innerText = sumArr(document.getElementsByName("LABOUR_HOURS"));
			document.getElementById("BASE_LABOUR_AMOUNT").innerText = sumArr(document.getElementsByName("LABOUR_AMOUNT"));
			document.getElementById("ALL_LABOUR_AMOUNT").innerText = accAdd(sumArr(document.getElementsByName("LABOUR_AMOUNT")),sumArr(document.getElementsByName("LABOUR_AMOUNT0")));
			
			
			sumAll();
			//refreshAppTimeCombo();
		}
		
	}
	//工时选择(附加工时name加0)
	function setTime(id,labourCode,wrgroupName,cnDes,labourQuotiety,parameterValue) {
	 	var table = myobj.parentNode;
	    var length = myobj.parentNode.childNodes.length; //取得配件列表长度
	    var line = getRowNo(myobj); //取得当前行在表格中的行数
	    var strline = 0;
	    var endline = length;
	    var flag = 0;
	    for (var i=line;i>=0;i--) {
	    	if (table.childNodes[i].childNodes[11].childNodes.length==3) {
	    		strline = i;
				break;
			}
	    }
	    for (var i=line;i<length;i++) {
	    	if (table.childNodes[i].childNodes[11].childNodes.length==3) {
	    		endline = i;
				break;
			}
	    }
	    for (var i=strline+1;i<endline;i++) {
	    	if (table.childNodes[i].childNodes[1].childNodes[1].value==labourCode) {
	    		cloTime=0;
	    		flag=1;
	    		MyAlert('不能在一个主工时下添加同一个附属工时');
	    		break;
	    	}
	    }
	    if (flag==0) {
	    cloTime=1;
	    for (var i=line;i>=0;i--) {
	    	if (table.childNodes[i].childNodes[11].childNodes.length==3) {
	    		//MyAlert(table.childNodes[i].childNodes[1].childNodes[2].value);
	    		myobj.cells.item(0).innerHTML='<td><input type="hidden" name="B_ML_CODE" value="'+table.childNodes[i].childNodes[1].childNodes[2].value+'"/><input name="ITEM" disabled="true"  type="checkbox" onClick="javascript:checkCheckBox(this);"/></td>';
				break;
			}
	    }
		myobj.cells.item(1).innerHTML='<td><input type="text" class="phone_txt"   name="WR_LABOURCODE0" readonly value="'+labourCode+'" size="10"/><span style="color:red">*</span><a href="#"  onclick="selectTime(this);">选择</a></td>'
		myobj.cells.item(2).innerHTML='<td><span class="tbwhite"><input type="text" class="long_txt" name="WR_LABOURNAME0"  value="'+cnDes+'" size="10" readonly/></span></td>';
		myobj.cells.item(3).innerHTML='<td><input type="text" name="LABOUR_HOURS0" class="little_txt"   value="'+labourQuotiety+'" size="8" maxlength="11" id="PRICE" readonly/></td>';
		myobj.cells.item(4).innerHTML='<td><input type="text" name="LABOUR_PRICE0" class="little_txt"   value="'+parameterValue+'" size="8" maxlength="11" id="y20000001" readonly/></td>';
		myobj.cells.item(5).innerHTML='<td><input type="text" name="LABOUR_AMOUNT0" class="little_txt" readonly  value="'+accMul(labourQuotiety,parameterValue)+'" size="8" maxlength="9" readonly="true"/></td>';
		document.getElementById("ADD_LABOUR").innerText = sumArr(document.getElementsByName("LABOUR_HOURS0"));
		document.getElementById("ADD_LABOUR_AMOUNT").innerText = sumArr(document.getElementsByName("LABOUR_AMOUNT0"));
		document.getElementById("ALL_LABOUR_AMOUNT").innerText = accAdd(sumArr(document.getElementsByName("LABOUR_AMOUNT")),sumArr(document.getElementsByName("LABOUR_AMOUNT0")));
		sumAll();
		}
	}

	var zfRoNo1="";
	//主配件选择
	function setMainPartCode(partId,partCode,partName,stockPrice,supplierCode,supplierName,fore,level,zfRoNo) {
		zfRoNo1="";
		var table = myobj.parentNode;
			var length= table.childNodes.length;
			var flag=0;
			var total=0;
			if($('PART_CODE')!=null && partCode !="00-000" && partCode !="00-0000"){
				var partCodes = $$('.PART_CODE').pluck('value');
				if(partCodes.indexOf(partCode)!=-1){
						MyAlert("该配件已经存在，不可添加！");
						return false;
				}
			}
			
			//判断是否添加了重复的主工时
			for (var i = 0;i<length;i++) {
					if(partCode==table.childNodes[i].childNodes[0].childNodes[0].value && (partCode!="00-000" && partCode!="00-0000")){
						cloMainPart=0;
						MyAlert("该配件已经存在，不可添加！");
						flag=1;
						break;
					}
			}
			if (flag==0) {
			cloMainPart=1;
			if($('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_07%>){
				stockPrice=0.0;
			}
	  	myobj.cells.item(1).innerHTML='<input type="text" class="phone_txt PART_CODE" style="width:30px;" name="PART_CODE"  value="'+partCode+'" size="10" id="PART_CODE" readonly="true"/><span style="color:red">*</span><span class="tbwhite"><a onClick="javascript:selectMainPartCode(this);alterPartCode('+'\''+partCode+'\''+')">选择</a></span>';
		myobj.cells.item(2).innerHTML='<span class="tbwhite"><input type="text" style="width:45px;" class="phone_txt" name="PART_NAME" readonly value="'+partName+'" id="PART_NAME"  size="10"/></span>';
		myobj.cells.item(4).innerHTML='<input type="text" class="little_txt" name="PRICE" value="'+stockPrice+'" size="10" maxlength="11" id="PRICE" readonly/>';
		myobj.cells.item(3).childNodes[0].value="";
		myobj.cells.item(5).childNodes[0].value="";   
		if (typeof(zfRoNo) == "undefined") { 
			zfRoNo1="";
		}else{
			zfRoNo1=zfRoNo;
		}
		
		myobj.cells.item(15).innerHTML='<td><textarea cols="8" rows="4" name="zfRoNo"  ID="zfRoNo" class="middle_txt" readonly="readonly" maxlength="2000" >'+zfRoNo1+'</textarea></td>';
		myobj.cells.item(16).innerHTML='<td><input type="hidden"  class="little_txt"  value="'+level+'"  name="PARTLEVEL" /><input type="hidden"  class="little_txt"  value="'+fore+'"  name="PARTFORE" /><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/><input type="hidden"  class="little_txt"  value="'+partId+'"  name="REAL_PART_ID"/></td>';
		
		getGuaFlag();
		alterPartCodeLast(partCode);
		}
	}
	
	//换下件选择
	function setDownPartCode(partId,partCode,partName,stockPrice,supplierCode,supplierName) {
		myobj.cells.item(2).innerHTML='<td><input type="text" name="DOWN_PART_CODE" value="'+partCode+'" size="10" readonly="true"/><span class="tbwhite"><input type="hidden" name="DOWN_PART_NAME"  value="'+partName+'" size="10" maxlength="13" readonly/><input type="hidden" name="s"  value="1" size="10" maxlength="13" readonly/><span style="color:red">*</span><a href="#" onClick="javascript:selectDownPartCode(this)">选择</a></span></td>';
		myobj.cells.item(8).innerHTML='<input type="hidden" class="short_txt" name="PRODUCER_CODE1" readonly value="'+supplierCode+'" id="PRODUCER_CODE1" /><input type="text" name="PRODUCER_NAME1" readonly id="PRODUCER_NAME1" class="short_txt" value="'+supplierName+'"/>';
	}
	//插入OPTION
	function   InsertSelect(objSel,strValue,strText) { 
		var   newOpt   =   document.createElement( "OPTION "); 
		newOpt.innerText =   strText; 
		newOpt.value =   strValue; 
		objSel.appendChild(newOpt); 
		//MyAlert(objSel.innerHTML);
	} 
	//配件工时下拉框
	function genAppTimeCombo(obj) {
		var codes=document.getElementsByName("WR_LABOURCODE");//取得主工时CODE数组
		var names=document.getElementsByName("WR_LABOURNAME");//取得主工时NAME数组
		if (codes!=null&&codes!="")
		var innerHTML = '<select  name="appTime" class="min_sel">';
		//var innerHTML= '';
		innerHTML += '<option value="">索赔工时列表</option>';
		//InsertSelect(obj,'','索赔工时列表');
		for (var i=0;i<codes.length;i++) {
			innerHTML += '<option value="'+codes[i].value+'">'+names[i].value+'</option>';
			//InsertSelect(obj,codes[i].value,names[i].value);
		}
		innerHTML += '</select><input type="hidden" name="PART" value="on"/>';
		//MyAlert(obj.innerHTML);
		//MyAlert(innerHTML);
		obj.parentNode.innerHTML=innerHTML;
	}
	//点新增时配件工时下拉框
	function genAppTimeCombo1(obj) {
		var codes=document.getElementsByName("WR_LABOURCODE");//取得主工时CODE数组
		var names=document.getElementsByName("WR_LABOURNAME");//取得主工时NAME数组
		if (codes!=null&&codes!="")
		var innerHTML = '<select  name="appTime" class="min_sel">';
		//var innerHTML= '';
		innerHTML += '<option value="">索赔工时列表</option>';
		//InsertSelect(obj,'','索赔工时列表');
		for (var i=0;i<codes.length;i++) {
			innerHTML += '<option value="'+codes[i].value+'">'+names[i].value+'</option>';
			//InsertSelect(obj,codes[i].value,names[i].value);
		}
		innerHTML += '</select><input type="hidden" name="PART" value="on"/>';
		//MyAlert(obj.innerHTML);
		//MyAlert(innerHTML);
		obj.cells[19].innerHTML=innerHTML;//update zhumingwei 2014-09-19
		//obj.cells[18].innerHTML=innerHTML;
	}
	//添加配件下拉框
	function setTd(object) {
		object.parentNode.parentNode.cells[10].innerHTML =  '<td><select name="appTime" class="min_sel"><option value="">索赔工时列表</option></select><input type="hidden" name="PART" value="on"/></td>';
	}
	//清除配件下拉框
	function clearTd(object) {
		object.parentNode.parentNode.cells[10].innerHTML =  '<td><input type="hidden" name="PART" value="off"/></td>';
	}
	 // 动态生成表格
 	function addRow(tableId){
	    var addTable = document.getElementById(tableId);
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);
		insertRow.className = "table_list_row1";
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
		insertRow.insertCell(4);
		insertRow.insertCell(5);
		if (tableId=='itemTable'||tableId=='partTable'){
			insertRow.insertCell(6);
			insertRow.insertCell(7);
		}
		if (tableId=='partTable') {
			insertRow.insertCell(8);
			insertRow.insertCell(9);
			insertRow.insertCell(10);
			insertRow.insertCell(11);
			insertRow.insertCell(12);
			insertRow.insertCell(13);
			insertRow.insertCell(14);
			insertRow.insertCell(15);
			//以下1行为  //update zhumingwei 2014-09-19
			insertRow.insertCell(16);
			insertRow.insertCell(17);
		}
		
		//这里的NAME都加0，空的时候就自动不插入到后台了
		if (tableId=='itemTable') {
		if(document.getElementById("REPAIR_TYPE").value==11441005&&document.getElementById("Activity_Type").value!=10561001&&document.getElementById("Activity_Type").value!=10561005){
			addTable.rows[length].cells[0].innerHTML =  '<td><input type="text" class="phone_txt"  id="WR_LABOURCODE'+length+'" name="WR_LABOURCODE0" readonly  value="" size="10"/><span style="color:red">*</span><a href="#" name="choiseLabouritem"  onclick="selectMainTime(this);">选择</a></td>';
			setMustStyle([document.getElementById("WR_LABOURCODE"+length)]);
			addTable.rows[length].cells[1].innerHTML =  '<td><span class="tbwhite"><input type="text" name="WR_LABOURNAME0" class="long_txt" value="" size="10" readonly/></span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" name="LABOUR_HOURS0" class="little_txt"   value="" size="8" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" name="LABOUR_PRICE0" class="little_txt"   value="" size="8" maxlength="11" id="y20000001" readonly/></td>';
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" name="LABOUR_AMOUNT0" class="little_txt"   value="" size="8" maxlength="9"  readonly/></td>';
			addTable.rows[length].cells[5].innerHTML = 	'<td><input type="text" name="PAY_TYPE_ITEM_txt"  class="little_txt" value="自费" size="8" maxlength="9" readonly/><input type="hidden" name="PAY_TYPE_ITEM"  class="little_txt" value="11801001" size="8" maxlength="9" readonly/></td>';
			addTable.rows[length].cells[6].innerHTML ='<td><input disabled="disabled" id="MALFUNCTIONS" name="MALFUNCTIONS" class="middle_txt" value="" /><input type="hidden" id="MALFUNCTION" name="MALFUNCTION" class="middle_txt" value="" /><a href="#" name="choiseLabouritem" onclick="selectMalCode(this);">选择</a></td>';
			addTable.rows[length].cells[7].innerHTML =  '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'itemTable\',this,\'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this);"/></td>';
		}else{
			//addTable.rows[length].cells[0].innerHTML =  '<td><input name="ITEM"  checked type="hidden" disabled="true" "/><input name="MAIN_ITEM"  type="hidden" value="on"/><input name="ITEM_IS_FORE"  type="checkbox" disabled="true" /></td>';
			addTable.rows[length].cells[0].innerHTML =  '<td><input type="text" class="phone_txt"  id="WR_LABOURCODE'+length+'" name="WR_LABOURCODE0" readonly  value="" size="10"/><span style="color:red">*</span><a href="#" name="choiseLabouritem"  onclick="selectMainTime(this);">选择</a></td>';
			setMustStyle([document.getElementById("WR_LABOURCODE"+length)]);
			addTable.rows[length].cells[1].innerHTML =  '<td><span class="tbwhite"><input type="text" name="WR_LABOURNAME0" class="long_txt" value="" size="10" readonly/></span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" name="LABOUR_HOURS0" class="little_txt"   value="" size="8" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" name="LABOUR_PRICE0" class="little_txt"   value="" size="8" maxlength="11" id="y20000001" readonly/></td>';
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" name="LABOUR_AMOUNT0" class="little_txt"   value="" size="8" maxlength="9"  readonly/></td>';
			addTable.rows[length].cells[5].innerHTML = genSelBoxExpPay("PAY_TYPE_ITEM",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'');
			addTable.rows[length].cells[6].innerHTML ='<td><input disabled="disabled" id="MALFUNCTIONS" name="MALFUNCTIONS" class="middle_txt" value="" /><input type="hidden" id="MALFUNCTION" name="MALFUNCTION" class="middle_txt" value="" /><a href="#" name="choiseLabouritem" onclick="selectMalCode(this);">选择</a></td>';
			addTable.rows[length].cells[7].innerHTML =  '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'itemTable\',this,\'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this);"/></td>';
			}
		}else if (tableId == 'partTable') {
		if(document.getElementById("REPAIR_TYPE").value==11441005&&document.getElementById("Activity_Type").value!=10561001&&document.getElementById("Activity_Type").value!=10561005){
			addTable.rows[length].cells[0].innerHTML = '<td><input type="checkbox" style="width: 24px;" value="" onclick="return false;" name="IS_GUA" /></td>.';
			addTable.rows[length].cells[1].innerHTML =  '<td ><input  style="width: 30px;" type="text" class="phone_txt" name="PART_CODE"   value="" size="10" id="PART_CODE" readonly/><span style="color:red">*</span><span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this)">选择</a></span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><span class="tbwhite"><input type="text" style="width: 45px;" class="phone_txt" name="PART_NAME" readonly  id="PART_NAME" name="PART_SN3"  size="10"/></span></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" style="width: 24px;" class="little_txt" onkeyup="countQuantity(this)"  onblur="countQuantity(this)" size="10" name="QUANTITY" id="QUANTITY'+length+'" datatype="0,is_double,10" decimal="2"  maxlength="20"/></td>';
			setMustStyle([document.getElementById("QUANTITY"+length)]);
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" style="width: 35px;" class="little_txt"name="PRICE0"  size="10" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" style="width: 35px;  class="little_txt" name="AMOUNT0" id="AMOUNT" size="10" maxlength="9" datatype="0,is_double,10" decimal="2" readonly /></td>';
			addTable.rows[length].cells[6].innerHTML = '<td><input type="text" style="width: 45px;" name="PAY_TYPE_PART_txt"  class="little_txt" value="自费" size="8" maxlength="9" readonly/><input type="hidden" name="PAY_TYPE_PART"  class="little_txt" value="11801001" size="8" maxlength="9" readonly/></td>';
			addTable.rows[length].cells[7].innerHTML =  '<td><select id="Labour"   style="width: 60px;" value="" name="Labour0" ></select></td>';
			addTable.rows[length].cells[8].innerHTML =  '<td><select id="mainPartCode" style="width: 60px;" value="" name="mainPartCode" ></select></td>';
			addTable.rows[length].cells[9].innerHTML =  genSelBoxExpPay10("RESPONS_NATURE",<%=Constant.RESPONS_NATURE_STATUS%>,"",false,"min_sel","","false",'');
			addTable.rows[length].cells[10].innerHTML =  genSelBoxExpPay("HAS_PART",<%=Constant.IF_TYPE%>,"<%=Constant.IF_TYPE_YES%>",false,"min_sel","","false",'');
			addTable.rows[length].cells[11].innerHTML ='<td><select style="width: 55px" onchange="changeQ(this,'+length+');" name="PART_USE_TYPE" id="PART_USE_TYPE'+length+'"><option value="95431001">维修</option> <option value="95431002" selected>更换</option></select></td>';
			
			addTable.rows[length].cells[12].innerHTML =  '<td><textarea rows="4" cols="8" class="middle_txt" name="TROUBLE_DESCRIBE"   maxlength="2000" id="TROUBLE_DESCRIBE" ></textarea></td>';
			addTable.rows[length].cells[13].innerHTML =  '<td><textarea rows="4" cols="8" class="middle_txt" name="TROUBLE_REASON" id="TROUBLE_REASON"  maxlength="2000"   ></textarea></td>';
			addTable.rows[length].cells[14].innerHTML = '<td><textarea rows="4" cols="8" name="DEAL_METHOD" ID="DEAL_METHOD" class="middle_txt" value="" maxlength="2000" ></textarea></td>';

			//以下2行为  //update zhumingwei 2014-09-19
			addTable.rows[length].cells[15].innerHTML = '<td><textarea rows="4" cols="8" name="zfRoNo" readonly="readonly" ID="zfRoNo" class="middle_txt" maxlength="2000"></textarea></td>';
			addTable.rows[length].cells[16].innerHTML = '<td><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			//addTable.rows[length].cells[15].innerHTML = '<td><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			addTable.rows[length].cells[17].innerHTML = '<td><input type="button" class="normal_btn" onclick="showUploadWithId(\'<%=contextPath%>\',this)" value="添加附件" /></td>';
			addValues();
		}else{
			addTable.rows[length].cells[0].innerHTML = '<td><input style="width: 24px;" type="checkbox" value="" onclick="return false;" name="IS_GUA" /></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><input type="text" style="width: 30px;" class="phone_txt" name="PART_CODE"   value="" size="10" id="PART_CODE" readonly/><span style="color:red">*</span><span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this);">选择</a></span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><span class="tbwhite"><input style="width: 45px;" type="text" class="phone_txt" name="PART_NAME" readonly  id="PART_NAME" name="PART_SN3"  size="10"/></span></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" style="width: 24px;" class="little_txt" onkeyup="countQuantity(this)"  onblur="countQuantity(this)" size="10" name="QUANTITY" id="QUANTITY'+length+'" datatype="0,is_double,10" decimal="2"  maxlength="20"/></td>';
			setMustStyle([document.getElementById("QUANTITY"+length)]);
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" class="little_txt"name="PRICE0" style="width: 35px;" size="10" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" class="little_txt" name="AMOUNT0" style="width: 35px;" id="AMOUNT" size="10" maxlength="9" datatype="0,is_double,10" decimal="2" readonly /></td>';
			addTable.rows[length].cells[6].innerHTML = genSelBoxExpPay("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'');
			addTable.rows[length].cells[7].innerHTML =  '<td><select id="Labour" value="" name="Labour0" style="width: 60px;" ></select></td>';
			addTable.rows[length].cells[8].innerHTML =  '<td><select id="mainPartCode" value="" name="mainPartCode" style="width: 60px;" ></select></td>';
			addTable.rows[length].cells[9].innerHTML =  genSelBoxExpPay10("RESPONS_NATURE",<%=Constant.RESPONS_NATURE_STATUS%>,"",false,"min_sel","","false",'');
			
			addTable.rows[length].cells[10].innerHTML =  genSelBoxExpPay("HAS_PART",<%=Constant.IF_TYPE%>,"<%=Constant.IF_TYPE_YES%>",false,"min_sel","","false",'');
			addTable.rows[length].cells[11].innerHTML =  '<td><select style="width: 55px" onchange="changeQ(this,'+length+');"  name="PART_USE_TYPE" id="PART_USE_TYPE'+length+'"><option value="95431001">维修</option> <option value="95431002" selected>更换</option></select></td>';
			addTable.rows[length].cells[12].innerHTML =  '<td><textarea rows="4" cols="8"  class="middle_txt" name="TROUBLE_DESCRIBE"   maxlength="2000" id="TROUBLE_DESCRIBE" ></textarea></td>';
			addTable.rows[length].cells[13].innerHTML =  '<td><textarea rows="4" cols="8" class="middle_txt" name="TROUBLE_REASON" id="TROUBLE_REASON"  maxlength="2000" ></textarea></td>';
			addTable.rows[length].cells[14].innerHTML = '<td><textarea rows="4" cols="8" name="DEAL_METHOD" ID="DEAL_METHOD" class="middle_txt" maxlength="2000" ></textarea></td>';

			//以下2行为  //update zhumingwei 2014-09-19
			addTable.rows[length].cells[15].innerHTML = '<td><textarea rows="4" cols="8"  name="zfRoNo" readonly="readonly" ID="zfRoNo" class="middle_txt" value="" maxlength="2000" ></textarea></td>';
			addTable.rows[length].cells[16].innerHTML = '<td><input style="width: 60px;" type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			//addTable.rows[length].cells[15].innerHTML = '<td><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			addTable.rows[length].cells[17].innerHTML = '<td><input style="width: 60px;" type="button" class="normal_btn" onclick="showUploadWithId(\'<%=contextPath%>\',this)" value="添加附件" /></td>';
			addValues();
			}
		//这个不加0，不用通过弹出页面带值（其他项目）
		}else if (tableId == 'otherTable') { 
			addTable.rows[length].cells[0].innerHTML =  '<td><div align="center"><select onchange="setOtherName(this);" id="ITEM_CODE" name="ITEM_CODE">'+document.getElementById('OTHERFEE').value+'</select></div></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_NAME" id="ITEM_NAME'+length+'"  readonly class="short_txt" /></span></div></td>';
			setMustStyle([document.getElementById("ITEM_NAME"+length)]);
			addTable.rows[length].cells[2].innerHTML =  '<td><div align="center"><input type="text"  name="ITEM_AMOUNT" id="ITEM_AMOUNT'+length+'"  datatype="0,is_yuan" onblur="sumItem();"  class="short_txt"/></div></td>';
			setMustStyle([document.getElementById("ITEM_AMOUNT"+length)]);
			addTable.rows[length].cells[3].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_REMARK"  id="ITEM_REMARK" maxlength="30" class="middle_txt"  /></span></div></td>';
			addTable.rows[length].cells[4].innerHTML = genSelBoxExpPay("PAY_TYPE_OTHER",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'');
			addTable.rows[length].cells[5].innerHTML =  '<td><div align="center"><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="delItemOther(this);"/></div></td>';
			}
			return addTable.rows[length];
		}
	 
 		function showUploadWithId(path,obj){
 			var tr = this.getRowObj(obj);
 			var id = tr.childNodes[1].childNodes[0].value;
 			if (id == "" || id == null || id == "undefined") {
				MyAlert("请先选择新件代码");
				return false;
			}
 			OpenHtmlWindow(path+'/commonUpload.jsp?id='+id,800,450);
 		}
		function changeQ(obj,id){
			myobj = obj.parentNode.parentNode
		if(obj.value=='95431001'){
			document.getElementById("QUANTITY"+id).value=0;
			myobj.cells.item(5).innerHTML = '<td><input type="text" class="little_txt" value="0.0" name="AMOUNT" id="AMOUNT"  size="10" maxlength="9" readonly /></td>';
			document.getElementById("QUANTITY"+id).setAttribute("readOnly","true"); 
		}else if(obj.value=='95431002'){
			document.getElementById("QUANTITY"+id).value='';
			myobj.cells.item(5).innerHTML = '<td><input type="text" class="little_txt" value="" name="AMOUNT" id="AMOUNT"  size="10" maxlength="9" readonly /></td>';
			document.getElementById("QUANTITY"+id).setAttribute("readOnly",""); 
		}
		}
		function addValues(){
			//刷新维修配件上的作业代码值
		var Labour0s = document.getElementsByName("Labour0");
		if(Labour0s.length > 0){
			//清空所有值
			
			
			for(var j=0; j<Labour0s.length; j++){
				Labour0s[j].options.length = 0;
			}
			//重新赋值
			for(var k=0; k<Labour0s.length; k++){
				for(var a=0; a<itemcodes.length; a++){
					var varItem = new Option(itemnames[a],itemcodes[a]);
					Labour0s[k].options.add(varItem);
				}
			}
		
		}
		}
		var objRow ="";
	function changeCase(obj,value){
		objRow ="";
		if(''!=value){
		objRow=obj;
		var url = '<%=contextPath%>/repairOrder/RoMaintainMain/changeMalQud.json';
    		makeCall(url,changeBack,{code:value});
			}else{
		var tr = this.getRowObj(obj);
			tr.childNodes[7].childNodes[0].length;
		var varItem = new Option("","");
			tr.childNodes[7].childNodes[0].options.add(varItem);
			}
	}
	function changeBack(json){
		var last = json.regionList;
		var tr = this.getRowObj(objRow);
		tr.childNodes[7].childNodes[0].length=0;
	for(var i=0;i<json.regionList.size();i++){
		var varItem = new Option(json.regionList[i].MAL_NAME,json.regionList[i].MAL_CODE);
		tr.childNodes[7].childNodes[0].options.add(varItem);
	}
	}
		//选择主工时
		function selectMainTime(obj) {
			//MyAlert(document.getElementById('timeId').value);
			myobj = getRowObj(obj);
			var treeCode = 3;
			var timeId;
			treeCode=3;
			timeId='';
			openTime(obj,treeCode,timeId);
		}
		
	function selectMalCode(obj){
		myobj="";
		myobj = obj.parentNode;
		parent.OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectMalCodeForward.do',800,500);
	}
	
	function setMainMal(v1,v2,v3){
			var tr = this.getRowObj(myobj);
			tr.childNodes[6].childNodes[0].value=v2+"--"+v3;
			tr.childNodes[6].childNodes[1].value=v1;
	}
		//选择附加工时
		function selectTime(obj) {
			//MyAlert(document.getElementById('timeId').value);
			myobj = getRowObj(obj);
			var table = myobj.parentNode;
	    	var length = myobj.parentNode.childNodes.length; //取得配件列表长度
	    	var line = getRowNo(myobj); //取得当前行在表格中的行数
			var treeCode = 3;
			var timeId;
			treeCode=4;
			for (var i=line;i>=0;i--) {
	    	if (table.childNodes[i].childNodes[10].childNodes.length==3) {
	    		timeId = table.childNodes[i].childNodes[1].childNodes[2].value;
				break;
				}
	    	}
			//timeId = document.getElementById("timeId").value;
			OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectTimeForward.do?TREE_CODE='+treeCode+"&timeId="+timeId+"&MODEL_ID="+modelId,800,500);
		}
		//修改主项目工时需要删除附加工时
		function openTime(obj,treeCode,timeId){
		var aCode = document.getElementById("CAMPAIGN_CODE").value;
		var yiedlyType = document.getElementById("YIELDLY_TYPE").value;
		var vin = document.getElementById("VIN").value;
		var tr = obj.parentNode.parentNode;
		//delPlusItems(tr);
			parent.OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectMainTimeForward.do?TREE_CODE='+treeCode+"&timeId="+timeId+"&MODEL_ID="+modelId+"&vin="+vin+"&aCode="+aCode+"&yiedly="+yiedlyType,800,500);
		}
		//带出费用名称
		function setOtherName(obj){
			myobj = getRowObj(obj);
			var options = myobj.cells.item(0).childNodes[0].childNodes[0].options;
			var index = options.selectedIndex;
			var text = options[index].title;
			//changeOtherFore(options[index].value);
			myobj.cells.item(1).innerHTML='<td><div align="center"><input type="text" class="short_txt" readonly name="ITEM_NAME" value="'+text+'" id="ITEM_NAME"/></div></td>';
		}
		//其他项目选择下拉框 ：是否授权联动
		function changeOtherFore(itemCode){
			var roNo = document.getElementById("RO_NO").value;
			var vin = document.getElementById("VIN").value;
			var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeOtherFore.json';
	    	makeCall(url,changeOtherForeBack,{itemCode:itemCode,roNo:roNo,vin:vin});
		}
		function changeOtherForeBack(json){
			var count = json.count;
			if (count!=null&&count!=0) {
				myobj.cells.item(0).innerHTML='<input type="checkbox"  name="OTHER_IS_FORE" disabled checked / >';
			}else {
				myobj.cells.item(0).innerHTML='<input type="checkbox"  name="OTHER_IS_FORE" disabled / >';
			}
		}
		//选择主上件
		function selectMainPartCode(obj){
			myobj = getRowObj(obj);
			var lic  =  $('IN_MILEAGE').value;
			if(lic==""){
				MyAlert("请选输入进厂里程在进行配件选择!");
				return ;
				}
			var vin = document.getElementById("VIN").value;
			if (vin!=null&&vin!=''&&vin!='null') {
			var yiedlyType = document.getElementById("YIELDLY_TYPE").value;
			var aCode = document.getElementById("CAMPAIGN_CODE").value;
			var type =$('REPAIR_TYPE').value;
			OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectMainPartCodeForward.do?GROUP_ID='+modelId+'&vin='+vin+'&yiedlyType='+yiedlyType+'&aCode='+aCode+'&repairType='+type,800,500);
			}else {
				MyAlert('请先输入车辆VIN后，再添加配件！');
			}
		}
		//选择上件
		function selectPartCode(obj){
			myobj = getRowObj(obj);//取得行对象
			OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectPartCodeForward.do?GROUP_ID='+modelId,800,500);
		}
		//选择下件
		function selectDownPartCode(obj){
			myobj = getRowObj(obj);
			OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectDownPartCodeForward.do?GROUP_ID='+modelId,800,500);
		}
		//得到行对象
		function getRowObj(obj)
		{
		   var i = 0;
		   while(obj.tagName.toLowerCase() != "tr"){
		    obj = obj.parentNode;
		    if(obj.tagName.toLowerCase() == "table")
		  return null;
		   }
		   return obj;
		}
		
		//根据得到的行对象得到所在的行数
		function getRowNo(obj){
		   var trObj = getRowObj(obj); 
		   var trArr = trObj.parentNode.children;
		   var ret;
		 for(var trNo= 0; trNo < trArr.length; trNo++){
		  if(trObj == trObj.parentNode.children[trNo]){
		  		ret = trNo;
		  		break;
		  }
		 }
		 return ret;
		}
		
		
		//删除行
		function delItem(obj){
		    var tr = this.getRowObj(obj);
		    if(tr.childNodes[5].childNodes.length==3) {
		    	MyConfirm("是否删除？",delItems,[tr]);
		    }else{
			   if(tr != null){
				    /*艾春2013.04.16 添加删除提示窗口*/
					var res = confirm("你确认要删除吗？");
				    if(res){
				    	tr.parentNode.removeChild(tr);
					    countFee();
					  	//刷新配件上的作业代码列表
					  	var Labour0s = document.getElementsByName("Labour0");
					  	var name = '';
					  	if(Labour0s.length > 0){
									//清空所有值
									
									for(var j=0; j<Labour0s.length; j++){
										if(j == 0)
										{
											name = Labour0s[j].options.value;
										}
										Labour0s[j].options.length = 0;
									}
								}
								for(var l =0; l < itemcodes.length ;l++)
								{
									if(name == itemcodes[l]){
									var type = itemcodes[0];
									var type1 = itemnames[0];
									itemcodes[0] = name;
									itemcodes[l] = type
									itemnames[0] = itemnames[l];
									itemnames[l] = type1;
									}
								}
								
								
								
					    var rescode = tr.childNodes[0].childNodes[2].value;
						if(undefined != rescode && '' != rescode){
							if(itemcodes.length > 0 ){
								for(var i=0, n=0, k=0; i<itemcodes.length; i++){
									if(itemcodes[i] != rescode){
									
									  	itemcodes[n++] = itemcodes[i];
									    itemnames[k++] = itemnames[i];
									   
									}
								}
								itemcodes.length -= 1;
								itemnames.length -= 1;
								//刷新维修配件上的作业代码值
								
								if(Labour0s.length > 0){
									//重新赋值
									for(var k=0; k<Labour0s.length; k++){
										for(var a=0; a<itemcodes.length; a++){
											var varItem = new Option(itemnames[a],itemcodes[a]);
											Labour0s[k].options.add(varItem);
										}
									}
								}
							}
						}
				    }
			   }else{
			    throw new Error("the given object is not contained by the table");
			   }
		   }
		}
		//删除行 配件
		function delPartItem(obj,name){
		     var tr = this.getRowObj(obj);
		    //MyAlert("length="+tr.childNodes[10].childNodes.length);
		    if(tr.childNodes[5].childNodes.length==3) {
		    	MyConfirm("是否删除？",delPartItems,[tr]);
		    }else{
		   if(tr != null){
		    tr.parentNode.removeChild(tr);
		    var partCode = tr.childNodes[1].childNodes[0].value;
		    delAttach(partCode);
		   }else{
		    throw new Error("the given object is not contained by the table");
		   }
		   }
		}
		//删除工时
		function delItems(tr){
		    var roNo = getRowNo(tr);
			var trlen = tr.childNodes.length;
			var length=tr.parentNode.childNodes.length;
			var endRo=roNo;
			if (length>roNo){
			for (var i=roNo+1;i<length;i++) {
				if (tr.parentNode.childNodes[i].childNodes[5].childNodes.length==3) {
					endRo=i;
					//MyAlert(endRo);
					break;
				}
			}
			}
			//如果没有找到结束行，说明为最后一个主工时，一直删除到表格长度即可
			if (endRo==roNo) {
				endRo=length;
			}
			var trObj = tr.parentNode;
			for (var i=roNo;i<endRo;i++) {
				var oldNode = trObj.removeChild(trObj.childNodes[roNo]);
				oldNode = null;
			}
			countFee();
			//refreshAppTimeCombo();
		}
		//删除配件
		function delPartItems(tr){
		    var roNo = getRowNo(tr);
			var trlen = tr.childNodes.length;
			var length=tr.parentNode.childNodes.length;
			var endRo=roNo;
			if (length>roNo){
			for (var i=roNo+1;i<length;i++) {
				if (tr.parentNode.childNodes[i].childNodes[5].childNodes.length==3) {
					endRo=i;
					break;
				}
			}
			}
			//如果没有找到结束行，说明为最后一个主工时，一直删除到表格长度即可
			if (endRo==roNo) {
				endRo=length;
			}
			var trObj = tr.parentNode;
			for (var i=roNo;i<endRo;i++) {
				var oldNode = trObj.removeChild(trObj.childNodes[roNo]);
				oldNode = null;
			}
			countFee();
			//refreshAppTimeCombo();
		}
		
		function delPlusItems(tr){
		    var roNo = getRowNo(tr);
			var trlen = tr.childNodes.length;
			var length=tr.parentNode.childNodes.length;
			var endRo=roNo;
			if (length>roNo){
			for (var i=roNo+1;i<length;i++) {
				if (tr.parentNode.childNodes[i].childNodes.length==11) {
					endRo=i;
					//MyAlert(endRo);
					break;
				}
			}
			}
			//如果没有找到结束行，说明为最后一个主工时，一直删除到表格长度即可
			if (endRo==roNo) {
				endRo=length;
			}
			var trObj = tr.parentNode;
			for (var i=roNo+1;i<endRo;i++) {
				var oldNode = trObj.removeChild(trObj.childNodes[roNo+1]);
				oldNode = null;
			}
		}
		
		
		//循环删除节点
		function clearAllNode(parentNode){
		    while (parentNode.firstChild) {
		      var oldNode = parentNode.removeChild(parentNode.firstChild);
		       oldNode = null;
		     }
		   } 
		//删除行其他项目
		function delItemOther(obj){
		    var tr = this.getRowObj(obj);
		   if(tr != null){
		    tr.parentNode.removeChild(tr);
		    countFee();
		   }else{
		    throw new Error("the given object is not contained by the table");
		   }
		}
	//设置费用INPUT框
		function setFee(object) {
			//MyAlert(object.selectedIndex);
			var sum = 0;
			var options = object.options;
			for (var i=0;i<=object.selectedIndex;i++) {
				sum += parseFloat(options[i].value);
			}
			document.getElementById("fee").value=sum;
		}
		//设置费用INPUT框
		function setFee0(object) {
			var sum = 0;
			var flag = 0;
			for (var i = 0;i<arr3.length;i++) {
				if (object.value==arr3[i]){
					flag=1;
					document.getElementById("fee").value=arr2[i];
					document.getElementById("GAME_AMOUNT").innerText=arr2[i];
					break;
				}
			}
			if (flag==0) {
				document.getElementById("fee").value=='0';
				document.getElementById("GAME_AMOUNT").innerText='0';
			}
			sumAll();
		}
		//设定固定费用
		function setCheckbox(object) {
			var arr = object.value.split(",");
			document.getElementById("CAMPAIGN_CODE").value=arr[0];
			if (arr[1]=='undefined'||arr[1]=='') {
				tmp=0;
			}else {
				tmp = parseFloat(arr[1]);
			}
			
			sumAll();
			document.getElementById("CAMPAIGN_FEE").value=tmp;
			document.getElementById("IS_FIX").value=arr[2];
			if (arr[2]==1) { //1是固定费用
				document.getElementById("IS_FIX0").checked=true;
				//是固定费用需要隐藏掉工时和配件列表
				clearAllNode(document.getElementById('itemTable'));
				clearAllNode(document.getElementById('partTable'));
				clearAllNode(document.getElementById('otherTable'));
				document.getElementById('itemTableId').style.display='none';
    			document.getElementById('partTableId').style.display='none';
    			document.getElementById('otherTableId').style.display='none';
    			document.getElementById("BASE_LABOUR").innerText ='0.00';
				document.getElementById("BASE_LABOUR_AMOUNT").innerText ='0.00';
				document.getElementById("ALL_LABOUR_AMOUNT").innerText ='0.00';
				document.getElementById("ALL_PART_AMOUNT").innerText ='0.00';
    			document.getElementById("ACTIVITY_AMOUNT").innerText = tmp;
    			sumFunc();
			}else {
				document.getElementById("IS_FIX0").checked=false;
				clearAllNode(document.getElementById('itemTable'));
				clearAllNode(document.getElementById('partTable'));
				clearAllNode(document.getElementById('otherTable'));
				document.getElementById('itemTableId').style.display='';
    			document.getElementById('partTableId').style.display='';
    			document.getElementById('otherTableId').style.display='';
    			document.getElementById("ACTIVITY_AMOUNT").innerText ='0.00';
    			changeRepair(arr[0]);
				//changePart(arr[0]);
				changeOther(arr[0]);
				//document.getElementById("BASE_LABOUR").innerText = sumArr(document.getElementsByName("LABOUR_HOURS"));
				//document.getElementById("BASE_LABOUR_AMOUNT").innerText = sumArr(document.getElementsByName("LABOUR_AMOUNT"));
				//document.getElementById("ALL_LABOUR_AMOUNT").innerText = sumArr(document.getElementsByName("LABOUR_AMOUNT"))+sumArr(document.getElementsByName("LABOUR_AMOUNT0"));
				//document.getElementById("ALL_PART_AMOUNT").innerText = sumArr(document.getElementsByName("AMOUNT"));
				//sumAll();
				//SAN20100727003143
			}
		}
		//服务活动累计计算功能
		function sumFunc() {
			document.getElementById("BASE_LABOUR").innerText = sumArr(document.getElementsByName("LABOUR_HOURS"));
			document.getElementById("BASE_LABOUR_AMOUNT").innerText = sumArr(document.getElementsByName("LABOUR_AMOUNT"));
			document.getElementById("ALL_LABOUR_AMOUNT").innerText = accAdd(sumArr(document.getElementsByName("LABOUR_AMOUNT")),sumArr(document.getElementsByName("LABOUR_AMOUNT0")));
			document.getElementById("ALL_PART_AMOUNT").innerText = sumArr(document.getElementsByName("AMOUNT"));
			sumAll();
		}
		
		function setRepairTable () {
			var addTable = document.getElementById('itemTable');
			//MyAlert(addTable);
			var len  = addTable.rows.length;
			for (var i=0;i<len;i++) {
				myobj=addTable.rows[i];
				//MyAlert(addTable.rows[i].cells.item(1).childNodes[2].value);
				//联动故障代码
				chooseItem(addTable.rows[i].cells.item(1).childNodes[2].value);
			}
		}
		//通过服务活动带出的工时信息中，需要联动故障代码
		function setRepairTrouble (objec) {
			myobj = objec;
			//联动故障代码
			chooseItem2(objec.parentNode.parentNode.childNodes[1].childNodes[2].value);
		}
		//服务活动下拉框联动工时表
		function changeRepair(code) {
			var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getActivityRepairs.json';
	    	makeCall(url,dynaRepair,{CODE:code});
    	}
    	//服务活动下拉框联动配件表
    	function changePart(code) {
			var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getActivityParts.json';
	    	makeCall(url,dynaPart,{CODE:code});
    	}
    	//服务活动下拉框联动其他项目表
    	function changeOther(code) {
    	var yiedily = $("YIELDLY_TYPE").value;
    	var vin = $('VIN').value ;
			var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getActivityOthers.json?Uflag=1&yiedily='+yiedily+'&vin='+vin;
	    	makeCall(url,dynaOther,{CODE:code});
    	}
    	//回调
		function dynaRepair(json) {
    		var last=json.activityRepairs;
    		var len = last.length;
    		var innerHTML="";
    		var addTable = document.getElementById('itemTable');
			var rows = addTable.rows;
			for (var i=0;i<len;i++ ){
				var insertRow = addTable.insertRow(i);
				insertRow.className = "table_list_row1";
				insertRow.className = "table_edit";
				insertRow.insertCell(0);
				insertRow.insertCell(1);
				insertRow.insertCell(2);
				insertRow.insertCell(3);
				insertRow.insertCell(4);
				insertRow.insertCell(5);
				insertRow.insertCell(6);
				addTable.rows[i].cells[0].innerHTML =  '<td><span class="tbwhite"><a href="#"  onclick="selectTime();"></a></span><input name="MAIN_ITEM"  type="hidden" value="on"/><input type="text" class="phone_txt"  id="WR_LABOURCODE'+i+'" name="WR_LABOURCODE" readonly value="'+last[i].itemCode+'" size="10"/><a href="#"  onclick="selectMainTime(this);"></a></td>'
				setMustStyle([document.getElementById("WR_LABOURCODE"+i)]);
				addTable.rows[i].cells[1].innerHTML =  '<td><span class="tbwhite"><input type="text" name="WR_LABOURNAME"  value="'+last[i].itemName+'" size="10" readonly/></span></td>';
				addTable.rows[i].cells[2].innerHTML =  '<td><input type="text" name="LABOUR_HOURS" class="little_txt"   value="'+last[i].normalLabor+'" size="8" maxlength="11" id="PRICE" readonly/></td>';
				addTable.rows[i].cells[3].innerHTML =  '<td><input type="text" name="LABOUR_PRICE" class="little_txt"   value="'+last[i].parameterValue+'" size="8" maxlength="11" id="y20000001" readonly/></td>';
				addTable.rows[i].cells[4].innerHTML =  '<td><input type="text" name="LABOUR_AMOUNT" class="little_txt" readonly  value="'+last[i].sum+'" size="8" maxlength="9" readonly="true"/></td>';
				addTable.rows[i].cells[5].innerHTML =  '<td>'+genSelBoxExpPay("PAY_TYPE_ITEM",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'')+'</td>';
				addTable.rows[i].cells[6].innerHTML =  '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'itemTable\',this,\''+last[i].itemCode+'\');"/><input type="button" class="normal_btn" disabled value="删除" disabled name="button42" onClick="javascript:delItem(this);"/></td>';
				//addTable.rows[i].cells[6].style.display='none';
			}
			sumFunc();
		}
		//回调
		function dynaPart(json) {
			var last=json.activityParts;
    		var len = last.length;
    		var innerHTML="";
    		var addTable = document.getElementById('partTable');
			var rows = addTable.rows;
			for (var i=0;i<len;i++ ){
				var insertRow = addTable.insertRow(i);
				insertRow.className = "table_list_row1";
				insertRow.className = "table_edit";
				insertRow.insertCell(0);
				insertRow.insertCell(1);
				insertRow.insertCell(2);
				insertRow.insertCell(3);
				insertRow.insertCell(4);
				insertRow.insertCell(5);
				insertRow.insertCell(6);
				insertRow.insertCell(7);
				//insertRow.insertCell(8);
				if (actIsClaim=='0'){
					addTable.rows[i].cells[0].innerHTML =  '<td><input type="checkbox" disabled name="IS_GUA" checked/><input type="hidden" name="B_MP_CODE" value=""/><input name="PART0" type="hidden" checked="true" disabled onClick="if (this.checked) setTd(this);else clearTd(this);"/></td>';
					addTable.rows[i].cells[6].innerHTML = '<td>'+genSelBoxExpPay("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"<%=Constant.PAY_TYPE_02%>",false,"min_sel","","false",'')+'</td>';
				}else {
					addTable.rows[i].cells[0].innerHTML =  '<td><input type="checkbox" disabled/><input type="hidden" name="IS_GUA" value="off"/><input type="hidden" name="B_MP_CODE" value=""/><input name="PART0" type="hidden" checked="true" disabled onClick="if (this.checked) setTd(this);else clearTd(this);"/></td>';
					addTable.rows[i].cells[6].innerHTML = '<td>'+genSelBoxExpPay("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"<%=Constant.PAY_TYPE_01%>",false,"min_sel","","false",'')+'</td>';
				}
				addTable.rows[i].cells[1].innerHTML =  '<input type="text" class="phone_txt" name="PART_CODE"   value="'+last[i].partNo+'" size="10" id="PART_CODE" readonly="true"/><span class="tbwhite"><a href="#" onClick="javascript:selectPartCode(this)"></a></span>';
				addTable.rows[i].cells[2].innerHTML =  '<span class="tbwhite"><input type="text" class="phone_txt" name="PART_NAME" readonly value="'+last[i].partName+'" id="PART_NAME"  size="10"/></span>';
				addTable.rows[i].cells[3].innerHTML =  '<td><input type="text" class="short_txt" datatype="0,isDigit" onkeyup="countQuantity(this)" onblur="countQuantity(this)" size="10" name="QUANTITY" id="QUANTITY'+i+'" value="'+last[i].partQuantity+'" datatype="0,is_double" decimal="2" maxlength="20"/></td>';
				setMustStyle([document.getElementById("QUANTITY"+i)]);
				var supplierCode = last[i].supplierCode==null?"":last[i].supplierCode;
				var supplierName = last[i].supplierName==null?"":last[i].supplierName;
				addTable.rows[i].cells[4].innerHTML =  '<input type="text" class="little_txt" name="PRICE" value="'+last[i].partPrice+'" size="10" maxlength="11" id="PRICE" readonly/>';
				addTable.rows[i].cells[5].innerHTML =  '<td><input type="text" class="little_txt" name="AMOUNT" id="AMOUNT" value="'+last[i].partAmount+'" size="10" datatype="0,isMoney,10" maxlength="9" readonly /></td>';
				//addTable.rows[i].cells[6].innerHTML =  '<td>'+genSelBoxExpPay("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'')+'</td>';
				addTable.rows[i].cells[7].innerHTML = '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\''+last[i].partNo+'\');"/><input type="button" class="normal_btn" disabled value="删除" disabled name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
				//addTable.rows[i].cells[7].style.display='none';
			}
			//refreshAppTimeCombo();
			sumFunc();
		}
		//回调
		function dynaOther(json) {
			document.getElementById("GS_DOWN").value="";
			document.getElementById("CL_DOWN").value="";
			var last=json.activityOthers;
    		var len = last.length;
    		var innerHTML="";
    		var addTable = document.getElementById('otherTable');
			var rows = addTable.rows;
			for (var i=0;i<len;i++ ){
			if(last[i].proCode == 3537006){
				document.getElementById("GS_DOWN").value=last[i].amount;
				var insertRow = addTable.insertRow(i);
				insertRow.className = "table_list_row1";
				insertRow.className = "table_edit";
				insertRow.insertCell(0);
				insertRow.insertCell(1);
				insertRow.insertCell(2);
				insertRow.insertCell(3);
				insertRow.insertCell(4);
				insertRow.insertCell(5);
				addTable.rows[i].cells[0].innerHTML =  '<td><div align="center"><input type="hidden" name="ITEM_CODE" id="OTHER_CODE'+i+'" value="'+last[i].proCode+'" readonly/>'+last[i].proCode+'</div></td>';
				addTable.rows[i].cells[1].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="hidden" name="ITEM_NAME" id="ITEM_NAME'+i+'" readonly  value="'+last[i].proName+':'+last[i].amount/100+'折" /><a href="#" onclick="getPartDetai4('+last[i].activityId+')">'+last[i].proName+':'+last[i].amount/100+'折</a></span></div></td>';
				addTable.rows[i].cells[2].innerHTML =  '<td><div align="center"><input type="hidden" name="ITEM_AMOUNT" id="ITEM_AMOUNT'+i+'"  datatype="0,is_yuan" value="0" readonly class="short_txt"/>0</div></td>';
				addTable.rows[i].cells[3].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_REMARK"  id="ITEM_REMARK"  datatype="1,is_digit_letter_cn,100" class="middle_txt"  value="" /></span></div></td>';
				addTable.rows[i].cells[4].innerHTML =  '<td><input type="hidden" name="PAY_TYPE_OTHER" id="PAY_TYPE_OTHER"'+i+'" value="'+last[i].paid+'"/>'+last[i].paidName+'</td>';
				addTable.rows[i].cells[5].innerHTML =  '<td><div align="center"><input type="button" class="normal_btn" disabled="disabled"  value="删除"  name="button42" onClick="delItemOther(this);"/></div></td>';
				//addTable.rows[i].cells[5].style.display='none';
			}else if(last[i].proCode == 3537007){
				document.getElementById("CL_DOWN").value=last[i].amount;
					var insertRow = addTable.insertRow(i);
				insertRow.className = "table_list_row1";
				insertRow.className = "table_edit";
				insertRow.insertCell(0);
				insertRow.insertCell(1);
				insertRow.insertCell(2);
				insertRow.insertCell(3);
				insertRow.insertCell(4);
				insertRow.insertCell(5);
				addTable.rows[i].cells[0].innerHTML =  '<td><div align="center"><input type="hidden" name="ITEM_CODE" id="OTHER_CODE'+i+'" value="'+last[i].proCode+'" readonly/>'+last[i].proCode+'</div></td>';
				addTable.rows[i].cells[1].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="hidden" name="ITEM_NAME" id="ITEM_NAME'+i+'" readonly  value="'+last[i].proName+':'+last[i].amount/100+'折" /><a href="#" onclick="getPartDetai5('+last[i].activityId+')">'+last[i].proName+':'+last[i].amount/100+'折</a></span></div></td>';
				addTable.rows[i].cells[2].innerHTML =  '<td><div align="center"><input type="hidden" name="ITEM_AMOUNT" id="ITEM_AMOUNT'+i+'"  datatype="0,is_yuan" value="0" readonly class="short_txt"/>0</div></td>';
				addTable.rows[i].cells[3].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_REMARK"  id="ITEM_REMARK"  datatype="1,is_digit_letter_cn,100" class="middle_txt"  value="" /></span></div></td>';
				addTable.rows[i].cells[4].innerHTML =  '<td><input type="hidden" name="PAY_TYPE_OTHER" id="PAY_TYPE_OTHER"'+i+'" value="'+last[i].paid+'"/>'+last[i].paidName+'</td>';
				addTable.rows[i].cells[5].innerHTML =  '<td><div align="center"><input type="button" class="normal_btn" disabled="disabled"  value="删除"  name="button42" onClick="delItemOther(this);"/></div></td>';
				//addTable.rows[i].cells[5].style.display='none';
			}else if(last[i].proCode == 3537005||last[i].proCode == 3537003){
			var insertRow = addTable.insertRow(i);
				insertRow.className = "table_list_row1";
				insertRow.className = "table_edit";
				insertRow.insertCell(0);
				insertRow.insertCell(1);
				insertRow.insertCell(2);
				insertRow.insertCell(3);
				insertRow.insertCell(4);
				insertRow.insertCell(5);
				addTable.rows[i].cells[0].innerHTML =  '<td><div align="center"><input type="hidden" name="ITEM_CODE" id="OTHER_CODE'+i+'" value="'+last[i].proCode+'" readonly/>'+last[i].proCode+'</div></td>';
				addTable.rows[i].cells[1].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="hidden" name="ITEM_NAME" id="ITEM_NAME'+i+'" readonly  value="'+last[i].proName+'" /><a href="#" onclick="getPartDetai('+last[i].activityId+')">'+last[i].proName+'</a></span></div></td>';
				addTable.rows[i].cells[2].innerHTML =  '<td><div align="center"><input type="hidden" name="ITEM_AMOUNT" id="ITEM_AMOUNT'+i+'"  datatype="0,is_yuan" value="'+json.amounts+'" readonly class="short_txt"/>'+json.amounts+'</div></td>';
				addTable.rows[i].cells[3].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_REMARK"  id="ITEM_REMARK"  datatype="1,is_digit_letter_cn,100" class="middle_txt"  value="" /></span></div></td>';
				addTable.rows[i].cells[4].innerHTML =  '<td><input type="hidden" name="PAY_TYPE_OTHER" id="PAY_TYPE_OTHER"'+i+'" value="'+last[i].paid+'"/>'+last[i].paidName+'</td>';
				addTable.rows[i].cells[5].innerHTML =  '<td><div align="center"><input type="button" class="normal_btn" disabled="disabled"  value="删除"  name="button42" onClick="delItemOther(this);"/></div></td>';
				
			}else if(last[i].proCode == 3537001){
			var insertRow = addTable.insertRow(i);
				insertRow.className = "table_list_row1";
				insertRow.className = "table_edit";
				insertRow.insertCell(0);
				insertRow.insertCell(1);
				insertRow.insertCell(2);
				insertRow.insertCell(3);
				insertRow.insertCell(4);
				insertRow.insertCell(5);
				addTable.rows[i].cells[0].innerHTML =  '<td><div align="center"><input type="hidden" name="ITEM_CODE" id="OTHER_CODE'+i+'" value="'+last[i].proCode+'" readonly/>'+last[i].proCode+'</div></td>';
				addTable.rows[i].cells[1].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="hidden" name="ITEM_NAME" id="ITEM_NAME'+i+'" readonly  value="'+last[i].proName+'" /><a href="#" onclick="getPartDetai2('+last[i].activityId+')">'+last[i].proName+'</a></span></div></td>';
				addTable.rows[i].cells[2].innerHTML =  '<td><div align="center"><input type="hidden" name="ITEM_AMOUNT" id="ITEM_AMOUNT'+i+'"  datatype="0,is_yuan" value="'+last[i].amount+'" readonly class="short_txt"/>'+last[i].amount+'</div></td>';
				addTable.rows[i].cells[3].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_REMARK"  id="ITEM_REMARK"  datatype="1,is_digit_letter_cn,100" class="middle_txt"  value="" /></span></div></td>';
				addTable.rows[i].cells[4].innerHTML =  '<td><input type="hidden" name="PAY_TYPE_OTHER" id="PAY_TYPE_OTHER"'+i+'" value="'+last[i].paid+'"/>'+last[i].paidName+'</td>';
				addTable.rows[i].cells[5].innerHTML =  '<td><div align="center"><input type="button" class="normal_btn" disabled="disabled"  value="删除"  name="button42" onClick="delItemOther(this);"/></div></td>';
				
			} else if(last[i].proCode == 3537004){
				var insertRow = addTable.insertRow(i);
				insertRow.className = "table_list_row1";
				insertRow.className = "table_edit";
				insertRow.insertCell(0);
				insertRow.insertCell(1);
				insertRow.insertCell(2);
				insertRow.insertCell(3);
				insertRow.insertCell(4);
				insertRow.insertCell(5);
				addTable.rows[i].cells[0].innerHTML =  '<td><div align="center"><input type="hidden" name="ITEM_CODE" id="OTHER_CODE'+i+'" value="'+last[i].proCode+'" readonly/>'+last[i].proCode+'</div></td>';
				addTable.rows[i].cells[1].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="hidden" name="ITEM_NAME" id="ITEM_NAME'+i+'" readonly  value="'+last[i].proName+'" /><a href="#" onclick="getPartDetai3('+last[i].activityId+')">'+last[i].proName+'</a></span></div></td>';
				addTable.rows[i].cells[2].innerHTML =  '<td><div align="center"><input type="hidden" name="ITEM_AMOUNT" id="ITEM_AMOUNT'+i+'"  datatype="0,is_yuan" value="'+last[i].amount+'" readonly class="short_txt"/>'+last[i].amount+'</div></td>';
				addTable.rows[i].cells[3].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_REMARK"  id="ITEM_REMARK"  datatype="1,is_digit_letter_cn,100" class="middle_txt"  value="" /></span></div></td>';
				addTable.rows[i].cells[4].innerHTML =  '<td><input type="hidden" name="PAY_TYPE_OTHER" id="PAY_TYPE_OTHER"'+i+'" value="'+last[i].paid+'"/>'+last[i].paidName+'</td>';
				addTable.rows[i].cells[5].innerHTML =  '<td><div align="center"><input type="button" class="normal_btn" disabled="disabled"  value="删除"  name="button42" onClick="delItemOther(this);"/></div></td>';
				
			}else if(last[i].proCode == 3537002){
			$('byAmount').value=json.baoyAmount;
				var insertRow = addTable.insertRow(i);
				insertRow.className = "table_list_row1";
				insertRow.className = "table_edit";
				insertRow.insertCell(0);
				insertRow.insertCell(1);
				insertRow.insertCell(2);
				insertRow.insertCell(3);
				insertRow.insertCell(4);
				insertRow.insertCell(5);
				addTable.rows[i].cells[0].innerHTML =  '<td><div align="center"><input type="hidden" name="ITEM_CODE" id="OTHER_CODE'+i+'" value="'+last[i].proCode+'" readonly/>'+last[i].proCode+'</div></td>';
				addTable.rows[i].cells[1].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="hidden" name="ITEM_NAME" id="ITEM_NAME'+i+'" readonly  value="'+last[i].proName +':'+last[i].maintainTime +'次,并且为'+last[i].amount/100 +'折" />'+last[i].proName +':'+last[i].maintainTime +'次,并且为'+last[i].amount/100 +'折</span></div></td>';
				addTable.rows[i].cells[2].innerHTML =  '<td><div align="center"><input type="hidden" name="ITEM_AMOUNT" id="ITEM_AMOUNT'+i+'"  datatype="0,is_yuan" value="'+(json.baoyAmount*last[i].amount/100)+'" readonly class="short_txt"/>'+(json.baoyAmount*last[i].amount/100).toFixed(2)+'</div></td>';
				addTable.rows[i].cells[3].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_REMARK"  id="ITEM_REMARK"  datatype="1,is_digit_letter_cn,100" class="middle_txt"  value="此处为客户支付金额" /></span></div></td>';
				addTable.rows[i].cells[4].innerHTML =  '<td><input type="hidden" name="PAY_TYPE_OTHER" id="PAY_TYPE_OTHER"'+i+'" value="'+last[i].paid+'"/>'+last[i].paidName+'</td>';
				addTable.rows[i].cells[5].innerHTML =  '<td><div align="center"><input type="button" class="normal_btn" disabled="disabled"  value="删除"  name="button42" onClick="delItemOther(this);"/></div></td>';
			}
			}
			sumFunc();
		}
		
		function getPartDetai(id){
		var yiedlyType = document.getElementById("YIELDLY_TYPE").value;
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getPartDetai.do?id='+id+'&yiedlyType='+yiedlyType,800,500);
		}
		
		function getPartDetai2(id){
		var yiedlyType = document.getElementById("YIELDLY_TYPE").value;
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getJianche.do?id='+id+'&yiedlyType='+yiedlyType,800,500);
		}
		
		function getPartDetai3(id){
		var yiedlyType = document.getElementById("YIELDLY_TYPE").value;
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getZengSong.do?id='+id+'&yiedlyType='+yiedlyType,800,500);
		}
		function getPartDetai4(id){
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getLabour.do?id='+id,800,500);
		}
		function getPartDetai5(id){
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getPart.do?id='+id,800,500);
		}
		//
		function setActivity(activityCode,activityName,activityFee,isFixfee,isClaim,type,activityNum){
			document.getElementById("activityNum").value=activityNum;//单台次数
			//var arr = object.value.split(",");
			actIsClaim = isClaim;
			document.getElementById("ACTIVITYCOMBO").value=activityName;
			var arr = [activityCode,activityFee,isFixfee];
			document.getElementById("CAMPAIGN_CODE").value=arr[0];
			document.getElementById("Activity_Type").value=type;
			if (arr[1]=='undefined'||arr[1]=='') {
				tmp=0;
			}else {
				tmp = parseFloat(arr[1]);
			}
			
			sumAll();
			document.getElementById("CAMPAIGN_FEE").value=tmp;
			document.getElementById("IS_FIX").value=arr[2];
		
				document.getElementById("IS_FIX0").checked=false;
				clearAllNode(document.getElementById('itemTable'));
				clearAllNode(document.getElementById('partTable'));
				clearAllNode(document.getElementById('otherTable'));
				document.getElementById('itemTableId').style.display='';
    			document.getElementById('partTableId').style.display='';
    			document.getElementById('otherTableId').style.display='';
    			document.getElementById("ACTIVITY_AMOUNT").innerText ='0.00';
    			changeRepair(arr[0]);
				//changePart(arr[0]);
				changeOther(arr[0]);
			//}
			$('IN_MILEAGE').readOnly  =true;
		}
		
		//活动为替换件的时候设置维修工时和维修配件
		function setActivityData(labouritemList,partsitemList,d1,d2,d3,d4,d5,d6,d7,d8,d9,d10,d11){
			//数据处理
			setActivity(d1,d2,d3,d4,d5,d6,d11);
			//设置维修工时信息
			if(labouritemList != null && labouritemList.length > 0){
				for(var i = 0;i < labouritemList.length;i++){
					addRow('itemTable');
					var tb = document.getElementById("itemTable");
					var myobj = tb.childNodes[i];
					
					//设置值
					//setMainTime工时选择
					//setMainMal故障代码选择
					myobj.cells.item(0).innerHTML='<td><input name="MAIN_ITEM"  type="hidden" value="on"/><input type="text" class="phone_txt"   name="WR_LABOURCODE" readonly value="'+0+'" size="10"/><span style="color:red">*</span><a href="#" style="display:none;"  onclick="selectMainTime(this);">选择</a></td>'
					myobj.cells.item(1).innerHTML='<td><span class="tbwhite"><input type="text" class="long_txt" name="WR_LABOURNAME"  value="'+0+'" size="10" readonly/><input type="hidden" class="long_txt" name="FORE"  value="'+0+'" size="10" readonly/></span></td>';
					myobj.cells.item(2).innerHTML='<td><input type="text" name="LABOUR_HOURS" class="little_txt"   value="'+0+'" size="8" maxlength="11" id="PRICE" readonly/></td>';
					myobj.cells.item(3).innerHTML='<td><input type="text" name="LABOUR_PRICE" class="little_txt"   value="'+0+'" size="8" maxlength="11" id="y20000001" readonly/></td>';
					myobj.cells.item(4).innerHTML='<td><input type="text" name="LABOUR_AMOUNT" class="little_txt" readonly  value="'+0+'" size="8" maxlength="9" readonly="true"/></td>';
					myobj.cells.item(6).innerHTML='<td><input disabled="disabled" id="MALFUNCTIONS" name="MALFUNCTIONS" class="middle_txt" value="" /><input type="hidden" id="MALFUNCTION" name="MALFUNCTION" class="middle_txt" value="" /><a href="#" style="display:none;" name="choiseLabouritem" onclick="selectMalCode(this);">选择</a></td>';
					myobj.cells.item(7).innerHTML =  '<td><input type="hidden"  class="little_txt"  value="'+0+'"  name="LEVELS"/><input type="button" class="normal_btn"  value="删除" disabled  name="button42" onClick="javascript:delItem(this);"/></td>';
					
					document.getElementsByName("WR_LABOURCODE")[i].value = labouritemList[i].WR_LABOURCODE;
					document.getElementsByName("WR_LABOURNAME")[i].value = labouritemList[i].WR_LABOURNAME;
					document.getElementsByName("LABOUR_HOURS")[i].value = "0";
					document.getElementsByName("LABOUR_PRICE")[i].value = "0";
					document.getElementsByName("LABOUR_AMOUNT")[i].value = labouritemList[i].LABOUR_AMOUNT;
					document.getElementsByName("FORE")[i].value = "10041002";
					document.getElementsByName("LEVELS")[i].value = "";
					document.getElementsByName("MALFUNCTIONS")[i].value = labouritemList[i].MAL_FUNCTION_VALUE;
					document.getElementsByName("MALFUNCTION")[i].value = labouritemList[i].MAL_FUNCTION_ID;
					document.getElementsByName("PAY_TYPE_ITEM")[i].value = labouritemList[i].PAY_TYPE;
					//移除多余项目
					removeOption("PAY_TYPE_ITEM",i,labouritemList[i].PAY_TYPE);
// 					String[] wrLabourCodes = request.getParamValues("WR_LABOURCODE"); // 获取工时代码[11040010101, 33030060101]
// 					String[] wrLabournames = request.getParamValues("WR_LABOURNAME"); // 获取工时名称[PCV阀—更换, 变速器壳体—更换]
// 					String[] labourHours = request.getParamValues("LABOUR_HOURS"); // 获取工时数[0.4, 4.2]
// 					String[] labourPrices = request.getParamValues("LABOUR_PRICE"); // 获取工时单价[50, 50]
// 					String[] labourAmounts = request.getParamValues("LABOUR_AMOUNT"); // 获取工时金额[20.00, 210.00]
// 					String[] fore  = request.getParamValues("FORE");//获取工时是否需要预授权[10041002, 10041002]
// 					String[] level = request.getParamValues("LEVELS");//取得需要预授权的层级null null
// 					String[] malFunction = request.getParamValues("MALFUNCTION");//故障代码[2014031894324612, 2014031894324613]
// 					String[] itemPayType = request.getParamValues("PAY_TYPE_ITEM");//付费方式[11801002, 11801002]
				}
			}
			//设置维修配件信息
			var codeArray = new Array();
			var nameArray = new Array();
			if(partsitemList != null && partsitemList.length > 0){
				for(var i = 0;i < partsitemList.length;i++){
					addRow('partTable');
					//设置值
					//setMainPartCode新件代码选择
					//setPartCodes 刷新全局数据
					var tb = document.getElementById("partTable");
					var myobj = tb.childNodes[i];
					myobj.cells.item(1).innerHTML='<input type="text" class="phone_txt PART_CODE" name="PART_CODE"   value="'+0+'" size="10" id="PART_CODE" readonly="true"/><span style="color:red">*</span><span class="tbwhite"><a href="#" style="display:none;" onClick="javascript:selectMainPartCode(this)">选择</a></span>';
					myobj.cells.item(2).innerHTML='<span class="tbwhite"><input type="text" class="phone_txt" name="PART_NAME" readonly value="'+0+'" id="PART_NAME"  size="10"/></span>';
					myobj.cells.item(4).innerHTML='<input type="text" class="little_txt" name="PRICE" value="'+0+'" size="10" maxlength="11" id="PRICE" readonly/>';
					myobj.cells.item(5).innerHTML='<input type="text" class="little_txt" name="AMOUNT" value="'+0+'" size="10" maxlength="11" readonly />';
					myobj.cells.item(15).innerHTML='<td><input type="hidden"  class="little_txt"  value="'+0+'"  name="PARTLEVEL" /><input type="hidden"  class="little_txt"  value="'+0+'"  name="PARTFORE" /><input type="text"  class="little_txt" readOnly="true" value="" name="zfRoNo" /><input type="hidden"  class="little_txt"  value="'+0+'"  name="REAL_PART_ID"/></td>';
					
					document.getElementsByName("PARTFORE")[i].value = "10041002";
					document.getElementsByName("PARTLEVEL")[i].value = "";
					document.getElementsByName("PART_CODE")[i].value = partsitemList[i].PART_CODE;
					document.getElementsByName("PART_NAME")[i].value = partsitemList[i].PART_NAME;
					document.getElementsByName("QUANTITY")[i].value = partsitemList[i].QUANTITY;
					document.getElementsByName("PRICE")[i].value = "0";
					document.getElementsByName("AMOUNT")[i].value = "0";
					document.getElementsByName("PAY_TYPE_PART")[i].value = partsitemList[i].PAY_TYPE;
					document.getElementsByName("TROUBLE_DESCRIBE")[i].value = partsitemList[i].TROUBLE_DESC;
					document.getElementsByName("TROUBLE_REASON")[i].value = partsitemList[i].TROUBLE_REASON;
					document.getElementsByName("DEAL_METHOD")[i].value = partsitemList[i].REPAIR_METHOD;
					var respons = document.getElementsByName("RESPONS_NATURE")[i];
					for(var j = 0;j < respons.length;j++){
						if(respons[j].value == partsitemList[i].RESPONSIBILITY_TYPE){
							respons[j].selected = true;
							break;
						}
					}
					var labour0 = document.getElementsByName("Labour0")[i];
					var lbCode = document.getElementsByName("WR_LABOURCODE");
					var lbName = document.getElementsByName("WR_LABOURNAME");
					for(var j = 0;j < lbCode.length;j++){
						if(lbCode[j].value == partsitemList[i].LABOUR_CODE){
							codeArray[i] = partsitemList[i].LABOUR_CODE;
							nameArray[i] = lbName[j].value;;
							break;
						}
					}
					document.getElementsByName("HAS_PART")[i].value = partsitemList[i].HAS_PART;
					document.getElementsByName("PART_USE_TYPE")[i].value = partsitemList[i].PART_USE_TYPE;
					document.getElementsByName("IS_GUA")[i].value = partsitemList[i].IS_OLD_CLAIM_PRINT;
					document.getElementsByName("REAL_PART_ID")[i].value = partsitemList[i].PART_ID;
					
					//移除多余项目
					removeOption("PAY_TYPE_PART",i,partsitemList[i].PAY_TYPE);
					removeOption("RESPONS_NATURE",i,partsitemList[i].RESPONSIBILITY_TYPE);
					removeOption("HAS_PART",i,partsitemList[i].HAS_PART);
					removeOption("PART_USE_TYPE",i,partsitemList[i].PART_USE_TYPE);
// 					setPartCodes();
// 					String[] partFore = request.getParamValues("PARTFORE");//获取配件是否预授权、[10041002, 10041002]
// 					String[] partLevel = request.getParamValues("PARTLEVEL");//获取配件预授权层级null null
// 					String[] partCodes = request.getParamValues("PART_CODE"); // 上件代码[63090014-B01-B00, 63050410-B41-B00]
// 					String[] partNames = request.getParamValues("PART_NAME"); // 上件名称[背门气撑杆上球头销支架组件(右), 背门外开手柄总成（镀铬）]
// 					String[] quantitys = request.getParamValues("QUANTITY"); // 上件数量[1, 1]
// 					String[] prices = request.getParamValues("PRICE"); // 单价[11.2, 53.76]
// 					String[] amounts = request.getParamValues("AMOUNT"); // 数量[11.20, 53.76]
// 					String[] labour = request.getParamValues("Labour0");//所属作业代码[11040010101, 33030060101]
// 					String[] nature = request.getParamValues("RESPONS_NATURE");//质损性质[94001001, 94001002]
// 					String[] mainPartCode = request.getParamValues("mainPartCode");//次因件关联主因件[-1, 63090014-B01-B00]
// 					String[] partPayType = request.getParamValues("PAY_TYPE_PART"); //配件付费方式[11801002, 11801002]
// 					String[] partUseType = request.getParamValues("PART_USE_TYPE");//获取配件的使用类型[95431002, 95431002]
// 					String[] guaNotice = request.getParamValues("IS_GUA");//[1, 1]
// 					String[] realPartId = request.getParamValues("REAL_PART_ID");//获取选择的配件ID[2014031303901077, 2014031303901068]
// 					String[] hasPart = request.getParamValues("HAS_PART");//得到配件是否有库存[10041001, 10041001]
				}
			}
			
			var labour0s = document.getElementsByName("Labour0");
			for(var j = 0;j < labour0s.length;j++){
				labour0s[j].options.add(new Option(nameArray[j],codeArray[j]));
				if(partsitemList[j].MAIN_PART_CODE == "-1"){
					document.getElementsByName("mainPartCode")[j].options.add(new Option("---请选择---","-1"));
				}else{
					var partCodes = document.getElementsByName("PART_CODE");
					var partNames = document.getElementsByName("PART_NAME");
					for(var k = 0;k < partCodes.length;k++){
						if(partCodes[k].value == partsitemList[j].MAIN_PART_CODE){
							document.getElementsByName("mainPartCode")[j].options.add(new Option(partNames[k].value,partsitemList[j].MAIN_PART_CODE));
							break;
						}
					}
				}
			}
			
			for(var j = 0;j < labour0s.length;j++){
				removeOption("Labour0",j,codeArray[j]);
				removeOption("mainPartCode",j,partsitemList[j].MAIN_PART_CODE);
			}
			
			
			//设置只读属性
			document.getElementById("itemBtn").disabled = "disabled";
			document.getElementById("partBtn").disabled = "disabled";
			var delBtns = document.getElementsByName("button42");
			for(var i = 0;i < delBtns.length;i++){
				delBtns[i].disabled = "disabled";
			}
			
			setIsEnable("itemTable",true);
			setIsEnable("partTable",true);
		}
		
		//移除多余数据
		function removeOption(selName,i,value){
			var sel = document.getElementsByName(selName)[i];
			var rvList = new Array();
			var k = 0;
			for(var j = 0;j < sel.options.length;j++){
				if(sel.options[j].value != value){
					rvList[k] = j;
					k++;
				}
			}
			for(var j = 0;j < rvList.length;j++){
				sel.remove(rvList[j]);
			}
		}
		
		function setIsEnable(tableId,isReadonly){
			var trs = document.getElementById(tableId).childNodes;
			for(var i = 0;i < trs.length;i++){
				var tds = trs[i].childNodes;
				for(var j = 0;j < tds.length;j++){
					var widgets = tds[j].childNodes;
					for(var k = 0;k < widgets.length;k++){
						try{
							widgets[k].readOnly = isReadonly;
						} catch(e){}
					}
				}
			}
		}
		
		function setBtnEnable(){
			//移除数据
			//clearAllNode("itemTable");
			//clearAllNode("partTable");
			document.getElementById("itemBtn").disabled = "";
			document.getElementById("partBtn").disabled = "";
			
		}
var arrvins = new Array();
function get(){
if(<%=scan%> ==0 ){
	var iKeyCode = window.event.keyCode;
	if(iKeyCode != 46 && iKeyCode != 8) {
	   var ddate = new Date();
	   arrvins[arrvins.length] = ddate.getTime();
	}
	}
	return true;
}

</SCRIPT>
	</HEAD>
	<BODY onload="doInit();setMyBtnStyle();">
		<div class="navigation">
			<img src="../../img/nav.gif" />
			&nbsp;当前位置：售后服务管理&gt;维修登记&gt;维修工单登记
		</div>

		<form method="post" name="fm" id="fm">
			<input type="hidden" name="activityNum" id="activityNum" value=""/>
			<input type="hidden" name="activityBoolean" id="activityBoolean" value=""/>
			<input type="hidden" name="Activity_Type" id="Activity_Type" value=""/>
			<input type="hidden" name="car_use_type" id="car_use_type" value=""/>
			<input type="hidden" name="reqGuId" value="<%=request.getSession().getAttribute("GuId") %>"/>
			<input type="hidden" name="RO_NO" id="roNo" value=""/>
			<input type="hidden" name="APPROVAL_YN" id="APPROVAL_YN" value=""/>
			<input type="hidden" name="CL_DOWN" id="CL_DOWN" value=""/>
			<input type="hidden" name="GS_DOWN" id="GS_DOWN" value=""/>
			<input type="hidden" name="mastMileage" id="mastMileage" value=""/>
			<input type="hidden" name="BRAND_NAME0" id="BRAND_NAME0" value=""/>
			<input type="hidden" name="SERIES_NAME0" id="SERIES_NAME0" value=""/>
			<input type="hidden" name="MODEL_NAME0" id="MODEL_NAME0" value=""/>
			<input type="hidden" name="ENGINE_NO0" id="ENGINE_NO0" value=""/>
			<input type="hidden" name="REARAXLE_NO0" id="REARAXLE_NO0" value=""/>
			<input type="hidden" name="GEARBOX_NO0" id="GEARBOX_NO0" value=""/>
			<input type="hidden" name="TRANSFER_NO0" id="TRANSFER_NO0" value=""/>
			<input type="hidden" name="BRAND_CODE0" id="BRAND_CODE0" value=""/>
			<input type="hidden" name="SERIES_CODE0" id="SERIES_CODE0" value=""/>
			<input type="hidden" name="MODEL_CODE0" id="MODEL_CODE0" value=""/>
			<input type="hidden" name="ENGINE_NO" id="ENGINE_NO" value=""   />
			<input type="hidden" name="DELIVERER_ADRESSS" id="DELIVERER_ADRESSS" value=""   />
			<input type="hidden" name="saleInfo" id="saleInfo" value=""/>
			<input type="hidden" name="pdiInfo" id="pdiInfo" value=""/>
			<input type="hidden" name="timeOut" id="timeOut" value=""/>
			<input type="hidden" name="Wr_Melieage" id="Wr_Melieage" value=""/>
			<input type="hidden" name="hasWr" id="hasWr" value="1"/>
			<input type="hidden" name="timeId" id="timeId" />
			<input type="hidden" name="byAmount" id="byAmount" value="0"/>
			
			<!-- 判定用 -->
			<input type="hidden" name="codes" id="codes" />
			<input type="hidden" name="codes_type" id="codes_type" />
			<input type="hidden" name="labcodes" id="labcodes" />
			<input type="hidden" name="labcodes_type" id="labcodes_type" />
			
			<input type="hidden" name="list01" id="list01"
				value="<%=request.getAttribute("BUSINESS_CHNG_CODE_01")%>" />
			<input type="hidden" name="list02" id="list02"
				value="<%=request.getAttribute("BUSINESS_CHNG_CODE_02")%>" />
			<input type="hidden" name="list03" id="list03"
				value="<%=request.getAttribute("BUSINESS_CHNG_CODE_03")%>" />
			<input type="hidden" name="list04" id="list04"
				value="<%=request.getAttribute("BUSINESS_CHNG_CODE_04")%>" />
			<input type="hidden" name="OTHERFEE" id="OTHERFEE"
				value="<%=request.getAttribute("OTHERFEE")%>" />
			<input type="hidden" name="brand" id="brand"
				value="<%=request.getAttribute("brand")%>" />
			<input type="hidden" name="series" id="series"
				value="<%=request.getAttribute("series")%>" />
			<input type="hidden" name="model" id="model"
				value="<%=request.getAttribute("model")%>" />
			<!-- <input type="hidden" name="ACTIVITYCOMBO0" id="ACTIVITYCOMBO0"
				value="<%//= request.getAttribute("ACTIVITYCOMBO")%>" />
			 -->	
				<input type="hidden" name="DEALER_CODE" id="DEALER_CODE"
				value="<%=request.getAttribute("dealerCode")%>" />
			<table border="0" align="center" cellpadding="1" cellspacing="1"
				class="table_edit">
				<tr id="noticeInfo" style="display: none">
				<td colspan="6" style="color: red" align="center">
					PDI检测,只能是车辆在验收入库开始7个工作日内有效。
				</td>
				</tr>
				<th colspan="6">
					<img class="nav" src="../../img/subNav.gif" />
					基本信息
				</th>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						经销商代码：
					</td>
					<td align="left">
						<%=request.getAttribute("dealerCode")%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						经销商名称：
					</td>
					<td align="left">
						<%=request.getAttribute("dealerName")%>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						经销商电话：
					</td>
					<td>
						<%=request.getAttribute("phone")%>
					</td>
						<td class="table_edit_2Col_label_7Letter">
						维修类型：
					</td>
					<td>
				 <script type="text/javascript">
	              		genSelBoxExp("REPAIR_TYPE",<%=Constant.REPAIR_TYPE%>,"",false,"short_sel","","false",'');
	       			</script> 
					</td>
					<!--  <td class="table_edit_2Col_label_7Letter">
						工单号：
					</td>
					<td align="left">-->
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						系统里程数：
					</td>
					<td align="left" id="LAST_MILEAGE">
					
					</td>
					
					<td class="table_edit_2Col_label_7Letter">
						工单开始时间：
					</td >
					<td nowrap="nowrap" class="">
					<input type="hidden" name="RO_STARTDATE"  id="RO_STARTDATE" value="${now }"/>
						${now }
					</td>
				
					<td class="table_edit_2Col_label_7Letter" style="display: none" >
						预计工单结束时间：
					</td>
					<td  style="display: none">
						<input type="text" name="RO_ENDDATE" id="RO_ENDDATE" value="${now }"
							class="short_txt" datatype="0,is_date,10"
							group="RO_STARTDATE,RO_ENDDATE" hasbtn="true"
							callFunction="showcalendar(event, 'RO_ENDDATE', false);" />
					</td>
					
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						进厂里程数：
					</td>
					<td>
						<input type='text' name='IN_MILEAGE' onchange="changeBtn();" id='IN_MILEAGE' maxlength="8"
							class="middle_txt" blurback="true" datatype="0,is_double,8" />
					</td>
					<td class="table_edit_2Col_label_7Letter">
						索  赔  员：
					</td>
					<td>
					<input type='text' maxlength="15"
							 class="middle_txt" value="<%=logonUser.getName()%>" />
						<input type='hidden' name='SERVE_ADVISOR' id='SERVE_ADVISOR' maxlength="15"
							 class="middle_txt" value="<%=request.getAttribute("dealerCode")%>" />
					</td>
				</tr>
				
				<tr>
				<td class="table_edit_2Col_label_7Letter">
						结算基地：
					</td>
					<td>
					 <script type="text/javascript">
				            genSelBoxExp("YIELDLY_TYPE",<%=Constant.PART_IS_CHANGHE%>,"<%=Constant.PART_IS_CHANGHE_01%>",false,"short_sel","","false",'');
				        </script>
					</td>
					<td class="table_edit_2Col_label_7Letter">
					</td>
					<td>
						<input type="hidden" name="QUELITY_GRATE" value="95551001"/>
					 <%-- <script type="text/javascript">
						质量等级：
				            genSelBoxExp("QUELITY_GRATE",<%=Constant.QUELITY_GRATE%>,"",true,"short_sel","","true",'');
				        </script> --%>
					</td>
					<td style="display: none" class="table_edit_2Col_label_7Letter">
						VIN录入是否手输：
					</td>
					<td style="display: none">
					 <script type="text/javascript">
				            genSelBoxExp("VIN_INTYPE",<%=Constant.IF_TYPE%>,"<%=Constant.IF_TYPE_NO%>",false,"short_sel","","false",'');
				        </script>
					</td>
				</tr>
					<tr id="activity" style="display:none">
					<td id="activityTableId" align="right">
						活动编号：</td>
						<td id="activityTableId0">
						<input type="text" value="" id="ACTIVITYCOMBO" name="ACTIVITYCOMBO" onpropertychange="javascript:setBtnEnable();" readonly="readonly"/>
						<a href="#" onclick="showActivity();">选择</a>
						</td>
						<td id="activityTableId1" style="display: none">
						是否固定费用：
						<input type="checkbox" id="IS_FIX0" name="IS_FIX0" disabled/>
						<input type="hidden" id="IS_FIX" name="IS_FIX" value="" />
						<input type="hidden" id="CAMPAIGN_FEE" name="CAMPAIGN_FEE" value="" />
						<input type="hidden" id="CAMPAIGN_CODE" name="CAMPAIGN_CODE" value="" />
					</td>
					</tr>
					<tr id="freeTime" style="display:none">
					<td colspan="4" align="center">
					需要第<input readonly="true" type="text" class="mini_txt" value="1" name="freeTimes" id="freeTimes"/>次保养
					<font color="red" id="cost"></font>
					</td>
					</tr>
			</table>
			
			<table class="table_edit">
				<th colspan="6">
					<img class="nav" src="../../img/subNav.gif" />
					车辆信息
				</th>
				<tr>
					<td align="right" class="table_edit_3Col_label_7Letter"> VIN： </td>
					<td>
						<input type='text' name='VIN' id='VIN'     maxlength="18"
							 class="middle_txt" 
							<% if(scan ==0){%> 
							 onkeydown="get()" onpaste="return false"
							<%  } %>
							 datatype="0,is_vin" blurback="true"/>
						<!--  <a href="#" onClick="javascript:checkVin();">检查</a>-->
					</td>
					<td align="right" class="table_edit_3Col_label_7Letter">
						<span class="zi">发动机号：</span>					</td>
					<td align="left" id="ENGINE_NOS">
							</td>
					<td align="right" class="table_edit_3Col_label_7Letter">
						<span class="zi">牌照号：</span>					</td>
					<td>
						<input type="text" value="" name="LICENSE_NO" id="LICENSE_NO" maxlength="10"
							class="short_txt" />
						<!--  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
					</td>
				</tr>
				<tr>
					<td align="right" class="table_edit_3Col_label_7Letter">
						品牌：					</td>
					<td align="left" id="BRAND_NAME">

					</td>
					<td align="right" class="table_edit_3Col_label_7Letter">
						车系：					</td>
					<td id="SERIES_NAME">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td align="right" class="table_edit_3Col_label_7Letter">
						车型：					</td>
					<td id="MODEL_NAME">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
				<tr>
				<td align="right" class="table_edit_3Col_label_7Letter">
						<span class="zi">产地：</span> </td>
					
					<td id="YIELDLY"></td>
					
					<td align="right" class="table_edit_2Col_label_7Letter">
						<span class="zi">购车日期：</span>					</td>
					<td id="GUARANTEE_DATE_ID">
					</td>
					<td align="right" class="table_edit_2Col_label_7Letter">
						<span class="zi">生产日期：</span>					</td>
					<td id="PRODUCT_DATE_ID">
					</td>
					<input type="hidden" name="GUARANTEE_DATE" id="GUARANTEE_DATE"
							class="short_txt"  hasbtn="true"
							callFunction="showcalendar(event, 'GUARANTEE_DATE', false);" />
				</tr>
				<tr>
					<td align="right" class="table_edit_3Col_label_7Letter">车主姓名：</td>
					<td id="CTM_NAME">
					</td>
					<input type="hidden" id="CTM_NAME_1"  name="CTM_NAME_1" />
					<td align="right" class="table_edit_3Col_label_7Letter">车主电话：</td>
					<td id="MAIN_PHONE"></td>
					<td align="right" class="table_edit_3Col_label_7Letter">三包规则代码：</td>
					<td id="GUARANTEE_CODE"></td>
				</tr>
				<tr>
					<td align="right" class="table_edit_3Col_label_7Letter">配置：</td>
					<td id="packageName"></td>
					<td class="table_edit_3Col_label_6Letter">
						<span class="zi">车主地址：</span>
					</td>
					<td id="DELIVERER_ADRESS"> 
					</td>
					
					<td align="right" class="table_edit_3Col_label_7Letter">车辆用途：</td>
					<td id="car_use_desc"></td>
				</tr>
				<tr>
					<td align="right" class="table_edit_3Col_label_7Letter">颜色：</td>
					<td id="car_color"></td>
					<td class="table_edit_3Col_label_6Letter">
					</td>
					<td >
					</td>
					
					<td align="right" class="table_edit_3Col_label_7Letter"></td>
					<td ></td>
				</tr>
				
			</table>
			<table class="table_edit">
				<th colspan="6">
					<img class="nav" src="../../img/subNav.gif" />
					用户信息
				</th>
				<tr>
					<td class="table_edit_3Col_label_7Letter">
						送修人姓名：
					</td>
					<td>
						<input type='text' name='DELIVERER' id='DELIVERER' maxlength="100"   datatype="0,is_textarea,100" class="middle_txt"/>
						<!--  <a href="#" onClick="javascript:checkVin();">检查</a>-->
					</td>
					<td class="table_edit_3Col_label_6Letter">
						<span class="zi">送修人电话：</span>
					</td>
					<td align="left" >
						<input type="text" name="DELIVERER_PHONE" maxlength="17" id="DELIVERER_PHONE" class="middle_txt" />
					</td>
				</tr>
				<tr>
					<td class="table_edit_3Col_label_7Letter">
						送修人手机：
					</td>
					<td>
						<input type='text' name='DELIVERER_MOBILE' id='DELIVERER_MOBILE' 
							 class="middle_txt" datatype="0,is_digit,11" maxlength="11"/>
						<!--  <a href="#" onClick="javascript:checkVin();">检查</a>-->
					</td>
					
				</tr>
			</table>
	<!-- 添加附件 -->
			<table class="table_info" border="0" id="file" style="display:none;">
				<input type="hidden" id="fjids" name="fjids" />
				<tr colspan="8">
					<th>
						<img class="nav" src="../../img/subNav.gif" />
						&nbsp;附件列表：
					</th>
					<th>
						<span align="left"><input type="button" class="normal_btn"
								onclick="showUpload('<%=contextPath%>')" value='添加附件' /> </span>
					</th>
				</tr>
				<!--  
				<tr>
					<td width="100%" colspan="2"><jsp:include
							page="${contextPath}/uploadDiv.jsp" /></td>
				</tr>
				-->
			</table>
			
			<table id="itemTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<th colspan="11" align="left">
					<img class="nav" src="../../img/subNav.gif" />
					维修工时
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						作业代码
					</td>
					<td>
						作业名称
					</td>
					<td>
						工时定额
					</td>
					<td>
						工时单价
					</td>
					<td>
						工时金额(元)
					</td>
					<td>
						付费方式
					</td>
					<td>
						故障代码
					</td>
					<td>
						<input id="itemBtn" type="button" class="normal_btn" value="新增"
							name="button422" onClick="javascript:addRow('itemTable');" />
					</td>
				</tr>
				<tbody id="itemTable">
				</tbody>
			</table>

			<table id="partTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list">

				<th colspan="18" align="left">
					<img class="nav" src="../../img/subNav.gif" />
					维修配件
				</th>
				<tr align="center" class="table_list_row1">
					<td style="width: 24px;">
						是否三包
					</td>
					<td >
						新件代码
					</td>
					<td>
						新件名称
					</td>
					<td style="width: 26px;">
						新件数量
					</td>
					<td>
						单价
					</td>
					<td>
						金额(元)
					</td>
					<td>
						付费方式
					</td>
					<td>
						作业代码
					</td>
					<td>
						关联主因件
					</td>
					<td  >
						责任性质
					</td>
					<td >
						配件是否有库存
					</td>
					<td>
					配件维修类型
					</td>
					<td>
					故障描述
					</td>
					<td>
					故障原因
					</td>
					<td>
					维修措施
					</td>
					<td >
					自费工单
					</td>
					<td>
						<input id="partBtn" type="button" class="normal_btn" value="新增"
							name="button422" onClick="javascript:genAppTimeCombo1(addRow('partTable'));" />
					</td>
				</tr>
				<tbody id="partTable">
				
				</tbody>
				
			</table>
			<table id='fjtable' width="100%" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list" >
			<tr id="hideTr" style="display: none">
	        		<th>
						<input type="hidden" id="fjids" name="fjids"/>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
			     		<input type="button" class="normal_btn" align="right" id="addfile" onclick="showUpload('<%=contextPath%>')" value ='添加附件'/><font color="red">*</font>
					</th>
				</tr>
				<tr>
					<td width="100%" colspan="9"><jsp:include
							page="${contextPath}/uploadDiv2.jsp" /></td>
				</tr>
			</table>
			<table id='otherTableId' border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list">
				<th colspan="8" align="left">
					<img class="nav" src="../../img/subNav.gif" />
					其他项目
				</th>
				<tr class="table_list_row1">
					<td>
						<div align="center">
							项目代码
						</div>
					</td>
					<td>
						<div align="center">
							项目名称
						</div>
					</td>
					<td>
						<div align="center">
							金额(元)
						</div>
					</td>
					<td id="RemarkPDI">
						备注
					</td> 
					<td>
						付费方式
					</td>
					<td>
						<input id="otherBtn" type="button" class="normal_btn" value="新增"
							name="button422" onClick="javascript:addRow('otherTable');" />
					</td>
				</tr>
				<tbody id="otherTable">
				</tbody>
			</table>
			<TABLE class="table_edit" style="display:none">
			<tr>
              <th colspan="10"  ><img src="../../img/subNav.gif" alt="" class="nav" />
              申请费用</th>
            </tr>
            </TABLE>
			<table class="table_edit" style="display:none">
            <tr>
              <td  class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >基本工时：</td>
              <td id="BASE_LABOUR">0.0</td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >基本工时金额：</td>
              <td  id="BASE_LABOUR_AMOUNT" >0.0</td>
              <td  class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >附加工时：</td>
              <td  id="ADD_LABOUR">0.0</td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >附加工时金额：</td>
              <td id="ADD_LABOUR_AMOUNT">0.0</td>
            </tr>
            <tr>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >配件金额：</td>
              <td id="ALL_PART_AMOUNT">0.0</td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >工时金额：</td>
              <td id="ALL_LABOUR_AMOUNT">0.0</td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >其它费用金额：</td>
              <td id="OTHER_AMOUNT">0.0</td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >保养金额：</td>
              <td id="GAME_AMOUNT">0.0</td>
            </tr>
            <tr>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >服务活动金额：</td>
              <td id="ACTIVITY_AMOUNT">0.0</td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >索赔申请金额：</td>
              <td id="APPLY_AMOUNT">0.0</td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >税额：</td>
              <td id="TAX">0.0</td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >总金额：</td>
              <td id="ALL_AMOUNT">0.0</td>
            </tr>
          </table> 
          <table class="table_edit" id="outId" style="display:none">
          	<tr>
              <th colspan="10"  ><img src="../../img/subNav.gif" alt="" class="nav" />
              外出维修</th>
            </tr>
          	<tr>
          	<td align="right">
          	开始时间：
          	</td>
          	<td nowrap="nowrap">
				<input type="text" name="START_DATE" id="START_DATE"
					class="middle_txt" 
					group="START_DATE,END_DATE" hasbtn="true"
					callFunction="showcalendar(event, 'START_DATE', false)"; />
				<select name="hh" id="se_hh">
					<option value=""></option>
					<%for(int i=1;i<25;i++){ %>
						<option value="<%=i %>"><%=i %></option>
					<%} %>
				</select>时
				<select name="mm" id="se_mm">
					<option value=""></option>
					<%for(int i=1;i<61;i++){ %>
						<option value="<%=i %>"><%=i %></option>
					<%} %>
				</select>分<span style="color: red;size: 10px">*</span>
			</td>
          <td align="right">
          	外出人：
          	</td>
          	<td align="left">
          	<input type="text" name="OUT_PERSON" id="OUT_PERSON" maxlength="20"  class="middle_txt"/><span style="color:red">*</span>
          	</td>
          	<td align="right">
          	派车车牌号：
          	</td>
          	<td align="left">
          	<input type="text" name="OUT_LICENSENO" id="OUT_LICENSENO" class="middle_txt" maxlength="17"/>
          	</td>
          	</tr>
          	<tr>
          	<td align="right">
          	结束时间：
          	</td>
          	<td align="left">
          	<input type="text" name="END_DATE" id="END_DATE" 
					class="middle_txt" 
					group="START_DATE,END_DATE" hasbtn="true"
					callFunction="showcalendar(event, 'END_DATE', false)"; />
			<select name="hh1" id="hh1">
					<option value=""></option>
					<%for(int i=1;i<25;i++){ %>
						<option value="<%=i %>"><%=i %></option>
					<%} %>
				</select>时
				<select name="mm1" id="mm1">
					<option value=""></option>
					<%for(int i=1;i<61;i++){ %>
						<option value="<%=i %>"><%=i %></option>
					<%} %>
				</select>分<span style="color: red;size: 10px">*</span>
          	</td>
          	<td align="right">
          	外出目的地：
          	</td>
          	<td align="left">
          	<input type="text" name="OUT_SITE" id="OUT_SITE"  maxlength="50" class="middle_txt"/><span style="color:red">*</span>
          	</td>
          	
          	<td align="right">
          	<c:if test="${code.codeId==80081001}">
          		单程救急里程：
          	</c:if>
          	<c:if test="${code.codeId==80081002}">
          		单程里程：
          	</c:if>
          	</td>
          	<td align="left">
          	<input type="text" name="OUT_MILEAGE" id="OUT_MILEAGE" blurback="true"  maxlength="8" datatype="1,is_double,10" class="middle_txt"/>
          	</td>
          	</tr>
          	<tr>
          		<td align="right">
          		外派关联主因件：
          		</td>
          		<td align="left" >
          		<select name="OUT_MAIN_PART" id="OUT_MAIN_PART" value="" style="width: 150px;">
          		<option value="-1">--请选择--</option>
          		</select>
          		<span style="color: red">*</span>
          	</td>
          	</tr>
          </table>  
          
          <table id="compensationMoney" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<th colspan="11" align="left">
					<img class="nav" src="../../img/subNav.gif" />
					补偿费
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						供应商代码
					</td>
					<td>
						补偿费申请金额
					</td>
					<td>
						审批金额
					</td>
					<td>
						补偿关联主因件
					</td>
					<td>
						<input id="itemBtn" type="button" class="normal_btn" value="新增"
							name="button422" onClick="addCompensation();" />
					</td>
				</tr>
				<tbody id="itemTableAccessories">
				</tbody>
			</table>
          <table id="accessories" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<th colspan="11" align="left">
					<img class="nav" src="../../img/subNav.gif" />
					辅料
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						辅料代码
					</td>
					<td>
						辅料名称
					</td>
					<td>
						辅料价格
					</td>
					<td>
						辅料关联主因件
					</td>
					<td>
						<input id="itemBtn" type="button" class="normal_btn" value="新增"
							name="button422" onClick="add();" />
					</td>
				</tr>
				<tbody id="itemTableAccessories">
				</tbody>
			</table>
			<!-- 添加附件 开始  -->
        <%-- <table id="add_file"  width="75%" class="table_info" border="0" id="file">
	    		<tr>
	        		<th>
						<input type="hidden" id="fjids" name="fjids"/>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
			     		<input type="button" class="normal_btn" align="right" id="addfile" onclick="showUpload('<%=contextPath%>')" value ='添加附件'/><font color="red">*</font>
					</th>
				</tr>
				<tr>
    				<td width="75%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  				<%if(fileList!=null){
	  				for(int i=0;i<fileList.size();i++) { %>
					  <script type="text/javascript">
				    		addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
				    	</script>
				<%}}%>
			</table>  --%>
  		<!-- 添加附件 结束 -->
		</br>
			<table border="0" cellspacing="0" cellpadding="0" class="table_edit">
				<tr>
					<td colspan="2">&nbsp;</td>
					<td colspan="2" align=center>
						<input class="normal_btn" type="button" id="btn1" value="维修历史" onclick="maintaimHistory();"/>&nbsp;
		                <input class="normal_btn" type="button" id="btn2" value="授权历史" onclick="auditingHistory();"/>&nbsp;
		                <input class="normal_btn" type="button" id="btn3" value="保养历史" onclick="freeMaintainHistory();"/>&nbsp;
						
						<input type="button" onClick="confirmAdd();" id="save_but" class="normal_btn"
							style="" value="确定" />&nbsp;
						<input type="button" onClick="goBack();" class="normal_btn"
							style="" value="返回" />
					</td>
					<td colspan="2">
						<!-- <input class="long_btn" type="button" id="maintain_btn" value="保养状态判定" onclick="maintainStateSet();"/>&nbsp; -->
						<input class="long_btn" type="button" id="three_package_set_btn" value="三包判定" onclick="threePackageSet();"/>
					</td>
				</tr>
			</table>
			<script type="text/javascript">
			var CODE = new Array();
			var NAME = new Array();
			var PRICE = new Array();
			<c:forEach items="${workHourCodeMap}" var="workHourCodeMap">
				NAME.push("${workHourCodeMap.WORKHOUR_NAME}");
        		CODE.push("${workHourCodeMap.WORKHOUR_CODE}");
        		PRICE.push("${workHourCodeMap.PRICE}");
       		</c:forEach>
			function setValue(_this){
				
				for (var int = 0; int < CODE.length; int++) {
						
					if(_this.value==CODE[int]){
						_this.parentNode.parentNode.cells[3].innerHTML='<td><input name="workhour_name" id="workhour_name" value="'+NAME[int]+'" type="text" class="middle_txt" readonly> </td>';
						_this.parentNode.parentNode.cells[5].innerHTML='<td><input name="accessoriesPrice" id="accessoriesPrice" value="'+PRICE[int]+'" type="text" class="middle_txt" readonly></td>';
		 				break;
					}
				}
			}
			//维修历史按钮方法
			function maintaimHistory(){
				var vin = $('VIN').value ;
				window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN='+vin);
			}
			//授权历史按钮方法
			function auditingHistory(){
				var vin = $('VIN').value ;
				window.open('<%=contextPath%>/repairOrder/RoMaintainMain/authDetail.do?VIN='+vin);
			}
			//保养历史按钮方法
			function freeMaintainHistory(){
				var vin = $('VIN').value ;
				window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/freeMaintainHistory.do?VIN='+vin);
			}
			//保养状态判定按钮方法
			function maintainStateSet(){
				var vin = document.getElementById("VIN").value;
				var inMileage = document.getElementById("IN_MILEAGE").value;
				if (vin==null||vin==''||vin=='null') {
					MyAlert("车辆VIN不能为空！");
				}else if (inMileage==null||inMileage==''||inMileage=='null') {
					MyAlert("进厂里程数不能为空！");
				}else{
					window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintainStateSet.do?VIN='+vin+'&mile='+inMileage);
				}
			}
			//配件三包判定按钮方法
			function threePackageSet(){
				var vin = document.getElementById("VIN").value;
				var inMileage = document.getElementById("IN_MILEAGE").value;
				var arr = document.getElementsByName('PART_CODE');
				var PAY_TYPE_PART = document.getElementsByName('PAY_TYPE_PART');
				var WR_LABOURCODE= document.getElementsByName('WR_LABOURCODE');
				var PAY_TYPE_ITEM= document.getElementsByName('PAY_TYPE_ITEM');
				var str = ''; 
				for(var i=0;i<arr.length;i++)
					str = str+arr[i].value+"," ;
				var codes = str.substr(0,str.length-1);
				str = '';
				for(var i=0;i<PAY_TYPE_PART.length;i++)
					str = str+PAY_TYPE_PART[i].value+"," ;
				var codes_type = str.substr(0,str.length-1);
				
				var strcode = '';
				for(var i=0;i<WR_LABOURCODE.length;i++)
					strcode = strcode+WR_LABOURCODE[i].value+"," ;	
				var labcodes = strcode.substr(0,strcode.length-1);
				strcode = '';
				for(var i=0;i<PAY_TYPE_ITEM.length;i++)
					strcode = strcode+PAY_TYPE_ITEM[i].value+"," ;	
				var labcodes_type = strcode.substr(0,strcode.length-1);
				
				if (vin==null||vin==''||vin=='null') {
					MyAlert("车辆VIN不能为空！");
				}else if (inMileage==null||inMileage==''||inMileage=='null') {
					MyAlert("进厂里程数不能为空！");
				}else{
					window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/threePackageSet.do?VIN='+vin+'&mile='+inMileage+'&codes='+codes+'&codes_type='+codes_type+'&labcodes='+labcodes+'&labcodes_type='+labcodes_type);
				}
			}
			function showActivity() {
	    		var vin = document.getElementById("VIN").value;
	    		var inMileage = document.getElementById("IN_MILEAGE").value;
	    		if(vin==''||vin==null||inMileage==''||inMileage==null){
	    			MyAlert('请填写VIN和进厂里程！');
	    		}else {
	    			OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/showActivityForward.do?vin='+vin+"&MODEL_ID="+modelId+"&IN_MILEAGE="+inMileage,800,500);
	    		}
    		}
			
			//判断免费保养是否需要授权
			
			function verFree(){
				var vin = document.getElementById("VIN").value;
				if (vin==null||vin==''||vin=='null') {
					  return false;
				}
				var url = '<%=contextPath%>/repairOrder/RoMaintainMain/getFreeTimes.json';
				makeCall(url,verFreeTimes,{VIN:vin});
			}
			function verFreeTimes(json){
				document.getElementById("freeTimes").value=json.needTime;
 			 	if(json.needTime==1){
					document.getElementById("cost").innerHTML = "首次保养免费";
				}else{
					if(json.free==null||json.free==""){
						document.getElementById("cost").innerHTML = "定保自费，该车型未维护售后车型组";
					}else{
						document.getElementById("cost").innerHTML = "定保自费，收费金额为"+json.free+"元";
					}
				}
				verFree_1();
				}
			function verFree_1() {
			  	var purchasedDate = document.getElementById("GUARANTEE_DATE").value;
				var vin = document.getElementById("VIN").value;
				var inMileage = document.getElementById("IN_MILEAGE").value;
				if (vin==null||vin==''||vin=='null') {
					MyAlert("车辆VIN不能为空！");
				}else if (inMileage==null||inMileage==''||inMileage=='null') {
					MyAlert("进厂里程数不能为空！");
				}else{
				var url = '<%=contextPath%>/repairOrder/RoMaintainMain/getFree.json';
				makeCall(url,verFreeBack,{VIN:vin,IN_MILEAGE:inMileage,PURCHASED_DATE:purchasedDate});
				}
			}
			function verFree3() {
				//$('save_but').disabled=true;
				var vin = document.getElementById("VIN").value;
				if (vin==null||vin==''||vin=='null') {
					  return false;
				}
				var url = '<%=contextPath%>/repairOrder/RoMaintainMain/getFreeTimes.json';
				makeCall(url,verFreeBackFree,{VIN:vin});
				
			}
			function verFreeBackFree(json){
				var inMileage = document.getElementById("IN_MILEAGE").value;
				document.getElementById("freeTimes").value=json.needTime;
				var vin = document.getElementById("VIN").value;
				var url = '<%=contextPath%>/repairOrder/RoMaintainMain/getFree.json';
				makeCall(url,verFreeBack3,{VIN:vin,IN_MILEAGE:inMileage,PURCHASED_DATE:purchasedDate});
				}
			function verFreeBack3(json) {
				var last = json.approve;
				var mastMileage = json.mileage_vin.MILEAGE;
			  	var purchasedDate = document.getElementById("GUARANTEE_DATE").value;
				$('mastMileage').value=mastMileage;
				if(Number($('IN_MILEAGE').value)<Number(mastMileage)){
					return false;
				}
				document.getElementById("freeTimes").value=json.needTime;
				if($('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_02%>||$('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_04%>){
					if (last) {
						if (!hasFore) {
					
						}
					}
				}
				$('save_but').disabled=false;
			}
			function verFreeBack(json) {
				var last = json.approve;
				var mastMileage = json.mileage_vin.MILEAGE;
				$('mastMileage').value=mastMileage;
				if(Number($('IN_MILEAGE').value)<Number(mastMileage)){
					MyAlert("进厂行驶里程不能小于系统行驶里程");
					return false;
				}
				document.getElementById("freeTimes").value=json.needTime;
				//取出baoyangcishu
				var vin = document.getElementById("VIN").value;
			    var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getDetailByVin.json';
			    var pattern=/^([A-Z]|[0-9]){17,17}$/;
			    if(pattern.exec(vin)) {
			      if (vin!=null&&vin!='') {
			          makeCall(url,oneVINBack,{vinParent:vin});
			        }
			
				}
			}
	//故障代码联动
    function chooseItem(itemId) {
        //var itemId = document.getElementById('timeId').value;
        var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeItem.json';
    	makeCall(url,changeTroubleCode,{ITEM_ID:itemId});
    }
    //回调函数
    function changeTroubleCode(json) {
    	var last=json.changeCode;
     	last = "<select id='TROUBLE_CODE' class='min_sel' name='TROUBLE_CODE'>"+last+"</select>";
     	myobj.cells.item(6).innerHTML = last;
    	//document.getElementById("myTrouble").innerHTML = last;
    }
    //选择工时联动故障代码(产生服务活动后，点击故障代码下拉框)
	function chooseItem2(itemId) {
        //var itemId = document.getElementById('timeId').value;
        var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeItem.json';
    	makeCall(url,changeTroubleCode2,{ITEM_ID:itemId});
    }
    //回调函数
    function changeTroubleCode2(json) {
    	var last=json.changeCode;
     	last = "<select id='TROUBLE_CODE' name='TROUBLE_CODE' class='min_sel'>"+last+"</select>";
    	myobj.parentNode.innerHTML = last;
    }
    var obj = document.getElementById("REPAIR_TYPE");
    var obj2 = document.getElementById("YIELDLY_TYPE");
    if(obj2){
    obj2.attachEvent('onchange',getTypeChangeStyleParam);//
    }
    if(obj){
   		obj.attachEvent('onchange',getTypeChangeStyleParam);//
   	}
   	function getTypeChangeStyleParam() {
   		getTypeChangeStyle(obj.value);
   	}
   	//根据索赔类型变换样式
   function getTypeChangeStyle(obj) {
	   $('fjtable').style.display='inline';
	   $('hideTr').style.display='none';
	   	if(obj=="11441008")
	   		$('fjtable').style.display='none';
	   	if(obj=="11441004")
	   		$('hideTr').style.display='inline';
   		document.getElementById('ACTIVITYCOMBO').selectedIndex=0;
   		//zeroAllFee();
  		clearAllNode(document.getElementById('itemTable'));
		clearAllNode(document.getElementById('partTable'));
		clearAllNode(document.getElementById('otherTable'));
		clearAllNode(document.getElementById('itemTableAccessories'));
		document.getElementById("CAMPAIGN_CODE").value="";
		document.getElementById("freeTime").style.display = 'none';
		//document.getElementById("zhuyinjian").style.display = 'none';
		document.getElementById("itemBtn").disabled = false;
		document.getElementById("partBtn").disabled = false;
		document.getElementById("otherBtn").disabled = false;
    	if(obj=='<%=Constant.REPAIR_TYPE_01%>') {//一般索赔
    		$('noticeInfo').style.display='none';
    		$('itemTableId').style.display='';
    		$('partTableId').style.display='';
    		$('otherTableId').style.display='none';
    		$('activityTableId').style.display='none';
    		$('activityTableId0').style.display='none';
    		$('activityTableId1').style.display='none';
    		$("activity").style.display='none';
    		$('outId').style.display='none';
    		$('add_file').style.display='none';
    		$("CL_DOWN").value='';
    		$("GS_DOWN").value='';
    		//$("REMARKS_ID").style.display = '';
    		$('ACTIVITYCOMBO').value='';
    		$('accessories').style.display='';
    		$('compensationMoney').style.display='';
    		$('fjtable').style.display='inline';
    	//zmw 2014-09-18
   		}else if(obj=='<%=Constant.REPAIR_TYPE_09%>'){
   			addOutInfo('QT006','过路过桥费',0);
    		addOutInfo('QT007','交通补助费',1);
    		addOutInfo('QT008','住宿费',2);
    		addOutInfo('QT009','餐补费',3);
    		addOutInfo('QT010','人员补助',4);
    		$('accessories').style.display='';
    		$('compensationMoney').style.display='';
    		$('noticeInfo').style.display='none';
    		$('itemTableId').style.display='';
    		$('partTableId').style.display='';
    		$('otherTableId').style.display='';
    		$('activityTableId').style.display='none';
    		$('activityTableId0').style.display='none';
    		$('activityTableId1').style.display='none';
    		$("activity").style.display='none';
    		$("VIN").style.readonly='';
    		$('outId').style.display='';
    		$("CL_DOWN").value='';
    		$("GS_DOWN").value='';
    		//$("REMARKS_ID").style.display = '';
    		$('add_file').style.display='none';
    		$('ACTIVITYCOMBO').value='';
    		$('RemarkPDI').innerHTML="备注";
    		$('fjtable').style.display='inline';
    	//zmw 2014-09-18 end
    	}else if(obj=='<%=Constant.REPAIR_TYPE_04%>') {//免费保养
    		//$('REMARKS_ID').style.display = 'none' ;
    		$('noticeInfo').style.display='none';
    		$('itemTableId').style.display='none';
    		$('partTableId').style.display='none';
    		$('otherTableId').style.display='none';
    		$('activityTableId').style.display='none';
    		$('activityTableId0').style.display='none';
    		$('activityTableId1').style.display='none';
    		$("activity").style.display='none';
    		$('outId').style.display='none';
    		$("freeTime").style.display = '';
    		$("CL_DOWN").value='';
    		$("GS_DOWN").value='';
    	//	$("REMARKS_ID").style.display = 'none';
    		$('ACTIVITYCOMBO').value='';
    		$('accessories').style.display='none';
    		$('compensationMoney').style.display='none';
    		$('add_file').style.display='';
    		$('fjtable').style.display='inline';
    	}else if(obj=='<%=Constant.REPAIR_TYPE_02%>') {//外出维修
    		addOutInfo('QT006','过路过桥费',0);
    		addOutInfo('QT007','交通补助费',1);
    		addOutInfo('QT008','住宿费',2);
    		addOutInfo('QT009','餐补费',3);
    		addOutInfo('QT010','人员补助',4);
    		$('accessories').style.display='';
    		$('compensationMoney').style.display='';
    		$('noticeInfo').style.display='none';
    		$('itemTableId').style.display='';
    		$('partTableId').style.display='';
    		$('otherTableId').style.display='';
    		$('activityTableId').style.display='none';
    		$('activityTableId0').style.display='none';
    		$('activityTableId1').style.display='none';
    		$("activity").style.display='none';
    		$("VIN").style.readonly='';
    		$('outId').style.display='';
    		$("CL_DOWN").value='';
    		$("GS_DOWN").value='';
    		//$("REMARKS_ID").style.display = '';
    		$('add_file').style.display='none';
    		$('ACTIVITYCOMBO').value='';
    		$('add_file').style.display='none';
    		$('RemarkPDI').innerHTML="备注";
    		$('fjtable').style.display='inline';
    	}else if(obj=='<%=Constant.REPAIR_TYPE_03%>') {//保外索赔
    		$('noticeInfo').style.display='none';
    		$('itemTableId').style.display='';
    		$('partTableId').style.display='';
    		$('otherTableId').style.display='none';
    		$('activityTableId').style.display='none';
    		$('activityTableId0').style.display='none';
    		$('activityTableId1').style.display='none';
    		$("activity").style.display='none';
    		$('outId').style.display='none';
    		//$("REMARKS_ID").style.display = '';
    		$("CL_DOWN").value='';
    		//$("REMARKS_ID").style.display = '';
    		$('ACTIVITYCOMBO').value='';
    		$('add_file').style.display='none';
    		$("GS_DOWN").value='';
    		$('accessories').style.display='';
    		$('compensationMoney').style.display='';
    		$('fjtable').style.display='inline';
    	}else if(obj=='<%=Constant.REPAIR_TYPE_05%>') {
    	$('noticeInfo').style.display='none';
	    	$("itemBtn").disabled = false;
			$("partBtn").disabled = false;
			$("otherBtn").disabled = true;
    		$('itemTableId').style.display='';
    		$('partTableId').style.display='';
    		$('otherTableId').style.display='';
    		$('outId').style.display='none';
    		$('activityTableId').style.display='';
    		$('activityTableId0').style.display='';
    		$('activityTableId1').style.display='none';
    		$("activity").style.display='';
    		$('add_file').style.display='none';
    		//$("REMARKS_ID").style.display = '';
    		$('accessories').style.display='none';
    		$('compensationMoney').style.display='none';
    		$('fjtable').style.display='inline';
    	}else if(obj=='<%=Constant.REPAIR_TYPE_06%>') {//特殊服务
    		$('noticeInfo').style.display='none';
    		$('itemTableId').style.display='';
    		$('partTableId').style.display='';
    		$('otherTableId').style.display='none';
    		$('activityTableId').style.display='none';
    		$('activityTableId0').style.display='none';
    		$('activityTableId1').style.display='none';
    		$("activity").style.display='none';
    		$('outId').style.display='none';
    		$("CL_DOWN").value='';
    		$("GS_DOWN").value='';
    		$('add_file').style.display='none';
    		//$("REMARKS_ID").style.display = '';
    		$('ACTIVITYCOMBO').value='';
    		$('accessories').style.display='';
    		$('compensationMoney').style.display='';
    		$('fjtable').style.display='inline';
    	}else if(obj=='<%=Constant.REPAIR_TYPE_07%>') {//急件
    		$('noticeInfo').style.display='none';
    		$('itemTableId').style.display='';
    		$('partTableId').style.display='';
    		$('otherTableId').style.display='none';
    		$('activityTableId').style.display='none';
    		$('activityTableId0').style.display='none';
    		$('activityTableId1').style.display='none';
    		$("activity").style.display='none';
    		$('outId').style.display='none';
    		$("CL_DOWN").value='';
    		$('add_file').style.display='none';
    		$("GS_DOWN").value='';
    		//$("REMARKS_ID").style.display = '';
    		$('ACTIVITYCOMBO').value='';
    		$('accessories').style.display='';
    		$('compensationMoney').style.display='';
    		$('fjtable').style.display='inline';
    		
    	}else if(obj=='<%=Constant.REPAIR_TYPE_08%>') {//PDI
    		//$('REMARKS_ID').style.display = 'none' ;
    		$('noticeInfo').style.display='';
    		$('itemTableId').style.display='none';
    		$('partTableId').style.display='none';
    		$('otherTableId').style.display='';
    		$('otherBtn').disabled=true;
    		addPdiInfo('QT011','PDI检测费用',0);
    		$('activityTableId').style.display='none';
    		$('activityTableId0').style.display='none';
    		$('activityTableId1').style.display='none';
    		$("activity").style.display='none';
    		$('outId').style.display='none';
    		$("freeTime").style.display = 'none';
    		$("CL_DOWN").value='';
    		$("GS_DOWN").value='';
    		$('add_file').style.display='none';
    		//$("REMARKS_ID").style.display = 'none';
    		$('ACTIVITYCOMBO').value='';
    		$('accessories').style.display='none';
    		$('RemarkPDI').innerHTML="<span style='color: red;'>备注（PDI）必须填写</span>";
    		$('compensationMoney').style.display='none';
    		$('fjtable').style.display='none';
    	}else {
    		$('noticeInfo').style.display='none';
    		$('itemTableId').style.display='none';
    		$('partTableId').style.display='none';
    		$('otherTableId').style.display='none';
    		$('activityTableId').style.display='none';
    		$('activityTableId0').style.display='none';
    		$('activityTableId1').style.display='none';
    		$("activity").style.display='none';
    		$('outId').style.display='none';
    		$("CL_DOWN").value='';
    		$("GS_DOWN").value='';
    		$('add_file').style.display='none';
    		//$("REMARKS_ID").style.display = '';
    		$('accessories').style.display='none';
    		$('ACTIVITYCOMBO').value='';
    		$('compensationMoney').style.display='none';
    		$('fjtable').style.display='inline';
    	}
    }
    //判断工单时候已经存在
    function verDupR() {
    	var roNo = document.getElementById("RO_NO").value;
		var url = '<%=contextPath%>/repairOrder/RoMaintainMain/verDupRo.json';
		if (roNo!=null&&roNo!=""&&roNo!="null") {
    	makeCall(url,verDupRoBack,{RO_NO:roNo});
    	}
    }
    function verDupRBack(json) {
    	var last = json.dup;
    	if (last){
    		MyAlert("该工单号已经存在！");
    	}else {
    		MyConfirm("是否添加？",confirmAdd0,[]);
    	}
    }
    function verDupRoBackjude(josn)
    {
    	document.getElementById('three_package_set_btn').value = josn.namejdue;
    }
    
    
    function blurBack (obj) {
        if(obj == 'OUT_MILEAGE')
        {
           var OUT_MILEAGE =   document.getElementById(obj).value;
            document.getElementsByName("ITEM_AMOUNT")[1].value =  OUT_MILEAGE*3;
           
        }else
        {
        	    var vin = document.getElementById("VIN").value;
	        if(vin.length ==18){
	        	 vin = vin.substring(0,vin.length-1);
	        	  var strAscii = new Array();//用于接收ASCII码
			var res = "";
			for(var i = 0 ; i < vin.length ; i++ ){
			strAscii[i] = parseInt(vin.charCodeAt(i))-1;//只能把字符串中的字符一个一个的解码
			res+=String.fromCharCode(strAscii[i]);   
			}
			document.getElementById("VIN").value=res;
			vin = res;
	        }
	      
	        var url = '<%=contextPath%>/repairOrder/RoMaintainMain/judeverDupRo.json';
			if (vin!=null&&vin!=""&&vin!="null") {
	    	makeCall(url,verDupRoBackjude,{vin:vin});
	    	}
		    if (obj=="VIN") {
		    var type = document.getElementById("REPAIR_TYPE").value;
		    if(type!=<%=Constant.REPAIR_TYPE_02%>){
		    if(<%=scan%>==0){
		    	var time = arrvins[arrvins.length-1]-arrvins[0];
				arrvins = new Array();
				if(time > 400){
					document.getElementById("VIN").value = "";
					MyAlert("你不能手动输入VIN!");
					return false;
				}
		    }
		  }
		   
		       oneVIN();
		  
		    	
		    	if (document.getElementById("REPAIR_TYPE").value=='<%=Constant.REPAIR_TYPE_04%>') {
		    		verFree();
		    	}
		    	
		    }else {
		    cleanPart();
		    	if (obj=="RO_NO"){
		    		verDupR();
		    	}
		    	setFee0(document.getElementById(obj));
		    }
           
        }
    
    }
    $('otherTableId').style.display='none';
    
    var options =  document.getElementById("CLAIM_TYPE").options;
   	var index = options.selectedIndex;
   	var myvalue = options[index].value;
	getTypeChangeStyle(myvalue);
	function setMyBtnStyle(){
		//维修历史，授权历史，保养历史三按钮 控制
		$('btn1').disabled = 'disabled' ;
		$('btn2').disabled = 'disabled' ;
		$('btn3').disabled = 'disabled' ;
		$('three_package_set_btn').disabled = 'disabled' ;
		//保养的时候显示     保养状态判定      按钮
		$('maintain_btn').style.display = 'none' ;
	}
	setMyBtnStyle();
    
    function cleanValue(){
    	document.getElementById("VIN").value="";
    		document.getElementById("LICENSE_NO").value = '' ;
			document.getElementById("BRAND_NAME").innerHTML = '' ;
			document.getElementById("SERIES_NAME").innerHTML = '' ;
			document.getElementById("MODEL_NAME").innerHTML = '' ;
			document.getElementById("packageName").innerHTML = '' ;
			document.getElementById("ENGINE_NO").value = '';
			document.getElementById("ENGINE_NOS").innerHTML = '' ;
			
			document.getElementById("BRAND_NAME0").value = '' ;
			document.getElementById("SERIES_NAME0").value = '' ;
			document.getElementById("car_color").value = '' ;
			document.getElementById("MODEL_NAME0").value = '' ;
			document.getElementById("BRAND_CODE0").value = '' ;
			document.getElementById("SERIES_CODE0").value = '';
			document.getElementById("MODEL_CODE0").value = '' ;
			document.getElementById("ENGINE_NO0").value = '' ;
			document.getElementById("GUARANTEE_DATE").value='';
			document.getElementById("GUARANTEE_DATE_ID").innerHTML='';
			document.getElementById("PRODUCT_DATE_ID").innerHTML='';
			document.getElementById("CTM_NAME").innerHTML = '';
    		document.getElementById("CTM_NAME_1").value ='' ;
			document.getElementById("MAIN_PHONE").innerHTML = '' ;
			document.getElementById("GUARANTEE_CODE").innerHTML ='';
			document.getElementById("YIELDLY").innerHTML = '' ;
			$('DELIVERER').value = '' ;
			$('DELIVERER_PHONE').value = '' ;
			document.getElementById("DELIVERER_ADRESS").innerHTML = '' ;
			document.getElementById("DELIVERER_ADRESSS").value = '' ;
			assignSelect("YIELDLY",'');
			modelId='';
			purchasedDate='';
			freeOnchangeText('');
			get();
			//维修历史，授权历史，保养历史三按钮 控制
			$('btn1').disabled = 'disabled' ;
			$('btn2').disabled = 'disabled' ;
			$('btn3').disabled = 'disabled' ;
    }
    
    function changeBtn(){
    	$('save_but').disabled=false;
    }
     function cleanPart(){
   	 clearAllNode(document.getElementById('partTable'));
    }
    function addOutInfo(code,name,id){//选择外出维修时,带出必填的几项费用
     var addTable = document.getElementById("otherTable");
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);
		insertRow.className = "table_list_row1";
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
		insertRow.insertCell(4);
		insertRow.insertCell(5);
		//过路过桥费
    		addTable.rows[length].cells[0].innerHTML =  '<td><input type="text" class="short_txt" name="ITEM_CODE" id="ITEM_CODE"'+id+' value="'+code+'" readonly/></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><input type="text" value="'+name+'" name="ITEM_NAME" id="ITEM_NAME"'+id+'  readonly class="short_txt" /></td>';
			if(id == 1)
			{
			  addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" readonly="readonly"  value="0"  name="ITEM_AMOUNT" id="ITEM_AMOUNT"'+id+'" datatype="0,is_yuan" onblur="sumItem();"  class="short_txt"/></td>';
			}else
			{
			  addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" value="0"  name="ITEM_AMOUNT" id="ITEM_AMOUNT"'+id+'" datatype="0,is_yuan" onblur="sumItem();"  class="short_txt"/></td>';
			}
			
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" name="ITEM_REMARK"  id="ITEM_REMARK"  maxlength="30" class="middle_txt"  /></td>';
			addTable.rows[length].cells[4].innerHTML = genSelBoxExpPay("PAY_TYPE_OTHER",<%=Constant.PAY_TYPE%>,"<%=Constant.PAY_TYPE_02%>",false,"min_sel","","false",'');
			addTable.rows[length].cells[5].innerHTML =  '<td><div align="center"><input type="button" class="normal_btn"  value="删除" disabled  name="button42" onClick="delItemOther(this);"/></div></td>';
    }
     function addPdiInfo(code,name,id){//选择PDI时，生成PDI费用
     var addTable = document.getElementById("otherTable");
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);
		insertRow.className = "table_list_row1";
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
		insertRow.insertCell(4);
		insertRow.insertCell(5);
    		addTable.rows[length].cells[0].innerHTML =  '<td><input type="text" class="short_txt" name="ITEM_CODE" id="ITEM_CODE"'+id+' value="'+code+'" readonly/></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><input type="text" value="'+name+'" name="ITEM_NAME" id="ITEM_NAME"'+id+'  readonly class="short_txt" /></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" value="30"  name="ITEM_AMOUNT" id="ITEM_AMOUNT"'+id+' datatype="0,is_yuan" onblur="sumItem();" readonly class="short_txt"/></td>';
			
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" name="ITEM_REMARK"  id="ITEM_REMARK"  maxlength="30" class="middle_txt"  /></td>';
			addTable.rows[length].cells[4].innerHTML = genSelBoxExpPay("PAY_TYPE_OTHER",<%=Constant.PAY_TYPE%>,"<%=Constant.PAY_TYPE_02%>",false,"min_sel","","false",'');
			addTable.rows[length].cells[5].innerHTML =  '<td><div align="center"><input type="button" class="normal_btn"  value="删除" disabled  name="button42" onClick="delItemOther(this);"/></div></td>';
    }
     function add(){
         var addTable = document.getElementById("accessories");
    		var rows = addTable.rows;
    		var length = rows.length;
    		var insertRow = addTable.insertRow(length);
    		insertRow.className = "table_list_row1";
    		insertRow.insertCell(0);
    		insertRow.insertCell(1);
    		insertRow.insertCell(2);
    		insertRow.insertCell(3);
    		insertRow.insertCell(4);
       		addTable.rows[length].cells[0].innerHTML =  '<td><input type="text" name="workHourCodeMap" class="middle_txt"   id="workHourCodeMap" readonly/><span style="color:red">*</span><a href="#" name="selectworkHour"  onclick="selectworkHour(this);">选择</a></td>';
   			addTable.rows[length].cells[1].innerHTML =  '<td><input name="workhour_name" id="workhour_name"  type="text" class="middle_txt" readonly> </td>';
   			addTable.rows[length].cells[2].innerHTML =  '<td><input name="accessoriesPrice" id="accessoriesPrice" value="" type="text" class="middle_txt" readonly></td>';
   			addTable.rows[length].cells[3].innerHTML =  '<td><select name="accessoriesOutMainPart" id="accessoriesOutMainPart" value="" style="width: 150px;"><option value="-1">--请选择--</option></select> <span style="color: red">*</span></td>';
   			addTable.rows[length].cells[4].innerHTML =  '<td><div align="left"><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="delItemOther(this);"/></div></td>';
   			setPartCodes();
   			//zhuyinjianShow();
   	        }
   //============zyw 2014-8-4
     function addCompensation(){
         var addTable = document.getElementById("compensationMoney");
    		var rows = addTable.rows;
    		var length = rows.length;
    		var insertRow = addTable.insertRow(length);
    		insertRow.className = "table_list_row1";
    		insertRow.insertCell(0);
    		insertRow.insertCell(1);
    		insertRow.insertCell(2);
    		insertRow.insertCell(3);
    		insertRow.insertCell(4);
       		addTable.rows[length].cells[0].innerHTML =  '<td><input type="text" name="supplier_code" class="middle_txt"   id="supplier_code" readonly/><span style="color:red">*</span><a href="#" name="selectSupplierCode"  onclick="selectSupplierCode(this,2);">选择</a></td>';
   			addTable.rows[length].cells[1].innerHTML =  '<td><input name="apply_price" id="apply_price"  type="text" class="middle_txt" maxlength="9"  onblur="checkPrice(this);"/> <span style="color:red">*</span></td>';
   			addTable.rows[length].cells[2].innerHTML =  '<td><input name="pass_price" id="pass_price" value="" type="text" class="middle_txt" maxlength="9" readonly/><span style="color:red">*</span></td>';
   			addTable.rows[length].cells[3].innerHTML =  '<td><select name="part_code_temp" id="part_code_temp" value="" style="width: 150px;"><option value="-1">--请选择--</option></select> <span style="color: red">*</span></td>';
   			addTable.rows[length].cells[4].innerHTML =  '<td><div align="left"><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="delItemOther(this);"/></div></td>';
   			setPartCodesCompensation();
   	       }
     
     function selectSupplierCode(obj,type){
 		myobj="";
 		myobj = obj.parentNode;
 		var partcodes=document.getElementsByName("PART_CODE");
 		var partCode="";
 		for(var i =0;i<partcodes.length;i++){
 			partCode+=partcodes[i].value;
 		}
 		if(partCode==""){
 			MyAlert("提示：请先选择配件");
 			return;
 		}
 		parent.OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectSupplierForward.do?type='+type+'&partCode='+partCode,800,500);
 	}
    
     //验证并赋值的操作
     function checkPrice(obj){
    	 if(obj.value!=""){
    		 var patten = /^\d+\.?\d{0,2}$/;
    		 if(!patten.test(obj.value)){
    		 	MyAlert("提示：请输入正小数并最多保留两位！")
    		 	obj.value="";
    		 }else{
    			 obj.parentNode.nextSibling.firstChild.value=obj.value;
    		 }
    	 }
     }
   //设定供应商Id,code,name
		function setSupplier(id,code,name,partCode) {
			var flag =true;
			var tr = this.getRowObj(myobj);
			var supplierCode =document.getElementsByName('supplier_code');
			for(var i=0;i<supplierCode.length;i++){
				if(supplierCode[i].value==code){
					MyAlert("提示：不能添加相同的供应商!");
					flag = false;
					break;;
					}
				}
			if(flag){
				tr.childNodes[0].childNodes[0].value=code;
			}
		}
   //============zyw 2014-8-4
     function selectworkHour(obj){
 		myobj="";
 		myobj = obj.parentNode;
 		parent.OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectworkHourForward.do',800,500);
 	}
     function setMainWork(v1,v2,v3){
         var flag =true;
			var tr = this.getRowObj(myobj);
			var workCode =document.getElementsByName('workHourCodeMap');
			
				for(var i=0;i<workCode.length;i++){
					if(workCode[i].value==v1){
						MyAlert("不能添加相同的辅料!");
						flag = false;
						break;;
						}
					}
			if(flag){
				tr.childNodes[0].childNodes[0].value=v1;
				tr.childNodes[1].childNodes[0].value=v2;
				tr.childNodes[2].childNodes[0].value=v3;
				}
	}
    </script>
			<!-- 资料显示区结束 -->

		</form>
	</body>
</html>

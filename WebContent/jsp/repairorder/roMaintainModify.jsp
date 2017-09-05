<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.bean.TtAsRepairOrderExtBean"%>
<%@page import="com.infodms.dms.po.TtAsRoLabourBean"%>
<%@page import="com.infodms.dms.po.TtAsRoAddItemPO"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="com.infodms.dms.po.TtAsWrMalfunctionPO"%>
<%@page import="com.infodms.dms.po.TtAsWrQualityDamagePO"%>
<%@page import="com.infodms.dms.po.TtAsRoRepairPartBean"%>
<%@ page import="com.infodms.dms.bean.AclUserBean"%>
<%@ page import="com.infoservice.mvc.context.ActionContext"%>
<%
	String contextPath = request.getContextPath();
%>
<%
			/** 格式化金钱时保留的小数位数 */
			int minFractionDigits = 2;
    		/** 当格式化金钱为空时，默认返回值 */
    		String defaultValue = "0";
			TtAsRepairOrderExtBean tawep = (TtAsRepairOrderExtBean) request.getAttribute("application");
			List<TtAsRoLabourBean> itemLs = (LinkedList<TtAsRoLabourBean>) request.getAttribute("itemLs");
			List<TtAsRoRepairPartBean> partLs = (LinkedList<TtAsRoRepairPartBean>) request.getAttribute("partLs");
			List<TtAsRoAddItemPO> otherLs = (LinkedList<TtAsRoAddItemPO>) request.getAttribute("otherLs");
			List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
			List<TtAsWrMalfunctionPO> malList = (List<TtAsWrMalfunctionPO>) request.getAttribute("MALFUNCTION");
			List<TtAsWrQualityDamagePO> qudList = (List<TtAsWrQualityDamagePO>) request.getAttribute("QUALITY");
			List<Map<String, Object>> accessoryDtlList = (List<Map<String, Object>>)request.getAttribute("accessoryDtlList");
			List<Map<String, Object>> compensationMoneyList = (List<Map<String, Object>>)request.getAttribute("compensationMoneyList");
			String id = (String) request.getAttribute("ID"); 
			String gs = (String) request.getAttribute("GS_DOWN");
			String cl = (String) request.getAttribute("CL_DOWN");
			Integer fore = (Integer) request.getAttribute("fore");
			Integer accSize = (Integer) request.getAttribute("accSize");
			System.out.println(accSize);
			String outMainPart="";
			String outMainPartName = "";
			String code ="";
			String acccode ="";
			ActionContext act = ActionContext.getContext();
		    AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER); 
		    List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
			request.setAttribute("fileList",fileList);
		%>

<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<TITLE>索赔申请创建</TITLE>
<SCRIPT LANGUAGE="JavaScript"><!--
	var first=false; //第一次进入页面为FALSE
	var cloMainTime=1; //关闭主工时选择页面
	var cloTime=1; //关闭附属工时选择页面
	var cloMainPart=1; //关闭主要配件选择页面
	var cloPart=1; //关闭附加配件选择页面
	var myobj;
	var modelId = '<%=tawep.getPackageId() %>';
	var purchasedDate = '<%=tawep.getPurchasedDate()%>';
	var itemToDel='';
	var itemToUp='';
	var partToDel='';
	var partToUp='';
	var otherToDel='';
	var otherToUp='';
	var random=0;
	var flags = true;
	var oneFlag = false;
	var haspart = false;
	var actIsClaim='1'; //不是索赔
	var hasFore;  //已经授权标识
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
			outMainPart[0].options.length = 0;
		//	accessoriesOutMainPart.options.length = 0;
			//重新赋值
			
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
			//accessoriesOutMainPart.options.add(new Option("---请选择---","-1"));
			for(var sa=0;sa<partcodes.length;sa++){
				varItem3 = new Option(partnames[sa],partcodes[sa]);
				outMainPart[0].options.add(varItem3);
			//	accessoriesOutMainPart.options.add(new Option(partnames[sa],partcodes[sa]));
			}
		}
	}
	if('<%=tawep.getForlStatus()%>'=='<%=Constant.RO_FORE_02%>') {
		hasFore = true;
	}else {
		hasFore = false;
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
    	setPartCodes();
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
	function genSelBoxExpPay10(id,type,selectedKey,setAll,_class_,_script_,nullFlag,expStr){
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
		
			
	function confirmUpdate() {
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
		var boo = true;
	       var type =$('REPAIR_TYPE').value;//获取维修类型
	       if(Number($('IN_MILEAGE').value)<Number($('mastMileage').value)){
				MyAlert("进厂行驶里程不能小于系统行驶里程");
				$('saveBtn').disabled = false;
				return false;
			}
			if($('QUELITY_GRATE').value==""||$('QUELITY_GRATE').value==null){
			MyAlert('请选择质量等级!');
				$('save_but').disabled=false;	
				return false;
		}
	       if($('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_07%>||$('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_01%>||$('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_02%>||$('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_03%>||type==<%=Constant.REPAIR_TYPE_06%>){
			    var addTable = document.getElementById('itemTable');
			    var addPartTavle= document.getElementById('partTable');
				var itemRows = addTable.rows;
				var partRows = addPartTavle.rows;
				if(itemRows.length<1){
					MyAlert("一般维修,售前维修,特殊服务以及急件工单必须选择一个作业代码和一个维修配件!") 
					$('saveBtn').disabled = false;
					boo = false;
					return false;
				}
				if(partRows.length<1){
					MyAlert("一般维修,售前维修,特殊服务以及急件工单必须选择一个作业代码和一个维修配件!") 
					$('saveBtn').disabled = false;
					boo = false;
					return false;
				}
				if($('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_02%>){
		 		var otherTable= document.getElementById('otherTable');
		 		var otherRows = otherTable.rows;
		 		if(otherRows.length<1){
				MyAlert("外出维修,必须选择其他项目,添加外出项!") 
				$('save_but').disabled=false;
				return false;
			}
			if($('OUT_MAIN_PART').value==''||$('OUT_MAIN_PART').value==null||$('OUT_MAIN_PART').value=='-1'){
				MyAlert('请选择外出费用关联主因件!');
				$('save_but').disabled=false;	
				return false;   
			}
			}
			}
		var is_gua = document.getElementsByName("IS_GUA");
			for(var i=0;i<is_gua.length;i++){
			if(is_gua[i].value=="0"&& '<%=Constant.REPAIR_TYPE_06%>' != type && '<%=Constant.REPAIR_TYPE_05%>' != type && '<%=Constant.REPAIR_TYPE_03%>' != type){
					MyAlert("含有超三包配件,请开特殊服务单!");
					return false;
			}else if(is_gua[i].value =="1"&& '<%=Constant.REPAIR_TYPE_06%>' == type){
				MyAlert("含有未超三包配件,不能开特殊服务单!");
					return false;
			}
		}
			if($('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_04%>){
				var  hasWr = document.getElementById("hasWr").value;
				if(hasWr==0||hasWr=="0"){
					MyAlert("该车已经超过整车三包!此次保养不能享受三包服务.");
				}
			}
			
			$('VIN').disabled=false;
			var options =  document.getElementById("REPAIR_TYPE_1").options;
	    	var index = options.selectedIndex;
	    	if(options[index].value=='<%=Constant.REPAIR_TYPE_01%>') {//一般索赔
	    	//需要有主要工时，配件
	    	if(document.getElementById('itemTable').childNodes.length==0) {
	    		MyAlert("一般索赔需要添加至少一个主工时");
	    		$('saveBtn').disabled = false;
	    		return false;
	    	}
    	}
	    	 if($('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_08%>){
				 var auditCons=document.getElementsByName('ITEM_REMARK');
					for(var i=0;i<auditCons.length;i++){
						if(auditCons[i].value==""){
							MyAlert("提示：索赔类型为PDI的必须填写备注！  ");
							return;
						}
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
				 $('saveBtn').disabled = false;
				 return false;
				}
				for(var k =0;k<partlen;k++){
				var lCode=addTable.childNodes[a].childNodes[0].childNodes[0].value;//主工时
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
				if(len > 0 || partlen > 0){
					if(!laHasPart){
					MyAlert("一个工时至少对应一个配件!");
					return;
				}
				}
				
			}
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
			 if($('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_07%>||$('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_01%>||$('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_02%>||$('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_03%>||type==<%=Constant.REPAIR_TYPE_06%>){
			
			if(!hasMainCode){
			MyAlert("每次维修必须选择一个主因件!");
			return false;
		}
		}
		var len = $('remark_ysq').value.length;				
				if(len>800){
					MyAlert("预授权内容不能超过800字！");
					return false;
				}
	 	//2012-12-25 判断此车是不是该用户的业务范围（暂时不要。）
	 	//makeNomalFormCall('<%=request.getContextPath()%>/repairOrder/ClaimBillMaintainMain/getVinAndUser.json',modify111,'fm');
	 	if(type!=<%=Constant.REPAIR_TYPE_04%>){
	 	//判断 工时为自费,配件为索赔
	 	makeNomalFormCall('<%=request.getContextPath()%>/repairOrder/RoMaintainMain/checkLabour.json',checks,'fm');
	 	}
	// if((type!=<%=Constant.REPAIR_TYPE_04%>)&&(type!=<%=Constant.REPAIR_TYPE_05%>)){
	if((type!=<%=Constant.REPAIR_TYPE_04%>)){
	 	//判断 是否为一车一天
	 	makeNomalFormCall('<%=request.getContextPath()%>/repairOrder/RoMaintainMain/checkOneDay.json',checkVinDay,'fm');
	 	}
		//加一个材料费为0的判断，不允许修改成0上报
		var url = '<%=request.getContextPath()%>/repairOrder/RoMaintainMain/getFree.json';
		makeNomalFormCall(url,function(json){if(json.bug == "该件更换状态下材料费为零，请联系总部确认"){MyAlert(json.bug);return false;}},'fm');
	    if(boo==true){
			if(submitForm('fm')){
				MyConfirm("确定修改吗？",confirmUpdate0,[]);
			}
	    }
	}
function checkVinDay(json){
		if(json.flag=='true'){
			MyAlert("同一车一天只能开一次索赔或者预授权工单!");
			$('saveBtn').disabled = false;		
			boo = false;
			return false;
		}else if(json.haspart=='true'){
			MyAlert("含有没有库存的配件,不能开工单!");
		$('saveBtn').disabled = false;		
			boo = false;
			return false;
		}else {
			boo = true;
		}
}
function checks(json){
		//MyAlert(json.flag);
	if(json.flag=='true'){
			MyAlert("工时为自费时,其下配件不能进行索赔!");
		$('saveBtn').disabled = false;	
			boo = false;
			return false;
		}else  {
		boo = true;
		}
}
function checks2(json){
		//MyAlert(json.flag);
		if(json.flag=='true'){
			MyAlert("同一车一天只能开一次索赔或者预授权工单!");
			$('saveBtn').disabled = false;
			oneFlag =true ;
			return false;
		}else {
			oneFlag = false;
			haspart = false;
		}
}
	//function modify111(json){
		//MyAlert(json.flag);
		//if(json.flag=='false'){
			//MyAlert("此车不在这个用户范围内!");
			//return false;
		//}
	//}
	
	function keyListnerResp(){
	   if((typeof window.event)!= 'undefined'){
		   var type = event.srcElement.type;   
	       var code = event.keyCode;
	       if(type=='text'||type=='textarea'){
	    	   event.returnValue=true;
	       }else{//如 不是文本域则屏蔽 Alt+ 方向键 ← Alt+ 方向键 →   //屏蔽后退键    
	         if(code==8||((window.event.altKey)&&((code==37)||(code==39)))){
	            event.returnValue=false;       
	         }
	       }
	   }
	}
	function confirmUpdate0() {
		var vin =$('VIN').value;
		var ro_type = $('REPAIR_TYPE').value ;
		makeFormCall('<%=contextPath%>/OrderAction/checkActivityByVin.json?vin='+vin+'&ro_type='+ro_type+'&type=update',backCheckActivityByVin,'fm');
	}
	function backCheckActivityByVin(json){
		var res=json.map.res;
		var checkParts=json.map.checkParts;
		if(""==checkParts){
			var needApprove = '1';
			if ($('approve').disabled==true||$('approve').style.display=='none') { //没有预授权按钮或被禁用：说明不需要预授权
				needApprove = '0';
			}
			$('freeTimes').disabled=false;
			$('approve').disabled = true;
			$('saveBtn').disabled = true;
			var isGua = document.getElementsByName("IS_GUA");
			
			var url1 = '<%=request.getContextPath()%>/repairOrder/RoMaintainMain/roUpdate.json?ITEM_DEL='+itemToDel+'&PART_DEL='+partToDel+'&OTHER_DEL='+otherToDel+'&APPROVAL_YN='+needApprove;
			if(submitForm('fm')){
				makeNomalFormCall(url1,modify_1,'fm');
			}
		}else{
			MyAlert(res+checkParts);
			$('save_but').disabled=false;
		}
	}
	function modify_1(json){
	 if(json.success=='true'){
		MyAlert("保存成功!");
		updateFsFile(json);
		if(json.accredit==1 && json.isClaimFore==0){
			fm.action="<%=request.getContextPath()%>/repairOrder/RoMaintainMain/roModifyForward1.do?ID="+json.ID;
			fm.submit();
		}else {
			fm.action="<%=request.getContextPath()%>/repairOrder/RoMaintainMain/roForward.do";
			fm.submit();
		}
		} else if(json.success=='false'){
			MyAlert(json.outNotice);
			$('saveBtn').disabled = false;
			return;
		} else{
		 MyAlert("保存失败!"+json.outNotice);
		$('saveBtn').disabled = false;
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
		window.location.reload=true;
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
    		MyConfirm("是否修改？",confirmUpdate0,[]);
    	}
    }
	var codeList = new Array();
	<c:forEach items="${accessoryDtlList}" var="accessoryDtlList">
	codeList.push("${accessoryDtlList.WORKHOUR_CODE}");
		</c:forEach>
	var partCodeList = new Array();
	<c:forEach items="${compensationMoneyList}" var="compensationMoneyList">
	codeList.push("${compensationMoneyList.PART_CODE}");
		</c:forEach>
	function doInit()
	{
	   	loadcalendar();
	   	//赋值全局数据
		var WR_LABOURCODEs = document.getElementsByName("WR_LABOURCODE");
		var WR_LABOURNAMEs = document.getElementsByName("WR_LABOURNAME");
		for(var i=0; i<WR_LABOURCODEs.length; i++){
			itemcodes[i] = WR_LABOURCODEs[i].value;
			itemnames[i] = WR_LABOURNAMEs[i].value;
		}
	}
	function notice(){
	if(<%=Constant.REPAIR_TYPE_04%>==<%=tawep.getRepairTypeCode()%>){
		$('freetime').style.display=""; 
	}else if(<%=Constant.REPAIR_TYPE_05%>==<%=tawep.getRepairTypeCode()%>){
		$('activity').style.display=""; 
	}
		if(<%=tawep.getApprovalYn()%>==1  && <%=tawep.getForlStatus()%>!=<%=Constant.RO_FORE_02%> && <%=tawep.getIsClaimFore()%>==0){
						MyAlert("该工单需要进行预授权申请!");
						$('approve').style.display='';
						$('REMARK_ID').style.display='';
		}
	}
	//计算费用
	function countQuantity(obj) {
		var total =0;
		$('approve').disabled=true;
		myobj = obj.parentNode.parentNode
		var price = myobj.cells.item(4).childNodes[0].value;
		var quantity = obj.value;
		var amount =(price*quantity);
		var down  = document.getElementById("CL_DOWN").value;
		var reg = /^\d+$/;
		if (quantity!=null&&quantity!=""){
			if(!reg.test(quantity)){
				MyAlert("配件数量请输入正整数!");
				$('saveBtn').disabled=false;
				return false;
			}else{
			$('saveBtn').disabled=false;
			if(down !=null && down !="" && down != 'null'){
			total = amount*down/100;
			}else {
			total = amount;
			}
			total=total.toFixed(2); 
			myobj.cells.item(5).innerHTML = '<td><input type="text" class="little_txt" value="'+total+'" name="AMOUNT" id="AMOUNT"  size="10" maxlength="9" readonly /></td>';
			document.getElementById("ALL_PART_AMOUNT").innerText =sumArr(document.getElementsByName("AMOUNT")) ;
			sumAll();
			}
		}else {
			MyAlert("请输入配件使用数量！");
			myobj.cells.item(5).childNodes[0].value=0;
			$('saveBtn').disabled=false;
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
	//计算费用
	function countQuantity1(obj) {
	//MyAlert(obj.value);
		var price = myobj.cells.item(5).childNodes[0].value;
		var quantity = obj.value;
		if (quantity!=null&&quantity!=""){
			myobj.cells.item(6).innerHTML = '<input type="text" value="'+price*quantity+'" name="AMOUNT" id="AMOUNT" size="10" maxlength="9" onBlur="javascript:countQuantity(this);"/>';
			document.getElementById("ALL_PART_AMOUNT").innerText = sumArr(document.getElementsByName("AMOUNT"));
		}else {
			MyAlert("请输入数量！");
		}
		
	}
	//增加或删除工时时刷新配件列表中的索赔工时下拉框
	function refreshAppTimeCombo() {
		var codes=document.getElementsByName("WR_LABOURCODE");//取得主工时CODE数组
		var names=document.getElementsByName("WR_LABOURNAME");//取得主工时NAME数组
		if (codes!=null&&codes!="")
		var innerHTML = '<select  name="appTime">';
		//var innerHTML= '';
		innerHTML += '<option value="">索赔工时列表</option>';
		//InsertSelect(obj,'','索赔工时列表');
		for (var i=0;i<codes.length;i++) {
			innerHTML += '<option value="'+codes[i].value+'">'+names[i].value+'</option>';
			//InsertSelect(obj,codes[i].value,names[i].value);
		}
		innerHTML += '</select><input type="hidden" name="PART" value="on"/>';
		//取得配件列表
		var partTable = document.getElementById('partTable');
		if (partTable!=null&&partTable.rows.length!=0) {
			for (var i=0;i<partTable.rows.length;i++) {
				if (partTable.rows[i].childNodes[12].childNodes.length==3) {
					partTable.rows[i].cells[11].innerHTML=innerHTML;
				}
			}
		}
		//MyAlert(partTable.rows[0].childNodes[10].childNodes.length);
	}
	
	//配件工时下拉框
	function genAppTimeCombo(obj) {
		var codes=document.getElementsByName("WR_LABOURCODE");//取得主工时CODE数组
		var names=document.getElementsByName("WR_LABOURNAME");//取得主工时NAME数组
		if (codes!=null&&codes!="")
		var innerHTML = '<select  name="appTime">';
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
		//MyAlert(obj.parentNode.innerHTML);
		obj.parentNode.innerHTML=innerHTML;
	}
	//配件工时下拉框
	function genAppTimeComboStr(id) {
		var codes=document.getElementsByName("WR_LABOURCODE");//取得主工时CODE数组
		var names=document.getElementsByName("WR_LABOURNAME");//取得主工时NAME数组
		if (codes!=null&&codes!="")
		var innerHTML = '<select  id="'+id+'" name="appTime">';
		innerHTML += '<option value="">索赔工时列表</option>';
		for (var i=0;i<codes.length;i++) {
			innerHTML += '<option value="'+codes[i].value+'">'+names[i].value+'</option>';
		}
		innerHTML += '</select><input type="hidden" name="PART" value="on"/>';
		return innerHTML;
	}
	//点新增时配件工时下拉框
	function genAppTimeCombo1(obj) {
		var codes=document.getElementsByName("WR_LABOURCODE");//取得主工时CODE数组
		var names=document.getElementsByName("WR_LABOURNAME");//取得主工时NAME数组
		if (codes!=null&&codes!="")
		var innerHTML = '<select  name="appTime">';
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
		obj.cells[19].innerHTML=innerHTML;
	}
	//获取VIN的方法
	function showVIN(){
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getDetailByVinForward.do',800,500);
	}
	//获取子页面传过来的数据
	function setVIN(VIN,LICENSE_NO,MODEL_NAME,SERIES_NAME,REARAXLE_NO,GEARBOX_NO,ENGINE_NO,COLOR,PRODUCT_DATE,PURCHASED_DATE,CUSTOMER_NAME,CERT_NO,MOBILE,ADDRESS_DESC,HISTORY_MILE,MODEL_ID,BRAND_NAME,PURCHASED_DATE,TRANSFER_NO,BRAND_CODE,SERIES_CODE,MODEL_CODE,YIELDLY){
		document.getElementById("VIN").value = VIN;
		//document.getElementById("LICENSE_NO").value = LICENSE_NO;
		document.getElementById("BRAND_NAME").innerHTML = BRAND_NAME;
		//MyAlert(document.getElementById("brand").value);
		//document.getElementById("BRAND_NAME").innerHTML = '<select onchange="brandOnchange(this)">'+document.getElementById("brand").value+'</select>';
		document.getElementById("SERIES_NAME").innerHTML = SERIES_NAME;
		//document.getElementById("SERIES_NAME").innerHTML = '<select onchange="seriesOnchange(this)">'+document.getElementById("series").value+'</select>';
		
		//document.getElementById("MODEL_NAME").innerHTML = '<select>'+document.getElementById("model").value+'</select>';
		document.getElementById("MODEL_NAME").innerHTML = MODEL_NAME;
		document.getElementById("ENGINE_NO").innerHTML = ENGINE_NO;
		//document.getElementById("REARAXLE_NO").innerHTML = REARAXLE_NO;
		//document.getElementById("GEARBOX_NO").innerHTML = GEARBOX_NO;
		//document.getElementById("TRANSFER_NO").innerHTML = TRANSFER_NO;
		
		document.getElementById("BRAND_NAME0").value = BRAND_NAME;
		document.getElementById("SERIES_NAME0").value = SERIES_NAME;
		document.getElementById("MODEL_NAME0").value = MODEL_NAME;
		document.getElementById("BRAND_CODE0").value = BRAND_CODE;
		document.getElementById("SERIES_CODE0").value = SERIES_CODE;
		document.getElementById("MODEL_CODE0").value = MODEL_CODE;
		document.getElementById("ENGINE_NO0").value = ENGINE_NO;
		//document.getElementById("REARAXLE_NO0").value = REARAXLE_NO;
		//document.getElementById("GEARBOX_NO0").value = GEARBOX_NO;
		//document.getElementById("TRANSFER_NO0").value = TRANSFER_NO;
		document.getElementById("GUARANTEE_DATE").value=formatDate(PURCHASED_DATE);
		//assignSelect("YIELDLY",YIELDLY);
		modelId=MODEL_ID;
		purchasedDate=PURCHASED_DATE;
		//freeOnchange(modelId);
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
		var license = document.getElementById("LICENSE_NO").value;
		var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getDetailByVin.json';
		var pattern=/^([A-Z]|[0-9]){17,17}$/;
		if(pattern.exec(vin)) {
			if (vin!=null&&vin!='') {
	    		makeCall(url,oneVINBack,{vinParent:vin,license:license});
	    	}
		}else {
			//document.getElementById("VIN").value ='' ;
			//document.getElementById("LICENSE_NO").value = '' ;
			document.getElementById("BRAND_NAME").innerHTML = '' ;
			document.getElementById("SERIES_NAME").innerHTML = '' ;
			document.getElementById("MODEL_NAME").innerHTML = '' ;
			document.getElementById("ENGINE_NO").value = '';
			//document.getElementById("REARAXLE_NO").innerHTML = '' ;
			//document.getElementById("GEARBOX_NO").innerHTML = '' ;
			//document.getElementById("TRANSFER_NO").innerHTML = '' ;
			
			document.getElementById("BRAND_NAME0").value = '' ;
			document.getElementById("SERIES_NAME0").value = '' ;
			document.getElementById("MODEL_NAME0").value = '' ;
			document.getElementById("BRAND_CODE0").value = '' ;
			document.getElementById("SERIES_CODE0").value = '';
			document.getElementById("MODEL_CODE0").value = '' ;
			document.getElementById("ENGINE_NO0").value = '' ;
			//document.getElementById("REARAXLE_NO0").value = '' ;
			//document.getElementById("GEARBOX_NO0").value = '' ;
			//document.getElementById("TRANSFER_NO0").value = '' ;
			document.getElementById("GUARANTEE_DATE").value='';
			document.getElementById("GUARANTEE_DATE_ID").innerHTML='';
			document.getElementById("CTM_NAME").innerHTML = '';
    		document.getElementById("CTM_NAME_1").value ='' ;
			document.getElementById("MAIN_PHONE").innerHTML = '' ;
			document.getElementById("GUARANTEE_CODE").innerHTML ='';
			assignSelect("YIELDLY",'');
			modelId='';
			purchasedDate='';
			freeOnchangeText('');
			MyAlert("输入的不是有效VIN格式！");
		}
	}
	//回调函数
	function oneVINBack(json) {
	if (json.ps.records==null){
			//document.getElementById("LICENSE_NO").value = '' ;
			document.getElementById("BRAND_NAME").innerHTML = '' ;
			document.getElementById("SERIES_NAME").innerHTML = '' ;
			document.getElementById("MODEL_NAME").innerHTML = '' ;
			document.getElementById("ENGINE_NO").value = '';
			//document.getElementById("REARAXLE_NO").innerHTML = '' ;
			//document.getElementById("GEARBOX_NO").innerHTML = '' ;
			//document.getElementById("TRANSFER_NO").innerHTML = '' ;
			
			document.getElementById("BRAND_NAME0").value = '' ;
			document.getElementById("SERIES_NAME0").value = '' ;
			document.getElementById("MODEL_NAME0").value = '' ;
			document.getElementById("BRAND_CODE0").value = '' ;
			document.getElementById("SERIES_CODE0").value = '';
			document.getElementById("MODEL_CODE0").value = '' ;
			document.getElementById("ENGINE_NO0").value = '' ;
			//document.getElementById("REARAXLE_NO0").value = '' ;
			//document.getElementById("GEARBOX_NO0").value = '' ;
			//document.getElementById("TRANSFER_NO0").value = '' ;
			document.getElementById("GUARANTEE_DATE").value='';
			document.getElementById("GUARANTEE_DATE_ID").innerHTML='';
			document.getElementById("CTM_NAME").innerHTML = '';
    		document.getElementById("CTM_NAME_1").value ='' ;
			document.getElementById("MAIN_PHONE").innerHTML = '' ;
			document.getElementById("GUARANTEE_CODE").innerHTML ='';
			assignSelect("YIELDLY",'');
			modelId='';
			purchasedDate='';
			freeOnchangeText('');
		}else {
	   	var last=json.ps.records;
    	var size=last.length;
    	var record;
    	if (size>0) {
    		record = last[0];
    		document.getElementById("VIN").value =getNull(record.vin) ;
			//document.getElementById("LICENSE_NO").value = getNull(record.licenseNo) ;
			document.getElementById("BRAND_NAME").innerHTML = getNull(record.brandName) ;
			document.getElementById("SERIES_NAME").innerHTML = getNull(record.seriesName) ;
			document.getElementById("MODEL_NAME").innerHTML = getNull(record.modelCode) ;
			document.getElementById("ENGINE_NO").value = getNull(record.engineNo) ;
			//document.getElementById("REARAXLE_NO").innerHTML = getNull(record.rearaxleNo) ;
			//document.getElementById("GEARBOX_NO").innerHTML = getNull(record.gearboxNo) ;
			//document.getElementById("TRANSFER_NO").innerHTML = getNull(record.transferNo) ;
			document.getElementById("GUARANTEE_CODE").innerHTML = getNull(record.ruleCode) ;
			document.getElementById("BRAND_NAME0").value = getNull(record.brandName) ;
			document.getElementById("SERIES_NAME0").value = getNull(record.seriesName) ;
			document.getElementById("MODEL_NAME0").value = getNull(record.modelName) ;
			document.getElementById("BRAND_CODE0").value = getNull(record.brandCode) ;
			document.getElementById("SERIES_CODE0").value = getNull(record.seriesCode) ;
			document.getElementById("MODEL_CODE0").value = getNull(record.modelCode) ;
			document.getElementById("ENGINE_NO0").value = getNull(record.engineNo) ;
			//document.getElementById("REARAXLE_NO0").value = getNull(record.rearaxleNo) ;
			//document.getElementById("GEARBOX_NO0").value = getNull(record.gearboxNo) ;
			//document.getElementById("TRANSFER_NO0").value = getNull(record.transferNo) ;
			document.getElementById("GUARANTEE_DATE").value=formatDate(getNull(record.purchasedDate) );
			document.getElementById("GUARANTEE_DATE_ID").innerHTML=formatDate(getNull(record.purchasedDate) );
			document.getElementById("DELIVERER_ADRESS").innerHTML = getNull(record.address) ;
			document.getElementById("CTM_NAME").innerHTML = getNull(record.ctmName) ;
    		document.getElementById("CTM_NAME_1").value = getNull(record.ctmName) ;
			document.getElementById("MAIN_PHONE").innerHTML = getNull(record.mainPhone) ;
			
			assignSelect("YIELDLY",record.yieldly);
			modelId=record.modelId;
			purchasedDate=record.purchasedDate;
			freeOnchangeText(record.modelId);
			//changeActivity(record.modelId);
    	
    		
        }
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
	   //var arg1 = args1.toString().replace("\,","");
	   //var arg2 = args2.toString().replace("\,","");
	   //MyAlert(arg1);
	   //MyAlert(arg2);
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
	//免费活动下拉框联动
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
		var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeFreeText.json';
    	makeCall(url,changeFreeText,{MODEL_ID:modelId,PURCHASED_DATE:purchasedDate});
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
    //累加数组和
    function sumArr(arr) {
    	var sum=0;
    	var tmp = 0;
    	for (var i=0;i<arr.length;i++) {
    		tmp = arr[i].value==""?0:arr[i].value;
    		sum=accAdd(sum , parseFloat(tmp));
    	}
		return sum;	    	
    }
    //累加数组和
    function sumArr1(arr) {
    	var sum=0;
    	var tmp = 0;
    	for (var i=0;i<arr.length;i++) {
    		tmp = arr[i]==""?0:arr[i];
    		sum=accAdd(sum , parseFloat(tmp));
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
    	baseLabourAmount = baseLabourAmount.replace(",","");
    	var addLabour = document.getElementById('ADD_LABOUR').innerText; //附加工时
    	var addLabourAmount = document.getElementById('ADD_LABOUR_AMOUNT').innerText; //附加工时金额
    	addLabourAmount = addLabourAmount.replace(",","");
    	var allPartAmount = document.getElementById('ALL_PART_AMOUNT').innerText;//配件金额
    	allPartAmount = allPartAmount.replace(",","");
    	var allLabourAmount = document.getElementById('ALL_LABOUR_AMOUNT').innerText;//工时金额
    	allLabourAmount = allLabourAmount.replace(",","");
    	var otherAmount = document.getElementById('OTHER_AMOUNT').innerText;//其他费用金额
    	otherAmount = otherAmount.replace(",","");
    	var gameAmount = document.getElementById('GAME_AMOUNT').innerText;//免费保养金额
    	gameAmount = gameAmount.replace(",","");
    	var activityAmount = document.getElementById('ACTIVITY_AMOUNT').innerText;//服务活动金额
    	activityAmount = activityAmount.replace(",","");
    	var applyAmount = document.getElementById('APPLY_AMOUNT').innerText;//申请金额
    	applyAmount = applyAmount.replace(",","");
    	var tax = document.getElementById('TAX').innerText;//税额
    	var arr=new Array();
		//arr.push(baseLabour);
		//arr.push(baseLabourAmount);
		//arr.push(addLabour);
		//arr.push(addLabourAmount);
		arr.push(allPartAmount);
		arr.push(allLabourAmount);
		arr.push(otherAmount);
		arr.push(gameAmount);
		arr.push(activityAmount);
		//arr.push(baseLabour);
    	document.getElementById('APPLY_AMOUNT').innerText = sumArr1(arr);
    	applyAmount = document.getElementById('APPLY_AMOUNT').innerText;
    	//arr.push(applyAmount);
    	arr.push(tax);
    	document.getElementById('ALL_AMOUNT').innerText = sumArr1(arr);
    }
	//工时选择
	function setMainTime(id,labourCode,wrgroupName,cnDes,labourQuotiety,parameterValue,fore,isSpec,level) {
		var table = myobj.parentNode;
		var length= table.childNodes.length;
		var flag=0;
		//判断是否添加了重复的主工时
		if($('WR_LABOURCODE')!=null){
			var labourCodes = $$('[name="WR_LABOURCODE"]').pluck('value');
			if(labourCodes.indexOf(labourCode)!=-1){
					MyAlert("该工时已经存在，不可添加！");
					return false;
			}
		}
		if (flag==0){
			cloMainTime=1;
			if(isSpec==0) {
			var down  = document.getElementById("GS_DOWN").value;
			if(down !=null && down !="" && down !='null'){
			total = accMul(labourQuotiety,parameterValue)*down/100;
			}else{
			total = accMul(labourQuotiety,parameterValue);
			}
			total=total.toFixed(2); 
			//chooseItem(labourCode);
			myobj.cells.item(0).innerHTML='<td><input type="text" class="phone_txt"   name="WR_LABOURCODE"  datatype="0,is_null" readonly value="'+labourCode+'" size="10"/><input name="MAIN_ITEM"  type="hidden" value="on"/><span style="color:red">*</span><a href="#"  onclick="selectMainTime(this);">选择</a></td>'
			myobj.cells.item(1).innerHTML='<td><span class="tbwhite"><input type="text" name="WR_LABOURNAME" class="long_txt" value="'+cnDes+'" size="10" readonly/><input type="hidden" class="long_txt"  name="FORE" datatype="0,is_digit_letter_cn,100" value="'+fore+'" size="10" /></span></td>';
			myobj.cells.item(2).innerHTML='<td><input type="text" name="LABOUR_HOURS" class="little_txt"   value="'+labourQuotiety+'" size="8" maxlength="11" id="PRICE" readonly/></td>';
			myobj.cells.item(3).childNodes[0].value=parameterValue;
			//myobj.cells.item(3).innerHTML='<td><input type="text" name="LABOUR_PRICE" class="little_txt"   value="'+parameterValue+'" size="8" maxlength="11" id="y20000001" readonly/></td>';
			myobj.cells.item(4).innerHTML='<td><input type="text" name="LABOUR_AMOUNT" class="little_txt" readonly  value="'+total+'" size="8" maxlength="9" readonly="true"/></td>';
			myobj.cells.item(7).innerHTML =  '<td><input type="hidden"  class="little_txt"  value="'+level+'"  name="LEVELS"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this,\'item\');"/></td>';
			}else {
			var down  = document.getElementById("GS_DOWN").value;
			if(down !=null && down !="" && down !='null'){
			total = accMul(labourQuotiety,parameterValue)*down/100;
			}else {
			total=accMul(labourQuotiety,parameterValue);
			}
			total=total.toFixed(2); 
				random++;
				//chooseItem(labourCode);
				myobj.cells.item(0).innerHTML='<td></span><input name="MAIN_ITEM"  type="hidden" value="on"/><input type="text" class="phone_txt"   name="WR_LABOURCODE" readonly value="'+labourCode+'" size="10"/><span style="color:red">*</span><a href="#"  onclick="selectMainTime(this);">选择</a></td>'
				myobj.cells.item(1).innerHTML='<td><input type="hidden" class="long_txt"  name="FORE" datatype="0,is_digit_letter_cn,100" value="'+fore+'" size="10" /><input type="text" class="long_txt" name="WR_LABOURNAME" datatype="0,is_digit_letter_cn,100" value="'+cnDes+'" size="10" /></td>';
				setMustStyle([myobj.cells.item(1).childNodes[0]]);
				myobj.cells.item(2).innerHTML='<td><input type="text" name="LABOUR_HOURS" class="little_txt" datatype="0,is_double,6" decimal="2"  value="'+labourQuotiety+'" size="8" maxlength="11" id="PRICE'+random+'" onblur="countQuantityLabour(this);" /></td>';
				setMustStyle([myobj.cells.item(2).childNodes[0]]);
				myobj.cells.item(3).innerHTML='<td><input type="text" name="LABOUR_PRICE" class="little_txt"   value="'+parameterValue+'" size="8" maxlength="11" id="y20000001" readonly/></td>';
				myobj.cells.item(4).innerHTML='<td><input type="text" name="LABOUR_AMOUNT" class="little_txt" readonly  value="'+total+'" size="8" maxlength="9" ="true" readonly/></td>';
				myobj.cells.item(7).innerHTML =  '<td><input type="hidden"  class="little_txt"  value="'+level+'"  name="LEVELS"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this,\'item\');"/></td>';
			}
			
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
	    	if (table.childNodes[i].childNodes[10].childNodes.length==3) {
	    		strline = i;
				break;
			}
	    }
	    for (var i=line;i<length;i++) {
	    	if (table.childNodes[i].childNodes[10].childNodes.length==3) {
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
	    
	    	if (table.childNodes[i].childNodes[10].childNodes.length==3) {
	    		//MyAlert(table.childNodes[i].childNodes[1].childNodes[2].value);
	    		myobj.cells.item(0).innerHTML='<td><input type="hidden" name="B_ML_CODE" value="'+table.childNodes[i].childNodes[1].childNodes[0].value+'"/><input name="ITEM" disabled="true"  type="checkbox" onClick="javascript:checkCheckBox(this);"/></td>';
				break;
			}
	    }
		myobj.cells.item(1).innerHTML='<td><span class="tbwhite"><a href="#"  onclick="selectTime();"></a></span><input type="text" class="phone_txt"   name="WR_LABOURCODE0" readonly value="'+labourCode+'" size="10"/><a href="#"  onclick="selectTime(this);">选择</a></td>'
		myobj.cells.item(2).innerHTML='<td><span class="tbwhite"><input type="text" name="WR_LABOURNAME0"  value="'+cnDes+'" size="10" readonly/></span></td>';
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
			//判断是否添加了重复的主工时
			if($('PART_CODE')!=null && (partCode!="00-000" && partCode!="00-0000") ){
				var partCodes =	$$('[name="PART_CODE"]').pluck('value');
				if(partCodes.indexOf(partCode)!=-1){
						MyAlert("该配件已经存在，不可添加！");
						return false;
				}
			}
			for (var i = 0;i<length;i++) {
					if(partCode==table.childNodes[i].childNodes[1].childNodes[2].value && (partCode!="00-000" && partCode!="00-0000")){
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
			myobj.cells.item(1).innerHTML='<input type="text" class="phone_txt" name="PART_CODE"   value="'+partCode+'" size="10" id="PART_CODE" readonly="true"/><span style="color:red">*</span><span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this);alterPartCode('+'\''+partCode+'\''+')">选择</a></span>';
			myobj.cells.item(2).innerHTML='<span class="tbwhite"><input type="text" class="phone_txt" name="PART_NAME" readonly value="'+partName+'" id="PART_NAME"  size="10"/></span>';
			myobj.cells.item(4).innerHTML='<input type="text" class="little_txt" name="PRICE" value="'+stockPrice+'" size="10" maxlength="11" id="PRICE" readonly/>';
			myobj.cells.item(3).childNodes[0].value="";
			myobj.cells.item(5).childNodes[0].value="";   
			if (typeof(zfRoNo) == "undefined") { 
				zfRoNo1="";
			}else{
				zfRoNo1=zfRoNo;
			}			
			myobj.cells.item(15).innerHTML='<textarea cols="8" rows="4" name="zfRoNo" ID="zfRoNo" readonly class="middle_txt" maxlength="2000" >'+zfRoNo1+'</textarea>';
			
			myobj.cells.item(16).innerHTML='<td><input type="hidden"  class="little_txt"  value="'+level+'"  name="PARTLEVEL" /><input type="hidden"  class="little_txt"  value="'+fore+'"  name="PARTFORE" /><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/><input type="hidden"  class="little_txt"  value="'+partId+'"  name="REAL_PART_ID"/></td>';
			getGuaFlag(); 
			alterPartCodeLast(partCode);
			}
	}
	//附加配件选择
	function setPartCode(partId,partCode,partName,stockPrice,supplierCode,supplierName) {
	    var table = myobj.parentNode;
	    var length = myobj.parentNode.childNodes.length; //取得配件列表长度
	    var line = getRowNo(myobj); //取得当前行在表格中的行数
	    var flag=0;
			//判断是否添加了重复的主工时
			for (var i=0;i<length;i++) {
					if(partCode==table.childNodes[i].childNodes[1].childNodes[0].value && (partCode!="00-000" && partCode!="00-0000")){
						cloPart=0;
						MyAlert("该配件已经存在，不可添加！");
						flag=1;
					}
					//break;
			}
			if (flag==0) {
			cloPart=1;
	    for (var i=line;i>=0;i--) {
	    	if (table.childNodes[i].childNodes[7].childNodes.length==3) {
	    		myobj.cells.item(0).innerHTML='<td><input type="hidden" name="B_MP_CODE" value="'+table.childNodes[i].childNodes[1].childNodes[0].value+'"/><input name="PART0" type="checkbox" disabled onClick="javascript:checkCheckBox(this);"/></td>';
	    		break;
			}
	    }
	    myobj.cells.item(4).childNodes[0].value='';
	    myobj.cells.item(6).childNodes[0].value='';
		myobj.cells.item(1).innerHTML='<input type="text" class="phone_txt" name="PART_CODE"   value="'+partCode+'" size="10" id="PART_CODE" readonly="true"/><span class="tbwhite"><a href="#" onClick="javascript:selectPartCode(this)">选择</a></span>';
		myobj.cells.item(2).innerHTML='<td><input type="text" name="DOWN_PART_CODE" value="'+partCode+'" size="10" readonly="true"/><span class="tbwhite"><input type="hidden" name="DOWN_PART_NAME"  value="'+partName+'" size="10" maxlength="13" readonly/><input type="hidden" name="s"  value="1" size="10" maxlength="13" readonly/><span style="color:red">*</span><a href="#" onClick="javascript:selectDownPartCode(this)">选择</a></span></td>';
		myobj.cells.item(3).innerHTML='<span class="tbwhite"><input type="text" class="phone_txt" name="PART_NAME" readonly value="'+partName+'" id="PART_NAME"  size="10"/></span>';
		myobj.cells.item(5).innerHTML='<input type="text" class="little_txt" name="PRICE" value="'+stockPrice+'" size="10" maxlength="11" id="PRICE" readonly/>';
		myobj.cells.item(7).innerHTML='<input type="hidden" class="short_txt" name="PRODUCER_CODE" readonly value="'+supplierCode+'" id="PRODUCER_CODE" /><input type="text" name="PRODUCER_NAME" id="PRODUCER_NAME" value="'+supplierName+'"/>';
		}
	}
	function setDownPartCode(partId,partCode,partName,stockPrice,supplierCode,supplierName) {
		myobj.cells.item(2).innerHTML='<input type="text" class="short_txt" name="DOWN_PART_CODE" value="'+partCode+'" size="10" readonly="true" /><span class="tbwhite"><input type="hidden" name="DOWN_PART_NAME"  value="'+partName+'" size="10" maxlength="13" readonly/><input type="hidden" name="s"  value="1" size="10" maxlength="13" readonly/><span style="color:red">*</span><a href="#" onClick="javascript:selectDownPartCode(this)">选择</a></span>';
		myobj.cells.item(8).innerHTML='<input type="hidden" class="short_txt" name="PRODUCER_CODE1" readonly value="'+supplierCode+'" id="PRODUCER_CODE1" /><input type="text" name="PRODUCER_NAME1" id="PRODUCER_NAME1" readonly class="short_txt" value="'+supplierName+'"/>';
	}
	 // 动态生成表格
 	function addRow(tableId){
 	$('approve').disabled=true;
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
			insertRow.insertCell(16);
			insertRow.insertCell(17);
		}
		//这里的NAME都加0，空的时候就自动不插入到后台了
		if (tableId=='itemTable') {
		if(document.getElementById("REPAIR_TYPE").value==11441005&&document.getElementById("Activity_Type").value!=10561001){
			addTable.rows[length].cells[0].innerHTML =  '<td><span class="tbwhite"><a href="#"  onclick="selectMainTime();"></a></span><input type="text" class="phone_txt"  id="WR_LABOURCODE'+length+'" name="WR_LABOURCODE0" readonly datatype="0,is_null" value="" size="10"/><a href="#"  onclick="selectMainTime(this);">选择</a></td>';
			setMustStyle([document.getElementById("WR_LABOURCODE"+length)]);
			addTable.rows[length].cells[1].innerHTML =  '<td> <input type="text" name="WR_LABOURNAME0" class="long_txt" value="" size="10" readonly/><span style="color:red">*</span><a href="#"  onclick="selectMainTime(this);">选择</a></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" name="LABOUR_HOURS0" class="little_txt"   value="" size="8" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" name="LABOUR_PRICE" class="little_txt"   value="" size="8" maxlength="11" id="y20000001" readonly/></td>';
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" name="LABOUR_AMOUNT0" class="little_txt"   value="" size="8" maxlength="9"  readonly/></td>';
			addTable.rows[length].cells[5].innerHTML = 	'<td>自费<input type="hidden" name="PAY_TYPE_ITEM"  class="little_txt" value="11801001" size="8" maxlength="9" readonly/></td>';
			addTable.rows[length].cells[6].innerHTML ='<td><input disabled="disabled" id="MALFUNCTIONS" name="MALFUNCTIONS" class="middle_txt" value="" /><input type="hidden" id="MALFUNCTION" name="MALFUNCTION" class="middle_txt" value="" /><a href="#" onclick="selectMalCode(this);">选择</a></td>';
			addTable.rows[length].cells[7].innerHTML =  '<td><input type="button" style="display:none" class="normal_btn"  value="删除"  name="button42" onClick="javascript:addPlusRow(\'itemTable\',this,\'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this);"/></td>';
		}else{
			//addTable.rows[length].cells[0].innerHTML =  '<td><input name="ITEM"  checked type="hidden" disabled="true" "/><input name="MAIN_ITEM"  type="hidden" value="on"/><input name="ITEM_IS_FORE"  type="checkbox" disabled="true" /></td>';
			addTable.rows[length].cells[0].innerHTML =  '<td><span class="tbwhite"><a href="#"  onclick="selectMainTime();"></a></span><input type="text" class="phone_txt"  id="WR_LABOURCODE'+length+'" name="WR_LABOURCODE0" readonly datatype="0,is_null" value="" size="10"/><a href="#"  onclick="selectMainTime(this);">选择</a></td>';
			setMustStyle([document.getElementById("WR_LABOURCODE"+length)]);
			addTable.rows[length].cells[1].innerHTML =  '<td><input type="text" name="WR_LABOURNAME0" class="long_txt" value="" size="10" readonly/><span style="color:red">*</span><a href="#"  onclick="selectMainTime(this);">选择</a></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" name="LABOUR_HOURS0" class="little_txt"   value="" size="8" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" name="LABOUR_PRICE" class="little_txt"   value="" size="8" maxlength="11" id="y20000001" readonly/></td>';
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" name="LABOUR_AMOUNT0" class="little_txt"   value="" size="8" maxlength="9"  readonly/></td>';
			addTable.rows[length].cells[5].innerHTML = genSelBoxExpPay("PAY_TYPE_ITEM",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'');
			addTable.rows[length].cells[6].innerHTML ='<td><input disabled="disabled" id="MALFUNCTIONS" name="MALFUNCTIONS" class="middle_txt" value="" /><input type="hidden" id="MALFUNCTION" name="MALFUNCTION" class="middle_txt" value="" /><a href="#" onclick="selectMalCode(this);">选择</a></td>';
			addTable.rows[length].cells[7].innerHTML =  '<td><input type="button" style="display:none" class="normal_btn"  value="删除"  name="button42" onClick="javascript:addPlusRow(\'itemTable\',this,\'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this);"/></td>';
			}
		}else if (tableId == 'partTable') {
		if(document.getElementById("REPAIR_TYPE").value==11441005&&document.getElementById("Activity_Type").value!=10561001){
		addTable.rows[length].cells[0].innerHTML = '<td><input type="checkbox" style="width: 24px;" value="" onclick="return false;" name="IS_GUA" /></td>';
				addTable.rows[length].cells[1].innerHTML =  '<td><input style="width: 30px;" type="text" style="width: 45px;" class="phone_txt" name="PART_CODE"  value="" size="10" id="PART_CODE" readonly/><span style="color:red">*</span><span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this)">选择</a></span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><span class="tbwhite"><input type="text" class="phone_txt" name="PART_NAME" readonly  id="PART_NAME" name="PART_SN3"  size="10"/></span></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" style="width: 24px;" class="little_txt"  onkeyup="countQuantity(this)" size="10" name="QUANTITY" id="QUANTITY'+length+'" datatype="0,is_double,10" decimal="2" maxlength="20"/></td>';
			setMustStyle([document.getElementById("QUANTITY"+length)]);
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" style="width: 35px;" class="little_txt"name="PRICE0"  size="10" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" style="width: 35px;" class="little_txt" name="AMOUNT0" id="AMOUNT" size="10" maxlength="9" datatype="0,is_double,10" decimal="2" readonly /></td>';
			addTable.rows[length].cells[6].innerHTML = '<td><input type="text" name="PAY_TYPE_PART_txt"  class="little_txt" style="width: 45px;" value="自费" size="8" maxlength="9" readonly/><input type="hidden" name="PAY_TYPE_PART"  class="little_txt" value="11801001" size="8" maxlength="9" readonly/></td>';
			addTable.rows[length].cells[7].innerHTML =  '<td><select id="Labour" style="width: 60px;" value="" name="Labour0" ></select></td>';
			addTable.rows[length].cells[8].innerHTML =  '<td><select id="mainPartCode" style="width: 60px;" value="" name="mainPartCode" ></select></td>';
			addTable.rows[length].cells[9].innerHTML =  genSelBoxExpPay10("RESPONS_NATURE",<%=Constant.RESPONS_NATURE_STATUS%>,"",false,"min_sel","","false",'');
			addTable.rows[length].cells[10].innerHTML =  genSelBoxExpPay("HAS_PART",<%=Constant.IF_TYPE%>,"<%=Constant.IF_TYPE_YES%>",false,"min_sel","","false",'');
			addTable.rows[length].cells[11].innerHTML ='<td><select style="width: 55px" onchange="changeQ(this,'+length+');" name="PART_USE_TYPE" id="PART_USE_TYPE'+length+'"><option value="95431001">维修</option> <option value="95431002" selected>更换</option></select></td>';
			addTable.rows[length].cells[12].innerHTML =  '<textarea rows="4" cols="8" class="middle_txt" name="TROUBLE_DESCRIBE"   maxlength="2000" id="TROUBLE_DESCRIBE" />';
			addTable.rows[length].cells[13].innerHTML =  '<textarea rows="4" cols="8" class="middle_txt" name="TROUBLE_REASON" id="TROUBLE_REASON"  maxlength="2000"   />';
			addTable.rows[length].cells[14].innerHTML = '<textarea rows="4" cols="8" name="DEAL_METHOD" ID="DEAL_METHOD" class="middle_txt" value="" maxlength="2000" />';
			addTable.rows[length].cells[15].innerHTML = '<textarea rows="4" cols="8" name="zfRoNo" ID="zfRoNo" readonly class="middle_txt" value="" maxlength="2000" />';
			
			addTable.rows[length].cells[16].innerHTML = '<td><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			addTable.rows[length].cells[17].innerHTML = '<td><input type="button" class="normal_btn" onclick="showUploadWithId(\'<%=contextPath%>\',this)" value="添加附件" /></td>';
			addValues();
			
		}else{
			addTable.rows[length].cells[0].innerHTML = '<td><input type="checkbox" style="width: 24px;" value="" onclick="return false;" name="IS_GUA" /></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><input type="text" style="width: 30px;" class="phone_txt" name="PART_CODE"   value="" size="10" id="PART_CODE" readonly/><span style="color:red">*</span><span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this)">选择</a></span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><span class="tbwhite"><input type="text" style="width: 45px;" class="phone_txt" name="PART_NAME" readonly  id="PART_NAME" name="PART_SN3"  size="10"/></span></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" class="little_txt" style="width: 24px;" onkeyup="countQuantity(this)"  onblur="countQuantity(this)" size="10" name="QUANTITY" id="QUANTITY'+length+'" datatype="0,is_double,10" decimal="2"  maxlength="20"/></td>';
			setMustStyle([document.getElementById("QUANTITY"+length)]);
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" class="little_txt"name="PRICE0" style="width:35px;"  size="10" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" class="little_txt" name="AMOUNT0" id="AMOUNT" style="width: 35px;" size="10" maxlength="9" datatype="0,is_double,10" decimal="2" readonly /></td>';
			addTable.rows[length].cells[6].innerHTML = genSelBoxExpPay("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'');
			addTable.rows[length].cells[7].innerHTML =  '<td><select id="Labour" value="" name="Labour0" style="width: 60px;" ></select></td>';
			addTable.rows[length].cells[8].innerHTML =  '<td><select id="mainPartCode" value="" name="mainPartCode" style="width: 60px;" ></select></td>';
			addTable.rows[length].cells[9].innerHTML =  genSelBoxExpPay10("RESPONS_NATURE",<%=Constant.RESPONS_NATURE_STATUS%>,"",false,"min_sel","","false",'');
			addTable.rows[length].cells[11].innerHTML =  '<td><select style="width: 55px" onchange="changeQ(this,'+length+');" name="PART_USE_TYPE" id="PART_USE_TYPE'+length+'"><option value="95431001">维修</option> <option value="95431002" selected>更换</option></select></td>';
			addTable.rows[length].cells[10].innerHTML =  genSelBoxExpPay("HAS_PART",<%=Constant.IF_TYPE%>,"<%=Constant.IF_TYPE_YES%>",false,"min_sel","","false",'');
			
			addTable.rows[length].cells[12].innerHTML =  '<textarea rows="4" cols="8" class="middle_txt" name="TROUBLE_DESCRIBE"   maxlength="2000" id="TROUBLE_DESCRIBE" />';
			addTable.rows[length].cells[13].innerHTML =  '<textarea rows="4" cols="8" class="middle_txt" name="TROUBLE_REASON" id="TROUBLE_REASON"  maxlength="2000"   />';
			addTable.rows[length].cells[14].innerHTML = '<textarea rows="4" cols="8" name="DEAL_METHOD" ID="DEAL_METHOD" class="middle_txt" value="" maxlength="2000" />';
			addTable.rows[length].cells[15].innerHTML = '<textarea rows="4" cols="8" name="zfRoNo" ID="zfRoNo" class="middle_txt" readonly value="" maxlength="2000" />';
			
			addTable.rows[length].cells[16].innerHTML = '<td><input type="button" class="normal_btn" style="width: 60px;"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			addTable.rows[length].cells[17].innerHTML = '<td><input type="button" class="normal_btn" style="width: 60px;" onclick="showUploadWithId(\'<%=contextPath%>\',this)" value="添加附件" /></td>';

			addValues();
			}
		//这个不加0，不用通过弹出页面带值（其他项目）
		}else if (tableId == 'otherTable') { 
			addTable.rows[length].cells[0].innerHTML =  '<td><div align="center"><select onchange="setOtherName(this);" id="ITEM_CODE" name="ITEM_CODE">'+document.getElementById('OTHERFEE').value+'</select></div></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_NAME" id="ITEM_NAME'+length+'"  readonly class="short_txt"/></span></div></td>';
			setMustStyle([document.getElementById("ITEM_NAME"+length)]);
			addTable.rows[length].cells[2].innerHTML =  '<td><div align="center"><input type="text"  name="ITEM_AMOUNT" id="ITEM_AMOUNT'+length+'"  datatype="0,is_yuan" onblur="sumItem();"  class="short_txt"/></div></td>';
			setMustStyle([document.getElementById("ITEM_AMOUNT"+length)]);
			addTable.rows[length].cells[3].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_REMARK"  id="ITEM_REMARK" maxlength="50" class="middle_txt"  /></span></div></td>';
			addTable.rows[length].cells[4].innerHTML = genSelBoxExpPay("PAY_TYPE_OTHER",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'');
			addTable.rows[length].cells[5].innerHTML =  '<td><div align="center"><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="delItemOther(this);"/></div></td>';
			}
			return addTable.rows[length];
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
		
		//设置配件 下拉框 作业代码的值
		function addValues(){
			
		//刷新维修配件上的作业代码值
		var Labour0s = document.getElementsByName("Labour0");
		
		if(Labour0s.length > 0){
			//清空所有值
		//	for(var j=0; j<Labour0s.length; j++){
		//		Labour0s[j].options.length = 0;
		//	}
			//重新赋值
			//for(var k=0; k<Labour0s.length; k++){
				for(var a=0; a<itemcodes.length; a++){  
					var varItem = new Option(itemnames[a],itemcodes[a]);
					Labour0s[Labour0s.length-1].options.add(varItem);
				}
		//	}
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
		 // 动态生成表格
 	function addPlusRow(tableId,obj,code){
 	//MyAlert(getRowNo(obj.parentNode));
	    var addTable = document.getElementById(tableId);
		var rows = addTable.rows;
		//var length = rows.length;
		var length = getRowNo(obj.parentNode)+1;
		var insertRow = addTable.insertRow(length);
		insertRow.className = "table_list_row1";
		if (tableId=='otherTable'){
			insertRow.className = "table_edit";
		}
		//insertRow.id = dataId;
		
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
		insertRow.insertCell(4);
		if (tableId=='itemTable'||tableId=='partTable'){
			insertRow.insertCell(5);
			insertRow.insertCell(6);
		//}
		//if (tableId=='partTable'){
			insertRow.insertCell(7);
			insertRow.insertCell(8);
			insertRow.insertCell(9);
			insertRow.insertCell(10);
		}
		//这里的NAME都加0，空的时候就自动不插入到后台了
		if (tableId=='itemTable') {
		if(document.getElementById("REPAIR_TYPE").value==11441005){
			addTable.rows[length].cells[0].innerHTML =  '<td><span class="tbwhite"><a href="#"  onclick="selectMainTime();"></a></span><input type="text" class="phone_txt"  id="WR_LABOURCODE'+length+'" name="WR_LABOURCODE0" readonly datatype="0,is_null" value="" size="10"/><a href="#"  onclick="selectMainTime(this);">选择</a></td>';
			setMustStyle([document.getElementById("WR_LABOURCODE"+length)]);
			addTable.rows[length].cells[1].innerHTML =  '<td><span class="tbwhite"><input type="text" name="WR_LABOURNAME0" class="long_txt" value="" size="10" readonly/></span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" name="LABOUR_HOURS0" class="little_txt"   value="" size="8" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" name="LABOUR_PRICE0" class="little_txt"   value="" size="8" maxlength="11" id="y20000001" readonly/></td>';
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" name="LABOUR_AMOUNT0" class="little_txt"   value="" size="8" maxlength="9"  readonly/></td>';
			addTable.rows[length].cells[5].innerHTML = 	'<td><input type="text" name="PAY_TYPE_ITEM_txt"  class="little_txt" value="自费" size="8" maxlength="9" readonly/><input type="hidden" name="PAY_TYPE_ITEM"  class="little_txt" value="11801001" size="8" maxlength="9" readonly/></td>';
			addTable.rows[length].cells[6].innerHTML =  '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'itemTable\',this,\'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this);"/></td>';
		
		}else{
			//addTable.rows[length].cells[0].innerHTML =  '<td><input name="ITEM"  checked type="hidden" disabled="true" "/><input name="MAIN_ITEM"  type="hidden" value="on"/><input name="ITEM_IS_FORE"  type="checkbox" disabled="true" /></td>';
			addTable.rows[length].cells[0].innerHTML =  '<td><span class="tbwhite"><a href="#"  onclick="selectMainTime();"></a></span><input type="text" class="phone_txt"  id="WR_LABOURCODE'+length+'" name="WR_LABOURCODE0" readonly datatype="0,is_null" value="" size="10"/><a href="#"  onclick="selectMainTime(this);">选择</a></td>';
			setMustStyle([document.getElementById("WR_LABOURCODE"+length)]);
			addTable.rows[length].cells[1].innerHTML =  '<td><span class="tbwhite"><input type="text" name="WR_LABOURNAME0" class="long_txt" value="" size="10" readonly/></span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" name="LABOUR_HOURS0" class="little_txt"   value="" size="8" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" name="LABOUR_PRICE0" class="little_txt"   value="" size="8" maxlength="11" id="y20000001" readonly/></td>';
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" name="LABOUR_AMOUNT0" class="little_txt"   value="" size="8" maxlength="9"  readonly/></td>';
			addTable.rows[length].cells[5].innerHTML = genSelBoxExpPay("PAY_TYPE_ITEM",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'');
			addTable.rows[length].cells[6].innerHTML =  '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'itemTable\',this,\'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this);"/></td>';
			}
		}else if (tableId == 'partTable') {
		if(document.getElementById("REPAIR_TYPE").value==11441005){
			addTable.rows[length].cells[0].innerHTML = '<td><input type="checkbox" disabled name="IS_GUA" /></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><input type="text" class="phone_txt" name="PART_CODE" datatype="0,is_null"  value="" size="10" id="PART_CODE" readonly/><span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this)">选择</a></span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><span class="tbwhite"><input type="text" class="phone_txt" name="PART_NAME" readonly  id="PART_NAME" name="PART_SN3"  size="10"/></span></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" class="little_txt" datatype="0,isDigit"  onkeyup="countQuantity(this)" onblur="countQuantity(this)" size="10" name="QUANTITY" id="QUANTITY'+length+'" datatype="0,is_double" decimal="2" maxlength="20"/></td>';
			setMustStyle([document.getElementById("QUANTITY"+length)]);
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" class="little_txt"name="PRICE0"  size="10" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" class="little_txt" name="AMOUNT0" id="AMOUNT" size="10" maxlength="9" datatype="0,is_double,10" decimal="2" readonly /></td>';
			//addTable.rows[length].cells[6].innerHTML =  '<td><input type="text" class="short_txt" name="REMARK" id="REMARK" size="10" maxlength="13" /></td>';
			//addTable.rows[length].cells[7].innerHTML = genSelBoxExpPay("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'');
			//addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" class="short_txt" name="REMARK" id="REMARK" size="10" maxlength="13" /></td>';
			//addTable.rows[length].cells[8].innerHTML = '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			addTable.rows[length].cells[6].innerHTML = '<td><input type="text" name="PAY_TYPE_PART_txt"  class="little_txt" value="自费" size="8" maxlength="9" readonly/><input type="hidden" name="PAY_TYPE_PART"  class="little_txt" value="11801001" size="8" maxlength="9" readonly/></td>';
			//addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" class="short_txt" name="REMARK" id="REMARK" size="10" maxlength="13" /></td>';
			addTable.rows[length].cells[7].innerHTML = '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			
		}else{
			addTable.rows[length].cells[0].innerHTML = '<td><input type="checkbox" disabled name="IS_GUA" /></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><input type="text" class="phone_txt" name="PART_CODE" datatype="0,is_null"  value="" size="10" id="PART_CODE" readonly/><span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this)">选择</a></span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><span class="tbwhite"><input type="text" class="phone_txt" name="PART_NAME" readonly  id="PART_NAME" name="PART_SN3"  size="10"/></span></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" class="little_txt" datatype="0,isDigit"   onkeyup="countQuantity(this)" onblur="countQuantity(this)" size="10" name="QUANTITY" id="QUANTITY'+length+'" datatype="0,is_double" decimal="2" maxlength="20"/></td>';
			setMustStyle([document.getElementById("QUANTITY"+length)]);
			addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" class="little_txt"name="PRICE0"  size="10" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" class="little_txt" name="AMOUNT0" id="AMOUNT" size="10" maxlength="9" datatype="0,is_double,10" decimal="2" readonly /></td>';
			//addTable.rows[length].cells[6].innerHTML =  '<td><input type="text" class="short_txt" name="REMARK" id="REMARK" size="10" maxlength="13" /></td>';
			//addTable.rows[length].cells[7].innerHTML = genSelBoxExpPay("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'');
			//addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" class="short_txt" name="REMARK" id="REMARK" size="10" maxlength="13" /></td>';
			//addTable.rows[length].cells[8].innerHTML = '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			addTable.rows[length].cells[6].innerHTML = genSelBoxExpPay("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'');
			//addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" class="short_txt" name="REMARK" id="REMARK" size="10" maxlength="13" /></td>';
			addTable.rows[length].cells[7].innerHTML = '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			
			}
		//这个不加0，不用通过弹出页面带值（其他项目）
		}else if (tableId == 'otherTable') { 
			addTable.rows[length].cells[0].innerHTML =  '<td><div align="center"><select onchange="setOtherName(this);" id="ITEM_CODE" name="ITEM_CODE">'+document.getElementById('OTHERFEE').value+'</select></div></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_NAME" id="ITEM_NAME'+length+'"  class="phone_txt" datatype="0,is_digit_letter_cn,30"/></span></div></td>';
			setMustStyle([document.getElementById("ITEM_NAME"+length)]);
			addTable.rows[length].cells[2].innerHTML =  '<td><div align="center"><input type="text"  name="ITEM_AMOUNT" id="ITEM_AMOUNT'+length+'"  datatype="0,is_yuan" onblur="sumItem();"  class="short_txt"/></div></td>';
			setMustStyle([document.getElementById("ITEM_AMOUNT"+length)]);
			addTable.rows[length].cells[3].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_REMARK"  id="ITEM_REMARK" maxlength="50" class="middle_txt"  /></span></div></td>';
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
 		function showUploadWithIdByupdate(path,id){
 			OpenHtmlWindow(path+'/commonUpload.jsp?id='+id,800,450);
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
	    		timeId = table.childNodes[i].childNodes[1].childNodes[0].value;
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
			myobj.cells.item(1).innerHTML='<td><div align="center"><input readonly type="text" name="ITEM_NAME" class="short_txt" value="'+text+'" datatype="0,is_digit_letter_cn,100"  id="ITEM_NAME"/></div></td>';
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
			var yiedlyType = document.getElementById("YIELDLY_TYPE").value;
			var aCode = document.getElementById("CAMPAIGN_CODE").value;
			var vin = $("VIN").value;
			var type =$('REPAIR_TYPE').value;
			OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectMainPartCodeForward.do?GROUP_ID='+modelId+'&vin='+vin+'&yiedlyType='+yiedlyType+'&aCode='+aCode+'&repairType='+type,800,500);
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
		
		//删除行 工时
		function delItem(obj,name){
			$('approve').disabled=true;
		//MyAlert(obj.parentNode.parentNode.childNodes.length);
		    var tr = this.getRowObj(obj);
		    //MyAlert(tr.childNodes[10].childNodes[2].value);
		    //MyAlert(tr.childNodes[10].childNodes[3].value);
		    //MyAlert("length="+tr.childNodes[10].childNodes.length);
		    if(tr.childNodes[1].childNodes.length==3) {
		    	MyConfirm("删除主工时将会随之删除附加工时，是否删除？",delItems,[tr,name]);
		    }else{
		      if(tr != null){
				    /*艾春2013.04.16 添加删除提示窗口*/
					var res = confirm("你确认要删除吗？");
				    if(res){
				    	tr.parentNode.removeChild(tr);
					    countFee();
					  	//刷新配件上的作业代码列表
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
							document.getElementsByName("Labour0").option[value=rescode].remove();
						}
				    }
		   }else{
		    throw new Error("the given object is not contained by the table");
		   }
		   }
		}
		//删除行 配件
		function delPartItem(obj,name){
			$('approve').disabled=true;
		     var tr = this.getRowObj(obj);
		     
		   if(tr != null){
		    tr.parentNode.removeChild(tr);
		    var partCode = tr.childNodes[1].childNodes[0].value;
		    delAttach(partCode);
		   }else{
		    throw new Error("the given object is not contained by the table");
		   }
		   setPartCodes();
		}
		//删除工时
		function delItems(tr,name){
			$('approve').disabled=true;
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
				//MyAlert(trObj.childNodes[roNo].cells[0].childNodes[0].value);
				if (trObj.childNodes[roNo].cells[0].childNodes.length==6) {
					if (name=='item'){
						itemToDel += ','+trObj.childNodes[roNo].cells[0].childNodes[0].value;				
					}else if (name=='part'){
						partToDel += ','+trObj.childNodes[roNo].cells[0].childNodes[0].value;
					}
				}
				var oldNode = trObj.removeChild(trObj.childNodes[roNo]);
				oldNode = null;
			}
			countFee();
			//refreshAppTimeCombo();
		}
		//删除配件
		function delPartItems(tr){
			$('approve').disabled=true;
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
				//MyAlert(i);
				//MyAlert(roNo);
				//MyAlert(tr.parentNode.childNodes[roNo].innerHTML);
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
		function clearAllNode(parentNode,name){
		    while (parentNode.firstChild) {
		      var oldNode = parentNode.removeChild(parentNode.firstChild);
		      if (oldNode.childNodes[0].childNodes.length==7){
		      if (name=='item'){
		      	//MyAlert(oldNode.childNodes[0].childNodes[0].value);
		      	itemToDel = itemToDel+","+oldNode.childNodes[0].childNodes[0].value;
		      	}else if (name=='part') {
		      	partToDel = partToDel+","+oldNode.childNodes[0].childNodes[0].value;
		      	}
		      }
		      if (name=='part') {
		      	if (oldNode.childNodes[0].childNodes.length==5){
		      	partToDel = partToDel+","+oldNode.childNodes[0].childNodes[0].value;
		      	}
		      }
		      if (name=='other') {
		      
		      //MyAlert(oldNode.childNodes[4].innerHTML);
		      if(oldNode.childNodes[4].childNodes[0].childNodes.length==4){
		      	otherToDel = otherToDel+ ","+ oldNode.childNodes[4].childNodes[0].childNodes[0].value;
		      }
		      
		      	//otherToDel = 
		      }
		     // MyAlert(itemToDel);
		     // MyAlert(partToDel);
		       oldNode = null;
		     }
		    // MyAlert(itemToDel);
		  //   MyAlert(partToDel);
		     //MyAlert(otherToDel);
		   } 
		//删除行其他项目
		function delItemOther(obj){
		$('approve').disabled=true;
		    var tr = this.getRowObj(obj);
		   if(tr != null){
		    tr.parentNode.removeChild(tr);
		    countFee();
		   }else{
		    throw new Error("the given object is not contained by the table");
		   }
		}
		//删除或更新行时累加删除ID 工时
		function delItemIds(obj,type) {
			$('approve').disabled=true;
			//var tr = this.getRowObj(obj);
			//MyAlert(tr.childNodes[0].childNodes[0].value);
			if(type=="del"){
				itemToDel = itemToDel +","+ obj ;
			}else if (type=="update") {
				itemToUp = itemToUp +","+ obj;
			}
			//MyAlert(tr.childNodes[0].childNodes[0].value);
		}
		//删除或更新行时累加删除ID 配件
		function delPartIds(obj,type) {
			$('approve').disabled=true;
			//var tr = this.getRowObj(obj);
			//MyAlert(tr.childNodes[0].childNodes[0].value);
			if(type=="del"){
				partToDel = partToDel +","+ obj  ;
			}else if (type=="update") {
				partToUp =  partToUp +","+ obj;
			}
			//MyAlert(tr.childNodes[0].childNodes[0].value);
		}
		//删除或更新行时累加删除ID 其他项目
		function delOtherIds(obj,type) {
			$('approve').disabled=true;
			//var tr = this.getRowObj(obj);
			if(type=="del"){
				otherToDel = otherToDel +","+ obj;
			}else if (type=="update") {
				otherToUp =  otherToUp +","+ obj;
			}
			//MyAlert(tr.childNodes[4].childNodes[0].childNodes[0].value);
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
		//免费保养选择次数下拉框赋值
		function assignSelectTime(name,value) {
		var sel = document.getElementById(name);
		var option = sel.options;
		var optionLength = option.length;
		for (var i = 0;i<optionLength;i++) {
		  	if (value ==option[i].innerText) {
		  		sel.selectedIndex = i;
		  		break;
		  	}
		}
		}
		//服务活动下拉框赋值
		function assignSelectActivity(name,value) {
			var sel = document.getElementById(name);
			var option = sel.options;
			var optionLength = option.length;
			for (var i = 0;i<optionLength;i++) {
				var arr=option[i].value.split(",");
			  	if (value == arr[0]) {
			  		sel.selectedIndex = i;
			  		break;
			  	}
			}
		}
		//下拉框修改时赋值
		function assignSelectByObj(sel,value) {
		MyAlert(sel.options.length);
		//var sel = document.getElementById(name);
		var option = sel.options;
		var optionLength = option.length;
		for (var i = 0;i<optionLength;i++) {
		  	if (value ==option[i].value) {
		  		sel.selectedIndex = i;
		  	}
		}
		}
		//删除行:其他项目
		function delItemOther(obj){
			$('approve').disabled=true;
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
				clearAllNode(document.getElementById('itemTable'),'item');
				clearAllNode(document.getElementById('partTable'),'part');
				clearAllNode(document.getElementById('otherTable'),'other');
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
				clearAllNode(document.getElementById('itemTable'),'item');
				clearAllNode(document.getElementById('partTable'),'part');
				clearAllNode(document.getElementById('otherTable'),'other');
				document.getElementById('itemTableId').style.display='';
    			document.getElementById('partTableId').style.display='';
    			document.getElementById('otherTableId').style.display='';
    			document.getElementById("ACTIVITY_AMOUNT").innerText ='0.00';
    			changeRepair(arr[0]);
				changePart(arr[0]);
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
		//
		function setRepairTable () {
			var addTable = document.getElementById('itemTable');
			MyAlert(addTable);
			var len  = addTable.rows.length;
			for (var i=0;i<len;i++) {
				myobj=addTable.rows[i];
				//联动故障代码
				chooseItem2(addTable.rows[i].cells.item(1).childNodes[2].value);
			}
		}
		//
		function setRepairTrouble (objec) {
			myobj = objec;
			//联动故障代码
			chooseItem2(objec.parentNode.parentNode.childNodes[1].childNodes[2].value);
		}
		var objValue; 
		function fillCode(objec) {
			myobj = objec;
			objValue = objec.value;
			//联动故障代码
			chooseItem3(objec.parentNode.parentNode.childNodes[1].childNodes[2].value);
			
			
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
			var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getActivityOthers.json?yiedily='+yiedily;
	    	makeCall(url,dynaOther,{CODE:code});
    	}
    	function getPartDetai(){
		var yiedlyType = document.getElementById("YIELDLY_TYPE").value;
		var code = $("CAMPAIGN_CODE").value;
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getPartDetai.do?code='+code+'&yiedlyType='+yiedlyType,800,500);
		}
		function getPartDetai2(){
		var yiedlyType = document.getElementById("YIELDLY_TYPE").value;
		var code = $("CAMPAIGN_CODE").value;
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getJianche.do?code='+code+'&yiedlyType='+yiedlyType,800,500);
		
		}
		function getPartDetai3(){
		var yiedlyType = document.getElementById("YIELDLY_TYPE").value;
		var code = $("CAMPAIGN_CODE").value;
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getZengSong.do?code='+code+'&yiedlyType='+yiedlyType,800,500);
		}
		function getPartDetai4(){
		var yiedlyType = document.getElementById("YIELDLY_TYPE").value;
		var code = $("CAMPAIGN_CODE").value;
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getLabour.do?code='+code+'&yiedlyType='+yiedlyType,800,500);
		}
		function getPartDetai5(){
		var yiedlyType = document.getElementById("YIELDLY_TYPE").value;
		var code = $("CAMPAIGN_CODE").value;
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getPart.do?code='+code+'&yiedlyType='+yiedlyType,800,500);
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
				addTable.rows[i].cells[1].innerHTML =  '<td><span class="tbwhite"><input type="text" name="WR_LABOURNAME" class="long_txt" value="'+last[i].itemName+'" size="10" readonly/></span></td>';
				addTable.rows[i].cells[2].innerHTML =  '<td><input type="text" name="LABOUR_HOURS" class="little_txt"   value="'+last[i].normalLabor+'" size="8" maxlength="11" id="PRICE" readonly/></td>';
				addTable.rows[i].cells[3].innerHTML =  '<td><input type="text" name="LABOUR_PRICE" class="little_txt"   value="'+last[i].parameterValue+'" size="8" maxlength="11" id="y20000001" readonly/></td>';
				addTable.rows[i].cells[4].innerHTML =  '<td><input type="text" name="LABOUR_AMOUNT" class="little_txt" readonly  value="'+last[i].sum+'" size="8" maxlength="9" readonly="true"/></td>';
				addTable.rows[i].cells[5].innerHTML =  '<td>'+genSelBoxExpPay("PAY_TYPE_ITEM",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'')+'</td>';
				addTable.rows[i].cells[6].innerHTML =  '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'itemTable\',this,\''+last[i].itemCode+'\');"/><input type="button" class="normal_btn" disabled value="删除"  name="button42" onClick="javascript:delItem(this);"/></td>';
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
				if (actIsClaim=='0'){
					addTable.rows[i].cells[0].innerHTML =  '<td><input type="checkbox" disabled name="IS_GUA" checked/><input type="hidden" name="B_MP_CODE" value=""/><input name="PART0" type="hidden" checked="true" disabled onClick="if (this.checked) setTd(this);else clearTd(this);"/></td>';
					addTable.rows[i].cells[6].innerHTML = '<td>'+genSelBoxExpPay("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"<%=Constant.PAY_TYPE_02%>",false,"min_sel","","false",'')+'</td>';
				}else {
					addTable.rows[i].cells[0].innerHTML =  '<td><input type="checkbox" disabled/><input type="hidden" name="IS_GUA" value="off"/><input type="hidden" name="B_MP_CODE" value=""/><input name="PART0" type="hidden" checked="true" disabled onClick="if (this.checked) setTd(this);else clearTd(this);"/></td>';
					addTable.rows[i].cells[6].innerHTML = '<td>'+genSelBoxExpPay("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"<%=Constant.PAY_TYPE_01%>",false,"min_sel","","false",'')+'</td>';
				}
				addTable.rows[i].cells[1].innerHTML =  '<input type="text" class="phone_txt" name="PART_CODE"   value="'+last[i].partNo+'" size="10" id="PART_CODE" readonly="true"/><span class="tbwhite"><a href="#" onClick="javascript:selectPartCode(this)"></a></span>';
				addTable.rows[i].cells[2].innerHTML =  '<span class="tbwhite"><input type="text" class="phone_txt" name="PART_NAME" readonly value="'+last[i].partName+'" id="PART_NAME"  size="10"/></span>';
				addTable.rows[i].cells[3].innerHTML =  '<td><input type="text" class="short_txt" datatype="0,isDigit"  onkeyup="countQuantity(this)" onblur="countQuantity(this)" size="10" name="QUANTITY" id="QUANTITY'+i+'" value="'+last[i].partQuantity+'" datatype="0,is_double" decimal="2" maxlength="20"/></td>';
				setMustStyle([document.getElementById("QUANTITY"+i)]);
				var supplierCode = last[i].supplierCode==null?"":last[i].supplierCode;
				var supplierName = last[i].supplierName==null?"":last[i].supplierName;
				addTable.rows[i].cells[4].innerHTML =  '<input type="text" class="little_txt" name="PRICE" value="'+last[i].partPrice+'" size="10" maxlength="11" id="PRICE" readonly/>';
				addTable.rows[i].cells[5].innerHTML =  '<td><input type="text" class="little_txt" name="AMOUNT" id="AMOUNT" value="'+last[i].partAmount+'" size="10" datatype="0,is_money,10"  maxlength="9" readonly /></td>';
				
				addTable.rows[i].cells[7].innerHTML = '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\''+last[i].partNo+'\');"/><input type="button" class="normal_btn" disabled value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			}
			//refreshAppTimeCombo();
			sumFunc();
		}
		//回调
		function dynaOther(json) {
			var last=json.activityOthers;
    		var len = last.length;
    		var innerHTML="";
    		var addTable = document.getElementById('otherTable');
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
				
				addTable.rows[i].cells[0].innerHTML =  '<td><div align="center"><select onchange="setOtherName(this);" id="OTHER_CODE'+i+'" name="ITEM_CODE">'+document.getElementById('OTHERFEE').value+'</select></div></td>';
				assignSelect('OTHER_CODE'+i,last[i].itemCode);
				addTable.rows[i].cells[1].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_NAME" id="ITEM_NAME'+i+'" readonly class="short_txt" value="'+last[i].itemDesc+'" datatype="0,is_digit_letter_cn,100"/></span></div></td>';
				setMustStyle([document.getElementById("ITEM_NAME"+i)]);
				addTable.rows[i].cells[2].innerHTML =  '<td><div align="center"><input type="text" name="ITEM_AMOUNT" id="ITEM_AMOUNT'+i+'" onblur="sumItem();"   datatype="0,is_yuan"  class="short_txt"/></div></td>';
				setMustStyle([document.getElementById("ITEM_AMOUNT"+i)]);
				addTable.rows[i].cells[3].innerHTML =  '<td><div align="center"><span class="tbwhite"><input type="text" name="ITEM_REMARK"  id="ITEM_REMARK" maxlength="50" class="middle_txt"  /></span></div></td>';
				addTable.rows[i].cells[4].innerHTML =  '<td>'+genSelBoxExpPay("PAY_TYPE_OTHER",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'')+'</td>';
				addTable.rows[i].cells[5].innerHTML =  '<div align="center"><input type="button" class="normal_btn" disabled value="删除"  name="button42" onClick="delItemOther(this);"/></div>';
			}
			sumFunc();
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
	    //
	    function setActivity(activityCode,activityName,activityFee,isFixfee,isClaim){
			//var arr = object.value.split(",");
			actIsClaim = isClaim;
			document.getElementById("ACTIVITYCOMBO").value=activityName;
			var arr = [activityCode,activityFee,isFixfee];
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
				changePart(arr[0]);
				changeOther(arr[0]);
			}
		}
		
--></SCRIPT>
</HEAD>
<BODY onload="doInit();notice();" onkeydown="keyListnerResp();">
	<div class="navigation">
		<img src="../../img/nav.gif" /> &nbsp;当前位置：售后服务管理&gt;维修登记&gt;维修工单登记
	</div>

	<form method="post" name="fm" id="fm">
		<input type="hidden" name="dealerLevel" id="dealerLevel"
			value="${dealerLevel }" /> <input type="hidden" name="attIds"
			id="attIds" value="" />
		<!-- 删除附件隐藏 -->
		<input type="hidden" name="BRAND_NAME0" id="BRAND_NAME0" value="" /> <input
			type="hidden" name="mastMileage" id="mastMileage" value="" /> <input
			type="hidden" name="FORLSTATUS" id="FORLSTATUS"
			value="<%=tawep.getForlStatus()%>" /> <input type="hidden"
			name="flag" id="flag" value="${flag}" /> <input type="hidden"
			name="SERIES_NAME0" id="SERIES_NAME0" value="" /> <input
			type="hidden" name="MODEL_NAME0" id="MODEL_NAME0" value="" /> <input
			type="hidden" name="ENGINE_NO0" id="ENGINE_NO0" value="" /> <input
			type="hidden" name="REARAXLE_NO0" id="REARAXLE_NO0" value="" /> <input
			type="hidden" name="GEARBOX_NO0" id="GEARBOX_NO0" value="" /> <input
			type="hidden" name="TRANSFER_NO0" id="TRANSFER_NO0" value="" /> <input
			type="hidden" name="BRAND_CODE0" id="BRAND_CODE0" value="" /> <input
			type="hidden" name="SERIES_CODE0" id="SERIES_CODE0" value="" /> <input
			type="hidden" name="MODEL_CODE0" id="MODEL_CODE0" value="" /> <input
			type="hidden" id="CAMPAIGN_CODE" name="CAMPAIGN_CODE"
			value="<%=tawep.getCamCode()%>" /> <input type="hidden"
			id="resultType" value="<%=tawep.getIsWash()%>" /> <input
			type="hidden" name="repareType" id="repareType"
			value="<%=tawep.getRepairTypeCode()%>" /> <input type="hidden"
			name="ID" id="my_ro_id" value="<%=id%>" /> <input type="hidden"
			name="timeId" id="timeId" /> <input type="hidden" name="hasWr"
			id="hasWr" value="<%=tawep.getPrimaryRoNo()%>" /> <input
			type="hidden" name="CL_DOWN" id="CL_DOWN" value="<%=cl %>" /> <input
			type="hidden" name="GS_DOWN" id="GS_DOWN" value="<%=gs %>" /> <input
			type="hidden" name="list01" id="list01"
			value="<%=request.getAttribute("BUSINESS_CHNG_CODE_01")%>" /> <input
			type="hidden" name="list02" id="list02"
			value="<%=request.getAttribute("BUSINESS_CHNG_CODE_02")%>" /> <input
			type="hidden" name="list03" id="list03"
			value="<%=request.getAttribute("BUSINESS_CHNG_CODE_03")%>" /> <input
			type="hidden" name="list04" id="list04"
			value="<%=request.getAttribute("BUSINESS_CHNG_CODE_04")%>" /> <input
			type="hidden" name="OTHERFEE" id="OTHERFEE"
			value="<%=request.getAttribute("OTHERFEE")%>" /> <input
			type="hidden" name="ACTIVITYCOMBO0" id="ACTIVITYCOMBO0"
			value="<%= request.getAttribute("ACTIVITYCOMBO")%>" /> <input
			type="hidden" name="DEALER_CODE"
			value="<%=CommonUtils.checkNull(tawep.getDealerCodeS())%>" />
		<table border="0" align="center" cellpadding="1" cellspacing="1"
			class="table_edit">
			<th colspan="6"><img class="nav" src="../../img/subNav.gif" />
				基本信息</th>
			<tr>
				<td class="table_edit_2Col_label_7Letter">工单号：</td>
				<td align="left"><input type="hidden" name='RO_NO' id='RO_NO'
					class="middle_txt" readonly='true' datatype="0,is_digit_letter,20"
					value='<%=CommonUtils.checkNull(tawep.getRoNo())%>' /> <%=CommonUtils.checkNull(tawep.getRoNo())%>
				</td>
			</tr>
			<tr>
				<td class="table_edit_2Col_label_7Letter">经销商代码：</td>
				<td><input type="hidden" name="dealerId"
					value="<%=CommonUtils.checkNull(tawep.getDealerIdS())%>" /> <%=CommonUtils.checkNull(tawep.getDealerCodeS())%>
				</td>
				<td class="table_edit_2Col_label_7Letter">经销商名称：</td>
				<td><%=CommonUtils.checkNull(tawep.getDealerName())%></td>

			</tr>
			<tr>
				<td class="table_edit_2Col_label_7Letter">经销商电话：</td>
				<td><%=CommonUtils.checkNull(tawep.getDealerPhone())%></td>
				<td class="table_edit_2Col_label_7Letter">维修类型：</td>
				<td align="left"><input type="hidden" name="REPAIR_TYPE"
					value="<%=tawep.getRepairTypeCode()%>" /> <script
						type="text/javascript">
	              		genSelBoxExp("REPAIR_TYPE_1",<%=Constant.REPAIR_TYPE%>,"<%=tawep.getRepairTypeCode()%>",false,"short_sel","","false",'');
	       			</script></td>
			</tr>
			<tr>
				<td class="table_edit_2Col_label_7Letter">工单开始时间：</td>
				<td nowrap="nowrap"><%=CommonUtils.checkNull(Utility.handleDate1(tawep.getRoCreateDate()))%>
					<input type="hidden" name="RO_STARTDATE" id="RO_STARTDATE"
					class="short_txt"
					value='<%=CommonUtils.checkNull(Utility.handleDate1(tawep.getRoCreateDate()))%>'
					style="width: 100px" readonly="readonly" /></td>
				<td class="table_edit_2Col_label_7Letter">结算基地：</td>
				<td><input name="YIELDLY_TYPE" id="YIELDLY_TYPE"
					value="<%=tawep.getBalanceYieldly() %>" type="hidden" /> <script
						type="text/javascript">
				            genSelBoxExp("YIELDLY_TYPE_01",<%=Constant.PART_IS_CHANGHE%>,"<%=tawep.getBalanceYieldly() %>",false,"short_sel","","false",'');
				        </script></td>
				<td class="table_edit_2Col_label_7Letter" style="display: none">
					预计工单结束时间：</td>
				<td style="display: none"><input type="text" name="RO_ENDDATE"
					id="RO_ENDDATE" class="short_txt"
					value='<%=CommonUtils.checkNull(Utility.handleDate(tawep.getDeliveryDate()))%>'
					datatype="0,is_date,10" group="RO_STARTDATE,RO_ENDDATE"
					hasbtn="true"
					callFunction="showcalendar(event, 'RO_ENDDATE', false);" /></td>
			</tr>
			<tr class="table_edit_2Col_label_7Letter" style="display: none">
				<td class="table_edit_2Col_label_7Letter">质量等级：</td>
				<td><script type="text/javascript">
				            genSelBoxExp("QUELITY_GRATE",<%=Constant.QUELITY_GRATE%>,"<%=tawep.getQuelityGrate() %>",true,"short_sel","","true",'');
				        </script></td>
				<td colspan="2"></td>
			</tr>
			<tr>
				<td class="table_edit_2Col_label_7Letter">进厂里程数：</td>
				<td>
				<input type="hidden" name="QUELITY_GRATE" id="QUELITY_GRATE" value="<%=Constant.QUELITY_GRATE_01%>" />
				<input type='text' name='IN_MILEAGE' id='IN_MILEAGE'
					datatype="0,is_double,8" maxlength="8" blurback="true" 
					class="middle_txt" readonly /> <script type="text/javascript">
							function fmtScience(){
								var a = <%=tawep.getInMileage()%> ;
								$('IN_MILEAGE').value = a;
							}
							fmtScience();
						</script></td>
				<td class="table_edit_2Col_label_7Letter">索  赔  员：</td>
				<td>
				<input type='text' maxlength="15"
							 class="middle_txt" value="<%=logonUser.getName()%>" />
				<input type='hidden' name='SERVE_ADVISOR' id='SERVE_ADVISOR'
					maxlength="10"
					value='<%=CommonUtils.checkNull(tawep.getServiceAdvisor())%>'
					class="middle_txt" />
				</td>

			</tr>
			<tr id="activity" style="display: none">
				<td id="activityTableId" align="right">活动名称：</td>
				<td id="activityTableId0" colspan="3"><%=tawep.getCampaignName()%>
					<input readonly="readonly" type="hidden"
					value="<%=tawep.getCampaignName()%>" id="ACTIVITYCOMBO"
					name="ACTIVITYCOMBO" /></td>
				<td id="activityTableId1" align="right" style="display: none">
					是否固定费用： <%if (tawep.getCamFix()==1){ %> <input type="checkbox"
					id="IS_FIX0" name="IS_FIX0" checked disabled style="display: none" />
					<%}else { %> <input type="checkbox" id="IS_FIX0" name="IS_FIX0"
					disabled style="display: none" /> <% } %> <input type="hidden"
					id="IS_FIX" name="IS_FIX" value="" /> <input type="hidden"
					id="CAMPAIGN_FEE" name="CAMPAIGN_FEE" value="" /> <input
					type="hidden" id="Activity_Type" name="Activity_Type"
					value="<%=tawep.getActivityType()%>" />
				</td>
			</tr>
			<tr id="freeTime" style="display: none">
				<td align="center" colspan="4">需要第<input readonly="true"
					type="text" class="mini_txt" value="<%=tawep.getFreeTimes() %>"
					name="freeTimes" id="freeTimes" />次保养
				</td>
			</tr>
			<tr>
			</tr>
		</table>
		<table class="table_edit">
			<th colspan="6"><img class="nav" src="../../img/subNav.gif" />
				车辆信息</th>
			<tr>
				<td align="right" class="table_edit_3Col_label_7Letter">VIN：</td>
				<td align="left"><input type='text' name='VIN' id='VIN'
					datatype="0,is_vin" readonly="readonly"
					value='<%=CommonUtils.checkNull(tawep.getVin())%>'
					class="middle_txt" /> <input type="hidden" name="VIN_FOR"
					ID="VIN_FOR" value='<%=CommonUtils.checkNull(tawep.getVin())%>' />
					<input type="hidden" name="VIN" ID="VIN"
					value='<%=CommonUtils.checkNull(tawep.getVin())%>' /> <!--  <a href="#" onClick="javascript:checkVin();">检查</a>-->
				</td>
				<td align="right" class="table_edit_3Col_label_7Letter"><span
					class="zi">发动机号：</span></td>
				<td align="left"><%=CommonUtils.checkNull(tawep.getEngineNo())%>
					<input type="hidden" id="ENGINE_NO" name="ENGINE_NO"
					value="<%=CommonUtils.checkNull(tawep.getEngineNo())%>"
					class="short_txt" style="width: 100PX" readonly /></td>
				<td align="right" class="table_edit_3Col_label_7Letter"><span
					class="zi">牌照号：</span></td>
				<td><input type="text"
					value="<%=CommonUtils.checkNull(tawep.getLicense())%>"
					name="LICENSE_NO" id="LICENSE_NO" class="short_txt" maxlength="15" />
				</td>
			</tr>
			<tr>
				<td align="right" class="table_edit_3Col_label_7Letter">品牌：</td>
				<td id="BRAND_NAME"><%=CommonUtils.checkNullEx(tawep.getBrandName())%>
				</td>
				<td align="right" class="table_edit_3Col_label_7Letter">车系：</td>
				<td id="SERIES_NAME"><%=CommonUtils.checkNull(tawep.getSeriesName())%>
				</td>
				<td align="right" class="table_edit_3Col_label_7Letter">车型：</td>
				<td id="MODEL_NAME"><%=CommonUtils.checkNull(tawep.getModelCode())%></td>
			</tr>
			<tr>
				<td align="right" class="table_edit_3Col_label_7Letter"><span
					class="zi">产地：</span></td>
				<td id="YIELDLY"><%=CommonUtils.checkNull(tawep.getYieldlyName())%>
					<input type="hidden" name="YIELDLY"
					value="<%=CommonUtils.checkNull(tawep.getYieldly())%>" /></td>
				<td align="right" class="table_edit_3Col_label_7Letter"><span
					class="zi">购车日期：</span></td>
				<td id="GUARANTEE_DATE_ID"><%=CommonUtils.checkNull(Utility.handleDate(tawep.getGuaranteeDate()))%>
					<input type="hidden" name="carTime" id="carTime"
					value="<%=CommonUtils.checkNull(Utility.handleDate(tawep.getGuaranteeDate()))%>" />
					<input type="hidden" name=carType id="carType"
					value="<%=tawep.getRepairTypeCode()%>" /></td>
				<td align="right" class="table_edit_3Col_label_7Letter"><span
					class="zi">生产日期：</span></td>
				<td id="GUARANTEE_DATE_ID"><%=CommonUtils.checkNull(Utility.handleDate(tawep.getProductDate()))%>
				</td>
				<input type="hidden" name="GUARANTEE_DATE" id="GUARANTEE_DATE"
					class="short_txt"
					value='<%=CommonUtils.checkNull(Utility.handleDate(tawep.getGuaranteeDate()))%>'
					hasbtn="true"
					callFunction="showcalendar(event, 'GUARANTEE_DATE', false);" />
			</tr>
			<tr>
				<td align="right" class="table_edit_3Col_label_7Letter"><span
					class="zi">车主姓名：</span></td>
				<td id="CTM_NAME"><%=CommonUtils.checkNull(tawep.getOwnerName())%>
				</td>
				<input type="hidden" name="CTM_NAME_1" id="CTM_NAME_1" />
				<td align="right" class="table_edit_3Col_label_7Letter">配置：</td>
				<td id="packageName"><%=CommonUtils.checkNull(tawep.getPackageName())%></td>
				<td align="right" class="table_edit_3Col_label_7Letter">三包规则代码：</td>
				<td id="GUARANTEE_CODE"></td>
			</tr>
			<tr>
				<td class="table_edit_3Col_label_6Letter"><span class="zi">车主地址：</span>
				</td>
				<td id="DELIVERER_ADRESS"><%=CommonUtils.checkNull(tawep.getDelivererAdress()) %>
				</td>
				<td class="table_edit_3Col_label_6Letter"><span class="zi">车辆用途：</span>
				</td>
				<td id="car_use_desc"><script type="text/javascript">writeItemValue('<%=CommonUtils.checkNull(tawep.getCarUseType()) %>')</script>
				</td>
				<td class="table_edit_3Col_label_6Letter"><span class="zi">颜色：</span>
				</td>
				<td id="car_color">
					<%=CommonUtils.checkNull(tawep.getColor()) %>
				</td>
			</tr>
		</table>
		<table class="table_edit">
			<th colspan="6"><img class="nav" src="../../img/subNav.gif" />
				用户信息</th>
			<tr>
				<td class="table_edit_3Col_label_7Letter">送修人姓名：</td>
				<td><input type='text' maxlength="15" name='DELIVERER'
					id='DELIVERER'
					value="<%=CommonUtils.checkNull(tawep.getDeliverer()) %>"
					datatype="1,is_textarea,100" class="middle_txt" /> <!--  <a href="#" onClick="javascript:checkVin();">检查</a>-->
				</td>
				<td class="table_edit_3Col_label_6Letter"><span class="zi">送修人电话：</span>
				</td>
				<td align="left"><input type="text" maxlength="17"
					name="DELIVERER_PHONE" id="DELIVERER_PHONE"
					value="<%=CommonUtils.checkNull(tawep.getDelivererPhone()) %>"
					maxlength="17" class="middle_txt" /></td>
			</tr>
			<tr>
				<td class="table_edit_3Col_label_7Letter">送修人手机：</td>
				<td><input type='text' name='DELIVERER_MOBILE'
					id='DELIVERER_MOBILE' maxlength="11" class="middle_txt"
					value="<%=CommonUtils.checkNull(tawep.getDelivererMobile()) %>"
					datatype="1,is_digit,11" /> <!--  <a href="#" onClick="javascript:checkVin();">检查</a>-->
				</td>
			</tr>
		</table>
		<table id="itemTableId" border="0" align="center" cellpadding="0"
			cellspacing="1" class="table_list">
			<th colspan="11" align="left"><img class="nav"
				src="../../img/subNav.gif" /> 维修工时</th>
			<tr align="center" class="table_list_row1">
				<td>作业代码</td>
				<td>作业名称</td>
				<td>工时定额</td>
				<td>工时单价</td>
				<td>工时金额(元)</td>
				<td>付费方式</td>
				<td>故障代码</td>
				<td><input id="itemBtn" type="button" class="normal_btn"
					value="新增" name="button422"
					onClick="javascript:addRow('itemTable');" /></td>
			</tr>
			<tbody id="itemTable">
				<%
						for (int i = 0; i < itemLs.size(); i++) {
							TtAsRoLabourBean tl = (TtAsRoLabourBean)itemLs.get(i);
					%>
				<tr class="table_list_row1">
					<td><input type="text" class="phone_txt" name="WR_LABOURCODE"
						value="<%=CommonUtils.checkNull(tl.getWrLabourcode())%>" size="10"
						readonly datatype="0,is_null" id="WR_LABOURCODES<%=i %>" /> <!--  <a href="#" onclick="selectMainTime(this);">选择</a>-->
					</td>
					<td><span class="tbwhite"> <input type="text"
							class="long_txt" name="WR_LABOURNAME"
							value="<%=CommonUtils.checkNull(tl.getWrLabourname())%>"
							size="10" readonly /> <input type="hidden" class="long_txt"
							name="FORE" value="<%=CommonUtils.checkNull(tl.getIsFore())%>"
							size="10" readonly /> <input type="hidden" class="long_txt"
							name="LEVELS" value="<%=tl.getForeLevel()%>" size="10" readonly />
					</span></td>
					<td><input type="text" name="LABOUR_HOURS" class="little_txt"
						value="<%=CommonUtils.checkNull(tl.getStdLabourHour())%>" size="8"
						maxlength="11" id="LABOUR_HOURS" readonly /></td>
					<td><input type="text" name="LABOUR_PRICE" class="little_txt"
						value="<%=CommonUtils.checkNull(tl.getLabourPrice())%>" size="8"
						maxlength="11" id="LABOUR_PRICE" readonly /> <input type="hidden"
						name="LABOUR_ID" value="<%=tl.getId()%>" /></td>
					<td><input type="text" name="LABOUR_AMOUNT"
						id="LABOUR_AMOUNTS<%=i %>" class="little_txt"
						value="<%=CommonUtils.checkNull(tl.getLabourAmount())%>" size="8"
						maxlength="9" readonly /></td>
					<%  if("11441005".equalsIgnoreCase(tawep.getRepairTypeCode())&& !"10561001".equalsIgnoreCase(tawep.getActivityType().toString())){%>
					<td><input type="text" name="PAY_TYPE_ITEM_txt"
						class="little_txt" value="自费" size="8" maxlength="9" readonly /> <input
						type="hidden" name="PAY_TYPE_ITEM" class="little_txt"
						value="11801001" size="8" maxlength="9" readonly /></td>
					<%} else {%>
					<td><script type="text/javascript">
	              				genSelBoxExp("PAY_TYPE_ITEM",<%=Constant.PAY_TYPE%>,"<%=tl.getPayType()%>",false,"min_sel","","false",'');
	       					</script></td>
					<%} %>


					<td><input disabled="disabled" id="MALFUNCTIONS"
						name="MALFUNCTIONS" class="middle_txt"
						value="<%=tl.getMalName() %>" /><input type="hidden"
						id="MALFUNCTION" name="MALFUNCTION" class="middle_txt"
						value="<%=tl.getMalFunction() %>" /><a href="#"
						onclick="selectMalCode(this);">选择</a></td>
					<td>
						<div>
							<input type="button" style="display: none" class="normal_btn"
								value="新增附加" name="button42"
								onClick="addPlusRow('itemTable',this,'<%=tl.getWrLabourcode()%>');"></input>
							<input type="button" class="normal_btn" value="删除"
								name="button42"
								onClick="delItem(this.parentNode,'item');delItemIds('<%=tl.getId() %>','del');"></input>
						</div>
						<div></div>
						<div></div>
					</td>
				</tr>
				<%
					}
					%>
			</tbody>
		</table>
		<table id="partTableId" border="0" align="center" cellpadding="0"
			cellspacing="1" class="table_list">
			<th colspan="18" align="left"><img class="nav"
				src="../../img/subNav.gif" /> 维修配件</th>
			<tr align="center" class="table_list_row1">
				<td style="width: 24px;">是否三包</td>
				<td>新件代码</td>
				<td>新件名称</td>
				<td style="width: 26px;">新件数量</td>
				<td>单价</td>
				<td>金额(元)</td>
				<td>付费方式</td>
				<td>作业代码</td>
				<td>关联主因件</td>
				<td>责任性质</td>
				<td>配件是否有库存</td>
				<td>配件维修类型</td>
				<td>故障描述</td>
				<td>故障原因</td>
				<td>维修措施</td>
				<td>索赔自费工单</td>
				<td><input id="partBtn" type="button" class="normal_btn"
					value="新增" name="button422"
					onClick="javascript:genAppTimeCombo1(addRow('partTable'));" /></td>
			</tr>
			<tbody id="partTable">
				<%
						for (int i = 0; i < partLs.size(); i++) {
							if(partLs.get(i).getResponsNature().intValue()==Constant.RESPONS_NATURE_STATUS_01){ 
								outMainPart=partLs.get(i).getPartNo()+","+outMainPart;
								outMainPartName = partLs.get(i).getPartName()+","+outMainPartName;
								}
					%>
				<%
					TtAsRoRepairPartBean tl = partLs.get(i);
							//TtAsWrPartsitemPO tl = (TtAsWrPartsitemPO)clb.getMain();
					%>
				<tr class="table_list_row1">
					<td>
						<%if (tl.getIsGua()==1) {%> <input type="checkbox" style="width: 24px;" name="IS_GUA"
						checked value="1" onclick="return false;" /> <%}else { %> <input
						type="checkbox" style="width: 24px;" value="0" name="IS_GUA" onclick="return false;" />
						<%} %>
					</td>
					<td><input type="text" style="width: 30px;" name="PART_CODE" class="phone_txt"
						value="<%=CommonUtils.checkNull(tl.getPartNo())%>" size="10"
						id="PART_CODE" readonly datatype="0,is_null" /> <span
						class="tbwhite"> <!-- 	<a href="#" onClick="javascript:selectMainPartCode(this);delPartIds('<%=tl.getId()%>','del')">选择</a> -->
					</span></td>

					<td><span class="tbwhite"> <input type="text"
							name="PART_NAME" style="width: 45px;" class="phone_txt" readonly
							value="<%=CommonUtils.checkNull(tl.getPartName())%>"
							id="PART_NAME" name="PART_SN3" size="10" /> <input type="hidden"
							class="long_txt" name="PARTFORE"
							value="<%=CommonUtils.checkNull(tl.getIsFore())%>" size="10"
							readonly /> <input type="hidden" class="long_txt"
							name="PARTLEVEL" value="<%=tl.getForeLevel()%>" size="10"
							readonly />
					</span></td>
					<td><input type="text" style="width: 24px;" class="little_txt"
						datatype="0,is_double,10" decimal="2" onkeyup="countQuantity(this);"
						name="QUANTITY" id="QUANTITY<%=i%>"
						value="<%=(tl.getPartQuantity().intValue())%>" maxlength="20" />
					</td>
					<td><input type="text" style="width: 35px;" class="little_txt" name="PRICE"
						value="<%=CommonUtils.checkNull(tl.getPartCostPrice())%>"
						size="10" maxlength="11" id="PRICE" readOnly="true" /></td>
					<td><input type="text" style="width: 35px;" class="little_txt" name="AMOUNT"
						id="AMOUNT" size="10"
						value="<%=CommonUtils.checkNull(tl.getPartCostAmount())%>"
						maxlength="9" readonly /></td>

					<%  if("11441005".equalsIgnoreCase(tawep.getRepairTypeCode())&& !"10561001".equalsIgnoreCase(tawep.getActivityType().toString())){%>
					<td><input type="text" style="width: 45px;" name="PAY_TYPE_PART_txt"
						class="little_txt" value="自费" size="8" maxlength="9" readonly /> <input
						type="hidden" name="PAY_TYPE_PART" class="little_txt"
						value="11801001" size="8" maxlength="9" readonly /></td>
					<%} else {%>
					<td><script type="text/javascript">
	              				genSelBoxExp("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"<%=tl.getPayType()%>",false,"min_sel","","false",'');
	       					</script></td>
					<%} %>
					<td><select style="width: 60px;" id="Labour" value="" name="Labour0">
							<% for(int j=0;j<itemLs.size();j++){%>
							<option value="<%=itemLs.get(j).getWrLabourcode()%>"
								<% if(itemLs.get(j).getWrLabourcode().equalsIgnoreCase(tl.getLabour())){%>
								selected="selected" <%} %>>
								<%=itemLs.get(j).getWrLabourname()%>
							</option>
							<%}%>
					</select></td>


					<td><select style="width: 60px;" id="mainPartCode" value="" name="mainPartCode">
							<%
							if(tl.getResponsNature().intValue()==Constant.RESPONS_NATURE_STATUS_02){
								for(int j =0;j<partLs.size();j++){
									if(partLs.get(j).getResponsNature().intValue()==Constant.RESPONS_NATURE_STATUS_01){ 
										%>
							<option value="<%=partLs.get(j).getPartNo()%>"
								<% 	if(partLs.get(j).getPartNo().equalsIgnoreCase(tl.getMainPartCode())){
										%>
								selected="selected" <%
										}%>>
								<%=partLs.get(j).getPartNo()%>
							</option>
							<%} }
							}else{%>
							<option value="-1">--请选择--</option>
							<%}%>

					</select></td>
					<td><select style="width: 55px" onchange="setPartCodes();"
						name="RESPONS_NATURE" id="RESPONS_NATURE">
							<option value="<%=Constant.RESPONS_NATURE_STATUS_01%>"
								<% if(Constant.RESPONS_NATURE_STATUS_01==tl.getResponsNature().intValue()){ %>
								selected <%} %>>主因件</option>
							<option value="<%=Constant.RESPONS_NATURE_STATUS_02%>"
								<% if(Constant.RESPONS_NATURE_STATUS_02==tl.getResponsNature().intValue()){ %>
								selected <%} %>>次因件</option>
					</select></td>
					<td><script type="text/javascript">
								genSelBoxExp("HAS_PART",<%=Constant.IF_TYPE%>,"<%=tl.getHasPart().intValue()%>",false,"min_sel","","false",'');
	       					</script></td>
					<td><select style="width: 55px"
						onchange="changeQ(this,'<%=i %>');" name="PART_USE_TYPE"
						id="PART_USE_TYPE<%=i %>">
							<option value="95431001" <% if(95431001==tl.getPartUseType()){ %>
								selected <%} %>>维修</option>
							<option value="95431002" <% if(95431002==tl.getPartUseType()){ %>
								selected <%} %>>更换</option>
					</select></td>
			<td><textarea rows="4" cols="8" class="middle_txt" name="TROUBLE_DESCRIBE"   maxlength="2000" id="TROUBLE_DESCRIBE" ><%=CommonUtils.checkNull(tl.getTroubleDescribe())%></textarea></td>
			<td><textarea rows="4" cols="8" class="middle_txt" name="TROUBLE_REASON" id="TROUBLE_REASON" maxlength="2000" ><%=CommonUtils.checkNull(tl.getTroubleReason())%></textarea></td>
			<td><textarea rows="4" cols="8" name="DEAL_METHOD" ID="DEAL_METHOD" class="middle_txt" maxlength="2000" ><%=CommonUtils.checkNull(tl.getDealMethod())%></textarea></td>
			<td><textarea rows="4" cols="8" name="zfRoNo" ID="zfRoNo" readonly class="middle_txt" maxlength="2000" ><%=CommonUtils.checkNull(tl.getZfRono())%></textarea></td>
					<td><input type="button" class="normal_btn" value="删除"name="button42" onClick="javascript:delPartItem(this,'part');delPartIds('<%=tl.getId()%>','del');" />
						<input type="hidden" class="little_txt"
						value="<%=CommonUtils.checkNull(tl.getRealPartId())%>"
						name="REAL_PART_ID" /> <input type="hidden" name="PART_ID"
						value="<%=tl.getId()%>" /></td>
						<td><input type="button" class="normal_btn" onClick="showUploadWithIdByupdate('<%=request.getContextPath()%>','<%=tl.getPartNo()%>');" value="追加附件" /></td>
				</tr>
				<%
						}
				if(!"".equalsIgnoreCase(outMainPart)){
					outMainPart = outMainPart.substring(0,outMainPart.length()-1);
					outMainPartName=outMainPartName.substring(0,outMainPartName.length()-1);
				}
					%>
			</tbody>
			
		</table>
				<% 
					if(!tawep.getRepairTypeCode().equalsIgnoreCase("11441004")){
					%>
					<tr>
					<td width="100%" colspan="9"><jsp:include page="${contextPath}/uploadDiv2.jsp" /></td>
					</tr>
					<%for(int i=0;i<attachLs.size();i++) { %>
	    			<script type="text/javascript">
	    			addUploadRowByDbShow('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getPjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
	    			</script>
	    			<%} %> 
					<%
					}
				%>
				 
		<table id="otherTableId" align="center" cellpadding="0"
			cellspacing="1" class="table_list">
			<th colspan="8" align="left"><img class="nav"
				src="../../img/subNav.gif" /> 其他项目</th>
			<tr align="center" class="table_list_row1">
				<td>
					<div align="center">项目代码</div>
				</td>
				<td>
					<div align="center">项目名称</div>
				</td>
				<td>
					<div align="center">金额(元)</div>
				</td>
				<td id="RemarkPDI">备注</td>
				<td>付费方式</td>
				<td><input id="otherBtn" type="button" class="normal_btn"
					value="新增" name="button422"
					onClick="javascript:addRow('otherTable');" /></td>
			</tr>
			<tbody id="otherTable">
				<%
					if("11441005".equalsIgnoreCase(tawep.getRepairTypeCode())){
						for (int i = 0; i < otherLs.size(); i++) { 
					%>
				<tr class="table_list_row1">
					<td>
						<div align="center">
							<span class="tbwhite"><input type="hidden"
								name="ITEM_CODE" readonly id="ITEM_CODE"
								value="<%=otherLs.get(i).getAddItemCode()%>" class="short_txt"
								datatype="0,is_digit_letter_cn,100" /> <%=otherLs.get(i).getAddItemCode()%>
							</span>
						</div>
					</td>
					<%if("3537005".equalsIgnoreCase(otherLs.get(i).getAddItemCode())){ %>
					<td>
						<div align="center">
							<span class="tbwhite"><input type="hidden"
								name="ITEM_NAME" readonly id="ITEM_NAME"
								value="<%=CommonUtils.checkNull(otherLs.get(i).getAddItemName())%>"
								class="short_txt" maxlength="30" /> <a href="#"
								onclick="getPartDetai();"> <%=CommonUtils.checkNull(otherLs.get(i).getAddItemName())%>
							</a> </span>
						</div>
					</td>
					<%	}else if("3537001".equalsIgnoreCase(otherLs.get(i).getAddItemCode())){ %>
					<td>
						<div align="center">
							<span class="tbwhite"><input type="hidden"
								name="ITEM_NAME" readonly id="ITEM_NAME"
								value="<%=CommonUtils.checkNull(otherLs.get(i).getAddItemName())%>"
								class="short_txt" maxlength="30" /> <a href="#"
								onclick="getPartDetai2();"> <%=CommonUtils.checkNull(otherLs.get(i).getAddItemName())%>
							</a> </span>
						</div>
					</td>
					<%	} else if("3537004".equalsIgnoreCase(otherLs.get(i).getAddItemCode())){ %>
					<td>
						<div align="center">
							<span class="tbwhite"><input type="hidden"
								name="ITEM_NAME" readonly id="ITEM_NAME"
								value="<%=CommonUtils.checkNull(otherLs.get(i).getAddItemName())%>"
								class="short_txt" maxlength="30" /> <a href="#"
								onclick="getPartDetai3();"> <%=CommonUtils.checkNull(otherLs.get(i).getAddItemName())%>
							</a> </span>
						</div>
					</td>
					<%	}else if("3537006".equalsIgnoreCase(otherLs.get(i).getAddItemCode())){ %>
					<td>
						<div align="center">
							<span class="tbwhite"><input type="hidden"
								name="ITEM_NAME" readonly id="ITEM_NAME"
								value="<%=CommonUtils.checkNull(otherLs.get(i).getAddItemName())%>"
								class="short_txt" maxlength="30" /> <a href="#"
								onclick="getPartDetai4();"> <%=CommonUtils.checkNull(otherLs.get(i).getAddItemName())%>
							</a> </span>
						</div>
					</td>
					<%	}else if("3537007".equalsIgnoreCase(otherLs.get(i).getAddItemCode())){ %>
					<td>
						<div align="center">
							<span class="tbwhite"><input type="hidden"
								name="ITEM_NAME" readonly id="ITEM_NAME"
								value="<%=CommonUtils.checkNull(otherLs.get(i).getAddItemName())%>"
								class="short_txt" maxlength="30" /> <a href="#"
								onclick="getPartDetai5();"> <%=CommonUtils.checkNull(otherLs.get(i).getAddItemName())%>
							</a> </span>
						</div>
					</td>
					<%	} else {%>
					<td>
						<div align="center">
							<span class="tbwhite"><input type="hidden"
								name="ITEM_NAME" readonly id="ITEM_NAME"
								value="<%=CommonUtils.checkNull(otherLs.get(i).getAddItemName())%>"
								class="short_txt" maxlength="30" /> <%=CommonUtils.checkNull(otherLs.get(i).getAddItemName())%>
							</span>
						</div>
					</td>
					<%} %>



					<td>
						<div align="center">
							<input type="hidden" name="ITEM_AMOUNT"
								<% if(otherLs.get(i).getAddItemCode().toString().equals("QT007")){%>
								readonly="readonly" <%} %>
								id="ITEM_AMOUNT<%=CommonUtils.checkNull(otherLs.get(i).getAddItemCode())%>"
								value="<%=CommonUtils.checkNull(otherLs.get(i).getAddItemAmount())%>"
								datatype="0,is_yuan" class="short_txt" />
							<%=CommonUtils.checkNull(otherLs.get(i).getAddItemAmount())%>
						</div>
					</td>
					<td>
						<div align="center">
							<input type="text" name="ITEM_REMARK" id="ITEM_REMARK"
								maxlength="50" class="middle_txt"
								value="<%=CommonUtils.checkNull(otherLs.get(i).getRemark())%>" />

						</div>
					</td>
					<td><input type="hidden" name="PAY_TYPE_OTHER"
						id="PAY_TYPE_OTHER" value="<%=otherLs.get(i).getPayType()%>" /> <script
							type="text/javascript">
			              	writeItemValue(<%=otherLs.get(i).getPayType()%>);			
			              	//genSelBoxExp("PAY_TYPE_OTHER",<%=Constant.PAY_TYPE%>,"<%=otherLs.get(i).getPayType()%>",false,"min_sel","","false",'');
			       		</script>
						<td>
							<div align="center">
								<input type="hidden" name="OTHER_ID"
									value="<%=CommonUtils.checkNull(otherLs.get(i).getId())%>" />
								<input type="button" class="normal_btn" value="删除"
									name="button42" disabled="disabled"
									onClick="javascript:delItemOther(this);delOtherIds('<%=otherLs.get(i).getId() %>','del');" />
							</div>
					</td>
				</tr>
				<%
						}
					}else {
						for (int i = 0; i < otherLs.size(); i++) {
							System.out.println(otherLs.size()+"-=-----");
							code=otherLs.get(i).getMainPartCode();
							%>
				<tr class="table_list_row1">
					<c:choose>
						<c:when
							test='<%="QT006".equalsIgnoreCase(otherLs.get(i).getAddItemCode())||"QT011".equalsIgnoreCase(otherLs.get(i).getAddItemCode())||"QT007".equalsIgnoreCase(otherLs.get(i).getAddItemCode())||"QT008".equalsIgnoreCase(otherLs.get(i).getAddItemCode())||"QT009".equalsIgnoreCase(otherLs.get(i).getAddItemCode())||"QT010".equalsIgnoreCase(otherLs.get(i).getAddItemCode())%>'>
							<td>
								<div align="center">
									<span class="tbwhite"><input type="text"
										name="ITEM_CODE" readonly id="ITEM_CODE"
										value="<%=otherLs.get(i).getAddItemCode()%>" class="short_txt"
										datatype="1,is_digit_letter_cn,100" /> </span>
								</div>
							</td>
						</c:when>
						<c:otherwise>
							<td>
								<div align="center">
									<select onchange="setOtherName(this);" id="ITEM_CODE<%=i%>"
										name="ITEM_CODE">
										<script type="text/javascript">var tec = document.getElementById('OTHERFEE').value;
											document.write(tec);assignSelect('ITEM_CODE'+'<%=i%>','<%=otherLs.get(i).getAddItemCode()%>');</script>
									</select>
								</div>
							</td>

						</c:otherwise>
					</c:choose>
					<td>
						<div align="center">
							<span class="tbwhite"><input type="text" name="ITEM_NAME"
								readonly id="ITEM_NAME"
								value="<%=CommonUtils.checkNull(otherLs.get(i).getAddItemName())%>"
								class="short_txt" datatype="1,is_digit_letter_cn,100" /> </span>
						</div>
					</td>
					<td>
						<div align="center">
							<input type="text" name="ITEM_AMOUNT"
								<% if(otherLs.get(i).getAddItemCode().toString().equals("QT007")){%>
								readonly="readonly" <%} %>
								id="ITEM_AMOUNT<%=CommonUtils.checkNull(otherLs.get(i).getAddItemCode())%>"
								value="<%=CommonUtils.checkNull(otherLs.get(i).getAddItemAmount())%>"
								datatype="0,is_yuan" class="short_txt" />
						</div>
					</td>
					<td>
						<div align="center">
							<input type="text" name="ITEM_REMARK" id="ITEM_REMARK"
								maxlength="50" class="middle_txt"
								value="<%=CommonUtils.checkNull(otherLs.get(i).getRemark())%>" />

						</div>
					</td>
					<td><script type="text/javascript">
			              				genSelBoxExp("PAY_TYPE_OTHER",<%=Constant.PAY_TYPE%>,"<%=otherLs.get(i).getPayType()%>",false,"min_sel","","false",'');
			       					</script></td>

					<c:choose>
						<c:when
							test='<%="QT006".equalsIgnoreCase(otherLs.get(i).getAddItemCode())||"QT007".equalsIgnoreCase(otherLs.get(i).getAddItemCode())||"QT008".equalsIgnoreCase(otherLs.get(i).getAddItemCode())||"QT009".equalsIgnoreCase(otherLs.get(i).getAddItemCode())||"QT010".equalsIgnoreCase(otherLs.get(i).getAddItemCode())||"QT011".equalsIgnoreCase(otherLs.get(i).getAddItemCode())%>'>
							<td>
								<div align="center">
									<input type="hidden" name="OTHER_ID"
										value="<%=CommonUtils.checkNull(otherLs.get(i).getId())%>" />
									<input type="button" class="normal_btn" value="删除"
										disabled="disabled" name="button42"
										onClick="javascript:delItemOther(this);delOtherIds('<%=otherLs.get(i).getId() %>','del');" />
								</div>
							</td>
						</c:when>
						<c:otherwise>
							<td>
								<div align="center">
									<input type="hidden" name="OTHER_ID"
										value="<%=CommonUtils.checkNull(otherLs.get(i).getId())%>" />
									<input type="button" class="normal_btn" value="删除"
										name="button42"
										onClick="javascript:delItemOther(this);delOtherIds('<%=otherLs.get(i).getId() %>','del');" />
								</div>
							</td>

						</c:otherwise>
					</c:choose>
				</tr>
				<%
						}
					}
					%>
			</tbody>
		</table>
		<TABLE class="table_edit" style="display: none">
			<tr>
				<th colspan="10"><img src="../../img/subNav.gif" alt=""
					class="nav" /> 申请费用</th>
			</tr>
		</TABLE>
		<table class="table_edit" style="display: none">
			<tr>
				<td class="table_edit_4Col_label_7Letter" nowrap="nowrap">基本工时：</td>
				<td id="BASE_LABOUR">
					<%//=tawep.getLabourHours() %>
				</td>
				<td class="table_edit_4Col_label_7Letter" nowrap="nowrap">基本工时金额：</td>
				<td nowrap="nowrap" id="BASE_LABOUR_AMOUNT"><%=CommonUtils.formatPrice(tawep.getLabourAmount()) %></td>
				<td class="table_edit_4Col_label_7Letter" nowrap="nowrap">附加工时：</td>
				<td id="ADD_LABOUR">0.0</td>
				<td class="table_edit_4Col_label_7Letter" nowrap="nowrap">附加工时金额：</td>
				<td id="ADD_LABOUR_AMOUNT">0.0</td>
			</tr>
			<tr>
				<td class="table_edit_4Col_label_7Letter" nowrap="nowrap">配件金额：</td>
				<td id="ALL_PART_AMOUNT">
					<%//=CommonUtils.formatPrice(tawep.getPartAmount()) %>
				</td>
				<td class="table_edit_4Col_label_7Letter" nowrap="nowrap">工时金额：</td>
				<td id="ALL_LABOUR_AMOUNT"><%=CommonUtils.formatPrice(tawep.getLabourAmount()) %></td>
				<td class="table_edit_4Col_label_7Letter" nowrap="nowrap">其它费用金额：</td>
				<td id="OTHER_AMOUNT">
					<%//=CommonUtils.formatPrice(tawep.getNetitemAmount()) %>
				</td>
				<td class="table_edit_4Col_label_7Letter" nowrap="nowrap">保养金额：</td>
				<td id="GAME_AMOUNT">
					<%//=CommonUtils.formatPrice(tawep.getFreeMPrice()) %>
				</td>
			</tr>
			<tr>
				<td class="table_edit_4Col_label_7Letter" nowrap="nowrap">服务活动金额：</td>
				<td id="ACTIVITY_AMOUNT">
					<%//=CommonUtils.formatPrice(tawep.getCampaignFee()) %>
				</td>
				<td class="table_edit_4Col_label_7Letter" nowrap="nowrap">索赔申请金额：</td>
				<td id="APPLY_AMOUNT">
					<%//=CommonUtils.formatPrice(tawep.getRepairTotal()) %>
				</td>
				<td class="table_edit_4Col_label_7Letter" nowrap="nowrap">税额：</td>
				<td id="TAX">
					<%//=CommonUtils.formatPrice(tawep.getTaxSum()) %>
				</td>
				<td class="table_edit_4Col_label_7Letter" nowrap="nowrap">总金额：</td>
				<td id="ALL_AMOUNT">
					<%//=CommonUtils.formatPrice(tawep.getGrossCredit()) %>
				</td>
			</tr>
		</table>
		<table class="table_edit" id="outId" style="display: none">
			<tr>
				<th colspan="10"><img src="../../img/subNav.gif" alt=""
					class="nav" /> 外出维修</th>
			</tr>
			<%if("11441002".equals(tawep.getRepairTypeCode()) && tawep.getForlStatus()==0 || tawep.getForlStatus()==11561003){ %>
			<tr>
				<td align="right">开始时间：</td>
				<td nowrap="nowrap"><input type="text" name="START_DATE"
					id="START_DATE" class="middle_txt" datatype="0,is_datetime,20"
					group="START_DATE,END_DATE" hasbtn="true"
					value="<%=CommonUtils.checkNull(Utility.handleDateMmSs(tawep.getStartTime())) %>"
					callFunction="showcalendar(event, 'START_DATE', true);" /></td>
				<td align="right">结束时间：</td>
				<td align="left"><input type="text" name="END_DATE"
					id="END_DATE" class="middle_txt" datatype="0,is_datetime,20"
					group="START_DATE,END_DATE" hasbtn="true"
					value="<%=CommonUtils.checkNull(Utility.handleDateMmSs(tawep.getEndTime())) %>"
					callFunction="showcalendar(event, 'END_DATE', true);" /></td>
				<td align="right">派车车牌号：</td>
				<td align="left"><input type="text" name="OUT_LICENSENO"
					maxlength="15" id="OUT_LICENSENO"
					value="<%=CommonUtils.checkNull(tawep.getOutLicenseno()) %>"
					class="middle_txt" /></td>
			</tr>
			<%}else{ %>
			<tr>
				<td align="right">开始时间：</td>
				<td nowrap="nowrap">
					<!--				<input type="text" name="START_DATE" id="START_DATE"--> <!--					class="middle_txt" datatype="0,is_datetime,20"-->
					<!--					group="START_DATE,END_DATE" hasbtn="true" value="<%=CommonUtils.checkNull(Utility.handleDateMmSs(tawep.getStartTime())) %>"-->
					<!--					callFunction="showcalendar(event, 'START_DATE', true);" />-->
					<%=CommonUtils.checkNull(Utility.handleDateMmSs(tawep.getStartTime())) %>
				</td>
				<td align="right">结束时间：</td>
				<td align="left">
					<!--          	<input type="text" name="END_DATE" id="END_DATE"-->
					<!--					class="middle_txt" datatype="0,is_datetime,20"--> <!--					group="START_DATE,END_DATE" hasbtn="true" value="<%=CommonUtils.checkNull(Utility.handleDateMmSs(tawep.getEndTime())) %>"-->
					<!--					callFunction="showcalendar(event, 'END_DATE', true);" />-->
					<%=CommonUtils.checkNull(Utility.handleDateMmSs(tawep.getEndTime())) %>
				</td>
				<td align="right">派车车牌号：</td>
				<td align="left">
					<!--          	<input type="text" name="OUT_LICENSENO"  id="OUT_LICENSENO" value="<%=CommonUtils.checkNull(tawep.getOutLicenseno()) %>" class="middle_txt"/>-->
					<%=CommonUtils.checkNull(tawep.getOutLicenseno()) %>
				</td>
			</tr>
			<%} %>
			<%if("11441002".equals(tawep.getRepairTypeCode()) && tawep.getForlStatus()==0 || tawep.getForlStatus()==11561003){ %>
			<tr>
				<td align="right">外出人：</td>
				<td align="left"><input type="text" name="OUT_PERSON"
					id="OUT_PERSON" maxlength="15"
					value="<%=CommonUtils.checkNull(tawep.getOutPerson())%>"
					maxlength="20" class="middle_txt" /><span style="color: red">*</span>

				</td>
				<td align="right">外出目的地：</td>
				<td align="left"><input type="text" name="OUT_SITE"
					id="OUT_SITE" maxlength="100"
					value="<%=CommonUtils.checkNull(tawep.getOutSite()) %>"
					maxlength="20" class="middle_txt" /><span style="color: red">*</span>

				</td>

				<td align="right"><c:if test="${code.codeId==80081001}">
          		单程救急里程：
          	</c:if> <c:if test="${code.codeId==80081002}">
          		单程里程：
          	</c:if></td>
				<td align="left"><input type="text" name="OUT_MILEAGE"
					maxlength="8" blurback="true" id="OUT_MILEAGE"
					value="<%=tawep.getOutMileages()%>" datatype="1,is_double,10"
					class="middle_txt" /></td>
			</tr>
			<tr>
				<td align="right">外派关联主因件：</td>
				<td align="left"><select name="OUT_MAIN_PART"
					id="OUT_MAIN_PART" value="" style="width: 150px;">
						<option value="-1">--请选择--</option>
						<% System.out.println(outMainPart);
          		if(!"".equalsIgnoreCase(outMainPart)){
          			String str[] = outMainPart.split(",");
          			String name[] = outMainPartName.split(",");
          			for(int i=0;i<str.length;i++){System.out.println(code+"====1111111111++++++===="+str[i]);%>
						<option value="<%=str[i] %>"
							<%if(code.equalsIgnoreCase(str[i])){ out.print("selected");}%>><%=name[i] %></option>
						<%}
          		}
          		%>
				</select> <span style="color: red">*</span></td>
			</tr>
			<%}else{ %>
			<tr>
				<td align="right">外出人：</td>
				<td align="left">
					<!--          	<input type="text" name="OUT_PERSON" id="OUT_PERSON" value="<%=CommonUtils.checkNull(tawep.getOutPerson())%>" datatype="1,is_digit_letter_cn,10" class="middle_txt"/>-->
					<%=CommonUtils.checkNull(tawep.getOutPerson())%>
				</td>
				<td align="right">出差目的地：</td>
				<td align="left">
					<!--          	<input type="text" name="OUT_SITE" id="OUT_SITE" value="<%=CommonUtils.checkNull(tawep.getOutSite()) %>" datatype="1,is_digit_letter_cn,100" class="middle_txt"/>-->
					<%=CommonUtils.checkNull(tawep.getOutSite()) %>
				</td>

				<td align="right"><c:if test="${code.codeId==80081001}">
          		单程救急里程：
          	</c:if> <c:if test="${code.codeId==80081002}">
          		单程里程：
          	</c:if></td>
				<td align="left"><input type="hidden" name="OUT_MILEAGE"
					id="OUT_MILEAGE" value="<%=tawep.getOutMileages()%>"
					datatype="1,is_double,10" class="middle_txt" /> <%=tawep.getOutMileages()%>
				</td>
			</tr>
			<tr>
				<td align="right">外派关联主因件：</td>
				<td align="left"><select name="OUT_MAIN_PART"
					id="OUT_MAIN_PART" value="" style="width: 150px;">
						<option value="-1">--请选择--</option>
						<% System.out.println(outMainPart);
          		if(!"".equalsIgnoreCase(outMainPart)){
          			String str[] = outMainPart.split(",");
          			String name[] = outMainPartName.split(",");
          			for(int i=0;i<str.length;i++){%>
						<option value="<%=str[i] %>"
							<%if(code.equalsIgnoreCase(str[i])){ out.print("selected");}%>><%=name[i] %></option>
						<%}
          		}
          		%>
				</select> <span style="color: red">*</span></td>
			</tr>
			<%} %>
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
				<%
				if(compensationMoneyList!=null && compensationMoneyList.size()>0){
					for(int i=0;i<compensationMoneyList.size();i++){%>
						<tr>
						<td>
						<input type="text" name="supplier_code" class="middle_txt" value="<%=compensationMoneyList.get(i).get("SUPPLIER_CODE") %>"  id="supplier_code" readonly/><span style="color:red">*</span><a href="#" name="selectSupplierCode"  onclick="selectSupplierCode(this,'<%=compensationMoneyList.get(i).get("PKID") %>','<%=compensationMoneyList.get(i).get("PART_CODE") %>');">选择</a>
						</td>
						<td>
						<input name="apply_price" id="apply_price" value="<%=compensationMoneyList.get(i).get("APPLY_PRICE") %>" type="text" class="middle_txt" readonly><span style="color:red">*</span>
						</td>
						<td>
						<input name="pass_price" id="pass_price" value="<%=compensationMoneyList.get(i).get("PASS_PRICE") %>" type="text" class="middle_txt" readonly><span style="color:red">*</span>
						</td>
						<td>
				<select name="part_code_temp"
					id="part_code_temp" value="" style="width: 150px;">
						<option value="-1">--请选择--</option>
					<% if(!"".equalsIgnoreCase(outMainPart)){
	          			String str[] = outMainPart.split(",");
	          			String name[] = outMainPartName.split(",");
	          			for(int m=0;m<str.length;m++){%>
						<option value="<%=str[m] %>"
						<%
							if(String.valueOf(compensationMoneyList.get(i).get("PART_CODE")).equalsIgnoreCase(str[m])){ out.print("selected");}%>><%=name[m] %></option>
						<%}
          		}      
          		%>
				</select> <span style="color: red">*</span></td>
						</td>
						<td><div align="left">
								<input type="button" class="normal_btn" value="删除"
									name="button42" onClick="delItemOther(this);" />
							</div></td>
					</tr>
				<%	}
				}
				%>
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
				<%
				if(accessoryDtlList!=null && accessoryDtlList.size()>0){
					//acccode=(String)accessoryDtlList.get(0).get("MAIN_PART_CODE");
					for(int i=0;i<accessoryDtlList.size();i++){%>
						<tr>
						<td>
						<input type="text" name="workHourCodeMap" class="middle_txt" value="<%=accessoryDtlList.get(i).get("WORKHOUR_CODE") %>"  id="workHourCodeMap" readonly/><span style="color:red">*</span>
						</td>
						<td>
						<input name="workhour_name" id="workhour_name" value="<%=accessoryDtlList.get(i).get("WORKHOUR_NAME") %>" type="text" class="middle_txt" readonly>
						</td>
						<td>
						<input name="accessoriesPrice" id="accessoriesPrice" value="<%=accessoryDtlList.get(i).get("PRICE") %>" type="text" class="middle_txt" readonly>
						</td>
						<td>
				<select name="accessoriesOutMainPart"
					id="accessoriesOutMainPart" value="" style="width: 150px;">
						<option value="-1">--请选择--</option>
						<% System.out.print(outMainPart);
          		if(!"".equalsIgnoreCase(outMainPart)){
          			String str[] = outMainPart.split(",");
          			String name[] = outMainPartName.split(",");
          			for(int m=0;m<str.length;m++){%>
						<option value="<%=str[m] %>"
							<%
							if(String.valueOf(accessoryDtlList.get(i).get("MAIN_PART_CODE")).equalsIgnoreCase(str[m])){ out.print("selected");}%>><%=name[m] %></option>
						<%}
          		}      
          		%>
				</select> <span style="color: red">*</span></td>
						</td>
						<td><div align="left">
								<input type="button" class="normal_btn" value="删除"
									name="button42" onClick="delItemOther(this);" />
							</div></td>
					</tr>
				<%	}
				}
				%>
				</tbody>
			</table>

		<table class="table_edit" id="table_edit_remark" style="display: none">

			<th colspan="8"><img src="../../img/subNav.gif" alt=""
				class="nav" /> 申请内容</th>
			<tr>
				<td class="table_edit_2Col_label_5Letter">故障描述：</td>
				<%if(tawep.getForlStatus()==11561002){ %>
				<td class="tbwhite" colspan="3"><textarea name='TROUBLE_DESC'
						datatype="0,is_null" disabled="disabled" id='TROUBLE_DESC'
						rows='2' cols='28'><%=CommonUtils.checkNull(tawep.getTroubleDescriptions()) %></textarea>
				</td>
				<%}else{ %>
				<td class="tbwhite" colspan="3"><textarea name='TROUBLE_DESC'
						datatype="1,is_textarea,100" id='TROUBLE_DESC' rows='2' cols='28'><%=CommonUtils.checkNull(tawep.getTroubleDescriptions()) %></textarea><span
					style="color: red">*</span></td>
				<%} %>
				<td class="table_edit_2Col_label_5Letter">故障原因：</td>
				<%if(tawep.getForlStatus()==11561002){ %>
				<td colspan="3" class="tbwhite"><textarea name='TROUBLE_REASON1'
						id='TROUBLE_REASON1' datatype="1,is_textarea,100"
						disabled="disabled" rows='2' cols='28'><%=CommonUtils.checkNull(tawep.getTroubleReason()) %></textarea>
				</td>
				<%}else{ %>
				<td colspan="3" class="tbwhite"><textarea name='TROUBLE_REASON1'
						id='TROUBLE_REASON1' datatype="1,is_textarea,100" rows='2'
						cols='28'><%=CommonUtils.checkNull(tawep.getTroubleReason()) %></textarea><span
					style="color: red">*</span></td>
				<%} %>
			</tr>
			<tr>
				<td class="table_edit_2Col_label_5Letter">维修措施：</td>
				<%if(tawep.getForlStatus()==11561002){ %>
				<td colspan="3" class="tbwhite"><textarea name='REPAIR_METHOD'
						datatype="1,is_textarea,100" disabled="disabled"
						id='REPAIR_METHOD' rows='2' cols='28'><%=CommonUtils.checkNull(tawep.getRepairMethod()) %></textarea>
				</td>
				<%}else{ %>
				<td colspan="3" class="tbwhite"><textarea name='REPAIR_METHOD'
						datatype="1,is_textarea,100" id='REPAIR_METHOD' rows='2' cols='28'><%=CommonUtils.checkNull(tawep.getRepairMethod()) %></textarea><span
					style="color: red">*</span></td>
				<%} %>
				<td class="table_edit_2Col_label_5Letter">申请备注：</td>
				<td colspan="3" class="tbwhite"><textarea name='APP_REMARK'
						id='APP_REMARK' datatype="1,is_textarea,100" rows='2' cols='28'><%=CommonUtils.checkNull(tawep.getRemarks()) %></textarea>
				</td>
			</tr>
		</table>
		<table class="table_edit" id="REMARK_ID" align="center"
			style="display: none">
			<tr>
				<th colspan="10"><img src="../../img/subNav.gif" alt=""
					class="nav" /> 预授权申请内容</th>
			</tr>
			<tr>
				<td><textarea rows="5" name="remark_ysq" id="remark_ysq"
						onblur="checkLen();" cols="100">${remark2}</textarea><span style ="color: red;margin: 50px 10px;">预授权申请内容限制输入300汉字</span></td>
			</tr>
		</table>
		<%
			if(tawep.getRepairTypeCode().equalsIgnoreCase("11441004")){
				
			%>
				 <!-- 添加附件 开始  -->
        <table id="add_file"  width="75%" class="table_info" border="0" id="file">
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
			</table> 
  		<!-- 添加附件 结束 -->
		</br>
			<%
			}
		%>
		<table class="table_edit">
			<tr>
				<td style="display: none"><input type="button"
					id="myApprove_btn" style="display: none"
					onclick="myApproveHandler();" class="long_btn" value="预授权申请2" /></td>
				<td colspan="7" align=center><input class="normal_btn"
					type="button" value="维修历史"
					onclick="openWindowDialog('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN=<%=tawep.getVin()%>');" />
					<input class="normal_btn" type="button" value="授权历史" onclick="openWindowDialog('<%=contextPath%>/repairOrder/RoMaintainMain/authDetail.do?VIN=<%=tawep.getVin()%>&RO_NO=<%=tawep.getRoNo()%>');"/>
					<input class="normal_btn" type="button" value="保养历史"
					onclick="openWindowDialog('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/freeMaintainHistory.do?VIN=<%=tawep.getVin()%>');" />

					<input type="button" onclick="confirmUpdate();" id="saveBtn"
					class="normal_btn" style="" value="保存" /> <input type="button"
					onclick="history.back();" class="normal_btn" style="" value="返回" /></td>
				<td><input type="button" style="display: none" id="approve"
					onclick="approve1()" class="long_btn" value="预授权申请" /> <input
					class="long_btn" type="button" id="three_package_set_btn"
					value="${frLevel}" onclick="threePackageSet();" /></td>
			</tr>
		</table>
	</form>
	<script type="text/javascript">
	//	$('ENGINE_NO').disabled=true;
		$('VIN').disabled=true;
		oneVIN();
		function myApproveHandler(){
			var id = $('my_ro_id').value ;
			var url = '<%=contextPath%>/repairOrder/RoMaintainMain/roModifyForward3.do?type=4&ID='+id ;
			OpenHtmlWindow(url,800,500) ;
		}
		//配件三包判定按钮方法
		function threePackageSet(){
			var roNo = $('RO_NO').value ;
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
					window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/threePackageSet.do?VIN='+vin+'&mile='+inMileage+'&codes='+codes+'&codes_type='+codes_type+'&labcodes='+labcodes+'&labcodes_type='+labcodes_type+'&roNo='+roNo);
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
		document.getElementById("REPAIR_TYPE_1").disabled = true;
		document.getElementById("YIELDLY_TYPE_01").disabled = true;
		//document.getElementById("VIN_INTYPE_01").disabled = true;
	 //  	$('YIELDLY').disabled=true;
			//授权
	function checkLen(){
	var len = $('remark_ysq').value.length;				
				if(len>300){
					MyAlert("预授权内容不能超过300字！");
					return false;
				}
	}
	function approve1(){
		var dealerLevel = document.getElementById("dealerLevel").value;
		if(dealerLevel=='<%=Constant.DEALER_LEVEL_02%>'){
			MyAlert("二级经销商不能进行预授权申请!");
		return false
		}
				var len = $('remark_ysq').value.length;				
				if(len<1){
					MyAlert("预授权内容必填");
					return false;
				}
				if(len>800){
					MyAlert("预授权内容不能超过800字！");
					return false;
				}
				//zhumingwei 2011-01-26 begin
				if('<%=CommonUtils.checkNull(Utility.handleDate(tawep.getGuaranteeDate()))%>' == '' && '<%=Constant.REPAIR_TYPE_04%>'=='<%=tawep.getRepairTypeCode()%>'){//<%=Constant.REPAIR_TYPE_04%>
					MyAlert("售前车不能做保养类型!");
					return false;
				}
				 var type =$('REPAIR_TYPE').value;//获取维修类型
				if(type!=<%=Constant.REPAIR_TYPE_04%>){
				//预授权申请时，也要判断工时为自费,配件为索赔情况
				makeNomalFormCall('<%=request.getContextPath()%>/repairOrder/RoMaintainMain/checkLabour.json',checks,'fm');
				}
				 if((type!=<%=Constant.REPAIR_TYPE_04%>)&&(type!=<%=Constant.REPAIR_TYPE_05%>)){
	 		//判断 是否为一车一天
	 		makeNomalFormCall('<%=request.getContextPath()%>/repairOrder/RoMaintainMain/checkOneDay.json',checks2,'fm');
	 		}
				if(flags && !oneFlag){
				MyConfirm("确认申请？",approveSure,[]);
				}else{
					return;
				}
			}
			function approveSure(){
			$('saveBtn').disabled=true;
					document.getElementById("approve").disabled=true;
					document.getElementById("REMARK_ID").disabled=true;
			var url = '<%=contextPath%>/repairOrder/RoMaintainMain/accredit.json';
				makeNomalFormCall(url,approveBack,'fm','approve');
			}
			//授权回调
			function approveBack(json) {
				var last = json.success;
				if (last=="true") {
				$('approve').disabled=true;
					MyAlert("预授权申请成功!");
					hasFore = true;
					//zhumingwei 2012-02-29 begin
					window.location.href='<%=contextPath%>/repairOrder/RoMaintainMain/roForward.do';
					//window.history.back();
					//zhumingwei 2012-02-29  end
				}else if(last=="noPart"){
					MyAlert("含有没有库存的配件,不能开工单!");
					hasFore = false;
					return;
				}else if(last=="cover"){
					MyAlert("不能重复申请!");
					hasFore = false;
					return;
				}else if(last=="noPoint"){
					MyAlert("每个工时必须含有至少一个主因件配件!");
					hasFore = false;
					return;
				}else {
					MyAlert("预授权申请失败，请联系管理员!");
					hasFore = false;
				}
			}
		//判断免费保养是否需要授权
			function verFree() {
			  	var purchasedDate = document.getElementById("GUARANTEE_DATE").value;
				var vin = document.getElementById("VIN").value;
				var inMileage = document.getElementById("IN_MILEAGE").value;
				if (vin==null||vin==''||vin=='null') {
					MyAlert("车辆VIN不能为空！");
				}else if (inMileage==null||inMileage==''||inMileage=='null') {
					MyAlert("进场里程数不能为空！");
				}else{
				var db = 1;//用于区分是修改还是新增页面getFree()
				var do_no = '<%=CommonUtils.checkNull(tawep.getRoNo())%>';
				var url = '<%=contextPath%>/repairOrder/RoMaintainMain/getFree.json';
				makeCall(url,verFreeBack,{VIN:vin,IN_MILEAGE:inMileage,PURCHASED_DATE:purchasedDate,DB:db,Do_No:do_no});
				}
			}
			function verFreeBack(json) {
				var last = json.approve;
				var mastMileage = json.in_milage;
				$('mastMileage').value=mastMileage;
				if(Number($('IN_MILEAGE').value)<Number(mastMileage)){
					MyAlert("进厂行驶里程不能小于该单据行驶里程");
					//document.getElementById("approve").style.display='none';
					//document.getElementById("REMARK_ID").style.display='none';
					return false;
					//MyAlert(0);
				}
				//document.getElementById("freeTimes").value=json.needTime;
				//if($('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_02%>||$('REPAIR_TYPE').value==<%=Constant.REPAIR_TYPE_04%>){
				//	if (last) {
				//		if (!hasFore) {
				//			MyAlert(last);
				//		document.getElementById("approve").style.display='';
				//		document.getElementById("REMARK_ID").style.display='';
					//	}
				//	}else {
				//		document.getElementById("approve").style.display='none';
				//		document.getElementById("REMARK_ID").style.display='none';
				//	}
				//}
			}
		//根据索赔类型变换样式
   function getTypeChangeStyle(obj) {
   		//assignSelectActivity("ACTIVITYCOMBO",'<%=tawep.getCamCode()%>'); 
   		//document.getElementById('ACTIVITYCOMBO').selectedIndex=0;
   		if (first) {
		 	document.getElementById('ACTIVITYCOMBO').selectedIndex=0;
		 	//zeroAllFee();
  			clearAllNode(document.getElementById('itemTable'),'item');
			clearAllNode(document.getElementById('partTable'),'part');
			clearAllNode(document.getElementById('otherTable'),'other');
		 }
   		//zeroAllFee();
  		//clearAllNode(document.getElementById('itemTable'));
		//clearAllNode(document.getElementById('partTable'));
		//clearAllNode(document.getElementById('otherTable'));
		document.getElementById("approve").style.display='none';
		document.getElementById("REMARK_ID").style.display='none';
		document.getElementById("itemBtn").disabled = false;
		document.getElementById("partBtn").disabled = false;
		document.getElementById("otherBtn").disabled = false;
    	if(obj=='<%=Constant.REPAIR_TYPE_01%>') {//一般索赔
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='none';
    		//document.getElementById('feeTableId').style.display='none';
    		//document.getElementById('feeId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById('activityTableId0').style.display='none';
    		document.getElementById('activityTableId1').style.display='none';
    		document.getElementById("activity").style.display='none';
    		document.getElementById('outId').style.display='none';
    		document.getElementById('accessories').style.display='';
    	}else if(obj=='<%=Constant.REPAIR_TYPE_04%>') {//免费保养
    		document.getElementById('itemTableId').style.display='none';
    		document.getElementById('partTableId').style.display='none';
    		document.getElementById('otherTableId').style.display='none';
    		//document.getElementById('feeTableId').style.display='';
    		//document.getElementById('feeId').style.display='';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById('activityTableId0').style.display='none';
    		document.getElementById('activityTableId1').style.display='none';
    		document.getElementById("activity").style.display='none';
    		document.getElementById('outId').style.display='none';
    		document.getElementById("freeTime").style.display = '';
    		document.getElementById('accessories').style.display='none';
    	//	$('table_edit_remark').style.display='none';
    		verFree();
    	}else if(obj=='<%=Constant.REPAIR_TYPE_09%>'){
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById('activityTableId0').style.display='none';
    		document.getElementById('activityTableId1').style.display='none';
    		document.getElementById("activity").style.display='none';
    		document.getElementById('outId').style.display='block';
    		if (!hasFore) {
    		document.getElementById("approve").style.display='';
    		document.getElementById("REMARK_ID").style.display='';
    		document.getElementById('accessories').style.display='';
    		}
    	}else if(obj=='<%=Constant.REPAIR_TYPE_02%>') {//PDI索赔
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		//document.getElementById('feeTableId').style.display='none';
    		//document.getElementById('feeId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById('activityTableId0').style.display='none';
    		document.getElementById('activityTableId1').style.display='none';
    		document.getElementById("activity").style.display='none';
    		document.getElementById('outId').style.display='block';
    		if (!hasFore) {
    		document.getElementById("approve").style.display='';
    		document.getElementById("REMARK_ID").style.display='';
    		document.getElementById('accessories').style.display='';
    		}
    	}else if(obj=='<%=Constant.REPAIR_TYPE_03%>') {//保外索赔
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='none';
    		//document.getElementById('feeTableId').style.display='none';
    		//document.getElementById('feeId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById('activityTableId0').style.display='none';
    		document.getElementById('activityTableId1').style.display='none';
    		document.getElementById("activity").style.display='none';
    		document.getElementById('outId').style.display='none';
    		document.getElementById('accessories').style.display='';
    	}else if(obj=='<%=Constant.REPAIR_TYPE_05%>') {
	    	document.getElementById("itemBtn").disabled = false;
			document.getElementById("partBtn").disabled = false;
			document.getElementById("otherBtn").disabled = true;
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='';
    		document.getElementById('outId').style.display='none';
    		//document.getElementById('feeTableId').style.display='none';
    		//document.getElementById('feeId').style.display='none';
    		document.getElementById('activityTableId').style.display='';
    		document.getElementById('activityTableId0').style.display='';
    		document.getElementById('activityTableId1').style.display='none';
    		document.getElementById("activity").style.display='';
    		//$('table_edit_remark').style.display='';
    	}else if(obj=='<%=Constant.REPAIR_TYPE_06%>') {//特殊服务
    		document.getElementById('accessories').style.display='';
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='none';
    		//document.getElementById('feeTableId').style.display='none';
    		//document.getElementById('feeId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById('activityTableId0').style.display='none';
    		document.getElementById('activityTableId1').style.display='none';
    		document.getElementById("activity").style.display='none';
    		document.getElementById('outId').style.display='none';
    	}else if(obj=='<%=Constant.REPAIR_TYPE_07%>') {//急件
    		document.getElementById('itemTableId').style.display='';
    		document.getElementById('partTableId').style.display='';
    		document.getElementById('otherTableId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById('activityTableId0').style.display='none';
    		document.getElementById('activityTableId1').style.display='none';
    		document.getElementById("activity").style.display='none';
    		document.getElementById('outId').style.display='none';
    		document.getElementById("CL_DOWN").value='';
    		document.getElementById("GS_DOWN").value='';
    		//document.getElementById("REMARKS_ID").style.display = '';
    		document.getElementById('ACTIVITYCOMBO').value='';
    		$("VIN").attr(onpaste,'return false');
    		
    	}else if(obj=='<%=Constant.REPAIR_TYPE_08%>') {//PDI
    		document.getElementById('itemTableId').style.display='none';
    		document.getElementById('partTableId').style.display='none';
    		document.getElementById('otherTableId').style.display='';
    		$('otherBtn').disabled=true;
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById('activityTableId0').style.display='none';
    		document.getElementById('activityTableId1').style.display='none';
    		document.getElementById("activity").style.display='none';
    		document.getElementById('outId').style.display='none';
    		document.getElementById("freeTime").style.display = 'none';
    		document.getElementById("CL_DOWN").value='';
    		document.getElementById("GS_DOWN").value='';
    		document.getElementById('RemarkPDI').innerHTML="<span style='color: red;'>备注（PDI）必须填写</span>";
    		//document.getElementById("REMARKS_ID").style.display = 'none';
    		document.getElementById('ACTIVITYCOMBO').value='';
    		$("VIN").attr(onpaste,'return false');
    		verFree();
    	}else  {
    		document.getElementById('itemTableId').style.display='none';
    		document.getElementById('partTableId').style.display='none';
    		document.getElementById('otherTableId').style.display='none';
    		//document.getElementById('feeTableId').style.display='none';
    		//document.getElementById('feeId').style.display='none';
    		document.getElementById('activityTableId').style.display='none';
    		document.getElementById('activityTableId0').style.display='none';
    		document.getElementById('activityTableId1').style.display='none';
    		document.getElementById("activity").style.display='none';
    		document.getElementById('outId').style.display='none';
    	//	$('table_edit_remark').style.display='none';
    	}
    }
	//选择工时联动故障代码
	function chooseItem() {
        var itemId = document.getElementById('timeId').value;
        var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeItem.json';
    	makeCall(url,changeTroubleCode,{ITEM_ID:itemId});
    }
    //回调函数
    function changeTroubleCode(json) {
    	var last=json.changeCode;
     	last = "<select id='TROUBLE_CODE' name='TROUBLE_CODE' class='min_sel'>"+last+"</select>";
    	document.getElementById("myTrouble").innerHTML = last;
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
    //选择工时联动故障代码(产生服务活动后，点击故障代码下拉框)
	function chooseItem3(itemId) {
        //var itemId = document.getElementById('timeId').value;
        var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeItem.json';
    	makeCall(url,changeTroubleCode3,{ITEM_ID:itemId});
    }
    //回调函数
    function changeTroubleCode3(json) {
    	var last=json.changeCode;
     	last = "<select id='TROUBLE_CODE' name='TROUBLE_CODE' class='min_sel'>"+last+"</select>";
     	// MyAlert(myobj.options.length);
    	myobj.parentNode.innerHTML = last;
    	// MyAlert(myobj.parentNode.innerHTML);
    	// MyAlert(myobj.options.length);
    	assignSelectByObj(myobj,objValue);
    }
    
   
    //加载工时，得到第一项即主工时的值
    function chooseItem1() {
        var itemId = document.getElementById('timeId').value;
        var url0 = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeItem.json';
    	makeCall(url0,changeTroubleCode1,{ITEM_ID:itemId});
    }
  
    
	
	//chooseItem1();
	//var options =  document.getElementById("REPAIR_TYPE").options;
	var options1 =  document.getElementById("REPAIR_TYPE").value;
   	//var index = options.selectedIndex;
   	//var myvalue = options[index].value;
   	//MyAlert(options1);
	getTypeChangeStyle(options1);
	first=true;
	//这里给下拉框动态赋一个ONCLICK事件
	var obj = document.getElementById("REPAIR_TYPE");
	//MyAlert(obj);
    if(obj){
   		obj.attachEvent('onchange',getTypeChangeStyleParam);//
   	}
   	function getTypeChangeStyleParam() {
   		getTypeChangeStyle(obj.value);
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
   	function blurBack(obj){
   	    if(obj == "OUT_MILEAGE")
   	    {
   	         var OUT_MILEAGE =   document.getElementById(obj).value;
            document.getElementById("ITEM_AMOUNTQT007").value =  OUT_MILEAGE*3;
   	    } else
   	    {
   	       if (obj=="IN_MILEAGE") {
    	//if (document.getElementById("REPAIR_TYPE").value=='<%=Constant.REPAIR_TYPE_04%>') {
    		verFree();
	    	//}
	    }
   		if (obj=="VIN") {
   			oneVIN();
   			if (document.getElementById("REPAIR_TYPE").value=='<%=Constant.REPAIR_TYPE_04%>') {
    			verFree();
    		}
   		}else {
   		if (obj=="RO_NO"){
    		verDupR();
    	}
   		if (obj!='FREE_M_AMOUNT') {
   	   		countQuantity(document.getElementById(obj));
   	   	}
   	   	
   	   	setFee0(document.getElementById('FREE_M_AMOUNT'));
   	   	}
   	       
   	    }
   		
   	}
   	 function delUploadFile(obj){
   	 document.getElementById("attIds").value += ","+obj.parentElement.parentElement.cells.item(0).childNodes[1].value;
   	 //MyAlert(obj.parentElement.parentElement.cells.item(0).childNodes[1].value);
  		var idx = obj.parentElement.parentElement.rowIndex;
		var tbl = document.getElementById('fileUploadTab');
		tbl.deleteRow(idx);
	}
	//document.getElementById("YIELDLY").disabled = true;
	function ISDIS(){
			if(<%=tawep.getApprovalYn()%>==1){
				if(<%=tawep.getForlStatus()%>==<%=Constant.RO_FORE_02%>){
					$('approve').style.display='';
					$('REMARK_ID').style.display='';
					$('approve').disabled=true;
					$('REMARK_ID').disabled=true;
					$('remark_ysq').disabled=true;
				}
				if(<%=tawep.getIsClaimFore()%>==0){
					$('approve').style.display='';
					$('REMARK_ID').style.display='';
				}
			}
			else{
				$('approve').style.display='none';
				$('remark_ysq').style.display='none';
			}	
	}
	ISDIS();

	function openWindowDialog(targetUrl){
		  var height = 500;
		  var width = 800;
		  var iTop = (window.screen.availHeight-30-height)/2;  //获得窗口的垂直位置      
		  var iLeft = (window.screen.availWidth-10-width)/2;   //获得窗口的水平位置
		  var params = "width="+width+",height="+height+",top="+iTop+",left="+iLeft+",scrollbars=yes,resizable=yes,status=no";
		  window.open(targetUrl,null,params);
	  	}
	reloadRname();
	function reloadRname(){
		var partname=document.getElementsByName("PART_NAME");
		var partcode=document.getElementsByName("PART_CODE");
		var mainPartCode = document.getElementsByName("mainPartCode");
		var outMainPart = document.getElementsByName("OUT_MAIN_PART");
		if(mainPartCode.length > 0){
			
			//outMainPart[0].options.length = 0;
			
			for(var k=0; k<mainPartCode.length; k++){
				var temp=0;
				for(var j=0; j<partcode.length; j++){
					if(partcode[j].value==mainPartCode[k].value){
						mainPartCode[k].options.length = 0;
						var  varItem = new Option(partname[temp].value,partcode[j].value);
					 	mainPartCode[k].options.add(varItem);
					}
				temp++;
				}
			}
		}
	}
	 function add(){
         var addTable = document.getElementById("accessories");
         $('approve').disabled=true;
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
    // function zhuyinjianShow(){
   // 	 $('zhuyinjian').style.display="";
    // }
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
     //============zyw 2014-8-4
     function addCompensation(){
     	$('approve').disabled=true;
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
       		addTable.rows[length].cells[0].innerHTML =  '<td><input type="text" name="supplier_code" class="middle_txt"   id="supplier_code" readonly/><span style="color:red">*</span><a href="#" name="selectSupplierCode"  onclick="selectSupplierCode(this);">选择</a></td>';
   			addTable.rows[length].cells[1].innerHTML =  '<td><input name="apply_price" id="apply_price"  type="text" class="middle_txt" maxlength="9"  onblur="checkPrice(this);"> <span style="color:red">*</span></td>';
   			addTable.rows[length].cells[2].innerHTML =  '<td><input name="pass_price" id="pass_price" value="" type="text" class="middle_txt" maxlength="9"><span style="color:red">*</span></td>';
   			addTable.rows[length].cells[3].innerHTML =  '<td><select name="part_code_temp" id="part_code_temp" value="" style="width: 150px;"><option value="-1">--请选择--</option></select> <span style="color: red">*</span></td>';
   			addTable.rows[length].cells[4].innerHTML =  '<td><div align="left"><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="delItemOther(this);"/></div></td>';
   			setPartCodesCompensation();
   	       }
     
     function selectSupplierCode(obj,id,partcode){
 		myobj="";
 		myobj = obj.parentNode;
 		parent.OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectSupplierCodeForward.do?partcode='+partcode+'&id='+id,800,500);
 	}
     function setMainSupplierCode(v1,v2){
    	 var flag =true;
			var tr = this.getRowObj(myobj);
			var supplierCode =document.getElementsByName('supplier_code');
			
				for(var i=0;i<supplierCode.length;i++){
					if(supplierCode[i].value==v1){
						MyAlert("提示：不能添加相同的供应商!");
						flag = false;
						break;;
						}
					}
			if(flag){
				tr.childNodes[0].childNodes[0].value=v1;
			}
     }
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
 		var accessoriesOutMainPart = document.getElementsByName("part_code_temp");
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
 			//accessoriesOutMainPart.options.length = 0;
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
   //============zyw 2014-8-4
</script>
</BODY>
</html>

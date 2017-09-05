 <!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.bean.TtAsActivityBean"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c" %>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动管理</title>
<% String contextPath = request.getContextPath(); %>
<% TtAsActivityBean ActivityPO=(TtAsActivityBean)request.getAttribute("ActivityPO");%>
<%
TtAsActivityBean beforeVehicle = (TtAsActivityBean) request.getAttribute("beforeVehicle");//服务活动信息-服务车辆活动范围
TtAsActivityBean afterVehicle = (TtAsActivityBean) request.getAttribute("afterVehicle");//服务活动信息-服务车辆活动范围
%>			
<script type="text/javascript">
var myobj;
var cloMainTime=1; //关闭主工时选择页面
var cloTime=1; //关闭附属工时选择页面
var cloMainPart=1; //关闭主要配件选择页面
var cloPart=1; //关闭附加配件选择页面
/* function hideReplce(){
	document.getElementById("showorhide").style.display="";
	document.getElementById("showorhide1").style.display="";
	document.getElementById("show").style.display="none";
	clearTable();
}
//初始清除table
function clearTable(){
	var obj1=document.getElementById("itemTableId");
	var obj2=document.getElementById("partTableId");
    
    var rowNum=obj1.rows.length;
    if(rowNum>0)
    {
        for(var i=2;i<rowNum;i++)
        {
        	obj1.deleteRow(i);
          rowNum=rowNum-1;
          i=i-1;
        }    
    }
    var rowNum1=obj2.rows.length;
    if(rowNum1>0)
    {
        for(var j=2;j<rowNum1;j++)
        {
          obj2.deleteRow(j);
          rowNum1=rowNum1-1;
          j=j-1;
        }    
    }
}
function showReplce(){
	document.getElementById("showorhide").style.display="none";
	document.getElementById("showorhide1").style.display="none";
	document.getElementById("show").style.display="";
	clearTable();
} */
    //日历控件初始化
	function doInit()
		{
		   loadcalendar();
		   checkType();
		}
	//索赔选择
	function chickFixfee(show){
		if(document.fm.isFixfee.checked==true){
			document.fm.isClaim.checked=true;
			show.style.display ="inline";
		    document.getElementById("showItem").style.display ="none";//活动项目隐藏
		}else {
		 show.style.display ="none";
		 document.getElementById("activityFee").value="";
		 document.getElementById("showItem").style.display ="inline";//活动项目显示
		//document.fm.partFee.value =0.0;
        //document.fm.worktimeFee.value =0.0;
		}
	}
	function chickClaim(show){
		if(document.fm.isClaim.checked==false&&document.fm.isFixfee.checked==true){
			document.fm.isClaim.checked=true;
		}
		if(document.fm.isClaim.checked==true){ 
		     document.getElementById("showItem").style.cssText='white-space:nowrap;';
		     document.getElementById("showItem").style.display ="inline";//活动项目显示
		}else{
		     document.getElementById("showItem").style.display ="none";//活动项目隐藏
		}
		if(document.fm.isClaim.checked==true&&document.fm.isFixfee.checked==true){
		     document.getElementById("showItem").style.display ="none";//活动项目隐藏
		}
	}
	function fix(show){
          if(document.fm.isFixfee.checked==true){
           show.style.display ="inline";
           }else{
           show.style.display ="none";
           //document.fm.partFee.value =0.0;
           //document.fm.worktimeFee.value =0.0;
           }
    }
	//修改 开始
	function serviceActivityManageUpdate(){
			fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/serviceActivityManageUpdate.do?activityId="+<%=ActivityPO.getActivityId()%>+"&type=yes";
			fm.submit();
	}
	
		function subChecked(action){
			if(!submitForm('fm')) {
				return false;
			}	
			var actype=document.getElementById("activityType").value;
			if(actype=="10561005"){
				var yz1=document.getElementById("TROUBLE_DESC").value;
				var yz2=document.getElementById("TROUBLE_REASON").value;
				var yz3=document.getElementById("REPAIR_METHOD").value;
				var wr=document.getElementsByName("WR_LABOURCODE");
				var rn=document.getElementsByName("RESPONS_NATURE");
				var mc=document.getElementsByName("mainPartCode");
				var pc=document.getElementsByName("PART_CODE");
				var pt=document.getElementsByName("PAY_TYPE_ITEM");
				var pp=document.getElementsByName("PAY_TYPE_PART");
				var la=document.getElementsByName("Labour0");
				var len=wr.length;
				var len1=pc.length;
				if(len1==0){
					MyAlert("提示：请添加配件");
					return;
				}
				if(len==0){
					MyAlert("提示：请添加工时！");
					return;
				}
				if(yz1.length==0){
					MyAlert("提示：请填写故障描述");
					return;
				}
				if(yz2.length==0){
					MyAlert("提示：请填写故障原因");
					return;
				}
				if(yz3.length==0){
					MyAlert("提示：请填写维修措施");
					return;
				}
				for(var i=0;i<mc.length;i++){
					if(mc[i].value==""){
						MyAlert("提示：关联主因件不能为空，请选择至少一个主因件");
						return;
					}
				}
				var temp=0;
				for(var j=0;j<rn.length;j++){
					if(rn[j].value=="94001001"){
						temp++;
					}
				}
				if(temp!=len){
					MyAlert("提示：一个工时必须对应一个主因件");
					return;
				}
				for(var k=0;k<pt.length;k++){
					if(pt[k].value=="11801001"){//自费的
						var wrl=wr[k].value;//工时代码
						for(var z=0;z<la.length;z++){//维修项目Code
							if(la[z].value==wrl){//付费方式为自费的工时代码下的维修项目
								if(pp[z].value!='11801001'){
									MyAlert("提示：替换工时的付费方式为自费的，对应的工时项目只能为自费！");
									return;
								}
							}
						}
					}
				}
				for(var e=0;e<rn.length;e++){//循环关联主因件
					if(rn[e].value=="94001001"){//取为主因件的
						var temp2=0;
						for(var t=0;t<la.length;t++){//循环维修项目code
							if(la[t].value==la[e].value && rn[t].value=="94001001"){
								temp2++;
							}
						}
						if(temp2>=2){
							MyAlert("提示：一个主因件只能对应一个工时的维修项目！");
							return;
						}
					}
				}
				MyConfirm("是否确认提交？",judede);
			}
			if(actype!="10561005"){
			 //disableBtn($("commitBtn"));//点击按钮后，按钮变成灰色不可用;
				MyConfirm("是否确认提交？",judede);
			}
		     
	}
	function returnBackjudede(json)
	{
		if( json.fuwu == "0")
		{
			MyAlert("服务活动日期范围错误！");
		} else if(json.paduan !=null && json.paduan == "0")
		{
			MyAlert("服务活动必须要有经销商 (车行等 或者 活动vin必须要有一个)");
		}else 
		{
			serviceActivityManageUpdate();
		}
	}
	
	//修改 开始
	//添加服务活动主数据信息
	function judede()
	{
		makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/ServiceActivityManagejudeMy.json',returnBackjudede,'fm','queryBtn');
	}	
	function subCheckedbao(action){
		if(!submitForm('fm')) {
			return false;
		}		
		var actype=document.getElementById("activityType").value;
		if(actype=="10561005"){
			var yz1=document.getElementById("TROUBLE_DESC").value;
			var yz2=document.getElementById("TROUBLE_REASON").value;
			var yz3=document.getElementById("REPAIR_METHOD").value;
			var wr=document.getElementsByName("WR_LABOURCODE");
			var rn=document.getElementsByName("RESPONS_NATURE");
			var mc=document.getElementsByName("mainPartCode");
			var pc=document.getElementsByName("PART_CODE");
			var pt=document.getElementsByName("PAY_TYPE_ITEM");
			var pp=document.getElementsByName("PAY_TYPE_PART");
			var la=document.getElementsByName("Labour0");
			var len=wr.length;
			var len1=pc.length;
			if(len1==0){
				MyAlert("提示：请添加配件");
				return;
			}
			if(len==0){
				MyAlert("提示：请添加工时！");
				return;
			}
			if(yz1.length==0){
				MyAlert("提示：请填写故障描述");
				return;
			}
			if(yz2.length==0){
				MyAlert("提示：请填写故障原因");
				return;
			}
			if(yz3.length==0){
				MyAlert("提示：请填写维修措施");
				return;
			}
			for(var i=0;i<mc.length;i++){
				if(mc[i].value==""){
					MyAlert("提示：关联主因件不能为空，请选择至少一个主因件");
					return;
				}
			}
			var temp=0;
			for(var j=0;j<rn.length;j++){
				if(rn[j].value=="94001001"){
					temp++;
				}
			}
			if(temp!=len){
				MyAlert("提示：一个工时必须对应一个主因件");
				return;
			}
			for(var k=0;k<pt.length;k++){
				if(pt[k].value=="11801001"){//自费的
					var wrl=wr[k].value;//工时代码
					for(var z=0;z<la.length;z++){//维修项目Code
						if(la[z].value==wrl){//付费方式为自费的工时代码下的维修项目
							if(pp[z].value!='11801001'){
								MyAlert("提示：替换工时的付费方式为自费的，对应的工时项目只能为自费！");
								return;
							}
						}
					}
				}
			}
			for(var e=0;e<rn.length;e++){//循环关联主因件
				if(rn[e].value=="94001001"){//取为主因件的
					var temp2=0;
					for(var t=0;t<la.length;t++){//循环维修项目code
						if(la[t].value==la[e].value && rn[t].value=="94001001"){
							temp2++;
						}
					}
					if(temp2>=2){
						MyAlert("提示：一个主因件只能对应一个工时的维修项目！");
						return;
					}
				}
			}
			MyConfirm("是否确认保存？",jude);
		}
		if(actype!="10561005"){
			 MyConfirm("是否确认保存？",jude);
		}
	    
	}    
	//修改 开始
	//添加服务活动主数据信息
	function jude()
	{
		makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/ServiceActivityManagejudeMy.json',returnBackjude,'fm','queryBtn');
	}
	function returnBackjude(json)
	{
		if(json.fuwu != null && json.fuwu == "1" ) 
		{
			serviceActivityManageUpdatebao();
		}else if( json.fuwu == "0")
		{
			MyAlert("服务活动日期范围错误！");
		}
	}
	
	function serviceActivityManageUpdatebao(){
			makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/serviceActivityManageUpdate.json?activityId='+<%=ActivityPO.getActivityId()%>,returnBack,'fm','queryBtn');
	}
	function returnBack(json)
	{
			var pro_codev = document.getElementsByName('pro_codev');
			if(pro_codev.length > 0)
			{
				for(var i = 0;i<pro_codev.length;i++)
				{
					pro_codev[i].name = '';
				}
			}
			
			
			MyAlert("保存成功！");
	}
	
	//修改 结束
	//服务活动-车型列表开始
	function openModel(){
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageModel/serviceActivityManageModelQuery.do?activityId='+<%=ActivityPO.getActivityId()%>,800,500);
	}
	/**
	 * 服务活动车型：选择物料组树界面
	 * inputId   : 回填页面物料组code域id
	 * inputName ：回填页面物料组name域id
	 * isMulti   : true值多选，否则单选
	 * groupLevel：输出的物料组等级
	 */
	function serviceShowMaterialGroup(inputCode ,inputName ,isMulti ,groupLevel,activityId)
	{
		if(!inputCode){ inputCode = null;}
		if(!inputName){ inputName = null;}
		if(!groupLevel){ groupLevel = null;}
		if(!activityId){ activityId = null;}
		OpenHtmlWindow("<%=contextPath%>/claim/serviceActivity/ServiceActivityManageModel/serviceGroupListQuery.do?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&GROUPLEVEL="+groupLevel+"&ACTIVITYID="+activityId,770,410);
	}

	//服务活动-车型列表结束
	
	//服务活动-车龄定义列表开始
	function openVehicleAge(){
	    var beforeVehicle ="";
	    if(document.fm.beforeVehicle.checked==true){
	    	beforeVehicle =document.fm.beforeVehicle.value;
	    }
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageVehicleAge/serviceActivityManageVehicleAgeQuery.do?activityId='+<%=ActivityPO.getActivityId()%>+'&beforeVehicle='+beforeVehicle,500,300);
	}
	//服务活动-车龄定义列表结束
	//服务活动-车辆性质列表开始
	function openCharactor(){
	    var beforeVehicle =document.fm.beforeVehicle;
	    if(beforeVehicle.checked==true){
				MyAlert("选择售前车不能维护车辆性质！");
				return false;
			}	
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageCharactor/serviceActivityManageCharactorQuery.do?activityId='+<%=ActivityPO.getActivityId()%>,800,500);
	}
	//服务活动-车辆性质列表结束
	//服务活动-活动项目列表开始
	function openItem(){
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageItem/serviceActivityManageItemQuery.do?activityId='+<%=ActivityPO.getActivityId()%>,800,500);
	}
	
	//服务活动-活动项目列表结束
	//服务活动-生产基地列表开始
	function openProduceBase(){
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageProduceBase/serviceActivityManageProduceBaseQuery.do?activityId='+<%=ActivityPO.getActivityId()%>,800,500);
	}
	//服务互动 里程限制
	function openMilage(){
	var 	activeId = $('activityId').value 
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/getActivityMilage.do?activeId='+activeId,400,200);
	}
	
	//服务活动-生产基地列表结束
	//服务活动-VIN清单导入开始
	function openVIN(){
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityVinImport/serviceActivityVinImportInit.do?activityId='+<%=ActivityPO.getActivityId()%>+"&flag=modify",800,500);
	}
	//服务活动-VIN清单导入结束
	
	function downloadTemplate(){
		fm.action = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/downloadVin.do';
	    fm.submit();
	 }
    //返回主页面
    function goBack(){
        var activityCode='<%=ActivityPO.getActivityCode()%>';
        var activityId=document.getElementById("activityId").value;
        fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/serviceActivityManageQuery.do?activityCode="+activityCode+"&activityId="+activityId+"&flag=onFlag";
	    fm.submit();
    }
  //活动类别下拉框onchange事件
	function doMySel(value){
		if(value=='<%=Constant.SERVICEACTIVITY_KIND_02%>'){
			$('isFixfee').checked = true ;
			chickFixfee(show) ;
			$('activityFee').value = 0 ;
			$('isFixfee').disabled = 'disabled' ;
		}else{
			$('isFixfee').checked = false ;
			chickFixfee(show) ;
			$('isFixfee').disabled = false ;
		}
	}
	// 生产基地下拉框显示控制
	function showYieldly(value){
		if(value==1)
			$('yieldly1').style.display = 'block' ;
		else
			$('yieldly1').style.display = 'none' ;
	}
	// 活动类别的联动
	function checkType(){
		if('<%=Constant.SERVICEACTIVITY_KIND_02%>'=='<%=ActivityPO.getActivityKind()%>'){
			$('show').style.display='block';
			$('activityFee').value = '' ;
			$('activityFee').value = 0 ;
			$('activityFee').disabled = 'disabled' ;
			$('isFixfee').checked = true ;
			$('isFixfee').disabled = true ;
		}
	}
	checkType();
	// 结算指向的联动
	function checkDirect(){
		if('0'!='${beanDirect}'){
			document.getElementById('default').checked = false;
			document.getElementById('directed').checked = true;
			document.getElementById('yieldly1').style.display = '';
		}
		var type=document.getElementById('type').value;
		if(type=='10561005'){
			document.getElementById('show1').style.display = 'none';
			document.getElementById('show').style.display = '';
			document.getElementById('show2').style.display = 'none';
			document.getElementById('t_news').style.display = 'none';
		}
		if(type!='10561005'){
			document.getElementById('show1').style.display = '';
			document.getElementById('show').style.display = 'none';
			document.getElementById('show2').style.display = '';
			document.getElementById('t_news').style.display = '';
		}
	}
	
	function showFree(free){
	  $('activityFee').value = free;
	}	
	function viewNews(value){
		OpenHtmlWindow("<%=contextPath%>/claim/basicData/HomePageNews/viewNews.do?comman=2&newsId="+value,800,500);
	}
	function open_conent(val)
	{
		var activityId = document.getElementById('activityId').value;
		if(val == '3537006')
		{
			OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/get_laber.do?largess_type='+val+'&activityId='+activityId,800,500);
		}else if(val == '3537007')
		{
			OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/get_part.do?largess_type='+val+'&activityId='+activityId,800,500);
		}else if(val == '3537005')
		{
			OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/get_part.do?largess_type='+val+'&activityId='+activityId,800,500);
		}else if(val == '3537004')
		{
				OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/get_largess.do?largess_type='+val+'&activityId='+activityId,800,500);
		}else if(val == '3537001')
		{
			OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/get_check.do?largess_type='+val+'&activityId='+activityId,800,500);
		}
		
	}
	function addRowRaplce(table){
		var addTable = document.getElementById(table);
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
		
		if(table=='itemTableId'){
			addTable.rows[length].cells[0].innerHTML =  '<td><input type="text" class="phone_txt"  id="WR_LABOURCODE'+length+'" name="WR_LABOURCODE" readonly  value="" size="10"/><span style="color:red">*</span><a href="#"  onclick="selectMainTime(this);">选择</a></td>';
			setMustStyle([document.getElementById("WR_LABOURCODE"+length)]);
			addTable.rows[length].cells[1].innerHTML =  '<td><span class="tbwhite"><input type="text" name="WR_LABOURNAME"  id="WR_LABOURNAME'+length+'" datatype="0,is_textarea,100" class="long_txt" value="" size="10" readonly/></span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" id="LABOUR_AMOUNT'+length+'" name="LABOUR_AMOUNT"  class="middle_txt"  datatype="0,is_double,10" decimal="2" value="" size="8" maxlength="9" /></td>';
			setMustStyle([document.getElementById("LABOUR_AMOUNT"+length)]);
			addTable.rows[length].cells[3].innerHTML = genSelBoxExpPay("PAY_TYPE_ITEM",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'<%=Constant.PAY_TYPE_01%>');
			addTable.rows[length].cells[4].innerHTML ='<td><input readonly id="MALFUNCTIONS'+length+'" datatype="0,is_textarea,100"  name="MALFUNCTIONS" class="middle_txt" value="" /><input type="hidden" id="MALFUNCTION" name="MALFUNCTION" class="middle_txt" value="" /><a href="#" onclick="selectMalCode(this);">选择</a></td>';
			addTable.rows[length].cells[5].innerHTML =  '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'itemTable\',this,\'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this);"/></td>';
		}
		if(table=='partTable'){

			insertRow.insertCell(6);
			insertRow.insertCell(7);
			insertRow.insertCell(8);
			insertRow.insertCell(9);
			insertRow.insertCell(10);
			insertRow.insertCell(11);
			addTable.rows[length].cells[0].innerHTML =  '<td><input type="text" class="phone_txt" name="PART_CODE"   value="" size="10" id="PART_CODE" readonly/><span style="color:red">*</span><span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this)">选择</a></span></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><span class="tbwhite"><input type="text" class="phone_txt" name="PART_NAME" readonly   id="PART_NAME'+length+'"  datatype="0,is_textarea,100" name="PART_SN3"  size="10"/></span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" class="little_txt" onkeyup="countQuantity(this)"  onblur="countQuantity(this)" size="10" name="QUANTITY" id="QUANTITY'+length+'" datatype="0,is_double,10" decimal="2"  maxlength="20"/></td>';
			setMustStyle([document.getElementById("QUANTITY"+length)]);
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" class="phone_txt" name="PART_CODE_OLD"   value="" size="10" id="PART_CODE_OLD" readonly/><span style="color:red">*</span><span class="tbwhite"></span></td>';
			addTable.rows[length].cells[4].innerHTML =  '<td><span class="tbwhite"><input type="text" class="phone_txt" name="PART_NAME_OLD"  readonly  id="PART_NAME_OLD'+length+'" datatype="0,is_textarea,100"  size="10"/></span></td>';
			addTable.rows[length].cells[5].innerHTML ='<td>'+ genSelBoxExpPay("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'<%=Constant.PAY_TYPE_01%>') +'</td>';
			addTable.rows[length].cells[6].innerHTML =  '<td><select id="Labour'+length+'" value="" name="Labour0" ></select></td>';
			addTable.rows[length].cells[7].innerHTML =  '<td><select id="mainPartCode'+length+'" value="" name="mainPartCode" ><option value="-1">---请选择---</option></select></td>';
			addTable.rows[length].cells[8].innerHTML = '<td>'+ genSelBoxExpPay10("RESPONS_NATURE",<%=Constant.RESPONS_NATURE_STATUS%>,"",false,"min_sel","","false",'') +'</td>';
			addTable.rows[length].cells[9].innerHTML = '<td>'+ genSelBoxExpPay("HAS_PART",<%=Constant.IF_TYPE%>,"",false,"min_sel","","false",'<%=Constant.IF_TYPE_NO%>') +'</td>';
			addTable.rows[length].cells[10].innerHTML ='<td><select style="width: 80px" onchange="changeQ(this,'+length+');" name="PART_USE_TYPE" id="PART_USE_TYPE'+length+'"><option value="95431001">维修</option> <option value="95431002" selected>更换</option></select></td>';
			addTable.rows[length].cells[11].innerHTML = '<td><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			addValues();				
		}
		return addTable.rows[length];
}
function setMainTime(v1,v2,v3,v4,v5){
    	var table = myobj.parentNode;
		var length= table.childNodes.length;
		var flag=0;
		//var total=0;
		//判断是否添加了重复的主工时
		for (var i = 0;i<length;i++) {
				if(v2==table.childNodes[i].childNodes[0].childNodes[0].value){
					MyAlert("该主工时已经存在，不可添加！");
					cloMainTime=0;
					flag=1;
					break;
				}
		}
		if (flag==0){
			/* total = accMul(v4,v5);
			total=total.toFixed(2); */
			cloMainTime=1;
			myobj.cells.item(0).innerHTML='<td><input name="MAIN_ITEM"  type="hidden" value="on"/><input type="text" class="phone_txt"   name="WR_LABOURCODE" readonly value="'+v2+'" size="10"/><span style="color:red">*</span><a href="#"  onclick="selectMainTime(this);">选择</a></td>'
			myobj.cells.item(1).innerHTML='<td><span class="tbwhite"><input type="text" class="long_txt" name="WR_LABOURNAME"  value="'+v3+'" size="10" readonly/></td>';
			myobj.cells.item(5).innerHTML = '<td><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this);"/></td>';
		}
}
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
	
	//刷新维修配件上的维修项目值
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
//乘法运算
function accMul(arg1,arg2) { 
   var m=0,s1=arg1.toString(),s2=arg2.toString();
   try{m+=s1.split(".")[1].length}catch(e){}
   try{m+=s2.split(".")[1].length}catch(e){}
   return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m) 
} 
function selectMalCode(obj){
	myobj="";
	myobj = obj.parentNode;
	parent.OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectMalCodeForward.do',800,500);
}

function setMainMal(v1,v2,v3){
		var tr = this.getRowObj(myobj);
		tr.childNodes[4].childNodes[0].value=v2+"--"+v3;
		tr.childNodes[4].childNodes[1].value=v1;
}
	//选择主上件
	function selectMainPartCode(obj){
		myobj = getRowObj(obj);
		var activityType = document.getElementById("type").value;
		if (activityType!=null&&activityType!=''&&activityType!='null' &&activityType=='10561005' ) {
		OpenHtmlWindow('<%=contextPath%>/jsp/claim/dealerClaimMng/showMainPartCode4.jsp',800,500);
		}
	}
	//选择主工时
	function selectMainTime(obj) {
		myobj = getRowObj(obj);
		var treeCode = 3;
		var timeId;
		treeCode=3;
		timeId='';
		openTime1(obj,treeCode,timeId);
	}
	//修改主项目工时需要删除附加工时
	function openTime1(obj,treeCode,timeId){
		OpenHtmlWindow('<%=contextPath%>/jsp/claim/dealerClaimMng/showMainTime1.jsp',800,500);
	}
	//删除行 配件
	function delPartItem(obj,name){
	     var tr = this.getRowObj(obj);
	    if(tr.childNodes[5].childNodes.length==3) {
	    	MyConfirm("是否删除？",delItems,[tr]);
	    }else{
	   if(tr != null){
	    tr.parentNode.removeChild(tr);
	   }else{
	    throw new Error("the given object is not contained by the table");
	   }
	   }
	}
	//删除行
	function delItem(obj){
	    var tr = this.getRowObj(obj);
	    if(tr.childNodes[4].childNodes.length==3) {
	    	MyConfirm("是否删除？",delItems,[tr]);
	    }else{
		   if(tr != null){
				var res = confirm("你确认要删除吗？");
			    if(res){
			    	tr.parentNode.removeChild(tr);
					setCodesAndNames();
			    }
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
			if (tr.parentNode.childNodes[i].childNodes[4].childNodes.length==3) {
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
		
	}
	function addValues(){
		//刷新维修配件上的维修项目值
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
	setCodesAndNames();
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
	//主配件选择
	function setMainPartCode(partId,partCode,partName,oldPartCode,oldPartName,def_id) {
		var table = myobj.parentNode;
		var length= table.childNodes.length;
		var flag=0;
		//判断是否添加了重复的主工时
		for (var i = 0;i<length;i++) {
				if(partCode==table.childNodes[i].childNodes[0].childNodes[0].value){
					MyAlert("该主工时已经存在，不可添加！");
					cloMainTime=0;
					flag=1;
					break;
				}
		}
		if (flag==0){
		cloMainTime=1;
		myobj.cells.item(0).innerHTML='<input type="hidden" name="def_id" value="'+def_id+'"/><input type="text" class="phone_txt" name="PART_CODE"   value="'+partCode+'" size="10" id="PART_CODE" readonly="true"/><span style="color:red">*</span><span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this)">选择</a></span>';
		myobj.cells.item(1).innerHTML='<span class="tbwhite"><input type="text" class="phone_txt" name="PART_NAME" readonly value="'+partName+'" id="PART_NAME"  size="10"/></span>';
	  	myobj.cells.item(3).innerHTML='<input type="text" class="phone_txt" name="PART_CODE_OLD"   value="'+oldPartCode+'" size="10" id="PART_CODE_OLD" readonly="true"/><span style="color:red">*</span>';
		myobj.cells.item(4).innerHTML='<span class="tbwhite"><input type="text" class="phone_txt" name="PART_NAME_OLD" readonly value="'+oldPartName+'" id="PART_NAME_OLD"  size="10"/></span>';
		myobj.cells.item(11).innerHTML='<td><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/><input type="hidden"  class="little_txt"  value="'+partId+'"  name="REAL_PART_ID"/></td>';
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
				var malValue ="";
				malValue= partTable.childNodes[a].childNodes[8].childNodes[1].value;
				if(malValue==""){
					malValue=partTable.childNodes[a].childNodes[8].childNodes[0].value;
				}
				if(malValue==<%=Constant.RESPONS_NATURE_STATUS_01%>){//如果是主因件则加入集合
					partcodes[temp]=partTable.childNodes[a].childNodes[0].childNodes[1].value;
					partnames[temp]=partTable.childNodes[a].childNodes[1].childNodes[0].childNodes[0].value;
					temp++;
				}
			}
		//刷新维修配件上的维修项目值
		var mainPartCode = document.getElementsByName("mainPartCode");
		if(mainPartCode.length > 0){
			//清空所有值
			var name= '';
			for(var j=0; j<mainPartCode.length; j++){
				if(j==0){
					name = mainPartCode[j].options.value;
				}
				mainPartCode[j].options.length = 0;
			}
			//重新赋值
			for(var k=0; k<mainPartCode.length; k++){
				var malValue ="";
				malValue = partTable.childNodes[k].childNodes[8].childNodes[1].value;
				if(malValue==undefined){
					malValue=partTable.childNodes[k].childNodes[8].childNodes[0].value;
				}
				var z = 0;
				for(var a=0; a<partcodes.length; a++){
					if(malValue==<%=Constant.RESPONS_NATURE_STATUS_02%>){//如果是次因件，则让其选择对应的主因件
					 var  varItem = new Option(partnames[a],partcodes[a]);
					 	mainPartCode[k].options.add(varItem);
					} else{
						var varItem2 = new Option("---请选择---","-1");
						if( z== 0){
							mainPartCode[k].options.add(varItem2);
							z++;
						}
					}
				}
			}
		}
	}
	
</script>
</head>

<body onload="checkDirect();">
<table cellSpacing=0 cellPadding=0 width="100%" border=0>
	<tbody>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td height="30">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动管理
			</div>
			</td>
		</tr>
	</tbody>
</table>
<form method="post" name="fm" id="fm">
<input type="hidden" value="${typeleng}" name="typeleng">
<table class="table_edit">
    <tr>
		<th  colspan="6">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息
		</th>
	</tr>
	<tr>
	    <td width="20%"  align="right">服务活动主题：</td>
		    <td width="30%">
              <select name="subjectId">
              <c:forEach var="ttAsActivitySubjectPO" items="${ttAsActivitySubjectPO}" begin="0" end="0">
     					<option value="${ttAsActivitySubjectPO.subjectId}" >
       					<c:out value="${ttAsActivitySubjectPO.subjectName}"/>
       					</option>
       				  </c:forEach>
              </select>
		</td>
		<td width="20%" align="right">活动编号：</td>
		<td width="30%">
		<input type="hidden" name="activityId" id="activityId" value="<%=request.getAttribute("activityId")%>"  />
		<input type="hidden" id="type" name="activityType" value="<%=ActivityPO.getActivityType() %>"  />
		<%=ActivityPO.getActivityCode()%>
		</td>
	</tr>
	<tr>
		 <td  align="right">活动名称：</td>
		<td >
		   <input name="activityName" id="activityName" value="<%=ActivityPO.getActivityName()%>" type="text" class="middle_txt" datatype="0,is_null,100" >
		</td>
		
		<td align="right">实际活动日期</td>
		<td >
			<input class="short_txt" id="factstartdate" value="<%=ActivityPO.getFactstartdate()%>" name="factstartdate" datatype="1,is_date,10"
                   maxlength="10" group="factstartdate,factenddate"/>
            <input class="time_ico" value=" " onclick="showcalendar(event, 'factstartdate', false);" type="button"/>
                    至
             <input class="short_txt" id="factenddate" value="<%=ActivityPO.getFactenddate()%>" name="factenddate" datatype="1,is_date,10"
                    maxlength="10" group="factstartdate,factenddate"/>
             <input class="time_ico" value=" " onclick="showcalendar(event, 'factenddate', false);" type="button"/>
             
			<input type="hidden" class="middle_txt" name="car_max" id="car_max" datatype="0,is_digit,6" value="<%=ActivityPO.getMaxCar()%>"/>
		</td>
		
	   
	</tr>
	<tr>
		<td  align="right">活动日期：</td>
		<td >
			<div align="left">
			<input class="short_txt" id="t1" name="startDate" datatype="0,is_date,10" value="<%=ActivityPO.getStartdate()%>"
                   maxlength="10" group="t1,t2"/>
            <input class="time_ico" value=" " onclick="showcalendar(event, 't1', false);" type="button"/>
                    至
             <input class="short_txt" id="t2" name="endDate" datatype="0,is_date,10" value="<%=ActivityPO.getEnddate()%>"
                    maxlength="10" group="t1,t2"/>
             <input class="time_ico" value=" " onclick="showcalendar(event, 't2', false);" type="button"/>
            </div>
	    </td>
	     <td width="10%" align="right">服务活动类型二级：</td>
		<td width="20%">
		    <script type="text/javascript">
   				genSelBoxExp("typetowactivity",<%=Constant.TYPE_tow_activity%>,"${tow_type_activity}",true,"short_sel","","true",'');
  		    </script>
		</td>
	</tr>
	<tr>
     	<td   align="right" >服务活动车辆范围：</td>
     	<td>
     	  <input type="checkbox" value="<%=beforeVehicle.getCodeId()%>" name="beforeVehicle" id="beforeVehicle" 
     	   <%if("11321001".equals(request.getAttribute("vicle1"))) {%>checked="checked"<%}%>  />
     	  <script type='text/javascript'>
				       var beforeVehicle=getItemValue('<%=beforeVehicle.getCodeId()%>');
				       document.write(beforeVehicle) ;
		 </script>
     	 <input type="checkbox" value="<%=afterVehicle.getCodeId()%>" name="afterVehicle" id="afterVehicle"
     	   <%if("11321002".equals(request.getAttribute("vicle2"))) {%>checked="checked"<%}%> />
     	 <script type='text/javascript'>
				       var afterVehicle=getItemValue('<%=afterVehicle.getCodeId()%>');
				       document.write(afterVehicle) ;
		 </script>
		 <span style="color:red">*</span>
     	</td>
     	<td align="right">结算指向：</td>
     	<td>
     		<input type="radio" name="default" id="default" value="1"  checked="checked" onclick="showYieldly(0);"/>按生产基地结算
     		<input type="radio" name="default" id="directed" value="2" onclick="showYieldly(1);"/>定向结算
     		<span id="yieldly1" style="display:none">
     			<select name="yieldly" id="yieldly">
	              <c:forEach var="areaPO" items="${areaPO}" >
 				  <option value="${areaPO.areaId}" >
    				<c:out value="${areaPO.areaName}"/>
    			  </option>
    			 </c:forEach>
             </select>
     		</span>
     	</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">解决方案说明：</td>
		<td colspan="3" align="left" >
			<textarea name="solution" id="solution" class="SearchInput" rows="6" cols="90" datatype="0,is_null,200"><%=ActivityPO.getSolution()==null?"":ActivityPO.getSolution()%></textarea>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">索赔申请指导：</td>
		<td colspan="3" align="left">
			<textarea name="claimGuide" id="claimGuide" class="SearchInput" rows="6" cols="90" datatype="0,is_null,200"><%=ActivityPO.getClaimGuide()==null?"":ActivityPO.getClaimGuide()%></textarea>
		</td>
	</tr>
		<tr id="show1"><td colspan="6">
        	<input class="normal_btn"  name="add4" type="button" onclick="selectMainNew();" value ='新增' /></td>
       </tr>
       <table class="table_list_line" id="t_news" border="1">
	        <tr >
	          <th width="50" align="center" nowrap="nowrap" >NO </th>
	          <th width="220" align="center" nowrap="nowrap" >编码 </th>
	          <th width="400" align="center" nowrap="nowrap" >新闻名称</th>
	          <th width="80" align="center" nowrap="nowrap" >操作 </th>
	        </tr>
	        <c:if test="${!empty listNews}">
	        	<c:forEach var="newDetail" items="${listNews}" varStatus="vs">
	        		<tr >
			          <th width="50" align="center" nowrap="nowrap" >1 </th>
			          <th width="220" align="center" nowrap="nowrap" >${newDetail.NEWS_CODE } </th>
			          <th width="400" align="center" nowrap="nowrap" >${newDetail.NEWS_TITLE }</th>
			          <th width="80" align="center" nowrap="nowrap" ><a href="#" onclick='deleteRowConfirm(this,${newDetail.NEWS_ID})'>删除</a></th>
			        </tr>
	        	</c:forEach>
	        </c:if>
		</table>
		<div id="show" style="display: none">
		<table id="itemTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<th colspan="11" align="left">
					<img class="nav" src="../../../img/subNav.gif" />
					替换工时
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						作业代码
					</td>
					<td>
						作业名称
					</td>
					<!-- <td>
						工时定额
					</td>
					<td>
						工时单价
					</td> -->
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
							name="button422" onClick="javascript:addRowRaplce('itemTableId');" />
					</td>
				</tr>

				<tbody id="itemTable">
					<c:forEach var="l" items="${lp }" varStatus="len">
						<tr>
							<td><input type="text" class="phone_txt"  id="WR_LABOURCODE'${len.index }'" name="WR_LABOURCODE" readonly  value="${l.wrLabourcode }" size="10"/><span style="color:red">*</span><a href="#"  onclick="selectMainTime(this);">选择</a></td>
							<td><span class="tbwhite"><input type="text" name="WR_LABOURNAME" class="long_txt" value="${l.wrLabourname }" size="10" readonly/></span></td>
							<td><input type="text" id="LABOUR_AMOUNT'${len.index }'" name="LABOUR_AMOUNT" class="middle_txt"  datatype="0,is_double,10" decimal="2"  value="${l.labourAmount }" size="8" maxlength="9" /></td>		
							<td>
							<script type="text/javascript">
							 genSelBoxExp("PAY_TYPE_ITEM",<%=Constant.PAY_TYPE%>,"${l.payType }",false,"min_sel","","false",'<%=Constant.PAY_TYPE_01%>');
							</script>
							</td>
							<td><input readonly="readonly" id="MALFUNCTIONS" name="MALFUNCTIONS" class="middle_txt" value="${l.malFunctionValue }" /><input type="hidden" id="MALFUNCTION" name="MALFUNCTION" class="middle_txt" value="${l.malFunctionId }" /><a href="#" onclick="selectMalCode(this);">选择</a></td>
							<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'itemTable\',this,\'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this);"/></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<table id="partTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list">

				<th colspan="13" align="left">
					<img class="nav" src="../../../img/subNav.gif" />
					替换配件
				</th>
				<tr align="center" class="table_list_row1">
					<!-- <td>
						是否三包
					</td> -->
					<td>
						新件代码
					</td>
					<td>
						新件名称
					</td>
					<td>
						新件数量
					</td>
					<td>
						旧件代码
					</td>
					<td>
						旧件名称
					</td>
					<td>
						付费方式
					</td>
					<td>
						维修项目
					</td>
					<td>
						关联主因件
					</td>
					<td>
						责任性质
					</td>
					<td>
						配件是否有库存
					</td>
					<td>
					配件维修类型
					</td>
					
					<td>
						<input id="partBtn" type="button" class="normal_btn" value="新增"
							name="button422" onClick="javascript:genAppTimeCombo1(addRowRaplce('partTable'));" />
					</td>
				</tr>
				<tbody id="partTable">
					<c:forEach var="r" items="${rp }">
						<tr>
							<td nowrap="true"><input type="hidden" name="def_id" value="${r.realPartId }"/><input type="text" class="phone_txt" name="PART_CODE"   value="${r.partCode }" size="10" id="PART_CODE" readonly/><span style="color:red">*</span><span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this)">选择</a></span></td>
							<td><span class="tbwhite"><input type="text" class="phone_txt" name="PART_NAME" readonly  value="${r.partName }" id="PART_NAME"   size="10"/></span></td>
							<td><input type="text" class="little_txt" onkeyup="countQuantity(this)"  onblur="countQuantity(this)" size="10" name="QUANTITY" id="QUANTITY'+length+'" datatype="0,is_double,10" decimal="2"  value="${r.quantity }" maxlength="20"/></td>
							<td nowrap="true"><input type="text" class="phone_txt" name="PART_CODE_OLD"   value="${r.downPartCode }" size="10" id="PART_CODE_OLD" readonly/><span style="color:red">*</span></td>
							<td><span class="tbwhite"><input type="text" class="phone_txt" name="PART_NAME_OLD" readonly  value="${r.downPartName }" id="PART_NAME_OLD" size="10"/></span></td>
							<td>
								<script type="text/javascript">
								genSelBoxExp("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"${r.payType}",false,"min_sel","","false",'<%=Constant.PAY_TYPE_01%>');
								</script>
							</td>
							<td>
								<select id="Labour" name="Labour0" >
									<c:if test="${r.labourCode!=null}">
										<option value="${r.labourCode}">${r.labourCode}</option>
									</c:if>
								</select>
							</td>
							<td>
								<select id="mainPartCode" name="mainPartCode" >
									<c:if test="${r.mainPartCode==-1}">
										<option value="-1">---请选择---</option>
									</c:if>
									<c:if test="${r.mainPartCode!=-1}">
										<option value="${r.mainPartCode}">${r.mainPartCode}</option>
									</c:if>
								</select>
							</td>
							<td>
								<script type="text/javascript">
								genSelBoxExp("RESPONS_NATURE",<%=Constant.RESPONS_NATURE_STATUS%>,"${r.responsibilityType}",false,"min_sel","onchange='setPartCodes(this.value)'","false",'');
								</script>
							</td>
							<td>
								<script type="text/javascript">
								genSelBoxExp("HAS_PART",<%=Constant.IF_TYPE%>,"${r.hasPart}",false,"min_sel","","false",'<%=Constant.IF_TYPE_NO%>');
								</script>
							</td>
							<td>
								<select style="width: 80px" onchange="changeQ(this,'+length+');" name="PART_USE_TYPE" id="PART_USE_TYPE'+length+'">
									<c:if test="${r.partUseType==95431001}">
										<option value="95431001">维修</option> 
										<option value="95431002">更换</option> 
									</c:if>
									<c:if test="${r.partUseType==95431002}">
										<option value="95431002">更换</option> 
										<option value="95431001">维修</option> 
									</c:if>
								</select>
							</td>
							<td>
								<input name="button42" class="normal_btn" onclick="javascript:delPartItem(this,'part');" type="button" value="删除"/>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		<table class="table_edit"  id="REMARKS_ID" > 
        	 <th colspan="8"  ><img src="../../../img/subNav.gif" alt="" class="nav" />
					申请内容
				</th>
          	<tr>
          	
					<td class="table_edit_2Col_label_5Letter">
						故障描述：
					</td>
					<td class="tbwhite" colspan="3">
						<textarea name='TROUBLE_DESC' datatype="0,is_textarea,100" maxlength="100"	id='TROUBLE_DESC' rows='2' cols='28'><%=ActivityPO.getTroubleDesc()%></textarea>
					</td>
					<td class="table_edit_2Col_label_5Letter">
						故障原因：
					</td>
					<td colspan="3" class="tbwhite">
						<textarea name='TROUBLE_REASON' id='TROUBLE_REASON' maxlength="100"	datatype="0,is_textarea,100" rows='2' cols='28'><%=ActivityPO.getTroubleReason()%></textarea>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_5Letter">
						维修措施：
					</td>
					<td colspan="3" class="tbwhite">
						<textarea name='REPAIR_METHOD' datatype="0,is_textarea,100"id='REPAIR_METHOD' maxlength="100" rows='2' cols='28'><%=ActivityPO.getRepairMethod()%></textarea>
					</td>
					<td class="table_edit_2Col_label_5Letter">
						申请备注：
					</td>
					<td colspan="3" class="tbwhite">
						<textarea name='APP_REMARK' id='APP_REMARK'	datatype="1,is_textarea,100" maxlength="100" rows='2' cols='28'><%=ActivityPO.getAppRemark()==null?"":ActivityPO.getAppRemark()%></textarea>
					</td>
				</tr>
          </table>
	</div>
  <TABLE id=otherTableId class=table_list border=0 cellSpacing=1 cellPadding=0 align=center>
    <TBODY id="show2">
      <TR>
        <TH colSpan=14 align=left><IMG class=nav src="../../../img/subNav.gif"> 活动项目
        <!-- 艾春 9.13 修改 -->
          <c:if test="${typeleng ==0 }">
         <script type="text/javascript">
		   	genSelBoxExp("pro_code",<%=Constant.SERVICEACTIVITY_CAR_cms%>,"",true,"short_sel","","false",'<%=Constant.SERVICEACTIVITY_CAR_cms_06%>,<%=Constant.SERVICEACTIVITY_CAR_cms_07%>');
		  </script>
        </c:if>
        <c:if test="${typeleng ==1 }">
         <script type="text/javascript">
		   	genSelBoxExp("pro_code",<%=Constant.SERVICEACTIVITY_CAR_cms%>,"",true,"short_sel","","false",'<%=Constant.SERVICEACTIVITY_CAR_cms_03%>');
		  </script>
        </c:if>
          <INPUT id=otherBtn class=normal_btn onclick="javascript:addRow2('otherTable');" name=button422 value=新增 type=button></TH>
      </TR>
    <TBODY id=otherTable>
    <c:forEach var="project" items="${project}">
    
    <c:if test="${project.proCode==3537006 || project.proCode==3537007}">
      <TR class=table_list_row1>
        <TD align=right>项目名称：</TD>
        <TD>
        <script type='text/javascript'>
	       var proCode=getItemValue('${project.proCode}');
	       document.write(proCode) ;
	     </script>
	     <input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='open_conent("${project.proCode}")' value='...' />
	     <input type="hidden"  name= "proCodeMy" value="${project.proCode}">
		 <input type="hidden"  name= "idMy" value="${project.id}">
		</TD>
        <TD>折扣：</TD>
        <TD><INPUT name= "amountMy" id="LABOUR_AMOUNT0"  datatype="0,is_double" decimal="2" class=middle_txt value="${project.amount}" size=8 maxLength=5><INPUT type="hidden" id="" name=pro_codeo class=middle_txt value="${project.proCode}" >%</TD>
        <TD>处理方式：</TD>
        <TD>
        <script type="text/javascript">
		 genSelBoxExp("wuyong",'<%=Constant.SERVICEACTIVITY_CAR_type%>',"${project.dealWay}",false,"short_sel","","true",'');
		</script>
		</TD>
        <TD></TD>
        <TD></TD>
         <TD>付费方式：</TD>
        <TD>
        <script type="text/javascript">
		 genSelBoxExp("fufei",'<%=Constant.PAY_TYPE%>',"${project.paid}",false,"short_sel","","true",'');
		</script>
		</TD>
        <TD><INPUT class="normal_btn" id="queryBtn" onclick="javascript:deleteRow(this,${project.id})"; name="delete" value="删除" type="button"></TD>
      </TR>
    </c:if>
    
     <c:if test="${project.proCode==3537002}">
      <TR class=table_list_row1>
        <TD align=right>项目名称：</TD>
        <TD>
        <script type='text/javascript'>
	       var proCode=getItemValue('${project.proCode}');
	       document.write(proCode) ;
	     </script>
	     <input type="hidden"  name= "proCodeMy" value="${project.proCode}">
		 <input type="hidden"  name= "idMy" value="${project.id}">
		</TD>
		<TD>保养次数：</TD>
        <TD><INPUT name= "zhekname" id="zhekname"  datatype="0,is_digit,2" class=middle_txt value="${project.maintainTime}" size=8 maxLength=5></TD>
        <TD>折扣：</TD>
        <TD><INPUT name= "amountMy" id="LABOUR_AMOUNT0"  datatype="0,is_double" decimal="2" class=middle_txt value="${project.amount}" size=8 maxLength=5><INPUT type="hidden" id="" name=pro_codeo class=middle_txt value="${project.proCode}" >%</TD>
        <TD>处理方式：</TD>
        <TD>
        <script type="text/javascript">
		 genSelBoxExp("wuyong",'<%=Constant.SERVICEACTIVITY_CAR_type%>',"${project.dealWay}",false,"short_sel","","true",'');
		</script>
		</TD>
         <TD>付费方式：</TD>
        <TD>
        <script type="text/javascript">
		 genSelBoxExp("fufei",'<%=Constant.PAY_TYPE%>',"${project.paid}",false,"short_sel","","true",'');
		</script>
		</TD>
        <TD><INPUT class="normal_btn" id="queryBtn" onclick="javascript:deleteRow(this,${project.id})"; name="delete" value="删除" type="button"></TD>
      </TR>
    </c:if>
    
     <c:if test="${project.proCode ==3537005}">
      <TR class=table_list_row1>
        <TD align=right>项目名称：</TD>
        <TD>
        <script type='text/javascript'>
	       var proCode=getItemValue('${project.proCode}');
	       document.write(proCode) ;
	     </script>
	     <input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='open_conent("${project.proCode}")' value='...' />
	     <input type="hidden"  name= "proCodeMy" value="${project.proCode}">
		 <input type="hidden"  name= "idMy" value="${project.id}">
        </TD>
        <TD>费用：</TD>
        <TD>&nbsp<INPUT name= "amountMy" type="hidden" id="LABOUR_AMOUNT0" class="middle_txt" value="0" size="8" maxLength="9"><INPUT type="hidden"  id="" name="pro_codeo" class="middle_txt" value="${project.proCode}"  ></TD>
        <TD>处理方式：</TD>
        <TD>
        <script type="text/javascript">
		 genSelBoxExp("wuyong",'<%=Constant.SERVICEACTIVITY_CAR_type%>',"${project.dealWay}",false,"short_sel","","true",'');
		</script>
        </TD>
        <TD></TD>
        <TD></TD>
         <TD>付费方式：</TD>
        <TD>
        <script type="text/javascript">
		 genSelBoxExp("fufei",'<%=Constant.PAY_TYPE%>',"${project.paid}",false,"short_sel","","true",'');
		</script>
		</TD>
        <TD><INPUT class="normal_btn" id="queryBtn" onclick="javascript:deleteRow(this,${project.id});" name="delete" value="删除" type="button"></TD>
      </TR>
      </c:if>
      
      <c:if test="${project.proCode ==3537004}">
      <TR class=table_list_row1>
        <TD align=right>项目名称：</TD>
        <TD>
        <script type='text/javascript'>
	       var proCode=getItemValue('${project.proCode}');
	       document.write(proCode) ;
	     </script>
	     <input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='open_conent("${project.proCode}")' value='...' />
	     <input type="hidden"  name= "proCodeMy" value="${project.proCode}">
		 <input type="hidden"  name= "idMy" value="${project.id}">
        </TD>
        <TD>费用：</TD>
        <TD>&nbsp<INPUT name= "amountMy"  id="amounttt" class="middle_txt" value="${project.amount}" size="8" maxLength="9"><INPUT type="hidden"  id="" name="pro_codeo" class="middle_txt" value="${project.proCode}"  ></TD>
        <TD>处理方式：</TD>
        <TD>
        <script type="text/javascript">
		 genSelBoxExp("wuyong",'<%=Constant.SERVICEACTIVITY_CAR_type%>',"${project.dealWay}",false,"short_sel","","true",'');
		</script>
        </TD>
        <TD></TD>
        <TD></TD>
         <TD>付费方式：</TD>
        <TD>
        <script type="text/javascript">
		 genSelBoxExp("fufei",'<%=Constant.PAY_TYPE%>',"${project.paid}",false,"short_sel","","true",'');
		</script>
		</TD>
        <TD><INPUT class="normal_btn" id="queryBtn" onclick="javascript:deleteRow(this,${project.id});" name="delete" value="删除" type="button"></TD>
      </TR>
      </c:if>
      
       <c:if test="${project.proCode ==3537001}">
      <TR class=table_list_row1>
        <TD align=right>项目名称：</TD>
        <TD>
        <script type='text/javascript'>
	       var proCode=getItemValue('${project.proCode}');
	       document.write(proCode) ;
	     </script>
	     <input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='open_conent("${project.proCode}")' value='...' />
	     <input type="hidden"  name= "proCodeMy" value="${project.proCode}">
		 <input type="hidden"  name= "idMy" value="${project.id}">
        </TD>
        <TD>费用：</TD>
        <TD>&nbsp<INPUT name= "amountMy"  id="amountff" class="middle_txt" value="${project.amount}" size="8" maxLength="9"><INPUT type="hidden"  id="" name="pro_codeo" class="middle_txt" value="${project.proCode}"  ></TD>
        <TD>处理方式：</TD>
        <TD>
        <script type="text/javascript">
		 genSelBoxExp("wuyong",'<%=Constant.SERVICEACTIVITY_CAR_type%>',"${project.dealWay}",false,"short_sel","","true",'');
		</script>
        </TD>
        <TD></TD>
        <TD></TD>
         <TD>付费方式：</TD>
        <TD>
        <script type="text/javascript">
		 genSelBoxExp("fufei",'<%=Constant.PAY_TYPE%>',"${project.paid}",false,"short_sel","","true",'');
		</script>
		</TD>
        <TD><INPUT class="normal_btn" id="queryBtn" onclick="javascript:deleteRow(this,${project.id});" name="delete" value="删除" type="button"></TD>
      </TR>
      </c:if>
      
      </c:forEach>
    </TBODY>
  </TABLE>
</table>


<br/>
		

<table class="table_edit">
	<tr>
		<td colspan="4" align="center">
			<input type="button" id="" name="bt_add" class="long_btn" onclick="subCheckedbao('up');" value="保存"  id="commitBtn"/>
			<input type="button" id="" name="bt_add" class="long_btn" onclick="subChecked('up');" value="提交"  id="commitBtn"/>
		    <input type="button" id="" name="bt_back" class="normal_btn" onclick="re_back();" value="返回"/>
	    </td>
	</tr>
	<tr>
		<td colspan="12" align="center">
			<input type="button" name="type_add" id="" class="normal_btn"  onclick="openModel();" value="车型" /> 
			<!--  <input type="button" id="type_add" name="type_add" class="normal_btn"  onclick="serviceShowMaterialGroup('groupCode','','true','3','<%=ActivityPO.getActivityId()%>')"  value="车型" />-->
			<input type="button" id="milage_add" name="milage_add" class="normal_btn"   onclick="openMilage();" value="里程限制" /> 
			<input type="button" id="age_add" name="age_add" class="normal_btn"   onclick="openVehicleAge();" value="车龄" /> 
			<input type="button" id="veh_add" name="veh_add" class="normal_btn"  onclick="openCharactor();" value="车辆性质" />
			<input type="button" id="act_add" name="act_add" class="long_btn"  onclick="openProduceBase();" value="生产基地" />
			<input type="button" id="vin_insert" name="vin_insert" class="long_btn" onclick="openVIN();" value="活动VIN导入" />
			<input type="button" id="vin_load" name="vin_load" class="long_btn"   onclick="downloadTemplate();" value="下载VIN模版" />
		</td>
	</tr>
</table>
<script type="text/javascript">
//1.当服务活动为索赔时显示“活动项目”按钮;
//2.当服务活动为索赔是否为固定费用时，隐藏“活动项目”按钮;
//3.当默认状态下 “活动项目”按钮隐藏;

 function checkHiddenItem(){
	    if("0"=='<%=ActivityPO.getIsClaim()%>'){//当服务活动为索赔时显示“活动项目”按钮;
	          document.getElementById("showItem").style.display  ="inline";
	 	}
	   if("0"=='<%=ActivityPO.getIsClaim()%>'&&"1"=='<%=ActivityPO.getIsFixfee()%>'){
	 	      document.getElementById("showItem").style.display  ="none";
	 	}
	 	if("0"!='<%=ActivityPO.getIsClaim()%>'){//当服务活动不是索赔时隐藏“活动项目”按钮;
	          document.getElementById("showItem").style.display  ="none";
	 	}
 }
 //售前车
 function checkSelect(){
    if("11321001"==<%=request.getAttribute("vicle1")%>){
      document.fm.beforeVehicle.checked=true;
    }
   
 }

/*******
		服务活动增加首页新闻连接
	addUser:	xiongchuan 
	addTime:    2011-07-06	
	***/
	function selectMainNew(){
		var tabl=document.all['t_news'];
			if(tabl.children[0].children.length == 2)
			{
				MyAlert("新闻以添加");
			}else
			{
				OpenHtmlWindow('<%=contextPath%>/claim/other/Bonus/newsQuery.do',800,500);
			}
	}
	function deleteRowConfirm(obj,id){
		makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/ServiceActivityManageDeleteNew.json?newid='+id+'&activityId='+<%=ActivityPO.getActivityId()%>,returnBackcom,'fm','queryBtn');
		 var tabl=document.all['t_news'];
		 var index = obj.parentElement.parentElement.rowIndex;
		 
		 tabl.deleteRow(index); 
		 countSeq();
	}
	function returnBackcom()
	{
	}
	
	function deleteRowaa(obj,type)
	{
		makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/ServiceActivityManageDeletedelp.json?type='+type+'&activityId='+<%=ActivityPO.getActivityId()%>,returnBackcom,'fm','queryBtn');
		 var tabl=document.all['otherTableId'];
		 var index = obj.parentElement.parentElement.rowIndex;
		 tabl.deleteRow(index); 
		 countSeq();
	}
	function addRow2(tableId){
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
		insertRow.insertCell(6);
		insertRow.insertCell(7);
		insertRow.insertCell(8);
		insertRow.insertCell(9);	
		insertRow.insertCell(10);
		insertRow.insertCell(11);
		addTable.rows[length].cells[0].align='right';	
		addTable.rows[length].cells[0].align='right';		
		addTable.rows[length].cells[0].align='right';		
		addTable.rows[length].cells[0].align='right';		
		addTable.rows[length].cells[0].align='right';		
		addTable.rows[length].cells[0].align='right';		
		addTable.rows[length].cells[0].align='right';		
		addTable.rows[length].cells[0].align='right';			
		var selObj=document.getElementById('pro_code');
		var selectOptionText=selObj.options[selObj.selectedIndex].innerText;
		var selectOption=selObj.options[selObj.selectedIndex].value;
		var pro_codev = document.getElementsByName('pro_codev');
		var pro_codeo = document.getElementsByName('pro_codeo');
		if(pro_codeo.length > 0)
		{
			for(var i = 0;i< pro_codeo.length;i++)
			{
				if(selObj.value == pro_codeo[i].value )
				{
					MyAlert('添加的项目已经存在！');
					return;
				}
			}
		}
		
		if(pro_codev.length > 0)
		{
			for(var i = 0;i< pro_codev.length;i++)
			{
				if(selObj.value == pro_codev[i].value )
				{
					MyAlert('添加的项目已经存在！');
					return;
				}
			}
		}
		
		var str =  "<input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='open_conent("+selectOption+")' value='...' />"; 
		if(selObj.value==3537006||selObj.value==3537007){
			addTable.rows[length].cells[0].innerHTML = '项目名称：'; 
			addTable.rows[length].cells[1].innerHTML = selectOptionText+str; 
			addTable.rows[length].cells[2].innerHTML = '   折扣：'; 
			addTable.rows[length].cells[3].innerHTML = '<input type="text" name="amount" class="middle_txt" id="amount" datatype="0,is_double" decimal="2"  datatype="0,is_digit,2" maxlength="2"  value="" size="8"/>%';
			addTable.rows[length].cells[4].innerHTML = '&nbsp'; 
			addTable.rows[length].cells[5].innerHTML =  '<input type="hidden" name="zhek" class="middle_txt" id="zhek" datatype="0,is_double" decimal="2"  datatype="0,is_digit,2" maxlength="2"  value="" size="8"/>';   
			addTable.rows[length].cells[6].innerHTML = '处理方式：'; 
			addTable.rows[length].cells[7].innerHTML = genSelBoxExpPay("dealwith",<%=Constant.SERVICEACTIVITY_CAR_type%>,"",false,"min_sel","","true",'');
			addTable.rows[length].cells[8].innerHTML = '付费方式：'; 
			addTable.rows[length].cells[9].innerHTML = genSelBoxExpPay("paid",<%=Constant.PAY_TYPE%>,"<%=Constant.PAY_TYPE_02%>",false,"min_sel","","true",'');
			addTable.rows[length].cells[10].innerHTML = '<input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRow(this,'+selObj.value+');" />'; 
			addTable.rows[length].cells[11].innerHTML = '<input type="hidden" name= "pro_codev" class="middle_txt"    value="'+selObj.value+'" size="8" maxlength="9"/>';
		}else if(selObj.value==3537005){
			addTable.rows[length].cells[0].innerHTML = '项目名称：'; 
			addTable.rows[length].cells[1].innerHTML = selectOptionText+str; 
			addTable.rows[length].cells[2].innerHTML = '&nbsp'; 
			addTable.rows[length].cells[3].innerHTML = '<input type="hidden"  name="amount" id="amount" class="middle_txt" datatype="0,is_double" decimal="2"    value="0" size="8" maxlength="9"/>&nbsp'; 
			addTable.rows[length].cells[4].innerHTML = '&nbsp'; 
			addTable.rows[length].cells[5].innerHTML =  '<input type="hidden" name="zhek" class="middle_txt" id="zhek" datatype="0,is_double" decimal="2"  datatype="0,is_digit,2" maxlength="2"  value="" size="8"/>';   
			addTable.rows[length].cells[6].innerHTML = '处理方式：'; 
			addTable.rows[length].cells[7].innerHTML = genSelBoxExpPay("dealwith",<%=Constant.SERVICEACTIVITY_CAR_type%>,"",false,"min_sel","","true",'');
			addTable.rows[length].cells[8].innerHTML = '付费方式：'; 
			addTable.rows[length].cells[9].innerHTML = genSelBoxExpPay("paid",<%=Constant.PAY_TYPE%>,"<%=Constant.PAY_TYPE_02%>",false,"min_sel","","true",'');
			addTable.rows[length].cells[10].innerHTML = '<input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRow(this,'+selObj.value+');" />';  
			addTable.rows[length].cells[11].innerHTML = '<input type="hidden"  class="middle_txt" id="hiddenaa"   name="pro_codev" value="'+selObj.value+'" size="8" maxlength="9"/>';
		}else if(selObj.value==3537002)
		{
			addTable.rows[length].cells[0].innerHTML = '项目名称：'; 
			addTable.rows[length].cells[1].innerHTML = selectOptionText; 
			addTable.rows[length].cells[2].innerHTML = '保养次数'; 
			addTable.rows[length].cells[3].innerHTML = '<input type="text"  name="zhek" id="zhek" class="middle_txt"  datatype="0,is_digit,2"    value="" size="8" maxlength="9"/>&nbsp'; 
			addTable.rows[length].cells[4].innerHTML = '折扣'; 
			addTable.rows[length].cells[5].innerHTML =  '<input type="text" name="amount" class="middle_txt" id="amount" datatype="0,is_double" decimal="2"  datatype="0,is_digit,2" maxlength="2"  value="" size="8"/>%'; 
			addTable.rows[length].cells[6].innerHTML = '处理方式：'; 
			addTable.rows[length].cells[7].innerHTML = genSelBoxExpPay("dealwith",<%=Constant.SERVICEACTIVITY_CAR_type%>,"",false,"min_sel","","true",'');
			addTable.rows[length].cells[8].innerHTML = '付费方式：'; 
			addTable.rows[length].cells[9].innerHTML = genSelBoxExpPay("paid",<%=Constant.PAY_TYPE%>,"<%=Constant.PAY_TYPE_02%>",false,"min_sel","","true",'');
			addTable.rows[length].cells[10].innerHTML = '<input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRow(this,'+selObj.value+');" />';  
			addTable.rows[length].cells[11].innerHTML = '<input type="hidden"  class="middle_txt" id="hiddenaa"   name="pro_codev" value="'+selObj.value+'" size="8" maxlength="9"/>';
		}
		else if(selObj.value == '')
		{
			MyAlert('请选择活动项目');
		}else if(selObj.value==3537004){
			addTable.rows[length].cells[0].innerHTML = '项目名称：'; 
			addTable.rows[length].cells[1].innerHTML = selectOptionText+str; 
			addTable.rows[length].cells[2].innerHTML = '    费用：'; 
			addTable.rows[length].cells[3].innerHTML = '<input type="text" name="amount" id="amounttt" class="middle_txt" datatype="0,is_double" decimal="2"    value="" size="8" maxlength="9"/>'; 
			addTable.rows[length].cells[4].innerHTML = '&nbsp'; 
			addTable.rows[length].cells[5].innerHTML =  '<input type="hidden" name="zhek" class="middle_txt" id="zhek" datatype="0,is_double" decimal="2"  datatype="0,is_digit,2" maxlength="2"  value="" size="8"/>';    
			addTable.rows[length].cells[6].innerHTML = '处理方式：'; 
			addTable.rows[length].cells[7].innerHTML = genSelBoxExpPay("dealwith",<%=Constant.SERVICEACTIVITY_CAR_type%>,"",false,"min_sel","","true",'');
			addTable.rows[length].cells[8].innerHTML = '付费方式：'; 
			addTable.rows[length].cells[9].innerHTML = genSelBoxExpPay("paid",<%=Constant.PAY_TYPE%>,"<%=Constant.PAY_TYPE_02%>",false,"min_sel","","true",'');
			addTable.rows[length].cells[10].innerHTML = '<input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRow(this,'+selObj.value+');" />';  
			addTable.rows[length].cells[11].innerHTML = '<input type="hidden"  class="middle_txt" id="hiddenaa"   name="pro_codev" value='+selObj.value+' size="8" maxlength="9"/>';
		}else if(selObj.value==3537001)
		{
			addTable.rows[length].cells[0].innerHTML = '项目名称：'; 
			addTable.rows[length].cells[1].innerHTML = selectOptionText+str; 
			addTable.rows[length].cells[2].innerHTML = '    费用：'; 
			addTable.rows[length].cells[3].innerHTML = '<input type="text" name="amount" id="amountff" class="middle_txt" datatype="0,is_double" decimal="2"    value="" size="8" maxlength="9"/>'; 
			addTable.rows[length].cells[4].innerHTML = '&nbsp'; 
			addTable.rows[length].cells[5].innerHTML =  '<input type="hidden" name="zhek" class="middle_txt" id="zhek" datatype="0,is_double" decimal="2"  datatype="0,is_digit,2" maxlength="2"  value="" size="8"/>';   
			addTable.rows[length].cells[6].innerHTML = '处理方式：'; 
			addTable.rows[length].cells[7].innerHTML = genSelBoxExpPay("dealwith",<%=Constant.SERVICEACTIVITY_CAR_type%>,"",false,"min_sel","","true",'');
			addTable.rows[length].cells[8].innerHTML = '付费方式：'; 
			addTable.rows[length].cells[9].innerHTML = genSelBoxExpPay("paid",<%=Constant.PAY_TYPE%>,"<%=Constant.PAY_TYPE_02%>",false,"min_sel","","true",'');
			addTable.rows[length].cells[11].innerHTML = '<input type="hidden"  class="middle_txt" id="hiddenaa"   name="pro_codev" value="'+selObj.value+'" size="8" maxlength="9"/>';
		}
		return addTable.rows[length];
		}
		
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
	function addRow(tableId,newId,newCode,newTitle){
	 	
	 	
	    var addTable = document.getElementById(tableId);
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);


		insertRow.className = "table_list_row1";
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
			addTable.rows[length].cells[0].innerHTML =  '<td>'+length+'</td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><a href="#" onclick="viewNews('+newId+')">'+newCode+'</a><input type=hidden name="newsId" value="'+newId+'"/></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td>'+newTitle+'</td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRowConfirm(this,'+newId+');" /></td>';
			
			return addTable.rows[length];
		}
		
		function deleteRow(obj,id){
		makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/ServiceActivityManageDeleteProject.json?id='+id,returnBackcom,'fm','queryBtn');
		 var tabl=document.all['otherTableId'];
		 var index = obj.parentElement.parentElement.rowIndex;
		 tabl.deleteRow(index); 
	   }
		function re_back()
		{
			if('0' == '${typeleng}')
			{
				fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/serviceActivityManageInit.do";
				fm.submit();
			}else
			{
				fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/serviceActivityManageMarkfwInit.do";
				fm.submit();
			}
		}
 function y_e(){
	$('milage_add').disabled=false;
}
 function n_o(){
	 $('milage_add').disabled=true;
	 }
	function reloadRname(){
		var partname=document.getElementsByName("PART_NAME");
		var partcode=document.getElementsByName("PART_CODE");
		var mainPartCode = document.getElementsByName("mainPartCode");
		if(mainPartCode.length > 0){
			
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
	function reloadProject(){
		var labourname=document.getElementsByName("WR_LABOURNAME");
		var labourcode=document.getElementsByName("WR_LABOURCODE");
		var Labour0=document.getElementsByName("Labour0");
		
		if(labourcode.length > 0){
			for(var k=0; k<Labour0.length; k++){
				var temp=0;
				for(var j=0; j<labourcode.length; j++){
					if(labourcode[j].value==Labour0[k].value){
						Labour0[k].options.length = 0;
						var  varItem = new Option(labourname[temp].value,labourcode[j].value);
						Labour0[k].options.add(varItem);
					}
					temp++;
				}
			}
			for(var k=0; k<Labour0.length; k++){
				var temp=0;
				for(var j=0; j<labourcode.length; j++){
					if(labourcode[j].value!=Labour0[k].value){
						var  varItem = new Option(labourname[temp].value,labourcode[j].value);
						Labour0[k].options.add(varItem);
					}
					temp++;
				}
			}
		}
		
		
	}
 reloadRname();
 reloadProject();
</script>
</form>
</body>
</html>
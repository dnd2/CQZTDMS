<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.bean.TtAsActivityBean"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动管理</title>
<%
TtAsActivityBean beforeVehicle = (TtAsActivityBean) request.getAttribute("beforeVehicle");//服务活动信息-服务车辆活动范围
TtAsActivityBean afterVehicle = (TtAsActivityBean) request.getAttribute("afterVehicle");//服务活动信息-服务车辆活动范围
%>			
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
var myobj;
var cloMainTime=1; //关闭主工时选择页面
var cloTime=1; //关闭附属工时选择页面
var cloMainPart=1; //关闭主要配件选择页面
var cloPart=1; //关闭附加配件选择页面
function hideReplce(){
	document.getElementById("showorhide").style.display="";
	document.getElementById("showorhide1").style.display="";
	document.getElementById("show").style.display="none";
	document.getElementById("activityCode").disabled=false;
	document.getElementById("activityCode").value="";
	clearTable();
}
//初始清除table
function clearTable(){
	var obj1=document.getElementById("itemTableId");
	var obj2=document.getElementById("partTableId");
	document.getElementById("TROUBLE_DESC").value="";
	document.getElementById("TROUBLE_REASON").value="";
	document.getElementById("REPAIR_METHOD").value="";
	document.getElementById("APP_REMARK").value="";
    
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
	document.getElementById("activityCode").disabled=true;
	document.getElementById("activityCode").value="自动生成";
	clearTable();
}
//日历控件初始化
function deleteRow(obj,rel){
		 sendAjax('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/ServiceActivityManageDeleteProject.json?objv='+rel,returnBackcom,'fm');	
		 var tabl=document.all['otherTableId'];
		 var index = obj.parentElement.parentElement.rowIndex;
		 tabl.deleteRow(index); 
		 countSeq();
	}
	function returnBackcom()
	{
	}
	function doInit()
		{
		   loadcalendar();
		}
//索赔选择
	function chickFixfee(show){
		if(document.fm.isFixfee.checked==true){
			document.fm.isClaim.checked=true;
			show.style.display ="block";
		}else {
		 show.style.display ="block";
		}
	}
	function chickClaim(show){
		if(document.fm.isClaim.checked==false&&document.fm.isFixfee.checked==true){
			document.fm.isClaim.checked=true;
		}
	}
	function fix(show){
          if(document.fm.isFixfee.checked==true){
           show.style.display ="block";
           }else{
           show.style.display ="block";
           }
    }
    
	function subChecked() {
			//服务活动车辆范围结束
			if(!submitForm('fm')) {
				return false;
			}
			
			var actype=document.getElementById("activityType").value;
			if(actype=="10561005"){
				var yz1=document.getElementById("TROUBLE_DESC").value;
				var yz2=document.getElementById("TROUBLE_REASON").value;
				var yz3=document.getElementById("REPAIR_METHOD").value;
				var rn=document.getElementsByName("RESPONS_NATURE");
				var wr=document.getElementsByName("WR_LABOURCODE");
				var pc=document.getElementsByName("PART_CODE");
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
				MyConfirm("是否确认添加？",jude);
			}
			if(actype!="10561005"){
			 //disableBtn($("commitBtn"));//点击按钮后，按钮变成灰色不可用;
		    	MyConfirm("是否确认添加？",jude);
			}
	}
	//添加服务活动主数据信息
	function jude()
	{
		var id = document.getElementById('activityCode').value;
		makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/ServiceActivityManagejude.json?id='+id,returnBackjude,'fm','queryBtn');
	}
	
	function openTime(vale)
    {
    	sendAjax('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/ServiceActivityManageTime.json?id='+vale,returnBackTime,'fm');
    }
    function returnBackTime(json)
    {
    	document.getElementById('t1').value = json.StartDate;
    	document.getElementById('t2').value = json.EndDate;
    	document.getElementById('t3').value = json.factStartDate;
    	document.getElementById('t4').value = json.factEndDate;
    }
	function returnBackjude(json)
	{
		if( json.success == "1"  && json.fuwu == "1" && json.xianmu == "1" ) 
		{
			add();
		}else if(json.success == "0")
		{
			MyAlert("活动编号已存在！");
		}else if( json.fuwu == "0")
		{
			MyAlert("服务活动日期范围错误！");
		}else if(json.xianmu == "0")
		{
			MyAlert("配件赠送必须加上配件！");
		}
	}
	function add(){
		fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/serviceActivityManageAdd.do";
		fm.submit();
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
	//活动类别下拉框onchange事件
	function doMySel(value){
		if(value=='<%=Constant.SERVICEACTIVITY_KIND_02%>'){
			$('isFixfee').checked = true ;
			$('isClaim').disabled = 'disabled';
			chickFixfee(show) ;
			$('isFixfee').disabled = 'disabled' ;
			$('afterVehicle').disabled= 'disabled';
			$('beforeVehicle').disabled='disabled';
			$('activityFee').value = 0 ;
			$('activityFee').readOnly = true;
		}else{
		    $('activityFee').readOnly = false;
			$('isFixfee').checked = false ;
			$('isClaim').disabled = false;
			$('isClaim').checked = false;
			chickFixfee(show) ;
			$('isFixfee').disabled = false ;
			$('afterVehicle').disabled= false;
			$('beforeVehicle').disabled=false;
		}
	}
	// 生产基地下拉框显示控制
	function showYieldly(value){	
		if(value==1)
			$('yieldly1').style.display = 'block' ;
		else
			$('yieldly1').style.display = 'none' ;
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
	function deleteRowConfirm(obj){
		 var tabl=document.all['t_news'];
		 var index = obj.parentElement.parentElement.rowIndex;
		 
		 tabl.deleteRow(index); 
		 countSeq();
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
			addTable.rows[length].cells[3].innerHTML =  '<td><input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRowConfirm(this);" /></td>';
			
			return addTable.rows[length];
		}

	function viewNews(value){
		OpenHtmlWindow("<%=contextPath%>/claim/basicData/HomePageNews/viewNews.do?comman=2&newsId="+value,800,500);
	}
	//产生处理方式下拉框
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
		insertRow.insertCell(12);
		insertRow.insertCell(13);
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
			addTable.rows[length].cells[10].innerHTML = ''; 
			addTable.rows[length].cells[11].innerHTML = ''; 
			addTable.rows[length].cells[12].innerHTML = '<input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRow(this,'+selObj.value+');" />'; 
			addTable.rows[length].cells[13].innerHTML = '<input type="hidden" name= "pro_codev" class="middle_txt"    value="'+selObj.value+'" size="8" maxlength="9"/>';
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
			addTable.rows[length].cells[10].innerHTML = ''; 
			addTable.rows[length].cells[11].innerHTML = ''; 
			addTable.rows[length].cells[12].innerHTML = '<input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRow(this,'+selObj.value+');" />';  
			addTable.rows[length].cells[13].innerHTML = '<input type="hidden"  class="middle_txt" id="hiddenaa"   name="pro_codev" value="'+selObj.value+'" size="8" maxlength="9"/>';
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
			addTable.rows[length].cells[10].innerHTML = ''; 
			addTable.rows[length].cells[11].innerHTML = ''; 
			addTable.rows[length].cells[12].innerHTML = '<input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRow(this,'+selObj.value+');" />';  
			addTable.rows[length].cells[13].innerHTML = '<input type="hidden"  class="middle_txt" id="hiddenaa"   name="pro_codev" value="'+selObj.value+'" size="8" maxlength="9"/>';
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
			addTable.rows[length].cells[10].innerHTML = ''; 
			addTable.rows[length].cells[11].innerHTML = ''; 
			addTable.rows[length].cells[12].innerHTML = '<input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRow(this,'+selObj.value+');" />';  
			addTable.rows[length].cells[13].innerHTML = '<input type="hidden"  class="middle_txt" id="hiddenaa"   name="pro_codev" value='+selObj.value+' size="8" maxlength="9"/>';
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
			addTable.rows[length].cells[10].innerHTML = ''; 
			addTable.rows[length].cells[11].innerHTML = ''; 
			addTable.rows[length].cells[12].innerHTML = '<input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRow(this,'+selObj.value+');" />';  
			addTable.rows[length].cells[13].innerHTML = '<input type="hidden"  class="middle_txt" id="hiddenaa"   name="pro_codev" value="'+selObj.value+'" size="8" maxlength="9"/>';
		}
		return addTable.rows[length];
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
function wrapOut2()
{
  document.getElementById('subjectName').value = '';
  document.getElementById('subjectId').value = '';
}

function clearCode()
{
  document.getElementById('ACTIVITY_CODE').value = '';
  var acs = document.getElementsByName('ACCODES');
  for(var i=0; i<acs.length; i++){
	  acs[i].checked = false;
  }
}
		
function showsubjectId(subjectName,subjectId)
{
	OpenHtmlWindow('<%=contextPath%>/dialog/subjectName.jsp?subjectName='+subjectName+"&subjectId="+subjectId+"&type="+${typeleng},800,460);
}

function selAccodes() {
	var acs = document.getElementsByName('ACCODES');
	var str = "";
	for(var i=0; i<acs.length; i++){
		if(acs[i].checked){
			str += acs[i].value;
		}
	}
	document.getElementById("ACTIVITY_CODE").value = str;
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
			addTable.rows[length].cells[0].innerHTML =  '<td><input type="text" class="phone_txt"  id="WR_LABOURCODE'+length+'" name="WR_LABOURCODE0" readonly  value="" size="10"/><span style="color:red">*</span><a href="#"  onclick="selectMainTime(this);">选择</a></td>';
			setMustStyle([document.getElementById("WR_LABOURCODE"+length)]);
			addTable.rows[length].cells[1].innerHTML =  '<td><span class="tbwhite"><input type="text" id="WR_LABOURNAME'+length+'" name="WR_LABOURNAME0" datatype="0,is_textarea,100" class="long_txt" value="" datatype="0,is_double,10" decimal="2" size="10" readonly/></span></td>';
			/* addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" name="LABOUR_HOURS0" class="little_txt"   value="" size="8" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" name="LABOUR_PRICE0" class="little_txt"   value="" size="8" maxlength="11" id="y20000001" readonly/></td>';
			 */
			addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" id="LABOUR_AMOUNT'+length+'" name="LABOUR_AMOUNT0"  class="middle_txt"  datatype="0,is_double,10" decimal="2" value="" size="8" maxlength="9" /></td>';
			setMustStyle([document.getElementById("LABOUR_AMOUNT"+length)]);
			addTable.rows[length].cells[3].innerHTML = genSelBoxExpPay("PAY_TYPE_ITEM",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'<%=Constant.PAY_TYPE_01%>');
			addTable.rows[length].cells[4].innerHTML ='<td><input readonly="readonly" id="MALFUNCTIONS'+length+'" name="MALFUNCTIONS" datatype="0,is_textarea,100" class="middle_txt" value="" /><input type="hidden" id="MALFUNCTION" name="MALFUNCTION" class="middle_txt" value="" /><a href="#" onclick="selectMalCode(this);">选择</a></td>';
			addTable.rows[length].cells[5].innerHTML =  '<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'itemTable\',this,\'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this);"/></td>';
		}
		if(table=='partTableId'){

			insertRow.insertCell(6);
			insertRow.insertCell(7);
			insertRow.insertCell(8);
			insertRow.insertCell(9);
			insertRow.insertCell(10);
			insertRow.insertCell(11);
/* 			addTable.rows[length].cells[0].innerHTML = '<td><input type="checkbox" value="" onclick="return false;" name="IS_GUA" /></td>';
 */			addTable.rows[length].cells[0].innerHTML =  '<td><input type="text" class="phone_txt" name="PART_CODE"   value="" size="10" id="PART_CODE" readonly/><span style="color:red">*</span><span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this)">选择</a></span></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><span class="tbwhite"><input type="text" class="phone_txt" name="PART_NAME"  readonly  id="PART_NAME'+length+'" datatype="0,is_textarea,100"  size="10"/></span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" class="little_txt" onkeyup="countQuantity(this)"  onblur="countQuantity(this)" size="10" name="QUANTITY" id="QUANTITY'+length+'" datatype="0,is_double,10" decimal="2"  maxlength="20"/></td>';
			setMustStyle([document.getElementById("QUANTITY"+length)]);
 			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" class="phone_txt" name="PART_CODE_OLD"   value="" size="10" id="PART_CODE_OLD" readonly/><span style="color:red">*</span><span class="tbwhite"></span></td>';
			addTable.rows[length].cells[4].innerHTML =  '<td><span class="tbwhite"><input type="text" class="phone_txt" name="PART_NAME_OLD"  readonly  id="PART_NAME_OLD'+length+'" datatype="0,is_textarea,100"  size="10"/></span></td>';
			/* addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" class="little_txt"name="PRICE0"  size="10" maxlength="11" id="PRICE" readonly/></td>';
			addTable.rows[length].cells[5].innerHTML =  '<td><input type="text" class="little_txt" name="AMOUNT0" id="AMOUNT" size="10" maxlength="9" datatype="0,is_double,10" decimal="2" readonly /></td>';
			 */addTable.rows[length].cells[5].innerHTML = genSelBoxExpPay("PAY_TYPE_PART",<%=Constant.PAY_TYPE%>,"",false,"min_sel","","false",'<%=Constant.PAY_TYPE_01%>');
			addTable.rows[length].cells[6].innerHTML =  '<td><select id="Labour" value="" name="Labour0" ></select></td>';
			addTable.rows[length].cells[7].innerHTML =  '<td><select id="mainPartCode" value="" name="mainPartCode" ><option value="-1">---请选择---</option></select></td>';
			addTable.rows[length].cells[8].innerHTML =  genSelBoxExpPay10("RESPONS_NATURE",<%=Constant.RESPONS_NATURE_STATUS%>,"",false,"min_sel","","false",'');
			addTable.rows[length].cells[9].innerHTML =  genSelBoxExpPay("HAS_PART",<%=Constant.IF_TYPE%>,"",false,"min_sel","","false",'<%=Constant.IF_TYPE_NO%>');
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
				if(v2==table.childNodes[i].childNodes[0].childNodes[1].value){
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
		var activityType = document.getElementById("activityType").value;
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
	    	MyConfirm("是否删除？",delPartItems,[tr]);
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
	  	myobj.cells.item(0).innerHTML='<input type="hidden" name="def_id" value="'+def_id+'"/><input type="hidden" name="part_id" value="'+partId+'"/><input type="text" class="phone_txt" name="PART_CODE"   value="'+partCode+'" size="10" id="PART_CODE" readonly="true"/><span style="color:red">*</span><span class="tbwhite"><a href="#" onClick="javascript:selectMainPartCode(this)">选择</a></span>';
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
				var malValue = partTable.childNodes[a].childNodes[8].childNodes[0].value;
				if(malValue==<%=Constant.RESPONS_NATURE_STATUS_01%>){//如果是主因件则加入集合
					partcodes[temp]=partTable.childNodes[a].childNodes[0].childNodes[0].value;
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
				var malValue = partTable.childNodes[k].childNodes[8].childNodes[0].value;
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

<body onLoad="fix(show);">
<table cellSpacing=0 cellPadding=0 width="100%" border=0>
	<tbody>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td height="30">
				<div class="navigation">
					<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动管理
				</div>
			</td>
		</tr>
	</tbody>
</table>
<form method="post" name="fm" id="fm">
<input name="typeleng" value="${typeleng}" type="hidden" />
<input name="activityId" id="activityId" type="hidden" value="${activityId}"/>
<table class="table_edit" >
	<tr>
		<th colspan="6">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息
		</th>
	</tr>
	<tr>
		<!--
		<td width="16%" align="right">活动编号：</td>
		<td width="28%" align="left">
		             系统自动生成(新增时不显示)<font color="red">*</font>
			  <input name="activityCode" id="activityCode" value="" type="text" class="middle_txt" datatype="0,is_digit_letter,18" > 
		</td>
		-->
		<td width="10%" align="right">服务活动主题：</td>
	       <td width="30%" >
				<input type="text" readonly="readonly" name="subjectName" id="subjectName" class="long_txt"/>
				<input type="hidden" name="subjectId" id="subjectId"/>
				<input type="hidden" name="activityType" id="activityType"/>
				<input type="button" class="mini_btn" value="..." onclick="showsubjectId('subjectName','subjectId');"/>
            	<input type="button" class="normal_btn" value="清除" onclick="wrapOut2();"/>
			 </td>
		<td width="10%" align="right">活动编号：</td>
		<c:if test="${typeleng == 1}">
			<td width="20%">
			    <input name="activityCode" readonly="readonly" id="activityCode" value="" type="text" class="middle_txt" >
			</td>
		</c:if>
		<c:if test="${typeleng == 0}">
			<td width="20%">
			    <input name="activityCode" id="activityCode" value="" type="text" class="middle_txt" datatype="0,is_null,100">
			</td>
		</c:if>
		
	</tr>
	<tr>
		<td width="10%" align="right">活动名称：</td>
		<td width="20%">
		     <input name="activityName" id="activityName" value="" type="text" class="middle_txt" datatype="0,is_null,100">
		</td>
		<td width="10%" align="right">实际活动日期</td>
		<td width="20%">
			<input class="short_txt" id="t3" name="factstartdate" datatype="1,is_date,10"
                   maxlength="10" group="factstartdate,factenddate"/>
            <input class="time_ico" value=" " onclick="showcalendar(event, 'factstartdate', false);" type="button"/>
                    至
             <input class="short_txt" id="t4" name="factenddate" datatype="1,is_date,10"
                    maxlength="10" group="factstartdate,factenddate"/>
             <input class="time_ico" value=" " onclick="showcalendar(event, 'factenddate', false);" type="button"/>
		   <input type="hidden" class="middle_txt" value="10000"  name="car_max" id="car_max" datatype="0,is_digit,6" />
		</td>
	</tr>
	<tr>
		<td width="10%" align="right" >信息录入日期：</td>
		<td width="20%">
			<div align="left">
			<input class="short_txt" id="t1" name="startDate" datatype="0,is_date,10"
                   maxlength="10" group="t1,t2"/>
            <input class="time_ico" value=" " onclick="showcalendar(event, 't1', false);" type="button"/>
                    至
             <input class="short_txt" id="t2" name="endDate" datatype="0,is_date,10"
                    maxlength="10" group="t1,t2"/>
             <input class="time_ico" value=" " onclick="showcalendar(event, 't2', false);" type="button"/>
            </div>
	    </td>
	    <td width="10%" align="right">服务活动类型二级：</td>
		<td width="20%">
		    <script type="text/javascript">
   				genSelBoxExp("typetowactivity",<%=Constant.TYPE_tow_activity%>,<%=Constant.TYPE_tow_activity1%>,true,"short_sel","","true",'');
  		    </script>
		</td>
	</tr>
	
	<tr>
     	
     	<td width="10%" align="right">结算指向：</td>
     	<td width="20%"><input id="default" onclick="showYieldly(0);" value="1" checked="checked" type="radio" name="default" />
          按产地结算
          <input id="directed" onclick="showYieldly(1);" value="2" type="radio" name="default" />
          定向结算 <span style="DISPLAY: none" id="yieldly1">
          <select name="yieldly" id="yieldly">
	              <c:forEach var="areaPO" items="${areaPO}" >
 				  <option value="${areaPO.areaId}" >
    				<c:out value="${areaPO.areaName}"/>
    			  </option>
    			 </c:forEach>
             </select>
		</span>
		<TD width="10%" align=right>服务活动车辆范围：</TD>
        <TD><input id=beforeVehicle value=11321001 type=checkbox name=beforeVehicle />
          售前车
          <input id=afterVehicle value=11321002 checked type=checkbox name=afterVehicle />
          售后车 <span style="COLOR: red">*</span></TD>
	</tr>
	<tr>
		<td width="10%" align="right" nowrap="true">活动方案说明：</td>
		<td colspan="3" align="left">
			<textarea name="solution" id="solution" class="SearchInput" rows="6" cols="90" datatype="0,is_null,200"></textarea>
		</td>
	</tr>	 
	<tr>
		<td width="10%" align="right"  nowrap="true">活动申请指导：</td>
		<td colspan="3" align="left">
			<textarea name="claimGuide" id="claimGuide" class="SearchInput" rows="6" cols="90" datatype="0,is_null,200"></textarea>
		</td>
	</tr>
</table>
<TABLE id=otherTableId class=table_list border=0 cellSpacing=1 cellPadding=0 align=center>
    <TBODY id="showorhide">
      <TR >
        <TH colSpan=14 align=left><IMG class=nav src="../../../img/subNav.gif"> 活动项目：
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
          
          <input id=otherBtn class=normal_btn onclick="javascript:addRow2('otherTable');" name=button422 value=新增 type=button />
          <c:if test="${typeleng == 1}">
          		活动套餐：<input type="text" name="ACTIVITY_CODE" class="short_txt" id="ACTIVITY_CODE" datatype="0,is_digit_letter,10" readonly="readonly">
            		<input type="button" class="normal_btn" value="清除" onclick="clearCode();"/>
            		<c:forEach items="${acCodes}" var="ac">
						<input type="checkbox" name="ACCODES" value="${ac.NAME}" onclick="selAccodes();">${ac.NAME}
					</c:forEach>
          </c:if>
          	
					
					
          </TH>
    <TBODY id=otherTable>
    </TBODY>
  </TABLE>
<table id="showorhide1">
	<tr ><td colspan="6">
        <input class="normal_btn"  name="add4" type="button" onclick="selectMainNew();" value ='新增' /></td>
    </tr>
    <tr>
      <td colspan="6">
      	<table class="table_list_line" id="t_news" border="1">
	        <tr >
	          <th width="50" align="center" nowrap="nowrap" >NO </th>
	          <th width="220" align="center" nowrap="nowrap" >编码 </th>
	          <th width="400" align="center" nowrap="nowrap" >新闻名称</th>
	          <th width="80" align="center" nowrap="nowrap" >操作 </th>
	        </tr>
		</table>
		</td>
	</tr>
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
							name="button422" onClick="javascript:genAppTimeCombo1(addRowRaplce('partTableId'));" />
					</td>
				</tr>
				<tbody id="partTable">
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
						<textarea id="TROUBLE_DESC" name='TROUBLE_DESC' datatype="0,is_textarea,100" maxlength="100" rows='2' cols='28'></textarea>
					</td>
					<td class="table_edit_2Col_label_5Letter">
						故障原因：
					</td>
					<td colspan="3" class="tbwhite">
						<textarea id="TROUBLE_REASON" name='TROUBLE_REASON' maxlength="100"	datatype="0,is_textarea,100" rows='2' cols='28'></textarea>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_5Letter">
						维修措施：
					</td>
					<td colspan="3" class="tbwhite">
						<textarea  id='REPAIR_METHOD' name='REPAIR_METHOD' datatype="0,is_textarea,100" maxlength="100" rows='2' cols='28'></textarea>
					</td>
					<td class="table_edit_2Col_label_5Letter">
						申请备注：
					</td>
					<td colspan="3" class="tbwhite">
						<textarea id='APP_REMARK'  name='APP_REMARK' 	datatype="1,is_textarea,100" maxlength="100" rows='2' cols='28'></textarea>
					</td>
				</tr>
          </table>
	</div>
<br/>
<table class="table_edit">
	<tr>
		<td colspan="4" align="center">
			<input type="button" name="bt_add" class="long_btn" onclick="subChecked();" value="保存" id="commitBtn"/>
		    <input type="button" name="bt_back" class="normal_btn" onclick="re_back()" value="返回"/>
	    </td>
	</tr>
	<tr>
		<td colspan="4" align="center"></td>
	</tr>
</table>
</form>
</body>
</html>
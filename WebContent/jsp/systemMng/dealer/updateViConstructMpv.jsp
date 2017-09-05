<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@page import="java.util.Arrays"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/change" prefix="change" %>

<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>

<% String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
	List  attachList   = (List)request.getAttribute("attachList");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>新增建店支持</title>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<script type="text/javascript">
var g_webAppName = '<%=request.getContextPath()%>';
var dealerLevel='<%=Constant.DEALER_LEVEL_01%>';
var yearArray=new Array("第   1 年","第    2 年","第   3 年","第    4  年","第    5 年","第   6 年","第   7 年","第   8 年","第   9 年","第  10 年","第 11 年","第 12 年","第 13 年","第 14 年","第 15 年","第 16 年","第  17 年","第十八年","第十九年","第二十年"); 
var  i3="";
var yearId=2;
var saveFlag="${size}";
var oldYearId="${size}";
var statusArray=new Array(100);
var yearstatus="${size+1}";



//添加年份 
function addYear(){
	var ih=document.getElementById("tab1").innerHTML;
	var CHAyear=yearArray[oldYearId];
	var yearStatusValue=' <input type="hidden" name="yearStatus" value="'+yearstatus+'"/>';
	ih='<table id="table_'+oldYearId+'" year="'+CHAyear+'" width=100% border="0" align="center" cellpadding="1" cellspacing="1" id="sec_year"> '+ih+yearStatusValue+'<table>';
	if(ih.indexOf("第")>0){
		//替换元素
		var i1=ih.substring(0, ih.indexOf("第二年"));
		var i2=ih.substring(ih.indexOf("第二年")+3,ih.length);
		i3=i1+CHAyear+i2;
	}
	if(i3.indexOf("saveViDetail()")>0){
		i3=i3.replace("saveViDetail()","saveViDetail("+oldYearId+")");
	}if(i3.indexOf("addViDetail()")>0){
		i3=i3.replace("addViDetail()","addViDetail("+oldYearId+")");
	}
	
	var div_1=document.getElementById("year_div");
	div_1.innerHTML=div_1.innerHTML+i3;	
	statusArray[oldYearId]=1;
	
	document.getElementById("_saveBtnAll").removeAttribute("disabled");
	document.getElementById("_addBtnAll").removeAttribute("disabled");

	// 将DETAIL_ID置空
	var table = document.getElementById("table_"+oldYearId);
	table.getElementsByTagName("input").namedItem("DETAIL_ID").setAttribute("value","");
	oldYearId++;
	yearstatus++;
}


//判断非空，格式
function checkNull(){
	var flag=true;
	var tables=document.getElementsByTagName("table");
	//所有table
	outer: for(var n=0;n<tables.length;n++){
		var tableName=tables[n].year;
		if(tableName){
			var inputs = tables[n].getElementsByTagName("input");
	 		var len=inputs.length;
	 		var stime;
	 		var etime;
	 		var SUPPORT_SDATE;
	 		var SUPPORT_EDATE;
	 		//table中所有input
	 		for(var i=0;i<len;i++){
	 			var input=inputs[i];
	 			var inputVal = input.value;
	 			var cn_name=input.cn_name;
	 			var name=input.name;
	 			if(input.type=="text"){
	 				if(cn_name!=null){
	 					if(name=="CONSTRUCT_SDATE"){
	 						stime=inputVal;
	 					}if(name=="CONSTRUCT_EDATE"){
	 						etime=inputVal;
	 					}
	 					if(name=="SUPPORT_SDATE"){
	 						SUPPORT_SDATE=inputVal;
	 					}
	 					if(name=="SUPPORT_EDATE"){
	 						SUPPORT_EDATE=inputVal;
	 					}
	 				}
	 				if(inputVal==""&&cn_name!="" ){
	 					MyAlert(tableName+":"+cn_name+"不能为空");
	 					flag=false;
	 					break outer;
	 				}
	 			}
	 		}
	 		if(dateCompare(stime,etime)){
				MyAlert(tableName+":建设时间 -结束时间不能小于开始时间");
				flag= false;
				break outer;
			}
			if(dateCompare(SUPPORT_SDATE,SUPPORT_EDATE)){
				MyAlert(tableName+":支持月度 -结束时间不能小于开始时间");
				flag= false;
				break outer;
			}
		}
	}
	return flag;
}



//判断日期大小
function dateCompare(date1,date2){
	date1 = date1.replace(/\-/gi,"/");
	date2 = date2.replace(/\-/gi,"/");
	var time1 = new Date(date1).getTime();
	var time2 = new Date(date2).getTime();
	
	if(time1 > time2){
		return true;
	}else{
		return false;
	}
}


function doInit()
{
	
	var body=document.getElementById("body");
	
	document.getElementById("addYear").onclick=function(){
		addYear();
	}
	loadcalendar();
	var VI_CONSTRUCT_TYPE_VALUE = document.getElementById("VI_CONSTRUCT_TYPE").value;
	onchangeViConstructType(VI_CONSTRUCT_TYPE_VALUE);

	setTableDisable();
}   
function onchangeVlidateSaleQty(obj,offLineRebate) {
    if (obj.value == "") {
        return;
    }
    if (isNaN(obj.value)) {
        MyAlert("请输入数字!");
        obj.value = "";
        return;
    }
    var idx = obj.parentElement.parentElement.rowIndex;
    var tbl = document.getElementById('myTable');

    if (obj.value != '0') {
        tbl.rows[idx].cells[1].firstChild.checked = true;
    } else {
        tbl.rows[idx].cells[1].firstChild.checked = false;
    }
}

function onchangeViConstructType(value) {
	var table = document.getElementById("table_0");  
	var offlineRebate = document.getElementById("offlineRebate").value;
	if (value == 20021002) {
		var tr = table.insertRow(6);
		var td0 = tr.insertCell(0);
		td0.innerHTML = "线下已返(元)：";
		td0.setAttribute("align","right");
		var td1 = tr.insertCell(1);
		td1.colSpan = 5;
		td1.setAttribute("align","left");
		td1.innerHTML = "<input id='offline_rebate' name='offline_rebate' cn_name='线下返利' datatype='0,isMoney,10' class='middle_txt'  type='text' value='"+offlineRebate+"'/>";
	} else {
		var rowNum = table.rows.length;
		if (rowNum > 9) {
			table.deleteRow(6);
		}
	}
}

function onchangeVlidateSaleQty1(obj) {
    if (obj.value == "") {
        return;
    }
    if (isNaN(obj.value)) {
        MyAlert("请输入数字!");
        obj.value = "";
        return;
    }
    var re = /^[0-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "";
        return;
    }

    var idx = obj.parentElement.parentElement.rowIndex;
    var tbl = document.getElementById('myTable');

    if (obj.value != '0') {
        tbl.rows[idx].cells[1].firstChild.checked = true;
    } else {
        tbl.rows[idx].cells[1].firstChild.checked = false;
    }
}
function addVi()
{
	var fjid = document.getElementsByName("fjid");
// 	if(fjid.length==0){
// 		MyAlert("请添加附件");
// 		return false;
// 	}
	document.getElementById("BUTTON_TYPE").value="2";
	var flag=checkNull();
	if(flag){
		deleteNullTable();
		var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/saveVi.do?list="+list;
		fm.action = url;
	 	fm.submit();
	}
	
}  
//去除jsp模板
function deleteNullTable(){
	var div_2=document.getElementById("div_2");
	div_2.innerHTML="";
}
function saveVi()
{   
	var fjid = document.getElementsByName("fjid");
// 	if(fjid.length==0){
// 		MyAlert("请添加附件");
// 		return false;
// 	}
	var flag=checkNull();
	if(flag){
		//保存
		document.getElementById("BUTTON_TYPE").value="1";
		deleteNullTable();
		var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/saveVi.do?list="+list;
		fm.action = url;
	 	fm.submit();
	}
}  
function stopVi(year,dealerId, detailId) {
	if(window.confirm('您确定要终止吗?')){
		var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/stopVi.do?year="+year+"&DEALER_ID="+dealerId+"&DETAIL_ID="+detailId;
		fm.action = url;
	 	fm.submit();
	}
}  

function recoveryVi(year, dealerId, detailId) {
	if(window.confirm('您确定要恢复吗?')){
		var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/recoveryViCheck.json?&DETAIL_ID="+detailId;
		sendAjax(url,function(json){
			if(json.Exception){
				MyAlert(json.Exception.message);   
			} else {
				var addSupportNumber = document.getElementsByName("SUPPORT_NUMBER")[0].value;
				var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/recoveryVi.do?year="+year+"&DEALER_ID="+dealerId+"&DETAIL_ID="+detailId+"&addSupportNumber="+addSupportNumber;
				fm.action = url;
			 	fm.submit();
			}
		},'fm1');
	}	
}  

function selectOther(selectOther) {
	OpenHtmlWindow(g_webAppName + '/sales/financemanage/SalesPolicyManager/queryDeployedPolicyInit.do?id='+selectOther,700,500);
}
function otherConfirm(id, ids, otherAmount, minDate, maxDate) {
	document.getElementById(id).value = otherAmount;
	_hide();
}

function saveViDetail(index){
	document.getElementById("_saveBtn").disabled = 'disabled';
	var fjid = document.getElementsByName("fjid");
	var fjids = '';
// 	if(fjid.length==0){
// 		MyAlert("请添加附件");
// 		return false;
// 	}
	for (var int = 0; int < fjid.length; int++) {
		if(int>0){
			fjids = fjids +","+fjid[int].value;
		}else{
			fjids = fjid[int].value;
		}
	}
	document.getElementById("fjid_n").value=fjids;
	document.getElementById("BUTTON_TYPE1").value="1";
	var table=document.getElementById("table_"+index);
	var inputs=table.getElementsByTagName("input");
	var selects=table.getElementsByTagName("select");
	var textarea=table.getElementsByTagName("textarea");
	document.getElementById("SIZE").value=index+1;
	
	

	var stime="";
	var etime="";
	var SUPPORT_SDATE="";
	var SUPPORT_EDATE="";
	
	for(var i=0;i<inputs.length;i++){
		var input=inputs[i];
		var name=input.name;
		var value=input.value;
		var cn_name=input.cn_name;
		
		
	
		if(name=="CONSTRUCT_SDATE"){
			stime=value;
		}if(name=="CONSTRUCT_EDATE"){
			etime=value;
		}if(name=="SUPPORT_SDATE"){
			SUPPORT_SDATE=value;
		}if(name=="SUPPORT_EDATE"){
			SUPPORT_EDATE=value;
		}
		
	//	MyAlert(cn_name+"--"+name+"--"+value);
		var dom=document.getElementById(name+"_n");
		if(value==""&&cn_name){
			MyAlert(cn_name+"不能为空");
			return false;
		}
		if(dom){
			dom.value=value;
		}
	}
	for(var i=0;i<selects.length;i++){
		var select=selects[i];
		var name=select.name;
		var value=select.value;
		
		var dom=document.getElementById(name+"_n");
		
		if(value==""){
			if(name=="AMOUNT"){
				MyAlert("提车支持款不能为空");
				return false;
			}else if(name=="IMAGE_LEVEL"){
				MyAlert("申请形象等级不能为空");
				return false;
			}else if(name=="IMAGE_COMFIRM_LEVEL"){
				MyAlert("验收形象等级不能为空");
				return false;
			}
		}
		if(dom){
			dom.value=value;
		}
	}
	for(var i=0;i<textarea.length;i++){
		var input=textarea[i];
		var name=input.name;
		var value=input.value;
		var dom=document.getElementById(name+"_n");
		if(dom){
			dom.value=value;
		}
	}
	if(dateCompare(stime,etime)){
		MyAlert("建设时间 -结束时间不能小于开始时间");
		return false;
	}
	if(dateCompare(SUPPORT_SDATE,SUPPORT_EDATE)){
		MyAlert("支持月度 -结束时间不能小于开始时间");
		return false;
	}
	
	var url="<%=contextPath%>/sysmng/dealer/DealerInfo/singleSaveVi.json?list="+list;
	sendAjax(url,function(json){
		if (json.Exception) {
			MyAlert(json.Exception.message);
		} else {
			document.getElementById("idSingle").value=json.result;
			document.getElementById("idAll").value=json.result;
			table.getElementsByTagName("input").namedItem("DETAIL_ID").setAttribute("value",json.DETAIL_ID);
			MyAlert("保存成功");
			var tr = table.rows[table.rows.length - 3];
			tr.deleteCell(1);
			var td1 = tr.insertCell(1);
			td1.innerHTML="<span>已保存</span>";
			document.getElementById("_saveBtn").disabled = '';
		}
	},'fm1');
} 

function saveViDetailForAfterSubmit() {
	var table=document.getElementById("table_0");
	var IMAGE_COMFIRM_LEVEL = table.getElementsByTagName("select").namedItem("IMAGE_COMFIRM_LEVEL").value;
	var mainId = document.getElementById("idAll").value;
	var url="<%=contextPath%>/sysmng/dealer/DealerInfo/singleSaveViForAfterSubmit.json?IMAGE_COMFIRM_LEVEL="+IMAGE_COMFIRM_LEVEL+"&mainId="+mainId;
	sendAjax(url,function(json){
		if(json.Exception){
			MyAlert(json.Exception.message);
		} else {
			MyAlert("保存成功");
		}
	},'fm1');
}

function addViDetail(index){
	document.getElementById("_addBtn").disabled = 'disabled';
	var fjid = document.getElementsByName("fjid");	
	var fjids = '';
// 	if(fjid.length==0){
// 		MyAlert("请添加附件");
// 		return false;
// 	}
	for (var int = 0; int < fjid.length; int++) {
		if(int>0){
			fjids = fjids +","+fjid[int].value;
		}else{
			fjids = fjid[int].value;
		}
	}
	document.getElementById("fjid_n").value=fjids;
	document.getElementById("BUTTON_TYPE1").value="2";
	if(index>1){
		var lastStatus=statusArray[index-1];
		if(lastStatus!=2&&lastStatus!=3){
			MyAlert("请先上报上一年数据");
			return false;
		}
	}
	var table=document.getElementById("table_"+index);
	var inputs=table.getElementsByTagName("input");
	var selects=table.getElementsByTagName("select");
	var textarea=table.getElementsByTagName("textarea");
	document.getElementById("SIZE").value=index+1;
	
	var stime="";
	var etime="";
	var SUPPORT_SDATE="";
	var SUPPORT_EDATE="";
	
	for(var i=0;i<inputs.length;i++){
		var input=inputs[i];
		var name=input.name;
		var value=input.value;
		var cn_name=input.cn_name;
	
		if(name=="CONSTRUCT_SDATE"){
			stime=value;
		}if(name=="CONSTRUCT_EDATE"){
			etime=value;
		}if(name=="SUPPORT_SDATE"){
			SUPPORT_SDATE=value;
		}if(name=="SUPPORT_EDATE"){
			SUPPORT_EDATE=value;
		}
		
	//	MyAlert(cn_name+"--"+name+"--"+value);
		var dom=document.getElementById(name+"_n");
		if(value==""&&cn_name){
			MyAlert(cn_name+"不能为空");
			return false;
		}
		if(dom){
			dom.value=value;
		}
	}
	
	for(var i=0;i<selects.length;i++){
		var select=selects[i];
		var name=select.name;
		var value=select.value;
		var dom=document.getElementById(name+"_n");
		if(value==""){
			if(name=="AMOUNT"){
				MyAlert("提车支持款不能为空");
				return false;
			}else if(name=="IMAGE_LEVEL"){
				MyAlert("申请形象等级不能为空");
				return false;
			}else if(name=="IMAGE_COMFIRM_LEVEL"){
				MyAlert("验收形象等级不能为空");
				return false;
			}
		}
		if(dom){
			dom.value=value;
		}
	}
	
	for(var i=0;i<textarea.length;i++){
		var input=textarea[i];
		var name=input.name;
		var value=input.value;
		var dom=document.getElementById(name+"_n");
		if(dom){
			dom.value=value;
		}
	}

	if (index == 0) {
		if(dateCompare(stime,etime)){
			MyAlert("建设时间 -结束时间不能小于开始时间");
			return false;
		}
	}
	if(dateCompare(SUPPORT_SDATE,SUPPORT_EDATE)){
		MyAlert("支持月度 -结束时间不能小于开始时间");
		return false;
	}
	
	
	var url="<%=contextPath%>/sysmng/dealer/DealerInfo/singleSaveVi.json?list="+list;
	sendAjax(url,function(json){
		if (json.Exception) {
			MyAlert(json.Exception.message);
		} else {
			document.getElementById("idSingle").value=json.result;
			document.getElementById("idAll").value=json.result;
			table.getElementsByTagName("input").namedItem("DETAIL_ID").setAttribute("value",json.DETAIL_ID);
			statusArray[index]=2;
			MyAlert("上报成功");
			
			
			for(var i=0;i<inputs.length;i++){
			//	if(inputs[i].value!="增加"||inputs[i].value!="保存"||inputs[i].value!="上报"||inputs[i].value!="终止"){
				if(inputs[i].value!="增加"){
					inputs[i].disabled="disabled";
				}
			}
			for(var i=0;i<selects.length;i++){
				selects[i].disabled="disabled";
			}
			textarea[0].disabled = "disabled";
			// 状态变更
			var tr = table.rows[table.rows.length - 3];
			tr.deleteCell(1);
			var td1 = tr.insertCell(1);
			td1.innerHTML="<span>销售部审核中</span>";
			document.getElementById("_addBtn").disabled = '';
			setTableDisable();
		}
	},'fm1');
} 
//设置页面table为disable
function goBack(){
	window.location.href="<%=contextPath%>/sysmng/dealer/DealerInfo/viConstructQryInit.do";
}

</script>
</head>
<body>
<form method="post" id="fm" name="fm">

<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 财务管理&gt; 财务管理 &gt;形象店支持维护&gt;新增建店支持</div>
<input id="STATUS" name="STATUS" value="${dMap_main.STATUS }" type="hidden"/>
<input type="hidden"  name="ID1" id="idAll"  value="${dMap_detail[0].ID}"/>
<input type="hidden"  name="vehicle_series_flag1" id="vehicle_series_flag1" value="2015011508783002"/>
<input type="hidden"  name="offlineRebate" id="offlineRebate" value="${dMap_main.OFFLINE_REBATE}"/>
<input id="BUTTON_TYPE" name="BUTTON_TYPE" value="" type="hidden"/>
 <table id="year1" width=100% border="0"  align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <tr>
			<TH colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif"> 经销商基础信息</TH>
    	  </tr>
		  <tr>
		  <input type="hidden"  name="ID"  value="${dMap_detail[0].ID}"/>
		  <input type="hidden"  name="PAGE_TYPE"  value="2"/>
		  <input id="DEALER_ID" name="DEALER_ID" value="${dealerInfo.dealerId}"class=""  type="hidden"/></td>
		    <td align="right" width="20%">经销商简称：</td>
		    <td align="left">
		    	${dealerInfo.dealerShortname }
		    </td>
		    		    <td align="right" width="20%">经销商全称：</td>
		    <td align="left">
		    	${dealerInfo.dealerName }
		    	           
		    </td>
      </tr>
		  <tr>
		    <td align="right" width="20%">经销商代码：</td>
		    <td align="left">
		    	${dealerInfo.dealerCode }
		    </td>

		    		    <td align="right">经销商状态：</td>
		    <td align="left" colspan="3">
				<script type="text/javascript">
                    document.write(getItemValue("${dealerInfo.serviceStatus}"));
                </script>
		    </td>
	      </tr>
			 <tr><td width="100%" colspan=4>
	    <table class="table_info" border="0" id="file">
	    <tr>
				<span>
					<input type="button" class="cssbutton"  onclick="showUpload('<%=request.getContextPath()%>')" value ='添加附件'/>
					<font color="red" style="padding-left: 50px;">提示：图片分辨率为800*600</font>
				</span>
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
  		<input type="hidden" name="delAttachs" id="delAttachs" value="" />
  		 	  		 <c:if test="${attachList!=null}">
  			<c:forEach items="${attachList}" var="list">
  				<script type="text/javascript">
  				addUploadRowByDbView_01('${list.FILENAME}','${list.FILEID}','${list.FILEURL}','${list.FJID}');
	 	 		</script>
  			</c:forEach>
  		 </c:if>
	</table>
<!-- 	<table id="attachTab" class="table_info"> -->
<%--   		<% if(attachList!=null&&attachList.size()!=0){ %> --%>
<%--   		<c:forEach items="${attachList}" var="attls"> --%>
<%-- 		    <tr class="table_list_row1" id="${attls.FJID}"> --%>
<%-- 		    <td><a target="_blank" href="<%=request.getContextPath()%>/util/FileDownLoad/fileDownloadQuery.do?fjid=${attls.FJID}">${attls.FILENAME}</a> --%>
<!-- 		    </td> -->
<%-- 		    <td><input type=button onclick="delAttach('${attls.FJID}')" class="normal_btn"  value="删 除"/></td> --%>
<!-- 		    </tr> -->
<%-- 		</c:forEach> --%>
<%-- 		<%} %> --%>
<!-- 		</table> -->
	    
	  </td>  </tr>
			
			
			   
	      <c:forEach var="dmap" items="${dMap_detail}" varStatus="status">
	      <script>
	      //转化当前状态为js状态，用于判断上报时查看上一条数据是否已经保存或者上报
	      var json={
	    		  "92861000":1, //可保存，提交
	    		  "92861001":2, //不可保存，可提交
	    		  "92861002":3,
	    		  "92861003":1,
	    		  "92861004":2
	      }
	      //将后台数据状态转换0,1,2   新增，保存，上报
	      var index="${status.index}";
	      var value=json["${dmap.STATUS}"];
	      statusArray[index]=value;
	      </script>
		      <tr id="zcxs">
				<td width="100%" colspan=4>
				<table  width=100% border="0" align="center" id="table_${status.index}" cellpadding="1" cellspacing="1" year="第${status.index+1}年">
				   <input type="hidden" value="${status.index+1}" name="yearStatus"/>
				   <input type="hidden" value="${dmap.DEALER_ID}" name="DEALER_ID" id="DEALER_ID_${status.index}"/>
			       <input type="hidden" value="${dmap.CREATE_BY}" name="CREATE_BY" id="CREATE_BY_${status.index}"/>
			       <input type="hidden" value="${dmap.CREATE_DATE}" name="CREATE_DATE" id="CREATE_DATE_${status.index}"/>
			       <input type="hidden" value="${dmap.STATUS}" name="STATUS" id="STATUS_${status.index}"/>
			       <input type="hidden" value="${dmap.DETAIL_ID}" name="DETAIL_ID" id="DETAIL_ID_${status.index}"/>
			       <input type="hidden" value="${dmap.USER_ID}" name="USER_ID" id="USER_ID_${status.index}"/>
			       <input type="hidden" value="${dmap.YEAR_FLAG}"name="YEAR_FLAG" id="YEAR_FLAG_${status.index}"/>
				   <input type="hidden" value="${dmap.CHECKED_IMAGE_LEVEL}" name="CHECKED_IMAGE_LEVEL" id="CHECKED_IMAGE_LEVEL_${status.index}"/>
				
				<tr>
				<th colSpan=4 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif"> <span id="up_name_00">第 &nbsp;${status.index+1} &nbsp;年   <c:if test="${status.index==0}"> <input name="addYear" type="button" class="normal_btn" value="增加" id="addYear" /></c:if> &nbsp; </span></th>
				</tr>
				<c:if test="${status.index==0}">
	             <tr >
	                <td width="10%" align="right">建设日期：</td>
	                <td width="22%" align="left">
	                <input cn_name="建设日期" class="time_txt" readonly="readonly" id="CONSTRUCT_SDATE_${status.index}" name="CONSTRUCT_SDATE"
	                                             type="text"     onclick="calendar();"  datatype="1,is_date,10" maxlength="10" value="<fmt:formatDate value="${dMap_main.CONSTRUCT_SDATE}" pattern="yyyy-MM-dd"/>" style="width:65px"
	                                                    group="CONSTRUCT_SDATE,CONSTRUCT_EDATE"/>
	                    至
	                    <input cn_name="建设日期" class="time_txt" readonly="readonly" id="CONSTRUCT_EDATE_${status.index}" onclick="calendar();" name="CONSTRUCT_EDATE" datatype="1,is_date,10" value="<fmt:formatDate value="${dMap_main.CONSTRUCT_EDATE}"  pattern="yyyy-MM-dd"/>" style="width:65px"
	                          type="text"  maxlength="10" group="CONSTRUCT_SDATE,CONSTRUCT_EDATE"/>
	                </td>
	            
	                <td width="10%" align="right">实地验收时间：</td>  
				    <td width="22%" align="left">
	                <input class="time_txt" cn_name="实地验收时间" readonly="readonly" id="ACCEPT_DATE_${status.index}" name="ACCEPT_DATE" 
	                    type="text"  onclick="calendar();"  datatype="1,is_date,10" maxlength="10" value="<fmt:formatDate value="${dMap_main.ACCEPT_DATE}" pattern="yyyy-MM-dd"/>" style="width:65px" /></td>
	             </tr>
          </c:if>

             <tr >
               <td align="right">年租金：</td>
               <td align="left"><input cn_name="年租金" id="YEAR_RENT_${status.index}" name="YEAR_RENT" onchange="onchangeVlidateSaleQty(this)" value="${dmap.YEAR_RENT}"class=""  type="text"/></td>
               <td align="right">比例：</td>
               <td align="left"><input cn_name="比例" id="SCALE_${status.index}" name="SCALE" onchange="onchangeVlidateSaleQty1(this)" value="${dmap.SCALE}"class=""  type="text"/>%</td>
             </tr>
                 	  <tr>
                 	   <td width="10%" align="right">支持月度：</td>
                <td width="22%" align="left">
                <input class="time_txt" cn_name="支持月度" readonly="readonly" id="SUPPORT_SDATE_${status.index}" name="SUPPORT_SDATE" 
                                              type="text"  onclick="calendar();"      datatype="1,is_date,10" maxlength="10" value="<fmt:formatDate value="${dmap.SUPPORT_SDATE}" pattern="yyyy-MM-dd"/>" style="width:65px"
                                                    group="SUPPORT_SDATE,SUPPORT_EDATE"/>
                    至
                    <input class="time_txt" cn_name="支持月度" readonly="readonly" id="SUPPORT_EDATE_${status.index}" name="SUPPORT_EDATE" datatype="1,is_date,10" value="<fmt:formatDate value="${dmap.SUPPORT_EDATE}"pattern="yyyy-MM-dd"/>" style="width:65px"
                        onclick="calendar();"    type="text"  maxlength="10" group="SUPPORT_SDATE,SUPPORT_EDATE"/>
                </td>
			  
             <td align="right">提车支持款：</td>
               <td align="left">
                	<select id="AMOUNT" name="AMOUNT">
		        		<option value="">-- 请选择 --</option>
		        		<c:forEach items="${amountList}" var="list">
		        		<c:if test="${list.DEPLOY_ID == dmap.DEPLOY_ID}">
		        			<option value="${list.DEPLOY_ID}" SELECTED>${list.POLICY_NAME}</option>
		        		</c:if>
		        		<c:if test="${list.DEPLOY_ID != dmap.DEPLOY_ID}">
		        			<option value="${list.DEPLOY_ID}">${list.POLICY_NAME}</option>
		        		</c:if>
		        		</c:forEach>
		        	</select>
		       </td>
		 </tr>
		 
		 
		 <c:if test="${status.index==0}">
		 <tr>
		 <td align="right">VI形象验收日期：</td>
             <td align="left">
             	<input name="VI_CONFIRM_DATE"  cn_name="VI形象验收日期"  type="text" class="short_time_txt"  id="VI_CONFRIM_DATE_${status.index}"  onclick="calendar();" readonly="readonly"  value="<fmt:formatDate value="${dMap_main.CHECKED_DATE}" pattern="yyyy-MM-dd"/>"/>
             </td>
           
             <td align="right">申请形象等级：</td>
             <td align="left">
             	<script type="text/javascript">
					genSelBoxExp("IMAGE_LEVEL",<%=Constant.IMAGE_LEVEL%>,"${dMap_main.IMAGE_LEVEL}",true,"short_sel",'',"false",'80111005');
				</script>
             </td>
             </tr>
             <tr>
             <td align="right">验收形象等级：</td>
             <td align="left">
             	<script type="text/javascript">
					genSelBoxExp("IMAGE_COMFIRM_LEVEL",<%=Constant.IMAGE_LEVEL%>,"${dMap_main.CHECKED_IMAGE_LEVEL}",true,"short_sel",'',"false",'80111005');
				</script>
             </td>
             <td align="right">验收形象店形式：</td>
             <td align="left">
             	<script type="text/javascript">
					genSelBoxExp("VI_CONSTRUCT_TYPE",<%=Constant.VI_CONSTRUCT_TYPE%>,"${dMap_main.CHECKED_TYPE}",false,"short_sel",'onchange="onchangeViConstructType(this.value)"',"false",'');
				</script>
             </td>             
		 	</tr>

                <tr>
                </c:if>
            
            <c:if test="${status.index!=0}">
            	<tr>
            		<td align="right">增加台数：</td>
		 			<td>
		 				<input type="text" value="${dmap.SUPPORT_NUMBER}" name="SUPPORT_NUMBER" id="SUPPORT_NUMBER" cn_name="增加台数" onchange="onchangeVlidateSaleQty1(this)" class="" />
		 			</td>
            	</tr>   
            </c:if>
            <tr>
             <td align="right">状态：</td>
             <td align="left">
             	<span id="hStatus">${dmap.H_STATE}</span>
             </td>
             <td align="right">车系：</td>
             <td align="left">
             	${dmap.GROUP_NAME}
             </td>
		 	</tr>   
                
        <td align="right">备注：</td>
        <td colspan="5"><textarea name="REMARK" id="textarea"  cols="80" rows="4"
                                   class="middle_txt">${dmap.REMARK}</textarea>
            <font style="color: red"></font>
        </td>
    </tr>   
		 <tr align="center">
				<td colspan="4" class="table_query_4Col_input" style="text-align: center">
					
						
						
					<c:choose>
						<c:when test="${dmap.STATUS==92861000 || dmap.STATUS==92861003}">  <!-- 保存或者驳回 -->
							<input name="_saveBtn" type="button" class="normal_btn" onclick="saveViDetail(${status.index})" value="保存" id="_saveBtn" /> &nbsp; 
							<input name="_addBtn" type="button" class="normal_btn" onclick="addViDetail(${status.index})" value="上报" id="_addBtn" /> &nbsp; 
							<input type="button" class="normal_btn"  value="终止"  disabled="disabled"/> &nbsp; 
						</c:when>
						<c:when test="${dmap.STATUS==92861002}"><!-- 通过或者上报 -->
							<c:if test="${status.index==0}">
								<input name="_saveBtn" type="button" class="normal_btn" value="保存" id="_saveBtn" onclick="saveViDetailForAfterSubmit()"/> &nbsp; 
							</c:if>
							<c:if test="${status.index!=0}">
								<input name="_saveBtn" type="button" class="normal_btn" value="保存" id="_saveBtn"/> &nbsp; 
							</c:if>
							<input name="_addBtn" type="button" class="normal_btn"  disabled="disabled" value="上报" id="_addBtn" /> &nbsp; 
							<input type="button" class="normal_btn"  value="终止" onclick="stopVi(${status.index+1},${dealerInfo.dealerId },${dmap.DETAIL_ID })" /> &nbsp; 
						</c:when>
						<c:when test="${dmap.STATUS==92861001}"><!-- 通过或者上报 -->
							<input name="_saveBtn" type="button" class="normal_btn"  disabled="disabled" value="保存" id="_saveBtn" /> &nbsp; 
							<input name="_addBtn" type="button" class="normal_btn"  disabled="disabled" value="上报" id="_addBtn" /> &nbsp; 
							<input type="button" class="normal_btn"  value="终止"  disabled="disabled" /> &nbsp; 
						</c:when>
						
						<c:otherwise>
							<input name="_saveBtn" type="button" class="normal_btn"  value="保存" id="_saveBtn" disabled="disabled" /> &nbsp; 
							<input name="_addBtn" type="button" class="normal_btn"   value="上报" id="_addBtn" disabled="disabled" /> &nbsp; 
							<input type="button" class="normal_btn"  value="恢复" onclick="recoveryVi(${status.index+1},${dealerInfo.dealerId },${dmap.DETAIL_ID })"  /> &nbsp; 
						</c:otherwise>
					</c:choose>
					
				</td>
		  </tr>
			</table>
			</td>
    	  <tr>
    	  </div>
    	  </c:forEach>
    	  
    	   <tr id="kpxx" >
			<td width="100%" colspan=4>
			<div id="div_2">
			<table style="display: none;"  id="tab1" width=100% border="0" align="center" cellpadding="1" cellspacing="1"> 
			<input type="hidden" name="DETAIL_ID" id="detail_id_hid" value="" />
	      <tr>
			<th colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif"> 第二年</th>
    	  <tr>
    	  <tr>
			    <td width="10%" align="right">支持月度：</td>
                <td width="22%" align="left">
                  
       <input class="time_txt" cn_name="支持月度开始时间" readonly="readonly" onclick="calendar();"  id="SUPPORT_SDATE" name="SUPPORT_SDATE"
                                                    datatype="1,is_date,10" maxlength="10"  style="width:65px"
                                                    group="SUPPORT_SDATE,SUPPORT_EDATE"/>
                    至
                    <input type="text" cn_name="支持月度结束时间" class="time_txt" readonly="readonly" id="SUPPORT_EDATE" name="SUPPORT_EDATE" datatype="1,is_date,10" onclick="calendar();"   style="width:65px"
                           maxlength="10" group="SUPPORT_SDATE,SUPPORT_EDATE"/>
                </td>
			   <td align="right">年租金：</td>
               <td align="left"><input id="YEAR_RENT" cn_name="年租金" type="text" name="YEAR_RENT" onchange="onchangeVlidateSaleQty(this)" value="${dMap.YEAR_RENT}" class=""  /></td>
		 </tr>
		 <tr>
			   <td align="right">比例：</td>
               <td align="left"><input id="SCALE" cn_name="比例" type="text" name="SCALE" onchange="onchangeVlidateSaleQty1(this)" value="${dMap.SCALE}" class=""  />%</td>
			   <td align="right">提车支持款：</td>
               <td align="left">
                	<select id="AMOUNT" name="AMOUNT">
		        		<option value="">-- 请选择 --</option>
		        		<c:forEach items="${amountList}" var="list">
		        			<option value="${list.DEPLOY_ID}">${list.POLICY_NAME}</option>
		        		</c:forEach>
		        	</select>
		       </td>
		 </tr>
		 <tr>
		 	<td align="right">增加台数：</td>
		 	<td><input type="text" name="SUPPORT_NUMBER" id="SUPPORT_NUMBER" cn_name="增加台数" onchange="onchangeVlidateSaleQty1(this)" value="${dMap.SUPPORT_NUMBER}" class=""></input></td>
		 </tr>
		 <tr>
		 	<td align="right">
		 		状态：
		 	</td>
		 	<td>
		 		未保存
		 	</td>
		 	<td align="right">
		 		车系：
		 	</td>
		 	<td>
		 		MPV
		 	</td>
		 </tr>
                <tr>
        <td align="right">备注：</td>
        <td colspan="5"><textarea name="REMARK" id="textarea"  cols="80" rows="4"
                                   class="middle_txt">${dMap.REMARK}</textarea>
            <font style="color: red"></font>
        </td>
    </tr>   
		  <tr align="center">
				<td colspan="4" class="table_query_4Col_input" style="text-align: center">
					<input name="_saveBtn" type="button" class="normal_btn" onclick="saveViDetail()" value="保存" id="_saveBtn" /> &nbsp; 
					<input name="_addBtn" type="button" class="normal_btn" onclick="addViDetail()" value="上报" id="_addBtn" /> &nbsp; 
				</td>
		  </tr>
		 
		 </table>
		 </div>
		 <div id="year_div"></div>
		 	<table id="year1" width=100% border="0"  align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <tr>
			<TH colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif"> 操作</TH>
    	  </tr>
		 	<tr  width=100% border="0" align="center" cellpadding="1" cellspacing="1">
				<td colspan="4" class="table_query_4Col_input" style="text-align: center">
		 		<input name="_saveBtn" type="button" class="normal_btn" onclick="saveVi()" value="保存" id="_saveBtnAll" /> &nbsp; 
				<input name="_addBtn" type="button" class="normal_btn" onclick="addVi()" value="上报" id="_addBtnAll" /> &nbsp; 
		 		<input name="_addBtn" type="button" class="normal_btn" onclick="goBack()" value="返回" id="_addBtn" /> &nbsp; 
		 		</td>
		 	</table>
		 </form>
		 <form action="" method="post" id="fm1" name="fm1">
				   <input id="DEALER_ID_n" name="DEALER_ID1" value="${dealerInfo.dealerId}" type="hidden"/></td>
			       <input type="hidden"  name="CREATE_BY1" id="CREATE_BY_n"value=""/>
			       <input type="hidden"  name="CREATE_DATE1" id="CREATE_DATE_n"value=""/>
			       <input type="hidden"  name="STATUS1" id="STATUS_n"value=""/>
			       <input type="hidden"  name="USER_ID1" id="USER_ID_n" value=""/>
			       <input type="hidden" name="YEAR_FLAG1" id="YEAR_FLAG_n" value=""/>
				   <input type="hidden"  name="ACCEPT_DATE1" id="ACCEPT_DATE_n" value=""/>
				   <input type="hidden"  name="IMAGE_LEVEL1" id="IMAGE_LEVEL_n" value=""/>
				   <input type="hidden"  name="CHECKED_IMAGE_LEVEL1" id="CHECKED_IMAGE_LEVEL_n" value=""/>
				   <input type="hidden"  name="CHECKED_DATE1" id="CHECKED_DATE_n" value=""/>
				   <input type="hidden"  name="CONSTRUCT_SDATE1" id="CONSTRUCT_SDATE_n" value=""/>
			       <input type="hidden"  name="CONSTRUCT_EDATE1" id="CONSTRUCT_EDATE_n" value=""/>
				   <input type="hidden"  name="YEAR_RENT1" id="YEAR_RENT_n" value=""/>
				   <input type="hidden"  name="SCALE1" id="SCALE_n" value=""/>
				   <input type="hidden"  name="SUPPORT_SDATE1" id="SUPPORT_SDATE_n"value=""/>
				   <input type="hidden"  name="SUPPORT_EDATE1" id="SUPPORT_EDATE_n"value=""/>
				   <input type="hidden"  name="VI_CONFRIM_DATE1" id="VI_CONFRIM_DATE_n"value=""/>
				   <input type="hidden"  name="IMAGE_COMFIRM_LEVEL1" id="IMAGE_COMFIRM_LEVEL_n"value=""/>
				   <input type="hidden"  name="AMOUNT1" id="AMOUNT_n" value=""/>
				   <input type="hidden"  name="REMARK1" id="REMARK_n"value=""/>
				   <input type="hidden"  name="ACCEPT_DATE1" id="ACCEPT_DATE_n"value=""/>
				   <input type="hidden"  name="DEPLOY_ID1" id="DEPLOY_ID_n" value=""/>
				   <input type="hidden"  name="PAGE_TYPE"  value="2"/>
				   <input type="hidden"  name="ID1" id="idSingle"  value="${dMap_detail[0].ID}"/>
				  <input type="hidden"  name="SIZE"  value="" id="SIZE"/>
				  <input type="hidden"  name="VI_CONFIRM_DATE1" id="VI_CONFIRM_DATE_n"  value=""/>
					<input type="hidden"  name="BUTTON_TYPE" id="BUTTON_TYPE1" value=""/>
					<input type="hidden"  name="VI_CONSTRUCT_TYPE1" id="VI_CONSTRUCT_TYPE_n" value=""/>
					<input type="hidden"  name="fjid1" id="fjid_n" value=""/>
				   <input type="hidden"  name="offline_rebate1" id="offline_rebate_n" value=""/>
				   <input type="hidden"  name="DETAIL_ID" id="DETAIL_ID_n" value=""/>
				    <input type="hidden"  name="SUPPORT_NUMBER" id="SUPPORT_NUMBER_n" value=""/>
				   <input type="hidden"  name="vehicle_series_flag" id="vehicle_series_flag" value="2015011508783002"/>
		 </form>
		 
		 
</body>
<script type="text/javascript">
function delUploadFile(obj){
	var idx = obj.parentElement.parentElement.rowIndex;
	var tbl = document.getElementById('fileUploadTab');
	tbl.deleteRow(idx);
}
function setTableDisable(){

	var flag=true;
	for (var j = 0; j < statusArray.length; j++) {
		if(statusArray[j]!=null&&statusArray[j]==2||statusArray[j]==3){
			var table=document.getElementById("table_"+j);
			var inputs=table.getElementsByTagName("input");
			var selects=table.getElementsByTagName("select");
			var textarea=table.getElementsByTagName("textarea");
			for(var i=0;i<inputs.length;i++){
				if(inputs[i].value!="增加"&&inputs[i].value!="终止"&&inputs[i].value!="恢复"){
					inputs[i].disabled="disabled";
				}

				if (j == 0 && statusArray[j]==3 && inputs[i].value=="保存") {
					inputs[i].disabled="";
				}

				if (j != 0 && statusArray[j]==2 && inputs[i].id=="SUPPORT_NUMBER") {
					inputs[i].disabled="";
				}
			}
			for(var i=0;i<selects.length;i++){
				selects[i].disabled="disabled";
				if (statusArray[j]==3 && selects[i].name=="IMAGE_COMFIRM_LEVEL") {
					selects[i].disabled="";
				}
			}
			textarea[0].disabled = "disabled";
		};
	};
	//判断总按钮是否可用
	checkAll:for (var j = 0; j < statusArray.length; j++) {
		if(statusArray[j]!=null&&statusArray[j]!=2&&statusArray[j]!=3){
			flag=false;
			break checkAll;
		}
	}
	if(flag){
		document.getElementById("_saveBtnAll").disabled="disabled";
		document.getElementById("_addBtnAll").disabled="disabled";
	}else{
		document.getElementById("_saveBtnAll").removeAttribute("disabled");
		document.getElementById("_addBtnAll").removeAttribute("disabled");
	}
}
</script>
</html>


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
var yearArray=new Array("第二年","第三年","第四年","第五年","第六年","第七年","第八年","第九年","第十年","第十一年","第十二年","第十三年","第十四年","第十五年","第十六年","第十七年","第十八年","第十九年","第二十年","第二十一年","第二十二年","第二十三年","第二十四年"); 
var  i3="";
var yearId=1;
var saveFlag=0;
var statusArray=new Array();
statusArray.push(0);
//上传后台隐藏域
var yearstatus=2;

//添加年份 
function addYear(){
	statusArray.push(0);
	var ih=document.getElementById("tab1").innerHTML;
	
	var CHAyear=yearArray[yearId-1];
	var yearStatusValue=' <input type="hidden" name="yearStatus" value="'+yearstatus+'"/>';
	
	ih='<table id="table_'+yearId+'" year="'+CHAyear+'"  width=100% border="0" align="center" cellpadding="1" cellspacing="1" id="sec_year"> '+ih+yearStatusValue+'<table>';
	
	if(ih.indexOf("第")>0){
		//替换元素
		var i1=ih.substring(0, ih.indexOf("第二年"));
		var i2=ih.substring(ih.indexOf("第二年")+3,ih.length);
		i3=i1+CHAyear+i2;
	}
	if(i3.indexOf("saveViDetail()")>0){
		i3=i3.replace("saveViDetail()","saveViDetail("+yearId+",this)");
	}if(i3.indexOf("addViDetail()")>0){
		i3=i3.replace("addViDetail()","addViDetail("+yearId+",this)");
	}
	
	var div_1=document.getElementById("year_div");
	div_1.innerHTML=div_1.innerHTML+i3;	
	yearId++;
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
	 					}if(name=="SUPPORT_SDATE"){
	 						SUPPORT_SDATE=inputVal;
	 					}if(name=="SUPPORT_EDATE"){
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
	document.getElementById("checkBtn").onclick=function(){
		
		checkNull();
	}
	loadcalendar();
	var status = document.getElementById("STATUS").value;
	if(status==92861001||status==92861002||status==92861003||status==92861004){
		document.getElementById("_saveBtn").disabled=true;
		document.getElementById("_addBtn").disabled=true;
	}else if(status==92861000){
		document.getElementById("_saveBtn").disabled=false;
		document.getElementById("_addBtn").disabled=false;
// 		document.getElementById("_stopBtn").disabled=true;
	}else if(status==92861006||status==92861007){
		document.getElementById("_saveBtn").disabled=true;
		document.getElementById("_addBtn").disabled=true;
		document.getElementById("_stopBtn").disabled=true;
	}else if(status==92861005){
		document.getElementById("_saveBtn").disabled=true;
	}else{
		document.getElementById("_stopBtn").disabled=true;
	}
}   
function onchangeViConstructType(value) {
	var table = document.getElementById("table_0");  
	if (value == 20021002) {
		var tr = table.rows[6];
		var td2 = tr.insertCell(2);
		td2.innerHTML = "线下已返(元)：";
		td2.setAttribute("align","right");
		var td3 = tr.insertCell(3);
		td3.colSpan = 5;
		td3.setAttribute("align","left");
		td3.innerHTML = "<input id='offline_rebate' name='offline_rebate' cn_name='线下返利' value='' datatype='0,isMoney,10' class='middle_txt'  type='text'/>";
	} else {
		table.rows[6].deleteCell(3);
		table.rows[6].deleteCell(2);
	}
}
function onchangeVlidateSaleQty(obj) {
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
function addVi(btn)
{
	btn.disabled=true;
	var fjid = document.getElementsByName("fjid");
// 	if(fjid.length==0){
// 		MyAlert("请添加附件");
// 		return false;
// 	}
	document.getElementById("BUTTON_TYPE").value="2";
	var flag=checkNull();
	if(flag){
		this.disabled;
		deleteNullTable();
		var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/saveVi.do";
		fm.action = url;
	 	fm.submit();
	 	indexFlag++;
	}
	
	
}  
//去除jsp模板
function deleteNullTable(){
	var div_2=document.getElementById("div_2");
	div_2.innerHTML="";
}
function saveVi(btn)
{   
	btn.disabled=true;
	var fjid = document.getElementsByName("fjid");
// 	if(fjid.length==0){
// 		MyAlert("请添加附件");
// 		return false;
// 	}
	document.getElementById("BUTTON_TYPE").value="1";
	var flag=checkNull();
	if(flag){
		this.disabled;
		deleteNullTable();
		var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/saveVi.do";
		fm.action = url;
	 	fm.submit();
	 	indexFlag++;
	}
}  
function stopVi()
{
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/stopVi.do";
	fm.action = url;
 	fm.submit();
}  


function selectOther(selectOther) {
	OpenHtmlWindow(g_webAppName + '/sales/financemanage/SalesPolicyManager/queryDeployedPolicyInit.do?id='+selectOther,700,500);
}
function otherConfirm(id, ids, otherAmount, minDate, maxDate) {
	document.getElementById(id).value = otherAmount;
	_hide();
}
//单条提交
function saveViDetail(index,btn){
	
	var fjid = document.getElementsByName("fjid");
// 	if(fjid.length==0){
// 		MyAlert("请添加附件");
// 		return false;
// 	}
	var fjids = '';
	for (var int = 0; int < fjid.length; int++) {
		if(int>0){
			fjids = fjids +","+fjid[int].value;
		}else{
			fjids = fjid[int].value;
		}
	}
	document.getElementById("fjid_n").value=fjids;
	document.getElementById("BUTTON_TYPE1").value="1";
	//第二年开始校验，前一年没有上报，该年不能上报
	if(statusArray.length>1){
		var lastStatus=statusArray[index-1];
		//上条数据必须上报或者保存
		if(lastStatus==0){
			MyAlert("请先保存上一年数据");
			return false;
		}
	}
	document.getElementById("SIZE").value=index+1;
	var table=document.getElementById("table_"+index);
	var inputs=table.getElementsByTagName("input");
	var selects=table.getElementsByTagName("select");
	var textarea=table.getElementsByTagName("textarea");
	
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
// 		MyAlert(name+"--"+value);
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
		MyAlert("建设时间 :结束时间不能小于开始时间");
		return false;
	}
	if(dateCompare(SUPPORT_SDATE,SUPPORT_EDATE)){
		MyAlert("支持月度 :结束时间不能小于开始时间");
		return false;
	}
	
	var url="<%=contextPath%>/sysmng/dealer/DealerInfo/singleSaveVi.json";
	btn.disabled=true;
	sendAjax(url,function(json){
		if (json.Exception) {
			MyAlert(json.Exception.message);
		} else {
			document.getElementById("myName").value=json.result;
			document.getElementById("idAll").value=json.result;
			table.getElementsByTagName("input").namedItem("DETAIL_ID").setAttribute("value",json.DETAIL_ID);
			MyAlert("保存成功");
			statusArray[index]=1;	
			btn.disabled=false;
			var tr = table.rows[table.rows.length - 3];
			tr.deleteCell(1);
			var td1 = tr.insertCell(1);
			td1.innerHTML="<span>已保存</span>";
		}
	},'fm1');
} 

function addViDetail(index,btn){
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
	//第二年开始校验，前一年没有上报，该年不能上报
	document.getElementById("BUTTON_TYPE1").value="2";
	if(index>0){
		var lastStatus=statusArray[index-1];
		if(lastStatus!=2){
			MyAlert("请先上报上一年数据");
			return false;
		}
	}
	document.getElementById("SIZE").value=index+1;
	var table=document.getElementById("table_"+index);
	var inputs=table.getElementsByTagName("input");
	var selects=table.getElementsByTagName("select");
	var textarea=table.getElementsByTagName("textarea");

	var stime;
	var etime;
	var SUPPORT_SDATE;
	var SUPPORT_EDATE;
	var VI_CONSTRUCT_TYPE;
	
	for(var i=0;i<inputs.length;i++){
		var input=inputs[i];
		var name=input.name;
		var inputVal=input.value;
		var cn_name=input.cn_name;
		
		if(name=="CONSTRUCT_SDATE"){
			stime=inputVal;
		}if(name=="CONSTRUCT_EDATE"){
			etime=inputVal;
		}if(name=="SUPPORT_SDATE"){
			SUPPORT_SDATE=inputVal;
		}if(name=="SUPPORT_EDATE"){
			SUPPORT_EDATE=inputVal;
		}if(name=="VI_CONSTRUCT_TYPE"){
			VI_CONSTRUCT_TYPE=inputVal;
		}
		
	//	MyAlert(cn_name+"--"+name+"--"+value);
		var dom=document.getElementById(name+"_n");
		if(inputVal==""&&cn_name){
			MyAlert(cn_name+"不能为空");
			return false;
		}
		if(dom){
			dom.value=inputVal;
		}
	}
	for(var i=0;i<selects.length;i++){
		var select=selects[i];
		var name=select.name;
		
		var value=select.value;
// 		MyAlert(name+"--"+value);
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
	
	
	var url="<%=contextPath%>/sysmng/dealer/DealerInfo/singleSaveVi.json";
	
	btn.disabled="true";
	sendAjax(url,function(json){
		if (json.Exception) {
			MyAlert(json.Exception.message);
		} else {
			document.getElementById("myName").value=json.result;
			document.getElementById("idAll").value=json.result;
			table.getElementsByTagName("input").namedItem("DETAIL_ID").setAttribute("value",json.DETAIL_ID);
			MyAlert("上报成功");
			for(var i=0;i<inputs.length;i++){
			//	if(inputs[i].value!="增加"||inputs[i].value!="保存"||inputs[i].value!="上报"||inputs[i].value!="终止"){
				if(inputs[i].value!="增加"){
					inputs[i].disabled="disabled";
				}
// 				if(inputs[i].name=="yearStatus"){
// 					inputs[i].name="upload";
// 				}
			}
			for(var i=0;i<selects.length;i++){
				selects[i].disabled="disabled";
			}
			textarea[0].disabled = "disabled";
			statusArray[index]=2;
			// 状态变更
			var tr = table.rows[table.rows.length - 3];
			tr.deleteCell(1);
			var td1 = tr.insertCell(1);
			td1.innerHTML="<span>销售部审核中(已上报)</span>";
		}
	},'fm1');
	
	
	
} 


function goBack(){
	window.location.href="<%=contextPath%>/sysmng/dealer/DealerInfo/viConstructQryInit.do";
}




</script>
</head>
<body>
<form method="post" id="fm" name="fm">
<input name="ID1" id="idAll" type="hidden" value="" /> 
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 财务管理&gt; 财务管理 &gt;形象店支持维护&gt;新增建店支持</div>
<input id="BUTTON_TYPE" name="BUTTON_TYPE" value="" type="hidden"/>
<input id="STATUS" name="STATUS" value="${dMap.STATUS }" type="hidden"/>
<input type="hidden"  name="vehicle_series_flag1" id="vehicle_series_flag1" value="2015011508783002"/>
  
 
 <table id="year1" width=100% border="0"  align="center" cellpadding="1" cellspacing="1" class="table_query" >
    	  <tr>
			<TH colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif"> 经销商基础信息</TH>
    	  </tr>
		  
		  <tr>
		  
		  <input type="hidden"  name="PAGE_TYPE"  value="1"/>
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
				    		addUploadRowByDbView('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
				    	</script>
				<%}}%>
			</table> 
  		<!-- 添加附件 结束 -->
	    
	  </td>  </tr>
	      
	      <tr id="zcxs">
			<td width="100%" colspan=4>
			<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1" id="table_0" year="第一年">
			<input type="hidden" name="yearStatus" value="1" />
			<input type="hidden" name="DETAIL_ID" value="" id="DETAIL_ID"/>
			<tr>
			<th colSpan=4 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif"> <span id="up_name_00">第一年 <input name="addYear" type="button" class="normal_btn" value="增加" id="addYear" /> &nbsp; </span></th>
			</tr>
             <tr >
                <td width="10%" align="right">建设日期：</td>
                <td width="22%" align="left">
                <input cn_name="建设时间" type="text" class="time_txt" readonly="readonly" id="CONSTRUCT_SDATE" name="CONSTRUCT_SDATE"
                                                  onclick="calendar();"  datatype="1,is_date,10" maxlength="10"  style="width:65px"  />
                    至
                    <input cn_name="建设时间" type="text"  class="time_txt" readonly="readonly" id="CONSTRUCT_EDATE" onclick="calendar();" name="CONSTRUCT_EDATE" datatype="1,is_date,10" value="" style="width:65px"
                           maxlength="10" group="CONSTRUCT_SDATE,CONSTRUCT_EDATE"/>
                </td>
                <td width="10%" align="right">支持月度：</td>
                <td width="22%" align="left">
                <input cn_name="支持月度开始时间" type="text"  class="time_txt" readonly="readonly" id="SUPPORT_SDATE" name="SUPPORT_SDATE" 
                                              onclick="calendar();"      datatype="1,is_date,10" maxlength="10"  style="width:65px"
                                                    group="SUPPORT_SDATE,SUPPORT_EDATE"/>
                    至
                    <input cn_name="支持月度结束时间" type="text" class="time_txt" readonly="readonly" id="SUPPORT_EDATE" name="SUPPORT_EDATE" datatype="1,is_date,10" value="${dMap.SUPPORT_EDATE}" style="width:65px"
                        onclick="calendar();"   maxlength="10" group="SUPPORT_SDATE,SUPPORT_EDATE"/>
                </td>
             </tr>
             <tr >
               <td align="right">年租金：</td>
               <td align="left"><input id="YEAR_RENT" cn_name="年租金" name="YEAR_RENT" onchange="onchangeVlidateSaleQty(this)" value="${dMap.YEAR_RENT}"class=""  type="text"/></td>
               <td align="right">比例：</td>
               <td align="left"><input id="SCALE" name="SCALE" cn_name="比例" onchange="onchangeVlidateSaleQty1(this)" value="${dMap.SCALE}"class=""  type="text"/>%</td>
             </tr>
                 	  <tr>
			   <td width="10%" align="right">实地验收时间：</td>
			    <td width="22%" align="left">
                <input cn_name="实地验收时间" type="text" class="time_txt" readonly="readonly" id="ACCEPT_DATE" name="ACCEPT_DATE"
                                                  onclick="calendar();"  datatype="1,is_date,10" maxlength="10" value="${dMap.ACCEPT_DATE}" style="width:65px"
                                                    /></td>
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
		 <td align="right">VI形象验收日期：</td>
             <td align="left">
             	<input name="VI_CONFIRM_DATE" cn_name="VI形象验收日期" type="text"  type="text" class="short_time_txt" id="VI_CONFIRM_DATE" onclick="calendar();" readonly="readonly" value=""/>
             </td>
           
           
             <td align="right">申请形象等级：</td>
             <td align="left">
             	<script type="text/javascript">
					genSelBoxExp("IMAGE_LEVEL",<%=Constant.IMAGE_LEVEL%>,"${dMap.IMAGE_LEVEL}",true,"short_sel",'',"false",'80111005');
				</script>
             </td>
             </tr>
             <tr>
             <td align="right">验收形象等级：</td>
             <td align="left">
             	<script type="text/javascript">
					genSelBoxExp("IMAGE_COMFIRM_LEVEL",<%=Constant.IMAGE_LEVEL%>,"${dMap.IMAGE_COMFIRM_LEVEL}",true,"short_sel",'',"false",'80111005');
				</script>
             </td>
             <td align="right">验收形象店形式：</td>
             <td align="left">
             	<script type="text/javascript">
					genSelBoxExp("VI_CONSTRUCT_TYPE",<%=Constant.VI_CONSTRUCT_TYPE%>,"${dMap.VI_CONSTRUCT_TYPE}",false,"short_sel","onchange='onchangeViConstructType(this.value)'","false",'');
				</script>
             </td>
		 	</tr>
		 	<tr>
		 	<td align="right">状态：</td>
	             <td align="left">
	             	<span id="hStatus">未保存</span>
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
                <input name="_saveBtn" type="button" class="normal_btn" onclick="saveViDetail(0,this)" value="保存" id="_saveBtn" /> &nbsp; 
				<input name="_addBtn" type="button" class="normal_btn" onclick="addViDetail(0,this)" value="上报" id="_addBtn" /> &nbsp; 
        </td>
        </tr>
			</table>
			</td>
    	  <tr>

    	   <tr id="kpxx" >
			<td width="100%" colspan=4>
			<div id="div_2">
			<table style="display: none;"  id="tab1" width=100% border="0" align="center" cellpadding="1" cellspacing="1"> 
	     	<input type="hidden" name="STATUS" value=""/>
	     	<input type="hidden" name="DETAIL_ID" value="" id="detail_id"/>
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
               <td align="left"><input id="YEAR_RENT" cn_name="年租金"  type="text" name="YEAR_RENT" onchange="onchangeVlidateSaleQty(this)" class=""  /></td>
		 </tr>
		 <tr>
			   <td align="right">比例：</td>
               <td align="left"><input id="SCALE" cn_name="比例" type="text" name="SCALE" onchange="onchangeVlidateSaleQty1(this)" value="${dMap.SCALE}"class=""  />%</td>
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
		 	<td align="right">状态：</td>
	             <td align="left">
	             	<span id="hStatus">未保存</span>
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
		 		<input name="_saveBtn" type="button" class="normal_btn" onclick="saveVi(this)" value="保存" id="_saveBtn" /> &nbsp; 
				<input name="_addBtn" type="button" class="normal_btn" onclick="addVi(this)" value="上报" id="_addBtn" /> &nbsp; 
		 		<input name="_addBtn" type="button" class="normal_btn" onclick="goBack()" value="返回" id="_addBtn" /> &nbsp; 
		 		</td>
		 	</table>
		 </form>
		 
		 <form action="<%=contextPath%>/sysmng/dealer/DealerInfo/singleAddVi.json" method="post" id="fm1" name="fm1">
				   <input type="hidden"  name="ID1" id="myName"  value=""/>
				   <input id="DEALER_ID_n" name="DEALER_ID1" value="${dealerInfo.dealerId}" type="hidden"/></td>
			       <input type="hidden"  name="CREATE_BY1" id="CREATE_BY_n"value=""/>
			       <input type="hidden"  name="CREATE_DATE1" id="CREATE_DATE_n"value=""/>
			       <input type="hidden"  name="STATUS1" id="STATUS_n"value=""/>
			       <input type="hidden"  name="USER_ID1" id="USER_ID_n" value=""/>
			       <input type="hidden"  name="YEAR_FLAG1" id="YEAR_FLAG_n" value=""/>
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
				   <input type="hidden"  name="AMOUNT1" id="AMOUNT_n" value=""/>
				   <input type="hidden"  name="REMARK1" id="REMARK_n"value=""/>
				   <input type="hidden"  name="VI_CONFIRM_DATE1" id="VI_CONFIRM_DATE_n"value=""/>
				   <input type="hidden"  name="DEPLOY_ID1" id="DEPLOY_ID_n" value=""/>
				   <input type="hidden"  name="IMAGE_COMFIRM_LEVEL1" id="IMAGE_COMFIRM_LEVEL_n" value=""/>
				   <input type="hidden"  name="PAGE_TYPE"  value="1"/>
				   <input type="hidden"  name="VI_CONFRIM_DATE1" id="VI_CONFRIM_DATE_n" value=""/>
				   <input type="hidden"  name="SIZE"  value="" id="SIZE"/>
				   <input type="hidden"  name="BUTTON_TYPE" id="BUTTON_TYPE1" value=""/>
				   <input type="hidden"  name="VI_CONSTRUCT_TYPE1" id="VI_CONSTRUCT_TYPE_n" value=""/>
				   <input type="hidden"  name="fjid1" id="fjid_n" value=""/>
				   <input type="hidden"  name="offline_rebate1" id="offline_rebate_n" value=""/>
				   <input type="hidden"  name="vehicle_series_flag" id="vehicle_series_flag" value="2015011508783002"/>
				   <input type="hidden"  name="DETAIL_ID" id="DETAIL_ID_n" value=""/>
		 </form>
</body>
</html>


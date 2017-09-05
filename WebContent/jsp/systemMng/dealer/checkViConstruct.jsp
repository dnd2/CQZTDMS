<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@page import="java.util.Arrays"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%
	String contextPath = request.getContextPath();
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
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<script type="text/javascript">
var g_webAppName = '<%=request.getContextPath()%>';
var dealerLevel='<%=Constant.DEALER_LEVEL_01%>';
var yearArray=new Array("第    2  年","第   3 年","第    4  年","第    5  年","第   6  年","第  7 年","第 8 年","第  9 年","第 10 年","第 11 年","第 12 年","第 13 年","第 14 年","第 15 年","第 16 年","第  17 年","第十八年","第十九年","第二十年"); 
var  i3="";
var yearId=2;
var oldYearId="${size}";



//添加年份 
function addYear(){
	var tab1=document.getElementById("div_2");
	var ih=document.getElementById("tab1").innerHTML;
	var CHAyear=yearArray[oldYearId-1];
	ih='<table id="year'+yearId+'" year="'+CHAyear+'" width=100% border="0" align="center" cellpadding="1" cellspacing="1" id="sec_year"> '+ih+'<table>';
	if(ih.indexOf("第")>0){
		//替换元素
		var i1=ih.substring(0, ih.indexOf("第二年"));
		var i2=ih.substring(ih.indexOf("第二年")+3,ih.length);
		i3=i3+i1+CHAyear+i2;
	}
	var div_1=document.getElementById("year_div");
	div_1.innerHTML=i3;	
	oldYearId++;
}


//判断非空
function checkNull(){
	var tables=document.getElementsByTagName("table");
	//所有table
	for(var n=0;n<tables.length;n++){
		var tableName=tables[n].year;
		if(tableName){
			var inputs = tables[n].getElementsByTagName("input");
	 		var len=inputs.length;
	 		//table中所有input
	 		for(var i=0;i<len;i++){
	 			var input=inputs[i];
	 			var inputVal = input.value;
	 			var cn_name=input.cn_name;
	 			if(input.type=="text"){
	 				if(inputVal==""&&cn_name!="" ){
	 					MyAlert(tableName+":"+cn_name+"不能为空");
	 					return false;
	 				}
	 			}
				
	 		}
		}
	}
}






function doInit()
{
	var body=document.getElementById("body");
	
	document.getElementById("addYear").onclick=function(){
		addYear();
	}
	loadcalendar();

	document.getElementById("_saveBtn").disabled=true;
	document.getElementById("_addBtn").disabled=true;
	document.getElementById("_stopBtn").disabled=true;
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
function addVi()
{
	var flag=checkNull();
	if(flag){
		deleteNullTable();
		var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/addVi.do";
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
	var flag=checkNull();
	if(flag){
		deleteNullTable();
		var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/saveVi.do";
		fm.action = url;
	 	fm.submit();
	}
}  
function stopVi(year)
{
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/stopVi.do?year="+year;
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


function goBack(){
	window.location.href="<%=contextPath%>/sysmng/dealer/DealerInfo/viConstructQryInit.do";
}

</script>
</head>
<body>
<form method="post" id="fm" name="fm">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 财务管理&gt; 财务管理 &gt;形象店支持维护&gt;新增建店支持</div>
 <table id="year1" width=100% border="0"  align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <tr>
			<TH colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif"> 经销商基础信息</TH>
    	  </tr>
		  <tr>
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
<!-- 			<table id="attachTab" class="table_info"> -->
				<%
					if (attachList != null && attachList.size() != 0) {
				%>
				<tr>
					<td width="75%" colspan="6"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
				</tr>
				<c:forEach items="${attachList}" var="attls">
					<tr class="table_list_row1" id="${attls.FJID}">
						<td colspan="3"><a target="_blank" href="<%=request.getContextPath()%>/util/FileDownLoad/fileDownloadQuery.do?fjid=${attls.FJID}">${attls.FILENAME}</a></td>
						<td colspan="2"><input type=button onclick="delAttach('${attls.FJID}')" disabled="disabled" class="normal_btn" value="删 除" /></td>
					</tr>
				</c:forEach>
				<%
					}
				%>
<!-- 			</table> -->
			<c:forEach var="dmap" items="${dMap_detail}" varStatus="status">
			 
		      <tr id="zcxs">
				<td width="100%" colspan=4>
				<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1" year="第${dmap.YEAR_FLAG}年">
				<tr>
				<th colSpan=4 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif"> <span id="up_name_00">第 &nbsp;${dmap.YEAR_FLAG} &nbsp;年    &nbsp; </span></th>
				</tr>
				<c:if test="${dmap.YEAR_FLAG==1}">
	             <tr >
	                <td width="10%" align="right">建设日期：</td>
	                <td width="22%" align="left">
	                <input cn_name="建设日期" class="time_txt" readonly="readonly" id="CONSTRUCT_SDATE" name="CONSTRUCT_SDATE"
	                                                    datatype="1,is_date,10" maxlength="10" value="<fmt:formatDate value="${dmap.CONSTRUCT_SDATE}" pattern="yyyy-MM-dd"/>" style="width:65px"
	                                                    group="CONSTRUCT_SDATE,CONSTRUCT_EDATE"/>
	                    至
	                    <input class="time_txt" readonly="readonly" id="CONSTRUCT_EDATE"  name="CONSTRUCT_EDATE" datatype="1,is_date,10" value="<fmt:formatDate value="${dmap.CONSTRUCT_EDATE}"  pattern="yyyy-MM-dd"/>" style="width:65px"
	                           maxlength="10" group="CONSTRUCT_SDATE,CONSTRUCT_EDATE"/>
	                </td>
	            
	                <td width="10%" align="right">实地验收时间：</td>  
				    <td width="22%" align="left">
	                <input class="time_txt" cn_name="实地验收时间"  id="ACCEPT_DATE" name="ACCEPT_DATE" 
	                      datatype="1,is_date,10" maxlength="10" value="<fmt:formatDate value="${dmap.ACCEPT_DATE}" pattern="yyyy-MM-dd"/>" style="width:65px" /></td>
	             </tr>
          </c:if>

             <tr >
               <td align="right">年租金：</td>
               <td align="left"><input cn_name="年租金" readonly="readonly" id="YEAR_RENT" name="YEAR_RENT"  value="${dmap.YEAR_RENT}"class=""  type="text"/></td>
               <td align="right">比例：</td>
               <td align="left"><input cn_name="比例" readonly="readonly" id="SCALE" name="SCALE" value="${dmap.SCALE}"class=""  type="text"/>%</td>
             </tr>
                 	  <tr>
                 	   <td width="10%" align="right">支持月度：</td>
                <td width="22%" align="left">
                <input class="time_txt" cn_name="支持月度" readonly="readonly" id="SUPPORT_SDATE" name="SUPPORT_SDATE" 
                                                   datatype="1,is_date,10" maxlength="10" value="<fmt:formatDate value="${dmap.SUPPORT_SDATE}" pattern="yyyy-MM-dd"/>" style="width:65px"
                                                    group="SUPPORT_SDATE,SUPPORT_EDATE"/>
                    至
                    <input class="time_txt" cn_name="支持月度" readonly="readonly" id="SUPPORT_EDATE" name="SUPPORT_EDATE" datatype="1,is_date,10" value="<fmt:formatDate value="${dmap.SUPPORT_EDATE}"pattern="yyyy-MM-dd"/>" style="width:65px"
                           maxlength="10" group="SUPPORT_SDATE,SUPPORT_EDATE"/>
                </td>
			  
             <td align="right">提车支持款：</td>
               <td align="left">
               <input value="${dmap.AMOUNT }" readonly="readonly">
		    	</td>
		 </tr>
		 
		 
		 <c:if test="${dmap.YEAR_FLAG==1}">
		 <tr>
		 <td align="right">VI形象验收日期：</td>
             <td align="left">
             	<input name="VI_CONFRIM_DATE" readonly="readonly" cn_name="VI形象验收日期"  type="text"  class="short_time_txt" id="VI_CONFRIM_DATE"  onclick="calendar();" readonly="readonly"  value="<fmt:formatDate value="${dmap.CHECKED_DATE}" pattern="yyyy-MM-dd"/>"/>
             </td>
           
             <td align="right">申请形象等级：</td>
             <td align="left">
             	<script type="text/javascript">
					genSelBoxExp("IMAGE_LEVEL",<%=Constant.IMAGE_LEVEL%>,"${dmap.IMAGE_LEVEL}",true,"short_sel",'',"false",'');
				</script>
             </td>
             </tr>
             <tr>
             <td align="right">验收形象等级：</td>
             <td align="left">
             	<script type="text/javascript">
					genSelBoxExp("IMAGE_COMFIRM_LEVEL",<%=Constant.IMAGE_LEVEL%>,"${dmap.CHECKED_IMAGE_LEVEL}",true,"short_sel",'',"false",'');
				</script>
             </td>
             <td align="right">验收形象店形式：</td>
             <td align="left">
             	<script type="text/javascript">
					genSelBoxExp("VI_CONSTRUCT_TYPE",<%=Constant.VI_CONSTRUCT_TYPE%>,"${dmap.CHECKED_TYPE}",false,"short_sel",'',"false",'');
				</script>
             </td>             
		 	</tr>
		 	<c:if test="${dmap.CHECKED_TYPE == 20021002}">
		 	<tr>
		 		<td align="right">线下已返(元)：</td>
		 		<td align="left" colspan="5">${dmap.OFFLINE_REBATE }</td>
		 	</tr>
		 	</c:if>
          </c:if>
          <c:if test="${dmap.YEAR_FLAG!=1}">
           	<tr>
           		<td align="right">增加台数：</td>
	 			<td>
	 				<input type="text" value="${dmap.SUPPORT_NUMBER}" name="SUPPORT_NUMBER" id="SUPPORT_NUMBER" readonly="readonly" cn_name="增加台数" class="" />
	 			</td>
           	</tr>   
          </c:if>
          <tr>
             <td align="right">状态：</td>
             <td align="left">
             ${dmap.H_STATE}
             </td>
             <td align="right">车系：</td>
             <td align="left">
             	${dmap.GROUP_NAME}
             </td>
		 	</tr>   
        <td align="right">备注：</td>
        <td colspan="5"><textarea name="textarea" readonly="readonly" id="textarea"  cols="80" rows="4"
                               readonly="readonly"    class="middle_txt">${dmap.REMARK}</textarea>
            <font style="color: red"></font>
        </td>
    </tr>   
			</table>
			</td>
    	  <tr>
    	  </c:forEach>
    	  
    	   <tr id="kpxx" >
			<td width="100%" colspan=4>
			<div id="div_2">
			<table style="display: none;"  id="tab1" width=100% border="0" align="center" cellpadding="1" cellspacing="1"> 
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
               <td align="left"><input id="YEAR_RENT" cn_name="年租金" type="text" readonly="readonly" name="YEAR_RENT" onchange="onchangeVlidateSaleQty(this)" value="${dMap.YEAR_RENT}"class=""  /></td>
		 </tr>
		 <tr>
			   <td align="right">比例：</td>
               <td align="left"><input id="SCALE" cn_name="比例" type="text" name="SCALE"  readonly="readonly" onchange="onchangeVlidateSaleQty1(this)" value="${dMap.SCALE}"class=""  />%</td>
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
        <td align="right">备注：</td>
        <td colspan="5"><textarea name="textarea" id="textarea"  cols="80" rows="4" readonly="readonly"
                                   class="middle_txt">${dMap.REMARK}</textarea>
            <font style="color: red"></font>
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
		 		<input name="_saveBtn" type="button" disabled="disabled" class="normal_btn" onclick="saveVi()" value="保存" id="_saveBtn" /> &nbsp; 
				<input name="_addBtn" type="button" disabled="disabled" class="normal_btn" onclick="addVi()" value="上报" id="_addBtn" /> &nbsp; 
		 		<input name="_addBtn" type="button" class="normal_btn" onclick="goBack()" value="返回" id="_addBtn" /> &nbsp; 
		 		</td>
		 	</table>
		 </form>
		 <form action="" method="post" id="fm1" name="fm1">
		 	 <input id="DEALER_ID" name="DEALER_ID" value="${dealerInfo.dealerId}"class=""  type="hidden"/></td>
		 </form>
</body>
</html>


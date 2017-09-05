<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>无旧件(无费用申明)新增</title>
<%String contextPath = request.getContextPath();%>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();//初始化时间控件
   		$('old').style.display='none';
   		$('balance').style.display='none';
	}
</script>
</head>
<body onload="doInit();">
<%String type = (String)request.getAttribute("type"); %>
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;经销商信息变更&gt;无旧件(无费用申明)新增
<form name="fm" id="fm">
	<input type="hidden" id="type" name="type" value="<%=type %>" />
	<table class="table_edit" id="vehicleInfo">
		<tr>
			<td class="table_query_2Col_label_6Letter">基地：<input type="hidden" id="count" name="count" value="" /></td>
			<td>
				<script type="text/javascript">
					genSelBoxExp("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"min_sel","onchange=showDate(this.value,<%=type%>);","false",'');
				</script>
				<font color="red" size="2">*请选择基地(必选)*</font>
			</td>
		</tr>
	</table>
	
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query"  id="old">
		<tr>
			<td align="right" nowrap="true">变更类型：</td>
			<td align="left" nowrap="true">
				<script type="text/javascript">
					writeItemValue(${type });
				</script>
			</td>
			<td align="right" nowrap="true"></td>
			<td align="left" nowrap="true"></td>
		</tr>
		<tr>
			<td align="right" nowrap="true">旧件回运单提交至：</td>
			<td align="left" nowrap="true">
				<label id="div"></label><input type="hidden" id="oldDate" name="oldDate" value="" />
			</td>
			<td align="right" nowrap="true">旧件审核至：</td>
			<td align="left" nowrap="true">
				<label id="div1"></label><input type="hidden" id="oldReview" name="oldReview" value="" />
			</td>
		</tr>
		<tr>
			<td align="right" nowrap="true">无旧件申明日期起：</td>
			<td align="left" nowrap="true">
				<label id="div11"></label><input type="hidden" id="oldDate_1" name="oldDate_1" value="" />
			</td>
			<td align="right" nowrap="true">无旧件申明日期止：</td>
	        <td>
	        	<input name="old_Date" type="text" id="old_Date" class="short_txt" datatype="0,is_date,10" hasbtn="true" callFunction="showcalendar(event, 'old_Date', false);" />
	        </td>
		</tr>
	    <tr>
	    	<td align="center" colspan="5">
	    		<input type="button" name="BtnQuery" id="queryBtn1"  value="提交"  class="normal_btn" onClick="updateDate(1);" >
	    		<input type="button" name="BtnAdd" id="queryBtn2"  value="返回"  class="normal_btn" onClick="goHome();" >
	    	</td>
	    </tr>
	</table>
	
	
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query" id="balance">
		<tr>
			<td align="right" nowrap="true">变更类型：</td>
			<td align="left" nowrap="true">
				<script type="text/javascript">
					writeItemValue(${type });
				</script>
			</td>
			<td align="right" nowrap="true"></td>
			<td align="left" nowrap="true"></td>
		</tr>
		<tr>
			<td align="right" nowrap="true">结算提交至：</td>
			<td align="left" nowrap="true">
				<label id="div2"></label><input type="hidden" id="balanceDate" name="balanceDate" value="" />
			</td>
			<td align="right" nowrap="true">结算审核至：</td>
			<td align="left" nowrap="true">
				<label id="div3"></label><input type="hidden" id="balanceReview" name="balanceReview" value="" />
			</td>
		</tr>
		<tr>
			<td align="right" nowrap="true">无费用申明日期起：</td>
			<td align="left" nowrap="true">
				<label id="div22"></label><input type="hidden" id="balanceDate_1" name="balanceDate_1" value="" />
			</td>
			<td align="right" nowrap="true">无费用申明日期止：</td>
	        <td>
	        	<input name="balance_Date" type="text" id="balance_Date" class="short_txt" datatype="0,is_date,10" hasbtn="true" callFunction="showcalendar(event, 'balance_Date', false);" />
	        </td>
		</tr>
	    <tr>
	    	<td align="center" colspan="5">
	    		<input type="button" name="BtnQuery" id="queryBtn3"  value="提交"  class="normal_btn" onClick="updateDate(2);" >
	    		<input type="button" name="BtnAdd" id="queryBtn4"  value="返回"  class="normal_btn" onClick="goHome();" >
	    	</td>
	    </tr>
	</table>
	
	
	</form>
</body>

<script type="text/javascript">

function showDate(yieldly,type){
	var url = "<%=contextPath%>/dealerInfo/dealerInfoChange/changeOldDate/showDate.json?yieldly="+yieldly+"&type="+type;
	sendAjax(url,showDetail2,"fm")
}
//设置时间到页面上
function showDetail2(json){
	var oldDate = json.oldDate;
	var oldReviewDate = json.oldReviewDate;
	var balanceDate = json.balanceDate;
	var balanceReviewDate = json.balanceReviewDate;
	var oldDate_1 = json.oldDate_1;
	var balanceDate_1 = json.balanceDate_1;
	if(oldDate==undefined && oldReviewDate==undefined){
		var balanceDate1 = balanceDate.substring(0,10);
		var balanceReviewDate1 = balanceReviewDate.substring(0,10);
		var balanceDate_11 = balanceDate_1.substring(0,10);
		$('div2').innerText=balanceDate1;
		$('div3').innerText=balanceReviewDate1;
		$('div22').innerText=balanceDate_11;
		$('balanceDate').value=balanceDate1;
		$('balanceReview').value=balanceReviewDate1;
		$('balanceDate_1').value=balanceDate_11;

		$('old').style.display='none';
		$('balance').style.display='';
		
	}else{
		var oldDate1 = oldDate.substring(0,10);
		var oldReviewDate1 = oldReviewDate.substring(0,10);
		var oldDate_11 = oldDate_1.substring(0,10);
		
		$('div').innerText=oldDate1;
		$('div1').innerText=oldReviewDate1;
		$('div11').innerText=oldDate_11;
		$('oldDate').value=oldDate1;
		$('oldReview').value=oldReviewDate1;
		$('oldDate_1').value=oldDate_11;
		
		$('old').style.display='';
		$('balance').style.display='none';
	}
}

function updateDate(count){
	if(!submitForm('fm')){
		return;
	}
	$('count').value=count;
	//add  xiongchuan 2011-04-14 
	if(count==1){
       if($('oldDate').value!=$('oldReview').value){
    	   MyAlert('期间有旧件未审核!');
    	   return;
          }
       }
	//add  xiongchuan 2011-04-14 
	if($('type').value==<%=Constant.CHANGE_TYPE_OLD%>){
		var oldDate_1=document.getElementById("oldDate_1").value;
	    var old_Date=document.getElementById("old_Date").value;
	    var year=old_Date.substring(0,4);
		var mouth=old_Date.substring(5,7);
		var day=old_Date.substring(8,10);
		var yieldly=document.getElementById("yieldly").value;
		if(yieldly==''){
			MyAlert('请选择基地!');
			return;
		}
	    if(oldDate_1>old_Date){
		       MyAlert("起始年份不应大于结束年份!");
		       return;
		}
		var last=(new Date((new Date(year,mouth,1)).getTime()-1000*60*60*24)).getDate();//获取当月最后一天日期
		if(day!=last){
			MyAlert('必须选择当月的最后一天!');
			return;
		}
		var url = "<%=contextPath%>/dealerInfo/dealerInfoChange/changeOldDate/isCheck.json?count="+count;
		sendAjax(url,showDetail3,"fm")
	}else{
		var url6="<%=contextPath%>/dealerInfo/dealerInfoChange/changeOldDate/getFalg.json?count="+count;
		makeNomalFormCall(url6,showDetail6,'fm','');
	}
}

function showDetail6(json){
	var flag = json.flag;
	var error = json.error;
	if(error=='error'){
		MyAlert('选择时间不能大于当前时间!');
		return;
	}
	if(flag=='false'){
		MyAlert('请先在顶腾系统中完成11月份前[维护结算申请单]的提报!');
		return;
	}else{
		isCheck6();
	}
}

function isCheck6(count){
	var balanceDate_1=document.getElementById("balanceDate_1").value;
	var balance_Date = document.getElementById("balance_Date").value;
	var year=balance_Date.substring(0,4);
	var mouth=balance_Date.substring(5,7);
	var day=balance_Date.substring(8,10);
	var beginDay=balanceDate_1.substring(8,10);
	var yieldly = document.getElementById("yieldly").value;
	if(yieldly==''){
		MyAlert("请选择基地");
		return;
	}
	if(balanceDate_1>balance_Date){
	       MyAlert("起始年份不应大于结束年份！");
	       return;
	}
	var last=(new Date((new Date(year,mouth,1)).getTime()-1000*60*60*24)).getDate();//获取当月最后一天日期
	if(mouth!=12){
		if(day!=last){
			MyAlert('必须选择当月的最后一天!');
			return;
		}
	}else{
		if(Number(beginDay)==Number(1) && Number(day)==Number(${day })){
			
		}else if(Number(beginDay)==Number(${day+1 }) && Number(day)==${day1 }){
			
		}else{
			MyAlert('12月份必须选择结算日!');
			return;
		}
	}
	MyConfirm("你确定申明无费用!",NoMoney);
}

function NoMoney(){
	$('queryBtn3').disabled = true ;
	$('queryBtn4').disabled = true ;
	var url = "<%=contextPath%>/dealerInfo/dealerInfoChange/changeOldDate/updateOldReturn.json";
	sendAjax(url,showDetail4,"fm")
}

function showDetail3(json){
	var ok = json.ok;
	var flag=json.flag;
	var nowTime1=json.nowTime1;
	var nowTime2=json.nowTime2;
	var yearTime1=json.yearTime1;
	var yearTime2=json.yearTime2;
	var rr=json.rr;
	if(ok=='ok'){
		MyAlert('还未到结算日!');
		return;
	}
	if(Number(yearTime1)<=Number(yearTime2)){
		if(Number(nowTime2)>=Number(nowTime1)){
			MyAlert('不能选择当前月份,只能选择当前月的上月回运!');
    		return;
		}
	}
	if(flag=='false'){
		MyAlert('请先在顶腾系统中完成11月份前[旧件清单]的提报!');
		return;
	}
	if(rr=='rr'){
		MyAlert('前期有旧件未审核通过!');
		return;
	}
	MyConfirm("你确定申明无旧件!",NoOrder);
}
function NoOrder(){
	$('queryBtn1').disabled = true ;
	$('queryBtn2').disabled = true ;
	var url = "<%=contextPath%>/dealerInfo/dealerInfoChange/changeOldDate/updateOldReturn.json";
	sendAjax(url,showDetail4,"fm")
}
function showDetail4(json){
	if(json.type == 1){
		$('queryBtn1').disabled = false ;
		$('queryBtn2').disabled = false ;
	}else{
		$('queryBtn3').disabled = false ;
		$('queryBtn4').disabled = false ;
	}
	var ok = json.ok;
	if(ok=='error'){
		MyAlert("此时间段存在费用，不予做无费用申明！");
		return ;
	}
	if(ok=='errorOld'){
		MyAlert("此时间段存在旧件，不予做无旧件申明！");
		return ;
	}
	if(ok=='ok'){
		MyAlert('变更成功!');
		//_hide();
		goHome();
	}else{
		MyAlert('变更失败!');
		return;
	}
}
function goHome(){
	location.href="<%=contextPath%>/dealerInfo/dealerInfoChange/changeOldDate/oldReturnDateChangeInit.do";
}
</script>
</html>

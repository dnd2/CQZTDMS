<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="co" %>   
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>   
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>我的客户回访</title>
<% String contextPath = request.getContextPath(); %>
<style type="text/css">
.table_list_row0 td {
	background-color:#FFFFCC;
	border: 1px solid #DAE0EE;
	white-space:    nowrap;
}
</style>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt;回访管理&gt;我的客户回访 
</div>
  <table  align="center" cellpadding="2" cellspacing="1" class=table_query border="1px">
    <tbody>
      <tr class="">
        <td width="162" align="right" bgcolor="#ffffff">单子详细信息：</td>
        <td width="194" bgcolor="#ffffff"><a href="#" onClick="javascript:Toggle();">查看</a></td>
        <td width="298" align="right" bgcolor="#ffffff">回访电话：</td>
        <td width="629" bgcolor="#ffffff"><label><span id="lblType1">  ${result.basicInof[0].RV_PHONE}</span></label></td>
      </tr>
      <tr class="">
        <td align="right" bgcolor="#ffffff">客户名称：</td>
        <td bgcolor="#ffffff"><span id="lblName">${result.basicInof[0].CTM_NAME} </span></td>
        <td align="right" bgcolor="#ffffff">回访类型：</td>
        <td bgcolor="#ffffff"><span id="lblType">
        <script type='text/javascript'>
       var activityType=getItemValue('${result.basicInof[0].RV_TYPE}');
       document.write(activityType) ;
     	</script> 
       </span></td>
      </tr>
      <tr class="">
        <td align="right" bgcolor="#ffffff">生成时间：</td>
        <td bgcolor="#ffffff"><span id="lblTime">${result.basicInof[0].RV_DATE}</span></td>
        <td align="right" bgcolor="#ffffff">无人接听次数：</td>
        <td bgcolor="#ffffff"><span id="lblAboveIsSucceed">${result.basicInof[0].RV_TIMES} </span></td>
      </tr>
      <tr class="">
        <td align="right" bgcolor="#ffffff">回访结果：</td>
        <td bgcolor="#ffffff"><span id="lblAboveIsSucceed0">
         <script type='text/javascript'>
       var activityType=getItemValue('${result.basicInof[0].RV_RESULT}');
       document.write(activityType) ;
     	</script> 
        </span></td>
        <td align="right" bgcolor="#ffffff"> </td>
        <td bgcolor="#ffffff"> </td>
      </tr>
       <tr class="">
        <td align="right" bgcolor="#ffffff">维修历史：</td>
        <td bgcolor="#ffffff"><span id="lblAboveIsSucceed0">
        <a href="<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN=${result.basicInof[0].VIN}" target="_blank" >查看</a></span></td>
        <td align="right" bgcolor="#ffffff"> </td>
        <td bgcolor="#ffffff"> </td>
      </tr>
    </tbody>
  </table>
  <table width="89%" align="center" cellpadding="2" cellspacing="1" class="table_query"  border="1px" id="tb2" style="display:none">
  <tbody>
    <tr  align="center">
      <td height="26" colspan="6" align="center" valign="middle" bgcolor="#ffffff"><strong>客户详细信息 </strong></td>
    </tr>
    <tr class="">
      <td colspan="6" align="left"><strong>基本信息</strong></td>
    </tr>
    <tr class="">
      <td width="13%" align="right">客户名称： </td>
      <td width="20%" align="left">${result.basicInof[0].CTM_NAME} </td>
      <td width="13%" align="right">客户类型： </td>
      <td width="20%" align="left">
       <script type='text/javascript'>
       var activityType=getItemValue('${result.basicInof[0].CTM_TYPE}');
       document.write(activityType) ;
     	</script>  </td>
      <td width="13%" align="right">用途： </td>
      <td width="20%" align="left">${result.basicInof[0].SALES_ADDRESS} </td>
    </tr>
    <tr class="">
      <td align="right">身份证号： </td>
      <td colspan="5" align="left">${result.basicInof[0].CARD_NUM}</td>
    </tr>
    <tr class="">
      <td align="right">省份： </td>
      <td align="left">${result.basicInof[0].PROVINCE}</td>
      <td align="right">县市： </td>
      <td align="left">${result.basicInof[0].CITY} </td>
      <td align="right">邮编： </td>
      <td align="left">${result.basicInof[0].POST_CODE} </td>
    </tr>
    <tr class="">
      <td align="right">客户地址： </td>
      <td align="left" colspan="5">${result.basicInof[0].ADDRESS}</td>
    </tr>
    <tr class="">
      <td align="right">联系人： </td>
      <td align="left">${result.basicInof[0].CTM_NAME} </td>
      <td align="right">用户级别： </td>
      <td bgcolor="#ffffff">${result.basicInof[0].GUEST_STARS}</td>
      <td bgcolor="#ffffff">&nbsp;</td>
      <td bgcolor="#ffffff">&nbsp;</td>
    </tr>
    <tr class="">
      <td align="right">单位电话： </td>
      <td align="left">${result.basicInof[0].COMPANY_PHONE} </td>
      <td align="right">住宅电话： </td>
      <td align="left">${result.basicInof[0].OTHER_PHONE} </td>
      <td align="right">手机： </td>
      <td align="left">${result.basicInof[0].MAIN_PHONE}</td>
    </tr>
    <tr class="" style="background-color: rgb(255, 255, 255);">
      <td colspan="6" align="left"><strong>车辆信息</strong></td>
    </tr>
    <tr class="" style="background-color: rgb(255, 255, 255);">
      <td align="right">车种： </td>
      <td align="left">${result.basicInof[0].SERIES_NAME} </td>
      <td align="right">车型： </td>
      <td align="left">${result.basicInof[0].MODEL_NAME} </td>
      <td align="right">颜色： </td>
      <td align="left">${result.basicInof[0].COLOR} </td>
    </tr>
    <tr class="" style="background-color: rgb(255, 255, 255);">
      <td align="right">产地： </td>
      <td align="left">${result.basicInof[0].YIELDLY} </td>
      <td align="right">内部型号： </td>
      <td align="left">${result.basicInof[0].PACKAGE_NAME}</td>
      <td align="right">地区： </td>
      <td align="left">${result.basicInof[0].DISTRICT} </td>
    </tr>
    <tr class="" style="background-color: rgb(255, 255, 255);">
      <td align="right">发动机号： </td>
      <td align="left">${result.basicInof[0].ENGINE_NO} </td>
      <td align="right"> </td>
      <td align="left"></td>
      <td align="right">底盘号： </td>
      <td align="left">${result.basicInof[0].VIN}</td>
    </tr>
    <tr class="" style="background-color: rgb(255, 255, 255);">
      <td align="right">生产日期： </td>
      <td align="left">${result.basicInof[0].PRODUCT_DATE}</td>
      <td align="right">购买日期： </td>
      <td align="left">${result.basicInof[0].PURCHASED_DATE} </td>
      <td align="right">价格： </td>
      <td align="left">${result.basicInof[0].PRICE} </td>
    </tr>
    <tr class="">
      <td align="right">装备代码： </td>
      <td align="left">${result.basicInof[0].MATERIAL_CODE}</td>
      <td align="right">经销商名称：</td>
      <td colspan="3" bgcolor="#ffffff">${result.basicInof[0].DEALER_NAME}</td>
    </tr>
  </tbody>
</table>
 <form method="post" name ="fm" id="fm">
 <input type="hidden" name="list" id= "list"  value="" />
 <input type="hidden" name="QR_ID" id= "QR_ID"  value="${result.basicInof[0].QR_ID}" />
 <c:if test="${fn:length(questionhtml)==0}">
<table width="89%" align="center" cellpadding="2" cellspacing="1" id="questionair" class="table_query"  border="1px">
  <tbody>
       <tr  align="center">
      <td height="26" colspan="2" align="center" valign="left" bgcolor="#ffffff"> 
		<strong>没有设置问卷。</strong>
	  </td>
    </tr>
    </tbody>
</table>
</c:if>
 <c:if test="${fn:length(questionhtml)>0}">
<table width="89%" align="center" cellpadding="2" cellspacing="1" class="table_query" id="questionair" border="1px">

  <tbody>
       <tr  align="center">
      <td height="26" colspan="2" align="center" valign="middle" bgcolor="#ffffff"> 
		<strong><span id="ReturnVisitHandling1_lblHead"> ${result.basicInof[0].QR_NAME} </span></strong></td>
    </tr>
    <tr  align="center">
      <td height="26" colspan="2" align="center" valign="middle" bgcolor="#ffffff">${result.basicInof[0].QR_GUIDE}</td>
    </tr>

    <tr class="">
      <td width="94" align="center">题号</td>
      <td width="1057" align="center">问题</td>
    </tr>
    
   		${questionhtml}
    <tr>
    	<td colspan="2" align="left">${result.basicInof[0].QR_THANKS}</td>
    </tr>
  </tbody>
</table>
</c:if>
<!--页面列表 begin -->

 <input type="hidden" id="RV_ID" name="RV_ID" value="${result.basicInof[0].RV_ID}"/>
<table width="89%" align="center" cellpadding="2" cellspacing="1" class="table_query"  border="1px">
  <tbody>
    <tr class="">
      <td colspan="4" align="center" bgcolor="#ffffff"><input id="queryBtn" class="cssbutton" onclick="dialAction();" value="拨号" type="button" name="queryBtn" />
      <input id="queryBtn3" class="cssbutton" onclick="saveReview();" value="保存" type="button" name="queryBtn2" />
      <input id="queryBtn4" class="cssbutton" onclick="javascript:history.go(-1)" value="返回" type="button" name="queryBtn3" /></td>
    </tr>
  </tbody>
</table>
</form>
<script type="text/javascript" >
function Toggle()
{
    var el = document.getElementById('tb2');
	if(el.style.display=='none'){
		document.getElementById('tb2').style.display='';
	}else 
		document.getElementById('tb2').style.display='none';
}
 function saveReview(){
	setList();
    var url= "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/saveAnswer.json;
	makeNomalFormCall(url,showResult,'fm');
	
}
function setList(){
	var list = getanswer();
	document.getElementById('list').value = list;
}
function showResult(json){
	var msg=json.msg;
	if(msg=='01'){
		MyAlertForFun("保存成功",closeSaveReview);
	}else if(msg=='02'){
		MyAlert('已经对该客户成功进行了一次回访，此次回访不进行记录，请点击返回。');
	}
}
function closeSaveReview(){
	fm.action = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/reviewQuestionnairExportInit.do";
	fm.submit();
}
function dialAction(){
	fm.action = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/dialAction.do";
	fm.submit();
}
function getanswer()
{
	var list=document.getElementById("questionair").getElementsByTagName("input");
	var textarealist=document.getElementById("questionair").getElementsByTagName("textarea");
	var strData="";
	 for(var i=0;i<list.length && list[i];i++)
	 {
	       //判断是否为文本框
	       if(list[i].type=="text")   
	       {
	       		var ttemp=list[i].value;
	       		if((null!=ttemp)&&(Trim(ttemp)!=''))
	       		{
	           	 strData +=list[i].type+":"+list[i].id+":"+list[i].value+"|"; 
	            }         
	       }else if(list[i].type=="radio")
	       {
	       		if(list[i].checked){
		       		var rtemp=list[i].value;
		       		if((null!=rtemp)&&(Trim(rtemp)!=''))
		       		{
	       				strData +=list[i].type+":"+list[i].id+":"+list[i].value+"|";
	       			}
	       		}
	       }else if(list[i].type=="checkbox"){
	       		if(list[i].checked){
	       			var ctemp=list[i].value;
		       		if((null!=ctemp)&&(Trim(ctemp)!=''))
		       		{	
	       				strData +=list[i].type+":"+list[i].id+":"+list[i].value+"|";
	       			}
	       		}
	       }
	       
	 }
	 for(var j =0;j<textarealist.length;j++)
	 {
	 	var temp=textarealist[j].value;
	 	if(null!=temp)
	 	{
	 		if(Trim(temp)!=''){
	 			strData +=textarealist[j].type+":"+textarealist[j].id+":"+textarealist[j].value+"|";
	 		}
	 	}
	 }
	 //MyAlert(strData);
	 return strData;
	 
}
function LTrim(str)
{
    var i;
    for(i=0;i<str.length;i++)
    {
        if(str.charAt(i)!=" "&&str.charAt(i)!=" ")break;
    }
    str=str.substring(i,str.length);
    return str;
}
function RTrim(str)
{
    var i;
    for(i=str.length-1;i>=0;i--)
    {
        if(str.charAt(i)!=" "&&str.charAt(i)!=" ")break;
    }
    str=str.substring(0,i+1);
    return str;
}
function Trim(str)
{
    return LTrim(RTrim(str));
}
function checkAnswer()
{
	var clist=document.getElementById("questionair").getElementsByTagName("input");
	var ctextarealist=document.getElementById("questionair").getElementsByTagName("textarea");
	var cstrData="";
	 for(var i=0;i<clist.length && clist[i];i++)
	 {
	       //判断是否为文本框
	       if(clist[i].type=="text")   
	       {
	            var txvalue= clist[i].value; 
	            var txid=clist[i].id;
	            if(null==txvalue&&"".indexOf(txvalue)==0)
	            {
	            	 MyAlert("请回答第"+txid.substring(2,txid.length)+"个问题的答案！");
	            	return false;
	            	
	            }         
	       }else if(clist[i].type=="radio")
	       {
	            var raid=clist[i].id;
	            
	            var raeckbox=document.getElementsByName(raid);
	            var ischeck=false;
	            for(i=0;i<raeckbox.length;i++)
	            {
	            	if(raeckbox[i].checked)
	            	{
	            		ischeck=true;
	            	}
	            }
	            if(ischeck==false)
	            {
	            	MyAlert("请回答第"+raid.substring(2,raid.length)+"个问题的答案！");
	            	return false;
	            } 
	       }else if(clist[i].type=="checkbox"){
	       		var chvalue= clist[i].value; 
	            var chid=clist[i].id;
	            var checkbox=document.getElementsByName(chid);
	            var checkorno=false;
	            for(i=0;i<checkbox.length;i++)
	            {
	            	if(checkbox[i].checked)
	            	{
	            		checkorno=true;
	            	}
	            }
	            if(checkorno==false)
	            {
	            	MyAlert("请回答第"+chid.substring(2,chid.length)+"个问题的答案！");
	            	return false;
	            }    
	       }
	       
	 }
	
}
</script>  
</body>
</html>
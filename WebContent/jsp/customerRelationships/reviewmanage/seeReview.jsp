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
<body onload="setFalse();">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt;回访管理&gt;我的客户回访 
</div>
  <table  align="center" cellpadding="2" cellspacing="1" class=table_query border="1px">
    <tbody>
      <tr class="">
        <td width="162" align="right" bgcolor="#ffffff">单子详细信息：</td>
        <td width="194" bgcolor="#ffffff"><a href="#" onClick="javascript:Toggle();">查看</a></td>
        <td width="298" align="right" bgcolor="#ffffff">回访电话：</td>
        <td width="629" bgcolor="#ffffff"><input type="text" id="txtTel" value="${result.basicInof[0].RV_PHONE}" name="incomeTep"/></td>
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
        </span>
        </td>
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
	     	</script>  </span></td>
        <td align="right" bgcolor="#ffffff"> </td>
        <td bgcolor="#ffffff"> </td>
      </tr>
       <tr class="">
        <td align="right" bgcolor="#ffffff">维修历史：</td>
        <td bgcolor="#ffffff"><span id="lblAboveIsSucceed0">
        <a href="<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN=${result.basicInof[0].VIN}" target="_blank" >查看</a></td>
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
	     	</script> 
       </td>
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
     
      <td align="right">底盘号： </td>
      <td align="left">${result.basicInof[0].VIN}</td>
      <td align="right"> </td>
      <td align="left"></td>
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
<c:if test="${fn:length(questionhtml)==0}">
<table width="89%" align="center" cellpadding="2" cellspacing="1" id="questionair" class="table_query"  border="1px">
  <tbody>
       <tr  align="center">
      <td height="26" colspan="2" align="center" valign="left" bgcolor="#ffffff"> 
		<strong>还未设置问卷。</strong>
	  </td>
    </tr>
    </tbody>
</table>
</c:if>
<c:if test="${fn:length(questionhtml)>0}">
<table width="89%" align="center" cellpadding="2" cellspacing="1" id="questionair" class="table_query"  border="1px">
  <tbody>
       <tr  align="center">
      <td height="26" colspan="2" align="center" valign="middle" bgcolor="#ffffff"> 
		<strong><span id="ReturnVisitHandling1_lblHead">${result.basicInof[0].QR_NAME} </span></strong></td>
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
<table width="100%" class="table_list">
  <tbody>
    <tr>
      <th colspan="6" align="left"><strong>回访历史</strong></th>
    </tr>
    <tr class="table_list_row0">
      <td width="5%">序号</td>
      <td width="19%">是否有人接听</td>
      <td width="19%">处理方式</td>
      <td width="19%">回访人</td>
      <td width="19%">回访时间</td>
      <td width="19%">备注内容</td>
    </tr>
  	 <c:if test="${fn:length(result.reviewInof)==0}">
  	  <tr ><td colspan="6" align="middle"><strong>暂无回访记录。</strong></td>
  	  </tr>
     </c:if>
     <c:forEach var="ql" items="${result.reviewInof}">
      <tr class="table_list_row1">
      <td>${ql.ROWNUM}</td>
      <td>${ql.RD_IS_ACCEPT}</td>
      <td>${ql.RD_MODE}</td>
      <td>${ql.RD_USER}</td>
      <td>${ql.RD_DATE}</td>
      <td>${ql.RD_CONTENT}</td>
      <td>&nbsp;</td>
    </tr>
   </c:forEach>
    
  </tbody>
</table>
<script type="text/javascript" >
function Toggle()
{
    var el = document.getElementById('tb2');
	if(el.style.display=='none'){
		document.getElementById('tb2').style.display='';
	}else 
		document.getElementById('tb2').style.display='none';
}
function setFalse()
{
	var clist=document.getElementById("questionair").getElementsByTagName("input");
	var ctextarealist=document.getElementById("questionair").getElementsByTagName("textarea");
	 for(var i=0;i<clist.length && clist[i];i++)
	 {
	 	clist[i].disabled='disabled';
	 }
	 for( var j =0;j<ctextarealist.length;j++)
	 {
	 	ctextarealist[j].disabled='disabled';
	 }
}
function  Pingfen()
{
	  var strIvr = "AC_SWITCHIVR;CALLID=" +  parent.document.ut_atocx.myCall_CallId + ";EXT=" + ${LOGON_USER.txtExt} + ";IVRFILE=SCORE.DAT;NODE=1;IVRMSG="+${LOGON_USER.actn}+"|"+ parent.document.ut_atocx.myCall_CallId + ";";
        parent.document.ut_atocx.ATTranCall_toIVR('${LOGON_USER.actn}',0,"",strIvr);
}

function AT_Command(strCmd) 
{
    switch (strCmd) {
        case "Pickup":
            parent.document.ut_atocx.ATAnswer('${LOGON_USER.actn}', "");
            break;
        case "Hangup":
            parent.document.ut_atocx.ATHangup('${LOGON_USER.actn}', "");
            break;
        case "PlaceCall": //呼叫电话
            parent.document.ut_atocx.ATPlaceCall('${LOGON_USER.actn}',$("txtTel").value);
            break;
        default:
            MyAlert("未处理命令：" + strCmd);
            break;
    }
    return true;
}

function updateStatus(type)
{
	var url = "<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAccept/updateStatus.json?type="+type;
	makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
}
//回调处理
function afterCall(json)
	{
		    parent.document.ut_atocx.UidSelected = '${LOGON_USER.actn}';
			var UidStatus =  parent.document.ut_atocx.UidStatus;
		    var UidCall_Status =  parent.document.ut_atocx.UidCall_Status;
		    var str = '';
			if(UidStatus == "00")
			{
			   str = '未登陆';
			}else if (UidCall_Status == "05")
			{
				str = '正在电话受理';
			}else if(UidCall_Status == "03")
			{
				str = '正在电话受理';
			}else if(UidCall_Status == "04")
			{
				str = '正在电话受理';
			}
			else if(UidStatus == "02")
			{
			  str = '已置忙';
			}
			else if(UidCall_Status == "01")
			{
				str = '空闲';
			}
			else 
			{
				str = '空闲';
			}
			 parent.document.getElementById('Ustast').innerHTML = '你的状态是 : '+ str;
			
	}
	
	function setFree()
	{
       	 parent.document.ut_atocx.ATSetBusy('${LOGON_USER.actn}',0, '');
       	 MyAlertForFun('置闲成功');
		 makeNomalFormCall('<%=contextPath%>/customerRelationships/classMonitorManage/ClassMonitorManage/logupdate.json?RpcStat=95131002&stat=95131002',RpcStat,'fm','');
       	
    }
	    
    function setBusy()
	{
	    parent.document.ut_atocx.ATSetBusy('${LOGON_USER.actn}', 1, '');
	    MyAlert("置忙成功！");
	     makeNomalFormCall('<%=contextPath%>/customerRelationships/classMonitorManage/ClassMonitorManage/logupdate.json?RpcStat=95131005&stat=95131002',RpcStat,'fm','');
	}

   function RpcStat()
   {
   }
</script>

<table align="center">
	<tbody>
    <tr class="">
      <td colspan="4" align="center" bgcolor="#ffffff">
      <input id="queryBtn4" class="cssbutton" onclick="javascript:history.go(-1)" value="返回" type="button" name="queryBtn3" /></td>
    </tr>
  </tbody>
</table>  
</body>
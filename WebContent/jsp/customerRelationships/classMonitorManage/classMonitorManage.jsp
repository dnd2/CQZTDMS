<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>班长监控</title>
<LINK rel=stylesheet type=text/css href="../../style/content.css">
<LINK rel=stylesheet type=text/css href="../../style/calendar.css">
<LINK rel=stylesheet type=text/css href="../../style/page-info.css">
<script type="text/javascript">
	var i = 0; // 空闲状态
	var j = 0;
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}	
	function genImage(expStr,sta){
		parent.document.ut_atocx.UidSelected = sta;
		var UidStatus = parent.document.ut_atocx.UidStatus;
		var UidCall_Status = parent.document.ut_atocx.UidCall_Status;
		var str = "";
		if(UidStatus == "00")
		{
		   str = '<img src="../../../img/tel0.gif"></img>';
		}else if (UidCall_Status == "05")
		{
			str = '<img src="../../../img/tel3.gif" border="0" name="picture1" style="height: 40px; width: 45px"></img>';
			j++;
		}else if(UidCall_Status == "03")
		{
			str = '<img src="../../../img/tel2.gif" border="0" name="picture1" style="height: 40px; width: 45px"></img>';
			j++;
		}else if(UidStatus == "02")
		{
		  str = '<img src="../../../img/tel4.gif" border="0" name="picture1" style="height: 40px; width: 45px"></img>';
		  j++;
		}
		else if(UidCall_Status == "01")
		{
			str = '<img src="../../../img/tel1.gif"></img>';
			i++;
		}
		else 
		{
			str = '<img src="../../../img/tel1.gif"></img>';
			i++;
		}
		document.write(str);
	}
	
	function setValue(){
		document.getElementById('freeData').value = i;
		document.getElementById('busyData').value = j;
		parent.document.ut_atocx.GroupSelected = '1';
		var  incomingData = parent.document.ut_atocx.Group_QueueCallId ;
		if(incomingData.length > 0)
		{
		     var Datalength= incomingData.split('|');
		     document.getElementById('incomingData').value = Datalength.length;
		}
	}
</script>
</head>

<body leftmargin="0" topmargin="0" onload="setValue();">

  <table class="table" id="table1" cellspacing="1" cellpadding="1" width="98%" align="center">
        <tr>
            <td class="tablebody1" id="tdSelectAgent" valign="top" width="100%" height="330px">
                <div id="wins" style="background: #eeeeee; overflow: auto; width: 100%; height: 100%">
                      <form method="post" name = "fm" id="fm">
                    <!--webbot bot="SaveResults" U-File="fpweb:///_private/form_results.csv" S-Format="TEXT/CSV" S-Label-Fields="TRUE" -->
                    <table id="table2" style="border-collapse: collapse" cellspacing="1"
                        cellpadding="0" width="100%" border="0" class=table_query>
                        
                                     <c:forEach var="ttCrmSeatsPOlist" items="${ttCrmSeatsPOList}">
							<tr>
							<c:forEach var="ttCrmSeatsPO" items="${ttCrmSeatsPOlist}">
								<td>
										<script type="text/javascript">
				        	 				genImage('${ttCrmSeatsPO.seWorkStatus}','${ttCrmSeatsPO.seSeatsNo}');
				        	 			</script>
									</br>
									<input type='checkbox'  name='seIds' value='${ttCrmSeatsPO.seSeatsNo}'  title="${ttCrmSeatsPO.seSeatsNo} ${ttCrmSeatsPO.seName}" />${ttCrmSeatsPO.seSeatsNo} ${ttCrmSeatsPO.seName}
								</td>
							</c:forEach>
							</tr>
						</c:forEach>
                    </table>
                    </form>
                </div>
            </td>
        </tr>
        <tr>
            <form id="process_he" action="--WEBBOT-SELF--" method="post">
            <td width="100%" height="30" align="center">
              <input class="normal_btn" onclick="forceInsert();" value="强插" type="button" name="button12" />&nbsp;&nbsp;
               &nbsp;&nbsp;
			    <input class="normal_btn" onclick="forceMoveSeatPull();" value="强拆" type="button" name="button12" />
			    &nbsp;&nbsp; <input class="normal_btn" onclick="intercept();" value="强插并强拆" type="button" name="button12" />
			    &nbsp;&nbsp; <input class="normal_btn" onclick="forceMoveSeat();" value="强制离席" type="button" name="button12" />
			    &nbsp;&nbsp; <input class="normal_btn" onclick="setFree();" value="置闲" type="button" name="button12" />
			    &nbsp;&nbsp; <input class="normal_btn" onclick="setBusy();" value="置忙" type="button" name="button12" />
&nbsp;&nbsp;&nbsp;		      </td>
            </form>
        </tr>
        <tr>
          
            <td style="height: 20px" valign="top" align="center" width="100%" height="20">
                                         当前排队队列长度: <input value="0" id="incomingData" size="4" name="cur_pd_len" readonly="readonly" style="border-top-style: none; border-right-style: none; border-left-style: none; background-color: transparent; border-bottom-style: groove">&nbsp;&nbsp;&nbsp;&nbsp; 
                                              空闲状态外线数: <input value="0" id="freeData" size="4" name="cur_ivr_idle" readonly="readonly" style="border-top-style: none; border-right-style: none; border-left-style: none; background-color: transparent; border-bottom-style: groove">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                                   忙状态外线数: <input value="0" id="busyData" size="4" name="cur_ivr_busy" readonly="readonly" style="border-top-style: none; border-right-style: none; border-left-style: none; background-color: transparent; border-bottom-style: groove">
            </td>
       
        </tr>
        <tr>
            <td valign="bottom" id="tdButton" width="100%">
                <font face="宋体">
                    <table id="Table3" height="100px" cellspacing="1" cellpadding="1" width="100%" align="center"
                        border="0">
                        <tr>
                            <td align="center" style="width: 20%">
                                <img alt="" src="../../../img/tel0.gif">
                            </td>
                            <td align="center" style="width: 20%">
                                <img alt="" src="../../../img/tel1.gif">
                            </td>
                            <td align="center" style="width: 20%">
                                <img alt="" src="../../../img/tel2.gif">
                            </td>
                            <td align="center" style="width: 20%">
                                <img alt="" src="../../../img/tel3.gif">
                            </td>
                            <td align="center" style="width: 20%">
                                <img alt="" src="../../../img/tel4.gif">
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                坐席未登录
                            </td>
                            <td align="center">
                                坐席空闲
                            </td>
                            <td align="center">
                                坐席有来电
                            </td>
                            <td align="center">
                                坐席处于受话状态
                            </td>
                            <td align="center">
                                坐席被置忙
                            </td>
                        </tr>
                    </table>
                </font>
            </td>
        </tr>
    </table>


<script type="text/javascript">
	var checkIds = new Array(); 
	//坐席组删除
	function refreshCheckIds(){
		var cnt = 0;
		var chk=document.getElementsByName("seIds");
		var l = chk.length;
		checkIds.splice(0,checkIds.length);
		for(var i=0;i<l;i++){
			if(chk[i].checked){
		       cnt++;
		       checkIds.push(chk[i].value);
			}
		 }
	}
	//强插
	function forceInsert(){
		var seIds = document.getElementsByName('seIds');
	    if(seIds.length > 0)
	    {
	       for(var i = 0 ;i < seIds.length ; i++)
	       {
	          if(seIds[i].checked)
	          {
	          	 makeNomalFormCall('<%=contextPath%>/customerRelationships/classMonitorManage/ClassMonitorManage/forceInsert.json?actn='+seIds[i].value,forceInsertBack,'fm','');
	          }
	         
	       }
	    }
	}
	
	function forceInsertBack(json) {
		if(json.success != null && json.success=='true'){
		   parent.document.ut_atocx.ATInsert('${LOGON_USER.actn}',json.SE_EXT, 1);	
			MyAlert("强插成功",sendPage);
		}else{
			MyAlert("强插失败！请联系管理员");
		}
	}
	//挂断
	function hangup(){
		refreshCheckIds();
		makeNomalFormCall('<%=contextPath%>/customerRelationships/classMonitorManage/ClassMonitorManage/hangup.json?ids='+checkIds,forceInsertBack,'fm','');
	}
	
	function hangupBack(json) {
		if(json.success != null && json.success=='true'){
			MyAlertForFun("挂断成功",sendPage);
		}else{
			MyAlert("挂断失败！请联系管理员");
		}
	}
	
	//强制离席(强拆)
	function forceMoveSeatPull(){
		var seIds = document.getElementsByName('seIds');
	    if(seIds.length > 0)
	    {
	       for(var i = 0 ;i < seIds.length ; i++)
	       {
	          if(seIds[i].checked)
	          {
	          	 makeNomalFormCall('<%=contextPath%>/customerRelationships/classMonitorManage/ClassMonitorManage/forceInsert.json?actn='+seIds[i].value,forceMoveSeatPullBack,'fm','');
	          }
	         
	       }
	    }
	}
	
	function forceMoveSeatPullBack(json) {
		if(json.success != null && json.success=='true'){
			  parent.document.ut_atocx.ATDiscCall('${LOGON_USER.actn}',json.SE_EXT,"");
			
			makeNomalFormCall('<%=contextPath%>/customerRelationships/classMonitorManage/ClassMonitorManage/pullSta.json?actn='+json.actn+'&stat=95131002',pullSta,'fm','');
		}else{
			MyAlert("强拆失败！请联系管理员");
		}
	}
	function pullSta(json)
	{
	    	MyAlertForFun("操作成功",sendPage);
	}
	//拦截
	function intercept(){
		var seIds = document.getElementsByName('seIds');
	    if(seIds.length > 0)
	    {
	       for(var i = 0 ;i < seIds.length ; i++)
	       {
	          if(seIds[i].checked)
	          {
	          	 makeNomalFormCall('<%=contextPath%>/customerRelationships/classMonitorManage/ClassMonitorManage/forceInsert.json?actn='+seIds[i].value,interceptBack,'fm','');
	          }
	         
	       }
	    }
	}
	
	function interceptBack(json) {
		if(json.success != null && json.success=='true')
		{
			parent.document.ut_atocx.ATInsert('${LOGON_USER.actn}',json.SE_EXT, 1);	
			parent.document.ut_atocx.ATDiscCall('${LOGON_USER.actn}',json.SE_EXT,"");
			makeNomalFormCall('<%=contextPath%>/customerRelationships/classMonitorManage/ClassMonitorManage/pullSta.json?actn='+json.actn+'&stat=95131002',pullSta,'fm','');
		}else{
			MyAlert("拦截失败！请联系管理员");
		}
	}
	
	//强制离席
	function forceMoveSeat(){
		var seIds = document.getElementsByName('seIds');
	    if(seIds.length > 0)
	    {
	       for(var i = 0 ;i < seIds.length ; i++)
	       {
	          if(seIds[i].checked)
	          {
	          	  parent.document.ut_atocx.ATSetLeaveSeat(seIds[i].value, 1, '');
	          	  makeNomalFormCall('<%=contextPath%>/customerRelationships/classMonitorManage/ClassMonitorManage/pullSta.json?actn='+seIds[i].value+'&stat=95131002',pullSta,'fm','');
	          }
	       }
	       
	    }
	}
	
	function forceMoveSeatBack(json) {
		if(json.success != null && json.success=='true'){
			MyAlertForFun("强制离席成功",sendPage);
		}else{
			MyAlert("强制离席失败！请联系管理员");
		}
	}
	
	//置闲
	function setFree(){
		var seIds = document.getElementsByName('seIds');
	    if(seIds.length > 0)
	    {
	       for(var i = 0 ;i < seIds.length ; i++)
	       {
	          if(seIds[i].checked)
	          {
	          	 parent.document.ut_atocx.ATSetBusy(seIds[i].value,0, '');
	          	 makeNomalFormCall('<%=contextPath%>/customerRelationships/classMonitorManage/ClassMonitorManage/pullSta.json?actn='+seIds[i].value+'&stat=95131002',pullSta,'fm','');
	          }
	         
	       }
	    }
	}
	
	function setFreeBack(json) {
		if(json.success != null && json.success=='true'){
			MyAlertForFun("置闲成功",sendPage);
		}else{
			MyAlert("置闲失败！请联系管理员");
		}
	}
	//置忙
	function setBusy()
	{
	    var seIds = document.getElementsByName('seIds');
	    if(seIds.length > 0)
	    {
	       for(var i = 0 ;i < seIds.length ; i++)
	       {
	          if(seIds[i].checked)
	          {
	          	  parent.document.ut_atocx.ATSetBusy(seIds[i].value, 1, '');
	          	  makeNomalFormCall('<%=contextPath%>/customerRelationships/classMonitorManage/ClassMonitorManage/pullSta.json?actn='+seIds[i].value+'&stat=95131005',pullSta,'fm','');
	          }
	         
	       }
	    }
		
	}
	
	function setBusyBack(json) {
		if(json.success != null && json.success=='true'){
			MyAlertForFun("置忙成功",sendPage);
		}else{
			MyAlert("置忙失败！请联系管理员");
		}
	}
	
	function sendPage(){
		window.location.href = "<%=contextPath%>/customerRelationships/classMonitorManage/ClassMonitorManage/classMonitorManageInit.do";
	}

</script>


</body>
</html>
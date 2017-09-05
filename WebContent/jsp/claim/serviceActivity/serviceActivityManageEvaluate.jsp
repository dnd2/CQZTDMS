<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="com.infodms.dms.po.TtAsActivityEvaluatePO"%>

<head>
<%
TtAsActivityEvaluatePO evaluatePO =(TtAsActivityEvaluatePO)request.getAttribute("evaluatePO");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动总结查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
function doInit()
	{
	   loadcalendar();
	}
</script>
</head>

<body>
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动评估
	</div>
  <form method="post" name="fm" id="fm">
        <table width="100%" class="tab_list">
          <tr>
            <td>服务站代码</td>   
            <td>服务站名称</td> 
            <td>申请金额</td> 
            <td>结算金额</td> 
            <td>评估等级</td> 
          </tr> 
          <% int i = 1; %>
          <c:forEach var="Evaluate" items="${Evaluate}">
          <tr>
            <td>${Evaluate.DEALER_CODE}<input type="hidden" name="DEALER_ID<%= i %>" value="${Evaluate.DEALER_ID}" ></td>   
            <td>${Evaluate.DEALER_NAME}<input type="hidden" name="SUBJECT_ID<%= i %>" value="${Evaluate.SUBJECT_ID}" ></td> 
            <td>${Evaluate.REPAIR_TOTAL}<input type="hidden" name="REPAIR_TOTAL<%= i %>" id="REPAIR_TOTAL<%= i %>" value="${Evaluate.REPAIR_TOTAL}" ></td> 
            <td><input type="text" datatype="0,is_double" decimal="2"  name="EVALUATE_AMOUNT<%= i %>" id="EVALUATE_AMOUNT<%= i %>" value="${Evaluate.EVALUATE_AMOUNT}" ></td> 
            <td> <script type="text/javascript">
					genSelBoxExp001("EVALUATE_TYPE<%= i %>",<%=Constant.EVALUATE_TYPE%>,"${Evaluate.EVALUATE_RES}",true,"short_sel","","true",'');
 				</script>
 				<input type="hidden" id="oder<%= i %>"  value="${Evaluate.EVALUATE_RES}" />
 				<input type="hidden" id="jude<%= i %>" name="jude<%= i %>" value="0">
			</td> 
           </tr>
           <% i = i+1; %>
          </c:forEach>
          <tr>
            <td colspan="6">
            	 <input type="button" class="normal_btn" value="保存" onclick="baoChu()" >
            	 <input type="button" class="normal_btn" value="上报技术室" onclick="commit()" >
            	 <INPUT class=normal_btn onclick="javascript:history.go(-1)" value=返回 type=button name=bt_back>
            </td>
          </tr>
        </table>
       <input type="hidden" name="day" id="day" value="${day}" >
        <input type="hidden" name="iconunt" value="<%= i %>" >
</form>
 <br/>
 <script type="text/javascript" >
   function infor(DEALER_ID,SUBJECT_ID)
   {
		var tarUrl = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageSummarySearch/serviceActivityManageSummaryEvaluateInfor.do?DEALER_ID="+DEALER_ID+"&SUBJECT_ID="+SUBJECT_ID;
		var width=200;
		var height=200;
		var screenW = window.screen.width-700;	
		var screenH = document.viewport.getHeight()-200;
		if(screenW!=null && screenW!='undefined')
			width = screenW;
		if(screenH!=null && screenH!='undefined')
			height = screenH;
		
		OpenHtmlWindow(tarUrl,width,height);
   		
   }
    function commit()
    {
    	if(!submitForm('fm')) 
    	{
				return false;
		}
    	for(var j= 1 ;j < <%= i%> ;j++)
    	{
    	    var REPAIR_TOTAL= document.getElementById('REPAIR_TOTAL'+j).value;
    	    var EVALUATE_AMOUNT= document.getElementById('EVALUATE_AMOUNT'+j).value;
    	    if(Number(REPAIR_TOTAL) < Number(EVALUATE_AMOUNT))
    	    {
    	        MyAlert('评估金额不能大于申请金额');
    			return;
    	    } 
    		var EVALUATE_TYPE= document.getElementById('EVALUATE_TYPE'+j).value;
    		if(EVALUATE_TYPE.length == 0)
    		{
    			MyAlert('请选择评估等级');
    			return;
    		}
    	}
    	 if(document.getElementById('day').value == 'no')
    	 {
    	 	MyAlert('还没有到上报日期 ！');
    	   return;
    	}
    	 MyConfirm("是否确认上报？",commitall);
    }
    function commitall()
    {
    	makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageSummarySearch/serviceActivityManageSummaryEvaluateCommit.json',commitBack,'fm','');
    }
    
    function commitBack(json)
    {
    	if(json.msg != null && json.msg == "yes") {
		MyAlertForFun("上报成功！",function(){
			location = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageSummarySearch/serviceActivityEvaluate.do";
		});
		} else {
			MyAlert("上报失败！请联系管理员！");
		}
    }
    
    function baoChu()
    {
    	if(!submitForm('fm')) 
    	{
			return false;
		}
   	
    	for(var j= 1 ;j < <%= i%> ;j++)
    	{
    	    
    	    var REPAIR_TOTAL= document.getElementById('REPAIR_TOTAL'+j).value;
    	    var EVALUATE_AMOUNT= document.getElementById('EVALUATE_AMOUNT'+j).value;
    	    if(Number(REPAIR_TOTAL) < Number(EVALUATE_AMOUNT))
    	    {
    	        MyAlert('评估金额不能大于申请金额');
    			return;
    	    }
    	    
    		var EVALUATE_TYPE= document.getElementById('EVALUATE_TYPE'+j).value;
    		if(EVALUATE_TYPE.length == 0)
    		{
    			MyAlert('请选择评估等级');
    			return;
    		}
    	}
    	 MyConfirm("是否确认保存？",bao);
    }
    function bao()
    {
    	makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageSummarySearch/serviceActivityManageSummaryEvaluateUpdate.json',updateBack,'fm','');
    }
    function updateBack(json)
    {
    	if(json.msg != null && json.msg == "yes") {
		MyAlertForFun("保存成功！",function(){
			location = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageSummarySearch/serviceActivityEvaluate.do";
		});
		} else {
			MyAlert("保存失败！请联系管理员！");
		}
    }
 	//function doCusChange(obj)
 	//{
 		//var tr= obj.parentNode.parentNode;
 		//var n =  tr.rowIndex;
 		//if(obj.value == '94101001')
 	//	{
 			
 			//document.getElementById('jude'+n).value = '0';
 		 //	document.getElementById('EVALUATE_AMOUNT'+n).value = '';
 		//}else if(obj.value == '94101002')
 	//	{
 			//if(document.getElementById('oder' + n).value == '94101002')
 			//{
 				//document.getElementById('jude'+n).value = '1';
 			//}else if(document.getElementById('oder' + n).value == '94101003')
 		//	{
 				//document.getElementById('jude'+n).value = '2';
 			//}else
 		//	{
 				//document.getElementById('jude'+n).value = '0';
 			//}
 			//document.getElementById('EVALUATE_AMOUNT'+n).value = ''; 
 	//	}else
 	//	{
 		//	document.getElementById('jude'+n).value = '0';
 			//document.getElementById('EVALUATE_AMOUNT'+n).value = '';
 		//}
 		 
 	//}
 </script>
</body>
</html>
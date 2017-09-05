<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    int yes = Constant.IF_TYPE_YES;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/common.js"></script>
<script type="text/javascript">
	 function doInit(){
		loadcalendar();   //初始化时间控件
	} 
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
</script>

<script type="text/javascript"> 
//定时刷新提醒信息
window.setTimeout(showalert,1000);
window.setInterval(showalert, 60000); 
	function showMyAlert() 
	{
		var remindInfo = document.getElementById("remindInfo");
		var url = "<%=contextPath%>/crm/taskmanage/TaskManage/getRemindInfo.json";
		makeFormCall(url, showInfo, "fm") ;
		
		function showInfo(json) {
			var nowDate = new Date();
			
			if(json.ps[0]!=null) {
				var size = json.ps.length;
				var str = "";
				for(var i=0;i<size;i++) {
					var d1 = new Date().Format("yyyy-MM-dd");  
					var d2 = new Date(json.ps[i].REMIND_DATE.replace(/\-/g, "\/")).Format("yyyy-MM-dd");
					if(d1<=d2) {
						if(json.ps[i].REMIND_TYPE=='60261001') {//线索分派
							str = str+"<div><span>您有一条 "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToLeadsAllotUrl()'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261002') {//客户信息补录
							str = str+"<div><span>您有一条 "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToConfirmUrl(null,60141002,"+json.ps[i].BEREMIND_ID+")'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261003') {//(车厂)线索确认
							str = str+"<div><span>您有一条 "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToConfirmUrl(null,60141001,"+json.ps[i].BEREMIND_ID+")'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261004') {//(DCRC)线索确认
							str = str+"<div><span>您有一条 "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToConfirmUrl(null,60141002,"+json.ps[i].BEREMIND_ID+")'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
// 						if(json.ps[i].REMIND_TYPE=='60261005') {//新增跟进任务
// 							str = str+"<div><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToFollowUrl("+json.ps[i].BEREMIND_ID+","+json.ps[i].CUSTOMER_ID+")'>待处理!</a></span></div>";
// 						}
// 						if(json.ps[i].REMIND_TYPE=='60261006') {//新增计划邀约任务
// 							str = str+"<div><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToInviteUrl("+json.ps[i].BEREMIND_ID+",null,"+json.ps[i].CUSTOMER_ID+")'>待处理!</a></span></div>";
// 						}
// 						if(json.ps[i].REMIND_TYPE=='60261007') {//新增邀约到店任务
// 							str = str+"<div><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToInviteUrl(null,"+json.ps[i].BEREMIND_ID+","+json.ps[i].CUSTOMER_ID+")'>待处理!</a></span></div>";
// 						}
// 						if(json.ps[i].REMIND_TYPE=='60261008') {//新增订单任务
// 							str = str+"<div><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToOrderUrl("+json.ps[i].BEREMIND_ID+","+json.ps[i].CUSTOMER_ID+",60231001)'>待处理!</a></span></div>";
// 						}
// 						if(json.ps[i].REMIND_TYPE=='60261009') {//新增交车任务
// 							str = str+"<div><span>"+json.ps[i].CUSTOMER_NAME+", 该客户有 "+json.ps[i].REMIND_NUM+"条 "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToDeliveryUrl(null,"+json.ps[i].BEREMIND_ID+","+json.ps[i].CUSTOMER_ID+")'>待处理!</a></span></div>";
// 						}
						if(json.ps[i].REMIND_TYPE=='60261010') {//新增战败审核
							str = str+"<div><span>"+json.ps[i].CUSTOMER_NAME+", 该客户 "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToDefeatUrl()'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261011') {//新增失效审核
							str = str+"<div><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToDefeatUrl()'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
// 						if(json.ps[i].REMIND_TYPE=='60261012') {//新增回访
// 							str = str+"<div><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToRevisitUrl("+json.ps[i].BEREMIND_ID+")'><font color=red><font color=red>待处理!</font></a></span></div>";
// 						}
						if(json.ps[i].REMIND_TYPE=='60261013') {//修改订单审核
							str = str+"<div><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToOrderAuditUrl()'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261014') {//退订单审核
							str = str+"<div><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToOrderAuditUrl()'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261015') {//新增邀约计划审核
							str = str+"<div><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToInviteAuditUrl()'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261016') {//新增修改邀约计划
							str = str+"<div><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToInviteUpdateUrl()'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261018') {//新增退订单重启任务
							str = str+"<div><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToOrderUrl("+json.ps[i].BEREMIND_ID+","+json.ps[i].CUSTOMER_ID+",60231007)'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261020') {//新增重复线索
							str = str+"<div><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToCustomerUrl("+json.ps[i].BEREMIND_ID+","+json.ps[i].CUSTOMER_ID+")'><font color=red><font color=red>待查看!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261021') {//修改订单审核
							str = str+"<div><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToOrderAmountAuditUrl()'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
					} else {
						if(json.ps[i].REMIND_TYPE=='60261001') {//线索分派
							str = str+"<div style='color:red'><span>您有一条 "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+",逾期, <a href='#' onclick='goToLeadsAllotUrl()'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261002') {//客户信息补录
							str = str+"<div style='color:red'><span>您有一条 "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+",逾期, <a href='#' onclick='goToConfirmUrl(null,60141002,"+json.ps[i].BEREMIND_ID+")'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261003') {//(车厂)线索确认
							str = str+"<div style='color:red'><span>您有一条 "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+",逾期, <a href='#' onclick='goToConfirmUrl(null,60141001,"+json.ps[i].BEREMIND_ID+")'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261004') {//(DCRC)线索确认
							str = str+"<div style='color:red'><span>您有一条 "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+",逾期, <a href='#' onclick='goToConfirmUrl(null,60141002,"+json.ps[i].BEREMIND_ID+")'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261005') {//新增跟进任务
							str = str+"<div style='color:red'><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+",逾期, "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToFollowUrl("+json.ps[i].BEREMIND_ID+","+json.ps[i].CUSTOMER_ID+")'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261006') {//新增计划邀约任务
							str = str+"<div style='color:red'><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+",逾期, "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToInviteUrl("+json.ps[i].BEREMIND_ID+",null,"+json.ps[i].CUSTOMER_ID+")'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261007') {//新增邀约到店任务
							str = str+"<div style='color:red'><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+",逾期, "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToInviteUrl(null,"+json.ps[i].BEREMIND_ID+","+json.ps[i].CUSTOMER_ID+")'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261008') {//新增订单任务
							str = str+"<div style='color:red'><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+",逾期, "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToOrderUrl("+json.ps[i].BEREMIND_ID+","+json.ps[i].CUSTOMER_ID+",60231001,"+json.ps[i].TELEPHONE+")'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261009') {//新增交车任务
							str = str+"<div style='color:red'><span>"+json.ps[i].CUSTOMER_NAME+", 该客户有 "+json.ps[i].REMIND_NUM+"条 "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+",逾期, <a href='#' onclick='goToDeliveryUrl(null,"+json.ps[i].BEREMIND_ID+","+json.ps[i].CUSTOMER_ID+")'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261010') {//新增战败审核
							str = str+"<div style='color:red'><span>"+json.ps[i].CUSTOMER_NAME+", 该客户 "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+",逾期, <a href='#' onclick='goToDefeatUrl()'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261011') {//新增失效审核
							str = str+"<div style='color:red'><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+",逾期, <a href='#' onclick='goToDefeatUrl()'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261012') {//新增回访
							str = str+"<div style='color:red'><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+",逾期, <a href='#' onclick='goToRevisitUrl("+json.ps[i].BEREMIND_ID+")'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261013') {//修改订单审核
							str = str+"<div style='color:red'><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+",逾期, <a href='#' onclick='goToOrderAuditUrl()'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261014') {//退订单审核
							str = str+"<div style='color:red'><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+",逾期, <a href='#' onclick='goToOrderAuditUrl()'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261015') {//新增邀约计划审核
							str = str+"<div style='color:red'><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+",逾期, <a href='#' onclick='goToInviteAuditUrl()'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261016') {//新增修改邀约计划
							str = str+"<div style='color:red'><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+",逾期, <a href='#' onclick='goToInviteUpdateUrl()'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261018') {//新增退订单重启任务
							str = str+"<div style='color:red'><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+",逾期, <a href='#' onclick='goToOrderUrl("+json.ps[i].BEREMIND_ID+","+json.ps[i].CUSTOMER_ID+",60231007)'><font color=red><font color=red>待处理!</font></a></span></div>";
						}
						if(json.ps[i].REMIND_TYPE=='60261020') {//新增重复线索
							str = str+"<div><span>"+json.ps[i].CUSTOMER_NAME+", 该客户  "+json.ps[i].REMIND_TYPE_NAME+", "+json.ps[i].REMIND_DATE+", <a href='#' onclick='goToCustomerUrl("+json.ps[i].BEREMIND_ID+","+json.ps[i].CUSTOMER_ID+")'><font color=red><font color=red>待查看!</font></a></span></div>";
						}
					}
				}
				remindInfo.innerHTML = str;
			} else {
				remindInfo.innerHTML = "<div style='margin: 0 auto;text-align: center;'><span>没有任何提醒信息!</span></div>";
			}
		}
	} 
	//定时刷新提醒信息
	window.setTimeout(overviewInfo,1000);
	window.setInterval(overviewInfo, 50000); 
		function overviewInfo() 
		{
			var overviewInfo = document.getElementById("overviewInfo");
			var url = "<%=contextPath%>/crm/taskmanage/TaskManage/overviewInfo.json";
			makeFormCall(url, showInfo, "fm") ;
			function showInfo(json) {
				if(json.ps[0]!=null) {
					var size = json.ps.length;
					var str  = "<table class='new_table' border=0 align=center bgcolor='#44BBBB' style='width:100%; margin-left: -15px; margin-top: -15px;'>";
					    str += "<tr style='color: #416C9B;height: 23px;background-color: #DAE0EE;font-weight: bold;'>";
					    str += "<td>销售顾问</td><td>逾期数量</td><td>正常数量</td></tr>";
					for(var i=0;i<size;i++) {
							if(i%2){
								str +=  "<TR id='moveOn'><TD>"+json.ps[i].NAME+"</td><td><a href='#' onclick='showOverviewInfo("+json.ps[i].ADVISER+",1)'>"+json.ps[i].OUTS+"条</a></td> <td><a href='#' onclick='showOverviewInfo("+json.ps[i].ADVISER+",0)'>"+json.ps[i].NORMALS+"条</a></td> </tr>";
							}else{
								str +=  "<TR style='height: 23px;background-color: rgb(247, 247, 247);'><TD>"+json.ps[i].NAME+"</td><td><a href='#' onclick='showOverviewInfo("+json.ps[i].ADVISER+",1)'>"+json.ps[i].OUTS+"条</a></td><td><a href='#' onclick='showOverviewInfo("+json.ps[i].ADVISER+",0)'>"+json.ps[i].NORMALS+"条</a></td> </tr>";
							}
					}
					
					str +="	</table>";
					
					 overviewInfo.innerHTML = str;
				} else {
					overviewInfo.innerHTML = "<div style='margin: 0 auto;text-align: center;'><span>没有任何提醒信息!</span></div>";
				}
			}
		} 	
	//比较日前大小  
	function compareDate(checkStartDate, checkEndDate) { 
	    var arys1= new Array();      
	    var arys2= new Array();      
	if(checkStartDate != null && checkEndDate != null) {      
	    arys1=checkStartDate.split('-');      
	      var sdate=new Date(arys1[0],parseInt(arys1[1]-1),arys1[2]);      
	    arys2=checkEndDate.split('-');      
	    var edate=new Date(arys2[0],parseInt(arys2[1]-1),arys2[2]);     
	if(sdate > edate) {      
	    return 'no';         
	}  else {   
	    return 'yes';      
	    }   
	   }      
	}    
</script> 


<title>日程管理</title>
</head>
<body onload="switchTab(${switchtab})" onunload='javascript:destoryPrototype();' > 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/crm/nav.gif" />&nbsp;当前位置>潜客管理>日程管理>任务管理</div>
	<div class="contant">
		<div class="top">
			<div class="top_left" style="float: left; width:100%; height: 320px">
				<div class="chaxuntiaojian" style="margin-left: 30px">
					<form id="fm" name="fm" method="post">
						查询日期从：<input name="startDate"  id="startDate" value="${startDate }"  type="text"  class="short_txt"  datatype="1,is_date,10" group="startDate,endDate" />
						<input style="margin-left: -4px;" class="time_ico" type="button" onclick="showcalendar(event, 'startDate', false);" />
	            		&nbsp;到&nbsp;
	            		<input name="endDate"  id="endDate" value="${endDate }" type="text" class="short_txt" datatype="1,is_date,10" group="startDates,endDate" />
	            		<input style="margin-left: 0px" class="time_ico" type="button" onclick="showcalendar(event, 'endDate', false)" /> 
	            		<input style="background: url('<%=contextPath%>/img/crm/find_button.gif'); border:none;width:45px;height:23px;vertical-align: middle" name="queryBtn" type="button" class="normal_btn" onclick="doQuery();" value="" />
					</form>
				</div>
				<img src="<%=contextPath%>/img/crm/hr.gif" style="margin-bottom: 5px;margin-top: 5px" width="95%"></img>
					<div class="task_tab" style="vertical-align: top">
						<div id="box">
							<ul id="tab">
								<li class="on" id="tab_1" onclick="switchTab(1)" value="1">线索(${leadsListNoSize }/${leadsListYesSize })</li>
								<li id="tab_2" onclick="switchTab(2)" value="2">跟(${followNoListSize }/${followYesListSize })</li>
								<li id="tab_3" onclick="switchTab(3)" value="3"  style="display: none;">邀(${inviteNoListSize }/${inviteYesListSize })</li>
								<li id="tab_4" onclick="switchTab(4)" value="4">订(${orderNoListSize }/${orderYesListSize })</li>
								<li id="tab_5" onclick="switchTab(5)" value="5">交(${deliveryNoListSize }/${deliveryYesListSize })</li>
								<li id="tab_6" onclick="switchTab(6)" value="6">访(${revisitNoListSize }/${revisitYesList })</li>
							</ul>
							<ul id="tab_con" style="overflow:scroll;height:200px;overflow-x:hidden;">
								<li id="tab_con_1">
								<table class="new_table" border=0 align=center style="width:101%; margin-left: -15px; margin-top: -15px;" >
									<tr style="color: #416C9B;height: 23px; background-color: #DAE0EE;font-weight: bold;">
										<td>客户姓名</td><td>手机</td><td>到店(电)时间</td><td>离店(电)时间</td><td>客户描述</td><td>来源</td><td>顾问</td><td>分派时间</td>
									</tr>
									<c:forEach items="${leadsList }" var="leadsList" varStatus="status">
									<c:if test="${status.index % 2==0 }">
										<tr id="moveOn" style="height: 23px;">
									</c:if>
									<c:if test="${status.index % 2!=0 }">
										<tr style="height: 23px;background-color: rgb(247, 247, 247);">
									</c:if>
											<td>${leadsList.CUSTOMER_NAME }</td>
											<td>${leadsList.TELEPHONE }</td>
											<td>${leadsList.COME_DATE }</td>
											<td>${leadsList.LEAVE_DATE }</td>
											<td>${leadsList.CUSTOMER_DESCRIBE }</td>
											<td>${leadsList.CODE_DESC }</td>
											<td>${leadsList.NAME }</td>
											<td><a href="#" id="LEADS_ALLOT_ID" name="LEADS_ALLOT_ID" onclick='goToConfirmUrl("${leadsList.LEADS_CODE}","${leadsList.LEADS_TYPE }","${leadsList.LEADS_ALLOT_ID }")'>${leadsList.ALLOT_ADVISER_DATE }</a></td>
										</tr>
									</c:forEach>
								</table>
								</li>
								<li id="tab_con_2">
								<table class="new_table" border=0 align=center bgcolor="#44BBBB" style="width:101%; margin-left: -15px; margin-top: -15px;" >
									<tr style="color: #416C9B;height: 23px;background-color: #DAE0EE;font-weight: bold;">
										<td>客户姓名</td><td>手机</td><td>意向车型</td><td>意向等级</td><td>顾问</td><td>下次跟进时间</td>
									</tr>
									<c:forEach items="${followList }" var="followList" varStatus="status">
									<c:if test="${followList.RESTART_TYPE == '60521002' || followList.RESTART_TYPE == '60521003'}">
										<tr style="height: 23px;background-color: #f7e845;border: 0; border-color:#44BBBB;">
											<td>${followList.CUSTOMER_NAME }</td>
											<td>${followList.TELEPHONE }</td>
											<td>${followList.SERIES_NAME }</td>
											<td>${followList.CODE_DESC }</td>
											<td>${followList.NAME }</td>
											<td><a href="#" id="FOLLOW_ID" name="FOLLOW_ID" onclick='goToFollowUrl("${followList.FOLLOW_ID }","${followList.CUSTOMER_ID }")'>${followList.FOLLOW_DATE }</a></td>
										</tr>
									</c:if>
									<c:if test="${followList.RESTART_TYPE != '60521002' && followList.RESTART_TYPE != '60521003'}">
									<c:if test="${status.index % 2==0 }">
										<tr id="moveOn" style="height: 23px;">
									</c:if>
									<c:if test="${status.index % 2!=0 }">
										<tr style="height: 23px;background-color: rgb(247, 247, 247);">
									</c:if>
											<td>${followList.CUSTOMER_NAME }</td>
											<td>${followList.TELEPHONE }</td>
											<td>${followList.SERIES_NAME }</td>
											<td>${followList.CODE_DESC }</td>
											<td>${followList.NAME }</td>
											<td><a href="#" id="FOLLOW_ID" name="FOLLOW_ID" onclick='goToFollowUrl("${followList.FOLLOW_ID }","${followList.CUSTOMER_ID }")'>${followList.FOLLOW_DATE }</a></td>
										</tr>
									</c:if>
									</c:forEach>
								</table>
								</li>
								<li id="tab_con_3">
								<table class="new_table" border=0 align=center bgcolor="#44BBBB" style="width:101%; margin-left: -15px; margin-top: -15px;" >
									<tr style="color: #416C9B;height: 23px;background-color: #DAE0EE;font-weight: bold;">
										<td>客户姓名</td><td>手机</td><td>意向车型</td><td>意向等级</td><td>提醒方式</td><td>顾问</td><td>日期</td>
									</tr>
									<c:forEach items="${inviteList }" var="inviteList" varStatus="status">
									<c:if test="${inviteList.RESTART_TYPE == '60521002' || inviteList.RESTART_TYPE == '60521003'}">
										<tr style="height: 23px;background-color: #f7e845;border: 0; border-color:#44BBBB;">
											<td>${inviteList.CUSTOMER_NAME }</td>
											<td>${inviteList.TELEPHONE }</td>
											<td>${inviteList.SERIES_NAME }</td>
											<td>${inviteList.CODE_DESC }</td>
											<td>${inviteList.INVITE_TYPE }</td>
											<td>${inviteList.NAME }</td>
											<td><a href="#" id="INVITE_ID" name="INVITE_ID" onclick='goToInviteUrl("${inviteList.INVITE_ID}","${inviteList.INVITE_SHOP_ID }","${inviteList.CUSTOMER_ID }")'>${inviteList.PLAN_DATE }</a></td>
										</tr>
									</c:if>
									<c:if test="${inviteList.RESTART_TYPE != '60521002' && inviteList.RESTART_TYPE != '60521003'}">
									<c:if test="${status.index % 2==0 }">
										<tr id="moveOn" style="height: 23px;">
									</c:if>
									<c:if test="${status.index % 2!=0 }">
										<tr style="height: 23px;background-color: rgb(247, 247, 247);">
									</c:if>
											<td>${inviteList.CUSTOMER_NAME }</td>
											<td>${inviteList.TELEPHONE }</td>
											<td>${inviteList.SERIES_NAME }</td>
											<td>${inviteList.CODE_DESC }</td>
											<td>${inviteList.INVITE_TYPE }</td>
											<td>${inviteList.NAME }</td>
											<td><a href="#" id="INVITE_ID" name="INVITE_ID" onclick='goToInviteUrl("${inviteList.INVITE_ID}","${inviteList.INVITE_SHOP_ID }","${inviteList.CUSTOMER_ID }")'>${inviteList.PLAN_DATE }</a></td>
										</tr>
									</c:if>
									</c:forEach>
								</table>
								</li>
								<li id="tab_con_4">
								<table class="new_table" border=0 align=center bgcolor="#44BBBB" style="width:101%; margin-left: -15px; margin-top: -15px;" >
									<tr style="color: #416C9B;height: 23px;background-color: #DAE0EE;font-weight: bold;">
										<td>客户姓名</td><td>手机</td><td>意向车型</td><td>顾问</td><td>计划订车日期</td>
									</tr>
									<c:forEach items="${orderList }" var="orderList" varStatus="status">
									<c:if test="${orderList.ORDER_STATUS == '60231007'}">
										<tr style="height: 23px;background-color: #f7e845;border: 0; border-color:#44BBBB;">
											<td>${orderList.CUSTOMER_NAME }</td>
											<td>${orderList.TELEPHONE }</td>
											<td>${orderList.SERIES_NAME }</td>
											<td>${orderList.NAME }</td>
											<td><a href="#" id="ORDER_ID" name="ORDER_ID" onclick='goToOrderUrl("${orderList.ORDER_ID}","${orderList.CUSTOMER_ID }","${orderList.ORDER_STATUS }","${orderList.TELEPHONE }")'>${orderList.ORDER_DATE }</a></td>
										</tr>
									</c:if>
									<c:if test="${orderList.ORDER_STATUS != '60231007'}">
									<c:if test="${status.index % 2==0 }">
										<tr id="moveOn" style="height: 23px;">
									</c:if>
									<c:if test="${status.index % 2!=0 }">
										<tr style="height: 23px;background-color: rgb(247, 247, 247);">
									</c:if>
											<td>${orderList.CUSTOMER_NAME }</td>
											<td>${orderList.TELEPHONE }</td>
											<td>${orderList.SERIES_NAME }</td>
											<td>${orderList.NAME }</td>
											<td><a href="#" id="ORDER_ID" name="ORDER_ID" onclick='goToOrderUrl("${orderList.ORDER_ID}","${orderList.CUSTOMER_ID }","${orderList.ORDER_STATUS }","${orderList.TELEPHONE }")'>${orderList.ORDER_DATE }</a></td>
										</tr>
									</c:if>
									</c:forEach>
								</table>
								</li>
								<li id="tab_con_5">
								<table class="new_table" border=0 align=center bgcolor="#44BBBB" style="width:101%; margin-left: -15px; margin-top: -15px;" >
									<tr style="color: #416C9B;height: 23px;background-color: #DAE0EE;font-weight: bold;">
										<td>客户姓名</td><td>手机</td><td>购买车型</td><td>顾问</td><td>计划交车日期</td>
									</tr>
									<c:forEach items="${deliveryList }" var="deliveryList" varStatus="status">
									<c:if test="${status.index % 2==0 }">
										<tr id="moveOn" style="height: 23px;">
									</c:if>
									<c:if test="${status.index % 2!=0 }">
										<tr style="height: 23px;background-color: rgb(247, 247, 247);">
									</c:if>
											<td>${deliveryList.CUSTOMER_NAME }</td>
											<td>${deliveryList.TELEPHONE }</td>
											<td>${deliveryList.BUYMODEL }</td>
											<td>${deliveryList.NAME }</td>
											<td><a href="#" id="DETAIL_ID" name="DETAIL_ID" onclick='goToDeliveryUrl("${deliveryList.ORDER_ID}","${deliveryList.DETAIL_ID}","${deliveryList.CUSTOMER_ID }")'>${deliveryList.DELIVERY_DATE }</a></td>
										</tr>
									</c:forEach>
								</table>
								</li>
								<li id="tab_con_6">
								<table class="new_table" border=0 align=center bgcolor="#44BBBB" style="width:101%; margin-left: -15px; margin-top: -15px;" >
									<tr style="color: #416C9B;height: 23px;background-color: #DAE0EE;font-weight: bold;">
										<td>客户姓名</td><td>手机</td><td>购车日期</td><td>车型</td><td>回访类型</td><td>顾问</td><td>下次回访时间</td>
									</tr>
									<c:forEach items="${revisitList }" var="revisitList" varStatus="status">
									<c:if test="${status.index % 2==0 }">
										<tr id="moveOn" style="height: 23px;">
									</c:if>
									<c:if test="${status.index % 2!=0 }">
										<tr style="height: 23px;background-color: rgb(247, 247, 247);">
									</c:if>
											<td>${revisitList.CUSTOMER_NAME }</td>
											<td>${revisitList.TELEPHONE }</td>
											<td>${revisitList.BUY_DATE }</td>
											<td>${revisitList.SERIES_NAME }</td>
											<td>${revisitList.CODE_DESC }</td>
											<td>${revisitList.NAME }</td>
											<td><a href="#" id="REVISIT_ID" name="REVISIT_ID" onclick='goToRevisitUrl("${revisitList.REVISIT_ID }")'>${revisitList.REVISIT_DATE }</a></td>
										</tr>
									</c:forEach>
								</table>
								</li>
							</ul>
						</div>
					</div>
				</div>
				<!-- <div style="float: right; width:25%; height: 10px">
				    <ul id="xin" >
							<li id="xinz" >当月新增潜客等级统计</li>
					</ul>
				</div> -->
			<!-- <div class="top_right" style="display: inline-block; margin-top: 54px; height:238px">
				<div class="customer_info" >
					<div class="task_tab2" style="vertical-align: top">
							<div id="box2" style="margin-left: -15px; display: inline-block;">
								<ul id="tab2" >
									<li class="on" id="tab2_1" onclick="switchTab2(1)">顾问</li>
									<li id="tab2_2" onclick="switchTabX(2)">车系</li>
									<li id="tab2_3" onclick="switchTabQ(3)">渠道</li>
								</ul>
								<ul id="tab_con2" style="overflow:scroll;height:170px;overflow-x:hidden;">
									<li id="tab_con2_1">
									</li>
									<li id="tab_con2_2">
									</li>
									<li id="tab_con2_3">
									</li>
								</ul>
							</div>
						</div>
				</div>
			</div> -->
		</div>
		<div class="foot" style="margin-top: 10px">
			<div class="foot_left" style="float: left;display: inline-block; width:60%; line-height: 20px;height:200px">
				<div class="foot_left1" style="float: left; display: inline-block; width: 90%;">
					<div class="task_tab3" style="vertical-align: top" >
						<div id="box3" style="display: inline-block;">
							<ul id="tab3" >
								<li class="on" id="tab3_1" onclick="switchTab3(1)">销售排名</li>
							</ul>
							<ul id="tab_con3" style="overflow:scroll;height:130px;overflow-x:hidden;">
								<li id="tab_con3_1">
									<table class="new_table" border=0 align=center bgcolor="#44BBBB" style="width:102%; margin-left: -15px; margin-top: -15px; color:black;">
										<tr style="color: #416C9B;height: 23px;background-color: #DAE0EE;font-weight: bold;">
											<td>排名</td><td>顾问</td><td>数量</td>
										</tr>
										<c:forEach items="${rankingList }" var="rankingList" varStatus="status">
										<c:if test="${status.index % 2==0 }">
										<tr id="moveOn" style="height: 23px;">
										</c:if>
										<c:if test="${status.index % 2!=0 }">
										<tr style="height: 23px;background-color: rgb(247, 247, 247);">
										</c:if>
												<td>${rankingList.RANKING }</td>
												<td>${rankingList.ADVISER }</td>
												<td>${rankingList.CUT }</td>
											</tr>
										</c:forEach>
									</table>
								</li>
							</ul>
						</div>
					</div>
				</div>
				<!-- <div class="foot_left2" style="display: inline-block; width: 57%; padding-left: 10px">
					<div class="task_tab4" >
						<div id="box4" style="display: inline-block;">
							<ul id="tab4" >
								<li class="on" id="tab4_1" onclick="switchTab4(1)">车辆管理</li>
							</ul>
							<ul id="tab_con4" style="overflow:scroll;height:130px;overflow-x:hidden;">
								<li id="tab_con4_1">
								</li>
							</ul>
						</div>
					</div>
				</div> -->
			</div>
			<div class="foot_right">
				<div class="task_tab5" >
						<div id="box5" style="display: inline-block;width: 39%; line-height: 20px">
							<ul id="tab5" >
								<li class="on" id="tab5_1" onclick="switchTab5(1)" style="color:black;">提醒</li>
								<li id="tab5_2" onclick="switchTab5(2)" style="color:black;">总览</li>
							</ul>
							<ul id="tab_con5" style="overflow:scroll;height:130px;overflow-x:hidden;">
								<li id="tab_con5_1">
									<div id="remindInfo" style="font-size: 13px;color: black;"></div>
								</li>
								<li id="tab_con5_2" style="width: 100%;">
									<div id="overviewInfo" style="font-size: 13px; color: black;"></div>
								</li>
							</ul>
						</div>
				</div>
			</div>
		</div>
	</div>
</div>

<style type="text/css">
#box{width:97%;font-size:12px}
#box ul{margin:0;padding:0;list-style:none}
#box #tab{height:25px;padding-left:8px;border-bottom:1px solid #DAE0EE}
#box #tab li{width:75px;height:18px;padding-top:7px;margin-right:5px;text-align:center;float:left;background:#F7F7F7;cursor:pointer}
#box #tab li.on{width:82px;height:20px;padding-top:5px;border:1px solid #DAE0EE;border-bottom:none;color:black;background:#DAE0EE;top:1px}
#box #tab_con{border:1px solid #DAE0EE;border-top:none;padding:20px}
#box #tab_con li{display:none}
#box #tab_con #tab_con_1{display:block;}
#box2{width:100%;font-size:12px}
#box2 ul{margin:0;padding:0;list-style:none}
#box2 #tab2{height:25px;padding-left:8px;border-bottom:1px solid #DAE0EE}
#box2 #tab2 li{width:75px;height:18px;padding-top:7px;margin-right:5px;text-align:center;float:left;background:#F7F7F7;cursor:pointer}
#box2 #tab2 li.on{width:82px;height:19px;padding-top:5px;border:1px solid #DAE0EE;border-bottom:none;color:black;background:#DAE0EE;position:relative;top:1px}
#box2 #tab_con2{border:1px solid #DAE0EE;border-top:none;padding:20px}
#box2 #tab_con2 li{display:none}
#box2 #tab_con2 #tab_con2_1{display:block;}
#box3{width:100%;font-size:12px}
#box3 ul{margin:0;padding:0;list-style:none}
#box3 #tab3{height:25px;padding-left:8px;border-bottom:1px solid #DAE0EE}
#box3 #tab3 li{width:75px;height:18px;padding-top:7px;margin-right:5px;text-align:center;float:left;background:#F7F7F7;cursor:pointer}
#box3 #tab3 li.on{width:82px;height:19px;padding-top:5px;border:1px solid #DAE0EE;border-bottom:none;color:black;background:#DAE0EE;position:relative;top:1px}
#box3 #tab_con3{border:1px solid #DAE0EE;border-top:none;padding:20px}
#box3 #tab_con3 li{display:none}
#box3 #tab_con3 #tab_con3_1{display:block;}
#box4{width:100%;font-size:12px}
#box4 ul{margin:0;padding:0;list-style:none}
#box4 #tab4{height:25px;padding-left:8px;border-bottom:1px solid #DAE0EE}
#box4 #tab4 li{width:75px;height:18px;padding-top:7px;margin-right:5px;text-align:center;float:left;background:#F7F7F7;cursor:pointer}
#box4 #tab4 li.on{width:82px;height:19px;padding-top:5px;border:1px solid #DAE0EE;border-bottom:none;color:black;background:#DAE0EE;position:relative;top:1px}
#box4 #tab_con4{border:1px solid #DAE0EE;border-top:none;padding:20px}
#box4 #tab_con4 li{display:none}
#box4 #tab_con4 #tab_con4_1{display:block;}
#box5{width:100%;font-size:12px}
#box5 ul{margin:0;padding:0;list-style:none}
#box5 #tab5{height:25px;padding-left:8px;border-bottom:1px solid #DAE0EE;}
#box5 #tab5 li{width:75px;height:18px;padding-top:7px;margin-right:5px;text-align:center;float:left;background:#F7F7F7;cursor:pointer}
#box5 #tab5 li.on{width:82px;height:19px;padding-top:5px;border:1px solid #DAE0EE;border-bottom:none;color:black;background:#DAE0EE;position:relative;top:1px}
#box5 #tab_con5{border:1px solid #DAE0EE;border-top:none;padding:20px}
#box5 #tab_con5 li{display:none;float: left;}
#box5 #tab_con5 #tab_con5_1{display:block;}
.new_table {
border-collapse:collapse;
}
.new_table td{
border:1px solid #E7E7E7;
}
#moveOn {
	background-color:white;
}
#moveOn:hover {
	background-color:rgb(189, 237, 205);
}
</style>
<script type="text/javascript" > 
function switchTab(n){
	//当页面加载时，加载日期控件
	loadcalendar();
	if(n==null||n=="") {
		return false;
	}
	for(var i = 1; i <= 6; i++){
	document.getElementById("tab_" + i).className = "";
	document.getElementById("tab_con_" + i).style.display = "none";
	}
	document.getElementById("tab_" + n).className = "on";
	document.getElementById("tab_con_" + n).style.display = "block";
}
function switchTab2(n){
	for(var i = 1; i <= 3; i++){
	document.getElementById("tab2_" + i).className = "";
	document.getElementById("tab_con2_" + i).style.display = "none";
	}
	document.getElementById("tab2_" + n).className = "on";
	document.getElementById("tab_con2_" + n).style.display = "block";
	var seriesInfo=document.getElementById("tab_con2_1");
	var url = "<%=contextPath%>/crm/taskmanage/TaskManage/doUserInit.json";
	makeFormCall(url, showInfo, "fm") ;
	function showInfo(json) {
		if(json.ps[0]!=null) {
			var size = json.ps.length;
	var str="<table class='new_table' border=0 align=center bgcolor='#44BBBB' style='width:102%; margin-left: -15px; margin-top: -15px;'>";
	str +="<tr style='color: #416C9B;height: 23px;background-color: #DAE0EE;font-weight: bold;'>";
	str +="<td>顾问</td><td>O</td><td>H</td><td>A</td><td>B</td><td>C</td><td>E</td><td>L</td>";
	str +="</tr>";
	  for(var i=0;i<size;i++) {
	    	if(i%2){
	    		str +="<tr style='height: 23px;background-color: rgb(247, 247, 247);'>";
	    		str +="<td>"+json.ps[i].ADVISER+"</td><td>"+json.ps[i].O+"</td><td>"+json.ps[i].H+"</td><td>"+json.ps[i].A+"</td><td>"+json.ps[i].B+"</td><td>"+json.ps[i].C+"</td><td>"+json.ps[i].E+"</td><td>"+json.ps[i].L+"</td></tr>";
	    	}else {
				str +="<tr id='moveOn' style='height: 23px;'>";
				str +="<td>"+json.ps[i].ADVISER+"</td><td>"+json.ps[i].O+"</td><td>"+json.ps[i].H+"</td><td>"+json.ps[i].A+"</td><td>"+json.ps[i].B+"</td><td>"+json.ps[i].C+"</td><td>"+json.ps[i].E+"</td><td>"+json.ps[i].L+"</td></tr>";
	    	}
		}

		str +="	</table>";
		seriesInfo.innerHTML = str;

	}else{
		var std="<table class='new_table' border=0 align=center bgcolor='#44BBBB' style='width:102%; margin-left: -15px; margin-top: -15px;'>";
		std +="<tr style='color: #416C9B;height: 23px;background-color: #DAE0EE;font-weight: bold;'>";
		std +="<td>顾问</td><td>O</td><td>H</td><td>A</td><td>B</td><td>C</td><td>E</td><td>L</td>";
		std +="</tr>";
		std +="	</table>";
		seriesInfo.innerHTML = std;
		}
	}
	}
function switchTabX(n){
	for(var i = 1; i <= 3; i++){
	document.getElementById("tab2_" + i).className = "";
	document.getElementById("tab_con2_" + i).style.display = "none";
	}
	document.getElementById("tab2_" + n).className = "on";
	document.getElementById("tab_con2_" + n).style.display = "block";
	var seriesInfo=document.getElementById("tab_con2_2");
	var url = "<%=contextPath%>/crm/taskmanage/TaskManage/doSeriesInit.json";
	makeFormCall(url, showInfo, "fm") ;
	function showInfo(json) {
		if(json.ps[0]!=null) {
			var size = json.ps.length;
			var str="<table class='new_table' border=0 align=center bgcolor='#44BBBB' style='width:102%; margin-left: -15px; margin-top: -28px;'>";
			str +="<tr style='color: #416C9B;height: 23px;background-color: #DAE0EE;font-weight: bold;'>";
			str +="<td>车系</td><td>O</td><td>H</td><td>A</td><td>B</td><td>C</td><td>E</td><td>L</td>";
		    str +="</tr>'";
		    for(var i=0;i<size;i++) {
		    	if(i%2){
					str +=  "<TR style='height: 23px;background-color: rgb(247, 247, 247);'><TD>"+json.ps[i].SERIES_NAME+"</td><td>"+json.ps[i].O+"</td><td>"+json.ps[i].H+"</td><td>"+json.ps[i].A+"</td><td>"+json.ps[i].B+"</td><td>"+json.ps[i].C+"</td><td>"+json.ps[i].E+"</td><td>"+json.ps[i].L+"</td></tr>";
				}else{
					str +=  "<TR id='moveOn' style='height: 23px;'><TD>"+json.ps[i].SERIES_NAME+"</td><td>"+json.ps[i].O+"</td><td>"+json.ps[i].H+"</td><td>"+json.ps[i].A+"</td><td>"+json.ps[i].B+"</td><td>"+json.ps[i].C+"</td><td>"+json.ps[i].E+"</td><td>"+json.ps[i].L+"</td></tr>";
				}
				}
		
		str +="	</table>";
		seriesInfo.innerHTML = str;
			
		}else{
			var std="<table class='new_table' border=0 align=center bgcolor='#44BBBB' style='width:102%; margin-left: -15px; margin-top: -28px;'>";
			std +="<tr style='color: #416C9B;height: 23px;background-color: #DAE0EE;font-weight: bold;'>";
			std +="<td>车系</td><td>O</td><td>H</td><td>A</td><td>B</td><td>C</td><td>E</td><td>L</td>";
			std +="</tr>'";
			std +="	</table>";
			seriesInfo.innerHTML = std;
			}
	}
	}
function switchTabQ(n){
	for(var i = 1; i <= 3; i++){
	document.getElementById("tab2_" + i).className = "";
	document.getElementById("tab_con2_" + i).style.display = "none";
	}
	document.getElementById("tab2_" + n).className = "on";
	document.getElementById("tab_con2_" + n).style.display = "block";
	var dealerInfo=document.getElementById("tab_con2_3");
	var url = "<%=contextPath%>/crm/taskmanage/TaskManage/doDealerInit.json";
	makeFormCall(url, showInfo, "fm") ;
	function showInfo(json) {
		if(json.ps[0]!=null) {
			var size = json.ps.length;
			var str="<table class='new_table' border=0 align=center bgcolor='#44BBBB' style='width:102%; margin-left: -15px; margin-top: -28px;'>";
			str +="<tr style='color: #416C9B;height: 23px;background-color: #DAE0EE;font-weight: bold;'>";
			str +="<td>经销商等级</td><td>O</td><td>H</td><td>A</td><td>B</td><td>C</td><td>E</td><td>L</td>";
		    str +="</tr>'";
		    for(var i=0;i<size;i++) {
		    	if(i%2){
					str +=  "<TR style='height: 23px;background-color: rgb(247, 247, 247);'><TD>"+json.ps[i].DEALER_LEVEL+"</td><td>"+json.ps[i].O+"</td><td>"+json.ps[i].H+"</td><td>"+json.ps[i].A+"</td><td>"+json.ps[i].B+"</td><td>"+json.ps[i].C+"</td><td>"+json.ps[i].E+"</td><td>"+json.ps[i].L+"</td></tr>";
				}else{
					str +=  "<TR id='moveOn' style='height: 23px;'><TD>"+json.ps[i].DEALER_LEVEL+"</td><td>"+json.ps[i].O+"</td><td>"+json.ps[i].H+"</td><td>"+json.ps[i].A+"</td><td>"+json.ps[i].B+"</td><td>"+json.ps[i].C+"</td><td>"+json.ps[i].E+"</td><td>"+json.ps[i].L+"</td></tr>";
				}
				}
		
		str +="	</table>";
		dealerInfo.innerHTML = str;
			
		}else{
			var std="<table class='new_table' border=0 align=center bgcolor='#44BBBB' style='width:102%; margin-left: -15px; margin-top: -28px;'>";
			std +="<tr style='color: #416C9B;height: 23px;background-color: #DAE0EE;font-weight: bold;'>";
			std +="<td>经销商等级</td><td>O</td><td>H</td><td>A</td><td>B</td><td>C</td><td>E</td><td>L</td>";
			std +="</tr>'";
			std +="	</table>";
			seriesInfo.innerHTML = std;
			}
	}
	}
function switchTab3(n){
	for(var i = 1; i <= 1; i++){
	document.getElementById("tab3_" + i).className = "";
	document.getElementById("tab_con3_" + i).style.display = "none";
	}
	document.getElementById("tab3_" + n).className = "on";
	document.getElementById("tab_con3_" + n).style.display = "block";
	}
function switchTab4(n){
	for(var i = 1; i <= 1; i++){
	document.getElementById("tab4_" + i).className = "";
	document.getElementById("tab_con4_" + i).style.display = "none";
	}
	document.getElementById("tab4_" + n).className = "on";
	document.getElementById("tab_con4_" + n).style.display = "block";
	}
function switchTab5(n){
	for(var i = 1; i <= 2; i++){
		document.getElementById("tab5_" + i).className = "";
		document.getElementById("tab_con5_" + i).style.display = "none";
	}
	document.getElementById("tab5_" + n).className = "on";
	document.getElementById("tab_con5_" + n).style.display = "block";
}

</script>
<script type="text/javascript" > 
function doQuery(){
	//switchtab为回跳到指定的Tab
	var switchtab=null;
	var target = document.getElementById("tab");
	var lis = target.getElementsByTagName('li');
	for(var i=0; i<lis.length; i++){
		if(lis[i].className=='on'){
			switchtab = i+1;
		}
	}
	$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doInit.do?switchtab="+switchtab;
	$('fm').submit();
}
function goToConfirmUrl(leadsCode,leadsType,leadsAllotId){
	//判断线索类别
	//车厂导入
	if( leadsType=="60141001") {
		$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/leadsConfirmByOemInit.do?leadsCode="+leadsCode+"&leadsAllotId="+leadsAllotId;
		$('fm').submit();
		//DCRC录入
	} else {
		$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/leadsConfirmByDcrcInit.do?leadsCode="+leadsCode+"&leadsAllotId="+leadsAllotId;
		$('fm').submit();
	}
}
function goToFollowUrl(followId,customerId) {
	$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTaskFollowInit.do?customerId="+customerId+"&taskId="+followId;
	$('fm').submit();
}
function goToInviteUrl(inviteId,inviteShopId,customerId){
	$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTaskInviteInit.do?taskId="+inviteId+"&inviteShopId="+inviteShopId+"&customerId="+customerId;
	$('fm').submit();
}
function goToOrderUrl(orderId,customerId,orderStatus,tel){

		var flag=judgeIfAbleOderDate(tel);
		//if(!flag && (orderStatus!='60231007')){
		//	MyAlert("DCRC未录入该客户今日到店客流信息，不能做订单！！！");
		//	return;
		//}
	if(orderStatus!='60231007') {
		$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTaskOrderInit.do?taskId="+orderId+"&customerId="+customerId;
		$('fm').submit();
	} else {//退单情况
		$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTaskOrderBackInit.do?taskId="+orderId+"&customerId="+customerId+"&orderStatus="+orderStatus;
		$('fm').submit();
	}
}
//交车页面
function goToDeliveryUrl(orderId,detailId,customerId){
	$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTaskDeliveryInit.do?orderId="+orderId+"&taskId="+detailId+"&customerId="+customerId;
	$('fm').submit();
}
function goToRevisitUrl(revisit_id){
	$('fm').action = "<%=contextPath%>/crm/revisit/RevisitManage/detailRevisit.do?revisitId="+revisit_id+"&typeFrom=taskManage";
	$('fm').submit();
}
function goToLeadsAllotUrl(){
	$('fm').action = "<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dlrLeadsAllot.do";
	$('fm').submit();
}
function goToInviteAuditUrl(){
	$('fm').action = "<%=contextPath%>/crm/invite/InviteCheck/doInit.do";
	$('fm').submit();
}
function goToInviteUpdateUrl(){
	$('fm').action = "<%=contextPath%>/crm/invite/InviteManage/doInit.do";
	$('fm').submit();
}
function goToOrderAuditUrl(){
	$('fm').action = "<%=contextPath%>/crm/order/OrderManage/doAuditInit.do";
	$('fm').submit();
}
function goToOrderAmountAuditUrl(){
	$('fm').action = "<%=contextPath%>/crm/order/OrderManage/doAuditAmountInit.do";
	$('fm').submit();
}
function goToDefeatUrl(){
	$('fm').action = "<%=contextPath%>/crm/defeat/DefeatManage/doInit.do";
	$('fm').submit();
}
function goToCustomerUrl(beRemindId,customerId){
	$('fm').action = "<%=contextPath%>/crm/customer/CustomerManage/ctmUpdateInit.do?ctmId="+customerId+"&beRemindId="+beRemindId;
	$('fm').submit();
}
//添加信息"+son.ps[i].REMIND_TYPE+","+son.ps[i].CUSTOMER_ID+"
function showOverviewInfo(adviser,flag){
	if(adviser == null|| "" == adviser){
		MyAlert("！！！");
		return;
	}
	var url="<%=contextPath%>/crm/taskmanage/TaskManage/overviewInfoInit.do?adviser="+adviser+"&flag="+flag;
	OpenHtmlWindow(url,600,400);
}

</script>
<script language="javascript" for="window" event="onload">   
switchTab2(1);
</script> 
</body>
</html>
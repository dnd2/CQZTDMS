<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List"%>
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
		var msg = document.getElementById("errorMsg").value;
		if(msg!=null&&msg!="") {
			MyAlert(msg);
			msg="";
		}
		loadcalendar();   //初始化时间控件
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	function followClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var defeatTable = document.getElementById("defeat_table");
		var orderTable = document.getElementById("order_table");
		var failureTable = document.getElementById("failure_table");
		var intentType = document.getElementById("intent_type");
		var intentTypeh = document.getElementById("intent_typeh");
		intentType.value = '';
		intentTypeh.innerHTML = "--请选择--";
		//intentType.value = "${ctmRank}";
		//intentTypeh.innerHTML = "${ctmRank2}";
		followTable.style.display = "block";
		inviteTable.style.display = "none";
		defeatTable.style.display = "none";
		orderTable.style.display = "none";
		failureTable.style.display = "none";
		intentTypeh.setAttribute("notselected","60101005,60101006,60101007");
		intentTypeh.setAttribute("isload",false);
	}
	function inviteClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var defeatTable = document.getElementById("defeat_table");
		var orderTable = document.getElementById("order_table");
		var failureTable = document.getElementById("failure_table");
		var intentType = document.getElementById("intent_type");
		var intentTypeh = document.getElementById("intent_typeh");
		intentType.value = '';
		intentTypeh.innerHTML = "--请选择--";
		//intentType.value = "${ctmRank}";
		//intentTypeh.innerHTML = "${ctmRank2}";
		followTable.style.display = "none";
		inviteTable.style.display = "block";
		defeatTable.style.display = "none";
		orderTable.style.display = "none";
		failureTable.style.display = "none";
		intentTypeh.setAttribute("notselected","60101005,60101006,60101007");
		intentTypeh.setAttribute("isload",false);
	}
	function orderClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var defeatTable = document.getElementById("defeat_table");
		var orderTable = document.getElementById("order_table");
		var failureTable = document.getElementById("failure_table");
		var intentType = document.getElementById("intent_type");
		var intentTypeh = document.getElementById("intent_typeh");
		intentType.value = '60101005';
		intentTypeh.innerHTML = "O";
		followTable.style.display = "none";
		inviteTable.style.display = "none";
		defeatTable.style.display = "none";
		orderTable.style.display = "block";
		failureTable.style.display = "none";
		intentTypeh.setAttribute("notselected","60101004,60101006,60101007,60101001,60101002,60101003");
		intentTypeh.setAttribute("isload",false);
	}
	function defeatClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var defeatTable = document.getElementById("defeat_table");
		var orderTable = document.getElementById("order_table");
		var failureTable = document.getElementById("failure_table");
		var intentType = document.getElementById("intent_type");
		var intentTypeh = document.getElementById("intent_typeh");
		intentType.value = '60101006';
		intentTypeh.innerHTML = "E";
		followTable.style.display = "none";
		inviteTable.style.display = "none";
		defeatTable.style.display = "block";
		orderTable.style.display = "none";
		failureTable.style.display = "none";
		intentTypeh.setAttribute("notselected","60101004,60101005,60101007,60101001,60101002,60101003");
		intentTypeh.setAttribute("isload",false);
	}
	function failureClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var defeatTable = document.getElementById("defeat_table");
		var orderTable = document.getElementById("order_table");
		var failureTable = document.getElementById("failure_table");
		var intentType = document.getElementById("intent_type");
		var intentTypeh = document.getElementById("intent_typeh");
		intentType.value = '60101007';
		intentTypeh.innerHTML = "L";
		followTable.style.display = "none";
		inviteTable.style.display = "none";
		defeatTable.style.display = "none";
		orderTable.style.display = "none";
		failureTable.style.display = "block";
		intentTypeh.setAttribute("notselected","60101004,60101006,60101005,60101001,60101002,60101003");
		intentTypeh.setAttribute("isload",false);
	}
	function checkClick(){
		var check = document.getElementById("checkbox");
		var yaoyuejihua = document.getElementById("yaoyuejihua");
		var yaoyuejihua2 = document.getElementById("yaoyuejihua2");
		if(check.checked){
			yaoyuejihua.style.display = "block";
			yaoyuejihua2.style.display = "block";
			document.getElementById("xqfx").value = "";
			document.getElementById("yymb").value = "";
			document.getElementById("ydkhxrsj").value = "";
			document.getElementById("gdkhqjsj").value = "";
		} else {
			yaoyuejihua.style.display = "none";
			yaoyuejihua2.style.display = "none";
			document.getElementById("xqfx").value = "";
			document.getElementById("yymb").value = "";
			document.getElementById("ydkhxrsj").value = "";
			document.getElementById("gdkhqjsj").value = "";
		}
	}
	function doSave(){
		var salesProgress = document.getElementById("sales_progress").value;
		var inviteType = document.getElementById("invite_type").value;
		var inviteWay = document.getElementById("invite_way").value;
		var intentType = document.getElementById("intent_type").value;
		var followRadio = document.getElementById("follow_radio");
		var nextFollowDate = document.getElementById("next_follow_date").value;
		var telephone = document.getElementById("telephone").value;
		var inviteRadio = document.getElementById("invite_radio");
		var xqfx = document.getElementById("xqfx").value;
		var yymb = document.getElementById("yymb").value;
		var ydkhxrsj = document.getElementById("ydkhxrsj").value;
		var gdkhqjsj = document.getElementById("gdkhqjsj").value;
		var planInviteDate = document.getElementById("plan_invite_date").value;
		var followType = document.getElementById("follow_type").value;
		var planMeetDate = document.getElementById("plan_meet_date").value;
		var inviteTypeNew = document.getElementById("invite_type_new").value;
		var orderRadio = document.getElementById("order_radio");
		var orderDate = document.getElementById("order_date").value;
		var defeatRadio = document.getElementById("defeat_radio");
		var defeatVehicle = document.getElementById("defeatVehicleB").value;
		var defeatReason = document.getElementById("defeatReasonB").value;
		var defeatEndDate = document.getElementById("defeat_end_date").value;
		var failureRadio = document.getElementById("failure_radio");
		var failureDate = document.getElementById("failure_date").value;
		var failureRemark = document.getElementById("failure_remark").value;
		var ifInvite = document.getElementById("if_invite2").value;
		document.getElementById("if_invite").value = ifInvite;
		var inviteShopDate = document.getElementById("invite_shop_date").value;
		var reg = new RegExp("^[0-9]*$");
		var time = new Date().Format("yyyy-MM-dd");
		function getZ(n){    return /^\d+(\.\d+)?$/.test(n+"");}
		if(salesProgress ==null||salesProgress=="") {
			 MyAlert("请选择销售流程进度!");
	         return false;
		}
		//if(!reg.test(telePhone)){
	     //    MyAlert("联系电话格式不正确!");
	      //   return false;
	    //}
		//if(telePhone.length != 11 )
	    //{
	     //   MyAlert('请输入有效的联系电话！');
	      //  return false;
	   // } 
		if(intentType ==null||intentType=="") {
			 MyAlert("请选择意向等级!");
	         return false;
		}
		if(ifInvite==null||ifInvite=="") {
			MyAlert("请选择是否到店!");
			return false;
		}
		if(ifInvite=='10041001') {//到店
			
			if(inviteShopDate==null||inviteShopDate=="") {
				MyAlert("请选择到店时间!");
				return false;
			}
	 	 if(inviteShopDate < time){
			MyAlert("到店时间要大于当前时间");
			return false;
	 	 }
		}
		if(followRadio.checked == true) {
			var dd = new Date();
			var nextDate=parseDate(nextFollowDate);
			var inbound=document.getElementById("intent_typeh").innerHTML.split("<")[0];
			if(inbound == "A"){
				var da = new Date();
		    	da.setDate(da.getDate()+6);
				if(nextDate > da ){
					MyAlert("A等级跟进时间要小于7天");
					return false;
				}	
			}
			if(inbound == "B"){
				var db = new Date();
		    	db.setDate(db.getDate()+14);
				if(nextDate >  db ){
					MyAlert("B等级跟进时间要小于15天");
					return false;
				}
			}
			if(inbound == "C"){
				var dc = new Date();
		    	dc.setDate(dc.getDate()+29);
				if(nextDate > dc ){
					MyAlert("C等级跟进时间要小于30天");
					return false;
				}	
			}
			 if(inbound == "H"){
				    var dh = new Date();
			    	dh.setDate(dh.getDate()+2);
					if(nextDate > dh){
						MyAlert("H等级跟进时间要小于3天");
						return false;
					}	
			}
			if(nextFollowDate==null||nextFollowDate=="") {
				MyAlert("请选择下次跟进时间！");
				return false;
			} else if(followType==null||followType=="") {
				MyAlert("请选择跟进方式！");
				return false;
			}else if(nextDate < dd.setDate(dd.getDate()-1)){
				MyAlert("跟进时间要大于当前时间");
				return false;
			} else {
				var customerId = document.getElementById("customerId").value;
				var taskId = document.getElementById("taskId").value;
				var inviteShopId = document.getElementById("inviteShopId").value;
				document.getElementById("saveButton").disabled = true;
				document.getElementById("insertBtn").disabled = true;
				$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTask.do?taskType=inviteShop&typeFlag=followRadio&customerId="+customerId+"&taskId="+taskId+"&inviteShopId="+inviteShopId;
				$('fm').submit();
			}
		} else if(inviteRadio.checked == true) {
			
			var dd = new Date();
			var planIDate=parseDate(planInviteDate);
			var planMDate=parseDate(planMeetDate);
			var inbound=document.getElementById("intent_typeh").innerHTML.split("<")[0];
			if(inbound == "A"){
				var da = new Date();
		    	da.setDate(da.getDate()+6);
				if(planIDate > da ){
					MyAlert("A等级邀约时间要小于7天");
					return false;
				}	
			}
			if(inbound == "B"){
				var db = new Date();
		    	db.setDate(db.getDate()+14);
				if(planIDate >  db ){
					MyAlert("B等级邀约时间要小于15天");
					return false;
				}
			}
			if(inbound == "C"){
				var dc = new Date();
		    	dc.setDate(dc.getDate()+29);
				if(planIDate > dc ){
					MyAlert("C等级邀约时间要小于30天");
					return false;
				}	
			}
			 if(inbound == "H"){
				    var dh = new Date();
			    	dh.setDate(dh.getDate()+2);
					if(planIDate > dh){
						MyAlert("H等级邀约时间要小于3天");
						return false;
					}	
			}
			
			if(planInviteDate==null||planInviteDate=="") {
				MyAlert("请选择计划邀约时间");
				return false;
			}
			if(planMeetDate==null||planMeetDate=="") {
				MyAlert("请选择计划见面时间！");
				return false;
			}
			if(inviteTypeNew==null||inviteTypeNew=="") {
				MyAlert("请选择邀约方式！");
				return false;
			}
			if(planIDate < dd.setDate(dd.getDate()-1)){
				MyAlert("邀约时间要大于当前时间！");
				return false;
			}
			if(planMDate < planIDate){
				MyAlert("计划见面时间大于邀约时间！");
				return false;
			}
			var customerId = document.getElementById("customerId").value;
			var taskId = document.getElementById("taskId").value;
			var inviteShopId = document.getElementById("inviteShopId").value;
			document.getElementById("saveButton").disabled = true;
			document.getElementById("insertBtn").disabled = true;
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTask.do?taskType=inviteShop&typeFlag=inviteRadio&customerId="+customerId+"&taskId="+taskId+"&inviteShopId="+inviteShopId;
			$('fm').submit();
		} else if(orderRadio.checked == true) {
		
			var telephone=$("telephone").value;
			var flag=judgeIfAbleOder(telephone);
			if(!flag){
				MyAlert("该客户无首次到店客流信息，无法生成订车计划！！！");
				return;
			}
			if(orderDate==null||orderDate=="") {
				MyAlert("请选择订车时间！");
				return false;
			}
			if(orderDate < time){
				MyAlert("邀约时间要大于当前时间");
				return false;
			}
		 
			var customerId = document.getElementById("customerId").value;
			var taskId = document.getElementById("taskId").value;
			var inviteShopId = document.getElementById("inviteShopId").value;
			document.getElementById("saveButton").disabled = true;
			document.getElementById("insertBtn").disabled = true;
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTask.do?taskType=inviteShop&typeFlag=orderRadio&customerId="+customerId+"&taskId="+taskId+"&inviteShopId="+inviteShopId;;
			$('fm').submit();
		} else if(defeatRadio.checked == true) {
			if(defeatVehicle==null||defeatVehicle=="") {
				MyAlert("请选择战败车型！");
				return false;
			}
			if(defeatReason==null||defeatReason=="") {
				MyAlert("请选择战败原因！");
				return false;
			}
			if(defeatEndDate==null||defeatEndDate=="") {
				MyAlert("请选择战败结束日期！");
				return false;
			}
			var customerId = document.getElementById("customerId").value;
			var taskId = document.getElementById("taskId").value;
			document.getElementById("saveButton").disabled = true;
			document.getElementById("insertBtn").disabled = true;
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTask.do?taskType=inviteShop&typeFlag=defeatRadio&customerId="+customerId+"&taskId="+taskId;
			$('fm').submit();
		} else if(failureRadio.checked == true) {
			if(failureDate==null||failureDate=="") {
				MyAlert("请选择失效日期！");
				return false;
			}
			var customerId = document.getElementById("customerId").value;
			var taskId = document.getElementById("taskId").value;
			document.getElementById("saveButton").disabled = true;
			document.getElementById("insertBtn").disabled = true;
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTask.do?taskType=inviteShop&typeFlag=failureRadio&customerId="+customerId+"&taskId="+taskId;
			$('fm').submit();
		}
	}
	function parseDate(str)  
	{
	    return new Date(Date.parse(str.replace(/-/g,"/")));
	}
</script>
<title>邀约到店任务</title>
</head>
<body onunload='javascript:destoryPrototype();'>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>日程管理>任务管理>邀约到店任务
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="1" /> 
			<input type="hidden" id="dlrId" name="dlrId" value="" />
			<input type="hidden" name="customerId" id="customerId" value="${customerId }" />
			<input type="hidden" name="taskId" id="taskId" value="${taskId }" />
			<input type="hidden" name="inviteShopId" id="inviteShopId" value="${inviteShopId }" />
			<input type="hidden" name="errorMsg" id="errorMsg" value="${errorMsg }" />
			<input type="hidden" name="intentVehicle" id="intentVehicle" value="" />
			<input type="hidden" name="if_invite" id="if_invite" value="" />
			
			<div>
				<b style="display: inline-block; float: left">邀约计划</b><hr style="display: inline-block;">
			</div>
			<br>
			<table class="table_query" width="95%" align="center">
			<!-- 二级级联菜单 -->
			<script language="javascript">
				function doQuery(){
					MyAlert(document.getElementById("intentVehicleB").value);
				}
			</script>
			<c:forEach items="${customerList }" var="customerList">
				<tr>
					<td align="right" width="6%">客户编码：</td>
					<td><input id="customer_code" name="customer_code" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${customerList.CUSTOMER_CODE }"
						maxlength="60" /></td>
					<td align="right" width="14%">客户姓名：</td>
					<td><input id="customer_name" name="customer_name" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${customerList.CUSTOMER_NAME }"
						maxlength="60" /></td>
					<td align="right" width="14%">联系电话：</td>
					<td width="12%"><input id="telephone" name="telephone" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${customerList.TELEPHONE }"
						size="20" maxlength="60" /></td>
					<td align="right" width="6%">邀约方式：</td>
					<td>
					<input id="invite_type" name="invite_type" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${inviteType2 }"
						size="20" maxlength="60" />
					</td>
				</tr>
				
				<tr>
					<td align="right" width="12%">邀约类型：</td>
					<td>
					<input id="invite_way" name="invite_way" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${inviteWay2 }"
						size="20" maxlength="60" />
					</td>
					<td align="right" width="10%">是否邀约成功：</td>
					<td>
					<input id="oif_invite" name="oif_invite" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="是"
						size="20" maxlength="60" />
					</td>
					<td width="3%" align="right">预约到店时间：</td>
					<td width="22%">
						<div align="left">
							<input type="text" value="${inviteShopDate }" name="pre_invite_shop_date" id="pre_invite_shop_date" group="pre_invite_shop_date" style="background-color: #EEEEEE;"
								class="middle_txt" datatype="1,is_textarea,30" size="20" readonly="readonly"
								maxlength="60"  />
						</div>
					</td>
					<td align="right" width="15%">意向等级：</td>
					<td width="25%">
					<div align="left">
							<input type="text" value="${ctmRank2 }" name="ointent_type" id="ointent_type" group="pre_invite_shop_date" style="background-color: #EEEEEE;"
								class="middle_txt" datatype="1,is_textarea,30" size="20" readonly="readonly"
								maxlength="60"  />
						</div>
					</td>
				</tr>
				
				<tr>
					<td align="right" colspan="1">邀约内容：</td>
					<td align="left" colspan="5">
						<textarea rows="5" cols="70" id="invite_info" name="invite_info" readonly="readonly" style="background-color: #EEEEEE;">${inviteRemartk }</textarea>
					</td>
				</tr>
				<tr>
					 <td align="center" colspan="7">
						<a href="#" id="FOLLOW_ID" name="FOLLOW_ID" onclick='followOpenInfo("${customerId}")'>客户资料维护</a>
					</td>
				</tr>
				</c:forEach>
			</table>
			</br>
			<div>
				<b style="display: inline-block; float: left">到店结果</b><hr style="display: inline-block;">
			</div>
			</br>
			<table class="table_query" width="95%" align="center">
				<tr>
					<td align="right" width="8%">到店时间：</td>
					<td width="10%">
						<div align="left">
							<input type="text" name="invite_shop_date" id="invite_shop_date" group="invite_shop_date" 
								class="short_txt" datatype="1,is_date,10" size="20" hasbtn="true" readonly="readonly"
								maxlength="60" callFunction="showcalendar(event, 'invite_shop_date', false);" />
						</div>
					</td>
					<td align="right" width="8%">是否到店：</td>
					<td width="7%"><select id="if_invite2">
							<option id="yes" value="10041001">是</option>
							<option id="no" value="10041002">否</option>
						</select>
					</td>
					<td align="right" width="7%">意向等级：</td>
					<td width="12%">
					<input type="hidden" id="intent_type" name="intent_type" value="${ctmRank }"/>
		      		<div id="ddtopmenubar29" class="mattblackmenu">
						<ul> 
							<li>
								<a id="intent_typeh" style="width:103px;" rel="ddsubmenu29" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6010', loadIntentType);" notselected="60101005,60101006,60101007" deftitle="--请选择--">
								<c:if test="${ctmRank==null}">--请选择--</c:if><c:if test="${ctmRank!=null}">${ctmRank2}</c:if></a>
								<ul id="ddsubmenu29" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
					<td align="right" width="10%">销售流程进度：</td>
					<td>
					<input type="hidden" id="sales_progress" name="sales_progress" value="${salesProgress}"/>
		      		<div id="ddtopmenubar28" class="mattblackmenu">
						<ul> 
							<li>
								<a id="sales_progressh" style="width:103px;" rel="ddsubmenu28" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6037', loadSalesProgress2);" deftitle="--请选择--">
								<c:if test="${salesProgress==null}">--请选择--</c:if><c:if test="${salesProgress!=null}">${salesProgress2}</c:if></a>
								<ul id="ddsubmenu28" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
					<td align="right" width="8%">意向车型：</td>
					<td>
					<select id="intentVehicleA" onchange="toChangeMenuSelected(this,'intentVehicleB')" style="width: 70px">
						<c:forEach items="${menusAList }" var="blist">
							<c:if test="${upSeriesCode == blist.MAINID }" >
								<option id="all" value="${blist.MAINID }" selected="selected">${blist.NAME }</option>
							</c:if>
							<c:if test="${upSeriesCode != blist.MAINID }" >
								<option id="all" value="${blist.MAINID }">${blist.NAME }</option>
							</c:if>
						</c:forEach>
					</select>
					<c:forEach items="${customerList }" var="customerList">
					<select id="intentVehicleB" name="intentVehicleB" style="width: 100px">
						<c:forEach items="${menusABList2 }" var="blist">
							<c:if test="${customerList.INTENT_VEHICLE == blist.MAINID }" >
								<option id="all" value="${blist.MAINID }" selected="selected">${blist.NAME }</option>
							</c:if>
							<c:if test="${customerList.INTENT_VEHICLE != blist.MAINID }" >
								<option id="all" value="${blist.MAINID }">${blist.NAME }</option>
							</c:if>
						</c:forEach>
					</select>
					</c:forEach>
					</td>
				</tr>
				<tr>
					<td align="right" colspan="1">到店结果：</td>
					<td align="left" colspan="5">
						<textarea rows="5" cols="70" id="invite_shop_info" name="invite_shop_info"></textarea>
					</td>
				</tr>
			</table>
			</br>
			</br>
			<div>
				<b style="display: inline-block; float: left">下次计划任务</b><hr style="display: inline-block;">
			</div>
			<table class="table_query" width="95%" align="center" id="groupRadio">
				<tr>
					<td align="right" width="20%">
						<input type="radio"  id="follow_radio" name="group_radio" onclick="followClick()" checked="checked">跟进</input>
						<input type="radio"  id="invite_radio" name="group_radio" onclick="inviteClick()">邀约</input>
						<input type="radio"  id="order_radio" name="group_radio" onclick="orderClick()">订车</input>
					</td>
					<td width="18%">
						<input type="radio"  id="defeat_radio" name="group_radio" onclick="defeatClick()">战败</input>
						<input type="radio"  id="failure_radio" name="group_radio" onclick="failureClick()">失效</input>
					</td>
				</tr>
			</table>
			</br>
			<table class="table_query" width="95%" align="center" id="follow_table" >
				<tr>
					<td width="6%" align="right">下次跟进时间：</td>
					<td width="22%">
						<div align="left">
							<input name="next_follow_date" id="next_follow_date" readonly="readonly" value="" type="text" class="short_txt" datatype="1,is_date,10" group="startDate,endDate" />
							<input id="next_follow_date2" name="next_follow_date2" style="margin-left: -4px;" class="time_ico" type="button"  onclick="changeEvent()";  />
						</div>
					</td>
					<td width="6%" align="right">跟进方式：</td>
					<td width="22%">
						<input type="hidden" id="follow_type" name="follow_type" value=""/>
		      			<div id="ddtopmenubar27" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:103px;" rel="ddsubmenu27" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6046', loadFollowType);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu27" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
				</tr>
				<tr>
					<td width="6%" align="right">跟进计划：</td>
					<td align="left" colspan="6">
						<textarea rows="5" cols="70" id="follow_plan" name="follow_plan"></textarea>
					</td>
				</tr>
			</table>
			
			<table class="table_query" width="95%" align="center" id="invite_table" style="display: none">
				<tr>
					<td align="right" colspan="4">是否填写邀约计划</td>
					<td colspan="4"><input type="checkbox" id="checkbox" onclick="checkClick()" /></td>
				</tr>
				<tr id="yaoyuejihua" style="display: none">
					<td align="center" colspan="2">需求分析</td>
					<td align="center" colspan="2">邀约目标</td>
					<td align="center" colspan="2">赢得客户信任设计</td>
					<td align="center" colspan="2">感动客户情景设计</td>
				</tr>
				<tr id="yaoyuejihua2" style="display: none">
					<td align="center" colspan="2"><textarea rows="10" cols="30" id="xqfx" name="xqfx"></textarea></td>
					<td align="center" colspan="2"><textarea rows="10" cols="30" id="yymb" name="yymb"></textarea></td>
					<td align="center" colspan="2"><textarea rows="10" cols="30" id="ydkhxrsj" name="ydkhxrsj"></textarea></td>
					<td align="center" colspan="2"><textarea rows="10" cols="30" id="gdkhqjsj" name="gdkhqjsj"></textarea></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td align="right">计划邀约时间:</td>
					<td>
						<input name="plan_invite_date" id="plan_invite_date" readonly="readonly" value="" type="text" class="short_txt" datatype="1,is_date,10" group="startDate,endDate" />
						<input id="plan_invite_date2" name="plan_invite_date2" style="margin-left: -4px;" class="time_ico" type="button" onClick="changeEvent1()" />
					</td>
					<td align="right">计划见面时间:</td>
					<td><input type="text" value="" name="plan_meet_date" id="plan_meet_date" group="plan_meet_date"
								class="short_txt" datatype="1,is_date,10" size="20" hasbtn="true" readonly="readonly"
								maxlength="60" callFunction="showcalendar(event, 'plan_meet_date', false);" />
					</td>
					<td align="right">邀约方式:</td>
					<td>
					<input type="hidden" id="invite_type_new" name="invite_type_new" value=""/>
		      			<div id="ddtopmenubar26" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:103px;" rel="ddsubmenu26" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6047', loadInviteType2);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu26" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
					<td align="right"></td>
					 
				</tr>
			</table>
			
			<table class="table_query" width="95%" align="center" id="order_table" style="display: none">
				<tr>
					<td align="right" width="11%">订车时间:</td>
					<td width="12%"><input type="text" value="" name="order_date" id="order_date" group="order_date"
								class="short_txt" datatype="1,is_date,10" size="20" hasbtn="true" readonly="readonly"
								maxlength="60" callFunction="showcalendar(event, 'order_date', false);" />
					</td>
				</tr>
			</table>
			
			<table class="table_query" width="95%" align="center" id="defeat_table" style="display: none">
				<tr>
					<td align="right">战败车型：</td>
					<td>
					<select id="defeatVehicleA" name="defeatVehicleA" onchange="toChangeMenu2(this,'defeatVehicleB')">
						<option id="all" value="">-请选择-</option>
						<c:forEach items="${menusAList2 }" var="alist">
							<option id="${alist.MAINID }" value="${alist.MAINID }">${alist.NAME }</option>
						</c:forEach>
					</select>
					<select id="defeatVehicleB" name="defeatVehicleB">
						<option id="all" value="">-请选择-</option>
					</select>
					</td>
					<td align="right">战败原因：</td>
					<td>
					<input type="hidden" id="defeatReasonB" name="defeatReasonB" value=""/>
		      		<div id="ddtopmenubar23" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:103px;" rel="ddsubmenu23" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6033', loadDefeatReason2);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu23" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
					<td align="right">战败时间：</td>
					<td>
						<div align="left">
							<input type="text" value="${nowDate }" name="defeat_end_date" id="defeat_end_date" group="defeat_end_date"
								class="short_txt" datatype="1,is_date,10" size="20" hasbtn="true" readonly="readonly"
								maxlength="60" callFunction="showcalendar(event, 'defeat_end_date', false);" />
						</div>
					</td>
				</tr>
				<tr>
					<td align="right" colspan="1">原因分析：</td>
					<td align="left" colspan="8">
						<textarea rows="5" cols="72" id="reason_analysis" name="reason_analysis"></textarea>
					</td>
				</tr>
			</table>
			<table class="table_query" width="95%" align="center" id="failure_table" style="display: none">
				<tr>
					<td align="right" width="11%">失效时间:</td>
					<td width="12%"><input type="text" value="" name="failure_date" id="failure_date" group="failure_date"
								class="short_txt" datatype="1,is_date,10" size="20" hasbtn="true" readonly="readonly"
								maxlength="60" callFunction="showcalendar(event, 'failure_date', false);" />
					</td>
					<td align="right" width="11%"></td>
					<td width="12%"></td>
					<td align="right" width="11%"></td>
					<td width="12%"></td>
					<td align="right" width="11%"></td>
					<td width="12%"></td>
				</tr>
				<tr>
					<td align="right" colspan="1">原因说明：</td>
					<td align="left" colspan="8">
						<textarea rows="5" cols="72" id="failure_remark" name="failure_remark"></textarea>
					</td>
				</tr>
			</table>
			<table class="table_query" width="95%" align="center">
				
				<tr>
					<td colspan="3" align="center">
					<!-- 此处只有顾问登陆时才有保存按钮 -->
					<c:if test="${adviserLogon=='yes' }">
					<input name="queryBtn"
						type="button" class="normal_btn" onclick="doSave();" id="saveButton"
						value="保存" />
						</c:if>
						<input name="insertBtn" id="insertBtn"
						type="button" class="normal_btn" onclick="javascript:history.go(-1);"
						value="取消" />
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>

	</div>
	<script type="text/javascript" > 
	function followOpenInfo(customerId){
		var context=null;
		var openUrl = "<%=request.getContextPath()%>/crm/customer/CustomerManage/ctmUpdateInit.do?ctmId="+customerId;
		var parameters="fullscreen=yes, toolbar=yes, menubar=no, scrollbars=no, resizable=yes, location=no, status=yes";
		window.open (openUrl, "newwindow");
	}
	var dd1 = new Date(); 
	var data= dd1.Format("yyyy-M-d");
	function changeEvent(){
		var inbound=document.getElementById("intent_typeh").innerHTML.split("<")[0];
		var nextDate=document.getElementById("next_follow_date2");
		if(inbound == "A"){
			var dd = new Date();
		    	dd.setDate(dd.getDate()+6);
			nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
		}
		if(inbound == "B"){
			var dd = new Date();
	    	dd.setDate(dd.getDate()+14);
		nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
		}
		if(inbound == "C"){
			var dd = new Date();
	    	dd.setDate(dd.getDate()+29);
		nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
		}
		 if(inbound == "H"){
			 var dd = new Date();
		    	dd.setDate(dd.getDate()+2);
			nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
		}
		nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data));
	}
	function changeEvent1(){
		var inbound=document.getElementById("intent_typeh").innerHTML.split("<")[0];
		var nextDate=document.getElementById("plan_invite_date2");
		if(inbound == "A"){
			var dd = new Date();
		    	dd.setDate(dd.getDate()+6);
			nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data,dd.Format("yyyy-M-d")));
		}
		if(inbound == "B"){
			var dd = new Date();
	    	dd.setDate(dd.getDate()+14);
		nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data,dd.Format("yyyy-M-d")));
		}
		if(inbound == "C"){
			var dd = new Date();
	    	dd.setDate(dd.getDate()+29);
		nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data,dd.Format("yyyy-M-d")));
		}
		 if(inbound == "H"){
			 var dd = new Date();
		    	dd.setDate(dd.getDate()+2);
			nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data,dd.Format("yyyy-M-d")));
		}
		nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data));
	}
	//禁止用F5键 
	function document.onkeydown() 
	{ 
	if ( event.keyCode==116) 
	{ 
	event.keyCode = 0; 
	event.cancelBubble = true; 
	return false; 
	} 
	} 

	//禁止右键弹出菜单 
	function document.oncontextmenu(){event.returnValue=false;}//屏蔽鼠标右键 
</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar29", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar28", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar27", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar26", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar23", "topbar")</script>
</body>
</html>
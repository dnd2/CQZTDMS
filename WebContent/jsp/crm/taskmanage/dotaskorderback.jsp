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
		followTable.style.display = "block";
		inviteTable.style.display = "none";
		defeatTable.style.display = "none";
		orderTable.style.display = "none";
		failureTable.style.display = "none";
	}
	function inviteClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var defeatTable = document.getElementById("defeat_table");
		var orderTable = document.getElementById("order_table");
		var failureTable = document.getElementById("failure_table");
		followTable.style.display = "none";
		inviteTable.style.display = "block";
		defeatTable.style.display = "none";
		orderTable.style.display = "none";
		failureTable.style.display = "none";
	}
	function orderClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var defeatTable = document.getElementById("defeat_table");
		var orderTable = document.getElementById("order_table");
		var failureTable = document.getElementById("failure_table");
		followTable.style.display = "none";
		inviteTable.style.display = "none";
		defeatTable.style.display = "none";
		orderTable.style.display = "block";
		failureTable.style.display = "none";
	}
	function defeatClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var defeatTable = document.getElementById("defeat_table");
		var orderTable = document.getElementById("order_table");
		var failureTable = document.getElementById("failure_table");
		followTable.style.display = "none";
		inviteTable.style.display = "none";
		defeatTable.style.display = "block";
		orderTable.style.display = "none";
		failureTable.style.display = "none";
	}
	function failureClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var defeatTable = document.getElementById("defeat_table");
		var orderTable = document.getElementById("order_table");
		var failureTable = document.getElementById("failure_table");
		followTable.style.display = "none";
		inviteTable.style.display = "none";
		defeatTable.style.display = "none";
		orderTable.style.display = "none";
		failureTable.style.display = "block";
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
		var followRadio = document.getElementById("follow_radio");
		var nextFollowDate = document.getElementById("next_follow_date").value;
		var followType = document.getElementById("follow_type").value;
		var inviteRadio = document.getElementById("invite_radio");
		var xqfx = document.getElementById("xqfx").value;
		var yymb = document.getElementById("yymb").value;
		var ydkhxrsj = document.getElementById("ydkhxrsj").value;
		var gdkhqjsj = document.getElementById("gdkhqjsj").value;
		var planInviteDate = document.getElementById("plan_invite_date").value;
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
		var intentType2 = document.getElementById("intent_type2").value;
		var intentType3 = document.getElementById("intent_type3").value;
		var reg = new RegExp("^[0-9]*$");
		var dd = new Date();
		var nextDate=parseDate(nextFollowDate);
		function getZ(n){    return /^\d+(\.\d+)?$/.test(n+"");}
		if(followRadio.checked == true) {
			if(nextFollowDate==null||nextFollowDate=="") {
				MyAlert("请选择下次跟进时间！");
				return false;
			} else if(followType==null||followType=="") {
				MyAlert("请选择跟进方式！");
				return false;
			} else if(nextDate < dd.setDate(dd.getDate()-1)){
				MyAlert("跟进时间要大于当前时间");
				return false;
			} else {
				var customerId = document.getElementById("customerId").value;
				var taskId = document.getElementById("taskId").value;
				document.getElementById("saveButton").disabled = true;
				$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTask.do?taskType=orderBack&typeFlag=followRadio&customerId="+customerId+"&taskId="+taskId+"&intentType2="+intentType2;
				$('fm').submit();
			}
		} else if(inviteRadio.checked == true) {
			var dd = new Date();
			var planIDate=parseDate(planInviteDate);
			var planMDate=parseDate(planMeetDate);
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
				MyAlert("计划见面时间小于邀约时间！");
				return false;
			}
			var customerId = document.getElementById("customerId").value;
			var taskId = document.getElementById("taskId").value;
			document.getElementById("saveButton").disabled = true;
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTask.do?taskType=orderBack&typeFlag=inviteRadio&customerId="+customerId+"&taskId="+taskId+"&intentType3="+intentType3;
			$('fm').submit();
		} else if(orderRadio.checked == true) {
			if(orderDate==null||orderDate=="") {
				MyAlert("请选择订车时间！");
				return false;
			}
			var customerId = document.getElementById("customerId").value;
			var taskId = document.getElementById("taskId").value;
			document.getElementById("saveButton").disabled = true;
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTask.do?taskType=orderBack&typeFlag=orderRadio&customerId="+customerId+"&taskId="+taskId;
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
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTask.do?taskType=orderBack&typeFlag=defeatRadio&customerId="+customerId+"&taskId="+taskId;
			$('fm').submit();
		} else if(failureRadio.checked == true) {
			if(failureDate==null||failureDate=="") {
				MyAlert("请选择失效日期！");
				return false;
			}
			var customerId = document.getElementById("customerId").value;
			var taskId = document.getElementById("taskId").value;
			document.getElementById("saveButton").disabled = true;
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTask.do?taskType=orderBack&typeFlag=failureRadio&customerId="+customerId+"&taskId="+taskId;
			$('fm').submit();
		}
	}
	function parseDate(str)  
	{
	    return new Date(Date.parse(str.replace(/-/g,"/")));
	}
</script>
<title>订单退单任务</title>
</head>
<body onunload='javascript:destoryPrototype();'>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>日程管理>任务管理>订单退单
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="1" /> 
			<input type="hidden" id="dlrId" name="dlrId" value="" />
			<input type="hidden" name="customerId" id="customerId" value="${customerId }" />
			<input type="hidden" name="taskId" id="taskId" value="${taskId }" />
			<input type="hidden" name="errorMsg" id="errorMsg" value="${errorMsg }" />
			
			<table class="table_query" width="95%" align="center">
				<c:forEach items="${customerList }" var="customerList">
				<tr>
					<td align="right" width="10%">车主姓名：</td>
					<td><input id="customer_name" name="customer_name" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${customerList.OWNER_NAME }"
						maxlength="60" /></td>
					<td align="right" width="10%">联系电话：</td>
					<td width="12%"><input id="telephone" name="telephone" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${customerList.OWNER_PHONE }"
						size="20" maxlength="60" /></td>
					<td align="right" width="11%">车主证件名：</td>
					<td><input id="paper_name" name="paper_name" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${customerList.PAPER_TYPE }"
						maxlength="60" /></td>
					<td align="right" width="10%">车主证件号：</td>
					<td><input id="paper_no" name="paper_no" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${customerList.OWNER_PAPER_NO }"
						maxlength="60" /></td>
				</tr>
				
				<tr>
					<td align="right" width="6%">车主地址：</td>
					<td width="12%"><input id="address" name="address" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${customerList.OWNER_ADDRESS }"
						size="20" maxlength="60" /></td>
					<td align="right" width="12%">&nbsp;省份：</td>
					<td align="left" width="24%" colspan="4">
					<input id="dPro" name="dPro" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="short_txt" datatype="1,is_textarea,30" size="20" value="${customerList.PRO }"
						maxlength="60" /> 城市：<input id="dCity" name="dCity" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="short_txt" datatype="1,is_textarea,30" size="20" value="${customerList.CITY }"
						maxlength="60" /> 区县：<input id="dArea" name="dArea" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="short_txt" datatype="1,is_textarea,30" size="20" value="${customerList.AREA }"
						maxlength="60" /></td>
				</tr>
				<tr>
					 <td align="center" colspan="8">
						<a href="#" id="FOLLOW_ID" name="FOLLOW_ID" onclick='followOpenInfo("${customerId}")'>客户资料维护</a>
					</td>
				</tr>
				</c:forEach>
			</table>
			</br>
			</br>
			<div>
				<b style="display: inline-block; float: left">下次计划任务</b><hr style="display: inline-block;">
			</div>
			<table class="table_query" width="95%" align="center">
				<tr>
					<td align="right" width="20%">
						<input type="radio" checked="checked" id="follow_radio" name="group_radio" onclick="followClick()">跟进</input>
						<input type="radio"  id="invite_radio" name="group_radio" onclick="inviteClick()" style="display:none;"><!-- 邀约 --></input>
						<input type="radio"  id="order_radio" name="group_radio" onclick="orderClick()">订车</input>
					</td>
					<td width="18%">
						<input type="radio"  id="defeat_radio" name="group_radio" onclick="defeatClick()">战败</input>
						<input type="radio"  id="failure_radio" name="group_radio" onclick="failureClick()" style="display:none;"><!-- 失效 --></input>
					</td>
				</tr>
			</table>
			</br>
			<table class="table_query" width="95%" align="center" id="follow_table">
				<tr>
					<td width="9%" align="right">下次跟进时间：</td>
					<td width="20%">
						<div align="left">
							<input type="text" value="" name="next_follow_date" id="next_follow_date" group="next_follow_date"
								class="short_txt" datatype="1,is_date,10" size="20" hasbtn="true" readonly="readonly"
								maxlength="60" callFunction="showcalendar(event, 'next_follow_date', false);" />
						</div>
					</td>
					<td align="right" width="7%">意向等级：</td>
					<td><input type="hidden" id="intent_type2" name="intent_type2"
						value="" />
						<div id="ddtopmenubar20" class="mattblackmenu">
							<ul>
								<li><a id="intent_typeh" style="width: 103px;"
									rel="ddsubmenu20" href="###" isclick="true"
									onclick="stree.loadtree(this, '<%=request.getContextPath()%>/crm/data/DataManage/initData.json?codeId=6010', loadIntentType2);"
									notselected="60101005,60101006,60101007" deftitle="--请选择--">
										--请选择--</a>
									<ul id="ddsubmenu20" class="ddsubmenustyle"></ul></li>
							</ul>
					</div></td>
					<td width="8%" align="right">跟进方式：</td>
					<td width="20%">
						<input type="hidden" id="follow_type" name="follow_type" value=""/>
		      			<div id="ddtopmenubar25" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:103px;" rel="ddsubmenu25" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6046', loadFollowType);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu25" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
				</tr>
				<tr>
					<td width="6%" align="right">跟进计划：</td>
					<td align="left" colspan="4">
						<textarea rows="5" cols="70" id="follow_plan" name="follow_plan"></textarea>
					</td>
				</tr>
			</table>
			
			<table class="table_query" width="95%" align="center" id="invite_table" style="display: none">
				<tr>
					<td align="right" colspan="3">是否填写邀约计划</td>
					<td colspan="2"><input type="checkbox" id="checkbox" onclick="checkClick()" /></td>
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
					<td align="right">计划邀约时间:</td>
					<td><input type="text" value="" name="plan_invite_date" id="plan_invite_date" group="plan_invite_date"
								class="short_txt" datatype="1,is_date,10" size="20" hasbtn="true" readonly="readonly"
								maxlength="60" callFunction="showcalendar(event, 'plan_invite_date', false);" />
					</td>
					<td align="right">计划见面时间:</td>
					<td><input type="text" value="" name="plan_meet_date" id="plan_meet_date" group="plan_meet_date"
								class="short_txt" datatype="1,is_date,10" size="20" hasbtn="true" readonly="readonly"
								maxlength="60" callFunction="showcalendar(event, 'plan_meet_date', false);" />
					</td>
					<td align="right" width="7%">意向等级：</td>
					<td><input type="hidden" id="intent_type3" name="intent_type3"
						value="" />
						<div id="ddtopmenubar21" class="mattblackmenu">
							<ul>
								<li><a id="intent_typeh" style="width: 103px;"
									rel="ddsubmenu21" href="###" isclick="true"
									onclick="stree.loadtree(this, '<%=request.getContextPath()%>/crm/data/DataManage/initData.json?codeId=6010', loadIntentType2);"
									notselected="60101005,60101006,60101007" deftitle="--请选择--">
										--请选择--</a>
									<ul id="ddsubmenu21" class="ddsubmenustyle"></ul></li>
							</ul>
						</div></td>
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
					<td>
					</td>
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
						<input name="insertBtn"
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
	genLocSel('dPro','dCity','dArea','','',''); // 加载省份城市和区县
	/**  弹出框设置   **/
	function followOpenInfo(customerId){
		var context=null;
		var openUrl = "<%=request.getContextPath()%>/crm/customer/CustomerManage/ctmUpdateInit.do?ctmId="+customerId;
		var parameters="fullscreen=yes, toolbar=yes, menubar=no, scrollbars=no, resizable=yes, location=no, status=yes";
		window.open (openUrl, "newwindow");
	}
</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar20", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar21", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar26", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar25", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar24", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar23", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar24", "topbar")</script>

</body>
</html>
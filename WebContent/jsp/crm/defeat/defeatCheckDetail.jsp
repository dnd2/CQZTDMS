<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>战败实效经理审核</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/defeat/defeat.js"></script>
</head>
<body onload="loadcalendar();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;1当前位置： 潜客管理 &gt; 战败/失效管理 &gt; 战败/失效审核</div>
<form id="fm" name="fm" method="post">
<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
<input type="hidden" name="defeatId" id="defeatId" value="${tpd.defeatfailureId}" />
<input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query" border="0">
		<tr>
			<td align="right">客户姓名：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${tpc.customerName}" readonly />	
			</td>
			<td align="right">手机：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${tpc.telephone}" readonly />	
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">意向车型：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${tpiv.seriesName}"  readonly />	
			</td>
		</tr>
		<tr>
			<td align="right">战败车型：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${tpdv.seriesName}" readonly />
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">顾问：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${tu.name}"  readonly />	
			</td>
			<td align="right" style="display: none;">战败/失效时间：</td>
			<td align="left" style="display: none;">
				<c:if test="${tpd.defeatfailureType==60391001}">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${map.endDate}" readonly />	
				</c:if>
				<c:if test="${tpd.defeatfailureType==60391002}">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${failureDate}"  readonly/>	
				</c:if>	
			</td>
		</tr>
		<tr>
			<td align="right">战败原因：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="groupName" id="name" value="${defeatReason}" readonly />	
			</td>
			<td align="right">&nbsp;</td>
			<td align="left">
				&nbsp;
<!--				<input type="text" class="middle_txt" name="groupName" id="name" value="${tc.codeDesc}" datatype="0,is_textarea,30" disabled />	-->
			</td>
			<td align="right" class="table_query_2Col_label_6Letter" nowrap="nowrap">&nbsp;</td>
			<td align="left">
			</td>
		</tr>
		<tr>
			<td align="right">原因分析：</td>
			<td align="left" colspan="5">
				<textarea rows="5" cols="70" readonly>${tpd.reasonAnalysis}</textarea>
			</td>
		
		</tr>
		<tr>
			<td align="right">审核意见：</td>
			<td align="left" colspan="5">
				<textarea rows="5" cols="70" name="auditRemark"></textarea>
			</td>
		</tr>
		<tr>
		<td align="center" colspan="6">
			<input type="button" class="normal_btn" onclick="auditPass();" value="通  过" id="auditSub" />
			<input type="button" class="normal_btn" onclick="auditBack();" value="驳  回" id="retBtn" />
			<input type="button" class="normal_btn" onclick="history.back();" value="返  回" id="retBtn" />
		</td>
	</tr>
	</table>
	<div id="backDiv" style="display:none;">
	
	<hr />
			<center>
				跟进/邀约的顾问：
				<select name="adviser" id="adviser">
	         	 	<c:forEach items="${userList}" var="po">
	         	 		<c:if test="${po.USER_ID==tpc.adviser}"><option value="${po.USER_ID}" selected="selected" >${po.NAME}</option></c:if>
	         	 		<c:if test="${po.USER_ID!=tpc.adviser}"><option value="${po.USER_ID}"  >${po.NAME}</option></c:if>
	         	 	</c:forEach>
	           </select>
	           	意向等级：
	            <script type="text/javascript">
						genSelBoxExp("ctm_rank",<%=Constant.INTENT_TYPE%>,"",false,"short_sel",'onchange=changeSelect(this);',"false",'');
				</script>
				<input type="hidden" id="ctm_rank1" name="ctm_rank1" value="H"/>
           </center>
            <center>
           		<div style="width: 100%;text-align: center;display: none;background-color: #F0F7F2;height: 20px;line-height: 20px;"id="groupRadio">
           			<input type="radio"  id="follow_radio" name="group_radio" onclick="followClick()"/>跟进
					<input type="radio"  id="invite_radio" name="group_radio" style="display: none;" onclick="inviteClick()"/><!-- 邀约 -->
           		</div>
           </center>
	   <!-- <table class="table_query" width="95%" align="center" id="groupRadio" style="display: none">
				<tr>
					<td align="center" >
						<input type="radio"  id="follow_radio" name="group_radio" onclick="followClick()"/>跟进
						<input type="radio"  id="invite_radio" name="group_radio" style="display: none;" onclick="inviteClick()"/>邀约
					</td>
				</tr>
			</table> -->
	<table class="table_query" width="95%" align="center" id="follow_table" style="display: none">
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
						<label> <script type="text/javascript">
						genSelBoxExp("follow_type",<%=Constant.FOLLOW_TYPE%>,"",true,"short_sel",'',"false",'');
					</script> </label>
					</td>
				</tr>
				<tr>
					<td width="6%" align="right">推进建议：</td>
					<td align="left" colspan="4">
						<textarea rows="5" cols="70" id="follow_plan" name="follow_plan"></textarea>
					</td>
				</tr>
			</table>
			
			<table class="table_query" width="95%" align="center" id="invite_table" style="display: none">
				
<!--				<tr id="yaoyuejihua" style="display: none">-->
<!--					<td align="center" colspan="2">需求分析</td>-->
<!--					<td align="center" colspan="2">邀约目标</td>-->
<!--					<td align="center" colspan="2">赢得客户信任设计</td>-->
<!--					<td align="center" colspan="2">感动客户情景设计</td>-->
<!--				</tr>-->
<!--				<tr id="yaoyuejihua2" style="display: none">-->
<!--					<td align="center" colspan="2"><textarea rows="10" cols="30" id="xqfx" name="xqfx"></textarea></td>-->
<!--					<td align="center" colspan="2"><textarea rows="10" cols="30" id="yymb" name="yymb"></textarea></td>-->
<!--					<td align="center" colspan="2"><textarea rows="10" cols="30" id="ydkhxrsj" name="ydkhxrsj"></textarea></td>-->
<!--					<td align="center" colspan="2"><textarea rows="10" cols="30" id="gdkhqjsj" name="gdkhqjsj"></textarea></td>-->
<!--				</tr>-->
				<tr>
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
					<label> <script type="text/javascript">
						genSelBoxExp("invite_type_new",<%=Constant.INVITE_TYPE%>,"",true,"short_sel",'',"false",'');
					</script> </label>
					</td>
					<td align="right"></td>
					<td>
					</td>
				</tr>
				<tr>
					<td align="right" >推进建议：</td>
					<td colspan="7" align="left">
						<textarea rows="5" cols="30" id="inviteTip" name="inviteTip"></textarea>
					</td>
				</tr>
			</table>
			<center>
           		<div style="width: 100%;text-align: center;display: none;background-color: #F0F7F2;height: 40px;line-height: 40px;"id="sureTable">
           			<input type="button"  id="sure" name="sureSubmits" class="normal_btn" onclick="sureSubmit();" value="确认"/>
           		</div>
           </center>
			<!-- <table class="table_query" width="95%" align="center" id="sureTable" style="display: none;">
				<tr>
					<td align="center" >
						<input type="button"  id="sure" name="sureSubmits" class="normal_btn" onclick="sureSubmit();" value="确认"/>
					</td>
				</tr>
			</table> -->
	</div>
			
</form>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar29", "topbar");
function loadIntentType(obj){
	$("ctm_rank").value=obj.getAttribute("TREE_ID");
	}
</script>
</div>
</body>
</html>

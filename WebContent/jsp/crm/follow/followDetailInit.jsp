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
</script>
<title>跟进详细信息</title>
</head>
<body onunload='javascript:destoryPrototype();'>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>日程管理>任务管理>跟进详细信息
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="1" /> 
			<input type="hidden" id="dlrId" name="dlrId" value="" />
			<div>
				<b style="display: inline-block; float: left">跟进计划</b>
				<hr style="display: inline-block;"/>
			</div>
			<table class="table_query" width="95%" align="center">
						<tr>
					<td align="right" width="10%">跟进时间：</td>
					<td><input id="follow_date" name="follow_date" type="text"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${customerList[0].FOLLOW_DATE }" readonly="readonly"
						maxlength="60" style="background-color: #EEEEEE;" /></td>
					<td align="right" width="15%">跟进方式：</td>
					<td width="25%"><input id="ctm_rank" name="ctm_rank" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${customerList[0].FOLLOW_TYPE }"
						size="20" maxlength="60" /></td>
				</tr>
				<tr>
					<td align="right" colspan="1">跟进计划：</td>
					<td align="left" colspan="3">
						<textarea rows="5" cols="70" id="follow_plan" name="follow_plan" readonly="readonly" style="background-color: #EEEEEE;">${customerList[0].FOLLOW_PLAN }</textarea>
					</td>
				</tr>
			</table>
			<div>
				<b style="display: inline-block; float: left">跟进结果</b>
				<hr style="display: inline-block;"/>
			</div>
			<table class="table_query" width="95%" align="center">
			<c:forEach items="${customerList }" var="customerList">
			<tr>
					<td align="right" width="10%">客户姓名：</td>
					<td><input id="customer_name" name="customer_name" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${customerList.CUSTOMER_NAME }"
						maxlength="60" /></td>
					<td align="right" width="7%">联系电话：</td>
					<td width="12%"><input id="telephone" name="telephone" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${customerList.TELEPHONE }"
						size="20" maxlength="60" /></td>
					<td align="right" width="10%">意向车型：</td>
					<td>
					<input id="intent_vehicle" name="intent_vehicle" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${customerList.INTENT_VEHICLE }"
						size="20" maxlength="60" />
					</td>
				</tr>
			
				<tr>
					<td align="right" width="10%">跟进时间：</td>
					<td><input id="follow_date" name="follow_date" type="text"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${customerList.FINISH_DATE }" readonly="readonly"
						maxlength="60" style="background-color: #EEEEEE;" /></td>
					<td align="right" width="15%">意向等级：</td>
					<td width="25%"><input id="ctm_rank" name="ctm_rank" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${customerList.CTM_RANK }"
						size="20" maxlength="60" /></td>
					<td align="right" width="10%">销售流程进度：</td>
					<td><input id="sales_progress" name="sales_progress" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${customerList.SALES_PROGRESS }"
						size="20" maxlength="60" /></td>
				</tr>
				
				<tr>
					<td align="right" colspan="1">跟进结果：</td>
					<td align="left" colspan="5">
						<textarea rows="5" cols="70" id="follow_info" name="follow_info" readonly="readonly" style="background-color: #EEEEEE;">${customerList.FOLLOW_INFO }</textarea>
					</td>
				</tr>
<!--				<tr>-->
<!--					<td align="right" colspan="1">跟进计划：</td>-->
<!--					<td align="left" colspan="5">-->
<!--						<textarea rows="5" cols="70" id="follow_plan" name="follow_plan" readonly="readonly" style="background-color: #EEEEEE;">${customerList.FOLLOW_PLAN }</textarea>-->
<!--					</td>-->
<!--				</tr>-->
				</c:forEach>
			</table>
			<table class="table_query" width="95%" align="center">
				<tr>
					<td colspan="3" align="center">
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
</SCRIPT>

</body>
</html>
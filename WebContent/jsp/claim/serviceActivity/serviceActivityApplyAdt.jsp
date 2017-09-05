<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@page import="com.infodms.dms.common.Constant"%>
<head> 
<%  
	String contextPath = request.getContextPath(); 
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="/BQYXDMS/js/jslib/calendar.js"></script>
<title>服务活动审核</title>
</head>
<body>
<div class="wbox">
<div class="navigation">
<img src="<%=contextPath%>/jsp_new/img/nav.gif" />&nbsp;当前位置：服务活动管理&gt;服务活动审核页面
</div>
<form name="fm" id="fm" method="post">
<input type="hidden" id="isPass" name="isPass" value="">
<input type="hidden" id="activityId" name="activityId" value="${map.ID}">
<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
<table border="0" cellpadding="1" cellspacing="1" class="table_query" width="100%" style="text-align: center;">
	<tr>
		<td style="text-align:right">服务站代码:</td>
    	<td>
    	${map.DEALER_CODE }
    	</td>
		<td style="text-align:right">服务站名称:</td>
    	<td>
    	${map.DEALER_NAME }
    	</td>
    	<td style="text-align:right">单据新增时间:</td>
    	<td>
    	${map.NOWDATE }
    	</td>
	</tr>
	<tr>
		<td style="text-align:right">活动时间:</td>
    	<td>
			${map.START_DATE }
		             &nbsp;至&nbsp;
		 	${map.END_DATE }
		</td>	
		<td style="text-align:right">所属区域:</td>
    	<td>
			${map.ORG_NAME }
    	</td>
	</tr>
	<tr>
		<td style="text-align:right">活动申请内容:</td>
		<td colspan="5">
		<textarea name='remark' id='remark' maxlength="100"  rows='3' cols='80' disabled="disabled">${map.ACTIVITY_CONTENT}</textarea>
		</td>
	</tr>
	 <tr>
     	<td  style="text-align:right">
     		审核内容:
    	</td>
    	<td colspan="5">
    		<textarea  rows='3' cols='80' id="auditAdvice" name="auditAdvice"></textarea>
    	</td>
    </tr>
	
</table>
</div>
</div>
<div class="form-panel">
		<h2>审核记录</h2>
			<div class="form-body">
<table class="table_list">
				<tr >
					<th style="text-align:center" width="20%" >审核时间</th>
					<th style="text-align:center" width="60%">审核内容</th>
					<th style="text-align:center" width="8%">审核人</th>
					<th style="text-align:center" width="12%">审核状态</th>
				</tr>
				<c:forEach items="${applyRecordList}" var="applyR">
					<tr class="table_list_row2">
						<td align="center">${applyR.CDDATE}</td>
						<td align="left">${applyR.AUDIT_CONTENT}</td>
						<td align="center">${applyR.NAME}</td>
						<td align="center">${applyR.STATUS}</td>
					</tr>
				</c:forEach>
			</table>
<table class="table_query">
		<tr>
			<td style="text-align:center">
               	<input type="button" class="normal_btn" id="sure" onclick="subPass();"  style="width=8%" value="通 过" />
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sure" onclick="subRefuse();"  style="width=8%" value="驳 回" />
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sure" onclick="subReject();"  style="width=8%" value="拒 绝" />
               	&nbsp;&nbsp;
				<input type="button" id="back" onClick="_hidn();" class="normal_btn"  style="width=8%" value="返 回"/>
          	</td>
		</tr>
</table>
</div>
</div>
</form>
</body>
</div>
<script type="text/javascript">
	function sureInsert(){
		var auditAdvice=document.getElementById("auditAdvice").value;
		if(""==auditAdvice){
			MyAlert("提示：请先输入审核内容！");
			return;
		}
		var url="<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/audittServiceActivity.json";
		makeNomalFormCall(url,saveBack,'fm','');
	}
	//通过
	function subPass(){
		document.getElementById("isPass").value ="0";
		sureInsert();
	}
	//驳回
	function subRefuse(){
		document.getElementById("isPass").value ="1";
		MyConfirm("是否确认驳回？",sureInsert,'');
	}
	//拒绝
	function subReject(){
		document.getElementById("isPass").value ="2";
		MyConfirm("是否确认拒绝？",sureInsert,'');
	}
	function saveBack(json){
		if(json.isSuccess != null && json.isSuccess=='0'){
			MyAlert(json.message);
			__parent().__extQuery__(1);
			_hide() ;
		}
		else{
			MyAlert("操作失败,请联系管理员!");
		}
	}
	function goBack(){
		window.location.href = "<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/getServiceActivityAudit.do";
	}
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>

</html>
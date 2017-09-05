<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动-新增</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onload="doInit()">
<div class="wbox">
<div class="navigation">
<img src="<%=contextPath%>/jsp_new/img/nav.gif" />&nbsp;当前位置：服务活动管理&gt;服务活动-新增
</div>
<form name="fm" id="fm" method="post">
<input type="hidden" id="dealerId" name="dealerId" value="${map.DEALER_ID}">
<input type="hidden" id="orgId" name="orgId" value="${map.ORG_ID}">
<input type="hidden" id="orgCode" name="orgCode" value="${map.ORG_CODE}">
<input type="hidden" id="id" name="id" value="${map.ID}">
<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
<table class="table_query" >
		<td style="text-align:right">服务站代码:</td>
    	<td>
    	<input class="middle_txt" id="dealerCode" value="${map.DEALER_CODE }" readonly="readonly" name="dealerCode" type="text" maxlength="30" />
    	</td>
		<td style="text-align:right">服务站名称:</td>
    	<td>
    		<input class="middle_txt" id="dealerName" value="${map.DEALER_NAME }" readonly="readonly" name="dealerName" type="text" maxlength="30" />
    	</td>
    	<td style="text-align:right">单据新增时间:</td>
    	<td>
    		<input class="middle_txt"  id="createDate" value="${map.NOWDATE }" readonly="readonly" name="createDate" type="text" maxlength="30" />
    	</td>
	</tr>
	<tr>
		<td style="text-align:right">活动时间:</td>
    	<td>
    		<input id="acceptStart" name="acceptStart" value="${map.START_DATE }"  datatype="1,is_date,10" class="middle_txt" style="width:80px;" type="text" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'acceptEnd\')}'})"/>
			<%-- <input name="acceptStart" type="text" value="${map.START_DATE }"  group="dealStart,dealEnd" datatype="1,is_date,10" class="short_time_txt" id="acceptStart" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'acceptStart', false);" />  	 --%>
		             &nbsp;至&nbsp;
		     <input id="acceptEnd" name="acceptEnd" value="${map.END_DATE }" datatype="1,is_date,10" class="middle_txt" style="width:80px;" type="text" onclick="WdatePicker({minDate:'#F{$dp.$D(\'acceptStart\')}'})"/>
		    <%-- <input name="acceptEnd" type="text" value="${map.END_DATE }" group="dealStart,dealEnd" datatype="1,is_date,10" class="short_time_txt" id="acceptEnd" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'acceptEnd', false);" />  --%>
			<font color="red">*</font>
		</td>	
		<td style="text-align:right">所属区域:</td>
    	<td>
			<input class="middle_txt" id="orgName" value="${map.ORG_NAME }" readonly="readonly" name="orgName" type="text" maxlength="30" />
    	</td>
	</tr>
	<tr>
		<td style="text-align:right">活动申请内容:</td>
		<td colspan="5">
		<textarea name="addtext" id="addtext" rows='3' cols='80'>${map.ACTIVITY_CONTENT }</textarea>
		<font color="red">*</font>
		</td>
		
	</tr>
	
</table>
<br>
<div <c:if test="${flag=='1'}">style="display:none;"</c:if>>
<table class="table_list" >
				<tr >
					<th colspan="7"><img class="nav"
						src="<%=contextPath%>/img/subNav.gif" />审核记录</th>
				</tr>
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
			</div>
	<br/>
<br>
<table class="table_query">
		<tr>
			<td height="6" style="text-align:center">
               	<input type="button" class="normal_btn" id="sureButton" onclick="sureInsert();"   value="确 定" />&nbsp;&nbsp;
				<input type="button" id="back" onClick="_hide();" class="normal_btn"   value="返 回"/>
          	</td>
		</tr>
</table>
</div>
</div>
</form>
</div>
</body>
<script type="text/javascript">
	function sureInsert(){
		var acceptStart=document.getElementById("acceptStart").value;
		var acceptEnd=document.getElementById("acceptEnd").value;
		var createDate=document.getElementById("createDate").value;
		var addtext=document.getElementById("addtext").value;
		 var d1 = new Date(acceptStart.replace(/\-/g, "\/"));  
		 var d2 = new Date(acceptEnd.replace(/\-/g, "\/"));  
		 var d3 = new Date(createDate.replace(/\-/g, "\/"));  
		  if(acceptStart!=""&&acceptEnd!=""&&d1 >d2)  
		 {  
		  MyAlert("提示：活动开始时间不能大于结束时间！");  
		  return ;  
		 }
		 if(createDate!=""&&acceptEnd!=""&&d3 >d2)  
			 {  
			 MyAlert("提示：活动结束时间不能小于当前时间！");  
			  return ;  
			 }
		if(""==acceptStart||""==acceptEnd){
			MyAlert("提示：请选择对应的活动时间！");
			return;
		}
		if(""==addtext){
			MyAlert("提示：请先输入活动内容！");
			return;
		}
		makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/saveServiceActivityApply.json',saveBack,'fm','');
	}
	function saveBack(json){
		if(json.isSuccess != null && json.isSuccess=='0'){
			MyAlert("保存成功!");
			__parent().__extQuery__(1);
			_hide() ;
		}
		else{
			MyAlert("保存失败,请联系管理员!");
		}
	}
	function goBack(){
		window.location.href = "<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/getServiceActivityApply.do";
	}
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</html>
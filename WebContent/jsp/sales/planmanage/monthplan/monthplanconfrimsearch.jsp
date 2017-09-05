<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>月度任务确认</title>
<script language="JavaScript">
	function doInit(){
			setDate();	//页面初始化
	}
	function searchSubmit(){
		if(!submitForm('fm')){
			return false;
		}
		var fms = document.getElementById("fm");
		fms.action = "<%=contextPath %>/sales/planmanage/YearTarget/YearTargetConfirm/yearTargetConfirmSearch.do";
    	fms.submit();
	}
	function subChecked(){
	    var str="";
		MyConfirm("是否执行确认操作?",confirmSubmit);
	}
	function confirmSubmit(){
		fm.action=  "<%=contextPath %>/sales/planmanage/MonthTarget/MonthTargetConfirm/monthTargetConfirmSubmint.do";
		fm.submit();
	}
	//设置超链接
	function mySelect(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.ORDER_ID+"\")'>["+ value +"]</a>");
	}
	
	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/feedbackmng/query/PunishmentApplyCarMantain/getOrderIdInfo.do?orderId='+value,800,500);
	}
	//•	版本描述默认显示“当前日期”，格式为“YYYY-MM-DD”。
	function setDate(){
		var myDate = new Date();
		var aDate = myDate.Format("yyyy-MM-dd");
		document.getElementById("plan_desc_id").value = aDate;
	}	
</script>
</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置>计划管理>月度任务>${year}年${month}月度任务确认</div>
		<form name="fm"  id="fm" method="post" >
		<div class="form-panel">
			<h2>月度任务确认</h2>
			<div class="form-body">
				<table class="table_query table_list">
				  <tr>
					    <th><div class="center">经销商代码</div></th>
			            <th><div class="center">经销商名称</div></th>
			            <th><div class="center">车系代码</div></th>
			            <th><div class="center">车系</div></th>
			            <th><div class="center">月度任务</div></th>
			  	   </tr>
				  	<c:forEach items="${list}" var="list" >
						<tr>
							<td><div class="center">${list.DEALER_CODE }</div></td>
							<td><div class="center">${list.DEALER_SHORTNAME }</div></td>
							<td><div class="center">${list.GROUP_CODE }</div></td>
							<td><div class="center">${list.GROUP_NAME }</div></td>
							<td><div class="center">${list.SALE_AMOUNT }</div></td>
						</tr>
					</c:forEach>
				</table>
				<br>
				<table>
					<tr>
					   <td colspan="6" nowrap="nowrap">
				       	  新版本号:<input type="text" name="plan_ver" size="17" value="${planVer}" class="middle_txt" readonly="true" />      
				       	  版本描述:<input type="text" id="plan_desc_id" name="plan_desc" size="17" value="" class="middle_txt" /> 
				       	  <input type="button" class="u-button u-submit"  name="vdcConfirm" value="确认" onclick="subChecked();" />
				       	  <input type="button" class="u-button u-reset"   name="vdcConfirm" value="返回" onclick="history.back();" />
				       	  <input type="hidden" name="year" value="${year }" />   
				       	  <input type="hidden" name="month" value="${month }" />   
				       	  <input type="hidden" name="areaId" value="${areaId }" />
				       	  <input type="hidden" name="planType" value="${planType}" />
				       </td>
					</tr>
				</table>
			</div>
		</div>
		</form>
</body>
</html>

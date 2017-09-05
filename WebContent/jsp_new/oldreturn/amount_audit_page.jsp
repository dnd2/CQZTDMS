<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.Map"%>
<head> 
<%  
	String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
	List<Map<String, Object>> detailList1 = (List) request.getAttribute("detailList1");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<title>索赔旧件运费审批</title>
<script type="text/javascript">
	function audit(){
		var price=$("#price").val();
		var authPrice = $("#authPrice").val();
		var priceRemark = $("#priceRemark").val();
		var reg2 = /^(\d+\.\d{1,2}|\d+)$/;
		 if(authPrice==null || authPrice ==""){
			MyAlert("请输入审核运费!");
			return;
		}else if(!reg2.test(authPrice)){
			MyAlert("审核运费为最多2位小数的数字!");
			return;
		}else if (parseFloat(price)<parseFloat(authPrice)){
			MyAlert("审核运费不能大于申请运费!");
			return;
		}else if ((parseFloat(price)>parseFloat(authPrice))&&(priceRemark==null||priceRemark=="")){
			MyAlert("扣减运费时必须填写备注!");
			return;
		}
		MyConfirm("是否确认审核该运费为"+authPrice+"？",auditSure,"");
	}
	function auditSure(){
		var id=$("#id").val();
		var authPrice=$("#authPrice").val();
		var priceRemark = $("#priceRemark").val();
		var url='<%=contextPath%>/OldReturnAction/authPriceAudit.json';
		sendAjax(url,function(json){
			if(json.code=="succ"){
    			MyAlert("提示：运费审核成功！");
    			window.location.href='<%=contextPath%>/OldReturnAction/returnAmountAuditList.do?query=true';
    		}else{
    			MyAlert(json.msg);
    		}
		},'fm');
	}
	function backTo(){
		window.location.href='<%=contextPath%>/OldReturnAction/returnAmountAuditList.do?query=true';
	}
</script>
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;索赔旧件运费审批</div>
</div>
<form name="fm" id="fm" method="post">
<input type="hidden" name="id" id="id" value="${t.ID }"/>
<input type="hidden" id="price" value="${t.PRICE }">
<!-- 基本信息 -->
<div class="form-panel">
  <h2>基本信息</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- <table class="table_edit" id="baseTabId"> -->
	<tr>
		<td class="table_add_2Col_label_5Letter" style="text-align:right" nowrap="true">经销商代码：</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:left">${t.DEALER_CODE }</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:right" nowrap="true">经销商名称：</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:left">${t.DEALER_NAME }</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:right" nowrap="true">所属区域：</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:left">${t.ATTACH_AREA }</td>
	</tr>
	<tr>
		<td class="table_add_2Col_label_5Letter" style="text-align:right">回运清单号：</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:left">${t.RETURN_NO }</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:right">回运类型：</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:left">${t.RETURN_DESC }</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:right">旧件回运起止时间：</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:left">${t.WR_START_DATE }</td>
	</tr>
	
	<tr>
		<td class="table_add_2Col_label_5Letter" style="text-align:right">发运时间：</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:left">${t.CREATE_DATE }
		<td class="table_add_2Col_label_5Letter" style="text-align:right">货运方式：</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:left">${t.TRANSPORT_DESC }</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:right">发运单号：</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:left">${t.RETURN_NO } </td>
	</tr>
	<tr>
		<td class="table_add_2Col_label_5Letter" style="text-align:right">装箱总数：</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:left">${t.PARKAGE_AMOUNT }</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:right">实到箱数：</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:left">${t.REAL_BOX_NO }</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:right">物流公司：</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:left">${t.TRANSPORT_NAME}</td></tr>
	 <tr>
	   <td class="table_add_2Col_label_5Letter" style="text-align:right">包装情况：</td>
	   <td class="table_add_2Col_label_5Letter" style="text-align:left">
	     <change:tcCode value="${t.PART_PACKGE}" showType="0"></change:tcCode>
	   </td>
	   <td class="table_add_2Col_label_5Letter" style="text-align:right" style="display:none;">故障卡情况：</td>
	   <td class="table_add_2Col_label_5Letter" style="text-align:left" style="display:none;">
	     <change:tcCode value="${t.PART_MARK}" showType="0"></change:tcCode>
	   </td>
	   <td class="table_add_2Col_label_5Letter" style="text-align:right">清单情况：</td>
	   <td class="table_add_2Col_label_5Letter" style="text-align:left">
	     <change:tcCode value="${t.PART_DETAIL}" showType="0"></change:tcCode>
	   </td>
	 </tr>
	 <tr>
		<td class="table_add_2Col_label_5Letter" style="text-align:right">申请运费：</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:left">${t.PRICE }(元)</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:right">审核运费：</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:left">
		  <input class="middle_txt" maxlength="25" name="authPrice" id="authPrice" value="${t.PRICE }"/>
		  <span style="color: red">*</span>(元) 
		</td>
	 </tr>
	 <tr>
		<td class="table_add_2Col_label_5Letter" style="text-align:right;text-valign:top">审核运费备注：</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:left" colspan=3>
		  <textarea name="priceRemark" id="priceRemark" rows="5" onblur="checkMax();" cols="80"></textarea>
		</td>
	 </tr>
</table>
</div>
</div>
<!-- 附件信息 -->
<div class="form-panel">
<h2><img src="<%=contextPath%>/img/nav.gif"/>附件信息&nbsp;&nbsp;&nbsp; </h2>
<div class="form-body">
  <table class="table_list" id="file">
  <tr >
    <th class="center"> 附件名称 </th>
    <th class="center"> 操作</th>
  </tr>
  <c:forEach items="${fileList}" var="list" varStatus="st">
  <tr>
    <td class="center">${list.filename }</td>
	<td class="center">
	  <a href="<%=contextPath%>/util/FileDownLoad/fileDownloadQuery.do?fjid=${list.fjid}" >&nbsp;下载</a>
	</td>
  </tr>
     </c:forEach>
</table>
</div>
</div>
		
<table class="table_query" style="width:100%">
	<tr>
		<td height="10" style="text-align:center">
		<input type="button" onclick="audit();" class="normal_btn" value="审核" />&nbsp;&nbsp;
		<input type="button" onclick="backTo();"class="normal_btn" value="返回" />
		</td>
	</tr>
</table>
</form>
</body>
</html>
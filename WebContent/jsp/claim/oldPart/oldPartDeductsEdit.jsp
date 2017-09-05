<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>索赔抵扣单明细(编辑)页面</title>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body onload="__extQuery__(1);">
<div class="navigation">
	<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
	售后服务管理 &gt; 经销商索赔管理 &gt;索赔旧件管理&gt;索赔旧件抵扣明细
</div>
<form method="post" name="fm">
	<input type="hidden" name="deductId" value="<%=request.getParameter("ID")%>"/>
</form>
<!--分页 begin -->
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
<br/>
<div align="center">
	<% 
		String status = request.getParameter("STATUS");
		if(status!=null)
			status = status.trim();
		if("0".equals(status)){//未结算
	%>
	<input type="button" name="button3" value="修改" onclick="MyAlert('还未实现，请完成');" class="normal_btn"/>
    <input type="button" name="button2" value="取消" onclick="MyAlert('还未实现，请完成');" class="normal_btn"/>	
	<%
		}else{
	%>
	注：该抵扣单已结算，不可以再修改或编辑<br/>
	<input type="button" name="button3" value="修改" disabled="true" class="normal_btn"/>
    <input type="button" name="button2" value="取消" disabled="true" class="normal_btn"/>
	<% } %>
	<input name="Submit222" type="button" 
		class="normal_btn" value="关闭" onclick="window.close();"/>
</div>
	<script type="text/javascript">
	   var myPage;
	   //抵扣通知信息查询链接
	   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartDeduceNoticeManager/queryDeduceNoticeDetail.json";
	   url = url + "?ID=" + <%=request.getParameter("ID")%>;			
	   var title = null;
	   var columns = [
	  				{header: "序号",align:'center',renderer:getIndex},
	  				{id:'action',header: "索赔申请单号",dataIndex: 'CLAIM_NO',align:'center',renderer:claimDetail},
	  				{header: "VIN", dataIndex: 'VIN', align:'center'},
	  				{header: "项目类型", dataIndex: 'ITEM_TYPE', align:'right',renderer:getItemValue},
	  				{header: "项目代码", dataIndex: 'ITEM_CODE', align:'right'},
	  				{header: "项目名称", dataIndex: 'ITEM_NAME', align:'right'},
	  				{header: "抵扣费用", dataIndex: 'DEDUCT_MONEY', align:'center',renderer:formatCurrency},
	  				{header: "抵扣原因", dataIndex: 'DEDUCT_REASON', align:'center',renderer:getItemValue},
	  				{header: "备注", dataIndex: 'REMARK', align:'center'}
	  				];

	    function claimDetail(value,meta,record){
		    return String.format("<a href=\"<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?ID="
					+ record.data.CLAIM_ID + "\" target=\"_blank\">" + value + "</a>");
	    }
	 </script>
</body>
</html>
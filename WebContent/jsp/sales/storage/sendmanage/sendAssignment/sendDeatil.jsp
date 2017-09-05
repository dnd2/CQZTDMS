<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.TtDealerActualSalesAuditPO"%>
<%@ page import="com.infodms.dms.po.TtCustomerEditLogPO"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
	String contextPath=request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		   //初始化时间控件
	}

</script>

<title>发运分派信息查看</title>
</head>
<body onunload='javascript:destoryPrototype();'>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 销售管理 &gt; 整车物流管理 &gt; 发运分派查看</div>
<form id="fm" name="fm" method="post"><input type="hidden" name="curPage" id="curPage" value="1" /> <input type="hidden" id="dlrId" name="dlrId" value="" />

<table class="table_edit" align=center>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />订单信息</th>
	</tr>
	<tr>
		<td align=right>发运申请号：</td>
		<td align=left>${list.ORDER_NO}</td>
		<td align=right>订单类型：</td>
		<td align=left>${list.ORDER_STATUS}</td>
	</tr>
	<tr>
		<td align=right>订单数量：</td>
		<td align=left>${list.ORDER_NUM}</td>
		<td align=right>承运物流商：</td>
		<td align=left>${list.LOGI_NAME}</td>
	</tr>
	<tr>
		<td align=right>发运方式：</td>
		<td align=left>${list.SEND_STATUS}</td>
		<td align=right>发运省市：</td>
		<td align=left>${list.PCA_NAME}</td>
	</tr>
	<tr>
		<td align=right>收车经销商：</td>
		<td align=left>${list.DEALER_NAME}</td>
		<td align=right>收货地址：</td>
		<td align=left>${list.ADDRESS}</td>
	</tr>
	<tr>
		<td class="right">订单提报时间：</td>
		<td align="left">${list.RAISE_DATE}</td>
		<td align=right>&nbsp;</td>
		<td align="left">&nbsp;</td>
	</tr>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />组板信息</th>
	</tr>
	<tr>
		<td class="right">组板状态：</td>
		<td align="left">${list.BO_SA}</td>
		<td class="right">已组板数量：</td>
		<td width="35%" align="left">${list.ALLOCA_NUM}</td>
	</tr>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />发运信息</th>
	</tr>
	<tr>
		<td class="right">已配车数量：</td>
		<td align="left">${list.ALLOCA_NUM}</td>
		<td class="right">已出库数量：</td>
		<td align="left">${list.OUT_NUM}</td>
	</tr>
	<tr>
		<td class="right">发运数量：</td>
		<td align="left">${list.SEND_NUM}</td>
		<td class="right">已验收数量：</td>
		<td align="left">${list.ACC_NUM}</td>
	</tr>
	<tr>
		<td class="right">计划装车时间：</td>
		<td align="left">${list.PLAN_DATE}</td>
		<td class="right">说明：</td>
		<td align="left">${list.AS_REMARK}</td>
	</tr>
</table>
<table class="table_edit" align=center>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />分派信息</th>
	</tr>
	<tr>
		<td align=right>分派人：</td>
		<td align="left">${list.NAME}</td>
		<td align=right>分派时间：</td>
		<td align="left">${list.ASS_DATE}</td>
	</tr>
	<tr>
		<td align=right>分派备注：</td>
		<td align="left">${list.ASS_REMARK}</td>
		<td align=right>&nbsp;</td>
		<td align="left">&nbsp;</td>
	</tr>
	
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />发票信息</th>
	</tr>
	<tr>
		<td class="right">发票号：</td>
		<td align="left">${list.INVOICE_NO}</td>
		<td class="right">发票版本号：</td>
		<td align="left">${list.INVOICE_NO_VER}</td>
	</tr>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />经销商联系信息</th>
	</tr>
	<tr>
		<td align=right>联系人：</td>
		<td align="left">${list.LINK_MAN}</td>
		<td class="right">联系电话：</td>
		<td align="left">${list.TEL}</td>
	</tr>
</table>

</form>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td>
			<input name="backBtn" type="button" class="normal_btn" onclick="window.history.back();" value="返回" />		
		</td>
	</tr>
</table>
</div>
</body>
</html>
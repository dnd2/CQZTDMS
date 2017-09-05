<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.DecimalFormat"%> 
<%@ page import="com.infoservice.mvc.context.ActionContext"%>
<%@ page import="com.infodms.dms.po.TtDlrPayDetailsPO"%> 
<%@ page import="com.infodms.dms.actions.sales.accountsmanage.DlrPayInquiry"%>
<%
	String contextPath = request.getContextPath();
	//获取经销商付款信息数据
	ActionContext act = ActionContext.getContext();
	TtDlrPayDetailsPO tdpd = new TtDlrPayDetailsPO();
    tdpd = (TtDlrPayDetailsPO)act.getOutData("map");
	//设置开票日期格式
	Date PayDate =  tdpd.getPayDate();  
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String payDate = formatter.format(PayDate);
	
	DlrPayInquiry dpi = new DlrPayInquiry();
	//获取往来单位名称
	String contactDeptName = dpi.getContactDeptName();
	//获得账户类型名称
	String accountTypeName = dpi.getAccountName();
	//金额格式转换
	DecimalFormat df = new DecimalFormat("0.00"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<style type="text/css">   
 .table_edit,.table_edit td {   
    border:1px solid #cccccc;   
    border-collapse:collapse;  
}
</style>

<script type="text/javascript">
	//返回
	function toGoBack() {
		window.location = "<%=contextPath%>/sales/accountsmanage/DlrPayInquiry/dlrPayInquiryInit.do";
	}
</script>

</head>
<body onunload="javascript:destoryPrototype();">
	<div id="loader" style="position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;"></div>
			
<title>经销商付款查询</title>
<div class="wbox" id="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 财务管理 &gt; 账务收款 &gt; 经销商付款查询</div>
<form id="fm" name="fm" method="post"><input type="hidden" name="curPage" id="curPage" value="1"/> 
<div id="invoiceInfoId">
			<table class="table_query table_list" class="center" id="ctm_table_id">
				<tr class="tabletitle">
					<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>经销商付款信息</th>
				</tr>				
				<tr>
					<td width="15%" class=right>票号：</td>
					<td width="35%" class=left><%=CommonUtils.checkNull(tdpd.getTicketNo())%></td>
					<td width="15%" class=right>账户类型：</td>
					<td class=left><%=CommonUtils.checkNull(accountTypeName)%></td>
				</tr>
				
				<tr>
					<td width="15%" class=right>往来单位名称：</td>
					<td width="35%" class=left><%=CommonUtils.checkNull(contactDeptName)%></td>
					<td width="15%" class=right>金额：</td>
					<td class=left><%=CommonUtils.checkNull(df.format(tdpd.getPaySum()))%></td>
				</tr>
				
				<tr>
					<td width="15%" class=right>付款日期：</td>
					<td width="35%" class=left><%=payDate%></td>
					<td width="15%" class=right>备注：</td>
					<td class=left><textarea name='remark' id='remark'	 rows='3' cols='44' readonly style="border:0" ><%=CommonUtils.checkNull(tdpd.getRemark())%></textarea></td>
					
				</tr>
				
			</table>
		</div>

<table class="table_query" id="submitTable">
	<tbody><tr align="center">
		<td>		
			<input type="button" value="返 回" class="u-button u-reset" onclick="toGoBack();"/> 
		</td>
	</tr>
</tbody></table>
</form>
</div>

</body>
</html>

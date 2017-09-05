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
		window.location = "<%=contextPath%>/sales/financemanage/RebateDetailQuery/rebateDetailInit.do";
	}
</script>

</head>
<body onunload="javascript:destoryPrototype();">
	<div id="loader" style="position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;"></div>
			
<title>经销商付款录入</title>
<div class="wbox" id="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 财务管理 &gt; 返利管理 &gt;返利明细查询</div>
<form id="fm" name="fm" method="post"><input type="hidden" name="curPage" id="curPage" value="1"/> 
<div id="invoiceInfoId">
	<div class="form-panel">
		<h2>经销商付款录入</h2>
		<div class="form-body">	
			<table class="table_edit" class="center" id="ctm_table_id">
				<tbody>
					<tr>
						<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>经销商付款信息</th>
					</tr>
					<tr>
						<td width="15%" class="right " id="tcmtd">票号：</td>
						<td width="20%" class="left">
							<input id="ticketNo" name="ticketNo" value="<%=CommonUtils.checkNull(tdpd.getTicketNo())%>" type="text"  size="40" readonly  maxlength="30" style="border:0" /> 
						</td>
						<td width="15%" class="right" id="tcmtd">账户类型：</td>
						<td width="20%" class="left">
							<input id="payType" name="payType" value="<%=CommonUtils.checkNull(accountTypeName)%>" type="text"  size="40"  readonly maxlength="30" style="border:0" /> 
						</td>
						<td width="20%" class="right" id="sextd">往来单位名称：</td>
						<td align="left">
							<input name="contactDeptName" id="contactDeptName" value="<%=CommonUtils.checkNull(contactDeptName)%>" type="text" readonly  size="60" style="border:0" />
						</td>
					</tr>
					<tr>
						<td width="15%" class="right">金额：</td>
						<td width="20%" class="left">
							<input id="sum" name="sum" value="<%=CommonUtils.checkNull(df.format(tdpd.getPaySum()))%>" type="text" size="40" readonly  style="border:0;height:30px;" />
						</td>
						<td width="15%" class="right">付款日期：</td>
						<td width="20%" class="left">
							<input id="payDate" name="payDate" value="<%=payDate%>" type="text"  readonly  size="40"  style="border:0;height:30px;" />
						</td>
						<td width="20%"class="right">备注：</td>
						<td align="left">
							<textarea name='remark' id='remark'	 rows='2' cols='44' readonly style="border:0" ><%=CommonUtils.checkNull(tdpd.getRemark())%></textarea>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
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

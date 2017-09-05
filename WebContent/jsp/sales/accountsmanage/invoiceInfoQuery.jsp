<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.infoservice.mvc.context.ActionContext"%>
<%@ page import="com.infodms.dms.po.TtPurchaseInvoicePO"%> 
<%@ page import="com.infodms.dms.po.TtPurchaseInvoicesProductPO"%> 
<%@ page import="com.infodms.dms.actions.sales.accountsmanage.InvoiceManage"%> 
<%@ page import="java.text.DecimalFormat"%> 
<%
	String contextPath = request.getContextPath();
	//获取采购发票信息数据
	ActionContext act = ActionContext.getContext();
	TtPurchaseInvoicePO tpi = new TtPurchaseInvoicePO();  
	tpi = (TtPurchaseInvoicePO)act.getOutData("map");
	//销售订单NO
	String dlvryReqNo =tpi.getDlvryCode();
	//设置开票日期格式
	Date BillDate =  tpi.getTicketDate();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String billDate = formatter.format(BillDate);
	Long invoiceId = tpi.getInvoiceId();
	//获取采购产品信息数据
	InvoiceManage im = InvoiceManage.getInvoiceManage();
	List<TtPurchaseInvoicesProductPO> list = im.getProductByInvoiceId(String.valueOf(invoiceId));
	//开票单位名称
	String billDeptName = im.getBillDeptName();
	//收票单位名称
	String receiveDeptName = im.getReceiveDeptName();
	
	//获取产品编码
	List<String> productcodes = im.getProductCodes();
	//获取产品名称
	List<String> productNames = im.getProductNames();
	//获取账户类型名称
	String accountTypeName = im.getAccountName();
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
	function toGoBack1() {
		window.location = "<%=contextPath%>/sales/accountsmanage/InvoiceManage/invoiceManageInit.do";		
	}
	
	function toGoBack2(){
		window.location = "<%=contextPath%>/sales/accountsmanage/InvoiceManage/invoiceManageInitQuerypage.do";
	}
</script>


</head>
<body onunload="javascript:destoryPrototype();">
	<div id="loader" style="position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;"></div>
			
<title>采购发票查看</title>
<div class="wbox" id="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 来往账管理 &gt; 来往账管理 &gt; 采购发票查看</div>
<form id="fm" name="fm" method="post"><input type="hidden" name="curPage" id="curPage" value="1"/> 

<div id="invoiceInfoId">
<table class="table_query table_list" class="center" id="ctm_table_id">
	<tbody>
		<tr>
			<input type="hidden" id="invoiceId" name="invoiceId" value="<%=invoiceId%>" />
			<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>采购发票信息</th>
		</tr>
		<tr>
			<td  class="right" id="tcmtd">单据编码：</td>
			<td  class="left">
				<%=CommonUtils.checkNull(tpi.getInvoiceId())%>
			</td>
			<td class="right" id="tcmtd">采购发票号：</td>
			<td  class="left">
				<%=CommonUtils.checkNull(tpi.getPurchaseInvoiceNo())%> 
			</td>			
		</tr>
		<tr>
			<td  class="right">开票单位名称：</td>
			<td  class="left">
				<%=CommonUtils.checkNull(billDeptName)%>
			</td>
			<td  class="right">收票单位名称：</td>
			<td  class="left">
				<%=CommonUtils.checkNull(receiveDeptName)%>
			</td>
			
		</tr>
		<tr>
			<td  class="right">销售订单编号：</td>
			<td  class="left">
				<%=CommonUtils.checkNull(dlvryReqNo)%>
			</td>
			<td  class="right" id="sextd">开票日期：</td>
			<td class="left">
				<%=billDate%>
			</td>
		</tr>
		<tr>
			<td  class="right">合计金额：</td>
			<td  class="left">
				<%=CommonUtils.checkNull(df.format(tpi.getTotalAmount()))%>
			</td>
			<td  class="right">账户类型：</td>
			<td  class="left">
				<%=CommonUtils.checkNull(accountTypeName)%>
			</td>
			
		</tr>
		<tr>
			<td  class="right">备注：</td>
			<td  class="left">
				<textarea name='remark' id='remark'	 rows='2' cols='30' readonly style="border:0" ><%=CommonUtils.checkNull(tpi.getRemark())%></textarea>
			</td>
		</tr>
</tbody></table>
<table class="table_edit" class="center" id="ctm_table_id_2">
	<tbody>
	<tr>
		<th colspan="16"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>采购产品信息</th>
	</tr>
	<tr>
		<td class="center" >行号</td>
		<td class="center" >产品编码</td>
		<td class="center" >产品名称</td>
		<td class="center" >数量</td>
		<td class="center">含税单价</td>
		<td class="center" >含税金额</td>
		<td class="center">税率</td>
		<td class="center" >税额</td>
	</tr>
	<% 
		for(int i=0;i<list.size();i++){
			int rowNo = i+1;
	%>
	<tr>
		<td width="5%">
			<input id="rowNo" name="rowNo" value="<%=rowNo%>" type="text"  style="TEXT-ALIGN: center;border:0"  readonly />
		</td>
		<td width="20%">
			<input id="productCode" name="productCode" value="<%=CommonUtils.checkNull(productcodes.get(i))%>" type="text"  style="TEXT-ALIGN: center;border:0" maxlength="50" readonly />
		</td>
		<td width="20%">
			<input id="productName" value="<%=CommonUtils.checkNull(productNames.get(i))%>" name="productName" style="TEXT-ALIGN: center;border:0" type="text" maxlength="50" readonly />
		</td>
		<td  width="5%">
			<input id="amount" name="amount" value="<%=list.get(i).getAmount()%>" style="TEXT-ALIGN: center;border:0" type="text" maxlength="30" readonly />
		</td>
		<td  width="15%">
			<input id="taxPrice" name="taxPrice" value="<%=df.format(list.get(i).getTaxPrice())%>" style="TEXT-ALIGN: center;border:0" type="text" maxlength="30" readonly />
		</td>
		<td width="15%">
			<input id="taxTotalSum" name="taxTotalSum" value="<%=df.format(list.get(i).getTaxTotalSum())%>" style="TEXT-ALIGN: center;border:0" type="text" maxlength="30" readonly />
		</td>
		<td width="5%">
			<input name="taxRate" id="taxRate" value="<%=list.get(i).getTaxRate()%>" style="TEXT-ALIGN: center;border:0" type="text" maxlength="30" readonly />
		</td> 
		<td width="15%">
			<input name="taxSum" id="taxSum" value="<%=df.format(list.get(i).getTaxSum())%>" style="TEXT-ALIGN: center;border:0" type="text" maxlength="30" readonly />
		</td> 
	</tr>
	<% 
		}
	%>
</tbody></table>
</div>
<table class="table_query" id="submitTable">
	<tbody><tr class="center">
		<td>	
		 <c:if test="${returnWhere eq 1 }">	
			<input type="button" value="返 回" class="u-button u-reset" onclick="toGoBack1();"/> 
		 </c:if>
		 <c:if test="${returnWhere eq 2 }">	
			<input type="button" value="返 回" class="u-button u-reset" onclick="toGoBack2();"/> 
		 </c:if>
		</td>
	</tr>
</tbody></table>
</form>
</div>


</body>
</html>

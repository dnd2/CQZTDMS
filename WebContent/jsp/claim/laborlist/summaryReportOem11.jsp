<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">
function doInit(){
   	loadcalendar();  //初始化时间控件
}
</script>
<TITLE>售后服务管理</TITLE>

</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;
	应税劳务清单汇总报表收票</div>
    <form method="post" name ="fm" id="fm">
    <table  class="table_query">
		<tr>
			<td width="10%" align="right">报表参数编码：</td>
			<td width="20%" align="left">
				<input type="text" class="middle_txt" name="reportCode" id="reportCode"/>
			</td>
			<td width="10%" align="right">开票单位编码：</td>
			<td width="20%" align="left">
				<input type="text" class="middle_txt" name="code" id="code"/>
			</td>
		</tr>
		<tr>
			<td width="10%" align="right">开票单位名称：</td>
			<td width="20%" align="left">
				<input type="text" class="middle_txt" name="name" id="name"/>
			</td>
			<td width="10%" align="right">税率：</td>
			<td width="20%" align="left">
				<select name="tax" id="tax" class="short_sel">
					<option value="">所有</option>
					<option value="0.17">17%</option>
					<option value="0.06">6%</option>
					<option value="0.03">3%</option>
				</select>
			</td>
		</tr>
		<tr>
			<td width="10%" align="right">生产厂商：</td>
			<td width="20%" align="left">
				<div id="div1">
					<script type="text/javascript">
					genSelBoxContainStr("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'<%=CommonUtils.checkNull(request.getAttribute("yieldly"))%>');
					</script>
				</div>
			</td>
			<td width="10%" align="right">发票号：</td>
			<td width="20%" align="left">
				<input type="text" name="invoinceCode" id="invoinceCode" class="middle_txt"/>
			</td>
		</tr>
		<tr>
			<td width="10%" align="right">单据状态：</td>
			<td width="20%" align="left">
				<div id="div2">
					<script>
						genSelBoxExp("status","<%=Constant.TAXABLE_SERVICE_SUM%>","<%=Constant.TAXABLE_SERVICE_SUM_WAIT%>",true,"short_sel","","false","<%=Constant.TAXABLE_SERVICE_SUM_FANCE3%>,<%=Constant.TAXABLE_SERVICE_SUM_FANCE4%>");
					</script>
				</div>
			</td>
			<td colspan="2" align="center">
				<input type="button" value="查询" class="normal_btn" onclick="__extQuery__(1)"/>
			</td>
		</tr>
  </table>

	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

</form>
<script type="text/javascript">
var url = '<%=contextPath%>/claim/laborlist/LaborListSummaryReport/mainQuery11.json' ;
var title = null ;
var columns = [
               {header:'序号',width:'8%',align:'center',renderer:getIndex},
               {header:'报表参数编码',dataIndex:'SUM_PARAMETER_NO',width:'12%',align:'center'},
               {header:'购买方名称',dataIndex:'PURCHASER_ID',width:'8%',align:'center',renderer:getItemValue},
               {header:'经销商代码',dataIndex:'DEALER_CODE',width:'12%',align:'center'},
               {header:'经销商名称',dataIndex:'DEALER_NAME',width:'12%',align:'center'},
               {header:'税率',dataIndex:'TAX_RATE',width:'10%',align:'center'},
               {header:'发票号码',dataIndex:'INVOICE_NO',width:'12%',align:'center'},
               {header:'状态',dataIndex:'AUTH_STATUS',width:'12%',align:'center',renderer:getItemValue},
               {header:'合计金额',dataIndex:'AMOUNT',width:'12%',align:'center'},
               {header:'操作',width:'10%',align:'center',renderer:myHandler}
               ];
function myHandler(value,meta,record){
	if(record.data.LIST_STATUS == '11551004'){
		return '<a href="<%=contextPath%>/claim/laborlist/LaborListSummaryReport/showDetailOem22.do?id='+record.data.SUM_PARAMETER_ID+'&code='+record.data.SUM_PARAMETER_NO+'">[明细]</a>'
	}else{
		if(record.data.AUTH_STATUS == '11881000'){
			return "<a href='#' onclick='getInvoice3(\""+record.data.SUM_PARAMETER_ID+"\",\""+1+"\")'>[签收]</a>|<a href='#' onclick='getInvoice1(\""+record.data.SUM_PARAMETER_ID+"\",\""+record.data.SUM_PARAMETER_NO+"\",\""+1+"\")'>[收票]</a>|<a href="+"<%=contextPath%>/claim/laborlist/LaborListSummaryReport/showDetailOem22.do?id="+record.data.SUM_PARAMETER_ID+"&code="+record.data.SUM_PARAMETER_NO+">[明细]</a>|<a href='#' onclick=\"window.open('<%=contextPath%>/claim/laborlist/LaborListSummaryReport/prinReimbursement.do?id="+record.data.SUM_PARAMETER_ID+"','','left=0,top=0,width="+screen.availWidth +"- 10,height="+screen.availHeight+"-50,toolbar=no,menubar=no,scrollbars=no,location=no');\">[报销单打印]</a>";
		}else if(record.data.AUTH_STATUS == '11881001'){
		  return "<a href='#' onclick='getInvoice(\""+record.data.SUM_PARAMETER_ID+"\",\""+2+"\")'>[财务挂账]</a>|<a href="+"<%=contextPath%>/claim/laborlist/LaborListSummaryReport/showDetailOem22.do?id="+record.data.SUM_PARAMETER_ID+"&code="+record.data.SUM_PARAMETER_NO+">[明细]</a>|<a href='#' onclick='getInvoice2(\""+record.data.SUM_PARAMETER_ID+"\",\""+3+"\")'>[退票]</a>|<a href='#' onclick=\"window.open('<%=contextPath%>/claim/laborlist/LaborListSummaryReport/prinReimbursement.do?id="+record.data.SUM_PARAMETER_ID+"','','left=0,top=0,width="+screen.availWidth +"- 10,height="+screen.availHeight+"-50,toolbar=no,menubar=no,scrollbars=no,location=no');\">[报销单打印]</a>";
		}else {
		  return '<a href="<%=contextPath%>/claim/laborlist/LaborListSummaryReport/showDetailOem22.do?id='+record.data.SUM_PARAMETER_ID+'&code='+record.data.SUM_PARAMETER_NO+'">[明细]</a>'+"|<a href='#' onclick='getInvoice4(\""+record.data.SUM_PARAMETER_ID+"\",\""+4+"\")'>[退帐]</a>|<a href='#' onclick=\"window.open('<%=contextPath%>/claim/laborlist/LaborListSummaryReport/prinReimbursement.do?id="+record.data.SUM_PARAMETER_ID+"','','left=0,top=0,width="+screen.availWidth +"- 10,height="+screen.availHeight+"-50,toolbar=no,menubar=no,scrollbars=no,location=no');\">[报销单打印]</a>";
		}
	}
}
function getInvoice(record,value){
	var id = record;
	if(confirm('确认操作？')){
		var url = '<%=contextPath%>/claim/laborlist/LaborListSummaryReport/getInvoice.json?id='+id+'&flag='+value ;
		makeNomalFormCall(url,getCallback,'fm');
	}
}
function getInvoice2(record,value){
	var id = record;
	if(confirm('确认操作？')){
		var url = '<%=contextPath%>/claim/laborlist/LaborListSummaryReport/getInvoice.json?id='+id+'&flag='+value ;
		makeNomalFormCall(url,getCallback,'fm');
	}
}
function getInvoice3(record,value){
	var id = record;
	if(confirm('确认操作？')){
		var url = '<%=contextPath%>/claim/laborlist/LaborListSummaryReport/getInvoice.json?id='+id+'&flag='+value ;
		makeNomalFormCall(url,getCallback,'fm');
	}
}
function getInvoice4(record,value){
	var id = record;
	if(confirm('确认操作？')){
		var url = '<%=contextPath%>/claim/laborlist/LaborListSummaryReport/getInvoice.json?id='+id+'&flag='+value ;
		makeNomalFormCall(url,getCallback,'fm');
	}
}
//开票
function getInvoice1(record,code,value){
	location.href='<%=contextPath%>/claim/laborlist/LaborListSummaryReport/showDetailOem11.do?id='+record+'&code='+code ;
}
function getCallback(json){
	if(json.flag){
		MyAlert('操作已成功！');
		if (parent.$('inIframe')) {
		 parentContainer.refreshWindow();
	    } else {
		 parent.refreshWindow();
	   }		
     }
 }
 function refreshWindow(){
  //MyAlert('刷新');
  __extQuery__(1); 
 }	
</script>
</BODY>
</html>
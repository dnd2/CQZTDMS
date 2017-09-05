<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
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

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;厂端查询打印汇总报表</div>
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
						genSelBoxExp("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'');
					</script>
				</div>
			</td>
			<td width="10%" align="right">发票号：</td>
			<td width="20%" align="left">
				<input type="text" name="invoinceCode" id="invoinceCode" class="middle_txt"/>
			</td>
		</tr>
		<tr style="display:none">
			<td width="10%" align="right">单据状态：</td>
			<td width="20%" align="left">
				<div id="div2">
					<script>
						genSelBoxExp("status","<%=Constant.LABOR_LIST_STATUS%>","",true,"short_sel","","false","");
					</script>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<input type="button" value="查询" class="normal_btn" onclick="doQuery()" />
			</td>
		</tr>
  </table>

	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

</form>
<script type="text/javascript">
var url = '<%=contextPath%>/claim/laborlist/LaborListAction/oemQuery.json' ;
var title = null ;
var columns = [
               {header:'序号',width:'8%',align:'center',renderer:getIndex},
               {header:'汇总单号',dataIndex:'SUM_PARAMETER_NO',width:'12%',align:'center'},
               {header:'购买方名称',dataIndex:'PURCHASER_ID',width:'8%',align:'center',renderer:getItemValue},
               {header:'经销商代码',dataIndex:'DEALER_CODE',width:'12%',align:'center'},
               {header:'经销商名称',dataIndex:'DEALER_NAME',width:'12%',align:'center'},
               {header:'税率',dataIndex:'TAX_RATE',width:'10%',align:'center'},
               {header:'发票号码',dataIndex:'INVOICE_NO',width:'12%',align:'center'},
               {header:'操作',width:'10%',align:'center',renderer:myHandler}
               ];
function doQuery(){
	__extQuery__(1) ;
}
function showReportCode(){
	$('genBtn').disabled = false ;
	var url = '<%=contextPath%>/claim/laborlist/LaborListSummaryReport/queryReportUrlInit.do' ;
	OpenHtmlWindow(url,800,500);
}
function setReportCode(id,code,did,dcode,dname,invoice_code,yieldly,status,amount){
	$('reportId').value=id;
	$('reportCode').value=code;
	$('code').value=dcode;
	$('name').value=dname;
	$('yieldly').value = yieldly ; 
	$('status').value = status ;
	$('invoinceCode').value = invoice_code ;
}
function wrapOut(){
	setReportCode('','','','','','','','','')
}
function myHandler(value,meta,record){
	return res = "<a href='#' onclick=\"window.open('<%=contextPath%>/claim/laborlist/LaborListSummaryReport/printSum.do?id="+record.data.SUM_PARAMETER_ID+"','','left=0,top=0,width="+screen.availWidth +"- 10,height="+screen.availHeight+"-50,toolbar=no,menubar=no,scrollbars=no,location=no');\">[打印]</a>";
}
</script>
</BODY>
</html>
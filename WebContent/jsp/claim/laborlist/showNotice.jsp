<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>   
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<body>
<div class="navigation">
	<img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：报表参数选择
	</div>
	<form name = "fm" id="fm">
		<table align=center class=table_query>
			<tr>
				<td width="10%" align="right">单据编号：</td>
				<td width="20%">
					<input type="text" name="ro_no" class="middle_txt"/>
				</td>
				<td width="10%" align="right">经销商代码：</td>
				<td width="20%">
					<input type="text" name="dealerCode" class="middle_txt"/>
				</td>
			</tr>
			<tr>
				<td width="10%" align="right">经销商名称：</td>
				<td width="20%">
					<input type="text" name="dealerName" class="middle_txt"/>
				</td>
				<td colspan="2" align="center">
					<input type="button" name="btnQuery" value="查询" class="normal_btn" onclick="__extQuery__(1);"/>
					&nbsp;&nbsp;
					<input type="button" value="关闭" class="normal_btn" onclick="_hide();"/>
				</td>
			</tr>
		</table>
		<input type="hidden" name="selPartIds" value="${selPartIds}"/>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>

<script language="JavaScript">
var url = "<%=contextPath %>/claim/laborlist/LaborListForTax/noticeQuery.json";

var title= null;

var columns= [
		{header: '序号', align:'center', renderer:getIndex,width:'7%'},
		{header: '选择',  align:'center',renderer:myCheckBox, width:'10%'},
		{header: '单号', dataIndex:'BALANCE_NO',align:'center',width:'10%'},
		{header: '经销商代码',dataIndex:'DEALER_CODE',align:'center',width:'10%'},
		{header: '经销商名称',dataIndex:'DEALER_NAME',align:'center',width:'10%'},
		{header: '开票方名称', dataIndex:'INVOICE_MAKER',align:'center',width:'10%'},
		{header: '生产厂商',dataIndex:'YIELDLY',align:'center',width:'10%',renderer:getItemValue},
		{header: '状态',dataIndex:'STATUS',align:'center',width:'10%',renderer:getItemValue}
     ];
function myCheckBox(val,meta,rec){
	var ipt='';
		ipt+= '<input type="radio" onclick="selReport(\'' ;
		ipt+=rec.data.ID+"','" ;
		ipt+=rec.data.BALANCE_NO+"','" ;
		ipt+=rec.data.DEALER_ID+"','" ;
		ipt+=rec.data.DEALER_CODE+"','" ;
		ipt+=rec.data.DEALER_NAME+"','" ;
		ipt+=rec.data.INVOICE_MAKER+"','" ;
		ipt+=rec.data.YIELDLY+"','" ;
		ipt+=rec.data.START_DATE+"','" ;
		ipt+=rec.data.END_DATE+"','" ;
		ipt+=rec.data.STATUS+"');\">" ;
	return String.format(ipt);
}
var pWin=parentContainer;
function selReport(id,ro_no,did,dcode,dname,maker,yieldly,sd,ed,status){
	if(pWin.setNotic!=undefined)
		pWin.setNotic(id,ro_no,did,dcode,dname,maker,yieldly,sd,ed,status);
	else
		MyAlert('调用父页面setNotic方法出现异常!');
	_hide();
}
__extQuery__(1);

</script>
</body>
</html>
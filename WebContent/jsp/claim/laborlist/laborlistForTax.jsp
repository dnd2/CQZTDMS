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

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;
	应税劳务清单</div>
    <form method="post" name ="fm" id="fm">
    <table  class="table_query">
		<tr>
			<td width="10%" align="right">开票通知单：</td>
			<td width="20%" align="left">
				<input type="text" class="middle_txt" name="ro_no" id="ro_no" readOnly/>
				<span style="color:red">*</span>
				<input type="button" value="..." class="mini_btn" onclick="showNotice();"/>
				<input type="button" class="normal_btn" value="清除" onclick="wrapOut();"/>
				<input type="hidden" name="id" id="id"/>
			</td>
			<td width="10%" align="right">购买方名称：</td>
			<td width="20%" align="left">
				<script>
					genSelBoxExp("yieldly","<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>","",true,"short_sel","","false","");
				</script>
			</td>
		</tr>
		<tr>
			<td width="10%" align="right">销售方名称：</td>
			<td width="20%" align="left">
				<input type="text" class="middle_txt" name="saleName" id="saleName"/>
				<input type="hidden" name="saleId" id="saleId"/>
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
			<td width="10%" align="right">号码：</td>
			<td width="20%" align="left">
				<input type="text" class="middle_txt" name="number" id="number"/>
				<span style="color:red">*</span>				
			</td>
			<td width="10%" align="right">所属增值税专用发票代码：</td>
			<td width="20%" align="left">
				<input type="text" class="middle_txt" name="invoinceCode" id="invoinceCode"/>
				<span style="color:red">*</span>
			</td>
		</tr>
		<tr>
			<td width="10%" align="right">维修时间：</td>
			<td width="20%">
				<input type="text" class="short_txt" name="beginDate" id="beginDate"  datatype="1,is_date,10" group="beginDate,endDate" hasbtn="true" callFunction="showcalendar(event, 'beginDate', false);"/>
					至
				<input type="text" class="short_txt" name="endDate" id="endDate"  datatype="1,is_date,10" group="beginDate,endDate" hasbtn="true" callFunction="showcalendar(event, 'endDate', false);"/>
			</td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<input type="button" class="normal_btn" value="查询" onclick="__extQuery__(1)"/>
				&nbsp;
				<input type="button" class="normal_btn" id="genBtn" value="生成" onclick="genFun();"/>				
			</td>
		</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript">
var url = '<%=contextPath%>/claim/laborlist/LaborListForTax/mainQuery.json' ;
var title = null ;
var columns = [
               {header:'序号',width:'8%',align:'center',renderer:getIndex},
               {header:'开票通知单',dataIndex:'BALANCE_NO',width:'10%',align:'center'},
               {header:'购买方',dataIndex:'PURCHASER_NAME',width:'10%',align:'center'},
               {header:'销售方代码',dataIndex:'SALES_CODE',width:'10%',align:'center'},
               {header:'销售方名称',dataIndex:'SALES_NAME',width:'10%',align:'center'},
               {header:'维修开始时间',dataIndex:'CLAIM_START_DATE',width:'10%',align:'center'},
               {header:'维修结束时间',dataIndex:'CLAIM_END_DATE',width:'10%',align:'center'},
               {header:'号码',dataIndex:'NO',width:'10%',align:'center'},
               {header:'发票代码',dataIndex:'INVOICE_NO',width:'10%',align:'center'},
               {header:'税率',dataIndex:'TAX_RATE',width:'8%',align:'center'},
               {header:'操作',width:'8%',align:'center',renderer:myHandler}
               ] ;
function myHandler(value,meta,record){
	var vv = '<a href="#" onclick="delThis('+record.data.BALANCE_ID+')">[删除]</a>' ;
	return res = "<a href='#' onclick=\"window.open('<%=contextPath%>/claim/laborlist/LaborListForTax/getTaxableService.do?id="+record.data.BALANCE_ID+"&tax="+record.data.TAX_RATE+"','','left=0,top=0,width="+screen.availWidth +"- 10,height="+screen.availHeight+"-50,toolbar=no,menubar=no,scrollbars=no,location=no');\">[打印]</a>"+vv ;
	//return '<a href="#" onclick="genFun('+record.data.BALANCE_ID+','+record.data.TAX_RATE+')">[明细]</a>' ;
}
function genFun(value,tax){
	if(value){
		MyAlert(value);
		window.open('<%=contextPath%>/claim/laborlist/LaborListForTax/getTaxableService.do?id='+value+'&tax='+tax);
	}else{
		$('genBtn').disabled = true ;
		var id = $('id').value ;
		var no = $('number').value ;
		var tax = $('tax').value ;
		var no2 = $('invoinceCode').value ;
		if(id==''){
			MyAlert('开票通知单为必填项！');
			return ;
		}
		if(no==''){
			MyAlert('号码为必填项！');
			return ;
		}
		if(no2==''){
			MyAlert('发票代码为必填项！');
			return ;
		}
		window.open('<%=contextPath%>/claim/laborlist/LaborListForTax/getTaxableService.do?id='+id+'&no='+no+'&tax='+tax+'&no2='+no2) ;		
	}
}
function delThis(val){
	//var id = $('id').value ;
	//if(id==''){
		//MyAlert('开票通知单为必填项！');
		//return ;
	//}
	if(confirm('确认删除?')){
		var url = '<%=contextPath%>/claim/laborlist/LaborListForTax/deleteTaxService.json?id='+val ;
		makeNomalFormCall(url,delCallback,'fm');
	}else return ;
}
function delCallback(json){
	if(json)
		MyAlert('删除此开票通知单对应的应税劳务清单成功！');
	__extQuery__(1) ;
}
function showNotice(){
	$('genBtn').disabled = false ;
	var url = '<%=contextPath%>/claim/laborlist/LaborListForTax/noticeUrlInit.do' ;
	OpenHtmlWindow(url,800,500);
}
function setNotic(id,ro_no,did,dcode,dname,maker,yieldly,sd,ed,status){
	$('ro_no').value = ro_no ;
	$('id').value = id ;
	$('yieldly').value = yieldly ;
	$('saleName').value = maker ;
	$('saleId').value = did ;
	$('beginDate').value = sd ;
	$('endDate').value = ed ;
}
function wrapOut(){
	setNotic('','','','','','','','','','');
}
</script>
</BODY>
</html>
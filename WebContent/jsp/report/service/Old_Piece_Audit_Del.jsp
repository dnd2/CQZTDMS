<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>旧件审核扣件明细表</TITLE>
<% 
	String contextPath = request.getContextPath();
   List<Map<String, Object>> list = (List<Map<String, Object>>)request.getAttribute("list");
%>
<SCRIPT LANGUAGE="JavaScript">

		      
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
   		
   		
	}
	var myPage;
	//查询路径           
 	var url = "<%=contextPath%>/report/service/OldPieceAuditDel/OldPieceAuditDelQueryData.json";
	var title = null;
	var columns = [

				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商全称", dataIndex: 'DEALER_NAME', align:'center'},				
				{header: "小区", dataIndex: 'ORG_NAME', align:'center'},
				{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "结算单号", dataIndex: 'CLAIM_NO', align:'center'},
				{header: "故障件代码（换下件）", dataIndex: 'PART_CODE', align:'center'},
				{header: "故障件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "申报金额", dataIndex: 'PRICE', align:'center'},
				{header: "回运数", dataIndex: 'RETURN_NUM', align:'center'},
				{header: "签收数", dataIndex: 'SIGN_NUM', align:'center'},
				{header: "扣件原因", dataIndex: 'DEDUCT_REMARK', align:'center'},
				{header: "条码", dataIndex: 'BARCODE_NO', align:'center'},
				{header: "旧件审核日期", dataIndex: 'REPORT_DATE', align:'center'}
	

		      ]; 
</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;旧件审核扣件明细表</div>
  <form method="post" name = "fm" id="fm">
	
    <TABLE align=center width="95%" class="table_query" >
        <tr>
        <td align="right">结算单号：</td>
		<td>
             <input id="settlementNo" class="middle_txt" name="settlementNo" />

		</td>
             <td  align="right">VIN：</td>
             <td> <input id="vin" class="middle_txt" name="vin" />
			</td>
          </tr>
        <tr>
		<td width="15%" align="right">扣减原因：</td>
		<td width="30%" align="left">
					<script type="text/javascript">
			    genSelBoxExp("OLDPART_DEDUCT_TYPE",<%=Constant.OLDPART_DEDUCT_TYPE%>,"",true,"short_sel","","false",'');
			</script>
			
		 </td>
		 <td align="right">经销商代码：</td>
			<td align="left">
				<input type="text" name="dealer_code" id="dealer_code" readonly="readonly" class="long_txt"/>
				<input type="hidden" name="dealer_id" id="dealer_id"/>
				<input type="button" class="mini_btn" value="..." onclick="showOrgDealer('dealer_code','dealer_id',true,'',true,'','10771002');"/>
            	<input type="button" class="normal_btn" value="清除" onclick="clrTxt('dealer_id','dealer_code')"/>
			</td>

		         
        </tr>
	<tr>

		<td width="15%" align="right">小区：</td>
		<td width="30%" align="left">
			<select class="short_sel" id="small_org" name="small_org" value="${small_org }">
				<option value="">--请选择--</option>
				<%for(int i=0;i<list.size();i++){%>
					<option value="<%=list.get(i).get("ORG_ID") %>"><%=list.get(i).get("ORG_NAME") %></option>
				<%} %>
			</select>
		 </td>
		  <td  align="right">经销商全称：</td>
             <td>  <input name="dealerName" id="dealerName" value="" type="text" class="middle_txt" />
			</td>
	        </tr>

			<tr>
					<td width="15%" align="right">旧件审核日期：</td>
			<td align="left" nowrap="true">
				<input name="bDate" type="text" class="short_time_txt" id="bDate" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bDate', false);" />  	
	             &nbsp;至&nbsp; <input name="eDate" type="text" class="short_time_txt" id="eDate" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'eDate', false);" /> 
			</td>
			</tr>

			<tr>

            <td colspan="6" align="center" nowrap><input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="查询"  onclick="__extQuery__(1);" />
            <input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="导出excel"  onclick="goImport();" />
            </td>
          </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->

</form>


<br>

<SCRIPT LANGUAGE="JavaScript">
function   showMonthFirstDay()     
{     
	  var   Nowdate=new   Date();     
	  var   MonthFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth(),1);     
	  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
}     
function   showMonthLastDay()     
{     
	  var   Nowdate=new   Date();     
	  var   MonthNextFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
	  var   MonthLastDay=new   Date(MonthNextFirstDay-86400000);     
	  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
}     
// $('CREATE_DATE_STR').value=showMonthFirstDay();
$('bDate').value=showMonthFirstDay();
$('eDate').value=showMonthLastDay();

function clrTxt(value1,value2){
	document.getElementById(value1).value = "";
	document.getElementById(value2).value = "";

}
//清空
function clrTxt1(str){
	document.getElementById(str).value = "";
}

function goImport(){
	
    fm.action = '<%=contextPath%>/report/service/OldPieceAuditDel/OldPieceAuditDelExport.do';
    fm.submit();
}
</script>

</BODY>
</html>
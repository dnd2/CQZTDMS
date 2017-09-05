<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>运费结算明细</TITLE>
<% 
	String contextPath = request.getContextPath();
%>
<SCRIPT LANGUAGE="JavaScript">

		      
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
   		
   		
	}
	var myPage;
	//查询路径           
 	var url = "<%=contextPath%>/report/service/FreightBillingDetails/FreightBillingDetailstQueryData.json";
	var title = null;
	var columns = [

				{header: "一级站代码",dataIndex: 'ROOT_DEALER_CODE',align:'center'},
				{header: "服务站代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "服务站名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "结算年月",dataIndex: 'BALANCE_MONTH',align:'center'},
				{header: "运费",dataIndex: 'TRAN_SUM',align:'center'},
				{header: "结算基地",dataIndex: 'BALANCE_YIELDLY',align:'center'},
				{header: "系统确认时间",dataIndex: 'FI_MONTH',align:'center'}


		      ]; 
</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;运费结算明细</div>
  <form method="post" name = "fm" id="fm">
	
    <TABLE align=center width="95%" class="table_query" >
  			<tr>
			 <td align="right">服务站代码：</td>
			<td align="left">
          <input id="supply_code" name="supply_code" value="" type="text" class="middle_txt" >

			</td>
			 <td  align="right">服务站名称：</td>
             <td>  <input name="supply_name" id="supply_name" value="" type="text" class="middle_txt" />
			</td>

			</tr>
                 <tr>
        <td align="right">结算基地：</td>
		<td>
			<script type="text/javascript">
			    genSelBoxExp("YIELDLY_TYPE",9541,"",true,"short_sel","","false",'');
			</script>
		</td>
						<td width="15%" align="right">结算年月：</td>
			<td align="left" nowrap="true">
				<input name="bgDate" type="text" class="short_time_txt" id="bgDate" group="bgDate,egDate"  datatype="1,is_date,10"readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bgDate', false);" />  	
	             &nbsp;至&nbsp; <input name="egDate" type="text" class="short_time_txt" group="bgDate,egDate"  datatype="1,is_date,10"id="egDate" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'egDate', false);" /> 
			</td>
		
			</tr>

			<tr>
					<td width="15%" align="right">系统确认时间：</td>
			<td align="left" nowrap="true">
				<input name="bDate" type="text" class="short_time_txt" id="bDate" group="bDate,eDate"  datatype="1,is_date,10"readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bDate', false);" />  	
	             &nbsp;至&nbsp; <input name="eDate" type="text" class="short_time_txt" group="bDate,eDate"  datatype="1,is_date,10"id="eDate" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'eDate', false);" /> 
			</td>
		
			</tr>

			
			<tr>

            <td colspan="6" align="center" nowrap><input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="查询"  onclick=" __extQuery__(1);" />
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
//$('bDate').value=showMonthFirstDay();
//$('eDate').value=showMonthLastDay();


//清空
function clrTxt1(str){
	document.getElementById(str).value = "";
}

function goImport(){
	
    fm.action = '<%=contextPath%>/report/service/FreightBillingDetails/FreightBillingDetailstExport.do';
    fm.submit();
}


</script>

</BODY>
</html>
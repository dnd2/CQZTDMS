<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>售后服务情况报告</TITLE>
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
 	var url = "<%=contextPath%>/report/service/FreightBillingDetails/AfterSalesServiceQueryData.json";
	var title = null;
	var columns = [

				{header: "类型",dataIndex: 'TYPE',align:'center'},
				{header: "当年单月",dataIndex: 'MNTOTAL',align:'center'},
				{header: "前年单月",dataIndex: 'MYTOTAL',align:'center'},
				{header: "前年单月比",dataIndex: 'MPER',align:'center'},
				{header: "当年月累计",dataIndex: 'YNTOTAL',align:'center'},
				{header: "前年月累计",dataIndex: 'YYTOTAL',align:'center'},
				{header: "前年累计比",dataIndex: 'YPER',align:'center'}


		      ]; 
</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;售后服务情况报告</div>
  <form method="post" name = "fm" id="fm">
	
    <TABLE align=center width="95%" class="table_query" >



			<tr>
					<td width="15%" align="right">系统确认时间：</td>
			<td align="left" nowrap="true">
				<input name="bDate" type="text" class="short_time_txt" id="bDate"  datatype="0,is_date,10"readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bDate', false);" />  	 
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
$('bDate').value=showMonthLastDay();


//清空
function clrTxt1(str){
	document.getElementById(str).value = "";
}

function goImport(){
	
    fm.action = '<%=contextPath%>/report/service/FreightBillingDetails/AfterSalesServiceExport.do';
    fm.submit();
}


</script>

</BODY>
</html>
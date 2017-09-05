<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>结算明细</TITLE>
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
 	var url = "<%=contextPath%>/report/service/BaseReport/SettlementWorkloadQueryData.json";
	var title = null;
	var columns = [
				{header: "服务站代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "服务站名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "小区",dataIndex: 'REGION_NAME',align:'center'},
				{header: "索赔单数",dataIndex: 'APPSUM',align:'center'},
				{header: "特殊单数",dataIndex: 'SFEESUM',align:'center'},
				{header: "单据合计",dataIndex: 'COUNTSUM',align:'center'},
				{header: "劳务费",dataIndex: 'LABOUR_AMOUNT',align:'center'},
				{header: "材料费",dataIndex: 'PART_AMOUNT',align:'center'},
				{header: "救急费",dataIndex: 'NETITEM_AMOUNT',align:'center'},
				{header: "费用合计",dataIndex: 'REPAIR_TOTAL',align:'center'},
				{header: "结算劳务费",dataIndex: 'BALANCE_LABOUR_AMOUNT',align:'center'},
				{header: "结算材料费",dataIndex: 'BALANCE_PART_AMOUNT',align:'center'},
				{header: "结算救急费",dataIndex: 'BALANCE_NETITEM_AMOUNT',align:'center'},
				{header: "结算合计",dataIndex: 'BALANCE_AMOUNT',align:'center'},
				{header: "审去劳务费",dataIndex: 'DIS_LABOUR_AMOUNT',align:'center'},
				{header: "审去材料费",dataIndex: 'DIS_PART_AMOUNT',align:'center'},
				
				{header: "审去救急费",dataIndex: 'DIS_NETITEM_AMOUNT',align:'center'},
				{header: "审去合计",dataIndex: 'DIS_AMOUNT',align:'center'}

		      ]; 
</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;结算室审核工作量</div>
  <form method="post" name = "fm" id="fm">
	
    <TABLE align=center width="95%" class="table_query" >
       
         <tr>
	        <td align="right">结算基地：</td>
			<td>
				<script type="text/javascript">
				    genSelBoxExp("YIELDLY_TYPE",9541,"",true,"short_sel","","false",'');
				</script>
			</td>
			<td width="15%" align="right">系统确认时间：</td>
			<td align="left" nowrap="true">
			<input name="bDate" type="text" class="short_time_txt" id="bDate" group="bDate,eDate"  datatype="1,is_date,10"readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bDate', false);" />  	
	            &nbsp;至&nbsp; <input name="eDate" type="text" class="short_time_txt" group="bDate,eDate"  datatype="1,is_date,10"id="eDate" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'eDate', false);" /> 
			</td>
						
			</tr>

			<tr>
				 <td align="right">服务商商代码：</td>
				 <td align="left">
	              <input id="supply_code" name="supply_code" value="" type="text" class="middle_txt" >
				 </td>
				 <td  align="right">服务商全称：</td>
	             <td>  <input name="supply_name" id="supply_name" value="" type="text" class="middle_txt" />
				</td>
			</tr>
			<tr>
			  <td align="right">小区</td>
			  <td width="30%" align="left">
				<select class="short_sel" id="small_org" name="small_org" value="${small_org }">
					<option value="">--请选择--</option>
					<%for(int i=0;i<list.size();i++){%>
						<option value="<%=list.get(i).get("ORG_ID") %>"><%=list.get(i).get("ORG_NAME") %></option>
					<%} %>
				</select>
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
	
    fm.action = '<%=contextPath%>/report/service/BaseReport/SettlementWorkloadExport.do';
    fm.submit();
}


</script>

</BODY>
</html>
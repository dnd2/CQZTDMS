<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>外部质量损失汇总表</TITLE>
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
 	var url = "<%=contextPath%>/report/service/FreightBillingDetails/ExternalQualityQueryData.json";
	var title = null;
	var columns = [

				{header: "车系",dataIndex: 'SERIES',align:'center'},
				{header: "车型",dataIndex: 'MODEL_CODE',align:'center'},
				
				{header: "强保费<br/>工时费",dataIndex: 'FL_AMOUNT',align:'center'},
				{header: "强保费<br/>材料费", dataIndex: 'FP_AMOUNT', align:'center'},
				{header: "强保费<br/>小计", dataIndex: 'F_AMOUNT', align:'center'},
				
				{header: "售前费<br/>工时费",dataIndex: 'QL_AMOUNT',align:'center'},
				{header: "售前费<br/>材料费", dataIndex: 'QP_AMOUNT', align:'center'},
				{header: "售前费<br/>小计", dataIndex: 'Q_AMOUNT', align:'center'},
				
				{header: "保修费<br/>工时费",dataIndex: 'BL_AMOUNT',align:'center'},
				{header: "保修费<br/>材料费", dataIndex: 'BP_AMOUNT', align:'center'},
				{header: "保修费<br/>派出费", dataIndex: 'BW_AMOUNT', align:'center'},
				{header: "保修费<br/>小计", dataIndex: 'B_AMOUNT', align:'center'},
				
				{header: "索赔费<br/>工时费",dataIndex: 'TL_AMOUNT',align:'center'},
				{header: "索赔费<br/>材料费", dataIndex: 'TP_AMOUNT', align:'center'},
				//{header: "索赔费<br/>派出费", dataIndex: 'D_MILEAGE', align:'center'},
				{header: "索赔费<br/>小计", dataIndex: 'T_AMOUNT', align:'center'},
				
				{header: "退货损失费<br/>工时费",dataIndex: 'HL_AMOUNT',align:'center'},
				{header: "退货损失费<br/>材料费", dataIndex: 'HP_AMOUNT', align:'center'},
				{header: "退货损失费<br/>小计", dataIndex: 'H_AMOUNT', align:'center'},
				
				{header: "批量费<br/>工时费",dataIndex: 'PL_AMOUNT',align:'center'},
				{header: "批量费<br/>材料费", dataIndex: 'PP_AMOUNT', align:'center'},
				{header: "批量费<br/>管理费", dataIndex: 'PG_AMOUNT', align:'center'},
				{header: "批量费<br/>小计", dataIndex: 'P_AMOUNT', align:'center'}
				


		      ]; 
</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;外部质量损失汇总表</div>
  <form method="post" name = "fm" id="fm">
	
    <TABLE align=center width="95%" class="table_query" >

        <tr>
       <td align="right">车系：</td>
		         <td>
		        	<input type="text" name="serisid" size="15" class="middle_txt" />
		         </td>
		 <td align="right">车型：</td>
         <td>
             <input type="text"  name="groupCode" class="middle_txt" size="15" id="groupCode" value=""  />
         </td>
		     </tr>    

			<tr>
					<td width="15%" align="right">结算上报日期：</td>
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
	
    fm.action = '<%=contextPath%>/report/service/FreightBillingDetails/ExternalQualityExport.do';
    fm.submit();
}


</script>

</BODY>
</html>
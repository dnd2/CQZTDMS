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
%>
<SCRIPT LANGUAGE="JavaScript">

		      
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
   		
   		
	}
	var myPage;
	//查询路径           
 	var url = "<%=contextPath%>/report/service/BaseReport/SettlementSummaryDelQueryData.json";
	var title = null;
	var columns = [
				{header: "维修类型",dataIndex: 'TYPE',align:'center'},
				{header: "单据号码",dataIndex: 'ORDER_NO',align:'center'},
				{header: "一级站代码",dataIndex: 'ROOT_DEALER_CODE',align:'center'},
				{header: "服务站代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "活动名称",dataIndex: 'ACTIVITY_NAME',align:'center'},
				{header: "车系",dataIndex: 'SERIES',align:'center'},
				{header: "车型",dataIndex: 'MODEL',align:'center'},
				{header: "发动机号码",dataIndex: 'ENGINE_NO',align:'center'},
				{header: "VIN号码",dataIndex: 'VIN',align:'center'},
				{header: "销售日期",dataIndex: 'BUY_DATE',align:'center'},
				{header: "维修日期",dataIndex: 'FINE_DATE',align:'center'},
				{header: "换上件代码",dataIndex: 'PART_CODE',align:'center'},
				{header: "换上件名称",dataIndex: 'PART_NAME',align:'center'},
				{header: "数量",dataIndex: 'NUM',align:'center'},
				{header: "作业代码",dataIndex: 'LABOUR_CODE',align:'center'},
				{header: "作业名称",dataIndex: 'LABOUR_NAME',align:'center'},
				
				{header: "上报工时费",dataIndex: 'LABOUR_SUM',align:'center'},
				{header: "上报材料费",dataIndex: 'PART_SUM',align:'center'},
				{header: "上报派出费用",dataIndex: 'SEND_SUM',align:'center'},
				{header: "上报赠送费用",dataIndex: 'FREE_SUM',align:'center'},
				
				{header: "工时费",dataIndex: 'B_LABOUR_SUM',align:'center'},
				{header: "材料费",dataIndex: 'B_PART_SUM',align:'center'},
				{header: "外派费用",dataIndex: 'B_SEND_SUM',align:'center'},
				{header: "赠送费用	",dataIndex: 'B_FREE_SUM',align:'center'},

				{header: "其他费用", dataIndex: 'OTHER_SUM', align:'center'},
				{header: "结算基地", dataIndex: 'BALANCE_YIELD', align:'center'},
				{header: "系统确认时间", dataIndex: 'FI_DATE', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center'}

		      ]; 
</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;结算明细</div>
  <form method="post" name = "fm" id="fm">
	
    <TABLE align=center width="95%" class="table_query" >
        <tr>
       <td align="right">车系选择：</td>
		         <td>
	         		<select id="serisid" name="serisid" style="width: 152px;">
		        		<option value="">-- 请选择 --</option>
		        		<c:forEach items="${serislist }" var="list">
		        			<option value="${list.GROUP_ID }">${list.GROUP_NAME }</option>
		        		</c:forEach>
		        	</select>
		         </td>
		 <td align="right">车型选择：</td>
         <td>
             <input type="text"  name="groupCode" class="middle_txt" readonly="readonly" size="15" id="groupCode" value=""  jset="para"/>
	         <input name="button3" type="button" class="normal_btn"  style="width:20px;" onclick="showMaterialGroup('groupCode','','true','3')" value="..." />
	         <input class="normal_btn" type="button" value="清空" onclick="clrTxt1('groupCode');"/>
         </td>
         </tr>
                 <tr>
        <td align="right">结算基地：</td>
		<td>
			<script type="text/javascript">
			    genSelBoxExp("YIELDLY_TYPE",9541,"",true,"short_sel","","false",'');
			</script>
		</td>
						   <td  align="right">结算单号：</td>
             <td> <input id="settlement_no" class="middle_txt" name="settlement_no" />
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
			    <td  align="right">VIN：</td>
             <td> <input id="vin" class="middle_txt" name="vin" />
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
		     <td align="right">配件代码：</td>
			 <td align="left">
             <input id="part_code" name="part_code" value="" type="text" class="middle_txt" >
			</td>
			 <td  align="right">配件名称：</td>
             <td>  <input name="part_name" id="part_name" value="" type="text" class="middle_txt" />
			</td>
			</tr>
			<tr>
			 <td align="right">作业代码：</td>
			  <td align="left">
              <input id="homework_code" name="homework_code" value="" type="text" class="middle_txt" >
			</td>
			 <td  align="right">作业名称：</td>
             <td>  <input name="homework_name" id="homework_name" value="" type="text" class="middle_txt" />
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
	
    fm.action = '<%=contextPath%>/report/service/BaseReport/SettlementSummaryDelExport.do';
    fm.submit();
}


</script>

</BODY>
</html>
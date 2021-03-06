<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>出门证及索赔通知单</TITLE>
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
 	var url = "<%=contextPath%>/report/service/BaseReport/ShipperClaimQueryData.json";
	var title = null;
	var columns = [
				{header: "出门证号",dataIndex: 'OUT_NO',align:'center'},
				{header: "出门证生产时间(天)",dataIndex: 'DOOR_DATE',align:'center'},
				{header: "出门单位",dataIndex: 'OUT_COMPANY',align:'center'},
				{header: "型号", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "出门证数量", dataIndex: 'OUT_NUM', align:'center'},
				{header: "索赔通知单号", dataIndex: 'NOTICE_NO', align:'center'},
				{header: "开票时间", dataIndex: 'NOTICE_DATE', align:'center'},
				{header: "索赔数量", dataIndex: 'OUT_NUM1', align:'center'},
				{header: "索赔单价", dataIndex: 'PART_PRICE', align:'center'},
				{header: "工时定额", dataIndex: 'LABOUR_PRICE', align:'center'},
				{header: "金额总计", dataIndex: 'TOTAL', align:'center'},
				{header: "开票经办", dataIndex: 'NAME', align:'center'},
				{header: "备注", dataIndex: 'OUT_REMARK', align:'center'}


		      ]; 
</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;出门证及索赔通知单</div>
  <form method="post" name = "fm" id="fm">
	
    <TABLE align=center width="95%" class="table_query" >
        <tr>
        <td align="right">结算基地：</td>
		<td>
			<script type="text/javascript">
			    genSelBoxExp("YIELDLY_TYPE",9541,"",true,"short_sel","","false",'');
			</script>
		</td>
						   <td  align="right">型号：</td>
             <td> <input id="model_name" class="middle_txt" name="model_name" />
			</td>
			</tr>
			<tr>
			   <td  align="right">出门单位：</td>
             <td> <input id="out_company" class="middle_txt" name="out_company" />
			</td>
			   <td  align="right">索赔通知单：</td>
             <td> <input id="notice_no" class="middle_txt" name="notice_no" />
			</td>
			
		
			</tr>
		         
   
			<tr>

					<td width="15%" align="right">出门证生产时间：</td>
			<td align="left" nowrap="true">
             <input name="bDate" type="text" class="short_time_txt" id="bDate" group="bDate,eDate" datatype="1,is_date,10" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bDate', false);" />  	
	             &nbsp;至&nbsp; <input name="eDate" type="text" class="short_time_txt" group="bDate,eDate" datatype="1,is_date,10" id="eDate" readonly="readonly"/> 
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
//$('bDate').value=showMonthFirstDay();
//$('eDate').value=showMonthLastDay();


//清空
function clrTxt1(str){
	document.getElementById(str).value = "";
}

function goImport(){
	
    fm.action = '<%=contextPath%>/report/service/BaseReport/ShipperClaimExport.do';
    fm.submit();
}
</script>

</BODY>
</html>
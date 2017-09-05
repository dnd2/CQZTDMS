<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>车辆PIN码导入查询</TITLE>
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
 	var url = "<%=contextPath%>/repairOrder/VehicleQueryPin/queryVehicleQueryPin.json";
	var title = null;
	var columns = [
				{header: "车系",dataIndex: 'SERIES_NAME',align:'center'},
				{header: "车型",dataIndex: 'MODEL_NAME',align:'center'},
				{header: "VIN",dataIndex: 'VIN',align:'center'},
				{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
				{header: "生产时间", dataIndex: 'PRODUCT_DATE', align:'center'},				
				{header: "销售时间", dataIndex: 'PURCHASED_DATE', align:'center'},
				{header: "PIN码", dataIndex: 'PIN', align:'center'},
				{header: "导入人", dataIndex: 'IMPORT_PEOPLE', align:'center'},
				{header: "导入时间", dataIndex: 'IMPORT_DATE', align:'center'}

		      ]; 
</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;车辆信息管理&gt;车辆PIN码导入查询</div>
  <form method="post" name = "fm" id="fm">
	
    <TABLE align=center width="95%" class="table_query" >

        <tr>
       <td align="right">车系：</td>
		         <td>
		        	 <input type="text"  name="serisCode" class="middle_txt" size="15" id="serisCode" value=""  jset="para"/>
		         </td>
		 <td align="right">车型：</td>
         <td>
             <input type="text"  name="groupCode" class="middle_txt" size="15" id="groupCode" value=""  jset="para"/>
         </td>
		         
        </tr>


			<tr>
		 <td align="right">PIN码：</td>
			<td align="left">
				<input type="text" name="PIN" id="PIN" size="15"  class="middle_txt" />

			</td>
			 <td  align="right">销售时间：</td>
			<td align="left" nowrap="true">
				<input name="bDate" type="text" class="short_time_txt" group="bDate,eDate" datatype="1,is_date,10" id="bDate" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bDate', false);" />  	
	             &nbsp;至&nbsp; <input name="eDate" type="text"  class="short_time_txt" id="eDate" group="bDate,eDate" datatype="1,is_date,10"  readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'eDate', false);" /> 
			</td>
			</tr>
			<tr>
					<td width="15%" align="right">导入人：</td>
			<td align="left" nowrap="true">
				<input type="text" name="Importpeople" value="" class="middle_txt" id="Importpeople" 	/>

			</td>
			<td width="15%" align="right">发动机号：</td>
			<td width="30%" align="left">
				<input type="text" name="engine_no" value="" class="middle_txt" id="engine_no"/>
				
		  </td>
			</tr>
			<tr>
			<td width="15%" align="right">导入时间：</td>
			<td align="left" nowrap="true">
				<input name="bgDate" type="text" class="short_time_txt" id="bgDate" group="bgDate,egDate" datatype="1,is_date,10"  readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bgDate', false);" />  	
	             &nbsp;至&nbsp; <input name="egDate" type="text" class="short_time_txt" id="egDate" group="bgDate,egDate" datatype="1,is_date,10"  readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'egDate', false);" /> 
			</td>
			<td width="15%" align="right">PIN是否为空：</td>
			<td width="30%" align="left">
				<select name="SelectPin">
				<option value="0">否</option>
				<option value="1">是</option>
				</select>
		  </td>
			</tr>

			<tr>

            <td colspan="6" align="center" nowrap><input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="查询"  onclick="__extQuery__(1);" />
            <input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="导入"  onclick="goImport();" />
            <input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="导出excel"  onclick="goExport();" />
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

function showActivity(inputCode ,inputName ,isMulti , areaId, ids, productId){
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+"/jsp/report/service/show_activity_del.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&areaId="+areaId+"&ids="+ids+"&productId="+productId,730,390);
}
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
//$('bgDate').value=showMonthFirstDay();
//$('egDate').value=showMonthLastDay();
//$('crbDate').value=showMonthFirstDay();
//$('creDate').value=showMonthLastDay();

function clrTxt(value1,value2){
	document.getElementById(value1).value = "";
	document.getElementById(value2).value = "";

}
//清空
function clrTxt1(str){
	document.getElementById(str).value = "";
}

function goExport(){
	
    fm.action = '<%=contextPath%>/repairOrder/VehicleQueryPin/VehicleQueryPinExport.do';
    fm.submit();
}
function goImport(){
	
    fm.action = '<%=contextPath%>/repairOrder/VehicleQueryPin/VehicleQueryPinimport.do';
    fm.submit();
}
</script>

</BODY>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>维修频次统计</TITLE>
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
 	var url = "<%=contextPath%>/report/service/BaseReport/MaintenanceFrequencyQueryData.json";
	var title = null;
	var columns = [
				{header: "车系",dataIndex: 'GROUP_NAME',align:'center'},
				{header: "销售总数",dataIndex: 'S_TOTAL',align:'center'},
				{header: "维修台次",dataIndex: 'R_TOTAL',align:'center'},
				{header: "总费用", dataIndex: 'R_AMOUNT', align:'center'},
				{header: "单次频次",dataIndex: 'RD_TIMES',align:'center'},
				{header: "单车费用",dataIndex: 'RD_AMOUNT',align:'center'}


		      ]; 
</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;维修频次统计</div>
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
			<td width="15%" align="right">索赔类型：</td>
			<td align="left" nowrap="true">
	         <input type="checkbox"  name="CLA_TYPE" value="<%=Constant.CLA_TYPE_01%>" >正常维修</input>&nbsp;
	         <input type="checkbox"  name="CLA_TYPE" value="<%=Constant.CLA_TYPE_07%>" >售前维修</input>&nbsp;
	         <input type="checkbox"  name="CLA_TYPE" value="<%=Constant.CLA_TYPE_09%>" >外派维修</input>&nbsp;
				
			 </td>
		         
   
			<tr>
		<td width="15%" align="right">配件代码：</td>
			<td align="left" nowrap="true">
	      <input class="middle_txt" type="text" name="PART_OLDCODE" id="PART_OLDCODE"/>
	      <input type="hidden" name="PART_CODE" id="PART_CODE"/>
	      <input type="hidden" name="PART_CNAME" id="PART_CNAME"/>
	      <input type="hidden" name="PART_ID" id="PART_ID"/>
	      <input class="mark_btn" type="button" value="&hellip;" onclick="showPartInfo('PART_CODE','PART_OLDCODE','PART_CNAME','PART_ID','true')"/>
          <input class="normal_btn" type="button" value="清空" onclick="clrTxt1('PART_CODE','PART_OLDCODE','PART_CNAME','PART_ID');"/>
 
			</td>
					<td width="15%" align="right">生产日期：</td>
			<td align="left" nowrap="true">
				<input name="bDate" type="text" class="short_time_txt" id="bDate" group="bDate,eDate" datatype="0,is_date,10" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bDate', false);" />  	
	             &nbsp;至&nbsp; <input name="eDate" type="text" class="short_time_txt" group="bDate,eDate" datatype="0,is_date,10"id="eDate" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'eDate', false);" /> 
			</td>

			</tr>
			<tr>
				<td width="15%" align="right">销售时间标准：</td>
			<td align="left" nowrap="true">
	
          		<select id="bgDate" name="bgDate" style="width: 152px;">
		        			<option value="30">30</option>
		        			<option value="60">60</option>
		        			<option value="90">90</option>
		        			<option value="180">180</option>
		        	</select>
			</td>
			
			
			</tr>
			
			<tr>

            <td colspan="6" align="center" nowrap><input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="查询"  onclick="chaxun();" />
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

//清空
function clrTxt1(str,str1,str2,str3){
	document.getElementById(str).value = "";
	document.getElementById(str1).value = "";
	document.getElementById(str2).value = "";
	document.getElementById(str3).value = "";
}
function chaxun(){
	var bDate=document.getElementById("bDate").value;
	var eDate=document.getElementById("eDate").value;
  if(!DiffLong(bDate,eDate)){
	MyAlert("时间跨度不能超过180天");
	return;
  }else{
	  __extQuery__(1);
  }
}

function goImport(){
	
    fm.action = '<%=contextPath%>/report/service/BaseReport/MaintenanceFrequencyExport.do';
    fm.submit();
}
function DiffLong(datestr1, datestr2) {

    var date1 = new Date(Date.parse(datestr1.replace(/-/g, "/")));
    var date2 = new Date(Date.parse(datestr2.replace(/-/g, "/")));
    var datetimeTemp;
    var isLater = true;

    if (date1.getTime() > date2.getTime()) {
        isLater = false;
        datetimeTemp = date1;
        date1 = date2;
        date2 = datetimeTemp;
    }

    difference = date2.getTime() - date1.getTime();
    thisdays = Math.floor(difference / (1000 * 60 * 60 * 24));

    difference = difference - thisdays * (1000 * 60 * 60 * 24);
    thishours = Math.floor(difference / (1000 * 60 * 60));


   // var strRet = thisdays + '天' + thishours + '小时';
   
   var strRet = thisdays + '天';
   if(thisdays>180){
	  return false; 
   }
    return true;
}

</script>

</BODY>
</html>
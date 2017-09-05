<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>结算数量金额明细查询</TITLE>
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
 	var url = "<%=contextPath%>/report/service/BaseReport/SettlementAmountSubsidiaryQueryData.json";
	var title = null;
	var columns = [

				{header: "车系",dataIndex: 'SERIES_NAME',align:'center'},
				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "故障件名称", dataIndex: 'MAL_NAME', align:'center'},
				{header: "供应商", dataIndex: 'MAKER_NAME', align:'center'},
				{header: "更换件数量", dataIndex: 'BALANCE_QUANTITY', align:'center'},
				{header: "结算金额", dataIndex: 'BALANCE_AMOUNT', align:'center'}


		      ]; 
</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;结算数量金额明细查询</div>
  <form method="post" name = "fm" id="fm"  enctype="multipart/form-data" >
	
    <TABLE align=center width="95%" class="table_query" >
        
			<tr>

					<td width="15%" align="right">系统确认时间：</td>
			<td align="left" nowrap="true">
				<input name="bDate" type="text" class="short_time_txt" id="bDate" group="bDate,eDate" datatype="1,is_date,10" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bDate', false);" />  	
	             &nbsp;至&nbsp; <input name="eDate" type="text" class="short_time_txt" group="bDate,eDate" datatype="1,is_date,10" id="eDate" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'eDate', false);" /> 
			</td>
			</tr>
			
			<tr>

            <td colspan="6" align="center" nowrap><input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="查询"  onclick="__extQuery__(1);" />
            <input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="导出excel"  onclick="goImport();" />&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="file" name="uploadFile" id="uploadFile" style="width:150px" value=""/>
            <input id="uploadBtn" type="button" class="normal_btn" onclick="uploadExcel();" value="导入模板"/>
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
 function uploadExcel() 
 {
    	var str = document.getElementById('uploadFile').value; //得到浏览文件的路径值,是否为空
    	if("" == str) {
    		MyAlert("请选择上传文件!");
    	}else{
    		fm.action = "<%=contextPath%>/customerRelationships/baseSetting/CRMSortManager/readExcel01.do";
			fm.submit();
    	}
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


//清空
function clrTxt1(str){
	document.getElementById(str).value = "";
}
function clrTxt(value1,value2){
	document.getElementById(value1).value = "";
	document.getElementById(value2).value = "";
 
}


function goImport(){
	
    fm.action = '<%=contextPath%>/report/service/BaseReport/SettlementAmountSubsidiaryExport.do';
    fm.submit();
}
</script>

</BODY>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<TITLE>工单维护</TITLE>
</HEAD>
<BODY onload="doInit()">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔管理&gt;工单维护</div>
  <form method="post" name ="fm" id="fm">
<input type="hidden" name="id" id="id" value="<c:out value="${id}"/>"/>
<input type="hidden" name="channel" value="${channel}"/>
<input type="hidden" name="yieldly" value="${yieldly}"/>
    <TABLE  class="table_query">
          <tr>
            <td  class="table_query_2Col_label_5Letter">工单单号：</td>
            <td><input name="RO_NO" id="RO_NO" value="" type="text" datatype="1,is_digit_letter,20" class="middle_txt" />
            </td>
             <td class="table_query_2Col_label_7Letter">工单开始日期：</td>
             <td nowrap="nowrap">
            	<input class="short_txt" type="text" name="RO_STARTDATE" id="RO_STARTDATE"  datatype="1,is_date,10" group="RO_STARTDATE,RO_ENDDATE" hasbtn="true" callFunction="showcalendar(event, 'RO_STARTDATE', false);"/>
           		 至
            	<input class="short_txt" type="text" name="RO_ENDDATE" id="RO_ENDDATE"  datatype="1,is_date,10" group="RO_STARTDATE,RO_ENDDATE" hasbtn="true" callFunction="showcalendar(event, 'RO_ENDDATE', false);"/>
  			</td>           
          </tr>
          <tr>
 			<td  class="table_query_2Col_label_5Letter">VIN：</td>
 			<td  align="left">
 				<textarea name="VIN" cols="18" rows="3" datatype="1,is_digit_letter"></textarea>
 			</td>
            <td align="right" nowrap>
            	<input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="__extQuery__(1);" />
				&nbsp;
    		<input class="normal_btn" type="button" value="关闭" onclick="_hide();">
			</td>
          </tr>                   
  </table>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
  <form name="form1" style="display:none">
   <table class="table_list" id="table1" >
  	  <tr>
  	  	<th align="center">
    		<input class="normal_btn" type="button" value="确定" onclick="putForwordConfirm()">
    		&nbsp;
    		<input class="normal_btn" type="button" value="关闭" onclick="_hide();">
       </th>
  	  </tr>
   </table>
  </form>
<SCRIPT LANGUAGE="JavaScript">

	document.form1.style.display = "none";
	
	var HIDDEN_ARRAY_IDS=['form1'];
	
	var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/roQuery.json";
				
	var title = null;

	var columns = [
					{header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"ids\")' />", width:'8%',sortable: false,dataIndex: 'ID',renderer:myCheckBox},
					{header: "工单单号", width:'15%', dataIndex: 'RO_NO'},
					{header: "工单开始日期", width:'15%', dataIndex: 'CREATE_DATE',renderer:fmtDate},
					{header: "车系", width:'15%', dataIndex: 'SERIES'},
					{header: "车型", width:'15%', dataIndex: 'MODEL'},
					{header: "VIN", width:'15%', dataIndex: 'VIN'},
					{header: "发动机号", width:'15%', dataIndex: 'ENGINE_NO'},
					{header: "生产日期", width:'15%', dataIndex: 'PRODUCT_DATE',renderer:fmtDate},
					{header: "里程(公里)", width:'15%', dataIndex: 'IN_MILEAGE'}
		      ];
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
	}

	function fmtDate(value,meta,rec){
		return String.format(value.substring(0,10));
	}
	
	function getYuan(value,metaDate,record)
	{
		if(value == 0)
		{
			return "0.00";
		}
		else
		{
			return amountFormat(value);
		}
	}
	
	function putForwordConfirm()
	{
		makeNomalFormCall("<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/addRoToR.json",showForwordValue,'fm','queryBtn'); 
	}
	
	//提报回调函数
	function showForwordValue(json)
	{
		if(json.returnValue == '1')
		{
			var val = document.getElementById("id").value;
			parentContainer.akka(val);
			_hide();
		}else
		{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	
	//全选checkbox
	function myCheckBox(value,metaDate,record)
	{
		return String.format("<input type='checkbox' name='ids' value='" + value + "'/>");
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
	  $('RO_STARTDATE').value=showMonthFirstDay();
	  $('RO_ENDDATE').value=showMonthLastDay();

//设置超链接 end
</script>
</BODY>
</html>
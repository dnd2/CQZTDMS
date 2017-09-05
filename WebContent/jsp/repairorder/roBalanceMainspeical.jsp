<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>索赔单维护</TITLE>
<SCRIPT LANGUAGE="JavaScript">
	var myPage;
	//查询路径
	var url = "<%=contextPath%>/repairOrder/RoMaintainMain/queryRepairOrderSpecial.json";
				
	var title = null;

	var columns = [
					{id:'action',header: "选择",sortable: false,dataIndex: 'id',renderer:mySelect,align:'center'},
					{header: "工单号", width:'15%', dataIndex: 'roNo'},
					{header: "维修类型", width:'7%', dataIndex: 'repairTypeCode',renderer:getItemValue},
					{header: "车牌号", width:'7%', dataIndex: 'license'},
					//{header: "提交次数", width:'5%', dataIndex: 'submitTimes'},
					{header: "VIN", width:'15%', dataIndex: 'vin'},
					{header: "车型", width:'15%', dataIndex: 'model'},
					{header: "车主", width:'15%', dataIndex: 'ownerName'},
					{header: "工单开始时间", width:'15%', dataIndex: 'roCreateDate',renderer:formatDate},
					{header: "进厂里程数", width:'15%', dataIndex: 'inMileage'},
					{header: "工单状态", width:'15%', dataIndex: 'roStatus',renderer:getItemValue},
					{header: "预授权状态", width:'15%', dataIndex: 'forlStatus',renderer:getItemValue}
					//{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'id',renderer:myLink1,align:'center'}
		      ];
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
	}
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' value="+record.data.id+" onclick='setRoSpecial(\""+record.data.id+"\")'/>");
	}

	function setRoSpecial(id){

	}

	//格式化时间为YYYY-MM-DD
	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
	
	//具体操作
	function sel(){
		MyAlert("超链接！");
	}
	
	function ROMerge(){
		var v=document.getElementsByName('rd');
		var j=0;
		for (var i=0;i<v.length;i++){
		 if(v.item(i).checked){
			 var id = v.item(i).value;
			 var url = "<%=contextPath%>/repairOrder/RoMaintainMain/ROMerge.json";
			 var roId = $('RO_ID').value;
		    	makeCall(url,verMergeRoBack,{ID:id,RO_ID:roId});
		   j++;
		 }
		}
		if(j<1){
		MyAlert(" 一个位选中");
		}

	}
	function verMergeRoBack(json){
	if(json.sucess==true){
		MyAlert("合并成功");
		parentContainer.__extQuery__(1);
		_hide();
		}else{
		MyAlert("合并失败");
		}
	}
	
	//返回
	function goBack(){
		window.location.href = "<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyQuery.json";
	}
//设置超链接 end
</script>
</HEAD>
<BODY onload="doInit()">
<div class="navigation"><img src="../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;维修登记&gt;特殊工单查询</div>
  <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
          <tr>
            <td  class="table_query_2Col_label_7Letter">工单号：</td>
            <td><input name="RO_NO" id="RO_NO" value="" type="text" datatype="1,is_digit_letter,50" class="middle_txt" />
            <input name="RO_ID" id="RO_ID" value="${id}" type="hidden"  />
            </td>
            <td rowspan="2" class="table_query_2Col_label_7Letter" >VIN：</td>
 			<td rowspan="2"  align="left">
 			<!--  <input name="VIN" id="VIN" datatype="1,is_vin" type="text"  value="" class="middle_txt"/>-->
 			<textarea name="VIN" cols="18" rows="3" datatype="1,is_digit_letter"></textarea>
 			</td>
 			
          </tr>
          <tr>
            <td  class="table_query_2Col_label_7Letter">维修类型：</td>
            <td >
            <script type="text/javascript">
	              genSelBoxExp("REPAIR_TYPE",<%=Constant.REPAIR_TYPE%>,"",true,"short_sel","","true",'');
	       	</script>
 		    </td> 	         
          </tr>    
                         
          <tr>
            <td class="table_query_2Col_label_7Letter">工单开始日期：</td>
             <td nowrap="nowrap">
            <input class="short_txt" type="text" name="RO_CREATE_DATE" id="RO_CREATE_DATE"  datatype="1,is_date,10" group="RO_CREATE_DATE,DELIVERY_DATE" hasbtn="true" callFunction="showcalendar(event, 'RO_CREATE_DATE', false);"/>
            至
            <input class="short_txt" type="text" name="DELIVERY_DATE" id="DELIVERY_DATE"  datatype="1,is_date,10" group="RO_CREATE_DATE,DELIVERY_DATE" hasbtn="true" callFunction="showcalendar(event, 'DELIVERY_DATE', false);"/>
  			</td>
            <td  class="table_query_2Col_label_7Letter">申请单状态：</td>
  			<td >
  			<script type="text/javascript">
	              genSelBoxExp("RO_STATUS",<%=Constant.RO_STATUS%>,"",true,"short_sel","","true",'');
	        </script>
  			</td>
          </tr>
          <tr>
          	<td class="table_query_2Col_label_7Letter">
          		授权：
          	</td>
          	<td >
          	  	<script type="text/javascript">
	              genSelBoxExp("RO_FORE",<%=Constant.RO_FORE%>,"",true,"short_sel","","true",'');
	        </script>
          	</td>
          	<td class="table_query_2Col_label_7Letter">工单创建日期：</td>
             <td nowrap="nowrap">
            <input class="short_txt" type="text" name="CREATE_DATE_STR" id="CREATE_DATE_STR"  datatype="1,is_date,10" group="CREATE_DATE_STR,CREATE_DATE_END" hasbtn="true" callFunction="showcalendar(event, 'CREATE_DATE_STR', false);"/>
            至
            <input class="short_txt" type="text" name="CREATE_DATE_END" id="CREATE_DATE_END"  datatype="1,is_date,10" group="CREATE_DATE_STR,CREATE_DATE_END" hasbtn="true" callFunction="showcalendar(event, 'CREATE_DATE_END', false);"/>
  			</td>
          </tr>
    	  <tr>
            <td colspan="4" align="center" nowrap><input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="__extQuery__(1);" />
			<input name="button" id="queryBtn" type="button" onclick="ROMerge()" class="normal_btn"  value="合并" />
			<input name="button" id="queryBtn" type="button" onclick="_hide()" class="normal_btn"  value="关闭" />
			</td>
			 
            <td  align="right" ></td>
  </table>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</BODY>
<script type="text/javascript">
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

      
  $('CREATE_DATE_STR').value=showMonthFirstDay();
  $('CREATE_DATE_END').value=showMonthLastDay();
  $('RO_CREATE_DATE').value=showMonthFirstDay();
  $('DELIVERY_DATE').value=showMonthLastDay();
</script>
</html>
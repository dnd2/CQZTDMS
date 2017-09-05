<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat" %>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<title>服务商三包结算总表</title>
<head>
<SCRIPT LANGUAGE="JavaScript">
function doInit()
{
		loadcalendar();  //初始化时间控件
		genLocSel('province','','','','',''); // 加载省份城市和县
}
var myPage;
	//查询路径
	var url = "<%=contextPath%>/report/jcafterservicereport/ServicerBalanceReport/viewServicerStatus.json";
				
	var title = null;

	var columns = [
					{header: "序号", width:'10%',renderer:getIndex},
					{header: "服务商代码", width:'10%', dataIndex: 'DEALER_CODE'},
					{header: "服务商名称名称", width:'11%', dataIndex: 'DEALER_NAME'},
					{header: "厂家", width:'15%', dataIndex: 'YIELDLY',renderer:getItemValue},
					{header: "大区", width:'15%', dataIndex: 'ROOT_ORG_NAME'},
					{header: "省份", width:'7%', dataIndex: 'REGION_NAME'},
					{header: "一月", width:'5%', dataIndex: 'STATUS_1',renderer:getItemValue2},
					{header: "二月", width:'5%', dataIndex: 'STATUS_2',renderer:getItemValue2},
					{header: "三月", width:'5%', dataIndex: 'STATUS_3',renderer:getItemValue2},
					{header: "四月", width:'5%', dataIndex: 'STATUS_4',renderer:getItemValue2},
					{header: "五月", width:'5%', dataIndex: 'STATUS_5',renderer:getItemValue2},
					{header: "六月", width:'5%', dataIndex: 'STATUS_6',renderer:getItemValue2},
					{header: "七月", width:'5%', dataIndex: 'STATUS_7',renderer:getItemValue2},
					{header: "八月", width:'5%', dataIndex: 'STATUS_8',renderer:getItemValue2},
					{header: "九月", width:'5%', dataIndex: 'STATUS_9',renderer:getItemValue2},
					{header: "十月", width:'5%', dataIndex: 'STATUS_10',renderer:getItemValue2},
					{header: "十一月", width:'5%', dataIndex: 'STATUS_11',renderer:getItemValue2},
					{header: "十二月上", width:'5%', dataIndex: 'STATUS_12',renderer:getItemValue2},
					{header: "十二月下", width:'5%', dataIndex: 'STATUS_13',renderer:getItemValue2}
					
		      ];
	</SCRIPT>
</head>
<body>
<script type="text/javascript">
function exportExcle(){
	var fm = document.getElementById('fm');
	fm.action='<%=request.getContextPath()%>/report/jcafterservicereport/ServicerBalanceReport/viewServicerReportExcel.do';
	fm.submit();
	
}

</script>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;轿车售后索赔报表&gt;服务商三包结算总表</div>
  
<form action="post" name="fm" id="fm">
 <table class="table_query">
	 <tr>
	 	<td align="right" class="table_query_2Col_label_6Letter">服务商代码：</td>
	
		        <td  >
            	<input class="middle_txt" id="dealerCode" name="DEALER_CODE" value="" type="text"/>
            	<input class="mark_btn" type="button" value="&hellip;" onclick="showOrgDealer('DEALER_CODE','','true','','true')"/>   
            	<input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/> 
				</td>
		<td align="right" class="table_query_2Col_label_6Letter">服务商名称：</td>	
		<td  align="left"><input type="text" NAME="DEALER_NAME" value=""/>
		</td>
	</tr>
	<tr>
	    <td id="deId2" class="table_query_2Col_label_6Letter" align="right">大区：</td>
	       	  <td  id="deId3" align="left" nowrap="true" nowrap>
	          <input name="orgCode" type="text" id="orgCode" size="23" value="" readonly/>
	          <input name="orgId" type="hidden" id="orgId" size="40" value="" />
			  <input name="orgSel" type="button" class="mini_btn" onClick="showOrg('orgCode' ,'orgId' ,true,'')" value="&hellip;" />
			  <input class="cssbutton" type="button" value="清空" onClick="clrTxt('orgId','orgCode');"/>
        </td>
			<td align="right" nowrap="nowrap" class="table_query_2Col_label_6Letter">省份：</td>
            <td colspan="2" align="left">
				<select class="short_sel" id="province" name="PROVICE_ID"></select>
			</td>
	</tr>
		<tr>
		<td align="right" class="table_query_2Col_label_6Letter">厂家：</td>
		<td align="left">
		  <script type="text/javascript">
		      
		      	genSelBoxContainStr("YILYIE",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"true",true,"short_sel","","true",'<%=Constant.SERVICEACTIVITY_CAR_YIELDLY_01%>,<%=Constant.SERVICEACTIVITY_CAR_YIELDLY_02%>,<%=Constant.SERVICEACTIVITY_CAR_YIELDLY_03%>,<%=Constant.SERVICEACTIVITY_CAR_YIELDLY_04%>');
			      </script>
		 </td>
	</tr>
			<tr>
		<td align="right" class="table_query_2Col_label_6Letter">年份：</td>
		<td align="left">
		<select name="year">
		<option>2010</option>
		<option>2011</option>
		<option>2012</option>
		</select>
		
		</td>
	</tr>
	<tr align="center"> 
	   <td>&nbsp;</td> <td>&nbsp;</td><td>&nbsp;</td>
	  <td align="left"><input type="button" value="查询" class="normal_btn" onClick="__extQuery__(1);"/> <input type="button"  class="normal_btn" value="下载" onclick="exportExcle();"/></td>
	</tr>
</table>     
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
</body>
</html>
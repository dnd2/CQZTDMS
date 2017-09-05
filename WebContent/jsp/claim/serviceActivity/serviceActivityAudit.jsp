<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动管理</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
//日历控件初始化
function doInit()
	{
	   __extQuery__(1);
	}
</script>
</head>

<body >
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动审核查询</div>
    <!-- 查询条件 begin -->
  <form method="post" name ="fm" id="fm">
    <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
    <table class="table_query">
	           <tr>
	              <td style="text-align:right">服务站：</td>
					<td >
      				<input name="DealerCode" type="hidden" id="DealerCode" class="middle_txt" value="" />
      				<input name="DealerName" type="text" id="DealerName"  readonly onclick="showOrgDealer('DealerCode','','false', '', '','','','DealerName');" class="middle_txt" value="" />
                    <!-- <input name="dlbtn" type="button" class="mini_btn" onclick="showOrgDealer('DealerCode','','false', '', '','','10771001','DealerName');" value="..." /> -->
                    <input type="button" class="normal_btn" onclick="txtClr('DealerCode','DealerName');" value="清 空" id="clrBtn" /> 
    				</td>
	              <td style="text-align:right">申请单号： </td>
		            <td >
			          <input name="applyNo" id="applyNo" value="" type="text" class="middle_txt"  />
	              </td>
	            </tr>
            	<tr>
            		<td style="text-align:right">状态：</td>
					<td>
					 	 <script type="text/javascript">
		   					   genSelBoxExp("status",<%=Constant.SERVICEACTIVITYAPPLY_STATUS%>,"",true,"","","false",'<%=Constant.SERVICEACTIVITYAPPLY_STATUS_01%>');
		  			  </script>
					</td>
					<td style="text-align:right">单据新增时间：</td>
					<td>
					<input id="addStart" name="addStart" style="width:80px;"  class="middle_txt" type="text" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'addEnd\')}'})"/>
					<!-- <input name="addStart" type="text" group="dealStart,dealEnd" datatype="1,is_date,10" class="short_time_txt" id="dealStart" /> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'dealStart', false);" />  	 -->
		             &nbsp;至&nbsp;
		             <input id="addEnd"  name="addEnd" style="width:80px;" class="middle_txt" type="text" onclick="WdatePicker({minDate:'#F{$dp.$D(\'addStart\')}'})"/>
		            <!-- <input name="addEnd" type="text" group="dealStart,dealEnd" datatype="1,is_date,10" class="short_time_txt" id="dealEnd" /> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'dealEnd', false);" />  -->
				</td>
				</tr>
                <tr>
				  <td colspan="4" style="text-align:center">
	                <input class="normal_btn" type="button" value="查 询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
	              </td>
		       </tr>
  </table>
  </div>
  </div>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form> 
<!-- 查询条件 end -->
<br/>
<!--页面列表 begin -->
<script type="text/javascript" >

	var myPage;
//查询路径
	var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/serviceActivityAuditQuery.json";
				
	var title = null;

	var columns = [
				{header:'序号', renderer:getIndex,align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'},
	            {header: "申请单号", dataIndex: 'APPLY_NO', align:'center'},
				{header: "服务站代码",dataIndex: 'DEALER_CODE' ,align:'center'},
				{header: "服务站名称",dataIndex: 'DEALER_NAME' ,align:'center'},
				{header: "单据新增时间",dataIndex: 'CRDATE' ,align:'center'},
				{header: "所属区域",dataIndex: 'ORG_NAME' ,align:'center'},
				{header: '状态',dataIndex:'STATUS',align:'center',renderer:getItemValue}
		      ];
	//超链接设置
	function myLink(value,meta,record){
		/* var str1="<input name='detailBtn' type='button' class='middle_btn' onclick='activitySearch(\""+ record.data.ID +"\")' value='查看'/>&nbsp;";
		var str2="<input name='detailBtn' type='button' class='middle_btn' onclick='subcommit(\""+ record.data.ID  +"\")' value='审核'/>&nbsp;"; */
		var str1="<a class=\"u-anchor\" href=\"#\" onclick='activitySearch(\""+ record.data.ID +"\")'>查看</a>";
		var str2="<a class=\"u-anchor\" href=\"#\" onclick='subcommit(\""+ record.data.ID +"\")'>审核</a>";
		var str=str1;
		if(record.data.STATUS == <%=Constant.SERVICEACTIVITYAPPLY_STATUS_02%>){
			str+=str2;
		}
		return String.format(str);
	
	}
	
	//查询
	function activitySearch(str){	
		var url = '<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/serviceActivityApplySearchInit.do?activityId='+str;
		OpenHtmlWindow(url, 800, 500, '服务活动查看');
		<%-- window.location.href='<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/serviceActivityApplySearchInit.do?activityId='+str; --%>
	}
	//审核
	function subcommit(str){
		var url = '<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/serviceActivityApplyAuditInit.do?activityId='+str;
		OpenHtmlWindow(url, 800, 500, '服务活动审核');
		<%-- fm.action = '<%=contextPath%>/claim/serviceActivity/ServiceActivityApply/serviceActivityApplyAuditInit.do?activityId='+str;
		fm.submit(); --%>
	}
	
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
</script>
</div>
<!--页面列表 end -->
</body>
</html>
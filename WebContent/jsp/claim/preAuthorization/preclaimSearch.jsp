<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔预授权审核作业</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
//日历控件初始化
function doInit()
	{
	   loadcalendar();
	   __extQuery__(1);
	}
</script>
</head>

<body>
 <form name="fm" id="fm" >
  <div class="navigation">
	<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔预授权&gt;索赔预授权审核作业
  </div>
 <table class="table_query">
          <tr>
	            <td class="table_query_2Col_label_5Letter">经销商代码：</td>
	            <td align="left">
<%--	                <input class="middle_txt" id="dealerCode" style="cursor: pointer;" name="dealerCode" type="text" />--%>
	                <textarea rows="3" cols="40" id="dealerCode" name="dealerCode"></textarea>
				    <input class="mini_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','','true','',true)" />
				    <input name="button2" type="button" class="normal_btn" onclick="clr();" value="清除"/>
	            </td>
	            
	            <td class="table_query_2Col_label_6Letter">经销商名称：</td>
	            <td align="left">
	                <input class="middle_txt" id="dealerName"  name="dealerName" type="text" datatype="1,is_null,100"/>
	            </td>
          </tr>
          <tr>
	            <td class="table_query_2Col_label_5Letter">预授权单号：</td>
	            <td align="left">
	            	  <input type="text" name="foNo" id="foNo" class="middle_txt"  datatype="1,is_null,20"/>
	            </td>
	            <td class="table_query_2Col_label_6Letter">维修工单号：</td>
	            <td align="left">
	                  <input type="text"  name="roNo" id="roNo"  class="middle_txt" datatype="1,is_null,20"/>
	            </td>
          </tr>
          <tr>
          <td class="table_query_2Col_label_5Letter">申请日期：</td>
	            <td>
		              <div align="left">
	            	   <input name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
	            	   <input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
	            	  </div>
	            </td>
          </tr>
	    <tr>
			    <td align="center" colspan="4">
			  	    <input type="button"  class="normal_btn" name="button1" value="查询" onclick="__extQuery__(1);" />
			    </td>
	    </tr>
 	</table>
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
	var url = "<%=contextPath%>/claim/preAuthorization/PreclaimSearch/preclaimAuditQuery.json";
				
	var title = null;

	var columns = [
				{header: "序号", align:'center',renderer:getIndex},//生成序列号
				{header: "预授权单号",dataIndex: 'FO_NO' ,align:'center'},
				{header: "维修工单号 ",dataIndex: 'RO_NO' ,align:'center'},
				{header: "经销商代码",dataIndex: 'DEALER_CODE' ,align:'center'},
				{header: "经销商简称",dataIndex: 'DEALER_SHORTNAME' ,align:'center'},
				{header: "VIN",dataIndex: 'VIN' ,align:'center'},
				{header: "申请日期",dataIndex: 'APPROVAL_DATE' ,align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:preclaimAuditDetialQueryInit ,align:'center'}
		      ];
	//审核超链接设置
	function preclaimAuditDetialQueryInit(value,meta,record){
	return String.format(
	         "<a href=\"#\" onclick='Complete(\""+record.data.ID+"\")'>[审核]</a>");
	}
	function Complete(value){
	OpenHtmlWindow('<%=contextPath%>/claim/preAuthorization/PreclaimSearch/preclaimAuditDetialQueryInit.do?id='+value,850,500);
	}
//清除方法
 function clr() {
	document.getElementById('dealerCode').value = "";
  }	
</script>
<!--页面列表 end -->
</body>
</html>
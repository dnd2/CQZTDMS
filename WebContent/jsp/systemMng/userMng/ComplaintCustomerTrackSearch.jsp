<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>投诉跟踪查询(经销商端)</title>
<style>
.img {
	border: none
}
</style>
<script language="JavaScript">
    var myPage;
    var url = "<%=contextPath%>/sysmng/usemng/ComplaintCustomerTrack/Query.json";		
	var title = null;
	var columns = [
				{id:'id',header: "客户ID", width:'10%', dataIndex: 'CUS_ID'},
				{header: "客户名称", width:'10%', dataIndex: 'CUSTOMER_NAME'},
				{header: "跟踪日期", width:'10%', dataIndex: 'TRACK_DATE',renderer:formatDate},
				{header: "电话", width:'10%', dataIndex: 'PHONE'},
				{header: "车型", width:'10%', dataIndex: 'GROUP_CODE'},
				{header: "车牌号", width:'10%', dataIndex: 'LICENSE_NO'},
				{header: "受理人", width:'10%', dataIndex: 'DEAL_WITH'},
				{header: "频次", width:'10%', dataIndex: 'TIMES'},
			    {header: "创建日期", width:'10%', dataIndex: 'CREATE_DATE',renderer:formatDate},
			    {header: "操作",width:'10%',dataIndex: 'CUS_ID',renderer:myLink ,align:'center'}			
	      ];
  	//修改的超链接设置
function myLink(value,meta,record){
		 return String.format(
					"<a href=\"#\" onclick='updateCus(\""+value+"\")'>[修改]</a>"
			)	    		
    }	      
function updateCus(value){
     location = '<%=contextPath%>/sysmng/usemng/ComplaintCustomerTrack/modfiInit.do?CUS_ID='+value;
 }	  
function dropCus(value){
	MyConfirm("是否确认删除？",drop,[value]);
}
function drop(value){
	makeNomalFormCall('<%=contextPath%>/sysmng/usemng/ComplaintCustomerTrack/delete.json?CUS_ID='+value,dropCall,'fm','');
}
function dropCall(json) {
	if(json.flag!= null && json.flag== true) {
		MyAlert(" 删除成功！");
		__extQuery__(1);
	} else {
		MyAlert("删除失败！请联系管理员！");
	}
}	            
 function doInit() {
   		loadcalendar();
	}
//格式化时间为YYYY-MM-DD
 function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
function requery() {
	$('customer_Name').value="";
	$('init_Level').value="";
	$('intent_Level').value="";
}	
	
</script>
</head>

<body onload='doInit()'>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理 &gt; 信息反馈提报 &gt; 维修投诉跟踪管理(经销商端)</div>
<form method="post" name = "fm" id="fm">
<input type="hidden" name="curPage" id="curPage" value="1" />
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
<table class="table_query" border="0">
<tr>		
		<td class="table_query_2Col_label_5Letter" nowrap="nowrap">客户名称：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			<input class="middle_txt" type="text" maxlength="30" datatype="1,is_noquotation,30" id="customer_Name" name="customer_Name"/>
		</td>
	</tr>
	<tr>
		 <td class="table_query_2Col_label_5Letter">提报时间：</td>
              <td  nowrap="nowrap">
              <input type="text" name="CON_APPLY_DATE_START" id="CON_APPLY_DATE_START_ID"  datatype="1,is_date,10" group="CON_APPLY_DATE_START_ID,CON_APPLY_DATE_END_ID" hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_START_ID', false);"/>
              至
  			<input type="text" name="CON_APPLY_DATE_END" id="CON_APPLY_DATE_END_ID"  datatype="1,is_date,10" group="CON_APPLY_DATE_START_ID,CON_APPLY_DATE_END_ID" hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_END_ID', false);"/>
  			</td>
	</tr>
	<tr>
		<td align="center" colspan="4">
            <input class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1);" />			
            <input class="normal_btn" type="button" value="重 置" onclick="requery()"/>
			<input class="normal_btn" type="button" value="新 增" onclick="window.location.href='<%=contextPath%>/sysmng/usemng/ComplaintCustomerTrack/addInit.do'" value="新 增" />
		</td>
	</tr>
</table>
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
</body>
</html>

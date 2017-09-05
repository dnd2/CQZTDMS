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
<title>潜在客户管理(经销商端)</title>
<style>
.img {
	border: none
}
</style>
<script language="JavaScript">
    var myPage;
    var url = "<%=contextPath%>/sysmng/usemng/PotentialCustomer/PotentialCustomerQuery.json";		
	var title = null;
	var columns = [
				{id:'id',header: "客户代码", width:'10%', dataIndex: 'CUSTOMER_NO'},
				{header: "客户名称", width:'10%', dataIndex: 'CUSTOMER_NAME'},
				{header: "性别", width:'10%', dataIndex: 'GENDER',renderer:getItemValue},
				{header: "电话", width:'10%', dataIndex: 'CONTACTOR_MOBILE'},
				{header: "初始级别", width:'10%', dataIndex: 'INIT_LEVEL',renderer:getItemValue},
				{header: "意向级别", width:'10%', dataIndex: 'INTENT_LEVEL',renderer:getItemValue},
			    {header: "创建日期", width:'10%', dataIndex: 'CREATE_DATE',renderer:formatDate},
			    {header: "操作",width:'10%',dataIndex: 'CUSTOMER_NO',renderer:myLink ,align:'center'}			
	      ];
  	//修改的超链接设置
function myLink(value,meta,record){
		 return String.format(
					"<a href=\"#\" onclick='cusDetail(\""+value+"\")'>[查看]</a><a href=\"#\" onclick='updateCus(\""+value+"\")'>[修改]</a><a href=\"#\" onclick='dropCus(\""+value+"\")'>[删除]</a>"
			)	    		
    }	      
function updateCus(value){
     location = '<%=contextPath%>/sysmng/usemng/PotentialCustomer/modfiPotentialCustomerInit.do?CUSTOMER_NO='+value;
 }	 
function cusDetail(value){
    location = '<%=contextPath%>/sysmng/usemng/PotentialCustomer/getPotentialCustomerDetail.do?CUSTOMER_NO='+value+"&command=2";
}	
function dropCus(value){
	MyConfirm("是否确认删除？",drop,[value]);
}
function drop(value){
	makeNomalFormCall('<%=contextPath%>/sysmng/usemng/PotentialCustomer/deletePotentialCustomer.json?CUSTOMER_NO='+value,dropCall,'fm','');
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
   	//	__extQuery__(1);
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
	$('CON_APPLY_DATE_START').value="";
	$('CON_APPLY_DATE_END').value="";
}	
	
</script>
</head>

<body onload='doInit()'>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 经销商库存管理 &gt; 经销商端潜在客户管理</div>
<form method="post" name = "fm" id="fm">
<input type="hidden" name="curPage" id="curPage" value="1" />
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
<table class="table_query" border="0">
<tr>		
		<td class="table_query_2Col_label_5Letter" nowrap="nowrap">客户名称：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			<input class="middle_txt" type="text" maxlength="30" datatype="1,is_noquotation,30" id="customer_Name" name="customer_Name"/>
		</td>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">客户初始级别：</td>
		<td class="table_query_2Col_input" >
		 <script type="text/javascript">
	         genSelBoxExp("init_Level",<%=Constant.DICT_INTENT_LEVEL%>,"",true,"short_sel","","false",'');
	     </script>
		</td>
	</tr>
	<tr>			
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">客户意向级别：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			 <script type="text/javascript">
	         genSelBoxExp("intent_Level",<%=Constant.DICT_INTENT_LEVEL%>,"",true,"short_sel","","false",'');
	       </script> 
		</td>
		 <td class="table_query_2Col_label_5Letter">提报时间：</td>
		  <td  nowrap="nowrap">
		 <input name="CON_APPLY_DATE_START" type="text" class="short_time_txt" id="CON_APPLY_DATE_START" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'CON_APPLY_DATE_START', false);" />  	
             &nbsp;至&nbsp;
             <input name="CON_APPLY_DATE_END" type="text" class="short_time_txt" id="CON_APPLY_DATE_END" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'CON_APPLY_DATE_END', false);" /> 
  			</td>
	</tr>
	<tr>
		<td align="center" colspan="4">
            <input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询"  onclick="__extQuery__(1);" />			
            <input class="normal_btn" type="button" value="重 置" onclick="requery()"/>
			<input class="normal_btn" type="button" value="新 增" onclick="window.location.href='<%=contextPath%>/sysmng/usemng/PotentialCustomer/addPotentialCustomerInit.do'" value="新 增" />
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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>潜在客户管理查询(OEM端)</title>
<style>
.img {
	border: none
}
</style>
<script language="JavaScript">
    var myPage;
    var url = null;
	var title = null;
	var columns = null;
	
  function detailQuery(){
	url = "<%=contextPath%>/sysmng/usemng/PotentialCustomer/PotentialCustomerOemQuery.json";		
	columns = [
				{id:'id',header: "客户代码", width:'10%', dataIndex: 'CUSTOMER_NO'},
				{header: "客户名称", width:'10%', dataIndex: 'CUSTOMER_NAME'},
				{header: "性别", width:'10%', dataIndex: 'GENDER',renderer:getItemValue},
				{header: "电话", width:'10%', dataIndex: 'CONTACTOR_MOBILE'},
				{header: "初始级别", width:'10%', dataIndex: 'INIT_LEVEL',renderer:getItemValue},
				{header: "意向级别", width:'10%', dataIndex: 'INTENT_LEVEL',renderer:getItemValue},
				{header: "客户来源", width:'10%', dataIndex: 'CUS_SOURCE',renderer:getItemValue},
				{header: "媒体类型", width:'10%', dataIndex: 'MEDIA_TYPE',renderer:getItemValue},
				{header: "销售顾问", width:'10%', dataIndex: 'SOLD_BY'},
				{header: "所属大区", width:'10%', dataIndex: 'ROOT_ORG_NAME'},
				{header: "经销商", width:'10%', dataIndex: 'DEALER_NAME'},
			    {header: "创建日期", width:'10%', dataIndex: 'CREATE_DATE',renderer:formatDate},
			    {header: "操作", width:'10%',dataIndex: 'CUSTOMER_NO',renderer:mySelect}
	      ];
	     __extQuery__(1); 
	  } 
    function mySelect(value,meta,record){
	  	return String.format("<a href=\"#\" onclick='moreLinkman(\""+value+"\")';>查看</a>");
	}
	function moreLinkman(value){
		OpenHtmlWindow('<%=contextPath%>/sysmng/usemng/PotentialCustomer/getPotentialCustomerDetail.do?CUSTOMER_NO='+value+'&command=1',700,500);
	}	  
  function totalQuery(){
		url = "<%=contextPath%>/sysmng/usemng/PotentialCustomer/PotentialCustomerOemTotalQuery.json";
		columns = [
				{header: "所属大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
				{header: "省份", dataIndex: 'REGION_NAME', align:'center'},
				{header: "经销商", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "展厅活动", dataIndex: 'SALE', align:'center'},
				{header: "报纸杂志", dataIndex: 'FRIEND', align:'center'},
				{header: "促销活动", dataIndex: 'AD', align:'center'},
				{header: "重复购买", dataIndex: 'TV', align:'center'},
				{header: "朋友介绍", dataIndex: 'INTERNET', align:'center'},
				{header: "老客户介绍", dataIndex: 'NEWSPAPER', align:'center'},
				{header: "其他", dataIndex: 'SHOW', align:'center'},
				{header: "陌生拜访", dataIndex: 'RADIO', align:'center'},
				{header: "电台广告", dataIndex: 'OUTDOORS', align:'center'},
				{header: "电视广告", dataIndex: 'DM', align:'center'},
				{header: "路牌广告", dataIndex: 'NOTE', align:'center'},
				{header: "网络广告", dataIndex: 'OTHER', align:'center'},
				{header: "路过", dataIndex: 'AGAIN', align:'center'},
				{header: "H级", dataIndex: 'H', align:'center'},
				{header: "A级", dataIndex: 'A', align:'center'},
				{header: "B级", dataIndex: 'B', align:'center'},
				{header: "C级", dataIndex: 'C', align:'center'},
				{header: "D级", dataIndex: 'D', align:'center'},
				{header: "F级", dataIndex: 'F', align:'center'},
				{header: "F0级", dataIndex: 'F0', align:'center'},
				{header: "N级", dataIndex: 'N', align:'center'},
				{header: "O级", dataIndex: 'O', align:'center'}
		      ];
		__extQuery__(1);
	}           
 function doInit() {
   		loadcalendar();
   		if(document.getElementById("orgCode").value ==""&&document.getElementById("dealerCode").value ==""){
		document.getElementById("button4").disabled=true;
		document.getElementById("button4clearbutton").disabled=true;
			document.getElementById("orgbuclearbutton").disabled=false;
			document.getElementById("orgbu").disabled=false;
		}else if(document.getElementById("orgCode").value !=""){
			document.getElementById("button4").disabled=true;
			document.getElementById("button4clearbutton").disabled=true;
				document.getElementById("orgbuclearbutton").disabled=false;
				document.getElementById("orgbu").disabled=false;
			}else{
			document.getElementById("button4").disabled=false;
			document.getElementById("button4clearbutton").disabled=false;
				document.getElementById("orgbuclearbutton").disabled=true;
				document.getElementById("orgbu").disabled=true;
		}
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
	$('dealerId').value="";
	$('dealerCode').value="";
	$('orgId').value="";
	$('orgCode').value="";
}	

  function checkType(){
		document.getElementById("orgbuclearbutton").disabled=true;
		document.getElementById("orgbu").disabled=true;
		document.getElementById("button4").disabled=false;
		document.getElementById("button4clearbutton").disabled=false;
		document.getElementById("orgCode").value="";
		document.getElementById("orgId").value="";
		}
	function checkOrgType(){
		document.getElementById("orgbuclearbutton").disabled=false;
		document.getElementById("orgbu").disabled=false;
		document.getElementById("button4").disabled=true;
		document.getElementById("button4clearbutton").disabled=true;
		document.getElementById("dealerCode").value="";
		document.getElementById("dealerId").value="";
		}
	function exportExclel(){
	var fm = document.getElementById('fm');
	fm.action='<%=contextPath%>/sysmng/usemng/PotentialCustomer/viewExcel.do';
	fm.submit();
	
}
	function exportCountExclel(){
	var fm = document.getElementById('fm');
	fm.action='<%=contextPath%>/sysmng/usemng/PotentialCustomer/viewCountExcel.do';
	fm.submit();
	
}		

function toClearDealers(){
	document.getElementById("dealerId").value = "";
	document.getElementById("dealerCode").value = "";
	document.getElementById("dealerName").value = "";
}
function toClearOrgs(){
	document.getElementById("orgId").value = "";
	document.getElementById("orgCode").value = "";
}
</script>
</head>

<body onload='doInit()'>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 客户信息管理 &gt; 潜在客户管理(Oem端)</div>
<form method="post" name = "fm" id="fm">
<input type="hidden" name="curPage" id="curPage" value="1" />
<table class="table_query" border="0">
<tr>
       <td align="right"><input type="radio" id="odtype" name="odtype" value="0" onclick="checkOrgType()" checked="checked"></input>选择区域：</td>
		<td class="table_list_th">
		<input type="hidden"  name="orgId" size="15" value=""  id="orgId" class="middle_txt" datatype="1,is_noquotation,75" />
		<input type="text"  readonly="readonly"  name="orgCode" size="15" value=""  id="orgCode" class="middle_txt" datatype="1,is_noquotation,75" />
		<input name="orgbu"  id="orgbu" type="button" class="mark_btn" onclick="showOrg('orgCode','orgId','false')" value="..." />
		<input type="button" name="orgbuclearbutton" id="orgbuclearbutton" class="cssbutton" value="清除" onClick="toClearOrgs();"/>
		</td>
		<td align="right"><input type="radio" id="odtype" name="odtype" value="0" onclick="checkType()"></input>选择经销商：</td>
			<td class="table_list_th"><input type="hidden" name="dealerId" size="15" id="dealerId" value="" />
				<input type="hidden" class="middle_txt" readonly="readonly" name="dealerCode" size="15" id="dealerCode" value="" />
				<input type="text" class="middle_txt" readonly="readonly" name="dealerName" size="15" id="dealerName" value="" />
				<input name="button4" type="button"  class="mini_btn" onclick="showOrgDealer('dealerCode','dealerId','true','','','','','dealerName');" value="..." />
				<input type="button" name="button4clearbutton" id="button4clearbutton" class="cssbutton" value="清除" onClick="toClearDealers();"/>
			</td>
</tr>
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
		 <td class="table_query_2Col_label_6Letter">提报时间(昨天)：</td>
              <td  nowrap="nowrap">
              <input type="text" name="CON_APPLY_DATE_START" id="CON_APPLY_DATE_START_ID" value="${date }"  datatype="0,is_date,10" group="CON_APPLY_DATE_START_ID,CON_APPLY_DATE_END_ID" hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_START_ID', false);"/>
              至
  			<input type="text" name="CON_APPLY_DATE_END" id="CON_APPLY_DATE_END_ID" value="${date }" datatype="0,is_date,10" group="CON_APPLY_DATE_START_ID,CON_APPLY_DATE_END_ID" hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_END_ID', false);"/>
  			</td>
	</tr>
	<tr>
		<td align="center" colspan="4">
            <input class="normal_btn" type="button" name="button1" value="明细查询"  onclick="detailQuery();" id="queryBtn"/>			
            <input class="normal_btn" type="button" value="重 置" onclick="requery()"/>
            <input type="button"  class="normal_btn" value="明细下载" onclick="exportExclel();" />
            <input type="button"  class="normal_btn" value="汇总查询" onclick=" totalQuery();" id="queryBtn" />
            <input type="button"  class="normal_btn" value="汇总下载" onclick="exportCountExclel();"/>
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

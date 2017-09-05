<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>工单选择列表</title>
<% String contextPath = request.getContextPath();%>
<script>
var arrayobj = new Array();
function doInit() {
	CompanyName(document.getElementById("selectType").value)  ;
	__extQuery__(1);
}
</script>
</head>

<body>
<div class="wbox">
<div class="navigation">
<img src="<%=contextPath %>/img/nav.gif" />&nbsp;售后管理>超级用户权限>用户切换
</div>
<form name="fm" id="fm">
<div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
  	<table class="table_query">
  	<tr>	
  	        <td style="text-align:right">选择方式：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
			   <select id="selectType" name="selectType" class="u-select" onchange="CompanyName(this.value)">
			      <option value="1" selected="selected">OEM部门用户</option>
			      <option value="2">经销商公司用户</option>
			   </select>
		    </td>	 		  
		    
			<td style="text-align:right">
		   <div id="divcompany" style="display: none;">公司名称：</div>
		   <div id="divpose" style="display: inline;">职位名称：</div>
		   </td>
		   <td>
		   <div id="divcompanytxt" style="display: none;"><input type="text" name="companyName" class="middle_txt"/></div>
		   <div id="divposetxt" style="display: inline;"><input type="text" name="poseName" class="middle_txt"/></div>
		   </td>
	</tr>
	<tr>	
		<td style="text-align:right">账号：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			<input class="middle_txt" type="text" maxlength="30" datatype="1,is_noquotation,30" id="ACNT" name="ACNT"/>
		</td>
		<td style="text-align:right">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;姓名：</td><td><input class="middle_txt" type="text" maxlength="30" datatype="1,is_noquotation,30" id="NAME" name="NAME"/>
		</td>
	</tr>
	<tr>
    	<td style="text-align:center" colspan="4">
    		<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" />
    		<input class="normal_btn" type="button" value="重 置" onclick="requery()"/>
    		<input type="button" name="return1" onclick="parent.window._hide();"  class="normal_btn" value="关闭"/>
    		<input type="hidden" name="selectType" value="<%=request.getParameter("selectType") %>"/>
    	</td>
    </tr>
    </table>
</div>
</div>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="javascript">
 	var myPage;
	var url = "<%=contextPath%>/sysmng/usemng/UserChangeAction/queryOrder.json";
	var title = null;
	var columns = [
				{header: "序号", dataIndex: 'index', align:'center',renderer:getIndex},
				{header: "选择", dataIndex: '', align:'center',renderer:mySelect},
				{header: "账号", dataIndex: 'acnt', align:'center'},
				{header: "姓名", dataIndex: 'name', align:'center'},
				{header: "职位", dataIndex: 'addr', align:'center'},
				{header: "状态",  sortable: true, dataIndex: 'userStatus', align:'center',orderCol:"USER_STATUS",renderer:getItemValue,width:'8%'}
		      ];
		      
	
function mySelect(value,metaDate,record){
    
    return String.format("<input type='radio' name='rd' onclick='showNo(\""+record.data.userId+"\", \""+record.data.acnt+"\",\""+record.data.password+"\")' />");
}	
function showNo(userId,acnt, password) {
	//parent._hide();
	goTo(userId,acnt, password);
} 

//刷新父页面
function goTo(userId,acnt, password){
	if (parent.$('#inIframe')[0]) {	
	    var url = "<%=contextPath%>/common/UserManager/login.do?userchange=1&userId="+userId+"&userName="+acnt+"&password="+password; 
	    //MyAlert(url);
	    parent.location.href = url;   
	} else {
		parent.location.href="<%=contextPath%>/common/UserManager/login.do";
	}
}
function requery() {
	$('#ACNT')[0].value="";
	$('#NAME')[0].value="";
	$('#poseName')[0].value="";
}
function CompanyName(index){
   if(2 == index){
     $('#divcompany')[0].style.display="";//显示
     $('#divcompanytxt')[0].style.display="";//显示
     $('#divpose')[0].style.display="none";//隐藏
     $('#divposetxt')[0].style.display="none";//隐藏
   }else{
      $('#divcompany')[0].style.display="none";//隐藏
      $('#divcompanytxt')[0].style.display="none";//隐藏
      $('#divpose')[0].style.display="";//显示
      $('#divposetxt')[0].style.display="";//显示
   }  
}
</script>
</div>
</body>

</html>

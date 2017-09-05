<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		var ctmId_Old =parent.$('inIframe').contentWindow.$('ctmId_Old').value;
		if(ctmId_Old==0){
			document.getElementById("queryBtn").disabled =true
		}else{
			document.getElementById("ctmId_Old").value=ctmId_Old;
		}
	}
</script>

<title>实销信息上报</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 实销信息上报&gt; 新增联系人</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
	    		<td align="right">姓名：</td>
	    		<td width="35%" align="left" >
	      		<input id="vehicle_no" name="vehicle_no" type="text" class="middle_txt" value="${salesInfo.vehicleNo }" datatype="1,is_carno,20" maxlength="20" />
	    		</td>
	   			<td width="15%" align="right">联系目的：</td>
	    		<td width="35%" align="left" >
	      		<input id="contract_no" name="contract_no" type="text" class="middle_txt" value="${salesInfo.contractNo }" datatype="1,is_textarea,25" maxlength="25" />
	    		</td>
  			</tr>
  			<tr>
	    		<td align="right">主要联系电话：</td>
	    		<td width="35%" align="left" >
	      		<input id="vehicle_no" name="vehicle_no" type="text" class="middle_txt" value="${salesInfo.vehicleNo }" datatype="1,is_carno,20" maxlength="20" />
	    		</td>
	   			<td width="15%" align="right">其他联系电话：</td>
	    		<td width="35%" align="left" >
	      		<input id="contract_no" name="contract_no" type="text" class="middle_txt" value="${salesInfo.contractNo }" datatype="1,is_textarea,25" maxlength="25" />
	    		</td>
  			</tr>
		</table>
		<table class=table_query>
		    <tr align="center">
			    <td>
				    <input type="button" value="保 存" class="normal_btn" onclick="saveReport();" />
			    </td>
		    </tr>
		</table>
</form>
	
</div>
<script type="text/javascript">

	function toAddLinkMan(){
		window.location.href = "<%=contextPath%>/sales/customerInfoManage/SalesReport/toAddLinkMan.do";
	}
</script>    
</body>
</html>
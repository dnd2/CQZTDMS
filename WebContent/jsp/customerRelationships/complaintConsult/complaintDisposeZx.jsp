	<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%
	String contextPath = request.getContextPath();
	String cpId=request.getParameter("cpId");
	String flag=request.getParameter("flag");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>咨询查询</title>
<script type="text/javascript">
</script>
</head>
<body onload="doInit();">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系 &gt; 投诉咨询管理 &gt;投诉咨询处理</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<input type="hidden" id="cpId" name="cpId" value="<%=cpId%>">
		<input type="hidden" id="id" name="id" value="<%=flag%>">
	    <tr >
				<td align="right" nowrap="true">沟通记录：</td>
				<td align="left"  colspan="4">
					<textarea id="ccont" name="ccont" rows="5" style="width: 95%"></textarea>
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td colspan="8" align="center">
					<input name="button"  id="setQ" type="button" class="normal_btn" onclick="complaintClose();"  value="确 定" />
					&nbsp;
					<input class="normal_btn" type="reset" value="重 置" />
					&nbsp;
					<input class="normal_btn" type="button" onclick="_hide();" value="关 闭" /></td>
        		</td>
			</tr>
		</table>
	
	</form>
<script type="text/javascript">
	
	function complaintDealerSet(){
    	if(check()){
    		var cpId=	$("cpId").value;
			makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptZx/complaintDealerSetInit.json?cpId='+cpId+'&CuRa='+CuRa,setBack,'fm','');
				}
    	}
	function setBack(json){
		if(json.success != null && json.success=='true'){
			MyAlert("分派成功!");
		}else{
			MyAlert("分派失败,请联系管理员!");
		}
	}
	
	function check(){ 
		if(""==document.getElementById('ccont').value){
			MyAlert("沟通记录不能为空!");
			return false;
		}
		return true;
	}
	
	function complaintClose(){
    	if(check()){
    		var cpId=$("cpId").value;
    		var flag=$("flag").value;
    		if(flag=="0"){
    			makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptZx/complaintDealerCloseInit.json?cpId='+cpId,setBack,'fm','');
    		}else{
    			makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptZx/complaintDealerSetInit.json?cpId='+cpId,setBack,'fm','');
    			}
			}
    	}
	function setBack(json){
		if(json.success != null && json.success=='true'){
			MyAlert("操作成功!");
		}else{
			MyAlert("操作失败,请联系管理员!");
		}
	}
</script>
</body>
</html>
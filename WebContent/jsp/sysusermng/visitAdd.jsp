<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%String contextPath = request.getContextPath();%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<TITLE>访问地址</TITLE>
<SCRIPT LANGUAGE="JavaScript">
	function clearInput(){
		$('dealerId').value='';
		$('dealerCode').value='';
	}
	function telecomAddress(){
		var dealerId= $('dealerId').value;
		url="http://222.177.8.19/tms/dealer/dealerMain.action?DEALER_ID="+dealerId;
		window.open(url);
		
	}
	function netcomAddress(){
		var dealerId= $('dealerId').value;
		url="http://58.17.185.174/tms/dealer/dealerMain.action?DEALER_ID="+dealerId;
		window.open(url);
	}
</script>
</HEAD>
<BODY>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：个人信息管理&gt;个人信息管理&gt;访问地址</div>
  <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
           <tr>  
	        <td align="right" class="table_query_2Col_label_7Letter" id="deId">选择经销商：</td>
	 		<td align="left">
				<input class="middle_txt" id="dealerCode"  name="dealerCode" type="text"/>
				<input class="middle_txt" id="dealerId"  name="dealerId" type="hidden"/>
	            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onClick="showOrgDealerZW('dealerCode','dealerId','false','',true);" value="..." />        
	            <input name="clrBtn" type="button" class="normal_btn" onClick="clearInput();" value="清除"/>  
			</td>
          </tr>
    	  <tr>
            <td colspan="2" align="center">
            <input id="queryBtn" class="normal_btn" type="button" name="button" value="电信地址" onclick="telecomAddress();"/>
            <input id="queryBtn" class="normal_btn" type="button" name="button" value="网通地址" onclick="netcomAddress();"/>
			</td>
            </tr>
  </table>
</form>
</BODY>
</html>
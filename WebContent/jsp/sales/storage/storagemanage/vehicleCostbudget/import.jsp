<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<TITLE>计划查询</TITLE>
<% String contextPath = request.getContextPath();  %>
<SCRIPT type=text/javascript>

	function dataImport(){
		document.getElementById("impBtn").disabled=true;  
		document.getElementById('fm').action= "<%=request.getContextPath()%>/sales/storage/storagemanage/VehicleCostbudget/vehicleInvoiceTmpImport.do";
		document.getElementById('fm').submit();
	}
	
</script>
</HEAD>
<BODY>
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：订单管理 &gt;提车单管理 &gt;财务开票导入</div>
  <form method="post" name="fm" id="fm"   enctype="multipart/form-data">
  <table class="table_query" bordercolor="#DAE0EE">
    <tr>
      <th width="100" align="left" colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 查询条件</th>
    </tr>
    <tr>
      <td><font color="red">
文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
        <input type="file" id="uploadFile" name="uploadFile"/>
      <input type="button" id="impBtn" class="normal_btn" value="确定" onclick="dataImport();"/>
      &nbsp;
      <input class="normal_btn" type="button" value="关闭" name="button3" onclick="_hide()"/></td>
    </tr>
  </table>
  </form>
</div>
</BODY>
</html>
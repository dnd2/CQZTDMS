<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/jquery-1.7.2.js"></script>
<title>各大区库存统计报表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;整车销售报表&gt;各大区库存统计</div>
  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">
  		 <tr>
				<td width="20%" class="tblopt"><div align="right">产地：</div></td>
		      	<td align="left"><select id="areaId" name="areaId" class="short_sel">
		      			<option value="">-请选择-</option>
					<c:forEach items="${areaList}" var="po">
						<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
					</c:forEach>
				</select>
				</td>
  		  </tr> 	
          <tr>
	            <td align="center" colspan="2">
	            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="query_();"/>&nbsp;
	            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="下载"  onclick="exportExcel()" />
	            </td>
          </tr>
         
  </table>
  <!-- 查询条件 end -->
 <div id="myDiv"></div>
</form>

<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
function query_(){
  		var fm =  document.getElementById("fm");
  		fm.target="_blank";
		fm.action = "<%=contextPath%>/report/reportOne/OrgStorageDetailReport/getOrgStorageDetailInfo.do";
		fm.submit();
}

function exportExcel(){
	var fm =  document.getElementById("fm");
		fm.action = "<%=contextPath%>/report/reportOne/OrgStorageDetailReport/toExcel.json";
		fm.submit();
}
</script>
<!--页面列表 end -->

</body>
</html>
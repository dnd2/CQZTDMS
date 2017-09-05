<%-- 
创建时间 : 2010.08.27
             创建人:lishuai
             功能描述：结算室审核，可对索赔单进行批量审核，和逐条审核
--%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>北汽幻速开票</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;北汽幻速开票
</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
	<td align="center">
	转账日期 :
		 <input class="short_txt" id="startDate" name="startDate" readonly="readonly"  value="${END_DATE }"  datatype="1,is_date,10"
                 maxlength="10" group="startDate,endDate"/>
              至
          <input class="short_txt" id="endDate" name="endDate" datatype="0,is_date,10"
                 maxlength="10" group="startDate,endDate"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'endDate', false);" type="button"/>
           </td>
	</tr>
	<tr>
   
    
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="确定" onclick="addDealerUnitKp()"/>
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
	<script type="text/javascript">
	function  addDealerUnitKp()
	{
		  if(!submitForm('fm'))
		 {
			return false;
		 }
	 	fm.action = '<%=contextPath%>/claim/application/DealerNewKp/addDealerUnitKpPo.do';
		fm.submit();
	}
	
	</script>
</body>
</html>
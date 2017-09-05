<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
	Long userId = (Long)request.getAttribute("logonUser");
	
%>

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>首页新闻查询</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<form name='fm' id='fm' method="post">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;售后服务活动&gt;里程限制</div>
  <table>
  <tr>
  		<td>开始里程</td>
  		<td>结束里程</td>
  </tr>
  <tr>
  		<td><input type="text" datatype="0,is_double" decimal="2" maxlength="8" id="MILAGE_START" name ="MILAGE_START" value="${MILAGE_START}"/> </td>
  		<td><input type="text" datatype="0,is_double" decimal="2" maxlength="8" id="MILAGE_END" name ="MILAGE_END" value="${MILAGE_END}"/> </td>
  </tr>
  <tr>
  <td colspan="1" align="right"><input type="button" align="right" value="确定" onClick="add();"/> </td>
  <td colspan="1"><input type="hidden"  id="activeId" name ="activeId" value="${activeId}"/> </td>
  </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<script type="text/javascript" >
	function add(){
	      var MILAGE_START= document.getElementById('MILAGE_START').value;
	      var MILAGE_END = document.getElementById('MILAGE_END').value;
	      if(!submitForm('fm')) {
				return false;
			}
		  if('${type}' != '1')
		  {
		  	 if(parseInt(MILAGE_START) >= parseInt(MILAGE_END))
		      {
		      	document.getElementById('MILAGE_START').value = "开始里程不能大于结束里程";
		      }else
		      {
		      	$('fm').action = "<%=request.getContextPath()%>/claim/serviceActivity/ServiceActivityManage/insertActivityMilage.do"
			    $('fm').submit();
			    _hide();
		      }
		  }else
		  {
		  	 if(parseInt(MILAGE_START) > parseInt('${MILAGE_START}')  || parseInt(MILAGE_END) < parseInt('${MILAGE_END}') )
		      {
		      	document.getElementById('MILAGE_START').value = "提报后的里程必须在提报前里程外！";
		      }else
		      {
		      	$('fm').action = "<%=request.getContextPath()%>/claim/serviceActivity/ServiceActivityManage/insertActivityMilage.do"
			    $('fm').submit();
			    _hide();
		      }
		  }	
			
	     
		
	}
</script>
  <table align="center">
  <tr align="center">
  <td>
  </td>
  </tr>
  </table>
</form>
</body>
</html>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">

</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>pin申请查询查询</title>
</head>
<body>
<div class="wbox">
	<div class="navigation"><font style="font-size: 13px;font-family:serif;"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 车辆信息管理管理&gt; PIN码查看页面</font></div>
	<form id="fm" name="fm" method="post">
	<div class="form-panel">
<h2>基本信息</h2>
			<div class="form-body">
	<table class="table_query">
		<tr >			
				<td style="text-align:right">单据编码：
				</td>
				<td style="text-align:left">
					${sepin.PIN_NO }
				</td>
				<td style="text-align:right">制单人：
				</td>
				<td style="text-align:left">
					${sepin.NAME }
				</td>
				<td style="text-align:right">制单日期：
				</td>
				<td style="text-align:left">
					${sepin.CREATE_DATE }
				</td>
			 </tr>	 
			  <tr >		
			  <td style="text-align:right">维修站编码：
				</td>
				<td style="text-align:left">
					${sepin.DEALER_CODE }
				</td>	
				<td style="text-align:right">维修站名称：</td>
  				<td style="text-align:left">
  					${sepin.DEALER_NAME }
  				</td> 				
				<td style="text-align:right">VIN码：</td>
				<td style="text-align:left">
					${sepin.VIN }
				</td>				
			 </tr>	 
			 <tr >
			  	<td style="text-align:right">
			  		备注：
				</td>
				<td style="text-align:left">
			  		${sepin.REMARK }
				</td>
			 </tr>
			 <tr >
			  	<td style="text-align:right">
			  		车辆PIN码：
				</td>
				<td style="text-align:left">
			  		${sepin.PIN_CODE }
				</td>
			 </tr>
			 <tr >
			  	<td style="text-align:right">
			  		回复备注：
				</td>
				<td style="text-align:left">
			  		${sepin.REPLY }
				</td>
			 </tr>
			 <tr >
				 <td colspan="6" style="text-align:center">
					<input class="normal_btn" type="button" value="关闭" onclick="_hide();"/>&nbsp;		
			   	 </td> 
			</tr>
	</table>
	</div>
	</div>
</form>
</div>
</body>
</html>
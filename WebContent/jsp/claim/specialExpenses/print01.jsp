<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>特殊费用申请单</title>
</head>
<%
	
%>
<body>
<br/>
<center><strong><font size="6">特殊费用申请单</font></strong></center>
<br><br><br>
<div align="center">
<table width="700px" border="1" cellSpacing= "0" cellPadding= "0" class="tabp">
	<tr>
		<td align="left" height="30" class="tdp" nowrap="nowrap" width="">&nbsp;特殊费用申请单号：</td>
		<td align="left" height="30" class="tdp" nowrap="nowrap" width="">&nbsp;
			<c:out value="${map.FEE_NO}"/>
		</td>
		<td align="left" class="tdp" nowrap="nowrap" width="">&nbsp;经销商代码：</td>
		<td align="left" class="tdp" nowrap="nowrap" width="">&nbsp;
			<c:out value="${map.DEALER_CODE}"/>
		</td>
		<td align="left" class="tdp" nowrap="nowrap" width="">&nbsp;经销商名称：</td>
		<td align="left" class="tdp" width="">&nbsp;
			<c:out value="${map.DEALER_SHORTNAME}"/>
		</td>	
	</tr>
	<tr>
		<td align="left" height="30" class="tdp" nowrap="nowrap" width="">&nbsp;制单日期：</td>
		<td align="left" height="30" class="tdp" nowrap="nowrap" width="">&nbsp;
			<fmt:formatDate value="${map.CREATE_DATE}" pattern="yyyy-MM-dd"/>
		</td>
		<td align="left" class="tdp" nowrap="nowrap" width="">&nbsp;结算厂家：</td>
		<td align="left" class="tdp" nowrap="nowrap" width="">&nbsp;
			<script type="text/javascript">
				writeItemValue(<c:out value="${map.YIELD}"/>)
			</script>
		</td>
		<td align="left" class="tdp" nowrap="nowrap" width="">&nbsp;申报金额：</td>
		<td align="left" class="tdp" width="">&nbsp;
			<c:out value="${map.DECLARE_SUM1}"/>
		</td>	
	</tr>
	<tr>
		<td align="left" height="30" class="tdp" nowrap="nowrap" width="">&nbsp;联系人：</td>
		<td align="left" height="30" class="tdp" nowrap="nowrap" width="">&nbsp;
			<c:out value="${map.LINKMAN}"/>
		</td>
		<td align="left" class="tdp" nowrap="nowrap" width="">&nbsp;联系电话：</td>
		<td align="left" class="tdp" nowrap="nowrap" width="" >&nbsp;
			<c:out value="${map.LINKMAN_TEL}"/>
		</td>
		<td align="left" class="tdp" nowrap="nowrap" width="">&nbsp;</td>
		<td align="left" class="tdp" nowrap="nowrap" width="" >&nbsp;
			
		</td>
	</tr>
	<tr>
		<td align="left" class="tdp" nowrap="nowrap" width="">&nbsp;费用类型：</td>
		<td align="left" class="tdp" width="">&nbsp;
			<script type="text/javascript">
				writeItemValue(<c:out value="${map.FEE_TYPE}"/>)
			</script>
		</td>
			<td align="left" height="30" class="tdp" nowrap="nowrap" width="">&nbsp;费用类别：</td>
			<td align="left" height="30" class="tdp" nowrap="nowrap" width="">&nbsp;
				<script type="text/javascript">
					writeItemValue(<c:out value="${map.FEE_CHANNEL}"/>)
				</script>
			</td>
			<td align="left" class="tdp" nowrap="nowrap" width="">&nbsp;活动项目：</td>
			<td align="left" class="tdp" nowrap="nowrap" width="">&nbsp;
				${map.ACTIVITY_NAME }
			</td>
	</tr>
	<tr>
		<td align="left" class="tdp" height="30" nowrap="nowrap" width="">&nbsp;VIN：</td>
		<td align="left" class="tdp" height="30" nowrap="nowrap" width="">&nbsp;
			<c:out value="${map.VIN}"/>
		</td>
		<td align="left" class="tdp" nowrap="nowrap" width="">&nbsp;车型：</td>
		<td align="left" class="tdp" nowrap="nowrap" width="">&nbsp;
			<c:out value="${map.V_MODEL}"/>
		</td>
		<td align="left" class="tdp" nowrap="nowrap" width="">&nbsp;</td>
		<td align="left" class="tdp" nowrap="nowrap" width="">&nbsp;</td>	
	</tr>
	<tr>
		<td align="left" class="tdp" nowrap="nowrap" height="80" width="" height="80" >&nbsp;备注：</td>
		<td align="left" class="tdp" width="" colspan="5">&nbsp;
			<c:out value="${map.APPLY_CONTENT}"/>
		</td>
	</tr>
</table>

<br><br><br><br><br>
<table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" >   
	<tr>    
		<td width="100%" height="25" colspan="3">   
		<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
			<div id="kpr">    
				<input class="ipt" type="button" value="打印" onclick="kpr.style.display='none';javascript:printit();">    
				<input class="ipt" type="button" value="打印页面设置" onclick="javascript:printsetup();">    
				<input class="ipt" type="button" value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();">    
			</div>
		</td>
	</tr>     
</table> 
</div>
<script language="javascript">    
  
	function printsetup()
	{       
		wb.execwb(8,1);    // 打印页面设置 
	}    
	function printpreview()
	{    
		wb.execwb(7,1);   // 打印页面预览       
	}      
	function printit()    
	{    
		if(confirm('确定打印吗？'))
		{    
			wb.execwb(6,6)    
		}    
	}    
</script> 
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>特殊外出费用申请单</title>
</head>
<%
	
%>
<body>
<br/>
<center><strong><font size="6">特殊外出费用申请单</font></strong></center>
<br><br><br>
<div align="center">
<table width="700px" border="1" cellSpacing= "0" cellPadding= "0" class="tabp">
	<tr>
		<td align="left" colspan="1" height="30" class="tdp" nowrap="nowrap" >&nbsp;特殊费用申请单号：</td>
		<td align="left" colspan="1" height="30" class="tdp" >&nbsp;
			<c:out value="${map.FEE_NO}"/>
		</td>
		<td align="left" colspan="1" height="30" class="tdp" nowrap="nowrap" >&nbsp;经销商代码：</td>
		<td align="left" colspan="1" height="30" class="tdp" >&nbsp;
			<c:out value="${map.DEALER_CODE}"/>
		</td>
		<td align="left" colspan="1" class="tdp" nowrap="nowrap" >&nbsp;经销商名称：</td>
		<td align="left" colspan="1" class="tdp" >&nbsp;
			<c:out value="${map.DEALER_SHORTNAME}"/>
		</td>	
	</tr>
	<tr>
		<td align="left" colspan="1" height="30" class="tdp" nowrap="nowrap" >&nbsp;制单人：</td>
		<td align="left" colspan="1" height="30" class="tdp" >&nbsp;
			<c:out value="${map.NAME}"/>
		</td>
		<td align="left" colspan="1" class="tdp" nowrap="nowrap" >&nbsp;制单日期：</td>
		<td align="left" colspan="1" class="tdp" >&nbsp;
			<fmt:formatDate value="${map.CREATE_DATE}" pattern="yyyy-MM-dd"/>
		</td>
		<td align="left" colspan="1" class="tdp" nowrap="nowrap" >&nbsp;生产厂家：</td>
		<td align="left" colspan="1" class="tdp" >&nbsp;
			<script type="text/javascript">
				writeItemValue(<c:out value="${map.YIELD}"/>);
			</script>
		</td>	
	</tr>
	<tr>
		<td align="left" colspan="1" height="30" class="tdp" nowrap="nowrap" >&nbsp;外出开始时间：</td>
		<td align="left" colspan="1" height="30" class="tdp" >&nbsp;
			${map.STARTDATE}
		</td>
		<td align="left" colspan="1" class="tdp" nowrap="nowrap" >&nbsp;外出结束时间：</td>
		<td align="left" colspan="1" class="tdp" >&nbsp;
			<c:out value="${map.ENDDATE}"/>
		</td>
		<td align="left" colspan="1" class="tdp" nowrap="nowrap" >&nbsp;出差地：</td>
		<td align="left" colspan="1" class="tdp" >&nbsp;
			<c:out value="${map.PURPOSE_ADDRESS}"/>
		</td>	
	</tr>
	<tr>
		<td align="left" colspan="1" class="tdp" height="30" nowrap="nowrap" >&nbsp;住宿费(元)：</td>
		<td align="left" colspan="1" class="tdp" height="30" >&nbsp;
			<c:out value="${map.QUARTER_FEE}"/>
		</td>
		<td align="left" colspan="1" class="tdp" nowrap="nowrap" >&nbsp;餐补费(元)：</td>
		<td align="left" colspan="1" class="tdp" >&nbsp;
			<c:out value="${map.EAT_FEE}"/>
		</td>
		<td align="left" colspan="1" class="tdp" nowrap="nowrap" >&nbsp;补助工时费(元)：</td>
		<td align="left" colspan="1" class="tdp" >&nbsp;
			<c:out value="${map.PERSON_SUBSIDE}"/>
		</td>	
	</tr>
	<tr>
		<td align="left" class="tdp" height="30" nowrap="nowrap" >&nbsp;费用类型：</td>
		<td align="left" class="tdp" height="30" >&nbsp;
			<script type="text/javascript">writeItemValue(${map.FEE_TYPE})</script>
		</td>
		<td align="left" class="tdp" height="30" nowrap="nowrap" >&nbsp;费用渠道：</td>
		<td align="left" class="tdp" height="30" >&nbsp;
			<script type="text/javascript">writeItemValue(${map.FEE_CHANNEL})</script>
		</td>
		<td align="left" class="tdp" height="30" nowrap="nowrap" >&nbsp;总申报费用(元)：</td>
		<td align="left" class="tdp" height="30" >&nbsp;
			<c:out value="${map.DECLARE_SUM1}"/>
		</td>
	</tr>
	<tr>
		<td align="left" class="tdp" height="30" nowrap="nowrap">&nbsp;总里程：</td>
		<td align="left" class="tdp" height="30">&nbsp;${map.SINGLE_MILEAGE}</td>
	</tr>
	<tr>
		<td align="left" class="tdp" colspan="1" nowrap="nowrap" height="80"  height="80" >&nbsp;备注：</td>
		<td align="left" class="tdp"  colspan="5">&nbsp;
			<c:out value="${map.APPLY_CONTENT}"/>
		</td>
	</tr>
	<tr>
		<td align="left" class="tdp"  height="10" colspan="6">&nbsp;</td>
	</tr>
	<tr>
		<td align="left" colspan="1" class="tdp" height="30" nowrap="nowrap" >&nbsp;审批时间</td>
		<td align="left" colspan="1" class="tdp" nowrap="nowrap" >&nbsp;审批人</td>
		<td align="left" colspan="1" class="tdp" nowrap="nowrap" >&nbsp;审批部门</td>
		<td align="left" colspan="1" class="tdp" nowrap="nowrap" >&nbsp;审批状态</td>
		<td align="left" class="tdp" nowrap="nowrap" colspan="2">&nbsp;审批意见</td>
	</tr>
	<c:if test="${list!=null}">
		<c:forEach items="${list}" var="l">
			<tr>
				<td align="left" colspan="1" class="tdp" height="30" nowrap="nowrap" >&nbsp;${l.AUDITING_DATE}</td>
				<td align="left" colspan="1" class="tdp" nowrap="nowrap" >&nbsp;<c:out value="${l.AUDITING_PERSON}"/></td>
				<td align="left" colspan="1" class="tdp" nowrap="nowrap" >&nbsp;<c:out value="${l.ORG_NAME}"/></td>
				<td align="left" colspan="1" class="tdp" nowrap="nowrap" >&nbsp;
					<script type='text/javascript'>
	     			  	writeItemValue(<c:out value="${l.STATUS}"/>);
					</script>
				</td>
				<td align="left" class="tdp" nowrap="nowrap" colspan="2">&nbsp;<c:out value="${al.AUDITING_OPINION}"/></td>
			</tr>
		</c:forEach>
	</c:if>
	<tr>
		<td align="left" class="tdp" height="10" colspan="6">&nbsp;</td>
	</tr>
	<tr>
	
	<table  width="700px" border="1" cellSpacing= "0" cellPadding= "0" class="tabp">
	<tr>
		<td height="30" align="left" class="tdp" nowrap="nowrap" >&nbsp;行号</td>
		<td height="30" align="left" class="tdp" nowrap="nowrap" >&nbsp;工单单号</td>
		<td height="30" align="left" class="tdp" nowrap="nowrap" >&nbsp;车型</td>
		<td height="30" align="left" class="tdp" nowrap="nowrap" >&nbsp;VIN</td>
		<td height="30" align="left" class="tdp" nowrap="nowrap" >&nbsp;发动机号</td>
		<!-- <td height="30" align="left" class="tdp" nowrap="nowrap" >&nbsp;生产日期</td> -->
		<td height="30" align="left" class="tdp" nowrap="nowrap" >&nbsp;维修日期</td>
		<td class="tdp" nowrap="nowrap">&nbsp;厂家</td>
		<td height="30" align="left" class="tdp" colspan="">&nbsp;备注</td>
	</tr>
	<c:set var="pageSize"  value="10" />
	<c:if test="${claim!=null}">
		<c:forEach items="${claim}" var="c" varStatus="status">
			<!-- 分页打印 -->
			<tr style='${(status.count%pageSize==0) ? "page-break-after:always;":""}'>
				<td height="30" align="left" class="tdp" nowrap="nowrap" >&nbsp;${status.index+1}</td>
				<td height="30" align="left" class="tdp" nowrap="nowrap" >&nbsp;<c:out value="${c.CLAIM_NO}"/></td>
				<td height="30" align="left" class="tdp" nowrap="nowrap" >&nbsp;<c:out value="${c.SERIES}"/></td>
				<td height="30" align="left" class="tdp" nowrap="nowrap" >&nbsp;<c:out value="${c.VIN}"/></td>
				<td height="30" align="left" class="tdp" nowrap="nowrap" >&nbsp;<c:out value="${c.ENGINE_NO}"/></td>
				<!-- <td height="30" align="left" class="tdp" nowrap="nowrap" >&nbsp;<c:out value="${c.PRODUCT_DATE}"/></td> -->
				<td height="30" align="left" class="tdp" nowrap="nowrap" >&nbsp;<fmt:formatDate value="${c.CLAIM_DATE}" pattern="yyyy-MM-dd"/></td>
				<td class="tdp" nowrap="nowrap">
					<script>
						writeItemValue(${c.YIELDLY});
					</script>
				</td>
				<td height="30" width="140px" align="left" class="tdp" colspan="5">&nbsp;
					${c.REMARK}
				</td>
			</tr>
		</c:forEach>
	</c:if>
	</table>
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

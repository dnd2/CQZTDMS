<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>无费用(无旧件)申明明细</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;经销商信息变更&gt;无费用(无旧件)申明明细
<form name="fm" id="fm">
		<c:if test="${poValue.STATUS==status }">
			<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<tr>
			<td align="right">变更类型：</td>
			<td>
				<script type="text/javascript">
					writeItemValue(${poValue.STATUS });
				</script>
			</td>
			<td align="right">基地：</td>
			<td>
				<script type="text/javascript">
					writeItemValue(${poValue.YIELDLY });
				</script>
			</td>
		</tr>
		<tr>
			<td align="right">变更前旧件提交至：</td>
			<td>
				<fmt:formatDate value="${poValue.HOST_DATE }" pattern="yyyy-MM-dd"/>
			</td>
			<td align="right">变更前旧件审核至：</td>
			<td>
				<fmt:formatDate value="${poValue.HOST_REVIEW }" pattern="yyyy-MM-dd"/>
			</td>
		</tr>
		<tr>
			<td align="right">无旧件申明日期起：</td>
			<td>
				<fmt:formatDate value="${poValue.CHANGE_DATE }" pattern="yyyy-MM-dd"/>
			</td>
			<td align="right">无旧件申明日期止：</td>
			<td>
				<fmt:formatDate value="${poValue.CHANGE_REVIEW_DATE }" pattern="yyyy-MM-dd"/>
			</td>
		</tr>
		<tr>
			<td align="right">变更时间：</td>
			<td>
				<fmt:formatDate value="${poValue.CHANGE_TIME }" pattern="yyyy-MM-dd"/>
			</td>
			<td align="right"></td>
			<td></td>
		</tr>
	</table>
		</c:if>
		<c:if test="${poValue.STATUS!=status }">
			<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<tr>
			<td align="right">变更类型：</td>
			<td>
				<script type="text/javascript">
					writeItemValue(${poValue.STATUS });
				</script>
			</td>
			<td align="right">基地：</td>
			<td>
				<script type="text/javascript">
					writeItemValue(${poValue.YIELDLY });
				</script>
			</td>
		</tr>
		<tr>
			<td align="right">变更前结算单提交至：</td>
			<td>
				<fmt:formatDate value="${poValue.HOST_DATE }" pattern="yyyy-MM-dd"/>
			</td>
			<td align="right">变更前结算单审核至：</td>
			<td>
				<fmt:formatDate value="${poValue.HOST_REVIEW }" pattern="yyyy-MM-dd"/>
			</td>
		</tr>
		<tr>
			<td align="right">无费用申明日期起：</td>
			<td>
				<fmt:formatDate value="${poValue.CHANGE_DATE }" pattern="yyyy-MM-dd"/>
			</td>
			<td align="right">无费用申明日期止：</td>
			<td>
				<fmt:formatDate value="${poValue.CHANGE_REVIEW_DATE }" pattern="yyyy-MM-dd"/>
			</td>
		</tr>
		<tr>
			<td align="right">变更时间：</td>
			<td>
				<fmt:formatDate value="${poValue.CHANGE_TIME }" pattern="yyyy-MM-dd"/>
			</td>
			<td align="right"></td>
			<td></td>
		</tr>
	</table>
		</c:if>
		<table width=100% border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="12" align=left width="33%">&nbsp;</td>
             	<td height="12" align=center width="33%">
					<input type="button" onClick="javascript:history.go(-1);" class="normal_btn"  style="width=8%" value="返回"/>
    			</td>
            	<td height="12" align=center width="33%">
      			</td>
			</tr>
		</table>
	</form>
</body>
</html>

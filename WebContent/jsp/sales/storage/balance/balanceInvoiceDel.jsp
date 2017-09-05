<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %> 
<%
	String contextPath = request.getContextPath();
%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发票附件显示</title>
</head>
<body>
	<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;当前位置：储运管理&gt;结算管理&gt;结算申请&gt;发票附件显示</div>
	<form method="POST" name="fm" id="fm">
	<div class="form-panel">
		<h2>发票附件显示</h2>
		<div class="form-body">
	  <TABLE class=table_query id="moneyTable">
	  	<tr>
	  		<td class="right">结算单号：</td>
			<td><span id="orderNo_span"><c:out value="${map.APPLY_NO}" default="0"></c:out></span></td>
			<td class="right">发票号：</td>
			<td><span id="orderNo_span"><c:out value="${map.INVOICE_NO}" default="0"></c:out></span></td>
		</tr>
	  </TABLE>
	  </div>
	 </div>
	 <TABLE class=table_query style="margin-top: 2px;">
	    <TBODY>
	      <tr>
	        <TH colSpan=6 noWrap align=left><IMG class=nav src="<%=request.getContextPath()%>/img/subNav.gif">附件明细 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
	      </TR>
	    </TBODY>
	  </TABLE>
   		<TABLE class=table_list style="border-bottom:1px solid #DAE0EE" >  
		    <tr class=cssTable >
		      <th width="20%">图片名称</th>
		      <th width="80%">显示</th>
		    </tr>
    		<tbody id="tbody1">
    			<c:forEach items="${flist}" var="list" varStatus="status">
    				<tr>
    					<td><a href="javascript:void(0);" onclick="viewPic('${list.FILE_PATH}');">${list.FILE_NAME}</a></td>
    					<c:if test="${status.index==0}">
    						<td rowspan="${fsize}"><div id="showPic"></div></td>
    					</c:if>
    				</tr>
    			</c:forEach>
    		</tbody>
  		</table>
	 
</form>
<script type="text/javascript">
//查看图片   
function viewPic(path){
	//MyAlert(path);
	document.getElementById("showPic").innerHTML="<img src='"+path+"' height='300px'/>";
}
</script>
</body>
</html>

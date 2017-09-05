<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ taglib uri="/jstl/cout" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户服务资料审批表明细</title>
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理&gt;信息反馈提报&gt;服务资料明细</div>
 <form method="post" name = "fm" id="fm" >
   <table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
     <tr>
       <th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> 基本信息</th>
     </tr>
     <tr bgcolor="F3F4F8">
       <td width="14%" align="right">工单号：</td>
       <td width="21%"><c:out value="${ps.ORDER_ID}"/></td>
       <td width="13%" align="right">经销商代码：</td>
       <td width="19%"><c:out value="${ps.DEALER_CODE}"/></td>
       <td width="12%" align="right">经销商名称：</td>
       <td width="21%"><c:out value="${ps.DEALER_NAME}"/></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">经销商联系人：</td>
       <td><c:out value="${ps.LINK_MAN}"/></td>
       <td align="right">经销商电话：</td>
       <td><c:out value="${ps.TEL}"/></td>
       <td height="27"  align="right" bgcolor="FFFFFF">经销商传真：</td>
       <td bgcolor="FFFFFF"><c:out value="${ps.FAX}"/></td>
     </tr>
     <tr bgcolor="FFFFFF">
       <td height="27"  align="right">邮寄方式：</td>
       <td><script type='text/javascript'>
	       var name=getItemValue('<c:out value="${ps.MAIL_TYPE}"/>');
	       document.write(name);
       </script>     
       </td>
       <td height="27"  align="right">邮寄地址：</td>
       <td colspan="3"><c:out value="${ps.MAIL_ADDRESS}"/></td>
     </tr>
     <tr bgcolor="FFFFFF">
       <td height="27"  align="right">邮政编码：</td>
       <td><c:out value="${ps.ZIP_CODE}"/></td>
       <td height="27" align="right">申请内容：</td>
       <td height="27" colspan="5" align="left"><c:out value="${ps.SE_CONTENT}"/></td>
     </tr>
   </table>
   <table  class="table_list" style="border-bottom:1px solid #DAE0EE">
     <tr><th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />资料明细</th></tr>
	 <tr>
	        <th align="center">名称</th>
            <th align="center">数量</th>
            <th align="center">单价</th>
            <th align="center">总价</th>
            <th align="center">审核后总价</th>
            <th align="center">说明</th>
     </tr>	     
     <c:forEach items="${detailList}" var="dl">
       		<tr>
            	<td><c:out value="${dl.DATA_NAME}"/></td>
            	<td><c:out value="${dl.AMOUNT}"/></td>
            	<td><c:out value="${dl.PRICE}"/></td>
	            <td><c:out value="${dl.SUMPRICE}"/></td>	
           	 	<td><c:out value="${dl.AUDIT_SUM}"/></td>
           	 	<td><c:out value="${dl.REMARK}"/></td>
        	</tr>
    </c:forEach>
  </table>
  <TABLE class="table_list" style="border-bottom:1px solid #DAE0EE">
		   <th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 审批明细</th>
           <tr  bgcolor="F3F4F8">
            <th > 审批时间</th>
            <th > 审批人员</th>
            <th > 人员部门</th>
            <th > 审批状态</th>
            <th > 审批意见</th>
           </tr>
           <c:forEach items="${auditList}" var="al">
       		<tr>
            	<td><c:out value="${al.AUDIT_DATE}"/></td>
            	<td><c:out value="${al.NAME}"/></td>
            	<td><c:out value="${al.ORG_NAME}"/></td>
	            <td><c:out value="${al.AUDIT_STATUS}"/></td>	
           	 	<td><c:out value="${al.AUDIT_CONTENT}"/></td>
        	</tr>
    	   </c:forEach>
   </table> 
    <table class="table_list">
       <tr > 
         <th height="12" align=center>
          <input type="button" onClick="_hide();" class="normal_btn" style="width=8%" value="关闭"/>
           &nbsp;&nbsp;
       </tr>
     </table>    
</form>
<script type="text/javascript">
	function closeWindow(){
		_hide();
	}
</script>
</body>
</html>
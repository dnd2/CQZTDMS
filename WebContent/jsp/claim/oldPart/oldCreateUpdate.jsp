<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<head>
<% 
   String contextPath = request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<title>旧件修改</title>
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;旧件修改</div>
 <form method="post" name ="fm" id="fm">
       <table id="itemTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<th colspan="17" >
					<h3>修改页面</h3>
				</th>
				</tr>
				<tr align="center" class="table_list_row1">
					<td width="20%">
						类型:
					</td>
					<td width="20%">
						代码:
					</td>
					<td width="20%">
						名称:
					</td>
					
				</tr>
				<tr align="center" class="table_list_row1">
					<td width="20%">
						<input type="hidden"" name="id" value="${po.id}">
						<c:if test="${po.nameType==1}">
							<input type="text" class="middle_txt"readonly="readonly" name="po.nameType" value="库区">
						</c:if>
						<c:if test="${po.nameType==2}">
							<input type="text" class="middle_txt"readonly="readonly" name="po.nameType" value="货架">
						</c:if>
						<c:if test="${po.nameType==3}">
							<input type="text" class="middle_txt"readonly="readonly" name="po.nameType" value="层数">
						</c:if>
					</td>
					<td width="20%">
						<input type="text"  class="middle_txt"readonly="readonly" name="po.codeOld" value="${po.codeOld}">
					</td>
					<td width="20%">
						<input type="text" class="middle_txt" name="nameOld" value="${po.nameOld}">
					</td>
					
				</tr>
				<tbody id="transportTable">
				</tbody>
			</table>
     <table class="table_list">
      <tr > 
       <td height="12" align="center">
       	  <input type="button"  id="commit_btn" class="normal_btn" style="width=8%"  value="修改"/>
          <input type="button"  onclick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
       </td>
      </tr>
    </table>  
</form>
<br />
<script type="text/javascript">
	$("#commit_btn").live("click",function(){
			fm.action='<%=contextPath%>/claim/oldPart/ClaimOldPartTransportManager/oldTypeaddUpdateCommit.do';
			fm.method="post";
			fm.submit();
	});
	
</script>
</body>
</html>

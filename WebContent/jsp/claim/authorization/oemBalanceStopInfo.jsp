<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.*"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%String contextPath = request.getContextPath();
List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("lists");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>       
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>暂停结算</TITLE>
</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;暂停汇总参数设置维护</div>
<form method="post" name ="fm" id="fm">
<input type="hidden" name="ID" id="ID" value="${ID }"/>
	<table  class="table_query">
		<tr class="table_list_th">
			    <th  >行号</th>
			    <th  >审核人</th>
			    <th  >审核时间</th>
			    <th  >状态</th>
			    <th  >备注</th>
		</tr>
		<c:forEach var="dList" items="${detail}" varStatus="status">
		<tr class="table_list_row${status.index%2+1}">
	    		<td >${status.index+1 }</td>
			    <td  >${dList.STOP_BY}</td>
			    <td  >${dList.STOP_DATE}</td>
			    <td  >${dList.STATUS}</td>
			    <td  width="30px%">${dList.REMARK} </td>
	 	</tr>
  		</c:forEach>
	</table>
	<br />
	<table  class="table_query">
		<tr>
			<td>备注：</td>
			<td align="left"><textarea name="remake" cols="100" rows="10" ></textarea></td>
		</tr>
	</table>
	<br />
	<table class="table_info" border="0" id="file">
			<input type="hidden" name="fjids"/>
		<tr colspan="8">
			<th>
				<img class="nav" src="../../../img/subNav.gif" />&nbsp;附件列表：
					&nbsp;&nbsp;&nbsp;&nbsp;
				<span align="left"><input type="button" class="normal_btn"  onclick="showUpload2('<%=contextPath%>')" value ='添加附件'/></span>
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2">
				<jsp:include page="${contextPath}/uploadDiv.jsp" />
			</td>
		</tr>
		<%for(int i=0;i<attachLs.size();i++) { %>
    			<script type="text/javascript">
    			addUploadRowByDb('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
    			</script>
    	<%} %>
	</table>
	<br />
	<table class="table_edit">
		<tr>
			<td colspan="4" align="center">
				<input type="button" value="确定" id="_btn1" class="normal_btn" onclick="getInvoice();"/>
				&nbsp;
				<input type="button" value="返回" class="normal_btn" onclick="goBack();"/>
			</td>
		</tr>
  </table>
</form>
<script type="text/javascript">
function goBack(){
	location = '<%=contextPath%>/claim/authorization/BalanceMain/oemBalanceQueryInitStop.do';
}
function getInvoice(){
	$('_btn1').disabled="disabled";
	var id = $('ID').value ;
	var url = '<%=contextPath%>/claim/authorization/BalanceMain/getInvoice.json?id='+id;
	makeNomalFormCall(url,getCallback,'fm');
}
function getCallback(json){
	if(json.flag){
		MyAlert('暂停成功!');
		goBack();
	}
}
</script>
</BODY>
</html>
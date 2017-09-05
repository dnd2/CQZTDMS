<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib uri="/jstl/cout" prefix="c" %>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%
	String contextPath = request.getContextPath();
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
%>
<%@page import="com.infodms.dms.common.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>结算单管理</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<title>开票单位变更申请</title>
<%String checkId=request.getParameter("checkId"); %>
<%String id=request.getParameter("ID"); %>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;三包档案抽查管理&gt;添加附件
</div>
		<form method="post" name="fm" id="fm">
	<input type="hidden" id="ID" name="ID" value="${ID }" />
	<input type="hidden" id="checkId" name="checkId" value="${checkId }" />
	<input type="hidden" id="ok" name="ok" value="${ok }" />
			<!-- 添加附件 -->
			<table class="table_info" border="0" id="file">
				<input type="hidden" id="fjids" name="fjids" />
				<tr colspan="8">
					<th>
						<img class="nav" src="../../../img/subNav.gif" />&nbsp;附件列表：
					</th>
					<th>
						<span align="left"><input type="button" class="normal_btn" onclick="showUpload('<%=contextPath%>')" value='添加附件' /> </span>
					</th>
				</tr>
				<tr>
					<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
				</tr>
				<%for(int i=0;i<attachLs.size();i++) { %>
		  			<script type="text/javascript">
		  				addUploadRowByDb('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
		  			</script>
	  			<%} %>
			</table>
				<table border="0" cellspacing="0" cellpadding="0" class="table_edit">
				<tr>
					<td><span style="color:red">附件必须传三张以上</span></td>
				</tr>
				<tr>
					<td align="center">
						<input type="button" onClick="confirmAdd();" class="normal_btn" id="butADD" value="确定" />
						<input type="button" onClick="goBack();" class="normal_btn" value="返回" />
					</td>
				</tr>
			</table>
		</form>	
<script type="text/javascript">
function goBack(){
	var checkId=document.getElementById("checkId").value;
	fm.action="<%=contextPath%>/claim/application/DealerNewKp/upfileDetail.do?id="+checkId;
	fm.submit();
}
function confirmAdd(){
	if(document.getElementsByName('fjid').length<3){
		MyAlert("附件張數必須大於3張!");
		return false;
	}
	makeNomalFormCall("<%=contextPath%>/claim/application/DealerNewKp/saveUpFile.json",showForwordValue,'fm','queryBtn');
}
function showForwordValue(json){
	var checkId=json.checkId;
	if(json.success=='true'){
		MyAlert('上传成功!');
		fm.action="<%=contextPath%>/claim/application/DealerNewKp/upfileDetail.do?id="+checkId;
		fm.submit();
	}else{
		MyAlert("上传失败!");
	}
}
</script>
</body>
</html>
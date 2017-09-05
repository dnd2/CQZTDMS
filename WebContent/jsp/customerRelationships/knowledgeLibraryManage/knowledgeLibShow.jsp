<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.*"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript">
function doInit()
{
   loadcalendar();
}
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>知识库查询</title>
<% String contextPath = request.getContextPath(); %>
<% List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("lists");%>
</head>
<body>
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
		客户关系管理&gt;知识库管理&gt;知识库更新
	</div>
	<form method="post" name="fm" id="fm">
		<div>
			<table border="0" class="table_edit">
				<tbody>
					<input type="hidden" name="KG_ID" value='<c:out value="${map.KG_ID}"/>' />
					<tr bgcolor="#ffffff">
						<td><table class="table_edit">
								<tbody>
									<tr>
										<td width="20%" align="right" bgcolor="#ffffff">资料标题：</td>
										<td width="30%" bgcolor="#ffffff"><c:out
												value="${map.KG_TOPIC}" /></td>
										<td width="20%" bgcolor="#ffffff"><div align="right">类型：</div>
										</td>
										<td width="30%" bgcolor="#ffffff"><c:out
												value="${map.KG_TYPE}" /></td>
									</tr>
									<tr>
										<td bgcolor="#ffffff" align="right">签发时间：</td>
										<td bgcolor="#ffffff"><c:out value="${map.KG_SIGN_TIME}" />
										</td>
										<td bgcolor="#ffffff">&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									<tr>
										<td bgcolor="#ffffff" align="right">内容：</td>
										<td colspan="3"><c:out value="${map.KG_MEMO}" />
										</td>
									</tr>
									<tr>
										<td bgcolor="#ffffff" align="right">附件：</td>
										<td bgcolor="#ffffff"><jsp:include
												page="${contextPath}/uploadDiv.jsp" /> <%for(int i=0;i<attachLs.size();i++) { %>
											<script type="text/javascript">
    			showUploadRowByDb('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
    			
					</script>
					<%} %>
					</td>
					<td bgcolor="#ffffff">&nbsp;</td>
					<td>&nbsp;</td>
					</tr>
				</tbody>
			</table>
			</td>
			</tr>
			</tbody>
			</table>
			<TABLE align=center width="100%" class=csstable>
				<TR class="tblopt">
					<TD width="100%" class="tblopt"><div align="center">
							<input name="button" type="button" class="normal_btn"
								onclick="closeKnowledgeLibShow();" value="返回" />
						</div></TD>
				</TR>
			</TABLE>
		</div>
	</form>
	<script type="text/javascript">
function closeKnowledgeLibShow(){
	fm.action = "<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/knowledgelibrarySearch.do";
			fm.submit();
		}
	</script>
</body>
</html>

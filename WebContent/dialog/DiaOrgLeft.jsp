<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<link href="<%=request.getContextPath()%>/style/dtree_default.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dtree1.js"></script>
</head>
	
<body onunload='javascript:destoryPrototype()' >	
	<div style=" margin:2px;">
			<script type="text/javascript">
				var rs = ${orgs}.orgs;
				d = new dTree('d', "<%=contextPath%>");
				d.add(0,-1,'经销商选择');
				
				for(var i=0;i<rs.length;i++){
					d.add(rs[i].id, rs[i].pid != rs[i].id ? rs[i].pid : 0, rs[i].name, "javascript:show(" + rs[i].id + ",'" + rs[i].name + "');");
				}
				
				document.write(d);
				d.closeAll();
				d.o(1);
		
			</script>
			<SCRIPT LANGUAGE="JavaScript">
		   	function show(id,name){
		   		parent.frames["mainFrame"].$("orgId").value = id;
		   		//parent.frames["mainFrame"].$("orgTxt").value = name;
		   		parent.frames["mainFrame"].__extQuery__(1);
			}
		   </SCRIPT>
	 </div>
</body>
</html>
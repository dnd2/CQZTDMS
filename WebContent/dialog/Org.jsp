<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head.jsp" />
	<link href="<%=request.getContextPath()%>/style/dtree_default.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dtree1.js"></script>
</head>
	
<body onunload='javascript:destoryPrototype()' >	
	<form id='fm' name='fm'>
		<input type="hidden" id="orgId" name="orgId" value="" />
		<input type="hidden" id="orgName" name="orgName" value="" />
	</form>
	<div style=" margin:2px;">
			<script type="text/javascript">
				var rs = ${orgs};
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
		   		var url ="<%=contextPath%>/common/OrgMng/queryOrgChild.json";
		   		$("orgId").value = id;
		   		$("orgName").value = name;
		   		makeFormCall(url,showResult,'fm');
		   		
			}
			function showResult(json){
				if(json.ACTION_RESULT == '1'){
					parent.parentDocument.getElementById('orgId').value = document.getElementById("orgId").value;
					parent.parentDocument.getElementById('orgName').value = document.getElementById("orgName").value;
			   		parent.parent.removeDiv();
				}	
			}
		   </SCRIPT>
	 </div>
</body>
</html>
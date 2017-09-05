<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="com.infodms.dms.common.Constant"%>
    <%@page import="java.util.List"%>
    <%@page import="java.util.Map"%>
    <%@page import="com.infodms.dms.po.TtCrmComplaintRulePO"%>
<%
	String contextPath = request.getContextPath();
	List<Map<String,Object>> map = (List<Map<String,Object>>)request.getAttribute("ps");
//	List<Map<String,Object>> levelList = (List<Map<String,Object>>)request.getAttribute("level");
//	List<Map<String,Object>> itemList = (List<Map<String,Object>>)request.getAttribute("item");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>回访考核规则设定</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<body onload="">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
          当前位置：员工绩效管理&gt;坐席绩效考核设定
	</div>
	<form method="post" name="fm" id="fm">
		<table class="table_query" border="1">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />回访考核规则信息</th>
       <tr align="center">
  			<td width="20%">考核项目</td>
  			<td width="20%">考核指标</td>
  			<td width="20%">权重</td>
  		</tr>
  		<%if(map!=null&&map.size()>0){ %>
  		<%	for(int i=0;i<map.size();i++){ %>
  		<tr align="center">
  			<td><%=map.get(i).get("ITEM_DESC") %><input type="hidden" value="<%=map.get(i).get("ITEM_ID") %>" name="assessItem"/></td>
  			<td><span>&gt;=</span>
  			<input type="text" style="WIDTH: 80px" id="target"  name="target"   value="<%=map.get(i).get("RU_TARGET")==null?"":map.get(i).get("RU_TARGET") %>" />
  			<span>%</span></td>
  			<td><input type="text" name="weight" id="weight"  value="<%=map.get(i).get("RU_WEIGHT")==null?"":map.get(i).get("RU_WEIGHT") %>" style="WIDTH: 80px" />%</td>
  		</tr>
  		<%	} %>
  		<%} %>
  		
  	<tr><td align="center" colspan="4"><input type="button" value="保存" onclick="save();" class="normal_btn"/></td></tr>
 	</table>
	</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<script type="text/javascript">
		
		   function execute(){
			   fm.action = "<%=contextPath%>/customerRelationships/performancemanage/PerformanceManage/reviewAssessRuleSet.do";
			   fm.submit();
		   }
		   function save(){
			   var targets = document.getElementsByName("target");
			   var weights = document.getElementsByName("weight");
			   var trex = /^(0|[1-9]\d{0,1})(\.\d{1,2})?$/;
			   for(var i=0;i<targets.length;i++){
				   if(!trex.test(targets[i].value)){
						MyAlert("指标["+targets[i].value+"]输入有误,请重新输入！");
						targets[i].focus();
						return false;
				   }
				   if(!trex.test(weights[i].value)){
						MyAlert("权重["+weights[i].value+"]输入有误,请重新输入！");
						weights[i].focus();
						return false;
				   }
				}
			   makeNomalFormCall("<%=contextPath%>/customerRelationships/performancemanage/PerformanceManage/saveReviewRule.json",showReulst,'fm');
		   }
		   function showReulst(json){
			   if(json.success!=null&&json.success=="true"){
				   MyAlert("保存成功！");
			   }else{
				   MyAlert("保存失败，请联系管理员！");
			   }
		   }
	 </script>
</body>
</html>
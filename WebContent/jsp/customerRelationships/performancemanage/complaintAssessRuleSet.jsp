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
	List<Map<String,Object>> levelList = (List<Map<String,Object>>)request.getAttribute("level");
	List<Map<String,Object>> itemList = (List<Map<String,Object>>)request.getAttribute("item");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>投诉考核规则设定</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<body onload="">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
          当前位置：员工绩效管理&gt;服务经理KPI考核设定
	</div>
	<form method="post" name="fm" id="fm">
		<table class="table_query" border="1px">
  		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />投诉考核规则信息</th>
  	
  		<tr align="center">
  			<td width="10%">考核等级</td>
  			<td width="20%">考核项目</td>
  			<td width="30%">考核指标</td>
  			<td width="20%"> 指标分值</td>
  		</tr>
  		<%if(map!=null && map.size()>0){ %>
  		<%	for(int i=0;i<map.size();i++){ %>
  		<tr align="center">
  			<td><%=map.get(i).get("CULEVEL") %><input type="hidden" name="assessLevel" value="<%=map.get(i).get("CODE_ID_2") %>"/> </td>
  			<td><%=map.get(i).get("ITEM_NAME") %><input type="hidden" name="assessItem" value="<%=map.get(i).get("CODE_ID_1") %>"/>  </td>
  			<td>
  			<input type="text" style="WIDTH: 80px" id="mintarget"  name="mintarget"   value="<%=map.get(i).get("MIN_CU_TARGET") %>" />
			<span> % &lt;=</span>
			<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; X &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>
			<span> &lt;</span>
			<input type="text" style="WIDTH: 80px" id="maxtarget"  name="maxtarget"   value="<%=map.get(i).get("MAX_CU_TARGET") %>" /> %
  			</td>
  			<td><input type="text" name="weight" id="weight" value="<%=map.get(i).get("CU_WEIGHT") %>" style="WIDTH: 80px" datatype="0,is_digit,2"/></td>
  		</tr>
  		<%		} %>
  		<%}else{ %>
  			<%
  			  if(itemList!=null && levelList!=null){ 
  				  for(int i=0;i<levelList.size();i++){
  					  for(int j=0;j<itemList.size();j++){
  			%>
  					<tr align="center">
  						<td>
	  						<%=levelList.get(i).get("CODE_DESC") %>
	  						<input type="hidden" value="<%=levelList.get(i).get("CODE_ID") %>" id="assessLevel" name="assessLevel"/>
	  					</td>
  						<td>
  							<%=itemList.get(j).get("CODE_DESC") %>
	  						<input type="hidden" value="<%=itemList.get(j).get("CODE_ID") %>" id="assessItem" name="assessItem"/>
  						</td>
  						
  						<td>
  						<input type="text" style="WIDTH: 80px" id="mintarget"  name="mintarget"   value="" />
  						<span> % &lt;=</span>
			  			
			  			<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; X &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>
			  			<span> &lt;</span>
			  			<input type="text" style="WIDTH: 80px" id="maxtarget"  name="maxtarget"   value="" /> %
			  			</td>
			  			<td><input type="text" name="weight" id="weight" value="" style="WIDTH: 80px" datatype="0,is_digit,4"/></td>
  					</tr>
  					<%} %>
  				<%} %>
  			<%} %>
  	<%} %>
       <tr><td align="center" colspan="4"><input type="button" value="保存" onclick="save();" class="normal_btn"/></td></tr>
 	</table>
	</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<script type="text/javascript">
		   function excute(){
			   fm.action="<%=contextPath%>/customerRelationships/performancemanage/PerformanceManage/complaintAssessRuleSet.do";
			   fm.submit();
		   }
		   function save(){
			   var mintarget = document.getElementsByName("mintarget");
			   var maxtarget = document.getElementsByName("maxtarget");
			   var weights = document.getElementsByName("weight");
			   var trex = /^(0|100|101|[1-9]\d{0,1})(\.\d{1,2})?$/;
			   for(var i=0;i<mintarget.length;i++){
				   if(!trex.test(mintarget[i].value)){
						MyAlert("指标["+mintarget[i].value+"]输入有误,请重新输入！");
						mintarget[i].focus();
						return false;
				   }
				   
				   if(!trex.test(maxtarget[i].value)){
						MyAlert("指标["+maxtarget[i].value+"]输入有误,请重新输入！");
						maxtarget[i].focus();
						return false;
				   }
				   if(!trex.test(weights[i].value)){
						MyAlert("权重["+weights[i].value+"]输入有误,请重新输入！");
						weights[i].focus();
						return false;
				   }
				}
				
				if(Number(mintarget[0].value) > Number(maxtarget[0].value)){
					MyAlert("考核指标A级大小值书写有误！");
					return false;
				}
				if(Number(mintarget[1].value) > Number(maxtarget[1].value)){
					MyAlert("考核指标B级大小值书写有误！");
					return false;
				}
				if(Number(mintarget[2].value) > Number(maxtarget[2].value)){
					MyAlert("考核指标C级大小值书写有误！");
					return false;
				}
				
				if(Number(mintarget[3].value) > Number(maxtarget[3].value)){
					MyAlert("考核指标D级大小值书写有误！");
					return false;
				}
				if(Number(mintarget[0].value) < Number(maxtarget[1].value)){
					MyAlert("考核指标A级的最小值必须大于等于B级的最大值！");
					return false;
				}
				if(Number(mintarget[1].value) < Number(maxtarget[2].value)){
					MyAlert("考核指标B级的最小值必须大于等于C级的最大值！");
					return false;
				}
				if(Number(mintarget[2].value) < Number(maxtarget[3].value)){
					MyAlert("考核指标C级的最小值必须大于等于D级的最大值！");
					return false;
				}

				if(Number(weights[0].value)<=Number(weights[1].value)){
					MyAlert("A级及时关闭率权重必须大于B级及时关闭率权重！");
					return false;
				}
			    if(Number(weights[1].value)<=Number(weights[2].value)){
					MyAlert("B级及时关闭率权重必须大于C级及时关闭率权重！");
					return false;
				}
			    if(Number(weights[2].value)<=Number(weights[3].value)){
					MyAlert("C级及时关闭率权重必须大于D级及时关闭率权重！");
					return false;
				}
			   makeNomalFormCall("<%=contextPath%>/customerRelationships/performancemanage/PerformanceManage/saveComplaintRule.json",showReulst,'fm');
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
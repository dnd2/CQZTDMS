<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	List sumList = (List)request.getAttribute("sumList");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>库存查询</title>
</head>
<body onunload='javascript:destoryPrototype();' > 
    <%
    	if(null != sumList && sumList.size()>0){
    %>
	<table class="table_list" width="85%" align="center" border="0"  id="table1" >	
		<tr align="center" class="tabletitle" >
			<th align="center">车系</th>
			<th align="center">车型</th>
			<th align="center">配置</th>
			<th align="center">物料</th>
			<th align="center">在途数量</th>
			<th align="center">在库数量</th>
			<th align="center">合计</th>
	  	</tr>
	  	
	  	<c:forEach items="${sumList}" var="sumList" varStatus="vstatus">
		  	<tr class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
		  		<td>${sumList.SERIES_NAME }</td>
	  			<td>${sumList.MODEL_NAME }</td>
	  			<td>${sumList.PACKAGE_NAME }</td>
	  			<td>${sumList.MATERIAL_NAME }</td>
	  			<td width="8%">${sumList.ON_WAY }</td>
	  			<td width="8%">${sumList.NO_WAY }</td>
	  			<td width="8%">${sumList.SUM_NO }</td>
	  		</tr>	
	  	</c:forEach>
	</table>
   <%
    	}else{
   %>
   <div class='pageTips'>没有满足条件的数据</div>
   <%
    	}
   %>
<script type="text/javascript">

	function sum_no(){
		//行
		var rowNum = document.getElementById("table1").rows.length;
		//列 
		var colNum = document.getElementById("table1").rows[0].cells.length
	
		//MyAlert("行："+rowNum+"  列："+colNum);
		//MyAlert(document.getElementById('table1').rows[1].cells[4].innerText);
	
		var r=0;
		for(var j=4;j<colNum ;j++){
			for(var i=1;i<rowNum-1;i++){
				r = r+ parseFloat(document.getElementById('table1').rows[i].cells[j].innerHTML);
			}
			document.getElementById(j).innerHTML=r;
			r=0;
		}	
	 }	
	    
</script>    
</body>
</html>
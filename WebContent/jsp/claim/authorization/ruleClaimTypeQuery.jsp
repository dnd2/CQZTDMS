<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
	String rowNum = (String)request.getAttribute("ROWNUM");//行号
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔授权规则</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body onbeforeunload="returnBefore();">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;授权规则维护</div>
  <form name='fm' id='fm'>
  <input id="CODE_ID" name="CODE_ID" type="hidden" />
  <input id="CODE_DESC" name="CODE_DESC" type="hidden" />
  <table class="table_list" id="myTable2" style="border-bottom:1px solid #DAE0EE" >
	  <th><input type="checkbox" name="checkAll" onclick="selectAll(this,'businesscodeIds')"/>全选</th>
      <th>索赔类型代码</th>
      <th>索赔类型名称</th>
       <c:forEach var="addlist" items="${ADDLIST}">
       <tr class="table_list_row1">
       	  <td>
			<input type="checkbox" id="${addlist.CODE_ID}" name="businesscodeIds" value="${addlist.CODE_ID}"/>
       	  </td>
          <td>
          <c:out value="${addlist.CODE_ID}"></c:out>
          </td>
          <td>
          <c:out value="${addlist.CODE_DESC}"></c:out>
          </td>
        </tr>
    </c:forEach>    
</table>
<table class="table_query">
    <tr>
     <td  align="center">
		<input class="normal_btn" type="button" name="button1" value="确认"  onclick="claimTypeConfirm();"/>     
		<input class="normal_btn" type="button" name="button1" value="关闭"  onclick="_hide();"/>
     </td>
    </tr>       
</table> 
</form>
<script type="text/javascript" >
function returnBefore()
{   
	var ggg = 'ELEMENT_VALUE_'+<%=rowNum%>; //索赔类型的codedesc
	var codeids = 'ELEMENT_CODE_'+<%=rowNum%>;//索赔类型的codeid
	var code = document.getElementById("CODE_DESC").value;
	var id = document.getElementById("CODE_ID").value;
	if(code && code.length > 0){
		parentDocument.getElementById(ggg).value = document.getElementById("CODE_DESC").value;
	}
	if(id && id.length > 0){
		parentDocument.getElementById(codeids).value = document.getElementById("CODE_ID").value;
	}	
}
/**
*索赔类型的选择确认方法：
*/
function claimTypeConfirm()
{
	var tab = document.getElementById("myTable2");
	if(!tab)
	{
		_hide();
		closePan();
	}
	var codes = "";
	var ids = "";
	if(tab.rows.length >1)
	{
		for(var i=1; i < tab.rows.length; i++)
		{
			var checkObj = tab.rows[i].cells[0].firstChild;
			if(checkObj.checked ==  true)
			{
				var code = tab.rows[i].cells[2].innerText;
				if(codes)
					codes += "," + code;
			    else
			        codes = code;
			    var id = tab.rows[i].cells[1].innerText;
			    if(ids.length > 0)
			        ids += "," + id;
			    else
			        ids = id;
			      
			}
		}
	}
	if(codes && codes.length > 0)
	   $('CODE_DESC').value = codes;
	if(ids && ids.length > 0)
	   $('CODE_ID').value = ids;
	_hide();
	closePan();
}
</script>  
</body>
</html>


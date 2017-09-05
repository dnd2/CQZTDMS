<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时维护-新增故障</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔工时维护</div>
<form name='fm' id='fm'>
<input type="hidden" name="W_ID" id="W_ID" value="<%=request.getAttribute("W_ID")%>"/>
  <table class="table_list" style="border-bottom:1px solid #DAE0EE">
      <th><input type="checkbox" name="checkAll" onclick="selectAll(this,'businesscodeIds')"/>全选</th>
      <th>故障代码</th>      
      <th>故障名称</th>         
     <c:forEach var="buslist" items="${BUSICODELIST}">
       <tr class="table_list_row1">
       	  <td>
       	  	<input type="checkbox" id="${buslist.BUSINESS_CODE_ID}" name="businesscodeIds" value="${buslist.BUSINESS_CODE_ID}"/>
       	  </td>
          <td>
          <c:out value="${buslist.CODE}"></c:out>
          </td>
          <td>
          <c:out value="${buslist.CODE_NAME}"></c:out>
          </td>
        </tr>
    </c:forEach>
</table>

<table class="table_edit" >
 <tr>
 	<td colspan="2" align="center">
 		<input class="normal_btn" type="button" name="ok" value="添加" onclick="subChecked();"/> 
 		<input class="normal_btn" name="back" type="button" onclick="JavaScript:history.back();" value="返回"/>
 	</td>
 </tr>
</table> 
</form>
<script type="text/javascript">
function subChecked() {
	var str="";
	var chk = document.getElementsByName("businesscodeIds");
	var l = chk.length;
	var cnt = 0;
	for(var i=0;i<l;i++){        
		if(chk[i].checked)
		{            
			str = chk[i].value+","+str; 
			cnt++;
		}
	}
	if(cnt==0){
        MyAlert("请选择！");
        return;
    }else{
    	MyConfirm("是否确认新增？",add,[str]);
    }
}
//添加
function add(str){
	fm.action = '<%=contextPath%>/claim/basicData/ClaimLaborMain/businessCodeAdd.do?businesscodeIds='+str;
	fm.submit();
}
</script>
</body>
</html>
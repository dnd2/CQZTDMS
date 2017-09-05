<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrPartsAssemblyPO"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
TtAsWrPartsAssemblyPO tawep = (TtAsWrPartsAssemblyPO) request.getAttribute("lit");

%>
<%@taglib uri="/jstl/cout" prefix="c" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>
<title>总成维护</title>
</head>
<body>
<div class="wbox">
<form name='fm' id='fm' method="post">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基本数据&gt;总成维护</div>
   <div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
   <table  class="table_query">
       
          <tr>
          	<td style="text-align:right">总成代码：</td>
          	<td>
          		<input name = "ID" type="hidden" value="<%=CommonUtils.checkNull(tawep.getPartsAssemblyId()) %>" />
          		<input  name="parts_accembly_code" type="text" id="parts_accembly_code" value="<%=CommonUtils.checkNull(tawep.getPartsAssemblyCode()) %>" datatype="0,is_null,20"  class="middle_txt" />
          	</td>
          </tr>        
          <tr>
          <td style="text-align:right">总成名称：</td>
          	<td><input name="parts_accembly_name" type="text" id="parts_accembly_name" value="<%=CommonUtils.checkNull(tawep.getPartsAssemblyName()) %>"  datatype="0,is_null,100"  class="middle_txt"/></td>
          </tr>   
          <tr>
          	<td style="text-align:right">状态：</td>
            <td>
				 <script type="text/javascript">
	              genSelBoxExp("TYPE_CODE",<%=Constant.STATUS%>,"<%=CommonUtils.checkNull(tawep.getStatus()) %>",true,"","","true",'');
	            </script>
            </td>  
          </tr> 
		   <tr>    
      	<td colspan="2" style="text-align:center">
            
            <input class="normal_btn" type="button" value="保存" name="add" onclick="save();"/>
            <input type="button"  onclick="_hide() ;" class="normal_btn" id="backId" style="" value="返回" />
           
            
			
           </td>
           </tr>
       
  </table>
</div>
</div>
</form>  
<script type="text/javascript">
function save(){	

	
		var url="<%=request.getContextPath()%>/claim/basicData/PartsAssembly/savePartsAssembly.json";
		makeNomalFormCall(url,showResult22,'fm');
	

		
}
function showResult22(json){
	if(json.flag='true'){
		MyAlert("修改成功");
		
		__parent().__extQuery__(1);//xiongchuan添加,返回列表也同时刷新页面
		_hide();
	}else{
		MyAlert("修改失败");
	}
}
</script>
</div>
</body>
</html>
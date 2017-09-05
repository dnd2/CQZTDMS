<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔附加工时明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔自动结算天数维护</div>
<form id="fm" name="fm">
<center>
  <table class="table_list" width="50%">
       <tr class="table_list_row1">
          <td> 
			自动结算天数
          </td>
          <td>
          <input type="text" name = "type" datatype="0,is_digit,2" id="type" value= "${tc.codeDesc}" />
          <input type="hidden" name = "code_id"  id="code_id" value= "${tc.codeId}" />
          </td>
        </tr>
         <tr class="table_list_row1">
          <td> 
			索赔单上报天数
          </td>
          <td>
          <input type="text" name = "type2" datatype="0,is_digit,2" id="type2" value= "${tc2.codeDesc}" />
          <input type="hidden" name = "code_id2"  id="code_id2" value= "${tc2.codeId}" />
          </td>
        </tr>
          <tr class="table_list_row1">
          <td> 
			工单到索赔单天数限制
          </td>
          <td>
          <input type="text" name = "type3" datatype="0,is_digit,2" id="type3" value= "${tc3.codeDesc}" />
          <input type="hidden" name = "code_id3"  id="code_id3" value= "${tc3.codeId}" />
          </td>
        </tr>
</table>
</center>
<table width="100%">
      <tr> 
      <td colspan="2" align="center">
      	<input type="button" onclick="comfiy()" class="normal_btn"  value="修改"/>&nbsp;&nbsp;
		<input type="button" style="display: none" onclick="history.back(-1);" class="normal_btn"  value="返回"/>
      </td>
	  </tr>
</table>
</form>
<script type="text/javascript">
 function comfiy()
 {
 if($('type').value!=""&&$('type2').value!=""&&$('type3').value!=""){
 	 MyConfirm("是否确认修改？",add);
 }else{
 	MyAlert("天数限制必填!");
 	return false ;
 }
 	
 }
 function add()
 {
 	makeNomalFormCall('<%=contextPath%>/claim/basicData/LaborPriceMain/updateDay.json',updateBack,'fm','');
 }
 function updateBack(josn)
 {
 	 MyAlert("修改成功");
 }
</script>
</body>
</html>

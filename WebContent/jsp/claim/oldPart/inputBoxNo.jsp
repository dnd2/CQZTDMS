<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>索赔件回运清单维护</title>
<% String contextPath = request.getContextPath(); %>
</head>
<script type="text/javascript">
var parentContainer = parent || top;
if( parentContainer.frames ['inIframe'] ){
	parentContainer = parentContainer.document.getElementById ('inIframe').contentWindow;
}
var parentDocument =parentContainer.document;

</script>
<BODY>
<form id="fm" name="fm">
  <table class="table_edit"  >
    <tr align="center">
         <td>
           旧件装箱号：<input type="text" id="boxNo" name="boxNo" value="" datatype="0,is_null,50"/>&nbsp;&nbsp;&nbsp;
           </td>
           <td>
           <input class="normal_btn" type="button" name="finishButton" value="确定" onclick="boxNo11();" />
         </td>
    </tr>
    </table>
</form>
<br>
<script type="text/javascript">
	function boxNo11(){
		var no = document.getElementById("boxNo").value;
		if(no=='' || no==null){
       	  MyAlert("装箱号不能为空!");
       	  return;
   		}
   		var reg = /^\d+$/;
		if(!reg.test(no)){
         	  MyAlert("请输入正整数!");
         	  return;
     	}
     	if(parseInt(no)==0){
         	  MyAlert("箱号只能是大于0的整数!");
         	  return;
     	}
     	if(no.length>4){
     	MyAlert("箱号最长为4位!");
     	return false;
     	}
     	__parent().boxNoBack(no);
    	_hide();
		
	}
</script>
</BODY>
</html>
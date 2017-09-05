<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件出库</title>
<% String contextPath = request.getContextPath(); %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件扫描出库</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
  	  <input type="hidden" name="stockId" id="stockId" value="${stockId}" />
    <TABLE class="table_query">
       <tr>
       
          <td class="table_query_2Col_label_5Letter">件号： </td>
         <td nowrap>
          <input id="part_code" name="part_code" value="" type="text" class="middle_txt"   readonly="readonly">
          
          <input id="part_code1" name="part_code1" value="" type="hidden" class="middle_txt"   readonly="readonly">
            <input id="part_name" name="part_name" value="" type="hidden" class="middle_txt" >
          <a href="#" onclick="selectMainPartCode();">选择</a>
         </td>
         <td class="table_query_2Col_label_5Letter">数量： </td>
              <td nowrap>
           <input id="num" name="num" value="" type="text" class="middle_txt" >
           </td>
       </tr>
       
       <tr>
      <td align="center" colspan="4">
          <input class="normal_btn" type="button" id="addButton" name="addButton" value="补录"  onClick="bulu();">
          &nbsp;&nbsp;
          <input class="normal_btn" type="button" id="addButton" name="addButton" value="返回"  onClick="back();">
       </td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form> 
<br>
<script type="text/javascript">
var cloMainPart=1; //关闭主要配件选择页面
function selectMainPartCode(){
	
	OpenHtmlWindow('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/selectMainPartCodeForward.do',800,500);
	
}

function setMainPartCode(partId,erpdCode,partName,partCode){
	cloMainPart=1;
	document.getElementById("part_code").value =erpdCode;
	document.getElementById("part_name").value =partName;
	document.getElementById("part_code1").value =partCode;
}


function back(){
	var stockId=document.getElementById("stockId").value;
	
	 fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryScanningBulu.do?stockId="+stockId;
	   fm.method="post";
	   fm.submit();
}

function bulu(){
	var partCode=document.getElementById("part_code1").value;
	var num=document.getElementById("num").value;
	var stockId=document.getElementById("stockId").value;
	 var strP=/^\d+(\.\d+)?$/;

		
	if(partCode==null||partCode==''){
		MyAlert("请选择配件代码");
		return;
		}
	if(num==null||num==''||!strP.test(num)){
		MyAlert("请输入数字");
		return;
		}
	 fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/addBuluInfo.do?stockId="+stockId;
	   fm.method="post";
	   fm.submit();

	
}
</script>
</BODY>
</html>
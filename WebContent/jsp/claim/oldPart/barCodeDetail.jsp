<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.bean.ClaimOldPartOutPreListBean"%>
<%@page import="java.util.*" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件出库</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件出库条码选择</div>
  <form id="fm" name="fm">
  <input type="hidden" name="partCode" id="partCode" value="${partCode }"/>
  <input type="hidden" name="partId" id="partId" value="${ID }"/>
    <table class="table_query">
       <tr>
	         <td class="table_query_3Col_label_5Letter">旧件条码： </td>
	         <td nowrap="nowrap" colspan="3">
	          <input id="barNo" name="barNo" value="" type="text" onblur="selectValue(this);" onkeyup="selectValue(this);"  class="middle_txt" datatype="1,is_null,30">
	         </td>
       </tr>
       <tr>
         <td align="center" nowrap="nowrap" colspan="6">
         <!--   <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="query();"> -->
            <input type="button" onclick="preChecked();" class="normal_btn"  value="确定"/>
         &nbsp;&nbsp;
         <input type="button" onclick="_hide();" class="normal_btn"  value="关闭"/>
         </td>
       </tr>
        <tr class="table_list_th">
            <td>全选<input type='checkbox' id='checkAll' name='checkAll' onclick='selectAllBar(this)'></td>
            <td>索赔单号</td>
            <td>供应商代码</td>
            <td>供应商名称</td>
            <td>旧件代码</td>
            <td>旧件名称</td>
            <td>旧件条码</td>
       </tr>
       <% List<ClaimOldPartOutPreListBean> list = (List<ClaimOldPartOutPreListBean>)request.getAttribute("detailList");
      	if(list!=null&&list.size()>0){
      	for(int i=0;i<list.size();i++){ %>
       <tr align="center">
           <td>
            <input type='checkbox' id='orderIds' name='orderIds' value="<%=list.get(i).getBarcode_no() %>" />
           </td>
           <td>
              <%=list.get(i).getClaim_no() %>
           </td>
           <td>
               <%=list.get(i).getSupplier_code() %>
           </td>
           <td>
              <%=list.get(i).getSupplier_name() %> 
           </td>
           <td>
               <%=list.get(i).getPart_code() %>
           </td>
           <td>
             <%=list.get(i).getPart_name() %>  
           </td>
           <td>
              <%=list.get(i).getBarcode_no() %> 
           </td>
         </tr>
      <%	}} else{%>
    	  <tr align="center"> <td colspan="6">
    	  <span style="color: red"> 没有找到相应的数据!</span>
    	   </td>
    	  </tr>
    	  
    	  <% }%>
     
     </table>
</form>
<br>
<script type="text/javascript">
  function selectValue(obj){
   var str = obj.value;
   str = str.replace(/\s+/g,"");  
   	var chk = document.getElementsByName("orderIds");
   	var len = chk.length;
   	for(var i=0;i<len;i++){
   		if(str==chk[i].value){
   		chk[i].checked = true;
   		obj.value="";
   		break;
   		}
   	}
   }
   //格式化时间为YYYY-MM-DD
   function formatDate(value,meta,record) {
	 if (value==""||value==null) {
		return "";
	 }else {
		return value.substr(0,10);
	 }
   }
   function doInit(){
	   loadcalendar();
   }
   function selectAllBar(obj){
   	var chk = document.getElementsByName("orderIds");
   		var len = chk.length;
   var checkAll = document.getElementsByName("checkAll");
   if(obj.checked){
   	for(var i=0;i<len;i++){        
   		chk[i].checked = true;;          
   	}
   }else{
  	 for(var i=0;i<len;i++){        
   		chk[i].checked=false;          
   	}
   	}
   }
   function preChecked(){
   var str="";
   var chk = document.getElementsByName("orderIds");
   	var len = chk.length;
   	var cnt = 0;
   	var id = document.getElementById("partId").value;
   	for(var i=0;i<len;i++){        
   		if(chk[i].checked){            
   			str = str+chk[i].value+","; 
   			cnt++;
   		}
   	}
   	if(str!=""){
   		str = str.substring(0,str.length-1);
   	}

		if (parent.$('inIframe')) {
 			parentContainer.setMainTime(cnt,str,id);
 			parent._hide();
 		} else {
			parent.setMainTime(cnt,str,id);
			parent._hide();
		}
 		if (parent.$('inIframe')) {
 			if(parentContainer.cloMainTime==1) {
	 			//关闭弹出页面
	 			parent._hide();
 			}
 		}else {
 			if(parent.cloMainTime==1) {
	 			//关闭弹出页面
	 			parent._hide();
 			}	
 		}
 		
   }
</script>
</body>
</html>
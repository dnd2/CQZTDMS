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
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件出库单据选择</div>
  <form id="fm" name="fm">
  <input type="hidden" name="partCode" id="partCode" value="${partCode }"/>
  <input type="hidden" name="partId" id="partId" value="${ID }"/>
    <table class="table_query">
       <tr>
	         <td class="table_query_3Col_label_5Letter">旧件条码： </td>
	         <td nowrap="nowrap" colspan="7">
	          <input id="barNo" name="barNo" value="" type="text" onblur="selectValue(this);" onkeyup="selectValue(this);"   class="middle_txt" datatype="1,is_null,30">
	         </td>
       </tr>
       <tr>
         <td align="center" nowrap="nowrap" colspan="8">
            <input type="button" onclick="preChecked();" class="normal_btn" style="width=8%" value="确定"/>
         &nbsp;&nbsp;
         <input type="button" onclick="_hide();" class="normal_btn" style="width=8%" value="关闭"/>
         </td>
       </tr>
       
        <tr>
         <td align="left" nowrap="nowrap" colspan="8">
         随机选中 ：<input id="aa"  value="" type="text" onkeyup="checkNum(this.value);"   class="little_txt" >&nbsp;&nbsp;条
	         
            <input type="button" id="zd" onclick="zdChoose();" class="normal_btn" style="width=8%" value="选择"/>
            <span style="color: red">注：【确定】 按钮是确认整个页面的选择结果并返回,【选择】按钮是根据输入的随机选择条数进行自动选择数据.</span>
         </td>
       </tr>
       
        <tr class="table_list_th">
            <td>全选<input type='checkbox' id='checkAll' name='checkAll' onclick='selectAllBar(this)'></td>
            <td>序号</td>
            <td>索赔单号</td>
            <td>供应商代码</td>
            <td>供应商名称</td>
            <td>旧件代码</td>
            <td>旧件名称</td>
            <td>旧件条码</td>
       </tr>
       <% List<ClaimOldPartOutPreListBean> list = (List<ClaimOldPartOutPreListBean>)request.getAttribute("detailList");
       int size = list.size();
      	if(list!=null&&list.size()>0){
      	for(int i=0;i<list.size();i++){ %>
      		 <tr >
           <td>
            <input type='checkbox' id='orderIds' name='orderIds' value="<%=list.get(i).getBarcode_no() %>" />
            <input type='hidden' id='flag<%=list.get(i).getBarcode_no() %>' name='flag' value="0" />
           </td>
           <td>
              <%=i+1 %>
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
    	  <tr align="center">
    	  <span style="color: red"> 没有找到相应的数据!</span>
    	  </tr>
    	  
    	  <% }%>
     
     </table>
</form>
<br>
<script type="text/javascript">
	function checkNum(num){
	 	var reg = /^\d+$/;
	if(num!="" && !reg.test(num)){
		MyAlert("请输入正整数!");
		$('aa').value="";
		return false;
	}else if(parseInt(num)>380){
		MyAlert("输入的数量不能大于380!");
		$('aa').value="";
		return false;
	}else 	if(parseInt(num)><%=size%>){
		MyAlert("输入的数量不能大于总数量!");
		$('aa').value="";
		return false;
	}else{
		return true;
	}
	}
	function zdChoose(){
		var num = $('aa').value;
		if(checkNum(num)){
			var chk = document.getElementsByName("orderIds");
   			var len = chk.length;
   			if(parseInt(num)>len){
   				MyAlert("输入的随机选择数量不能大于可选择数量!");
   				return false;
   			}else{
   			for(var i=0;i<len;i++){
   				if(document.getElementById("flag"+chk[i].value).value==0){//将所有不是扫描的条码设置为不是选中状态
   				chk[i].checked = false;
   				}
   			}
   			for(var k = 0;k<num;k++){
   				if(document.getElementById("flag"+chk[k].value).value==0){//如果是扫描的条码则不处理并将num+1
   					chk[k].checked = true;;
   				} else {
   					num++;
   				}
   			}
   		}
 	 }
}
   function selectValue(obj){
   var str = obj.value;
   str = str.replace(/\s+/g,"");  
   	var chk = document.getElementsByName("orderIds");
   	var len = chk.length;
   	for(var i=0;i<len;i++){
   		if(str==chk[i].value){
   		chk[i].checked = true;
   		obj.value="";
   		document.getElementById("flag"+str).value=1;
   		break;
   		}
   	}
   	if(str.length==18){
   	obj.value="";
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
  	 if(document.getElementById("flag"+chk[i].value).value==0){
  		 chk[i].checked=false;   
  	 }        
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
 			parentContainer.setMainTime2(cnt,str,id);
 			parent._hide();
 		} else {
			parent.setMainTime2(cnt,str,id);
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
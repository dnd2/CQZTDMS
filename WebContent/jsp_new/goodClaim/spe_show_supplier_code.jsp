<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); 
String id=request.getParameter("id");
String id_name=request.getParameter("id_name");
String old_code=request.getParameter("old_code");
%>
<script language="JavaScript" >
</script>
<body onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation">
<img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：供应商选择 </div>
</div>
 <form  name="fm" id="fm" method="post">
<input class="middle_txt" id="partcode" name="partcode" value="${partcode }" type="hidden"/>
<input class="middle_txt" id="id" name="id" value="<%=id%>" type="hidden"/>
<input class="middle_txt" id="id_name" name="id_name" value="<%=id_name%>" type="hidden"/>
<input class="middle_txt" id="old_code" name="old_code" value="<%=old_code%>" type="hidden"/>
<!--查询条件begin-->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    <tr>
		<td  class="right" nowrap="true">供应商代码：</td>
      	<td  nowrap="true">
      		<input class="middle_txt" id="maker_code" name="maker_code" value="" type="text"/>
      	</td>
        <td  class="right" nowrap="true">供应商名称：</td>
      	<td  nowrap="true">
      		<input name="maker_shotname" type="text" id="maker_shotname"  class="middle_txt"/>
      	</td>
	</tr>
  	<tr>
    	<td class="center" colspan="8">
    		<input type="button" name="btnQuery" id="btnQuery" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;
    		<input name="button2" type="button" class="normal_btn" onclick="parent._hide();" value="关 闭" />
    	</td>
    </tr>
 </table>
 <!--查询条件end-->
 <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
 <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="JavaScript" >
	var myPage;
	
	var url = "<%=contextPath%>/GoodClaimAction/querySupplierCode.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'MAKER_ID',renderer:mySelect,align:'center'},
				{header: "供应商代码", dataIndex: 'MAKER_CODE', align:'center'},
				{header: "供应商名称", dataIndex: 'MAKER_SHOTNAME', align:'center'}
		      ];
		      
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setData(\""+record.data.MAKER_CODE+"\",\""+record.data.MAKER_SHOTNAME+"\")' />");
	}

	function setData(v1,v2){
		 //调用父页面方法
		 if(v2==null||v2=="null"){
		 	v2 = "";
		 }
		 if(v1==null||v1=="null"){
		 	v1 = "";
		 }
		 var id=document.getElementById("id").value;
		 var id_name=document.getElementById("id_name").value;
		 if(id=="0"){
			 __parent().setOldCode(v1);
		 }
		 else{
			 if (__parent().$('inIframe')) {			
		 			__parent().setSupplierCode(v1,v2,id,id_name);
		 		} else {
		 			__parent().setSupplierCode(v1,v2,id,id_name);
				} 
		 }
	 		
	 	//关闭弹出页面
	 	parent._hide();
	}
	
</script>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>监控配件维护</title>
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔预授权&gt;预授权监控配件维护</div>

  <form name='fm' id='fm'>
	  <input type="hidden" name="wrmodelgrouplist" id="wrmodelgrouplist" value="<%=request.getAttribute("wrmodelgrouplist")%>"/>
	  <table class="table_query">
       <tr>            
        <td class="table_query_2Col_label_5Letter">配件代码：</td>            
        <td>
			<input  class="middle_txt" id="PART_CODE"  name="PART_CODE" type="text" datatype="1,is_null,27"/>
        </td>
        <td class="table_query_2Col_label_5Letter">配件名称：</td>
        <td><input type="text" name="PART_NAME" id="PART_NAME" datatype="1,is_digit_letter_cn,30" class="middle_txt" value=""/></td>
       </tr>
       <tr>            
        <td style="color: #252525;width: 115px;text-align: right">索赔配件车型组：</td>            
        <td>
		   <script type="text/javascript">
              var wrmodelgrouplist=document.getElementById("wrmodelgrouplist").value;
    	      var str="";
    	      str += "<select id='WRGROUP_ID' name='WRGROUP_ID'  datatype='0,is_null,18' class='short_sel'>";
    	      str+=wrmodelgrouplist;
    		  str += "</select>";
    		  document.write(str);
	       </script>
        </td>
       </tr> 
       <tr>            
        <td colspan="4" align="center">
        	    <input  id="queryBtn" class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1)" />
        	    <input  id="queryBtn1" class="normal_btn" type="button" name="button2" value="关闭"  onclick="parent.window._hide();" />
        </td>
       </tr>       
 	</table>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<form  name="form1" id="form1">
<table class="table_query" width="85%" align="center">
	<tr class="table_list_row2">
		<td align="center">
			<input type="button" name="button1" class="cssbutton" onclick="toDispatch();" value="下发" /> 
		</td>
	</tr>
</table>
</form> 
<script type="text/javascript">
	document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];
	
	var myPage;
	var url = "<%=request.getContextPath()%>/claim/preAuthorization/PartMaintain/toDispatchQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"ids\")' />全选", width:'6%',sortable: false,dataIndex: 'ID',renderer:myCheckBox},
				{header: "车型组代码",sortable: false,dataIndex: 'WRGROUP_CODE',align:'center'},
				{header: "车型组名称",sortable: false,dataIndex: 'WRGROUP_NAME',align:'center'},
				{header: "配件代码",sortable: false,dataIndex: 'PART_CODE',align:'center'},
				{header: "配件名称",sortable: false,dataIndex: 'PART_NAME',align:'center'}	
		      ];
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='ids' value='" + value + "' />");
	}
	function toDispatch(){
		var ids = document.getElementsByName("ids");
		var str = new Array();
		var ids__="";
		if(ids){
			for(var i=0;i<ids.length;i++){
				if(ids[i].checked){
					str.push(ids[i].value);
				}
				
			}
		}
		if(str.toString()){
			MyDivConfirm("是否下发?",dispatchAction,[str.toString()]);
		}else{
			MyDivAlert("请选择具体信息!");
		}
	}
	function dispatchAction(ids){
		makeNomalFormCall('<%=contextPath%>/claim/preAuthorization/PartMaintain/partDispatch.json?ids='+ids,showResult,'fm');
		
	}
	function showResult(josn){
		parent._hide();
	}
</script> 
  </body> 
</html>
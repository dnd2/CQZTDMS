<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();

List<Map<String, Object>> list = (List<Map<String, Object>>)request.getAttribute("list");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>物料组选择</title>

<script language="JavaScript">
function init(){
	getSeries($('brand'));
}

</script>
</head>
<body onload="init();">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：物料组选择 </div>
</div>
<form method="post" name ="fm" id="fm">
	<table class="table_edit">
   <!--   <tr>
      <td class="table_query_3Col_label_6Letter" nowrap="nowrap">物料组代码：</td>
      <td class="table_query_3Col_input" nowrap="nowrap">
      	<input class="middle_txt" id="groupCode" name="groupCode" value="" type="text"/>
      </td>
      <td class="table_query_3Col_label_6Letter" nowrap="nowrap">物料组名称：</td>
      <td>
      	<input name="groupName" type="text" id="groupName"  class="middle_txt"/>
      </td>
      </tr>
      -->
       <tr>
      <td class="table_query_3Col_label_6Letter" nowrap="nowrap">品牌：</td>
      <td class="table_query_3Col_input" nowrap="nowrap">
      <select name="brand" id = "brand" class="short_sel" onchange="getSeries(this);">
				<%for(int i=0;i<list.size();i++){%>
					<option value="<%=list.get(i).get("GROUP_ID") %>"><%=list.get(i).get("GROUP_CODE") %>--<%=list.get(i).get("GROUP_NAME") %></option>
				<%} %>
      </select>
      </td>
      <td class="table_query_3Col_label_6Letter" nowrap="nowrap">车系：</td>
      <td id="series1">
 		<select name="series" id = "series" class="short_sel">
 		<option value="">--请选择--</option>
      </select>
      </td>
      </tr>
      
      <tr>
      <td align="center" colspan="4">
      	<input type="button" name="queryBtn"  id="queryBtn"  value="查询"  class="normal_btn" onClick="checkData();" >
      </td>
  	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<form name="form1" style="display:none">
   <table class="table_list" id="table1" >
  	  <tr>
  	  	<th align="center">
	    	<input class="normal_btn" type="button" value="确认" onclick="doConfirm()"/>
	    	<input class="normal_btn" type="button" value="关闭" onclick="parent._hide()"/>
       </th>
  	  </tr>
   </table>
  </form>
<script type="text/javascript" >
	document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];
	
	var myPage;

	var url = "<%=contextPath%>/repairOrder/RoMaintainMain/showGroupList.json";
				
	var title = null;

	var columns = [
				{header: "选择", dataIndex: 'GROUP_CODE', align:'center',renderer:seled},
				{header: "物料组代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "物料组名称", dataIndex: 'GROUP_NAME', align:'center'}
		      ];
		      __extQuery__(1);
	function seled(value,meta,record){
        	return "<input type='checkbox' name='checkCode' id='checkCode' value='"+ value + "' />";
    }
 function doConfirm(){
	var code = document.getElementsByName("checkCode");
	var str="";
	var len = code.length;
	for(var i=0;i<len;i++){        
   		if(code[i].checked){            
   			str = str+code[i].value+","; 
   		}
   	}
	if(str!=""){
		str = str.substring(0,str.length-1);
	}
		
 		if (parent.$('inIframe')) {
 			parentContainer.setMainCode(str);
 		} else {
			parent.setMainCode(str);
		}
 		if(parentContainer.cloMainPart==1) {
 			//关闭弹出页面
 			parent._hide();
 		 }
	}    
	
	
function getSeries(obj){

 	if(obj.value==""){
 		var str = '<select class="short_sel" id="series" name="series" > <option value="00">--请选择--</option> </select>';
		document.getElementById('series1').innerHTML = str;
 	}else{
	makeNomalFormCall('<%=contextPath%>/repairOrder/RoMaintainMain/showSeriesList.json?ID='+obj.value,changeBack,'fm','');
	}
}
function changeBack(json) {
	if(json.seriesList != null && json.seriesList != "") {
		document.getElementById('series1').innerHTML = json.seriesList;
		__extQuery__(1);
	} else {
		MyAlert("加载车系失败！");
	}
} 
function checkData(){
	var brand=$('brand').value;
	if(brand==""){
	MyAlert("请选择品牌!");
	return false;
	}else{
	__extQuery__(1);
	}
}
</script>
</body>
</html>
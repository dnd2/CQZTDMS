<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title> 城市里程数维护 </title>
</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置&gt;整车物流管理&gt;储运基础信息&gt;城市里程数维护
	</div>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab" >
  <tr class="csstr" align="center"> 		
		 <td class="right">省份：</td>  
		    <td align="left">
	  		<select class="u-select" id="pro" name="PROVINCE" onchange="_genCity(this,'city')"></select>
     	 </td> 
     	 <td class="right">地级市：</td>  
		    <td align="left">
	  		<select class="u-select" id="city" name="CITY_ID" onchange="_genCity(this,'county')"></select>
     	 </td>   
		  <td class="right">区县：</td>
		   	   <td align="left">
	  				<select class="u-select" id="county" name="COUNTY_ID"></select>
			 </td> 
  </tr>  
  <tr align="center">
  <td colspan="9" align="center"> 
    	   <input type="button" id="queryBtn" class="normal_btn"  value="确定" onclick="add();" />  	
    	  <input type="button" id="saveButton" class="normal_btn"  value="关闭" onclick="_hide();" />   	
    </td>
  </tr>
</table>

</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	
	//初始化    
	function doInit(){
		//__extQuery__(1);
		 genLocSel('pro','','');//支持火狐
	}
	function add()
	{
		//得到目的地
		var endPlace = "";
		var pro = document.getElementById("pro");
		for(var i = 0;i<pro.length;i++){
			if(pro[i].selected == true){
				endPlace += pro[i].innerText+"-";
				break;
			}
		}
		var city =document.getElementById("city");
		for(var i = 0;i<city.length;i++){
			if(city[i].selected == true){
				endPlace += city[i].innerText+"-";
				break;
			}
		}
		var con = document.getElementById("county");
		for(var i = 0;i<con.length;i++){
			if(con[i].selected == true){
				endPlace += con[i].innerText;
				break;
			}
		}
		parent.document.getElementById('inIframe').contentWindow.setText(endPlace);
		_hide();
	}
	
</script>
</body>
</html>

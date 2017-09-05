<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件车系关系修改</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据维护&gt;配件车系关系修改</div>
  <form name='fm' id='fm' method="post">
  <input type="hidden" value="${id}" name="id" />
  <TABLE class="table_query">
       <tr>            
        <td style="color: #252525;width: 115px;text-align: right">部位代码：</td>            
        <td>
			<input  class="middle_txt" disabled="disabled" id="PART_OLDCODE"  name="PART_OLDCODE" type="text" value="${map.PART_OLDCODE}"  />
			
        </td>
        <td class="table_query_3Col_label_6Letter">部位名称：</td>
        <td>
        	<input  disabled="disabled"  name="PART_CNAME" id="PART_CNAME" class="middle_txt" value="${map.PART_CNAME}"/>
        	<script type='text/javascript'>
					var name=getItemValue('${map.PART_TYPE}');
					document.write(name);
				</script>
        </td>  
       </tr>  
       <tr>
         <td style="color: #252525;width: 115px;text-align: right">车型代码：</td>            
        <td>
			<input  class="middle_txt" disabled="disabled" id="GROUP_CODE"   name="GROUP_CODE" type="text" value="${map.GROUP_CODE}"/>
			<input  class="middle_txt"  id="GROUP_ID"   name="GROUP_ID" type="hidden" value="${map.GROUP_ID}"/>
			<input name='showBtn1' type='button' class='mini_btn' style='cursor: pointer;' onclick='open_group()' value='...' />
        </td>
        <td class="table_query_3Col_label_6Letter">车型名称：</td>
        <td><input type="text" disabled="disabled" name="GROUP_NAME" id="GROUP_NAME" class="middle_txt" value="${map.GROUP_NAME}"/></td>
       </tr>  
       <tr>  
       <td colspan="4" align="center">
        	     <input   id="queryBtn" class="normal_btn" type="button" name="button1" value="修改"  onclick="judePosition()"/>
        	     <input   id="queryBtn" class="normal_btn" type="button" name="button1" value="返回"  onclick="javascript:history.go(-1)"/>
        </td>
       </tr>    
 	</table>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  </body>
</html>
<script type="text/javascript" >	
function judePosition()
 {
 	var GROUP_CODE = document.getElementById('GROUP_CODE').value;
 	if(GROUP_CODE.length == 0) 
 	{
 		MyAlert('请选择车系');
 	}
 	makeNomalFormCall('<%=contextPath%>/claim/basicData/LaborPriceMain/updateintpart_series.json',addBack,'fm','');
 }


function addBack(json)
 {
    if(json.cont != null )
    {
       MyAlert(json.cont);
    }else
    {
    	MyAlert('修改成功');
	   	$('fm').action = "<%=contextPath%>/claim/basicData/LaborPriceMain/part_series.do"
		$('fm').submit();
    }
   
 }
 
 function open_group()
 {
   	OpenHtmlWindow('<%=contextPath%>/claim/basicData/LaborPriceMain/open_group.do',800,500);
 }
 

</script>  
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
<title>监控配件维护</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔预授权&gt;关注部位维护</div>
  <form name='fm' id='fm' method="post">
  <TABLE class="table_query">
       <tr id="judeposition" style="display: none;"><td colspan="4"><span style="color: red;">部位代码或者部位名称已存在</span></td></tr>
       <tr>            
        <td style="color: #252525;width: 115px;text-align: right">关注部位代码：</td>            
        <td>
			<input  class="middle_txt" id="POS_CODE"  onfocus="javascript:this.value='';" name="POS_CODE" type="text"  datatype="2,is_null,27"/>
        </td>
        <td class="table_query_3Col_label_6Letter">关注部位名称：</td>
        <td><input type="text" name="POS_NAME" id="POS_NAME" onfocus="javascript:this.value='';" datatype="2,is_null,27" class="middle_txt"/></td>
        <td class="table_query_3Col_label_6Letter">&nbsp;</td>
        <td>&nbsp;</td>     
       </tr>  
       <tr>
         <td style="color: #252525;width: 115px;text-align: right">三包月份：</td>            
        <td>
			<input  class="middle_txt" id="WR_MONTHS"   name="WR_MONTHS" type="text" datatype="0,is_digit,4"/>
        </td>
        <td class="table_query_3Col_label_6Letter">三包里程：</td>
        <td><input type="text" name="WR_MILEAGE" id="WR_MILEAGE" datatype="0,is_double" decimal="2" class="middle_txt" value=""/></td>
        <td class="table_query_3Col_label_6Letter">&nbsp;</td>
        <td>&nbsp;</td>
       </tr>    
       <tr>
         <td style="color: #252525;width: 115px;text-align: right">关注天数：</td>            
        <td>
			<input  class="middle_txt" id="ATT_DAYS"  name="ATT_DAYS" type="text" datatype="0,is_digit,4"/>
        </td>
        <td class="table_query_3Col_label_6Letter">关注里程：</td>
        <td><input type="text" name="ATT_MILEAGE" id="ATT_MILEAGE" datatype="0,is_double" decimal="2" class="middle_txt" value=""/></td>
        <td class="table_query_3Col_label_6Letter">&nbsp;</td>
        <td>&nbsp;</td>
       </tr>  
       <tr>
       <td colspan="4" align="center">
        	     <input   id="queryBtn" class="normal_btn" type="button" name="button1" value="新增"  onclick="judePosition()"/>
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
 	var POS_CODE = document.getElementById('POS_CODE').value;
 	var POS_NAME = document.getElementById('POS_NAME').value;
 	if(POS_CODE.length == 0) 
 	{
 		document.getElementById('POS_CODE').value = '配件代码不能不空';
 		return;
 	}
 	
 	if(POS_NAME.length == 0) 
 	{
 		document.getElementById('POS_NAME').value = '配件名称不能不空';
 		return;
 	}
 	
 	makeNomalFormCall('<%=contextPath%>/repairOrder/RoMaintainMain/judePosition.json',addBack,'fm','');
 }
 function addBack(json)
 {
 	if(json.success != null && json.success == "yes") {
            MyConfirm("是否新增？",addBackcomit);
		}else{
			document.getElementById('judeposition').style.display = '';
		}
 }
function addBackcomit()
{
	$('fm').action = "<%=contextPath%>/repairOrder/RoMaintainMain/addposition.do"
    $('fm').submit();
}
 

</script>  
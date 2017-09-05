<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件库存查询</title>
<% String contextPath = request.getContextPath(); %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;条码出库明细查询</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
    <TABLE class="table_query">
       <tr>
		
	         <td class="table_query_3Col_label_5Letter">配件名称： </td>
	         <td nowrap="nowrap">
	          <input id="part_name" name="part_name" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	         </td>
	             <td class="table_query_3Col_label_5Letter">配件代码： </td>
	         <td nowrap="nowrap">
	          <input id="part_code" name="part_code" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	         </td>
	            
       </tr>
       <tr>
	      
	      
			<td align="right" nowrap="nowrap">出库方式：</td>
			<td align="left" nowrap="nowrap">
				<script type="text/javascript">
				 genSelBoxExp("exitScrap",<%=Constant.Exit_scrap%>,'',true,"false","","true",'<%=Constant.Exit_scrap_1%>');					
			    </script>
			</td>
			 <td class="table_query_2Col_label_6Letter">出库时间： </td>
         <td nowrap="nowrap">
          <input name="create_start_date" id="create_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="create_start_date,create_end_date" hasbtn="true" callFunction="showcalendar(event, 'create_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="create_end_date" id="create_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="create_start_date,create_end_date" hasbtn="true" callFunction="showcalendar(event, 'create_end_date', false);">
          </td>
       </tr>
       <tr>
         <td align="center" nowrap="nowrap" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onclick="asd();">
           &nbsp;&nbsp;
          
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

function asd(){
	__extQuery__(1);
	
}
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/StockDetailQuery.json";
				
   var title = null;
   
   var columns = [
				{header: "序号",align:'center',renderer:getIndex},							
  				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
  				{header: "配件名称",dataIndex: 'PART_NAME',align:'center'},		
  				{header: "出库方式",dataIndex: 'STOCK_TYPE',align:'center',renderer:getItemValue},	
  				{header: "出库时间",dataIndex: 'STOCK_DATE',align:'center'},	
  				{header: "出库数", dataIndex: 'STOCK_NUM', align:'center'}
  			
  				
  		      ];
   function doInit(){
	   loadcalendar();
   }
   //导出功能
   function to_excel(){
	   fm.action = "<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/toExcel.do";
	fm.submit();
   }
</script>
</BODY>
</html>
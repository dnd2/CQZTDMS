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
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;配件是否索赔维护</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
    <TABLE class="table_query">
      
       <tr>
         <td class="table_query_2Col_label_5Letter">配件名称： </td>
         <td nowrap>
          <input id="part_name" name="part_name" value="" type="text" class="middle_txt" datatype="1,is_null,30">
         </td>
         <td class="table_query_2Col_label_5Letter">配件代码：</td>
         <td nowrap>
            <input id="part_code" name="part_code" value="" type="text" class="middle_txt" datatype="1,is_null,30">
         </td>
       </tr>
        <tr>
         <td class="table_query_2Col_label_5Letter">件号： </td>
         <td nowrap>
          <input id="erpd_code" name="erpd_code" value="" type="text" class="middle_txt" datatype="1,is_null,30">
         </td>
        <td class="table_query_2Col_label_5Letter">是否索赔： </td>
         <td nowrap>
          <select id="isCliam" name="isCliam">
          		<option value="">-请选择-</option>
          			<option value="no">-是-</option>
          				<option value="yes">-否-</option>
          </select>
         </td>
       </tr>
       <tr>
         <td align="center" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="__extQuery__(1);">
      
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
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryPartBaseList.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "配件名称",dataIndex:'partName',align:'center'},
  				{header: "配件代码", dataIndex:'partCode', align:'center'},
  				{header: "件号", dataIndex:'erpdCode', align:'center'},
  				{header: "是否索赔", dataIndex:'isCliams', align:'center'},
  				{header: "操作", dataIndex:'partId', align:'center',renderer:myLink}
  		      ];

   function myLink(value,meta,record){

	 

	   return String.format("<a href='#' onclick=isCheck13("+value+","+record.data.isCliam+")>[修改]</a>");
	 
	   }


   function isCheck13(partId,isCliam){
	   if (confirm('确定修改吗？')){    
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryPartBaseUpdate.do?partId="+partId+"&isCliam="+isCliam;
	   fm.method="post";
	   fm.submit();
	}}




   
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
  
</script>
</BODY>
</html>
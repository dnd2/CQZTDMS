<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件审批入库</title>
<% String contextPath = request.getContextPath(); %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;北京车型组维护</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
    <TABLE class="table_query">
     
       <tr>
          <td class="table_query_2Col_label_6Letter">车型代码：</td>
         <td><input id="group_code" name="groupCode" value="" type="text" class="middle_txt"></td>
          <td class="table_query_2Col_label_6Letter">车型名称：</td>
         <td><input id="group_name" name="groupName" value="" type="text" class="middle_txt"></td>
       </tr>
     
       <tr>
         <td align="center" colspan="2" nowrap="nowrap">
           <input class="normal_btn" type="button" name="qryButton" value="查询"  onClick="__extQuery__(1);">
         </td>
         
            <td>
           <input class="normal_btn" type="button" name="qryButton" value="新 增"  onClick="goToAdd();">
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/BJMaterialMaintainQuery.json";
				
   var title = null;

   var columns = [
  			
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "车型代码", dataIndex: 'GROUP_CODE', align:'center'},
  				{header: "车型名称", dataIndex: 'GROUP_NAME', align:'center'},
  				{header: "操作",dataIndex: 'ID',renderer:myLink,align:'center'}
  		      ];

   //超链接设置
   function myLink(value,meta,record){
	   var back_type=record.data.back_type;
	   var box_no = record.data.box_no;//装箱单号

	   var currStatus = record.data.status;
 	 
		   return String.format(
	               "<a href='#' onclick=isCheck("+value+")>[删除]</a>");
	   
	   
   }
   function isCheck(id){
	   if (confirm('确定删除吗？')){    
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/BJMaterialMaintainDelete.do?id="+id;
       fm.method="post";
       fm.submit();
	   }
   }
  

   //转到新增页面
   function goToAdd(){
	
	   
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/BJMaterialMaintainAddQuery.do";
	   fm.method="post";
       fm.submit();
   }
</script>
</BODY>
</html>
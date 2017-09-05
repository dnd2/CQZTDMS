<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="com.infodms.dms.po.TmBusinessAreaPO,java.util.*" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件审批入库</title>
<% String contextPath = request.getContextPath();
	String isReutrn=request.getAttribute("isReturn")==null?"":request.getAttribute("isReturn").toString();

	List<TmBusinessAreaPO> list = (List<TmBusinessAreaPO>)request.getAttribute("yieldlyList");
	if(isReutrn.equals("1")) isReutrn="__extQuery__(1);";
%>
</head>
<BODY onload="doInit();<%=isReutrn %>">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;供应商问题明细</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
    <TABLE class="table_query">
       <tr>
         <td align="right" nowrap="nowrap"  >经销商代码：</td>            
         <td align="left" nowrap="nowrap" >
        <input id="dealerCode" name="supply_code"  type="text" class="middle_txt">
         </td>
         <td align="right" nowrap="nowrap" >经销商名称：</td>
         <td align="left" nowrap="nowrap"><input id="supply_name" name="supply_name"  type="text" class="middle_txt" datatype="1,is_name,15" callFunction="javascript:MyAlert();"></td>
       </tr>
        <tr>
         <td align="right" nowrap="nowrap"  >配件代码：</td>            
         <td align="left" nowrap="nowrap" >
        <input id="part_name" name="part_code"  type="text" class="middle_txt">
         </td>
         <td align="right" nowrap="nowrap" >配件名称：</td>
         <td align="left" nowrap="nowrap"><input id="part_name" name="part_name" type="text" class="middle_txt" datatype="1,is_name,15" callFunction="javascript:MyAlert();"></td>
       </tr>
      
       <tr>
         <td align="center" colspan="4" nowrap="nowrap">
           <input class="normal_btn" type="button" name="qryButton" value="查询"  onClick="__extQuery__(1);">
         <input class="normal_btn" type="button" name="qryButton" value="返回"  onClick="history.back();">
         </td>
       </tr>
       
       <input type="hidden" name="problem_id" value="${problem_id }"> 
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
   var url = "<%=contextPath%>/claim/oldPart/OldPartMakerProblemManager/makerProblemDetailQuery.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
				{header: "配件代码",dataIndex: 'PART_CODE',align:'center'},
  				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
  				{header: "供应商代码", dataIndex: 'SUPPLY_CODE', align:'center'},
  				{header: "供应商名称", dataIndex: 'SUPPLY_NAME', align:'center'},
  				{header: "创建时间", dataIndex: 'CREATE_DATE', align:'center',renderer:formatDate}
  		      ];
  		      __extQuery__(1);
  
	
   //格式化时间为YYYY-MM-DD
   function formatDate(value,meta,record) {
	 if (value==""||value==null) {
		return "";
	 }else {
		return value.substr(0,16);
	 }
   }
   function doInit(){
	  loadcalendar();
   }
</script>
</BODY>
</html>
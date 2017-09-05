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
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;退赔单财务确认</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
  	  <input type="hidden" name="yieldly" id="yieldly" value="${yieldly }" />
    <TABLE class="table_query">
       <tr>
         <td style="color: #252525;width: 115px;text-align: right">供应商简称：</td>
         <td><input id="supply_name" name="supply_name" value="" type="text" class="middle_txt" datatype="1,is_null,30" callFunction="javascript:MyAlert();"></td>
        <td style="color: #252525;width: 115px;text-align: right">供应商编码： </td>
         <td nowrap>
          <input id="supply_code" name="supply_code" value="" type="text" class="middle_txt" datatype="1,is_null,30">
         </td>
       </tr>
        <tr>
         <td style="color: #252525;width: 115px;text-align: right">退赔单号： </td>
         <td nowrap>
          <input id="range_no" name="range_no" value="" type="text" class="middle_txt" datatype="1,is_null,30">
         </td>
         <td style="color: #252525;width: 115px;text-align: right"></td>
         <td nowrap>
         </td>
       </tr>
       <tr>
         <td align="center" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="__extQuery__(1);">
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
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryRangeList.json";
				
   var title = null;

   var columns = [
   				{header: "序号",align:'center',renderer:getIndex},
   				{header: "操作", dataIndex: 'OUT_NO', align:'center',renderer:myLink},
  				{header: "退赔单号", dataIndex: 'RANGE_NO', align:'center'},
  				{header: "出库时间",dataIndex: 'CREATE_TIME',align:'center'},
  				{header: "供应商编码", dataIndex: 'SUPPLY_CODE',align:'center'},
  				{header: "供应商名称", dataIndex: 'SUPPLY_NAME', align:'center'},
  				{header: "确认人", dataIndex: 'NAME',align:'center'},
  				{header: "确认时间", dataIndex: 'AUDIT_TIME', align:'center'},
  				{header: "数量", dataIndex: 'PART_QUANTITY',align:'center'},
  				{header: "小计", dataIndex: 'SMALL_AMOUNT', align:'center'}
  		      ];
  		      
  		      __extQuery__(1);
  	function myLink(value,meta,record){
  	var flag = record.data.OUT_TYPE;
  	var relNo=record.data.RELATIONAL_OUT_NO;
  	var str="";
  		str +="<a href='#' onClick='saveRenge(\""+value+"\");'>[退赔单明细]</a>";
  		return String.format( str);
	}
	//生成退赔单
	function saveRenge(value){
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/saveRengePer.do?cType=2&out_no="+value;
       	fm.method="post";
       	fm.submit();
	}
	
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
   //转到新增页面
   function goToAdd(){
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/addPageList.do";
       fm.submit();
   }
</script>
</BODY>
</html>
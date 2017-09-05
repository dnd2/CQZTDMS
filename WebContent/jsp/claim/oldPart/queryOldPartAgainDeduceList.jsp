<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件二次抵扣</title>
<% String contextPath = request.getContextPath(); %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件二次抵扣</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
    <TABLE class="table_query">
       <tr>
         <td class="table_query_2Col_label_5Letter">经销商代码：</td>            
         <td>
			<input class="middle_txt" id="dealerName"  name="dealerName" type="text" datatype="1,is_null,100"/>
            <input name="button1" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerName','','true','',true);" value="..." />        
            <input name="button2" type="button" class="normal_btn" onClick="clr();" value="清除"/>
         </td>
         <td width="7%" align="right" nowrap>索赔单号：</td>
         <td><input id="back_order_no" name="back_order_no" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,16" callFunction="javascript:MyAlert();"></td>
       </tr>
       <tr>
         <td width="7%" align="right" nowrap>配件代码：</td>
         <td>
           <input id="part_code" name="part_code" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,16" callFunction="javascript:MyAlert();">
         </td>
         <td width="7%" align="right" nowrap>配件名称：</td>
         <td>
           <input id="part_name" name="part_name" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,16" callFunction="javascript:MyAlert();">
         </td>
       </tr>
       <tr>
         <td align="center" colspan="4" nowrap><input class="normal_btn" type="button" name="qryButton" value="查询"  onClick="__extQuery__(1);">
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartAgainDeduceManager/queryOldPartAgainDeduceList.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "索赔单号", dataIndex: 'return_no', align:'center'},
  				{header: "配件代码", dataIndex: 'transport_desc', align:'center'},
  				{header: "配件名称", dataIndex: 'create_date', align:'center'},
  				{header: "需回运数", dataIndex: 'report_date', align:'center'},
  				{header: "入库数", dataIndex: 'store_date', align:'center'},
  				{header: "差异数", dataIndex: 'diff_amount', align:'center'},
  				{id:'action',header: "操作",sortable: false,dataIndex: 'return_id',renderer:myLink,align:'center'}
  		      ];
   //超链接设置
   function myLink(value,meta,record){
	   return String.format(
		       "<a href=\"<%=contextPath%>/claim/oldPart/ClaimOldPartDeduceManager/searchDetailInfo.do?RETURN_ID="
				+ value + "\">[抵扣]</a>"
				);
   }
   function doInit(){
	  loadcalendar();
   }
   //清除经销商代码
   function clr() {
  	  document.getElementById('dealerName').value = "";
   }
</script>
</BODY>
</html>
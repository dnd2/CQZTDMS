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
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件扫描出库</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
    <TABLE class="table_query">
       <tr>
       
          <td class="table_query_2Col_label_5Letter">出库单号： </td>
         <td nowrap>
          <input id="stock_no" name="stock_no" value="" type="text" class="middle_txt" datatype="1,is_null,30">
         </td>
         <td class="table_query_2Col_label_5Letter">出库时间：</td>
         <td align="left" >
          <input name="out_start_date" id="out_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="out_start_date,out_end_date" hasbtn="true" callFunction="showcalendar(event, 'out_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="out_end_date" id="out_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="out_start_date,out_end_date" hasbtn="true" callFunction="showcalendar(event, 'out_end_date', false);">
         </td>
         </tr>
         <tr>
         <td class="table_query_2Col_label_6Letter">供应商代码：</td>
      <td class="table_query_2Col_input">
      	<input class="long_txt" id="SUPPLIER_CODE" name="SUPPLIER_CODE" value="" type="text"/>
        <input class="mark_btn" type="button" value="&hellip;" onclick="showSuppliar('SUPPLIER_CODE','SUPPLIER_ID','true')"/>
        <input class="normal_btn" type="button" value="清除" onclick="reset();"/>
        <input id="SUPPLIER_ID" name="SUPPLIER_ID" type="hidden" value="">
        <td class="table_query_2Col_label_6Letter">供应商名称：</td>
      <td class="table_query_2Col_input">
      	<input class="long_txt" id="SUPPLIER_NAME" name="SUPPLIER_NAME" value="" type="text"/>
     
        
        </td>
        </tr>
        <tr>
          <td class="table_query_2Col_label_5Letter">是否已出库： </td>
         <td nowrap>
          <select id="is_stock" name="is_stock"> 
          <option value="10041001">是</option>
             <option value="10041002" selected="selected">否</option>
          </select>
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryScanningStockList.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "供应商代码", dataIndex: 'supplierCode', align:'center'},
  				{header: "供应商名称", dataIndex: 'supplierName', align:'center'},
  				{header: "出库单号", dataIndex: 'stockNo', align:'center'},
  				{header: "是否已经出库", dataIndex: 'stockState', align:'center',renderer:getItemValue},
  				{header: "出库时间", dataIndex: 'stockDate', align:'center'},
  				{header: "出库总数",dataIndex: 'stockNumber',align:'center'},
  				{header: "操作", dataIndex: 'stockId',align:'center',renderer:myLink}
  				
  		      ];


   function myLink(value,meta,record){

	   var state=record.data.stockState;

	   if(state=='<%=Constant.IF_TYPE_YES%>'){
	   return String.format("<a href='#' onclick=isCheck13("+value+","+record.data.stockType+")>[明细]</a>"+"<a href='#' onclick=isCheck14("+value+")>[打印]</a>");
	   } else if (state=='<%=Constant.IF_TYPE_NO%>'){
		   return String.format("<a href='#' onclick=isCheck12("+value+")>[删除]</a>"+"<a href='#' onclick=isCheck1("+value+")>[生成]</a>"+"<a href='#' onclick=isCheck20("+value+")>[没有匹配]</a>");
		   }
	   
	   }

   function isCheck13(stockId,stockType){
	   
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/ScanningDetail.do?stockId="+stockId;
	   fm.method="post";
	   fm.submit();
	}

   function isCheck20(stockId,stockType){
		
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/notScanningDetail.do?stockId="+stockId;
	   fm.method="post";
	   fm.submit();
	}
   
   function isCheck1(stockId){

	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryScanningBulu.do?stockId="+stockId;
	   fm.method="post";
	   fm.submit();
	  
   }

   function isCheck12(stockId){
	   if (confirm('确定删除吗？')){    
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/ScanningDelete.do?stockId="+stockId;
	   fm.method="post";
	   fm.submit();
	   }
   }
	
   function isCheck14(stockId){
	   window.open("<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/ScanningPrint.do?stockId="+stockId);
	 
	   
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
		var Stock_type=document.getElementById("Stock_type").value;
		
		if(Stock_type==null||Stock_type==''){
			MyAlert('请选择出库类型');
				return;
			}
	   
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/addStockList.do?Stock_type="+Stock_type;
	   fm.method="post";
       fm.submit();
   }
</script>
</BODY>
</html>
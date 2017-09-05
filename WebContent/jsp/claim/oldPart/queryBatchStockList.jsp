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
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;不索赔旧件批量出库</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
    <TABLE class="table_query">
       <tr>
         <td class="table_query_2Col_label_5Letter">出库类型：</td>
        <td align="left" nowrap="nowrap">
				<script type="text/javascript">
				 genSelBoxExp("Stock_type",<%=Constant.Stock_type%>,"",true,"short_sel","","true",'<%=Constant.Stock_type_3%>,<%=Constant.Stock_type_4%>');
			    </script>
			</td>
         <td class="table_query_2Col_label_5Letter">出库时间：</td>
         <td align="left" >
          <input name="out_start_date" id="out_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="out_start_date,out_end_date" hasbtn="true" callFunction="showcalendar(event, 'out_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="out_end_date" id="out_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="out_start_date,out_end_date" hasbtn="true" callFunction="showcalendar(event, 'out_end_date', false);">
         </td>
         
          <td class="table_query_2Col_label_5Letter">出库单号： </td>
         <td nowrap>
          <input id="stock_no" name="stock_no" value="" type="text" class="middle_txt" datatype="1,is_null,30">
         </td>
      
         <td align="center" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="__extQuery__(1);">
           &nbsp;&nbsp;
           <input class="normal_btn" type="button" id="addButton" name="addButton" value="新增"  onClick="goToAdd();">
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryBatchStockList.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "出库单号", dataIndex: 'stockNo', align:'center'},
  				{header: "出库时间", dataIndex: 'stockDate', align:'center'},
  				{header: "入库开始时间", dataIndex: 'starTime', align:'center'},
  				{header: "入库结束时间", dataIndex: 'endTime', align:'center'},
  				{header: "出库总数",dataIndex: 'stockNumber',align:'center'},
  				{header: "出库类型", dataIndex: 'stockType', align:'center',renderer:getItemValue},
  				{header: "操作", dataIndex: 'stockId',align:'center',renderer:myLink}
  				
  		      ];


   function myLink(value,meta,record){
	   return String.format("<a href='#' onclick=isCheck13("+value+","+record.data.stockType+")>[明细]</a>"+"<a href='#' onclick=isCheck1("+value+")>[打印]</a>");

	   
	   }

   function isCheck13(stockId,stockType){
	
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryScanningDetail.do?stockId="+stockId;
	   fm.method="post";
	   fm.submit();
	}
   
   function isCheck1(id){
	   window.open("<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/DYClaimDetail.do?stockId="+id);
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
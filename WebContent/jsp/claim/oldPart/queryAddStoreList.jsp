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
         
          <td class="table_query_2Col_label_5Letter">出库单号： </td>
         <td nowrap>
          <input id="stock_no" name="stock_no" value="${stockNo}" type="text" class="middle_txt" readonly datatype="1,is_null,30" disabled >
         </td>
      
         <td class="table_query_2Col_label_5Letter">入库时间：</td>
         <td align="left" >
          <input name="out_start_date" id="out_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="out_start_date,out_end_date" hasbtn="true" callFunction="showcalendar(event, 'out_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="out_end_date" id="out_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="out_start_date,out_end_date" hasbtn="true" callFunction="showcalendar(event, 'out_end_date', false);"><font color="red">*</font>
         </td>
         
      
         <td align="center" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="sel();">
           &nbsp;&nbsp;
           <input class="normal_btn" type="button" id="addButton" name="addButton" value="返 回"  onClick="goToBack();">
                &nbsp;&nbsp;
           <input class="normal_btn" type="button" id="addButton" name="addButton" value="出 库"  onClick="goToStock();">
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

function sel(){
	var out_start_date=document.getElementById("out_start_date").value;
	var out_end_date=document.getElementById("out_end_date").value;
	if(out_start_date==null||out_start_date==''){
		MyAlert('请选择入库开始时间');
		return;
		}
	if(out_end_date==null||out_end_date==''){
		MyAlert('请选择入库结束时间');
		return;
		}
	__extQuery__(1);
	
}
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/addStockListQuery.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "配件名称", dataIndex: 'partName', align:'center'},
  				{header: "配件代码", dataIndex: 'partCode', align:'center'},
  				{header: "出库零件数",dataIndex: 'count',align:'center'}
  			
  				
  		      ];
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

   //出库
   function goToStock(){
	   var stock_no=document.getElementById("stock_no").value;
	   var out_start_date=document.getElementById("out_start_date").value;
	   var out_end_date=document.getElementById("out_end_date").value;
	   if(out_start_date==null||out_start_date==''){
			MyAlert('请选择入库开始时间');
			return;
			}
		if(out_end_date==null||out_end_date==''){
			MyAlert('请选择入库结束时间');
			return;
			}
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/notCliamStockInfo.do?stock_no="+stock_no+"&out_start_date="+out_start_date+"&out_end_date="+out_end_date;;
	   fm.method="post";
       fm.submit();
	   
	   }


   function goToBack(){
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryBatchStock.do";
	   fm.method="post";
       fm.submit();
	   }
</script>
</BODY>
</html>
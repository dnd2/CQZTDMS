<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<%@page import="com.infodms.dms.po.TmBusinessAreaPO,java.util.*" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件库存查询</title>
<% String contextPath = request.getContextPath(); 

List<TmBusinessAreaPO> list = (List<TmBusinessAreaPO>)request.getAttribute("yieldlyList");
%>

</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件库存查询</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
    <TABLE class="table_query">
       <tr>
			<td align="right" nowrap="nowrap">库房：</td>
			<%if((Integer)request.getAttribute("poseType")==10781011){%>
				<td align="left" >
				<script type="text/javascript">
				  genSelBoxExp("YIELDLY_TYPE",<%=Constant.PART_IS_CHANGHE%>,"<%=Constant.PART_IS_CHANGHE_02%>",false,"short_sel","","false",'<%=Constant.PART_IS_CHANGHE_01%>');
				 </script>
			</td>
			<%}else{%>
				<td align="left" >
				<script type="text/javascript">
				  genSelBoxExp("YIELDLY_TYPE",<%=Constant.PART_IS_CHANGHE%>,"<%=Constant.PART_IS_CHANGHE_01%>",false,"short_sel","","false",'<%=Constant.PART_IS_CHANGHE_02%>');
				 </script>
			</td>
			<%} %>
			
			<td class="table_query_3Col_label_5Letter">零件性质： </td>
	         <td nowrap="nowrap">
	         <script type="text/javascript">
				  genSelBoxExp("IS_MAIN_CODE",<%=Constant.RESPONS_NATURE_STATUS%>,"<%=Constant.RESPONS_NATURE_STATUS_01%>",false,"short_sel","","false",'');
				 </script>
	         </td>
       </tr>
       <tr>
      		 <td class="table_query_3Col_label_5Letter">供应商代码： </td>
	         <td nowrap="nowrap">
	          <input id="supply_code" name="supply_code" value="" type="text" class="middle_txt" maxlength="20">
	         </td>
	         <td class="table_query_3Col_label_5Letter">供应商名称： </td>
	         <td nowrap="nowrap">
	          <input id="supply_name" name="supply_name" value="" type="text" class="middle_txt" maxlength="20">
	         </td>
       </tr>
       <tr>
	         <td class="table_query_3Col_label_5Letter">配件代码：</td>
	         <td nowrap="nowrap">
	            <input id="part_code" name="part_code" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	         </td>
	         <td class="table_query_3Col_label_5Letter">配件名称： </td>
	         <td nowrap="nowrap">
	          <input id="part_name" name="part_name" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	         </td>
       </tr>
       <tr>
         <td align="center" nowrap="nowrap" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="__extQuery__(1);countTotals();">
           &nbsp;&nbsp;
           <input class="normal_btn"  type="button" id="qryButton" name="qryButton" value="导出"  onClick="to_excel();">
         </td>
       </tr>
       <tr>
       		<td align="right" nowrap="true" colspan="4">
       			<div id="totals" style="color: red"></div>
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/queryCurStoreList.json";
				
   var title = null;
   
   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "供应商代码", dataIndex: 'supply_code', align:'center'},
  				{header: "供应商名称", dataIndex: 'supply_name', align:'center'},
  				{header: "配件代码", dataIndex: 'part_code', align:'center'},
  				{header: "配件名称",dataIndex: 'part_name',align:'center'},
  				{header: "总入库数量",dataIndex: 'all_in_amount',align:'center'},
  				{header: "总出库数量",dataIndex: 'all_out_amount',align:'center'},
  				{header: "库存总数",dataIndex: 'all_amount',align:'center',renderer:myLink},
  				{header: "被调拨库存",dataIndex: 'all_amount_kc',align:'center'},
  				{header: "索赔金额",dataIndex: 'partPrice',align:'center'}
  		      ];
   function myLink(value,meta,record){
	   var temp = record.data.part_code;
	   if(temp==""||temp==null||temp=='null'){
		   return  value ;
		   }else{
   	return String.format( "<a href='#' onClick='claimDetail(\""+record.data.yieldly+"\",\""+record.data.supply_code+"\",\""+record.data.part_code+"\");'>["+value+"]</a>");
   }
   }
  
  function claimDetail(value,supCode,partCode){
	  var main = $('IS_MAIN_CODE').value;
   	  window.open("<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/claimDetail2.do?partCode="
				+ partCode+"&code="+supCode+"&areaId="+value+"&IS_MAIN_CODE="+main);
  }
   function doInit(){
	   loadcalendar();
   }
   //导出功能
   function to_excel(){
	   fm.action = "<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/toExcel.do";
	fm.submit();
   }
   
   function countTotals() {
	   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/queryCurStoreList01.json";
	   makeNomalFormCall(url,succRes,'fm','');
   }
   
   function succRes(json) {
	   var totals=json.totals;
	   var totals_kc=json.totals_kc;
	   var totalsMoney=json.totalsMoney;
	   $("totals").innerHTML= "总库存数:"+totals+" 被调拨存数:"+totals_kc+" 总库存索赔金额:"+totalsMoney;;
   }
</script>
</BODY>
</html>
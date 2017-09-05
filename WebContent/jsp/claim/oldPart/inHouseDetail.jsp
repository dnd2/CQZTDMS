<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<%@page import="com.infodms.dms.po.TmBusinessAreaPO,java.util.*" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件入库明细</title>
<% String contextPath = request.getContextPath(); 
%>

</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件入库明细</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
    <TABLE class="table_query">
       <tr>
			<td align="right" nowrap="nowrap">服务站名称：</td>
			<td align="left" >
				<input id="dealer_name" name="dealer_name" value="" type="text" class="middle_txt" maxlength="20">
			</td>
			
			
			<td class="table_query_3Col_label_5Letter">配件名称： </td>
	         <td nowrap="nowrap">
	         <input id="part_name" name="part_name" value="" type="text" class="middle_txt" maxlength="20">
			 </td>
       </tr>
       <tr>
      		 <td class="table_query_3Col_label_5Letter">服务站代码： </td>
	         <td nowrap="nowrap">
	          <input id="dealer_code" name="dealer_code" value="" type="text" class="middle_txt" maxlength="20">
	         </td>
	         <td class="table_query_3Col_label_5Letter">配件代码： </td>
	         <td nowrap="nowrap">
	          <input id="part_code" name="part_code" value="" type="text" class="middle_txt" maxlength="20">
	         </td>
       </tr>
       <tr>
	         <td class="table_query_3Col_label_5Letter">索赔单号：</td>
	         <td nowrap="nowrap">
	            <input id="claim_no" name="claim_no" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	         </td>
	         <td class="table_query_3Col_label_5Letter">VIN： </td>
	         <td nowrap="nowrap">
	          <input id="vin" name="vin" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	         </td>
       </tr>
       <tr>
       	<td class="table_query_3Col_label_5Letter">零件性质： </td>
	         <td nowrap="nowrap">
	         <script type="text/javascript">
				  genSelBoxExp("IS_MAIN_CODE",<%=Constant.RESPONS_NATURE_STATUS%>,"",true,"short_sel","","false",'');
				 </script>
	         </td>
	         	<td class="table_query_3Col_label_5Letter">入库状态： </td>
	         <td nowrap="nowrap">
	        	<select name="is_ok" id="is_ok" value="" class="short_sel">
	        	<option value="">--请选择--</option>
	        	<option value="1">合格</option>
	        	<option value="2">拒赔</option>
	        	</select>
	         </td>
       </tr>
        <tr>
	         <td class="table_query_3Col_label_5Letter">操作人：</td>
	         <td nowrap="nowrap">
	            <input id="in_by_name" name="in_by_name" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	         </td>
	         <td class="table_query_3Col_label_5Letter">入库时间： </td>
	        <td align="left" nowrap="true">
			<input name="in_start_date" type="text" class="short_time_txt" id="in_start_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'in_start_date', false);" />  	
             &nbsp;至&nbsp; <input name="in_end_date" type="text" class="short_time_txt" id="in_end_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'in_end_date', false);" /> 
		</td>	
       </tr>
       
        <tr>
	         <td class="table_query_3Col_label_5Letter">发动机号：</td>
	         <td nowrap="nowrap">
	            <input id="engine_no" name="engine_no" value="" type="text" class="middle_txt" >
	         </td>
			<td class="table_query_3Col_label_5Letter">供应商名称：</td>
	         <td nowrap="nowrap">
	            <input type="text" name="producer_name" id="producer_name" class="middle_txt"/>
	         </td>
       </tr>
       
       
       <tr>
         <td align="center" nowrap="nowrap" colspan="4">
           <input class="normal_btn" type="button" id="queryBtn" name="qryButton" value="查询"  onClick="__extQuery__(1);">
           &nbsp;&nbsp;
           <input class="normal_btn" type="button" id="queryBtn" name="qryButton" value="导出"  onClick="to_excel();">
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/inHouseDetailList.json";
				
   var title = null;
   
   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "入库序号", dataIndex: 'SIGN_NO', align:'center'},
  				{header: "服务站代码", dataIndex: 'DEALER_CODE', align:'center'},
  				{header: "服务站名称", dataIndex: 'DEALER_NAME', align:'center'},
  				{header: "索赔单号",dataIndex: 'CLAIM_NO',align:'center',renderer:myLink},
  				{header: "数量",dataIndex: 'SIGN_AMOUNT',align:'center'},
  				{header: "扣件原因", dataIndex: 'DEDUCT_REMARK', align:'center',renderer:getItemValue},
  				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
  				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
  				{header: "供应商代码",dataIndex: 'PRODUCER_CODE',align:'center'},
  				{header: "供应商名称",dataIndex: 'PRODUCER_NAME',align:'center'},
  				{header: "故障描述", dataIndex: 'REMARK', align:'center'},
  				{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
  				{header: "VIN", dataIndex: 'VIN', align:'center'},
  				{header: "审核人",dataIndex: 'IN_WARHOUSE_NAME',align:'center'},
  				{header: "操作人",dataIndex: 'NAME',align:'center'},
  				{header: "入库时间",dataIndex: 'IN_TIME',align:'center'},
  				{header: "责任性质", dataIndex: 'IS_MAIN_CODE', align:'center',renderer:getItemValue},
  				{header: "索赔类型", dataIndex: 'CLAIM_TYPE', align:'center',renderer:getItemValue},
  				{header: "回运单号",dataIndex: 'RETURN_NO',align:'center'},
  				{header: "旧件编号",dataIndex: 'BARCODE_NO',align:'center'}
  		      ];
   function myLink(value,meta,record){
   	return String.format( "<a href='#' onClick='claimDetail(\""+record.data.CLAIM_ID+"\",\""+record.data.RO_NO+"\");'>["+record.data.CLAIM_NO+"]");
   }
  
   function claimDetail(id,ro_no){
         var url = "<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?roNo="+ro_no+"&ID="+ id;
         OpenHtmlWindow(url,900,500);
	 }
   function doInit(){
	   loadcalendar();
   }
   //导出功能
   function to_excel(){
	   fm.action = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/toExcel.do";
	fm.submit();
   }
</script>
</BODY>
</html>
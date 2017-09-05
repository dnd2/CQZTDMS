<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发运查询</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 销售管理&gt;整车物流管理&gt;发运管理&gt;运单发票查询</div>
  <form name='fm' id='fm' method="post">
  <TABLE class="table_query">
       <tr>            
        <td style="color: #252525;width: 115px;text-align: right">发运单号：</td>            
        <td>
			<input  class="middle_txt" id="BILL_NO" datatype="1,is_digit_letter,30" maxlength="30" name="BILL_NO" type="text" maxlength="20"  />
        </td>
        <td class="table_query_3Col_label_6Letter">VIN：</td>
        <td><textarea id="VIN" name="VIN" cols="20" rows="2" ></textarea></td>
        <td class="table_query_3Col_label_6Letter">&nbsp;</td>
        <td>&nbsp;</td>   
       </tr> 
       <tr> <td style="color: #252525;width: 115px;text-align: right" >是否已录入发票：
     	 </td> <td><select class="u-select" id="INVOINCE_FLAG" name="INVOINCE_FLAG" >
	  			<option value="">-请选择-</option>
	  			<option value="yes">是</option>
	  			<option value="no">否</option>
	  		</select></td>
	  		
	  		<td><div class="right">经销商：</div></td>
			<td >
     				<input name="dealerName" readonly="readonly" type="text" maxlength="20"  id="dealer_Name" class="middle_txt" value="" />
     				<input name="dealerName" type="hidden" id="dealer_code" class="middle_txt" value="" />
                   <input name="dlbtn" type="button" class="mini_btn" onclick="showOrgDealer('dealer_code','dearler_Id','false', '', 'true','','<%=Constant.DEALER_TYPE_DVS %>,<%=Constant.DEALER_TYPE_DP %>','dealer_Name');" value="..." />
                   <input type="button" class="normal_btn" onclick="txtClr('dearler_Id','dealer_Name');" value="清 空" id="clrBtn" />
   					<input name="dearlerId" type="hidden" id="dearler_Id" class="middle_txt" value="" />
   			</td>
	  		
	   </tr> 
       <tr>
       
       <td colspan="4" class="table_query_4Col_input" style="text-align: center">
        	<input   id="queryBtn" class="normal_btn" type="button" name="button1"  value="查询"  onclick="__extQuery__(1)"/>
        </td>
       </tr>    
 	</table>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  </body>
</html>
<script type="text/javascript" >

var myPage;
	var url = "<%=request.getContextPath()%>/sales/storage/sendmanage/WaybillIn/waybillInQuery.json";
	var title = null;
	//声明一个表格，header为表头名称，sortable为排序，dataIndex是对应数据库查出来的数据，如果是单表查询，则与PO的属性一致，如果是多表SQL查询，则必须用大写，名称与sql语句对应的别名一致
	var columns = [
				{header: "产地",dataIndex: 'AREA_NAME',align:'center'},
				{header: "发运单号",dataIndex: 'BILL_NO',align:'center'},
				{header: "发票号",dataIndex: 'INVOICE_NO',align:'center'},
				{header: "VIN",dataIndex: 'VIN',align:'center'},	
				{header: "车系",dataIndex: 'SERIES_NAME',align:'center'},	
				{header: "车型",dataIndex: 'MODEL_NAME',align:'center'},	
				{header: "配置",dataIndex: 'MODEL_NAME',align:'center'},	
				{header: "颜色",dataIndex: 'MODEL_NAME',align:'center'},	
				{header: "物料代码",dataIndex: 'MATERIAL_NAME',align:'center'},
				{header: "发运时间",dataIndex: 'BILL_CRT_DATE',align:'center'}
		      ];
//设置超链接  begin      
	
	//修改的超链接设置
	function myLink(value,meta,record){
	    return String.format(
         "<a href='javascript:void(0);' onclick = 'addInvoiceNo("+value+")' >[录入运单发票]</a>");
	}
	
	function addInvoiceNo(value)
	{
		document.getElementById('fm').action = "<%=contextPath%>/sales/storage/sendmanage/WaybillIn/initAddInvoiceNo.do?VEHICLE_ID="+value;
	   	document.getElementById('fm').submit();
	}
	
	
	function doInit(){
		__extQuery__(1); //加载时同时加载表格数据s
	}


	function txtClr(valueId1,valueId2,valueId3) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
		document.getElementById(valueId3).value = '' ;
	}

</script>  


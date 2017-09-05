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
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;特殊单查询</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
    <TABLE class="table_query">
       <tr>
         <td style="color: #252525;width: 115px;text-align: right">供应商简称：</td>
         <td><input id="supply_name" name="supply_name" value="" type="text" class="middle_txt" datatype="1,is_null,30" ></td>
         <td style="color: #252525;width: 115px;text-align: right">特殊单编号：</td>
         <td align="left" ><input id="spefee_no" name="spefee_no" value="" type="text" class="middle_txt" datatype="1,is_null,30" >
            </td>
       </tr>
       <tr>
        <td style="color: #252525;width: 115px;text-align: right">供应商编码： </td>
         <td nowrap>
          <input id="SUPPLY_CODE" name="SUPPLY_CODE" value="" type="text" class="middle_txt" datatype="1,is_null,30">
         </td>
          <td style="color: #252525;width: 115px;text-align: right">特殊单状态： </td>
         <td >
        	  <script type="text/javascript">
			genSelBoxExp("status",<%=Constant.SPEFEE_STATUS%>,"",true,"short_sel","","false",'<%=Constant.SPEFEE_STATUS_01%>');
 			</script>
         </td>
       </tr>
       <tr>
         <td align="center" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="__extQuery__(1);">
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/querySpefeeList2.json";
				
   var title = null;

   var columns = [
   				{header: "序号",align:'center',renderer:getIndex},
   				{header: "操作", dataIndex: 'id', align:'center',renderer:myLink},
  				{header: "特殊费用单号", dataIndex: 'feeNo', align:'center',renderer:mySelect},
  				{header: "VIN", dataIndex: 'vin', align:'center'},
  				{header: "车型", dataIndex: 'modelCode', align:'center'},
  				{header: "特殊单类型",dataIndex: 'feeType',align:'center',renderer:getItemValue},
  				{header: "特殊单状态", dataIndex: 'status', align:'center',renderer:getItemValue},
  				{header: "供应商代码", dataIndex: 'supplyCode',align:'center'},
  				{header: "供应商名称", dataIndex: 'supplyName', align:'center'},
  				{header: "配件代码", dataIndex: 'partCode',align:'center'},
  				{header: "配件名称", dataIndex: 'partName', align:'center'},
  				{header: "索赔价", dataIndex: 'claimPrice', align:'center'}
  		      ];
  		      __extQuery__(1);
  	function myLink(value,meta,record){
  	var  str="<a href='#' onClick='addNotice(\""+value+"\");'>[生成通知单]</a>";
  		return String.format( str);
	}
	
function mySelect(value,meta,record)
	{
  		return String.format("<a href=\"#\" onclick='sel(\""+record.data.id+"\",\""+ record.data.feeType +"\")'>["+ value +"]</a>");
	}
//详细页面
	function sel(val1,val2)
	{
		OpenHtmlWindow('<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/speciaExpensesInfo.do?id='+val1+"&feeType="+val2,800,500);
	}
	
	function addNotice(value){
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/addNotice.do?id="+value;
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
</script>
</BODY>
</html>
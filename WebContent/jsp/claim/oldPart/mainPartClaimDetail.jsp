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
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;主因件索赔明细</div>
  <form id="fm" name="fm">
  <input type="hidden" name="yieldly" id="yieldly" value="${yieldly }" />
  <input type="hidden" name="supply_code" id="supply_code" value="${supply_code }" />
    <table class="table_query">
          <tr>
         <td style="color: #252525;width: 115px;text-align: right">配件名称：</td>
         <td><input id="part_name" name="part_name" value="" type="text" class="middle_txt" datatype="1,is_null,30" ></td>
          <td style="color: #252525;width: 115px;text-align: right">是否打印： </td>
         <td nowrap>
          <script type="text/javascript">
				            genSelBoxExp("IS_PRINT",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
				        </script>
		
         </td>
       </tr>
       <tr>
         <td align="center" nowrap="nowrap" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="__extQuery__(1);">
           <input type="button" onclick="history.back();" class="normal_btn" style="width=8%" value="返回"/>
         </td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<form name="form1" style="display:none">
  <table id="bt" class="table_list">
	  <tr>
        <th height="12" align="center">
         <input type="button" onclick="preChecked();" id="save_btn" class="normal_btn" style="width=8%" value="出库"/>
         &nbsp;&nbsp;
         <input type="button" onclick="history.back();" class="normal_btn" style="width=8%" value="返回"/></th>
	  </tr>
  </table>
</form>
<br>
<script type="text/javascript">

   
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/mainPartClaimDetail2.json";
				
   var title = null;
   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "供应商代码", dataIndex: 'supply_code', align:'center'},
  				{header: "供应商名称",dataIndex: 'supply_name',align:'center'},
  				{header: "索赔单号",dataIndex: 'claim_no',align:'center'},
  				{header: "VIN",dataIndex: 'vin',align:'center'},
  				{header: "配件代码", dataIndex: 'part_code', align:'center'},
  				{header: "配件名称",dataIndex: 'part_name',align:'center'},
  				{header: "是否打印",dataIndex: 'is_print',align:'center',renderer:getItemValue},
  				{header: "操作",dataIndex: 'claim_no',align:'center',renderer:myLink}
  		      ];
 __extQuery__(1);
 function myLink(value,metaDate,record){
    	return String.format( "<a href='#' onClick='printClaim(\""+value+"\",\""+record.data.wr_labourcode+"\");'>[打印]</a>");
   }
   
   function printClaim(claimNo,code){
   var yieldly = $('yieldly').value;
   window.open('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/mainPartClaimPrint.do?no='+claimNo+'&labourCode='+code+'&yieldly='+yieldly,"主因件索赔单", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	__extQuery__(1);
}
</script>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔件回运清单管理</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onload="">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件运输查询</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
    <table class="table_query">
    	<tr>
       	 <td align="right" nowrap="nowrap">商家名称：</td>
         <td><input id="DEALER_NAME" name="DEALER_NAME" value="" type="text" maxlength="20" class="middle_txt"></td>
         <td align="right" nowrap="nowrap">商家代码：</td>
         <td><input id="DEALER_CODE" name="DEALER_CODE" value="" type="text" maxlength="20" class="middle_txt"></td>
       </tr>
       <tr>
         <td align="right" nowrap="nowrap">运输单号：</td>
         <td><input id="TRANSPORT_NO" name="TRANSPORT_NO" value="" type="text" maxlength="20" class="middle_txt" datatype="1,is_null,50" callFunction="javascript:MyAlert();"></td>
		 <td align="right" nowrap="nowrap">运输单状态：</td>
         <td>
         		<script type="text/javascript">
         		 genSelBoxExp("TRANSPORT_STATUS",<%=Constant.SP_JJ_TRANSPORT_STATUS%>,"",true,"short_sel","","false",'<%=Constant.SP_JJ_TRANSPORT_STATUS_01%>');
		        </script> 
         </td>
       </tr>
       <tr>
       	 <td align="right" nowrap="nowrap">审核结果：</td>
         <td>
         	<select id="STATUS" name="STATUS" class="short_sel">
         		<option value="">-请选择-</option>
         		<option value="1">同意</option>
         		<option value="0">不同意</option>
         		<option value="3">审核中</option>
         	</select>	
         </td>
       </tr>
       
       
       <tr>
         <td align="center" colspan="4"><input class="normal_btn" type="button" name="qryButton" value="查询"  onClick="__extQuery__(1);">
           </td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form> 
<br>
<script type="text/javascript">
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartTransportManager/queryOldpartTransportDetail.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'TRANSPORT_ID',renderer:operateLink,align:'center'},
  				{header: "服务站代码",dataIndex: 'DEALER_CODE',align:'center'},
  				{header: "服务站名称",dataIndex: 'DEALER_NAME',align:'center'},//return_no
  				{header: "申请单号",dataIndex: 'TRANSPORT_NO',align:'center'},
  				{header: "物流公司",dataIndex: 'TRANSPORT_NAME',align:'center'},
  				{header: "审核结果",dataIndex: 'STATUS',align:'center'},
  				{header: "申请单状态",dataIndex: 'TRANSPORT_STATUS',align:'center',renderer:getItemValue},
  				{header: "上报人", dataIndex: 'REPORT_NAME', align:'center'},
  				{header: "上报时间", dataIndex: 'REPORT_DATE', align:'center'},
  				{header: "审核人", dataIndex: 'CHECK_NAME', align:'center'},
  				{header: "审核时间", dataIndex: 'CHECK_DATE', align:'center'}
  				//{header: "备注", dataIndex: 'REMARK', align:'center'} 
  		      ];
  		     
   __extQuery__(1);
 //超链接设置
   function operateLink(value,meta,record){
	   var width=800;
	   var height=500;
	   var screenW = window.screen.width-30;	
	   var screenH = document.viewport.getHeight();
	   if(screenW!=null && screenW!='undefined')
		   width = screenW;
	   if(screenH!=null && screenH!='undefined')
		   height = screenH;
		var  link = "<a href='#' onclick=\"checkTransportDetail("+value+",2)\">[查看]</a>";
		return String.format(
				link
			   );
   }
 	//审核查看
   function checkTransportDetail(transportId,viewOrCheck){
	   var i_url="<%=contextPath %>/claim/oldPart/ClaimOldPartTransportManager/oemTransportCheck.do?TRANSPORT_ID="+transportId+"&VIEW_OR_CHECK="+viewOrCheck;
	   location.href=i_url;
   }
</script>
</body>
</html>
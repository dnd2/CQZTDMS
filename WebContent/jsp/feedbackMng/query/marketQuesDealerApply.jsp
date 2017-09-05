<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>市场问题处理工单查询</title>
<% String contextPath = request.getContextPath(); %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 信息反馈管理&gt;信息反馈审批 &gt;市场问题处理工单查询</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
    <TABLE class="table_query">
       <tr>
         <td width="7%" align="right" nowrap>工单号：</td>
         <td colspan="6"><input name="query_order_no" value="" type="text" class="middle_txt" callFunction="javascript:MyAlert();"></td>
         <td width="9%" align="right" nowrap>车辆识别码(VIN)：</td>
         <td colspan="2" align="left" ><input type="text" name="query_vin"  value="" class="middle_txt"/> </td>
       </tr>
       <tr>
         <td width="9%" align="right" nowrap>工单状态：</td>
         <td nowrap="nowrap"  colspan="2">
           <script type="text/javascript">
		      genSelBoxExp("status",<%=Constant.MARKET_BACK_STATUS_TYPE%>,"",true,"short_sel","onchange='selectAction()'","false",'<%=Constant.MARKET_BACK_STATUS_UNREPORT%>');
           </script>
         </td>
       </tr>
       <tr>
         <td width="7%" align="right" nowrap>投诉类別：</td>
         <td colspan="6">
            <select id="comp_type" name="comp_type" class="short_sel">
               <option value="">-请选择-</option>
               <option value="F">服务</option>
               <option value="C">产品质量</option>
               <option value="B">备件</option>
            </select>
         </td>
         <td width="9%" align="right" nowrap>信息类别：</td>
         <td colspan="2" align="left" ><script type="text/javascript">
	         genSelBoxExp("info_type",<%=Constant.MARKET_ORDER_INFO_TYPE%>,"",true,"short_sel","","true",'');
	     </script></td>
       </tr>
       <tr>
         <td width="7%" align="right" nowrap >提报时间： </td>
         <td colspan="6" nowrap>
          <input name="query_start_date" id="query_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="query_start_date,query_end_date" hasbtn="true" callFunction="showcalendar(event, 'query_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="query_end_date" id="query_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="query_start_date,query_end_date" hasbtn="true" callFunction="showcalendar(event, 'query_end_date', false);"></td>
         <td align="right">车系：</td>
         <td><script type="text/javascript">
              var seriesList=document.getElementById("seriesList").value;
    	      var str="";
    	      str += "<select id='vehicleSeriesList' name='vehicleSeriesList'>";
    	      str+=seriesList;
    		  str += "</select>";
    		  document.write(str);
	        </script></td>
         <td width="29%" align="right" >			</td>
       </tr>
       <tr>
         <td align="right" nowrap >&nbsp;</td>
         <td colspan="6" nowrap>&nbsp;</td>
         <td align="center" nowrap><input class="normal_btn" type="BUTTON" name="button1" value="查询"  onClick="__extQuery__(1);"></td>
         <td>&nbsp;</td>
         <td align="right" ></td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form> 
<!-- 查询条件 end -->
<br>
<script type="text/javascript">
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/feedbackmng/query/MarketQuestionOrderQueryCarManager/queryMarketOrderInfo.json";
				
   var title = null;

   var columns = [
  				{id:'action',header: "工单号",sortable: false,dataIndex: 'orderId',renderer:orderDetail ,align:'center'},
  				{header: "车辆识别码(VIN)", dataIndex: 'vin', align:'center'},
  				{header: "车系", dataIndex: 'group_name', align:'center'},
  				{header: "投诉类别", dataIndex: 'compType', align:'center',renderer:exportCHN},
  				{header: "信息类型", dataIndex: 'infoType', align:'center',renderer:getItemValue},
  				{header: "提报日期", dataIndex: 'orderDate', align:'center'},
  				{header: "工单状态", dataIndex: 'code_desc', align:'center'}
  		      ];
   //前台页面解释投诉类型
   function exportCHN(value,metaDate,record){
	   var strEn=record.data.compType;
	   var strCHN=strEn.replace("F","服务").replace("C","产品质量").replace("B","备件");
	   return String.format(strCHN);
   }
   //全选checkbox
   function myCheckBox(value,metaDate,record){
	  return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
   }
   function doInit(){
	   loadcalendar();
	}
   //按工单号明细查询
   function orderDetail(value){
     	//return String.format("<a href=\"#\" onclick='sel(\""+value+"\")'>["+ value +"]</a>");
     	return String.format("<a target=\"_blank\" href=\"<%=contextPath%>/feedbackmng/query/MarketQuestionOrderQueryCarManager/queryOrderDetailInfo.do?orderId="+value+"\">["+ value +"]</a>");
   }
   	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/feedbackmng/query/MarketQuestionOrderQueryCarManager/queryOrderDetailInfo.do?orderId='+value,800,500);
	}
</script>
</BODY>
</html>
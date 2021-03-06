<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务资料审批表查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理&gt;信息反馈查询&gt;服务资料审批表查询</div>

  <form method="post" name="fm" id="fm">
   <!-- 查询条件 begin -->
  <table class="table_query">
 		<tr>
            <td width="10%" align="right" nowrap>工单号：</td>
            <td >
            	<input name="orderId" id="orderId" type="text" size="18"  class="middle_txt" value="" >
            </td>
             <td width="10%" align="right" nowrap > 经销商代码：</td>
	        <td colspan="6" nowrap>
			<textarea rows="2" cols="30" id="dealerCode" name="dealerCode"></textarea>
			     <input name="button1" type="button" class="mini_btn" style="cursor: pointer;"  onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
			     <input name="button2" type="button" class="normal_btn" onclick="clr();" value="清除"/>
	        </td> 
          </tr>
          <tr>
            <td width="10%" align="right" nowrap>经销商名称：</td>
            <td><input name="DEALER_NAME" id="DEALER_NAME" value="" type="text" class="middle_txt" datatype="1,is_digit_letter_cn" /></td>
            
            <td width="10%" align="right" nowrap>工单状态：</td>
            <td  nowrap="nowrap"><label>
              <script type="text/javascript">
 				 genSelBoxExp("status",<%=Constant.SERVICEINFO_VIP_STATUS_TYPE%>,"",true,"short_sel","onchange='selectAction()'","false",'<%=Constant.SERVICEINFO_VIP_STATUS_UNREPORT%>');
			  </script>
            </label></td>
          </tr>
          <tr>
            <td width="10%" align="right" nowrap>提报时间： </td>
          <td class="" nowrap >
          <input name="beginTime" id="beginTime" value="" type="text" class="short_txt" datatype="1,is_date,10" group="beginTime,endTime" hasbtn="true" callFunction="showcalendar(event, 'beginTime', false);">
         	&nbsp;至&nbsp;
          <input name="endTime" id="endTime" value="" type="text" class="short_txt" datatype="1,is_date,10" group="beginTime,endTime" hasbtn="true" callFunction="showcalendar(event, 'endTime', false);">
          </td>  
            <td align="right" nowrap="nowrap" ><!--  邮寄方式：--></td>
            <td colspan="6" nowrap="nowrap">
              <input type="hidden" value="<%=Constant.ORDER_SI_MAIL_TYPE_02 %>" name="mailType"/>
             <script type="text/javascript">
                //genSelBoxExp("mailType",<%=Constant.ORDER_SI_MAIL_TYPE%>,"",true,"short_sel","","false",'');
             </script>
            </td>
          </tr>                   
          <tr>
            <td width="7%" align="right" nowrap >&nbsp;</td>
            <td nowrap>&nbsp;</td>
            <td width="10%" align="left" nowrap><input class="normal_btn" type="BUTTON" name="button1" value="查询"  onClick="__extQuery__(1);" /></td>
            <td width="19%">&nbsp;</td>
            <td width="24%" align="right" >&nbsp;</td>
          </tr>
  </table>
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>

<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
	var url = "<%=contextPath%>/feedbackmng/query/ServiceInfoQueryManager/queryServiceInfo.json";
				
	var title = null;

	var columns = [
				{header: "申请单位代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "申请单位名称", dataIndex: 'DEALER_NAME', align:'center'},
				{id:'action',header: "工单号",sortable: false,dataIndex: 'ORDER_ID',renderer:mySelect ,align:'center'},
				{header: "邮寄方式", dataIndex: 'MAIL_TYPE', align:'center',renderer:getItemValue},
				{header: "提报时间", dataIndex: 'APP_DATE', align:'center'},
				{header: "工单状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue}
		      ];
		      
//设置超链接  begin      
	
	//设置超链接
	function mySelect(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.ORDER_ID+"\")'>["+ value +"]</a>");
	}
	
	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/feedbackmng/apply/ServiceInfoApplyManager/serviceInfoDetailView.do?orderId='+value,800,500);
	}
//清除方法
 function clr() {
	document.getElementById('dealerCode').value = "";
  }
//设置超链接 end
	
</script>
<!--页面列表 end -->

</body>
</html>
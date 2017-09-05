<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务资料售后服务部审批</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
   //初始化时间控件
	function doInit(){
   		loadcalendar();  
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理&gt;信息反馈审批&gt;服务资料售后服务部审批</div>

  <form method="post" name="fm" id="fm">
   <!-- 查询条件 begin -->
  <table class="table_query">
 		<tr>
            <td align="right" nowrap>工单号：</td>
            <td >
            	<input name="orderId" id="orderId" type="text" size="18"  class="middle_txt" value="" >
            </td>
            <td align="right" nowrap >提报时间： </td>
            <td colspan="2" nowrap>
            	<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
                &nbsp;至&nbsp;
				<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
			</td>
          </tr>
          <tr>
            <td align="right" nowrap><span class="zi">经销商代码：</td>
	        <td>
			<textarea rows="2" cols="30" id="dealerCode" name="dealerCode"></textarea>
			     <input name="button1" type="button" class="mini_btn" style="cursor: pointer;"  onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
			     <input name="button2" type="button" class="normal_btn" onclick="clr();" value="清除"/>
	        </td>  
	       </tr>
	       <tr>
            <td align="right" nowrap="nowrap" >邮寄方式：</td>
            <td colspan="2" nowrap="nowrap">
             <script type="text/javascript">
                genSelBoxExp("mailType",<%=Constant.ORDER_SI_MAIL_TYPE%>,"",true,"short_sel","","false",'');
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
	var url = "<%=contextPath%>/feedbackmng/approve/ServiceInfoApproveManager/queryServiceInfoApprove.json";
				
	var title = null;

	var columns = [
				{header: "申请单位代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "申请单位名称", dataIndex: 'DEALER_NAME', align:'center'},
				{id:'action',header: "工单号",sortable: false,dataIndex: 'ORDER_ID',renderer:mySelect ,align:'center'},
				{header: "邮寄方式", dataIndex: 'MAIL_TYPE', align:'center',renderer:getItemValue},
				{header: "提报时间", dataIndex: 'APP_DATE', align:'center'},
				{header: "工单状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ORDER_ID',renderer:myLink ,align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//审批的超链接
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='serviceInfoApproveEdit(\""+ value +"\")'>[审批]</a>");
	}
	
	//审批的超链接设置
	function serviceInfoApproveEdit(value){
		fm.action = '<%=contextPath%>/feedbackmng/approve/ServiceInfoApproveManager/serviceInfoApprove.do?orderId=' + value;
	 	fm.submit();
	}
	
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
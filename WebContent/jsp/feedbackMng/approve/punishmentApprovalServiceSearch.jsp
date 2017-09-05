<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%String contextPath = request.getContextPath();%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script>
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
<title>奖惩审批表售后服务部审核</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：信息反馈管理&gt;信息反馈审批&gt;奖惩审批表售后服务部审核</div>
<form method="post" name="fm" id="fm">
<table align="center" width="95%" class="table_query">
	<tr>
		<td width="7%" align="right" nowrap="nowrap">工单号：</td>
		<td colspan="6">
			<input name="orderId" id="orderId" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,18" />
		</td>
		<td width="13%" align="right" nowrap="nowrap">经销商代码：</td>
        <td>
		<textarea rows="2" cols="30" id="dealerCode" name="dealerCode"></textarea>
		     <input name="button1" type="button" class="mini_btn" style="cursor: pointer;"  onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
		     <input name="button2" type="button" class="normal_btn" onclick="clr();" value="清除"/>
        </td>  
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">经销商名称：</td>
		<td colspan="6" nowrap="nowrap">
			<input class="middle_txt" id="dealerName" style="cursor: pointer;" name="dealerName" type="text" />
		</td>
		<td align="right" nowrap="nowrap">类型：</td>
		<td>
			<script type="text/javascript">
	   					genSelBoxExp("rewardType",<%=Constant.PUNISHMENT%>,"",true,"short_sel","","true",'');
	  		</script>
  		</td>
		<td align="right">&nbsp;</td>
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">申请单位：</td>
		<td colspan="6" nowrap="nowrap">
			<input class="middle_txt" id="name" style="cursor: pointer;" name="name" type="text" />
			<input id="userId" name="userId" type="hidden" /> 
			<input id="phone" name="phone" type="hidden" /> 
		</td>
		<td align="right" nowrap="nowrap">提报时间：</td>
		<td>
			<div align="left">
				<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);" />
				&nbsp;至&nbsp; 
				<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);" />
			</div>
		</td>
		<td align="right">&nbsp;</td>
	</tr>
	<tr>
		<td align="right" nowrap>&nbsp;</td>
		<td colspan="6" nowrap>&nbsp;</td>
		<td align="left" nowrap>
			<input class="normal_btn" type="button" name="button" value="查询" onclick="__extQuery__(1);" />
		</td>
		<td>&nbsp;</td>
		<td align="right">&nbsp;</td>
	</tr>
</table>
</form>
<!--分页 begin -->
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</body>
</html>

<!--页面列表 begin -->
<script type="text/javascript">
	var myPage;
//查询路径
	var url = "<%=contextPath%>/feedbackmng/approve/PunishmentApproval/punishmentApprovalQuery.json";
				
	var title = null;

	var columns = [
				//{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				//{header: "经销商心名称",dataIndex: 'DEALER_NAME' ,align:'center'},
				{id:'action',header: "工单号",sortable: false,dataIndex: 'ORDER_ID',renderer:mySelect ,align:'center'},
				{header: "申请单位",dataIndex: 'LINK_MAN' ,align:'center'},
				{header: "类型",dataIndex: 'REWARD_TYPE' ,align:'center',renderer:getItemValue},
				{header: "提报时间",dataIndex: 'REWARD_DATE' ,align:'center'},
				{header: "工单状态",dataIndex: 'REWARD_STATUS' ,align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ORDER_ID',renderer:punishmentApprovalUpdateInit ,align:'center'}
		      ];
     //设置超链接  begin      
	//审核的超链接设置
	function punishmentApprovalUpdateInit(value,meta,record){
		var pass='pass';//表示审核通过
	    return String.format(
	    		"<a href=\"<%=contextPath%>/feedbackmng/approve/PunishmentApproval/getOrderIdInfo.do?orderId="
				+ value +"&Approval=Approval\">[审核]</a>")
       
	}
	//设置超链接
	function mySelect(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.ORDER_ID+"\")'>["+ value +"]</a>");
	}
	
	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/feedbackmng/approve/PunishmentApproval/getOrderIdInfo.do?orderId='+value,800,500);
	}
	//清除方法
 function clr() {
	document.getElementById('dealerCode').value = "";
  }
</script>
<!--页面列表 end -->
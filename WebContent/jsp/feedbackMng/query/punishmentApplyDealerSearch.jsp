<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>奖惩审批表查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
function doInit()
	{
	   loadcalendar();
	}
</script>
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理 &gt; 信息反馈查询 &gt;奖惩审批表查询</div>
    <!-- 查询条件 begin -->
  <form method="post" name ="fm" id="fm" >
    <table align=center width="95%" class="table_query" >
          <tr>
            <td width="7%" align="right" nowrap>工单号：</td>
            <td colspan="6"><input name="orderId" id="orderId" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,18" /></td>
             <td align="right" nowrap >申请单位：</td>
            <td colspan="6" nowrap>
                <input class="middle_txt" id="name" style="cursor: pointer;" name="name" type="text"/>
            	<input id="userId" name="userId" type="hidden"/>
            	<input id="phone" name="phone" type="hidden"/>
			    <input class="mark_btn" type="button" value="&hellip;" onclick="showApply('<%=contextPath %>');"/>
            </td>
          </tr>
          <tr>
            <td width="7%" align="right" nowrap>类型：</td>
            <td colspan="6">
             <script type="text/javascript">
   					genSelBoxExp("rewardType",<%=Constant.PUNISHMENT%>,"",true,"short_sel","","true",'');
  				</script>
               </td>
             <td align="right" nowrap >提报时间：</td>
            <td colspan="6" nowrap>
               <div align="left">
            		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
            	</div>
            </td>
          </tr>
		  <tr>
            <td align="right" nowrap >&nbsp;</td>
            <td colspan="6" nowrap>&nbsp;</td>
            <td align="left" nowrap>
            <input class="normal_btn" type="button" name="button" value="查询"  onclick="__extQuery__(1);"/>
            </td>
            <td>&nbsp;</td>
            <td align="right" >&nbsp;</td>
          </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form> 
<br>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
	var url = "<%=contextPath%>/feedbackmng/query/PunishmentApplyDealerMantain/punishmentApplyDealerMantainQuery.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "工单号",sortable: false,dataIndex: 'ORDER_ID',renderer:mySelect ,align:'center'},
				{header: "申请单位",dataIndex: 'LINK_MAN' ,align:'center'},
				{header: "类型",dataIndex: 'REWARD_TYPE' ,align:'center',renderer:getItemValue},
				{header: "提报时间",dataIndex: 'REWARD_DATE' ,align:'center'},
				{header: "工单状态",dataIndex: 'REWARD_STATUS' ,align:'center',renderer:getItemValue}
		      ];
     //设置超链接  begin      
	
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
	}
	
	//设置超链接
	function mySelect(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.ORDER_ID+"\")'>["+ value +"]</a>");
	}
	
	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/feedbackmng/query/PunishmentApplyDealerMantain/getOrderIdInfo.do?orderId='+value,800,500);
	}
	function showButton(){//显示上报、删除按钮
		document.getElementById("bt").style.visibility = "visible";
	}
</script>
<!--页面列表 end -->
</body>
</html>
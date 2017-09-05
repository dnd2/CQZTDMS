<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>特殊费用重新审核</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}

</script>
</head>
<body>

<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;特殊费用管理&gt;特殊费用重新审核</div>
  
  <form method="post" name="fm" id="fm">
  
  <!-- 车系列表 -->
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
  
   <!-- 查询条件 begin -->
    <table class="table_query" >
		<tr>
			<td align="right" nowrap="true">经销商代码：</td>
			<td align="left" nowrap="true">
				<input class="long_txt" id="dealerCode"  name="dealerCode" type="text"/>
	            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
	            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput('dealerCode');" value="清除"/>
			</td>
			<td align="right" nowrap="true">&nbsp;</td>
			<td align="left" nowrap="true">&nbsp;</td>
		</tr>
          <tr>
            <td width="7%" align="right" nowrap>费用编号：</td>
            <td colspan="6">
            	<input name="feeNo" id="feeNo" type="text" size="18"  class="middle_txt" value="" >
            </td>
			<td align="right">生产基地：</td>
			<td align="left">
				<script type="text/javascript">
				genSelBoxContainStr("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'<%=CommonUtils.checkNull(request.getAttribute("yieldly"))%>');
		    	</script>
			</td>
          </tr>                  
          <tr>
            <td align="right" nowrap="nowrap" >审核时间：</td>
            <td colspan="6" nowrap="nowrap">
            	<div align="left">
            		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            	</div>
            </td>
            <td align="right" nowrap="nowrap" >费用类型：</td>
            <td colspan="6" nowrap="nowrap">
              	<script type="text/javascript">
 					 genSelBoxExp("feeType",<%=Constant.FEE_TYPE%>,"",true,"short_sel","","false",'');
			  	</script>
            </td>
          </tr>
          <tr>
            <td align="center" colspan="14">
            	<input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onClick="__extQuery__(1);">
			</td>
          </tr>
  </table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  </form> 
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/reauditQuerySpeciaExpenses.json";
				
	var title = null;

	var columns = [
				{header: "序号", renderer:getIndex, align:'center'},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "单据号码", dataIndex: 'FEE_NO', align:'center'},
				{header: "制单日期", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "结算厂家", dataIndex: 'YIELD', align:'center',renderer:getItemValue},
				{header: "费用类型", dataIndex: 'FEE_TYPE', align:'center',renderer:getItemValue},
				{header: "申报金额(元)", dataIndex: 'DECLARE_SUM1', align:'center',renderer:amountFormat},
				{header: "工单状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
		      ];

	function myLink(val,meta,rec){
		return '<a href="<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/reauditDoInit.do?id='+rec.data.ID+'&feeType='+rec.data.FEE_TYPE+'">[审核]</a>' ;
	}	      

	
</script>
</body>
</html>
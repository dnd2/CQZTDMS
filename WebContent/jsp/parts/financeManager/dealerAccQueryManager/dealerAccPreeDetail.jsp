<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>资金占用详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script language="javascript" type="text/javascript">
	function doInit(){
		loadcalendar();  //初始化时间控件
		getDate();
		__extQuery__(1);
	}
</script>
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件财务管理 &gt; 服务商账户查询 &gt; 已扣款金额详情</div>
  <form name='fm' id='fm'>
  <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
  <input type="hidden" name="ACCOUNT_ID" id="ACCOUNT_ID" value="${ACCOUNT_ID }"/>
  <table class="table_query">
       <tr>            
        <td width="10%" align="right">服务商名称：
        </td>            
        <td width="20%">
			<input  class="long_txt" id="dealerNameSelect"  name="dealerNameSelect" type="text" value="${dealerName}"  disabled="disabled" readonly="readonly"/>
		    <input type="hidden" name="dealerId" id="dealerId" value="${dealerId }"/>
        </td>
        <td width="10%" align="right">日期：</td>
        <td width="25%" >
          <input id="checkSDate" class="short_txt" name="checkSDate"  datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
       	  <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
		 至
		  <input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
		  <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
        </td>
        <td width="10%" align="right">订单号：
        </td>            
        <td width="20%">
			<input  class="long_txt" id="sourceCode"  name="sourceCode" type="text" />
        </td>
       </tr>
       <tr>
        <td align="right">业务单号：</td> <!-- 销售单号、出库单号 -->           
        <td>
			<input  class="long_txt" id="orderCode"  name="orderCode" type="text"/>
        </td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
       </tr>
       <tr>
         <td colspan="6" align="center">
          <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)"/>
          <input class="normal_btn" type="button" value="导 出" onclick="exportPartPreeDetailExcel()"/>
		  <input class="normal_btn" type="button" name="button1" value="关 闭"  onclick="_hide();"/>
         </td>
       </tr>       
 	</table>
 	<br/>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--  
	<table class="table_query" id="amountSumTabstyle="">
	  <tr >
	    <td width="25%"></td>
	    <td width="25%"></td>
	     <td width="25%" align="center" >
	    总计:&nbsp;<font id="amountSum"></font>
	    </td>
	    <td width="25%"></td>
	  </tr>
	</table>
	-->
<!--分页 end -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/parts/financeManager/dealerAccQueryManager/partDealerAccQueryAction/partDealerPreemptionDetail.json?query=1";
	var title = null;
	var columns = [
					{header: "序号", dataIndex: 'RECORD_ID', renderer:getIndex},
					{header: "服务商名称", dataIndex: 'DEALER_NAME'},
					{header: "订单号", dataIndex: 'SOURCE_CODE'},
					{header: "业务单号", dataIndex: 'ORDER_CODE'},
		//			{header: "订单金额(元)", dataIndex: 'AMOUNT', align:'center'},
		//			{header: "开票金额(元)", dataIndex: 'INVO_AMOUNT', align:'center'},
					{header: "占用/释放金额(元)", dataIndex: 'AMOUNT',style:'text-align:right; padding-right:2%;'},
					{header: "变动类型", dataIndex: 'CHANGE_TYPE',renderer:getItemValue},
					{header: "变动人", dataIndex: 'NAME',style:'text-align:left; padding-left:1%;'},
					{header: "发生日期", dataIndex: 'CREATE_DATE'}
					
		      	  ];

	//导出
	function exportPartPreeDetailExcel(){
		document.fm.action="<%=contextPath%>/parts/financeManager/dealerAccQueryManager/partDealerAccQueryAction/exportParDeaAccPreeDetExcel.do";
		document.fm.target="_self";
		document.fm.submit();
	}

	function getDate()
	{
		var dateS = "";
		var dateE = "";
		var myDate = new Date();
	    var year = myDate.getFullYear();   //获取完整的年份(4位,1970-????)
	    var moth = myDate.getMonth();      //获取当前月份(0-11,0代表1月)
	    if(moth < 10)
	    {
	    	if(0 < moth)
		    {
		    	moth = "0" + moth;
		    }
		    else
		    {
		    	year = myDate.getFullYear() - 1;
		    	moth = moth + 12;
		    	if(moth < 10)
			    {
		    		moth = "0" + moth;
			    }
		    }
	    }
	    var day = myDate.getDate();       //获取当前日(1-31)
	    if(day < 10)
	    {
	    	day = "0" + day;
	    }
	    
	    dateS = year + "-" + moth + "-" + day;

	    moth = myDate.getMonth() + 1;	//获取当前月份(0-11,0代表1月)
	    if(moth < 10)
	    {
	    	moth = "0" + moth;
	    }
	    
	    dateE = myDate.getFullYear() + "-" + moth + "-" + day; 

	    //document.getElementById("checkSDate").value = dateS;
	    document.getElementById("checkEDate").value = dateE;
	}
</script> 
</form>
</body>
</html>
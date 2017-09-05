<%-- 
创建时间 : 2010.08.27
             创建人:lishuai
             功能描述：经销商查看开票的结果
--%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>结算单管理</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;开票通知单查询
</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="true">结算单号：</td>
        <td>
        	<input name="balanceNo" value="" type="text" class="middle_txt"/>
        </td>
		<td align="right" nowrap="true">生产基地：</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
				 genSelBoxExp("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'');
		    </script>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">制单日期：</td>
		<td align="left" nowrap="true">
			<input type="text" name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
		</td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/authorization/BalanceMain/delaerBalanceMainQuery.json";
		var title = null;
		
		var columns = [
					{header: "序号",dataIndex: 'NUM',align:'center'},
					{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "结算单号",dataIndex: 'BALANCE_NO',align:'center'},	
					{header: "状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
					{header: "接收人",dataIndex: 'AUTH_PERSON_NAME',align:'center'},
					{header: "接收时间",dataIndex: 'AUTH_DATE',align:'center'},
					{header: "开始时间",dataIndex: 'START_DATE',align:'center'},
					{header: "结束时间",dataIndex: 'END_DATE',align:'center'},
					{header: "开票单位",dataIndex: 'INVOICE_MAKER',align:'center'},	
					{header: "索赔单数量",dataIndex: 'CLAIM_COUNT',align:'center'}, 
					{header: "制单日期",dataIndex: 'CREATE_DATE',align:'center'},	
					{header: "总计(元)",dataIndex: 'BALANCE_AMOUNT',align:'center'},	
					{header: "财务扣款(元)",dataIndex: 'FINANCIAL_DEDUCT',align:'center'},	
					{header: "开票总金额(元)",dataIndex: 'NOTE_AMOUNT',align:'center',renderer:changeMoney},
					{header: "财务备注",dataIndex: 'FUNANCIAL_REMARK',align:'center'},
					{header: "操作",dataIndex: 'ID',align:'center', renderer:accAudut}
			      ];
	      
	//修改的超链接
	function accAudut(value,meta,record)
	{
		var status=record.data.STATUS;
		if(status==<%=Constant.ACC_STATUS_05 %>){
			return String.format("<a href='#' onclick='queryInfo(\""+ value +"\")'>[查看]</a><a href='#' onclick='queryPrint(\""+ value +"\")'>[打印]</a>");
		}else{
			return String.format("<a href='#' onclick='queryInfo(\""+ value +"\")'>[查看]</a>");
		}
	}

	function changeMoney(value){
		var money=parseFloat(value);
		if(money<0){
			return 0;
		}else{
			return value;
		}
	}

	function queryPrint(val){
		window.open('<%=contextPath%>/claim/authorization/BalanceMain/queryPrintInfo.do?id='+val,'','left=0,top=0,width='+ (screen.availWidth - 10) +',height='+ (screen.availHeight-50) +',toolbar=no,menubar=no,scrollbars=no,location=no');
	}
	
	function queryInfo(val)
	{
		fm.action = "<%=contextPath%>/claim/authorization/BalanceMain/queryDealerBalanceInfo.do?id="+val+"&infoFlag=1";
		fm.submit();
	}
	
</script>
</body>
</html>
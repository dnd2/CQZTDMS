<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>结算室收单(按汇总单)</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}

		function clearInput(inputId){
			var inputVar = document.getElementById(inputId);
			inputVar.value = '';
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;结算室收单
</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="true">经销商代码：</td>
		<td align="left" nowrap="true">
			<input class="long_txt" id="dealerCode"  name="dealerCode" type="text"/>
            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput('dealerCode');" value="清除"/>
		</td>
		<td align="right" nowrap="true">生产基地：</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
				genSelBoxContainStr("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'<%=CommonUtils.checkNull(request.getAttribute("yieldly"))%>');
		    </script>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">建单日期：</td>
		<td align="left" nowrap="true">
			<input type="text" name="startDate" id="t1" value="" type="text" class="short_txt" 
			datatype="1,is_date,10" group="t1,t2"  hasbtn="true" 
			callFunction="showcalendar(event, 't1', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="endDate" id="t2" value="" type="text" class="short_txt" 
 			datatype="1,is_date,10" group="t1,t2" hasbtn="true" 
 			callFunction="showcalendar(event, 't2', false);"/>
		</td>
		<td align="right" nowrap="true">上报日期：</td>
		<td align="left" nowrap="true">
			<input type="text" name="startReportDate" id="tr1" value="" type="text" class="short_txt" 
			datatype="1,is_date,10" group="tr1,tr2"  hasbtn="true" 
			callFunction="showcalendar(event, 'tr1', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="endReportDate" id="tr2" value="" type="text" class="short_txt" 
 			datatype="1,is_date,10" group="tr1,tr2" hasbtn="true" 
 			callFunction="showcalendar(event, 'tr2', false);"/>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">结算汇总单号：</td>
        <td>
        	<input name="balanceNo" value="" type="text" class="middle_txt"/>
        </td>
		<td align="right" nowrap="true">单据状态：</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
				 genSelBoxExp("status",<%=Constant.BALANCE_GATHER_TYPE%>,"",true,"short_sel","","false",'<%=Constant.BALANCE_GATHER_TYPE_01%>');
		    </script>
		</td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input type="hidden" name="status" value="<%=Constant.ACC_STATUS_06%>"/><!-- 收单状态 -->
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
			var url = "<%=contextPath%>/claim/application/DealerBalance/oemGatherBalanceOrderQuery.json";
			var title = null;
			
			var columns = [
						{header: "序号",align:'center',renderer:getIndex},
						{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},	
						{header: "经销商简称",dataIndex: 'DEALER_NAME',align:'center'},	
						{header:'省份',dataIndex:'REGION_NAME',align:'center'},
						{header: "结算汇总单号",dataIndex: 'TOTAL_NO',align:'center'},		
						{header: "结算单起止时间",dataIndex: '',align:'center',renderer:MyDate},
						//{header: "结算单结束时间",dataIndex: 'END_BALANCE_DATE',align:'center'},
						{header: "产地",dataIndex: 'YIELDLY',align:'center',renderer:getItemValue},
						{header: "结算单数量",dataIndex: 'BALANCE_COUNT',align:'center'}, 
						{header: "建单日期",dataIndex: 'CREATEDATE',align:'center'}, 	
						{header: "处理状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},	
						{header: "操作",dataIndex: 'ID',align:'center', renderer:accAudut}
				      ];

			//设置时间
			function MyDate(value,meta,record)
			{ 
				var START_DATE = record.data.START_DATE;
		  		var END_DATE = record.data.END_DATE;
		  		return String.format(START_DATE+"至"+END_DATE);
			}
		      
		//修改的超链接
		function accAudut(value,meta,record)
		{ 
	  		var res = '';
	  		var curStatus = record.data.STATUS;
	  		var tipContent = '明细';
	  		if(curStatus==<%=Constant.BALANCE_GATHER_TYPE_02%>){//"已上报"
	  			tipContent = '签收';
	  			res += '<a href="#" onclick="chanageOrderStatusConfirm('+value+')">['+'退回'+']</a>';
			}
			res += '<a href="#" onclick="toDetail('+value+','+curStatus+')">['+tipContent+']</a>';
			//res += '<a href="#" onclick="printDetail('+value+','+curStatus+')">[打印]</a>';
			return res;
		}

		//刷新页面
		function refreshPage(){
			__extQuery__(1);
		}

		function chanageOrderStatusConfirm(orderId){
			MyConfirm("是否退回！",chanageOrderStatus,[orderId]);
		}
		
		//变更索赔单状态
		function chanageOrderStatus(orderId){
		    var chanage_url ="<%=contextPath%>/claim/application/DealerBalance/returnGatherOrderStatus.json?orderId="+orderId;
		    makeNomalFormCall(chanage_url,refreshPage,'fm','');
		}

		//结算汇总单明细查询
		//var 汇总单Id  curStatus :汇总单状态
		function toDetail(val,curStatus)
		{
			var tarUrl = "<%=contextPath%>/claim/application/DealerBalance/queryGatherBalanceOrder.do?id="+val;
			var width=900;
			var height=500;

			if(curStatus==<%=Constant.BALANCE_GATHER_TYPE_02%>){//"已上报"
				tarUrl = tarUrl + "&isSign=true";
			}
			var screenW = window.screen.width-30;	
			var screenH = document.viewport.getHeight();

			if(screenW!=null && screenW!='undefined')
				width = screenW;
			if(screenH!=null && screenH!='undefined')
				height = screenH;
			
			OpenHtmlWindow(tarUrl,width,height);
		}

		//打印结算汇总单明细 
		function printDetail(val,curStatus){
			var tarUrl = "<%=contextPath%>/claim/application/DealerBalance/printGatherBalanceOrder.do?id="+val;
			window.open(tarUrl,'','left=0,top=0,width='+screen.availWidth +'- 10,height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=no,location=no');
		}
	</script>
</body>
</html>
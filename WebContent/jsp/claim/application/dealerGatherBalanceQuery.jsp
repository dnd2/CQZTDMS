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
	<title>结算汇总单查询</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;结算单物流管理
</div>
<form method="post" name="fm" id="fm">
    <%-- 经销商级别 --%>
    <input type="hidden" id="dealerLevel" value="<%=CommonUtils.checkNull(request.getAttribute("dealerLevel")) %>"/>
    <%-- 经销商ID--%>
    <input type="hidden" id="dealerId" value="<%=CommonUtils.checkNull(request.getAttribute("dealerId")) %>"/>
<table align="center" class="table_query">
	<tr>
	    <td align="right" nowrap="true">结算汇总单号：</td>
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
		<td align="right" nowrap="true">单据状态：</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
				 genSelBoxExp("status",<%=Constant.BALANCE_GATHER_TYPE%>,"",true,"short_sel","","true",'');
		    </script>
		</td>
	</tr>
	<tr>
		<td colspan="4" align="center"><font color="red">*必须上报结算物流单后才能打印结算单</font></td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
			&nbsp;&nbsp;
			<input class="normal_btn" type="button" name="addBtn" id="addBtn" value="新增" 
				onclick="forwordToAddPage('fm','<%=contextPath%>/claim/application/DealerBalance/createGatherBalanceOrderPage.do','_self');"/>
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<script type="text/javascript">
			var myPage;
			var url = "<%=contextPath%>/claim/application/DealerBalance/gatherBalanceOrderQuery.json";
			var title = null;
			
			var columns = [
						{header: "序号",align:'center',renderer:getIndex},
						{header: "结算汇总单号",dataIndex: 'TOTAL_NO',align:'center'},		
						{header: "产地",dataIndex: 'YIELDLY',align:'center',renderer:getItemValue},
						{header: "结算单数量",dataIndex: 'BALANCE_COUNT',align:'center'}, 
						{header: "建单日期",dataIndex: 'CREATEDATE',align:'center'}, 	
						{header: "处理状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},	
						{header: "操作",dataIndex: 'ID',align:'center', renderer:accAudut}
				      ];
		      
		//修改的超链接
		function accAudut(value,meta,record)
		{ 
	  		var res = '';
	  		var status = record.data.STATUS;
			if(<%=Constant.BALANCE_GATHER_TYPE_01%>==status){
				res += '<a href="#" onclick="deleteOrderConfirm('+value+')">[删除]</a>';
				res += '<a href="#" onclick="chanageOrderStatusConfirm('+value+',<%=Constant.BALANCE_GATHER_TYPE_02%>)">[上报]</a>';
				res += '<a href="#" onclick="toDetail('+value+')">[明细]</a>';
			}else{
				res += '<a href="#" onclick="toDetail('+value+')">[明细]</a>';
				//res += '<a href="#" onclick="printDetail('+value+','+status+')">[打印]</a>';
			}
			return res;
		}

		function chanageOrderStatusConfirm(orderId,status){
			MyConfirm("确定上报！",chanageOrderStatus,[orderId,status]);
		}

		//变更索赔单状态
		function chanageOrderStatus(orderId,status){
		    var chanage_url ="<%=contextPath%>/claim/application/DealerBalance/chanageGatherOrderStatus.json?orderId="+orderId+"&status="+status;
		    makeNomalFormCall(chanage_url,refreshPage,'fm','');
		}

		function deleteOrderConfirm(orderId){
			MyConfirm("确定删除！",deleteOrder,[orderId]);
		}

		//删除对应结算单
		function deleteOrder(orderId){
		    var delete_url ="<%=contextPath%>/claim/application/DealerBalance/deleteGatherBalanceOrder.json?orderId="+orderId;
		    makeNomalFormCall(delete_url,refreshPage,'fm','');
		}

		//刷新页面
		function refreshPage(){
			__extQuery__(1);
		}

		//结算汇总单明细查询
		function toDetail(val)
		{
			var tarUrl = "<%=contextPath%>/claim/application/DealerBalance/queryGatherBalanceOrder.do?id="+val;
			var width=900;
			var height=500;

			var screenW = window.screen.width-30;	
			var screenH = document.viewport.getHeight();

			if(screenW!=null && screenW!='undefined')
				width = screenW;
			if(screenH!=null && screenH!='undefined')
				height = screenH;
			
			OpenHtmlWindow(tarUrl,width,height);
		}
	

		//跳转到新增结算页面
		function forwordToAddPage(frmId,url,targetVar){
			var frmVar = document.getElementById(frmId);
			frmVar.action = url;
			frmVar.target = targetVar;
			frmVar.submit();
		}

		//打印结算汇总单明细 
		function printDetail(val,curStatus){
			var tarUrl = "<%=contextPath%>/claim/application/DealerBalance/printGatherBalanceOrder.do?id="+val;
			window.open(tarUrl,'','left=0,top=0,width='+screen.availWidth +'- 10,height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=no,location=no');
		}
	</script>
</body>
</html>
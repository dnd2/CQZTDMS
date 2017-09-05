<%-- 
创建时间 : 2010.08.27
             创建人:lishuai
             功能描述：结算室审核，可对索赔单进行批量审核，和逐条审核
--%>
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
	<title>结算室审核</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;结算单维护
</div>
<form method="post" name="fm" id="fm">
    <%-- 经销商级别 --%>
    <input type="hidden" id="dealerLevel" value="<%=CommonUtils.checkNull(request.getAttribute("dealerLevel")) %>"/>
    <%-- 经销商ID--%>
    <input type="hidden" id="dealerId" value="<%=CommonUtils.checkNull(request.getAttribute("dealerId")) %>"/>
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="true">制单日期：</td>
		<td align="left" nowrap="true">
			<input type="text" name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
		</td>
		<td align="right" nowrap="true">生产基地：</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
				 genSelBoxExp("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'');
		    </script>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">结算单号：</td>
        <td>
        	<input name="balanceNo" value="" type="text" class="middle_txt"/>
        </td>
		<td align="right" nowrap="true">单据状态：</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
				 genSelBoxExp("status",<%=Constant.ACC_STATUS%>,"",true,"short_sel","","true",'');
		    </script>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="4"><font color="red">*必须上报结算物流单后才能打印结算单</font></td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
			&nbsp;&nbsp;
			<input class="normal_btn" type="button" name="addBtn" id="addBtn" value="新增" 
				onclick="forwordToAddPage('fm','<%=contextPath%>/claim/application/DealerNewKp/addDealerKpInit.do','_self');"/>
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<script type="text/javascript">
			var myPage;
			var url = "<%=contextPath%>/claim/application/DealerNewKp/dealerKpOrderQuery.json";
			var title = null;
			
			var columns = [
						{header: "序号",align:'center',renderer:getIndex},
						{header: "结算单号",dataIndex: 'BALANCE_NO',align:'center'},		
						{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
						{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
						{header: "开始-结束时间",dataIndex: 'START_DATE',align:'center',renderer:dealDateArea},
						{header: "结算单状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
						{header: "索赔单数量",dataIndex: 'CLAIM_COUNT',align:'center'}, 
						<%--{header: "索赔单申请金额(元)",dataIndex: 'AMOUNT_SUM',align:'center',renderer:formatCurrency},--%> 
						{header: "索赔单审请金额(元)",dataIndex: 'APPLY_AMOUNT',align:'center',renderer:formatCurrency}, 
						{header: "索赔单开票金额(元)",dataIndex: 'KP_AMOUNT',align:'center',renderer:formatCurrency}, 
						{header: "制单日期",dataIndex: 'CREATE_DATE',align:'center'},
						{header:'生产基地',dataIndex:'YIELDLY',align:'center',renderer:getItemValue},		
						{header: "操作",dataIndex: 'ID',align:'center', renderer:accAudut}
				      ];
		      
		//修改的超链接
		function accAudut(value,meta,record)
		{ 
	  		var res = '';
	  		var status = record.data.STATUS;
	  		var gatherStatus = record.data.GATHERSTATUS;
		  	var dealerL = $('dealerLevel').value;//登陆经销商级别
		  	var dealerId = $('dealerId').value;//当前登陆经销商ID
		  	var balanceLevel=${balanceLevel}; //结算级别
		  		
		  	var currDealerId = record.data.DEALER_ID;//当前结算单对应经销商ID
			if('<%=Constant.DEALER_LEVEL_01%>'==dealerL){//登陆用户为一级经销商
				if(dealerId==currDealerId){//当前结算单为一级经销商的
					if('<%=Constant.ACC_STATUS_07%>'==status || '<%=Constant.ACC_STATUS_08%>'==status){//该结算单未标识为"已完成"
						res += "<a href='#' onclick='deleteOrderConfirm(\""+value+"\")'>[删除]</a>";
						res += "<a href='#' onclick='chanageOrderStatusConifrm(\""+value+"\",<%=Constant.ACC_STATUS_09%>)'>[完成]</a>";
					}
				}else{//当前结算单为二级经销商
					if('<%=Constant.ACC_STATUS_08%>'==status){
						res += "<a href='#' onclick='chanageOrderStatusConifrm(\""+value+"\",<%=Constant.ACC_STATUS_07%>)'>[退回]</a>";
						res += "<a href='#' onclick='chanageOrderStatusConifrm(\""+value+"\",<%=Constant.ACC_STATUS_09%>)'>[完成]</a>";
					}
				}
			}else{//其他经销商 不是一级经销商
				if(dealerId==currDealerId){//结算单为当前登陆用户结算单
					if('<%=Constant.ACC_STATUS_07%>'==status){//该结算单未标识为"已完成"
						if(balanceLevel=='<%=Constant.BALANCE_LEVEL_HIGH%>'){//上级结算
							res += "<a href='#' onclick='deleteOrderConfirm(\""+value+"\")'>[删除]</a>";
						    res += "<a href='#' onclick='chanageOrderStatusConifrm(\""+value+"\",<%=Constant.ACC_STATUS_08%>)'>[上报]</a>";
						}
						if(balanceLevel=='<%=Constant.BALANCE_LEVEL_SELF%>'){//独立结算
							res += "<a href='#' onclick='deleteOrderConfirm(\""+value+"\")'>[删除]</a>";
							res += "<a href='#' onclick='chanageOrderStatusConifrm(\""+value+"\",<%=Constant.ACC_STATUS_09%>)'>[完成]</a>";
						}
					}
				}
			}
	  		res += String.format("<a href='#' onclick='toDetail(\""+ value +"\")'>[明细]</a>");
	  		if(<%=Constant.BALANCE_GATHER_TYPE_01.toString()%>!=gatherStatus&&gatherStatus!=null){
	  			res += "<a href='#' onclick=\"window.open('<%=contextPath%>/claim/application/DealerBalance/querySumBalanceDetail.do?id="+value+"','','left=0,top=0,width="+screen.availWidth +"- 10,height="+screen.availHeight+"-50,toolbar=no,menubar=no,scrollbars=no,location=no');\">[打印]</a>";
	  		}
			return res;
		}

		function chanageOrderStatusConifrm(orderId,status){
			MyConfirm("是否确定！",chanageOrderStatus,[orderId,status]);
		}

		//变更索赔单状态
		function chanageOrderStatus(orderId,status){
		    var chanage_url ="<%=contextPath%>/claim/application/DealerBalance/chanageOrderStatus.json?orderId="+orderId+"&status="+status;
		    makeNomalFormCall(chanage_url,refreshPage,'fm','');
		}

		function deleteOrderConfirm(orderId){
			MyConfirm("确定删除！",deleteOrder,[orderId]);
		}

		//删除对应结算单
		function deleteOrder(orderId){
			var delete_url ="<%=contextPath%>/claim/application/DealerBalance/deleteBalanceOrder1.json?orderId="+orderId;
		    makeNomalFormCall(delete_url,refreshPage1,'fm','');
		}

		function refreshPage1(json){
			var msg=json.msg;
			var orderId=json.orderId;
			if(msg==true){
				var delete_url ="<%=contextPath%>/claim/application/DealerBalance/deleteBalanceOrder.json?orderId="+orderId;
				makeNomalFormCall(delete_url,refreshPage,'fm','');
			}else{
				MyAlert("请删除之前月份数据,不能跨月删除!");
			}
		}
		
		function refreshPage(){
			__extQuery__(1);
		}

		//取得结算单结算时需要的审核日期范围
		function dealDateArea(value,meta,record){
			var startDate = record.data.START_DATE;
			var endDate = record.data.END_DATE;
			return String.format(startDate+" 至  "+endDate);
		}
		
		function toDetail(val)
		{
			var tarUrl = "<%=contextPath%>/claim/application/DealerBalance/queryBalanceOrderDetail2.do?id="+val;
            //不采用OpenHtmlWindow方式
			//aobj.href = tarUrl;
			//aobj.click();
			var width=900;
			var height=500;

			var screenW = window.screen.width-30;	
			var screenH = document.viewport.getHeight();

			if(screenW!=null && screenW!='undefined')
				width = screenW;
			if(screenH!=null && screenH!='undefined')
				height = screenH;
			
			OpenHtmlWindow(tarUrl,width,height);
			//fm.action = "<%=contextPath%>/claim/application/DealerBalance/queryBalanceOrderDetail.do?id="+val;
			//fm.submit();
		}
	

		//跳转到新增结算页面
		function forwordToAddPage(frmId,url,targetVar){
			var frmVar = document.getElementById(frmId);
			frmVar.action = url;
			frmVar.target = targetVar;
			frmVar.submit();
		}
	</script>
</body>
</html>
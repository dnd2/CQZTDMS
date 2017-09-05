<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrGatherBalancePO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔件结算汇总单明细</title>
<% String contextPath = request.getContextPath(); %>
</head>
<script type="text/javascript">
	function doInit(){
		__extQuery__(1);
	}

	//返回查询页面
	function pageToQuery(){
		location.href = "<%=contextPath%>/claim/application/DealerBalance/dealerGatherBalanceInit.do";
	}
</script>
<body>
    <%
        String isSign = (String)request.getParameter("isSign");//是否为签收操作 true 是 | 其他   不是
    	TtAsWrGatherBalancePO gatherPO = (TtAsWrGatherBalancePO)request.getAttribute("balancePO");
    %>
	<form method="post" name ="fm" id="fm">
		<table width="100%">
		<tr><td>
	 	<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif"/>&nbsp; 当前位置：售后服务管理&gt;索赔结算管理&gt;结算单物流管理
		</div>
		<table class="table_edit">
	    	<tr>
		    	<th><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
	        </tr>
        </table>
        <table class="table_edit">
        	<tr>
        		<td align="right" nowrap="nowrap">结算汇总单号:</td>
        		<td align="left">
        		<%=CommonUtils.checkNull(gatherPO.getTotalNo())%>&nbsp;
        		<%-- 结算汇总单Id  --%>
        		<input type="hidden" value="<%=gatherPO.getId()%>" name="id"/>
        		</td>
        		<td align="right" nowrap="nowrap">建单日期:</td>
        		<td align="left"><%=CommonUtils.printDate(gatherPO.getCreateDate())%>&nbsp;</td>
        	</tr>
        	<tr>
        		<td align="right" nowrap="nowrap">产地:</td>
        		<td align="left">
        			<script type="text/javascript">
        				document.write(getItemValue('<%=CommonUtils.checkNull(gatherPO.getYieldly())%>'));
        			</script>&nbsp;
        		</td>
        		<td align="right" nowrap="nowrap">结算单数:</td>
        		<td align="left"><%=CommonUtils.checkNull(gatherPO.getBalanceCount())%>&nbsp;</td>
        	</tr>
        </table>
        <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
        <!--分页 end --> 
        <script type="text/javascript">
	        var myPage;
	        //查询路径
	        var url = "<%=contextPath%>/claim/application/DealerBalance/queryGatherBalanceOrderDetail.json";
	     				
	        var title = null;
	
	        var columns = [
						{id:'action',header:"序号",align:'center',renderer:getIndex},
						{header: "产地",dataIndex: 'YIELDLY',align:'center',renderer:getItemValue},
						{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
	       				{header: "经销商简称",dataIndex: 'DEALER_NAME',align:'center'},
	       				{header:'省份',dataIndex:'REGION_NAME',align:'center'},
	       				{header: "结算单类型",dataIndex: 'DEALER_LEVEL',align:'center',renderer:getItemValue},
	       				{header: "结算单号", dataIndex: 'BALANCE_NO', align:'center',renderer:toDetail},
	       				{header: "开始-结束日期", dataIndex: 'START_DATE', align:'center',renderer:dealDateArea},
	       				{header: "申报金额", dataIndex: 'APPLY_AMOUNT', align:'center',renderer:formatCurrency},
	       				{header: "索赔单数量", dataIndex: 'CLAIM_COUNT', align:'center'},
	       				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
	       				{header: "备注", dataIndex: 'REMARK', align:'center'}
	       		      ];

			//取得结算单结算时需要的审核日期范围
			function dealDateArea(value,meta,record){
				var startDate = record.data.START_DATE;
				var endDate = record.data.END_DATE;
				return String.format(startDate+" 至  "+endDate);
			}

			function closeWindow(){
				try{
					_hide();
				}catch(e){
					window.close();
				}
			}

			//查询结算单明细
			function toDetail(value,meta,record){
				var orderId = record.data.ID;
				var tarUrl = "<%=contextPath%>/claim/application/DealerBalance/queryBalanceOrderDetail3.do?id="+orderId;
				return String.format("<a href='#' onclick='openWindowDialog(\""+tarUrl+"\")'>"+value+"</a>");
			}

		    function openWindowDialog(targetUrl){
			    var height = 500;
			    var width = 800;
			    var iTop = (window.screen.availHeight-30-height)/2;  //获得窗口的垂直位置      
			    var iLeft = (window.screen.availWidth-10-width)/2;   //获得窗口的水平位置
			    var params = "width="+width+",height="+height+",top="+iTop+",left="+iLeft+",scrollbars=yes,resizable=yes,status=no";
			    window.open(targetUrl,null,params);
		    }

		    function signConfirm(){
			    var flag = confirm("确定签收？");
			    if(flag){
			    	signOrder();
			    }
		    }

		    function signOrder(){
			    var sign_url ="<%=contextPath%>/claim/application/DealerBalance/signGatherBalanceOrder.json";
			    makeNomalFormCall(sign_url,dealSign,'fm','');
		    }

		    function dealSign(json){
			    var status = json.SUCCESS;
			    if('DEALED'==status)
				    MyAlert("其他人正在签收该结算单！");
			    else if('' == status)
				    MyAlert("该结算单已经完成签收！");
			    refreshParentPage();
		    }

		    function refreshParentPage(){
		    	closeWindow();
		    	parentContainer.__extQuery__(1);
		    }
        </script>
        <br>
        </td></tr>
        <tr>
        	<td align="center">
        	    <% if("true".equals(isSign)){ %>
        	    	<input type="button" name="closeBtn" class="normal_btn" value="签收" onclick="signConfirm();"/>
        	    <% } %>
        		<input type="button" name="closeBtn" class="normal_btn" value="关闭" onclick="closeWindow();"/>
        	</td>
        </tr>
        </table>
        <script type="text/javascript">
        	doInit();
        </script>
	</form>
</body>
</html>
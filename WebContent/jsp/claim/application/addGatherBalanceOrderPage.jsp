<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔件结算汇总单新增</title>
<% String contextPath = request.getContextPath(); %>
</head>
<script type="text/javascript">
	function doInit(){
	   loadcalendar();
	}

	//返回查询页面
	function pageToQuery(){
		location.href = "<%=contextPath%>/claim/application/DealerBalance/dealerGatherBalanceInit.do";
	}
</script>
<body onload="doInit();">
	<form method="post" name ="fm" id="fm">
	<% String dealerLevel = (request.getAttribute("dealerLevel")).toString(); 
	   String balanceLevel = (request.getAttribute("balanceLevel")).toString();  //结算等级
	  if((Constant.BALANCE_LEVEL_SELF).equals(balanceLevel)){%>
		<table width="100%">
		<tr><td>
	 	<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif"/>&nbsp; 当前位置：售后服务管理&gt;索赔结算管理&gt;结算单物流管理
		</div>
		<table class="table_edit">
	    	<tr>
		    	<th><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
	        </tr>
	        <tr>
	        	<td>
			        <table>
			        	<tr align="left">
			        		<td align="right" nowrap="nowrap">结算产地：</td>
			        		<td align="left">
					           <script type="text/javascript">
					               genSelBoxExp("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'');
					           </script>
					           <font color="red">&nbsp;*</font>
			        		</td>
			        	</tr>
			        </table>
	        	</td>
	        </tr>
        </table>
        <!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
        <!--分页 end --> 
        <script type="text/javascript">
	        var myPage;
	        //查询路径
	        var url = "<%=contextPath%>/claim/application/DealerBalance/queryBalanceOrderForGather.json";
	     				
	        var title = null;
	
	        var columns = [
						{id:'action',header: "选择<input type=\"checkbox\" name=\"balanceIds\" onclick=\"selectAll(this,'balanceId')\">",dataIndex: 'id',renderer:selectOrder},
						{header: "产地",dataIndex: 'YIELDLY',align:'center',renderer:getItemValue},
						{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
	       				{header: "经销商简称",dataIndex: 'DEALER_NAME',align:'center'},
	       				{header: "结算单类型",dataIndex: 'DEALER_LEVEL',align:'center',renderer:getItemValue},
	       				{header: "结算单号", dataIndex: 'BALANCE_NO', align:'center'},
	       				{header: "开始-结束日期", dataIndex: 'START_DATE', align:'center',renderer:dealDateArea},
	       				{header: "申报金额", dataIndex: 'BALANCE_AMOUNT', align:'center',renderer:formatCurrency},
	       				{header: "索赔单数量", dataIndex: 'CLAIM_COUNT', align:'center'},
	       				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
	       				{header: "备注", dataIndex: 'ID', align:'center',renderer:remarkDeal}
	       		      ];

			//取得结算单结算时需要的审核日期范围
			function dealDateArea(value,meta,record){
				var startDate = record.data.START_DATE;
				var endDate = record.data.END_DATE;
				return String.format(startDate+" 至  "+endDate);
			}

			function remarkDeal(value,meta,record){
				var res = '';
				res += '<input type="text" name="bRemark" datatype="1,is_null,300" class="middle_txt"/>';
				return res;
			}
			
	        //产地列表onchange联动响应
	        function doCusChange(value){
	        	__extQuery__(1);
	        }

	        function selectOrder(value,meta,record){
		        var balanceId = record.data.ID;
		        return String.format("<input type=\"checkbox\" name=\"balanceId\" value=\""+balanceId+"\"/>");
	        }
	        
            //返回回运单查询页
	        function hisBack(){
		        location.href = "<%=contextPath%>/claim/application/DealerBalance/dealerGatherBalanceInit.do";
	        }

	        function createOrderConfirm(){
		        var yieldlyEle = $('yieldly');
		        var yieldly = '';
		        if(yieldlyEle)
		        	yieldly = yieldlyEle.value;
		        if(yieldly && ''!=yieldly){
			        MyConfirm("是否确认！",createOrder,[]);
		        }else{
			        MyAlert("请选择发运地点！");
		        }
	        }

            //创建物流单（由一级经销商向车厂发起回运）
	        function createOrder(){
		        var turl = "<%=contextPath%>/claim/application/DealerBalance/addGatherBalanceOrder.json";
		        makeNomalFormCall(turl,showDetail,'fm','createOrdBtn');
	        }

	        function showDetail(json){
		        var status = json.SUCCESS;
		        if('DEALED'==status){
			        MyAlert("其他人正在创建物流单！");
			        hisBack();
		        }else if('SUCCESS'!=status){
		        	MyAlert("操作失败！");
		        	__extQuery__(1);
		        }else{
		        	__extQuery__(1);
		        }
	        }
        </script>
        <br>
        <table class="table_edit">
        	<tr>
        		<td align="center">
					<input class="normal_btn" type="button" id="createOrdBtn" name="createOrdBtn" value="确认" onclick="createOrderConfirm();">&nbsp;&nbsp;
		            <input class="normal_btn" type="button" name="qryButton2" value="返回" onclick="hisBack();">
            	</td>
           </tr>
        </table>
        </td></tr>
        </table>
        <%}else{ %>
        	<script type="text/javascript">
        		MyConfirm("您没有独立结算权限!",pageToQuery,[],pageToQuery,[]);
        	</script>
        <%} %>
	</form>
</body>
</html>
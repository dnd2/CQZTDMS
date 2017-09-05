<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/productCombofunc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/sales/ordermanage/extractionofvehicle/js/orderUtils.js"></script>
<script type="text/javascript">
	//g_webAppName
	var rebate_01 = '<%=Constant.WRITE_DOWNS_WAY_STATUS_01 %>';	// 前冲
	var rebate_02 = '<%=Constant.WRITE_DOWNS_WAY_STATUS_02 %>';	// 后冲
	
	var EXCHANGE_TYPE = '<%=Constant.ORDER_TYPE_04 %>';	// 中转库订单
	var COMPANY_TYPE = '<%=Constant.ORDER_TYPE_05 %>';	// 大客户
	var COMPANY_CREATE_TYPE = '<%=Constant.ORDER_TYPE_02 %>';	// 集团订做车
	var IS_YES_TYPE = '<%=Constant.IF_TYPE_YES %>';	// 自提类型 
	var TRANSPORT_TYPE_01 = '<%=Constant.TRANSPORT_TYPE_01 %>';	// 自提 
	var TRANSPORT_TYPE_02 = '<%=Constant.TRANSPORT_TYPE_02 %>';	// 发运
	var PROMISE_TYPE = '<%=Constant.ACCOUNT_TYPE_05 %>';	// 承诺类型 

	function doInit() {
		loadAccountInfo();
	}

	/**
	 * 获取账户余额和可用用余额
	 */
	function loadAccountInfo() {
		
		var dealerId = document.getElementById("dealerId").value;
		var yieldlyId = document.getElementById("yieldId").value;
		var finType = document.getElementById("finType").value;

		var url = "<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionOrderQuery/loadDealerAreaAccountInfo.json";

		makeCall(url, showAccount, {dealerId: dealerId, yieldlyId: yieldlyId, finType: finType});
		
	}

	/**
	 * 显示账户数据
	 */
	function showAccount(json) {
		
		if(json.info) 
		{
			document.getElementById("yfin_total_span").innerHTML = json.info.AMOUNT;
			document.getElementById("yfin_total").value = json.info.AMOUNT;
			
			document.getElementById("fin_useful_span").innerHTML = json.info.USEFUL_AMOUNT;
			document.getElementById("fin_useful").value = json.info.USEFUL_AMOUNT;
		}
		
	}

	function save(type) {
		subDisabled(); 
		if(type == '0') {
			MyConfirm("确定驳回提车单?", checkno, null, subEnable);
		} else {
			MyConfirm("确定通过提车单申请?", checkyes, null, subEnable);
		}
	}
	
	function checkno() 
	{
		var orderId = document.getElementById('orderId').value;
		var url = "<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionOEM/saveCheckResult.json?type=0";
		
		makeCall(url, showcheck,{orderId: orderId});
	}

	
	function checkyes() 
	{
		var url = "<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionOEM/saveCheckResult.json?type=1";
		
		makeNomalFormCall(url, showcheck,'fm','');
	}

	function showcheck(json) 
	{
		if(json.Exception) {
			parent.MyAlert(json.Exception.message);
			subEnable();
		}
		else 
		{
			parent.MyAlert(json.info.message);
			window.location.href = '<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionOEM/orderAuditQuerySecond.do?time=' + new Date().getTime();
		}
	}

	function pageBack() {
		window.location.href = "<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionOEM/orderAuditQuerySecond.do";
	}
</script>
<title>常规订单提报 维护</title>
</head>
<body >
	<div style="z-index: 200; position: absolute; PADDING-BOTTOM: 1px; PADDING-LEFT: 1px; PADDING-RIGHT: 1px; DISPLAY: none; BACKGROUND: #ffcc00; TOP: 4px; PADDING-TOP: 1px" id=loader></DIV>
	<div class=navigation><IMG src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;当前位置：提车单管理 &gt; 提车单明细</DIV>
	<form method="POST" name="fm" id="fm">
     	<input type="hidden" name="yieldId" id="yieldId" value="${orderMap.AREA_ID }" /> 
     	<input type="hidden" name="orderId" id="orderId" value="${orderMap.ORDER_ID }" />
     	<input type="hidden" name="dealerId" id="dealerId" value="${orderMap.DEALER_ID }"/>
     	<input type="hidden" name="finType" id="finType" value="${orderMap.FUND_TYPE_ID }"/>
		<TABLE class=table_query align=center>
    		<TBODY>
     			 <tr>
        			<TH colSpan=4 noWrap align=left><IMG class=nav src="<%=request.getContextPath()%>/img/subNav.gif"> <a href="javascript:tabDisplayControl('moneyTable')">基本信息</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
      			</tr>
	   			<tr class="tabletitle">
      				<td width="20%" class="table_query_right">产地：</td>
      				<td width="30%" align="left">
      					<span id="yieldly_span">${orderMap.AREA_NAME }</span>
      				</td>
      				<td width="20%" class="table_query_right" id="_productControl_3"> 资金账户类型：</td>
      				<td width="30%" align="left">
      					<span id="finType_span">${orderMap.FUND_TYPE_NAME }</span>
       				</td>
      			</tr>
    		</TBODY>
  		</TABLE>
   		<TABLE class=table_list style="border-bottom:1px solid #DAE0EE" >  
		    <tr class=cssTable >
		      <th nowrap="nowrap">车系</th>
		      <th nowrap="nowrap">车型</th>
		      <th nowrap="nowrap">整车物料码</th>
		      <th nowrap="nowrap">整车物料名称</th>
		      <th nowrap="nowrap">颜色</th>
		      <th nowrap="nowrap">申请数量</th>
			  <th nowrap="nowrap">分配数量</th>
		      <th nowrap="nowrap" id="singleTdTitle">市场零售价</th>
		      <th nowrap="nowrap" id="singleTdTitle">审核价</th>
		      <th nowrap="nowrap" id="span7">折扣率%</th>
		      <th nowrap="nowrap" id="span8">折后价</th>
		      <th nowrap="nowrap" id="span9">折扣额</th>
		      <th nowrap="nowrap" id="totalTdTitle">合计</th>
		    </tr>
    		<tbody id="tbody1">
    			<c:forEach items="${materialList }" var="list">
    				<tr class="table_list_row2">
    					<td>
    						${list.SERIES_NAME }
    						<input type='hidden' id='orderDetailId${list.MATERIAL_ID }' name='orderDetailId' value='${list.DETAIL_ID }' />
    						<input type='hidden' id='materialId${list.MATERIAL_ID }' name='materialId' value='${list.MATERIAL_ID }' />
    					</td>
    					<td>${list.MODEL_NAME }</td>
    					<td>${list.MATERIAL_CODE }</td>
    					<td>${list.MATERIAL_NAME }</td>
    					<td>${list.COLOR_NAME }</td>
    					<td>${list.SUB_NUM }</td>
    					<td>
    						${list.CHECK_AMOUNT }
    					</td>
    					<td>
    						<script>document.write(amountFormat(${list.SINGLE_PRICE }));</script>
    					</td>
    					<td>
    						<script>document.write(amountFormat(${list.CHK_PRICE }));</script>
    					</td>
    					<td>
    						${list.DISCOUNT_RATE }
    					</td>
    					<td>
    						<script>document.write(amountFormat(${list.DISCOUNT_S_PRICE }));</script>
    					</td>
    					<td>
    						<script>document.write(amountFormat(${list.DISCOUNT_PRICE }));</script>
    					</td>
    					<td>
    						<script>document.write(amountFormat(${list.TOTAL_PRICE }));</script>
    					</td>
    				</tr>
    			</c:forEach>
    		</tbody>
		     <tr class="table_list_row1">
		      <td nowrap="nowrap"  >&nbsp;</td>
		      <td nowrap="nowrap" >&nbsp;</td>
		      <td align="right" nowrap="nowrap"  >
		      		<strong>总计： </strong>
		      </td>
		      <td align="center" nowrap="nowrap" >&nbsp;</td>
		      <td align="center" nowrap="nowrap" >&nbsp;</td>
			  <td align="center" nowrap="nowrap" >&nbsp;</td>
		      <td align="center" nowrap="nowrap" >&nbsp;</td>
		      <td align="center" nowrap="nowrap">
		      		<span id='totalSubAmount_span' title="提报总数">
		      			<c:out value="${orderMap.SUB_NUM}" default="0"></c:out>
		      		</span> 
		      </td>
			  <td align="center" nowrap="nowrap" >
			  		<span id='totalCheckedAmount_span' title="审核总数">
			  			<c:out value="${orderMap.CHK_NUM}" default="0"></c:out>
			  		</span>
			  </td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
		      <td align="center" nowrap="nowrap">
		      		<span id='totalPrice_span' title="合计">
		      			<script>document.write(amountFormat(${orderMap.ORDER_PRICE }));</script>
		      		</span> 
		      </td>
		    </tr>
  		</table>	
  
	  <TABLE class=table_query align=center style="margin-top: 2px;">
	    <TBODY>
	      <tr>
	        <TH colSpan=6 noWrap align=left><IMG class=nav src="<%=request.getContextPath()%>/img/subNav.gif"> <a href="javascript:tabDisplayControl('moneyTable')">资金信息</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
	      </TR>
	    </TBODY>
	  </TABLE>
	  <TABLE class=table_query align=center id="moneyTable">
	  	<tr>
			<td>发运申请号：</td>
			<td><span id="orderNo_span"><c:out value="${orderMap.ORDER_NO }" default="0"></c:out></span></td>
			<td>订单类型：</td>
			<td><span id="orderType_span">${orderMap.ORDER_TYPE_NAME }</span></td>
			<td>提报时间：</td>
			<td><span id="orderDate">${orderMap.RAISE_DATE }</span></td>
		</tr>
		<tr>
			<td>经销商代码：</td>
			<td><span id="dealer_code_span">${orderMap.DEALER_ID }</span></td>
			<td>经销商名称：</td>
			<td><span id="dealer_name_span">${orderMap.DEALER_NAME }</span></td>
			<td>发运方式：</td>
			<td><span id="delivery_span">${orderMap.DELIVERY_TYPE_NAME }</span></td>
		</tr>
	    <tr class="tabletitle">
	      <td width="18%" class="table_query_right">订单总价：</td>
	      <td width="20%" align="left" >
	      		<script>document.write(amountFormat(${orderMap.ORDER_PRICE }));</script>
	      </td>
	      <td width="13%" class="table_query_right">账户余额：</td>
	      <td width="20%" align="left" >
				<span id='yfin_total_span'>0</span> 
				<input type="hidden" name="yfun_total" id="yfin_total" value="0"/>
		  </td>
	      <td width="13%" class="table_query_right">
	      		<!-- 可用余额： -->	      
	      </td>
	      <td width="21%" align="left">
	      		<span id='fin_useful_span' style="display:none;">0</span> 
	      		<input type="hidden" id="fin_useful" name="fin_useful" value="0" />
	      </td>
	    </tr>
	    <tr class="tabletitle">
	      <td class="table_query_right" valign="middle">使用返利总额：</td>
	      <td align="left" valign="middle" >
	      		<script>document.write(amountFormat(${orderMap.REBATE_PRICE }));</script>
	      </td>
	      <td class="table_query_right" valign="middle">本单金额：</td>
	      <td align="left" valign="middle" >
	      		<script>document.write(amountFormat(${orderMap.ORDER_YF_PRICE }));</script>
	      </td>
	      <td><span id="preprice_name" style="display:none;">预付款：</span></td>
	    	<td align="left" >
	    		<span id='preprice_span' style="display: none;">
	    			<c:out value="${orderMap.PRE_PRICE }" default="0"></c:out>
	      		<input type="hidden" id="preprice" name="preprice" value="<c:out value="${orderMap.PRE_PRICE }" default="0"></c:out>" />
	      		</span>
	      		<script type="text/javascript">
					if('${orderMap.ORDER_TYPE }' == '<%=Constant.ORDER_TYPE_02 %>') {
						document.getElementById("preprice_span").style.display = "";
						document.getElementById("preprice_name").style.display = "";
					}
	      		</script>
	    	</td>
	    </tr>
	    <tr id="precontent">
	   	 	<td>开票类型： </td>
	      	<td>
	      		<script>document.write(getItemValue('${orderMap.INVO_TYPE }'));</script>
	     	 </td>
	      	<td>开票号：</td>
	      	<td colspan="3">${orderMap.INVOICE_NO }</td>
	    </tr>
	  </TABLE>

	  <table id="rebateTable" class="table_list" style="margin-top: 3px;">
	    <tbody id='tbody3'>
	      <tr>
	        <th colspan=11 align=left>
	        	<img class=nav src="<%=request.getContextPath()%>/img/subNav.gif" />
	        	使用返利明细 
	       	</th>
	      </tr>
	      <tr class="table_list_th">
	        <th nowrap="nowrap">返利名称</th>
	        <th nowrap="nowrap">冲减方式</th>
	        <th nowrap="nowrap">返利日期</th>
	        <th nowrap="nowrap">返利额度</th>
	        <th nowrap="nowrap">已用额度</th>
	        <th nowrap="nowrap">可用额度</th>
	        <th nowrap="nowrap">使用额度</th>
	        <th nowrap="nowrap">折后金额</th>
	        <th nowrap="nowrap">备注</th>
	      </tr>
        </tbody>
	        <tbody id="tbody2">
	        	<c:forEach items="${rebateList }" var="list">
	        		<tr class="table_list_row2">
	        			<td>${list.DIS_ITEM }</td>
	        			<td>
	        				<script type="text/javascript">
	        					document.write(getItemValue('${list.USE_TYPE }'));
	        				</script>
	        			</td>
	        			<td>${list.CREATE_DATE }</td>
	        			<td>${list.TOTAL_AMOUNT }</td>
	        			<td>${list.USED_AMOUNT }</td>
	        			<td>${list.REB_AMOUNT }</td>
	        			<td>${list.USE_AMOUNT }</td>
	        			<td>${list.DISCOUNT_REBATE }</td>
	        			<td>${list.REMARK }</td>
	        		</tr>
	        	</c:forEach>
	        </tbody>
	  </table>
	  <table class=table_query align=center>
			<tr>
				<td>收货方：</td>
				<td>
					<span id="rec_dealer_name_span">
						${orderMap.REC_DEALER_NAME }
					</span>
				</td>
				<td><span id="fleetName_span" style="display:none;">大客户：</span></td>
				<td>
					<span id="fleetName" style="display:none;">
						${orderMap.FLEET_NAME }
					</span>
					<script type="text/javascript">
						if('${orderMap.ORDER_TYPE }' == '<%=Constant.ORDER_TYPE_02 %>' || '${orderMap.ORDER_TYPE }' == '<%=Constant.ORDER_TYPE_05 %>') {
							document.getElementById("fleetName_span").style.display = '';
							document.getElementById("fleetName").style.display = '';
						}
					</script>
				</td>
			</tr>
			<tr>
				<td>运送地点：</td>
				<td>
					<span id="address_span">${orderMap.ADDRESS }</span>
				</td>
				<td>收车经销商：</td>
				<td>
					<span id="rec_unit_span">${orderMap.REC_DEALER_NAME }</span>
				</td>
			</tr>
			<tr>
				<td>联系人：</td>
				<td>
					<span id="linkMan">${orderMap.LINK_MAN }</span>
				</td>
				<td>联系电话：</td>
				<td>
					<span id="tel">${orderMap.TEL }</span>
				</td>
			</tr>
			<tr>
				<td>备注说明：</td>
				<td colspan="3">
					<span id="remark">${orderMap.ORDER_REMARK }</span>
				</td>
			</tr>
	  </table>
      <table class="table_list" style="margin-top: 3px;">
	    <tbody>
	      <tr class="table_list_th">
	        <th nowrap="nowrap">序号</th>
	        <th nowrap="nowrap">审核日期</th>
	        <th nowrap="nowrap">审核单位</th>
	        <th nowrap="nowrap">审核人</th>
	        <th nowrap="nowrap">审核结果</th>
	        <th nowrap="nowrap">审核描述</th>
	      </tr>
        </tbody>
	    <tbody id="tbody3">
	    	<c:forEach items="${resultList }" var="list" varStatus="m">
	    		<tr class="table_list_row2">
	    			<td nowrap="nowrap">${m.count }</td>
	    			<td nowrap="nowrap">${list.CREATE_DATE } </td>
	    			<td nowrap="nowrap">${list.ORG_NAME } </td>
	    			<td nowrap="nowrap">${list.NAME } </td>
	    			<td nowrap="nowrap">
	    				<script type="text/javascript">
	    					document.write(getItemValue('${list.CHECK_STATUS}'));
	    				</script>
	    			</td>
	    			<td nowrap="nowrap">${list.CHECK_DESC } </td>
	    		</tr>
	    	</c:forEach>
	    </tbody>
	  </table>
	 <table class="table_list" style="margin-top: 3px;">
	 	<tr>
	 		<td height="50"><input type="button" class="cssbutton" value="返回" onclick="history.go(-1)"/></td>
	 	</tr>
	 </table>
	  <c:if test="${orderMap != null }">
	  		<script>
	  			loadAccountInfo();
	  		</script>
	  </c:if>
</form>
</body>
</html>

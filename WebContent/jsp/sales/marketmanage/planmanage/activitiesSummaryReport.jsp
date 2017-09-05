<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%> 
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	List calist=(List)request.getAttribute("capList");
	List melist=(List)request.getAttribute("mediaList");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>活动总结提报</title>
<% String contextPath = request.getContextPath();  %>
<script type="text/javascript">
<!--
function getTotalPrice_noTax(nId, nType, nPrice) {
	spanNoTax = document.getElementById(nId) ;

	if(nType == <%= Constant.COST_TYPE_02%> || nType == <%= Constant.COST_TYPE_03%>) {
		spanNoTax.innerText = amountFormat(nPrice / parseFloat(<%= Constant.ACTIVITIES_TAX%>)) ;
	} else {
		spanNoTax.innerText = amountFormat(nPrice) ;
	}
}

function getRateCount(spanId) {
	var iTotalPrice_A = document.getElementById(arguments[1]).innerHTML.replace(/,/g, "") ;
	var iTotalPrice_B = document.getElementById(arguments[2]).innerHTML.replace(/,/g, "") ;
	
	var totalPrice = Number(iTotalPrice_A) + Number(iTotalPrice_B) ;
	
	document.getElementById(spanId).innerHTML = amountFormat(totalPrice) ;
}
//-->
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 活动方案管理&gt; 活动总结提报</div>
<form method="post" name="fm" id="fm">
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;方案信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="5" align="left">&nbsp;方案说明</th>
		</tr>
		<tr>
			<td align="right">品牌：</td>
			<td align="left">${groupName}</td>
			<td align="right">活动车型：</td>
			<td align="left">${planMap.PROD_NAME}</td>
			<td width="20%"></td>
		</tr>
		<tr>
			<td align="right">车厂方案编号：</td>
			<td align="left">${planMap.CAMPAIGN_NO}</td>
			<td align="right">方案名称：</td>
			<td align="left">${planMap.CAMPAIGN_NAME}</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">计划开始日期：</td>
			<td align="left">${planMap.START_DATE}</td>
			<td align="right">计划结束日期：</td>
			<td align="left">${planMap.END_DATE}</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">活动主题：</td>
			<td colspan="4" align="left">${planMap.CAMPAIGN_SUBJECT}</td>
		</tr>
		<tr>
			<td align="right">活动对象：</td>
			<td colspan="4" align="left">${planMap.CAMPAIGN_OBJECT}</td>
		</tr>
		<tr>
			<td align="right">活动目的：</td>
			<td colspan="4" align="left">
				${planMap.CAMPAIGN_PURPOSE}
			</td>
		</tr>
		<tr>
			<td align="right">活动要求：</td>
			<td colspan="4" align="left">
				${planMap.CAMPAIGN_NEED}
			</td>
		</tr>
		<tr>
			<td align="right">活动主要内容：</td>
			<td colspan="4" align="left">
				${planMap.CAMPAIGN_DESC}
			</td>
		</tr>
		<tr>
			<td align="right">活动地点说明：</td>
			<td colspan="4" align="left">
				${planMap.EXEC_ADD_DESC}
			</td>
		</tr>
	</table>
	<table id="table1" width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="5" align="left">&nbsp;方案附件</th>
		</tr>
		<tr align="center">
			<td>附件名称</td>
			<td>操作</td>
		</tr>
		<c:forEach items="${attachmentList}" var="list4">
			<tr class="table_list_row2" align="center">
				<td>${list4.FILENAME}</td>
				<td><a target="#" href="${list4.FILEURL}">[下载]</a></td>
			</tr>
		</c:forEach>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;费用类别</div>
	<table class="table_query" align="center">
		<tr class= "tabletitle">
			<th align = "right" width="10%" colspan="1">
			<label for="costType">费用类型：</label>
			</th>
			<th align = "left" colspan="2">
				<strong>
				<script type="text/javascript">
						writeItemValue('${type }');
				</script>
				</strong>
				<input type="hidden" name="costType" value="${type }" />
			</th>
			<th align="right"><strong>费用合计（元）：<font color="red"><span id="rateNoTax">0</span></font></strong>
			<th align="right"><strong>费用合计（元）[含税]：<font color="red"><span id="rateTax">0</span></font></strong></th>
		</tr>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="12" align="left">&nbsp;活动费用信息</th>
		</tr>
		<tr>
			<td align="center">省份</td>
			<td align="center">活动类型</td>
			<td align="center">开始日期</td>
			<td align="center">结束日期</td>
			<td align="center">活动主题</td>
			<td align="center">项目明细</td>
			<td align="center">规格/单位</td>
			<td align="center">项目单价（元）</td>
			<td align="center">项目数量</td>
			<td align="center">计划费用（元）</td>
			<td align="center">计划费用（元）[含税]</td>
			<td align="center">实际费用</td>
		</tr>
		<c:forEach items="${capList}" var="list1" varStatus="step">
			<tr class="table_list_row2" align="center">
				<td>
					<script type="text/javascript">
						writeRegionName('<c:out value="${list1.REGION}"/>') ;
					</script>
				</td>
				<td>
					<script type="text/javascript">
						writeItemValue('${list1.ACTIVITY_TYPE}');
					</script>
				</td>
				<td>${list1.START_DATE}</td>
				<td>${list1.END_DATE}</td>
				<td>${list1.ACTIVITY_CONTENT}</td>
				<td>${list1.ITEM_NAME}</td>
				<td>${list1.ITEM_REMARK}</td>
				<td>${list1.ITEM_PRICE}</td>
				<td>${list1.ITEM_COUNT}</td>
				<td><script>document.write(${list1.ITEM_COUNT} * ${list1.ITEM_PRICE})</script></td>
				<td>
					${list1.PLAN_COST}<input name="planCost1" type="hidden" value="${list1.PLAN_COST}"/>
				</td>
				<td>
					<input type="text" name="CAM${list1.COST_ID }"  id="CA${step.index }" <c:if test="${list1.REAL_COST != 0}">value="${list1.REAL_COST}"</c:if><c:if test="${list1.REAL_COST == 0}">value="${list1.PLAN_COST}"</c:if>" size="5" onchange="addTotal11();" datatype="1,is_double,8"/>
					<input name="CA_REAL_COST" type="hidden" value="${list1.REAL_COST}"/>
				</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="9" align="right"><strong>费用小计：</strong></td>
			<td align="center"><strong><span id="noTaxActive"></span></strong></td>
			<td align="center"><strong><span id="totalPrice1"></span></strong></td>
			<td align="center"><strong><span id="totalPrice11"></span></strong></td>
		</tr>
	</table>
	
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="15" align="left">&nbsp;媒体投放费用信息</th>
		</tr>
		<tr>
			<td align="center">省份</td>
			<td align="center">车型品牌</td>
			<td align="center">宣传主题</td>
			<td align="center">媒体类型</td>
			<td align="center">媒体名称</td>
			<td align="center">广告日期</td>
			<td align="center">栏目/套装</td>
			<td align="center">刊发位置/播出时段</td>
			<td align="center">规格/版式</td>
			<td align="center">单日频次</td>
			<td align="center">总次数</td>
			<td align="center">结算单价 元/次</td>
			<td align="center">计划费用（元）</td>
			<td align="center">计划费用（元）[含税]</td>
			<td align="center">实际费用</td>
		</tr>
		<c:forEach items="${mediaList}" var="list2" varStatus="step">
			<tr class="table_list_row2" align="center">
				<td>
						<script type="text/javascript">
							writeRegionName('<c:out value="${list2.REGION}"/>') ;
						</script>
					</td>
					<td>
						<label>
							<script type="text/javascript">
								writeItemValue("${list2.MEDIA_MODEL}") ;
							</script>
						</label>
					</td>
					<td>
						${list2.ADV_SUBJECT}
					</td>
					<td>
						<label>
							<script type="text/javascript">
								writeItemValue("${list2.MEDIA_TYPE}") ;
							</script>
						</label>
					</td>
					<td>
						${list2.MEDIA_NAME}
					</td>
					<td>
						${list2.ADV_DATE}~${list2.END_DATE}
					</td>
					<td>
						${list2.MEDIA_COLUMN}
					</td>
					<td>
						${list2.MEDIA_PUBLISH}
					</td>
					<td>
						${list2.MEDIA_SIZE}
					</td>
					<td>
						${list2.ITEM_COUNT}
					</td>
					<td>
						${list2.TOTAL_COUNT}
					</td>
					<td>
						${list2.ITEM_PRICE}
					</td>
					<td><script>document.write(${list2.TOTAL_COUNT} * ${list2.ITEM_PRICE})</script></td>
				<td>
					${list2.PLAN_COST}<input name="planCost2" type="hidden" value="${list2.PLAN_COST}"/>
				</td>
				<td>
					<input type="text" name="MED${list2.COST_ID }" id="ME${step.index }" <c:if test="${list2.REAL_COST != 0}">value="${list2.REAL_COST}"</c:if><c:if test="${list2.REAL_COST == 0}">value="${list2.PLAN_COST}"</c:if> size="5" onchange="addTotal21();" datatype="1,is_double,8"/>
					<input name="ME_REAL_COST" type="hidden" value="${list2.REAL_COST}"/>
				</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="12" align="right"><strong>费用小计：</strong></td>
			<td align="center"><strong><span id="noTaxMedia"></span></strong></td>
			<td align="center"><strong><span id="totalPrice2"></span></strong></td>
			<td align="center"><strong><span id="totalPrice21"></span></strong></td>
		</tr>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="5" align="left">
				&nbsp;目标信息
				<input type="hidden" name="TARGET_ID" value="${targetMap.TARGET_ID}" />
			</th>
		</tr>
		<tr>
			<td align="center" width="50%">项目</td>
			<td align="center">目标</td>
			<td align="center">实际完成</td>
		</tr>
		<tr class="table_list_row2">
			<td align="center">来电来店数</td>
			<td align="center">${targetMap.CALLS_HOUSES_CNT_TGT}</td>
			<td align="center">
				<input type="text" name="CALLS_HOUSES_CNT_ACT" id="CALLS_HOUSES_CNT_ACT" <c:if test="${targetMap.CALLS_HOUSES_CNT_ACT == 0 }">value="${targetMap.CALLS_HOUSES_CNT_TGT}"</c:if><c:if test="${targetMap.CALLS_HOUSES_CNT_ACT != 0 }">value="${targetMap.CALLS_HOUSES_CNT_ACT}"</c:if> size="5" datatype="1,is_digit,4"/>
			</td>
		</tr>
		<tr class="table_list_row2">
			<td align="center">信息留存量</td>
			<td align="center">${targetMap.RESERVE_CNT_TGT}</td>
			<td align="center">
				<input type="text" name="RESERVE_CNT_ACT" id="RESERVE_CNT_ACT" <c:if test="${targetMap.RESERVE_CNT_ACT == 0 }">value="${targetMap.RESERVE_CNT_TGT}"</c:if><c:if test="${targetMap.RESERVE_CNT_ACT != 0 }">value="${targetMap.RESERVE_CNT_ACT}"</c:if> size="5" datatype="1,is_digit,4"/>
			</td>
		</tr>
		<tr class="table_list_row2">
			<td align="center">启票数</td>
			<td align="center">${targetMap.ORDER_CNT_TGT}</td>
			<td align="center">
				<input type="text" name="ORDER_CNT_ACT" id="ORDER_CNT_ACT" <c:if test="${targetMap.ORDER_CNT_ACT == 0 }">value="${targetMap.ORDER_CNT_TGT}"</c:if><c:if test="${targetMap.ORDER_CNT_ACT != 0 }">value="${targetMap.ORDER_CNT_ACT}"</c:if>  size="5" datatype="1,is_digit,4"/>
			</td>
		</tr>
		<tr class="table_list_row2">
			<td align="center">实销数</td>
			<td align="center">${targetMap.DELIVERY_CNT_TGT}</td>
			<td align="center">
				<input type="text" name="DELIVERY_CNT_ACT" id="DELIVERY_CNT_ACT" <c:if test="${targetMap.DELIVERY_CNT_ACT == 0 }">value="${targetMap.DELIVERY_CNT_TGT}"</c:if><c:if test="${targetMap.DELIVERY_CNT_ACT != 0 }">value="${targetMap.DELIVERY_CNT_ACT}"</c:if>  size="5" datatype="1,is_digit,4"/>
			</td>
		</tr>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
	        <th colspan="7">&nbsp;总结审批记录</th>
		</tr>
		<tr align="center">
			<td>审批部门</td>
			<td>审批职务</td>
			<td>审批人员</td>
			<td>审批状态</td>
			<td>审批意见</td>
			<td>审批时间</td>
			<td>审批附件</td>
		</tr>
		<c:forEach items="${mycheckList}" var="list5">
			<tr class="table_list_row2" align="center">
				<td>${list5.ORG_NAME}</td>
				<td>${list5.POSE_NAME}</td>
				<td>${list5.NAME}</td>
				<td>
					<script type="text/javascript">
						writeItemValue('${list5.CHECK_STATUS}');
					</script>
				</td>
				<td>${list5.CHECK_DESC}</td>
				<td>${list5.CHECK_DATE}</td>
				<td><a target="_blank" href="${list5.FILEURL}">${list5.FILENAME}</a></td>
			</tr>
		</c:forEach>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	     <tr>
			<td align="right">活动评估：</td>
			<td colspan="4" align="left">
				<textarea name="EVALUATE_DESC" id="campaignDesc" rows="4" cols="70">${planMap.EVALUATE_DESC}</textarea>
			</td>
		</tr>
		<tr>
			<td align="right">建议及整改措施：</td>
			<td colspan="4" align="left">
				<textarea name="ADVICE_DESC" id="execAddDesc" rows="4" cols="70" >${planMap.ACTIVICE_SUMMARY_DESC}</textarea>
			</td>
		</tr>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
	        <th colspan="3">附件列表：<input type="hidden" id="fjids" name="fjids"/>
				<span>
					<input type="button" class="cssbutton"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
				</span>
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
		<tr>
			<td colspan="4" align="center">
				<input type="hidden" name="campaignId" value="${planMap.CAMPAIGN_ID}" />
				<input type="hidden" name="executeId" value="${planMap.EXECUTE_ID}" />
				<input type="button" class="cssbutton" name="button5" onClick="toConfirm();" value="上报"/>
				<input type="button" class="cssbutton" name="button4" onClick="toBack();" value="返回"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	//清除功能
	function toClear(){
		document.getElementById("campaignModel").value="";
		document.getElementById("modelId").value="";
	}
	//提报提交
	function toConfirm(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/marketmanage/planmanage/ActivitiesSummaryReport/showActivitiesSummaryReport.json',showResult,'fm');
	}
	//返回方法
	function toBack(){
		$('fm').action='<%=request.getContextPath()%>/sales/marketmanage/planmanage/ActivitiesSummaryReport/activitiesSummaryReportInit.do';
		$('fm').submit();
	}
	//回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("操作成功！");
			$('fm').action='<%=request.getContextPath()%>/sales/marketmanage/planmanage/ActivitiesSummaryReport/activitiesSummaryReportInit.do';
			$('fm').submit();
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
//页面初始化
	function doInit(){
		totalPrice();
		realCostTotalPrice();
		addTotal11() ;
		addTotal21() ;
		
		getRateCount("rateNoTax", "noTaxActive", "noTaxMedia") ;
		getRateCount("rateTax", "totalPrice1", "totalPrice2") ;
	}
	//费用合计
	function totalPrice(){
		var planCost1 = document.getElementsByName("planCost1");
		var planCost2 = document.getElementsByName("planCost2");
		var totalPrice1 = 0;
		var totalPrice2 = 0;
		
		for (var i=0; i<planCost1.length; i++){  
			totalPrice1 += Number(planCost1[i].value);  
		}
		
		for (var i=0; i<planCost2.length; i++){  
			totalPrice2 += Number(planCost2[i].value);  
		}
		
		document.getElementById("totalPrice1").innerText = amountFormat(totalPrice1);
		document.getElementById("totalPrice2").innerText = amountFormat(totalPrice2);
		
		getTotalPrice_noTax("noTaxMedia", ${type}, totalPrice2) ;
		getTotalPrice_noTax("noTaxActive", ${type}, totalPrice1) ;
	}
	
	function realCostTotalPrice(){
		var planCost11 = document.getElementsByName("CA_REAL_COST");
		var planCost21 = document.getElementsByName("ME_REAL_COST");
		var totalPrice11 = 0;
		var totalPrice21 = 0;
		
		for (var i=0; i<planCost11.length; i++){  
			totalPrice11 += Number(planCost11[i].value);  
		}
		for (var i=0; i<planCost21.length; i++){  
			totalPrice21 += Number(planCost21[i].value);  
		}
		document.getElementById("totalPrice11").innerText = amountFormat(totalPrice11);
		document.getElementById("totalPrice21").innerText = amountFormat(totalPrice21);
	}
	//活动实际费用
	function addTotal11(){
		var totalPrice11 = 0;
		var len=<%=calist.size()%>
		var obj;
	    for(var i=0;i<len;i++){
	    	obj=document.getElementById("CA"+i);
	    	totalPrice11+=Number(obj.value);
	    }
		document.getElementById("totalPrice11").innerText = amountFormat(totalPrice11);
	}
	//媒体实际费用
	function addTotal21(){
		var totalPrice21 = 0;
		var len=<%=melist.size()%>
		var obj;
	    for(var i=0;i<len;i++){
	    	obj=document.getElementById("ME"+i);
	    	totalPrice21+=Number(obj.value);
	    }
		document.getElementById("totalPrice21").innerText = amountFormat(totalPrice21);
	}

	//设置可用费用
		function getAvailableAmount(){
			var dealerId = document.getElementById("dealerId").value;
			<c:forEach items="${amountList1}" var="list1">
	        var id=<c:out value="${list1.DEALER_ID}"/>
	        if(dealerId==id){
	        	var availableAmount1=<c:out value="${list1.AVAILABLE_AMOUNT}"/>
	       	    document.getElementById("availableAmount1").innerText= amountFormat

	(availableAmount1);
	       	    document.getElementById("availableAmount5").value= availableAmount1;
	        }
			</c:forEach>
			<c:forEach items="${amountList2}" var="list2">
		       	var availableAmount2=<c:out value="${list2.AVAILABLE_AMOUNT}"/>
		      	document.getElementById("availableAmount2").innerText= amountFormat

	(availableAmount2);
		      	document.getElementById("availableAmount6").value= availableAmount2;
			</c:forEach>
			<c:forEach items="${amountList3}" var="list3">
				var type=<c:out value="${list3.COST_TYPE}"/>
				if(type==<%=Constant.COST_TYPE_01%>){
			       	var availableAmount3=<c:out value="${list3.AVAILABLE_AMOUNT}"/>
			      	document.getElementById("availableAmount3").innerText= 

	amountFormat(availableAmount3);
			      	document.getElementById("availableAmount7").value= 

	availableAmount3;
				}
				if(type==<%=Constant.COST_TYPE_02%>){
			       	var availableAmount4=<c:out value="${list3.AVAILABLE_AMOUNT}"/>
			      	document.getElementById("availableAmount4").innerText= 

	amountFormat(availableAmount4);
			      	document.getElementById("availableAmount8").value= 

	availableAmount4;
				}
			</c:forEach>
		}
</script>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<OBJECT classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 id=WebBrowser width=0></OBJECT>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>活动执行方案提报</title>
<% String contextPath = request.getContextPath();  %>
<script type="text/javascript">
</script>
<style media="print">.Noprint { DISPLAY: none }</style>
<style>
 @media print
 {   
  .Noprint{DISPLAY:none;}   
  .PageNext{PAGE-BREAK-AFTER:always}   
 } 
 .STYLE1 {
	font-size: 36px;
	font-weight: bold;
	color: #000000;
 }
.STYLE2 {
	font-size: 18px;
	color: #000000;
}
.Noprint {
	display: none;
}

.PageNext {
	page-break-after: always;
	}
.STYLE3 {font-size: 16px}
.tdp {
	border-bottom: 1 solid #000000;
	border-left: 1 solid #000000;
	border-right: 0 solid #ffffff;
	border-top: 0 solid #ffffff;
}

.tabp {
	border-color: #000000 #000000 #000000 #000000;
	border-style: solid;
	border-top-width: 2px;
	border-right-width: 2px;
	border-bottom-width: 1px;
	border-left-width: 1px;
}

.NOPRINT {
	font-family: "宋体";
	font-size: 9pt;
}
</style>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 活动方案管理&gt; 活动方案明细</div>
<form method="post" name="fm" id="fm">
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;方案信息</div>
	<table id="table1" width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="5" align="left">&nbsp;方案说明</th>
		</tr>
		<tr>
			<td align="right">品牌：</td>
			<td align="left">${map2.GROUP_NAME}</td>
			<td align="right">活动车型：</td>
			<td align="left">${map2.PROD_NAME}</td>
			<td width="20%"></td>
		</tr>
		<tr>
			<td align="right">车厂方案编号：</td>
			<td align="left">${map2.CAMPAIGN_NO}</td>
			<td align="right">方案名称：</td>
			<td align="left">${map2.CAMPAIGN_NAME}</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">计划开始日期：</td>
			<td align="left">${map2.START_DATE}</td>
			<td align="right">计划结束日期：</td>
			<td align="left">${map2.END_DATE}</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">活动主题：</td>
			<td colspan="4" align="left">${map2.CAMPAIGN_SUBJECT}</td>
		</tr>
		<tr>
			<td align="right">活动对象：</td>
			<td colspan="4" align="left">${map2.CAMPAIGN_OBJECT}</td>
		</tr>
		<tr>
			<td align="right">活动目的：</td>
			<td colspan="4" align="left">
				<textarea name="campaignPurpose" id="campaignPurpose" rows="4" cols="70" disabled="disabled">${map2.CAMPAIGN_PURPOSE}</textarea>
			</td>
		</tr>
		<tr>
			<td align="right">活动要求：</td>
			<td colspan="4" align="left">
				<textarea name="campaignNeed" id="campaignNeed" rows="4" cols="70" disabled="disabled">${map2.CAMPAIGN_NEED}</textarea>
			</td>
		</tr>
		<tr>
			<td align="right">活动主要内容：</td>
			<td colspan="4" align="left">
				<textarea name="campaignDesc" id="campaignDesc" rows="4" cols="70" disabled="disabled">${map2.CAMPAIGN_DESC}</textarea>
			</td>
		</tr>
		<tr>
			<th colspan="5" align="left">&nbsp;区域说明：</th>
		</tr>
		<tr>
			<td align="right">活动地点说明：</td>
			<td colspan="4" align="left">
				<textarea name="execAddDesc" id="execAddDesc" rows="4" cols="70" disabled="disabled">${map2.EXEC_ADD_DESC}</textarea>
			</td>
		</tr>
		<tr>
			<td align="right">区域建议及整改意见：：</td>
			<td colspan="4" align="left">
				<textarea name="adviceDesc" id="adviceDesc" rows="4" cols="70" disabled="disabled">${map2.ADVICE_DESC}</textarea>
			</td>
		</tr>
	</table>
	<table id="table1" width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="5" align="left">&nbsp;方案及总结附件</th>
		</tr>
		<tr align="center">
			<td>附件名称</td>
			<td>操作</td>
		</tr>
		<c:forEach items="${list4}" var="list4">
			<tr class="table_list_row2" align="center">
				<td>${list4.FILENAME}</td>
				<td><a target="_blank" href="${list4.FILEURL}">[下载]</a></td>
			</tr>
		</c:forEach>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;执行方案信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="5" align="left">&nbsp;目标信息</th>
		</tr>
		<tr>
			<td align="center">项目</td>
			<td align="center">目标</td>
			<td align="center">实际完成</td>
			<td align="center">完成率</td>
		</tr>
		<tr class="table_list_row2">
			<td align="center">来电来店数</td>
			<td align="center">${map3.CALLS_HOUSES_CNT_TGT}</td>
			<td align="center">${map3.CALLS_HOUSES_CNT_ACT}</td>
			<td align="center">${map3.CALLS_HOUSES_CNT}%</td>
		</tr>
		<tr class="table_list_row2">
			<td align="center">信息留存量</td>
			<td align="center">${map3.RESERVE_CNT_TGT}</td>
			<td align="center">${map3.RESERVE_CNT_ACT}</td>
			<td align="center">${map3.RESERVE_CNT}%</td>
		</tr>
		<tr class="table_list_row2">
			<td align="center">启票数</td>
			<td align="center">${map3.ORDER_CNT_TGT}</td>
			<td align="center">${map3.ORDER_CNT_ACT}</td>
			<td align="center">${map3.ORDER_CNT}%</td>
		</tr>
		<tr class="table_list_row2">
			<td align="center">实销数</td>
			<td align="center">${map3.DELIVERY_CNT_TGT}</td>
			<td align="center">${map3.DELIVERY_CNT_ACT}</td>
			<td align="center">${map3.DELIVERY_CNT}%</td>
		</tr>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<td align="right">活动评估：</td>
			<td colspan="4" align="left">
				<textarea name="evaluateDesc" id="evaluateDesc" rows="4" cols="70" disabled="disabled">${map2.EVALUATE_DESC}</textarea>
			</td>
		</tr>
		<tr>
			<td align="right">建议及整改措施：</td>
			<td colspan="4" align="left">
				<textarea name="adviceDesc" id="adviceDesc" rows="4" cols="70" disabled="disabled">${map2.ADVICE_DESC}</textarea>
			</td>
		</tr>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="11" align="left">&nbsp;活动费用信息</th>
		</tr>
		<tr>
			<td align="center">费用类型</td>
			<td align="center">费用来源</td>
			<td align="center">活动类型</td>
			<td align="center">活动主题</td>
			<td align="center">项目明细</td>
			<td align="center">规格/单位</td>
			<td align="center">项目单价（元）</td>
			<td align="center">项目数量</td>
			<td align="center">计划费用（元）</td>
			<td align="center">实际费用</td>
			<td align="center">补贴费用</td>
		</tr>
		<c:forEach items="${list1}" var="list1">
			<tr class="table_list_row2" align="center">
				<td>
					<script type="text/javascript">
						writeItemValue('${list1.COST_TYPE}');
					</script>
				</td>
				<td>
					<script type="text/javascript">
						writeItemValue('${list1.COST_ACCOUNT}');
					</script>
				</td>
				<td>
					<script type="text/javascript">
						writeItemValue('${list1.ACTIVITY_TYPE}');
					</script>
				</td>
				<td>${list1.ACTIVITY_CONTENT}</td>
				<td>${list1.ITEM_NAME}</td>
				<td>${list1.ITEM_REMARK}</td>
				<td>${list1.ITEM_PRICE}</td>
				<td>${list1.ITEM_COUNT}</td>
				<td>
					${list1.PLAN_COST}<input name="planCost1" type="hidden" value="${list1.PLAN_COST}"/>
				</td>
				<td>
					${list1.REAL_COST}<input name="realCost1" type="hidden" value="${list1.REAL_COST}"/>
				</td>
				<td>
					${list1.ITEM_COST}<input name="itemCost1" type="hidden" value="${list1.ITEM_COST}"/>
				</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="6" align="right"><strong>费用小计：</strong></td>
			<td align="center"><strong><span id="totalPrice1"></span></strong></td>
			<td align="center"><strong><span id="totalPrice2"></span></strong></td>
			<td align="center"><strong><span id="totalPrice3"></span></strong></td>
			<td align="left"></td>
		</tr>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="11" align="left">&nbsp;媒体投放费用信息</th>
		</tr>
		<tr>
			<td align="center">费用类型</td>
			<td align="center">广告日期</td>
			<td align="center">广告主题</td>
			<td align="center">媒体</td>
			<td align="center">规格/版式大小/电视片时间</td>
			<td align="center">结算价格</td>
			<td align="center">频次/数量</td>
			<td align="center">费用来源</td>
			<td align="center">计划费用</td>
			<td align="center">实际费用</td>
			<td align="center">补贴费用</td>
		</tr>
		<c:forEach items="${list2}" var="list2">
			<tr class="table_list_row2" align="center">
				<td>
					<script type="text/javascript">
						writeItemValue('${list2.COST_TYPE}');
					</script>
				</td>
				<td>${list2.ADV_DATE}</td>
				<td>${list2.ADV_SUBJECT}</td>
				<td>${list2.ADV_MEDIA}</td>
				<td>${list2.REMARK}</td>
				<td>${list2.ITEM_PRICE}</td>
				<td>${list2.ITEM_COUNT}</td>
				<td>
					<script type="text/javascript">
						writeItemValue('${list2.PAYMENT_ACCOUNT}');
					</script>
				</td>
				<td>
					${list2.PLAN_COST}<input name="planCost2" type="hidden" value="${list2.PLAN_COST}"/>
				</td>
				<td>
					${list2.REAL_COST}<input name="realCost2" type="hidden" value="${list2.REAL_COST}"/>
				</td>
				<td>
					${list2.ITEM_COST}<input name="itemCost2" type="hidden" value="${list2.ITEM_COST}"/>
				</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="8" align="right"><strong>费用小计：</strong></td>
			<td align="center"><strong><span id="totalPrice4"></span></strong></td>
			<td align="center"><strong><span id="totalPrice5"></span></strong></td>
			<td align="center"><strong><span id="totalPrice6"></span></strong></td>
			<td align="left"></td>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;审批信息</div>
	
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
	        <th colspan="7">&nbsp;方案审批记录</th>
		</tr>
		<tr align="center">
			<td>审批部门</td>
			<td>审批人员</td>
			<td>审批状态</td>
			<td>审批意见</td>
			<td>审批时间</td>
			<td>审批附件</td>
		</tr>
		<c:forEach items="${list3}" var="list3">
			<tr class="table_list_row2" align="center">
				<td>${list3.ORG_NAME}</td>
				<td>${list3.NAME}</td>
				<td>
					<script type="text/javascript">
						writeItemValue('${list3.CHECK_STATUS}');
					</script>
				</td>
				<td>${list3.CHECK_DESC}</td>
				<td>${list3.CHECK_DATE}</td>
				<td><a target="_blank" href="${list3.FILEURL}">${list3.FILENAME}</a></td>
			</tr>
		</c:forEach>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
	        <th colspan="7">&nbsp;总结审批记录</th>
		</tr>
		<tr align="center">
			<td>审批部门</td>
			<td>审批人员</td>
			<td>审批状态</td>
			<td>审批意见</td>
			<td>审批时间</td>
			<td>审批附件</td>
		</tr>
		<c:forEach items="${list5}" var="list5">
			<tr class="table_list_row2" align="center">
				<td>${list5.ORG_NAME}</td>
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
		<tr class="noprint">
			<td colspan="7" align="center">
				<input type="hidden" name="campaignId" value="${map2.CAMPAIGN_ID}" />
				<input type="hidden" name="dealerId" value="${map2.DEALRE_ID}" />
				<input type="hidden" name="executeId" value="${map2.EXECUTE_ID}" />
				<input type="hidden" name="flag" id="flag"/>
				<input class="cssbutton" onclick="document.all.WebBrowser.ExecWB(6,1)" type="button" value="打印">
				<input class="cssbutton" onclick="document.all.WebBrowser.ExecWB(6,6)" type="hidden" value="直接打印"> 
				<input class="cssbutton" onclick="document.all.WebBrowser.ExecWB(8,1)" type="button" value="页面设置"> 
				<input class="cssbutton" onclick="document.all.WebBrowser.ExecWB(7,1)" type="button" value="打印预览">&nbsp;
				<INPUT class="cssbutton" type="button" value="关闭窗口" onclick="javascript:window.close();"> 
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
function printStat(){
	var campaignId=${campaignId}
	var executeId=${executeId}
	window.open('<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesPlanSearch/activitiesMyPlanSearchDetail.do?&campaignId='+campaignId+'&executeId='+executeId,"活动信息打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
}
	//页面初始化
	function doInit(){
		totalPrice();
	}
	//费用合计
	function totalPrice(){
		var planCost1 = document.getElementsByName("planCost1");
		var realCost1 = document.getElementsByName("realCost1");
		var itemCost1 = document.getElementsByName("itemCost1");
		var planCost2 = document.getElementsByName("planCost2");
		var realCost2 = document.getElementsByName("realCost2");
		var itemCost2 = document.getElementsByName("itemCost2");
		var totalPrice1 = 0;
		var totalPrice2 = 0;
		var totalPrice3 = 0;
		var totalPrice4 = 0;
		var totalPrice5 = 0;
		var totalPrice6 = 0;
		for (var i=0; i<planCost1.length; i++){  
			totalPrice1 += Number(planCost1[i].value);  
			totalPrice2 += Number(realCost1[i].value);
			totalPrice3 += Number(itemCost1[i].value);
		}
		for (var i=0; i<planCost2.length; i++){  
			totalPrice4 += Number(planCost2[i].value);
			totalPrice5 += Number(realCost2[i].value);
			totalPrice6 += Number(itemCost2[i].value); 
		}
		document.getElementById("totalPrice1").innerText = amountFormat(totalPrice1);
		document.getElementById("totalPrice2").innerText = amountFormat(totalPrice2);
		document.getElementById("totalPrice3").innerText = amountFormat(totalPrice3);
		document.getElementById("totalPrice4").innerText = amountFormat(totalPrice4);
		document.getElementById("totalPrice5").innerText = amountFormat(totalPrice5);
		document.getElementById("totalPrice6").innerText = amountFormat(totalPrice6);
	}
	//返回方法
	function toBack(){
		$('fm').action='<%=request.getContextPath()%>/sales/marketmanage/planmanage/ActivitiesPlanSearch/activitiesPlansSearchInit.do';
		$('fm').submit();
	}
</script>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%

	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>索赔申请单结算</title>
<script type="text/javascript">
function doInit(){
	isCheck();
}
	    var myPage;
		//查询路径
		var url = "<%=contextPath%>/claim/application/DealerNewKp/dealerKpStatisQuery.json";
		//标题			
		var title = null;
	    //显示列表控制
		var columns = [															
						{header: "序号", width:'20%',renderer:getIndex},			
						{header: "索赔单数", width:'10%', dataIndex: 'CLAIM_NUM'},
						{header: "工时金额(元)", width:'5%', dataIndex: 'HOURS_SETTLEMENT_AMOUNT'},
						{header: "配件金额(元)", width:'15%', dataIndex: 'PART_SETTLEMENT_AMOUNT'},						
						{header: "PDI金额(元)", width:'5%', dataIndex: 'PDI_SETTLEMENT_AMOUNT'},						
						{header: "保养费用(元)", width:'5%', dataIndex: 'FIRST_SETTLEMENT_AMOUNT'},
						{header: "服务活动费用(元)", width:'5%', dataIndex: 'ACTIVITIE_SETTLEMENT_AMOUNT'},
						{header: "外出费用(元)", width:'5%', dataIndex: 'OUTWARD_SETTLEMENT_AMOUNT'},
						{header: "旧件运费(元)", width:'5%', dataIndex: 'AUTH_PRICE'},
						{header: "正负激励(元)", width:'5%', dataIndex: 'PN_EXACTION'},
						{header: "善于索赔费用(元)", width:'5%', dataIndex: 'GOOD_CLAIM_AMOUNT'},
						//{header: "二次抵扣(元)", width:'5%', dataIndex: 'AMOUNT_SUM'},
						{header: "上次行政扣款(元)", width:'5%', dataIndex: 'LAST_ADMINISTRATION_AMOUNT'},
						{header: "本次行政扣款(元)", width:'5%', dataIndex: 'THIS_ADMINISTRATION_AMOUNT'},
						{id:'action',header: "索赔结算总金额(元)", width:'5%', dataIndex: 'CLAIM_AMOUNT',renderer:myLink}
				
			      ];	
function myLink(value,metaData,record){
	var v1=accAdd(record.data.HOURS_SETTLEMENT_AMOUNT,record.data.PART_SETTLEMENT_AMOUNT);	
	var v2=accAdd(v1,record.data.PDI_SETTLEMENT_AMOUNT);
	var v3=accAdd(v2,record.data.FIRST_SETTLEMENT_AMOUNT);
	var v4=accAdd(v3,record.data.ACTIVITIE_SETTLEMENT_AMOUNT);
	var v5=accAdd(v4,record.data.OUTWARD_SETTLEMENT_AMOUNT);
	var v6=accAdd(v5,record.data.PN_EXACTION);
	var v7=accAdd(v6,record.data.GOOD_CLAIM_AMOUNT);
	var v8=accAdd(v7,-1*record.data.LAST_ADMINISTRATION_AMOUNT);
	var v9=accAdd(v8,record.data.AUTH_PRICE);
	//var v10=accAdd(v9,-1*record.data.AMOUNT_SUM);
	document.getElementById("c_s_amount").value=v9;
	document.getElementById("amount").value=record.data.CLAIM_AMOUNT;

	document.getElementById("CLAIM_NUM").value=record.data.CLAIM_NUM;//索赔单数
	document.getElementById("HOURS_SETTLEMENT_AMOUNT").value=record.data.HOURS_SETTLEMENT_AMOUNT;
	document.getElementById("PART_SETTLEMENT_AMOUNT").value=record.data.PART_SETTLEMENT_AMOUNT;
	document.getElementById("PDI_SETTLEMENT_AMOUNT").value=record.data.PDI_SETTLEMENT_AMOUNT;
	document.getElementById("FIRST_SETTLEMENT_AMOUNT").value=record.data.FIRST_SETTLEMENT_AMOUNT;
	document.getElementById("ACTIVITIE_SETTLEMENT_AMOUNT").value=record.data.ACTIVITIE_SETTLEMENT_AMOUNT;
	document.getElementById("PN_EXACTION").value=record.data.PN_EXACTION;
	document.getElementById("OUTWARD_SETTLEMENT_AMOUNT").value=record.data.OUTWARD_SETTLEMENT_AMOUNT;
	document.getElementById("GOOD_CLAIM_AMOUNT").value=record.data.GOOD_CLAIM_AMOUNT;
	document.getElementById("LAST_ADMINISTRATION_AMOUNT").value=record.data.LAST_ADMINISTRATION_AMOUNT;
	document.getElementById("THIS_ADMINISTRATION_AMOUNT").value=record.data.THIS_ADMINISTRATION_AMOUNT;
	document.getElementById("AUTH_PRICE").value=record.data.AUTH_PRICE;
	//document.getElementById("AMOUNT_SUM").value=record.data.AMOUNT_SUM;

	return String.format(value);
}
//结算操作
function balanceView() {			
	var canDoIt=document.getElementById("canDoIt").value;
	if(canDoIt=='NO'){
		MyAlert('没有满足条件的索赔单!');
		return;
	}
	//点开票先验证	
	var CSAmount = document.getElementById("c_s_amount").value;
	var amount = document.getElementById("amount").value;
	if(CSAmount!=amount){
		MyAlert('索赔单各项金额之和与索赔结算总金额不等!');
		return;
	}else{
		if(submitForm('fm')){
			MyConfirm("确认提交?", settlement);	
		}
	}
}
//验证通过才进行开票
function settlement(){		
	makeNomalFormCall("<%=contextPath%>/claim/application/DealerNewKp/doBalanceKp.json",callValue,'fm','saveBtn1');
}		
function callValue(json)
{
	if(json.msg=="0"){
		MyConfirm("操作成功！",backToQuery);
	}else{
		MyAlert("操作失败！请与管理员联系！");
	}
}
//返回结算单查询页面
function backToQuery(){
	location.href = "<%=contextPath%>/claim/application/DealerNewKp/dealerKpQueryInit.do";
}
//自定义浮点数相加
function accAdd(arg1,arg2){
		    var r1,r2,m,n; 
		    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0} 
		    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0} 
		    m=Math.pow(10,Math.max(r1,r2));
		    n=(r1>=r2)?r1:r2;
		    return ((arg1*m+arg2*m)/m).toFixed(n);  
		} 
	</script>
</head>
<body>
	<div class="navigation">
    	<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;经销商结算上报
    </div>
	<form method="post" name="fm" id="fm">
		<input type="hidden" name="dealerId" id="dealerId" value="${dealerId}" />
		<input type="hidden" name="returnId" id="returnId" value="${returnId }" />
		<input type="hidden" name="canDoIt" id="canDoIt" value="" />
		<input type="hidden" name="c_s_amount" id="c_s_amount"/>
		<input type="hidden" name="amount" id="amount"/>
		<input type="hidden" name="CLAIM_NUM" id="CLAIM_NUM"/><!--索赔单数  -->
		<input type="hidden" name="HOURS_SETTLEMENT_AMOUNT" id="HOURS_SETTLEMENT_AMOUNT"/>
		<input type="hidden" name="PART_SETTLEMENT_AMOUNT" id="PART_SETTLEMENT_AMOUNT"/>
		<input type="hidden" name="PDI_SETTLEMENT_AMOUNT" id="PDI_SETTLEMENT_AMOUNT"/>
		<input type="hidden" name="FIRST_SETTLEMENT_AMOUNT" id="FIRST_SETTLEMENT_AMOUNT"/>
		<input type="hidden" name="ACTIVITIE_SETTLEMENT_AMOUNT" id="ACTIVITIE_SETTLEMENT_AMOUNT"/>
		<input type="hidden" name="OUTWARD_SETTLEMENT_AMOUNT" id="OUTWARD_SETTLEMENT_AMOUNT"/>
		<input type="hidden" name="PN_EXACTION" id="PN_EXACTION"/>
		<input type="hidden" name="GOOD_CLAIM_AMOUNT" id="GOOD_CLAIM_AMOUNT"/>
		<input type="hidden" name="LAST_ADMINISTRATION_AMOUNT" id="LAST_ADMINISTRATION_AMOUNT"/>
		<input type="hidden" name="THIS_ADMINISTRATION_AMOUNT" id="THIS_ADMINISTRATION_AMOUNT"/>
		<input type="hidden" name="AUTH_PRICE" id="AUTH_PRICE"/>
		<input type="hidden" name="AMOUNT_SUM" id="AMOUNT_SUM"/>
		<table align="center" class="table_query">
			<tr>
				<td colspan="6" style="text-align: center;">
				结算通过日期 :
				  <input class="short_txt" style="width: 80px;" id="endBalanceDate" readonly="readonly" value="${sdate }" name="endBalanceDate" datatype="0,is_date,10"
                           maxlength="10" group="endBalanceDate,CON_END_DAY"/>
                    <!-- <input class="time_ico" value=" " onclick="showcalendar(event, 'endBalanceDate', false);" type="button"/> -->
                    至
                    <input class="short_txt" style="width: 80px;" id="CON_END_DAY" readonly="readonly" value="${edate }" name="CON_END_DAY" datatype="0,is_date,10"
                           maxlength="10" group="endBalanceDate,CON_END_DAY"/>
                    <!-- <input class="time_ico" value=" " onclick="showcalendar(event, 'CON_END_DAY', false);" type="button"/> -->
				</td>
			</tr>
			<tr>
				<td colspan="6" style="text-align: center;">
					注：只有旧件全部审核入库通过才能开票！<br/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 截至日期与旧件审核有关，如有异常请核实旧件是否审核！ 
				</td>
			</tr>
			<tr>
				<td colspan="6" style="text-align: center;">
					<input id="queryBtn"
					class="u-button u-query" type="button" value="查询" name="recommit"
					onclick="isCheck();" />
					&nbsp;&nbsp;
					<input id="backBtn" class="normal_btn" type="button" value="返回" name="backBtn"
					onclick="backToQuery();" />
				</td>
			</tr>
		</table>
    <!-- 查询条件 end --> 
    <!--分页 begin --> 
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> 
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" /> 
	<!--分页 end -->
	<br/>
	<table id='bt' class="table_list">
		<tr>
			<th align="center">
			<!-- <p><input class="u-button u-query" type="button" value='开票'
				onclick='balanceView()' name="modify" /></p> -->
				<input type="button" class="long_btn" name="saveBtn1" id="saveBtn1" onclick="balanceView()" value="生成开票通知单"/>
			</th>
		</tr>
	</table>
	</form>
	<script language="JavaScript">
		function isCheck(){
			var endBalanceDate=document.getElementById("endBalanceDate").value;
			var a = document.getElementById("CON_END_DAY").value;
			
			if(endBalanceDate>a){
			       MyAlert("开始时间不能大于结束时间！");
			       return;
			}		
			__extQuery__(1);
		}		
	</script>
</body>
</html>
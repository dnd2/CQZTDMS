<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>账户明细查询</title>
<% String contextPath = request.getContextPath();  %>
<script type="text/javascript">
function txtClr(valueId) {
	document.getElementById(valueId).value = '' ;
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 财务管理 &gt; 账户明细查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
	<c:if test="${returnValue==2}">
		<tr>
		<td  colspan="4"><font color="red">注意事项:</font></td>
	</tr>
		<tr>
		<td  colspan="4"><font color="red">1、入账联系人：现金-喻起萍 67595227 ，承兑汇票-郭萍 67591948 兵财额度-刘昕 67591948；</font></td>
		</tr>
		<tr>
		<td  colspan="4"><font color="red">2、如以上人员反映已录入，但dms在<font size="3" ><strong>2小时</strong></font>后无显示，请联系杨书友：67591947，确认财务的资金系统是否已入账。</font></td>
		</tr>
		<tr>
		<td  colspan="4"><font color="red">3、到账时间：现款-厂家财务收到款后当天录入系统，如<font size="3" ><strong>2-3天后</strong></font>未入账再进行咨询，请不要频繁打电话催促，承兑汇票、兵财额度-收到汇票和银行通知额度后录入系统。  
</font></td>
		</tr>
		</c:if>
		<tr>
			<td  colspan="4"><font color="red"><strong>兵财融资可用金额计算：兵财融资账户金额 - （轿车所有基地兵财融资冻结金额 + 微车所有基地兵财融资冻结金额）</strong></font></td>
		</tr>
		<tr>
			<td  colspan="4"><font color="red"> 1、现金、承兑汇票及长安信贷：扣订单的启票单位款项;</font></td>
		</tr>
		<tr>
			<td  colspan="4"><font color="red">2、三方信贷：扣订单的启票单位和采购单位款项，启票单位款项=启票单位自有款项+采购单位款项; </font></td>
		</tr>
		<tr>
			<td  colspan="4"><font color="red">3、兵财存货融资：扣订单的采购单位款项; </font></td>
		</tr>
		<tr>
			<td  colspan="4"><font color="red">4、所有资金类型订单的价格折扣均扣启票单位款项.</font></td>
		</tr>
		<tr>
			<td align="right">时间区间：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="t1" name="startDate" datatype="0,is_date,10" group="t1,t2"  value="${startDate }" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt"  type="text" id="t2" name="endDate" datatype="0,is_date,10" group="t1,t2" value="${endDate }" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" />
			</td>
			<td align="right">经销商：</td>
			<td>
				<input type="text" class="middle_txt" name="dealerCode" size="15" id="dealerCode" value="" datatype="1,is_null,18"/>
				<input name="button3" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','false');" value="..." />
				<input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
				
			</td>
			<td width="25%"></td>
		</tr>	
		<tr>
			<td align="right">账户类型：</td>
			<td>
				<select name="accountType" class="short_sel">
					<option value="">-请选择-</option>
					<c:forEach items="${list}" var="list">
							<option value="<c:out value="${list.TYPE_ID}"/>"><c:out value="${list.TYPE_NAME}"/></option>
					</c:forEach>
				</select>
			</td>
			<td align="right">操作类型：</td>
			<td>
				<label>
					<script type="text/javascript">
						genSelBoxExp("changeType",<%=Constant.ACCOUNT_CHANGE_TYPE_NEW%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
			<td width="25%"></td>
		</tr>		
		<tr>
			<td align="right"> 选择业务范围：</td>
		    <td>
		    	<select name="areaId" class="short_sel" id="areaId">
		    		<option value="">-请选择-</option>
					<c:if test="${areaList!=null}">
						<c:forEach items="${areaList}" var="list">
							<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
						</c:forEach>
					</c:if>
				</select>
			</td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td align="center">
				<!--  <input name="button2" type=button class="cssbutton" onClick="__extQuery__(1);querySubmit();" value="查询">-->
				<input name="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
				<input name="button4" type=button class="cssbutton" onClick="downLoad();" value="下载">
			</td>
			<td></td>
		</tr>
	</table>
	<br>
	<table class="table_query" id="detail" style="display:none">
		<tr>
			<td align="left">
				<strong><span id="qichu"></span>至<span id="qimo"></span><span id="mingzi"></span>(<span id="daima"></span>)帐目往来明细 </strong>
			</td>
		</tr>
		<tr>
			<td align="left">
				<strong>期初余额：<span id="amount1"></span></strong>
			</td>
		</tr>
		<tr>
			<td align="left">
				<strong>期末余额：<span id="amount2"></span></strong>
			</td>
		</tr>
	</table>
<!-- 查询条件 end -->   
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
<!--页面列表 begin -->
<script type="text/javascript" >


	var myPage;
	//查询路径 
	//var calculateConfig = {subTotalColumns:"TYPE_NAME|REMARK"};           
	var url = "<%=contextPath%>/sales/financemanage/OemAccountDetail/oemAccountDetailQuery.json";
	var title = null;
	var columns = [
				{header: "日期",dataIndex: 'CHNG_DATE',align:'center'},
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_SHORTNAME',align:'center'},
				{header: "账户类型",dataIndex: 'TYPE_NAME',align:'center'},
				{header: "账户名称", dataIndex: 'ACCOUNT_NAME', align:'center'},
				{header: "当前账面余额", dataIndex: 'BALANCE_AMOUNT', align:'center'},
				{header: "操作类型", dataIndex: 'CHNG_TYPE', align:'center',renderer:getItemValue},
				{header: "入账金额",dataIndex: 'CHANG_AMOUNT1', align:'center'},
				{header: "扣款金额",dataIndex: 'CHANG_AMOUNT2', align:'center'},
				{header: "凭证号码", dataIndex: 'ERP_DOC_NO', align:'center'},
				{header: "金税发票号", dataIndex: 'GOLDEN_INVOICE_NO', align:'center'},
				{header: "外部单据号", dataIndex: 'EXTERNAL_DOC_NO', align:'center'},
				{header: "描述", dataIndex: 'REMARK', align:'center'}
		      ];
    
    //设置金钱格式
    function myformat(value,metaDate,record){
        return String.format(amountFormat(value));
    }
    function querySubmit(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/financemanage/OemAccountDetail/oemAccountDetail.json',showForwordValue,'fm','queryBtn');
	}
    //初始化
    function doInit(){
   		loadcalendar();  //初始化时间控件
	}
  	//回调函数
	function showForwordValue(json){
		if(json.returnValue == '1'){
			document.getElementById("detail").style.display="inline";
			var startAmount  = json.amount1;
			var endAmount = json.amount2;
			var dealerName = json.dealerName;
			var dealerCode = json.dealerCode;
			var startDate = json.startDate;
			var endDate = json.endDate;
			document.getElementById("qichu").innerText = startDate.toString();
			document.getElementById("qimo").innerText = endDate.toString();
			document.getElementById("mingzi").innerText = dealerName.toString();
			document.getElementById("daima").innerText = dealerCode.toString();
			document.getElementById("amount1").innerText = amountFormat(startAmount).toString();
			document.getElementById("amount2").innerText = amountFormat(endAmount).toString();
		}else{
			MyAlert("执行异常，请与管理员联系！")
		}
	}
	function downLoad(){
		var t1 = document.getElementById("t1").value;
		var t2 = document.getElementById("t2").value;
		var dealerCode = document.getElementById("dealerCode").value;
		if(t1 == null || t1 == ""|| t2== null|| t2 == ""){
			MyAlert("时间区间不能为空！")
		 	return false;
		}
		//if(dealerCode == null || dealerCode == ""){
		//	MyAlert("经销商不能为空！")
		// 	return false;
		//}
    	$('fm').action="<%=contextPath%>/sales/financemanage/OemAccountDetail/oemAccountDetailDownLoad.do";
     	$('fm').submit();
    }
</script>
<!--页面列表 end -->
</body>
</html>
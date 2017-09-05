<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib prefix="fmt" uri="/jstl/fmt" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.List"%>

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>开票管理</title>
		<script type="text/javascript">
			function divControl()
			{
				document.getElementById('detailDiv').style.width=document.body.clientWidth;
			}
		</script>
	</head>
<body >
<form method="post" name="fm" id="fm">
<input type="hidden" name="balanceid" value="<c:out value="${balancePO1.id}"/>"/>
<input type="hidden" name="balanceid" value="<c:out value="${balancePO2.id}"/>"/>
<input type="hidden" name="balanceid" value="<c:out value="${balancePO3.id}"/>"/>
<input type="hidden" id="dealerId" name="dealerId" value="${dealerId}"/>
<input type="hidden" name="yieldly" value="${yieldly}"/>
<input type="hidden" name="version" value="${version}"/>
<input type="hidden" name="ReturnedID" value="${ReturnedID}"/>

	<table width="100%">
	    <tr>
		    <td colspan="6">
				<div class="navigation">
			    	<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;开票管理
			    </div>
		    </td>
	    </tr>
	</table>
	<table class="table_query" border="1" id="${balancePO1.yieldly}" bordercolor="#DAE0EE">
			<tr>
				<td align="right" nowrap="nowrap">结算单号：</td>
				<td align="left" nowrap="nowrap">
				   <c:out value="${jsNO}"/>
				</td>
				<td align="right" nowrap="nowrap">制单人姓名：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${balancePO1.applyPersonName}"/>
				</td>
				<td align="right" nowrap="nowrap">制单日期：</td>
				<td align="left" nowrap="nowrap">
					<fmt:formatDate  type="both" value="${balancePO1.createDate}"  pattern="yyyy-MM-dd"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">经销商代码：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${balancePO1.dealerCode}"/>
				</td>
				<td align="right" nowrap="nowrap">经销商名称：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${balancePO1.dealerName}"/>
				</td>
				<td align="right" nowrap="nowrap">开票单位：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${balancePO1.invoiceMaker}"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">维修时间起：</td>
				<td align="left" nowrap="nowrap">
					<fmt:formatDate  type="both" value="${balancePO1.startDate}"  pattern="yyyy-MM-dd"/>
					<input name="startDate" type="hidden" value="${endBalanceDate}" />
				</td>
				<td align="right" nowrap="nowrap">维修时间止：</td>
				<td align="left" nowrap="nowrap">
					<fmt:formatDate  type="both" value="${balancePO1.endDate}"  pattern="yyyy-MM-dd"/>
					<input name="endDate" type="hidden" value="${conEndDay}" />
				</td>
				<td align="right" nowrap="nowrap">&nbsp</td>
				<td align="left" nowrap="nowrap">
					&nbsp
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">工时费用(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${balancePO1.labourAmount }"/>
				</td>
				<td align="right" nowrap="nowrap">材料费(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${balancePO1.partAmount }"/>
				</td>
				<td align="right" nowrap="nowrap">其它费用(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${balancePO1.otherAmount }"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">保养费用(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${balancePO1.freeAmount }"/>
				</td>
				<td align="right" nowrap="nowrap">运费(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${balancePO1.returnAmount }"/>&nbsp;
				</td>
				<td align="right" nowrap="nowrap">辅料费</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${balancePO1.accessoriesPrice }"/>
				</td>
				
			</tr>
			<tr>
			<td align="right" nowrap="nowrap">特殊费用劳务费(元)：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${balancePO1.specialLabourSum  }"/>
			</td>
			<td align="right" nowrap="nowrap">特殊费用材料费(元)：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${balancePO1.specialDatumSum  }"/>
			</td>
			<td align="right" nowrap="nowrap">切换件活动费</td>
				<td align="left" nowrap="nowrap">
				<c:out value="${balancePO1.marketActivityAmount  }"/>
				</td>
			</tr>
			<tr>
				
				<td align="right" nowrap="nowrap">正负激励劳务费: </td>
				 
				<td align="left" nowrap="nowrap">
					<c:out value="${balancePO1.plusMinusLabourSum }"/>
				</td>
				<td align="right" nowrap="nowrap">正负激励材料费:</td>
				<td align="left" nowrap="nowrap"><c:out value="${balancePO1.plusMinusDatumSum }"/></td>
				<td align="right" nowrap="nowrap">补偿服务站：</td>
				<td align="left" nowrap="nowrap"><c:out value="${balancePO1.compensationDealerMoney }"/></td>
			</tr>
			<tr>
				
				<td align="right" nowrap="nowrap">旧件扣款(元)：</td>
				<td align="left" nowrap="nowrap">
					-<c:out value="${balancePO1.oldDeduct +balancePO2.oldDeduct +balancePO3.oldDeduct }"/>
				</td>
				<td align="right" nowrap="nowrap">上次行政扣款劳务费(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${labour}"/>
				</td>
				<td align="right" nowrap="nowrap">上次行政扣款材料费(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${datum }"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">本次行政扣款劳务费(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${labour_sum }"/>
					<input type="hidden" name ="labour_sum" value="${labour_sum }" />
				</td>
				<td align="right" nowrap="nowrap">本次行政扣款劳务费(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${datum_sum  }"/>
					<input type="hidden" name ="datum_sum" value="${datum_sum }" />
					<input type="hidden" name ="jsNO" value="${jsNO}" />
				</td>
				<td align="right" nowrap="nowrap">&nbsp;</td>
				<td align="left" nowrap="nowrap">&nbsp;</td>
			</tr>
			<tr>
			<td align="right" nowrap="nowrap">&nbsp;</td>
				<td align="left" nowrap="nowrap">&nbsp;</td>
			<td align="right">开票总金额：</td>
			<td align="left" >
				${AmountSum }
			</td>
			<td align="right" nowrap="nowrap">&nbsp;</td>
				<td align="left" nowrap="nowrap">&nbsp;</td>
			</tr>
		</table>
		
		<!--
	<table  class="table_query" border="1"  bordercolor="#DAE0EE" >
		<tr>
			<td>
				<div id="detailDiv" style="overflow:scroll;overflow-y:hidden">
					<table class="table_list">
						<tr class="table_list_th">
							<th style="position:relative">行号</th><%--0--%>
							<th style="position:relative">车系</th><%--1--%>
							<th>工时费用(元)</th><%--2--%>
							<th>配件费用(元)</th><%--3--%>
							<th>其它费用(元)</th><%--5--%>
							<th>保养费用(元)</th><%--6--%>
							<th>特殊费用(元)</th><%--9--%>
							<th>活动工时费用</th>
							<th>活动材料费用(元)</th><%--81--%>
							<th>活动其它费用(元)</th><%--82--%>
						</tr>
						<c:set var="a" value="${1}"></c:set>
						<c:forEach var="listtem" items="${listtem}" varStatus="vs">
							<tr class="table_list_th">
								<td >${a}</td>
								<td >${listtem.seriesName}</td>
								<td>${listtem.beforeLabourAmount}</td>
								<td>${listtem.beforePartAmount}</td>
								<td>${listtem.beforeOtherAmount}</td>
								<td>${listtem.afterLabourAmount}</td>
								<td>${listtem.afterPartAmount}</td>
								<td>${listtem.serviceLabourAmount}</td>
								<td>${listtem.servicePartAmount}</td>
								<td>${listtem.serviceOtherAmount}</td>
							</tr>
							 <c:set var="a" value="${a+1}"/>
						</c:forEach>
								
					</table>
					<script type="text/javascript">
						divControl();
					</script>
				</div>
			</td>
		</tr>
	</table>
  -->
		<table class="table_edit">
			<tr>
				<td colspan="6" align="center">
				<div id="buttontype">
				    <input type="button" class="long_btn" name="saveBtn1" id="saveBtn1" onclick="showVlue1()" value="生成开票通知单"/>
					<input type="button" class="normal_btn" name="backBtn" onclick="backTo()" value="返回"/>
				</div>
				</td>
			</tr>
		</table>
</form>
<script type="text/javascript">
function goClaimList(){
	window.open('<%=contextPath%>/claim/application/DealerNewKp/kpClaimList.do?balanceId=${map.ID}',"索赔单", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
}
function backTo(){
	location.href="<%=contextPath%>/claim/application/DealerNewKp/addDealerKpInit.do?dealerId="+document.getElementById('dealerId').value;
}
//财务回退此结算单至复核申请
function financialBack(){
	var balanceNo = '${map.BALANCE_NO}' ;
	if(confirm('确认回退此结算单至复核申请？')==true){
		$('saveBtn1').disabled = true ;
		$('btn2').disabled = true ;
		var url = '<%=contextPath%>/claim/authorization/BalanceMain/financialBack.json?balanceNo='+balanceNo ;
		makeNomalFormCall(url,financialBack2,'fm');
	}
}
function financialBack2(json){
	if(json.flag){
		window.parent.MyAlert('操作成功！') ;
		fm.action = "<%=contextPath%>/claim/authorization/BalanceMain/financialMainInit.do";
		fm.submit();
	}
	$('saveBtn1').disabled = false ;
	$('btn2').disabled = false ;
}
function ForDight(Dight,How)   
{    
        var Dight = Math.round(Dight*Math.pow(10,How))/Math.pow(10,How);    
        return Dight;    
} 
	function blurBack(obj) 
	{
		var val1 = document.getElementById(obj).value;
		if(val1)
		{
			var val2 = document.getElementById("aa").value;//总计
			var val3 = new Number(val2) - new Number(val1);
			var val4 = formatCurrency(val3);
			val4 = val4.replace(/[,]/g,"");
			document.getElementById("notea").innerHTML = Math.abs(val4);
			document.getElementById("noteaHidden").innerText = ForDight(val4,2);
			if(val3<=0){
				$('my_flag_1').style.display = '' ;
				$('notea').style.display = '' ;
				$('NOTE_AMOUNT_PRICE').innerHTML = 0 ;
			}else{
				$('my_flag_1').style.display = 'none' ;
				$('notea').style.display = 'none' ;
				$('NOTE_AMOUNT_PRICE').innerHTML = ForDight(val3,2) ;
			}
		}else{
			var val2 = document.getElementById("aa").value;//总计
			var val3 = new Number(val2)
			var val4 = formatCurrency(val3);
			val4 = val4.replace(/[,]/g,"");
			document.getElementById("notea").innerHTML =  Math.abs(ForDight(val4,2));
			if(val3<=0){
				$('my_flag_1').style.display = '' ;
				$('notea').style.display = '' ;
				$('NOTE_AMOUNT_PRICE').innerHTML = 0 ;
			}else{
				$('my_flag_1').style.display = 'none' ;
				$('notea').style.display = 'none' ;
				$('NOTE_AMOUNT_PRICE').innerHTML = ForDight(val3,2);
			}
		}
		    var lval2 = document.getElementById("aa").value;//总计
			var lval3 = new Number(lval2) - new Number(val1);
			var lval4 = formatCurrency(val3);
			val4 = val4.replace(/[,]/g,"");
		var valu = lval3;
		if(valu>=0)
		{
			var str = '';
			str = '<input type="button" class="long_btn" id="saveBtn1" onclick="showVlue1()" value="生成开票通知单" />' + 
			      '<input type="button" class="normal_btn" name="backBtn" onclick="javascript:history.go(-1)" value="返回"/>';
			document.getElementById("buttontype").innerHTML = str;
		}
		else
		{
			var str = '';
			str = '<input type="button" class="long_btn" id="saveBtn1" onclick="showVlue2()" value="转行政扣款单" />' + 
			      '<input type="button" class="normal_btn" name="backBtn" onclick="javascript:history.go(-1)" value="返回"/>';
			document.getElementById("buttontype").innerHTML = str;
		}
	}
	
	function showVlue1()
	{
		if(submitForm('fm')){
			MyConfirm("确认提交?", showVlue11);	
		}
		
	}
	
	function showVlue11(){
		makeNomalFormCall("<%=contextPath%>/claim/application/DealerNewKp/doBalanceKp.json",callValue,'fm','saveBtn1');
	}
	
	function showVlue2()
	{
		document.getElementById("noteCount").value = document.getElementById("NOTE_AMOUNT_PRICE").innerHTML;//
		document.getElementById("deductAmount").value = document.getElementById("notea").innerText;
		if(submitForm('fm')){
		MyConfirm("确认提交?", showVlue22);	
		}
	}
	
	function showVlue22(){
		noteAmount=document.getElementById("noteCount").value;
		if(noteAmount<0){
			document.getElementById("noteCount").value=0;
			}
		makeNomalFormCall("<%=contextPath%>/claim/authorization/BalanceMain/deductBalanceAudit.json",callValue,'fm','saveBtn1');
	}
	
	function callValue(json)
	{
		
			window.parent.MyAlert("操作成功！");
			if('${balancePO1.balanceYieldly}' == '95411001')
			{
				fm.action = "<%=contextPath%>/claim/application/DealerNewKp/dealerKpQueryInit.do";
				fm.submit();
			}else if('${balancePO1.balanceYieldly}' == '95411002')
			{
				fm.action = "<%=contextPath%>/claim/application/DealerNewKp/dealerKpQueryInit01.do";
				fm.submit();
			}
			
		
	}
	function yOnclick(yld)
	{
		
		document.getElementById(document.getElementById('yld1').value).style.display = 'none';
		document.getElementById(document.getElementById('yld2').value).style.display = 'none';
		document.getElementById(document.getElementById('yld3').value).style.display = 'none';
		 document.getElementById(yld.value).style.display = '';
	}
	
	function noteAmountSet(){
		var money = '${map.BALANCE_AMOUNT}' ;
		if(money<0){
			$('NOTE_AMOUNT_PRICE').innerHTML = 0 ;
			$('notea').innerTEXT = money*(-1);
			$('my_flag_1').style.display = '' ;
		}
	}
	noteAmountSet();
</script>
</body>
</html>
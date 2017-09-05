<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>结算单复核申请管理</title>
		<script type="text/javascript">
			function divControl()
			{
				document.getElementById('detailDiv').style.width=document.body.clientWidth;
			}
		</script>
	</head>
<body >
<form method="post" name="fm" id="fm">
<input type="hidden" name="var" value="<c:out value="${map.VAR}"/>"/>
<input type="hidden" name="jiujianCount" value="<c:out value="${map.TOTAL_AMOUNT}"/>"/>
<input type="hidden" name="kaoheCount" value="<c:out value="${map.FINE_SUM}"/>"/>
<input type="hidden" name="xingzhengCount" value="<c:out value="${map.DAMOUNT}"/>"/>
<input type="hidden" id="zongjiCount" name="zongjiCount" value="<c:out value="${map.AAMOUNT}"/>"/>
<input type="hidden" id="feiyongCount" name="feiyongCount" value="<c:out value="${map.AMOUNTSUM}"/>"/>
<input type="hidden" id="id" name="id" value="<c:out value="${map.ID}"/>"/>
<input type="hidden" name="dealerId" value="<c:out value="${map.DEALER_ID}"/>"/>
<input type="hidden" name="dealerCode" value="<c:out value="${map.DEALER_CODE}"/>"/>
<input type="hidden" name="dealerName" value="<c:out value="${map.DEALER_NAME}"/>"/>
<input type="hidden" name="aa" value="<c:out value="${map.AMOUNTSUM}"/>"/>
<input type="hidden" name="bb" value="<c:out value="${map.TAMOUNT}"/>"/>
<input type="hidden" name="cc" value="<c:out value="${map.AAMOUNT}"/>"/>
<input type="hidden" name="dd" value="<c:out value="${map.DECLARE_SUM1}"/>"/>
<input type="hidden" name="ee" value="<c:out value="${map.DECLARE_SUM2}"/>"/>
<input type="hidden" name="ff" value="<c:out value="${map.RETURN_AMOUNT}"/>"/>
<input type="hidden" name="gg" value="<c:out value="${map.RETURN_AMOUNT}"/>"/>
<input type="hidden" name="hh" value="0"/>
<input type="hidden" name="jj" value="0"/>
<input type="hidden" name="marketMoney" value="<c:out value="${money.SUM_MONEY}"/>"/>

<input type="hidden" name="balanceAmountHidden" value="<c:out value="${map.BALANCE_AMOUNT}"/>"/>
<input type="hidden" name="aaMountHidden" value="<c:out value="${map.AAMOUNT}"/>"/>

	<table width="100%">
	    <tr>
		    <td>
				<div class="navigation">
			    	<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;结算单复核申请管理
			    </div>
		    </td>
	    </tr>
	</table>
	<table class="table_edit">
			<tr>
				<td align="right" nowrap="nowrap">结算单号：</td>
				<td align="left" nowrap="nowrap">
				   <c:out value="${map.BALANCE_NO}"/>
				</td>
				<td align="right" nowrap="nowrap">制单人姓名：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.APPLY_PERSON_NAME}"/>
				</td>
				<td align="right" nowrap="nowrap">制单日期：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.CREATEDATE}"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">经销商代码：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.DEALER_CODE}"/>
				</td>
				<td align="right" nowrap="nowrap">经销商名称：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.DEALER_NAME}"/>
				</td>
				<td align="right" nowrap="nowrap">开票单位：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.INVOICE_MAKER}"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">维修时间起：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.STARTDATE}"/>
				</td>
				<td align="right" nowrap="nowrap">维修时间止：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.ENDDATE}"/>
				</td>
				<td align="right" nowrap="nowrap">生产商：</td>
				<td align="left" nowrap="nowrap">
					<script type="text/javascript">
						writeItemValue(<c:out value="${map.YIELDLY}"/>)
					</script>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">工时费用(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.LABOUR_AMOUNT}"/>
				</td>
				<td align="right" nowrap="nowrap">材料费(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.PART_AMOUNT}"/>
				</td>
				<td align="right" nowrap="nowrap">救急费(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.OTHER_AMOUNT}"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">保养费用(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.FREE_AMOUNT}"/>
				</td>
				<td align="right" nowrap="nowrap">服务活动费用(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.SERVICE_AMOUNT}"></c:out>
				</td>
				<td align="right" nowrap="nowrap">运费(元)：</td>
				<td align="left" nowrap="nowrap">
					<input type="text" datatype="1,isMoney" blurback="true" class="middle_txt" id="RETURN_AMOUNT" name="RETURN_AMOUNT" value="<c:out value="${map.RETURN_AMOUNT}"/>"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">特殊费用(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.DECLARE_SUM1}"/>
				</td>
				<td align="right" nowrap="nowrap">特殊外出费用(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.DECLARE_SUM2}"/>
				</td>
				<td align="right" nowrap="nowrap">&nbsp;</td>
				<td align="left" nowrap="nowrap">&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">旧件扣款(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.TOTAL_AMOUNT}"/>
				</td>
				<td align="right" nowrap="nowrap">考核扣款(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.FINE_SUM}"/>
				</td>
				<td align="right" nowrap="nowrap">行政扣款(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.DAMOUNT}"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">保养扣款(元)：</td>
				<td align="left" nowrap="nowrap">
					<input type="text" datatype="1,isMoney" blurback="true" class="middle_txt" id="repairAmount" name="repairAmount" value=""/>
				</td>
				<td align="right" nowrap="nowrap">服务活动扣款(元)：</td>
				<td align="left" nowrap="nowrap">
					<input type="text" datatype="1,isMoney" blurback="true" class="middle_txt" id="activityAmount" name="activityAmount" value=""/>
				</td>
				<td align="right" nowrap="nowrap">审核扣款统计：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.AUTHDEDUCT}"/>&nbsp;
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">费用合计(元)：</td>
				<td align="left" nowrap="nowrap">
					<div id="amountsum">
						<c:out value="${map.BALANCE_AMOUNT}"/>
					</div>
				</td>
				<td align="right" nowrap="nowrap">扣款合计(元)：</td>
				<td align="left" nowrap="nowrap">
					<div id="tamount">
						<c:out value="${map.TAMOUNT}"/>
					</div>
				</td>
				<td align="right" nowrap="nowrap">总计(元)：</td>
				<td>
					<div id="aamount">
						<c:out value="${map.AAMOUNT}"/>
					</div>
				</td>
			</tr>
			<tr>
				<td align="right">备注：</td>
				<td align="left" colspan="5">
					<textarea name="REMARK" id="REMARK" datatype="1,is_null,200" rows="5" cols="80" ><c:out value="${map.REMARK}"/></textarea>
				</td>
			</tr>
			<tr>
				<td align="right">索赔单数：</td>
				<td align="left" colspan="5">
					<c:out value="${map.CLAIM_COUNT}"/>
				</td>
			</tr>
			<tr>
				<td align="right">站长电话：</td>
				<td align="left">
					<c:out value="${map.STATIONER_TEL}"/>
				</td>
				<td align="right">索赔员电话：</td>
				<td align="left" colspan="3">
					<c:out value="${map.CLAIMER_TEL}"/>
				</td>
			</tr>
		</table>
	<table>
		<tr>
			<td>
				<div id="detailDiv" style="overflow:scroll">
					<table class="table_list">
						<tr class="table_list_th">
							<th style="position:relative">行号</th><%--0--%>
							<th style="position:relative">车系</th><%--1--%>
							<th style="position:relative">备注</th><%--17--%>
							<th>售前工时费用(元)</th><%--2--%>
							<th>售前配件费用(元)</th><%--3--%>
							<th>售后工时费用(元)</th><%--5--%>
							<th>售后配件费用(元)</th><%--6--%>
							<th>保养次数</th><%--9--%>
							<th>保养费用(元)</th><%--8--%>
							<th>保养工时费用(元)</th><%--81--%>
							<th>保养材料费用(元)</th><%--82--%>
							<th>服务活动次数</th><%--14--%>
							<th>服务活动费用(元)</th><%--13--%>
							<th>售前三包单数</th><%--15--%>
							<th>售后三包单数</th><%--16--%>
						</tr>
					<%
						List<Map<String, Object>> list = (List<Map<String, Object>>)request.getAttribute("list");
						if(list!=null&&list.size()>0){
							for(int i=0;i<list.size();i++){
								Map<String, Object> map = list.get(i);
					%>
								<tr class="<%=(i%2)==0?"table_list_row1":"table_list_row2"%>">
									<td style="position:relative"><%=map.get("NUM") %></td>
									<td style="position:relative"><%=map.get("SERIES_NAME") %></td>
									<td style="position:relative" title="<%=map.get("REMARK")==null?"":map.get("REMARK")%>">
										<%
							          			if(map.get("REMARK")!=null){
							          				if(String.valueOf(map.get("REMARK")).length()<=6){
							          		%>
							          			<%=map.get("REMARK")%>
							          		<%
						          				}
						          		%>
						          		<%
						          				if(String.valueOf(map.get("REMARK")).length()>10){
						          					String s = String.valueOf(map.get("REMARK"));
						          					s = s.substring(0,5);
						          		%>
						          			<%=s%>...
						          		<%
						          				}
						          			}
						          		%>
									</td>
									<td>
										<script type="text/javascript">
											document.write(amountFormat(<%=map.get("BEFORE_LABOUR_AMOUNT") %>))
										</script>
									</td>
									<td>
										<script type="text/javascript">
											document.write(amountFormat(<%=map.get("BEFORE_PART_AMOUNT") %>))
										</script>
									</td>
									<td>
										<script type="text/javascript">
											document.write(amountFormat(<%=map.get("AFTER_LABOUR_AMOUNT") %>))
										</script>
									</td>
									<td>
										<script type="text/javascript">
											document.write(amountFormat(<%=map.get("AFTER_PART_AMOUNT") %>))
										</script>
									</td>
									<td><%=map.get("FREE_CLAIM_COUNT") %></td>
									<td>
										<script type="text/javascript">
											document.write(amountFormat(<%=map.get("FREE_CLAIM_AMOUNT") %>))
										</script>
									</td>
									<td>
										<script type="text/javascript">
											document.write(amountFormat(<%=map.get("FREE_LABOUR_AMOUNT") %>))
										</script>
									</td>
									<td>
										<script type="text/javascript">
											document.write(amountFormat(<%=map.get("FREE_PART_AMOUNT") %>))
										</script>
									</td>
									<td><%=map.get("SERVICE_CLAIM_COUNT") %></td>
									<td>
										<script type="text/javascript">
											document.write(amountFormat(<%=map.get("SERVICE_AMOUNT") %>))
										</script>
									</td>
									<td><%=map.get("BEFORE_CLAIM_COUNT") %></td>
									<td><%=map.get("AFTER_CLAIM_COUNT") %></td>
								</tr>
					<%
							}
						}
					%>
					</table>
					<script type="text/javascript">
						divControl();
					</script>
				</div>
			</td>
		</tr>
	</table>
	<table class="table_edit">
			<tr>
				<td colspan="6" align="center">
				<div id="buttontype">
					<input type="button" class="normal_btn" name="saveBtn" onclick="balance()" value="复核申请"/>
					<input type="button" class="normal_btn" name="saveBtn" onclick="backToAudit()" value="退回"/>
					<input type="button" class="normal_btn" name="backBtn" onclick="javascript:history.go(-1)" value="返回"/>
				</div>
				</td>
			</tr>
	</table>
</form>
<script type="text/javascript">
	function backToAudit(){
		MyConfirm("确定要退回?",function(){
			makeNomalFormCall("<%=contextPath%>/claim/authorization/BalanceMain/backToAudit.json",backAuditBack,'fm');
		});
	}

	function backAuditBack(json){
		var msg=json.msg;
		if(msg=='true'){
			window.location = "<%=contextPath%>/claim/authorization/BalanceMain/balanceMainInit.do"
		}else{
			MyAlert('操作失败!');
		}
	}
	
	function ForDight(Dight,How)   
	{    
	        var Dight = Math.round(Dight*Math.pow(10,How))/Math.pow(10,How);    
	        return Dight;    
	}  
	function divControl()
	{
		document.getElementById('detailDiv').style.width=document.body.clientWidth;
	}
	
	function blurBack(obj) 
	{
		if(obj == "RETURN_AMOUNT")
		{
			var val1 = document.getElementById(obj).value;
			if(val1!=null||val1!="")
			{
				if(val1!=document.getElementById("ff").value)
				{
					document.getElementById("ff").value = val1;
					var val2 = document.getElementById("aa").value;//费用合计
					var val5 = document.getElementById("cc").value;
					var val3 = document.getElementById("aamount").innerHTML;//总计
					//document.getElementById("amountsum").innerHTML = new Number(val1) +  new Number(val2) - new Number(document.getElementById("gg").value);
					//var val4 = formatCurrency(new Number(val1) + new Number(val5) - new Number(document.getElementById("gg").value));
					//val4 = val4.replace(/[,]/g,"");
					//document.getElementById("aamount").innerHTML = val4;
					
					//var balanceAmountHidden=document.getElementById("balanceAmountHidden").value;
					//var returnAmountOld=document.getElementById("gg").value;
					
					//document.getElementById("amountsum").innerHTML=new Number(balanceAmountHidden)-new Number(returnAmountOld)+new Number(val1);
					//var aaMountHidden=document.getElementById("aaMountHidden").value;
					//document.getElementById("aamount").innerHTML = new Number(aaMountHidden)+new Number(val1);
					
					var returnAmountOld=document.getElementById("gg").value;
					var amountsum=document.getElementById("amountsum").innerText;
					var tamount=document.getElementById("tamount").innerText;
					var ks=new Number(amountsum)-new Number(returnAmountOld)+new Number(val1)-new Number(tamount);
					document.getElementById("aamount").innerText=ForDight(ks,2);
					
				}
			}
			if(val1==""||val1==null)
			{
				document.getElementById("amountsum").innerHTML = new Number(document.getElementById("amountsum").innerHTML) - new Number(document.getElementById("ff").value);
				document.getElementById("aamount").innerHTML = new Number(document.getElementById("aamount").innerHTML) - new Number(document.getElementById("ff").value);
			}
		}
		if(obj == "repairAmount")
		{
			var val4 = document.getElementById(obj).value;
			if(val4!=null||val4!="")
			{
				document.getElementById("hh").value = val4;
				var val5 = document.getElementById("bb").value;//扣款总金额
				var val7 = document.getElementById("amountsum").innerHTML;//费用合计
				var val6 = document.getElementById("activityAmount").value;
				
				var val8 = formatCurrency(new Number(val4) + new Number(val5) + new Number(val6));
				val8 = val8.replace(/[,]/g,"");
				
				document.getElementById("tamount").innerHTML = val8;
				
				//var val9 = formatCurrency(new Number(val7) - new Number(val8));
				//val9 = val9.replace(/[,]/g,"");
				
				//document.getElementById("aamount").innerHTML = val9;//总计
				var returnAmountNew=document.getElementById("RETURN_AMOUNT").value;
				var returnAmountOld=document.getElementById("gg").value; //默认运费
				var amountsum=document.getElementById("amountsum").innerText;
				var tamount=document.getElementById("tamount").innerText;
				var ks=new Number(amountsum)-new Number(returnAmountOld)+new Number(returnAmountNew)-new Number(tamount);
				document.getElementById("aamount").innerText=ForDight(ks,2);
			}
			else
			{
				document.getElementById("tamount").innerHTML = new Number(document.getElementById("tamount").innerHTML) - new Number(document.getElementById("hh").value);
				document.getElementById("aamount").innerHTML = new Number(document.getElementById("aamount").innerHTML) + new Number(document.getElementById("hh").value);
			}
		}
		if(obj == "activityAmount")
		{
			var val10 = document.getElementById(obj).value;
			if(val10!=null||val10!="")
			{
				var val5 = document.getElementById("bb").value;//扣款总金额
				var val7 = document.getElementById("amountsum").innerHTML;//费用合计
				var val4 = document.getElementById("repairAmount").value;
				var val8 = formatCurrency(new Number(val10) + new Number(val5) +  new Number(val4));
				val8 = val8.replace(/[,]/g,"");
				document.getElementById("tamount").innerHTML = val8;
				var val9 = formatCurrency(new Number(val7) - new Number(val8));
				val9 = val9.replace(/[,]/g,"");
				//document.getElementById("aamount").innerHTML = val9;//总计

				var returnAmountNew=document.getElementById("RETURN_AMOUNT").value;
				var returnAmountOld=document.getElementById("gg").value; //默认运费
				var amountsum=document.getElementById("amountsum").innerText;
				var tamount=document.getElementById("tamount").innerText;
				var ks=new Number(amountsum)-new Number(returnAmountOld)+new Number(returnAmountNew)-new Number(tamount);
				document.getElementById("aamount").innerText=ForDight(ks,2);
				
			}
			else
			{
				document.getElementById("tamount").innerHTML = new Number(document.getElementById("tamount").innerHTML) - new Number(document.getElementById("jj").value);
				document.getElementById("aamount").innerHTML = new Number(document.getElementById("aamount").innerHTML) + new Number(document.getElementById("jj").value);
			}
		}
	}
	
	function balance()
	{
		var returnAmountOld=document.getElementById("gg").value; //默认运费
		var returnAmountNow=document.getElementById("RETURN_AMOUNT").value;//现在运费
		//if(new Number(returnAmountNow)>new Number(returnAmountOld)){
			//MyAlert('录入运费不能大于申请运费.申请运费:'+returnAmountOld);
			//return;
		//}
		
		document.getElementById("feiyongCount").value = document.getElementById("amountsum").innerHTML;
		document.getElementById("zongjiCount").value = document.getElementById("aamount").innerHTML;
		MyConfirm("确认提报？",balance22);
	}
	
	function balance22(){
		makeNomalFormCall("<%=contextPath%>/claim/authorization/BalanceMain/dealerTimeJuge.json",jugeBack,'fm');
	}
	function jugeBack(json){
		var msg=json.msg;
		/*if(msg=='01'){
			MyAlert("前期结算单未复核!");
			return;
		}*/
		if(msg=="02"){
			MyAlert("前期旧件未审核!");
			return;
		}
		//zhumingwei 2011-03-08 注释代码。以前要限制，现在不需要，暂时注释
		//if(msg=="03"){
			//MyAlert("前期结算单未复核.!");
			//return;
		//}
		if(msg=="04"){
			MyAlert("此经销商已被暂停结算!");
			return;
		}
		makeNomalFormCall("<%=contextPath%>/claim/authorization/BalanceMain/financialModeAudit.json",infoBack,'fm','saveBtn');
	}
	function infoBack(json)
	{
		if(json.SUCCESS == "DEALED"){
			MyAlert("其他人在处理该经销商结算单！");
			window.location = "<%=contextPath%>/claim/authorization/BalanceMain/balanceMainInit.do"
		}else if(json.SUCCESS =='NOTSATISFLED'){
			MyAlert("该结算单对应特殊费用工单未完全审核,请审核完特殊费用后再操作！");
			window.location = "<%=contextPath%>/claim/authorization/BalanceMain/balanceMainInit.do"
		}else if(json.returnValue == "0"){
			window.parent.MyAlert("本单据已扣款！请勿重复操作！");
			fm.action = "<%=contextPath%>/claim/authorization/BalanceMain/balanceMainInit.do";
			fm.submit();
		}else if(json.returnValue == "1"){
			window.parent.MyAlert("操作成功！");
			window.location = "<%=contextPath%>/claim/authorization/BalanceMain/balanceMainInit.do"
		}else{
			MyAlert("错误信息，请联系管理员！");
		}
	}
	function accAddFloat(arg1,arg2){
		  var r1,r2,m;
		  try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
		  try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
		  m=Math.pow(10,Math.max(r1,r2))
		  return (arg1*m+arg2*m)/m
	}

	var amountsum=document.getElementById("amountsum").innerText;
	var tamount=document.getElementById("tamount").innerText;
	var kdfs=accAddFloat(parseFloat(amountsum),-parseFloat(tamount));
	document.getElementById("aamount").innerText=ForDight(kdfs,2);
	
</script>
</body>
</html>





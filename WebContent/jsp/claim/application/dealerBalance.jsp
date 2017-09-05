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
	    var ids;
	    var myPage;
		//查询路径
		var url = "<%=contextPath%>/claim/application/DealerBalance/dealerBalanceStatisQuery.json";
		//标题			
		var title = null;
	    //显示列表控制
		var columns = [
						{header: "序号", width:'20%',renderer:getIndex},
						{header: "产地", width:'20%', dataIndex: 'YIELDLY',renderer:getItemValue},
						{header: "索赔单数", width:'10%', dataIndex: 'CLAIMCOUNT'},
						{header: "索赔单状态", width:'15%', dataIndex: 'STATUS',renderer:getItemValue},
						{header: "配件金额(元)", width:'15%', dataIndex: 'PART_AMOUNT',renderer:formatCurrency},
						{header: "工时金额(元)", width:'5%', dataIndex: 'LABOUR_AMOUNT',renderer:formatCurrency},
						{header: "其他费用(元)", width:'5%', dataIndex: 'NETITEM_AMOUNT',renderer:formatCurrency},
						{header: "免费保养费用(元)", width:'5%', dataIndex: 'FREE_M_PRICE',renderer:formatCurrency},
						{header: "服务活动费用(元)", width:'5%', dataIndex: 'CAMPAIGN_FEE',renderer:formatCurrency},
						{header: "索赔申请金额(元)", width:'5%', dataIndex: 'BALANCE_AMOUNT',renderer:formatCurrency}
			      ];
			           
		function doInit(){
	   		loadcalendar();
		}
		
		//结算操作
		function balanceView() {
			var fm = document.getElementById("fm");
			fm.action = '<%=request.getContextPath()%>/claim/application/DealerBalance/balanceView.do';
			fm.target = "_self";
			fm.submit();
		}

		//返回结算单查询页面
		function backToQuery(){
			location.href = "<%=contextPath%>/claim/application/DealerBalance/dealerBalanceInit.do";
		}

		//根据基地查询结算开始时间
		function showDate(yieldly){
			var url = "<%=contextPath%>/claim/application/DealerBalance/showDate.json?yieldly="+yieldly;
			sendAjax(url,showDetail2,"fm")
		}
		//设置时间到页面上
		function showDetail2(json){
			var balanceDate = json.balanceDate;
			var balanceTime = balanceDate.substring(0,10);
			$('div').innerText=balanceTime;
			$('endBalanceDate').value=balanceTime;
		}
	</script>
</head>
<body onload="doInit()">
	<div class="navigation">
    	<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;经销商结算上报
    </div>
	<form method="post" name="fm" id="fm">
		<input type="hidden" name="ids" id="ids" value="" />
		<table align="center" class="table_query">
			<tr>
				<td class="table_query_2Col_label_6Letter">产地：<input type="hidden" id="flag" name="flag" value="" /></td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("YIELDLY",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"min_sel","onchange=showDate(this.value);","false",'');
					</script>
				</td>
				<td class="table_query_2Col_label_6Letter">审核通过日期：</td>
				<td>
					<label id="div"></label>
				    <input type="hidden" id="endBalanceDate" name="endBalanceDate" value="" />  至  
					<input name="CON_END_DAY" type="text" id="CON_END_DAY" class="short_txt"
					datatype="0,is_date,10" hasbtn="true"
					callFunction="showcalendar(event, 'CON_END_DAY', false);" />
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td colspan="3">
					注：只有索赔单状态为"审核通过"的可以结算！
				</td>
			</tr>
			<tr>
				<td colspan="4" align="center">
					<input id="queryBtn"
					class="normal_btn" type="button" value="查询" name="recommit"
					onclick="isFlag();" />
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
	<table class="table_list" id="sepcFeeTable">
	    <tr id="sepcFeeTr" style="display: none;">
	      	<th><b>序号</b></th>
	      	<th><b>产地</b></th>
	      	<th><b>工单类型</b></th>
	      	<th><b>工单数</b></th>
			<th><b>金额</b></th>
	    </tr>
	</table>
	</form>
	<form name="form1" style="display: none">
	<br/>
	<table id='bt' class="table_list">
		<tr>
			<th align="center">
			<p><input class="normal_btn" type="button" value='结算'
				onclick='balanceView()' name="modify" /></p>
			</th>
		</tr>
	</table>
	</form>
	<script language="JavaScript">
		//document.form1.style.display = "none";
		//var HIDDEN_ARRAY_IDS=['form1'];
	</script>
	<script language="JavaScript">
		function isFlag(){
			var endBalanceDate = document.getElementById("endBalanceDate").value;
			var url="<%=contextPath%>/claim/application/DealerBalance/getFalg.json?endBalanceDate="+endBalanceDate;
			makeNomalFormCall(url,showDetail1,'fm','');
		}
		function showDetail1(json){
			var flag = json.flag;
			var error = json.error;
			var monthday = json.monthday;
			if(Number(monthday)>12){
				MyAlert('不能超过一年做结算单!');
	    		return;
			}
			if(error=='error'){
				MyAlert('结算时间不能大于等于当前时间!');
	    		return;
			}
			if(flag=='false'){
	    		MyAlert('请先在顶腾系统中完成11月份前[维护结算申请单]的提报!');
	    		return;
			}else{
				isCheck();
			}
		}
		function isCheck(){
			var endBalanceDate=document.getElementById("endBalanceDate").value;
			var a = document.getElementById("CON_END_DAY").value;
			var year=a.substring(0,4);
			var mouth=a.substring(5,7);
			//var mouth1 = mouth++;
			var day=a.substring(8,10);
			var beginDay=endBalanceDate.substring(8,10);
			var mouth1=endBalanceDate.substring(5,7);//////////////////////////
			var year1=endBalanceDate.substring(0,4);//////////////////////////
			var YIELDLY = document.getElementById("YIELDLY").value;
			if(YIELDLY==''){
				MyAlert("请选择基地");
				return;
			}
			if(endBalanceDate>a){
			       MyAlert("起始年份不应大于结束年份！");
			       return;
			}
			//if(month>12){//如果当前大于12月，则年份转到下一年   
				//mouth1 -=12;        //月份减 
				//year++;            //年份增   
			//}		
			
			var last=(new Date((new Date(year,mouth,1)).getTime()-1000*60*60*24)).getDate();//获取当月最后一天日期

			if(Number(beginDay)==Number(${day+1 })){
				var aaa=Number(Number(year)+1)+'-12-'+${day };
			}else{
				var aaa=year1+'-12-'+${day };
			}
			if(a>aaa){
				MyAlert("不能大于当前年的12月结算日!");
				return;
			}
			
			if(mouth1!=12){
				if(mouth!=12){
					if(day!=last){
						MyAlert('必须选择当月的最后一天!');
						return;
					}
				}else{
					if(Number(day)!=Number(${day })){
						MyAlert('12月必须选择结算日'+${day }+'号!');
						return;
					}
				}
			}else{
				if(Number(beginDay)==Number(1) && Number(day)!=Number(${day })){
					MyAlert('12月1号的单子必须选择结算日'+${day }+'号!');
					return;
				}else if(Number(beginDay)==Number(11)){
					if(Number(year)>Number(year1)){
						if(mouth!=12){
							if(day!=last){
								MyAlert('必须选择当月的最后一天!');
								return;
							}
						}else{
							if(Number(day)!=Number(${day })){
								MyAlert('12月必须选择结算日'+${day }+'号!');
								return;
							}
						}
					}else{
						if(day!=last){
							MyAlert('必须选择当月的最后一天!');
							return;
						}
					}
				}
			}
			
			//if(mouth!=12){
				//if(day!=last){
					//MyAlert('必须选择当月的最后一天!');
					//return;
				//}
			//}else{
				//if(Number(beginDay)==Number(1) && Number(day)==Number(${day })){
					//MyAlert('OK');
					//return;
				//}else if(Number(beginDay)==Number(11) && Number(day)==${day1 }){
					//MyAlert('OK1');
					//return;
				//}else{
					//MyAlert('12月份必须选择结算日!');
					//return;
				//}
			//}
			returnDataAuth(endBalanceDate);
			document.form1.style.display = "";
			__extQuery__(1);
		}
		function returnDataAuth(endBalanceDate){
			var url="<%=contextPath%>/claim/application/DealerBalance/getSpecFeeList.json?endBalanceDate="+endBalanceDate;
			sendAjax(url,callBacksData,'fm');
		}
		function deleteElement(){
			var table = document.getElementById("sepcFeeTable");
			var trs = table.getElementsByTagName('tr');
			var rowCount=trs.length;
			for(var i=1;i<rowCount;i++){
				table.deleteRow(i);
			}
		}

		function callBacksData(obj) {
			document.getElementById("sepcFeeTr").style.display="";
			var table = document.getElementById('sepcFeeTable');//数据区域
			deleteElement();
	       
			var list = obj.spFeeList;
			if(list!=null) {
				for(var i=0; i<list.length; i++) {
					var trs = table.getElementsByTagName('tr');
					var rowCount=trs.length;
					var row=table.insertRow(rowCount);
					row.className="table_list_row1";
					var cel1=row.insertCell(0);
					var cel2=row.insertCell(1);
					var cel3=row.insertCell(2);
					var cel4=row.insertCell(3);
					var cel5=row.insertCell(4);

					cel1.innerHTML=(i+1);
					cel2.innerHTML=getItemValue(list[i].YIELD);
					cel3.innerHTML=getItemValue(list[i].FEE_TYPE);
					cel4.innerHTML=list[i].COUNT;
					cel5.innerHTML=formatCurrency(list[i].DECLARE_SUM);
				}
				
			}
		}
	</script>
</body>
</html>
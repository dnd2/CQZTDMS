<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>


<%@ page import="java.util.*"%>

<jsp:include page="${path}/common/jsp_head_new.jsp" />
<%
	String contextPath = request.getContextPath();
	//String expStatusCode = Constant.ORDER_STATUS_00 + "," + Constant.ORDER_STATUS_01 + "," + Constant.ORDER_STATUS_03;
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>运费发票补录</title>
<script type="text/javascript">
	
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>结算管理>运费发票补录</div>
	
	<form method="post" name="fm" id="fm">
	<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>运费发票补录</h2>
	<div class="form-body">
		<table class="table_query">
		    <tr>
		       <td class="right">结算单号：</td>
				<td>
				   <input type="text" name="apply_no" id="apply_no" class="middle_txt" />

				</td>
		       <td class="right">承运商：</td>
		       <td>
		         <select name="logi_id" id="logi_id" class="u-select" >
		 		<option value="">-请选择-</option>
				<c:if test="${list_logi!=null}">
					<c:forEach items="${list_logi}" var="list_logi">
						<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
					</c:forEach>
				</c:if>
		       </td>
		    </tr>
			<tr>
				<td colspan="4" class="table_query_4Col_input" style="text-align: center">
					<input type="hidden" id="balIdv" name="balIdv"/>
					<input type="button" value="查询" id="queryBtn" class="u-button u-query" onclick="__extQuery__(1);"/>
					&nbsp;
					<input type="reset" id="resetButton" class="u-button u-reset" value="重置"/>
				</td>
			</tr>
		</table>
		</div>
</div>
		<!-- 分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<!-- 分页 end -->
	</form>

	<script>
		var myPage;
	
		var url = "<%=request.getContextPath()%>/sales/storage/balancemanage/SalesBalanceSuppply/billSupplyList.json";
	
		var title = null;
	
		//设置列名属性
		var columns = [
				{header: "序号", dataIndex: '', align:'center', renderer:getIndex},
				{header: "操作", dataIndex: 'APPLY_ID', align:'center',renderer:mylink},
				{
					header: "结算单号", dataIndex: 'APPLY_NO', align:'center',
					renderer: function(value, metaData, record) {
						var url = '<%=contextPath%>/sales/storage/balancemanage/SalesBalanceApply/showBalanceDel.do?applyId=' + record.data.APPLY_ID;
						return "<a href='javascript:void(0);' onclick='viewOrderInfo(\""+url+"\")'>"+value+"</a>";
					}
				},
				{header: "承运商", dataIndex: 'LOGI_NAME', align:'center'},
				{header: "运输总量", dataIndex: 'BAL_COUNT', align:'center'},
				{header: "挂账合计", dataIndex: 'BAL_AMOUNT', align:'center'},
				{header: "补款金额", dataIndex: 'SUPPLY_MONEY', align:'center'},
				{header: "扣款金额", dataIndex: 'DEDUCT_MONEY', align:'center'},
				{header: "其他金额", dataIndex: 'OTHER_MONEY', align:'center'},
				{header: "结算金额", dataIndex: 'SUM_AMOUNT', align:'center'}
				
		];
		function mylink(value,meta,record){
			var str = "<a href='javascript:void(0);' class='u-anchor' onclick='supplyDo("+value+");'>补录</a>";
			return String.format(str);
		}
		//补录跳转
		function  supplyDo(value){
			var url = '<%=contextPath%>/sales/storage/balancemanage/SalesBalanceSuppply/toEditBalSupply.do?applyId=' +value;
			//OpenHtmlWindow(url,800,400);
			document.getElementById('fm').action = url;
			document.getElementById("fm").submit();
		}
		//审核
		function auditDo(flag){
			var b=0;
			var arrayObj = new Array(); 
			arrayObj=document.getElementsByName("groupIds");
			for(var i=0;i<arrayObj.length;i++){
				if(arrayObj[i].checked){
					b=1;//有选中
				}
				
			}
			if(b==0){
				MyAlert("请选择审核记录！");
				return ;
			}
			if(flag==1){
				MyConfirm("确认审核通过？",passDo);
			}else{
				MyConfirm("确认审核驳回？",refuseDo);
			}
			
			
		}
		function passDo(){
			var url  ="<%=request.getContextPath()%>/sales/storage/balancemanage/SalesBalanceAudit/auditDo.json?pflag=1";
			sendAjax(url,function(json){
				if(json.returnValue=='1'){
					MyAlertForFun("操作成功！",function(){
						document.getElementById("REMARK").value="";//清空备注
// 						 window.location.reload();
						__extQuery__(1);
					});
				}
			}
			,'fm');
		}
		function refuseDo(){
			var url  ="<%=request.getContextPath()%>/sales/storage/balancemanage/SalesBalanceAudit/auditDo.json?pflag=2";
			sendAjax(url,function(json){
				if(json.returnValue=='1'){
					MyAlertForFun("操作成功！",function(){
						document.getElementById("REMARK").value="";//清空备注
// 						 window.location.reload();
						__extQuery__(1);
					});
				}
			}
			,'fm');
		}
		function viewOrderInfo(url)
		{
			OpenHtmlWindow(url,1000,450);
		}
		
	</script>
</body>
</html>



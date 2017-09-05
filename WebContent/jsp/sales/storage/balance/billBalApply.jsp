<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.*"%>
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>运费结算生成</title>
<script type="text/javascript">
	
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>结算管理>运费结算生成</div>	
	<form method="post" name="fm" id="fm">
		<div class="form-panel">
		<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>运费结算生成</h2>
		<div class="form-body">
		<table class="table_query">
		    <tr>
		       <td class="right">挂账单号：</td>
				<td>
				   <input type="text" name="bal_no" id="bal_no" class="middle_txt" />

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
				<td class="right">是否调整：</td>
				<td>
				  <label>
					<script type="text/javascript">
							genSelBoxExp("isChange",<%=Constant.IF_TYPE %>,"-1",true,"u-select",'',"false",'');
					</script>
				</label>
				</td>
			</tr>
			<tr>
				<td colspan="6" class="table_query_4Col_input" style="text-align: center">
					<input type="hidden" id="balIdv" name="balIdv"/>
					<input type="button" value="查询" id="queryBtn" class="u-button u-query" onclick="__extQuery__(1);"/>&nbsp;
					<input type="reset" id="resetButton" class="u-button u-reset" value="重置"/>	&nbsp;
					<input type="button" value="生成结算单" class="normal_btn" onclick="subBill();"/>
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
	
		var url = "<%=request.getContextPath()%>/sales/storage/balancemanage/SalesBalanceApply/billApplyList.json";
	
		var title = null;
	
		//设置列名属性
		var columns = [
				{header: "序号", dataIndex: '', align:'center', renderer:getIndex},
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"groupIds\")'/>",sortable: false,dataIndex: 'BILL_ID',renderer:myCheckBox},
				//{header: "操作", dataIndex: 'BILL_ID', align:'center',renderer:mylink},
				{header: "备注", dataIndex: 'APPLY_REMARK', align:'center',renderer:setApplyRemark},
				{header: "发运结算省市县",dataIndex: 'DLV_BAL_ADDR',align:'center'},
				//{header: "发运结算城市",dataIndex: 'CITY_NAME',align:'center'},
				//{header: "发运结算区县",dataIndex: 'COUNTY_NAME',align:'center'},
				{header: "交接单号", dataIndex: 'BILL_NO', align:'center'},
				 {
					header: "挂帐单号", dataIndex: 'BAL_NO', align:'center',
					renderer: function(value, metaData, record) {
						var url = '<%=contextPath%>/sales/storage/balancemanage/SalesBalanceAction/showBalanceView.do?balId=' + record.data.BAL_ID;
						return "<a href='javascript:void(0);' onclick='viewOrderInfo(\""+url+"\")'>"+value+"</a>";
					}
				},
				 {header: "承运商", dataIndex: 'LOGI_NAME', align:'center'},
				 {header: "挂账月份", dataIndex: 'BAL_MONTH', align:'center'},
				 {header: "运输数量", dataIndex: 'BAL_COUNT', align:'center'},
				 {header: "挂账金额", dataIndex: 'BAL_AMOUNT', align:'center'},
				 {header: "补款金额", dataIndex: 'SUPPLY_MONEY', align:'center'},
				 {header: "扣款金额", dataIndex: 'DEDUCT_MONEY', align:'center'},
				 {header: "其他金额", dataIndex: 'OTHER_MONEY', align:'center'},
				 {header: "结算金额", dataIndex: 'SUM_AMOUNT', align:'center'}
				 //{header: "是否调整", dataIndex: 'IS_CHANGE_TXT', align:'center'}
		];
		//全选checkbox
		function myCheckBox(value,metaDate,record){
			var balId=record.data.BAL_ID;
			return String.format("<input type='checkbox' id='groupIds' name='groupIds' value='" + value + "' /><input type='hidden' name='hiddenIds' value='" + value + "' /><input type='hidden' name='balIds' value='"+balId+"' />");
		}
		function selectProv(value,metaDate,record){
			genLocSel('txt1'+record.data.BILL_ID,'txt2'+record.data.BILL_ID,'txt3'+record.data.BILL_ID);//支持火狐
			//省市
			_genCity(value,'txt2'+record.data.BILL_ID);
			var pro = document.getElementById("txt1"+record.data.BILL_ID);
			for(var i = 0;i<pro.length;i++){
				if(pro[i].value == value){
					pro[i].selected = true;
					break;
				}
			}
			var strOption="<select class='u-select' id='txt1'"+record.data.BILL_ID+" name='JS_PROVINCE' onchange='_genCity(this,\'txt2\'"+record.data.BILL_ID+")'></select>";
			return String.format(strOption);
		}
		function selectCity(value,metaDate,record){
			//地级市
			_genCity(value,'txt3'+record.data.BILL_ID);
			var city = document.getElementById("txt2"+record.data.BILL_ID);
			for(var i = 0;i<city.length;i++){
				if(city[i].value == value){
					city[i].selected = true;
					break;
				}
			}
			return String.format("<select class='u-select' id='txt2'"+record.data.BILL_ID+" name='JS_CITY' onchange='_genCity(this,\'txt3\'"+record.data.BILL_ID+")'></select>");
		}
		function selectCounty(value,metaDate,record){
			var county = document.getElementById("txt3"+record.data.BILL_ID);
			for(var i = 0;i<county.length;i++){
				if(county[i].value == value){
					county[i].selected = true;
					break;
				}
			}
			return String.format("<select class='u-select' id='txt3'"+record.data.BILL_ID+" name='JS_COUNTY'></select>");
		}
		function mylink(value,meta,record){
			var str = "<a href='javascript:void(0);' class='u-anchor' onclick='modifyDo("+value+");'>修改</a>";
			return String.format(str);
		}
		//设置申请备注
		function setApplyRemark(value,metaDate,record){		
			return String.format("<input type='text' name='remarks' id='remark"+record.data.BILL_ID+"' value='"+value+"' class='middle_txt'/>");
		}
		//设置其他金额
		function setOtherMoney(value,metaDate,record){
			
			return String.format("<form method='post' name='fm"+record.data.BILL_ID+"' id='fm"+record.data.BILL_ID+"'><input type='text' name='OM"+record.data.BILL_ID+"' id='OM"+record.data.BILL_ID+"' value='"+value+"' class='middle_txt' datatype='0,isMoney,20'/></form>");
		}
		//修改财务结算地
		function  modifyDo(billId){
			var otherMoney=document.getElementById("OM"+billId).value;
			//MyAlert(otherMoney);
			if(submitForm('fm'+billId)) {
				var url = '<%=contextPath%>/sales/storage/balancemanage/SalesBalanceApply/toEditBalAddr.do?billId=' +billId+'&otherMoney='+otherMoney;
				OpenHtmlWindow(url,1000,300);
			}
		}
		//挂账单
		function subBill(){
			var b=0;
			var arrayObj = new Array(); 
			arrayObj=document.getElementsByName("groupIds");
			
			for(var i=0;i<arrayObj.length;i++){
				if(arrayObj[i].checked){
					b=1;//有选中
				}
				
			}
			if(b==0){
				MyAlert("请选择提交记录！");
				return ;
			}
			//for(var i=0;i<arrayObj.length;i++){
			//	if(!submitForm('fm'+arrayObj[i].value)) {
			//		return;
			//	}
			//}
			MyConfirm("确认提交？",subBillDo);
		}
		function subBillDo(){
			var url  ="<%=request.getContextPath()%>/sales/storage/balancemanage/SalesBalanceApply/subBill.json?";
			sendAjax(url,function(json){
				if(json.returnValue=='1'){
					MyAlertForFun("提交成功！",function(){
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



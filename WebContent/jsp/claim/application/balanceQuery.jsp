<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.po.TtAsWrApplicationPO"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	List<TtAsWrApplicationPO> ls = (List<TtAsWrApplicationPO>)request.getAttribute("ids");
	request.setAttribute("ids",ls);
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>索赔申请单结算</TITLE>
<script type="text/javascript">
var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/application/BalanceQuery/balanceQuery.json";
				
	var title = null;

	var columns = [
					//{header: "起始号", width:'20%', dataIndex: 'startClaim',renderer:roLine},
					{header: "经销商代码", width:'10%', dataIndex: 'dealerCode'},
					{header: "经销商简称", width:'15%', dataIndex: 'dealerShortname'},
					{header: "产地", width:'15%', dataIndex: 'procFactory',renderer:getItemValue},
					{header: "结算单号", width:'5%', dataIndex: 'balanceNo'},
					{header: "结算日期", width:'5%', dataIndex: 'balanceDate',renderer:formatDate},
					{header: "发票号", width:'5%', dataIndex: 'invoiceNo'},
					{header: "费用(元)", width:'5%', dataIndex: 'balance',renderer:formatCurrency}
					//{header: "索赔申请金额", width:'5%', dataIndex: 'repairTotals'},
					//{header: "税额", width:'5%', dataIndex: 'taxSums'},
					//{header: "总金额", width:'5%', dataIndex: 'grossCredits'}
		      ];
		      
	//设置超链接  begin      
	function doInit(){
   		loadcalendar();
	}
	//起始号渲染
	function roLine(value,metadata,record) {
		return value+"-"+record.data.endClaim;
	}
	//单位代码渲染函数
	function dezero(value,metadata,record) {
		if (value==0){
			return "";
		}else {
			return value;
		}
	}
	//上报
	function submitId(){
		MyConfirm("索赔申请单结算？",submitApply);
	}
	//上报操作
	function submitApply () {
		var fm = document.getElementById("fm");
		fm.action = '<%=request.getContextPath()%>/claim/application/ClaimBillCount/claimBillCount.json';
		fm.submit();
		//makeNomalFormCall('<%=request.getContextPath()%>/claim/application/ClaimBillCount/claimBillCount.json','fm','queryBtn');
	}
	//上报回调函数
	function returnBack0(json){
		var avl = json.returnValue;
		if(avl==1){
			__extQuery__(1);
			MyAlert("上报成功！");
		}else{
			MyAlert("上报失败！请联系管理员！");
		}
	}
	//删除
	function deleteId(){
	var str=getCheckedToStr("orderIds");
		if (str!=""){
		MyConfirm("确认删除？",deleteApply,[str]);
		}else {
			MyAlert("请选择至少一条要删除的申请单！");
		}
	}
	function deleteApply(str) {
		
			makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyDelete.json?orderIds='+str,returnBack,'fm','queryBtn');
		
	}
	//删除回调函数
	function returnBack(json){
		var avl = json.returnValue;
		if(avl==1){
			__extQuery__(1);
			MyAlert("删除成功！");
		}else{
			MyAlert("删除失败！请联系管理员！");
		}
	}
	//回调函数
	function showResult(json){
		if(json.ACTION_RESULT == '1'){
			goBack();
			MyConfirm("新增成功！点击确认返回查询界面或者点击左边菜单进入其他功能！","window.location.href = '<%=request.getContextPath()%>/sysmng/orgmng/DlrInfoMng/queryAllDlrInfo.do'");
		}else if(json.ACTION_RESULT == '2'){
			MyAlert("新增失败！请重新载入或者联系系统管理员！");
		}
	}
	//返回
	function goBack(){
		window.location.href = "<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyQuery.json";
	}
	
	//格式化时间为YYYY-MM-DD
	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
	//清空经销商框
		function clearInput(){
			var target = document.getElementById('dealerCode');
			target.value = '';
		}
	
//设置超链接 end
</script>
</HEAD>
<BODY onload="doInit()">
<div class="navigation">
<img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔申请&gt;结算清单查询</div>
  
  <form method="post" name = "fm" id="fm">


  <TABLE align="center" class="table_query" >
          <tr>
            <td class="table_query_2Col_label_6Letter" > 经销商代码：</td>
            <td align="left" >
            <textarea id="dealerCode"  name="DEALER_CODE" rows="3" cols="20" datatype="1,is_null,2000"></textarea>
            <input name="button1" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
            <input name="button2" type="button" class="normal_btn" onClick="clearInput();" value="清除"/>   
            </td>
              <td class="table_query_2Col_label_6Letter">经销商名称：</td>
            <td><input name="DEALER_NAME" id="DEALER_NAME" value="" type="text" class="middle_txt" datatype="1,is_digit_letter_cn" />
            </td>
          </tr>
          <tr>
            <td  class="table_query_2Col_label_6Letter">结算单号：</td>
            <td >
            	<input name="BALANCE_NO" id="BALANCE_NO" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,20" />
 		    </td> 
            <td  class="table_query_2Col_label_6Letter">截止日期：</td>
            <td colspan="3">
            	<input name="CON_LAST_DAY" type="text" id="CON_LAST_DAY"  class="middle_txt"  datatype="1,is_date,10"  hasbtn="true" callFunction="showcalendar(event, 'CON_LAST_DAY', false);" />
      		</td> 
			</tr>
			<tr>
			<td class="table_query_2Col_label_5Letter">
			产地：
			</td>
			<td>
			<script type="text/javascript">
			genSelBoxExp("YIELDLY",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"min_sel","","false",'');
			</script>
			</td>
			<td class="table_query_2Col_label_6Letter">
			抵扣结算：
			</td>
			<td>
			<input type="checkbox" name="REDUCTION_FLAG"  />
			</td>
			</tr>
         	<tr>
         	<tr>
         	<td colspan="4" align="center">
         	<input id="queryBtn" class="normal_btn"  type="BUTTON" value="查询" name="recommit" onClick="__extQuery__(1);"/>
         	 </td>
        	</tr>
 	</table>
  	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>

</BODY>


</html>
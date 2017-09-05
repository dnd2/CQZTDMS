<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>索赔申请单结算</TITLE>
<script type="text/javascript">
var ids;
var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/application/ClaimBillCount/claimBillCountQuery.json";
				
	var title = null;

	var columns = [
					{header: "起始号", width:'20%', dataIndex: 'startClaim',renderer:roLine},
					{header: "经销商代码", width:'10%', dataIndex: 'dealerCode',renderer:getId},
					{header: "经销商简称", width:'15%', dataIndex: 'dealerShortname'},
					{header: "产地", width:'15%', dataIndex: 'yieldly',renderer:getItemValue},
					{header: "申请单数", width:'5%', dataIndex: 'counts'},
					{header: "工时金额(元)", width:'5%', dataIndex: 'labourAmounts',renderer:formatCurrency},
					{header: "配件金额(元)", width:'5%', dataIndex: 'partAmounts',renderer:formatCurrency},
					{header: "其他费用(元)", width:'5%', dataIndex: 'netitemAmounts',renderer:formatCurrency},
					{header: "索赔申请金额(元)", width:'5%', dataIndex: 'repairTotals',renderer:formatCurrency},
					{header: "税额%", width:'5%', dataIndex: 'taxSums',renderer:getFeeDealerId},
					{header: "总金额(元)", width:'5%', dataIndex: 'grossCredits',renderer:formatCurrency}
		      ];
		      
	//设置超链接  begin      
	function doInit(){
   		loadcalendar();
	}
	function getId(value,metadata,record) {
		document.getElementById("ids").value=record.data.ids;
		return value;
	}
	function getFeeDealerId(value,metadata,record) {
		return String.format(value+"<input  type='hidden' name='fee' value='" + record.data.grossCredits + "' /><input  type='hidden' name='yieldly' value='" + record.data.yieldly + "' /><input  type='hidden' name='dealerId' value='" + record.data.dealerId + "' />");;
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
	//清空经销商框
		function clearInput(){
			var target = document.getElementById('dealerCode');
			target.value = '';
		}
	
	
	
	//取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	function getCheckedToStr(name) {
		var str="";
		var chk = document.getElementsByName(name);
		if (chk==null){
			return "";
		}else {
		var l = chk.length;
		for(var i=0;i<l;i++){        
			if(chk[i].checked)
			{            
			str = chk[i].value+","+str; 
			}
		}
			return str;
		}
	}
	//上报
	function submitId(){
		MyConfirm("索赔申请单结算？",submitApply);
	}
	//上报操作
	function submitApply () {
		var fm = document.getElementById("fm");
		fm.action = '<%=request.getContextPath()%>/claim/application/ClaimBillCount/claimBillCount.do';
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
//设置超链接 end
</script>
</HEAD>
<BODY onload="doInit()">
<div class="navigation">
<img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔申请&gt;索赔申请单结算</div>
  
  <form method="post" name = "fm" id="fm">
<input type="hidden" name="ids" id="ids" value=""/>

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
            <td  class="table_query_2Col_label_6Letter">索赔类型：</td>
            <td >
			<script type="text/javascript">
	              genSelBoxExp("CLAIM_TYPE",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","true",'');
	       </script>
 		    </td> 
            <td  class="table_query_2Col_label_6Letter">截止日期：</td>
            <td colspan="3">
            	<input name="CON_LAST_DAY" type="text" id="CON_LAST_DAY"  class="middle_txt"  datatype="1,is_date,10"  hasbtn="true" callFunction="showcalendar(event, 'CON_LAST_DAY', false);" />
      		</td> 
			</tr>
			<tr>
			<td class="table_query_2Col_label_6Letter">
			产地：
			</td>
			<td>
			<script type="text/javascript">
			genSelBoxExp("YIELDLY",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"min_sel","","false",'');
			</script>
			</td>
			</tr>
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
<form name="form1" style="display:none">
<table id='bt' class="table_list">
<tr><th align="center"><p>
    <input class="normal_btn" type=button value='结算' onclick='submitId()' name=modify />
  </p></th>
  </tr>
</table>
</form>
</BODY>
<SCRIPT LANGUAGE="JavaScript">
	document.form1.style.display = "none";
		
	var HIDDEN_ARRAY_IDS=['form1'];
</script>

</html>
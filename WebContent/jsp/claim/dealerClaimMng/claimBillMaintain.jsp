<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>索赔单编辑</TITLE>
<SCRIPT LANGUAGE="JavaScript">
	var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/applicationQuery.json";
				
	var title = null;

	var columns = [
					{header: "序号",sortable: false,align:'center',renderer:getIndex},
					{header: "索赔申请单号", width:'15%', dataIndex: 'claimNo'},
					{header: "工单号", width:'15%', dataIndex: 'roNo',renderer:roLine},
					{header: "结算基地", width:'7%', dataIndex: 'balanceYieldly',renderer:getItemValue},
					{header: "索赔类型", width:'7%', dataIndex: 'claimType',renderer:getItemValue},
					{header: "修改次数", width:'5%', dataIndex: 'submitTimes'},
					{header: "VIN", width:'15%', dataIndex: 'vin'},
					{header: "建单时间", width:'15%', dataIndex: 'createDate',renderer:formatDate},
					{header: "申请状态", width:'15%', dataIndex: 'status',renderer:getItemValue},
					{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'id',renderer:myLink1,align:'center'}
		      ];
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
	}
	//行号加工单号
	function roLine(value,metadata,record) {
		//return value+"-"+record.data.lineNo;
		return value;
	}
	//单位代码渲染函数
	function dezero(value,metadata,record) {
		if (value==0){
			return "";
		}else {
			return value;
		}
	}
	//格式化时间为YYYY-MM-DD
	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
	//修改的超链接设置
	function myLink1(value,meta,record){
		if (record.data.status=='<%=Constant.CLAIM_APPLY_ORD_TYPE_05%>') {
			return String.format("<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailForward.do?ID="
				+ value + "\",800,500)'>[明细]</a>");
		}else if(record.data.status=='<%=Constant.CLAIM_APPLY_ORD_TYPE_03%>')
		{
		  return String.format("<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailForward.do?ID="
				+ value + "\",1000,500)'>[明细]</a><a href=\"#\" onclick='reback(\""+value+"\")'>[撤销上报]</a>");
		}else {
			var claimNo = record.data.claimNo;
  			return String.format("<a href=\"<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/claimBillModifyForward.do?claimNo="+claimNo+"&ID="+ value +(record.data.status == "<%=Constant.CLAIM_APPLY_ORD_TYPE_06%>" ? "&flag=1" : "") + "\">[修改]</a>");
		}
	}
	//设置超链接
	function mySelect(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.orderId+"\")'>"+ value +"</a>");

	}
	//具体操作
	function sel(){
		MyAlert("超链接！");
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
	var str=getCheckedToStr("orderIds");
		if (str!=""){
		MyConfirm("确认上报？",submitApply,[str]);
		}else {
			MyAlert("请选择至少一条要上报的申请单！");
		}
	}
	//上报操作
	function reback(val)
	{
	  makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/rebackSubmit.json?id='+val,rebackname,'fm','queryBtn');
	}
	function rebackname(json)
	{
	  __extQuery__(1);
	  MyAlert(json.bug);
	 
	}
	
	function submitApply (str) {
		
			makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplySubmit.json?orderIds='+str,returnBack0,'fm','queryBtn');
		
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
	function notice(){
		var msg = document.getElementById("msg").value;
		if(msg!=null&& msg!=""){
		MyAlert(msg);
		document.getElementById("msg").value="";
		}
	}
	
	function queryPer(){
	var star = $('RO_STARTDATE').value;
	var end = $('RO_ENDDATE').value;
	  if(star==""||end ==""){
	  	MyAlert("查询时间必须选择");
	 	 return false;
	  }else if(star>end){
	  	MyAlert("开始时间不能大于结束时间");
	  	return false;
	  }else {
	   var s1 = star.replace(/-/g, "/");
		var s2 = end.replace(/-/g, "/");
		var d1 = new Date(s1);
		var d2 = new Date(s2);
		var time= d2.getTime() - d1.getTime();
		var days = parseInt(time / (1000 * 60 * 60 * 24));
		if(days>=93){
		MyAlert("时间跨度不能超过3个月");
	  		return false;
		}
	 	 __extQuery__(1);
	  }
	}
//设置超链接 end
</script>
</HEAD>
<BODY onload="doInit();notice();">
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔管理&gt;索赔单编辑</div>
  <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
    <input name="msg" id="msg" value="${msg }" type="hidden" />
          <tr>
            <td  class="table_query_2Col_label_5Letter">索赔单号：</td>
            <td><input name="CLAIM_NO" id="CLAIM_NO" maxlength="22" value="" type="text" class="middle_txt" />
            </td>
            <td  class="table_query_2Col_label_7Letter">工单号：</td>
            <td  align="left" ><input type="text" name="RO_NO" maxlength="21" id="RO_NO"   value="" class="middle_txt"/>
            <INPUT name="LINE_NO" id="LINE_NO" value="" type="hidden"  datatype="1,is_digit" class="mini_txt"/></td>	            
          </tr>
          <tr>
            <td  class="table_query_2Col_label_5Letter">索赔类型：</td>
            <td >
			<script type="text/javascript">
	              genSelBoxExp("CLAIM_TYPE",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","false",'');
	       </script>
 		    </td> 	         
 			<td  class="table_query_2Col_label_7Letter">VIN：</td>
 			<td  align="left">
 			<!--  <input name="VIN" id="VIN" datatype="1,is_vin" type="text"  value="" class="middle_txt"/>-->
 			<textarea name="VIN" cols="18" rows="3" datatype="1,is_digit_letter"></textarea>
 			</td>
          </tr>                   
          <tr>
             <td class="table_query_2Col_label_5Letter">建单日期：</td>
              <td align="left" nowrap="true">
			<input name="RO_STARTDATE" type="text" class="short_time_txt" id="RO_STARTDATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'RO_STARTDATE', false);" />  	
             &nbsp;至&nbsp; <input name="RO_ENDDATE" type="text" class="short_time_txt" id="RO_ENDDATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'RO_ENDDATE', false);" /> 
		</td>
            <td  class="table_query_2Col_label_7Letter">索赔单状态：</td>
  			<td >
  			<script type="text/javascript">
	              genSelBoxExp("STATUS",<%=Constant.CLAIM_APPLY_ORD_TYPE%>,"",true,"short_sel","","false",'<%=Constant.CLAIM_APPLY_ORD_TYPE_02%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_03%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_04%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_10%>');
	       </script>
  			</td>
        
          </tr>
    	  <tr>
            <td colspan="4" align="center" nowrap><input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="queryPer();" />
			<!-- <input class="normal_btn"  type="button" name="button1" value="新增" id="addBtn"  onClick="openItem();"/> -->
			</td>
            <td  align="right" ></td>
  </table>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
<script type="text/javascript">
function openItem(){
			OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/chooseRoForward.do',1000,500);
		}
		function setRoNo(ro_id,ro_no) {
			if(ro_id==""){
				MyAlert("获取工单标识错误,请重新选择!");
				return;
			}else{
			$('addBtn').disabled=true;
			$('queryBtn').disabled=true;
			
			window.location.href = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changeRoDetail2.do?roId="+ro_id;
			}
		}
function   showMonthFirstDay()     
{     
	  var   Nowdate=new   Date();     
	  var   MonthFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth(),1);     
	  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
}     
function   showMonthLastDay()     
{     
	  var   Nowdate=new   Date();     
	  var   MonthNextFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
	  var   MonthLastDay=new   Date(MonthNextFirstDay-86400000);     
	  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
}     
$('RO_STARTDATE').value=showMonthFirstDay();
$('RO_ENDDATE').value=showMonthLastDay();
</script>
</BODY>
</html>
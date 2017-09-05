<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TmBusinessAreaPO,java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/change" prefix="change" %>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>冬季保养维护</TITLE>

<SCRIPT LANGUAGE="JavaScript">
var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/application/ClaimBillStatusTrack/negativeStockQuery.json";
				
	var title = null;

	var columns = [
					{header: "序号", width:'10%',renderer:getIndex},
					{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"recesel\")' />全选", align:'center',sortable:false, dataIndex:'ID',width:'5%',renderer:checkBoxShow},
					{header: "经销商代码", width:'15%', dataIndex: 'DEALER_CODE'},
					{header: "经销商名称", width:'15%', dataIndex: 'DEALER_SHORTNAME'},
					{header: "负库存", width:'15%', dataIndex: 'PART_NUM',renderer:formartNum}
					
		      ];
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
   		__extQuery__(1);
	}
	//行号加工单号
	function roLine(value,metadata,record) {
		//return value+"-"+record.data.lineNo;
		return value;
	}
	
	function multModNum() {
		var str = "";
		var chk = document.getElementsByName('recesel');
		var l = chk.length;
		for(var i=0;i<l;i++){        
			if(chk[i].checked)
			{
			str = chk[i].value + "@" + chk[i].nextSibling.value + "," + str;
			}
		}
		var id = str.substring(0,str.length-1);
		if (id == "") {
			MyAlert("请选择至少一个!");
			return false;
		}
		var mutilPartNum = document.getElementById("mutilPartNum").value;
		if (mutilPartNum == "") {
			MyAlert("请输入批量修改的负库存数");
			return false;
		}
		$('multModBtn').disabled = "disabled";
		var url="<%=contextPath%>/claim/application/ClaimBillStatusTrack/multInsertOrUpdateStock.json?id="+id+"&mutilPartNum="+mutilPartNum;
		makeNomalFormCall(url,returnBack3,'fm');
	}
	
	
	function formartNum(value,meta,record) {
		return String.format("<input type='text' id='partNum"+record.data.DEALER_ID+"' name='partNum"+record.data.DEALER_ID+"' onblur=changeNum("+"\'"+record.data.ID+"\'"+","+"\'"+record.data.DEALER_ID+"\'"+") value='" + record.data.PART_NUM + "' />");

	}
	
	function changeNum(id,dealerId) {
		var partNum = document.getElementById("partNum"+dealerId).value;
		showMask();
		$('multModBtn').disabled = "disabled";
		makeNomalFormCall('<%=contextPath%>/claim/application/ClaimBillStatusTrack/insertOrUpdateStock.json?id='+id+'&dealerId='+dealerId+'&partNum='+partNum,returnBack2,'fm','');
	}
	 function checkBoxShow(value,meta,record){
			return String.format("<input type='checkbox' id='recesel' name='recesel' value='" + record.data.ID + "' /><input type='hidden' value='" + record.data.DEALER_ID + "' />");
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
			return value.substr(0,16);
		}
	}
	function formatDate2(value,meta,record) {
		if(record.data.status =='<%=Constant.CLAIM_APPLY_ORD_TYPE_03%>'){
			return '';
		}else if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,16);
		}
	}
	//工单的超链接
	function myLink(value,meta,record){
        return String.format(
               value);
    }
	//修改的超链接设置
	function myLink1(value,meta,record){
		var width=600;
		var height=300;
		var str="";
		var screenW = window.screen.width-30;	
		var screenH = document.viewport.getHeight();
	var isImport = record.data.isImport;
	var roNo = record.data.roNo;
		if(screenW!=null && screenW!='undefined')
			width = screenW;
		if(screenH!=null && screenH!='undefined')
			height = screenH;
		if(record.data.status==<%=Constant.SERVICEACTIVITY_STATUS_02%>){
			return String.format("<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/application/ClaimBillStatusTrack/winterMaintenDetailForward.do?id="+record.data.id+ "\","+width+","+height+")' >[明细]</a>");
		}else{
  			return String.format("<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/application/ClaimBillStatusTrack/winterMaintenDetailForward.do?id="+record.data.id+ "\","+width+","+height+")' >[明细]</a><a href=\"#\" onclick=\"modWinter('"+record.data.id+"')\">[修改]</a><a href=\"#\" onclick=\"delWinter('"+record.data.id+"')\">[删除]</a><a href=\"#\" onclick=\"publishWinter('"+record.data.id+"')\">[发布]</a>");
		}
	}
	
	function modWinter(id) {
		location="<%=contextPath%>/claim/application/ClaimBillStatusTrack/winterMaintenModForward.do?id="+id;   

	}
	
	function publishWinter(id) {
		var str = id;
		MyConfirm("确认发布？",publishWinter2,[str]);
	}
	
	function publishWinter2(str) {
		makeNomalFormCall('<%=contextPath%>/claim/application/ClaimBillStatusTrack/publishWinter.json?id='+str,returnBack2,'fm','');
	}
	
	function showPrintPage1(claimId){ 
        
        var printUrl = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/barcodePrintDoGet.do?dtlIds="+claimId;
        window.open(printUrl,'','toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=1200'); 

      }
	function delWinter(id) {
		var str = id;
		MyConfirm("确认删除？",delWinter2,[str]);
	}
	function delWinter2(str) {
		makeNomalFormCall('<%=contextPath%>/claim/application/ClaimBillStatusTrack/delWinter.json?id='+str,returnBack1,'fm','');
	}
	
	function returnBack1(json){
		if(json.result == 'success'){
			__extQuery__(1);
			MyAlert("删除成功!");
		}else{
			MyAlert("删除失败！请联系管理员！");
		}
	}
	function returnBack2(json){
		if(json.result == 'success'){
			__extQuery__(json.num1);
			$('multModBtn').disabled = "";
			MyAlert("更新成功");
		}else{
			$('multModBtn').disabled = "";
			MyAlert("更新失败！请联系管理员！");
		}
	}
	
	function returnBack3(json){
		if(json.result == 'success'){
			__extQuery__(json.num1);
			$('multModBtn').disabled = "";
			MyAlert("更新成功");
		}else{
			$('multModBtn').disabled = "";
			MyAlert("更新失败！请联系管理员！");
		}
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
		showMask();
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
	//清空经销商框
		function clearInput(){
			var target = document.getElementById('dealerCode');
			target.value = '';
		}
	//返回
	function goBack(){
		window.location.href = "<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyQuery.json";
	}
	function queryPer(){
	var star = $('start_date').value;
	var end = $('end_date').value;
	  if(star==""||end ==""){
		  __extQuery__(1);
	  }else if(star>end){
	  	MyAlert("开始时间不能大于结束时间");
	  	return false;
	  }else {
	 	 __extQuery__(1);
	  }
	}
//设置超链接 end
</SCRIPT>

</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;负库存维护</div>
    <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
         <tr>
         <input type="hidden" id="curPage" name="curPage" value="${currP }">
          <td class="table_query_2Col_label_6Letter">经销商代码：</td>
		    <td>
		    	<input type="text" class="middle_txt" name="dealerCode" value="" maxlength="25" />
		    </td>
		    
           <td class="table_query_2Col_label_6Letter">经销商名称：</td>
		    <td>
		    	<input type="text" class="middle_txt" name="dealerName" value="" maxlength="25" />
		    </td>
          
          </tr>
    	  <tr>
            <td colspan="4" align="center" nowrap><input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="__extQuery__(1);" />
            <input id="multModBtn" class="normal_btn" type="button" name="button" value="批量修改"  onClick="multModNum();" />
            	批量修改的负库存数：<input type="text" class="middle_txt" id="mutilPartNum" name="mutilPartNum" value="" maxlength="25" />
			</td>
            <td  align="right" ></td>
          </tr>
  </table>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
<script type="text/javascript">
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

//新增
function subFun(){
	location="<%=contextPath%>/claim/application/ClaimBillStatusTrack/winterMaintenAdd.do";   
}
</script>
</BODY>
</html>
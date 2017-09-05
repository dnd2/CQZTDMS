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
<TITLE>索赔预授权审核</TITLE>
<SCRIPT LANGUAGE="JavaScript">
	var myPage;
	//查询路径
	var url = "<%=contextPath%>/repairOrder/RoMaintainMain/queryRepairOrder1.json";
				
	var title = null;

	var columns = [
				//	{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"recesel\")' />全选", align:'center',sortable:false, dataIndex:'id',width:'2%',renderer:checkBoxShow},
					{header: "序号",sortable: false,align:'center',renderer:getIndex},
					{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'id',renderer:myLink1,align:'center'},
					{header: "经销商名称", width:'15%', dataIndex: 'dealerName'},
					{header: "车型", width:'15%', dataIndex: 'modelCode'},
					{header: "预授权申请单号", width:'15%', dataIndex: 'foNo'},
					{header: "工单号", width:'15%', dataIndex: 'roNo'},
					{header: "维修类型", width:'7%', dataIndex: 'approvalType',renderer:getItemValue},
					{header: "结算基地", width:'7%', dataIndex: 'balanceYieldly',renderer:getItemValue},
					{header: "保养次数", width:'7%', dataIndex: 'freeTime'},
					{header: "申请时间", width:'7%', dataIndex: 'approvalDate',renderer:formatDate2},
					{header: "入厂里程数", width:'7%', dataIndex: 'inMileage'},
					{header: "VIN", width:'15%', dataIndex: 'vin'},
					{header: "预警", width:'15%', dataIndex: 'isWarning',renderer:getItemValue},
					{header: "授权状态", width:'15%', dataIndex: 'reportStatus',renderer:getItemValue},
					{header: "最终审核", width:'15%', dataIndex: 'lastName'},
					{header: "材料费", width:'15%', dataIndex: 'repairPartAmount'},
					{header: "工时费", width:'15%', dataIndex: 'labourAmount'},
					{header: "辅料费", width:'15%', dataIndex: 'accessoriesPrice'},
					{header: "其他费用", width:'15%', dataIndex: 'addItemAmount'},
					{header: "总费用", width:'15%', dataIndex: 'repairAmount'}
					
		      ];
__extQuery__(1);
	//里程差异
	function isaaa(value,metadata,record){
		var a = record.data.startMileage;
		var b = record.data.endMileage;
		var c = record.data.inMileage;
		if(Number(c)<Number(a)){
			return String.format('-'+(Number(a)-Number(c)));
		}else if(Number(c)>Number(b)){
			return String.format(Number(c)-Number(b));
		}else{
			return String.format('0');
		}
	}
	function forMate(value,metadata,record){
		if(value==""){
			return "";
		}else{
		return value.substring(0,10);
		}
	}
	//天数差异
	function isTs(value,metadata,record){
		var q = record.data.differencestime;
		var w = record.data.maxDays;
		var e = record.data.minDays;
		if(Number(q)<Number(e)){
			return String.format('-'+(Number(e)-Number(q)));
		}else if(Number(q)>Number(w)){
			return String.format(Number(q)-Number(w));
		}else{
			return String.format('0');
		}
	}
	//设置超链接  begin      
	function doInit(){
   		loadcalendar();
   		__extQuery__(1);
   		//genLocSel('provinceId','','','','',''); // 加载省份城市和县
	}
	function notice(){
	var tis = document.getElementById("notice").value;
	if(tis!=null&&tis!=""){
	MyAlert(tis);
	}
	}
	//行号加工单号
	function roLine(value,metadata,record) {
		return value+"-"+record.data.lineNo;
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
		//格式化时间为YYYY-MM-DD
	function formatDate2(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,16);
		}
	}
	//修改的超链接设置
	function myLink1(value,meta,record){
		
			if ('<%=Constant.RO_FORE_01%>'==record.data.reportStatus) {
				var rec =record.data.roNo;
				var createName = record.data.createName;
				var fid=value;
			    var htmlStr="<input type=\"button\" value=\"审核\" onclick=\"cun('"+fid+"','"+rec+"','"+createName+"');\" class=\"normal_btn\"/>&nbsp;";
				return String.format(htmlStr);
				
			}else  {
				return String.format("<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail1.do?type=1&roNo="+record.data.roNo+"&FID="
				+ value + "\">[明细]</a>");
			}
	}

	function cun(fid,rec,createName){
		document.getElementById('fm').action="<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail1.do?type=2&roNo="+rec+"&FID="+fid+"&createName="+createName;
		document.getElementById('fm').submit();
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

	function clearInput(){
	$('dealerCode').value='';$('dealerId').value='';

	}
	function clrTxt(){
	$('orgCode').value='';
	$('orgId').value='';
	}
//设置超链接 end

//设置复选框
function checkBoxShow(value,meta,record){
	return String.format("<input type='checkbox' id='recesel' name='recesel' value='" + record.data.id + "' />");
}
function addApproval() {
	var allChecks = document.getElementsByName("recesel");
	var optiona = document.getElementById("optiona").value;
	var allFlag = false;
	for(var i = 0;i<allChecks.length;i++){
		if(allChecks[i].checked){
			allFlag = true;
		}
	}
		
	if(allFlag){
	if(optiona==null||optiona==""){
		MyConfirm("尚未填写审核内容,确认批量审批?",changeSubmit);
		}else{
			MyConfirm("确认批量审批?",changeSubmit);
			}
	}else{
		MyAlert("请选择数据后再点击操作批量审核按钮！");
	}
}
function changeSubmit() {
	var url="<%=request.getContextPath()%>/repairOrder/RoMaintainMain/updateForStatus2.json";
	makeNomalFormCall(url,showResult22,'fm');
}
function showResult22(json){
	var msg=json.msg;
	if(msg=='01'){
		MyAlert('批量审核成功');
		__extQuery__(1);
	}else{
		MyAlert('操作失败,请联系管理员');
	}
}

</script>
</HEAD>
<BODY onload="doInit();notice();">
<div class="navigation"><img src="../../img/nav.gif" />&nbsp;当前位置：索赔预授权&gt;索赔预授权审核</div>
  <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
          <tr>
            <td  class="table_query_2Col_label_7Letter">工单号：
            <input type="hidden" value="${flag}" id="FLAG"/>
            </td>
            <td><input name="RO_NO" id="RO_NO" value="" maxlength="20" type="text"  class="middle_txt" />
            </td>
            <td rowspan="2" class="table_query_2Col_label_7Letter" >VIN：</td>
 			<td rowspan="2"  align="left">
 			<!--  <input name="VIN" id="VIN" datatype="1,is_vin" type="text"  value="" class="middle_txt"/>-->
 			<textarea name="VIN" cols="18" rows="3" datatype="1,is_digit_letter"></textarea>
 			<input type="hidden" id = "notice" value="${success }" />
 			</td>
          </tr>
          <tr>
            <td  class="table_query_2Col_label_7Letter">维修类型：</td>
            <td >
	       	<script type="text/javascript">
	              		genSelBoxExp("REPAIR_TYPE",<%=Constant.REPAIR_TYPE%>,"",true,"short_sel","","false",'');
	       			</script>
 		    </td> 	         
          </tr>    
          <tr>
          	<td class="table_query_2Col_label_7Letter">
          		授权状态：
          	</td>
          	<td >
          	  	<script type="text/javascript">
	              genSelBoxExp("RO_FORE",<%=Constant.RO_FORE%>,"",true,"short_sel","","false",'');
	        </script>
          	</td>
          	 <td align="right" nowrap class="table_query_2Col_label_7Letter" id="deId">选择经销商：</td>
	 		<td align="left" nowrap="true" id="deId1">
				<input class="middle_txt" id="dealerCode"  name="dealerCode" type="text" readonly="readonly"/>
				<input class="middle_txt" id="dealerId"  name="dealerId" type="hidden"/>
	            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','dealerId','true','','true','','10771002');" value="..." />        
	            <input name="clrBtn" type="button" class="normal_btn" onClick="clearInput();" value="清除"/>  
			</td>
          </tr>
          <tr>
				<td class="table_edit_2Col_label_7Letter">
										预警：
				</td>          
          		<td>
					 <script type="text/javascript">
				            genSelBoxExp("IS_WARNING",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
				     </script>
				</td>
				<td class="table_edit_2Col_label_7Letter">
										主故障件：
				</td>          
          		<td>
					 <input class="middle_txt" id="part_code"  name="part_code" type="text"  maxlength="30""/>
				</td>
          </tr>
    	  <tr>
            <td colspan="4" align="center" nowrap>
            <input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="__extQuery__(1);" />
          <!--   <input   id="save" name="save" type="button" class="normal_btn"  value="批量审批" onclick="addApproval()"/>
			 审核内容：<input  id="optiona" name="optiona" type="text" class="middle_txt" maxlength="100"  value="" /> --></td>
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
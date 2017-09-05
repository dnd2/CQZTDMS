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
	var url = "<%=contextPath%>/MainTainAction/WinterMaintenance.json?type=query";
				
	var title = null;

	var columns = [
					{header: "序号", width:'10%',renderer:getIndex},
					{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink1,align:'center'},
					{header: "冬季保养单号", width:'15%', dataIndex: 'ID'},
					{header: "车型", width:'15%', dataIndex: 'GROUP_NAME'},
					{header: "冬季保养补助", width:'7%', dataIndex: 'AMOUNT'},
					{header: "开始时间", width:'15%', dataIndex: 'START_DATE',renderer:formatDate},
					{header:'结束时间',width:'15%',dataIndex:'END_DATE',renderer:formatDate2},
					{header: "状态", width:'15%', dataIndex: 'STATUS',renderer:getItemValue}
					
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
	var isImport = record.data.ISIMPORT;
	var roNo = record.data.RONO;
		if(screenW!=null && screenW!='undefined')
			width = screenW;
		if(screenH!=null && screenH!='undefined')
			height = screenH;
		if(record.data.STATUS==<%=Constant.SERVICEACTIVITY_STATUS_02%>){
			return String.format("<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/MainTainAction/winterMaintenView.do?id="+record.data.ID+ "\","+width+","+height+")' >[明细]</a><a href=\"#\" onclick=\"modWinter('"+record.data.ID+"','"+record.data.STATUS+"')\">[修改]</a>");
		}else{
  			return String.format("<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/MainTainAction/winterMaintenView.do?id="+record.data.ID+ "\","+width+","+height+")' >[明细]</a><a href=\"#\" onclick=\"modWinter('"+record.data.ID+"','"+record.data.status+"')\">[修改]</a><a href=\"#\" onclick=\"delWinter('"+record.data.ID+"')\">[删除]</a><a href=\"#\" onclick=\"publishWinter('"+record.data.ID+"')\">[发布]</a>");
		}
	}
	
	function modWinter(id,status) {
		location="<%=contextPath%>/MainTainAction/winterMaintenAdd.do?id="+id+"&status="+status+"&type=update";   

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
			__extQuery__(1);
			MyAlert("发布成功!");
		}else{
			MyAlert("发布失败！请联系管理员！");
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

<div class="navigation"><img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;冬季保养维护</div>
    <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
         <tr>
          <td class="table_query_2Col_label_6Letter">冬季保养补助：</td>
		    <td>
		    	<input type="text" class="middle_txt"name="amount" value="" maxlength="25" />
		    </td>
		    
          <td class="table_query_2Col_label_6Letter" >开始时间：</td>
           <td align="left" nowrap="true">
			<input name="start_date" type="text" class="short_time_txt" id="start_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'start_date', false);" />  	
             &nbsp;至&nbsp; <input name="end_date" type="text" class="short_time_txt" id="end_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'end_date', false);" /> 
			</td>	
			<td>
			</td>
			<td>
			</td>
          
          </tr>
          <tr>
          <td class="table_query_2Col_label_6Letter">冬季保养单号：</td>
		    <td>
		    	<input type="text" class="middle_txt"name="winter_no" value="" maxlength="25" />
		    </td>
		    
          <td class="table_query_2Col_label_6Letter" >状态：</td>
           <td align="left" nowrap="true">
              <select id="status" name="status" class="short_sel">
					<option value=''>-请选择-</option>
					<c:forEach var="st" items="${statusList}">
						<option value="${st.CODE_ID}" title="${st.CODE_ID}">${st.CODE_DESC}</option>
					</c:forEach>
				</select>
			</td>	
			<td class="table_query_2Col_label_6Letter" >
			车型：
			</td>
			<td align="left">
				<select id="model" name="model" class="short_sel">
					<option value=''>-请选择-</option>
					<c:forEach var="mode" items="${modelList}">
						<option value="${mode.groupCode}" title="${mode.groupCode}">${mode.groupName}</option>
					</c:forEach>
				</select>
			</td>
          </tr>
    	  <tr>
            <td colspan="6" align="center" nowrap><input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="queryPer();" />
			<input class="normal_btn" type="button" value="新增" name="add" onclick="subFun();"/>
			</td>
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
	location="<%=contextPath%>/MainTainAction/winterMaintenAdd.do";   
}
</script>
</BODY>
</html>
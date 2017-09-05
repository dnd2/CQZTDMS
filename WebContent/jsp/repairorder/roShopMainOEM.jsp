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
<TITLE>预授权审核查询</TITLE>
<SCRIPT LANGUAGE="JavaScript">
	var myPage;
	//查询路径
	var url = "<%=contextPath%>/repairOrder/RoMaintainMain/queryRepairOrderOEM.json";
				
	var title = null;

	var columns = [
					{header: "序号",sortable: false,align:'center',renderer:getIndex},
					{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'id',renderer:myLink1,align:'center'},
					{header: "大区", width:'15%', dataIndex: 'orgname2'},
					{header: "省份", width:'15%', dataIndex: 'orgname'},
					{header: "经销商名称", width:'15%', dataIndex: 'dealerName'},
					{header: "车型", width:'15%', dataIndex: 'modelCode'},
					{header: "预授权申请单号", width:'15%', dataIndex: 'foNo'},
					{header: "申请时间", width:'7%', dataIndex: 'approvalDate',renderer:formatDate2},
					{header: "工单号", width:'15%', dataIndex: 'roNo'},
					{header: "维修类型", width:'7%', dataIndex: 'approvalType',renderer:getItemValue},
					{header: "结算基地", width:'7%', dataIndex: 'balanceYieldly',renderer:getItemValue},
					{header: "VIN", width:'15%', dataIndex: 'vin'},
					{header: "材料总费用", width:'15%', dataIndex: 'repairPartAmount'},
					{header: "工时总费用", width:'15%', dataIndex: 'labourAmount'},
					{header: "辅料总费用", width:'15%', dataIndex: 'accessoriesPrice'},
					{header: "其他总费用", width:'15%', dataIndex: 'addItemAmount'},
					{header: "最终汇总金额", width:'15%', dataIndex: 'repairAmount'},
					{header: "预警", width:'15%', dataIndex: 'isWarning',renderer:getItemValue},
					{header: "授权状态", width:'15%', dataIndex: 'reportStatus',renderer:getItemValue},
					{header:'授权人',width:'10%',dataIndex:'auditName'},
					{header:'审核时间',width:'10%',dataIndex:'updateDat'},
					{header:'下一授权级别',width:'10%',dataIndex:'approvalLevelName'},
					{header:'最终审核级别',width:'10%',dataIndex:'lastName'}
		      ];
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
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
		var createName = record.data.createName;
		return String.format("<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail1.do?createName="+createName+"&type=1&roNo="+record.data.roNo+"&FID="
		+ value + "\">[明细]</a><a href='#' onclick=printClaim('"+record.data.roNo+"','"+value+"'); >[打印]</a>");
	}
	
	function printClaim(ro_no,val)
	{
		 window.open('<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetailinfor.do?type=1&roNo='+ro_no+'&ID='+val,"市场质量问题处理单", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
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


	function wapCle(){
	$('orgId').value='';
	$('orgCode').value='';
	}
	function clearInput(){
		$('dealerId').value='';
		$('dealerCode').value='';
	}
	function materCle(){
		$('materId').value='';
		$('materCode').value='';
	}
	//格式化时间为YYYY-MM-DD
	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
	function queryPer(){
	var star = $('approve_date').value;
	var end = $('approve_date2').value;
	  if(star==""||end ==""){
	  	MyAlert("查询时间必须选择");
	 	 return false;
	  }else if(star>end){
	  	MyAlert("开始时间不能大于结束时间");
	  	return false;
	  }/* else {
	   var s1 = star.replace(/-/g, "/");
		var s2 = end.replace(/-/g, "/");
		var d1 = new Date(s1);
		var d2 = new Date(s2);
		var time= d2.getTime() - d1.getTime();
		var days = parseInt(time / (1000 * 60 * 60 * 24));
		if(days>=93){
		MyAlert("时间跨度不能超过3个月");
	  		return false;
		} */
	 	 __extQuery__(1);
	  }
//设置超链接 end
</script>
</HEAD>
<BODY onload="doInit(),__extQuery__(1)">
<div class="navigation"><img src="../../img/nav.gif" />&nbsp;当前位置：售后服务系统&gt;索赔预授权&gt;预授权审核查询</div>
  <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
          <tr>
            <td  class="table_query_2Col_label_7Letter">工单号：</td>
            <td><input name="RO_NO" id="RO_NO" value="" maxlength="20" type="text"  class="middle_txt" />
            </td>
            <td rowspan="2" class="table_query_2Col_label_7Letter" >VIN：</td>
 			<td rowspan="2"  align="left">
 			<!--  <input name="VIN" id="VIN" datatype="1,is_vin" type="text"  value="" class="middle_txt"/>-->
 			<textarea name="VIN" cols="18" rows="3" datatype="1,is_digit_letter"></textarea>
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
	            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onClick="showOrgDealer('dealerCode','dealerId','true','',true,'','10771002');" value="..." />        
	            <input name="clrBtn" type="button" class="normal_btn" onClick="clearInput();" value="清除"/>  
			</td>
          </tr>
          <tr>
          	<td class="table_query_2Col_label_7Letter">申请时间：</td>
		 <td align="left" nowrap="true">
			<input name="approve_date" type="text" class="short_time_txt" id="approve_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'approve_date', false);" />  	
             &nbsp;至&nbsp; <input name="approve_date2" type="text" class="short_time_txt" id="approve_date2" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'approve_date2', false);" /> 
		</td>	
			<td class="table_query_2Col_label_7Letter">授权人：</td>
			<td>
				<input type="text" class="middle_txt" name="auditName" maxlength="15"/>
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
				<td class="table_edit_2Col_label_7Letter">大区:</td>
				<td>
				 <input type="text" name="org_name" class="middle_txt" />
				</td>
          </tr>
          <tr>
            <td class="table_edit_2Col_label_5Letter">审核时间:</td>
            <td align="left" nowrap="true">
			<input name="audit_date_start" type="text" class="short_time_txt" id="audit_date_start" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'audit_date_start', false);" />  	
             &nbsp;至&nbsp; <input name="audit_date_end" type="text" class="short_time_txt" id="audit_date_end" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'audit_date_end', false);" /> 
		</td>
          </tr>
    	  <tr>
            <td colspan="4" align="center" nowrap>
            <input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="queryPer();" />
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
$('approve_date').value=showMonthFirstDay();
$('approve_date2').value=showMonthLastDay();
</script>
</BODY>
</html>
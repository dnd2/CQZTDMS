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
List<Map<String, Object>> list = (List<Map<String, Object>>)request.getAttribute("winterDetail");
String mod = (String)request.getAttribute("mod");
String sts = (String)request.getAttribute("status");
Long status = 0L;
if (sts != null) {
	status = Long.parseLong(sts);
}
Long constatus = Long.parseLong(Constant.SERVICEACTIVITY_STATUS_02.toString());
String ss = "";
String ss1 = "";
String ss2 = "";
String ss3 = "";
String id = "";
if (list != null) {
for(int i=0;i<list.size();i++) {
	Map<String, Object> map = list.get(i);
	if (ss == "") {
		ss = map.get("DEALER_CODE").toString();
	} else {
		ss = ss + "," + map.get("DEALER_CODE").toString();
	}
	
	ss1 = map.get("AMOUNT").toString();
	ss2 = map.get("START_DATE").toString().substring(0,10);
	ss3 = map.get("END_DATE").toString().substring(0,10);
	id = map.get("ID").toString();
}
}
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>索赔单上报</TITLE>

<SCRIPT LANGUAGE="JavaScript">
var myPage;
var boo = true;
//查询路径
var url = "<%=contextPath%>/claim/application/ClaimBillStatusTrack/winterMaintenDealerQuery.json";
			
var title = null;

var columns = [
				{header: "序号", width:'10%',renderer:getIndex},
				{header: "大区", width:'11%', dataIndex: 'ROOT_ORG_NAME'},
				{header: "省份", width:'11%', dataIndex: 'ORG_NAME'},
				{header: "经销商代码", width:'11%', dataIndex: 'DEALER_CODE'},
				{header: "经销商名称", width:'11%', dataIndex: 'DEALER_NAME'},
				{id:'action', width:'5%',header: "操作",sortable: false,renderer:myLink10,align:'center'}
				
	      ];
	function doInit()
	{
		loadcalendar();
		getDealer($('DEALER_CODE'));
	}
	
	function myLink10(value,meta,record){
		var dealerCode=record.data.DEALER_CODE;
		return String.format('<input class="normal_btn" type="button" name="button" value="删除"  onClick="delDealer('+"\'"+dealerCode+"\'"+');" />');
	}
	
	function delDealer(obj) {
		var sspp = $('dealerCode').value.split(",");
		var dealerCodeInput = "";
		for (var i=0;i<sspp.length;i++) {
			if (sspp[i] != obj) {
				if (dealerCodeInput == "") {
					dealerCodeInput = sspp[i];
				} else {
					dealerCodeInput = dealerCodeInput + "," + sspp[i];
				}
			}
		}
		$('dealerCode').value = dealerCodeInput;
		__extQuery__(1);
		makeNomalFormCall('<%=contextPath%>/claim/application/ClaimBillStatusTrack/winterMaintenDealerQuery.json?DEALER_CODE='+dealerCodeInput,getBackId,'fm','');

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
		var width=900;
		var height=500;
		var str="";
		var screenW = window.screen.width-30;	
		var screenH = document.viewport.getHeight();
	var isImport = record.data.isImport;
	var roNo = record.data.roNo;
		if(screenW!=null && screenW!='undefined')
			width = screenW;
		if(screenH!=null && screenH!='undefined')
			height = screenH;
			if(isImport==<%=Constant.IF_TYPE_YES%>){
			return String.format("<a href=\"#\" onclick=\"showPrintPage1('"+value+"')\">[标签]</a>");
			}else{
  		return String.format("<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?roNo="+roNo+"&ID="
			+ value + "\","+width+","+height+")' >[明细]</a><a href=\"#\" onclick=\"showPrintPage1('"+value+"')\">[标签]</a>");
	}
	}
	function showPrintPage1(claimId){ 
        
        var printUrl = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/barcodePrintDoGet.do?dtlIds="+claimId;
        window.open(printUrl,'','toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=1200'); 

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
	var star = $('RO_STARTDATE').value;
	var end = $('RO_ENDDATE').value;
	  if(star==""||end ==""){
	  	MyAlert("申请时间必须选择");
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
</SCRIPT>

</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;冬季保养维护新增</div>
    <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
          <tr>
            <td class="table_query_2Col_label_6Letter"> 经销商代码：</td>
            <td align="left" >
            <input class="middle_txt" id="dealerCode"  name="DEALER_CODE" onpropertychange="if(this.value != ''){if(boo == true){getDealer(this);}}"  value="<%=ss %>" type="text" readonly="readonly"/>
            <input type="hidden"  id="dealerCodeBack" />
            
            <%if(!status.equals(constatus)) { %>
            <input name="button1" type="button" class="mini_btn" style="cursor: pointer;" onclick="boo = true;showOrgDealer('dealerCode','dealerId','true','','true','','10771002');" value="..." />        
            <input name="button2" type="button" class="normal_btn" onClick="clearInput();" value="清除"/> 
            <%} %>
            </td>
          </tr>
          <tr>
            <td class="table_query_2Col_label_6Letter">冬季保养补助：</td>
		    <td>
		    <%if(!status.equals(constatus)) { %>
		    	<input type="text" class="middle_txt" name="amount" value="<%=ss1 %>" maxlength="25" />
		    <%} else { %>
		    	<input type="text" class="middle_txt" name="amount" value="<%=ss1 %>" readonly="readonly" maxlength="25" />
		    <%} %>
		    </td>
 		    </td> 
 		    <td class="table_query_2Col_label_6Letter" >开始时间：</td>
           <td align="left" nowrap="true">
			<input name="start_date" type="text" class="short_time_txt" id="start_date" value="<%=ss2 %>" readonly="readonly"/> 
			<%if(!status.equals(constatus)) { %>
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'start_date', false);" />
			<%} %>  	
             &nbsp;至&nbsp; <input name="end_date" type="text" class="short_time_txt" id="end_date" value="<%=ss3 %>" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'end_date', false);" /> 
			</td>	
          </tr>
           <tr>
           
            <td colspan="4" align="center" nowrap>
            <%if(mod == "true") { %>
            	<input id="saveBtn" class="normal_btn" type="button" name="button" value="修改"  onClick="modWinter();" />
            <% } else { %>
            	<input id="saveBtn" class="normal_btn" type="button" name="button" value="保存"  onClick="saveWinter();" />
            <%} %>
			<input class="normal_btn" type="button" value="返回" name="back" onclick="history.go(-1);"/>
			<input  type="hidden" name="dealerId" id="dealerId"/>
			</td>
            <td  align="right" ></td>
          </tr>                   
  </table>
    <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>

<script type="text/javascript">
function saveWinter() {
	var dealerId = $('dealerId').value;
	var amount = $('amount').value;
	var startDate = $('start_date').value;
	var endDate = $('end_date').value;
	if (dealerId == '') {
		MyAlert("请选择经销商");
		return;
	}
	if (amount == '') {
		MyAlert("请输入补助金额");
		return;
	}
	
	if (isNaN(amount)) {
		MyAlert("补助金额不合法");
		return;
	}
	
	if (startDate == ''||endDate =='') {
		MyAlert("请选择时间");
		return;
	}
	
	if (startDate > endDate) {
		MyAlert("开始时间不能大于截至时间");
		return;
	}
	$("saveBtn").disabled = "disabled";
	makeNomalFormCall('<%=contextPath%>/claim/application/ClaimBillStatusTrack/winterMaintenDealerAdd.json?dealerId='+dealerId,addBack,'fm','');

}

function modWinter() {
	var dealerId = $('dealerId').value;
	var amount = $('amount').value;
	var startDate = $('start_date').value;
	var endDate = $('end_date').value;
	var id = '<%=id%>';
	if (dealerId == '') {
		MyAlert("请选择经销商");
		return;
	}
	if (amount == '') {
		MyAlert("请输入补助金额");
		return;
	}
	if (isNaN(amount)) {
		MyAlert("补助金额不合法");
		return;
	}
	if (startDate == ''||endDate =='') {
		MyAlert("请选择时间");
		return;
	}
	if (startDate > endDate) {
		MyAlert("开始时间不能大于截至时间");
		return;
	}
	var tt0 = '<%=status%>';
	var tt1 = '<%=Constant.SERVICEACTIVITY_STATUS_02%>';
	if(tt0 == tt1) { 
		var iniEndDate = '<%=ss3%>';
		if (endDate < iniEndDate) {
			MyAlert("截至时间只能大于现截至时间");
			return;
		}
	}
	$("saveBtn").disabled = "disabled";
	makeNomalFormCall('<%=contextPath%>/claim/application/ClaimBillStatusTrack/winterMaintenMod.json?id='+id,modBackk,'fm','');

}

function modBackk(json) {
	if(json.result == "success") {
		$("saveBtn").disabled = "";
		MyAlert("修改成功");
		window.location.href = '<%=contextPath%>/claim/application/ClaimBillStatusTrack/winterMaintenForward.do';
	} else{
		$("saveBtn").disabled = "";
		MyAlert("修改失败！请联系管理员！");
	}
}

function addBack(json) {
	if(json.result == "success") {
		$("saveBtn").disabled = "";
		MyAlert("新增成功");
		window.location.href = '<%=contextPath%>/claim/application/ClaimBillStatusTrack/winterMaintenForward.do';
	} else{
		$("saveBtn").disabled = "";
		MyAlert("新增失败！请联系管理员！");
	}
}
function getDealer(obj) {
	if (obj.value == '') {
		return false;
	} else {
		boo = false;
		$("saveBtn").disabled = "disabled";
		var dealerCodeInput = "";
		if ($('dealerCodeBack').value == "") {
			dealerCodeInput = obj.value;
		} else {
			var ssp = (obj.value).split(",");
			dealerCodeInput = $('dealerCodeBack').value;
			for (var i=0;i<ssp.length;i++) {
				if (dealerCodeInput.indexOf(ssp[i]) < 0) {
					dealerCodeInput = dealerCodeInput + "," + ssp[i];
				}
			}
		}
		$('dealerCode').value = dealerCodeInput;
		__extQuery__(1);
		makeNomalFormCall('<%=contextPath%>/claim/application/ClaimBillStatusTrack/winterMaintenDealerQuery.json?DEALER_CODE='+dealerCodeInput,getBackId,'fm','');

	}
}

function getBackId(json) {
	$('dealerId').value=json.dealerId;
	$("saveBtn").disabled = "";
	if ($('dealerCodeBack').value == "") {
		$('dealerCodeBack').value=json.dealerCodeBack;
	} else {
		$('dealerCodeBack').value=json.dealerCodeBack;
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
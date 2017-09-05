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
List<Map<String, Object>> list1 = (List<Map<String, Object>>)request.getAttribute("list1");
String mod = (String)request.getAttribute("mod");
String sts = (String)request.getAttribute("status");
Long status = 0L;
if (sts != null) {
	status = Long.parseLong(sts);
}
Long constatus = Long.parseLong(Constant.SERVICEACTIVITY_STATUS_02.toString());
System.out.println(constatus);
String ss = "";
String ss1 = "";
String ss2 = "";
String ss3 = "";
String ss4 = "";
String ss5 = "";
String id = "";
if (list != null) {
for(int i=0;i<list.size();i++) {
	Map<String, Object> map = list.get(i);
	if (ss == "") {
		ss = map.get("DEALER_CODE").toString();
	} else {
		ss = ss + "," + map.get("DEALER_CODE").toString();
	}
	System.out.println(ss);
	ss1 = map.get("AMOUNT").toString();
	ss2 = map.get("START_DATE").toString().substring(0,10);
	ss3 = map.get("END_DATE").toString().substring(0,10);
	if(map.get("GROUP_NAME")!=null ){
	   ss4 = map.get("GROUP_NAME").toString();
	}
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
var url = "<%=contextPath%>/MainTainAction/winterMaintenDealerQuery.json";
			
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
		makeNomalFormCall('<%=contextPath%>/MainTainAction/winterMaintenDealerQuery.json?DEALER_CODE='+dealerCodeInput,getBackId,'fm','');

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

	function chooseConfiguration(){
		var model_code =	document.getElementById('model_code').value;
		if(""==model_code){
			MyAlert("请先选择车型");
          return;
		}else{
		  OpenHtmlWindow('<%=contextPath%>/MainTainAction/queryConfiguration.do?model_code='+model_code,800,500);
		}
	}
	//调用父页面的配置
	function setConfiguration(code,name){
	   document.getElementById('Configuration').value = name;
	   document.getElementById('Configuration_code').value = code;
	}
	function queryConfigurationById(val){
		 OpenHtmlWindow('<%=contextPath%>/MainTainAction/queryConfigurationById.do?id='+val,800,500);
	 }
//设置超链接 end
</SCRIPT>

</HEAD>
<body onload="__extQuery__(1);">
 <input type="hidden" value="${winterDetail }" name="winterDetail" id="winterDetail" />
<div class="navigation"><img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;冬季保养维护新增</div>
    <form method="post" name ="fm" id="fm">
 <input type="hidden" value="${packagecode }" name="Configuration_code" id="Configuration_code" />
    <TABLE  class="table_query">
          <tr>
            <td class="table_query_2Col_label_6Letter"> 经销商代码：</td>
            <td align="left" >
            <input class="middle_txt" id="dealerCode"  name="DEALER_CODE" onpropertychange="if(this.value != ''){if(boo == true){getDealer(this);}}"  value="${dealerCode}" type="text" readonly="readonly"/>
            <input type="hidden"  id="dealerCodeBack" value="" />
            <c:forEach items="${winterDetail}" var="st">
    	       <c:set var="status" value="${st.STATUS}"></c:set>
    	       <c:set var="GROUP_NAME" value="${st.GROUP_NAME}"></c:set>
    	       <c:set var="winter_id" value="${st.ID}"></c:set>
    	    </c:forEach>
            <c:if test="${type!='update'|| status==10681001 }">
            <input name="button1" type="button" class="mini_btn" style="cursor: pointer;" onclick="boo = true;showOrgDealer('dealerCode','dealerId','true','','true','','10771002');" value="..." />        
            <input name="button2" type="button" class="normal_btn" onClick="clearInput();" value="清除"/> 
            </c:if>
            </td>
            	<td class="table_query_2Col_label_6Letter" >
			车型：
			</td>
			<td align="left">
			<c:if test="${type!='update'|| status==10681001 }">
				<select id="model_code" name="model_code" class="short_sel">
					<option value=''>-请选择-</option>
					<c:forEach var="mode" items="${modelList}">
					  <c:if test="${GROUP_NAME==mode.groupName}">
						<option value="${mode.groupCode}" title="${mode.groupCode}" selected="selected" >${mode.groupName}</option>
					  </c:if>
					  <c:if test="${GROUP_NAME!=mode.groupName}">
						<option value="${mode.groupCode}" title="${mode.groupCode}" >${mode.groupName}</option>
					  </c:if>
					</c:forEach>
				</select>
			</c:if>
			<c:if test="${type=='update' && status==10681002 }">
			   <input type="text" id="model_code" name="model_code" value="<%=ss4 %>" readonly="readonly"/>
			</c:if>
			</td>
			<td>
			  配置：
			</td>
			<td>
			 <c:if test="${type=='update' && status==10681001 }">
			   <input type="text" id="Configuration" onclick="chooseConfiguration();" name="Configuration" value="${packagename }" style="color: red;" readonly="readonly" />
			   <input type="button" id="Configuration" onclick="queryConfigurationById(${winter_id});"    name="Configuration" value="点击查看历史选择"  readonly="readonly" />
			 </c:if>
			  <c:if test="${type=='update' && status==10681002 }">
			   <input type="button" id="Configuration" onclick="queryConfigurationById(${winter_id});"    name="Configuration" value="点击查看" style="color: red;" readonly="readonly" />
			  </c:if>
			   <c:if test="${type!='update' }">
			   <input type="text" id="Configuration" onclick="chooseConfiguration();"   name="Configuration" value="请先选择车型" style="color: red;" readonly="readonly" />
			  </c:if>
			 
			</td>
          </tr>
          <tr>
            <td class="table_query_2Col_label_6Letter">冬季保养补助：</td>
		    <td>
		    <c:if test="${type=='update' && status==10681001}">
		    	<input type="text" class="middle_txt" name="amount"  id="amount" value="<%=ss1 %>" maxlength="25" />
			</c:if> 
			 <c:if test="${type=='update' && status==10681002}">
		    	<input type="text" class="middle_txt" name="amount" readonly="readonly" id="amount" value="<%=ss1 %>" maxlength="25" />
			</c:if>
			<c:if test="${type!='update'}">
		    	<input type="text" class="middle_txt" name="amount"  id="amount" value="<%=ss1 %>" maxlength="25" />
			</c:if>
		    </td>
 		    <td class="table_query_2Col_label_6Letter" >开始时间：</td>
           <td align="left" nowrap="true">
			<input name="start_date" type="text" class="short_time_txt" id="start_date" value="<%=ss2 %>" readonly="readonly"/> 
			<c:if test="${type=='update' && status==10681001}">
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'start_date', false);" />
			</c:if> 
			<c:if test="${type!='update' }">
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'start_date', false);" />
			</c:if>  	
             &nbsp;至&nbsp; <input name="end_date" type="text" class="short_time_txt" id="end_date" value="<%=ss3 %>" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'end_date', false);" /> 
			</td>	
			<td></td>
			<td></td>
          </tr>
           <tr>
           
            <td colspan="6" align="center" nowrap>
            <c:if test="${type=='update'}">
            	<input id="Btn" class="normal_btn" type="button" name="button" value="修改"  onClick="modWinter();" />
            </c:if>
            <c:if test="${type!='update'}">
            	<input id="saveBtn" class="normal_btn" type="button" name="button" value="保存"  onClick="saveWinter(10681001);" />
            	<input id="saveBtn" class="normal_btn" type="button" name="button" value="发布"  onClick="saveWinter(10681002);" />
           </c:if>
			<input class="normal_btn" type="button" value="返回" name="back" onclick="history.go(-1);"/>
			<input  type="hidden" name="dealerId" id="dealerId" value=""/>
			</td>
            <td  align="right" ></td>
			<td></td>
			<td></td>
          </tr>                   
  </table>
    <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>

<script type="text/javascript">
function saveWinter(val) {
	var dealerId = $("dealerId").value;
	var amount = $('amount').value;
	var startDate = $('start_date').value;
	var endDate = $('end_date').value;
	var model_code = $('model_code').value;
	var Configuration = $('Configuration').value;
	
	if (dealerId == '') {
		MyAlert("请选择经销商");
		return;
	}
	if (model_code == '') {
		MyAlert("请选择车型");
		return;
	}
	if (Configuration == '请先选择车型') {
		MyAlert("请选择配置");
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
	makeNomalFormCall('<%=contextPath%>/MainTainAction/winterMaintenDealerAdd.json?dealerId='+dealerId+'&status='+val,addBack,'fm','');

}

function modWinter() {
	var amount = $('amount').value;
	var startDate = $('start_date').value;
	var endDate = $('end_date').value;
	var id = '<%=id%>';
	var model_code = $('model_code').value;
	if (model_code == '') {
		MyAlert("请选择车型");
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
    var url='<%=contextPath%>/MainTainAction/winterMaintenUpdate.json?id='+id;
	sendAjax(url,function show(json){
		if(json.result == "1") {
			MyAlert("修改成功");
			window.location.href = '<%=contextPath%>/MainTainAction/WinterMaintenance.do';
		} else{
			MyAlert(json.result);
		}
	 },"fm");
}

function addBack(json) {
	if(json.result == "success") {
		$("saveBtn").disabled = "";
		MyAlert("新增成功");
		window.location.href = '<%=contextPath%>/MainTainAction/WinterMaintenance.do';
	} else{
		$("saveBtn").disabled = "";
		MyAlert(json.result);
	}
}
function getDealer(obj) {
	if (obj.value == '') {
		return false;
	} else {
		boo = false;
		//$("saveBtn").disabled = "disabled";
		var dealerCodeInput = "";
		if ($('dealerCodeBack').value == "") {
			dealerCodeInput = obj.value;
		} else {
			var ssp = (obj.value).split(",");
			dealerCodeInput = $('dealerCodeBack').value;
			for (var i=0;i<ssp.length;i++) {
				if("undefined"==dealerCodeInput){
					dealerCodeInput="";
				}
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
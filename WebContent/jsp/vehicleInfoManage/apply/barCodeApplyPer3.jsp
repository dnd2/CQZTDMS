<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
   		__extQuery__(1);
	}

</script>
</head>
<body onload="notice();">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;三包凭证补办发运
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
<input type="hidden" id="type" name="type" value=""/>
	<tr>
		<td class="table_query_2Col_label_5Letter">VIN：</td>
      	<td><input name="vin" type="text" id="vin"  class="middle_txt"/></td>
         <td class="table_query_2Col_label_6Letter">申请状态：</td>
      <td>
      	<script type="text/javascript">
        	genSelBoxExp("status",<%=Constant.BARCODE_APPLY_STATUS%>,"",true,"short_sel","","false",'<%=Constant.BARCODE_APPLY_STATUS_01%>');
        </script>
      </td>
    </tr>
  <tr>            
        <td class="table_query_2Col_label_5Letter">经销商代码：</td>            
        <td>
		<textarea rows="2" cols="30" readonly="readonly" id="dealerCode" name="dealerCode"></textarea>
		<input name="dealerId" id="dealerId" readonly="readonly" type="hidden">
		     <input name="button1" type="button" class="mini_btn" style="cursor: pointer;"  onclick="showOrgDealer('dealerCode','dealerId','true','','true','','10771002');" value="..." />        
		     <input name="button2" type="button" class="normal_btn" onclick="clr();" value="清除"/>
        </td>   
        <td class="table_query_2Col_label_5Letter">经销商名称：</td>
        <td><input type="text" name="DEALER_NAME" id="DEALER_NAME" datatype="1,is_digit_letter_cn,30" class="middle_txt" value=""/></td>     
       </tr> 
        <tr>            
        <td class="table_query_2Col_label_5Letter">打印时间：</td>            
		<td  nowrap="true">
			<input name="S_DATE" type="text" class="short_time_txt" id="S_DATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'S_DATE', false);" />  	
             &nbsp;至&nbsp; <input name="E_DATE" type="text" class="short_time_txt" id="E_DATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'E_DATE', false);" /> 
		</td>
		<td class="table_query_2Col_label_5Letter">签收时间：</td>            
		<td   nowrap="true">
			<input name="SIGN_S_DATE" type="text" class="short_time_txt" id="S_DATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'SIGN_S_DATE', false);" />  	
             &nbsp;至&nbsp; <input name="SIGN_E_DATE" type="text" class="short_time_txt" id="E_DATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'SIGN_E_DATE', false);" /> 
		</td>	
       </tr> 
       <tr>
		<td class="table_query_2Col_label_5Letter">签收人：</td>
      	<td><input name="SIGN_NAME" type="text" id="SIGN_NAME"  class="middle_txt"/></td>
         <td class="table_query_2Col_label_6Letter">签收状态：</td>
      	<td>
      	<select name="SIGN_STATUS" id="SIGN_STATUS" class="table_query_2Col_label_6Letter">
      		<option value="">-请选择-</option>
      		<option value="0">未签收</option>
      		<option value="1">已签收</option>
      	</select>
      </td>
    	</tr>
    	<tr>
    	<td class="table_query_2Col_label_5Letter">车型：</td>
      	<td><input name=GROUP_NAME type="text" id="GROUP_NAME"  class="middle_txt"/></td>
    	<td class="table_query_2Col_label_5Letter">编码章：</td>
      	<td><input name=CHAPTER_CODE type="text" id="CHAPTER_CODE"  class="middle_txt"/></td>
    	</tr>    
    <tr>
    	<td align="center" colspan="5">
    		<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1);" >
<!--     		<input type="button"  name="BtnAdd" id="queryBtn"  value="批量打印"  class="normal_btn" onClick="viewprint2()" >
 -->    		<input type="button"  name="BtnAdd" id="queryBtn"  value="导出"  class="normal_btn" onClick="exportExcel();" >
    	</td>
    </tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/barCodeApplyQuery2.json";
	var title = null;
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "申请时间", dataIndex: 'APP_DATE', align:'center'},
				{header: "申请状态", dataIndex: 'APPLY_STATUS', align:'center', renderer:getItemValue},
				{header: "审核人", dataIndex: 'AUDIT_NAME', align:'center'},
				{header: "审核时间", dataIndex: 'AUDIT_TIME', align:'center'},
				{header: "签收人", dataIndex: 'SIGN_NAME', align:'center'},
				{header: "签收时间", dataIndex: 'SIGN_DATE', align:'center'},
				{header: "是否签收", dataIndex: 'SIGN_STATUS', align:'center',renderer:getSignStatus},
				{id:'action', header: "操作", sortable: false, dataIndex: 'ID', renderer:oper, align:'center'}
		      ];
	//
	function addRecord() {
		fm.action = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/barCodeApplyAdd.do?type=0';
		 fm.method="post";
		fm.submit();
	}
	function exportExcel(){
		document.getElementById("type").value=1;
		fm.action = "<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/barCodeApplyQuery2.do";
		fm.submit();
	}
	
	function getSignStatus(value,meta,record) {
		
		if (record.data.SIGN_STATUS == 0) {
			return String.format("未签收");
		} else {
			return String.format("已签收");
		}
	}
	
function checkBoxShow(value,meta,record){
	return String.format("<input type='hidden' id='aa" + record.data.ID + "'  name='aa' value='" + record.data.APPLY_STATUS + "' /><input type='checkbox' id='recesel' onclick='checkDate(this,this.value)'  name='recesel' value='" + record.data.ID + "' />");
}
function checkDate(obj,value){
	var aa = document.getElementById('aa'+value).value;
	if(obj.checked&&aa!=95451004){
		MyAlert("该单还未审核通过,不能进行批量打印!");
		obj.checked = false;
		return false;
	}
}
	//点击索赔申请
	function oper(value,meta,record) {
		if (record.data.APPLY_STATUS == 95451004 && (record.data.EXPRESS_MAIL==null || record.data.EXPRESS_MAIL=="")) {
			return String.format("<a href=\"#\" onclick='viewDetail(\""+record.data.ID+"\")'>[明细]</a>"+"<a href=\"#\" onclick='viewAudit(\""+record.data.ID+"\")'>[发运]</a>");
		}else {
			return String.format("<a href=\"#\" onclick='viewDetail(\""+record.data.ID+"\")'>[明细]</a>");
		}
	}
	//详细信息修改页面
	function viewAudit(id) {
		fm.method = 'post';
		fm.action = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/barCodeApplyAdd.do?type=4&id='+id;
		fm.submit();
	}	
	//详细信息查看页面
	function viewDetail(id) {
		fm.method = 'post';
		fm.action = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/barCodeApplyAdd.do?type=2&id='+id;
		fm.submit();
	}
	//
	function viewprint(id) {
	if(confirm("确认打印?")){
		window.open('<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/barCodeApplyPrint2.do?ids='+id,"条码打印", "height=800, width=500, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	}else 
	{
	return false;
	}
	}
function viewprint2() {
var allChecks = document.getElementsByName('recesel');
	var ids="";
	var count=0;
		for(var i = 0;i<allChecks.length;i++){
		if(allChecks[i].checked){
			ids = allChecks[i].value+","+ids;
			count++;
		}
	}
	if(count==0){
	MyAlert("请选择要打印的数据!");
	return false;
	}
	if(ids!=""){
	ids=ids.substring(0,ids.length-1);
	}
	if(confirm("确认批量打印?")){
		window.open('<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/barCodeApplyPrint2.do?ids='+ids,"条码打印", "height=800, width=500, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	}
	}
function showResult(json){
	if(json.result!=null&&json.result!=""){
		MyAlert("上报成功！");
		__extQuery__(1)
	}else{
		MyAlert("上报失败,请联系管理员");
	}
}
//清除方法
 function clr() {
	document.getElementById('DEALER_NAME').value = '';
	document.getElementById('dealerCode').value = '';
	$('dealerId').value="";
  }
</script>
<!--页面列表 end -->
</body>
</html>
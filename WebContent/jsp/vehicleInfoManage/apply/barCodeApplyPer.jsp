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
	}

</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;车辆条码补办申请查询
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td class="table_query_2Col_label_5Letter">VIN：</td>
      	<td><input name="vin" type="text" id="vin" class="middle_txt"/></td>
         <td class="table_query_2Col_label_6Letter">申请状态：</td>
      <td>
      	<script type="text/javascript">
        	genSelBoxExp("status",<%=Constant.BARCODE_APPLY_STATUS%>,"",true,"short_sel","","false",'');
        </script>
      </td>
    </tr>
  
    <tr>
    	<td align="center" colspan="5">
    		<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1);" >
    		<input type="button" name="BtnAdd" id="queryBtn"  value="新增"  class="normal_btn" onClick="addRecord()" >
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
	var url = "<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/barCodeApplyQuery.json";
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
				{id:'action', header: "操作", sortable: false, dataIndex: 'ID', renderer:oper, align:'center'}
		      ];
		__extQuery__(1)
	//
	function addRecord() {
		fm.action = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/barCodeApplyAdd.do?type=0';
		 fm.method="post";
		fm.submit();
	}
	//点击索赔申请
	function oper(value,meta,record) {
		if (record.data.APPLY_STATUS == 95451001||record.data.APPLY_STATUS == 95451003) {
			return String.format("<a href=\"#\" onclick='viewModDetail(\""+record.data.ID+"\")'>[修改]</a>"+"<a href=\"#\" onclick='viewDetail(\""+record.data.ID+"\")'>[明细]</a>"+"<a href=\"#\" onclick='viewReport(\""+record.data.ID+"\")'>[上报]</a>");
		} else {
			if(record.data.SIGN_STATUS == 0 && record.data.APPLY_STATUS == 95451004) {
				return String.format("<a href=\"#\" onclick='viewDetail(\""+record.data.ID+"\")'>[明细]</a>"+"<a href=\"#\" onclick='signIt(\""+record.data.ID+"\")'>[签收]</a>");
			} else {
				return String.format("<a href=\"#\" onclick='viewDetail(\""+record.data.ID+"\")'>[明细]</a>");
			}
			
		}
	}
	
	function signIt(id) {
		if (confirm("是否签收?")){
			var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/signIt.json?id='+id;
			makeNomalFormCall(url,showResult2,'fm');
			}
	}
	//详细信息修改页面
	function viewModDetail(id) {
		fm.method = 'post';
		fm.action = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/barCodeApplyAdd.do?type=1&id='+id;
		fm.submit();
	}	
	//详细信息查看页面
	function viewDetail(id) {
		fm.method = 'post';
		fm.action = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/barCodeApplyAdd.do?type=2&id='+id;
		fm.submit();
	}
	//上报
	function viewReport(id) {
	if (confirm("是否上报?")){
		var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/barCodeApplySave.json?type=3&id='+id;
		makeNomalFormCall(url,showResult,'fm');
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

function showResult2(json){
	if(json.result!=null&&json.result!=""){
		MyAlert("签收成功！");
		__extQuery__(1);
	}else{
		MyAlert("签收失败,请联系管理员");
	}
}
</script>
<!--页面列表 end -->
</body>
</html>
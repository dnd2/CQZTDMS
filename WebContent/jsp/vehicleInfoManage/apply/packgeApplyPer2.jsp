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
<body onload="notice();">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;三包凭证补办申请大区审核查询
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
<input name="type" id ="type" type="hidden" value="${type }"/>
	<tr>
		<td class="table_query_2Col_label_5Letter">VIN：</td>
      	<td><input name="vin" type="text" id="vin"  class="middle_txt"/></td>
         <td class="table_query_2Col_label_6Letter">申请状态：</td>
      <td>
      	<script type="text/javascript">
        	genSelBoxExp("status",<%=Constant.PACKGE_CHANGE_STATUS%>,"",true,"short_sel","","false",'<%=Constant.PACKGE_CHANGE_STATUS_01%>,<%=Constant.PACKGE_CHANGE_STATUS_05%>,<%=Constant.PACKGE_CHANGE_STATUS_06%>');
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
    	<td align="center" colspan="5">
    		<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1);" >
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
	var url = "<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/packgeApplyQuery2.json";
	var title = null;
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "申请时间", dataIndex: 'APP_DATE', align:'center'},
				{header: "审核费用", dataIndex: 'AUDIT_ACOUNT', align:'center'},
				{header: "申请状态", dataIndex: 'APPLY_STATUS', align:'center', renderer:getItemValue},
				{id:'action', header: "操作", sortable: false, dataIndex: 'ID', renderer:oper, align:'center'}
		      ];
		__extQuery__(1);

	//点击索赔申请
	function oper(value,meta,record) {
		 if (record.data.APPLY_STATUS == 95441002) {
			return String.format("<a href=\"#\" onclick='viewDetail(\""+record.data.ID+"\")'>[明细]</a>"+"<a href=\"#\" onclick='viewAudit(\""+record.data.ID+"\")'>[审核]</a>");
		}else {
			return String.format("<a href=\"#\" onclick='viewDetail(\""+record.data.ID+"\")'>[明细]</a>");
		}
	}
	//详细信息修改页面
	function viewAudit(id) {
	var type=$("type").value;
		fm.method = 'post';
		fm.action = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/packgeAppAudit.do?id='+id+"&type="+type;
		fm.submit();
	}	
	//详细信息查看页面
	function viewDetail(id) {
		fm.method = 'post';
		fm.action = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/packgeApplyDetail.do?id='+id;
		fm.submit();
	}
	//上报
	function viewprint(id) {
		window.open('<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/barCodeApplyPrint.do?id='+id,"条码打印", "height=800, width=500, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	__extQuery__(1);
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
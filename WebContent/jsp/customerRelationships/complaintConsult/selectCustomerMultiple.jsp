<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>客户查询</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body onload="__extQuery__(1)">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户查询</div>
	<form method="post" name = "fm" id="fm">	
	<input type="hidden" name="ctmname" id="ctmname" />
	<input type="hidden" name="tele" id="tele" />
	<input type="hidden" name="ctmId" id="ctmId" />
	<input type="hidden" name="vinStr" id="vinStr" />
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />客户查询</th>
			
			<tr>
				<td align="right" nowrap="true">客户名称：</td>
				<td align="left" nowrap="true">
					<input type="text" id="name" name="name"/>
				</td>
				<td align="right" nowrap="true">联系电话：</td>
				<td align="left" nowrap="true">
					<input type="text" id="telephone" name="telephone"/>
				</td>
				<td align="right" nowrap="true">VIN：</td>
				<td align="left" nowrap="true">
					<input type="text" id="vinNo" name="vinNo"/>
				</td>
			</tr>
	
			<tr>
				<td colspan="6" align="center">
					<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
        		</td>
			</tr>
		</table>
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	</form>
	<div style="margin-top:40px;margin-left:46%;">
		<input class="normal_btn" type="button" value="确定" name="btnOk" id="btnOk" onclick="confirm();" />
		<input class="normal_btn" type="button" value="关闭" name="btnClose" id="btnClose" onclick="window.close();" />
	</div>
<script type="text/javascript">
	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/clientManage/IncomingAlertScreen/queryIncomingAlertScreen.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "<input type='checkbox' name='checks' id='checks' onclick='choise();' />选择", width:'8%',sortable: false,dataIndex: 'CTMID',renderer:myCheckBox},
				{header: "客户名称",dataIndex: 'CTMNAME',align:'center'},
				{header: "手机号", dataIndex: 'PHONE', align:'center'},
				{header: "VIN号",dataIndex: 'VIN',align:'center'},
				{header: "发动机号",dataIndex: 'ENGINE_NO',align:'center'},
				{header: "车型", dataIndex: 'MODELNAME', align:'center'},
				{header: "省份",dataIndex: 'PROVINCE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALERNAME',align:'center'}			
		      ];
	
	function myCheckBox(value,metaDate,record){
		return String.format("<input name='checkbox' type='checkbox' value='"+ record.data.CTMID +","+ record.data.ORDERID +","+record.data.VIN+"'  onclick='changeCheck(this,"+ record.data.CTMID +","+ record.data.ORDERID +",\""+ record.data.VIN +"\")'/>");
	}
	
	function doRowClick(obj){
    	if(obj.cells[0].firstChild.checked != true){
    		obj.cells[0].firstChild.checked = true;
    		var str = obj.cells[0].firstChild.value;
    		
    		changeCheck(obj.cells[0].firstChild,str.split(',')[0],str.split(',')[1]);
    	}
    }
	
	function changeCheck(checkBox,ctmid,orderid,vin){
		if(checkBox.checked){
			makeNomalFormCall('<%=contextPath%>/customerRelationships/clientManage/IncomingAlertScreen/queryCustomerInfor.json?ctmid='+ctmid+'&orderid='+orderid,changeData,'fm','');			
		} else {
			var vinStrs = $("vinStr").value.split(",");
			for(var i = 0;i < vinStrs.length;i++){
				if(vinStrs[i] == vin){
					changeValue("ctmId",i);
					changeValue("ctmname",i);
					changeValue("tele",i);
					changeValue("vinStr",i);
					break;
				}
			}
		}
	}
	
	function changeValue(id,k){
		var val = $(id).value.split(",");
		var strs = "";
		for(var i = 0;i < val.length;i++){
			if(i != k){
				if(i == val.length - 1){
					strs += val[i];
				} else {
					strs += val[i]+",";
				}
			}
		}
		if(strs.lastIndexOf(",") == strs.length - 1){
			strs = strs.substring(0, strs.length - 1);
		}
		$(id).value = strs;
	}
	
	//返回的数据 更新页面数据
	function changeData(json) {
// 		opener.location="javascript:changeData('"+json.queryCustomerInforData.CTMNAME+"','"+json.queryCustomerInforData.PHONE+"','" +
// 			json.queryCustomerInforData.VIN+"','"+json.queryCustomerInforData.SALESADDRESS+"','"+json.queryCustomerInforData.SERIESID+"','" +
// 			json.queryCustomerInforData.MODELID+"','"+json.queryCustomerInforData.PUDATE+"','"+json.queryCustomerInforData.PDATE+"','"  +
// 			json.queryCustomerInforData.MILEAGE+"','"+json.queryCustomerInforData.MILEAGERANGE+"','"  +
// 			json.queryCustomerInforData.PROVINCE+"','"+json.queryCustomerInforData.CITY+"','"+json.queryCustomerInforData.CTMID+"','"+json.queryCustomerInforData.ENGINENO+"')";
		
		var ctmname = $("ctmname").value;
		var name = json.queryCustomerInforData.CTMNAME;
		if(name == null|| name == "" || name == "null"){
			name = "未知";
		}else{
			name = json.queryCustomerInforData.CTMNAME;
		}
		if(ctmname != null && ctmname != "" && ctmname != "null") {
			ctmname = ctmname+","+name;
		} else {
			ctmname = name;
		}
		var tele = $("tele").value;
		var phone = json.queryCustomerInforData.PHONE;
		if(phone == null || phone == "" || phone == "null"){
			phone = "00";
		}else{
			phone = json.queryCustomerInforData.PHONE;
		}
		if(tele != null && tele != "" && tele != "null") {
			tele = tele+","+phone;
		} else {
			tele = phone;
		}
		var ctmId = $("ctmId").value;
		if(ctmId != null && ctmId != "" && ctmId != "null") {
			ctmId = ctmId+","+json.queryCustomerInforData.CTMID;
		} else {
			ctmId = json.queryCustomerInforData.CTMID;
		}
		var vinStr = $("vinStr").value;
		if(vinStr != null && vinStr != "" && vinStr != "null") {
			vinStr = vinStr+","+json.queryCustomerInforData.VIN;
		} else {
			vinStr = json.queryCustomerInforData.VIN;
		}
		$("ctmname").value = ctmname;
		$("tele").value = tele;
		$("ctmId").value = ctmId;
		$("vinStr").value = vinStr;
	}
	
	function confirm(){
		var ctmnames = $("ctmname").value;
		var teles = $("tele").value;
		var ctmIds = $("ctmId").value;
		var vinStrs = $("vinStr").value;
		if(ctmIds == null || ctmIds == "" || ctmIds == "null"){
			MyAlert("请选择客户!");
			return;
		}
		
		opener.location="javascript:changeData('"+ctmnames+"','"+teles+"','" +ctmIds+"','"+vinStrs+"')";
		window.close();
	}
	
	function choise(){
		var checks = document.getElementById("checks");
		var rvIds = document.getElementsByName("checkbox");
		if(rvIds != null && rvIds.length > 0){
			if(checks.checked){
				for(var i = 0;i < rvIds.length;i++){
					rvIds[i].checked = true;
					var val = rvIds[i].value.split(",");
					var ctmid = val[0];
					var orderid = val[1];
					makeNomalFormCall('<%=contextPath%>/customerRelationships/clientManage/IncomingAlertScreen/queryCustomerInfor.json?ctmid='+ctmid+'&orderid='+orderid,changeData,'fm','');
				}
			}else{
				for(var i = 0;i < rvIds.length;i++){
					rvIds[i].checked = false;
					var val = rvIds[i].value.split(",");
					var vin = val[2];
					
					var vinStrs = $("vinStr").value.split(",");
					for(var j = 0;j < vinStrs.length;j++){
						if(vinStrs[j] == vin){
							changeValue("ctmId",j);
							changeValue("ctmname",j);
							changeValue("tele",j);
							changeValue("vinStr",j);
							break;
						}
					}
				}
			}
		}
	}
</script>
</body>
</html>
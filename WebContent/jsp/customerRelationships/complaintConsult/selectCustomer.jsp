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
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="8"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />客户查询</th>
			
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
				<td align="right" nowrap="true">发动机号：</td>
				<td align="left" nowrap="true">
					<input type="text" id="engineNo" name="engineNo"/>
				</td>
			</tr>
	
			<tr>
				<td colspan="8" align="center">
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
<script type="text/javascript">
	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/clientManage/IncomingAlertScreen/queryIncomingAlertScreen.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择", width:'8%',sortable: false,dataIndex: 'CTMID',renderer:myCheckBox},
				{header: "客户名称",dataIndex: 'CTMNAME',align:'center'},
				{header: "手机号", dataIndex: 'PHONE', align:'center'},
				{header: "VIN号",dataIndex: 'VIN',align:'center'},
				{header: "发动机号",dataIndex: 'ENGINE_NO',align:'center'},
				{header: "车型", dataIndex: 'MODELNAME', align:'center'},
				{header: "省份",dataIndex: 'PROVINCE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALERNAME',align:'center'}			
		      ];
	
	function myCheckBox(value,metaDate,record){
		return String.format("<input name='radio' type='radio' value='"+ record.data.CTMID +","+ record.data.ORDERID +"'  onclick='changeCheck(this,"+ record.data.CTMID +","+ record.data.ORDERID +")'/>");
	}
	
	function doRowClick(obj){
    	if(obj.cells[0].firstChild.checked != true){
    		obj.cells[0].firstChild.checked = true;
    		var str = obj.cells[0].firstChild.value;
    		
    		changeCheck(obj.cells[0].firstChild,str.split(',')[0],str.split(',')[1]);
    	}
    }
	
	function changeCheck(checkBox,ctmid,orderid){
		if(checkBox.checked){
			makeNomalFormCall('<%=contextPath%>/customerRelationships/clientManage/IncomingAlertScreen/queryCustomerInfor.json?ctmid='+ctmid+'&orderid='+orderid,changeData,'fm','');			
		}
	}
	//返回的数据 更新页面数据
	function changeData(json) {
		opener.location="javascript:changeData('"+json.queryCustomerInforData.CTMNAME+"','"+json.queryCustomerInforData.PHONE+"','" +
			json.queryCustomerInforData.VIN+"','"+json.queryCustomerInforData.SALESADDRESS+"','"+json.queryCustomerInforData.SERIESID+"','" +
			json.queryCustomerInforData.MODELID+"','"+json.queryCustomerInforData.PUDATE+"','"+json.queryCustomerInforData.PDATE+"','"  +
			json.queryCustomerInforData.MILEAGE+"','"+json.queryCustomerInforData.MILEAGERANGE+"','"  +
			json.queryCustomerInforData.PROVINCE+"','"+json.queryCustomerInforData.CITY+"','"+json.queryCustomerInforData.CTMID+"','"+json.queryCustomerInforData.ENGINENO+"')";
		window.close();
	}
	
</script>
</body>
</html>
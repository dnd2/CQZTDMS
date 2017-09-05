<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<% String contextPath = request.getContextPath(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>库存查询</title>
<script type="text/javascript">
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	
	var myPage;
	var title = null;
	var	url = "<%=contextPath%>/sales/storageManage/ReturnVehicleReq/returnCheckQueryStorage.json";
	var	columns = [
				// {id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"headIds\")' />", width:'8%',sortable: false,dataIndex: 'HEAD_ID',renderer:myCheckBox},
				{header: "退车号", dataIndex: 'RETURN_VEHICLE_NO', align:'center'},
				//{header: "资金类型", dataIndex: 'ACCOUNT_TYPE_NAME', align:'center'},
				{header: "经销商简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "申请日期", dataIndex: 'APPLAY_CREATE', align:'center'},
				{header: "申请数量", dataIndex: 'APPLY_AMOUNT', align:'center'},
				{header: "已审核数量", dataIndex: 'COU', align:'center'},
				{header: "退车原因", dataIndex: 'REASON', align:'center'},
				{header: "操作", dataIndex: 'HEAD_ID', align:'center',renderer:myOpera}
		      ];
		      
	//全选checkbox
	function myCheckBox(value,metaDate,record)
	{
		return String.format("<input type='checkbox' name='headIds' value='" + value + "'/>");
	}
	
	function myOpera(value,metaDate,record) {
		var isWareHousing =  record.data.IS_WARE_HOUSING;
		if (isWareHousing=='10011001') {
			return String.format("<a href=\"#\" onclick=\"operaItInWare(" + value + ");\">[入库]</a>") ;
		} else {
			return String.format("<a href=\"#\" onclick=\"operaIt(" + value + ");\">[明细审核]</a>") ;
		}
	}
	
	//明细审核操作
	function operaIt(value) {
		url = "<%=contextPath%>/sales/storageManage/ReturnVehicleReq/returnDtlCheckInitStorage.do?headId=" + value ;
		var fsm = document.getElementById('fm');
		fsm.action= url ;
		fsm.submit();
	}
	
	//入库操作
	function operaItInWare(value) {
		url = "<%=contextPath%>/sales/storageManage/ReturnVehicleReq/returnDtlCheckInitStorageInWare.do?headId=" + value ;
		var fsm = document.getElementById('fm');
		fsm.action= url ;
		fsm.submit();
	}
	
	function putForwordConfirm(value)
	{
		if(document.getElementById("reason1").value==null||document.getElementById("reason1").value=="")
		{
			MyAlert("审核原因不能为空！");
			return;
		}
		var chk = document.getElementsByName("headIds");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++)
		{        
			if(chk[i].checked)
			{            
				str = chk[i].value+","+str; 
				cnt++;
			}
		}
        if(cnt==0)
        {
             MyAlert("请选择要退车信息！");
             return;
        }
		MyConfirm("确认操作？",putForword,[value]);
	}
	
	
	//提报申请
	function putForword(value)
	{
		document.getElementById("status").value = value ;
		document.getElementById("reason").value = document.getElementById("reason1").value;
		sendAjax("<%=contextPath%>/sales/storageManage/ReturnVehicleReq/returnHeadCheck.json",showForwordValue,'fm','queryBtn'); 
	}
	
	//提报回调函数
	function showForwordValue(json)
	{
		if(json.returnValue == '1')
		{
			MyAlert("审核成功！");
			__extQuery__(1);
			document.getElementById("reason1").value = '';
		}else
		{
			MyAlert("提报失败！请联系系统管理员！");
		}
	}
</script>
</head>
<body> 
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 库存管理 &gt; 经销商退车审核</div>
	<form id="fm" name="fm" method="post">
	<input type="hidden" name="reason" id="reason" />
	<div class="form-panel">
		<h2>经销商退车审核</h2>
		<div class="form-body">
			<table class="table_query" >
				<tr>
					<td width="20%" class="tblopt right">退车单号：</td>
					<td width="39%" >
	      				<input type="text" class="middle_txt" id="returnNo" name="returnNo" />
	    			</td>
					<td>
						<input type="hidden" name="status" id="status" />
						<input type="button" class="u-button u-query"  onclick="__extQuery__(1)" value="查 询" id="queryBtn" />
					</td>
				</tr>
			</table>
		</div>
		</div>
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    	<!--分页 end -->
	</form>
</div>
</body>
</html>
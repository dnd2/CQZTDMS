<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/common.js"></script>


<title>交车vin码列表查询</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  潜客管理 &gt; 交车管理 &gt; 交车vin码列表</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<input type="hidden" id="qkId" name="qkId" value="${qkId}" />
		<input type="hidden" id="qkOrderDetailId" name="qkOrderDetailId" value="${qkOrderDetailId}" />
		<input type="hidden" id="inputId" name="inputId" value="${inputId }" />
		<input type="hidden" id="moderId" name="moderId" value="${moderId }" />
		<input type="hidden" id="colorTdId" name="colorTdId" value="${colorTdId }" />
		<input type="hidden" id="hiddenmoderId" name="hiddenmoderId" value="${hiddenmoderId }" />
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt"><div align="right">VIN：</div></td>
				<td width="30%" >
      				<input type="text" id="vin" name="vin" class="middle_txt" size="20"   />
    			</td>
    			<td width="10%" class="tblopt"><div align="right">车型代码：</div></td>
				<td width="30%" >
      				<input type="text" id="modelCode" name="modelCode" class="middle_txt" size="20"   />
    			</td>
				<td class="table_query_3Col_input"  width="10%" >
					&nbsp;&nbsp;<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> 
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
	
</div>
<script type="text/javascript">

	var myPage;
	var qkOrderDetailId=$("qkOrderDetailId").value;
	var url = "<%=contextPath%>/crm/delivery/DelvManage/getVinList.json?COMMAND=1&qkOrderDetailId="+qkOrderDetailId;
	var title = null;
	var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'VEHICLE_ID',renderer:myLink},
				{header: "车型代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "车型名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'}
		      ];

	function myLink(value,meta,rec){
		var data = rec.data;
		return "<input type='radio'  name='pose_id' value='"+value+"' onclick=submit_("+data.VEHICLE_ID+",'"+data.VIN+"'); />";
    }

	function submit_(vechile_id,vin,flag){
		var qkId=$("qkId").value;
		var qkOrderDetailId=$("qkOrderDetailId").value;
		var inputId=document.getElementById("inputId").value;
		var moderId=document.getElementById("moderId").value;
		var colorTdId=document.getElementById("colorTdId").value;
		var hiddenmoderId=document.getElementById("hiddenmoderId").value;
		//如果为订单页面
		if(inputId!=null&&inputId!="") {
			if (moderId!=null&&moderId!="") {
				parentContainer.$(inputId).value = vin;
				var hiddenVin = "hidden"+inputId;
				parentContainer.$(hiddenVin).value = vechile_id;
				parentContainer.doFindModelColor(inputId,moderId,colorTdId,hiddenmoderId);
				parentContainer._hide();
			} else {//交车页面
				var scrollHeight = parentContainer.document.getElementById("show").offsetHeight;//获取父窗口显示div的高度
				var orderDetailId=$("qkOrderDetailId").value;
				var delvFlag=judgeIfAbleDelvy(orderDetailId);
				if(!delvFlag){
					MyAlert("订单状态不对，无法交车!!!");
					return;
				}
				parentContainer.doDelivery(vechile_id,qkId,qkOrderDetailId,this);
				parentContainer._hide();
				// parentContainer.addMaskLayer(scrollHeight);//添加蒙层
			}
		} else {
			var scrollHeight = parentContainer.document.getElementById("show").offsetHeight;//获取父窗口显示div的高度
			//确认之后就加载数据到交车表中
			parentContainer._hide();
			parentContainer.addDelvData(vechile_id,qkId,qkOrderDetailId);
			parentContainer.addMaskLayer(scrollHeight);//添加蒙层
		}
	}

</script>    
</body>
</html>
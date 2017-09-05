<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 生成订单 </title>

</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：订单管理&gt;财务相关&gt;票据生成
	</div>
<form name="fm" method="post" id="fm">
<input type="hidden" name="groupCode" value="${groupCode}"/>
<input type="hidden" name="materialCode" value="${materialCode}" />
<input type="hidden" name="productstartdate" value="${productstartdate}" />
<input type="hidden" name="productEndDate" value="${productEndDate}" />
<input type="hidden" name="orgStartDate" value="${orgStartDate}" />
<input type="hidden" name="orgEndDate" value="${orgEndDate}" />
<input type="hidden" name="vin" value="${vin}" />
<!-- 查询条件 begin -->
<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;" id="subtab" >
<tr class="table_list_row2">
				<td width="33%">物料价格合计：<span id="a1"></span>
				<input type="hidden" class="middle_txt" name="costPrice" size="15"  value="" id="costPrice"/>
				<input type="hidden" class="middle_txt" name="vStr" size="15"  value="${vStr }" id="vStr"/>	
				</td>
				<td width="33%">车辆总数：<span id="a2"></span>
				<input type="hidden" class="middle_txt" name="vCount" size="15"  value="" id="vCount"/>
				</td>
				<td width="33%">
					开票产地：
					<select id="area" name="area">
						<c:forEach items="${area }" var="list">
							<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
						</c:forEach>
					</select>
				</td>
</tr>
  <tr align="center" class="table_list_row2">
  <td colspan="3" align="center"> 	
    	  <input type="button" id="addBtn" class="normal_btn"  value="保存" onclick="add();" />  
    	  <input type="button" id="canelBtn" class="normal_btn"  value="取消" onclick="back();" />   
    </td>
  </tr>
</table>
<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;">
			
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
	//查询路径           
	var url = "<%=contextPath%>/sales/storage/storagemanage/VehicleCostbudget/addVehicleCostbudgetQuery.json?common=0";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "VIN",dataIndex: 'VIN',align:'center'},
				{header: "物料价格",dataIndex: 'COM_VHCL_PRICE',align:'center'},
				{header: "车系名称",dataIndex: 'SERIES_NAME',align:'center'},
				{header: "车型名称",dataIndex: 'MODEL_NAME',align:'center'},
				{header: "车型",dataIndex: 'MODEL_CODE',align:'center'},
				{header: "配置名称",dataIndex: 'PACKAGE_NAME',align:'center'},
				{header: "配置代码",dataIndex: 'PACKAGE_CODE',align:'center'},
				{header: "物料名称",dataIndex: 'MATERIAL_NAME',align:'center'},
				{header: "物料代码",dataIndex: 'MATERIAL_CODE',align:'center'}
		      ];
	//初始化    
	function doInit(){
		tgSum();
		__extQuery__(1);
	}
	function add()
	{ 
		isNullsub();
	}
	function isNullsub(){
		makeNomalFormCall("<%=contextPath%>/sales/storage/storagemanage/VehicleCostbudget/queryMatPrice.json",function(json){
			if(json.returnValue==1){//有物料价格为0
				MyAlert("物料单价不能为零,请设置成本价！");
			}else if(json.returnValue==3){
				MyConfirm("确认生成！",saveCost);
			}else{
				MyAlert("操作失败！请联系系统管理员！");
			}
		},'fm');
	}
	function saveCost()
	{ 
		disabledButton(["addBtn","canelBtn"],true);
		makeNomalFormCall("<%=contextPath%>/sales/storage/storagemanage/VehicleCostbudget/saveVehicleCostbudget.json",saveCostBack,'fm','queryBtn'); 
	}

	function saveCostBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			fm.action="<%=contextPath%>/sales/storage/storagemanage/VehicleCostbudget/vehicleCostbudgetInit.do";
			fm.submit();
		}else if(json.returnValue == 2){
			disabledButton(["addBtn","canelBtn"],false);
			MyAlert("无车辆信息，无法添加！");
		}else{
			disabledButton(["addBtn","canelBtn"],false);
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	function back(){
		fm.action="<%=contextPath%>/sales/storage/storagemanage/VehicleCostbudget/vehicleCostbudgetInit.do";
		fm.submit();
	}
	//统计数量和
	function tgSum(){
		document.getElementById("a1").innerHTML = '';
		document.getElementById("a2").innerHTML = '';
		document.getElementById("costPrice").value = '';
		document.getElementById("vCount").value = '';
		makeNomalFormCall("<%=contextPath%>/sales/storage/storagemanage/VehicleCostbudget/addVehicleCostbudgetQuery.json?common=1",function(json){
			document.getElementById("a1").innerHTML = json.valueMap.COM_VHCL_PRICE_SUM == null ? '0.0' : json.valueMap.COM_VHCL_PRICE_SUM;
			document.getElementById("a2").innerHTML = json.valueMap.VCOUNT== null ? '0' : json.valueMap.VCOUNT;
			document.getElementById("costPrice").value = json.valueMap.COM_VHCL_PRICE_SUM == null ? '0.0' : json.valueMap.COM_VHCL_PRICE_SUM;
			document.getElementById("vCount").value = json.valueMap.VCOUNT== null ? '0' : json.valueMap.VCOUNT;
		},'fm');
	}
</script>
</body>
</html>

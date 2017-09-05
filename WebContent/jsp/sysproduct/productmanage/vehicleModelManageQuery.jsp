<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车辆型号维护</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 车辆型号维护</div>
<form method="POST" name="fm" id="fm">
  	<table class="table_query" border="0">
  		<Tr>
  			<td  nowrap="nowrap" align="right">车系：</td>
			<td>
				<select id="series" name="series">
					<option value="">--请选择--</option>
					<c:forEach items="${list }" var="po">
						<option value="${po.GROUP_ID }">${po.GROUP_NAME }</option>
					</c:forEach>
				</select>
			</td>
  			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">车辆型号代码：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input name="materialCode" maxlength="30" value="" datatype="1,is_noquotation,30" id="materialCode" type="text" class="middle_txt" />
			</td>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">车辆型号名称：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input name="materialName" maxlength="30" value="" datatype="1,is_noquotation,30" id="materialName" type="text" class="middle_txt" />
			</td>
  		</Tr>
		<tr>
			<td colspan="6" class="table_query_4Col_input" style="text-align: center">
				<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> &nbsp; 
				<input name="queryBtn" type="button" class="normal_btn" onclick="add()" value="新增" id="queryBtn" /> &nbsp; 
			</td>
		</tr>
	</table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">

	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sysproduct/productmanage/VehicleModelManage/getVehicleModelList.json";
				
	var title = null;

	var columns = [
					{header: "车系", dataIndex: 'GROUP_NAME', align:'center'},
					{header: "车辆型号代码", dataIndex: 'VEHICLE_MODEL_CODE', align:'center'},
					{header: "车辆型号名称", dataIndex: 'VEHICLE_MODEL_NAME', align:'center'},
					{header: "操作",dataIndex:'VEHICLE_MODEL_ID',renderer:myLink}
			      ];	
			      
	function myLink(value){
        return String.format(
               "<a href=\"<%=request.getContextPath()%>/sysproduct/productmanage/VehicleModelManage/queryVehicleModelInit.do?id="+value+"\">[查看]</a>"
            		   +"<a href=\"<%=request.getContextPath()%>/sysproduct/productmanage/VehicleModelManage/modifyVehicleModelInit.do?id="+value+"\">[修改]</a>"
            		   +"<a href=\"#\" onclick=\"del("+value+")\">[删除]</a>");
    }
	
	function add(){
		window.location.href = "<%=request.getContextPath()%>/sysproduct/productmanage/VehicleModelManage/addVehicleModelInit.do";
	}
	
	function del(value){
		MyConfirm("确认删除?",function(){
			window.location.href = "<%=request.getContextPath()%>/sysproduct/productmanage/VehicleModelManage/deletedVehicleModel.do?id="+value;
		});
	}
	
	function doInit(){
		__extQuery__(1);
	}
</script>
</body>
</html>

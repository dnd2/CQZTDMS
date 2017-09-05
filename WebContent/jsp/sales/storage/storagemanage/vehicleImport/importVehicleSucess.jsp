<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息导入</title>
<%
	String contextPath = request.getContextPath();
%>

</head>
<body onload="__extQuery__(1)">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：仓储管理

&nbsp;&gt;&nbsp;车辆信息导入</div>
<form method="POST" name="fm" id="fm">
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
	<table class="table_query" width="85%" align="center" border="0"  id="roll">
		<tr align="center" >
			<th colspan="6">
				<div align="left">
					<input class="normal_btn" id="savebtn"  type="button" name='saveResButton' onclick='importSave();' value='确认导入' />
					<input class="normal_btn" id="back" type='button' name='saveResButton' onclick='history.back();' value='返回' />
				</div>
			</th>
	  	</tr>
	</table>
</form>
<script type="text/javascript">

	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/storage/storagemanage/VehicleImport/expressResultSelect.json";

	var title = null;

	var columns;

	columns = [
				{header: "行数", dataIndex: 'ROW_NUMBER', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "物料编码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "颜色", dataIndex: 'COLOR', align:'center'},
				{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
				{header: "变速箱号", dataIndex: 'GEARBOX_NO', align:'center'},
				{header: "合格证号", dataIndex: 'HEGEZHENG_CODE', align:'center'},
				{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center'},
				{header: "下线日期", dataIndex: 'OFFLINE_DATE', align:'center'}
		      ];

	function doInit(){
		__extQuery__(1);
	}


	function importSave() {
		MyConfirm("确认导入?",importAddOrSubmit);
	}


	function importAddOrSubmit(){
		document.getElementById("savebtn").disabled=true;
		makeNomalFormCall('<%=request.getContextPath()%>/sales/storage/storagemanage/VehicleImport/importVehicleSave.json',saveResult,'fm');
	}

	function saveResult(json){
		if(json.eMsg!=null&&json.eMsg!=""&&json.eMsg!="null"){
			MyAlert(json.eMsg);
		}else{
			if(json.count>0){
				//MyAlert("保存成功！！");
				window.location.href="<%=request.getContextPath()%>/sales/storage/storagemanage/VehicleImport/importInit.do";
			}else{
				MyAlert("保存失败！！");
			}
		}
		
	}

</script>
</body>
</html>

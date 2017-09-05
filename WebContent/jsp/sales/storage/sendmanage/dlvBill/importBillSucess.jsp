<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>交接单信息导入</title>
<%
	String contextPath = request.getContextPath();
%>

</head>
<body onload="__extQuery__(1)">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />储运管理>发运管理>交接单导入</div>
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
	var url = "<%=request.getContextPath()%>/sales/storage/sendmanage/DlvWayBillManage/expressResultSelect.json";

	var title = null;

	var columns;

	columns = [
				{header: "行数", dataIndex: 'ROW_NUMBER', align:'center'},
				{header: "组板号", dataIndex: 'BO_NO', align:'center'},
				{header: "订单号", dataIndex: 'ORDER_NO', align:'center'},
				{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配置", dataIndex: 'PAKAGE_NAME', align:'center'},
				{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "原车架号", dataIndex: 'VIN', align:'center'},
				{header: "替换车架号", dataIndex: 'NEW_VIN', align:'center'}
		      ];

	function doInit(){
		__extQuery__(1);
	}


	function importSave() {
		MyConfirm("确认导入?",importAddOrSubmit);
	}


	function importAddOrSubmit(){
		document.getElementById("savebtn").disabled=true;
		makeNomalFormCall('<%=request.getContextPath()%>/sales/storage/sendmanage/DlvWayBillManage/importBillSave.json',saveResult,'fm');
	}

	function saveResult(json){
		
		if(json.eMsg!=null&&json.eMsg!=""&&json.eMsg=="success"){
			MyAlertForFun("导入成功!",queryInit);
		}else{
			MyAlertForFun(json.eMsg,queryInit);
		}
		
	}
	function queryInit() {
		window.location.href="<%=request.getContextPath()%>/sales/storage/sendmanage/DlvWayBillManage/toImportWayBillInit.do";
	}
</script>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>三方车辆导入管理</title>
<%
	String contextPath = request.getContextPath();
%>

</head>
<body onload="__extQuery__(1)">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：储运管理>仓库管理>三方车辆导入确认

</div>
<form method="POST" name="fm" id="fm">
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
	<table class="table_query" width="85%" align="center" border="0"  id="roll">
		<tr align="center" >
			<th colspan="6">
				<div align="left">
					<input type="hidden" id="vins" value="" />
					<!-- <input class="normal_btn" id="printInfo" type='button' name='saveResButton' onclick='openPrint();' style="display:none;" value='打印' /> -->
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
	var url = "<%=request.getContextPath()%>/sales/storage/storagemanage/RecievingStorageImport/expressResultSelectSto.json";
	var title = null;

	var columns;

	columns = [
				{header: "行数", dataIndex: 'ROW_NUMBER', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'}
				//{header: "入库类型", dataIndex: 'IN_TYPE', align:'center'}
		      ];

	function doInit(){
		__extQuery__(1);
	}


	function importSave() {
		MyConfirm("确认导入?",importAddOrSubmit);
	}


	function importAddOrSubmit(){
		//var importFlag=document.getElementById("importFlag").value;
		document.getElementById("savebtn").disabled=true;
		makeNomalFormCall('<%=request.getContextPath()%>/sales/storage/storagemanage/RecievingStorageImport/importRebaeStorageSave.json',saveResult,'fm');
		
	}

	function saveResult(json){
		if(json.count>0){
			MyAlertForFun("成功处理"+json.count+"行数据",queryInit);
			
		}else{
			MyAlertForFun("导入失败!",queryInit);
		}
	}
	
	function queryInit() {
		window.location.href="<%=request.getContextPath()%>/sales/storage/storagemanage/RecievingStorageImport/importQueryInit.do";
	}
</script>
</body>
</html>

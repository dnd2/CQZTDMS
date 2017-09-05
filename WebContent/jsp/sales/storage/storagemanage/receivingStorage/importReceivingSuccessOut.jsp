<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>返利维护导入</title>
<%
	String contextPath = request.getContextPath();
%>

</head>
<body onload="__extQuery__(1)">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车物流管理>接车入库>导入车辆信息

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
					<input type="hidden" name="importFlag" id="importFlag" value="${importFlag}"/>
				</div>
			</th>
	  	</tr>
	</table>
</form>
<script type="text/javascript">

	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/storage/storagemanage/RecievingStorageImport/expressResultSelect.json";
	var title = null;

	var columns;

	columns = [
				{header: "行数", dataIndex: 'ROW_NUMBER', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'}
				//{header: "出库类型", dataIndex: 'IN_OUT_DESC', align:'center'}
		      ];

	function doInit(){
		__extQuery__(1);
	}


	function importSave() {
		MyConfirm("确认导入?",importAddOrSubmit);
	}


	function importAddOrSubmit(){
		var importFlag=document.getElementById("importFlag").value;
		document.getElementById("savebtn").disabled=true;
		if(importFlag=='1'){
			makeNomalFormCall('<%=request.getContextPath()%>/sales/storage/storagemanage/RecievingStorageImport/importRebaeSave.json',saveResult,'fm');
		}else{
			makeNomalFormCall('<%=request.getContextPath()%>/sales/storage/storagemanage/RecievingStorageImport/importRebaeOutSave.json',saveResult2,'fm');
		}
		
	}

	function saveResult(json){
		if(json.count>0){
			if(json.status == 3){
				MyAlert("保存失败,从第"+json.count+"行起没有足够的区道,请添加足够的道位!");
			}else{
				//打开打印页面
				//window.location.href="<%=request.getContextPath()%>/sales/storage/storagemanage/ReceivingStorage/receivingStorageMainInit.do";
				//MyAlert("保存成功,共导入"+json.count+"条车辆记录");
				//document.getElementById("printInfo").style.display="inline";
				//document.getElementById("vins").value = json.vins;
				window.location.href="<%=request.getContextPath()%>/sales/storage/storagemanage/RecievingStorageImport/openImportInit.do";
			}
		}else{
			MyAlert("保存失败!");
		}
	}
	function saveResult2(json){
		if(json.count>0){
			if(json.status == 3){
				MyAlert("保存失败,从第"+json.count+"行起没有足够的区道,请添加足够的道位!");
			}else{
				
				window.location.href="<%=request.getContextPath()%>/sales/storage/storagemanage/RecievingStorageImport/openImportInitOut.do";
			}
		}else{
			MyAlert("保存失败!");
		}
	}
	//打开打印页面
	function openPrint(){
		var vins = document.getElementById("vins").value;
		var url = "<%=request.getContextPath()%>/sales/storage/storagemanage/RecievingStorageImport/openPrintView.do?vins="+vins;
		window.open(url,'','left=0,top=0,width='+screen.availWidth +'- 10,height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
	}

</script>
</body>
</html>

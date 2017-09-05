<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>区域配额导入</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="loadcalendar();showSub(); ">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 计划管理 > 配额分配 > 车厂配额导入</div>
<form method="POST" name="fm" id="fm">
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
	<table class="table_query" width="85%" align="center" border="0"  id="roll">	
		<tr align="center" >
			<th colspan="6">
				<div align="left">
				    <input type='hidden' name='year' value='${year}' />
				    <input type='hidden' name='month' value='${month}' />
				    <input type='hidden' name='areaId' value='${areaId}' />
					<input class="cssbutton" type="button" name='saveResButton' onclick='importSave();' value='保存' />
					<input class="cssbutton" type='button' name='saveResButton' onclick='history.back();' value='返回' />
				</div>
			</th>	
	  	</tr>
	</table>
</form>
<script type="text/javascript">
	document.getElementById("roll").style.display = "none";
	var HIDDEN_ARRAY_IDS=['roll'];
	
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/StorageImport/storageTempQuery.json";;
				
	var title = null;

	var columns;	
	
	columns = [
				{header: "区域代码", dataIndex: 'ORG_CODE', align:'center'},
				{header: "区域名称", dataIndex: 'ORG_NAME', align:'center'},
				{header: "车系代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "车系名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "总量", dataIndex: 'QUOTA_AMT', align:'center'}
		      ];
	function doInit(){
		getWeekList();
		__extQuery__(1);
	}
	
	function getWeekList(){
		var year = document.getElementById("year").value;
		var month = document.getElementById("month").value;
		var url1 = "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/MaxQuotaTotalQuery/getWeekList.json";
		makeCall(url1,showWeekList,{year:year,month:month});
	}
	
	function showWeekList(json){
		
		for(var i=0;i<json.list.length;i++){
			columns.push({header: json.list[i].SET_YEAR+"年"+json.list[i].SET_WEEK+"周", dataIndex: 'W'+i, align:'center'});
		}
	}
	
	function importSave() {
		if(submitForm('fm')){
			$('fm').action= "<%=request.getContextPath() %>/sales/planmanage/QuotaAssign/StorageImport/importExcel.do";
			$('fm').submit();
		}
	}
</script>
</body>
</html>

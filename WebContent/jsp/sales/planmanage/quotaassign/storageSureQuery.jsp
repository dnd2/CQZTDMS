<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车厂配额确认</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 计划管理 > 配额分配 > 车厂配额确认</div>
<form method="POST" name="fm" id="fm">
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
	<table class="table_query" width="85%" align="center" border="0"  id="roll">	
		<tr align="center" >
			<th colspan="6">
				<div align="left">
				    <input type='hidden' name='quotaYear' value='${quotaYear}' />
				    <input type='hidden' name='quotaMonth' value='${quotaMonth}' />
				    <input type='hidden' name='areaId' value='${areaId}' />
					<input class="cssbutton" type="button" name='saveResButton' onclick='confirmIssue();' value='确认' />
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
	var url = "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/StorageConfirm/storageSureQuery.json";;
				
	var title = null;

	var columns;
	
		
	
	function doInit(){
		getWeekList();
		
	}
	
	function getWeekList(){
		var year = document.getElementById("quotaYear").value;
		var month = document.getElementById("quotaMonth").value;
		var url1 = "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/MaxQuotaTotalQuery/getWeekList.json";
		makeCall(url1,showWeekList,{year:year,month:month});
	}
	
	function showWeekList(json){
		columns = [
				{header: "区域代码", dataIndex: 'ORG_CODE', align:'center'},
				{header: "区域名称", dataIndex: 'ORG_NAME', align:'center'},
				{header: "车系代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "车系名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "总量", dataIndex: 'QUOTA_AMT', align:'center',renderer:myLink}
		      ];
		for(var i=0;i<json.list.length;i++){
			columns.push({header: json.list[i].SET_YEAR+"年"+json.list[i].SET_WEEK+"周", dataIndex: 'W'+i, align:'center'});
		}
		__extQuery__(1);
	}
	
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='loginQuery(\""+ record.data.ORG_ID +"\",\""+ record.data.GROUP_ID +"\")'>"+value+"</a>");
	}
	
	function loginQuery(arg1, arg2) {
		var quotaYear = document.getElementById("quotaYear").value;
		var quotaMonth = document.getElementById("quotaMonth").value;
		var areaId = document.getElementById("areaId").value;
		OpenHtmlWindow("<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/StorageConfirm/storageSureDetailPre.do?quotaYear="+quotaYear+"&quotaMonth="+quotaMonth+"&areaId="+areaId+"&orgId="+arg1+"&groupId="+arg2,800,500);
	}
	
	function confirmIssue(){
		if(submitForm('fm')){
			MyConfirm("是否确认?",quotaIssue);
		}
	}
	
	function quotaIssue(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/StorageConfirm/storageSure.json',showResult,'fm');
	}
	
	function showResult(json){
		if(json.returnValue == '1'){
			window.location.href = '<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/StorageConfirm/storageConfirmPre.do';
		}else{
			MyAlert("下发失败！请联系系统管理员！");
		}
	}

</script>
</body>
</html>

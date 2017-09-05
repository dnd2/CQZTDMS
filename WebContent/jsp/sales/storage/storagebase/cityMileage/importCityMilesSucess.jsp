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
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：储运管理>基础管理>城市里程维护>城市里程导入确认

&nbsp;&gt;&nbsp;城市里程维护信息导入</div>
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
	var url = "<%=request.getContextPath()%>/sales/storage/storagebase/CityMilesManageImport/expressResultSelect.json";

	var title = null;

	var columns;

	columns = [
				{header: "行数", dataIndex: 'ROW_NUMBER', align:'center'},
				{header: "出发地", dataIndex: 'FROM_PLACE', align:'center'},
				{header: "目的地所在省", dataIndex: 'DES_PROVICE', align:'center'},
				{header: "目的地所在地级市", dataIndex: 'DES_CITY', align:'center'},
				{header: "目的地所在地区县", dataIndex: 'DES_COUNTY', align:'center'},
				{header: "里程数", dataIndex: 'DISTENCE', align:'center'},
				{header: "运输方式", dataIndex: 'TRANS_WAY', align:'center'},
				{header: "到达天数", dataIndex: 'REACH_DAY', align:'center'},
				{header: "单价", dataIndex: 'PRICE', align:'center'},
				{header: "手工运价", dataIndex: 'HAND_PRICE', align:'center'},
				{header: "系数生效日期", dataIndex: 'FUEL_BEGIN_DATE', align:'center'},
				{header: "系数失效日期", dataIndex: 'FUEL_END_DATE', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center'}
		      ];

	function doInit(){
		__extQuery__(1);
	}


	function importSave() {
		MyConfirm("确认导入?",importAddOrSubmit);
	}


	function importAddOrSubmit(){
		document.getElementById("savebtn").disabled=true;
		makeNomalFormCall('<%=request.getContextPath()%>/sales/storage/storagebase/CityMilesManageImport/importRebaeSave.json',saveResult,'fm');
	}

	function saveResult(json){
		
		if(json.count>0){
			window.location.href="<%=request.getContextPath()%>/sales/storage/storagebase/CityMileageManage/cityMileageInit.do";
		}else{
			if(json.eMsg!=""&&json.eMsg!=null){
				MyAlert(json.eMsg);
			}else{
				MyAlert("保存失败！！");
			}
			
		}
	}

</script>
</body>
</html>

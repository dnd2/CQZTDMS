<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>二手车置换申请</title>
<script type="text/javascript">
<!--
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
  //->
</script>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 二手车置换资格价格维护</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt"><div align="right">置换资格类型：</div></td>
				<td width="39%" >
				<input type="text" class="DISPLACEMENT_TYPE" name="DISPLACEMENT_TYPE" id="DISPLACEMENT_PRC"  value="" />
    			</td>
				<td class="table_query_3Col_input" >
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" /> 
					<input type="button" class="normal_btn" onclick="carAdd();" value=" 新增  " />
				</td>
				<td><input type="button" class="normal_btn" style="width:100px" onclick="dlrAdd();" value=" 经销商资格维护  " />  </td>
			</tr>
		</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	
</div>
<script type="text/javascript">

	var myPage;
	
	var url = "<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementPrcCarQuery.json?COMMAND=1";
	
	var title = null;
	
	var columns = [
				{header: "置换资格", dataIndex: 'DISPLACEMENT_PRC', align:'center',renderer:getItemValue},
				{header: "置换类型", dataIndex: 'DISPLACEMENT_TYPE', align:'center',renderer:getItemValue},
				{header: "返利价格", dataIndex: 'PRICE', align:'center'},
				{header: "创建日期", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "更新日期", dataIndex: 'UPDATE_DATE', align:'center'}
		      ];
		      
	function myLink(disPrc){
        return String.format(
        		 "<a href=\"#\" onclick=\"chkArea(" + disPrc + ");\">[经销商资格维护]</a>");
    }

	function chkArea(disPrc) {
		var url = "<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementCarPrcOneInfo.do?DISPLACEMENT_PRICE_ID="+DISPLACEMENT_PRICE_ID ;
		$('fm').action=url;
		$('fm').submit();
		//makeCall(url, answer, {vehicleId: vehicle_id}) ;
	}
	function carAdd(){
		$('fm').action="<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementPrcInsertCar.do";
		$('fm').submit();
	}
	
	function dlrAdd(){
		$('fm').action="<%=contextPath%>/sales/displacement/DisplacementCarPrice/DisplacementPrcDelaerInIt.do";
		$('fm').submit();
	}

	function answer(json) {
		var flag = json.vFlag ;

		if(flag == "0" && ${oemFlag == 0}) {  // 微车控制
			MyAlert("<strong><font color=\"red\">车辆业务范围与经销商业务范围不一致，为避免操作后的数据错误，请联系业务人员进行修改后再进行操作！</font><strong>") ;

			return ;
		} 
		
		$('fm').action= "<%=contextPath%>/sales/customerInfoManage/SalesReport/toReport.do" ;
		$('fm').submit();
	}
</script>    
</body>
</html>
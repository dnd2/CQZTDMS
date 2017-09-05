<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<%@taglib uri="/jstl/cout" prefix="c"%>
	<%String contextPath = request.getContextPath();%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<script type="text/javascript" src="<%=contextPath%>/js/web/jquery-1.8.0.min.js"></script>
		<title>位维护</title>
	</head>

	<body onload="__extQuery__(1);changeSub();">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理>基础信息管理>仓储相关信息维护>位维护
		</div>
		<form name="fm" method="post" id="fm">
			<!-- 查询条件 begin -->
			<table class="table_query" id="subtab">
				<tr class="csstr" align="center">
					<td align="right">库房：</td>
		            <td align="left">
		                <select name="WH_ID" id="WH_ID" class="short_sel" onchange="changeSub();">
		                  <c:forEach items="${wareHouseList}" var="wareHouse">
		                      <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
		                  </c:forEach>
		                </select>
		            </td>
					<td align="right">排代码：</td>
					<td align="left">
						<select name="LINE_ID" id="LINE_ID" class="short_sel" onchange="changeSub1();">
							<option selected value=''>-请选择-</option>
						</select>
					</td>
					<td align="right">货架代码：</td>
					<td align="left" class="table_info_2col_input">
						<select name="SHELF_ID" id="SHELF_ID" class="short_sel" onchange="changeSub2();">
							<option selected value=''>-请选择-</option>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">层代码：</td>
					<td align="left" class="table_info_2col_input">
						<select name="FLOOR_ID" id="FLOOR_ID" class="short_sel">
							<option selected value=''>-请选择-</option>
						</select>
					</td>
					<td align="right">位代码：</td>
					<td align="left" colspan="3">
						<input type="text" id="POSITION_CODE" name="POSITION_CODE" class="middle_txt" value="" size="15" />
					</td>
				</tr>
				<tr align="center">
					<td colspan="6" align="center">
						<input type="button" id="queryBtn" class="cssbutton" value="查询"
							onclick="__extQuery__(1);" />
						<input type="button" id="queryBtn" class="cssbutton" value="新增"
							onclick="addPage();" />
						<input type="reset" class="cssbutton" id="resetButton" value="重置" />
					</td>
				</tr>
			</table>
			<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
		</form>
	</body>
	<script type="text/javascript">
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/parts/storageManager/partPositionMgr/PartPositionMgr/partPositionQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "排代码",dataIndex: 'LINE_CODE',align:'center'},
				{header: "排名称",dataIndex: 'LINE_NAME',align:'center'},
				{header: "货架代码",dataIndex: 'SHELF_CODE',align:'center'},
				{header: "层代码",dataIndex: 'FLOOR_CODE',align:'center'},
				{header: "货位代码",dataIndex: 'POSITION_CODE',align:'center'},
				{header: "是否有效", dataIndex: 'STATE', align:'center',renderer:getItemValue}
		      ];
	//跳转新增页面
	function addPage(){
		fm.action = "<%=contextPath%>/parts/storageManager/partPositionMgr/PartPositionMgr/addInit.do";
		fm.submit();
	}
	/******************/
	jQuery.noConflict();
	function changeSub(){
		var whId = jQuery("#WH_ID").val();
		if(whId!=""){
			var url2 = "<%=contextPath%>/parts/storageManager/partFloorMgr/PartFloorMgr/getSubCode.json";
			var para = "table=TT_PART_LOCATION_LINE&column=WH_ID&parentId="+whId;
			makeCall(url2,forBack,para);
		}else{
			jQuery("#LINE_ID").find("option").remove();
			jQuery("#LINE_ID").append("<option value=''>-请选择-</option>");
		}
	}
	function forBack(json){
		jQuery("#LINE_ID").find("option").remove(); 
		jQuery("#LINE_ID").append("<option value=''>-请选择-</option>");
		for(var i=0;i<json.subcode.length;i++){
			var id = json.subcode[i].LINE_ID;
			var str = json.subcode[i].LINE_CODE;
			jQuery("#LINE_ID").append("<option value='"+id+"'>"+str+"</option>");
		}
	}
	function changeSub1(){
		var lineId = jQuery("#LINE_ID").val();
		if(lineId!=""){
			var url2 = "<%=contextPath%>/parts/storageManager/partFloorMgr/PartFloorMgr/getSubCode.json";
			var para = "table=TT_PART_LOCATION_SHELF&column=LINE_ID&parentId="+lineId;
			makeCall(url2,forBack1,para);
		}else{
			jQuery("#SHELF_ID").find("option").remove();
			jQuery("#SHELF_ID").append("<option value=''>-请选择-</option>");
		}
	}
	function forBack1(json){
		jQuery("#SHELF_ID").find("option").remove(); 
		jQuery("#SHELF_ID").append("<option value=''>-请选择-</option>");
		for(var i=0;i<json.subcode.length;i++){
			var str = json.subcode[i].SHELF_ID;
			var shelf_code = json.subcode[i].SHELF_CODE;
			jQuery("#SHELF_ID").append("<option value='"+str+"'>"+shelf_code+"</option>");
		}
	}
	function changeSub2(){
		var shelfId = jQuery("#SHELF_ID").val();
		if(shelfId!=""){
			var url2 = "<%=contextPath%>/parts/storageManager/partFloorMgr/PartFloorMgr/getSubCode.json";
			var para = "table=TT_PART_LOCATION_FLOOR&column=SHELF_ID&parentId="+shelfId;
			makeCall(url2,forBack2,para);
		}else{
			jQuery("#FLOOR_ID").find("option").remove();
			jQuery("#FLOOR_ID").append("<option value=''>-请选择-</option>");
		}
	}
	function forBack2(json){
		jQuery("#FLOOR_ID").find("option").remove(); 
		jQuery("#FLOOR_ID").append("<option value=''>-请选择-</option>");
		for(var i=0;i<json.subcode.length;i++){
			var str = json.subcode[i].FLOOR_ID;
			var floor_code = json.subcode[i].FLOOR_CODE;
			jQuery("#FLOOR_ID").append("<option value='"+str+"'>"+floor_code+"</option>");
		}
	}

</script>
</html>
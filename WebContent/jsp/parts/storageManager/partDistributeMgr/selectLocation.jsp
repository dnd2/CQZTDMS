<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%String contextPath = request.getContextPath();%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/web/jquery-1.8.0.min.js"></script>
<title>货位选择</title>
</head>

<body onload="__extQuery__(1);">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：货位选择
</div>
	<form name="fm" method="post" id="fm">
        <input type="hidden" name="whId" id="whId" value="${whId}"/>
	<!-- 查询条件 begin -->
	<table class="table_query" id="subtab">
		<tr >
			<td align="right">排代码：</td>
			<td align="left">
				<select name="LINE_ID" id="LINE_ID" class="short_sel" onchange="changeSub1();">
					<option selected value=''>-请选择-</option>
					<c:forEach items="${listLine}" var="obj">
						<option value="${obj.LINE_ID}">${obj.LINE_CODE}</option>
					</c:forEach>
				</select>
			</td>
			<td align="right">货架代码：</td>
			<td align="left">
				<select name="SHELF_ID" id="SHELF_ID" class="short_sel" onchange="changeSub2();">
					<option selected value=''>-请选择-</option>
				</select>
			</td>
		</tr>
		<tr>
			<td align="right">层代码：</td>
			<td align="left">
				<select name="FLOOR_ID" id="FLOOR_ID" class="short_sel">
					<option selected value=''>-请选择-</option>
				</select>
			</td>
			<td align="right">位代码：</td>
			<td align="left">
				<input type="text" id="POSITION_CODE" name="POSITION_CODE" class="middle_txt" value="" size="15" />
			</td>
		</tr>
        <tr>
            <td align="right">货位代码：</td>
            <td align="left">
                <input type="text" id="locCode" name="locCode" class="middle_txt" value="" size="15" />
            </td>
            <td align="right">配件编码：</td>
            <td align="left">
                <input type="text" id="partOldCode" name="partOldCode" class="middle_txt" value="" size="15" />
            </td>
        </tr>
        <tr align="center">
            <td colspan="4" align="center">
                <input type="button" id="queryBtn" class="cssbutton" value="查询" onclick="__extQuery__(1);" />
                <input type="reset" class="cssbutton" id="resetButton" value="重置" />
                <input type="reset" class="cssbutton" id="resetButton" value="关闭" onclick="_hide();" />
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
<script type="text/javascript" >
	jQuery.noConflict();
	var myPage;
	//查询路径
	var url = g_webAppName + "/parts/storageManager/partDistributeMgr/PartDistributeMgr/selectLocation.json";
	var title = null;
	var columns = [
				{header: "选择",  align:'center',dataIndex:'LOC_ID',renderer:getRadio},
				{header: "货位代码",dataIndex: 'LOC_CODE',align:'center'},
				{header: "货位名称",dataIndex: 'LOC_NAME',align:'center'}
//				{header: "对应配件",dataIndex: 'PART_CODE',align:'center'}
		      ];
	function getRadio(value,meta,record){
		var i = record.data.LOC_ID;
		var c = record.data.LOC_CODE;
		var n = record.data.LOC_NAME;
		var radioText = "<input type='radio' name='abc' value='123' onclick = 'selData("+'"'+i+'"'+","+'"'+c+'"'+","+'"'+n+'"'+");'/>";
		return String.format(radioText);
	}
	var locId = "${loc_id}";
	function selData(i,c,n){
		if(locId!='' && (typeof parentContainer.LOC_ID != 'undefined') ){parentContainer.LOC_ID=locId;}
		parentContainer.codeSet(i,c,n);
        _hide();
	}
	/******************/
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
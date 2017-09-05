<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%String contextPath = request.getContextPath();%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>架维护</title>
<script type="text/javascript" src="<%=contextPath%>/js/web/jquery-1.8.0.min.js"></script>
</head>

<body onload="__extQuery__(1);changeSub();">
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：配件管理>基础信息管理>仓储相关信息维护>架维护
	</div>
	<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" id="subtab" >
	    <tr class="csstr" align="center">
	    	<td align="right">库房：</td>
            <td align="left" class="table_info_2col_input">
                <select name="WH_ID" id="WH_ID" class="short_sel" onchange="changeSub();">
                  <c:forEach items="${wareHouseList}" var="wareHouse">
                      <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
                  </c:forEach>
                </select>
            </td>
	    	<td align="right">货位排代码：</td> 
	   		<td align="left">
			  <select name="LINE_CODE" id="LINE_CODE" class="short_sel">
                  <option selected value=''>-请选择-</option>
              </select>
		  	</td>
	    	<td align="right">货架代码：</td> 
		  	<td align="left">
			  <input type="text" id="SHELF_CODE" name="SHELF_CODE" class="middle_txt" value="" size="15" />
		  	</td> 
	  	</tr> 
	  	<tr align="center">
	  		<td colspan="6" align="center">
	    	  <input type="button" id="queryBtn" class="cssbutton"  value="查询" onclick="__extQuery__(1);" />  
	    	  <input type="button" id="queryBtn" class="cssbutton"  value="新增" onclick="addPage();" />   	
	  		  <input type="reset" class="cssbutton" id="resetButton"  value="重置"/>
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
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/parts/storageManager/partShelfMgr/PartShelfMgr/partShelfQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "排代码",dataIndex: 'LINE_CODE',align:'center'},
				{header: "排名称",dataIndex: 'LINE_NAME',align:'center'},
				{header: "货架代码",dataIndex: 'SHELF_CODE',align:'center'},
				{header: "层数", dataIndex: 'FLOOR_COUNT', align:'center'},
				{header: "位数", dataIndex: 'POSITION_COUNT', align:'center'},
				//{header: "入库状态", dataIndex: 'IN_STATUS', align:'center',renderer:getItemValue},
				//{header: "出库状态", dataIndex: 'OUT_STATUS', align:'center',renderer:getItemValue},
				{header: "是否有效", dataIndex: 'STATE', align:'center',renderer:getItemValue}
		      ];
	//跳转新增页面
	function addPage(){
		fm.action = "<%=contextPath%>/parts/storageManager/partShelfMgr/PartShelfMgr/addInit.do";
		fm.submit();
	}
	jQuery.noConflict();
	function changeSub(){
		var whId = jQuery("#WH_ID").val();
		if(whId!=""){
			var url2 = "<%=contextPath%>/parts/storageManager/partFloorMgr/PartFloorMgr/getSubCode.json";
			var para = "table=TT_PART_LOCATION_LINE&column=WH_ID&parentId="+whId;
			makeCall(url2,forBack,para);
		}else{
			jQuery("#LINE_CODE").find("option").remove();
			jQuery("#LINE_CODE").append("<option value=''>-请选择-</option>");
		}
	}
	function forBack(json){
		jQuery("#LINE_CODE").find("option").remove(); 
		jQuery("#LINE_CODE").append("<option value=''>-请选择-</option>");
		for(var i=0;i<json.subcode.length;i++){
			var str = json.subcode[i].LINE_CODE;
			jQuery("#LINE_CODE").append("<option value='"+str+"'>"+str+"</option>");
		}
	}
</script>
</html>
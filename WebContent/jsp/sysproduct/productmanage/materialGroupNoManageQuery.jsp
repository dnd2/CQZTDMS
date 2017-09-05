<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>物料维护</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 物料维护</div>
<form method="POST" name="fm" id="fm">
  	<table class="table_query" border="0">
		<tr>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">物料代码：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input name="materialCode" maxlength="30" value="" datatype="1,is_noquotation,30" id="materialCode" type="text" class="middle_txt" />
			</td>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">物料名称：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input name="materialName" maxlength="30" value="" datatype="1,is_noquotation,30" id="materialName" type="text" class="middle_txt" />
			</td>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">物料状态：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<script type="text/javascript">
			      	genSelBoxExp("status",<%=Constant.STATUS%>,"",true,"short_sel","","false",'');
			    </script>
			</td>
		</tr>
		<tr>
			<td colspan="6" class="table_query_4Col_input" style="text-align: center">
				<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> &nbsp; 
				<input type="button" class="normal_btn" onclick="reset();" value="重 置"/> &nbsp; 
			</td>
		</tr>
	</table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">

	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupNoManage/materialManageQuery.json";;
				
	var title = null;

	var columns = [
					{header: "序号", align:'center', renderer:getIndex},
					{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
					{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
					{header: "状态", dataIndex: 'STATUS', align:'center', renderer:getItemValue},
					{id:'action', header: "操作" ,dataIndex: 'MATERIAL_ID', renderer:myLink}
			      ];	
			      
	function myLink(value,metaDate,record){
		var materialCode = record.data.MATERIAL_CODE ;
        return String.format(
               "<a href=\"<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupNoManage/materialManageModPre.do?materialId="+value+"\">[维护]</a><a href=\"#\" onclick=\"mySure('" + materialCode + "') ;\">[关联]</a>");
    }
	
	function mySure(value) {
		MyConfirm("确认关联？", setLink, [value]) ;
	}
	
	function setLink(value, value1) {
		var oAs = document.getElementsByTagName("a") ;
		
		var iLen = oAs.length ;
		
		for(var i=0; i<iLen; i++) {
			oAs[i].style.display = "none" ;
		}
		
		var url = "<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupNoManage/setLink.json" ;
		makeCall(url, showNotice, {materialCode:value,seriesCode:value1}) ;
	}
	
	function showNotice(json) {
		var flag = json.flagInt ;
		
		if(flag == 1) {
			MyAlert("关联成功！") ;
			
			__extQuery__(1);
		} else if(flag == 0) {
			MyAlert(json.materialCode + "物料不存在！") ;
			
			__extQuery__(1);
		} else if(flag == -1) {
			MyAlert(json.materialCode + "物料编码错误！") ;
			
			__extQuery__(1);
		} else if(flag == -2) {
			showMaterialGroupN(json.materialCode,'','false','2','true') ;
		} else if(flag == -3) {
			MyAlert(json.materialCode + "关联已存在，请刷新后再尝试！") ;
			
			__extQuery__(1);
		} else if(flag == -4) {
			showMaterialGroupN(json.materialCode,'','false','2','true') ;
		} else if(flag == -5) {
			MyAlert("车型编码为：'" + json.modelCode + "'下不存在未关联的物料，请刷新后再尝试！") ;
			
			__extQuery__(1);
		}
		
		var oAs = document.getElementsByTagName("a") ;
		
		var iLen = oAs.length ;
		
		for(var i=0; i<iLen; i++) {
			oAs[i].style.display = "inline" ;
		}
	}
</script>
</body>
</html>

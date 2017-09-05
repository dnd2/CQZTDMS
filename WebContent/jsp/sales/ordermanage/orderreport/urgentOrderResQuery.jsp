<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<script type="text/javascript">
	
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>补充订单资源查询</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单提报 > 补充订单资源查询</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="95%">
  	<tr>
      <td align="right" nowrap >资源状态：</td>
      <td align="left" nowrap >
      	<select name="resStatus">
			<option value="">-请选择-</option>
			<option value="0">有</option>
			<option value="1">无</option>
        </select>
      </td>
      <td align="right" nowrap >业务范围：</td>
      <td align="left" nowrap >
      	<select name="areaId">
			<option value="">-请选择-</option>
			<c:forEach items="${areaList}" var="po">
				<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
			</c:forEach>
        </select>
      </td>
      <td align="right" nowrap >选择物料组：</td>
      <td align="left" nowrap >
      	<input type="text" class="middle_txt" name="groupCode" size="15"  value="" id="groupCode" />
      	<input type="hidden" name="groupName" size="20" id="groupName" value="" />
		<input name="button1" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','groupName','false','');" value="..." />
		<input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
      </td>
      <td align="center"><input id="queryBtn" name="button2" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">&nbsp;
      <input id="queryBtn" name="button2" type=button class="cssbutton" onClick="getDownLoad();" value="下载"></td>
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
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderResQuery/urgentOrderResQuery.json";
				
	var title = null;

	var columns = [
				{header: "物料编号", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
				{header: "资源情况", dataIndex: 'RAMOUNT', align:'center'}
		      ];		         
	
	//修改的超链接
	function resDesc(value,meta,record){
		var desc = "无";
		if(value > 0){
			desc = "有";
		}
  		return desc;
	}
	
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
    
    function toChangeDis1(){
		document.getElementById("materialCode").disabled = "disabled";
		document.getElementById("button2").disabled = "disabled";
		document.getElementById("groupCode").disabled = "";
		document.getElementById("button1").disabled = "";
		document.getElementById("groupCode").value = "";
		document.getElementById("materialCode").value = "";
	}
	
	function toChangeDis2(){
		document.getElementById("materialCode").disabled = "";
		document.getElementById("button2").disabled = "";
		document.getElementById("groupCode").disabled = "disabled";
		document.getElementById("button1").disabled = "disabled";
		document.getElementById("groupCode").value = "";
		document.getElementById("materialCode").value = "";
	}
	
	function getDownLoad(){
		$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderResQuery/urgentOrderResDownLoad.json";
		$('fm').submit();
	}
</script>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>

<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>可利用资源查询</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt;  销售订单管理  &gt;订单审核 &gt; 所有资源查询</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
	<table class="table_query" border="0">
		<tr>
	      <td align="left" nowrap >资源状态：</td>
	      <td align="left" nowrap >
	      	<select name="resStatus">
	      		<option value="">--请选择--</option>
	      		<option value="10241001">正常状态</option>
	      		<option value="10241012" selected>特殊车锁定状态</option>
	      	</select>
	      </td>
	     <td >物料代码：</td>
	     <td ><input type="text" name="materialCode"/></td>
		</tr>
		<tr>
		  <td class="tblopt"><div align="left">VIN：</div></td>
		  <td >
	      				<textarea id="vin" name="vin" cols="18" rows="5" ></textarea>
	      </td>
	      <td colspan="2"></td>
		</tr>
	    <tr>
	    	<td> &nbsp;</td>
			<td  colspan="2" align="left" >
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" />
			</td>
	        <td align="right" >
	             页面大小：<input name="pageSize" id="pageSize" type="text" class="mini_txt" value="10" datatype="0,isDigit,3"/>
	        </td>
		</tr>
	</table>
<!--分页部分  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />	
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>
<script type="text/javascript">
	var myPage;
	
	var url = "<%=contextPath%>/sales/ordermanage/orderaudit/ResourceLock/resourceQueryList.json?COMMAND=1";
	
	var title = null;
	
	var columns = [
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
				{header: "车架号", dataIndex: 'VIN', align:'center'},
				{header: "状态", dataIndex: 'LOCK_STATUS', align:'center',renderer:getItemValue},
				{header: "操作", dataIndex: 'VEHICLE_ID', align:'center',renderer:lockDetail}
		      ];
		      
	function lockDetail(value,meta,record){
		var material_id=record.data.MATERIAL_ID;
		var vehicle_id=record.data.VEHICLE_ID;
		var lock_status=record.data.LOCK_STATUS;
		var str="";
		if(lock_status!=10241012){
			str+="<a href='#' onclick='lockDetailInfo(\""+ vehicle_id +"\",1)'>[锁定]</a>";
		}else{
			str+="<a href='#' onclick='lockDetailInfo(\""+ vehicle_id +"\",0)'>[解锁]</a>";
		}
		return String.format(str) ;
	}
	function lockDetailInfo(vehicle_id,flag){
		var url = "<%=contextPath%>/sales/ordermanage/orderaudit/ResourceLock/lockVechile.json?vehicleId="+vehicle_id+"&flag="+flag;
		makeFormCall(url, showLock, "fm") ;
	}
	function showLock(json){
		if(json.flag==1){
			MyAlert("操作成功！！");
			__extQuery__(1);
			
		}else{
			MyAlert("操作失败！！");
		}
	}
</script>    
</body>
</html>
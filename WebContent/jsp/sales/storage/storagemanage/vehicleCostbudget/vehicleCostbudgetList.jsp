<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 车辆成本预算管理 </title>

</head>

<body onload="__extQuery__(1);">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：订单管理&gt;财务相关&gt;龙江开票
	</div>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab" >
<tr class="csstr" align="center">	
	<c:choose>
		<c:when test="${materialCode eq ''}">
			  <td class="right" width="15%"><input type="radio" checked="checked" name="flag" onclick="toChangeMaterial(1);" />选择物料组：</td>
		      <td align="left">
		      	<input type="text" maxlength="20"  class="middle_txt" name="groupCode" size="15"  value="${groupCode}" id="groupCode" readonly="readonly" />
				<input name="button1" id="btn1" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','true','');" value="..." />
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
		      </td>
			  <td class="right" width="15%"><input type="radio" name="flag" onclick="toChangeMaterial(2);" />选择物料：</td>
		      <td align="left">
		      	<input type="text" maxlength="20"  class="middle_txt" name="materialCode" size="15"  value="${materialCode}" id="materialCode" readonly="readonly" />
				<input name="button2" id="btn2"  type="button" class="mini_btn" onclick="showMaterial('materialCode','','true');" value="..." disabled="disabled"/>
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('materialCode');"/>
		      </td>
		</c:when>
		<c:otherwise>
			  <td class="right" width="15%"><input type="radio" name="flag" onclick="toChangeMaterial(1);" />选择物料组：</td>
		      <td align="left">
		      	<input type="text" maxlength="20"  class="middle_txt" name="groupCode" size="15"  value="${groupCode}" id="groupCode" readonly="readonly" />
				<input name="button1" id="btn1" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','true','');" value="..." disabled="disabled"/>
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
		      </td>
			  <td class="right" width="15%"><input type="radio" name="flag" checked="checked" onclick="toChangeMaterial(2);" />选择物料：</td>
		      <td align="left">
		      	<input type="text" maxlength="20"  class="middle_txt" name="materialCode" size="15"  value="${materialCode}" id="materialCode" readonly="readonly" />
				<input name="button2" id="btn2"  type="button" class="mini_btn" onclick="showMaterial('materialCode','','true');" value="..." />
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('materialCode');"/>
		      </td>
		</c:otherwise>
	</c:choose>
 </tr>
  <tr class="csstr" align="center">
	<td class="right" nowrap="true">生产日期：</td>
		<td align="left" nowrap="true">
			<input name="PRODUCT_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="PRODUCT_STARTDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'PRODUCT_STARTDATE', false);" />  
             &nbsp;至&nbsp;
             <input name="PRODUCT_ENDDATE" type="text" maxlength="20"  class="middle_txt" id="PRODUCT_ENDDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'PRODUCT_ENDDATE', false);" />  
		</td>
		<td class="right" nowrap="true">入库日期：</td>
		<td align="left" nowrap="true">
			<input name="ORG_STORAGE_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="ORG_STORAGE_STARTDATE"  readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'ORG_STORAGE_STARTDATE', false);" /> 
             &nbsp;至&nbsp;
             <input name="ORG_STORAGE_ENDDATE" type="text" maxlength="20"  class="middle_txt"   id="ORG_STORAGE_ENDDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'ORG_STORAGE_ENDDATE', false);" /> 
		</td>
		 
  </tr> 
 <tr class="csstr" align="center">
	<td class="right" nowrap="true">VIN：</td>
	<td align="left">
			<textarea  cols="30" rows="2" name="VIN"  id="VIN" ></textarea>
	</td>
	<td class="right" nowrap="true">产地：</td>
	<td align="left" nowrap="true">
					<select id="area" name="area">
						<c:forEach items="${area }" var="list">
							<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
						</c:forEach>
					</select>
	</td>
  </tr> 
  <tr align="center">
  <td colspan="6" class="table_query_4Col_input" style="text-align: center">
  		  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" />   	
    	  <input type="button" id="addBtn" class="normal_btn"  value="生成票据" onclick="add();" />   
    </td>
  </tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/storage/storagemanage/VehicleCostbudget/vehicleCostbudgetQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"groupIds\")'/>",sortable: false,dataIndex: 'VEHICLE_ID',renderer:myCheckBox},
				{header: "VIN",dataIndex: 'VIN',align:'center'},
				{header: "车系名称",dataIndex: 'SERIES_NAME',align:'center'},
				//{header: "车型名称",dataIndex: 'MODEL_NAME',align:'center'},
				//{header: "车型",dataIndex: 'MODEL_CODE',align:'center'},
				//{header: "配置名称",dataIndex: 'PACKAGE_NAME',align:'center'},
				//{header: "配置代码",dataIndex: 'PACKAGE_CODE',align:'center'},
				{header: "物料名称",dataIndex: 'MATERIAL_NAME',align:'center'},
				{header: "物料代码",dataIndex: 'MATERIAL_CODE',align:'center'},
				{header: "发动机号",dataIndex: 'ENGINE_NO',align:'center'},
				{header: "生产日期",dataIndex: 'PRODUCT_DATE',align:'center'},
				{header: "入库日期",dataIndex: 'ORG_STORAGE_DATE',align:'center'},
				{header: "物料价格",dataIndex: 'COM_VHCL_PRICE',align:'center'}
		      ];
	//初始化    
	function doInit(){
		//日期控件初始化
		//__extQuery__(1);
	}
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' id='groupIds' name='groupIds' value='" + value + "' title='"+record.data.PACKAGE_CODE+"' />");
	}
	//跳转新增页面
	function add()
	{
		var b=0;
		var arrayObj = new Array(); 
		arrayObj=document.getElementsByName("groupIds");
		for(var i=0;i<arrayObj.length;i++){
			if(arrayObj[i].checked){
				b=1;//有选中
			}
		}
		if(b==0){
			MyAlert("请选择车辆信息！");
			return ;
		}
		fm.action = "<%=contextPath%>/sales/storage/storagemanage/VehicleCostbudget/addVehicleCostbudgetInit.do";
		fm.submit();	
	}
	//清空数据
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
	
	function toChangeMaterial(type){
		var materialObj = document.getElementById("groupCode");//物料组
		var groupObj = document.getElementById("materialCode");//物料
		var bt1 = document.getElementById("btn1");
		var bt2 = document.getElementById("btn2");

		//选择物料组
		if(type==1){
			materialObj.readOnly = true;
			materialObj.value="";
			materialObj.readOnly = false;
			bt1.disabled  = false;
			bt2.disabled  = true;
		}else{
			groupObj.readOnly = true;
			groupObj.value="";
			groupObj.readOnly=false;
			bt1.disabled  = true;
			bt2.disabled  = false;
		}
	}
</script>
</body>
</html>

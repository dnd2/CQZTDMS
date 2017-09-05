<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>物料维护</title>
<% 
	String curPage = request.getAttribute("curPage") != null ? request.getAttribute("curPage").toString() : "1";
%>
</head>
<body>
<div class="wbox" id="wbox" >
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 物料维护</div>
<form method="POST" name="fm" id="fm">
<input type="hidden" id="flag" value="colorflag">
  	<table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query table_list">
		<tr>
		    <td class="right">物料代码：</td>
		    <td>
		    	<input name="materialCode" datatype="0,is_null,50" id="materialCode" type="text" class="middle_txt"/>
		    </td>
		    <td class="right">物料名称：</td>
		    <td>
		    	<input name="materialName" datatype="0,is_null,300" id="materialName" type="text" class="middle_txt" />
		    </td>
	  	</tr>
		<!-- <tr>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">车型6位码：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap">
		    	<input name="modelCode" datatype="1,is_null,30" id="modelCode" type="text" class="middle_txt" />
		    </td>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">内饰代码：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
		    	<input name="trimCode" id="trimCode" type="text" class="middle_txt" />
		    </td>
	  	</tr>
		<tr>
		    <td class="right">年型：</td>
		    <td>
		    	<input name="modelYear" datatype="1,is_null,4" id="modelYear" type="text" class="middle_txt" />
		    </td>
		    <td class="right">上市日期：</td>
		    <td>
				<input class="short_txt"  type="text" id="issueDate" name="issueDate"  datatype="1,is_date,10" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 'issueDate', false);" value="&nbsp;" />
		    </td>
	  	</tr>
		 <tr style="display:none">
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">生效日期：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap">
				<input class="short_txt"  type="text" id="enableDate" name="enableDate"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'enableDate', false);" value="&nbsp;" />
		    </td>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">失效日期：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
				<input class="short_txt"  type="text" id="disableDate" name="disableDate"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'disableDate', false);" value="&nbsp;" />
		    </td>
	  	</tr> -->
		<tr>
		    <td class="right">物料状态：</td>
		    <td>
		    	<script type="text/javascript">
			      	genSelBoxExp("status",<%=Constant.STATUS%>,"",false,"","","false",'');
			    </script>
		    </td>
		    <td class="right">生产状态：</td>
		    <td>
				<script type="text/javascript">
			      	genSelBoxExp("procuctFlag",<%=Constant.FORECAST_FLAG_REPORT_PRO%>,"",false,"","","false",'');
			    </script>
		    </td>
		    
	  	</tr>
	  	<tr>
	  		<td class="right">可提报订单：</td>
		    <td>
				<script type="text/javascript">
			      	genSelBoxExp("orderFlag",<%=Constant.NASTY_ORDER_REPORT_TYPE%>,"",false,"","","false",'');
			    </script>
		    </td>
		    <td class="right">所属物料组：</td>
		    <td>
		    	<input type="text" class="middle_txt" datatype="0,is_null,30" name="groupCode" id="groupCode" readonly="readonly" />
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','groupName','false','4','true');"  value="..." />
				<input class="middle_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
		    </td>
	  	</tr>
	    <!-- <tr>
	  		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">内部型号(老)：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
				<input name="erpPackage" maxlength="10" id="erpPackage" type="text" class="middle_txt" datatype="0,is_null,8"/>
		    </td>
		     <td class="table_query_2Col_label_6Letter" nowrap="nowrap">装配状态代码：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
		    	<input name="ERPName" datatype="0,is_null,300" id="ERPName" type="text" class="middle_txt" />
		    </td>
		   
	  	</tr>-->
	  	
	  	<tr>
		    <td class="right">颜色代码：</td>
		    <td>
		       <input name="colorCode" datatype="0,is_null,300" id="colorCode" type="text" class="middle_txt" />
		    </td>
		    <td class="right">颜色名称：</td>
		    <td>
		    	<input name="colorName" datatype="0,is_null,300" id="colorName" type="text" class="middle_txt" />
		    </td>
	  	</tr> 
	  	<!-- <tr>
		    <td class="right">物料类型：</td>
		    <td>
		    	<script type="text/javascript">
			      	genSelBoxExp("mat_type",<%=Constant.MAT_TYPE%>,"",false,"","","false",'');
			    </script>
		    </td>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">是否出口：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
		    	<script type="text/javascript">
			      	genSelBoxExp("is_export",<%=Constant.IF_TYPE%>,<%=Constant.IF_TYPE_NO%>,false,"min_sel","","false",'');
			    </script>
		    </td>
	  	</tr>
	  	<tr>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">是否内销：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap" colspan="3">
		    	<script type="text/javascript">
			      	genSelBoxExp("isInsale",<%=Constant.IF_TYPE%>,<%=Constant.IF_TYPE_NO%>,false,"min_sel","","false",'');
			    </script>
		    </td>
	  	</tr> -->
	  	<tr>
	  	    <td class="right" nowrap="nowrap">配置说明：</td>
		    <td align="left">
		    	<textarea rows="3" cols="50" name="remark2"></textarea>	     
		    </td>
		    <td class="right" nowrap="nowrap">上市日期：</td>
		    <td>
		    	<input name="issueDate" id="issueDate" type="text" class="middle_txt" datatype="0,is_null,10"onFocus="WdatePicker({el:$dp.$('issueDate')})"  style="cursor: pointer;width: 80px;"/>
		    </td>
	  	</tr>
	  	<tr>
		    <td colspan="4" align="center">
		      <input name="button2" type="button" class="normal_btn" onclick="confirmAdd();" value="保存"/>
		      <input name="button" type="button" class="normal_btn" onclick="goBack()" value="返回"/>
		    </td>
	  	</tr>
	</table>
</form>
</div>

<script type="text/javascript">	
	//初始化
    function doInit(){
   		//loadcalendar();  //初始化时间控件
	}
	
	function confirmAdd(){
		if(submitForm('fm')){
			MyConfirm("是否确认保存?",addSave);
		}
	}
	
	function addSave(){
		makeNomalFormCall('<%=request.getContextPath()%>/sysproduct/productmanage/MaterialManage/materialManageAdd.json',showResult,'fm');
	}
	
	function showResult(json){
		if(json.returnValue == '1'){
			MyAlert("新增物料成功!");
			//document.getElementById('materialCode').value = '';
			//window.location.href = '<%=request.getContextPath()%>/sysproduct/productmanage/MaterialManage/materialManageQueryPre.do';
			goBack();
		}else{
			MyAlert("新增失败！请联系系统管理员！");
		}
	}
	
	function clrTxt(txtId){
    	document.getElementById(txtId).value="";
    	var objSelectNow = document.getElementById("colorCode");
    	objSelectNow.options.length=0;
		var opp = new Option("-请选择-","");
		objSelectNow.add(opp);
    	document.getElementById("colorName").value = '';
    }
	
	//根据物料组编码获取该车系的所有颜色
	function getColor(){
		var groupCode = document.getElementById("groupCode").value;
		var url = "<%=request.getContextPath()%>/sysproduct/productmanage/MaterialManage/getColorByGroupCode.json";
		makeCall(url,addColorList,{groupCode:groupCode});
	}
	
	//获取颜色的回调函数
	function addColorList(json){
		var objSelectNow = document.getElementById("colorCode");
		if(json.colorList.length>0){
			for(var i=0;i<json.colorList.length;i++){
				var opp = new Option(json.colorList[i].COLOR_CODE,json.colorList[i].COLOR_CODE+"_"+json.colorList[i].COLOR_NAME);
				objSelectNow.add(opp);
			}
		}else{
			objSelectNow.options.length=0;
			var opp = new Option("-请选择-","");
			objSelectNow.add(opp);
		}
		document.getElementById("colorName").value = '';
	}
	
	//单击颜色编码下拉框动态给颜色名称赋值
	function changeColor(){
		var colorCodeAndName = document.getElementById("colorCode").value;
		var colorName = colorCodeAndName.split("_")[1];
		var colorCode = colorCodeAndName.split("_")[0];
		if(typeof(colorName)=="undefined"){
			document.getElementById("colorName").value = '';
		}else{
		   document.getElementById("colorName").value = colorName; 
		}
		// 根据选择的颜色自动生成物料代码
		var groupCode = document.getElementById("groupCode").value;
		var materialCode = groupCode + '.' + colorCode;
		document.getElementById("materialCode").value = materialCode;

		// 根据选择的颜色自动生成颜色名称
		var url = "<%=request.getContextPath()%>/sysproduct/productmanage/MaterialManage/getMaterialNameByGroupCode.json";
		makeCall(url,addMaterialName,{groupCode:groupCode});
	}

	function addMaterialName(json) {
		document.getElementById('materialName').value = json.name + ' ' +  document.getElementById("colorName").value;
	}
	
	//禁止没有选择所属物料组就去选择颜色编码
	function checkGroupCode(){
		var groupCode = document.getElementById("groupCode").value;
		if(groupCode==""){
			MyAlert("请先选择所属物料组！");
			return;
		}
	}
	// 返回到物料列表页面
	function goBack() {
		window.location.href = '<%=request.getContextPath()%>/sysproduct/productmanage/MaterialManage/materialManageQueryPre.do?curPage=<%=curPage %>';	
	}
</script>

</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>物料维护</title>
<style>#fm{height: 400px;overflow-y: auto;}</style>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 物料维护</div>
	<span style="color: red">提示：灰色背景表示不可以修改项！</span>
	<form method="POST" name="fm" id="fm">
	<input type="hidden" id="oldColorCode" value="${po.colorCode}">
	<input type="hidden" id="flag" value="colorflag">
		<table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query table_list">
  		<!--<tr style="display:none">
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">ERP名称：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap">
		    	<label>${po.erpName}</label>
		    	<input name="erpName" datatype="1,is_null,300" id="erpName_" type="text" class="middle_txt" disabled="disabled" value="${po.erpName}"/>
		    </td>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap"></td>
		    <td class="table_query_2Col_input" nowrap="nowrap"></td>
	  	</tr>-->
		<tr>
		    <td class="right">物料代码：</td>
		    <td>
		    	<input name="materialCode"  id="materialCode" style="background-color: #CCCCCC" type="text" class="middle_txt" value="${po.materialCode}"/>
		    </td>
		    <td class="right">物料名称：</td>
		    <td>
		    	<input name="materialName" datatype="0,is_null,300" id="materialName" type="text" class="middle_txt" value="${po.materialName}"/>
		    </td>
	  	</tr>
		<!-- <tr>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">车型6位码：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap">
		    	<input name="modelCode" datatype="1,is_null,30" id="modelCode" type="text" class="middle_txt" value="${po.modelCode}"/>
		    </td>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">内饰代码：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
		    	<input name="trimCode"  id="trimCode" type="text" class="middle_txt" value="${trimCode}"/>
		    </td>
	  	</tr
		<tr>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">年型：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap">
		    	<input name="modelYear" datatype="1,is_null,4" id="modelYear" type="text" class="middle_txt" value="${po.modelYear}"/>
		    </td>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">上市日期：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
				<input class="middle_txt"  type="text" id="issueDate" name="issueDate" datatype="1,is_date,10" value="${issueDate}"/>
				<input class="time_ico" type="button" value="&nbsp;" />
		    </td>
	  	</tr>
		<tr style="display:none">
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">生效日期：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap">
				<input class="short_txt"  type="text" id="enableDate" name="enableDate" value="${enableDate}"/>
				<input class="time_ico" type="button" value="&nbsp;" />
		    </td>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">失效日期：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
				<input class="short_txt"  type="text" id="disableDate" name="disableDate" value="${disableDate}"/>
				<input class="time_ico" type="button" value="&nbsp;" />
		    </td>
	  	</tr>>-->
		<tr>
		    <td class="right">物料状态：</td>
		    <td>
		    	<script type="text/javascript">
			      	genSelBoxExp("status",<%=Constant.STATUS%>,"${po.status}",false,"u-select","","false",'');
			    </script>
		    </td>
		    <td class="right">生产状态：</td>
		    <td>
				<script type="text/javascript">
			      	genSelBoxExp("procuctFlag",<%=Constant.FORECAST_FLAG_REPORT_PRO%>,"${po.procuctFlag}",false,"u-select","","false",'');
			    </script>
		    </td>
		   
	  	</tr>
	  	<tr>    
		    <td class="right">可提报订单：</td>
		    <td>
				<script type="text/javascript">
					var orderFlag = "${po.orderFlag}" ;
					
					if(orderFlag == 0) {
						orderFlag = <%=Constant.NASTY_ORDER_REPORT_TYPE_02%> ;
					}
					
			      	genSelBoxExp("orderFlag",<%=Constant.NASTY_ORDER_REPORT_TYPE%>,orderFlag,false,"u-select","","false",'');
			    </script>
		    </td>
		    <td class="right">所属物料组：</td>
		    <td>
		    	<input type="text" class="middle_txt"  name="groupCode" id="groupCode" readonly="readonly" style="background-color: #CCCCCC" value="${gpo.groupCode}"/>
				<!-- <input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','false','4','true')" value="..." />
				<input class="mark_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/> -->
		    </td>
	  	</tr>
	  	 <!-- <tr>
	  		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">内部型号(老)：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
				<input name="erpPackage" maxlength="10" id="erpPackage" type="text" class="middle_txt" value="${po.erpPackage}" datatype="0,is_null,8"/>
		    </td>
		     <td class="table_query_2Col_label_6Letter" nowrap="nowrap">装配状态代码：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
		    	<input name="ERPName2" datatype="0,is_null,300" id="ERPName2" type="text" class="middle_txt" value="${po.erpName}"/>
		    </td>
		   
	  	</tr> -->
	  	
	  	<tr>
		    <td class="right" >颜色代码：</td>
		    <td>
		       <input name="colorCode"  id="colorCode" type="text" readonly style="background-color: #CCCCCC" class="middle_txt" value="${po.colorCode}"/>
		    </td>
		    <td class="right">颜色名称：</td>
		    <td>
		    	<input name="colorName"  id="colorName" type="text" readonly style="background-color: #CCCCCC" class="middle_txt" value="${po.colorName}"/>
		    </td>
	  	</tr>
	  	<c:if test="${isFlag == 0 }">
	  	<tr style="display:none">    
		    <td class="right">是否可提报常规订单：</td>
		    <td>
				<script type="text/javascript">
					var orderFlag = "${po.orderFlag}" ;
					
					if(orderFlag == 0) {
						orderFlag = <%=Constant.NASTY_ORDER_REPORT_TYPE_02%> ;
					}
					
			      	genSelBoxExp("normalOrderFlag",<%=Constant.IF_TYPE%>,${normalOrderFlag},false,"short_sel u-select","","false",'');
			    </script>
		    </td>
		    <td class="right"></td>
		    <td>
		    </td>
	  	</tr>
	  	</c:if>
	  	<!-- <tr>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">物料类型：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
		    	<script type="text/javascript">
			      	genSelBoxExp("mat_type",<%=Constant.MAT_TYPE%>,"${po.matType}",false,"short_sel u-select","","false",'');
			    </script>
		    </td>
		     <td class="table_query_2Col_label_6Letter" nowrap="nowrap">是否出口：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
		    	<script type="text/javascript">
			      	genSelBoxExp("is_export",<%=Constant.IF_TYPE%>,${po.exportSalesFlag},false,"min_sel u-select","","false",'');
			    </script>
		    </td>
	  	</tr>
	  	<tr>
		    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">是否内销：</td>
		   <td class="table_query_2Col_input" nowrap="nowrap" colspan="3">
		    	<script type="text/javascript">
			      	genSelBoxExp("isInsale",<%=Constant.IF_TYPE%>,${po.isInsale},false,"min_sel u-select","","false",'');
			    </script>
		    </td>
	  	</tr> -->
	  	<tr>
	  	    <td class="right">配置说明：</td>
		    <td align="left">
		     <textarea class="remark align" rows="3" cols="50" name="remark2">${po.remark2 }</textarea>	     
		    </td>
		    <td class="right">上市日期：</td>
		    <td>
		    	<input name="issueDate" id="issueDate" value="${issueDate}" type="text" class="middle_txt" datatype="0,is_null,10"onFocus="WdatePicker({el:$dp.$('issueDate')})"  style="cursor: pointer;width: 80px;"/>
		    </td>
	  	</tr>
	  	<tr>
		    <td class="center" colspan="4" align="center">
		    	<input name="materialId" type="hidden" value="${po.materialId}">
		      	<input name="button2" id="button2" type="button" class="u-button" onclick="confirmAdd();" value="保存"/>
		      	<input name="button"  id="button" type="button" class="u-button" onclick="javascript:goBack();" value="返回"/>
		    </td>
	  	</tr>
	</table>
</form>
</div>	

<script type="text/javascript">
	var isSave = '0';
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
		document.getElementById("button2").disabled = true ;
		makeNomalFormCall('<%=request.getContextPath()%>/sysproduct/productmanage/MaterialManage/materialManageMod.json',showResult,'fm');
	}
	
	function showResult(json){
		if(json.returnValue == '1'){
			MyAlert('保存成功!');
			//_hide();
			//parentContainer.queryttt();
			goBack();
		}else{
			MyDivAlert("编辑失败！请联系系统管理员！");
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
		var oldColorCode = document.getElementById("oldColorCode").value;
		if(json.colorList.length>0){
			for(var i=0;i<json.colorList.length;i++){
				var opp = new Option(json.colorList[i].COLOR_CODE,json.colorList[i].COLOR_CODE+"_"+json.colorList[i].COLOR_NAME);
				if(json.colorList[i].COLOR_CODE==oldColorCode){
					opp.selected = "selected";
					document.getElementById("colorName").value = json.colorList[i].COLOR_NAME;
				}
				objSelectNow.add(opp);
			}
		}else{
			objSelectNow.options.length=0;
			var opp = new Option("-请选择-","");
			objSelectNow.add(opp);
			document.getElementById("colorName").value = '';
		}
		
	}
	
	//单击颜色编码下拉框动态给颜色名称赋值
	function changeColor(){
		var colorCodeAndName = document.getElementById("colorCode").value;
		var colorName = colorCodeAndName.split("_")[1];
		if(typeof(colorName)=="undefined"){
			document.getElementById("colorName").value = '';
		}else{
		   document.getElementById("colorName").value = colorName; 
		}
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
		window.location.href = '<%=request.getContextPath()%>/sysproduct/productmanage/MaterialManage/materialManageQueryPre.do';	
	}
</script>
</body>
</html>

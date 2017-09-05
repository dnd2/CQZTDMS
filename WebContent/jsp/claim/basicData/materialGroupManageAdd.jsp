<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>物料组维护</title>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：售后服务管理>索赔基础数据>售后物料组维护</div>
<form method="POST" name="fm" id="fm">
<div class="form-panel">
<h2>基本信息</h2>
			<div class="form-body">
  	<table class="table_query" border="0">
		<tr>
	    <td class="table_query_2Col_label_8Letter" style="text-align:right" nowrap="nowrap">物料组代码：</td>
	    <td class="table_query_4Col_input" nowrap="nowrap">
	    	<input name="groupCode" datatype="0,is_null,30" id="groupCode" type="text" class="middle_txt" />
	    </td>
	    <td class="table_query_2Col_label_8Letter" style="text-align:right" nowrap="nowrap">物料组名称：</td>
	    <td class="table_query_2Col_input" nowrap="nowrap">
	    	<input name="groupName" datatype="0,is_null,30" id="groupCode" type="text" class="middle_txt" />
	    </td>
	  </tr>
	  <tr>
	    <td class="table_query_2Col_label_8Letter" style="text-align:right" nowrap="nowrap">上级物料组：</td>
	    <td class="table_query_2Col_input" nowrap="nowrap">
	    	<input type="text" class="middle_txt" name="parentGroupCode" id="parentGroupCode" readonly="readonly" onfocus="showMaterialGroup('parentGroupCode','','false','','true')" onpropertychange="getModelGroup(this.value);"/>
			<!-- <input name="button3" type="button" class="normal_btn" onclick="showMaterialGroup('parentGroupCode','','false','','true')" value="..." /> -->
			<input class="normal_btn" type="button" value="清空" onclick="clrTxt('parentGroupCode');"/>
	    </td>
	    <td class="table_query_2Col_label_8Letter" style="text-align:right" nowrap="nowrap">物料状态：</td>
	    <td class="table_query_2Col_input" nowrap="nowrap">
	    	<script type="text/javascript">
		      	genSelBoxExp("status",<%=Constant.STATUS%>,"",false,"","","false",'');
		    </script>
	    </td>
	  </tr>
	  <tr>
	  <td class="table_query_2Col_label_8Letter" style="text-align:right" nowrap="nowrap">是否在产：</td>
	    <td class="table_query_2Col_input" nowrap="nowrap">
	    	<script type="text/javascript">
		      	genSelBoxExp("ifType",<%=Constant.IF_TYPE%>,"",false,"","","false",'');
		    </script>
	    </td>
	  </tr>
	  
	  <tr id="trObj" style="display:none">
	    <td class="table_query_2Col_label_8Letter" style="text-align:right" nowrap="nowrap">售后车型组：</td>
	    <td class="table_query_2Col_input" nowrap="nowrap">
	    	<select name="modelGroup1" id="modelGroup1">
	    		<c:forEach items="${groups1}" var="po">
					<option value="${po.WRGROUP_ID}">${po.WRGROUP_NAME}</option>
				</c:forEach>
	        </select><font color="red">*</font>
	    </td>
	  </tr>
	  
	  
	  <tr>
	    <td colspan="4" style="text-align:center">
	      <input id="id1" name="button2" type="button" class="normal_btn" onclick="confirmAdd();" value="保存"/>
	      <input id="id2" name="button" type="button" class="normal_btn" onclick="history.back();" value="返回"/>
	    </td>
	  </tr>
	</table>
	</div>
	</div>
</form>
</div>
<script type="text/javascript">
	function confirmAdd(){
		var modelGroup1 = document.getElementById("modelGroup1").value ;
		if(!modelGroup1) {
			MyAlert("请选择售后车型组!") ;
			return false ;
		}
		
		
		if(submitForm('fm')){
			MyConfirm("是否确认保存?",addSave);
		}
	}
	
	function addSave(){
		document.getElementById("id1").disabled = true;
	    document.getElementById("id2").disabled = true;
		makeNomalFormCall('<%=request.getContextPath()%>/claim/basicData/ClaimModelMain/materialGroupManageAdd.json',showResult,'fm');
	}
	
	function showResult(json){
		if(json.returnValue == '1'){
		MyAlert("新增成功！");
			window.location.href = '<%=request.getContextPath()%>/claim/basicData/ClaimModelMain/materialGroupManageQueryPre.do';
		}else{
			document.getElementById("id1").disabled = '';
		    document.getElementById("id2").disabled = '';
			MyAlert("新增失败！请联系系统管理员！");
		}
	}
	
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    	getModelGroup('clear');
    }
    
    function getModelGroup(arg){	
    	if (event.propertyName == "value"){
    		var url = "<%=request.getContextPath()%>/claim/basicData/ClaimModelMain/getModelGroup.json";
			makeCall(url,showModelGroup,{groupCode:arg}); 
    	}	
	}
	
	function showModelGroup(json){
		var trObj = document.getElementById("trObj");
		if(json.returnValue == '1'){
			trObj.style.display = "inline";
		}
		else{
			trObj.style.display = "none";
		}
	}
	
	//动态增加颜色表格的行
	function addColor(){
		var newRow = document.getElementById("tbody1").insertRow();
    	newRow.className  = "table_list_row2";
    	// 颜色编码
    	newCell = newRow.insertCell();
    	newCell.innerHTML = "<input id='"+dynamicCreateId()+"' name='colorCode' type='text'/>&nbsp;<font style='color:red'>*</font>";
    	
    	// 颜色名称
    	newCell = newRow.insertCell();
    	newCell.innerHTML = "<input id='"+dynamicCreateId()+"' name='colorName' type='text'/>&nbsp;<font style='color:red'>*</font>";
    	
    	// 操作
    	newCell = newRow.insertCell();
    	newCell.innerHTML ="<a href='javascript:void(0)' onclick='deleteColorRow(this);'>删除</a>";
    	
	}
	
	//删除表格的某一行
	function deleteColorRow(obj){
		document.getElementById("tbody1").deleteRow(obj.parentElement.parentElement.rowIndex - 2);
	}
	
	//验证颜色编码是否重复输入
	function checkoutColorCode(obj){
		var colorCode = document.getElementsByName("colorCode") ;
		if(obj.value!=""){
			for(var i=0;i<colorCode.length;i++){
				if(colorCode[i].value!=""&&colorCode[i].id!=obj.id){
					if(colorCode[i].value==obj.value){
						MyAlert("您有颜色编码重复输入，请检查!");
						obj.value = '';
						obj.focus();
					}
				}
			}	
		}
	}
	
	//验证颜色名称是否重复输入
    function checkoutColorName(obj){
    	var colorName = document.getElementsByName("colorName") ;
    	if(obj.value!=""){
	    	for(var i=0;i<colorName.length;i++){
				if(colorName[i].value!=""&&colorName[i].id!=obj.id){
					if(colorName[i].value==obj.value){
						MyAlert("您有颜色名称重复输入，请检查!");
						obj.value = '';
						obj.focus();
					}
				}
			}
        }
	}
    
	//动态生成ID
    function dynamicCreateId(){
    	var idDate = new Date().getMilliseconds()+'';
    	var Max = 100;
    	var Min = 999;
    	var Range = Max - Min;   
    	var Rand = Math.random();   
    	var idRandom = (Min + Math.round(Rand * Range)); 
    	var id = idDate+idRandom;
    	return id;
    }
</script>
</body>
</html>

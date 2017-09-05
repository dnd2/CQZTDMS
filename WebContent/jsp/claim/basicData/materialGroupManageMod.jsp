<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>物料组维护</title>
</head>
<body onload="showOrHiddenColorTable();">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：售后服务管理>索赔基础数据>售后物料组维护</div>
<form method="POST" name="fm" id="fm">
<div class="form-panel">
<h2>基本信息</h2>
			<div class="form-body">
<input type=hidden name='groupCodeArg' id='groupCodeArg' value='${groupCode }'/>
<input type=hidden name='groupNameArg' id='groupNameArg' value='${groupName }'/>
<input type=hidden name='statusArg' id='statusArg' value='${status }'/>
<input type=hidden name='parentGroupCodeArg' id='parentGroupCodeArg' value='${parentGroupCode }'/>
<input type=hidden name='curPage' id='curPage' value='${page }'/>
<input type=hidden id="groupLevel" value="${po.groupLevel}">
  	<table class="table_query" border="0">
		<tr>
		    <td class="table_query_2Col_label_8Letter" style="text-align:right" nowrap="nowrap">物料组代码：</td>
		    <td class="table_query_4Col_input" nowrap="nowrap">
		    	<input name="groupCode" datatype="0,is_null,30" id="groupCode" type="text" class="middle_txt" value="${po.groupCode}"/>
		    </td>
		    <td class="table_query_2Col_label_8Letter" style="text-align:right" nowrap="nowrap">物料组名称：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
		    	<input name="groupName" datatype="0,is_null,30" id="groupCode" type="text" class="middle_txt" value="${po.groupName}"/>
		    </td>
		</tr>
		<tr>
		    <td class="table_query_2Col_label_8Letter" style="text-align:right" nowrap="nowrap">上级物料组：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
		    	<input type="text" class="middle_txt" name="parentGroupCode" id="parentGroupCode" readonly="readonly" value="${po.groupLevel == 1 ? '' : parent.groupCode}" onfocus="showMaterialGroup('parentGroupCode','','false','','true')" onpropertychange="getModelGroup(this.value);"/>
				<!-- <input name="button3" type="button" class="normal_btn" onclick="showMaterialGroup('parentGroupCode','','false','','true')" value="..." /> -->
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('parentGroupCode');"/>
		    </td>
		    <td class="table_query_2Col_label_8Letter" style="text-align:right" nowrap="nowrap">物料状态：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap">
		    	<script type="text/javascript">
			      	genSelBoxExp("status_1",<%=Constant.STATUS%>,"${po.status}",false,"","","false",'');
			    </script>
			     <input name="status" type="hidden" value="${po.status}" id="status" />
		    </td>
		</tr>
		<tr>
		  <td class="table_query_2Col_label_8Letter" style="text-align:right" nowrap="nowrap">是否在产：</td>
		    <td class="table_query_2Col_input" nowrap="nowrap" colspan="3">
		    <c:if  test="${po.forcastFlag==1}">
		    	<script type="text/javascript">
			      	genSelBoxExp("ifType_1",<%=Constant.IF_TYPE%>,"<%=Constant.IF_TYPE_YES%>",false,"","","false",'');
			    </script>
			    <input name="ifType" type="hidden" value="<%=Constant.IF_TYPE_YES%>" id="ifType" />
			    </c:if>
			     <c:if  test="${po.forcastFlag==0}">
		    	<script type="text/javascript">
			      	genSelBoxExp("ifType_1",<%=Constant.IF_TYPE%>,"<%=Constant.IF_TYPE_NO%>",false,"","","false",'');
			    </script>
			    <input name="ifType" type="hidden" value="<%=Constant.IF_TYPE_NO%>" id="ifType" />
			    </c:if>
		    </td>
		</tr>
		<div style="display:${po.groupLevel == 4 ? 'inline': 'none'}">
			<tr id="trObj" >
			    <td class="table_query_2Col_label_8Letter" style="text-align:right" nowrap="nowrap">售后车型组：</td>
			    <td class="table_query_2Col_input" nowrap="nowrap" colspan="3">
			    	<select name="modelGroup1" id="modelGroup1" class="u-select">
			    		<c:forEach items="${groups1}" var="po">
							<option value="${po.WRGROUP_ID}">${po.WRGROUP_NAME}</option>
						</c:forEach>
			        </select>&nbsp;<font style="color:red">*</font>
			    </td>
			  </tr>
		  </div>
		 <tr >
		    <td colspan="4"  style="text-align:center">
		    	<input type="hidden" name="groupId"  value="${po.groupId}">
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
	function doInit(){
		if(${po.groupLevel == 4}){
			document.getElementById("modelGroup1").value = '${group1.WRGROUP_ID}';
		}
	}
	$('ifType_1').disabled=true;
	$('status_1').disabled=true;
	function confirmAdd(){
	
		if(submitForm('fm')){
			var modelGroup1 = document.getElementById("modelGroup1").value ;
			if(!modelGroup1) {
				MyAlert("请选择售后车型组!") ;
				return false ;
			}
			
			/* if(!modelGroup2) {
				MyAlert("请选择销售车型组!") ;
				
				return false ;
			} */
			
			
			MyConfirm("是否确认保存?",addSave);
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
	
	function addSave(){
		document.getElementById("id1").disabled = true;
	    document.getElementById("id2").disabled = true;
		makeNomalFormCall('<%=request.getContextPath()%>/claim/basicData/ClaimModelMain/materialGroupManageMod.json',showResult,'fm');
	}
	
	function showResult(json){
		if(json.returnValue == '1'){
		MyAlert("修改成功!");
			var groupCodeArg = $('groupCodeArg').value;
			var groupNameArg = encodeURI(encodeURI($('groupNameArg').value));
			var statusArg = $('statusArg').value;
			var parentGroupCodeArg = $('parentGroupCodeArg').value;
			var curPage = $('curPage').value;
			window.location.href='<%=request.getContextPath()%>/claim/basicData/ClaimModelMain/materialGroupManageQueryPre.do?groupCodeArg='+groupCodeArg+'&groupNameArg='+groupNameArg+'&statusArg='+statusArg+'&parentGroupCodeArg='+parentGroupCodeArg+'&curPage='+curPage;
		
			//window.location.href = '<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/materialGroupManageQueryPre.do';
		}else{
			document.getElementById('id1').disabled = '';
		    document.getElementById('id2').disabled = '';
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
	function goBackToSearchByArg(){
			var groupCodeArg = $('groupCodeArg').value;
			var groupNameArg = encodeURI(encodeURI($('groupNameArg').value));
			var statusArg = $('statusArg').value;
			var parentGroupCodeArg = $('parentGroupCodeArg').value;
			var curPage = $('curPage').value;
			window.location.href='<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/materialGroupManageQueryPre.do?groupCodeArg='+groupCodeArg+'&groupNameArg='+groupNameArg+'&statusArg='+statusArg+'&parentGroupCodeArg='+parentGroupCodeArg+'&curPage='+curPage;
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
    
    //由物料组等级判断colorTable在加载页面时显示还是yincang
    function showOrHiddenColorTable(){
    	var groupLevel = document.getElementById("groupLevel").value;
    	if(groupLevel==2){
    		document.getElementById("colorTable").style.display = 'block';
    	}else{
    		document.getElementById("colorTable").style.display = 'none';
    	}
    	
    	
    }
</script>
</body>
</html>

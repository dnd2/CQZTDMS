<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%
	String contextPath = request.getContextPath();
	//授权级别列表
	List levellist = (List)request.getAttribute("LEVELLIST");
	//规则明细初始值
	List items = (List)request.getAttribute("TT_AUTH_ITEM_LIST");  
	//授权角色初始值
	List ruleList = (List)request.getAttribute("TT_AUTH_RULE_LIST");
	HashMap temp = (HashMap)ruleList.get(0);
	String roleLevel = temp.get("ROLE").toString(); //授权角色
	String ruleId = temp.get("RULE_ELEMENT").toString();//授权规则id
	String priorLevel = temp.get("PRIOR_LEVEL").toString();//授权规则明细条件
	//原授权角色
	HashMap map = (HashMap)request.getAttribute("SELMAP");
		
	String[] boolComp = {"AND","OR"};//授权关系下拉框
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>修改索赔授权规则</title> 
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script type="text/javascript">
	//去掉字符串中的空格：
	function trim(str){
	    return str.replace(/(^\s*)|(\s*$)/g, "");
	}
	function codeTodesc(str){
		var arr = str.split(",");
		var le = arr.length;
		var desc = "";
		for(var i=0;i<le;i++){
			var des = getItemValue(trim(arr[i]));		
			if(i==0){
				desc = des;
			}else{
				desc += "," + des;
			}
		}
		return desc;
	}
	
	//验证是否是数字：	
	function isUnsignedInteger(obj) {
		var reg=/^\d+$/ ;
		if(reg.test(obj.value)){
			return true;
		}else{
			return false;
		}
	}	
</script>  
</head>
<body onload="javascript:onSelectAll(fm.checkboxnone,0);assembleItem();">
<div class="wbox">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;授权规则维护</div>
<form name='fm' id='fm'>
<input type="hidden" name="wrmodelgrouplist" id="wrmodelgrouplist" value="<%=request.getAttribute("wrmodelgrouplist")%>"/>
<input id="LABOUR_OPERATION_NAME" name="LABOUR_OPERATION_NAME" type="hidden" />
<input type="hidden" name="WRGROUP_ID" id="WRGROUP_ID"  />
<div class="form-panel">
				<h2>授权角色</h2>
					<div class="form-body">
  <table class="table_list">
	    <tr>
	    	<td style="display: none"> <input type="checkbox" name="checkboxnone" onClick="onSelectAll(this,1);" value="100" <%if(roleLevel.equals("100"))out.print("checked"); %>/>
				<span align="left"> <strong>自动拒绝</strong>(符合本条规则的索赔申请将自动被系统拒绝)</span>
	    	</td>
	    </tr>
	  <% for(int i=0;i<levellist.size();i++){ 
		  HashMap tempLevel = (HashMap)levellist.get(i);
		  boolean flag = false;
		  if(tempLevel.get("APPROVAL_LEVEL_CODE").equals(map.get(tempLevel.get("APPROVAL_LEVEL_CODE")))){
			  flag = true;
		  }
	  %>
	  <%if(i%3==0){%><tr><%}%>
			<td><input type="checkbox" name="AUTHROLECHK"  value="<%=tempLevel.get("APPROVAL_LEVEL_CODE")%>" <%if(flag)out.print("checked"); %> />
			<%=tempLevel.get("APPROVAL_LEVEL_NAME")%></td>
	   <%if(i%3==2){%></tr><%}%> 	
	  <%} %>
  </table>
  </div>
  </div>
  <div class="form-panel">
				<h2>授权条件</h2>
					<div class="form-body">		
 	<table class="table_list"  >
      <tbody id="editRule">
      <th>授权关系</th>
      <th>授权项</th>
      <th>比较符</th>
      <th>值</th>
      <th>检查顺序</th>
      <th>操作</th>
      
      
	<%	
		for (int i=0; i<items.size(); i++){
			HashMap tempItem = (HashMap)items.get(i); 
			String className = (i%2)==0?"table_list_row2":"table_list_row1";
	%>
    <tr class="<%=className%>">
      <td align="center">
	  <%if (0 == i){%>
			--
	  <%}else{%>
			<select name="BOOLEAN_COMPARISION" class="u-select" style="width:5px">
		<%
			for(int j=0; j<boolComp.length; j++){
				if (tempItem.get("BOOLEAN_COMPARISON").toString().trim().equals(boolComp[j])){
		%>
					<option value="<%= boolComp[j]%>" selected><%= boolComp[j]%></option>
		<%		}else{%>
					<option value="<%= boolComp[j]%>"><%= boolComp[j]%></option>
		<%		}
			}%>
			</select>
		<%}%>
	  </td>
      <td align="center">
	  	<input type="hidden" name="ELEMENT_ID" value="<%= tempItem.get("ELEMENT_NO")%>"/>
		<input type="hidden" name="ELEMENT_DESC" value="<%= tempItem.get("ELEMENT_DESC")%>"/>
		<%= tempItem.get("ELEMENT_DESC")%>
	  </td>
      <td align="center">
<%		if (("是否特殊质保车辆".equals(tempItem.get("ELEMENT_DESC").toString().trim())) || 
		("车型代码".equals(tempItem.get("ELEMENT_DESC").toString().trim())) ||  
		("维修操作代码".equals(tempItem.get("ELEMENT_DESC").toString().trim())) || 
		("索赔类型".equals(tempItem.get("ELEMENT_DESC").toString().trim())) || 
		("经销商代码".equals(tempItem.get("ELEMENT_DESC").toString().trim())) ||
		("车型大类".equals(tempItem.get("ELEMENT_DESC").toString().trim())) ||
		("配件代码".equals(tempItem.get("ELEMENT_DESC").toString().trim())) ||
		("产地".equals(tempItem.get("ELEMENT_DESC").toString().trim()))){
%>
		<script type="text/javascript">
		genSelBoxExp("COMPARISON_OP",<%=Constant.LOGIC_TYPE%>,<%=tempItem.get("COMPARISON_OP").toString().trim()%>,false,"","","true",'');
		</script>
<%		}else{
%>
		<script type="text/javascript">
			genSelBoxExp("COMPARISON_OP",<%=Constant.COMP_TYPE%>,<%=tempItem.get("COMPARISON_OP").toString().trim()%>,false,"","","true",'');
		</script>
<%		}
%>
	  </td>
      <td align="center">
      <input name="ELEMENT_CODE" id="ELEMENT_CODE_<%=i%>" type="hidden" value="<%= tempItem.get("ELEMENT_VALUE").toString().trim()%>"/>
<%   if("车型代码".equals(tempItem.get("ELEMENT_DESC").toString().trim())){ 
%>       
      <input name="ELEMENT_VALUE" class="short2_txt" id="ELEMENT_VALUE_<%=i%>" type="text" readonly="true" value="<%= tempItem.get("ELEMENT_VALUE")%>"/>
      <a href="#" onclick="showMaterialGroup('ELEMENT_VALUE_<%=i%>','','false','3','true')" style="cursor: pointer;">选择</a>
<%}else if("经销商代码".equals(tempItem.get("ELEMENT_DESC").toString().trim())){ %>
	  <input name="ELEMENT_VALUE" class="short2_txt" id="ELEMENT_VALUE_<%=i%>" type="text" readonly="true" value="<%= tempItem.get("ELEMENT_VALUE")%>"/>
	  <a href="#" onclick="showOrgDealer('ELEMENT_VALUE_<%=i%>','','false')"  style="cursor: pointer;">选择</a>
<%}else if("索赔类型".equals(tempItem.get("ELEMENT_DESC").toString().trim())){ %>
	<input name="ELEMENT_VALUE" class="short2_txt" id="ELEMENT_VALUE_<%=i%>" type="text" readonly="true"/>
	<script type="text/javascript">
	<!--
		document.getElementById('ELEMENT_VALUE_<%=i%>').value = codeTodesc('<%= tempItem.get("ELEMENT_VALUE").toString().trim()%>');
	//-->
	</script>
	<a href="#" onclick="selClaimType('<%=i%>')"  style="cursor: pointer;">选择</a>
<%}else if("车型大类".equals(tempItem.get("ELEMENT_DESC").toString().trim())){ %>
	<input name="ELEMENT_VALUE" class="short2_txt" id="ELEMENT_VALUE_<%=i%>" type="text" readonly="true"/>
	<script type="text/javascript">
	<!--
		document.getElementById('ELEMENT_VALUE_<%=i%>').value = '<%= tempItem.get("ELEMENT_VALUE").toString().trim()%>';
	//-->
	</script>
	<a href="#" onclick="showLabor('<%=i%>')"  style="cursor: pointer;">选择</a>
<%}else{ %>
	<input name="ELEMENT_VALUE" class="middle_txt"  type="text" value="<%= tempItem.get("ELEMENT_VALUE")%>"/>	
<%} %>     
      </td>
	  <td align="center"> <input name="ELEMENT_POSITION" type="text" value="<%= tempItem.get("ELEMENT_POSITION")%>" class="middle_txt" style="width:5px" onchange="javascript:positionChange(this)"/></td>
	  <!-- <td align="center"><input type=button value="移除" class="normal_btn" name="remain"  onclick="javascript:delItem(this)"/></td> -->
	  <td align="center"><a class="u-anchor" ame="remain"  onclick="javascript:delItem(this);">移除</a></td>
    </tr>
    <%}%>      
      
      	
      </tbody>   
	</table>
<table class="table_edit">
  <tbody id="reasonReview">
    <tr>
      <td align="center"></td>
      <td align="center">
      	<input class="normal_btn" type="button" name="view" value="预览" style="cursor: pointer;"  onclick="javascript:assembleItem();"/>
      </td>
    </tr>
  </tbody>
</table>
</div>
</div>
<div class="form-panel">
				<h2>授权项目</h2>
					<div class="form-body">		
<table class="table_query">
	<tr>
		<td style="text-align:right">授权项：</td>
		<td>
		     <script type="text/javascript">
	              genSelBoxExp("CHOOSE_ELEMENT",<%=Constant.CLAIM_AUTH_TYPE%>,"",false,"","","false",'');
	         </script>
	         <input class="normal_btn" type="button" name="add"  value="增加"  onclick="javascript:addItem();"/>
		</td>
	</tr>
	
	<tr> 
     	<td colspan="2" style="text-align:center">
     	<input type="hidden" name="AUTHROLE"/> <!-- 授权角色隐藏域 -->
     	<input type="hidden" name="RULEID" value="<%=ruleId%>"/> <!-- 待修改的授权规则ID -->
     	<input type="hidden" name="PRIOR_LEVEL" value="<%=priorLevel%>"/> <!-- 待修改的授权规则ID -->
        <input name="ok" type="button" class="normal_btn" id="commitBtn"  value="完成"  onclick="checkFormUpdate();"/>
        <input name="back" type="button" class="normal_btn" value="取消"  onclick="_hide() ;"/>
        </td>
    </tr>	
</table>
			  </div>
			  </div>
	</form>
	</div>
<script>
//选择自动拒绝后，前6个级别变灰不能选择 
function onSelectAll(obj,num){
	if(num == 1){
		if(obj.checked == true){
			for(j=0;j<fm.AUTHROLECHK.length;j++){
				fm.AUTHROLECHK[j].checked = false;
				fm.AUTHROLECHK[j].disabled =true;
			}
		}
		else{
			for(i=0;i<fm.AUTHROLECHK.length;i++){
				fm.AUTHROLECHK[i].disabled =false;
			}	
		}
	}else{

	}
}

function checkValue(desc,compare,objVal){
	if(desc.indexOf('索赔次数')!=-1){
		if(!(/^[1-9]{1}\d{0,59}$/.test(objVal)) ) {
			MyAlert('索赔次数只能为整数');
			return false;
		}
	}
<%--	if ((0 > compare.indexOf('Begin')) && (0 > compare.indexOf('Equal')) && (0 > compare.indexOf('notBegin')) && (0 > compare.indexOf('notEqual'))){--%>
<%--		if( !(/^\d{1,58}(\.\d){0,1}$/.test(objVal)) ) {--%>
<%--			MyAlert(desc+"值输入错误,请输入数值型的Value！(最多带一位小数)");--%>
<%--			return false;--%>
<%--		}--%>
<%--	}else{--%>
<%--		if (!(/^.{1,60}$/.test(objVal))){--%>
<%--			MyAlert(desc+"值输入错误,长度过大！");--%>
<%--			return false;--%>
<%--		}--%>
<%--	}--%>
	return true;
}
//行号计数：
var rowlen;
//增加授权项
function addItem()
{
	isChanged = true;
	var reason = "";

	var objId = fm.CHOOSE_ELEMENT.options[fm.CHOOSE_ELEMENT.selectedIndex].value;
	var objDesc = fm.CHOOSE_ELEMENT.options[fm.CHOOSE_ELEMENT.selectedIndex].text;
	
	//判断id
	var i = editRule.rows.length;
	if(i==0){
		rowlen = 1;
	}else{
		rowlen++;
	}
	
	//授权关系：
	var td1= '<select name=\"BOOLEAN_COMPARISION\" class=\"u-select\" style=\"width:5px\"><%	for (int j=0; j<boolComp.length; j++){ %><option value=\"<%= boolComp[j] %>\"><%= boolComp[j] %></option><% } %></select>';
	//授权项：
	var td2 = '<input type=\"hidden\" name=\"ELEMENT_ID\" value=\"'+objId+'\"><input type=\"hidden\" name=\"ELEMENT_DESC\" value=\"'+objDesc+'\">'+objDesc;
	//比较符
	//授权项算术比较符
	var td3 = genSelBoxStrExp("COMPARISON_OP",<%=Constant.COMP_TYPE%>,"",false,"u-select","","true",'');
	//if ("是否特殊质保车辆" == objDesc || "车型代码" == objDesc || "车型大类" == objDesc  || "维修操作代码" == objDesc || "索赔类型" == objDesc || "经销商代码"==objDesc || "产地"==objDesc){
	//	//授权项逻辑比较符
	//	td3 = genSelBoxStrExp("COMPARISON_OP",<%=Constant.LOGIC_TYPE%>,"",false,"u-select","","true",'');
	//}
	//值
	var td4 = '<input name=\"ELEMENT_CODE\" id=\"ELEMENT_CODE_'+rowlen+'\" type=\"hidden\">';//用于显示索赔类型code
	//if("车型代码" == objDesc){
	//	var v = document.getElementById("WRGROUP_ID").value;
	//	td4 += '<input name=\"ELEMENT_VALUE\" id=\"ELEMENT_VALUE_'+rowlen+'\" type=\"text\" class=\"short2_txt\">';
	//	td4 +='<a href=\"#\" onclick=\"showMaterialGroup(\'ELEMENT_VALUE_'+rowlen+'\',\'\',\'false\',\'3\',\'true\')\" style=\"cursor: pointer;\">选择</a>';
	//}else if("经销商代码" == objDesc){
	//	td4 += '<input name=\"ELEMENT_VALUE\" id=\"ELEMENT_VALUE_'+rowlen+'\" type=\"text\" class=\"short2_txt\">';
	//	td4 +='<a href=\"#\" onclick=\"showOrgDealer(\'ELEMENT_VALUE_'+rowlen+'\',\'\',\'true\')\"  style=\"cursor: pointer;\">选择</a>';
	//}else if("车型大类" == objDesc){
	//	td4 += '<input name=\"ELEMENT_VALUE\" id=\"ELEMENT_VALUE_'+rowlen+'\" type=\"text\" readonly=\"true\" class=\"short2_txt\">';
	//	td4 +='<a href=\"#\" onclick=\"showLabor('+rowlen+')\" style=\"cursor: pointer;\">选择</a>';
	//}else if("索赔类型" == objDesc){
	//	td4 += '<input name=\"ELEMENT_VALUE\" id=\"ELEMENT_VALUE_'+rowlen+'\" type=\"text\" readonly=\"true\" class=\"short2_txt\">';
	//	td4 +='<a href=\"#\" onclick=\"selClaimType('+rowlen+')\" style=\"cursor: pointer;\">选择</a>';
	//}else{
		td4 +='<input name=\"ELEMENT_VALUE\" type=\"text\" class=\"middle_txt\">';
	//}
	//检查顺序
	var td5 = '<input name=\"ELEMENT_POSITION\" class=\"middle_txt\" style=\"width:5px\" datatype=\"1,is_digit,3\"  type=\"text\"  onchange=\"javascript:positionChange(this)\">';
	//操作：
	/* var td6 = '<input type=\"button\" value=\"移除\" class=\"normal_btn\" style=\"cursor: pointer;\" name=\"remain\" onclick=\"javascript:delItem(this)\">'; */
	var td6 = '<a class="u-anchor" ame="remain"  onclick="javascript:delItem(this);">移除</a>';
	

	var aTr = document.createElement("tr");	
	if(rowlen%2==0){
		aTr.className = "table_list_row2";//偶数行样式
	}else{
		aTr.className = "table_list_row1";//奇数行样式
	}
	editRule.appendChild(aTr);

	var aTD1 = document.createElement("td");
	var aTD2 = document.createElement("td");
	var aTD3 = document.createElement("td");
	var aTD4 = document.createElement("td");
	var aTD5 = document.createElement("td");
	var aTD6 = document.createElement("td");
	
	aTr.appendChild(aTD1);
	aTr.appendChild(aTD2);
	aTr.appendChild(aTD3);
	aTr.appendChild(aTD4);
	aTr.appendChild(aTD5);
	aTr.appendChild(aTD6);
	aTD1.innerHTML=td1;
	aTD1.align = "center";
	aTD2.innerHTML=td2;
	aTD2.align = "center";
	aTD3.innerHTML=td3;
	aTD3.align = "center";
	aTD4.innerHTML=td4;
	aTD4.align = "center";
	aTD5.innerHTML=td5;	
	aTD5.align = "center";
	aTD6.innerHTML=td6;	
	aTD6.align = "center";
}
//移除行
function delItem(obj)
{
	var i = obj.parentElement.parentElement.rowIndex;
	//行号是从0开始的
	editRule.deleteRow(i);
}
//预览方法：
function assembleItem(){
	var tmpReason = "";
	try{
		//判断授权项
		if (fm.ELEMENT_DESC.length > 0){
			len = fm.ELEMENT_DESC.length;
			for (i=0; i<len; i++){
				fm.ELEMENT_VALUE[i].value = trim(fm.ELEMENT_VALUE[i].value);
				//进行授权关系的字符拼接：
				if (0 < i){
					if (len > 2){
						tmpReason = tmpReason + " " + fm.BOOLEAN_COMPARISION[i-1].value;
					}else{
						tmpReason = tmpReason + " " + fm.BOOLEAN_COMPARISION.value;
					}
				}
				
				//授权项验证是否存在
				if ("" == fm.ELEMENT_VALUE[i].value){
					MyAlert("请输入值...");
					fm.ELEMENT_VALUE[i].focus();
					return false;
				}
				//检查关系验证
				if ("" == fm.ELEMENT_POSITION[i].value){
					MyAlert("请输入检查顺序...");
					fm.ELEMENT_POSITION[i].focus();
					return false;
				}
				else if(!isUnsignedInteger(fm.ELEMENT_POSITION[i])){
					MyAlert("检查顺序只能为整数");
					fm.ELEMENT_POSITION[i].focus();
					return false;
				}
				else if(fm.ELEMENT_POSITION[i].value >=len||fm.ELEMENT_POSITION[i].value<0){
					MyAlert("检查顺序应从0开始且不可以大于等于授权数量");
					fm.ELEMENT_POSITION[i].focus();
					return false;
				}
				else{
					for(var j=i+1;j<len;j++){
						if(fm.ELEMENT_POSITION[i].value==fm.ELEMENT_POSITION[j].value){
							MyAlert("检查顺序不能重复");
							fm.ELEMENT_POSITION[j].focus();
							return false;
						}
					}
				}
		
				if (false == checkValue(fm.ELEMENT_DESC[i].value,fm.COMPARISON_OP[i].value,fm.ELEMENT_VALUE[i].value)){
					return false;
				}
				
				//根据检查顺序拼字符
				for(var m=0;m<len;m++){
					if(fm.ELEMENT_POSITION[m].value==i){
						tmpReason = tmpReason + " " + fm.ELEMENT_DESC[m].value + " " + fm.COMPARISON_OP[m].options[fm.COMPARISON_OP[m].selectedIndex].text + " " + fm.ELEMENT_VALUE[m].value;
						break;
					}
				}
			}
		}else{
			fm.ELEMENT_VALUE.value = trim(fm.ELEMENT_VALUE.value);

			if (false == checkValue(fm.ELEMENT_DESC.value,fm.COMPARISON_OP.value,fm.ELEMENT_VALUE.value)){
				return false;
			}

			if ("" == fm.ELEMENT_VALUE.value){
				MyAlert("请输入值...");
				fm.ELEMENT_VALUE.focus();
				return false;
			}
			
			
			if ("" == fm.ELEMENT_POSITION.value){
				MyAlert("请输入检查顺序...");
				fm.ELEMENT_POSITION.focus();
				return false;
			}
			else if(!isUnsignedInteger(fm.ELEMENT_POSITION)){
				MyAlert("检查顺序只能为整数");
				fm.ELEMENT_POSITION.focus();
				return false;
			}
			else if(fm.ELEMENT_POSITION.value >=1||fm.ELEMENT_POSITION.value<0){
				MyAlert("检查顺序应从0开始且不可以大于等于授权数量");
				fm.ELEMENT_POSITION.focus();
				return false;
			}

			tmpReason = fm.ELEMENT_DESC.value + " " + fm.COMPARISON_OP.options[fm.COMPARISON_OP.selectedIndex].text + " " + fm.ELEMENT_VALUE.value;
		}
	}catch(e){
			tmpReason = "无";
	}
	//规则明显存储赋值
	fm.PRIOR_LEVEL.value = tmpReason;
	reasonReview.children[0].children[0].innerHTML = '<STRONG>规则预览：'+ tmpReason + '</STRONG>';
	return true;
}

function positionChange(obj){	
	var i = obj.parentElement.parentElement.rowIndex;
	if(obj.value == 0){
		editRule.children[i].children[0].innerHTML="---";
	}
	else if(obj.value != 0){
		editRule.children[i].children[0].innerHTML='<select name=\"BOOLEAN_COMPARISION\" size=\"1\"><%	for (int j=0; j<boolComp.length; j++){ %><option value=\"<%= boolComp[j] %>\"><%= boolComp[j] %></option><% } %></select>';
	}
}
/**
*车型代码弹出框：
*value1 : 车型组id
*r      : 对应的行号计数
*/
function selModel(r){
	var value1 = document.getElementById("WRGROUP_ID").value;
	OpenHtmlWindow('<%=contextPath%>/claim/authorization/RuleMain/ruleModelSelect.do?ID='+value1+'&r='+r,900,500);
}
/**
*车型大类选择页面：
* r    : 对应的行号计数
*/
function showLabor(r){
		
		OpenHtmlWindow('<%=contextPath%>/claim/authorization/RuleMain/modeTypeSelect.do?r='+r,900,500);
	}
/**
*索赔类型选择页面：
* r    : 对应的行号计数
*/
function selClaimType(r){
	OpenHtmlWindow('<%=contextPath%>/claim/authorization/RuleMain/claimTypeSelect.do?r='+r,900,500);
}
	//表单提交方法：
	function checkForm(){
			document.getElementById("commitBtn").disabled = true ;
			makeNomalFormCall('<%=contextPath%>/claim/authorization/RuleMain/ruleUpdate.json',showResult,'fm');			
	}
	function goBack(){
		history.go(-1);
	}
	function showResult(json){
		if(json.success != null && json.success == "true"){
		MyAlert("修改成功!");
		__parent().__extQuery__(1) ;
		_hide() ;
		}else{
			MyAlert("修改失败，请联系管理员！");
		}
	}
	//表单提交前的验证：
	function checkFormUpdate(){
		if(!submitForm('fm')) {
			return false;
		}
		
		var roles = "";
		if(fm.checkboxnone.checked){
			roles = fm.checkboxnone.value;
		}else{
			var len = fm.AUTHROLECHK.length;
			for (i=0; i<len; i++){
				if (fm.AUTHROLECHK[i].checked){
					roles = ("" == roles)? fm.AUTHROLECHK[i].value: (roles + "," + fm.AUTHROLECHK[i].value);
				}
			}
		}
		if(roles.length<1){
		  	MyAlert("您至少选择一种授权角色");
		  	return false;
		}
		//授权角色赋值：
		fm.AUTHROLE.value=roles;
		
		if (!assembleItem())
			return false;

		if ("<STRONG>规则预览：无</STRONG>" == reasonReview.children[0].children[0].innerHTML){
			MyAlert("规则元素不能为空");
			return false;
		}	
		MyConfirm("是否确认修改?",checkForm);
	}
function checkAll(obj){
	var len = fm.AUTHROLECHK.length;
			for (i=0; i<len; i++){
			if(fm.AUTHROLECHK[i].value<obj.value){
				fm.AUTHROLECHK[i].checked = obj.checked;
				}
			}
	}
</script>
</body>
</html>
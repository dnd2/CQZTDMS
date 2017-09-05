<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%
	String contextPath = request.getContextPath();
	List levellist = (List) request.getAttribute("LEVELLIST");

	String[] boolComp = { "AND", "OR" };//授权关系下拉框
%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<title>新增索赔授权规则</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
	<body>
<div class="wbox">

		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />
			&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;授权规则维护
		</div>
		<form name='fm' id='fm'>
			<input type="hidden" name="wrmodelgrouplist" id="wrmodelgrouplist"
				value="<%=request.getAttribute("wrmodelgrouplist")%>" />
			<input id="PRIOR_LEVEL" name="PRIOR_LEVEL" type="hidden" />
			<input type="hidden" name="WRGROUP_ID" id="WRGROUP_ID"  />	
			<div class="form-panel">
				<h2>授权角色</h2>
					<div class="form-body">		
			<table class="table_list">
				
				<tr>
					<%-- <td class="table_add_3Col_label_5Letter"
						rowspan="<%=levellist.size() / 3 + 2%>">
						授权角色：
					</td> --%>
					<td style="display: none">
						<input type="checkbox" name="checkboxnone"
							onclick="onSelectAll(this,1);" value="100" />
						<span align="left"> <strong>自动拒绝</strong>(符合本条规则的索赔申请将自动被系统拒绝)</span>
					</td>
				</tr>
				<%
						for (int i = 0; i < levellist.size(); i++) {
						HashMap temp = (HashMap) levellist.get(i);
				%>
				<%
				if (i % 3 == 0) {
				%>
				<tr>
					<%
					}
					%>
					<td>
						<input type="checkbox" name="AUTHROLECHK"   value="<%=temp.get("APPROVAL_LEVEL_CODE")%>"  />
						<%=temp.get("APPROVAL_LEVEL_NAME")%>
					</td>
					<%
					if (i % 3 == 2) {
					%>
				</tr>
				<%
				}
				%>
				<%
				}
				%>
			</table>
			</div>
			</div>
			<div class="form-panel">
				<h2>授权条件</h2>
					<div class="form-body">		
			<table class="table_list">
				<th>
					授权关系
				</th>
				<th>
					授权项
				</th>
				<th>
					比较符
				</th>
				<th>
					值
				</th>
				<th>
					检查顺序
				</th>
				<th>
					操作
				</th>
				<tbody id="editRule">

				</tbody>
			</table>
			<table class="table_query">
				<tbody id="reasonReview">
					<tr>
						<td align="center"></td>
						<td align="center">
							<input class="normal_btn" type="button" name="view" value="预览"
								onclick="javascript:assembleItem();" />
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
					<td style="text-align:right">
						授权项：
					</td>
					<td>
						<script type="text/javascript">
	              genSelBoxExp("CHOOSE_ELEMENT",<%=Constant.CLAIM_AUTH_TYPE%>,"",false,"","","false",'');
	         </script>
						<input class="normal_btn" type="button" name="add" value="增加"
							onclick="javascript:addItem();" />
					</td>
				</tr>
				<tr style="display: none">
					<td style="text-align:right">
						注：
					</td>
					<td>
						<div align="left" >
							&nbsp;1.索赔类型：免费保养，追加索赔，重复修理索赔，一般索赔， 零件索赔更换，重新递交索赔，PDI索赔，保外索赔
							<br />
							&nbsp;&nbsp;&nbsp;2.规则运算不区分OR和AND的优先级，会按照上面的排列顺序依次执行
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align:center">
						<input type="hidden" name="AUTHROLE" />
						<!-- 授权角色隐藏域 -->
						<input name="ok" type="button" class="normal_btn" id="commitBtn"
							value="完成" onclick="checkFormUpdate();" />
						<input name="back" type="button" class="normal_btn" value="取消"
							onclick="_hide() ;" />
					</td>
				</tr>
			</table>
			</div>
			</div>
		</form>
		</div>
		<script>
//去掉字符串中的空格：
function trim(str){
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
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
//验证索赔次数
function checkValue(desc,compare,objVal){
	if(desc.indexOf('索赔次数')!=-1){
		if(!(/^[1-9]{1}\d{0,59}$/.test(objVal)) ) {
			MyAlert('索赔次数只能为整数');
			return false;
		}
	}	
	return true;
}
//验证是否是数字，字母，中文
function isDigitLetterCn(obj){
     var reg = /^[\u4e00-\u9fa5\da-zA-Z\-\/,()._ ]+$/;
     if (reg.test(obj.value)){
     	 return true;
     	//"不能输入数字、字母和中文以外的字符."
     }else{
	     return false;
     } 
}
//验证金钱
function isYuan(obj){
	var reg = /^(([1-9]\d*)|0)(\.\d{1,2})?$/;
	if(reg.test(obj.value)){
		return true;
	}else{
		return false;
	}
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
function fmfoucs(){
	fm.ELEMENT_VALUE.focus();
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
	var td1= '<select name=\"BOOLEAN_COMPARISION\" size=\"1\" class=\"u-select\" style=\"width:10px\" ><%	for (int j=0; j<boolComp.length; j++){ %><option value=\"<%=boolComp[j]%>\"><%=boolComp[j]%></option><% } %></select>';
	//授权项：
	var td2 = '<input type=\"hidden\" name=\"ELEMENT_ID\" value=\"'+objId+'\"><input type=\"hidden\" name=\"ELEMENT_DESC\" value=\"'+objDesc+'\">'+objDesc;
	//比较符
	//授权项算术比较符
	var td3 = genSelBoxStrExp("COMPARISON_OP",<%=Constant.COMP_TYPE%>,"",false,"u-select","","true",'');
	//if ("是否特殊质保车辆" == objDesc || "车型代码" == objDesc || "车型大类" == objDesc || "配件代码" == objDesc || "维修操作代码" == objDesc || "索赔类型" == objDesc || "经销商代码"==objDesc || "产地"==objDesc){
	//	//授权项逻辑比较符
	//	td3 = genSelBoxStrExp("COMPARISON_OP",<%=Constant.LOGIC_TYPE%>,"",false,"","","true",'');
	//}
	//值
	var td4 = '<input name=\"ELEMENT_CODE\" id=\"ELEMENT_CODE_'+rowlen+'\" type=\"hidden\">';//用于显示索赔类型code
	//if("车型代码" == objDesc){
	//	var v = document.getElementById("WRGROUP_ID").value;
	//	td4 += '<input name=\"ELEMENT_VALUE\" id=\"ELEMENT_VALUE_'+rowlen+'\" type=\"text\" readonly=\"true\" class=\"short2_txt\">';
	//	td4 +='<a href=\"#\" onclick=\"showMaterialGroup(\'ELEMENT_VALUE_'+rowlen+'\',\'\',\'false\',\'3\',\'true\')\" style=\"cursor: pointer;\">选择</a>';
	//}else if("经销商代码" == objDesc){
	//	td4 += '<input name=\"ELEMENT_VALUE\" id=\"ELEMENT_VALUE_'+rowlen+'\" type=\"text\"  readonly=\"true\" class=\"short2_txt\">';
	//	td4 +='<a href=\"#\" onclick=\"showOrgDealer(\'ELEMENT_VALUE_'+rowlen+'\',\'\',\'true\')\"  style=\"cursor: pointer;\">选择</a>';
	//}else if("车型大类" == objDesc){
	//	td4 += '<input name=\"ELEMENT_VALUE\" id=\"ELEMENT_VALUE_'+rowlen+'\" type=\"text\" readonly=\"true\" class=\"short2_txt\">';
	//	td4 +='<a href=\"#\" onclick=\"showLabor('+rowlen+')\" style=\"cursor: pointer;\">选择</a>';
	//}
	//else if("索赔类型" == objDesc){
	//	td4 += '<input name=\"ELEMENT_VALUE\" id=\"ELEMENT_VALUE_'+rowlen+'\" type=\"text\" readonly=\"true\" class=\"short2_txt\">';
	//	td4 +='<a href=\"#\" onclick=\"selClaimType('+rowlen+')\" style=\"cursor: pointer;\">选择</a>';
	//}else{
		td4 +='<input name=\"ELEMENT_VALUE\" type=\"text\" class=\"middle_txt\">';
	//}
	//检查顺序
	var td5 = '<input name=\"ELEMENT_POSITION\" class=\"middle_txt\" style=\"width:5px\"   type=\"text\"  onchange=\"javascript:positionChange(this)\">';
	//操作：
	/* var td6 = '<input type=\"button\" value=\"移除\" class=\"normal_btn\" name=\"remain\" onclick=\"javascript:delItem(this)\">'; */
	var td6 = '<a class=\"u-anchor\" name=\"remain\" onclick=\"javascript:delItem(this);\">移除</a>';


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
	editRule.deleteRow(i-1);
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

				//值列验证
				if ("" == fm.ELEMENT_VALUE[i].value){
					MyAlert("请输入值...",function(){
						fm.ELEMENT_VALUE[i].focus();
					});
					return false;
				}else if(!isDigitLetterCn(fm.ELEMENT_VALUE[i])){
					MyAlert("值只能是数字、字母和中文...",function(){
						fm.ELEMENT_VALUE[i].focus();
					});
					return false;
				}
				else if("索赔配件三包期内金额" == fm.ELEMENT_DESC[i].value || "维修项目总金额" == fm.ELEMENT_DESC[i].value|| "维修总费用" == fm.ELEMENT_DESC[i].value ){
					if(!isYuan(fm.ELEMENT_VALUE[i])){
						MyAlert("值列金额格式输入有误...",function(){
						fm.ELEMENT_VALUE[i].focus();
					});
						return false;
					}
				}
				else if("主要件次数" == fm.ELEMENT_DESC[i].value ||"非主要件次数" == fm.ELEMENT_DESC[i].value ||"修理累计时间" == fm.ELEMENT_DESC[i].value){
					if(!isUnsignedInteger(fm.ELEMENT_VALUE[i])){
						MyAlert("值列只能是整数",function(){
						fm.ELEMENT_VALUE[i].focus();
					});
						return false;
					}
				}				
				//检查顺序验证
				if ("" == fm.ELEMENT_POSITION[i].value){
					MyAlert("请输入检查顺序...",function(){
						fm.ELEMENT_POSITION[i].focus();
					});
					return false;
				}
				else if(!isUnsignedInteger(fm.ELEMENT_POSITION[i])){
					MyAlert("检查顺序只能为整数",function(){
						fm.ELEMENT_POSITION[i].focus();
					});
					return false;
				}
				else if(fm.ELEMENT_POSITION[i].value >=len||fm.ELEMENT_POSITION[i].value<0){
					MyAlert("检查顺序应从0开始且不可以大于等于授权数量",function(){
						fm.ELEMENT_POSITION[i].focus();
					});
					return false;
				}
				else{
					for(var j=i+1;j<len;j++){
						if(fm.ELEMENT_POSITION[i].value==fm.ELEMENT_POSITION[j].value){
							MyAlert("检查顺序不能重复",function(){
								fm.ELEMENT_POSITION[j].focus();
							});
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
			//值列验证
			if ("" == fm.ELEMENT_VALUE.value){
				MyAlert("请输入值...",fmfoucs);
				return false;		
			}else if(!isDigitLetterCn(fm.ELEMENT_VALUE)){
				MyAlert("值只能是数字、字母和中文...",fmfoucs);
				return false;
			}else if("索赔配件三包期内金额" == fm.ELEMENT_DESC.value || "维修项目总金额" == fm.ELEMENT_DESC.value || "维修总费用" == fm.ELEMENT_DESC.value){
				if(!isYuan(fm.ELEMENT_VALUE)){
					MyAlert("值列金额格式输入有误...",fmfoucs);
					return false;
				}
			}else if("主要件次数" == fm.ELEMENT_DESC.value ||"非主要件次数" == fm.ELEMENT_DESC.value 
				||"修理累计时间" == fm.ELEMENT_DESC.value){
				if(!isUnsignedInteger(fm.ELEMENT_VALUE)){
					MyAlert("值列只能是整数",fmfoucs);
					return false;
				}
			}

			
			//检查顺序验证
			if ("" == fm.ELEMENT_POSITION.value){
				MyAlert("请输入检查顺序...",function(){
					fm.ELEMENT_POSITION.focus();
				});
				return false;
			}
			else if(!isUnsignedInteger(fm.ELEMENT_POSITION)){
				MyAlert("检查顺序只能为整数",function(){
					fm.ELEMENT_POSITION.focus();
				});
				return false;
			}
			else if(fm.ELEMENT_POSITION.value >=1||fm.ELEMENT_POSITION.value<0){
				MyAlert("检查顺序应从0开始且不可以大于等于授权数量",function(){
					fm.ELEMENT_POSITION.focus();
				});
				return false;
			}
			//拼串
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
//根据检查顺序显示授权关系列
function positionChange(obj){	
	var i = obj.parentElement.parentElement.rowIndex;
	if(obj.value == 0){//起始显示
		editRule.children[i-1].children[0].innerHTML="---";
	}
	else if(obj.value != 0){
		editRule.children[i-1].children[0].innerHTML='<select name=\"BOOLEAN_COMPARISION\" size=\"1\"><%	for (int j=0; j<boolComp.length; j++){ %><option value=\"<%=boolComp[j]%>\"><%=boolComp[j]%></option><% } %></select>';
	}
}
/**
*车型代码弹出框：
*value1 : 车型组id
*r      : 对应的行号计数
*/
function selModel(value1,r){
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
			makeNomalFormCall('<%=contextPath%>/claim/authorization/RuleMain/ruleAdd.json',showResult,'fm');			
	}
	function goBack(){
		history.go(-1);
	}
	function showResult(json){
		if(json.success != null && json.success == "true"){
		MyAlert("新增成功！");
		__parent().__extQuery__(1) ;
		_hide() ;
			<%-- window.location.href = "<%=contextPath%>/claim/authorization/RuleMain/ruleInit.do"; --%>
		}else{
			MyAlert("新增失败，请联系管理员！");
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
		MyConfirm("是否确认添加?",checkForm);
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

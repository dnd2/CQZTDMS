<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<title>索赔工时维护</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	</head>
	<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />
			&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔工时维护
		</div>
		<form name='fm' id='fm'>
		<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
			<input type="hidden" name="wrmodelgrouplist" id="wrmodelgrouplist"
				value="<%=request.getAttribute("wrmodelgrouplist")%>" />
			<input type="hidden" name="P_ID" id="P_ID"/>	
			<table class="table_query">

				       <tr>
            <td class="table_query_2Col_label_6Letter"  style="text-align:right">索赔车型组：</td>
            <td >
            <input type="text" value="" name="WRGROUP_CODE"  id ="WRGROUP_CODE" class="middle_txt" readonly="readonly" onclick="showLabor();" />
            <input type="hidden" value="" name="WRGROUP_ID"  id ="WRGROUP_ID" class="middle_txt"/>
         	<!-- <input type="button" value="..." class="normal_btn" onclick="showLabor();"/> -->
         	<input type="button" value="清空" class="normal_btn" onclick="cleanInput();"/>
            </td>
            <td class="table_add_2Col_label_5Letter"  style="text-align:right">
						工时代码：
					</td>
<%--					is_labercode--%>
					<td>
						<input id="LABOR_CODE" name="LABOR_CODE" type="text"
							class="middle_txt" datatype="0,is_null,15" />
					</td>
					<td class="table_add_2Col_label_5Letter"  style="text-align:right">
						工时名称：
					</td>
					<td>
						<input id="LABOR_NAME" name="LABOR_NAME" type="text"
							class="middle_txt" datatype="0,is_null,30" />
					</td>
           </tr>

<%--				<tr>--%>
<%--					<td class="table_add_2Col_label_5Letter">--%>
<%--						大类名称：--%>
<%--					</td>--%>
<%--					<td>--%>
<%--						<input id="FIRST_CLASS" name="FIRST_CLASS" type="text"--%>
<%--							class="middle_txt" datatype="1,is_null,30" />--%>
<%--					</td>--%>
<%--				</tr>--%>
<%----%>
<%--				<tr>--%>
<%--					<td class="table_add_2Col_label_5Letter">--%>
<%--						小类名称：--%>
<%--					</td>--%>
<%--					<td>--%>
<%--						<input id="SECOND_CLASS" name="SECOND_CLASS" type="text"--%>
<%--							class="middle_txt" datatype="1,is_null,30" />--%>
<%--					</td>--%>
<%--				</tr>--%>

				<tr>
					<td class="table_add_2Col_label_5Letter"  style="text-align:right">
						工时系数：
					</td>
					<td>
						<input id="LABOR_QUOTIETY" name="LABOR_QUOTIETY" type="text"
							class="middle_txt" datatype="0,isMoney,7" />
					</td>
					<td class="table_add_2Col_label_5Letter"  style="text-align:right">
						索赔工时：
					</td>
					<td>
						<input id="LABOUR_HOUR" name="LABOUR_HOUR" type="text"
							class="middle_txt" datatype="0,isMoney,7" />
					</td>
					<td class="table_add_2Col_label_5Letter"  style="text-align:right">
						工时大类：
					</td>
					<td>
 		<input type="text" name="P_LABOUR_CODE" id="P_LABOUR_CODE" readonly="readonly" class="middle_txt" onclick="selPaterClass();" datatype=0,is_null,20/>
 		<!-- <input type="button" onclick="selPaterClass();" class="mini_btn" value="..."/> -->
					</td>
				</tr>
				<tr>
					<td class="table_add_2Col_label_5Letter"  style="text-align:right">
						备注：
					</td>
					<td colspan="5">
						<textarea id="remark" datatype="1,is_textarea,100"  maxlength="100" name="remark" rows="3" cols="40"></textarea>
					</td>
				</tr>

<%--				<tr>--%>
<%--					<td class="table_add_2Col_label_5Letter">--%>
<%--						注：--%>
<%--					</td>--%>
<%--					<td>--%>
<%--						<div align="left">--%>
<%--							&nbsp;1.工时代码规则如下--%>
<%--							<br/>--%>
<%--								&nbsp; 如工时代码 AB100203X --%>
<%--							<br/>--%>
<%--								&nbsp; A ：修理大类 必须A-Z的字母 --%>
<%--							<br/>--%>
<%--								&nbsp; B ：修理小类 必须A-Z的字母 --%>
<%--							<br/>--%>
<%--								&nbsp; 10：主组 必须为数字 --%>
<%--							<br/>--%>
<%--								&nbsp; 02：分组 必须为数字--%>
<%--							<br/>--%>
<%--								&nbsp; 03：分解码 --%>
<%--							<br/>--%>
<%--								&nbsp; X ：位置标识 0无位置 1左 2右 3前 4后 5上 6下 --%>
<%--							<br/>--%>
<%--								&nbsp;2.若你确定修理大类、修理小类已经存在，大类名称、小类名称可以不输入，否则请输入--%>
<%--							<br/>--%>
<%--								&nbsp;3.比如“发动机”是大类名称，“发动机总成”是小类名称--%>
<%--							<br/>--%>
<%--						</div>--%>
<%--					</td>--%>
<%--				</tr>--%>
			</table>

			<table class="table_query">

				<tr>
					<td align=center>
						&nbsp;
					</td>
					<td align=center></td>
				</tr>
				<tr>
					<td colspan="6" style="text-align:center">
						<input class="normal_btn" type="button" name="ok" value="确定" id="commitBtn"
							onclick="checkForm('<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborAdd.do');"/>
						<input class="normal_btn" name="back" type="button"
							onclick="sendPage();" value="返回"/>
					</td>
				</tr>
			</table>
			</div>
			</div>
		</form>
		</div>
<script>
//父类选择页面：
function selPaterClass(){
	OpenHtmlWindow('<%=contextPath%>/claim/basicData/ClaimLaborBigClassMain/laborPaterClassQueryInit.do',900,500);
}
//上级大类赋值：
function setPaterClass(paterid,paterCode){
	document.getElementById("P_LABOUR_CODE").value = paterCode;
	document.getElementById("P_ID").value = paterid;
}
//表单提交前的验证：
function checkForm(url){
	submitForm('fm') == true ? Add(url) : "";
}
//表单提交方法：
function Add(url){
	//fm.action = url;
	//fm.submit();
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborAdd.json',addBack,'fm','');
}

function showLabor(){
	var url = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/laborListInit2.do' ;
	OpenHtmlWindow(url,600,400);
}

function setLaborList(codes,Wcodes){
	 var scode="";
	 var wcode="";
		 for(var i=0;i<codes.length;i++){
			 scode+=codes[i]+",";
			 wcode += Wcodes[i]+",";
		 }
		 
			document.getElementById("WRGROUP_ID").value = scode.substring(0,scode.length-1);
			document.getElementById("WRGROUP_CODE").value = wcode.substring(0,wcode.length-1);
	}
	
function cleanInput(){
	document.getElementById("WRGROUP_ID").value ="";
	document.getElementById("WRGROUP_CODE").value='';
}


//回调方法：
function addBack(json) {
	if(json.error != null && json.error.length > 0){
<%--		if(json.error == 'F'){--%>
<%--			MyAlert("请输入大类名称");--%>
<%--		}else if(json.error == 'S'){--%>
<%--			MyAlert("请输入小类名称");--%>
<%--		}else {--%>
			MyAlert("工时代码:["+json.error+"]系统已存在，请重新输入");
<%--		}--%>
	}else if(json.success != null && json.success=='true'){
		/* disableBtn($("commitBtn")); */
		document.getElementById("commitBtn").disabled = true ;
		MyAlertForFun("新增成功",sendPage);
	}else{
		MyAlert("新增失败！请联系管理员");
	}
}
//页面跳转
function sendPage(){
	window.location = '<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborInit.do';
}  
</script>
	</body>
</html>

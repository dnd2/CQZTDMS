<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
    String contextPath = request.getContextPath();
			Map<String, Object> map = (Map<String, Object>) request.getAttribute("complaintMap");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>物流商管维护修改</title>
<style>textarea.form-control{margin-left:10px}</style>
<script type="text/javascript" >
var myPage;
var url = "<%=contextPath%>/sales/storage/storagebase/LogisticsManage/cityMileageQuery.json?COMMAND=1";
var title = null;
var columns = [
			{header: "序号", align:'center',renderer:getIndex},
			{header: "省份", dataIndex: 'PROVINCE_NAME', align:'center'},
			{header: "地级市", dataIndex: 'CITY_NAME', align:'center'},
			{header: "公里数", dataIndex: 'DISTANCE', align:'center'},
			{header: "操作",sortable: false, dataIndex: 'DIS_ID', align:'center',renderer:myButton}
	      ];
function myButton(value,metaDate,record){
	return String.format("<input type='button' name='delBtn' class='normal_btn' value='删除' onclick='del("+value+")'/>");
}
function doInit(){
	var obj = document.getElementById("YIELDLY");
	obj.value = <%=map.get("AREA_ID")%>;
	var dis=document.getElementById("disIds_").value='${parmlog}';
	__extQuery__(1);
	genLocSel('txt1','','');//支持火狐
	
}
function seach(){
	__extQuery__(1);
}
function showDisId(oldIds,disIds){
	var ids;
	if(oldIds.length>0){
		ids=oldIds+","+disIds;
	}else{
		ids=disIds;
	}
	document.getElementById("disIds_").value = ids.toString();
	__extQuery__(1);
}
function getDisValues(){
	return document.getElementById("disIds_").value;
}
function del(disId){
	var dis=document.getElementById("disIds_").value;
	var array=new Array();
	var newDisIds="";
	array=dis.split(",");
	var bl=0;
	if(array.length>0){
		for(var i=0;i<array.length;i++){
				if(array[i]!='' && array[i]!=disId){
					newDisIds+=array[i]+","	;
					bl=1;
				}
			}
	}
	
	var dId=newDisIds.substr(0,newDisIds.length-1);
	document.getElementById("disIds_").value=dId;
	__extQuery__(1);
}
function updateLogic()
{ 	
	if(checkData()==true){
		MyConfirm("确认修改该信息！",updateLogistics);
	}
}

function updateLogistics()
{ 
	disabledButton(["saveButton","goBack"],true);
	makeNomalFormCall("<%=contextPath%>/parts/baseManager/logisticsManage/LogisticsManage/editLogistics.json",updateLogisticsBack,'fm','queryBtn'); 
}

function updateLogisticsBack(json)
{
	if(json.returnValue == 1)
	{
		parent.MyAlert("操作成功！");
		fm.action = "<%=contextPath%>/parts/baseManager/logisticsManage/LogisticsManage/logisticsInit.do";
		fm.submit();
	}else if(json.returnValue == 2)//添加失败 
	{
		var errorInfo="";
		for(var i=0;i<json.disList.length;i++){
			errorInfo+=json.disList[i].PROVINCE_NAME+"-"+json.disList[i].CITY_NAME+",";
			if(json.disList.length/2==0){
				errorInfo+="<br>";
			}			
		}
		disabledButton(["saveButton","goBack"],false);
		MyAlert("操作失败！以下地市已有物流商：<p>"+errorInfo+"</p>");
	}
	else
	{
		disabledButton(["saveButton","goBack"],false);
		MyAlert("操作失败！请联系系统管理员！");
	}
}
function checkData(){
	var yieldly=document.getElementById("YIELDLY");// 产地
	var logiCode=document.getElementById("LOGI_CODE");// 物流商编码
	var logiName=document.getElementById("LOGI_NAME");// 物流商简称
	var logiFullName=document.getElementById("LOGI_FULL_NAME");// 物流商全称
	var corporation=document.getElementById("CORPORATION");// 法人
	var conPer=document.getElementById("CON_PER");// 联系人
	var conTel=document.getElementById("CON_TEL");// 联系人电话
	var status=document.getElementById("STATUS");// 状态
	var address=document.getElementById("ADDRESS");// 地址
		if(yieldly.value==null || yieldly.value==""){
			//logiCode.focus();
			MyAlert("产地不能为空！");
			return  false;
		}
		if(logiCode.value==null || logiCode.value==""){
			//logiCode.focus();
			MyAlert("物流商编码不能为空！");
			return  false;
		}
		if(logiName.value==null || logiName.value==""){
			//logiName.focus();
			MyAlert("物流商简称不能为空！");
			return  false;
		}
		if(logiFullName.value==null || logiFullName.value==""){
			MyAlert("物流商全称不能为空！");
			return  false;
		}
		/*if(corporation.value==null || corporation.value==""){
			MyAlert("法人不能为空！");
			return  false;
		}
		if(conPer.value==null || conPer.value==""){
			MyAlert("联系人不能为空！");
			return  false;
		}
		if(conTel.value==null || conTel.value==""){
			MyAlert("联系人电话不能为空！");
			return  false;
		}
		if(address.value==null || address.value==""){
			MyAlert("地址不能为空！");
			return  false;
		}*/
// 		var dis=document.getElementById("disIds_").value;
// 		var array=new Array();
// 		array=dis.split(",");
// 		var bl=0;
// 		if(array.length>0){
// 			for(var i=0;i<array.length;i++){
// 					if(array[i]!='' || array[i].length>0){
// 						bl+=1;
// 					}
// 				}
// 		}
// 		if(bl==0){
// 			MyAlert("请选择负责区域！");
// 			return  false;
// 		}
		return true;
	}
function back(){
	fm.action="<%=contextPath%>/parts/baseManager/logisticsManage/LogisticsManage/logisticsInit.do";
	fm.submit();
}
function regionFun(){
	if($("#YIELDLY")[0].value==""){
		MyAlert("请先选择产地！");
		return;
	}
	showCityMileage('','disIds_',true)	
}
</script>

</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 物流商维护 &gt; 修改
		</div>
		<form method="post" name="fm" id="fm">
			<input type="hidden" name="LOGI_ID" id="LOGI_ID" value="<c:out value="${complaintMap.LOGI_ID}"/>" />
			<div class="form-panel">
				<h2>
					<img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 基本信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td align="right" style="display: none">产地：</td>
							<td colspan="3" style="display: none">
								<select name="YIELDLY" id="YIELDLY" class="short_sel">
									<c:if test="${list!=null}">
										<c:forEach items="${list}" var="list">
											<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
										</c:forEach>
									</c:if>
								</select><span style="color: red">&nbsp;*</span>
							</td>
						</tr>
						<tr>
							<td class="right">物流商编码：</td>
							<td>
								<input type="text" class="middle_txt" name="LOGI_CODE" id="LOGI_CODE" value="<c:out value="${complaintMap.LOGI_CODE}"/>" />
							</td>
							<td class="right">物流商简称：</td>
							<td>
								<input type="text" class="middle_txt" name="LOGI_NAME" id="LOGI_NAME" value="<c:out value="${complaintMap.LOGI_NAME}"/>" />
							</td>
						</tr>
						<tr>
							<td class="right">物流商全称：</td>
							<td>
								<input type="text" class="middle_txt" name="LOGI_FULL_NAME" id="LOGI_FULL_NAME" value="<c:out value="${complaintMap.LOGI_FULL_NAME}"/>" />
							</td>
							<td class="right">法人：</td>
							<td>
								<input type="text" class="middle_txt" name="CORPORATION" id="CORPORATION" value="<c:out value="${complaintMap.CORPORATION}"/>" />
							</td>
						</tr>
						<tr>
							<td class="right">联系人：</td>
							<td>
								<input type="text" class="middle_txt" name="CON_PER" id="CON_PER" value="<c:out value="${complaintMap.CON_PER}"/>" />
							</td>
							<td class="right">联系人电话：</td>
							<td>
								<input type="text" class="middle_txt" name="CON_TEL" id="CON_TEL" value="<c:out value="${complaintMap.CON_TEL}"/>" />
							</td>
						</tr>
						<tr>
							<td class="right">状态：</td>
							<td colspan="3">
								<label> <script type="text/javascript">
									genSelBoxExp("STATUS", <%=Constant.STATUS%>, "<c:out value="${complaintMap.STATUS}"/>", false, "", "", "false", '');
								</script>
								</label>
							</td>
						</tr>
						<tr>
							<td class="right">地址：</td>
							<td colspan="3">
								<input type="text" class="middle_txt" style="width: 300px;" name="ADDRESS" id="ADDRESS" value="<c:out value="${complaintMap.ADDRESS}"/>" />
							</td>
						</tr>
						<tr>
							<td class="right">备注：</td>
							<td colspan="3">
								<textarea class="form-control" style="width: 80%;" rows="3" name="REMARK" id="REMARK"><c:out value="${complaintMap.REMARK}" /></textarea>
							</td>
						</tr>
						<tr />
						<tr>
							<td class="center" colspan="4">
								<input type="button" name="button1" id="saveButton" class="u-button" onclick="updateLogic();" value="保存" />
								<input type="button" name="button1" id="goBack" class="u-button" onclick="back();" value="返回" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
</body>
</html>

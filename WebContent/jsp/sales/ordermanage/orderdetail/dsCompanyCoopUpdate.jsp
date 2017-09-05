<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>经销商用户组修改</title>
<!-- 扩展组件(主要扩展公用弹窗和数据传输处理的方式) WUXB(2015-08-26 10:34) -->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/framecommon/extended.js"></script>
<script type="text/javascript">
function addOrgDealer() {
	var url = g_webAppName + "/dialog/showOrgDealerToDS.jsp";
	var setting = {
		ISMULTI:true,
		ORGID:'${orgId}'
	};
	Extended.Dialog.showModalDialog(url, setting);
}

/*
 * 新增合作经销商的处理函数(因开发框架机制的影响，该函数由弹窗页面调用)
 *
 * @param JSON格式的字符串
 */
function dialogCallBack(data) {
	if (null == data || data == "") {
		return;
	}
	addRow(eval("(" + data + ")"));
}

function addRow(json) {
	var ids = "", codes = "";
	var myTable = document.getElementById("myTable");
	if (!myTable) {
		return ;
	}
	var tbody = myTable.getElementsByTagName("tbody")[0];
	var trlength = tbody.getElementsByTagName("tr").length -1;
	var old_codes = $('dealerCodes').value;
	var temp_code = "," + old_codes + ",";
	for (var i = 0, len = json.length; i < len; i++) {
		//如果选择的合作经销商已经存在则不添加了
		if (temp_code.indexOf("," + json[i].code + ",") >= 0) {
			continue;
		}
		
		var newRow = tbody.insertRow();
		newRow.className = ((trlength + i) % 2 == 0) ? "table_list_row1" : "table_list_row2";
		var newCell = newRow.insertCell(0);
		newCell.align = "center";
		newCell.innerHTML = json[i].code;
		
		newCell = newRow.insertCell(1);
		newCell.align = "center";
		newCell.innerHTML = json[i].name;
		
		newCell = newRow.insertCell(2);
		newCell.align = "center";
		newCell.innerHTML = '<a id="a' + json[i].code + '" href="javascript:removerTR(\'' + json[i].code + '\');">[删除]</a>';
		
		codes += (codes == "") ? json[i].code : "," + json[i].code;
	}
	$('dealerCodes').value = (old_codes == "") ? codes : old_codes + (codes == "" ? "" : "," + codes);
}

function removerTR(code) {
	var code_array = $('dealerCodes').value.split(",");
	code_array.remove(code);
	$('dealerCodes').value = code_array.join(",");
	var obj = document.getElementById("a" + code);
	var pobj = obj.parentNode.parentNode;
	pobj.parentNode.removeChild(pobj);
}
</script>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 经销商集团 &gt; 虚拟集团维护</div>
<form id="fm" name="fm" method="post">
<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
<input type="hidden" name="dsDealerId" id="dsDealerId" value="${po.dsDealerId}" />
<input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query" border="0">
		<tr>
			<td align="right">组名称：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="coopName" id="coopName" value="${po.coopName}" datatype="0,is_textarea,30" />	
			</td>
			<td align="right">状态：</td>
			<td align="left">
				<select style="width:134px;" name="status">
					<option value="10011001" <c:if test="${status=='10011001'}">selected</c:if>>有效</option>
					<option value="10011002" <c:if test="${status=='10011002'}">selected</c:if>>无效</option>
				</select>				
			</td>
		</tr>
		<tr>
			<td align="right">电商代码：</td>
			<td align="left">
				<input name="dealerCode" type="text" id="dealerCode" class="middle_txt" value="${dealerCode}" size="20"  datatype="0,is_textarea,30" readonly/>
      			<input type="hidden" name="dealerId" size="15" id="dealerId" value="" />
				<input type="button" id="button1" value="..." class="mini_btn" onclick="showOrgDealerAll('dealerCode','dealerId','false','','true','true','<%=Constant.DEALER_TYPE_DVS%>,<%=Constant.DEALER_TYPE_JSZX%>,<%=Constant.DEALER_TYPE_QYZDL%>','dealerName')"/>
			</td>
			<td align="right">电商名称：</td>
			<td align="left">
			<input name="dealerName" type="text" id="dealerName" class="middle_txt" value="${dealerName}" size="20"  datatype="0,is_textarea,30" readonly/>
			<!--
			<input type="hidden" name="dealerIds" size="15" id="dealerIds" value="" />
		  	<input type="text" class="middle_txt"  name="dealerCodes" size="15" value="${dealerCodes}" id="dealerCodes" datatype="0,is_textarea,30"/>
	      	<input class="mini_btn"  name="button2" type="button" onclick="showOrgDealer('dealerCodes','dealerIds','true', '${orgId}')" value="..." />
			-->
		</tr>
		<tr>
		<td align="center" colspan="4">
			<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" class="normal_btn" onclick="updateSubmit();" value="保  存" id="addSub" />
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" class="normal_btn" onclick="javascript:history.back() ;" value="返 回" id="retBtn" />
		</td>
	</tr>
	</table>
	
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
	<table class="table_query">
    	<tr class="cssTable" >
      		<td width="100%" align="left">
	      		<input type="hidden" name="dealerIds" id="dealerIds" value="" />
	      		<input type="hidden" name="dealerCodes" id="dealerCodes" value="${dealerCodes}" />
	      		<input class='cssbutton' name="add22" type="button" style="width:100px;" onclick="addOrgDealer()" value ='新增合作经销商' />
         	</td>
    	</tr>
  </table>
</form>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar", "topbar")</script>
<script type="text/javascript">
var context= '<%=contextPath%>';
var myPage;
var url = context+"/sales/ordermanage/orderdetail/DsCompanyCoop/getCoopDealerByDealerId.json?dsDealerId=${po.dsDealerId}";
var title = null;
var columns = [
	{header: "合作经销商代码", dataIndex:'DEALER_CODE',  align:'center'},
	{header: "合作经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
	{id:'action', header: "操作", dataIndex: 'DEALER_CODE', align:'center', renderer:deleteCoopDealerByCoopId}
];

function deleteCoopDealerByCoopId(value) {
	var html = '<a id="a' + value + '" href="javascript:removerTR(\'' + value + '\');">[删除]</a>';
	return String.format(html);
}

function updateSubmit(){
		var status=$("status").value;
		if(status==null||''==status){
			MyAlert("请选择用户组状态！！！");
			return;
		}
		//checkName($("groupName").value,$("companyGroupId").value);
		if(submitForm("fm")){
				var urls=context+"/sales/ordermanage/orderdetail/DsCompanyCoop/coopUpdate.json";
				makeFormCall(urls, updateResult, "fm") ;
		}
	}
	function updateResult(json){
		if(json.flag==1){
			MyAlert("修改成功！！");
			location.href=context+"/sales/ordermanage/orderdetail/DsCompanyCoop/doInit.do";
			
		}else{
			MyAlert("修改失败！！");
		}
	}
</script>
</div>
</body>
</html>

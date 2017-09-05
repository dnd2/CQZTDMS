<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>配件直发条件设置</title><script type="text/javascript" >
$(function(){
	__extQuery__(1);
});
var myPage;

var url = "<%=contextPath%>/parts/baseManager/partInnerOrgManager/partInnerOrgAction/partInnerOrgSearch.json";

var title = null;

var columns = [
			{header: "序号", dataIndex: 'IN_ORG_ID', renderer:getIndex, style: 'text-align: center'},
			{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink , style: 'text-align: center'},
			{header: "单位编码", dataIndex: 'IN_ORG_CODE',  style: 'text-align:  center'},
			{header: "单位名称", dataIndex: 'IN_ORG_NAME',  style: 'text-align: center'},
			{header: "地址", dataIndex: 'ADDRESS',  style: 'text-align: center'},
			{header: "联系人", dataIndex: 'LINK_MAN',  style: 'text-align: center'},
			{header: "联系电话", dataIndex: 'LINK_PHONE',  style: 'text-align:center'},
			{header: "修改人", dataIndex: 'NAME',  style: 'text-align: center'},
			{header: "修改时间", dataIndex: 'UPDATE_DATE',  style: 'text-align:center'},
			{header: "是否有效", dataIndex: 'STATE',  style: 'text-align:center',renderer:getItemValue}
		];
     

//设置超链接
function myLink(value,meta,record)
{
	var defineId = record.data.IN_ORG_ID;
	var state = record.data.STATE;
	var disableValue = <%=Constant.STATUS_DISABLE%>;
	if(disableValue == state){
		return String.format("<a href=\"#\" onclick='changeStateConfirm(1, \"有效\", \""+defineId+"\")'>[有效]</a>");
	} else {
       	return String.format("<a href=\"#\" onclick='changeStateConfirm(2, \"失效\", \""+defineId+"\")'>[失效]</a>&nbsp;<a href=\"#\" onclick='formod(\""+defineId+"\")'>[维护]</a>");
	}
 		
}

// 改变记录状态
function changeStateConfirm(type, msg, id) {
	MyConfirm("确定"+msg+"该数据?", changeState, [[type, id]]);
}

//改变记录状态
 function changeState(paramArr){
	var type = paramArr[0];
	var id = paramArr[1];
	var url = '<%=contextPath%>/parts/baseManager/partInnerOrgManager/partInnerOrgAction/';
	if(type == 1){
		// 设置有效
		url += 'enablePartInnerOrg.json?enableParms='+id;
	}else if(type == 2){
		// 设置无效
		url += 'celPartInnerOrg.json?disabeParms='+id;
	}else{
		MyAlert('操作失败，请联系管理员！');
		return;
	}
	btnDisable();
	url += '&curPage='+myPage.page;;
	makeNomalFormCall(url,showResult,'fm');
	
 }

 function showResult(json) {
 	btnEnable();
	if (json.errorExist != null && json.errorExist.length > 0) {
	   MyAlert(json.errorExist);
	} else if (json.success != null && json.success == "true") {
		layer.msg("操作成功!", {icon: 1});
		__extQuery__(json.curPage);          
	} else {
	    MyAlert("操作失败，请联系管理员!");
	}
 }

 //维护
 function formod(defineId)
 {
 	btnDisable();
 	document.getElementById("defineIdMod").value = defineId;
 	document.fm.action = "<%=contextPath%>/parts/baseManager/partInnerOrgManager/partInnerOrgAction/partInnerOrgFormodInit.do";
	document.fm.target="_self";
	document.fm.submit();
   }

 	//新增
function relationAdd(){
	btnDisable();
	window.location.href ="<%=contextPath%>/parts/baseManager/partInnerOrgManager/partInnerOrgAction/partInnerOrgAddInit.do";
}

function exportPartSTOExcel()
{
	document.fm.action = "<%=contextPath%>/parts/baseManager/partInnerOrgManager/partInnerOrgAction/expPartInnerOrgExcel.do";
	document.fm.target="_self";
	document.fm.submit();
}
</script>

</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt;  基础信息管理 &gt; 配件基信息据维护 &gt; 配件内部单位维护
		</div>
		<form method="post" name="fm" id="fm">
			<input type="hidden" name="defineIdMod" id="defineIdMod" value="" />
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">单位编码：</td>
							<td width="20%">
								<input class="middle_txt" type="text" name="orgCode" id="orgCode" />
							</td>
							<td class="right">单位名称：</td>
							<td width="20%">
								<input class="middle_txt" type="text" name="orgName" id="orgName" />
							</td>
							<td class="right">是否有效：</td>
							<td width="20%">
								<script type="text/javascript">
			   						genSelBoxExp("STATE",<%=Constant.STATUS%>,"<%=Constant.STATUS_ENABLE%>", true, "", "", "false", '');
								</script>
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
								<input class="u-button" type="button" value="新 增" onclick="relationAdd()" />
								<input class="u-button" type="button" value="导 出" onclick="exportPartSTOExcel()" />
							</td>
						</tr>
					</table>
				</div>
			</div>

			<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
		</form>
	</div>
</body>
</html>
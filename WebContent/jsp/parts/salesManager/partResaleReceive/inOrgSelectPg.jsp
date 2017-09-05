<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>零售领用单位选择</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});
var myPage;
var url = "<%=request.getContextPath()%>/parts/salesManager/partResaleReceiveManager/partResRecAction/partInnerOrgSearch.json?query=1";
var title = null;
var columns = [
    {header: "选择", sortable: false, dataIndex: 'IN_ORG_ID', align: 'center', renderer: myLink},
    {header: "单位编码", sortable: false, dataIndex: 'IN_ORG_CODE', style: 'text-align: left;'},
    {header: "单位名称", sortable: false, dataIndex: 'IN_ORG_NAME', style: 'text-align: left;'}
];
function myLink(value, metadata, record) {
    return String.format(
                    "<input type='hidden' id='orgName_" + record.data.IN_ORG_ID + "' name='orgName_" + record.data.IN_ORG_ID + "' value='" + record.data.IN_ORG_NAME + "'/>" +
                    "<input type='hidden' id='orgPhone_" + record.data.IN_ORG_ID + "' name='orgPhone_" + record.data.IN_ORG_ID + "' value='" + record.data.LINK_PHONE + "'/>" +
                    "<input type='radio' id='" + record.data.IN_ORG_ID + "' name='" + record.data.IN_ORG_ID + "' value='" + record.data.IN_ORG_ID + "' onclick='selbyid(this);'/>"
    );
}
function selbyid(obj) {
    var orgId = obj.value;
    var orgName = document.getElementById("orgName_" + orgId).value;
    var orgPhone = document.getElementById("orgPhone_" + orgId).value;
    $('#orgNameSel').val(orgName);
    $('#linkPhoneSel').val(orgPhone);
    // 赋值父页面
    parentDocument.getElementById('linkMan').value = orgName;
    parentDocument.getElementById('telPhone').value = orgPhone;
    _hide();
}
</script>

</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理 &gt; 零售领用管理 &gt; 单位选择
		</div>
		<form name='fm' id='fm'>

			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/img/subNav.gif" /> 基本信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">单位编码：</td>
							<td>
								<input class="middle_txt" id="orgCode" name="orgCode" type="text" datatype="1,is_null,20" />
							</td>
							<td class="right">单位名称：</td>
							<td>
								<input type="text" name="orgName" id="orgName" datatype="1,is_null,30" class="middle_txt" value="" />
								<input type="hidden" name="orgNameSel" id="orgNameSel" />
								<input type="hidden" name="linkPhoneSel" id="linkPhoneSel" />
							</td>
						</tr>
						<tr>
							<td colspan="4" class="center">
								<input class="u-button u-query" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
								&nbsp;&nbsp;
								<input class="u-button u-cancel" type="button" name="button1" value="关 闭" onclick="_hide();" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
		</form>
	</div>
</body>
</html>
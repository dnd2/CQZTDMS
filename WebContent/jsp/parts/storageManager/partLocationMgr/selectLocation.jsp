<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>货位选择</title>
<script type="text/javascript" >
$(function(){
	__extQuery__(1);
});
var myPage;
//查询路径
var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/selectLocation.json";
var title = null;
var columns = [
			{header: "选择", align:'center', dataIndex:'LOC_ID',renderer:getRadio},
			{header: "货位代码",dataIndex: 'LOC_CODE',align:'center'},
			{header: "货位名称",dataIndex: 'LOC_NAME',align:'center'}
	      ];

function getRadio(value,meta,record){
	var i = record.data.LOC_ID;
	var c = record.data.LOC_CODE;
	var n = record.data.LOC_NAME;
	var radioText = "<input type='radio' name='abc' value='123' onclick = 'selData(\""+i+"\",\""+c+"\",\""+n+"\");'/>";
	return String.format(radioText);
}

function selData(i,c,n){
	parentDocument.getElementById('LOC_ID_'+'${partLocId}').value = i;
	parentDocument.getElementById('LOC_CODE_'+'${partLocId}').value = c;
	//parentDocument.getElementById('LOC_NAME'+'${partLocId}').value = n;
       _hide();
}
</script>
</head>

<body>
	<div class="wbox">
		<form name="fm" method="post" id="fm">
			<input type="hidden" name="whId" id="whId" value="${whId}" />
			<!-- 查询条件 begin -->
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/nav.gif" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query" id="subtab">
						<tr>
							<td class="right">配件编码：</td>
							<td>
								<input type="hidden" id="partId" name="partId" class="middle_txt" value="${partId }" />
								<input type="text" id="partOldCode" name="partOldCode" class="middle_txt" value="${partOldcode }" readonly />
							</td>
<!-- 							<td class="right">配件名称：</td> -->
<!-- 							<td> -->
<%-- 								<input type="text" id="partCname" name="partCname" class="middle_txt" value="${partCname }" readonly /> --%>
<!-- 							</td> -->
							<td class="right">货位代码：</td>
							<td>
								<input type="text" id="locCode" name="locCode" class="middle_txt" value="" size="15" />
							</td>
						</tr>
						<tr>
							<td colspan="6" class="center">
								<input type="button" id="queryBtn" class="u-button" value="查询" onclick="__extQuery__(1);" />
								<input type="reset" class="u-button" id="resetButton" value="重置" />
								<input type="reset" class="u-button" id="resetButton" value="关闭" onclick="_hide();" />
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
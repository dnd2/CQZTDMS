<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>配件销售价格维护</title>
<script type="text/javascript" >
$(function(){
	__extQuery__(1);
});

var typeId = <%=request.getParameter("typeId")%>;

var myPage;

var url = "<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePrice/getDealers.json?typeId="+typeId;

var title = null;

var columns = [
			{header: "序号",  style: 'text-align:left',renderer:getIndex},
            {header: "经销商名称", dataIndex: 'DEALER_NAME', align: 'center'},
			{header: "经销商代码", dataIndex: 'DEALER_CODE', align: 'center'},
            {header: "操作", dataIndex: 'DEALER_CODE', align: 'center',renderer:myLink}
	      ];
	      
function myLink(value, meta, record){
	return '<a href=\"#\" onclick="ckBtn('+"'"+value+"'"+')">[删除]</a>';
}

function ckBtn(val){
	MyConfirm("确定删除?", deletIt, val);
}

function deletIt(id){
	var url = "<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePrice/deleteDealer.json?typeId="+typeId+"&id="+id;
	sendAjax(url, getResult, 'fm');
}

function getResult(jsonObj){
	if(jsonObj!=null){
	    var success = jsonObj.success;
	    var error = jsonObj.error;
	    var exceptions = jsonObj.Exception;
	    if(success){
	    	MyAlert(success, function(){
		    	__extQuery__(1);
		    	mainPageQuery();
	    	});
	    }else if(error){
	    	MyAlert(error);
	    }else if(exceptions){
	    	MyAlert(exceptions.message);
		}
	}
}


function mainPageQuery(){
	parentContainer.__extQuery__(1);
}
</script>
</head>

<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input type="hidden" name="curPage" id="curPage" value="${curPage}" />
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>

							<td class="right">经销商名称：</td>
							<td>
								<input class="middle_txt" type="text" id="dealerName" name="dealerName" />
							</td>
							<td class="right">经销商代码：</td>
							<td>
								<input class="middle_txt" type="text" name="dealerCode" />
							</td>
						</tr>

						<tr>
							<td class="center" colspan="4">
								<input class="u-button u-query" type="button" value="查 询" onclick="__extQuery__(1);" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<!--  <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" scrolling="auto"> -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!-- 	</table> -->
	</div>
</body>
</form>
</html>

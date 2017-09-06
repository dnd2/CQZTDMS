<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>配件人员类型设置</title>
<script language="javascript" type="text/javascript">
$(function(){
	__extQuery__(1);
});

var myPage;

var url = "<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/partUserPostSearch.json";

var title = null;

var columns = [
			{header: "序号", dataIndex: 'SEQUENCE_ID', renderer:getIndex,align:'center'},
            {id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'},
			{header: "人员类型", dataIndex: 'FIX_NAME', style:'text-align: center;'},
            {header: "人员名称", dataIndex: 'NAME', style:'text-align: center;'},
			{header: "是否主管", dataIndex: 'IS_LEADER', align:'center',renderer:getItemValue},
			{header: "是否有效", dataIndex: 'STATE', align:'center',renderer:getItemValue}
	      ];
     

//设置超链接
function myLink(value,meta,record)
{
	var defineId = record.data.DEFINE_ID
	var uesrType = record.data.USER_TYPE;
	var fixName = record.data.FIX_NAME;
	var parms = (uesrType + "@@" + fixName).toString();
	var state = record.data.STATE;
	var userId = record.data.USER_ID;
	var disableValue = <%=Constant.STATUS_DISABLE%>;
	if(disableValue == state){
		return String.format("<a href=\"#\" onclick='enableData(\""+defineId+"\")'>[有效]</a>");
    } else {
       	return String.format("<a href=\"#\" onclick='cel(\""+defineId+"\")'>[失效]</a>&nbsp;<a href=\"#\" onclick='formod(\""+uesrType+"\",\""+fixName+"\")'>[维护]</a>&nbsp;<a href=\"#\" onclick='formodOrderType(\""+userId+"\",\""+uesrType+"\")'>[订单类型维护]</a>");
	}
 		
}
//维护订单类型
function formodOrderType(userId, userType) {
	btnDisable();
   	document.fm.action = "<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/partUserOrderFormodInit.do?userId="+userId+"&userType="+userType;
	document.fm.target="_self";
	document.fm.submit();
}
//设置失效：
function cel(parms) {
	MyConfirm('确定失效该数据?', function() {
		btnDisable();
		var url = '<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/celPartUserPost.json?disabeParms='+parms+'&curPage='+myPage.page;
		makeNomalFormCall(url,showResult,'fm');
	}, []);
}

  //设置有效：
function enableData(parms) {
	MyConfirm('确定失效该数据?', function() {
		btnDisable();
     	var url = '<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/enablePartUserPost.json?enableParms='+parms+'&curPage='+myPage.page;
     	makeNomalFormCall(url,showResult,'fm');
	}, []);
}

function showResult(json) {
	btnEnable();
    if (json.errorExist != null && json.errorExist.length > 0) {
       layer.msg(json.errorExist, {icon: 2});
    } else if (json.success != null && json.success == "true") {
    	layer.msg("操作成功!", {icon: 1});
    	__extQuery__(json.curPage);          
    } else {
        layer.msg("操作失败，请联系管理员!", {icon: 2});
    }
}

  //维护
function formod(uesrType,fixName)
{
	btnDisable();
	document.getElementById("uesrType").value = uesrType;
	document.getElementById("fixName").value = fixName;
	document.fm.action = "<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/partUserPostFormodInit.do";
	document.fm.target="_self";
	document.fm.submit();
  }

 	//新增
function relationAdd(){
	btnDisable();
	window.location.href ="<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/partUserPostAddInit.do";
}


</script>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：
		配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件人员类型设置
	</div>
	<form method="post" name ="fm" id="fm">
		<input type="hidden" name="uesrType" id="uesrType" value="" />
		<input type="hidden" name="fixName" id="fixName" value="" />
			<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">人员类型：</td>
							<td>
								<select name="fixValue" id="fixValue" class="u-select">
									<option value="">-请选择-</option>
									<c:if test="${postList!=null}">
									<c:forEach items="${postList}" var="list">
										<option value="${list.FIX_VALUE }">${list.FIX_NAME }</option>
									</c:forEach>
									</c:if>
								</select>
							</td>
							<td class="right">人员名称：</td>
							<td><input class="middle_txt" type="text" name="userName" id="userName"/></td>
							<td class="right">是否有效：</td>
							<td>
								<script type="text/javascript">
								genSelBoxExp("STATE",<%=Constant.STATUS%>,"<%=Constant.STATUS_ENABLE %>",true,"short_sel u-select","","false",'');
							</script>
							</td>
						</tr>
						<tr>
							<td class="formbtn-aln" class="center" colspan="6" >
	                			<input type="button" id="queryBtn" name="BtnQuery" class="u-button" value="查 询" onclick="__extQuery__(1);"/>
                				<input type="reset" class="u-button" value="重 置"/>
								<input class="u-button" type="button" value="新 增" onclick="relationAdd()"/>
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
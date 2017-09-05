<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" /> 
	<title>经销商用户维护</title>
	<script type="text/javascript">
		var url = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/sgmDealerSysUserQuery.json?COMMAND=1";
		var pat = "<%=contextPath%>";
	
		var columns = [
					{header: "序号", align:'center', renderer:getIndex},
					{header: "操作",sortable: false,dataIndex: 'USER_ID',renderer:myLink ,align:'center'},
					{header: "用户账号", dataIndex: 'ACNT', align:'center'},
					{header: "公司名称", dataIndex: 'COMPANY_SHORTNAME', align:'center'},
					{header: "联系人", dataIndex: 'NAME', align:'center'},
					{header: "类型", dataIndex: 'POSE_BUS_TYPE', align:'center'},
					{header: "用户名称", dataIndex: 'POSE_NAME', align:'center'},
					{header: "更新时间", dataIndex: 'UPDATE_DATE', align:'center'},
					{header: "操作人", dataIndex: 'UPDATE_NAME', align:'center'},
					{header: "用户ID", dataIndex: 'USER_ID', align:'center'},
					{header: "用户状态",sortable: false,dataIndex: 'USER_STATUS' ,align:'center',renderer:getItemValue}
		];
	
		function txtClr(value){
			document.getElementById(value).value = "";
		}
	
		/**
		 * 批量新增用户按钮
		 */
		/* function batchAdd(){
			var dealerCode = $('outDealerCode').value;
			if(null==dealerCode||dealerCode.length<1){
				return MyAlert("请选择经销商");
			}
			
			var batchUrl = globalContextPath + "/sysmng/usemng/SgmDealerSysUser/batchAddSgmDealerUser.json?dealerCode="+dealerCode;
			
			makeNomalFormCall(batchUrl, function(json){
				var hint = json.hint;
				if(hint!=null&&hint.length>0){
					MyAlert(json.hint);
				}
			}, 'fm');
		} */
		
		function myLink(value, meta, data) {
			return '<a href="#" onclick="doUpdate(' + value + ') ;">[修改]</a>';
		}
		
		function doUpdate(value) {
			window.location.href='<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/modfiSgmDealerSysUserInit.do?userId=' + value ;
		}
		
		$(function(){
			__extQuery__(1);
		});
	</script>
</head>

<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 系统管理 &gt; 经销商用户维护</div>
	<form id="fm" name="fm">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
		<input type="hidden" id="orderCol" name="orderCol" value="" />
		<input type="hidden" id="order" name="order" value="" />
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
		<div class="form-panel">
			<h2>经销商用户维护</h2>
			<div class="form-body">
				<table class="table_query">
					<tr>
						<td class="right">经销商：</td>
						<td>
							<input name="outDealerCode" type="hidden" id="outDealerCode" class="middle_txt"  />
							<input name="outDealerName" type="text" id="outDealerName" class="middle_txt" onclick="showOrgDealer('outDealerCode','dealerId','false', '', 'true','','','outDealerName', '选择经销商');"/>
							<input name="dealerId" type="hidden" id="dealerId" class="middle_txt" />
							<input type="button" class="u-button" onclick="txtClr('dealerId');txtClr('outDealerCode');txtClr('outDealerName');" value="清 空" id="clrBtn" /> 
						</td>
						<td class="right">职位代码：</td>
						<td>
							<input class="middle_txt" type="text" maxlength="30" datatype="1,is_noquotation,30" id="POSE_CODE" name="POSE_CODE"/>
						</td>
					</tr>
					<tr>	
						<td class="right">用户账号：</td>
						<td>
							<input class="middle_txt" type="text" maxlength="30" datatype="1,is_noquotation,30" id="ACNT" name="ACNT"/>
						</td>
						<td class="right">姓名：</td>
						<td>
							<input class="middle_txt" type="text" maxlength="30" datatype="1,is_noquotation,30" id="NAME" name="NAME" value=""/>
						</td>
					</tr>
					<tr>
						<td colspan="4" class="center">
							<input class="u-button u-query" type="button" value="查 询" name="queryBtn" onclick="__extQuery__(1)" />
							<input class="u-button u-reset" type="button" value="重 置" onclick="requery()"/>
							<input class="u-button u-submit" type="button" value="新 增" name="queryBtn" onclick="window.location.href='<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/addSgmDealerSysUserInit.do'"/>
							<!-- <input class="u-button u-reset" type="button" value="批量生成" name="queryBtn" onclick="batchAdd();" /> -->
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
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
	<link href="<%=contextPath %>/style/dtree1.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=contextPath %>/js/web/dept_tree.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js"></script>
	
	<title>系统职位维护</title>
	<script type="text/javascript">
		var filecontextPath="<%=contextPath%>";
		var drlurl = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/allDrlQuery.json?COMMAND=1";
	   	var tree_url = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/initOrgTree.json";
	   	var dept_tree_url = "<%=contextPath%>/sysmng/usemng/SgmSysUser/initOrgTree.json?unorglevel=3";
	   	var url = "<%=contextPath%>/sysmng/sysposition/SysPosition/sysPositionQuery.json?COMMAND=1";
	   	var title = null;
	   	var columns = [
	   			{header: "序号", align:'center', renderer:getIndex,width:'7%'},
	   			{id:'action',header: "操作", width:70,sortable: false,dataIndex: 'POSE_ID',renderer:myLink},
	   			{header: "职位代码", dataIndex: 'POSE_CODE', orderCol:"POSE_CODE", align:'center'},
	   			{header: "职位名称", dataIndex: 'POSE_NAME', align:'center'},			
	   			{header: "职位类别", dataIndex: 'POSE_TYPE', align:'center',renderer:getItemValue},
	   			{header: "状态", dataIndex: 'POSE_STATUS', orderCol:"POSE_STATUS", align:'center',renderer:getItemValue},
	   			{header: "最后操作时间", dataIndex: 'UPDATE_DATE', align:'center'},
	   			{header: "最后操作人", dataIndex: 'UPDATE_NAME', align:'center'}
	   	];
	    function myLink(value,metadata){
	    	return String.format("<a href=\"<%=contextPath%>/sysmng/sysposition/SysPosition/viewSysRoleInit.do?poseId=" + value + "\">[修改]</a>");
	    }
	    
	    var g = {
	    		selType : function() {
	    			objval = $("#POSE_TYPE").val();
	    			if(objval == '<%=Constant.SYS_USER_SGM%>') {
	    				$("#DEPT_NAME").val("");
	    				$("#jxsgs").hide();
	    				$("#cczz").show();
	    			}else if(objval == '<%=Constant.SYS_USER_DEALER%>') {
	    				$("#COMPANY_NAME").val("");
	    				$("#cczz").hide();
	    				$("#deptt").hide();
	    				$("#jxsgs").show();
	    			}
	    		}
	    };
	    
	    $(function(){
	    	$(document).on("click",function(event){
				 var x = event.pageX;
				 var y = event.pageY;
				 var dxl = $('#DEPT_NAME').position().left;
				 var dxr = $('#DEPT_NAME').position().left + $('#deptt').width();
				 var dyt = $('#DEPT_NAME').position().top - 30;
				 var dyb = $('#DEPT_NAME').position().top - 30 + $('#deptt').height();
				 if((dxl < x && x < dxr) && (dyt < y && y < dyb)) {
					 return;
				 } else {
					 $("#deptt").hide();
				 }
			 });
	    });
	</script>
</head>

<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 用户管理 &gt;职位维护
		</div>
		<form id="fm" name="fm">
			<input type="hidden" name="curPage" id="curPage" value="1" />
			<input type="hidden" id="orderCol" name="orderCol" value="" />
			<input type="hidden" id="tree_root_id" name="tree_root_id" value="" />
			<input id="DEPT_ID" name="DEPT_ID" type="hidden"/>
			<input id="DEALER_ID" name="DEALER_ID" type="hidden"/>
			<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
			<input type="hidden" id="order" name="order" value="" />
			<div class="dtree" id="deptt"></div>
			<div class="form-panel">
				<h2>职位维护</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">职位类别：</td>
							<td>
								<script type="text/javascript"> 
									genSelBox("POSE_TYPE",<%=Constant.SYS_USER%>,"<%=Constant.SYS_USER_SGM%>",false,"","onchange='g.selType(this.value)'");
								</script>
							</td>
							<td class="right">状态：</td>
							<td>
					        	<script type="text/javascript"> 
					        		genSelBox("POSE_STATUS",<%=Constant.STATUS%>,"",false,"","");
					        	</script>
					        </td>
						</tr>
						<tr id="cczz">
							<td class="right">车厂组织：</td>
							<td>
								<input class="middle_txt" id="DEPT_NAME"  onclick="dt.initDeptTree();"  readonly="readonly" name="DEPT_NAME" type="text" />
							</td>
							<td></td>
							<td></td>
						</tr>
						<tr id="jxsgs" style="display: none;">
							<td class="right">经销商公司：</td>
							<td>
								<input class="middle_txt" id="COMPANY_NAME" name="COMPANY_NAME" type="text" onclick="showCompany('<%=contextPath %>')"/>
							</td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td class="right">职位代码：</td>
							<td>
								<input name="POSE_CODE" maxlength="30" datatype="1,is_noquotation,30" id="POSE_CODE" type="text" class="middle_txt" /></td>
							<td class="right">职位名称：</td>
							<td>
								<input name="POSE_NAME" maxlength="30" datatype="1,is_noquotation,30" id="POSE_NAME" type="text" class="middle_txt" value="${POSE_NAME}" />
							</td>
						</tr>
						<tr>
							<td class="right">角色代码：</td>
							<td>
								<input class="middle_txt" id="ROLE_NAME" name="ROLE_NAME" type="text"/>
							</td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td colspan="4" class="table_query_4Col_input" style="text-align: center">
								<input name="queryBtn" type="button" class="u-button u-query" onclick="__extQuery__(1)" value="查 询" /> &nbsp; 
								<input type="reset" class="u-button u-reset" onclick="requery()" value="重 置"/> &nbsp; 
								<input type="button" class="u-button u-submit" onclick="window.location.href='<%=contextPath%>/sysmng/sysposition/SysPosition/addSysPositionInit.do'" value="新 增" />
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
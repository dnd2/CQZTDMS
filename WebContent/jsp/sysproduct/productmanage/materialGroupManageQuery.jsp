<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<title>物料组维护</title>
	<script type="text/javascript">
		var myPage;
		//查询路径
		var url = "<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/materialGroupManageQuery.json";
		var title = null;
		var columns = [
						{header: "序号", align:'center', renderer:getIndex},
						{header: "物料组代码", dataIndex: 'GROUP_CODE', align:'center'},
						{header: "物料组名称", dataIndex: 'GROUP_NAME', align:'center'},
						{
							header: "物料组级别", dataIndex: 'GROUP_LEVEL', align:'center',
							renderer: function(value, mata, record) {
								if(value == '1') return '品牌';
								else if(value == '2') return '车系';
								else if(value == '3') return '车型';
								else if(value == '4') return '配置';
							}
						},
						{header: "是否在产", dataIndex: 'FORCAST_FLAG', align:'center', renderer:myHref},
						{header: "状态", dataIndex: 'STATUS', align:'center', renderer:getItemValue},
						{
							header: "上级物料组", dataIndex: 'PAR_GROUP_NAME', align:'center', 
							renderer: function(value, mata, record) {
								if((value == '' || value == null || value == 'null') && record.data.GROUP_LEVEL == '4') return "<font color='red'>无上级关系</font>";
								else return value;
							}
						},
						{header: "更新人", dataIndex: 'NAME', align:'center'},
						{header: "更新时间", dataIndex: 'UPDATE_DATE', align:'center'},
						{id:'action', header: "操作" ,dataIndex: 'GROUP_ID', renderer:myLink}
				      ];	
			      
		function myLink(value){
			var groupCode=$('groupCode').value;
			var groupName=$('groupName').value;
			var status=$('status').value;
			var parentGroupCode=$('parentGroupCode').value;
			var forcast_flag=$('forcast_flag').value;
			var url = "<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/materialGroupManageModPre.do?groupId="+value;
			if(groupCode!=''){
				url = url+"&groupCode="+groupCode;
			}
			if(groupName!=''){
				url = url + "&groupName="+encodeURI(encodeURI(groupName));
			}
			if(status!=''){
				url = url + "&status="+status;
			}
			if(parentGroupCode!=''){
				url = url + "&parentGroupCode="+parentGroupCode;
			}
			if(forcast_flag!=''){
				url = url + "&forcast_flag="+forcast_flag;
			}
			url = url + "&page="+pageTemp;
	        return String.format(
	               "<a href=\""+url+"\">[维护]</a>");
	   	    }
		//新增
		function addMet(){
			var groupCode=$('groupCode').value;
			var groupName=$('groupName').value;
			var status=$('status').value;
			var parentGroupCode=$('parentGroupCode').value;
			var forcast_flag=$('forcast_flag').value;
			var url ="<%=request.getContextPath()%>/sysproduct/productmanage/MaterialGroupManage/materialGroupManageAddPre.do?1=1"
			if(groupCode!=''){
				url = url+"&groupCode="+groupCode;
			}
			if(groupName!=''){
				url = url + "&groupName="+encodeURI(encodeURI(groupName));
			}
			if(status!=''){
				url = url + "&status="+status;
			}
			if(parentGroupCode!=''){
				url = url + "&parentGroupCode="+parentGroupCode;
			}
			if(forcast_flag!=''){
				url = url + "&forcast_flag="+forcast_flag;
			}
			url = url + "&page="+pageTemp;
			window.location.href=url;
		}
		function clrTxt(txtId){
	    	document.getElementById(txtId).value="";
	    }
	    
	    function myHref(value){
	    	var msg = "否";
	    	if(value=='1'){
	    		msg = "是";
	    	}
	    	return String.format(msg);
	    }
	</script>
</head>
<body onload="__extQuery__(1);">
	<div class="wbox">
		<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 物料组维护</div>
		<form method="POST" name="fm" id="fm">
			<div class="form-panel">
				<h2><img src="/CQZTDMS/jmstyle/img/search-ico.png" class="panel-query-title">物料组维护</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">物料组代码：</td>
							<td>
								<input name="groupCode" maxlength="30" datatype="1,is_noquotation,30" id="groupCode" type="text" class="middle_txt" />
							</td>
							<td class="right">物料组名称：</td>
							<td>
								<input name="groupName" maxlength="30" datatype="1,is_noquotation,30" id="groupName" type="text" class="middle_txt" />
							</td>
							<td class="right">物料组状态：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("status",<%=Constant.STATUS%>,"",false,"","","false",'');
								</script>
							</td>
						</tr>
						<tr>
							<td class="right">上级物料组：</td>
							<td>
								<input type="text" class="middle_txt" name="parentGroupCode" size="20" id="parentGroupCode" onclick="showMaterialGroup('parentGroupCode','','false','','true')"/>
								<input class="u-button" type="button" value="清空" onclick="clrTxt('parentGroupCode');"/>
							</td>
							<!-- 新增过滤条件是否在产 2012-05-08 hxy -->
							<td class="right">是否在产：</td>
							<td>
								<select class="u-select" id="forcast_flag" name="forcast_flag">
									<option value="" >请选择</option>
									<option value="1">是</option>
									<option value="0">否</option>
								</select>
							</td>
							<td class="right">物料组级别：</td>
							<td>
								<select class="u-select" id="GROUP_LEVEL" name="GROUP_LEVEL">
									<option value="">==请选择==</option>
									<option value="1">品牌</option>
									<option value="2">车系</option>
									<option value="3">车型</option>
									<option value="4">配置</option>
								</select>
							</td>			
						</tr>
						<tr>
							<td colspan="6" style="text-align: center">
								<input type="button" name="queryBtn" class="u-button u-query" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> &nbsp; 
								<input type="reset" class="u-button u-cancel" value="重 置"/> &nbsp; 
								<input type="button" name="queryBtn" class="u-button u-submit" onclick="addMet()" value="新 增" />
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

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>项目执行方维护</title>
</head>
<body onload='init();'>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车管理 > 市场活动管理 > 项目执行方管理</div>
<form method="POST" name="fm" id="fm">
<c:choose>
	<c:when test="${!empty curPage}">
		<input type=hidden name='curPage' id='curPage' value='${curPage }'/>
	</c:when>
	<c:otherwise>
		<input type=hidden name='curPage' id='curPage' value='1'/>
	</c:otherwise>
</c:choose>

  	<table class="table_query" border="0">
		<tr>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">执行方代码：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input name="makerCode" maxlength="30"  datatype="1,is_noquotation,30" id="makerCode" type="text" class="middle_txt" />
			</td>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">执行方名称：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input name="makerName" maxlength="30"  datatype="1,is_noquotation,30" id="makerName" type="text" class="middle_txt" />
			</td>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap" style="display:none;">物料组状态：</td>
			<td class="table_query_4Col_input" nowrap="nowrap" style="display:none;">
				<c:set value="<%=Constant.STATUS_ENABLE%>" var="maturalStatus"></c:set>
				<c:choose>
					<c:when test="${!empty status}">
						<c:choose>
							<c:when test="${status==maturalStatus}">
								<script type="text/javascript">
				      				genSelBoxExp("status",<%=Constant.STATUS%>,"<%=Constant.STATUS_ENABLE%>",true,"short_sel","","false",'');
				   				 </script>
							</c:when>
							<c:otherwise>
								<script type="text/javascript">
				      				genSelBoxExp("status",<%=Constant.STATUS%>,"<%=Constant.STATUS_DISABLE%>",true,"short_sel","","false",'');
				   		 		</script>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<script type="text/javascript">
				      		genSelBoxExp("status",<%=Constant.STATUS%>,"",false,"short_sel","","false",'');
				  	  </script>
					</c:otherwise>
				</c:choose>
				
			</td>
		</tr>
		<tr style="display:none;">
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">上级物料组：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input type="text" name="parentGroupCode" size="20" id="parentGroupCode" value="${parentGroupCode }" />
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('parentGroupCode','','false','','true')" value="..." />
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('parentGroupCode');"/>
			</td>
			<!-- 新增过滤条件是否在产 2012-05-08 hxy -->
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">是否在产：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<select id="forcast_flag" name="forcast_flag">
					<option value="" selected="selected">请选择</option>
					<option value="1">是</option>
					<option value="0">否</option>
				</select>
				<%-- <script type="text/javascript">
					genSelBoxExp("forcast_flag",<%=Constant.IF_TYPE%>,"",false,"short_sel","","false",'');
				</script> --%>
			</td>
		</tr>
		<tr>
			<td colspan="6" class="table_query_4Col_input" style="text-align: center">
				<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> &nbsp; 
				<input type="button" class="normal_btn" onclick="myReset();" value="重 置"/> &nbsp; 
				<input name="button2" type="button" class="normal_btn" onclick="window.location.href='<%=request.getContextPath()%>/sales/marketmanage/activity/ActivityMakerManage/doAddPre.do'" value="新 增" />
			</td>
		</tr>
	</table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">

	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/marketmanage/activity/ActivityMakerManage/doQuery.json";
				
	var title = null;

	var columns = [
					{header: "序号", align:'center', renderer:getIndex},
					{header: "执行方代码", dataIndex: 'MAKER_CODE', align:'center'},
					{header: "执行方名称", dataIndex: 'MAKER_NAME', align:'center'},
					{header: "联系人", dataIndex: 'LINK_MAN', align:'center'},
					{header: "联系电话", dataIndex: 'PHONE', align:'center'},
					{id:'action', header: "操作" ,dataIndex: 'MAKER_ID', renderer:myLink}
			      ];	
			      
	function myLink(value){
		var url = "<%=request.getContextPath()%>/sales/marketmanage/activity/ActivityMakerManage/doModPre.do?makerId="+value;
        return String.format(
               "<a href=\""+url+"\">[维护]</a>");
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
    function init(){
    	__extQuery__($('curPage').value);
    }
    function myReset(){
    	$('groupCode').value='';
    	$('groupName').value='';
    	$('status').value='';
    	$('parentGroupCode').value='';
    }
</script>
</body>
</html>

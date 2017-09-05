<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>物料组维护</title>
</head>
<body onload='init();'>
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：售后服务管理>索赔基础数据>售后物料组维护</div>
<form method="POST" name="fm" id="fm">
<div class="form-panel">
		<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
<c:choose>
	<c:when test="${!empty curPage}">
		<input type="hidden" name='curPage' id='curPage' value='${curPage }'/>
	</c:when>
	<c:otherwise>
		<input type="hidden" name='curPage' id='curPage' value='1'/>
	</c:otherwise>
</c:choose>

  	<table class="table_query" border="0">
		<tr>
			<td class="table_query_3Col_label_6Letter" style="text-align:right" nowrap="nowrap">物料组代码：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input name="groupCode" maxlength="30" value="" datatype="1,is_noquotation,30" id="groupCode" type="text" class="middle_txt" />
			</td>
			<td class="table_query_3Col_label_6Letter" style="text-align:right" nowrap="nowrap">物料组名称：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input name="groupName" maxlength="30" value="" datatype="1,is_noquotation,30" id="groupName" type="text" class="middle_txt" />
			</td>
			<td class="table_query_3Col_label_6Letter" style="text-align:right" nowrap="nowrap">物料组状态：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
			<script type="text/javascript">
				genSelBoxExp("status",<%=Constant.STATUS%>,"${status}",true,"","","false",'');
				</script>
				
				
			</td>
		</tr>
		<tr>
			<td class="table_query_3Col_label_6Letter" style="text-align:right" nowrap="nowrap">上级物料组：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input type="text" name="parentGroupCode" size="20" class="middle_txt" id="parentGroupCode" value=""  onfocus="showMaterialGroup('parentGroupCode','','false','','true')" readonly />
				<!-- <input name="button3" type="button" class="normal_btn" onclick="showMaterialGroup('parentGroupCode','','false','','true')" value="..." /> -->
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('parentGroupCode');"/>
			</td>
			<!-- 新增过滤条件是否在产 2012-05-08 hxy -->
			<td class="table_query_3Col_label_6Letter" style="text-align:right" nowrap="nowrap">是否在产：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<select id="forcast_flag" name="forcast_flag" class="u-select">
					<option value="" selected="selected">请选择</option>
					<option value="1">是</option>
					<option value="0">否</option>
				</select>
				<%-- <script type="text/javascript">
					genSelBoxExp("forcast_flag",<%=Constant.IF_TYPE%>,"",false,"short_sel","","false",'');
				</script> --%>
			</td>	
			<td class="table_query_3Col_label_8Letter" style="text-align:right" nowrap="nowrap">是否维护车型组：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<script type="text/javascript">
				      		genSelBoxExp("IF_TYPE",<%=Constant.IF_TYPE%>,"",false,"","","false",'');
				  	  </script>
			</td>				
		</tr>
		<tr>
			<td colspan="6" class="table_query_4Col_input" style="text-align: center">
				<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> &nbsp; 
				<input type="button" class="normal_btn" onclick="myReset();" value="重 置"/> &nbsp; 
				<input name="button2" type="button" class="normal_btn" onclick="window.location.href='<%=request.getContextPath()%>/claim/basicData/ClaimModelMain/materialGroupManageAddPre.do'" value="新 增" />
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
<script type="text/javascript">

	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/claim/basicData/ClaimModelMain/materialGroupManageQuery.json";
				
	var title = null;

	var columns = [
					{header: "序号", align:'center', renderer:getIndex},
					{id:'action', header: "操作" ,dataIndex: 'GROUP_ID', renderer:myLink},
					{header: "物料组代码", dataIndex: 'GROUP_CODE', align:'center'},
					{header: "物料组名称", dataIndex: 'ALL_NAME', align:'center'},
					{header: "物料组级别", dataIndex: 'GROUP_LEVEL', align:'center'},
					{header: "是否在产", dataIndex: 'FORCAST_FLAG', align:'center', renderer:myHref},
					{header: "状态", dataIndex: 'STATUS', align:'center', renderer:getItemValue},
					{header: "更新人", dataIndex: 'NAME', align:'center'},
					{header: "更新时间", dataIndex: 'UPDATE_DATE', align:'center'}
			      ];	
			      
	function myLink(value,meta,record){
		var groupCode=$('#groupCode')[0].value;
		var groupName=$('#groupName')[0].value;
		var status=$('#status')[0].value;
		var parentGroupCode=$('parentGroupCode').value;
		var level = record.data.GROUP_LEVEL;
		var url = "<%=request.getContextPath()%>/claim/basicData/ClaimModelMain/materialGroupManageModPre.do?groupId="+value;
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
		url = url + "&page="+pageTemp;
		if(level==4){
			return String.format(
		               "<a href=\""+url+"\">[维护]</a>");
			}else{
				return String.format(
			               "");
				}
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
    	__extQuery__(1);
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

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<!--jsp:include page="${contextPath}/common/jsp_head_new.jsp" /-->
<title>用户与省份维护</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body>
<div class="wbox">
	<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
	<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 系统管理&gt;权限管理&gt;用户与省份维护</div>

  <form method="post" name="fm" id="fm">
	<input type="hidden" name="userId" id="userId" value="${map.USER_ID}"/>
	<!-- 查询条件 begin -->
	<div class="form-panel">
		<h2>用户与省份维护</h2>
		<div class="form-body">
			<table class="table_query">
				<tr>
					<td class="table_info_3col_label_4Letter right">用户账号：</td>
					<td class="table_info_3col_input">
							${map.ACNT}
					</td>
					<td class="table_info_3col_label_4Letter right">用户名称：</td>
					<td class="table_info_3col_input">
							${map.NAME}
					</td>
					<td class="table_info_3col_label_4Letter"></td>
					<td>
						<input type="button" class="normal_btn" value="新增" onclick="addUserRegion()">&nbsp;
						<input type="button" class="normal_btn" value="返回" onclick="goBack()">
					</td>
				</tr>             
			</table>
			<!-- 查询条件 end -->
		</div>
	</div>
	
	<!--分页 begin -->
		<%--
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->  --%>
	</form>
	<form name="form1" style="display:none">
		<table class="table_list" id="table1" >
			<tr>
				<th align="center">
					<input class="normal_btn" id="deleteBtn" type="button" value="删 除" onclick="deleteConfirm()">
				</th>
			</tr>
		</table>
	</form>
</div>	

<!--页面列表 begin -->
<script type="text/javascript" >

document.form1.style.display = "none";

var HIDDEN_ARRAY_IDS=['form1'];

	var myPage;
//查询路径
	var url = "<%=contextPath%>/sysmng/usemng/UserRegionRelation/queryRelationByUserList.json";
				
	var title = null;

	var columns = [
				{header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"ids\")' />", width:'8%',sortable: false,dataIndex: 'ID',renderer:myCheckBox},
				{header: "省份", dataIndex: 'REGION_NAME', align:'center'}
		      ];
		      
//设置超链接  begin      

	//全选checkbox
	function myCheckBox(value,metaDate,record)
	{
	 	return String.format("<input type='checkbox' name='ids' value='" + value + "'/>");
    }

  function myLink(value,meta,record)
  {
  	  return String.format("<a href='#' onclick='queryRelaction(\""+ value +"\")'>[删除]</a>");
  }    

  function queryRelaction(val)
  {
	  fm.action = "<%=contextPath%>/sysmng/usemng/UserRegionRelation/queryRelationByUserId.do";
	  fm.submit();
  }

	//删除提醒
	function deleteConfirm()
	{
		var chk = document.getElementsByName("ids");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++)
		{        
			if(chk[i].checked)
			{            
				cnt++;
			}
		}
      if(cnt==0)
      {
           MyAlert("请选择要删除省份！");
           return;
      }
		MyConfirm("确认删除？",deleteStanderVip);
	}
	
	//删除操作
	function deleteStanderVip()
	{
		makeNomalFormCall("<%=contextPath%>/sysmng/usemng/UserRegionRelation/deleteUserRegion.json",showDeleteValue,'fm','deleteBtn'); 
	}
	
	//删除回调函数
	function showDeleteValue(json)
	{
		if(json.returnValue == '1')
		{
			MyAlert("删除成功！");
			__extQuery__(1);
		}else
		{
			MyAlert("删除失败！请联系系统管理员！");
		}
	}

	function addUserRegion()
	{
		var userId = document.getElementById("userId").value;
		OpenHtmlWindow('<%=contextPath%>/sysmng/usemng/UserRegionRelation/queryRegion.do?userId='+userId,800,500);
	}
	
	__extQuery__(1);

	function queryExt()
	{
		__extQuery__(1);
	}

	function goBack()
	{
		fm.action = "<%=contextPath%>/sysmng/usemng/UserRegionRelation/userRegionRelationInit.do";
		fm.submit();
	}

  
//设置超链接 end
	
</script>
<!--页面列表 end -->

</body>
</html>
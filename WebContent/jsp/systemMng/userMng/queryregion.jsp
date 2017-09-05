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
	<input type="hidden" name="userId" id="userId" value="<%=request.getAttribute("userId")%>"/>
	<div class="form-panel">
		<h2>用户与省份维护</h2>
		<div class="form-body">
			<!-- 查询条件 begin -->
			<table class="table_query">
				<tr>
					<td class="right" align="right" nowrap>省份名称：</td>
					<td width="200">
						<input class="middle_txt" id="regionName" name="regionName" value="" type="text"/>
					</td>
					<td align="right" nowrap >&nbsp;</td>
					<td align="right" nowrap>
						<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="__extQuery__(1)"/>&nbsp;
					</td>
				</tr>             
			</table>
			<!-- 查询条件 end -->
		</div>
	</div>
	<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end --> 
	</form>
	<form name="form1" style="display:none">
	<table class="table_list" id="table1" >
		<tr>
			<th align="center">
				<input class="normal_btn" id="addBtn" type="button" value="确认 " onclick="setCheckModel()">
			</th>
		</tr>
	</table>
	</form>
	<!--页面列表 begin -->
</div>

<script type="text/javascript" >
document.form1.style.display = "none";

var HIDDEN_ARRAY_IDS=['form1'];

	var myPage;
//查询路径
	var url = "<%=contextPath%>/sysmng/usemng/UserRegionRelation/queryRegionList.json";
				
	var title = null;

	var columns = [
				{header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"codes\")' />", width:'8%',sortable: false,dataIndex: 'REGION_CODE',renderer:myCheckBox},
				{header: "省份", dataIndex: 'REGION_NAME', align:'center'}
		      ];

	function myCheckBox(value,metaDate,record)
	{
	 	return String.format("<input type='checkbox' name='codes' value='" + value + "'/>");
    }
  
	function setCheckModel()
	{
		var chk = document.getElementsByName("codes");
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
           MyDivAlert("请选择要添加省份！");
           return;
      }

      MyDivConfirm("确认添加？",addUserRegion);
	}

	function addUserRegion()
	{
		makeNomalFormCall("<%=contextPath%>/sysmng/usemng/UserRegionRelation/addUserRegion.json",showAddValue,'fm','addBtn'); 
	}

	function showAddValue(json)
	{
		if(json.returnValue==1)
		{
			parentContainer.queryExt();
			parent._hide();
		}
	}
	
  __extQuery__(1);

	
</script>
<!--页面列表 end -->

</body>
</html>
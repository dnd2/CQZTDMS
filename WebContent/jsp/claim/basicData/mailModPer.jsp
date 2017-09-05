<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>单据里程修改</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;单据里程数修改</div>
<form name='fm' id='fm' method="post">
<table class="table_query" >
		 <tr>
            <td class="table_query_2Col_label_7Letter">单据编号：</td>
            <td align="left" nowrap="true">
			<input name="roNo" type="text" class="middle_txt" id="roNo" maxlength="22"/> 
		</td>	
		  <td class="table_query_2Col_label_7Letter">VIN：</td>
            <td align="left" nowrap="true">
			<input name="vin" type="text" class="middle_txt" id="vin" maxlength="20"/> 
		</td>	
     </tr>
	<tr>
 		<td colspan="4" align="center">
 			<input class="normal_btn" type="button" value="查询" onclick="__extQuery__(1);"/> 
 			<input class="normal_btn" type="button" value="新增" onclick="add();"/> 
 		</td>
	</tr>
</table>
<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

</form>
<script type="text/javascript">
	var myPage;
	var url = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/modQuery.json' ;
	
	var title = null ;

	var columns = [
	               {header:'序号',align:'center',renderer:getIndex},
	               {header:'修改单据号',align:'center',dataIndex:'RO_NO'},
	                {header:'VIN',align:'center',dataIndex:'VIN'},
	               {header:'修改前数据',align:'center',dataIndex:'MOD_BEFORE'},
	               {header: "修改后数据",dataIndex: 'MOD_AFTER',align:'center'},
	                {header: "修改时系统数据",dataIndex: 'MOD_SYSTEM',align:'center'},
	               {header:'操作人',align:'center',dataIndex:'NAME'},
	               {header: "操作时间",dataIndex: 'MOD_TIME',align:'center'}
		           	];
		__extQuery__(1);
	function add(){
		fm.action = "<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/modAdd.do";
		fm.submit();
	}
</script>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>二次索赔车型维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;二次索赔车型维护</div>
<form name='fm' id='fm' method="post">
<table class="table_query" >
		 <tr>
            <td class="table_query_2Col_label_7Letter">车型代码：</td>
            <td align="left" nowrap="true">
			<input name="modelCode" type="text" class="middle_txt" id="modelCode" maxlength="22"/> 
		</td>	
		  <td class="table_query_2Col_label_7Letter">车型名称：</td>
            <td align="left" nowrap="true">
			<input name="modelName" type="text" class="middle_txt" id="modelName" maxlength="20"/> 
		</td>	
     </tr>
      <tr>
            <td class="table_query_2Col_label_7Letter">是否有效：</td>
            <td align="left" nowrap="true" colspan="3">
			<script type="text/javascript">
        	genSelBoxExp("status",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false","");
        	</script>
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
	var url = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/modelPriceQuery.json' ;
	
	var title = null ;

	var columns = [
	               {header:'序号',align:'center',renderer:getIndex},
	               {header:'车型代码',align:'center',dataIndex:'MODEL_CODE'},
	               {header:'车型名称',align:'center',dataIndex:'MODEL_NAME'},
	               {header:'工时单价',align:'center',dataIndex:'MODEL_PRICE'},
	               {header:'新增人',align:'center',dataIndex:'NAME'},
	               {header:'新增时间',align:'center',dataIndex:'CREATE_TIME'},
	               {header:'更新人',align:'center',dataIndex:'NAME1'},
	               {header:'更新时间',align:'center',dataIndex:'UPDATE_TIME'},
	               {header: "是否有效",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
	               {header:'操作',align:'center',dataIndex:'ID',renderer:myLink}
		           	];
		__extQuery__(1);
	function myLink(value,meta,record){
		return String.format("<a href='<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/modelPriceAdd.do?type=1&id="+value+"'>[修改]</a>");
	}
	function add(){
		fm.action = "<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/modelPriceAdd.do?type=0";
		fm.submit();
	}
</script>
</body>
</html>

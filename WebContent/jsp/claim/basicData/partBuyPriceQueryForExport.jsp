<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<title>demo</title>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>供应商索赔价格维护</title>

</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;供应商索赔价格查询</div>
<form name='fm' id='fm' method="post">
<table class="table_edit" >
	<tr>
 		  <td width="20%" align="right" >件号：</td>
	      <td  width="30%"><input class="middle_txt" type="text" name="part_code" /></td>
	      <td width="20%" align="right" >配件编码：</td>
	      <td width="30%" ><input class="middle_txt" type="text" name="part_oldCode" /></td>
	</tr>
	
	 <tr>
	      <td width="20%" align="right" >供应商名称：</td>
	      <td width="30%"><input class="middle_txt" type="text" name="vender_name" /></td>
	      <td width="20%" align="right" >配件名称：</td>
	      <td width="30%"><input class="middle_txt" type="text" name="part_name" /></td>
      </tr>
    <tr>
      <td align="right" >供应商编码：</td>
      <td><input class="middle_txt" type="text"  name="vender_code"/></td>
      <td align="right" >是否有效：</td>
      <td><script type="text/javascript">
						 genSelBoxExp("STATUS",<%=Constant.STATUS%>,"",true,"short_sel","","false",'');
				    </script></td>
	  </tr>
	<tr>
		<td align="right" >修改人：</td>
        <td colspan="3"><input class="middle_txt" type="text"  name="spy_name"/></td>
	</tr>
    <tr>
    	<td  align="center" colspan="4" >
    	 <input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询"  onclick="__extQuery__(1);"/>&nbsp;&nbsp;
    	  <input class="normal_btn"  type="hidden" value="导 出" onclick="exportExcel();"/>
		 </td>    
	</tr>	
</table>
<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

</form>
<script type="text/javascript">
	var myPage;
		var url = "<%=contextPath%>/claim/basicData/ClaimVenderPrice/claimVenderPriceQueryForUpdate.json";
		var title = null;
		
		var columns = [
					{header: "序号",sortable: false,align:'center',renderer:getIndex},
					{header: "件号",		dataIndex: 'PART_CODE',align:'center'},
					{header: "配件编码",	dataIndex: 'PART_OLDCODE'},	
					{header: "配件名称",	dataIndex: 'PART_NAME'},	
					{header: "供应商编码",dataIndex: 'VENDER_CODE'},	
					{header: "供应商名称",dataIndex: 'VENDER_NAME'},			
					{header: "供应索赔价",dataIndex: 'CLAIM_PRICE'},
					{header: "材料索赔系数",	dataIndex: 'PART_XS',align:'center'}, 
					{header: "工时索赔系数",	dataIndex: 'LABOUR_XS',align:'center'},
					{header: "修改人",	dataIndex: 'SPY_NAME',align:'center'}, 
					{header: "修改日期",	dataIndex: 'CLAIM_DATE',align:'center'},
					{header: "是否有效",	dataIndex: 'STATUS',align:'right',renderer:getItemValue}
			      ];
	 function exportExcel(){
       fm.action="<%=contextPath%>/claim/basicData/ClaimVenderPrice/exportExcel.do";
       fm.submit();
	 }
</script>
</body>
</html>

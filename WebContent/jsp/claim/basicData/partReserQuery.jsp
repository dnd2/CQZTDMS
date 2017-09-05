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
<title>旧件库区库位维护</title>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;旧件库区库位维护</div>
<form name='fm' id='fm' method="post">
<table class="table_edit" >
	<tr>
	      <td width="20%" align="right" >配件代码：</td>
	      <td width="30%" ><input class="middle_txt" type="text" name="part_code" /></td>
	      <td width="20%" align="right" >配件名称：</td>
	      <td width="30%"><input class="middle_txt" type="text" name="part_name" /></td>
	</tr>
	<tr>
	      <td width="20%" align="right" >库区：</td>
	      <td width="30%" ><input class="middle_txt" type="text" name="local_war_house" /></td>
	      <td width="20%" align="right" >货架：</td>
	      <td width="30%"><input class="middle_txt" type="text" name="local_war_shel" /></td>
	</tr>
	<tr>
	      <td width="20%" align="right" >层数：</td>
	      <td width="30%" colspan="3"><input class="middle_txt" type="text" name="local_war_layer" /></td>
	</tr>	
	
      <!-- 
    <tr>
      <td align="right" >是否有效：</td>
      <td><script type="text/javascript">
						 genSelBoxExp("STATUS",<%=Constant.STATUS%>,"",true,"short_sel","","false",'');
				    </script></td>
	  </tr>
       -->
       
    <tr>
    	<td  align="center" colspan="4" >
    	 <input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询"  onclick="__extQuery__(1);"/>&nbsp;&nbsp;
		 <input class="normal_btn" type="reset" value="重置"/>
		 </td>  
	</tr>	
</table>
<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

</form>
<script type="text/javascript">
	var myPage;
		var url = "<%=contextPath%>/claim/basicData/ClaimVenderPrice/oldPartReserQuery.json";
		var title = null;
		
		var columns = [
					{header: "序号",sortable: false,align:'center',renderer:getIndex},
					{header: "配件代码",	dataIndex: 'PART_CODE'},	
					{header: "配件名称",	dataIndex: 'PART_NAME'},		
				 	{header: "库区",	dataIndex: 'LOCAL_WAR_HOUSE',align:'center'}, 
					{header: "货架",	dataIndex: 'LOCAL_WAR_SHEL',align:'center'},
					{header: "层数",	dataIndex: 'LOCAL_WAR_LAYER',align:'right'},
					{header: "操作",sortable: false,dataIndex: 'PART_ID',align:'center',renderer:mylink}
			      ];
			      __extQuery__(1);
function mylink(value,meta,record){
	    return String.format(
         "<a href=\"#\"  onclick='checkPri(\""+value+"\")'>[修改]</a>");
	}

function checkPri(str){
	 fm.action="<%=contextPath%>/claim/basicData/ClaimVenderPrice/ExchangeReser.do?id="+str;
 	fm.submit();
}
   
</script>
</body>
</html>

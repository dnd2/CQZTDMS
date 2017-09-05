<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件三包期查询</title>
</head>
<body>
<form name='fm' id='fm'>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;配件三包期查询</div>
   <table  class="table_query">
          <tr>
            <td class="table_query_2Col_label_6Letter">配件编码：</td>
            <td><input name="partCode" type="text" id="partCode" class="middle_txt"/></td>
            <td class="table_query_2Col_label_6Letter">配件名称：</td>
          	<td><input name="partName" type="text" id="partName" class="middle_txt"/></td>
           </tr>
          <tr id="three">
          	<td class="table_query_2Col_label_6Letter">三包规则代码：</td>
          	<td><input name="threeCode" type="text" id="threeCode" class="middle_txt" /></td>
          	<td class="table_query_2Col_label_6Letter">三包规则名称：</td>
          	<td><input name="threeName" type="text" id="threeName" class="middle_txt"/></td>
          </tr>
		  <tr>    
		  <td colspan="4" align="center"><input class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1);"/></td>
          </tr>
       
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
</form>  
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/ClaimLaborMain/partThreeBagsQuery11.json?COMMAND=1";
	var title = null;
	if("${isDealer}"=='true'){
		document.getElementById("three").style.display="none";
		var columns = [
					{header: "序号",sortable: false,align:'center',renderer:getIndex},
					{header: '备件编码',align:'center',dataIndex:'PART_CODE'},
					{header: "配件名称",sortable: false,dataIndex: 'PART_NAME',align:'center'},				
					{header: "三包期(月)",sortable: false,dataIndex: 'CLAIM_MONTH',align:'center'},
					{header: "三包期(里程)",sortable: false,dataIndex: 'CLAIM_MELIEAGE',align:'center'}
			      ];
	}else{
		var columns = [
						{header: "序号",sortable: false,align:'center',renderer:getIndex},
						{header: '备件编码',align:'center',dataIndex:'PART_CODE'},
						{header: "配件名称",sortable: false,dataIndex: 'PART_NAME',align:'center'},				
						//{header: "索赔价",sortable: false,dataIndex: 'PRICE',align:'center'},
						{header: "三包期(月)",sortable: false,dataIndex: 'CLAIM_MONTH',align:'center'},
						{header: "三包期(里程)",sortable: false,dataIndex: 'CLAIM_MELIEAGE',align:'center'},
						{header: "三包规则代码",sortable: false,dataIndex: 'RULE_CODE' ,align:'center'},
						{header: "三包规则名称",sortable: false,dataIndex: 'RULE_NAME' ,align:'center'}
						//{header: "授权级别",sortable: false,dataIndex: 'APPROVAL_LEVEL' ,align:'center'},
					//	{header: "车型查询",sortable: false,dataIndex: 'ID' ,align:'center',renderer:mySelect}
				      ];
	}


//设置超链接
function mySelect(value,meta,record){
 		return String.format("<a href=\"#\" onclick='selbyid(\""+record.data.ID+"\")'>[查看]</a>");
}
//详细页面
function selbyid(value){
	OpenHtmlWindow('<%=contextPath%>/claim/basicData/ClaimLaborMain/partThreeBagsDetailInit.do?ID='+value,900,500);
}
</script>
</body>
</html>
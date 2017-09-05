<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
	Long userId = (Long)request.getAttribute("logonUser");
	
%>

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件工时关系维护</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<form name='fm' id='fm' method="post">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;二次索赔工时维护</div>
  <table class="table_query">
   <tr>            
        <td style="color: #252525;width: 115px;text-align: right">配件代码：</td>            
        <td>
			<input  class="middle_txt" id="PART_CODE"  name="PART_CODE" type="text" />
        </td>
        <td class="table_query_3Col_label_6Letter">配件名称：</td>
        <td><input type="text" name="PART_NAME" id="PART_NAME"  class="middle_txt" value=""/></td>
     </tr>
     <tr>            
        <td style="color: #252525;width: 115px;text-align: right">工时代码：</td>            
        <td>
			<input  class="middle_txt" id="LABOUR_CODE"  name="LABOUR_CODE" type="text" />
        </td>
        <td class="table_query_3Col_label_6Letter">工时名称：</td>
        <td><input type="text" name="LABOUR_NAME" id="LABOUR_NAME"  class="middle_txt" value=""/></td>
     </tr>
     <tr>  
        <td colspan="4" align="center">
        <input  class=normal_btn onclick=__extQuery__(1); align="right" value=查询 type=button name=button/> 
  		</td>
    </tr>   
    <br/> 
    <tr>
  
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/oldPart/ClaimOldPartOutStorageManager/queryPart.json";
	var title = null;
	
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "配件代码",sortable: false,dataIndex: 'PART_CODE',align:'center'},				
				{header: "配件名称",sortable: false,dataIndex: 'PART_NAME',align:'center'},
				{header: "工时代码",sortable: false,dataIndex: 'LABOUR_CODE',align:'center'},				
				{header: "工时名称",sortable: false,dataIndex: 'LABOUR_NAME',align:'center'},
				{header: "工时单价",sortable: false,dataIndex: 'LABOUR_PRICE',align:'center'},				
				{header: "工时系数",sortable: false,dataIndex: 'LABOUR_HOURS',align:'center'},
				{header: "新增/更新人",sortable: false,dataIndex: 'NAME',align:'center'},				
				{header: "新增/更新时间",sortable: false,dataIndex: 'UPDATE_TIME',align:'center',renderer:formatDate},
				{header: "操作",sortable: false,dataIndex: 'PART_CODE',align:'center',renderer:MyLinjk}	
		      ];
	__extQuery__(1);
	
  function formatDate(value,meta,record) {
	 if (value==""||value==null) {
	 	if(record.data.CREATE_TIME==""||record.data.CREATE_TIME==null){
	 		return "";
	 	}else{
		 	return record.data.CREATE_TIME;
		 }
	 }else {
		return value;
	 }
   }
	function MyLinjk(value,metaDate,record){
		return String.format("<input  class=normal_btn onclick=mod('"+value+"');  value=修改 type=button name=button/> ");
	}
	function mod(partCode){
		 fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/modfiyPer.do?partCode="+partCode;
       	 fm.submit();
	}
	
	
</script>
  <table align="center">
  <tr align="center">
  </tr>
  </table>
</form>
</body>
</html>
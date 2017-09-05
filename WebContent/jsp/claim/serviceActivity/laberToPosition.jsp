<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	Long userId = (Long)request.getAttribute("logonUser");
	
%>

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>首页新闻查询</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<form name='fm' id='fm' method="post">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;售后部位&gt;配置管理</div>
  <table>
  <tr><td><input type="hidden"  id="largess_type" name ="largess_type" value="${largess_type}"/><input type="hidden"  id="activityId" name ="activityId" value="${activityId}"/> </td>
  </tr>
   <tr>            
        <td style="color: #252525;width: 115px;text-align: right">工时代码：</td>            
        <td>
			<input  class="middle_txt" id="PART_CODE"  name="PART_CODE" type="text" datatype="1,is_null,27"/>
        </td>
        <td class="table_query_3Col_label_6Letter">工时名称：</td>
        <td><input type="text" name="PART_NAME" id="PART_NAME" datatype="1,is_null,30" class="middle_txt" value=""/></td>
        <td class="table_query_3Col_label_6Letter">&nbsp;</td>
        <td>&nbsp;</td>     
    </tr>   
    <br/> 
    <tr><td colspan="4" align="right"><input  class=normal_btn onclick=__extQuery__(1); align="right" value=查询 type=button name=button/> </td>
  
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/serviceActivity/ServiceActivityManage/open_laber.json";
	var title = null;
	
	var columns = [
				{id:'action',header: "选择<input type='checkbox' name='checkAll' onclick='selectAll(this,\"checkId\")' />", width:'3%',align:'center',sortable: false,dataIndex: 'ID',renderer:myCheckBox},
				{header: "工时代码",sortable: false,dataIndex: 'LABOUR_CODE',align:'center'},				
				{header: "工时名称",sortable: false,dataIndex: 'CN_DES',align:'center'}
		      ];
	__extQuery__(1);

	function myCheckBox(value,metaDate,record){
		var input2;
		var check = record.data.A;
		if(check==1){
			input2='<input type="checkbox" id="checkId" name="checkId" value="'+value+'" checked/>';
		}
		else{
			input2='<input type="checkbox" id="checkId" name="checkId" value="'+value+'" />';
			}
		input2 = input2 + '<input type="hidden" id="checkIds" name="checkIds" value="'+value+'" />'	;	
		return String.format(input2);
	}
	
	
	function add()
	{
		makeNomalFormCall('<%=request.getContextPath()%>/claim/serviceActivity/ServiceActivityManage/add_laber.json',updateBack,'fm','');
	}
	function updateBack(json){
			  _hide();

	}
	
	
	
</script>
  <table align="center">
  <tr align="center">
  <td>
  <c:if test="${is_add == 1}">
  	<input type="button" disabled="disabled" class=normal_btn value="确定" onClick="add();"/>
  </c:if>
  <c:if test="${is_add != 1}">
  	<input type="button" class=normal_btn value="确定" onClick="add();"/>
  </c:if>
  </td>
  </tr>
  </table>
</form>
</body>
</html>
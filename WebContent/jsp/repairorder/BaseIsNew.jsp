<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
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
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;新增配件查询</div>
  <table class="table_query">
  <tr id="err" style="display: none"><td><span style="color: red;">版本号已经更新</span></td></tr>
  <tr><td><input type="hidden"  id="pid" name ="pid" value="${pId}"/> </td>
  </tr>
   <tr>            
        <td style="color: #252525;width: 115px;text-align: right">配件代码：</td>            
        <td>
			<input  class="middle_txt" id="PART_CODE"  name="PART_CODE" type="text" datatype="1,is_null,27"/>
        </td>
        <td class="table_query_3Col_label_6Letter">配件名称：</td>
        <td><input type="text" name="PART_NAME" id="PART_NAME" datatype="1,is_null,30" class="middle_txt" value=""/></td>
     </tr>
     <tr>            
        <td style="color: #252525;width: 115px;text-align: right">新增时间：</td>            
        <td colspan="3"> 
			<input name="create_start" type="text" class="short_time_txt" id="create_start" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'create_start', false);" />  	
             &nbsp;至&nbsp; <input name="create_end" type="text" class="short_time_txt" id="create_end" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'create_end', false);" /> 
	 	</td>
     </tr>
     <tr>  
        <td colspan="4" align="center"><input  class=normal_btn onclick=__extQuery__(1); align="right" value=查询 type=button name=button/> 
        
         <input type="button" style="width: 59px;" class=normal_btn value="已查看" onClick="add();"/>
          <input type="button" style="width: 59px;" class=normal_btn value="导出" onClick="exportTo();"/>
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
	var url = "<%=request.getContextPath()%>/repairOrder/RoMaintainMain/BaseToPositionIsNew.json";
	var title = null;
	
	var columns = [
				{id:'action',header: "选择<input type='checkbox' name='checkAll' onclick='selectAll(this,\"checkId\")' />", width:'3%',align:'center',sortable: false,dataIndex: 'PART_ID',renderer:myCheckBox},
				{header: "配件代码",sortable: false,dataIndex: 'PART_CODE',align:'center'},				
				{header: "配件名称",sortable: false,dataIndex: 'PART_NAME',align:'center'},
				{header: "新增时间",sortable: false,dataIndex: 'CREATE_DATE',align:'center'}
		      ];
	__extQuery__(1);

	function myCheckBox(value,metaDate,record){
		var input2;
		var check = record.data.A;
			input2='<input type="checkbox" id="checkId" name="checkId" value="'+value+'" />';
		return String.format(input2);
	}
	
	
	function add()
	{
		makeNomalFormCall('<%=contextPath%>/repairOrder/RoMaintainMain/updatebaseisnew.json',updateBack,'fm','');
	}
	function updateBack(json){
		 MyAlert("配件查看成功！");
	    __extQuery__(1);
	}
	
	function exportTo(){
       fm.action="<%=contextPath%>/repairOrder/RoMaintainMain/exportExcel.do";
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
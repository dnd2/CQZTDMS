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
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;售后KD&gt;KD管理</div>
  <table>
  <tr><td><input type="hidden"  id="kdid" name ="kdid" value="${kdid}"/> </td>
  <tr id="err" style="display: none"><td><span style="color: red;">版本号已经更新</span></td></tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/repairOrder/RoMaintainMain/PositionTOKd.json";
	var title = null;
	
	var columns = [
				{id:'action',header: "选择<input type='checkbox' name='checkAll' onclick='selectAll(this,\"checkId\")' />", width:'3%',align:'center',sortable: false,dataIndex: 'POS_ID',renderer:myCheckBox},
				{header: "部位代码",sortable: false,dataIndex: 'POS_CODE',align:'center'},				
				{header: "部位名称",sortable: false,dataIndex: 'POS_NAME',align:'center'}
		      ];
	__extQuery__(1);

	function myCheckBox(value,metaDate,record){
		var input2;
		var check = record.data.A;
		if(check==1){
			input2='<input type="checkbox" id="checkId" name="checkId" value="'+value+'-'+record.data.VER+'" checked/>';
		}
		else{
			input2='<input type="checkbox" id="checkId" name="checkId" value="'+value+'-'+record.data.VER+'" />';
			}
		return String.format(input2);
	}
	
	
	function add()
	{
		makeNomalFormCall('<%=contextPath%>/repairOrder/RoMaintainMain/judepositiontokd.json',updateBack,'fm','');
	}
	function updateBack(json){
	    if(json.success != null && json.success == "yes")
	    {
	    
	    	document.getElementById('err').style.display = '';
	    }else
	    {
	    	$('fm').action = "<%=request.getContextPath()%>/repairOrder/RoMaintainMain/insertpositiontokd.do"
	   		$('fm').submit();
	    	_hide();
	    }
		
	    
	}
</script>
  <table align="center">
  <tr align="center">
  <td>
  <input type="button" value="确定" onClick="add();"/>
  </td>
  </tr>
  </table>
</form>
</body>
</html>
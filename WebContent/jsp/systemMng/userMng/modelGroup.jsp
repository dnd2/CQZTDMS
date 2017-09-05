<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车型组</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 后服务管理&gt;索赔基础数据&gt;工时单价维护&gt;车型组</div>
  <form method="post" name="fm" id="fm">
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
<script type="text/javascript" >
document.form1.style.display = "none";
var HIDDEN_ARRAY_IDS=['form1'];
var myPage;
//查询路径
	var url = "<%=contextPath%>/sysmng/usemng/UserRegionRelation/queryModelGroupList.json";			
	var title = null;
	var columns = [
				{header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"codes\")' />", width:'8%',sortable: false,dataIndex: 'WRGROUP_ID',renderer:myCheckBox},
				{header: "车型组", dataIndex: 'WRGROUP_NAME', align:'center' }
		      ];

	function myCheckBox(value,metaDate,record){
		var hi1 = "<input type=\"checkbox\" name=\"codes\" value='" + value + "'/>";
		var hi2 = "<input type=\"hidden\" name=\"names\" value='" +record.data.WRGROUP_NAME+ "'/>";
	 	return String.format(hi1+hi2);
    }
  
	function setCheckModel(){
		var chk = document.getElementsByName("codes");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++){
			if(chk[i].checked){            
				cnt++;
			}
		}
        if(cnt==0){
            MyDivAlert("请选择要添加车型组!");
            return;
        }
       
        var regionCodes = document.getElementsByName("codes");
	    var regionNames = document.getElementsByName("names");
		var codes = new Array();
		var names = new Array();
		var j=0;
		for(var i=0;i<regionCodes.length;i++){
			if(regionCodes[i].checked){       
				codes[j] = regionCodes[i].value;
				names[j] = regionNames[i].value;
				j++;
			}
		}
		parent._hide();
		parentContainer.viewModelGroup(codes,names);
    }
    
  	__extQuery__(1);
</script>
<!--页面列表 end -->

</body>
</html>
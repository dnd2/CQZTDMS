<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<!--jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/my-grid-pager.js"></script-->
<title>用户与省份维护</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
		__extQuery__(1);
	}

</script>
</head>
<body>
<div class="wbox">
   <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
   <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 系统管理&gt;权限管理&gt;用户与省份维护</div>

    <form method="post" name="fm" id="fm">
    <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
    <!-- 查询条件 begin -->
    <div class="form-panel">
       <h2>用户与省份维护</h2>
       <div class="form-body">
          <table class="table_query">
            <tr>
                    <td class="right" align="right" nowrap>用户账号：</td>
                    <td>
                      <input class="middle_txt" id="acnt" name="acnt" value="" type="text"/>
                    </td>
                    <td  class="right" align="right" nowrap>用户名称：</td>
                    <td>
                <input class="middle_txt" id="name" name="name" value="" type="text"/>
                    </td>
                    <td align="right" nowrap >&nbsp;</td>
                    <td align="right" nowrap>
                      <input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="__extQuery__(1)"/>&nbsp;
                    </td>
                  </tr>             
          </table><!-- 查询条件 end -->
       </div>
    </div>
    <!--分页 begin -->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
    <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end --> 
  </form>   
</div>  

<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
	var url = "<%=contextPath%>/sysmng/usemng/UserRegionRelation/queryUserRegionRelation.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'NUM', align:'center'},
				{header: "用户账号", dataIndex: 'ACNT', align:'center'},
				{header: "用户名称", dataIndex: 'NAME', align:'center'},
				{header: "用户状态",sortable: false,dataIndex: 'USER_STATUS' ,align:'center',renderer:getItemValue},
				{header: "操作",sortable: false,dataIndex: 'USER_ID',renderer:myLink ,align:'center'}
		      ];
		      
//设置超链接  begin      

  function myLink(value,meta,record)
  {
  	  return String.format("<a href='#' onclick='queryRelaction(\""+ value +"\")'>[查看]</a>");
  }    

  function queryRelaction(val)
  {
	  fm.action = "<%=contextPath%>/sysmng/usemng/UserRegionRelation/queryRelationByUserId.do?userId="+val;
	  fm.submit();
  }
  
//设置超链接 end
	
</script>
<!--页面列表 end -->

</body>
</html>
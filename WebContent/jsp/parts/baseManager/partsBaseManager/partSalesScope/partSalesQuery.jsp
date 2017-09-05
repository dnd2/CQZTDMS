<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>销售人员区域范围设置</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
		__extQuery__(1);
	}

</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp; 当前位置：
		基础信息管理 &gt; 配件基础信息维护 &gt; 配件销售人员区域维护</div>

  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">
 		<tr>
           <%-- <td width="20%" align="right">用户账号：</td>
            <td width="30%">
				<input class="middle_txt" id="acnt" name="acnt" value="" type="text"/>
            </td>--%>
               <td width="10%" align="right">销售员：</td>
               <td width="20%">
                   <input class="middle_txt" id="name" name="name" value="" type="text"/>
               </td>
            <td  width="10%" align="right">是否有效：</td>
	        <td width="20%">
	          <script type="text/javascript">
		       genSelBoxExp("USER_STATUS",<%=Constant.STATUS%>,"<%=Constant.STATUS_ENABLE %>",true,"short_sel","","false",'');
		      </script>
	       </td>
            <td width="10%"  align="right" >人员类型：</td>
            <td width="20%">
                <select name="userType" id="userType" class="short_sel">
                    <option value="">-请选择-</option>
                    <option value="3" selected="selected" >配件销售人员</option>
                    <option value="5">精品销售员</option>
                    <option value="6">广宣销售员</option>
                </select>
            </td>
            <td align="center" colspan="4" >
            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="__extQuery__(1)"/>&nbsp;
            </td>
          </tr>             
  </table>
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>

<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
	var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesScopeManager/queryPartSales.json";
				
	var title = null;

	var columns = [
				{header: "序号", align:'center',renderer:getIndex},
                {header: "操作",sortable: false,dataIndex: 'USER_ID',renderer:myLink ,align:'center'},
				//{header: "用户账号", dataIndex: 'ACNT', align:'center'},
				{header: "销售员", dataIndex: 'NAME', align:'center'},
                {header: "负责区域", dataIndex: 'LO', style:'text-align:left;'},
				{header: "用户类型", dataIndex: 'USER_TYPE', align:'center',renderer:getUserType},
				{header: "用户状态",sortable: false,dataIndex: 'USER_STATUS' ,align:'center',renderer:getItemValue}
		      ];
		      
//设置超链接  begin      

  function myLink(value,meta,record)
  {
	  var userType = record.data.USER_TYPE;
  	  return String.format("<a href='#' onclick='queryPartSalesByUserId(\""+ value +"\",\""+ userType +"\")'>[负责区域]</a>");
  }

  //获取人员类型
  function getUserType(value,meta,record)
  {
	  var str
	  if(value == "5"){
		  str = "整车销售人员";
	  } if (value == "6"){
          str = "配件索赔员";
      } if (value == "3"){
          str = "配件销售人员";
      }
  	  return String.format(str);
  }    

  function queryPartSalesByUserId(val,userType)
  {
	  fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesScopeManager/queryPartSalesByUserId.do?userId="+val+"&userType="+userType;
	  fm.submit();
  }
  
//设置超链接 end
	
</script>
<!--页面列表 end -->

</body>
</html>
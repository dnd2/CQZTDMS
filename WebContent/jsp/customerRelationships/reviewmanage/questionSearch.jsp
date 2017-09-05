<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>  
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript">
function doInit()
{
	loadcalendar();
}
</script>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>问卷查询</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户化关系管理 &gt; 回访管理 &gt;问卷管理
</div>
  <form method="post" name="fm" id="fm">
 <TABLE class=table_query>
  <tr class="">
    <td width="15%" align="right" nowrap="nowrap" class="style5">问卷类型: </td>
    <td width="14%" class="style4">
     <script type="text/javascript">
           genSelBoxExp("QR_TYPE",<%=Constant.QR_TYPE%>,null,true,"short_sel","","false",'');
	</script>
   </td>
    <td width="16%" align="right"> 创建时间:      </td>
    <td width="20%"><input name="checkSDate" class="short_txt" id="checkSDate" maxlength="10" group="checkSDate,checkEDate" datatype="1,is_date,10" readonly />
      <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" type="button" value=" " />
 至 
<input name="checkEDate" class="short_txt" id="checkEDate" maxlength="10" group="checkSDate,checkEDate" datatype="1,is_date,10" readonly />
<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" type="button" value=" " /></td>
    
  </tr>
  <tr>
    <td align="right" class=""> 问卷名称:</td>
    <td class="">
      <input  id="QR_NAME" class="Wdate" style="WIDTH: 120px" name="QR_NAME"  /></td>
    <td width="7%" align="right">有效:
      </td>
    <td width="28%">
     <script type="text/javascript">
           genSelBoxExp("QR_STATUS",<%=Constant.QR_STATUS%>,null,true,"short_sel","","false",'');
	</script>
   </td>
    
  </tr>
<TBODY>
  <TR align="center">
    <td colspan="4" class="">
   	 <span class="table">
      <input name="queryBtn" id="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查询" />
      </span></td>
    </TR>
  </TBODY></TABLE>
<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
 <!--分页 end --> 
</form> 
<br/>
<!--页面列表 begin -->
<script type="text/javascript" >
var myPage;
//查询路径
	var url = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewSearch/questionairManageQuery.json";
	var title = null;
	var columns = [
				{header: "问卷名称",sortable: false,dataIndex: 'QR_NAME',align:'center'}, 
				{header: "是否有效 ",sortable: false,dataIndex: 'QR_STATUS',align:'center'},
				{header: "类型",sortable: false,dataIndex: 'QR_TYPE',align:'center'},
				{header: "创建时间",sortable: false,dataIndex: 'CREATE_DATE',align:'center',renderer:formatDate},
				{header: "浏览",sortable: false,dataIndex: 'QR_ID',renderer:myHandler,align:'center'}
		      ];
	
	//格式 化日期	      
	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	} 	      
    
	//浏览超链接		      
  	function myHandler(value,meta,record){
		return '<a href="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/seeQuestionair.do?QR_ID='+record.data.QR_ID+'">[浏览]</a>' ;
	}
    
</script>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c" %>  
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
<title>知识库查询</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt;知识库管理&gt;知识库查询
</div>
  <form method="post" name="fm" id="fm">
 <table class=table_query>
  <tbody>
    <tr>
      <td height="21" colspan="5" align="center"><table border="0" cellspacing="1" cellpadding="0" width="100%">
        <tbody>
        </tbody>
      </table></td>
    </tr>
    <tr>
      <td width="14%" align="right">关键字：</td>
      <td colspan="2" align="left"><input id="KG_TOPIC" name="KG_TOPIC" /> (标题或内容简写) </td>
      <td width="14%" align="right">搜索区域：<label for="chkText"></label></td>
      
      <td colspan="2" width="28%"><label for="chkAtt">
        <input id="chkTitle" name="chkTitle" checked="checked" type="checkbox" />标题
		<input id="chkText" name="chkText" checked="checked" type="checkbox" />内容</label>
	  </td>
    </tr>
    <tr>
 
              <td width="14%" align="right">类型:</td>
              <td width="14%" align="left">
                <select name="KG_TYPE" id="KG_TYPE" style='width:160px;' >
                <option selected value=''>-请选择-</option>
                <c:forEach items="${typeList}" var="varType">
                    <option value="${varType.TYPE_ID}">${varType.TYPE_NAME}</option>
                </c:forEach>
                </select>
              </td>    
   
      <td width="14%" align="right">类别：      </td>
      <td width="14%" align="left">
       ${selectBox}
	  </td>
      <td width="14%" align="left"><div align="right">状态：</div></td>
      <td>
     	 <script type="text/javascript">
			genSelBoxExp("KG_STATUS",<%=Constant.STATE_MANAGE%>,"",true,"short_sel","","true",'');
		 </script>
      </td>
    </tr>
    <tr>
      <td colspan="5"></td>
    </tr>
    <tr>
      <td colspan="5">
        <div align="center">
        <input class="normal_btn" type="button" name="button1" value="查询" onclick="__extQuery__(1);"/>
      </div></td>
    </tr>
  </tbody>
</table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form> 
<!-- 查询条件 end -->
<br/>
<!--页面列表 begin -->
<script type="text/javascript" >
var myPage;
//查询路径
	var url = "<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/knowledgelibrarySearchDetail.json";
	var title = null;
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "资料标题",sortable: false,dataIndex: 'KG_ID',renderer:myLink,align:'center'},
				{header: "知识库类型 ",sortable: false,dataIndex: 'TYPE_NAME',align:'center'},
				{header: "类别",sortable: false,dataIndex: 'KIND',align:'center',renderer:getItemValue}, 
				{header: "签发时间 ",sortable: false,dataIndex: 'KG_SIGN_TIME',align:'center'}
		      ];
		      
	//修改的超链接
	function myLink(value,meta,record)
	{
  		return String.format("<a href='#' onclick='knowledgeLibShow(\""+record.data.KG_ID+"\")'>"+record.data.KG_TOPIC+"</a>");
	}
	//修改的超链接设置
	function knowledgeLibShow(kgid){
		fm.action = '<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/knowledgeLibShow.do?kgid=' + kgid;
	 	fm.submit();
	}
</script>  
  </body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>投诉信息导入预览</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		__extQuery__(1);  //查询投诉信息导入的临时表
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>客户关系管理>客户投诉管理>客户投诉导入(总部)</div>
<form name="fm" id="fm">
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<form name="fm1" id="fm1"> 
	<table align="center" class="table_query" >
      	<tr  class="table_list_row">
        	<td width="53%">
        		<input type="button" class="normal_btn" onclick="sure()" value="确认" name="ok" id="ok"/>
        		<input type="button" class="normal_btn" onclick="cancle()" value="取消"/>
        		<input type="button" class="long_btn" onclick="downError()" value="下载错误记录"/>
        	</td>
      	</tr>
	</table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/export/ComplaintExpImp/queryImpTmp.json";
				
	var title = null;

	var columns = [
				{header: "投诉编号", dataIndex: 'COMP_CODE', align:'center'},
				{header: "客户名称",sortable: false,dataIndex: 'LINK_MAN',align:'center'},
				{header: "联系电话", dataIndex: 'TEL', align:'center'},
				{header: "所购车型", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "大区", dataIndex: 'ORG_NAME', align:'center'},
				{header: "省份", dataIndex: 'PROVINCE', align:'center',renderer:getRegionName},
				{header: "投诉经销商", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "投诉来源", dataIndex: 'COMP_SOURCE', align:'center',renderer:getItemValue},
				{header: "投诉等级", dataIndex: 'COMP_LEVEL', align:'center',renderer:getItemValue},
				{header: "投诉类型", dataIndex: 'COMP_TYPE', align:'center',renderer:getItemValue},
				{header: "投诉时间", dataIndex: 'CREATE_DATE', align:'center'}
				//{id:'action',header: "操作",sortable: false,dataIndex: 'COMP_ID',renderer:myLink ,align:'center'}
		      ];
	//导入确认 从临时表删除增加到业务表
	function sure() {
		var url = "<%=contextPath%>/customerRelationships/export/ComplaintExpImp/saveImp.json";
		makeNomalFormCall(url,callback,'fm1');
	}
	//导入成功,跳转到导入初始页面
	function callback() {
		MyAlert('导入成功');
		var url = '<%=request.getContextPath()%>/customerRelationships/export/ComplaintExpImp/complaintImpInit.do';
		window.location = url;
	}
	//导入取消 删除临时表
	function cancle() {
		fm.action = "<%=contextPath%>/customerRelationships/export/ComplaintExpImp/delImpTmp.do";
		fm.submit();
	}
	//下载错误的导入记录

	function downError() {
		fm.action = "<%=contextPath%>/customerRelationships/export/ComplaintExpImp/expError.do";
		fm.submit();
	}
</script>
<!--页面列表 end -->
</body>
</html>
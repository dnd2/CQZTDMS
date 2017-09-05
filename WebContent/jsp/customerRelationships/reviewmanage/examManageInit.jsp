<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>  
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>我的客户回访</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户化关系管理 &gt; 回访管理 &gt;题库管理
</div>
  <form method="post" name="fm" id="fm">
 <TABLE class=table_query>

   <tr>                        
      <td class="table_query_2Col_label_5Letter">题库名：</td>
       <td><input type="text" name="EX_NAME" id="EX_NAME" datatype="1,is_digit_letter_cn,30" class="middle_txt" value=""/></td>     
  
  <tr>
            <td colspan="4" align="center">
            	<input class="normal_btn" type="button" name="button1" value="查询" onclick="__extQuery__(1);"/>
			<input class="normal_btn" type="button" name="button2" value="新增"  onclick="examAddInit();"/>
			<input class="normal_btn" onclick="examDelete();" value="删除" type="button" name="button22" />
		</td>
      </tr>
</TABLE>
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
	var url = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/examManageQuery.json";
	var title = null;
	var columns = [
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"code\")' />全选", align:'center',sortable:false, dataIndex:'id',width:'2%',renderer:checkBoxShow},
				{header: "题库名",sortable: false,dataIndex: 'EX_NAME',align:'center'}, 
				{header: "题库说明 ",sortable: false,dataIndex: 'EX_DESCRIPTION',align:'center'},
				{header: "问题总数",sortable: false,dataIndex: 'EXNUM',align:'center'},
				{header: "编辑",sortable: false,dataIndex: 'EX_ID',renderer:reviewLink,align:'center'},
				{header: "浏览",sortable: false,dataIndex: 'EX_ID',renderer:myHandler,align:'center'}
		      ];
	//新增
	function examAddInit(){
		fm.action = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/examAddInit.do";
		fm.submit();
	}	
	//删除
	function examDelete() {
		var allChecks = document.getElementsByName("code");
		var allFlag = false;
		for(var i = 0;i<allChecks.length;i++){
			if(allChecks[i].checked){
				allFlag = true;
			}
		}
		if(allFlag){
			MyConfirm("确认删除?", deleteSubmit);
		}else{
			MyAlert("请选择后再点击删除按钮！");
		}
	}
	function deleteSubmit() {
		var url="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/examDelete.json";
		makeNomalFormCall(url,showResult11,'fm');
    }
     function showResult11(json){
		var msg=json.msg;
		if(msg=='01'){
			MyAlert('删除成功');
			__extQuery__(1);
		}else{
			MyAlert('删除失败,请联系管理员');
		}
	}      
	//浏览超链接		      
  	function myHandler(value,meta,record){
  		if(record.data.EXNUM=="0")
  		{
  			return ' ' ;
  		}else{
			return '<a href="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/seeExam.do?EX_ID='+record.data.EX_ID+'">[浏览]</a>' ;
		}
	}
	//编辑超链接	      
	function reviewLink(value,meta,record)
	{
  		return '<a href="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/editExamInit.do?EX_ID='+record.data.EX_ID+'">[编辑]</a>' ;
	}
	//设置复选框
	function checkBoxShow(value,meta,record){
		return String.format("<input type='checkbox' id='code' name='code' value='" + record.data.EX_ID + "' />");
	}
</script>  
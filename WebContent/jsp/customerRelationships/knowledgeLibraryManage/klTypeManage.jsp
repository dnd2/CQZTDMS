<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>知识库类型管理</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt;知识库管理&gt;知识库查询
</div>
  <form method="post" name="fm" id="fm">
  <input type="hidden" name="isAdmin" value="${isAdmin}" />
  <table class="table_query">
      <tr>                        
      <td width="14%"  align="right">类型名称：</td>
       <td width="14%" align="left"><input type="text" name="typeName" id="typeName" datatype="1,is_digit_letter_cn,30" class="middle_txt" value=""/></td>     
       <td width="14%"  align="right">类别：</td> 
        <td width="14%" align="left">
	   		${selectBox}
    	</td>
      </tr>       
   	  <tr>
            <td colspan="4" align="center">
            	<input class="normal_btn" type="button" name="button1" value="查询" onclick="__extQuery__(1);"/>
			<input class="normal_btn" type="button" name="button2" value="新增"  onclick="klTypeAddInit();"/>
			<input class="normal_btn" onclick="knowledgeDelete();" value="删除" type="button" name="button22" />
		</td>
      </tr>
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
	var url = "<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/klTypeManageQuery.json";
	var title = null;
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"code\")' />全选", align:'center',sortable:false, dataIndex:'id',width:'2%',renderer:checkBoxShow},
				{header: "类型名称 ",sortable: false,dataIndex: 'TYPE_NAME',align:'center'},
				{header: "类别",sortable: false,dataIndex: 'KIND',align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'TYPE_ID',renderer:myLink ,align:'center'}
		      ];
	//修改的超链接
	function myLink(value,meta,record)
	{
        var formatString="";       
        if(record.data.KIND==<%=Constant.KNOW_MANAGE_1%>&&fm.isAdmin.value=='0'){
        	formatString="";
        }else{
            formatString = "<a href='#' onclick='klTypeUpdateInit(\""+record.data.TYPE_ID+"\")'>[修改]</a>";
        }                
        return String.format(formatString);
	}
	//修改的超链接设置
	function klTypeUpdateInit(typeId){
		fm.action = '<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/klTypeUpdateInit.do?typeId=' + typeId;
	 	fm.submit();
	}
	//新增知识库类型页面
	function klTypeAddInit(){
		fm.action = "<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/klTypeAddInit.do";
		fm.submit();
	}
	//设置复选框
	function checkBoxShow(value,meta,record){
        var formatString="";       
        if(record.data.KIND==<%=Constant.KNOW_MANAGE_1%>&&fm.isAdmin.value=='0'){
        	formatString="";
        }else{
            formatString = "<input type='checkbox' id='code' name='code' value='" + record.data.TYPE_ID + "' />";
        }                
        return String.format(formatString);
	}
	//删除
	function knowledgeDelete() {
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
		var url="<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/klTypeDelete.json";
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
    //审核
    function knowledgeAuthority() {
		var allChecks = document.getElementsByName("code");
		var allFlag = false;
		for(var i = 0;i<allChecks.length;i++){
			if(allChecks[i].checked){
				allFlag = true;
			}
		}
		if(allFlag){
			MyConfirm("确认审核?", authoritySubmit);
		}else{
			MyAlert("请选择后再点击审核通过按钮！");
		}
	}
	function authoritySubmit() {
		var url="<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/knowledgeAuthority.json";
		makeNomalFormCall(url,showResult22,'fm');
    }
    function showResult22(json){
	var msg=json.msg;
	if(msg=='01'){
		MyAlert('审核成功');
		__extQuery__(1);
	}else{
		MyAlert('审核失败,请联系管理员');
	}
}
</script>  
  </body>
</html>

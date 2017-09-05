<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>  
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>知识库管理</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt;知识库管理&gt;知识库管理 
</div>
  <form method="post" name="fm" id="fm">
    <input type="hidden" name="isAdmin" value="${isAdmin}" />
  <table class="table_query">
       <tr>                        
       <td width="14%" class="table_query_2Col_label_5Letter">关键字：</td>
        <td width="14%" ><input type="text" name="KEY_NAME" id="KEY_NAME" datatype="1,is_digit_letter_cn,30" value=""/></td>     
              <td width="14%" align="right" class="table_query_2Col_label_5Letter">类型:</td>
              <td width="14%" align="left">
                <select name="KG_TYPE" id="KG_TYPE" style='width:160px;' >
                <option selected value=''>-请选择-</option>
                <c:forEach items="${typeList}" var="varType">
                    <option value="${varType.TYPE_ID}">${varType.TYPE_NAME}</option>
                </c:forEach>
                </select>
              </td>        
        
        <td width="14%" class="table_query_2Col_label_5Letter">类别：</td> 
        <td width="14%" align="left">
	   		${selectBox}
    	</td>
       </tr>       
	   <tr>
           <td colspan="6" align="center">
	           	<input class="normal_btn" type="button" name="button1" value="查询" onclick="__extQuery__(1);"/>
				<input class="normal_btn" type="button" name="button2" value="新增"  onclick="knowledgeAddInit();"/>
				<input class="normal_btn" onclick="knowledgeDelete();" value="删除" type="button" name="button22" />
				<input class="normal_btn" onclick="knowledgeAuthority();" value="审核通过" type="button" name="button222" />
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
	var url = "<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/knowledgeLibraryManageQuery.json";
	var title = null;
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
	            {header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"code\")' />全选", align:'center',sortable:false, dataIndex:'id',width:'2%',renderer:checkBoxShow},
				{header: "资料标题",sortable: false,dataIndex: 'KG_TOPIC',align:'center'}, 
				{header: "知识库类型 ",sortable: false,dataIndex: 'TYPE_NAME',align:'center'},
				{header: "类别",sortable: false,dataIndex: 'KIND',align:'center',renderer:getItemValue},
				{header: "签发时间",sortable: false,dataIndex: 'KG_SIGN_TIME',align:'center'},
				{header: "状态",sortable: false,dataIndex: 'KG_STATUS',align:'center',renderer:getItemValue},			
				{id:'action',header: "操作",sortable: false,dataIndex: 'KG_ID',renderer:myLink ,align:'center'}
		      ];
	//修改的超链接
	function myLink(value,meta,record)
	{
        var formatString="";     
        if(record.data.KIND==<%=Constant.KNOW_MANAGE_1%>&&fm.isAdmin.value=='0'){
        	formatString="";
        }else{
            formatString = "<a href='#' onclick='knowledgeLibUpdateInit(\""+record.data.KG_ID+"\")'>[修改]</a>";
        }
        return String.format(formatString);		
  		//return String.format("<a href='#' onclick='knowledgeLibUpdateInit(\""+record.data.KG_ID+"\")'>[修改]</a>");
	}
	//修改的超链接设置
	function knowledgeLibUpdateInit(kgid){
		fm.action = '<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/knolibUpdateInit.do?kgid=' + kgid;
	 	fm.submit();
	}
	//新增知识库页面
	function knowledgeAddInit(){
		fm.action = "<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/knowledgeAddInit.do";
		fm.submit();
	}
	function knowledgeAddInit(){
		fm.action = "<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/knowledgeAddInit.do";
		fm.submit();
	}
	//设置复选框
	function checkBoxShow(value,meta,record){
        var formatString="";     
        if(record.data.KIND==<%=Constant.KNOW_MANAGE_1%>&&fm.isAdmin.value=='0'){
        	formatString="";
        }else{
            formatString = "<input type='checkbox' id='code' name='code'  value='" + record.data.KG_ID + "'  data="+JSON.stringify(record.data)+" />";
        }
        return String.format(formatString);
		//return String.format("<input type='checkbox' id='code' name='code' value='" + record.data.KG_ID + "' />");
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
		var url="<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/knowledgeDelete.json";
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
				var data=allChecks[i].getAttribute("data");
				var json=eval("(" + data + ")");
				if(json.KIND==<%=Constant.KNOW_MANAGE_2%>){
					MyAlert("第"+json.NUM+"行为私立库不需要审核!");
					return;
				}
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

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
<title>我的客户回访</title>
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
    <td colspan="4" class=""><span class="table">
      <input name="queryBtn" id="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查询" />
      <input name="button2" type="button" class="normal_btn"  value="新增" onclick="addQuestionair();"/>
      </span><span class="table">
        <input name="button3" type="button" class="normal_btn"  value="生效" onclick="setStatus();"/>
        </span><span class="table">
          <input name="button4" type="button" class="normal_btn"  value="删除" onclick="deleteQuestionair();" />
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
	var url = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/questionairManageQuery.json";
	var title = null;
	var columns = [
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"code\")' />全选", align:'center',sortable:false, dataIndex:'id',width:'2%',renderer:checkBoxShow},
				{header: "问卷名称",sortable: false,dataIndex: 'QR_NAME',align:'center'}, 
				{header: "是否有效 ",sortable: false,dataIndex: 'QR_STATUS',align:'center'},
				{header: "类型",sortable: false,dataIndex: 'QR_TYPE',align:'center'},
				{header: "创建时间",sortable: false,dataIndex: 'CREATE_DATE',align:'center',renderer:formatDate},
				{header: "浏览",sortable: false,dataIndex: 'QR_ID',renderer:myHandler,align:'center'},
				{header: "编辑",sortable: false,dataIndex: 'QR_ID',renderer:reviewLink,align:'center'},
				{header: "复制",sortable: false,dataIndex: 'QR_ID',renderer:myCopy,align:'center'}
		      ];
	
	//格式 化日期	      
	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	} 	      
	//新增
	function addQuestionair(){
		fm.action = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/addQuestionairInit.do";
		fm.submit();
	}	
	//删除
	function deleteQuestionair() {
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
		var url="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/deleteQuestionair.json";
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
		return '<a href="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/seeQuestionair.do?QR_ID='+record.data.QR_ID+'">[浏览]</a>' ;
	}

	//设置编辑超链接      
	function reviewLink(value,meta,record)
	{
		if(record.data.CODE_ID == <%=Constant.QR_STATUS_2%>){
			return '<a href="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/editQuestionairInit.do?QR_ID='+record.data.QR_ID+'">[编辑]</a>' ;
		}
		else{
			return String.format("<div>&nbsp</div>");
		}
		
	}
	//复制
	function myCopy(value,meta,record)
	{
  		return String.format("<input type='button' class='normal_btn' value='复制'  onclick='copySet(\""+record.data.QR_ID+"\")'/>");
	}
	//复制设置
	function copySet(QR_ID){
		var url="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/myCopy.json?QR_ID="+ QR_ID+"";
		makeNomalFormCall(url,showResult33,'fm');
	}
	function showResult33(json){
		var msg=json.msg;
		if(msg=='01'){
			MyAlert('复制成功');
			__extQuery__(1);
		}else{
			MyAlert('设置生效失败,请联系管理员');
		}
	}  
	
	//设置复选框
	function checkBoxShow(value,meta,record){
		//return String.format("<input type='checkbox' id='code' name='code' value='' />");
		return String.format("<input type='checkbox' id='code' name='code' value='" + record.data.QR_ID + "' />");
	}
	//设置生效
	function setStatus() {
		var allChecks = document.getElementsByName("code");
		var allFlag = false;
		for(var i = 0;i<allChecks.length;i++){
			if(allChecks[i].checked){
				allFlag = true;
			}
		}
		if(allFlag){
			MyConfirm("确认生效?", setStatusSubmit);
		}else{
			MyAlert("请选择后再点击生效按钮！");
		}
	}
	function setStatusSubmit() {
		var url="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/setStatus.json";
		makeNomalFormCall(url,showResult22,'fm');
    }
     function showResult22(json){
		var msg=json.msg;
		if(msg=='01'){
			MyAlert('设置生效成功');
			__extQuery__(1);
		}else{
			MyAlert('设置生效失败,请联系管理员');
		}
	}      
</script>  
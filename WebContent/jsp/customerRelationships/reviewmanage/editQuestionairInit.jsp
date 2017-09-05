<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>  
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>我的客户回访</title>
<% String contextPath = request.getContextPath();%>
<script type="text/javascript" >
function CheckSave()
{
	var name=document.fm.QR_NAME.value;
	if(null==name||"".indexOf(name)==0){
       MyAlert("【问卷名称】不能为空！");
       return false;
	}
	var type=document.fm.QR_TYPE.value;
	if(null==type||"".indexOf(type)==0){
       MyAlert("请选择【问卷类型】！");
       return false;
	}
	/*
	var des=document.fm.QR_DESCRIPTION.value;
	if(null==des||"".indexOf(des)==0){
       MyAlert("【问卷描述】不能为空值！");
       return false;
	}else if(des.length>500){
	   MyAlert("【问卷描述】长度超过500！");
       return false;
	}*/
	/*
	var guide=document.fm.QR_GUIDE.value;
	if(null==guide||"".indexOf(guide)==0){
       MyAlert("【问卷引导语】不能为空值！");
       return false;
	}else if(guide.length>500){
		MyAlert("【问卷引导语】长度超过500！");
        return false;
	}
	*/
	var thanks=document.fm.QR_THANKS.value;
	if(null==thanks||"".indexOf(thanks)==0){
	       MyAlert("【结束感谢语】不能为空值！");
	       return false;
	}else if(thanks.length>500){
		MyAlert("【结束感谢语】长度超过500！");
	       return false;
	}
	return true; 
}

function showResultss(json){
		var msg=json.msg;
		if(msg=='01'){
			MyAlert('问题添加成功!');
			__extQuery__(1);
		}else{
			MyAlert('问题添加失败,请联系管理员'); 
		}
}
function setQuestions(ids,qrId)
{
	var uurl = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/setQuestions.json?ids="+ids+"&qrId="+qrId;
	makeNomalFormCall(uurl,showResultss,'fm');
}
</script>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户化关系管理 &gt; 回访管理 &gt; 问卷管理 &gt; 编辑问卷 
</div>
  <form method="post" name="fm" id="fm">
  <input type="hidden" name="QR_ID" id="QR_ID" value="${questionair[0].QR_ID}"/>
  <input type="hidden" name="saveFlag" id="saveFlag" value=""/>
  
   <table width="100%" align="center" cellpadding="2" cellspacing="1" class=table_query>
    <tbody>
      <tr class="">
        <td width="190" height="30" align="right" nowrap="nowrap" class="style1">问卷名称： </td>
        <td style="WIDTH: 53%"><input name="QR_NAME" class="long_txt" id="QR_NAME" style="WIDTH: 35%" value="${questionair[0].QR_NAME}" maxlength="30" />
          <font color="#ff0000" face="宋体">*</font></td>
      </tr>
      <tr class="">
        <td width="17%" height="30" align="right" nowrap="nowrap" class="style1">问卷类型： </td>
        <td style="WIDTH: 53%">
          <script type="text/javascript">
           genSelBoxExp("QR_TYPE",<%=Constant.QR_TYPE%>,"${questionair[0].QR_TYPE}",true,"short_sel","","true",'');
	 	</script>
          <font color="#ff0000" face="宋体">*</font></td>
      </tr>
      <tr class=cssTable>
        <td style="WIDTH: 17%" align="right">问卷描述：</td>
        <td width="965"><textarea id="QR_DESCRIPTION" style="HEIGHT: 90px; WIDTH: 580px" maxlength="10" rows="3" name="QR_DESCRIPTION">${questionair[0].QR_DESCRIPTION}</textarea>
          <br />
        <span style="FONT-FAMILY: 宋体; COLOR: red"><span style="WIDTH: 17%">问卷描述</span>最多允许输入500个汉字。</span> <br /></td>
      </tr>
      <tr class=cssTable>
        <td style="WIDTH: 17%" align="right">问卷引导语：</td>
        <td><textarea id="QR_GUIDE" style="HEIGHT: 90px; WIDTH: 580px" maxlength="10" rows="3" name="QR_GUIDE">${questionair[0].QR_GUIDE}</textarea>
          <br />
        <span style="FONT-FAMILY: 宋体; COLOR: red"><span style="WIDTH: 17%">问卷引导语</span>最多允许输入500个汉字。</span> <br /></td>
      </tr>
      <tr class=cssTable>
        <td style="WIDTH: 17%" align="right">结束感谢语：</td>
        <td><textarea id="QR_THANKS" style="HEIGHT: 90px; WIDTH: 580px" maxlength="10" rows="3" name="QR_THANKS">${questionair[0].QR_THANKS}</textarea>
          <br />
          <span style="FONT-FAMILY: 宋体; COLOR: red">结束感谢语：最多允许输入50个汉字。</span> <br /></td>
      </tr>
      <tr>
        <td colspan="2" align="center"><input name="btnSave" type="button" class="cssbutton" id="btnSave" onclick="saveAction();" value="保存" />
        <input id="queryBtn2" class="cssbutton" onclick="closeSave();" value="返回" type="button" name="button2" /></td>
      </tr>
      <tr id="OperatorBar">
        <td colspan="2">     
          <input name="btnAdd" type="button" class="cssbutton" id="btnAdd" value="添加问题" onclick="addQuestion();"  />
          <input name="btnDel" type="button" class="cssbutton" id="btnDel" onclick="deleteQuestion();"  value="删除问题" />
          <%-- <input name="btnDel2" type="button" class="long_btn" id="btnDel2" onClick="showExam('true',${questionair[0].QR_ID});" value="从题库中选择" /> --%>
      </tr>
    </tbody>
  </table>
  
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
	var url = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/questionairAddQuery.json?QR_ID=${questionair[0].QR_ID}";
	var title = null;
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"code\")' />全选", align:'center',sortable:false, dataIndex:'id',width:'2%',renderer:checkBoxShow},
				{header: "题号",sortable: false,dataIndex: 'QD_NO',align:'center'},
				{header: "问题",sortable: false,dataIndex: 'QD_QUESTION',align:'left'},
				{header: "问题类型",sortable: false,dataIndex: 'QD_QUE_TYPE',align:'center'},
				{header: "答案选项",sortable: false,dataIndex: 'QD_CHOICE',align:'center'},
				{header: "选项分值",sortable: false,dataIndex: 'QD_POINTS',align:'center'},
				{header: "编辑",sortable: false,dataIndex: 'QD_ID',align:'center',renderer:reviewLink}
		      ];

		
	//设置复选框
	function checkBoxShow(value,meta,record){
		return String.format("<input type='checkbox' id='code' name='code' value='" + record.data.QD_ID + "' />");
	}
		      
	//编辑链接	      
	function reviewLink(value,meta,record)
	{
  		return '<a href="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/editQuestion2.do?QR_ID=${questionair[0].QR_ID}&QD_ID= '+record.data.QD_ID+' ">[编辑]</a>' ;
	}
	
	function saveAction()
	{
		var flag = false;
			flag=CheckSave();
		if(flag)
		{
			 var url= "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/questionairEditFact.json";
			 makeNomalFormCall(url,showResult12,'fm');
		}
	}
	function showResult12(json){
		var msg=json.msg;
		if(msg=='01'){
			document.fm.saveFlag.value="isSave";	
			MyAlertForFun("保存成功",closeSave);
		}else if(msg=='02'){
			MyAlert("问卷已存在，请修改问卷名称！");
		}else{
			MyAlert('保存失败,请联系管理员'); 
		}
	}
	
	//返回问卷管理主页
	function closeSave(){
		fm.action = '<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/questionManageInit.do?';
		fm.submit();
	}
	
	//删除
	function deleteQuestion() {
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
		var url="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/questionDelete2.json";
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
	function addQuestion()
	{
		
			fm.action = '<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/addQuestionInit2.do'; 
		 	fm.submit();
	
	}
	__extQuery__(1);
	
</script>  
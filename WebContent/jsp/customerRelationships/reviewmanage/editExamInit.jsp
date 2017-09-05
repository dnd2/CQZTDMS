<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>  
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript">
   function gcheck(keyCode, obj) {
	   if(keyCode == 222) {
		   MyAlert("对不起,你输入的字符是“‘”号，请重新输入！");
		   return false;
	   }
   }
   
   function seValue(obj) {
	   obj.value=obj.value.replace(/[\']/g,'');
   }
</script>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>我的客户回访</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt;回访管理&gt;题 库管理 &gt;编辑题库
</div>
<form name='fm' id='fm'>
<input type="hidden" name="EX_ID" id="EX_ID" value="${EX_ID}"/>
 <table width="100%" align="center" cellpadding="2" cellspacing="1" class=table_query>
    <tbody>
      <tr class=cssTable>
        <td style="WIDTH: 17%" align="right">题库名称：</td>
        <td style="WIDTH: 609px" width="609"><input id="EX_NAME" style="WIDTH: 580px" maxlength="50" value="${examinInfo[0].EX_NAME}" name="EX_NAME" datatype="1,is_digit_letter_cn,30"/>
        </td>
      </tr>
      <tr class=cssTable>
        <td style="WIDTH: 17%" align="right">题库说明： </td>
        <td><textarea id="EX_DESCRIPTION" style="HEIGHT: 119px; WIDTH: 580px" maxlength="10" rows="3" name="EX_DESCRIPTION"  >${examinInfo[0].EX_DESCRIPTION}</textarea>
          <br />
          <span style="FONT-FAMILY: 宋体; COLOR: red">题库说明最多允许输入50个汉字。</span> <br /></td>
      </tr>
      <tr>
        <td colspan="2" align="center"><input name="btnSave" type="button" class="cssbutton"" id="btnSave" onclick="saveExam();" value="保存" />
        <input id="queryBtn2" class="cssbutton" onclick="javascript:history.go(-1)" value="返回" type="button" name="button2" /></td>
      </tr>
      <tr id="OperatorBar">
        <td colspan="2">     
          <input name="btnAdd" type="button" class="cssbutton" id="btnAdd" value="添加问题" onclick="addQuestion();" />
          <input name="btnDel" type="button" class="cssbutton" id="btnDel" onclick="deleteQuestion();" value="删除问题" /></td>
      </tr>
      
    </tbody>
  </table>

 <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<!--页面列表 begin -->

<script type="text/javascript" >
var myPage;
	var url = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/questionEditQuary.json?EX_ID=${EX_ID}";
	var title = null;
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"code\")' />全选", align:'center',sortable:false, dataIndex:'id',width:'2%',renderer:checkBoxShow},
				{header: "问题",sortable: false,dataIndex: 'ED_QUESTION',align:'left'},
				{header: "问题类型",sortable: false,dataIndex: 'ED_QUE_TYPE',align:'center'},
				{header: "答案选项",sortable: false,dataIndex: 'ED_CHOICE',align:'center'},
				{header: "编辑",sortable: false,dataIndex: 'ED_ID',align:'center',renderer:reviewLink}
		      ];

	//编辑链接	      
	function reviewLink(value,meta,record)
	{
  		return '<a href="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/editQuestion.do?EX_ID=${EX_ID}&ED_ID='+record.data.ED_ID+'">[编辑]</a>' ;
	}
	
	//设置复选框
	function checkBoxShow(value,meta,record){
		return String.format("<input type='checkbox' id='code' name='code' value='" + record.data.ED_ID + "' />");
	}
	//保存
	function saveExam(){
		var exName = document.fm.EX_NAME.value;
	    if(null==exName||"".indexOf(exName)==0){
	       MyAlert("请输入题库名称！");
	       return false;
	    }
	    var exDes = document.fm.EX_DESCRIPTION.value;
	    if(null==exDes||"".indexOf(exDes)==0||exDes.length>50){
	       MyAlert("请输入题库名称或者名称太长！");
	       return false;
	    }
	    
	    var url= "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/editSaveFact.json";
		makeNomalFormCall(url,showResult,'fm');
	}
	function showResult(json){
		var msg=json.msg;
		if(msg=='01'){
			MyAlertForFun("保存成功",closeSave);
		}else if(msg=='02'){
			MyAlert('该题库已存在，请修改题库名称！');
		}else{
			MyAlert('保存失败,请联系管理员');
		}
	}
	function closeSave(){
		fm.action = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/examManageInit.do";
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
		var url="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/questionDelete.json";
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
		fm.action = '<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/addQuestionInit.do?RV_ID=${EX_ID}'; 
	 	fm.submit();	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	__extQuery__(1);
</script>
</form> 
</body>
</html>
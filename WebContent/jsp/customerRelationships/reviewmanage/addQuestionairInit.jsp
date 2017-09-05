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
	var des=document.fm.QR_DESCRIPTION.value;
	if(null==des||"".indexOf(des)==0){
       MyAlert("【问卷描述】不能为空值！");
       return false;
	}else if(des.length>500){
	   MyAlert("【问卷描述】长度超过500！");
       return false;
	}
	var guide=document.fm.QR_GUIDE.value;
	if(null==guide||"".indexOf(guide)==0){
       MyAlert("【问卷引导语】不能为空值！");
       return false;
	}else if(guide.length>500){
		MyAlert("【问卷引导语】长度超过500！");
        return false;
	}
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
</script>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户化关系管理 &gt; 回访管理 &gt; 问卷管理 &gt; 新增问卷 
</div>
  <form method="post" name="fm" id="fm">
  <input type="hidden" name="QR_ID" id="QR_ID" value="${QR_ID}"/>
  <input type="hidden" name="saveFlag" id="saveFlag" value=""/>
  
   <table width="100%" align="center" cellpadding="2" cellspacing="1" class=table_query>
    <tbody>
      <tr class="">
        <td width="17%" height="30" align="right" nowrap="nowrap" class="style1">问卷名称： </td>
        <td style="WIDTH: 53%"><input name="QR_NAME" class="long_txt" id="QR_NAME" style="WIDTH: 35%" value="" datatype="1,is_digit_letter_cn,30" maxlength="30" />
          <font color="#ff0000" face="宋体">*</font></td>
      </tr>
       <tr class="">
        <td width="17%" height="30" align="right" nowrap="nowrap" class="style1">问卷类型： </td>
        <td style="WIDTH: 53%">
          <script type="text/javascript">
           genSelBoxExp("QR_TYPE",<%=Constant.QR_TYPE%>,null,true,"short_sel","","true",'');
	 	</script>
          <font color="#ff0000" face="宋体">*</font></td>
      </tr>
      <tr class=cssTable>
        <td style="WIDTH: 17%" align="right">问卷描述：</td>
        <td width="965"><textarea id="QR_DESCRIPTION" style="HEIGHT: 90px; WIDTH: 580px" maxlength="10" rows="3" name="QR_DESCRIPTION"></textarea>
          <br />
        <span style="FONT-FAMILY: 宋体; COLOR: red"><span style="WIDTH: 17%">问卷描述</span>最多允许输入500个汉字。</span> <br /></td>
      </tr>
      <tr class=cssTable>
        <td style="WIDTH: 17%" align="right">问卷引导语：</td>
        <td><textarea id="QR_GUIDE" style="HEIGHT: 90px; WIDTH: 580px" maxlength="10" rows="3" name="QR_GUIDE"></textarea>
          <br />
        <span style="FONT-FAMILY: 宋体; COLOR: red"><span style="WIDTH: 17%">问卷引导语</span>最多允许输入500个汉字。</span> <br /></td>
      </tr>
      <tr class=cssTable>
        <td style="WIDTH: 17%" align="right">结束感谢语：</td>
        <td><textarea id="QR_THANKS" style="HEIGHT: 90px; WIDTH: 580px" maxlength="10" rows="3" name="QR_THANKS"></textarea>
          <br />
          <span style="FONT-FAMILY: 宋体; COLOR: red">结束感谢语：最多允许输入500个汉字。</span> <br /></td>
      </tr>
      <tr>
        <td colspan="2" align="center"><input name="btnSave" type="button" class="cssbutton" id="btnSave" onclick="saveAction();" value="保存" />
        <input id="queryBtn2" class="cssbutton" onclick="javascript:history.go(-1)" value="返回" type="button" name="button2" /></td>
      </tr>
    </tbody>
  </table>
</form> 
<br/>
<!--页面列表 begin -->
<script type="text/javascript" >
	function saveAction()
	{
		var flag = false;
			flag=CheckSave();
		if(flag)
		{
			 var url= "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/questionairAddFact.json";
			 makeNomalFormCall(url,showResult12,'fm');
		}
	}
	function showResult12(json){
		var msg=json.msg;
		if(msg=='01'){
			MyAlertForFun("保存成功",closeSave);
		}else if(msg=='02'){
			MyAlert("问卷已存在，请修改问卷名称！");
		}else{
			MyAlert('保存失败,请联系管理员'); 
		}
	}
function closeSave(){
		fm.action = '<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/questionManageInit.do';
		fm.submit();
	}
</script>  
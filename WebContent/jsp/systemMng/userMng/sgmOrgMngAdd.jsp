<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<link href="/CQZTDMS/style/dtree1.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/CQZTDMS/js/web/dept_tree.js"></script>
	<script type="text/javascript" src="/CQZTDMS/js/web/dtree.js"></script>
<!-- <link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/dept_tree.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js"></script>
	<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script> -->
<title>公司组织维护</title>
<style>textarea.form-control{height: 80px;margin-left: 10px;}</style>
<script>
	   var filecontextPath="<%=contextPath%>";
	   var dept_tree_url = "<%=contextPath%>/sysmng/orgmng/SgmOrgMng/initOrgTree.json";
</script>
</head>

<body>
<div class="wbox">
   <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 组织管理 &gt; 公司组织维护</div>
  <form id="fm" name="fm">
  <input id="DEPT_ID" name="DEPT_ID" type="hidden"/>
  <input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
  <div class="dtree" id="deptt"></div>
  <div class="form-panel">
     <h2>公司组织维护</h2>
     <div class="form-body">
        <table class="table_query" border="0">
          <tr>
            <td class="table_query_2Col_label_4Letter right" nowrap="nowrap">组织代码：</td>
            <td class="table_query_4Col_input" nowrap="nowrap"><span class="table_query_2Col_input">
              <input name="DEPT_CODE" id="DEPT_CODE" datatype="0,is_digit_letter,10" type="text" onkeydown="clearDiv()" class="middle_txt"/>
            </span>
            </td>
            <td class="table_query_2Col_label_4Letter right" nowrap="nowrap">组织名称：</td>
            <td class="table_query_2Col_input" nowrap="nowrap">
              <input name="DEPT_NNAME" id="DEPT_NNAME"  type="text" class="middle_txt" onkeydown="clearDiv()" datatype="0,is_null,30" />
          </tr>
          <tr>
            <td class="table_query_2Col_label_4Letter right" nowrap="nowrap">上级组织：</td>
            <td class="table_query_2Col_input" nowrap="nowrap">
              <input name="DEPT_NAME" id="DEPT_NAME"  onclick="dt.initDeptTree();"
                style="cursor: pointer;"
        type="text" class="middle_txt" datatype="0,is_null,30" readonly="readonly" />
            </td>
            <td class="table_query_2Col_label_4Letter right" nowrap="nowrap">组织状态：</td>
            <td class="table_query_2Col_input" nowrap="nowrap">
              <script type="text/javascript"> genSelBox("DEPT_STAT",<%=Constant.STATUS%>,"",false,"u-select","");</script>
            </td>
          </tr>
            <tr>
            <td class="table_query_2Col_label_4Letter right" nowrap="nowrap">组织类型：</td>
            <td class="table_query_2Col_input" nowrap="nowrap">
            <select class="min_sel u-select" name="ORG_TYPE" id="ORG_TYPE">
              <option value="<%=Constant.DUTY_TYPE_COMPANY %>">公司</option>
              <option value="<%=Constant.DUTY_TYPE_LARGEREGION %>">大区</option>
              <option value="<%=Constant.DUTY_TYPE_SMALLREGION %>">小区</option>
            </select><font color="red">&nbsp;*</font>
            </td>
            <td class="table_query_2Col_label_4Letter" nowrap="nowrap"></td>
            <td class="table_query_2Col_input" nowrap="nowrap">
            </td>
          </tr>
          <tr>
            <td class="table_query_2Col_label_4Letter right" nowrap="nowrap">备注：</td>
            <td class="table_query_2Col_input" nowrap="nowrap" colspan="3">
              <textarea name="DEPT_DESC" id="DEPT_DESC" maxlength="65" class="long2_txt form-control remark"></textarea>
              </td>
            </tr>
          <tr>
            <td class="center" colspan="4" align="center">
              <input name="button2" type="button" class="u-button" onclick="sub('<%=contextPath%>/sysmng/orgmng/SgmOrgMng/addSgmOrg.json')" value="保 存"/>
              <input name="button" type="button" class="u-button u-cancel" onclick="toGoSgmOrgSearch()" value="取 消"/>
            </td>
          </tr>
        </table>
     </div> 
  </div>
  
  </form>
  <br/>
  <div id="erdiv" style="position: absolute; top:-1000px; background: #FDFFCE; height: 17px; border:1px solid #FFBA43; display: none;">
  <img style="margin-top: 1px; margin-left: 2px;" src="<%=contextPath%>/img/exclamation.gif" />
  <span id="ermsg" style="color: red; position: absolute; margin-top: 3px;"></span></div>
</div>  

</body>
</html>
<script>
validateConfig.isOnBlur = false;

function sub(surl) {  
	// $('DEPT_CODE')[0].value = $('DEPT_CODE').value.clean();
	// $('DEPT_NNAME')[0].value = $('DEPT_NNAME').value.clean();
	// $('DEPT_DESC')[0].value = $('DEPT_DESC').value.clean();
	//submitForm('fm') ? sendAjax(surl,subBack,'fm') : "";
	sendAjax(surl,subBack,'fm');
}

function subBack(redata) {
	if(redata.st != null && redata.st == "succeed") {
		toGoSgmOrgSearch();
	}else if(redata.st != null && redata.st == "deptCode_error"){
    //showError('ermsg','erdiv','DEPT_CODE',redata.msg,130);
    MyAlert(redata.msg);
		$('#DEPT_CODE')[0].focus();
	}else if(redata.st != null && redata.st == "deptName_error"){
		//showError('ermsg','erdiv','DEPT_NNAME',redata.msg,170);
    //$('DEPT_NNAME').select();
    MyAlert(redata.msg);
    $('#DEPT_NNAME').focus();
	} else {
		MyAlert("未知错误");
	}
}

function toGoSgmOrgSearch() {
	window.location = "<%=contextPath%>/sysmng/orgmng/SgmOrgMng/sgmOrgMngQueryInit.do";
}

function clearDiv() {
	$('#erdiv').hide();
}
</script>
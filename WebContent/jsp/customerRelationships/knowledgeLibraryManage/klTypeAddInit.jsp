<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.Date" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>   
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
<title>知识库类型新增</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt;知识库管理&gt;知识库类型新增 
</div>
 <form method="post" name ="fm" id="fm">
 <div>
 <table class="table_edit">
          <tbody>
            <tr>
              <td width="14%" align="right" bgcolor="#ffffff">类型名称： </td>
              <td ><input id="TYPE_NAME" name="TYPE_NAME" datatype="0,is_null,20"/>  </td>
              <td bgcolor="#ffffff" align="right">&nbsp;</td>
              <td width="14%" bgcolor="#ffffff">类别</td>
              <td width="28%" bgcolor="#ffffff">
                <select name="kindId" id="kindId" style='width:160px;' >
                <option selected value=''>-请选择-</option>
                <c:forEach items="${kindList}" var="varKind">
                    <option value="${varKind.KIND_ID}">${varKind.KIND_NAME}</option>
                </c:forEach>
                </select>
            <font color="RED">*</font>
              </td>
            </tr>
          </tbody>
        </table>
  <TABLE align=center width="100%" class=csstable >
    <TR class="tblopt">
      <TD width="100%" class="tblopt"><div align="center">
        <input name="button4333223" type="button" class="normal_btn" onClick="saveklTypeAdd();" value="保存">&nbsp;
        <input name="button" type="button" class="normal_btn" onclick="closeklTypeAdd();" value="关闭" />
      </div></TD>
    </TR>
  </TABLE>
</div>
</form>

 <script type="text/javascript">
 function saveklTypeAdd(){
	var typeName = document.fm.TYPE_NAME.value;
    if(null==typeName||"".indexOf(typeName)==0){
       MyAlert("请输入知识库类型！");
       return false;
    }
	var kindId = document.fm.kindId.value;
    if(null==kindId||"".indexOf(kindId)==0){
       MyAlert("请输入类别！");
       return false;
    }    
    var url= "<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/klTypeAddFact.json";
	makeNomalFormCall(url,showResult,'fm');
}
function showResult(json){
	var msg=json.msg;
	if(msg=='01'){
		MyAlertForFun("新增成功",closeklTypeAdd);
	}else if(msg=='02'){
		MyAlert('类别名称重复,请重新录入');
	}else{
		MyAlert('增加失败,请联系管理员');
	}
}
function closeklTypeAdd(){
	fm.action = "<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/klTypeManage.do";
	fm.submit();
}
</script>
  </body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
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
<title>知识库新增</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt;知识库管理&gt;知识库新增 
</div>
 <form method="post" name ="fm" id="fm">
 <div>
  <table border="0" class="table_edit">
    <tbody>
      <tr bgcolor="#ffffff">
        <td><table class="table_edit">
          <tbody>
            <tr>
              <td width="20%" align="right" bgcolor="#ffffff">资料标题： </td>
              <td width="30%" bgcolor="#ffffff"><input id="KG_TOPIC" name="KG_TOPIC"  datatype="0,is_null,20"/> </td>
              <td width="20%" align="right">类型：</td>
              <td width="30%" align="left">
                <select name="KG_TYPE" id="KG_TYPE" style='width:160px;' >
                <option selected value=''>-请选择-</option>
                <c:forEach items="${typeList}" var="varType">
                    <option value="${varType.TYPE_ID}">${varType.TYPE_NAME}</option>
                </c:forEach>
                </select>
              </td> 
            </tr>
            <tr>
              <td bgcolor="#ffffff" align="right">签发时间： </td>
              <td bgcolor="#ffffff" colspan="3"><!-- <input id="KG_SIGN_TIME" name="KG_SIGN_TIME" /> -->
                <span class="tblopt">
               <!-- <input name="button2" type="button" class="normal_btn" align="right" value="..." onclick="location='parameter_set_add.html'" /> --> 
                <input name="KG_SIGN_TIME" id="KG_SIGN_TIME" 
                	   value="<fmt:formatDate value='<%=new Date()%>' pattern='yyyy-MM-dd'/>" 
                	   type="text" class="middle_txt" datatype="1,is_date,10" 
                	   hasbtn="true" callFunction="showcalendar(event, 'KG_SIGN_TIME', false);"/>
                </span></td>
            </tr>
			<tr>
              <td bgcolor="#ffffff" align="right">内容： </td>
              <td colspan="3" bgcolor="#ffffff"><textarea id="KG_MEMO" name="KG_MEMO" cols="100" rows="20" class="tb_list"></textarea></td>
              </tr>
              <tr>
              <td bgcolor="#ffffff" align="right">附件： </td>
              <td width="27%" bgcolor="#ffffff" colspan=2>
             	<jsp:include page="${contextPath}/uploadDiv.jsp" /> 
 				<input type="button" class="normal_btn"  onclick="showNewsUpload('<%=contextPath%>')" value ='添加附件'/>
              </td>
              <td width="13%" bgcolor="#ffffff">&nbsp;</td>
              <td width="46%">&nbsp;</td>
            </tr>
          </tbody>
        </table></td>
      </tr>
    </tbody>
  </table>
  <TABLE align=center width="100%" class=csstable >
    <TR class="tblopt">
      <TD width="100%" class="tblopt"><div align="center">
        <input name="button4333223" type="button" class="normal_btn" onClick="saveKnowledgeAdd();" value="保存">&nbsp;
        <input name="button" type="button" class="normal_btn" onclick="closeKnowledgeAdd();" value="返回" />
      </div></TD>
    </TR>
  </TABLE>
</div>
</form>
 <script type="text/javascript">
 function saveKnowledgeAdd(){
	var kgTopic = document.fm.KG_TOPIC.value;
    if(null==kgTopic||"".indexOf(kgTopic)==0){
       MyAlert("请输入资料标题！");
       return false;
    }
	var paraType = document.fm.KG_TYPE.value;
    if(null==paraType||"".indexOf(paraType)==0||paraType=='0'){
       MyAlert("请选择知识库类型！");
       return false;
    }
    var context = document.fm.KG_MEMO.value;
    if(null==context||"".indexOf(context)==0){
       MyAlert("请增加内容！");
       return false;
    }
	var url= "<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/knowledgeAddFact.json";
	makeNomalFormCall(url,showResult11,'fm');
	
	
}
function showResult11(json){
	var msg=json.msg;
	if(msg=='01'){
		MyAlertForFun("新增成功",closeKnowledgeAdd);
	}else{
		MyAlert('增加失败,请联系管理员');
	}
}
function closeKnowledgeAdd(){
	fm.action = "<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/knowledgeLibraryManageInit.do";
	fm.submit();
}
</script>
  </body>
</html>

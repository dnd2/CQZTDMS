<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="/jstl/cout" prefix="c" %>   
<%@ page import="java.util.*" %>
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
<title>知识库更新</title>
<% String contextPath = request.getContextPath(); %>
<% List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("lists");%>
</head> 
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt;知识库管理&gt;知识库更新 
</div>
 <form method="post" name ="fm" id="fm">
 <div>
  <table border="0" class="table_edit">
    <tbody>
     <input type="hidden" name="KG_ID" value="<c:out value="${map.KG_ID}"/>"/>
      <tr bgcolor="#ffffff">
        <td><table class="table_edit">
          <tbody>
            <tr>
              <td width="20%" align="right" bgcolor="#ffffff">资料标题： </td>
              <td width="30%" bgcolor="#ffffff"><input id="KG_TOPIC" name="KG_TOPIC" value='<c:out value="${map.KG_TOPIC}"/>' datatype="0,is_null,20"/></td>
              <td width="20%" align="right">类型：</td>
              <td width="30%" align="left">
                <select name="KG_TYPE" id="KG_TYPE" style='width:160px;' >
                <option selected value=''>-请选择-</option>
                <c:forEach items="${typeList}" var="varType">
                    <option value="${varType.TYPE_ID}"   <c:if test="${varType.TYPE_ID==map.TYPE_ID}">selected</c:if> >${varType.TYPE_NAME}</option>
                </c:forEach>
                </select>
              </td>
            </tr>
            <tr>
              <td bgcolor="#ffffff" align="right">签发时间： </td>
              <td bgcolor="#ffffff" colspan="3"><!-- <input id="KG_SIGN_TIME" name="KG_SIGN_TIME" /> -->
                <span class="tblopt">
               <!-- <input name="button2" type="button" class="normal_btn" align="right" value="..." onclick="location='parameter_set_add.html'" /> --> 
                <input name="KG_SIGN_TIME" id="KG_SIGN_TIME" value="<fmt:formatDate value='<%=new Date()%>' pattern='yyyy-MM-dd'/>" type="text" class="middle_txt" datatype="1,is_date,10"  hasbtn="true" callFunction="showcalendar(event, 'KG_SIGN_TIME', false);"/>
                </span></td>
            </tr>
			
			<tr>
              <td bgcolor="#ffffff" align="right">内容： </td>
              <td colspan="3" ><textarea id="KG_MEMO" name="KG_MEMO" style="width: 95%" rows="20" class="tb_list" ><c:out value="${map.KG_MEMO}"/></textarea></td>
              </tr>
              <tr>
              <td bgcolor="#ffffff" align="right">附件： </td>
              <td width="27%" bgcolor="#ffffff">
              
              <jsp:include page="${contextPath}/uploadDiv.jsp" /> 
              <%for(int i=0;i<attachLs.size();i++) { %>
    			<script type="text/javascript">
    			addUploadRowByDb('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
    			</script>
    		<%} %>
              <input type="button" class="normal_btn"  onclick="showNewsUpload('<%=contextPath%>')" value ='添加附件'/></td>
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
        <input name="button4333223" type="button" class="normal_btn" onClick="updateKnowledge();" value="保存">&nbsp;
        <input name="button" type="button" class="normal_btn" onclick="closeKnowledgeAdd();" value="返回" />
      </div></TD>
    </TR>
  </TABLE>
</div>
</form>
 <script type="text/javascript">
 function updateKnowledge(){
 
 	var kgTopic = document.fm.KG_TOPIC.value;
    if(null==kgTopic||"".indexOf(kgTopic)==0){
       MyAlert("请输入资料标题！");
       return false;
    }
	var paraType = document.fm.KG_TYPE.value;
    if(null==paraType||"".indexOf(paraType)==0||paraType=='0'){
       MyAlert("请选择所属类型！");
       return false;
    }
    var context = document.fm.KG_MEMO.value;
    if(null==context||"".indexOf(context)==0){
       MyAlert("请输入内容！");
       return false;
    }
	var url= "<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/knowledgeUpdateFact.json";
	makeNomalFormCall(url,showResult11,'fm');
}
function showResult11(json){
	var msg=json.msg;
	if(msg=='01'){
		MyAlertForFun("修改成功",closeKnowledgeAdd);
	}else{
		MyAlert('修改失败,请联系管理员');
	}
}
function closeKnowledgeAdd(){
	fm.action = "<%=contextPath%>/customerRelationships/knowledgelibrarymanage/KnowledgeLibraryManage2/knowledgeLibraryManageInit.do";
	fm.submit();
}
	
</script>
  </body>
</html>

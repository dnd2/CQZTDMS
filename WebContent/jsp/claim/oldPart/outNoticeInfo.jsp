<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件审批入库</title>
<% 
   String contextPath = request.getContextPath();
%>
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;通知单明细</div>
 <form method="post" name ="fm" id="fm">
  <input type="hidden" name="yieldly" id="yieldly" value="${yieldly }"/>
   <input type="hidden" name="code" id="code" value="${supplayCode }"/>
    <input type="hidden" name="outNo" id="outNo" value="${outNo }"/>
  <table class="table_edit">
    <tr bgcolor="F3F4F8">
      <td align="right">出门证号：</td>
       <td>${outNo }
        <input type="hidden" name="outNo" id="outNo" value="${outNo }"/>
        </td>
       <td align="right">出门证生成时间：</td>
       <td>
       <fmt:formatDate value='${baseBean.createDate}' pattern='yyyy-MM-dd'/>
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">出库总数量：</td>
       <td>
 			${totalNum }
 			 <input type="hidden" name="totalNum" id="totalNum" value="${totalNum }"/>
		</td>
       <td align="right">剩余开票数量：</td>
       <td>
      ${totalNum-needNum }
       <input type="hidden" name="needNum" id="needNum" value=" ${totalNum-needNum }"/>
       </td>
     </tr>
     <tr align="center">
     	<td colspan="4">
     	 <input type="button" id="save_btn" onclick="add();" class="normal_btn" style="width=8%" value="新增"/>
         <input type="button"  onclick="backTo();" class="normal_btn" style="width=8%" value="返回"/>
     	</td>
     </tr>
  </table>
  <table  class="table_list">
        <tr class="table_list_th">
            <th>序号</th>
            <th>操作</th>
            <th>通知单编号</th>
            <th>小计</th>
            <th>税额</th>
            <th>总计</th>
            <th>打印次数</th>
            <th>上次打印时间</th>
       </tr>
    <c:forEach var="dList" items="${detailList}" varStatus="status">
    <tr  align="center">
	  	<td class="tdp" align="center" >${status.index+1}</td> 
	    <td class="tdp" align="center"  >
	    <a href="#" onclick="noticeDetail(${dList.noticeId})" >[明细]</a>
	    <a href="#" onclick="noticeDown(${dList.noticeId})" >[导出]</a>
	    <a href="#" onclick="noticePrint(${dList.noticeId})" >[打印]</a>
	    <a href='#' onClick='NoticeDelete(${dList.noticeId});'>[删除]</a>
	    </td>
	    <td class="tdp" align="center"  >${dList.noticeNo}
	    </td>
	    <td class="tdp" align="center" >${dList.smallTotal}
	    </td>
	    <td class="tdp" align="center">${dList.taxTotal }
	     </td> 
	    <td class="tdp" align="center" >${dList.total}
	    </td>
	    <td class="tdp" align="center"  >${dList.printTimes}
	    </td>
	    <td class="tdp" align="center" >
	     <fmt:formatDate value='${dList.lastPrintDate}' pattern='yyyy-MM-dd'/>
	    </td>
	 </tr>
    </c:forEach>
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<br />
<script type="text/javascript">
function NoticeDelete(id){
	MyConfirm("确认删除？",deletes,[id]);
}
function deletes(id){
	var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outPartNoticeDelete.json?id="+id+"&type=0";
 	 makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
}
 function afterCall(json){	
   	var retCode=json.updateResult;
      if(retCode=="updateSuccess"){
    	MyAlert("删除成功!");
    	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outPartNoticeInfo.do?out_no="+json.outNo+"&code="+json.code;
       	fm.method="post";
       	fm.submit();
      }else{
    	   MyAlert("删除失败,"+json.strInfo);
     }
  }
function noticeDetail(id){
	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outPartNoticeDetail.do?id="+id;
   	fm.method="post";
	fm.submit();
}
function noticeDown(id){
	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/noticeDown.json?id="+id;
   	fm.method="post";
	fm.submit();
}
function noticePrint(id){
 window.open('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/noticePrint.do?id='+id,"旧件索赔通知单打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	 	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outPartNoticeInfo.do?out_no="+$('outNo').value+"&code="+$('code').value;
       	fm.method="post";
       	fm.submit();
}
function add(){
	var needNum = $('needNum').value;
	var outNo = $('outNo').value;
	var code = $('code').value;
	if(parseInt(needNum)==0){
		MyAlert("该出门证出库数量已完成!");
		return false;
	}else{
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outPartNoticeIn.do?out_no="+outNo+"&code="+code;
       	fm.method="post";
       	fm.submit();
       	}
}
function backTo(){
	var yieldly = $('yieldly').value;
	if(yieldly == <%=Constant.PART_IS_CHANGHE_01%>){
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryOut.do";
	}else{
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryOut2.do";
	}
       	fm.submit();
}
</script>
</body>
</html>

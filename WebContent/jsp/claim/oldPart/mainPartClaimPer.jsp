<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件出库</title>
<% String contextPath = request.getContextPath(); %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;特殊单二次索赔通知单明细</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
  	  <input type="hidden" name="yieldly" id="yieldly" value="${yieldly }" />
    <TABLE class="table_query">
       <tr>
         <td style="color: #252525;width: 115px;text-align: right">供应商简称：</td>
         <td><input id="supply_name" name="supply_name" value="" type="text" class="middle_txt" datatype="1,is_null,30" ></td>
          <td style="color: #252525;width: 115px;text-align: right">供应商编码： </td>
         <td nowrap>
          <input id="SUPPLY_CODE" name="SUPPLY_CODE" value="" type="text" class="middle_txt" datatype="1,is_null,30">
         </td>
       </tr>
       <tr >
         <td style="color: #252525;width: 115px;text-align: right">通知单编号：</td>
         <td><input id="NOTICE_NO" name="NOTICE_NO" value="" type="text" class="middle_txt" datatype="1,is_null,30" ></td>
         <td nowrap colspan="2">
         </td>
       </tr>
       <tr>
         <td align="center" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="__extQuery__(1);">
           &nbsp;&nbsp;
           <input class="normal_btn"  type="button" id="addButton" name="addButton" value="新增"  onClick="goToAdd();">
         </td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form> 
<br>
<script type="text/javascript">
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/mainPartClaimQuery.json";
				
   var title = null;

   var columns = [
   				{header: "序号",align:'center',renderer:getIndex},
   				{header: "操作", dataIndex: 'noticeId', align:'center',renderer:myLink},
  				{header: "通知单编号", dataIndex: 'noticeNo', align:'center'},
  				{header: "供应商名称", dataIndex: 'noticeCompany', align:'center'},
  				{header: "供应商代码", dataIndex: 'noticeCode', align:'center'},
  				{header: "通知单类型", dataIndex: 'type', align:'center',renderer:getItemValue},
  				{header: "打印次数",dataIndex: 'printTimes',align:'center'},
  				{header: "总金额", dataIndex: 'total', align:'center'}
  		      ];
  		      
  		      __extQuery__(1);
  	function myLink(value,meta,record){
  	var str="";
  		str="<a href='#' onClick='outDetail2(\""+value+"\");'>[明细]</a>"+
  		"<a href='#' onClick='NoticePrint(\""+value+"\");'>[打印]</a>"+
  		"<a href='#' onClick='NoticeDown(\""+value+"\");'>[导出]</a>";
  		if(record.data.delFlag==1){
  			str+="<a href='#' onClick='NoticeDelete(\""+value+"\");'>[删除]</a>";
  		}
  		
  		return String.format( str);
	}
function NoticeDelete(id){
	MyConfirm("确认删除？",deletes,[id]);
}
function deletes(id){
	var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outPartNoticeDelete.json?id="+id+"&type=1";
 	 makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
}
 function afterCall(json){	
   	var retCode=json.updateResult;
      if(retCode=="updateSuccess"){
    	MyAlert("删除成功!");
    	 __extQuery__(1);
      }else{
    	    MyAlert("删除失败!");
     }
  }
	function outDetail2(id){
	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outPartNoticeDetail.do?id="+id;
   	fm.method="post";
	fm.submit();
	}
function NoticeDown(id){
	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/noticeDown.json?id="+id;
   	fm.method="post";
	fm.submit();
}
	//通知单打印
	function NoticePrint(id){
	window.open('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/noticePrint.do?id='+id,"旧件索赔通知单打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	 __extQuery__(1);
	}

   //格式化时间为YYYY-MM-DD
   function formatDate(value,meta,record) {
	 if (value==""||value==null) {
		return "";
	 }else {
		return value.substr(0,10);
	 }
   }
   function doInit(){
	   loadcalendar();
   }
   //转到新增页面
   function goToAdd(){
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/querySpefeeList.do";
       fm.submit();
   }
</script>
</BODY>
</html>
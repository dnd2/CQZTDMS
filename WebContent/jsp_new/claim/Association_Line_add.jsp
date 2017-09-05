<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<head> 
<%  String contextPath = request.getContextPath(); 
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>

<title>线下单号关联</title>
<script type="text/javascript">
	function  sureInsert(){
		var lineNum=$("#lineNum").val();
		var reg = new RegExp("^[0-9]*$");
     if(!reg.test(lineNum)){
         MyAlert("请输入数字!");
         return;
     }
		var rang_no=$("#rang_no").val();
		var url ="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/AssociationLineAdd.json?type=insert";
		url +="&rang_no="+rang_no; //退配单号
		url +="&lineNum="+lineNum; //线下单号
	 	sendAjax(url,afterCall,'fm');
    }
    
    function afterCall(json){
      if(json.succ!=-1){
         MyAlert("提示:操作成功!");
          _hide();
         }else{
           MyAlert("提示:操作失败!");
         }
    }
	
</script>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;线下单号关联
</div>
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="rang_no" value="${rang_no }" name="rang_no" type="hidden"  />

<table>
  <tr>
    <td>线下单号： </td> 
    <td>
      <input type="text" name="lineNum" id="lineNum"/>
    </td>
  </tr>

</table>

<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sure" onclick="sureInsert();"  style="width=8%" value="确认" />
               	&nbsp;&nbsp;
				<input type="button" id="back" onClick="_hide();" class="normal_btn"  style="width=8%" value="返回"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</body>
</html>
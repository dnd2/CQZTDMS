<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>阶段性闭环申请表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit(){
	}

</script>
</head>
<body>

<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;
当前位置：客户关系管理&gt;投诉咨询管理&gt;阶段性闭环申请表&nbsp;&nbsp;</div>
  
  <form method="post" name="fm" id="fm">
   <!-- 查询条件 begin -->
    <input type="hidden"  name="CP_ID"  value="${info.CP_ID}" id="ID"/>
    <input type="hidden"  name="ID"  value="${info.ID}" id="ID"/>
    <input type="hidden"  name="recordID"  value="${info.RECORD_ID}" id="ID"/>
     <input type="hidden"  name="type"  value="${type}" id="ID"/>
    <table class="table_query" >
          <tr>
             <td align="right">经销商/服务商名称：</td>
	      	 <td colspan="6" align="left">
	      	 	<input name="DEALER_NAME" id="DEALER_NAME" type="text" value="${info.DEALER_NAME}"></input>
	         </td> 
          </tr> 
          <tr>
             <td align="right">客户姓名：</td>
	      	 <td  align="left">
	      	 	<input name="CUSTOMER_NAME" id="CUSTOMER_NAME" type="text" value="${info.CP_NAME}"></input>
	         </td> 
	         <td align="right">联系电话：</td>
	      	 <td  align="left">
	      	 	<input name="LINK_PHONE" id="LINK_PHONE" type="text" value="${info.CP_PHONE}"></input>
	         </td> 
          </tr> 
          <tr>
             <td align="right">车型：</td>
	      	 <td  align="left">
	      	 	<input name="MODEL_NAME" id="MODEL_NAME" type="text" value="${info.CP_MODEL_NAME}"></input>
	         </td>  
	         <td align="right">出厂编号：</td>
	      	 <td  align="left">
	      	 	<input name="NO" id="NO" type="text" value="${info.CP_CHUCHANG_NO}"></input>
	         </td> 
          </tr> 
          <tr>
             <td align="right">发动机型号：</td>
	      	 <td  align="left">
	      	 	<input name="ENGINE_NO" id="ENGINE_NO" type="text" value="${info.CP_ENGINE_NO}"></input>
	         </td> 
	         <td align="right">购买日期：</td>
	      	 <td  align="left">
	      	 	<input name="CP_BUY_DATE" id="CP_BUY_DATE" type="text" value="${info.CP_BUY_DATE}"></input>
	         </td> 
	         <td align="right">行驶里程：</td>
	      	 <td  align="left">
	      	 	<input name="CP_MILEAGE" id="CP_MILEAGE" type="text" value="${info.CP_MILEAGE}"></input>
	         </td> 
          </tr> 
          <tr>
             <td align="right">主要事由描述：</td>
			 <td colspan="6">
				<textarea id="MAIN_CONTENT" name="MAIN_CONTENT" rows="3" cols="80">${info.MAIN_DESCRIB}</textarea>
			 </td>
          </tr> 
          <tr>
          	 <td align="right">客户要求：</td>
	      	 <td colspan="6">
				<textarea id="CUS_YOUCYOU" name="CUS_YOUCYOU" rows="3" cols="80">${info.CUS_REQUIRE}</textarea>
			 </td>
           </tr> 
           <tr>
          	 <td align="right">处理单位意见：</td>
	      	 <td colspan="6">
				<textarea id="YIJIAN" name="YIJIAN" rows="3" cols="80"></textarea>
			 </td>
           </tr> 
         <tr>
            <td colspan="6" align="center" nowrap>
				<input class="normal_btn" type="button" name="button1" value="通过"  onClick="commit_(1)">
				<input class="normal_btn" type="button" name="button1" value="驳回"  onClick="commit_(2)">
				<input class="normal_btn" type="button" name="button1" value="返回"  onClick="history.back()">
			</td>
        </tr>
   </table> 

  </form> 
<!--页面列表 begin -->
<script type="text/javascript" >
	//跳转新增页面
	function commit_(value){
// 		var CUSTOMER_NAME = document.getElementById('CUSTOMER_NAME').value;
// 		if(CUSTOMER_NAME==''){
// 			MyAlert("主要事由描述不能为空");
// 			return false;
// 		}
		var url = '<%=contextPath%>/customerRelationships/complaintConsult/WaitComplaintSearchYx/kaidanClosedMoshikomiAudit.do?value='+value;
		fm.action = url;
		fm.submit();
	}
</script>
<!--页面列表 end -->

</body>
</html>
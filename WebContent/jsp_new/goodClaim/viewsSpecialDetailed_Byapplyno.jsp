<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>退换车及善意索赔申请号明细页</title>
<% String contextPath = request.getContextPath();
   List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
 %>
<script type="text/javascript"src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>

</script>
</head>
<form id="fm" name="fm">
<body>
<input type="hidden" id="SPE_ID" name="SPE_ID" />
<input type="hidden" id="DEALER_ID" name="DEALER_ID"  value="${map.DEALER_ID }"/>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;特殊费用管理&gt;申请号明细页</div>
 <table class="table_info" width="75%">
   <tr>
       <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
    </tr>
    <tr>
      <td>申报单位代码：${map.DEALER_CODE} </td>
      <td>&nbsp;&nbsp;&nbsp;申报单位名称：${map.DEALER_NAME}</td>
       <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;车型：${map.MODEL_NAME }</td>
      <td style="padding-left: 0px;" class="NVL">
           
      </td>
    </tr>
    <tr>
      <td>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;申请单号：${map.APPLY_NO }
      </td>
      <td>申报金额（元）：${map.APPLY_AMOUNT }</td>
      <td>审批金额：${map.APPROVAL_AMOUNT }
      </td>
    </tr>
    <tr>
      <td>服务站联系人：
          <c:if test="${map.DEALER_CONTACT!=null }">
          ${map.DEALER_CONTACT }
         </c:if>
       <c:if test="${map.APPLY_PERSON!=null }">
            ${map.APPLY_PERSON }
      </c:if>
      </td>
      <td>服务站联系电话：${map.DEALER_PHONE }</td>
      <td>索赔单号：${map.CLAIM_NO }</td>    
    </tr> 
    <tr>
      <c:if test="${map.TEC_SUPPLY_CODE!=null }">
      <td>&nbsp;&nbsp;&nbsp;供应商代码：${map.TEC_SUPPLY_CODE }</td>
     </c:if>
     <c:if test="${map.RESPONSIBILITY_CODE!=null }">
      <td>&nbsp;&nbsp;&nbsp;责任方代码：${map.RESPONSIBILITY_CODE }</td>
     </c:if>
      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         VIN：${map.VIN }</td>
         <td>&nbsp;&nbsp;&nbsp;类型： 
           <c:if test="${map.SPECIAL_TYPE==0}">
                                        退换车
           </c:if>
           <c:if test="${map.SPECIAL_TYPE==1}">
               &nbsp;&nbsp;&nbsp;善意索赔
           </c:if>
         </td>
    </tr>  
  </table>
  
</body>

</form>
</html>


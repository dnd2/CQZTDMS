<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.bean.ClaimApproveAndStoredReturnInfoBean"%>
<%@page import="com.infodms.dms.bean.TtAsWrOldPartSignDetailListBean"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件审批入库</title>
<% 
   String contextPath = request.getContextPath();
   ActionContext act = ActionContext.getContext();
   AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
   String logonName = logonUser.getName();
   ClaimApproveAndStoredReturnInfoBean detailBean = (ClaimApproveAndStoredReturnInfoBean)request.getAttribute("returnListBean");
   List<TtAsWrOldPartSignDetailListBean> detailList = (List)request.getAttribute("detailList");
%>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;索赔旧件审批入库</div>
 <form method="post" name ="fm" id="fm">
  <input type="hidden" name="i_back_id" id="i_back_id" value="" />
  <table class="table_edit">
    <tr>
      <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
    </tr>
    <tr bgcolor="F3F4F8">
      <td align="right">经销商代码：</td>
       <td><%=detailBean.getDealer_code()%></td>
       <td align="right">经销商名称：</td>
       <td>
          <%=detailBean.getDealer_name()%>              
       </td>
       <td align="right">所属区域：</td>
       <td>
         <%=detailBean.getAttach_area()%>
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">回运清单号：</td>
       <td><%=detailBean.getReturn_no()%></td>
       <td align="right">提报日期：</td>
       <td>
         <%=detailBean.getReturn_date()%>
       </td>
       <td align="right">建单日期：</td>
       <td>
         <%=detailBean.getCreate_date()%>
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">索赔申请单数：</td>
       <td><%=detailBean.getWr_amount()%></td>
       <td align="right">配件项数：</td>
       <td>
         <%=detailBean.getPart_item_amount()%>
       </td>
       <td align="right">配件数：</td>
       <td>
         <%=detailBean.getPart_amount()%>
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">货运方式：</td>
       <td>
         <%=detailBean.getTransport_desc()%>
       </td>
       <td align="right">回运类型：</td>
       <td>
         <%=detailBean.getReturn_desc()%>
       </td>
       <td align="right">装箱总数：</td>
       <td><%=detailBean.getParkage_amount()%></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">索赔单提交时间段：</td>
       <td>${time }</td>
       <td align="right">货运单号：</td>
       <td>
         <%=detailBean.getTran_no()%>
       </td>
       <td align="right"></td>
       <td></td>
     </tr>
  </table>
  <table width="100%" class="table_list">
  		<tr>
	    <th colspan="12" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 回运清单明细</th>
	    </tr>
        <tr class="table_list_th">
            <th>序号</th>
            <th>索赔申请单</th>
            <th>VIN</th>
            <th>配件代码</th>
            <th>配件名称</th>
            <th>回运数</th>
            <th>签收数</th>
            <th>装箱单号</th>
            <th>库区</th>
            <th>抵扣原因</th>
       </tr>
       <c:forEach var="detailList" items="${detailList}" varStatus="num">
        <tr class="table_list_row1">
           <td>
             <c:out value="${num.index+1}"></c:out>
           </td>
           <td>
               <c:out value="${detailList.claim_no}"></c:out>
           </td>
           <td>
               <c:out value="${detailList.vin}"></c:out>
           </td>
           <td>
               <c:out value="${detailList.part_code}"></c:out>
           </td>
           <td>
               <c:out value="${detailList.part_name}"></c:out>
           </td>
           <td>
               <c:out value="${detailList.return_amount}"></c:out>
           </td>
           <td>
               <c:out value="${detailList.sign_amount}"></c:out>
           </td>
           <td>
              <c:out value='${detailList.box_no}'/>
           </td>
           <td>
              <c:out value='${detailList.warehouse_region}'/>
           </td>
           <td>
              <c:out value='${detailList.deduct_desc}'/>
           </td>
         </tr>
      </c:forEach>  
     </table>
     <table class="table_list">
      <tr > 
       <td height="12" align="center">
         <input type="button" onclick="_hide();" class="normal_btn" style="width=8%" value="关闭"/>
       </td>
      </tr>
    </table>  
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.bean.TtAsWrOldPartBackListDetailBean"%>
<%@page import="com.infodms.dms.bean.TtAsWrOldPartDetailListBean"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔回运清单明细</title>
<% 
   String contextPath = request.getContextPath();
   ActionContext act = ActionContext.getContext();
   AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
   String logonName = logonUser.getName();
   TtAsWrOldPartBackListDetailBean detailBean = (TtAsWrOldPartBackListDetailBean)request.getAttribute("claimPartDetailBean");
   List<TtAsWrOldPartDetailListBean> detailList = (List)request.getAttribute("detailList");
%>
</head>
<body>
	<form method="post" name ="fm" id="fm">
 		<div class="navigation">
	<img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理 &gt;索赔旧件管理 &gt;索赔回运清单明细
	</div>
  	<input type="hidden" name="i_back_id" id="i_back_id" value="" />
  	<input type="hidden" name="i_return_no" id="i_return_no" value="" />
  	<input type="hidden" name="i_freight_type" id="i_freight_type" value="" />
  	<input type="hidden" name="i_boxTotalNum" id="i_boxTotalNum" value="" />
  	<input type="hidden" name="i_box_list" id="i_box_list" value="" />
  	<table class="table_edit">
    	<tr>
	    	<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
        </tr>
        <tr bgcolor="F3F4F8">
        	<td align="right">回运清单号：</td>
            <td align="left"><%=detailBean.getReturn_no()%></td>
            <td align="right">提报日期：</td>
            <td>
              <script type="text/javascript">
              if ('<%=detailBean.getReturn_date()%>'==""||<%=detailBean.getReturn_date()%>==null) {
          		  document.write("");
          	    }else {
          		  document.write('<%=detailBean.getReturn_date()%>');
          	    }
              </script>
            </td>
            <td align="right">建单日期：</td>
            <td align="left">
              <%=detailBean.getCreate_date()%>
            </td>
          </tr>
       
          <tr bgcolor="F3F4F8">
            <td align="right">索赔申请单数：</td>
            <td align="left"><%=detailBean.getWr_amount()%></td>
            <td align="right">配件项数：</td>
            <td align="left">
              <%=detailBean.getPart_item_amount()%>
            </td>
            <td align="right">配件数：</td>
            <td align="left">
              <%=detailBean.getPart_amount()%>
            </td>
          </tr>
          <tr bgcolor="F3F4F8">
          	<td align="right">旧件回运起始时间：</td>
			    <td align="left">
			<%=detailBean.getWr_start_date()%>  
			    </td>
          </tr>
          
          <tr>
          	<!-- 回运单id -->
          	<td><input type="hidden" id="orderId" value="<%=detailBean.getId()%>"/></td>
          </tr>
        </table>
        <table class="table_edit" style="border-bottom:1px solid #DAE0EE">
		    <tr>
	          <th colspan="12"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 回运清单明细</th>
	        </tr>
        </table>
        <table class="table_list" style="border-bottom:1px solid #DAE0EE">
	        <tr bgcolor="F3F4F8">
	            <th align="center">序号</th>
	            <th align="center">索赔申请单</th>
	            <th align="center">VIN</th>
	            <th align="center">配件代码</th>
	            <th align="center">配件名称</th>
	            <th align="center">产地</th>
	            <th align="center">需回运数</th>
	            <th align="center">回运数</th>
	       </tr>
       <c:forEach var="detailList" items="${detailList}" varStatus="num">
        <tr class="table_list_row1">
           <td align="center">
             <c:out value="${num.index+1}"></c:out>&nbsp;
           </td>
           <td align="center">
               <c:out value="${detailList.claim_no}"></c:out>&nbsp;
           </td>
           <td align="center">
               <c:out value="${detailList.vin}"></c:out>&nbsp;
           </td>
           <td align="center">
               <c:out value="${detailList.part_code}"></c:out>&nbsp;
           </td>
           <td align="center">
               <c:out value="${detailList.part_name}"></c:out>&nbsp;
           </td>
           <td align="center">
               <c:out value="${detailList.proc_factory}"></c:out>&nbsp;
           </td>
           <td align="center">
               <c:out value="${detailList.n_return_amount}"></c:out>&nbsp;
           </td>
           <td align="center">
               <c:out value="${detailList.return_amount}"></c:out>&nbsp;
           </td>
         </tr>
      </c:forEach>  
     </table>
		  
     <table class="table_list">
       <tr > 
          <td height="12" align="center">
              <% String closeFlag = request.getParameter("closeFlag");
                 if("1".equals(closeFlag)){
              %>
              
	          	<input type="button" onclick="hisBack();" class="normal_btn" style="width=8%" value="返回"/>
	          <%}else{ %>
	          	<input type="button" onclick="parent._hide();" class="normal_btn" style="width=8%" value="关闭"/>
	          <%} %>
          </td>
       </tr>
    </table>
	<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
	<script type="text/javascript">
		function formatDate(value) {
			 if (value==""||value==null) {
				document.write("");
			 }else {
				document.write(value.substr(0,10));
			 }
		}

		function hisBack(){
			location.href = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryReturnOrder.do";
		}
		
	</script>
</body>
</html>

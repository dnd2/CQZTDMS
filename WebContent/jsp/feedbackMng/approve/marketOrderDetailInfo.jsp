<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.bean.TtIfMarketDetailBean"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<%
    TtIfMarketDetailBean detailBean = (TtIfMarketDetailBean)request.getAttribute("marketOrderDetailBean");
	//List<BackChangeApplyMantainBean> MantainList = (List)request.getAttribute("MantainList");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<TITLE>市场问题处理工单明细</TITLE>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<BODY>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：信息反馈管理&gt;信息反馈审批&gt;市场问题处理工单明细</div>
 <form method="post" name="fm" id="fm">
        <br />
  <table width=100% border="0"  cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_info">
	      <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 基本信息</th>
          <tr bgcolor="F3F4F8">
            <td align="right">工单号：</td>
            <td><%=detailBean.getOrderId()==null?"":detailBean.getOrderId()%></td>
            <td align="right">经销商代码：</td>
             <td height="27" align="left" > <%=detailBean.getDealerCode()==null?"":detailBean.getDealerCode()%></td>
            <td align="right">经销商名称：</td>
            <td><%=detailBean.getDealerShortname()==null?"":detailBean.getDealerShortname()%></td>
          </tr>
          <tr bgcolor="F3F4F8">
            <td align="right">服务中心经办人：</td>
            <td><%=detailBean.getLinkMan()==null?"":detailBean.getLinkMan()%></td>
            <td align="right">联系电话：</td>
            <td><%=detailBean.getTel()==null?"":detailBean.getTel()%></td>
            <td height="27" align="right" >申报金额：</td>
            <td align="left"  >
              <%=detailBean.getMoney()==null?"":detailBean.getMoney()%>(元)
            </td>
          </tr>
          <tr >
            <td align="right" >车辆VIN码：</td>
            <td><%=detailBean.getVin()==null?"":detailBean.getVin()%></td>
            <td height="27" align="right">车系：</td>
            <td align="left" ><%=detailBean.getGroup_name()==null?"":detailBean.getGroup_name()%></td>
            <td align="right" >发动机号：</td>
            <td><%=detailBean.getEngine_no()==null?"":detailBean.getEngine_no()%></td>
          </tr>
          <tr bgcolor="F3F4F8">
            <td height="27"  align="right" bgcolor="F3F4F8">出厂日期：</td>
            <td bgcolor="F3F4F8"><%=detailBean.getFactory_date()==null?"":detailBean.getFactory_date()%></td> 
            <td align="right" bgcolor="F3F4F8">购车日期：</td>
            <td bgcolor="F3F4F8" ><%=detailBean.getDelivery_date()==null?"":detailBean.getDelivery_date()%></td>
            <td width="12%" align="right" bgcolor="F3F4F8" >行驶里程（KM）：</td>
            <td ><%=detailBean.getHistory_mile()==null?"":detailBean.getHistory_mile()%></td>
          </tr>
          <tr >
            <td height="27" align="right">客户姓名：</td>
            <td align="left" ><%=detailBean.getCustomerName()==null?"":detailBean.getCustomerName()%></td>
            <td align="right">客户联系电话：</td>
            <td><%=detailBean.getMobile()==null?"":detailBean.getMobile()%></td>
            <td align="right">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr >
            <td height="27" align="right">客户联系地址：</td>
            <td height="27" colspan="5" align="left" ><%=detailBean.getAddress_desc()==null?"":detailBean.getAddress_desc()%></td>
          </tr>
          <tr>
            <td height="27" align="right">问题描述：</td>
          <td height="27" colspan="5" align="left" ><span class="tbwhite">
              <%=detailBean.getProblemDescribe()==null?"":detailBean.getProblemDescribe()%>
            </span></td>
          </tr>
          <tr>
             <td height="27" align="right">用户要求如何：</td>
          <td height="27" colspan="5" align="left" ><span class="tbwhite">
              <%=detailBean.getUserRequest()==null?"":detailBean.getUserRequest()%>
            </span></td>
          </tr>
          <tr>
            <td height="27" align="right">建议处理方式：</td>
          <td height="27" colspan="5" align="left" ><span class="tbwhite">
              <%=detailBean.getAdviceDealMode()==null?"":detailBean.getAdviceDealMode()%>
            </span></td>
          </tr>
   </table>

       	  <br>
  <TABLE class="table_list" style="border-bottom:1px solid #DAE0EE">
		   <th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 审批明细</th>
          <tr  bgcolor="F3F4F8">
            <th > 审批时间</th>
            <th > 审批人员</th>
            <th > 人员部门</th>
            <th > 审批状态</th>
            <th >审批意见</th>
    </tr>
     <c:forEach var="TtIfMarketAuditListBean" items="${auditList}">
        <tr class="table_list_row1">
           <td> <c:out value="${TtIfMarketAuditListBean.audit_date}"></c:out>
           </td>
           <td><span class="tbwhite">
           <c:out value="${TtIfMarketAuditListBean.user_name}"></c:out>
           </span></td>
           <td><span class="tbwhite">
           <c:out value="${TtIfMarketAuditListBean.org_name}"></c:out>
           </span>
           </td>
           <td>
               <c:out value="${TtIfMarketAuditListBean.status}"></c:out>
           </td>
           <td>
           <c:out value="${TtIfMarketAuditListBean.audit_content}"></c:out>
           </td>
         </tr>
      </c:forEach>          
   </table>
    <table class="table_list">
       <tr > 
         <th height="12" align=center>
          <input type="button" onClick="_hide();" class="normal_btn" style="width=8%" value="关闭"/>
           &nbsp;&nbsp;
          </th>
       </tr>
     </table>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
 </form>
</BODY>
</html>

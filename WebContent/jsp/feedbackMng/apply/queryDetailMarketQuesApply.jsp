<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.bean.TtIfMarketDetailBean"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>市场问题处理工单明细页面</title>
<% 
   String contextPath = request.getContextPath();
   ActionContext act = ActionContext.getContext();
   AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
   String logonName = logonUser.getName();
   TtIfMarketDetailBean detailBean = (TtIfMarketDetailBean)request.getAttribute("marketOrderDetailBean");
%>
</head>
<script type="text/javascript">
function window.onload(){
   var comp_type='<%=detailBean.getComp_type()%>';
   if(comp_type!=null&&comp_type!=""){
      if(comp_type.indexOf("F")!=-1){
          document.getElementById("Check_Flag_F").checked=true;
      }
      if(comp_type.indexOf("C")!=-1){
          document.getElementById("Check_Flag_C").checked=true;
      }
      if(comp_type.indexOf("B")!=-1){
          document.getElementById("Check_Flag_B").checked=true;
      }
   }
}
</script>
<BODY>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：信息反馈管理 &gt;信息反馈提报 &gt;市场问题处理工单明细</div>
 <form method="post" name ="fm" id="fm">
  <input type="hidden" name="curSysDate" id="curSysDate" value="<%=request.getAttribute("curSysDate")%>" /> 
  <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
	      <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
          <tr bgcolor="F3F4F8">
            <td align="right">工单号：</td>
            <td colspan="5"><%=detailBean.getOrderId()%></td>
          </tr>
          <tr bgcolor="F3F4F8">
            <td align="right">服务中心经办人：</td>
            <td><%=detailBean.getLinkMan()%></td>
            <td align="right">联系电话：</td>
            <td><%=detailBean.getTel()%></td>
            <td height="27" align="right" bgcolor="FFFFFF">申报金额：</td>
            <td align="left" bgcolor="FFFFFF" ><%=detailBean.getMoney()%>(元)</td>
          </tr>
          <tr bgcolor="FFFFFF">
            <td align="right" >车辆识别码(VIN)：</td>
            <td><%=detailBean.getVin()==null?"":detailBean.getVin()%></td>
            <td height="27" align="right">车型：</td>
            <td align="left" ><%=detailBean.getGroup_name()==null?"":detailBean.getGroup_name()%></td>
            <td align="right" bgcolor="FFFFFF">发动机号：</td>
            <td bgcolor="FFFFFF"   ><%=detailBean.getEngine_no()==null?"":detailBean.getEngine_no()%></td>
          </tr>
          <tr bgcolor="F3F4F8">
            <td height="27"  align="right" bgcolor="F3F4F8">出厂日期：</td>
            <td bgcolor="F3F4F8"   ><%=detailBean.getFactory_date()==null?"":detailBean.getFactory_date()%></td> 
            <td align="right" bgcolor="F3F4F8">购车日期：</td>
            <td bgcolor="F3F4F8" ><%=detailBean.getDelivery_date()==null?"":detailBean.getDelivery_date()%></td>
            <td width="12%" align="right" bgcolor="F3F4F8" >行驶里程(KM)：</td>
            <td ><%=detailBean.getHistory_mile()%></td>
          </tr>
          
          <tr bgcolor="FFFFFF">
            <td align="right">客户姓名：</td>
            <td><%=detailBean.getCustomerName()==null?"":detailBean.getCustomerName()%></td>
            <td align="right">客户联系电话：</td>
            <td align="left"><%=detailBean.getMobile()==null?"":detailBean.getMobile()%></td>
            <td align="right">&nbsp;</td>
            <td align="left">&nbsp;</td>
          </tr>
		  <tr bgcolor="FFFFFF">
            <td height="27" align="right">客户地址：</td>
            <td colspan="5" align="left" ><%=detailBean.getAddress_desc()==null?"":detailBean.getAddress_desc()%></td>
          </tr>
        </table>
    <tr bgcolor="F3F4F8"> 
      <td></td>
   </tr>
    <tr> 
      <td  height=10> 
        <table width="100%" border="0" cellspacing="1">
         
        </table>
      </td>
    </tr>
    <tr> 
      <td > <table width=100% border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="91908E"  class="table_edit">
	    <th colspan="4"><img class="nav" src="../../../img/subNav.gif" /> 申请内容</th>
        <tr bgcolor="F3F4F8">
          <td align="right"><div align="right">信息类别：</div></td>
          <td><script type="text/javascript">
	          	  writeItemValue('<%=detailBean.getInfo_type()%>');
	           </script>
	        </td>
          <td align="right" id="blank1">发出时间：</td>
          <td bgcolor="F3F4F8" class="tbwhite" id="blank2">
          	<%=detailBean.getOrder_date()%>
          </td>
          
        </tr>
        <tr bgcolor="F3F4F8">
          <td align="right"><div align="right"><span class="style1"></span>投诉类型：</div></td>
          <td colspan="3"  class="tbwhite"><input name="Check_Flag_F" id="Check_Flag_F" type="checkbox" disabled="disabled"/>
                                   服务&nbsp;&nbsp;&nbsp;&nbsp;
            <input name="Check_Flag_C" id="Check_Flag_C" type="checkbox" disabled="disabled"/>
                                  产品质量&nbsp;&nbsp;&nbsp;&nbsp;
            <input name="Check_Flag_B" id="Check_Flag_B" type="checkbox" disabled="disabled"/>
                                 备件&nbsp;&nbsp;&nbsp;&nbsp;</td>
        </tr>
        </table>
<table width="100%" border="0" cellspacing="1">
        <tr bgcolor="FFFFFF">
          <td align="right" bgcolor="FFFFFF">问题描述：
            <div align="right"></div></td>
         	 <td  class="tbwhite" height="15">
             <textarea disabled="disabled" name="question_content" id="question_content" rows="3" cols="80" datatype="1,is_textarea,200"><%=detailBean.getProblemDescribe()%>
             </textarea>
          </td>
        </tr>
        <tr bgcolor="FFFFFF">
          <td align="right" bgcolor="FFFFFF">用户要求如何：
            <div align="right"></div></td>
         	 <td  class="tbwhite" height="15">
             <textarea disabled="disabled" name="user_content" id="user_content" rows="3" cols="80" datatype="1,is_textarea,200"><%=detailBean.getUserRequest()%>
             </textarea>
          </td>
        </tr>
        <tr bgcolor="FFFFFF">
          <td align="right" bgcolor="FFFFFF">建议处理方式：
            <div align="right"></div></td>
         	 <td  class="tbwhite" height="15">
             <textarea  disabled="disabled" name="deal_content" id="deal_content" rows="3" cols="80" datatype="1,is_textarea,200"><%=detailBean.getAdviceDealMode()%>
             </textarea>
          </td>
        </tr>
   </table>
     <br/>   
   <table class="table_list" style="border-bottom:1px solid #DAE0EE">
		   <th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 审批明细</th>
          <tr>
            <th> 审批时间</th>
            <th> 审批人员</th>
            <th> 人员部门</th>
            <th> 审批状态</th>
            <th> 审批意见</th>
           </tr>
     <c:forEach var="TtIfMarketAuditListBean" items="${approveDetailList}">
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
       </tr>
     </table>
      </td>
    </tr>
    <!-- 资料显示区结束 -->
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
</BODY>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<head>
<%@ page import="com.infodms.dms.po.TtIfServicecarExtPO"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.List"%>
<%
	TtIfServicecarExtPO tisp = (TtIfServicecarExtPO)request.getAttribute("servicecarBean");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	List<TtIfServicecarExtPO> ls = (List<TtIfServicecarExtPO>)request.getAttribute("auditDetails");
 %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<TITLE>服务车申请表资料上传明细</TITLE>

<SCRIPT LANGUAGE="JavaScript">
    
	
</SCRIPT>
</HEAD>
<BODY onLoad="javascript:changBand();">

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置： 信息反馈管理&gt; 信息反馈提报 &gt;服务车申请表</div>
 <form method="post" name = "FRM"  >


 <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">
	       <th colspan="6"><img class="nav" src="../../../img/subNav.gif" /> 基本信息&nbsp;&nbsp;&nbsp;&nbsp; <font color="#FF0000">(申请的车辆只能用于长安轿车的售后服务工作，并按长安轿车要求制作形象。一年内不得转让)</font></th>
           <tr>
             <td class="table_edit_3Col_label_7Letter">工单号：</td>
             <td width="23%"><%=tisp.getOrderId()==null?"":tisp.getOrderId() %></td>
             <td class="table_edit_3Col_label_8Letter"><span class="zi">经销商代码：</span></td>
             <td width="13%"><%=tisp.getDealerCode()==null?"":tisp.getDealerCode() %></td>
             <td class="table_edit_3Col_label_6Letter"><span class="zi">经销商名称：</span></td>
             <td width="15%"><%=tisp.getDealerName()==null?"":tisp.getDealerName()%></td>
           </tr>
          <tr>
            <td class="table_edit_3Col_label_7Letter">经销商联系人：</td>
            <td><%=tisp.getLinkMan()==null?"":tisp.getLinkMan() %></td>
            <td class="table_edit_3Col_label_8Letter">经销商电话：</td>
            <td><%=tisp.getTel()==null?"": tisp.getTel()%></td>
            <td class="table_edit_3Col_label_6Letter">经销商传真：</td>
            <td    ><%=tisp.getFax()==null?"": tisp.getFax()%></td>
          </tr>
          <tr >
            <td class="table_edit_3Col_label_7Letter">申请购买车型：</td>
            <td ><%=tisp.getModelName()==null?"":tisp.getModelName()%></td>
            <td class="table_edit_3Col_label_8Letter">申请车型市场价：</td>
            <td   ><%=tisp.getSaleAmount()==null?"":tisp.getSaleAmount() %></td>
            <td class="table_edit_3Col_label_6Letter" >状态：</td>
            <td ><%=tisp.getStatus()==null?"":tisp.getStatus() %></td>
          </tr>
          <tr >
            <td class="table_edit_3Col_label_7Letter">申请内容：</td>
            <td height="27" colspan="5" align="left" ><%=tisp.getContent()==null?"":tisp.getContent() %></td>
          </tr>
        </table>
        
  <TABLE align=center width="95%" class="table_list" style="border-bottom:1px solid #DAE0EE">
	       <th colspan="6" align="left"><img class="nav" src="../../../img/subNav.gif" /> 审批明细</th>
		   <tr>
         
            <th > 审批时间</th>
            <th > 审批人员</th>
            <th > 人员部门</th>
            <th > 审批状态</th>
            <th >审批意见 </th>
        </tr>
        <%
        TtIfServicecarExtPO temp = new TtIfServicecarExtPO();
        for (int i=0;i<ls.size();i++) { 
        temp = ls.get(i);
        %>
    <tr class="table_list_row1">
            <td ><%=temp.getAuditDate()==null?"":df.format(temp.getAuditDate()) %></td>
            <td>            <span class="tbwhite"><%=temp.getAuditByName()==null?"":temp.getAuditByName() %></span></td>
            <td><span class="tbwhite"><%=temp.getDeptName()==null?"":temp.getDeptName() %>
            </span>            </td>
            <td><script type='text/javascript'>var status = getItemValue('<%=temp.getAuditStatus()==null?"": temp.getAuditStatus()%>');document.write(status);</script></td>
            <td><%=temp.getAuditContent()==null?"":temp.getAuditContent() %></td>
          </tr>
          <%} %>
   </table>  
  <table class="table_list">
       <tr > 
         <th height="12" align=center>
          <input type="button" onClick="_hide();" class="normal_btn" style="width=8%" value="关闭"/>
           &nbsp;&nbsp;
       </tr>
   </table>  
</form>


  
</BODY>
</html>

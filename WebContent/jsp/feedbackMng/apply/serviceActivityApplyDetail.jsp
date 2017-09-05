<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<head>
<%@ page import="com.infodms.dms.po.TtIfWrActivityExtPO"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.List"%>
<%
	TtIfWrActivityExtPO tisp = (TtIfWrActivityExtPO)request.getAttribute("servicecarBean");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	List<TtIfWrActivityExtPO> ls = (List<TtIfWrActivityExtPO>)request.getAttribute("auditDetails");
	String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
 %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<TITLE>服务活动申请表明细</TITLE>

<SCRIPT LANGUAGE="JavaScript">
    
	
</SCRIPT>
</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：  信息反馈管理&gt;信息反馈提报 &gt;服务活动申请表</div>
 <form method="post" name = "FRM"  >


 <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">
	       <th colspan="6"><img class="nav" src="../../../img/subNav.gif" /> 基本信息&nbsp;&nbsp;&nbsp;&nbsp; </th>
           <tr>
             <td class="table_edit_3Col_label_7Letter">工单号：</td>
             <td width="23%"><%=tisp.getOrderId()==null?"":tisp.getOrderId() %></td>
             <td class="table_edit_3Col_label_6Letter"><span class="zi">经销商名称：</span></td>
             <td width="13%"><%=tisp.getDealerName()==null?"":tisp.getDealerName() %></td>
             <td class="table_edit_3Col_label_6Letter"><span class="zi">服务活动名称：</span></td>
             <td width="15%"><%=tisp.getActName()==null?"":tisp.getActName()%></td>
           </tr>
          <tr>
            <td class="table_edit_3Col_label_7Letter">经销商联系人：</td>
            <td><%=tisp.getLinkMan()==null?"":tisp.getLinkMan() %></td>
            <td class="table_edit_3Col_label_6Letter">经销商电话：</td>
            <td><%=tisp.getTel()==null?"": tisp.getTel()%></td>
            <td class="table_edit_3Col_label_6Letter" >经销商传真：</td>
            <td    ><%=tisp.getFax()==null?"": tisp.getFax()%></td>
          </tr>
          <tr >
            <td class="table_edit_3Col_label_7Letter">申请类型：</td>
            <td ><script type="text/javascript">var type=getItemValue('<%=tisp.getActType()==null?"":tisp.getActType()%>');document.write(type);</script></td>
            <td class="table_edit_3Col_label_6Letter">金额：</td>
            <td><%=tisp.getActMoney()==null?"":tisp.getActMoney() %></td>
          </tr>
          <tr>
            <td class="table_edit_3Col_label_7Letter">申请内容：</td>
            <td height="27" colspan="5" align="left" ><span class="tbwhite">
             <%=tisp.getActContent()==null?"":tisp.getActContent() %>
            </span></td>
          </tr>
        </table>
 <!-- 展示附件 开始-->
  <table class="table_info" border="0" id="file">
    <tr colspan="8">
        <th>
		<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
		&nbsp;附件列表：
		</th>
		<th><span align="left"></span>
		</th>
	</tr>
	<tr>
		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp"/></td>
	</tr>
	<%for(int i=0;i<fileList.size();i++) { %>
	  <script type="text/javascript">
	  showUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>');
	  </script>
	<%}%>
  </table>
<!-- 展示附件 结束-->       
  <TABLE align=center width="95%" class="table_list" style="border-bottom:1px solid #DAE0EE">
	       <th colspan="6" align="left"><img class="nav" src="../../../img/subNav.gif" /> 审批明细</th>
		   <tr>
         
            <th > 审批时间</th>
            <th > 审批人员</th>
            <th > 人员部门</th>
            <th > 审批状态</th>
            <th > 审批意见 </th>
        </tr>
        <%
        TtIfWrActivityExtPO temp = new TtIfWrActivityExtPO();
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

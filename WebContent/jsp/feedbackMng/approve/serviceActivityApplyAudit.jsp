<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<%@ page import="com.infodms.dms.po.TtIfWrActivityExtPO"%>
	<%@ page import="java.util.List"%>
	<%@ page import="java.text.SimpleDateFormat"%>
	<%@ page import=" com.infodms.dms.util.CommonUtils" %>
	<%@page import="java.util.LinkedList"%>
	<%@ page import="java.util.List" %>
	<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<%
	String contextPath = request.getContextPath();
	String title =(String) request.getAttribute("TITLE");
	String right = (String) request.getAttribute("RIGHT");
	String action = (String) request.getAttribute("ACTION");
	TtIfWrActivityExtPO tisp = (TtIfWrActivityExtPO)request.getAttribute("servicecarBean");
	List<TtIfWrActivityExtPO> ls = (List<TtIfWrActivityExtPO>)request.getAttribute("auditDetails");
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
%>
	<head>
		<TITLE><%=title%></TITLE>
		<SCRIPT LANGUAGE="JavaScript">
function oper(type) {
	var fm = document.getElementById('fm');
	if (type=='pass') {
		fm.action = '<%=contextPath%>/feedbackmng/approve/<%=action%>/passOrRefuse.do?RIGHT=<%=right%>&ORDER_ID=<%=tisp.getOrderId()%>&TYPE='+type;
		MyConfirm("确认操作?",fm.submit);
		//fm.submit();
	}else if (type=='refuse'){
	  	if (document.getElementById('AUDIT_CONTENT').value!=null&&document.getElementById('AUDIT_CONTENT').value!=""){
		fm.action = "<%=contextPath%>/feedbackmng/approve/<%=action%>/passOrRefuse.do?RIGHT=<%=right%>&ORDER_ID=<%=tisp.getOrderId()%>&TYPE="+type;
		MyConfirm("确认操作?",fm.submit);
		//fm.submit();
		}else {
			MyAlert("驳回操作请输入审批意见（驳回理由）！");
		}
	}
}
  
	
</SCRIPT>
	</HEAD>
	<BODY>

		<div class="navigation">
			<img src="../../../img/nav.gif" />
			&nbsp;当前位置：信息反馈管理&gt;信息反馈提报&gt;<%=title%></div>
		<form method="post" name="fm" id='fm'>
			<table class="table_edit">
				<th colspan="7">
					<img class="nav" src="../../../img/subNav.gif" />
					审核操作
				</th>
				<tr>
					<td height="12" align=left>
						审核意见：
					</td>
					<td align=left>
						<span class="tbwhite"> <textarea name='AUDIT_CONTENT'
								id='AUDIT_CONTENT' rows='2' datatype="1,is_textarea,200" cols='60'></textarea> <input
								type="button" onclick="oper('pass');" class="normal_btn"
								style="" value="通过" /> <input type="button"
								onclick="oper('refuse');" class="normal_btn" style="" value="驳回" />
								<input type="button"
								onclick="javascript:history.go(-1);" class="normal_btn" style="" value="返回" />
						</span>
					</td>
				</tr>
			</table>

			<br />

			<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">
	       <th colspan="6"><img class="nav" src="../../../img/subNav.gif" /> 基本信息&nbsp;&nbsp;&nbsp;&nbsp; <font color="#FF0000"></font></th>
           <tr>
             <td width="10%" align="right">工单号：</td>
             <td width="20%"><%=tisp.getOrderId()==null?"":tisp.getOrderId() %></td>
             <td width="10%" align="right"><span class="zi">经销商名称：</span></td>
             <td width="20%"><%=tisp.getDealerName()==null?"":tisp.getDealerName() %></td>
             <td width="10%" align="right"><span class="zi">服务活动名称：</span></td>
             <td width="20%"><%=tisp.getActName()==null?"":tisp.getActName()%></td>
           </tr>
          <tr>
            <td align="right">经销商联系人：</td>
            <td><%=tisp.getLinkMan()==null?"":tisp.getLinkMan() %></td>
            <td align="right">经销商电话：</td>
            <td><%=tisp.getTel()==null?"": tisp.getTel()%></td>
            <td align="right">经销商传真：</td>
            <td><%=tisp.getFax()==null?"": tisp.getFax()%></td>
          </tr>
          <tr >
            <td align="right">申请类型：</td>
            <td><script type="text/javascript">var type=getItemValue('<%=tisp.getActType()==null?"":tisp.getActType()%>');document.write(type);</script></td>
            <td align="right">金额：</td>
            <td><%=tisp.getActMoney()==null?"":tisp.getActMoney() %></td>
          </tr>
          <tr>
          	<td align="right">申请内容：</td>
          	<td colspan="3"><%=tisp.getActContent()%></td>
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


		</form>



	</BODY>
</html>

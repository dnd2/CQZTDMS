<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<%@ page import="com.infodms.dms.po.TtIfServicecarExtPO"%>
	<%@ page import="java.util.List"%>
	<%@ page import="java.text.SimpleDateFormat"%>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<%
	String contextPath = request.getContextPath();
	String title =(String) request.getAttribute("TITLE");
	String right = (String) request.getAttribute("RIGHT");
	String action = (String) request.getAttribute("ACTION");
	TtIfServicecarExtPO tisp = (TtIfServicecarExtPO)request.getAttribute("servicecarBean");
	List<TtIfServicecarExtPO> ls = (List<TtIfServicecarExtPO>)request.getAttribute("auditDetails");
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

			<table width=100% border="0" align="center" cellpadding="1"
				cellspacing="1" class="table_edit">
				<th colspan="6">
					<img class="nav" src="../../../img/subNav.gif" />
					基本信息&nbsp;&nbsp;&nbsp;&nbsp;
					<font color="#FF0000">(申请的车辆只能用于长安轿车的售后服务工作，并按长安轿车要求制作形象。一年内不得转让)</font>
				</th>
				<tr>
					<td width="14%" align="right">
						工单号：
					</td>
					<td width="20%"><%=tisp.getOrderId() == null ? "" : tisp.getOrderId()%></td>
					<td width="10%" align="right">
						<span class="zi">经销商代码：</span>
					</td>
					<td width="20%"><%=tisp.getDealerId() == null ? "" : tisp
							.getDealerId()%></td>
					<td width="10%" align="right">
						<span class="zi">经销商名称：</span>
					</td>
					<td width="20%"><%=tisp.getDealerName() == null ? "" : tisp
					.getDealerName()%></td>
				</tr>
				<tr>
					<td width="10%" align="right">
						经销商联系人：
					</td>
					<td><%=tisp.getLinkMan() == null ? "" : tisp.getLinkMan()%></td>
					<td width="10%" align="right">
						经销商电话：
					</td>
					<td><%=tisp.getTel() == null ? "" : tisp.getTel()%></td>
					<td width="10%" align="right">
						经销商传真：
					</td>
					<td><%=tisp.getFax() == null ? "" : tisp.getFax()%></td>
				</tr>
				<tr>
					<td width="10%" align="right">
						申请购买车型及状态：
					</td>
					<td><%=tisp.getModelName() == null ? "" : tisp
					.getModelName()%></td>
					<td width="10%" align="right">
						申请车型市场价：
					</td>
					<td><%=tisp.getSaleAmount() == null ? "" : tisp
					.getSaleAmount()%></td>
				</tr>
				<tr>
					<td width="10%" align="right">
						申请内容：
					</td>
					<td height="27" colspan="5" align="left"><%=tisp.getContent() == null ? "" : tisp.getContent()%></td>
				</tr>
			</table>

			<TABLE align=center width="95%" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<th colspan="6" align="left">
					<img class="nav" src="../../../img/subNav.gif" />
					审批明细
				</th>
				<tr>

					<th>
						审批时间
					</th>
					<th>
						审批人员
					</th>
					<th>
						人员部门
					</th>
					<th>
						审批状态
					</th>
					<th>
						审批意见
					</th>
				</tr>
				
				<% 
				TtIfServicecarExtPO temp = new TtIfServicecarExtPO();
				for (int i=0;i<ls.size();i++) {
				temp = ls.get(i);
				%>
					<tr class="table_list_row1">
					<td><%=temp.getAuditDate() == null ? "" : df.format(temp
					.getAuditDate())%></td>
					<td>
						<span class="tbwhite"><%=temp.getAuditByName() == null ? "" : temp
					.getAuditByName()%></span>
					</td>
					<td>
						<span class="tbwhite"><%=temp.getDeptName() == null ? "" : temp
							.getDeptName()%>
						</span>
					</td>
					<td><script type="text/javascript">var status = getItemValue('<%=temp.getAuditStatus() == null ? "" : temp.getAuditStatus()%>');document.write(status);</script></td>
					<td><%=temp.getAuditContent() == null ? "" : temp
					.getAuditContent()%></td>
					</tr>
					<% 
					}
					%>
			</table>



		</form>



	</BODY>
</html>

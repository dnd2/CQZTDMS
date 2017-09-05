<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.bean.BackChangeApplyMantainBean"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%
	String contextPath = request.getContextPath();
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
	BackChangeApplyMantainBean MantainBean = (BackChangeApplyMantainBean)request.getAttribute("MantainBean");
	List<BackChangeApplyMantainBean> MantainList = (List)request.getAttribute("MantainList");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<TITLE>退换车申请书技术支持室审核</TITLE>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<BODY>

<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理&gt;信息反馈提报&gt;退换车申请书技术支持室审核</div>
 <form method="post" name="fm" id="fm">
 <input type="hidden" name="ORDER_ID" id="ORDER_ID" value="<%=MantainBean.getOrderId()%>" />
 <table class="table_edit">
  <th colspan="7"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />审核操作</th>
          <tr > 
            <td height="12" align=left>审核意见：</td>
            <td align=left><span class="tbwhite">
            <textarea  name='AUDIT_CONTENT'  id='AUDIT_CONTENT' datatype="1,is_textarea,200"  rows='2' cols='60' ></textarea>
            <input type="button" onclick="backChangeApproveServiceAudit('<%=contextPath%>/feedbackmng/approve/BackChangeApproveService/backChangeApproveServiceAudit.do','p');" class="normal_btn" style="width=8%" value="通过"/>
            <input type="button" onclick="backChangeApproveServiceAudit('<%=contextPath%>/feedbackmng/approve/BackChangeApproveService/backChangeApproveServiceAudit.do','r')" class="normal_btn" style="width=8%" value="驳回"/>
			<input type="button" onClick="javascript:history.go(-1);" class="normal_btn" style="width=8%" value="返回"/>
            </span></td>
          </tr>
   </table>
        <br />
  <table width=100% border="0"  cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_info">
	      <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 基本信息</th>
          <tr bgcolor="F3F4F8">
            <td align="right">工单号：</td>
            <td><%=MantainBean.getOrderId()%></td>
            <td align="right">申请单位代码：</td>
            <td height="27" align="left" ><%=MantainBean.getDealerCode()==null?"":MantainBean.getDealerCode()%></td>
            <td align="right"  >申请单位名称：</td>
            <td><%=MantainBean.getDealerName()==null?"":MantainBean.getDealerName()%></td>
          </tr>
          <tr bgcolor="F3F4F8">
            <td align="right">服务中心经理：</td>
            <td><%=MantainBean.getLinkManager()==null?"":MantainBean.getLinkManager()%></td>
            <td align="right">服务中心经办人：</td>
            <td><%=MantainBean.getLinkMan()==null?"":MantainBean.getLinkMan()%></td>
            <td height="27" align="right" ><!-- 退换类型： --></td>
            <td align="left"  >
              <script type='text/javascript'>
		      	 	//var exType = getItemValue('<%=MantainBean.getExType()%>');
		       		//document.write(exType) ;
			 </script> 
            </td>
          </tr>
          <tr >
            <td align="right" >车辆VIN码：</td>
            <td><%=MantainBean.getVin()==null?"":MantainBean.getVin()%></td>
            <td height="27" align="right">车系：</td>
            <td align="left" ><%=MantainBean.getGroupName()==null?"":MantainBean.getGroupName()%></td>
            <td align="right" >发动机号：</td>
            <td><%=MantainBean.getEngineNo()==null?"":MantainBean.getEngineNo()%></td>
          </tr>
          <tr bgcolor="F3F4F8">
            <td height="27"  align="right" bgcolor="F3F4F8">出厂日期：</td>
            <td bgcolor="F3F4F8"><%=MantainBean.getProductionDate()==null?"":MantainBean.getProductionDate().substring(0,10)%></td> 
            <td align="right" bgcolor="F3F4F8">购车日期：</td>
            <td bgcolor="F3F4F8" ><%=MantainBean.getSellTime()==null?"":MantainBean.getSellTime().substring(0,10)%></td>
            <td width="12%" align="right" bgcolor="F3F4F8" >行驶里程（KM）：</td>
            <td ><%=MantainBean.getMileage()==null?"":MantainBean.getMileage()%></td>
          </tr>
          <tr >
            <td height="27" align="right">客户姓名：</td>
            <td align="left" ><%=MantainBean.getCustomerName()==null?"":MantainBean.getCustomerName()%></td>
            <td align="right">客户联系电话：</td>
            <td><%=MantainBean.getCurtPhone()==null?"":MantainBean.getCurtPhone()%></td>
            <td align="right">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr >
            <td height="27" align="right">客户联系地址：</td>
            <td height="27" colspan="5" align="left" ><%=MantainBean.getCurtAddress()==null?"":MantainBean.getCurtAddress()%></td>
          </tr>
          <tr >
            <td height="27" align="right">问题描述：</td>
          <td height="27" colspan="5" align="left" ><span class="tbwhite">
              <%=MantainBean.getProblemDescribe()==null?"":MantainBean.getProblemDescribe()%>
            </span></td>
          </tr>
          <tr >
            <td height="27" align="right">用户要求如何：</td>
          <td height="27" colspan="5" align="left" ><span class="tbwhite">
              <%=MantainBean.getUserRequest()==null?"":MantainBean.getUserRequest()%>
            </span></td>
          </tr>
          <tr >
            <td height="27" align="right">建议处理方式：</td>
          <td height="27" colspan="5" align="left" ><span class="tbwhite">
              <%=MantainBean.getAdviceDealMode()==null?"":MantainBean.getAdviceDealMode()%>
            </span></td>
          </tr>
          <tr >
            <td height="27" align="right">费用明细：</td>
          <td height="27" colspan="5" align="left" ><span class="tbwhite">
              <%=MantainBean.getCostDetail()==null?"":MantainBean.getCostDetail()%>
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
               <c:forEach var="MantainList" items="${MantainList}">
		          <tr class="table_list_row1">
			            <td> 
				            <script type='text/javascript'>
						       var adate = '${MantainList.auditDate}';
						       document.write(adate.substring(0,19)) ;
							</script>			            
			            </td>
			            <td><span class="tbwhite">
			            <c:out value="${MantainList.auditBy}"></c:out>
			            </span></td>
			            <td><span class="tbwhite">
			            <c:out value="${MantainList.auditBy}"></c:out>
			            </span>
			            </td>
			            <td>
			           <script type='text/javascript'>
						       var exStatus = getItemValue('${MantainList.exStatus}');
						       document.write(exStatus) ;
						</script>
			            </td>
			            <td>
			            <c:out value="${MantainList.auditContent}"></c:out>
			            </td>
		           </tr>
	        </c:forEach>
		
   </table>
 </form>
</BODY>
</html>
<script>
//验证
function backChangeApproveServiceAudit(url,type){
		var content = document.getElementById("AUDIT_CONTENT").value;
		var orderid = document.getElementById("ORDER_ID").value;
		if(type == 'r'){
			if(content != null && content != ''){
				if(content.length > 0 && content.length <= 200){
					submitFun(url,type,content,orderid);
				}else{
					MyAlert('审核意见最多只能输入200个字符！');
				}
				
			}else{
				MyAlert('审核驳回，必须填写审核意见！');
			}
		}else{
			if(content.length <= 200){
				submitFun(url,type,content,orderid);
			}else{
				MyAlert('审核意见最多只能输入200个字符！');
			}
		}
}
//提交方法；
function submitFun(url,type,content,orderid){
	fm.action = url+"?audit="+type+"&content="+content+"&orderid="+orderid;
	MyConfirm("确认操作?",fm.submit);
	//fm.submit();
}
</script>
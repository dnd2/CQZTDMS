<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> 
<%@ page import="com.infodms.dms.po.TtIfServicecarExtPO"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<%
	TtIfServicecarExtPO tisp = (TtIfServicecarExtPO)request.getAttribute("servicecarBean");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	List<TtIfServicecarExtPO> ls = (List<TtIfServicecarExtPO>)request.getAttribute("auditDetails");
	String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>服务车申请表资料上传</title>
<script type="text/javascript">
function getUploadMaterial(value){
	submitForm(fm);
	fm.action = '<%=contextPath%>/feedbackmng/approve/ServiceCarApplyDownload/serviceGetCarapplyDownloadMaterial.do?type='+value;
	MyConfirm("确认操作?",fm.submit);                
}
</script>
</head>

<body>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：  信息反馈管理&gt;信息反馈提报&gt;服务车申请表资料上传</div>
 <form method="post" name = "fm" >
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
  	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">
  		<tr>
  			<td>
  			备注:
  			</td>
  			<td>
  				<textarea id="remark" name="remark" rows='3' cols='60'></textarea>
  				<input id="ywzj" name="ywzj" type="hidden" value ="<%=tisp.getId()%>"/>
  				<input class="normal_btn" type="button" name="addBtn" value="签收"  onclick="getUploadMaterial(1);"/>
  				<input class="normal_btn" type="button" name="addBtn" value="拒签"  onclick="getUploadMaterial(2);"/>
		 	  	<input type="button" onclick="javascript:history.go(-1);" class="normal_btn" style="width=8%" value="返回"/>
  			</td>
  		</tr>
	</table> 
 <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">
	       <th colspan="6"><img class="nav" src="../../../img/subNav.gif" /> 基本信息&nbsp;&nbsp;&nbsp;&nbsp; <font color="#FF0000">(申请的车辆只能用于长安轿车的售后服务工作，并按长安轿车要求制作形象。一年内不得转让)</font></th>
           <tr>
             <td class="table_edit_3Col_label_7Letter">工单号:</td>
             <td width="23%"><%=tisp.getOrderId()==null?"":tisp.getOrderId() %>
             	<input type="hidden" name="ro_no" value="<%=tisp.getOrderId()==null?"":tisp.getOrderId() %>"/>
             </td>
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
          <c:forEach var = "detail" items="${list}">
          <tr>
          	<td><fmt:formatDate value="${detail.auditDate}" pattern="yyyy-MM-dd"/></td>
          	<td>${detail.userName}</td>
          	<td>${detail.orgName}</td>
          	<td><input type="hidden" id="ssss" value="${detail.auditStatus}"/>
          		<script>
          			var s = $('ssss').value;
          			var ss = getItemValue('${detail.auditStatus}');
          			document.write(ss);
          		</script>
          	</td>
          	<td>${detail.auditContent}</td>
          	</tr>
          </c:forEach>
   </table>  
</form>
</body>

</html>

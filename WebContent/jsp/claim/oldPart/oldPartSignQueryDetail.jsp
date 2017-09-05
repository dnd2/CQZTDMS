<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page
	import="com.infodms.dms.bean.ClaimApproveAndStoredReturnInfoBean"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@page import="java.util.List"%>

<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.Map"%><html
	xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件审批入库</title>
<%
	String contextPath = request.getContextPath();
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	List<Map<String, Object>> detailList1 = (List) request.getAttribute("detailList1");
	ClaimApproveAndStoredReturnInfoBean detailBean = (ClaimApproveAndStoredReturnInfoBean) request.getAttribute("returnListBean");
%>
<script type="text/javascript">
function init(){
		$('PART_PAKGE').disabled=true;
		$('PART_MARK').disabled=true;
		$('PART_DETAIL').disabled=true;
		$('OUT_PART_PAKGE').disabled=true;
		}
</script>
</head>
<body onload="init();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;索赔旧件签收明细</div>
<form method="post" name="fm" id="fm">
<table class="table_edit" >
	<tr>
		<th colspan="10">
					<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
					&nbsp;基本信息</a>
				</th>
	</tr>
	</table>
	<table class="table_edit" id="baseTabId">
	<tr bgcolor="F3F4F8">
		<td align="right">经销商代码：</td>
		<td><%=CommonUtils.checkNull(detailBean.getDealer_code())%></td>
		<td align="right">经销商名称：</td>
		<td><%=CommonUtils.checkNull(detailBean.getDealer_name())%></td>
		<td align="right">所属区域：</td>
		<td><%=CommonUtils.checkNull(detailBean.getAttach_area())%></td>
	</tr>
	<tr bgcolor="F3F4F8">
		<td align="right">回运清单号：</td>
		<td><%=CommonUtils.checkNull(detailBean.getReturn_no())%></td>
		<td align="right">提报日期：</td>
		<td><%=CommonUtils.checkNull(detailBean.getReturn_date())%></td>
		<td align="right">旧件回运起止时间：</td>
		<td colspan="1">
			<%
				for (int i = 0; i < detailList1.size(); i++) {
				Map<String, Object> detailMap1 = detailList1.get(i);
			%>
			<%=CommonUtils.getDataFromMap(detailMap1, "WR_START_DATE")%>
			<%} %>
		</td>
	</tr>
	
	<tr bgcolor="F3F4F8">
		<td align="right">货运方式：</td>
		<td><%=CommonUtils.checkNull(detailBean.getTransport_desc())%></td>
		<td align="right">回运类型：</td>
		<td   ><%=CommonUtils.checkNull(detailBean.getReturn_desc())%></td>
		<td align="right">物流公司：</td>
		<td colspan="3" ><%=CommonUtils.checkNull(detailBean.getTransportName())%></td>
	</tr>
	<tr bgcolor="F3F4F8">
		<td align="right">申请运费：</td>
		<td><%=CommonUtils.checkNull(detailBean.getPrice())%>(元)</td>
		<td align="right">审核运费：</td>
		<td ><%=CommonUtils.checkNull(detailBean.getNewPrice())%>(元)</td>
		<td align="right" nowrap="nowrap" >是否有外箱封面：</td>
	         <td align="left" nowrap="nowrap">
	          <script type="text/javascript">
	            genSelBoxExp("OUT_PART_PAKGE",<%=Constant.IF_TYPE%>,"<%=CommonUtils.checkNull(detailBean.getOutPartPackge()) %>",false,"short_sel","","false",'');
	           </script>
	          </td>
	</tr>

	<tr bgcolor="F3F4F8" id="bb">
		<td align="right">装箱总数：</td>
		<td><%=CommonUtils.checkNull(detailBean.getParkage_amount())%></td>
		<td align="right">实到箱数：</td>
		<td ><input class="short_txt" disabled="disabled"  name="REAL_BOX_NO" id="REAL_BOX_NO"  value="<%=CommonUtils.checkNull(detailBean.getRealBoxNo()) %>" />&nbsp;&nbsp;<span style="color:red">*</span> </td>
		<td align="right">发运单号：</td>
		<td ><input class="middle_txt" maxlength="25" disabled="disabled"  name="TRANSPORT_NO" id="TRANSPORT_NO" value="<%=CommonUtils.checkNull(detailBean.getTransport_no()) %>"/> </td>
	</tr>
	
	<tr bgcolor="F3F4F8" id="aa">
		<td align="right" nowrap="nowrap" >包装情况：</td>
	         <td align="left" nowrap="nowrap">
	          <script type="text/javascript">
	            genSelBoxExp("PART_PAKGE",<%=Constant.OLD_PART_PAKGE%>,"<%=CommonUtils.checkNull(detailBean.getPartPakge()) %>",true,"short_sel","","true",'');
	           </script>
	          </td>
	          <td align="right" nowrap="nowrap" style="display:none;">故障卡情况：</td>
	         <td align="left" nowrap="nowrap" style="display:none;">
	          <script type="text/javascript">
	            genSelBoxExp("PART_MARK",<%=Constant.OLD_PART_MARK%>,"<%=CommonUtils.checkNull(detailBean.getPartMark()) %>",true,"short_sel","","true",'');
	           </script>
	          </td>
	          <td align="right" nowrap="nowrap" >清单情况：</td>
	         <td align="left" nowrap="nowrap">
	          <script type="text/javascript">
	            genSelBoxExp("PART_DETAIL",<%=Constant.OLD_PART_DETAIL%>,"<%=CommonUtils.checkNull(detailBean.getPartDetail()) %>",true,"short_sel","","true",'');
	           </script>
	          </td>
	</tr>
	</tr>	
		<tr bgcolor="F3F4F8" >
		<td align="right">签收备注：</td>
		<td colspan="15">
			<textarea name="signRemark" id="signRemark" disabled="disabled" rows="5" onblur="checkMax();" cols="80"><%=CommonUtils.checkNull(detailBean.getSignRemark()) %></textarea>
		</td>
	</tr>
</table>
<!-- 添加附件 -->
			<table class="table_info" border="0" id="file">
				<input type="hidden" id="fjids" name="fjids"/>
			    <tr colspan="8">
			        <th>
					<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
					&nbsp;附件列表：
					</th>
					<th><span align="left"><input type="button" class="normal_btn" disabled="disabled"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/></span>
					</th>
				</tr>
				<tr>
      				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
    			</tr>
    			<%for(int i=0;i<attachLs.size();i++) { %>
    			<script type="text/javascript">
    			//	addUploadRowByDb('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
    			addUploadRowByDbView('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
    			</script>
    			<%} %>
 			</table>
 	<table class="table_edit" border="0" id="file">
	    	<tr bgcolor="F3F4F8">
    			<td width="100%" colspan="2">&nbsp;</td>
  			</tr>
		</table> 

<table class="table_list" id="id2">
	<tr>
		<td height="10" align="center" colspan="2"></td>
	</tr>
	<tr>
		<td height="10" align="center" colspan="2">
			<input type="button" onclick="history.back();"class="normal_btn" value="返回" />
		</td>
	</tr>
</table>
</form>
</body>
</html>

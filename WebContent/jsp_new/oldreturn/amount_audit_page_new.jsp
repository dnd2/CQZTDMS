<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.Map"%>
<head> 
<%  
	String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
	List<Map<String, Object>> detailList1 = (List) request.getAttribute("detailList1");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<title>索赔旧件运费审批</title>
<script type="text/javascript">
	function audit(){
		var price=$("#price").val();
		var price1 = $("#price1").val();
		var signRemark = $("#signRemark1").val();
		var reg2 = /^(\d+\.\d{1,2}|\d+)$/;
		 if(price==null || price ==""){
			MyAlert("请输入审核运费!");
			return;
		}else if(!reg2.test(price)){
			MyAlert("审核运费为最多2位小数的数字!");
			return;
		}else if (parseFloat(price)>parseFloat(price1)){
			MyAlert("审核运费不能大于申请运费!");
			return;
		}else if ((parseFloat(price)<parseFloat(price1))&&(signRemark==null||signRemark=="")){
			MyAlert("扣减运费时必须填写备注!");
			return;
		}
		MyConfirm("是否确认审核该运费为"+price+"？",auditSure,"");
	}
	function auditSure(){
		var id=$("#id").val();
		var price=$("#price").val();
		var signRemark = $("#signRemark1").val();
		var url='<%=contextPath%>/OldReturnAction/returnAmountAuditNew.json?id='+id+'&price='+price;
		makeNomalFormCall1(url,function(json){
			if(json.succ=="1"){
    			MyAlert("提示：审核成功！");
    			window.location.href='<%=contextPath%>/OldReturnAction/returnAmountAuditListNew.do?query=true';
    		}else{
    			MyAlert("提示：审核失败！");
    		}
		},'fm');
	}
	function backTo(){
		window.location.href='<%=contextPath%>/OldReturnAction/returnAmountAuditList.do?query=true';
	}
</script>
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;索赔旧件运费审批</div>
</div>
<form name="fm" id="fm" method="post">
<input type="hidden" name="id" id="id" value="${t.ID }"/>
<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<th colspan="8">
		<img class="nav" src="../jsp_new/img/subNav.gif"/>基本信息
	</th>
	<table class="table_edit" id="baseTabId">
	<tr bgcolor="F3F4F8">
		<td align="right"nowrap="true">经销商代码：</td>
		<td>${t.DEALER_CODE }</td>
		<td align="right"nowrap="true">经销商名称：</td>
		<td>${t.DEALER_NAME }</td>
		<td align="right"nowrap="true">所属区域：</td>
		<td>${t.ATTACH_AREA }</td>
	</tr>
	<tr bgcolor="F3F4F8">
		<td align="right"nowrap="true">回运清单号：</td>
		<td>${t.RETURN_NO }</td>
		<td align="right"nowrap="true">回运类型：</td>
		<td >${t.RETURN_DESC }</td>
		<td align="right"nowrap="true">旧件回运起止时间：</td>
		<td colspan="1">
			 <% if(detailList1!=null){
				for (int i = 0; i < detailList1.size(); i++) {
				Map<String, Object> detailMap1 = detailList1.get(i);
			%>
			<%=CommonUtils.getDataFromMap(detailMap1, "WR_START_DATE")%>
			<%} }%> 
		</td>
	</tr>
	
	<tr bgcolor="F3F4F8">
		<td align="right" nowrap="true">发运时间：</td>
		<td id="fay_time">${t.CREATE_DATE }
		<td align="right"nowrap="true">货运方式：</td>
		<td>
		${t.TRANSPORT_DESC }
		</td>
		<td align="right"nowrap="true">发运单号：</td>
		<td >${t.TRAN_NO } </td>
	</tr>

	<tr bgcolor="F3F4F8" id="bb">
		<td align="right" nowrap="true">装箱总数：</td>
		<td>${t.PARKAGE_AMOUNT }</td>
		<td align="right" nowrap="true">实到箱数：</td>
		<td >
		${t.REAL_BOX_NO }
		</td>
		<td align="right" nowrap="true">物流公司：</td>
		<td ><a href="<%=contextPath%>/claim/oldPart/ClaimOldPartTransportManager/oemTransportCheck.do?TRANSPORT_ID=${t.TRANSPORT_ID }&VIEWORCHECK=2">${t.TRANSPORT_NAME}</td>
		</tr>
	 <tr bgcolor="F3F4F8" id="bb">
		<td align="right"nowrap="true">申请运费：</td>
		<td ><input type="hidden" id="price1" value="${t.PRICE }">${t.PRICE }(元)</td>
		<td align="right" nowrap="true">审核运费：</td>
		<td  ><input class="middle_txt" maxlength="25" name="price" id="price" value="${t.PRICE }"/><span style="color: red">*</span>(元) </td>
		
		</tr>
		
	<tr bgcolor="F3F4F8" id="aa">
		<td align="right" nowrap="nowrap" >包装情况：</td>
	         <td align="left" nowrap="nowrap">
	         <change:tcCode value="${t.PART_PACKGE}" showType="0"></change:tcCode>
	          </td>
	          <td align="right" nowrap="nowrap" style="display:none;">故障卡情况：</td>
	           <change:tcCode value="${t.PART_MARK}" showType="0"></change:tcCode>
	          </td>
	          <td align="right" nowrap="nowrap" >清单情况：</td>
	         <td align="left" nowrap="nowrap">
	          <change:tcCode value="${t.PART_DETAIL}" showType="0"></change:tcCode>
	          </td>
	</tr>	 
	<tr bgcolor="F3F4F8" >
		<td align="right" nowrap="true">申请备注：</td>
		<td colspan="5"nowrap="true">
			<textarea name="signRemark" readonly="readonly"  id="signRemark"  rows="3" cols="120">${t.REMARK}</textarea>
		</td>
	</tr>
		<tr bgcolor="F3F4F8" >
		<td align="right"nowrap="true">审核备注：</td>
		<td colspan="5"nowrap="true">
			<textarea name="signRemark1" id="signRemark1" rows="3" cols="120">${t.SIGN_REMARK}</textarea>
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
				</tr>
				<tr>
      				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
    			</tr>
    			<%if(fileList!=null){
	  				for(int i=0;i<fileList.size();i++) { %>
					  <script type="text/javascript">
				    		addUploadRowByDbView('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
				    	</script>
				<%}}%>
 			</table> 
 <table class="table_edit" border="0" id="file">
	    	<tr bgcolor="F3F4F8">
    			<td width="100%" colspan="2">&nbsp;</td>
  			</tr>
		</table> 
		
<table class="table_list" style="text-align: center;">
	<tr>
		<td height="10" align="center" >
		<input type="button" onclick="audit();" class="normal_btn" value="审核" />&nbsp;&nbsp;
		<input type="button" onclick="history.go(-1);"class="normal_btn" value="返回" />
		</td>
	</tr>
</table>
</form>
</body>
</html>
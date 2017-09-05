<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<head> 
<%  String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/zyw/jquery-1.3.2.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/js/jslib/zyw/jquery-calendar.js"></script> 
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/js/jslib/zyw/jquery-calendar.css" /> 
<title>质保手册服务站</title>
<script type="text/javascript">
	$(function(){
		var type=$("#type").val();
		if("audit"==type){
			$("input[type='text']").attr("readonly","readonly");
			$("#send_no").removeAttr("readonly","readonly");
			$("#report_remark").attr("readonly","readonly");
			$(".normal_btn").each(function(){
				 if($(this).val()=="删 除" || $(this).val()=="添加附件"){
					 $(this).hide();
				} 
			});
			$("input[name='del']").each(function(){
				$(this).attr("disabled",true);
			});
			$("#add").attr("disabled",true);
		}
	});
	function audit(){
		var temp=0;
		var audit_remark=$.trim($("#audit_remark").val());
		if( ""==audit_remark){
			MyAlert("提示：审核需要填写备注!");
			temp++;
			return;
		}
		if(temp==0){
			MyConfirm("是否确认保存？",auditCommit,"");
		}
	}
	function auditCommit(){
		$("#auditPass").attr("disabled",true);
		var url="<%=contextPath%>/WarrantyManualAction/auditWarrantyManual.json";
		makeNomalFormCall1(url,auditCommitBack,"fm");
	}
	function auditCommitBack(json){
		var str="审核";
		if(json.succ=="1"){
			MyAlert("提示："+str+"成功！");
			var url='<%=contextPath%>/WarrantyManualAction/warrantyManualListTemp.do';
			window.location.href=url;
		}else{
			MyAlert("提示："+str+"失败！");
			$("#auditPass").attr("disabled",false);
		}
	}
</script>
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;车辆信息管理&gt;质保手册
</div>
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="type" value="${type }" name="type" type="hidden"  />
<input class="middle_txt" id="id" value="${t.ID }" name="id" type="hidden"  />
<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<th colspan="8">
		<img class="nav" src="../jsp_new/img/subNav.gif"/>服务站信息
	</th>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >服务站名称：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="dealer_shortname" readonly="readonly"  value="${t.DEALER_SHORTNAME }" name="dealer_shortname" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >服务站代码：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="dealer_code" readonly="readonly" value="${t.DEALER_CODE }"  name="dealer_code" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" >报告编号：</td>
    	<td nowrap="true" width="15%" >
			<input class="middle_txt" id="report_no" readonly="readonly" value="${t.REPORT_NO }"  name="report_no" type="text"  />
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >服务商联系人：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="dealer_contact_person"  value="${t.DEALER_CONTACT_PERSON }" name="dealer_contact_person" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >服务商联系电话：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="dealer_contact_phone" value="${t.DEALER_CONTACT_PHONE }"  name="dealer_contact_phone" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" >申报时间：</td>
    	<td nowrap="true" width="15%" >
			<input class="middle_txt" id="create_date"  readonly="readonly" value="<fmt:formatDate value="${t.CREATE_DATE }" pattern='yyyy-MM-dd hh:ss:mm'/>"  name="create_date" type="text" maxlength="30" />
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >发运单号：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="send_no"  value="${t.SEND_NO }" name="send_no" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >发运时间：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="send_date" onfocus="$(this).calendar()"  value="<fmt:formatDate value="${t.SEND_DATE }" pattern='yyyy-MM-dd HH:mm'/>" readonly="readonly"  name="send_date" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" ></td>
    	<td nowrap="true" width="15%" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
</table>
<table id="sub" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<th colspan="10">
		<img class="nav" src="../jsp_new/img/subNav.gif" />辅料项目&nbsp;&nbsp;
	</th>
	<tr>
		<td nowrap="true" width="8%" >序号</td>
		<td nowrap="true" width="10%" >VIN</td>
		<td nowrap="true" width="10%" >车主姓名</td>
		<td nowrap="true" width="10%" >车主电话</td>
		<td nowrap="true" width="10%" >购车日期</td>
		<td nowrap="true" width="10%" >车型</td>
		<td nowrap="true" width="10%" >付费方式</td>
		<td nowrap="true" width="22%" >申请原因</td>
		<td nowrap="true" width="10%" >操作</td>
	</tr>
	 <c:forEach items="${list }" var="t" varStatus="status">
		<tr>
			<td nowrap="true" width="8%" >${status.index+1}<input class="middle_txt"  value="${t.ID }" name="sub_id" type="hidden"  /></td>
			<td nowrap="true" width="10%" ><input type="text" name="vin"  class="middle_txt" value="${t.VIN }" /></td>
			<td nowrap="true" width="10%" >${t.CTM_NAME }</td>
			<td nowrap="true" width="10%" >${t.MAIN_PHONE }</td>
			<td nowrap="true" width="10%" >${t.PURCHASED_DATE }</td>
			<td nowrap="true" width="10%" >${t.MODEL_CODE }</td>
			<td nowrap="true" width="10%" >
			<script type="text/javascript">
	         		genSelBoxExp("pay_type",'9443',"${t.PAY_TYPE}",true,"short_sel","","false",'');
	         	</script>
			</td>
			<td nowrap="true" width="22%" ><input type="text" name="report_remark"  class="long_txt" value="${t.REPORT_REMARK }"/></td>
			<td nowrap="true" width="10%" ><input type="radio" checked="checked" name="operation${t.ID }" value="1"/>同意<input type="radio" name="operation${t.ID }" value="0"/>不同意</td>
		</tr>
	</c:forEach>
</table>
<!-- 添加附件 开始  -->
        <table id="add_file"  width="75%" class="table_info" border="0" id="file">
	    		<tr>
	        		<th>
						<input type="hidden" id="fjids" name="fjids"/>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
			     		<input type="button" class="normal_btn" align="right" id="addfile" onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
					</th>
				</tr>
				<tr>
    				<td width="75%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  				<%if(fileList!=null){
	  				for(int i=0;i<fileList.size();i++) { %>
					  <script type="text/javascript">
				    		addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
				    	</script>
				<%}}%>
			</table> 
  		<!-- 添加附件 结束 -->
  		<table border="0" id="tab_2" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
		<tr>
				<td nowrap="true" width="5%" ></td>
				<td nowrap="true" width="45%" style="font-weight: bold;">申请备注</td>
				<td nowrap="true" width="45%" style="font-weight: bold;">审核备注</td>
				<td nowrap="true" width="5%" ></td>
		</tr>
		<tr>
				<td nowrap="true" width="5%" ></td>
				<td nowrap="true" width="45%" >
					<textarea style="font-weight: bold;" name="remark" id="remark" rows="4" cols="50">${t.REMARK }</textarea>
				</td>
				<td nowrap="true" width="45%" >
					<textarea style="font-weight: bold;" name="audit_remark" id="audit_remark" rows="4" cols="50">${t.AUDIT_REMARK }</textarea>
				</td>
				<td nowrap="true" width="5%" ></td>
		</tr>
	</table>
	<br/>
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="auditPass" onclick="audit();"  style="width=8%" value="确认审核" />
               	&nbsp;&nbsp;
				<input type="button" id="back" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</body>
</html>
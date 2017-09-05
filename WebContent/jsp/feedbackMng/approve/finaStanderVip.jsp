<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath();
   List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
%>
<script type="text/javascript">

	//审批通过
	function passApply(){
		fm.action = "<%=contextPath%>/feedbackmng/approve/StandardVipFinalApproveManager/approveFinalStandardVip.do?flag=1";
		MyConfirm("确认操作?",fm.submit);
		//fm.submit();
	}
	
	//审批驳回，校验审核意见不能为空
	function rejectApply(){
		if(document.getElementById("content").value == null || document.getElementById("content").value == ""){
			 MyAlert("请填写驳回意见！");
			 fm.content.focus();
             return;
		}
		fm.action = "<%=contextPath%>/feedbackmng/approve/StandardVipFinalApproveManager/approveFinalStandardVip.do?flag=0";
		MyConfirm("确认操作?",fm.submit);
		//fm.submit();
	}

</script>

<title>合格证补办更换售后服务部审核</title>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理&gt;信息反馈提报&gt;合格证补办更换售后服务部审批</div>
 <form method="post" name="fm" id="fm" >
 	<table class="table_edit">
  		<th colspan="7"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />审核操作</th>
          <tr >
            <td height="12" align=left>审核意见：</td>
            <td align=left><span class="tbwhite">
              <textarea  name="content"  id="content" rows='2' cols='60'></textarea>
            	<input type="button" onclick="passApply()" class="normal_btn" style="width=8%" value="通过"/>
            	<input type="button" onclick="rejectApply()" class="normal_btn" style="width=8%" value="驳回"/>
            	<input class="normal_btn" type="button" name="button" value="返回"  onClick="javascript:history.go(-1);">
            </span></td>
          </tr>
        </table>
 
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">
          <th colspan="6"><img class="nav" src="../../../img/subNav.gif" /> 基本信息&nbsp;&nbsp;&nbsp;&nbsp; <font color="#FF0000">(合格证补办收费500元/张，合格证车身颜色更改：200元/张)</font></th>
		  <tr bgcolor="F3F4F8">
		    <td width="16%" align="right">工单号：</td>
		    <td width="17%">
		    	<c:out value="${svInfo.ORDER_ID}"/>
		    	<input type="hidden" name="orderId" value="<c:out value="${svInfo.ORDER_ID}"/>">
		    </td>
		    <td width="17%" height="27" align="right">服务中心代码：</td>
		    <td width="18%">
		    	<c:out value="${svInfo.DEALER_CODE}"/>
		    </td>
		    <td width="12%" height="27"  align="right" >服务中心名称：</td>
		    <td width="20%">
		    	<c:out value="${svInfo.DEALER_NAME}"/>
		    </td>
	      </tr>
		  <tr bgcolor="FFFFFF">
		    <td align="right">服务中心联系人：</td>
		    <td>
		    	<c:out value="${svInfo.LINK_MAN}"/>
		    </td>
		    <td align="right">服务中心联系电话：</td>
		    <td>
		    	<c:out value="${svInfo.TEL}"/>
		    </td>
		    <td align="right">服务中心邮编：</td>
		    <td>
				<c:out value="${svInfo.ZIP_CODE}"/>
			</td>
	      </tr>
		  <tr bgcolor="FFFFFF">
		    <td height="27" align="right">服务中心地址：</td>
		    <td colspan="5">
		    	<c:out value="${svInfo.ADDRESS}"/>
		    </td>
	      </tr>
          
		  <tr bgcolor="F3F4F8">
            <td align="right">工单类型：</td>
            <td>
            	<script type='text/javascript'>
					var name=getItemValue('<c:out value="${svInfo.ST_TYPE}"/>');
					document.write(name) ;
				</script> 
            </td>
            <td align="right" >操作类型：</td>
            <td>
            	<script type='text/javascript'>
					var name=getItemValue('<c:out value="${svInfo.ST_ACTION}"/>');
					document.write(name) ;
				</script>
            </td>
            <td align="right">&nbsp;</td>
            <td >&nbsp;</td>
          </tr>
          <tr>
            <td height="27"  align="right">申请内容：</td>
            <td colspan="5">
            	<c:out value="${svInfo.ST_CONTENT}"/>
            </td> 
          </tr>
     </table> 
 	 <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">
            <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 车辆信息</th>
		  
		  <tr bgcolor="F3F4F8">
		    <td align="right" >车辆识别码(VIN)：</td>
		    <td ><c:out value="${svInfo.VIN}"/></td>
		    <td height="27"  align="right" >车型：</td>
		    <td ><c:out value="${svInfo.GROUP_NAME}"/></td>
		    <td align="right" >发动机号：</td>
		    <td ><c:out value="${svInfo.ENGINE_NO}"/></td>
	      </tr>
		  <tr bgcolor="F3F4F8">
		    <td height="27"  align="right">颜色：</td>
		    <td><c:out value="${svInfo.COLOR}"/></td>
		    <td align="right" >生产日期：</td>
		    <td><c:out value="${svInfo.PRODUCT_DATE}"/></td>
		    <td align="right">购车日期：</td>
		    <td><c:out value="${svInfo.PURCHASED_DATE}"/></td>
	      </tr>
          <tr bgcolor="F3F4F8">
            <td height="27" align="right">车主姓名：</td>
            <td><c:out value="${svInfo.CUSTOMER_NAME}"/></td>
            <td height="27"  align="right" >车主身份证号：</td>
            <td><c:out value="${svInfo.CERT_NO}"/></td>
            <td align="right">车主联系电话：</td>
            <td><c:out value="${svInfo.MOBILE}"/></td>
          </tr>
          <tr bgcolor="F3F4F8">
            <td align="right">车主地址：</td>
            <td colspan="5"><c:out value="${svInfo.ADDRESS_DESC}"/></td>
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
   <TABLE class="table_list" style="border-bottom:1px solid #DAE0EE">
		<th colspan="6" align="left"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 审批明细</th>
        <tr  bgcolor="F3F4F8">
            <th>审批时间</th>
            <th>审批人员</th>
            <th>人员部门</th>
            <th>审批状态</th>
            <th>审批意见</th>
    	</tr>
        <c:forEach items="${auditList}" var="al">
       		<tr>
            	<td><c:out value="${al.AUDIT_DATE}"/></td>
            	<td><c:out value="${al.NAME}"/></td>
            	<td><c:out value="${al.ORG_NAME}"/></td>
            	<td>
	            	<script type='text/javascript'>
	     			  var name=getItemValue(<c:out value="${al.AUDIT_STATUS}"/>);
	      			  document.write(name) ;
					</script>
            	</td>
           	 	<td><c:out value="${al.AUDIT_CONTENT}"/></td>
        	</tr>
       	</c:forEach>
   </table>    
</form>
</body>
</html>

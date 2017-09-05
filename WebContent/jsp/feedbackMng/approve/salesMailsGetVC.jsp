<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<title>服务车资料签收</title>
<% 
	String contextPath = request.getContextPath();
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("lists");
%>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：信息反馈管理 &gt;信息反馈审批 &gt;服务车资料签收</div>
<form method="post" name ="fm" id="fm">
<input type="hidden" name="id" value="${map.ID}"/>
<input type="hidden" name="type" value="${type}"/>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
		<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
	</table>
    <table class="table_edit">
    	<tr>
    		<td width="10%" align="right">服务商代码：</td>
    		<td width="20%">${map.DEALER_CODE}</td>
    		<td width="12%" align="right">服务商名称：</td>
    		<td width="20%">${map.DEALER_NAME}</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">制单日期：</td>
    		<td width="20%"><fmt:formatDate value='${map.MAKE_DATE}' pattern='yyyy-MM-dd' /></td>
    		<td width="10%" align="right">申请单位联系人：</td>
    		<td width="20%">${map.LINK_MAN}</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">申请单位电话：</td>
    		<td width="20%">${map.LINK_PHONE}</td>
    		<td width="10%" align="right">申请单位传真：</td>
    		<td width="20%">${map.FAX_NO}</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">经销商名称：</td>
    		<td width="20%">${map.DEALER_NAME1}</td>
    		<td width="12%" align="right">经销商电话：</td>
    		<td width="20%">${map.DEALER_PHONE}</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">申请车型及状态：</td>
    		<td width="20%">${map.MODEL_CODE}</td>
    		<td width="12%" align="right">单位类型：</td>
    		<td width="20%">
    			<script type="text/javascript">
    				writeItemValue(${map.DEALER_LEVEL});
    			</script>
    		</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">单位类别：</td>
    		<td width="20%">
    			<script type="text/javascript">
    				writeItemValue(${map.DEALER_TYPE});
    			</script>
    		</td>
    		<td width="12%" align="right">单位等级：</td>
    		<td width="20%"></td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">VIN：</td>
    		<td width="20%">${map.VIN}</td>
    		<td width="12%" align="right">发动机号：</td>
    		<td width="20%">${map.ENGINE_NO}</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">服务车类型：</td>
    		<td width="20%">
    			<script type="text/javascript">
    				writeItemValue(${map.CAR_TYPE});
    			</script>
    		</td>
    		<td width="12%" align="right">生产厂家：</td>
    		<td width="20%">
    			<script type="text/javascript">
    				writeItemValue(${map.YIELDLY});
    			</script>
    		</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">申请车标准价：</td>
    		<td width="20%">${map.STANDARD_PRICE}</td>
    		<td width="12%" align="right">申购车折让总支持额度：</td>
    		<td width="20%">${map.SUPPORT_QUOTA}</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">申购车第一年支持额度：</td>
    		<td width="20%">${map.SUPPORT_QUOTA2}</td>
    		<td width="12%" align="right">申购车第一年优惠后购车金额：</td>
    		<td width="20%">${map.DISCOUNT_AMOUNT}</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">申请备注：</td>
    		<td colspan="3">${map.REMARK}</td>
    	</tr>
    	<tr>
    		<td colspan="4">&nbsp;</td>
    	</tr>
    </table>
    <br/>
    <table class="table_info" border="0" id="file">
			<input type="hidden" name="fjids"/>
		<tr colspan="8">
			<th>
				<img class="nav" src="../../../img/subNav.gif" />&nbsp;附件列表：
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2">
				<jsp:include page="${contextPath}/uploadDiv.jsp" />
			</td>
		</tr>
		<%for(int i=0;i<attachLs.size();i++) { %>
			<script type="text/javascript">
    			addUploadRowByDL('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
			</script>
    	<%} %>
	</table>
	<br />
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
		<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /><a href="#" onclick="showOrHid()">历史购车记录</a></th>
	</table>
    <table class="table_list" id="relation" style="display:none">
    	<tr class="table_list_row2">
    		<td>申请车型及状态</td>
    		<td>VIN</td>
    		<td>服务车类型</td>
    		<td>标准价</td>
    		<td>折让总<br />支持额度</td>
    		<td>第一年<br />支持额度</td>
    		<td>第一年优惠后<br />购车金额</td>
    		<td>申请日期</td>
    		<td>总部通过日期</td>
    	</tr>
    	<c:forEach var="r" items="${relations}" varStatus="st">
    		<tr class="table_list_row${st.index%2+1}">
    			<td>${r.MODEL_CODE}</td>
    			<td>${r.VIN}</td>
    			<td>
    				<script>
    					writeItemValue(${r.CAR_TYPE});
    				</script>
    			</td>
    			<td>${r.STANDARD_PRICE}</td>
    			<td>${r.SUPPORT_QUOTA}</td>
    			<td>${r.SUPPORT_QUOTA2}</td>
    			<td>${r.DISCOUNT_AMOUNT}</td>
    			<td>${r.REPORT}</td>
    			<td>${r.PASS}</td>
    		</tr>
    	</c:forEach>
    </table>
    <br/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
		<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /><a href="#" onclick="showOrHid2()">审核记录</a></th>
	</table>
    	<br />
	    <table class="table_list" id="auditTab" style="display:none">
    	<tr class="table_list_row2">
    		<td>审核人</td>
    		<td>审核时间</td>
    		<td>审核结果</td>
    		<td>审核意见</td>
    	</tr>
    	<c:forEach var="audit" items="${list}" varStatus="st">
    		<tr class="table_list_row${st.index%2+1}">
    			<td>${audit.NAME}</td>
    			<td>
    				<fmt:formatDate value='${audit.CREATE_DATE}' pattern='yyyy-MM-dd' />
    			</td>
    			<td>
    				<script type="text/javascript">
    					writeItemValue(${audit.AUDIT_STATUS});
    				</script>
    			</td>
    			<td>${audit.REMARK}</td>
    		</tr>
    	</c:forEach>
    </table>
    <br />
    <table class="table_edit">
    	<tr>
    		<td width="10%" align="right">审核意见：</td>
    		<td colspan="3">
    			<textarea cols="60" rows="3" name="remark"></textarea>
    		</td>
    	</tr>
    	<tr>
    		<td colspan="2">&nbsp;</td>
    	</tr>
    	<tr>
    		<td colspan="4" align="center">
    			<input type="button" value="通过" id="btn_01" class="normal_btn" onclick="approve(<%=Constant.SERVICE_CAR_FILES_12%>)"/>&nbsp;
    			<input type="button" value="退回" id="btn_02" class="normal_btn" onclick="approve(<%=Constant.SERVICE_CAR_FILES_13%>)"/>&nbsp;
    			<input type="button" value="拒绝" id="btn_03" class="normal_btn" onclick="approve(<%=Constant.SERVICE_CAR_FILES_14%>)"/>&nbsp;
    			<input type="button" value="返回" class="normal_btn" onclick="history.go(-1)"/>
    		</td>
    	</tr>
    </table>
    <br />

<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
	function approve(val){
		myBtnSet() ;
		if(val != <%=Constant.SERVICE_CAR_FILES_12%>){
			if($('remark').value.trim() == ''){
				MyAlert('审核意见为必填项！') ;
				myBtnSet2() ;
				return ;
			}
		}
		fm.action = '<%=contextPath%>/feedbackmng/apply/ServiceCarWCAction/filesApprove.do?status='+val ;
		if(confirm('确认签收？')){
			fm.submit();
		}else
			myBtnSet2() ;
	}
	function myBtnSet(){
		$('btn_01').disabled = true ;
		$('btn_02').disabled = true ;
		$('btn_03').disabled = true ;
	}
	function myBtnSet2(){
		$('btn_01').disabled = false ;
		$('btn_02').disabled = false ;
		$('btn_03').disabled = false ;
	}
	function showOrHid(){
		var sta = $('relation').style.display ;
		if(sta == 'none'){
			$('relation').style.display = 'block' ;
		}else{
			$('relation').style.display = 'none' ;
		}
	}
	function showOrHid2(){
		var sta = $('auditTab').style.display ;
		if(sta == 'none'){
			$('auditTab').style.display = 'block' ;
		}else{
			$('auditTab').style.display = 'none' ;
		}
	}
</script>
</BODY>
</html>
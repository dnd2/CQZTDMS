<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<title>服务车申请</title>
<% 
   String contextPath = request.getContextPath();
  %>
</head>
<script type="text/javascript">
function doInit(){
   loadcalendar();
}
</script>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：信息反馈管理 &gt;信息反馈提报 &gt;服务车申请事业部审批</div>
<form method="post" name ="fm" id="fm">
<input type="hidden" name="id" value="${map.ID}"/>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
		<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
	</table>
    <table class="table_edit">
    	<tr>
    		<td width="10%" align="right">服务商代码：</td>
    		<td width="20%">${map.DEALER_CODE}</td>
    		<td width="10%" align="right">服务商名称：</td>
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
    		<td width="10%" align="right">经销商电话：</td>
    		<td width="20%">${map.DEALER_PHONE}</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">申请车型及状态：</td>
    		<td width="20%">${map.MODEL_CODE}</td>
    		<td width="10%" align="right">单位类型：</td>
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
    		<td width="10%" align="right">单位等级：</td>
    		<td width="20%"></td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">服务车类型：</td>
    		<td width="20%">
    			<script type="text/javascript">
    				writeItemValue(${map.CAR_TYPE});
    			</script>
    		</td>
    		<td width="10%" align="right"></td>
    		<td width="20%"></td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">申请备注：</td>
    		<td colspan="3">${map.REMARK}</td>
    	</tr>
    </table>
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
    <c:if test="${list!=null}">
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
    </c:if>
    <br />
    <table class="table_edit">
    	<tr>
    		<td width="10%" align="right">审核意见：</td>
    		<td colspan="3">
    			<textarea cols="60" rows="3" name="remark"></textarea>
    		</td>
    	</tr>
    	<tr>
    		<td colspan="4">&nbsp;</td>
    	</tr>
    	<tr>
    		<td colspan="4" align="center">
    			<input type="button" class="normal_btn" value="通过" id="btn_01" onclick="approve(1);" />&nbsp;
    			<input type="button" class="normal_btn" value="退回" id="btn_02" onclick="approve(2);" />&nbsp;
    			<input type="button" class="normal_btn" value="拒绝" id="btn_03" onclick="approve(3);" />&nbsp;
    			<input type="button" class="normal_btn" value="返回" onclick="goBack()" />
    		</td>
    	</tr>
    </table>

<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
	function approve(val){
		myBtnSet();
		if(val == 2 || val == 3 ){
			if($('remark').value.trim() == ''){
				MyAlert('审核意见为必填项！');
				myBtnSet2();
				return ;
			}
		}
		fm.action = '<%=contextPath%>/feedbackmng/apply/ServiceCarWCAction/areaApprove.do?type='+val ;
		var flag = confirm("确定审核？");
		if(flag)
			fm.submit() ;
		else
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
	function goBack(){
		location = '<%=contextPath%>/feedbackmng/apply/ServiceCarWCAction/areaQueryInit.do' ;
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
</body>
</html>
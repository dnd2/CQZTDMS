<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<title>服务车资料上传</title>
<% 
	String contextPath = request.getContextPath();
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("lists");
%>
</head>
<script type="text/javascript">
function doInit(){
   loadcalendar();
}
</script>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：信息反馈管理 &gt;信息反馈提报 &gt;服务车资料上传</div>
<form method="post" name ="fm" id="fm">
<input type="hidden" name="id" value="${map.ID}"/>
<input type="hidden" name="oldEngine" id="oldEngine" value='${map.ENGINE_NO}'/>
<input type="hidden" name="yieldly" id="yieldly" value="${map.YIELDLY}"/>
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
			<td width="10%" align="right">VIN：</td>
			<td width="20%"><input type="text" name="vin" onchange="vinCheck()" id="vin" class="middle_txt" value='${map.VIN}' datatype="0,is_null"/></td>
			<td width="12%" align="right">发动机号：</td>
			<td width="20%"><input type="text" name="engine_no" id="engine" class="middle_txt" value='${map.ENGINE_NO}' datatype="0,is_null"/></td>
		</tr>
		<tr style="display:none">
			<td width="10%" align="right">生产厂家：</td>
			<td width="20%">
				<script type="text/javascript">
					genSelBoxExp("yieldly",<%=Constant.YIELDLY_TYPE%>,'${map.YIELDLY}',true,"short_sel","","true",'');
				</script>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<table class="table_info" border="0" id="file">
				<tr colspan="8">
					<th>
						<img class="nav" src="../../../img/subNav.gif" />&nbsp;附件列表：
						<span align="left"><input type="button" class="normal_btn"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/></span>
					</th>
				</tr>
				<tr>
					<td width="100%" colspan="2">
						<jsp:include page="${contextPath}/uploadDiv.jsp" />
					</td>
				</tr>
				<%for(int i=0;i<attachLs.size();i++) { %>
					<script type="text/javascript">
						addUploadRowByDb('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
					</script>
		    	<%} %>
			</table>
		</tr>
    </table>
    <br/>
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
	<table class="table_info">
		<tr>
    		<td colspan="4" align="center">
    			<input type="button" value="保存" id="btn_01" class="normal_btn" onclick="saveOrApply(1)"/>&nbsp;
    			<input type="button" value="提报" id="btn_02" class="normal_btn" onclick="saveOrApply(2)"/>&nbsp;
    			<input type="button" value="返回" class="normal_btn" onclick="history.go(-1)"/>
    		</td>
    	</tr>
	</table>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
	function saveOrApply(val){
		if(val==2){
			if(submitForm(fm)==false) return ;
			myBtnSet() ;
			var arr = document.getElementsByName('uploadFileId');
			if(arr.length==0){
				MyAlert('未上传购车资料！');
				myBtnSet2() ;
				return ;
			}
			var eng = $('engine').value ;
			var oldEng = $('oldEngine').value ;
			if(eng.trim()!=oldEng.trim()){
				MyAlert('VIN与发动机号不匹配！');
				myBtnSet2() ;
				return ;
			}
			if(!confirm('确认提报？')){
				myBtnSet2() ;
				return ;
			}
		}
		fm.action = '<%=contextPath%>/feedbackmng/apply/ServiceCarWCAction/filesUpload.do?type='+val ;
		fm.submit() ;
	}
	function myBtnSet(){
		$('btn_01').disabled = true ;
		$('btn_02').disabled = true ;
	}
	function myBtnSet2(){
		$('btn_01').disabled = false ;
		$('btn_02').disabled = false ;
	}
	function goBack(){
		location = '<%=contextPath%>/feedbackmng/apply/ServiceCarWCAction/applyFilesUrlInit.do' ;
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
	function vinCheck(){
		var vin = $('vin').value ;
		var url = '<%=contextPath%>/feedbackmng/apply/ServiceCarWCAction/queryEngine.json?vin='+vin ;
		sendAjax(url,checkBack,'fm');
	}
	function checkBack(json){
		if(json.flag){
			MyAlert('未找到与此VIN匹配的车辆信息！');
			$('oldEngine').value = 'XXX_XXX' ;
		}else{
			$('oldEngine').value = json.engineNo ;
			$('yieldly').value = json.yieldly ;
		}
	}
</script>
</BODY>
</html>
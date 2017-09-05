<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<title>服务车申请新增页面</title>
<% 
   String contextPath = request.getContextPath();
  %>
</head>
<script type="text/javascript">
function doInit(){
   loadcalendar();
}
</script>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：信息反馈管理 &gt;信息反馈提报 &gt;服务车申请修改</div>
<form method="post" name ="fm" id="fm">
<input type="hidden" name="id" value="${map.ID}"/>
<input type="hidden" name="model_id" id="model_id" value="${map.MODEL_ID}" />
<input type="hidden" name="d_code" id="d_code" />
<input type="hidden" name="d_id" id="d_id" value="${map.DEALER_ID2}"/>
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
    		<td width="20%">
    			<input type="text" name="linkMan" id="linkMan" datatype="0,is_null" value='${map.LINK_MAN}' class="middle_txt" />
    		</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">申请单位电话：</td>
    		<td width="20%">
    			<input type="text" name="linkPhone" id="linkPhone" datatype="0,is_null" value='${map.LINK_PHONE}' class="middle_txt" />
    		</td>
    		<td width="10%" align="right">申请单位传真：</td>
    		<td width="20%"><input type="text" name="faxNo" id="faxNo" datatype="0,is_null" class="middle_txt" value="${map.FAX_NO}"/></td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">经销商名称：</td>
    		<td width="20%">
    			<input type="text" name="d_name" id="d_name" datatype="0,is_null" value='${map.DEALER_NAME1}' class="middle_txt" readonly/>
    			<input type="button" value="..." class="mini_btn" id="mini_01" onclick="showOrgDealer('d_code','d_id','false','','true','true','<%=Constant.DEALER_TYPE_DVS %>','d_name')" />
    			<input type="button" value="清除" class="normal_btn" id="wrap_01" onclick="" />
    		</td>
    		<td width="10%" align="right">经销商电话：</td>
    		<td width="20%">
    			<input type="text" name="d_phone" id="d_phone" datatype="0,is_null" value='${map.DEALER_PHONE}' class="middle_txt" />
    		</td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">申请车型及状态：</td>
    		<td width="20%">
    			<input type="text" name="model" id="model" datatype="0,is_null" value=${map.MODEL_CODE} class="middle_txt" readonly/>
    			<input type="button" value="..." class="mini_btn" id="mini_02" onclick="selModel()" />
    			<input type="button" value="清除" class="normal_btn" id="wrap_02" onclick="setCarModel('','','')" />
    		</td>
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
    				genSelBoxExp("car_type",<%=Constant.SERVICE_CAR%>,${map.CAR_TYPE},false,"short_sel","","true",'');
    			</script>
    		</td>
    		<td width="10%" align="right"></td>
    		<td width="20%"></td>
    	</tr>
    	<tr>
    		<td width="10%" align="right">申请备注：</td>
    		<td colspan="3">
    			<textarea rows="2" cols="60" name="remark" id="remark">${map.REMARK}</textarea>
    		</td>
    	</tr>
    	<tr>
    		<td colspan="4">&nbsp;</td>
    	</tr>
    	<tr>
    		<td colspan="4" align="center">
    			<input type="button" class="normal_btn" value="保存" id="btn_01" onclick="saveOrApply(1);"/>&nbsp;
    			<input type="button" class="normal_btn" value="提报" id="btn_02" onclick="saveOrApply(2);"/>&nbsp;
    			<input type="button" class="normal_btn" value="返回" onclick="goBack()" />
    		</td>
    	</tr>
    </table>
        
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
	function saveOrApply(val){
		if(submitForm('fm')==false)return;
		$('btn_01').disabled = true ;
		$('btn_02').disabled = true ;
		fm.action = '<%=contextPath%>/feedbackmng/apply/ServiceCarWCAction/servicecarModify.do?type='+val ;
		fm.submit() ;
	}
	function goBack(){
		location = '<%=contextPath%>/feedbackmng/apply/ServiceCarWCAction/applyUrlInit.do' ;
	}
	function selModel(){
		var url = '<%=contextPath%>/common/SeriesShow/carModelUrlInit.do?' ;
		OpenHtmlWindow(url,800,500);
	}
	function setCarModel(id,code,name){
		$('model_id').value = id ;
		$('model').value = code ;
	}
	//未提报和退回状态可做修改，其它状态刚只执行查看明细
	function myBtnSet(){
		if(${map.STATUS}!='<%=Constant.SERVICE_CAR_APPLY_01%>' && ${map.STATUS}!='<%=Constant.SERVICE_CAR_APPLY_04%>'){
			$('btn_01').style.display = 'none' ;		
			$('btn_02').style.display = 'none' ;
			$('linkMan').disabled = true ;
			$('linkPhone').disabled = true ;
			$('d_phone').disabled = true ;
			$('mini_01').disabled = true ;
			$('mini_02').disabled = true ;
			$('wrap_01').disabled = true ;
			$('wrap_02').disabled = true ;
			$('car_type').disabled = true ;
			$('remark').disabled = true ;
		}
	}
	myBtnSet();
</script>
</BODY>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="/jstl/cout" %>
<%@page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>退换货解封审核配置修改</title>
    <script language="JavaScript">
    	jQuery.noConflict();//初始化jquery
        //初始化方法
    </script>
</head>
<body>
<div class="wbox">
    <div class="navigation">
    	<img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置： 备件仓储管理&gt; 备件退换货状态变更&gt; 退换货解封审核配置 &gt; 修改
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
    	<input type="hidden" name="DEALER_ID" value="${map.DEALER_ID}">
    	<input type="hidden" name="PARENT_ID" value="${map.PARENTORG_ID}">
        <table class="table_query" bordercolor="#DAE0EE">
            <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 修改条件</th>
            <tr>
                <td width="10%" align="right">配送中心编码：</td>
                <td width="20%">
                	<input class="middle_txt" type="text" id="DEALER_CODE"  name="DEALER_CODE" value="${map.DEALER_CODE}" readonly="readonly"/>
                </td>
                 <td width="10%" align="right">配送中心名称：</td>
                <td width="20%">
                	<input class="long_txt" type="text" id="DEALER_NAME"  name="DEALER_NAME" value="${map.DEALER_NAME}" readonly="readonly"/>
                </td>
            </tr>
            
            <tr>
                <td width="10%" align="right">额度：</td>
                <td width="20%">
                	<input class="middle_txt" type="text" id="AMOUNT" name="AMOUNT" value="${map.AMOUNT}" maxlength="16"/>
                </td>
                <td width="10%" align="right">&nbsp;</td>
                <td width="20%">
                	&nbsp;
                </td>
            </tr>
            
            <tr>
                <td width="10%" align="right">备注：</td>
                <td colspan="3">
                	<textarea id="REMARK" name="REMARK" rows="3" cols="60">${map.REMARK}</textarea>
                </td>
            </tr>
            
            <tr>
                <td align="center" colspan="6">
                	<input type="button" class="normal_btn" name="BtnQuery" id="queryBtn" onclick="updateReturnPartCfg()" value="保存"/>&nbsp;&nbsp;
                	<input type="button" class="normal_btn" name="BtnQuery" id="queryBtn" onclick="_hide()" value="关闭"/>&nbsp;&nbsp;
                </td>
            </tr>
        </table>
	 
    </form>

    <script type="text/javascript">
        autoAlertException();//输出错误信息
        //保存修改
        function updateReturnPartCfg(){
        	var dealerId = jQuery('#DEALER_ID').val();
        	var parentId = jQuery('#PARENT_ID').val();
        	var amount = jQuery('#AMOUNT').val();
        	if(isNaN(amount)){
        		alert('请输入0或0以上数字');
        		jQuery('#AMOUNT').val(0);
        		return;
        	}else{
        		if(Number(amount)<0){
        			MyAlert('请输入0或0以上数字');
	        		jQuery('#AMOUNT').val(0);
	        		return;
        		}
        	}
        	if(dealerId=='' || parentId==''){
        		MyAlert('配送中心或上级单位未找到，请关闭页面重试！');
        		return;
        	}
        	if(confirm('确认保存？')){
        		btnDisable();
	        	var urlkey = '<%=contextPath%>/parts/storageManager/partReturns/ReturnPartStateChange/updateReturnPartCfg.json';
	        	sendAjax(urlkey, getResult, 'fm');
        	}
        }
        //操作结果
        function getResult(json){
        	var success = json.success;
        	var error = json.error;
        	var ex = json.Exception;
        	if(success!=null && success!='' && success!='null' && success!='undefined'){
        		MyAlert(success);
        		parentContainer.selectQuery();
        		_hide();
        	}else if(error!=null && error!='' && error!='null' && error!='undefined'){
        		MyAlert(error);
        	}else if(ex!=null && ex!='' && ex!='null' && ex!='undefined'){
        		MyAlert(json.Exception.message);
        	}else{
        		MyAlert("保存失败，请联系管理员！");
        	}
        	btnEnable();
        }
    </script>
</div>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="/jstl/cout" %>
<%@page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
    String planId = request.getParameter("planId");
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <!-- <script type="text/javascript" src="<%=request.getContextPath()%>/js/web/$-1.7.2.min.js"></script> -->
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>周度需求计划明细新增</title>
</head>
<body> <!--  onload="__extQuery__(1);" -->
<div class="wbox">
    <div class="navigation">
    	<img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：总部采购管理&gt; 采购计划管理 &gt; 计划编制&gt; 新增
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
    	<input type="hidden" id="planId" name="planId" value="<%=planId%>">
        <table class="table_query">
            <th colspan="6"><img class="panel-icon nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 基础信息</th>
            <tr>
            	<td class="right">配件编码：</td>
                <td >
                	<input type="text" class="middle_txt" id="PART_OLDCODE" name="PART_OLDCODE" value="" onblur="getParts(this)">
                	<input type="hidden" class="middle_txt" id="PART_ID" name="PART_ID" value="">
                	<font color="red">*</font>
                </td>
                <td class="right">配件名称：</td>
                <td >
                	<input type="text" class="middle_txt" disabled="disabled" id="PART_CNAME" name="PART_CNAME" value="">
                </td>
                <td class="right">配件件号：</td>
                <td >
                	<input type="text" class="middle_txt" disabled="disabled" id="PART_CODE" name="PART_CODE" value="">
                </td>
            </tr>
            <tr>
            	<td class="right">计划数量：</td>
                <td >
                	<input type="text" class="middle_txt" id="PLAN_QTY" name="PLAN_QTY" value="" onblur="validatePlanQty(this)">
                	<font color="red">*</font>
                </td>
                <td class="right">最小包装量：</td>
                <td >
                	<input type="text" class="middle_txt" disabled="disabled" id="MIN_PKG" name="MIN_PKG" value="">
                </td>
                <td class="right"></td>
                <td ></td>
            </tr>
            <tr>
            	<td class="right">配件备注：</td>
                <td colspan="5">
                	<textarea id="remark" class="form-control remark align" name="remark" rows="3"></textarea>
                </td>
            </tr>
            <tr>
                <td class="center" colspan="6">
                	<input type="button" class="normal_btn" onclick="addPlan();" value="保存"/>&nbsp;
                	<input type="button" class="normal_btn" onclick="_hide()" value="关闭"/>&nbsp;
                </td>
            </tr>
        </table>
        
    </form>

    <script type="text/javascript">
    	//btnEnable();btnDisable();//全局
    	//获取配件
    	function getParts(self){
    		var value= $(self).val();
    		var urlkey = "<%=contextPath%>/parts/planManager/PartPlanManager/getPartsByPartOldcode.json";
       		makeNomalFormCall(urlkey, getResultPart, "fm");
    	}
    	
    	//审核结果
	    function getResultPart(json){
	    	var success = json.success;
	    	var error = json.error;
	    	var ex = json.Exception;
	    	
	    	$('#PART_ID').val('');
    		$('#PART_CNAME').val('');
    		$('#PART_CODE').val('');
    		$('#MIN_PKG').val('');
    		$('#PLAN_QTY').val('');
	    	
	    	if(success=='1'){
	    		$('#PART_ID').val(json.PART_ID);
	    		$('#PART_OLDCODE').val(json.PART_OLDCODE);
	    		$('#PART_CNAME').val(json.PART_CNAME);
	    		$('#PART_CODE').val(json.PART_CODE);
	    		$('#MIN_PKG').val(json.MIN_PKG);
	    		$('#PLAN_QTY').val(json.PLAN_QTY);
	    	}else if(error!=null && error!='' && error!='null' && error!='undefined'){
	    		MyAlert(error);
	    	}else if(ex!=null && ex!='' && ex!='null' && ex!='undefined'){
	    		MyAlert(json.Exception.message);
	    	}else{
	    		MyAlert("操作异常，请联系管理员！");
	    	}
	    }
	    
	    function validatePlanQty(self){
	    	var value= $(self).val();
	    	if(value==''){
	    		MyAlert('请输入计划数量！');
	    		return;
	    	}
	    	//验证数字
	    	if(isNaN(value)){
	    		MyAlert('请输入数字！');
	    		return;
	    	}
	    	
	    	value = parseInt(value);
	    	$(self).val(value);
	    	
	    	if(Number(value)<=0){
	    		MyAlert('计划数量必须大于0');
	    		return;
	    	}
	    	
	    	//验证最小包装量整数倍
	    	var minPkg = $('#MIN_PKG').val();
	    	if(Number(value)%Number(minPkg)!=0){
	    		MyAlert('计划数量不是最小包装量整数倍！');
	    		return;
	    	}
	    }
	    
	    //保存计划明细
	    function addPlan(){
	    	var PART_ID = $('#PART_ID').val();
    		var MIN_PKG = $('#MIN_PKG').val();
    		var PLAN_QTY = $('#PLAN_QTY').val();
    		
    		if(PART_ID==''){
    			MyAlert('没有选择配件，无法保存！');
    			return;	
    		}
    		
    		if(MIN_PKG==''){
    			MyAlert('最小包装量为空，无法保存！');
    			return;	
    		}else{
    			if(isNaN(MIN_PKG)){
		    		MyAlert('最小包装量不是数字！');
		    		return;
		    	}
    		}
    		if(PLAN_QTY==''){
    			MyAlert('计划数量为空，无法保存！');
    			return;	
    		}else{
    			if(isNaN(PLAN_QTY)){
		    		MyAlert('计划数量不是数字！');
		    		return;
		    	}else{
		    		if(Number(PLAN_QTY)%Number(MIN_PKG)!=0){
			    		MyAlert('计划数量不是最小包装量整数倍！');
			    		return;
			    	}
		    	}
    		}
    		MyConfirm('确认保存？',function(){
    			var urlkey = "<%=contextPath%>/parts/planManager/PartPlanManager/addPlanByPlanId.json";
       			makeNomalFormCall(urlkey, getResult, "fm");
    		});
	    }
	    
	    //审核结果
	    function getResult(json){
	    	var success = json.success;
	    	var error = json.error;
	    	var ex = json.Exception;
	    	if(success!=null && success!='' && success!='null' && success!='undefined'){
	    		MyAlert(success);
	    		__parent().query1();
	    		_hide();
	    	}else if(error!=null && error!='' && error!='null' && error!='undefined'){
	    		MyAlert(error);
	    	}else if(ex!=null && ex!='' && ex!='null' && ex!='undefined'){
	    		MyAlert(json.Exception.message);
	    	}else{
	    		MyAlert("操作异常，请联系管理员！");
	    	}
	    }
    </script>
</div>
</body>
</html>
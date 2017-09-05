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
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>计划明细修改</title>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
    <div class="navigation">
    	<img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：总部采购管理&gt; 采购计划管理 &gt; 周度需求计划编制&gt; 修改
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
    	<input type="hidden" id="planId" name="planId" value="${planId}">
    	<input type="hidden" id="planTypes" name="planTypes" value="${planTypes}">
    	<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
        <table class="table_query" >
            <tr>
            	<td class="right">配件编码：</td>
                <td width="23%">
                	<input type="text" class="middle_txt" id="PART_OLDCODE" name="PART_OLDCODE" value="">
                </td>
            	<td class="right">配件名称：</td>
                <td width="23%">
                	<input type="text" class="middle_txt" id="PART_CNAME" name="PART_CNAME" value="">
                </td>
                <td class="right">配件件号：</td>
                <td >
                	<input type="text" class="middle_txt" id="PART_CODE" name="PART_CODE" value="">
                </td>
            </tr>
            <tr>
                <td class="center" colspan="6">
                	<input type="button" class="normal_btn" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1);" value="查询"/>&nbsp;
                	<input type="button" class="normal_btn" onclick="addPlan();" value="新增"/>&nbsp;
                	<input type="button" class="normal_btn" onclick="savePlan()" value="保存"/>&nbsp;
                	<input type="button" class="normal_btn" onclick="deletePlan()" value="删除"/>&nbsp;
                	<input type="button" class="normal_btn" onclick="goback();" value="返回"/>&nbsp;
                </td>
            </tr>
        </table>
        </div>
        </div>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>
    <script type="text/javascript">
        var myPage;
        var url = "<%=contextPath%>/parts/planManager/PartPlanManager/getRollingPlanInfoById.json";
        var title = null;
        var columns = [
				{header: "序号", renderer: getIndex},
				{header: '<input type="checkbox" onclick="cksall(this)">',dataIndex:'PLINE_ID', style:"text-align: center",renderer:myLink},
				{header: "计划单号",dataIndex:'PLAN_NO', style:"text-align: center"},
				{header: "配件编码",dataIndex:'PART_OLDCODE', style:"text-align: center"},
				{header: "配件名称",dataIndex:'PART_CNAME', style:"text-align: center"},
				{header: "配件件号",dataIndex:'PART_CODE', style:"text-align: center"},
				{header: "计划数量",dataIndex:'PLAN_NUM', style:"text-align: center",renderer:setPlanQty},
				{header: "最小包装量",dataIndex:'BUY_MIN_PKG', style:"text-align: center"},
				{header: "配件备注",dataIndex:'SALES_REMARK', style:"text-align: center",renderer:setRemark},
// 				{header: "配件类型",dataIndex:'PART_TYPE', style:"text-align: center",renderer:getItemValue},
				{header: "自制配套",dataIndex:'PRODUCE_STATE', style:"text-align: center",renderer:getItemValue},
				{header: "采购方式",dataIndex:'PRODUCE_FAC', style:"text-align: center",renderer:getItemValue},
				{header: "上级单位",dataIndex:'SUPERIOR_PURCHASING', style:"text-align: center",renderer:getItemValue},
				{header: "计划年月",dataIndex:'MONTH_DATE', style:"text-align: center"},
				{header: "转单周期",dataIndex:'ORDER_PERIOD', style:"text-align: center",renderer:getItemValue},
				{header: "供应商",dataIndex:'VENDER_NAME', style:"text-align: center"},
				{header: "收货库房",dataIndex:'WH_NAME', style:"text-align: center"}
            ];
        
        //全选
        function cksall(self){
        	var bl = jQuery(self).prop('checked');
        	jQuery('input[name="ck"]').prop('checked',bl);
        }
        //操作链接生成
        function myLink(value,meta,record){
        	var str = '<input type="checkbox" name="ck" value="'+value+'">';
        	return str;
        }
        //计划数量
        function setPlanQty(value,meta,record){
        	var minpkg = record.data.BUY_MIN_PKG;
        	var PLINE_ID = record.data.PLINE_ID;
        	var str = '<input type="text" style="width:66px;" id="PLAN_QTY_'+PLINE_ID+'" name="PLAN_QTY_'+PLINE_ID+'" value="'+value+'" class="middle_txt">';
        		str+= '<input type="hidden" id="MIN_PKG_'+PLINE_ID+'" name="MIN_PKG_'+PLINE_ID+'" value="'+minpkg+'">';
        	return str;
        }
        //备注
        function setRemark(value,meta,record){
        	var PLINE_ID = record.data.PLINE_ID;
        	var str = '<input type="text" id="SALES_REMARK_'+PLINE_ID+'" name="SALES_REMARK_'+PLINE_ID+'" value="'+value+'" class="middle_txt">';
        	return str;
        }
        //返回
        function goback(){
        	if('${planTypes}'=='<%=Constant.PART_PURCHASE_PLAN_TYPE_01%>'){
        		location = '<%=contextPath%>/parts/planManager/PartPlanManager/toRollingPlanInit.do';
        	}else{
        		location = '<%=contextPath%>/parts/planManager/PartPlanManager/toRollingPlanInitZ.do';
        	}
        }
        
        //保存计划明细
        function savePlan(){
        	var len = jQuery('input[name="ck"]:checked').length;
        	if(len>0){
        		//验证数量
        		var lens = jQuery('input[name="ck"]').length;
        		for(var i=0;i<lens;i++){
        			var bl = jQuery('input[name="ck"]').eq(i).prop('checked');
        			if(bl==true){
        				//判断数字，最小包装量，大于0
        				var lineId = jQuery('input[name="ck"]').eq(i).val();
        				var PLAN_QTY = $('#PLAN_QTY_'+lineId)[0].value;
        				var MIN_PKG = $('#MIN_PKG_'+lineId)[0].value;
        				if(PLAN_QTY==""){
        					MyAlert('第'+(i+1)+'行，计划数量不能为空！');
        					return;
        				}
        				if(isNaN(PLAN_QTY)){
        					MyAlert('第'+(i+1)+'行，请输入数字！');
        					return;
        				}
        				PLAN_QTY = parseInt(PLAN_QTY);
        				jQuery('#PLAN_QTY_'+lineId).val(PLAN_QTY);
        				if(PLAN_QTY<=0){
        					MyAlert('第'+(i+1)+'行，请输入大于0的数字！');
        					return;
        				}
        				if(Number(PLAN_QTY)%Number(MIN_PKG)!=0){
        					MyAlert('第'+(i+1)+'行，计划数量不是最小包装量的整数倍！');
        					return;
        				}
        			}
        		}
        	}else{
        		MyAlert('请选择要保存的数据!');
        		return;
        	}
        	MyConfirm('确认保存?',function(){
        		var urlkey = "<%=contextPath%>/parts/planManager/PartPlanManager/savePlanByPlineId.json";
       			makeNomalFormCall(urlkey, getResultSave, "fm");
        	});
        }
        
        //删除计划明细
        function deletePlan(){
        	var len = jQuery('input[name="ck"]:checked').length;
        	if(len>0){
        		MyConfirm('确认删除?',function(){
	        		var urlkey = "<%=contextPath%>/parts/planManager/PartPlanManager/deletePlanByPlineId.json";
	       			makeNomalFormCall(urlkey, getResultDelete, "fm");
        		});
        	}else{
        		MyAlert('请选择要删除的数据!');
        		return;
        	}
        }
        
        //审核结果
	    function getResultDelete(json){
	    	var success = json.success;
	    	var error = json.error;
	    	var ex = json.Exception;
	    	if(success!=null && success!='' && success!='null' && success!='undefined'){
	    		if(success=='1'){
	    			MyAlert('删除成功！');
	    		}else{
	    			MyAlert('删除成功,已无数据！');
	    			goback();
	    		}
	    	}else if(error!=null && error!='' && error!='null' && error!='undefined'){
	    		MyAlert(error);
	    	}else if(ex!=null && ex!='' && ex!='null' && ex!='undefined'){
	    		MyAlert(json.Exception.message);
	    	}else{
	    		MyAlert("操作异常，请联系管理员！");
	    	}
	    	query1();
	    }
	    
	    //审核结果
	    function getResultSave(json){
	    	var success = json.success;
	    	var error = json.error;
	    	var ex = json.Exception;
	    	if(success!=null && success!='' && success!='null' && success!='undefined'){
    			MyAlert(success);
	    	}else if(error!=null && error!='' && error!='null' && error!='undefined'){
	    		MyAlert(error);
	    	}else if(ex!=null && ex!='' && ex!='null' && ex!='undefined'){
	    		MyAlert(json.Exception.message);
	    	}else{
	    		MyAlert("操作异常，请联系管理员！");
	    	}
	    	query1();
	    }
        
        //新增计划明细
        function addPlan(){
        	OpenHtmlWindow('<%=contextPath%>/jsp/parts/planManager/rollingPlanManager/toRollingPlanAdd.jsp?planId=${planId}',950,450);
        }
        function query1(){
        	__extQuery__(1);
        }
    </script>
</div>
</body>
</html>
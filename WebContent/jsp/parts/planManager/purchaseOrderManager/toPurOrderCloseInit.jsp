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
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>采购订单关闭</title>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
    <div class="navigation">
    	<img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置： 配件管理&gt; 采购订单管理&gt; 采购订单关闭
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table class="table_query">
	            <tr>
	                <td width="10%" class="right">订单单号：</td>
	                <td width="20%">
	                    <input class="middle_txt" type="text" id="ORDER_CODE" name="ORDER_CODE" value=""/>
	                </td>
	                <td width="10%" class="right">供应商：</td>
	                <td width="20%">
	                    <input class="middle_txt" type="text" id="VENDER_NAME" name="VENDER_NAME" value=""/>
	                </td>
	                <td width="10%" class="right">制单日期：</td>
	                <td width="23%">
	                	<input name="sCreateDate" readonly="readonly" id="sCreateDate" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="sCreateDate,eCreateDate" style="width: 80px;">
	                   <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" onclick="showcalendar(event, 'sCreateDate', false);"/>
					   <label>至</label>
	                   <input name="eCreateDate" readonly="readonly" id="eCreateDate" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="sCreateDate,eCreateDate" style="width: 80px;">
	                   <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" onclick="showcalendar(event, 'eCreateDate', false);"/>
	                </td>
	            </tr>
	             <tr>
	                <td width="10%" class="right">订单状态：</td>
	                <td width="20%">
	                	<script type="text/javascript">
	                        genSelBoxExp("STATE", <%=Constant.PARTS_ORDER_OEM_STATUS%>, "<%=Constant.PARTS_ORDER_OEM_STATUS_WEI%>", true, "u-select", "", "false", '<%=Constant.PARTS_ORDER_OEM_STATUS_YES%>');
	                    </script>
	                </td>
	                <td width="10%" class="right"></td>
	                <td width="20%">
	                </td>
	                <td width="10%" class="right"></td>
	                <td width="20%">
	                </td>
	            </tr>
	            <tr>
	                <td width="10%" class="right">配件编码：</td>
	                <td width="20%">
	                	<input class="middle_txt" type="text" id="PART_OLDCODE"  name="PART_OLDCODE" value=""/>
	                </td>
	                
	                <td width="10%" class="right">配件名称：</td>
	                <td width="20%">
	                    <input class="middle_txt" type="text" id="PART_CNAME" name="PART_CNAME" value=""/>
	                </td>
	                
	                <td width="10%" class="right">配件件号：</td>
	                <td width="20%">
	                    <input class="middle_txt" type="text" id="PART_CODE" name="PART_CODE" value=""/>
	                </td>
	            </tr>
	           
	          
	            <tr>
	            	<td width="10%" class="right">原因：</td>
	                <td align="left" colspan="5">
	                	<textarea id="CLOSE_REMARK" class="form-control remark align" name="CLOSE_REMARK" rows="3" style="width: 87%;"></textarea>
	                </td>
	            </tr>
	              <tr>
	                <td class="center" colspan="6">
	                	<input type="button" class="normal_btn" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1);" value="查询"/>&nbsp;
	                	<input type="button" class="normal_btn" onclick="optClosePurOrder()" value="关闭"/>&nbsp;
	                	<input type="button" class="normal_btn" onclick="optOpenPurOrder()" value="开启"/>&nbsp;
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
        var url = '<%=contextPath%>/parts/planManager/PartPlanManager/getPurOrderCloseInfo.json';
        var title = null;
        var columns = [
				{header: "序号", renderer: getIndex},
				{header: '<input type="checkbox" onclick="ckAll(this)">',dataIndex:'POLINE_ID',renderer: myLink, style:"text-align: center"},
				{header: "订单单号",dataIndex:'ORDER_CODE', style:"text-align: center"},
				{header: "计划单号",dataIndex:'PLAN_CODE', style:"text-align: center"},
				{header: "配件编码",dataIndex:'PART_OLDCODE', style:"text-align: center"},
				{header: "配件名称",dataIndex:'PART_CNAME', style:"text-align:center"},
				{header: "配件件号",dataIndex:'PART_CODE', style:"text-align: center"},
				{header: "采购数量",dataIndex:'BUY_QTY', style:"text-align: center"},
				//{header: "未入库数",dataIndex:'DIN_QTY', style:"text-align: center;width:65px;"},
				//{header: "已入库数",dataIndex:'IN_QTY', style:"text-align: center"},
				{header: "供应商代码",dataIndex:'VENDER_CODE', style:"text-align: center"},
				{header: "供应商名称",dataIndex:'VENDER_NAME', style:"text-align: center"},
				//{header: "上级单位",dataIndex:'SUPERIOR_PURCHASING', style:"text-align: center",renderer:getItemValue},
				//{header: "计划备注",dataIndex:'SALES_REMARK', style:"text-align: center"},
				{header: "单位",dataIndex:'UNIT', style:"text-align: center"},
				{header: "配件类型",dataIndex:'PART_TYPE', style:"text-align: center",renderer:getItemValue},
				{header: "制单日期",dataIndex:'CREATE_DATE', style:"text-align: center"},
				{header: "状态",dataIndex:'STATE', style:"text-align: center",renderer:getItemValue},
				{header: "关闭原因",dataIndex:'CLOSE_REMARK', style:"text-align: center"},
				{header: "开启原因",dataIndex:'OPEN_REMARK', style:"text-align: center"}
            ];
        //操作链接生成
        function myLink(value,meta,record){
        	var str = '<input type="checkbox" name="ck" value="'+value+'">';
        	return str;
        }
        //全选、全不选
        function ckAll(self){
        	var isck = jQuery(self).prop('checked');
        	jQuery('input[name="ck"]').prop('checked',isck);
        }
        //截取配件名称
        function subPartCnameText(value,meta,record){
        	var rs = value;
        	if(rs.length>10){
        		rs = value.substring(0,10)+"···";
        	}
        	return '<label title="'+value+'">'+rs+'</label>';
        }
        //设置待入库
        function setDInQtyText(value,meta,record){
        	var poId = record.data.PO_ID;
        	var BUY_QTY = record.data.BUY_QTY;
        	var IN_QTY = record.data.IN_QTY;
        	var WH_ID = record.data.WH_ID;
        	var str = '<input type="text" id="dInQty_'+poId+'" name="dInQty_'+poId+'" style="width:60px;" value="'+value+'">';
        		str+= '<input type="hidden" id="buyQty_'+poId+'" name="buyQty_'+poId+'" value="'+BUY_QTY+'">';
        		str+= '<input type="hidden" id="inQty_'+poId+'" name="inQty_'+poId+'" value="'+IN_QTY+'">';
        		str+= '<input type="hidden" id="whId_'+poId+'" name="whId_'+poId+'" value="'+WH_ID+'">';
        	return str;
        }
        //设置入库备注
        function setInRemarkText(value,meta,record){
        	var poId = record.data.PO_ID;
        	var str = '<input type="text" id="inRemark_'+poId+'" name="inRemark_'+poId+'" style="width:80px;" value="'+value+'">';
        	return str;
        }
        //关闭
        function optClosePurOrder(){
        	var len = jQuery('input[name="ck"]:checked').length;
        	if(len==0){
        		MyAlert('请选择待关闭的配件！');
        		return;
        	}else{
        		var CLOSE_REMARK = jQuery('#CLOSE_REMARK').val();
        		if(CLOSE_REMARK==''){
        			MyAlert('请填写关闭原因！');
        			return;
        		}
        		MyConfirm("确认关闭？",confirmResult);
        	}
        }
        function confirmResult(){
    		btnDisable();
    		var urlkey = "<%=contextPath%>/parts/planManager/PartPlanManager/optClosePurOrder.json";
        	makeNomalFormCall(urlkey, getResult, 'fm');
        }
        
        //开启
        function optOpenPurOrder(){
        	var len = jQuery('input[name="ck"]:checked').length;
        	if(len==0){
        		MyAlert('请选择开启配件！');
        		return;
        	}else{
        		var CLOSE_REMARK = jQuery('#CLOSE_REMARK').val();
        		if(CLOSE_REMARK==''){
        			MyAlert('请填写开启原因！');
        			return;
        		}
        		MyConfirm("确认开启？",confirmResult1);
        	}
        }
        function confirmResult1(){
    		btnDisable();
    		var urlkey = "<%=contextPath%>/parts/planManager/PartPlanManager/optOpenPurOrder.json";
        	makeNomalFormCall(urlkey, getResult, 'fm');
        }
        //审核结果
        function getResult(json){
        	var success = json.success;
        	var error = json.error;
        	var ex = json.Exception;
        	if(success!=null && success!='' && success!='null' && success!='undefined'){
       			MyAlert(success);
       			jQuery('#CLOSE_REMARK').val('');
        	}else if(error!=null && error!='' && error!='null' && error!='undefined'){
        		MyAlert(error);
        	}else if(ex!=null && ex!='' && ex!='null' && ex!='undefined'){
        		MyAlert(json.Exception.message);
        	}else{
        		MyAlert("操作异常，请联系管理员！");
        	}
        	btnEnable();
        	__extQuery__(1);
        }
    </script>
</div>
</body>
</html>
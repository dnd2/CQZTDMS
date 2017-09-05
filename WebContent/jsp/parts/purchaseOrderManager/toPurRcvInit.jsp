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
<title>采购订单验收</title>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation">
	<img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：配件管理&gt; 采购计划管理&gt; 采购订单验收
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage"/>
    <div class="form-panel">
	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
	<div class="form-body">
    <table class="table_query" >
       <tr>
           <td class="right">订单单号：</td>
           <td >
               <input class="middle_txt" type="text" id="ORDER_CODE" name="ORDER_CODE" value=""/>
           </td>
           
<!--            <td class="right">供应商：</td> -->
<!--            <td > -->
<!--                <input class="middle_txt" type="text" id="VENDER_NAME" name="VENDER_NAME" value=""/> -->
<!--            </td> -->
           
           <td class="right">制单日期：</td>
           <td >
           	<input name="sCreateDate" readonly="readonly" id="sCreateDate" value="" type="text"  class="middle_txt" datatype="1,is_date,10" group="sCreateDate,eCreateDate" style="width: 80px;">
              <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"/>
  				<label>至</label>
              <input name="eCreateDate" readonly="readonly" id="eCreateDate" value="" type="text"  class="middle_txt" datatype="1,is_date,10" group="sCreateDate,eCreateDate" style="width:80px;">
              <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"/>
           </td>
           <td class="right">状态：</td>
           <td >
           	<%-- <select id="IS_DQR" name="IS_DQR" class="u-select">
           		<option value="">-请选择-</option>
           		<option selected="selected" value="<%=Constant.PARTS_ORDER_STATUS_NO%>">未确认</option>  
           		<option value="<%=Constant.PARTS_ORDER_STATUS_YES%>">已确认</option>
           	</select> --%>
           	<script type="text/javascript">
              		genSelBoxExp("IS_DQR","<%=Constant.PARTS_ORDER_OEM_STATUS%>", "<%=Constant.PARTS_ORDER_OEM_STATUS_WEI%>", true, "u-select", "", "false", "");
            </script> 
           </td>
       </tr>
       <tr>
           <td class="right">配件编码：</td>
           <td >
           	<input class="middle_txt" type="text" id="PART_OLDCODE"  name="PART_OLDCODE" value=""/>
           </td>
           <td class="right">配件名称：</td>
           <td >
               <input class="middle_txt" type="text" id="PART_CNAME" name="PART_CNAME" value=""/>
           </td>
           <td class="right">配件件号：</td>
           <td >
               <input class="middle_txt" type="text" id="PART_CODE" name="PART_CODE" value=""/>
           </td>
       </tr>
       
<!--        <tr> -->
<!--            <td class="right">是否待确认：</td> -->
<!--            <td > -->
<!--            	<select id="IS_DQR" name="IS_DQR" class="short_sel"> -->
<!--            		<option value="">-请选择-</option> -->
<%--            		<option selected="selected" value="<%=Constant.IF_TYPE_YES%>">待确认信息</option> --%>
<%--            		<option value="<%=Constant.IF_TYPE_NO%>">已确认信息</option> --%>
<!--            	</select> -->
<!--            </td> -->
           
<!--            <td class="right">中储名称：</td> -->
<!--            <td > -->
<!--            	<input class="middle_txt" type="text" id="CMST_NAME" name="CMST_NAME" value=""/> -->
<!--            </td> -->
           
<!--            <td class="right"></td> -->
<!--            <td > -->
<!--            </td> -->
<!--        </tr> -->
       <tr>
           <td class="center" colspan="6">
           	<input type="button" class="u-button" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1);" value="查询"/>&nbsp;
           	<input type="button" class="u-button" onclick="saveConfirmPurRcvQty()" value="验收" id="ys_button"/>&nbsp;
           	<input type="button" class="normal_btn" onclick="exportPurRcv()" value="导出"/>&nbsp;
           	<input type="reset" class="u-button" onclick="btnEable();" value="重置"/>&nbsp;
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
   var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseArrConfir/getPurRcvInfo.json';
    var title = null;
    var columns = [
{header: "序号", renderer: getIndex},
{header: '<input type="checkbox" onclick="ckAll(this)">',dataIndex:'PO_ID',renderer: myLink, style:"text-align: center"},
{header: "订单单号",dataIndex:'ORDER_CODE', style:"text-align: center"},
{header: "配件编码",dataIndex:'PART_OLDCODE', style:"text-align: center"},
{header: "配件名称",dataIndex:'PART_CNAME', style:"text-align:center",renderer:subPartCnameText},
{header: "配件件号",dataIndex:'PART_CODE', style:"text-align: center"},
{header: "单位",dataIndex:'UNIT', style:"text-align: center"},
{header: "批次号",dataIndex:'BATCH_NO', style:"text-align: center"},
{header: "供应商",dataIndex:'VENDER_NAME', style:"text-align: center"},
{header: "计划数量",dataIndex:'PLAN_QTY', style:"text-align: center"},
{header: "采购数量",dataIndex:'BUY_QTY', style:"text-align: center"},
{header: "已验收数量",dataIndex:'YCON_QTY', style:"text-align: center"},
{header: "待验收数量",dataIndex:'DCON_QTY', style:"text-align: center;width:65px;",renderer:setDInQtyText},
// {header: "已入库数量",dataIndex:'IN_QTY', style:"text-align: center"},
// {header: "入库备注",dataIndex:'IN_REMARK', style:"text-align: center",renderer:setInRemarkText},
// {header: "计划名称",dataIndex:'PLAN_NAME', style:"text-align: center"},
// {header: "计划备注",dataIndex:'PLAN_REMARK', style:"text-align: center"},62827345
// {header: "库房",dataIndex:'WH_NAME', style:"text-align: center"},
// {header: "货位",dataIndex:'LOC_CODE', style:"text-align: center"},
// {header: "中储名称",dataIndex:'CMST_NAME', style:"text-align: center"},
// {header: "配件类型",dataIndex:'PART_TYPE', style:"text-align: center",renderer:getItemValue},
{header: "制单日期",dataIndex:'CREATE_DATE', style:"text-align: center"},
{header: "状态",dataIndex:'STATE', style:"text-align: center",renderer:getItemValue}
        ];
    
	//操作链接生成
	function myLink(value,meta,record){
    	var str = '<input type="checkbox" name="ck" value="'+value+'">';
    	var STATE = record.data.STATE;
    	if(STATE==15041002||STATE==15041004){
    		str = '<img src="<%=contextPath%>/img/close.gif" >';
   		}else{
   		}
   		return str;
   }
   //全选、全不选
   function ckAll(self){
   	var isck = $(self).prop('checked');
   	$('input[name="ck"]').prop('checked',isck);
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
//    	var WH_ID = record.data.WH_ID;
   	var POLINE_ID = record.data.POLINE_ID;
   	var str = '<input type="text" id="dInQty_'+poId+'" name="dInQty_'+poId+'" style="width:60px;" value="'+value+'">';
   		str+= '<input type="hidden" id="buyQty_'+poId+'" name="buyQty_'+poId+'" value="'+BUY_QTY+'">';
   		str+= '<input type="hidden" id="polineId_'+poId+'" name="polineId_'+poId+'" value="'+POLINE_ID+'">';
   		str+= '<input type="hidden" id="dInQtyInit_'+poId+'" name="dInQtyInit_'+poId+'" value="'+value+'">';
//    		str+= '<input type="hidden" id="inQty_'+poId+'" name="inQty_'+poId+'" value="'+IN_QTY+'">';
//    		str+= '<input type="hidden" id="whId_'+poId+'" name="whId_'+poId+'" value="'+WH_ID+'">';
   	return str;
   }
//    //设置入库备注
//    function setInRemarkText(value,meta,record){
//    	var poId = record.data.PO_ID;
//    	var str = '<input type="text" id="inRemark_'+poId+'" name="inRemark_'+poId+'" style="width:80px;" value="'+value+'">';
//    	return str;
//    }
   
   //确认到货数
   function saveConfirmPurRcvQty(){
	   	var len = $('input[name="ck"]:checked').length;
	   	if(len==0){
	   		MyAlert('请选择要确认的配件！');
	   		return;
	   	}
	   	var lens = $('input[name="ck"]').length;
	   	for(var i=0;i<lens;i++){
	   		var isck = $('input[name="ck"]').eq(i).prop('checked');
	   		if(isck==true){
	   			var poId = $('input[name="ck"]').eq(i).val();
	    		var dInQty = $('#dInQty_'+poId).val();//本次验收数量
	    		var buyQty = $('#buyQty_'+poId).val();//采购数量
	    		var dInQtyInit = $('#dInQtyInit_'+poId).val();//页面初始化验收数量
	    		if(dInQty==''){
	    			MyAlert('第'+(i+1)+'行,验收数不能为空！');
	    			return;
	    		}
	    		if(isNaN(dInQty)){
	    			MyAlert('第'+(i+1)+'行,请输入数字！');
	    			return;
	    		}else{
	    			/* if(parseInt(dInQty)>parseInt(buyQty)){
	    				MyAlert('第'+(i+1)+'行,待验收数量不能大于采购数量！');
	        			return;
	    			} */
	    			if(parseInt(dInQty)>parseInt(dInQtyInit)){
	    				MyAlert('第'+(i+1)+'行,待验收数量'+dInQty+'不能大于可验收数量'+dInQtyInit);
	        			return;
	    			}else{
	        			var dqty = parseInt(dInQty);
	        			$('#dInQty_'+poId).val(dqty);
	    			}
	    		}
	   		}
	   	}
	    MyConfirm('确认提交？',function() {
	   		var urlkey = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseArrConfir/saveConfirmPurRcvQty.json";
	   		makeNomalFormCall(urlkey, getResult, 'fm');
	   	}); 
   }
   
   //审核结果
   function getResult(json){
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
	   	//btnEable();
	   	__extQuery__(1);
   }
   
   //导出到货确认
   function exportPurRcv(){
	   
   		//btnDisable();
   		fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseArrConfir/exportPurRcv.do";
		fm.submit();
  
   }
</script>
</div>
</body>
</html>
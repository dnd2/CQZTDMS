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
<title>采购到货入库</title>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation">
	<img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：配件管理&gt; 采购计划管理&gt;采购到货入库
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage"/>
    <div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
    <table class="table_query">
       <tr>
           <td class="right">订单单号：</td>
           <td >
               <input class="middle_txt" type="text" id="ORDER_CODE" name="ORDER_CODE" value=""/>
           </td>
           <td class="right">供应商：</td>
           <td >
               <input class="middle_txt" type="text" id="VENDER_NAME" name="VENDER_NAME" value=""/>
           </td>
           <td class="right">制单日期：</td>
           <td >
           	<input name="sCreateDate" readonly="readonly" id="sCreateDate" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="sCreateDate,eCreateDate" style="width:80px">
              <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"/>
  				<label>至</label>
              <input name="eCreateDate" readonly="readonly" id="eCreateDate" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="sCreateDate,eCreateDate" style="width:80px">
              <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"/>
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
<!--            <td class="right">有采购价：</td> -->
<!--            <td > -->
<!--            	<script type="text/javascript"> -->
<%--               		genSelBoxExp("CG_PRICE_FLAG", '<%=Constant.IF_TYPE%>', "", true, "short_sel", "", "false", ''); --%>
<!--           		</script> -->
<!--            </td> -->
<!--            <td class="right"></td> -->
<!--            <td > -->
<!--            </td> -->
<!--            <td class="right"></td> -->
<!--            <td > -->
<!--            </td> -->
<!--        </tr> -->
       
       <tr>
           <td class="center" colspan="6">
           	<input type="button" class="u-button" name="queryBtn" id="queryBtn" onclick="__extQuery__(1);" value="查询"/>&nbsp;
           	<input type="button" class="u-button" onclick="instockPurRcvParts();" value="入库"/>&nbsp;
           	<input type="button" class="u-button" onclick="exportPurRcvParts();" value="导出"/>&nbsp;
<%--            	<a id="toRFPurchaseScanInStock" href="<%=contextPath%>/parts/planManager/PartPlanManager/toRFPurchaseScanInStock.do" target="_bank">扫描入库</a> --%>
           </td>
       </tr>
   </table>

   <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
   <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>

<script type="text/javascript">
   var myPage;
   var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseArrConfir/getPurRcvInwhInfo.json';
    var title = null;
    var columns = [
			{header: "序号", renderer: getIndex},
			{header: '<input type="checkbox" onclick="ckAll(this)">',dataIndex:'PO_ID',renderer: myLink, style:"text-align: center"},
			{header: "订单单号",dataIndex:'ORDER_CODE', style:"text-align: center"},
			{header: "配件编码",dataIndex:'PART_OLDCODE', style:"text-align: center"},
			{header: "配件名称",dataIndex:'PART_CNAME', style:"text-align:center",renderer:subPartCnameText},
			{header: "配件件号",dataIndex:'PART_CODE', style:"text-align: center"},
			{header: "批次号",dataIndex:'BATCH_NO', style:"text-align: center",renderer:setBatchNoText},
			{header: "采购数量",dataIndex:'BUY_QTY', style:"text-align: center"},
			{header: "到货验收数",dataIndex:'CON_QTY', style:"text-align: center"},
			{header: "到货入库数",dataIndex:'CON_IN_QTY', style:"text-align: center"},
			{header: "待入库数量",dataIndex:'CDIN_QTY', style:"text-align: center;width:65px;",renderer:setDInQtyText},
			{header: "总入库数量",dataIndex:'IN_QTY', style:"text-align: center"},
			{header: "库房",dataIndex:'WH_NAME', style:"text-align: center"},
			{header: "货位",dataIndex:'LOC_CODE', style:"text-align: center",renderer:setLocCodeText},
			{header: "入库备注",dataIndex:'', style:"text-align: center",renderer:setInRemarkText},
// 			{header: "确认备注",dataIndex:'REMARK', style:"text-align: center"},//验收时填写的备注信息
			{header: "单位",dataIndex:'UNIT', style:"text-align: center"},
			{header: "供应商",dataIndex:'VENDER_NAME', style:"text-align: center"},
// 			{header: "上级单位",dataIndex:'SUPERIOR_PURCHASING', style:"text-align: center"},
			{header: "配件类型",dataIndex:'PART_TYPE', style:"text-align: center",renderer:getItemValue},
			{header: "制单日期",dataIndex:'CREATE_DATE', style:"text-align: center"},
// 			{header: "状态",dataIndex:'STATE', style:"text-align: center",renderer:getItemValue},
// 			{header: "是否打印",dataIndex:'IS_PRINT', style:"text-align: center",renderer:getItemValue},
// 			{header: "打印日期",dataIndex:'PRINT_DATE', style:"text-align: center"},
			{header: "是否暂估价",dataIndex:'IS_GUARD', style:"text-align: center",renderer:getItemValue},
			{header: "采购价",dataIndex:'BUY_PRICE', style:"text-align: center"}
// 			,
// 			{header: "有采购价",dataIndex:'CG_PRICE_FLAG', style:"text-align: center"}
        ];
    
    //操作链接生成
    function myLink(value,meta,record){
    	var str = '<input type="checkbox" name="ck" value="'+value+'">';
    	return str;
    }
    //全选、全不选
    function ckAll(self){
    	var isck = $(self).prop('checked');
    	$('input[name="ck"]').prop('checked',isck);
    }
    //截取备件名称
    function subPartCnameText(value,meta,record){
    	var rs = value;
    	if(rs.length>10){
    		rs = value.substring(0,10)+"···";
    	}
    	return '<label title="'+value+'">'+rs+'</label>';
    }
    //设置待入库
    function setDInQtyText(value,meta,record){
    	var conId = record.data.PO_ID;
    	var str = '<input type="text" id="dInQty_'+conId+'" name="dInQty_'+conId+'" style="width:60px;" value="'+value+'">';//待入库数量
    		str+= '<input type="hidden" id="CDIN_QTY_'+conId+'" name="CDIN_QTY_'+conId+'" value="'+value+'">';//默认待入库数量
    	return str;
    }
   	//设置批次号
    function setBatchNoText(value,meta,record){
    	var conId = record.data.PO_ID;
    	var str = value+'<input type="hidden" id="batchNo_'+conId+'" name="batchNo_'+conId+'" value="'+value+'">';
    	return str;
    }
    //设置货位
    function setLocCodeText(value,meta,record){
    	var conId = record.data.PO_ID;
		var str ='<select id="LOC_CODE_'+conId +'" name="LOC_CODE_'+conId +'" >';
     	var conArr= value.substr(0,value.length-1).split(",");
     	for(var i=0;i<conArr.length;i++){
     	  	str += '<option value="'+conArr[i]+'">'+conArr[i]+'</option>';
     	}
    	str += '</select>';
    	return str;
    }
    //设置入库备注
    function setInRemarkText(value,meta,record){
    	var conId = record.data.PO_ID;
    	var str = '<input type="text" id="inRemark_'+conId+'" name="inRemark_'+conId+'" style="width:80px;" value="'+value+'">';
    	return str;
    }
    
    //确认到货数
    function instockPurRcvParts(){
    	var len = $('input[name="ck"]:checked').length;
    	if(len==0){
    		MyAlert('请选择要入库的备件！');
    		return;
    	}
    	var lens = $('input[name="ck"]').length;
    	for(var i=0;i<lens;i++){
    		var isck = $('input[name="ck"]').eq(i).prop('checked');
    		if(isck==true){
    			var conId = $('input[name="ck"]').eq(i).val();
    			var CDIN_QTY = $('#CDIN_QTY_'+conId).val();
	     		var dInQty = $('#dInQty_'+conId).val();
	     		//var locCode = $('#LOC_CODE_'+conId).val();
	     		if(dInQty==''){
	     			MyAlert('第'+(i+1)+'行,确认数不能为空！');
	     			return;
	     		}
	     		if(isNaN(dInQty)){
	     			MyAlert('第'+(i+1)+'行,请输入数字！');
	     			return;
	     		}else{
	     			var dqty = parseInt(dInQty);
	     			$('#dInQty_'+conId).val(dqty);
	     			if(dqty<=0){
	     				MyAlert('第'+(i+1)+'行,入库数量必须大于0！');
	     				return;
	     			}
	     			if(Number(dqty)>Number(CDIN_QTY)){
	     				MyAlert('第'+(i+1)+'行,入库数量'+dqty+'大于可入库数量'+CDIN_QTY+'！');
	     				return;
	     			}
	     		}
    		}
    	}
    	MyConfirm('确认入库？',function(){
    		var urlkey = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseArrConfir/instockPurRcvParts.json";
       		makeNomalFormCall(urlkey, getResult, 'fm');
    	});
   }
   
   //入库结果
   function getResult(json){
   	var success = json.success;
   	var error = json.error;
   	var ex = json.Exception;
   	if(success!=null && success!='' && success!='null' && success!='undefined'){
  		MyAlert(success);
   	}else if(error!=null && error!='' && error!='null' && error!='undefined'){
   		MyAlert(error);
   	}/* else if(ex!=null && ex!='' && ex!='null' && ex!='undefined'){
   		MyAlert(json.Exception.message);
   	} else{
   		MyAlert("操作异常，请联系管理员！");
   	}*/
   	//btnEnable();
   	__extQuery__(1);
   }
   
   function exportPurRcvParts() {
   	fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseArrConfir/exportPurRcvParts.do";
   	fm.submit();
   }
</script>
</div>
</body>
</html>
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
    <!-- <script type="text/javascript" src="<%=request.getContextPath()%>/js/web/$-1.7.2.min.js"></script> -->
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>采购到货打印</title>
</head>
<body >
<div class="wbox">
    <div class="navigation">
    	<img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置： 总部采购管理&gt; 采购订单管理&gt; 采购分包打印
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
                
                <td class="right">供应商：</td>
                <td >
                    <input class="middle_txt" type="text" id="VENDER_NAME" name="VENDER_NAME" value=""/>
                </td>
                
                <td class="right">制单日期：</td>
                <td >
                	<input name="sCreateDate" readonly="readonly" id="sCreateDate" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="sCreateDate,eCreateDate" style="width:80px;">
                   <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"/>
				   <label>至</label>
                   <input name="eCreateDate" readonly="readonly" id="eCreateDate" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="sCreateDate,eCreateDate" style="width:80px;">
                   <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"/>
                </td>
            </tr>
            
            <tr>
                <td class="right">备件编码：</td>
                <td >
                	<input class="middle_txt" type="text" id="PART_OLDCODE"  name="PART_OLDCODE" value=""/>
                </td>
                
                <td class="right">备件名称：</td>
                <td >
                    <input class="middle_txt" type="text" id="PART_CNAME" name="PART_CNAME" value=""/>
                </td>
                
                <td class="right">备件件号：</td>
                <td >
                    <input class="middle_txt" type="text" id="PART_CODE" name="PART_CODE" value=""/>
                </td>
            </tr>
            
           <%--  <tr>
                <td class="right">是否打印ASN单：</td>
                <td >
                	<script type="text/javascript">
                   		genSelBoxExp("IS_PRINT", <%=Constant.IF_TYPE%>, "<%=Constant.IF_TYPE_NO%>", true, "short_sel", "", "false", '');
               		</script>
                </td>
                
                <td class="right">是否打印二维码:</td>
                <td >
                   <script type="text/javascript">
                        genSelBoxExp("PRINT_FLAG", <%=Constant.IF_TYPE %>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
                
                <td class="right"></td>
                <td >
                </td>
            </tr> --%>
            
            <tr>
                <td class="center" colspan="6">
                	<input type="button" class="u-button" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1);" value="查询"/>&nbsp;
                	<input type="button" class="normal_btn" onclick="toPurConfirmAsnPrints()" value="打印分包单"/>&nbsp;
                </td>
            </tr>
        </table>
	 </div>
	 </div>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>
</div>
    <script type="text/javascript">
       // autoAlertException();//输出错误信息		sql.append("  F_GET_LOCS_CODE(p1.ORG_ID,p1.PART_ID,p.WH_ID) AS LOC_CODE,\n");
        var myPage;
        var url = '<%=contextPath%>/parts/planManager/PartPlanManager/getPurRcvPrintInfo.json';
        var title = null;
        var columns = [
				{header: "序号", renderer: getIndex},
				{header: '<input type="checkbox" onclick="ckAll(this)">',dataIndex:'POLINE_ID',renderer: setCheck, style:"text-align: center"},
				{header: "操作",dataIndex:'POLINE_ID',renderer: myLink, style:"text-align: center"},
				{header: "订单单号",dataIndex:'ORDER_CODE', style:"text-align: center"},
				{header: "备件编码",dataIndex:'PART_OLDCODE', style:"text-align: center"},
				{header: "备件名称",dataIndex:'PART_CNAME', style:"text-align:center",renderer:subPartCnameText},
				{header: "备件件号",dataIndex:'PART_CODE', style:"text-align: center"},
				{header: "采购数量",dataIndex:'BUY_QTY', style:"text-align: center"},				
				{header: "库房",dataIndex:'WH_NAME', style:"text-align: center"},
				{header: "货位",dataIndex:'LOC_CODE', style:"text-align: center"},
				{header: "备注",dataIndex:'REMARK', style:"text-align: center"},
				{header: "单位",dataIndex:'UNIT', style:"text-align: center"},
				{header: "供应商",dataIndex:'VENDER_NAME', style:"text-align: center"},
				{header: "备件类型",dataIndex:'PART_TYPE', style:"text-align: center",renderer:getItemValue},
				{header: "制单日期",dataIndex:'CREATE_DATE', style:"text-align: center"},
				{header: "状态",dataIndex:'STATE', style:"text-align: center",renderer:getItemValue},
				{header: "打印日期",dataIndex:'PRINT_DATE', style:"text-align: center"}
            ];
        //setCheck
        function setCheck(value,meta,record){
        	var str = '<input type="checkbox" name="ck" value="'+value+'" >';
        	return str;
        }
        //操作链接生成
        function myLink(value,meta,record){
        	var part_Id = record.data.PART_ID;
        	var order_id=record.data.ORDER_ID;
        	var str = '<a href="<%=contextPath%>/parts/planManager/PartPlanManager/toPurConfirmAsnPrint.do?po_Id='+value+'" target="_bank">[打印分包单]</a>&nbsp;';
        		str+= '<a href="<%=contextPath%>/parts/planManager/PartPlanManager/toPurcharOrderQrPrint.do?inId='+order_id+'&part_Id='+part_Id+'" target="_bank">[打印二维码]</a>&nbsp;';
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
        
        //确认到货数
        function disPurConfirm(conId){
        	MyConfirm('确认失效？',function(){
        		var urlkey = "<%=contextPath%>/parts/planManager/PartPlanManager/disPurConfirm.json?conId="+conId;
            	sendAjax(urlkey, getResult, 'fm');
        	})
        
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
        	btnEable();
        	__extQuery__(1);
        }
        
        function toPurConfirmAsnPrints(){
        	 var inIds = document.getElementsByName("ck");
             var l = inIds.length;
             var cnt = 0;
             for (var i = 0; i < l; i++) {
                 if (inIds[i].checked) {
                     cnt++;}
             }
             if (cnt == 0) {
                 MyAlert("请选择采购单信息！");
                 return;
             }
        	document.fm.action = '<%=contextPath%>/parts/planManager/PartPlanManager/toPurConfirmAsnPrints.do';
			document.fm.target = "_blank";
			document.fm.submit();
        }
        $(document).ready(function(){__extQuery__(1);});
    </script>
</div>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>采购订单明细及交货率统计</title>
    <script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }

    </script>
</head>
<body onunload='javascript:destoryPrototype()' onload="queryOrder();">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;
    当前位置：报表管理&gt;配件报表&gt;配件计划报表&gt;采购订单明细及交货率统计
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage"/>
    <input type="hidden" name="partId" id="partId"/>
    <table id="queryAll" class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />查询条件</th>
	    <tr >
	       <td width="10%" align="right">采购订货单号：</td>
           <td width="25%"><input class="long_txt" type="text" name="ORDER_CODE"/></td>
           <td width="10%" align="right">采购计划单号：</td>
	       <td width="20%" ><input class="middle_txt" type="text" name="PLAN_CODE" id="PLAN_CODE"/></td>
	       <td width="10%" align="right">供应商：</td>
	       <td width="25%">
           <div align="left">
           		<input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME" />
			    <input class="mark_btn" type="button" value="&hellip;" onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
			    <INPUT class=short_btn onclick="clearInput();" value=清除 type=button name=clrBtn>
			    <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
            </div>
           </td>
      </tr>
	
       <tr id="PART_DIV" style="display:none">
            <td width="10%" align="right">配件编码：</td>
            <td width="25%"><input name="PART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE"/></td>
            <td width="10%" align="right">配件件号：</td>
            <td width="20%"><input name="PART_CODE" type="text" class="middle_txt" id="PART_CODE"/></td>
            <td width="10%" align="right">配件类型：</td>
                <td width="25%">
                    <script type="text/javascript">
                    genSelBoxExp("PART_TYPE", <%=Constant.PART_BASE_PART_TYPES %>, "", true, "long_sel", "", "false", '');
                    </script>
                </td>
        </tr>
        
         <tr>

	       <td width="10%" align="right">制单日期：</td>
	       <td width="25%">
	            <input name="beginTime" id="t1" value="${old}" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't1', false);"/>
           		&nbsp;至&nbsp;
           		<input name="endTime" id="t2" value="${now}" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't2', false);"/>
	       </td>
	       <td width="10%" align="right"></td>
            <td width="20%"></td>
            <td width="10%" align="right"></td>
            <td width="25%"></td>
	       
             <!-- <td width="10%" align="right">制造商名称：</td>
	       <td width="30%">
           <div align="left">
           		<input class="middle_txt" type="text" readonly="readonly" id="MAKER_NAME" name="MAKER_NAME"/>
                    <input class="mark_btn" type="button" value="&hellip;"
                           onclick="showPartMaker('MAKER_NAME','MAKER_ID','false')"/>
                           <INPUT class=normal_btn onclick="clearMInput();" value=清除 type=button name=clrBtn>
                    <input id="MAKER_ID" name="MAKER_ID" type="hidden" value="">
            </div>
           </td> -->
      </tr>
      
         <tr>
                <td colspan="6" align="center">
                <input type="radio" name="RADIO_SELECT" value="1" checked onclick="changeDiv(this);"/>交货情况
                <input type="radio" name="RADIO_SELECT" value="2" onclick="changeDiv(this);"/>未满足情况
                </td>
            </tr>
            
	  <tr>
	   <td   colspan="6" align="center">
	   		<input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="queryOrder();"/>
            <input class="normal_btn" type="button" value="导出" onclick="expPurOrderDtlDExcel();"/>
       </td>
      </tr>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript">
autoAlertException();//输出错误信息
var myPage;
var url = "<%=contextPath%>/report/partReport/partPlanReport/PurOrderDtlReport/queryPurOrderDtlDelivery.json";

var title = null;
var columns = null;
var len = 0;
function queryOrder(){
	var chk = 0;
	var chkObjs = document.getElementsByName("RADIO_SELECT");
	for(var i=0;i<chkObjs.length;i++){
        if(chkObjs[i].checked){
            chk = i;
            break;
        }
    }
	if(chk==0){
		columns = [
		           {header: "序号", align:'center',renderer:getIndex},
		           {header: "计划单号", dataIndex: 'PLAN_CODE',  align: 'center'},
		           {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center', renderer:formatDate},
		           {header: "预计到货日期", dataIndex: 'FC_DATE', align: 'center', renderer:formatDate},
		           {header: "总计划金额", dataIndex: 'PL_AMT', style: 'text-align:right'},
		           {header: "总计划数量", dataIndex: 'PL_QTY',  align: 'center'},
		           {header: "计划类型", dataIndex: 'PLAN_TYPE',  align: 'center'},
		           {header: "采购订单号", dataIndex: 'ORDER_CODE',  align: 'center'},
		           {header: "订货品种数", dataIndex: 'POCNT',  align: 'center'},
		           {header: "订货总数量", dataIndex: 'BUY_QTY',  align: 'center'},
		           {header: "订单金额", dataIndex: 'BUY_AMT', style: 'text-align:right'},
		           {header: "供应商", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
		           {header: "订单状态", dataIndex: 'STATE',  align: 'center'},
		           {header: "已交货品种数", dataIndex: 'INCNT',  align: 'center'},
		           {header: "已交货数", dataIndex: 'IN_QTY',  align: 'center'},
		           {header: "已交货金额", dataIndex: 'IN_AMT', style: 'text-align:right'},
		           {header: "品种满足率", dataIndex: 'CNTRT',  align: 'center'},
		           {header: "数量满足率", dataIndex: 'QTYRT',  align: 'center'},
		           {header: "完成时间", dataIndex: 'CMPDATE', align: 'center'}
		       ];
		len = columns.length;
	}else{
		columns = [
		           {header: "序号", align:'center',renderer:getIndex},
		           {header: "计划单号", dataIndex: 'PLAN_CODE',  align: 'center'},
		           {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center', renderer:formatDate},
		           {header: "预计到货日期", dataIndex: 'FC_DATE', align: 'center', renderer:formatDate},
		           {header: "计划类型", dataIndex: 'PLAN_TYPE',  align: 'center'},
		           {header: "采购订单号", dataIndex: 'ORDER_CODE',  align: 'center'},
		           {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
		           {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
		           {header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
		           {header: "单位", dataIndex: 'UNIT',  align: 'center'},
		           {header: "计划数量", dataIndex: 'CHECK_NUM', align: 'center'},
		           {header: "订单数量", dataIndex: 'BUY_QTY', align: 'center'},
		           {header: "订单金额", dataIndex: 'BUY_AMOUNT', style: 'text-align:right'},
		           {header: "已转验收单数量", dataIndex: 'CHECK_QTY', align: 'center'},
		           {header: "剩余订单数量", dataIndex: 'SPARE_QTY', align: 'center'},
		           {header: "订单创建日期", dataIndex: 'O_CREATE_DATE', align: 'center', renderer:formatDate},
		           {header: "入库数量", dataIndex: 'IN_QTY', align: 'center'},
		           {header: "入库金额", dataIndex: 'IN_AMOUNT', style: 'text-align:right'},
		           {header: "入库日期", dataIndex: 'IN_DATE', align: 'center', renderer:formatDate}
		       ];
		len = columns.length;
	}
	__extQuery__(1);
}

function changeDiv(obj){
	var value = obj.value;
	if(value=="1"){
		$("PART_DIV").style.display = "none";
	}
	if(value=="2"){
		$("PART_DIV").style.display = "";
	}
}

//格式化日期
function formatDate(value,meta,record){
	var output = value.substr(0,10);
	return output;
}

//导出
function expPurOrderDtlDExcel() {
    fm.action = "<%=contextPath%>/report/partReport/partPlanReport/PurOrderDtlReport/expPurOrderDtlDceExcel.do";
    fm.target = "_self";
    fm.submit();
}

function clearInput() {
	//清空选定供应商
	document.getElementById("VENDER_ID").value = '';
	document.getElementById("VENDER_NAME").value = '';
}
</script>
</div>
</body>
</html>
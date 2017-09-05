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
    <title>入库统计</title>
    <script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }

    </script>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;本部仓储报表&gt;入库统计
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage"/>
    <input type="hidden" name="partId" id="partId"/>
    <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />查询条件</th>
	    <tr >
	       <td width="10%" align="right">入库单号：</td>
	       <td width="20%" ><input class="middle_txt" type="text" name="IN_CODE" id="IN_CODE"/></td>
	       <td width="10%" align="right">验收单号：</td>
	       <td width="20%" ><input class="middle_txt" type="text" name="CHECK_CODE" id="CHECK_CODE"/></td>
	       <td width="10%" align="right">入库退货单号：</td>
           <td width="20%"><input class="middle_txt" type="text" name="RETURN_CODE" id="RETURN_CODE"/></td>
      </tr>
	 <tr>

	        <td width="10%" align="right">车型：</td>
	       <td width="20%" ><input class="middle_txt" type="text" name="MODEL_NAME" id="MODEL_NAME"/></td>
	       <td width="10%" align="right">配件类型：</td>
                <td width="20%">
                    <script type="text/javascript">
                    genSelBoxExp("PART_TYPE", <%=Constant.PART_BASE_PART_TYPES %>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
             <td width="10%" align="right">供应商：</td>
	       <td width="30%">
           <div align="left">
           		<input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME" />
			    <input class="mark_btn" type="button" value="&hellip;" onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
			    <INPUT class=normal_btn onclick="clearInput();" value=清除 type=button name=clrBtn>
			    <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
            </div>
           </td>
      </tr>
       <tr>
            <td width="10%" align="right">配件编码：</td>
            <td width="20%"><input name="PART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE"/></td>
            <td width="10%" align="right">配件名称：</td>
            <td width="20%"><input name="PART_CNAME" type="text" class="middle_txt" id="PART_CNAME"/></td>
            <td width="10%" align="right">配件件号：</td>
            <td width="20%"><input name="PART_CODE" type="text" class="middle_txt" id="PART_CODE"/></td>
            
        </tr>
        
	  <tr>
	   <td   colspan="6" align="center">
	   		<input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1);"/>
            <input class="normal_btn" type="button" value="导出" onclick="expPurOrderInExcel();"/>
       </td>
      </tr>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript">
autoAlertException();//输出错误信息
var myPage;
var url = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/queryPurOrderIn.json";

var title = null;

var columns = [
    {header: "序号", align:'center',renderer:getIndex},
    {header: "入库单号", dataIndex: 'IN_CODE',  align: 'center'},
    {header: "进货单号", dataIndex: 'CHECK_CODE',  align: 'center'},
    {header: "入库退货单号", dataIndex: 'RETURN_CODE',  align: 'center'},
    {header: "供应商代码", dataIndex: 'VENDER_CODE', style: 'text-align:left'},
    {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "入库数量", dataIndex: 'IN_QTY', align: 'center'},
    {header: "不含税计划价", dataIndex: 'BUY_PRICE_NOTAX', style: 'text-align:right'},
    {header: "不含税计划金额", dataIndex: 'IN_AMOUNT_NOTAX', style: 'text-align:right'},
    {header: "车型", dataIndex: 'MODEL_NAME', align: 'center'},
    {header: "配件类型", dataIndex: 'PART_TYPE', align:'center',renderer: getItemValue},
    {header: "备注", dataIndex: 'REMARK', align: 'center'}
];

var len = columns.length;

//格式化日期
function formatDate(value,meta,record){
	var output = value.substr(0,10);
	return output;
}

//导出
function expPurOrderInExcel() {
    fm.action = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/expPurOrderInExcel.do";
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
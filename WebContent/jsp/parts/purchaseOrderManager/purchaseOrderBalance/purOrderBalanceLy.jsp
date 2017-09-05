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
    <title>料据拨付单查询(单个领用单明细)</title>
    <script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }

    </script>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：
    当前位置：报表管理&gt;本部计划报表&gt;料据拨付单查询&gt;单个领用单明细
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="balanceCode" id="balanceCode" value="${balanceCode }"/>
    <table id="queryAll" class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />查询条件</th>
       <tr>
            <td width="10%" align="right">配件编码：</td>
            <td width="20%"><input name="PART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE"/></td>
            <td width="10%" align="right">配件件号：</td>
            <td width="20%"><input name="PART_CODE" type="text" class="middle_txt" id="PART_CODE"/></td>
            <td width="10%" align="right">配件类型：</td>
                <td width="20%">
                    <script type="text/javascript">
                    genSelBoxExp("PART_TYPE", <%=Constant.PART_BASE_PART_TYPES %>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
        </tr>
	  <tr>
	   <td   colspan="6" align="center">
	   		<input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1);"/>
       </td>
      </tr>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript">
autoAlertException();//输出错误信息
var myPage;
var url = "<%=contextPath%>/report/partReport/partPlanReport/PurOrderBalanceReport/queryPurOrderBalanceByCode.json";

var title = null;
var columns = [
      		           {header: "序号", align:'center',renderer:getIndex},
    		           {header: "料据拨付单号", dataIndex: 'BALANCE_CODE',  align: 'center'},
    		           {header: "进货单号", dataIndex: 'CHECK_CODE',  align: 'center'},
    		           {header: "采购订单号", dataIndex: 'ORDER_CODE',  align: 'center'},
    		           {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    		           {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    		           {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    		           {header: "单位", dataIndex: 'UNIT',  align: 'center'},
    		           {header: "进货数量", dataIndex: 'CHECK_QTY', align: 'center'},
    		           {header: "入库数量", dataIndex: 'IN_QTY', align: 'center'},
    		           {header: "开票数量", dataIndex: 'BALANCE_QTY', align: 'center'},
    		           {header: "入库退货数量", dataIndex: 'RETURN_QTY', align: 'center'},
    		           {header: "单价", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
    		           {header: "金额", dataIndex: 'BALANCE_AMOUNT', style: 'text-align:right'},
    		           {header: "编制人", dataIndex: 'NAME', align: 'center'},
    		           {header: "编制时间", dataIndex: 'CREATE_DATE', align: 'center', renderer:formatDate},
    		           {header: "供货商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
    		           {header: "供货厂家名称", dataIndex: 'MAKER_NAME', style: 'text-align:left'}
    		       ];
var len = columns.length;

//格式化日期
function formatDate(value,meta,record){
	var output = value.substr(0,10);
	return output;
}
</script>
</div>
</body>
</html>
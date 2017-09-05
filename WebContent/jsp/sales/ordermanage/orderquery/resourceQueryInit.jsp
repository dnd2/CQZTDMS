<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>待经销商确认资源保留数量统计</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：销售订单管理 &gt; 订单查询 &gt; 发运订单汇总查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
	<tr>
	    <td align="right" class="table_list_th">发运申请时间：</td>
	    <td class="table_list_th" id="timeId">
	    	<div align="left">
       		<input name="startDate" id="t1" value="${startDate }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);" />
       		&nbsp;至&nbsp;
       		<input name="endDate" id="t2" value="${endDate }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);" />
   		</div>
        </td>
    	<td align="right" class="table_list_th">选择业务范围：</td>
	    <td class="table_list_th">
	    	<select name="areaId" class="short_sel">
      	  		<option value="">-请选择-</option>
				<c:forEach items="${areaList}" var="po">
					<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
				</c:forEach>
	        </select>
        </td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
    </tr>
    <tr>
    	<td width="12%" align="right" nowrap>开票经销商：</td>
		<td align="left" nowrap>
			<input type="text" class="middle_txt" name="billingOrgCode" size="15" value="" readonly="readonly" id="billingOrgCode"/>
			<input name="button3" type="button" class="mini_btn" onclick="showOrgDealer('billingOrgCode','','true', '${orgId}')" value="..." />
           	<input class="cssbutton" type="button" value="清空" onclick="clrTxt('billingOrgCode');"/>
        </td>
        
        <td width="12%" align="right" nowrap>定货经销商：</td>
		<td width="40%" align="left" nowrap>
			<input type="text" class="middle_txt" name="orderOrgCode" size="15" value="" readonly="readonly" id="orderOrgCode"/>
			<input name="button3" type="button" class="mini_btn" onclick="showOrgDealer('orderOrgCode','','true', '${orgId}')" value="..." />
           	<input class="cssbutton" type="button" value="清空" onclick="clrTxt('orderOrgCode');"/>
        </td>
    </tr>
	<tr>
		<td align="center" colspan="6">
			<input name="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
			<input name="button1" type=button class="cssbutton" onClick="downLoad();" value="下载">
		</td>
	</tr>
	</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/ordermanage/orderquery/DealerResourceQuery/resourceQueryList.json?COMMAND=1";
	var title = null;
	var columns = [
				{header: "开票经销商区域", dataIndex: 'ORG_NAME', align:'center'},
				{header: "开票经销商代码", dataIndex: 'BILLINGORGCODE', align:'center'},
	            {header: "开票经销商名称", dataIndex: 'BILLINGORGNAME', align:'center'},
				{header: "订货经销商代码", dataIndex: 'ORDERORGCODE', align:'center'},
				{header: "订货经销商名称", dataIndex: 'ORDERORGNAME', align:'center'},
				{header: "配置代码", dataIndex: 'STATUSCODE', align:'center'},
				{header: "已提报", dataIndex: 'HAS_COMMIT', align:'center'},
				{header: "代交车审核", dataIndex: 'DJC_CHECK', align:'center'},
				{header: "经销商待确认", dataIndex: 'DEALERCOMMIT', align:'center'},
				{header: "初审完成", dataIndex: 'PRE_CHECK', align:'center'},
				{header: "审核完成", dataIndex: 'HAS_CHECK', align:'center'}
		      ];
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
	function downLoad(){
    	$('fm').action="<%=contextPath%>/sales/ordermanage/orderquery/DealerResourceQuery/resourceQueryListDownLoad.do";
     	$('fm').submit();
    }
</script>
<!--页面列表 end -->
</body>
</html>
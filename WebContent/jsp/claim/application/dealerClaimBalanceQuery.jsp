<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%String contextPath = request.getContextPath();%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>结算单扣款明细查询</title>
	<script type="text/javascript">
	    function doInit(){
		   loadcalendar();
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />当前位置：售后服务管理&gt;索赔结算管理&gt;结算单扣款明细查询</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="true">经销商代码：</td>
		<td align="left" nowrap="true">
			<input class="middle_txt" id="dealerCode"  name="dealerCode" type="text"/>
            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>
		</td>
		<td align="right" nowrap="true">经销商名称：</td>
		<td align="left" nowrap="true">
			<input type="text" name="DEALER_NAME" id="DEALER_NAME" datatype="1,is_digit_letter_cn,30" class="middle_txt" value=""/>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">结算单号：</td>
        <td><input name="balanceNo" value="" type="text" class="middle_txt"/></td>
		<td align="right" nowrap="true">结算日期：</td>
		<td align="left" nowrap="true">
			<input type="text" name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">产地：</td>
		<td>
 <select style="width: 152px;" name="yieldly" id="yieldly">
				 <option value="" >
    				-请选择-
    			  </option>
	              <c:forEach var="Area" items="${Area}" >
 				  <option value="${Area.areaId}" >
    				<c:out value="${Area.areaName}"/>
    			  </option>
    			 </c:forEach>
             </select>
             </td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/application/DealerBalance/claimBalanceQuery.json";
		var title = null;
		
		var columns = [
						{header: "序号",align:'center',renderer:getIndex},
						{header: "结算单号",dataIndex: 'BALANCE_NO',align:'center'},
						{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
						{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
						{header: "生产基地",dataIndex: 'AREA_NAME',align:'center'},
						{header: "结算起",dataIndex: 'START_DATE',align:'center'},
						{header: "结算止",dataIndex: 'END_DATE',align:'center'},
						{header: "申请总费用(元)",dataIndex: 'APPLY_AMOUNT',align:'center'},
						{header: "结算总费用(元)",dataIndex: 'BALANCE_AMOUNT',align:'center'},
						{header: "扣款总费用(元)",dataIndex: 'KK',align:'center'},
						{header: "操作",dataIndex: 'ID',align:'center', renderer:accAudut}
			      ];
	      
	//修改的超链接
	function accAudut(value,meta,record){
		return String.format("<a href='#' onclick='queryInfo(\""+value+"\")'>[查看明细]</a>");
	}
	function queryInfo(id){
		location.href='<%=contextPath%>/claim/application/DealerBalance/detailByBalanceId.do?id='+id,"结算单明细", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no";
	}
</script>
</body>
</html>
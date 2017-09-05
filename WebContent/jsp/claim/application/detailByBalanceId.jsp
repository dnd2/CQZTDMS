<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.*"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>结算单扣款明细查询</title>
	</head>
<body >
<form method="post" name="fm" id="fm">
<input type="hidden" name="id" value="<c:out value="${map.ID}"/>"/>
	<table width="100%">
	    <tr>
		    <td>
				<div class="navigation">
			    	<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;结算单扣款明细查询
			    </div>
		    </td>
	    </tr>
	</table>
	<table class="table_edit">
		<tr>
			<td align="right" nowrap="nowrap">结算单号：</td>
			<td align="left" nowrap="nowrap">
			   <c:out value="${map.BALANCE_NO }"/>
			</td>
			<td align="right" nowrap="nowrap">经销商代码：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.DEALER_CODE }"/>
			</td>
			<td align="right" nowrap="nowrap">经销商名称：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.DEALER_NAME }"/>
			</td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">基地：</td>
			<td align="left" nowrap="nowrap">
					${map.AREA_NAME }
			</td>
			<td align="right" nowrap="nowrap">结算起：</td>
			<td align="left" nowrap="nowrap">
				<fmt:formatDate value="${map.START_DATE }" pattern="yyyy-MM-dd" />
			</td>
			<td align="right" nowrap="nowrap">结算止：</td>
			<td align="left" nowrap="nowrap">
				<fmt:formatDate value="${map.END_DATE }" pattern="yyyy-MM-dd" />
			</td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">申请工时金额：</td>
			<td align="left" nowrap="nowrap">
			   <c:out value="${map.LABOUR_AMOUNT_BAK }"/>
			</td>
			<td align="right" nowrap="nowrap">申请配件金额：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.PART_AMOUNT_BAK }"/>
			</td>
			<td align="right" nowrap="nowrap">申请追加工时金额：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.APPEND_LABOUR_AMOUNT_BAK }"/>
			</td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">申请其他费用金额：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.OTHER_AMOUNT_BAK }"/>
			</td>
			<td align="right" nowrap="nowrap">申请保养费用金额：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.FREE_AMOUNT_BAK }"/>
			</td>
			<td align="right" nowrap="nowrap">申请服务活动费用：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.SERVICE_TOTAL_AMOUNT_BAK }"/>
			</td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">申请特殊费用：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.MARKET_AMOUNT_BAK }"/>
			</td>
			<td align="right" nowrap="nowrap">申请特殊外出费用：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.SPEOUTFEE_AMOUNT_BAK }"/>
			</td>
			<td align="right" nowrap="nowrap">运费：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.RETURN_AMOUNT_BAK }"/>
			</td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">申请总费用：</td>
			<td colspan="5" align="left" nowrap="nowrap">
				<c:out value="${map.APPLY_AMOUNT }"/>
			</td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">财务扣款：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.FINANCIAL_DEDUCT }"/>
			</td>
			<td align="right" nowrap="nowrap">服务活动扣款：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.SERVICE_DEDUCT }"/>
			</td>
			<td align="right" nowrap="nowrap">保养费扣款：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.FREE_DEDUCT }"/>
			</td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">运费扣款：</td>
			<td colspan="5" align="left" nowrap="nowrap">
				<c:out value="${map.RETURN_AMOUNT_BAK-map.RETURN_AMOUNT }"/>
			</td>
		</tr>
	</table>
	<br/>
	<table class="table_list">
	<tr class="table_list_th">
		<th  >行号</th>
		<th  >索赔单号</th>
		<th  >申请金额</th>
		<th  >结算金额</th>
		<th  >扣款金额</th>
	</tr>
	<%
  		List list=(List)request.getAttribute("list");
    	for(int i=0;i<list.size();i++)
    	{
    		Map map=(Map)list.get(i);
    		if(i+1>=4) break;
    		int col=i%2;
    		if(col==0) col=1;
    		else col=2;
  	%>
	<tr class="table_list_row<%=col %>">
		<td ><%=i+1 %></td>
		<td  ><%=map.get("CLAIM_NO") %></td>
		<td  ><%=map.get("REPAIR_TOTAL") %></td>
		<td  ><%=map.get("BALANCE_AMOUNT") %></td>
		<td  ><%=map.get("SUB_AMOUNT") %></td>
	</tr>
	<%}//if(list.size()>=4){%>
	<tr class="table_list_row1">
		<td></td>
  		<td align="center"><a href="#" onclick="toDetail(${balanceId});">更多...</a></td>
  		<td></td>
  		<td></td>
  		<td></td>
  	</tr>
  	<%//}%>
	</table>
	<br />
	<table class="table_list">
	<tr class="table_list_th">
		<th  >行号</th>
		<th  >特殊费用单号</th>
		<th  >申请金额</th>
		<th  >结算金额</th>
		<th  >扣款金额</th>
	</tr>
  <%
  		List feeDetailList=(List)request.getAttribute("feeDetail");
	    for(int i=0;i<feeDetailList.size();i++){
	    	Map map=(Map)feeDetailList.get(i);
	    	if(i+1>=4) break;
	    	int col=i%2;
	    	if(col==0) col=1;
	    	else col=2;
  %>
	<tr class="table_list_row<%=col %>">
	    <td ><%=i+1 %></td>
		<td  ><%=map.get("FEE_NO") %></td>
		<td  ><%=map.get("DECLARE_SUM1") %></td>
		<td  ><%=map.get("DECLARE_SUM") %></td>
		<td  ><%=map.get("FEE_AMOUNT") %></td>
	</tr>
	<%}//if(feeDetailList.size()>=4){%>
  	<tr class="table_list_row1">
  		<td></td>
  		<td align="center"><a href="#" onclick="toDetail1(${balanceId});">更多...</a></td>
  		<td></td>
  		<td></td>
  		<td></td>
  	</tr>
  	<%//}%>
	</table>
	<br />
	<table class="table_edit">
		<tr>
			<td width="30%" align="right" nowrap="nowrap">旧件扣款：</td>
			<td width="30%" align="left" nowrap="nowrap">
			   <c:out value="${map.OLD_DEDUCT }"/>
			</td>
			<td width="40%">&nbsp;</td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">考核扣款：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.CHECK_DEDUCT }"/>
			</td>
			<td width="80%">&nbsp;</td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">行政扣款：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.ADMIN_DEDUCT }"/>
			</td>
			<td width="80%">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2" align="right"><a href="#" onclick="toDetail2(${balanceId});">更多...</a></td>
		</tr>
	</table>
	<br/>
	<table class="table_edit">
		<tr>
			<td align="center">
				<input type="button" class="normal_btn" name="backBtn" onclick="javascript:history.go(-1)" value="返回"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
function toDetail(id){
	var tarUrl = "<%=contextPath%>/claim/application/DealerBalance/claimInfo.do?id="+id;
	var width=900;
	var height=500;
	var screenW = window.screen.width-30;	
	var screenH = document.viewport.getHeight();

	if(screenW!=null && screenW!='undefined')
		width = screenW;
	if(screenH!=null && screenH!='undefined')
		height = screenH;
	OpenHtmlWindow(tarUrl,width,height);
}
function toDetail1(id){
	var tarUrl = "<%=contextPath%>/claim/application/DealerBalance/feeInfo.do?id="+id;
	var width=900;
	var height=500;
	var screenW = window.screen.width-30;	
	var screenH = document.viewport.getHeight();

	if(screenW!=null && screenW!='undefined')
		width = screenW;
	if(screenH!=null && screenH!='undefined')
		height = screenH;
	OpenHtmlWindow(tarUrl,width,height);
}
function toDetail2(id){
	var tarUrl = "<%=contextPath%>/claim/application/DealerBalance/deductByBalanceId.do?id="+id;
	var width=900;
	var height=500;
	var screenW = window.screen.width-30;	
	var screenH = document.viewport.getHeight();

	if(screenW!=null && screenW!='undefined')
		width = screenW;
	if(screenH!=null && screenH!='undefined')
		height = screenH;
	OpenHtmlWindow(tarUrl,width,height);
}
</script>
</body>
</html>
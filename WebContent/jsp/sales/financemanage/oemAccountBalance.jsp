<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>可用金额查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 财务管理 &gt; 可用金额查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >	
	<c:if test="${returnValue==2}">
		<tr>
		<td colspan="4"><font color="red">注意事项:</font></td>
	</tr>
		<tr>
		<td  colspan="4"><font color="red">1、入账联系人：现金-喻起萍 67595227 ，承兑汇票-郭萍 67591948 兵财额度-刘昕 67591948；</font></td>
		</tr>
		<tr>
		<td  colspan="4"><font color="red">2、如以上人员反映已录入，但dms在<font size="3" ><strong>2小时</strong></font>后无显示，请联系杨书友：67591947，确认财务的资金系统是否已入账。</font></td>
		</tr>
		<tr>
		<td  colspan="4"><font color="red">3、到账时间：现款-厂家财务收到款后当天录入系统，如<font size="3" ><strong>2-3天后</strong></font>未入账再进行咨询，请不要频繁打电话催促，承兑汇票、兵财额度-收到汇票和银行通知额度后录入系统。  
</font></td>
		</tr>
		</c:if>
		<tr>
			<td  colspan="4"><font color="red"><strong>兵财融资可用金额计算：兵财融资账户金额 - （轿车所有基地兵财融资冻结金额 + 微车所有基地兵财融资冻结金额） </strong></font></td>
		</tr>
		<tr>
			<td  colspan="4"><font color="red"> 1、现金、承兑汇票及长安信贷：扣订单的启票单位款项;</font></td>
		</tr>
		<tr>
			<td  colspan="4"><font color="red">2、三方信贷：扣订单的启票单位和采购单位款项，启票单位款项=启票单位自有款项+采购单位款项; </font></td>
		</tr>
		<tr>
			<td  colspan="4"><font color="red">3、兵财存货融资：扣订单的采购单位款项; </font></td>
		</tr>
		<tr>
			<td  colspan="4"><font color="red">4、所有资金类型订单的价格折扣均扣启票单位款项.</font></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap" class="table_query_2Col_label_6Letter"> 选择业务范围：</td>
		    <td align="left" nowrap="nowrap" class="table_edit_2Col_input">
		    	<select name="areaId" class="short_sel" id="areaId">
		    		<option value="">-请选择-</option>
					<c:if test="${areaList!=null}">
						<c:forEach items="${areaList}" var="list">
							<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
						</c:forEach>
					</c:if>
				</select>
			</td>
			<td align="right">选择经销商：</td>
			<td>
				<input type="text" class="middle_txt" name="dealerCode" size="15" id="dealerCode" value="" />
				<input name="button3" type="button"  class="mini_btn" onclick="showOrgDealer('dealerCode','','true');" value="..." />
				<input name="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
				<input name="button4" type=button class="cssbutton" onClick="downLoad();" value="下载">
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
	var url = "<%=contextPath%>/sales/financemanage/OemAccountBalance/oemAccountBalanceQuery.json";
	var title = null;
	var columns = [
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center',renderer:goDealerInfo},
				<c:forEach items="${typeList}" var="po">
					{header: "${po.typeName}", dataIndex: '${po.typeName}', align:'right',renderer:myformat},
				</c:forEach>
				{header: "可用合计", dataIndex: 'KYHJ', align:'center',renderer:myformat}
		      ];
    
    //设置金钱格式
    function myformat(value,metaDate,record){
        return String.format(amountFormat(value));
    }
    function goDealerInfo(value,metaDate,record){
    	var numberUrl="<%=request.getContextPath()%>/sales/financemanage/OemAccountBalance/getDealerInfo.do?dealerId="+record.data.DEALER_ID;
    	return "<a href=\"#\" onclick=\"mydetail('"+numberUrl+"');\">"+record.data.DEALER_NAME+"</a>";
       }
    function downLoad(){
    	$('fm').action="<%=contextPath%>/sales/financemanage/OemAccountBalance/oemAccountBalanceDownLoad.do";
     	$('fm').submit();
    }
    function mydetail(url)
	{
		OpenHtmlWindow(url,700,400);
	}
</script>
<!--页面列表 end -->
</body>
</html>
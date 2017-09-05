<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>领用单查询及统计</title>
<script language="javascript" type="text/javascript">
	function doInit(){
		loadcalendar();  //初始化时间控件
		__extQuery__(1);
	}
</script>
</head>
<body>
  <form method="post" name ="fm" id="fm">
	<div class="wbox">
		<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />
		  &nbsp;当前位置：报表管理&gt;配件报表&gt;本部计划报表&gt;调拨历史查询
		  <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
		  <input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
		  <input type="hidden" name="companyName" id="companyName" value="${companyName }"/>
		  <input type="hidden" name="actionURL" id="actionURL" value=""/>
		</div>
		<table class="table_query">
			<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 查询条件</th>
		    <tr >
		       <td width="10%" align="right">计划员：</td>
		       <td width="20%" ><input class="middle_txt" type="text" name="planner" id="planner"/></td>
		       <td width="10%" align="right">供应商：</td>
		       <td width="30%">
	           <div align="left">
	           		<input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME" />
				    <input class="mark_btn" type="button" value="&hellip;" onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
				    <INPUT class=short_btn onclick="clearInput();" value=清除 type=button name=clrBtn>
				    <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
	            </div>
	           </td>
	           <td width="10%" align="right">库房：</td>
	           <td width="20%">
	               <select id="WH_ID" name="WH_ID" class="short_sel">
	                   <option value="">-请选择-</option>
	                   <c:forEach items="${wareHouses}" var="wareHouse">
	                       <option value="${wareHouse.whId }">${wareHouse.whName }</option>
	                   </c:forEach>
	               </select>
	           </td>
	      </tr>
		  <tr>
		       <td width="10%" align="right">配件类型：</td>
	           <td width="20%">
	               <script type="text/javascript">
	               	genSelBoxExp("PART_TYPE", <%=Constant.PART_BASE_PART_TYPES %>, "", true, "short_sel", "", "false", '');
	               </script>
	           </td>
		       <td width="10%" align="right">制单日期：</td>
	           <td width="25%">
	               <input id="checkSDate" class="short_txt" name="checkSDate" value="${old}" datatype="1,is_date,10" maxlength="10"
	                      group="checkSDate,checkEDate"/>
	               <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" "
	                      type="button"/>
	              	 至
	               <input id="checkEDate" class="short_txt" name="checkEDate" value="${now}" datatype="1,is_date,10" maxlength="10"
	                      group="checkSDate,checkEDate"/>
	               <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" "
	                      type="button"/>
	           </td>
	           <td width="10%" align="right">制单状态：</td>
	           <td width="20%" align="left">
	               <script type="text/javascript">
	                   genSelBoxExp("state", <%=Constant.PURCHASE_ORDER_STATE%>, "", true, "short_sel", "", "false", '');
	               </script>
	           </td>
	       </tr>
	       <tr>
	            <td width="10%" align="right">配件编码：</td>
	            <td width="20%"><input name="partOldcode" type="text" class="middle_txt" id="partOldcode"/></td>
	            <td width="10%" align="right">配件件号：</td>
	            <td width="30%"><input name="partCode" type="text" class="long_txt" id="partCode"/></td>
	            <td width="10%" align="right">配件名称：</td>
	            <td width="20%"><input name="partCname" type="text" class="middle_txt" id="partCname"/></td>
	      </tr>
	      <tr>
	            <td width="10%" align="right">保管员：</td>
	            <td width="20%"><input name="whMan" type="text" class="middle_txt" id="whMan"/></td>
	            <td width="10%" align="right"></td>
	            <td width="30%"></td>
	            <td width="10%" align="right"></td>
	            <td width="20%"></td>
	      </tr>
	      <tr>
	    	<td  align="center" colspan="6" >
	    	  <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)"/>
	    	  <input class="normal_btn" type="button" value="导 出" onclick="expPurOrderBalanceExcel()"/>
	    	</td>    
		  </tr>
		</table>
	</div>
	
	<!-- 查询条件 end -->
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->
  </form>
  <script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/report/partReport/partPlanReport/partCtrlHisReport/queryCtrlHisInfos.json";
				
	var title = null;

	var columns = [
			   {header: "序号", align:'center',renderer:getIndex},
	           {header: "领件单号", dataIndex: 'ORDER_CODE',  style: 'text-align:left'},
	           {header: "验收单号", dataIndex: 'CHK_CODE',  align: 'center'},
	           {header: "采购订单号", dataIndex: 'PUR_ORDER_CODE',  style: 'text-align:left'},
	           {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
	           {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
	           {header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
	           {header: "需求数量", dataIndex: 'BUY_QTY', align: 'center'},
	           {header: "领件数量", dataIndex: 'CHECK_QTY', align: 'center'},
	           {header: "验收数量", dataIndex: 'CHECK_QTY_IN', align: 'center'},
	           {header: "计划员", dataIndex: 'BUYER', align: 'center'},
	           {header: "保管员", dataIndex: 'WHMAN', align: 'center'},
	           {header: "室", dataIndex: 'ROOM', align: 'center'},
	           {header: "当前库存", dataIndex: 'ITEM_QTY'},
	           {header: "打印日期", dataIndex: 'PRINT_DATE'},
	           {header: "验收日期", dataIndex: 'CREATE_DATE', align: 'center'},
	           {header: "库房", dataIndex: 'WH_NAME', align: 'center'},
	           {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
	           {header: "制单状态", dataIndex: 'STATE', align: 'center',renderer:getItemValue}
		      ];

	//导出
	function expPurOrderBalanceExcel() {
	    fm.action = "<%=contextPath%>/report/partReport/partPlanReport/partCtrlHisReport/expPartCtrlHisExcel.do";
	    fm.target = "_self";
	    fm.submit();
	}

	function clearInput() {
		//清空选定供应商
		document.getElementById("VENDER_ID").value = '';
		document.getElementById("VENDER_NAME").value = '';
	}
  </script>
</body>
</html>
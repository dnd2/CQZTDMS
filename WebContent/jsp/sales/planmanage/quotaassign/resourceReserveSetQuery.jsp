<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预留资源设定</title>
<script type="text/javascript">

</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 计划管理 > 配额调整 > 预留资源设定</div>
<form method="POST" name="fm" id="fm">
  <table width="85%" border="0" align="center" class="table_query">
    <tr>
      <TD width="35%" align=center nowrap><div align="right">选择配额月度：</div></td>
      <TD width="29%" align="left" nowrap>
	      <select name="year">
		      <option value="${year}">${year}</option>
	      </select>
            年
          <select name="month">
	          <option value="${month}">${month}</option>
          </select>
           月
      </TD>
      <td></td>
    </tr>
    <tr>
      <td align=right nowrap>选择车系：</td>
      <td align=left nowrap>
	      <select name="serie">
	          <c:forEach items="${serieList}" var="po">
			  	<option value="${po.GROUP_ID}">${po.GROUP_NAME}</option>
			  </c:forEach>
      	  </select>
      </td>
      <td width="36%" align=left nowrap><input name="button23" type="button" class="BUTTON" onClick="doQuery();" value="查询"></td>
    </tr>
  </table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
    
    <table width="85%" align="center" class="table_query" id="table1">
	  <tr>
		  <td>
		  	<input type="hidden" name="size">
		  	<input name="button2" type="button" class="button" onClick="confirmSubmit();" value="保存">
		  </td>
	  </tr>
	</table>
</form>
<script type="text/javascript">
	document.getElementById("table1").style.display = "none";
	var HIDDEN_ARRAY_IDS=['table1'];
	
	var i = 0;
	
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/ResourceReserveSet/resourceReserveSetQuery.json";
				
	var title = null;

	var columns = [
				{header: "配置代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "配置名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "生产计划周度", dataIndex: 'PLAN_WEEK', align:'center'},
				{header: "生产计划数量", dataIndex: 'PLAN_AMOUNT', align:'center'},
				{header: "原预留数量", dataIndex: 'LAST_RESERVE_AMT', align:'center'},
				{header: "预留数量", dataIndex: 'GROUP_ID', align:'center' ,renderer:myLink}
		      ];	
	
	function myLink(value,meta,record){
		i++;
		document.getElementById("size").value = i;
		return String.format("<input type='hidden' name='reserveYear"+i+"' value='"+record.data.PLAN_YEAR+"'><input type='hidden' name='reserveMonth"+i+"' value='"+record.data.PLAN_MONTH+"'><input type='hidden' name='reserveWeek"+i+"' value='"+record.data.PLAN_WEEK+"'><input type='hidden' name='groupId"+i+"' value='"+value+"'><input type='hidden' name='planAmount"+i+"' value='"+record.data.PLAN_AMOUNT+"'><input type='text' id='reserveAmt"+i+"' name='reserveAmt"+i+"' datatype='0,is_digit,6' size='3' value='"+record.data.LAST_RESERVE_AMT+"'>");
	}
	
	function confirmSubmit(){
		if(submitForm('fm')){
			var size = document.getElementById("size").value;
			for(var i = 1; i <= size; i++){
				var planAmount = document.getElementById("planAmount" + i).value;
				var reserveAmt = document.getElementById("reserveAmt" + i).value;
				if(parseInt(planAmount, 10) < parseInt(reserveAmt, 10)){
					MyAlert("预留数量不能大于生产计划数量！");
					return false;
				}
			}
			MyConfirm("是否确认保存?",setSubmit);
		}
	}
	
	function setSubmit(){
		i = 0;
		makeNomalFormCall('<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/ResourceReserveSet/resourceReserveSetSave.json',showResult,'fm');
	}
	
	function showResult(json){
		if(json.returnValue == '1'){
			MyAlert("保存成功！");
			__extQuery__(1);
		}else{
			MyAlert("保存失败！");
		}
	}
	
	function doQuery(){
		i = 0;
		__extQuery__(1);
	}
</script>
</body>
</html>

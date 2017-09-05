<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>经销商配额计算</title>
<script type="text/javascript">
<!--
	  function setDisabled(oObj, sFlag) {
			document.getElementById(oObj).disabled = sFlag ;
	  }
	function isDisabled() {
		if(document.getElementById('queryBtn').disabled == 'disabled') {
			return false ;
		}

		return true ;
	}
	function setAllDisF() {
		if(isDisabled) {
			setDisabled('query',false) ;
			setDisabled('download',false) ;
		}
	}
	function setAllDisT() {
		setDisabled('query','disabled') ;
		setDisabled('download','disabled') ;
	}
//-->
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 计划管理 > 配额调整 > 经销商配额计算</div>
<form method="POST" name="fm" id="fm">
  <TABLE class="table_query" width="95%" align="center">
    <TR>
      <TD width="33%" align=center nowrap><div align="right">选择配额月度：</div></td>
      <TD width="19%" align="left" nowrap>
      	<select name="quotaDate">
	      <c:forEach items="${dateList}" var="po">
				<option value="${po.code}">${po.name}</option>
		  </c:forEach>
        </select>
      </TD>
      <TD width="48%" align="left" nowrap>&nbsp;</TD>
    </TR>   
	<TR>
      <TD width="33%" align=center nowrap><div align="right">业务范围：</div></td>
      <TD width="19%" align="left" nowrap>
      	<select name="areaId">
			<c:forEach items="${areaList}" var="po">
				<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
			</c:forEach>
        </select>
      </TD>
      <TD width="48%" align="left" nowrap>&nbsp;</TD>
    </TR>
    <TR>
      <TD width="33%" align=center nowrap><div align="right">计算方法：</div></td>
      <TD width="19%" align="left" nowrap>
      	<script type="text/javascript">
      		genSelBoxExp("curType",<%=Constant.QUOTA_CALCULATE_TYPE%>,"",false,"short_sel","","false",'');
      	</script>
      </TD>
      <TD width="48%" align="left" nowrap>&nbsp;</TD>
    </TR>
    <tr>
      <TD align=center nowrap>&nbsp;</TD>
      <TD align=left nowrap>
	      <input name="button2" id="queryBtn" type=button class="normal_btn" onpropertychange="setAllDisF() ;" onClick="doCalculate();" value="计算">
	      <input name="button" id="query" type=button class="cssbutton" onClick="doQuery();" value="查询">
	      <input name="button4" id="download" type=button class="cssbutton" onClick="doExport();" value="下载">
      </TD>
      <td></td>
    </tr>
  </TABLE>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">
	
	var myPage;
	//查询路径
	var url;
				
	var title = null;

	var columns;	
	
	function doInit(){
		getWeekList();
	}
	
	function getWeekList(){
		var quotaDate = document.getElementById("quotaDate").value;
		var array = quotaDate.split("-");
		var year = array[0];
		var month = array[1];
		var url = "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/MaxQuotaTotalQuery/getWeekList.json";
		makeCall(url,showWeekList,{year:year,month:month});
	}
	
	function showWeekList(json){
		columns = [
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "产品组代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "产品组名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "总量", dataIndex: 'QUOTA_AMT', align:'center'}
		      ];
		for(var i=0;i<json.list.length;i++){
			columns.push({header: json.list[i].SET_WEEK+"周", dataIndex: 'W'+i, align:'center'});
		}
	}
	
	function doCalculate(){
		setAllDisT() ;
		url = "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/DealerQuotaCalculate/dealerQuotaCalculate.json";
		getWeekList();
		__extQuery__(1);
	}
	
	function doQuery(){
		setAllDisT() ;
		url = "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/DealerQuotaCalculate/dealerQuotaCalculateQuery.json";
		getWeekList();
		__extQuery__(1);
	}
	
	function doExport(){
		//var url = "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/DealerQuotaCalculate/toChk.json";
		//makeFormCall(url, exportResult, 'fm') ;
		
		$('fm').action= "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/DealerQuotaCalculate/dealerQuotaCalculateExport.json";
		$('fm').submit();
	}
	
	function exportResult(json) {
		var flag = json.flag ;
		
		if(flag == "error") {
			MyAlert("请在需求预测完成并配额计算后进行下载!") ;
			
			return false ;
		} else {
			$('fm').action= "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/DealerQuotaCalculate/dealerQuotaCalculateExport.json";
			$('fm').submit();
		}
	}
</script>
</body>
</html>

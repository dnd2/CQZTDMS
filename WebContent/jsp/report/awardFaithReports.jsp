<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 授信信息明细</title>
<style type="text/css"> 
.x-grid-cell.user-online 
{ 
background-color: #9fc; 
} 
.x-grid-cell.user-offline 
{ 
background-color: blue; 
} 
</style>
</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>整车销售报表>整车销售报表> 授信信息明细
	</div>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
  <tr class="csstr" align="center">
	  <td align="right">选择经销商：</td>
		<td align="left">
      		<input name="dealerName" type="text" id="dealerName" class="middle_txt" value=""  readonly="readonly"/>
            <input name="dlbtn" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode', '', 'true', '', 'true','','','dealerName');" value="..." />
    		<input type="button" class="normal_btn" onclick="txtClr('dealerCode','dealerName');" value="清 空" id="clrBtn" />
    		<input name="dealerCode" type="hidden" id="dealerCode" class="middle_txt" value="" />
		</td>
	   <td align="right">统计类型：</td> 
	     <td align="left">
		 <select name="STA_TYPE" id="STA_TYPE" class="selectlist" >
				<c:if test="${list!=null}">
					<c:forEach items="${list}" var="list">
						<option value="${list.CODE_ID}">${list.CODE_DESC}</option>
					</c:forEach>
				</c:if>
	  		</select>
		</td>
 </tr>
   <tr class="csstr" align="center">
	 <td align="right" nowrap="true">开票日期：</td>
		<td align="left" nowrap="true">
			<input name="ORDER_STARTDATE" type="text" class="short_time_txt" id="ORDER_STARTDATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'ORDER_STARTDATE', false);" />  	
             &nbsp;至&nbsp;
             <input name="ORDER_ENDDATE" type="text" class="short_time_txt" id="ORDER_ENDDATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'ORDER_ENDDATE', false);" /> 
		</td>
		 <td align="center" colspan="2">
		  <input type="reset" class="cssbutton" id="resetButton"  value="重置"/>
    	  <input type="button" id="queryBtn" class="cssbutton"  value="查询" onclick="_function(1);" />   
    	  <input type="button" id="queryBtn" class="cssbutton"  value="导出" onclick="_function(2);" />   	 	
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
	var url="<%=contextPath%>/report/AwardFaithReports/awardFaithQuery.json?common=1";
	var title = null;
	var columns;
 
	//初始化    
	function doInit(){
		loadcalendar();//日期控件初始化
		firstDay("ORDER_STARTDATE");//当月第一天
		lastDay("ORDER_ENDDATE");//当月最后一天
		 genLocSel('txt1','','');//支持火狐
		
		//__extQuery__(1);
	}
	function _function(_type){
	var staType=document.getElementById("STA_TYPE").value;
		resetParams(staType);
		if(_type==1){
			__extQuery__(1);
		  }
		 if(_type==2){
			    var url="<%=contextPath%>/report/AwardFaithReports/awardFaithQuery.do?common=2";
			  	fm.action=url;  
			   	fm.submit();
		 }
	}
	function resetParams(staType){
		if(staType==<%=Constant.ACCOUNT_TYPE_04%>){//深圳发展银行
			columns = [
						{header: "序号",align:'center',renderer:getIndex},
						{header: "经销商",dataIndex: 'DEALER_NAME',align:'center'},
						{header: "开票日期",dataIndex: 'INVO_DATE',align:'center'},///
						{header: "发票号",dataIndex: 'INVOICE_NO',align:'center'},///
						{header: "车型",dataIndex: 'MODEL_CODE',align:'center'},
						{header: "内部型号",dataIndex: 'ERP_PACKAGE',align:'center'},
						{header: "车架号",dataIndex: 'VIN',align:'center'},
						{header: "颜色",dataIndex: 'COLOR_NAME',align:'center'},
						{header: "提车价格(元)",dataIndex: 'PRICE_COUNT',align:'center'}						
				      ];
		}else if(staType==<%=Constant.ACCOUNT_TYPE_03%>){//中信银行
			columns = [
						{header: "序号",align:'center',renderer:getIndex},
						{header: "经销商",dataIndex: 'DEALER_NAME',align:'center'},
						{header: "开票日期",dataIndex: 'INVO_DATE',align:'center'},///
						{header: "发票号",dataIndex: 'INVOICE_NO',align:'center'},///
						{header: "内部型号",dataIndex: 'ERP_PACKAGE',align:'center'},
						{header: "颜色",dataIndex: 'COLOR_NAME',align:'center'},
						{header: "车型代码",dataIndex: 'MODEL_CODE',align:'center'},
						{header: "车架号",dataIndex: 'VIN',align:'center'},
						{header: "金额(元)",dataIndex: 'PRICE_COUNT',align:'center'}
						//{header: "备注",dataIndex: 'REMARK',align:'center'}
				      ];
		}else{//招商融资
			columns = [
						{header: "序号",align:'center',renderer:getIndex},
						{header: "经销商",dataIndex: 'DEALER_NAME',align:'center'},
						{header: "开票日期",dataIndex: 'INVO_DATE',align:'center'},///
						{header: "发票号",dataIndex: 'INVOICE_NO',align:'center'},///
						{header: "内部型号",dataIndex: 'ERP_PACKAGE',align:'center'},
						{header: "合格证编号",dataIndex: 'HEGEZHENG_CODE',align:'center'},
						{header: "发动机号",dataIndex: 'ENGINE_NO',align:'center'},
						{header: "底盘号",dataIndex: 'VIN',align:'center'},
						{header: "车型",dataIndex: 'MODEL_CODE',align:'center'},
						{header: "单价",dataIndex: 'PRICE_COUNT',align:'center'}
				      ];
		}

	}
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
</script>
</body>
</html>

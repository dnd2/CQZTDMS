<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		   //初始化时间控件
	}
	function toClearDealers(){
		document.getElementById("dealerCode").value="";
		document.getElementById("dealerId").value=""
	}
</script>
<title>车辆验收查询(DLR)</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  库存管理 &gt; 出入库管理 &gt; 车辆验收单查询</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
		<tr>
				<td width="20%" class="tblopt"><div class="right">选择经销商：</div></td>
				<td width="39%" >
      				<input name="dealerCode" type="text" id="dealerCode" class="middle_txt" value="" size="20" />
      				<input type="hidden" name="dealerId" size="15" id="dealerId" value="" />
                  <c:if test="${dutyType==10431001}">
                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer('dealerCode','dealerId','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431002}">
                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer3('dealerCode','dealerId','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431003}">
                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer3('dealerCode','dealerId','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431004}">
                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer6('dealerCode','dealerId','true', '${orgId}')" value="..." />
                  </c:if>   
                  <c:if test="${dutyType==10431005}">
                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer7('dealerCode','dealerId','true', '${orgId}')" value="..." />
                  </c:if>                                    
                    <input type="button" name="button4clearbutton" id="button4clearbutton" class="normal_btn" value="清除" onClick="toClearDealers();"/>
    			</td>
		</tr>
		<tr>
			<td class="tblopt">
			<div class="right">发车日期：</div>
			</td>
			<td>
				<div align="left">
            		<input name="deliverystartDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);" />
            		&nbsp;至&nbsp;
            		<input name="deliveryendDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);" />
            	</div>
			</td>
			<td></td>
		</tr>
<!--		<tr>-->
<!--			<td class="tblopt">-->
<!--			<div class="right">验收日期：</div>-->
<!--			</td>-->
<!--			<td>-->
<!--				<div align="left">-->
<!--            		<input name="checkstartDate" id="t3" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4" hasbtn="true" callFunction="showcalendar(event, 't3', false);" />-->
<!--            		&nbsp;至&nbsp;-->
<!--            		<input name="checkendDate" id="t4" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4" hasbtn="true" callFunction="showcalendar(event, 't4', false);" />-->
<!--            	</div>	-->
<!--			</td>-->
<!--			<td></td>-->
<!--		</tr>-->
		<tr>
		<td width="20%" class="tblopt">
		<div class="right">交接单号：</div>
		</td>
		<td><input type="text" name="dlvNo" id="dlvNo" /></td>
		<td></td>
		</tr>
		<tr>
		<td width="20%" class="tblopt">
		<div class="right">发运申请单号：</div>
		</td>
		<td><input type="text" name="reqNo" id="reqNo" /></td>
		<td></td>
		</tr>
		<tr>
				<td width="20%" class="tblopt"><div class="right">VIN：</div></td>
				<td width="39%" >
      				<textarea name="vin" cols="18" rows="3" ></textarea>
    			</td>
    			<td></td>
		</tr>
		<tr>
				<td width="20%" class="tblopt"><div class="right"></div></td>
				<td>
				</td>
				<td class="table_query_3Col_input" >
					<center><input type="button" class="normal_btn" onclick="detail_query();" value="查 询" id="queryBtn" /> </center>
				</td>
		</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
<script type="text/javascript" >
	var myPage;
	var url;
	var title = null;
	var columns=null;
		      
	function myLink(vehicle_id){
        return String.format(
        		 "<a href=\"<%=contextPath%>/sales/storageManage/CheckBillQuery/checkVehicleQueryDLR_Detail.do?vehicle_id="
                +vehicle_id+"\">[详细]</a>");
    }
	function totalQuery(){

	calculateConfig = {bindTableList:"myTable",totalColumns:"MATERIAL_NAME"};
		url = "<%=contextPath%>/sales/storageManage/CheckVehicleQuery/checkVehicleTotalQueryDLR.json?COMMAND=1";
		columns=[{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "验收数量", dataIndex: 'COUNT', align:'center'}
				];
		__extQuery__(1);
	}
	
	function detail_query(){
		calculateConfig = {};
		 url = "<%=contextPath%>/sales/storageManage/CheckBillQuery/checkBillQuery.json?COMMAND=1";
		 columns = [
				{header: "交接单号", dataIndex: 'SENDCAR_ORDER_NUMBER', align:'center'},
				{header: "收货单位", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "运输公司", dataIndex: 'SHIP_METHOD_CODE', align:'center'},
				{header: "发运申请单号", dataIndex: 'DLVRY_REQ_NO', align:'center'},
				{header: "车架号", dataIndex: 'VIN', align:'center'},
				{header: "发车日期", dataIndex: 'FLATCAR_ASSIGN_DATE', align:'center'},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'VEHICLE_ID',renderer:myLink}
		      ];
		      __extQuery__(1);
	}
 </script>    
</body>
</html>
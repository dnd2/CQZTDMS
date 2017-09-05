<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
List list =(List)request.getAttribute("list_logi");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 车辆出库 </title>
</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理> 车辆出库查询
	</div>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
  <tr class="csstr" align="center">
    <td class="table_info_slabel" >产地：</td>
    <td class="table_info_svalue">
      <select name="YIELDLY" id="YIELDLY" class="u-select">
        <option value="">--请选择--</option>
        <c:if test="${list!=null}">
          <c:forEach items="${list}" var="list">
            <option value="${list.AREA_ID}">${list.AREA_NAME}</option>
          </c:forEach>
        </c:if>
      </select></td>
    <td class="table_info_slabel">组板号：</td>
    <td class="table_info_svalue"><input type="text" maxlength="20"  id=BO_NO name="BO_NO" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" /></td>
    <td class="table_info_slabel">发运申请号：</td>
    <td class="table_info_svalue"><input type="text" maxlength="20"  id="orderNo" name="orderNo" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" /></td>
  </tr>
  <tr>
  	<td class="table_info_slabel">资金类型：</td>
  	<td class="table_info_svalue">
  		<script type="text/javascript">
			genSelBoxExp("fundType",<%=Constant.ACCOUNT_TYPE%>,"",true,"u-select","","false",'');
		</script>
	</td>
    <td class="table_info_slabel">VIN：</td>
    <td class="table_info_svalue"><input type="text" maxlength="20"  id="VIN"： name="VIN" class="middle_txt" size="15" /></td>
    <td class="table_info_slabel">经销商名称：</td>
    <td class="table_info_svalue"><input type="text" maxlength="20"  id="dealerName" name="dealerName" maxlength="30" class="middle_txt" size="15" /></td>
  </tr>
  <tr>
  	<td class="table_info_slabel">组板日期：</td>
    <td class="table_info_svalue" nowrap="true"><input name="BO_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="BO_STARTDATE" readonly="readonly"/>
      <input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'BO_STARTDATE', false);" />
      &nbsp;至&nbsp;
      <input name="BO_ENDDATE" type="text" maxlength="20"  class="middle_txt" id="BO_ENDDATE" readonly="readonly"/>
      <input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'BO_ENDDATE', false);" /></td>   
  	<td class="table_info_slabel">出库日期：</td>
    <td class="table_info_svalue" nowrap="true"><input name="OUT_SDATE" type="text" maxlength="20"  class="middle_txt" id="OUT_SDATE" readonly="readonly"/>
      <input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'OUT_SDATE', false);" />
      &nbsp;至&nbsp;
      <input name="OUT_EDATE" type="text" maxlength="20"  class="middle_txt" id="OUT_EDATE" readonly="readonly"/>
      <input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'OUT_EDATE', false);" /></td>   
  </tr>
  <tr align="center">
    <td colspan="6" class="table_query_4Col_input" style="text-align: center"><input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
      <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" />
      <input type="button" id="outputBtn" class="normal_btn"  value="导出" onclick="outputExcel();" />
      </td>
  </tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
<table border="0" class="table_query" id="tab_remark" style="display: none">
      <tr class="table_info_svalue">
        <td>备注：
          <textarea name="REMARK" id="REMARK" cols="50" rows="2" class="tb_list"></textarea>&nbsp;&nbsp;
          <input class="normal_btn" type="button" value="确定出库" onclick="retreat();"/></td>
      </tr>
  </table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/sales/storage/sendmanage/RemovalStorage/removalStoageVehicleQuery.json";
	var title = null;
	var columns = [
					{header: "序号",align:'center',renderer:getIndex},
					{header: "发运申请号",dataIndex: 'ORDER_NO',align:'center',renderer:getSyt},
					{header: "资金类型",dataIndex: 'FUND_TYPE',align:'center'},
					{header: "经销商",dataIndex: 'DEALER_SHORTNAME',align:'center'},
					{header: "组板号",dataIndex: 'BO_NO',align:'center'},
					{header: "组板完成时间",dataIndex: 'BO_DATE',align:'center'},
					{header: "VIN",dataIndex: 'VIN',align:'center'},
					{header: "车系",dataIndex: 'SERIES_NAME',align:'center'},
					{header: "车型",dataIndex: 'MODEL_NAME',align:'center'},
					{header: "配置",dataIndex: 'PACKAGE_NAME',align:'center'},
					{header: "颜色",dataIndex: 'COLOR_NAME',align:'center'},
					{header: "启票单价",dataIndex: 'DISCOUNT_S_PRICE',align:'center'},
					{header: "物料代码",dataIndex: 'MATERIAL_CODE',align:'center'},
					{header: "车辆状态",dataIndex: 'LIFE_CYCLE',align:'center', renderer: getItemValue},
					{header: "出库时间",dataIndex: 'OUT_DATE',align:'center'}
			      ];
	
	//初始化    
	function doInit(){
		//日期控件初始化
		//__extQuery__(1);
	}
	//处理隐藏备注框
	function customerFunc(){
		var arrayObj = new Array(); 
		arrayObj=document.getElementsByName("groupIds");
		if(arrayObj.length>0){//大于0表表示有数据，备注显示
			document.getElementById("tab_remark").style.display="";
		}else{
			document.getElementById("tab_remark").style.display="none";
		}
	}
	//清空数据
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
    function toChangeMaterial(parm){
		if(parm==1){
			document.getElementById("button1").disabled="";
			document.getElementById("button2").disabled="disabled";
		}else{
			document.getElementById("button1").disabled="disabled";
			document.getElementById("button2").disabled="";
		}
    }

    function getSyt(value,metaData,record){
		var url = '<%=contextPath%>/sales/ordermanage/extractionofvehicle/CarSubmissionOrderQuery/showOrderReport.do?orderId=' + record.data.ORDER_ID;		
		return String.format("<a href=\"javascript:void(0);\" onclick='viewOrderInfo(\""+url+"\")'>"+value+"</a>");
	}
    
  	//显示订单
	function viewOrderInfo(url)
	{
		OpenHtmlWindow(url,1000,450);
	}
    
    function outputExcel(){
    	fm.action="<%=contextPath%>/sales/storage/sendmanage/RemovalStorage/removalStoageVehicleExport.do";
    	fm.submit();
    }
</script>
</body>
</html>

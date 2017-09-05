<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发运计划查询 </title>
</head>

<body onload="doInit();">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>发运计划查询
	</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
<div class="form-body">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
<tr class="csstr" align="center"> 
	<td class="right">承运商：</td> 
	  <td align="left">
		 <select name="LOGI_NAME" id="LOGI_NAME" class="u-select" >
		 	<option value="">-请选择-</option>
				<c:if test="${list_logi!=null}">
					<c:forEach items="${list_logi}" var="list_logi">
						<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
	  </td>
	  <td class="right">发运结算省份：</td>  
		    <td align="left">
	  		<select class="u-select" id="txt1" name="jsProvince" onchange="_genCity(this,'txt2')"></select>
     	 </td>
</tr>
  <tr class="csstr" align="center">  
	  <td class="right">发运方式：</td> 
	  <td align="left">
		   <label>
				<script type="text/javascript">
						genSelBoxExp("TRANSPORT_TYPE",<%=Constant.TT_TRANS_WAY%>,"",true,"u-select","","false",'');
					</script>
			</label>
	  </td>
	  <td class="right">发运结算城市：</td>  
		    <td align="left">
	  		<select class="u-select" id="txt2" name="jsCity" onchange="_genCity(this,'txt3')"></select>
     	 </td> 
</tr>
<tr class="csstr" align="center">  
	<td class="right">组板号：</td> 
	  <td align="left" >
		  <input type="text" maxlength="20" id=BO_NO name="BO_NO" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" />
	  </td>	 
     	 <td class="right">发运结算区县：</td>
   	  <td align="left">
 			<select class="u-select" id="txt3" name="jsCounty"></select>
	 </td>
</tr>
<tr class="csstr" align="center">  
 	<td class="right">已生成交接单：</td>
		<td align="left">
			<label>
				<script type="text/javascript">
						genSelBoxExp("isBill",<%=Constant.IF_TYPE%>,"",true,"u-select","","false",'');
				</script>
			</label>
		</td>
	  <td class="right" nowrap="true">组板日期：</td>
		<td align="left" nowrap="true">
			<input class="short_txt" readonly="readonly"  type="text" id="START_DATE" name="START_DATE" onFocus="WdatePicker({el:$dp.$('START_DATE'), maxDate:'#F{$dp.$D(\'END_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
			<input class="short_txt" readonly="readonly"  type="text" id="END_DATE" name="END_DATE" onFocus="WdatePicker({el:$dp.$('END_DATE'), minDate:'#F{$dp.$D(\'START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
		</td>	
</tr>
<tr align="center">
  <td colspan="6" class="table_query_4Col_input" style="text-align: center">
  		  <input type="hidden" name="pFlag" id="pFlag" value="02"/><!-- 区别于发运计划发送查询 -->
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="doQuery()" />
  		  <input type="reset"  id="resetButton" class="u-button u-reset" value="重置"/>
    	  <input type="button" id="saveButton" class="normal_btn"  value="导出" onclick="downLoad();" />    	  
    	  <input type="button" id="saveButton" class="normal_btn"  value="明细导出" onclick="downLoadDel();" />   	 	
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
	var url = "<%=contextPath%>/sales/storage/sendmanage/DlvPlanManage/DlvPlanManageQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "操作",dataIndex: 'BO_ID',sortable: false, align:'center',renderer:myLink},
				{header: "计划装车日期",dataIndex: 'PLAN_LOAD_DATE',align:'center'},
				{header: "最晚发运日期",dataIndex: 'DLV_FY_DATE',align:'center'},
 				{header: "最晚到货日期",dataIndex: 'DLV_JJ_DATE',align:'center'},
				{header: "组板号",dataIndex: 'BO_NO',align:'center'},
				{header: "发运方式",dataIndex: 'SHIP_NAME',align:'center'},
				{header: "承运商",dataIndex: 'LOGI_NAME',align:'center'},
				{header: "组板量",dataIndex: 'BO_NUM',align:'center'},
				{header: "组板日期",dataIndex: 'BO_DATE',align:'center'},
				{header: "发运结算地",dataIndex: 'BAL_ADDR',align:'center'},
				{header: "已生成交接单",dataIndex: 'HAS_BILL',align:'center'}
		      ];
	//初始化    
	function doInit(){
		genLocSel('txt1','','');//支持火狐
		//__extQuery__(1);
	}
	function doQuery(){
		__extQuery__(1);
	}
	//清空数据
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
    function myLink(value,meta,record){
        var link="<a href='javascript:void(0);' class='u-anchor' onclick='seachSend(\""+value+"\")'>查看</a><a href='javascript:void(0);' class='u-anchor' onclick='printDlvOrder(\""+value+"\")'>打印</a>";
       
  		return String.format(link);
    }
    function seachSend(value,orderId,boNo){
   	 	
   	 	var urlss="<%=contextPath%>/sales/storage/sendmanage/DlvPlanManage/seachInit.do?Id="+value;
    	OpenHtmlWindow(urlss,1000,450);
     }
    //查询结果导出
    function downLoad() 
	{
		document.getElementById('fm').action= "<%=contextPath%>/sales/storage/sendmanage/DlvPlanManage/DlvPlanManageQuery.do?common=1";
		document.getElementById('fm').submit();
	}
    //查询结果明细导出
    function downLoadDel() 
	{
		document.getElementById('fm').action= "<%=contextPath%>/sales/storage/sendmanage/DlvPlanManage/DlvPlanManageQuery.do?common=2";
		document.getElementById('fm').submit();
	}
    //发运单打印
    function printDlvOrder(val){
    	var tarUrl = "<%=contextPath%>/sales/storage/sendmanage/DlvPlanManage/dlvOrderPrint.do?boId="+val;
    	// window.open(tarUrl,'','toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=1200'); 
    	window.open(tarUrl,'','left=0,top=0,width='+screen.availWidth +'- 10,height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
    }
</script>
</body>
</html>

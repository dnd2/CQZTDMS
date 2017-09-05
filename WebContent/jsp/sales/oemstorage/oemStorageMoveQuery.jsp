<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>移库单管理</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
   		loadcalendar();  //初始化时间控件
   		checkQuate() ;
	}
	
	function checkQuate() {
		var flag = "${flag}" ;
		
		if(flag == "false") {
			document.getElementById("button2").style.display = "none" ;
			
			MyAlert("登录用户没有权限进行新增移库操作，请与管理员联系！") ;
		}
	}

</script>
</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;移库单管理</div>
<form method="post" name="fm" id="fm">
<table width="95%" border="0" align="center" class="table_query">
	<tr>
			<td colspan="4">
				<strong>
					<font color="red">
<pre>注意事项:
	状态为"ERP处理完成",且已打印的移库单不能在系统中做取消操作,如有问题请联系相关人员.</pre>
					</font>
				</strong>
			</td>
	</tr>
	<tr>
	  <td align="right" nowrap="nowrap" class="table_query_2Col_label_6Letter">发运日期：</td>
	  <td align="left" nowrap="nowrap" class="table_query_2Col_input">
      	  <div align="left">
      		 <input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
      		 &nbsp;至&nbsp;
      		 <input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
      	  </div>	  
	  </td>
	  <td width="13%" align="right" class="table_query_2Col_label_6Letter">发运仓库：</TD>
	  <td width="35%" class="table_query_2Col_input">
	  		<select name="startName" id="startName" class="short_sel">
	  			<option value="">-请选择-</option>
	  			<c:if test="${list!=null}">
					<c:forEach items="${list}" var="list">
						<option value="${list.WAREHOUSE_ID }">${list.WAREHOUSE_NAME }</option>
					</c:forEach>
				</c:if>
	  		</select>
	  </td>
    </tr>
	<tr>
	  <td align="right" class="table_query_2Col_label_6Letter">移库单状态：</td>
	  <td class="table_query_2Col_input">
	  	  <script type="text/javascript">
 			   genSelBoxExp("stoStatus",<%=Constant.STO_STATUS%>,"",true,"short_sel","","false",'');
		  </script>
	  </td>
	  <td align="right" class="table_query_2Col_label_6Letter">目的仓库：</TD>
     	 <td class="table_query_2Col_input">
	  		<select name="endName" id="endName" class="short_sel">
	  			<option value="">-请选择-</option>
	  			<c:if test="${list_aim!=null}">
					<c:forEach items="${list_aim}" var="list">
						<option value="${list.WAREHOUSE_ID }">${list.WAREHOUSE_NAME }</option>
					</c:forEach>
				</c:if>
	  		</select>
     	 </td>
    </tr>

	<tr>
      <td align="left" class="table_query_2Col_label_6Letter">ERP订单号：</td>
	  <td><input type="text" name=ERP_ORDER_NO id="ERP_ORDER_NO" class="middle_txt" value=""/></td>
	  <td></td>
      <td valign="bottom" class="table_query_2Col_input">
        	<input name="queryBtn" type="button"  class="normal_btn"	onclick="__extQuery__(1)" value="查询" />
        	<input name="button2" type="button"  class="normal_btn"	onclick="addSto()" value="新建" />
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
	var url = "<%=contextPath%>/sales/oemstorage/OemStorageQuery/storageStoQuery.json";
				
	var title = null;

	var columns = [
				{header: "移库单号",dataIndex: 'ORDER_NO', align:'center'},
				{header: "发运仓库", dataIndex: 'FROM_NAME', align:'center'},
				{header: "目的仓库", dataIndex: 'TO_NAME', align:'center'},
				{header: "移库数量", dataIndex: 'AMOUNT', align:'center'},
				{header: "发运数量", dataIndex: 'DLVRY_COUNT', align:'center'},
				{header: "在途数量", dataIndex: 'A_COUNT', align:'center', renderer:vehcleDtl},
				{header: "发运时间", dataIndex: 'STO_DATE', align:'center'},
				{header: "发运状态", dataIndex: 'DLVRY_STATUS', align:'center'},
				{header: "收车入库状态", dataIndex: 'EXIST_STATUS', align:'center'},
				{header: "移库单状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "ERP订单号", dataIndex: 'ERP_ORDER_NO', align:'center'},
				{header: "失败原因", dataIndex: 'ERP_MSG', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'STO_ID',renderer:myLink ,align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//修改的超链接
	function myLink(value,meta,record)
	{
		var val = record.data.STATUS;
		var flag = record.data.PRINT_FLAG;
		var orderNo = record.data.ORDER_NO ;
		var erpNo = record.data.ERP_ORDER_NO ;
		if(val != <%=Constant.STO_STATUS_04 %> && !flag)
		{
			return String.format("<a href='#' onclick='selInfo(\""+ value +"\")'>[查看明细]</a>&nbsp;<a href='#' onclick='stoGOBACK(\""+ value +"\", \"" + val + "\", \"" + orderNo + "\", \"" + erpNo + "\")'>[取消]</a>");
		}
		else
		{
			return String.format("<a href='#' onclick='selInfo(\""+ value +"\")'>[查看明细]</a>");
		}
  		
	}
	
	function vehcleDtl(value,meta,record) {
		if(parseInt(value) != 0) {
			var stoId = record.data.STO_ID ;
			
			return String.format("<a href=\"#\" onclick='getVehcleDtl(\"" + stoId + "\")'>" + value + "</a>") ;
		} else {
			return String.format(value) ;
		}	
	}
	
	function getVehcleDtl(value) {
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/oemstorage/OemStorageQuery/getVhcl.do?&stoId='+value,850,500);
	}
	
	//详细
	function selInfo(value)
	{
		OpenHtmlWindow('<%=contextPath%>/sales/oemstorage/OemStorageQuery/selInfoView.do?id='+value,800,500);
	}
	
	//跳转新增页面
	function addSto()
	{
		$('fm').action= "<%=contextPath%>/sales/oemstorage/OemStorageQuery/addMoveSto.do";
		$('fm').submit();
	}
	
	//申报提醒
	function stoGOBACK(value, val, orderNo, erpNo)
	{	
		var tip = "" ;
		
		if(val == <%=Constant.STO_STATUS_01 %>) {
			tip = "移库单号：" + orderNo + "</br>状态：ERP处理中<br/>" ;
		} else if(val == <%=Constant.STO_STATUS_02 %>) {
			tip = "移库单号：" + orderNo + "</br>状态：ERP处理完成<br/>若移库车辆已处理完成，则需要通知erp重新传入车辆数据到dms。<br>" ;
		} else if(val == <%=Constant.STO_STATUS_03 %>) {
			tip = "移库单号：" + orderNo + "</br>状态：ERP处理失败<br/>" ;
		}
		MyConfirm(tip + "<font color='red'>1、请确认该移库单是否能够取消；</br>2、取消前需要通知erp取消该移库单；</br>3、取消后该移库单不可恢复。</font></br>是否确认取消？",putForword, [value]);
	}
	
	//提报申请
	function putForword(value)
	{
		makeNomalFormCall("<%=contextPath%>/sales/oemstorage/OemStorageQuery/stoGoBack.json?id="+value,showForwordValue,'fm','queryBtn'); 
	}
	
	//提报回调函数
	function showForwordValue(json)
	{
		if(json.returnValue == '1')
		{
			MyAlert("取消成功！");
			__extQuery__(1);
		}else
		{
			MyAlert("取消失败！请联系系统管理员！");
		}
	}
	
//设置超链接 end
	
</script>
<!--页面列表 end -->
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>


<%@ page import="java.util.*"%>

<jsp:include page="${path}/common/jsp_head_new.jsp" />
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>调拨订单申请</title>
<script type="text/javascript">
	
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
</script>
</head>
<body onunload="javascript:destoryPrototype()" onload="">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>调拨单申请</div>
	<form method="post" name="fm" id="fm">
	<div class="form-panel">
		<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>调拨单申请</h2>
	<div class="form-body">
			<table class="table_query">
				<tr>
					<td class="right" width="15%">发运仓库：</td>
					<td>
					  <select name="sendWareId" id="sendWareId" class="u-select" >
					 	<option value="">--请选择--</option>
							<c:if test="${list!=null}">
								<c:forEach items="${list}" var="list">
									<option value="${list.WAREHOUSE_ID}">${list.WAREHOUSE_NAME}</option>
								</c:forEach>
							</c:if>
				  		</select>
					</td>
					<td class="right" width="15%">调拨单号：</td>
					<td>
						<input name="dispNo" id="dispNo" type="text" maxlength="20"  class="middle_txt">
					</td>
				</tr>
				<tr>
					<td class="right" width="15%">收货仓库：</td>
					<td>
					  <select name="receiveWareId" id="receiveWareId" class="u-select" >
					 	<option value="">--请选择--</option>
							<c:if test="${list!=null}">
								<c:forEach items="${list}" var="list">
									<option value="${list.WAREHOUSE_ID}">${list.WAREHOUSE_NAME}</option>
								</c:forEach>
							</c:if>
				  		</select>
					</td>
					<td class="right" width="15%">最晚到货日期：</td>
					<td>
						<input class="short_txt" readonly="readonly"  type="text" id="lastStartdate" name="lastStartdate" onFocus="WdatePicker({el:$dp.$('lastStartdate'), maxDate:'#F{$dp.$D(\'lastEndDate\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
						<input class="short_txt" readonly="readonly"  type="text" id="lastEndDate" name="lastEndDate" onFocus="WdatePicker({el:$dp.$('lastEndDate'), minDate:'#F{$dp.$D(\'lastStartdate\')}'})"  style="cursor: pointer;width: 80px;"/>
					</td>
				</tr>
				<tr>
					<td class="right" width="15%">发运方式：</td>
					<td>
						<script type="text/javascript">
							genSelBoxExp("transType",<%=Constant.TT_TRANS_WAY%>,"-1",true,"u-select",'',"false",'');
						</script>
					</td>
					<td class="right" width="15%">提交日期：</td>
					<td>
						<input class="short_txt" readonly="readonly"  type="text" id="subStartdate" name="subStartdate" onFocus="WdatePicker({el:$dp.$('subStartdate'), maxDate:'#F{$dp.$D(\'subEndDate\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
						<input class="short_txt" readonly="readonly"  type="text" id="subEndDate" name="subEndDate" onFocus="WdatePicker({el:$dp.$('subEndDate'), minDate:'#F{$dp.$D(\'subStartdate\')}'})"  style="cursor: pointer;width: 80px;"/>
					</td>
				</tr>
				<tr>
					<td class="right" width="15%">状态：</td>
					<td>
						<script type="text/javascript">
							genSelBoxExp("orderStatus",<%=Constant.DB_ORDER_STATUS %>,"-1",true,"u-select",'',"false",'');
						</script>
					</td>
					<td></td>
					<td>
					</td>
				</tr>
				<tr>
					<td colspan="4" class="table_query_4Col_input" style="text-align: center">
						<input type="button" value="查询" id="queryBtn" class="u-button u-query" onclick="__extQuery__(1);"/>&nbsp;
						<input type="button" value="新增" class="u-button u-submit" onclick="addInit()"/>
					</td>
				</tr>
			</table>
			</div>
		</div>
		<!-- 分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<!-- 分页 end -->
	</form>

	<script>
		var myPage;
	
		var url = "<%=request.getContextPath()%>/sales/storage/sendmanage/DispatchOrderManage/queryDisPatchOrder.json";
	
		var title = null;
	
		//设置列名属性
		var columns = [
				{header: "序号", dataIndex: '', align:'center', renderer:getIndex},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'DISP_ID',renderer:myLink},
				{
					header: "调拨单号", dataIndex: 'DISP_NO', align:'center', 
					renderer: function(value, metaData, record) {
						var url = '<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/showOrderReport.do?orderId=' + record.data.DISP_ID;
						return "<a href='javascript:;' onclick='viewOrderInfo(\""+url+"\")'>"+value+"</a>";
					}
				},
				{header: "发运仓库", dataIndex: 'SEND_WAREHOUSE', align:'center'},
				 {header: "收货仓库", dataIndex: 'RECEIVE_WAREHOUSE', align:'center'},
				 {header: "最晚到货日期", dataIndex: 'PLAN_DELIVER_DATE', align:'center'},
				 {header: "发运方式", dataIndex: 'SEND_WAY_TXT', align:'center'},
				 {header: "提交日期", dataIndex: 'SUB_DATE', align:'center'},
				 {header: "已提报数量", dataIndex: 'SUB_NUM', align:'center'},
				 {header: "已分派数量", dataIndex: 'ASS_NUM', align:'center'},
				 {header: "已组板数量", dataIndex: 'BO_NUM', align:'center'},
				 {header: "已交接数量", dataIndex: 'REP_NUM', align:'center'},
				 {header: "在途数量", dataIndex: 'IN_WAY_NUM', align:'center'},
				 {header: "验收数量", dataIndex: 'ACC_NUM', align:'center'},
				 {header: "订单状态", dataIndex: 'ORDER_STATUS', align:'center', renderer:getItemValue}
		];
		
		function viewOrderInfo(url)
		{
			OpenHtmlWindow(url,1000,450);
		}
		//跳转新增页面
		function addInit()
		{
			fm.action = "<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/addDispatchOrderInit.do";
			fm.submit();
		}
		//设置超链接
		function myLink(value,meta,record)
		{
			if(record.data.ORDER_STATUS=='12141001'){//已保存时才能进行操作
				return String.format("<a href='javascript:void(0);' class='u-anchor' onclick='applyDo(\""+value+"\")'>申请</a><a href='javascript:void(0);' class='u-anchor' onclick='sel(\""+value+"\")'>修改</a><a href='javascript:void(0);' class='u-anchor' onclick='del(\""+value+"\")'>删除</a>");
			}else{
				return "";
			}
		}
		//详细页面
		function sel(value)
		{
		 	window.location.href='<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/toEditDispOrder.do?dispId='+value;  
		}
		//删除
		function del(value){
			MyConfirm("确定删除？",delInfo,[value]);
		}
		function delInfo(value){
			var url='<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/delDispatchOrder.json?dispId='+value;
			makeNomalFormCall(url,delBack,'fm','queryBtn'); 
		}
		function delBack(json)
		{
			if(json.returnValue == 1)
			{
				parent.MyAlert("删除成功！");
				fm.action = "<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/orderApplyInit.do";
				fm.submit();
				__extQuery__(1);
			}else{
				MyAlert("操作失败！请联系系统管理员！");
			}
		}
		//申请
		function applyDo(value){
			MyConfirm("确定申请？",applyDoInfo,[value]);
		}
		function applyDoInfo(value){
			var url='<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/applyAction.json?dispId='+value;
			makeNomalFormCall(url,applyDoBack,'fm','queryBtn'); 
		}
		function applyDoBack(json)
		{
			if(json.returnValue == 1)
			{
				parent.MyAlert("申请成功！");
				fm.action = "<%=contextPath%>/sales/storage/sendmanage/DispatchOrderManage/orderApplyInit.do";
				fm.submit();
				__extQuery__(1);
			}else if(json.returnValue == 3){
				MyAlert("可用库存数不足！");
			}else{
				MyAlert("操作失败！请联系系统管理员！");
			}
		}
	</script>
</body>
</html>



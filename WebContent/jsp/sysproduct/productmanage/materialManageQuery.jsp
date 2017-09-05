<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<% 
	String curPage = request.getAttribute("curPage") != null ? request.getAttribute("curPage").toString() : "1";
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<title>物料维护</title>
	<script type="text/javascript">
		var myPage;
		//查询路径
		var url = "<%=request.getContextPath()%>/sysproduct/productmanage/MaterialManage/materialManageQuery.json";;
		var title = null;
		var columns = [
				{header: "序号", align:'center', renderer:getIndex},
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', style:'text-align:left'},
				//{header: "装配状态代码", dataIndex: 'ERP_NAME', align: 'center'} ,
				{header: "状态", dataIndex: 'STATUS', align:'center', renderer:getItemValue},
				{header: "可提报订单", dataIndex: 'ORDER_FLAG', align: 'center'},
				{header: "是否可生产", dataIndex: 'PROCUCT_FLAG', align: 'center'},
				//{header: "是否出口", dataIndex: 'EXPORT_SALES_FLAG', align: 'center',renderer:getItemValue},
				//{header: "物料类型", dataIndex: 'MAT_TYPE', align: 'center',renderer:getItemValue},
				//{header: "是否内销", dataIndex: 'IS_INSALE', align: 'center', renderer:getItemValue},
				{id:'action', header: "操作" ,dataIndex: 'MATERIAL_ID', renderer:myLink}
	      ];	
				      
		function myLink(value){
	        var url22="<%=request.getContextPath()%>/sysproduct/productmanage/MaterialManage/materialManageModPre.do?materialId="+value;
	        return String.format("<a href=\"javascript:doOpenWithCurPage('"+url22+"')\">[维护]</a>");
	    }
	
	    function doOpenWithCurPage(url) {
	    	var curPage = myPage == null ? '1' : myPage.page; 
	    	url += "&curPage="+curPage;
	    	//OpenHtmlWindow(url,800,600);
	    	window.location.href = url;
	    }
		
		function clrTxt(txtId){
	    	document.getElementById(txtId).value="";
	    }
	   	function queryttt()
	   	{
	   		var curPage = myPage == null ? '<%=curPage %>' : myPage.page; 
	   		__extQuery__(curPage);
	   	}
		// 页面跳转到新增物料的页面
	   	function addNewMat() {
	   		var curPage = myPage == null ? '<%=curPage %>' : myPage.page; 
	   		window.location.href='<%=request.getContextPath()%>/sysproduct/productmanage/MaterialManage/materialManageAddPre.do?curPage=' + curPage;
	    }
	
	   	function saveSetting() {
			var orderSet = document.getElementById('orderSet').value;
			var procuctSet = document.getElementById('procuctSet').value;
			var status_up = document.getElementById('status_up').value;
			var matType = document.getElementById('matType').value;
			var mids = document.getElementsByName("mid");
			var mid = '';
	
			for(var i=0;i<mids.length;i++) {
				if(mids[i].checked == true) {
					mid += mids[i] + ',';
				}
			}
	
			if(mid.length == 0) {
				MyAlert('请选择您要操作的物料!'); return;
			}
	
			if(orderSet != '' || procuctSet != '' || status_up!=''|| matType!='') { 
				MyConfirm('确认保存您的设置？', function(){
					var subUrl = '<%=request.getContextPath()%>/sysproduct/productmanage/MaterialManage/materialManageSet.json';
					makeNomalFormCall(subUrl, function(json){
						if(json.Exception) {
							MyAlert(json.Exception.message);
						} else {
							MyAlert(json.message);
							__extQuery__(myPage.page);
						}
					}, 'fm');
				});
			} else {
				MyAlert('您还未设置状态!');
			}
	   	}
	</script>
</head>
<body onload="queryttt()">
	<div class="wbox">
		<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 物料维护</div>
		<form method="POST" name="fm" id="fm">
			<div class="form-panel">
				<h2>物料维护</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">物料代码：</td>
							<td>
								<input name="materialCode" maxlength="30" value="" datatype="1,is_noquotation,30" id="materialCode" type="text" class="middle_txt" />
							</td>
							<td class="right">物料名称：</td>
							<td>
								<input name="materialName" maxlength="30" value="" datatype="1,is_noquotation,30" id="materialName" type="text" class="middle_txt" />
							</td>
							<td class="right">装配状态代码：</td>
							<td>
								<input name="ERPName" maxlength="50" value="" datatype="1,is_noquotation,50" id="materialCode" type="text" class="middle_txt" />
							</td>
						</tr>
						<tr> 
						<td class="right">物料状态：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("status",<%=Constant.STATUS%>,"",true,"","","false",''); 
								</script>
							</td>
							<td class="right">所属物料组：</td>
							<td>
								<input type="text" name="groupCode" class="middle_txt" id="groupCode" onclick="showMaterialGroup('groupCode','','false','4','true')"/>
								<input class="u-button" type="button" value="清空" onclick="clrTxt('groupCode');"/>
							</td>
							<td class="right">物料类型：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("mtseach",<%=Constant.MAT_TYPE%>,"",true,"","","false",'');
								</script>
							</td>
						</tr>
						<tr>
							<td class="right">可提报订单：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("orderFlag",<%=Constant.NASTY_ORDER_REPORT_TYPE %>,"",true,"","","false",'');
								</script>
							</td>
							<td class="right">是否可生产：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("procuctFlag",<%=Constant.FORECAST_FLAG_REPORT_PRO %>,"",true,"","","false",'');
								</script>
							</td>
							<td class="right">是否出口</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("is_export",<%=Constant.IF_TYPE%>,"",true,"","","false",'');
								</script>
							</td>
						</tr>
						<tr>
							<td colspan="6" class="table_query_4Col_input" style="text-align: center">
								<input name="queryBtn" type="button" class="u-button u-query" onclick="queryttt()" value="查 询" id="queryBtn" /> &nbsp; 
								<input type="button" class="u-button" onclick="reset();" value="重 置"/> &nbsp; 
								<input name="button2" type="button" class="u-button u-submit" onclick="addNewMat()" value="新 增"/>
							</td>
						</tr>
					</table>
					<!-- <table class="table_query" style="border: 1px #f0f0f0 solid; margin-top: 2px;">
						<tr>
							<td class="right">提报状态设置：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("orderSet","<%=Constant.NASTY_ORDER_REPORT_TYPE %>","-1",true,"","","false",'<%=Constant.NASTY_ORDER_REPORT_TYPE %>');
								</script>
							</td>
							<td class="right">可生产设置：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("procuctSet",<%=Constant.FORECAST_FLAG_REPORT_PRO %>,"-1",true,"","","false",'');
								</script>
							</td>
							<td class="right">物料类型：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("matType",<%=Constant.MAT_TYPE%>,"",true,"","","false",'');
								</script>
							</td>
							<td class="right">物料状态设置：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("status_up",<%=Constant.STATUS%>,"",true,"","","false",''); 
								</script>
								<font color='red'>* 如果不需要设置，请设置"请选择"</font>
							</td>
							<td>
								<input type="button" class="u-button" value="保 存" onclick="saveSetting()"/>
							</td>
						</tr>
					</table> -->
				</div>
			</div>
		  
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		    <!--分页 end -->
		</form>
	</div>

</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>计划员与仓库维护</title>
<script language="javascript" type="text/javascript">
	$(function(){__extQuery__(1);});
	
	var myPage;

	var url = "<%=contextPath%>/parts/baseManager/partPlannerWarehouseManager/partPlannerWarehouseAction/partPlannerWarehouseSearch.json";
	
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'SEQUENCE_ID', renderer:getIndex,align:'center'},
                {id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'},
				{header: "计划员", dataIndex: 'NAME', style: 'text-align: center;'},
				{header: "仓库名称", dataIndex: 'WH_NAME', style: 'text-align: center;'},
				{header: "是否有效", dataIndex: 'STATE', align:'center',renderer:getItemValue}

		      ];
	     
	
	//设置超链接
	function myLink(value,meta,record)
	{
		var relationId = record.data.RELATION_ID
		var plannerID = record.data.PLANER_ID;
		var plannerName = record.data.NAME;
		var whId = record.data.WH_ID;
		var parms = (plannerID + "@@" + plannerName).toString();
		var state = record.data.STATE;
		var disableValue = <%=Constant.STATUS_DISABLE%>;
		if(disableValue == state){
			return String.format("<a href=\"#\" onclick='changeState(\""+relationId+"\", 2)'>[有效]</a>");
        } else {
        	return String.format("<a href=\"#\" onclick='changeState(\""+relationId+"\", 1)'>[失效]</a>&nbsp;<a href=\"#\" onclick='formod(\""+plannerID+"\",\""+plannerName+"\")'>[维护]</a>");
		}
  		
	}

	 //修改记录状态
    function changeState(parms, type) {
		 var msg = "";
		 if(type == 1){
			 msg = "确认失效该数据?";
		 }else{
			 msg = "确认有效该数据?";
		 }
		 MyConfirm(msg, validAfterConfirm, [parms, type]);
    }
	
	 // 确认后
    function validAfterConfirm(parms, type){
		btnDisable();
     	var url = '<%=contextPath%>/parts/baseManager/partPlannerWarehouseManager/partPlannerWarehouseAction/';
		if(type == 1){
			// 设置失效
			url += 'celPartPlannerWarehouse.json?disabeParms='+parms;
		}else{
			// 设置有效
			url += 'enablePartPlannerWarehouse.json?enableParms='+parms;
		}
		url += '&curPage='+myPage.page;
  		makeNomalFormCall(url,showResult,'fm');
    }

    // 修改数据后返回
    function showResult(json) {
    	btnEnable();
        if (json.errorExist != null && json.errorExist.length > 0) {
            MyAlert("配件编码：【" + json.errorExist + "】替代件系统中已创建，不能重复创建!");
        } else if (json.success != null && json.success == "true") {
//         	MyAlert("操作成功！");
        	layer.msg("操作成功！", {icon: 1});
        	__extQuery__(json.curPage);          
        } else {
            MyAlert("操作失败，请联系管理员！");
        }
    }

    //维护
    function formod(plannerID,plannerName)
    {
    	btnDisable();
    	document.getElementById("plannerID").value = plannerID;
    	document.getElementById("plannerName").value = plannerName;
    	document.fm.action = "<%=contextPath%>/parts/baseManager/partPlannerWarehouseManager/partPlannerWarehouseAction/partPlannerWarehouseFormodInit.do";
		document.fm.target="_self";
		document.fm.submit();
    }

  	//新增
	function relationAdd(){
		btnDisable();
		window.location.href ="<%=contextPath%>/parts/baseManager/partPlannerWarehouseManager/partPlannerWarehouseAction/partPlannerWarehouseAddInit.do";
	}

</script>
</head>
<body>
	<div class="wbox">
		<form method="post" name="fm" id="fm">
			<input type="hidden" name="plannerID" id="plannerID" value="" />
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 计划员与仓库维护
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">计划员：</td>
							<td>
								<input class="middle_txt" type="text" name="plannerName" id="plannerName" />
							</td>
							<td class="right">仓库名称：</td>
							<td>
								<input class="middle_txt" type="text" name="WHName" id="WHName" />
							</td>
							<td class="right">是否有效：</td>
							<td>
								<script type="text/javascript">
					   	 			genSelBox("STATE",<%=Constant.STATUS%>,"<%=Constant.STATUS_ENABLE%>", true, "", "");
								</script>
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
								<input class="u-button" type="button" value="重 置" onclick="reset()" />
								<input class="u-button" type="button" value="新 增" onclick="relationAdd()" />
							</td>
						</tr>
					</table>
				</div>
			</div>

			<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
		</form>
	</div>
</body>
</html>
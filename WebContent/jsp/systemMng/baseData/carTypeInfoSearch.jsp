<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>销售意向</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置：
	系统管理 &gt; 基础数据维护 &gt; 车型信息查询
</div>
<form id="fm">
<table class="table_query" border="0">
		<tr>
		<td class="table_query_3Col_label_3Letter" nowrap="nowrap">厂家：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
			<input type="hidden" name="brand_id" id="brand_id" value="">
			<input class="middle_txt"  type="text" name="brand" id="brand" readonly="readonly"/>&nbsp;
			<input class="mark_btn" type="button" id="showbrandbtn" value="&hellip;" onclick="showBrandSeries('<%=contextPath %>')"/>
		</td>
		<td class="table_query_3Col_label_3Letter" nowrap="nowrap">车系：</td>
		<td class="table_query_3Col_input" nowrap="nowrap" >
			<input type="hidden" name="series_id" id="series_id" value="" />
			<input class="middle_txt"  type="text" name="series" id="series" readonly="readonly" value="" />&nbsp;
			</td>
		<td class="table_query_3Col_label_4Letter" nowrap="nowrap">车辆类型：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
		 <select class="min_sel" name="VHCL_TYPE"  id="VHCL_TYPE">
							<option value="">-请选择-</option>
		  </select>
		</td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_3Letter" nowrap="nowrap">排量：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
			<select class="min_sel" name="DSPM" id="DSPM">
							<option value="">-请选择-</option>
			</select>
		</td>
		<td class="table_query_3Col_label_3etter" nowrap="nowrap">变速箱：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
			<select class="min_sel" name="GEAR_BOX" id="GEAR_BOX">
							<option value="">-请选择-</option>
			</select>
		</td>
		<td class="table_query_3Col_label_4Letter" nowrap="nowrap">年款：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
				<select class="min_sel" name="MODEL_YEAR" id="MODEL_YEAR">
							<option value="">-请选择-</option>
			    </select>
		</td>
	</tr>
	<tr>
		<td class="table_query_input" nowrap="nowrap" colspan="7" align="center">
			<input class="normal_btn" type="button" value="查 询" id="queryBtn" onclick="__extQuery__(1);"/>
		</td>		
	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
</body>
</html>
<script type="text/javascript" >
		var url = "<%=contextPath%>/sysmng/sysData/CarTypeQuery/queryCarInfo.json?COMMAND=1";
	//设置表格标题
	//var title = '<img class="nav" src="<%=contextPath%>/grid/subNav.gif" />&nbsp;'+ '意向列表';
	var title= null;
	//设置列名属性
	var columns = [
					{header: "序号", width:'5%', renderer:getIndex}, //设置序号的方式
					{header: "厂家",width:'12%',orderCol:"PROD_AREA", dataIndex: 'prodArea'},
					{header: "车系",width:'8%',dataIndex: 'seriesId'},
					{header: "车辆类型", width:'8%', dataIndex: 'vhclType'},
					{header: "排量", width:'6%', orderCol:"dspm", dataIndex: 'dspm'},
					{header: "变速箱", width:'10%',dataIndex: 'gearBox'},
					{header: "公告代码", orderCol:"NOTICE_CODE",width:'12%', dataIndex: 'noticeCode'},
					{header: "车型",  width:'30%', dataIndex: 'modelName'},
					{header: "年款",  width:'8%', dataIndex: 'modelYear'}
				  ];
	$("series_id").onpropertychange = function(){
			showSeriesPros();
		}
		/*
		* ============================================下拉框联动====================================================
		*	 ============================================BEGIN========================================
		*/

//根据车系的选择动态定义出 车辆类型 排量 变速箱 年款
			function showSeriesPros() {
				var series_id = $F("series_id");
				if(series_id == "") return false;
				makeCall('<%=request.getContextPath()%>/common/SaleIntentMng/queryProsBySeries.json',showpros,{SERIES_ID:series_id});
				}
			//addOption 方法在 InfoAjax.js 中定义
			//根据得到的数据来构造下拉框
			function showpros(json){
				//构造车辆类型下拉框
				var typeval = json.VHCL_TYPE_LIST;
				var typeobj = $("VHCL_TYPE");
				typeobj.options.length=1;
				for(var i=0;i<typeval.length;i++){
					addOption(typeobj,typeval[i].showValue,typeval[i].showValue);
				}
				//构造排量下拉框
				var dspmval = json.DSPM_LIST;
				var dspmobj = $("DSPM");
				dspmobj.options.length=1;
				for(var i=0;i<dspmval.length;i++){
					addOption(dspmobj,dspmval[i].showValue,dspmval[i].showValue);
				}
				//构造变速箱下拉框
				var gearboxval = json.GEAR_BOX_LIST;
				var gearboxobj = $("GEAR_BOX");
				gearboxobj.options.length=1;
				for(var i=0;i<gearboxval.length;i++){
					addOption(gearboxobj,gearboxval[i].showValue,gearboxval[i].showValue);
				}
				//构造车型年下拉框
				var modelyearval = json.MODEL_YEAR_LIST;
				var modelyearobj = $("MODEL_YEAR");
				modelyearobj.options.length=1;
				for(var i=0;i<modelyearval.length;i++){
					addOption(modelyearobj,modelyearval[i].showValue,modelyearval[i].showValue);
				}
			}
		/*
		* ============================================下拉框联动=======================================
		*	 ============================================END=========================================================
		*/	

</script>

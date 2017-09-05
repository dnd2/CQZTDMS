<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="sun.text.normalizer.ReplaceableString"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<% 
	String contextPath = request.getContextPath(); 
	List boardList = (List)request.getAttribute("boardList");	
	List boardListDay = (List)request.getAttribute("boardListDay");
	
	List newmonrep1list = (List)request.getAttribute("newmonrep1list");
	List newmonrep2list = (List)request.getAttribute("newmonrep2list");
	List newmonrep3list = (List)request.getAttribute("newmonrep3list");
	List newmonrep4list = (List)request.getAttribute("newmonrep4list");
	List newmonrep5list = (List)request.getAttribute("newmonrep5list");
	List newmonrep6list = (List)request.getAttribute("newmonrep6list");
	List newmonrep7list = (List)request.getAttribute("newmonrep7list");
	
	List newmonrep8list = (List)request.getAttribute("newmonrep8list");
	List newmonrep9list = (List)request.getAttribute("newmonrep9list");
	List newmonrep10list = (List)request.getAttribute("newmonrep10list");
	List newmonrep11list = (List)request.getAttribute("newmonrep11list");
	List newmonrep12list = (List)request.getAttribute("newmonrep12list");
	
	String kg = " ";
	
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>月度销售报表</title>
<script type="text/javascript">
	function doInit(){
		sum_no();
		sum_jidi();
	}

	function chkForm() {
		$('fm').action="<%=contextPath%>/sales/ordermanage/orderquery/MonReport/MonReportInitPara.do";		
		$('fm').submit();
	}
	
	function displaySwitch() {
		var queryConditionTable = document.getElementById('queryConditionTable');
		if(queryConditionTable.style.display == "none") {
			queryConditionTable.style.display = "inline";
			document.getElementById('controlDiv').firstChild.innerHTML = "隐藏查询";
		} else {
			queryConditionTable.style.display = "none";
			document.getElementById('controlDiv').firstChild.innerHTML = "显示查询";
		}
	}
	
</script>
</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;
	<strong>${Mon_name}月度销售报表</strong>
</div>

<div id="controlDiv" style="float: right;" align="right"><a href="javascript:void(0)" style="text-decoration: none;" onclick="displaySwitch()">显示查询</a></div>

<form method="post" name="fm" id="fm">

<table id="queryConditionTable" class="table_query" align="center"  style="display: none;">

	 <tr>
    	<td colspan="4">&nbsp;</td>
    </tr>
	
	<tr>
	    <td>&nbsp;</td>
	    <td align="left" nowrap="nowrap" width="10%">	    	
	    </td>
		<td width="30%" align="left" nowrap="nowrap">
			<label>月份:</label>
			<select name="monname" id="monname" class="short_sel" style="width:150px;">
				<c:forEach items="${monlist}" var="list">
					<option value="${list.MON_NAME}" <c:if test="${list.MON_NAME == MON_NAME}">selected="selected"</c:if>>
						<c:out value="${list.MON_NAME}"/>
					</option>
				</c:forEach>
			</select><input type="hidden" name="area" id="area"/>
		</td>
		<td>&nbsp;</td>
    </tr> 
    
    <tr>
    	<td colspan="4">&nbsp;</td>
    </tr>
    
    <tr>
    	<td colspan="4" align="center"><input type="button" class="cssbutton" name="submitBtn" value="查询" onclick="chkForm()"/></td>
    </tr>
    
    <tr>
    	<td colspan="4">&nbsp;</td>
    </tr>
  
    
</table>

<br/>

<table border="0" align="center" class="table_list" id="activeTable1">
	<tr class="table_list_th_report">
	    <th rowspan="2"><font style="font-size: 17px">车型名称</font></th>
		<th height="25px" colspan="3"><font style="font-size: 17px">年初库存</font></th>
		<th height="25px" colspan="3"><font style="font-size: 17px">本月计划</font></th>
		<th height="25px" colspan="3"><font style="font-size: 17px">本月完成</font></th>
		<th height="25px" colspan="3"><font style="font-size: 17px">完成月度计划(%)</font></th>
		<th height="25px" colspan="2"><font style="font-size: 17px">月同比</font></th>
		<th height="25px" colspan="2"><font style="font-size: 17px">月环比</font></th>
		<th height="25px" colspan="2"><font style="font-size: 17px">年累目标情况(事业部内部)</font></th>
		<th height="25px" colspan="2"><font style="font-size: 17px">年累目标情况(战略规划部)</font></th>		
		<th height="25px" colspan="2"><font style="font-size: 17px">年累计完成情况</font></th>		
		<th height="25px" colspan="2"><font style="font-size: 17px">年累完成率情况(事业部内部)</font></th>
		<th height="25px" colspan="2"><font style="font-size: 17px">年累完成率情况(战略规划部)</font></th>
		<th height="25px" colspan="2"><font style="font-size: 17px">年同比</font></th>
		<th height="25px" colspan="3"><font style="font-size: 17px">库存</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">月度库销比</font></th>
		<th height="25px" colspan="3"><font style="font-size: 17px">库存较年初变化</font></th>
	</tr>
	<tr class="table_list_th_report">
		<th height="25px">企业库存</th>
		<th>社会库存</th>
		<th>库存合计</th>
		<th>生产</th>
		<th>启票</th>
		<th>实销</th>
		<th>生产</th>
		<th>启票</th>
		<th>实销</th>
		<th>生产</th>
		<th>启票</th>
		<th>实销</th>
		<th>启票</th>
		<th>实销</th>
		<th>启票</th>
		<th>实销</th>
		<th>启票</th>
		<th>实销</th>
		<th>启票</th>
		<th>实销</th>
		<th>启票</th>
		<th>实销</th>
		<th>启票</th>
		<th>实销</th>
		<th>启票</th>
		<th>实销</th>
		<th>启票</th>
		<th>实销</th>
		<th>企业库存</th>
		<th>社会库存</th>
		<th>库存合计</th>
		<th>企业库存</th>
		<th>社会库存</th>
		<th>库存合计</th>		
	</tr>
	
		<%
			if(null != newmonrep1list && newmonrep1list.size()>0){
				for(int i=0;i<newmonrep1list.size();i++){
				Map map = (Map)newmonrep1list.get(i);
		%>
		<tr class="table_list_row1" height="25px">
		    <td><%=map.get("CAR_TYEP")==null?kg:map.get("CAR_TYEP")%></td>
		    <td><%=map.get("QC_QYKC")==null?kg:map.get("QC_QYKC")%></td>
		    <td><%=map.get("QC_SHKC")==null?kg:map.get("QC_SHKC")%></td>
		    <td><%=map.get("QC_KCHJ")==null?kg:map.get("QC_KCHJ")%></td>
		    <td><%=map.get("PLAN_SC")==null?kg:map.get("PLAN_SC")%></td>
		    
		    <td><%=map.get("PLAN_QP")==null?kg:map.get("PLAN_QP")%></td>
		    <td><%=map.get("PLAN_SX")==null?kg:map.get("PLAN_SX")%></td>
		    <td><%=map.get("COM_SC")==null?kg:map.get("COM_SC")%></td>
		    <td><%=map.get("COM_QP")==null?kg:map.get("COM_QP")%></td>
		    <td><%=map.get("COM_SX")==null?kg:map.get("COM_SX")%></td>
		    
		    <td><%=map.get("BL_SC")==null?kg:map.get("BL_SC")%></td>
		    <td><%=map.get("BL_QP")==null?kg:map.get("BL_QP")%></td>
		    <td><%=map.get("BL_SX")==null?kg:map.get("BL_SX")%></td>
		    <td><%=map.get("M_TB_QP")==null?kg:map.get("M_TB_QP")%></td>
		    <td><%=map.get("M_TB_SX")==null?kg:map.get("M_TB_SX")%></td>
		    
		    
		    <td><%=map.get("M_HB_QP")==null?kg:map.get("M_HB_QP")%></td>
		    <td><%=map.get("M_HB_SX")==null?kg:map.get("M_HB_SX")%></td>
		    <td><%=map.get("Y_PLAN_QP1")==null?kg:map.get("Y_PLAN_QP1")%></td>
		    <td><%=map.get("Y_PLAN_SX1")==null?kg:map.get("Y_PLAN_SX1")%></td>
		    <td><%=map.get("Y_PLAN_QP2")==null?kg:map.get("Y_PLAN_QP2")%></td>
		    
		    <td><%=map.get("Y_PLAN_SX2")==null?kg:map.get("Y_PLAN_SX2")%></td>
		    <td><%=map.get("Y_COM_QP")==null?kg:map.get("Y_COM_QP")%></td>
		    <td><%=map.get("Y_COM_SX")==null?kg:map.get("Y_COM_SX")%></td>
		    <td><%=map.get("Y_BL_QP1")==null?kg:map.get("Y_BL_QP1")%></td>
		    <td><%=map.get("Y_BL_SX1")==null?kg:map.get("Y_BL_SX1")%></td>
		    
		    <td><%=map.get("Y_BL_QP2")==null?kg:map.get("Y_BL_QP2")%></td>
		    <td><%=map.get("Y_BL_SX2")==null?kg:map.get("Y_BL_SX2")%></td>
		    <td><%=map.get("Y_TB_QP")==null?kg:map.get("Y_TB_QP")%></td>
		    <td><%=map.get("Y_TB_SX")==null?kg:map.get("Y_TB_SX")%></td>
		    <td><%=map.get("QM_QYKC")==null?kg:map.get("QM_QYKC")%></td>
		    
		    <td><%=map.get("QM_SHKC")==null?kg:map.get("QM_SHKC")%></td>
		    <td><%=map.get("QM_KCHJ")==null?kg:map.get("QM_KCHJ")%></td>
		    <td><%=map.get("BL_KC_XS")==null?kg:map.get("BL_KC_XS")%></td>
		    <td><%=map.get("KC_BH_QYKC")==null?kg:map.get("KC_BH_QYKC")%></td>
		    <td><%=map.get("KC_BH_SHKC")==null?kg:map.get("KC_BH_SHKC")%></td>
		    
		    <td><%=map.get("KC_BH_KCHJ")==null?kg:map.get("KC_BH_KCHJ")%></td>		   
		   
		</tr>
			<%
				}
			}
		%>
			
</table>

<table>
<tr>
<th height="25px" ><font style="font-size: 17px"></font></th>
</tr>
</table>

<table border="0" align="center" class="table_list" id="activeTable2">
	<tr class="table_list_th_report">
	    <th rowspan="1"><font style="font-size: 17px">启票</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">01月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">02月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">03月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">04月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">05月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">06月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">07月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">08月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">09月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">10月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">11月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">12月</font></th>
	</tr>
		
		<%
			if(null != newmonrep2list && newmonrep2list.size()>0){
				for(int i=0;i<newmonrep2list.size();i++){
				Map map = (Map)newmonrep2list.get(i);
		%>
		<tr class="table_list_row1" height="25px">
		<td><%=map.get("ITEM_TYPE")==null?kg:map.get("ITEM_TYPE")%></td>
        <td><%=map.get("MON1")==null?kg:map.get("MON1")%></td>
        <td><%=map.get("MON2")==null?kg:map.get("MON2")%></td>
        <td><%=map.get("MON3")==null?kg:map.get("MON3")%></td>
        <td><%=map.get("MON4")==null?kg:map.get("MON4")%></td>
        
        <td><%=map.get("MON5")==null?kg:map.get("MON5")%></td>
        <td><%=map.get("MON6")==null?kg:map.get("MON6")%></td>
        <td><%=map.get("MON7")==null?kg:map.get("MON7")%></td>
        <td><%=map.get("MON8")==null?kg:map.get("MON8")%></td>
        <td><%=map.get("MON9")==null?kg:map.get("MON9")%></td>
        
        <td><%=map.get("MON10")==null?kg:map.get("MON10")%></td>
        <td><%=map.get("MON11")==null?kg:map.get("MON11")%></td>
        <td><%=map.get("MON12")==null?kg:map.get("MON12")%></td>
		    	   	   		   
		</tr>
			<%
				}
			}
		%>
			
</table>

<table>
<tr>
<th height="25px" ><font style="font-size: 17px"></font></th>
</tr>
</table>

<table border="0" align="center" class="table_list" id="activeTable3">
	<tr class="table_list_th_report">
	    <th rowspan="1"><font style="font-size: 17px">零售</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">01月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">02月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">03月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">04月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">05月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">06月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">07月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">08月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">09月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">10月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">11月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">12月</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">本月单店销量</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">年初至今单店销量</font></th>
		<th height="25px" colspan="1"><font style="font-size: 17px">渠道数量</font></th>
	</tr>
		
		<%
			if(null != newmonrep3list && newmonrep3list.size()>0){
				for(int i=0;i<newmonrep3list.size();i++){
				Map map = (Map)newmonrep3list.get(i);
		%>
		<tr class="table_list_row1" height="25px">
		<td><%=map.get("ITEM_TYPE")==null?kg:map.get("ITEM_TYPE")%></td>
        <td><%=map.get("MON1")==null?kg:map.get("MON1")%></td>
        <td><%=map.get("MON2")==null?kg:map.get("MON2")%></td>
        <td><%=map.get("MON3")==null?kg:map.get("MON3")%></td>
        <td><%=map.get("MON4")==null?kg:map.get("MON4")%></td>
        
        <td><%=map.get("MON5")==null?kg:map.get("MON5")%></td>
        <td><%=map.get("MON6")==null?kg:map.get("MON6")%></td>
        <td><%=map.get("MON7")==null?kg:map.get("MON7")%></td>
        <td><%=map.get("MON8")==null?kg:map.get("MON8")%></td>
        <td><%=map.get("MON9")==null?kg:map.get("MON9")%></td>
        
        <td><%=map.get("MON10")==null?kg:map.get("MON10")%></td>
        <td><%=map.get("MON11")==null?kg:map.get("MON11")%></td>
        <td><%=map.get("MON12")==null?kg:map.get("MON12")%></td>
        
        <td><%=map.get("DDXL")==null?kg:map.get("DDXL")%></td>
        <td><%=map.get("LJXL")==null?kg:map.get("LJXL")%></td>
        <td><%=map.get("QDSL")==null?kg:map.get("QDSL")%></td>
		    	   	   		   
		</tr>
			<%
				}
			}
		%>
			
</table>


<table>
<tr>
<th height="25px" ><font style="font-size: 17px"></font></th>
</tr>
</table>


<table border="0" align="center" class="table_list" id="activeTable4">
	<tr class="table_list_th_report">
	    <th rowspan="2"><font style="font-size: 17px">A网</font></th>
		<th height="25px" colspan="6"><font style="font-size: 17px">启票</font></th>
		<th height="25px" colspan="6"><font style="font-size: 17px">实销</font></th>
		<th height="25px" colspan="3"><font style="font-size: 17px">库存(在库+在途+审核未打单)</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">本月库销比</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">本月单店销量</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">年初至今单店销量</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">渠道数量</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">同盟体</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">二级网点</font></th>
	</tr>
	<tr class="table_list_th_report">
		<th height="25px">月目标</th>
		<th>月累计</th>
		<th>完成进度</th>
		<th>年目标</th>
		<th>年完成</th>
		<th>年完成率</th>
		<th>月目标</th>
		<th>月累计</th>
		<th>完成进度</th>
		<th>年目标</th>
		<th>年完成</th>
		<th>年完成率</th>
		<th>当月末</th>
		<th>年初数</th>
		<th>变化</th>				
	</tr>
	
		<%
			if(null != newmonrep4list && newmonrep4list.size()>0){
				for(int i=0;i<newmonrep4list.size();i++){
				Map map = (Map)newmonrep4list.get(i);
		%>
		<tr class="table_list_row1" height="25px">
		    <td><%=map.get("AREA_NAME")==null?kg:map.get("AREA_NAME")%></td>
		    <td><%=map.get("QP_MON_MB")==null?kg:map.get("QP_MON_MB")%></td>
		    <td><%=map.get("QP_MON_LJ")==null?kg:map.get("QP_MON_LJ")%></td>
		    <td><%=map.get("QP_MON_JD")==null?kg:map.get("QP_MON_JD")%></td>
		    <td><%=map.get("QP_YEAR_MB")==null?kg:map.get("QP_YEAR_MB")%></td>
		    
		    <td><%=map.get("QP_YEAR_LJ")==null?kg:map.get("QP_YEAR_LJ")%></td>
		    <td><%=map.get("QP_YEAR_JD")==null?kg:map.get("QP_YEAR_JD")%></td>
		    <td><%=map.get("SX_MON_MB")==null?kg:map.get("SX_MON_MB")%></td>
		    <td><%=map.get("SX_MON_LJ")==null?kg:map.get("SX_MON_LJ")%></td>
		    <td><%=map.get("SX_MON_JD")==null?kg:map.get("SX_MON_JD")%></td>
		    
		    <td><%=map.get("SX_YEAR_MB")==null?kg:map.get("SX_YEAR_MB")%></td>
		    <td><%=map.get("SX_YEAR_LJ")==null?kg:map.get("SX_YEAR_LJ")%></td>
		    <td><%=map.get("SX_YEAR_JD")==null?kg:map.get("SX_YEAR_JD")%></td>
		    <td><%=map.get("KC_MON_QM")==null?kg:map.get("KC_MON_QM")%></td>
		    <td><%=map.get("KC_YEAR_QC")==null?kg:map.get("KC_YEAR_QC")%></td>
		    
		    
		    <td><%=map.get("KC_BH")==null?kg:map.get("KC_BH")%></td>
		    <td><%=map.get("MON_KXB")==null?kg:map.get("MON_KXB")%></td>
		    <td><%=map.get("DDXL")==null?kg:map.get("DDXL")%></td>
		    <td><%=map.get("LJXL")==null?kg:map.get("LJXL")%></td>
		    <td><%=map.get("QDSL")==null?kg:map.get("QDSL")%></td>
		    
		    <td><%=map.get("TMTSL")==null?kg:map.get("TMTSL")%></td>
		    <td><%=map.get("EJDSL")==null?kg:map.get("EJDSL")%></td>		   
		   
		</tr>
			<%
				}
			}
		%>
			
</table>

<table>
<tr>
<th height="25px" ><font style="font-size: 17px"></font></th>
</tr>
</table>


<table border="0" align="center" class="table_list" id="activeTable5">
	<tr class="table_list_th_report">
	    <th rowspan="2"><font style="font-size: 17px">B网</font></th>
		<th height="25px" colspan="6"><font style="font-size: 17px">启票</font></th>
		<th height="25px" colspan="6"><font style="font-size: 17px">实销</font></th>
		<th height="25px" colspan="3"><font style="font-size: 17px">库存(在库+在途+审核未打单)</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">本月库销比</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">本月单店销量</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">年初至今单店销量</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">渠道数量</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">同盟体</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">二级网点</font></th>
	</tr>
	<tr class="table_list_th_report">
		<th height="25px">月目标</th>
		<th>月累计</th>
		<th>完成进度</th>
		<th>年目标</th>
		<th>年完成</th>
		<th>年完成率</th>
		<th>月目标</th>
		<th>月累计</th>
		<th>完成进度</th>
		<th>年目标</th>
		<th>年完成</th>
		<th>年完成率</th>
		<th>当月末</th>
		<th>年初数</th>
		<th>变化</th>				
	</tr>
	
		<%
			if(null != newmonrep5list && newmonrep5list.size()>0){
				for(int i=0;i<newmonrep5list.size();i++){
				Map map = (Map)newmonrep5list.get(i);
		%>
		<tr class="table_list_row1" height="25px">
		    <td><%=map.get("AREA_NAME")==null?kg:map.get("AREA_NAME")%></td>
		    <td><%=map.get("QP_MON_MB")==null?kg:map.get("QP_MON_MB")%></td>
		    <td><%=map.get("QP_MON_LJ")==null?kg:map.get("QP_MON_LJ")%></td>
		    <td><%=map.get("QP_MON_JD")==null?kg:map.get("QP_MON_JD")%></td>
		    <td><%=map.get("QP_YEAR_MB")==null?kg:map.get("QP_YEAR_MB")%></td>
		    
		    <td><%=map.get("QP_YEAR_LJ")==null?kg:map.get("QP_YEAR_LJ")%></td>
		    <td><%=map.get("QP_YEAR_JD")==null?kg:map.get("QP_YEAR_JD")%></td>
		    <td><%=map.get("SX_MON_MB")==null?kg:map.get("SX_MON_MB")%></td>
		    <td><%=map.get("SX_MON_LJ")==null?kg:map.get("SX_MON_LJ")%></td>
		    <td><%=map.get("SX_MON_JD")==null?kg:map.get("SX_MON_JD")%></td>
		    
		    <td><%=map.get("SX_YEAR_MB")==null?kg:map.get("SX_YEAR_MB")%></td>
		    <td><%=map.get("SX_YEAR_LJ")==null?kg:map.get("SX_YEAR_LJ")%></td>
		    <td><%=map.get("SX_YEAR_JD")==null?kg:map.get("SX_YEAR_JD")%></td>
		    <td><%=map.get("KC_MON_QM")==null?kg:map.get("KC_MON_QM")%></td>
		    <td><%=map.get("KC_YEAR_QC")==null?kg:map.get("KC_YEAR_QC")%></td>
		    
		    
		    <td><%=map.get("KC_BH")==null?kg:map.get("KC_BH")%></td>
		    <td><%=map.get("MON_KXB")==null?kg:map.get("MON_KXB")%></td>
		    <td><%=map.get("DDXL")==null?kg:map.get("DDXL")%></td>
		    <td><%=map.get("LJXL")==null?kg:map.get("LJXL")%></td>
		    <td><%=map.get("QDSL")==null?kg:map.get("QDSL")%></td>
		    
		    <td><%=map.get("TMTSL")==null?kg:map.get("TMTSL")%></td>
		    <td><%=map.get("EJDSL")==null?kg:map.get("EJDSL")%></td>		   
		   
		</tr>
			<%
				}
			}
		%>
			
</table>

<table>
<tr>
<th height="25px" ><font style="font-size: 17px"></font></th>
</tr>
</table>


<table border="0" align="center" class="table_list" id="activeTable6">
	<tr class="table_list_th_report">
	    <th rowspan="2"><font style="font-size: 17px">华南特区</font></th>
		<th height="25px" colspan="6"><font style="font-size: 17px">启票</font></th>
		<th height="25px" colspan="6"><font style="font-size: 17px">实销</font></th>
		<th height="25px" colspan="3"><font style="font-size: 17px">库存(在库+在途+审核未打单)</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">本月库销比</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">本月单店销量</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">年初至今单店销量</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">渠道数量</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">同盟体</font></th>
		<th height="25px" rowspan="2"><font style="font-size: 17px">二级网点</font></th>
	</tr>
	<tr class="table_list_th_report">
		<th height="25px">月目标</th>
		<th>月累计</th>
		<th>完成进度</th>
		<th>年目标</th>
		<th>年完成</th>
		<th>年完成率</th>
		<th>月目标</th>
		<th>月累计</th>
		<th>完成进度</th>
		<th>年目标</th>
		<th>年完成</th>
		<th>年完成率</th>
		<th>当月末</th>
		<th>年初数</th>
		<th>变化</th>				
	</tr>
	
		<%
			if(null != newmonrep6list && newmonrep6list.size()>0){
				for(int i=0;i<newmonrep6list.size();i++){
				Map map = (Map)newmonrep6list.get(i);
		%>
		<tr class="table_list_row1" height="25px">
		    <td><%=map.get("AREA_NAME")==null?kg:map.get("AREA_NAME")%></td>
		    <td><%=map.get("QP_MON_MB")==null?kg:map.get("QP_MON_MB")%></td>
		    <td><%=map.get("QP_MON_LJ")==null?kg:map.get("QP_MON_LJ")%></td>
		    <td><%=map.get("QP_MON_JD")==null?kg:map.get("QP_MON_JD")%></td>
		    <td><%=map.get("QP_YEAR_MB")==null?kg:map.get("QP_YEAR_MB")%></td>
		    
		    <td><%=map.get("QP_YEAR_LJ")==null?kg:map.get("QP_YEAR_LJ")%></td>
		    <td><%=map.get("QP_YEAR_JD")==null?kg:map.get("QP_YEAR_JD")%></td>
		    <td><%=map.get("SX_MON_MB")==null?kg:map.get("SX_MON_MB")%></td>
		    <td><%=map.get("SX_MON_LJ")==null?kg:map.get("SX_MON_LJ")%></td>
		    <td><%=map.get("SX_MON_JD")==null?kg:map.get("SX_MON_JD")%></td>
		    
		    <td><%=map.get("SX_YEAR_MB")==null?kg:map.get("SX_YEAR_MB")%></td>
		    <td><%=map.get("SX_YEAR_LJ")==null?kg:map.get("SX_YEAR_LJ")%></td>
		    <td><%=map.get("SX_YEAR_JD")==null?kg:map.get("SX_YEAR_JD")%></td>
		    <td><%=map.get("KC_MON_QM")==null?kg:map.get("KC_MON_QM")%></td>
		    <td><%=map.get("KC_YEAR_QC")==null?kg:map.get("KC_YEAR_QC")%></td>
		    
		    
		    <td><%=map.get("KC_BH")==null?kg:map.get("KC_BH")%></td>
		    <td><%=map.get("MON_KXB")==null?kg:map.get("MON_KXB")%></td>
		    <td><%=map.get("DDXL")==null?kg:map.get("DDXL")%></td>
		    <td><%=map.get("LJXL")==null?kg:map.get("LJXL")%></td>
		    <td><%=map.get("QDSL")==null?kg:map.get("QDSL")%></td>
		    
		    <td><%=map.get("TMTSL")==null?kg:map.get("TMTSL")%></td>
		    <td><%=map.get("EJDSL")==null?kg:map.get("EJDSL")%></td>		   
		   
		</tr>
			<%
				}
			}
		%>
			
</table>

<table>
<tr>
<th height="25px" ><font style="font-size: 17px"></font></th>
</tr>
</table>


<table border="0" align="center" class="table_list" id="activeTable7">
	<tr class="table_list_th_report">
	    <th rowspan="2"><font style="font-size: 17px">A网</font></th>
		<th height="25px" colspan="13"><font style="font-size: 17px">启票</font></th>		
	</tr>
	<tr class="table_list_th_report">
		<th height="25px">01月</th>
		<th>02月</th>
		<th>03月</th>
		<th>04月</th>
		<th>05月</th>
		<th>06月</th>
		<th>07月</th>
		<th>08月</th>
		<th>09月</th>
		<th>10月</th>
		<th>11月</th>
		<th>12月</th>
		<th>累计</th>					
	</tr>
	
		<%
			if(null != newmonrep7list && newmonrep7list.size()>0){
				for(int i=0;i<newmonrep7list.size();i++){
				Map map = (Map)newmonrep7list.get(i);
		%>
		<tr class="table_list_row1" height="25px">
		    <td><%=map.get("AREA_NAME")==null?kg:map.get("AREA_NAME")%></td>
		    <td><%=map.get("MON1")==null?kg:map.get("MON1")%></td>
		    <td><%=map.get("MON2")==null?kg:map.get("MON2")%></td>
		    <td><%=map.get("MON3")==null?kg:map.get("MON3")%></td>
		    <td><%=map.get("MON4")==null?kg:map.get("MON4")%></td>
		    
		    <td><%=map.get("MON5")==null?kg:map.get("MON5")%></td>
		    <td><%=map.get("MON6")==null?kg:map.get("MON6")%></td>
		    <td><%=map.get("MON7")==null?kg:map.get("MON7")%></td>
		    <td><%=map.get("MON8")==null?kg:map.get("MON8")%></td>
		    <td><%=map.get("MON9")==null?kg:map.get("MON9")%></td>
		    
		    <td><%=map.get("MON10")==null?kg:map.get("MON10")%></td>
		    <td><%=map.get("MON11")==null?kg:map.get("MON11")%></td>
		    <td><%=map.get("MON12")==null?kg:map.get("MON12")%></td>
		    <td><%=map.get("LJ")==null?kg:map.get("LJ")%></td>		    	   
		   
		</tr>
			<%
				}
			}
		%>
			
</table>

<table>
<tr>
<th height="25px" ><font style="font-size: 17px"></font></th>
</tr>
</table>


<table border="0" align="center" class="table_list" id="activeTable8">
	<tr class="table_list_th_report">
	    <th rowspan="2"><font style="font-size: 17px">B网</font></th>
		<th height="25px" colspan="13"><font style="font-size: 17px">启票</font></th>		
	</tr>
	<tr class="table_list_th_report">
		<th height="25px">01月</th>
		<th>02月</th>
		<th>03月</th>
		<th>04月</th>
		<th>05月</th>
		<th>06月</th>
		<th>07月</th>
		<th>08月</th>
		<th>09月</th>
		<th>10月</th>
		<th>11月</th>
		<th>12月</th>
		<th>累计</th>					
	</tr>
	
		<%
			if(null != newmonrep8list && newmonrep8list.size()>0){
				for(int i=0;i<newmonrep8list.size();i++){
				Map map = (Map)newmonrep8list.get(i);
		%>
		<tr class="table_list_row1" height="25px">
		    <td><%=map.get("AREA_NAME")==null?kg:map.get("AREA_NAME")%></td>
		    <td><%=map.get("MON1")==null?kg:map.get("MON1")%></td>
		    <td><%=map.get("MON2")==null?kg:map.get("MON2")%></td>
		    <td><%=map.get("MON3")==null?kg:map.get("MON3")%></td>
		    <td><%=map.get("MON4")==null?kg:map.get("MON4")%></td>
		    
		    <td><%=map.get("MON5")==null?kg:map.get("MON5")%></td>
		    <td><%=map.get("MON6")==null?kg:map.get("MON6")%></td>
		    <td><%=map.get("MON7")==null?kg:map.get("MON7")%></td>
		    <td><%=map.get("MON8")==null?kg:map.get("MON8")%></td>
		    <td><%=map.get("MON9")==null?kg:map.get("MON9")%></td>
		    
		    <td><%=map.get("MON10")==null?kg:map.get("MON10")%></td>
		    <td><%=map.get("MON11")==null?kg:map.get("MON11")%></td>
		    <td><%=map.get("MON12")==null?kg:map.get("MON12")%></td>
		    <td><%=map.get("LJ")==null?kg:map.get("LJ")%></td>		    	   
		   
		</tr>
			<%
				}
			}
		%>
			
</table>

<table>
<tr>
<th height="25px" ><font style="font-size: 17px"></font></th>
</tr>
</table>


<table border="0" align="center" class="table_list" id="activeTable9">
	<tr class="table_list_th_report">
	    <th rowspan="2"><font style="font-size: 17px">华南特区</font></th>
		<th height="25px" colspan="13"><font style="font-size: 17px">启票</font></th>		
	</tr>
	<tr class="table_list_th_report">
		<th height="25px">01月</th>
		<th>02月</th>
		<th>03月</th>
		<th>04月</th>
		<th>05月</th>
		<th>06月</th>
		<th>07月</th>
		<th>08月</th>
		<th>09月</th>
		<th>10月</th>
		<th>11月</th>
		<th>12月</th>
		<th>累计</th>					
	</tr>
	
		<%
			if(null != newmonrep9list && newmonrep9list.size()>0){
				for(int i=0;i<newmonrep9list.size();i++){
				Map map = (Map)newmonrep9list.get(i);
		%>
		<tr class="table_list_row1" height="25px">
		    <td><%=map.get("AREA_NAME")==null?kg:map.get("AREA_NAME")%></td>
		    <td><%=map.get("MON1")==null?kg:map.get("MON1")%></td>
		    <td><%=map.get("MON2")==null?kg:map.get("MON2")%></td>
		    <td><%=map.get("MON3")==null?kg:map.get("MON3")%></td>
		    <td><%=map.get("MON4")==null?kg:map.get("MON4")%></td>
		    
		    <td><%=map.get("MON5")==null?kg:map.get("MON5")%></td>
		    <td><%=map.get("MON6")==null?kg:map.get("MON6")%></td>
		    <td><%=map.get("MON7")==null?kg:map.get("MON7")%></td>
		    <td><%=map.get("MON8")==null?kg:map.get("MON8")%></td>
		    <td><%=map.get("MON9")==null?kg:map.get("MON9")%></td>
		    
		    <td><%=map.get("MON10")==null?kg:map.get("MON10")%></td>
		    <td><%=map.get("MON11")==null?kg:map.get("MON11")%></td>
		    <td><%=map.get("MON12")==null?kg:map.get("MON12")%></td>
		    <td><%=map.get("LJ")==null?kg:map.get("LJ")%></td>		    	   
		   
		</tr>
			<%
				}
			}
		%>
			
</table>

<table>
<tr>
<th height="25px" ><font style="font-size: 17px"></font></th>
</tr>
</table>


<table border="0" align="center" class="table_list" id="activeTable10">
	<tr class="table_list_th_report">
	    <th rowspan="2"><font style="font-size: 17px">A网</font></th>
		<th height="25px" colspan="13"><font style="font-size: 17px">实销</font></th>		
	</tr>
	<tr class="table_list_th_report">
		<th height="25px">01月</th>
		<th>02月</th>
		<th>03月</th>
		<th>04月</th>
		<th>05月</th>
		<th>06月</th>
		<th>07月</th>
		<th>08月</th>
		<th>09月</th>
		<th>10月</th>
		<th>11月</th>
		<th>12月</th>
		<th>累计</th>					
	</tr>
	
		<%
			if(null != newmonrep10list && newmonrep10list.size()>0){
				for(int i=0;i<newmonrep10list.size();i++){
				Map map = (Map)newmonrep10list.get(i);
		%>
		<tr class="table_list_row1" height="25px">
		    <td><%=map.get("AREA_NAME")==null?kg:map.get("AREA_NAME")%></td>
		    <td><%=map.get("MON1")==null?kg:map.get("MON1")%></td>
		    <td><%=map.get("MON2")==null?kg:map.get("MON2")%></td>
		    <td><%=map.get("MON3")==null?kg:map.get("MON3")%></td>
		    <td><%=map.get("MON4")==null?kg:map.get("MON4")%></td>
		    
		    <td><%=map.get("MON5")==null?kg:map.get("MON5")%></td>
		    <td><%=map.get("MON6")==null?kg:map.get("MON6")%></td>
		    <td><%=map.get("MON7")==null?kg:map.get("MON7")%></td>
		    <td><%=map.get("MON8")==null?kg:map.get("MON8")%></td>
		    <td><%=map.get("MON9")==null?kg:map.get("MON9")%></td>
		    
		    <td><%=map.get("MON10")==null?kg:map.get("MON10")%></td>
		    <td><%=map.get("MON11")==null?kg:map.get("MON11")%></td>
		    <td><%=map.get("MON12")==null?kg:map.get("MON12")%></td>
		    <td><%=map.get("LJ")==null?kg:map.get("LJ")%></td>		    	   
		   
		</tr>
			<%
				}
			}
		%>
			
</table>

<table>
<tr>
<th height="25px" ><font style="font-size: 17px"></font></th>
</tr>
</table>


<table border="0" align="center" class="table_list" id="activeTable11">
	<tr class="table_list_th_report">
	    <th rowspan="2"><font style="font-size: 17px">B网</font></th>
		<th height="25px" colspan="13"><font style="font-size: 17px">实销</font></th>		
	</tr>
	<tr class="table_list_th_report">
		<th height="25px">01月</th>
		<th>02月</th>
		<th>03月</th>
		<th>04月</th>
		<th>05月</th>
		<th>06月</th>
		<th>07月</th>
		<th>08月</th>
		<th>09月</th>
		<th>10月</th>
		<th>11月</th>
		<th>12月</th>
		<th>累计</th>					
	</tr>
	
		<%
			if(null != newmonrep11list && newmonrep11list.size()>0){
				for(int i=0;i<newmonrep11list.size();i++){
				Map map = (Map)newmonrep11list.get(i);
		%>
		<tr class="table_list_row1" height="25px">
		    <td><%=map.get("AREA_NAME")==null?kg:map.get("AREA_NAME")%></td>
		    <td><%=map.get("MON1")==null?kg:map.get("MON1")%></td>
		    <td><%=map.get("MON2")==null?kg:map.get("MON2")%></td>
		    <td><%=map.get("MON3")==null?kg:map.get("MON3")%></td>
		    <td><%=map.get("MON4")==null?kg:map.get("MON4")%></td>
		    
		    <td><%=map.get("MON5")==null?kg:map.get("MON5")%></td>
		    <td><%=map.get("MON6")==null?kg:map.get("MON6")%></td>
		    <td><%=map.get("MON7")==null?kg:map.get("MON7")%></td>
		    <td><%=map.get("MON8")==null?kg:map.get("MON8")%></td>
		    <td><%=map.get("MON9")==null?kg:map.get("MON9")%></td>
		    
		    <td><%=map.get("MON10")==null?kg:map.get("MON10")%></td>
		    <td><%=map.get("MON11")==null?kg:map.get("MON11")%></td>
		    <td><%=map.get("MON12")==null?kg:map.get("MON12")%></td>
		    <td><%=map.get("LJ")==null?kg:map.get("LJ")%></td>		    	   
		   
		</tr>
			<%
				}
			}
		%>
			
</table>

<table>
<tr>
<th height="25px" ><font style="font-size: 17px"></font></th>
</tr>
</table>


<table border="0" align="center" class="table_list" id="activeTable12">
	<tr class="table_list_th_report">
	    <th rowspan="2"><font style="font-size: 17px">华南特区</font></th>
		<th height="25px" colspan="13"><font style="font-size: 17px">实销</font></th>		
	</tr>
	<tr class="table_list_th_report">
		<th height="25px">01月</th>
		<th>02月</th>
		<th>03月</th>
		<th>04月</th>
		<th>05月</th>
		<th>06月</th>
		<th>07月</th>
		<th>08月</th>
		<th>09月</th>
		<th>10月</th>
		<th>11月</th>
		<th>12月</th>
		<th>累计</th>					
	</tr>
	
		<%
			if(null != newmonrep12list && newmonrep12list.size()>0){
				for(int i=0;i<newmonrep12list.size();i++){
				Map map = (Map)newmonrep12list.get(i);
		%>
		<tr class="table_list_row1" height="25px">
		    <td><%=map.get("AREA_NAME")==null?kg:map.get("AREA_NAME")%></td>
		    <td><%=map.get("MON1")==null?kg:map.get("MON1")%></td>
		    <td><%=map.get("MON2")==null?kg:map.get("MON2")%></td>
		    <td><%=map.get("MON3")==null?kg:map.get("MON3")%></td>
		    <td><%=map.get("MON4")==null?kg:map.get("MON4")%></td>
		    
		    <td><%=map.get("MON5")==null?kg:map.get("MON5")%></td>
		    <td><%=map.get("MON6")==null?kg:map.get("MON6")%></td>
		    <td><%=map.get("MON7")==null?kg:map.get("MON7")%></td>
		    <td><%=map.get("MON8")==null?kg:map.get("MON8")%></td>
		    <td><%=map.get("MON9")==null?kg:map.get("MON9")%></td>
		    
		    <td><%=map.get("MON10")==null?kg:map.get("MON10")%></td>
		    <td><%=map.get("MON11")==null?kg:map.get("MON11")%></td>
		    <td><%=map.get("MON12")==null?kg:map.get("MON12")%></td>
		    <td><%=map.get("LJ")==null?kg:map.get("LJ")%></td>		    	   
		   
		</tr>
			<%
				}
			}
		%>
			
</table>

<br>
<table border="0" align="center" class="table_list">
	<tr><td>
		<input name="button2" type=button class="cssbutton" onClick="refresh();" value="刷新">
		<input name="button2" type=button class="cssbutton" onClick="window.close();" value="关闭">
	</td></tr>
</table>
</form> 
<script type="text/javascript" >
	function refresh(){
		$('fm').action="<%=contextPath%>/sales/ordermanage/orderquery/MonReport/MonReportInit.do";
		$('fm').submit();
	}
	
	
	//修改的超链接
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='searchServiceInfo("+ record.data.REQ_ID +","+ record.data.ORDER_TYPE +")'>"+ value +"</a>");
	}
	
	function searchServiceInfo(value,value2){
		$('fm').action= "<%=contextPath%>/sales/ordermanage/delivery/OrderCommandQuery/commandQueryInfo.do?reqId="+value+"&orderType="+value2;
		$('fm').submit();
	}
//设置超链接 end
	
</script>
<!--页面列表 end -->


</body>
</html>

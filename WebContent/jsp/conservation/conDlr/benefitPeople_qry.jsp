<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">
function doInit(){
   	loadcalendar();  //初始化时间控件
}
</script>
<TITLE>节能惠民信息查询</TITLE>

</HEAD>
<BODY>

<div class="navigation"><img src="../../img/nav.gif" />&nbsp;当前位置：节能惠民管理
	&nbsp;&gt;&nbsp;节能惠民信息查询</div>
    <form method="post" name ="fm" id="fm">
    <table  class="table_query">
    
     <tr>
			<td width="10%" align="right">上报时间：</td>
			<td width="20%">
				<input type="text" class="short_txt" name="reportStartDate" id="reportStartDate"  datatype="1,is_date,10" group="reportStartDate,reportEndDate" hasbtn="true" callFunction="showcalendar(event, 'reportStartDate', false);"/>
					至
				<input type="text" class="short_txt" name="reportEndDate" id="reportEndDate"  datatype="1,is_date,10" group="reportStartDate,reportEndDate" hasbtn="true" callFunction="showcalendar(event, 'reportEndDate', false);"/>
			</td> 
	 <td align="right" nowrap >业务范围：</td>
      <td align="left" nowrap >
      	<select name="areaId" class="min_sel">
				<option selected="selected" value="">--请选择--</option>
				<c:if test="${!empty areaBusList}">
					<c:forEach items="${areaBusList}" var="list">
						<option value="${list.AREA_ID}"><c:out value="${list.AREA_NAME}"/></option>
					</c:forEach>
				</c:if>
			</select>    
      </td>
      	
		</tr>
		
		<tr>
		<td align="right">车型：</td>
		      <td>
					<input type="text" name="model" id="model" size="10" value=""  />
       				<input type="button" value="..." class="mini_btn"  onclick="showMaterialGroup('model','','true','');" />
       				<input type="button" class="normal_btn" onclick="clrTxt('model');" value="清 空" id="clrBtn" /> 
			  </td>
		      
		
		<td align="right" nowrap>&nbsp;状态：</td>
		      <td>
		      	<label>
					<script type="text/javascript">
						genSelBoxExp("status",<%=Constant.VEHICLE_ENERGY_CON%>,"",true,"short_sel","onchange='changeFleet(this.value)'","false",'');
					</script>
				</label>
		      </td>
		
		<td align="right" nowrap></td>  	      
		</tr>
		
		<tr>
		<td valign="top" align="right" nowrap>节能惠民编号：</td>
		<td valign="top" align="left" nowrap>
			<input type="text" name="energyNo" id="energyNo" class="middle_txt" value="">
		</td>
		<td align="right" nowrap>&nbsp;VIN：</td>
		<td>
			<textarea name="vin" id="vin" rows="3" cols="18"></textarea>
		</td>
		<td align="center" nowrap></td>
    </tr>	
		<tr>
			<td colspan="4" align="center">
				<input name='queryBtn' type="button" class="normal_btn" value="查询" onclick="__extQuery__(1)"/>	
				<input type="button" name="queryBtn" class="normal_btn"  onclick="downLoad();" value="下载" />					
			</td>
		</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript">
var url = "<%=contextPath%>/conservation/EnergyConservationDlr/vehicleEnergyConQuery.json?COMMAND=1";
var title = null ;
var columns = [
				{header: "序号", renderer:getIndex, align:'center'},
				{header: "消费者名称", dataIndex: 'CTM_NAME', align:'center'},
				{header: "联系人", dataIndex: 'CTM_LINKMAN', align:'center'},
				{header: "联系电话", dataIndex: 'CTM_LINKTEL', align:'center'},
				{header: "销售日期", dataIndex: 'SALES_DATE', align:'center'},
				{header: "车型代码", dataIndex: 'MODELCODE', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "车系", dataIndex: 'SERIESNAME', align:'center'},
				//{header: "节能惠民单号", dataIndex: 'CONSERVATION_NO', align:'center'},
				{header: "经销商代码", dataIndex: 'DEALERCODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALERNAME', align:'center'},
				{header: "上报日期", dataIndex: 'CREATE_DATE', align:'center'},
				{header:'状态',dataIndex:'CONSERVATION_STATUS',width:'10%',align:'center',renderer:getItemValue},

               //{header:'序号',width:'8%',align:'center',renderer:getIndex},
               //{header:'节能惠民编号',dataIndex:'CONSERVATION_NO',width:'10%',align:'center'},
               //{header:'上报日期',dataIndex:'CREATE_DATE',width:'10%',align:'center'},
               //{header:'状态',dataIndex:'CONSERVATION_STATUS',width:'10%',align:'center',renderer:getItemValue},
               //{header:'车型代码',dataIndex:'MODELCODE',width:'10%',align:'center'},
               //{header:'车型',dataIndex:'MODELNAME',width:'10%',align:'center'},
               //{header:'VIN',dataIndex:'VIN',width:'10%',align:'center'},
               //{header:'销售日期',dataIndex:'SALES_DATE',width:'10%',align:'center'},
               //{header:'消费者名称',dataIndex:'CTM_NAME',width:'10%',align:'center'},
               //{header:'联系电话',dataIndex:'CTM_LINKTEL',width:'8%',align:'center'},             
               {header:'操作',width:'8%',align:'center',renderer:mylink}
               ] ;


function mylink(value,meta,record){
	var data = record.data;
  	return String.format("<a href=\"#\" onclick='Info(\""+data.CONSERVATION_ID+"\")';>[明细]</a>");
}

function Info(conservationId){
		window.location.href="<%=contextPath%>/conservation/EnergyConservationDlr/vehicleEnergyConView.do?conservationId="+conservationId;

}




function clrTxt(txtId){
	document.getElementById(txtId).value = "";
}


function showNotice(){
	$('genBtn').disabled = false ;
	var url = '<%=contextPath%>/claim/laborlist/LaborListForTax/noticeUrlInit.do' ;
	OpenHtmlWindow(url,800,500);
}
function setNotic(id,ro_no,did,dcode,dname,maker,yieldly,sd,ed,status){
	$('ro_no').value = ro_no ;
	$('id').value = id ;
	$('yieldly').value = yieldly ;
	$('saleName').value = maker ;
	$('saleId').value = did ;
	$('beginDate').value = sd ;
	$('endDate').value = ed ;
}
function wrapOut(){
	setNotic('','','','','','','','','','');
}

function downLoad(){
	var url = "<%=contextPath%>/conservation/EnergyConservationDlr/dealerVehicleEnergyConDownload.json" ;
	fm.action = url ;
	fm.submit() ;
	}
</script>
</BODY>
</html>
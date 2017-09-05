<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>合格证/VIP售后服务部审批</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}

	//申请类型和操作类型的联动
	function selectAction(){
		if(document.fm.stType.value == '<%=Constant.ORDER_SV_TYPE_01%>') {
			var sc = genSelBoxStrExp('stAction','<%=Constant.ORDER_SV_ACTION%>','',true,'short_sel','','true','<%=Constant.ORDER_SV_ACTION_03%>');
			var pu = document.getElementById("productUnit").innerHTML = sc;
		}else if(document.fm.stType.value == '<%=Constant.ORDER_SV_TYPE_02%>'){
			var sc = genSelBoxStrExp('stAction','<%=Constant.ORDER_SV_ACTION%>','',true,'short_sel','','true','');
			var pu = document.getElementById("productUnit").innerHTML = sc;
		}else{
			var sc = genSelBoxStrExp('stAction','','',true,'short_sel','','true','');
			var pu = document.getElementById("productUnit").innerHTML = sc;
		}
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理&gt;信息反馈审批&gt;合格证/VIP卡售后服务部审批</div>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">
 		<tr>
            <td width="7%" align="right" nowrap>工单号：</td>
            <td colspan="6">
            	<input name="orderId" id="orderId" type="text" size="18"  class="middle_txt" value="" >
            </td>
            <td width="19%" align="right" nowrap>经销商代码：</td>
	        <td>
			<textarea rows="2" cols="30" id="dealerCode" name="dealerCode"></textarea>
			     <input name="button1" type="button" class="mini_btn" style="cursor: pointer;"  onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
			     <input name="button2" type="button" class="normal_btn" onclick="clr();" value="清除"/>
	        </td>  
          </tr>
          <tr>
            <td align="right" nowrap="nowrap" >车辆识别码(VIN)：</td>
            <td colspan="6" nowrap="nowrap">
            	<input type="text" name="vin" size="17" value="" class="middle_txt"/>            
            </td>
            <td align="right" nowrap="nowrap">经销商名称：</td>
            <td colspan="2" align="left" >
            	<input class="middle_txt" id="dealerName" style="cursor: pointer;" name="dealerName" type="text"/>
			</td>
          </tr>
          <tr>
            <td align="right" nowrap="nowrap" >工单类型：</td>
            <td colspan="6" nowrap="nowrap"><label>
              <script type="text/javascript">
 				 genSelBoxExp("stType",<%=Constant.ORDER_SV_TYPE%>,"",true,"short_sel","onchange='selectAction()'","false",'');
			  </script>
            </label></td>
            <td align="right" nowrap="nowrap">操作类型：</td>
            <td colspan="2" align="left" >
            	<div id="productUnit">
					<select name="stAction" class="short_sel">
						<option value="">-请选择-</option>				
					</select>
				</div>
            </td>
          </tr>                   
          <tr>
            <td align="right" nowrap >提报时间： </td>
            <td colspan="6" nowrap>
            	<div align="left">
            		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            	</div>
			</td>
			<td align="right" nowrap="nowrap">车系：</td>
            <td colspan="2" align="left" >
            	<script type="text/javascript">
		              var seriesList=document.getElementById("seriesList").value;
		    	      var str="";
		    	      str += "<select id='vehicleSeriesList' name='vehicleSeriesList' class='short_txt'>";
		    	      str+=seriesList;
		    		  str += "</select>";
		    		  document.write(str);
	       		</script>	
			</td>
          </tr>
          <tr>
            <td align="right" nowrap >&nbsp;</td>
            <td colspan="6" nowrap>&nbsp;</td>
            <td align="left" nowrap><input class="normal_btn" type="BUTTON" name="button1" id="queryBtn" value="查询"  onclick="__extQuery__(1)" /></td>
            <td>&nbsp;</td>
            <td align="right" >&nbsp;</td>
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
	var url = "<%=contextPath%>/feedbackmng/approve/StandardVipFinalApproveManager/queryStandardVipFinalApprove.json";
				
	var title = null;

	var columns = [
				{header: "服务中心代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "服务中心名称", dataIndex: 'DEALER_NAME', align:'center'},
				{id:'action',header: "申请单号",sortable: false,dataIndex: 'ORDER_ID',renderer:mySelect ,align:'center'},
				{header: "车辆识别码(VIN)", dataIndex: 'VIN', align:'center'},
				{header: "车系", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "工单类型", dataIndex: 'ST_TYPE', align:'center',renderer:getItemValue},
				{header: "操作类型", dataIndex: 'ST_ACTION', align:'center',renderer:getItemValue},
				{header: "车主姓名", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "申请时间", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "申请状态", dataIndex: 'ST_STATUS', align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ORDER_ID',renderer:myLink ,align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//审批的超链接
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='approveStandardVip(\""+ value +"\")'>[审批]</a>");
	}
	
	//审批的超链接设置
	function approveStandardVip(value){
		fm.action = '<%=contextPath%>/feedbackmng/approve/StandardVipFinalApproveManager/approveFinalStandardVipApply.do?orderId=' + value;
	 	fm.submit();
	}
	
	//设置超链接
	function mySelect(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.ORDER_ID+"\")'>["+ value +"]</a>");
	}
	
	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/feedbackmng/apply/StandardVipApplyManager/queryStandardVipApplyInfo.do?orderId='+value,800,500);
	}
		//清除方法
 function clr() {
	document.getElementById('dealerCode').value = "";
  }
//设置超链接 end
	
</script>
<!--页面列表 end -->

</body>
</html>
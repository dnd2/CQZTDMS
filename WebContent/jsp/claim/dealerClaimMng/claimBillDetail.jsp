<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.po.TtAsWrApplicationExtPO"%>
<%@page import="com.infodms.dms.po.TtAsWrNetitemExtPO"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style type="text/css">
.tab{margin-top:50px;}

.tab1{width:115px;height:27px;line-height:27px;float:left;text-align:center;cursor:pointer;}
</style>
<%
	String contextPath = request.getContextPath();
%>
<%
			TtAsWrApplicationExtPO tawep = (TtAsWrApplicationExtPO) request.getAttribute("application");
			List<TtAsWrNetitemExtPO> otherLs = (LinkedList<TtAsWrNetitemExtPO>) request.getAttribute("otherLs");
			List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
			String id = (String) request.getAttribute("ID");
			List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
			request.setAttribute("fileList",fileList);
		%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<TITLE>索赔单审核</TITLE>
		<SCRIPT LANGUAGE="JavaScript">

	//返回
	function goBack(){
		window.location.href = "<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/claimBillForward.do";
	}
	
	//时间格式化
	Date.prototype.format = function(format) {   
    	var o = {   
			     "M+" : this.getMonth()+1, //month   
			     "d+" : this.getDate(),    //day   
			     "h+" : this.getHours(),   //hour   
			     "m+" : this.getMinutes(), //minute   
			     "s+" : this.getSeconds(), //second   
			     "q+" : Math.floor((this.getMonth()+3)/3), //quarter   
			     "S" : this.getMilliseconds() //millisecond   
   				}   
	   if(/(y+)/.test(format)) format=format.replace(RegExp.$1,   
	     (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
	   for(var k in o)if(new RegExp("("+ k +")").test(format))   
	     format = format.replace(RegExp.$1,   
	       RegExp.$1.length==1 ? o[k] :    
	         ("00"+ o[k]).substr((""+ o[k]).length));   
	   return format;   
	}  
	//格式化时间为YYYY-MM-DD
	function formatDate(value) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
function init(){
	if($('claimType').value==<%=Constant.CLA_TYPE_02 %>){
		$('otherTableId').style.display="none";
	}else{
	$('otherTableId').style.display="";
	}
	}
</SCRIPT>
	</HEAD>
	<BODY onload="init();" onkeydown="keyListnerResp();">
		<div class="navigation">
			<img src="../../../img/nav.gif" />
			&nbsp;当前位置：售后服务管理&gt;索赔申请&gt;索赔申明细
		</div>

		<form method="post" name="fm" id="fm">
			<input name="claimId" id ="claimId" type="hidden" value="<%=tawep.getId() %>"/>
			<input name="type" id="type" type="hidden"  value="<%=CommonUtils.checkNull(tawep.getClaimType())%>"/>
			<input type="hidden" name="claimType" value="<%=tawep.getClaimType()%>"/>
			<input type="hidden" name="VIN" id="VIN" value="<%=CommonUtils.checkNull(tawep.getVin())%>" />
			<input type="hidden" name="RO_NO" id="RO_NO" value="<%=CommonUtils.checkNull(tawep.getRoNo())%>" />
			<table border="0" align="center" cellpadding="1" cellspacing="1"
				class="table_edit">
				<th colspan="6">
					<img class="nav" src="../../../img/subNav.gif" />
					基本信息
				</th>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						VIN：
					</td>
					
					<td>
						 <%=CommonUtils.checkNull(tawep.getVin())%> 
					</td>
					
					<td class="table_edit_2Col_label_7Letter">
						发动机号：
					</td>
					
					<td>
						<%=CommonUtils.checkNull(tawep.getEngineNo())%>
					</td>
					
					<td class="table_edit_2Col_label_7Letter">
						索赔类型：
					</td>
					<td>
						<span style="color: red">
						<script type="text/javascript">
							document.write(getItemValue('<%=tawep.getClaimType()%>'));
	       				</script>
						</span>
					</td>
				</tr>
				
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						品牌：
					</td>
					<td >
						<%=CommonUtils.checkNullEx(tawep.getBrandName())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						车系：
					</td>
					<td ><%=CommonUtils.checkNull(tawep.getSeriesName())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						车型：
					</td>
					<td ><%=CommonUtils.checkNull(tawep.getModelName())%></td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						配置：
					</td>
					<td >
						<%=CommonUtils.checkNullEx(tawep.getPackageName())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						颜色：
					</td>
					<td>
						<%=CommonUtils.checkNull(tawep.getColor())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						车辆用途：
					</td>
					<td ><%=CommonUtils.checkNull(tawep.getCarUseType())%></td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						工单号：
					</td>
					<td>
						<%=CommonUtils.checkNullEx(tawep.getRoNo())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						工单开始时间：
					</td>
					<td nowrap="nowrap">
						 <%=CommonUtils.checkNull(Utility.handleDate1(tawep
							.getRoStartdate()))%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						工单结算时间：
					</td>
					<td>
						 <%=CommonUtils.checkNull(Utility.handleDate1(tawep
							.getRoEnddate()))%> 
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						系统里程：
					</td>
					<td>
					<span style="color: red">
						 <fmt:formatNumber value="<%=CommonUtils.checkNull(tawep.getMileage())%>" maxIntegerDigits="20" maxFractionDigits="10"  pattern="0"/>
					</span>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						进厂里程：
					</td>
					<td>
						 <fmt:formatNumber value="<%=CommonUtils.checkNull(tawep.getInMileage())%>" maxIntegerDigits="20" maxFractionDigits="10"  pattern="0"/>
					<input type="hidden" name="IN_MILEAGE" id="IN_MILEAGE" value="<%=CommonUtils.checkNull(tawep.getInMileage())%>" />
					</td>
					<td class="table_edit_2Col_label_7Letter">
						活动代码：
					</td>
					<td><%=CommonUtils.checkNull(tawep.getCampaignName())%></td>
				</tr>
				
				<tr>
				<td class="table_edit_2Col_label_7Letter">
						系统保养次数：
					</td>
					<td >
					<span style="color: red">
					<%=CommonUtils.checkNull(tawep.getFreeTimes())%>
					</span>
					</td>
					
					<td class="table_edit_2Col_label_7Letter">
						单据保养次数：
					</td>
					<td colspan="4">
						<% if(tawep.getFreeMAmount()>0){
						
						out.print(tawep.getFreeMAmount());
					} else{
						out.print("");
					}%>
					</td>
					
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						索  赔  员：
					</td>
					<td>
						 <%=CommonUtils.checkNull(tawep.getReporter())%> 
					</td>
					<td class="table_edit_2Col_label_7Letter">
						索赔主管电话：
					</td>
					<td ><%=CommonUtils.checkNull(tawep.getClaimDirectorTelphone())%></td>
					<td class="table_edit_2Col_label_7Letter">
						索赔单号：
					</td>
					<td  ><%=CommonUtils.checkNull(tawep.getClaimNo())%>
					</td>
				</tr>
				</table>
			<table border="0" align="center" cellpadding="1" cellspacing="1"
				class="table_edit">
				<th colspan="6">
					<img class="nav" src="../../../img/subNav.gif" />
					用户信息
				</th>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						车主姓名：
					</td>
					<td >
						<%=CommonUtils.checkNull(tawep.getCtmName())%>&nbsp;
					</td>
					<td class="table_edit_2Col_label_7Letter">
						车主电话：
					</td>
					<td ><%=CommonUtils.checkNull(tawep.getCtmPhone())%>&nbsp;
					</td>
					<td class="table_edit_2Col_label_7Letter">
						车主地址：
					</td>
					<td ><%=CommonUtils.checkNull(tawep.getCtmAddress())%>&nbsp;</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						送修人姓名：
					</td>
					<td >
						<%=CommonUtils.checkNull(tawep.getDeliverer())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						送修人手机：
					</td>
					<td colspan="4" ><%=CommonUtils.checkNull(tawep.getDelivererPhone())%>
					</td>
					
				</tr>
			</table>
			<table id="otherTableId" align="center" cellpadding="0"
				cellspacing="1" class="table_list">
				<tr align="center" class="table_list_row1">
					<td>
							项目代码
					</td>
					<td>
							项目名称
					</td>
					<td>
							金额(元)
					</td>
					<td>
						备注
					</td>
				</tr>
				<tbody id="otherTable">
					<%
						for (int i = 0; i < otherLs.size(); i++) {%> 
					<tr class="table_list_row1" >
						<td>
						 
						   <%=otherLs.get(i).getItemCode()%> 
						   <input type="hidden"  id="ItemCode" name="ItemCode" value="<%=otherLs.get(i).getItemCode()%>" />
						 
						</td>
						<td>
						 <%=CommonUtils.checkNull(otherLs.get(i).getItemDesc())%> 
						</td>
						<td>
						<%=CommonUtils.checkNull(otherLs.get(i).getAmount())%>
						</td>
						<td title="<%=otherLs.get(i).getRemark()==null?"":otherLs.get(i).getRemark()%>">
						<a href="#" onclick="alInfo('<%=otherLs.get(i).getRemark()==null?"":otherLs.get(i).getRemark()%>');" ><%=otherLs.get(i).getRemark()==null?"":otherLs.get(i).getRemark()%></a>
						</td>
					</tr>
					<%
						}
					%>
				</tbody>
			</table>
		<table border="0" align="center" cellpadding="1" cellspacing="1"
				class="table_edit">
				<th colspan="6">
					<img class="nav" src="../../../img/subNav.gif" />
					申请费用：<span style="color:red">总金额(含税)：<%=CommonUtils.checkNull(tawep.getGrossCredit())%>元整</span>
				</th>
			</table>
            <c:if test="<%=!tawep.getClaimType().equals(10661002)%>">
				 <!-- 添加附件 -->
			<table class="table_info" border="0" id="file">
				<input type="hidden" id="fjids" name="fjids"/>
			    <tr colspan="8">
			        <th>
					<img class="nav" src="../../../img/subNav.gif" />
					&nbsp;附件列表：
					</th>
				</tr>
				<tr>
      				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv2.jsp" /></td>
    			</tr>
    			<%for(int i=0;i<attachLs.size();i++) { %>
    			<script type="text/javascript">
    			addUploadRowByDbView('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getPjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
    			</script>
    			<%} %>
 			</table>
 			</c:if>
 			<table class="table_edit">
			<tr>
				<th colspan="10" align="left"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
				&nbsp;<a href="#" onclick="hiddenTable('authTable');" >索赔单审核明细</a>
				</th>
		 	</tr>
		 </table>
		 <table class="table_list" style="border-bottom: 1px solid #DAE0EE;display: none" id="authTable">
			 <tr class='table_list_th'>
	        <th nowrap="true">序号</th>
	        <th nowrap="true">授权人</th>
	        <th nowrap="true">日期</th>
	        <th nowrap="true">授权结果</th>
	        <th nowrap="true">授权备注</th>
	      </tr>
	       <c:set var="numSize"  value="1" />
		 <c:forEach var="list" items="${appAuthls}" varStatus="s" >
		  <tr class="${numSize}%2==0?"table_list_row1":"table_list_row2"">
	          
	            <td>${numSize}</td>
	            <td>
	              ${list.NAME}&nbsp;
	          </td>
	          <td>
					${list.AUDIT_TIME}&nbsp;</td>
	          <td>
	              ${list.CODE_DESC}&nbsp;
	          </td>
	          <td width="300">
					${list.AUDIT_REMARK}&nbsp;
				</td>
	           </tr>
	           <c:set var="numSize"  value="${numSize+1}" />
			</c:forEach>
	    </table>
	    <c:if test="<%=tawep.getClaimType().equals(10661002)%>">
		 <!-- 添加附件 开始  -->
        <table id="add_file"  width="75%" class="table_info" border="0" id="file">
	    		<tr>
	        		<th>
						<input type="hidden" id="fjids" name="fjids"/>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
					</th>
				</tr>
				<tr>
    				<td width="75%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  				<%if(fileList!=null){
	  				for(int i=0;i<fileList.size();i++) { %>
					  <script type="text/javascript">
				    		addUploadRowByDbView('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
				    	</script>
				<%}}%>
			</table> 
  		<!-- 添加附件 结束 -->
		</br>
		</c:if>
			<table border="0" cellspacing="0" cellpadding="0" class="table_edit">
				<tr>
					<td  colspan="5" align="center">
					<input class="normal_btn" type="button" value="维修历史" name="historyBtn"
		 onclick="openWindowDialog(1);"/>
		
		<input class="normal_btn" type="button" value="授权历史" name="historyBtn"
		 onclick="openWindowDialog(2);"/>
		
		<input class="normal_btn" type="button" value="保养历史" name="historyBtn" 
		onclick="openWindowDialog(3);"/>
		 <input class="normal_btn" type="button" value="关闭" onclick="closeWindow();"/>
			 </td>
		 <td>
		 	<input class="long_btn" type="button" id="three_package_set_btn" value="三包判定" onclick="threePackageSet();"/>
			 </td>
				</tr>
			</table>
		</form>
<script type="text/javascript">
 //根据是否存在父页面关闭窗口
	  function closeWindow(){
		if(parent.${'inIframe'}){
			parent.window._hide();
		}else{
			window.close();
		}
	  }
	  function openWindowDialog(type){
			var targetUrl="";
			var vin = $('VIN').value;
			var RO_NO = $('RO_NO').value;
			if(type==1){
			targetUrl = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN='+vin;
			}else if(type==2){
				targetUrl = '<%=contextPath%>/repairOrder/RoMaintainMain/authDetail.do?VIN='+vin+'&RO_NO='+RO_NO;
			}else{
			targetUrl = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/freeMaintainHistory.do?VIN='+vin;
			}
			  var height = 500;
			  var width = 800;
			  var iTop = (window.screen.availHeight-30-height)/2;  //获得窗口的垂直位置      
			  var iLeft = (window.screen.availWidth-10-width)/2;   //获得窗口的水平位置
			  var params = "width="+width+",height="+height+",top="+iTop+",left="+iLeft+",scrollbars=yes,resizable=yes,status=no";
			  window.open(targetUrl,null,params);
		  	}

	function alInfo(str){
		MyAlert(str);
	}
	function hiddenTable(str){
	if($(str).style.display=='none'){
		$(str).style.display='';
	}else{
		$(str).style.display='none';
	 }
	}
	//配件三包判定按钮方法
		function threePackageSet(){
				var roNo = $('RO_NO').value ;
				var vin = document.getElementById("VIN").value;
				var inMileage = document.getElementById("IN_MILEAGE").value;
				var arr = document.getElementsByName('PART_CODE');
				var PAY_TYPE_PART = document.getElementsByName('PAY_TYPE_PART');
				var WR_LABOURCODE= document.getElementsByName('WR_LABOURCODE');
				var PAY_TYPE_ITEM= document.getElementsByName('PAY_TYPE_ITEM');
				var str = ''; 
				for(var i=0;i<arr.length;i++)
					str = str+arr[i].value+"," ;
				var codes = str.substr(0,str.length-1);
				str = '';
				for(var i=0;i<PAY_TYPE_PART.length;i++)
					str = str+PAY_TYPE_PART[i].value+"," ;
				var codes_type = str.substr(0,str.length-1);
				
				var strcode = '';
				for(var i=0;i<WR_LABOURCODE.length;i++)
					strcode = strcode+WR_LABOURCODE[i].value+"," ;	
				var labcodes = strcode.substr(0,strcode.length-1);
				strcode = '';
				for(var i=0;i<PAY_TYPE_ITEM.length;i++)
					strcode = strcode+PAY_TYPE_ITEM[i].value+"," ;	
				var labcodes_type = strcode.substr(0,strcode.length-1);
				
				if (vin==null||vin==''||vin=='null') {
					MyAlert("车辆VIN不能为空！");
				}else if (inMileage==null||inMileage==''||inMileage=='null') {
					MyAlert("进厂里程数不能为空！");
				}else{
					window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/threePackageSet.do?VIN='+vin+'&mile='+inMileage+'&codes='+codes+'&codes_type='+codes_type+'&labcodes='+labcodes+'&labcodes_type='+labcodes_type+'&roNo='+roNo);
				}
		}
</script>
	</BODY>
</html>

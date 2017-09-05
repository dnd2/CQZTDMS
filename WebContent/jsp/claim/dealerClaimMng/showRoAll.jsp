<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>工单</title>
<% String contextPath = request.getContextPath();%>
<%
List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
%>
<script language="javascript" type="text/javascript">
 	var selItemIds='';
 	var selPartsIds='';
 	var selIds = '';
	var roId='<%=request.getAttribute("roId")%>';
	
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/queryRepairOrder.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'id',align:'center',renderer:mySelect},
				{header: "工单号", dataIndex: 'roNo', align:'center'}
				//{header: "车牌号", dataIndex: 'licenseNo', align:'center'},
				//{header: "车系", dataIndex: 'seriesName', align:'center'},
				//{header: "车型", dataIndex: 'modelName', align:'center'},
				//{header: "发动机号", dataIndex: 'engineNo', align:'center'}
		      ];
	//单选radio 渲染
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setRoNo(\""+value+"\")' />");
	}
	//设置全局变量roNo(切换工单需要清空工时，配件和其他项目列表)
	function setRoNo(ro_id) {
		//没有变工单号的点击不执行任何操作
		if (ro_id==roId) {
			
		}else{
			document.getElementById("roId").value=ro_id;
			roId = ro_id;
		}
	} 
	//新增查询 活动工时 
	function OpenItemWorkHours(){
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/roItemWorkHoursQuery.do?roId='+roId,800,500);
	}
	//新增查询 配件 
	function OpenItemParts(){
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/roItemPartsQuery.do?roId='+roId,800,500);
	}
	//新增查询 活动其它项目 
	function OpenItemOthers(){
		OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/roItemOthersQuery.do?roId='+roId,800,500);
	}
	/*
	           功能：工时删除方法
	  	参数：action : "del"删除
	  	描述:取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	*/
	function optionsWork(id) {
		MyDivConfirm("是否确认删除？",optionsChanage,[id]);
	}
	function optionsChanage(id) {
		makeNomalFormCall('<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/deleteItemWorkHoursOption.json?itemId='+id+'&roId='+<%=request.getAttribute("roId") %>,delBack,'fm','queryBtn');
	}
	/*
		 功能：配件删除方法
		参数：action : "del"删除
		描述: 取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	*/
	function optionsParts(id) {
		MyDivConfirm("是否确认删除？",Parts,[id]);
			
	}
	function Parts(id){
		makeNomalFormCall('<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/deleteItemPartsOption.json?partsId='+id+'&roId='+<%=request.getAttribute("roId") %>,delBack,'fm','queryBtn');
	}
	/*
		功能：活动其它项目 删除方法
		参数：action : "del"删除
		描述:取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	*/
	function optionsOthers(id) {
		MyDivConfirm("是否确认删除？",options,[id]);
	}
	function options(id){
		makeNomalFormCall('<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/deleteItemOthersOption.json?id='+id+'&activityId='+<%=request.getAttribute("activityId") %>,delBack,'fm','queryBtn');
	}
	//删除所有工时，配件，其他项目
	function delAlloptions() {
		makeNomalFormCall('<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/deleteItemOthersOption.json?activityId='+<%=request.getAttribute("activityId") %>,delBack,'fm','queryBtn');
	}
//删除回调方法
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyDivAlert("删除成功！");
		goBack();
	} else {
		MyDivAlert("删除失败！请联系管理员！");
	}
}
 //返回配件列表页面;
 //返回活动工时列表;
 //返回配件列表页面;
	function goBack(){
		fm.action = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/roItemQuery.do?roId="+<%=request.getAttribute("roId") %>;
		fm.submit();
	}
	//截掉第一个,
	function removeComma(cons) {
		if(cons!=null&&cons!='') {
			return cons.substring(1,cons.length);
		}else {
			return '';
		}
	}
	//提交
	function confirmRo(ro_id,ro_no) {
	getChecked();
	//fm.action='<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selAllOptions.do?labourId='+removeComma(selItemIds)+'&partsId='+removeComma(selPartsIds)+'&id='+removeComma(selIds)+'&roId'+roId;
	//fm.submit();
	//MyAlert(removeComma(selItemIds)+"====="+removeComma(selPartsIds)+"====="+removeComma(selIds));
		//MyAlert(ro_id);
		if (parent.$('inIframe')) {
 		 parentContainer.confirmRo(roId,removeComma(selItemIds),removeComma(selPartsIds),removeComma(selIds),ro_no);
 		} else {
		 parent.confirmRo(roId,removeComma(selItemIds),removeComma(selPartsIds),removeComma(selIds),ro_no);
		}
		parent._hide();
	}
	//全选按钮
	function selectAll(obj,name) {
		var objch = obj.checked;
		var toGet;
		if (name=="item") {
			toGet="itemId";
		}else if (name=="part") {
			toGet="partsId";
		}else if (name=="other") {
			toGet="id";
		}
		if (objch) {
		 	var myarr = document.getElementsByName(toGet);
		 	if (myarr!=null) {
			 	for (var i=0;i<myarr.length;i++) {
			 		myarr[i].checked=true;
		 		}
		 	}
		}else{
			var myarr = document.getElementsByName(toGet);
		 	if (myarr!=null) {
			 	for (var i=0;i<myarr.length;i++) {
			 		myarr[i].checked=false;
		 		}
		 	}
		}
	}
	//取所有选择的复选框
	function getChecked() {
		var itemIds = document.getElementsByName('itemId');
		var partsIds = document.getElementsByName('partsId');
		var ids = document.getElementsByName('id');
		if (itemIds!=null) {
			for (var i=0;i<itemIds.length;i++) {
				if (itemIds[i].checked) {
					selItemIds += ","+itemIds[i].value;
				}
			}
		}
		if (partsIds!=null) {
			for (var i=0;i<partsIds.length;i++) {
				if (partsIds[i].checked) {
					selPartsIds += ","+partsIds[i].value;
				}
			}
		}
		if (ids!=null) {
			for (var i=0;i<ids.length;i++) {
				if (ids[i].checked) {
					selIds += ","+ids[i].value;
				}
			}
		}
	}
</script>
</head>

<body>
	<div class="navigation">
			<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;经销商索赔管理&gt;索赔单编辑&gt;工单明细
	</div>
<form name="fm" id="fm" method="post">
  <input type="hidden" name="roId" id="roId" value="<%=request.getAttribute("roId") %>" />
  <table class="table_edit">
  	<tr>
  		<td align="right">工单号：</td>
  		<td align="left"><c:out value="${roBean.roNo}"/></td>
  		<td  align="right">工单开始时间：</td>
  		<td  align="left">
  		${roBean.startDate }
  		</td>
  		<td  align="right">工单结算时间：</td>
  		<td  align="left">
  		${roBean.endDate}
  		</td>
  	</tr>
  	<tr>
  		<td  align="right">进场里程数：</td>
  		<td  align="left"><c:out value="${roBean.inMileage}"/></td>
  		<td  align="right">保修开始时间：</td>
  		<td  align="left">
  		<script type="text/javascript">
  		document.write(DateUtil.Format('yyyy-MM-dd','${roBean.guaranteeDate}'));
  		</script>
  		</td>
  		<td  align="right">接待员：</td>
  		<td  align="left"><c:out value="${roBean.serviceAdvisor}"/></td>
  	</tr>
  	<tr>
  		<td  align="right">VIN：</td>
  		<td  align="left"><c:out value="${roBean.vin}"/></td>
  		<td  align="right">车牌号：</td>
  		<td  align="left"><c:out value="${roBean.licenseNo}"/></td>
  		<td  align="right">发动机号：</td>
  		<td  align="left"><c:out value="${roBean.engineNoV}"/></td>
  	</tr>
  	<tr>
  		<td  align="right">品牌：</td>
  		<td  align="left"><c:out value="${roBean.brandName}"/></td>
  		<td  align="right">车系：</td>
  		<td  align="left"><c:out value="${roBean.seriesName}"/></td>
  		<td  align="right">车型配置：</td>
  		<td  align="left"><c:out value="${roBean.modelName}"/></td>
  		
  	</tr>
  	<tr>
  		<td  align="right">产地：</td>
  		<td  align="left">
  		<c:out value="${roBean.yieldlyName}"/>
  		</td>
  		<td align="right">购车日期：</td>
  		<td id="PURCHASED_DATE" name="PURCHASED_DATE">
  		<script type="text/javascript">
  		document.write(DateUtil.Format('yyyy-MM-dd','${roBean.purchasedDate}'));
  		</script>
  		</td>
  	</tr>
  	
  </table>
  	<table width=95% border=0 class="table_list" style="border-bottom:1px solid #DAE0EE">
  	<tbody>
     <tr>
		  <th colspan="7" align="left">
			  <img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 工单工时 
		      <input type="button" name="time_add"  class="normal_btn" style="display:none" value="新增" onclick="OpenItemWorkHours();"  />
	      </th>
      </tr>
      <tr>
      	  <th height="22" align="center" class="zi" style="display:none"><b><input type="checkbox" onclick="selectAll(this,'item');"/></b></th>
	      <th height="22" align="center" class="zi"><b>工时代码</b></th>
	      <th height="22" align="center" class="zi"><b>工时名称</b></th>
	      <th height="22" align="center" class="zi"><b>工时数</b></th>
	      <th height="22" align="center" class="zi"><b>付费方式</b></th>
	      <!-- <th height="22" align="center" class="zi"><b>操作</b></th>  -->
	  </tr>
	  <c:forEach var="ActivityBeanList" items="${ActivityBeanList}">
		         <tr class="table_list_row1">
		              <!-- <input type="hidden" name="itemId" id="itemId" value="${ActivityBeanList.itemId}"></input> -->
		              <td style="display:none"><input type="checkbox" checked name="itemId" id="itemId" value="${ActivityBeanList.itemId}" /></td>
				      <td><c:out value="${ActivityBeanList.itemCode}"></c:out></td>
				      <td><c:out value="${ActivityBeanList.itemName}"></c:out></td>
				      <td><c:out value="${ActivityBeanList.normalLabor}"></c:out></td> 
				      <td><script type="text/javascript">document.write(getItemValue('<c:out value="${ActivityBeanList.payType}"/>'))</script></td> 
				      <!--  <td>
				           <input type="button" name="time_delete" class="normal_btn" value="删除" onclick="optionsWork('${ActivityBeanList.itemId}');">
				      </td>          -->  
	             </tr>
	</c:forEach>
    </tbody>
    </table>
  <br>
  <table width=95% border=0 class="table_list" style="border-bottom:1px solid #DAE0EE">
      <tr>
        <th colspan="8" align="left">
	         <img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 工单配件 
	      	 <input type="button" name="time_add3" class="normal_btn"  style="display:none"  value="新增" onclick="OpenItemParts();"  />
      	</th>
      </tr>
      <tr>
      	  <th width="14%" style="display:none"><b><input type="checkbox" onclick="selectAll(this,'part');"/></b></th>
      	  <th width="10%" ><b>是否三包</b></th>
	      <th width="14%" ><b>配件代码</b></th>
	      <th width="14%" ><b>配件名称</b></th>
	      <th width="10%" ><b>配件数量</b></th>
	      <th width="10%" ><b>付费方式</b></th>
	      <th width="10%" ><b>责任性质</b></th>
	      <th width="14%" ><b>工时代码</b></th>
	      <th width="14%" ><b>处理方式</b></th>
	  </tr>
	   <c:forEach var="ActivityPartsList" items="${ActivityPartsList}">
		<tr>
		 <!--  <input type="hidden" name="partsId" id="partsId" value="${ActivityPartsList.partsId}"></input>-->
		  <td style="display:none"><input type="checkbox" checked name="partsId" id="partsId"  value="${ActivityPartsList.partsId}" /></td>
		   <td>	
		  <c:choose> 
		  <c:when test="${ActivityPartsList.isGua==1}"> 
		    Y
		  </c:when> 
  		  <c:otherwise> 
			N
 		  </c:otherwise> 
		  </c:choose>
		  </td>
		  <td><c:out value="${ActivityPartsList.partNo}"></c:out></td>
		  <td><c:out value="${ActivityPartsList.partName}"></c:out></td>
		  <td><c:out value="${ActivityPartsList.partQuantity}"></c:out></td>
		  <td><script type="text/javascript">document.write(getItemValue('${ActivityPartsList.payType}'))</script></td>
		  <td><script type="text/javascript">document.write(getItemValue('${ActivityPartsList.pratResponsId}'))</script></td>
		   <td><c:out value="${ActivityPartsList.labour}"></c:out></td>
		 <td><script type="text/javascript">document.write(getItemValue('${ActivityPartsList.partUseType}'))</script></td>
		</tr>
      </c:forEach>
  </table>
  
    <table width=95% border=0 class="table_list" style="border-bottom:1px solid #DAE0EE">
      <tr>
        <th colspan="6" align="left">
	         <img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 补偿
      	</th>
      </tr>
      <tr>
      	  <th width="14%" ><b>供应商代码</b></th>
	      <th width="14%" ><b>补偿费申请金额</b></th>
	      <th width="14%" ><b>审批金额</b></th>
	      <th width="14%" ><b>补偿关联主因件</b></th>
	      
	  </tr>
	   <c:forEach var="compensationMoneyList" items="${compensationMoneyList}">
		<tr>
		  <td><c:out value="${compensationMoneyList.SUPPLIER_CODE}"></c:out></td>
		  <td><c:out value="${compensationMoneyList.APPLY_PRICE}"></c:out></td>
		  <td><c:out value="${compensationMoneyList.PASS_PRICE}"></c:out></td>
		  <td><c:out value="${compensationMoneyList.PART_CODE}"></c:out></td>
		</tr>
      </c:forEach>
  </table>
  	<br/>
    <table width=95% border=0 class="table_list" style="border-bottom:1px solid #DAE0EE">
      <tr>
        <th colspan="6" align="left">
	         <img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 辅料 
      	</th>
      </tr>
      <tr>

      	  <th width="14%" ><b>辅料代码</b></th>
	      <th width="14%" ><b>辅料名称</b></th>
	      <th width="14%" ><b>辅料金额</b></th>
	      <th width="14%" ><b>辅料关联主因件</b></th>
	      
	  </tr>
	   <c:forEach var="accessoryDtlList" items="${accessoryDtlList}">
		<tr>
		  <td><c:out value="${accessoryDtlList.WORKHOUR_CODE}"></c:out></td>
		  <td><c:out value="${accessoryDtlList.WORKHOUR_NAME}"></c:out></td>
		  <td><c:out value="${accessoryDtlList.PRICE}"></c:out></td>
		  <td><c:out value="${accessoryDtlList.MAIN_PART_CODE}"></c:out></td>
		</tr>
      </c:forEach>
  </table>
   <br>
  <table width=95% border=0 class="table_list" style="border-bottom:1px solid #DAE0EE">
      <tr>
	      <th colspan="6" align="left">
		        <img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 活动其它项目 
		        <input type="button" name="time_add2" class="normal_btn"  style="display:none" value="新增" onclick="OpenItemOthers();"/>
	      </th>
      </tr>
      <tr>
      	  <th width="14%" height="22" align="center" class="zi" style="display:none"><b><input type="checkbox" onclick="selectAll(this,'other');"/></b></th>
	      <th width="14%" height="22" align="center" class="zi"><b>项目代码</b></th>
	      <th width="14%" height="22" align="center" class="zi"><b>项目名称</b></th>
	      <th width="14%" height="22" align="center" class="zi"><b>金额</b></th>
	      <th width="14%" height="22" align="center" class="zi"><b>备注</b></th>
	      <th width="14%" height="22" align="center" class="zi"><b>付费方式</b></th>
	  </tr>
	   <c:forEach var="ActivityNetItemList" items="${ActivityNetItemList}">
			<tr>
			 <!-- <input type="hidden" name="id" id="id" value="${ActivityNetItemList.id}"></input> -->
			  <td style="display:none"><input type="checkbox" checked name="id" id="id" value="${ActivityNetItemList.id}" /></td>
			  <td><c:out value="${ActivityNetItemList.itemCodes}"></c:out></td>
			  <td><c:out value="${ActivityNetItemList.itemDesc}"></c:out></td>
			  <td><c:out value="${ActivityNetItemList.amount}"></c:out></td>
			  <td><c:out value="${ActivityNetItemList.remark}"></c:out></td>
			   <td><script type="text/javascript">document.write(getItemValue('<c:out value="${ActivityNetItemList.payType}"/>'))</script></td>
			  <!--<td><input type="button" name="time_delete" class="normal_btn" value="删除" onclick="optionsOthers('${ActivityNetItemList.id}');"></td>   -->    
			</tr>
	      </c:forEach>
  </table>
  
   <!-- 添加附件 -->
			<table class="table_info" border="0" id="file">
				<input type="hidden" id="fjids" name="fjids"/>
			    <tr colspan="8">
			        <th>
					<img class="nav" src="../../../img/subNav.gif" />
					&nbsp;附件列表：
					</th>
					<th><span align="left"><input type="button" class="normal_btn" disabled="disabled"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/></span>
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
 			
<br>
  <table class="table_query">
	  <tr>
	  		<td align="center"><input type="button" name="return1" onclick="confirmRo('<%=request.getAttribute("roId")%>','<%=request.getAttribute("roNo")%>');"  class="normal_btn" value="确定"/>
	  		&nbsp;&nbsp;<input type="button" name="return1" onclick="parent.window._hide();"  class="normal_btn" value="关闭"/></td>
	  </tr>
  </table>
  </form>
</body>
</html>
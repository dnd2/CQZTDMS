<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单提报</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订做审核 > 订做车需求产品审核</div>
<form method="POST" name="fm" id="fm">
	<table class=table_list style="border-bottom:1px solid #DAE0EE" >  
		<tr class=cssTable >
			<th nowrap="nowrap">车系</th>
			<th nowrap="nowrap">车型编号</th>
			<th nowrap="nowrap">配置编号</th>
			<th nowrap="nowrap">配置名称</th>
			<th nowrap="nowrap">需求数量</th>
			<th nowrap="nowrap">订做批次号</th>
			<th nowrap="nowrap">预计交付周期</th>
		</tr>
    	<c:forEach items="${list}" var="po">
    		<tr class="table_list_row2">
		      <td align="center">${po.SERIES_NAME}</td>
		      <td align="center">${po.MODEL_CODE}</td>
		      <td align="center">${po.GROUP_CODE}</td>
		      <td align="center">${po.GROUP_NAME}</td>
		      <td align="center">${po.AMOUNT}</td>
		      
		      <td align="center"><input type="text" class="short_txt" name="batchNo" maxlength="10" size="10" value="${po.BATCH_NO}"/><input type="hidden" name="detailId" value="${po.DTL_ID}"/></td>
		      <td align="center">
		      	<input class="short_txt"  type="text" id="expectedPeriod${po.DTL_ID}" name="expectedPeriod" datatype="1,isDigit,4" value="" />天
		      </td>
		    </tr>
    	</c:forEach>
	</table>	
	<c:if test="${attachList!=null}">
	<br>
	<table id="attachTab" class="table_info">
		<tr>
	        <th colspan="2">附件列表(经销商)：<!-- <input type="hidden" id="fjids" name="fjids"/> -->
			</th>
		</tr>
	  		<c:forEach items="${attachList}" var="attls">
			    <tr class="table_list_row1" id="${attls.FJID}">
			    <td colspan="2"><a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a></td>
			    </tr>
			</c:forEach>
	</table>
	</c:if>
	<br />
	<table class=table_list style="border-bottom:1px solid #DAE0EE" >  
		<tr class=cssTable >
			<th nowrap="nowrap">日期</th>
			<th nowrap="nowrap">单位</th>
			<th nowrap="nowrap">操作人</th>
			<th nowrap="nowrap">审核结果</th>
			<th nowrap="nowrap">审核描述</th>
		</tr>
    	<c:forEach items="${checkList}" var="checkList">
    		<tr class="table_list_row2">
		      <td align="center">${checkList.CHECK_DATE}</td>
		      <td align="center">${checkList.ORG_NAME}</td>
		      <td align="center">${checkList.NAME}</td>
		      <td align="center">${checkList.CHECK_STATUS}</td>
		      <td align="center">${checkList.CHECK_DESC}</td>
		    </tr>
    	</c:forEach>
	</table>	
	<br />
	<!-- 附件 -->
	<table class="table_info" border="0" id="file">
	    <tr>
	        <th>附件列表：<input type="hidden" id="fjids" name="fjids"/>
				<span>
					<input type="button" class="cssbutton"  onclick="showUpload('<%=request.getContextPath()%>')" value ='添加附件'/>
				</span>
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
	</table>
	<table class=table_query>
		<tr class="cssTable">
			<td width="10%" align="right">
					是否改变配置颜色：
			</td>
			<td>
				<select id="isAudit" name="isAudit">
					<option value="1">是</option>
					<option value="0">否</option>
				</select>
			</td>
		</tr>
		<tr class=cssTable>
			<td width="7%" align="right">集团客户：</td>
			<td width="50%" colspan="3" align="left"  nowrap>${fleetName}</td>
		</tr>
		<tr class=cssTable>
			<td width="7%" align="right">改装说明：</td>
			<td width="50%" colspan="3" align="left"  nowrap><textarea name="remark1" id="remark1" rows="4" cols="50" disabled="disabled"><c:out value="${remark}"/></textarea></td>
		</tr>
		<tr class=cssTable>
			<td width="7%" align="right">审核描述：</td>
			<td width="50%" colspan="3" align="left"  nowrap><textarea name="remark" id="remark" rows="4" cols="50"></textarea></td>
		</tr>
		<tr class=cssTable >
			<td>&nbsp;</td>
			<td colspan="3" align="left">
				<input type="hidden" name="detailIds" id="detailIds"/>
				<input type="hidden" name="batchNos" id="batchNos"/>
				<input type="hidden" name="flag" id="flag"/>
				<input type="hidden" name="reqId" id="reqId" value="${reqId}"/>
				<input type="hidden" name="ver" id="ver" value="${ver}"/>
				<input type="button" name="button1" class="cssbutton" onclick="confirmAdd('0');" value="审核通过" id="queryBtn1" />
				<input type="button" name="button2" class="cssbutton" onclick="confirmAdd('1');" value="驳回" id="queryBtn2" /> 
				<input type="button" name="button3" class="cssbutton" onclick="toBack();" value="返回" id="queryBtn3" /> 
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	//提交校验
	function confirmAdd(value){
		var batchNos = '';
		var detailIds ='';
		var batchNo = document.getElementsByName("batchNo");
		var detailId = document.getElementsByName("detailId");
		var expectedPeriod = document.getElementsByName("expectedPeriod");//预计交付周期
		for(var i=0 ;i< detailId.length; i++){
			if(value==0){
				//if(!batchNo[i].value){
				//	MyDivAlert("请输入订做车批次号！");
				//	return false;
				//}
				if(!expectedPeriod[i].value){
					MyAlert("请输入预计交付周期！");
					return false;
				}
			}
			if(!batchNo[i].value){
				batchNos = '...' + ',' + batchNos;
			}else{
				batchNos = batchNo[i].value + ',' + batchNos;
			}
			detailIds = detailId[i].value + ',' + detailIds;
		}
		
		/* //添加选择是否添加价为是时必须上传附件
		var isAudit = document.getElementById('isAudit').value;
		var fjids = document.getElementById("uploadFileId");
		if(isAudit == "1" && fjids == null) {
			MyAlert("请选上传附件！");
			return false;
		} */
		if(value==1&&document.getElementById("remark").value.trim()==""){
			MyAlert("请输入审核描述！");
			return false;
		}
		document.getElementById("batchNos").value=batchNos;
		document.getElementById("detailIds").value=detailIds;
		document.getElementById("flag").value = value;
		/* MyDivConfirm("确认提交？",toAdd); */
		MyConfirm("确认提交？",toAdd);
	}
	//提交
	function toAdd(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderaudit/SpecialNeedProductCheck/specialNeedProductDetailCheck.json',showResult,'fm');
	}
	//返回
	function toBack(){
		/* _hide(); */
		history.back();
	}
	//回调方法
	function showResult(json){ 
		if(json.returnValue == '1'){
			try{
				var rowIndex = parent.$('inIframe').contentWindow.rowObjNum;
				var tab = parent.$('inIframe').contentWindow.tabObj;
				tab.rows(rowIndex).removeNode(true);
			}catch(e){}
			parent._hide();
			parent.MyAlert("操作成功！");
		}else if(json.returnValue == '2'){
			window.parent.MyAlert("数据已被修改,提交失败！");
		}else if(json.returnValue == '3'){
			window.parent.MyAlert("价格未维护,请先核价！");
		}else{
			MyAlert("提交失败！请联系系统管理员！");
		}
		$('fm').action='<%=request.getContextPath()%>/sales/ordermanage/orderaudit/SpecialNeedProductCheck/specialNeedProductCheckInit.do';
		$('fm').submit();
	}
	//初始化
    function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</body>
</html>

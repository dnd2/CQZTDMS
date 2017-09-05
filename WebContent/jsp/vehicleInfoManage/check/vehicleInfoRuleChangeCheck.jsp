<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更审核</title>
<% String contextPath = request.getContextPath(); 
List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
request.setAttribute("fileList",fileList); %>
<script type="text/javascript">
	
</script>
<body onload="javascript:initPage();">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;车辆三包信息变更审核
<form name="fm" id="fm">
<input type="hidden" name="code" id='code' value="${code.codeId }"/>
<input type="hidden" name="changeId" id='changeId' value="${map.CHANGE_ID }"/>
<input type="hidden" name="partCode" id='partCode' value="${map.PART_CODE }"/>
<input type="hidden" name="partName" id='partName' value="${map.PART_NAME }"/>
<input type="hidden" name="ruleListId" id='ruleListId' value="${map.RULE_LIST_ID }"/>
<input type="hidden" name="ruleCode" id='ruleCode' value="${map.RULE_CODE }"/>
<input type="hidden" name="gameId" id="gameId" value="${map.GAME_ID}"/>
<input type="hidden" name="checkFlag" id='checkFlag' value="0"/>
<input type="hidden" name="commonRuleValue" id='commonRuleValue' value="<%=Constant.COMMON_RULE %>"/>
<c:set var="commonRule" value="<%=Constant.COMMON_RULE %>"></c:set>
	<table class="table_edit" id="vehicleInfo">
		<th colspan="6"><img class="nav" src="../../../img/subNav.gif"/> 车辆信息</th>
		<input type="hidden" id="id" value="${map.ID}"/>
		<input type="hidden" name="ctmId" value="${map.CTM_ID }"/>
		<tr>
			<td width="10%" align="right">VIN：</td>
			<td><c:out value="${map.VIN}"/><input type="hidden" value="${map.VIN}" name="vin"/></td>
			<td width="10%" align="right">制单人：</td>
			<td><c:out value="${map.APPLY_PERSON}"/></td>
			<td width="12%" align="right">制单时间：</td>
			<td><fmt:formatDate  value="${map.APPLY_DATE}" pattern="yyyy-MM-dd" /></td>
		</tr>
		<tr>
			<td width="10%" align="right">配件代码:</td>
			<td>${map.PART_CODE }</td>
			<td width="10%" align="right">配件名称:</td>
			<td>${map.PART_NAME }</td>
			<td align="right">制单公司:</td>
			<td>${map.COM_NAME }</td>
		</tr>
		<tr>
			<td align="right">车型：</td>
			<td><c:out value="${map.MODEL_NAME}"/></td>
			<td align="right">购车日期：</td>
			<td><c:out value="${map.PURCHASED_DATE}"/></td>
			<td></td>
			<td></td>
		</tr>
		<tr style="display:none;">
			<td align="right">产地：</td>
			<td><c:out value="${map.YIELDLY}"/></td>
			<td width="10%" align="right">车系：</td>
			<td><c:out value="${map.SERIES_NAME}"/></td>
			<td width="12%" align="right">车型：</td>
			<td><c:out value="${map.MODEL_NAME}"/></td>
		</tr>
		<tr style="display:none;">
			<td align="right">购车日期：</td>
			<td><c:out value="${map.PURCHASED_DATE}"/></td>
			<td align="right">行驶里程：</td>
			<td><c:out value="${map.MILEAGE}"/></td>
			<td align="right">保养次数：</td>
			<td><c:out value="${map.FREE_TIMES}"/></td>
		</tr>
		<tr style="display:none;">
			<td width="10%" align="right">车主姓名：</td>
			<td><c:out value="${map.CTM_NAME}"/></td>
			<td width="10%" align="right">车主电话：</td>
			<td><c:out value="${map.MAIN_PHONE}"/></td>
			<td width="12%" align="right">三包策略代码：</td>
			<td><c:out value="${map.GAME_CODE}"/></td>
		</tr>
	</table>
    <table class="table_edit" id="applyTable">
		<th colspan="9" align="left"><img class="nav" src="../../../img/subNav.gif" />申请内容</th>
		<c:if test="${map.APPLY_ID == 13141001}">
		<tr>
			<td align="right" nowrap="nowrap">申请类型：</td>
		  	<td align="left" colspan="2">
		  		<c:out value="${map.APPLY_TYPE}"/>
		  		<input type="hidden" value="${map.APPLY_ID}" name="changeType"/>
		  	</td>
		  	<td align="right">变更后数据：</td>	  	
            <td align="left" id="tdmileage">
            	<c:out value="${map.APPLY_DATA}"/>
            	<input type="hidden" value="${map.APPLY_DATA}" name="changeData"/>
            </td>
		</tr>
        <tr>
        	<td align="right">数据提报错误经销商：</td>
            <td colspan="2" align="left">
            	<c:out value="${map.DEALER_SHORTNAME}"/>
            </td>
            <td align="right">数据提报错类型：</td>
            <td colspan="2" align="left">
              	<c:out value="${map.ERROR_TYPE}"/>
            </td>
		</tr>
		</c:if>
		<c:if test="${map.APPLY_ID == 13141002}">
		<tr>
			<td align="right" nowrap="nowrap">申请类型：</td>
		  	<td align="left" colspan="2">
		  		<c:out value="${map.APPLY_TYPE}"/>
		  		<input type="hidden" value="${map.APPLY_ID}" name="changeType"/>
		  	</td>
		  	<td align="right">变更后数据：</td>	  	
            <td align="left" id="tdmileage">
            	<c:out value="${map.APPLY_DATA}"/>
            	<input type="hidden" value="${map.APPLY_DATA}" name="changeData"/>
            </td>
		</tr>
        <tr>
        	<td align="right">数据提报错误经销商：</td>
            <td colspan="2" align="left">
            	<c:out value="${map.DEALER_SHORTNAME}"/>
            </td>
            <td align="right">数据提报错类型：</td>
            <td colspan="2" align="left">
              	<c:out value="${map.ERROR_TYPE}"/>
            </td>
		</tr>
		</c:if>
		<c:if test="${map.APPLY_ID == 13141003}">
		<tr>
			<td align="right" nowrap="nowrap">申请类型：</td>
		  	<td align="left" colspan="1">
		  		<c:out value="${map.APPLY_TYPE}"/>
		  		<input type="hidden" value="${map.APPLY_ID}" name="changeType"/>
		  	</td>
		  	<td align="right">申请时间：</td>
		  		<td align="left">${map.CREATE_DATE}</td>
		</tr>
		<tr>
		  	<td align="right">变更后数据：</td>	  	
            <td align="left" id="tdmileage">
            	<fmt:formatDate value='${map.C_PURCHASE_DATE}' pattern='yyyy-MM-dd'/>
            	<input type="hidden" value="${map.C_PURCHASE_DATE}" name="changeData"/>
            </td>
		</tr>
		</c:if>
		<c:if test="${map.APPLY_ID == 13141005}">
		<input type="hidden" name="ctm_name" value="${map.C_CTM_NAME }"/>
		<input type="hidden" name="ctm_phone" value="${map.C_CTM_PHONE }"/>
		<input type="hidden" name="ctm_address" value="${map.C_CTM_ADDRESS }"/>
		<input type="hidden" value="${map.APPLY_ID}" name="changeType"/>
		<tbody id="ctm_info_chg">
			<tr>
				<td align="right">申请类型：</td>
		  		<td align="left"><c:out value="${map.APPLY_TYPE}"/></td>
			</tr>
			<tr>
				<td align="right">用户姓名：</td>
				<td>${map.C_CTM_NAME }</td>
				<td align="right">用户电话：</td>
				<td>${map.C_CTM_PHONE }</td>
			</tr>
			<tr>
				<td align="right">用户地址：</td>
				<td colspan="3">${map.C_CTM_ADDRESS }</td>
			</tr>
		</tbody>  
		</c:if> 
            <tr>
			  <td align="right">备注：</td>
			  <td colspan="6" align="left"><textarea id="remark" name="remark" cols="120" rows="3" readonly><c:out value="${map.APPLY_REMARK}"/></textarea></td>
		  </tr>
		</table>
		</br>
		<!-- 添加附件 开始  -->
        <table id="add_file" style="display:none" width="100%" class="table_info" border="0" id="file">
				<input type="hidden" id="fjids" name="fjids"/>
	    		<tr>
	        		<th>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
					</th>
				</tr>
				<tr>
    				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  					<%if(fileList!=null){for(int i=0;i<fileList.size();i++) { %>
	 					<script type="text/javascript">
    						addUploadRowByDbView('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
    					</script>
					<%}}%>
			</table> 
  		<!-- 添加附件 结束 -->
		<table class="table_edit" id="">
			<th colspan="9" align="left"><img class="nav" src="../../../img/subNav.gif" />审核内容</th>
			<tr>
			  <td align="right" nowrap="nowrap">审批备注：</td>
			  <td colspan="6" align="left"><textarea name="checkRemark" id="checkRemark" cols="120" rows="3"></textarea></td>
		  	</tr>
		</table>
		<table class="table_edit" id="gameRuleSel" border="1">
			<th colspan="6" align="left"><img class="nav" src="../../../img/subNav.gif" />更改规则</th>
				<tr>
					<td></td>
					<td>是否属通用三包规则:
					<c:choose>
							<c:when test="${map.RULE_CODE=='20060101'}">
								是
							</c:when>
							<c:otherwise>
								否
							</c:otherwise>
						</c:choose></td>
					<td>
					</td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr style="display: none">
				 <td align="right" nowrap="nowrap">
				  	
				  </td>
				  <td align="left">三包策略:${map.GAME_NAME }&nbsp;&nbsp;三包规则:${map.RULE_CODE }</td>
				  <td align='left'>原三包期(月)：</td>
				  <td align='left'>${map.WR_MONTH }</td>
				  <td align='left'>原三包里程：</td>
				  <td align='left'>${map.WR_MELIEAGE }</td>
			  	</tr>
			  	<tr>
			  	 <td align="right" nowrap="nowrap">
				  		<input type="checkbox" name='sysRule' id='sysRule' onclick="sysRuleCheckRule();"/>
				  	
				  </td>
				  <td align="left" nowrap="nowrap">三包策略:${map.GAME_NAME }&nbsp;&nbsp;三包规则:${map.RULE_CODE }</td>
				  <td align='left'>更改为三包期(月)：</td>
				  <td align='left'><input class="short_txt" type=text name='changeSysClaimMonth' id='changeSysClaimMonth' value='${map.CLAIM_MONTH_NEW }'/></td>
				  <td align='left'>更改为三包里程：</td>
				  <td align='left'><input class='short_txt' type=text name='changeSysClaimMelieage' id='changeSysClaimMelieage' value='${map.CLAIM_MELIEAGE_NEW }'/></td>
			  	</tr>
		  	<tr style="display: none">
		  	  <td align="right" nowrap="nowrap">
		  	 </td>
			  <td align="left">通用三包规则:</td>
			  <td align='left'>三包期(月)：</td>
			  <td align='left'>${usualMap.WR_MONTH }</td>
			  <td align='left'>三包里程：</td>
			  <td align='left'>${usualMap.WR_MELIEAGE }</td>
		  	</tr>
		  	<tr>
		  	  <td align="right" nowrap="nowrap">
		  	 <c:choose>
		  	 	<c:when test="${empty usualMap}">
		  	 	新增	
		  	 	</c:when>
		  	 	<c:otherwise>	  	 		
		  	 	</c:otherwise>
		  	 </c:choose> 
		  	 <input  type="checkbox" name='usualRule' id='usualRule' onclick="javascript:getGame();"/>
		  	 
		  	 </td>
			  <td align="left" nowrap="nowrap">通用三包规则:</td>
			  <td align='left'>更改为三包期(月)：</td>
			  <td align='left'><input class='short_txt' type=text name='changeUsualClaimMonth' id='changeUsualClaimMonth' value=''/></td>
			  <td align='left'>更改为三包里程：</td>
			  <td align='left'><input class='short_txt' type=text name='changeUsualClaimMelieage' id='changeUsualClaimMelieage' value=''/></td>
		  	</tr>
		  	<tr>
		  	  <td align="right" nowrap="nowrap"><input type="checkbox" name='sysGame' id='sysGame' onclick="javascript:sysGameCheckRule();"/></td>
			  <td align="left">修改三包策略:<select id='gameSel' name='gameSel' onchange="javascript:getRuleByGame();" class='long_sel'>
			  	<option value=''/>-请选择-
			  	<c:forEach items="${listGame}" var="game">
			  		<option value='${game.GAME_ID }'/>${game.GAME_NAME }
			  	</c:forEach>
			  </select></td>
			  <td align='left'>三包期(月)：</td>
			  <td align='left'><input class='short_txt' type=text name='claimMonth' id='claimMonth'/></td>
			  <td align='left'>三包里程：</td>
			  <td align='left'><input class='short_txt' type=text name='claimMelieage' id='claimMelieage'/></td>
		  	</tr>
		  	<tr>
		  		<td>&nbsp;</td>
		  		<td>&nbsp;</td>
		  		<td>&nbsp;</td>
		  		<td>&nbsp;</td>
		  		<td>&nbsp;</td>
		  		<td>&nbsp;</td>
		  	</tr>
		  	<tr id="partTrMod">
		  	  <td align="right" nowrap="nowrap">&nbsp;</td>
			  <td align="left"><font color="red">配件是否授权</font>：
			  	<c:choose>
			  		<c:when test="${monFlag==0}">
			  			<font color='red'><c:out value="否"></c:out></font>
			  		</c:when>
			  		<c:otherwise>
			  			<font color='red'><c:out value="是"></c:out></font>
			  		</c:otherwise>
			  	</c:choose>
			  <input type="button" onClick="add()" class="normal_btn"  style="width=8%" value="维  护"/></td>
			       <td class="table_query_3Col_label_6Letter">是否回运：</td>
      <td class="table_query_3Col_input">
      		<select id="isReturn" name="isReturn">
      			<c:if test="${dealerInfo.IS_RETURN == 0}">
      				<option value="0" selected>否</option>
      				<option value="1">是</option>
      			</c:if>
      			<c:if test="${dealerInfo.IS_RETURN == 1}">
      				<option value="0">否</option>
      				<option value="1" selected>是</option>
      			</c:if>
      		</select>
      </td>
       <td class="table_query_3Col_label_6Letter">是否新件：</td>
      <td class="table_query_3Col_input">
      		<select id="IS_NEW_PART" name="IS_NEW_PART">
      			<c:if test="${dealerInfo.IS_NEW_PART == 0}">
      				<option value="0" selected>否</option>
      				<option value="1">是</option>
      			</c:if>
      			<c:if test="${dealerInfo.IS_NEW_PART == 1}">
      				<option value="0">否</option>
      				<option value="1" selected>是</option>
      			</c:if>
      		</select>
      </td>
		  	</tr>
		</table>
		<br/>
		<table width=100% border="0" cellspacing="0" cellpadding="0">
  			<tr>
             	<td height="12" align=center width="33%">
                	<input type="button" onClick="agree()" class="normal_btn"  style="width=8%" value="同意"/>
                	&nbsp;
                	<input type="button" onclick="reject()" class="normal_btn"  style="width=8%" value="退回"/>
                	&nbsp;
					<input type="button" onClick="javascript:history.go(-1);" class="normal_btn"  style="width=8%" value="返回"/>
    			</td>
        	</tr>
		</table>
	</form>
</body>
</html>
<script type="text/javascript">
	//窗口柄
	var winHandle;
	function getGame(){
		var url = '<%=contextPath%>/vehicleInfoManage/check/VehicleInfoChangeCheck/getGameBySomething.json';
		makeNomalFormCall(url,callBack,"fm");
	}
	function callBack(obj){
		var listGame = obj.listGame;
		var gameSel = $('gameSel');
		gameSel.length = 0;
		gameSel.options.add(new Option("-请选择-",""));
		for(var i=0;i<listGame.length;i++){
			gameSel.options.add(new Option(listGame[i].GAME_NAME,listGame[i].GAME_ID.toString())); 
		}
	} 
	function getRuleByGame(){
		var gameSel = $('gameSel');
		var sysGame = $('sysGame');
		var sysRule = $('sysRule');
		var usualRule = $('usualRule');
		var claimMonth = $('claimMonth');
		var claimMelieage = $('claimMelieage');
		if(gameSel.value==''){
			if(sysGame.checked){
				sysGame.checked='';
			}
			claimMonth.value='';
			claimMelieage.value='';
			sysRule.disabled='';
			usualRule.disabled='';
			
			return;
		}else{
			sysGame.checked='checked';
		}
		var url = '<%=contextPath%>/vehicleInfoManage/check/VehicleInfoChangeCheck/getRuleDetailFromGame.json';
		makeNomalFormCall(url,getRuleByGameBack,"fm");
	}
	function getRuleByGameBack(obj){
		var mapRuleDetail = obj.mapRuleDetail;
		var mapRuleCode = obj.mapRuleCode;
		var usualRule = $('usualRule');
		var sysRule = $('sysRule');
		var sysGame = $('sysGame');
		var gameSel = $('gameSel');
		var claimMonth = $('claimMonth');
		var claimMelieage = $('claimMelieage');
		if(mapRuleDetail!=null){
			claimMonth.value=mapRuleDetail.CLAIM_MONTH;
			claimMelieage.value=mapRuleDetail.CLAIM_MELIEAGE;
		}else{

			claimMonth.value='';
			claimMelieage.value='';
		}
		//下拉框选择为通用三包规则时
		if(mapRuleCode.RULE_CODE==$('commonRuleValue').value){
			usualRule.checked='';
			usualRule.disabled="disabled";
		}else{
			usualRule.disabled="";
		}
		//系统三包规则判断
		if(sysGame.checked){
			sysRule.checked='';
			sysRule.disabled='disabled';
		}else{
			sysRule.disabled='';
		}
		
	} 
	function sysGameCheckRule(){
		
		var usualRule = $('usualRule');
		var sysRule = $('sysRule');
		var sysGame = $('sysGame');
		var gameSel = $('gameSel');
		var claimMonth = $('claimMonth');
		var claimMelieage = $('claimMelieage');
		if(sysGame.checked){
			usualRule.checked='';
			sysRule.checked='';
			sysRule.disabled='disabled';
			usualRule.disabled='disabled';
		}else{
			sysRule.disabled='';
			usualRule.disabled='';
			claimMonth.value='';
			claimMelieage.value='';
			gameSel.options[0].selected='selected';
		}
	}
	function sysRuleCheckRule(){
		var usualRule = $('usualRule');
		var sysRule = $('sysRule');
		var sysGame = $('sysGame');
		var gameSel = $('gameSel');
		var claimMonth = $('claimMonth');
		var claimMelieage = $('claimMelieage');
		if(sysRule.checked){
			sysGame.checked='';
			sysGame.disabled='disabled';
			gameSel.options[0].selected='selected';
			//当系统三包规则为通用规则时
			if($('ruleCode').value==$('commonRuleValue').value){
				usualRule.checked='';
				usualRule.disabled='disabled';
			}
		}else{
			sysGame.disabled='';
			gameSel.options[0].selected='selected';
			usualRule.disabled='';
		}
	}
//同意
function agree() {
	//var checkRemark = document.getElementById("checkRemark").value;
	//if (!checkRemark.trim()) {
	//	MyAlert("请填写审批备注");
	//	return ;
	//}
	if(!$('sysRule').checked&&!$('usualRule').checked&&!$('sysGame').checked){
		MyAlert('请选择一个修改的规则！');
		return;
	}
	if($('sysRule').checked){
		if($('changeSysClaimMonth').value==''){
			MyAlert('请输入修改的三包期！');
			return;
		}
		if($('changeSysClaimMelieage').value==''){
			MyAlert('请输入修改的三包里程！');
			return;
		}
	}
	if($('usualRule').checked){
		if($('changeUsualClaimMonth').value==''){
			MyAlert('请输入修改的通用三包期！');
			return;
		}
		if($('changeUsualClaimMelieage').value==''){
			MyAlert('请输入修改的通用三包里程！');
			return;
		}
	}
	if($('sysGame').checked){
		if($('gameSel').value==''){
			MyAlert('请选择一个要修改的规则！');
			return;
		}
		if($('claimMonth').value==''){
			MyAlert('请输入修改的三包期！');
			return;
		}
		if($('claimMelieage').value==''){
			MyAlert('请输入修改的三包里程！');
			return;
		}
	}
	
	MyConfirm('确认同意三包规则修改！',agreeDo);
	$('checkFlag').value='0';
	//check(0);
}
function agreeDo(){
	fm.method = 'post';
	var url = '<%=contextPath%>/vehicleInfoManage/check/VehicleInfoChangeCheck/doRuleChangeCheck.do';
	fm.action = url
	fm.submit();
}
//退回
function reject() {
	var checkRemark = document.getElementById("checkRemark").value;
	if (!checkRemark.trim()) {
		MyAlert("请填写审批备注");
		return ;
	}
	$('checkFlag').value='1';
	
	/*if(!$('sysRule').checked&&!$('usualRule').checked&&!$('sysGame').checked){
		MyAlert('请选择一个修改的规则！');
		return;
	}
	if($('sysRule').checked){
		if($('changeSysClaimMonth').value==''){
			MyAlert('请输入修改的三包期！');
			return;
		}
		if($('changeSysClaimMelieage').value==''){
			MyAlert('请输入修改的三包里程！');
			return;
		}
	}
	if($('usualRule').checked){
		if($('changeUsualClaimMonth').value==''){
			MyAlert('请输入修改的通用三包期！');
			return;
		}
		if($('changeUsualClaimMelieage').value==''){
			MyAlert('请输入修改的通用三包里程！');
			return;
		}
	}
	if($('sysGame').checked){
		if($('claimMonth').value==''){
			MyAlert('请输入修改的三包期！');
			return;
		}
		if($('claimMelieage').value==''){
			MyAlert('请输入修改的三包里程！');
			return;
		}
	}*/
	MyConfirm('确认拒绝此次三包规则修改！',check);
}
//审核操作 0:同意 1:退回
function check() {
	fm.method = 'post';
	var url = '<%=contextPath%>/vehicleInfoManage/check/VehicleInfoChangeCheck/doRuleChangeCheck.do';
	fm.action = url
	fm.submit();
}
//新增监控配件
  function add(){
  
  		//var tarUrl = "<%=contextPath%>/claim/authorization/ClaimPartWatchMain/claimPartWatchAddInitVehRuleChgJC.do";
		//var width=900;
		//var height=500;

		//var screenW = window.screen.width-30;	
		//var screenH = document.viewport.getHeight();

		//if(screenW!=null && screenW!='undefined')
		//	width = screenW;
		//if(screenH!=null && screenH!='undefined')
		//	height = screenH;
		//if(fm.submit()){
		
		//	OpenHtmlWindow(tarUrl,900,800)
		//}
	var changeId = $('changeId').value;
	var partCode = $('partCode').value;
	var vin = $('vin').value;
	var url = "<%=contextPath%>/claim/authorization/ClaimPartWatchMain/claimPartWatchAddInitVehRuleChgJC.do?changeId="+changeId+"&partCode="+partCode+"&vin="+vin;
  	openWindowDialog(url);
    //fm.action ="<%=contextPath%>/claim/authorization/ClaimPartWatchMain/claimPartWatchAddInitVehRuleChgJC.do";
    //fm.submit();    
  }
 //页面初始化判断系统
 function initPage(){
 	var partTrMod = document.getElementById('partTrMod');
 	if($('code').value=='<%=Constant.chana_jc%>'){
 		
 		partTrMod.style.display="";
 	}else{
 		partTrMod.style.display="none";
 	}
 }
 	  function openWindowDialog(targetUrl){
		  var height = 800;
		  var width = 1000;
		  var iTop = (window.screen.availHeight-30-height)/2;  //获得窗口的垂直位置      
		  var iLeft = (window.screen.availWidth-10-width)/2;   //获得窗口的水平位置
		  var params = "width="+width+",height="+height+",top="+iTop+",left="+iLeft+",scrollbars=yes,resizable=yes,status=no";
		  winHandle = window.open(targetUrl,null,params);
		  return winHandle;
	  }
</script>

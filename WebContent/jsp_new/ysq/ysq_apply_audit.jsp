 <!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<head> 
<%  String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<title>服务活动管理</title>	
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>

<script type="text/javascript">
	$(function(){
		var type=$("#type").val();
		if("tech"==type || "director"==type){
			$("input[type='text']").attr("readonly","readonly");
			$("select").attr("disabled",true);
			$("textarea").attr("readonly","readonly");
			$("#text_remark").removeAttr("readonly","readonly");
			$("#chooseMainPart").hide();
			$("#chooseProducer").hide();
			$("#com_pass").removeAttr("readonly","readonly");
			$("#bc_pass").removeAttr("readonly","readonly");
			$(".normal_btn").each(function(){
				 if($(this).val()=="删 除" || $(this).val()=="添加附件"){
					 $(this).hide();
				} 
			});
		}
		if("engine"==type){
			$("input[type='text']").attr("readonly","readonly");
			$("select").attr("disabled",true);
			$("textarea").attr("readonly","readonly");
			$("#text_remark").removeAttr("readonly","readonly");
			$("#chooseMainPart").hide();
			$("#chooseProducer").hide();
			$("#rebut").hide();
			$(".normal_btn").each(function(){
				 if($(this).val()=="删 除" || $(this).val()=="添加附件"){
					 $(this).hide();
				} 
			});
		}
	});
	function auditThis(){
		var id=$("#id").val();
		var url='<%=contextPath%>/YsqAction/jugeYsq.json?id='+id;
		makeFormCall(url,function(json){
			MyConfirm(json.msg,auditPass,"");
		},'fm');
	}
	function auditPass(){
		var id=$("#id").val();
		var type=$("#type").val();
		var com_pass=$("#com_pass").val();
		var bc_pass=$("#bc_pass").val();
		var url='<%=contextPath%>/YsqAction/auditYsq.json?id='+id+'&type='+type+'&bc_pass='+bc_pass+'&com_pass='+com_pass;
		makeNomalFormCall1(url,function(json){
			if(json.succ=="1"){
    			MyAlert("提示：审核成功！");
    			var type=$("#type").val();
    			if("tech"==type){
    				window.location.href = "<%=contextPath%>/YsqAction/ysqTechList.do";
    			}
				if("director"==type){
					window.location.href = "<%=contextPath%>/YsqAction/ysqTechDirectorList.do";
				}
    			if("engine"==type){
    				window.location.href = "<%=contextPath%>/YsqAction/ysqEngineList.do";
    			}
    		}else{
    			MyAlert("提示：审核失败！");
    		}
		},'fm');
	}
	function rebutThis(){
		var text=$.trim($("#text_remark").val());
		if(""==text){
			MyAlert("提示：驳回请填写内容！");
			return;
		}
		MyConfirm("提示：驳回操作将直接打回到服务站！",auditRebut,"");
	}
	function auditRebut(){
		var id=$("#id").val();
		var type=$("#type").val();
		var com_pass=$("#com_pass").val();
		var bc_pass=$("#bc_pass").val();
		var type=$("#type").val();
		var status="";
		if("tech"==type){
			status=93461004;
		}
		if("director"==type){
			status=93461006;
		}
		var url='<%=contextPath%>/YsqAction/auditRebut.json?id='+id+'&type='+type+'&bc_pass='+bc_pass+'&com_pass='+com_pass+'&status='+status;
		makeNomalFormCall1(url,function(json){
			if(json.succ=="1"){
    			MyAlert("提示：驳回成功！");
    			var type=$("#type").val();
    			if("tech"==type){
    				window.location.href = "<%=contextPath%>/YsqAction/ysqTechList.do";
    			}
				if("director"==type){
					window.location.href = "<%=contextPath%>/YsqAction/ysqTechDirectorList.do";
				}
    			if("engine"==type){
    				window.location.href = "<%=contextPath%>/YsqAction/ysqEngineList.do";
    			}
    		}else{
    			MyAlert("提示：驳回失败！");
    		}
		},'fm');
	}

	function RefusalThis(){
		var text=$.trim($("#text_remark").val());
		if(""==text){
			MyAlert("提示：拒赔请填写内容！");
			return;
		}
		MyConfirm("提示：拒赔操作将直接打回到服务站！",auditRefusal,"");

	}
	function auditRefusal(){
		var id=$("#id").val();
		var type=$("#type").val();
		var com_pass=$("#com_pass").val();
		var bc_pass=$("#bc_pass").val();
		var type=$("#type").val();
		var status="";
		if("tech"==type){
			status=93461010;//技术室人员审核拒赔
		}
		var url='<%=contextPath%>/YsqAction/auditRebut.json?id='+id+'&type='+type+'&bc_pass='+bc_pass+'&com_pass='+com_pass+'&status='+status;
		makeNomalFormCall1(url,function(json){
			if(json.succ=="1"){
    			MyAlert("提示：拒赔成功！");
    			var type=$("#type").val();
    			if("tech"==type){
    				window.location.href = "<%=contextPath%>/YsqAction/ysqTechList.do";
    			}
				if("director"==type){
					window.location.href = "<%=contextPath%>/YsqAction/ysqTechDirectorList.do";
				}
    			if("engine"==type){
    				window.location.href = "<%=contextPath%>/YsqAction/ysqEngineList.do";
    			}
    		}else{
    			MyAlert("提示：操作失败,请尝试重新审核!");
    		}
		},'fm');
	}
	//维修历史按钮方法
	function maintaimHistory(){
		var vin = $("#vin").val() ;
		if(""==vin){
			MyAlert("提示：请输入正确的vin");
			return;
		}
		window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN='+vin);
	}
	//保养历史按钮方法
	function freeMaintainHistory(){
		var vin = $("#vin").val() ;
		if(""==vin){
			MyAlert("提示：请输入正确的vin");
			return;
		}
		window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/freeMaintainHistory.do?VIN='+vin);
	}
	function showWorkFlow(){
		var id=$("#id").val();
		OpenHtmlWindow('<%=contextPath%>/YsqAction/showWorkFlow.do?id='+id,800,500);
	}
</script>
</head>

<form method="post" name="fm" id="fm">
<body>
<div class="navigation"><img class="nav" src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔预授权&gt;新增索赔单预授权</div>
<input name="type" id="type" value="${type}" type="hidden" />
<input type = "hidden"  value = "${t.ID }" id ="id" name="id" />
<input type = "hidden"  value = "${t.RELATION_RO }" id ="relation_ro" name="relation_ro" />
<input type = "hidden"  value = "${t.SERIES_ID }" id ="series_id" name ="series_id" />
<input type = "hidden"  value = "${t.MODEL_ID }" id ="model_id" name ="model_id" />
<input type = "hidden"  value = "${t.PART_ID }" id ="part_id" name ="part_id" />
<input type = "hidden" value = "${t.PRODUCER_NAME }" id ="producer_name" name ="producer_name"/>
<input type = "hidden" value = "${t.MILEAGE }" id ="vin_mileage" name ="vin_mileage"/>
<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<tr>
		<th colspan="8">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息
		</th>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" ><span style="COLOR: red">*</span>预授权号：</td>
    	<td nowrap="true" width="15%" >
    		<input type="text" readonly="readonly" name="ysq_no" id="ysq_no" value="${t.YSQ_NO }" maxlength="50" class="middle_txt"/>
    	</td>
		<td nowrap="true" width="10%" >车型：</td>
    	<td nowrap="true" width="15%" >
			 <input  readonly="readonly"  id="model_name" value="${v.MODEL_NAME }" type="text" class="middle_txt" >
    	</td>
    	<td nowrap="true" width="10%" >服务站代码：</td>
    	<td nowrap="true" width="15%" >
			<input  readonly="readonly" value="${u.DEALER_CODE }" type="text" class="middle_txt" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" ><span style="COLOR: red">*</span>VIN：</td>
    	<td nowrap="true" width="15%" >
    		<input type="text" name="vin" id="vin" value="${t.VIN }" maxlength="50"  class="middle_txt"/>
    	</td>
		<td nowrap="true" width="10%" >配置：</td>
    	<td nowrap="true" width="15%" >
    		<input  readonly="readonly" id="package_name" value="${v.PACKAGE_NAME }" type="text" class="middle_txt" >
    	</td>
    	<td nowrap="true" width="10%" >服务站简称：</td>
    	<td nowrap="true" width="15%" >
			<input  readonly="readonly" value="${u.DEALER_SHORTNAME }" type="text" class="middle_txt" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >进厂里程：</td>
    	<td nowrap="true" width="15%" >
    		<input  id="mileage" name="mileage"  onblur="checkMileage(this);" value="${t.MILEAGE }" type="text" class="middle_txt" maxlength="10" >
    	</td>
		<td nowrap="true" width="10%" >颜色：</td>
    	<td nowrap="true" width="15%" >
			<input  readonly="readonly" id="color" value="${v.COLOR }" type="text" class="middle_txt" >
    	</td>
    	<td nowrap="true" width="10%" >索赔员：</td>
    	<td nowrap="true" width="15%" >
    	 	<input  readonly="readonly" value="${u.NAME }" type="text" class="middle_txt" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >三包预警：</td>
    	<td nowrap="true" width="15%" >
    		<input  readonly="readonly" id="vrLevel" value="${vrLevel}" type="text" class="middle_txt" >
    	</td>
		<td nowrap="true" width="10%" >车辆用途：</td>
    	<td nowrap="true" width="15%" >
 			<input  readonly="readonly" id="car_use_desc" value="${v.CAR_USE_DESC }" type="text" class="middle_txt" >
     	</td>
    	<td nowrap="true" width="10%" >索赔员电话：</td>
    	<td nowrap="true" width="15%" >
    		 <input  readonly="readonly" value="${u.HAND_PHONE }" type="text" class="middle_txt" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >发动机号：</td>
    	<td nowrap="true" width="15%" >
    		<input  readonly="readonly" id="engine_no" value="${v.ENGINE_NO }" type="text" class="middle_txt" >
    	</td>
		<td nowrap="true" width="10%" >购车日期：</td>
    	<td nowrap="true" width="15%" >
			<input type='text' id='invoice_date'  class="middle_txt" readonly="readonly" value ="<fmt:formatDate value="${v.INVOICE_DATE }" type="date" dateStyle="full" pattern="yyyy-MM-dd"/>"/>
					
    	</td>
    	<td nowrap="true" width="10%" ></td>
    	<td nowrap="true" width="15%" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
</table>
<br>
 <table  border="0" align="center" cellpadding="0"cellspacing="1" class="table_list"style="border-bottom: 1px solid #DAE0EE">
				<th colspan="11" align="left">
					<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
					作业项目
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						维修类型
					</td>
					<td>
						主损件代码
					</td>
					<td>
						主损件名称
					</td>
					<td>
						供应商代码
					</td>
					<td>
						最大预估金额
					</td>
					<td>
						是否回运
					</td>
					<td>
						关联工单
					</td>
					
				</tr>
				<tr align="center" class="table_list_row1">
					<td>
						<script type="text/javascript">
			         		genSelBoxExp("claim_type",<%=Constant.CLA_TYPE%>,"${t.CLAIM_TYPE}",true,"short_sel","onchange=changeIsRelation(this)","false",'10661002,10661003,10661004,10661005,10661008,10661009,10661011');
			         	 </script>
					</td>
					<td>
						<input type="text" name="part_code" class="middle_txt"   id="part_code" value="${t.PART_CODE }" datatype="0,is_null" readonly="readonly" /><input type="button" onClick="addPart();" id="chooseMainPart" name="chooseMainPart"  class="mini_btn" value="..." />
					</td>
					<td>
						<input type="text" name="part_name" class="middle_txt"   id="part_name" value="${t.PART_NAME }" datatype="0,is_null"  readonly="readonly"/>
					</td>
					<td>
						<input type="text" name="producer_code" class="short_txt"   id="producer_code" value = "${t.PRODUCER_CODE }" datatype="0,is_null" readonly="readonly" /><input type="button" onClick="choose_producer_code();" id="chooseProducer" name="chooseProducer"  class="mini_btn" value="..." />
					</td>
					<td>
						<input type="text" name="max_estimate" class="short_txt" value="${t.MAX_ESTIMATE }"  id="max_estimate" datatype="1,isMoney,10" />
					</td>
					<td>
				       	<script type="text/javascript">
				       	genSelBoxExp("is_return",<%=Constant.IS_RETURN%>,"${t.IS_RETURN }",true,"short_sel","","false",'');
				        </script>
					</td>
					<td>
						<input type="button" onClick="selectOrder();" id="chooseOrder" class="normal_btn" disabled="disabled" value="关联工单" />
					</td>
			</tr>
</table>
<br>
<table border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">
			<th colspan="6">
				<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
			申请内容
			</th>
			<tr>
				<td  nowrap="true">
					故障描述：
				</td>
				<td  nowrap="true">
					<textarea name='trouble_reason' style="font-weight: bold;" id="trouble_reason"	datatype="1,is_textarea,1000" maxlength="1000" rows='5' cols='40'>${t.TROUBLE_REASON }</textarea>
				</td>
				
				<td  nowrap="true">
					原因分析及处理结果：
				</td>
				<td  nowrap="true">
					<textarea name="trouble_desc" id="trouble_desc" style="font-weight: bold;" datatype="1,is_textarea,1000" maxlength="1000" rows='5' cols='40'>${t.TROUBLE_DESC }</textarea>
				</td>
			</tr>
</table>
<table border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">
			<th colspan="6">
				<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
			补偿费用
			</th>
			<tr>
				<td  nowrap="true">
					&nbsp;补偿申请金额:&nbsp;&nbsp;&nbsp;<input type="text" name="com_apply" class="short_txt"   id="com_apply" value="${t.COM_APPLY }" datatype="1,isDigitAll,10"  maxlength="10" />
				</td>
				<td class="pass_show" nowrap="true">
					&nbsp;补偿审核金额:&nbsp;&nbsp;&nbsp;<input type="text" name="com_pass" class="short_txt"   id="com_pass" value="${t.COM_PASS }" datatype="1,isDigitAll,10"  readonly="readonly" maxlength="10" />
				</td>
				<td  nowrap="true">
					&nbsp;补偿申请备注:&nbsp;&nbsp;&nbsp;<input type="text" name="com_remark" class="long_txt"   id="com_remark" value="${t.COM_REMARK }"  maxlength="300" />
				</td>
			</tr>
			<th colspan="6">
				<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
			背车费用
			</th>
			<tr>
				<td  nowrap="true">
					&nbsp;背车申请金额:&nbsp;&nbsp;&nbsp;<input type="text" name="bc_apply" class="short_txt"   id="bc_apply" value="${t.BC_APPLY }" datatype="1,isMoney,10"  maxlength="10" />
				</td>
				<td class="pass_show"  nowrap="true">
					&nbsp;背车审核金额:&nbsp;&nbsp;&nbsp;<input type="text" name="bc_pass" class="short_txt"   id="bc_pass" value="${t.BC_PASS }" datatype="1,isMoney,10"  readonly="readonly"  maxlength="10" />
				</td>
				<td  nowrap="true">
					&nbsp;背车申请备注:&nbsp;&nbsp;&nbsp;<input type="text" name="bc_remark" class="long_txt"   id="bc_remark" value="${t.BC_REMARK }"  maxlength="300" />
				</td>
			</tr>
</table>
</br>
<!-- 添加附件 开始  -->
        <table id="add_file"  width="75%" class="table_info" border="0" id="file">
	    		<tr>
	        		<th>
						<input type="hidden" id="fjids" name="fjids"/>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
			     		<input type="button" class="normal_btn" align="right" id="addfile" onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
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
<table border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit" >
			<th colspan="6">
				<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
			审核填写
			</th>
			<tr>
				<td  nowrap="true">
					填写内容：
				</td>
				<td  nowrap="true">
					<textarea name="text_remark" id="text_remark" style="font-weight: bold;" datatype="1,is_textarea,1000" maxlength="1000" rows='3' cols='125'></textarea>
				</td>
			</tr>
</table>
<br>
<table width=100% border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="12" align=left width="33%">&nbsp;</td>
             	<td height="12" align=center width="33%" nowrap="true">
	             	<input class="normal_btn" type="button" id="btn1" value="维修历史" onclick="maintaimHistory();"/>
	             	&nbsp;&nbsp;
	                <input class="normal_btn" type="button" id="btn3" value="保养历史" onclick="freeMaintainHistory();"/>
	                &nbsp;&nbsp;
                	<input type="button" class="normal_btn" id="audit" onClick="auditThis();"  style="width=8%" value="审核 " />
                	&nbsp;&nbsp;
                	<input type="button" class="normal_btn" id="rebut" onClick="rebutThis();" style="width=8%" value="驳回 "/>
                	&nbsp;&nbsp;
                	<input type="button" class="normal_btn" id="refusal" onClick="RefusalThis();" style="width=8%" value="拒赔"/>
                	&nbsp;&nbsp;
					<input type="button" onClick="showWorkFlow();" class="normal_btn"  style="width=8%" value="授权记录"/>
                	&nbsp;&nbsp;
					<input type="button" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
    			</td>
            	<td height="12" align=center width="33%">
      			</td>
			</tr>
		</table>
</form>
</body>
</html>
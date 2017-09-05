<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="com.infodms.dms.po.TtAsWrGamefeePO"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TcCodePO"%>
<%@page import="com.infodms.dms.po.TtAsWrInformationqualityExtPO"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
	TtAsWrInformationqualityExtPO tawep = (TtAsWrInformationqualityExtPO)request.getAttribute("info");
%>
	<head>
		<script type="text/javascript">
			var infoIdSub = '<%=tawep.getId()%>';
			var billNo = '<%=tawep.getBillNo() %>';
			var isModify = false; //不是修改入口
			function doInit()
			{
			   	loadcalendar();
			}
			//确认提交
			function confirmAdd() {
				if (!isModify) { //不是修改
				if(submitForm('fm')){
					MyDivConfirm("是否添加？",add,[]);
				}
				}else {
					MyDivConfirm("是否修改？",modify,[]);
				}
			}
			//提交函数
			function modify() {
				var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/infoUpdate.json?infoSubId='+infoIdSub+'&billNo='+billNo;
				//makeCall(url,addBack,'fm','queryBtn');
				makeNomalFormCall(url,modifyBack,'fm','queryBtn');
			}
			//提交回调函数
			function addBack(json) {
		    	var last=json.success;
		    	if (last){
		    		MyAlert("添加成功！");
	    			if (parent.$('inIframe')) {
	 					parentContainer.infoId=json.infoId;
	 					parent._hide();
		 			} else {
						parent.infoId=json.infoId;
						_hide();
					}
		    	}else {
		    		MyAlert("添加失败，请与管理员联系！");
		    	}
		    }
		    //提交回调函数
			function modifyBack(json) {
		    	var last=json.success;
		    	if (last){
		    		MyAlert("修改成功！");
	    			if (parent.$('inIframe')) {
	 					parentContainer.infoId=json.infoId;
	 					parent._hide();
		 			} else {
						parent.infoId=json.infoId;
						_hide();
					}
		    	}else {
		    		MyAlert("修改失败，请与管理员联系！");
		    	}
		    }
			//提交函数
			function add() {
				var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/infoInsert.json';
				//makeCall(url,addBack,'fm','queryBtn');
				makeNomalFormCall(url,addBack,'fm','queryBtn');
			}
			//提交回调函数
			function addBack(json) {
		    	var last=json.success;
		    	if (last){
		    		MyAlert("添加成功！");
	    			if (parent.$('inIframe')) {
	 					parentContainer.infoId=json.infoId;
		 			} else {
						parent.infoId=json.infoId;
					}
		    	}else {
		    		MyAlert("添加失败，请与管理员联系！");
		    	}
		    }
		    //校验工单和行号在索赔单表中是否有重复
	function oneVIN() {
		var vin = document.getElementById("VIN").value;
		var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getDetailByVin.json';
		var pattern=/^([A-Z]|[0-9]){17,17}$/;
		if(pattern.exec(vin)) {
			if (vin!=null&&vin!='') {
	    		makeCall(url,oneVINBack,{vinParent:vin});
	    	}
		}else {
			document.getElementById("MODEL_NAME").innerHTML = '' ;
			document.getElementById("MODEL_CODE").value= '';
			document.getElementById("ENGINE_NO").value = '';
			document.getElementById("CUSTOMER_NAME").value= '';
			
			document.getElementById("MILEAGE").value =  '';
			document.getElementById("FACTORY_DATE").value = '';
			document.getElementById("PURCHASED_DATE").value='';
			MyAlert("输入的不是有效VIN格式！");
		}
	}
	//null返回''
	function getNull(data) {
		if (data==null) {
			return '';
		}else {
			return data;
		}
	}
	//回调函数
	function oneVINBack(json) {
    	var last=json.ps.records;
    	var size=last.length;
    	var record;
    	if (size>0) {
    		record = last[0];
    		document.getElementById("VIN").value =getNull(record.vin) ;
			//document.getElementById("BRAND_NAME").innerHTML = getNull(record.brandName) ;
			//document.getElementById("SERIES_NAME").innerHTML = getNull(record.seriesName) ;
			document.getElementById("MODEL_NAME").innerHTML = getNull(record.modelName) ;
			document.getElementById("MODEL_CODE").value = getNull(record.modelCode) ;
			document.getElementById("ENGINE_NO").value = getNull(record.engineNo) ;
			document.getElementById("CUSTOMER_NAME").value= getNull(record.customerName);
			//document.getElementById("BRAND_NAME0").value = getNull(record.brandName) ;
			//document.getElementById("SERIES_NAME0").value = getNull(record.seriesName) ;
			//document.getElementById("BRAND_CODE0").value = getNull(record.brandCode) ;
			//document.getElementById("SERIES_CODE0").value = getNull(record.seriesCode) ;
			//document.getElementById("MODEL_NAME0").value = getNull(record.modelName) ;
			//document.getElementById("MODEL_CODE0").value = getNull(record.modelCode) ;
			//document.getElementById("ENGINE_NO0").value = getNull(record.engineNo) ;
			
			document.getElementById("MILEAGE").value =  getNull(record.mileage);
			document.getElementById("FACTORY_DATE").value = formatDate(getNull(record.factoryDate) );
			document.getElementById("PURCHASED_DATE").value= formatDate(getNull(record.purchasedDate) );
    	}
    }
    function formatDate(value){
    	if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
    }
    //vin码焦点离开调用函数
    function blurBack() {
    	oneVIN();
    }
		</script>
	</head>
	<body onload="doInit()">
		<div class="navigation">
			<img src="../../../img/nav.gif" />
			&nbsp;当前位置：质量信息质量跟踪卡&gt;质量信息质量跟踪卡
		</div>
		<form method="post" name="fm" id="fm">
		<input type="hidden" name="claimId" value="${claimId}"/>
		<table border="0" align="center" cellpadding="1" cellspacing="1"
				class="table_edit">
			<th colspan="6">
					<img class="nav" src="../../../img/subNav.gif" />
					基本信息
			</th>
			<tr id="BILL_NO">
				<td class="table_edit_2Col_label_7Letter">
					单据编号：
				</td>
				<td align="left" colspan="3">
					<%=CommonUtils.checkNull(tawep.getBillNo()) %>
				</td>
			</tr>
			<tr>
					<td class="table_edit_2Col_label_7Letter">
						信息发出单位编号：
					</td>
					<td align="left">
						<input type="text" name="DEALER_CODE" id="DEALER_CODE" value="${dealerCode}" readonly/>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						投诉类型：
					</td>
					<td align="left">
					<script type="text/javascript">
	              		genSelBoxExp("COMPLAIN_SORT",<%=Constant.COMPLAIN_SORT%>,"<%=tawep.getComplainSort()%>",true,"min_sel","","true",'');
	       			</script>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						信息发出单位：
					</td>
					<td align="left">
						<input type="text" name="DEALER_NAME" id="DEALER_NAME" value="${dealerName }" readonly/>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						联系电话：
					</td>
					<td align="left">
						<input type="text"  name="TEL" datatype="0,is_phone" class="middle_txt" id="TEL" value="${info.tel }"/>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						信息类别：
					</td>
					<td align="left">
					<script type="text/javascript">
	              		genSelBoxExp("INFOMATION_SORT",<%=Constant.INFOMATION_SORT%>,"<%=tawep.getInformationSort()%>",true,"min_sel","","true",'');
	       			</script>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						发出时间：
					</td>
					<td align="left">
						<input type="text" name="CURRENT_DATE" id="CURRENT_DATE" value="<%=CommonUtils.printDate(tawep.getCurrentDate()) %>"
							class="short_txt" datatype="0,is_date,10"
							 hasbtn="true" callFunction="showcalendar(event, 'CURRENT_DATE', false);" />
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						车辆识别码：
					</td>
					<td align="left">
						<input type="text" name="VIN" id="VIN" class="middle_txt" datatype="0,is_vin" blurback="true" value="${info.vin }"/>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						车型：
					</td>
					<td align="left" id="MODEL_NAME">
					</td>
					<input type="hidden" name="MODEL_CODE" id="MODEL_CODE" class="middle_txt" value="${info.modelCode }"/>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						发动机号：
					</td>
					<td align="left">
						<input type="text" name="ENGINE_NO" id="ENGINE_NO" datatype="0,is_null" class="middle_txt" value="${info.engineNo }"/>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						出厂日期：
					</td>
					<td align="left">
						<input type="text" name="FACTORY_DATE" id="FACTORY_DATE" value="<%=CommonUtils.printDate(tawep.getFactoryDate()) %>"
							class="short_txt" datatype="0,is_date,10"
							 hasbtn="true" callFunction="showcalendar(event, 'FACTORY_DATE', false);" />
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						购车日期：
					</td>
					<td align="left">
					<input type="text" name="PURCHASED_DATE" id="PURCHASED_DATE" value="<%=CommonUtils.printDate(tawep.getPurchasedDate()) %>"
							class="short_txt" datatype="1,is_date,10"
							 hasbtn="true" callFunction="showcalendar(event, 'PURCHASED_DATE', false);" />
					</td>
					<td class="table_edit_2Col_label_7Letter">
						行驶里程：
					</td>
					<td align="left">
						<input type="text" name="MILEAGE" id="MILEAGE" class="middle_txt" value="${info.mileage }"/>
					</td>
				</tr><tr>
					<td class="table_edit_2Col_label_7Letter">
						客户姓名：
					</td>
					<td align="left">
						<input type="text" name="CUSTOMER_NAME" id="CUSTOMER_NAME" class="middle_txt" value="${info.customName }"/>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						联系电话：
					</td>
					<td align="left">
						<input type="text" name="PHONE" id="PHONE" class="middle_txt" datatype="0,is_phone" value="${info.phone }"/>
					</td>
				</tr><tr>
					<td class="table_edit_2Col_label_7Letter">
						问题部位：
					</td>
					<td align="left">
						<script type="text/javascript">
		              		genSelBoxExp("QUESTION_PART",<%=Constant.QUESTION_PART%>,"<%=tawep.getQuestionPart()%>",true,"min_sel","","true",'');
		       			</script>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						故障性质：
					</td>
					<td align="left">
					<script type="text/javascript">
		              	genSelBoxExp("FAULT_NATURE",<%=Constant.FAULT_NATURE%>,"<%=tawep.getFaultNatrue()%>",true,"min_sel","","true",'');
		       		</script>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						变速器类型：
					</td>
					<td align="left">
					<script type="text/javascript">
		              	genSelBoxExp('GEARBOX_NATURE','<%=Constant.GEARBOX_NATURE%>',"<%=tawep.getGearboxNatrue()%>",true,'min_sel','','true',''); 
		             </script>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						车辆用途：
					</td>
					<td align="left">
						<input type="text" name="VHCL_USE" id="VHCL_USE" class="middle_txt" datatype="0,is_digit_letter_cn,10" value="${info.vhclUse }"/>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						故障发生时的车速情况：
					</td>
					<td align="left">
						<input type="text" name="SPEED_CASE" id="SPEED_CASE" class="middle_txt" datatype="0,is_digit_letter_cn,10" value="${info.speedCase}"/>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						故障发生时的路面情况：
					</td>
					<td align="left">
						<input type="text" name="ROAD_CASE" id="ROAD_CASE" class="middle_txt" datatype="0,is_digit_letter_cn,10" value="${info.roadCase}"/>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						故障发生时的装载情况：
					</td>
					<td align="left">
						<input type="text" name="LOADING_CASE" id="LOADING_CASE" class="middle_txt" datatype="0,is_digit_letter_cn,10" value="${info.loadingCase}"/>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						故障发生时的车辆改装情况：
					</td>
					<td align="left">
						<input type="text" name="ADPT_CASE" id="ADPT_CASE" class="middle_txt" datatype="0,is_digit_letter_cn,10" value="${info.adaptCase}"/>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						故障零部件生产厂家：
					</td>
					<td align="left">
						<input type="text" name="PRODUCE_FACTORY" id="PRODUCE_FACTORY" class="middle_txt" datatype="0,is_digit_letter_cn,10" value="${info.produceFactroy}"/>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						完整发动机号：
					</td>
					<td align="left">
						<input type="text" name="COMPLETE_ENGINENO" class="middle_txt" datatype="0,is_digit_letter,60" id="COMPLETE_ENGINENO" value="${info.completeEngineno}"/>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						故障原因及处理意见：
					</td>
					<td align="left" colspan="4">
						<textarea type="text" rows="1" cols="70" name="FAULT_OPINION" id="FAULT_OPINION" datatype="0,is_textarea,330" > ${info.faultOpinion}</textarea>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						配套件损坏情况(件名及供应商等)：
					</td>
					<td align="left" colspan="4">
						<textarea type="text" rows="1" cols="70" name="DAMAGE_CASE" id="DAMAGE_CASE" datatype="0,is_textarea,330" >${info.damageCase}</textarea>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						损失估计：
					</td>
					<td align="left">
						<input type="text" name="DAMAGE_PRICE" id="DAMAGE_PRICE" class="middle_txt" datatype="0,is_double,10" value="${info.damagePrice}"/>
					</td>
					
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						技术服务处处理意见及处理人,处理时间：
					</td>
					<td align="left">
						<input type="text" name="PROCESS_OPINION" id="PROCESS_OPINION" datatype="1,is_digit_letter_cn,330" value="${info.processOpinion}"/>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						结果：
					</td>
					<td align="left">
						<input type="text" name="RESULT" id="RESULT" datatype="1,is_digit_letter_cn,330" value="${info.result}"/>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						登记人：
					</td>
					<td align="left">
						<input type="text" name="BOOKER"  class="middle_txt"  id="BOOKER" datatype="0,is_digit_letter_cn,10" value="${info.booker }"/>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						回访单位：
					</td>
					<td align="left">
						<input type="text" name="BACK_UNIT"  class="middle_txt" id="BACK_UNIT" datatype="0,is_digit_letter_cn,100" value="${info.backUnit }"/>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						回访日期：
					</td>
					<td align="left">
						<input type="text" name="BACK_DATE" id="BACK_DATE" class="short_txt" datatype="0,is_date,10" value="<%=CommonUtils.printDate(tawep.getBackDate()) %> "
							 hasbtn="true" callFunction="showcalendar(event, 'BACK_DATE', false);" />
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						质量信息回复及改进情况：
					</td>
					<td align="left">
					（以下由长安公司内部职能部门填写）
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						围绕原因分析所开展的工作：
					</td>
					<td align="left">
						<input type="text" name="CASE_WORK" id="CASE_WORK" datatype="1,is_digit_letter_cn,330" value="${info.caseWork }"/>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						问题原因：
					</td>
					<td align="left">
						<input type="text" name="QUESTION_CASE"  class="long_txt" id="QUESTION_CASE" datatype="1,is_digit_letter_cn,100" value="${info.questionCase }"/>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						结论：
					</td>
					<td align="left">
						<input type="text" name="CONNTION" id="CONNTION"  class="long_txt"  datatype="1,is_digit_letter_cn,100" value="${info.conntion }"/>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						责任单位：
					</td>
					<td align="left">
						<input type="text" name="DUTY_UNIT" id="DUTY_UNIT"  class="long_txt"  datatype="1,is_digit_letter_cn,100" value="${info.dutyUnit }"/>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						临时措施：
					</td>
					<td align="left">
						<input type="text" name="TEMP_STEP"  class="long_txt" id="TEMP_STEP" datatype="1,is_digit_letter_cn,100" value="${info.tempStep }"/>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						永久措施：
					</td>
					<td align="left">
						<input type="text" name="FORVE_STEP"  class="long_txt"  datatype="1,is_digit_letter_cn,100" id="FORVE_STEP" value="${info.forveStep }"/>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						建议：
					</td>
					<td align="left">
						<input type="text" name="SUGGEST" id="SUGGEST"  class="long_txt"  datatype="1,is_digit_letter_cn,100" value="${info.suggest }"/>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						改进情况客户服务部意见：
					</td>
					<td align="left">
						<input type="text" name="SERVICE_OPINION"  class="long_txt" id="SERVICE_OPINION" datatype="1,is_digit_letter_cn,330" value="${info.serviceOpinion }"/>
					</td>
				</tr>
				
				<tr>
				<td colspan="4" align="center">
				<input type="button" value="确定" onclick="confirmAdd();" id="queryBtn" class="normal_btn"/>&nbsp;
				<input type="button" value="打印" style="display:none" onclick="printIt();" id="printBtn" class="normal_btn"/>&nbsp;
				<input type="button" value="关闭" onclick="parent._hide();" class="normal_btn"/>
				</td> 
				</tr>
		</table>
		</form>
	</body>
	<script type="text/javascript">
	function printIt(){
	    var url = '<%=contextPath%>/infomationQuality/InfomationQualityMaintain/printUrlInit.do?id='+infoIdSub ;
	    window.open(url);
	}
	 //genSelBoxStrExp('stAction','<%=Constant.ORDER_SV_ACTION%>','',true,'short_sel','','true','<%=Constant.ORDER_SV_ACTION_03%>');
			if (infoIdSub==''||infoIdSub==null||infoIdSub=='null') {
				isModify = false;
				document.getElementById("BILL_NO").style.display='none';

				$('printBtn').style.display = 'none' ;
			}else {
				isModify = true; //是修改入口
				document.getElementById("BILL_NO").style.display='';

				$('printBtn').style.display = '' ;
			}
	</script>
</html>
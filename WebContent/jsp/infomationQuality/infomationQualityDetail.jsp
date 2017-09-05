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
			function doInit(){
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
		 			} else {
						parent.infoId=json.infoId;
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
		 			} else {
						parent.infoId=json.infoId;
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
			document.getElementById("CUSTOMER_NAME").value= '');
			
			//document.getElementById("MODEL_NAME0").value = '' ;
			//document.getElementById("MODEL_CODE0").value = '' ;
			//document.getElementById("ENGINE_NO0").value = '' ;
			document.getElementById("MILEAGE").value =  '';
			document.getElementById("FACTORY_DATE").value = '';
			document.getElementById("PURCHASED_DATE").value='';
			MyAlert("输入的不是有效VIN格式！");
		}
	}
	//null返回“”
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
			
			document.getElementById("MILEAGE").value =  getNull(record.mileage);
			document.getElementById("FACTORY_DATE").value = formatDate(getNull(record.factoryDate) );
			document.getElementById("PURCHASED_DATE").value=formatDate(getNull(record.purchasedDate) );
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
					<%=tawep.getBillNo() %>
				</td>
			</tr>
			<tr>
					<td class="table_edit_2Col_label_7Letter">
						信息发出单位编号：
					</td>
					<td align="left">
						${dealerCode}
					</td>
					<td class="table_edit_2Col_label_7Letter">
						投诉类型：
					</td>
					<td align="left">
					<script type="text/javascript">
					writeItemValue('<%=tawep.getComplainSort()%>');
	       			</script>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						信息发出单位：
					</td>
					<td align="left">
						${dealerName }
					</td>
					<td class="table_edit_2Col_label_7Letter">
						联系电话：
					</td>
					<td align="left">
						${info.tel }
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						信息类别：
					</td>
					<td align="left">
					<script type="text/javascript">
						writeItemValue('<%=tawep.getInformationSort()%>');
	       			</script>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						发出时间：
					</td>
					<td align="left">
						<%=CommonUtils.printDate(tawep.getCurrentDate()) %>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						车辆识别码：
					</td>
					<td align="left">
						${info.vin }
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
						${info.engineNo }
					</td>
					<td class="table_edit_2Col_label_7Letter">
						出厂日期：
					</td>
					<td align="left">
						<%=CommonUtils.printDate(tawep.getFactoryDate()) %>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						购车日期：
					</td>
					<td align="left">
					<%=CommonUtils.printDate(tawep.getPurchasedDate()) %>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						行驶里程：
					</td>
					<td align="left">
						${info.mileage }
					</td>
				</tr><tr>
					<td class="table_edit_2Col_label_7Letter">
						客户姓名：
					</td>
					<td align="left">
						${info.customName }
					</td>
					<td class="table_edit_2Col_label_7Letter">
						联系电话：
					</td>
					<td align="left">
						${info.phone }
					</td>
				</tr><tr>
					<td class="table_edit_2Col_label_7Letter">
						问题部位：
					</td>
					<td align="left">
						<script type="text/javascript">
							writeItemValue('<%=tawep.getQuestionPart()%>');
		       			</script>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						故障性质：
					</td>
					<td align="left">
					<script type="text/javascript">
						writeItemValue('<%=tawep.getFaultNatrue()%>');
		       		</script>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						变速器类型：
					</td>
					<td align="left">
					<script type="text/javascript">
						writeItemValue('<%=tawep.getGearboxNatrue()%>');
		             </script>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						车辆用途：
					</td>
					<td align="left">
						${info.vhclUse }
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						故障发生时的车速情况：
					</td>
					<td align="left">
						${info.speedCase}
					</td>
					<td class="table_edit_2Col_label_7Letter">
						故障发生时的路面情况：
					</td>
					<td align="left">
						${info.roadCase}
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						故障发生时的装载情况：
					</td>
					<td align="left">
						${info.loadingCase}
					</td>
					<td class="table_edit_2Col_label_7Letter">
						故障发生时的车辆改装情况：
					</td>
					<td align="left">
						${info.adaptCase}
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						故障零部件生产厂家：
					</td>
					<td align="left">
						${info.produceFactroy}
					</td>
					<td class="table_edit_2Col_label_7Letter">
						完整发动机号：
					</td>
					<td align="left">
						${info.completeEngineno}
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						故障原因及处理意见：
					</td>
					<td align="left" colspan="4">
						${info.faultOpinion}
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						配套件损坏情况(件名及供应商等)：
					</td>
					<td align="left" colspan="4">
						${info.damageCase}
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						损失估计：
					</td>
					<td align="left">
						${info.damagePrice}
					</td>
					
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						技术服务处处理意见及处理人,处理时间：
					</td>
					<td align="left">
						${info.processOpinion}
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						结果：
					</td>
					<td align="left">
						${info.result}
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						登记人：
					</td>
					<td align="left">
						${info.booker }
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						回访单位：
					</td>
					<td align="left">
						${info.backUnit }
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						回访日期：
					</td>
					<td align="left">
						<%=CommonUtils.printDate(tawep.getBackDate()) %>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						质量信息回复及改进情况：
					</td>
					<td align="left">
					（以下又长安公司内部职能部门填写）
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						围绕原因分析所开展的工作：
					</td>
					<td align="left">
						${info.caseWork }
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						问题原因：
					</td>
					<td align="left">
						${info.questionCase }
					</td>
					<td class="table_edit_2Col_label_7Letter">
						结论：
					</td>
					<td align="left">
						${info.conntion }
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						责任单位：
					</td>
					<td align="left">
						${info.dutyUnit }
					</td>
					<td class="table_edit_2Col_label_7Letter">
						临时措施：
					</td>
					<td align="left">
						${info.tempStep }
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						永久措施：
					</td>
					<td align="left">
						${info.forveStep }
					</td>
					<td class="table_edit_2Col_label_7Letter">
						建议：
					</td>
					<td align="left">
						${info.suggest }
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						改进情况客户服务部意见：
					</td>
					<td align="left">
						${info.serviceOpinion }
					</td>
				</tr>
				
				<tr>
				<td colspan="4" align="center">
				<!--  <input type="button" value="确定" onclick="confirmAdd();" id="queryBtn" class="normal_btn"/>&nbsp-->
				<input type="button" value="关闭" onclick="parent._hide();" class="normal_btn"/>
				</td> 
				</tr>
		</table>
		</form>
	</body>
	<script type="text/javascript">
	 //genSelBoxStrExp('stAction','<%=Constant.ORDER_SV_ACTION%>','',true,'short_sel','','true','<%=Constant.ORDER_SV_ACTION_03%>');
			if (infoIdSub==''||infoIdSub==null||infoIdSub=='null') {
				isModify = false;
				document.getElementById("BILL_NO").style.display='none';
			}else {
				isModify = true; //是修改入口
				document.getElementById("BILL_NO").style.display='';
			}
	</script>
</html>
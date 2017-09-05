<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>   
<%! 
    /** 格式化金钱时保留的小数位数 */
	int minFractionDigits = 2;
    /** 当格式化金钱为空时，默认返回值 */
    String defaultValue = "0";
%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.po.TtAsWrApplicationExtPO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.po.TtAsWrNetitemExtPO"%>
<%@page import="com.infodms.dms.po.TtAsWrWrauthorizationPO"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.infodms.dms.po.TtAsActivityPO"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrAppauthitemPO"%>
<%@page import="com.infodms.dms.bean.ClaimListBean"%>
<%@page import="com.infodms.dms.po.TtAsWrLabouritemPO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.infodms.dms.po.TtAsWrPartsitemPO"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>索赔申请单明细</title>
	<script type="text/javascript">
	    //控制信息列表显示或隐藏
		function tabDisplayControl(tableId){
			var tab = document.getElementById(tableId);
			if(tab.style.display=='none'){
				tab.style.display = '';
			}else{
				tab.style.display = 'none';
			}
		}

		//展示附件
	    function addUploadRowByDb1(filename,fileId,fileUrl){
		 	var tab =document.getElementById("fileUploadTab");
		 	var row =  tab.insertRow();
		    row.className='table_list_row1';
		    row.insertCell();
		    row.insertCell();
		    row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> ";
		    row.cells(1).innerHTML = "<input disabled type=button onclick='delUploadFile(this)' class='normal_btn' value='删 除' />";    
		}
		//下拉框修改时赋值
		function assignSelect(name,s) {
			var t = "";
			if(!s || s=='undefined'){
				t = "&nbsp;";
				s = "";
			}else if(s.length > 3){
	        	t = s.substr(0,3) + "...";
			}else{
	     		t = s;
			}
     		document.getElementById(name).title=s;
     		return t;
		}
		//根据工单号查询维修记录
		function roNoDetail(value){
			var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillRoNoForward.do?roNo='+value ;
			window.open(url,800,500);
			//location = url ;
			//OpenHtmlWindow(url,900,500);
		}
	</script>
</head>
<body>
<%
	TtAsWrApplicationExtPO tawep = (TtAsWrApplicationExtPO)request.getAttribute("application");//索赔申请单信息
	List<ClaimListBean> itemLs = (LinkedList<ClaimListBean>)request.getAttribute("itemLs");//索赔申请单之工时信息
	List<ClaimListBean> partLs = (LinkedList<ClaimListBean>)request.getAttribute("partLs");//索赔申请单只配件信息
	List<TtAsWrNetitemExtPO> otherLs = (LinkedList<TtAsWrNetitemExtPO>)request.getAttribute("otherLs");//索赔申请单之其他项目
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");//索赔但附件
	String id = (String)request.getAttribute("ID");//索赔申请单ID
	TtAsWrWrauthorizationPO authReason = (TtAsWrWrauthorizationPO)request.getAttribute("authReason");//需要授权原因-在授权审核时呈现
	Map<String,Object> customerMap = (Map<String,Object>)request.getAttribute("customerMap");//客户信息
	String authLevel = (String)request.getAttribute("authLevel");//需要审核的级别
	if(authReason==null){
		authReason = new TtAsWrWrauthorizationPO();
	}
	String isAuditStr = (String)request.getAttribute("isAudit");
	boolean isAudit = true;
	if(!"true".equalsIgnoreCase(isAuditStr)){//是否进行审核（true:是）
		isAudit = false;
	}
%>
<form method="post" name="fm" id="fm">
	<table width="100%">
	 <tr><td>
	 	<input type="hidden" name="claimid" id="claimid" value="${ID}" />
		<input type="hidden" name="OTHERFEE" id="OTHERFEE" value="<%=request.getAttribute("OTHERFEE")%>" /><!-- 其他费用下拉框(已文本方式存放) -->
		<!-- 申请单信息 ~ 基本信息  -->
		<table class="table_edit">
			<tr>
				<th colspan="10">
					<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
					&nbsp;<a href="#" onclick="tabDisplayControl('baseTabId')">基本信息</a>
				</th>
			</tr>
		</table>
		<table class="table_edit" id="baseTabId">
			<tr>
				<td align="right" nowrap="true">索赔申请单号:</td>
				<td align="left" nowrap="true" id="claimNo"><%=CommonUtils.checkNull(tawep.getClaimNo())%>&nbsp;</td>
				<td align="right" nowrap="true">经销商代码:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getDealerCode())%>&nbsp;</td>
				<td align="right" nowrap="true">经销商名称:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getDealerName())%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">工单号:</td>
				<td align="left" nowrap="true">
				<%=CommonUtils.checkNull(tawep.getRoNo())%>
				<%-- 2010-09-17 注释 现在使用 最下面按钮方式
				<a href="#" onclick="roNoDetail('<%=CommonUtils.checkNull(tawep.getRoNo())%>');">
				    [<%=CommonUtils.checkNull(tawep.getRoNo())%>
					-<%=CommonUtils.checkNull(tawep.getLineNo()) %>]</a>--%>
					&nbsp;
				</td>
				<td align="right" nowrap="true">工单开始时间:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(Utility.handleDate1(tawep.getRoStartdate()))%>&nbsp;</td>
				<td align="right" nowrap="true">工单结束时间:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(Utility.handleDate1(tawep.getRoEnddate())) %>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">VIN:
				<input type="hidden" id="VIN" value="<%=CommonUtils.checkNull(tawep.getVin())%>"/>
				</td>
				<td align="left" nowrap="true">
					<%=CommonUtils.checkNull(tawep.getVin())%>&nbsp;
				<%-- 2010-09-17 注释 现在使用 最下面按钮方式
				 	<a href="<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillTrackForword.do?vin=<%=CommonUtils.checkNull(tawep.getVin())%>">
						<%=CommonUtils.checkNull(tawep.getVin())%>
					</a>
					--%>
				</td>
				<td align="right" nowrap="true">发动机号:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getEngineNo())%></td>
				<td align="right" nowrap="true">牌照号:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getLicenseNo())%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">车系:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getSeriesName()) %>&nbsp;</td>
				<td align="right" nowrap="true">车型:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getModel()) %>&nbsp;</td>
				<td align="right" nowrap="true">配置:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getPackageName()) %>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">生产基地:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getYieldlyName()) %>&nbsp;</td>
				<td align="right" nowrap="true">行驶里程:
				<input type="hidden" value="<%=CommonUtils.checkNull(tawep.getInMileage()) %>" id="IN_MILEAGE"/>
				</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getInMileage()) %>&nbsp;</td>
				<td align="right" nowrap="true">索赔类型:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getClaimName())%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">送修人姓名:</td>
				<td align="left" nowrap="true"><%=CommonUtils.getDataFromMap(customerMap,"CTM_NAME")%></td>
				<td align="right" nowrap="true">送修人电话:</td>
				<td align="left" nowrap="true"><%=CommonUtils.getDataFromMap(customerMap,"MAIN_PHONE")%></td>
				<%--<td align="right" nowrap="true">客户手机:</td>
				<td align="left" nowrap="true"><%=CommonUtils.getDataFromMap(customerMap,"")%></td>--%>
				<td align="right" nowrap="true">车辆购车日期:</td>
				<td align="left" nowrap="true"><%=CommonUtils.printDate(tawep.getPurchasedDate())%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" valign="top">活动代码：</td>
				<td align="left" colspan="5">
					<%=CommonUtils.checkNull(tawep.getCampaignCode()) %>
				</td>
				
			</tr>
		</table>
		</td></tr>
		<%if(!Constant.CLA_TYPE_02.equals(tawep.getClaimType()) && !Constant.CLA_TYPE_06.equals(tawep.getClaimType())){
		//但是“免费保养”“服务活动”两种索赔单类型该项不展示 %>
		<tr><td>
		<!-- 申请单信息 ~ 故障说明  -->
		<table class="table_edit">
			<tr>
				<th colspan="6">
					<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
					&nbsp;<a href="#" onclick="tabDisplayControl('troubleTabId')">故障说明</a>
				</th>
			</tr>
		</table>
		<table class="table_edit" id="troubleTabId" style="display:none">
			<tr>
			    <td align="right" valign="top">故障描述：</td>
				<td align="left">
					&nbsp;<textarea rows="2" cols="30" readonly="readonly"><%=CommonUtils.checkNull(tawep.getTroubleDesc()) %>&nbsp;</textarea>
				</td>
				<td align="right" valign="top">故障原因：</td>
				<td align="left">
					&nbsp;<textarea rows="2" cols="30" readonly="readonly"><%=CommonUtils.checkNull(tawep.getTroubleReason()) %>&nbsp;</textarea>
				</td>
			</tr>
			<tr>
				<td align="right" valign="top">维修措施：</td>
				<td align="left">
					&nbsp;<textarea rows="2" cols="30" readonly="readonly"><%=CommonUtils.checkNull(tawep.getRepairMethod()) %>&nbsp;</textarea>
				</td>
				<td align="right" valign="top">申请备注：</td>
				<td align="left">
					&nbsp;<textarea rows="2" cols="30" readonly="readonly"><%=CommonUtils.checkNull(tawep.getRemark()) %>&nbsp;</textarea>
				</td>
			</tr>
		</table>
	</td></tr>
	<%} %>
	
	<tr><td>
		<!-- 申请单信息 ~ 申请费用  -->
		<table class="table_edit">
			<tr>
				<th colspan="10">
					<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
					&nbsp;<a href="#" onclick="tabDisplayControl('amountTabId')">申请费用</a>
				</th>
			</tr>
		</table>
		<%-- ADD BY XZM 2010-09-15 显示的都是结算金额  --%>
		<table class="table_edit" id="amountTabId">
			<tr>
				<td align="right" nowrap="true">工时数量：</td>
				<td align="left"><%=CommonUtils.checkNull(tawep.getLabourHours())%>&nbsp;</td>
				<td align="right" nowrap="true">工时金额：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getBalanceLabourAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">配件数量：</td>
				<td align="left"><%=CommonUtils.checkNull(tawep.getPartsCount())%>&nbsp;</td>
				<td align="right" nowrap="true">配件金额：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getBalancePartAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">其他项目金额：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getBalanceNetitemAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">服务活动金额：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getCampaignFee(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">保养金额：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getFreeMPrice(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">&nbsp;</td>
				<td align="left">&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">追加工时数量：</td>
				<td align="left"><%=CommonUtils.checkNull(tawep.getAppendlabourNum())%>&nbsp;</td>
				<td align="right" nowrap="true">追加工时金额：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getAppendlabourAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">追加费用：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getAppendAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">索赔总金额：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getBalanceAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
			</tr>
				<tr>
				<td align="right" nowrap="true">工时费打折：</td>
				<td align="left"><%=CommonUtils.checkNull(tawep.getLabourDown())%>&nbsp;</td>
				<td align="right" nowrap="true">材料费打折：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getPartDown(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">辅料费：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getAccessoriesPrice(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">补偿费：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getCompensationMoney(),minFractionDigits,defaultValue)%>&nbsp;</td>
				</tr>
		</table>
		<%-- MODIFY BY XZM 2010-09-15 按新格式显示费用信息 
		<table class="table_edit" id="amountTabId" style="display:none">
			<tr>
				<td align="right" nowrap="true">基本工时：</td>
				<td><%=CommonUtils.checkNull(tawep.getLabourHours())%>&nbsp;</td>
				<td align="right" nowrap="true">附加工时：</td>
				<td><%=CommonUtils.checkNull(tawep.getReinforceHours())%>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
			    <td align="right" nowrap="true">工时金额(元)：</td>
				<td><%=CommonUtils.currencyFormat(tawep.getLabourAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">基本工时金额(元)：</td>
				<td><%=CommonUtils.currencyFormat(tawep.getStdLabourAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">附加工时金额(元)：</td>
				<td><%=CommonUtils.currencyFormat(tawep.getReinLabourAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">配件金额(元)：</td>
				<td><%=CommonUtils.currencyFormat(tawep.getPartAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">其它费用金额(元)：</td>
				<td><%=CommonUtils.currencyFormat(tawep.getNetitemAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">保养金额(元)：</td>
				<td><%=CommonUtils.currencyFormat(tawep.getFreeMPrice(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">服务活动金额(元)：</td>
				<td><%=CommonUtils.currencyFormat(tawep.getCampaignFee(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">索赔申请金额(元)：</td>
				<td><%=CommonUtils.currencyFormat(tawep.getRepairTotal(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">税额(元)：</td>
				<td><%=CommonUtils.currencyFormat(tawep.getTaxSum(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">总金额(元)：</td>
				<td><%=CommonUtils.currencyFormat(tawep.getGrossCredit(),minFractionDigits,defaultValue)%>&nbsp;</td>
			</tr>
		</table>
		--%>
	</td></tr>
	<%if(Constant.CLA_TYPE_09.equals(tawep.getClaimType())){
		//但是“免费保养”“服务活动”两种索赔单类型该项不展示 %>
	<tr>
	<td>
		<table class="table_edit">
			<tr>
				<th colspan="6">
					<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
					&nbsp;<a href="#" onclick="tabDisplayControl('outId')">外出维修</a>
				</th>
			</tr>
		</table>
		<table class="table_edit" id="outId" style="display:none">
          	<!--  <tr>
              <th colspan="10"  ><img src="../../../img/subNav.gif" alt="" class="nav" />
              外出维修</th>
            </tr>-->
            <tr>
          	<td align="right">
          	开始时间：
          	</td>
          	<td nowrap="nowrap" id="startTime">
          	<input type="hidden" id="START_TIME" value="${listOutRepair.START_TIME}" />
			</td>
          	<td align="right">
          	结束时间：
          	</td>
          	<td align="left" id="endTime">
          	<input type="hidden" id="END_TIME" value="${listOutRepair.END_TIME}" />
          	</td>
          	<td align="right">
          	派车车牌号：
          	</td>
          	<td align="left">
        	${listOutRepair.OUT_LICENSENO}  
          	</td>
          	</tr>
          	<tr>
          	<td align="right">
          	外出人：
          	</td>
          	<td align="left">
			${listOutRepair.OUT_PERSON}  
          	</td>
          	<td align="right">
          	出差目的地：
          	</td>
          	
          	<td align="left">
          	
          	${listOutRepair.OUT_SITE}  
          	</td>
          	<td align="right">
          	总里程：
          	</td>
          	<td align="left"> 
          	${listOutRepair.OUT_MILEAGE} 
          	</td>
          	</tr>
          </table>  
			<!--  <TABLE class="table_edit">
						<tr>
              <th colspan="10"  ><img src="../../../img/subNav.gif" alt="" class="nav" />
              申请费用</th>
            </tr>
            </TABLE>-->
	</td>
	</tr>
	<%} %>
	<tr><td>
		<!-- 申请单信息 ~ 附件信息  -->
		<!-- 添加附件 -->
		<input type="hidden" id="fjids" name="fjids"/>
		<table class="table_edit" border="0" id="file">
		    <tr>
		        <th>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				&nbsp;<a href="#" onclick="tabDisplayControl('attachTabId')">附件信息</a>
				</th>
			</tr>
		</table>
		<table class="table_edit" id="attachTabId" style="display:none">
			<% if(attachLs!=null && attachLs.size()>0) {%>
			<tr>
	    		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
	  		</tr>
	  			<%for(int i=0;i<attachLs.size();i++) { %>
	  			<script type="text/javascript">
	  			addUploadRowByDb1('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
	  			</script>
	  			<%} %>
	  		<%} else { %>
	  			<tr>
	  				<td>&nbsp;未上传附件</td>
	  			</tr>
	  		<%} %>
		</table>
	</td></tr>
	<tr><td>
		<table class="table_edit">
			<tr>
				<th colspan="11" align="left">
					<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
					&nbsp;<a href="#" onclick="tabDisplayControl('labourTable')">申请单-索赔项目明细</a>
				</th>
			</tr>
		</table>
		<table class="table_list" name="all_group_" style="border-bottom: 1px solid #DAE0EE;" id="labourTable">
			<tr class='table_list_th'>
			    <th>操作</th>
				<th>选择</th>
				<th>索赔作业名称</th>
				<th>索赔作业代码</th>
				<th>工时结算数</th>
				<th>工时单价(元)</th>
				<th>工时金额(元)</th>
				<th>作业次数</th>
				<th>工时结算金额(元)</th>
				<th width="20%">审核意见</th>
			</tr>
			<%
			  List<Object> labourList = new ArrayList<Object>();
			  for(int i=0;i<itemLs.size();i++){
				    ClaimListBean claimBean = itemLs.get(i);
					TtAsWrLabouritemPO mainLabourPO = (TtAsWrLabouritemPO)claimBean.getMain();
					List<?> subLabourList = claimBean.getAdditional();
					labourList.add(mainLabourPO);
					if(subLabourList!=null && subLabourList.size()>0)
						labourList.addAll(subLabourList);
			  }
			  for (int i=0;i<labourList.size();i++) {
				TtAsWrLabouritemPO mainLabourPO = (TtAsWrLabouritemPO)labourList.get(i);
			%>
			 
		     	 <tr class="<%=(i%2)==0?"table_list_row1":"table_list_row2"%>">
			     	 <td>
			     	 	<input type="hidden" name="LABOUR_ID" value="<%=mainLabourPO.getLabourId()%>"/>
					    <img src="<%=contextPath%>/img/nolines_minus.gif" alt="查询对应索赔配件" onclick="showPart('<%=mainLabourPO.getWrLabourcode()%>',this)"/>
			     	 </td>
			     	 <td> <input type="checkbox"  
				<%  if(mainLabourPO.getUpdateBy() != 1){%> disabled="true"  <% } %>  name="labourIds"   value="<%=mainLabourPO.getLabourId()%>"/></td>    
				     <td><%=CommonUtils.checkNull(mainLabourPO.getWrLabourname() )%>&nbsp;</td>
				     <td><%=CommonUtils.checkNull(mainLabourPO.getWrLabourcode() )%>&nbsp;</td>
				     <td><%=CommonUtils.checkNull(mainLabourPO.getBalanceQuantity()) %>&nbsp;</td>
				     <td><%=CommonUtils.currencyFormat(mainLabourPO.getLabourPrice(),minFractionDigits,defaultValue)%>&nbsp;</td>
				     <td><%=CommonUtils.currencyFormat(mainLabourPO.getLabourAmount(),minFractionDigits,defaultValue)%> <input type="hidden" name="Labour<%=mainLabourPO.getLabourId()%>" value="<%=mainLabourPO.getLabourAmount() %>"/> &nbsp;</td>
				     <td><%="".equals(CommonUtils.checkNull(mainLabourPO.getLabourCount()))?
				    				 "0":CommonUtils.checkNull(mainLabourPO.getLabourCount())%>
				     </td>
				     <td>
				     	<%if(isAudit){//为审核页面 %>
				     		<input name="LOBOURAMOUNT" id="LOBOURAMOUNT<%=i%>" type="text" datatype="1,is_double,20" decimal="2" 
				     		value="<%=CommonUtils.checkNull(mainLabourPO.getBalanceAmount())%>"/>
				     	<%}else{ %>
				     		<%=CommonUtils.currencyFormat(mainLabourPO.getBalanceAmount(),minFractionDigits,defaultValue)%>
				     	<%} %>&nbsp;
				     </td>
				     <td rowspan="2" >
						<textarea  rows="5" name="authremark<%=mainLabourPO.getLabourId()%>" id="authremark<%=mainLabourPO.getLabourId()%>" datatype="0,is_textarea,100" maxlength="100"><%=CommonUtils.checkNull(mainLabourPO.getAuthRemark()) %></textarea>
				     </td>
                    <c:if test="${10791007 == application.status}">				     
				     <td ><%=CommonUtils.checkNull(mainLabourPO.getAuthRemark()) %>&nbsp;
				     </td>
				    </c:if>
				    </tr>  
				    
				
				 <% 
				    if(partLs!=null && partLs.size()>0){
				    	boolean isShow = false;
				 %>
					 <%
					    for(int j=0;j<partLs.size();j++) {
			     		   ClaimListBean claimBean = partLs.get(j);
						   TtAsWrPartsitemPO mainPartPO = (TtAsWrPartsitemPO)claimBean.getMain();
			     	 %>
	 					<%if(mainLabourPO.getWrLabourcode().equals(mainPartPO.getWrLabourcode())){
						        if(!isShow){%>
								<tr class="table_list_row1" id="<%=mainLabourPO.getWrLabourcode()%>">
						 		<td>&nbsp;</td>
							 	<td colspan="8"> 
								 	<table class="table_list">
						        	  <tr class="table_list_th">
						        	   	<th>是否三包</th>
									    <th>换下配件名称</th>
										<th>换上配件名称</th>
										<th>换上配件编码</th>
										<th>配件结算数量</th>
										<th>配件单价</th>
										<th>配件金额</th>
										<th>配件结算金额</th>
										<th>责任性质</th>
										<th>需要审核级别</th>
                                    <c:if test="${10791007 == application.status}">										
										<th>审核意见</th>
									</c:if>
									 </tr>
						        <%isShow=true;} %>
				
						         <tr>
						          <td><%=CommonUtils.checkNull(mainPartPO.getIsGua()==1?"Y":"N")%>&nbsp;</td>
							     <td><%=CommonUtils.checkNull(mainPartPO.getDownPartName()) %>&nbsp;
							         <input type="hidden" name="PART_ID" value="<%=mainPartPO.getPartId() %>"/>
							         <input type="hidden" name="PART_CODE" value="<%=CommonUtils.checkNull(mainPartPO.getPartCode()) %>"/>
							     </td>
							     <td><%=CommonUtils.checkNull(mainPartPO.getPartName()) %>&nbsp;</td>
							      <td><%=CommonUtils.checkNull(mainPartPO.getPartCode()) %>&nbsp;</td>
							     <td><%=CommonUtils.checkNull(mainPartPO.getBalanceQuantity()) %>&nbsp;</td>
							     <td><%=CommonUtils.currencyFormat(mainPartPO.getPrice(),minFractionDigits,defaultValue) %>&nbsp;</td>
							     <td><%=CommonUtils.currencyFormat(mainPartPO.getAmount(),minFractionDigits,defaultValue) %>&nbsp;</td>
							     
								 <td>
									<%if(isAudit){//为审核页面 %>
							     		<input name="PARTAMOUNT" id="PARTAMOUNT<%=i %>" type="text" datatype="1,is_double,20" decimal="2" 
							     		value="<%=CommonUtils.checkNull(mainPartPO.getBalanceAmount())%>"/>
							     	<%}else{ %>
							     		<%=CommonUtils.currencyFormat(mainPartPO.getBalanceAmount(),minFractionDigits,defaultValue)%>
							     	<%} %>&nbsp;
							     	<input type="hidden"  name="PARTA<%=mainLabourPO.getLabourId()%>" value="<%=CommonUtils.checkNull(mainPartPO.getBalanceAmount())%>"/>
							    </td>
							    <td>
							    <script type="">
							    	document.write(getItemValue(<%=CommonUtils.checkNull(mainPartPO.getResponsibilityType()) %>))
							    </script>
							    </td>
							    <td><%=authLevel %>&nbsp;</td>
							   <c:if test="${10791007 == application.status}">							    
							     <td><%=CommonUtils.checkNull(mainPartPO.getAuthRemark()) %>&nbsp;</td>
							   </c:if>  
							    </tr>
							 
					     <%} %>   
		 		 	<%}
					 if(isShow){%>
				 		 		</table>
			 		 		 </td>
		 		 		</tr>	 
					<% }
		 		 	%>
	 		 	<%}%>
	 		 <%} %>
		</table>
	</td></tr>
    <%if(!Constant.CLA_TYPE_02.equals(tawep.getClaimType()) && !Constant.CLA_TYPE_01.equals(tawep.getClaimType()) 
    		&& !Constant.CLA_TYPE_07.equals(tawep.getClaimType())){
		//“免费保养”“一般维修”“售前维修”三种索赔单类型该项不展示 %>
	<tr><td>
		<table class="table_edit">
			<tr>
				<th colspan="10" align="left">
				    <img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
					&nbsp;<a href="#" onclick="tabDisplayControl('otherTable')">申请单-其他费用明细</a>
				</th>
			</tr>
		</table>
		<table class="table_list" style="border-bottom: 1px solid #DAE0EE;" id="otherTable">
			<tr class='table_list_th'>
				<th>序号</th>
				<th>项目代码</th>
				<th>项目名称</th>
				<th>金额(元)</th>
				<th>备注</th>
				<th>结算金额(元)</th>
			</tr>
			<%for (int i=0;i<otherLs.size();i++) { %>
			    <tr class="<%=(i%2)==0?"table_list_row1":"table_list_row2"%>">
			    	<td><%=i+1%><input type="hidden" name="OTHER_ID" value="<%=otherLs.get(i).getNetitemId()%>"/>&nbsp;</td>
					<td><%=CommonUtils.checkNull(otherLs.get(i).getItemCode()) %>&nbsp;</td>
					<td><%=CommonUtils.checkNull(otherLs.get(i).getItemDesc()) %>&nbsp;</td>
					<td><%=CommonUtils.currencyFormat(otherLs.get(i).getAmount(),minFractionDigits,defaultValue) %>&nbsp;</td>
					<td><%=CommonUtils.checkNull(otherLs.get(i).getRemark()) %>&nbsp;</td>
					<td>
				     	<%if(isAudit){//为审核页面 %>
				     		<input name="OTHERAMOUNT" id="OTHERAMOUNT<%=i%>" type="text" datatype="1,is_double,20" decimal="2" 
				     		value="<%=CommonUtils.checkNull(otherLs.get(i).getBalanceAmount())%>"/>
				     	<%}else{ %>
				     		<%=CommonUtils.currencyFormat(otherLs.get(i).getBalanceAmount(),minFractionDigits,defaultValue)%>
				     	<%} %>&nbsp;			
					</td>
		   		</tr>
		   	<%}%>
		</table>
	</td></tr>
	<%} %>
	<tr><td>
	<br/>
	<div align="center" > 
		<input class="normal_btn" type="button" value="反索赔" onclick="systemCounter();"/>
		<input class="normal_btn" type="button" value="返回" onclick="history.back();"/>
	</div>
		<input type="hidden" name="claimId" value="<%=tawep.getId() %>"/><!-- 索赔申请单ID -->
	</td></tr>
	</table>
</form>
<script type="text/javascript">
	  function systemCounter(){
		   	var r=document.getElementsByName("labourIds");
		   	var temp=0;
			for(var i=0;i<r.length;i++){
		  			if(r[i].checked){
			  				if(check(r[i].value)!=true){
			  					MyAlert("提示：审核意见不能为空!</br>");
			  					return;
			  				}
			  				temp++;
		  			}
	  		}
  			if(temp==0){
  				MyAlert("请选择反索赔工时！");
  				return;
  			}else{
  				MyConfirm("是否反索赔？",commitCounter);
  			}
	  };
	  
	  function check(val){
		var msg ="";
		if(""==document.getElementById('authremark'+val).value){
			msg+="审核意见不能为空!</br>"
		}	
		
		if(msg!=""){
			return false;
		}else{
			return true;
		}
	};
	  function commitCounter()
	  {
			sendAjax('<%=contextPath%>/report/dmsReport/Accord/accord.json?type=-1&claimid='+$('claimid').value,backdoSummit,'fm');
	  		
	  }
	  function backdoSummit(json){
		  if(json.succ=="1"){
		  	fm.action='<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimSystemLabourids.do';
			fm.method="post";
			fm.submit();
		  }else{
			 MyAlert("提示：插入记录数据失败！");
		  }
	  }
      var tempBtn = null;

      //控制表单元素是否显示
	  function disableLink(obj,show){
			if(!show){
				obj.disabled = true;
				obj.style.border = '1px solid #999';
				obj.style.background = '#EEE';
				obj.style.color = '#999';
			}else{
				obj.disabled = false;
				obj.style = '';
			}
	  }
	//配件三包判定按钮方法
		function threePackageSet(){
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
				openWindowDialog('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/threePackageSet.do?VIN='+vin+'&mile='+inMileage+'&codes='+codes+'&codes_type='+codes_type+'&labcodes='+labcodes+'&labcodes_type='+labcodes_type+'&roNo='+roNo);
					//window.open(');
				}
		}
      //根据是否存在父页面关闭窗口
	  function closeWindow(){
		if(parent.${'inIframe'}){
			parent.window._hide();
		}else{
			window.close();
		}
	  }

	  function showPart(partTabId,imgObj){
		  var tab = document.getElementById(partTabId);
		  if(tab!=undefined && tab!=null){
			  tabdisplay = tab.style.display;
			  if('none'!=tabdisplay){
				  tab.style.display = 'none';
				  imgObj.src='<%=contextPath%>/img/nolines_plus.gif';
			  }else{
				  tab.style.display = '';
				  imgObj.src='<%=contextPath%>/img/nolines_minus.gif';
			  }
		  }else{
			  imgObj.src='<%=contextPath%>/img/nolines_minus.gif';
		  }
	  }

	  function openWindowDialog(targetUrl){
		  var height = 500;
		  var width = 800;
		  var iTop = (window.screen.availHeight-30-height)/2;  //获得窗口的垂直位置      
		  var iLeft = (window.screen.availWidth-10-width)/2;   //获得窗口的水平位置
		  var params = "width="+width+",height="+height+",top="+iTop+",left="+iLeft+",scrollbars=yes,resizable=yes,status=no";
		  window.open(targetUrl,null,params);
	  }
			var start = $('START_TIME').value
			var end = $('END_TIME').value
		  	document.getElementById("startTime").innerHTML =start.substr(0,10);
		 	document.getElementById("endTime").innerHTML =end.substr(0,10);
</script>
</body>
</html>
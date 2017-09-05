<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.po.TtAsWrApplicationPartPO"%>
    <%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
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
<%@page import="com.infodms.dms.po.TmVehiclePO"%>
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
    TmVehiclePO veh=(TmVehiclePO)request.getAttribute("vehSel");
    List<TtAsWrApplicationPartPO> applicationPartList = (List<TtAsWrApplicationPartPO>)request.getAttribute("applicationPartList");
	TtAsWrApplicationExtPO tawep = (TtAsWrApplicationExtPO)request.getAttribute("application");//索赔申请单信息
	List<ClaimListBean> itemLs = (LinkedList<ClaimListBean>)request.getAttribute("itemLs");//索赔申请单之工时信息
	List<ClaimListBean> partLs = (LinkedList<ClaimListBean>)request.getAttribute("partLs");//索赔申请单只配件信息
	List<TtAsWrNetitemExtPO> otherLs = (LinkedList<TtAsWrNetitemExtPO>)request.getAttribute("otherLs");//索赔申请单之其他项目
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");//索赔但附件
	List<TtAsWrAppauthitemPO> appAuthls = (List<TtAsWrAppauthitemPO>)request.getAttribute("appAuthls");//授权信息
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
<%
    /*
	List<?> feeTypeList = (List<?>)request.getAttribute("FEETYPE");//保养费用集合 
	HashMap<?,?> hm = (HashMap<?,?>)request.getAttribute("FEE"); //保养费用参数对应的值
	if (hm==null) {
		hm = new HashMap();
	}
	*/
	//TtAsActivityPO tap = (TtAsActivityPO)request.getAttribute("ACTIVITY");//服务活动
%>
<form method="post" name="fm" id="fm">
	<table width="100%">
	 <tr><td>
		<input type="hidden" name="OTHERFEE" id="OTHERFEE" value="<%=request.getAttribute("OTHERFEE")%>" /><!-- 其他费用下拉框(已文本方式存放) -->
		<%-- 2010-08-18 XZM 现在页面不显示质损相关信息，屏蔽掉，同时屏蔽ClaimBillStatusTrack中加载代码
		    <input type="hidden" name="list01" id="list01"
				value="<%=request.getAttribute("BUSINESS_CHNG_CODE_01")%>" />
			<input type="hidden" name="list02" id="list02"
				value="<%=request.getAttribute("BUSINESS_CHNG_CODE_02")%>" />
			<input type="hidden" name="list03" id="list03"
				value="<%=request.getAttribute("BUSINESS_CHNG_CODE_03")%>" />
			<input type="hidden" name="list04" id="list04"
				value="<%=request.getAttribute("BUSINESS_CHNG_CODE_04")%>" />
	     --%>
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
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getClaimNo())%>&nbsp;</td>
				<td align="right" nowrap="true">服务商代码:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getDealerCode())%>&nbsp;</td>
				<td align="right" nowrap="true">经销商名称:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getDealerName())%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">生产基地:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getYieldlyName()) %>&nbsp;</td>
				<% if(veh != null ){ %>
				<td align="right" nowrap="true">车辆生产日期:</td>
				<td align="left" nowrap="true"><%=  CommonUtils.checkNull(Utility.handleDate(veh.getProductDate())) %>&nbsp;</td>
				<% } %>
				
				<td align="right" nowrap="true">客户姓名:</td>
				<td align="left" nowrap="true"><%=CommonUtils.getDataFromMap(customerMap,"CTM_NAME")%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">VIN:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getVin())%>&nbsp;</td>
				<td align="right" nowrap="true">索赔类型:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getClaimName())%>&nbsp;&nbsp; <%=CommonUtils.checkNull(tawep.getActivityName())%> </td>
				<td align="right" nowrap="true">客户电话:</td>
				<td align="left" nowrap="true"><%=CommonUtils.getDataFromMap(customerMap,"MAIN_PHONE")%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">发动机号:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getEngineNo())%></td>
				<td align="right" nowrap="true">车辆购车日期:</td>
				<td align="left" nowrap="true"><%=CommonUtils.printDate(tawep.getPurchasedDate())%>&nbsp;</td>
				<td align="right" nowrap="true">送修人电话:</td>
				<td align="left" nowrap="true"><%=CommonUtils.getDataFromMap(customerMap,"MAIN_PHONE")%></td>
			</tr>
			<tr>
				<td align="right" nowrap="true">配置:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getPackageName()) %>&nbsp;</td>
				<td align="right" nowrap="true">行驶里程:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getInMileage()) %>&nbsp;</td>
				<td align="right" nowrap="true">维修次数:</td>
				<td align="left" nowrap="true">${count }&nbsp;</td>
			</tr>
			<!--
			<tr>
				<td align="right" nowrap="true">工单号:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getRoNo())%>&nbsp;</td>
				<td align="right" nowrap="true">工单开始时间:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(Utility.handleDate(tawep.getRoStartdate()))%>&nbsp;</td>
				<td align="right" nowrap="true">工单结束时间:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(Utility.handleDate(tawep.getRoEnddate())) %>&nbsp;</td>
				<td align="right" nowrap="true">车系:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getSeriesName()) %>&nbsp;</td>
				<td align="right" nowrap="true">车型:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getModelName()) %>&nbsp;</td>
				<td align="right" nowrap="true">送修人姓名:</td>
				<td align="left" nowrap="true"><%=CommonUtils.getDataFromMap(customerMap,"CTM_NAME")%></td>
			</tr>
			-->
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
				<th colspan="10" align="left">
					<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
					&nbsp;<a href="#" onclick="tabDisplayControl('labourTable')">申请单-索赔工时明细</a>
				</th>
			</tr>
		</table>
		<table class="table_list" name="all_group_" style="border-bottom: 1px solid #DAE0EE;" id="labourTable">
			<tr class='table_list_th'>
			    <!-- <th>操作</th> -->
				<th>索赔工时名称</th>
				<%int codeId = (Integer)request.getAttribute("codeId");%>
				<%if(codeId==Constant.chana_jc){ %>
					<th>顾客问题</th>
				<%} %>
				<th>工时结算数</th>
				<th>工时单价(元)</th>
				<th>工时金额(元)</th>
				<th>作业次数</th>
				<th>工时结算金额(元)</th>
				<th>需要审核级别</th>
				<!-- <th>审核意见</th> -->
				
				<th>换下配件名称</th>
				<th>换上配件名称</th>
				<th>换上配件代码</th>
				<th>配件结算数量</th>
				<th>配件单价</th>
				<th>配件金额</th>
				<th>配件结算金额</th>
				<% if(applicationPartList.size() > 0)
				{%>
				<th>配件抵扣金额</th>	
				<% } %>
				<th>需要审核级别</th>
				<th>责任性质</th>
				<th>审核意见</th>
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
			
			<%
				    
				if(partLs!=null && partLs.size()>0){
				boolean isShow = false;
				for(int j=0;j<partLs.size();j++) {
				    ClaimListBean claimBean = partLs.get(j);
					TtAsWrPartsitemPO mainPartPO = (TtAsWrPartsitemPO)claimBean.getMain(); 
						if( String.valueOf(mainLabourPO.getFirstPart()).equals(String.valueOf(mainPartPO.getPartId()))){
			%>
		     	 <tr class="<%=(i%2)==0?"table_list_row1":"table_list_row2"%>">
			     	 <!-- <td>
			     	 	<input type="hidden" name="LABOUR_ID" value="<%=mainLabourPO.getLabourId()%>"/>
					    <img src="<%=contextPath%>/img/nolines_minus.gif" alt="查询对应索赔配件" onclick="showPart('<%=mainLabourPO.getWrLabourcode()%>',this)"/>
			     	 </td> -->
				     <td><%=CommonUtils.checkNull(mainLabourPO.getWrLabourname() )%>&nbsp;</td>
				     <%if(codeId==Constant.chana_jc){ %>
						<td><%=CommonUtils.checkNull(mainLabourPO.getTroubleType() )%>&nbsp;</td>
					 <%} %>
				     <td><%=CommonUtils.checkNull(mainLabourPO.getBalanceQuantity()) %>&nbsp;</td>
				     <td><%=CommonUtils.currencyFormat(mainLabourPO.getLabourPrice(),minFractionDigits,defaultValue)%>&nbsp;</td>
				     <td><%=CommonUtils.currencyFormat(mainLabourPO.getLabourAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
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
				     <td>
				     	<%=authLevel %>&nbsp;
				     </td>
				     <!-- <td><%=CommonUtils.checkNull(mainLabourPO.getAuthRemark()) %></td> -->
				     
				     <td><%=CommonUtils.checkNull(mainPartPO.getDownPartName()) %>&nbsp;
							         <input type="hidden" name="PART_ID" value="<%=mainPartPO.getPartId() %>"/>
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
							    </td>
							    <%
								    if(applicationPartList.size() > 0)
								    {
								       for(TtAsWrApplicationPartPO applicationPartPO: applicationPartList)
								       {
								       	  if((""+applicationPartPO.getPartId()).equals( ""+mainPartPO.getPartId()) )
								       	  {%>
								       	  	  <td>
										     	<%= applicationPartPO.getPartDiscount() %>
										     </td>
								       	 <% }
								       }
								    }
							    %>
							    
							     <td>
							     	<%=authLevel %>&nbsp;
							     </td>
							     <td>
							     	<script type="">
							     		document.write(getItemValue(<%=CommonUtils.checkNull(mainPartPO.getResponsibilityType())%>));
							     	</script>
							     </td>
							     <td><%=CommonUtils.checkNull(mainPartPO.getAuthRemark()) %>&nbsp;</td>
				    </tr>  
				    
				    <%} %>
				     <%  if(mainLabourPO.getWrLabourcode().equals(mainPartPO.getWrLabourcode())&&!(String.valueOf(mainLabourPO.getFirstPart())).equals(String.valueOf(mainPartPO.getPartId()))){%>
				     
				    <tr class="<%=(i%2)==0?"table_list_row1":"table_list_row2"%>">
				     <td><%=CommonUtils.checkNull(mainLabourPO.getWrLabourname() )%>&nbsp;</td>
				     <%if(codeId==Constant.chana_jc){ %>
						<td><%=CommonUtils.checkNull(mainLabourPO.getTroubleType() )%>&nbsp;</td>
					 <%} %>
				     <td><%=CommonUtils.checkNull(mainLabourPO.getBalanceQuantity()) %>&nbsp;</td>
				     <td><%=CommonUtils.currencyFormat(mainLabourPO.getLabourPrice(),minFractionDigits,defaultValue)%>&nbsp;</td>
				     <td><%=CommonUtils.currencyFormat(mainLabourPO.getLabourAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
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
				     <td>
				     	<%=authLevel %>&nbsp;
				     </td>
				     <!-- <td><%=CommonUtils.checkNull(mainLabourPO.getAuthRemark()) %></td> -->
				    
				     <td><%=CommonUtils.checkNull(mainPartPO.getDownPartName()) %>&nbsp;
							         <input type="hidden" name="PART_ID" value="<%=mainPartPO.getPartId() %>"/>
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
							    </td>
							     <%
								    if(applicationPartList.size() > 0)
								    {
								       for(TtAsWrApplicationPartPO applicationPartPO: applicationPartList)
								       {
								       	  if((""+applicationPartPO.getPartId()).equals( ""+mainPartPO.getPartId()) )
								       	  {%>
								       	  	  <td>
										     	<%= applicationPartPO.getPartDiscount() %>
										     </td>
								       	 <% }
								       }
								    }
							    %>
							    
							     <td>
							     	<%=authLevel %>&nbsp;
							     </td>
							     <td>
							     	<script type="">
							     		document.write(getItemValue(<%=CommonUtils.checkNull(mainPartPO.getResponsibilityType())%>));
							     	</script>
							     </td>
							     <td><%=CommonUtils.checkNull(mainPartPO.getAuthRemark()) %>&nbsp;</td>
				
				 </tr>
							    <%}}}%>
			<%} %>
			
		</table>
		
		<c:if test="${outInfo==null}">
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
		<table class="table_edit" id="amountTabId" style="display:none">
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
				<td align="right" nowrap="true">追加工时原因:</td>
				<td align="left"><%=CommonUtils.checkNull(tawep.getAppendlabourReason())%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">追加工时数量：</td>
				<td align="left"><%=CommonUtils.checkNull(tawep.getAppendlabourNum())%>&nbsp;</td>
				<td align="right" nowrap="true">追加工时金额：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getAppendlabourAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">追加费用：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getAppendAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">材料费打折金额：</td>
				<td align="left"><%=CommonUtils.checkNull(tawep.getPartDown())%>&nbsp;</td>
				<td align="right" nowrap="true">工时费打折金额：</td>
				<td align="left"><%=CommonUtils.checkNull(tawep.getLabourDown())%>&nbsp;</td>
				<td align="right" nowrap="true">索赔总金额：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getBalanceAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
			</tr>
		</table>
		</c:if>
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
				<th>审核意见</th>
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
					<td>
						<%=CommonUtils.checkNull(otherLs.get(i).getAuditCon()) %>&nbsp;
					</td>
		   		</tr>
		   	<%}%>
		</table>
	</td></tr>
	<%} %>
	<tr><td>
		<c:if test="${outInfo!=null}">
	<tr>
		<td>
			<table class="table_edit">
				<tr>
					<th colspan="10" align="left"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
					&nbsp;<a href="#" onclick="tabDisplayControl('outTable')">外出维修信息</a>
					</th>
		 		</tr>
		 	</table>
	    	<table class="table_edit" style="border-bottom: 1px solid #DAE0EE;" id="outTable">
				<tr>
					<td width="10%" align="right">外出人员：</td>
					<td width="15%" align="left">${outInfo.outPerson}</td>
					<td width="10%" align="right">产差地：</td>
					<td width="25%" align="left">${outInfo.outSite}</td>
				</tr>
				<tr>
					<td width="10%" align="right">开始时间：</td>
					<td width="15%" align="left"><fmt:formatDate value='${outInfo.startTime}' pattern='yyyy-MM-dd'/></td>
					<td width="10%" align="right">结束时间：</td>
					<td width="25%" align="left"><fmt:formatDate value='${outInfo.endTime}' pattern='yyyy-MM-dd'/></td>
				</tr>
				<tr>
					<td width="10%" align="right">派车车牌号：</td>
					<td width="15%" align="left">${outInfo.outLicenseno}</td>
					<td width="10%" align="right">出发地：</td>
					<td width="25%" align="left">${outInfo.fromAdress}</td>
				</tr>
				<tr>
					<td width="10%" align="right">目的地址：</td>
					<td width="15%" align="left">${outInfo.endAdress}</td>
					<td width="10%" align="right">总里程：</td>
					<td width="25%" align="left">${outInfo.outMileage}</td>
				</tr>
	    	</table>
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
		<table class="table_edit" id="amountTabId" style="display:none">
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
				<td align="right" nowrap="true">追加工时原因:</td>
				<td align="left"><%=CommonUtils.checkNull(tawep.getAppendlabourReason())%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">追加工时数量：</td>
				<td align="left"><%=CommonUtils.checkNull(tawep.getAppendlabourNum())%>&nbsp;</td>
				<td align="right" nowrap="true">追加工时金额：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getAppendlabourAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">追加费用：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getAppendAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">材料费打折金额：</td>
				<td align="left"><%=CommonUtils.checkNull(tawep.getPartDown())%>&nbsp;</td>
				<td align="right" nowrap="true">工时费打折金额：</td>
				<td align="left"><%=CommonUtils.checkNull(tawep.getLabourDown())%>&nbsp;</td>
				<td align="right" nowrap="true">索赔总金额：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getBalanceAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
	
	</c:if>
		<table class="table_edit">
			<tr>
				<th colspan="10" align="left"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
				&nbsp;<a href="#" onclick="tabDisplayControl('authTable')">索赔单审核明细</a>
				</th>
		 	</tr>
		 </table>
		 <table class="table_list" style="border-bottom: 1px solid #DAE0EE;display:none" id="authTable">
			<tr class='table_list_th'>
				<th nowrap="true">序号</th>
				<th nowrap="true">授权人员</th>
				<th nowrap="true">授权角色</th>
				<th nowrap="true">日期</th>
				<th nowrap="true">授权结果</th>
				<th nowrap="true">授权代码</th>
				<th nowrap="true">授权备注</th>
			</tr>
			<%for (int i=0;i<appAuthls.size();i++) { %>
			    <tr class="<%=(i%2)==0?"table_list_row1":"table_list_row2"%>">
			    	<td><%=i+1%></td>
					<td><%=CommonUtils.checkNull(appAuthls.get(i).getApprovalPerson()) %>&nbsp;</td>
					<td><%=CommonUtils.checkNull(appAuthls.get(i).getApprovalLevelCode()) %>&nbsp;</td>
					<td><%=CommonUtils.checkNull(Utility.handleFormatDate(appAuthls.get(i).getCreateDate())) %>&nbsp;</td>
					<td>
					    <%=CommonUtils.checkNull(appAuthls.get(i).getApprovalResult()) %>&nbsp;
					</td>
					<td><%=CommonUtils.checkNull(appAuthls.get(i).getAuthorizedCode()).replaceAll("\\d","*") %>&nbsp;</td>
					<td width="300"><%=CommonUtils.checkNull(appAuthls.get(i).getRemark()) %>&nbsp;</td>
		   		</tr>
		   	<%}%>
		</table>
		<br/>
	</td></tr>
	<tr><td>
	<!-- 索赔申请单之授权原因(只在审核页面显示) -->
	<%if(isAudit){%>
		<div align="left">
			需授权原因：<%=CommonUtils.checkNull(authReason.getApprovalReason())%>
			<br/>
			审核意见：<input name="CON_REMARK" id="CON_REMARK" datatype="1,is_null,300" type="text"  class="middle_txt"/>
		</div>
	<%} %>
	<br/>
		<div align="center"> 
			<div align="center"><input style="width:150px;color:#1E3988;
			height:16px;	/*为了文本框高度对齐*/
			line-height:10px;
			background-color:#EEF0FC;
			border:1px solid #5E7692;
			cursor: pointer;" type="button" value="站长三包员联系信息" name="contactInfo"
		 onclick="openWindowDialog('<%=contextPath%>/sysusermng/sysuserinfo/SysPasswordManager/showContactPhone.do?DEALER_ID=<%=tawep.getDealerId()%>');"/>
		<input class="long_btn" style="display: none" type="button" value="质量信息跟踪卡" onclick="openWindowDialog('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getQualityCard.do?claimId=<%=tawep.getId() %>');"/> 
		<input class="normal_btn" type="button" value="维修历史" name="historyBtn"
		 onclick="openWindowDialog('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN=<%=tawep.getVin()%>');"/>
		<input class="normal_btn" type="button" value="授权历史" name="historyBtn"
		 onclick="openWindowDialog('<%=contextPath%>/repairOrder/RoMaintainMain/authDetail.do?VIN=<%=tawep.getVin()%>');"/>
		<input class="normal_btn" type="button" value="保养历史" name="historyBtn" 
		onclick="openWindowDialog('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/freeMaintainHistory.do?VIN=<%=tawep.getVin()%>');"/>
		<%if(isAudit){//只在审核时使用%>
	 		 <input class="normal_btn"	type="button" value="同意" name="bt_back2" onclick="confirmCustom('<%=Constant.CLAIM_APPLY_ORD_TYPE_04%>','fm',this);"/>
	         <input class="normal_btn" type="button" value="退回" name="bt_back3" onclick="confirmCustom('<%=Constant.CLAIM_APPLY_ORD_TYPE_06%>','fm',this);"/>
	         <input class="normal_btn" type="button" value="拒绝" name="bt_back" onclick="confirmCustom('<%=Constant.CLAIM_APPLY_ORD_TYPE_05%>','fm',this);"/>
	         <input class="normal_btn" type="button" value="返回" onclick="parent.window._hide();"/>
	 	<%} else {%>
		     <input class="normal_btn" type="button" value="关闭" onclick="closeWindow();"/>
		<%} %>
	</div>
		<input type="hidden" name="claimId" value="<%=tawep.getId() %>"/><!-- 索赔申请单ID -->
	</td></tr>
	</table>
</form>
<script type="text/javascript">
      var tempBtn = null;
      //通过JSON提交审核结果
	  function jsonSubmit(statusValue,frmName,btn){
		  createTip();
		  var turl = '<%=contextPath%>'+'/claim/application/ClaimManualAuditing/manualAuditing.json?status='+statusValue;;
		  makeNomalFormCall(turl,showResult,frmName);
		  disableLink(btn,false);
	  }

      //确定审核
	  function confirmCustom(statusValue,frmName,btn) {
		  	var cremark = document.getElementById('CON_REMARK').value;
		  	if(btn.name=='bt_back2' || (cremark!=null && cremark!='' && cremark!='Undefined')){//备注信息不为空
			  	var flag = window.confirm('是否确认审核' + btn.value + '？');
		  	    if(flag){
		  	    	jsonSubmit(statusValue,frmName,btn);
		  	    }
				//MyDivConfirm("是否确认审核" + btn.value + "？",jsonSubmit,[statusValue,frmName,btn]);
		  	}else{
			  	MyAlert("请填写审核意见！");
		  		//MyDivAlert("请填写审核意见！");
		  	}
	  }

      //审核成后跳转到审核首页
	  function showResult(json){
			//if(json.ACTION_RESULT == '1'){
				 //location.href = '<%=contextPath%>' + json.FORWORD_URL;
			//}
		   parent.window._hide();
		   parentContainer.__extQuery__(1);
	  }

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
</script>
</body>
</html>
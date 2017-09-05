<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@taglib uri="/jstl/cout" prefix="c"%>
 <%@taglib uri="/jstl/fmt" prefix="fmt"%>
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
<%@page import="com.infodms.dms.po.TtAsWrPartsitemPO"%>
<%@page import="com.infodms.dms.po.TcCodePO"%>
<%@page import="com.infodms.dms.dao.claim.dealerClaimMng.*"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();%>
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>索赔申请单结算审核</title>
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
	List<TtAsWrAppauthitemPO> appAuthls = (List<TtAsWrAppauthitemPO>)request.getAttribute("appAuthls");//授权信息
	String id = (String)request.getAttribute("ID");//索赔申请单ID
	TtAsWrWrauthorizationPO authReason = (TtAsWrWrauthorizationPO)request.getAttribute("authReason");//需要授权原因-在授权审核时呈现
	List<TcCodePO> statusList = (List<TcCodePO>)request.getAttribute("statusList");//是否同意状态
	Map<String,Object> customerMap = (Map<String,Object>)request.getAttribute("customerMap");//客户信息
	String claimId = (String)request.getAttribute("claimId");//索赔单Id
	Map<String,Object> statusMap = new HashMap<String,Object>();
	for(int k=0;k<statusList.size();k++){
		TcCodePO codePO = statusList.get(k);
		statusMap.put(codePO.getCodeId(),codePO.getCodeDesc());
	}
	if(authReason==null){
		authReason = new TtAsWrWrauthorizationPO();
	}
	String isRedo=(String)request.getAttribute("isRedo");
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
		<input type="hidden" name="isRedo" id="isRedo" value="<%=isRedo%>" />
		<input type="hidden" id="IN_MILEAGE" value="<%=CommonUtils.checkNull(tawep.getInMileage()) %>"/>
		<input type="hidden" id="VIN" value="<%=CommonUtils.checkNull(tawep.getVin())%>"/>
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
				<td align="right" nowrap="true">车辆生产日期:</td>
				<td align="left" nowrap="true"><%=CommonUtils.printDate(tawep.getProductDate())%>&nbsp;</td>
				<%if(!Constant.CLA_TYPE_07.toString().equals(tawep.getClaimType().toString())){ %>	
				<td align="right" nowrap="true">客户姓名:</td>
				<td align="left" nowrap="true"><%=CommonUtils.getDataFromMap(customerMap,"CTM_NAME")%></td>
				<%} %>
			</tr>
			<tr>
				<td align="right" nowrap="true">VIN:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getVin())%>&nbsp;</td>
				<td align="right" nowrap="true">索赔类型:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getClaimName())%>&nbsp;</td>
				<%if(!Constant.CLA_TYPE_07.toString().equals(tawep.getClaimType().toString())){ %>	
				<td align="right" nowrap="true">客户电话:</td>
				<td align="left" nowrap="true"><%=CommonUtils.getDataFromMap(customerMap,"MAIN_PHONE")%></td>
				<%} %>
			</tr>
			<tr>
				<td align="right" nowrap="true">发动机号:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getEngineNo())%></td>
				<%if(!Constant.CLA_TYPE_07.toString().equals(tawep.getClaimType().toString())){ %>	
				<td align="right" nowrap="true">购车日期:</td>
				<td align="left" nowrap="true"><%=CommonUtils.printDate(tawep.getPurchasedDate())%>&nbsp;</td>
				<%} %>
				<td align="right" nowrap="true">送修人电话:</td>
				<td align="left" nowrap="true"><%=CommonUtils.getDataFromMap(customerMap,"MAIN_PHONE")%>&nbsp;</td>
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
			<tr style="display:none">
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
				<td align="right" nowrap="true">DQV认证标识:</td>
				<td align="left" nowrap="true">
					<script>
						writeItemValue(<%=tawep.getIsDqv()%>);
					</script>
				</td>
				<%if(Constant.CLA_TYPE_06.toString().equals(tawep.getClaimType().toString())){%>
				<td align="right" nowrap="true">活动代码:</td>
				<td align="left" nowrap="true">${activity.activityCode }</td>
				<td align="right" nowrap="true">活动名称:</td>
				<td align="left" nowrap="true">${activity.activityName }</td>
			<%} %>
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
		<table class="table_edit" id="troubleTabId">
			<tr>
			    <td align="right" valign="top">故障描述：</td>
				<td align="left">
					&nbsp;<textarea rows="2" cols="60" readonly="readonly"><%=CommonUtils.checkNull(tawep.getTroubleDesc()) %>&nbsp;</textarea>
				</td>
				<td align="right" valign="top">故障原因：</td>
				<td align="left">
					&nbsp;<textarea rows="2" cols="60" readonly="readonly"><%=CommonUtils.checkNull(tawep.getTroubleReason()) %>&nbsp;</textarea>
				</td>
			</tr>
			<tr>
				<td align="right" valign="top">维修措施：</td>
				<td align="left">
					&nbsp;<textarea rows="2" cols="60" readonly="readonly"><%=CommonUtils.checkNull(tawep.getRepairMethod()) %>&nbsp;</textarea>
				</td>
				<td align="right" valign="top">申请备注：</td>
				<td align="left">
					&nbsp;<textarea rows="2" cols="60" readonly="readonly"><%=CommonUtils.checkNull(tawep.getRemark()) %>&nbsp;</textarea>
				</td>
			</tr>
		</table>
	</td></tr>
	<%} %>
	<tr><td>
		
		
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
		<table border="1" class="table_list" name="all_group_"  id="labourTable">
			<tr class=''>
				<%int codeId = (Integer)request.getAttribute("codeId");%>
			    <!-- <th width="5%">操作</th> -->
			    <th width="10%">索赔工时代码</th>
			    <%if(codeId==Constant.chana_jc){ %>
					<th width="10%">索赔工时名称</th>
				<%}else{ %>
					<th width="18%">索赔工时名称</th>
				<%} %>
				<%if(codeId==Constant.chana_jc){ %>
					<th width="8%">顾客问题</th>
				<%} %>
				<th width="5%">工时结算数</th>
				<th width="7%">作业次数</th>
				<th width="10%">工时结算金额</th>
				<!-- <th width="20%">审核意见</th> -->
				
				<th width="10%">换上配件代码</th>
				<th width="15%">换上配件名称</th>
				<th width="5%">配件结算数量</th>
				<th width="6%">换件次数</th>
				<th width="8%">配件结算金额</th>
				<th width="6%">审核意见</th>
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
							
		     	 <tr
		     	 	<%if(mainPartPO.getIsGua().intValue()!=1&&!Constant.CLA_TYPE_07.toString().equals(tawep.getClaimType().toString())){%>
		     	 	class="table_list_row_red"
		     	 	<%}else{ %>
		     	 		<%if(mainLabourPO.getIsFore().intValue()>=1){%>
		     	 			class="table_list_row_light"
		     	 		<%}else{ %>
		     	 			<%if(mainPartPO.getIsFore().intValue()>=1){%>
		     	 				class="table_list_row_light"
		     	 			<%} %>
		     	 		<%} %>
		     	 	<%} %>
			     >
			     	 <!-- <td>
			     	 	<input type="hidden" name="LABOUR_ID" value="<%=mainLabourPO.getLabourId()%>"/>
					    <img src="<%=contextPath%>/img/nolines_minus.gif" alt="查询对应索赔配件" onclick="showPart('<%=mainLabourPO.getWrLabourcode()%>',this)"/>
			     	 </td> -->
			     	 <td>
			     	 <input type="hidden" name="LABOUR_ID" value="<%=mainLabourPO.getLabourId()%>"/>
			     	 <%=CommonUtils.checkNull(mainLabourPO.getWrLabourcode() )%>&nbsp;</td>
				     <td ><%=CommonUtils.checkNull(mainLabourPO.getWrLabourname() )%>&nbsp;</td>
				     <%if(codeId==Constant.chana_jc){ %>
						<td><%=CommonUtils.checkNull(mainLabourPO.getTroubleType() )%>&nbsp;</td>
					 <%} %>
				     <td>
				     	&nbsp;&nbsp;&nbsp;<input style="text-align:center" type="text" name="LOBOURCOUNT" id="LOBOURCOUNT<%=i%>" class="short_txt" datatype="0,is_double,20" decimal="2"  
				     	value="<%=CommonUtils.checkNull(mainLabourPO.getBalanceQuantity()) %>" price="<%=mainLabourPO.getLabourPrice()%>" 
				     	prevValue="<%=CommonUtils.checkNull(mainLabourPO.getBalanceQuantity())%>" limitValue="<%=CommonUtils.checkNull(mainLabourPO.getLabourAmount())%>" 
				     	onkeyup="linkAgeOver('LOBOURCOUNT<%=i%>','LOBOURAMOUNT<%=i%>',true,true);linkAge('LOBOURCOUNT<%=i%>','labourCountId',false);linkAge('LOBOURCOUNT<%=i%>','partAmountId',false);"/>&nbsp;
				     </td>
				     <td><%="".equals(CommonUtils.checkNull(mainLabourPO.getLabourCount()))?
				    				 "0":CommonUtils.checkNull(mainLabourPO.getLabourCount())%>
				     </td>
				     <td align="center">
			     		  <input style="text-align:center;width: 60px" name="LOBOURAMOUNT" id="LOBOURAMOUNT<%=i%>" type="text" class="no_border_txt"
			     		  datatype="1,is_double,20" decimal="2" readonly="readonly"
			     		  value="<%=CommonUtils.checkNull(mainLabourPO.getBalanceAmount())%>"
			     		  prevValue="<%=CommonUtils.checkNull(mainLabourPO.getBalanceAmount())%>" 
			     		  nextDeal="linkAge('LOBOURAMOUNT<%=i%>','labourAmountId',false);"/>
				     </td>
					 <!-- <td>
					 	 <input type="text" name="LOBOURREMARK" id="LOBOURREMARKID<%=i%>" class="normal_txt" value="<%=CommonUtils.checkNull(mainLabourPO.getAuthRemark()) %>" datatype="1,is_null,100"/>
					 </td> -->
					 
					 
					<td><%=CommonUtils.checkNull(mainPartPO.getPartCode()) %>&nbsp;
						<input type="hidden" name="PART_ID" value="<%=mainPartPO.getPartId() %>"/>
						<input type="hidden" name="PART_CODE" value="<%=CommonUtils.checkNull(mainPartPO.getPartCode()) %>"/>
					</td>
					<td 
					><%=CommonUtils.checkNull(mainPartPO.getPartName()) %>&nbsp;</td>
					<td>
							     <input style="text-align:center" type="text" name="PARTCOUNT" id="PARTCOUNT<%=j%>" 
							     	datatype="0,is_double,20" decimal="2"  class="short_txt" 
							     	value="<%=CommonUtils.checkNull(mainPartPO.getBalanceQuantity()) %>" price="<%=mainPartPO.getPrice()%>" 
							     	prevValue="<%=CommonUtils.checkNull(mainPartPO.getBalanceQuantity())%>" limitValue="<%=CommonUtils.checkNull(mainPartPO.getAmount())%>" 
							     	onkeyup="linkAgeOver('PARTCOUNT<%=j%>','PARTAMOUNT<%=j%>',true,true);linkAge('PARTCOUNT<%=j%>','partCountId',false);"/>&nbsp;
					</td>	     
					<td><%=dao.getCount(claimId,CommonUtils.checkNull(mainPartPO.getPartCode()))%></td>
					<td align="center">
								      <input type="hidden" name="PARTCOUNT_APPLY<%=j%>" value="<%=mainPartPO.getQuantity() %>"></input>
									  <input style="text-align:center" name="PARTAMOUNT" id="PARTAMOUNT<%=j%>" type="text" class="short_txt" 
									  	 decimal="2" 
							     		value="<%=CommonUtils.checkNull(mainPartPO.getBalanceAmount())%>"
							     		prevValue="<%=CommonUtils.checkNull(mainPartPO.getBalanceAmount())%>" 
							     		nextDeal="linkAge('PARTAMOUNT<%=j%>','partAmountId',false);"
							     		onblur="isCheck(this.id,this.value,<%=j%>,<%=mainPartPO.getPrice() %>);"
							     		/> 
					</td> 	   
					<td>&nbsp;&nbsp;<input type="text" name="PARTREMARK" id="PARTREMARKID<%=i%>" class="normal_txt" value="<%=CommonUtils.checkNull(mainPartPO.getAuthRemark()) %>" datatype="1,is_null,100"/></td>
				    </tr>  
				    
				    <%} %>
							  <%  if(mainLabourPO.getWrLabourcode().equals(mainPartPO.getWrLabourcode())&&!(String.valueOf(mainLabourPO.getFirstPart())).equals(String.valueOf(mainPartPO.getPartId()))){%>
								<tr
							<%if(mainPartPO.getIsGua().intValue()!=1&&!Constant.CLA_TYPE_07.toString().equals(tawep.getClaimType().toString())){%>
		     	 		class="table_list_row_red"
		     	 	<%}else{ %>
		     	 		<%if(mainLabourPO.getIsFore().intValue()>=1){%>
		     	 			class="table_list_row_light"
		     	 		<%}else{ %>
		     	 			<%if(mainPartPO.getIsFore().intValue()>=1){%>
		     	 				class="table_list_row_light"
		     	 			<%} %>
		     	 		<%} %>
		     	 	<%} %>
								>
								<td></td>
								<td></td>
								<%if(codeId==Constant.chana_jc){ %>
								<td></td>
								<%} %>
								<td></td>
								<td></td>
								<td></td>
								<td><%=CommonUtils.checkNull(mainPartPO.getPartCode()) %>&nbsp;
								<input type="hidden" name="PART_ID" value="<%=mainPartPO.getPartId() %>"/>
								<input type="hidden" name="PART_CODE" value="<%=CommonUtils.checkNull(mainPartPO.getPartCode()) %>"/>
								</td>
								<td><%=CommonUtils.checkNull(mainPartPO.getPartName()) %>&nbsp;</td>
								<td>
										     <input style="text-align:center" type="text" name="PARTCOUNT" id="PARTCOUNT<%=j%>" 
										     	datatype="0,is_double,20" decimal="2"  class="short_txt" 
										     	value="<%=CommonUtils.checkNull(mainPartPO.getBalanceQuantity()) %>" price="<%=mainPartPO.getPrice()%>" 
										     	prevValue="<%=CommonUtils.checkNull(mainPartPO.getBalanceQuantity())%>" limitValue="<%=CommonUtils.checkNull(mainPartPO.getAmount())%>" 
										     	onkeyup="linkAgeOver('PARTCOUNT<%=j%>','PARTAMOUNT<%=j%>',true,true);linkAge('PARTCOUNT<%=j%>','partCountId',false);"/>&nbsp;
								</td>	     
								<td><%=dao.getCount(claimId,CommonUtils.checkNull(mainPartPO.getPartCode()))%></td>
								<td align="center">
								      <input type="hidden" name="PARTCOUNT_APPLY<%=j%>" value="<%=mainPartPO.getQuantity() %>"></input>
									  <input style="text-align:center" name="PARTAMOUNT" id="PARTAMOUNT<%=j%>" type="text" class="short_txt" 
									  	 decimal="2" 
							     		value="<%=CommonUtils.checkNull(mainPartPO.getBalanceAmount())%>"
							     		prevValue="<%=CommonUtils.checkNull(mainPartPO.getBalanceAmount())%>" 
							     		nextDeal="linkAge('PARTAMOUNT<%=j%>','partAmountId',false);"
							     		onblur="isCheck(this.id,this.value,<%=j%>,<%=mainPartPO.getPrice() %>);"
							     		/> 
								</td> 	   
								<td>&nbsp;&nbsp;<input type="text" name="PARTREMARK" id="PARTREMARKID<%=i%>" class="normal_txt" value="<%=CommonUtils.checkNull(mainPartPO.getAuthRemark()) %>" datatype="1,is_null,100"/></td>
								</tr>
							    <%}}}%>
			<%} %>
		</table>
		<c:if test="${listOutRepair==null}">
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
				<!-- <td align="right" nowrap="true">工时数量：</td>
				<td align="left" id="labourCountId" value="<%=CommonUtils.checkNull(tawep.getLabourHours())%>">
					<%=CommonUtils.checkNull(tawep.getLabourHours())%>&nbsp;
				</td> -->
				<td align="right" nowrap="true">工时金额：</td>
				<td align="left" id="labourAmountId" value="<%=CommonUtils.checkNull(tawep.getBalanceLabourAmount())%>" 
				prevValue="<%=CommonUtils.checkNull(tawep.getBalanceLabourAmount())%>" price="1"
				nextDeal="linkAge('labourAmountId','totalAmountId',false)">
					<%=CommonUtils.currencyFormat(tawep.getBalanceLabourAmount(),minFractionDigits,defaultValue)%>&nbsp;
				</td>
				<!--<td align="right" nowrap="true">配件数量：</td>
				<td align="left" id="partCountId" value="<%=CommonUtils.checkNull(tawep.getPartsCount())%>">
					<%=CommonUtils.checkNull(tawep.getPartsCount())%>&nbsp;
				</td> -->
				<td align="right" nowrap="true">配件金额：</td>
				<td align="left" id="partAmountId" value="<%=CommonUtils.checkNull(tawep.getBalancePartAmount())%>" 
				prevValue="<%=CommonUtils.checkNull(tawep.getBalancePartAmount())%>" price="1"
				nextDeal="linkAge('partAmountId','totalAmountId',false)">
					<%=CommonUtils.currencyFormat(tawep.getBalancePartAmount(),minFractionDigits,defaultValue)%>&nbsp;
				</td>
				<td align="right" nowrap="true">保养金额：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getFreeMPrice(),minFractionDigits,defaultValue)%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">其他项目金额：</td>
				<td align="left" id="otherAmountId" value="<%=CommonUtils.checkNull(tawep.getBalanceNetitemAmount())%>"
					prevValue="<%=CommonUtils.checkNull(tawep.getBalanceNetitemAmount())%>"
					nextDeal="linkAge('otherAmountId','totalAmountId',false)">
					<%=CommonUtils.currencyFormat(tawep.getBalanceNetitemAmount(),minFractionDigits,defaultValue)%>&nbsp;
				</td>
				<td align="right" nowrap="true">服务活动金额：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getCampaignFee(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">追加工时数量：</td>
				<td align="left" id="aLabourId" value="0" price="<%=CommonUtils.checkNull(tawep.getLabourPrice())%>"
					nextDeal="linkAgeOver('aLabourId','aLabourAmountId',true);">
					<%=CommonUtils.checkNull(tawep.getAppendlabourNum())%>&nbsp;
				</td>
				<td align="right" nowrap="true">&nbsp;</td>
				<td align="left">&nbsp;</td>
			</tr>
			<tr>
				
				<td align="right" nowrap="true">追加工时金额：</td>
				<td align="left" id="aLabourAmountId" value="0" prevValue="<%=tawep.getAppendlabourAmount()%>" price="1" 
				    nextDeal="linkAge('aLabourAmountId','totalAmountId',false);">
					<%=CommonUtils.currencyFormat(tawep.getAppendlabourAmount(),minFractionDigits,defaultValue)%>&nbsp;
				</td>
				<td align="right" nowrap="true">追加费用：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getAppendAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">索赔总金额：</td>
				<td align="left" id="totalAmountId" value="<%=CommonUtils.checkNull(tawep.getBalanceAmount())%>">
				<%=CommonUtils.currencyFormat(tawep.getBalanceAmount(),minFractionDigits,defaultValue)%>&nbsp;
				</td>
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
				<th>授权批示</th>
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
			     	<input name="OTHERAMOUNT" value="<%=CommonUtils.checkNull(otherLs.get(i).getBalanceAmount())%>" 
			     	      id="OTHERAMOUNT<%=i%>" type="text" 
			     	      prevValue="<%=CommonUtils.checkNull(otherLs.get(i).getBalanceAmount())%>" 
			     	      limitValue="<%=CommonUtils.checkNull(otherLs.get(i).getBalanceAmount())%>" 
			     	      price="<%=CommonUtils.checkNull(otherLs.get(i).getBalanceAmount())%>" 
			     	      onblur="javascript:checkMoney(<%=i %>,<%=CommonUtils.checkNull(otherLs.get(i).getBalanceAmount()) %>,<%=CommonUtils.checkNull(otherLs.get(i).getAmount()) %>);"
			     	      />	
					</td>
				     <td>
						  <%=CommonUtils.getDataFromMap(statusMap,otherLs.get(i).getIsAgree().toString())%>
					</td>
					<td>
						<input type="text" name="audit_con" class="middle_txt" value="<%=CommonUtils.checkNull(otherLs.get(i).getAuditCon()) %>" />
					</td>
		   		</tr>
		   	<%}%>
		</table>
	</td></tr>
	
	<%} %>
	
	<c:if test="${listOutRepair!=null}">
	<tr><td>
		<table class="table_edit">
			<tr>
				<th colspan="10" align="left">
				    <img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
					&nbsp;<a href="#" onclick="tabDisplayControl('outId')">申请单-外出维修</a>
				</th>
			</tr>
		</table>
		<table class="table_edit" style="border-bottom: 1px solid #DAE0EE;" id="outId">
			 <tr class="table_list_th">
          	<td align="right">
          	开始时间：
          	</td>
          	<td nowrap="nowrap" id="startTime">
          	<fmt:formatDate value='${listOutRepair.START_TIME}' pattern='yyyy-MM-dd HH:mm' />
			</td>
          	<td align="right">
          	结束时间：
          	</td>
          	<td align="left" id="endTime">
         	 <fmt:formatDate value='${listOutRepair.END_TIME}' pattern='yyyy-MM-dd HH:mm' />
          	</td>
          	<td align="right">
          	派车车牌号：
          	</td>
          	<td align="left">
        	${listOutRepair.OUT_LICENSENO}  
          	</td>
          	</tr>
          	 <tr class="table_list_th">
          	<td align="right">
          	外出人：
          	</td>
          	<td align="left">
			${listOutRepair.OUT_PERSON}  
          	</td>
          	<td align="right">
          	出差地：
          	</td>
          	<td align="left">
          	${listOutRepair.OUT_SITE}  
          	</td>
          	<td align="right">
          	出发地址：
          	</td>
          	<td align="left">
			${listOutRepair.FROM_ADRESS}  
          	</td>
          	</tr>
          	 <tr class="table_list_th">
          	<td align="right">
          	目的地址：
          	</td>
          	<td align="left">
          	
          	${listOutRepair.END_ADRESS}  
          	</td>
          	<td align="right">
          	总里程：
          	</td>
          	<td align="left"> 
          	${listOutRepair.OUT_MILEAGE} 
          	</td>
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
		<table class="table_edit" id="amountTabId">
			<tr>
				<!-- <td align="right" nowrap="true">工时数量：</td>
				<td align="left" id="labourCountId" value="<%=CommonUtils.checkNull(tawep.getLabourHours())%>">
					<%=CommonUtils.checkNull(tawep.getLabourHours())%>&nbsp;
				</td> -->
				<td align="right" nowrap="true">工时金额：</td>
				<td align="left" id="labourAmountId" value="<%=CommonUtils.checkNull(tawep.getBalanceLabourAmount())%>" 
				prevValue="<%=CommonUtils.checkNull(tawep.getBalanceLabourAmount())%>" price="1"
				nextDeal="linkAge('labourAmountId','totalAmountId',false)">
					<%=CommonUtils.currencyFormat(tawep.getBalanceLabourAmount(),minFractionDigits,defaultValue)%>&nbsp;
				</td>
				<!--<td align="right" nowrap="true">配件数量：</td>
				<td align="left" id="partCountId" value="<%=CommonUtils.checkNull(tawep.getPartsCount())%>">
					<%=CommonUtils.checkNull(tawep.getPartsCount())%>&nbsp;
				</td> -->
				<td align="right" nowrap="true">配件金额：</td>
				<td align="left" id="partAmountId" value="<%=CommonUtils.checkNull(tawep.getBalancePartAmount())%>" 
				prevValue="<%=CommonUtils.checkNull(tawep.getBalancePartAmount())%>" price="1"
				nextDeal="linkAge('partAmountId','totalAmountId',false)">
					<%=CommonUtils.currencyFormat(tawep.getBalancePartAmount(),minFractionDigits,defaultValue)%>&nbsp;
				</td>
				<td align="right" nowrap="true">保养金额：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getFreeMPrice(),minFractionDigits,defaultValue)%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">其他项目金额：</td>
				<td align="left" id="otherAmountId" value="<%=CommonUtils.checkNull(tawep.getBalanceNetitemAmount())%>"
					prevValue="<%=CommonUtils.checkNull(tawep.getBalanceNetitemAmount())%>"
					nextDeal="linkAge('otherAmountId','totalAmountId',false)">
					<%=CommonUtils.currencyFormat(tawep.getBalanceNetitemAmount(),minFractionDigits,defaultValue)%>&nbsp;
				</td>
				<td align="right" nowrap="true">服务活动金额：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getCampaignFee(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">追加工时数量：</td>
				<td align="left" id="aLabourId" value="0" price="<%=CommonUtils.checkNull(tawep.getLabourPrice())%>"
					nextDeal="linkAgeOver('aLabourId','aLabourAmountId',true);">
					<%=CommonUtils.checkNull(tawep.getAppendlabourNum())%>&nbsp;
				</td>
				<td align="right" nowrap="true">&nbsp;</td>
				<td align="left">&nbsp;</td>
			</tr>
			<tr>
				
				<td align="right" nowrap="true">追加工时金额：</td>
				<td align="left" id="aLabourAmountId" value="0" prevValue="<%=tawep.getAppendlabourAmount()%>" price="1" 
				    nextDeal="linkAge('aLabourAmountId','totalAmountId',false);">
					<%=CommonUtils.currencyFormat(tawep.getAppendlabourAmount(),minFractionDigits,defaultValue)%>&nbsp;
				</td>
				<td align="right" nowrap="true">追加费用：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getAppendAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">索赔总金额：</td>
				<td align="left" id="totalAmountId" value="<%=CommonUtils.checkNull(tawep.getBalanceAmount())%>">
				<%=CommonUtils.currencyFormat(tawep.getBalanceAmount(),minFractionDigits,defaultValue)%>&nbsp;
				</td>
			</tr>
		</table>
	</td></tr>
	</c:if>
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
	<div align="left">
	    <%  String claimType = CommonUtils.checkNull(tawep.getClaimType());
	    if(Constant.CLA_TYPE_02.toString().equals(claimType) || Constant.CLA_TYPE_06.toString().equals(claimType)){ %>
	    	<input type="hidden" id="APPEND_LABOUR_ID" name="APPEND_LABOUR" value="<%if(tawep.getAppendlabourNum()!=null){%><%=tawep.getAppendlabourNum()%><%}else{ %>0<%}%>"/>
	    <%}else{ %>
	           追加工时：<input type="text" id="APPEND_LABOUR_ID" name="APPEND_LABOUR"
	            value="<%if(tawep.getAppendlabourNum()!=null){%><%=tawep.getAppendlabourNum()%><%}else{ %>0<%}%>" datatype="0,is_double,100" decimal="2" 
	            price="1" onkeyup="clearNoNum(this);linkAgeOver('APPEND_LABOUR_ID','aLabourId',false);"/>
	    &nbsp;&nbsp;工时单价: <%=CommonUtils.currencyFormat(tawep.getLabourPrice(),minFractionDigits,defaultValue)%>
	    &nbsp;&nbsp;授权人名称: ${APPROVAL_PERSON }
	    	    <br/> 
	    <%} %>
	    <input type="hidden" name="labourPrice" value="<%=tawep.getLabourPrice()%>"/>
		<!--  需授权原因：<%=CommonUtils.checkNull(authReason.getApprovalReason())%>
		<br/>
		审核意见：<input name="CON_REMARK" id="CON_REMARK" datatype="1,is_null,300" type="text"  class="middle_txt" value="<%=CommonUtils.checkNull(tawep.getRemark())%>"/>-->
	</div>
	<br/>
	<div align="center"> 
		<input class="normal_btn" type="button" value="维修历史" name="historyBtn"
		 onclick="openWindowDialog('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN=<%=tawep.getVin()%>');"/>
		<input class="normal_btn" type="button" value="授权历史" name="historyBtn"
		 onclick="openWindowDialog('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/auditingHistory.do?VIN=<%=tawep.getVin()%>');"/>
		<input class="normal_btn" type="button" value="保养历史" name="historyBtn" 
		onclick="openWindowDialog('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/freeMaintainHistory.do?VIN=<%=tawep.getVin()%>');"/>
		<input class="normal_btn"	type="button" value="同意" name="bt_back2" onclick="confirmCustom('<%=Constant.CLAIM_APPLY_ORD_TYPE_04%>','fm',this);"/>
        <input class="normal_btn" type="button" value="返回" onclick="parent.window._hide();"/>
        &nbsp;&nbsp;&nbsp;
        <!-- <input class="long_btn" type="button" id="three_package_set_btn" value="配件三包判定" onclick="threePackageSet();"/> -->
	</div>
		<input type="hidden" name="claimId" value="<%=tawep.getId() %>"/><!-- 索赔申请单ID -->
		<input type="hidden" name="id" value="<%=request.getAttribute("BALANCE_ID")%>"/><!-- 结算单ID -->
	</td></tr>
	</table>

</form>
<script type="text/javascript">
      var tempBtn = null;
      function checkMoney(i,balAmt,amt){
          var curValue=document.getElementById("OTHERAMOUNT"+i).value;
          if(isNaN(curValue)){
              MyAlert('请录入数字!');
              document.getElementById("OTHERAMOUNT"+i).value=balAmt;
              linkAge("OTHERAMOUNT"+i,'partAmountId',false);
          }
          else if(new Number(curValue)>new Number(amt)){
              MyAlert('结算金额不能超过申请金额');
              document.getElementById("OTHERAMOUNT"+i).value=balAmt;
              linkAge("OTHERAMOUNT"+i,'otherAmountId',false);
          }else{
        	  linkAge("OTHERAMOUNT"+i,'otherAmountId',false);  
          }
          changeOtherMoney();
      }
      function changeOtherMoney(){
    	    var table = document.getElementById("otherTable");//数据区域
    		var trs = table.getElementsByTagName('tr');
    		var rowCount=trs.length;
    		var otherAmountCount=0;
    		 for(var i = 0; i <rowCount ; i++){
        		 if(i>0){
        			 var cells = trs[i].cells;
        			 var celCount=parseFloat(cells[5].childNodes[0].value);
        			 otherAmountCount+=celCount;
        		 }
    		 }
    		 document.getElementById("otherAmountId").innerText=otherAmountCount;
      }
      //通过JSON提交审核结果 statusValue 状态 
	  function jsonSubmit(statusValue,frmName,btn){
		  createTip();
		  var turl = '<%=contextPath%>'+'/claim/application/ClaimManualAuditing/balanceAuditing.json?status='+statusValue;;
		  setTimeout("closeBalance()",3000);
		  makeNomalFormCall(turl,showResult,frmName);
		  disableLink(btn,false);
	  }
	  function closeBalance(){
		_hide();

	  }

      //确定审核
	  function confirmCustom(statusValue,frmName,btn) {
	  	  var flag = window.confirm('是否确认审核？');
  	      if(flag){
  	    	  jsonSubmit(statusValue,frmName,btn);
  	      }
	  }

      //审核成后跳转到审核首页
	  function showResult(json){
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

	  /*
	  * 联动覆盖原来值
	  * 注：1、sourceId 元素需要 存在 price,prevValue和value属性
	  *     2、targetId 元素需要 存在 value属性
	  *     3、如果存在根据targetId元素变化而变化的原始
	  *        需要配置nextDeal属性，其中配置需要响应的方法
	  * @param sourceId 事件元素对象ID
	  * @param targetId 需要变化元素ID
	  * @param userPrice 是否使用单价
	  * @param isCheckLimit 是否检测
	  */
	  function linkAgeOver(sourceId,targetId,userPrice,isCheckLimit){

		  var sourceEle = $(sourceId);
		  var targetEle = $(targetId);
		  if(isNaN(sourceEle.value)){
			  return;
		  }
		  var sourcePrice = sourceEle.price;
		  if(isNaN(sourcePrice)||!userPrice)
			  sourcePrice = 1;

		  var targetValue =(sourceEle.value*sourcePrice).toFixed(2);
		  var isOver = limitLinkAge(targetValue,sourceEle);
		  if(!isCheckLimit)
			  isOver = false;

		  if(isOver){
			  MyAlert('结算金额不能超过申请金额！');
			  sourceEle.value = sourceEle.prevValue;
			 
		  }else{
			  if(typeof(targetEle.text)!='undifined' && targetEle.text){
				  targetEle.text = targetValue;
			  }else{
				  if(typeof(targetEle.type)=='undifined' || targetEle.type!='text'){
				  	  targetEle.innerHTML = targetValue;
				  }
			  }

			  targetEle.value = targetValue;
		  }

		  var nextDealFunc = targetEle.nextDeal;
		  if(nextDealFunc)
			  eval(nextDealFunc);
	  }
	  
	  /*
	  * 联动使用变化值更新原来数值
	  * 注：1、sourceId 元素需要 存在 prevValue、price和value属性
	  *     2、sourceId 元素同时需要存在value属性
	  *     3、如果存在根据targetId元素变化而变化的原始
	  *        需要配置nextDeal属性，其中配置需要响应的方法
	  * @param sourceId 事件元素ID
	  * @param targetId 需要变化元素ID
	  * @param userPrice 是否使用单价
	  * @param isCheckLimit 是否检测  true :检测是否大于限制值  其他:不检测
	  */
	  function linkAge(sourceId,targetId,userPrice,isCheckLimit){

		  var sourceEle = $(sourceId);
		  var targetEle = $(targetId);
		  
		  var sourcePrevValue = sourceEle.prevValue;
		  var sourceCurrValue = sourceEle.value;
		  var diffValue = 0;

		  if(isNaN(sourcePrevValue)||isNaN(sourceCurrValue)){
			  return;
		  }
		  
		  if(!isNaN(sourcePrevValue) && !isNaN(sourceCurrValue)){
			  diffValue = sourceCurrValue-sourcePrevValue;
		  }

		  if(!targetEle.value || isNaN(targetEle.value))
			  targetEle.value = 0;

		  var elePrice = 1;
		  if(userPrice){
			  if(typeof(sourceEle.price)!='undifined' && sourceEle.price)
				  elePrice = sourceEle.price;
		  }
		  var targetValue = ((parseFloat(targetEle.value) + parseFloat(diffValue))*elePrice).toFixed(2);
		  var isOver = limitLinkAge(targetValue,sourceEle);
		  if(!isCheckLimit)
			  isOver = false;
		  if(isOver){
			  MyAlert('结算金额不能超过申请金额！');
			  sourceEle.value = sourceEle.prevValue;
	      }else{
			  if(typeof(targetEle.text)!='undifined' && targetEle.text){
				  targetEle.text = targetValue;
			  }else{
				  if(typeof(targetEle.type)=='undifined' || targetEle.type!='text'){
				      targetEle.innerHTML = targetValue;
				  }
			  }
			  
			  targetEle.value = targetValue;
			  sourceEle.prevValue = sourceCurrValue;
		  }
		  
		  var nextDealFunc = targetEle.nextDeal;
		  if(nextDealFunc)
			  eval(nextDealFunc);
	  }

	  /**
	  * 限制联动值的范围
	  * 规则： 
	  ×　　　　targetValue > eventObj.limitValue  返回 true  其他： 返回false
	  * @param targetValue 改变后的值
	  * @param eventObj 响应事件元素
	  * @return boolean true : 超过设定值   false :未超过设定值
	  */
	  function limitLinkAge(targetValue,eventObj){
		  var isOver = false;
		  var lv = eventObj.limitValue;
		  if(lv && lv!='undifined'){
			  if(parseFloat(targetValue+0)>parseFloat(lv+0))
				  isOver = true;
		  }
		  return isOver;
	  }

	  function openWindowDialog(targetUrl){
		  var height = 500;
		  var width = 800;
		  var iTop = (window.screen.availHeight-30-height)/2;  //获得窗口的垂直位置      
		  var iLeft = (window.screen.availWidth-10-width)/2;   //获得窗口的水平位置
		  var params = "width="+width+",height="+height+",top="+iTop+",left="+iLeft+",scrollbars=yes,resizable=yes,status=no";
		  window.open(targetUrl,null,params);
	  }
	  
	  function clearNoNum(obj){
			//先把非数字的都替换掉，除了数字和.
			obj.value = obj.value.replace(/[^\d.]/g,"");
			//必须保证第一个为数字而不是.
			obj.value = obj.value.replace(/^\./g,"");
			//保证只有出现一个.而没有多个.
			obj.value = obj.value.replace(/\.{2,}/g,".");
			//保证.只出现一次，而不能出现两次以上
			obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	  }

	  //修改金额处理
	  function isCheck(id,a,j,d){
		  if(a>accMulFloat(($('PARTCOUNT_APPLY'+j).value),d)){
			  MyAlert('结算金额不能超过申请金额!');
			  $(id).value=accMulFloat(($('PARTCOUNT'+j).value),d);
		  }else{
			  linkAge(id,'partAmountId',false);//自动算钱函数
		}
	  }

	  //屏蔽中文
	  function numberAutoCheck(){ 
			return event.keyCode>=48&&event.keyCode<=57||event.keyCode==46;
	  }

	//说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
	//调用：accMul(arg1,arg2)
	//返回值：arg1乘以arg2的精确结果
	function accMulFloat(arg1,arg2){
	  var m=0,s1=arg1.toString(),s2=arg2.toString();
	  try{m+=s1.split(".")[1].length}catch(e){}
	  try{m+=s2.split(".")[1].length}catch(e){}
	  return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
	}

	//配件三包判定按钮方法
	function threePackageSet(){
		var vin = document.getElementById("VIN").value;
		var inMileage = document.getElementById("IN_MILEAGE").value;
		var roNo = '<%=CommonUtils.checkNull(tawep.getRoNo())%>' ;
		var arr = document.getElementsByName('PART_CODE');
		var str = ''; 
		for(var i=0;i<arr.length;i++)
			str = str+arr[i].value+"," ;
		var codes = str.substr(0,str.length-1);
		if (vin==null||vin==''||vin=='null') {
			MyAlert("车辆VIN不能为空！");
		}else if (inMileage==null||inMileage==''||inMileage=='null') {
			MyAlert("进厂里程数不能为空！");
		}else{
			window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/threePackageSet.do?VIN='+vin+'&mile='+inMileage+'&codes='+codes+'&roNo='+roNo);
		}
	}
</script>
</body>
</html>
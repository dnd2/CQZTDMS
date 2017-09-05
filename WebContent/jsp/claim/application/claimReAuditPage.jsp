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
<%@page import="com.infodms.dms.po.TmVehiclePO"%>
<%@page import="com.infodms.dms.po.TcCodePO"%><html xmlns="http://www.w3.org/1999/xhtml">
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
		}
	</script>
</head>
<body>
<%
    TmVehiclePO veh=(TmVehiclePO)request.getAttribute("vehSel");
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
	if(authReason==null){
		authReason = new TtAsWrWrauthorizationPO();
	}
	int codeId = (Integer)request.getAttribute("codeId");
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
					-<%=CommonUtils.checkNull(tawep.getLineNo()) %>]</a>
					--%>&nbsp;
				</td>
				<td align="right" nowrap="true">工单开始时间:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(Utility.handleDate(tawep.getRoStartdate()))%>&nbsp;</td>
				<td align="right" nowrap="true">工单结束时间:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(Utility.handleDate(tawep.getRoEnddate())) %>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">VIN:</td>
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
				<td align="right" nowrap="true">生产日期</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(Utility.handleDate(veh.getProductDate())) %>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">车系:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getSeriesName()) %>&nbsp;</td>
				<td align="right" nowrap="true">车型:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getModelName()) %>&nbsp;</td>
				<td align="right" nowrap="true">配置:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getPackageName()) %>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">生产基地:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getYieldlyName()) %>&nbsp;</td>
				<td align="right" nowrap="true">行驶里程:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getInMileage()) %>&nbsp;</td>
				<td align="right" nowrap="true">索赔类型:</td>
				<td align="left" nowrap="true"><%=CommonUtils.checkNull(tawep.getClaimName())%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">客户姓名:</td>
				<td align="left" nowrap="true"><%=CommonUtils.getDataFromMap(customerMap,"CTM_NAME")%></td>
				<td align="right" nowrap="true">客户电话:</td>
				<td align="left" nowrap="true"><%=CommonUtils.getDataFromMap(customerMap,"MAIN_PHONE")%></td>
				<%--<td align="right" nowrap="true">客户手机:</td>
				<td align="left" nowrap="true"><%=CommonUtils.getDataFromMap(customerMap,"")%></td>--%>
				<td align="right" nowrap="true">车辆购车日期:</td>
				<td align="left" nowrap="true"><%=CommonUtils.printDate(tawep.getPurchasedDate())%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">DQV认证标识:</td>
				<td align="left" nowrap="true">
					<script>
						writeItemValue(<%=tawep.getIsDqv()%>);
					</script>
				</td>
				<td align="right" nowrap="true"></td>
				<td align="left" nowrap="true"></td>
				<%if(Constant.CLA_TYPE_07.toString().equals(tawep.getClaimType().toString())&&codeId==Constant.chana_wc){ %>
				<td align="right" nowrap="true">质损类型:</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
						document.write(getItemValue("<%=CommonUtils.checkNull(tawep.getDegradationType())%>"));
					</script>
				</td>
				<%} %>
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
		<table class="table_edit" id="amountTabId" style="display:none">
			<tr>
				<td align="right" nowrap="true">工时数量：</td>
				<td align="left" id="labourCountId" value="<%=CommonUtils.checkNull(tawep.getLabourHours())%>">
					<%=CommonUtils.checkNull(tawep.getLabourHours())%>&nbsp;
				</td>
				<td align="right" nowrap="true">工时金额：</td>
				<td align="left" id="labourAmountId" value="<%=CommonUtils.checkNull(tawep.getBalanceLabourAmount())%>" 
				prevValue="<%=CommonUtils.checkNull(tawep.getBalanceLabourAmount())%>" price="1"
				nextDeal="linkAge('labourAmountId','totalAmountId',false)">
					<%=CommonUtils.currencyFormat(tawep.getBalanceLabourAmount(),minFractionDigits,defaultValue)%>&nbsp;
				</td>
				<td align="right" nowrap="true">配件数量：</td>
				<td align="left" id="partCountId" value="<%=CommonUtils.checkNull(tawep.getPartsCount())%>">
					<%=CommonUtils.checkNull(tawep.getPartsCount())%>&nbsp;
				</td>
				<td align="right" nowrap="true">配件金额：</td>
				<td align="left" id="partAmountId" value="<%=CommonUtils.checkNull(tawep.getBalancePartAmount())%>" 
				prevValue="<%=CommonUtils.checkNull(tawep.getBalancePartAmount())%>" price="1"
				nextDeal="linkAge('partAmountId','totalAmountId',false)">
					<%=CommonUtils.currencyFormat(tawep.getBalancePartAmount(),minFractionDigits,defaultValue)%>&nbsp;
				</td>
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
				<td align="right" nowrap="true">保养金额：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getFreeMPrice(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">&nbsp;</td>
				<td align="left">&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">追加工时数量：</td>
				<td align="left" id="aLabourId" nextDeal="linkAgeOver('aLabourId','aLabourAmountId',true);" 
					price="<%=tawep.getLabourPrice()%>" value="0"> 
					<%=CommonUtils.checkNull(tawep.getAppendlabourNum())%>&nbsp;
				</td>
				<td align="right" nowrap="true">追加工时金额：</td>
				<td align="left" id="aLabourAmountId" nextDeal="linkAge('aLabourAmountId','totalAmountId',false);"
					value="0" prevValue="<%=tawep.getAppendlabourAmount()%>">
					<%=CommonUtils.currencyFormat(tawep.getAppendlabourAmount(),minFractionDigits,defaultValue)%>&nbsp;
				</td>
				<td align="right" nowrap="true">追加费用：</td>
				<td align="left"><%=CommonUtils.currencyFormat(tawep.getAppendAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
				<td align="right" nowrap="true">索赔总金额：</td>
				<td align="left" id="totalAmountId" value="<%=tawep.getBalanceAmount()%>">
					<%=CommonUtils.currencyFormat(tawep.getBalanceAmount(),minFractionDigits,defaultValue)%>&nbsp;
				</td>
			</tr>
		</table>
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
					&nbsp;&nbsp;&nbsp;&nbsp;
					<span style="BACKGROUND-COLOR: #ffa0a0;width=25px">&nbsp;&nbsp;&nbsp;&nbsp;</span>三包期外&nbsp;&nbsp;
					<span style="BACKGROUND-COLOR: #cceedd;width=25px">&nbsp;&nbsp;&nbsp;&nbsp;</span>三包期内
				</th>
			</tr>
		</table>
		<table class="table_list" name="all_group_" style="border-bottom: 1px solid #DAE0EE;" id="labourTable">
			<tr class='table_list_th'>
			    <th>操作</th>
				<th>索赔工时名称</th>
				
				<%if(codeId==Constant.chana_jc){ %>
					<th>顾客问题</th>
				<%} %>
				<th>工时结算数</th>
				<th>工时单价(元)</th>
				<th>工时金额(元)</th>
				<th>作业次数</th>
				<th>工时结算金额(元)</th>
				<th>授权批示</th>
				<%-- <th>审核意见</th> --%>
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
				     <td><%=CommonUtils.checkNull(mainLabourPO.getWrLabourname() )%>&nbsp;</td>
				     <%if(codeId==Constant.chana_jc){ %>
					 	<td><%=CommonUtils.checkNull(mainLabourPO.getTroubleType() )%>&nbsp;</td>
				     <%} %>
				     <td>
				     	<input type="text" class="short_no_border_txt" readonly="readonly"
				     	 name="LABOURBCOUNTTD" id="LABOURBCOUNTTD<%=i%>" value="<%=CommonUtils.checkNull(mainLabourPO.getBalanceQuantity()) %>"
				     	 prevValue="<%=CommonUtils.checkNull(mainLabourPO.getBalanceQuantity())%>"/>
				     	<input type="hidden" name="LABOURBCOUNT" id="LABOURBCOUNT<%=i%>" value="<%=CommonUtils.checkNull(mainLabourPO.getLabourCount()) %>"/>
				     	&nbsp;
				     </td>
				     <td><%=CommonUtils.currencyFormat(mainLabourPO.getLabourPrice(),minFractionDigits,defaultValue) %>&nbsp;</td>
				     <td><%=CommonUtils.currencyFormat(mainLabourPO.getLabourAmount(),minFractionDigits,defaultValue)%>&nbsp;</td>
				     <td><%="".equals(CommonUtils.checkNull(mainLabourPO.getLabourCount()))?
				    				 "0":CommonUtils.checkNull(mainLabourPO.getLabourCount())%>
				     </td>
				     <!-- 区分轿车和微车 -->
				     <%if(codeId==Constant.chana_jc){ %>
				     <!-- 轿车 -->
				     <td>
				          <input name="LOBOURAMOUNTTD" id="LOBOURAMOUNTTD<%=i%>" type="text" class="no_border_txt" 
			     		  value="<%=CommonUtils.checkNull(mainLabourPO.getBalanceAmount())%>" readonly="readonly" price="1"
			     		  prevValue="<%=CommonUtils.checkNull(mainLabourPO.getBalanceAmount())%>"/>
			     		  <input name="LOBOURAMOUNT" id="LOBOURAMOUNT<%=i%>" type="hidden"
			     		  value="<%=CommonUtils.checkNull(mainLabourPO.getLabourAmount())%>"/>
				     </td>
				     <!-- 轿车 -->
				     <%}else{ %>
				     <td>
				          <input name="LOBOURAMOUNTTD" id="LOBOURAMOUNTTD<%=i%>" type="text" class="short_txt" 
			     		  value="<%=CommonUtils.checkNull(mainLabourPO.getBalanceAmount())%>" price="1"
			     		  prevValue="<%=CommonUtils.checkNull(mainLabourPO.getBalanceAmount())%>"
			     		  nextDeal="linkAge('LOBOURAMOUNTTD<%=i%>','labourAmountId',false);"
			     		  onblur="isCheck(this.id,this.value,<%=i%>,<%=mainLabourPO.getLabourPrice() %>);"
			     		  />
			     		  <input name="LOBOURAMOUNT" id="LOBOURAMOUNT<%=i%>" type="hidden"
			     		  value="<%=CommonUtils.checkNull(mainLabourPO.getLabourAmount())%>"/>
				     </td>
				     <%} %>
				     <!-- 区分轿车和微车 -->
				     <td>
				     	  <select name="LOBOURSTATUS" id="LOBOURSTATUS<%=i%>" 
				     	  nextDeal="changeBalanceViewById('<%=i%>','LOBOURAMOUNT','LOBOURSTATUS<%=i%>');linkAge('LOBOURAMOUNTTD<%=i%>','labourAmountId',false);
				     	  changeBalanceViewById('<%=i%>','LABOURBCOUNT','LOBOURSTATUS<%=i%>');linkAge('LABOURBCOUNTTD<%=i%>','labourCountId',false);"  
				     	  onchange="changeBalanceView('<%=i%>','LOBOURAMOUNT',this);linkAgeLabour(this,'PARTSTATUS');linkAge('LOBOURAMOUNTTD<%=i%>','labourAmountId',false);
				     	  changeBalanceViewById('<%=i%>','LABOURBCOUNT','LOBOURSTATUS<%=i%>');linkAge('LABOURBCOUNTTD<%=i%>','labourCountId',false);">
				     	  		<%
				     	  			for(TcCodePO codePO:statusList){
				     	  				%>
				     	  					<option value="<%=codePO.getCodeId()%>" 
				     	  					<%if(codePO.getCodeId().equals(mainLabourPO.getIsAgree().toString())){%>selected="selected"<%} %>>
				     	  					<%=codePO.getCodeDesc()%>
				     	  				    </option>
				     	  				<%
				     	  			}
				     	  		%>
				     	  </select>
				     </td>
				    </tr>  
				
				 <% 
				    if(partLs!=null && partLs.size()>0){
				    	boolean isShow = false;//控制显示标题
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
							 	<td colspan="7"> 
								 	<table class="table_list">
						        	  <tr class="table_list_th">
									    <th>换下配件名称</th>
										<th>换上配件名称</th>
										<th>配件结算数量</th>
										<th>配件单价</th>
										<th>配件金额</th>
										<th>配件结算金额</th>
										<th>责任性质</th>
										<th>授权批示</th>
									 </tr>
						        <%isShow=true;} %>
						         <tr <% if(Constant.IS_NO_OVER_GUARANTEES.equals(mainPartPO.getIsGua())) {//未过三包期%>
										style="BACKGROUND-COLOR: #cceedd;" title="三包期内"
									<%  }else{//超过三包期%>
										style="BACKGROUND-COLOR: #ffa0a0;" title="三包期外"
									<%} %>>
							     <td><%=CommonUtils.checkNull(mainPartPO.getDownPartName()) %>&nbsp;
							         <input type="hidden" name="PART_ID" value="<%=mainPartPO.getPartId() %>"/>
							     </td>
							     <td><%=CommonUtils.checkNull(mainPartPO.getPartName()) %>&nbsp;</td>
							     <td>
							     	<input type="text" class="short_no_border_txt" readonly="readonly"
							     	name="PARTBCOUNTTD" id="PARTBCOUNTTD<%=j%>" value="<%=CommonUtils.checkNull(mainPartPO.getBalanceQuantity()) %>"
							     	prevValue="<%=CommonUtils.checkNull(mainPartPO.getBalanceQuantity()) %>"/>
							     	<input type="hidden" name="PARTBCOUNT" id="PARTBCOUNT<%=j%>" value="<%=CommonUtils.checkNull(mainPartPO.getQuantity()) %>"/>
							     	&nbsp;
							     </td>
							     <td><%=CommonUtils.currencyFormat(mainPartPO.getPrice(),minFractionDigits,defaultValue) %>&nbsp;</td>
							     <td><%=CommonUtils.currencyFormat(mainPartPO.getAmount(),minFractionDigits,defaultValue) %>&nbsp;</td>
							     <!-- 区分轿车和微车 -->
				     			 <%if(codeId==Constant.chana_jc){ %>
				     			 <!-- 轿车 -->
				     			 <td>
								      <input name="PARTAMOUNTTD" id="PARTAMOUNTTD<%=j%>" type="text" class="no_border_txt" 
							     		value="<%=CommonUtils.checkNull(mainPartPO.getBalanceAmount())%>" readonly="readonly" price="1"
			     		  				prevValue="<%=CommonUtils.checkNull(mainPartPO.getBalanceAmount())%>"/>
									  <input name="PARTAMOUNT" id="PARTAMOUNT<%=j%>" type="hidden"
							     		value="<%=CommonUtils.checkNull(mainPartPO.getAmount())%>"/>
							     </td>
				     			 <!-- 轿车 -->
				     			 <%}else{ %>
				     			 <td>
								      <input name="PARTAMOUNTTD" id="PARTAMOUNTTD<%=j%>" type="text" class="short_txt" 
							     		value="<%=CommonUtils.checkNull(mainPartPO.getBalanceAmount())%>" price="1"
			     		  				prevValue="<%=CommonUtils.checkNull(mainPartPO.getBalanceAmount())%>"
			     		  				nextDeal="linkAge('PARTAMOUNTTD<%=j%>','partAmountId',false);"
			     		  				onblur="isCheck1(this.id,this.value,<%=j%>,<%=mainPartPO.getPrice() %>);"
			     		  				/>
									  <input name="PARTAMOUNT" id="PARTAMOUNT<%=j%>" type="hidden"
							     		value="<%=CommonUtils.checkNull(mainPartPO.getAmount())%>"/>
							     </td>
				     			 <%} %>
				     			 <!-- 区分轿车和微车 -->
				     			 <td>
				     			  	<script type="">
				     			  	document.write(getItemValue(<%=CommonUtils.checkNull(mainPartPO.getResponsibilityType()) %>));
				     			  	</script>
				     			  </td>
							     <td>
							     	  <select name="PARTSTATUS" id="PARTSTATUS<%=j%>" parentselect="LOBOURSTATUS<%=i%>" 
							     	  nextDeal="changeBalanceViewById('<%=j%>','PARTAMOUNT','PARTSTATUS<%=j%>');linkAge('PARTAMOUNTTD<%=j%>','partAmountId',false);
							     	  changeBalanceViewById('<%=j%>','PARTBCOUNT','PARTSTATUS<%=j%>');linkAge('PARTBCOUNTTD<%=j%>','partCountId',false);"  
							     	  onchange="changeBalanceView('<%=j%>','PARTAMOUNT',this);linkAgePart(this,'PARTSTATUS');linkAge('PARTAMOUNTTD<%=j%>','partAmountId',false);
							     	  changeBalanceViewById('<%=j%>','PARTBCOUNT','PARTSTATUS<%=j%>');linkAge('PARTBCOUNTTD<%=j%>','partCountId',false);">
							     	  		<%
							     	  			for(TcCodePO codePO:statusList){
							     	  				%>
							     	  					<option value="<%=codePO.getCodeId()%>" <%if(codePO.getCodeId().equals(mainPartPO.getIsAgree().toString())){%>selected="selected"<%} %>>
							     	  						<%=codePO.getCodeDesc()%>
							     	  					</option>
							     	  				<%
							     	  			}
							     	  		%>
							     	  </select>
							     </td>
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
				<th style="display:none">授权批示</th>
			</tr>
			<%for (int i=0;i<otherLs.size();i++) { 
				TtAsWrNetitemExtPO netPO = otherLs.get(i);%>
			    <tr class="<%=(i%2)==0?"table_list_row1":"table_list_row2"%>">
			    	<td><%=i+1%><input type="hidden" name="OTHER_ID" value="<%=otherLs.get(i).getNetitemId()%>"/>&nbsp;</td>
					<td><%=CommonUtils.checkNull(netPO.getItemCode()) %>&nbsp;</td>
					<td><%=CommonUtils.checkNull(netPO.getItemDesc()) %>&nbsp;</td>
					<td><%=CommonUtils.currencyFormat(netPO.getAmount(),minFractionDigits,defaultValue) %>&nbsp;</td>
					<td><%=CommonUtils.checkNull(netPO.getRemark()) %>&nbsp;</td>
					<!-- 区分轿车和微车 -->
				     <%if(codeId==Constant.chana_jc){ %>
				     <!-- 轿车 -->
				     <td>
					    <input name="OTHERAMOUNTTD" id="OTHERAMOUNTTD<%=i%>" type="text" class="no_border_txt"  
			     		value="<%=CommonUtils.checkNull(netPO.getBalanceAmount())%>" readonly="readonly" price="1"
			     		prevValue="<%=CommonUtils.checkNull(netPO.getBalanceAmount())%>"/>	
			     		<input name="OTHERAMOUNT" id="OTHERAMOUNT<%=i%>" type="hidden" 
			     		value="<%=CommonUtils.checkNull(netPO.getAmount())%>"/>		
			     		<input type="hidden" name="OTHERSTATUS" value="<%=Constant.IF_TYPE_YES%>"/>
					 </td>
				     <!-- 轿车 -->
				     <%}else{ %>
				     <td>
					    <input name="OTHERAMOUNTTD" id="OTHERAMOUNTTD<%=i%>" type="text" class="short_txt" 
			     		value="<%=CommonUtils.checkNull(netPO.getBalanceAmount())%>" price="1"
			     		prevValue="<%=CommonUtils.checkNull(netPO.getBalanceAmount())%>"
			     		nextDeal="linkAge('OTHERAMOUNTTD<%=i%>','otherAmountId',false);"
			     		onblur="isCheck2(this.id,this.value,<%=i%>);"
			     		/>
			     		<input name="OTHERAMOUNT11" id="OTHERAMOUNT11<%=i%>" type="hidden" 
			     		value="<%=CommonUtils.checkNull(netPO.getBalanceAmount())%>"/>	
			     		<input name="OTHERAMOUNT" id="OTHERAMOUNT<%=i%>" type="hidden" 
			     		value="<%=CommonUtils.checkNull(netPO.getAmount())%>"/>	
			     		<input type="hidden" name="OTHERSTATUS" value="<%=Constant.IF_TYPE_YES%>"/>		
					 </td>
				     <%} %>
				     <!-- 区分轿车和微车 -->
					<td style="display:none">
				     	  <select name="OTHERSTATUS" onchange="changeBalanceView('<%=i%>','OTHERAMOUNT',this);linkAge('OTHERAMOUNTTD<%=i%>','otherAmountId',false);">
				     	  		<%
				     	  			for(TcCodePO codePO:statusList){
				     	  				%>
				     	  					<option value="<%=codePO.getCodeId()%>" 
				     	  						<%if((codePO.getCodeId()).equals(netPO.getIsAgree().toString())){%>selected="true"<%} %>>
				     	  						<%=codePO.getCodeDesc()%>
				     	  					</option>
				     	  				<%
				     	  			}
				     	  		%>
				     	  </select>
					</td> 
		   		</tr>
		   	<%}%>
		</table>
	</td></tr>
	
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
	
	</td></tr>
</c:if>
	<%} %>
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
	           追加工时：<input type="text" id="APPEND_LABOUR_ID" name="APPEND_LABOUR" class="middle_txt"
	            value="<%if(tawep.getAppendlabourNum()!=null){%><%=tawep.getAppendlabourNum()%><%}else{ %>0<%}%>" datatype="0,is_double,100" decimal="2" 
	            price="1" onkeyup="clearNoNum(this);linkAgeOver('APPEND_LABOUR_ID','aLabourId',false);"/>
	    &nbsp;&nbsp;工时单价: <%=CommonUtils.currencyFormat(tawep.getLabourPrice(),minFractionDigits,defaultValue)%> 
	    <input type="hidden" name="labourPrice" value="<%=tawep.getLabourPrice()%>"/>
	    <br/>
		需授权原因：<%=CommonUtils.checkNull(authReason.getApprovalReason())%>
		<br/>
		审核意见：<input name="CON_REMARK" id="CON_REMARK" datatype="1,is_null,300" type="text"  class="middle_txt"/>
	</div>
	<br/>
	<div align="center"> 
	    <input class="long_btn" type="button" value="质量信息跟踪卡"
		 onclick="openWindowDialog('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/qualitySerch.do?claimId=<%=tawep.getId()%>');"/>
		<input class="normal_btn" type="button" value="维修历史" name="historyBtn"
		 onclick="openWindowDialog('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN=<%=tawep.getVin()%>');"/>
		<input class="normal_btn" type="button" value="授权历史" name="historyBtn"
		 onclick="openWindowDialog('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/auditingHistory.do?VIN=<%=tawep.getVin()%>');"/>
		<input class="normal_btn" type="button" value="保养历史" name="historyBtn" 
		onclick="openWindowDialog('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/freeMaintainHistory.do?VIN=<%=tawep.getVin()%>');"/>
		<input class="normal_btn"	type="button" value="同意" name="bt_back2" onclick="confirmCustom('<%=Constant.CLAIM_APPLY_ORD_TYPE_04%>','fm',this);"/>
        <input class="normal_btn" type="button" value="退回" name="bt_back3" onclick="confirmCustom('<%=Constant.CLAIM_APPLY_ORD_TYPE_06%>','fm',this);"/>
        <input class="normal_btn" type="button" value="拒绝" name="bt_back" onclick="confirmCustom('<%=Constant.CLAIM_APPLY_ORD_TYPE_05%>','fm',this);"/>
        <input class="normal_btn" type="button" value="返回" onclick="parent.window._hide();"/>
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
		  var turl = '<%=contextPath%>'+'/claim/application/ClaimManualAuditing/reAuditing.json?status='+statusValue;;
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
		  	}else{
			  	MyAlert("请填写审核意见！");
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

	  //历史维修查询
	  function historyMaintains(){
		  location = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillTrackForword.do?vin=<%=CommonUtils.checkNull(tawep.getVin())%>';
	  }

	  var yesValue = '<%=Constant.IF_TYPE_YES%>';
	  var noValue = '<%=Constant.IF_TYPE_NO%>';

	  //根据授权状态改变修改结算金额
	  function changeBalanceView(index,id,bStatus){
		  var tdid = id+'TD'+index;
		  var td = document.getElementById(tdid);
		  var balanceInput = document.getElementById(id+index);
		  var balanceValue = balanceInput.value;
		  var showValue = 0;
		  var status = bStatus.options[bStatus.options.selectedIndex].value;
		  if(yesValue==status)
			  showValue=balanceValue;
		  td.value = showValue;
	  }

	  function changeBalanceViewById(index,id,bStatusId){
		  var bStatus = document.getElementById(bStatusId);
		  changeBalanceView(index,id,bStatus);
	  }
	  
	  /**
	  * 根据配件授权批示调整对应工时授权批示
	  * @param sourceEle 事件发起元素
	  * @param selectName 配件对应下拉框名称
	  **/
	  function linkAgePart(sourceEle,selectName){
		  var parentEle = document.getElementById(sourceEle.parentselect);

		  var sourceStatus = sourceEle.options[sourceEle.options.selectedIndex].value;

		  var isChange = false;
		  
		  if(yesValue==sourceStatus){
			  selectOneOption(parentEle.options,yesValue);
			  isChange = true;
		  }else if(isAllNo(selectName,sourceEle.parentselect)){
			  selectOneOption(parentEle.options,noValue);
			  isChange = true;
		  }	  

		  if(isChange){
			  if(parentEle.nextDeal)
				    eval(parentEle.nextDeal);
		  }
	  }

	  /**
	  * 根据工时授权批示调整对应配件授权批示
	  * @param sourceEle 事件发起元素
	  * @param selectName 配件对应下拉框名称
	  **/
	  function linkAgeLabour(sourceEle,selectName){
		  var sourceid = sourceEle.id;
		  var sourceStatus = sourceEle.options[sourceEle.options.selectedIndex].value;

		  var selectArray = document.getElementsByName(selectName);
		  for(var i=0;i<selectArray.length;i++){
			  var tempObj = selectArray[i];
			  var status = tempObj.parentselect;
			  if(sourceid==status){
			  		selectOneOption(tempObj.options,sourceStatus);
				    if(tempObj.nextDeal)
					    eval(tempObj.nextDeal);
			  }
		  }
	  }

	  /**
	  * 按指定的值optionValue选择下拉列表中的项
	  * @param optionValue 要选定的值
	  * @param optionArray 下拉框的options数组
	  */
	  function selectOneOption(optionArray,optionValue){
			for(var i = 0;i<optionArray.length;i++){
				if(optionArray[i].value==optionValue){
					optionArray[i].selected = true;
					break;
				}
			}
	  }

	  /**
	  * 判断配件是否都选择"否"
	  * @param selectName 配件对应下拉框名称
	  * @param partentId 所属工时授权批示下拉框ID
	  * @return true : 全选"否" false : 未全选否
	  **/
	  function isAllNo(selectName,partentId){
		  var result = true;
		  var selectArray = document.getElementsByName(selectName);
		  for(var i=0;i<selectArray.length;i++){
			  var tempObj = selectArray[i];
			  if(tempObj.parentselect==partentId){
				  var status = tempObj.options[tempObj.options.selectedIndex].value;
				  if(yesValue==status){
					  result = false;
					  break;
				  }
			  }
		  }
		  return result;
	  }

	  function openWindowDialog(targetUrl){
		  var height = 500;
		  var width = 800;
		  var iTop = (window.screen.availHeight-30-height)/2;  //获得窗口的垂直位置      
		  var iLeft = (window.screen.availWidth-10-width)/2;   //获得窗口的水平位置
		  var params = "width="+width+",height="+height+",top="+iTop+",left="+iLeft+",scrollbars=yes,resizable=yes,status=no";
		  window.open(targetUrl,null,params);
	  }

	  /*
	  * 联动覆盖原来值
	  * 注：1、sourceId 元素需要 存在 price和value属性
	  *     2、targetId 元素需要 存在 value属性
	  *     3、如果存在根据targetId元素变化而变化的原始
	  *        需要配置nextDeal属性，其中配置需要响应的方法
	  * @param sourceId 事件元素对象ID
	  * @param targetId 需要变化元素ID
	  * @param userPrice 是否使用单价
	  */
	  function linkAgeOver(sourceId,targetId,userPrice){

		  var sourceEle = $(sourceId);
		  var targetEle = $(targetId);
		  if(isNaN(sourceEle.value)){
			  return;
		  }
		  var sourcePrice = sourceEle.price;
		  if(isNaN(sourcePrice)||!userPrice)
			  sourcePrice = 1;

		  if(typeof(targetEle.text)!='undifined' && targetEle.text){
			  targetEle.text = (sourceEle.value*sourcePrice).toFixed(2);
		  }else{
			  if(typeof(targetEle.type)=='undifined' || targetEle.type!='text')
			  	  targetEle.innerHTML = (sourceEle.value*sourcePrice).toFixed(2);
		  }

		  targetEle.value = (sourceEle.value*sourcePrice).toFixed(2);
		  
		  var nextDealFunc = targetEle.nextDeal;
		  if(nextDealFunc)
			  eval(nextDealFunc);
	  }
		//格式化小数，只能输入数字和一个小数点
		function clearNoNum(obj)
		{
			//先把非数字的都替换掉，除了数字和.
			obj.value = obj.value.replace(/[^\d.]/g,"");
			//必须保证第一个为数字而不是.
			obj.value = obj.value.replace(/^\./g,"");
			//保证只有出现一个.而没有多个.
			obj.value = obj.value.replace(/\.{2,}/g,".");
			//保证.只出现一次，而不能出现两次以上
			obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
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
	  */
	  function linkAge(sourceId,targetId,userPrice){

		  var sourceEle = $(sourceId);
		  var targetEle = $(targetId);
		  
		  var sourcePrevValue = sourceEle.prevValue;
		  var sourceCurrValue = sourceEle.value;
		  var diffValue = 0;

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

		  if(typeof(targetEle.text)!='undifined' && targetEle.text){
			  targetEle.text = ((parseFloat(targetEle.value) + parseFloat(diffValue))*elePrice).toFixed(2);
		  }else{
			  if(typeof(targetEle.type)=='undifined' || targetEle.type!='text'){
			  		targetEle.innerHTML = ((parseFloat(targetEle.value) + parseFloat(diffValue))*elePrice).toFixed(2);
			  }
		  }
		  
		  targetEle.value = ((parseFloat(targetEle.value) + parseFloat(diffValue))*elePrice).toFixed(2)
		  sourceEle.prevValue = sourceCurrValue;

		  var nextDealFunc = targetEle.nextDeal;
		  if(nextDealFunc)
			  eval(nextDealFunc);
	  }



	//zhumingwei 2011-04-26   BEGION
		//用于自动计算金额计算
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

		


		//修改工时金额处理
		  function isCheck(id,a,j,d){
			  if(a>accMulFloat(($('LABOURBCOUNTTD'+j).value),d)){
				  MyAlert('结算金额不能超过申请金额!');
				  $(id).value=accMulFloat(($('LABOURBCOUNTTD'+j).value),d);
			  }else{
				  linkAge(id,'labourAmountId',false);//自动算钱函数
			}
		  }

		//修改配件金额处理
		  function isCheck1(id,a,j,d){
			  if(a>accMulFloat(($('PARTBCOUNTTD'+j).value),d)){
				  MyAlert('结算金额不能超过申请金额!');
				  $(id).value=accMulFloat(($('PARTBCOUNTTD'+j).value),d);
			  }else{
				  linkAge(id,'partAmountId',false);//自动算钱函数
			}
		  }

		//外出维修的单的金额也可以变化
		  function isCheck2(id,a,j){
			  if(Number(a)>Number($('OTHERAMOUNT11'+j).value)){
				  MyAlert('结算金额不能超过申请金额!');
				  $(id).value=$('OTHERAMOUNT11'+j).value;
			  }else{
				  linkAge(id,'otherAmountId',false);//自动算钱函数
			}
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


			//zhumingwei 2011-04-26   END
</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
	String today = (String)request.getAttribute("TODAY");//当天日期
	String startDate = (String)request.getAttribute("STARTDATE");//开始时间
	String endDate = (String)request.getAttribute("ENDDATE");//开始时间
	TmDealerPO dealerPO = (TmDealerPO)request.getAttribute("DEALERPO");//所属经销商信息
	TmDealerPO invoiceMaker = (TmDealerPO)request.getAttribute("INVOICEMAKER");//开票经销商信息
	Map<String,Object> feeMap = (Map<String,Object>)request.getAttribute("FEEMAP");//费用信息
	Map<String,Object> specialFeeMap = (Map<String,Object>)request.getAttribute("specialFeeMap");//特殊费用信息
	TcUserPO logonUser = (TcUserPO)request.getAttribute("USERVO");//登陆用户信息
	String order = SequenceManager.getSequence("BO");//工单号
	TmRegionPO regionPO = (TmRegionPO)request.getAttribute("REGION");//省份信息
	List<Map<String,Object>> balanceDetailList = (List<Map<String,Object>>)request.getAttribute("BALANCEDETAIL");//按车系统计
	String isReturn = (String)request.getAttribute("ISRETURN");
	
	TmDealerPO balanceMaker = (TmDealerPO)request.getAttribute("BALANCEEMAKER");//结算经销商信息
	List spcFeelist=(List)request.getAttribute("spcFeelist");
	String YIELDLY=(String)request.getAttribute("YIELDLY");
%>

<%@page import="com.infodms.dms.po.TmDealerPO"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.util.sequenceUitl.SequenceManager"%>
<%@page import="com.infodms.dms.po.TcUserPO"%>
<%@page import="com.infodms.dms.po.TmRegionPO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.List"%>

<%@page import="com.infodms.dms.actions.claim.application.DealerBalance"%><html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>索赔结算预览</title>
		<script type="text/javascript">
		    //格式化钱格式
			function formatMoney(dataStr){
				var str = formatCurrency(dataStr);
				document.write(str);
			}
		    
		    //返回
			function historyBack(){
				var hurl = "<%=contextPath%>/claim/application/DealerBalance/addDealerBalanceInit.do";
				location.href = hurl;
			}

			function balanceConfirm(){

				   if(submitForm('fm')){
					   MyConfirm("是否确定结算！",balance);
				    }
				
			}

		    //确认结算
		    function balance(){
			    createTip();
			    if(submitForm('fm')){
			        var turl = "<%=contextPath%>/claim/application/DealerBalance/balance.json";
			        makeNomalFormCall(turl,dealBalance,'fm','saveBtn');
			    }
		    }

		    function dealBalance(json){
			    var status = json.SUCCESS;
			    if(status=='DEALED'){
				    MyAlert("其他人员正进行结算！");
			    }
			    if(status=='NO'){
				    MyAlert("此经销商已被停止结算！");
			    }
			    historyBack();
		    }

		    var  resizeTimer = null;  

		    function resetResize(){
		    	window.onresize=divControl;
		    }   
		    
		    function divControl(){
		    	document.getElementById('detailDiv').style.width=document.body.clientWidth;
		    	window.onresize = null;
		    	if(resizeTimer) clearTimeout(resizeTimer);    
					resizeTimer = setTimeout("resetResize()",200); 
		    }

		    window.onresize=function(){
		    	divControl(); 
		    }; 

		  /*
		  * 联动使用变化值更新原来数值
		  * 注：1、sourceId 元素需要 存在 prevValue和value属性
		  *     2、sourceId 元素同时需要存在value属性
		  *     3、如果存在根据targetId元素变化而变化的原始
		  *        需要配置nextDeal属性，其中配置需要响应的方法
		  * @param sourceId 事件元素ID
		  * @param targetId 需要变化元素ID
		  */
		  function linkAge(sourceId,targetId){

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

			  if(typeof(targetEle.text)!='undifined' && targetEle.text){
				  targetEle.text = ((parseFloat(targetEle.value) + parseFloat(diffValue))).toFixed(2);
			  }else{
				  if(typeof(targetEle.type)=='undifined' || targetEle.type!='text'){
				  		targetEle.innerHTML = ((parseFloat(targetEle.value) + parseFloat(diffValue))).toFixed(2);
				  }
			  }
			  
			  targetEle.value = ((parseFloat(targetEle.value) + parseFloat(diffValue))).toFixed(2)
			  sourceEle.prevValue = sourceCurrValue;
		  }	    
		</script>
	</head>
<body><!--
		    window.onresize = function(){    
				if(resizeTimer) clearTimeout(resizeTimer);    
					resizeTimer = setTimeout("divControl()",200);    
		    }
	--><form method="post" name="fm" id="fm">
	<input type="hidden" name="conEndDay" id="conEndDay" value="${conEndDay }" />
	<table width="100%">
	    <tr><td>
			<div class="navigation">
		    	<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;经销商结算预览
		    </div>
	    </td></tr>
	    <% if("false".equals(isReturn)){ %>
	    	<tr><td>
	    	<script type="text/javascript">
					MyConfirm("存在超过 "+<%=DealerBalance.RETURN_LIMIT_MONTHS%>+" 个月未回运的配件,不能结算！",historyBack,[],historyBack,[]);
			</script>
			</td></tr>
	    <% //}else if(feeMap!=null && feeMap.size()>0 && balanceDetailList!=null && balanceDetailList.size()>0) {//存在需要结算的数据%>
	    <% }else if((spcFeelist!=null&&spcFeelist.size()>0) || (balanceDetailList!=null && balanceDetailList.size()>0)) {//存在需要结算的数据%>
		<tr><td>
		<table class="table_edit">
			<tr>
				<td align="right" nowrap="nowrap">结算单号：</td>
				<td align="left" nowrap="nowrap">
				    <%=order%>&nbsp;
					<input type="hidden" name="BALANACE_NO" value="<%=order%>"/>
				</td>
				<td align="right" nowrap="nowrap">制单人姓名：</td>
				<td align="left" nowrap="nowrap">
				<%=CommonUtils.checkNull(logonUser.getName())%>&nbsp;
				<input type="hidden" name="APPLY_NAME" value="<%=CommonUtils.checkNull(logonUser.getName())%>"/>
				<input type="hidden" name="APPLY_ID" value="<%=CommonUtils.checkNull(logonUser.getUserId())%>"/>
				</td>
				<td align="right" nowrap="nowrap">制单日期：</td>
				<td align="left" nowrap="nowrap">
				<%=today%>&nbsp;
				<input type="hidden" name="APPLY_DATE" value="<%=today%>"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">经销商代码：</td>
				<td align="left" nowrap="nowrap">
				<%=CommonUtils.checkNull(dealerPO.getDealerCode())%>&nbsp;
				<input type="hidden" name="DEALER_ID" value="<%=CommonUtils.checkNull(dealerPO.getDealerId())%>"/>
				<input type="hidden" name="DEALER_CODE" value="<%=CommonUtils.checkNull(dealerPO.getDealerCode())%>"/>
				</td>
				<td align="right" nowrap="nowrap">经销商名称：</td>
				<td align="left" nowrap="nowrap">
				<%=CommonUtils.checkNull(dealerPO.getDealerName())%>&nbsp;
				<input type="hidden" name="DEALER_NAME" value="<%=CommonUtils.checkNull(dealerPO.getDealerName())%>"/>
				</td>
				<td align="right" nowrap="nowrap"></td>
				<td align="left" nowrap="nowrap">
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">开票单位：</td>
				<td align="left" nowrap="nowrap">
				<%=invoiceMaker==null?"":invoiceMaker.getDealerName()%>&nbsp;
				<input type="hidden" name="INVOICE_MAKER" value="<%=invoiceMaker==null?"":invoiceMaker.getDealerName()%>"/>
				</td>
				
				<td align="right" nowrap="nowrap">结算单位：</td>
				<td align="left" nowrap="nowrap">
				<%=CommonUtils.checkNull(balanceMaker.getDealerName())%>&nbsp;
				<input type="hidden" name="BALANCE_NAME" value="<%=CommonUtils.checkNull(balanceMaker.getDealerName())%>"/>
				</td>
				<td align="right" nowrap="nowrap"></td>
				<td align="left" nowrap="nowrap">
				
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">维修时间起：</td>
				<td align="left" nowrap="nowrap">
				<%=startDate %>&nbsp;
				<input type="hidden" name="START_DATE" value="<%=startDate %>"/>
				</td>
				<td align="right" nowrap="nowrap">维修时间止：</td>
				<td align="left" nowrap="nowrap">
					<%=endDate %>&nbsp;
					<input type="hidden" name="END_DATE" value="<%=endDate %>"/>
				</td>
				<td align="right" nowrap="nowrap">生产商：</td>
				<td align="left" nowrap="nowrap">
				<script>
					writeItemValue(<%=YIELDLY%>);
				</script>
				<input type="hidden" name="YIELDLY" value="<%=YIELDLY %>"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">工时费(元)：</td>
				<td align="left" nowrap="nowrap">
				<script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(feeMap,"LABOUR_AMOUNT").toString().equals("")?"0":CommonUtils.getDataFromMap(feeMap,"LABOUR_AMOUNT")%>);</script>
				&nbsp;
				<%-- 工时费用 --%>
				<input type="hidden" name="LABOUR_AMOUNT" value="<%=CommonUtils.getDataFromMap(feeMap,"LABOUR_AMOUNT")%>"/>
				<%-- 追加工时费用 --%>
				<input type="hidden" name="APPENDLABOUR_AMOUNT" value="<%=CommonUtils.getDataFromMap(feeMap,"APPENDLABOUR_AMOUNT")%>"/>
				</td>
				<td align="right" nowrap="nowrap">材料费(元)：</td>
				<td align="left" nowrap="nowrap">
				<script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(feeMap,"PART_AMOUNT").toString().equals("")?"0":CommonUtils.getDataFromMap(feeMap,"PART_AMOUNT")%>);</script>&nbsp;
				<input type="hidden" name="PART_AMOUNT" value="<%=CommonUtils.getDataFromMap(feeMap,"PART_AMOUNT")%>"/>
				</td>
				<td align="right" nowrap="nowrap">救急费(元)：</td><%--原其他费用(现在只有服务活动和外出维修存在) 20100927 --%>
				<td align="left" nowrap="nowrap">
				<script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(feeMap,"NETITEM_AMOUNT").toString().equals("")?"0":CommonUtils.getDataFromMap(feeMap,"NETITEM_AMOUNT")%>);</script>&nbsp;
				<input type="hidden" name="OTHER_AMOUNT" value="<%=CommonUtils.getDataFromMap(feeMap,"NETITEM_AMOUNT")%>"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">保养费用(元)：</td>
				<td align="left" nowrap="nowrap">
				<script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(feeMap,"FREE_M_PRICE").toString().equals("")?"0":CommonUtils.getDataFromMap(feeMap,"FREE_M_PRICE")%>);</script>&nbsp;
				<%-- 保养总费用 --%>
				<input type="hidden" name="FREE_AMOUNT" value="<%=CommonUtils.getDataFromMap(feeMap,"FREE_M_PRICE")%>"/>
				<%-- 保养工时费用 --%>
				<input type="hidden" name="FREE_LABOUR_AMOUNT" value="<%=CommonUtils.getDataFromMap(feeMap,"FREE_LABOUR_AMOUNT")%>"/>
				<%-- 保养配件费用 --%>
				<input type="hidden" name="FREE_PART_AMOUNT" value="<%=CommonUtils.getDataFromMap(feeMap,"FREE_PART_AMOUNT")%>"/>
				</td>
				<td align="right" nowrap="nowrap">服务活动费用(元)：</td>
				<td align="left" nowrap="nowrap">
				<script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(feeMap,"SERVICE_TOTAL_AMOUNT").toString().equals("")?"0":CommonUtils.getDataFromMap(feeMap,"SERVICE_TOTAL_AMOUNT")%>);</script>&nbsp;
				<%--服务活动固定费用--%>
				<input type="hidden" name="SERVICE_AMOUNT" value="<%=CommonUtils.getDataFromMap(feeMap,"CAMPAIGN_FEE")%>"/>
				<%--服务活动总费用(服务活动固定费用+服务活动工时费用+服务活动配件费用+服务活动其他项目费用)--%>
				<input type="hidden" name="SERVICE_TOTAL_AMOUNT" value="<%=CommonUtils.getDataFromMap(feeMap,"SERVICE_TOTAL_AMOUNT")%>"/>
				<%--服务活动工时费用--%>
				<input type="hidden" name="SERVICE_LABOUR_AMOUNT" value="<%=CommonUtils.getDataFromMap(feeMap,"SERVICE_LABOUR_AMOUNT")%>"/>
				<%--服务活动配件费用--%>
				<input type="hidden" name="SERVICE_PART_AMOUNT" value="<%=CommonUtils.getDataFromMap(feeMap,"SERVICE_PART_AMOUNT")%>"/>
				<%--服务活动其他项目费用--%>
				<input type="hidden" name="SERVICE_OTHER_AMOUNT" value="<%=CommonUtils.getDataFromMap(feeMap,"SERVICE_NETITEM_AMOUNT")%>"/>
				</td>
				<td align="right" nowrap="nowrap">运费(元)：</td>
				<td align="left" nowrap="nowrap">
				<input type="text" name="RETURN_AMOUNT" id="RETURN_AMOUNT" value="" datatype="0,is_double,12"
				 onkeyup="linkAge('RETURN_AMOUNT','totalAmount');" prevValue="0"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">特殊费用(元)：</td><%--追加费用 20100927 --%>
				<td align="left" nowrap="nowrap">
				<script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(feeMap,"APPEND_AMOUNT").toString().equals("")?"0":CommonUtils.getDataFromMap(feeMap,"APPEND_AMOUNT")%>);</script>&nbsp;
				<input type="hidden" name="APPEND_AMOUNT" value="<%=CommonUtils.getDataFromMap(feeMap,"APPEND_AMOUNT")%>"/>
				</td>
				<td align="right" nowrap="nowrap">市场工单处理费用(元)：</td>
				<td align="left" nowrap="nowrap"><%=CommonUtils.getDataFromMap(specialFeeMap,"MARKETFEE")%>&nbsp;</td>
				<td align="right" nowrap="nowrap">特殊外出费用(元)：</td>
				<td align="left" nowrap="nowrap"><%=CommonUtils.getDataFromMap(specialFeeMap,"OUTFEE")%>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">省份：</td>
				<td align="left" nowrap="nowrap">
				<%=CommonUtils.checkNull(regionPO.getRegionName())%>&nbsp;
				<input type="hidden" name="PROVINCE" value="<%=CommonUtils.checkNull(regionPO.getRegionCode())%>"/>
				<input type="hidden" name="REMARK" value=""/>
				</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td align="right" nowrap="nowrap">费用合计(元)：</td>
				<td align="left" nowrap="nowrap">
				<input type="text" name="BALANCE_AMOUNT" id="totalAmount" value="<%=CommonUtils.getDataFromMap(feeMap,"BALANCE_AMOUNT").toString().equals("")?"0":CommonUtils.getDataFromMap(feeMap,"BALANCE_AMOUNT")%>"
				 class="short_no_border_txt" readonly="readonly"/>
				</td>
			</tr>
			<%-- 
			<tr>
				<td align="right" nowrap="nowrap">备注：</td>
				<td align="left" colspan="5" nowrap="nowrap">
					<textarea rows="3" cols="75" id="REMARK" name="REMARK" datatype="1,is_textarea,180"></textarea>
				</td>
			</tr>
			--%>
			<tr>
				<td align="right" nowrap="nowrap">索赔单数：</td>
				<td align="left" colspan="5" nowrap="nowrap">
					<%=CommonUtils.getDataFromMap(feeMap,"CLAIMCOUNT").toString().equals("")?"0":CommonUtils.getDataFromMap(feeMap,"CLAIMCOUNT")%>&nbsp;
					<input type="hidden" name="CLAIM_COUNT" value="<%=CommonUtils.getDataFromMap(feeMap,"CLAIMCOUNT").toString().equals("")?"0":CommonUtils.getDataFromMap(feeMap,"CLAIMCOUNT")%>"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">站长电话：</td>
				<td align="left" nowrap="nowrap">
					<input type="text" name="STATIONER_TEL" id="STATIONER_TEL" value="" datatype="0,is_phone,20"/>
				</td>
				<td align="right" nowrap="nowrap">索赔员电话：</td>
				<td align="left" colspan="3" nowrap="nowrap">
					<input type="text" name="CLAIMER_TEL" id="CLAIMER_TEL" value="" datatype="0,is_phone,20"/>
				</td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<input type="button" name="saveBtn" id="saveBtn" onclick="balanceConfirm();" value="保存"/>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" name="backBtn" onclick="historyBack();" value="返回"/>
				</td>
			</tr>
		</table>
		</td></tr>
		<tr><td>
			&nbsp;
		</td></tr>
		<tr><td>
				<% if(balanceDetailList!=null && balanceDetailList.size()>0) {%>
					<div id="detailDiv" style="overflow:scroll;width:100%">
					<table class="table_list">
						<tr class="table_list_th">
							<th style="position:relative">行号</th><%--0--%>
							<th style="position:relative">车系</th><%--1--%>
							<th style="position:relative">备注</th><%--17--%>
							<th>售前工时费用(元)</th><%--2--%>
							<th>售前配件费用(元)</th><%--3--%>
							<%--<th>售前其他费用(元)</th>4--%>
							<th>售后工时费用(元)</th><%--5--%>
							<th>售后配件费用(元)</th><%--6--%>
							<%--<th>售后其他费用(元)</th>7--%>
							<th>保养次数</th><%--9--%>
							<th>保养费用(元)</th><%--8--%>
							<th>保养工时费用(元)</th><%--81--%>
							<th>保养材料费用(元)</th><%--82--%>
							<th>服务活动次数</th><%--14--%>
							<%--<th>服务活动工时费用(元)</th><%--10--%>
							<%--<th>服务活动配件费用(元)</th>11--%>
							<%--<th>服务活动其他费用(元)</th>12--%>
							<th>服务活动费用(元)</th><%--13--%>
							<th>售前三包单数</th><%--15--%>
							<th>售后三包单数</th><%--16--%>
						</tr>
						<%
							int i=0;
							for(Map<String,Object> detailMap:balanceDetailList){
								i++;
						%>
							<tr class="<%=(i%2)==0?"table_list_row1":"table_list_row2"%>">
								<td style="position:relative"><%=i%></td><%--0--%>
								<%--1--%><td style="position:relative">
									<input type="hidden" name="SERIES_COCE" value="<%=CommonUtils.getDataFromMap(detailMap,"SERIES_CODE")%>"/>
									<%=CommonUtils.getDataFromMap(detailMap,"SERIES_NAME")%>
								</td>
					            <%--17--%><td style="position:relative">
									<input name="SERIESREMARK" id="SERIESREMARK<%=i%>" type="text" datatype="1,is_null,180"/>&nbsp;
								</td>
								<%--2--%><td><script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(detailMap,"BEFORE_LABOUR_AMOUNT")%>);</script>&nbsp;</td>
								<%--3--%><td><script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(detailMap,"BEFORE_PART_AMOUNT")%>);</script>&nbsp;</td>
								<%--4<td><script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(detailMap,"BEFORE_OTHER_AMOUNT")%>);</script>&nbsp;</td>--%>
								<%--5--%><td><script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(detailMap,"AFTER_LABOUR_AMOUNT")%>);</script>&nbsp;</td>
								<%--6--%><td><script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(detailMap,"AFTER_PART_AMOUNT")%>);</script>&nbsp;</td>
								<%--7<td><script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(detailMap,"AFTER_OTHER_AMOUNT")%>);</script>&nbsp;</td>--%>
								<%--9--%><td><%=CommonUtils.getDataFromMap(detailMap,"FREE_COUNT")%>&nbsp;</td>
								<%--8--%><td><script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(detailMap,"FREE_AMOUNT")%>);</script>&nbsp;</td>
								<%--81--%><td><script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(detailMap,"FREE_LABOUR_AMOUNT")%>);</script>&nbsp;</td>
								<%--82--%><td><script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(detailMap,"FREE_PART_AMOUNT")%>);</script>&nbsp;</td>
								<%--14--%><td><%=CommonUtils.getDataFromMap(detailMap,"SERVICE_COUNT")%>&nbsp;</td>
								<%--10<td><script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(detailMap,"SERVICE_LABOUR_AMOUNT")%>);</script>&nbsp;</td>--%>
								<%--11<td><script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(detailMap,"SERVICE_PART_AMOUNT")%>);</script>&nbsp;</td>--%>
								<%--12<td><script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(detailMap,"SERVICE_OTHER_AMOUNT")%>);</script>&nbsp;</td>--%>
								<%--13<td><script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(detailMap,"SERVICE_FIXED_AMOUNT")%>);</script>&nbsp;</td>--%>
								<%--13--%><td><script type="text/javascript">formatMoney(<%=CommonUtils.getDataFromMap(detailMap,"SERVICE_TOTAL_AMOUNT")%>);</script>&nbsp;</td>
								<%--15--%><td><%=CommonUtils.getDataFromMap(detailMap,"BEFORE_CLAIM_COUNT")%>&nbsp;</td>
								<%--16--%><td><%=CommonUtils.getDataFromMap(detailMap,"AFTER_CLAIM_COUNT")%>&nbsp;</td>
							</tr>
						<%} %>
					</table>
					&nbsp;</div>
					<script type="text/javascript">
						divControl();
					</script>
				    <%} %>
			</td></tr>
			</table>
			<%}else{ %>
				<script type="text/javascript">
					MyConfirm("没有需要结算的索赔单，请点击确认返回！",historyBack,[],historyBack,[]);
				</script>
			<%} %>
	</form>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<% String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	Map cmap = (Map)request.getAttribute("map");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>特殊外出费用结算室审核</title>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 特殊费用管理 &gt; 特殊外出费用审核</div>
 <form method="post" name = "fm" >
<input type="hidden" name="totalCount" id="totalCount" value="<c:out value="${map.DECLARE_SUM}"/>"/>
<input type="hidden" name="id" value="<c:out value="${map.ID}"/>"/>
<!-- <input type="text" name="feeType" value="<c:out value="${map.FEE_TYPE}"/>"/> -->
<input type="hidden" name="balanceId" value="<c:out value="${map.CLAIMBALANCE_ID}"/>"/>
<input type="hidden" id="c1" value="${map.PASS_FEE}" />
<input type="hidden" id="c2" value="${map.TRAFFIC_FEE}" />
<input type="hidden" id="c3" value="${map.QUARTER_FEE}" />
<input type="hidden" id="c4" value="${map.EAT_FEE}" />
<input type="hidden" id="c5" value="${map.PERSON_SUBSIDE}" />

<!-- 根据查询条件逐条审核 -->
<input type="hidden" name="totalCount" id="totalCount" value="<c:out value="${map.DECLARE_SUM}"/>"/>
<input type="hidden" name="dealerCode" value="${dealerCode }" />
<input type="hidden" name="beginTime" value="${beginTime }" />
<input type="hidden" name="endTime" value="${endTime }" />
<input type="hidden" name="feeNo" value="${feeNo }" />
<input type="hidden" name="yieldly" value="${yieldly }" />
<input type="hidden" name="feeType" value="${map.FEE_TYPE}" />
<input type="hidden" name="count" value="${count }" />
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <tr>
          <th colspan="6"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 基本信息</th>
          </tr>
		  <tr>
		    <td align="right" nowrap="nowrap" >单据号码：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.FEE_NO}"/>
		    </td>
		    <td align="right" nowrap="nowrap" >经销商代码：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.DEALER_CODE}"/>
		    </td>
		    <td align="right" nowrap="nowrap" >经销商名称：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.DEALER_SHORTNAME}"/>
		    </td>
	      </tr>
		  <tr>
		    <td align="right" nowrap="nowrap" >制单日期：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.CREATE_DATE}"/>
		    </td>
		    <td align="right" nowrap="nowrap" >结算厂家：</td>
		    <td class="table_info_3col_input">
		    	<script type='text/javascript'>
					var name=getItemValue('<c:out value="${map.YIELD}"/>');
					document.write(name) ;
				</script> 
		    </td>
		    <td align="right" nowrap="nowrap">外出时间：</td>
		    <td class="table_info_3col_input">
<c:out value="${map.STARTDATE}"/>&nbsp;至&nbsp;<c:out value="${map.ENDDATE}"/>
			</td>
	      </tr>
		  <tr>
		    <td align="right" nowrap="nowrap" >目的地：</td>
            <td class="table_info_3col_input">
            	<c:out value="${map.PURPOSE_ADDRESS}"/>
            </td>
		    <td align="right">外出人员数量：</td>
		    <td class="table_info_3col_input">
				<c:out value="${map.PERSON_NUM}"/>
		    </td>
		    <td align="right" nowrap="nowrap" title="<%=cmap.get("PERSON_NAME")==null?"":cmap.get("PERSON_NAME")%>">外出人员姓名：</td>
		    <td class="table_info_3col_input" title="<%=cmap.get("PERSON_NAME")==null?"":cmap.get("PERSON_NAME")%>">
      		<%
      			if(cmap.get("PERSON_NAME")!=null){
      				if(String.valueOf(cmap.get("PERSON_NAME")).length()<=8){
      		%>
      			<%=cmap.get("PERSON_NAME")%>
      		<%
      				}
      		%>
      		<%
      				if(String.valueOf(cmap.get("PERSON_NAME")).length()>10){
      					String d = String.valueOf(cmap.get("PERSON_NAME"));
      					d = d.substring(0,7);
      		%>
      			<%=d%>...
      		<%
      				}
      			}
      		%>
      		</td>
	      </tr>			 
	      <tr>
		    <td align="right" nowrap="nowrap" >总里程(公里)：</td>
            <td class="table_info_3col_input">
				<c:out value="${map.SINGLE_MILEAGE}"/>
            </td>
		    <td align="right">过路过桥费(元)：</td>
		    <td>
		    	<input type="text" name="PASS_FEE" id="PASS_FEE" value="<c:out value="${map.PASS_FEE}"/>" datatype="0,isMoney,30" blurback="true" class="middle_txt"/>
		    </td>
		    <td align="right">交通补助(元)：</td>
		    <td>
				<input type="text" name="TRAFFIC_FEE" id="TRAFFIC_FEE" value="<c:out value="${map.TRAFFIC_FEE}"/>" datatype="0,isMoney,30" blurback="true" class="middle_txt"/>
			</td>
	      </tr>	     
	      <tr>
		    <td align="right" nowrap="nowrap" >住宿费(元)：</td>
            <td nowrap="nowrap">
				<input type="text" name="QUARTER_FEE" id="QUARTER_FEE" value="<c:out value="${map.QUARTER_FEE}"/>" datatype="0,isMoney,30" blurback="true" class="middle_txt"/>
            </td>
		    <td align="right">餐补费(元)：</td>
		    <td>
		    	<input type="text" name="EAT_FEE" id="EAT_FEE" value="<c:out value="${map.EAT_FEE}"/>" datatype="0,isMoney,30" blurback="true" class="middle_txt"/>
		    </td>
		    <td align="right">人员补助(元)：</td>
		    <td>
				<input type="text" name="PERSON_SUBSIDE" value="<c:out value="${map.PERSON_SUBSIDE}"/>" id="PERSON_SUBSIDE" datatype="0,isMoney,30" blurback="true" class="middle_txt"/>
			</td>
	      </tr>	  
	      <tr>
	      <tr>
		    <td align="right" nowrap="nowrap" >总申报费用(元)：</td>
            <td class="table_info_3col_input" id="to">
		    	<script type="text/javascript">
		    		document.write(amountFormat(<c:out value="${map.DECLARE_SUM1}"/>));
		    	</script>元
            </td>
		    <td align="right" nowrap="nowrap" >费用渠道：</td>
		    <td class="table_info_3col_input">
		    	<script type='text/javascript'>
					var name=getItemValue('<c:out value="${map.FEE_CHANNEL}"/>');
					document.write(name) ;
				</script>
		    </td>
		    <c:if test="${map.FEE_CHANNEL==11851002}">
		    <td align="right">三包内用户：</td>
		    <td>
				${map.FREE_CHARGE}
			</td>
			</c:if>
	      </tr>	 
          <tr>
            <td class="table_info_3col_label_6Letter">备注：</td>
			<td colspan="5" align="left" valign="top" nowrap="nowrap" class="table_info_input_all">
				<c:out value="${map.APPLY_CONTENT}"/>
      		</td>
          </tr>
     </table>   

<!-- 展示附件 开始-->
  <table class="table_info" border="0" id="file">
    <tr colspan="8">
        <th>
		<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
		&nbsp;附件列表：
		</th>
		<th><span align="left"></span>
		</th>
	</tr>
	<tr>
		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp"/></td>
	</tr>
	<%for(int i=0;i<fileList.size();i++) { %>
	  <script type="text/javascript">
	  showUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>');
	  </script>
	<%}%>
  </table>
<!-- 展示附件 结束-->
<table class="table_list" style="border-bottom:1px solid #DAE0EE">
   		<tr>
			<th colspan="11" align="left">
				<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />明细信息
			</th>
		</tr>
        <tr bgcolor="F3F4F8">
            <th>序号</th>
            <th>工单单号</th>
            <th>车系</th>
            <th>车型</th>
            <th>VIN</th>
            <th>发动机号</th>
            <th>生产日期</th>
            <th>里程</th>
    	</tr>
        <c:forEach items="${claim}" var="claim" varStatus="st">
       		<tr class="table_list_row2">
            	<td>${st.index+1}</td>
            	<td>
            		<a href="#" onclick="showDetail(<c:out value="${claim.CLAIM_ID}"/>)"><c:out value="${claim.CLAIM_NO}"/></a>
            	</td>
           	 	<td><c:out value="${claim.SERIES}"/></td>
           	 	<td><c:out value="${claim.MODEL}"/></td>
           	 	<td><c:out value="${claim.VIN}"/></td>
           	 	<td><c:out value="${claim.ENGINE_NO}"/></td>
           	 	<td><fmt:formatDate value="${claim.PRODUCT_DATE}" pattern="yyyy-MM-dd"/></td>
           	 	<td><c:out value="${claim.MILEAGE}"/></td>
        	</tr>
       	</c:forEach>
   </table>    
   <table class="table_list" style="border-bottom:1px solid #DAE0EE">
   		<tr>
			<th colspan="6" align="left"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 审批明细</th>
		
        <tr  bgcolor="F3F4F8">
            <th>审批时间</th>
            <th>审批人员</th>
            <th>人员部门</th>
            <th>审批状态</th>
            <th>审批意见</th>
    	</tr>
        <c:forEach items="${list}" var="al">
       		<tr class="table_list_row2">
            	<td><c:out value="${al.AUDITING_DATE}"/></td>
            	<td><c:out value="${al.AUDITING_PERSON}"/></td>
            	<td><c:out value="${al.ORG_NAME}"/></td>
            	<td>
	            	<script type='text/javascript'>
	     			  	writeItemValue(<c:out value="${al.STATUS}"/>);
					</script>
            	</td>
           	 	<td><c:out value="${al.AUDITING_OPINION}"/></td>
        	</tr>
       	</c:forEach>
   </table> 
   <br>   
   <table class="table_query">
   		<tr>
			<th colspan="6" align="left"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 审批</th>
		</tr>
		<tr >
            <td align="right">审批意见：</td>
            <td colspan="5">
              <textarea name="remark" id="remark" rows="5" cols="80" ></textarea>
            </td> 
        </tr>
   </table>    
    <table class="table_list">
       <tr > 
         <th height="12" align=center>
           <div id="a1">
	           <input type="button" onclick="audit(1)" id="btn1" class="long_btn" value="同意并跳转下一条"/>
	           <input type="button" onclick="audit(2)" id="btn2" class="normal_btn" value="拒绝" /> 
	           <input type="button" onclick="pass();" class="normal_btn" value="跳过" /> 
	           <input type="button" onclick="javascript:window.location.href='<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/specialExaminesFor.do'" class="normal_btn" value="返回" /> 
           </div>
       </tr>
     </table> 
</form>
<script type="text/javascript">
	
	function audit(val)
	{	
		var pass = Number(formatNumber($('c1').value)) ;
		var traffic = Number(formatNumber($('c2').value)) ;
		var quarter = Number(formatNumber($('c3').value)) ;
		var eat = Number(formatNumber($('c4').value)) ;
		var person = Number(formatNumber($('c5').value)) ;
		
		var pass2 = Number(formatNumber($('PASS_FEE').value)) ;
		var traffic2 = Number(formatNumber($('TRAFFIC_FEE').value)) ;
		var quarter2 = Number(formatNumber($('QUARTER_FEE').value)) ;
		var eat2 = Number(formatNumber($('EAT_FEE').value)) ;
		var person2 = Number(formatNumber($('PERSON_SUBSIDE').value)) ;
		if(pass<pass2){
			MyAlert('过路过桥费大于申请金额');
			return ;
		}
		if(traffic<traffic2){
			MyAlert('交通补助大于申请金额');
			return ;
		}
		if(quarter<quarter2){
			MyAlert('住宿费大于申请金额');
			return ;
		}
		if(eat<eat2){
			MyAlert('餐补费大于申请金额');
			return ;
		}
		if(person<person2){
			MyAlert('人员补助大于申请金额');
			return ;
		}
		if(val==2){
			if($('remark').value.trim()==''){
				MyAlert('拒绝原因必须填写！');
				return ;
			}
		}
		MyConfirm("是否确认审核同意并跳转下条！",addSpecia, [val]);
	}
	
	function addSpecia(val)
	{
		$('btn1').disabled = true ;
		$('btn2').disabled = true ;
		makeNomalFormCall("<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/auditSpeciaExpenses01.json?auditFlag="+val,addSpeciaBack,'fm','queryBtn'); 
	}
	
	function addSpeciaBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/auditSpecialExpensesInfoByOneJC.do";
			//fm.action = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/auditSpecialExpensesInfoByOne.do";
			fm.submit();
		}
		else
		{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	
	function blurBack()
	{
		var val1 = Number(formatNumber(document.getElementById("PASS_FEE").value));
		var val2 = Number(formatNumber(document.getElementById("TRAFFIC_FEE").value));
		var val3 = Number(formatNumber(document.getElementById("QUARTER_FEE").value));
		var val4 = Number(formatNumber(document.getElementById("EAT_FEE").value));
		var val5 = Number(formatNumber(document.getElementById("PERSON_SUBSIDE").value));
		document.getElementById("totalCount").value = val1 + val2 + val3 + val4 + val5;
		document.getElementById("to").innerHTML = amountFormat(val1 + val2 + val3 + val4 + val5) + "元";
	}
	
	function formatNumber(value)
	{
		if(value)
		{ 
			return value;
		}
		else return 0;
	}
	
	function openWindowDialog(val)
	{
		var height = 500;
		var width = 800;
		var iTop = (window.screen.availHeight-30-height)/2;  //获得窗口的垂直位置      
		var iLeft = (window.screen.availWidth-10-width)/2;   //获得窗口的水平位置
		var params = "width="+width+",height="+height+",top="+iTop+",left="+iLeft+",scrollbars=yes,resizable=yes,status=no";
		window.open("<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?ID="+val,null,params);
	}
	function showDetail(val){
		var url='<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail.do?type=000&ID='+val ;
		OpenHtmlWindow(url,1000,600);
	}
	//跳过审核下一条
	function pass(){
		$('count').value = parseInt($('count').value) + 1;
		fm.action = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/auditSpecialExpensesInfoByOneJC.do";
		//fm.action = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/auditSpecialExpensesInfoByOne.do";
		fm.submit();
	}
</script>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<% String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>市场工单费用审核</title>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 特殊费用审核 &gt; 市场工单费用审核(事业部)</div>
 <form method="post" name="fm" id="fm" >
<input type="hidden" name="id" value="<c:out value="${map.ID}"/>" />
<input type="hidden" name="balanceId" value="<c:out value="${map.CLAIMBALANCE_ID}"/>"/>
<input type="hidden" id="apply_money" value="${map.DECLARE_SUM1}"/>
<input type=hidden id='checkFeeType' name='checkFeeType' value="${map.FEE_TYPE}"/>
<input type=hidden id='checkFeeChannel' name='checkFeeChannel' value="${map.FEE_CHANNEL}"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <tr>
          <th colspan="6"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 基本信息</th>
          </tr>
		  <tr>
		    <td class="table_info_3col_label_6Letter">单据号码：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.FEE_NO}"/>
		    </td>
		    <td class="table_info_3col_label_6Letter">经销商代码：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.DEALER_CODE}"/>
		    </td>
		    <td class="table_info_3col_label_6Letter">经销商名称：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.DEALER_SHORTNAME}"/>
		    </td>
	      </tr>
		  <tr>
		    <td class="table_info_3col_label_6Letter">制单日期：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.CREATE_DATE}"/>
		    </td>
		    <td class="table_info_3col_label_6Letter">结算厂家：</td>
		    <td class="table_info_3col_input">
		    	<script type='text/javascript'>
					var name=getItemValue('<c:out value="${map.YIELD}"/>');
					document.write(name) ;
				</script> 
		    </td>
		    <td class="table_info_3col_label_6Letter">总金额(元)：</td>
		    <td class="table_info_3col_input">
				<script type='text/javascript'>
					var name=amountFormat('<c:out value="${map.DECLARE_SUM1}"/>');
					document.write(name) ;
				</script> 
				<input type="hidden" name="audit_money" value="${map.DECLARE_SUM1}" id="audit_money"/>
			</td>
	      </tr>
		  <tr>
		    <td class="table_info_3col_label_6Letter">VIN：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.VIN}"/>
		    </td>
		    <td class="table_info_3col_label_6Letter">车型：</td>
		    <td class="table_info_3col_input">
				<c:out value="${map.V_MODEL}"/>
		    </td>
		    <td class="table_info_3col_label_6Letter" >费用类型：</td>
		    <td class="table_info_3col_input" >
				<!-- <input type="text" name="audit_money" value="${map.DECLARE_SUM1}" id="audit_money"/> -->
				<script type='text/javascript'>
					var name=getItemValue('<c:out value="${map.FEE_TYPE}"/>');
					document.write(name);
				</script>
			</td>
	      </tr>
		  <tr>
		    <td class="table_info_3col_label_6Letter">联系人：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.LINKMAN}"/>
		    </td>
            <td class="table_info_3col_label_6Letter">联系人电话：</td>
            <td class="table_info_3col_input">
				<c:out value="${map.LINKMAN_TEL}"/>
            </td>

			<td class="table_info_3col_label_6Letter" >费用渠道：</td>
         	<td class="table_info_3col_input" >
         		<script type='text/javascript'>
					var name=getItemValue('<c:out value="${map.FEE_CHANNEL }"/>');
					document.write(name);
				</script></td>
         </tr>
         <tr>
         	            <td class="table_info_3col_label_6Letter" >活动项目：</td>
		    <td class="table_info_3col_input" >
				<!-- <input type="text" name="audit_money" value="${map.DECLARE_SUM1}" id="audit_money"/> -->
				<c:set value="<%=Constant.FEE_TYPE1_01 %>" var="channel1"></c:set>
				<c:set value="<%=Constant.FEE_TYPE1_02 %>" var="channel2"></c:set>
				<c:if test="${map.FEE_CHANNEL==13641001}">
			    	<script type='text/javascript'>
						var name=getItemValue('<c:out value="${map.ACTIVITY_PROJECT}"/>');
						document.write(name);
					</script>
		    </c:if>
		    <c:if test="${map.FEE_CHANNEL==13641002}">
		    	${map.ACTIVITY_NAME }
		    </c:if>
			</td>
         	<td class="table_info_3col_label_6Letter" >&nbsp;</td>
         	<td class="table_info_3col_label_6Letter" >&nbsp;</td>
         	<td class="table_info_3col_label_6Letter" >&nbsp;</td>
         	<td class="table_info_3col_label_6Letter" >&nbsp;</td>
         	
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
   <table class="table_list" style="border-bottom:1px solid #DAE0EE">
   		<tr>
			<th colspan="6" align="left"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 审批明细</th>
		</tr>
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
<!-- 展示附件 结束-->
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
	           <input type="button" onclick="audit(1)" id="btn1" class="normal_btn" style="width=8%" value="通过"/>
	           <input type="button" onclick="audit(2)" id="btn2" class="normal_btn" value="拒绝" /> 
	           <input type="button" onclick="history.go(-1)" class="normal_btn" value="返回" /> 
           </div>
       </tr>
     </table>    
</form>
<script type="text/javascript">
	$('aaaa').value = '<%=request.getParameter("flag")%>' ;
	if($('aaaa').value!='null'){
		$('a2').style.display = 'block' ;
		$('a1').style.display = 'none' ;
	}
	
	function audit(val)
	{   
		var apply_m = $('apply_money').value ;
		var audit_m = $('audit_money').value ;
		if(audit_m*1>apply_m*1){
			MyAlert('审核同意金额不能大于申请金额！');
			return ;
		}
		if(val==2){
			if($('remark').value.trim()==''){
				MyAlert('拒绝原因必须填写！');
				return ;
			}
		}
		MyConfirm("确认提交！",addSpecia, [val]);
	}
	
	function addSpecia(val)
	{
		$('btn1').disabled = true ;
		$('btn2').disabled = true ;
		makeNomalFormCall("<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/areaAuditSpeciaExpensesWC.json?auditFlag="+val,addSpeciaBack,'fm','queryBtn'); 
	}
	
	function addSpeciaBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/areaAuditQuerySpeciaExpensesWC.do";
			fm.submit();
		}
		else
		{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	
	
</script>
</body>
</html>

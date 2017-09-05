<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<html xmlns="http://www.w3.org/1999/xhtml">
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
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 特殊费用审核 &gt; 市场工单费用审核(轿车)</div>
 <form method="post" name="fm" id="fm" >
<input type="hidden" name="fid" value="${fid}" />
<input type="hidden" name="aid" value="${aid}" />
<input type="hidden" name="type" value="${type}" />
<input type="hidden" name="o_status" value="${o_status}" />
<input type="hidden" name="feeType" value="${map.FEE_TYPE}" />
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E" class="table_edit">
   		<tr>
          <th colspan="10"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 基本信息</th>
        </tr>
		  <tr>
		    <td  width="10%" nowrap="true">申报单位代码：</td>
		    <td width="15%" nowrap="true">
		    	<c:out value="${map.DEALER_CODE}"/>
		    </td>
		    <td width="10%" nowrap="true">申报单位名称：</td>
		    <td width="15%" nowrap="true">
		    	<c:out value="${map.DEALER_SHORTNAME}"/>
		    </td>
   		    <td width="10%" nowrap="true">结算费用类型：</td>
		    <td width="15%" nowrap="true">
		    	<script type='text/javascript'>
					var name=getItemValue('<c:out value="${map.BALANCE_FEE_TYPE}"/>');
					document.write(name);
				</script>
		    </td>
			<td  width="10%" nowrap="true">制单日期：</td>
		    <td width="15%" nowrap="true">
		    	<c:out value="${map.CREATE_DATE}"/>点
		    </td>
	      </tr>
		  <tr>
		    <td>结算厂家：</td>
		    <td>
		    	<c:out value="${Area}"/>
		    </td>
		    <td>结算金额(元)：</td>
		    <td>
    			<input type="text" value="<c:out value="${map.DECLARE_SUM}"/>" name="declareSum" id="declareSum" datatype="0,isMoney,30" class="middle_txt"/>	
		    </td>
		    <td >申请金额(元)：</td>
		    <td>
		    	${map.DECLARE_SUM1}
		    	<input type="hidden" value="<c:out value="${map.DECLARE_SUM1}"/>" name="declareSum1" id="declareSum1" datatype="0,isMoney,30" class="middle_txt"/>
		    </td>
		    <td >VIN：</td>
		    <td>
		    	<c:if test="${code.codeId==80081001}">
			    	<input disabled="disabled" type="text" name="vin" id="vin" class="middle_txt" blurback="true" datatype="0,is_vin,17" maxlength="17" value="${map.VIN}" />
			    </c:if>
			    <c:if test="${code.codeId==80081002}">
			    	<input  disabled="disabled" type="text" name="vin" id="vin" class="middle_txt" blurback="true" datatype="1,is_vin,17" maxlength="17" value="${map.VIN}" />
			    </c:if>
		    </td>
	      </tr>
		  <tr>
		    
		    <td>车型：</td>
		    <td>
		    	<input  disabled="disabled" type="text" name="model" id="model" class="middle_txt" datatype="1,is_null,40"  value="<c:out value="${map.V_MODEL}"/>" />
		    </td>
		    <td>费用类型：</td>
		    <td>
		        <script type='text/javascript'>
				       var activityType=getItemValue('${map.FEE_TYPE}');
				       document.write(activityType) ;
				     </script>
		    </td>
		    <td >联系人：</td>
		    <td>
		    	<input disabled="disabled" type="text" name="linkman" id="linkman" class="middle_txt" datatype="0,is_null,20"  value="<c:out value="${map.LINKMAN}"/>" />
		    </td>
		    <td >联系电话：</td>
		    <td>
		    	<input disabled="disabled" type="text" name="tel" id="tel" class="middle_txt" datatype="0,is_null,20"  value="<c:out value="${map.LINKMAN_TEL}"/>" />
		    </td>
	      </tr>
	      <tr>
		    <td >主因件编码：</td>
            <td >
				<c:out value="${map.PART_CODE}"/>
            </td>
		   
	       	 <td >主因件名称：</td>
            <td >
				<c:out value="${map.PART_NAME}"/>
            </td>
            <td >索赔单号：</td>
            <td >
            	<c:out value="${map.CLAIM_NO}"/>
            </td>
	        <td nowrap="true">制造商名称：</td>
            <td >
            	<c:out value="${map.SUPPLIER_NAME}"/>
            </td>
	      </tr>		
	       <tr>
             <td > 备注：</td>
            <td colspan="9" >
            	<textarea rows="3" cols="120" readonly="readonly">${map.APPLY_CONTENT}</textarea>
            </td> 
          </tr>
          <%-- <tr>
            <td align="right">各级审核意见 ：</td>
            <td>
              <c:if test="${map.AUDITING_OPINION != null}">服务经理意见 ： ${map.AUDITING_OPINION}  </c:if><!-- 服务经理审核意见 -->
              <c:if test="${map.DIRECTOR_AUDITING_OPINION != null}">车厂意见 ： ${map.DIRECTOR_AUDITING_OPINION}  </c:if>
              <c:if test="${map.SECTION_AUDITING_OPINION != null}">部门总监意见 ： ${map.SECTION_AUDITING_OPINION}  </c:if>
              <c:if test="${map.MANEGER_AUDITING_OPINION != null}">销售公司意见 ： ${map.MANEGER_AUDITING_OPINION}  </c:if>
              <c:if test="${map.OFFICE_AUDITING_OPINIONN != null}">集体公司意见 ： ${map.OFFICE_AUDITING_OPINIONN}  </c:if>
            </td>
          </tr>	  --%>
        </table>        
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	 		<tr>
		 		<th colspan="6"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 特殊费用审核明细</th>
	 		</tr>
	 		<tr>
		 		<td>审核人</td>
		 		<td>审核时间</td>
		 		<td>审核状态</td>
		 		<td colspan="3">审核内容</td>
	 		</tr>
	 		<c:forEach items="${list }" var="t">
	 			<tr>
	 				<td>${t.AUDITING_PERSON }</td>
	 				<td>${t.AUDITING_DATE }</td>
	 				<td>${t.STATUS }</td>
	 				<td>${t.AUDITING_OPINION }</td>
	 			</tr>
	 		</c:forEach>
	 </table>
 <!-- 添加附件 开始  -->
	<table class="table_info" border="0" id="file">
		<input type="hidden" id="fjids" name="fjids"/>
		<tr>
    		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
  		<%for(int i=0;i<fileList.size();i++) { %>
	  <script type="text/javascript">
	  showUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>');
	  </script>
	<%}%>
	</table> 
  <!-- 添加附件 结束 -->
<!-- 展示附件 结束-->
   <table class="table_query">
   		<tr>
			<th colspan="6" align="left"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 审批</th>
		</tr>
		<tr >
            <td align="right" nowrap="true">审批意见：</td>
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
	           <c:if test="${o_status != 11841014}">
		           <c:if test="${type != o_status}">
		           	<input type="button" onclick="audit(2)" id="btn2" class="normal_btn" value="拒绝" /> 
		           </c:if>
		            <c:if test="${type == o_status}">
		           	<input type="hidden" onclick="audit(2)" id="btn2" class="normal_btn" value="拒绝" /> 
		           </c:if>
	           </c:if>
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
		var apply_m = $('declareSum').value ;
		var audit_m = $('declareSum1').value ;
		if(apply_m*1>audit_m*1){
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
		if($('btn2')!=null && $('btn2')!=undefined && $('btn2')!="null"){
			$('btn2').disabled = true ;
		}
		makeNomalFormCall("<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/auditSpeciaExpenses01JC.json?auditFlag="+val,addSpeciaBack,'fm','queryBtn'); 
	}
	
	function addSpeciaBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/specialExaminesForJC.do";
			fm.submit();
		}
		else
		{
			MyAlert("操作失败！请联系系统管理员！");
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

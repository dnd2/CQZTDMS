<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>退换车及善意索赔费用申请</title>
<% String contextPath = request.getContextPath();
   List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
 %>
 <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript"src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript">
  //审核方法
  function Audit(view){
     if("1"==view || "2"==view)
     {
        if(!$("#audit_reason").val())
        {
          MyAlert("提示：请填写审核说明！");
          return;
        }
      }
      if("0"==view){
       //检测输入金额
      var re = /^[0-9]+.?[0-9]*$/;
      var amount =  document.getElementById("AUDIT_AMOUNT").value;
      if(""==$.trim($("#AUDIT_AMOUNT").val())){
		 MyAlert("提示：请填写审批金额！");
		 return;
		}
      if(!re.test(amount))
      {
         MyAlert("提示：请正确输入审核金额！");
         document.getElementById("AUDIT_AMOUNT").value = "";
         return ;
      }
      if(parseFloat($.trim($("#APPLY_MONEY").val()))<parseFloat($.trim($("#AUDIT_AMOUNT").val()))){
		 MyAlert("提示：审批金额不能大于申请金额！");
		 return;
		}
		if(0.0>parseFloat($.trim($("#AUDIT_AMOUNT").val()))){
				   MyAlert("提示：审批金额不能小于0！");
				   return;
		}
	  }
		var audit_amount = $("#AUDIT_AMOUNT").val();
     var updateturl='<%=contextPath%>/GoodClaimAction/AuditReturncarAndgoodwill.json?&view='+view;
     makeNomalFormCall1(updateturl,sureInsertCommitBack,"fm");
  }
 
  
  function sureInsertCommitBack(json){
      if(json.succ=="1"){
         MyAlert("提示：操作成功！");
         window.location="<%=contextPath%>/ReturnAndclaimAction/returnAndclaim.do";
      }else{
    	  MyAlert("提示：操作失败！");
      }
  }
  //返回
  function returncar(){
     window.location="<%=contextPath%>/ReturnAndclaimAction/returnAndclaim.do";
  }
  
  //初始化加载
  $(function(){
     $(".normal_btn").each(function(){
		 if($(this).val()=="删 除" ){
			 $(this).hide();
		} 
});
  });
  //申请号
  function selecapp(apply_no,ID){
	var special_type =  $("#special_type").val();
     OpenHtmlWindow('<%=contextPath%>/GoodClaimAction/queryByapplyno.do?apply_no='+apply_no+"&ID="+ID+"&special_type="+special_type,800,400);
  }
  //索赔单号
  function selecCLAIMNO(CLAIM_NO){
        var str='<%=contextPath%>/GoodClaimAction/queryByCLAIMNO.json?CLAIM_NO='+CLAIM_NO;
        sendAjax(str,showFunc,null);
        function showFunc(json){
          var id=json.ID;
          var RONO=json.RO_NO;
          var url = "<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?roNo="+RONO+"&ID="+ id;
          OpenHtmlWindow(url,900,500);
        }
       
  }
  
</script>
</head>
<body>
<form id="fm" name="fm">
<input type="hidden" id="id" name="id" value="${map.APPLY_ID }"/>
<input type="hidden" id="apply_no" name="apply_no" value="${map.APPLY_NO }"/>
<input type="hidden" id="special_type" name="special_type" value="${map.SPECIAL_TYPE }"/>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;特殊费用管理&gt;退换车及善意索赔费用申请审核</div>
  <table class="table_info" width="75%">
    <tr>
       <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
    </tr>
    <tr>
      <td>申报单位代码：<input class="long_txt"  type="text" id="DEALER_CODE" name="DEALER_CODE" readonly="readonly" value="${map.DEALER_CODE}"/> </td>
      <td>&nbsp;&nbsp;&nbsp;申报单位名称：<input class="long_txt"  id="DEALER_NAME" name="DEALER_NAME" type="text" readonly="readonly" value="${map.DEALER_NAME}"/> </td>
      <td>结算费用类型： 
            <c:if test="${map.SPECIAL_TYPE==1 }">善意索赔</c:if>
              <c:if test="${map.SPECIAL_TYPE==0 }">退换车</c:if>
      </td>
      <td style="padding-left: 0px;" class="NVL">
      </td>
    </tr>
    <tr>
      <td>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;申请单号：<a href="#"  onclick="selecapp('${map.APPLY_NO }','${map.ID }')">${map.APPLY_NO }</a>
      </td>
      <td>申请金额（元）：<input class="long_txt" readonly="readonly" id="APPLY_MONEY" name="APPLY_MONEY" type="text" value="${map.APPLY_AMOUNT }"/></td>
      <td>审核金额（元）：<input class="long_txt"  id="AUDIT_AMOUNT" name="AUDIT_AMOUNT" type="text" /></td>
     
    </tr>
    <tr>
      <td>服务站联系人：
      <c:if test="${map.DEALER_CONTACT!=null }">
      <input class="long_txt"  id="DEALER_CONTACT" name="DEALER_CONTACT" readonly="readonly" type="text" value="${map.DEALER_CONTACT }" />
      </c:if>
       <c:if test="${map.APPLY_PERSON!=null }">
      <input class="long_txt"  id="APPLY_PERSON" name="APPLY_PERSON" readonly="readonly" type="text" value="${map.APPLY_PERSON }" />
      </c:if>
      </td>
     <c:if test="${map.DEALER_PHONE !=null}">
      <td>服务站联系电话：<input class="long_txt"  id="DEALER_PHONE" name="DEALER_PHONE" type="text" readonly="readonly" value="${map.DEALER_PHONE }" /></td>
     </c:if>
      <td>索赔单号：<a href="#" onclick="selecCLAIMNO('${map.CLAIM_NO }');">${map.CLAIM_NO }</a></td>    
    </tr> 
    <tr>
     <c:if test="${map.TEC_SUPPLY_CODE!=null }">
      <td>&nbsp;&nbsp;&nbsp;供应商代码：<input class="long_txt"  id="TEC_SUPPLY_CODE" name="TEC_SUPPLY_CODE" readonly="readonly" type="text" value="${map.TEC_SUPPLY_CODE }"/></td>
     </c:if>
     <c:if test="${map.RESPONSIBILITY_CODE!=null }">
      <td>&nbsp;&nbsp;&nbsp;责任方代码：<input class="long_txt"  id="RESPONSIBILITY_CODE" name="RESPONSIBILITY_CODE" readonly="readonly" type="text" value="${map.RESPONSIBILITY_CODE }"/></td>
     </c:if>
      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         VIN：<input class="long_txt" id="VIN" name="VIN" type="text" value="${map.VIN }"  />
      </td>
      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;车型：<input class="long_txt"  id="MODEL_NAME" name="MODEL_NAME" readonly="readonly" type="text" value="${map.MODEL_NAME }"/>
      </td>
    </tr>  
     <tr>
      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;状态：${map.CODE_DESC }
      </td>
      <td>&nbsp;&nbsp;&nbsp;当前申报次数：${mapcount }
      </td>
       <td>结算厂家：
        <c:if test="${map.YIELDLY}==2010010100000001" >
            重庆
        </c:if>
      </td>
    </tr>  
  </table>
  <table>
    <tr>
      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;备注：</td>
      <td>
        <textarea id="apply_remark" name="apply_remark" readonly="readonly" rows="5" cols="50">${map.APPLY_REMARK}</textarea>
      </td>
    </tr>
  </table>
  <table>
    <tr>
      <td>审核说明：</td>
      <td>
        <textarea id="audit_reason" name="audit_reason" rows="5" cols="50"></textarea>
      </td>
    </tr>
  </table>
<!-- 添加附件 开始  -->
        <table id="add_file"  width="75%" class="table_info" border="0" id="file">
	    		<tr>
	        		<th>
						<input type="hidden" id="fjids" name="fjids"/>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
			     		<input type="button" class="normal_btn" align="right" id="addfile" onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
					</th>
				</tr>
				<tr>
    				<td width="75%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  				<%if(fileList!=null){
	  				for(int i=0;i<fileList.size();i++) { %>
					  <script type="text/javascript">
				    		addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
				    	</script>
				<%}}%>
			</table> 
  		<!-- 添加附件 结束 -->
  		
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sure" onclick="Audit(0);"  style="width=8%" value="审核通过" />
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="report" onclick="Audit(1);"  style="width=8%" value="审核退回" />
               	&nbsp;&nbsp;
				<input type="button" id="back" onClick="Audit(2);" class="normal_btn"  style="width=8%" value="拒绝"/>
   			    &nbsp;&nbsp;
				<input type="button" id="back" onClick="returncar();" class="normal_btn"  style="width=8%" value="返回"/>
   			</td>
           	<td height="12" align=center width="33%">
     		</td>
		</tr>
</table>
</form>
</body>
</html>


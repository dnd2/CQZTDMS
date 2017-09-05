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
<title>退换车费用申请</title>
<% String contextPath = request.getContextPath();
   List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
 %>
<script type="text/javascript"src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript">
//保存和提报操作
  function sureInsert(view){
    if(!$("#appno").val()){
     MyAlert("提示：请选择申请单号！");
     return;
    }
    if(!$("#APPLY_REMARK").val()){
      MyAlert("提示：请填写备注！");
      return;
    }
     var inserturl='<%=contextPath%>/GoodClaimAction/sureInsert.json?&view='+view;
     makeNomalFormCall1(inserturl,sureInsertCommitBack,"fm");
  }
  function sureInsertCommitBack(json){
         if(json.succ=="-1"){
            MyAlert("提示：操作失败！该数据已存在");
         }
         if(json.succ=="1"){
         MyAlert("提示：操作成功！");
         window.location.href = "<%=contextPath%>/GoodClaimAction/returnAndclaim.do";
      }
     
  }
  function selecapply_no(){
     OpenHtmlWindow('<%=contextPath%>/jsp_new/special/selecReturncarAndgoodwill_apply_no.jsp',600,400);
  }
  //设置值
  function  myCheck(appno,CLAIM_NO,SPE_ID,DEALER_CONTACT,DEALER_PHONE,SUPPLY_CODE_DEALER,APPLY_MONEY,VIN,MODEL_NAME){
    document.getElementById("SPE_ID").value=SPE_ID;
    document.getElementById("APPLY_MONEY").value=APPLY_MONEY;
    document.getElementById("appno").value=appno;
    document.getElementById("CLAIM_NO").value=CLAIM_NO;
    document.getElementById("DEALER_CONTACT").value=DEALER_CONTACT;
    document.getElementById("DEALER_PHONE").value=DEALER_PHONE;
  //  document.getElementById("TEC_SUPPLY_CODE").value=SUPPLY_CODE_DEALER;
    document.getElementById("MODEL_NAME").value=MODEL_NAME;
    document.getElementById("VIN").value=VIN;
  }
    function reback(){
      window.location.href = "<%=contextPath%>/GoodClaimAction/returnAndclaim.do";
    }
  
  
  
</script>
</head>
<form id="fm" name="fm">
<body>
<input type="hidden" id="SPE_ID" name="SPE_ID" />
<input type="hidden" id="DEALER_ID" name="DEALER_ID"  value="${mp.DEALER_ID }"/>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;特殊费用管理&gt;退换车费用申请</div>
 <table class="table_info" width="75%">
    <tr>
       <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
    </tr>
    <tr>
      <td>申报单位代码：<input class="long_txt" type="text" id="DEALER_CODE" name="DEALER_CODE" readonly="readonly" value="${mp.DEALER_CODE}"/> </td>
      <td>&nbsp;&nbsp;&nbsp;申报单位名称：<input class="long_txt"  id="DEALER_NAME" name="DEALER_NAME" type="text" readonly="readonly" value="${mp.DEALER_NAME}"/> </td>
      <td>结算费用类型： 
           <select id="type_claim" name="type_claim">
               <option value="0">-退换车-</option>
            </select>
      </td>
      <td style="padding-left: 0px;" class="NVL">
      </td>
    </tr>
    <tr>
      <td>
        申请单号：<input value="..." type="button" onclick="selecapply_no();"/>&nbsp;&nbsp;<input class="long_txt"  type="text" id="appno" name="appno" readonly="readonly" />
      </td>
      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         VIN：<input id="VIN" class="long_txt"  name="VIN" type="text"  /></td>
        <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;结算厂家：
        <select id="address" name="address">
            <option value="2010010100000001">重庆</option>
         </select>
      </td>
    </tr>
    <tr>
      <td>服务站联系人：<input class="long_txt"  id="DEALER_CONTACT" name="DEALER_CONTACT" readonly="readonly" type="text" /></td>
      <td>服务站联系电话：<input class="long_txt"  id="DEALER_PHONE" name="DEALER_PHONE" type="text" readonly="readonly" /></td>
      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;索赔单号：<input class="long_txt"  id="CLAIM_NO" name="CLAIM_NO" type="text" readonly="readonly" /></td>    
    </tr> 
    <tr>
      
      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;车型：<input class="long_txt"  id="MODEL_NAME" name="MODEL_NAME" readonly="readonly" type="text"/></td>
    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;申请金额：<input class="long_txt"  id="APPLY_MONEY" name="APPLY_MONEY" readonly="readonly" type="text"/></td>
    </tr>   
  </table>
  <table>
    <tr>
      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;备注：</td>
      <td>
        <textarea id="APPLY_REMARK" name="APPLY_REMARK" rows="5" cols="50"></textarea>
      </td>
    </tr>
  </table>
  <table>
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
    <% }}%>
</table> 
<!-- 添加附件 结束 -->
  		
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sure" onclick="sureInsert(0);"  style="width=8%" value="保存" />
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="report" onclick="sureInsert(1);"  style="width=8%" value="提报" />
               	&nbsp;&nbsp;
				<input type="button" id="back" onClick="reback();" class="normal_btn"  style="width=8%" value="返回"/>
   			</td>
           	<td height="12" align=center width="33%">
     		</td>
		</tr>
</table>
</body>

</form>
</html>


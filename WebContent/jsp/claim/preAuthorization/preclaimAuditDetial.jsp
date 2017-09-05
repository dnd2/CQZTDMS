<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="com.infodms.dms.common.Constant" %>
<%@page import="com.infodms.dms.bean.PreclaimAuditBean" %>
<%@page import="java.util.*" %>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔预授权作业_项目审核</title>
<% String contextPath = request.getContextPath(); 
List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
Map map = (Map)request.getAttribute("userMap");
%>
<script type="text/javascript" language="javascript">
	function ModifyStauts(){
		var id=document.getElementById("id").value;
		//var itemId=document.getElementById("itemId").value;
		FRM.action = "<%=contextPath%>/claim/preAuthorization/PreclaimSearch/preclaimAuditDetialAdd.do?id="+id;
		FRM.submit();
	}
		/*
  	增加方法
  	参数：action : "add":增加
  	取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	*/
function subChecked() {
		MyDivConfirm("是否确认审核?",subconfirm);
}
function subconfirm(){
		var id=document.getElementById("id").value;
    	makeNomalFormCall('<%=request.getContextPath()%>/claim/preAuthorization/PreclaimSearch/preclaimAuditDetialAdd.json?id='+id,delBack,'FRM','queryBtn');
}
//新增回调方法：
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyAlert("审核成功！");
		parent.window._hide();
		
	} else {
		MyDivAlert("审核失败！请联系管理员！");
	}
}
//当关闭子页面时，执行此方法调用父页面doInit()方法中的__extQuery__(1);刷新查询
function showReaction(){
	parentContainer.doInit();
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
</script>
<%
PreclaimAuditBean auditBean=(PreclaimAuditBean)request.getAttribute("auditBean");
List<PreclaimAuditBean> list=(List<PreclaimAuditBean>)request.getAttribute("list");
%>

</head>

<body onbeforeunload="showReaction()">
  <div class="navigation">
   	 	<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔预授权&gt;索赔预授权审核作业
  </div>
<form method="post" name = "FRM">
 <input type="hidden" name="id" id="id" value="<%=request.getAttribute("id")%>"></input>
<table class="table_edit">
	  <tr>
		    <th colspan="6">
		    		<img src="<%=contextPath %>/img/subNav.gif" alt="" class="nav" /> 基本信息
		    </th>
	  </tr>
	  <tr>
		    <td class="table_edit_3Col_label_6Letter">经销商代码:</td>
		    <td>
		 		  <%=auditBean.getDealerCode()==null?"":auditBean.getDealerCode() %>
		    </td>
		    <td class="table_edit_3Col_label_5Letter">经销商名称:</td>
		    <td>
		      <%=auditBean.getDealerShortname()==null?"":auditBean.getDealerShortname()%>
		    </td>
		    <td class="table_edit_3Col_label_8Letter">维修工单号:</td>
		    <td>
		           <%=auditBean.getRoNo()==null?"":auditBean.getRoNo() %>
		    </td>
	  </tr>
	  <tr>
		    <td class="table_edit_3Col_label_6Letter">VIN:</td>
		    <td>
		        <%=auditBean.getVin()==null?"":auditBean.getVin() %>
		    </td>
		    <td class="table_edit_3Col_label_5Letter">牌照号:</td>
		    <td>
		         <%=auditBean.getLicenseNo()==null?"":auditBean.getLicenseNo() %>
		    </td>
		    <td class="table_edit_3Col_label_8Letter">发动机号:</td>
		    <td>
		    	<%=auditBean.getEngineNo()==null?"":auditBean.getEngineNo() %>
		    </td>
	  </tr>
	  <tr>
	    <td class="table_edit_3Col_label_6Letter">品牌:</td>
	    <td>
		    <%=auditBean.getBrandName()==null?"":auditBean.getBrandName()%>
	    </td>
	    <td class="table_edit_3Col_label_5Letter">车系:</td>
	    <td>
		   <%=auditBean.getSeriesName()==null?"":auditBean.getSeriesName() %>
	    </td>
	    <td class="table_edit_3Col_label_8Letter">车型:</td>
	    <td>
	       <%=auditBean.getModelName()==null?"":auditBean.getModelName() %>
	    </td>
	  </tr>	  
	  <tr>
		    <td class="table_edit_3Col_label_6Letter">保修开始日期:</td>
		    <td>
			  <%=auditBean.getKeepBegDate()==null?"":auditBean.getKeepBegDate() %>
	        </td>
		    <td class="table_edit_3Col_label_5Letter">产地:</td>
		    <td>
		    	 <%=auditBean.getYieldly()==null?"":auditBean.getYieldly() %>
		    </td>	        
	  </tr>
	  <tr>
		    <td class="table_edit_3Col_label_6Letter">接 待 员:</td>
		    <td>
		   		<%=auditBean.getDestClerk()==null?"":auditBean.getDestClerk()%>
		    </td>
		    <td class="table_edit_3Col_label_5Letter">进厂日期:</td>
		    <td>
		    	 <%=auditBean.getInFactoryDate()==null?"":auditBean.getInFactoryDate() %>
		    </td>
		    <td class="table_edit_3Col_label_8Letter">进厂里程数(公里)：</td>
		    <td>
			    <%=auditBean.getInMileage()==null?"":auditBean.getInMileage() %>
		    </td>		    
	  </tr>
  <tr>
	    <td class="table_edit_3Col_label_6Letter">申请日期:</td>
	    <td>
		    <%=auditBean.getApprovalDate()==null?"":auditBean.getApprovalDate() %>
	    </td>
	    <td class="table_edit_3Col_label_5Letter">申请人:</td>
	    <td>
	   		 <%=auditBean.getApprovalPerson()==null?"":auditBean.getApprovalPerson() %>
	    </td>
	    <td class="table_edit_3Col_label_8Letter">联系电话:</td>
	    <td>
	   		 <%=auditBean.getApprovalPhone()==null?"":auditBean.getApprovalPhone() %>
	    </td>
  </tr>
       <tr>
         <td class="table_edit_3Col_label_6Letter">外出时间:</td>
         <td>
         <%=auditBean.getOutDate()==null?"":auditBean.getOutDate() %>
         </td>
         <td class="table_edit_3Col_label_5Letter">外出人:</td>
         <td>
         	<%=auditBean.getOutPerson()==null ? "" : auditBean.getOutPerson() %>
         </td>
         <td class="table_edit_3Col_label_8Letter">外出费用:</td>
         <td>
         <%=auditBean.getOutFee()==null ? "" : auditBean.getOutFee() %>
         </td>
       </tr> 
       <tr>
         <td class="table_edit_3Col_label_9Letter">申请类型：</td>
         <td>
			<%=auditBean.getApprovalType()==null ? "" : auditBean.getApprovalType()%>;
         </td>
       </tr>          
</table>
<!-- 添加附件 -->
	<table class="table_info" border="0" id="file">
		<input type="hidden" id="fjids" name="fjids"/>
	    <tr colspan="8">
	        <th>
			<img class="nav" src="../../../img/subNav.gif" />
			&nbsp;附件列表：
			</th>
		</tr>
		<tr>
    				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  			</tr>
  			<%for(int i=0;i<attachLs.size();i++) { %>
  			<script type="text/javascript">
  			addUploadRowByDb1('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
  			</script>
  			<%} %>
		</table>
<table align=center width="95%" class="table_list" style="border-bottom:1px solid #DAE0EE" >
    <tr>
	      <th width="3%">序号</th>
	      <th width="6%" >类型</th>
	      <th width="8%">项目代码</th>
	      <th width="13%">项目说明</th>
	      <th width="16%">维修说明</th>
	      <th width="8%">授权代码</th>
	      <th width="9%">审批意见</th>
    </tr>
    <c:forEach var="list" items="${list}">
      <tr class="table_list_row1">
       <input type="hidden" name="itemId" id="itemId" value="${list.itemId}"></input>
	      <td height="20" align=center>
	    	    <c:out value="${list.rum}"/>
	      </td>
	      <td height="20" align=center nowrap>
<%--	      		<c:out value="${list.itemType}"/>--%>
			<script type="text/javascript">
			<!--
				document.write(getItemValue("${list.itemType}"));
			//-->
			</script>	      		
	      </td>
	      <td align=center>
	            <c:out value="${list.itemCode}"/>
	      </td>
	      <td align=center>
	            <c:out value="${list.itemDesc}"/>
	      </td>
	      <td align=center>
	            <c:out value="${list.dealerRemark}"/>
	      </td>
	      <td align=center>
	      		<input name="authCode" type="text"  class="short_txt" size="5" value="<%=map.get("PERSON_CODE") == null ? "":map.get("PERSON_CODE").toString() %>"/>
	      </td>
	      <td align=center>
		       <script type="text/javascript">
   					genSelBoxExp("status",<%=Constant.PRECLAIM_AUDIT%>,"",false,"short_sel","","false",'<%=Constant.PRECLAIM_AUDIT_03%>');
  			   </script>
	      </td>
    </tr>
   </c:forEach>
</table>
		 <table class="table_edit" >
		    <tr>
			    <td align="center">
				    <input name="button2" type="button" class="normal_btn" value="确定" onClick="subChecked();" />
				    <input name="submit3" type="button" class="normal_btn" value="返回" onClick="parent.window._hide();" />
			    </td>
	       </tr>
</table>
</form>
</body>
</html>
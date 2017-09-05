<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	//审批通过
	function passApply(){
	    var str="";
	    var dataIds = document.getElementsByName("infoIds");
	    var auditsum = document.getElementsByName("auditsum");
	    for(var i=0;i<dataIds.length;i++){
	    	str=str+","+dataIds[i].value+"@"+auditsum[i].value;
	    }
	    document.getElementById("strId").value = str;
		fm.action = "<%=contextPath%>/feedbackmng/approve/ServiceInfoApproveManager/serviceInfoApproveEdit.do?flag=1";
		MyConfirm("确认操作?",fm.submit);
		//fm.submit();
	}
	
	//审批驳回，校验审核意见不能为空
	function rejectApply(){
	    
		if(document.getElementById("content").value == null || document.getElementById("content").value == ""){
			 MyAlert("请填写驳回意见！");
			 fm.content.focus();
             return;
		}
		fm.action = "<%=contextPath%>/feedbackmng/approve/ServiceInfoApproveManager/serviceInfoApproveEdit.do?flag=0";
			MyConfirm("确认操作?",fm.submit);
		//fm.submit();
	}

</script>

<title>服务资料售后服务部审批</title>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理&gt;信息反馈提报&gt;服务资料售后服务部审批</div>
 <form method="post" name="fm" id="fm" >
 	<table class="table_edit">
  <th colspan="7"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />审核操作</th>
          <tr > 
            <td height="12" align=left>审核意见：</td>
            <td align=left><span class="tbwhite">
              <textarea  name='content'  id='content'   rows='2' cols='60' ></textarea>
            <input type="button" onclick="passApply()" class="normal_btn" style="width=8%" value="通过"/>
            <input type="button" onclick="rejectApply()" class="normal_btn" style="width=8%" value="驳回"/>
            <input type="button" onclick="javascript:history.go(-1);" class="normal_btn"  style="width=8%" value="返回"/>
            </span></td>
          </tr>
        </table>
        <br />

   <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
	      <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 基本信息</th>
          <tr bgcolor="F3F4F8">
            <td width="14%" align="right">工单号：</td>
            <td width="21%"><c:out value="${ps.ORDER_ID}"/>
                <input type="hidden" name="orderId" value="<c:out value="${ps.ORDER_ID}"/>"></td>
            <td width="13%" align="right">经销商代码：</td>
            <td width="19%"><c:out value="${ps.DEALER_CODE}"/></td>
            <td width="12%" align="right">经销商名称：</td>
            <td width="21%"><c:out value="${ps.DEALER_NAME}"/></td>
          </tr>
          <tr bgcolor="F3F4F8">
            <td align="right">经销商联系人：</td>
            <td><c:out value="${ps.LINK_MAN}"/></td>
            <td align="right">经销商电话：</td>
            <td><c:out value="${ps.TEL}"/></td>
            <td height="27"  align="right" bgcolor="FFFFFF">经销商传真：</td>
            <td bgcolor="FFFFFF"><c:out value="${ps.FAX}"/></td>
          </tr>
          <tr bgcolor="FFFFFF">
            <td height="27"  align="right">邮寄方式：</td>
            <td><script type='text/javascript'>
			       var name=getItemValue('<c:out value="${ps.MAIL_TYPE}"/>');
			       document.write(name);
            </script></td>
            <td height="27"  align="right">邮寄地址：</td>
            <td colspan="3"><c:out value="${ps.MAIL_ADDRESS}"/></td>
          </tr>
          <tr bgcolor="FFFFFF">
            <td height="27" align="right">申请内容：</td>
            <td height="27" colspan="5" align="left" ><c:out value="${ps.SE_CONTENT}"/></td>
          </tr>
      </table>

          <table  class="table_list" style="border-bottom:1px solid #DAE0EE">
          <tr>
		     <th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />资料明细</th>
             </tr>
            <tr >
	        <th align="center">名称</th>
            <th align="center">数量</th>
            <th align="center">审核同意数量</th> 
            <th align="center">单价</th>
            <th align="center">总价</th>
            <th align="center">说明</th>
          </tr>
		  <c:forEach items="${detailList}" var="dl">
       		<tr id="<c:out value="${dl.DATA_ID}"/>">
            	<td>
            		<c:out value="${dl.DATA_NAME}"/>
            		<input type="hidden" name="sure_data_id" value="${dl.ID}"/>
            	</td>
            	<td><c:out value="${dl.AMOUNT}"/></td>
            	<td><input type="text" class="short_txt" name="sure_amount" id="sure_amount" value="${dl.AMOUNT}" onblur='calculate(${dl.DATA_ID},${dl.AMOUNT},this);'/></td>
            	<td><input type="text" name="auditsum" class='short_txt' id="sure_price" value="<c:out value="${dl.PRICE}"/>" onblur='calculate(${dl.DATA_ID})' maxlength="60"/></td>
	            <td><div id="sum_price"><c:out value="${dl.SUMPRICE}"/></div></td>	
           	 	<td><c:out value="${dl.REMARK}"/></td>
           	 	<td><input type="hidden" name="infoIds" value="<c:out value="${dl.DATA_ID}"/>"></td>
        	</tr>
		  </c:forEach>
  </table>
 <TABLE class="table_list" style="border-bottom:1px solid #DAE0EE">
		   <th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 审批明细</th>
          <tr  bgcolor="F3F4F8">
            <th >审批时间</th>
            <th >审批人员</th>
            <th >人员部门</th>
            <th >审批状态</th>
            <th >审批意见</th>
         </tr> 
       <c:forEach items="${auditList}" var="al">
       		<tr>
            	<td><c:out value="${al.AUDIT_DATE}"/></td>
            	<td><c:out value="${al.NAME}"/></td>
            	<td><c:out value="${al.ORG_NAME}"/></td>
	            <td><c:out value="${al.AUDIT_STATUS}"/></td>	
           	 	<td><c:out value="${al.AUDIT_CONTENT}"/></td>
        	</tr>
      </c:forEach>
   </table>  
   <input type="hidden" name="str" id="strId" value=""/>
</form>
<script type="text/javascript">
 // 计算总价显示在页面中
 function calculate(dataId,amount,obj){
      var trNode=document.getElementById(dataId);

      var count = trNode.cells[2].childNodes[0].value;
      var price = trNode.cells[3].childNodes[0].value;
      var sumPrice = price*count;

      if(count>amount){
		trNode.cells[2].childNodes[0].value = amount ;
		MyAlert('审核数量大于申请数量');
		var newtext=document.createTextNode(price*amount);
	      var tdNode = trNode.cells[4];
	      tdNode.innerHTML="";
	      tdNode.appendChild(newtext);
		return ;	
      }
      
      var newtext=document.createTextNode(sumPrice);
      var tdNode = trNode.cells[4];
      tdNode.innerHTML="";
      tdNode.appendChild(newtext); 
 }
</script>
</body>
</html>

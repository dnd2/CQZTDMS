<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
 <title>延期申请页面</title>
<% String contextPath = request.getContextPath(); %>
  <script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
  <script type="text/javascript">
     function Returnold(){
        window.location.href='<%=contextPath%>/OldReturnAction/oldPartApplyAuditList.do';
     }
     //审核通过
     function audit(type){
     	 var url  = "<%=contextPath%>/OldReturnAction/oldPartApplyThrough.json?type="+type+"&RETURN_NO="+${map.RETURN_NO }+"&REBUT_REASON="+$("#REBUT_REASON").val();
          makeNomalFormCall(url,back,'fm');
     }
     function back(json){
              if(json.succ=="1"){
				MyAlert("提示：操作成功！");
				window.location="<%=contextPath%>/OldReturnAction/oldPartApplyAuditList.do"
			}else{
			    MyAlert("提示：操作失败！");
			    window.location="<%=contextPath%>/OldReturnAction/oldPartApplyAuditList.do"
			}
     }
     $(function(){
        $("#remark").text("${map.APPLY_REASON}").attr("readonly","readonly");
         $("#REBUT_REASON").text("${map.REBUT_REASON}");
     })
     
     
    
  </script>
  </head>
  <form id="fm" name="form1"  >
    <body>
     <input   style="display: none;" type="text" name="ID" id="ID" value="${map.ID }"/>
    <table class="table_edit" id="table">
          <tr>
	         <th colspan="6">延期申请信息</th>
          </tr>
          <tr bgcolor="F3F4F8">
            <td align="right" nowrap="nowrap" width="10%" >回运清单号：</td>
            <td align="left" name="RETURN_NO" width="20%">
            <input name="RETURN_NO" value="${map.RETURN_NO}" readonly="readonly"/ style="border: 0px;width: 100%" >
            </td>
            <td align="right" nowrap="nowrap" width="15%">经销商ID：</td>
            <td align="left" name="RETURN_NO" width="20%">
            <input name="DEALER_ID" value="${map.DEALER_ID}" readonly="readonly"/ style="border: 0px;width: 100%" >
            </td>
          </tr>
         
          <tr bgcolor="F3F4F8">
            <td  align="right" nowrap="nowrap"  >经销商名称：</td>
            <td > <input name="DEALER_NAME" value="${map.DEALER_NAME }" readonly="readonly"/ style="border: 0px;width: 100%" >  </td>
	        <td align="right" nowrap="nowrap">索赔申请单数：</td>
              <td align="left" >
             	<input name="WR_AMOUNT" value="${map.WR_AMOUNT}" readonly="readonly"/ style="border: 0px;width: 100%" > 
              </td>
           </tr>
          <tr bgcolor="F3F4F8">
            <td align="right" nowrap="nowrap">配件项数：</td>
            <td align="left">
              	<input name="PARKAGE_AMOUNT" value="${map.PARKAGE_AMOUNT }" readonly="readonly" style="border: 0px;width: 100%" > 
            </td>
            <td align="right" nowrap="nowrap">配件数：</td>
            <td align="left">
               <input name="PART_AMOUNT" value="${map.PART_AMOUNT }" readonly="readonly" style="border: 0px;width: 100%" > 
            </td>
          </tr>
          <tr  bgcolor="F3F4F8">
            <td align="right" nowrap="nowrap">建单日期：</td>
	        <td align="left" name="CREATE_DATE">
	            <input name="CREATE_DATE" value=" <fmt:formatDate value='${map.CREATE_DATE}' pattern='yyyy-MM-dd HH:mm:ss'/>" readonly="readonly" style="border: 0px;width: 100%" > 
	        </td>
	         <td align="right" nowrap="nowrap">状态：</td>
	         <td>
	          <input name="STATUS_DESC" value="${map.CODE_DESC }"  readonly="readonly" style="border: 0px;width: 100%" > 
	         </td>
          </tr>
            
          
          </tr>
         
            <td align="right" nowrap="nowrap">开始时间：</td>
	        <td align="left" name="">
	         <input name="WR_START_DATE" value=" <fmt:formatDate value='${map.START_DATE}' pattern='yyyy-MM-dd'/>" readonly="readonly" style="border: 0px;width: 100%" >  
	        </td>
	         <td align="right" nowrap="nowrap">结束时间：</td>
	         <td>	
	           <input name="RETURN_END_DATE" value=" <fmt:formatDate value='${map.END_DATE}' pattern='yyyy-MM-dd'/>" readonly="readonly" style="border: 0px;width: 100%" >         
	         </td>
           <tr  bgcolor="F3F4F8">
            <td align="right" nowrap="nowrap">申请时间：</td>
	        <td align="left" name="">
	         <input name="WR_START_DATE" value=" <fmt:formatDate value='${map.APPLY_DATE}' pattern='yyyy-MM-dd '/>" readonly="readonly" style="border: 0px;width: 100%" >  
	        </td>
	         <td align="right" nowrap="nowrap">审核时间：</td>
	         <td>	
	           <input name="RETURN_END_DATE" value=" <fmt:formatDate value='${map.AUDIT_DATE}' pattern='yyyy-MM-dd HH:mm:ss'/>" readonly="readonly" style="border: 0px;width: 100%" >         
	         </td>
            <tr  bgcolor="F3F4F8">
            <td align="right" nowrap="nowrap">申请人：</td>
	        <td align="left" name="">
	         <input name="loginUser" value="${map.USER_NAME }" readonly="readonly" style="border: 0px;width: 100%" > 
	        </td>
	         <td align="right" nowrap="nowrap"> 审核人：</td>
	        <td>
	        	 <input name="loginUser" value="${ map.AUDIT_MAN1}" readonly="readonly" style="border: 0px;width: 100%" > 
	        </td>
          </tr>
           <tr bgcolor="F3F4F8" height="50px;" >
            <td align="right" nowrap="nowrap"  >申请备注：</td>
            <td align="left" rowspan="3" colspan="3" width="100%" >
              <span style="color: red;">*</span> <textarea  style="padding-bottom: 0px;padding-top: 0px;" name='remark' value="${map.APPLY_REASON}"	id='remark' rows='4' cols='50' ></textarea>
            </td>
          </tr>
          
  </table>
  <table class="table_edit" id="REBUT_REASON1">
      <tr bgcolor="F3F4F8" height="50px;" style="margin-top: 140px;"  >
            <td align="right" nowrap="nowrap"  width="155px;" >审核备注：</td>
            <td align="left" rowspan="3" colspan="3" width="100%" >
              <span style="color: red;">*</span> <textarea readonly="readonly" name='REBUT_REASON' value="${map.REBUT_REASON}"	id='REBUT_REASON' rows='4' cols='50' ></textarea>
            </td>
       </tr>
       
  </table>
  <table style="margin-left: 40%;" >
       <tr 	  align="center">
            <td width="5%"></td>
            <td nowrap="nowrap"   width="25%" align="center"> <input  type="button"  value="&nbsp;&nbsp;返&nbsp;&nbsp;&nbsp;&nbsp;回&nbsp;&nbsp;" onclick="Returnold();" /> </td>
            <td nowrap="nowrap"   width="25%" align="center"></td>
       </tr>
  </table>
  
  
  </body>
  </form>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld" %>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld" %>
<%
String contextPath = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+contextPath+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>延期申请页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  <script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/InfoAjax.js"></script>
  <script type="text/javascript">
     function Returnold(){
      window.location.href='<%=contextPath%>/OldReturnAction/oldPartApplyAdd.do';
     }
     //检测备注是否填写
     $(function(){
     $("#submit").click(function(){
         if($("#remark").val()==""){
         MyAlert("提示：备注为空，请重新填写");
         return false;
       }else {
         return true;
       }
     });
     $("#remark").text("${map.APPLY_REASON}");
     });
     //修改操作
     function Subm(){
       updateurl='<%=contextPath%>/OldReturnAction/oldPartApplyAddsub.do?type=update&id='+${map.ID}+'&apply_reason='+$("#remark").val();
	   window.location.href = updateurl;
	  
	    	
     }
     
     
  </script>
  <form id="fm"  name="fm" onsubmit="check();">
    <body>
    <table class="table_edit">
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
            <td  align="right" nowrap="nowrap" >经销商名称：</td>
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
	          <input name="STATUS_DESC" value=" ${map.CODE_DESC }" readonly="readonly" style="border: 0px;width: 100%" > 
	         </td>
          </tr>
            <tr  bgcolor="F3F4F8">
            <td align="right" nowrap="nowrap">申请人：</td>
	        <td align="left" name="">
	         <input name="loginUser" value="${loginUser }" readonly="readonly" style="border: 0px;width: 100%" > 
	        </td>
	         <td align="right" nowrap="nowrap"></td>
	         <td></td>
          </tr>
          </tr>
            <tr  bgcolor="F3F4F8">
            <td align="right" nowrap="nowrap">开始时间：</td>
	        <td align="left" name="">
	         <input name="START_DATE" value=" <fmt:formatDate value='${map.START_DATE}' pattern='yyyy-MM-dd'/>" readonly="readonly" style="border: 0px;width: 100%" > 
	        </td>
	         <td align="right" nowrap="nowrap">结束时间：</td>
	         <td>	
	           <input name="END_DATE" value=" <fmt:formatDate value='${map.END_DATE}' pattern='yyyy-MM-dd'/>" readonly="readonly" style="border: 0px;width: 100%" >         
	         </td>
          </tr>
           <tr bgcolor="F3F4F8" height="50px;" >
            <td align="right" nowrap="nowrap"  >申请备注：</td>
            <td align="left" rowspan="3" colspan="3" width="100%" >
              <span style="color: red;">*</span> <textarea name='remark' value="${map.APPLY_REASON}"	id='remark' rows='4' cols='50' ></textarea>
            </td>
          </tr>
         
  </table>
  <table style="margin-left:40%" >
       <tr 	  align="center">
            <td nowrap="nowrap"   width="25%" align="center"> <input type="button" id="submit"  value="保存" onclick="Subm();" /></td>
            <td width="5%"></td>
            <td nowrap="nowrap"   width="25%" align="center"> <input  type="reset"  value="重置" /> </td>
            <td width="5%"></td>
            <td nowrap="nowrap"   width="25%" align="center"> <input  type="button"  value="返回" onclick="Returnold();" /> </td>
            <td nowrap="nowrap"   width="25%" align="center"></td>
       </tr>
  </table>
  
  
  </body>
  </form>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.bean.TtAsWrOldOutDetailBean"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件审批入库</title>
<% 
   String contextPath = request.getContextPath();
   List<TtAsWrOldOutDetailBean> listBean = (List)request.getAttribute("listBean");
%>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;出库单生成</div>
 <form method="post" name ="fm" id="fm">
  <table class="table_edit">
    <tr bgcolor="F3F4F8">
      <td align="right">出库单号：</td>
       <td>${baseBean.outNo}</td>
       <td align="right">出库时间：</td>
       <td>
       <fmt:formatDate value="${baseBean.createDate }" timeStyle="yyyy-MM-dd hh:mm" />
       </td>
     </tr>
     <tr></tr>
     <tr bgcolor="F3F4F8">
       <td align="right">供应商代码：</td>
       <td>
 			${baseBean.outCompanyCode }
		</td>
       <td align="right">供应商：</td>
       <td>
       ${baseBean.outCompany }
       <input type="hidden" name="supplayName" id="supplayName" value="${baseBean.outCompany }"/>
       </td>
     </tr>
        <tr bgcolor="F3F4F8">
       <td align="right">供应商电话：</td>
       <td>
 			${baseBean.outCompanyTel }
 			  <input type="hidden" name="tel" id="tel" value="${baseBean.outCompanyTel }"/>
		</td>
       <td align="right">&nbsp;</td>
       <td>
      &nbsp;
       </td>
     </tr>
  </table>
  <table width="100%" class="table_list">
        <tr class="table_list_th">
            <th>序号</th>
            <th>配件代码</th>
            <th>配件名称</th>
            <th>出门证数量</th>
            <th>型号</th>
            <th>备注</th>
       </tr>
       <c:forEach var="listBean" items="${listBean}" varStatus="num">
        <tr class="table_list_row1">
           <td>
             <c:out value="${num.index+1}"></c:out>
           </td>
           <td>
               <c:out value="${listBean.partCode}"></c:out>
           </td>
           <td>
               <c:out value="${listBean.partName}"></c:out>
           </td>
           <td>
               <c:out value="${listBean.outNum}"></c:out>
           </td>
           <td>
           ${listBean.modelName}
           </td>
            <td>
             <c:out value="${listBean.outRemark}"></c:out>
           </td>
         </tr>
      </c:forEach>  
     </table>
     <table class="table_list">
      <tr > 
       <td height="12" align="center">
          <input type="button"  onclick="history.back();" class="normal_btn" style="width=8%" value="返回"/>
         <input type="hidden" name="outNo" id="outNo" value="${outNo }"/>
         <input type="hidden" name="code" id="code" value="${code }"/>
       </td>
      </tr>
    </table>  
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<br />
<script type="text/javascript">
	function goClaimDetail(code){
	var outNo = document.getElementById("outNo").value;
	var codes = document.getElementById("code").value;
	   OpenHtmlWindow("<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/claimDetail.do?partCode="
				+ code+"&code="+codes+"&outNo="+outNo ,800,500);
   }


function save(){
	var model = document.getElementsByName("model");
	for(var i=0;i<model.length;i++){
		if(model[i].value==""){
			MyAlert("请输入车型!");
			return;
		}
	}
	MyConfirm("是否保存？",outOfStore,[]);
}
function outOfStore(){
	$('save_btn').disabled=true;
 var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/saveOutDoor.json";
 	 makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
}
 function afterCall(json){	
   	var retCode=json.updateResult;
    if(retCode!=null&&retCode!=''){
      if(retCode=="updateSuccess"){
    	    MyAlert("出门证生成成功!");
    	   if(json.yieldly==<%=Constant.PART_IS_CHANGHE_01%>){
    	    fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryListPage.do";
    	    }else{
    	    fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryListPage2.do";
    	    }
			fm.submit();
      }else if(retCode=="updateFailure"){
    	    MyAlert("出门证生成失败!");
     }
   }
  }
</script>
</body>
</html>

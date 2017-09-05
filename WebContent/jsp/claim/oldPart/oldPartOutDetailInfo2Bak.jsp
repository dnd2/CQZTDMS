<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@page import="java.util.*"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件审批入库</title>
<% 
   String contextPath = request.getContextPath();
	List<Map<String,Object>> list  = (List)request.getAttribute("listBean");
%>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;出库明细</div>
 <form method="post" name ="fm" id="fm">
  <table class="table_edit">
    <tr bgcolor="F3F4F8">
      <td align="right">出库单号：</td>
       <td><%=list.get(0).get("OUT_NO") %></td>
       <td align="right">出库时间：</td>
       <td>
        <%=list.get(0).get("OUT_TIME") %>
       </td>
     </tr>
     <tr></tr>
     <tr bgcolor="F3F4F8">
       <td align="right">供应商代码：</td>
       <td>
 		<%=list.get(0).get("SUPPLY_CODE") %>
		</td>
       <td align="right">供应商名称：</td>
       <td>
      <%=list.get(0).get("SUPPLY_NAME") %>
       </td>
     </tr>
       <td align="right">出库类型:</td>
       <td >
      		 <script type="text/javascript">
      		 document.write(getItemValue('<%=list.get(0).get("OUT_TYPE") %>'));
          </script>
       </td>
        <td align="right">出库人:</td>
       <td >
      		 <%=list.get(0).get("NAME") %>
       </td>
     </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
  <table width="100%" class="table_list">
        <tr class="table_list_th">
            <th>序号</th>
            <th>索赔单号</th>
            <th>配件代码</th>
            <th>配件名称</th>
            <th>数量</th>
            <th>配件类型</th>
            <th>备注</th>
       </tr>
       <c:forEach var="listBean" items="${listBean}" varStatus="num">
        <tr class="table_list_row1">
           <td>
             <c:out value="${num.index+1}"></c:out>
           </td>
           <td>
               <a href="#" onclick="selectClaim('${listBean.ID}','${listBean.RO_NO}');"><c:out value="${listBean.CLAIM_NO}"></c:out></a>
           </td>
           <td>
               <c:out value="${listBean.OUT_PART_CODE}"></c:out>
           </td>
           <td>
               <c:out value="${listBean.OUT_PART_NAME}"></c:out>
           </td>
             <td>
               <c:out value="${listBean.OUT_AMOUT}"></c:out>
           </td>
            <td>
               <c:out value="${listBean.HAS_PART}"></c:out>
           </td>
            <td>
               <c:out value="${listBean.REMARK}"></c:out>
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
 var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/saveOutDoor.json";
 	 makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
}
 function afterCall(json){	
   	var retCode=json.updateResult;
    if(retCode!=null&&retCode!=''){
      if(retCode=="updateSuccess"){
    	    MyAlert("保存成功!");
      }else if(retCode=="updateFailure"){
    	    MyAlert("出库失败!");
     }
   }
  }
 //查询索赔单明细传 索赔单ID 和工单ro_no
 function selectClaim(id,roNo){
	 OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailForward.do?roNo='+roNo+ '&ID='+id ,1000,500);
 }
<%--  function goBack(){
	 fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryListPage.do";
	 fm.submit();
	 } --%>
	 __extQuery__(1);
	 
	 
	 var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryOutDetail2.json?outNo=${outNo}&code=${code}";
		var title = null;
		var columns = [
		               
		               
			{header: "序号",sortable: false,align:'center',renderer:getIndex},
			{header: "索赔单号", dataIndex: 'OUT_NO', align:'center'},
			{header: "配件代码", dataIndex: 'SUPPLY_CODE', align:'center'},
			{header: "配件名称", dataIndex: 'SUPPLY_NAME', align:'center'},
			{header: "数量", dataIndex: 'PART_CODE', align:'center'},
			{header: "配件类型", dataIndex: 'PART_NAME', align:'center'},
			{header: "备注", dataIndex: 'PART_UNIT', align:'center'}
		];
</script>
</body>
</html>

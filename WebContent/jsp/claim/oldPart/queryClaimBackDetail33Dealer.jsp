<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.bean.TtAsWrOldPartBackListDetailBean"%>
<%@page import="com.infodms.dms.bean.TtAsWrOldPartDetailListBean"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔回运清单明细</title>
<% 
   String contextPath = request.getContextPath();
   ActionContext act = ActionContext.getContext();
   AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
   String logonName = logonUser.getName();
   TtAsWrOldPartBackListDetailBean detailBean = (TtAsWrOldPartBackListDetailBean)request.getAttribute("claimPartDetailBean");
   List<TtAsWrOldPartDetailListBean> detailList = (List)request.getAttribute("detailList");
%>
</head>
<body>
	<form method="post" name ="fm" id="fm">
 		<div class="navigation">
	<img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理 &gt;索赔旧件管理 &gt;索赔回运清单明细
	</div>
  	<input type="hidden" name="i_back_id" id="i_back_id" value="" />
  	<input type="hidden" name="i_return_no" id="i_return_no" value="" />
  	<input type="hidden" name="i_freight_type" id="i_freight_type" value="" />
  	<input type="hidden" name="i_boxTotalNum" id="i_boxTotalNum" value="" />
  	<input type="hidden" name="i_box_list" id="i_box_list" value="" />
  	<!-- 基本信息 -->
  	<div class="form-panel">
    <h2>基本信息</h2>
    <div class="form-body">
	<table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
        <tr >
        	<td class="right">回运清单号：</td>
            <td><%=detailBean.getReturn_no()%></td>
            <td class="right">提报日期：</td>
            <td>
              <script type="text/javascript">
              if ('<%=detailBean.getReturn_date()%>'==""||<%=detailBean.getReturn_date()%>==null) {
          		  document.write("");
          	    }else {
          		  document.write('<%=detailBean.getReturn_date()%>');
          	    }
              </script>
            </td>
            <td class="right">建单日期：</td>
            <td>
              <%=detailBean.getCreate_date()%>
            </td>
          </tr>
          <tr >
            <td class="right">索赔申请单数：</td>
            <td><%=detailBean.getWr_amount()%></td>
            <td class="right">配件项数：</td>
            <td>
              <%=detailBean.getPart_item_amount()%>
            </td>
            <td class="right">配件数：</td>
            <td>
              <%=detailBean.getPart_amount()%>
            </td>
          </tr>
          <tr >
            <td class="right">货运方式：</td>
            <td>
              <%=CommonUtils.checkNull(detailBean.getTransport_desc())%>
            </td>
            <td class="right">回运类型：</td>
            <td>
              <%=detailBean.getReturn_desc()%>
            </td>
            <td class="right">装箱总数：</td>
            <td><%=detailBean.getParkage_amount()%></td>
          </tr>
          <tr >
            <td class="right">处理状态：</td>
            <td>
              <%=detailBean.getStatus_desc()%>
            </td>
            <td class="right">建单人：</td>
            <td>
              <%=detailBean.getCreator()==null?"":detailBean.getCreator()%>
            </td>
            <td class="right">货运单号：</td>
            <td><%=CommonUtils.checkNull(detailBean.getTran_no())%></td>
          </tr>
          <tr >
            <td class="right">申报运费：</td>
            <td >
              <%=detailBean.getPrice()==null?0:detailBean.getPrice()%>
            </td>
            <td class="right">审核运费：</td>
            <td >
              <%=detailBean.getAuthPrice()==null?0:detailBean.getAuthPrice()%>
            </td>
              <td class="right">发运时间：</td>
            <td >
              <%=detailBean.getSendDate()==null?0:detailBean.getSendDate()%>
            </td>
          </tr>
          <tr >
         	 <td class="right">预计到货时间：</td>
	            <td >
	              <%=detailBean.getArrive_date()==null?0:detailBean.getArrive_date()%>
	            </td>
	             <td class="right" >物流公司：</td>
				<td >
				  <%=CommonUtils.checkNull(detailBean.getTransport_company()) %>
				</td>
            <td class="right">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
    		<td class="right">审核备注：</td>
    		<td colspan="5"><%=CommonUtils.checkNull(detailBean.getPriceRemark())%></td>
  		  </tr>
          <tr>
    		<td class="right">回运备注：</td>
    		<td colspan="5"><%=CommonUtils.checkNull(detailBean.getTransport_remark()) %></td>
  		  </tr>
  		  <tr>
    			<td class="right">签收备注：</td>
    			<td colspan="5"><%=CommonUtils.checkNull(detailBean.getSign_remark()) %></td>
  		  </tr>
          <tr>
			<td colspan="6">
			 </td>
		  </tr>
          <tr>
          	<!-- 回运单id -->
          	<td><input type="hidden" id="orderId" value="<%=detailBean.getId()%>"/></td>
          </tr>
        </table>
    </div>
    </div>
     <!-- 附件信息 -->
    <div class="form-panel">
	<h2>附件信息</h2>
	<div class="form-body">
	  <table class="table_list"   id="file">
	  <tr >
	    <th class="center"> 附件名称 </th>
	    <th class="center"> 操作</th>
	  </tr>
	  <c:forEach items="${fileList}" var="list" varStatus="st">
	  <tr>
	    <td class="center">${list.filename }</td>
		<td class="center">
		  <a href="<%=contextPath%>/util/FileDownLoad/fileDownloadQuery.do?fjid=${list.fjid}" >&nbsp;下载</a>
		</td>
	  </tr>
      </c:forEach>
	</table>
	</div>
	</div>
	<!-- 回运清单明细 -->
	<div class="form-panel">
	<h2>回运清单明细</h2>
	<div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">
        <tr class="table_list_th">
            <th class="center">序号</th>
            <th class="center">索赔申请单</th>
            <th class="center">VIN</th>
            <th class="center">配件代码</th>
            <th class="center">配件名称</th>
            <th class="center">产地</th>
            <th class="center">需回运数</th>
            <th class="center">回运数</th>
            <th class="center">装箱单号</th>
            <th class="center">签收数</th>
            <th class="center">扣件说明</th>
              <th class="center">编号</th>
              <th class="center">审核日期</th>
       </tr>
       <c:forEach var="detailList" items="${detailList}" varStatus="num">
        <tr class="table_list_row1">
           <td class="center">
             <c:out value="${num.index+1}"></c:out>
           </td>
           <td class="center">
               <c:out value="${detailList.claim_no}"></c:out>
           </td>
           <td class="center">
               <c:out value="${detailList.vin}"></c:out>
           </td>
           <td class="center">
               <c:out value="${detailList.part_code}"></c:out>
           </td>
           <td class="center">
               <c:out value="${detailList.part_name}"></c:out>
           </td>
           <td class="center">
               <c:out value="${detailList.proc_factory}"></c:out>
           </td>
           <td class="center">
               <c:out value="${detailList.n_return_amount}"></c:out>
           </td>
           <td class="center">
               <c:out value="${detailList.return_amount}"></c:out>
           </td>
           <td class="center">
              <c:out value='${detailList.box_no}'/>
           </td>
            <td class="center">
               <c:out value="${detailList.sign_amount}"></c:out>
           </td>
           <td class="center">
	       	   <c:if test="${detailList.deduct_remark == '其它'}">
           	  		<c:out value='${detailList.deduct_remark}:${detailList.other_remark}'/>
           	  </c:if>
           	  
           	  <c:if test="${detailList.deduct_remark != '其它'}">
           	  		<c:out value='${detailList.deduct_remark}'/>
           	  </c:if>
           </td>
           
            <td class="center">
              <c:out value='${detailList.barcode_no}'/>
           </td>
           <td class="center">
               <c:out value="${detailList.report_date}"></c:out>
           </td>
         </tr>
      </c:forEach>  
     </table>
	</div>
	</div>
	
     <table class="table_query">
       <tr > 
         <td height="12" class=center>
          <input type="button"  onclick="exportDetail()" class="normal_btn" style="display: none" width=8%" value="打印"/>
          &nbsp;&nbsp;
          <input type="button" onclick="history.back();" class="normal_btn" style="width=8%" value="返回"/>
          </td>
       </tr>
     </table>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
function formatDate(value) {
	 if (value==""||value==null) {
		document.write("");
	 }else {
		document.write(value.substr(0,10));
	 }
}
function exportDetail() {
	var orderId = document.getElementById("orderId").value;
	var url = '<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/exportDetail.do?ORDER_ID=' + orderId;
	window.location = url;
}
</script>
</body>
</html>

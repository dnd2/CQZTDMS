<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infoservice.mvc.context.ActionContext"%>
<%@ page import="com.infodms.dms.bean.AclUserBean"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page
	import="com.infodms.dms.bean.ClaimApproveAndStoredReturnInfoBean"%>
<%@page import="com.infodms.dms.bean.TtAsWrOldPartSignDetailListBean"%>
<%@page import="java.util.List"%>

<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.Map"%><html
	xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件出库</title>
<% String contextPath = request.getContextPath(); %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;旧件扫描出库补录修改</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
  	    <input type="hidden" name="stockIds" id="stockIds" value="${stockId}" />
    <TABLE class="table_query">
     
   
       <tr>
         <td class="table_query_2Col_label_5Letter">序号</td>
         <td class="table_query_2Col_label_5Letter">操作</td>
        <td class="table_query_2Col_label_5Letter">出库单号 </td>
         <td class="table_query_2Col_label_5Letter">出库类型 </td>
          <td class="table_query_2Col_label_5Letter">零件名称 </td>
           <td class="table_query_2Col_label_5Letter">零件名代码</td>        
           <td class="table_query_2Col_label_5Letter">件号</td>        
            <td class="table_query_2Col_label_5Letter">条码</td> 
            
       </tr>
       <c:set var="Num" value="${1}"/>
       
        <c:set var="aaa" />
       <c:set var="bbb" />
       <c:forEach items="${ls}" var="ls">
       
        <tr>
         <td class="table_query_2Col_label_5Letter">${Num}</td>
           <td class="table_query_2Col_label_5Letter"><input type="checkbox" value="${ls.ID}" id="isDel" name="isDel"></td>
        <td class="table_query_2Col_label_5Letter">	${ls.STOCK_NO}  </td>
         <td class="table_query_2Col_label_5Letter">${ls.STOCK_TYPE}</td>
          <td class="table_query_2Col_label_5Letter">${ls.PART_NAME}</td>
           <td class="table_query_2Col_label_5Letter">${ls.PART_CODE}</td>
             <td class="table_query_2Col_label_5Letter">${ls.ERPD_CODE}</td>
             <td class="table_query_2Col_label_5Letter">${ls.BAR_NO}</td>
           
       </tr>
        <c:set var="Num" value="${Num+1}"/>
         <c:set var="aaa" value="${ls.STOCK_ID}" />
          <c:set var="bbb" value="${ls.PART_CODE}" />
       </c:forEach>
    
       <tr>
        <td align="center" colspan="7">
        条码：<input type="text" name='barcodeNo' id='barcodeNo'>
        </td>
        </tr><tr>
       <td align="center" colspan="7">
       
        	<input class="normal_btn" type="button" value="查询" onclick="sel()"/>
       	<input class="normal_btn" type="button" value="全选" onclick="doAllClick()"/>
       <input class="normal_btn" type="button" id="addButton" name="addButton" value="删除"  onClick="del1('${bbb}');">
       <input class="normal_btn" type="button" id="addButton" name="addButton" value="返回"  onClick="back('${aaa}');"></td>
       </tr>
  </table>
  
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form> 
<br>
<script type="text/javascript">
  function back(stockId){
	  var stockIds=document.getElementById("stockIds").value;
	  fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryScanningBulu.do?stockId="+stockIds;
	   fm.method="post";
	   fm.submit();
	  }


  function del(id,stockId,partCode){
	  if (confirm('确定删除吗？')){    
	  fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/deleteBulu.do?id="+id+"&stockId="+stockId+"&partCode="+partCode;
	   fm.method="post";
	   fm.submit();}
	  }

  function sel(partCode){
	  var stockIds=document.getElementById("stockIds").value;
	  var barcodeNo=document.getElementById("barcodeNo").value;

	  if(barcodeNo==''||barcodeNo==null){
			MyAlert("条码不能为空");
			return;
		  }
	  fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryScanningBuluDetail1.do?barcodeNo="+barcodeNo+"&stockId="+stockIds+"&partCode="+partCode;
	   fm.method="post";
	   fm.submit();}

  function del1(partCode){
	  var stockIds=document.getElementById("stockIds").value;
	
	  var chk = document.getElementsByName("isDel");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++)
		{        
			if(chk[i].checked)
			{            
				cnt++;			
			}
		}
      if(cnt==0)
      {
           MyDivAlert("请选择配件！");
      }else{
	        var codes = "";
			var ids = "";
	        for(var i=0;i<l;i++)
			{        
				if(chk[i].checked)
				{
					if(chk[i].value)
					{
						var arr = chk[i].value.split("||");
						if(ids)
						ids += "," + arr[0];
				    	else
				        ids = arr[0];
				        if(codes)
						codes += "," + arr[1];
				    	else
				        codes = arr[1];

				       
				    }    
				}				
			}
		
		 if (confirm('确定删除吗？')){    
			  fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/deleteBulu.do?id="+ids+"&stockId="+stockIds+"&partCode="+partCode;
			   fm.method="post";
			   fm.submit();}
      }
    
	  }

  function doAllClick()
	{
		var chk = document.getElementsByName("isDel");
		var l = chk.length;
		for(var i=0;i<l;i++)
		{        
			chk[i].checked = true;
		}
	}
</script>
</BODY>
</html>
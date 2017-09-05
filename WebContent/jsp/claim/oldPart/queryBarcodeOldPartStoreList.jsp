<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件库存查询</title>
<% String contextPath = request.getContextPath(); %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件库存查询</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
    <TABLE class="table_query">
       <tr>
			<td align="right" nowrap="nowrap">生产基地：</td>
			<td align="left" nowrap="nowrap">
				<script type="text/javascript">
					genSelBoxContainStr("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'<%=CommonUtils.checkNull(request.getAttribute("yieldly"))%>');
			    </script>
			</td>
	         <td class="table_query_3Col_label_5Letter">配件名称： </td>
	         <td nowrap="nowrap">
	          <input id="part_name" name="part_name" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	         </td>
       </tr>
       <tr>
	      
	          <td class="table_query_3Col_label_5Letter">配件代码： </td>
	         <td nowrap="nowrap">
	          <input id="part_code" name="part_code" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	         </td>
	            
			<td align="right" nowrap="nowrap">出库方式：</td>
			<td align="left" nowrap="nowrap">
				<script type="text/javascript">
				 genSelBoxExp("exitScrap",<%=Constant.Exit_scrap%>,'',false,"false","","true",'<%=Constant.Exit_scrap_1%>');
					
			    </script>
			</td>
       </tr>
       <tr>
         <td align="center" nowrap="nowrap" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="__extQuery__(1);">
           &nbsp;&nbsp;
           <input type="button" onclick="preChecked();" class="normal_btn" style="width=8%" value="出库"/>
               &nbsp;&nbsp;
           <input type="button" onclick="detail();" class="normal_btn" style="width=8%" value="出库明细"/>
         </td>
         
     
         
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
   
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/queryBarcodeCurStoreList.json";
				
   var title = null;
   
   var columns = [
			{header: "序号",align:'center',renderer:getIndex},
			{id:'action',header: "<input type='checkbox' id='checkAll' name='checkAll' onclick='selectAll(this,\"orderIds\")'>", width:'8%',sortable: false,dataIndex: 'id',renderer:myCheckBox},		  	
  				{header: "产地", dataIndex: 'YIELDLY', align:'center',renderer:getItemValue},  	 							
  				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
  				{header: "配件名称",dataIndex: 'PART_NAME',align:'center'},		
  				{header: "库存数", dataIndex: 'COUNT_ALL', align:'center'},
  				{header: "出库数", dataIndex: 'COUNT_ALL', align:'center',renderer:editOutStore}
  				
  		      ];
   function doInit(){
	   loadcalendar();
   }
   //出库数编辑框
   function editOutStore(value,meta,record){
	   var id=record.data.PART_CODE;
	   var remainNum=record.data.COUNT_ALL;
	   return String.format("<input type=\"text\" id=\"outStoreNum"+id+"\" name=\"outStoreNum"+id+"\" onkeypress=\"return event.keyCode>=48&&event.keyCode<=57\" class=\"middle_txt\" datatype='0,is_null,30' value='0' onblur=\"checkOutNum('"+id+"',"+remainNum+")\"/>");
   }

 //检查出库数是否合法
   function checkOutNum(textId,remainNum){
	 
	   var outNum=document.getElementById("outStoreNum"+textId).value;
	  
	   if(document.getElementById("outStoreNum"+textId)==null||outNum==''){
			MyAlert("请填写出库数！");
			document.getElementById("outStoreNum"+textId).focus();
	    	return;
		}
		if((remainNum-outNum)<0){
			MyAlert("出库数不能超过库存数！");
			document.getElementById("outStoreNum"+textId).focus();
	    	return;
		}
   }
 //全选checkbox
   function myCheckBox(value,metaDate,record){
   	  return String.format("<input type='checkbox' id='orderIds' name='orderIds' value='" + record.data.PART_CODE + "' />");
   }
   //选中预检查
   function preChecked() {
   	var str="";
   	var chk = document.getElementsByName("orderIds");
   	var len = chk.length;
   	var cnt = 0;
   	for(var i=0;i<len;i++){        
   		if(chk[i].checked){            
   			str = chk[i].value+","+str; 
   			cnt++;
   		}
   	}
   
   	if(cnt==0){
          MyAlert("请选择要出库的配件！");
          return;
       }else{
       	  MyConfirm("确认出库？",outOfStore,[str.substring(0,str.length-1)]);
       }
   }

 //出库操作
   function outOfStore(str){
     var selectIdStr=str;
   
     var idArr=selectIdStr.split(",");
   
     var url_str="?idStr="+selectIdStr;
     for(var i=0;i<idArr.length;i++){
    	 
    	 if(document.getElementById("outStoreNum"+idArr[i]).value=='0'){	
    		MyAlert("出库数不能小于等于零！");
 			return;
         }
    	 url_str+="&outStoreNum"+idArr[i]+"="+document.getElementById("outStoreNum"+idArr[i]).value+"&";
     }
    
     url_str=url_str.substring(0,url_str.length-1);
     var url="<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/outOfStore.json"+url_str;
  
 	 //var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outOfStore.json"+url_str;
 	 makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
   }
   //签收回调处理
   function afterCall(json){
   	var retCode=json.updateResult;
    if(retCode!=null&&retCode!=''){
      if(retCode=="updateSuccess"){
    	    MyAlert("出库成功!");
    	    __extQuery__(1);
      }else if(retCode=="updateFailure"){
    	    MyAlert("出库失败!");
     }
   }
  }


   function detail(){
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/StockDetail.do?";
       fm.method="post";
       fm.submit();
	   }
</script>
</BODY>
</html>
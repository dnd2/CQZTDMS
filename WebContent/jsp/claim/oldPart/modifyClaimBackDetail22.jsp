<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%String contextPath = request.getContextPath();%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="select" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/common.js"></script>
<title>索赔回运清单装箱</title>
<script type="text/javascript">
	function init(){
	   if("10731001"==$('#backType')[0].value){
		 //根据id查找对象，
         var obj=document.getElementById('backType');
		  obj.options.add(new Option("常规回运","10731002")); //这个兼容IE与firefox
	   }
	   if($('#notice')[0].value=='noTrans'){
	   	MyAlert("你还未申请旧件运输方式或者未通过审核,请先申请运输方式！");
	   }
	}
</script>
</head>
<body onload="doInit();">
 <form method="post" name ="fm" id="fm">
  <input type="hidden" name="claimId" id="claimId" value="${claimId }" /><!-- 回运单ID -->
  <input type="hidden" name="notice" id="notice" value="${notice }" />
  <table width="100%">
  <tr>
  	<td>
  	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理 &gt;索赔旧件管理 &gt;索赔件回运物流单管理</div>
  	</td>
  </tr>
  <tr>
  	<td>
  <table class="table_query">
          <tr>
	         <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
          </tr>
          <tr>
          	<td class="right">货运单号：</td>
          	<td >
          		<input type="text" id="returnNo" name="returnNo" value="${returnNo }" class="middle_txt"  readonly="readonly"/>
          	</td>
          	<td class="right">发运日期：</td>
       		<td >
       		  <input id="sendTime" name="sendTime" value="${sendTime }" readonly class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
       		  <span style="color: red;" >*</span>
       		</td>
       		<td class="right">三包员电话：</td>
            <td  colspan="5">
              <input type="text" name="tel" id="tel" class="middle_txt" value="${poValue.tel}" />
              <span style="color: red;" >*</span>
            </td>
          </tr>
          <tr>
	            <td class="right">运费：</td>
	            <td >
	               <input type="text" id="price" name="price"  class="short_txt" value="${poValue.price }" />
	               <span style="color: red;" >*</span>
	            </td>
	            <td class="right">预计到货时间：</td>
	            <td >
	              <input type="hidden" name="returnType" id="returnType" value="${return_type }" />
	              <input id="arriveDate" name="arriveDate" value="${arriveDate }" readonly class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
	              <span style="color: red;" >*</span>
	            </td>
	           <td class="right">货运方式：</td>
            <td >
                <script type="text/javascript">
			      genSelBoxExp("transportType",<%=Constant.OLD_RETURN_STATUS%>,'${poValue.transportType }',false,"","onchange='getTypeChangeStyle(this)'","true",'<%=Constant.OLD_RETURN_STATUS_02%>,<%=Constant.OLD_RETURN_STATUS_04%>');
			    </script>
			    <span style="color: red;" >*</span>
            </td>
          </tr>
          <tr style="height:40px">
	        <td class="right">发运单号：</td>
            <td>
              <div id="transportLogistics" name="transportLogistics">
                <input type="text" name="transportNo" id="transportNo" class="middle_txt" value="${poValue.transportNo }" maxlength="20"/>
                <span style="color: red;" >*</span>
              </div>
              <div id="transportSelf" name="transportSelf" style="display:none">&nbsp;--/--</div>
            </td>
            <td class="right" nowrap="nowrap">快递公司：</td>
            <td>
              <div id="transportLogistics" name="transportLogistics">
                <input type="text" name="transportCompany" id="transportCompany" class="middle_txt" value="${poValue.transportCompany}" maxlength="20" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
                <span style="color: red;">*</span>
              </div>
              <div id="transportSelf" name="transportSelf" style="display:none">&nbsp;--/--</div>
            </td>
          </tr>  
		  <tr>
    		<td class="right">发运备注：</td>
    		<td colspan="5">
    		  <textarea rows="5" cols="100" id="transportRemark" name="transportRemark">${poValue.transportRemark }</textarea>
    		</td>
  		  </tr>
  		</table>
  	    <br/>
        <div class="form-panel">
		<h2><img src="<%=contextPath%>/img/nav.gif"/>附件信息&nbsp;&nbsp;&nbsp;<a class="u-anchor" href="#" onclick="showUpload('<%=contextPath%>','PNG;PDF;JPG;JPEG;BMP;RAR;ZIP;TXT;XLS;XLSX;DOC;DOCX',10)" />添加附件</a> </h2>
		<div class="form-body">
		<table class="table_query" border="0" id="file">
		<tr>
				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
			</tr>
			<c:choose>
				<c:when test="${empty select}">
					<c:forEach items="${fsList}" var="flist">
			    		<script type="text/javascript">
			    			addUploadRowByDb('${flist.filename}','${flist.fjid}','${flist.fileurl}','${flist.fjid}','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
						</script>
	    			</c:forEach>
				</c:when>
				<c:otherwise>
					<c:forEach items="${fsList}" var="flist">
			    		<script type="text/javascript">
			    			addUploadRowByDb('${flist.filename}','${flist.fjid}','${flist.fileurl}','${flist.fjid}','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
						</script>
			    	</c:forEach>
				</c:otherwise>
			</c:choose>
	 	</table>
	</div>
	</div>
      
  </td></tr>
	<tr><td>	  
     <table class="table_query">
       <tr > 
         <td height="12" class="center">
          <input id="a11111" type="button" onclick="Save();" class="u-button u-submit" style="width=8%" value="确定"/>
           &nbsp;&nbsp;
          <input id="b11111" type="button" onclick="history.back();" class="u-button u-cancel" style="width=8%" value="返回"/>
         </td>
       </tr>
     </table>
      </td></tr>
  </table>
     <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
function doInit(){
	var transportType = document.getElementById("transportType");
	getTypeChangeStyle(transportType);
}

function backUp(){
	location.href="<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/returnOrder.do";
}
function Save(){
	if(submitForm(fm)==false){
		return;
	}
	//开始验证
	var sendTime = document.getElementById("sendTime");//发运日期
	var tel = document.getElementById("tel");//三包员电话
	var price = document.getElementById("price");//运费
	var arriveDate = document.getElementById("arriveDate");//预计到货时间
	var transportType = document.getElementById("transportType");//货运方式
	var transportNo = document.getElementById("transportNo");//发运单号
	var transportCompany = document.getElementById("transportCompany");//快递公司
	
	//提示信息
    var msg = "";
    var count = 0;
    if(sendTime.value==""){
        count++;
        msg = msg + "<div>" + count + "、请选择发运日期!</div>\n";
        //repairType.style.border = "solid 1px #FF0000";
    }
    if(tel.value==""){
        count++;
        msg = msg + "<div>" + count + "、请填写三包员电话!</div>\n";
    }
    if(price.value==""){
        count++;
        msg = msg + "<div>" + count + "、请填写运费!</div>\n";
    }else{
        if(!isPrice(price.value)){
            count++;
            msg = msg + "<div>" + count + "、运费必须为金额（两位小数）!</div>\n";
        }else if(parseFloat(price.value)>0){
            if(document.getElementById("fileUploadTab").rows.length<=1){
			    count++;
                msg = msg + "<div>" + count + "、运费大于0时,必须上传附件!</div>\n";
		    }
        }
    } 
    if(arriveDate.value==""){
        count++;
        msg = msg + "<div>" + count + "、请选择预计到货时间!</div>\n";
    }
    if(transportType.value==""){
        count++;
        msg = msg + "<div>" + count + "、请选择货运方式!</div>\n";
    }else{
        if(transportType.value==<%=Constant.OLD_RETURN_STATUS_03%>){//自送
        
        }else{
            if(transportNo.value==""){
                count++;
                msg = msg + "<div>" + count + "、请输入发运单号!</div>\n";
            }
            if(transportCompany.value==""){
                count++;
                msg = msg + "<div>" + count + "、请输入快递公司!</div>\n";
            }
        }
    }
    
    if(msg == ""){
    	MyConfirm("确定补录吗?",save,[]);
      }else{
    	  MyAlert(msg);
    }
}
function save(){
	fm.action = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/updateReturnListInfo22.do";
	fm.submit();
}
function trim(str){ //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
function getTypeChangeStyle(obj) {
  	//alert(obj.value);
  	var transportLogistics = document.getElementsByName("transportLogistics");
  	var transportSelf = document.getElementsByName("transportSelf");
	if(obj.value=='<%=Constant.OLD_RETURN_STATUS_03%>') {//自送
		for(var i=0;i<transportLogistics.length;i++){
			transportLogistics[i].style.display = "none";
		}
		for(var i=0;i<transportSelf.length;i++){
			transportSelf[i].style.display = "block";
		}
  	}else {
  		for(var i=0;i<transportLogistics.length;i++){
			transportLogistics[i].style.display = "block";
		}
		for(var i=0;i<transportSelf.length;i++){
			transportSelf[i].style.display = "none";
		}
  	}
}
//两位小数
function isPrice(value){
  var reg = /^\d+(\.\d{1,2})?$/;
  if (value!="") {
    if (reg.test(value)) {
      return true;
    }
  }
  return false;
}
</script>
</body>
</html>
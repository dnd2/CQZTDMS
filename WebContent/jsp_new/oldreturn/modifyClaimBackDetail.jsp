<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.bean.TtAsWrOldPartBackListDetailBean"%>
<%@page import="com.infodms.dms.bean.TtAsWrOldPartDetailListBean"%>
<%@page import="java.util.List"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔回运清单装箱</title>
<% 
   String contextPath = request.getContextPath();
   ActionContext act = ActionContext.getContext();
   AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
   String logonName = logonUser.getName();
   TtAsWrOldPartBackListDetailBean detailBean = (TtAsWrOldPartBackListDetailBean)request.getAttribute("claimPartDetailBean");
   List<TtAsWrOldPartDetailListBean> detailList = (List)request.getAttribute("detailList");
   boolean flag=detailBean.getReturn_type().equals(Constant.BACK_TRANSPORT_TYPE_01)?true:false;
%>
<script type="text/javascript">
	function doInit(){
	   loadcalendar();
	}
</script>
</head>
<body onload="doInit()">
 <form method="post" name ="fm" id="fm">
  <input type="hidden" name="i_back_id" id="i_back_id" value="" />
  <input type="hidden" name="i_return_no" id="i_return_no" value="" />
  <input type="hidden" name="i_freight_type" id="i_freight_type" value="" />
  <input type="hidden" name="i_return_type" id="i_return_type" value="" />
  <input type="hidden" name="i_boxTotalNum" id="i_boxTotalNum" value="" />
  <input type="hidden" name="i_box_list" id="i_box_list" value="" />
  <table width="100%">
  <tr>
  	<td>
  	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理 &gt;索赔旧件管理 &gt;索赔件回运物流单管理</div>
  	</td>
  </tr>
  <tr>
  	<td>
  <table class="table_edit">
          <tr>
	         <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
          </tr>
          <tr>
          	<td align="right" nowrap="nowrap">货运单号：</td>
          	<td align="left" colspan="5">
          		<input type="text" id="transNo" name="transNo" value="<%=CommonUtils.checkNull(detailBean.getTran_no())%>" class="long_txt" />
          	</td>
          </tr>
          <tr>
            <td align="right" nowrap="nowrap">回运清单号：</td>
            <td align="left"><%=detailBean.getReturn_no()%>&nbsp;</td>
           	<td align="right" nowrap="nowrap">发运日期：</td>
       		<td align="left">
       			<input name="sendDate" id="sendDate" value="<%=CommonUtils.checkNull(detailBean.getSendDate())%>" type="text" 
       			class="short_txt" datatype="0,is_date,10" hasbtn="true" callFunction="showcalendar(event, 'sendDate', false);"/>
       		</td>
            <td align="right" nowrap="nowrap">货运方式：</td>
            <td align="left">
              <script type="text/javascript">
               genSelBoxExp("freight_type",<%=Constant.OLD_RETURN_STATUS%>,<%=detailBean.getTransport_type()%>,false,"min_sel","","false",'');
              </script>
            </td>
            <%-- 
            <td align="right">提报日期：</td>
            <td>
              <script type="text/javascript">
              if ('<%=detailBean.getReturn_date()%>'==""||<%=detailBean.getReturn_date()%>==null) {
          		  document.write("");
          	    }else {
          		  document.write('<%=detailBean.getReturn_date()%>');
          	    }
              </script>
            </td>
            --%>
          </tr>
          <tr>
	            <td align="right" nowrap="nowrap">装箱总数：</td>
	            <td align="left">
	               <input type="text" id="boxTotalNum" name="boxTotalNum" class="short_txt" datatype="1,isDigit,6"
                 value='<%=detailBean.getParkage_amount()==null||"0".equals(detailBean.getParkage_amount())?"":detailBean.getParkage_amount()%>'/>
	            </td>
	            <td align="right" nowrap="nowrap">回运类型：</td>
	            <td align="left">
	                <%=detailBean.getReturn_desc()%>&nbsp;
	            </td>
	            <td align="right" nowrap="nowrap">建单日期：</td>
	            <td align="left">
	                <%=detailBean.getCreate_date()%>&nbsp;
	            </td>
          </tr>
          <tr bgcolor="F3F4F8">
            <td align="right" nowrap="nowrap">索赔申请单数：</td>
            <td align="left">
            	<%=detailBean.getWr_amount()%>&nbsp;
            </td>
            <td align="right" nowrap="nowrap">配件项数：</td>
            <td align="left">
                <%=detailBean.getPart_item_amount()%>&nbsp;
            </td>
            <td align="right" nowrap="nowrap">配件数：</td>
            <td align="left">
                <%=detailBean.getPart_amount()%>&nbsp;
            </td>
          </tr>
          
          <tr bgcolor="F3F4F8">
            <td align="right" nowrap="nowrap">三包员电话：</td>
            <td align="left" colspan="5"><input type="text" name="tel" id="tel" class="long_txt" value="<%=detailBean.getTel() %>" datatype="0,is_null,50" />&nbsp;</td>
          </tr>
          <%-- 
          <tr bgcolor="F3F4F8">
            <td align="right">处理状态：</td>
            <td>
              <%=detailBean.getStatus_desc()%>
            </td>
            <td align="right">建单人：</td>
            <td>
              <%=detailBean.getCreator()%>
            </td>
          </tr>--%>
  </table>
  </td></tr>
  <tr><td>
  <table class="table_edit">
     <tr>
	   <th colspan="8"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 回运清单明细</th>
     </tr>
  </table>
  <table class="table_list" id="boxNo">
     <tr bgcolor="F3F4F8">
     	<th align="center"><input type="checkbox" id="checkBoxAll" name="checkBoxAll" id="checkBoxAll" onclick="selectAll(this,'recesel')" />全选</th>
       <th align="center">序号</th>
       <th align="center">索赔申请单</th>
       <th align="center">VIN</th>
       <th align="center">配件代码</th>
       <th align="center">配件名称</th>
       <th align="center">需回运数</th>
       <th align="center">回运数</th>
       <th align="center">装箱单号</th>
       <!-- <th align="center">装箱单号</th> -->
     </tr>
     <c:forEach var="detailList" items="${detailList}" varStatus="num">
     <tr class="<c:choose><c:when test="${(num.index%2)==0}">table_list_row1</c:when><c:otherwise>table_list_row2</c:otherwise></c:choose>">
       <td>
          <input type="checkbox" name="recesel" id="recesel" value="${detailList.id}" />
       </td>
       <td>
          <c:out value="${num.index+1}"></c:out>
       </td>
       <td>
          <c:out value="${detailList.claim_no}"></c:out>
       </td>
       <td>
          <c:out value="${detailList.vin}"></c:out>
       </td>
       <td>
          <c:out value="${detailList.part_code}"></c:out>
       </td>
       <td>
          <c:out value="${detailList.part_name}"></c:out>
       </td>
       <td>
          <c:out value="${detailList.n_return_amount}"></c:out>
       </td>
       <td>
          <c:set var="flag" value="<%=flag%>"></c:set>
          <c:if test="${flag=='true'}">
            <input type="text" id="returnNum<c:out value='${detailList.id}'/>" class="middle_txt"
             name="returnNum<c:out value='${detailList.id}'/>"  value="<c:out value='${detailList.return_amount}'/>" datatype="1,is_digit,5" blurback="true"
              blurValue="<c:out value='${detailList.n_return_amount}'/>"/>
          </c:if>
          <c:if test="${flag=='false'}">
            <c:out value="${detailList.return_amount}"></c:out>
            <input type="hidden" id="returnNum<c:out value='${detailList.id}'/>" 
             name="returnNum<c:out value='${detailList.id}'/>" datatype='1,is_digit,5' value="<c:out value='${detailList.return_amount}'/>"/>
          </c:if>
       </td>
       <!--<td name="god">
          <input type="text" id="boxOrd<c:out value='${detailList.id}'/>" onblur="javascript:isChange(this.id);"  class="middle_txt"
           name="boxOrd<c:out value='${detailList.id}'/>" value="<c:out value='${detailList.box_no}'/>"/>
       </td>-->
       <td>
       	<label id="boxNo111"><c:out value='${detailList.box_no}'/>
       	<input type="hidden" id="boxOrd<c:out value='${detailList.id}'/>" name="boxOrd<c:out value='${detailList.id}'/>" value="<c:out value='${detailList.box_no}'/>"/>
       </label></td>
     </tr>
     </c:forEach>  
     </table>
	</td></tr>
	<tr><td>	  
     <table class="table_list">
       <tr > 
         <td height="12" align="center">
          <input id="c11111" type="button" onclick="isCheckBox();" class="normal_btn" style="width=16%" value="填写箱号"/>
           &nbsp;&nbsp;
          <input id="a11111" type="button" onclick="Save();" class="normal_btn" style="width=8%" value="确定"/>
           &nbsp;&nbsp;
          <input id="b11111" type="button" onclick="parent.window._hide();" class="normal_btn" style="width=8%" value="关闭"/>
         </td>
       </tr>
     </table>
      </td></tr>
  </table>
     <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
function blurBack(obj){
	checkUrgeBackCondition(obj);
}
//检查回运数量是否大于需回运数量
function checkUrgeBackCondition(obj){
	var checkId=obj;
	var txt_return_num=document.getElementById(checkId).value;
	var n_return_num=document.getElementById(checkId).blurValue;
	var diff_num=txt_return_num-n_return_num;
	if(txt_return_num==''){
		MyDivAlert("请填写紧急回运数！");
    	return 1;
	}
	if(txt_return_num<=0){
		MyDivAlert("紧急回运数不能小于等于零！");
    	return 1;
	}
	if(diff_num>0){
		MyDivAlert("紧急回运数不能超过需回运数！");
    	return 1;
	}
	return 0;
}
//格式化日期
function formatDate(value) {
	 if (value==""||value==null) {
		document.write("");
	 }else {
		document.write(value.substr(0,10));
	 }
}
function Save(){
	var boo = false;
    if(!boo){
		if(submitForm('fm')) {
			boo = true;
		}
    }
	if(!boo){
	}
	
	if(boo){
		if(confirm("确认修改？")){
			$('a11111').disabled=true;
			$('b11111').disabled=true;
			ok();
	  	}
		else{
			$('a11111').disabled=false;
			$('b11111').disabled=false;
		}
	}
}
function ok(){
	fm.i_back_id.value='<%=detailBean.getId()%>';
	fm.i_return_no.value='<%=detailBean.getReturn_no()%>';
	fm.i_freight_type.value=document.getElementById("freight_type").value;
	fm.i_return_type.value='<%=detailBean.getReturn_type()%>';
	fm.i_boxTotalNum.value=document.getElementById("boxTotalNum").value;
	//var url2= "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/jugeMax.json";
	//makeNomalFormCall(url2,afterRetTTTT,'fm');
	//makeNomalFormCall(url,afterRet,'fm');
	
	if(!isChange()){
		$('a11111').disabled=false;
		$('b11111').disabled=false;
		return;
	}
	var url= "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/updateReturnListInfo.json";
	makeNomalFormCall(url,afterRet,'fm');
}
//function afterRetTTTT(json){
	//var msg=json.msg;
	//if(msg=='true'){
		//$('a11111').disabled=false;
		//$('b11111').disabled=false;
		//MyAlert("装箱总数错误,请检查!");
	//}else{
		//var url= "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/updateReturnListInfo.json";
		//makeNomalFormCall(url,afterRet,'fm');
	//}
//}

function afterRet(json){
   var retCode=json.updateResult;
   if(retCode!=null||retCode!=''){
     if(retCode=="updateSuccess"){
    	 //MyAlertForFun("修改成功!",closeMeAndRefreshParentPage);
    	 MyAlert("修改成功！");
    	 closeMeAndRefreshParentPage();
        //goSearchPage();
     }else if(retCode=="updateFailure"){
    	 MyAlert("修改失败！");
    	//goSearchPage();
     }
   }
}
function closeMeAndRefreshParentPage(){
   parent.window._hide();
   parentContainer.refreshPage();
}
//到查询页面
function goSearchPage(){
	window.location.href = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryListPage.do";
}

Object.prototype.equals=function(){

    throw new Error("don't implements equals method!");
}
Date.prototype.equals=function(d){
 if(d instanceof Date&&this.valueOf()===d.valueOf())
  return true;
  else
         return false;
}
String.prototype.equals=function(anotherString){
 if (this === anotherString) {
        return true;
     }
               if (anotherString instanceof String||typeof anotherString =="string") {
                  var anotherStringObj = new String(anotherString) ;
                  if(anotherStringObj.length!=this.length) return false;
                     for(var i=0;i<this.length;i++)
                                    {
                                             if(this.charAt(i)!=anotherStringObj.charAt(i))
                                                       return false;
                                    }
                             return true;
               }
     return false;
}   

Number.prototype.equals=function(anotherNumber){
 if (this === anotherNumber) {
        return true;
     }
     if (anotherNumber instanceof Number||typeof anotherNumber ==="number") {
        var anotherNumberObj = new Number(anotherNumber) ;
           if(this.valueOf()===anotherNumberObj.valueOf())
                                      return true;
     }
    return false;
}
Boolean.prototype.equals=function(anotherBoolean){
 if (this === anotherBoolean) 
        return true;
     
     if (anotherBoolean instanceof Boolean||typeof anotherBoolean ==="boolean") {
           if(this.valueOf()===anotherBoolean.valueOf())
                                      return true;
     }
     return false;
} 
Array.prototype.equals=function(anotherArray){
 if (this === anotherArray) {
        return true;
     }
//define find method        
var findStr=function(arr){
                       var count=0;
                       var result=[];
                       if(arr instanceof Array){
                                for(var i=0;i<arr.length;i++){
                                          if(typeof arr[i]==="string"||arr[i] instanceof String){
                                                    result[count]=arr[i];
                                                    count++;
                                          }
                                }
                                return result;
                       }
                       esle
                                return null;
              }
var findNum=function(arr){
                       var count=0;
                       var result=[];
                       if(arr instanceof Array){
                                for(var i=0;i<arr.length;i++){
    if((typeof arr[i]==="number"||arr[i] instanceof Number)&& !isNaN(arr[i])){
                                                    result[count]=arr[i];
                                                    count++;
                                          }
                                }
                                return result;
                       }
                       esle
                                return null;
              }
var findBoolean=function(arr){
                       var count=0;
                       var result=[];
                       if(arr instanceof Array){
                                for(var i=0;i<arr.length;i++){
                                          if(typeof arr[i]==="boolean"||arr[i] instanceof Boolean){
                                                    result[count]=arr[i];
                                                    count++;
                                          }
                                }
                                return result;
                       }
                       esle
                                return null;
              }
var findArray=function(arr){
                       var count=0;
                       var result=[];
                       if(arr instanceof Array){
                                for(var i=0;i<arr.length;i++){
                                          if(arr[i] instanceof Array){
                                                    result[count]=arr[i];
                                                    count++;
                                          }
                                }
                                return result;
                       }
                       esle
                                return null;
              }
var findObject=function(arr){
                       var count=0;
                       var result=[];
                       if(arr instanceof Array){
                                for(var i=0;i<arr.length;i++){
              if(typeof arr[i]==="object"&& !(arr[i] instanceof Array)&& arr[i]!==null&&
                                             !(arr[i] instanceof Boolean)&&!(arr[i] instanceof Number&&!(arr[i] instanceof String))){
                                                    result[count]=arr[i];
                                                    count++;
                                          }
                                }
                                return result;
                       }
                       esle
                                return null;
              }                 
    //begin to check equality 
     if (anotherArray instanceof Array) {
           
           if(this.length!=anotherArray.length)
                           return false;
           //string                   
         var this_StrArray=findStr(this).sort();
         var another_StrArray=findStr(anotherArray).sort();
         if(this_StrArray.length!=another_StrArray.length)
                         return false;
         for(var i=0;i<this_StrArray.length;i++)
                if(!this_StrArray[i].equals(another_StrArray[i]))
                         return false;
         //number
         var this_NumArray=findNum(this).sort();
         var another_NumArray=findNum(anotherArray).sort();
         if(this_NumArray.length!=another_NumArray.length)
                         return false;
         for(var i=0;i<this_NumArray.length;i++)
                if(!this_NumArray[i].equals(another_NumArray[i]))
                         return false;
         //boolean
         var this_BooleanArray=findBoolean(this).sort();
         var another_BooleanArray=findBoolean(anotherArray).sort();
         if(this_BooleanArray.length!=another_BooleanArray.length)
                         return false;
         for(var i=0;i<this_BooleanArray.length;i++)
                { 
                         if(!this_BooleanArray[i].equals(another_BooleanArray[i]))
                         return false;
                }
                //object
         var this_Object=findObject(this);
         var another_Object=findObject(anotherArray);
         if(this_Object.length!=another_Object.length)
                         return false;
         for(var i=0;i<this_Object.length;i++)
         {      for(var j=0;j<another_Object.length;j++)
                          { if(!this_Object[i].equals(another_Object[j]))
                                      continue;
                                    else
                                           //success++;
                                            break;
                          }
                          //MyAlert(j); 
                          if(j>=another_Object.length)
                                             return false;
        }
         //array 
         var this_Array=findArray(this);
         var another_Array=findArray(anotherArray);
         if(this_Array.length!=another_Array.length)
                         return false;
                 //var success=0;      
         for(var i=0;i<this_Array.length;i++)
         {      for(var j=0;j<another_Array.length;j++)
                          { if(!this_Array[i].equals(another_Array[j]))
                                      continue;
                                    else
                                           //success++;
                                            break;
                          }
                          //MyAlert(j); 
                          if(j>=another_Array.length)
                                             return false;
        }
       /* if(success<this_Array.length)
                 return false;*/
                           
       return true;
     }
    return false;
}


var AUITrim=function(v){   
         return   v.replace(/(^\s+)|(\s+$)/g,   "");   
       }

var AUIMap=function(){
var table=[""];
this.set=function(k,v){
   if(k===null) return putForNullKey(v);
     for(var i=1;i<table.length;i++)
    { 
     if(table[i].getKey().equals(k))
      return table[i].setValue(v);
   }
     addEntry(k,v,table.length);
     return null;                  
}
this.get=function(k){
    if(k===null) return getForNullKey();
     for(var i=1;i<table.length;i++)
     {
      if(table[i].getKey().equals(k))
       return table[i].getValue();
     } 
    return null;        
}
//remove the entry who's key equals parameter
this.remove=function(key){
    if(key===null)
     if(table[0] instanceof Entry)
     { var temp= table[0];
      table[0]="";
      return temp.value;
     }
     else
      return null;
    for(var i=1;i<table.length;i++)
    {
     if(table[i].getKey().equals(key))
     {
      return table.splice(i,1)[0].getValue();
     }   
    }
     return null;      
}
//return all of entry this map contains with form "[key1:v1],[key2:v2]"
this.toString=function(){ 
     var str="";
   if(table[0] instanceof Entry)
    str+=table[0].toString()+",";
    for(var i=1;i<table.length;i++)
     str+=table[i].toString()+",";
   return str;
}
// check the map is empty or not!
this.isEmpty=function(){
   if(table[0] instanceof Entry)
    return false;
   else return table[1]instanceof Entry ?false:true; 

}
//empty the map
this.clear=function(){
table.length=1;
table[0]="";
}
//check the map contains this key or not
this.containsKey=function(key){
if(key===null) 
 return table[0] instanceof Entry ?true:false;
for(var i=1;i<table.length;i++)
{
 if(table[i].getKey().equals(key)) 
  return true;
}
return false;
}
//inner method,not invoked by user
var getForNullKey=function(){
 if(table[0] instanceof Entry)
                return table[0].getValue();
        else 
                  return null;      
}
//inner method,not invoked by user         
var putForNullKey=function(value){
    if(table[0] instanceof Entry)
  return table[0].setValue(value);
addEntry(null,value,0);
return null;
}
//inner method,not invoked by user                      
var addEntry=function(key,value,bucketIndex){
 table[bucketIndex]=new Entry(key,value);
}
//iterate the map keys collection,pass the "fun" parameter one key every time
this.each = function(fun){ 
    if(typeof fun=="function")
{var keys=this.keySet();
for(var i=0;i<keys.length;i++)
        fun(keys[i]);
}
}
//return the map's length
this.size=function(){ if(table[0] instanceof Entry)
             return table.length;
            else
             return table.length-1;
                   }
//return key collection this map contains
this.keySet=function(){
var keys=[];
if(this.containsKey(null))
 keys[0]=null;
for(var i=1;i<table.length;i++)
    keys[i]=table[i].getKey();
if(keys[0]===null)
  return keys;
else 
  return keys.slice(1);   
}
           
//Entry inner class                                                                    
var Entry= function (key,value){
var key=key;

var value=value;
this.getKey =function(){return key;}
this.getValue=function(){return value;}
this.setValue=function(v){
                    var oldvalue=value;
                     value=v;
                     return oldvalue;
                   }
this.toString=function(){
                    return "["+key+":"+value+"]";
                    }
}
}

//AUISet class   
var AUISet=function(){
var map=new AUIMap();
var obj={};
//if this set did not already contain the specified element,return true
this.add=function(o){
return map.set(o,obj)==null;
}
//check the set contains this element or not
this.contains=function(o){
return map.containsKey(o);
}
//iterate set element,pass parameter function the element containd in the set
this.each=function(fun){
map.each(fun);
}
//empty the set
this.clear=function(){
map.clear();
}
//return set's length
this.size=function(){
return map.size();
}
//if the set contained the specified element,return true;
this.remove=function(o){
return map.remove(o)===obj;
}
//check the set is empty or not
this.isEmpty=function(){
return map.isEmpty();
}
//return all of element with form "v1,:v2" 
this.toString=function(){
 var str="";
 var arr=map.keySet();
  for(var i=0;i<arr.length;i++)
     str+=arr[i]+",";
 return str;
}
}


function isChange(curValue){
	var flag=true;
	var set=new AUISet();
	var tb=document.getElementById("boxNo");
	var rows=tb.rows;
    for(var i=0;i<rows.length;i++){ 
        if(i>0){
	       	var cells=rows[i].cells; 
	       	//var value=cells[7].childNodes[0].value;
	       	var value=trim(cells[8].innerText);
	       	//if(isNaN(value)){
	           	//MyAlert('第'+i+"行请输入数字!");
	           	//return;
	       	//}
	       
	       	if(value=='' || value==null || value==undefined){
		       	
	        }else{
	    		set.add(value);
	        }
    	}
    } 
    var boxTotalNum=document.getElementById("boxTotalNum").value;
    if(new Number(boxTotalNum)>0){
	    if(new Number(boxTotalNum)<set.size()){
	        MyAlert('实际装'+set.size()+'箱大于总箱数'+boxTotalNum+'箱!');
	        flag=false;
	    }
    }else if(new Number(boxTotalNum)<=0){
    	MyAlert('请填写装箱总数!');
    	flag=false;
    }
    return flag;
}
function trim(str){ //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}

function isCheckBox(){
	//if(!isChange()){
		//return;
	//}
	fm.i_back_id.value='<%=detailBean.getId()%>';
	fm.i_boxTotalNum.value=document.getElementById("boxTotalNum").value;
	var checkboxs = document.getElementsByName("recesel");
	var n = checkboxs.length;
	var no= window.showModalDialog('<%=contextPath%>/jsp/claim/oldPart/inputBoxNo.jsp','','dialogWidth=200px;dialogHeight=100px');
	
	if(no=='' || no=='undefined' || no ==null){
		return;
	}else{
		for (var i = 0; i < n; i++) {
	        if (checkboxs[i].checked) {
	            var tb=document.getElementById("boxNo");
	       	    var rows=tb.rows;
				var hidd="<input type='hidden' id='boxOrd"+checkboxs[i].value+"'  name='boxOrd"+checkboxs[i].value+"' value='"+no+"'></input>";
	       	 	rows[i+1].cells[8].innerHTML=no+hidd;
	        }
	    }
	    //异步更新数据
		var url= "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/updateReturnDetailInfo.json";
		makeNomalFormCall(url,afterRet222,'fm');
	}
}
function afterRet222(json){
	var ok=json.ok;
	if(ok=="ok"){
		MyAlert("装箱成功!");
		var checkboxs = document.getElementsByName("recesel");
		var n = checkboxs.length;
		for (var i = 0; i < n; i++) {
	        if (checkboxs[i].checked) {
	        	checkboxs[i].checked=false;
	        }
	    }
	}else{
		MyAlert("装箱失败!");
		return;
	}
}
</script>
</body>
</html>

function MyConfirmR(info,_width,_height,funYes,arrYes,funNo,arrNo){
	var owner = getTopWinRef();
	var _widthAfter=_width-5;
	var _heightAfter=_height-5;
	try{
	_dialogInit(_widthAfter,0,0,0);
	owner.getElementById('dialog_back_glass_div').style.height =_heightAfter+'px';	
	owner.getElementById('dialog_back_iframe').style.height = _heightAfter+'px';
	owner.getElementById('dialog_modal_div').style.height = _heightAfter+'px';
	owner.getElementById('dialog_content_div').innerHTML='\
	<div style="font-size:12px;">\
	 <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
	  <b>信息</b>\
	 </div>\
	 <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
	 <div style="padding:2px;text-align:center;background:#D0BFA1;">\
	  <input id="dialog_yes_button" type="button" value=" 确定 " style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
	  <input id="dialog_no_button" type="button" value=" 取消 " style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
	 </div>\
	</div>';
	owner.getElementById('dialog_alert_info').innerHTML=info;
	owner.getElementById('dialog_yes_button').onclick=function(){
	try{
	 if(funYes&&arrYes){
	  funYes.apply(this,arrYes);
	 }else if(funYes){
	  funYes();
	 }
	}finally{
	 _hide();
	}
	};
	owner.getElementById('dialog_no_button').onclick=function(){
	try{
	 if(funNo&&arrNo){
	  funNo.apply(this,arrNo);
	 }else if(funNo){
	  funNo();
	 }
	}finally{
	 _hide();
	}
	};	
	_setSize(300, 100,'',700,300); 
	_show();
	}catch(e){
	alert('MyAlert : '+ e.name+'='+e.message);
	}finally{
	owner=null;
	}
}
function MyAlertR(info,_width,_height){
	 var owner = getTopWinRef();
	 var _widthAfter=_width-5;
	 var _heightAfter=_height-5;
	 try{
	  _dialogInit(_widthAfter,0,0,0); 
	  owner.getElementById('dialog_back_glass_div').style.height = _heightAfter+'px';	
		owner.getElementById('dialog_back_iframe').style.height = _heightAfter+'245px';
		owner.getElementById('dialog_modal_div').style.height = _heightAfter+'245px'; 
	  owner.getElementById('dialog_content_div').innerHTML='\
	    <div style="font-size:12px;">\
	     <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
	      <b>信息</b>\
	     </div>\
	     <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
	     <div style="padding:2px;text-align:center;background:#D0BFA1;">\
	      <input id="dialog_alert_button" type="button" value="确定" style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
	     </div>\
	    </div>';
	  owner.getElementById('dialog_alert_info').innerHTML=info;
	  owner.getElementById('dialog_alert_button').onclick=_hide;
	  
	  _setSize(300, 100,'',700,300); 
	  _show();
	 }catch(e){
	  alert('MyAlert : '+ e.name+'='+e.message);
	 }finally{
	  owner=null;
	 }
}
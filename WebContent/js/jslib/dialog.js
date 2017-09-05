// JavaScript Document
// JavaScript Document
/*===========================Open Modal window===========================*/

var str_modal_error='\
	<div style="font-size:12px;">\
		<div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
			<b>错 误</b>\
		</div>\
		<div style="padding:10px; line-height:2em">PARAM_content</div>\
		<div style="padding:2px;text-align:center;background:#D0BFA1;">\
			<input type="button" value="确 定"\ style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"\ onclick="javascript:eval(PARAM_cbMth);"/>\
		</div>\
	</div>';
	
var str_modal_loading='\
	<div style="font-size:12px;">\
		Loading...\
	</div>';

var fullDiv=null,fullIframe=null,modalDiv=null,backDiv=null,contDiv=null;
function OpenModalWindow(url,width,height){

	fullDiv = _crtFullDiv();
	fullIframe = _ctrFullIframe();
	modalDiv = _crtModalDiv();
	backDiv = _crtBackDiv();
	contDiv = _crtContentDiv();

	$(contDiv).update(str_modal_loading);
	_setDSize(contDiv,backDiv,modalDiv, width, height);
	$(fullDiv).show();
	$(fullIframe).show();
	$(modalDiv).show();
	$(backDiv).show();
	$(contDiv).show();	

	new Ajax.Request(url, {
			method:'post',
			
			onFailure: function(){
				var contStr = str_modal_error.replace(new RegExp('PARAM_content','gm'),'request failed!');
				contStr= contStr.replace(new RegExp('PARAM_cbMth','gm'),'_remove_D(\''+contDiv+'\',\''+backDiv+'\',\''+modalDiv+'\',\''+fullIframe+'\',\''+fullDiv+'\')');
				$(contDiv).update(contStr);
				_setDSize(contDiv,backDiv,modalDiv, width, $(contDiv).down().offsetHeight);
			},
	  		onSuccess: function(transport){
				var contStr = transport.responseText;
				contStr= contStr.replace(new RegExp('PARAM_REMOVE','gm'),'_remove_D(\''+contDiv+'\',\''+backDiv+'\',\''+modalDiv+'\',\''+fullIframe+'\',\''+fullDiv+'\')');
				$(contDiv).update(contStr);
				_setDSize(contDiv,backDiv,modalDiv, width, $(contDiv).down().offsetHeight);
	  		},
			onException:function(url){
				
				var contStr = str_modal_error.replace(new RegExp('PARAM_content','gm'),arguments[1].name+'='+arguments[1].message);
				contStr= contStr.replace(new RegExp('PARAM_cbMth','gm'),'_remove_D(\''+contDiv+'\',\''+backDiv+'\',\''+modalDiv+'\',\''+fullIframe+'\',\''+fullDiv+'\')');
				$(contDiv).update(contStr);
				_setDSize(contDiv,backDiv,modalDiv,width, $(contDiv).down().offsetHeight);
			}
	 	});		
}


/* ================ function MyConfirm() ============================*/
var str_dialog_confirm='\
	<div style="font-size:12px;">\
		<div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
			<b>确 认</b>\
		</div>\
		<div style="padding:10px; line-height:2em">PARAM_content</div>\
		<div style="padding:2px;text-align:center;background:#D0BFA1;">\
			<input type="button" onclick="javascript:eval(PARAM_REMOVE);eval(PARAM_yCbMth);" value="确 定" style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\
			<input type="button" onclick="javascript:eval(PARAM_REMOVE);eval(PARAM_cFunc)" value="取 消" style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
		</div>\
	</div>';
function MyConfirm(info, yCbMth, cFunc){
	try{
		fullDiv = _crtFullDiv();
		fullIframe = _ctrFullIframe();
		modalDiv = _crtModalDiv();
		backDiv = _crtBackDiv();
		contDiv = _crtContentDiv();

		var contStr = str_dialog_confirm.replace(new RegExp('PARAM_content','gm'),info);
		contStr= contStr.replace(new RegExp('PARAM_REMOVE','gm'),'_remove_D(\''+contDiv+'\',\''+backDiv+'\',\''+modalDiv+'\',\''+fullIframe+'\',\''+fullDiv+'\')');
		contStr= contStr.replace(new RegExp('PARAM_yCbMth','gm'),yCbMth);
		contStr= contStr.replace(new RegExp('PARAM_cFunc','gm'),cFunc);
		
		$(contDiv).update(contStr);
		
		_setDSize(300, 200);	
		$(fullDiv).show();
		$(fullIframe).show();
		$(modalDiv).show();
		$(backDiv).show();
		$(contDiv).show();	
		_setDSize(300, $(contDiv).down().offsetHeight);
	}catch(e){
		alert('MyAlert : '+ e.name+'='+e.message);
	}
}



/*========================function myAlert()==================================*/
var str_dialog_alert='\
	<div style="font-size:12px;">\
		<div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
			<b>信 息</b>\
		</div>\
		<div style="padding:10px; line-height:2em">PARAM_content</div>\
		<div style="padding:2px;text-align:center;background:#D0BFA1;">\
			<input type="button" value="确 定"\ style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"\ onclick="javascript:eval(PARAM_cbMth);eval(PARAM_aFunc);"/>\
		</div>\
	</div>';

function MyAlert(info,aFunc){
	try{
		fullDiv = _crtFullDiv();
		fullIframe = _ctrFullIframe();
		modalDiv = _crtModalDiv();
		backDiv = _crtBackDiv();
		contDiv = _crtContentDiv();

		var contStr = str_dialog_alert.replace(new RegExp('PARAM_content','gm'),info);
		contStr= contStr.replace(new RegExp('PARAM_cbMth','gm'),'_remove_D(\''+contDiv+'\',\''+backDiv+'\',\''+modalDiv+'\',\''+fullIframe+'\',\''+fullDiv+'\')');
		contStr= contStr.replace(new RegExp('PARAM_aFunc','gm'),aFunc);
		$(contDiv).update(contStr);

		_setDSize(300, 200);	
		$(fullDiv).show();
		$(fullIframe).show();
		$(modalDiv).show();
		$(backDiv).show();
		$(contDiv).show();	
		_setDSize(300, $(contDiv).down().offsetHeight);
	}catch(e){
		alert('MyAlert : '+ e.name+'='+e.message);
	}
}

/*====================辅助方法============================================*/
function _remove_D(){
	try{
		$(contDiv).remove();
		$(backDiv).remove();
		$(modalDiv).remove();
		$(fullIframe).remove();
		$(fullDiv).remove();
		fullDiv=null,fullIframe=null,modalDiv=null,backDiv=null,contDiv=null;
	}catch(e){
		alert('_remove_D : '+ e.name+'='+e.message);
	}
}

function _setDSize(width, height){//设置高度和宽度
	try{
		var bwidth = 8;
		var ctop = (document.viewport.getHeight()-height)/3;
		var cleft= (document.viewport.getWidth()-width)/2;
		
		$(contDiv).setStyle({width: width+'px', height: height+'px', left: cleft+'px', top: ctop+'px'});
		$(backDiv).setStyle({width: (width+bwidth*2)+'px', height: (height+bwidth*2)+'px', left: (cleft-bwidth)+'px', top: (ctop-bwidth)+'px'});	
		$(modalDiv).setStyle({width: document.viewport.getWidth()+'px', height: document.viewport.getHeight()+'px', left: '0px', top: '0px'});
	}catch(e){
		alert('_setDSize : '+ e.name+'='+e.message);
	}
}

function _crtFullDiv(){//创建全屏 div
	try{
		var objId = 'div_full_';
		var obj = new Element('div',{id:objId});
		var tmp = 0;
		obj.style.width = '100%';
		if(parent.$('inIframe')){
			tmp = parentContainer.document.body.scrollHeight;
			obj.style.height = ((tmp <= 570)?window.screen.availHeight:tmp) + 'px';
		}else{
			obj.style.height = 1000 + 'px';
		}		
		window.scroll(0,0);
		obj.style.left = '0px';
		obj.style.top = '0px';
		obj.setOpacity(0.4);
		obj.setStyle({
					zIndex:100,
					position:'absolute',
					background:'#000',
					display:'none'
					});			

		document.body.appendChild(obj);
		return objId;
	}catch(e){
		alert('_crtFullDiv : '+ e.name+'='+e.message);
	}	
}

function _ctrFullIframe(){
	try{
		var fObjId = 'div_iframe_';
		var fObj = new Element('iframe',{id:fObjId});		
		fObj.style.width = '100%';
		fObj.style.height = 600 + 'px';

		fObj.style.left = '0px';
		fObj.style.top = '0px';
		fObj.setOpacity(0);
		fObj.setStyle({
					zIndex:101,
					position:'absolute',
					background:'#000',
					display:'none'
					});	

		document.body.appendChild(fObj);
		return fObjId;
	}catch(e){
		alert('_crtFullIframe : '+ e.name+'='+e.message);
	}	
}

function _crtModalDiv(){//创建modal div
	try{
		var objId = 'div_modal_';
		var obj = new Element('div',{id:objId});
		obj.setOpacity(0);
		obj.setStyle({
					zIndex:102,
					position:'absolute',
					background:'#FFFFFF',
					display:'none'
					});	
		obj.onclick = function(){
			_remove_D();
		}
		document.body.appendChild(obj);
		return objId;
	}catch(e){
		alert('_crtModalDiv : '+ e.name+'='+e.message);
	}
}

function _crtBackDiv(){//创建背景div
	try{
		var objId = 'div_background_';
		var obj = new Element('div',{id:objId});
		obj.setOpacity(0.5);
		obj.setStyle({
					zIndex:200,
					position:'absolute',
					background:'#000000',
					display:'none'
					});	

		document.body.appendChild(obj);
		return objId;
	}catch(e){
		alert('_crtBackDiv : '+ e.name+'='+e.message);
	}
}

function _crtContentDiv(){//创建内容div
	try{
		var objId = 'div_content_';
		var obj = new Element('div',{id:objId});
		obj.setStyle({
					zIndex:300,
					position:'absolute',
					background:'#FFFFFF',
					display:'none'
					});	

		document.body.appendChild(obj);
		return objId;
	}catch(e){
		alert('_crtContentDiv : '+ e.name+'='+e.message);
	}
}

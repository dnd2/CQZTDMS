var fullFDiv=null,fullFIframe=null,modalFDiv=null,backFDiv=null,contFDiv=null;
function OpenHtmlWindow(url,width,height){
	
	$(contFDiv).src = url;
	$(fullFDiv).setStyle("display","");
	$(fullFIframe).setStyle("display","");
	$(modalFDiv).setStyle("display","");
	$(backFDiv).setStyle("display","");
	$(contFDiv).setStyle("display","");	
	
	setSize(width, height);
}

function intiFullDiv() {
	if(fullFDiv == null) {
		fullFDiv = crtFullFDiv();
		fullFIframe = ctrFullFIframe();
		modalFDiv = crtModalFDiv();
		backFDiv = crtBackFDiv();
		contFDiv = crtContentFDiv();
	}
}

function setSize(width, height){
		var bwidth = 8;
		var ctop = (window.document.body.getSize().size.y-height)/3;
		var cleft= (window.document.body.getSize().size.x-width)/2;
		
		$(contFDiv).setStyles({width: width+'px', height: height+'px', left: cleft+'px', top: ctop+'px'});
		$(backFDiv).setStyles({width: (width+bwidth*2)+'px', height: (height+bwidth*2)+'px', left: (cleft-bwidth)+'px', top: (ctop-bwidth)+'px'});	
		$(modalFDiv).setStyles({width: window.getWidth()+'px', height: window.getHeight()+'px', left: '0px', top: '0px'});
}

function crtFullFDiv(){
		var tmp = 0;
		tmp = parentContainer.document.body.scrollHeight;
		var objId = 'fullDiv'
		var obj = new Element('div',{
			'id' : objId,
			'styles' : {
				'width' : '100%',
				'height' : ((tmp <= 570)?window.screen.availHeight:tmp) + 'px',
				'zIndex' : '100',
				'position' : 'absolute',
				'background' : '#000',
				'display' : 'none',
				'overflow' : 'auto',
				'left' : '0px',
				'top' : '0px',
				'opacity' : '0.4'
			},
			'events': {
				'click': fulldivRemove
			}
		});
		window.scroll(0,0);
		obj.inject(document.body);
		return objId;
}

function ctrFullFIframe(){
		var fObjId = 'divIframe';
		var fObj = new Element('div',{
				'id' : fObjId,
				'styles' : {
					'width' : '100%',
					'height' : '300px',
					'zIndex' : '101',
					'position' : 'absolute',
					'background' : '#000',
					'display' : 'none',
					'overflow' : 'auto',
					'left' : '0px',
					'top' : '0px',
					'opacity' : '0'
				}
			});
			fObj.inject(document.body);
			return fObjId;
}

function crtModalFDiv(){
		var objId = 'divModal';
		var obj = new Element('div',{
			'id' : objId,
			'styles' : {
				'zIndex' : '102',
				'position' : 'absolute',
				'background' : '#FFFFFF',
				'display' : 'none',
				'opacity' : '0'
			}
		});
		obj.inject(document.body);
		return objId;		
}

function fulldivRemove() {
	$(fullFDiv).setStyle("display","none");
	$(fullFIframe).setStyle("display","none");
	$(modalFDiv).setStyle("display","none");
	$(backFDiv).setStyle("display","none");
	$(contFDiv).setStyle("display","none");	
	$(contFDiv).src = path+"/loading.html";
}

function crtBackFDiv(){
		var objId = 'divBackground';
		var obj = new Element('div',{
			'id' : objId,
			'styles' : {
				'zIndex' : '200',
				'position' : 'absolute',
				'background' : '#000000',
				'display' : 'none',
				'opacity' : '0.5'
			}
		});
		obj.inject(document.body);
		return objId;
}

function crtContentFDiv(){
		var fObjId = 'myframe';
		var fObj = new Element('iframe',{
			'id' : fObjId,
			'name' : fObjId,
			'frameborder' : '0',
			'styles' : {
				'zIndex' : '300',
				'position' : 'absolute',
				'background' : '#000',
				'display' : 'none'
			}
		});
		fObj.inject(document.body);
		return fObjId;
}

function removeDiv(){
	parent.fulldivRemove();
}

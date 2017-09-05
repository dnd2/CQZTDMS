function JatoolsTooltip(){this.show=function(){if(!this._div){this._div=document.createElement('div')
this._div.style.display='none'
this._div.style.position='absolute'
document.body.appendChild(this._div)
}__div=event.srcElement.getElementsByTagName('div')[0]
this._div.className=__div.className
this._div.innerHTML=__div.innerHTML
this._div.style.left=document.body.scrollLeft+event.clientX
this._div.style.top=document.body.scrollTop+event.clientY
this._div.style.display='block'
}
this.hide=function(){this._div.style.display='none'
}}
var _jatools_tooltip=new JatoolsTooltip()

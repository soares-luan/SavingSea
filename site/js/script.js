function makeid()
{
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < 5; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
}
var poligonos = {};

var texto = '';
var data1 ='',data2='';
var filtro = '';
var selecionados = {};
var poluicao = 0;
var pesca = 0;

function limpar(){
	$("#selecionados").find("li").remove();
    $("#filtro").find('input#busca').val("");
    selecionados = {};
    texto = '';
    pesca = 0;
    poluicao = 0;
    $('input#data1').val();
    $('input#data2').val();
    $("#tipos li#poluicao i").html(poluicao);
    $("#tipos li#pesca i").html(pesca);
    data1 = '';
    data2 = '';
    filtro = '';
    poligonos= {};
    $(".btn-filtro a[type=button]").removeClass('active');
}

$("#buscaPesca button").on('click',function(){
	texto = $("#buscaPesca input").val();
	$(".panel-default").each(function(){
		if(filtro != ''){
			if($(this).find("li.status span").text() == filtro && $(this).attr('texto').indexOf(texto.toLowerCase()) != -1){
				$(this).show();
			}else{
				$(this).hide();
			}
		}else{
			if($(this).attr('texto').indexOf(texto.toLowerCase()) == -1){
				$(this).hide();
			}else{
				$(this).show();
			}	
		}
		
	});
})




$("#geraGrafico").on('click',function(){
	localStorage.selecionados = JSON.stringify(selecionados);
	location.href = 'graficos.html'
});

function geraPesca(){

var div = "";
	for(id in pescas){
			pesca = pescas[id];
			pesca.id = id;
		div += "<div class='panel panel-default' id='"+pesca.id+"' texto='"+pesca.nome.toLowerCase()+"'> ";
		div += "<div class='panel-heading role='tab' id='heading"+pesca.id+"'> ";
		div += "<h4 class='panel-title'> ";
		div += "<a href='#collapse"+pesca.id+"' role='button' data-toggle='collapse' data-parent='#accordion' aria-expanded='false' aria-controls='collapse"+pesca.id+"' class='collapsed'> ";
		div += "<ul class='list-inline'>";
		div += "<li class='name'>Nome: <span>"+pesca.nome+"</span></li>";
		div += "<li class='status'>Status: <span class='"+pesca.status+"'>"+pesca.status+"</span></li>";
		div += "<li class='motivo'>Motivo: <span>"+pesca.motivo+"</span></li>";
		div += "</ul>";
		div += "</a>";
		div += "</h4> ";
		div += "</div> ";
		div += "<div class='panel-collapse collapse' role='tabpanel' id='collapse"+pesca.id+"' aria-labelledby='heading"+pesca.id+"' aria-expanded='false'> ";
		div += "<div class='panel-body' id='map"+pesca.id+"' style='width:700px; height:500px;'></div>";
		div += "</div> ";
		div += "</div> ";
	};

$(".panel-group").append(div);

$(".panel-default").on('click',function(){
	var id = $(this).attr('id');
	setTimeout(function(){
		initMap(id);
	},1000);	
})

$(".btn-filtro a[type=button]").on('click',function(){
		if($(this).attr('filtro') == 'proibido'){
			$(".btn-filtro a[type=button][filtro=proibido]").addClass('active');
			$(".btn-filtro a[type=button][filtro=pendente]").removeClass('active');
			$(".btn-filtro a[type=button][filtro=liberado]").removeClass('active');
		}else if($(this).attr('filtro') == 'pendente'){
			$(".btn-filtro a[type=button][filtro=proibido]").removeClass('active');
			$(".btn-filtro a[type=button][filtro=pendente]").addClass('active');
			$(".btn-filtro a[type=button][filtro=liberado]").removeClass('active');
		}else if($(this).attr('filtro') == 'liberado'){
			$(".btn-filtro a[type=button][filtro=proibido]").removeClass('active');
			$(".btn-filtro a[type=button][filtro=pendente]").removeClass('active');
			$(".btn-filtro a[type=button][filtro=liberado]").addClass('active');
		}

		if(filtro == $(this).attr('filtro')){
			filtro = "";
			$(this).removeClass('active');
		}else{
			filtro = $(this).attr('filtro');
		}
		
		
		$(".panel-default").each(function(){

			if(texto != ""){
				if(filtro == ""){
					if($(this).attr('texto').indexOf(texto.toLowerCase()) != -1){
						$(this).show();	
					}else{
						$(this).hide();
					}
				}else{
					if($(this).find("li.status span").text() == filtro && $(this).attr('texto').indexOf(texto.toLowerCase()) != -1){
						$(this).show();	
					}else{
						$(this).hide();
					}
				}
				
			}else{
				if(filtro == "" || $(this).find("li.status span").text() == filtro){
					$(this).show();
				}else{
					$(this).hide();
				}
			}
		});
	});

}


function geraDenuncias(){

var div = "";
		for(id in denuncias){
			denuncia = denuncias[id];
			denuncia.id = id;
			var data = new Date(denuncia.data);
			data = (data.getDate().toString().length == 1 ? "0" : "")+data.getDate()+"/"+(data.getMonth().toString().length == 1 ? "0" : "")+(data.getMonth()+1)+"/"+data.getFullYear();
			div += "<div class='panel panel-default' id='"+denuncia.id+"' data='"+denuncia.data+"' texto='"+denuncia.descricao.toLowerCase()+"'> ";
			div += "<div class='panel-heading role='tab' id='heading"+denuncia.id+"'> ";
			div += "<h4 class='panel-title'> ";
			div += "<input type='checkbox' class='pull-left' value='"+denuncia.id+"'> ";
			div += "<a href='#collapse"+denuncia.id+"' role='button' data-toggle='collapse' data-parent='#accordion' aria-expanded='false' aria-controls='collapse"+denuncia.id+"' class='collapsed'> ";
			div += "<ul class='list-inline'>";
			div += "<li class='name'>Nome: <span>"+denuncia.descricao+"</span></li>";
			div += "<li class='motivo'>Tipo de denúncia: <span class="+denuncia.tipo+">"+denuncia.tipo+"</span></li>";
			div += "<li class='data'>Data: <span>"+data+"</span></li>";
			div += "</ul>";
			div += "</a>";
			div += "</h4> ";
			div += "</div> ";
			div += "<div class='panel-collapse collapse' role='tabpanel' id='collapse"+denuncia.id+"' aria-labelledby='heading"+denuncia.id+"' aria-expanded='false'> ";
			denuncia.imagens.forEach(function(imagem){
				div += "<img class='img-responsive img-thumbnail' src='img/"+imagem+"'/>";
			})
			

			div += "</div> ";
			div += "</div> ";
		}

	$(".panel-group").append(div);

	$(".panel-default input[type=checkbox]").on('change',function(){
		var id = $(this).val();
		var data = new Date(denuncias[id].data);
		data = (data.getDate().toString().length == 1 ? "0" : "")+data.getDate()+"/"+(data.getMonth().toString().length == 1 ? "0" : "")+(data.getMonth()+1)+"/"+data.getFullYear();
		if($(this).is(":checked")){
			$("#selecionados").append("<li id="+id+"><a href='javascript:;'><i class='fa fa-trash'></i></a>"+denuncias[id].descricao.replace(id,'')+" - Data: "+data+"</li>");
			atualizaContador();
			selecionados[id] = true;
			$("#selecionados li a").on('click',function(){
				var id = $(this).parent().attr('id');
				$("#selecionados li[id="+id+"]").remove();
				$(".panel-default#"+id+" input[type=checkbox]").attr('checked',false);
				atualizaContador();
				delete selecionados[id];
			});

		}else{
			$("#selecionados li[id="+id+"]").remove();
			atualizaContador();
			delete selecionados[id];
		}
	});


	$(".btn-filtro a[type=button]").on('click',function(){
		if($(this).attr('filtro') == 'pesca'){
			$(".btn-filtro a[type=button][filtro=pesca]").addClass('active');
			$(".btn-filtro a[type=button][filtro=poluicao]").removeClass('active');
		}else{
			$(".btn-filtro a[type=button][filtro=poluicao]").addClass('active');
			$(".btn-filtro a[type=button][filtro=pesca]").removeClass('active');
		}

		if(filtro == $(this).attr('filtro')){
			filtro = "";
			$(this).removeClass('active');
		}else{
			filtro = $(this).attr('filtro');
		}
		
		
		$(".panel-default").each(function(){

			if(filtro == "" || $(this).find("li.motivo span").text() == filtro){
				$(this).show();
			}else{
				$(this).hide();
			}
		});
	})

	function atualizaContador(){
		var qtdPolui = 0;
		var qtdPesca = 0;
		$("#selecionados li").each(function(){
		  var tipo = denuncias[$(this).attr('id')].tipo;
		  if(tipo == 'poluicao'){
            	qtdPolui ++;
	        }else{
	            qtdPesca ++;
	        }
		})
		$("#tipos li#pesca i").html(qtdPesca);
		$("#tipos li#poluicao i").html(qtdPolui);
	}

}


$(function(){
	$(window).load(function(){
		setTimeout(function(){
			$('.panelContent').addClass('on');
		},600);
	});  
	
	$('.login').click(function(){
		$('.loadingWrap').slideDown();
		setTimeout(function(){
			$('.loadingWrap p i').attr('class','fa fa-times');
			$('.loadingWrap p span').html('<b>Giriş Başarısız</b> <p><small><a class="repeat" href="#">Tekrar Deneyin</a></small></p>');
		},2000);
	});
	$(document).on('click','.repeat',function(){
		$('.loadingWrap').slideUp(); 
		setTimeout(function(){
			$('.loadingWrap p i').attr('class','fa fa-cog fa-spin'); 
			$('.loadingWrap p span').html('İşlem Yapılıyor Lütfen Bekleyin');
		},1000);
	});
});
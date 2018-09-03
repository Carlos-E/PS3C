$(document).ready(function() {
		
		$('#myForm').submit(function(e) {
						
			e.preventDefault();

			$('#ModalButton').hide();

			let url = $(this).attr('action');
			let data = $(this).serializeArray();
			
			$.ajax({
				url : url,
				data : data,
				type : "POST",
				dataType : "json",
			}).done(function(data, statusText, xhr) {

				$('#ModalTitle').html('Operaci&oacute;n exitosa');
				$('#ModalBody').html('Operaci&oacute;n completada');
				
			}).fail(function(xhr, statusText) {

				$('#ModalTitle').html('C&oacute;digo de Estado: ' + xhr.status);
				$('#ModalBody').html('Oopss a ocurrido un error');
				
			}).always(function(xhr, statusText,a,b) {
								
				if(a!=''){
					xhr = a;
				}
				
				console.log(JSON.stringify(xhr, null, 2));
				console.log(JSON.stringify(statusText, null, 2));
				console.log(JSON.stringify(a, null, 2));
				console.log(JSON.stringify(b, null, 2));

				if (typeof xhr.responseJSON != 'undefined') {
					if (typeof xhr.responseJSON.title != 'undefined') {
						$('#ModalTitle').html(xhr.responseJSON.title);
					}
					if (typeof xhr.responseJSON.message != 'undefined') {
						$('#ModalBody').html(xhr.responseJSON.message);
					}
					if(typeof xhr.responseJSON.sendRedirect != 'undefined'){
						window.location.replace(xhr.responseJSON.sendRedirect);
					}
				}
				
				$('#Modal').modal();
				
				if(typeof scanFunction != 'undefined'){
					scanFunction(table);
				}
			});

		});
		
	});
$('.group_submit').click(function() {
    $.post('groups/add', {url: $('.group_input').val()}, function(resp) {
        window.location = "/vote?group_id=" + resp;
    });
});

$('.cross').click(function() {
	var password = prompt("Please enter password");
	$.post('groups/delere', {password:password}, function(resp) {
		window.location = "/";
	});
})
$('.group_submit').click(function() {
    $.post('groups/add', {url: $('.group_input').val()}, function(resp) {
        window.location = "/vote?group_id=" + resp;
    });
});
$(function () {
    $('.btn-danger').click(function(){
        var self = $(this);
        var url = self.data().url;
        $.ajax({
            method: 'DELETE',
            url: url,
            success: function() {
                self.parents('li').remove();
            },
            error: function() {
                console.log('error');
            }
        });
    });
});
// submit時ローディング画面
$(document).ready(function() {
    var $submitBtn = $('button[type="submit"]');
    $submitBtn.on('click', function() {
        setTimeout(function() {
            $("#overlay").css("display", "block");
        }, 50);
    });
});
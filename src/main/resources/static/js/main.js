$(document).ready(function () {

    $("#bth-search").click(function (event) {

        fire_ajax_submit();

    });

});

function fire_ajax_submit() {


    $("#btn-search").prop("disabled", true);

    $.ajax({
        type: "GET",
        url: "/api/works",
        success: function (data) {
            var json = JSON.stringify(data);
            $('#feedback').html(json);
            $("#btn-search").prop("disabled", false);
            console.log("SUCCESS : ", data);

        },
        error: function (e) {

            var msg = "error";
            $('#feedback').html(msg);
            $("#btn-search").prop("disabled", false);
            console.log("ERROR : ", e);

        }
    });

}
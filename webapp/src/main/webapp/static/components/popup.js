$(document).ready(function () {
    $('#pop').click(function () {

        callAjax();
        var interval= setInterval(function(){
            callAjax();
        },6*1000);
        $('#closePopUp').click(function(){
            console.log("Closing popup!!!!");
            clearInterval(interval);
        });
    });



    function popup(intervalId) {
        var marginX = 450;
        var marginY = 250;
        var width = $(window).width();
        var height = $(window).height();
        var x =Math.floor( Math.random()*width);
        var y =Math.floor( Math.random()*height);
        if(x+marginX>width){
            x=width-(marginX*2);
        }
        if(x-marginX<0){
            x=100;
        }
        if(y+marginY>height){
            y=height-(marginY*2);
        }
        if(y-marginY<0){
            y=100;
        }
        $('#popup').bPopup({
            autoClose: 5*1000, //Auto closes after 1000ms/1sec
            follow: [false, false],
            position: [x,y]

        });
    };

    function callAjax(){
        console.log("calling ajax")
        $.ajax({
            type: 'GET',
            dataType: 'text',
            url:"/twitter/home/getLastTweet",
            success:function(result){
                console.log("result");
                popup();
                document.getElementById("popupText").innerHTML = result+"";
            },
            failure:function(){
                console.log("nu vrea :(");
            }
        });

    }
});



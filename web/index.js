$(document).ready(function() {

    window.location.hash = '#startingPoint';

    $("#menu_plugin").gabrielmenubar({
        fontColor: "#91867C",
        textWidth: "200px",
        fontOnHover: "helvetica",
        fontColorOnHover: "#FFFFFF",
        background: "#222222",
        backgroundOnHover: "#91867C",
        distanceToTop: "0px",
        menuWidth: "18px",
        currentSelectedElementFont: "sans-serif",
        currentSelectedElementBackground: "#f1f1f1",
        spaceBetweenElements: "10px",
        elementCellWidth: "20px",
    });


    $("#slider").bxSlider({
        auto: true,
        autoControls: true,
        captions: true,
        minSlides: 3,
        maxSlides: 3,
        slideWidth: 260,
        slideMargin: 10,
        infiniteLoop: false
    });

    $(window).scroll(function() {
        $(".slideanim").each(function() {
            var pos = $(this).offset().top;

            var winTop = $(window).scrollTop();
            if (pos < winTop + 600) {
                $(this).addClass("slide");
            }
        });
    });

    $("#iPicture").iPicture({
        animation: true,
        animationBg: "bgblack",
        animationType: "ltr-slide",
        pictures: ["picture1"],
        button: "moreblack",
        moreInfos: {
            "picture1": [{
                "id": "tooltip1",
                "descr": "Name your event",
                "top": "20px",
                "left": "70px"
            }, {
                "id": "tooltip2",
                "descr": "Give specific info",
                "top": "100px",
                "left": "50px"
            }, {
                "id": "tooltip3",
                "descr": "Add sponsors",
                "top": "430px",
                "left": "200px"
            }, {
                "id": "tooltip4",
                "descr": "Define ticket price",
                "top": "240px",
                "left": "1140px"
            }, {
                "id": "tooltip5",
                "descr": "Add sectors and more",
                "top": "580px",
                "left": "350px"
            }]
        }
    });
});

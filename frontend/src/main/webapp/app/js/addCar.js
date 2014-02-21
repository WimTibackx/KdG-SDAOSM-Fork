/**
 * Created by peter on 19/02/14.
 */
function getFileData() {
    // var fr = new FileReader();
    var file = document.getElementById('image').files[0];
    /*var result = document.getElementById('imgResult');
     fr.onload = function() {
     var xhr = new XMLHttpRequest();
     xhr.open("POST", "http://example.com");
     xhr.send(fr.result);
     }
     fr.readAsDataURL(file); */
    var data = new FormData();
    data.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://example.com");
    xhr.send(data);
}
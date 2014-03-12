/**
 * Created by Thierry on 6/03/14.
 */
// CONTROLLER: Inbox
carpoolingApp.controllerProvider.register('inboxCtrl', ['$scope', '$http', '$location', 'SharedProperties', function ($scope, $http, $location, SharedProperties) {
    deleteActiveClass();
    $('#InboxTab').addClass('active');

    var detailsEnabled = true;
    var formPopupEnabled = false;

    $scope.begin = 0;

    $scope.end = 8;


    $scope.type = "Inbox";
    $scope.hideSend = true;
    $scope.hideMessage = true;
    $scope.hideRecieve = false;
    $scope.recieverName = "";
    $scope.subject = "";



    loadMessages('recieve');


    $scope.nextPage = function(){
        if($scope.type == 'Inbox'){
            if ($scope.end < $scope.allRecievedMessages.length){
                $scope.begin = $scope.begin + 9;
                $scope.end = $scope.end + 9;
                selectString($scope, 'recieve')
            }
        }else{
            if ($scope.end < $scope.allSendMessages.length){
                $scope.begin = $scope.begin + 9;
                $scope.end = $scope.end + 9;
                selectString($scope, 'send')
            }
        }

    }

    $scope.previousPage = function(){
        if($scope.type == 'Inbox'){

            if ($scope.begin > 0){
                $scope.begin = $scope.begin - 9;
                $scope.end = $scope.end - 9;
                selectString($scope, 'recieve')
            }
        }else{

            if ($scope.begin > 0){
                $scope.begin = $scope.begin - 9;
                $scope.end = $scope.end - 9;
                selectString($scope, 'send')
            }
        }
    }

    $scope.replyMessage = function(message){
        detailsEnabled = false;
        //ng-Show, ng-Hide won't work, it keeps the ng-hide propery active on the overlay
        $('#overlay').show();
        $scope.recieverName = message.senderUsername;
        $scope.sender = message.receiverUsername;
        $scope.subject = "RE:" + message.messageSubject;


    }

    $scope.openDetails = function(message, type){
        if (detailsEnabled) {
            var data = {messageId :message.id}
            if (!message.read && type == 'recieve' ){
                $http({
                    method: 'POST',
                    data: JSON.stringify(data),
                    headers: {'Content-Type': "text/plain; charset=utf-8"},
                    url: rootUrl + "/authorized/textmessage/read"
                })
            }
            $scope.subject = message.messageSubject;
            $scope.messageBody = message.messageBody;
            $scope.sender = message.senderUsername;
            $scope.receiver = message.receiverUsername;
            $scope.hideMessage = false;
            $scope.hideSend = true;
            $scope.hideRecieve = true;
        }

    }


    $scope.tabInboxClick = function(){
        detailsEnabled = true;
        $scope.type = "Inbox";
        $scope.hideSend = true;
        $scope.hideRecieve = false;
        $scope.hideMessage = true;
        $scope.begin = 0;
        $scope.end = 8;
        loadMessages('recieve');
    }

    $scope.tabSendClick = function(){
        detailsEnabled = true;
        $scope.type = "Verzonden berichten";
        $scope.hideSend = false;
        $scope.hideRecieve = true;
        $scope.hideMessage = true;
        $scope.begin = 0;
        $scope.end = 8;
        loadMessages('send');
    }

    $scope.inputStyle = function(str) {
        var charWidth = 7;
        return  {"width": (str.length +1) * charWidth + "px" };
    }

    function selectString($scope, type){

        if (type == 'recieve'){
            $scope.recievedMessages = [];
            var counter = 0;
            var i = $scope.begin;
            while(i <=$scope.end && $scope.allRecievedMessages[i] != undefined){
                $scope.recievedMessages.push($scope.allRecievedMessages[i])
                if(!$scope.allRecievedMessages[i].read) {
                    $scope.recievedMessages[counter].readSrc = '../app/img/unread.png';
                }else{
                    $scope.recievedMessages[counter].readSrc = '../app/img/transparent.png';
                }
                counter++;
                i++;
            }
        }else if (type == 'send'){
            console.log($scope.allSendMessages);
            $scope.sendMessages = [];
            var i = $scope.begin;
            var counter = 0;
            while(i <=$scope.end && $scope.allSendMessages[i] != undefined){
                $scope.sendMessages.push($scope.allSendMessages[i])
                if(!$scope.allSendMessages[i].read) {
                    $scope.sendMessages[counter].readSrc = '../app/img/unread.png';
                }else{
                    $scope.sendMessages[counter].readSrc = '../app/img/transparent.png';
                }

                i++;
                counter++;
            }
            console.log($scope.sendMessages)
        }

    }

    function loadMessages(type){
        $http({
            method: 'GET',
            url: rootUrl + "/authorized/textmessage/get"
        }).success(function (response) {
                obj = response;
                if (obj.hasOwnProperty("error")) {
                    if (obj["error"] == "AuthorizationNeeded") {
                        $location.path("/login");
                        SharedProperties.setProperty("Authorization is required")

                    }
                } else {
                    $scope.allRecievedMessages = obj["receivedMessages"]
                    $scope.allSendMessages = obj["sentMessages"]
                    selectString($scope, type)

                }
            });
    }

    $scope.clickOverlay = function(){
        if (formPopupEnabled){
            formPopupEnabled = false;
        }else{
            //ng-Show, ng-Hide won't work, it keeps the ng-hide propery active on the overlay
            $('#overlay').hide();
        }
    }

    $scope.clickFormpopup = function(){
        formPopupEnabled = true;
    }

    $scope.sendMessageToUser = function(){
        var data = {
            senderUsername : $scope.sender,
            receiverUsername : $scope.recieverName,
            messageBody : $scope.messageBody,
            messageSubject : $scope.subject
        }
        $http({
            method: 'POST',
            data: JSON.stringify(data),
            headers: {'Content-Type': "text/plain; charset=utf-8"},
            url: rootUrl + "/authorized/textmessage/send"
        }).success(function (response) {
                $('#overlay').hide();
            });


    }
}]);
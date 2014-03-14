/**
 * Created by Thierry on 6/03/14.
 */
// CONTROLLER: Inbox
var currentMessage;
var selectedMessage = 4;
var finished = false;
var page = 1;

var clickedMessage = false;
var clickedOverlay = false;

var tabClicked = "recieve";

carpoolingApp.controllerProvider.register('inboxCtrl', ['$scope', '$http', '$location', 'SharedProperties', function ($scope, $http, $location, SharedProperties) {
    deleteActiveClass();
    $('#InboxTab').addClass('active');

    var defaultActive = function () {
        $('#tabInbox').removeClass('activeTab');
        $('#tabSend').removeClass('activeTab');
    }

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

    $scope.page = page;

    $scope.backIcon = "../app/img/backDisabled.png";
    $scope.nextIcon = "../app/img/next.png";

    $scope.allRecievedMessages = [];
    $scope.allSendMessages = []

    var beginIcons = function () {
        console.log("CHeck")
        console.log(tabClicked);
        if (tabClicked == "recieve") {

            $scope.backIcon = "../app/img/backDisabled.png";

            if (((page * 9) >= $scope.allRecievedMessages.length)) {
                console.log("wroonf if");
                $scope.nextIcon = "../app/img/nextDisabled.png";
            } else {
                $scope.nextIcon = "../app/img/next.png";
            }
        } else {
            if (((page * 9) >= $scope.allSendMessages.length)) {
                $scope.nextIcon = "../app/img/nextDisabled.png";
            } else {
                $scope.nextIcon = "../app/img/next.png";
            }
        }
    }
    var overPageOne = function () {
        if (page > 1) {
            $scope.backIcon = "../app/img/back.png";
        }
    }
    var isPageOne = function () {
        beginIcons();
        if (page == 1) {
            $scope.backIcon = "../app/img/backDisabled.png";
        }
    }

    $scope.nextPage = function () {
        if ($scope.type == 'Inbox') {
            if ($scope.end < ($scope.allRecievedMessages.length) - 1) {
                $scope.begin = $scope.begin + 9;
                $scope.end = $scope.end + 9;
                selectString($scope, 'recieve')
                page++;
                $scope.page = page;
                $scope.backIcon = "../app/img/back.png";
                /*if ((page * 9) >= $scope.allRecievedMessages.length) {
                 $scope.nextIcon = "../app/img/nextDisabled.png";
                 } */
                beginIcons();
                overPageOne();
            }
        } else {
            if ($scope.end < ($scope.allSendMessages.length) - 1) {
                $scope.begin = $scope.begin + 9;
                $scope.end = $scope.end + 9;
                selectString($scope, 'send')
                page++;
                $scope.page = page;
                beginIcons();
                overPageOne();
            }
        }

    }

    $scope.previousPage = function () {
        if ($scope.type == 'Inbox') {
            if ($scope.begin > 0) {
                $scope.begin = $scope.begin - 9;
                $scope.end = $scope.end - 9;
                selectString($scope, 'recieve')
                page--;
                $scope.page = page;
                isPageOne();
            }
            if ($scope.allSendMessages.length > 9) {
                $scope.nextIcon = "../app/img/next.png";
            }
        } else {
            if ($scope.begin > 0) {
                $scope.begin = $scope.begin - 9;
                $scope.end = $scope.end - 9;
                selectString($scope, 'send')
                page--;
                $scope.page = page;
                isPageOne();
                if ($scope.allRecievedMessages.length > 9) {
                    $scope.nextIcon = "../app/img/next.png";
                }
            }
        }
    }

    $scope.replyMessage = function (message) {

        clickedOverlay = true;

        detailsEnabled = false;
        //ng-Show, ng-Hide won't work, it keeps the ng-hide propery active on the overlay
        $('#overlay').show();

        $scope.mBody = "";
        $scope.recieverName = message.senderUsername;
        $scope.sender = message.receiverUsername;
        $scope.subject = "RE:" + message.messageSubject;
    }

    $scope.openDetails = function (message, type) {

        if (!clickedOverlay) {

            $scope.hideMessage = false;

            clickedMessage = true;

            selectedMessage = message.id;

            if (type == 'recieve') {
                $('.hideReceiver').hide();
                $('.hideSender').show();
            } else {
                $('.hideReceiver').show();
                $('.hideSender').hide();
            }
            if (detailsEnabled) {

                currentMessage = message;
                var data = {messageId: message.id}
                if (!message.read && type == 'recieve') {
                    $http({
                        method: 'POST',
                        data: JSON.stringify(data),
                        headers: {'Content-Type': "text/plain; charset=utf-8"},
                        url: rootUrl + "/authorized/textmessage/read"
                    })
                }
                $scope.subjectO = message.messageSubject;
                $scope.messageBody = message.messageBody;
                $scope.sender = message.senderUsername;
                $scope.receiver = message.receiverUsername;
                $scope.hideMessage = false;

                //$scope.hideSend = true;

                /*changes from here
                 $scope.hideRecieve = true;
                 */

                loadMessages(type);


                //$scope.hidePagingIcons = true;
                //$scope.recieveMessage.readSrc = '../app/img/check.png';
            }
        } else {
            clickedOverlay = false;
        }
    }

    $scope.tabInboxClick = function () {
        tabClicked = "recieve";
        defaultActive();
        $('#tabInbox').addClass('activeTab');

        page = 1;
        $scope.page = page;



        detailsEnabled = true;
        $scope.type = "Inbox";
        $scope.hideSend = true;
        $scope.hideRecieve = false;
        $scope.hideMessage = true;
        $scope.begin = 0;
        $scope.end = 8;

        //
        //$scope.hidePagingIcons = false;

        loadMessages('recieve');
        beginIcons();
    }

    $scope.tabSendClick = function () {
        defaultActive();
        $('#tabSend').addClass('activeTab');

        tabClicked = "send";

        page = 1;
        $scope.page = page;



        detailsEnabled = true;
        $scope.type = "Verzonden berichten";
        $scope.hideSend = false;
        $scope.hideRecieve = true;
        $scope.hideMessage = true;
        $scope.begin = 0;
        $scope.end = 8;
        loadMessages('send');
        beginIcons();
    }

    $scope.inputStyle = function (str) {
        var charWidth = 7;
        return  {"width": (str.length + 1) * charWidth + "px" };
    }

    function selectString($scope, type) {

        if (type == 'recieve') {
            $scope.recievedMessages = [];
            var counter = 0;
            var i = $scope.begin;
            while (i <= $scope.end && $scope.allRecievedMessages[i] != undefined) {
                $scope.recievedMessages.push($scope.allRecievedMessages[i])
                if (!$scope.allRecievedMessages[i].read) {
                    $scope.recievedMessages[counter].readSrc = '../app/img/unread.png';
                } else {
                    $scope.recievedMessages[counter].readSrc = '../app/img/check.png';
                }
                counter++;
                i++;
            }

        } else if (type == 'send') {
            $scope.sendMessages = [];
            var i = $scope.begin;
            var counter = 0;
            while (i <= $scope.end && $scope.allSendMessages[i] != undefined) {
                $scope.sendMessages.push($scope.allSendMessages[i])
                if (!$scope.allSendMessages[i].read) {
                    $scope.sendMessages[counter].readSrc = '../app/img/unread.png';
                } else {
                    $scope.sendMessages[counter].readSrc = '../app/img/check.png';
                }

                i++;
                counter++;
            }
        }
    }

    function loadMessages(type) {

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
                    console.log($scope.allSendMessages);
                    console.log($scope.allRecievedMessages);
                    $scope.noMessagesToShow = false;
                    if (type == 'recieve') {
                        if (obj["receivedMessages"].length == 0) {
                            $scope.noMessagesToShow = true;
                        }
                    } else {
                        if (obj["sentMessages"].length == 0) {
                            $scope.noMessagesToShow = true;
                        }
                    }

                    selectString($scope, type)
                }
            });
    }

    $scope.clickOverlay = function () {

        if (formPopupEnabled) {
            formPopupEnabled = false;
        } else {
            //ng-Show, ng-Hide won't work, it keeps the ng-hide propery active on the overlay
            $('#overlay').hide();
        }

        detailsEnabled = true;
    }

    $scope.clickFormpopup = function () {
        formPopupEnabled = true;
    }

    $scope.sendMessageToUser = function () {
        var data = {
            senderUsername: $scope.sender,
            receiverUsername: $scope.recieverName,
            messageBody: $scope.mBody,
            messageSubject: $scope.subject
        }
        $http({
            method: 'POST',
            data: JSON.stringify(data),
            headers: {'Content-Type': "text/plain; charset=utf-8"},
            url: rootUrl + "/authorized/textmessage/send"
        }).success(function (response) {
                $('#overlay').hide();
                $scope.tabInboxClick();
                $scope.tabSendClick();
            });
    }

    $scope.sendNewMessage = function () {

        detailsEnabled = false;
        //ng-Show, ng-Hide won't work, it keeps the ng-hide propery active on the overlay
        $('#overlay').show();

        $http({
            method: 'GET',
            url: rootUrl + "/authorized/myprofile/",
            headers: {'Content-Type': "text/plain; charset=utf-8"}
        }).success(function (response) {
                obj = response;

                $scope.mBody = "";
                $scope.recieverName = "Ontvanger";
                $scope.sender = obj["username"];
                $scope.subject = "Onderwerp";

                $('#recieverInputField').focus(function () {
                    if ($scope.recieverName == "Ontvanger") {
                        $scope.recieverName = "";
                    }
                })
                $('#subjectInputField').focus(function () {
                    if ($scope.subject == "Onderwerp") {
                        $scope.subject = "";
                    }
                })
            });
    }

    $scope.tabInboxClick();
}]);
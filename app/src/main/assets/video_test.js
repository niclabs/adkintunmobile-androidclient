<html><body><div id="player"></div>
    <script type="text/javascript">

        var qualities = [];//['tiny', 'small', 'medium', 'large', 'hd720'];
        getQualities();

        var tag = document.createElement('script');
        tag.src = 'https://www.youtube.com/iframe_api';
        var firstScriptTag = document.getElementsByTagName('script')[0];
        firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
        var player;
        var lastState = -1;
        var i = 0;
        var timesBuffering = 0;
        var lastTime;
        var timeout;

        function getQualities(){
            var options = ['tiny', 'small', 'medium', 'large', 'hd720'];
            for (var i=0; i < options.length; i++) {
                if (window.JSInterface.getQuality(options[i]))
                    qualities.push(options[i]);
            }
            if (qualities.length == 0){
                window.JSInterface.noneSelectedQuality();
                throw new Error("Something went badly wrong!");
            }
        }

        function onYouTubeIframeAPIReady() {
            player = new YT.Player('player', {
                height: '100%',
                width: '100%',
                playerVars: {
                    autoplay: 1,
                    controls: 0,
                    showinfo: 0,
                    rel: 0,
                    iv_load_policy: 3,
                },
                events: {
                    'onReady': onPlayerReady,
                    'onStateChange': onPlayerStateChange,
                    'onPlaybackQualityChange': onPlayerPlaybackQualityChange,
                    'onError': onPlayerError
                }
            });
        }

        function testEcho(message) {
            window.JSInterface.doEchoTest(message);
        }

        function onPlayerReady(event) {
            window.JSInterface.startCountingBytes();
            timeout = setTimeout(playNextVideo, 15000);
            player.loadVideoById({'videoId': 'gPmbH8eCUj4',
                //'startSeconds': 360,
                //'endSeconds': 97,
                'suggestedQuality': qualities[i]});
            player.mute();
        }

        function playNextVideo(){
            clearTimeout(timeout);
            var loadedFraction = player.getVideoLoadedFraction()
            window.JSInterface.onVideoEnded(qualities[i], timesBuffering, loadedFraction);
            i = i + 1;
            window.JSInterface.startCountingBytes();
            timeout = setTimeout(playNextVideo, 15000);
            player.loadVideoById({'videoId': 'gPmbH8eCUj4',
                //'startSeconds': 360,
                //'endSeconds': 97,
                'suggestedQuality': qualities[i]});
        }
        function onPlayerStateChange(event){
            testEcho(event.data);
            if (event.data == YT.PlayerState.ENDED ) {//&& i < qualities.length - 1) {
                if (lastState == YT.PlayerState.PAUSED){
                    playNextVideo();
                }
            }
            else if (event.data == YT.PlayerState.UNSTARTED){
                timesBuffering = 0;
            }

            else if (event.data == YT.PlayerState.BUFFERING){
                lastTime = Date.now();
            }
            else if (lastState == YT.PlayerState.BUFFERING){
                timesBuffering += Date.now() - lastTime
            }

            lastState = event.data;
        }

        function onPlayerPlaybackQualityChange(event){
            if (i>0 && event.data == qualities[i-1]){
                player.stopVideo();
                window.JSInterface.onVideoTestFinish();
                return;
            }
            window.JSInterface.makeToast(event.data);
            testEcho(event.data);
        }

        function onPlayerError(event){
            player.stopVideo();
            window.JSInterface.onVideoTestFinish();
        }
    </script>
</body></html>
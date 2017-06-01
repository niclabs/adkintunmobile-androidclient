<html><body><div id="player"></div>
    <script type="text/javascript">

        var qualities = ['hd720'];

        var tag = document.createElement('script');
        tag.src = 'https://www.youtube.com/iframe_api';
        var firstScriptTag = document.getElementsByTagName('script')[0];
        firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
        var player;
        var i = 0;
        var videoId =  window.JSInterface.getVideoId();


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
                    'onPlaybackQualityChange': onPlayerPlaybackQualityChange,
                    'onError': onPlayerError
                }
            });
        }

        function onPlayerReady(event) {
            player.loadVideoById({'videoId': videoId,
                //'startSeconds': 360,
                //'endSeconds': 97,
                'suggestedQuality': qualities[i]});
            player.mute();
        }

        function playNextVideo(){
            var loadedFraction = player.getVideoLoadedFraction()
            i = i + 1;
            player.loadVideoById({'videoId': videoId,
                //'startSeconds': 360,
                //'endSeconds': 97,
                'suggestedQuality': qualities[i]});
        }

        function onPlayerPlaybackQualityChange(event){
            if (i>0 && event.data == qualities[i-1]){
                player.stopVideo();
                return;
            }
            window.JSInterface.setMaxQuality(event.data);
            player.stopVideo();
        }

        function onPlayerError(event){
            player.stopVideo();
        }
    </script>
</body></html>
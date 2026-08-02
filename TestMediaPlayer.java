import android.media.MediaPlayer;
import android.content.Context;
public class TestMediaPlayer {
    public void test(Context context) {
        MediaPlayer mp = MediaPlayer.create(context, android.net.Uri.parse(""));
    }
}

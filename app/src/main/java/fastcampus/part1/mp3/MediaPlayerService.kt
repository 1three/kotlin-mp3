package fastcampus.part1.mp3

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MediaPlayerService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    // no bind, yes foreground ∴ return null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    // 일반(백그라운드) Service 상태 → 포그라운드 Service 변경 필요
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            MEDIA_PLAYER_PLAY -> {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(baseContext, R.raw.sugarsweet).apply {
                        isLooping = true
                    }
                }
                mediaPlayer?.start()
            }

            MEDIA_PLAYER_STOP -> {
                mediaPlayer?.stop()
                mediaPlayer?.release() // memory 해제
                mediaPlayer = null
                stopSelf() // Service 종료 (명시적 종료 중요)
            }

            MEDIA_PLAYER_PAUSE -> {
                mediaPlayer?.pause()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }
}
package fastcampus.part1.mp3

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Icon
import android.media.MediaPlayer
import android.os.IBinder

class MediaPlayerService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private val receiver = LowBatteryReceiver()

    // 바인드가 아닌 포그라운드 ∴ return null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        // 1) notification 채널 생성
        createNotificationChannel()

        initReceiver()

        // Icon
        val playIcon = Icon.createWithResource(baseContext, R.drawable.round_play_arrow_24)
        val stopIcon = Icon.createWithResource(baseContext, R.drawable.round_stop_24)
        val pauseIcon = Icon.createWithResource(baseContext, R.drawable.round_pause_24)

        // PendingIntent
        val mainPendingIntent = PendingIntent.getActivity(
            baseContext,
            0,
            Intent(baseContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_IMMUTABLE
        )
        val playPendingIntent = PendingIntent.getService(
            baseContext,
            0,
            Intent(baseContext, MediaPlayerService::class.java).apply {
                action = MEDIA_PLAYER_PLAY
            },
            PendingIntent.FLAG_IMMUTABLE
        )
        val stopPendingIntent = PendingIntent.getService(
            baseContext,
            0,
            Intent(baseContext, MediaPlayerService::class.java).apply {
                action = MEDIA_PLAYER_STOP
            },
            PendingIntent.FLAG_IMMUTABLE
        )
        val pausePendingIntent = PendingIntent.getService(
            baseContext,
            0,
            Intent(baseContext, MediaPlayerService::class.java).apply {
                action = MEDIA_PLAYER_PAUSE
            },
            PendingIntent.FLAG_IMMUTABLE
        )

        /**
         * FLAG_ACTIVITY_SINGLE_TOP
         *
         * 액티비티 스택의 Top에 호출하려는 액티비티가 존재할 경우,
         * 다시 호출하지 않고 기존 액티비티를 재사용
         * */

        // 2) notification 추가 (Builder 생성)
        val notification = Notification.Builder(baseContext, CHANNEL_ID)
            .setStyle(
                Notification.MediaStyle().setShowActionsInCompactView(0, 1, 2)
            )
            .setVisibility(Notification.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.round_audiotrack_24)
            .addAction(
                Notification.Action.Builder(
                    stopIcon, // 아이콘
                    "Stop", // Title
                    stopPendingIntent // PendingIntent
                ).build()
            )
            .addAction(
                Notification.Action.Builder(
                    playIcon,
                    "Play",
                    playPendingIntent
                ).build()
            )
            .addAction(
                Notification.Action.Builder(
                    pauseIcon,
                    "Pause",
                    pausePendingIntent
                ).build()
            )
            .setContentIntent(mainPendingIntent) // content 클릭 시, MainActivity 실행
            .setContentTitle("Music Play")
            .setContentText("playing music...")
            .setOngoing(true)
            .build()

        startForeground(100, notification)
    }

    private fun initReceiver() {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_LOW)
        }
        // 1) Receiver 등록
        registerReceiver(receiver, filter)
    }

    private fun createNotificationChannel() {
        val channel =
            NotificationChannel(CHANNEL_ID, "MEDIA_PLAYER", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = baseContext.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

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

    override fun onDestroy() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        unregisterReceiver(receiver) // 2) Receiver 해제
        super.onDestroy()
    }
}
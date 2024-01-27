package fastcampus.part1.mp3

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fastcampus.part1.mp3.databinding.ActivityMainBinding

/**
 * 음악 플레이어 앱
 *
 * 1. 백그라운드 기능 실행
 * 2. 음원 재생
 * 3. 디바이스 이벤트 캐치 (네트워크 상태 변경, 전원 연결, 배터리 사용량 확인)
 * */

/**
 * MediaPlayer
 * Service
 * Notification - PendingIntent, Intent flag
 * BroadCastReceiver - LOW_BATTERY
 * */

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playButton.setOnClickListener {
            mediaPlay()
        }

        binding.stopButton.setOnClickListener {
            mediaStop()
        }

        binding.pauseButton.setOnClickListener {
            mediaPause()
        }
    }

    private fun mediaPlay() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.status).apply {
                isLooping = true // 반복 재생 사용
            }
        }
        mediaPlayer?.start()
    }

    private fun mediaStop() {
        mediaPlayer?.stop()
        mediaPlayer?.release() // memory 해제
        mediaPlayer = null
    }

    private fun mediaPause() {
        mediaPlayer?.pause()
    }
}
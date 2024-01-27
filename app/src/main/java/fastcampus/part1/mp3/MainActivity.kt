package fastcampus.part1.mp3

import android.content.Intent
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

/**
 * Service
 *
 * 1. 백그라운드에서 실행되는 컴포넌트 (no UI)
 * 2. 상호작용 없는 동안, 계속 실행되어야 하는 작업 처리
 * 3. 종류
 *    사용자에 보이는 포그라운드
 *    사용자가 알지 못하는 백그라운드
 *    다른 앱 구성요소와 상호작용하는 바인드
 * */

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
        // Service
        val intent = Intent(this, MediaPlayerService::class.java).apply {
            action = MEDIA_PLAYER_PLAY
        }
        startService(intent) // Service 시작
    }

    private fun mediaStop() {
        val intent = Intent(this, MediaPlayerService::class.java).apply {
            action = MEDIA_PLAYER_STOP
        }
        startService(intent) // Service 정지 (새로운 Service 시작 X)
    }

    private fun mediaPause() {
        val intent = Intent(this, MediaPlayerService::class.java).apply {
            action = MEDIA_PLAYER_PAUSE
        }
        startService(intent) // Service 일시 정지 (새로운 Service 시작 X)
    }
}
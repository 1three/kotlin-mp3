package fastcampus.part1.mp3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
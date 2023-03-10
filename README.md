# TerrasProject
### 정보과학관 테라스 예약 어플 프로젝트
### Seat Reservation for "Terras in Soongil Univ"
<br>
스터디룸을 예약한 후, 15분 이내에 스터디룸 내부에 있는 **QR 코드 인증**을 완료해야 예약이 확정되는 시스템의 어플 입니다. 

<br><br>

### Layout
![image](https://user-images.githubusercontent.com/65281502/224206901-d18d75c7-ae38-4458-9d58-2f949eb36555.png)
![image](https://user-images.githubusercontent.com/65281502/224206929-5d6de6eb-0c93-44ad-9d6d-72a4f90535bd.png)

<br><br>
### Tech Stack
- **Firebase**- 이메일을 통한 로그인 및, 사용자 정보 및 좌석 예약을 관리하기 위한 실시간 데이터베이스를 사용하였습니다.
- **QRcode** - zxing 라이브러리를 통해 QR 코드를 스캔하고 반환 값을 가져오는 작업을 실행했습니다.
- **broadcast** - 예약한 시간이 지나면 자동으로 좌석의 상태를 업데이트하기 위해 백그라운드에서 alarmmanger 를 통해 타이머를 설정하였습니다.


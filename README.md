# meta-public
마인크래프트와 IoT 플랫폼을 잇는 플러그인

IoT 플랫폼 - [DeviceResitry]
언어 - Only Java

개요  
- IoT 플랫폼과 마인크래프트를 연결하여 디지털 트윈을 구현하고자 하였다.

설정 및 실행
- [Server]를 참고하여 /mysql/DBManager를 구현할 것
- 빌드 후 서버에 플러그인 파일에 빌드된 java파일 넣어서 서버 실행

작동 방법
- 명령어: "/meta getDevice" 를 입력하여 등록된 Device 목록를 가져옴
- 다음 UI에서 해당 디바이스의 한 요소(module)를 선택
- 해당 module이 인벤토리에 생성이 됨
- sensor이면 "/meta setSensor" 입력 or actuator이면 "/meta setActuator" 입력
- 원하는 곳에 설치 
- 동기화가 되는지 확인 

예시 
![image](https://user-images.githubusercontent.com/67933557/208829700-55365446-52e8-4963-94c9-0bc42fafb4e0.png)



[DeviceResitry]: https://github.com/leha82/dsem_iot_ms
[Server]: https://github.com/YangBH-0/springLoginServer-public.git

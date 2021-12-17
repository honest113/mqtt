# mqtt

- JAVA PROJECT
- Linux OS (Ubuntu 20.04)

## Mô tả

- Chương trình kiểu Publish/Subscribe như MQTT gồm các thành phần:
    - Server đóng vai trò Broker
    - Chương trình sinh dữ liệu Publisher
    - Chương trình hiển thị dữ liệu Subscriber
    
## Chức năng chính

- Subscriber kết nối với Broker để đăng ký nhận thông tin về topic (Subscriber được đăng ký nhiều topic)
- Publisher kết nối với Broker để đăng ký topic và publish data (Publisher được publish thông tin về 1 topic)
- Subscriber nhận được dữ liệu từ topic đã đăng ký và hiển thị thông tin cho người dùng
- Nhiều Publisher cùng kết nối đến Broker và publish dữ liệu với các topic khác nhau
- Nhiều Subscriber cùng kết nối với Broker, nhận và hiển thị thông tin từ các topic khác nhau đã đăng ký

## Giao thức

Publisher và Subscriber độc lập với nhau, có nghĩa là 2 bên không biết gì về nhau cả, Publisher không biết những subscriber nào đăng ký nhận thông tin về topic mà mình publish và ngược lại.

Giao thức giữa Publisher và Broker:

```bash
Publisher:  <Enter name>
Server:     200 Hello <Publisher name>
Publisher:  GET LIST TOPIC
Server:     201 LIST TOPIC OK:
            <--- list topic info --->
Publisher:  SET TOPIC
Server:     202 SET TOPIC OK
Publisher:  <Valid Topic Name>
Server:     221 REGISTER DONE
Publisher:  START PUB
Server:     203 START PUB OK
Publisher:  STOP PUB
Server:     204 STOP PUB OK
Publisher:  QUIT
Server:     500  BYE
```

Giao thức giữa Subscriber và Broker:

```bash
Subscriber: <Enter name>
Server:     200 Hello <Subscriber name>
Subscriber: GET LIST TOPIC
Server:     201 LIST TOPIC OK:
            <--- list topic info --->
Subscriber: SUB TOPIC
Server:     212 SUB TOPIC OK
Subscriber: <Topic name>
Server:     206 UPDATE TOPIC SUCCESSFUL
Subscriber: GET MY TOPIC
Server:     208 TOPIC: 
            <--- topic info --->
Subscriber: UNSUB TOPIC
Server:     213 UNSUB TOPIC OK
Subscriber: <Topic name>
Server:     206 UPDATE TOPIC SUCCESSFUL
Subscriber: QUIT
Server:     500 BYE
```

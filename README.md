# mqtt

- JAVA PROJECT
- Linux OS (Ubuntu 20.04)
- [Source-Code](https://github.com/honest113/mqtt)

## Mô tả

- Chương trình kiểu Publish/Subscribe như MQTT gồm các thành phần:
    - Server đóng vai trò Broker
    - Chương trình sinh dữ liệu Publisher
    - Chương trình hiển thị dữ liệu Subscriber
    
## Chạy chương trình

- run with command

```bash
# compile common
$ javac -cp lib/gson-2.8.8.jar common/*.java

# compile server
$ javac server/*.java

# compile publisher and subscriber
$ javac clients/*.java

# run server
$ java -cp .:./lib/gson-2.8.8.jar server.Server

# run publisher
$ java -cp .:./lib/gson-2.8.8.jar clients.Publisher 127.0.0.1

# run subscriber
$ java -cp .:./lib/gson-2.8.8.jar clients.Subscriber 127.0.0.1
```

- run with script file

```bash
$ chmod +x script.sh
$ ./script.sh
```

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

## Các yêu cầu và phản hồi trong chương trình

### Các request được hỗ trợ:

| Câu lệnh              | Đối tượng                 | Giải thích                                    |
| :--------             | :--------                 | :---------                                    |
| GET LIST TOPIC        | Publisher & Subscriber    | Lấy thông tin về topic hiện có trên server    |
| SET TOPIC             | Publisher                 | Set topic cho publisher                       |
| START PUB             | Publisher                 | Publisher sau khi đăng ký topic có thể dùng lệnh này để bắt đầu phát data (ngẫu nhiên) |
| STOP PUB              | Publisher                 | Dừng việc publish data                        |
| GET MY TOPIC          | Subscriber                | Hiển thị các topic mà subscriber đăng ký      |
| SUB TOPIC             | Subscriber                | Subscriver đăng ký nhận thông tin từ topic    |
| UNSUB TOPIC           | Subscriber                | Subscriver hủy đăng ký nhận tin từ topic      |
| QUIT                  | Publisher & Subscriber    | Kết thúc phiên làm việc                       |

### Các phản hồi trả về:

| Phản hồi                              | Đối tượng nhận            | Giải thích                                                    |
| :--------                             | :--------                 | :---------                                                    |
| 200 Hello                             | Publisher & Subscriber    | Hello response                                                |
| 201 LIST TOPIC OK                     | Publisher & Subscriber    | Thông tin về list topic                                       |
| 202 SET TOPIC OK                      | Publisher                 | Server đã chấp nhận yêu cầu set topic của publisher           |
| 212 SUB TOPIC OK                      | Subscriber                | Server đã chấp nhận yêu cầu đăng ký topic của subscriber      |
| 213 UNSUB TOPIC OK                    | Subscriber                | Server đã chấp nhận yêu cầu hủy đăng ký topic của subscriber  |
| 221 REGISTER DONE                     | Publisher                 | Publisher đã hoàn tất thông tin                               |
| 203 START PUB OK                      | Publisher                 | Publisher tự động sinh dữ liệu                                |
| 204 STOP PUB OK                       | Publisher                 | Publisher ngừng tự động sinh dữ liệu                          |
| 205 RECEIVE MESSAGE OK                | Publisher                 | Publisher publish thông tin thành công                        |
| 206 UPDATE TOPIC SUCCESSFUL           | Subscriber                | Subscriber cập nhật list topic thành công                     |
| 208 TOPIC:                            | Subscriber                | List topic đang đăng ký của subscriber                        |
| 400 BAD REQUEST                       | Publisher & Subscriber    | Không rõ lệnh thực thi                                        |
| 411 SET TOPIC ERROR                   | Publisher                 | Publisher đăng ký topic thất bại                              |
| 412 START PUB FAIL - REGISTER REQUIRE | Publisher                 | Publisher chưa hoàn tất thông tin                             |
| 413 TOPIC NOT FOUND                   | Subscriber                | Không tìm thấy topic                                          |
| 423 INVALID TOPIC NAME                | Publisher                 | Tên topic không hợp lệ                                        |
| 500 BYE                               | Publisher & Subscriber    | Kết thúc kết nối                                              |

#!/bin/bash 

echo "===== auto compile project by script ====="

# compile common
javac -cp lib/gson-2.8.8.jar common/*.java

# compile server
javac server/*.java

# compile publisher and subscriber
javac clients/*.java

# run
gnome-terminal --title='Server' -- java -cp .:./lib/gson-2.8.8.jar server.Server
gnome-terminal --title='Publisher' -- java -cp .:./lib/gson-2.8.8.jar clients.Publisher 127.0.0.1
gnome-terminal --title='Subscriber' -- java -cp .:./lib/gson-2.8.8.jar clients.Subscriber 127.0.0.1

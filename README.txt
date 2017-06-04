** Client is supposed to use default port = 1997 for this assignment

Compile source code:

javac -cp rocksaw-1.1.0.jar; runServer.java runClient.java


Run: (you have to run in command line in project directory with administration permission)

1. Server: 
java -cp rocksaw-1.1.0.jar; runServer <FrontServer_port> <MD5_port> <SHA256_port>
Example:
java -cp rocksaw-1.1.0.jar; runServer 32000 1600 81

2. Client:
java -cp rocksaw-1.1.0.jar; runClient <FrontServer_IP> <FrontServer_port> <your_string>
Example:
java -cp rocksaw-1.1.0.jar; runClient 192.168.10.1 32000 helloserver!!
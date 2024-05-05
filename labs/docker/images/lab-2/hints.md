# Lab Hints

To install OpenJDK-11 in ubuntu you need to `RUN apt-get update && apt-get install openjdk-11-jdk -y`

To extract the tar file you need to `RUN tar -xvzf apache-tomcat-9.0.56.tar.gz`

To move the extracted contents `RUN mv apache-tomcat-9.0.56/* /opt/tomcat`

To start tomcat `CMD ["/opt/tomcat/bin/catalina.sh", "run"]`

> Need more? Here's the [solution](solution.md).
FROM java:8
MAINTAINER 564203965@qq.com

#这里的 /tmp 目录就会在运行时自动挂载为匿名卷，任何向 /tmp 中写入的信息都不会记录进容器存储层
#VOLUME /cloud-application-docker
#RUN mkdir /gateway
#WORKDIR /gateway

#复制target/cloud-gateway-1.0.0.jar到容器里WORKDIR下
COPY target/cloud-gateway-1.0.0.jar cloud-gateway-1.0.0.jar
EXPOSE 9090

ENTRYPOINT ["java","-jar","cloud-gateway-1.0.0.jar"]
#ENTRYPOINT ["java","${JAVA_OPTS}","-jar","cloud-gateway-1.0.0.jar"]
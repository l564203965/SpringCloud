cd /app/web/jenkins/code/SpringCloud/cloud-gateway
mvn clean package
if [ -e "./volumes/app/cloud-gateway-1.0.0.jar" ]
 then rm -f ./volumes/app/cloud-gateway-1.0.0.jar \
&& cp ./target/cloud-gateway-1.0.0.jar ./volumes/app/cloud-gateway-1.0.0.jar \
&& docker restart cloud-gateway \
 && echo "update restart success"
else mkdir volumes/app -p \
&& cp ./target/cloud-gateway-1.0.0.jar ./volumes/app/cloud-gateway-1.0.0.jar \
&& docker-compose -p cloud-gateway up -d \
&& echo "first start"
fi
if [ ! -d apache-maven-3.8.6 ]; then
	wget https://dlcdn.apache.org/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.tar.gz
	tar xf apache-maven-3.8.6-bin.tar.gz
fi
apache-maven-3.8.6/bin/mvn package
if [ ! -d apache-tomcat-10.0.27 ]; then
	wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.0.27/bin/apache-tomcat-10.0.27.tar.gz
	tar xf apache-tomcat-10.0.27.tar.gz
fi
cp target/CinemaReservation-1.0-SNAPSHOT.war apache-tomcat-10.0.27/webapps/CinemaReservation.war
apache-tomcat-10.0.27/bin/startup.sh

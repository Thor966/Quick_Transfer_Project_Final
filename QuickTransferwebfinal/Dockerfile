FROM tomcat:9.0-jdk11

# Remove default ROOT app (optional but recommended)
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your WAR into Tomcat's webapps directory
COPY target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

# Render will assign a PORT, so make Tomcat use it
CMD sed -i "s/port=\"8080\"/port=\"${PORT}\"/g" /usr/local/tomcat/conf/server.xml && \
    catalina.sh run

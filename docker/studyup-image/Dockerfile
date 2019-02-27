# Pull base image.
FROM tomcat:9-jre11

RUN ["rm", "-fr", "/usr/local/tomcat/webapps/ROOT"]
COPY ROOT.war /usr/local/tomcat/webapps/
ENV PATH=$PATH:/usr/local/tomcat/bin:
CMD catalina.sh run

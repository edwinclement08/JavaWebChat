FROM tomcat:9.0.17-jre8-alpine 

RUN mkdir /usr/local/tomcat/webapps/myapp

RUN rm -rf /usr/local/tomcat/webapps/ROOT
RUN rm -f /usr/local/tomcat/webapps/ROOT.war


COPY ./war/JavaWebChat-v1.war /usr/local/tomcat/webapps/ROOT.war

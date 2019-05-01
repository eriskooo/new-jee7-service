FROM jboss/wildfly
ADD target/new-jee7-service.war /opt/jboss/wildfly/standalone/deployments/

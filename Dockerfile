FROM maven:3.9.9-eclipse-temurin-17

RUN mkdir -p /home/applications/VacationPaymentCalculator

WORKDIR /home/application/VacationPaymentCalculator

COPY pom.xml /home/application/VacationPaymentCalculator
COPY /src /home/application/VacationPaymentCalculator/src

EXPOSE 8080

RUN mvn clean package
CMD ["java", "-jar", "/home/application/VacationPaymentCalculator/target/VacationPaymentCalculator-0.0.1-SNAPSHOT.jar"]
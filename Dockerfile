FROM java:8

ADD /target/talkingService-1.0-SNAPSHOT.jar talkingService-1.0-SNAPSHOT.jar

ADD talkingService.yml talkingService.yml

CMD java -jar talkingService-1.0-SNAPSHOT.jar server talkingService.yml

EXPOSE 8080

EXPOSE 8081
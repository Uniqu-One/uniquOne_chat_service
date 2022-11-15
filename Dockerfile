FROM adoptopenjdk/openjdk11
COPY build/libs/msa_chat_service-0.0.1-SNAPSHOT.jar app/chat-service.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "chat-service.jar"]
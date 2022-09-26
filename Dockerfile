FROM openjdk:11
COPY build/libs/*.jar uniquone_chat_img.jar
ENTRYPOINT ["java", "-jar", "uniquone_chat_img.jar"]
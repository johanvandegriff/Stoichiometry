FROM openjdk:25-jdk-slim-bookworm
COPY Stoichiometry.jar Stoichiometry.jar
ENTRYPOINT ["java","-jar","Stoichiometry.jar", "--server"]
# Build stage: Maven + Temurin 21 JDK
FROM maven:3.9.11-eclipse-temurin-21-noble AS build
WORKDIR /workspace

# Copy pom and download dependencies to leverage cache
COPY pom.xml .
RUN mvn -B -f pom.xml -DskipTests dependency:go-offline

# Copy source and build the project
COPY . .
RUN mvn -B -DskipTests package

# Runtime stage: Temurin 21 JRE
FROM eclipse-temurin:21-jre-noble
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /workspace/target/*.jar app.jar

# Defaults: dev profile and port 8091 (can be overridden with -e)
ENV SPRING_PROFILES_ACTIVE=dev
ENV PORT=8091
ENV JAVA_OPTS=""

EXPOSE 8091

# Allow passing JAVA_OPTS and override profile/port via env vars
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Dserver.port=${PORT} -jar /app/app.jar"]
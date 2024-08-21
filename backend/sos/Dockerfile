FROM mdernst/cf-ubuntu-jdk21

VOLUME /tmp

EXPOSE 8082

ARG JAR_FILE=build/libs/*.jar

ADD ${JAR_FILE} app.jar

WORKDIR /app

RUN apt-get update -qq && apt-get install -y wget unzip gnupg ca-certificates

RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-chrome-keyring.gpg

RUN echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome-keyring.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list

RUN apt-get update && apt-get install -y google-chrome-stable

ENV PATH="/usr/bin/google-chrome:${PATH}"

ENTRYPOINT ["java","-jar","/app.jar"]

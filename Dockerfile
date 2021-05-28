FROM public.ecr.aws/amazonlinux/amazonlinux:latest
COPY ./target/*.jar .
EXPOSE 18081
ENTRYPOINT ["java","-jar","eco-games.jar"]

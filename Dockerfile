FROM public.ecr.aws/amazonlinux/amazonlinux:latest
COPY ./target/*.jar .
ENTRYPOINT ["java","-jar","eco-games.jar"]

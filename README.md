mvn clean

mvn compile

mvn package


docker build -t iris_model .

docker run -it --publish 8080:8080 --rm iris_model

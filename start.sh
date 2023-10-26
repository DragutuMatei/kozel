
# echo "Build docker .."
# docker build -t fastlane-image .
echo "Pulling mongo .."
docker pull mongo
echo "Run mongo and springapplication .."
docker run -d --name mongo-on-docker -p 27017:27017 mongo
# docker run -d --name springapplication-on-docker -p 8080:8080 fastlane-image/
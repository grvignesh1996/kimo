mvn clean install
docker login
docker build -f Dockerfile -t assignment . --platform linux/amd64
docker tag assignment:latest cachecodevigneshgr/kimo:latest
docker push cachecodevigneshgr/kimo:latest
# vpay

## Deploy
### Docker Install (Recommend)
When deploying with docker, you need to install `docker` and `docker-compose` first

```bash
wget https://raw.githubusercontent.com/kales0202/vpay/main/docker-compose.yml -O docker-compose.yml
wget https://raw.githubusercontent.com/kales0202/vpay/main/vpay.tar.gz -O vpay.tar.gz

tar -zxf vpay.tar.gz -C ./app
docker-compose up
```

### Code Install
```bash
wget https://raw.githubusercontent.com/kales0202/vpay/main/vpay.tar.gz -O vpay.tar.gz
tar -zxf vpay.tar.gz
java -jar myapp.jar -Dspring.profiles.active=prod
```
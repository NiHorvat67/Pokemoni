## Kako pokrenuti aplikaciju

### O aplikaciji
- Sastoji se od 3 dijela: frontend, backend, postgress odvojenim u različite direktorije
- Frontend - `React VITE`
- Backend  - `Java Spring Boot`
- Postgres(database) - `Docker` +  `postgress`

- Za pokretanje aplikacije preporučuje se korištenje `Ubuntu/Linux Mint/EndeavorOS` ili na Windowsu `wsl2` 

### Kako pokrenuti frontend

- aplikacija se pokreće lokalno na [port:5173](http://localhost:5173/)



```bash
$ # cd into frontend dir
$ npm install
$ npm run dev
```

### Kako pokrenuti backend

- requirements :
    - Maven
    - JAVA_HOME enviromental variable set
    - [pomoć ako se aplikacije "ne pokreće odmah"](https://spring.io/guides/gs/spring-boot)
- aplikacija se pokreće lokalno na [port:8080](http://localhost:8080/)


```bash
$ # cd into backend dir
$ ./mvnw spring-boot:run
```


### Kako pokrenut postgres bazu

- requirements :
    - slobodan `port:5431` 
    - Docker, Docker-compose, postgres
- posgress baza je realizirana pomoću Dockera na kojem se "vrti postgres"


Start(first time)
```bash
# cd into postgres dir
$ sudo docker-compose up -d

```

Restart

```bash
# cd into postgres dir
$ sudo docker-compose down -v
$ sudo docker-compose up -d

```

Spajanje na bazu
``` bash
$ sudo docker exec -it my_postgres psql -U postgres gsadmin
psql
```

```psql
gsadmin=# -- to view all the accounts in db use this query
gsadmin=# select * from account; 

gsadmin=# -- to exit use this
gsadmin=# \q
```



### Mogući problemi
- docker container nije pokrenut -> moguće je pokrenuti docker container koristeći `Docker Desktop`
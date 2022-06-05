# NORMA FINAL PROJECT

## Proje tanıtımı

Online bankacılık sistemi, banka şubesine gitmeden internet erişimi olan bir bilgisayar aracılığı ile 
yapılan bir backend uygulaması olup birçok bankacılık işlemini kapsar.
Müşterilere verilen 4 temel hizmet vardır.

* Müşteri yönetimi
* Hesap yönetimi
* Kart yönetimi
* Transfer yönetimi

### Projede kullanılan teknolojiler
* Java 17
* Spring Boot
* Hibernate
* PostgresSql
* Docker
* Junit Test
* Integration Test
* Spring Security
* JWT
* Lombok
* Swagger v3

### Uygulamayı localde başlatmak için komut  dizisi
```
git clone https://github.com/EnginAkin/final-project.git
$ cd ./compose
$ docker-compose up
$ mvn clean install
$ mvn spring-boot:run
```

#### Kullanıcılara sunulan hizmetler
* üyelik
* kullanıcı bilgilerini güncelleme
* kullanıcı silme
* giriş 
* vadesiz hesap oluşturma
* vadesiz hesabın hesap hareketlerini detaylı görüntüleme
* vadeli birikim hesabı oluşturma
* vadeli hesabın hesap hareketlerini detaylı görüntüleme
* vadeli birikimli hesaptan verilen vade ile para biriktirme (faiz)
* banka kartı başvurusunda bulunma
* kredi kartı başvurusunda bulunma
* banka kartı ile alışveriş , para yatırma ve çekme işlemleri
* kredi kartı ile alışveriş yapma
* kredi kartının güncel ve önceki ekstreden kalan borçları görüntüleme
* kredi kartlarının detaylı hesap hareketlerini görüntüleme
* hesapları arasında para transferi yapma
* iban üzerinden ve email üzerinden transfer yapma
* farklı türlerde açılmış hesaplar arasında güncel kurdan anlık para transferi

## Swagger 

### Customer endpoints
![presentation](screen-shoots/swagger-end-points/user-end-points.PNG)

------------
### Authentication endpoints
![presentation](screen-shoots/swagger-end-points/authentication-end-points.PNG)

------------
### Checking account endpoints
![presentation](screen-shoots/swagger-end-points/checking-account-end-points.PNG)

------------
### Saving account endpoints
![presentation](screen-shoots/swagger-end-points/saving-account-end-points.PNG)

------------
### Debit card endpoints
![presentation](screen-shoots/swagger-end-points/debit-cards-end-points.PNG)

------------
### Credit card endpoints
![presentation](screen-shoots/swagger-end-points/credit-card-end-points.PNG)

------------
### Transfer endpoints
![presentation](screen-shoots/swagger-end-points/transfer-end-points.PNG)

------------
### Shopping endpoints
![presentation](screen-shoots/swagger-end-points/shopping-end-points.PNG)

------------
### ATM endpoints
![presentation](screen-shoots/swagger-end-points/atm-end-points.PNG)

---------
### Database Diagrams

![presentation](screen-shoots/table-diagrams/database-diagram.png)

### Test sonuçları
![presentation](screen-shoots/test-coverage/coverage-project.PNG)

package com.Omer.Account.notes;

public class SecurityNotes {
    /*

                                    username@password               ------------->DB
                                  ----------------------->HttpBasic
                                  |     (encrypted)                 ------------>Inmemory
                                  |
      username@password           |
     =====================>       |
                                  |
                                  |
istek                       Account-Api
                                  |
              login               |
       ======================>    |
       <======================    ------------------------------                                          (authentication provider)
               token                                          |                                                MS
                                                              |               username@password                Google
               GET /Header: token                                          |------>      ----------------------------->     Github
        ======================>                                     | OAUTH2       Rest İstek                  Twitter
        <======================                                     |        <------------------------------- FaceBook
                 result                                                   |                   token
                                                         Token      |            ttl:180sn genelde
                                                                    |               time to live
                                                                    |
                                                                    |
                                                                   \/
                                                                OAUTH2 içinde bulunan Db
                                                                    Redis
                                                                    Db
                                                                    Mongo Db
                                                                    Inmemory
                                                                    H2 olabilri neresi conf edilirse







Basic:
Uygulamaya istek atıldığında uygulama kullanıcıdan username ve password ister. password encrypted dır.
Daha sonra username ve passwordu Basic katmanı Db ye sorgular
Db ya gerçek db dir (pg,mysql vs) yada inmemory dir.
inmemoryde uygulama ayağı kalkarken inmemory olarak configure edilen username ve password kaydedilir ve o kullanıcı ile girilir. Oraya yeni bir user eklenemez
Test uygulamalarında inmemoryde tutulabilir. Büyük projelerde db kullanılabilir.


OAUTH2:

Uygulama ya username ve password vererek istek atar.
 username ve password OUTH2 katmanından Bir uygulama ya rest isteği atarak username@password sorgular.
 Bu uygulama Ms olabilir Google FaceBook vs olabilir.
Daha sonra istek atılıp sorgulanan bu uygulamadan OAUTH2 ye token döner.
OAUTH2 katmanı bu tokenı kendi içinde bulunan configure edilen db ye kaydeder.

Daha sonra client yapacağı tüm isteklerde headera tokenı ekler.token Token hashlenmiş bir string yapısıdır.
  Bu hash'in yapısını authentication provider uygulaması biliyor. Eğer OAUTH2 ye kaydedersek oda bilir.



OAUTH2 tokenı nasıl kullanır:
client uygulamaya login olur ve uygulama bize OAUTH2 ile birikte bir token döner.
ttl suresi configure edilen saniyeye göre (örneğin 180 saniye içerisinde)
client http isteklerinde (örneğin get olsun) header olarak token ekler
Account-api her almış olduğu istekte bu tokenın doğruluğunu ve ttl sini kontrol eder.
eğer valid se istek yapılır ve geriye result dönülür.



Not:
authentication : username password doğrumu
authorization : userın işleme yetkisi varmı



******************* Spring Security Starting ***************

Basic Config:Inmemory olarak yapacağız
öncelikle dependency ekliyoruz

	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-security</artifactId>
	</dependency>

Daha sonra SecurityConfig  configuration ekliyoruz.

 @Bean
    protected InMemoryUserDetailsManager configureAuthentication()
    {
        String passwordUser= "User1234";
        String passwordAdmin ="Admin1234";

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode(passwordUser))
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode(passwordAdmin))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user,admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .antMatchers("/v1/account/trs").hasRole("ADMIN")
                .antMatchers("/v1/account").hasRole("USER")
                .antMatchers("/v1/customer/**").hasRole("USER")
                .and().formLogin();
        return http.build();

   }


   Bu configuration da Authorization ve Authentication işlemleri yaptık

   Authorization methodunda gelen http isteklerine url bazında rol odaklı yetkilendirme işlemleri gerçekleştirdik.
   Authentication methodunda ise Yetkilendirme yapacağımız rolleri username ve password bilgilerini Securitynin bize sağlamış olduğu
     User classını oluşturarakn ekledik. Daha sonra bu User bilgilerini Inmemory de sakladik.(password bilgisini encoder ladık)




 ****************************************************************************************************************************************

JWt Nedir:

( Json Web Token )

 JWT token request response ların üzerinde servera giden isteklerin aynı kişiden gidip gitmediğini doğrulayan bir yöntem.

JWT token 3 parçadan oluşur(3 noktayla ayrılmış karmaşık string bir mesaj)
     Parçalar sırası ile:
     -Header : Şifreleme algoritmasının ne olduğu belirtilir
     -Payload : Bizim istediğimiz data ve iat: tarih alanı(token ne zaman oluşturuldu)
     -Signature : sunucunun imzası(headerın base64encoder,payload base64encoder,sunucu tarafında şifrelenen kod (tek yönlü: bir daha oluşmuyor)

Client username@password ile server'a doğrulama isteğ attığında server bize jwt token oluşturur ve geri döner.
Daha sonra Sunucuya gönderilecek isteklerde HTTP Header içerisinde alınan bu token gönderilir ve server dan cevap gelir.

!!
JWT nin güvenlik açığı: JWT tokenın amacı isteğin doğru client tarafından gelip gelmediğini analamak
Bu nedenle bu token başkası tarafından bizim tarafımızdan kullanılabilir.
Bu açığı kapatmak için https protoklü kullanarak mesajı şifreleyip gönderirsek tokenın çalınmasını engelleyeibilirz




       Client                         JWT Filter                     AUTH Controller                          SpringAuthManager                        UseDetailsService                              Token Manager
         |          HTTP POST             |                                    |                                      |                                        |                                           |
         |------------------------------->                                     |                                      |                                        |                                           |
         |  username@password doğrulama   |                                    |                                      |                                        |                                           |
         |                                |                                    |                                      |                                        |                                           |
         |                     Token varmı/Token Doğrumu                       |                                      |                                        |                                           |
         |                                |                                    |                                      |                                        |                                           |
         |                                |            Token Oluştur           |                                      |                                        |                                           |    Methodlar:
         |                                |----------------------------------->|                                      |                                        |                                           |   Token Oluştur(fonksiyon a)
         |                                |                                    |      username@password doğrula       |                                        |                                           |
         |                                |                                    | ---------------------------------->  |                                        |                                           |
         |                                |                                    |                                      |           loadUserByUsername           |                                           |   Token Kontrol Et (fonksiyon b )
         |                                |                                    |                                      |--------------------------------------> |                                           |
         |                                |                                    |                                      |         username i doğrulamak için     |    Database                               |
         |                                |                                    |                                      |         username 'i repository e sor   |                                           |   User Dan Token Al (fonksiyon c)
         |                                |                                    |                                      |                                        |                                           |
         |                                |                                    |                                      |                                        |                                           |
                                          |                                    |       Doğrulama yapıldı(true)        |                                        |                                           |
         |                                |                                    |<-------------------------------------|                                        |                                           |
         |                                |                                    |                                      |                                        |                                           |
         |                                |                               Token Oluştur                               |                                        |                                           |
         |                                |                          (fonksiyon a çağırılır)                          |                                        |                                           |
         |                                |                                    |                                      |                                        |                                           |
         |                                |                                    |                                      |                                        |                                           |
         |                                |               JWT Token            |                                      |                                        |                                           |
         |                                | <----------------------------------|                                      |                                        |                                           |
         |          JWT Token             |                                    |                                      |                                        |                                           |
         |<-------------------------------|                                    |                                      |                                        |                                           |
         |                                |                                    |                                      |                                        |                                           |
         |                                |                                    |                                      |                                        |                                           |
         |       HTTP Request (Token)     |                                    |                                      |                                        |                                           |
         |-------------------------------->            (fonksiyon b)           |                                      |                                        |                                           |
         |                                |            Token Doğrula           |                                      |                                        |                                           |
         |                                |----------------------------------->|                                      |                                        |                                           |
         |                                |                                    |                                      |                                        |                                           |
         |                                |                                    |                          Kullanıcı Doğrula (LoadUserBy                        |                                           |
         |                                |                                    |------------------------------------------------------------------------------>|                                           |
         |                                |                                    |                                      |                                        |   Database                                |
         |                                |                                    |                                    VALID                                      |                                           |
         |                                |      (istek atılan controller)     |<----------------------------------------------------------------------------->|                                           |
         |                                |     İlgili Controller Çalıştır     |                                                                               |                                           |
         |            Response            |<-----------------------------------|                                                                               |                                           |
         |<------------------------------





*Jwt filter : her gelen istek jwt filte'e girer bu filterleme işleminden sonra ilgili controller lara gider


****************************************************** OAUTH2  Security  *************************************************************************


                                                        Authorization Grant
                                                  |---------------------------------------->  Authorization
                  Authorization Request           |            Acces Token                      Server
              ---------------------------->       |   <--------------------------------------

   Kullanıcı                                    Client
                                                  |              Protected Resource
                   Authorization Grant            |   <--------------------------------------
              <----------------------------       |                                           Resource
                                                  ------------------------------------------->
                                                                     Acces Token

Kullanıcı : yetkilendirilen kişidir
Client App: Facebook login, Github Login vs Veya kendi yazmış olduğumuz client
Authorization Server: FaceBook login server,Google login server ( FaceBook yetkilendirme sunucusu)
Resource Server:FaceBook kullanıcısı gönderielerinin saklı olduğu sunucu


Yani Facebook ile login ol sayfasına aktarıcaktır gerekli conf lardan sonra
















































    */
}



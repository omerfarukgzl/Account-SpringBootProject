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

JSWT Codding Start:

*************************************** Dependency ******************************
Öncelikle dependency eklenir

       <dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt</artifactId>
			<version>0.9.1</version>
		</dependency>





*************************************** JWT Token Manager ******************************

Daha sonra Jwt Token Manager oluşturduk:

@Service
public class TokenManager {


        private final String SECRET_KEY = "omerfarukgzl";//şifrelenicek olan anahtar değeri


userDetails objesini alır. createToken metoduna gönderir.
         public String generateToken(UserDetails userDetails) {
          Map<String, Object> claims = new HashMap<>();
          return createToken(claims, userDetails.getUsername());
         }

         private String createToken(Map<String, Object> claims, String subject) {
          return Jwts.builder().setClaims(claims)
                  .setSubject(subject) // username
                  .setIssuer("Omer")//kim tarafından oluşturuldu
                  .setIssuedAt(new Date(System.currentTimeMillis())) // oluşturulma zamanı
                  .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 60 * 1000)) // geçerlilik süresi
                  .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // kullanılan algoritma ve bu algoritma çalışırken kullanılacak hash key değeri
                  .compact();
         }


token hala geçerli mi? kullanıcı adı doğru ise ve token ın geçerlilik süresi devam ediyorsa true döner.
         public Boolean validateToken(String token, UserDetails userDetails) {
          // Username and token süresi Valid ise return true
          final String username = extractUsername(token);
          return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
         }



verilen token a ait kullanıcı adını döndürür.
         public String extractUsername(String token) {
          //return token's user name
          return extractClaim(token, Claims::getSubject);
         }

verilen token a ait token bitiş süresini verir.
         public Date extractExpiration(String token) {
          //return token's bitiş süresi
          return extractClaim(token, Claims::getExpiration);
         }


token ın geçerlilik süre doldu mu?
         private Boolean isTokenExpired(String token) {
          //nowDate token bitiş tarihinden önce ise return true
          return extractExpiration(token).before(new Date());
         }


istenilen token bilgilerini çöz ve istenilen bilgiyi geri döndürür
         public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
          final Claims claims = extractAllClaims(token);
          return claimsResolver.apply(claims);
         }

verilen token a ait bilgi alanlarını çöz ve tüm alanları geri döndür
         private Claims extractAllClaims(String token) {
          //şifrelenen gizli anahtarı kullanarak istenilen token bilgisini parçala ve paraçalan kısmı döndür
          return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
         }


}





*************************************** UserDetails ******************************


Daha sonra UserDetailsService Service ini oluşturduk.
* Springin bize sağladığı userDetailsService interface ini implement ettik
ve bu implement sonucu loadUserByUsername fonksiyonunu override ettik
override edilen bu fonksiyonda db ile konuşarak (kullanıcı yetkilerini ve kullanıcıları db yerine bir hashmap te tutacağız)
bu kullanıcı varsa bize bu kullanıcıyı dön (spring security nin User özelliği)


        @Service
        public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

            @Autowired
            AuthUserMockDb authUser;

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return authUser.getUserByUsername(username);
            }
}








*************************************** Filter ******************************



Daha sonra Token Fiter Service ini oluşturudk

!! Bu service her bir isteğe bakabilmesi , her gelen isteği filtereleyip sonra geçirebilmesi için OncePerRequestFilter türetilmesi gerekir

Token Header, istek gelirken Authorization keyinden Bearer 123jdwjabd33 value şeklinde gelir.
Bizim tokenla işlem yapabilmemiz için token headrı bir değişkende tutup onu parse etmemiz gerekir.







































































    */
}



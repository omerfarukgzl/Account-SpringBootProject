package com.Omer.Account.notes;

public class IntegrationTestNotes {

/*

##########################################################################################################################################################################################################################################




********************** Unit Test - Intagration Test Farkı *********************************

Bütün unitlerin birbirleriyle entegrasyonun testidir !!

Controller seviyesyinde controller'a istek yapılır.
Controllerın service 'i çağırması beklenir.
Veriyi gerçekten db ye kaydedip kaydetmediği test edilir.



Her ikisi de farklı amaçlara hizmet ediyorlar.
Integration Test bize bir şeyin çalışıp çalışmadığını söylerken, Unit Test neden çalışmadığını söyler.
Unit Test yazılımcı perspektifinden bakarken, Integration Test kullanıcı perspektifinden yazılır.
Integration Test ile, sistemin kullanıcıların beklediği gibi çalıştığından emin oluruz.

Integration test, unit test’e göre çok daha maliyetlidir.
Örneğin; içerisinde veri tabanına erişen kodlar barındıran bir metot için Unit Test yazarken, gerçekte veri tabanına erişmezsiniz.
Veri tabanına erişen servisinizi mock’layarak metodun dış dünya ile olan ilişkisini kesersiniz. Ancak Integration Test farklıdır.
Integration Test, veri tabanı erişimi veya bir web servis çağrısı söz konusu ise bu IO işlemini gerçekten icra edecek şekilde yazılır.
Buradan hangisinin daha maliyetli olduğu anlaşılabiliyor.


*******************************************************************************************


******************************** Account Controller Test **********************************


  public void testCreateAccount_whenCustomerIdExist_ItShouldCreateAccountAndReturnAccountDto () throws Exception {


Uygulamanın gerçek çalışır halinin akışını düşünmeliyiz.
Öncelikle bu senaryoyu işletebilmem için veritabanına customer kaydetmem gerekiyor.
Çünkü projemizde var olan customer'a hesap ekleme fonskiyonları yazıldı.
Zaten gerçek uygulama ayağa kalkarken customer oluşturulur ve db ye kaydedilir

        Customer customer = customerRepository.save(new Customer("","Omer","Faruk", new HashSet<>()));
        (Neden Unit Test deki Customer customer = getCustomer() (TestSupport sınıfınfaki method) kullanmadık ?
        Çünkü mock lamıyoruz gerçek verilerle haraket edeceğimiz için gerçekten db ye kaydedip o id ler le işlem yapacağız.)

Daha sonra istek göndereceğimiz parametreyi oluşturyoruz.(CreateAccountRequest)

Daha sonra crud metodumuzu post göndereçeğimiz içerik tipini (json) ve post olarak gönderceğimiz formattaki içeriği (creatAccountRequest) belirliyoruz
     .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createAccountRequest)))


Daha sonra sonuç olarak beklediğimiz durum tipini belirtiyoruz(200,300,400,500 durumları(error , succes vs))
    .andExpect(status().is2xxSuccessful())

Daha sonra bekledğimiz içerik tipinin formatını belirtiyoruz(json)
     .andExpect(content().contentType(MediaType.APPLICATION_JSON))

Daha sonra geri repsone oladrak beklediğimiz jsonpath içeriklerini belirtiyoruz.
    andExpect(jsonPath("$.id", notNullValue()))// veritabnına kayıtlı olan id yi getirecektir .
                .andExpect(jsonPath("$.balance", is(100)))//AccountDto içerisindeki  balance verilerini getir
                .andExpect(jsonPath("$.customer.id", is(customer.getId())))//AccountDto içerisindeki customer içerisindeki customer id verisini getir
                .andExpect(jsonPath("$.customer.name", is(customer.getName())))//AccountDto içerisindeki customer içerisindeki customer name getir
                .andExpect(jsonPath("$.customer.surname", is(customer.getSurname()))) //AccountDto içerisindeki customer içerisindeki customer surname getir
                .andExpect(jsonPath("$.transactionDtos", hasSize(1))) // AccountDto içerisindeki transactionDtos verileri getir
                .andDo(print()) // dönen sonucu ekranda göster

İşlem sonucumuzda geriye bir accountDto nesnesi döndüğü için accountDto verilerinden yola çıkarak repsone dönen verilerimizin bilgilerini belirtiyoruz ve test başarılı sonuçlandı.



@Test
    public void testCreateAccount_whenCustomerIdDoesNotExit_shouldReturn404NotFound() throws Exception {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("id", new BigDecimal(100.0));

        this.mockMvc.perform(post(ACCOUNT_API_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createAccountRequest)))
                .andExpect(status().isNotFound())
                .andDo(print()); // dönen sonucu ekranda göster;

    }

    @Test
    public void testCreateAccount_whenCustomerIsNotValid_shouldReturn404NotFound() throws Exception {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("isNotValidId", new BigDecimal(100.0));

        this.mockMvc.perform(post(ACCOUNT_API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createAccountRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print()); // dönen sonucu ekranda göster;

    }

    @Test
    public void testCreateAccount_whenCustomerIdExist_ButInitialCreditLessThanZero_ItShouldReturn400BadRequest () throws Exception {

        CreateAccountRequest createAccountRequest = new CreateAccountRequest("id", new BigDecimal(-100));

        this.mockMvc.perform(post(ACCOUNT_API_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createAccountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print()); // dönen sonucu ekranda göster;




    }





    Bu Senaryolarda ise Sırası ile
    Gönderilen istek nesnesindeki verilerin geçerli olduğu fakat istenilen customer bulunamadığı ,
    Gönderilen istek nesnesinde customerId verisinin geçersiz Initial Credit verisi geçerli bir veri olduğu,
    Gönderilen istek nesnesinde customerId verisinin geçerli Initial Credit verisinin geçersiz olduğu durumların testi yapılmıştır.

   Codlarındaki tek fark status(dönen durumlar : 200,300,400,500. BadRequest, NotFoundErrror vs)


*******************************************************************************************





******************************** Customer Controller Test **********************************


Öncelikle Bu Controllerin testini yazarken hata aldım !
Bu hata : Java 8 date/time type `java.time.>LocalDateTime` not supported by default

Hata java 8 den sonra LocalDateTime Veri tipini desteklemediğini bunu için bir dependency yüklemem gerektiğini anlatıyor.

Çözümü:
1)Dependency Ekle
<dependency>
        <groupId>com.fasterxml.jackson.datatype</groupId>
        <artifactId>jackson-datatype-jsr310</artifactId>
        <version>${jackson.version}</version>
</dependency>

2) LocalDateTime verilerine Annotation'ları ekle
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
   @JsonSerialize(using = LocalDateTimeSerializer.class)
   @JsonDeserialize(using = LocalDateTimeDeserializer.class)



   Öncelikle customer bilgilerini getireceğimiz için db de customer account ve transaction bilgilerinin olması gerek. Dolayısıyla Öncleikle bu verileri oluşturuyoruz
   (Customer ve Account )
   ( Account oluşumu transaction oluşumunu tetikliyor) ==> accountService.createAccount Metodu.

   Daha sonra Controller içerisindeki test ettiğimi api nin akışına göre senaryoyu başlatıyoruz.

   Senaryomuz gereği istek atılan id parametresini customerService.getCustomerInformation Methoduna gönderip burdan yapılan işlemlerle Bir CustomerDto döndürelecek.

        Service methodu:
            public CustomerDto getCustomerInformation(String customerId)
            {
                Customer customer= customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer could not find by id: " + customerId));
                CustomerDto customerDto = customerDtoConverter.convertToCustomerDto(customer);
                return customerDto;

            }

        Bu service deki işlemde gönderilen id ye ait customer bulunmuş ve bu customer convert edilip csutomerDto olarak geri dönülmüş
        Bu işlemleri yazıyoruz.

                CustomerDto expected = converter.convertToCustomerDto(customerRepository.findById(Objects.requireNonNull(customer.getId())).get());

        Daha sonra Dönen Dto nesnesi conrollerda json tipinde kullanıcya geri dönülür.

            this.mockMvc.perform(get(CUSTOMER_API_ENDPOINT + customer.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(expected)));

                Öncelikle get işlemimizi belirtiyoruz.Get işleminin Url bilgisini veriyoruz  ==> this.mockMvc.perform(get(CUSTOMER_API_ENDPOINT + customer.getId()))
                Daha sonra beklenilen geri dönecek başarılı durumunu belirtiyoruz  ==>  .andExpect(status().is2xxSuccessful())
                Daha sonra beklenilen geri dönüş içeriğinin tipini (Json) belirtitouz.  ==>  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                En sonunda beklenilen json içeriğini döndürmek için  oluşturlan Dto yu objectMapper ile json'a çeviriyoruz. ==>  .andExpect(content().json(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(expected)));



public void testGetCustomerInformation_WhenIsExistCustomerId_ShoulRetrunCustomerDto() throws Exception
{

   Customer customer = customerRepository.save(generateCustomer());
   accountService.createAccount(generateCreateAccountRequest(customer.getId(), 100));

     CustomerDto expected = converter.convertToCustomerDto(
     customerRepository.findById(
     Objects.requireNonNull(customer.getId())).get());

      this.mockMvc.perform(get(CUSTOMER_API_ENDPOINT + customer.getId()))
     .andExpect(status().is2xxSuccessful())
     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
     .andExpect(content().json(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(expected)));
 }



    @Test
    public void testGetCustomerInformation_WhenIsNotExistCustomerId_ShoulRetrunBadRequest() throws Exception
    {
        String id="NotExistId";
        this.mockMvc.perform(get(CUSTOMER_API_ENDPOINT + id))
                .andExpect(status().isNotFound())
                .andDo(print());

    }
    Bu methodda ise geçersiz id verilip kullaınıcın bulunamaması senaryosunu test ettik





 @Test
    public void testGetCustomerAll_ShoulRetrunCustomerDtoList() throws Exception
    {

        List<Customer> customers=customerRepository.findAll();

        List<CustomerDto> customerDtoList =
                customers.stream()
                .map(converter::convertToCustomerDto)
                .collect(Collectors.toList());

        this.mockMvc.perform(get(CUSTOMER_ALL_API_ENDPOINT))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(customerDtoList)))
                 .andDo(print());
    }



Bu methodda ise tüm customer ların listelenmesi senaryosunu test ettik senaryosunu test ettik














*******************************************************************************************





  */






}

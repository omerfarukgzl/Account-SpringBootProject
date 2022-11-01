package com.Omer.Account.notes;

public class Notes {


    /*


###############################################################################################################################################


        Bu proje, mevcut müşteriler için hesap oluşturmayı sağlar.

        Özet
        Değerlendirme, halihazırda mevcut müşterilerin yeni bir "cari hesabı" açmak için kullanılacak bir API'den oluşur.

        Gereksinimler
        • API, kullanıcı bilgilerini (customerID, initialCredit) kabul eden bir endpoints ortaya çıkaracaktır. ( Accounts Api)

        • Endpoints arandığında, kimliği müşteri kimliği olan kullanıcıya bağlı yeni bir hesap açılacaktır.

        • Ayrıca, initialCredit 0 değilse, yeni hesaba bir işlem gönderilir.

        • Başka bir Endpoints, hesapların Adı, Soyadı, bakiyesi ve işlemlerini gösteren kullanıcı bilgilerini çıkaracaktır.(Customer Api)


###############################################################################################################################################





    --Entitylerimizi oluşturduk ve birbirine bğlamak , tablo ilişkilerini kurmak için OneToMany ve ManyToOne özelliklerini ekledik.

                      Entity Relationship

    Customer ============> Account =============> Transaction
              OneToMany             OneToMany


####################################################################################################################################################################################################################################


********** Customer Table  ***********

     @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )

    --Generated value id nin nasıl oluşması gerektiğini (UUID=> uniq 32 karakterli bir id)
    --@GenericGenerator ise genaretedvalue de verdiğimiz özelliğin nasıl oluşacağını belirtir


    @OneToMany(mappedBy = "customer",fetch = FetchType.LAZY)
    private Set<Account> accounts;

    -- Customer tablosu ile Accont arasında OneToMany İlişkisi olduğu için OneToMany ilişkisi özelliğini ekledik
       Bu parametredeki :
       mappedBy = "customer" ==> özelliği Account tablosunda var olan customer nesnesi ile bağ kuracağını ifade eder.
       fetch = FetchType.LAZY ==> özelliği ise birbirleriyel ilişki halinde olan tablolarda sorgu yapıldığı zaman içlerinde bulunan birbirleirnin özelliklerini select loop halinde vermemesi için customer içinde bulunan account bilgileri get işlemi yapıldıktan sonra atanır.
               Bu özellik account tablosunda da yazıldığı zaman account tablosunda olan customer bilgileri get işlemi yapıldıktan sonra atanır ve select loop un önüne geçilmiş olur.

        private Set<Account> accounts; ==> özelliği Customer ın birçok accounts olacağı için Set yapısında Account Türünde nesnelerini sakladık.




--------------------------------------------------------------------Not-----------------------------------------------------------------------

 #FetchType : Aralarında ilişki bulunan entitylerden bir tarafı yüklerken diğer tarafın yüklenme stratejisini belirlememize olanak sağlar.

Hibernate içerisinde EAGER(Ön Yükleme) ve LAZY(Tembel/Sonradan Yükleme) şeklinde 2 tip entity yükleme stratejisi vardır. Bu tipleri örnekle açıklayacak olursak;

Elimizde yürütülen projeleri (Proje) ve bu projelerde çalışanları(Calisan) tuttuğumuz iki entity olsun.
 Projeler ve çalışanlar arasında bir ilişki bulunduğundan veritabanından Proje entitysini yüklediğimizde ilişkili olduğu Calisan tablosununda yüklenmesini istiyorsak fetch=FetchType.EAGER kullanırız.

Proje entitysini yüklediğimizde Calisan entitysinin yüklenmesini istemiyorsak yani ihtiyaç olması halinde Calisan entitysini yüklemek istiyorsak fetch=FetchType.LAZY kullanırız.


!!! Biz projemizde account oluşturuken transaction nesnesinin account kaydedilirken transaction da kaydedilmesini istedik.




!!!!!  @OneToOne veya @ManyToOne ========>  FetchType.EAGER

       Yani ilişkili entity bir tane olduğundan ön yükleme yapmak performans açısından bir sorun oluşturmaz.



       @OneToMany veya @ManyToMany ======> FetchType.LAZY

       Çünkü ilişkili entityler çok sayıda olması halinde ön yükleme yapacak olursak bu durum performans kaybına neden olur.
       Bunun için ihtiyaç olması halinde yüklemek daha doğru bir çözüm olur.



!!! cascade = CascadeType.ALL

Örnek vermek gerekirse Yazar ve Kitap ilişkisinde bir yazar silinirse onunla ilgili olan tüm kitaplar birlikte kaydedilir ve güncellenir.



--------------------------------------------------------------------Not-----------------------------------------------------------------------




--------------------------------------------------------------------Not-----------------------------------------------------------------------


                                            @OneToOne veya @ManyToOne ========>  FetchType.EAGER

                                            @OneToMany veya @ManyToMany ======> FetchType.LAZY


--------------------------------------------------------------------Not-----------------------------------------------------------------------



************   Account Table   **************

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL) // customer in içinde account bilgileri olucak fakat account nesnesi çağırıldığı zaman customer bilgilerini çekicek customer account çekicek ve loop select sorgu olucak bunun onune geçiyor fetchType.Lazy / cascade ise entitde yapılan herhangi bir işlemde eğer account a ait customer güncellenirse customer ds da güncelle => all crud hepsi.
    @JoinColumn(name = "customer_id", nullable = false)//foreign key
    private Customer customer;

    -- Account Tablosu ile Customer tablosu arasında ise ManyToOne İlişkisi olduğu için ManyToOne özelliğişn kullandık.
       fetch = FetchType.LAZY ==> account içinde bulunan customer bilgileri get işlemi yapıldıktan sonra atanır. ve selectloop un önüne geçilir.
       cascade = CascadeType.ALL ==> account üzerinde ki customer da değişiklik yapılırsa (all==>tüm crud işlemleri) customer tablosuna bu değişiklikler yansısın.


       @JoinColumn(name = "customer_id", nullable = false) ==> Account tablosunda customer customer_id foreign keyini kullanarak bağlansın

       private Customer customer ==> Customer ile Account bağlantısı kuracak olan nesne


    @OneToMany(mappedBy = "account",fetch = FetchType.EAGER)// Transaction entitysindeki account değişkeni ile bağlanır
    private Set<Transaction> transactions = new HashSet<>();

     -- aynı mantık üzerinden transaction tablosu bağlantısı gerçekleştirlildi.


####################################################################################################################################################################################################################################







-------------------------- Not ----------------------------

@Autowired Kullanma!!

Autowired yapıldığı zaman test edilebirlik düşer
örneğin service içinde repository kullanımını ele alalım
private final Repository repository
yani constructor ile oluştuurlduğu zaman uygulama ayağı kalkarken service anottaionunu görür constructor çağırır içerisinde autowired yerine kulandığımız final zorunlu repository nesnesini görür ve bunu oluşturmaya gider .
Daha sonra repsoitory nesnesi oluşturacağı zmaan extend edilen jpa ya gider ve zincir halinde devam eder biz bu zinciri takip edebilirz
fakat autowired kullanımında bir problem olursa zincir nerede kopuyor bilemeyiz .

-------------------------- Not ----------------------------






-------------------------- Not ----------------------------

Service lerin interface ini oluşturma !!!

 Bu eski bir yaklaşım java 7 den önce kullanılırdı.
 daha sonra ki kullanımlarda zaten java spring boot bizim yerimize dependency injection ve IoC ile interface e ihtiyaç duymadan injection ları yapabilmemiz sağlıyor. instance oluşturuyor ve her yerde kullanılabiliyor.
 Önceden servisler arası interface ler paylaşılırdı.
 spring boot  ile bu kullanıma gerek kalmadı.

-------------------------- Not ----------------------------


-------------------------- Not ----------------------------
Bir service yanlızca kendisine ait repository kullanmalı!!

Yani account service customerrepository e ihtiyacı varsa , örneğin customer db ye instert işlemi yapacaksa customer repository kullanmaz!
Customer Service kullanılır . Customer Service,  Customer Repository i kullanır ve Account Service gerekli işlemi yapar.

-------------------------- Not ----------------------------








####################################################################################################################################################################################################################################


# Dto'ların oluşumu

 Dto lar oluşturulurken her entitynin dto su oluşturuldu fakat dto lar üzerindeki ilişkili tablo bilgileri için
 örneğin account çağırıldığında customer bilgileri de gelsin
  customer çağırıldığında account bilgileri de gelsin istendiği için
    - AccountCustomerDto
    - CustomerAccountDto
     oluşturuldu.
     Account içinde transaction nesneleri tutuldu. transaction içinn ayrı dtolar oluşturulmadı
     çünkü transaction çağırımıyla iligli bir bilgi getirmi söz konusu değil.

     Account içinde AccountCustomerDto yerine  CustomerDto tutsaydık CustomerDto'da da Set<CustomerAccountDto> yerine Set<AccountDto> tutsaydık
     örneğin account çağırımında customer bilgileride getireleceği zaman customer da account bilgilerini çekmeye çalışacak tı bunu önlemek için Ayrı Dto lar oluşturduk
     fetch örneğinin dto hali gibi düşünülebilir.

####################################################################################################################################################################################################################################



####################################################################################################################################################################################################################################


# DtoConverter'ların oluşumu

   Her Dto nun converter sınıfı oluşturulmuştur.



***********   AccountDtoConverter  **************

@Component
     public AccountDto convert(Account from) {
        return new AccountDto(from.getId(),
                from.getBalance(),
                from.getCreationDate(),
                customerDtoConverter.convertToAccountCustomer(Optional.ofNullable(from.getCustomer())),
                Objects.requireNonNull(from.getTransactions())
                        .stream()
                        .map(transactionDtoConverter::convert)
                        .collect(Collectors.toSet()));
    }


 Method bize AccountDto dönecek.Çünkü Account nesnesi AccountDto ya dönüştürülecek.
 Account farom parametresi aldık
 Yeni bir AccaountDto nesnesi oluşturduk ve Account içerisindeki bilgileri from dan gelen bilgilerle eşleştirdik.
 !fakat account içerisindeki AccountCustomerDto nesnesine gelince bu nesneye accountdan gelen customer nesnesini atayabilmek için AccountCustomerDto nesnesinide convert etmemiz gerekli
 !Bundan dolayı bu kısımda AccountCustomerDtoConverter oluşturduk ve customer ı AccountCustomerDto ya çevirdik.
 Daha sonra accaountdan gelen Set<Transaction>transaction nesnesi ni accountDto içerisindeki Set<TransactionDto> ya çevirmek için transactionDtoConverter sınıfını kullandık
 !fakat bu nesne (Set<Transaction>) Set tipinde olduğu için içerisinde bulunan tüm transaction nesnelerini çevirip tekrar Set haline getirilmeli
 !Bundan dolayı  foreach kullanmayıp stream yapısı ile yapıyoruz.
 Objects.requireNonNull(from.getTransactions())
 .stream()
 .map(transactionDtoConverter::convert)
  .collect(Collectors.toSet())            ====> Set içerisindeki tüm transaction nesnelerini convert edip tekrar Set ledik



***********   CustomertDtoConverter  **************

    public AccountCustomerDto convertToAccountCustomer(Optional<Customer> from) {
        return from.map(f ->
                new AccountCustomerDto(f.getId(),
                        f.getName(),
                        f.getSurname())).orElse(null);
    }

    Bu Method AccountDtoConverter içerisindeki AccountCustomerDto için yazıldı ve yanlızda id ad soyad bilgisi döndürüyor




    public  CustomerDto convertToCustomerDto(Customer from) {
        return new CustomerDto(
                from.getId(),
                from.getName(),
                from.getSurname(),
                from.getAccounts()
                        .stream()
                        .map(customerAccountDtoConverter::convert)
                        .collect(Collectors.toSet()));


       Bu method ise Customer bilgisi çağırıldığında customer bilgileri hesap bilgileri ve işlem bilgilerini çevirmek için yazıldı.









!!!!!!  Diğer Converter larda aynı mantıkla oluşturuldu.


####################################################################################################################################################################################################################################





####################################################################################################################################################################################################################################

**********    Service'lerin oluşumu   ***********



Proje bizden customer eklememizi istemiyor. Var olan customer'lara account eklememizi istiyor.
Bu nedenle cutomer service bize customer bilgilerini getirmeli fakat dışarıya değil Account Service kullanımı için getirmeli.(protected yanlızca paket içerisinden erişimi)
Account Service yeni bir account eklemek istediğinde customer bilgilerini de kullanmak zorunda.Çünkü Account ve Customer ilişkisinde oneToMany-ManyToOne Customer sız account olamaz.
Account Service Customer Bilgilerini ve Transaction Bilgilerini de getirebilmeli

Account service den customer repository i kullanıp bilgi alabilirdik fakat bu yaklaşım kullanılmaz. Single Responsibility ilkesine aykırı. Her iş o işe ait service tarafından yapılır. Customer biligisi getirilmesi customer servisinin bir sürecidir.

- CustomerService

 Customer Service AccountService için customer bilgisi dönmeli bunun için aşağıdaki metodu yazdık.


 protected Customer findCustomerById(String id) {
        return customerRepository.findById(id)
                .orElseThrow(
                        () -> new CustomerNotFoundException("Customer could not find by id: " + id));
    }

    --Account Service den gelen customer id ile Customer tablosundan bu id nin ait olduğu customer'ı getir. Eğer null (boş ),böyle bir id ye sahip customer yok dönerse Custom olarak tanımlanan CustomerNotFoundException hatasını fırlat.

     CustomerNotFoundException =>
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public class CustomerNotFoundException extends RuntimeException {
            public CustomerNotFoundException(String message) {
                super(message);
            }
        }




*******  Account Service  ********

Bir Account oluşturulmak istendiğinde var olan customer tablosundan customer id si gelecek.

 public AccountDto createAccount(CreateAccountRequest createAccountRequest)
    {

        Customer customer = customerService.findCustomerById(createAccountRequest.getCustomerId());

        Account account = new Account(customer,createAccountRequest.getInitialCredit(),LocalDateTime.now());

        if(createAccountRequest.getInitialCredit().compareTo(BigDecimal.ZERO)>0)
        {
            //Transaction transaction = transactionService.createTransaction(account,createAccountRequest.getInitialCredit());

            Transaction transaction = new Transaction(account,
                    createAccountRequest.getInitialCredit(),
                    LocalDateTime.now()
                     );

            account.getTransactions().add(transaction);//      !!!! @@@@@@@   transaction repository.save burda denemiyeceği için add yaparak account u kaydettik böylece transaction da db ye eklendi.
        }

        AccountDto accountDto= converter.convert(accountRepository.save(account));


        return accountDto;

    }


    Customer customer = customerService.findCustomerById(createAccountRequest.getCustomerId());
    -- Öncelikle her account bir hesaba bağlı olacağı için apiden parametre olarak gönderilen customer id ve inital credit json bilgilerinden
       customerid bilgisi ile o id ye sahip olan customer bulduk

    Account account = new Account(customer,createAccountRequest.getInitialCredit(),LocalDateTime.now());
     -- Daha sonra o müşteriye ait yeni bir account oluşturmak istediğimiz için account entity deki customer nesnesine bulduğumuz customer ı ve apiden gelen initalcredit bilgisini göndererk yeni bir account oluşturduk.


   if(createAccountRequest.getInitialCredit().compareTo(BigDecimal.ZERO)>0)
   {
            //Transaction transaction = transactionService.createTransaction(account,createAccountRequest.getInitialCredit());

            Transaction transaction = new Transaction(account,createAccountRequest.getInitialCredit(),LocalDateTime.now());
            account.getTransactions().add(transaction);
   }

   -- Daha sonra apiden gelen initialCredit bilgisi 0 dan büyükse bir transaction işlemi oluşturmamız gerektiği için
     gerekkli kontrolü yaptıktan sonra transaction'u transactionService den bağlanıp transactionRepositoryde oluşturmadık.
     Çünkü transaction oluştuktan sonra veritabanına kaydedemeyiz. Bunun sebebi transaction bir Accounta ihtiyaç duyar bundan dolayı
     burada transaction nesnesini oluşturup daha sonra account nesnesindeki Set veri yapısı tipinde saklanan transaction nesnesine ekledik // Transaction transaction = new Transaction(account,createAccountRequest.getInitialCredit(),LocalDateTime.now());
     transaction repository.save burda denemiyeceği için add yaparak account u kaydettik böylece transaction da db ye eklendi.

     AccountDto accountDto= converter.convert(accountRepository.save(account));
     -- Daha  sonra account nesnesini kaydettiğimiz için Set<>transaction veri yapısıda veritabanına kaydolmuş oldu.




*******  Customer Service  ********

Bu Methodda Servisinde id si verilen kulanıcıyı ve bu kullanıcı bilgilerini(account,transactions,name surname id) görüntüledik

public CustomerDto getCustomerInformation(String customerId)
    {
        Customer customer= customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer could not find by id: " + customerId));
        CustomerDto customerDto = customerDtoConverter.convertToCustomerDto(customer);
        return customerDto;

    }



Bu Methodda Servisinde  tüm kullanıcıları ve bu kullanıcı bilgilerini(account,transactions,name surname id) görüntüledik

public List<CustomerDto> getAllCustomer()
    {
        List<Customer> customers=customerRepository.findAll();

                List<CustomerDto> customerDtoList =  customers
                .stream()
                .map(customerDtoConverter::convertToCustomerDto)
                .collect(Collectors.toList());

        return customerDtoList;
    }


    CustomeLitsesi olduğu için tüm customer'ları customerDtoConverter nesnesi içerisindeki  convertToCustomerDto fonksiyonu ile convert işlemi yaptık.
    Bu convert işlemini foreach lede yapabilirdik fakat stream sınıfı daha rahat.

        List<CustomerDto> customerDtoList=new ArrayList<>();
        for (Customer customer:customers) {

            customerDtoList.add(customerDtoConverter.convertToCustomerDto(customer));
        }



*******  Transaction Service  ********


 public TransactionDto createTransaction(CreateTransactionRequest createTransactionRequest)
    {


        Account account = accountService.getAccount(createTransactionRequest.getAccountId());
        Transaction transaction= new Transaction(account,createTransactionRequest.getAmaount(), LocalDateTime.now());
        //Account.getTransaction().add() demeye gerek yok çünkü transaction save edildiğinde account a da değişiklik yansıyacaktır.
        TransactionDto transactionDto = transactionDtoConverter.convert(transactionRepository.save(transaction));

        return transactionDto;

    }

    Bu serviste varolan hesaba yeni bir transaction ekleme işlemi ve eklenen transaction'u geri dönme işlemi gerçekleştirdik.
    transaction ekleme işlemini yaparken transactionun Set tipinde olması ve foreign key olan tablo olmasını göz önünde bulundurduğumuz zaman transaction eklerken account daki transaction nesnesnine ekleme yapmadık save etmemiz iki tarafdaki değişiklik için yeterli
    //Account.getTransaction().add() demeye gerek yok çünkü transaction save edildiğinde account a da değişiklik yansıyacaktır.


####################################################################################################################################################################################################################################












####################################################################################################################################################################################################################################


###########  Unit Test  ###########

unit test: programımızdaki her unitin yani fonksiyonun sahip olduğu her mantıksal noktayı test ettiğimiz test tipi.

Unit Test, bir yazılımın en küçük test edilebilir bölümlerinin, tek tek ve bağımsız olarak doğru çalışması için incelendiği bir yazılım geliştirme sürecidir.
Unit Test yazılım testinin ilk seviyesidir ve entegrasyon testinden önce gelir.


  Neyin Testlerini Yazarız? işlem olan mantıksal her nokta örneğin modelin testi yazılmaz. çünkü herhangi bir işlem yok.

 -- Servis ler
 -- Converter lar
 -- Controller lar


Örneğin AccountService test edelim
public AccountDto createAccount(CreateAccountRequest createAccountRequest)
    {

        Customer customer = customerService.findCustomerById(createAccountRequest.getCustomerId());
         ...
         ...

         }
ve bu servis Customerservice i çağırıyor olsun . Bu çağırımda hangi fonksiyonu kullanıyorum findCustomerById



                      findCustomerById
                    ---------------------->
AccountServiceTest                            CustomerServicesMock
                    <-----------------------
                        custom Nesnesi dön

Simulasyonunu yapıyoruz. AccountService ten CustomerServices çağırmıyoruz.Nasıl davranacağını söylüyorum.
Unit test : Test ettiğim sınıfın sınıfın içerisindeki fonksiyonunu test ettiğim
Mock Test: Sınıflarımdaki Fonskiyonların dışarıyla olan bağlantılarını simüle ettiğim , mock ladığım teste mock test denir.
Unit: Test ettiğmiz servis,controller,=> örneğin AccountService Unit dir.Bunun dışındaki herşey unit dışında oluyor onları test etmiyoruz.

!! private ve protected methodlar test EDILMEZ !!
!! void değer dönen methodlar test EDILMEZ !!
!! Bunlar dışındaki tüm methodların her bir durumu için ayrı ayrı test senaryoları yazılır. !!


____TEST NASIL YAZILIR____
Test Etmek isteiğimiz sınıfın içerisinde ctrl+shift+T tuşlarına bastık.
Create new test dedik.
JUnit4
Adı : AccounServiceTest
SuperClass yok
Destination otomatik atadı
setup Methodu seçtik
Ok

   Before Methodu: Her test senaryosu çalıştırılmadan önce çalıştırılan method
    İçerisinde genelde her senaryoda ortak kullanılanlar yazılır
    Ve Hangi senaryo test edilecekse onun objesi oluşturulur. Ki test sınıfı içerisinde kullabılabilsin.



    AccountServiceTest classının test edeceğimiz  AccountService classıyla bağlantısı yok. Dolayısıyla öncellikle bağlantı kurulur.
    Test etmek istediğimiz Classın objesi oluşturulur:
     private AccountService accountService

     Daha sonra test edilecek sınıfın kullanması gereken objeler de buraya eklenir.(private final yapılmasının sebebi aslında budur. Test sınıfında ihtiyacımız olan bu nesneleri service(accountService) objesine ınject edebilelim.)

    Before içerisinde accountService objesi oluşturulduğu (new) zaman içerisine(consturctor) parametre istediği nesneleri göndermem gerekli.
    örneğin CustomerService nesnesi parametresi alması gerekli. Biz bu nesneyide before içeriisnde new lersek (oluşturusak) bu nesnede parametreler alması gerekecek.Böyle böyle uzar gider.
    Bundan dolayı JUnit bize kolaylık sağlıyor.
 !! Mockito kütüphanesi kullanarak yalancı servis oluşturabilmemizi sağlıyor   ===> accountRepository = Mockito.mock(AccountRepository.class);
    Mocklama işlemiyle birlikte accountRepository classının tüm özellikleri davranışları kontrolü testin eline geçebildi.

    Ve daha sonra oluşturduğumuz mock nesnelerini test edeceğimiz class nesnesinin parametrelerine atadık(cunstructor)
    accountService = new AccountService(accountRepository,customerService,converter);
    artık accountService in her metodu çağırılabilir.

    Artık Test senaryolarını yazabliriz. Bunun için Test Annatation u eklenir.(junit)
    Kurallar:
    * test senaryosu methodu public olmalı
    * test senaryosu methodu void olmalı. Geri birşey dönmemeli
    * test senaryolarının genel yazım kurallarına dikkat edilmeli
      :method adı when ile başlar



      ## İlk test methodu yazımı

      whenCreateAccountCalledWithValidRequest_itShouldReturnValidAccountDto()
      createAccount Geçerli istekle çağırıldığında geçerli accountDto dönmeli senaryosu  / Fonksiyonun kendisinin testi ( parametre olarak geçerli bir istek parametresi alır ve geriye accountDto döner)
       Geçerli istek parametresi olarak CreateAccountRequest alır.Bu nedenle bu parametre nesnesi oluşturulur.
       Bu geçerli request sonucu testiydi şimdi mockların test edilmesi gerek


        Customer customer = customerService.findCustomerById(createAccountRequest.getCustomerId());

       İlk Mockumuz bize custom nesnesi dönen bir methodu geçerli bir parametre göndererek çağırılması.
       geri dönen bir customer olduğu için test edilebilirlik açısından bir customer nesnesi oluşturmalıyız. Yoksa testi deneyecek bir customer nesnemiz olmaz.
                 Customer customer = Customer.builder()
                .id("12345")
                .name("Omer")
                .surname("Faruk")
                .build();

                ==>findCustomerById(createAccountRequest.getCustomerId() buradaki getCustomerId() gönderip test edebilmek için customer oluşturdukj


       Daha sonra Mock olarak oluşturulan servisin davranışını belirtmemiz gerekli.
       => Mockito.when(customerService.findCustomerById("12345")).thenReturn(customer);
         : customerService.findCustomerById "12345" parametresiyle çağırıldığı zaman ( request nesnesi oluşturduğumuz createAccountRequest in customerId si) customer nesnesini dön
           :Böylece Junit aşağıdaki satıra geldiği zaman Mockito ile yalancı servisi çağırıp davranışını belirliyor. Davranışı: getcustomerById her "12345" ile çağırıldığında her zaman customer dönecek.
            Customer customer = customerService.findCustomerById(createAccountRequest.getCustomerId());




















     */





}

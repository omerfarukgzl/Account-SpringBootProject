package com.Omer.Account.notes;

public class NotesTest {
    /*
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
JUnit5
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
     #     CreateAccountRequest createAccountRequest = new CreateAccountRequest("12345",new BigDecimal(100.0));  //parametre isteği




        Customer customer = customerService.findCustomerById(createAccountRequest.getCustomerId());
     # İlk Mockumuz bize custom nesnesi dönen bir methodu geçerli bir parametre göndererek çağırılması.
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



           Account account = new Account(customer,createAccountRequest.getInitialCredit(),LocalDateTime.now());
      #  Bir Sonraki Mock olarak geriye account döndüren Account oluşturduk
        =>
         Account account = Account.builder()
                .id("1234")
                .balance(new BigDecimal(100.0))
                .creationDate(LocalDateTime.now())
                .customer(customer)
                .build();






    ################   AccountServiceTest #########################

    Önemli:!! save methodunda null hatası alıyordum. when save null dönüyordu => any(Account.class) ile çözdüm

    when(accountRepository.save(account).thenReturn(account);  ==> first code
     when(accountRepository.save(any(Account.class))).thenReturn(account); ==> last code



    @Test
    public void whenCreateAccountCalledWithValidRequestAndInitialCreditMoreThanZero_itShouldReturnValidAccountDto()//createAccount Geçerli istekle çağırıldığında geçerli accountDto dönmeli senaryosu  / Fonksiyonun kendisinin testi ( parametre olarak geçerli bir istek parametresi alır ve geriye accountDto döner)
    {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("customer-id", new BigDecimal(100.0));

        when(customerService.findCustomerById("customer-id")).thenReturn(customer);

        Account account = generateAccount(customer);

        Transaction transaction = new Transaction(
                "transaction_id",
                TransactionType.INITIAL,
                createAccountRequest.getInitialCredit(),
                LocalDateTime.now(),
                account);

         account.getTransactions().add(transaction);
        // AccountCustomerDto accountCustomerDto = generateAccountCustomerDto(customer);

        AccountCustomerDto accountCustomerDto = new AccountCustomerDto("customer-id",
                "customer-name",
                "customer-surname");

        TransactionDto transactionDto = new TransactionDto("transaction_id", TransactionType.INITIAL,  new BigDecimal(100.0), getLocalDateTime()); // account Dto transaction Dto ya sahip oludğu için oluşturduk
        AccountDto accountDtoExpected = new AccountDto("account_id", new BigDecimal(100.0), getLocalDateTime(), accountCustomerDto, Set.of(transactionDto));

         when(accountRepository.save(any(Account.class))).thenReturn(account);
         when(converter.convert(account)).thenReturn(accountDtoExpected);
        System.out.println(account.toString());
         AccountDto result = accountService.createAccount(createAccountRequest); // null Dönüyor hata var!!

        //System.out.println(result.toString());
        System.out.println(accountDtoExpected.toString());
        System.out.println(account.toString());
        System.out.println(result.toString());
        assertEquals(result,accountDtoExpected);
    }



      Öncelikle  Test edeceğimiz fonksiyona gönderilen obje parametresinin nesnesini oluşturduk.

      Daha sonra  diğer metodlarda da customer nesnesini kullanacağımız için method dışında da cusrtomer nesnesini oluşturduk.

     Nesne oluşturma işlemlerinde test ettiiğimiz için parametre olarak göndereceğimiz verileri isim formatında girdikki test ederken isim olarakk yazması ve okuması kolay olsun.

     Daha sonra bize customerService den fonskiyona parametre olarak gönderdiğimiz nesnenin customer id si ile birlikte gelecek customer'ın  dönmesini istediğimiz methodu yazdık. Bu method bize geriye customer döndü

     Daha sonra Account nesnesi oluşturduk. nesnenin içine gönderidğimiz parametreleri yine isime dayalı olarak verdik. Nesne Oluşturma işlemi lombok dan kullandığımız allargsconstructor annotationu olan tüm parameterlerin verildiği constructor metdou ile oluşturduk.

     Daha sonra Transaction nesnesini oluşturduk.

     Daha sonra oluşturduğumuz transaction nesnesini account nesnesine ekledik.

     Dahs sonra Account nesnesinin AccountDto ya dönüşümü sırasında accountDto içerisinde AccountCustomerDto ve transactionDto nesneleri olduğu için (Aslında account da accountCustomer var fakat account accountDtoya dönüştüğü zaman accountCustomer da accountCustomerDto ya transaction da transactionDto ya dönüşür)

     Daha sonraki işlemlerde bu account nesnesini db ye kaydedip accountDto döneceğimiz için AccountDto nesnesi oluşturduk.
     Account Dto nesnesi oluşumunda account parameterlerinin aynısını verdik. çünkü bizim amacımız test etmek. Yani account nesnesi accountDto nesnesine dönüşmüş gibi senaryoluyourz.
     Account Dto da accounta eklenen transactionun transactionDto dönüşmüş nesnesnini de set.of ile parametre olarak gönderidk.)

     Daha sonra accountRepository ye gidip account nesnenini db ye kaydeden ve geriye account nesnesi dönen mocku yazdık.
     Daha sonra dönen accountu accoundto ya convert eden ve geriye accounDto dönen mockyu yazdık.

     En sonunda Bu fonksiyonun dönen değerini resultta tuttuk

     Ve result ile AccountDto yu karşışaltırdık

     The End..











*/
}

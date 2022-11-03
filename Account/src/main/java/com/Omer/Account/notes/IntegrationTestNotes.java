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

Uygulamanın gerçek çalışır halinin akışını düşünmeliyiz.
Öncelikle bu senaryoyu işletebilmem için veritabanına customer kaydetmem gerekiyor.
Çünkü projemizde var olan customer'a hesap ekleme fonskiyonları yazıldı.
Zaten gerçek uygulama ayağa kalkarken customer oluşturulur ve db ye kaydedilir

        Customer customer = customerRepository.save(new Customer("","Omer","Faruk", new HashSet<>()));
        (Neden Unit Test deki Customer customer = getCustomer() (TestSupport sınıfınfaki method) kullanmadık ?
        Çünkü mock lamıyoruz gerçek verilerle haraket edeceğimiz için gerçekten db ye kaydedip o id ler le işlem yapacağız.)

Daha sonra istek göndereceğimiz parametreyi oluşturyoruz.(CreateAccountRequest)













  */






}

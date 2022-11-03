package com.Omer.Account.notes;

public class ExcepitonHandler {

    /*
####################################################################################################################################################################################################################################
*****************   ExcepitonHandler   ******************

Exception Handler RestContollerAdvice Etiketi ile yazılır.
Bu ise uygulama içerisinde throw edilen tüm exceptionları yakalayıp http response üretir.

Hatasız bir kod yazma süreci neredeyse imkansız, ister istemez hatalar ile karşılaşabiliriz
Bu karşılaşacağımız hataları java exception handling kullanımı ile yakalayarak sorunumuzun nereden ve neden kaynaklı olduğunu öğrenebiliriz.

Exception Türleri :


Checked Exception: Derleme sırasında kontrol edilen istisnalardır. Derleme zamanında istisna yakalanıp müdahale edilmez ise hata verecektir, müdahalemizi ise try-catch blokları sayesinde yaparız. Bu soruna örnek vermemiz gerekirse ağ bağlantısının kopması gibi sorunlar ile karşı karşıya kalmamız gibi.

Unchecked Exception: Bu tür istisnalarda çalışma anı istisnaları da denmektedir. Yani çalışma sürecinde meydana gelen istisnalardır. Bu tür istisnalar mantık hataları veya bir API’nin uygunsuz kullanımı gibi programlama hatalarını içermektedir. Bu tür istisnalar derleme sırasında göz ardı edilir.

Eror: Bu türler aslında istisna değillerdir. Ancak kullanıcının veya programcının kontrolü dışında gerçekleşen sorunlardır.



VirtualMachineError :
JVM’nin çalışmasını etkileyen durumları inceler.

AWTError :
Grafik arayüze ait hataları inceler.

OutOfMemoryError :
Bellek yetersizliği durumlarını inceler.

ClassNotFoundException :
Olmayan bir dosyaya erişme istediği durumlarını inceler.

IOException :
Giriş çıkış işlemlerindeki istenmeyen durumları inceler.

ArithmeticException:
Herhangi bir sayıyı sıfıra böler isek bu istisna türü ile karşılaşırız.

NullPointerException :
Herhangi bir nesneye null referanslı bir değişken ile ulaşılmaya çalışılan durumlarda fırlatılır.

IllegalArgumentException :
Metotlara geçersiz argüman atamalarında fırlatılır.

ArrayIndexOutOfBoundsException:
Dizi içerisinde tanımlanmayan bir değeri çağırdığımızda bu sorun ile karşılaşırız.

NumberFormatException:
Herhangi bir değişkenin yanlış biçimlendirilmesi istisnayı meydana getirir.


         ----------------------istek(Kullanıcı)---------------
        \/                      ||                           |
  javax.validation        -----------------------------      |
         |               |     \/                     |      |
         |--------------------Contoller              \/     \/
                       @Valid   ||               RestContollerAdvice
                                \/                          /\
                              Service-------------------------
                                ||
                                \/
                            Repository
                                ||
                                \/
                                Db


    Normal Akış:
    Kullanıcı istek attığında bu isteği controller karşıladı ve controller isteği işledi ve service e gönderdi.
    Service üzerinde işlem sırasında hata oldu ve service bu hatayı controller'a bildirir.
    Controller da bu hatyala ilgili işlem yapacaksa yapar ve biter . Bu normal akıştır

    RestControllerAdvice Kullanarak :
     Kullanıcı istek attığında bu isteği controller karşıladı ve controller isteği işledi ve service e gönderdi.
    Service üzerinde işlem sırasında hata oldu ve RestControllerAdvice araya girer ve hatada oluşumundan dolayı araya girerek akışı keser.
    Hatayı kendine tanımlanan hatalardan bilir. Hatayı kullanıcıya http response döner.
    Normal akış thread' ölür ve kendine yeni thread açarak kullanıcıya hatayı http respone olarak döner.


    Validation sürecinde de exception handler ın kullanımı vardır.
    Validation request ile controller a bağlıdır
    javax.validation : requestin validsoyanunu sağlayan kütüphanedir.@Valid Annotation'u konulan tüm requestlerde validation işlemi başlar.
    Eğer validation işleminde aykırı bir durum olduğunda burada da RestControllerAdvice devreye girer.
    RestControllerAdvice daha istek controller'a girmeden araya girer ve hatayı üretir.(örneğin customer id boşmu dolumu bunun kontrollerini artık controller da yapmaya gerek yok kod kalablaığından kurtulduk.)


 ValidExcepiton:

    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatus status,
                                                                  @NotNull WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error ->{
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


handleMethodArgumentNotValid Methodu ResponseEntityExceptionHandler içerisinde barınır.
Bu method ArgumentNotFoundException hatasını handler eder ve ınternalServerError dönder (500 döner ) fakat bu kullanıcı için tam bilgilendirici değil.
Dolayısıyla kullanıcıyı tam manası ile bilgilendirmek için bu methodu override ediyoruz.
bizim örneğimizde createAccountRequest sınıfı verileri üzerinden request sağlıyoruz ve override ettiğmiz bu method bu verilerin validation hatalarını hangi fieldlarda ayrkırılık varsa handler eder.


Örneğimizdeki createAccountRequest Validation hatalarını handler edebilmek için request verilenerine validation (pom.xml strater-validation dependency) kutuphanesinin özelliklierni kullanacağız.
örneğin string boş olamaz (Not Blank)  / initialCredit 0 dan küçük olamaz (min value)
Daha sonra bu hataların istektek anındaki validation kontrolünü Controller da @Valid Annotationu ile sağlıyoruz.(@Valid RequestBody CreatAccpuntRequest createAccountRequet)

Aynı validationu TransactionController içinde gerçekleştirdik.








####################################################################################################################################################################################################################################

*/
}

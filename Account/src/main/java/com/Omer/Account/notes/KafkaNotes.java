package com.Omer.Account.notes;

public class KafkaNotes {/*


-- Kafka Nedir:

    Kafka bir MESSAGE BROKER sistemidir. Yani bir mesajı alır bir yere depolar ve onu okuyacak olanlarda bu mesajları okur.
    Linkedin tarafındna oluşturulmuştur.
    Sacala ve JAva ile yazılmıştır.
    İçerisindeki mesajlar INDEX yapısında göre yazılır ve okunur. Bundan dolayı oldukça hızlı bir sistemdir

-- Kafka Nerelerde Kullanılabilir:
    Web Sayfası etkinlik izlemelerinde
    İzleme sistemlerindeki metriklerde
    Log Toplama sistemlerinde
    Stream proccessing işlemlerde

-- Kafka Avantajları:

    Hızlıdır : Yüksek trafikde, düşük gecikme
    Ölçeklenebilir: Node ve partitionlar ile ölçeklenebilir
    Güvenilir: Ölçeklendirilmiş ve hata tolere edilebilir.
    Yüksek dayanıklılık: Kafka mesajları disk üzerinde depolar ve bunları silmez. Bu sayede mesaj kaybı olmaz.
    Açık kaynaklıdır: Apache 2.0 lisansı ile açık kaynaklıdır.


-- Kafka Bileşenleri:

        Producer: Mesaj üreten uygulamalar
        Consumer: Mesaj alan uygulamalar
        Broker: Mesajları depolayan ve yöneten uygulamalar(sunucunun kendisi)
        Topic: Mesajların depolandığı yerler
        Partition: Topiclerin içerisindeki mesajların depolandığı yerler
        Offset: Partitionlardaki mesajların sırası

-- Kafka Nasıl Çalışır:

    Öncelikle bir topic oluşturulur. Bu topiclerin içerisine mesajlar yazılır.
    Bu mesajlar partitionlara yazılır.
    ve geriye producer'a bir offset döner.(mesaj pozisoyon bilgileri)
    Consumer ise bir topic'e join olur ve bu topic üzerindeki mesajları sırası ile bitene kadar okur.

       ( Producer mesaj üretir ve bunu bir topic'e gönderir.
        Broker mesajı alır ve bunu bir partition'a gönderir.
        Consumer mesajı alır ve işler.)

  --Multi Partition:
         Peki belirli şartlara göre mesajları aynı topicte farklı farklı yerlerde tutmak istemiyorsak ne yapacağız?
         Bunun için partitionlar kullanılır. Partitionlar topiclerin içerisindeki mesajların depolandığı yerlerdir.
         Partitionlar sayesinde mesajları farklı yerlerde tutabiliriz.

         Örneğin uygulama loglarını tutmak istiyoruz. Bunun için bir topic oluşturduk.
         Bu topicin içerisinde 2 partition oluşturduk.
          ve dedikki partion1'e cpu,memory,disk error 'ları partition2'ye de application ve Db error 'ları yazalım.
          Bu partition ayarlamasını producer tarafında yaparız.(error type x ise a partitionuna yaz)

         Consumer'a ise ilgili topic üzerindeki
         ilgilendiği partitionları okumasını söyleriz.





-- Kafka'da Queue ve Pub/Sub modelleri:

    Queue: Mesaj 1 kere publish edilir ve 1 kere consumer tarafından okunur daha sonra queue üzerinden silinir.
    Pub/Sub: Mesaj 1 kere publish edilir ve 1 den fazla consumer tarafından okunabilir. Queue üzerinden silinmez. Bu sayede bir sonraki consumer tarafından okunabilir.
                (Radyo yayını gibi düşünülür. Bir radyo yayını 1 kere yayınlanır ve 1 den fazla kişi dinler.
                Yayın yapılırken önceki yayınlar dinlenilmez . O anki yyaın dinlenilir)

    Kafka'da Queue ve Pub/Sub modelleri birlikte kullanılabilir.

--Kafka'da Consumer Group:


    Partionların paralel olarak işlenmesi için kullanılır.
    Genelde 1 Consumer 1 Partition okumakla sorumludur.
    Fakat bu durum consumer grupları ile değişir.

        Queue gibi davranma:
            Bir cunsomer group oluşturulur.
            Consumer group topic'e bağlanır.
            Consumer group içerisindeki cunsomerlar partition lara bağlanır.
            Consumer 1 partition1 okur. Consumer 2 partition2 okur.
            Bu sayede paralel olarak işlem yapılır. Paralel okuma gerçekleşir. (Aynı anda)

        Pub/Sub gibi davranma:
            3 Cunsomer group oluşturulur.
            Her Consumer group da 1 consumer oluşturulur.
            Her bir consumer group aynı topic'e bağlanır.
            Partition içerisindeki mesajlar 3 consumer group tarafından okunur.
            her bir mesajı bir cunsomer group okur. ve o mesaj gider



-- Topic Bazlı Dağıtık Sistem ( Disturbuted System)

                Kafka'da bir topic oluşturulur.
                Bu topic 3 broker üzerinde depolanır.
                Bu brokerlara 3 farklı sunucu bağlanır.
                Bu sunucular 3 farklı data center'da bulunur.
                Bu sayede topiclerin birbirinden bağımsız olarak depolanması sağlanır.
                Bu sayede bir sunucu veya broker down olduğunda diğer sunucular ve brokerlar çalışmaya devam eder.
                Bu sayede sistemlerin yüksek mevcutluk ve dayanıklılığı sağlanır.


        Master:  Leaader ====> ana broker
        Slave :  Follower ==> Instance Broker
        Topik Bazlı dağıtık sistem de master down olduğunda slave lere veri yazılayamayacağı için veri kaybı olur.

        Bunu önlemek için partition bazlı dağıtık sistem kullanılır.
        Bu sistemde master topic içeridsindeki partitionlardan seçilir.


                       9092 port                                 9093 port
                       Topic1                                      Topic2

             partirion1   partition2                       partition1   partition2
             (master)     (slave)                         (slave)      (master)
                |            /\                              /\                 |
                |            |                               |               |
                |            |------------------------------------------------
                |                                            |
                |--------------------------------------------



-- Peki veriler nerde tutuluyor: Zookeeper


    Zookeeper: Kafka 'da ki verilerin tutulduğu yerdir.
    Kafka Cluster'a bağlıdır. Yani zookeper kafkayı sürekli dinler gibi düşünülebilir.
    Kafka cluster'ı dinler ve kafka cluster'ı dinlerken verileri tutar.

======> Gossip Protokolü
    Kim master kim salve bizim bilmemize gerek yok . zookeper yapar bunu.
    Biz sadece partition2 ye yaz deriz o master'a yazar slave de clone lanır



Kafka Starting Configuration:

    Kafka kurulumu biraz karmaşık olduğu için docker üzerinde çalıştırılmasını  gerçekleştireceğiz.
    Docker üzerinde çalıştırmak için zookeper ve kafka image lerinin configurationları ayarlandı
    kafka enviroment variable ları ayarlandı.
    Docker compose ile zookeper ve kafka containerları çalıştırıldı.

    Uygulamamızda kafka dependency eklendi
            <dependency>
                <groupId>org.springframework.kafka</groupId>
                <artifactId>spring-kafka</artifactId>
            </dependency>


    Daha sonra application.properties dosyasında consumer ve producer özelliklierini ayarladık.



    Daha sonra uygulamamızda producer olacak yerde  kafka ya  göndeirlecek mesaj yapısı tanımlandı
            private final KafkaTemplate<String, String> kafkaTemplate;
                String String verilmesinin sebebi
                ilk string group id
                ikinci string ise kafkaya push lanacak mesaj tipi (biz string bir tipte mesaj basmak istedik)

    Daha sonra consumer olacak yeri ise notoication service olarak belirledik ve
        Kafkayı dinleyip istenilen user'a bir mail göndermesini istedik
            @Service
            public class NotificationService {

                @Autowired
                private JavaMailSender mailSender;
                private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

                @KafkaListener(
                        topics = "transfer-notification",
                        groupId = "group-id")
                public void consume(String message){

                    String from = "saupay54@gmail.com";
                    String to = "guzelomerfaruk9@gmail.com";

                    SimpleMailMessage mess = new SimpleMailMessage();

                    mess.setFrom(from);
                    mess.setTo(to);
                    mess.setSubject("Omer Spring Boot");
                    mess.setText(message);

                    mailSender.send(mess);

                    logger.info(String.format("Message receiver \n %s", message));
                }
            }


















*/
}

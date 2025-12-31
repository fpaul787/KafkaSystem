package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
  private static final Logger logger = LoggerFactory.getLogger(App.class);
  private static final Random random = new Random();

  // Arrays of sample data
  private static final String[] firstNames = { "John", "Jane", "Michael", "Emily", "David", "Sarah", "James", "Jessica",
      "Robert", "Ashley" };
  private static final String[] lastNames = { "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller",
      "Davis", "Rodriguez", "Martinez" };
  private static final String[] makes = { "Toyota", "Honda", "Ford", "Chevrolet", "Nissan", "BMW", "Mercedes", "Audi",
      "Hyundai", "Mazda" };
  private static final String[] models = { "Camry", "Civic", "F-150", "Silverado", "Altima", "X3", "C-Class", "A4",
      "Elantra", "CX-5" };
  private static final String[] colors = { "White", "Black", "Silver", "Gray", "Red", "Blue", "Green", "Brown", "Gold",
      "Orange" };
  private static final String[] paymentMethods = { "Cash", "Finance", "Lease", "Credit Card", "Bank Transfer" };
  private static final String[] dealerships = { "AutoMax", "CarWorld", "Premier Motors", "City Auto", "Elite Cars",
      "Metro Motors", "Sunshine Auto", "Victory Motors" };

  private static final int NUM_CAR_PURCHASES = 1;
  public static void main(String[] args) throws InterruptedException, ExecutionException {
    try {
      String topic = "topic_car_purchases";
      final Properties config = readConfig("client.properties");

      // produce(topic, config);
      CarPurchase[] carPurchases = new CarPurchase[NUM_CAR_PURCHASES];
      for (int i = 0; i < carPurchases.length; i++) {
        carPurchases[i] = generateRandom();
      }
      produceCarPurchase(topic, config, carPurchases);
      // consume(topic, config);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Properties readConfig(final String configFile) throws IOException {
    // reads the client configuration from client.properties
    // and returns it as a Properties object
    if (!Files.exists(Paths.get(configFile))) {
      throw new IOException(configFile + " not found.");
    }

    final Properties config = new Properties();
    try (InputStream inputStream = new FileInputStream(configFile)) {
      config.load(inputStream);
    }

    return config;
  }

  public static void produce(String topic, Properties config) throws InterruptedException, ExecutionException {
    // sets the message serializers
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    // props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CarPurchaseSerializer.class.getName());

    // creates a new producer instance and sends a sample message to the topic
    String key = "key";
    String value = "value";
    Producer<String, String> producer = new KafkaProducer<>(config);
    ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
    RecordMetadata metadata = producer.send(record).get();
    logger.info("Message sent to topic {} partition {} with offset {}", metadata.topic(),
        metadata.partition(), metadata.offset());
    System.out.println(
        String.format(
            "Produced message to topic %s: key = %s value = %s", topic, key, value));

    // closes the producer connection
    producer.close();
  }

  public static void produceCarPurchase(String topic, Properties config, CarPurchase[] carPurchases) throws InterruptedException {
    // sets the message serializers
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

    try (Producer<String, CarPurchase> producer = new KafkaProducer<>(config)) {
      for (CarPurchase carPurchase : carPurchases) {
        ProducerRecord<String, CarPurchase> record = new ProducerRecord<>(
            topic,
            carPurchase.getTransactionId(),
            carPurchase);

        // Send message synchronously
        try {
          RecordMetadata metadata = producer.send(record).get();
          logger.info("Transaction sent - ID: {}, Customer: {}, Car: {} {} {}, Price: ${}, Partition: {}, Offset: {}",
              carPurchase.getTransactionId(),
              carPurchase.getCustomerName(),
              carPurchase.getYear(),
              carPurchase.getMake(),
              carPurchase.getModel(),
              carPurchase.getPurchasePrice(),
              metadata.partition(),
              metadata.offset());
        } catch (ExecutionException | InterruptedException e) {
          logger.error("Error sending transaction: {}", carPurchase.getTransactionId(), e);
          Thread.currentThread().interrupt();
        }
        Thread.sleep(100);
      }
    } catch (Exception e) {
      logger.error("Error in Kafka producer", e);
      System.exit(1);
    }
  }

  public static void consume(String topic, Properties config) {
    // sets the group ID, offset and message deserializers
    config.put(ConsumerConfig.GROUP_ID_CONFIG, "java-group-1");
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    // creates a new consumer instance and subscribes to messages from the topic
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(config);
    consumer.subscribe(Arrays.asList(topic));

    while (true) {
      // polls the consumer for new messages and prints them
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<String, String> record : records) {
        System.out.println(
            String.format(
                "Consumed message from topic %s: key = %s value = %s", topic, record.key(), record.value()));
      }
    }
  }

  // Static method to generate a random car purchase
  public static CarPurchase generateRandom() {
    // Generate random data
    String transactionId = UUID.randomUUID().toString();
    String customerId = "CUST-" + String.format("%05d", random.nextInt(10000));
    String firstName = firstNames[random.nextInt(firstNames.length)];
    String lastName = lastNames[random.nextInt(lastNames.length)];
    String customerName = firstName + " " + lastName;
    String customerEmail = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@email.com";

    String make = makes[random.nextInt(makes.length)];
    String model = models[random.nextInt(models.length)];
    int year = 2015 + random.nextInt(10); // Year between 2015 and 2024
    String color = colors[random.nextInt(colors.length)];
    String vin = generateVIN(random);

    BigDecimal purchasePrice = new BigDecimal(15000 + random.nextInt(85000)); // Price between $15,000 and $100,000
    String paymentMethod = paymentMethods[random.nextInt(paymentMethods.length)];

    String dealershipId = "DEALER-" + String.format("%03d", random.nextInt(999));
    String dealershipName = dealerships[random.nextInt(dealerships.length)];
    String salesRepId = "SALES-" + String.format("%04d", random.nextInt(9999));

    LocalDateTime purchaseDate = LocalDateTime.now().minusDays(random.nextInt(365)); // Within last year

    return new CarPurchase(
        transactionId, customerId, customerName, customerEmail,
        vin, make, model, year, color, purchasePrice,
        dealershipId, dealershipName, paymentMethod,
        purchaseDate, salesRepId);
  }

  private static String generateVIN(Random random) {
    StringBuilder vin = new StringBuilder();
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    for (int i = 0; i < 17; i++) {
      vin.append(chars.charAt(random.nextInt(chars.length())));
    }
    return vin.toString();
  }
}

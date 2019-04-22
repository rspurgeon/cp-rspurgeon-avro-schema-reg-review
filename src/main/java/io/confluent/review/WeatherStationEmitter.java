package io.confluent.review;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.SerializationException;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class WeatherStationEmitter {

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                  io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                  io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        props.put("schema.registry.url", "http://localhost:8081");

        final AtomicBoolean justKeepSwimming = new AtomicBoolean(true);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                justKeepSwimming.set(false);
            }
         });

        KafkaProducer producer = new KafkaProducer(props);

        String weatherReadingSchema = "{\"type\":\"record\"," +
                    "\"name\":\"WeatherReading\"," +
                    "\"fields\":[" +
                        "{\"name\":\"temp\",\"type\":\"double\"}," +
                        "{\"name\":\"dewpoint\",\"type\":\"int\"}," +
                        "{\"name\":\"humidity\",\"type\":\"double\"}," +
                        "{\"name\":\"wind\",\"type\":\"string\"}" +
                    "]}";

        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(weatherReadingSchema);
        Random rnd = new Random();

        while(justKeepSwimming.get()) {

            GenericRecord weatherReading = new GenericData.Record(schema);
            weatherReading.put("temp", rnd.nextDouble()* 100);
            weatherReading.put("dewpoint", 45);
            weatherReading.put("humidity", 33D);
            weatherReading.put("wind", "11 mph SSE");

            ProducerRecord<Object, Object> record = new ProducerRecord<>(
                "weather-readings",
                "station-1",
                weatherReading);

            try  {
                producer.send(record);
                producer.flush();
            }
            catch (SerializationException ex) {
                System.out.println(ex.getMessage());
            }

            try {
                Thread.sleep(1000*3);
            } catch (Exception e) {
            }
        }
    }

}


### Confluent Platform Schema Registry & Avro Review
The agenda for this project was to evaluate the Confluent Schema Registry (SR) and Avro as it relates to developer friction.  Primarily I was asked to look for any experiences that would prevent me from viewing the product as pleasant and sensible.  A secondary goal was to look for gaps in the documentation and provide suggestions for improvements.

I attempted to approach this problem from the perspective of a developer, making the assumption that this technical role was the desired one for which to remove fricition (versus a System Administrator or KSQL user, for instance).

Following the documentation I wrote two programs.
* The first program served the purpose of using the core Kafka Producer client to publish some sample data in Avro format.  I wanted to experience using SR/Avro at the base level.
* The seconds program was a KStream application that aimed to read the Avro values, calculate a derived value, and produce a different type.

I attempted to use the development of the programs as a way to surface issues in the product or documentation.  It was requested to maintain a 2 hour timebox on this project, which proved to be quite limiting for finding serious flaws in the products as they are mature.

### Summary
Generally, and as a matter of opinion, I find the Confluent documentation to be above average for technical documentation found in similar projects.  Documentation bases I would rank higher would be Amazon Web Services, and documentation I would rate lower would be Terraform (for navigation purposes).

Starting from the CP Quick Start, as requested, there is no immediately apparent documentation related to message formatting.  To find the relevant documentation for the simple producer I wanted to build, I needed to navigate using the documentation Table Of Contents tree in order to find documentation specific to SR/Arvo.  Once I found the [key document](https://docs.confluent.io/current/app-development/index.html), coding the Producer with Avro schemas was relatively painless.  The most painful experience during this phase was crafting an Avro Schema String literal in Java, as most other languages support raw strings to ease this difficulty.

Moving to the KStreams application, I ran into slightly more difficulty dealing with message formats as my experiences were dated in this area.  I found [this document](https://docs.confluent.io/current/app-development/index.html) to be particularly helpful for my streaming application use case.  However, I spent more time looking at Confluent sample code repositories for this stage of the task than I did at documentation.  I had to commit myself to reading, with care, some of the documentation as it related to changing formats within the stages of the Streaming Topology.  As this is a complex topic and API, it's not surprising to me that I stumbled here.  Generally the documentation was clear here and without further thought I cannot derive ways in which the API behavior could be presented clearer.

Over all, I think the SR/Avro system works quite well and as expected.  The integrations with Confluent Serdes is strong.  I especially like the new (to me) Control Center integration with topic inspection.

One idiosyncrasy is in the difference between the SR concepts vs the Kafka concepts.  For instance, the term `subject` doesn't map exactly onto Topic, which could be confusing to someone new.

### Issues Encoutered
* The Confluent Documentation search feature gave me problems when looking for SR/Avro specific documents.  For instance, searching for `maven` or `packages` resulted in unhelpful results.
* The "Schema Registry Developer Guide" is presented among the documentation and my first assumption was that this would contain the documentation I needed to build applications against the SR.  However, it appears that this documentation is somehwat about building SR itself, some plugins, or utilizing the REST API, which I find somewhat misleading.  In my opinion, the typical developer is going to, first, use 'out of the box' de/serdes which should be more clearly described before building SR itself or showcasing the HTTP API.

### Recommendations
* From the Quick Start documentation, there isn't any 'early' dialog about data format decisions.  I think the record format question is one of the critical first decisions for a new adopter (the likely audience for quickstart).  I would recommend adding a "Feature Support Matrix" style document, linked from the "Next Steps" Section at the tail of the [cp quickstart](https://docs.confluent.io/current/quickstart/ce-quickstart.html#next-steps). An example of this kind of document for [clients exists](https://docs.confluent.io/current/clients/index.html#feature-support). This document should clearly highlight the benefits of the SR/Avro integrations through the rest of the platform.  A link to the [Jay Kreps blog](https://www.confluent.io/blog/avro-kafka) on the subject could be benefiical.  

* Move the Schema Registry Developer Guide into the SR code repository, or elswehwere labeled, to prevent a reader from thinking they are reading the more developer specific documentation.

* Improve search term results in Confluent Documentation search to increase relevance.

* Anywhere within the documentation that references adding a JVM dependency, the code required to add the Confluent repository should be included with it.  I had to Google to find the relevant POM entry, althought it's located in the documentation in places, not everywhere that dependencies are decribed.  Searching for `packages` or `maven` did not help me here.

* Without much thought to implementation difficulty, a "virtual" route could be added to the SR HTTP Service to bridge the terminiology between SR and Kafka.  For instance, a `topics` route could be added that provides the Key/Value schema data as a pair instead of them being under `subjects` with independent names.  Something like the following:

```
curl -XGET http://localhost:8081/topic-schemas
[
  { "name": "weather-readings",
    "key": {
      "subject": "weather-readings-key",
      "schema": {
        ...
      }
    },
    "value": {
      "subject": "weather-readings-value",
      "schema": {
        ...
      }
    }
]
```

### Notes
[Full notes from project here](NOTES.md)


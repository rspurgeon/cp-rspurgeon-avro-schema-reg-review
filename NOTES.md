
## The Task 
Using the Confluent Platform quickstart at https://docs.confluent.io/current/quickstart/index.html (either for local install or Docker) as the context for your analysis, critique __**Confluent Schema Registry**__ and __**Avro**__ with regard to user pain points or friction in using them:
* Primary: look for any element of the product experience that would prevent you from thinking of this as the most pleasant and sensible product you've ever used
  * suggest improvements how it could get on that level.
* Secondary: look for any part of the documentation that would keep you from thinking it was world-class documentation
  * how it should grow towards that goal. 
* Please limit to 1-2 hours

## Plan 
Considering we are looking to foster developer experience, i'm going to approach this problem like a developer, opposed to a kafka administrator, casual documentation/kafka user, KSQL user, etc...  My experience tells me that the likely pain points in using avro and schema registry (SR) are in server setup/config, de/serde config, IDL maintenence, schema evolution, and...  So i'll write a program or two with the documentation as a guide and try and construct a critique and suggestions from there.

#### Ideas
* I'm tempted to use non JVM languages to work on one component of the sample to vet out issues because the JVM bindings are the 'best traveled'.  I'm more likely to surface issues in the documentation and product using the less mature technologies, but I'm concerned about the timebox of the project limiting what i can accomplish.  So I think I'll just stick with very traditional Java in any coding, based on the timebox issue.  Would love an opportunity to vet out the Go/Python/C++ support.

So I'm going to write two programs that communicate via kafka using Avro and use SR.
* A simple producer which will, tada, produce simple values on a lazy loop using the traditional Kafka Producer java class configured with avro & SR.
* A kstream app that consumes the values produced above, and materializes a new avro value of some sort as well as a second topic in some other type, TBD.

## Notes

#### Confluent CLI startup issue notes
Right away I experienced a new issue with confluent CLI.  I manually worked around it, Details are noted [here](CLI_START_ISSUE.md)

#### Control Center macbook sleep survival issue
At times the control center will not survive a macbook sleep -> restart scenario.  I've not spent time trying to find a pattern.  Trying various `confluent start control-center` and `confluent stop control-center` doesn't seem to have a positive effect

`confluent destroy`, then `confluent start` seems to address the issue

#### Documentation Notes
From the cp quickstart, the left margin is where the SR is referenced, which leads to here, which is where I'll start my development project: https://docs.confluent.io/current/schema-management.html

From the top levels of the documentation, there isn't any initially apparent dialog about data format decisions.  Jay Kreps has a good [blog post](https://www.confluent.io/blog/avro-kafka-data/) about this.  I think the wireline format decision is one of the critical first decisions for a new adopter (the likely audience for quickstart), so maybe pros/cons of this decision could be more prominent in the "Next Steps" Section at the tail of the cp quickstart: https://docs.confluent.io/current/quickstart/ce-quickstart.html#next-steps  We could link the Kreps blog or we could author a new document that details the pros / cons of this decision highlighting the pros of Avro/SR through the rest of the platform... maybe something like this [feature support matrix for the client](https://docs.confluent.io/current/clients/index.html#feature-support)

[Schema Registry Developer Guide](https://docs.confluent.io/current/schema-registry/develop/development.html) made me think I was going to get documentation on developing to Avro/SR, but instead this is related to building SR itself.

Took me some poking around before I found what I think is the key [document](https://docs.confluent.io/current/app-development/index.html) for a developer who wishes to start producing data using 'old school' producer.


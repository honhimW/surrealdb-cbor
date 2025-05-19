# Surreal DB Client Using Http CBOR Protocol

**Maven Central**: [surrealdb-cbor](https://central.sonatype.com/artifact/io.github.honhimw/surrealdb-cbor)

```groovy
implementation 'io.github.honhimw:surrealdb-cbor:0.0.1-RC.0'
```

```xml

<dependency>
    <groupId>io.github.honhimw</groupId>
    <artifactId>surrealdb-cbor</artifactId>
    <version>0.0.1-RC.0</version>
</dependency>
```

## Usage

#### Create Client

```java
public static void main(String[] args) {
    SurrealClient client = SurrealClient.builder()
        .host("127.0.0.1")
        .namespace("surrealdb")
        .database("surrealdb")
        .username("root")
        .password("root")
        .blocking(); // blocking client
//        .reactive(); // reactive client

    ReactiveSurrealClient reactiveClient = client.reactive(); // To reactive
    SurrealClient blocking = reactive.blocking(); // To blocking

    // PING, Throws Exception if connecting error
    client.ping();
}
```

#### Execute SQL

```java
// With Default Decode as Response
Response response = client.sql("UPSERT tmp CONTENT $content;", Map.of("content", Map.of(
        "id", 1,
        "foo", "bar",
        "hello", "world"
    )));

// None Decoded bytes, May manually decode by using CborUtils
byte[] bytes = client.sqlBytes("UPSERT tmp CONTENT $content;", Map.of("content", Map.of(
    "id", 1,
    "foo", "bar",
    "hello", "world"
)));
JsonNode jsonNode = CborUtils.readTree(bytes);
```

#### Typed Client

> Auto transform between Id <-> RecordId

```java
/**
 * String  : SurrealClient#string
 * Long    : SurrealClient#i64
 * UUID    : SurrealClient#uuid
 * RecordId: SurrealClient#record
 */
TypedSurrealClient<StringIdEntity, String> typedClient = client.string("table_name", StringIdEntity.class);
Optional<StringIdEntity> optional = person.getById("1");
```

## Benchmark

> 4 Thread

| Benchmark       | dataSize | Mode | Cnt | Score   | Error    | Units |
|-----------------|----------|------|-----|---------|----------|-------|
| This(Http)      | 256      | avgt | 10  | 74.749  | ± 2.657  | ms/op |
| This(Http)      | 512      | avgt | 10  | 74.151  | ± 2.428  | ms/op |
| OfficialSdkHttp | 256      | avgt | 10  | 244.266 | ± 2.318  | ms/op |
| OfficialSdkHttp | 512      | avgt | 10  | 247.006 | ± 7.695  | ms/op |
| OfficialSdkWs   | 256      | avgt | 10  | 9.507   | ± 0.417  | ms/op |
| OfficialSdkWs   | 512      | avgt | 10  | 25.800  | ± 52.037 | ms/op |

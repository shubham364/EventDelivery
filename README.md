# Event Delivery Service
A [DropWizard](https://www.dropwizard.io/en/stable/) app for Event Delivery Service. A service that receives events from multiple users from an HTTP
endpoint and delivers (broadcast) them to various destinations. 
The app will fan-out requests to multiple destinations while handling failures with downstream destinations.

Functionalities :-
1. Durability
   * The events are stored in MongoDB and thus durability is guaranteed. 
   * The event is persisted in the database until and unless all the consumers process the event successfully, or it has been exhaustively retried. 
2. At-least-once delivery
   * I move the cursor for the consumer only when the event is successfully processed, thus ensuring at-least once delivery.
3. Retry backoff and limit
   * Events are retried using an exponential backoff algorithm and is retried until the maximum number of retries is reached or the message is processed successfully. 
4. Maintaining order
   * I assign an ID to every event and process events the same order. The ID generation method is kept in a synchronized block 
   to make sure every request gets its own ID even in case of concurrent requests. 
5. Delivery Isolation
   * I relay the message to the consumers using **Observer Design Pattern**. To mock the processing of events I have created 
   a DummyEventProcessing class that works on a new thread for each Observer. Even if an event is failing for one observer, all the other observers 
   are unaware of this, and they continue their own processing.

APIs :-
- Event Resource
1. /event/publish?topic_name=''
   * This API is used to publish event in a topic. The topic_name is an optional query parameter. If not provided the events will go into a default topic.
   
   * Payload :-
   ```json
   {
    "userId": "",
    "payload": ""
   }
   ```
   * Response :-
   ```json
   {
    "userId": "",
    "payload": "",
    "_id": "1"
   }
   ```
2. /event/get/{topic_name}/{user_id}
   * This API is used to get the event from a topic based on user_id.
   * Response :-
   ```json
   {
    "userId": "",
    "payload": "",
    "_id": "1"
   }
   ```
3. /event/create_topic/{topic_name}
   * This API is used to create a new topic.

- Consumer Resource
1. /consumer/get/{consumer_name}
   * This API is used to get Consumer info.
   * Response :-
   ```json
   {
    "topicName": "topics",
    "cursor": "54",
    "maxRetry": 3,
    "currRetry": 0,
    "processingEvent": false,
    "_id": "consumer1"
   }
   ```
2. /consumer/create
   * This API is used to create a new consumer that will receive the event pushed into the topic it has subscribed to.
   * Payload :-
   ```json
   {
    "consumer_name": "consumer2",
    "topic_name": "topics",
    "max_Retry": 5
   }
   ```
   * Response :-
   ```json
   {
    "topicName": "topics",
    "cursor": "0",
    "maxRetry": 5,
    "currRetry": 0,
    "processingEvent": false,
    "_id": "consumer2"
   }
   ```
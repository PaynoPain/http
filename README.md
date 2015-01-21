# http
A library to simplify http communications

## Main abstraction
![2]
[2]: https://raw.githubusercontent.com/PaynoPain/http/master/docs/ResourceRequester.jpg (Main abstraction)

## Usage examples
#### Without extra mechanisms

Given the following endpoint:

```
POST person/add

parameters:
name, surname, years

response:
201
{
    id: 5,
    full_name: "Name Surname" 
}
```

Endpoint definition:

```java
public class Person {
    public final Integer id;
    public final String fullName;

    public Person(Integer id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }
}

public class AddPerson {
    private static final int MILLISECONDS_TO_TIMEOUT = 5000;

    private static class Parameters {
        private final String name;
        private final String surname;
        private final int years;

        private Parameters(String name, String surname, int years) {
            this.name = name;
            this.surname = surname;
            this.years = years;
        }
    }

    private final RequesterAction<Parameters, Person> action;

    public AddPerson(Server server){
        action = new RequesterAction<Parameters, Person>(
                new ContextRequester(
                        new PostMethod(MILLISECONDS_TO_TIMEOUT),
                        new ServerContext(server)
                ),
                new RequestComposer<Parameters>() {
                    @Override
                    public Request compose(Parameters parameters) {
                        return new BaseRequest(
                                "person/add",
                                new LiteralStringsMap(
                                        "name", parameters.name,
                                        "surname", parameters.surname,
                                        "years", String.valueOf(parameters.years)
                                )
                        );
                    }
                },
                new ResponseInterpreter<Person>() {
                    @Override
                    public Person interpret(Response response) throws IllegalArgumentException {
                        try {
                            final JSONObject json = new JSONObject(response.getBody());
                            return new Person(json.getInt("id"), json.getString("full_name"));
                        } catch (JSONException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                }
        );
    }

    public Person addPerson(String name, String surname, int years) {
        return action.apply(new Parameters(name, surname, years));
    }
}
```

Usage:

```java
public class Main {
    private static final Server SERVER = new Server(
            Server.Scheme.HTTP, "www.server.com", 80
    );
    private static final AddPerson ADD_PERSON_ENDPOINT = new AddPerson(SERVER);

    public static void main(String... args){
        final Person person = ADD_PERSON_ENDPOINT.addPerson("John", "Doe", 30);
        System.out.println(person.fullName + " has the id: " + person.id);
    }
}
```

#### Commands with offline notification queue mechanism.

Given the following endpoint:

```
POST movement/register

parameters:
itinerant, scanner, zone, moment

response:
201
ok
```

Endpoint definition:

```java
public class Movement {
    public final String itinerant;
    public final String scanner;
    public final String destinationZone;
    public final String happenedMoment;

    public Movement(String itinerant, String scanner, String destinationZone, String happenedMoment) {
        this.itinerant = itinerant;
        this.scanner = scanner;
        this.destinationZone = destinationZone;
        this.happenedMoment = happenedMoment;
    }
}

public class RegisterMovementCommand implements Flushable {
    private static final ValidStatusCodesValidator RESPONSE_VALIDATOR = new ValidStatusCodesValidator(Arrays.asList(201));

    private final OnDemandQueuedCommand<Movement> command;

    public RegisterMovementCommand(final ResourceRequester requester, final QueueStorage<Request> queue) {
        command = new OnDemandQueuedCommand<Movement>(
                requester,
                queue,
                new RequestComposer<Movement>() {
                    @Override
                    public Request compose(Movement movement) {
                        return new BaseRequest(
                                "movement/register",
                                new LiteralStringsMap(
                                        "itinerant", movement.itinerant,
                                        "scanner", movement.scanner,
                                        "zone", movement.destinationZone,
                                        "moment", movement.happenedMoment
                                )
                        );
                    }
                },
                RESPONSE_VALIDATOR
        );
    }

    @Override
    public boolean canFlush() {
        return command.canFlush();
    }

    @Override
    public void flush() {
        command.flush();
    }

    public void register(Movement movement) {
        command.apply(movement);
    }
}
```

Usage:

```java
public class Main {
    private static final Server SERVER = new Server(
            Server.Scheme.HTTP, "www.server.com", 80
    );
    private static final ContextRequester POST = new ContextRequester(
            new PostMethod(5000),
            new ServerContext(SERVER)
    );
    public static final QueueStorageInDirectory<Request> QUEUE_STORAGE = new QueueStorageInDirectory<Request>(
            new File("/tmp/httpQueue/"),
            new JsonRequestComposer(),
            new JsonRequestParser()
    );
    public static final RegisterMovementCommand REGISTER_MOVEMENT_ENDPOINT = new RegisterMovementCommand(POST, QUEUE_STORAGE);

    public static void main(String... args){
        REGISTER_MOVEMENT_ENDPOINT.register(new Movement("Paco", "A", "Vip", "01/10/2014 10:00:00.0"));
        REGISTER_MOVEMENT_ENDPOINT.register(new Movement("Pepe", "B", "Vip", "01/10/2014 10:01:12.542"));
        REGISTER_MOVEMENT_ENDPOINT.register(new Movement("Paco", "A", "Comun", "01/10/2014 11:00:00.0"));
        REGISTER_MOVEMENT_ENDPOINT.register(new Movement("Fran", "B", "Comun", "01/10/2014 11:00:00.001"));

        if (REGISTER_MOVEMENT_ENDPOINT.canFlush()){
            System.out.println("There are pending notifications!");
            System.out.println("Trying to send them...");
            REGISTER_MOVEMENT_ENDPOINT.flush();
        }
    }
}
```

#### Queries with cache mechanism.

Given the following endpoint:

```
GET society/listAll

parameters:
-

response:
200
{
    "data": [
        {
            "Society": {
                "id": "111",
                "name": "::111_name::",
                "address": "::111_address::" 
            }
        },
        {
            "Society": {
                "id": "222",
                "name": "::222_name::",
                "address": "::222_address::" 
            }
        }
    ]
}
```

Endpoint definition:

```java
public class Society {
    public final Integer id;
    public final String name;
    public final String address;

    public Society(Integer id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }
}

public class FindAllSocietiesQuery {
    private static final long MILLISECOND = 1;
    private static final long SECOND = 1000 * MILLISECOND;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;

    private static final Function<String, List<Society>> BODY_PARSER = new Function<String, List<Society>>() {
        @Override
        public List<Society> apply(String responseBody) throws RuntimeException {
            List<Society> societies = new ArrayList<Society>();

            try {
                final JSONArray jsonSocieties = new JSONObject(responseBody).getJSONArray("data");
                for (int i = 0; i < jsonSocieties.length(); i++) {
                    final JSONObject societyJson = jsonSocieties.getJSONObject(i).getJSONObject("Society");
                    societies.add(new Society(
                            societyJson.getInt("id"),
                            societyJson.getString("name"),
                            societyJson.getString("address")
                    ));
                }
            } catch (JSONException e){
                throw new IllegalArgumentException(e);
            }

            return societies;
        }
    };

    private static final ResponseValidator CACHE_VALIDATOR = new ValidatorCollection(Arrays.asList(
            new ValidStatusCodesValidator(Arrays.asList(200)),
            new ParseableResponseValidator<List<Society>>(BODY_PARSER)
    ));

    private static final BaseRequest ALL_SOCIETIES_REQUEST = new BaseRequest("society/listAll");

    private final OnDemandCachedQuery<Void, List<Society>> query;

    public FindAllSocietiesQuery(final ResourceRequester requester, final MapStorage<Request, CacheEntry> cacheStorage, final Factory<Date> timeGateway) {
            query = new OnDemandCachedQuery<Void, List<Society>>(
                    requester,
                    cacheStorage,
                    CACHE_VALIDATOR,
                    new RequestComposer<Void>() {
                        @Override
                        public Request compose(Void aVoid) {
                            return ALL_SOCIETIES_REQUEST;
                        }
                    },
                    new ResponseInterpreter<List<Society>>() {
                        @Override
                        public List<Society> interpret(Response response) throws IllegalArgumentException {
                            return BODY_PARSER.apply(response.getBody());
                        }
                    },
                    timeGateway,
                    5 * MINUTE,
                    1 * DAY
            );
    }

    public Collection<Society> findAllSocieties() {
        return query.apply(null);
    }
}
```

Usage:

```java
public class Main {
    private static final MapStorageInDirectory CACHE_STORAGE = new MapStorageInDirectory(new File("/tmp/httpCache/"));
    private static final Factory<Date> TIME_GATEWAY = new Factory<Date>() {
        @Override
        public Date get() {
            return new Date();
        }
    };
    private static final Server SERVER = new Server(
            Server.Scheme.HTTP, "www.server.com", 80
    );
    private static final ContextRequester GET = new ContextRequester(
            new GetMethod(5000),
            new ServerContext(SERVER)
    );
    private static final FindAllSocietiesQuery FIND_ALL_SOCIETIES_ENDPOINT = new FindAllSocietiesQuery(GET, CACHE_STORAGE, TIME_GATEWAY);

    public static void main(String ... args){
        for (Society society : FIND_ALL_SOCIETIES_ENDPOINT.findAllSocieties()){
            System.out.println(
                    society.name + "(" + society.id + "): " + society.address
            );
        }
    }
}
```

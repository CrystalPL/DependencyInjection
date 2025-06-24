# CrCDependencyInjection

## O projekcie

CrCDependencyInjection to lekki framework do wstrzykiwania zależności (Dependency Injection) napisany w Javie.
Framework pozwala na automatyczne zarządzanie obiektami i ich zależnościami, eliminując potrzebę ręcznego tworzenia
i konfigurowania obiektów w aplikacji.

## Funkcjonalności

- Automatyczne skanowanie klas oznaczonych adnotacją `@Injectable`
- Automatyczne wstrzykiwanie zależności poprzez konstruktor
- Tworzenie obiektów za pomocą metod fabrycznych oznaczonych adnotacją `@Factory`

## Struktura projektu

Projekt składa się z dwóch głównych modułów:
- **app** - zawierający główną implementację
- **test** - zawierający przykłady użycia i jednocześnie "testy"

### Wymagania

- Java 8 lub nowsza

### Gradle
```groovy
maven { 
    url "https://repo.crystalek.pl/crc" 
}

dependencies {
    implementation 'pl.crystalek:dependency-injection:1.0'
}
```

### Przykład użycia

1. Oznacz klasy, które mają być zarządzane przez framework, adnotacją `@Injectable`:

```java
@Injectable
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // metody klasy...
}

@Injectable
public class UserRepository {
    // implementacja...
}
```

2. Możesz również tworzyć obiekty za pomocą metod fabrycznych:

```java
public class ConfigFactory {
    @Factory
    public static ConfigInterface createConfig(Repository repository) {
        return new ConfigImplementation(repository.getSettings());
    }
}
```

3. Inicjalizacja kontenera DI:

```java
import pl.crystalek.di.InjectService;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Main.class.getName());
        
        // Inicjalizacja kontenera DI
        InjectService injectService = InjectService.getInjectService(
            Main.class.getClassLoader(),
            "com.twoja.aplikacja",
            logger
        );
        
        // Uruchomienie procesu skanowania i tworzenia obiektów
        injectService.injectObjects();
        
        // Pobranie stworzonego obiektu
        UserService userService = injectService.getObjectRepository().getObjectByClassName(UserService.class);
        
        // Użycie obiektu...
    }
}
```

### Adnotacje

- **@Injectable** - oznacza klasę, która ma być zarządzana przez kontener DI
- **@Factory** - oznacza metodę statyczną, która tworzy obiekt; metoda musi zwracać interfejs, a jej parametry zostaną automatycznie wstrzyknięte

### Interfejsy

- **InjectService** - główna usługa zarządzająca procesem wstrzykiwania zależności
- **ObjectRepository** - repozytorium przechowujące wszystkie utworzone obiekty

### Przykłady użycia

W module `test` znajduje się kilka przykładów użycia frameworka:

- Podstawowe wstrzykiwanie zależności
- Użycie metod fabrycznych
- Zaawansowane scenariusze z wieloma poziomami zależności
- Przykłady wykrywania zależności cyklicznych
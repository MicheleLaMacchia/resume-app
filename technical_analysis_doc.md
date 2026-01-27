# Technical Analysis: Resume-App

## 1. Panoramica Generale

`resume-app` è un'applicazione Spring Boot che espone API REST per la creazione e la gestione di curriculum professionali. L'applicazione è progettata per essere eseguita in un ambiente serverless, come indicato dalla dipendenza `aws-serverless-java-container-springboot3`.

## 2. Architettura

L'applicazione segue una classica architettura a 3 livelli (3-tier) per garantire la separazione delle responsabilità (Separation of Concerns):

-   **Controller Layer (`ResumeController.java`)**: Responsabile della gestione delle richieste HTTP in entrata, della validazione dell'input e della formattazione delle risposte HTTP in uscita. Delega la logica di business al livello di servizio.
-   **Service Layer (`ResumeServiceImpl.java`)**: Contiene la logica di business principale dell'applicazione. Coordina le operazioni, applica le regole di business e interagisce con il livello di persistenza (DAO).
-   **DAO (Data Access Object) Layer (`DynamoResumeDaoImpl.java`)**: Responsabile di tutto l'accesso e la manipolazione dei dati. Abstrae la logica di interazione con il database (AWS DynamoDB), isolando il resto dell'applicazione dai dettagli di implementazione della persistenza.

## 3. Tecnologie Utilizzate

-   **Linguaggio**: Java 17
-   **Framework**: Spring Boot 3.2.0
-   **Database**: AWS DynamoDB (tramite AWS SDK v2)
-   **API Documentation**: Springdoc OpenAPI (per la generazione automatica della documentazione Swagger UI)
-   **Validazione**: Spring Boot Starter Validation (per la validazione dichiarativa dei bean)
-   **Deployment**: Progettato per AWS Lambda (tramite `aws-serverless-java-container`)

## 4. API Endpoints

Tutti gli endpoint sono prefissati con `/api`.

| Metodo | Percorso                               | Descrizione                                                                 | Risposta Successo | Risposte Errore                                                                                                                              |
| :----- | :------------------------------------- | :-------------------------------------------------------------------------- | :---------------- | :------------------------------------------------------------------------------------------------------------------------------------------- |
| `POST` | `/resume`                              | Crea un nuovo curriculum.                                                   | `201 Created`     | `400 Bad Request` (dati di input non validi), `409 Conflict` (curriculum con lo stesso `codiceFiscale` già esistente)                     |
| `GET`  | `/resume-list`                         | Restituisce l'elenco di tutti i `codiceFiscale` (PK) dei curriculum presenti. | `200 OK`          | -                                                                                                                                          |
| `GET`  | `/resume/{pk}`                         | Recupera un intero curriculum dato il suo `codiceFiscale` (pk).             | `200 OK`          | `404 Not Found` (se il pk non esiste)                                                                                                        |
| `GET`  | `/personal-data/{pk}`                  | Recupera solo i dati anagrafici di un curriculum.                           | `200 OK`          | `404 Not Found`                                                                                                                            |
| `GET`  | `/working-experience/{pk}`             | Recupera la lista delle esperienze lavorative.                              | `200 OK`          | `404 Not Found`                                                                                                                            |
| `GET`  | `/working-experience/{pk}/{index}`     | Recupera una specifica esperienza lavorativa per indice.                    | `200 OK`          | `404 Not Found`                                                                                                                            |
| `GET`  | `/education-training/{pk}`             | Recupera la lista di istruzione e formazione.                              | `200 OK`          | `404 Not Found`                                                                                                                            |
| `GET`  | `/education-training/{pk}/{index}`     | Recupera un elemento specifico di istruzione per indice.                    | `200 OK`          | `404 Not Found`                                                                                                                            |
| `GET`  | `/language-skills/{pk}`                | Recupera le competenze linguistiche.                                        | `200 OK`          | `404 Not Found`                                                                                                                            |
| `GET`  | `/language-skills/{pk}/{index}`        | Recupera una specifica competenza linguistica per indice.                   | `200 OK`          | `404 Not Found`                                                                                                                            |
| `GET`  | `/soft-skills/{pk}`                    | Recupera le competenze trasversali.                                         | `200 OK`          | `404 Not Found`                                                                                                                            |
| `GET`  | `/technical-skills/{pk}`               | Recupera le competenze tecnologiche.                                        | `200 OK`          | `404 Not Found`                                                                                                                            |
| `GET`  | `/organizational-skills/{pk}`          | Recupera le competenze organizzative.                                       | `200 OK`          | `404 Not Found`                                                                                                                            |
| `GET`  | `/functional-skills/{pk}`              | Recupera le competenze funzionali.                                          | `200 OK`          | `404 Not Found`                                                                                                                            |

## 5. Modello Dati

Il modello dati principale è rappresentato dal record Java `ResumeModels`. Questo oggetto aggrega tutte le sezioni di un curriculum, tra cui:

-   `DatiGenerali`: Informazioni anagrafiche della persona.
-   `EsperienzaLavorativa`: Elenco delle esperienze professionali.
-   `IstruzioneFormazione`: Elenco dei titoli di studio e corsi.
-   `CompetenzaLinguistica`: Elenco delle lingue conosciute.
-   `Competenza`: Utilizzato per diverse categorie di skill (trasversali, tecnologiche, organizzative, funzionali).

La validazione dei dati in input è gestita tramite annotazioni di Bean Validation (es. `@Valid`, `@NotNull`, `@NotBlank`) sui record stessi.

## 6. Persistenza dei Dati

I dati vengono salvati su una tabella AWS DynamoDB chiamata `ResumeTable`.

-   **Strategia di Chiave**: Viene utilizzato un modello di chiave composita:
    -   **Partition Key (PK)**: `codiceFiscale` dell'utente. Questo garantisce l'unicità di ogni curriculum.
    -   **Sort Key (SK)**: Un valore costante `"PROFILE"`.
-   **Dati**: L'intero oggetto `ResumeModels` viene serializzato in una stringa JSON e salvato in un attributo chiamato `data`.
-   **Prevenzione Duplicati**: Durante l'inserimento, viene usata una `ConditionExpression` (`attribute_not_exists(pk)`) per garantire che l'operazione fallisca se un curriculum con lo stesso `codiceFiscale` esiste già. Questa operazione atomica a livello di database è efficiente e previene la sovrascrittura accidentale dei dati.

## 7. Gestione degli Errori

La gestione degli errori è centralizzata nella classe `GlobalExceptionHandler` tramite l'annotazione `@ControllerAdvice`.

Questa classe intercetta eccezioni specifiche e le mappa a risposte HTTP standardizzate in formato JSON:

-   `DuplicateResumeException` -> `409 Conflict`
-   `ResourceNotFoundException` -> `404 Not Found`
-   `IllegalArgumentException` -> `400 Bad Request`
-   `MethodArgumentNotValidException` (per errori di validazione) -> `400 Bad Request` con un corpo che dettaglia i campi errati.
-   Qualsiasi altra `Exception` -> `500 Internal Server Error` con logging dell'errore.

Questo approccio mantiene i controller puliti e garantisce risposte di errore coerenti e informative per i client dell'API.

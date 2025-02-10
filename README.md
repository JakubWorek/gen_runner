# GENRunner

Aplikacja do sterowania eksperymentami z wielokryterialnymi algorytmami ewolucyjnymi (MOEA) opartymi na [MOEA Framework](http://moeaframework.org/). System działa w architekturze klient-serwer, gdzie:

- **Serwer** – asynchronicznie uruchamia eksperymenty, zbiera rezultaty i zarządza statystykami.
- **Klient** – konsolowa aplikacja umożliwiająca uruchamianie eksperymentów, przeglądanie statusów i wyników oraz zarządzanie eksperymentami.

## Funkcjonalności

- **Uruchamianie eksperymentów:**
  - Wykorzystanie MOEA Framework.
  - Parametry eksperymentu: nazwy algorytmów, problemów testowych, metryk oraz budżet (liczba wywołań funkcji celu lub iteracji).
  - Konfiguracja wielu algorytmów i problemów w jednym eksperymencie.
  - Powtórzenia eksperymentów (uruchomienie eksperymentu w wielu instancjach).

- **Asynchroniczne wykonywanie eksperymentów:**
  - Jednoczesne uruchamianie wielu eksperymentów z uwzględnieniem ograniczeń maszyny.

- **Zarządzanie eksperymentami:**
  - Listowanie eksperymentów (trwających i zakończonych).
  - Odczytywanie statusu eksperymentu (działający, zakończony, błąd).
  - Odczytywanie wyników – prezentacja tabelaryczna (iteracje jako wiersze, metryki jako kolumny).
  - Agregacja statystyk (średnia, mediana, odchylenie standardowe) z możliwością filtrowania po zakresie dat.
  - Filtrowanie instancji eksperymentów według algorytmu, problemu lub metryk.

- **Prezentacja wyników:**
  - Eksport wyników do pliku CSV.
  - Generowanie wykresów (PNG) – wykres wartości metryki w kolejnych iteracjach.
  - Nowe formy prezentacji kompatybilne z dotychczasowymi mechanizmami (np. filtrowanie po czasie).

- **Zarządzanie grupami eksperymentów:**
  - Tworzenie grup eksperymentów poprzez oznaczenie (po ID lub wyniku filtrowania) oraz możliwość dodania do istniejącej grupy.
  - Wyświetlanie wyników dla eksperymentów z danej grupy.

- **Obsługa API:**
  - Klient CLI komunikuje się z serwerem, obsługuje błędy i przekazuje parametry eksperymentów.

- **Usuwanie eksperymentów:**
  - Usuwanie pojedynczych eksperymentów lub całych grup (razem z rezultatami i metadanymi).


Skład zespołu:
- Jakub Worek
- Jakub Białecki
- Bartłomiej Treśka

# Jak odpalić:
- Należy uruchomić mongodb na dockerze (instrukcje w folderze database)
- Należy uruchomić `backend/src/main/java/pl/edu/agh/GenRunnerApplication.java`
- Należu uruchomić `cli/src/main/java/cli/Client.java`

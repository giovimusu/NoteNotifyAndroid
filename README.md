# NoteNotify — prototipo Android

Questa è la prima base del progetto, non un APK già installabile.

## Cosa include
- Schermata minimalista in bianco e nero.
- WhatsApp rappresentato da una semiminima (♩).
- Instagram rappresentato da una croma (♪).
- Se entrambe le app hanno notifiche attive, mostra una semiminima col punto (♩.).
- Lettura delle notifiche tramite `NotificationListenerService`, solo dopo l'autorizzazione esplicita dell'utente.
- Elenco delle notifiche rilevate per le due app.

## Come provarlo
1. Installa Android Studio su un computer.
2. Apri la cartella `NoteNotifyAndroid` come progetto.
3. Attendi la sincronizzazione Gradle e installa l'app su un telefono Android.
4. Nell'app premi **Attiva accesso alle notifiche** e abilita NoteNotify nelle impostazioni di Android.
5. Ricevi notifiche da WhatsApp o Instagram e riapri NoteNotify.

## Limiti della versione 0.1
- Non sostituisce le icone originali nella barra di stato Android: mostra le figure nella propria schermata.
- Il testo delle notifiche può contenere informazioni private; l'app conserva localmente le notifiche rilevate in SharedPreferences.
- Il simbolo combinato è una regola provvisoria: appare quando entrambe le app hanno almeno una notifica attiva. In una versione successiva possiamo definire se aggregare per singolo messaggio, app o intervallo temporale.
- Il progetto sorgente non è un APK firmato e non è stato compilato in questo ambiente.

## Compilazione dal telefono con GitHub Actions

Il progetto include `.github/workflows/android.yml`: dopo aver caricato tutti i file in un repository GitHub sul branch `main`, GitHub avvia automaticamente la compilazione. In alternativa, dalla scheda **Actions** puoi avviare **Build Android APK** con **Run workflow**. Al termine, apri l'esecuzione completata e scarica l'artefatto **NoteNotify-debug-apk**. Dentro troverai `app-debug.apk`.

Nota: questa è una build di debug, adatta a installazione e prova personale. Il progetto è una base iniziale e potrebbe richiedere correzioni se la compilazione segnala errori.

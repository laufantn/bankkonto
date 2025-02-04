//Titel: Kontoverwaltung
//Autor: 2bAPC Urschler

import java.util.ArrayList;
import java.util.Scanner;

abstract class Konto {
    protected String kontoinhaber;
    protected String bankleitzahl;
    protected String kontonummer;
    protected double kontostand;

    public Konto(String kontoinhaber, String bankleitzahl, String kontonummer, double startbetrag) {
        this.kontoinhaber = kontoinhaber;
        this.bankleitzahl = bankleitzahl;
        this.kontonummer = kontonummer;
        this.kontostand = startbetrag;
    }

    public void einzahlen(double betrag) {
        if (betrag > 0) {
            kontostand += betrag;
            System.out.println("Einzahlung erfolgreich. Neuer Kontostand: " + kontostand);
        } else {
            System.out.println("Ungültiger Betrag.");
        }
    }

    public abstract void abheben(double betrag);

    public void kontoauszug() {
        System.out.println("Kontoauszug:");
        System.out.println("Inhaber: " + kontoinhaber);
        System.out.println("Kontonummer: " + kontonummer);
        System.out.println("Kontostand: " + kontostand);
    }
}

class Girokonto extends Konto {
    private double ueberziehungsrahmen;

    public Girokonto(String kontoinhaber, String bankleitzahl, String kontonummer, double startbetrag, double ueberziehungsrahmen) {
        super(kontoinhaber, bankleitzahl, kontonummer, startbetrag);
        this.ueberziehungsrahmen = ueberziehungsrahmen;
    }

    @Override
    public void abheben(double betrag) {
        if (kontostand - betrag >= -ueberziehungsrahmen) {
            kontostand -= betrag;
            System.out.println("Abhebung erfolgreich. Neuer Kontostand: " + kontostand);
        } else {
            System.out.println("Überziehungsrahmen überschritten.");
        }
    }
}

class Sparkonto extends Konto {
    public Sparkonto(String kontoinhaber, String bankleitzahl, String kontonummer, double startbetrag) {
        super(kontoinhaber, bankleitzahl, kontonummer, startbetrag);
    }

    @Override
    public void abheben(double betrag) {
        if (kontostand >= betrag) {
            kontostand -= betrag;
            System.out.println("Abhebung erfolgreich. Neuer Kontostand: " + kontostand);
        } else {
            System.out.println("Nicht genug Guthaben.");
        }
    }
}

class Kreditkonto extends Konto {
    private double kreditlimit;

    public Kreditkonto(String kontoinhaber, String bankleitzahl, String kontonummer, double startbetrag, double kreditlimit) {
        super(kontoinhaber, bankleitzahl, kontonummer, startbetrag);
        this.kreditlimit = kreditlimit;
    }

    @Override
    public void abheben(double betrag) {
        if (kontostand - betrag >= -kreditlimit) {
            kontostand -= betrag;
            System.out.println("Abhebung erfolgreich. Neuer Kontostand: " + kontostand);
        } else {
            System.out.println("Kreditlimit überschritten.");
        }
    }
}

// Hauptklasse mit Menüführung
public class Kontoverwaltung {
    private static ArrayList<Konto> konten = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("1. Konto anlegen");
            System.out.println("2. Einzahlen");
            System.out.println("3. Abheben");
            System.out.println("4. Kontoauszug");
            System.out.println("5. Überweisen");
            System.out.println("6. Konto auflösen");
            System.out.println("0. Beenden");
            System.out.print("Auswahl: ");
            int auswahl = scanner.nextInt();

            switch (auswahl) {
                case 1 -> kontoAnlegen();
                case 2 -> einzahlen();
                case 3 -> abheben();
                case 4 -> kontoauszug();
                case 5 -> ueberweisen();
                case 6 -> kontoAufloesen();
                case 0 -> {
                    System.out.println("Programm beendet.");
                    return;
                }
                default -> System.out.println("Ungültige Auswahl.");
            }
        }
    }

    private static void kontoAnlegen() {
        System.out.print("Inhaber: ");
        scanner.nextLine(); // Puffer leeren
        String inhaber = scanner.nextLine();
        System.out.print("Bankleitzahl: ");
        String blz = scanner.nextLine();
        System.out.print("Kontonummer: ");
        String knr = scanner.nextLine();
        System.out.print("Startbetrag: ");
        double startbetrag = scanner.nextDouble();
        System.out.println("Kontoart wählen: 1) Girokonto 2) Sparkonto 3) Kreditkonto");
        int art = scanner.nextInt();

        switch (art) {
            case 1 -> {
                System.out.print("Überziehungsrahmen: ");
                double limit = scanner.nextDouble();
                konten.add(new Girokonto(inhaber, blz, knr, startbetrag, limit));
            }
            case 2 -> konten.add(new Sparkonto(inhaber, blz, knr, startbetrag));
            case 3 -> {
                System.out.print("Kreditlimit: ");
                double kredit = scanner.nextDouble();
                konten.add(new Kreditkonto(inhaber, blz, knr, startbetrag, kredit));
            }
            default -> System.out.println("Ungültige Kontoart.");
        }
    }

    private static Konto findeKonto(String kontonummer) {
        for (Konto k : konten) {
            if (k.kontonummer.equals(kontonummer)) {
                return k;
            }
        }
        return null;
    }

    private static void einzahlen() {
        System.out.print("Kontonummer: ");
        String knr = scanner.next();
        Konto k = findeKonto(knr);
        if (k != null) {
            System.out.print("Betrag: ");
            double betrag = scanner.nextDouble();
            k.einzahlen(betrag);
        } else {
            System.out.println("Konto nicht gefunden.");
        }
    }

    private static void abheben() {
        System.out.print("Kontonummer: ");
        String knr = scanner.next();
        Konto k = findeKonto(knr);
        if (k != null) {
            System.out.print("Betrag: ");
            double betrag = scanner.nextDouble();
            k.abheben(betrag);
        } else {
            System.out.println("Konto nicht gefunden.");
        }
    }

    private static void kontoauszug() {
        System.out.print("Kontonummer: ");
        String knr = scanner.next();
        Konto k = findeKonto(knr);
        if (k != null) {
            k.kontoauszug();
        } else {
            System.out.println("Konto nicht gefunden.");
        }
    }

    private static void kontoAufloesen() {
        System.out.print("Kontonummer des zu löschenden Kontos: ");
        String knr = scanner.next();
        Konto k = findeKonto(knr);

        if (k != null) {
            konten.remove(k);
            System.out.println("Konto erfolgreich aufgelöst.");
        } else {
            System.out.println("Konto nicht gefunden.");
        }
    }

    private static void ueberweisen() {
        System.out.print("Von Konto: ");
        String von = scanner.next();
        System.out.print("Zu Konto: ");
        String zu = scanner.next();
        System.out.print("Betrag: ");
        double betrag = scanner.nextDouble();

        Konto sender = findeKonto(von);
        Konto empfaenger = findeKonto(zu);

        if (sender != null && empfaenger != null) {
            sender.abheben(betrag);
            empfaenger.einzahlen(betrag);
            System.out.println("Überweisung erfolgreich.");
        } else {
            System.out.println("Eines der Konten wurde nicht gefunden.");
        }
    }
}

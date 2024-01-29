/*
public class Main {
    public static void main(String[] args) throws OrdineFornitoreException {
        // Inizializza il database e altre variabili necessarie
        Database database = new Database();
        Scanner scanner = new Scanner(System.in);

        // Loop principale per interagire con l'utente
        boolean continuaEsecuzione = true;
        while (continuaEsecuzione) {
            System.out.println("Scegli un'opzione:");
            System.out.println("1. Esegui un ordine da un fornitore");
            System.out.println("2. Registra una vendita da cliente");
            System.out.println("3. Esci");

            int scelta = scanner.nextInt();
            scanner.nextLine(); // Consuma il carattere newline

            switch (scelta) {
                case 1:
                    eseguiOrdineDaFornitore(database);
                    break;
                case 2:
                    registraVenditaDaCliente(database);
                    break;
                case 3:
                    continuaEsecuzione = false;
                    break;
                default:
                    System.out.println("Scelta non valida. Riprova.");
                    break;
            }
        }

        // Chiudi il database e termina il programma
        database.close();
        System.out.println("Grazie per aver usato il negozio di scarpe!");
    }

    public static void eseguiOrdineDaFornitore(Database database) throws OrdineFornitoreException{

        //Chiede le informazioni sul fornitore
        //OrdiniFornitore ordiniFornitore = restituisciFornitore();

        //Chiede che scarpa si vuole ordinare e restituisce la lista
        //List<Scarpe> scarpaOrdinata = restituisciScarpa();
        List<Scarpe> listaScarpe = new ArrayList<>();

        ModelloScarpa modelloScarpa_1 = new ModelloScarpa(1, "Da corsa");
        MarcaScarpa marcaScarpa_1 = new MarcaScarpa(1, "Nike");
        TipoScarpa tipoScarpa_1 = new TipoScarpa(1, "Sandali");
        ColoreScarpa coloreScarpa_1 = new ColoreScarpa(1, "Rosso");
        Scarpe scarpaId_1 = new Scarpe(1, marcaScarpa_1, modelloScarpa_1, 37.5F, tipoScarpa_1, coloreScarpa_1, 40.0F);
        System.out.println(scarpaId_1.getID());
        listaScarpe.add(scarpaId_1);


        Date dataOrdine = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dataOrdineStringa = dateFormat.format(dataOrdine);

        OrdiniFornitore ordiniFornitore = new OrdiniFornitore("amazon", dataOrdineStringa, "completato");

        database.ordineFornitore(ordiniFornitore, listaScarpe);

    }

    public static void registraVenditaDaCliente(Database database) {




    }

    /*
    public static ArrayList<Scarpe> restituisciScarpa(){

        List<Scarpe> listaScarpe = new ArrayList<>();

        boolean continuaEsecuzione = true;
        while(continuaEsecuzione) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Immetti l'ID della scarpa da ordinare da 1 - 4: ");
            System.out.println("Premi 0 una volta terminato l'esecuzione");
            int scelta = scanner.nextInt();

            switch (scelta) {
                case 1:
                    ModelloScarpa modelloScarpa_1 = new ModelloScarpa(1, "Da corsa");
                    MarcaScarpa marcaScarpa_1 = new MarcaScarpa(1, "Nike");
                    TipoScarpa tipoScarpa_1 = new TipoScarpa(1, "Sandali");
                    ColoreScarpa coloreScarpa_1 = new ColoreScarpa(1, "Rosso");
                    Scarpe scarpaId_1 = new Scarpe(1, marcaScarpa_1, modelloScarpa_1, 37.5F, tipoScarpa_1, coloreScarpa_1, 40.0F);
                    listaScarpe.add(scarpaId_1);
                    break;
                case 2:
                    ModelloScarpa modelloScarpa_2 = new ModelloScarpa(2, "Casual");
                    MarcaScarpa marcaScarpa_2 = new MarcaScarpa(2, "Adidas");
                    TipoScarpa tipoScarpa_2 = new TipoScarpa(2, "Da Ginnastica");
                    ColoreScarpa coloreScarpa_2 = new ColoreScarpa(2, "Bianco");
                    Scarpe scarpaId_2 = new Scarpe(1, marcaScarpa_2, modelloScarpa_2, 38.0F, tipoScarpa_2, coloreScarpa_2, 29.99F);
                    listaScarpe.add(scarpaId_2);
                    break;
                case 0:
                    continuaEsecuzione = false;
                    break;

                default:
                    System.out.println("Id non valido riprova");


            }
        }
        return (ArrayList<Scarpe>) listaScarpe;

    }

     */
/*
    public static OrdiniFornitore restituisciFornitore(){

        Scanner scanner = new Scanner(System.in);
        String fornitore;
        Date dataOrdine = null;
        String dataOrdineStringa = " ";
        String statordine;

        System.out.println("Inserisci il nome del fornitore: "); //Fare il controllo delle lettere inserite
        fornitore = scanner.nextLine();

        System.out.println("Inserire la data corrente? S/N");
        String scelta = scanner.nextLine().trim().toLowerCase();

        if (scelta.equals("s")) {
            dataOrdine = new Date(); // Utilizza la data corrente
        } else {
            // Chiedi all'utente di inserire una data personalizzata
            System.out.println("Inserisci la data nel formato 'yyyy-MM-dd': ");
            String dataInserita = scanner.nextLine().trim();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                dataOrdine = dateFormat.parse(dataInserita);
            } catch (ParseException e) {
                System.out.println("Formato data non valido. Assicurati di inserire la data nel formato corretto.");
            }
        }

        if (dataOrdine != null) {
            // Converti la data in una stringa nel formato desiderato
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dataOrdineStringa = dateFormat.format(dataOrdine);
            System.out.println("Data dell'ordine (come stringa): " + dataOrdineStringa);
        } else {
            System.out.println("Data non valida o non specificata.");
        }

        System.out.println("Stato del ordine: "); //Fare il controllo delle lettere inserite
        statordine = scanner.nextLine();

        OrdiniFornitore ordiniFornitore = new OrdiniFornitore(fornitore, dataOrdineStringa, statordine);
        return ordiniFornitore;
    }
*/

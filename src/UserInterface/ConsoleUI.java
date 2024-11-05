package UserInterface;

import Domain.Prietenie;
import Domain.Tuple;
import Domain.Utilizator;
import Domain.Validators.ValidationException;
import Service.Service;

import java.io.*;
import java.sql.SQLOutput;

public class ConsoleUI {
    Service service;

    public ConsoleUI(Service service) {
        this.service = service;
    }

    static void PrintMenu()
    {
        System.out.println("1. Adauga Utilizator.");
        System.out.println("2. Sterge Utilizator.");
        System.out.println("3. Adauga Prietenie.");
        System.out.println("4. Sterge Prietenie.");
        System.out.println("5. Afisare numar de comunitati.");
        System.out.println("6. Afisare cea mai sociabila comunitate.");
        System.out.println("0. Exit");

    }

    String ReadOption()
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while (true)
        {
            try {
                line = reader.readLine();
                break;
            }catch (IOException e){
                System.out.println("An I/O exception has occurred, please try again.");
            }
        }

        return  line;
    }

    private void AdaugareUserUi()
    {
        System.out.println("Introdu numele Utilizatorului:");
        String nume = ReadOption();
        System.out.println("Introdu prenumele utilizatorului:");
        String prenume = ReadOption();

        try {
            service.AdaugaUser(prenume, nume);
            System.out.println("Utilizatorul a fost adaugat cu succes!");
        }catch(ValidationException | IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void StergereUserUi() {
        service.GetUseri().forEach(System.out::println);
        System.out.println("Introdu id-ul utilizatorului pe care doriti sa l stergeti:");
        String Sid = ReadOption();
        long Lid;
        try {
            Lid = Long.parseLong(Sid);
        } catch (NumberFormatException e) {
            System.out.println("Id-ul trebuie sa fie un numar!");
            return;
        }

        try{
            service.StergeUser(Lid);
            System.out.println("User-ul a fost sters cu succes");
        }catch(ValidationException | IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
    }


    private void AdaugaPrieteniUi() {
        for (Utilizator user : service.GetUseri()) {
            System.out.println(user);
        }

        System.out.println("Introdu id User 1:");
        String sId1 = ReadOption();
        System.out.println("Introdu id User 2:");
        String sId2 = ReadOption();

        long lId1;
        long lId2;

        try {
            lId1 = Long.parseLong(sId1);
            lId2 = Long.parseLong(sId2);
        } catch (NumberFormatException e) {
            System.out.println("Ambele id-uri trebuie sa fie un numar!");
            return;
        }
        try {
            service.AdaugaPrietenie(lId1, lId2);
            System.out.println("Prietenia a fost creata cu succes!");
        }catch (ValidationException | IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void StergePrietenieUi() {
        service.GetPrietenii().forEach(System.out::println);

        System.out.println("Introdu perechea de id uri:");
        String sIds = ReadOption();

        Tuple<Long, Long> Fid;
        try {
            //separa numerele din input si le introduce intr un tuple
            Fid = new Tuple<>(Long.parseLong(sIds.strip().split(" ")[0]), Long.parseLong(sIds.strip().split(" ")[1]));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Perechea de id uri trebuie sa fie numere!(cu un singur spatiu intre ele!!)");
            return;
        }

        try{
            service.StergePrietenie(Fid);
            System.out.println("Prietenia a fost stearsa cu succes!");
        }catch (ValidationException | IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
    }
    private void AfisareComunitatiUi()
    {
        int comunitati = 0;
        int comunitatiMinim2 = 0;
        for(var list : service.GetComunitati())
        {
            if(!list.isEmpty())
            {
                System.out.println(list);
                comunitati++;
            }

            if(list.size() >= 2)
                comunitatiMinim2++;
        }

        System.out.println("In total exista " + comunitati + " comunitati .( "+comunitatiMinim2+" comunitati cu macar 2 persoane)");
    }

    private void ComunitateSociabilaUi()
    {
        System.out.println(service.ComunitateSociabila());
    }


    public void Run()
    {
        while(true)
        {
            boolean stop = false;

            PrintMenu();
            int option;

            while(true) {
                try {
                    option = Integer.parseInt(ReadOption().strip());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Wrong option format, please use only option-showed numbers!");
                }
            }

            switch(option)
            {
                case(1):
                    AdaugareUserUi();
                    break;
                case(2):
                    StergereUserUi();
                    break;
                case(3):
                    AdaugaPrieteniUi();
                    break;
                case(4):
                    StergePrietenieUi();
                    break;
                case(5):
                    AfisareComunitatiUi();
                    break;
                case(6):
                    ComunitateSociabilaUi();
                    break;
                case(0):
                    stop = true;
                    break;
                default:
                    System.out.println("Nu avem atatea optiuni!");
                    break;

            }

            if (stop)
                break;
        }
    }
}

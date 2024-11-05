import Domain.Prietenie;
import Domain.Tuple;
import Domain.Utilizator;
import Domain.Validators.PrietenieValidator;
import Domain.Validators.UtilizatorValidator;
import Domain.Validators.ValidationException;
import Repository.InFile.AbstractFileRepository;
import Repository.InFile.PrietenieRepository;
import Repository.InFile.UtilizatorRepository;
import Repository.InMemory.InMemoryRepository;
import Repository.Repository;
import UserInterface.ConsoleUI;
import Service.Service;

public class Main {
    public static void main(String[] args) {
        Repository<Long,Utilizator> userRepo = new UtilizatorRepository(new UtilizatorValidator(),"./data/utilizatori.txt");
        Repository<Tuple<Long,Long>, Prietenie> friendRepo = new PrietenieRepository(new PrietenieValidator(),"./data/prietenii.txt");
        Service servo = new Service(userRepo,friendRepo);
        ConsoleUI console = new ConsoleUI(servo);
        console.Run();
    }

    public void lastSeminary()
    {
        Repository<Long, Utilizator> repo = new InMemoryRepository<Long, Utilizator>(new UtilizatorValidator());
        Repository<Long, Utilizator> repoFile = new UtilizatorRepository(new UtilizatorValidator(), "./data/utilizatori.txt");

        for(Utilizator u: repoFile.findAll())
            System.out.println(u);

        Utilizator u1 = new Utilizator("IONUT2", "a");
        Utilizator u2 = new Utilizator("Mihai2", "b");
        Utilizator u3 = null;
        u1.setId(4L);
        u2.setId(5L);
        try {
            repoFile.save(u1);
            repoFile.save(u2);
            repoFile.save(u3);
        }catch(IllegalArgumentException | ValidationException e)
        {
            System.out.println(e.getMessage());
        }
        System.out.println("Process finished succesfully");
    }
}
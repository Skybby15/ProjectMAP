import Domain.Prietenie;
import Domain.Tuple;
import Domain.Utilizator;
import Domain.Validators.PrietenieValidator;
import Domain.Validators.UtilizatorValidator;
import Domain.Validators.ValidationException;
import Domain.Validators.Validator;
import Repository.DbRepos.PrietenieDbRepo;
import Repository.DbRepos.UtilizatorDbRepo;
import Repository.InFile.PrietenieRepository;
import Repository.InFile.UtilizatorRepository;
import Repository.InMemory.InMemoryRepository;
import Repository.Repository;
import UserInterface.ConsoleUI;
import Service.Service;
import Utils.Passes;

import javax.swing.text.html.Option;
import java.util.Optional;

public class Application {
    public static void main(String[] args) {
        runApplication();
    }

    public static void runApplication()
    {
        Repository<Long,Utilizator> userRepo = new UtilizatorDbRepo("jdbc:postgresql://localhost:5432/ProjectMAP","postgres",Passes.postgresPass);
        Repository<Tuple<Long,Long>, Prietenie> friendRepo = new PrietenieDbRepo("jdbc:postgresql://localhost:5432/ProjectMAP","postgres",Passes.postgresPass);
        Service servo = new Service(userRepo,friendRepo);
        ConsoleUI console = new ConsoleUI(servo);
        console.Run();
    }

    public static void testDbItems()
    {
        Repository<Long,Utilizator> userRepo = new UtilizatorDbRepo("jdbc:postgresql://localhost:5432/ProjectMAP","postgres",Passes.postgresPass);
        userRepo.findAll().forEach(System.out::println);
        System.out.println(userRepo.findLength());
        //Optional<Utilizator> = userRepo.save(new Utilizator("numemeh","prenumemeh"));


    }

    public static void lastSeminary()
    {
        Repository<Long, Utilizator> repo = new InMemoryRepository<Long, Utilizator>(new UtilizatorValidator());
        Repository<Long, Utilizator> repoFile = new UtilizatorRepository(new UtilizatorValidator(), "./data/utilizatori.txt");

        for(Utilizator u: repoFile.findAll())
            System.out.println(u);

        Utilizator u1 = new Utilizator("IONUT2", "a");
        Utilizator u2 = new Utilizator("Mihai2", "b");
        Utilizator u3 = null;
        //u1.setId(4L);
        //u2.setId(5L);
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
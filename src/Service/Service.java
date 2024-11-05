package Service;
import Domain.Prietenie;
import Domain.Tuple;
import Domain.Utilizator;
import Domain.Validators.ValidationException;
import Repository.Repository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import Utils.Graph;

public class Service {
    Repository<Long,Utilizator> userRepo;
    Repository<Tuple<Long,Long>,Prietenie> friendRepo;
    //AtomicLong idGenerator = new AtomicLong(0);

    public Service(Repository<Long,Utilizator> userRepository, Repository<Tuple<Long,Long>,Prietenie> friendRepository)
    {
        this.userRepo = userRepository;
        this.friendRepo = friendRepository;
    }

    public void AdaugaUser(String firstName, String lastName) throws ValidationException
    {
        Utilizator nouUser = new Utilizator(firstName, lastName);
        Optional<Utilizator> salvat = userRepo.save(nouUser);
        if(salvat.isPresent())
            throw new ValidationException("Un utilizator cu acest id este deja salvat!");
    }

    public void StergeUser(long idUser) throws ValidationException
    {//modification needed
        if(userRepo.findOne(idUser).isPresent())
        {
            //stergerea prieteniilor legate de user , trebuie realizat inainte datorita constrangerilor din sql
            ArrayList<Tuple<Long,Long>> List = new ArrayList<>();

            friendRepo.findAll().forEach(f ->{
                if(f.getId().getLeft() == idUser || f.getId().getRight() == idUser)
                {
                    List.add(new Tuple<>(f.getId().getLeft(),f.getId().getRight()));
                }
            });

            List.forEach(f->{
                friendRepo.delete(f);
            });

            //apoi se sterge user-ul
            Optional<Utilizator> deleted = userRepo.delete(idUser);
        }else // altfel nu s-a gasit
            throw new ValidationException("Utilizatorul cu acest id nu exista!");

    }

    public void AdaugaPrietenie(long idUser1,long idUser2) throws ValidationException
    {
        AtomicBoolean ok1 = new AtomicBoolean(false);  // verific daca id urile date apartin unor utilizatori
        AtomicBoolean ok2 = new AtomicBoolean(false);
        userRepo.findAll().forEach(u->
        {
            if(u.getId() == idUser1)
                ok1.set(true);

            if(u.getId() == idUser2)
                ok2.set(true);

        });

        if(!ok1.get() || !ok2.get())
        {
            throw new ValidationException("Ambele id uri trebuie sa apartina unui utilizator!");
        }

        Prietenie deSalvat = new Prietenie(LocalDateTime.now());
        if(idUser1 < idUser2) // mentinem ordinea de id crescatoare pentru a nu se putea introduce aceeasi utilizatori ca 2 prietenii
            deSalvat.setId(new Tuple<>(idUser1,idUser2));
        else
            deSalvat.setId(new Tuple<>(idUser2,idUser1));


        Optional<Prietenie> savedF = friendRepo.save(deSalvat);
        if(savedF.isPresent())
            throw new ValidationException("Prietenia intre acesti useri deja exista!");
    }

    public void StergePrietenie(Tuple<Long,Long> idFriendship) throws IllegalArgumentException
    {
        Optional<Prietenie> deletedF = friendRepo.delete(idFriendship);
        if(deletedF.isEmpty())
            throw new ValidationException("Prietenia cu acest id nu exista!");
    }

    public Iterable<Utilizator> GetUseri()
    {
        return (Iterable<Utilizator>) userRepo.findAll();
    }

    public Iterable<Prietenie> GetPrietenii()
    {
        return (Iterable<Prietenie>) friendRepo.findAll();
    }

    public ArrayList<ArrayList<Long>> GetComunitati()
    {

        Graph grafComunitati = new Graph(userRepo.findLength());

        grafComunitati.addVertices(userRepo.findAll());

        friendRepo.findAll().forEach(friendship->
        {
            long stanga = friendship.getId().getLeft();
            long dreapta = friendship.getId().getRight();

            grafComunitati.addEdge(stanga,dreapta);
        });

        return grafComunitati.connectedComponents();
    }

    public ArrayList<Long> ComunitateSociabila()
    {
        int longestListPos = 0;
        int maxElements = 0;

        int currentPos = -1;
        for(var list : GetComunitati())
        {
            currentPos++;
            if (list.size() > maxElements)
            {
                maxElements = list.size();
                longestListPos = currentPos;
            }
        }

        return GetComunitati().get(longestListPos);
    }
}

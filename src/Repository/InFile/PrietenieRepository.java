package Repository.InFile;

import Domain.Tuple;
import Domain.Validators.Validator;
import Domain.Prietenie;

import java.time.LocalDateTime;

public class PrietenieRepository extends AbstractFileRepository<Tuple<Long,Long>,Prietenie> {
    public PrietenieRepository(Validator<Prietenie> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public Prietenie createEntity(String line)
    {
        String[] split = line.split(";");

        Prietenie prietenie = new Prietenie(LocalDateTime.parse(split[2]));
        Tuple<Long,Long> Id = new Tuple<>(Long.parseLong(split[0]),Long.parseLong(split[1]));
        prietenie.setId(Id);

        return prietenie;
    }
    public String saveEntity(Prietenie entity)
    {
        Long stanga = entity.getId().getLeft();
        Long dreapta = entity.getId().getRight();
        LocalDateTime data = entity.getDate();

        return stanga.toString() + ";" + dreapta.toString() + ";" + data.toString();
    }

}

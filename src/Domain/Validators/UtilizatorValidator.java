package Domain.Validators;

import Domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if (entity.getFirstName().isEmpty() || entity.getLastName().isEmpty()) {
            throw new ValidationException("Numele utilizatorului nu poate fi gol!");
        }else
            for(Character ch : entity.getFirstName().concat(entity.getLastName()).toCharArray())
            {
                if("1234567890".contains(ch.toString()))
                {
                    throw new ValidationException("Numele utilizatorului nu poate contine numere!");
                }
            }
    }
}

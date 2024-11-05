package Domain.Validators;

import Domain.Prietenie;

public class PrietenieValidator implements Validator<Prietenie>{

    @Override
    public void validate(Prietenie entity) throws ValidationException{
        if(entity.getId().getLeft().equals(entity.getId().getRight())){
            throw new ValidationException("Prietenia trebuie realizata intre 2 utilizatori diferiti!");
        }
    }
}

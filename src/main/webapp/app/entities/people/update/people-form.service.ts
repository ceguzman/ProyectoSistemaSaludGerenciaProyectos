import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPeople, NewPeople } from '../people.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPeople for edit and NewPeopleFormGroupInput for create.
 */
type PeopleFormGroupInput = IPeople | PartialWithRequiredKeyOf<NewPeople>;

type PeopleFormDefaults = Pick<NewPeople, 'id'>;

type PeopleFormGroupContent = {
  id: FormControl<IPeople['id'] | NewPeople['id']>;
  documentNumber: FormControl<IPeople['documentNumber']>;
  firstName: FormControl<IPeople['firstName']>;
  firstSurname: FormControl<IPeople['firstSurname']>;
  secondName: FormControl<IPeople['secondName']>;
  secondSurname: FormControl<IPeople['secondSurname']>;
  typeDocument: FormControl<IPeople['typeDocument']>;
  typeSpecialist: FormControl<IPeople['typeSpecialist']>;
};

export type PeopleFormGroup = FormGroup<PeopleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PeopleFormService {
  createPeopleFormGroup(people: PeopleFormGroupInput = { id: null }): PeopleFormGroup {
    const peopleRawValue = {
      ...this.getFormDefaults(),
      ...people,
    };
    return new FormGroup<PeopleFormGroupContent>({
      id: new FormControl(
        { value: peopleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentNumber: new FormControl(peopleRawValue.documentNumber, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      firstName: new FormControl(peopleRawValue.firstName, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      firstSurname: new FormControl(peopleRawValue.firstSurname, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      secondName: new FormControl(peopleRawValue.secondName, {
        validators: [Validators.maxLength(50)],
      }),
      secondSurname: new FormControl(peopleRawValue.secondSurname, {
        validators: [Validators.maxLength(50)],
      }),
      typeDocument: new FormControl(peopleRawValue.typeDocument, {
        validators: [Validators.required],
      }),
      typeSpecialist: new FormControl(peopleRawValue.typeSpecialist, {
        validators: [Validators.required],
      }),
    });
  }

  getPeople(form: PeopleFormGroup): IPeople | NewPeople {
    return form.getRawValue() as IPeople | NewPeople;
  }

  resetForm(form: PeopleFormGroup, people: PeopleFormGroupInput): void {
    const peopleRawValue = { ...this.getFormDefaults(), ...people };
    form.reset(
      {
        ...peopleRawValue,
        id: { value: peopleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PeopleFormDefaults {
    return {
      id: null,
    };
  }
}

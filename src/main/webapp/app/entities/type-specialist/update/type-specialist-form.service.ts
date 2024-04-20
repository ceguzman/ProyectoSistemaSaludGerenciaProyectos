import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeSpecialist, NewTypeSpecialist } from '../type-specialist.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeSpecialist for edit and NewTypeSpecialistFormGroupInput for create.
 */
type TypeSpecialistFormGroupInput = ITypeSpecialist | PartialWithRequiredKeyOf<NewTypeSpecialist>;

type TypeSpecialistFormDefaults = Pick<NewTypeSpecialist, 'id'>;

type TypeSpecialistFormGroupContent = {
  id: FormControl<ITypeSpecialist['id'] | NewTypeSpecialist['id']>;
  specialistType: FormControl<ITypeSpecialist['specialistType']>;
  stateSpecialist: FormControl<ITypeSpecialist['stateSpecialist']>;
};

export type TypeSpecialistFormGroup = FormGroup<TypeSpecialistFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeSpecialistFormService {
  createTypeSpecialistFormGroup(typeSpecialist: TypeSpecialistFormGroupInput = { id: null }): TypeSpecialistFormGroup {
    const typeSpecialistRawValue = {
      ...this.getFormDefaults(),
      ...typeSpecialist,
    };
    return new FormGroup<TypeSpecialistFormGroupContent>({
      id: new FormControl(
        { value: typeSpecialistRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      specialistType: new FormControl(typeSpecialistRawValue.specialistType, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      stateSpecialist: new FormControl(typeSpecialistRawValue.stateSpecialist, {
        validators: [Validators.required],
      }),
    });
  }

  getTypeSpecialist(form: TypeSpecialistFormGroup): ITypeSpecialist | NewTypeSpecialist {
    return form.getRawValue() as ITypeSpecialist | NewTypeSpecialist;
  }

  resetForm(form: TypeSpecialistFormGroup, typeSpecialist: TypeSpecialistFormGroupInput): void {
    const typeSpecialistRawValue = { ...this.getFormDefaults(), ...typeSpecialist };
    form.reset(
      {
        ...typeSpecialistRawValue,
        id: { value: typeSpecialistRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TypeSpecialistFormDefaults {
    return {
      id: null,
    };
  }
}

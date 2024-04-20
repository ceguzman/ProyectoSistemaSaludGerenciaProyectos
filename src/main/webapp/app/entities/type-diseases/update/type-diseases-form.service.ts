import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeDiseases, NewTypeDiseases } from '../type-diseases.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeDiseases for edit and NewTypeDiseasesFormGroupInput for create.
 */
type TypeDiseasesFormGroupInput = ITypeDiseases | PartialWithRequiredKeyOf<NewTypeDiseases>;

type TypeDiseasesFormDefaults = Pick<NewTypeDiseases, 'id'>;

type TypeDiseasesFormGroupContent = {
  id: FormControl<ITypeDiseases['id'] | NewTypeDiseases['id']>;
  diseasesType: FormControl<ITypeDiseases['diseasesType']>;
};

export type TypeDiseasesFormGroup = FormGroup<TypeDiseasesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeDiseasesFormService {
  createTypeDiseasesFormGroup(typeDiseases: TypeDiseasesFormGroupInput = { id: null }): TypeDiseasesFormGroup {
    const typeDiseasesRawValue = {
      ...this.getFormDefaults(),
      ...typeDiseases,
    };
    return new FormGroup<TypeDiseasesFormGroupContent>({
      id: new FormControl(
        { value: typeDiseasesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      diseasesType: new FormControl(typeDiseasesRawValue.diseasesType, {
        validators: [Validators.required, Validators.maxLength(200)],
      }),
    });
  }

  getTypeDiseases(form: TypeDiseasesFormGroup): ITypeDiseases | NewTypeDiseases {
    return form.getRawValue() as ITypeDiseases | NewTypeDiseases;
  }

  resetForm(form: TypeDiseasesFormGroup, typeDiseases: TypeDiseasesFormGroupInput): void {
    const typeDiseasesRawValue = { ...this.getFormDefaults(), ...typeDiseases };
    form.reset(
      {
        ...typeDiseasesRawValue,
        id: { value: typeDiseasesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TypeDiseasesFormDefaults {
    return {
      id: null,
    };
  }
}
